
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

SELECT  'Return Header ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);
	
	
	
SELECT  'Count Comparison between ReturnOrder & ReturnRequest Started' AS ValidationTypes ,
        CAST(GETDATE() AS TIME) AS Startedtime;
	
		 SELECT   COUNT (a.PK) RetunNumberNotInReturnRequest FROM (SELECT (ho.PK)
                              FROM      Hybris.dbo.orders (NOLOCK) ho
                                        JOIN Hybris.dbo.users (NOLOCK) u ON u.PK = ho.userpk
                                                              AND u.p_country = 8796100624418
                                                              AND u.p_sourcename = 'Hybris-DM'
                              WHERE     ISNULL(ho.p_template, 0) = 0
                                        AND ho.TypePkString = 8796127723602 --Returns
                              EXCEPT
                              SELECT    p_returnorder
                              FROM      Hybris..returnrequest(NOLOCK)
                              WHERE     p_returnorder IN (
                                        SELECT  ho.PK
                                        FROM    Hybris.dbo.orders (NOLOCK) ho
                                                JOIN Hybris.dbo.users (NOLOCK) u ON u.PK = ho.userpk
                                                              AND u.p_country = 8796100624418
                                                              AND u.p_sourcename = 'Hybris-DM'
                                        WHERE   ISNULL(ho.p_template, 0) = 0
                                                AND ho.TypePkString = 8796127723602))a
										
                 




 SELECT  COUNT(*) CountsOfDuplicateReturnNumber
                    FROM    Hybris.dbo.orders (NOLOCK) a ,
                            Hybris.dbo.users (NOLOCK) b ,
                            Hybris.dbo.countries (NOLOCK) c
                    WHERE   a.userpk = b.PK
                            AND b.p_country = c.PK
                            AND c.isocode = 'US'
                            AND a.p_template IS NULL
                            AND a.TypePkString = 8796127723602
                    GROUP BY a.code
                    HAVING  COUNT(*) > 1
                 

     SELECT  COUNT(*) CountsofDuplicatesReturnRequest
                    FROM    Hybris.dbo.orders (NOLOCK) a ,
                            Hybris.dbo.users (NOLOCK) b ,
                            Hybris.dbo.countries (NOLOCK) c ,
                            Hybris.dbo.returnrequest (NOLOCK) d
                    WHERE   a.userpk = b.PK
                            AND b.p_country = c.PK
                            AND a.PK = d.p_returnorder
                            AND c.isocode = 'US'
                            AND a.p_template IS NULL
                            AND a.TypePkString = 8796127723602
                    GROUP BY p_returnorder ,
                            p_order
                    HAVING  COUNT(*) > 1
                



DECLARE @ModifiedDate DATE= '2016-01-04';


SELECT  hybris_cnt ,
        rfo_cnt ,
        t1.hybris_cnt - t2.rfo_cnt AS Diff ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(ho.PK) hybris_cnt
          FROM      Hybris.dbo.orders (NOLOCK) ho
                    JOIN Hybris.dbo.users (NOLOCK) u ON u.PK = ho.userpk
                                                        AND u.p_country = 8796100624418
                                                        AND u.p_sourcename = 'Hybris-DM'
          WHERE     ISNULL(ho.p_template, 0) = 0
                    AND ho.TypePkString = 8796127723602 --Returns
                    --AND CAST(ho.modifiedTS AS DATE) = @ModifiedDate
        ) t1 , --179902
        ( SELECT    COUNT(ro.ReturnOrderID) rfo_cnt
          FROM      Hybris.ReturnOrder (NOLOCK) ro
                    JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderNumber = rfl.OrderID
                                                           AND rfl.OrderTypeID = 9
                    JOIN Hybris.dbo.orders (NOLOCK) ho ON ro.OrderID = ho.Pk
                    JOIN Hybris.dbo.users (NOLOCK) u ON u.PK = ho.userpk
          WHERE 
		  ---Commented, No Issues after CleanUp.   
		   --ro.ReturnOrderNumber NOT IN (
     --               SELECT  a.ReturnOrderNumber
     --               FROM    Hybris.ReturnOrder (NOLOCK) a
     --                       JOIN RodanFieldsLive.dbo.Orders rfl ON a.ReturnOrderNumber = CAST(rfl.OrderID AS NVARCHAR)
     --                                                         AND rfl.OrderTypeID = 9
     --                       JOIN Hybris.Orders (NOLOCK) b ON a.ReturnOrderNumber = b.OrderNumber
     --                                                        AND a.CountryID = 236 )
     --               AND
					-- ro.ReturnOrderID IN ( SELECT  ReturnOrderID  FROM    Hybris.ReturnItem (NOLOCK) ) AND
					--Excluding Items not having return.
                    ro.CountryID = 236
                    AND p_sourcename = 'Hybris-DM'
                    AND ro.ReturnStatusID = 5
        ) t2;
		  --179824
		  --179845
IF OBJECT_ID('Tempdb..#missing') IS NOT NULL
    DROP TABLE #missing;

SELECT  a.ReturnOrderID ,
        b.pk ,
        CASE WHEN b.pk IS NULL THEN 'Destination'
             WHEN a.ReturnOrderID IS NULL THEN 'Source'
        END AS MissingFrom
INTO    #missing
FROM    ( SELECT    ro.ReturnOrderID
          FROM      Hybris.ReturnOrder (NOLOCK) ro
                    JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderNumber = rfl.OrderID
                                                           AND rfl.OrderTypeID = 9
                    JOIN Hybris.dbo.orders (NOLOCK) ho ON ro.OrderID = ho.pk
                    JOIN Hybris.dbo.users (NOLOCK) u ON u.PK = ho.userpk
          WHERE     
		  --ro.ReturnOrderNumber NOT IN (
    --                SELECT  a.ReturnOrderNumber
    --                FROM    Hybris.ReturnOrder (NOLOCK) a
    --                        JOIN RodanFieldsLive.dbo.Orders rfl ON a.ReturnorderNumber = rfl.OrderID
    --                                                          AND rfl.OrderTypeID = 9
    --                        JOIN Hybris.Orders (NOLOCK) b ON a.ReturnOrderNumber = b.OrderNumber
    --                                                         AND a.CountryID = 236 )
                   -- AND ro.ReturnOrderID IN ( SELECT  ReturnOrderID FROM    Hybris.ReturnItem (NOLOCK) ) AND 
                   ro.CountryID = 236
                    AND p_sourcename = 'Hybris-DM'
                    AND ro.ReturnStatusID = 5
        ) a
        FULL OUTER JOIN ( SELECT    ho.pk
                          FROM      Hybris.dbo.orders (NOLOCK) ho
                                    JOIN Hybris.dbo.users (NOLOCK) u ON u.PK = ho.userpk
                                                              AND u.p_country = 8796100624418
                                                              AND u.p_sourcename = 'Hybris-DM'
                          WHERE     ISNULL(ho.p_template, 0) = 0
                                    AND ho.TypePkString = 8796127723602 --Returns
                                   --AND CAST(ho.modifiedTS AS DATE) = @ModifiedDate
                        ) b ON a.ReturnOrderID = b.pk
WHERE   ( b.pk IS NULL
          OR a.ReturnOrderID IS NULL
        );  


		SELECT COUNT(*),MissingFrom
		FROM #missing 
		GROUP BY missingFrom





		/* Checking Key been Updated In RFO properly .*/

			SELECT  COUNT(ho.PK) [Counts of ReturnOrder Key Updted in RFO]
			FROM    Hybris.dbo.orders (NOLOCK) ho
					JOIN Hybris.dbo.users (NOLOCK) u ON u.PK = ho.userpk
														AND u.p_country = 8796100624418
														AND u.p_sourcename = 'Hybris-DM'
			WHERE   EXISTS ( SELECT 1
							 FROM   RFOperations.Hybris.ReturnOrder ro
							 WHERE  ro.ReturnOrderID = ho.PK )
					AND ISNULL(ho.p_template, 0) = 0
					AND ho.TypePkString = 8796127723602; --Returns




SELECT  'Return  header WithKey' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return Header' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Return Header WithKey' AS Entity ,
                'Counts,Dups,Missing' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;

				
				
				/* Return Entries */

--DECLARE @StartedTime TIME;
--DECLARE @EndTime TIME; 

		
SELECT  'Return Entries ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);
	
				
--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    COUNT(*) cnt ,
                    e.p_returnrequest ,
                    e.p_orderentry
          FROM      Hybris.dbo.orders a ,
                    Hybris.dbo.users b ,
                    Hybris.dbo.countries c ,
                    Hybris.dbo.returnrequest d ,
                    Hybris.dbo.returnentry e
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND a.PK = d.p_returnorder
                    AND d.PK = e.p_returnrequest
                    AND c.isocode = 'US'
                    AND p_sourcename = 'Hybris-DM'
                    AND a.p_template IS NULL
                    AND a.TypePkString = 8796127723602
          GROUP BY  e.p_returnrequest ,
                    e.p_orderentry
          HAVING    COUNT(*) > 1
        ) t1;

--Counts check between ReturnRequest & ReturnEntry
SELECT  CASE WHEN COUNT(pk) > 0
             THEN 'Count Comparison between ReturnRequest & ReturnEntry - Failed!'
             ELSE 'Count Comparison between ReturnRequest & ReturnEntry - Passed'
        END Results
FROM    ( SELECT    d.PK
          FROM      Hybris.dbo.orders a ,
                    Hybris.dbo.users b ,
                    Hybris.dbo.countries c ,
                    Hybris.dbo.returnrequest d
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND c.isocode = 'US'
                    AND a.PK = d.p_returnorder
                    AND a.p_template IS NULL
                    AND a.TypePkString = 8796127723602 --Returns
                    AND p_sourcename = 'Hybris-DM'
          EXCEPT
          SELECT    p_returnrequest
          FROM      Hybris..returnentry
          WHERE     p_returnrequest IN (
                    SELECT  d.PK
                    FROM    Hybris.dbo.orders a ,
                            Hybris.dbo.users b ,
                            Hybris.dbo.countries c ,
                            Hybris.dbo.returnrequest d
                    WHERE   a.userpk = b.PK
                            AND b.p_country = c.PK
                            AND c.isocode = 'US'
                            AND a.PK = d.p_returnorder
                            AND a.p_template IS NULL
                            AND a.TypePkString = 8796127723602 --Returns
                            AND p_sourcename = 'Hybris-DM' )
        ) t1;

--Counts check on Hybris side for OrderEntries US
SELECT  hybris_cnt ,
        RFO_CNT ,
        t1.hybris_cnt - t2.RFO_CNT AS Diff ,
        CASE WHEN hybris_cnt > RFO_CNT THEN 'Hybris count more than RFO count'
             WHEN RFO_CNT > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(d.PK) hybris_cnt
          FROM      Hybris.dbo.orders (NOLOCK) ho
                    JOIN Hybris.dbo.users (NOLOCK) u ON u.PK = ho.userpk
                                                        AND u.p_country = 8796100624418
                                                        AND u.p_sourcename = 'Hybris-DM'
                    JOIN Hybris..orderentries d ON d.orderpk = ho.PK
          WHERE     ISNULL(ho.p_template, 0) = 0
                    AND ho.TypePkString = 8796127723602 --Returns
                   -- AND CAST(ho.modifiedTS AS DATE) = @ModifiedDate
                    AND ho.pk NOT IN ( SELECT pk
                                         FROM   #missing
                                         WHERE  MissingFrom = 'Source' )
        ) t1 , --106994
        ( SELECT    COUNT(ri.ReturnItemID) RFO_CNT
        FROM      Hybris.ReturnOrder ro
                    JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderNumber= rfl.OrderID
                                                           AND rfl.OrderTypeID = 9 
					JOIN  Hybris.dbo.orders ho ON ho.pk=ro.returnOrderId
					JOIN  Hybris.dbo.users u ON u.pk=ho.userpk
					join  Hybris.ReturnItem ri ON ri.ReturnOrderId=ro.ReturnOrderId AND ri.orderItemId IS NOT NULL 
					JOIN  Hybris.dbo.products p ON p.p_rflegacyproductid=ri.productId
          WHERE     p_catalog = '8796093088344'
                    AND p_catalogversion = '8796093153881'
                    --AND a.ReturnOrderNumber NOT IN (
                    --SELECT  a.ReturnOrderNumber
                    --FROM    Hybris.ReturnOrder a
                    --        JOIN Hybris.Orders b ON a.ReturnOrderNumber = b.OrderNumber
                    --                                AND a.CountryID = 236 ) --CleanUP fixed this Issues
                    AND ro.ReturnOrderID NOT IN (  SELECT  ReturnOrderID FROM    #missing WHERE   MissingFrom = 'Destination' )
                    AND ho.pk  NOT IN ( SELECT  pk    FROM    #missing  WHERE   MissingFrom = 'Source' )
                    AND ro.CountryID = 236
                    AND p_sourcename = 'Hybris-DM'
        ) t2;
  --107014

  /* previous Code having Multiple line Items */

  --SELECT    COUNT(*) RFO_CNT
  --        FROM      ( SELECT    MAX(ReturnItemID) id ,
  --                              d.ReturnOrderID ,
  --                              ProductID ,
  --                              OrderItemID
  --                    FROM      Hybris.ReturnOrder a
  --                              JOIN RodanFieldsLive.dbo.Orders rfl ON a.ReturnOrderID = rfl.OrderID
  --                                                            AND rfl.OrderTypeID = 9 ,
  --                              Hybris.dbo.orders b ,
  --                              Hybris.dbo.users c ,
  --                              Hybris.ReturnItem d ,
  --                              Hybris.dbo.products e
		--						--,Hybris.dbo.orders r
  --                    WHERE     a.ReturnOrderID = b.code
  --                              AND b.userpk = c.PK
  --                              AND a.ReturnOrderID = d.ReturnOrderID
  --                              AND d.ProductID = e.p_rflegacyproductid
		--						---AND a.OrderID=r.code
  --                              AND p_catalog = '8796093088344'
  --                              AND p_catalogversion = '8796093153881'
  --                              --AND a.ReturnOrderNumber NOT IN (
  --                              --SELECT  a.ReturnOrderNumber
  --                              --FROM    Hybris.ReturnOrder a
  --                              --        JOIN Hybris.Orders b ON a.ReturnOrderNumber = b.OrderNumber
  --                              --                              AND a.CountryID = 236 )
  --                              --AND a.ReturnOrderNumber <> '11030155' --AS no same as Return no
  --                              AND CountryId = 236
  --                              AND p_sourcename = 'Hybris-DM'
  --                    GROUP BY  d.ReturnOrderID ,
  --                              ProductID ,
  --                              OrderItemID
  --                  ) a

				
		
		
		/* Checking Key been Uddated properly with the counts */
		
         SELECT COUNT(d.PK) [Counts RFO ReturnItem  KeyUpdated]
         FROM   Hybris.dbo.orders (NOLOCK) ho
                JOIN Hybris.dbo.users (NOLOCK) u ON u.PK = ho.userpk
                                                    AND u.p_country = 8796100624418
                                                    AND u.p_sourcename = 'Hybris-DM'
                JOIN Hybris..orderentries d ON d.orderpk = ho.PK
         WHERE EXISTS (SELECT 1 FROM RFOperations.Hybris.ReturnItem ri WHERE ri.ReturnItemID=d.pk)
		 AND  ISNULL(ho.p_template, 0) = 0
                AND ho.TypePkString = 8796127723602 --Returns             
                AND ho.PK NOT IN ( SELECT   PK FROM     #missing WHERE    MissingFrom = 'Source' );		



SELECT  'Return  Entries' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        USER_NAME() AS UserName ,
        'Return Entries' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Return Entries WithKey' AS Entity ,
                'Counts,Dups,Missing' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;


				/* Return PaymentInfos*/



--DECLARE @StartedTime TIME;
--DECLARE @EndTime TIME; 

		
SELECT  'Return PaymentInfos ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);

		--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    COUNT(*) DupCount
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris..users u ON u.PK = ho.userpk
                                            AND u.p_sourcename = 'Hybris-DM'
                                            AND u.p_country = 8796100624418
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
          WHERE     hpi.duplicate = 1
                    AND ho.p_template IS NULL
                    AND ho.TypePkString = 8796127723602
          GROUP BY  hpi.OwnerPkString ,
                    hpi.code
          HAVING    COUNT(*) > 1
        ) t3;

			--Counts check on Hybris side for US
SELECT  hybris_cnt ,
        rfo_cnt ,
        t1.hybris_cnt - t2.rfo_cnt AS Diff ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(hpi.Pk) hybris_cnt
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris..users u ON u.PK = ho.userpk
                                            AND u.p_sourcename = 'Hybris-DM'
                                            AND u.p_country = 8796100624418
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
          WHERE     hpi.duplicate = 1
                    AND ISNULL(ho.p_template, 0) = 0
                    AND ho.TypePkString = 8796127723602
                   -- AND CAST(ho.modifiedTS AS DATE) = @ModifiedDate
                    AND ho.Pk NOT IN ( SELECT Pk  FROM   #missing  WHERE  MissingFrom = 'Source' )
					 AND ho.Pk NOT IN (  SELECT  ReturnOrderID FROM    #missing WHERE   MissingFrom = 'Destination' )
        ) t1 , --119320
        ( SELECT    COUNT(rp.ReturnPaymentId) rfo_cnt
          FROM      RFOperations.Hybris.ReturnOrder ro
                    JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderNumber = rfl.OrderID
                                                           AND rfl.OrderTypeID = 9
                    JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
                                               AND ro.CountryID = 236
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID 
                    JOIN Hybris..orders ho ON ho.PK = ro.ReturnOrderID
                                              AND ho.TypePkString = 8796127723602
                    --JOIN RodanFieldsLive.dbo.OrderPayments rrp ON rrp.OrderPaymentID = rp.ReturnPaymentId
          WHERE      LEN(rp.ReturnPaymentId)>11  AND --Excluding Non Key Updated
		 rp.PaymenttypeId IS NOT NULL AND 
		   --( LTRIM(RTRIM(rrp.BillingFirstName)) <> ''
     --                 OR LTRIM(RTRIM(rrp.BillingLastName)) <> ''
     --               )
     --               AND 
					ro.ReturnOrderID NOT IN (  SELECT  ReturnOrderID FROM    #missing WHERE   MissingFrom = 'Destination' )
				AND ro.ReturnOrderID NOT IN  ( SELECT Pk  FROM   #missing  WHERE  MissingFrom = 'Source' )
        ) t2;
--120368
 
IF OBJECT_ID('tempdb..#missingInfo') IS NOT NULL
    DROP TABLE #missingInfo;

SELECT  hybris.pk ,
        rfo.ReturnPaymentId ,
        CASE WHEN hybris.pk IS NULL THEN 'missing in hybris'
             WHEN rfo.ReturnPaymentId IS NULL THEN 'missing in rfo'
        END results
INTO    #missingInfo
FROM    ( SELECT    hpi.pk
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                               AND u.p_sourcename = 'hybris-dm'
                    JOIN Hybris..countries c ON c.PK = u.p_country
                                                AND c.isocode = 'us'
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                                        AND hpi.duplicate = 1
                                                        AND ho.TypePkString = 8796127723602
                                                        AND ho.p_template IS NULL
                    JOIN Hybris.dbo.paymenttransactions hpt ON hpt.p_order = ho.PK
          WHERE     ho.pk NOT IN ( SELECT pk  FROM   #missing WHERE  MissingFrom = 'Source' )
		  AND  ho.pk NOT IN   (SELECT  ReturnOrderID FROM    #missing WHERE   MissingFrom = 'Destination' )
        ) hybris
        FULL OUTER JOIN ( SELECT    rp.ReturnPaymentId 
                          FROM      RFOperations.Hybris.ReturnOrder ro
                                    JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderNumber = rfl.OrderID
                                                              AND rfl.OrderTypeID = 9 
                                    JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
                                                              AND ro.CountryID = 236
                                                              AND u.p_sourcename = 'hybris-dm'
                                    JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID 
                                    JOIN Hybris..orders ho ON ho.pk = ro.ReturnOrderID
                                    JOIN Hybris.ReturnPaymentTransaction rpt ON rpt.ReturnPaymentId = rp.ReturnPaymentId 
                                   -- JOIN RodanFieldsLive.dbo.OrderPayments rop ON rop.OrderPaymentID = rp.ReturnPaymentId
                          WHERE     LEN(rp.ReturnPaymentId)>11 AND  rp.PaymenttypeId IS NOT NULL AND 
						   --AccountNumber <> 'HDCm5F9HLZ6JyWpnoVViLw=='
         --                           AND ( LTRIM(RTRIM(rop.BillingFirstName)) <> ''
         --                                 OR LTRIM(RTRIM(rop.BillingLastName)) <> ''
         --                               )
         --                           AND 
									ro.ReturnOrderID NOT IN (SELECT  ReturnOrderID FROM    #missing WHERE   MissingFrom = 'Destination' )
									AND ro.ReturnOrderID NOT IN( SELECT pk  FROM   #missing WHERE  MissingFrom = 'Source' )
                        ) rfo ON hybris.pk = rfo.ReturnPaymentID
WHERE   rfo.ReturnPaymentId IS NULL
        OR hybris.PK IS NULL; 
								


DECLARE @dupcounts INT;
SELECT  @dupcounts = COUNT(*)
FROM    #missingInfo;
		

SELECT  
        *
FROM    #missingInfo
WHERE   ReturnPaymentId IS NULL; 
		
SELECT 
        *
FROM    #missingInfo
WHERE   pk IS NULL; 
    
	

SELECT  'Return  PaymentInfos' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);


     /* Checking Counts of Key Updated In RFO Correctly.*/

     SELECT COUNT(hpi.PK) [Counts of RFO ReturnPayment KeyUpdated]
     FROM   Hybris.dbo.orders ho
            JOIN Hybris..users u ON u.PK = ho.userpk
                                    AND u.p_sourcename = 'Hybris-DM'
                                    AND u.p_country = 8796100624418
            JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
     WHERE  EXISTS ( SELECT 1
                     FROM   RFOperations.Hybris.ReturnPayment rp
                     WHERE  rp.ReturnPaymentId = hpi.PK )
            AND hpi.duplicate = 1
            AND ISNULL(ho.p_template, 0) = 0
            AND ho.TypePkString = 8796127723602
                   -- AND CAST(ho.modifiedTS AS DATE) = @ModifiedDate
            AND ho.PK NOT IN ( SELECT   PK
                               FROM     #missing
                               WHERE    MissingFrom = 'Source' )
            AND ho.PK NOT IN ( SELECT   ReturnOrderID
                               FROM     #missing
                               WHERE    MissingFrom = 'Destination' );



SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        USER_NAME() AS UserName ,
        'Return PaymentInfos' AS Entity; 
		
		

		



INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Return PaymentInfos WithKey' AS Entity ,
                'Counts,Dups,Missing' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;

			
			
			
				/* Returnpayment Billing Addresses*/


--DECLARE @StartedTime TIME;
--DECLARE @EndTime TIME; 

				
SELECT  'Return PaymentBillingAddress ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);

                 SELECT hybris_cnt ,
                        rfo_cnt ,
                        t1.hybris_cnt - t2.rfo_cnt AS Diff ,
                        CASE WHEN hybris_cnt > rfo_cnt
                             THEN 'Hybris count more than RFO count'
                             WHEN rfo_cnt > hybris_cnt
                             THEN 'RFO count more than Hybris count'
                             ELSE 'Count matches - validation passed'
                        END Results
                 FROM   ( SELECT    COUNT(ad.pk) hybris_cnt
                          FROM      Hybris.dbo.orders ho
                                    JOIN Hybris..users u ON u.PK = ho.userpk
                                                            AND u.p_sourcename = 'Hybris-DM'
                                                            AND u.p_country = 8796100624418
                                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
									JOIN Hybris..addresses ad ON ad.ownerpkstring=hpi.pk
									AND ad.duplicate=1 AND ad.P_billingaddress=1
                          WHERE     hpi.duplicate = 1 AND hpi.P_sourcename='hybris-DM'
                                    AND ISNULL(ho.p_template, 0) = 0
                                    AND ho.TypePkString = 8796127723602
                                   -- AND CAST(ho.modifiedTS AS DATE) = @ModifiedDate
                                    AND ho.PK NOT IN (  SELECT  pk  FROM    #missing WHERE   MissingFrom = 'Source' )
									AND ho.PK NOT IN( SELECT  ReturnOrderID  FROM    #missing  WHERE   MissingFrom = 'Destination' )
                        ) t1 , --119320
                        ( SELECT    COUNT(DISTINCT rba.ReturnBillingAddressId) rfo_cnt
                          FROM      RFOperations.Hybris.ReturnOrder ro
                                    JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderNumber = rfl.OrderID
                                                              AND rfl.OrderTypeID = 9
                                    JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
                                                              AND ro.CountryID = 236
                                                              AND u.p_sourcename = 'Hybris-DM'
                                    JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID
                                    JOIN Hybris..orders ho ON ho.Pk = ro.ReturnOrderID
                                                              AND ho.TypePkString = 8796127723602
                                   -- JOIN RodanFieldsLive.dbo.OrderPayments rrp ON rrp.OrderPaymentID = rp.ReturnPaymentId
									JOIN RFOperations.Hybris.ReturnBillingAddress rba ON rba.ReturnOrderId=ro.returnOrderId
                          WHERE      LEN(rp.ReturnPaymentId)>11 AND  rp.PaymenttypeId IS NOT NULL AND 
						  --If any Discripancy we should find the another method to join with RFL.
						   --( LTRIM(RTRIM(rrp.BillingFirstName)) <> ''
         --                             OR LTRIM(RTRIM(rrp.BillingLastName)) <> ''
         --                           )
         --                           AND 
									ro.ReturnOrderID NOT IN ( SELECT  ReturnOrderID  FROM    #missing  WHERE   MissingFrom = 'Destination' )
									AND ro.ReturnOrderID NOT IN (  SELECT  pk  FROM    #missing WHERE   MissingFrom = 'Source' )
                        ) t2;
						

						/* Checking Key Updated Properly in RFO */
				
                        SELECT  COUNT(ad.PK) [Counts of RFO ReturnBillingAddress KeyUpdated]
                        FROM    Hybris.dbo.orders ho
                                JOIN Hybris..users u ON u.PK = ho.userpk
                                                        AND u.p_sourcename = 'Hybris-DM'
                                                        AND u.p_country = 8796100624418
                                JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                JOIN Hybris..addresses ad ON ad.OwnerPkString = hpi.PK
                                                             AND ad.duplicate = 1
                                                             AND ad.p_billingaddress = 1
                        WHERE   EXISTS ( SELECT 1
                                         FROM   RFOperations.Hybris.ReturnBillingAddress rb
                                         WHERE  rb.ReturnBillingAddressID = ad.PK )
                                AND hpi.duplicate = 1
                                AND hpi.p_sourcename = 'hybris-DM'
                                AND ISNULL(ho.p_template, 0) = 0
                                AND ho.TypePkString = 8796127723602
                                   -- AND CAST(ho.modifiedTS AS DATE) = @ModifiedDate
                                AND ho.PK NOT IN (
                                SELECT  PK
                                FROM    #missing
                                WHERE   MissingFrom = 'Source' )
                                AND ho.PK NOT IN (
                                SELECT  ReturnOrderID
                                FROM    #missing
                                WHERE   MissingFrom = 'Destination' );



SELECT  'Return  PaymentBillingAddress' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);


     
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        USER_NAME() AS UserName ,
        'Return PaymentBillingAddress' AS Entity; 
		
		

		



INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Return PaymentAddress WithKey' AS Entity ,
                'Counts,Dups,Missing' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;

			
			
				/* Return paymentTransanction  */

				
--DECLARE @StartedTime TIME;
--DECLARE @EndTime TIME; 
		
SELECT  'Return PaymentTransaction With Key PaymentInfos ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);

SELECT  CASE WHEN COUNT(1) > 0 THEN 'duplicates found'
             ELSE 'no duplicates - validation passed'
        END AS [step-1 validation]
FROM    ( SELECT    COUNT(*) cnt ,
                    hpt.p_order ,
                    hpt.p_info --, a.ownerpkstring  
          FROM      Hybris.dbo.paymentinfos (NOLOCK) hpi
                    JOIN Hybris.dbo.users u ON u.PK = hpi.userpk
                                               AND u.p_sourcename = 'hybris-dm'
                                               AND u.p_country = 8796100624418
                    JOIN Hybris.dbo.orders (NOLOCK) ho ON ho.PK = hpi.OwnerPkString
                                                          AND ho.TypePkString = 8796127723602
                                                          AND ho.p_template IS NULL
                    JOIN Hybris.dbo.paymenttransactions hpt ON hpt.p_info = hpi.PK
                                                              AND hpi.duplicate = 1
          GROUP BY  hpt.p_order ,
                    hpt.p_info
          HAVING    COUNT(*) > 1
        ) t1;

				
SELECT  hybris_cnt ,
        rfo_cnt ,
        t1.hybris_cnt - t2.rfo_cnt AS Diff ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'hybris count more than rfo count'
             WHEN rfo_cnt > hybris_cnt THEN 'rfo count more than hybris count'
             ELSE 'count matches - validation passed'
        END results
FROM    ( SELECT    COUNT(DISTINCT hpi.pk) hybris_cnt
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                               AND u.p_sourcename = 'hybris-DM'
                                               AND u.p_country = 8796100624418
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                                        AND hpi.duplicate = 1
                                                        AND ho.TypePkString = 8796127723602
                                                        AND ISNULL(ho.p_template,
                                                              0) = 0
                    JOIN Hybris.dbo.paymenttransactions hpt ON hpt.p_order = ho.PK
          WHERE     ho.pk NOT IN ( SELECT pk
                                     FROM   #missing
                                     WHERE  MissingFrom = 'Source' )
        ) t1 , --181619
        ( SELECT    COUNT(DISTINCT rpt.ReturnPaymentId) rfo_cnt
          FROM      RFOperations.Hybris.ReturnOrder ro
                    JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderNumber = rfl.OrderID
                                                           AND rfl.OrderTypeID = 9
                    JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
                                               AND ro.CountryID = 236
                                               AND u.p_sourcename = 'hybris-DM'
                    JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID
                    JOIN Hybris..orders ho ON ho.pk = ro.ReturnOrderID
                    JOIN Hybris.ReturnPaymentTransaction rpt ON rpt.ReturnPaymentId = rp.ReturnPaymentId
                    --JOIN RodanFieldsLive.dbo.OrderPayments rop ON rop.OrderPaymentID = rp.ReturnPaymentId
          WHERE     LEN(rp.ReturnPaymentId)>11 AND  rp.PaymenttypeId IS NOT NULL AND 
		   --( LTRIM(RTRIM(rop.BillingFirstName)) <> ''
     --                 OR LTRIM(RTRIM(rop.BillingLastName)) <> ''
     --               )
     --               AND 
	 -- We should find Alternative way to join with RFL.
					ro.ReturnOrderID NOT IN (
                    SELECT  ReturnOrderID
                    FROM    #missing
                    WHERE   MissingFrom = 'Destination' )
        ) t2;
 --181616



SELECT  hybris.p_requestid ,
        rfo.transactionId ,
        CASE WHEN hybris.p_requestid IS NULL THEN 'missing in hybris'
             WHEN rfo.transactionId IS NULL THEN 'missing in rfo'
        END results
FROM    ( SELECT    hpt.p_requestid 
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                               AND u.p_sourcename = 'hybris-dm'
                    JOIN Hybris..countries c ON c.PK = u.p_country
                                                AND c.isocode = 'us'
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                                        AND hpi.duplicate = 1
                                                        AND ho.TypePkString = 8796127723602
                                                        AND ho.p_template IS NULL
                    JOIN Hybris.dbo.paymenttransactions hpt ON hpt.p_order = ho.PK 
          WHERE     ho.pk NOT IN ( SELECT pk FROM   #missing  WHERE  MissingFrom = 'Source' )
		  AND ho.pk NOT IN ( SELECT  ReturnOrderID FROM    #missing WHERE   MissingFrom = 'Destination' )
        ) hybris
        FULL OUTER JOIN ( SELECT  rpt.transactionId 
                         FROM      RFOperations.Hybris.ReturnOrder ro
                                    JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderNumber = rfl.OrderID
                                                              AND rfl.OrderTypeID = 9
                                    JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
                                                              AND ro.CountryID = 236
                                                              AND u.p_sourcename = 'hybris-dm'
                                    JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID
                                    JOIN Hybris..orders ho ON ho.pk = ro.ReturnOrderID
                                    JOIN Hybris.ReturnPaymentTransaction rpt ON rpt.ReturnPaymentId = rp.ReturnPaymentId  
                                  --  JOIN RodanFieldsLive.dbo.OrderPayments rop ON rop.OrderPaymentID = rp.ReturnPaymentId
                          WHERE     LEN(rp.ReturnPaymentId)>11 AND  rp.PaymenttypeId IS NOT NULL AND 
						    --AccountNumber <> 'HDCm5F9HLZ6JyWpnoVViLw=='
          --                          AND ( LTRIM(RTRIM(rop.BillingFirstName)) <> ''
          --                                OR LTRIM(RTRIM(rop.BillingLastName)) <> ''
          --                              )
          --                          AND 
									ro.ReturnOrderID NOT IN ( SELECT  ReturnOrderID FROM    #missing WHERE   MissingFrom = 'Destination' )
									AND ro.ReturnOrderID NOT IN( SELECT pk FROM   #missing  WHERE  MissingFrom = 'Source' )
                        ) rfo ON hybris.p_requestid = rfo.transactionId
WHERE   rfo.transactionId IS NULL
        OR hybris.p_requestid IS NULL; 
								

/* Checking Key Updated properly In RFO with Hybris Key.*/
SELECT  COUNT(hpt.p_requestid) [Counts of RFO ReturnPaymentTransaction Key Updated]
FROM    Hybris.dbo.orders ho
        JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                   AND u.p_sourcename = 'hybris-dm'
        JOIN Hybris..countries c ON c.PK = u.p_country
                                    AND c.isocode = 'us'
        JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                            AND hpi.duplicate = 1
                                            AND ho.TypePkString = 8796127723602
                                            AND ho.p_template IS NULL
        JOIN Hybris.dbo.paymenttransactions hpt ON hpt.p_order = ho.PK
WHERE   EXISTS ( SELECT 1
                 FROM   RFOperations.Hybris.ReturnPaymentTransaction rpt
                 WHERE  rpt.TransactionID = hpt.p_requestid )
        AND ho.PK NOT IN ( SELECT   PK
                           FROM     #missing
                           WHERE    MissingFrom = 'Source' )
        AND ho.PK NOT IN ( SELECT   ReturnOrderID
                           FROM     #missing
                           WHERE    MissingFrom = 'Destination' );
		

SELECT  'Return  PaymentTransaction  With Key PaymentInfos' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return PaymentTransaction  With Key PaymentInfos' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Return PaymentTransaction via PaymentInfos WithKey' AS Entity ,
                'Counts,Dups,Missing' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;


				
			


				
				
				/* Return PaymentTransaction */

				
--DECLARE @StartedTime TIME;
--DECLARE @EndTime TIME; 
		
SELECT  'Return PaymentTransaction ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);
			
	

--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    COUNT(*) cnt ,
                    f.p_paymenttransaction
          FROM      Hybris.dbo.paymentinfos (NOLOCK) a ,
                    Hybris.dbo.users b ,
                    Hybris.dbo.countries c ,
                    Hybris.dbo.orders (NOLOCK) d ,
                    Hybris.dbo.paymenttransactions e ,
                    Hybris.dbo.paymnttrnsctentries f
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND d.PK = a.OwnerPkString
                    AND a.PK = e.p_info
                    AND e.PK = f.p_paymenttransaction
                    AND c.isocode = 'US'
                    AND b.p_sourcename = 'Hybris-DM'
                    AND d.p_template IS NULL
                    AND d.TypePkString = 8796127723602
                    AND a.duplicate = 1 --R profile
GROUP BY            f.p_paymenttransaction
          HAVING    COUNT(*) > 1
        ) t1;


--Counts check on Hybris side for US
SELECT  hybris_cnt ,
        rfo_cnt ,
		t1.hybris_cnt-t2.rfo_cnt AS diff,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END ResultsFromPKtoReturnPaymentTransaction
FROM    ( SELECT    COUNT(hpe.PK) hybris_cnt
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON u.PK = ho.userpk
                                               AND ho.TypePkString = 8796127723602
                                               AND ISNULL(ho.p_template, 0) = 0
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                                        AND hpi.duplicate = 1
                    JOIN Hybris.dbo.paymenttransactions hpt ON hpt.p_order = ho.PK
                    JOIN Hybris.dbo.paymnttrnsctentries hpe ON hpe.p_paymenttransaction = hpt.PK
        ) t1 , --181630
        ( SELECT    COUNT([ReturnPaymentTransactionId]) rfo_cnt
          FROM      RFOperations.Hybris.ReturnOrder ro
                    JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderNumber = rfl.OrderID
                                                           AND rfl.OrderTypeID = 9
                    JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID
                    JOIN Hybris..orders ho ON ho.PK = ro.ReturnOrderID
                                              AND ro.CountryID = 236
                    JOIN Hybris.ReturnPaymentTransaction rpt ON rpt.ReturnPaymentId = rp.ReturnPaymentId
                    --JOIN RodanFieldsLive.dbo.OrderPayments rfp ON rfp.OrderPaymentID = rp.ReturnPaymentId
          --WHERE     ( LTRIM(RTRIM(rfp.BillingFirstName)) <> ''
          --            OR LTRIM(RTRIM(rfp.BillingLastName)) <> ''
          --          )

        ) t2;
 --181629
	

	/* Checking If RFO side Key been Updated Properly*/

	 SELECT    COUNT(hpe.PK) [Counts of RFO ReturnPaymentTransaction Key Updated]
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON u.PK = ho.userpk
                                               AND ho.TypePkString = 8796127723602
                                               AND ISNULL(ho.p_template, 0) = 0
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                                        AND hpi.duplicate = 1
                    JOIN Hybris.dbo.paymenttransactions hpt ON hpt.p_order = ho.PK
                    JOIN Hybris.dbo.paymnttrnsctentries hpe ON hpe.p_paymenttransaction = hpt.PK
					WHERE EXISTS(SELECT 1 FROM RFOperations.Hybris.ReturnPaymentTransaction rpt WHERE rpt.ReturnPaymentTransactionId=hpe.pk)

	

SELECT  'Return  PaymentTransaction' AS EntityName ,
        GETDATE() AS CompletionTime;
SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return PaymentTransaction' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Return PaymentTranstion WithKey' AS Entity ,
                'Counts,Dups,Missing' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;


				
				
				
SET @StartedTime = ( SELECT TOP 1
                            StartedTime
                     FROM   DataMigration.dbo.ExecResult
                     WHERE  Entity = 'Return Header'
                            AND Types = 'Counts,Dups,Missing'
                     ORDER BY RunDate DESC ,
                            StartedTime DESC
                   );


SET @EndTime = ( SELECT TOP 1
                        CompletionTime
                 FROM   DataMigration.dbo.ExecResult
                 WHERE  Entity = 'Return PaymentTranstion'
                        AND Types = 'Counts,Dups,Missing'
                 ORDER BY RunDate DESC ,
                        StartedTime DESC
               );
 	
INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'All Entities Related to Return WithKey' AS Entity ,
                'Counts,Dups,Missing' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;         
				