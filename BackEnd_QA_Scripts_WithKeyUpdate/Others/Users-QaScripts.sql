


			CREATE PROCEDURE [Migration].[Migration_VerifyUserMigration]
			AS
				SET NOCOUNT ON; 

				BEGIN

					IF OBJECT_ID('dbo.#UserQALOG', 'U') IS NOT NULL
						DROP TABLE dbo.UserQALOG;


					CREATE TABLE #USERQALOG
						(
						  SRC_ID NVARCHAR(255) ,
						  SRC_EMAILID NVARCHAR(255) ,
						  DEST_ID BIGINT ,
						  DEST_EMAILID NVARCHAR(255) ,
						  ErrorReason NVARCHAR(255)
						);

			--Find Missing users in Hybris Table.


					INSERT  INTO #USERQALOG
							( SRC_ID ,
							  SRC_EMAILID ,
							  DEST_ID ,
							  DEST_EMAILID ,
							  ErrorReason
							)
							SELECT  CONVERT(NVARCHAR(255), U.UserID) ,
									U.EmailAddress ,
									NULL ,
									NULL ,
									'Missing in Hybris'
							FROM    RFOperations.Hybris.Users U
							EXCEPT
							SELECT  p_rfaccountid ,
									p_customeremail ,
									NULL ,
									NULL ,
									'Missing in Hybris'
							FROM    Hybris.dbo.users;

                                                                                                      
					SELECT  UserID ,
							EmailAddress ,
							COUNT(*)
					FROM    RFOperations.Hybris.Users
					WHERE   UserID IN ( SELECT  SRC_ID
										FROM    #USERQALOG )
							AND ( EmailAddress IS NOT NULL
								  AND EmailAddress <> ''
								  AND ( EmailAddress NOT LIKE 'TEST%'
										OR EmailAddress NOT LIKE 'TEMP%'
									  )
								)
							AND EmailAddress NOT IN ( SELECT    p_customeremail
													  FROM      Hybris.dbo.users )
					GROUP BY EmailAddress ,
							UserID;

                                                                                          

			-- Find records with Incorrect column
					INSERT  INTO #USERQALOG
							( SRC_ID ,
							  SRC_EMAILID ,
							  DEST_ID ,
							  DEST_EMAILID ,
							  ErrorReason
							)
							SELECT  CONVERT(NVARCHAR(255), U.UserID) ,
									U.EmailAddress ,
									HU.PK ,
									HU.p_customeremail ,
									' Value of Name in RFO and Hybris does not match. It should be FirstName LastName separated by space' AS Error
							FROM    RFOperations.Hybris.Users U ,
									Hybris.dbo.users HU
							WHERE   CONVERT(NVARCHAR(255), U.UserID) = HU.p_rfaccountid
									AND U.EmailAddress = HU.p_customeremail
									AND U.FirstName + ' ' + U.LastName <> HU.name;

				END;
				GO



	                                                                                                                                                                          



			DECLARE @p_TestResult VARCHAR(8000);
			DECLARE @p_Distinct_values BIGINT;
			DECLARE @p_NULL_DUP_keys BIGINT;
			SET NOCOUNT ON; 

			BEGIN

			--Source Table DataMigration.Hybris_Users

			-- Verify if UserIds are unique.
			-- Verify if some columns have some SAME/NULL values.

				IF ( SELECT COUNT(*)
					 FROM   RFOperations.Hybris.Users
				   ) = 0
					SELECT  @p_TestResult = 'Table is Empty , Exiting Verification process';
				ELSE
					BEGIN

						SELECT  @p_NULL_DUP_keys = ( SELECT COUNT(*)
													 FROM   RFOperations.Hybris.Users
													 WHERE  UserID IS NULL
												   );
						IF @p_NULL_DUP_keys > 0
							SELECT  @p_TestResult = 'Some Unique Key has NUll Values. These would fail the Hybris Upload process.';
	                                      
						SELECT  @p_NULL_DUP_keys = ( SELECT COUNT(*)
													 FROM   RFOperations.Hybris.Users
													 WHERE  EmailAddress IS NULL
												   );
						IF @p_NULL_DUP_keys > 0
							SELECT  @p_TestResult = 'Some EmailAddress Addresses are NUll. These might fail the Hybris Upload process.';
	
						SELECT  @p_NULL_DUP_keys = ( SELECT COUNT(*)
													 FROM   ( SELECT    COUNT(*) AS total
															  FROM      RFOperations.Hybris.Users
															  GROUP BY  EmailAddress
															  HAVING    COUNT(*) > 1
															) a
												   );
						IF @p_NULL_DUP_keys > 0
							SELECT  @p_TestResult = 'Some EmailAddress Addresses are Duplicated. These might fail the Hybris Upload process.';
	
						SELECT  @p_NULL_DUP_keys = ( SELECT COUNT(*)
													 FROM   RFOperations.Hybris.Users
													 GROUP BY UserID
													 HAVING COUNT(*) > 1
												   );
						IF @p_NULL_DUP_keys > 0
							SELECT  @p_TestResult = CONCAT(@p_TestResult,
														   'Some Key Values are Duplicated. These would fail the Hybris Upload process.');
	
						SELECT  @p_Distinct_values = ( SELECT   COUNT(DISTINCT EmailAddress)
													   FROM     RFOperations.Hybris.Users
													 );
						IF @p_Distinct_values = 0
							SELECT  @p_TestResult = CONCAT(@p_TestResult,
														   'All EmailAddress Address Values are NULL , Check the mapping');
	
						SELECT  @p_Distinct_values = ( SELECT   COUNT(DISTINCT Password)
													   FROM     RFOperations.Hybris.Users
													 );
						IF @p_Distinct_values = 0
							SELECT  @p_TestResult = CONCAT(@p_TestResult,
														   ', All Password Values are NULL , Check the mapping');

						SELECT  @p_Distinct_values = ( SELECT   COUNT(DISTINCT Username)
													   FROM     RFOperations.Hybris.Users
													 );
						IF @p_Distinct_values = 0
							SELECT  @p_TestResult = CONCAT(@p_TestResult,
														   ', All Username Values are NULL , Check the mapping');
	
					END;
	
				SELECT  @p_TestResult = ISNULL(@p_TestResult,
											   'No Data Inconsistencies observed');
	  
				PRINT @p_TestResult;
	   
			END;

			GO

			SELECT  *
			FROM    RFOperations.Hybris.Users
			WHERE   CONVERT(NVARCHAR(255), UserID) IN ( SELECT  p_rfaccountid
														FROM    Hybris.dbo.users )
					AND Username IN ( SELECT    Username
									  FROM      RFOperations.Hybris.Users
									  GROUP BY  Username ,
												EmailAddress
									  HAVING    COUNT(*) > 1 );


			SELECT  *
			FROM    RFOperations.Hybris.Users
			WHERE   CONVERT(NVARCHAR(255), UserID) IN ( SELECT  p_rfaccountid
														FROM    Hybris.dbo.users )
					AND EmailAddress IN ( SELECT    EmailAddress
										  FROM      RFOperations.Hybris.Users
										  GROUP BY  Username ,
													EmailAddress
										  HAVING    COUNT(*) > 1 );

			SELECT  *
			FROM    RFOperations.Hybris.Users A ,
					RFOperations.Security.AccountSecurity B
			WHERE   A.UserID = B.AccountID
					AND A.Username = B.Username;


			SELECT  *
			FROM    RFOperations.Security.AccountSecurity
			WHERE   AccountID IN ( SELECT   p_rfaccountid
								   FROM     Hybris.dbo.users
								   WHERE    p_country IN ( SELECT   PK
														   FROM     Hybris.dbo.countries
														   WHERE    isocode = 'US' ) )
					AND Username IN ( SELECT    Username
									  FROM      RFOperations.Security.AccountSecurity
									  GROUP BY  Username
									  HAVING    COUNT(*) > 1 )
			ORDER BY Username;



			SELECT  uniqueid ,
					passwd ,
					name ,
					[description]
			FROM    Hybris.dbo.users
			WHERE   p_country IN ( SELECT   PK
								   FROM     Hybris.dbo.countries
								   WHERE    isocode = 'US' );

			--=====================================================
			--			COUNTS OF USERS MIGRATED
			--=====================================================
			
			
						SELECT  COUNT(uniqueid)
						FROM    Hybris..users
						WHERE   TypePkString = 8796094005330
								AND createdTS >= GETDATE() - 6;
						---504 total got migrated.

						SELECT  COUNT(DISTINCT EmailAddress)
						FROM    RFOperations.Hybris.Users
						WHERE   EmailAddress IS NOT NULL
								AND EmailAddress NOT LIKE ''
								AND EmailAddress NOT LIKE '%temp%'
								AND FirstName + '' + LastName NOT LIKE '%Training%'
								AND Username NOT LIKE '%admin'
								AND EmailAddress NOT IN (
								SELECT  uniqueid
								FROM    Hybris..Users
								WHERE   --TypePkString=8796094005330 AND 
										createdTS <= GETDATE() - 6 );
						--504
			

						SELECT  COUNT(EmailAddress)
						FROM    RFOperations.Hybris.Users
						WHERE   EmailAddress IN ( SELECT    uniqueid
												  FROM      Hybris..Users
												  WHERE     TypePkString = 8796094005330
															AND createdTS <= GETDATE() - 6 );

						--35  been already in Hybris.

			
			
						WITH    cte
								  AS ( SELECT   FirstName + '' + LastName AS name ,
												[Password] ,
												EmailAddress ,
												ROW_NUMBER() OVER ( PARTITION BY EmailAddress ORDER BY UserID ASC ) AS rn
						---INTO #tempact
									   FROM     RFOperations.Hybris.Users
									   WHERE    EmailAddress IS NOT NULL
												AND EmailAddress NOT LIKE ''
												AND EmailAddress NOT LIKE '%temp%'
												AND FirstName + '' + LastName NOT LIKE '%Training%'
												AND Username NOT LIKE '%admin'
									 )
							--504
			
						SELECT  *
						INTO    #RFOusers
						FROM    cte
						WHERE   rn = 1
								AND EmailAddress NOT IN (
								SELECT  uniqueid
								FROM    Hybris..users
								WHERE   --TypePkString=8796094005330 AND 
										createdTS <= GETDATE() - 6 )
						ORDER BY cte.EmailAddress;

		
		
						DROP TABLE #tempact;

			
						SELECT  EmailAddress
						INTO    #tempact
						FROM    #RFOusers;
			
			
			
	
			
			DECLARE @HYB_key VARCHAR(100) = 'UniqueId';
			DECLARE @RFO_key VARCHAR(100) = 'EmailAddress';
			DECLARE @sql_gen_1 NVARCHAR(MAX);
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

			SET @cnt = 1;
			SELECT  @lt_1 = COUNT(*)
			FROM    [DataMigration].[dbo].[map_tab]
			WHERE   flag = 'C2C'
					AND rfo_column <> @RFO_key
					AND [owner] = '871-USERS';

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
			SELECT a.' + @RFO_key + ', ' + RFO_Column
							+ ' FROM #RFOusers a, #tempact b where a.' + @RFO_key + '=b.'
							+ @RFO_key + ') A  

			LEFT JOIN

			(SELECT a.' + @RFO_key + ', ' + RFO_Column
							+ ' FROM #RFOusers a, #tempact b where a.' + @RFO_key + '=b.'
							+ @RFO_key + '
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
			SELECT a.' + @RFO_key + ', ' + RFO_Column
							+ ' FROM #RFOusers a, #tempact b where a.' + @RFO_key + '=b.'
							+ @RFO_key + ') A  

			RIGHT JOIN

			(SELECT a.' + @RFO_key + ', ' + RFO_Column
							+ ' FROM  #RFOusers a, #tempact b where a.' + @RFO_key + '=b.'
							+ @RFO_key + '
			except
			SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM hybris.dbo.'
							+ Hybris_Table + ' a, #tempact b where a.' + @HYB_key + '=b.'
							+ @RFO_key + ') B
			ON A.' + @HYB_key + '=B.' + @RFO_key + ''
					FROM    ( SELECT    * ,
										ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
							  FROM      [DataMigration].[dbo].[map_tab]
							  WHERE     flag = 'C2C'
										AND rfo_column <> @RFO_key
										AND [owner] = '871-USERS'
							) temp
					WHERE   rn = @cnt;

			---print @sql_gen_1
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


			/* Added this code to get Counting the total of defects.****/

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
									AND [owner] = '871-USERS'; 
						END;   

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
									);

					DELETE  FROM @temp;

					SET @cnt = @cnt + 1;

		
				END;
			--END


			UPDATE  DataMigration.dbo.map_tab
			SET     [prev_run_err] = 0
			WHERE   [owner] = '871-USERS' --and id=407
					AND Hybris_column NOT IN ( SELECT DISTINCT
														hybris_column
											   FROM     DataMigration.dbo.dm_log
											   WHERE    test_area = '871-USERS'
														AND test_type = 'C2C' );




			SELECT  *
			FROM    DataMigration..map_tab
			WHERE   [owner] LIKE '%users';

			SELECT  *
			FROM    DataMigration..dm_log
			WHERE   test_area = '871-Users';

		