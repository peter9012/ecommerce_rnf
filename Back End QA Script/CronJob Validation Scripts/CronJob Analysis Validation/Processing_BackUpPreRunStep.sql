USE Hybris
GO
/****** Object:  StoredProcedure [dbo].[usp_CRjob_Templates_PreRun_Backup]    Script Date: 11/7/2015 2:05:41 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO



CREATE PROCEDURE [dbo].[usp_CRjob_Templates_PreRun_Backup]
 @Template NVARCHAR(10), @ScheduledDate DATE=NULL,@RunDate DATE=NULL  
  AS

  
--DECLARE @Template NVARCHAR(10)= 'CRP'; 
--DECLARE @ScheduledDate DATE= '2015-11-11'; 

IF OBJECT_ID('TEMPDB..##CRP_TEMP') IS NOT NULL
DROP TABLE TEMPDB..##CRP_TEMP

IF OBJECT_ID('TEMPDB..##CRP_TEMP_ORDER') IS NOT NULL
DROP TABLE TEMPDB..##CRP_TEMP_Order

IF OBJECT_ID('TEMPDB..##PCPERK_TEMP') IS NOT NULL
DROP TABLE TEMPDB..##CRP_TEMP

IF OBJECT_ID('TEMPDB..##PCPERK_TEMP_Order') IS NOT NULL
DROP TABLE TEMPDB..##CRP_TEMP_Order

IF OBJECT_ID('TEMPDB..##PULSE_TEMP') IS NOT NULL
DROP TABLE TEMPDB..##CRP_TEMP

IF OBJECT_ID('TEMPDB..##PULSE_TEMP_ORDER') IS NOT NULL
DROP TABLE TEMPDB..##CRP_TEMP_ORDER

IF OBJECT_ID('TEMPDB..##PULSE_TEMP_ORDER') IS NOT NULL
DROP TABLE TEMPDB..##CRP_TEMP_ORDER


 
DECLARE @BackupTemplateName NVARCHAR(255) ,
    @BackupOrderName NVARCHAR(255) ,
    @totalCounts INT ,
    @MatchingSchedulingDate INT ,
    @pastScheduledCount INT ,
	@ScheduledToRunDateCounts INT,
    @RecentRanCount INT ,
    @CcFailureCount INT ,
	@CcFailureCount3 INT,
    @CcFailureCount2 INT ,
    @CcFailureCount1 INT ,
    @CcFailureCount0 INT,
	@Date DATE ; 
 
 
SELECT  @Date=COALESCE(@RunDate, CAST(GETDATE()AS DATE ))

SELECT  @ScheduledDate=COALESCE(@ScheduledDate, CAST(GETDATE()AS DATE ))
 


IF @Template = 'CRP'
    OR @Template = 'PCPerk'
    OR @Template = 'Pulse'
    BEGIN
	BEGIN TRY
    
        IF @Template = 'CRP'
            BEGIN
                SET @BackupTemplateName = CONCAT('Hybris.dbo.[', @Template, '_',
                                                 @Date,
                                                 '_Template_', 'BackUp]'); 
 
                SET @BackupOrderName = CONCAT('Hybris.dbo.[', @Template, '_',
                                              @Date,
                                              '_Order_', 'BackUp]'); 

                EXECUTE
                ('SELECT   *   INTO ' + @BackupTemplateName +
                'FROM  Hybris.dbo.orders
                WHERE     p_template = 1
                AND currencypk = 8796125855777
                AND p_active = 1
                AND TypePkString = 8796124676178 --CRPtemplates
                AND CAST(p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)'); 

                
                SELECT  'Backing Up Tempalates been completed' AS StepOne ,
                        GETDATE() AS completionTime;

     
                EXECUTE    ('SELECT   b.*   INTO '+  @BackupOrderName+
                'FROM '   + @BackupTemplateName+ 'a
                JOIN  Hybris.dbo.orders b ON a.pk=b.p_associatedtemplate
                WHERE     a.p_template = 1
                AND a.currencypk = 8796125855777
                AND a.p_active = 1
                AND a.TypePkString = 8796124676178 --CRPtemplates
                AND CAST(a.p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)');

    --DECLARE  @sql NVARCHAR(MAX) = 
                EXECUTE ('SELECT * INTO ##CRP_TEMP FROM '+@BackupTemplateName+ '' );
                EXECUTE ('SELECT * INTO ##CRP_TEMP_Order FROM '+@BackupTemplateName+ '' );
 

    --DECLARE  @sql2 NVARCHAR(MAX) = 'SELECT * INTO #CRP_TEMP_Orders FROM '+@BackupOrderName+ '' 

                SELECT  @pastScheduledCount = COUNT(*)
                FROM    ##CRP_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777
                        AND p_active = 1
                        AND TypePkString = 8796124676178
                        AND CAST(p_schedulingdate AS DATE) < @ScheduledDate; 

					
	

                SELECT  @MatchingSchedulingDate = COUNT(*)
                FROM    ##CRP_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124676178
                        AND CAST(p_schedulingdate AS DATE) = @ScheduledDate; 

                SELECT  @totalCounts = COUNT(*)
                FROM    ##CRP_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124676178
                        AND CAST(p_schedulingdate AS DATE) <= @Date;

						 SELECT  @ScheduledToRunDateCounts = COUNT(*)
                FROM    ##CRP_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124676178
                        AND CAST(p_schedulingdate AS DATE) >@ScheduledDate
						AND CAST(p_schedulingdate AS DATE)<=@Date;


                SELECT  @RecentRanCount = COUNT(*)
                FROM    ##CRP_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124676178
                        AND CAST(p_lastprocessingdate AS DATE) BETWEEN DATEADD(DAY,
                                                              5, @ScheduledDate)
                                                              AND
                                                              @ScheduledDate
                        AND TypePkString = 8796124676178
                        AND statuspk = 8796135030875; /*need a conform On this StatusPk */

                SELECT  @CcFailureCount = COUNT(*)
                FROM    ##CRP_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124676178 
                        AND p_ccfailurecount >= 3; 

						 SELECT  @CcFailureCount2 = COUNT(*)
                FROM    ##CRP_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124676178 
                        AND p_ccfailurecount = 2; 

						 SELECT  @CcFailureCount1 = COUNT(*)
                FROM    ##CRP_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124676178 --active CRPtemplates
                        AND p_ccfailurecount = 1; 

						 SELECT  @CcFailureCount = COUNT(*)
                FROM    ##CRP_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124676178 
                        AND p_ccfailurecount = 0; 
              


                SELECT  CONCAT(@Template, '  Templates') AS TemplateType ,
                        @ScheduledDate AS CronJobScheduledDate ,
                        @totalCounts AS TotalTemplatesToRun ,
                        @MatchingSchedulingDate AS [Counts=RunDate] ,
						@ScheduledToRunDateCounts AS ScheduledToRunDateCounts,
                        @pastScheduledCount AS [Counts<Scheduled] ,
                        @RecentRanCount AS ProcessedWithin5Days ,
                        @CcFailureCount AS [CcFailureCounts>=3] ,
                        @CcFailureCount2 AS [CcFailureCounts=2] ,
                        @CcFailureCount1 AS [CcFailureCounts=1] ,
                        @CcFailureCount0 AS [CcFailureCounts=0];
				
				
            END



        IF @Template = 'PCperk'        

            BEGIN
                SET @BackupTemplateName = CONCAT('Hybris.dbo.[', @Template, '_',
                                                 @Date,
                                                 '_Template_', 'BackUp]'); 
 
                SET @BackupOrderName = CONCAT('Hybris.dbo.[', @Template, '_',
                                              @Date,
                                              '_Order_', 'BackUp]'); 

                EXECUTE
                ('SELECT    *   INTO ' + @BackupTemplateName +
                'FROM    Hybris.dbo.orders
                WHERE     p_template = 1
                AND currencypk = 8796125855777
                AND p_active = 1
                AND TypePkString = 8796124708946 --PcPerkTemplates
                AND CAST(p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)'); 

                
                SELECT  'Backing Up Tempalates been completed' AS StepOne ,
                        GETDATE() AS completionTime;

     
                EXECUTE    ('SELECT   b.*   INTO '+  @BackupOrderName+
                'FROM '   + @BackupTemplateName+ 'a
                JOIN  Hybris.dbo.orders b ON a.pk=b.p_associatedtemplate
                WHERE     a.p_template = 1
                AND a.currencypk = 8796125855777
                AND a.p_active = 1
                AND a.TypePkString = 8796124708946 --PcPerkTemplates
                AND CAST(a.p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)');



                EXECUTE ('SELECT * INTO ##PCPERK_TEMP FROM '+@BackupTemplateName+ '' );
                EXECUTE ('SELECT * INTO ##PCPERK_Order FROM '+@BackupTemplateName+ '' );
 

    

                SELECT  @pastScheduledCount = COUNT(*)
                FROM    ##PCPERK_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777
                        AND p_active = 1
                        AND TypePkString = 8796124708946 --PcPerkTemplates
                        AND CAST(p_schedulingdate AS DATE) < @ScheduledDate; 

					
	

                SELECT  @MatchingSchedulingDate = COUNT(*)
                FROM    ##PCPERK_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124708946
                        AND CAST(p_schedulingdate AS DATE) = @ScheduledDate; 

                SELECT  @totalCounts = COUNT(*)
                FROM    ##PCPERK_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124708946
                        AND CAST(p_schedulingdate AS DATE) <= @Date;


                SELECT  @RecentRanCount = COUNT(*)
                FROM    ##PCPERK_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124708946
                        AND CAST(p_lastprocessingdate AS DATE) BETWEEN DATEADD(DAY,
                                                              5,
                                                              @ScheduledDate)
                                                              AND
                                                              @ScheduledDate
                        AND statuspk = 8796135030875; 
						/*need a conform On this StatusPk */
						
						 SELECT  @ScheduledToRunDateCounts = COUNT(*)
                FROM    ##PCPERK_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124708946
                        AND CAST(p_schedulingdate AS DATE) >@ScheduledDate
						AND CAST(p_schedulingdate AS DATE)<=@Date;

						SELECT  @CcFailureCount = COUNT(*)
                FROM    ##PCPERK_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777 --US autoships
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124708946 
                        AND p_ccfailurecount <= 4; 


                SELECT  @CcFailureCount3 = COUNT(*)
                FROM    ##PCPERK_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777 --US autoships
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124708946 
                        AND p_ccfailurecount = 3; 

						
						SELECT  @CcFailureCount2 = COUNT(*)
                FROM    ##PCPERK_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777 --US autoships
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124708946 
                        AND p_ccfailurecount = 2;
						

						 SELECT  @CcFailureCount1 = COUNT(*)
                FROM    ##PCPERK_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777 --US autoships
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124708946 
                        AND p_ccfailurecount = 1; 

						SELECT  @CcFailureCount0 = COUNT(*)
                FROM    ##PCPERK_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777 --US autoships
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124708946 
                        AND p_ccfailurecount = 0;  

              


                SELECT  CONCAT(@Template, '  Templates') AS TemplateType ,
                        @ScheduledDate AS CronJobRunDate ,
                        @totalCounts AS TotalTemplatesToRun ,
                        @MatchingSchedulingDate AS CountOfRunDate ,
                        @pastScheduledCount AS [Counts<Scheduled] ,
                        @RecentRanCount AS ProcessedWithin5Days ,
						@ScheduledToRunDateCounts AS ScheduledToRunDateCounts,
                        @CcFailureCount AS [CcFailureCounts>=4],
						@CcFailureCount3 AS [CcFailureCount=3],
						@CcFailureCount2 AS [CcFailureCount=2],
						@CcFailureCount1 AS [CcFailureCount=1],
						@CcFailureCount0 AS [CcFailureCount=0]; 

						

            END
     
     

        IF @Template = 'Pulse'
            BEGIN

                SET @BackupTemplateName = CONCAT('Hybris.dbo.[', @Template, '_',
                                                 @Date,
                                                 '_Template_', 'BackUp]'); 
 
                SET @BackupOrderName = CONCAT('Hybris.dbo.[', @Template, '_',
                                              @Date,
                                              '_Order_', 'BackUp]'); 

                EXECUTE
                ('SELECT   *   INTO ' + @BackupTemplateName +
                'FROM  Hybris.dbo.orders
                WHERE     p_template = 1
                AND currencypk = 8796125855777
                AND p_active = 1
                AND TypePkString = 8796124741714 --Pulse Templates
                AND CAST(p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)'); 

                
                SELECT  'Backing Up Tempalates been completed' AS StepOne ,
                        GETDATE() AS completionTime;

     
                EXECUTE    ('SELECT   b.*   INTO '+  @BackupOrderName+
                'FROM '   + @BackupTemplateName+ 'a
                JOIN Hybris.dbo.orders b ON a.pk=b.p_associatedtemplate
                WHERE     a.p_template = 1
                AND a.currencypk = 8796125855777
                AND a.p_active = 1
                AND a.TypePkString = 8796124741714 
                AND CAST(a.p_schedulingdate AS DATE) <=CAST(GETDATE() AS DATE)');

  

                EXECUTE ('SELECT * INTO ##PULSE_TEMP FROM '+@BackupTemplateName+ '' );
                EXECUTE ('SELECT * INTO ##PULSE_TEMP_Order FROM '+@BackupTemplateName+ '' );
 


                SELECT  @pastScheduledCount = COUNT(*)
                FROM    ##PULSE_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777
                        AND p_active = 1
                        AND TypePkString = 8796124741714 
                        AND CAST(p_schedulingdate AS DATE) < @ScheduledDate; 

					
	

                SELECT  @MatchingSchedulingDate = COUNT(*)
                FROM    ##PULSE_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124741714
                        AND CAST(p_schedulingdate AS DATE) = @ScheduledDate; 

                SELECT  @totalCounts = COUNT(*)
                FROM    ##PULSE_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124741714
                        AND CAST(p_schedulingdate AS DATE) <= @Date;


                SELECT  @RecentRanCount = COUNT(*)
                FROM    ##PULSE_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124741714
                        AND CAST(p_lastprocessingdate AS DATE) BETWEEN DATEADD(DAY,
                                                              5, @ScheduledDate)
                                                              AND
                                                              @ScheduledDate
                       
                        AND statuspk = 8796135030875; 
						/*need a conform from pravin */

						 SELECT  @ScheduledToRunDateCounts = COUNT(*)
                FROM     ##PULSE_TEMP
                WHERE   p_template = 1
                        AND p_active = 1
                        AND currencypk = 8796125855777
                        AND TypePkString = 8796124741714
                        AND CAST(p_schedulingdate AS DATE) >@ScheduledDate
						AND CAST(p_schedulingdate AS DATE)<=@Date;


                SELECT  @CcFailureCount = COUNT(*)
                FROM    ##PULSE_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777 --US autoships
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124741714 
                        AND p_ccfailurecount >= 3; 

						   SELECT  @CcFailureCount2 = COUNT(*)
                FROM    ##PULSE_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777 --US autoships
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124741714 
                        AND p_ccfailurecount = 2; 

						 SELECT  @CcFailureCount1 = COUNT(*)
                FROM    ##PULSE_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777 --US autoships
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124741714 
                        AND p_ccfailurecount = 1; 

						 SELECT  @CcFailureCount0 = COUNT(*)
                FROM    ##PULSE_TEMP
                WHERE   p_template = 1
                        AND currencypk = 8796125855777 --US autoships
                        AND CAST(p_schedulingdate AS DATE) <= @ScheduledDate
                        AND p_active = 1
                        AND TypePkString = 8796124741714 
                        AND p_ccfailurecount = 0; 
              


                SELECT  CONCAT(@Template, '  Templates') AS TemplateType ,
                        @ScheduledDate AS CronJobRunDate ,
                        @totalCounts AS TotalTemplatesToRun ,
                        @MatchingSchedulingDate AS CountOfSchedulingDate ,
						@ScheduledToRunDateCounts AS ScheduledToRunDateCounts,
                        @pastScheduledCount AS CountOfPastScheduled ,
                        @RecentRanCount AS ProcessedWithin5Days ,
                        @CcFailureCount AS [CcFailureCounts>=3],
						 @CcFailureCount2 AS [CcFailureCounts=2],
						 @CcFailureCount1 AS [CcFailureCounts=1],
						 @CcFailureCount0 AS [CcFailureCounts=0]; 

						

            END
     
	 END TRY
	 BEGIN CATCH
	 RAISERROR('Data Set Up Has Failed',16,1);
            SELECT  ERROR_PROCEDURE() AS [SP];
            SELECT  ERROR_MESSAGE() AS [ERROR MESSAGE]; 
            SELECT  ERROR_LINE() AS [ERROR LINE];
	 END CATCH
    END 


      

   