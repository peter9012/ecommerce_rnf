
        USE RFOperations; 
		SET STATISTICS TIME ON;
GO

   


        SELECT  CASE WHEN ( SELECT  COUNT(*)
                            FROM    Hybris..autoshiplogs
                            GROUP BY PK
                            HAVING  COUNT(*) > 1
                          ) IS NULL THEN 'No Duplicates PK'
                     ELSE 'Duplicate PK'
                END AS DuplicatePK_CheckInHybris ,
                t1.* ,
                CASE WHEN t1.CancelResult = 'CancelCountMatching'
                          AND t1.DelayResult = 'DelayCountMatching'
                     THEN 'CountValidation Passed'
                     ELSE 'Validation Failled'
                END ValidationResult, GETDATE() AS DupsNCountCompletedTime
        FROM    ( SELECT    HC.Hybris_CancelCnt ,
                            RC.RFO_CancelCnt ,
                            CASE WHEN HC.Hybris_CancelCnt <> RC.RFO_CancelCnt
                                 THEN 'CancelCountNotMatching'
                                 ELSE 'CancelCountMatching'
                            END CancelResult ,
                            HD.Hybris_DelayCnt ,
                            RD.RFO_DelayCnt ,
                            CASE WHEN HD.Hybris_DelayCnt <> RC.RFO_CancelCnt
                                 THEN 'DelayCountNotMatching'
                                 ELSE 'DelayCountMatching'
                            END DelayResult
                  FROM      ( SELECT    COUNT(PK) Hybris_CancelCnt
                              FROM      Hybris.dbo.autoshiplogs
                              WHERE     p_rfaccountid IN (
                                        SELECT  p_rfaccountid
                                        FROM    Hybris.dbo.users
                                        WHERE   p_sourcename = 'Hybris-DM'
                                                AND p_country IN (
                                                SELECT  PK
                                                FROM    Hybris.dbo.countries
                                                WHERE   isocode = 'us' ) )
                                        AND p_status = 'cancel'
                                        AND p_logdate < GETDATE()
                            ) HC ,
                            ( SELECT    COUNT(autoshipDelayCancellationLogId) RFO_CancelCnt
                              FROM      Logging.autoshipDelayCancellationLog adcl
                                        JOIN Hybris..users u ON CAST(adcl.accountId AS NVARCHAR) = u.p_rfaccountid
                                        JOIN Hybris..countries c ON u.p_country = c.PK
                                                              AND c.isocode = 'US'
								  --JOIN Hybris..orders o ON o.pk=adcl.templateId 
                              WHERE     adcl.[status] = 'Cancel'
                            ) RC ,
                            ( SELECT    COUNT(a.PK) Hybris_DelayCnt
                              FROM      Hybris.dbo.autoshiplogs a
                                        JOIN Hybris..users u ON a.p_rfaccountid = u.p_rfaccountid
                                                              AND u.p_sourcename = 'Hybris-DM'
                                        JOIN Hybris..countries c ON c.PK = u.p_country
                                                              AND c.isocode = 'US'
                              WHERE     a.[p_status] = 'Delay'
                                        AND a.p_source = 'Hybris-DM'
                                        AND a.p_logdate < GETDATE()
                            ) HD ,
                            ( SELECT    COUNT(autoshipDelayCancellationLogId) RFO_DelayCnt
                              FROM      Logging.autoshipDelayCancellationLog adcl
                                        JOIN Hybris..users u ON CAST(adcl.accountId AS NVARCHAR) = u.p_rfaccountid
                                        JOIN Hybris..countries c ON u.p_country = c.PK
                                                              AND c.isocode = 'US'
                                        LEFT JOIN Hybris..orders o ON o.PK = adcl.templateId
                              WHERE     adcl.[status] = 'Delay'
                            ) RD
                ) t1;

   
    
---==============================================================================
--			COLUMN2COLUMN VALIDATIONS
--===============================================================================

 SELECT 'Column2ColumnValidation' AS TypeOfTesting,GETDATE() AS Startedtime

		SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
		DECLARE @HYB_key VARCHAR(100) = 'pk'
		DECLARE @RFO_key VARCHAR(100) = 'AutoshipDelayCancellationLogID'
		DECLARE @sql_gen_1 NVARCHAR(MAX)
		DECLARE @cnt INT
		DECLARE @lt_1 INT
		DECLARE @lt_2 INT
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
	
		IF OBJECT_ID('TempDB..#Tempact') IS NOT NULL
			DROP TABLE #Tempact			
	
		DELETE  DataMigration..dm_log
		WHERE   test_area = '985-ASDC'
			
		
	
		SELECT  autoshipDelayCancellationLogId
		INTO    #Tempact
		FROM    Logging.autoshipDelayCancellationLog adcl
				JOIN Hybris..users u ON CAST(adcl.accountId AS NVARCHAR) = u.p_rfaccountid
				JOIN Hybris..countries c ON u.p_country = c.PK
											AND c.isocode = 'US'
				LEFT JOIN Hybris..orders o ON o.PK = adcl.templateId
		WHERE   adcl.[status] IS NOT NULL
                    
	BEGIN


		

		SET @cnt = 1
		SELECT  @lt_1 = COUNT(*)
		FROM    [DataMigration].[dbo].[map_tab]
		WHERE   flag = 'c2c'
				AND rfo_column <> @RFO_key
				AND [owner] = '985-ASDC'

		WHILE @cnt <= @lt_1
			BEGIN

				SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner] + ''', ''' + flag
						+ ''', ''' + RFO_Column + ''' as rfo_column, '''
						+ Hybris_Column + ''' as hybris_column, A.' + @HYB_key
						+ ' as hyb_key, A.' + Hybris_Column + ' as hyb_value, B.'
						+ @RFO_key + ' as rfo_key, B.' + RFO_Column + ' as rfo_value

		FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
						+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
						+ @RFO_key + '
		except
		SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM rfoperations.' + [Schema]
						+ '.' + RFO_Table + ' a, #tempact b where a.' + @RFO_key
						+ '=b.' + @RFO_key + ') A  

		LEFT JOIN

		(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM rfoperations.' + [Schema]
						+ '.' + RFO_Table + ' a, #tempact b where a.' + @RFO_key
						+ '=b.' + @RFO_key + '
		except
		SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
						+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
						+ @RFO_key + ') B
		ON A.' + @HYB_key + '=B.' + @RFO_key + '
		UNION
		SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', ''' + RFO_Column
						+ ''', ''' + Hybris_Column + ''', A.' + @HYB_key + ', A.'
						+ Hybris_Column + ', B.' + @RFO_key + ',B.' + RFO_Column + '

		FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
						+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
						+ @RFO_key + '
		except
		SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM rfoperations.' + [Schema]
						+ '.' + RFO_Table + ' a, #tempact b where a.' + @RFO_key
						+ '=b.' + @RFO_key + ') A  

		RIGHT JOIN

		(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM rfoperations.' + [Schema]
						+ '.' + RFO_Table + ' a, #tempact b where a.' + @RFO_key
						+ '=b.' + @RFO_key + '
		except
		SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
						+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
						+ @RFO_key + ') B
		ON A.' + @HYB_key + '=B.' + @RFO_key + ''
				FROM    ( SELECT    * ,
									ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
						  FROM      [DataMigration].[dbo].[map_tab]
						  WHERE     flag = 'c2c'
									AND rfo_column <> @RFO_key
									AND [owner] = '985-ASDC'
						) temp
				WHERE   rn = @cnt

			
			PRINT @sql_gen_1
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
						EXEC sp_executesql @sql_gen_1


		/* Added this code to get Counting the total of defects.****/

				IF ( SELECT COUNT(*)
					 FROM   @temp
				   ) > 1
					BEGIN
						DECLARE @err_cnt INT
      
						SELECT  @err_cnt = CASE WHEN hyb_cnt = 0 THEN rfo_cnt
												ELSE hyb_cnt
										   END
						FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
											COUNT(DISTINCT rfo_key) rfo_cnt
								  FROM      @temp
								) t1

						UPDATE  a
						SET     [prev_run_err] = @err_cnt
						FROM    DataMigration.dbo.map_tab a ,
								@temp b
						WHERE   a.hybris_column = b.hybris_column
								AND [owner] = '985-ASDC' 
					END   

				INSERT  INTO [DataMigration].[dbo].[dm_log]
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
                
				

			END
		

		UPDATE  DataMigration.dbo.map_tab
				SET     [prev_run_err] = 0
				WHERE   [owner] = '985-ASDC'
						AND flag = 'C2C'
						AND Hybris_column NOT IN ( SELECT DISTINCT
															hybris_column
												   FROM     DataMigration.dbo.dm_log
												   WHERE    test_area = '985-ASDC'
															AND test_type = 'C2C' )


															
															
---==========================================================================================================
----			MANUAL
---==============================================================================================================
			
			
 SELECT 'ManualFlagFieldValidation' AS TypeOfTesting,GETDATE() AS Startedtime
			
			--SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED

			DELETE DataMigration..dm_log
			WHERE test_area='985-ASDC'AND test_type='Manual'

			
				
				   
	BEGIN
    SET @cnt = 1
    SELECT  @lt_1 = COUNT(*)
    FROM    [DataMigration].[dbo].[map_tab]
    WHERE   flag = 'manual'
            AND rfo_column <> @RFO_key
            AND [owner] = '985-ASDC' 

    WHILE @cnt <= @lt_1
        BEGIN




            SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner] + ''', '''
                    + flag + ''', ''' + RFO_Column + ''' as rfo_column, '''
                    + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                    + ' as hyb_key, A.' + Hybris_Column + ' as hyb_value, B.'
                    + @RFO_key + ' as rfo_key, B.' + RFO_Column
                    + ' as rfo_value

	FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                    + Hybris_Table + ' a, tempact b where a.' + @HYB_key
                    + '=b.' + @RFO_key + '
	except
	SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM ' + RFO_Table
                    + ', #tempact b where a.' + @RFO_key + '=b.' + @RFO_key
                    + ') A  

	LEFT JOIN

	(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM ' + RFO_Table
                    + ' , tempact b where a.' + @RFO_key + '=b.' + @RFO_key
                    + '
	except
	SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                    + Hybris_Table + ' a, tempact b where a.' + @HYB_key
                    + '=b.' + @RFO_key + ') B
	ON A.' + @HYB_key + '=B.' + @RFO_key + '
	UNION
	SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', ''' + RFO_Column
                    + ''', ''' + Hybris_Column + ''', A.' + @HYB_key + ', A.'
                    + Hybris_Column + ', B.' + @RFO_key + ',B.' + RFO_Column
                    + '

	FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                    + Hybris_Table + ' a, tempact b where a.' + @HYB_key
                    + '=b.' + @RFO_key + '
	except
	SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM ' + RFO_Table
                    + ' , tempact b where a.' + @RFO_key + '=b.' + @RFO_key
                    + ') A  

	RIGHT JOIN

	(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM ' + RFO_Table
                    + ' , tempact b where a.' + @RFO_key + '=b.' + @RFO_key
                    + '
	except
	SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                    + Hybris_Table + ' a, tempact b where a.' + @HYB_key
                    + '=b.' + @RFO_key + ') B
	ON A.' + @HYB_key + '=B.' + @RFO_key + ''
            FROM    ( SELECT    * ,
                                ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                      FROM      [DataMigration].[dbo].[map_tab]
                      WHERE     flag = 'manual'
                                AND rfo_column <> @RFO_key
                                AND [owner] = '985-ASDC'
                    ) temp
            WHERE   rn = @cnt

		PRINT @sql_gen_1


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
                    EXEC sp_executesql @sql_gen_1



	/* Added this code to get Counting the total of defects.****/

            IF ( SELECT COUNT(*)
                 FROM   @temp
               ) > 1
                BEGIN
                   
                    SELECT  @err_cnt = COUNT(DISTINCT hyb_key)
                    FROM    @temp

                    UPDATE  a
                    SET     [prev_run_err] = @err_cnt
                    FROM    DataMigration.dbo.map_tab a ,
                            @temp b
                    WHERE   a.hybris_column = b.hybris_column
                            AND [owner] = '985-ASDC' 
                END   

            INSERT  INTO [DataMigration].[dbo].[dm_log]
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
                    WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key,
                                                              '~') )
                              OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                            )

            DELETE  FROM @temp

            SET @cnt = @cnt + 1

        END
		END

UPDATE  DataMigration.dbo.map_tab
SET     [prev_run_err] = 0
WHERE   [owner] = '985-ASDC'
        AND flag = 'Manual'
        AND Hybris_column NOT IN ( SELECT DISTINCT
                                            hybris_column
                                   FROM     DataMigration.dbo.dm_log
                                   WHERE    test_area = '985-ASDC'
                                            AND test_type = 'Manual' )

						

--				============================================
--				VALIDATING P_rfmodifiedTime
--				============================================
            --SELECT  A.PK ,
            --        A.p_rfmodifiedtime ,
            --        d.ServerModifiedDate
            --FROM    Hybris.dbo.orders A
            --        JOIN Hybris.dbo.users b ON A.userpk = b.PK
            --        JOIN Hybris.dbo.autoshiplogs c ON b.p_rfaccountid = c.p_rfaccountid
            --        JOIN RFOperations.Logging.autoshipDelayCancellationLog d ON c.PK = d.autoshipDelayCancellationLogId
            --        JOIN Hybris.dbo.countries e ON b.p_country = e.PK
            --WHERE   e.isocode = 'US'
            --        AND CAST(A.p_rfmodifiedtime AS DATE) <> CAST(d.ServerModifiedDate AS DATE)



----========Ajay's Notes: for Excluded Accounts;

/*****
		--21 records excluded due to duplicates in RFL. Cleanup is necessary in RFO(LogDate)

SELECT  a.autoshipDelayCancellationLogId ,
        a.accountId ,
        a.*
FROM    RFOperations.Logging.autoshipDelayCancellationLog a
        LEFT JOIN Hybris.dbo.autoshiplogs b ON a.autoshipDelayCancellationLogId = b.PK
        JOIN Hybris.dbo.users u ON p_country = '8796100624418'
                                   AND u.p_rfaccountid = a.accountId
WHERE   b.PK IS NULL
        AND a.templateId IN ( 1415629, 6388236, 8138749, 2342918, 6942574,
                              5692622, 8731529, 6942575, 5692623, 8138748 ) 

– --20 records not moved as the account doesnt contain autoshipid in RFO

SELECT  a.autoshipDelayCancellationLogId ,
        a.accountId ,
        aus.AutoshipID
FROM    RFOperations.Logging.autoshipDelayCancellationLog a
        LEFT JOIN Hybris.dbo.autoshiplogs b ON a.autoshipDelayCancellationLogId = b.PK
        JOIN Hybris.dbo.users u ON p_country = '8796100624418'
                                   AND u.p_rfaccountid = a.accountId
        LEFT JOIN RFOperations.Hybris.Autoship aus ON aus.AccountID = u.p_rfaccountid
WHERE   b.PK IS NULL
        AND u.p_rfaccountid = '701367' 
	
	
	
			***************************************/

			
 SELECT 'ManualRefFlagFieldValidation' AS TypeOfTesting,GETDATE() AS Startedtime

			
			
			
		
			
		DELETE datamigration..dm_log WHERE test_area='985-ASDC'
							AND test_type='ref_manual'
							
          
		
		IF OBJECT_ID('Tempdb..#Accounts') IS NOT NULL
		DROP TABLE #Accounts
				
						
		
            SELECT DISTINCT
                    accountId
            INTO    #Accounts
            FROM    RFOperations.Logging.autoshipDelayCancellationLog
            WHERE   accountId IN (
                    SELECT  p_rfaccountid
                    FROM    Hybris.dbo.users
                    WHERE   p_country IN ( SELECT   PK
                                           FROM     Hybris.dbo.countries
                                           WHERE    isocode = 'US' 
                            AND p_sourcename = 'Hybris-DM' ))
                    AND [status] IS NOT NULL;
                  
  BEGIN	

    SET @cnt = 1
    SELECT  @lt_1 = COUNT(*)
    FROM    [DataMigration].[dbo].[map_tab]
    WHERE   flag = 'ref_manual'
            AND rfo_column <> @RFO_key
            AND [owner] = '985-ASDC'
            AND Hybris_Table = 'users'

    WHILE @cnt <= @lt_1
        BEGIN

            SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner] + ''', '''
                    + flag + ''', ''' + RFO_Column + ''' as rfo_column, '''
                    + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                    + ' as hyb_key, A.' + Hybris_Column + ' as hyb_value, B.'
                    + @RFO_key + ' as rfo_key, B.' + RFO_Column
                    + ' as rfo_value

	FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                    + Hybris_Table + ' a, #Accounts b where a.' + @HYB_key
                    + '=b.' + @RFO_key + '
	except
	SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM rfoperations.'
                    + [Schema] + '.' + RFO_Table + ' a, #Accounts b where a.'
                    + @RFO_key + '=b.' + @RFO_key + ') A  

	LEFT JOIN

	(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM rfoperations.'
                    + [Schema] + '.' + RFO_Table + ' a, #Accounts b where a.'
                    + @RFO_key + '=b.' + @RFO_key + '
	except
	SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                    + Hybris_Table + ' a, #Accounts b where a.' + @HYB_key
                    + '=b.' + @RFO_key + ') B
	ON A.' + @HYB_key + '=B.' + @RFO_key + '
	UNION
	SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', ''' + RFO_Column
                    + ''', ''' + Hybris_Column + ''', A.' + @HYB_key + ', A.'
                    + Hybris_Column + ', B.' + @RFO_key + ',B.' + RFO_Column
                    + '

	FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                    + Hybris_Table + ' a, #Accounts b where a.' + @HYB_key
                    + '=b.' + @RFO_key + '
	except
	SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM rfoperations.'
                    + [Schema] + '.' + RFO_Table + ' a, #Accounts b where a.'
                    + @RFO_key + '=b.' + @RFO_key + ') A  

	RIGHT JOIN

	(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' FROM rfoperations.'
                    + [Schema] + '.' + RFO_Table + ' a, #Accounts b where a.'
                    + @RFO_key + '=b.' + @RFO_key + '
	except
	SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
                    + Hybris_Table + ' a, #Accounts b where a.' + @HYB_key
                    + '=b.' + @RFO_key + ') B
	ON A.' + @HYB_key + '=B.' + @RFO_key + ''
            FROM    ( SELECT    * ,
                                ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                      FROM      [DataMigration].[dbo].[map_tab]
                      WHERE     flag = 'ref_manual'
                                AND rfo_column <> @RFO_key
                                AND [owner] = '985-ASDC'
                                AND Hybris_Table = 'users'
                    ) temp
            WHERE   rn = @cnt

	print @sql_gen_1
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
                    EXEC sp_executesql @sql_gen_1


	/* Added this code to get Counting the total of defects.****/

            IF ( SELECT COUNT(*)
                 FROM   @temp
               ) > 1
                BEGIN
                    --DECLARE @err_cnt INT
      
                    SELECT  @err_cnt = CASE WHEN hyb_cnt = 0 THEN rfo_cnt
                                            ELSE hyb_cnt
                                       END
                    FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
                                        COUNT(DISTINCT rfo_key) rfo_cnt
                              FROM      @temp
                            ) t1

                    UPDATE  a
                    SET     [prev_run_err] = @err_cnt
                    FROM    DataMigration.dbo.map_tab a ,
                            @temp b
                    WHERE   a.hybris_column = b.hybris_column
                            AND [owner] = '985-ASDC'
                            AND Hybris_Table = 'users'
                END   

            INSERT  INTO [DataMigration].[dbo].[dm_log]
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
                    WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key,
                                                              '~') )
                              OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                            )

            DELETE  FROM @temp

            SET @cnt = @cnt + 1



           

        END
	END

	 UPDATE  DataMigration.dbo.map_tab
            SET     [prev_run_err] = 0
            WHERE   [owner] = '985-ASDC'
                    AND flag = 'ref_manual'
                    AND Hybris_column NOT IN (
                    SELECT DISTINCT
                            hybris_column
                    FROM    DataMigration.dbo.dm_log
                    WHERE   test_area = '985-ASDC'
                            AND test_type = 'ref_manual' )


												


--				============================================
--				VALIDATING DEFAULT SOURCENAME AS HYBRIS-DM
--				============================================


 SELECT 'ManuallySourceNameValidation' AS TypeOfTesting,GETDATE() AS Startedtime
BEGIN

DECLARE @ERRCOUNT INT
SELECT  @ERRCOUNT = COUNT(PK)
FROM    Hybris.dbo.autoshiplogs
WHERE   p_rfaccountid IN (
        SELECT  p_rfaccountid
        FROM    Hybris.dbo.users
        WHERE   p_country IN ( SELECT   PK
                               FROM     Hybris.dbo.countries
                               WHERE    isocode = 'US' ) )
        AND PK IN ( SELECT  autoshipDelayCancellationLogId
                    FROM    RFOperations.Logging.autoshipDelayCancellationLog )
        AND p_source <> 'Hybris-DM'
		
IF @ERRCOUNT = 0
    UPDATE  DataMigration.dbo.map_tab
    SET     [prev_run_err] = 0
    WHERE   [owner] = '985-ASDC'
            AND [Hybris_Column ] = 'P_source'
		
		END
		SELECT '985-AutoShipDelayCancel' AS AreaToValidate, (SELECT COUNT(*) FROM datamigration..map_tab
		WHERE [owner]='985-ASDC') AS TotalFields,(SELECT COUNT(*) FROM datamigration..map_tab
		WHERE [owner]='985-ASDC' AND prev_run_err<>0 AND prev_run_err IS NOT NULL) AS TotalfieldsFailled,
		(SELECT COUNT(*) FROM datamigration..map_tab
		WHERE [owner]='985-ASDC' AND (prev_run_err=0 OR  prev_run_err IS  NULL)) AS TotalFieldPassed

		

							SELECT RFO_Table,[RFO_Column ],Hybris_Table,[Hybris_Column ],prev_run_err,'Required Analysis' AS Results FROM datamigration..map_tab WHERE [owner]='985-ASDC'
							AND prev_run_err<>0 AND  prev_run_err IS NOT NULL

							SELECT * FROM datamigration..dm_log WHERE test_area='985-ASDC'
							




---------------------------------------------------------------------------------------------
				
--				--For Hybris_US

--                SELECT  COUNT(*) ,
--                        p_updatedbyapplication ,
--                        p_updatedbyuser
--                FROM    Hybris.dbo.users
--                WHERE   p_country IN ( SELECT   PK
--                                       FROM     Hybris.dbo.countries
--                                       WHERE    isocode = 'US' )
--                        AND p_sourcename = 'HYbris-dm'
--                GROUP BY p_updatedbyapplication ,
--                        p_updatedbyuser

--						----For Hybirs Canada
--                SELECT  COUNT(*) ,
--                        p_updatedbyapplication ,
--                        p_updatedbyuser
--                FROM    Hybris.dbo.users
--                WHERE   p_country IN ( SELECT   PK
--                                       FROM     Hybris.dbo.countries
--                                       WHERE    isocode = 'CA' )
--                GROUP BY p_updatedbyapplication ,
--                        p_updatedbyuser


--            SELECT  COUNT(*) ,
--                    ChangedByApplication ,
--                    ChangedByUser
--            FROM    RFOperations.Logging.autoshipDelayCancellationLog
--            WHERE   CAST(accountId AS NVARCHAR) IN (
--                    SELECT  p_rfaccountid
--                    FROM    Hybris.dbo.users
--                    WHERE   p_country IN ( SELECT   PK
--                                           FROM     Hybris.dbo.countries
--                                           WHERE    isocode = 'US' ) )
--            GROUP BY ChangedByApplication ,
--                    ChangedByUser 

------------------------------------------------------------------------------------------			
		
       
			
			
--			--DELAYCOUNT<>P_TOTALDELAYCOUNT-- 

--            SELECT  *
--            FROM    DataMigration..map_tab
--            WHERE   [owner] = '985-ASDC'
--                    AND HYBRIS_COLUMN = 'P_TOTALDELAYCOUNT'

--            SELECT  COUNT(*)
--            FROM    RFOperations.Logging.autoshipDelayCancellationLog
--            WHERE   accountId IN (
--                    SELECT  p_rfaccountid
--                    FROM    Hybris.dbo.users
--                    WHERE   p_country IN ( SELECT   PK
--                                           FROM     Hybris.dbo.countries
--                                           WHERE    isocode = 'US' ) )
--                    AND delayCount IS NULL
--                    AND accountId NOT IN ( 681014, 701367, 1143902, 571034,
--                                           1298292, 1457914 )
			
--            SELECT  COUNT(p_totaldelaycount)
--            FROM    Hybris..autoshiplogs
--            WHERE   p_rfaccountid IN (
--                    SELECT  p_rfaccountid
--                    FROM    Hybris.dbo.users
--                    WHERE   p_country IN ( SELECT   PK
--                                           FROM     Hybris.dbo.countries
--                                           WHERE    isocode = 'US' ) )
--                    AND p_logdate < '2015-07-14'
			

--            SELECT  COUNT(*) ,
--                    delayCount
--            FROM    RFOperations.Logging.autoshipDelayCancellationLog
--            WHERE   accountId IN (
--                    SELECT  p_rfaccountid
--                    FROM    Hybris.dbo.users
--                    WHERE   p_country IN ( SELECT   PK
--                                           FROM     Hybris.dbo.countries
--                                           WHERE    isocode = 'US' ) )
--                    AND accountId NOT IN ( 681014, 701367, 1143902, 571034,
--                                           1298292, 1457914 )
--            GROUP BY delayCount

--            SELECT  COUNT(*) ,
--                    p_totaldelaycount
--            FROM    Hybris..autoshiplogs
--            WHERE   p_rfaccountid IN (
--                    SELECT  p_rfaccountid
--                    FROM    Hybris.dbo.users
--                    WHERE   p_country IN ( SELECT   PK
--                                           FROM     Hybris.dbo.countries
--                                           WHERE    isocode = 'US' ) )
--                    AND p_logdate < '2015-07-14'
--            GROUP BY p_totaldelaycount

--			--DELAYTYPE<>P_DELAYTYPE-
--            SELECT  a.* ,
--                    b.*
--            FROM    ( SELECT    'RFO_US' AS DataFrom ,
--                                COUNT(*) AS TotalCount ,
--                                status ,
--                                tookOrderAfterDelay ,
--                                orderTotalCount ,
--                                daysSinceLastAutoship ,
--                                delayType
--                      FROM      RFOperations.Logging.autoshipDelayCancellationLog
--                      WHERE     accountId IN (
--                                SELECT  p_rfaccountid
--                                FROM    Hybris.dbo.users
--                                WHERE   p_country IN (
--                                        SELECT  PK
--                                        FROM    Hybris.dbo.countries
--                                        WHERE   isocode = 'US' ) )
--                                AND status = 'CANCEL'
--                      GROUP BY  tookOrderAfterDelay ,
--                                orderTotalCount ,
--                                daysSinceLastAutoship ,
--                                delayType ,
--                                status
--                    ) a
--                    JOIN ( SELECT   'Hybris_US' AS DataFrom ,
--                                    COUNT(*) AS TotalCount ,
--                                    p_status ,
--                                    p_tookorderafterdelay ,
--                                    p_ordertotalcount ,
--                                    p_dayssincelastautoship ,
--                                    p_delaytype
--                           FROM     Hybris.dbo.autoshiplogs
--                           WHERE    p_rfaccountid IN (
--                                    SELECT  p_rfaccountid
--                                    FROM    Hybris.dbo.users
--                                    WHERE   p_country IN (
--                                            SELECT  PK
--                                            FROM    Hybris.dbo.countries
--                                            WHERE   isocode = 'US' ) )
--                                    AND p_status = 'CANCEL'
--                                    AND p_logdate < '2015-07-14'
--                           GROUP BY p_tookorderafterdelay ,
--                                    p_ordertotalcount ,
--                                    p_dayssincelastautoship ,
--                                    p_delaytype ,
--                                    p_status
--                         ) b ON a.status = b.p_status


--			--- REASON<>P_REASON--
			
--            SELECT  *
--            FROM    DataMigration..map_tab
--            WHERE   [owner] = '985-ASDC'
--                    AND HYBRIS_COLUMN = 'P_REASON'
						
--            SELECT  A.reason ,
--                    LEN(LTRIM(RTRIM(A.reason))) ,
--                    B.p_reason ,
--                    LEN(LTRIM(RTRIM(B.p_reason)))
--            FROM    RFOperations.Logging.autoshipDelayCancellationLog A
--                    INNER JOIN Hybris.dbo.autoshiplogs B ON A.autoshipDelayCancellationLogId = B.PK
--            WHERE   ( A.reason <> B.p_reason
--                      OR A.reason = ''
--                    )


--            SELECT  COUNT(*)
--            FROM    RFOperations.Logging.autoshipDelayCancellationLog A
--                    INNER JOIN Hybris.dbo.autoshiplogs B ON A.autoshipDelayCancellationLogId = B.PK
--            WHERE   ( A.reason <> B.p_reason
--                      OR A.reason = ''
--                    )
			
			
			
			


			
			

--			--Defects Questions


--			/**  UserType in Hybris_Ca is name not type, we are migrating type code  ***/
		

		
--            SELECT  COUNT(*) ,
--                    p_usertype
--            FROM    Hybris.dbo.autoshiplogs
--            WHERE   p_rfaccountid IN (
--                    SELECT  p_rfaccountid
--                    FROM    Hybris.dbo.users
--                    WHERE   p_country IN ( SELECT   PK
--                                           FROM     Hybris.dbo.countries
--                                           WHERE    isocode = 'US' ) )
--                    AND p_logdate < '2015-07-14'
--            GROUP BY p_usertype

			
--            SELECT  COUNT(*) ,
--                    p_usertype
--            FROM    Hybris.dbo.autoshiplogs
--            WHERE   p_rfaccountid IN (
--                    SELECT  p_rfaccountid
--                    FROM    Hybris.dbo.users
--                    WHERE   p_country IN ( SELECT   PK
--                                           FROM     Hybris.dbo.countries
--                                           WHERE    isocode = 'CA' ) )
--            GROUP BY p_usertype
--			--============================================

			
--            SELECT  COUNT(*) ,
--                    p_source
--            FROM    Hybris.dbo.autoshiplogs
--            WHERE   p_rfaccountid IN (
--                    SELECT  p_rfaccountid
--                    FROM    Hybris.dbo.users
--                    WHERE   p_country IN ( SELECT   PK
--                                           FROM     Hybris.dbo.countries
--                                           WHERE    isocode = 'US' ) )
--                    AND p_logdate < '2015-07-14'
--            GROUP BY p_source

			
--            SELECT  COUNT(*) ,
--                    p_source
--            FROM    Hybris.dbo.autoshiplogs
--            WHERE   p_rfaccountid IN (
--                    SELECT  p_rfaccountid
--                    FROM    Hybris.dbo.users
--                    WHERE   p_country IN ( SELECT   PK
--                                           FROM     Hybris.dbo.countries
--                                           WHERE    isocode = 'CA' ) )
--                    AND p_logdate < '2015-07-14'
--            GROUP BY p_source
			
			
--            SELECT  COUNT(*) ,
--                    a.[Description]
--            FROM    ( SELECT DISTINCT
--                                a.[autoshipDelayCancellationLogId] ,
--                                b.*
--                      FROM      RFOperations.Logging.autoshipDelayCancellationLog a
--                                JOIN RFOperations.[RFO_Reference].[CancelingRequestSource] b ON a.CancellationSourceId = b.CancelingRequestSourceId
--                    ) a
--            GROUP BY a.[Description]


		

--		--========================================================================
--		-----==== RFO_servermodifiedDate and LastProcessingdate to Hybris_orders.

--WITH    Hybris_cte
--          AS ( SELECT DISTINCT
--                        MAX(ISNULL(c.p_rfmodifiedtime, '')) AS modified ,
--                        MAX(ISNULL(c.p_lastprocessingdate, '')) AS serverdate ,
--                        a.PK AS pk ,
--                        DENSE_RANK() OVER ( PARTITION BY a.PK ORDER BY MAX(ISNULL(c.p_rfmodifiedtime,
--                                                              '')) DESC ) AS rn
--               FROM     Hybris.dbo.autoshiplogs a ,
--                        Hybris.dbo.users b ,
--                        Hybris.dbo.orders c ,
--                        Hybris.dbo.countries d
--               WHERE    a.p_rfaccountid = b.p_rfaccountid
--                        AND b.PK = c.userpk
--                        AND b.p_country = d.PK
--                        AND d.isocode = 'us'
--               GROUP BY c.p_rfmodifiedtime ,
--                        c.p_lastprocessingdate ,
--                        a.PK
--             )
--    SELECT  a.pk ,
--            b.ServerModifiedDate AS RFO_ASDC_ServerModifiedDate ,
--            a.modified AS Hybris_Order_ServerModifiedDate ,
--            b.LastProcessingDate AS RFO_ASDC_LastProcessingDate ,
--            a.serverdate AS Hybris_Orders_lastprocessingDate ,
--            b.status
--    FROM    Hybris_cte a
--            JOIN RFOperations.Logging.autoshipDelayCancellationLog b ON a.pk = b.autoshipDelayCancellationLogId
--    WHERE   a.rn = 1
--    ORDER BY a.pk