


---- AutoshipItem Validation.

--SELECT * FROM datamigration..dm_log
--WHERE test_area='824-AutoshipItem'

----SELECT * FROM datamigration..map_tab
----WHERE [owner]='824-AutoshipItem'




USE RFOperations;
SET STATISTICS TIME ON;
GO

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

DECLARE @HYB_key VARCHAR(100) = 'code';
DECLARE @RFO_key VARCHAR(100) = 'AutoshipID';
DECLARE @sql_gen_1 NVARCHAR(MAX);
DECLARE @sql_gen_2 NVARCHAR(MAX);
DECLARE @cnt INT;
DECLARE @lt_1 INT;
DECLARE @lt_2 INT;
DECLARE @temp TABLE
    (
      test_area VARCHAR(MAX) ,
      test_type VARCHAR(MAX) ,
      rfo_column VARCHAR(MAX) ,
      hybris_column VARCHAR(MAX) ,
      hyb_key VARCHAR(MAX) ,
      hyb_value VARCHAR(MAX) ,
      rfo_key VARCHAR(MAX) ,
      rfo_value VARCHAR(MAX)
    );

----Validation of AUTOSHIP Counts, Dups & Columns without transformations

--Counts check on Hybris side for US
IF OBJECT_ID('tempdb..#DuplicateAutoship') IS NOT NULL
    DROP TABLE #DuplicateAutoship;

SELECT  AutoshipID
INTO    #DuplicateAutoship  ---Loading Duplicates Autoship into Temp Table.425 records 
FROM    Hybris.Autoship
WHERE   AccountID IN (
        SELECT  a.AccountID
        FROM    Hybris.Autoship a
                INNER JOIN RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
        WHERE   ab.AccountTypeID = 1
                AND a.CountryID = 236
                AND a.AutoshipTypeID = 2
                AND a.Active = 1
        GROUP BY a.AccountID
        HAVING  COUNT(*) > 1 )
        AND Active = 1
        AND AutoshipTypeID = 2--total 809
EXCEPT
SELECT  MAX(AutoshipID) AutoshipID-- INTO #maxautoship
FROM    Hybris.Autoship a
        INNER JOIN RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
WHERE   ab.AccountTypeID = 1
        AND a.CountryID = 236
        AND a.AutoshipTypeID = 2
        AND a.Active = 1
GROUP BY a.AccountID
HAVING  COUNT(*) > 1;
		 --total 386



IF OBJECT_ID('tempdb..#LoadedAutoshipID') IS NOT NULL
    DROP TABLE #LoadedAutoshipID;

SELECT    DISTINCT
        a.AutoshipID
INTO    #LoadedAutoshipID
FROM    RFOperations.Hybris.Autoship (NOLOCK) a
        INNER JOIN RFOperations.Hybris.AutoshipItem (NOLOCK) ai ON ai.AutoshipId = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipPayment (NOLOCK) ap ON ap.AutoshipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipShipment (NOLOCK) ash ON ash.AutoshipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipPaymentAddress (NOLOCK) apa ON apa.AutoShipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) asha ON asha.AutoShipID = a.AutoshipID
        INNER JOIN Hybris.dbo.users u ON a.AccountID = u.p_rfaccountid
                                        --AND u.p_sourcename = 'Hybris-DM'
WHERE   a.CountryID = 236
        AND a.AutoshipID NOT IN ( SELECT    AutoshipID
                                  FROM      #DuplicateAutoship );----3162608




								  
IF OBJECT_ID('tempdb..#extra') IS NOT NULL
    DROP TABLE #extra;

SELECT  ho.code
INTO    #extra
FROM    Hybris..orders ho
        JOIN Hybris..users u ON u.PK = ho.userpk
                                AND ho.p_template = 1
                                AND u.p_sourcename = 'Hybris-DM'
        JOIN Hybris..countries c ON c.PK = u.p_country
                                    AND c.isocode = 'US'
        LEFT JOIN #LoadedAutoshipID lo ON lo.AutoshipID = ho.code
WHERE   lo.AutoshipID IS NULL; 







--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    COUNT(*) cnt ,
                    d.orderpk ,
                    d.productpk ,
                    d.[entrynumber]
          FROM      Hybris.dbo.orders a ,
                    Hybris.dbo.users b ,
                    Hybris.dbo.countries c ,
                    Hybris.dbo.orderentries d
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND a.PK = d.orderpk
                    AND c.isocode = 'US'
                    AND p_sourcename = 'Hybris-DM'
                    AND a.p_template = 1 --AS
GROUP BY            d.orderpk ,
                    d.productpk ,
                    d.[entrynumber]
          HAVING    COUNT(*) > 1
        ) t1;

--Counts check on Hybris side for US
SELECT  hybris_cnt ,
        rfo_cnt ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(DISTINCT d.PK) hybris_cnt
          FROM      Hybris.dbo.orders a ,
                    Hybris.dbo.users b ,
                    Hybris.dbo.countries c ,
                    Hybris.dbo.orderentries d
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND a.PK = d.orderpk
                    AND c.isocode = 'US'
                    AND a.p_template = 1
                    AND p_sourcename = 'Hybris-DM'
					AND a.code NOT IN (SELECT code FROM #extra)
        ) t1 , --1710792
        ( SELECT    SUM(cnt) rfo_cnt
          FROM      ( SELECT    COUNT(c.AutoshipItemID) cnt ,
                                a.AutoshipID ,
                                LineItemNo ,
                                ProductID rfo_cnt
                      FROM      RFOperations.Hybris.Autoship a ,
                                Hybris.dbo.users b ,
                                RFOperations.Hybris.AutoshipItem c ,
                                #LoadedAutoshipID d
                      WHERE     a.AccountID = b.p_rfaccountid
                                AND a.AutoshipID = c.AutoshipId
                                AND a.AutoshipID = d.AutoshipID
                                AND CountryID = 236
                                AND p_sourcename = 'Hybris-DM'
                      GROUP BY  a.AutoshipID ,
                                LineItemNo ,
                                ProductID
                    ) --	having count(*) = 1)
                    a
        ) t2;
 --1712145
	

--Column2Column Validation that doesn't have transformation - Autoship

DELETE  FROM DataMigration.dbo.dm_log
WHERE   test_area = '824-AutoshipItem';


IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
    DROP TABLE #tempact;

SELECT  a.AutoshipID ,
        AutoshipNumber ,
        b.PK ,
        c.ProductID ,
        c.LineItemNo ,
        CAST(c.TotalTax AS FLOAT) AS [totaltax]
INTO    #tempact
FROM    RFOperations.Hybris.Autoship a ,
        Hybris.dbo.users b ,
        RFOperations.Hybris.AutoshipItem c ,
        #LoadedAutoshipID d
WHERE   a.AccountID = b.p_rfaccountid
        AND a.AutoshipID = c.AutoshipId
        AND a.AutoshipID = d.AutoshipID
        AND CountryID = 236
       -- AND p_sourcename = 'Hybris-DM'
--and a.autoshipid not in (8809794175021, 8816660840493) --offshore team updated these values
       -- AND b.modifiedTS <= '2015-07-14 06:00:00.000'
        AND a.AutoshipNumber NOT IN ( SELECT    OrderNumber
                                      FROM      Hybris.Orders
                                      WHERE     CountryID = 236 )
GROUP BY a.AutoshipID ,
        AutoshipNumber ,
        b.PK ,
        c.ProductID ,
        c.LineItemNo ,
        c.TotalTax;

CREATE CLUSTERED INDEX as_cls1 ON #tempact (AutoshipID);
CREATE NONCLUSTERED COLUMNSTORE INDEX as_cls2 ON #tempact (AutoshipNumber);


SELECT  'Validation of column to column with no transformation in progress' AS [Step-2 Validation] ,
        GETDATE() AS StartTime;
SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'c2c'
        AND [Hybris_Column] <> @HYB_key
		AND id NOT IN (89)--autoshipID
        AND [owner] = '824-AutoshipItem';

WHILE @cnt <= @lt_1
    BEGIN

        SELECT @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''' as rfo_column, '''
                + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                + ' as hyb_key, A.' + Hybris_Column + ' as hyb_value, B.'
                + @RFO_key + ' as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ', t.* 
																from hybris.dbo.' + Hybris_Table + ' t, hybris.dbo.orders b
																		where t.orderpk=b.pk 
																			and p_template = 1 and currencypk = 8796125855777) a
except
SELECT b.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT b.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ', t.* 
																from hybris.dbo.' + Hybris_Table + ' t, hybris.dbo.orders b
																		where t.orderpk=b.pk 
																			and p_template = 1 and currencypk = 8796125855777) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.' + Hybris_Column + ', B.' + @RFO_key
                + ',B.RFO_Col

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.*
																from hybris.dbo.' + Hybris_Table + ' t, hybris.dbo.orders b
																		where t.orderpk=b.pk 
																			and p_template = 1 and currencypk = 8796125855777) a
except
SELECT b.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT b.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ', t.* 
																from hybris.dbo.' + Hybris_Table + ' t, hybris.dbo.orders b
																		where t.orderpk=b.pk 
																			and p_template = 1 and currencypk = 8796125855777) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + ''
        FROM    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'c2c'
                            AND [Hybris_Column] <> @HYB_key
							AND [RFO_Column ]<>@RFO_key
							AND id NOT IN (89)--autoshipID
                            AND [owner] = '824-AutoshipItem'
                ) temp
        WHERE   rn = @cnt; 

        PRINT @sql_gen_1;
        INSERT  INTO @temp
                ( test_area ,
                  test_type ,
                  rfo_column ,
                  hybris_column ,
                  hyb_key ,
                  hyb_value ,
                  rfo_key ,
                  rfo_value
                )
                EXEC sp_executesql @sql_gen_1;

        IF ( SELECT COUNT(*)
             FROM   @temp
           ) > 1
            BEGIN
                DECLARE @err_cnt INT;
                SELECT  @err_cnt = CASE WHEN hyb_cnt = 0 THEN rfo_cnt
                                        ELSE hyb_cnt
                                   END
                FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
                                    COUNT(DISTINCT rfo_key) rfo_cnt
                          FROM      @temp
                        ) t1;

                UPDATE  a
                SET     [prev_run_err] = @err_cnt
                FROM    DataMigration.dbo.map_tab a ,
                        @temp b
                WHERE   a.hybris_column = b.hybris_column
                        AND [owner] = '824-AutoshipItem'; 
            END;	

        INSERT  INTO DataMigration.dbo.dm_log
                SELECT TOP 5
                        test_area ,
                        test_type ,
                        rfo_column ,
                        hybris_column ,
                        hyb_key ,
                        hyb_value ,
                        rfo_key ,
                        rfo_value
                FROM    @temp
                WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key, '~') )
                          AND ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                        )
                UNION
                SELECT TOP 5
                        test_area ,
                        test_type ,
                        rfo_column ,
                        hybris_column ,
                        hyb_key ,
                        hyb_value ,
                        rfo_key ,
                        rfo_value
                FROM    @temp
                WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key, '~') )
                          OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                        );


        DELETE  FROM @temp;

        SET @cnt = @cnt + 1;

    END;

UPDATE  DataMigration.dbo.map_tab
SET     [prev_run_err] = 0
WHERE   [owner] = '824-AutoshipItem'
        AND flag = 'c2c'
        AND hybris_column NOT IN ( SELECT DISTINCT
                                            hybris_column
                                   FROM     DataMigration..dm_log
                                   WHERE    test_area = '824-AutoshipItem'
                                            AND test_type = 'c2c' );


SELECT  'Step-2 Completed, Validation of default columns in progress' AS [Step-3 Validation] ,
        GETDATE() AS StartTime;

----Defaults Check
SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'defaults'
        AND [owner] = '824-AutoshipItem'
        AND [RFO_Reference Table] = 'NULL';
SELECT  @lt_2 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'defaults'
        AND [owner] = '824-AutoshipItem'
        AND [RFO_Reference Table] <> 'NULL';

WHILE ( @cnt <= @lt_1
        AND @cnt <= @lt_2
      )
    BEGIN
        IF ( SELECT COUNT(*)
             FROM   DataMigration.dbo.map_tab
             WHERE  flag = 'defaults'
                    AND [owner] = '824-AutoshipItem'
                    AND [RFO_Reference Table] = 'NULL'
           ) > 1
            BEGIN
                SELECT  @sql_gen_1 = 'use rfoperations
		select distinct ''' + [owner] + ''' as test_area, ''' + flag
                        + ''' as test_type, null as rfo_column, '''
                        + Hybris_Column + ''' as hybris_column, a.' + @HYB_key
                        + ', ' + hybris_column
                        + ', null as rfo_key, null as rfo_value
		FROM (select b.'+ @HYB_key + ' , t.*
																from hybris.dbo.' + Hybris_Table + ' t, hybris.dbo.orders b
																		where t.orderpk=b.pk 
																			and p_template = 1 and currencypk = 8796125855777) a
		where ' + hybris_column + ' is not null'
                FROM    ( SELECT    * ,
                                    ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                          FROM      DataMigration.dbo.map_tab
                          WHERE     flag = 'defaults'
                                    AND [RFO_Reference Table] = 'NULL'
                                    AND [owner] = '824-AutoshipItem'
                        ) temp
                WHERE   rn = @cnt;
            END;

        PRINT @sql_gen_1;
        INSERT  INTO @temp
                ( test_area ,
                  test_type ,
                  rfo_column ,
                  hybris_column ,
                  hyb_key ,
                  hyb_value ,
                  rfo_key ,
                  rfo_value
                )
                EXEC sp_executesql @sql_gen_1;

        IF ( SELECT COUNT(*)
             FROM   @temp
           ) > 1
            BEGIN
		--declare @err_cnt int
                SELECT  @err_cnt = CASE WHEN hyb_cnt = 0 THEN rfo_cnt
                                        ELSE hyb_cnt
                                   END
                FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
                                    COUNT(DISTINCT rfo_key) rfo_cnt
                          FROM      @temp
                        ) t1;

                UPDATE  a
                SET     [prev_run_err] = @err_cnt
                FROM    DataMigration.dbo.map_tab a ,
                        @temp b
                WHERE   a.hybris_column = b.hybris_column
                        AND [owner] = '824-AutoshipItem'; 
            END;	

        INSERT  INTO DataMigration.dbo.dm_log
                SELECT TOP 5
                        test_area ,
                        test_type ,
                        rfo_column ,
                        hybris_column ,
                        hyb_key ,
                        hyb_value ,
                        rfo_key ,
                        rfo_value
                FROM    @temp
                WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key, '~') )
                          AND ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                        )
                UNION
                SELECT TOP 5
                        test_area ,
                        test_type ,
                        rfo_column ,
                        hybris_column ,
                        hyb_key ,
                        hyb_value ,
                        rfo_key ,
                        rfo_value
                FROM    @temp
                WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key, '~') )
                          OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                        );

	--select * from datamigration.dbo.dm_log where test_type = 'defaults'

        IF ( SELECT COUNT(*)
             FROM   DataMigration.dbo.map_tab
             WHERE  flag = 'defaults'
                    AND [owner] = '824-AutoshipItem'
                    AND [RFO_Reference Table] <> 'NULL'
           ) > 1
            BEGIN
                SELECT  @sql_gen_2 = 'use rfoperations
		select distinct ''' + [owner] + ''' as test_area, ''' + flag
                        + ''' as test_type, null as rfo_column, '''
                        + Hybris_Column + ''' as hybris_column, a.' + @HYB_key
                        + ', ' + hybris_column
                        + ', null as rfo_key, null as rfo_value
		FROM (select b.'+ @HYB_key + ' , t.*
																from hybris.dbo.' + Hybris_Table + ' t, hybris.dbo.orders b
																		where t.orderpk=b.pk 
																			and p_template = 1 and currencypk = 8796125855777) a
		where ' + hybris_column + ' <> ''' + [RFO_Reference Table] + ''''
                FROM    ( SELECT    * ,
                                    ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                          FROM      DataMigration.dbo.map_tab
                          WHERE     flag = 'defaults'
                                    AND [RFO_Reference Table] <> 'NULL'
                                    AND [owner] = '824-AutoshipItem'
                        ) temp
                WHERE   rn = @cnt;
            END;

        PRINT @sql_gen_2;
        INSERT  INTO @temp
                ( test_area ,
                  test_type ,
                  rfo_column ,
                  hybris_column ,
                  hyb_key ,
                  hyb_value ,
                  rfo_key ,
                  rfo_value
                )
                EXEC sp_executesql @sql_gen_2;

        IF ( SELECT COUNT(*)
             FROM   @temp
           ) > 1
            BEGIN
		--declare @err_cnt int
                SELECT  @err_cnt = CASE WHEN hyb_cnt = 0 THEN rfo_cnt
                                        ELSE hyb_cnt
                                   END
                FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
                                    COUNT(DISTINCT rfo_key) rfo_cnt
                          FROM      @temp
                        ) t1;

                UPDATE  a
                SET     [prev_run_err] = @err_cnt
                FROM    DataMigration.dbo.map_tab a ,
                        @temp b
                WHERE   a.hybris_column = b.hybris_column
                        AND [owner] = '824-AutoshipItem'; 
            END;	

        INSERT  INTO DataMigration.dbo.dm_log
                SELECT TOP 5
                        test_area ,
                        test_type ,
                        rfo_column ,
                        hybris_column ,
                        hyb_key ,
                        hyb_value ,
                        rfo_key ,
                        rfo_value
                FROM    @temp
                WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key, '~') )
                          AND ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                        )
                UNION
                SELECT TOP 5
                        test_area ,
                        test_type ,
                        rfo_column ,
                        hybris_column ,
                        hyb_key ,
                        hyb_value ,
                        rfo_key ,
                        rfo_value
                FROM    @temp
                WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key, '~') )
                          OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                        );

        DELETE  FROM @temp;

        SET @cnt = @cnt + 1;

    END;

UPDATE  DataMigration.dbo.map_tab
SET     [prev_run_err] = 0
WHERE   [owner] = '824-AutoshipItem'
        AND flag = 'defaults'
        AND hybris_column NOT IN ( SELECT DISTINCT
                                            hybris_column
                                   FROM     DataMigration..dm_log
                                   WHERE    test_area = '824-AutoshipItem'
                                            AND test_type = 'defaults' );



SELECT  'Step-3 completed, Validation of transformed columns in progress' AS [Step-4 Validation] ,
        GETDATE() AS StartTime;

--Transformed Columns Validation --10:16 mins
SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'manual'
        AND [Hybris_Column] <> @HYB_key
        AND [owner] = '824-AutoshipItem';

WHILE @cnt <= @lt_1
    BEGIN

        SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner]
                + ''' as test_area, ''' + flag + ''' as test_type, '''
                + [RFO_Reference Table] + ''' as rfo_column, '''
                + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                + ' as hyb_key, A.Hyb_Trans_col as hyb_value, B.' + @RFO_key
                + ' as rfo_key, B.RFO_Trans_Col as rfo_value

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.*
																from hybris.dbo.' + Hybris_Table + ' t, hybris.dbo.orders b
																		where t.orderpk=b.pk 
																			and p_template = 1 and currencypk = 8796125855777) a
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + ') A  

LEFT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ', t.* 
																from hybris.dbo.' + Hybris_Table + ' t, hybris.dbo.orders b
																		where t.orderpk=b.pk 
																			and p_template = 1 and currencypk = 8796125855777) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT  ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.Hyb_Trans_col, B.' + @RFO_key
                + ', B.RFO_Trans_Col

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.*
																from hybris.dbo.' + Hybris_Table + ' t, hybris.dbo.orders b
																		where t.orderpk=b.pk 
																			and p_template = 1 and currencypk = 8796125855777) a
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + ') A  

RIGHT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.*
																from hybris.dbo.' + Hybris_Table + ' t, hybris.dbo.orders b
																		where t.orderpk=b.pk 
																			and p_template = 1 and currencypk = 8796125855777) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + ''
        FROM    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'manual'
                            AND rfo_column <> @RFO_key
                            AND id NOT IN ( 69, 70 ) --Data types are image on Hybris end. This value is generated by the system
                           -- AND id NOT IN ( 3, 4, 64, 65 ) --order not migrated yet
                            AND [owner] = '824-AutoshipItem'
                ) temp
        WHERE   rn = @cnt;

        PRINT @sql_gen_1;
        INSERT  INTO @temp
                ( test_area ,
                  test_type ,
                  rfo_column ,
                  hybris_column ,
                  hyb_key ,
                  hyb_value ,
                  rfo_key ,
                  rfo_value
                )
                EXEC sp_executesql @sql_gen_1;


        IF ( SELECT COUNT(*)
             FROM   @temp
           ) > 1
            BEGIN
	--declare @err_cnt int
                SELECT  @err_cnt = CASE WHEN hyb_cnt = 0 THEN rfo_cnt
                                        ELSE hyb_cnt
                                   END
                FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
                                    COUNT(DISTINCT rfo_key) rfo_cnt
                          FROM      @temp
                        ) t1;

                UPDATE  a
                SET     [prev_run_err] = @err_cnt
                FROM    DataMigration.dbo.map_tab a ,
                        @temp b
                WHERE   a.hybris_column = b.hybris_column
                        AND [owner] = '824-AutoshipItem'; 
            END;	

        INSERT  INTO DataMigration..dm_log
                SELECT TOP 5
                        test_area ,
                        test_type ,
                        rfo_column ,
                        hybris_column ,
                        hyb_key ,
                        hyb_value ,
                        rfo_key ,
                        rfo_value
                FROM    @temp
                WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key, '~') )
                          AND ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                        )
                UNION
                SELECT TOP 5
                        test_area ,
                        test_type ,
                        rfo_column ,
                        hybris_column ,
                        hyb_key ,
                        hyb_value ,
                        rfo_key ,
                        rfo_value
                FROM    @temp
                WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key, '~') )
                          OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                        );

        UPDATE  DataMigration.dbo.map_tab
        SET     [prev_run_err] = 0
        WHERE   [owner] = '824-AutoshipItem'
                AND flag = 'manual'
                AND hybris_column NOT IN (
                SELECT DISTINCT
                        hybris_column
                FROM    DataMigration..dm_log
                WHERE   test_area = '824-AutoshipItem'
                        AND test_type = 'manual' );

        DELETE  FROM @temp;

        SET @cnt = @cnt + 1;

    END;

SELECT  'VALIDATION COMPLETED' [Status] ,
        [total no of columns] ,
        [columns passed] ,
        [total no of columns] - [columns passed] AS [Required Analysis] ,
        GETDATE() AS EndTime
FROM    ( SELECT    COUNT(cnt) AS [columns passed]
          FROM      ( SELECT DISTINCT
                                hybris_column AS cnt
                      FROM      DataMigration.dbo.map_tab
                      WHERE     [owner] = '824-AutoshipItem'
                                AND flag IN ( 'c2c', 'manual', 'defaults' )
                      EXCEPT
                      SELECT DISTINCT
                                hybris_column
                      FROM      DataMigration..dm_log
                      WHERE     test_area = '824-AutoshipItem'
                    ) a
        ) tab1 ,
        ( SELECT    COUNT(id) AS [total no of columns]
          FROM      DataMigration.dbo.map_tab
          WHERE     [owner] = '824-AutoshipItem'
                    AND flag IN ( 'c2c', 'manual', 'defaults' )
        ) tab2;

SET STATISTICS TIME OFF;
GO

