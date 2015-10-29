

--SELECT * FROM datamigration..map_tab
--WHERE [owner]='824-Autoship' AND [Hybris_Column ]='statuspk'



SELECT * FROM datamigration..dm_log
WHERE test_area ='824-Autoship'

SELECT ato.ConsultantID,ato.AutoshipID,ho.p_consultantIdReceivingCommiss,ho.pk FROM Hybris..orders ho
JOIN hybris.autoship ato ON ho.pk=ato.autoshipid
WHERE ato.ConsultantID <>ho.p_consultantIdReceivingCommiss 

USE RFOperations;
SET STATISTICS TIME ON;
GO

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

DECLARE @HYB_key VARCHAR(100) = 'pk';
DECLARE @RFO_key VARCHAR(100) = 'autoshipid';
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

	--Validation of AUTOSHIP Counts, Dups & Columns without transformations

	--Duplicate check on Hybris side for US
	SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
				 ELSE 'No duplicates - Validation Passed'
			END AS [Step-1 Validation]
	FROM    ( SELECT    COUNT(*) cnt ,
						a.code AS autoshipnumber
			  FROM      Hybris.dbo.orders a ,
						Hybris.dbo.users b ,
						Hybris.dbo.countries c
			  WHERE     a.userpk = b.PK
						AND b.p_country = c.PK
						AND c.isocode = 'US'
						AND a.p_template = 1 --AS
	GROUP BY            a.code
			  HAVING    COUNT(*) > 1
			) t1;

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
												 AND u.p_sourcename = 'Hybris-DM'
		WHERE   a.CountryID = 236
				AND a.AutoshipID NOT IN ( SELECT    AutoshipID
										  FROM      #DuplicateAutoship );


		SELECT  hybris_cnt ,
				rfo_cnt ,
				CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
					 WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
					 ELSE 'Count matches - validation passed'
				END Results
		FROM    ( SELECT    COUNT(a.PK) hybris_cnt
				  FROM      Hybris.dbo.orders a ,
							Hybris.dbo.users b ,
							Hybris.dbo.countries c
				  WHERE     a.userpk = b.PK
							AND b.p_country = c.PK
							AND c.isocode = 'US'
							AND a.p_template = 1
							AND p_sourcename = 'Hybris-DM'
				) t1 ,
				(
				  SELECT    COUNT(DISTINCT a.AutoshipID) rfo_cnt
				  FROM      RFOperations.Hybris.Autoship (NOLOCK) a
							JOIN #LoadedAutoshipID b ON a.AutoshipID=b.AutoshipID
				  WHERE     a.CountryID = 236
							
										) t2;



                                         IF OBJECT_ID('tempdb..#missing') IS NOT NULL
                                            DROP TABLE #missing;

                                        SELECT  t1.pk,t2.AutoshipID,CASE WHEN t1.pk IS NULL THEN 'Missing in Hybris' 
										WHEN t2.AutoshipID IS NULL THEN 'Missing in RFO' END  results INTO #missing
                                        FROM    ( SELECT    a.PK
                                                  FROM      Hybris.dbo.orders a ,
                                                            Hybris.dbo.users b ,
                                                            Hybris.dbo.countries c
                                                  WHERE     a.userpk = b.PK
                                                            AND b.p_country = c.PK
                                                            AND c.isocode = 'US'
                                                            AND a.p_template = 1
                                                            AND p_sourcename = 'Hybris-DM'
                                                ) t1 FULL OUTER JOIN 
                                                ( SELECT    a.AutoshipID
                                                  FROM      RFOperations.Hybris.Autoship (NOLOCK) a
                                                            JOIN #LoadedAutoshipID b ON a.AutoshipID = b.AutoshipID
                                                  WHERE     a.CountryID = 236
                                                ) t2 ON t1.pk=t2.AutoshipID
												WHERE t1.pk IS NULL OR t2.AutoshipID IS NULL 
												SELECT COUNT(*) AS TotalMissingAutoship FROM #missing
												SELECT *FROM #missing

		--Column2Column Validation that doesn't have transformation - Autoship



        DELETE  FROM DataMigration.dbo.dm_log
        WHERE   test_area = '824-Autoship';
        IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
            DROP TABLE #tempact;



			SELECT  a.AutoshipID ,
					a.AutoshipNumber ,
					a.AccountID ,
					u.PK ,
					a.AutoshipStatusID ,
					a.AutoshipTypeID ,
					a.StartDate ,
				    a.TotalTax
			into #tempact 
			FROM      RFOperations.Hybris.Autoship (NOLOCK) a
			JOIN #LoadedAutoshipID b ON a.AutoshipID=b.AutoshipID
			 JOIN Hybris.dbo.users u ON a.AccountID = u.p_rfaccountid
												AND u.p_sourcename = 'Hybris-DM'
			WHERE   a.CountryID = 236
					AND a.AutoshipNumber NOT IN ( SELECT    OrderNumber
												  FROM      Hybris.Orders
												  WHERE     CountryID = 236 )
												  
			GROUP BY a.AutoshipID ,
					a.AutoshipNumber ,
					a.AccountID ,
					u.PK ,
					a.AutoshipStatusID ,
					a.AutoshipTypeID ,
					a.StartDate ,
				    a.TotalTax;


			CREATE CLUSTERED INDEX as_cls1 ON #tempact (AutoshipID);

			SELECT  'Validation of column to column with no transformation in progress' AS [Step-2 Validation] ,
					GETDATE() AS StartTime;
			SET @cnt = 1;
			SELECT  @lt_1 = COUNT(*)
			FROM    DataMigration.dbo.map_tab
			WHERE   flag = 'c2c'
					AND rfo_column <> @RFO_key
					AND [owner] = '824-Autoship';
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
										AND [owner] = '824-Autoship'
							) temp
					WHERE   rn = @cnt; 

					--PRINT @sql_gen_1;
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
									AND [owner] = '824-Autoship'; 
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
			WHERE   [owner] = '824-Autoship'
					AND flag = 'c2c'
					AND hybris_column NOT IN ( SELECT DISTINCT
														hybris_column
											   FROM     DataMigration..dm_log
											   WHERE    test_area = '824-Autoship'
														AND test_type = 'c2c' );

--			===========================================================================
--
--		================================================================================


		SELECT  'Step-2 Completed, Validation of default columns in progress' AS [Step-3 Validation] ,
				GETDATE() AS StartTime;

		----Defaults Check
		SET @cnt = 1;
		SELECT  @lt_1 = COUNT(*)
		FROM    DataMigration.dbo.map_tab
		WHERE   flag = 'defaults'
				AND [owner] = '824-Autoship'
				AND [RFO_Reference Table] = 'NULL';
		 --and prev_run_err > 0
		SELECT  @lt_2 = COUNT(*)
		FROM    DataMigration.dbo.map_tab
		WHERE   flag = 'defaults'
				AND [owner] = '824-Autoship'
				AND [RFO_Reference Table] <> 'NULL';
		 --and prev_run_err > 0

		WHILE ( @cnt <= @lt_1
				AND @cnt <= @lt_2
			  )
			BEGIN
				IF ( SELECT COUNT(*)
					 FROM   DataMigration.dbo.map_tab
					 WHERE  flag = 'defaults'
							AND [owner] = '824-Autoship'
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
				--and prev_run_err > 0
											AND [owner] = '824-Autoship'
								) temp
						WHERE   rn = @cnt;
					END;

				--PRINT @sql_gen_1;
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
								AND [owner] = '824-Autoship'; 
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
							AND [owner] = '824-Autoship'
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
				--and prev_run_err > 0
											AND [owner] = '824-Autoship'
								) temp
						WHERE   rn = @cnt;
					END;

				--PRINT @sql_gen_2;
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
								AND [owner] = '824-Autoship'; 
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
		WHERE   [owner] = '824-Autoship'
				AND flag = 'defaults'
				AND hybris_column NOT IN ( SELECT DISTINCT
													hybris_column
										   FROM     DataMigration..dm_log
										   WHERE    test_area = '824-Autoship'
													AND test_type = 'defaults' )

--		===============================================================================

--		===============================================================================


			SELECT  'Step-3 completed, Validation of transformed columns in progress' AS [Step-4 Validation] ,
					GETDATE() AS StartTime;

			--Transformed Columns Validation --10:16 mins
			SET @cnt = 1;
			SELECT  @lt_1 = COUNT(*)
			FROM    DataMigration.dbo.map_tab
			WHERE   flag = 'manual'
					AND rfo_column <> @RFO_key
					AND [owner] = '824-Autoship';
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
			--and rfo_column <> @RFO_key 
										AND id NOT IN ( 69, 70 ) --Data types are image on Hybris end. This value is generated by the system
										AND id NOT IN ( 3, 4, 64, 65 ) --order not migrated yet
			--and prev_run_err > 0
										AND [owner] = '824-Autoship'
							) temp
					WHERE   rn = @cnt;

					--PRINT @sql_gen_1;
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
									AND [owner] = '824-Autoship'; 
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
			WHERE   [owner] = '824-Autoship'
					AND flag = 'manual'
					AND hybris_column NOT IN ( SELECT DISTINCT
														hybris_column
											   FROM     DataMigration..dm_log
											   WHERE    test_area = '824-Autoship'
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
								  WHERE     [owner] = '824-Autoship'
											AND flag IN ( 'c2c', 'manual', 'defaults' )
								  EXCEPT
								  SELECT DISTINCT
											hybris_column
								  FROM      DataMigration..dm_log
								  WHERE     test_area = '824-Autoship'
								) a
					) tab1 ,
					( SELECT    COUNT(id) AS [total no of columns]
					  FROM      DataMigration.dbo.map_tab
					  WHERE     [owner] = '824-Autoship'
								AND flag IN ( 'c2c', 'manual', 'defaults' )
					) tab2;

			SET STATISTICS TIME OFF;
			GO


			

--USE RFOperations;
--SET STATISTICS TIME ON;
--GO

--SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

--DECLARE @HYB_key VARCHAR(100) = 'pk';
--DECLARE @RFO_key VARCHAR(100) = 'autoshipid';
--DECLARE @sql_gen_1 NVARCHAR(MAX);
--DECLARE @sql_gen_2 NVARCHAR(MAX);
--DECLARE @cnt INT;
--DECLARE @lt_1 INT;
--DECLARE @lt_2 INT;
--DECLARE @temp TABLE
--    (
--      test_area VARCHAR(MAX) ,
--      test_type VARCHAR(MAX) ,
--      rfo_column VARCHAR(MAX) ,
--      hybris_column VARCHAR(MAX) ,
--      hyb_key VARCHAR(MAX) ,
--      hyb_value VARCHAR(MAX) ,
--      rfo_key VARCHAR(MAX) ,
--      rfo_value VARCHAR(MAX)
--    );

--	--Validation of AUTOSHIP Counts, Dups & Columns without transformations

--	--Duplicate check on Hybris side for US
--	SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
--				 ELSE 'No duplicates - Validation Passed'
--			END AS [Step-1 Validation]
--	FROM    ( SELECT    COUNT(*) cnt ,
--						a.code AS autoshipnumber
--			  FROM      Hybris.dbo.orders a ,
--						Hybris.dbo.users b ,
--						Hybris.dbo.countries c
--			  WHERE     a.userpk = b.PK
--						AND b.p_country = c.PK
--						AND c.isocode = 'US'
--						AND a.p_template = 1 --AS
--	GROUP BY            a.code
--			  HAVING    COUNT(*) > 1
--			) t1;

--	--Counts check on Hybris side for US


--		SELECT  hybris_cnt ,
--				rfo_cnt ,
--				CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
--					 WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
--					 ELSE 'Count matches - validation passed'
--				END Results
--		FROM    ( SELECT    COUNT(a.PK) hybris_cnt
--				  FROM      Hybris.dbo.orders a ,
--							Hybris.dbo.users b ,
--							Hybris.dbo.countries c
--				  WHERE     a.userpk = b.PK
--							AND b.p_country = c.PK
--							AND c.isocode = 'US'
--							AND a.p_template = 1
--							AND p_sourcename = 'Hybris-DM'
--				) t1 ,
--				(

--				--SELECT count(distinct autoshipid) rfo_cnt
--				--from rfoperations.hybris.autoship a,
--				--hybris.dbo.users b
--				--where a.accountid=b.p_rfaccountid
--				--and countryid = 236
--				--and p_sourcename = 'Hybris-DM'
--				  SELECT    COUNT(DISTINCT a.AutoshipID) rfo_cnt
--				  FROM      RFOperations.Hybris.Autoship (NOLOCK) a
--							INNER JOIN RFOperations.Hybris.AutoshipItem (NOLOCK) ai ON ai.AutoshipId = a.AutoshipID
--							INNER JOIN RFOperations.Hybris.AutoshipPayment (NOLOCK) ap ON ap.AutoshipID = a.AutoshipID
--							INNER JOIN RFOperations.Hybris.AutoshipShipment (NOLOCK) ash ON ash.AutoshipID = a.AutoshipID
--							INNER JOIN RFOperations.Hybris.AutoshipPaymentAddress (NOLOCK) apa ON apa.AutoShipID = a.AutoshipID
--							INNER JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) asha ON asha.AutoShipID = a.AutoshipID
--							INNER JOIN Hybris.dbo.users u ON a.AccountID = u.p_rfaccountid
--															 AND u.p_sourcename = 'Hybris-DM'
--				  WHERE     a.CountryID = 236
--							AND a.AutoshipID NOT IN (
--							SELECT  AutoshipID
--							FROM    Hybris.Autoship
--							WHERE   AccountID IN (
--									SELECT  a.AccountID
--									FROM    Hybris.Autoship a
--											INNER JOIN RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
--									WHERE   ab.AccountTypeID = 1
--											AND a.CountryID = 236
--											AND a.AutoshipTypeID = 2
--											AND a.Active = 1
--									GROUP BY a.AccountID
--									HAVING  COUNT(*) > 1 )
--									AND AutoshipID NOT IN (
--									SELECT  MAX(AutoshipID) AutoshipID
--									FROM    Hybris.Autoship a
--											INNER JOIN RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
--									WHERE   ab.AccountTypeID = 1
--											AND a.CountryID = 236
--											AND a.AutoshipTypeID = 2
--											AND a.Active = 1
--									GROUP BY a.AccountID
--									HAVING  COUNT(*) > 1 )
--									AND CountryID = 236
--									AND AutoshipTypeID = 2 
--							--SELECT AutoshipID FROM #DuplicateAutoship
--							)
--				) t2;

--		--Column2Column Validation that doesn't have transformation - Autoship



--        DELETE  FROM DataMigration.dbo.dm_log
--        WHERE   test_area = '824-Autoship';
--        IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
--            DROP TABLE #tempact;



--		--select autoshipid, autoshipnumber, a.accountid, b.pk, a.autoshipstatusid, a.autoshiptypeid, startdate, totaltax
--		--into #tempact 
--		--from rfoperations.hybris.autoship a,
--		--hybris.dbo.users b
--		--where a.accountid=b.p_rfaccountid
--		--and countryid = 236 and p_sourcename = 'Hybris-DM'
--		--AND a.AutoshipNumber NOT IN (SELECT ordernumber FROM Hybris.Orders WHERE countryid = 236)
--		--group by autoshipid, autoshipnumber, a.accountid, b.pk, a.autoshipstatusid, autoshiptypeid, startdate, totaltax



--			SELECT COUNT( a.AutoshipID )--,
--					--a.AutoshipNumber ,
--					--a.AccountID ,
--					--u.PK ,
--					--a.AutoshipStatusID ,
--					--a.AutoshipTypeID ,
--					--a.StartDate ,
--					--a.TotalTax
--			--into #tempact 
--			FROM    RFOperations.Hybris.Autoship (NOLOCK) a
--					INNER JOIN RFOperations.Hybris.AutoshipItem (NOLOCK) ai ON ai.AutoshipId = a.AutoshipID
--					INNER JOIN RFOperations.Hybris.AutoshipPayment (NOLOCK) ap ON ap.AutoshipID = a.AutoshipID
--					INNER JOIN RFOperations.Hybris.AutoshipShipment (NOLOCK) ash ON ash.AutoshipID = a.AutoshipID
--					INNER JOIN RFOperations.Hybris.AutoshipPaymentAddress (NOLOCK) apa ON apa.AutoShipID = a.AutoshipID
--					INNER JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) asha ON asha.AutoShipID = a.AutoshipID
--					INNER JOIN Hybris.dbo.users u ON a.AccountID = u.p_rfaccountid
--													 AND u.p_sourcename = 'Hybris-DM'
--			WHERE   a.CountryID = 236
--					AND a.AutoshipID NOT IN (
--					SELECT  AutoshipID
--					FROM    Hybris.Autoship
--					WHERE   AccountID IN (
--							SELECT  a.AccountID
--							FROM    Hybris.Autoship a
--									INNER JOIN RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
--							WHERE   ab.AccountTypeID = 1
--									AND a.CountryID = 236
--									AND a.AutoshipTypeID = 2
--									AND a.Active = 1
--							GROUP BY a.AccountID
--							HAVING  COUNT(*) > 1 )
--							AND AutoshipID NOT IN (
--							SELECT  MAX(AutoshipID) AutoshipID
--							FROM    Hybris.Autoship a
--									INNER JOIN RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
--							WHERE   ab.AccountTypeID = 1
--									AND a.CountryID = 236
--									AND a.AutoshipTypeID = 2
--									AND a.Active = 1
--							GROUP BY a.AccountID
--							HAVING  COUNT(*) > 1 )
--							AND CountryID = 236
--							AND AutoshipTypeID = 2 )
--					AND a.AutoshipNumber NOT IN ( SELECT    OrderNumber
--												  FROM      Hybris.Orders
--												  WHERE     CountryID = 236 )
--			GROUP BY a.AutoshipID ,
--					a.AutoshipNumber ,
--					a.AccountID ,
--					u.PK ,
--					a.AutoshipStatusID ,
--					a.AutoshipTypeID ,
--					a.StartDate ,
--					a.TotalTax;


--			CREATE CLUSTERED INDEX as_cls1 ON #tempact (AutoshipID);

--			SELECT  'Validation of column to column with no transformation in progress' AS [Step-2 Validation] ,
--					GETDATE() AS StartTime;
--			SET @cnt = 1;
--			SELECT  @lt_1 = COUNT(*)
--			FROM    DataMigration.dbo.map_tab
--			WHERE   flag = 'c2c'
--					AND rfo_column <> @RFO_key
--					AND [owner] = '824-Autoship';
--			 --and prev_run_err > 0

--			WHILE @cnt <= @lt_1
--				BEGIN

--					SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner] + ''', ''' + flag
--							+ ''', ''' + [RFO_Reference Table] + ''' as rfo_column, '''
--							+ Hybris_Column + ''' as hybris_column, A.' + @HYB_key
--							+ ' as hyb_key, A.' + Hybris_Column + ' as hyb_value, B.'
--							+ @RFO_key + ' as rfo_key, B.RFO_Col as rfo_value

--			FROM (SELECT a.' + @HYB_key + ', a.' + Hybris_Column + ' FROM hybris.dbo.'
--							+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
--							+ @RFO_key + '
--			except
--			SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
--							+ [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
--							+ @RFO_key + '=b.' + @RFO_key + ') A  

--			LEFT JOIN

--			(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
--							+ [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
--							+ @RFO_key + '=b.' + @RFO_key + '
--			except
--			SELECT a.' + @HYB_key + ', a.' + Hybris_Column + ' FROM hybris.dbo.'
--							+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
--							+ @RFO_key + ') B
--			ON A.' + @HYB_key + '=B.' + @RFO_key + '
--			UNION
--			SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', '''
--							+ [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
--							+ @HYB_key + ', A.' + Hybris_Column + ', B.' + @RFO_key
--							+ ',B.RFO_Col

--			FROM (SELECT a.' + @HYB_key + ', a.' + Hybris_Column + ' FROM hybris.dbo.'
--							+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
--							+ @RFO_key + '
--			except
--			SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
--							+ [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
--							+ @RFO_key + '=b.' + @RFO_key + ') A  

--			RIGHT JOIN

--			(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
--							+ [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
--							+ @RFO_key + '=b.' + @RFO_key + '
--			except
--			SELECT a.' + @HYB_key + ', a.' + Hybris_Column + ' FROM hybris.dbo.'
--							+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
--							+ @RFO_key + ') B
--			ON A.' + @HYB_key + '=B.' + @RFO_key + ''
--					FROM    ( SELECT    * ,
--										ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
--							  FROM      DataMigration.dbo.map_tab
--							  WHERE     flag = 'c2c'
--										AND rfo_column <> @RFO_key
--			--and prev_run_err > 0
--										AND [owner] = '824-Autoship'
--							) temp
--					WHERE   rn = @cnt; 

--					PRINT @sql_gen_1;
--					INSERT  INTO @temp
--							( test_area ,
--							  test_type ,
--							  rfo_column ,
--							  hybris_column ,
--							  hyb_key ,
--							  hyb_value ,
--							  rfo_key ,
--							  rfo_value
--							)
--							EXEC sp_executesql @sql_gen_1;

--					IF ( SELECT COUNT(*)
--						 FROM   @temp
--					   ) > 1
--						BEGIN
--							DECLARE @err_cnt INT;
--							SELECT  @err_cnt = CASE WHEN hyb_cnt = 0 THEN rfo_cnt
--													ELSE hyb_cnt
--											   END
--							FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
--												COUNT(DISTINCT rfo_key) rfo_cnt
--									  FROM      @temp
--									) t1;

--							UPDATE  a
--							SET     [prev_run_err] = @err_cnt
--							FROM    DataMigration.dbo.map_tab a ,
--									@temp b
--							WHERE   a.hybris_column = b.hybris_column
--									AND [owner] = '824-Autoship'; 
--						END;	

--					INSERT  INTO DataMigration.dbo.dm_log
--							SELECT TOP 5
--									test_area ,
--									test_type ,
--									rfo_column ,
--									hybris_column ,
--									hyb_key ,
--									hyb_value ,
--									rfo_key ,
--									rfo_value
--							FROM    @temp
--							WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key, '~') )
--									  AND ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
--																		  '~') )
--									)
--							UNION
--							SELECT TOP 5
--									test_area ,
--									test_type ,
--									rfo_column ,
--									hybris_column ,
--									hyb_key ,
--									hyb_value ,
--									rfo_key ,
--									rfo_value
--							FROM    @temp
--							WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key, '~') )
--									  OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
--																		  '~') )
--									);

--					DELETE  FROM @temp;

--					SET @cnt = @cnt + 1;

--				END;

--			UPDATE  DataMigration.dbo.map_tab
--			SET     [prev_run_err] = 0
--			WHERE   [owner] = '824-Autoship'
--					AND flag = 'c2c'
--					AND hybris_column NOT IN ( SELECT DISTINCT
--														hybris_column
--											   FROM     DataMigration..dm_log
--											   WHERE    test_area = '824-Autoship'
--														AND test_type = 'c2c' );

----			===========================================================================
----
----		================================================================================


--		SELECT  'Step-2 Completed, Validation of default columns in progress' AS [Step-3 Validation] ,
--				GETDATE() AS StartTime;

--		----Defaults Check
--		SET @cnt = 1;
--		SELECT  @lt_1 = COUNT(*)
--		FROM    DataMigration.dbo.map_tab
--		WHERE   flag = 'defaults'
--				AND [owner] = '824-Autoship'
--				AND [RFO_Reference Table] = 'NULL';
--		 --and prev_run_err > 0
--		SELECT  @lt_2 = COUNT(*)
--		FROM    DataMigration.dbo.map_tab
--		WHERE   flag = 'defaults'
--				AND [owner] = '824-Autoship'
--				AND [RFO_Reference Table] <> 'NULL';
--		 --and prev_run_err > 0

--		WHILE ( @cnt <= @lt_1
--				AND @cnt <= @lt_2
--			  )
--			BEGIN
--				IF ( SELECT COUNT(*)
--					 FROM   DataMigration.dbo.map_tab
--					 WHERE  flag = 'defaults'
--							AND [owner] = '824-Autoship'
--							AND [RFO_Reference Table] = 'NULL'
--				   ) > 1
--					BEGIN
--						SELECT  @sql_gen_1 = 'use rfoperations
--				select distinct ''' + [owner] + ''' as test_area, ''' + flag
--								+ ''' as test_type, null as rfo_column, '''
--								+ Hybris_Column + ''' as hybris_column, a.' + @HYB_key
--								+ ', ' + hybris_column
--								+ ', null as rfo_key, null as rfo_value
--				from hybris.dbo.' + Hybris_Table + ' a, #tempact b 
--				where a.' + @HYB_key + '=b.' + @RFO_key + '
--				and ' + hybris_column + ' is not null'
--						FROM    ( SELECT    * ,
--											ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
--								  FROM      DataMigration.dbo.map_tab
--								  WHERE     flag = 'defaults'
--											AND [RFO_Reference Table] = 'NULL'
--				--and prev_run_err > 0
--											AND [owner] = '824-Autoship'
--								) temp
--						WHERE   rn = @cnt;
--					END;

--				PRINT @sql_gen_1;
--				INSERT  INTO @temp
--						( test_area ,
--						  test_type ,
--						  rfo_column ,
--						  hybris_column ,
--						  hyb_key ,
--						  hyb_value ,
--						  rfo_key ,
--						  rfo_value
--						)
--						EXEC sp_executesql @sql_gen_1;

--				IF ( SELECT COUNT(*)
--					 FROM   @temp
--				   ) > 1
--					BEGIN
--				--declare @err_cnt int
--						SELECT  @err_cnt = CASE WHEN hyb_cnt = 0 THEN rfo_cnt
--												ELSE hyb_cnt
--										   END
--						FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
--											COUNT(DISTINCT rfo_key) rfo_cnt
--								  FROM      @temp
--								) t1;

--						UPDATE  a
--						SET     [prev_run_err] = @err_cnt
--						FROM    DataMigration.dbo.map_tab a ,
--								@temp b
--						WHERE   a.hybris_column = b.hybris_column
--								AND [owner] = '824-Autoship'; 
--					END;	

--				INSERT  INTO DataMigration.dbo.dm_log
--						SELECT TOP 5
--								test_area ,
--								test_type ,
--								rfo_column ,
--								hybris_column ,
--								hyb_key ,
--								hyb_value ,
--								rfo_key ,
--								rfo_value
--						FROM    @temp
--						WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key, '~') )
--								  AND ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
--																	  '~') )
--								)
--						UNION
--						SELECT TOP 5
--								test_area ,
--								test_type ,
--								rfo_column ,
--								hybris_column ,
--								hyb_key ,
--								hyb_value ,
--								rfo_key ,
--								rfo_value
--						FROM    @temp
--						WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key, '~') )
--								  OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
--																	  '~') )
--								);

--			--select * from datamigration.dbo.dm_log where test_type = 'defaults'

--				IF ( SELECT COUNT(*)
--					 FROM   DataMigration.dbo.map_tab
--					 WHERE  flag = 'defaults'
--							AND [owner] = '824-Autoship'
--							AND [RFO_Reference Table] <> 'NULL'
--				   ) > 1
--					BEGIN
--						SELECT  @sql_gen_2 = 'use rfoperations
--				select distinct ''' + [owner] + ''' as test_area, ''' + flag
--								+ ''' as test_type, null as rfo_column, '''
--								+ Hybris_Column + ''' as hybris_column, a.' + @HYB_key
--								+ ', ' + hybris_column
--								+ ', null as rfo_key, null as rfo_value
--				from hybris.dbo.' + Hybris_Table + ' a, #tempact b 
--				where a.' + @HYB_key + '=b.' + @RFO_key + '
--				and ' + hybris_column + ' <> ''' + [RFO_Reference Table] + ''''
--						FROM    ( SELECT    * ,
--											ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
--								  FROM      DataMigration.dbo.map_tab
--								  WHERE     flag = 'defaults'
--											AND [RFO_Reference Table] <> 'NULL'
--				--and prev_run_err > 0
--											AND [owner] = '824-Autoship'
--								) temp
--						WHERE   rn = @cnt;
--					END;

--				PRINT @sql_gen_2;
--				INSERT  INTO @temp
--						( test_area ,
--						  test_type ,
--						  rfo_column ,
--						  hybris_column ,
--						  hyb_key ,
--						  hyb_value ,
--						  rfo_key ,
--						  rfo_value
--						)
--						EXEC sp_executesql @sql_gen_2;

--				IF ( SELECT COUNT(*)
--					 FROM   @temp
--				   ) > 1
--					BEGIN
--				--declare @err_cnt int
--						SELECT  @err_cnt = CASE WHEN hyb_cnt = 0 THEN rfo_cnt
--												ELSE hyb_cnt
--										   END
--						FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
--											COUNT(DISTINCT rfo_key) rfo_cnt
--								  FROM      @temp
--								) t1;

--						UPDATE  a
--						SET     [prev_run_err] = @err_cnt
--						FROM    DataMigration.dbo.map_tab a ,
--								@temp b
--						WHERE   a.hybris_column = b.hybris_column
--								AND [owner] = '824-Autoship'; 
--					END;	

--				INSERT  INTO DataMigration.dbo.dm_log
--						SELECT TOP 5
--								test_area ,
--								test_type ,
--								rfo_column ,
--								hybris_column ,
--								hyb_key ,
--								hyb_value ,
--								rfo_key ,
--								rfo_value
--						FROM    @temp
--						WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key, '~') )
--								  AND ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
--																	  '~') )
--								)
--						UNION
--						SELECT TOP 5
--								test_area ,
--								test_type ,
--								rfo_column ,
--								hybris_column ,
--								hyb_key ,
--								hyb_value ,
--								rfo_key ,
--								rfo_value
--						FROM    @temp
--						WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key, '~') )
--								  OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
--																	  '~') )
--								);

--				DELETE  FROM @temp;

--				SET @cnt = @cnt + 1;

--			END;

--		UPDATE  DataMigration.dbo.map_tab
--		SET     [prev_run_err] = 0
--		WHERE   [owner] = '824-Autoship'
--				AND flag = 'defaults'
--				AND hybris_column NOT IN ( SELECT DISTINCT
--													hybris_column
--										   FROM     DataMigration..dm_log
--										   WHERE    test_area = '824-Autoship'
--													AND test_type = 'defaults' )

----		===============================================================================

----		===============================================================================


--			SELECT  'Step-3 completed, Validation of transformed columns in progress' AS [Step-4 Validation] ,
--					GETDATE() AS StartTime;

--			--Transformed Columns Validation --10:16 mins
--			SET @cnt = 1;
--			SELECT  @lt_1 = COUNT(*)
--			FROM    DataMigration.dbo.map_tab
--			WHERE   flag = 'manual'
--					AND rfo_column <> @RFO_key
--					AND [owner] = '824-Autoship';
--			 --and prev_run_err > 0

--			WHILE @cnt <= @lt_1
--				BEGIN

--					SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner]
--							+ ''' as test_area, ''' + flag + ''' as test_type, '''
--							+ [RFO_Reference Table] + ''' as rfo_column, '''
--							+ Hybris_Column + ''' as hybris_column, A.' + @HYB_key
--							+ ' as hyb_key, A.Hyb_Trans_col as hyb_value, B.' + @RFO_key
--							+ ' as rfo_key, B.RFO_Trans_Col as rfo_value

--			FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
--							+ ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
--							+ ' a, #tempact b where a.' + @HYB_key + '=b.' + @RFO_key + '
--			except
--			SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
--							+ RFO_Table + ') A  

--			LEFT JOIN

--			(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
--							+ RFO_Table + '
--			except
--			SELECT a.' + @HYB_key + ', ' + Hybris_Column
--							+ ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
--							+ ' a, #tempact b where a.' + @HYB_key + '=b.' + @RFO_key
--							+ ') B
--			ON A.' + @HYB_key + '=B.' + @RFO_key + '
--			UNION
--			SELECT DISTINCT  ''' + [owner] + ''', ''' + flag + ''', '''
--							+ [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
--							+ @HYB_key + ', A.Hyb_Trans_col, B.' + @RFO_key
--							+ ', B.RFO_Trans_Col

--			FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
--							+ ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
--							+ ' a, #tempact b where a.' + @HYB_key + '=b.' + @RFO_key + '
--			except
--			SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
--							+ RFO_Table + ') A  

--			RIGHT JOIN

--			(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
--							+ RFO_Table + '
--			except
--			SELECT a.' + @HYB_key + ', ' + Hybris_Column
--							+ ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
--							+ ' a, #tempact b where a.' + @HYB_key + '=b.' + @RFO_key
--							+ ') B
--			ON A.' + @HYB_key + '=B.' + @RFO_key + ''
--					FROM    ( SELECT    * ,
--										ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
--							  FROM      DataMigration.dbo.map_tab
--							  WHERE     flag = 'manual'
--			--and rfo_column <> @RFO_key 
--										AND id NOT IN ( 69, 70 ) --Data types are image on Hybris end. This value is generated by the system
--										AND id NOT IN ( 3, 4, 64, 65 ) --order not migrated yet
--			--and prev_run_err > 0
--										AND [owner] = '824-Autoship'
--							) temp
--					WHERE   rn = @cnt;

--					PRINT @sql_gen_1;
--					INSERT  INTO @temp
--							( test_area ,
--							  test_type ,
--							  rfo_column ,
--							  hybris_column ,
--							  hyb_key ,
--							  hyb_value ,
--							  rfo_key ,
--							  rfo_value
--							)
--							EXEC sp_executesql @sql_gen_1;


--					IF ( SELECT COUNT(*)
--						 FROM   @temp
--					   ) > 1
--						BEGIN
--				--declare @err_cnt int
--							SELECT  @err_cnt = CASE WHEN hyb_cnt = 0 THEN rfo_cnt
--													ELSE hyb_cnt
--											   END
--							FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
--												COUNT(DISTINCT rfo_key) rfo_cnt
--									  FROM      @temp
--									) t1;

--							UPDATE  a
--							SET     [prev_run_err] = @err_cnt
--							FROM    DataMigration.dbo.map_tab a ,
--									@temp b
--							WHERE   a.hybris_column = b.hybris_column
--									AND [owner] = '824-Autoship'; 
--						END;	

--					INSERT  INTO DataMigration..dm_log
--							SELECT TOP 5
--									test_area ,
--									test_type ,
--									rfo_column ,
--									hybris_column ,
--									hyb_key ,
--									hyb_value ,
--									rfo_key ,
--									rfo_value
--							FROM    @temp
--							WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key, '~') )
--									  AND ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
--																		  '~') )
--									)
--							UNION
--							SELECT TOP 5
--									test_area ,
--									test_type ,
--									rfo_column ,
--									hybris_column ,
--									hyb_key ,
--									hyb_value ,
--									rfo_key ,
--									rfo_value
--							FROM    @temp
--							WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key, '~') )
--									  OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
--																		  '~') )
--									);

--					DELETE  FROM @temp;

--					SET @cnt = @cnt + 1;

--				END;

--			UPDATE  DataMigration.dbo.map_tab
--			SET     [prev_run_err] = 0
--			WHERE   [owner] = '824-Autoship'
--					AND flag = 'manual'
--					AND hybris_column NOT IN ( SELECT DISTINCT
--														hybris_column
--											   FROM     DataMigration..dm_log
--											   WHERE    test_area = '824-Autoship'
--														AND test_type = 'manual' );



--			SELECT  'VALIDATION COMPLETED' [Status] ,
--					[total no of columns] ,
--					[columns passed] ,
--					[total no of columns] - [columns passed] AS [Required Analysis] ,
--					GETDATE() AS EndTime
--			FROM    ( SELECT    COUNT(cnt) AS [columns passed]
--					  FROM      ( SELECT DISTINCT
--											hybris_column AS cnt
--								  FROM      DataMigration.dbo.map_tab
--								  WHERE     [owner] = '824-Autoship'
--											AND flag IN ( 'c2c', 'manual', 'defaults' )
--								  EXCEPT
--								  SELECT DISTINCT
--											hybris_column
--								  FROM      DataMigration..dm_log
--								  WHERE     test_area = '824-Autoship'
--								) a
--					) tab1 ,
--					( SELECT    COUNT(id) AS [total no of columns]
--					  FROM      DataMigration.dbo.map_tab
--					  WHERE     [owner] = '824-Autoship'
--								AND flag IN ( 'c2c', 'manual', 'defaults' )
--					) tab2;

--			SET STATISTICS TIME OFF;
--			GO


SELECT *FROM datamigration..dm_log
WHERE test_area='824-autoship'