

--===============================================================================
--			VALIDATION OF ITEM QUANTITY=0 FOR US ORDERS IN RFL
--===============================================================================

SELECT  o.OrderID ,
        oi.OrderItemID ,
        oi.SKU ,
        oi.Quantity ,
        o.OrderStatusID ,
        ot.Name AS OrderType
FROM    RodanFieldsLive.dbo.Orders o
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON o.OrderID = oc.OrderID
        JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
        JOIN RodanFieldsLive.dbo.OrderTypes ot ON ot.OrderTypeID = o.OrderTypeID
WHERE   o.OrderStatusID = 4
        AND o.CompleteDate >= GETDATE() - 15 
 --o.CompleteDate BETWEEN '1/1/2016' AND '3/25/2016'
        AND oi.Quantity = 0
        AND oi.CommissionableTotal > 0;
 -- Not fixing for historical record according to Paul.


 --================================================================================
 --		 VALIDATION OF ITEM QUANTITY=0 IN FOR US ORDERS IN RFO
 --================================================================================


SELECT  ro.CompletionDate ,
        ro.OrderID ,
        ro.CountryID ,
        ro.OrderTypeID ,
        ro.OrderStatusID ,
        oi.OrderItemID ,
        oi.ProductID ,
        ro.SubTotal ,
        oi.Quantity
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
WHERE   ro.CompletionDate >= GETDATE() - 15
        AND oi.Quantity = 0
        AND oi.CV > 0
        AND ro.OrderStatusID NOT IN ( 1 )-- Cancelled OrderStatus
        AND ro.OrderTypeID NOT IN ( 11 );
-- Override Orders Type
		
		
		--Latest Orders completionDate Max(ro.CompletionDate)=2016-04-07 10:49:16.740
		
		/* If only want to analysis for historical Orders in RFO  */

SELECT  CAST(ro.CompletionDate AS DATE) CompletedDate ,
        os.Name ,
        COUNT(*) Counts
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
        JOIN RFOperations.RFO_Reference.OrderStatus os ON os.OrderStatusId = ro.OrderStatusID
WHERE   --ro.CompletionDate>=GETDATE()-15 AND
        oi.Quantity = 0
        AND ro.OrderStatusID NOT IN ( 1 )-- Cancelled OrderStatus
        AND ro.OrderTypeID NOT IN ( 11 )-- Override Orders Type
		--Max(ro.CompletionDate)=2016-04-07 10:49:16.740
GROUP BY CAST(ro.CompletionDate AS DATE) ,
        os.Name
ORDER BY CompletedDate DESC;


		--+++++++++++++++++++++++++++++++++++++++++++++++++
		-- VALIDATION OF ETL AND BOOMI TO HANDLE QUANTITY-0
		--+++++++++++++++++++++++++++++++++++++++++++++++++

		SELECT * FROM RodanFieldsLive.dbo.OrderTypes
		
		--SELECT * FROM RFOperations.etl.StagingOrders WHERE orderid  in (select distinct orderid from #T)		AND OrderItemCV<1

		UPDATE RFOperations.etl.StagingOrders
		--SET OrderItemCV=100
		SET OrderStatusID=1
		WHERE OrderID=22862040
		--SET OrderTypeID=1
		WHERE orderid
 in (select distinct orderid from #T)	AND OrderItemCV<1
		
		--=================================================
		--Executing OrderStaging SP to get data in Staging
		--================================================
		--TRUNCATE TABLE RFOperations.etl.StagingOrders
		
		USE         RFOperations
		EXECUTE ETL.USPExceptStagingOrder  -- about 2/3 minutes,1641 records

		--SELECT * FROM RFOperations.ETL.StagingOrders
		 

		/* FOR EXISTING ORDERS TO VALIDATE ******************************/


DECLARE @ConsSubmittedOrderID INT ,
    @ConsFailledOrderID INT ,
    @PCSubmittedOrderID INT ,
    @PCFailledOrderID INT ,
    @RCSubmittedOrderID INT ,
    @RCFailledOrderID INT;  

SET @ConsSubmittedOrderID = ( SELECT TOP 1
                                        OrderID
                              FROM      RFOperations.ETL.StagingOrders
                              WHERE     OrderTypeID = 3
                                        AND OrderStatusID = 2
                                        AND NOT EXISTS ( SELECT
                                                              1
                                                         FROM RFOperations.Logging.BoomiError r
                                                         WHERE
                                                              r.RecordID = OrderID )
                                        AND NOT EXISTS ( SELECT
                                                              1
                                                         FROM RFOperations.Logging.BoomiException e
                                                         WHERE
                                                              e.RecordID = OrderID )
                            );
SET @ConsFailledOrderID = ( SELECT TOP 1
                                    OrderID
                            FROM    RFOperations.ETL.StagingOrders
                            WHERE   OrderTypeID = 3
                                    AND OrderStatusID = 1
                                    AND NOT EXISTS ( SELECT 1
                                                     FROM   RFOperations.Logging.BoomiError r
                                                     WHERE  r.RecordID = OrderID )
                                    AND NOT EXISTS ( SELECT 1
                                                     FROM   RFOperations.Logging.BoomiException e
                                                     WHERE  e.RecordID = OrderID )
                          );

SET @PCSubmittedOrderID = ( SELECT TOP 1
                                    OrderID
                            FROM    RFOperations.ETL.StagingOrders
                            WHERE   OrderTypeID = 2
                                    AND OrderStatusID = 2
                                    AND NOT EXISTS ( SELECT 1
                                                     FROM   RFOperations.Logging.BoomiError r
                                                     WHERE  r.RecordID = OrderID )
                                    AND NOT EXISTS ( SELECT 1
                                                     FROM   RFOperations.Logging.BoomiException e
                                                     WHERE  e.RecordID = OrderID )
                          );
SET @PCFailledOrderID = ( SELECT TOP 1
                                    OrderID
                          FROM      RFOperations.ETL.StagingOrders
                          WHERE     OrderTypeID = 2
                                    AND OrderStatusID = 1
                                    AND NOT EXISTS ( SELECT 1
                                                     FROM   RFOperations.Logging.BoomiError r
                                                     WHERE  r.RecordID = OrderID )
                                    AND NOT EXISTS ( SELECT 1
                                                     FROM   RFOperations.Logging.BoomiException e
                                                     WHERE  e.RecordID = OrderID )
                        );

SET @RCSubmittedOrderID = ( SELECT TOP 1
                                    OrderID
                            FROM    RFOperations.ETL.StagingOrders
                            WHERE   OrderTypeID = 1
                                    AND OrderStatusID = 2
                                    AND NOT EXISTS ( SELECT 1
                                                     FROM   RFOperations.Logging.BoomiError r
                                                     WHERE  r.RecordID = OrderID )
                                    AND NOT EXISTS ( SELECT 1
                                                     FROM   RFOperations.Logging.BoomiException e
                                                     WHERE  e.RecordID = OrderID )
                          );
SET @RCFailledOrderID = ( SELECT TOP 1
                                    OrderID
                          FROM      RFOperations.ETL.StagingOrders
                          WHERE     OrderTypeID = 1
                                    AND OrderStatusID = 1
                                    AND NOT EXISTS ( SELECT 1
                                                     FROM   RFOperations.Logging.BoomiError r
                                                     WHERE  r.RecordID = OrderID )
                                    AND NOT EXISTS ( SELECT 1
                                                     FROM   RFOperations.Logging.BoomiException e
                                                     WHERE  e.RecordID = OrderID )
                        );
						
IF OBJECT_ID(N'Tempdb..#T') IS NOT NULL
    DROP TABLE #T;

CREATE TABLE #T
    (
      OrderID INT ,
      Scenario NVARCHAR(50)
    );

INSERT  INTO #T
        ( OrderID, Scenario )
VALUES  ( @ConsSubmittedOrderID, 'ConsSubmittedOrderID' ) ,
        ( @ConsFailledOrderID, 'ConsFailledOrderID' ) ,
        ( @PCSubmittedOrderID, 'PCSubmittedOrderID' ) ,
        ( @PCFailledOrderID, 'PCFailledOrderID' ) ,
        ( @RCSubmittedOrderID, 'RCSubmittedOrderID' ) ,
        ( @RCFailledOrderID, 'RCFailledOrderID' ); 

SELECT  'SampleOrder' AS [Table] ,
        *
FROM    #T;

SELECT  'OriginalStaging' AS [Table] ,
        so.OrderID ,
        so.Quantity ,
        so.OrderItemCV ,
        so.OrderStatusID ,
        so.OrderTypeID
FROM    RFOperations.ETL.StagingOrders so
        JOIN #T t ON t.OrderID = so.OrderID;

BEGIN TRAN; 

		UPDATE RFOperations.etl.StagingOrders
		SET OrderItemCV=100		
		WHERE orderid
 in (select  orderid from #T)	AND OrderItemCV<1

UPDATE  so
SET     so.Quantity = 0 
--,     so.CV = 0
FROM    RFOperations.ETL.StagingOrders so
        JOIN #T t ON t.OrderID = so.OrderID;

IF @@ERROR <> 0
    BEGIN 
        PRINT 'Transaction Rolled Back';
        ROLLBACK TRAN;
    END;
ELSE
    BEGIN
        PRINT 'Transaction Committed Successfully';
        COMMIT TRAN;
    END;
--====================================
--	 Retrieving Data from Stage 
--====================================

SELECT DISTINCT 'UpdatedStaging' AS [Table] ,
        so.OrderID ,
        so.Quantity ,
        so.OrderItemCV ,
        so.OrderStatusID ,
        so.OrderTypeID
FROM    RFOperations.ETL.StagingOrders so
        JOIN #T t ON t.OrderID = so.OrderID;


--=========================================
--Executing the SP ETL. uspOrderValidation 
--=========================================

USE RFOperations;
EXECUTE ETL.uspOrderValidation; --

--====================================
-- Validation Part.
--===================================

SELECT  DISTINCT
        'Validation' [Table] ,
        t.OrderID,t.OrderStatusID,s.OrderStatusID,s.Quantity,s.OrderItemCV,e.ErrorMessage,r.ErrorMessage ,
        CASE WHEN e.RecordID IS NULL
                  OR r.RecordID IS NULL
             THEN 'Validation Failled with No BoomiError exception  Log'
             ELSE 'Validation Passed'
        END AS Result
FROM    RodanFieldsLive.dbo.Orders t
LEFT JOIN RFOperations.etl.StagingOrders s ON s.OrderID=t.OrderID  AND t.OrderID IN ( 21967136,
                                                              21969896,
                                                              22811140,
                                                              21970063,
                                                              22862051,
                                                              22862040 )
        LEFT JOIN RFOperations.Logging.BoomiException e ON e.RecordID = t.OrderID
        LEFT JOIN RFOperations.Logging.BoomiError r ON r.RecordID = t.OrderID
        LEFT JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = t.OrderID
                                                   AND CAST(ro.ServerModifiedDate AS DATE) = CAST(GETDATE() AS DATE)
                                                  ;


SELECT * FROM RFOperations.Logging.BoomiError WHERE RecordID IN  ( 21967136, 21969896, 22811140, 21970063, 22862051,

                        22862040 )
SELECT * FROM RFOperations.Logging.BoomiException WHERE RecordID IN ( 21967136, 21969896, 22811140, 21970063, 22862051,

                        22862040 )



/* FOR NEWLY GENERATED ORDERS TO VALIDATE */


	-- Ask Front End QA to Generate 2 Retail Orders,2 PC orders and 2 Consultant Orders with Submitted .

DECLARE @ConsSubmittedOrderID INT = 21967136;
DECLARE @ConsFailledOrderID INT= 21969896; 
DECLARE @PCSubmittedOrderID INT= 22811140;  
DECLARE @PCFailledOrderID INT= 21970063; 
DECLARE @RCSubmittedOrderID INT = 22862051; 
DECLARE @RCFailledOrderID INT= 22862040; 

	-- Ask Front End QA to Generate 2 Retail Orders,2 PC orders and 2 Consultant Orders

						
IF OBJECT_ID(N'Tempdb..#T1') IS NOT NULL
    DROP TABLE #T1;

CREATE TABLE #T1
    (
      OrderID INT ,
      Scenario NVARCHAR(50)
    );

INSERT  INTO #T1
        ( OrderID, Scenario )
VALUES  ( @ConsSubmittedOrderID, 'ConsSubmittedOrderID' ) ,
        ( @ConsFailledOrderID, 'ConsFailledOrderID' ) ,
        ( @PCSubmittedOrderID, 'PCSubmittedOrderID' ) ,
        ( @PCFailledOrderID, 'PCFailledOrderID' ) ,
        ( @RCSubmittedOrderID, 'RCSubmittedOrderID' ) ,
        ( @RCFailledOrderID, 'RCFailledOrderID' ); 

SELECT  'SampleOrder' AS [Table] ,
        *
FROM    #T1;

SELECT  'OriginalStaging' AS [Table] ,
        so.OrderID ,
        so.Quantity ,
        so.CV ,
		so.OrderItemCV,
        so.OrderStatusID ,
        so.OrderTypeID
FROM    RFOperations.ETL.StagingOrders so
        JOIN #T t ON t.OrderID = so.OrderID;

--BEGIN TRAN; 
--UPDATE  so
--SET     so.Quantity = 0 ,
--        --so.CV = 0 ,
--        so.OrderStatusID = CASE WHEN t.Scenario LIKE '%Submitte%' THEN 2
--                                WHEN t.Scenario LIKE '%Failled%' THEN 1
--                           END
--FROM    RFOperations.ETL.StagingOrders so
--        JOIN #T t ON t.OrderID = so.OrderID;

/*  --- This updatation from RFL DB itself If required.

UPDATE  oi
SET     oi.Quantity = 0 ,
        oi.CommissionableTotal = 0
FROM    RodanFieldsLive.dbo.Orders ro
        JOIN #T t ON t.OrderID = ro.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID = ro.OrderID
        JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID;

UPDATE  o
SET     o.OrderStatusID = CASE WHEN t.Scenario LIKE '%Submitte%' THEN 2
                               WHEN t.Scenario LIKE '%Failled%' THEN 1
                          END
FROM    RodanFieldsLive.dbo.Orders ro
        JOIN #T t ON t.OrderID = ro.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID = ro.OrderID
        JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID;
		*************************************/

--IF @@ERROR <> 0
--    BEGIN 
--        PRINT 'Transaction Rolled Back';
--        ROLLBACK TRAN;
--    END;
--ELSE
--    BEGIN
--        PRINT 'Transaction Committed Successfully';
--        COMMIT TRAN;
--    END;

--SELECT  'UpdatedStaging' AS [Table] ,
--        so.OrderID ,
--        so.Quantity ,
--        so.CV ,
--        so.OrderStatusID ,
--        so.OrderTypeID
--FROM    RFOperations.ETL.StagingOrders so
--        JOIN #T t ON t.OrderID = so.OrderID;



--=========================================
--Executing the SP ETL. uspOrderValidation 
--=========================================

USE RFOperations;
EXECUTE ETL.uspOrderValidation; 

--====================================
-- Validation Part.
--===================================

SELECT  DISTINCT
        'Validation' [Table] ,
        o.OrderStatusID ,
        t.* ,r.ErrorDate,e.ServerModifiedDate,
        o.OrderStatusID ,
        CASE WHEN e.RecordID IS NULL
                  OR r.RecordID IS NULL
             THEN 'Validation Failled with No BoomiError exception  Log'
             WHEN ro.OrderID IS NOT NULL
             THEN 'Validation Failled Order flown to RFO'
             ELSE 'Validation Passed'
        END AS Result,r.ErrorMessage AS [BoomiError Message],e.ErrorMessage[Exception message]
   FROM    #T1 t
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = t.OrderID  		
        LEFT JOIN RFOperations.Logging.BoomiException e ON e.RecordID = t.OrderID
        LEFT JOIN RFOperations.Logging.BoomiError r ON r.RecordID = t.OrderID
        LEFT JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = t.OrderID
                                                   AND CAST(ro.CompletionDate AS DATE) = CAST(GETDATE() AS DATE);
					
					
					
					
					
					
							  