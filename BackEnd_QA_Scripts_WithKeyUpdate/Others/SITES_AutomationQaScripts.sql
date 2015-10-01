


			SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED				
			
            
          BEGIN  

			DROP TABLE #tempact

			DELETE  DataMigration..dm_log
			WHERE   test_area = '817-sites'
					AND test_type = 'c2c'

			SELECT  DISTINCT
					a.AccountID
			INTO    #tempact
			FROM    RFOperations.Hybris.Sites a ,
					RFOperations.RFO_Accounts.AccountRF b ,
					RFOperations.RFO_Accounts.AccountBase c ,
					Hybris..users d
			WHERE   a.AccountID = b.AccountID
					AND a.AccountID = d.p_rfaccountid
					AND a.AccountID = c.AccountID
					AND c.CountryID = 236
			--and (hardterminationdate is null or hardterminationdate >= dateadd(month, -6, getdate()))
					AND ( SoftTerminationDate IS NULL
						  OR SoftTerminationDate >= '2014-05-01'
						)
					AND AccountStatusID <> 3
			GROUP BY a.AccountID


			DECLARE @HYB_key VARCHAR(100) = 'P_rfaccountid'
			DECLARE @RFO_key VARCHAR(100) = 'Accountid'
			DECLARE @sql_gen NVARCHAR(MAX)
			DECLARE @cnt INT
			DECLARE @lt INT
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
				)


			--select count(distinct accountid) from rfoperations.hybris.sites a
			--join hybris.dbo.users b
			--on a.accountid=b.p_rfaccountid
			--JOIN HYBRIS.DBO.COUNTRIES C
			--ON B.P_COUNTRY=C.PK
			--WHERE C.ISOCODE='US'


			SET @cnt = 1
			SELECT  @lt = COUNT(*)
			FROM    DataMigration..map_tab
			WHERE   flag = 'c2c'
					AND rfo_column <> @RFO_key
					AND [owner] = '817-sites' 
			SELECT  COUNT(*)
			FROM    DataMigration..map_tab
			WHERE   flag = 'c2c' --and rfo_column <> @RFO_Key
					AND [owner] = '817-sites'
			WHILE @cnt <= @lt
				BEGIN

					SELECT  @sql_gen = 'SELECT DISTINCT  ''' + [owner]
							+ ''' as test_area ,''' + flag + ''' as test_type, '''
							+ RFO_Column + ''' as rfo_column, ''' + Hybris_Column
							+ ''' as hybris_column, A.' + @HYB_key + ' as hyb_key, A.'
							+ Hybris_Column + ' as hyb_value, B.' + @RFO_key
							+ ' as rfo_key, B.' + RFO_Column + ' as rfo_value
			FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
							+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
							+ @RFO_key + '
			except
			SELECT a.' + @RFO_key + ', a.' + RFO_Column + ' FROM  rfoperations.'
							+ [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
							+ @RFO_key + '=b.' + @RFO_key + ') A  

			LEFT JOIN

			(SELECT a.' + @RFO_key + ', a.' + RFO_Column + ' FROM rfoperations.'
							+ [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
							+ @RFO_key + '=b.' + @RFO_key + '
			except
			SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
							+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
							+ @RFO_key + ') B
			ON A.' + @HYB_key + '=B.' + @RFO_key + '
			UNION
			SELECT DISTINCT ''' + [owner] + ''' as test_area ,''' + flag
							+ ''' as test_type,''' + RFO_Column + ''', ''' + Hybris_Column
							+ ''', A.' + @HYB_key + ', A.' + Hybris_Column + ', B.'
							+ @RFO_key + ',B.' + RFO_Column + '

			FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
							+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
							+ @RFO_key + '
			except
			SELECT a.' + @RFO_key + ', a.' + RFO_Column + ' FROM rfoperations.' + [Schema]
							+ '.' + RFO_Table + ' a, #tempact b where a.' + @RFO_key
							+ '=b.' + @RFO_key + ') A 

			RIGHT JOIN

			(SELECT a.' + @RFO_key + ', a.' + RFO_Column + ' FROM rfoperations.'
							+ [Schema] + '.' + RFO_Table + ' a, #tempact b where a.'
							+ @RFO_key + '=b.' + @RFO_key + '
			except
			SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
							+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
							+ @RFO_key + ') B
			ON A.' + @HYB_key + '=B.' + @RFO_key + ''
					FROM    ( SELECT    * ,
										ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
							  FROM      DataMigration..map_tab
							  WHERE     flag = 'c2c'
										AND rfo_column <> @RFO_key
										AND [owner] = '817-sites'
							) temp
					WHERE   rn = @cnt

					PRINT @sql_gen
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
							EXEC sp_executesql @sql_gen

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
									)

					DELETE  FROM @temp

					SET @cnt = @cnt + 1
				END 
			UPDATE  DataMigration.dbo.map_tab
			SET     [prev_run_err] = 0
			WHERE   [owner] = '817-sites'
					AND flag = 'C2C'
					AND Hybris_column NOT IN ( SELECT DISTINCT
														hybris_column
											   FROM     DataMigration.dbo.dm_log
											   WHERE    test_area = '817-sites'
														AND test_type = 'C2C' )




END




SELECT * FROM DataMigration..map_tab
WHERE [owner]='817-sites'

