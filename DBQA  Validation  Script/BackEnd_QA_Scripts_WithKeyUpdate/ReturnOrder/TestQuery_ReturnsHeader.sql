

--SELECT * FROM datamigration..dm_log
--WHERE  test_area='853-Returns'

USE RFOperations;
SET STATISTICS TIME ON;
GO

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

DECLARE @HYB_key VARCHAR(100) = 'p_returnorder';
DECLARE @RFO_key VARCHAR(100) = 'returnorderid';
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



--Counts check on Hybris side for US
SELECT  CASE WHEN ( SELECT  COUNT(*)
                    FROM    ( SELECT    a.PK
                              FROM      Hybris.dbo.orders (NOLOCK) a ,
                                        Hybris.dbo.users (NOLOCK) b ,
                                        Hybris.dbo.countries (NOLOCK) c
                              WHERE     a.userpk = b.PK
                                        AND b.p_country = c.PK
                                        AND c.isocode = 'US'
                                        AND a.p_template IS NULL
                                        AND a.TypePkString = 8796127723602 --Returns
                                        AND p_sourcename = 'Hybris-DM'
                              EXCEPT
                              SELECT    p_returnorder
                              FROM      Hybris..returnrequest(NOLOCK)
                              WHERE     p_returnorder IN (
                                        SELECT  a.PK
                                        FROM    Hybris.dbo.orders (NOLOCK) a ,
                                                Hybris.dbo.users (NOLOCK) b ,
                                                Hybris.dbo.countries (NOLOCK) c
                                        WHERE   a.userpk = b.PK
                                                AND b.p_country = c.PK
                                                AND c.isocode = 'US'
                                                AND a.p_template IS NULL
                                                AND a.TypePkString = 8796127723602
                                                AND p_sourcename = 'Hybris-DM' )
                            ) a
                  ) > 0
             THEN 'Count Comparison between ReturnOrder & ReturnRequest - Failed!'
             ELSE 'Count Comparison between ReturnOrder & ReturnRequest - Passed'
        END Results ,
        CASE WHEN ( SELECT  COUNT(*)
                    FROM    Hybris.dbo.orders (NOLOCK) a ,
                            Hybris.dbo.users (NOLOCK) b ,
                            Hybris.dbo.countries (NOLOCK) c
                    WHERE   a.userpk = b.PK
                            AND b.p_country = c.PK
                            AND c.isocode = 'US'
                            AND a.p_template IS NULL
                            AND a.TypePkString = 8796127723602
                    GROUP BY a.code
                    HAVING  COUNT(*) > 1
                  ) IS NULL
             THEN 'No Duplicates Return Order-Validation Passed'
             ELSE 'Duplicate Order Returns-Validation failled'
        END [Step-1 Validation] ,
        CASE WHEN ( SELECT  COUNT(*)
                    FROM    Hybris.dbo.orders (NOLOCK) a ,
                            Hybris.dbo.users (NOLOCK) b ,
                            Hybris.dbo.countries (NOLOCK) c ,
                            Hybris.dbo.returnrequest (NOLOCK) d
                    WHERE   a.userpk = b.PK
                            AND b.p_country = c.PK
                            AND a.PK = d.p_returnorder
                            AND c.isocode = 'US'
                            AND a.p_template IS NULL
                            AND a.TypePkString = 8796127723602
                    GROUP BY p_returnorder ,
                            p_order
                    HAVING  COUNT(*) > 1
                  ) IS NULL
             THEN 'No duplicates in Return Request - Validation Passed'
             ELSE 'Duplicates Found'
        END [Step-1 Validation] ,
        hybris_cnt ,
        rfo_cnt ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(a.PK) hybris_cnt
          FROM      Hybris.dbo.orders (NOLOCK) a ,
                    Hybris.dbo.users (NOLOCK) b ,
                    Hybris.dbo.countries (NOLOCK) c
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND c.isocode = 'US'
                    AND ISNULL(a.p_template, 0) = 0
                    AND a.TypePkString = 8796127723602 --Returns
                    AND p_sourcename = 'Hybris-DM'
        ) t1 , --120379
        ( SELECT    COUNT(DISTINCT ReturnOrderID) rfo_cnt
          FROM      Hybris.ReturnOrder (NOLOCK) a ,
                    Hybris.dbo.orders (NOLOCK) b ,					
                    Hybris.dbo.users (NOLOCK) c,
					Rodanfieldslive.dbo.orders RFL
          WHERE     a.OrderID = b.pk
                    AND b.userpk = c.PK                   
                    AND a.ReturnOrderID IN ( SELECT ReturnOrderID
                                             FROM   Hybris.ReturnItem (NOLOCK) )
                    AND CountryID = 236
                    AND p_sourcename = 'Hybris-DM'
                    AND a.ReturnStatusID = 5
					AND a.ReturnOrderNumber=RFL.orderid 
					AND rfl.orderTypeID = 9

        ) t2;
  --120379
IF OBJECT_ID('Tempdb..#missing') IS NOT NULL
    DROP TABLE #missing;

SELECT  t1.ReturnOrderID ,
        t2.PK ,
        CASE WHEN t2.PK IS NULL THEN 'Destination'
             WHEN t1.ReturnOrderID IS NULL THEN 'Source'
        END AS MissingFrom
INTO    #missing
FROM    ( SELECT   DISTINCT 
                    a.ReturnOrderID
          FROM      Hybris.ReturnOrder (NOLOCK) a ,
                    Hybris.dbo.orders (NOLOCK) b ,
                    Hybris.dbo.users (NOLOCK) c,
					RODANFIELDSLIVE.DBO.ORDERS RFL
          WHERE     a.OrderID = b.PK
                    AND b.userpk = c.PK                  
                    AND CountryID = 236
                    AND p_sourcename = 'Hybris-DM'
                    AND a.ReturnStatusID = 5
					AND a.ReturnOrderID IN ( SELECT ReturnOrderID
                                             FROM   Hybris.ReturnItem (NOLOCK) )
					AND A.ReturnOrderID = rfl.orderID
                    AND rfl.orderTypeID = 9
        ) t1
        FULL OUTER JOIN ( SELECT    a.pk
                          FROM      Hybris.dbo.orders (NOLOCK) a ,
                                    Hybris.dbo.users (NOLOCK) b ,
                                    Hybris.dbo.countries (NOLOCK) c
                          WHERE     a.userpk = b.PK
                                    AND b.p_country = c.PK
                                    AND c.isocode = 'US'
                                    AND ISNULL(a.p_template, 0) = 0
                                    AND a.TypePkString = 8796127723602 --Returns
                                    AND p_sourcename = 'Hybris-DM'
                        ) t2 ON t1.returnorderID = t2.pk
WHERE   ( t2.PK  IS NULL
          OR t1.ReturnOrderID IS NULL
        );

SELECT  *
FROM    #missing m;
		
		--DATA-1898

		--SELECT ho.pk,ro.ReturnOrderID,ri.ReturnOrderID AS ReturnOrderIDInReturnItem FROM #missing m
		--JOIN Hybris..orders ho ON ho.pk=m.pk
		--JOIN hybris.returnorder ro ON ro.ReturnOrderID=m.pk
		--LEFT JOIN hybris.returnitem ri ON ri.returnorderid=ro.ReturnOrderID
		
  --======================================================================
  --				Column2Column Coparision Starts.
  ---======================================================================


DELETE  FROM DataMigration.dbo.dm_log
WHERE   test_area = '853-Returns';
IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
    DROP TABLE #tempact;

SELECT  a.ReturnOrderID ,
        a.OrderID ,
        a.AccountID ,
        c.PK ,
        d.ReturnTypeID ,
        a.ReturnStatusID ,
        [RefundedTax] ,
        [RefundedShippingCost] ,
        [RefundedHandlingCost]
INTO    #tempact
FROM    Hybris.ReturnOrder a ,
        Hybris.dbo.orders b ,
        Hybris.dbo.users c ,
        Hybris.ReturnItem d,
		Rodanfieldslive.dbo.Orders RFL
WHERE   a.OrderID = b.PK 
        AND b.userpk = c.PK
        AND a.ReturnOrderID = d.ReturnOrderID
        AND a.CountryID = 236
        AND p_sourcename = 'Hybris-DM'        
        AND a.ReturnStatusID = 5
		AND a.ReturnOrderNumber=RFL.orderid 
		AND rfl.orderTypeID = 9
		--AND a.ReturnOrderID NOT IN (SELECT pk FROM #missing)-- Loaded which doesn't have Items
--AND b.p_template IS NULL AND b.TypePkString = 8796127723602
GROUP BY a.ReturnOrderID ,
        a.OrderID ,
        a.AccountID ,
        c.PK ,
        d.ReturnTypeID ,
        a.ReturnStatusID ,
        [RefundedTax] ,
        [RefundedShippingCost] ,
        [RefundedHandlingCost]; 



CREATE NONCLUSTERED INDEX as_cls1 ON #tempact (ReturnOrderID);

SELECT  'Validation of column to column with no transformation in progress' AS [Step-2 Validation] ,
        GETDATE() AS StartTime;
SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'c2c'
        AND rfo_column <> @RFO_key
        AND [owner] = '853-Returns'
        AND Hybris_Table = 'ReturnRequest';
 --and prev_run_err > 0

WHILE @cnt <= @lt_1
    BEGIN

        SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner] + ''', ''' + flag
                + ''', ''' + [RFO_Reference Table] + ''' as rfo_column, '''
                + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                + ' as hyb_key, A.' + Hybris_Column + ' as hyb_value, B.'
                + @RFO_key + ' as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.' + @HYB_key + ', a.' + Hybris_Column + ' FROM hybris.dbo.'
                + Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
                + @RFO_key + '
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
                + @RFO_key + '=b.' + @RFO_key + ') A  

LEFT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
                + @RFO_key + '=b.' + @RFO_key + '
except
SELECT a.' + @HYB_key + ', a.' + Hybris_Column + ' FROM hybris.dbo.'
                + Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
                + @RFO_key + ') B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.' + Hybris_Column + ', B.' + @RFO_key
                + ',B.RFO_Col

FROM (SELECT a.' + @HYB_key + ', a.' + Hybris_Column + ' FROM hybris.dbo.'
                + Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
                + @RFO_key + '
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
                + @RFO_key + '=b.' + @RFO_key + ') A  

RIGHT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
                + @RFO_key + '=b.' + @RFO_key + '
except
SELECT a.' + @HYB_key + ', a.' + Hybris_Column + ' FROM hybris.dbo.'
                + Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
                + @RFO_key + ') B
ON A.' + @HYB_key + '=B.' + @RFO_key + ''
        FROM    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'c2c'
                            AND rfo_column <> @RFO_key
--and prev_run_err > 0
                            AND [owner] = '853-Returns'
                            AND Hybris_Table = 'ReturnRequest'
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
                        AND [owner] = '853-Returns'
                        AND Hybris_Table = 'ReturnRequest';
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
WHERE   [owner] = '853-Returns'
        AND flag = 'c2c'
        AND Hybris_Table = 'ReturnRequest'
        AND hybris_column NOT IN ( SELECT DISTINCT
                                            hybris_column
                                   FROM     DataMigration..dm_log
                                   WHERE    test_area = '853-Returns'
                                            AND test_type = 'c2c' );


--Defaults Check
SELECT  'Step-2 Completed, Validation of default columns in progress' AS [Step-3 Validation] ,
        GETDATE() AS StartTime;

SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'defaults'
        AND [owner] = '853-Returns'
        AND [RFO_Reference Table] = 'NULL'
        AND Hybris_Table = 'ReturnRequest';
--and prev_run_err > 0
SELECT  @lt_2 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'defaults'
        AND [owner] = '853-Returns'
        AND [RFO_Reference Table] <> 'NULL'
        AND Hybris_Table = 'ReturnRequest';
--and prev_run_err > 0

WHILE ( @cnt <= @lt_1
        AND @cnt <= @lt_2
      )
    BEGIN
        IF ( SELECT COUNT(*)
             FROM   DataMigration.dbo.map_tab
             WHERE  flag = 'defaults'
                    AND [owner] = '853-Returns'
                    AND [RFO_Reference Table] = 'NULL'
                    AND Hybris_Table = 'ReturnRequest'
           ) > 1
            BEGIN
                SELECT  @sql_gen_1 = 'use rfoperations
		select distinct ''' + [owner] + ''' as test_area, ''' + flag
                        + ''' as test_type, null as rfo_column, '''
                        + Hybris_Column + ''' as hybris_column, a.' + @HYB_key
                        + ', ' + hybris_column
                        + ', null as rfo_key, null as rfo_value
		from hybris.dbo.' + Hybris_Table + ' a, #tempact b 
		where a.' + @HYB_key + '=b.' + @RFO_key + '
		and ' + hybris_column + ' is not null'
                FROM    ( SELECT    * ,
                                    ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                          FROM      DataMigration.dbo.map_tab
                          WHERE     flag = 'defaults'
                                    AND [RFO_Reference Table] = 'NULL'
		--and prev_run_err > 0
                                    AND [owner] = '853-Returns'
                                    AND Hybris_Table = 'ReturnRequest'
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
                        AND [owner] = '853-Returns'
                        AND Hybris_Table = 'ReturnRequest';
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

        IF ( SELECT COUNT(*)
             FROM   DataMigration.dbo.map_tab
             WHERE  flag = 'defaults'
                    AND [owner] = '853-Returns'
                    AND [RFO_Reference Table] <> 'NULL'
                    AND Hybris_Table = 'ReturnRequest'
           ) > 1
            BEGIN
                SELECT  @sql_gen_2 = 'use rfoperations
		select distinct ''' + [owner] + ''' as test_area, ''' + flag
                        + ''' as test_type, null as rfo_column, '''
                        + Hybris_Column + ''' as hybris_column, a.' + @HYB_key
                        + ', ' + hybris_column
                        + ', null as rfo_key, null as rfo_value
		from hybris.dbo.' + Hybris_Table + ' a, #tempact b 
		where a.' + @HYB_key + '=b.' + @RFO_key + '
		and ' + hybris_column + ' <> ''' + [RFO_Reference Table] + ''''
                FROM    ( SELECT    * ,
                                    ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                          FROM      DataMigration.dbo.map_tab
                          WHERE     flag = 'defaults'
                                    AND [RFO_Reference Table] <> 'NULL'
		--and prev_run_err > 0
                                    AND [owner] = '853-Returns'
                                    AND Hybris_Table = 'ReturnRequest'
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
                        AND [owner] = '853-Returns'
                        AND Hybris_Table = 'ReturnRequest';
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
WHERE   [owner] = '853-Returns'
        AND flag = 'defaults'
        AND Hybris_Table = 'ReturnRequest'
        AND hybris_column NOT IN ( SELECT DISTINCT
                                            hybris_column
                                   FROM     DataMigration..dm_log
                                   WHERE    test_area = '853-Returns'
                                            AND test_type = 'defaults' );



SELECT  'Step-3 completed, Validation of transformed columns in progress' AS [Step-4 Validation] ,
        GETDATE() AS StartTime;

--Transformed Columns Validation 
SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'manual'
        AND rfo_column <> @RFO_key
        AND [owner] = '853-Returns'
        AND Hybris_Table = 'ReturnRequest';
--and prev_run_err > 0

WHILE @cnt <= @lt_1
    BEGIN

        SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner]
                + ''' as test_area, ''' + flag + ''' as test_type, '''
                + [RFO_Reference Table] + ''' as rfo_column, '''
                + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                + ' as hyb_key, A.Hyb_Trans_col as hyb_value, B.' + @RFO_key
                + ' as rfo_key, B.RFO_Trans_Col as rfo_value

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
                + ' a, #tempact b where a.' + @HYB_key + '=b.' + @RFO_key + '
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + ') A  

LEFT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
                + ' a, #tempact b where a.' + @HYB_key + '=b.' + @RFO_key
                + ') B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT  ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.Hyb_Trans_col, B.' + @RFO_key
                + ', B.RFO_Trans_Col

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
                + ' a, #tempact b where a.' + @HYB_key + '=b.' + @RFO_key + '
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + ') A  

RIGHT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
                + ' a, #tempact b where a.' + @HYB_key + '=b.' + @RFO_key
                + ') B
ON A.' + @HYB_key + '=B.' + @RFO_key + ''
        FROM    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'manual'
                            AND rfo_column <> @RFO_key 
--and prev_run_err > 0
                            AND [owner] = '853-Returns'
                            AND Hybris_Table = 'ReturnRequest'
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
                        AND [owner] = '853-Returns'
                        AND Hybris_Table = 'ReturnRequest';
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

        DELETE  FROM @temp;

        SET @cnt = @cnt + 1;

    END;

UPDATE  DataMigration.dbo.map_tab
SET     [prev_run_err] = 0
WHERE   [owner] = '853-Returns'
        AND flag = 'manual'
        AND Hybris_Table = 'ReturnRequest'
        AND hybris_column NOT IN ( SELECT DISTINCT
                                            hybris_column
                                   FROM     DataMigration..dm_log
                                   WHERE    test_area = '853-Returns'
                                            AND test_type = 'manual' );


SELECT  CASE WHEN ( [total no of columns] - [columns passed] ) > 0
             THEN 'VALIDATION INPROGRESS'
             ELSE 'VALIDATION COMPLETED'
        END [Status] ,
        [total no of columns] ,
        [columns passed] ,
        [total no of columns] - [columns passed] AS [Required Analysis] ,
        GETDATE() AS EndTime
FROM    ( SELECT    COUNT(cnt) AS [columns passed]
          FROM      ( SELECT DISTINCT
                                hybris_column AS cnt
                      FROM      DataMigration.dbo.map_tab
                      WHERE     [owner] = '853-Returns'
                                AND Hybris_Table = 'ReturnRequest'
                                AND flag IN ( 'c2c', 'manual', 'defaults' )
                      EXCEPT
                      SELECT DISTINCT
                                hybris_column
                      FROM      DataMigration..dm_log
                      WHERE     test_area = '853-Returns'
                    ) a
        ) tab1 ,
        ( SELECT    COUNT(id) AS [total no of columns]
          FROM      DataMigration.dbo.map_tab
          WHERE     [owner] = '853-Returns'
                    AND flag IN ( 'c2c', 'manual', 'defaults' )
                    AND Hybris_Table = 'ReturnRequest'
        ) tab2;




SELECT  *
FROM    DataMigration.dbo.map_tab
WHERE   [owner] = '853-Returns'
        AND  prev_run_err <> 0
             AND  prev_run_err IS NOT NULL
            ;

SELECT  *
FROM    DataMigration.dbo.dm_log
WHERE   test_area = '853-Returns';

SET STATISTICS TIME OFF;
GO

