USE RFOperations;
SET STATISTICS TIME ON;
GO

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

DECLARE @StartedTime TIME;
DECLARE @EndTime TIME; 

SELECT  'Autoship Items Loading TempTable ' AS EntityName ,
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


IF OBJECT_ID('tempdb..#Missing') IS NOT NULL
    DROP TABLE #extra;

SELECT  lo.AutoshipID
INTO    #missing
FROM    Hybris..orders ho
        JOIN Hybris..users u ON u.PK = ho.userpk
                                AND ho.p_template = 1
                                AND u.p_sourcename = 'Hybris-DM'
        JOIN Hybris..countries c ON c.PK = u.p_country
                                    AND c.isocode = 'US'
        RIGHT  JOIN #LoadedAutoshipID lo ON lo.AutoshipID = ho.code
WHERE   ho.code IS NULL; 



	
IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
    DROP TABLE #tempact;

SELECT  a.AutoShipID ,
        AutoshipNumber ,
        u.PK ,
        ai.ProductID ,
        ai.LineItemNo ,
        CAST(ai.TotalTax AS FLOAT) AS [totaltax]
INTO    #tempact
FROM    RFOperations.Hybris.Autoship a
        JOIN Hybris.dbo.users u ON CAST(a.AccountID AS NVARCHAR) = u.p_rfaccountid
                                   AND a.CountryID = 236
        JOIN RFOperations.Hybris.AutoshipItem ai ON ai.AutoshipId = a.AutoShipID
        JOIN #LoadedAutoshipID l ON l.AutoshipID = a.AutoShipID
WHERE   a.AutoshipNumber NOT IN (
        SELECT  a.OrderNumber
        FROM    RFOperations.Hybris.Orders a
                JOIN RodanFieldsLive.dbo.Orders b ON a.OrderID = b.OrderID
                                                     AND b.OrderTypeID NOT IN (
                                                     4, 5, 9 )
                                                     AND a.CountryID = 236 )
        AND a.AutoShipID NOT IN ( SELECT    code
                                  FROM      #extra )
        AND a.AutoShipID NOT IN ( SELECT    AutoshipID
                                  FROM      #missing )
GROUP BY a.AutoShipID ,
        AutoshipNumber ,
        u.PK ,
        ai.ProductID ,
        ai.LineItemNo ,
        CAST(ai.TotalTax AS FLOAT);



CREATE CLUSTERED INDEX as_cls1 ON #tempact (AutoshipID);
CREATE NONCLUSTERED INDEX cls2 ON #tempact(AutoshipNumber);

			
SELECT  'Autoship  Items Templtable load is Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Autoship Items Temp Table Loading ' AS Entity; 
		
		

SELECT  'Autoship Items Transformed Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);



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
                + ' as Hyb_Trans_col FROM (select b.' + @HYB_key
                + ' , t.*
																from hybris.dbo.'
                + Hybris_Table
                + ' t, hybris.dbo.orders b
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
                + ' as Hyb_Trans_col FROM (select b.' + @HYB_key
                + ', t.* 
																from hybris.dbo.'
                + Hybris_Table
                + ' t, hybris.dbo.orders b
																		where t.orderpk=b.pk 
																			and p_template = 1 and currencypk = 8796125855777) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT  ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.Hyb_Trans_col, B.' + @RFO_key
                + ', B.RFO_Trans_Col

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.' + @HYB_key
                + ' , t.*
																from hybris.dbo.'
                + Hybris_Table
                + ' t, hybris.dbo.orders b
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
                + ' as Hyb_Trans_col FROM (select b.' + @HYB_key
                + ' , t.*
																from hybris.dbo.'
                + Hybris_Table
                + ' t, hybris.dbo.orders b
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
WHERE   [owner] = '824-AutoshipItem'
        AND flag = 'manual'
        AND hybris_column NOT IN ( SELECT DISTINCT
                                            hybris_column
                                   FROM     DataMigration..dm_log
                                   WHERE    test_area = '824-AutoshipItem'
                                            AND test_type = 'manual' );


						

													
SELECT  'Autoship items  Transformed Column Validation Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Autoship items   Transformed Column Validation' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship Items ' AS Entity ,
                'Transformed' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;
