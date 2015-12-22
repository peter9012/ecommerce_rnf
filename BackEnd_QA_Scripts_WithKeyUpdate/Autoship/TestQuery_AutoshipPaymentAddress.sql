


SELECT  *
FROM    DataMigration..map_tab
WHERE   owner = '824-AutoshipPaymentAddress'
        AND [Hybris_Column ] = 'p_rfaddressid';


--										  ;
--SELECT  a.AutoshipPaymentAddressID ,
--        a.addressId ,
--        ho.PK ,
--        ho.p_rfaddressid ,
--        a.AddressTypeID
--FROM    ( SELECT    apa.AutoshipPaymentAddressID ,
--                    ap.AutoshipID ,
--                    MAX(ad.AddressID) addressId ,
--                    ad.AddressTypeID
--          FROM      Hybris.Autoship ap
--                    JOIN Hybris.AutoshipPaymentAddress apa ON ap.AutoshipID = apa.AutoShipID
--                    JOIN RFO_Accounts.AccountContacts ac ON ap.AccountID = ac.AccountId
--                    JOIN RFO_Accounts.AccountContactAddresses aca ON ac.AccountContactId = aca.AccountContactId
--                    JOIN RFO_Accounts.Addresses ad ON aca.AddressID = ad.AddressID
--                                                      AND IsDefault = 1
--                                                      AND ad.AddressTypeID = 3
--                                                      --AND apa.AutoshipPaymentAddressID = 8983438852119
--          GROUP BY  apa.AutoshipPaymentAddressID ,
--                    ap.AutoshipID ,
--                    ad.AddressTypeID
--        ) a
--        JOIN Hybris..addresses ho ON ho.PK = a.AutoshipPaymentAddressID
--WHERE   ho.p_rfaddressid <> a.addressId;





USE RFOperations;
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

DECLARE @HYB_key VARCHAR(100) = 'pk';
DECLARE @RFO_key VARCHAR(100) = 'AutoshipPaymentAddressID';
DECLARE @sql_gen_1 NVARCHAR(MAX);
DECLARE @cnt INT;
DECLARE @lt_1 INT;
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

--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    OwnerPkString
          FROM      Hybris.dbo.addresses
          WHERE     duplicate = 1
                    AND OwnerPkString IN ( SELECT DISTINCT
                                                    AutoshipID
                                           FROM     Hybris.Autoship
                                           WHERE    CountryID = 236 )
                    AND p_billingaddress = 1
          GROUP BY  OwnerPkString
          HAVING    COUNT(*) > 1
        ) t1;



		
IF OBJECT_ID('tempdb..#LoadedAutoshipID') IS NOT NULL
    DROP TABLE #LoadedAutoshipID;

SELECT    DISTINCT
        a.AutoshipID
INTO    #LoadedAutoshipID
FROM    RFOperations.Hybris.Autoship (NOLOCK) a
        INNER JOIN RodanFieldsLive.dbo.AutoshipOrders ao ON ao.TemplateOrderID = a.AutoshipNumber
                                                            AND ao.AccountID = a.AccountID
        INNER JOIN RFOperations.Hybris.AutoshipItem (NOLOCK) ai ON ai.AutoshipId = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipPayment (NOLOCK) ap ON ap.AutoshipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipShipment (NOLOCK) ash ON ash.AutoshipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipPaymentAddress (NOLOCK) apa ON apa.AutoShipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) asha ON asha.AutoShipID = a.AutoshipID
        INNER JOIN Hybris.dbo.users u ON a.AccountID = u.p_rfaccountid
                                         AND u.p_sourcename = 'Hybris-DM'
WHERE   a.CountryID = 236;
     ---   AND a.AutoshipID NOT IN ( SELECT    AutoshipID FROM      #DuplicateAutoship );



	 
								  
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


/*  Address OwnerPkSting <>Pk of Templates 
--Counts check on Hybris side for US
SELECT  hybris_cnt ,
        rfo_cnt ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(DISTINCT d.OwnerPkString) hybris_cnt
          FROM      Hybris.dbo.orders a ,
                    Hybris.dbo.users b ,
                    Hybris.dbo.countries c ,
                    Hybris.dbo.addresses d
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND a.PK = d.OwnerPkString
                    AND c.isocode = 'US'
                    AND a.p_template = 1
                    AND d.p_billingaddress = 1
                    AND duplicate = 1
                    AND b.p_sourcename = 'Hybris-DM'
        ) t1 , --909344
        ( SELECT    COUNT(DISTINCT a.AutoshipID) rfo_cnt
          FROM      RFOperations.Hybris.Autoship a ,
                    Hybris.dbo.users b ,
                    Hybris.AutoshipPaymentAddress c
          WHERE     a.AccountID = b.p_rfaccountid
                    AND a.AutoshipID = c.AutoShipID
                    AND a.CountryID = 236
                    AND p_sourcename = 'Hybris-DM'
        ) t2;
 --909463

 ***********************************************************/

SELECT  hybris_cnt ,
        rfo_cnt ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(DISTINCT ad.PK) hybris_cnt
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                               AND u.p_sourcename = 'Hybris-DM'
                                               AND ho.p_template = 1
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos pa ON pa.OwnerPkString = ho.PK
                                                       AND pa.duplicate = 1
                    JOIN Hybris.dbo.addresses ad ON ad.OwnerPkString = pa.PK
                                                    AND ad.duplicate = 1
		--  JOIN      Hybris.dbo.addresses ad ON ad.pk=pa.p_billingaddress AND ad.duplicate=1
          WHERE     ho.code NOT IN ( SELECT code
                                     FROM   #extra )
        ) t1 , --1135025
        ( SELECT    COUNT(DISTINCT apa.AutoshipPaymentAddressID) rfo_cnt
          FROM      RFOperations.Hybris.Autoship a
                    JOIN #LoadedAutoshipID l ON l.AutoshipID = a.AutoshipID
                                                AND a.CountryID = 236
                    JOIN RFOperations.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = a.AutoshipID
                    JOIN Hybris.dbo.users c ON c.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
                    JOIN RFOperations.Hybris.AutoshipPayment ap ON ap.AutoshipID = a.AutoshipID
                    JOIN Hybris..paymentinfos hpa ON hpa.code = CAST(ap.AutoshipPaymentID AS NVARCHAR)
        ) t2; 	
		
		
SELECT  t1.code ,
        t2.AutoshipPaymentID ,
        CASE WHEN t1.code IS NULL THEN 'Missing in AutoshipPayment in  Hybris'
             WHEN t2.AutoshipPaymentAddressID IS NULL
             THEN 'Missing AutoshipPayment in RFO'
        END AS Results
FROM    ( SELECT    ad.PK
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                               AND u.p_sourcename = 'Hybris-DM'
                                               AND ho.p_template = 1
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos pa ON pa.OwnerPkString = ho.PK
                                                       AND pa.duplicate = 1
                    JOIN Hybris.dbo.addresses ad ON ad.OwnerPkString = pa.PK
                                                    AND ad.duplicate = 1
		--  JOIN      Hybris.dbo.addresses ad ON ad.pk=pa.p_billingaddress AND ad.duplicate=1
          WHERE     ho.code NOT IN ( SELECT code
                                     FROM   #extra )
        ) t1
        FULL OUTER JOIN ( SELECT    apa.AutoshipPaymentAddressID
                          FROM      RFOperations.Hybris.Autoship a
                                    JOIN #LoadedAutoshipID l ON l.AutoshipID = a.AutoshipID
                                                              AND a.CountryID = 236
                                    JOIN RFOperations.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = a.AutoshipID
                                    JOIN Hybris.dbo.users c ON c.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
                                    JOIN RFOperations.Hybris.AutoshipPayment ap ON ap.AutoshipID = a.AutoshipID
                                    JOIN Hybris..paymentinfos hpa ON hpa.code = CAST(ap.AutoshipPaymentID AS NVARCHAR)
                        ) t2 ON t1.code = t2.AutoshipPaymentID
WHERE   t1.code IS NULL
        OR t2.AutoshipPaymentAddressID IS NULL; 




			

DELETE  FROM DataMigration.dbo.dm_log
WHERE   test_area = '824-AutoshipPaymentAddress';
IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
    DROP TABLE #tempact;

SELECT  a.AutoshipID ,
        a.AutoshipNumber ,
        a.AccountID ,
        u.PK ,
        apa.AutoshipPaymentAddressID
INTO    #tempact
FROM    RFOperations.Hybris.Autoship a
        JOIN #LoadedAutoshipID l ON l.AutoshipID = a.AutoshipID
                                    AND a.CountryID = 236
        JOIN RFOperations.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = a.AutoshipID
        JOIN Hybris.dbo.users c ON c.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
        JOIN RFOperations.Hybris.AutoshipPayment ap ON ap.AutoshipID = a.AutoshipID
        JOIN Hybris..paymentinfos hpa ON hpa.code = CAST(ap.AutoshipPaymentID AS NVARCHAR)
GROUP BY a.AutoshipID ,
        a.AutoshipNumber ,
        a.AccountID ,
        u.PK ,
        apa.AutoshipPaymentAddressID;

CREATE CLUSTERED INDEX as_cls1 ON #tempact (AutoshipID);
CREATE NONCLUSTERED COLUMNSTORE INDEX as_cls2 ON #tempact (AutoshipNumber);

SELECT  'Validation of column to column with no transformation in progress' AS [Step-1 Validation] ,
        GETDATE() AS StartTime;

SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'c2c'
        AND rfo_column <> @RFO_key
        AND [owner] = '824-AutoshipPaymentAddress';

WHILE @cnt <= @lt_1
    BEGIN

        SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner] + ''', ''' + flag
                + ''', ''' + [RFO_Reference Table] + ''' as rfo_column, '''
                + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                + ' as hyb_key, A.' + Hybris_Column + ' as hyb_value, B.'
                + @RFO_key + ' as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                + Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
                + @RFO_key + '
except
SELECT a.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
                + @RFO_key + '=b.' + @RFO_key + ') A  

LEFT JOIN

(SELECT a.' + @RFO_key + ', a.' + RFO_Column
                + ' as RFO_Col FROM rfoperations.' + [Schema] + '.'
                + RFO_Table + ' a, #tempact b where a.' + @RFO_key + '=b.'
                + @RFO_key + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                + Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
                + @RFO_key + ') B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.' + Hybris_Column + ', B.' + @RFO_key
                + ',B.RFO_Col

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                + Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
                + @RFO_key + '
except
SELECT a.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
                + @RFO_key + '=b.' + @RFO_key + ') A  

RIGHT JOIN

(SELECT a.' + @RFO_key + ', a.' + RFO_Column
                + ' as RFO_Col FROM rfoperations.' + [Schema] + '.'
                + RFO_Table + ' a, #tempact b where a.' + @RFO_key + '=b.'
                + @RFO_key + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                + Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
                + @RFO_key + ') B
ON A.' + @HYB_key + '=B.' + @RFO_key + ''
        FROM    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'c2c'
                            AND rfo_column <> @RFO_key
                            AND [owner] = '824-AutoshipPaymentAddress'
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
                        AND [owner] = '824-AutoshipPaymentAddress'; 
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
WHERE   [owner] = '824-AutoshipPaymentAddress'
        AND flag = 'c2c'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '824-AutoshipPaymentAddress'
                AND test_type = 'c2c' );


--Defaults Check
SELECT  'Step-1 Completed, Validation of default columns in progress' AS [Step-2 Validation] ,
        GETDATE() AS StartTime;

SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'defaults'
        AND [owner] = '824-AutoshipPaymentAddress'
        AND [RFO_Reference Table] <> 'NULL';

WHILE ( @cnt <= @lt_1 )
    BEGIN

        IF ( SELECT COUNT(*)
             FROM   DataMigration.dbo.map_tab
             WHERE  flag = 'defaults'
                    AND [owner] = '824-AutoshipPaymentAddress'
                    AND [RFO_Reference Table] <> 'NULL'
           ) > 1
            BEGIN
                SELECT  @sql_gen_1 = 'select distinct ''' + [owner]
                        + ''' as test_area, ''' + flag
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
                                    AND [owner] = '824-AutoshipPaymentAddress'
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
                        AND [owner] = '824-AutoshipPaymentAddress'; 
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

        SET @cnt = @cnt + 1;

    END;

UPDATE  DataMigration.dbo.map_tab
SET     [prev_run_err] = 0
WHERE   [owner] = '824-AutoshipPaymentAddress'
        AND flag = 'defaults'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '824-AutoshipPaymentAddress'
                AND test_type = 'defaults' );


--update datamigration.dbo.map_tab
--set [prev_run_err] = 0
--where [owner] = '824-Autoship' and flag = 'defaults' and [prev_run_err] is null

SELECT  'Step-2 completed, Validation of transformed columns in progress' AS [Step-3 Validation] ,
        GETDATE() AS StartTime;

SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'manual'
        AND rfo_column <> @RFO_key
        AND [owner] = '824-AutoshipPaymentAddress';

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
--and id not in (3,4,64,65) --order not migrated yet
                            AND [owner] = '824-AutoshipPaymentAddress'
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
                        AND [owner] = '824-AutoshipPaymentAddress'; 
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
WHERE   [owner] = '824-AutoshipPaymentAddress'
        AND flag = 'manual'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '824-AutoshipPaymentAddress'
                AND test_type = 'manual' );

SELECT  'VALIDATION COMPLETED' [Status] ,
        [total no of columns] ,
        [columns passed] ,
        [total no of columns] - [columns passed] AS [Required Analysis] ,
        GETDATE() AS EndTime
FROM    ( SELECT    COUNT(cnt) AS [columns passed]
          FROM      ( SELECT DISTINCT
                                hybris_column AS cnt
                      FROM      DataMigration.dbo.map_tab
                      WHERE     [owner] = '824-AutoshipPaymentAddress'
                                AND flag IN ( 'c2c', 'manual', 'defaults' )
                      EXCEPT
                      SELECT DISTINCT
                                hybris_column
                      FROM      DataMigration..dm_log
                      WHERE     test_area = '824-AutoshipPaymentAddress'
                    ) a
        ) tab1 ,
        ( SELECT    COUNT(id) AS [total no of columns]
          FROM      DataMigration.dbo.map_tab
          WHERE     [owner] = '824-AutoshipPaymentAddress'
                    AND flag IN ( 'c2c', 'manual', 'defaults' )
        ) tab2;


