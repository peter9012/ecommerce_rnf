		USE RFOperations;
		SET STATISTICS TIME ON;
		GO

		SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
		DECLARE @HYB_key VARCHAR(100) = 'P_rma';
		DECLARE @RFO_key VARCHAR(100) = 'returnorderID';
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
		DECLARE @StartedTime TIME;
DECLARE @EndTime TIME; 

SELECT  'Return Header Loading TempTable ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);
		
		
		DECLARE @modifiedDate DATE='2016-01-04'
		
		IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
			DROP TABLE #tempact;

        SELECT  ro.ReturnOrderID ,
                ro.OrderID ,
                ho.PK AS HybrisOrder ,
                ro.AccountID ,
                u.PK ,
                ri.ReturnTypeID ,
                ro.ReturnStatusID ,
                [RefundedTax] ,
                [RefundedShippingCost] ,
                [RefundedHandlingCost] ,
                ro.ReturnOrderNumber ,
                ho.p_associatedorder AS associatedorder
        INTO    #tempact
        FROM    Hybris.ReturnOrder ro
                JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderID = rfl.OrderID
                                                       AND rfl.OrderTypeID = 9
                JOIN Hybris.dbo.orders ho ON ho.code = ro.ReturnOrderID
                JOIN Hybris.dbo.users u ON u.PK = ho.userpk
                                           AND ro.CountryId = 236
                                           AND u.p_sourcename = 'hybris-DM'
                JOIN RFOperations.Hybris.ReturnItem ri ON ri.ReturnOrderID = ro.ReturnOrderID
        WHERE   ro.ReturnOrderNumber NOT IN (
                SELECT  a.ReturnOrderNumber
                FROM    Hybris.ReturnOrder a
                        JOIN Hybris.Orders b ON a.ReturnOrderNumber = b.OrderNumber
                                                AND a.CountryID = 236 )               
                AND ro.ReturnStatusID = 5
				AND CAST(ho.modifiedTS AS DATE)=@modifiedDate
		
		GROUP BY ro.ReturnOrderID ,
                ro.OrderID ,
                ho.PK  ,
                ro.AccountID ,
                u.PK ,
                ri.ReturnTypeID ,
                ro.ReturnStatusID ,
                [RefundedTax] ,
                [RefundedShippingCost] ,
                [RefundedHandlingCost] ,
                ro.ReturnOrderNumber ,
                ho.p_associatedorder 

		CREATE NONCLUSTERED INDEX as_cls1 ON #tempact (ReturnOrderID);

				
SELECT  'Return  header Templtable load is Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return Header Temp Table Loading ' AS Entity; 
		
		

		SELECT  'Return Header to ReturnRequest Transformed Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
		SELECT  @StartedTime=CAST(GETDATE() AS TIME)

	

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
			 declare @err_cnt int
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



													
						
SELECT  'Return Header to ReturnRequest Transformed Column Validation' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return Header to ReturnRequest Transformed Column Validation' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'ReturnRequest ' AS Entity ,
                'Transformed' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;


		SELECT  'Return Header to Hybris-Order Transformed Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
		SELECT  @StartedTime=CAST(GETDATE() AS TIME)
													

				SET  @HYB_key  = 'code';
		SET  @RFO_key ='returnorderID';


		SET @cnt = 1;
		SELECT  @lt_1 = COUNT(*)
		FROM    DataMigration.dbo.map_tab
		WHERE   flag = 'manual'
				AND rfo_column <> @RFO_key
				AND [owner] = '853-Returns'
				AND Hybris_Table = 'orders';
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
									AND Hybris_Table = 'orders'
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
								AND Hybris_Table = 'orders';
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
				AND Hybris_Table = 'orders'
				AND hybris_column NOT IN ( SELECT DISTINCT
													hybris_column
										   FROM     DataMigration..dm_log
										   WHERE    test_area = '853-Returns'
													AND test_type = 'manual' );

													
						
SELECT  'Return Header to Hybris-Order Transformed Column Validation' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return Header to Hybris-order Transformed  Column Validation' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Return Header ' AS Entity ,
                'Transformed' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;


		