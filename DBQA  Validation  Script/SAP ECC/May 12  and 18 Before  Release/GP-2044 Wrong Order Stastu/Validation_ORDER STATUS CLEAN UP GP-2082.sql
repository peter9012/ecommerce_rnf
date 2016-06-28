
--20 test Cases Included 

/* BULK VALIDATON OF ORDER STATUS RFO VS RFL*/



--===========================================================================		
-- Checking Updated OrderStatus ChangedByUser=GP-2082 and ServerModified Tag 
--===========================================================================

          DECLARE @Startdate DATE = '01/01/2016';
          DECLARE @Loaddate DATE = '05/01/2016';
	SELECT  COUNT(*) Total ,
        ro.OrderStatusID ,
        ChangedByUser ,
		ro.ChangedByUser,
        CAST(ServerModifiedDate AS DATE) ModifiedDate
FROM    RFOperations.Hybris.Orders ro
        JOIN RodanFieldsLive.dbo.Orders lo ON lo.OrderID = ro.OrderID
WHERE   ro.ChangedByUser LIKE '%GP%'
        AND lo.OrderTypeID NOT IN ( 4, 5, 9 )
		AND ro.CompletionDate>=@Startdate
		AND ro.CompletionDate<@Loaddate
GROUP BY ro.OrderStatusID ,
        ChangedByUser ,
		ro.ChangedByUser,
        CAST(ServerModifiedDate AS DATE); 

				/*	Total	OrderStatusID	ChangedByUser	ModifiedDate
				2281	4	GP-2082	2016-05-15   **/



				--============================================================
				-- CHECKING WETHER ANY GP-2083 ORDERS NOT BEEN SHIPPED STATUS
                --============================================================

				SELECT  ro.OrderID ,
                        ro.OrderStatusID ,
                        osi.OrderShipmentPackageItemID ,
                        osi.OrderItemId ,
                        osi.ChangedByApplication ,
                        osi.ChangedByUser
                FROM    RFOperations.Hybris.Orders ro
                        JOIN RFOperations.Hybris.OrderShipmentPackageItem osi ON osi.OrderID = ro.OrderID
                WHERE   osi.ChangedByApplication LIKE '%GP-2083%'
                        AND ro.OrderStatusID <> 4; --Shipped Order Status.
						---This is Test Orders to validate Order Status should flow and made it Cancelled in RFL .OrderID=22564707
						
                        DECLARE @OrderId INT= 22564707;
                        SELECT  *
                        FROM    RFOperations.Hybris.OrderShipment
                        WHERE   OrderID = @OrderID;
                        SELECT  *
                        FROM    RFOperations.Hybris.OrderShipmentPackageItem
                        WHERE   OrderID = @OrderID;



	
			--============================================================
			-- Details  IF  Clean UP Staging Table OldStatus<>NewStatus 
			--============================================================

SELECT  COUNT(*) Total ,
        ro.OrderStatusID ,
        s.OrderStatusID_Old ,
        ro.ChangedByUser ,
        s.ChangedByUser ChangedByUser_OLD ,
        CAST(ro.ServerModifiedDate AS DATE) ModifiedDate
FROM    RFOperations.Hybris.Orders ro
        JOIN RodanFieldsLive.dbo.Orders lo ON lo.OrderID = ro.OrderID
        JOIN RFOperations.CleanUp.orderstatus s ON s.OrderID = ro.OrderID
WHERE   ro.ChangedByUser LIKE '%GP%'   --- SINCE IT HAS A TAG , WE DON'T NEED DATE FILTER.
        AND lo.OrderTypeID NOT IN ( 4, 5, 9 )
        AND s.OrderStatusID_Old <> ro.OrderStatusID
GROUP BY ro.OrderStatusID ,
        s.OrderStatusID_Old ,
        ro.ChangedByUser ,
        s.ChangedByUser ,
        CAST(ro.ServerModifiedDate AS DATE); 



--====================================================================
	-- Checking If Any Templates been updated with this Update in RFO 
	--Templates in RFL and Orders in RFO 
--====================================================================

SELECT  ro.OrderID ,
        ro.OrderStatusID ,
        ho.OrderStatusID ,
        ho.OrderTypeID ,
        ro.ChangedByUser
FROM    RFOperations.Hybris.Orders ro
        JOIN RodanFieldsLive.dbo.Orders ho ON ro.OrderID = ho.OrderID
WHERE   ChangedByUser LIKE '%GP%'
        AND ho.OrderTypeID IN ( 4, 5,9 );
		
	


		/* Bulk Validation */


SELECT  COUNT(*) /*
     DISTINCT
        ospi.ChangedByUser ,
        o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
        CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		--*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND o.ChangedByUser LIKE '%GP%'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND ( ( b.OrderStatusID IN ( 1, 2, 3, 6, 8 )
                AND o.OrderStatusID <> 1--Pending 
              )
              OR ( b.OrderStatusID IN ( 4, 7 )
                   AND shipstatus.Shipped <> 1
                   AND shipstatus.Pending <> 1
                   AND o.OrderStatusID <> 2 --submitted 
                 )
              OR ( b.OrderStatusID = 5
                   AND o.OrderStatusID <> 3
                 )
              OR ( ( b.OrderStatusID = 4
                     AND shipstatus.Pending = 1
                     AND shipstatus.Shipped = 1
                     AND o.OrderStatusID != 5
                   )
                   OR ( b.OrderStatusID = 7
                        AND shipstatus.Pending = 1
                        AND shipstatus.Shipped = 1
                        AND o.OrderStatusID != 5
                      )
                 )
              OR ( ( b.OrderStatusID = 4
                     AND shipstatus.Shipped = 1
                     AND shipstatus.Pending = 0
                     AND o.OrderStatusID <> 4
                   )
                   OR ( b.OrderStatusID = 7
                        AND shipstatus.Shipped = 1
                        AND shipstatus.Pending = 0
                        AND o.OrderStatusID <> 4
                      )
                   OR ( b.OrderStatusID = 9
                        AND o.OrderStatusID <> 4
                      )
                 )
            );
--ORDER BY OrderStatusID_Now;
                  
                  
                  

SELECT  COUNT(*) ShippedStatusCount /*
         ospi.ChangedByUser ,
        o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
        CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		--*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND o.ChangedByUser LIKE '%GP%'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND ( ( b.OrderStatusID IN ( 4, 7 )
                AND ospi.ChangedByUser = 'GP-2083'
                AND o.OrderStatusID <> 4
              )
              OR ( b.OrderStatusID = 4
                   AND shipstatus.Shipped = 1
                   AND shipstatus.Pending <> 1
                   AND o.OrderStatusID <> 4
                 )
              OR ( b.OrderStatusID = 7
                   AND shipstatus.Shipped = 1
                   AND o.OrderStatusID <> 4
                 )
              OR ( b.OrderStatusID = 4
                   AND o.OrderStatusID <> 4
                 )
              OR ( b.OrderStatusID = 9
                   AND o.OrderStatusID <> 4
                 )
            );
--97,070
						
							/* Partially Submitted Validation */
SELECT  COUNT(*) PartiallyShippedCounts /*
        o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
         CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
			   WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4           
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
			 WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND o.ChangedByUser LIKE '%GP%'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND ( ( b.OrderStatusID = 4
                AND shipstatus.Pending = 1
                AND shipstatus.Shipped = 1
                AND o.OrderStatusID != 5
              )
              OR ( b.OrderStatusID = 7
                   AND shipstatus.Pending = 1
                   AND shipstatus.Shipped = 1
                   AND o.OrderStatusID != 5
                 )
            );

			--Not any records

					   /* Submitted Validation */

SELECT  COUNT(*) SubmittedStatusCounts /*
        o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
          CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
			   WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4           
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
			 WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND o.ChangedByUser LIKE '%GP%'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND ( b.OrderStatusID IN ( 4, 7 )
              AND shipstatus.Shipped = 1
              AND o.OrderStatusID = 2 --submitted 
            );
--8344 

	/* Pending Validation */

SELECT  COUNT(*) PendingStatusCount /*
         o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
        CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		--*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND o.ChangedByUser LIKE '%GP%'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND ( b.OrderStatusID IN ( 1, 2, 3, 6, 8 )
              AND o.OrderStatusID <> 1--Pending 
            );

	  /* Cancelled Validation */
															   
SELECT  COUNT(*) CancelledStatusCount /*
         o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
        CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		--*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND o.ChangedByUser LIKE '%GP%'
        AND ( b.OrderStatusID = 5
              AND o.OrderStatusID != 3
            );

			--OR 

                    
SELECT  COUNT(*) Counts /*
		o.OrderID AS RFO_OrderId ,
        o.OrderStatusID AS RFO_OrderStatus ,
        b.OrderStatusID AS RFL_OrderStatus ,
        shipstatus.Pending ,
        shipstatus.Shipped ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.CompletionDate ,
        o.ServerModifiedDate ,
        o.ChangedByUser 
		--***/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND b.OrderStatusID = 5
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND o.OrderStatusID NOT IN ( 3 )
        AND o.ChangedByUser LIKE '%GP%'
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01';
				

--==============================================
--			ETL PROCESS VALIDATION SCRIPTS
--==============================================



-- Updated RFL to Different OrderStatus using below Query:

--Changing Submitted Orders from RFO  to Cancelled In RFL


IF OBJECT_ID(N'Tempdb..#t') IS NOT NULL
    DROP TABLE #t;
IF OBJECT_ID(N'Tempdb..#t1') IS NOT NULL
    DROP TABLE #t1;
IF OBJECT_ID(N'Tempdb..#t2') IS NOT NULL
    DROP TABLE #t2;
IF OBJECT_ID(N'Tempdb..#t3') IS NOT NULL
    DROP TABLE #t3
	IF OBJECT_ID(N'Tempdb..#t4') IS NOT NULL
    DROP TABLE #t4
	IF OBJECT_ID(N'Tempdb..#t5') IS NOT NULL
    DROP TABLE #t5;

SELECT TOP 2
        OrderID
INTO    #t
FROM    RFOperations.Hybris.Orders
WHERE   OrderStatusID = 2
        AND CompletionDate > DATEADD(MONTH, -1, GETDATE())
ORDER BY ServerModifiedDate DESC;
               

SELECT TOP 10
        ro.OrderID
INTO    #t1
FROM    RFOperations.Hybris.Orders ro
JOIN RodanFieldsLive.dbo.orders lo ON lo.OrderID=ro.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem sp ON sp.OrderID = ro.OrderID
WHERE  ro.OrderStatusID <> 4
        AND ro.CompletionDate > DATEADD(dd, -42, GETDATE())
        AND sp.ChangedByUser LIKE '%GP%'
ORDER BY ro.ServerModifiedDate DESC;
 
SELECT TOP 2
        ro.OrderID
INTO    #t2
FROM    RFOperations.Hybris.Orders ro
JOIN RodanFieldsLive.dbo.orders lo ON lo.OrderID=ro.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem sp ON sp.OrderID = ro.OrderID
WHERE  ro.OrderStatusID = 4
        AND ro.CompletionDate > DATEADD(dd, -42, GETDATE())
        AND sp.ChangedByUser LIKE '%GP%'
ORDER BY ro.ServerModifiedDate;
SELECT TOP 2
        ro.OrderID
INTO    #t4
FROM    RFOperations.Hybris.Orders ro
JOIN RodanFieldsLive.dbo.orders lo ON lo.OrderID=ro.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem sp ON sp.OrderID = ro.OrderID
WHERE  ro.OrderStatusID = 2
        AND ro.CompletionDate > DATEADD(dd, -42, GETDATE())
        AND sp.ChangedByUser LIKE '%GP%'
ORDER BY ro.ServerModifiedDate;
SELECT TOP 2
        ro.OrderID,ro.OrderStatusID
INTO    #t5
FROM    RFOperations.Hybris.Orders ro
JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID AND os.ChangedByUser LIKE 'GP%'       
WHERE ro.CompletionDate > DATEADD(dd, -59, GETDATE())
AND ro.OrderStatusID IN (2,4)     
ORDER BY ro.ServerModifiedDate;

SELECT * FROM RFOperations.Hybris.OrderItem WHERE OrderID IN (22718932 )                

SELECT  OrderID
INTO    #t3
FROM    #t
UNION
SELECT  OrderID
FROM    #t1
UNION
SELECT  OrderID
FROM    #t2
UNION
SELECT  OrderID
FROM    #t4
UNION
SELECT  OrderID
FROM    #t5
;

SELECT OrderID FROM  #t3

 
BEGIN TRANSACTION;

UPDATE  oi
SET     oi.CommissionableTotal = 100
FROM    RodanFieldsLive.dbo.Orders o
JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID=o.OrderID
JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID=oc.OrderCustomerID
WHERE  o. OrderID IN (22718932);


IF @@ERROR <> 0
    BEGIN
        ROLLBACK TRANSACTION;
        PRINT ' Transaction Rolled back';
    END;
ELSE
    BEGIN
        COMMIT TRANSACTION;
        PRINT ' Transaction Committed Successfully';
    END;

	-- Attected Result to Retrieve from ETL 

SELECT DISTINCT ro.OrderID ,
        ro.ServerModifiedDate ,ro.CompletionDate,
        ro.OrderStatusID AS RFO ,
        lo.OrderStatusID AS RFL ,
        so.OrderStatusID AS STG,
		oi.OrderItemID,oi.CommissionableTotal,
		so.OrderItemID,so.OrderItemCV[Stage CV],
		oss.ChangedByUser AS OrdershipmentChangedByUser,
		spi.ChangedByUser AS OrdershipmentItemChangedByUser,
		os.DateShipped,v.*
FROM    RFOperations.Hybris.Orders ro
        JOIN RodanFieldsLive.dbo.Orders lo ON lo.OrderID = ro.OrderID
		JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID=lo.OrderID
		JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID=oc.OrderCustomerID
        LEFT JOIN RFOperations.ETL.StagingOrders so ON so.OrderID = ro.OrderID
		LEFT JOIN RFOperations.dbo.vwOrderShipmentStatus v ON v.OrderID=lo.OrderID
		LEFT JOIN RFOperations.Hybris.OrderShipmentPackageItem spi ON spi.OrderID = lo.OrderID
		LEFT JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderID = lo.OrderID
		LEFT JOIN RFOperations.hybris.OrderShipment oss ON oss.OrderID = lo.OrderID
WHERE   ro.OrderID IN (22718932 );
--SELECT MAX(ServerModifiedDate) FROM RFOperations.hybris.orders
					
--SELECT COUNT(*) FROM RFOperations.etl.StagingOrders

		--SELECT * FROM RodanFieldsLive.dbo.OrderStatus

		/* Validating in a Bulk after ETL Process*/		

SELECT  COUNT(*) /*
     DISTINCT
        ospi.ChangedByUser ,
        o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
        CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		--*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND ( ( b.OrderStatusID IN ( 1, 2, 3, 6, 8 )
                AND o.OrderStatusID <> 1--Pending 
              )
              OR ( b.OrderStatusID IN ( 4, 7 )
                   AND shipstatus.Shipped <> 1
                   AND shipstatus.Pending <> 1
                   AND o.OrderStatusID <> 2 --submitted 
                 )
              OR ( b.OrderStatusID = 5
                   AND o.OrderStatusID <> 3
                 )
              OR ( ( b.OrderStatusID = 4
                     AND shipstatus.Pending = 1
                     AND shipstatus.Shipped = 1
                     AND o.OrderStatusID <> 5
                   )
                   OR ( b.OrderStatusID = 7
                        AND shipstatus.Pending = 1
                        AND shipstatus.Shipped = 1
                        AND o.OrderStatusID <> 5
                      )
                 )
              OR ( ( b.OrderStatusID = 4
                     AND shipstatus.Shipped = 1
                     AND shipstatus.Pending = 0
                     AND o.OrderStatusID <> 4
                   )
                   OR ( b.OrderStatusID = 7
                        AND shipstatus.Shipped = 1
                        AND o.OrderStatusID <> 4
                      )
                   OR ( b.OrderStatusID = 9
                        AND o.OrderStatusID <> 4
                      )
                 )
            );
                  
                  
    /* Shipped Vs Shipped between RFL and RFO */              

SELECT  COUNT(*) ShippedStatusCount /*
         ospi.ChangedByUser ,
        o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
        CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		--*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND ( ( b.OrderStatusID IN ( 4, 7 )
                AND shipstatus.Shipped = 1
                AND shipstatus.Pending <> 1
                AND o.OrderStatusID <> 4
              )
              OR ( b.OrderStatusID IN ( 4, 7 )
                   AND shipstatus.Shipped = 1
                   AND shipstatus.Pending = 1
                   AND o.OrderStatusID = 4
                 )
              OR ( b.OrderStatusID = 9
                   AND o.OrderStatusID <> 4
                 )
            );
--97,070 now 0
						
							/* Partially Submitted Validation */
SELECT  COUNT(*) PartiallyShippedCounts /*
        o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
         CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
			   WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4           
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
			 WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND ( ( b.OrderStatusID = 4
                AND shipstatus.Pending = 1
                AND shipstatus.Shipped = 1
                AND o.OrderStatusID != 5
              )
              OR ( b.OrderStatusID = 7
                   AND shipstatus.Pending = 1
                   AND shipstatus.Shipped = 1
                   AND o.OrderStatusID != 5
                 )
            );

			--Not any records

					   /* Submitted Validation */

SELECT  COUNT(*) SubmittedStatusCounts /*
        o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
          CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
			   WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4           
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
			 WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND ( b.OrderStatusID IN ( 4, 7 )
              AND shipstatus.Shipped = 1
              AND o.OrderStatusID = 2 --submitted 
            );
--8344 and now 0

	
/* Checking If OrderStatus RFL Pending , Pending Error, Pending Cancelled ,partially Submitted and Printed not haing Failed in  RFO */

SELECT  COUNT(*) PendingStatusCount /*
         o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
        CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		--*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND ( b.OrderStatusID IN ( 1, 2, 3, 6, 8 )
              AND o.OrderStatusID <> 1--Pending 
            );

	  /* Cancelled Validation */
															   
SELECT  --COUNT(*) CancelledStatusCount /*
         o.OrderID ,
        o.OrderStatusID AS OrderStatusID_Now ,
        CASE WHEN b.OrderStatusID = 1 THEN 1
             WHEN b.OrderStatusID = 2 THEN 1
             WHEN b.OrderStatusID = 3 THEN 1
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
             WHEN b.OrderStatusID = 4
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 4
                  AND shipstatus.Shipped = 1 THEN 4
             WHEN b.OrderStatusID = 4 THEN 2
             WHEN b.OrderStatusID = 5 THEN 3
             WHEN b.OrderStatusID = 6 THEN 1
             WHEN b.OrderStatusID = 8 THEN 1
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Pending = 1
                  AND shipstatus.Shipped = 1 THEN 5
			 /*Orders that are mistakenly classified as submitted templates should be treated as submitted orders*/
             WHEN b.OrderStatusID = 7
                  AND ospi.ChangedByUser = 'GP-2083'
                  AND ISNULL(ospi.ShipDate, '1900-01-01') <> '1900-01-01'
             THEN 4
             WHEN b.OrderStatusID = 7
                  AND shipstatus.Shipped = 1 THEN 4
			/*******************************************************/
             WHEN b.OrderStatusID = 7 THEN 2
             WHEN b.OrderStatusID = 9 THEN 4
             ELSE 10
        END AS OrderStatusID_Cal ,
        b.OrderStatusID ,
        shipstatus.Shipped ,
        shipstatus.Pending ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.ChangedByUser
		--*/
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.Hybris.OrderShipmentPackageItem ospi ON ospi.OrderItemId = oi.OrderItemID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01'
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND b.OrderStatusID = 5
        AND o.OrderStatusID <> 3;
                 
				 
				 --OR  

				 /* Cancelled Orders having Shipped or Submitted ir RFL not been touched by clean UP*/
SELECT  o.OrderID AS RFO_OrderId ,
        CASE WHEN ISNULL(oi.OrderItemID, 0) = 0 THEN 'Missing Item in RFO'
             ELSE CAST(oi.OrderItemID AS VARCHAR(25))
        END AS OrderItemRFO ,
        o.OrderStatusID AS RFO_OrderStatus ,
        b.OrderStatusID AS RFL_OrderStatus ,
        shipstatus.Pending ,
        shipstatus.Shipped ,
        OS.DateShipped ,
        o.ServerModifiedDate ,
        o.CompletionDate ,
        o.ServerModifiedDate ,
        o.ChangedByUser
FROM    RFOperations.Hybris.Orders (NOLOCK) o
        LEFT JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        JOIN RFOperations.dbo.vwOrderShipmentStatus (NOLOCK) shipstatus ON shipstatus.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) b ON b.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments AS OS WITH ( NOLOCK ) ON OS.OrderID = o.OrderID
WHERE   o.CountryID = 236
        AND b.OrderStatusID = 5
        AND b.OrderTypeID NOT IN ( 4, 5, 9 )
        AND o.OrderStatusID NOT IN ( 1, 3 );
        --AND ISNULL(OS.DateShipped, '1900-01-01') <> '1900-01-01';



--==========================================================================
-- Checking OrderStatus in Different Tables .

DECLARE @OrderID INT= 1990255;

SELECT  'vwOrderShipmentStatus' [Table] ,
        *
FROM    RFOperations.dbo.vwOrderShipmentStatus
WHERE   OrderID = @OrderID;
SELECT  'RFO_orders' [Table] ,
        *
FROM    RFOperations.Hybris.Orders
WHERE   OrderID = @OrderID;
SELECT  'RFO_ordersItem' [Table] ,
        *
FROM    RFOperations.Hybris.OrderItem
WHERE   OrderId = @OrderID;
SELECT  'RFO_Ordershipment' AS [Table] ,
        *
FROM    RFOperations.Hybris.OrderShipment
WHERE   OrderID = @OrderID;
SELECT  'RFO_OrderShipmentPackageItem' [Table] ,
        *
FROM    RFOperations.Hybris.OrderShipmentPackageItem
WHERE   OrderID = @OrderID;
SELECT  'RFL_Orders' [Table] ,
        *
FROM    RodanFieldsLive.dbo.Orders
WHERE   OrderID = @OrderID;
SELECT  'RFL_OrderShipments' [Table] ,
        DateShipped ,
        *
FROM    RodanFieldsLive.dbo.OrderShipments
WHERE   OrderID = @OrderID;
SELECT  'RFL_OrderShipmentItems' [Table] ,
        *
FROM    RodanFieldsLive.dbo.OrderShipmentItems
WHERE   OrderShipmentID IN ( SELECT OrderShipmentID
                             FROM   RodanFieldsLive.dbo.OrderShipments
                             WHERE  OrderID = @OrderID );
SELECT  'cleanup' [Table] ,
        *
FROM    RFOperations.CleanUp.orderstatus
WHERE   OrderID = @OrderID;

--==========================================================================
		 
                            


                            

/* having Multiple Ordershipment.*/
SELECT  os.OrderID ,
        COUNT(*) Counts ,
        MAX(OrderShipmentID) AS MaxShipID ,
        MIN(OrderShipmentID) AS MinShipID
FROM    RodanFieldsLive.dbo.Orders o
        JOIN RodanFieldsLive.dbo.OrderShipments os ON o.OrderID = os.OrderID
WHERE   o.CountryId = 1
GROUP BY os.OrderID ,
        o.CountryId
HAVING  COUNT(*) > 1;

/* TEST SENARIO FOR ETL TESTING
1	RFO  SUBMITTED ORDER  UPDATED TO CANCELLED IN RFL 
2	RFO SUBMITTED ORDER  UPDATED TO FAILED IN RFL  
3	RFO PENDING ORDER UPDATED TO SUBMITTED IN RFL 
4	RFO FAILLED ORDERS UPDATED TO SUBMITTED
5	RFO SHIPPED ORDERS UPDATED TO FAILLED FOR GP-2083


***************************/


------------------------------------------------------------
/* Running Except ETL SP */

SELECT  COUNT(*)
FROM    RFOperations.ETL.StagingOrders;
 --2543 with 1 minutes and 50 secs in STG2. with same time and 3605 count after update .
TRUNCATE TABLE RFOperations. ETL.StagingOrders;



USE RFOperations;
 GO
 
EXECUTE [ETL].[USPExceptStagingOrder];  
 ------------------------------------------------------------


/* RESTORING ORDERS TABLE IN RFO USING BACK UP TABLE */

SET NOCOUNT ON; 
BEGIN TRANSACTION;
UPDATE  ro
SET     ro.OrderStatusID = bk.OrderStatusID ,
        ro.ChangedByApplication = bk.ChangedByApplication ,
        ro.ChangedByUser = bk.ChangedByUser ,
        ro.ServerModifiedDate = bk.ServerModifiedDate
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.CleanUp.Orders_bkp bk ON bk.OrderID = ro.OrderID;

IF @@ERROR <> 0
    BEGIN 
        ROLLBACK TRANSACTION;
        PRINT ' Transaction Rolled back';
    END;
ELSE
    BEGIN
        COMMIT TRANSACTION;
        PRINT ' Transaction Committed Successfully ';
    END;


