USE RFOperations;
SET STATISTICS TIME ON;
GO

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;


		DECLARE @StartedTime TIME;
DECLARE @EndTime TIME; 

SELECT  '  AutoshipPaymentAddress Loading TempTable ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);

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
	

IF OBJECT_ID('tempdb..#LoadedAutoshipID') IS NOT NULL
    DROP TABLE #LoadedAutoshipID;

SELECT    DISTINCT
        a.AutoshipID
INTO    #LoadedAutoshipID
FROM    RFOperations.Hybris.Autoship (NOLOCK) a
        INNER JOIN RodanFieldsLive.dbo.AutoshipOrders ao ON ao.TemplateOrderID = a.AutoshipID
                                                            AND ao.AccountID = a.AccountID
        INNER JOIN RFOperations.Hybris.AutoshipItem (NOLOCK) ai ON ai.AutoshipId = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipPayment (NOLOCK) ap ON ap.AutoshipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipShipment (NOLOCK) ash ON ash.AutoshipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipPaymentAddress (NOLOCK) apa ON apa.AutoShipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) asha ON asha.AutoShipID = a.AutoshipID
        INNER JOIN Hybris.dbo.users u ON a.AccountID = u.p_rfaccountid
                                         AND u.p_sourcename = 'Hybris-DM'
WHERE   a.CountryID = 236
     --   AND a.AutoshipID NOT IN ( SELECT    AutoshipID FROM      #DuplicateAutoship );

								  

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



--IF OBJECT_ID('tempdb..#Missing') IS NOT NULL
--    DROP TABLE #extra;

--SELECT lo.AutoshipID
--INTO    #missing
--FROM    Hybris..orders ho
--        JOIN Hybris..users u ON u.PK = ho.userpk
--                                AND ho.p_template = 1
--                                AND u.p_sourcename = 'Hybris-DM'
--        JOIN Hybris..countries c ON c.PK = u.p_country
--                                    AND c.isocode = 'US'
--        RIGHT  JOIN #LoadedAutoshipID lo ON lo.AutoshipID = ho.code
--WHERE   ho.code IS NULL; 

IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
    DROP TABLE #tempact;

	
SELECT  a.AutoshipID ,
        a.AutoshipNumber ,
        a.AccountID ,
        c.PK ,
        apa.AutoshipPaymentAddressID
INTO    #tempact
FROM    RFOperations.Hybris.Autoship a
        JOIN #LoadedAutoshipID l ON l.AutoshipID = a.AutoshipID
                                    AND a.CountryID = 236
        JOIN RFOperations.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = a.AutoshipID
        JOIN Hybris.dbo.users c ON c.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
        JOIN RFOperations.Hybris.AutoshipPayment ap ON ap.AutoshipID = a.AutoshipID AND apa.AutoshipPaymentAddressID=ap.AutoshipPaymentID
        JOIN Hybris..paymentinfos hpa ON hpa.code = CAST(ap.AutoshipPaymentID AS NVARCHAR) AND hpa.duplicate=1
GROUP BY a.AutoshipID ,
        a.AutoshipNumber ,
        a.AccountID ,
        c.PK ,
        apa.AutoshipPaymentAddressID;

CREATE CLUSTERED INDEX as_cls1 ON #tempact (AutoshipID);
CREATE NONCLUSTERED COLUMNSTORE INDEX as_cls2 ON #tempact (AutoshipNumber);

			
SELECT  'Autoship  AutoshipPaymentAddress Templtable load is Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        '  AutoshipPaymentAddress Temp Table Loading ' AS Entity; 
		
		

		SELECT  '  AutoshipPaymentAddress Column to Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
		SELECT  @StartedTime=CAST(GETDATE() AS TIME)



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

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
																from hybris.dbo.' + Hybris_Table + ' t, 
																hybris.dbo.paymentinfos c,
																hybris.dbo.orders b
																		where t.ownerpkstring=c.pk 
																		and c.ownerpkstring=b.pk
																			and p_template = 1 and currencypk = 8796125855777 and c.duplicate = 1) a
except
SELECT b.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT b.' + @RFO_key + ', a.' + RFO_Column
                + ' as RFO_Col FROM rfoperations.' + [Schema] + '.'
                + RFO_Table + ' a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
																from hybris.dbo.' + Hybris_Table + ' t, 
																hybris.dbo.paymentinfos c,
																hybris.dbo.orders b
																		where t.ownerpkstring=c.pk 
																		and c.ownerpkstring=b.pk
																			and p_template = 1 and currencypk = 8796125855777 and c.duplicate = 1) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.' + Hybris_Column + ', B.' + @RFO_key
                + ',B.RFO_Col

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
																from hybris.dbo.' + Hybris_Table + ' t, 
																hybris.dbo.paymentinfos c,
																hybris.dbo.orders b
																		where t.ownerpkstring=c.pk 
																		and c.ownerpkstring=b.pk
																			and p_template = 1 and currencypk = 8796125855777 and c.duplicate = 1) a
except
SELECT b.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT b.' + @RFO_key + ', a.' + RFO_Column
                + ' as RFO_Col FROM rfoperations.' + [Schema] + '.'
                + RFO_Table + ' a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
																from hybris.dbo.' + Hybris_Table + ' t, 
																hybris.dbo.paymentinfos c,
																hybris.dbo.orders b
																		where t.ownerpkstring=c.pk 
																		and c.ownerpkstring=b.pk
																			and p_template = 1 and currencypk = 8796125855777 and c.duplicate = 1) a) B
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



		
SELECT  '  AutoshipPaymentAddress Column to  Columns Validation' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        '  AutoshipPaymentAddress Column to Column Validation' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship  PaymentAddress ' AS Entity ,
                'C2C' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;


		SELECT  '  AutoshipPaymentAddress Default Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
		SELECT  @StartedTime=CAST(GETDATE() AS TIME)




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
		from (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
																from hybris.dbo.' + Hybris_Table + ' t, 
																hybris.dbo.paymentinfos c,
																hybris.dbo.orders b
																		where t.ownerpkstring=c.pk 
																		and c.ownerpkstring=b.pk
																			and p_template = 1 and currencypk = 8796125855777 and c.duplicate = 1) a
		where ' + hybris_column + ' <> ''' + [RFO_Reference Table] + ''''
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


		
SELECT  '  AutoshipPaymentAddress default Columns Validation Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        '  AutoshipPaymentAddress Default Column Validation' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship  PaymentAddress ' AS Entity ,
                'Default' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;

