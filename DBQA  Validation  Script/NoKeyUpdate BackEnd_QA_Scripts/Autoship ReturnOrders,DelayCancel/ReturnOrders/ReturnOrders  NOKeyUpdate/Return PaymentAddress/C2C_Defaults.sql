	
 USE RFOperations;
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
	
		DECLARE @StartedTime TIME;
DECLARE @EndTime TIME; 
SELECT  'Return PaymentAddress Loading Temp Table ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);
		
		
		--DECLARE @modifiedDate DATE='2016-01-04'
		
		IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
			DROP TABLE #tempact;

       SELECT  a.ReturnOrderID ,
            a.ReturnOrderNumber ,
            a.AccountID ,
            b.PK ,
            c.ReturnBillingAddressID
    INTO    #tempact
    FROM    RFOperations.Hybris.ReturnOrder a
			JOIN RodanFieldsLive.dbo.Orders rfl ON A.ReturnOrderID = rfl.orderID
                                                             AND rfl.orderTypeID = 9
            JOIN Hybris.dbo.users b ON a.AccountID = b.p_rfaccountid
                                       AND a.CountryID = 236
            JOIN Hybris.dbo.orders d ON a.ReturnOrderID = d.code
            JOIN Hybris..paymentinfos e ON e.OwnerPkString = d.PK
            LEFT JOIN RFOperations.Hybris.ReturnBillingAddress c ON a.ReturnOrderID = c.ReturnOrderID
    WHERE   a.CountryID = 236
            AND b.p_sourcename = 'Hybris-DM'
    GROUP BY a.ReturnOrderID ,
            a.ReturnOrderNumber ,
            a.AccountID ,
            b.PK ,
            c.ReturnBillingAddressID;
			

		CREATE  CLUSTERED INDEX as_cls1 ON #tempact (ReturnOrderID);

				
SELECT  'Return  PaymentAddress Temp table load is Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return PaymentAddress Temp Table Loading ' AS Entity; 
		
		

		SELECT  'Return PaymentAddress Column to Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
		SELECT  @StartedTime=CAST(GETDATE() AS TIME)

		

    DECLARE @HYB_key VARCHAR(100) = 'code';
    DECLARE @RFO_key VARCHAR(100) = 'ReturnorderId';
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


		

	SET @cnt = 1;
	SELECT  @lt_1 = COUNT(*)
 FROM    DataMigration.dbo.map_tab
	WHERE   flag = 'c2c'
			 AND rfo_column <> @RFO_key
			AND [owner] = '853-ReturnPaymentAddress'
			

	WHILE @cnt <= @lt_1
		BEGIN

			SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner] + ''', ''' + flag
					+ ''', ''' + [RFO_Reference Table] + ''' as rfo_column, '''
					+ Hybris_Column + ''' as hybris_column, A.' + @HYB_key
					+ ' as hyb_key, A.' + Hybris_Column + ' as hyb_value, B.'
					+ @RFO_key + ' as rfo_key, B.RFO_Col as rfo_value

	FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'
					+ @HYB_key + ' , t.' + Hybris_Column
					+ '
										   from hybris.dbo.'
					+ Hybris_Table
					+ ' t, 
							   hybris.dbo.paymentinfos c,
								hybris.dbo.orders b
								   where t.ownerpkstring=c.pk 
									and c.ownerpkstring=b.pk
									  and b.typepkstring=8796127723602 
									  and p_template is null 
									  and currencypk = 8796125855777 
									  and c.duplicate = 1) a
	except
	SELECT b.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM rfoperations.'
					+ [Schema] + '.' + RFO_Table
					+ ' a, #tempact b where a.ReturnOrderID=b.ReturnOrderID) A  

	LEFT JOIN

	(SELECT b.' + @RFO_key + ', a.' + RFO_Column
					+ ' as RFO_Col FROM rfoperations.' + [Schema] + '.'
					+ RFO_Table
					+ ' a, #tempact b where a.ReturnOrderID=b.ReturnOrderID
	except
	SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.' + @HYB_key
					+ ' , t.' + Hybris_Column
					+ '
					   from hybris.dbo.'
					+ Hybris_Table
					+ ' t, 
					   hybris.dbo.paymentinfos c,
						  hybris.dbo.orders b
						   where t.ownerpkstring=c.pk 
							and c.ownerpkstring=b.pk
						  and b.typepkstring=8796127723602 
						   and p_template is null 
							and currencypk = 8796125855777
							and c.duplicate = 1) a) B
	ON A.' + @HYB_key + '=B.' + @RFO_key + '
	UNION
	SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', '''
					+ [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
					+ @HYB_key + ', A.' + Hybris_Column + ', B.' + @RFO_key
					+ ',B.RFO_Col

	FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'
					+ @HYB_key + ' , t.' + Hybris_Column
					+ '
						 from hybris.dbo.'
					+ Hybris_Table
					+ ' t, 
					   hybris.dbo.paymentinfos c,
					   hybris.dbo.orders b
					  where t.ownerpkstring=c.pk 
					   and c.ownerpkstring=b.pk
						  and b.typepkstring=8796127723602 
						  and p_template is null 
						   and currencypk = 8796125855777 
						   and c.duplicate = 1) a
	except
	SELECT b.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM rfoperations.'
					+ [Schema] + '.' + RFO_Table
					+ ' a, #tempact b where a.ReturnOrderID=b.ReturnOrderID) A  

	RIGHT JOIN

	(SELECT b.' + @RFO_key + ', a.' + RFO_Column
					+ ' as RFO_Col FROM rfoperations.' + [Schema] + '.'
					+ RFO_Table
					+ ' a, #tempact b where a.ReturnOrderID=b.ReturnOrderID
	except
	SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.' + @HYB_key
					+ ' , t.' + Hybris_Column
					+ '
						  from hybris.dbo.'
					+ Hybris_Table
					+ ' t, 
					  hybris.dbo.paymentinfos c,
					   hybris.dbo.orders b
					   where t.ownerpkstring=c.pk 
					   and c.ownerpkstring=b.pk
					   and b.typepkstring=8796127723602 
					   and p_template is null 
					   and currencypk = 8796125855777 
					   and c.duplicate = 1) a) B

	ON A.' + @HYB_key + '=B.' + @RFO_key + ''
			FROM    ( SELECT    * ,
								ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
					  FROM      DataMigration.dbo.map_tab
					  WHERE     flag = 'c2c'
								AND rfo_column <> @RFO_key
								AND [owner] = '853-ReturnPaymentAddress'
								 
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
							AND [owner] = '853-ReturnPaymentAddress'
							  
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
	WHERE   [owner] = '853-ReturnPaymentAddress'
			AND flag = 'c2c'
			 
			AND hybris_column NOT IN (
			SELECT DISTINCT
					hybris_column
			FROM    DataMigration..dm_log
			WHERE   test_area = '853-ReturnPaymentAddress'
					AND test_type = 'c2c' );

				
						
SELECT  'Return PaymentAddress C2C Validation Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return PaymentAddress C2C Validation Completed' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Return PaymentAddress ' AS Entity ,
                'C2C' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;


		SELECT  'Return  PaymentAddress  Default Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
		SELECT  @StartedTime=CAST(GETDATE() AS TIME)

	

	SET @cnt = 1;
	SELECT  @lt_1 = COUNT(*)
	FROM    DataMigration.dbo.map_tab
	WHERE   flag = 'defaults'
			AND [owner] = '853-ReturnPaymentAddress'
			AND [RFO_Reference Table] <> 'NULL';

	WHILE ( @cnt <= @lt_1 )
		BEGIN

			IF ( SELECT COUNT(*)
				 FROM   DataMigration.dbo.map_tab
				 WHERE  flag = 'defaults'
						AND [owner] = '853-ReturnPaymentAddress'
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
										and b.typepkstring=8796127723602
										and p_template is null 
										and currencypk = 8796125855777 
										and c.duplicate = 1) a
				where ' + hybris_column + ' <> ''' + [RFO_Reference Table] + ''''
					FROM    ( SELECT    * ,
										ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
							  FROM      DataMigration.dbo.map_tab
							  WHERE     flag = 'defaults'
										AND [RFO_Reference Table] <> 'NULL'
										AND [owner] = '853-ReturnPaymentAddress'
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
							AND [owner] = '853-ReturnPaymentAddress'; 
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
	WHERE   [owner] = '853-ReturnPaymentAddress'
			AND flag = 'defaults'
			AND hybris_column NOT IN (
			SELECT DISTINCT
					hybris_column
			FROM    DataMigration..dm_log
			WHERE   test_area = '853-ReturnPaymentAddress'
					AND test_type = 'defaults' );


						
SELECT  'Return  PaymentAddress  Default Column Validation Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return  PaymentAddress  Default Column Validation Completed' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Return PaymentAddress ' AS Entity ,
                'Default' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;


	