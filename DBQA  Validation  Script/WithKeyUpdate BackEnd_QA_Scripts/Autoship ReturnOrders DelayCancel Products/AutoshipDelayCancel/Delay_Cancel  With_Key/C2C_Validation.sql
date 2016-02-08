 USE RFOperations;
SET STATISTICS TIME ON;
GO

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;


		DECLARE @StartedTime TIME;
DECLARE @EndTime TIME; 

SELECT  'Autoship Header Loading TempTable ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);

		
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
            DROP TABLE #Tempact;			
	
        SELECT DISTINCT adcl.autoshipDelayCancellationLogId INTO    #Tempact
        FROM    Logging.autoshipDelayCancellationLog adcl
                JOIN Hybris..users u ON CAST(adcl.accountId AS NVARCHAR) = u.p_rfaccountid
                AND u.p_country=8796100624418 
                LEFT JOIN Hybris..orders o ON o.code = adcl.templateId AND o.p_template=1
				JOIN Hybris..autoshiplogs at ON at.p_rfaccountid=adcl.accountId
        WHERE   adcl.[status] IS NOT NULL;


		CREATE CLUSTERED INDEX cls1 ON #Tempact (autoshipDelayCancellationLogId)
		  
			
SELECT  'Autoship  DelayCancel Templtable load is Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Autoship DelayCancel Temp Table  ' AS Entity; 
		
		

		SELECT  'Autoship DelayCancel Column to Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
		SELECT  @StartedTime=CAST(GETDATE() AS TIME)
		  
            SET @cnt = 1;
            SELECT  @lt_1 = COUNT(*)
            FROM    [DataMigration].[dbo].[map_tab]
            WHERE   flag = 'c2c'
                    AND rfo_column <> @RFO_key
                    AND [owner] = '985-ASDC';

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
										--AND id NOT IN (158) --Template is null for Cancel status
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


		/* Added this code to get Counting the total of defects.****/

                    IF ( SELECT COUNT(*)
                         FROM   @temp
                       ) > 1
                        BEGIN
                            DECLARE @err_cnt INT;
      
                            SELECT  @err_cnt = CASE WHEN hyb_cnt = 0
                                                    THEN rfo_cnt
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
                                    AND [owner] = '985-ASDC'; 
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
                            WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key,
                                                              '~') )
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
                                    );

                    DELETE  FROM @temp;

                    SET @cnt = @cnt + 1;


                END;
                

        UPDATE  DataMigration.dbo.map_tab
        SET     [prev_run_err] = 0
        WHERE   [owner] = '985-ASDC'
                AND flag = 'C2C'
                AND Hybris_column NOT IN ( SELECT DISTINCT
                                                    hybris_column
                                           FROM     DataMigration.dbo.dm_log
                                           WHERE    test_area = '985-ASDC'
                                                    AND test_type = 'C2C' );


						
SELECT  'Autoship DelayCancel Column to Columns Validation Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Autoship DelayCancel Column to Columns Validation Completed' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship DelayCancel WithKey' AS Entity ,
                'C2C' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;