  --====================================================
  -- TEST : ORDER ITEMS 
  --==================================================

    --CHECKING IF ANY ORDERITEM MISSING IN RFO FROM RFL.
 DECLARE @Startdate DATE = '01/01/2016'
 DECLARE @Loaddate DATE = '05/01/2016'

 SELECT COUNT(*) [TOTAL MISSING ORDERITEMS]
  FROM   RodanFieldsLive.dbo.Orders o
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID = o.OrderID
                                                    
        JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderID = o.OrderID
        JOIN RFOperations.Hybris.Orders ho ON ho.OrderID = oc.OrderID
        JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
 WHERE  NOT EXISTS ( SELECT 1
                     FROM   RFOperations.Hybris.OrderItem roi
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
FROM    RFOperations.Hybris.OrderItem oi
        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = oi.OrderId
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
         FROM   [RodanFieldsLive].[dbo].[OrderShipments] os
                JOIN [RodanFieldsLive].[dbo].OrderCustomers oc ON oc.OrderID = os.OrderID
                JOIN [RodanFieldsLive].[dbo].Orders o ON o.OrderID = os.OrderID
                JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = os.OrderID
                LEFT JOIN RFOperations.Hybris.OrderShipment ros ON ros.OrderShipmentID = os.OrderShipmentID
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
 FROM   [RodanFieldsLive].[dbo].[OrderShipments] os
        JOIN [RodanFieldsLive].[dbo].OrderCustomers oc ON oc.OrderID = os.OrderID
        JOIN [RodanFieldsLive].[dbo].Orders o ON o.OrderID = os.OrderID
        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = os.OrderID
        JOIN RFOperations.Hybris.OrderShipment ros ON ros.OrderShipmentID = os.OrderShipmentID
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
     FROM   RFOperations.Cleanup.OrderShipments ros
            JOIN RodanFieldsLive..OrderShipments os ON ros.OrderShipmentID = os.OrderShipmentID
            JOIN RFOperations.Hybris.Orders o ON o.OrderID = os.OrderID
            LEFT JOIN RFOperations.Hybris.OrderShippingAddress ross ON ross.OrderShippingAddressID = os.OrderShipmentID
     WHERE  ross.OrderShippingAddressID IS NULL; 
       
		-- CHECKING THE COUNTS OF NEWLY INSERTED ORDERSHIPMENT ADDRESS IN RFO , TEST CASE NO 9 AND 10

		   
		     DECLARE @Startdate DATE = '01/01/2016';
             --DECLARE @Loaddate DATE = '05/01/2016';
			 DECLARE @loadate DATE =GETDATE()
             SELECT COUNT(*) [Total Inserted Address] ,
                    ross.ChangedByApplication ,
                    ross.ChangedByUser
             FROM   RFOperations.Cleanup.OrderShipments ros
                    JOIN RodanFieldsLive..OrderShipments os ON ros.OrderShipmentID = os.OrderShipmentID
                    JOIN RFOperations.Hybris.Orders o ON o.OrderID = os.OrderID
                    JOIN RFOperations.Hybris.OrderShippingAddress ross ON ross.OrderShippingAddressID = os.OrderShipmentID
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
                        FROM    RodanFieldsLive.dbo.Orders o
                                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID = o.OrderID
                                JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
                                JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderID = o.OrderID
                                JOIN RodanFieldsLive.dbo.OrderShipmentItems osi ON osi.OrderItemID = oi.OrderItemID
                                LEFT JOIN RFOperations.Hybris.OrderShipmentPackageItem rsi ON rsi.OrderItemId = oi.OrderItemID
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
                                 FROM   RFOperations.Hybris.OrderItem rt
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
                                 FROM   RFOperations.Hybris.OrderItem rt
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
                                INNER JOIN RFOperations.Hybris.Orders o ON rflo.OrderID = o.OrderNumber
                                INNER JOIN RodanFieldsLive.dbo.OrderCustomers (NOLOCK) oc ON rflo.OrderID = oc.OrderID
                                                              AND rflo.StartDate >= @Startdate
                                                              AND rflo.StartDate < @Loaddate
                                                              AND rflo.OrderStatusID IN (
                                                              4, 7 )
                                                              AND rflo.OrderTypeID NOT IN (
                                                              4, 5, 9 )
                                INNER JOIN RodanFieldsLive.dbo.OrderItems (NOLOCK) oi ON oc.OrderCustomerID = oi.OrderCustomerID
                                INNER JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderID = rflo.OrderID
                                INNER JOIN RodanFieldsLive.dbo.OrderShipmentItems osi ON osi.OrderShipmentID = os.OrderShipmentID
                                LEFT JOIN RodanFieldsLive.dbo.OrderShipmentItems ossi ON ossi.OrderItemID = oi.OrderItemID
                       WHERE    ossi.OrderShipmentItemID IS NULL
                     )
           
										
          SELECT    RFL.* ,
                    RFO.*
          FROM      RFOperations.Hybris.OrderShipmentPackageItem RFO
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
            INNER JOIN RFOperations.Hybris.OrderItem ri ON ri.OrderItemID = oi.OrderItemID
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
            INNER JOIN RFOperations.Hybris.OrderItem ri ON ri.OrderItemID = oi.OrderItemID
            LEFT JOIN RFOperations.Hybris.OrderShipmentPackageItem (NOLOCK) osi ON osi.OrderItemId = oi.OrderItemID
  WHERE     o.CompletionDate >= @Startdate
            AND o.CompletionDate <= @Loaddate
            AND --comment to get historical records.
            osi.OrderShipmentPackageItemID IS NULL
            AND oi.Quantity <> 0 ---Quantity Zeros.
            AND EXISTS ( SELECT 1
                         FROM   RodanFieldsLive.dbo.OrderLog a
                         WHERE  a.OrderID = rflo.OrderID
                                AND a.[Description] = 'USA FF Export' )
								--AND rflo.OrderID IN (SELECT a.orderID FROM RFOperations.Hybris.OrderItem a
								--GROUP BY  a.OrderID
								--HAVING COUNT(a.OrderId)=1)							
            AND oc.OrderCustomerID NOT IN (
            SELECT  a.OrderCustomerID
            FROM    RodanFieldsLive.dbo.OrderItems a
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
           FROM     RFOperations.Hybris.OrderShipmentPackageItem osi
                    JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID = osi.OrderItemId
           WHERE    osi.ChangedByUser LIKE '%GP%'-- All orders=2691194
                    --AND osi.ChangedByApplication <> 'GP-2083 Batch Update'
                    AND EXISTS ( SELECT 1
                                 FROM   RFOperations.Hybris.Orders o
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
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = ro.OrderStatusID
		JOIN RFOperations.Hybris.OrderShipmentPackageItem osi ON osi.OrderID = ro.OrderID
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
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = ro.OrderStatusID
		JOIN RFOperations.Hybris.OrderShipmentPackageItem osi ON osi.OrderID = ro.OrderID		
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
		 SELECT COUNT(*) FROM RFOperations.Hybris.OrderItem 
		 WHERE ChangedByApplication LIKE'%GP%'
		 GROUP BY OrderId,ProductID,Quantity
		 HAVING COUNT(*)>1

		 --CHECKING IN ORDERSHIPMENT TABLE LOADED BY GP-2083
		 SELECT COUNT(*) FROM RFOperations.Hybris.OrderShipment
		 WHERE ChangedByApplication LIKE'%GP%'
		 GROUP BY OrderID--,ShippingCost,HandlingCost
		 HAVING COUNT(*)>1

		 -- CHECKING IN ORDERSHIPMENT ADDRESS TABLE LOADED BY GP-2083
		 SELECT COUNT(*) FROM RFOperations.Hybris.OrderShippingAddress 
		 WHERE ChangedByApplication LIKE '%GP%'
		 GROUP BY OrderID
		 HAVING COUNT(*)>1

		 --CHECKING IN ORDERSHIPMENTPACKAGEITEM TABLE LOADED BY GP-2083, BULK AND BATCH.
		 SELECT COUNT(*),OrderID FROM RFOperations.Hybris.OrderShipmentPackageItem 
		 WHERE ChangedByApplication LIKE '%GP%'
		 GROUP BY OrderID,OrderItemId
		 HAVING COUNT(*)>1

		/* Orders having 2 shipment even for One Item with one qnty. in RFL 
		 --OrderID 19496291
	  Legacy Duplicate Issues in OrderShipment for an Order */


    SELECT  COUNT(ct) RecordsCount ,
            ct AS [Counts]
    FROM    ( SELECT    COUNT(*) ct ,
                        OrderShipmentID ,
                        OrderItemID
              FROM      RodanFieldsLive.dbo.OrderShipmentItems
              WHERE     QtyShipped = 1
                       -- AND ISNULL(TrackingNumber, '') <> ''
              GROUP BY  OrderShipmentID ,
                        OrderItemID
              HAVING    COUNT(*) > 1
            ) t
    GROUP BY t.ct;


	--*****************************************************************
	---- RFO ISSUES in Duplicate OrdershipmentItem for historical data
	--****************************************************************

		SELECT COUNT(DISTINCT orderItemId) [Total Item Count] FROM (
       SELECT   COUNT(*) Cnt, osi.OrderItemId ,
                osi.Quantity ,
                osi.ShipNumber ,
                osi.ShipDate ,
                osi.modifiedDate ,
                osi.OrderID ,
                osi.OrderShipmentID
       FROM     RFOperations.Hybris.OrderShipmentPackageItem osi
                JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = osi.OrderID
       GROUP BY osi.OrderItemId ,
                osi.Quantity ,
                osi.ShipNumber ,
                osi.ShipDate ,
                osi.modifiedDate ,
                osi.OrderID ,
                osi.OrderShipmentID
				HAVING COUNT(*)>1) t

           

			



				 /* Checking If any Sequence been duplicates*/
				;
          WITH  CTE
                  AS ( SELECT   OrderShipmentPackageItemID ,
                                ROW_NUMBER() OVER ( PARTITION BY OrderShipmentPackageItemID ORDER BY OrderID ) rn
                       FROM     RFOperations.Hybris.OrderShipmentPackageItem
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
          FROM      RFOperations.Hybris.OrderShipmentPackageItem osi
		  JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID = osi.OrderItemId		  
          WHERE   EXISTS (SELECT 1 FROM RFOperations.hybris.orders ro WHERE ro.OrderID=oi.OrderId
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
FROM    RFOperations.Hybris.OrderShipmentPackageItem osi
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID = osi.OrderItemId
WHERE   EXISTS ( SELECT 1
                 FROM   RFOperations.Hybris.Orders ro
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
                    FROM    RFOperations.Hybris.OrderShipmentPackageItem osi
                            JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID = osi.OrderItemId
                    WHERE   EXISTS ( SELECT 1
                                     FROM   RFOperations.Hybris.Orders ro
                                     WHERE  ro.OrderID = oi.OrderId
                                            AND ro.CompletionDate >= @Startdate
                                            AND ro.CompletionDate < @Loaddate )
                            AND ---Comment to get historical data.
                            OrderShipmentPackageItemID >= 2147483648
                            AND OrderShipmentPackageItemID <= 8796093056979							
                    GROUP BY osi.ChangedByApplication ,
                            osi.ChangedByUser ,
                            CAST(osi.ServerModifiedDate AS DATE);



	



        
		/*Test 16: Checking ETL Process If there is any Impact with Code modification Using ChangeByUser Tag in Except*/


		

/*   Picking Ordershimpment and OrdershipmentPackageItem added  Orders in RFO */


          IF OBJECT_ID('Tempdb..#t') IS NOT NULL
            DROP TABLE #t;      
          SELECT DISTINCT
                    ro.OrderID ,
                    ro.OrderStatusID ,
                    ro.OrderTypeID
         -- INTO      #t
          FROM      RFOperations.Hybris.Orders ro
                    JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.OrderID
                                                         AND o.OrderTypeID NOT IN (
                                                         4, 5, 9 )
                    JOIN RFOperations.Hybris.OrderShipment sp ON sp.OrderID = ro.OrderID
                    JOIN RFOperations.Hybris.OrderShipmentPackageItem si ON si.OrderID = ro.OrderID
          WHERE     --si.ChangedByUser = 'GP-2083' AND
		   sp.ChangedByApplication LIKE '%GP%'
          GROUP BY  ro.OrderID ,
                    ro.OrderStatusID ,
                    ro.OrderTypeID
          HAVING    ro.OrderStatusID IN (  4 );
		  SELECT * FROM #t
		 
/*   filtering Orders within 60days which will pick  within except sp in ETL */


	 IF OBJECT_ID('Tempdb..#Temp_NewOrders') IS NOT NULL
            DROP TABLE #Temp_NewOrders;  
          DECLARE @Datedelta DATETIME;
          SELECT    @Datedelta = DATEADD(D, -60, MAX(CompletionDate))
          FROM      RFOperations.Hybris.Orders;		
          SELECT  TOP 9  ro. OrderID ,
                    ro.OrderStatusID,CAST('' AS NVARCHAR(25)) AS [Scenario],CAST('' AS NVARCHAR(25)) AS [Expected Result]
     INTO      #Temp_NewOrders
          FROM      RFOperations.ETL.synRFLiveOrders AS a ( NOLOCK )
		  JOIN RFOperations.Hybris.orders ro ON ro.OrderID=a.OrderID
          WHERE     a.StartDate >= @Datedelta                  
                    AND NOT EXISTS ( SELECT OrderID
                                     FROM   RFOperations.ETL.StagingOrdersRaw AS c ( NOLOCK )
                                     WHERE  a.OrderID = c.OrderID )
                    AND EXISTS ( SELECT 1
                                 FROM   #t t
                                 WHERE  t.OrderID = a.OrderID );



SELECT  o.OrderID ,
        o.OrderStatusID ,
        oi.CommissionableTotal ,
       s.OrderStatusID [Staging OrderStatus],
	   s.OrderItemCV [Staging CV]
FROM    RodanFieldsLive.dbo.Orders o
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
		LEFT JOIN RFOperations.etl.StagingOrders s ON s.OrderID=o.OrderID
WHERE   o.OrderID IN ( 19321035, 19334705, 22364264, 22364425, 22367701,
                       22367835, 19321035, 19334705, 19370737, 22564707 );


SELECT  o.OrderID ,
        o.OrderStatusID ,
        oi.CommissionableTotal ,
       s.OrderStatusID [RFO OrderStatus],
	   ri.CV [RFO CV]
FROM    RodanFieldsLive.dbo.Orders o
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
		 JOIN RFOperations.hybris.orders s ON s.OrderID=o.OrderID
		 JOIN RFOperations.Hybris.OrderItem ri ON ri.OrderId=s.OrderID
WHERE   o.OrderID IN ( 19321035, 19334705, 22364264, 22364425, 22367701,
                       22367835, 19321035, 19334705, 19370737, 22564707 );



 INSERT INTO #Temp_NewOrders
        ( OrderID ,
          OrderStatusID ,
          Scenario ,
          [Expected Result]
        )
 VALUES ( 19370737 , -- OrderID - bigint
          4 , -- OrderStatusID - bigint
          'newly added shipment' , -- Scenario - 
          '' -- Expected Result - 
        );


 UPDATE #Temp_NewOrders
 SET    Scenario = 'Newly  OrdershipmentItem'
 WHERE  OrderID <> 19370737;

 UPDATE RodanFieldsLive.dbo.OrderItems
 SET    CommissionableTotal = 500
--UPDATE #Temp_NewOrders
--SET Scenario='items',[Expected Result]='Status Shipped'
 WHERE  OrderCustomerID IN ( SELECT OrderCustomerID
                             FROM   RodanFieldsLive.dbo.OrderCustomers
                             WHERE  OrderID IN ( 19321035, 19334705 ) );
--WHERE OrderID IN (22364264,22364425,22367701,
--22367835))
--WHERE  OrderID IN (22358039,22359343))


 UPDATE #Temp_NewOrders
 SET    Scenario = 'Modifying Orderitem  ' ,
        [Expected Result] = 'Status should be Shipped'
 WHERE  OrderID IN ( 22364264, 22364425, 22367701, 22367835 );

 UPDATE #Temp_NewOrders
 SET    Scenario = ' CV newly added Shipment' ,
        [Expected Result] = 'Status should be Shipped'
 WHERE  OrderID IN ( 19321035, 19334705 );

 UPDATE RodanFieldsLive.dbo.Orders
 SET    OrderStatusID = 1
--UPDATE #Temp_NewOrders
--SET Scenario='Modifying items CV ',[Expected Result]='Status should be Shipped'
 WHERE  OrderID IN ( 19370737, 22564707 );


DECLARE @total INT; 
DECLARE @ite INT= 1;

SET @total = ( SELECT   COUNT(*)
               FROM     #Temp_NewOrders
             );
WHILE @total <> 0
    BEGIN
        SELECT  * ,
                ROW_NUMBER() OVER ( PARTITION BY 1 ORDER BY OrderID ) AS rn
        FROM    #Temp_NewOrders
        WHERE    ROW_NUMBER() OVER ( PARTITION BY 1 ORDER BY OrderID ) = @total;
        SET @total = @total - 1;
    END;


/*  : MOdifing the status of Orders to Cancelled in RFL */

SELECT  TOP 2 * FROM #Temp_NewOrders WHERE OrderStatusID=2

SELECT  TOP 10 * INTO #tb FROM #Temp_NewOrders WHERE OrderStatusID=4

SELECT * FROM #tb 


UPDATE #tb 
SET Scenario='Cancelled'
WHERE OrderID IN (22183929,22183931,22183938)

UPDATE #tb 
SET Scenario='Submitted'
WHERE OrderID IN (22183940,22183963,22183965,22183970)

UPDATE #tb 
SET Scenario='Shipped'
WHERE OrderID IN (22183972,22183995,22183997)


SELECT * FROM #tb



--- STG2 Sources 
BEGIN TRAN

          UPDATE    RodanFieldsLive.dbo.Orders
          SET       OrderStatusID = 5 --cancelled.
		  --SELECT orderstatusID FROM RodanFieldsLive.dbo.orders
          WHERE     OrderID IN (22183929,22183931,22183938)

		   UPDATE    RodanFieldsLive.dbo.Orders
          SET       OrderStatusID = 4 --cancelled.
          WHERE     OrderID  IN (22183940,22183963,22183965,22183970)
		  
		   UPDATE    RodanFieldsLive.dbo.Orders
          SET       OrderStatusID = 9 --cancelled.
          WHERE     OrderID  IN (22183972,22183995,22183997)

		IF @@ERROR <>0
		BEGIN
		PRINT ' Rolled back'
		ROLLBACK TRAN
		END
        
	ELSE 
	BEGIN
	PRINT 'Committed'
	COMMIT TRAN
	END 
-- Test 17 pending error  to Cancelled.

          SELECT    ro.OrderID ,
                    lo.OrderStatusID RFL_Status ,
                    so.OrderStatusID AS Stg_Status ,
                    ro.OrderStatusID AS RFO_Status
         FROM      RFOperations.Hybris.Orders ro
                    JOIN RodanFieldsLive.dbo.Orders lo ON lo.OrderID = ro.OrderID
               LEFT   JOIN RFOperations.ETL.StagingOrders so ON so.OrderID = lo.OrderID
          WHERE     lo.OrderID IN (22183929,22183931,22183938,22183940,22183963,22183965,22183970,22183972,22183995,22183997)
                    AND ro.OrderStatusID <> ISNULL(so.OrderStatusID,0)

		
-- Test 18: Submitted to Cancelled.

          SELECT    ro.OrderID ,
                    lo.OrderStatusID RFL_Status ,
                    so.OrderStatusID AS Stg_Status ,
                    ro.OrderStatusID AS RFO_Status
          FROM      RFOperations.Hybris.Orders ro
                    JOIN RodanFieldsLive.dbo.Orders lo ON lo.OrderID = ro.OrderID
                   LEFT JOIN RFOperations.ETL.StagingOrders so ON so.OrderID = lo.OrderID
          WHERE     ro.OrderID IN (22183929,22183931,22183938,22183940,22183963,22183965,22183970,22183972,22183995,22183997)
                    AND ro.OrderStatusID <>  ISNULL(so.OrderStatusID,0)





------------------------------------------------------------
/* Running Except ETL SP */

 SELECT COUNT(*) FROM RFOperations.ETL.StagingOrders
 --2536 Time : 1M50Sec
 TRUNCATE TABLE RFOperations. ETL.StagingOrders



 USE RFOperations
 GO
 
 EXECUTE  [ETL].[USPExceptStagingOrder]  
 ------------------------------------------------------------

 --***************************************************************************


/* Deleting Loaded records from Ordershipmentpackage table and Ordershipment table for revalidation */
-- Dependency On OrderStatus GP_2082 ( Need to revert OrderStatus)

          DECLARE @count INT;
          BEGIN TRANSACTION;
          SET NOCOUNT ON; 

          SET @count = ( SELECT COUNT(*)
                         FROM   RFOperations.Hybris.OrderShipmentPackageItem
                         WHERE  ChangedByUser = 'GP-2083'
                       );
          IF @count > 0
            BEGIN
                DELETE  RFOperations.Hybris.OrderShipmentPackageItem
                WHERE   ChangedByUser = 'GP-2083';
            END; 
          ELSE
            PRINT 'NO records Found  to be loaded OrderShipmentPackage';


          SET @count = ( SELECT COUNT(*)
                         FROM   RFOperations.Hybris.OrderShipment
                         WHERE  ChangedByUser = 'GP-2083'
                       );
          IF @count > 0
            BEGIN
                DELETE  RFOperations.Hybris.OrderShipment
                WHERE   ChangedByUser = 'GP-2083';
            END; 
          ELSE
            PRINT ' NO records Found  to be loaded Ordershipment';
  

          IF @@ERROR <> 0
            BEGIN
                ROLLBACK TRANSACTION;
                PRINT ' Transaction Rolled Back';
            END;
          ELSE
            BEGIN 
                COMMIT TRANSACTION;
                PRINT ' Transaction Committed Successfully';
            END; 



	--***************************************************************	         

 /* Test 5: Checking Records not been loaded from Clean up Staging tables.*/



 -- Delta Orders from Jan 2016 to April 2016.
   DECLARE @Startdate DATE = '01/01/2016';
                    DECLARE @Loaddate DATE = '05/01/2016';

 DECLARE @stg INT ,
            @loded INT;
          SET @stg = ( SELECT   COUNT(*)
                       FROM     RFOperations.CleanUp.OrderShipmentPackageItems osi
					   JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID=osi.OrderItemID
					   WHERE EXISTS (SELECT 1 FROM RFOperations.hybris.orders ro 
					   WHERE ro.OrderID=oi.OrderId AND  ro.CompletionDate >= @Startdate
                                            AND ro.CompletionDate < @Loaddate )
                     );
          SET @loded = ( SELECT COUNT(*)
                         FROM   RFOperations.Hybris.OrderShipmentPackageItem osi
						  JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID=osi.OrderItemID
                         WHERE EXISTS (SELECT 1 FROM RFOperations.hybris.orders ro 
					   WHERE ro.OrderID=oi.OrderId AND  ro.CompletionDate >= @Startdate
                                            AND ro.CompletionDate < @Loaddate ) 
											AND  osi.ChangedByUser LIKE '%GP%'
                       );
          IF @stg <> @loded
            SELECT  'Staging and Target Count Not matching' AS Result ,
                    @stg AS STGCount ,
                    @loded AS TargetCount ,
                    @stg - @loded AS Diff;
          ELSE
            SELECT  ' Staging and Target Count is Matching';
 GO

 ---Creating Index in clean up table to make it faster.
 CREATE CLUSTERED INDEX cls ON RFOperations.Cleanup.OrderShipmentPackageItems(OrderItemID)

   IF OBJECT_ID('Tempdb..#temp') IS NOT NULL
            DROP TABLE #temp;

	/* Test 6: Stage to Target Minus */

	 DECLARE @Startdate DATE = '01/01/2016';
                    DECLARE @Loaddate DATE = '05/01/2016';
          SELECT   osi. OrderShipmentPackageItemID ,
                   osi.OrderItemID ,
                   osi.Quantity ,
                   osi.TrackingNumber ,
                   osi.ShipNumber ,
                   osi.ShipDate ,
                   osi.ServerModifiedDate ,
                   osi.ChangedByApplication ,
                   osi.ChangedByUser ,
                   osi.OrderID ,
                   osi.ShipStatus ,
                   osi.OrderShipmentID ,
                   osi.TrackingURL
          INTO      #temp
          FROM      RFOperations.CleanUp.OrderShipmentPackageItems osi
		  JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID=osi.OrderItemID
					   WHERE EXISTS (SELECT 1 FROM RFOperations.hybris.orders ro 
					   WHERE ro.OrderID=oi.OrderId AND  ro.CompletionDate >= @Startdate
                                            AND ro.CompletionDate < @Loaddate )
          EXCEPT
          SELECT   osi.OrderShipmentPackageItemID ,
                   osi.OrderItemId ,
                   osi.Quantity ,
                   osi.TrackingNumber ,
                   osi.ShipNumber ,
                   osi.ShipDate ,
                   osi.ServerModifiedDate ,
                   osi.ChangedByApplication ,
                   osi.ChangedByUser ,
                   osi.OrderID ,
                   osi.ShipStatus ,
                   osi.OrderShipmentID ,
                   osi.TrackingUrl
          FROM      RFOperations.Hybris.OrderShipmentPackageItem osi
						  JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID=osi.OrderItemID
                         WHERE EXISTS (SELECT 1 FROM RFOperations.hybris.orders ro 
					   WHERE ro.OrderID=oi.OrderId AND  ro.CompletionDate >= @Startdate
                                            AND ro.CompletionDate < @Loaddate ) AND  osi.ChangedByUser LIKE '%GP%'
                       ;

          SELECT    COUNT(DISTINCT OrderID) AS CountNotLoadedInSource
          FROM      #temp;


          SELECT    COUNT(*) CountOfItemExistInOrderItemTable
          FROM      #temp t
          WHERE     EXISTS ( SELECT 1
                             FROM   RFOperations.Hybris.OrderItem oi
                             WHERE  oi.OrderItemID = t.OrderItemID )
			AND NOT EXISTS (SELECT 1 FROM RFOperations.hybris.OrderShipment s
			WHERE s.OrderID=t.OrderID );


/* Test 7: SOURCE To Stage Minus*/


 DECLARE @Startdate DATE = '01/01/2016';
 DECLARE @Loaddate DATE = '05/01/2016';

          IF OBJECT_ID('Tempdb..#t') IS NOT NULL
            DROP TABLE #t;        
          SELECT   osi.OrderShipmentPackageItemID ,
                   osi.OrderItemId ,
                   osi.Quantity ,
                   osi.TrackingNumber ,
                   osi.ShipNumber ,
                   osi.ShipDate ,
                   osi.ServerModifiedDate ,
                   osi.ChangedByApplication ,
                   osi.ChangedByUser ,
                   osi.OrderID ,
                   osi.ShipStatus ,
                   osi.OrderShipmentID ,
                   osi.TrackingUrl INTO #t
          FROM      RFOperations.Hybris.OrderShipmentPackageItem osi
						  JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID=osi.OrderItemID
                         WHERE EXISTS (SELECT 1 FROM RFOperations.hybris.orders ro 
					   WHERE ro.OrderID=oi.OrderId AND  ro.CompletionDate >= @Startdate
                                            AND ro.CompletionDate < @Loaddate ) AND  osi.ChangedByUser LIKE '%GP%'
          EXCEPT
          SELECT   osi. OrderShipmentPackageItemID ,
                   osi.OrderItemID ,
                   osi.Quantity ,
                   osi.TrackingNumber ,
                   osi.ShipNumber ,
                   osi.ShipDate ,
                   osi.ServerModifiedDate ,
                   osi.ChangedByApplication ,
                   osi.ChangedByUser ,
                   osi.OrderID ,
                   osi.ShipStatus ,
                   osi.OrderShipmentID ,
                   osi.TrackingURL          
          FROM      RFOperations.CleanUp.OrderShipmentPackageItems osi
		  JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID=osi.OrderItemID
					   WHERE EXISTS (SELECT 1 FROM RFOperations.hybris.orders ro 
					   WHERE ro.OrderID=oi.OrderId AND  ro.CompletionDate >= @Startdate
                                            AND ro.CompletionDate < @Loaddate )

          SELECT  r.CompletionDate,  *
          FROM      #t t
		  JOIN RFOperations.hybris.orders r ON r.OrderID = t.OrderID

		  
          SELECT    COUNT(*) CountOfItemExistInOrderItemTable
          FROM      #t  t
          WHERE     EXISTS ( SELECT 1
                             FROM   RFOperations.Hybris.OrderItem oi
                             WHERE  oi.OrderItemID = t.OrderItemID )
			AND NOT EXISTS (SELECT 1 FROM RFOperations.hybris.OrderShipment s
			WHERE s.OrderID=t.OrderID );


		

			
			

--+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 -- Historical Orders till Today.
---++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		  
		  DECLARE @stg INT ,
            @loded INT;
          SET @stg = ( SELECT   COUNT(*)
                       FROM     RFOperations.CleanUp.OrderShipmentPackageItems
                     );
          SET @loded = ( SELECT COUNT(*)
                         FROM   RFOperations.Hybris.OrderShipmentPackageItem
                         WHERE  ChangedByUser LIKE '%GP%'
                       );
          IF @stg <> @loded
            SELECT  'Staging and Source Count Not matching' AS Result ,
                    @stg AS STGCount ,
                    @loded AS SourceCount ,
                    @stg - @loded AS Diff;
          ELSE
            SELECT  ' Staging and Source Count is Matching';
 GO



          IF OBJECT_ID('Tempdb..#temp') IS NOT NULL
            DROP TABLE #temp;

	/* Test 6: Stage to Source Minus */
          SELECT    OrderShipmentPackageItemID ,
                    OrderItemID ,
                    Quantity ,
                    TrackingNumber ,
                    ShipNumber ,
                    ShipDate ,
                    ServerModifiedDate ,
                    ChangedByApplication ,
                    ChangedByUser ,
                    OrderID ,
                    ShipStatus ,
                    OrderShipmentID ,
                    TrackingURL
          INTO      #temp
          FROM      RFOperations.CleanUp.OrderShipmentPackageItems
          EXCEPT
          SELECT    OrderShipmentPackageItemID ,
                    OrderItemId ,
                    Quantity ,
                    TrackingNumber ,
                    ShipNumber ,
                    ShipDate ,
                    ServerModifiedDate ,
                    ChangedByApplication ,
                    ChangedByUser ,
                    OrderID ,
                    ShipStatus ,
                    OrderShipmentID ,
                    TrackingUrl
          FROM      RFOperations.Hybris.OrderShipmentPackageItem
          WHERE     ChangedByUser LIKE '%GP%';

          SELECT    COUNT(DISTINCT OrderID) AS CountNotLoadedInSource
          FROM      #temp;


          SELECT    COUNT(*) CountOfItemExistInOrderItemTable
          FROM      #temp t
          WHERE     EXISTS ( SELECT 1
                             FROM   RFOperations.Hybris.OrderItem oi
                             WHERE  oi.OrderItemID = t.OrderItemID );
/* Test 7: SOURCE To Stage Minus*/
          IF OBJECT_ID('Tempdb..#t') IS NOT NULL
            DROP TABLE #t;        
          SELECT    OrderShipmentPackageItemID ,
                    OrderItemId ,
                    Quantity ,
                    TrackingNumber ,
                    ShipNumber ,
                    ShipDate ,
                    ServerModifiedDate ,
                    ChangedByApplication ,
                    ChangedByUser ,
                    OrderID ,
                    ShipStatus ,
                    OrderShipmentID ,
                    TrackingUrl
          INTO      #t
          FROM      RFOperations.Hybris.OrderShipmentPackageItem
          WHERE     ChangedByUser LIKE '%GP%'
          EXCEPT
          SELECT    OrderShipmentPackageItemID ,
                    OrderItemID ,
                    Quantity ,
                    TrackingNumber ,
                    ShipNumber ,
                    ShipDate ,
                    ServerModifiedDate ,
                    ChangedByApplication ,
                    ChangedByUser ,
                    OrderID ,
                    ShipStatus ,
                    OrderShipmentID ,
                    TrackingURL
          FROM      RFOperations.CleanUp.OrderShipmentPackageItems;

          SELECT    *
          FROM      #t





			
				
					

	
		    