
USE RFOperations
GO

ALTER   PROC dbo.usp_CRjob_Templates_PostRun_Metrics
@Template NVARCHAR(10),
@ScheduledDate DATE,
@Date DATE =NULL 

AS

--DECLARE  @Template NVARCHAR(10)='CRP'
--DECLARE @ScheduledDate DATE='2015-10-11'
--DECLARE @RunDate DATE ='2015-11-06'

 
 IF OBJECT_ID('TEMPDB..##CRP_TEMPS') IS NOT NULL
    DROP TABLE ##CRP_TEMPS;

 IF OBJECT_ID('TEMPDB..##CRP_TEMP_ORDERS') IS NOT NULL
    DROP TABLE ##CRP_TEMP_OrderS;

 IF OBJECT_ID('TEMPDB..##CRPTemplatesReports') IS NOT NULL
    DROP TABLE ##CRPTemplatesReports;

 IF OBJECT_ID('TEMPDB..##Pulse_TEMPS') IS NOT NULL
    DROP TABLE ##Pulse_TEMPS;

 IF OBJECT_ID('TEMPDB..##Pulse_TEMP_ORDERS') IS NOT NULL
    DROP TABLE ##Pulse_TEMP_OrderS;

 IF OBJECT_ID('TEMPDB..##PulseTemplatesReports') IS NOT NULL
    DROP TABLE ##PulseTemplatesReports;

 IF OBJECT_ID('TEMPDB..##PCPerk_TEMPS') IS NOT NULL
    DROP TABLE ##PCPerk_TEMPS;

 IF OBJECT_ID('TEMPDB..##PCPerk_TEMP_ORDERS') IS NOT NULL
    DROP TABLE ##PCPerk_TEMP_OrderS;

 IF OBJECT_ID('TEMPDB..##PCPerkTemplatesReports') IS NOT NULL
    DROP TABLE ##PCPerkTemplatesReports;

	

 --SET @ScheduledDate = '2015-10-11'; 
 --SET @Template = 'CRP';

 DECLARE @BackupTemplateName NVARCHAR(255) ,
    @BackupOrderName NVARCHAR(255) ,
    @Sql_1 NVARCHAR(MAX) ,
    @Sql_2 NVARCHAR(MAX) ,
    @CcFailureCount NVARCHAR ,
    @TemplatesProcessed INT ,
    @TemplatesNotProcessed INT ,
    @OrdersProcessed INT,
	@RunDate Date 

	IF @Date IS NULL 
	SET @RunDate=CAST(GETDATE()AS DATE)
	IF @Date IS NOT NULL 
	SET  @Date=@RunDate
 
 IF @Template = 'CRP'
    OR @Template = 'PCPerk'
    OR @Template = 'Pulse'
    BEGIN
	BEGIN TRY 

        IF @Template = 'CRP'
            BEGIN
                SET @BackupTemplateName = CONCAT('dbo.[', @Template, '_',
                                                 @RunDate,
                                                 '_Template_', 'BackUp]'); 
 
                SET @BackupOrderName = CONCAT('dbo.[', @Template, '_',
                                             @RunDate,
                                              '_Order_', 'BackUp]'); 

                SET @Sql_1 = 'SELECT * INTO ##CRP_TEMPS FROM '
                    + @BackupTemplateName + ''; 
                EXECUTE sp_executesql @Sql_1;

                SET @Sql_2 = 'SELECT * INTO ##CRP_TEMP_orders FROM '
                    + @BackupOrderName + ''; 

                EXECUTE sp_executesql @Sql_2;

--No of Templates Processed

                SELECT  @TemplatesProcessed = COUNT(DISTINCT a.code)
                FROM    Hybris..orders a
                        JOIN Hybris..orders a1 ON a.pk = a1.p_associatedtemplate
                                                  AND ( a1.p_template = 0
                                                        OR a1.p_template IS NOT NULL
                                                      )
                                                  AND a1.typepkstring <> 8796127723602
                                                  AND CAST(a1.createdTS AS DATE) = @RunDate
                WHERE   a.code IN ( SELECT  code
                                    FROM    ##CRP_TEMPS );

--No of not Templates Processed

                SELECT  @TemplatesNotProcessed = COUNT(DISTINCT a.code)
                FROM    Hybris..orders a
                        LEFT  JOIN Hybris..orders a1 ON a.pk = a1.p_associatedtemplate
                                                        AND ( a1.p_template = 0
                                                              OR a1.p_template IS NOT NULL
                                                            )
                                                        AND a1.typepkstring <> 8796127723602
                                                        AND CAST(a1.createdTS AS DATE) = @RunDate
                WHERE   a.code IN ( SELECT  code
                                    FROM    ##CRP_TEMPS )
                        AND a1.pk IS NULL; 

--No of Orders Genereted

                SELECT  @OrdersProcessed = COUNT(DISTINCT a1.code)
                FROM    Hybris..orders a
                        JOIN Hybris..orders a1 ON a.pk = a1.p_associatedtemplate
                                                  AND ( a1.p_template = 0
                                                        OR a1.p_template IS NOT NULL
                                                      )
                                                  AND a1.typepkstring <> 8796127723602
                                                  AND CAST(a1.createdTS AS DATE) = @RunDate
                WHERE   a.code IN ( SELECT  code
                                    FROM    ##CRP_TEMPS );

                SELECT  @ScheduledDate AS Rundate ,
                        @Template AS Templatetype ,
                        @TemplatesProcessed AS Templatesprocessed ,
                        @TemplatesNotProcessed AS TemplatesNotProcessed ,
                        @OrdersProcessed AS OrdersProcessed;

--Query to find the status of orders created on the day of CJ execution

                SELECT  b.VALUE AS OrderStatus ,
                        COUNT(*) AS OrderCounts
                FROM    ##CRP_TEMPS a
                        JOIN Hybris..orders a1 --order info
                        ON a.pk = a1.p_associatedtemplate
                           AND ( a1.p_template = 0
                                 OR a1.p_template IS NOT NULL
                               )
                           AND a1.typepkstring <> 8796127723602
                           AND CAST(a1.createdTS AS DATE) = @RunDate
                        JOIN Hybris..vEnumerationValues b ON a1.statuspk = b.PK
                                                             AND [type] = 'OrderStatus'
                                             
                GROUP BY b.VALUE;




 /* AFTER RUN Metrics and Comparision */


                SELECT  a.pk ,
                        a.code ,
                        f.code AS OrderNumber ,
                        v.value AS orderStatus ,
                        CAST (a.p_lastprocessingdate AS DATE) AS LastPDate ,
                        CAST(a.p_schedulingdate AS DATE) AS nextschedulingdate ,
                        ISNULL(a.p_ccfailurecount, 0) AS p_ccfailurecount , 
                        COUNT(DISTINCT b.PK) item_count ,
                        COUNT(c.PK) swappable_prod_count ,
                        a.p_totalcv ,
                        a.p_totalqv ,
                        CAST('' AS NVARCHAR(25)) AS [status] ,
                        CAST('' AS NVARCHAR(25)) AS [DateField] ,
                        CAST('' AS NVARCHAR(255)) AS OrderNotes ,
                        CAST('' AS NVARCHAR(255)) AS Reason
                INTO    ##CRPTemplatesReports
                FROM    Hybris..orders a
                        JOIN Hybris..orderentries b ON a.PK = b.orderpk
                        JOIN Hybris..paymentinfos e ON a.PK = e.OwnerPkString
                        LEFT JOIN Hybris..orders f ON f.P_associatedTemplate = a.pk
                        LEFT JOIN Hybris..venumerationvalues v ON v.pk = f.statuspk
                        LEFT JOIN Hybris..products c ON b.productpk = c.PK
                                                        AND c.p_crpreplacementproducts IS NOT NULL
                WHERE   a.p_template = 1
                        AND a.currencypk = 8796125855777  --us
                        AND a.p_active = 1
                        AND a.TypePkString = 8796124676178 --active crp
                        AND ISNULL(f.p_template, 0) = 0
                        AND f.TypePkString <> 8796127723602
                        AND CAST(f.createdTS AS DATE) = @RunDate
                        AND a.code IN ( SELECT  code
                                        FROM    ##CRP_TEMPS )
                GROUP BY a.pk ,
                        a.code ,
                        f.code ,
                        v.value ,
                        CAST (a.p_lastprocessingdate AS DATE) ,
                        CAST(a.p_schedulingdate AS DATE) ,
                        ISNULL(a.p_ccfailurecount, 0) ,
                        a.p_totalcv ,
                        a.p_totalqv; 
/*Update Analysis*/
                UPDATE  ##CRPTemplatesReports
                SET     [status] = ( CASE WHEN a.LastPDate = @RunDate
                                               AND a.nextschedulingdate = DATEADD(MONTH,
                                                              1,
                                                              @RunDate)
                                               AND a.p_ccfailurecount = 0
                                               AND ( a.orderStatus = 'Submitted'
                                                     OR a.orderStatus = 'partially-shipped'
                                                     OR a.orderStatus = 'shipped'
                                                   ) THEN 'Pass'
                                          ELSE 'Fail'
                                     END ) ,
                        [DateField] = ( CASE WHEN a.LastPDate <> @RunDate
                                                  OR a.nextschedulingdate <> DATEADD(MONTH,
                                                              1,
                                                              @RunDate)
                                                  AND ( a.orderStatus = 'Submitted'
                                                        OR a.orderStatus = 'partially-shipped'
                                                        OR a.orderStatus = 'shipped'
                                                      ) THEN 'RequireAnalysis'
                                             WHEN ( a.orderStatus = 'cancelled'
                                                    OR a.orderStatus = 'Failed'
                                                  )
                                                  AND ( a.LastPDate <> @RunDate
                                                        OR a.nextschedulingdate >= @RunDate
                                                      ) THEN 'RequireAnalysis'
                                             ELSE ''
                                        END ) ,
                        OrderNotes = n.P_OrderNotes
                FROM    ##CRPTemplatesReports a
                        LEFT JOIN Hybris..Orders ho ON ho.p_associatedtemplate = a.pk
                        LEFT JOIN Hybris..OrderNotes n ON n.P_Order = ho.pk
                        JOIN ##CRP_TEMPS t ON t.pk = a.pk
                WHERE   ISNULL(ho.p_template, 0) = 0
                        AND ho.TypePkString <> 8796127723602
                        AND ho.currencypk = 8796125855777
                        AND CAST(ho.createdTS AS DATE) = @RunDate; 
                        
/*Unprocessed Templates Information*/
                INSERT  INTO ##CRPTemplatesReports
                        ( pk ,
                          code ,
                          Orderstatus ,
                          LastPDate ,
                          nextschedulingdate ,
                          p_ccfailurecount ,
                          item_count ,
                          swappable_prod_count ,
                          p_totalcv ,
                          p_totalqv
				        )
                        SELECT  a.pk ,
                                a.code ,
                                'Not Processed' ,
                                CAST (ISNULL(a.p_lastprocessingdate,
                                             '1900-01-01') AS DATE) AS LastPDate ,
                                CAST(a.p_schedulingdate AS DATE) AS nextschedulingdate ,
                                ISNULL(a.p_ccfailurecount, 0) AS p_ccfailurecount ,  
                                COUNT(DISTINCT b.PK) item_count ,
                                COUNT(c.PK) swappable_prod_count ,
                                a.p_totalcv ,
                                a.p_totalqv
                        FROM    Hybris..orders a
                                JOIN Hybris..orderentries b ON a.PK = b.orderpk
                                LEFT JOIN Hybris..products c ON b.productpk = c.PK
                                                              AND c.p_crpreplacementproducts IS NOT NULL
                        WHERE   a.p_template = 1
                                AND a.currencypk = 8796125855777  --us
                                AND a.p_active = 1
                                AND a.TypePkString = 8796124676178 --active crp
                                AND a.code IN ( SELECT  code
                                                FROM    ##CRP_TEMPS )
                                AND a.code NOT IN (
                                SELECT  code
                                FROM    ##CRPTemplatesReports )
                        GROUP BY a.pk ,
                                a.code ,
                                CAST (ISNULL(a.p_lastprocessingdate,
                                             '1900-01-01') AS DATE) ,
                                CAST(a.p_schedulingdate AS DATE) ,
                                ISNULL(a.p_ccfailurecount, 0) ,
                                a.p_totalcv ,
                                a.p_totalqv; 
                        

-- UPDATE Not Proccessed Templates Analysis

               
	

                UPDATE  ##CRPTemplatesReports
                SET     [status] = ( CASE WHEN a.orderstatus = 'Not Processed'
                                               AND a.p_ccfailurecount >= 3
                                               AND a.LastPDate = ISNULL(CAST(t.p_schedulingdate AS DATE),
                                                              '1900-01-01')
                                               AND a.nextschedulingdate = CAST(t.p_schedulingdate AS DATE)
                                          THEN 'Pass'
                                          WHEN a.orderstatus = 'Not Processed'
                                               AND ( a.p_ccfailurecount < 3
                                                     OR a.LastPDate <> ISNULL(CAST(t.p_schedulingdate AS DATE),
                                                              '1900-01-01')
                                                     OR a.nextschedulingdate <> CAST(t.p_schedulingdate AS DATE)
                                                   ) THEN 'fail'
                                          ELSE [status]
                                     END )
                FROM    ##CRPTemplatesReports a
                        JOIN ##CRP_TEMPS t ON t.pk = a.pk;
              





                DECLARE @CRPTemplatesReports NVARCHAR(250)= CONCAT('Hybris.dbo.[',
                                                              @Template, '_',
                                                              @RunDate,
                                                              '_CRP_CRJob_Report]'); 

                EXEC  ('select * Into '+ @CRPTemplatesReports+ ' from ##CRPTemplatesReports');

            END; 

        IF @Template = 'PULSE'
            BEGIN
                SET @BackupTemplateName = CONCAT('dbo.[', @Template, '_',
                                                 @RunDate,
                                                 '_Template_', 'BackUp]'); 
 
                SET @BackupOrderName = CONCAT('dbo.[', @Template, '_',
                                              @RunDate,
                                              '_Order_', 'BackUp]'); 

                SET @Sql_1 = 'SELECT * INTO ##PULSE_TEMPS FROM '
                    + @BackupTemplateName + ''; 
                EXECUTE sp_executesql @Sql_1;

                SET @Sql_2 = 'SELECT * INTO ##PULSE_TEMP_orders FROM '
                    + @BackupOrderName + ''; 

                EXECUTE sp_executesql @Sql_2;

--No of Templates Processed

                SELECT  @TemplatesProcessed = COUNT(DISTINCT a.code)
                FROM    Hybris..orders a
                        JOIN Hybris..orders a1 ON a.pk = a1.p_associatedtemplate
                                                  AND ( a1.p_template = 0
                                                        OR a1.p_template IS NOT NULL
                                                      )
                                                  AND a1.typepkstring <> 8796127723602
                                                  AND CAST(a1.createdTS AS DATE) = @RunDate
                WHERE   a.code IN ( SELECT  code
                                    FROM    ##PULSE_TEMPS );

--No of not Templates Processed

                SELECT  @TemplatesNotProcessed = COUNT(DISTINCT a.code)
                FROM    Hybris..orders a
                        LEFT  JOIN Hybris..orders a1 ON a.pk = a1.p_associatedtemplate
                                                        AND ( a1.p_template = 0
                                                              OR a1.p_template IS NOT NULL
                                                            )
                                                        AND a1.typepkstring <> 8796127723602
                                                        AND CAST(a1.createdTS AS DATE) = @RunDate
                WHERE   a.code IN ( SELECT  code
                                    FROM    ##PULSE_TEMPS )
                        AND a1.pk IS NULL; 

--No of Orders Genereted

                SELECT  @OrdersProcessed = COUNT(DISTINCT a1.code)
                FROM    Hybris..orders a
                        JOIN Hybris..orders a1 ON a.pk = a1.p_associatedtemplate
                                                  AND ( a1.p_template = 0
                                                        OR a1.p_template IS NOT NULL
                                                      )
                                                  AND a1.typepkstring <> 8796127723602
                                                  AND CAST(a1.createdTS AS DATE) = @RunDate
                WHERE   a.code IN ( SELECT  code
                                    FROM    ##PULSE_TEMPS );

                SELECT  @ScheduledDate AS Rundate ,
                        @Template AS Templatetype ,
                        @TemplatesProcessed AS Templatesprocessed ,
                        @TemplatesNotProcessed AS TemplatesNotProcessed ,
                        @OrdersProcessed AS OrdersProcessed;

--Query to find the status of orders created on the day of CJ execution

                SELECT  b.VALUE AS OrderStatus ,
                        COUNT(*) AS OrderCounts
                FROM    ##PULSE_TEMPS a
                        JOIN Hybris..orders a1 --order info
                        ON a.pk = a1.p_associatedtemplate
                           AND ( a1.p_template = 0
                                 OR a1.p_template IS NOT NULL
                               )
                           AND a1.typepkstring <> 8796127723602
                           AND CAST(a1.createdTS AS DATE) = @RunDate
                        JOIN Hybris..vEnumerationValues b ON a1.statuspk = b.PK
                                                             AND [type] = 'OrderStatus'
                                             --AND b.value = ''SUBMITTED''
                GROUP BY b.VALUE;




 /* AFTER RUN Metrics and Comparision */


                SELECT  a.pk ,
                        a.code ,
                        f.code AS OrderNumber ,
                        v.value AS orderStatus ,
                        CAST (a.p_lastprocessingdate AS DATE) AS LastPDate ,
                        CAST(a.p_schedulingdate AS DATE) AS nextschedulingdate ,
                        ISNULL(a.p_ccfailurecount, 0) AS p_ccfailurecount ,  
--substring(totaltaxvalues, 1, charindex('|', totaltaxvalues)) taxvalues,
                        COUNT(DISTINCT b.PK) item_count ,
                        COUNT(c.PK) swappable_prod_count ,
                        a.p_totalcv ,
                        a.p_totalqv ,
                        CAST('' AS NVARCHAR(25)) AS [status] ,
                        CAST('' AS NVARCHAR(25)) AS [DateField] ,
                        CAST('' AS NVARCHAR(255)) AS OrderNotes ,
                        CAST('' AS NVARCHAR(255)) AS Reason
                INTO    ##PULSETemplatesReports
                FROM    Hybris..orders a
                        JOIN Hybris..orderentries b ON a.PK = b.orderpk
                        JOIN Hybris..paymentinfos e ON a.PK = e.OwnerPkString
                        LEFT JOIN Hybris..orders f ON f.P_associatedTemplate = a.pk
                        LEFT JOIN Hybris..venumerationvalues v ON v.pk = f.statuspk
                        LEFT JOIN Hybris..products c ON b.productpk = c.PK
                                                        AND c.p_crpreplacementproducts IS NOT NULL
                WHERE   a.p_template = 1
                        AND a.currencypk = 8796125855777  --us
                        AND a.p_active = 1
                        AND a.TypePkString = 8796124676178 --active crp
                        AND ISNULL(f.p_template, 0) = 0
                        AND f.TypePkString <> 8796127723602
                        AND CAST(f.createdTS AS DATE) = @RunDate
                        AND a.code IN ( SELECT  code
                                        FROM    ##PULSE_TEMPS )
                GROUP BY a.pk ,
                        a.code ,
                        f.code ,
                        v.value ,
                        CAST (a.p_lastprocessingdate AS DATE) ,
                        CAST(a.p_schedulingdate AS DATE) ,
                        ISNULL(a.p_ccfailurecount, 0) ,
                        a.p_totalcv ,
                        a.p_totalqv; 

                UPDATE  ##PULSETemplatesReports
                SET     [status] = ( CASE WHEN a.LastPDate = @RunDate
                                               AND a.nextschedulingdate = DATEADD(MONTH,
                                                              1,
                                                              @RunDate)
                                               AND a.p_ccfailurecount = 0
                                               AND ( a.orderStatus = 'Submitted'
                                                     OR a.orderStatus = 'partially-shipped'
                                                     OR a.orderStatus = 'shipped'
                                                   ) THEN 'Pass'
                                          ELSE 'Fail'
                                     END ) ,
                        [DateField] = ( CASE WHEN a.LastPDate <> @RunDate
                                                  OR a.nextschedulingdate <> DATEADD(MONTH,
                                                              1,
                                                             @RunDate)
                                                  AND ( a.orderStatus = 'Submitted'
                                                        OR a.orderStatus = 'partially-shipped'
                                                        OR a.orderStatus = 'shipped'
                                                      ) THEN 'RequireAnalysis'
                                             WHEN ( a.orderStatus = 'cancelled'
                                                    OR a.orderStatus = 'Failed'
                                                  )
                                                  AND ( a.LastPDate <> @RunDate
                                                        OR a.nextschedulingdate <> t.p_schedulingdate
                                                      ) THEN 'RequireAnalysis'
                                             ELSE ''
                                        END ) ,
                        OrderNotes = n.P_OrderNotes
                FROM    ##PULSETemplatesReports a
                        LEFT JOIN Hybris..Orders ho ON ho.p_associatedtemplate = a.pk
                        LEFT JOIN Hybris..OrderNotes n ON n.P_Order = ho.pk
                        JOIN ##PULSE_TEMPS t ON t.pk = a.pk
                WHERE   ISNULL(ho.p_template, 0) = 0
                        AND ho.TypePkString <> 8796127723602
                        AND ho.currencypk = 8796125855777
                        AND CAST(ho.createdTS AS DATE) = @RunDate; 
                        
/*Unprocessed Templates Information*/
                INSERT  INTO ##PULSETemplatesReports
                        ( pk ,
                          code ,
                          Orderstatus ,
                          LastPDate ,
                          nextschedulingdate ,
                          p_ccfailurecount ,
                          item_count ,
                          swappable_prod_count ,
                          p_totalcv ,
                          p_totalqv
				        )
                        SELECT  a.pk ,
                                a.code ,
                                'Not Processed' ,
                                CAST (ISNULL(a.p_lastprocessingdate,
                                             '1900-01-01') AS DATE) AS LastPDate ,
                                CAST(a.p_schedulingdate AS DATE) AS nextschedulingdate ,
                                ISNULL(a.p_ccfailurecount, 0) AS p_ccfailurecount ,  
--substring(totaltaxvalues, 1, charindex('|', totaltaxvalues)) taxvalues,
                                COUNT(DISTINCT b.PK) item_count ,
                                COUNT(c.PK) swappable_prod_count ,
                                a.p_totalcv ,
                                a.p_totalqv
                        FROM    Hybris..orders a
                                JOIN Hybris..orderentries b ON a.PK = b.orderpk
                                LEFT JOIN Hybris..products c ON b.productpk = c.PK
                                                              AND c.p_crpreplacementproducts IS NOT NULL
                        WHERE   a.p_template = 1
                                AND a.currencypk = 8796125855777  --us
                                AND a.p_active = 1
                                AND a.TypePkString = 8796124676178 --active crp
                                AND a.code IN ( SELECT  code
                                                FROM    ##PULSE_TEMPS )
                                AND a.code NOT IN (
                                SELECT  code
                                FROM    ##PULSETemplatesReports )
                        GROUP BY a.pk ,
                                a.code ,
                                CAST (ISNULL(a.p_lastprocessingdate,
                                             '1900-01-01') AS DATE) ,
                                CAST(a.p_schedulingdate AS DATE) ,
                                ISNULL(a.p_ccfailurecount, 0) ,
                                a.p_totalcv ,
                                a.p_totalqv; 
                        

-- UPDATE analysis

               
		--AND A.PK = 8804028645421

                UPDATE  ##PULSETemplatesReports
                SET     [status] = ( CASE WHEN a.orderstatus = 'Not Processed'
                                               AND a.p_ccfailurecount >= 3
                                               AND a.LastPDate = ISNULL(CAST(t.p_schedulingdate AS DATE),
                                                              '1900-01-01')
                                               AND a.nextschedulingdate = CAST(t.p_schedulingdate AS DATE)
                                          THEN 'Pass'
                                          WHEN a.orderstatus = 'Not Processed'
                                               AND ( a.p_ccfailurecount < 3
                                                     OR a.LastPDate <> ISNULL(CAST(t.p_schedulingdate AS DATE),
                                                              '1900-01-01')
                                                     OR a.nextschedulingdate <> CAST(t.p_schedulingdate AS DATE)
                                                   ) THEN 'fail'
                                          ELSE [status]
                                     END )
                FROM    ##PULSETemplatesReports a
                        JOIN ##PULSE_TEMPS t ON t.pk = a.pk;
              





                DECLARE @PULSETemplatesReports NVARCHAR(250)= CONCAT('Hybris.dbo.[',
                                                              @Template, '_',
                                                              @RunDate,
                                                              '_PULSE_CRJob_Report]'); 

                EXEC  ('select * Into '+ @PULSETemplatesReports+ ' from ##PULSETemplatesReports');

            END; 



        IF @Template = 'PCPerk'
            BEGIN
                SET @BackupTemplateName = CONCAT('dbo.[', @Template, '_',
                                                @RunDate,
                                                 '_Template_', 'BackUp]'); 
 
                SET @BackupOrderName = CONCAT('dbo.[', @Template, '_',
                                              @RunDate,
                                              '_Order_', 'BackUp]'); 

                SET @Sql_1 = 'SELECT * INTO ##PCPerk_TEMPS FROM '
                    + @BackupTemplateName + ''; 
                EXECUTE sp_executesql @Sql_1;

                SET @Sql_2 = 'SELECT * INTO ##PCPerk_TEMP_orders FROM '
                    + @BackupOrderName + ''; 

                EXECUTE sp_executesql @Sql_2;

--No of Templates Processed

                SELECT  @TemplatesProcessed = COUNT(DISTINCT a.code)
                FROM    Hybris..orders a
                        JOIN Hybris..orders a1 ON a.pk = a1.p_associatedtemplate
                                                  AND ( a1.p_template = 0
                                                        OR a1.p_template IS NOT NULL
                                                      )
                                                  AND a1.typepkstring <> 8796127723602
                                                  AND CAST(a1.createdTS AS DATE) = @RunDate
                WHERE   a.code IN ( SELECT  code
                                    FROM    ##PCPerk_TEMPS );

--No of not Templates Processed

                SELECT  @TemplatesNotProcessed = COUNT(DISTINCT a.code)
                FROM    Hybris..orders a
                        LEFT  JOIN Hybris..orders a1 ON a.pk = a1.p_associatedtemplate
                                                        AND ( a1.p_template = 0
                                                              OR a1.p_template IS NOT NULL
                                                            )
                                                        AND a1.typepkstring <> 8796127723602
                                                        AND CAST(a1.createdTS AS DATE) = @RunDate
                WHERE   a.code IN ( SELECT  code
                                    FROM    ##PCPerk_TEMPS )
                        AND a1.pk IS NULL; 

--No of Orders Genereted

                SELECT  @OrdersProcessed = COUNT(DISTINCT a1.code)
                FROM    Hybris..orders a
                        JOIN Hybris..orders a1 ON a.pk = a1.p_associatedtemplate
                                                  AND ( a1.p_template = 0
                                                        OR a1.p_template IS NOT NULL
                                                      )
                                                  AND a1.typepkstring <> 8796127723602
                                                  AND CAST(a1.createdTS AS DATE) = @RunDate
                WHERE   a.code IN ( SELECT  code
                                    FROM    ##PCPerk_TEMPS );

                SELECT  @ScheduledDate AS Rundate ,
                        @Template AS Templatetype ,
                        @TemplatesProcessed AS Templatesprocessed ,
                        @TemplatesNotProcessed AS TemplatesNotProcessed ,
                        @OrdersProcessed AS OrdersProcessed;

--Query to find the status of orders created on the day of CJ execution

                SELECT  b.VALUE AS OrderStatus ,
                        COUNT(*) AS OrderCounts
                FROM    ##PCPerk_TEMPS a
                        JOIN Hybris..orders a1 --order info
                        ON a.pk = a1.p_associatedtemplate
                           AND ( a1.p_template = 0
                                 OR a1.p_template IS NOT NULL
                               )
                           AND a1.typepkstring <> 8796127723602
                           AND CAST(a1.createdTS AS DATE) = @RunDate
                        JOIN Hybris..vEnumerationValues b ON a1.statuspk = b.PK
                                                             AND [type] = 'OrderStatus'
                                             --AND b.value = ''SUBMITTED''
                GROUP BY b.VALUE;




 /* AFTER RUN Metrics and Comparision */


                SELECT  a.pk ,
                        a.code ,
                        f.code AS OrderNumber ,
                        v.value AS orderStatus ,
                        CAST (a.p_lastprocessingdate AS DATE) AS LastPDate ,
                        CAST(a.p_schedulingdate AS DATE) AS nextschedulingdate ,
                        ISNULL(a.p_ccfailurecount, 0) AS p_ccfailurecount ,  
--substring(totaltaxvalues, 1, charindex('|', totaltaxvalues)) taxvalues,
                        COUNT(DISTINCT b.PK) item_count ,
                        COUNT(c.PK) swappable_prod_count ,
                        a.p_totalcv ,
                        a.p_totalqv ,
                        CAST('' AS NVARCHAR(25)) AS [status] ,
                        CAST('' AS NVARCHAR(25)) AS [DateField] ,
                        CAST('' AS NVARCHAR(255)) AS OrderNotes ,
                        CAST('' AS NVARCHAR(255)) AS Reason
                INTO    ##PCPerkTemplatesReports
                FROM    Hybris..orders a
                        JOIN Hybris..orderentries b ON a.PK = b.orderpk
                        JOIN Hybris..paymentinfos e ON a.PK = e.OwnerPkString
                        LEFT JOIN Hybris..orders f ON f.P_associatedTemplate = a.pk
                        LEFT JOIN Hybris..venumerationvalues v ON v.pk = f.statuspk
                        LEFT JOIN Hybris..products c ON b.productpk = c.PK
                                                        AND c.p_pcperkreplacementproducts IS NOT NULL
                WHERE   a.p_template = 1
                        AND a.currencypk = 8796125855777  --us
                        AND a.p_active = 1
                        AND a.TypePkString = 8796124676178 --active crp
                        AND ISNULL(f.p_template, 0) = 0
                        AND f.TypePkString <> 8796127723602
                        AND CAST(f.createdTS AS DATE) = @RunDate
                        AND a.code IN ( SELECT  code
                                        FROM    ##PCPerk_TEMPS )
                GROUP BY a.pk ,
                        a.code ,
                        f.code ,
                        v.value ,
                        CAST (a.p_lastprocessingdate AS DATE) ,
                        CAST(a.p_schedulingdate AS DATE) ,
                        ISNULL(a.p_ccfailurecount, 0) ,
                        a.p_totalcv ,
                        a.p_totalqv; 

                UPDATE  ##PCPerkTemplatesReports
                SET     [status] = ( CASE WHEN a.LastPDate = @RunDate
                                               AND a.nextschedulingdate = DATEADD(MONTH,
                                                              1,
                                                              @RunDate)
                                               AND a.p_ccfailurecount = 0
                                               AND ( a.orderStatus = 'Submitted'
                                                     OR a.orderStatus = 'partially-shipped'
                                                     OR a.orderStatus = 'shipped'
                                                   ) THEN 'Pass'
                                          ELSE 'Fail'
                                     END ) ,
                        [DateField] = ( CASE WHEN a.LastPDate <> @RunDate
                                                  OR a.nextschedulingdate <> DATEADD(MONTH,
                                                              1,
                                                             @RunDate)
                                                  AND ( a.orderStatus = 'Submitted'
                                                        OR a.orderStatus = 'partially-shipped'
                                                        OR a.orderStatus = 'shipped'
                                                      ) THEN 'RequireAnalysis'
                                             WHEN ( a.orderStatus = 'cancelled'
                                                    OR a.orderStatus = 'Failed'
                                                  )
                                                  AND ( a.LastPDate <> @RunDate
                                                        OR a.nextschedulingdate >= @RunDate
                                                      ) THEN 'RequireAnalysis'
                                             ELSE ''
                                        END ) ,
                        OrderNotes = n.P_OrderNotes
                FROM    ##PCPerkTemplatesReports a
                        LEFT JOIN Hybris..Orders ho ON ho.p_associatedtemplate = a.pk
                        LEFT JOIN Hybris..OrderNotes n ON n.P_Order = ho.pk
                        JOIN ##PCPerk_TEMPS t ON t.pk = a.pk
                WHERE   ISNULL(ho.p_template, 0) = 0
                        AND ho.TypePkString <> 8796127723602
                        AND ho.currencypk = 8796125855777
                        AND CAST(ho.createdTS AS DATE) = @RunDate; 
                        
/*Unprocessed Templates Information*/
                INSERT  INTO ##PCPerkTemplatesReports
                        ( pk ,
                          code ,
                          Orderstatus ,
                          LastPDate ,
                          nextschedulingdate ,
                          p_ccfailurecount ,
                          item_count ,
                          swappable_prod_count ,
                          p_totalcv ,
                          p_totalqv
				        )
                        SELECT  a.pk ,
                                a.code ,
                                'Not Processed' ,
                                CAST (ISNULL(a.p_lastprocessingdate,
                                             '1900-01-01') AS DATE) AS LastPDate ,
                                CAST(a.p_schedulingdate AS DATE) AS nextschedulingdate ,
                                ISNULL(a.p_ccfailurecount, 0) AS p_ccfailurecount ,  
--substring(totaltaxvalues, 1, charindex('|', totaltaxvalues)) taxvalues,
                                COUNT(DISTINCT b.PK) item_count ,
                                COUNT(c.PK) swappable_prod_count ,
                                a.p_totalcv ,
                                a.p_totalqv
                        FROM    Hybris..orders a
                                JOIN Hybris..orderentries b ON a.PK = b.orderpk
                                LEFT JOIN Hybris..products c ON b.productpk = c.PK
                                                              AND c.p_pcperkreplacementproducts IS NOT NULL
                        WHERE   a.p_template = 1
                                AND a.currencypk = 8796125855777  --us
                                AND a.p_active = 1
                                AND a.TypePkString = 8796124676178 --active crp
                                AND a.code IN ( SELECT  code
                                                FROM    ##PCPerk_TEMPS )
                                AND a.code NOT IN (
                                SELECT  code
                                FROM    ##PCPerkTemplatesReports )
                        GROUP BY a.pk ,
                                a.code ,
                                CAST (ISNULL(a.p_lastprocessingdate,
                                             '1900-01-01') AS DATE) ,
                                CAST(a.p_schedulingdate AS DATE) ,
                                ISNULL(a.p_ccfailurecount, 0) ,
                                a.p_totalcv ,
                                a.p_totalqv; 
                        

-- UPDATE analysis

               
		--AND A.PK = 8804028645421

                UPDATE  ##PCPerkTemplatesReports
                SET     [status] = ( CASE WHEN a.orderstatus = 'Not Processed'
                                               AND a.p_ccfailurecount >= 3
                                               AND a.LastPDate = ISNULL(CAST(t.p_schedulingdate AS DATE),
                                                              '1900-01-01')
                                               AND a.nextschedulingdate = CAST(t.p_schedulingdate AS DATE)
                                          THEN 'Pass'
                                          WHEN a.orderstatus = 'Not Processed'
                                               AND ( a.p_ccfailurecount < 3
                                                     OR a.LastPDate <> ISNULL(CAST(t.p_schedulingdate AS DATE),
                                                              '1900-01-01')
                                                     OR a.nextschedulingdate <> CAST(t.p_schedulingdate AS DATE)
                                                   ) THEN 'fail'
                                          ELSE [status]
                                     END )
                FROM    ##PCPerkTemplatesReports a
                        JOIN ##PCPerk_TEMPS t ON t.pk = a.pk;
              





                DECLARE @PCPerkTemplatesReports NVARCHAR(250)= CONCAT('Hybris.dbo.[',
                                                              @Template, '_',
                                                             @RunDate,
                                                              '_PCPerk_CRJob_Report]'); 

                EXEC  ('select * Into '+ @PCPerkTemplatesReports+ ' from ##PCPerkTemplatesReports');

            END;
			
			 END TRY
        BEGIN CATCH
            RAISERROR('Data Set Up Has Failed',16,1);
            SELECT  ERROR_PROCEDURE() AS [SP];
            SELECT  ERROR_MESSAGE() AS [ERROR MESSAGE]; 
            SELECT  ERROR_LINE() AS [ERROR LINE];
        END CATCH; 


    END; 


