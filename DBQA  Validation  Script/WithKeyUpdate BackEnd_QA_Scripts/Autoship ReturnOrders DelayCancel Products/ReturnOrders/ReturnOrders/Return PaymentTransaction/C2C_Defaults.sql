	
 USE RFOperations;
 SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
	
 DECLARE @StartedTime TIME;
 DECLARE @EndTime TIME; 
 SELECT 'Return Payment Transaction Loading Temp Table ' AS EntityName ,
        GETDATE() AS StartedTime;
 SELECT @StartedTime = CAST(GETDATE() AS TIME);
		
		
 DECLARE @modifiedDate DATE= '2016-01-04';
		
 DELETE FROM DataMigration.dbo.dm_log
 WHERE  test_area = '853-ReturnPaymentTransaction'
        AND test_type IN ( 'c2c', 'defaults' );
 IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
    DROP TABLE #tempact;

 SELECT a.ReturnOrderID ,
        a.OrderID ,
        ReturnOrderNumber ,
        a.AccountID ,
        b.PK ,
        c.ReturnPaymentId ,
        c.VendorID ,
        c.AmountToBeAuthorized ,
        c.ProcessOnDate
 INTO   #tempact
 FROM   RFOperations.Hybris.ReturnOrder a
        JOIN RodanFieldsLive.dbo.Orders rfl ON a.ReturnOrderNumber = rfl.OrderID
                                               AND rfl.OrderTypeID = 9 ,
        Hybris.dbo.users b ,
        RFOperations.Hybris.ReturnPayment c ,
        Hybris.dbo.paymentinfos d
 WHERE  a.AccountID = b.p_rfaccountid
        AND a.ReturnOrderID = c.ReturnOrderID
        AND c.ReturnPaymentId = d.PK
        AND a.CountryID = 236
        AND b.p_sourcename = 'Hybris-DM'
        AND a.ReturnOrderNumber NOT IN (
        SELECT  a.ReturnOrderNumber
        FROM    Hybris.ReturnOrder a
                JOIN Hybris.Orders b ON a.ReturnOrderNumber = b.OrderNumber
                                        AND a.CountryID = 236 )
        AND a.ReturnOrderID IN ( SELECT ReturnOrderID
                                 FROM   Hybris.ReturnItem )
 GROUP BY a.ReturnOrderID ,
        a.OrderID ,
        ReturnOrderNumber ,
        a.AccountID ,
        b.PK ,
        c.ReturnPaymentId ,
        c.VendorID ,
        d.code ,
        c.AmountToBeAuthorized ,
        c.ProcessOnDate;

 CREATE CLUSTERED INDEX as_cls1 ON #tempact (ReturnOrderID);

				
 SELECT 'Return Payment Transaction Temp table load is Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

 SELECT @EndTime = CAST(GETDATE() AS TIME);
 SELECT @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return Payment Transaction  Temp Table Loading ' AS Entity; 
		
		

 SELECT 'Return Payment Transaction  Column to Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
 SELECT @StartedTime = CAST(GETDATE() AS TIME);

		

 DECLARE @HYB_key VARCHAR(100) = 'P_Order';
 DECLARE @RFO_key VARCHAR(100) = 'ReturnorderId';
 DECLARE @sql_gen_1 NVARCHAR(MAX);
 DECLARE @lt_2 INT;
 DECLARE @sql_gen_2 NVARCHAR(MAX);
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






 SET @cnt = 1;
 SELECT @lt_1 = COUNT(*);
 SELECT *
 FROM   DataMigration.dbo.map_tab
 WHERE  flag = 'c2c'
        AND [Hybris_Column ] <> @HYB_key
        AND [owner] = '853-ReturnPaymentTransaction';

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
                            AND Hybris_column <> @HYB_key
									--AND [RFO_Column ]<>@RFO_key
									--AND id NOT IN (326)--KeyUpdateScriptsValidates 
                            AND [owner] = '853-ReturnPaymentTransaction'
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
                        AND [owner] = '853-ReturnPaymentTransaction'; 
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

 UPDATE DataMigration.dbo.map_tab
 SET    [prev_run_err] = 0
 WHERE  [owner] = '853-ReturnPaymentTransaction'
        AND flag = 'c2c'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '853-ReturnPaymentTransaction'
                AND test_type = 'c2c' );

			
						
 SELECT 'Return PaymentTranSanction C2C Validation Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

 SELECT @EndTime = CAST(GETDATE() AS TIME);
 SELECT @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return PaymentTransaction C2C Validation Completed' AS Entity; 
		
		

 INSERT INTO DataMigration.dbo.ExecResult
        SELECT  'Return PaymentTransaction WithKey' AS Entity ,
                'C2C' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;


 SELECT 'Return  PaymentTransaction  Default Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
 SELECT @StartedTime = CAST(GETDATE() AS TIME);



		
 SET @cnt = 1;
 SELECT @lt_1 = COUNT(*)
 FROM   DataMigration.dbo.map_tab
 WHERE  flag = 'defaults'
        AND [owner] = '853-ReturnPaymentTransaction'
        AND [RFO_Reference Table] = 'NULL';
 SELECT @lt_2 = COUNT(*)
 FROM   DataMigration.dbo.map_tab
 WHERE  flag = 'defaults'
        AND [owner] = '853-ReturnPaymentTransaction'
        AND [RFO_Reference Table] <> 'NULL';

 WHILE ( @cnt <= @lt_1
         AND @cnt <= @lt_2
       )
    BEGIN
        IF ( SELECT COUNT(*)
             FROM   DataMigration.dbo.map_tab
             WHERE  flag = 'defaults'
                    AND [owner] = '853-ReturnPaymentTransaction'
                    AND [RFO_Reference Table] = 'NULL'
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
                                    AND [owner] = '853-ReturnPaymentTransaction'
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
                        AND [owner] = '853-ReturnPaymentTransaction'; 
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
                    AND [owner] = '853-ReturnPaymentTransaction'
                    AND [RFO_Reference Table] <> 'NULL'
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
                                    AND [owner] = '853-ReturnPaymentTransaction'
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
                        AND [owner] = '853-ReturnPaymentTransaction'; 
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

 UPDATE DataMigration.dbo.map_tab
 SET    [prev_run_err] = 0
 WHERE  [owner] = '853-ReturnPaymentTransaction'
        AND flag = 'defaults'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '853-ReturnPaymentTransaction'
                AND test_type = 'defaults' );






		
						
 SELECT 'Return paymentTransaction Defaults Validation Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

 SELECT @EndTime = CAST(GETDATE() AS TIME);
 SELECT @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return paymentTransaction Defaults Validation  ' AS Entity; 
		
		

 INSERT INTO DataMigration.dbo.ExecResult
        SELECT  'Return PaymentTransaction WithKey' AS Entity ,
                'Default' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;


		