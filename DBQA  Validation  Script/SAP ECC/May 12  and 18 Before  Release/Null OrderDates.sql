-- RFO Orders 
SELECT  COUNT(*) ,
        os.Name AS [Order Status] ,
        t.Name AS [Order Type]
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.RFO_Reference.OrderStatus os ON os.OrderStatusId = ro.OrderStatusID
        JOIN RFOperations.RFO_Reference.OrderType t ON t.OrderTypeID = ro.OrderTypeID
WHERE   ISNULL(ro.CompletionDate, '1900-01-01') = '1900-01-01'
        AND ro.CountryID = 236
GROUP BY os.Name ,
        t.Name;


----RFO Templates.

SELECT  COUNT(*) ,
        t.Name AS [Template Types ] ,
        s.Name AS [Template Status]
FROM    RFOperations.Hybris.Autoship a
        JOIN RFOperations.RFO_Reference.AutoShipType t ON t.AutoShipTypeID = a.AutoshipTypeID
        JOIN RFOperations.RFO_Reference.AutoshipStatus s ON s.AutoshipStatusId = a.AutoshipStatusID
WHERE   ISNULL(a.CompletionDate, '1900-01-01') = '1900-01-01'
        AND a.CountryID = 236
GROUP BY t.Name ,
        s.Name;



--RFL
BEGIN TRAN

;




WITH    OrdersNull
          AS ( SELECT  DISTINCT
                        o.OrderID ,
                        os.Name AS [Order Status] ,
                        t.Name AS [Order type] ,
                        o.CompleteDate
               FROM     RodanFieldsLive.dbo.Orders (NOLOCK) o
                        JOIN RodanFieldsLive.dbo.OrderStatus (NOLOCK) os ON os.OrderStatusID = o.OrderStatusID
                        JOIN RodanFieldsLive.dbo.OrderTypes (NOLOCK) t ON t.OrderTypeID = o.OrderTypeID
               WHERE    ISNULL(o.CompleteDate, '1900-01-01') = '1900-01-01'
                        AND CAST(o.StartDate AS DATE) >= '2016-03-01'--Starting from March 1 , 2016
                        AND o.OrderTypeID IN ( 4, 5 )-- No Templates 
             )
    --SELECT *  INTO RodanFieldsLive.dbo.Orders_BackUpCompleteDate FROM OrdersNull 



			  SELECT    COUNT(DISTINCT o.OrderID)
              FROM      OrdersNull n
                        JOIN RodanFieldsLive.dbo.Orders (NOLOCK) o ON o.OrderID = n.OrderID ---RFL Orders 
                        JOIN RFOperations.Hybris.ReturnOrder (NOLOCK) ro ON ro.OrderID = o.OrderID--- RFO Orders 
              WHERE     o.StartDate <> ro.CompletionDate;

			 ---1450 Orders in Production .



IF @@ERROR<>0 
BEGIN
PRINT ' Transaction Rolled Back'
ROLLBACK TRAN
END
ELSE
BEGIN
PRINT ' Transaction Committed Successfully.'
COMMIT TRAN
END



			 
			 
		

        USE RodanFieldsLive;
        WITH    OrdersAffected
                  AS ( SELECT   OrderID ,
                                StartDate
                       FROM     dbo.Orders
                       WHERE    ( CompleteDate IS NULL
                                  OR CompleteDate < '20000101'
                                )
                                AND StartDate >= '20160301'
                                AND OrderTypeID NOT IN ( 4, 5 )
                     )
            SELECT  *
            INTO    RodanFieldsLive.dbo.Orders_BackUpCompleteDate
            FROM    OrdersAffected;


        SELECT  COUNT(*) cnt
        FROM    RodanFieldsLive.dbo.Orders_BackUpCompleteDate;