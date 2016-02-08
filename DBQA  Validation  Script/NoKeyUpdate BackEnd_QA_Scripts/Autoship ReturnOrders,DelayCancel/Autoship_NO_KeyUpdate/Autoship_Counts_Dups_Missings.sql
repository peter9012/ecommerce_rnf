
-- AUTOSHIP HEADER COUNTS ,DUPLICATES AND MISSING.

 --					SELECT  * FROM    DataMigration.dbo.ExecResult;
    --				TRUNCATE TABLE DataMigration.dbo.ExecResult;


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
--		RunDate DATE)
		--SELECT * FROM DataMigration.dbo.ExecResult

DECLARE @StartedTime TIME;
DECLARE @EndTime TIME; 

SELECT  'Autoship header' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);




SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Templates  Found'
             ELSE 'No duplicates Templates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    COUNT(*) cnt ,
                    ho.code AS autoshipnumber
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
          WHERE     c.isocode = 'US'
                    AND ho.p_template = 1
          GROUP BY  ho.code
          HAVING    COUNT(*) > 1
        ) t1;
		-- time taken 3 sec


		
/* BUSINESS RELES VALIDATION */      

SELECT CASE WHEN COUNT(t.pk)>0 THEN 'Templates Not Having AssociatedPaymentInfo'
ELSE 'AssociatedPaymentInfo Check Passed'
END AS Results FROM (
SELECT TOP 1 Pk FROM hybris..orders WHERE paymentinfopk IS NULL OR paymentaddresspk IS NULL  
UNION 
SELECT TOP 1  pk FROM Hybris..orders WHERE pk NOT IN (SELECT OwnerPkString FROM Hybris..paymentinfos
WHERE p_sourcename='Hybris-DM' AND duplicate=1 ))t


--      --Counts check on Hybris side for US
--IF OBJECT_ID('tempdb..#DuplicateAutoship') IS NOT NULL
--    DROP TABLE #DuplicateAutoship;


SELECT  CASE WHEN COUNT(1) > 1
             THEN 'Accounts with duplicate Active templates found'
             WHEN COUNT(1) = 0
             THEN 'No Accounts with duplicate templates found'
        END AS CheckDuplicateActiveTemplates
FROM    ( SELECT    AccountID ,
                    a.AutoshipTypeID ,
                    COUNT(*) AS Counts
          FROM      RFOperations.Hybris.Autoship a
                    JOIN Hybris..users u ON CAST(a.AccountID AS VARCHAR) = u.p_rfaccountid
          WHERE     CountryID = 236
                    AND Active = 1
          GROUP BY  AccountID ,
                    a.AutoshipTypeID
          HAVING    COUNT(*) > 1
        ) A;
                                     --timetaken=3sec
		      
----Loading duplicate Active autoships into temp table

--SELECT  AccountID ,
--        a.AutoshipTypeID
--INTO    #DuplicateAutoship
--FROM    RFOperations.Hybris.Autoship a
--        JOIN Hybris..users u ON CAST(a.AccountID AS VARCHAR) = u.p_rfaccountid
--WHERE   CountryID = 236
--        AND Active = 1
--GROUP BY AccountID ,
--        a.AutoshipTypeID
--HAVING  COUNT(*) > 1

--timetaken: 9 sec.

IF OBJECT_ID('tempdb..#LoadedAutoshipID') IS NOT NULL
    DROP TABLE #LoadedAutoshipID;

SELECT    DISTINCT
        a.AutoshipID
INTO    #LoadedAutoshipID          --1461695 Templates with timetaken 32 sec	Dry:1501895 templates with time:25 sec
FROM    RFOperations.Hybris.Autoship (NOLOCK) a
        INNER JOIN RodanFieldsLive.dbo.AutoshipOrders ao ON ao.TemplateOrderID = a.AutoshipID
                                                            AND ao.AccountID = a.AccountID
        INNER JOIN RFOperations.Hybris.AutoshipItem (NOLOCK) ai ON ai.AutoshipId = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipPayment (NOLOCK) ap ON ap.AutoshipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipShipment (NOLOCK) ash ON ash.AutoshipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipPaymentAddress (NOLOCK) apa ON apa.AutoShipID = a.AutoshipID
        INNER JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) asha ON asha.AutoShipID = a.AutoshipID
        INNER JOIN Hybris.dbo.users u ON CAST(a.AccountID AS NVARCHAR) = u.p_rfaccountid
                                         AND u.p_sourcename = 'Hybris-DM'
WHERE   a.CountryID = 236;
     -- AND a.AutoshipID NOT IN ( SELECT    AutoshipID FROM      #DuplicateAutoship );

CREATE CLUSTERED INDEX cls_autoship ON #LoadedAutoshipID(AutoshipID);	
SELECT COUNT(*) LoadedCount FROM #LoadedAutoshipID					  

IF OBJECT_ID('tempdb..#extra') IS NOT NULL
    DROP TABLE #extra;

SELECT  ho.code ,
        c.modifiedTS
INTO    #extra                        --131 Templates with timetaken 2 sec. Dry:97 Templates 2 Sec.
FROM    Hybris..orders ho
        JOIN Hybris..users u ON u.PK = ho.userpk
                                AND ho.p_template = 1
                                AND u.p_sourcename = 'Hybris-DM'
        JOIN Hybris..countries c ON c.PK = u.p_country
                                    AND c.isocode = 'US'
        LEFT JOIN #LoadedAutoshipID lo ON lo.AutoshipID = ho.code
WHERE   lo.AutoshipID IS NULL; 


SELECT  COUNT(*) AS [ExtraCountsLoaded IN Hybris]
FROM    #extra;


IF OBJECT_ID('tempdb..#missing') IS NOT NULL
    DROP TABLE #missing;

SELECT  lo.AutoshipID
INTO    #missing                      --  Templates with timetaken 2 sec. Dry:0 Templates 2 Sec.
FROM    Hybris..orders ho
        JOIN Hybris..users u ON u.PK = ho.userpk
                                AND ho.p_template = 1
                                AND u.p_sourcename = 'Hybris-DM'
                                AND ho.p_template = 1
        JOIN Hybris..countries c ON c.PK = u.p_country
                                    AND c.isocode = 'US'
        RIGHT JOIN #LoadedAutoshipID lo ON lo.AutoshipID = ho.code
WHERE   ho.code IS NULL; 


SELECT  COUNT(*) AS [NotLoaded IN Hybris]
FROM    #missing;
		

SELECT  hybris_cnt ,
        rfo_cnt ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(ho.PK) hybris_cnt                        --1461694 
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                               AND u.p_country = 8796100624418
                                               AND ho.p_template = 1
                                               AND p_sourcename = 'Hybris-DM'
                                               AND code NOT IN ( SELECT
                                                              code
                                                              FROM
                                                              #extra )
        ) t1 ,
        ( SELECT    COUNT(DISTINCT a.AutoshipID) rfo_cnt           --1461695
          FROM      RFOperations.Hybris.Autoship (NOLOCK) a
                    JOIN #LoadedAutoshipID b ON a.AutoshipID = b.AutoshipID
                                                AND a.AutoshipID NOT IN (
                                                SELECT  AutoshipID
                                                FROM    #missing )
          WHERE     a.CountryID = 236
        ) t2;
                                             -- timetaken 5 sec.



											 

 /*															STG3
 Execution Stats:
  Time Taken:														25						
  RFO Counts:														1501895										
  Hybris Counts:													1501895								
  Diff :																0											
  Comments:																97 Extras													
   
 
 */



--IF OBJECT_ID('tempdb..#missing') IS NOT NULL
--    DROP TABLE #missing;


--SELECT  t1.code ,
--        t2.AutoshipID ,
--        CASE WHEN t1.code IS NULL THEN 'Missing in Hybris'
--             WHEN t2.AutoshipID IS NULL THEN 'Missing in RFO'
--        END Results
--INTO    #missing
--FROM    ( SELECT    a.code
--          FROM      Hybris.dbo.orders a ,
--                    Hybris.dbo.users b ,
--                    Hybris.dbo.countries c
--          WHERE     a.userpk = b.PK
--                    AND b.p_country = c.PK
--                    AND c.isocode = 'US'
--                    AND a.p_template = 1
--                    AND p_sourcename = 'Hybris-DM'
--        ) t1
--        FULL OUTER JOIN ( SELECT    a.AutoshipID
--                          FROM      RFOperations.Hybris.Autoship (NOLOCK) a
--                                    JOIN #LoadedAutoshipID b ON a.AutoshipID = b.AutoshipID
--                          WHERE     a.CountryID = 236
--                        ) t2 ON t1.code = t2.AutoshipID
--WHERE   t1.code IS NULL
--        OR t2.AutoshipID IS NULL;
--									-- Time taken 4 sec



SELECT  'Autoship header' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,              
        'Autoship Header' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship Header' AS Entity ,
                'Counts,Dups,Missing' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;




--***********************************************************************




--DECLARE @StartedTime TIME,
--@EndTime TIME 

SELECT  'Autoship Items' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);

--- AUTOSHIPITEM COUNTS, DUPS AND MISSING 

--Autoship Entries 

--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    COUNT(*) cnt ,
                    d.orderpk ,
                    d.productpk ,
                    d.[entrynumber]
          FROM      Hybris.dbo.orders a ,
                    Hybris.dbo.users b ,
                    Hybris.dbo.countries c ,
                    Hybris.dbo.orderentries d
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND a.PK = d.orderpk
                    AND c.isocode = 'US'
                    AND p_sourcename = 'Hybris-DM'
                    AND a.p_template = 1 --AS
GROUP BY            d.orderpk ,
                    d.productpk ,
                    d.[entrynumber]
          HAVING    COUNT(*) > 1
        ) t1;



--Counts check on Hybris side for US
SELECT  hybris_cnt ,
        rfo_cnt ,
        t1.hybris_cnt - t2.rfo_cnt AS diff ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(DISTINCT oi.PK) hybris_cnt
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON u.PK = ho.userpk
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN Hybris.dbo.countries c ON u.p_country = c.PK
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.orderentries oi ON oi.orderpk = ho.PK
                                                       AND ho.p_template = 1
          WHERE     ho.code NOT IN ( SELECT code
                                     FROM   #extra )   --Excluding Extra added Templates in Hybris.
        ) t1 , --1710792
        ( SELECT    COUNT(c.AutoshipItemID) rfo_cnt
          FROM      RFOperations.Hybris.Autoship a ,
                    Hybris.dbo.users b ,
                    RFOperations.Hybris.AutoshipItem c ,
                    #LoadedAutoshipID d
          WHERE     a.AccountID = b.p_rfaccountid
                    AND a.AutoshipID = c.AutoshipId
                    AND a.AutoshipID = d.AutoshipID
                    AND CountryID = 236
                    AND p_sourcename = 'Hybris-DM'
                    AND a.AutoshipID NOT IN ( SELECT    code
                                              FROM      #extra )
											-- Excluding Missing Templates in Hybris.
        ) t2;
 --1712145


 
--Counts check on Hybris side for US
SELECT  t1.code ,
        t2.AutoshipID ,
        t1.hybris_cnt ,
        t2.rfo_cnt
FROM    ( SELECT    COUNT(DISTINCT oi.PK) hybris_cnt ,
                    ho.code
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON u.PK = ho.userpk
                                               AND u.p_sourcename = 'Hybris-DM'
                                               AND ho.p_template = 1
                    JOIN Hybris.dbo.countries c ON u.p_country = c.PK
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.orderentries oi ON oi.orderpk = ho.PK
          WHERE     ho.code NOT IN ( SELECT code
                                     FROM   #extra )   --Excluding Extra added Templates in Hybris.
          GROUP BY  ho.code
        ) t1
        JOIN --1710792
        ( SELECT    COUNT(c.AutoshipItemID) rfo_cnt ,
                    a.AutoshipID
          FROM      RFOperations.Hybris.Autoship a ,
                    Hybris.dbo.users b ,
                    RFOperations.Hybris.AutoshipItem c ,
                    #LoadedAutoshipID d
          WHERE     a.AccountID = b.p_rfaccountid
                    AND a.AutoshipID = c.AutoshipId
                    AND a.AutoshipID = d.AutoshipID
                    AND CountryID = 236
                    AND p_sourcename = 'Hybris-DM'
                    AND a.AutoshipID NOT IN ( SELECT    code
                                              FROM      #extra )
          GROUP BY  a.AutoshipID
											-- Excluding Missing Templates in Hybris.
        ) t2 ON t1.code = t2.AutoshipID
WHERE   t1.hybris_cnt <> t2.rfo_cnt;
 --1712145



 /*															STG3
 Execution Stats:
  Time Taken:														5						
  RFO Counts:														2692575										
  Hybris Counts:													2692575								
  Diff :																0										
  Comments:																+Hybris												
   
 
 */

 
SELECT  'Autoship items' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);


SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
       DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,             
        'Autoship Items' AS Entity; 

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship Items' AS Entity ,
		'Counts,Dups,Missing' AS Types,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate; 


       

--*******************************AutoshipPaymentInfos***************	



--DECLARE @StartedTime TIME,
--@EndTime TIME 

SELECT  'Autoship PaymentInfos' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);

--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    COUNT(a.OwnerPkString) cnt --, a.ownerpkstring 
                    ,
                    a.OwnerPkString ,
                    a.code
          FROM      Hybris.dbo.paymentinfos (NOLOCK) a ,
                    Hybris.dbo.users b ,
                    Hybris.dbo.countries c ,
                    Hybris.dbo.orders (NOLOCK) d
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND d.PK = a.OwnerPkString
                    AND c.isocode = 'US'
                    AND b.p_sourcename = 'Hybris-DM'
                    AND d.p_template = 1
                    AND a.duplicate = 1 --AS/AS profile
                    AND a.p_sourcename = 'Hybris-DM'
          GROUP BY  a.OwnerPkString ,
                    a.code
          HAVING    COUNT(*) > 1
        ) t1;

----Counts check on Hybris side for US
SELECT  hybris_cnt ,
        rfo_cnt ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(DISTINCT hpi.PK) hybris_cnt
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON u.PK = ho.userpk
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                                        AND ho.p_template = 1
                                                        AND hpi.duplicate = 1
														AND hpi.p_sourcename='Hybris-DM'
          WHERE     ho.code NOT IN ( SELECT code
                                     FROM   #extra ) --EXcluding Extra Templates added in Hybris.         
        ) t1 , --1093729  -1501195
        ( SELECT    COUNT(DISTINCT asp.AutoshipPaymentID) rfo_cnt
          FROM      RFOperations.Hybris.Autoship a
                    JOIN #LoadedAutoshipID l ON a.AutoshipID = l.AutoshipID
                                                AND a.CountryID = 236
                    JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN RFOperations.Hybris.AutoshipPayment asp ON asp.AutoshipID = a.AutoshipID
                    JOIN RodanFieldsLive.dbo.OrderPayments lop ON lop.OrderPaymentID = asp.AutoshipPaymentID
          WHERE     a.AutoshipID NOT IN ( SELECT    code
                                          FROM      #extra
                                          WHERE     code IS NOT NULL )--Excluding Missing in Hybris.
        ) t2;
--986577   --1501196

	--timetaken =1min and 29 sec 


	SELECT      hpi.PK 
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON u.PK = ho.userpk
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                                        AND ho.p_template = 1
                                                        AND hpi.duplicate = 1
														AND hpi.p_sourcename='Hybris-DM'
          WHERE     ho.code NOT IN ( SELECT code
                                     FROM   #extra ) --EXcluding Extra Templates added in Hybris.         
      EXCEPT
        ( SELECT    asp.AutoshipPaymentID 
          FROM      RFOperations.Hybris.Autoship a
                    JOIN #LoadedAutoshipID l ON a.AutoshipID = l.AutoshipID
                                                AND a.CountryID = 236
                    JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN RFOperations.Hybris.AutoshipPayment asp ON asp.AutoshipID = a.AutoshipID
                    JOIN RodanFieldsLive.dbo.OrderPayments lop ON lop.OrderPaymentID = asp.AutoshipPaymentID
          WHERE     a.AutoshipID NOT IN ( SELECT    code
                                          FROM      #extra
                                          WHERE     code IS NOT NULL )--Excluding Missing in Hybris.

										  )


 /*															STG3
 Execution Stats:
  Time Taken:													6 Sec							
  RFO Counts:												1501196												
  Hybris Counts:										1501195														
  Diff :													1														
  Comments:																													
   
 
 */

--Checking the mismatch back in RFL		

SELECT  CASE WHEN ISNULL(b.CNT,0) > 0 THEN 'Payments having issue'
             ELSE 'No Issues - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    COUNT(*) CNT
          FROM      RodanFieldsLive..OrderPayments
          WHERE     OrderPaymentID IN (
                    SELECT  CAST(c.AutoshipPaymentID AS NVARCHAR)
                    FROM    RFOperations.Hybris.Autoship a ,
                            RFOperations.Hybris.AutoshipItem b ,
                            RFOperations.Hybris.AutoshipPayment c ,
                            RFOperations.Hybris.AutoshipPaymentAddress d ,
                            RFOperations.Hybris.AutoshipShipment e ,
                            RFOperations.Hybris.AutoshipShippingAddress f ,
                            Hybris.dbo.users g ,
                            RodanFieldsLive..OrderPayments h
                    WHERE   a.AutoshipID = b.AutoshipId
                            AND a.AutoshipID = c.AutoshipID
                            AND a.AutoshipID = d.AutoShipID
                            AND a.AutoshipID = e.AutoshipID
                            AND a.AutoshipID = f.AutoShipID
                            AND a.AccountID = g.p_rfaccountid
                            AND c.AutoshipPaymentID = h.OrderPaymentID
                            AND a.CountryID = 236
                            AND p_sourcename = 'Hybris-DM'
                    EXCEPT
                    SELECT  d.code
                    FROM    Hybris.dbo.orders a ,
                            Hybris.dbo.users b ,
                            Hybris.dbo.countries c ,
                            Hybris.dbo.paymentinfos d
                    WHERE   a.userpk = b.PK
                            AND b.p_country = c.PK
                            AND a.PK = d.OwnerPkString
                            AND c.isocode = 'US'
                            AND a.p_template = 1
                            AND d.duplicate = 1
                            AND b.p_sourcename = 'Hybris-DM' )
                    AND OrderID IN ( SELECT code
                                     FROM   Hybris..orders
                                     WHERE  p_template = 1 )
                    AND AccountNumber <> 'HDCm5F9HLZ6JyWpnoVViLw=='
                    AND ( LTRIM(RTRIM(BillingFirstName)) <> ''
                          OR LTRIM(RTRIM(BillingLastName)) <> ''
                     )
        ) b;

		---Missing In hybris= PaymentId 8841933
		
----Counts check on Hybris side for US
SELECT  t1.code ,
        t2.AutoshipID ,
        t1.hybris_cnt ,
        t2.rfo_cnt
FROM    ( SELECT    COUNT(hpi.PK) hybris_cnt ,
                    ho.code
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON u.PK = ho.userpk
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                                        AND ho.p_template = 1
                                                        AND hpi.duplicate = 1
          WHERE     ho.code NOT IN ( SELECT code
                                     FROM   #extra ) --EXcluding Extra Templates added in Hybris. 
          GROUP BY  ho.code
        ) t1 --1093729
        JOIN ( SELECT   COUNT(asp.AutoshipPaymentID) rfo_cnt ,
                        a.AutoshipID
               FROM     RFOperations.Hybris.Autoship a
                        JOIN #LoadedAutoshipID l ON a.AutoshipID = l.AutoshipID
                                                    AND a.CountryID = 236
                        JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
                                                   AND u.p_sourcename = 'Hybris-DM'
                        JOIN RFOperations.Hybris.AutoshipPayment asp ON asp.AutoshipID = a.AutoshipID
                        JOIN RodanFieldsLive.dbo.OrderPayments lop ON lop.OrderPaymentID = asp.AutoshipPaymentID
               WHERE    a.AutoshipID NOT IN ( SELECT    code
                                              FROM      #extra
                                              WHERE     code IS NOT NULL )--Excluding Missing in Hybris.
               GROUP BY a.AutoshipID
             ) t2 ---986577
        ON t1.code = t2.AutoshipID
WHERE   t1.hybris_cnt <> t2.rfo_cnt;




SELECT  'Autoship paymentInfos' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
     DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,               
        'Autoship PaymentInfos' AS Entity; 

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship PaymentInfos' AS Entity ,
		'Counts,Dups,Missing' AS Types,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
               DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate; 
			  
	
	--*******************************AutoshipPaymentAddress*************


	
--DECLARE @StartedTime TIME,
--@EndTime TIME 

SELECT  'Autoship PaymentAddress' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);

	
--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    OwnerPkString
          FROM      Hybris.dbo.addresses
          WHERE     duplicate = 1
                    AND OwnerPkString IN (
                    SELECT DISTINCT
                            AutoshipID
                    FROM    RFOperations.Hybris.Autoship
                    WHERE   CountryID = 236 )
                    AND p_billingaddress = 1
          GROUP BY  OwnerPkString
          HAVING    COUNT(*) > 1
        ) t1;


		
SELECT  hybris_cnt ,
        rfo_cnt ,
        t1.hybris_cnt - t2.rfo_cnt AS diff ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(DISTINCT ad.PK) hybris_cnt
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                               AND u.p_sourcename = 'Hybris-DM'
                                               AND ho.p_template = 1
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos pa ON pa.OwnerPkString = ho.PK
                                                       AND pa.duplicate = 1
                                                       AND ho.p_template = 1
                  --  JOIN Hybris.dbo.addresses ad ON ad.OwnerPkString = pa.PK   AND ad.duplicate = 1 AND pa.p_sourcename='Hybris-DM'
                    JOIN Hybris.dbo.addresses ad ON ad.PK = pa.p_billingaddress
                                                    AND pa.duplicate = 1
                                                    AND ad.duplicate = 1
                                                    AND pa.p_sourcename = 'Hybris-DM'
                                                    AND ad.p_billingaddress = 1
          WHERE     ho.code NOT IN ( SELECT code
                                     FROM   #extra )
        ) t1 , --1135025
        ( SELECT    COUNT(DISTINCT apa.AutoshipPaymentAddressID) rfo_cnt
          FROM      RFOperations.Hybris.Autoship a
                    JOIN #LoadedAutoshipID l ON l.AutoshipID = a.AutoshipID
                                                AND a.CountryID = 236
                    JOIN RFOperations.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = a.AutoshipID
                    JOIN Hybris.dbo.users c ON c.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
                    JOIN RFOperations.Hybris.AutoshipPayment ap ON ap.AutoshipID = a.AutoshipID
                                                              AND apa.AutoshipPaymentAddressID = ap.AutoshipPaymentID
                    JOIN Hybris..paymentinfos hpa ON hpa.code = CAST(ap.AutoshipPaymentID AS NVARCHAR)
                                                     AND hpa.duplicate = 1
          WHERE     a.AutoshipID NOT IN ( SELECT    code
                                          FROM      #extra )
        ) t2; 	
		

SELECT  t1.* ,
        t2.*
FROM    ( SELECT    COUNT(DISTINCT ad.PK) hybris_cnt ,
                    pa.code
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                               AND u.p_sourcename = 'Hybris-DM'
                                               AND ho.p_template = 1
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos pa ON pa.OwnerPkString = ho.PK
                                                       AND pa.duplicate = 1
                    JOIN Hybris.dbo.addresses ad ON ad.OwnerPkString = pa.PK
                                                    AND ad.duplicate = 1
		--  JOIN      Hybris.dbo.addresses ad ON ad.pk=pa.p_billingaddress AND ad.duplicate=1
          WHERE     ho.code NOT IN ( SELECT code
                                     FROM   #extra )
          GROUP BY  pa.code
        ) t1 --1135025
        JOIN ( SELECT   COUNT(DISTINCT apa.AutoshipPaymentAddressID) rfo_cnt ,
                        ap.AutoshipPaymentID
               FROM     RFOperations.Hybris.Autoship a
                        JOIN #LoadedAutoshipID l ON l.AutoshipID = a.AutoshipID
                                                    AND a.CountryID = 236
                        JOIN RFOperations.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = a.AutoshipID
                        JOIN Hybris.dbo.users c ON c.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
                        JOIN RFOperations.Hybris.AutoshipPayment ap ON ap.AutoshipID = a.AutoshipID
                                                              AND ap.AutoshipPaymentID = apa.AutoshipPaymentAddressID
                        JOIN Hybris..paymentinfos hpa ON hpa.code = CAST(ap.AutoshipPaymentID AS NVARCHAR)
                                                         AND hpa.duplicate = 1
               GROUP BY ap.AutoshipPaymentID
             ) t2 ON t1.code = t2.AutoshipPaymentID
WHERE   t1.hybris_cnt <> t2.rfo_cnt;	
		


 /*															STG3
 Execution Stats:
  Time Taken:												9								
  RFO Counts:												1501195												
  Hybris Counts:											1501195													
  Diff :													0														
  Comments:																													
   
 
 */
		
SELECT  t1.code ,
        t2.AutoshipPaymentID ,
        CASE WHEN t1.code IS NULL THEN 'Missing in AutoshipPayment in  Hybris'
             WHEN t2.AutoshipPaymentID IS NULL
             THEN 'Missing AutoshipPayment in RFO'
        END AS Results
FROM    ( SELECT    pa.code
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                               AND u.p_sourcename = 'Hybris-DM'
                                               AND ho.p_template = 1
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos pa ON pa.OwnerPkString = ho.PK
                                                       AND pa.duplicate = 1
                                                       AND pa.p_sourcename = 'Hybris-DM'
                    JOIN Hybris.dbo.addresses ad ON ad.OwnerPkString = pa.PK
                                                    AND ad.duplicate = 1
                                                    AND ad.p_billingaddress = 1
		--  JOIN      Hybris.dbo.addresses ad ON ad.pk=pa.p_billingaddress AND ad.duplicate=1 AND ad.p_billingaddress=1
          WHERE     ho.code NOT IN ( SELECT code
                                     FROM   #extra )
        ) t1
        FULL OUTER JOIN ( SELECT    DISTINCT
                                    CAST(ap.AutoshipPaymentID AS NVARCHAR) AutoshipPaymentID
                          FROM      RFOperations.Hybris.Autoship a
                                    JOIN #LoadedAutoshipID l ON l.AutoshipID = a.AutoshipID
                                                              AND a.CountryID = 236
                                    JOIN RFOperations.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = a.AutoshipID
                                    JOIN Hybris.dbo.users c ON c.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
                                    JOIN RFOperations.Hybris.AutoshipPayment ap ON ap.AutoshipID = a.AutoshipID
                                    JOIN Hybris..paymentinfos hpa ON hpa.code = CAST(ap.AutoshipPaymentID AS NVARCHAR)
                                                              AND hpa.duplicate = 1
                        ) t2 ON t1.code = t2.AutoshipPaymentID
WHERE   t1.code IS NULL
        OR t2.AutoshipPaymentID IS NULL; 


		
SELECT  t1.code ,
        t2.AutoshipID ,
        t1.hybris_cnt ,
        t2.rfo_cnt
FROM    ( SELECT    COUNT(DISTINCT ad.PK) hybris_cnt ,
                    ho.code
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                               AND u.p_sourcename = 'Hybris-DM'
                                               AND ho.p_template = 1
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos pa ON pa.OwnerPkString = ho.PK
                                                       AND pa.duplicate = 1
                                                       AND pa.p_sourcename = 'Hybris-DM'
                    JOIN Hybris.dbo.addresses ad ON ad.OwnerPkString = pa.PK
                                                    AND ad.duplicate = 1
		--  JOIN      Hybris.dbo.addresses ad ON ad.pk=pa.p_billingaddress AND ad.duplicate=1
          WHERE     ho.code NOT IN ( SELECT code
                                     FROM   #extra )
          GROUP BY  ho.code
        ) t1 --1135025
        JOIN ( SELECT   COUNT(DISTINCT apa.AutoshipPaymentAddressID) rfo_cnt ,
                        a.AutoshipID
               FROM     RFOperations.Hybris.Autoship a
                        JOIN #LoadedAutoshipID l ON l.AutoshipID = a.AutoshipID
                                                    AND a.CountryID = 236
                        JOIN RFOperations.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = a.AutoshipID
                        JOIN Hybris.dbo.users c ON c.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
                        JOIN RFOperations.Hybris.AutoshipPayment ap ON ap.AutoshipID = a.AutoshipID
                        JOIN Hybris..paymentinfos hpa ON hpa.code = CAST(ap.AutoshipPaymentID AS NVARCHAR)
                                                         AND hpa.duplicate = 1
               WHERE    a.AutoshipID NOT IN ( SELECT    AutoshipID
                                              FROM      #missing )  --To Avoid Missing in Hybris.
               GROUP BY a.AutoshipID
             ) t2 ON t1.code = t2.AutoshipID
WHERE   t1.hybris_cnt <> t2.rfo_cnt;




SELECT  'Autoship payment Address ' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
         DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,                
        'Autoship PaymentAddress' AS Entity; 


INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship payment Address' AS Entity ,
		'Counts,Dups,Missing' AS Types,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate; 
				
		--****************************AutoshipShippingAddress****************


				
--DECLARE @StartedTime TIME,
--@EndTime TIME 

SELECT  'Autoship Shipping Address' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);
		
--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    OwnerPkString
          FROM      Hybris.dbo.addresses(NOLOCK)
          WHERE     duplicate = 1
                    AND OwnerPkString IN (
                    SELECT DISTINCT
                            AutoshipID
                    FROM    RFOperations.Hybris.Autoship
                    WHERE   CountryID = 236 )
                    AND p_shippingaddress = 1
          GROUP BY  OwnerPkString
          HAVING    COUNT(*) > 1
        ) t1;

--Counts check on Hybris side for US
WITH    cte
          AS ( SELECT   * ,
                        ROW_NUMBER() OVER ( PARTITION BY AutoShipID ORDER BY Address1 DESC ) AS rn
               FROM     RFOperations.Hybris.AutoshipShippingAddress
             )
    SELECT  hybris_cnt ,
            rfo_cnt ,
            t1.hybris_cnt - t2.rfo_cnt AS diff ,
            CASE WHEN hybris_cnt > rfo_cnt
                 THEN 'Hybris count more than RFO count'
                 WHEN rfo_cnt > hybris_cnt
                 THEN 'RFO count more than Hybris count'
                 ELSE 'Count matches - validation passed'
            END Results
    FROM    ( SELECT    COUNT(DISTINCT had.PK) hybris_cnt
              FROM      Hybris.dbo.orders (NOLOCK) ho
                        JOIN Hybris.dbo.users (NOLOCK) u ON ho.userpk = u.PK
                                                            AND u.p_country = 8796100624418
                                                            AND u.p_sourcename = 'Hybris-DM'
                        JOIN Hybris.dbo.addresses (NOLOCK) had ON had.OwnerPkString = ho.PK
                        JOIN #LoadedAutoshipID lo ON lo.AutoshipID = ho.code
              WHERE     ho.p_template = 1
                        AND had.p_shippingaddress = 1
                        AND duplicate = 1
						AND ho.code NOT IN (SELECT code FROM #extra)
            ) t1 , --1502046
            ( SELECT    COUNT(DISTINCT c.AutoshipShippingAddressID) rfo_cnt
              FROM      RFOperations.Hybris.Autoship (NOLOCK) a
                        JOIN Hybris.dbo.users (NOLOCK) u ON u.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
                                                            AND u.p_sourcename = 'Hybris-DM'
                                                            AND a.CountryID = 236
                        JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) c ON c.AutoShipID = a.AutoshipID
                        JOIN #LoadedAutoshipID d ON d.AutoshipID = a.AutoshipID
                        JOIN cte e ON e.AutoshipShippingAddressID = e.AutoshipShippingAddressID
                                      AND e.rn = 1
									  AND a.AutoshipID NOT IN (SELECT AutoshipID FROM #missing)
            ) t2;
 --1502047


 


 /*															STG3
 Execution Stats:
  Time Taken:																				
  RFO Counts:													1501895										
  Hybris Counts:												1502047												
  Diff :															152												
  Comments:															+Hybris														
   
 
 */

 
--Counts check on Hybris side for US
WITH    cte
          AS ( SELECT   * ,
                        ROW_NUMBER() OVER ( PARTITION BY AutoShipID ORDER BY Address1 DESC ) AS rn
               FROM     RFOperations.Hybris.AutoshipShippingAddress
             )
    SELECT  t1.code ,
            t2.AutoshipID ,
            t1.hybris_cnt ,
            t2.rfo_cnt
    FROM    ( SELECT    COUNT(DISTINCT had.PK) hybris_cnt ,
                        ho.code
              FROM      Hybris.dbo.orders (NOLOCK) ho
                        JOIN Hybris.dbo.users (NOLOCK) u ON ho.userpk = u.PK
                                                            AND u.p_country = 8796100624418
                                                            AND u.p_sourcename = 'Hybris-DM'
                        JOIN Hybris.dbo.addresses (NOLOCK) had ON had.OwnerPkString = ho.PK
                        JOIN #LoadedAutoshipID lo ON lo.AutoshipID = ho.code
              WHERE     ho.p_template = 1
                        AND had.p_shippingaddress = 1
                        AND duplicate = 1
						AND ho.code NOT IN (SELECT code FROM #extra)
              GROUP BY  ho.code
            ) t1
            JOIN ( SELECT   COUNT(DISTINCT c.AutoshipShippingAddressID) rfo_cnt ,
                            a.AutoshipID
                   FROM      RFOperations.Hybris.Autoship (NOLOCK) a
                        JOIN Hybris.dbo.users (NOLOCK) u ON u.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
                                                            AND u.p_sourcename = 'Hybris-DM'
                                                            AND a.CountryID = 236
                        JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) c ON c.AutoShipID = a.AutoshipID
                        JOIN #LoadedAutoshipID d ON d.AutoshipID = a.AutoshipID
                        JOIN cte e ON e.AutoshipShippingAddressID = e.AutoshipShippingAddressID
                                      AND e.rn = 1
									  AND a.AutoshipID NOT IN (SELECT AutoshipID FROM #missing)
                   GROUP BY a.AutoshipID
                 ) t2 ON t1.code = t2.AutoshipID
    WHERE   t1.hybris_cnt <> t2.rfo_cnt;
 

 
SELECT  'Autoship shipping Address' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,              
        'Autoship Shipping Address' AS Entity; 


INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Autoship shipping Address' AS Entity ,
		'Counts,Dups,Missing' AS Types,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;
				  

				  


								
						
			
				
SET @StartedTime = ( SELECT TOP 1
                            StartedTime
                     FROM   DataMigration.dbo.ExecResult
                     WHERE  Entity = 'Autoship Header'
                            AND Types = 'Counts,Dups,Missing'
                     ORDER BY RunDate DESC ,
                            StartedTime DESC
                   );


SET @EndTime = ( SELECT TOP 1
                        CompletionTime
                 FROM   DataMigration.dbo.ExecResult
                 WHERE  Entity = 'Autoship shipping Address'
                        AND Types = 'Counts,Dups,Missing'
                 ORDER BY RunDate DESC ,
                        StartedTime DESC
               );
 	
INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'All Entities Related to Autoship' AS Entity ,
                'Counts,Dups,Missing' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                 DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;         
				