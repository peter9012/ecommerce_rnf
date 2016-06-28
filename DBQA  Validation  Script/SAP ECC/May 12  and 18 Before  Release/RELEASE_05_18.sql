--=======================================================================================
--						MAIN-4371 NULL SHIP DATE
--========================================================================================
--- Checking if MAIN-4371: Any missing ordershipment for services SKU's
USE RodanFieldsLive
SELECT COUNT(*)
FROM    [dbo].[Orders] AS O
        INNER JOIN [dbo].[OrderCustomers](NOLOCK) AS  OC ON [O].[OrderID] = [OC].[OrderID]
        INNER JOIN dbo.OrderItems (NOLOCK) AS  oi ON OC.OrderCustomerID = oi.OrderCustomerID
        INNER JOIN [dbo].[Accounts](NOLOCK) AS  A ON [OC].[AccountID] = [A].[AccountID]
		INNER JOIN [dbo].[AccountAddresses](NOLOCK) AS  AA ON [A].[AccountID] = [AA].[AccountID] AND [AA].[AddressTypeId] = 1
        LEFT JOIN [dbo].[OrderShipments] (NOLOCK) AS  OS ON [O].[OrderID] = [OS].[OrderID]		
WHERE   1 = 1
        AND o.[OrderStatusID] IN ( 4, 5 )
        AND o.[OrderTypeID] NOT IN ( 9, 11, 4, 5 )
		AND O.CompleteDate >= '1/01/2016'
        AND os.[OrderShipmentID] IS NULL
		AND oi.SKU IN ( 'RNW001','PULSE01');


-- for SKU PFC

		WITH PFC_OrderCustomers (OrderCustomerId)
AS
-- Select all orders with only PFC0001 item.
(
	SELECT 
		oi.OrderCustomerId
	FROM OrderItems oi JOIN 
		(
			SELECT 
					o.OrderID, oc.OrderCustomerID, oi.OrderItemId
			FROM Orders o 
					JOIN dbo.OrderCustomers(NOLOCK) oc ON oc.OrderID = o.OrderID
					JOIN dbo.OrderItems(NOLOCK) oi ON oc.OrderCustomerID = oi.OrderCustomerID
			WHERE   o.[OrderStatusID] IN ( 4, 5 )
					AND o.[OrderTypeID] NOT IN ( 9, 11, 4, 5 )
					AND oi.SKU IN ( 'PFC0001')
					AND O.CompleteDate >= '1/01/2016'
		) oc ON oi.OrderCustomerId = oc.OrderCustomerId
	GROUP BY oi.OrderCustomerId
	HAVING COUNT(oi.OrderItemId) = 1
)
SELECT COUNT(*)
FROM    [dbo].[Orders] AS O
        INNER JOIN [dbo].[OrderCustomers] (NOLOCK) AS OC ON [O].[OrderID] = [OC].[OrderID]
		JOIN PFC_OrderCustomers(NOLOCK) POC ON [OC].OrderCustomerID = [POC].OrderCustomerID
        INNER JOIN dbo.OrderItems(NOLOCK) AS oi ON OC.OrderCustomerID = oi.OrderCustomerID
        INNER JOIN [dbo].[Accounts] (NOLOCK) AS A ON [OC].[AccountID] = [A].[AccountID]
		INNER JOIN [dbo].[AccountAddresses] (NOLOCK) AS AA ON [A].[AccountID] = [AA].[AccountID] AND [AA].[AddressTypeId] = 1
        LEFT JOIN [dbo].[OrderShipments] (NOLOCK) AS OS ON [O].[OrderID] = [OS].[OrderID]
WHERE   1 = 1                    
        AND os.[OrderShipmentID] IS NULL



		--MAIN-4371: Add missing ordershipment items for services SKU's

		SELECT  os.OrderShipmentID ,
                    oi.OrderItemID ,
                    oi.Quantity ,
                    NULL
            FROM    Orders o
                    JOIN dbo.OrderCustomers(NOLOCK) oc ON oc.OrderID = o.OrderID
                    JOIN dbo.OrderItems(NOLOCK) oi ON oc.OrderCustomerID = oi.OrderCustomerID
                    JOIN dbo.OrderShipments(NOLOCK) os ON os.OrderID = o.OrderID
                    LEFT JOIN dbo.OrderShipmentItems (NOLOCK) osi ON osi.OrderItemID = oi.OrderItemID
                                                            AND osi.OrderShipmentID = os.OrderShipmentID
            WHERE   o.OrderStatusID = 4
                    AND oi.SKU IN ( 'RNW001' )
                    AND o.CompleteDate >= '1/01/2016'
                    AND o.OrderTypeID IN ( 1, 2, 3, 6, 7, 8 )
                    AND os.DateShipped IS NOT NULL
                    AND os.DateShipped > '2000-01-01'
                    AND osi.OrderShipmentItemID IS NULL;

					--FIX Service SKUs that always are alone in the orders

					SELECT  os.OrderShipmentID ,
                    oi.OrderItemID ,
                    oi.Quantity ,
                    NULL
            FROM    Orders o
                    JOIN dbo.OrderCustomers(NOLOCK) oc ON oc.OrderID = o.OrderID
                    JOIN dbo.OrderItems(NOLOCK) oi ON oc.OrderCustomerID = oi.OrderCustomerID
                    JOIN dbo.OrderShipments (NOLOCK)os ON os.OrderID = o.OrderID
                    LEFT JOIN dbo.OrderShipmentItems (NOLOCK)osi ON osi.OrderItemID = oi.OrderItemID
                                                            AND osi.OrderShipmentID = os.OrderShipmentID
            WHERE   o.OrderStatusID = 4
                    AND oi.SKU IN ( 'PULSE01' )
                    AND o.CompleteDate >= '1/01/2016'
                    AND o.OrderTypeID IN ( 1, 2, 3, 6, 7, 8 )
                    AND os.DateShipped IS NOT NULL
                    AND os.DateShipped > '2000-01-01'
                    AND osi.OrderShipmentItemID IS NULL;


					
			--FIX Service SKUs that needs to be alone in the orders to create a new OrderShipmentItems

   ; WITH    PFC_OrderCustomers ( OrderCustomerId )
              AS -- Select all orders with only PFC0001 item.
			( SELECT    oi.OrderCustomerID
              FROM      OrderItems (NOLOCK) oi
                        JOIN ( SELECT   o.OrderID ,
                                        oc.OrderCustomerID ,
                                        oi.OrderItemID
                               FROM     Orders (NOLOCK) o
                                        JOIN dbo.OrderCustomers (NOLOCK) oc ON oc.OrderID = o.OrderID
                                        JOIN dbo.OrderItems(NOLOCK) oi ON oc.OrderCustomerID = oi.OrderCustomerID
                               WHERE    o.OrderStatusID = 4
                                        AND oi.SKU IN ( 'PFC0001' )
                                        AND o.CompleteDate >= '1/01/2016'
                                        AND o.OrderTypeID IN ( 1, 2, 3, 6, 7, 8 )
                             ) oc ON oi.OrderCustomerID = oc.OrderCustomerID
                                    
              GROUP BY  oi.OrderCustomerID
              HAVING    COUNT(oi.OrderItemID) = 1
            )
			 SELECT  os.OrderShipmentID ,
                        oi.OrderItemID ,
                        oi.Quantity ,
                        NULL
                FROM    Orders (NOLOCK) o
                        JOIN dbo.OrderCustomers (NOLOCK) oc ON oc.OrderID = o.OrderID
                        JOIN PFC_OrderCustomers poc ON oc.OrderCustomerID = poc.OrderCustomerId
                        JOIN dbo.OrderItems (NOLOCK) oi ON oc.OrderCustomerID = oi.OrderCustomerID
                        JOIN dbo.OrderShipments(NOLOCK) os ON os.OrderID = o.OrderID
                        LEFT JOIN dbo.OrderShipmentItems(NOLOCK) osi ON osi.OrderItemID = oi.OrderItemID
                                                                AND osi.OrderShipmentID = os.OrderShipmentID
                WHERE   osi.OrderShipmentItemID IS NULL
                        AND os.DateShipped IS NOT NULL
                        AND os.DateShipped > '2000-01-01';

--=======================================================================================
--					MAIN-4375  Missing order dates
--========================================================================================
 ---RFL 
 SELECT  DISTINCT
        o.OrderID ,
        os.Name AS [Order Status] ,
        t.Name AS [Order type] ,
        o.CompleteDate
 FROM   RodanFieldsLive.dbo.Orders (NOLOCK) o
        JOIN RodanFieldsLive.dbo.OrderStatus (NOLOCK) os ON os.OrderStatusID = o.OrderStatusID
        JOIN RodanFieldsLive.dbo.OrderTypes (NOLOCK) t ON t.OrderTypeID = o.OrderTypeID
 WHERE  ISNULL(o.CompleteDate, '1900-01-01') = '1900-01-01'
        AND CAST(o.StartDate AS DATE) >= '2016-03-01'--Starting from March 1 , 2016
        AND o.OrderTypeID IN ( 4, 5 );-- No Templates 

     ---RFO sides
	    SELECT  COUNT(*)
        FROM    RFOperations.Hybris.Orders ro
        WHERE   ISNULL(ro.CompletionDate, '1900-01-01') = '1900-01-01'
                AND ro.CompletionDate >= '2016-03-01';

				
              --SELECT    o.CompleteDate,o.StartDate,ro.CompletionDate
              --FROM      RodanFieldsLive.dbo.Orders (NOLOCK) o
              --          JOIN RFOperations.Hybris.ReturnOrder (NOLOCK) ro ON ro.OrderID = o.OrderID--- RFO Orders 
              --WHERE     o.CompleteDate <> ro.CompletionDate
              --          AND o.OrderTypeID NOT IN ( 4, 5 )
              --          AND o.StartDate >= '2016-03-01'; 



--=======================================================================================
--						MAIN-4374 Same item on different line items
--========================================================================================
USE RodanFieldsLive
SELECT  o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU ,
        COUNT(*) AS 'Repeat'
FROM    dbo.Orders o
        JOIN OrderCustomers(NOLOCK) oc ON o.OrderID = oc.OrderID
        JOIN OrderItems (NOLOCK)oi ON oi.OrderCustomerID = oc.OrderCustomerID
WHERE   o.OrderTypeID IN ( 4, 5 )
        AND o.OrderStatusID IN (4,7)
GROUP BY o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU
HAVING  COUNT(*) > 1
ORDER BY oc.OrderCustomerID DESC


---- Take a back up Please 
USE RodanFieldsLive
SELECT  o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU ,
		oi.Quantity,
        COUNT(*) AS 'Repeat' --INTO RodanFieldsLive.dbo.tempales_backup_0618
FROM    dbo.Orders o
        JOIN OrderCustomers(NOLOCK) oc ON o.OrderID = oc.OrderID
        JOIN OrderItems (NOLOCK)oi ON oi.OrderCustomerID = oc.OrderCustomerID
WHERE   o.OrderTypeID IN ( 4, 5 )
        AND o.OrderStatusID IN (4,7)
GROUP BY o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU ,
		oi.Quantity
HAVING  COUNT(*) > 1
ORDER BY oc.OrderCustomerID DESC


-- Validation of Quantity.
USE RodanFieldsLive;
SELECT  DISTINCT o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU ,
		B.SKU AS BackUpSKU,
		oi.Quantity,
		B.Quantity BackUpQuantity,
		B.[Repeat],
		B.Quantity*B.[Repeat] AS DerivedQuantity       
FROM    dbo.Orders (NOLOCK) o
        JOIN dbo.OrderCustomers (NOLOCK) oc ON o.OrderID = oc.OrderID
        JOIN dbo.OrderItems (NOLOCK) oi ON oi.OrderCustomerID = oc.OrderCustomerID
        JOIN RodanFieldsLive.dbo.tempales_backup_0618 B ON B.OrderID = o.OrderID
WHERE   oi.SKU = B.SKU
        AND  oi.Quantity<>B.Quantity*B.[Repeat]
        AND o.OrderTypeID IN ( 4, 5 )
        AND o.OrderStatusID IN ( 4, 7 )


--=======================================================================================
--					GP-2147 MAIN-4542  Zero order item quantities – Boomi
--========================================================================================


--- No Such things to do In this validation .

SELECT  o.OrderID ,
o.StartDate,
        oi.OrderItemID ,
        oi.SKU ,
        oi.Quantity ,
        o.OrderStatusID ,
        ot.Name AS OrderType
FROM    RodanFieldsLive.dbo.Orders(NOLOCK) o
        JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON o.OrderID = oc.OrderID
        JOIN RodanFieldsLive.dbo.OrderItems(NOLOCK) oi ON oi.OrderCustomerID = oc.OrderCustomerID
        JOIN RodanFieldsLive.dbo.OrderTypes(NOLOCK) ot ON ot.OrderTypeID = o.OrderTypeID
WHERE   o.OrderStatusID = 4
        AND o.CompleteDate >= GETDATE() - 15 
 --o.CompleteDate BETWEEN '1/1/2016' AND '3/25/2016'
        AND oi.Quantity = 0
        AND oi.CommissionableTotal > 0;

--=======================================================================================
--					GP-2044,GP-2082 AND GP-2083 Wrong order statuses – data cleanup
--========================================================================================


  --====================================================
  -- TEST : ORDER ITEMS 
  --==================================================

    --CHECKING IF ANY ORDERITEM MISSING IN RFO FROM RFL.
 DECLARE @Startdate DATE = '01/01/2016'
 DECLARE @Loaddate DATE = '05/01/2016'

 SELECT COUNT(*) [TOTAL MISSING ORDERITEMS]
  FROM   RodanFieldsLive.dbo.Orders(NOLOCK) o
        JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.OrderID = o.OrderID
                                                    
        JOIN RodanFieldsLive.dbo.OrderShipments(NOLOCK) os ON os.OrderID = o.OrderID
        JOIN RFOperations.Hybris.Orders(NOLOCK) ho ON ho.OrderID = oc.OrderID
        JOIN RodanFieldsLive.dbo.OrderItems(NOLOCK) oi ON oi.OrderCustomerID = oc.OrderCustomerID
 WHERE  NOT EXISTS ( SELECT 1
                     FROM   RFOperations.Hybris.OrderItem(NOLOCK) roi
                     WHERE  oi.OrderItemID = roi.OrderItemID )
        AND o.OrderTypeID NOT IN ( 4, 5, 9 )
        AND o.OrderStatusID IN ( 4, 7 )
        AND ISNULL(os.DateShipped, '1900-01-01') <> '1900-01-01'
		 AND o.StartDate >= @Startdate AND o.StartDate<@Loaddate;


		 

		/* TEST CAST 2 and 3 :Taking Counts of Loaded OrderItem records in RFO */
		
DECLARE @Startdate DATE = '01/01/2016';
DECLARE @Loaddate DATE = GETDATE();

SELECT  COUNT(*) ,
        oi.ChangedByApplication ,
        oi.ChangedByUser
FROM    RFOperations.Hybris.OrderItem(NOLOCK) oi
        JOIN RFOperations.Hybris.Orders(NOLOCK) ro ON ro.OrderID = oi.OrderId
WHERE   oi.ChangedByUser LIKE '%GP%'
        AND ro.CompletionDate >= @Startdate
        AND ro.CompletionDate < @Loaddate
GROUP BY oi.ChangedByApplication ,
        oi.ChangedByUser;
		-- 66 Historical
		-- 0 on this date.



	--============================
	 --Test 3: ORDERSSHIPMENT 
	 --==========================

	 --Checking if still missing OrderShipment from RFL to Inserted Ordershiping in RFO 

         DECLARE @Startdate DATE = '01/01/2016';
        -- DECLARE @Loaddate DATE = '05/01/2016';
         SELECT COUNT(*) -- Should be Null after insert.
         FROM   [RodanFieldsLive].[dbo].[OrderShipments](NOLOCK) os
                JOIN [RodanFieldsLive].[dbo].OrderCustomers(NOLOCK) oc ON oc.OrderID = os.OrderID
                JOIN [RodanFieldsLive].[dbo].Orders(NOLOCK) o ON o.OrderID = os.OrderID
                JOIN RFOperations.Hybris.Orders(NOLOCK) ro ON ro.OrderID = os.OrderID
                LEFT JOIN RFOperations.Hybris.OrderShipment(NOLOCK) ros ON ros.OrderShipmentID = os.OrderShipmentID
                                                              AND ros.OrderID = ro.OrderID
         WHERE  ro.CompletionDate >= @Startdate
              --  AND ro.CompletionDate < @Loaddate
                AND-- comment to get historical 
                o.OrderTypeID NOT  IN ( 4, 5, 9 ) -- just Orders excluding Templates and Returns.
                AND oc.AccountID IS NOT NULL
                AND ros.OrderShipmentID IS NULL; 


-- Test 5 and 6 Count and 'GP-2083' tag for  Ordershipment part of GP-2083

 DECLARE @Startdate DATE = '01/01/2016';

        -- DECLARE @Loaddate DATE = '05/01/2016';
 SELECT COUNT(*) [Total inserted by GP-2083] ,
        ros.ChangedByApplication ,
        ros.ChangedByUser
 FROM   [RodanFieldsLive].[dbo].[OrderShipments](NOLOCK) os
        JOIN [RodanFieldsLive].[dbo].OrderCustomers(NOLOCK) oc ON oc.OrderID = os.OrderID
        JOIN [RodanFieldsLive].[dbo].Orders(NOLOCK) o ON o.OrderID = os.OrderID
        JOIN RFOperations.Hybris.Orders(NOLOCK) ro ON ro.OrderID = os.OrderID
        JOIN RFOperations.Hybris.OrderShipment(NOLOCK) ros ON ros.OrderShipmentID = os.OrderShipmentID
                                                      AND ros.OrderID = ro.OrderID
 WHERE  ro.CompletionDate >= @Startdate
        AND o.OrderTypeID NOT  IN ( 4, 5, 9 ) -- just Orders excluding Templates and Returns.
        AND oc.AccountID IS NOT NULL
 GROUP BY ros.ChangedByApplication ,
        ros.ChangedByUser
               
			   
		
		
	 --================================================ 
	       --ORDERSHIPPING  ADDRESS 
	 --================================================ 
	 -- TEST CASE NO :8

     SELECT COUNT(*) [Total Count of Missing Shipment Addresses] -- Should be Zero .
     FROM   RFOperations.Cleanup.OrderShipments(NOLOCK) ros
            JOIN RodanFieldsLive..OrderShipments(NOLOCK) os ON ros.OrderShipmentID = os.OrderShipmentID
            JOIN RFOperations.Hybris.Orders(NOLOCK) o ON o.OrderID = os.OrderID
            LEFT JOIN RFOperations.Hybris.OrderShippingAddress(NOLOCK) ross ON ross.OrderShippingAddressID = os.OrderShipmentID
     WHERE  ross.OrderShippingAddressID IS NULL; 
       
		-- CHECKING THE COUNTS OF NEWLY INSERTED ORDERSHIPMENT ADDRESS IN RFO , TEST CASE NO 9 AND 10

		   
		     DECLARE @Startdate DATE = '01/01/2016';
             --DECLARE @Loaddate DATE = '05/01/2016';
			 DECLARE @loadate DATE =GETDATE()
             SELECT COUNT(*) [Total Inserted Address] ,
                    ross.ChangedByApplication ,
                    ross.ChangedByUser
             FROM   RFOperations.Cleanup.OrderShipments(NOLOCK) ros
                    JOIN RodanFieldsLive..OrderShipments(NOLOCK) os ON ros.OrderShipmentID = os.OrderShipmentID
                    JOIN RFOperations.Hybris.Orders(NOLOCK) o ON o.OrderID = os.OrderID
                    JOIN RFOperations.Hybris.OrderShippingAddress(NOLOCK) ross ON ross.OrderShippingAddressID = os.OrderShipmentID
             WHERE  o.CompletionDate >= @Startdate
                   -- AND o.CompletionDate < @Loaddate
             GROUP BY ross.ChangedByApplication ,
                    ross.ChangedByUser


--=========================================================
--		ORDERSHIPMENT PACKAGE ITEMS 
--=========================================================



					--Test 12:  Checking If item not having records in OrderItem table in RFO



						/* for Missing OrdershipmentItem in RFO ****/		
                        DECLARE @Startdate DATE = '01/01/2016';
                        DECLARE @Loaddate DATE = GETDATE()
                        SELECT  o.OrderID ,
                                o.StartDate ,
                                o.OrderStatusID ,
                                rsi.OrderShipmentPackageItemID ,
                                os.DateShipped ,
                                osi.*
                        FROM    RodanFieldsLive.dbo.Orders(NOLOCK) o
                                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.OrderID = o.OrderID
                                JOIN RodanFieldsLive.dbo.OrderItems(NOLOCK) oi ON oi.OrderCustomerID = oc.OrderCustomerID
                                JOIN RodanFieldsLive.dbo.OrderShipments(NOLOCK) os ON os.OrderID = o.OrderID
                                JOIN RodanFieldsLive.dbo.OrderShipmentItems(NOLOCK) osi ON osi.OrderItemID = oi.OrderItemID
                                LEFT JOIN RFOperations.Hybris.OrderShipmentPackageItem(NOLOCK) rsi ON rsi.OrderItemId = oi.OrderItemID
                        WHERE   rsi.OrderItemId IS NULL
                                AND oi.Quantity <> 0
                                AND o.OrderTypeID NOT IN ( 4, 5, 9 )
--rsi.OrderShipmentPackageItemID IS NULL
                                AND o.StartDate >= @Startdate
                                AND o.StartDate <= @Loaddate
	




				--FULLY MISSING FROM THE POIN OF ORDERSHIPMEMT 
             DECLARE @Startdate DATE = '01/01/2016';
             DECLARE @Loaddate DATE = GETDATE();
             SELECT COUNT(*) -- SHOULD BE ZERO IF ITEM NOT IN ORDERITEM TABLE IN RFO.(Known Issue)
             FROM   RodanFieldsLive.dbo.Orders (NOLOCK) rflo
                    INNER JOIN RFOperations.Hybris.Orders (NOLOCK) o ON rflo.OrderID = o.OrderNumber
                    INNER JOIN RodanFieldsLive.dbo.OrderCustomers (NOLOCK) oc ON rflo.OrderID = oc.OrderID
                                                              AND rflo.OrderStatusID IN (
                                                              4, 7 )
                                                              AND rflo.OrderTypeID NOT IN (
                                                              4, 5, 9 )
                    INNER JOIN RodanFieldsLive.dbo.OrderItems (NOLOCK) oi ON oc.OrderCustomerID = oi.OrderCustomerID
                    INNER JOIN RFOperations.Hybris.ProductsRF (NOLOCK) p ON p.productID = oi.ProductID
                    INNER JOIN RodanFieldsLive.dbo.OrderShipments (NOLOCK) os ON os.OrderID = rflo.OrderID
                    INNER JOIN RodanFieldsLive.dbo.OrderShipmentItems (NOLOCK) osip ON osip.OrderItemID = oi.OrderItemID
                                                              AND osip.OrderShipmentID = os.OrderShipmentID
                    LEFT JOIN RFOperations.Hybris.OrderShipmentPackageItem (NOLOCK) osi ON osi.OrderShipmentID = os.OrderShipmentID
             WHERE  o.CompletionDate >= @Startdate
                    AND o.CompletionDate < @Loaddate
                    AND --comment to get historical records.
                    osi.OrderShipmentPackageItemID IS NULL
                    AND EXISTS ( SELECT 1
                                 FROM   RFOperations.Hybris.OrderItem(NOLOCK) rt
                                 WHERE  rt.OrderItemID = osi.OrderItemId ); 


								 -- PARTIALLY MISSING

	
             DECLARE @Startdate DATE = '01/01/2016';
             DECLARE @Loaddate DATE = GETDATE();
            SELECT COUNT(*) -- SHOULD BE ZERO IF ITEM NOT IN ORDERITEM TABLE IN RFO.(Known Issue)
         -- oi.* 
		    FROM   RodanFieldsLive.dbo.Orders (NOLOCK) rflo
                    INNER JOIN RFOperations.Hybris.Orders (NOLOCK) o ON rflo.OrderID = o.OrderNumber
                    INNER JOIN RodanFieldsLive.dbo.OrderCustomers (NOLOCK) oc ON rflo.OrderID = oc.OrderID
                                                              AND rflo.OrderStatusID IN (
                                                              4, 7 )
                                                              AND rflo.OrderTypeID NOT IN (
                                                              4, 5, 9 )
                    INNER JOIN RodanFieldsLive.dbo.OrderItems (NOLOCK) oi ON oc.OrderCustomerID = oi.OrderCustomerID
                    INNER JOIN RFOperations.Hybris.ProductsRF (NOLOCK) p ON p.productID = oi.ProductID
                    INNER JOIN RodanFieldsLive.dbo.OrderShipments (NOLOCK) os ON os.OrderID = rflo.OrderID
                    INNER JOIN RodanFieldsLive.dbo.OrderShipmentItems (NOLOCK) osip ON osip.OrderItemID = oi.OrderItemID
                                                              AND osip.OrderShipmentID = os.OrderShipmentID
                    LEFT JOIN RFOperations.Hybris.OrderShipmentPackageItem (NOLOCK) osi ON osi.OrderItemId = osip.OrderItemID
             WHERE  o.CompletionDate >= @Startdate
                    AND o.CompletionDate < @Loaddate
                    AND --comment to get historical records.
                    osi.OrderShipmentPackageItemID IS NULL
                    AND EXISTS ( SELECT 1
                                 FROM   RFOperations.Hybris.OrderItem(NOLOCK) rt
                                 WHERE  rt.OrderItemID = osi.OrderItemId ); 

								 --OR
								 /* SCENARIO 3: Test 15 Checking  If Partial Item Missing In Shipment Items in RFL NOT LOADED AFTER INSERTING */
		
          DECLARE @Startdate DATE = '01/01/2016';
          DECLARE @Loaddate DATE = '05/01/2016';
          WITH  RFLItems
                  AS ( SELECT  DISTINCT
                                oi.OrderItemID ,
                                ossi.OrderShipmentItemID
                       FROM     RodanFieldsLive.dbo.Orders (NOLOCK) rflo
                                INNER JOIN RFOperations.Hybris.Orders(NOLOCK) o ON rflo.OrderID = o.OrderNumber
                                INNER JOIN RodanFieldsLive.dbo.OrderCustomers (NOLOCK) oc ON rflo.OrderID = oc.OrderID
                                                              AND rflo.StartDate >= @Startdate
                                                              AND rflo.StartDate < @Loaddate
                                                              AND rflo.OrderStatusID IN (
                                                              4, 7 )
                                                              AND rflo.OrderTypeID NOT IN (
                                                              4, 5, 9 )
                                INNER JOIN RodanFieldsLive.dbo.OrderItems (NOLOCK) oi ON oc.OrderCustomerID = oi.OrderCustomerID
                                INNER JOIN RodanFieldsLive.dbo.OrderShipments(NOLOCK) os ON os.OrderID = rflo.OrderID
                                INNER JOIN RodanFieldsLive.dbo.OrderShipmentItems(NOLOCK) osi ON osi.OrderShipmentID = os.OrderShipmentID
                                LEFT JOIN RodanFieldsLive.dbo.OrderShipmentItems(NOLOCK) ossi ON ossi.OrderItemID = oi.OrderItemID
                       WHERE    ossi.OrderShipmentItemID IS NULL
                     )
           
										
          SELECT    RFL.* ,
                    RFO.*
          FROM      RFOperations.Hybris.OrderShipmentPackageItem(NOLOCK) RFO
                    JOIN RFLItems RFL ON RFL.OrderItemId = RFO.OrderItemId
          WHERE     ISNULL(RFL.OrderShipmentItemID, 0) <> RFO.OrderShipmentPackageItemID -- Make = to see Corrected records.
		  AND RFO.Quantity=0


		  

-- ANY ITEM FROM ITEMS TO ORDERSHIMPMENTITEM TABLE.
  DECLARE @Startdate DATE = '01/01/2016';
  DECLARE @Loaddate DATE = '04/01/2016';
  SELECT   -- COUNT(*) -- SHOULD BE ZERO IF ITEM NOT IN ORDERITEM TABLE IN RFO.(Known Issue)
            o.OrderID,oi.*
  FROM      RodanFieldsLive.dbo.Orders (NOLOCK) rflo
            INNER JOIN RFOperations.Hybris.Orders (NOLOCK) o ON rflo.OrderID = o.OrderID
                                                              AND o.CountryID = 236
            INNER JOIN RodanFieldsLive.dbo.OrderCustomers (NOLOCK) oc ON rflo.OrderID = oc.OrderID
                                                              AND rflo.OrderStatusID IN (
                                                              4, 7 )
                                                              AND rflo.OrderTypeID NOT IN (
                                                              4, 5, 9 )
            INNER JOIN RodanFieldsLive.dbo.OrderItems (NOLOCK) oi ON oc.OrderCustomerID = oi.OrderCustomerID
            INNER JOIN RodanFieldsLive.dbo.OrderShipments (NOLOCK) os ON os.OrderID = rflo.OrderID
            INNER JOIN RFOperations.Hybris.OrderItem(NOLOCK) ri ON ri.OrderItemID = oi.OrderItemID
            LEFT JOIN RFOperations.Hybris.OrderShipmentPackageItem (NOLOCK) osi ON osi.OrderItemId = oi.OrderItemID
  WHERE     o.CompletionDate >= @Startdate
            AND o.CompletionDate <= @Loaddate
            AND --comment to get historical records.
            osi.OrderShipmentPackageItemID IS NULL
            AND oi.Quantity <> 0 ---Quantity Issues
			AND ISNULL(os.DateShipped,'1900-01-01')<>'1900-01-01'
            AND ri.OrderItemID NOT IN (
            SELECT  a.OrderItemID
            FROM    RodanFieldsLive.dbo.OrderItems a
            GROUP BY a.ProductID ,
                    a.SKU ,
                    a.Quantity ,
                    a.orderitemID
            HAVING  COUNT(*) > 1 )

                   

-- ANY ITEM FROM ITEMS TO ORDERSHIMPMENTITEM TABLE.

  DECLARE @Startdate DATE = '01/01/2016';
  DECLARE @Loaddate DATE ='04/01/2016';
  SELECT    --COUNT(*) -- SHOULD BE ZERO IF ITEM NOT IN ORDERITEM TABLE IN RFO.(Known Issue)
            oi.OrderItemID ,
            o.OrderID ,
            rflo.StartDate ,
            oi.SKU ,
            o.OrderStatusID ,
            oi.Quantity ,
            os.*
  FROM      RodanFieldsLive.dbo.Orders (NOLOCK) rflo
            INNER JOIN RFOperations.Hybris.Orders (NOLOCK) o ON rflo.OrderID = o.OrderNumber
            INNER JOIN RodanFieldsLive.dbo.OrderCustomers (NOLOCK) oc ON rflo.OrderID = oc.OrderID
                                                              AND rflo.OrderStatusID IN (
                                                              4, 7 )
                                                              AND rflo.OrderTypeID NOT IN (
                                                              4, 5, 9 )
            INNER JOIN RodanFieldsLive.dbo.OrderItems (NOLOCK) oi ON oc.OrderCustomerID = oi.OrderCustomerID
            INNER JOIN RodanFieldsLive.dbo.OrderShipments (NOLOCK) os ON os.OrderID = rflo.OrderID
                                                              AND ISNULL(os.DateShipped,
                                                              '1900-01-01') <> '1900-01-01'
            INNER JOIN RFOperations.Hybris.OrderItem(NOLOCK) ri ON ri.OrderItemID = oi.OrderItemID
            LEFT JOIN RFOperations.Hybris.OrderShipmentPackageItem (NOLOCK) osi ON osi.OrderItemId = oi.OrderItemID
  WHERE     o.CompletionDate >= @Startdate
            AND o.CompletionDate <= @Loaddate
            AND --comment to get historical records.
            osi.OrderShipmentPackageItemID IS NULL
            AND oi.Quantity <> 0 ---Quantity Zeros.
            AND EXISTS ( SELECT 1
                         FROM   RodanFieldsLive.dbo.OrderLog(NOLOCK) a
                         WHERE  a.OrderID = rflo.OrderID
                                AND a.[Description] = 'USA FF Export' )
								--AND rflo.OrderID IN (SELECT a.orderID FROM RFOperations.Hybris.OrderItem a
								--GROUP BY  a.OrderID
								--HAVING COUNT(a.OrderId)=1)							
            AND oc.OrderCustomerID NOT IN (
            SELECT  a.OrderCustomerID
            FROM    RodanFieldsLive.dbo.OrderItems(NOLOCK) a
            GROUP BY a.ProductID ,
                    a.SKU ,
                    a.Quantity ,
                    a.OrderCustomerID
            HAVING  COUNT(*) > 1 ) --- duplicate Items in orderItems
            AND os.OrderShipmentStatusID = 3 ---Shipped

			

  /* Test 13,14 AND 15 : Getting Count of Inserted in RFO With Proper tags. */

           DECLARE @Startdate DATE = '01/01/2016';
           DECLARE @Loaddate DATE = GETDATE();

			 --DECLARE @Startdate DATE = DATEADD(DAY,-60,GETDATE());
			 --DECLARE @Loaddate DATE = GETDATE();

           SELECT   COUNT(*) [Total counts of Inserted In RFO ] ,
                    osi.ChangedByApplication ,
                    osi.ChangedByUser
           FROM     RFOperations.Hybris.OrderShipmentPackageItem(NOLOCK) osi
                    JOIN RFOperations.Hybris.OrderItem(NOLOCK) oi ON oi.OrderItemID = osi.OrderItemId
           WHERE    osi.ChangedByUser LIKE '%GP%'-- All orders=2691194
                    --AND osi.ChangedByApplication <> 'GP-2083 Batch Update'
                    AND EXISTS ( SELECT 1
                                 FROM   RFOperations.Hybris.Orders(NOLOCK) o
                                 WHERE  o.OrderID = oi.OrderId
                                        AND o.CompletionDate >= @Startdate
                                        AND o.CompletionDate < @Loaddate )
           GROUP BY osi.ChangedByApplication ,
                    osi.ChangedByUser
					  


									
	

		
		/*  For all historical orders in STG2:

				Total counts of Inserted In RFO 	ChangedByApplication	ChangedByUser
				20723	GP-2083 Missing Shipment Items in RFL	GP-2083
				4213	GP-2083 Partially Missing Shipment Items in RFO	GP-2083
				315697	GP-2083 Missing Shipment Items in RFO	GP-2083 RFLIVE


				From January Orders in STG2 to today.
				Total counts of Inserted In RFO 	ChangedByApplication	ModifiedDate	ChangedByUser
				4292	GP-2083 Partially Missing Shipment Items in RFO	2016-05-12	GP-2083
				27143	GP-2083 Batch Update	2016-05-12	GP-2083
				304356	GP-2083 Missing Shipment Items in RFL	2016-05-12	GP-2083

				From January to March 4th 
				Total counts of Inserted In RFO 	ChangedByApplication	ModifiedDate	ChangedByUser
				4	GP-2083 Batch Update	2016-05-15	GP-2083
				4287	GP-2083 Partially Missing Shipment Items in RFO	2016-05-15	GP-2083
				304356	GP-2083 Missing Shipment Items in RFL	2016-05-15	GP-2083

		
		
		*/
		
		
				
		
		 --====================================================================
		 -- TEST CASE NO 16: Is there any orders not been updated as shipped because of 2083.
		 --====================================================================

DECLARE @Startdate DATE = '01/01/2016';
DECLARE @Loaddate DATE = '05/01/2016';
SELECT  COUNT(*) Counts ,ro.OrderStatusID,
        s.Name,osi.ChangedByApplication
FROM    RFOperations.Hybris.Orders(NOLOCK) ro
        JOIN RFOperations.RFO_Reference.OrderStatus(NOLOCK) s ON s.OrderStatusId = ro.OrderStatusID
		JOIN RFOperations.Hybris.OrderShipmentPackageItem(NOLOCK) osi ON osi.OrderID = ro.OrderID
WHERE   ro.CompletionDate >= @Startdate
        AND ro.CompletionDate < @Loaddate -- Comment to get historical records.
        AND ro.ChangedByUser LIKE '%GP%'
       AND  osi.ChangedByUser = 'GP-2083' 
	   AND ro.OrderStatusID<>4 --shipped 
GROUP BY s.Name ,osi.ChangedByApplication,ro.OrderStatusID


		
		
		



/* Test 17,18 and 19: Checking Load Impact in RFO OrderStatus Modification because of newly inserted Ordershipment Item */
		
		
DECLARE @Startdate DATE = '01/01/2016';
--DECLARE @Loaddate DATE = '05/01/2016';
DECLARE @Loaddate DATE = GETDATE();
SELECT  COUNT(*) Counts ,
        s.Name,osi.ChangedByApplication
FROM    RFOperations.Hybris.Orders(NOLOCK) ro
        JOIN RFOperations.RFO_Reference.OrderStatus(NOLOCK) s ON s.OrderStatusId = ro.OrderStatusID
		JOIN RFOperations.Hybris.OrderShipmentPackageItem(NOLOCK) osi ON osi.OrderID = ro.OrderID		
WHERE   ro.CompletionDate >= @Startdate
        AND ro.CompletionDate < @Loaddate -- Comment to get historical records.
        AND ro.ChangedByUser LIKE '%GP%'
       AND  osi.ChangedByUser = 'GP-2083' 
GROUP BY s.Name ,osi.ChangedByApplication



	/*	CCounts	Name	ChangedByApplication
7122	Shipped	GP-2083 Missing Shipment Items in RFL


		 */


		 --===========================================================
		 --	TEST CASE 20:  Duplication Validation 
		 --===========================================================

		 -- CHECKING IN ORDERITEMS TABLE LOADED WITH GP-TAG
		 SELECT COUNT(*) FROM RFOperations.Hybris.OrderItem (NOLOCK)
		 WHERE ChangedByApplication LIKE'%GP%'
		 GROUP BY OrderId,ProductID,Quantity
		 HAVING COUNT(*)>1

		 --CHECKING IN ORDERSHIPMENT TABLE LOADED BY GP-2083
		 SELECT COUNT(*) FROM RFOperations.Hybris.OrderShipment(NOLOCK)
		 WHERE ChangedByApplication LIKE'%GP%'
		 GROUP BY OrderID--,ShippingCost,HandlingCost
		 HAVING COUNT(*)>1

		 -- CHECKING IN ORDERSHIPMENT ADDRESS TABLE LOADED BY GP-2083
		 SELECT COUNT(*) FROM RFOperations.Hybris.OrderShippingAddress (NOLOCK)
		 WHERE ChangedByApplication LIKE '%GP%'
		 GROUP BY OrderID
		 HAVING COUNT(*)>1

		 --CHECKING IN ORDERSHIPMENTPACKAGEITEM TABLE LOADED BY GP-2083, BULK AND BATCH.
		 SELECT COUNT(*),OrderID --,OrdershipmentID
		 FROM RFOperations.Hybris.OrderShipmentPackageItem (NOLOCK)
		 WHERE ChangedByApplication LIKE '%GP%'
		 GROUP BY OrderID,OrderItemId--,OrdershipmentID
		 HAVING COUNT(*)>1

		

			



				 /* Checking If any Sequence been duplicates*/
				;
          WITH  CTE
                  AS ( SELECT   OrderShipmentPackageItemID ,
                                ROW_NUMBER() OVER ( PARTITION BY OrderShipmentPackageItemID ORDER BY OrderID ) rn
                       FROM     RFOperations.Hybris.OrderShipmentPackageItem(NOLOCK)
                       WHERE    ( ChangedByUser = 'GP-2083'
                                  AND ChangedByApplication IN (
                                  'GP-2083 Missing Shipment Items in RFL',
                                  'GP-2083 Partially Missing Shipment Items in RFO' )
                                  AND OrderShipmentPackageItemID > 2147483648
                                )
                     )
            SELECT  COUNT(*) --Should not be any
            FROM    CTE
            WHERE   rn > 1;





		/* Test 3: Getting Count of Sequency Loaded in RFO */
		
DECLARE @Startdate DATE = '01/01/2016';
DECLARE @Loaddate DATE = '05/01/2016';
          SELECT    COUNT(*) TotalSequencelyCount ,
                    osi.ChangedByApplication
          FROM      RFOperations.Hybris.OrderShipmentPackageItem(NOLOCK) osi
		  JOIN RFOperations.Hybris.OrderItem(NOLOCK) oi ON oi.OrderItemID = osi.OrderItemId		  
          WHERE   EXISTS (SELECT 1 FROM RFOperations.hybris.orders(NOLOCK) ro WHERE ro.OrderID=oi.OrderId
		  AND ro.CompletionDate>=@Startdate AND ro.CompletionDate<@Loaddate)
		  AND   ( osi.ChangedByUser = 'GP-2083'
                      AND osi.ChangedByApplication IN (
                      'GP-2083 Missing Shipment Items in RFL',
                      'GP-2083 Partially Missing Shipment Items in RFO' )
                      AND OrderShipmentPackageItemID > 2147483648
                    )
          GROUP BY  osi.ChangedByApplication;



		/*  In TST4 
		 TotalSequencelyCount	ChangedByApplication
			2401937					GP-2083 Missing Shipment Items in RFL
			163412					GP-2083 Partially Missing Shipment Items in RFO 


			STG2
			TotalSequencelyCount	ChangedByApplication
					4286	GP-2083 Partially Missing Shipment Items in RFO
					304356	GP-2083 Missing Shipment Items in RFL                                         ********/

/* Test 4: checking Inserted Squencial Number is Correct or Not */


DECLARE @Startdate DATE = '01/01/2016';
DECLARE @Loaddate DATE = '05/01/2016';
SELECT  COUNT(*)-- Should not be any .
FROM    RFOperations.Hybris.OrderShipmentPackageItem(NOLOCK) osi
        JOIN RFOperations.Hybris.OrderItem(NOLOCK) oi ON oi.OrderItemID = osi.OrderItemId
WHERE   EXISTS ( SELECT 1
                 FROM   RFOperations.Hybris.Orders(NOLOCK) ro
                 WHERE  ro.OrderID = oi.OrderId
                        AND ro.CompletionDate >= @Startdate
                        AND ro.CompletionDate < @Loaddate )
        AND -- comment for historical records.
        ( ( osi.ChangedByUser = 'GP-2083'
            AND osi.ChangedByApplication IN (
            'GP-2083 Missing Shipment Items in RFL',
            'GP-2083 Partially Missing Shipment Items in RFO' )
            AND osi.OrderShipmentPackageItemID < 2147483648
          )
          OR ( osi.ChangedByUser = 'GP-2083'
               AND osi.ChangedByApplication IN (
               'GP-2083 Missing Shipment Items in RFO' )
               AND osi.OrderShipmentPackageItemID > 2147483648
             )
          OR ( osi.ChangedByUser = 'GP-2083'
               AND osi.ChangedByApplication NOT IN (
               'GP-2083 Missing Shipment Items in RFL',
               'GP-2083 Partially Missing Shipment Items in RFO',
               'GP-2083 Batch Update' )
               AND osi.OrderShipmentPackageItemID > 2147483648
             )
        );


 -- Provided By Paul.
					
					
					
					/* Details of Sequence Object */
					
                    DECLARE @Startdate DATE = '01/01/2016';
                    --DECLARE @Loaddate DATE = '05/01/2016';
					DECLARE @Loaddate DATE = GETDATE();
                    SELECT  COUNT(*) Counts ,
                            osi.ChangedByApplication ,
                            osi.ChangedByUser ,
                            CAST(osi.ServerModifiedDate AS DATE) ModifiedDate
                    FROM    RFOperations.Hybris.OrderShipmentPackageItem(NOLOCK) osi
                            JOIN RFOperations.Hybris.OrderItem(NOLOCK) oi ON oi.OrderItemID = osi.OrderItemId
                    WHERE   EXISTS ( SELECT 1
                                     FROM   RFOperations.Hybris.Orders(NOLOCK) ro
                                     WHERE  ro.OrderID = oi.OrderId
                                            AND ro.CompletionDate >= @Startdate
                                            AND ro.CompletionDate < @Loaddate )
                            AND ---Comment to get historical data.
                            OrderShipmentPackageItemID >= 2147483648
                            AND OrderShipmentPackageItemID <= 8796093056979							
                    GROUP BY osi.ChangedByApplication ,
                            osi.ChangedByUser ,
                            CAST(osi.ServerModifiedDate AS DATE);



	

--=======================================================================================
--					II-1831 Test order identification --Sowshilya
--========================================================================================


--=======================================================================================
--				(GP-2156) Wrong order statuses – batch update
--========================================================================================




--=======================================================================================
--					II-1837  DoNotShip flag for orders without TrackingNumber
--========================================================================================

