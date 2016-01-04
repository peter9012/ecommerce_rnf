
		USE RFOperations;
		SET STATISTICS TIME ON;
		GO

		SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
				
		DECLARE @HYB_key VARCHAR(100) = 'code';
		DECLARE @RFO_key VARCHAR(100) = 'ReturnOrderId';
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


		SELECT  CASE WHEN COUNT(1) > 0 THEN 'duplicates found'
					 ELSE 'no duplicates - validation passed'
				END AS [step-1 validation]
		FROM    ( SELECT    COUNT(*) cnt ,
							hpt.p_order ,
							hpt.p_info --, a.ownerpkstring  
				  FROM      Hybris.dbo.paymentinfos (NOLOCK) hpi
							JOIN Hybris.dbo.users u ON u.PK = hpi.userpk
													   AND u.p_sourcename = 'hybris-dm'
							JOIN Hybris.dbo.countries c ON c.PK = u.p_country
														   AND c.isocode = 'us'
							JOIN Hybris.dbo.orders (NOLOCK) ho ON ho.PK = hpi.OwnerPkString
																  AND ho.TypePkString = 8796127723602
																  AND ho.p_template IS NULL
							JOIN Hybris.dbo.paymenttransactions hpt ON hpt.p_info = hpi.PK
																	  AND hpi.duplicate = 1
				  GROUP BY  hpt.p_order ,
							hpt.p_info
				  HAVING    COUNT(*) > 1
				) t1;

				
		SELECT  hybris_cnt ,
				rfo_cnt ,
				CASE WHEN hybris_cnt > rfo_cnt THEN 'hybris count more than rfo count'
					 WHEN rfo_cnt > hybris_cnt THEN 'rfo count more than hybris count'
					 ELSE 'count matches - validation passed'
				END results
		FROM    ( SELECT    COUNT(DISTINCT hpi.code) hybris_cnt
				  FROM      Hybris.dbo.orders ho
							JOIN Hybris.dbo.users u ON ho.userpk = u.PK
													   AND u.p_sourcename = 'hybris-dm'
							JOIN Hybris..countries c ON c.PK = u.p_country
														AND c.isocode = 'us'
							JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
																AND hpi.duplicate = 1
																AND ho.TypePkString = 8796127723602
																AND ISNULL(ho.p_template,0)=0
							JOIN Hybris.dbo.paymenttransactions hpt ON hpt.p_order = ho.PK
							
						
				) t1 , --105789
				( SELECT    COUNT(DISTINCT rpt.ReturnPaymentId) rfo_cnt
				  FROM      RFOperations.Hybris.ReturnOrder ro
							JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderID = rfl.orderID
                                                             AND rfl.orderTypeID = 9
							JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
													   AND ro.CountryID = 236
													   AND u.p_sourcename = 'hybris-dm'
							JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID
							JOIN Hybris..orders ho ON ho.code = ro.ReturnOrderID
							JOIN Hybris.ReturnPaymentTransaction rpt ON rpt.ReturnPaymentId = rp.ReturnPaymentId
							JOIN RodanFieldsLive.dbo.OrderPayments rop ON rop.OrderPaymentID=rp.ReturnPaymentId
							WHERE  AccountNumber <> 'HDCm5F9HLZ6JyWpnoVViLw==' AND (LTRIM(RTRIM(rop.BillingFirstName))<>''
							OR LTRIM(RTRIM(rop.BillingLastName))<>'')
				) t2; --149286

		IF OBJECT_ID('tempdb..#missingcounts') IS NOT NULL
			DROP TABLE #missingcounts;

		SELECT  hybris.p_code ,
				rfo.code ,
				CASE WHEN hybris.p_code IS NULL THEN 'missing in hybris'
					 WHEN rfo.code IS NULL THEN 'missing in rfo'
				END results
		INTO    #missingcounts
		FROM    ( SELECT    hpt.p_code
				  FROM      Hybris.dbo.orders ho
							JOIN Hybris.dbo.users u ON ho.userpk = u.PK
													   AND u.p_sourcename = 'hybris-dm'
							JOIN Hybris..countries c ON c.PK = u.p_country
														AND c.isocode = 'us'
							JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
																AND hpi.duplicate = 1
																AND ho.TypePkString = 8796127723602
																AND ho.p_template IS NULL
							JOIN Hybris.dbo.paymenttransactions hpt ON hpt.p_order = ho.pk
				) hybris
				FULL OUTER JOIN ( SELECT    ro.ReturnOrderNumber + '_'
											+ CAST(rp.ReturnPaymentId AS NVARCHAR) AS code
								  FROM      RFOperations.Hybris.ReturnOrder ro
											 JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderID = rfl.orderID
                                                             AND rfl.orderTypeID = 9
											JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
																	  AND ro.CountryID = 236
																	  AND u.p_sourcename = 'hybris-dm'
											JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID
											JOIN Hybris..orders ho ON ho.code = ro.ReturnOrderID
											JOIN Hybris.ReturnPaymentTransaction rpt ON rpt.ReturnPaymentId = rp.ReturnPaymentId
											JOIN RodanFieldsLive.dbo.OrderPayments rop ON rop.OrderPaymentID=rp.ReturnPaymentId
											WHERE  AccountNumber <> 'HDCm5F9HLZ6JyWpnoVViLw==' AND (LTRIM(RTRIM(rop.BillingFirstName))<>''
							OR LTRIM(RTRIM(rop.BillingLastName))<>'')
								) rfo ON hybris.p_code = rfo.code
								WHERE rfo.code IS NULL OR hybris.p_code IS NULL 
								


		DECLARE @dupcounts INT;
		SELECT  @dupcounts = COUNT(*)
		FROM    #missingcounts;
		
		IF @dupcounts > 0
			UPDATE  DataMigration..map_tab
			SET     prev_run_err = @dupcounts
			WHERE   [owner] = '853-returnpaymenttransaction'
					AND [rfo_column ] = 'a.code';

		SELECT TOP 10
				*
		FROM    #missingcounts
		WHERE   code IS NULL; 
		
		SELECT TOP 10
				*
		FROM    #missingcounts
		WHERE   p_code IS NULL; 

		--DROP TABLE #missingcounts;

		SELECT  'Validation of column to column Started' AS [Step-2 Validation] ,
				GETDATE() AS StartTime;

		DELETE  FROM DataMigration.dbo.dm_log
		WHERE   test_area = '853-ReturnPaymentTransaction';
		IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
			DROP TABLE #tempact;

		SELECT  a.ReturnOrderID ,
				a.OrderID ,
				ReturnOrderNumber ,
				a.AccountID ,
				b.PK ,
				c.ReturnPaymentId ,
				c.VendorID ,
				c.AmountToBeAuthorized ,
				c.ProcessOnDate ,
				a.ReturnOrderNumber + '_' + CAST(c.ReturnPaymentId AS NVARCHAR) AS code
		INTO    #tempact
		FROM    RFOperations.Hybris.ReturnOrder a  JOIN RodanFieldsLive.dbo.Orders rfl ON A.ReturnOrderID = rfl.orderID
                                                             AND rfl.orderTypeID = 9,
				Hybris.dbo.users b ,
				RFOperations.Hybris.ReturnPayment c ,
				Hybris.dbo.paymentinfos d,
				RodanFieldsLive.dbo.OrderPayments rop 
		WHERE   a.AccountID = b.p_rfaccountid
				AND a.ReturnOrderID = c.ReturnOrderID
				AND c.ReturnPaymentId = d.PK
				AND CountryID = 236
				AND b.p_sourcename = 'Hybris-DM'
				AND a.ReturnOrderNumber NOT IN (
				SELECT  a.ReturnOrderNumber
				FROM    Hybris.ReturnOrder a
						JOIN Hybris.Orders b ON a.ReturnOrderNumber = b.OrderNumber
												AND a.CountryID = 236 )
				AND a.ReturnOrderNumber <> '11030155' --AS no same as Return no
				AND a.ReturnOrderID IN ( SELECT ReturnOrderID
										 FROM   Hybris.ReturnItem )
				AND  rop.OrderPaymentID=c.ReturnPaymentId
				AND  rop.AccountNumber <> 'HDCm5F9HLZ6JyWpnoVViLw==' 
				AND (LTRIM(RTRIM(rop.BillingFirstName))<>''
					OR LTRIM(RTRIM(rop.BillingLastName))<>'')
		GROUP BY a.ReturnOrderID ,
				a.OrderID ,
				ReturnOrderNumber ,
				a.AccountID ,
				b.PK ,
				c.ReturnPaymentId ,
				c.VendorID ,
				d.code ,
				c.AmountToBeAuthorized ,
				c.ProcessOnDate ,
				a.ReturnOrderNumber + '_' + CAST(c.ReturnPaymentId AS NVARCHAR);

		CREATE CLUSTERED INDEX as_cls1 ON #tempact (ReturnOrderID);

		

		SELECT  'Validation of column to column with no transformation in progress' AS [Step-2 Validation] ,
				GETDATE() AS StartTime;
		SET @cnt = 1;
		SELECT  @lt_1 =   COUNT(*)
		FROM    DataMigration.dbo.map_tab
		WHERE   flag = 'c2c'
				--AND [Hybris_Column ] <> @HYB_key
				AND [owner] = '853-ReturnPaymentTransaction';

		WHILE @cnt <= @lt_1
			BEGIN

				SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner] + ''', ''' + flag
						+ ''', ''' + [RFO_Reference Table] + ''' as rfo_column, '''
						+ Hybris_Column + ''' as hybris_column, A.' + @HYB_key
						+ ' as hyb_key, A.' + Hybris_Column + ' as hyb_value, B.'
						+ @RFO_key + ' as rfo_key, B.RFO_Col as rfo_value

		FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM Hybris.dbo.'
						+ Hybris_Table + ' a, 
						Hybris..orders ho,
						Hybris..users u, 
						Hybris..paymentinfos hpi,
						#tempact b 
						where ho.' + @HYB_key + '=b.'+ @RFO_key + ' 
						AND a.p_order = ho.pk
						AND hpi.OwnerPkString = ho.PK
						AND  ho.userpk = u.PK
						AND hpi.duplicate = 1
						AND ho.TypePkString = 8796127723602
						AND ho.p_template IS NULL 
						AND u.P_country=8796100624418
		except
		SELECT a.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM ' + RFO_Table
						+ ' , #tempact b where a.' + @RFO_key + '=b.' + @RFO_key
						+ ') A  

		LEFT JOIN

		(SELECT a.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM ' + RFO_Table
						+ ' , #tempact b where a.' + @RFO_key + '=b.' + @RFO_key + '
		except
		SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM Hybris.dbo.'
						+ Hybris_Table + ' a, Hybris..orders ho,
						Hybris..users u, 
						Hybris..paymentinfos hpi,
						#tempact b 
						where ho.' + @HYB_key + '=b.'+ @RFO_key + ' 
						AND a.p_order = ho.pk
						AND hpi.OwnerPkString = ho.PK
						AND  ho.userpk = u.PK
						AND hpi.duplicate = 1
						AND ho.TypePkString = 8796127723602
						AND ho.p_template IS NULL 
						AND u.P_country=8796100624418) B
		ON A.' + @HYB_key + '=B.' + @RFO_key + '
		UNION
		SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', '''
						+ [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
						+ @HYB_key + ', A.' + Hybris_Column + ', B.' + @RFO_key
						+ ',B.RFO_Col

		FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM Hybris.dbo.'
						+ Hybris_Table + ' a,
						Hybris..orders ho,
						Hybris..users u, 
						Hybris..paymentinfos hpi,
						#tempact b 
						where ho.' + @HYB_key + '=b.'+ @RFO_key + ' 
						AND a.p_order = ho.pk
						AND hpi.OwnerPkString = ho.PK
						AND  ho.userpk = u.PK
						AND hpi.duplicate = 1
						AND ho.TypePkString = 8796127723602
						AND ho.p_template IS NULL 
						AND u.P_country=8796100624418
		except
		SELECT a.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM ' + RFO_Table
						+ ', #tempact b where a.' + @RFO_key + '=b.' + @RFO_key
						+ ') A  

		RIGHT JOIN

		(SELECT a.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM ' + RFO_Table
						+ ' , #tempact b where a.' + @RFO_key + '=b.' + @RFO_key + '
		except
		SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM Hybris.dbo.'
						+ Hybris_Table + ' a, 
						Hybris..orders ho,
						Hybris..users u, 
						Hybris..paymentinfos hpi,
						#tempact b 
						where ho.' + @HYB_key + '=b.'+ @RFO_key + ' 
						AND a.p_order = ho.pk
						AND hpi.OwnerPkString = ho.PK
						AND  ho.userpk = u.PK
						AND hpi.duplicate = 1
						AND ho.TypePkString = 8796127723602
						AND ho.p_template IS NULL 
						AND u.P_country=8796100624418) B
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

		UPDATE  DataMigration.dbo.map_tab
		SET     [prev_run_err] = 0
		WHERE   [owner] = '853-ReturnPaymentTransaction'
				AND flag = 'c2c'
				AND hybris_column NOT IN (
				SELECT DISTINCT
						hybris_column
				FROM    DataMigration..dm_log
				WHERE   test_area = '853-ReturnPaymentTransaction'
						AND test_type = 'c2c' );


		SELECT  'Step-2 Completed, Validation of default columns in progress' AS [Step-3 Validation] ,
				GETDATE() AS StartTime;

		----Defaults Check
		SET @cnt = 1;
		SELECT  @lt_1 = COUNT(*)
		FROM    DataMigration.dbo.map_tab
		WHERE   flag = 'defaults'
				AND [owner] = '853-ReturnPaymentTransaction'
				AND [RFO_Reference Table] = 'NULL';
		SELECT  @lt_2 = COUNT(*)
		FROM    DataMigration.dbo.map_tab
		WHERE   flag = 'defaults'
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
								+ Hybris_Column + ''' as hybris_column, ho.' + @HYB_key
								+ ', a.' + hybris_column
								+ ', null as rfo_key, null as rfo_value
				from hybris.dbo.' + Hybris_Table + ' a, 
						Hybris..orders ho,
						Hybris..users u, 
						Hybris..paymentinfos hpi,
						#tempact b 
						where ho.' + @HYB_key + '=b.'+ @RFO_key + ' 
						AND a.p_order = ho.pk
						AND hpi.OwnerPkString = ho.PK
						AND  ho.userpk = u.PK
						AND hpi.duplicate = 1
						AND ho.TypePkString = 8796127723602
						AND ho.p_template IS NULL 
						AND u.P_country=8796100624418
				and a.' + hybris_column + ' is not null'
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
								+ Hybris_Column + ''' as hybris_column, ho.' + @HYB_key
								+ ', a.' + hybris_column
								+ ', null as rfo_key, null as rfo_value
				from hybris.dbo.' + Hybris_Table + ' a, 
						Hybris..orders ho,
						Hybris..users u, 
						Hybris..paymentinfos hpi,
						#tempact b 
						where ho.' + @HYB_key + '=b.'+ @RFO_key + ' 
						AND a.p_order = ho.pk
						AND hpi.OwnerPkString = ho.PK
						AND  ho.userpk = u.PK
						AND hpi.duplicate = 1
						AND ho.TypePkString = 8796127723602
						AND ho.p_template IS NULL 
						AND u.P_country=8796100624418
				and a.' + hybris_column + ' <> ''' + [RFO_Reference Table] + ''''
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

		UPDATE  DataMigration.dbo.map_tab
		SET     [prev_run_err] = 0
		WHERE   [owner] = '853-ReturnPaymentTransaction'
				AND flag = 'defaults'
				AND hybris_column NOT IN (
				SELECT DISTINCT
						hybris_column
				FROM    DataMigration..dm_log
				WHERE   test_area = '853-ReturnPaymentTransaction'
						AND test_type = 'defaults' );



		SELECT  'Step-3 completed, Validation of transformed columns in progress' AS [Step-4 Validation] ,
				GETDATE() AS StartTime;

		--Transformed Columns Validation --10:16 mins
		SET @cnt = 1;
		SELECT  @lt_1 = COUNT(*)
		FROM    DataMigration.dbo.map_tab
		WHERE   flag = 'manual'
				AND rfo_column <> @RFO_key
				AND [owner] = '853-ReturnPaymentTransaction';

		WHILE @cnt <= @lt_1
			BEGIN

				SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner]
						+ ''' as test_area, ''' + flag + ''' as test_type, '''
						+ [RFO_Reference Table] + ''' as rfo_column, '''
						+ Hybris_Column + ''' as hybris_column, A.' + @HYB_key
						+ ' as hyb_key, A.Hyb_Trans_col as hyb_value, B.' + @RFO_key
						+ ' as rfo_key, B.RFO_Trans_Col as rfo_value

		FROM (SELECT ho.' + @HYB_key + ', a.' + Hybris_Column
						+ ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
						+ ' a, 
						Hybris..orders ho,
						Hybris..users u, 
						Hybris..paymentinfos hpi,
						#tempact b 
						where ho.' + @HYB_key + '=b.'+ @RFO_key + ' 
						AND a.p_order = ho.pk
						AND hpi.OwnerPkString = ho.PK
						AND  ho.userpk = u.PK
						AND hpi.duplicate = 1
						AND ho.TypePkString = 8796127723602
						AND ho.p_template IS NULL 
						AND u.P_country=8796100624418
		except
		SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
						+ RFO_Table + ') A  

		LEFT JOIN

		(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
						+ RFO_Table + '
		except
		SELECT ho.' + @HYB_key + ',a. ' + Hybris_Column
						+ ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
						+ ' a, Hybris..orders ho,
						Hybris..users u, 
						Hybris..paymentinfos hpi,
						#tempact b 
						where ho.' + @HYB_key + '=b.'+ @RFO_key + ' 
						AND a.p_order = ho.pk
						AND hpi.OwnerPkString = ho.PK
						AND  ho.userpk = u.PK
						AND hpi.duplicate = 1
						AND ho.TypePkString = 8796127723602
						AND ho.p_template IS NULL 
						AND u.P_country=8796100624418) B
		ON A.' + @HYB_key + '=B.' + @RFO_key + '
		UNION
		SELECT DISTINCT  ''' + [owner] + ''', ''' + flag + ''', '''
						+ [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
						+ @HYB_key + ', A.Hyb_Trans_col, B.' + @RFO_key
						+ ', B.RFO_Trans_Col

		FROM (SELECT ho.' + @HYB_key + ',a. ' + Hybris_Column
						+ ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
						+ ' a, Hybris..orders ho,
						Hybris..users u, 
						Hybris..paymentinfos hpi,
						#tempact b 
						where ho.' + @HYB_key + '=b.'+ @RFO_key + ' 
						AND a.p_order = ho.pk
						AND hpi.OwnerPkString = ho.PK
						AND  ho.userpk = u.PK
						AND hpi.duplicate = 1
						AND ho.TypePkString = 8796127723602
						AND ho.p_template IS NULL 
						AND u.P_country=8796100624418
		except
		SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
						+ RFO_Table + ') A  

		RIGHT JOIN

		(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
						+ RFO_Table + '
		except
		SELECT ho.' + @HYB_key + ',a. ' + Hybris_Column
						+ ' as Hyb_Trans_col FROM hybris.dbo.' + Hybris_Table
						+ ' a,Hybris..orders ho,
						Hybris..users u, 
						Hybris..paymentinfos hpi,
						#tempact b 
						where ho.' + @HYB_key + '=b.'+ @RFO_key + ' 
						AND a.p_order = ho.pk
						AND hpi.OwnerPkString = ho.PK
						AND  ho.userpk = u.PK
						AND hpi.duplicate = 1
						AND ho.TypePkString = 8796127723602
						AND ho.p_template IS NULL 
						AND u.P_country=8796100624418) B
		ON A.' + @HYB_key + '=B.' + @RFO_key + ''
				FROM    ( SELECT    * ,
									ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
						  FROM      DataMigration.dbo.map_tab
						  WHERE     flag = 'manual'
									AND Hybris_column <> @HYB_key
									AND id <> 327 --orders related
									AND id<>323--keys for both side
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
				WHERE   [owner] = '853-ReturnPaymentTransaction'
						AND flag = 'manual'
						AND hybris_column NOT IN (
						SELECT DISTINCT
								hybris_column
						FROM    DataMigration..dm_log
						WHERE   test_area = '853-ReturnPaymentTransaction'
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
							  WHERE     [owner] = '853-ReturnPaymentTransaction'
										AND flag IN ( 'c2c', 'manual', 'defaults' )
							  EXCEPT
							  SELECT DISTINCT
										hybris_column
							  FROM      DataMigration..dm_log
							  WHERE     test_area = '853-ReturnPaymentTransaction'
							) a
				) tab1 ,
				( SELECT    COUNT(id) AS [total no of columns]
				  FROM      DataMigration.dbo.map_tab
				  WHERE     [owner] = '853-ReturnPaymentTransaction'
							AND flag IN ( 'c2c', 'manual', 'defaults' )
				) tab2;

		SET STATISTICS TIME OFF;
		GO


