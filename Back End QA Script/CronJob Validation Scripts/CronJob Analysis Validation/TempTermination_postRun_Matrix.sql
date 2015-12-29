USE RFOperations
GO

/****** Object:  StoredProcedure [dbo].[usp_CRjob_Templates_PostRun_Metrics]    Script Date: 11/8/2015 12:41:20 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE   PROC [dbo].[usp_CRjob_TempTermination_postRun_Matrix]
@Template NVARCHAR(10),
@ScheduledDate DATE=NULL ,
@RunDate DATE =NULL 

AS

--DECLARE  @Template NVARCHAR(10)='CRP'
--DECLARE @ScheduledDate DATE='2015-10-11'
--DECLARE @RunDate DATE ='2015-11-06'

 
 IF OBJECT_ID('TEMPDB..##CRP_TEMPS') IS NOT NULL
    DROP TABLE ##CRP_TEMPS;

 IF OBJECT_ID('TEMPDB..##CRPTemplatesReports') IS NOT NULL
    DROP TABLE ##CRPTemplatesReports;

 IF OBJECT_ID('TEMPDB..##Pulse_TEMPS') IS NOT NULL
    DROP TABLE ##Pulse_TEMPS;
 IF OBJECT_ID('TEMPDB..##PulseTemplatesReports') IS NOT NULL
    DROP TABLE ##PulseTemplatesReports;

 IF OBJECT_ID('TEMPDB..##PCPerk_TEMPS') IS NOT NULL
    DROP TABLE ##PCPerk_TEMPS;

 IF OBJECT_ID('TEMPDB..##PCPerkTemplatesReports') IS NOT NULL
    DROP TABLE ##PCPerkTemplatesReports;

	

 --SET @ScheduledDate = '2015-10-11'; 
 --SET @Template = 'CRP';

 DECLARE @BackupTemplateName NVARCHAR(255) ,
    @Sql_1 NVARCHAR(MAX)
    
  
	
	SELECT @ScheduledDate = COALESCE(@ScheduledDate,CAST(GETDATE() AS date))
     SELECT @RunDate = COALESCE(@RunDate,CAST(GETDATE()AS date))
 
 IF @Template = 'CRP'
    OR @Template = 'PCPerk'
    OR @Template = 'Pulse'
    BEGIN
	BEGIN TRY 

        IF @Template = 'CRP'
            BEGIN
                SET @BackupTemplateName = CONCAT('dbo.[', @Template, '_',
                                                 @ScheduledDate,
                                                 '_Template_', 'BackUp]'); 
 

                SET @Sql_1 = 'SELECT * INTO ##CRP_TEMPS FROM '
                    + @BackupTemplateName + ''; 
                EXECUTE sp_executesql @Sql_1;

            


                SELECT COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
				JOIN  ##CRP_TEMPS b ON a.code=b.code
				JOIN Hybris.dbo.vEnumerationValues v ON pk=a.pk
				WHERE a.p_template=1
				AND a.p_active=0 AND b.P_active=1
				

			 SELECT COUNT(DISTINCT a.code) ActiveCounts
                FROM    Hybris..orders a
				JOIN  ##CRP_TEMPS b ON a.code=b.code
				JOIN Hybris.dbo.vEnumerationValues v ON pk=a.pk
				WHERE a.p_template=1
				AND a.p_active=1 AND b.P_active=1
			
					
				
				
                SELECT  'Date IS NOT valid' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##CRP_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1
                        AND a.p_cancelationdate = @RunDate
                        AND ( b.p_schedulingdate <> a.p_schedulingdate
                              OR b.p_lastprocessingdate <> a.p_lastprocessingdate
                             OR a.p_cancelationdate<>@RunDate )
				
                SELECT  'Status not changed' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##CRP_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1                       
                        AND v.Value<>'CANCELLED'

						
                SELECT  'Inactive Templates been Active' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##CRP_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 1
                        AND b.p_active = 0                       
                        
				
				 SELECT  'CcfailureCount<3 made cancelled' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##CRP_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1
                       AND a.p_ccfailurecount<3

					   
				 SELECT  'CcfailureCount>=3 made cancelled' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##CRP_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1
						AND DATEPART(MONTH,a.p_lastprocessingdate ) < DATEPART(MONTH,@RunDate)
						AND DATEPART(MONTH,a.p_schedulingdate ) < DATEPART(MONTH,@RunDate)
                       AND a.p_ccfailurecount>=3




 /* AFTER RUN Metrics and Comparision */
 
                SELECT  a.code TemplateCode ,
                        CAST(a.p_lastprocessingdate AS DATE) AS ReCent_p_lastprocessingdate ,
                        CAST(b.p_lastprocessingdate AS DATE) AS p_lastprocessingdate ,
                        CAST(a.p_schedulingdate AS DATE) AS ReCent_p_schedulingdate ,
                        CAST(b.p_schedulingdate AS DATE) AS p_schedulingdate ,
                        CASE WHEN a.p_active = 1 THEN 'Active'
                             ELSE 'Inactive'
                        END AS RecentActiveFlag ,
                        CASE WHEN b.p_active = 1 THEN 'Active'
                             ELSE 'Inactive'
                        END AS ActiveFlag ,
                        CASE WHEN a.statuspk = 8796093284443 THEN 'CANCELLED'
                             WHEN a.statuspk = 8796134080603 THEN 'PROCESSING'
                             WHEN a.statuspk = 8796135030875 THEN 'PENDING'
                        END AS RecentStatus ,
                        CASE WHEN b.statuspk = 8796093284443 THEN 'CANCELLED'
                             WHEN b.statuspk = 8796095610962 THEN 'PROCESSING'
                             WHEN b.statuspk = 8796135030875 THEN 'PENDING'
                        END BackUpTemplateStatus ,
                        a.p_ccfailurecount AS RecentCcFailureCount ,
                        b.p_ccfailurecount ,
                        CAST (a.p_cancelationdate AS DATE) AS RecentCancellationDate ,
                        CAST (b.p_cancelationdate AS DATE) p_cancelationdate ,
                        CASE WHEN ( CAST(a.p_lastprocessingdate AS DATE) <> CAST(b.p_lastprocessingdate AS DATE)
                                    OR CAST(b.p_schedulingdate AS DATE) <> CAST(a.p_schedulingdate AS DATE)
                                    OR CAST (a.p_cancelationdate AS DATE) <> @RunDate
                                  )
                                  AND b.statuspk = 8796093284443
                             THEN 'Date Field Incorrect'
                             WHEN a.p_ccfailurecount >= 3
                                  AND b.statuspk <> 8796093284443
                             THEN 'Not Cancelled'
                             WHEN a.p_ccfailurecount < 3
                                  AND b.statuspk = 8796093284443
                             THEN 'Wrongly Cancelled'
                        END AS Results
                INTO    ##CRPTemplatesReports
                FROM    Hybris..orders a
                        RIGHT JOIN ##CRP_TEMPS b ON a.code = b.code
                                                    AND a.p_template = 1;                  





                DECLARE @CRPTemplatesReports NVARCHAR(250)= CONCAT('Hybris.dbo.[',
                                                              @Template, '_',
                                                              @ScheduledDate,
                                                              '_CRP_Termination_Report]'); 

                EXEC  ('select * Into '+ @CRPTemplatesReports+ ' from ##CRPTemplatesReports');
				SELECT * FROM  ##CRPTemplatesReports
            END; 



        IF @Template = 'PULSE'
            BEGIN
                SET @BackupTemplateName = CONCAT('dbo.[', @Template, '_',
                                                 @RunDate,
                                                 '_Template_', 'BackUp]'); 
 
            

                SET @Sql_1 = 'SELECT * INTO ##PULSE_TEMPS FROM '
                    + @BackupTemplateName + ''; 
                EXECUTE sp_executesql @Sql_1;

              
				
                SELECT COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
				JOIN  ##PULSE_TEMPS b ON a.code=b.code
				JOIN Hybris.dbo.vEnumerationValues v ON pk=a.pk
				WHERE a.p_template=1
				AND a.p_active=0 AND b.P_active=1
				

			 SELECT COUNT(DISTINCT a.code) ActiveCounts
                FROM    Hybris..orders a
				JOIN ##PULSE_TEMPS b ON a.code=b.code
				JOIN Hybris.dbo.vEnumerationValues v ON pk=a.pk
				WHERE a.p_template=1
				AND a.p_active=1 AND b.P_active=1
			
					
				
				
                SELECT  'Date IS NOT valid' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##PULSE_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1
                        AND a.p_cancelationdate = @RunDate
                        AND ( b.p_schedulingdate <> a.p_schedulingdate
                              OR b.p_lastprocessingdate <> a.p_lastprocessingdate
                             OR a.p_cancelationdate<>@RunDate )
				
                SELECT  'Status not changed' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##PULSE_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1                       
                        AND v.Value<>'CANCELLED'

						
                SELECT  'Inactive Templates been Active' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##PULSE_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 1
                        AND b.p_active = 0                       
                        
				
				 SELECT  'CcfailureCount<3 made cancelled' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##PULSE_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1
                       AND a.p_ccfailurecount<3

					   
				 SELECT  'CcfailureCount>=3 Not Cancelled' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##PULSE_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1
						AND DATEPART(MONTH,a.p_lastprocessingdate ) < DATEPART(MONTH,@RunDate)
						AND DATEPART(MONTH,a.p_schedulingdate ) < DATEPART(MONTH,@RunDate)
                       AND a.p_ccfailurecount>=3




 /* AFTER RUN Metrics and Comparision */


               
                SELECT  a.code TemplateCode ,
                        CAST(a.p_lastprocessingdate AS DATE) AS ReCent_p_lastprocessingdate ,
                        CAST(b.p_lastprocessingdate AS DATE) AS p_lastprocessingdate ,
                        CAST(a.p_schedulingdate AS DATE) AS ReCent_p_schedulingdate ,
                        CAST(b.p_schedulingdate AS DATE) AS p_schedulingdate ,
                        CASE WHEN a.p_active = 1 THEN 'Active'
                             ELSE 'Inactive'
                        END AS RecentActiveFlag ,
                        CASE WHEN b.p_active = 1 THEN 'Active'
                             ELSE 'Inactive'
                        END AS ActiveFlag ,
                        CASE WHEN a.statuspk = 8796093284443 THEN 'CANCELLED'
                             WHEN a.statuspk = 8796134080603 THEN 'PROCESSING'
                             WHEN a.statuspk = 8796135030875 THEN 'PENDING'
                        END AS RecentStatus ,
                        CASE WHEN b.statuspk = 8796093284443 THEN 'CANCELLED'
                             WHEN b.statuspk = 8796095610962 THEN 'PROCESSING'
                             WHEN b.statuspk = 8796135030875 THEN 'PENDING'
                        END BackUpTemplateStatus ,
                        a.p_ccfailurecount AS RecentCcFailureCount ,
                        b.p_ccfailurecount ,
                        CAST (a.p_cancelationdate AS DATE) AS RecentCancellationDate ,
                        CAST (b.p_cancelationdate AS DATE) p_cancelationdate ,
                        CASE WHEN ( CAST(a.p_lastprocessingdate AS DATE) <> CAST(b.p_lastprocessingdate AS DATE)
                                    OR CAST(b.p_schedulingdate AS DATE) <> CAST(a.p_schedulingdate AS DATE)
                                    OR CAST (a.p_cancelationdate AS DATE) <> @RunDate
                                  )
                                  AND b.statuspk = 8796093284443
                             THEN 'Date Field Incorrect'
                             WHEN a.p_ccfailurecount >= 3
                                  AND b.statuspk <> 8796093284443
                             THEN 'Not Cancelled'
                             WHEN a.p_ccfailurecount < 3
                                  AND b.statuspk = 8796093284443
                             THEN 'Wrongly Cancelled'
                        END AS Results
                INTO    ##PULSETemplatesReports 
                FROM    Hybris..orders a
                        RIGHT JOIN ##PULSE_TEMPS b ON a.code = b.code
                                                    AND a.p_template = 1;

              
              





                DECLARE @PULSETemplatesReports NVARCHAR(250)= CONCAT('Hybris.dbo.[',
                                                              @Template, '_',
                                                              @ScheduledDate,
                                                              '_PULSE_Termination_Report]'); 

                EXEC  ('select * Into '+ @PULSETemplatesReports+ ' from ##PULSETemplatesReports');

				SELECT * FROM ##PULSETemplatesReports

            END; 



        IF @Template = 'PCPerk'
            BEGIN
                SET @BackupTemplateName = CONCAT('dbo.[', @Template, '_',
                                                @RunDate,
                                                 '_Template_', 'BackUp]'); 
 
              

                SET @Sql_1 = 'SELECT * INTO ##PCPerk_TEMPS FROM '
                    + @BackupTemplateName + ''; 
                EXECUTE sp_executesql @Sql_1;

            SELECT COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
				JOIN  ##PCPerk_TEMPS b ON a.code=b.code
				JOIN Hybris.dbo.vEnumerationValues v ON pk=a.pk
				WHERE a.p_template=1
				AND a.p_active=0 AND b.P_active=1
				

			 SELECT COUNT(DISTINCT a.code) ActiveCounts
                FROM    Hybris..orders a
				JOIN ##PCPerk_TEMPS b ON a.code=b.code
				JOIN Hybris.dbo.vEnumerationValues v ON pk=a.pk
				WHERE a.p_template=1
				AND a.p_active=1 AND b.P_active=1
			
					
				
				
                SELECT  'Date IS NOT valid' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                       JOIN  ##PCPerk_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1                       
                        AND ( b.p_schedulingdate <> a.p_schedulingdate
                              OR b.p_lastprocessingdate <> a.p_lastprocessingdate
                             OR a.p_cancelationdate<>@RunDate )
				
                SELECT  'Status not changed' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##PCPerk_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1                       
                        AND v.Value<>'CANCELLED'

						
                SELECT  'Inactive Templates been Active' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##PCPerk_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 1
                        AND b.p_active = 0                       
                        
				
				 SELECT  'CcfailureCount<4 made cancelled' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##PCPerk_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1
                       AND a.p_ccfailurecount<4

					   
				 SELECT  'CcfailureCount>=4 Not Cancelled' AS issues, COUNT(DISTINCT a.code) CancelledCounts
                FROM    Hybris..orders a
                        JOIN ##PCPerk_TEMPS b ON a.code = b.code
                        JOIN Hybris.dbo.vEnumerationValues v ON PK = a.PK
                WHERE   a.p_template = 1
                        AND a.p_active = 0
                        AND b.p_active = 1
						AND DATEPART(MONTH,a.p_lastprocessingdate ) < DATEPART(MONTH,@RunDate)
						AND DATEPART(MONTH,a.p_schedulingdate ) < DATEPART(MONTH,@RunDate)
                       AND a.p_ccfailurecount>=4




 /* AFTER RUN Metrics and Comparision */


                  SELECT  a.code TemplateCode ,
                        CAST(a.p_lastprocessingdate AS DATE) AS ReCent_p_lastprocessingdate ,
                        CAST(b.p_lastprocessingdate AS DATE) AS p_lastprocessingdate ,
                        CAST(a.p_schedulingdate AS DATE) AS ReCent_p_schedulingdate ,
                        CAST(b.p_schedulingdate AS DATE) AS p_schedulingdate ,
                        CASE WHEN a.p_active = 1 THEN 'Active'
                             ELSE 'Inactive'
                        END AS RecentActiveFlag ,
                        CASE WHEN b.p_active = 1 THEN 'Active'
                             ELSE 'Inactive'
                        END AS ActiveFlag ,
                        CASE WHEN a.statuspk = 8796093284443 THEN 'CANCELLED'
                             WHEN a.statuspk = 8796134080603 THEN 'PROCESSING'
                             WHEN a.statuspk = 8796135030875 THEN 'PENDING'
                        END AS RecentStatus ,
                        CASE WHEN b.statuspk = 8796093284443 THEN 'CANCELLED'
                             WHEN b.statuspk = 8796095610962 THEN 'PROCESSING'
                             WHEN b.statuspk = 8796135030875 THEN 'PENDING'
                        END BackUpTemplateStatus ,
                        a.p_ccfailurecount AS RecentCcFailureCount ,
                        b.p_ccfailurecount ,
                        CAST (a.p_cancelationdate AS DATE) AS RecentCancellationDate ,
                        CAST (b.p_cancelationdate AS DATE) p_cancelationdate ,
                        CASE WHEN ( CAST(a.p_lastprocessingdate AS DATE) <> CAST(b.p_lastprocessingdate AS DATE)
                                    OR CAST(b.p_schedulingdate AS DATE) <> CAST(a.p_schedulingdate AS DATE)
                                    OR CAST (a.p_cancelationdate AS DATE) <> @RunDate
                                  )
                                  AND b.statuspk = 8796093284443
                             THEN 'Date Field Incorrect'
                             WHEN a.p_ccfailurecount >= 4
                                  AND b.statuspk <> 8796093284443
                             THEN 'Not Cancelled'
                             WHEN a.p_ccfailurecount < 4
                                  AND b.statuspk = 8796093284443
                             THEN 'Wrongly Cancelled'
                        END AS Results
                INTO    ##PCPERKTemplatesReports 
                FROM    Hybris..orders a
                        RIGHT JOIN ##PCPerk_TEMPS b ON a.code = b.code
                                                    AND a.p_template = 1;

               
              





                DECLARE @PCPerkTemplatesReports NVARCHAR(250)= CONCAT('Hybris.dbo.[',
                                                              @Template, '_',
                                                             @ScheduledDate,
                                                              '_PCPerk_CRJob_Report]'); 

                EXEC  ('select * Into '+ @PCPerkTemplatesReports+ ' from ##PCPerkTemplatesReports');


				SELECT * FROM ##PCPerkTemplatesReports
            END;
			
			 END TRY
        BEGIN CATCH
            RAISERROR('Data Set Up Has Failed',16,1);
            SELECT  ERROR_PROCEDURE() AS [SP];
            SELECT  ERROR_MESSAGE() AS [ERROR MESSAGE]; 
            SELECT  ERROR_LINE() AS [ERROR LINE];
        END CATCH; 


    END; 



GO


