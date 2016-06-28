DECLARE @OrderID INT = 19496291;

SELECT  'RFL Orders' [Table] ,
        *
FROM    RodanFieldsLive.dbo.Orders
WHERE   OrderID = @OrderID;
SELECT  'RFL OrderCustomers' AS [Table] ,
        *
FROM    RodanFieldsLive.dbo.OrderCustomers
WHERE   OrderID = @OrderID;

SELECT  'RFL OrderItems' AS [Table] ,
        *
FROM    RodanFieldsLive.dbo.OrderItems
WHERE   OrderCustomerID IN ( SELECT OrderCustomerID
                             FROM   RodanFieldsLive.dbo.OrderCustomers
                             WHERE  OrderID = @OrderID );
SELECT  'RFL OrderShipment' AS [Table] ,
        *
FROM    RodanFieldsLive.dbo.OrderShipments
WHERE   OrderID = @OrderID;
SELECT  ' RFL OrderShipmentItem by OrderItem ' AS [Table] ,
        *
FROM    RodanFieldsLive.dbo.OrderShipmentItems
WHERE   OrderItemID IN (
        SELECT  OrderItemID
        FROM    RodanFieldsLive.dbo.OrderItems
        WHERE   OrderCustomerID IN ( SELECT OrderCustomerID
                                     FROM   RodanFieldsLive.dbo.OrderCustomers
                                     WHERE  OrderID = @OrderID ) );
