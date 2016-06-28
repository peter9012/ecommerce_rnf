	--22877621  22877619
	DECLARE @ReturnNumber NVARCHAR(25)= 22877621 ,
		@OrderID BIGINT ,
		@ReturnID BIGINT;

	SELECT  @ReturnId = OrderID ,
			@OrderID = ParentOrderID
	FROM    RodanFieldsLive.dbo.orders 
	WHERE   OrderNumber = @ReturnNumber
	
			
        --SELECT TOP  * FROM RFOperations.Hybris.ReturnOrder WHERE ReturnOrderID=22877619  
		--SELECT * FROM RodanFieldsLive.dbo.orders WHERE OrderID=22877619
		
		--SELECT * FROM RFOperations.Hybris.ReturnItemTax WHERE ReturnItemID=61868782

	SELECT  @ReturnID ReturnOrderID ,
			@OrderID AS OriginalOrder ,
			@ReturnNumber AS ReturnNumber; 
  

	SELECT  'RFL ReturnOrder' AS [Table] ,
			*
	FROM    RodanFieldsLive.dbo.orders 
	WHERE   OrderID = @ReturnID;
	SELECT  'RFL Original Order' AS [Table] ,
			*
	FROM    RodanFieldsLive.dbo.orders 
	WHERE   OrderID = @OrderID;

	SELECT  'RFO ReturnOrders' AS [Table] ,
			ReturnOrderID ,
			OrderID ,
			ProductTax ,
			TotalTax ,
			TotalGST ,
			TotalPST ,
			TotalHST ,
			CountryID
	FROM    RFOperations.Hybris.ReturnOrder
	WHERE   ReturnOrderID = @ReturnID; 
	SELECT  'RFO Orders' AS [Table] ,
			OrderID ,
			ProductTax ,
			TotalTax ,
			TotalGST ,
			TotalPST ,
			TotalHST ,
			CountryID
	FROM    RFOperations.Hybris.Orders
	WHERE   OrderID = @OrderID; 
   
       
	SELECT  'RFO OrderShipment' [Table] ,
			OrderShipmentID ,
			OrderID ,
			TaxOnShippingCost ,
			TaxOnHandlingCost ,
			ShippingMethodID
	FROM    RFOperations.Hybris.OrderShipment
	WHERE   OrderID = @OrderID; 
	SELECT  'RFL ReturnItems' AS [Table] ,oc.OrderID ,oi.*
	FROM    RodanFieldsLive.dbo.OrderItems oi
	JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderCustomerID=oi.OrderCustomerID	
	WHERE   oc.OrderID = @ReturnID;
	SELECT  'RFL OrderItems' AS [Table] ,oc.OrderID,oi.*
	FROM    RodanFieldsLive.dbo.OrderItems oi
	JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderCustomerID=oi.OrderCustomerID	
	WHERE   oc.OrderID = @OrderID;
	
		SELECT  'RFO ReturnOrderItem' AS [Table] ,
			ReturnItemID ,
			ReturnOrderID ,
			OrderItemID ,
			ri.ReceivedQuantity ,
			[ReStockingFeeTax]
			,p.SKU
	FROM    RFOperations.Hybris.ReturnItem ri 
	JOIN RFOperations.Hybris.ProductBase p ON p.productID=ri.ProductID
	WHERE   ReturnOrderID = @ReturnID;

	SELECT  'RFO ReturnOrderTax' AS [Table] ,
			*
	FROM    RFOperations.Hybris.ReturnOrderTax
	WHERE   ReturnOrderID = @ReturnID;
        
	SELECT  'RFO OrderTax' AS [Table] ,
			*
	FROM    RFOperations.Hybris.OrdersTax
	WHERE   OrderID = @OrderID;
  
  
	SELECT  'RFO ReturnOrderItemTax' AS [Table] ,
			*
	FROM    RFOperations.Hybris.ReturnItemTax
	WHERE   ReturnItemID IN ( SELECT    ReturnItemID
							  FROM      RFOperations.Hybris.ReturnItem
							  WHERE     ReturnOrderID = @ReturnID );

								
	SELECT  'RFO OrderItemTax' AS [Table] ,
			*
	FROM    RFOperations.Hybris.OrderItemTax
	WHERE   OrderItemID IN ( SELECT OrderItemID
							 FROM  RFOperations.Hybris.OrderItem
							 WHERE  OrderId = @OrderID );

