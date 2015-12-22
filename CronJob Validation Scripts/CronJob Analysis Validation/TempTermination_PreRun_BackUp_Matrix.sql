USE Hybris
GO 
CREATE  PROCEDURE dbo.usp_CRJob_TempTermination_PreRun_BackUp_Matrix
@Template NVARCHAR(25)=NULL ,@RunDate DATE=NULL, @ScheduledDate DATE=NULL 
AS 
BEGIN

		IF OBJECT_ID('Tempdb..#crp') IS NOT NULL 
		DROP TABLE #crp
		
		IF OBJECT_ID('Tempdb..#crpDetails') IS NOT NULL 
		DROP TABLE #crpDetails
		
		IF OBJECT_ID('Tempdb..#Pcperk') IS NOT NULL 
		DROP TABLE #Pcperk
		
		IF OBJECT_ID('Tempdb..#PcperkDetails') IS NOT NULL 
		DROP TABLE #PcperkDetails

		IF OBJECT_ID('Tempdb..#Pulse') IS NOT NULL 
		DROP TABLE #Pulse

		IF OBJECT_ID('Tempdb..#PulseDetails') IS NOT NULL 
		DROP TABLE #PulseDetails

		IF OBJECT_ID('Tempdb..##CRP_TEMPS') IS NOT NULL 
		DROP TABLE ##CRP_TEMPS

		IF OBJECT_ID('Tempdb..##pc_TEMPS') IS NOT NULL 
		DROP TABLE ##pc_TEMPS

		IF OBJECT_ID('Tempdb.. ##pulse_TEMPS') IS NOT NULL 
		DROP TABLE  ##pulse_TEMPS


 DECLARE @Counts1 INT ,
    @Count2 INT ,
    @Count3 INT ,
    @Count4 INT ,
    @Count5 INT ,
    @Count6 INT ,
    @Count7 INT,
	@Count8 INT,
	@Count9 INT ,
	@Count10 INT ,
	@BackupTemplateName NVARCHAR(225),
	 @BackupOrderName NVARCHAR(255),
	 @sql_1 NVARCHAR(255)

	
	SELECT @ScheduledDate = COALESCE(@ScheduledDate,CAST(GETDATE() AS date))
     SELECT @RunDate = COALESCE(@RunDate,CAST(GETDATE()AS date))
 
 IF @Template IS NOT NULL 


	BEGIN

	IF @Template = 'CRP'

	BEGIN

	 SET @BackupTemplateName = CONCAT('Hybris.dbo.[', @Template, '_',
                                                 @RunDate,
                                                 '_TermTemp_', 'BackUp]'); 
 
                EXECUTE
                ('SELECT   *   INTO ' + @BackupTemplateName +
                'FROM  Hybris.dbo.orders
                WHERE     p_template = 1
                AND currencypk = 8796125855777
                AND p_active = 1
                AND TypePkString = 8796124676178 --CRPtemplates
                AND CAST(p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)'); 

				 

                SET @Sql_1 = 'SELECT * INTO ##CRP_TEMPS FROM '
                    + @BackupTemplateName + ' '; 
                EXECUTE sp_executesql @Sql_1;

               






 SELECT @Counts1 = COUNT(DISTINCT a.code)
 FROM   ##CRP_TEMPS a
        LEFT JOIN Hybris.dbo.orders a1 ON a.PK = a1.p_associatedtemplate
                                       AND a1.TypePkString IN ( 8796124676178,
                                                              8796124708946,
                                                              8796124741714 )
                                       AND a1.statuspk IN ( 8796134178907,
                                                            8796134113371,
                                                            8796093251675 )
        JOIN Hybris.dbo.composedtypes c ON c.PK = a.TypePkString
 WHERE  a.p_template = 1
        AND a.currencypk = 8796125855777  --US templates
        AND a.p_active = 1
        AND a.TypePkString = 8796124676178
        AND a.p_totalqv < 80
        AND a1.PK IS NULL

 
 
  

 SELECT @Count2 = COUNT(DISTINCT a.code)
 FROM   ##CRP_TEMPS a
        JOIN Hybris.dbo.orders a1 ON a.PK = a1.p_associatedtemplate
                                  AND a1.TypePkString IN ( 8796124676178,
                                                           8796124708946,
                                                           8796124741714 )
                                  AND a1.statuspk IN ( 8796134178907,
                                                       8796134113371,
                                                       8796093251675 )
        JOIN Hybris.dbo.composedtypes c ON c.PK = a.TypePkString
 WHERE  a.p_template = 1
        AND a.currencypk = 8796125855777  --US templates
        AND a.p_active = 1
        AND a.TypePkString = 8796124676178
        AND a.p_totalqv < 80
       



---QV<80 with NO pulse 
 SELECT @Count3 = COUNT(DISTINCT b.code)
 FROM   Hybris..users a
        JOIN ( SELECT   crp.code ,
                        crp.userpk ,
                        crp.p_template ,
                        crp.p_active ,
                        crp.p_totalqv ,
                        crp.TypePkString
               FROM     ##CRP_TEMPS crp
                        JOIN Hybris.dbo.orders crp_orders ON crp.PK = crp_orders.p_associatedtemplate
                                                          AND crp_orders.TypePkString IN (
                                                          8796124676178,
                                                          8796124708946,
                                                          8796124741714 )
               WHERE    crp.p_template = 1
                        AND crp.p_active = 1
                        AND crp.TypePkString = 8796124676178
                        AND crp_orders.statuspk IN ( 8796134178907,
                                                     8796134113371,
                                                     8796093251675 )
             ) b ON a.PK = b.userpk
        LEFT JOIN Hybris.dbo.orders pulse ON a.PK = pulse.userpk
                                          AND pulse.TypePkString = 8796124741714
                                          AND pulse.p_template = 1
                                          AND pulse.p_active = 1
 WHERE  a.p_country = 8796100624418  --US templates
 AND b.TypePkString =8796124676178
        AND b.p_totalqv < 80
        AND pulse.PK IS NULL
 GROUP BY b.TypePkString;

--WITH pusleSubcriptionwith: QV<80 
 SELECT @Count4 = COUNT(DISTINCT b.code)
 FROM   Hybris..users a
        JOIN ( SELECT   crp.code ,
                        crp.userpk ,
                        crp.p_template ,
                        crp.p_active ,
                        crp.p_totalqv ,
                        crp.TypePkString
               FROM    ##CRP_TEMPS crp
                        JOIN Hybris.dbo.orders crp_orders ON crp.PK = crp_orders.p_associatedtemplate
                                                          AND crp_orders.TypePkString IN (
                                                          8796124676178,
                                                          8796124708946,
                                                          8796124741714 )
               WHERE    crp.p_template = 1
                        AND crp.p_active = 1
                        AND crp.TypePkString = 8796124676178
                        AND crp_orders.statuspk IN ( 8796134178907,
                                                     8796134113371,
                                                     8796093251675 )
             ) b ON a.PK = b.userpk
        JOIN Hybris.dbo.orders pulse ON a.PK = pulse.userpk
                                     AND pulse.TypePkString = 8796124741714
                                     AND pulse.p_template = 1
                                     AND pulse.p_active = 1
 WHERE  a.p_country = 8796100624418  --US templates
  AND b.TypePkString =8796124676178
        AND b.p_totalqv < 80

		--QV >=80 AND <=100 WITH PULSE
 SELECT @Count5 = COUNT(DISTINCT b.code)
 FROM   Hybris..users a
        JOIN  ##CRP_TEMPS b ON a.PK = b.userpk
        JOIN Hybris.dbo.orders pulse ON a.PK = pulse.userpk
                                     AND pulse.TypePkString = 8796124741714
                                     AND pulse.p_template = 1
                                     AND pulse.p_active = 1
 WHERE  a.p_country = 8796100624418  --US templates
        AND b.TypePkString = 8796124676178
        AND b.p_totalqv >= 80
        AND b.p_totalqv <= 100


		--QV >=80 AND <=100 NO  PULSE
 SELECT @Count6 = COUNT(DISTINCT b.code)
 FROM   Hybris..users a
        JOIN ( SELECT   crp.code ,
                        crp.userpk ,
                        crp.p_template ,
                        crp.p_active ,
                        crp.p_totalqv ,
                        crp.TypePkString
               FROM    ##CRP_TEMPS crp
                        JOIN Hybris.dbo.orders crp_orders ON crp.PK = crp_orders.p_associatedtemplate
                                                          AND crp_orders.TypePkString IN (
                                                          8796124676178,
                                                          8796124708946,
                                                          8796124741714 )
               WHERE    crp.p_template = 1
                        AND crp.p_active = 1
                        AND crp.TypePkString = 8796124676178
                        AND crp_orders.statuspk IN ( 8796134178907,
                                                     8796134113371,
                                                     8796093251675 )
             ) b ON a.PK = b.userpk
        LEFT JOIN Hybris.dbo.orders pulse ON a.PK = pulse.userpk
                                          AND pulse.TypePkString = 8796124741714
                                          AND pulse.p_template = 1
                                          AND pulse.p_active = 1
 WHERE  a.p_country = 8796100624418  --US templates
        AND b.TypePkString = 8796124676178
        AND pulse.PK IS NULL
        AND b.p_totalqv >= 80
        AND b.p_totalqv <= 100
        



--QV>=100

 SELECT @Count7 = COUNT(DISTINCT a.code)
 FROM  ##CRP_TEMPS a    
 WHERE  a.p_template = 1
        AND a.currencypk = 8796125855777  --US templates
        AND a.p_active = 1
        AND a.TypePkString = 8796124676178
        AND a.p_totalqv >= 100
       
		
		---CcFailure>=3
		SELECT @Count8= COUNT(DISTINCT a.code) 
FROM    ##CRP_TEMPS a       
WHERE   a.p_template = 1
        AND a.currencypk = 8796125855777  
        AND a.p_active = 1
        AND a.TypePkString IN ( 8796124676178)
        AND a.p_ccfailurecount >= 3 
       

		
		---TotalTemplates 
		SELECT @Count9= COUNT(DISTINCT a.code) 
FROM    ##CRP_TEMPS a       
WHERE   a.p_template = 1
        AND a.currencypk = 8796125855777  
        AND a.p_active = 1
        AND a.TypePkString IN ( 8796124676178)
        
       
	   
		---counts Inactive Templates 
		SELECT @Count10= COUNT(DISTINCT a.code) 
FROM    Hybris..orders a       
WHERE   a.p_template = 1
        AND a.currencypk = 8796125855777  
        AND a.p_active <> 1
        AND a.TypePkString IN ( 8796124676178)
        
       


 SELECT 'CRP' AS TemplateType ,
        @Count9 AS TotalCounts ,
		@Count10 AS CountsInactive,
        @Counts1 AS [ QV<80 & No Orders] ,
        @Count2 AS [ QV<80 & WithOrders] ,
        @Count3 AS [QV<80 NoPulse] ,
        @Count4 AS [QV<80 WithPulse] ,
        @Count5 AS [QV >=80 AND <=100 WithPULSE] ,
        @Count6 AS [QV >=80 AND <=100 NO-PULSE] ,
        @Count7 AS [QV>=100] ,
        @Count8 AS [CcFailure>=3]
 INTO   #crp;

		SELECT * FROM #crp

SELECT DISTINCT
        c.InternalCode AS TemplateType ,
        b.code AS TemplateCode ,
        a.p_rfaccountid AS AcccountNumber ,
		b.p_lastprocessingdate AS LastPDate,
		b.p_schedulingdate AS ScheduledDate,
        CASE WHEN b.p_template = 1 THEN 'Active'
             ELSE 'InActive'
        END AS TemplateStatus ,       
        CASE WHEN pulse.PK IS NOT NULL THEN 'Yes'
             ELSE 'No'
        END AS PulseSbscription ,
        b.p_ccfailurecount INTO #CrpDetials
FROM    Hybris..users a
        JOIN  ##CRP_TEMPS b ON a.pk=b.userpk
        LEFT JOIN hybris..orders pulse ON a.PK = pulse.userpk
                                          AND pulse.TypePkString = 8796124741714
                                          AND pulse.p_template = 1
                                          AND pulse.p_active = 1
        JOIN Hybris.dbo.composedtypes c ON c.PK = b.TypePkString
WHERE   a.p_country = 8796100624418  --US templates
        AND b.TypePkString = 8796124676178

      SELECT * FROM #CrpDetials


	  END 





IF @Template='PcPerk'
	  
	BEGIN

	 SET @BackupTemplateName = CONCAT('Hybris.dbo.[', @Template, '_',
                                                 @RunDate,
                                                 '_TermTemp_', 'BackUp]'); 
 
                EXECUTE
                ('SELECT   *   INTO ' + @BackupTemplateName +
                'FROM  Hybris.dbo.orders
                WHERE     p_template = 1
                AND currencypk = 8796125855777
                AND p_active = 1
                AND TypePkString = 8796124708946 --PCperk Templates
                AND CAST(p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)'); 

				 

                SET @Sql_1 = 'SELECT * INTO ##pc_TEMPS FROM '
                    + @BackupTemplateName + ' '; 
                EXECUTE sp_executesql @Sql_1;

               

 SELECT  @Counts1 =  COUNT(DISTINCT a.code)
 FROM  ##pc_TEMPS a
        LEFT JOIN Hybris..orders a1 ON a.PK = a1.p_associatedtemplate
                                       AND a1.TypePkString IN ( 8796124676178,
                                                              8796124708946,
                                                              8796124741714 )
                                       AND a1.statuspk IN ( 8796134178907,
                                                            8796134113371,
                                                            8796093251675 )
        
 WHERE  a.p_template = 1
        AND a.currencypk = 8796125855777  --US templates
        AND a.p_active = 1
        AND a.TypePkString = 8796124708946 --PC Perk 
        AND a.p_totalqv < 80
        AND a1.PK IS NULL

 
 
  

 SELECT @Count2 =  COUNT(DISTINCT a.code)
 FROM  ##pc_TEMPS a
        JOIN Hybris..orders a1 ON a.PK = a1.p_associatedtemplate
                                  AND a1.TypePkString IN ( 8796124676178,
                                                           8796124708946,
                                                           8796124741714 )
                                  AND a1.statuspk IN ( 8796134178907,
                                                       8796134113371,
                                                       8796093251675 )
        
 WHERE  a.p_template = 1
        AND a.currencypk = 8796125855777  
        AND a.p_active = 1
        AND a.TypePkString = 8796124708946
        AND a.p_totalqv < 80
       



---QV<80 with NO pulse 
 SELECT @Count3 = COUNT(DISTINCT b.code)
 FROM ##pc_TEMPS a
        JOIN ( SELECT   pc.code ,
                       pc.userpk ,
                        pc.p_template ,
                        pc.p_active ,
                        pc.p_totalqv ,
                        pc.TypePkString
               FROM    ##pc_TEMPS pc
                        JOIN Hybris..orders pc_orders ON pc.PK = pc_orders.p_associatedtemplate
                                                          AND pc_orders.TypePkString IN (
                                                          8796124676178,
                                                          8796124708946,
                                                          8796124741714 )
               WHERE    pc.p_template = 1
                        AND pc.p_active = 1
                        AND pc.TypePkString = 8796124708946
                        AND pc_orders.statuspk IN ( 8796134178907,
                                                     8796134113371,
                                                     8796093251675 )
             ) b ON a.PK = b.userpk
        LEFT JOIN Hybris..orders pulse ON a.PK = pulse.userpk
                                          AND pulse.TypePkString = 8796124741714
                                          AND pulse.p_template = 1
                                          AND pulse.p_active = 1
 WHERE  a.p_country = 8796100624418  --US templates
AND b.TypePkString =8796124708946
        AND b.p_totalqv < 80
        AND pulse.PK IS NULL


--WITH pusleSubcriptionwith: QV<80 
 SELECT @Count4 = COUNT(DISTINCT b.code)
 FROM   ##pc_TEMPS a
        JOIN ( SELECT   pc.code ,
                        pc.userpk ,
                        pc.p_template ,
                        pc.p_active ,
                        pc.p_totalqv ,
                        pc.TypePkString
               FROM    ##pc_TEMPS pc
                        JOIN Hybris..orders pc_orders ON pc.PK = pc_orders.p_associatedtemplate
                                                          AND pc_orders.TypePkString IN (
                                                          8796124676178,
                                                          8796124708946,
                                                          8796124741714 )
               WHERE    pc.p_template = 1
                        AND pc.p_active = 1
                        AND pc.TypePkString = 8796124708946
                        AND pc_orders.statuspk IN ( 8796134178907,
                                                     8796134113371,
                                                     8796093251675 )
             ) b ON a.PK = b.userpk
        JOIN Hybris..orders pulse ON a.PK = pulse.userpk
                                     AND pulse.TypePkString = 8796124741714
                                     AND pulse.p_template = 1
                                     AND pulse.p_active = 1
 WHERE  a.p_country = 8796100624418  --US templates
 AND b.TypePkString =8796124708946
        AND b.p_totalqv < 80

		--QV >=80 AND <=100 WITH PULSE
 SELECT @Count5 = COUNT(DISTINCT b.code)
 FROM   Hybris..users a
        JOIN ( SELECT   crp.code ,
                        crp.userpk ,
                        crp.p_template ,
                        crp.p_active ,
                        crp.p_totalqv ,
                        crp.TypePkString
               FROM     ##pc_TEMPS crp
                        JOIN Hybris..orders crp_orders ON crp.PK = crp_orders.p_associatedtemplate
                                                          AND crp_orders.TypePkString IN (
                                                          8796124676178,
                                                          8796124708946,
                                                          8796124741714 )
               WHERE    crp.p_template = 1
                        AND crp.p_active = 1
                        AND crp.TypePkString = 8796124708946
                        AND crp_orders.statuspk IN ( 8796134178907,
                                                     8796134113371,
                                                     8796093251675 )
             ) b ON a.PK = b.userpk
        JOIN Hybris..orders pulse ON a.PK = pulse.userpk
                                     AND pulse.TypePkString = 8796124741714
                                     AND pulse.p_template = 1
                                     AND pulse.p_active = 1
 WHERE  a.p_country = 8796100624418  --US templates
        AND b.TypePkString = 8796124708946
        AND b.p_totalqv >= 80
        AND b.p_totalqv <= 100


		--QV >=80 AND <=100 NO  PULSE
 SELECT @Count6 = COUNT(DISTINCT b.code)
 FROM   Hybris..users a
        JOIN ( SELECT   crp.code ,
                        crp.userpk ,
                        crp.p_template ,
                        crp.p_active ,
                        crp.p_totalqv ,
                        crp.TypePkString
               FROM     ##pc_TEMPS crp
                        JOIN Hybris..orders crp_orders ON crp.PK = crp_orders.p_associatedtemplate
                                                          AND crp_orders.TypePkString IN (
                                                          8796124676178,
                                                          8796124708946,
                                                          8796124741714 )
               WHERE    crp.p_template = 1
                        AND crp.p_active = 1
                        AND crp.TypePkString = 8796124708946
                        AND crp_orders.statuspk IN ( 8796134178907,
                                                     8796134113371,
                                                     8796093251675 )
             ) b ON a.PK = b.userpk
        LEFT JOIN Hybris..orders pulse ON a.PK = pulse.userpk
                                          AND pulse.TypePkString = 8796124741714
                                          AND pulse.p_template = 1
                                          AND pulse.p_active = 1
 WHERE  a.p_country = 8796100624418  --US templates
        AND b.TypePkString = 8796124708946
        AND pulse.PK IS NULL
        AND b.p_totalqv >= 80
        AND b.p_totalqv <= 100
        



--QV>=100

 SELECT @Count7 = COUNT(DISTINCT a.code)
 FROM  ##pc_TEMPS a
        LEFT JOIN Hybris..orders a1 ON a.PK = a1.p_associatedtemplate
                                       AND a1.TypePkString IN ( 8796124676178,
                                                              8796124708946,
                                                              8796124741714 )
                                       AND a1.statuspk IN ( 8796134178907,
                                                            8796134113371,
                                                            8796093251675 )
        JOIN Hybris.dbo.composedtypes c ON c.PK = a.TypePkString
 WHERE  a.p_template = 1
        AND a.currencypk = 8796125855777  --US templates
        AND a.p_active = 1
        AND a.TypePkString = 8796124708946
        AND a.p_totalqv <= 100
       

		
		---CcFailure>=4
		SELECT @Count8= COUNT(DISTINCT a.code) 
FROM   ##pc_TEMPS a       
WHERE   a.p_template = 1
        AND a.currencypk = 8796125855777  
        AND a.p_active = 1
        AND a.TypePkString IN ( 8796124708946)
        AND a.p_ccfailurecount >= 4
        
		
		---TotalTemplates 
		SELECT @Count9= COUNT(DISTINCT a.code) 
FROM    ##pc_TEMPS a       
WHERE   a.p_template = 1
        AND a.currencypk = 8796125855777  
        AND a.p_active = 1
        AND a.TypePkString IN ( 8796124708946)

		---Counts of Inavtive templates 
		SELECT @Count10= COUNT(DISTINCT a.code) 
FROM    ##pc_TEMPS a       
WHERE   a.p_template <> 1
        AND a.currencypk = 8796125855777  
        AND a.p_active = 1
        AND a.TypePkString IN ( 8796124708946)
        
      


		IF OBJECT_ID('Tempdb.. #pcPerk') IS NOT NULL 
		DROP TABLE  #pcPerk

 SELECT 'PCperk' AS TemplateType ,
        @Count9 AS TotalCounts ,
		@Count10 AS CountsInactive,
        @Counts1 AS [ QV<80 & No Orders] ,
        @Count2 AS [ QV<80 & WithOrders] ,
        @Count3 AS [QV<80 NoPulse] ,
        @Count4 AS [QV<80 WithPulse] ,
        @Count5 AS [QV >=80 AND <=100 WithPULSE] ,
        @Count6 AS [QV >=80 AND <=100 NO-PULSE] ,
        @Count7 AS [QV>=100] ,
        @Count8 AS [CcFailure>=3]
 INTO   #pcPerk;

		SELECT * FROM #pcPerk

		
		IF OBJECT_ID('Tempdb.. #pcperkDetails') IS NOT NULL 
		DROP TABLE  #pcperkDetails

SELECT DISTINCT
        c.InternalCode AS TemplateType ,
        b.code AS TemplateCode ,
        a.p_rfaccountid AS AcccountNumber ,
		b.p_lastprocessingdate AS LastPDate,
		b.p_schedulingdate AS ScheduledDate,
        CASE WHEN b.p_template = 1 THEN 'Active'
             ELSE 'InActive'
        END AS TemplateStatus ,       
        CASE WHEN pulse.PK IS NOT NULL THEN 'Yes'
             ELSE 'No'
        END AS PulseSbscription ,
        b.p_ccfailurecount #pcperkDetails
FROM   Hybris.dbo.users  a
        JOIN ##pc_TEMPS b ON a.pk=b.userpk               
        LEFT JOIN Hybris..orders pulse ON a.PK = pulse.userpk
                                          AND pulse.TypePkString = 8796124741714
                                          AND pulse.p_template = 1
                                          AND pulse.p_active = 1
        JOIN Hybris.dbo.composedtypes c ON c.PK = b.TypePkString
WHERE   a.p_country = 8796100624418  --US templates
        AND b.TypePkString = 8796124708946

      
	  SELECT * FROM #pcperDetials


	  END 



	  IF @Template='Pulse'

	BEGIN

	 SET @BackupTemplateName = CONCAT('Hybris.dbo.[', @Template, '_',
                                                 @RunDate,
                                                 '_TermTemp_', 'BackUp]'); 
 
                EXECUTE
                ('SELECT   *   INTO ' + @BackupTemplateName +
                'FROM  Hybris.dbo.orders
                WHERE     p_template = 1
                AND currencypk = 8796125855777
                AND p_active = 1
                AND TypePkString = 8796124741714 --Pulse Templates
                AND CAST(p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)'); 

				 

                SET @Sql_1 = 'SELECT * INTO ##pulse_TEMPS FROM '
                    + @BackupTemplateName + ' '; 
                EXECUTE sp_executesql @Sql_1;




 SELECT  @Counts1 =  COUNT(DISTINCT a.code)
 FROM   ##pulse_TEMPS a
        LEFT JOIN Hybris..orders a1 ON a.PK = a1.p_associatedtemplate
                                       AND a1.TypePkString IN ( 8796124676178,
                                                              8796124708946,
                                                              8796124741714 )
                                       AND a1.statuspk IN ( 8796134178907,
                                                            8796134113371,
                                                            8796093251675 )
        
 WHERE  a.p_template = 1
        AND a.currencypk = 8796125855777  --US templates
        AND a.p_active = 1
        AND a.TypePkString = 8796124741714 --Pulse
        AND a.p_totalqv < 80
        AND a1.PK IS NULL

 
 
  

 SELECT @Count2 =  COUNT(DISTINCT a.code)
 FROM  ##pulse_TEMPS a
        JOIN Hybris..orders a1 ON a.PK = a1.p_associatedtemplate
                                  AND a1.TypePkString IN ( 8796124676178,
                                                           8796124708946,
                                                           8796124741714 )
                                  AND a1.statuspk IN ( 8796134178907,
                                                       8796134113371,
                                                       8796093251675 )
        
 WHERE  a.p_template = 1
        AND a.currencypk = 8796125855777  
        AND a.p_active = 1
        AND a.TypePkString = 8796124741714
        AND a.p_totalqv < 80
       




		--QV >=80 AND <=100 NO  PULSE
 SELECT @Count6 = COUNT(DISTINCT code)
 FROM   ##pulse_TEMPS              
 WHERE currencypk = 8796125855777    --US templates
        AND b.TypePkString = 8796124741714
        AND pulse.PK IS NULL
        AND b.p_totalqv >= 80
        AND b.p_totalqv <= 100
        



--QV>=100

 SELECT @Count7 = COUNT(DISTINCT a.code)
 FROM   ##pulse_TEMPS a
        LEFT JOIN Hybris..orders a1 ON a.PK = a1.p_associatedtemplate
                                       AND a1.TypePkString IN ( 8796124676178,
                                                              8796124708946,
                                                              8796124741714 )
                                       AND a1.statuspk IN ( 8796134178907,
                                                            8796134113371,
                                                            8796093251675 )
        JOIN Hybris.dbo.composedtypes c ON c.PK = a.TypePkString
 WHERE  a.p_template = 1
        AND a.currencypk = 8796125855777  --US templates
        AND a.p_active = 1
        AND a.TypePkString = 8796124741714
        AND a.p_totalqv <= 100
        AND a1.PK IS NULL

		
		---CcFailure>=4
		SELECT @Count8= COUNT(DISTINCT a.code) 
FROM    ##pulse_TEMPS a       
WHERE   a.p_template = 1
        AND a.currencypk = 8796125855777  
        AND a.p_active = 1
        AND a.TypePkString IN ( 8796124741714)
        AND a.p_ccfailurecount >= 3
       

		
		---TotalTemplates 
		SELECT @Count9= COUNT(DISTINCT a.code) 
FROM   ##pulse_TEMPS a       
WHERE   a.p_template = 1
        AND a.currencypk = 8796125855777  
        AND a.p_active = 1
        AND a.TypePkString IN ( 8796124741714)

		---Counts of Inavtive templates 
		SELECT @Count10= COUNT(DISTINCT a.code) 
FROM    ##pulse_TEMPS a       
WHERE   a.p_template <> 1
        AND a.currencypk = 8796125855777  
        AND a.p_active = 1
        AND a.TypePkString IN ( 8796124741714)
        
       
	   IF OBJECT_ID('Tempdb..#pulse') IS NOT NULL
       DROP TABLE #pulse
	   

 SELECT 'Pulse' AS TemplateType ,
        @Count9 AS TotalCounts ,
		@Count10 AS CountsInactive,
        @Counts1 AS [ QV<80 & No Orders] ,
        @Count2 AS [ QV<80 & WithOrders] ,
        0 AS [QV<80 NoPulse] ,
        0  AS [QV<80 WithPulse] ,
        0  AS [QV >=80 AND <=100 WithPULSE] ,
        @Count6 AS [QV >=80 AND <=100 NO-PULSE] ,
        @Count7 AS [QV>=100] ,
        @Count8 AS [CcFailure>=CrT]
 INTO   #pulse;

		SELECT * FROM #pulse



		  IF OBJECT_ID('Tempdb..#pulseDetails') IS NOT NULL
       DROP TABLE #pulseDetails
SELECT DISTINCT
        c.InternalCode AS TemplateType ,
        b.code AS TemplateCode ,
        a.p_rfaccountid AS AcccountNumber ,
		b.p_lastprocessingdate AS LastPDate,
		b.p_schedulingdate AS ScheduledDate,
        CASE WHEN b.p_template = 1 THEN 'Active'
             ELSE 'InActive'
        END AS TemplateStatus ,       
       'Yes' PulseSbscription ,
        b.p_ccfailurecount INTO #pulseDetails
FROM    Hybris..users a
        JOIN Hybis.dbo.orders b ON a.PK = b.userpk       
        JOIN Hybris.dbo.composedtypes c ON c.PK = b.TypePkString
WHERE   a.p_country = 8796100624418  --US templates
        AND b.TypePkString = 8796124741714

     SELECT * FROM #pulseDetails

	  END 

	END 

BEGIN

	

	  
	  IF OBJECT_ID('TempDb..##bulk_TEMPS')IS NOT NULL 
	  DROP TABLE ##bulk_TEMPS

	   SET @BackupTemplateName = CONCAT('Hybris.dbo.[BUlk_',
                                                 @RunDate,
                                                 '_TermTemp_', 'BackUp]'); 
 

 


SET @BackupTemplateName=CONCAT('Hybris.dbo.[BUlk_',
                                                 @RunDate,
                                                 '_TermTemp_', 'BackUp]')
                EXECUTE
                ('SELECT   *   INTO ' + @BackupTemplateName +
                'FROM  Hybris.dbo.orders
                WHERE     p_template = 1
                AND currencypk = 8796125855777
                AND p_active = 1                
                AND CAST(p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)'); 

			
			
				 

                SET @Sql_1 = 'SELECT * INTO ##bulk_TEMPS FROM '
                    + @BackupTemplateName + ' '; 
                EXECUTE sp_executesql @Sql_1;

	  
SELECT  c.InternalCode AS TemplateType ,
        b.code AS TemplateCode ,
        a.p_rfaccountid AS AcccountNumber ,
        b.p_lastprocessingdate AS LastPDate ,
        b.p_schedulingdate AS ScheduledDate ,
        CASE WHEN b.p_template = 1 THEN 'Active'
             ELSE 'InActive'
        END AS TemplateStatus ,
        CASE WHEN pulse.PK IS NOT NULL THEN 'Yes'
             ELSE 'No'
        END AS PulseSbscription ,
        CASE WHEN ho.PK IS NOT NULL THEN 'Yes'
             ELSE 'No'
        END AS HasOrders ,
        b.p_ccfailurecount  
FROM    Hybris..users a
        JOIN  ##bulk_TEMPS b ON a.PK = b.userpk
        LEFT JOIN Hybris..orders pulse ON a.PK = pulse.userpk
                                          AND pulse.TypePkString = 8796124741714
                                          AND pulse.p_template = 1
                                          AND pulse.p_active = 1
        LEFT JOIN Hybris.dbo.orders ho ON ho.p_associatedtemplate = b.PK
                                          AND ho.TypePkString IN (
                                          8796124676178, 8796124708946,
                                          8796124741714 )
                                          AND ho.statuspk IN ( 8796134178907,
                                                              8796134113371,
                                                              8796093251675 )
        JOIN Hybris.dbo.composedtypes c ON c.PK = b.TypePkString
WHERE   a.p_country = 8796100624418  --US templates       
        AND b.p_template = 1
        AND b.p_active = 1
        AND b.p_schedulingdate <= GETDATE()
GROUP BY c.InternalCode ,
        b.code ,
        a.p_rfaccountid ,
        b.p_lastprocessingdate ,
        b.p_schedulingdate ,
        CASE WHEN b.p_template = 1 THEN 'Active'
             ELSE 'InActive'
        END ,
        CASE WHEN pulse.PK IS NOT NULL THEN 'Yes'
             ELSE 'No'
        END ,
        CASE WHEN ho.PK IS NOT NULL THEN 'Yes'
             ELSE 'No'
        END ,
        b.p_ccfailurecount; 
		

		
		SELECT 	COUNT(*) CountsToTerminate
		 FROM  ##bulk_TEMPS
		WHERE ( typepkstring=8796124708946 AND p_ccfailurecount>=4)
		OR   ( typepkstring IN ( 8796124676178,8796124741714 )AND p_ccfailurecount>=3)
 
 SELECT COUNT(*) CountsNotToTerminate
 FROM  ##bulk_TEMPS
		WHERE ( typepkstring=8796124708946 AND p_ccfailurecount<4)
		OR   ( typepkstring IN ( 8796124676178,8796124741714 )AND p_ccfailurecount<3)

			SELECT COUNT(DISTINCT a.code) CountsInactiveTemplates
FROM    ##bulk_TEMPS a       
WHERE   a.p_template = 1
        AND a.currencypk = 8796125855777  
        AND a.p_active <> 1
       
       
       

END 
 END 
