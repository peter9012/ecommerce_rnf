USE RFOperations;
SET STATISTICS TIME ON;
GO

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;


DECLARE @StartedTime TIME;
DECLARE @EndTime TIME; 

SELECT  'Autoship ShippingAddress Loading TempTable ' AS EntityName ,
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
WHERE   a.CountryID = 236;
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


WITH    cte
          AS ( SELECT   * ,
                        ROW_NUMBER() OVER ( PARTITION BY AutoShipID ORDER BY Address1 DESC ) AS rn
               FROM     Hybris.AutoshipShippingAddress
             )
    SELECT  a.AutoshipID ,
            a.AutoshipNumber ,
            a.AccountID ,
            b.PK ,
            c.AutoshipShippingAddressID
		--ROW_NUMBER() OVER(PARTITION BY c.AutoshipID ORDER BY c.Address1 DESC) AS Rn
    INTO    #tempact
    FROM    RFOperations.Hybris.Autoship (NOLOCK) a
            JOIN cte e ON e.AutoShipID = a.AutoshipID
                          AND e.rn = 1
            JOIN Hybris.dbo.users (NOLOCK) b ON a.AccountID = b.p_rfaccountid
            JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) c ON a.AutoshipID = c.AutoShipID
                                                              AND a.CountryID = 236
                                                              AND p_sourcename = 'Hybris-DM'
            JOIN #LoadedAutoshipID d ON a.AutoshipID = d.AutoshipID
    GROUP BY a.AutoshipID ,
            a.AutoshipNumber ,
            a.AccountID ,
            b.PK ,
            c.AutoshipShippingAddressID;

		


CREATE CLUSTERED INDEX as_cls1 ON #tempact (AutoshipID);
CREATE NONCLUSTERED COLUMNSTORE INDEX as_cls2 ON #tempact (AutoshipNumber);

			
SELECT  'Autoship  ShippingAddress Templtable load is Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Autoship ShippingAddress Temp Table Loading ' AS Entity; 
		
		

SELECT  'Autoship ShippingAddress Transformed Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);

 
SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'manual'
        AND rfo_column <> @RFO_key
        AND [owner] = '824-AutoshipShippingAddress';

WHILE @cnt <= @lt_1
    BEGIN

        SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner]
                + ''' as test_area, ''' + flag + ''' as test_type, '''
                + [RFO_Reference Table] + ''' as rfo_column, '''
                + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                + ' as hyb_key, A.Hyb_Trans_col as hyb_value, B.' + @RFO_key
                + ' as rfo_key, B.RFO_Trans_Col as rfo_value

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.' + @HYB_key + ' , t.'
                + Hybris_Column
                + '
																from hybris.dbo.'
                + Hybris_Table
                + ' t, hybris.dbo.orders b
																		where t.ownerpkstring=b.pk 
																			and p_template = 1 and currencypk = 8796125855777 and t.p_shippingaddress = 1) a
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + ') A  

LEFT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.' + @HYB_key + ' , t.'
                + Hybris_Column
                + '
																from hybris.dbo.'
                + Hybris_Table
                + ' t, hybris.dbo.orders b
																		where t.ownerpkstring=b.pk 
																			and p_template = 1 and currencypk = 8796125855777 and t.p_shippingaddress = 1) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT  ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.Hyb_Trans_col, B.' + @RFO_key
                + ', B.RFO_Trans_Col

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.' + @HYB_key + ' , t.'
                + Hybris_Column
                + '
																from hybris.dbo.'
                + Hybris_Table
                + ' t, hybris.dbo.orders b
																		where t.ownerpkstring=b.pk 
																			and p_template = 1 and currencypk = 8796125855777 and t.p_shippingaddress = 1) a
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + ') A  

RIGHT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.' + @HYB_key + ' , t.'
                + Hybris_Column
                + '
																from hybris.dbo.'
                + Hybris_Table
                + ' t, hybris.dbo.orders b
																		where t.ownerpkstring=b.pk 
																			and p_template = 1 and currencypk = 8796125855777 and t.p_shippingaddress = 1) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + ''
        FROM    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'manual'
                            AND rfo_column <> @RFO_key 
--and id not in (3,4,64,65) --order not migrated yet
                            AND [owner] = '824-AutoshipShippingAddress'
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
                        AND [owner] = '824-AutoshipShippingAddress'; 
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
WHERE   [owner] = '824-AutoshipShippingAddress'
        AND flag = 'manual'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '824-AutoshipShippingAddress'
                AND test_type = 'manual' );

				
						
SELECT  'Autoship ShippingAddress Transformed Columns Validation Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Autoship ShippingAddress Transformed Columns Validation' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship ShippingAddress ' AS Entity ,
                'Transformed' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;


	