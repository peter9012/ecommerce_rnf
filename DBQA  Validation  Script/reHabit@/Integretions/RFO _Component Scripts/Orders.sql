DECLARE @OrderNumber NVARCHAR(255)= '6254517'  -->>> Please  Change  the OrderNumber 

 
SELECT  'Orders' [Table] ,s.Name[OrderStatus],t.Name[OrderType],co.Alpha2Code[Country],cu.Name[Currency],
        ro.*
FROM    RFOperations.Hybris.orders ro
LEFT JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = ro.OrderStatusID
LEFT JOIN RFOperations.RFO_Reference.OrderType  t ON t.OrderTypeID = ro.OrderTypeID
LEFT JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ro.CountryID
LEFT JOIN RFOperations.RFO_Reference.Currency cu ON cu.CurrencyID = ro.CurrencyID     
WHERE   ro.OrderNumber = @OrderNumber



SELECT  'OrderShipment' [Table] ,os.*
FROM    RFOperations.Hybris.orders ro
        LEFT JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
WHERE   ro.OrderNumber = @OrderNumber




SELECT  'OrdersTax' [Table] ,ot.*
FROM    RFOperations.Hybris.orders ro
        LEFT JOIN RFOperations.Hybris.OrdersTax ot ON ot.OrderID = ro.OrderID
WHERE   ro.OrderNumber = @OrderNumber



SELECT  'OrderItems' [Table] ,p.SKU,oi.*
FROM    RFOperations.Hybris.orders ro
        LEFT JOIN RFOperations.Hybris.orderitem oi ON oi.OrderId = ro.OrderID
		LEFT JOIN RFOperations.Hybris.ProductBase p ON p.productID=oi.ProductID
       WHERE   ro.OrderNumber = @OrderNumber



	   
SELECT  'OrderItemTax' [Table] ,oit.*
FROM    RFOperations.Hybris.orders ro
LEFT JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
        LEFT JOIN RFOperations.Hybris.OrderItemTax oit ON oit.OrderItemID = oi.OrderItemID
WHERE   ro.OrderNumber = @OrderNumber





SELECT  'OrderShipmentPackageItem' [Table] ,osp.*
FROM    RFOperations.Hybris.orders ro
        LEFT JOIN RFOperations.Hybris.OrderShipmentPackageItem osp ON osp.OrderID = ro.OrderID
WHERE   ro.OrderNumber = @OrderNumber



SELECT  'OrderPayments' [Table] ,v.Name[CreditCardType],op.*
FROM    RFOperations.Hybris.orders ro
        LEFT JOIN RFOperations.Hybris.OrderPayment op ON op.OrderID = ro.OrderID		
		LEFT JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID=op.VendorID
WHERE   ro.OrderNumber = @OrderNumber


SELECT  'OrderPaymentTransaction' [Table] ,opt.*
FROM    RFOperations.Hybris.orders ro
LEFT JOIN RFOperations.Hybris.OrderPayment op ON op.OrderID = ro.OrderID
        LEFT JOIN RFOperations.Hybris.OrderPaymentTransaction opt ON opt.OrderPaymentID = op.OrderPaymentID
WHERE   ro.OrderNumber = @OrderNumber



SELECT  'OrderBillingAddress' [Table] ,ob.*
FROM    RFOperations.Hybris.orders ro
        LEFT JOIN RFOperations.Hybris.OrderBillingAddress ob ON ob.OrderID = ro.OrderID
WHERE   ro.OrderNumber = @OrderNumber




SELECT  'OrderShippingAddress' [Table] ,osa.Telephone,osa.*
FROM    RFOperations.Hybris.orders ro
        LEFT JOIN RFOperations.Hybris.OrderShippingAddress osa ON osa.OrderID = ro.OrderID
WHERE   ro.OrderNumber = @OrderNumber








SELECT  'OrderNotes' [Table] ,n.*
FROM    RFOperations.Hybris.orders ro
        LEFT JOIN RFOperations.Hybris.OrderNotes n ON n.OrderID = ro.OrderID
WHERE   ro.OrderNumber = @OrderNumber












