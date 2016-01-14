    USE RFOperations;
    SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

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

	--Duplicate check on Hybris side for US
    SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
                 ELSE 'No duplicates - Validation Passed'
            END AS [Step-1 Validation]
    FROM    ( SELECT    OwnerPkString
              FROM      Hybris.dbo.addresses
              WHERE     duplicate = 1
                        AND OwnerPkString IN (
                        SELECT DISTINCT
                                ReturnPaymentId
                        FROM    Hybris.ReturnOrder a
                                JOIN Hybris.ReturnPayment b ON a.ReturnOrderID = b.ReturnOrderID
                        WHERE   CountryID = 236 )
                        AND p_billingaddress = 1
              GROUP BY  OwnerPkString
              HAVING    COUNT(*) > 1
            ) t1;


	--Counts check on Hybris side for US
    SELECT  hybris_cnt ,
            rfo_cnt ,
            CASE WHEN hybris_cnt > rfo_cnt
                 THEN 'Hybris count more than RFO count'
                 WHEN rfo_cnt > hybris_cnt
                 THEN 'RFO count more than Hybris count'
                 ELSE 'Count matches - validation passed'
            END Results
    FROM    ( SELECT    COUNT(DISTINCT e.OwnerPkString) hybris_cnt
              FROM      Hybris.dbo.orders a ,
                        Hybris.dbo.users b ,
                        Hybris.dbo.countries c ,
                        Hybris.dbo.paymentinfos d ,
                        Hybris.dbo.addresses e
              WHERE     a.userpk = b.PK
                        AND b.p_country = c.PK
                        AND a.PK = d.OwnerPkString
                        AND d.PK = e.OwnerPkString
                        AND c.isocode = 'US'
                       --AND a.p_template IS NULL
                        AND a.TypePkString = 8796127723602
                        AND d.duplicate = 1
                        AND e.p_billingaddress = 1
                        AND e.duplicate = 1
                        AND b.p_sourcename = 'Hybris-DM'
            ) t1 , --105777
            ( SELECT    COUNT(c.ReturnBillingAddressID) rfo_cnt
              FROM      RFOperations.Hybris.ReturnOrder a  JOIN RodanFieldsLive.dbo.Orders rfl ON A.ReturnOrderID = rfl.orderID
                                                             AND rfl.orderTypeID = 9,
                        Hybris.dbo.users b ,						
                        [Hybris].[ReturnBillingAddress] c ,
						Hybris.returnpayment rp,
				    	Hybris..paymentinfos hpi,
                        Hybris..orders d
              WHERE     a.AccountID = b.p_rfaccountid
                        AND a.ReturnOrderID = c.ReturnOrderID
                        AND a.ReturnOrderID = d.code
						AND c.ReturnOrderID=rp.ReturnOrderID
						 AND hpi.code=CAST(rp.returnpaymentId AS nvarchar)
                        AND a.CountryID = 236                        
                        AND b.p_sourcename = 'Hybris-DM'
            ) t2;
 --

    DELETE  FROM DataMigration.dbo.dm_log
    WHERE   test_area = '853-ReturnPaymentAddress';
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



    CREATE CLUSTERED INDEX as_cls1 ON #tempact (ReturnBillingAddressID)


	SELECT  'Validation of column to column with no transformation in progress' AS [Step-1 Validation part 1] ,
			GETDATE() AS StartTime;

	SET @cnt = 1;
	SELECT  @lt_1 = COUNT(*)
	FROM    DataMigration.dbo.map_tab
	WHERE   flag = 'c2c'
			AND rfo_column <> @RFO_key
			AND [owner] = '853-ReturnPaymentAddress'
			AND id NOT IN (432,434,435,436,438,440);

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
								AND id NOT IN (432,434,435,436,438,440)
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
							AND id NOT IN (432,434,435,436,438,440); 
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
			AND id NOT  IN (432,434,435,436,438,440)
			AND hybris_column NOT IN (
			SELECT DISTINCT
					hybris_column
			FROM    DataMigration..dm_log
			WHERE   test_area = '853-ReturnPaymentAddress'
					AND test_type = 'c2c' );

					SELECT  'Validation of column to column with no transformation in progress' AS [Step-1 Validation part 1] ,
			GETDATE() AS StartTime;

	SET @cnt = 1;
	SELECT  @lt_1 = COUNT(*)
	FROM    DataMigration.dbo.map_tab
	WHERE   flag = 'c2c'
			AND rfo_column <> @RFO_key
			AND [owner] = '853-ReturnPaymentAddress'
			AND id IN (432,434,435,436,438,440);

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
	SELECT b.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
					+ [Schema] + '.' + RFO_Table
					+ ' a, #tempact b where a.ReturnOrderID=b.ReturnOrderID) A  

	LEFT JOIN

	(SELECT b.' + @RFO_key + ', ' + RFO_Column
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
	SELECT b.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
					+ [Schema] + '.' + RFO_Table
					+ ' a, #tempact b where a.ReturnOrderID=b.ReturnOrderID) A  

	RIGHT JOIN

	(SELECT b.' + @RFO_key + ', ' + RFO_Column
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
								AND id IN (432,434,435,436,438,440)
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
					--DECLARE @err_cnt INT;
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
							AND id IN (432,434,435,436,438,440); 
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
			AND id IN (432,434,435,436,438,440)
			AND hybris_column NOT IN (
			SELECT DISTINCT
					hybris_column
			FROM    DataMigration..dm_log
			WHERE   test_area = '853-ReturnPaymentAddress'
					AND test_type = 'c2c' );

	--Defaults Check
	SELECT  'Step-1 Completed, Validation of default columns in progress' AS [Step-2 Validation] ,
			GETDATE() AS StartTime;

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


	--update datamigration.dbo.map_tab
	--set [prev_run_err] = 0
	--where [owner] = '824-Autoship' and flag = 'defaults' and [prev_run_err] is null

	SELECT  'Step-2 completed, Validation of transformed columns in progress' AS [Step-3 Validation] ,
			GETDATE() AS StartTime;

	SET @cnt = 1;
	SELECT  @lt_1 = COUNT(*)
	FROM    DataMigration.dbo.map_tab
	WHERE   flag = 'manual'
			AND rfo_column <> @RFO_key
			AND [owner] = '853-ReturnPaymentAddress';

	WHILE @cnt <= @lt_1
		BEGIN

			SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner]
					+ ''' as test_area, ''' + flag + ''' as test_type, '''
					+ [RFO_Reference Table] + ''' as rfo_column, '''
					+ Hybris_Column + ''' as hybris_column, A.' + @HYB_key
					+ ' as hyb_key, A.Hyb_Trans_col as hyb_value, B.' + @RFO_key
					+ ' as rfo_key, B.RFO_Trans_Col as rfo_value

	FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
					+ ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
			from hybris.dbo.' + Hybris_Table + ' t, 
			hybris.dbo.paymentinfos c,
			hybris.dbo.orders b
			where t.ownerpkstring=c.pk 
			and c.ownerpkstring=b.pk
			and b.typepkstring=8796127723602 
			and p_template is null  
			and currencypk = 8796125855777 
			and c.duplicate = 1) a
	except
	SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
					+ RFO_Table + ') A  

	LEFT JOIN

	(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
					+ RFO_Table + '
	except
	SELECT a.' + @HYB_key + ', ' + Hybris_Column
		+ ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
		from hybris.dbo.' + Hybris_Table + ' t, 
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
	SELECT DISTINCT  ''' + [owner] + ''', ''' + flag + ''', '''
					+ [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
					+ @HYB_key + ', A.Hyb_Trans_col, B.' + @RFO_key
					+ ', B.RFO_Trans_Col

	FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
			+ ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
		from hybris.dbo.' + Hybris_Table + ' t, 
		hybris.dbo.paymentinfos c,
		hybris.dbo.orders b
		where t.ownerpkstring=c.pk 
		and c.ownerpkstring=b.pk
		and p_template is null 
		and b.typepkstring=8796127723602 
		and currencypk = 8796125855777
		and c.duplicate = 1) a
	except
	SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
					+ RFO_Table + ') A  

	RIGHT JOIN

	(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
					+ RFO_Table + '
	except
	SELECT a.' + @HYB_key + ', ' + Hybris_Column
			+ ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
			from hybris.dbo.' + Hybris_Table + ' t, 
			hybris.dbo.paymentinfos c,
			hybris.dbo.orders b
			where t.ownerpkstring=c.pk 
			and c.ownerpkstring=b.pk
			and p_template is null 
			and b.typepkstring=8796127723602 
			and currencypk = 8796125855777 
			and c.duplicate = 1) a) B
	ON A.' + @HYB_key + '=B.' + @RFO_key + ''
			FROM    ( SELECT    * ,
								ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
					  FROM      DataMigration.dbo.map_tab
					  WHERE     flag = 'manual'
								AND rfo_column <> @RFO_key 
	--and id not in (3,4,64,65) --order not migrated yet
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
			AND flag = 'manual'
			AND hybris_column NOT IN (
			SELECT DISTINCT
					hybris_column
			FROM    DataMigration..dm_log
			WHERE   test_area = '853-ReturnPaymentAddress'
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
						  WHERE     [owner] = '853-ReturnPaymentAddress'
									AND flag IN ( 'c2c', 'manual', 'defaults' )
						  EXCEPT
						  SELECT DISTINCT
									hybris_column
						  FROM      DataMigration..dm_log
						  WHERE     test_area = '853-ReturnPaymentAddress'
						) a
			) tab1 ,
			( SELECT    COUNT(id) AS [total no of columns]
			  FROM      DataMigration.dbo.map_tab
			  WHERE     [owner] = '853-ReturnPaymentAddress'
						AND flag IN ( 'c2c', 'manual', 'defaults' )
			) tab2;

