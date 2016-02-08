
USE RFOperations;
SET STATISTICS TIME ON;
GO

--CREATE TABLE DataMigration.dbo.ExecResult
--		(Entity NVARCHAR(255),
--		Types NVARCHAR(225),
--		StartedTime TIME,
--		CompletionTime TIME,
--		[Total Time (MM)] NVARCHAR(255),
--		 UserName NVARCHAR(50) ,
		--RunDate DATE)
		--SELECT * FROM DataMigration.dbo.ExecResult

DECLARE @StartedTime TIME;
DECLARE @EndTime TIME; 




/* Autoship Delay Cancellation */



SELECT  'Autoship DelayCancellaton ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);



SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

        SELECT  CASE WHEN ( SELECT  COUNT(*)
                            FROM    Hybris..autoshiplogs
                            GROUP BY PK
                            HAVING  COUNT(*) > 1
                          ) IS NULL THEN 'No Duplicates PK'
                     ELSE 'Duplicate PK Found'
                END AS DuplicatePK_CheckInHybris ,
                t1.* ,
                CASE WHEN t1.CancelResult = 'CancelCountMatching'
                          AND t1.DelayResult = 'DelayCountMatching'
                     THEN 'CountValidation Passed'
                     ELSE 'Validation Failled'
                END ValidationResult ,
                GETDATE() AS DupsNCountCompletedTime
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
                  FROM      ( SELECT    COUNT(a.PK) Hybris_CancelCnt
                              FROM      Hybris.dbo.autoshiplogs a
							  JOIN Hybris..users u ON a.p_rfaccountid=u.p_rfaccountid
							  AND u.p_country=8796100624418
                              WHERE     p_status = 'cancel' AND a.p_source='Hybris-DM'
                                      --  AND p_logdate < GETDATE()
                            ) HC ,--292409
                            ( SELECT    COUNT(autoshipDelayCancellationLogId) RFO_CancelCnt
                              FROM      Logging.autoshipDelayCancellationLog adcl
                                        JOIN Hybris..users u ON CAST(adcl.accountId AS NVARCHAR) = u.p_rfaccountid
                                        JOIN Hybris..countries c ON u.p_country = c.PK
                                                              AND c.isocode = 'US'
								  --JOIN Hybris..orders o ON o.pk=adcl.templateId 
                              WHERE     adcl.[status] = 'Cancel'
                            ) RC ,--292441
                            ( SELECT    COUNT(a.PK) Hybris_DelayCnt
                              FROM      Hybris.dbo.autoshiplogs a
                                         JOIN Hybris..users u ON a.p_rfaccountid = u.p_rfaccountid
                                                              AND u.p_sourcename = 'Hybris-DM'
															  AND u.p_country=8796100624418
                              WHERE     a.[p_status] = 'Delay'
                                        AND a.p_source = 'Hybris-DM'
                                        AND a.p_logdate < GETDATE()
                            ) HD ,--1838916
                            ( SELECT    COUNT(autoshipDelayCancellationLogId) RFO_DelayCnt
                              FROM      Logging.autoshipDelayCancellationLog adcl
                                        JOIN Hybris..users u ON CAST(adcl.accountId AS NVARCHAR) = u.p_rfaccountid
                                        AND u.p_sourcename = 'Hybris-DM'
															  AND u.p_country=8796100624418 
                                       
                              WHERE     adcl.[status] = 'Delay' AND adcl.templateId IN (SELECT code FROM Hybris..orders 
							  WHERE p_template=1)
                            ) RD--1838881
                ) t1;


						
							 

    /* Missing Cancel */
	SELECT 'Missing Details of Cancel orders '

 SELECT *     FROM      ( SELECT    COUNT(a.PK) Hybris_CancelCnt ,a.p_rfaccountid
                              FROM      Hybris.dbo.autoshiplogs a
							  JOIN Hybris.dbo.users u ON a.p_rfaccountid=u.p_rfaccountid
							  WHERE u.p_country=8796100624418 
							  AND a.p_status='Cancel'  
							AND  a.p_source='Hybris-DM'                           
                                       -- AND p_logdate < GETDATE()
									   --292409
									   GROUP BY a.p_rfaccountid
                            ) HC join
                            ( SELECT    COUNT(autoshipDelayCancellationLogId) RFO_CancelCnt,adcl.accountId
                              FROM      RFOperations.Logging.autoshipDelayCancellationLog adcl
                                        JOIN Hybris..users u ON CAST(adcl.accountId AS NVARCHAR) = u.p_rfaccountid
                                        AND u.p_country=8796100624418 								 
                              WHERE     adcl.[status] = 'Cancel'
							  GROUP BY adcl.accountId
							  --292441
                            ) RC ON HC.p_rfaccountid=RC.accountId
							WHERE HC.Hybris_CancelCnt<>RC.RFO_CancelCnt



SELECT 'Missing Details of Delay records'

			/* Missing Delay Records*/

			SELECT * FROM ( SELECT    COUNT(a.PK) Hybris_DelayCnt,a.p_templateid
                           FROM      Hybris.dbo.autoshiplogs a
                                        JOIN Hybris..users u ON a.p_rfaccountid = u.p_rfaccountid
                                                              AND u.p_sourcename = 'Hybris-DM'
															  AND u.p_country=8796100624418
                                        
                              WHERE     a.[p_status] = 'Delay'
                                        AND a.p_source = 'Hybris-DM'
                                        AND a.p_logdate < GETDATE()
										GROUP BY a.p_templateid
										--1838916
                            ) HD 
							JOIN 
                            ( SELECT    COUNT(autoshipDelayCancellationLogId) RFO_DelayCnt,adcl.templateId
                      FROM      RFOperations.Logging.autoshipDelayCancellationLog adcl
                                        JOIN Hybris..users u ON CAST(adcl.accountId AS NVARCHAR) = u.p_rfaccountid
                                        AND u.p_sourcename = 'Hybris-DM'
															  AND u.p_country=8796100624418 
                                        LEFT JOIN Hybris..orders o ON o.PK = adcl.templateId
                              WHERE     adcl.[status] = 'Delay'
							  GROUP BY adcl.templateId
							  --1838928
                            ) RD ON HD.p_templateid=RD.templateId
							WHERE HD.Hybris_DelayCnt<>RD.RFO_DelayCnt



							
                             SELECT  DISTINCT
                                    adcl.templateId
                             FROM   RFOperations.Logging.autoshipDelayCancellationLog adcl
                                    JOIN Hybris..users u ON CAST(adcl.accountId AS NVARCHAR) = u.p_rfaccountid
                                                            AND u.p_sourcename = 'Hybris-DM'
                                                            AND u.p_country = 8796100624418
                                 
                             WHERE  adcl.[status] = 'Delay' AND adcl.templateId IN (SELECT code
							 FROM Hybris..orders WHERE p_template=1)
                             EXCEPT
                             SELECT DISTINCT
                                    a.p_templateid
                             FROM   Hybris.dbo.autoshiplogs a
                                    JOIN Hybris..users u ON a.p_rfaccountid = u.p_rfaccountid
                                                            AND u.p_sourcename = 'Hybris-DM'
															  AND u.p_country = 8796100624418;
                                
								
								

SELECT  'Autoship DelayCancellation ' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,               
        'Autoship DelayCacnellation' AS Entity; 
		
INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship DelayCancellation' AS Entity ,
		'Counts,Dups,Missing' AS Types,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                 DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;         
				