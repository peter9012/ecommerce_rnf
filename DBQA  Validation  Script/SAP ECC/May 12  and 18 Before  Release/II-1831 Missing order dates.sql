

----STEP 1: TO UPDATE 39 ORDERS TO PREVIOUS COMPETEDATE FROM PRODUCTION.

BEGIN TRAN;
UPDATE  RodanFieldsLive.dbo.Orders
SET     CompleteDate = NULL
WHERE   OrderID IN ( 749186, 917068, 1804646, 2193379, 3137475, 3272698,
                     3272841, 3273225, 4160969, 4840120, 4874474, 4971572,
                     5303635, 5303647, 5459280, 5507713, 5521718, 5528208,
                     5535352, 5654688, 5654698, 5654716, 5654747, 5654755,
                     5657816, 5862349, 6401544, 6401549, 6584083, 7176377,
                     10972465, 11376756, 11767502, 12806687, 20529159 );
--NULL in production .
					 
	
IF @@ERROR <> 0
    BEGIN
        ROLLBACK TRAN; 
        PRINT 'Rolled Back Transaction';
    END;
ELSE
    BEGIN
        COMMIT TRAN;
        PRINT 'Transaction committed Successfully';
    END; 

	
BEGIN TRAN;
UPDATE  RodanFieldsLive.dbo.Orders
SET     CompleteDate = '1900-01-01'
WHERE   OrderID IN ( 601281, 19980607, 22238915, 22566292 );
--'1900-01-01' in Production.

	
IF @@ERROR <> 0
    BEGIN
        ROLLBACK TRAN; 
        PRINT 'Rolled Back Transaction';
    END;
ELSE
    BEGIN
        COMMIT TRAN;
        PRINT 'Transaction committed Successfully';
    END; 



--STEP 2: PUSH DEV CODE TO UPDATE PROPERLY.






--STEP 3:VALIDATE THOSE ORDERS WITH PROPER COMPLETEDATE.

---Logic Validation .
SELECT  o.OrderID ,
        o.StartDate ,
        os.DateShipped ,
        o.CompleteDate ,
        CASE WHEN ISNULL(o.StartDate, '1900-01-01') <> '1900-01-01'
             THEN o.StartDate
             ELSE os.DateShipped
        END AS NewCompeteDate
FROM    RodanFieldsLive.dbo.Orders o
        LEFT JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderID = o.OrderID
WHERE   o.OrderID IN ( 749186, 917068, 1804646, 2193379, 3137475, 3272698,
                       3272841, 3273225, 4160969, 4840120, 4874474, 4971572,
                       5303635, 5303647, 5459280, 5507713, 5521718, 5528208,
                       5535352, 5654688, 5654698, 5654716, 5654747, 5654755,
                       5657816, 5862349, 6401544, 6401549, 6584083, 7176377,
                       10972465, 11376756, 11767502, 12806687, 20529159,
                       601281, 19980607, 22238915, 22566292 )
        AND o.CompleteDate <>CASE WHEN ISNULL(o.StartDate, '1900-01-01') <> '1900-01-01'
             THEN o.StartDate
             ELSE os.DateShipped
        END;
	---FULL VALIDATION 


SELECT  COUNT(*) Cnt, os.Name
FROM    RodanFieldsLive.dbo.Orders ro 
JOIN RodanFieldsLive.dbo.OrderStatus os ON os.OrderStatusID = ro.OrderStatusID
WHERE   ro.OrderStatusID <> 4 AND ro.OrderTypeID NOT IN (4,5)
        AND ISNULL(CompleteDate, '1900-01-01') = '1900-01-01'
		GROUP BY os.Name;
		


		



USE RodanFieldsLive;
 SET NOCOUNT ON;
 GO
 ;WITH historicalSubmittedOrdersModified
 AS (
 SELECT o.OrderId,
 (CASE 
 WHEN o.CompleteDate = o.StartDate OR o.CompleteDate = os.DateShipped THEN 1
 ELSE
 0
 END) AS 'WasModified',
 o.CompleteDate,
 o.StartDate,
 os.DateShipped
 FROM dbo.Orders AS o
  LEFT JOIN dbo.OrderShipments AS os ON o.OrderId = os.OrderId
 WHERE o.OrderStatusId = 4 ) --– submitted 

SELECT
 OrderId,
 CompleteDate,
 (CASE 
 WHEN CompleteDate = StartDate THEN 'equals to StartDate'
 WHEN CompleteDate = DateShipped THEN 'equals to DateShipped'
 END) AS 'EqualsTo', 
 StartDate AS 'StartDate from dbo.Orders',
 DateShipped AS 'DateShipped from dbo.OrderShipments'
 FROM 
 historicalSubmittedOrdersModified
 WHERE 
 WasModified = 1 
 AND historicalSubmittedOrdersModified.DateShipped IS NULL 


 
 SELECT * FROM RFOperations.Hybris.orders WHERE ISNULL(CompletionDate,'1900-01-01')='1900-01-01' AND CountryID=236


 SELECT *
 FROM   RodanFieldsLive.dbo.Orders
 WHERE  OrderStatusID = 4
        AND ISNULL(CompleteDate, '1900-01-01') = '1900-01-01';

	
	SELECT ro.CompleteDate,o.CompletionDate, ro.OrderID,o.OrderStatusID FROM RodanFieldsLive.dbo.orders ro
	JOIN RFOperations.Hybris.orders o ON ro.OrderID=o.OrderID
	WHERE ro.OrderStatusID=4  AND  ISNULL(o.CompletionDate,'1900-01-01')='1900-01-01'
	AND CAST(ro.CompleteDate AS DATE)<>CAST(o.CompletionDate AS DATE)

