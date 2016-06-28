USE RFOperations;
GO

--================================================================================================
--CHECKING US TAXEDITEM<>'' OR  TAXAMOUNT<>0.0 FOR ORDER HEADER AND ORDERITEM  LEVEL TAX TABLE.
--================================================================================================


SELECT  'Order Tax ' AS [Table] ,
        CASE WHEN COUNT(*) >= 1
             THEN 'Zero TaxAmount And Space TaxedItem Validation Failed '
             ELSE 'Zero TaxAmount And Space TaxedItem Validation Passed '
        END AS [Result] ,
        COUNT(*) AS [Error Total]
FROM    RFOperations.Hybris.OrdersTax
WHERE   TaxType = 'US'
        AND ( CAST(TaxAmount AS MONEY) = 0.0
              OR ISNULL(TaxedItem, '') = ''
            )
UNION ALL
SELECT  'OrderItem Tax' AS [Table] ,
        CASE WHEN COUNT(*) >= 1
             THEN 'Zero TaxAmount And Space TaxedItem Validation Failed '
             ELSE ' Zero TaxAmount And Space TaxedItem Validation Passed '
        END AS [Result] ,
        COUNT(*) AS [Error Total]
FROM    RFOperations.Hybris.OrderItemTax
WHERE   TaxType = 'US'
        AND ( CAST(TaxAmount AS MONEY) = 0.0
              OR ISNULL(TaxedItem, '') = ''
            )
UNION ALL
SELECT  'ReturnOrder Tax ' AS [Table] ,
        CASE WHEN COUNT(*) >= 1
             THEN 'Zero TaxAmount And Space TaxedItem Validation Failed '
             ELSE ' Zero TaxAmount And Space TaxedItem Validation Passed '
        END AS [Result] ,
        COUNT(*) AS [Error Total]
FROM    RFOperations.Hybris.ReturnOrderTax
WHERE   TaxType = 'US'
        AND ( CAST(TaxAmount AS MONEY) = 0.0
              OR ISNULL(TaxedItem, '') = ''
            )
UNION ALL
SELECT  'ReturnItem Tax' AS [Table] ,
        CASE WHEN COUNT(*) >= 1
             THEN 'Zero TaxAmount And Space TaxedItem Validation Failed '
             ELSE ' Zero TaxAmount And Space TaxedItem Validation Passed '
        END AS [Result] ,
        COUNT(*) AS [Error Total]
FROM    RFOperations.Hybris.ReturnItemTax
WHERE   TaxType = 'US'
        AND ( CAST(TaxAmount AS MONEY) = 0.0
              OR ISNULL(TaxedItem, '') = ''
            );

			

--===============================================================================================
-- DUPLICATE VALIDATION FOR ORDER TAX ,RETURNORDER TAX , ORDERITEM TAX AND RETURNORDER TAX TABLES
--===============================================================================================


--CHECKING DUPLICATES TAXTYPES AND TAXEDITEM IN ORDERSTAX TABLE FOR US ORDERS.

SELECT  'Order Tax ' AS [Table] ,
        CASE WHEN COUNT([Total Count]) >= 1 THEN 'Duplicate Validation Failed'
             ELSE 'Duplicate Validation Passed'
        END AS [Results] ,
        COUNT([Total Count]) [Error Total]
FROM    ( SELECT    ro.OrderID ,
                    TaxType ,
                    TaxedItem ,
                    COUNT(*) [Total Count]
          FROM      RFOperations.Hybris.OrdersTax A
                    JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = A.OrderID
                                                          AND ro.CountryID = 236
          GROUP BY  ro.OrderID ,
                    TaxType ,
                    TaxedItem
          HAVING    COUNT(*) > 1
        ) OrderTax
UNION ALL 
--CHECKING DUPLICATES TAXTYPES AND TAXEDITEM IN ORDERITEM  TABLE FOR CANADA PRODUCTTAX.
SELECT  'OrderItem' AS [Table] ,
        CASE WHEN COUNT(*) >= 1 THEN 'Duplicate Validation Failed'
             ELSE 'Duplicate Validation Passed'
        END AS [Results] ,
        COUNT([Total Count]) [Error Total]
FROM    ( SELECT    a.OrderItemID ,
                    TaxType ,
                    TaxedItem ,
                    COUNT(*) [Total Count]
          FROM      RFOperations.Hybris.OrderItemTax a
                    JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID = a.OrderItemID
                    JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = oi.OrderId
                                                          AND ro.CountryID = 236
          GROUP BY  a.OrderItemID ,
                    TaxType ,
                    TaxedItem
          HAVING    COUNT(*) > 1
        ) OrderItemTax
UNION ALL 




--CHECKING DUPLICATES TAXTYPES AND TAXEDITEM IN RETURNORDERS TAX TABLE FOR US ORDERS.
SELECT  'Return Tax ' AS [Table] ,
        CASE WHEN COUNT([Total Count]) >= 1 THEN 'Duplicate Validation Failed'
             ELSE 'Duplicate Validation Passed'
        END AS [Results] ,
        COUNT([Total Count]) [Error Total]
FROM    ( SELECT    ro.ReturnOrderID ,
                    TaxType ,
                    TaxedItem ,
                    COUNT(*) [Total Count]
          FROM      RFOperations.Hybris.ReturnOrderTax A
                    JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = A.ReturnOrderID
                                                              AND ro.CountryID = 236
          GROUP BY  ro.ReturnOrderID ,
                    TaxType ,
                    TaxedItem
          HAVING    COUNT(*) > 1
        ) ReturnTax
UNION ALL 




--CHECKING DUPLICATES TAXTYPES AND TAXEDITEM IN RETURNITEM TAX  TABLE FOR US .
SELECT  'ReturnItem' AS [Table] ,
        CASE WHEN COUNT(*) >= 1 THEN 'Duplicate Validation Failed'
             ELSE 'Duplicate Validation Passed'
        END AS [Results] ,
        COUNT([Total Count]) [Error Total]
FROM    ( SELECT    a.OrderItemID ,
                    TaxType ,
                    TaxedItem ,
                    COUNT(*) [Total Count]
          FROM      RFOperations.Hybris.OrderItemTax a
                    JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID = a.OrderItemID
                    JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = oi.OrderId
                                                          AND ro.CountryID = 236
          GROUP BY  a.OrderItemID ,
                    TaxType ,
                    TaxedItem
          HAVING    COUNT(*) > 1
        ) ReturnItem; 


		
--=================================================================================================
--	VALIDATING MISSING RECORDS IN  ORDERTAX ,ORDERITEMTAX, RETURNORDERTAX AND RETURNITEMTAX TABLE
--=================================================================================================


SELECT  'Missing OrderShipingTax  in OrdersTax Table' AS [Validation] ,
        CASE WHEN COUNT(*) > 0
             THEN 'Validation Failed, Missing Orders Shipment tax '
             ELSE 'Validation Passed ,No Missing Orders Shippment Tax '
        END AS Results ,
        COUNT(*) AS [Error Total]
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.Hybris.OrderShipment s ON s.OrderID = ro.OrderID
WHERE   CountryID = 236
        --AND OrderStatusID IN ( 2, 4, 5 )
        AND s.TaxOnShippingCost > 0
        AND NOT EXISTS ( SELECT 1
                         FROM   RFOperations.Hybris.OrdersTax t
                         WHERE  t.OrderID = ro.OrderID
                                AND t.TaxedItem = 'Shipping' )
UNION ALL
SELECT  'Missing OrderHandlingTax  in OrdersTax Table' AS [Validation] ,
        CASE WHEN COUNT(*) > 0
             THEN 'Validation Failed, Missing Orders Handling tax '
             ELSE 'Validation Passed ,No Missing Orders Handling Tax '
        END AS Results ,
        COUNT(*) AS [Error Total]
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.Hybris.OrderShipment s ON s.OrderID = ro.OrderID
WHERE   CountryID = 236
       -- AND OrderStatusID IN ( 2, 4, 5 )
        AND s.TaxOnHandlingCost > 0
        AND NOT EXISTS ( SELECT 1
                         FROM   RFOperations.Hybris.OrdersTax t
                         WHERE  t.OrderID = ro.OrderID
                                AND t.TaxedItem = 'Handling' )
UNION ALL
SELECT  'Missing ItemTax in OrderItemTax Table' AS [Validation] ,
        CASE WHEN COUNT(*) > 0
             THEN 'Validation Failed, Missing OrderItem tax '
             ELSE 'Validation Passed ,No Missing OrderItem Tax '
        END AS Results ,
        COUNT(*) AS [Error Total]
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
WHERE   ro.CountryID = 236
        AND oi.TotalTax > 0
        AND NOT EXISTS ( SELECT 1
                         FROM   RFOperations.Hybris.OrderItemTax t
                         WHERE  oi.OrderItemID = t.OrderItemID
                                AND t.TaxedItem = 'product' )
UNION ALL
SELECT  'Missing ReturnShippingTax  in ReturnTax Table' AS [Validation] ,
        CASE WHEN COUNT(*) > 0
             THEN 'Validation Failed, Missing Return Shipping tax '
             ELSE 'Validation Passed ,No Missing Return Shipping Tax '
        END AS Results ,
        COUNT(*) AS [Error Total]
FROM    RFOperations.Hybris.ReturnOrder ro
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.ReturnOrderID
WHERE   ro.CountryID = 236
        AND o.OrderTypeID = 9
        AND o.TaxAmountShipping > 0
        AND NOT EXISTS ( SELECT 1
                         FROM   RFOperations.Hybris.ReturnOrderTax t
                         WHERE  t.ReturnOrderID = ro.ReturnOrderID
                                AND t.TaxedItem = 'Shipping' )
UNION ALL
SELECT  'Missing ReturnHandlingTax  in ReturnTax Table' AS [Validation] ,
        CASE WHEN COUNT(*) > 0
             THEN 'Validation Failed, Missing Return Handling tax '
             ELSE 'Validation Passed ,No missing Return Handling Tax '
        END AS Results ,
        COUNT(*) AS [Error Total]
FROM    RFOperations.Hybris.ReturnOrder ro
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.ReturnOrderID
WHERE   ro.CountryID = 236
        AND o.OrderTypeID = 9
        AND o.TaxAmountHandling > 0
        AND NOT EXISTS ( SELECT 1
                         FROM   RFOperations.Hybris.ReturnOrderTax t
                         WHERE  t.ReturnOrderID = ro.ReturnOrderID
                                AND t.TaxedItem = 'Handling' )
UNION ALL
SELECT  'Missing ItemTax in ReturnItemTax Table' AS [Validation] ,
        CASE WHEN COUNT(*) > 0
             THEN 'Validation Failed, Missing ReturnItem tax '
             ELSE 'Validation Passed ,No missing ReturnItem Tax '
        END AS Results ,
        COUNT(*) AS [Error Total]
FROM    RFOperations.Hybris.ReturnOrder ro
        JOIN RFOperations.Hybris.ReturnItem oi ON oi.ReturnItemID = ro.ReturnOrderID
        JOIN RodanFieldsLive.dbo.OrderItems i ON i.OrderItemID = oi.ReturnItemID
WHERE   ro.CountryID = 236
        AND i.TaxAmount > 0
        AND i.OrderItemID NOT IN ( SELECT t.ReturnItemID
                         FROM   RFOperations.Hybris.ReturnItemTax t
                         WHERE -- oi.OrderItemID = t.OrderItemID
                               -- AND
								 t.TaxedItem = 'product' );




		--===========================================================================================
			-- VALIDATING TOTAL ORDERTAX = SHIPPING TAX + HANDLING TAX + TOTAL ITEM TAX  WITHIN RFO 	
		--===========================================================================================
					
					
					/* RFO Orders VS OrderItem and Ordershipment*/
                   
IF OBJECT_ID(N'Tempdb..#Temp') IS NOT NULL
    DROP TABLE #Temp;
WITH    OrderIetmTax
          --Product tax
          AS ( SELECT   ro.OrderID ,
                        SUM(oi.TotalTax) [ProductTax]
               FROM     RFOperations.Hybris.Orders ro
                        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
               WHERE    ro.CountryID = 236
                        AND ro.OrderStatusID IN ( 2, 4, 5 )
               GROUP BY ro.OrderID
             ),
        CalculatedTax
          AS ( SELECT   ro.OrderID ,
                        ro.OrderStatusID ,
                        ro.OrderTypeID ,
                        ro.TotalTax AS [OrderTotalTax] ,
                        ro.ProductTax [Order ProductTax] ,
                        t.[ProductTax] ,
                        os.TaxOnShippingCost ,
                        os.TaxOnHandlingCost ,
                        CASE WHEN os.TaxOnShippingCost = 0
                                  AND os.TaxOnHandlingCost = 0
                                  AND t.ProductTax = 0 THEN 0.0
                             WHEN ( os.TaxOnShippingCost > 0
                                    OR os.TaxOnHandlingCost > 0
                                    OR t.[ProductTax] > 0
                                  )
                             THEN t.[ProductTax] + os.TaxOnShippingCost
                                  + os.TaxOnHandlingCost
                        END AS [LogicalTaxAmount] ,
                        CASE WHEN os.TaxOnShippingCost = 0
                                  AND os.TaxOnHandlingCost = 0
                                  AND ro.ProductTax = 0 THEN 0.0
                             WHEN ( os.TaxOnShippingCost > 0
                                    OR os.TaxOnHandlingCost > 0
                                    OR ro.[ProductTax] > 0
                                  )
                             THEN ro.[ProductTax] + os.TaxOnShippingCost
                                  + os.TaxOnHandlingCost
                        END AS [LogicalTaxAmount within RFL]
               FROM     RFOperations.Hybris.Orders ro
                        JOIN OrderIetmTax t ON ro.OrderID = t.OrderID
                        JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
               WHERE    ro.CompletionDate >= '2015-01-01'
                        AND ro.OrderStatusID IN ( 2, 4, 5 )
             )
    SELECT  *
    INTO    #temp
    FROM    CalculatedTax t
    WHERE   [LogicalTaxAmount] <> CAST([OrderTotalTax] AS MONEY);
								
								
								
SELECT  *
FROM    #temp --130K
								--WHERE [LogicalTaxAmount within RFL]<>OrderTotalTax
								--WHERE LogicalTaxAmount<>OrderTotalTax
WHERE   LogicalTaxAmount <> [LogicalTaxAmount within RFL];


SELECT  *
FROM    #temp
WHERE   OrderID IN (
        SELECT  o.OrderID
        FROM    RFOperations.Hybris.Orders o
                JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
        GROUP BY o.OrderID
        HAVING  COUNT(o.OrderID) > 1 );


								
SELECT  COUNT(*)--,t.OrderTypeID
FROM    #temp t
GROUP BY t.OrderTypeID
ORDER BY t.OrderTypeID;

								---Scenario 1 (OrderItemtax,ShippingTax and HandlingTax=0) Records:90K
SELECT  *
FROM    #temp
WHERE   ( ProductTax = 0
          AND TaxOnHandlingCost = 0
          AND TaxOnShippingCost = 0
          AND OrderTotalTax <> 0
        )
        OR ( OrderTotalTax = 0
             AND ( TaxOnHandlingCost > 0
                   OR TaxOnShippingCost > 0
                   OR ProductTax > 0
                 )
           );
                                       

								
SELECT  *
INTO    #t1
FROM    #temp
WHERE   ( ProductTax = 0
          AND TaxOnHandlingCost = 0
          AND TaxOnShippingCost = 0
          AND OrderTotalTax <> 0
        )
        OR ( OrderTotalTax = 0
             AND ( TaxOnHandlingCost > 0
                   OR TaxOnShippingCost > 0
                   OR ProductTax > 0
                 )
           );
                                      
								--Scenario 2 (ProductTax>=999999.00) Records:15,610
SELECT  *
FROM    #temp t
WHERE   NOT EXISTS ( SELECT 1
                     FROM   #t1 t1
                     WHERE  t.OrderID = t1.OrderID )
        AND t.ProductTax >= 999999.00;
                                     


SELECT  *
INTO    #t2
FROM    #temp t
WHERE   NOT EXISTS ( SELECT 1
                     FROM   #t1 t1
                     WHERE  t.OrderID = t1.OrderID )
        AND t.ProductTax >= 999999.00;
								
								
								--Scenario 3 ( No logic 24K)
SELECT  *
FROM    #temp t
WHERE   NOT EXISTS ( SELECT 1
                     FROM   #t1 t2
                     WHERE  t.OrderID = t2.OrderID )
        AND NOT EXISTS ( SELECT 1
                         FROM   #t2 t2
                         WHERE  t.OrderID = t2.OrderID );
															 



                             




				
				-- Tax within RFO Orders table  Producttax +shippingtax+handlingTax<>totaltax in Orders table.

SELECT  ro.OrderID ,
        ro.TotalTax ,
        s.TaxOnShippingCost ,
        s.TaxOnHandlingCost ,
        ro.ProductTax ,
        ISNULL(s.TaxOnShippingCost, 0) + ISNULL(s.TaxOnHandlingCost, 0) [TotalShipHandTax]
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.Hybris.OrderShipment s ON ro.OrderID = s.OrderID
WHERE   ro.CountryID = 236-- AND ro.CompletionDate>='2016-01-01' --89K for Historical.
                           -- AND ro.OrderStatusID IN ( 2, 4, 5 )
        AND ro.ProductTax + ISNULL(s.TaxOnShippingCost, 0)
        + ISNULL(s.TaxOnHandlingCost, 0) <> ro.TotalTax;


							/* RFO OrderTax VS OrderShipment Table */


							--Shipping TaxAmount Validation.
SELECT  ro.OrderID ,
        t.TaxAmount AS [ShippingTaxAmount]
FROM    RFOperations.Hybris.OrdersTax t
        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = t.OrderID
        JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
WHERE   ro.CountryID = 236
        AND t.TaxedItem = 'Shipping'
        AND t.TaxAmount <> os.TaxOnShippingCost;
                                           
                                     -- Handling tax amount validation OrderShipment Vs OrderTax.  
SELECT  ro.OrderID ,
        t.TaxAmount AS [HandlingTaxAmount]
FROM    RFOperations.Hybris.OrdersTax t
        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = t.OrderID
        JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
WHERE   ro.CountryID = 236
        AND t.TaxedItem = 'Handling'
        AND t.TaxAmount <> os.TaxOnHandlingCost;

                           --Total tax from OrderShipment Vs TaxAmount OrdersTax 
						   
WITH    ShippingHandlingTax
          AS ( SELECT   ro.OrderID ,
                        SUM(t.TaxAmount) AS [TaxAmount]
               FROM     RFOperations.Hybris.OrdersTax t
                        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = t.OrderID
               WHERE    ro.CountryID = 236
               GROUP BY ro.OrderID
             )
    SELECT  'Validatoin Totaltax OrderShipment Vs TaxAmount OrdersTax' AS [Validation] ,
            COUNT(*) AS [Total] ,
            CASE WHEN COUNT(*) > 0
                 THEN 'Validation Failed, Total Tax OrderShipment Vs TaxAmount ordersTax '
                 ELSE 'Validation Passed, Totaltax Ordershipment Vs TaxAmount OrdersTax'
            END AS [Result]
    FROM    RFOperations.Hybris.OrderShipment os
            JOIN ShippingHandlingTax sp ON sp.OrderID = os.OrderID
    WHERE   os.TaxOnShippingCost + os.TaxOnHandlingCost <> sp.TaxAmount;


							/* RFO OrderItem Table Vs OrderItemTax Table */
							--Individually One Item with One tax types.

SELECT  'OrderItem TotalTax Vs ItemTax TaxAmount ' AS [Validation] ,
        COUNT(*) [Total] ,
        CASE WHEN COUNT(*) > 0
             THEN 'Validatoin Failed,Orderitem TatolTax Vs TaxAmount OrderItemTax'
             ELSE 'Validation Passed, OrderItem TotalTax Vs TaxAmount  OrderItemTax'
        END AS [Result]
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.Hybris.OrderItem oi ON ro.OrderID = oi.OrderId
        JOIN RFOperations.Hybris.OrderItemTax t ON t.OrderItemID = oi.OrderItemID
WHERE   ro.CountryID = 236
        AND oi.TotalTax <> t.TaxAmount;


									--Group validation of One item with multiple tax types.
WITH    ItemTaxTotal
          AS ( SELECT   oi.OrderItemID ,
                        SUM(t.TaxAmount) [TotalTax]
               FROM     RFOperations.Hybris.OrderItemTax t
                        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID = t.OrderItemID
                        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = oi.OrderId
               WHERE    ro.CountryID = 236
               GROUP BY oi.OrderItemID
             )
    SELECT  'OrderItem TatalTax Vs TaxAmount OrderITemTax  ' AS [Validation] ,
            COUNT(*) [Error Counts] ,
            CASE WHEN COUNT(*) > 0 THEN 'Validation Failed '
                 ELSE 'Validation Passed'
            END AS [Result]
    FROM    RFOperations.Hybris.OrderItem oi
            JOIN ItemTaxTotal t ON t.OrderItemID = oi.OrderItemID
    WHERE   t.TotalTax <> oi.TotalTax;
                             






							--============================================================================
							-- Validating Orders table  ProductTax<>Sum(taxAmount) in ItemTax Table.
							--============================================================================
							



							
WITH    ProductTax
          AS ( SELECT   ro.OrderID ,
                        SUM(oi.TotalTax) AS [OrderItem TotalProductTax]
               FROM     RFOperations.Hybris.OrderItem oi
                        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = oi.OrderId
               WHERE    ro.CountryID = 236 
                                                --AND ro.OrderStatusID IN ( 2, 4, 5 )
               GROUP BY ro.OrderID
             )
    --SELECT  p.*,o.ProductTax AS [OrderTable ProductTax]
							SELECT  COUNT(*) [Total Count]
                            FROM    RFOperations.Hybris.Orders o
                                    JOIN ProductTax p ON p.OrderID = o.OrderID
                            WHERE   p.[OrderItem TotalProductTax] <> o.ProductTax;
 --AND p.ProductTax <999999.00 AND o.ProductTax<>0


					--SELECT MAX(o.StartDate) AS Start FROM RodanFieldsLive.dbo.orderitems oi
					--JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderCustomerID=oi.OrderCustomerID
					--JOIN RodanFieldsLive.dbo.orders o ON o.OrderID=oc.OrderID
					-- WHERE oi.TaxAmount>9000.0


							---Singple Product with One Quantity.
                       
WITH    SingleProductOrders
          AS ( SELECT   ro.OrderID
               FROM     RFOperations.Hybris.Orders ro
                        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
               WHERE    ro.CountryID = 236
                        AND oi.Quantity = 1
               GROUP BY ro.OrderID
               HAVING   COUNT(*) = 1
             ),
        ProductTax
          AS ( SELECT   ro.OrderID ,
                        SUM(oi.TotalTax) AS [OrderItem TotalProductTax]
               FROM     RFOperations.Hybris.OrderItem oi
                        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = oi.OrderId
               WHERE    ro.CountryID = 236 
                                                --AND ro.OrderStatusID IN ( 2, 4, 5 )
               GROUP BY ro.OrderID
             )
    --SELECT  p.* , o.ProductTax AS [OrderTable ProductTax]
							SELECT  COUNT(*) [Total Counts]
                            FROM    RFOperations.Hybris.Orders o
                                    JOIN SingleProductOrders s ON s.OrderID = o.OrderID
                                    JOIN ProductTax p ON p.OrderID = o.OrderID
                            WHERE   p.[OrderItem TotalProductTax] <> o.ProductTax;
 --AND p.ProductTax <999999.00 AND o.ProductTax<>0



							--SingleProductWith more than One Quantity.

WITH    SingleProductwithTwoQntOrders
          AS ( SELECT   ro.OrderID
               FROM     RFOperations.Hybris.Orders ro
                        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
               WHERE    ro.CountryID = 236
                        AND oi.Quantity > 1
               GROUP BY ro.OrderID
               HAVING   COUNT(*) = 1
             ),
        ProductTax
          AS ( SELECT   ro.OrderID ,
                        SUM(oi.TotalTax) AS [OrderItem TotalProductTax]
               FROM     RFOperations.Hybris.OrderItem oi
                        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = oi.OrderId
               WHERE    ro.CountryID = 236 
                                                --AND ro.OrderStatusID IN ( 2, 4, 5 )
               GROUP BY ro.OrderID
             )
    --SELECT  p.* , o.ProductTax AS [OrderTable ProductTax]
							SELECT  COUNT(*) [Total Counts]
                            FROM    RFOperations.Hybris.Orders o
                                    JOIN SingleProductwithTwoQntOrders s ON s.OrderID = o.OrderID
                                    JOIN ProductTax p ON p.OrderID = o.OrderID
                            WHERE   p.[OrderItem TotalProductTax] <> o.ProductTax;
 --AND p.ProductTax <999999.00 AND o.ProductTax<>0





							--Multiple Products Orders.

WITH    SingleProductwithTwoQntOrders
          AS ( SELECT   ro.OrderID
               FROM     RFOperations.Hybris.Orders ro
                        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
               WHERE    ro.CountryID = 236
               GROUP BY ro.OrderID
               HAVING   COUNT(*) > 1
             ),
        ProductTax
          AS ( SELECT   ro.OrderID ,
                        SUM(oi.TotalTax) AS [OrderItem TotalProductTax]
               FROM     RFOperations.Hybris.OrderItem oi
                        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = oi.OrderId
               WHERE    ro.CountryID = 236 
                                                --AND ro.OrderStatusID IN ( 2, 4, 5 )
               GROUP BY ro.OrderID
             )
    --SELECT  p.* , o.ProductTax AS [OrderTable ProductTax]
							SELECT  COUNT(*) [Total Counts]
                            FROM    RFOperations.Hybris.Orders o
                                    JOIN SingleProductwithTwoQntOrders s ON s.OrderID = o.OrderID
                                    JOIN ProductTax p ON p.OrderID = o.OrderID
                            WHERE   p.[OrderItem TotalProductTax] <> o.ProductTax;
 --AND p.ProductTax <999999.00 AND o.ProductTax<>0






SELECT  OrderID ,
        OrderNumber ,
        t.OrderStatusID ,
        t.Itemtax ,
        t.shippingTax ,
        t.shippingTax ,
        ( t.Itemtax + t.shippingTax + TaxOnHandling ) AS CalculatedTotal ,
        t.OrderTotalTax ,
        CASE WHEN ( t.Itemtax + t.shippingTax + TaxOnHandling ) <> OrderTotalTax
             THEN 'TotalOrderTax is not equal to ItemTotalTax and ShipmentHandlingTax'
             ELSE 'TotalOrderTax is  equal to ItemTotalTax and ShipmentHandlingTax'
        END AS Result
FROM    ( SELECT    SUM(oi.TotalTax) AS Itemtax ,
                    MAX(os.TaxOnShippingCost) shippingTax ,
                    MAX(os.TaxOnHandlingCost) AS TaxOnHandling ,
                    MAX(ro.TotalTax) OrderTotalTax ,
                    ro.OrderID ,
                    ro.OrderNumber ,
                    ro.CountryID ,
                    ro.OrderStatusID
          FROM      RFOperations.Hybris.Orders ro
                    JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
                                                             AND ro.CountryID = 236
                    JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
                                                             -- AND ro.OrderStatusID IN (2, 4, 5 )
          GROUP BY  ro.OrderID ,
                    ro.OrderNumber ,
                    ro.CountryID ,
                    ro.OrderStatusID
        ) t
WHERE   CASE WHEN ( t.Itemtax + t.shippingTax + TaxOnHandling ) <> OrderTotalTax  ---Change to equal to see valid result set
                  THEN 'TotalOrderTax is not equal to ItemTotalTax and ShipmentHandlingTax'
             ELSE 'TotalOrderTax is  equal to ItemTotalTax and ShipmentHandlingTax'
        END = 'TotalOrderTax is not equal to ItemTotalTax and ShipmentHandlingTax'
        AND t.CountryID = 236;
						--SELECT * FROM RFOperations.Hybris.orders WHERE OrderID=11877258	
						  
						  
SELECT  ro.OrderID ,
        ro.OrderStatusID ,
        ro.OrderTypeID ,
        ro.TotalTax ,
        ro.ProductTax ,
        os.TaxOnShippingCost ,
        os.TaxOnHandlingCost ,
        ro.ProductTax + os.TaxOnShippingCost + os.TaxOnHandlingCost [CalcultedTotal]
FROM    RFOperations.Hybris.Orders ro
        JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
                                                             -- AND ro.OrderStatusID IN (2, 4, 5 )
WHERE   ro.CountryID = 236
        AND ro.CompletionDate >= '2016-01-01'
        AND ro.TotalTax <> ro.ProductTax + os.TaxOnShippingCost
        + os.TaxOnHandlingCost;
                                  
                               --SELECT * FROM RodanFieldsLive.dbo.OrderTypes


						/* RETRIEVING TAX FIELDS FROM DIFFERENT TABLES */

DECLARE @OrderID INT = 833892;
SELECT  'OrderTax' [Table] ,
        *
FROM    RFOperations.Hybris.Orders
WHERE   OrderID = @OrderID;

SELECT  'Ordershipment' [Table] ,
        *
FROM    RFOperations.Hybris.OrderShipment
WHERE   OrderID = @OrderID;

SELECT  'OrderItem ' [Table] ,
        *
FROM    RFOperations.Hybris.OrderItem
WHERE   OrderId = @OrderID;

SELECT  'OrderTax' [Table] ,
        *
FROM    RFOperations.Hybris.OrdersTax
WHERE   OrderID = @OrderID;

SELECT  'OrderItemTax' [Table] ,
        *
FROM    RFOperations.Hybris.OrderItemTax
WHERE   OrderItemID IN ( SELECT OrderItemID
                         FROM   RFOperations.Hybris.OrderItem
                         WHERE  OrderId = @OrderID );


SELECT  'RFL OrderItem ' [Table] ,
        *
FROM    RodanFieldsLive.dbo.OrderItems(NOLOCK)
WHERE   OrderItemID IN ( SELECT OrderItemID
                         FROM   RFOperations.Hybris.OrderItem
                         WHERE  OrderId = @OrderID );
		
		
		--######################################################################################
		-- RETURN ORDER TAX AND RETURN ITEM TAX VALIDATION .
		--######################################################################################
		
     /* RFL ReturnOrders VS ReturnItem and ReturnShipment*/
                   
IF OBJECT_ID(N'Tempdb..#Error') IS NOT NULL
    DROP TABLE #Error;
WITH    ReturnIetmTax
          --Product tax
          AS ( SELECT   ro.ReturnOrderID ,
                        SUM(i.TaxAmount) [ProductTax]
               FROM     RFOperations.Hybris.ReturnOrder ro
                        LEFT JOIN RFOperations.Hybris.ReturnItem oi ON oi.ReturnOrderID = ro.ReturnOrderID
                        LEFT JOIN RodanFieldsLive.dbo.OrderItems i ON i.OrderItemID = oi.OrderItemID
               WHERE    ro.CountryID = 236
                        AND ro.CompletionDate >= '2016-01-01'
               GROUP BY ro.ReturnOrderID
             ),
        CalculatedTax
          AS ( SELECT   ro.ReturnOrderID ,
                        ro.ReturnStatusID ,
                        o.TaxAmountTotal AS [ReturnOrderTotalTax] ,
                        ro.ProductTax AS [Orders Product Tax ] ,
                        t.[ProductTax] AS [Item ProductTax] ,
                        o.TaxAmountShipping ,
                        o.TaxAmountHandling ,
                        CASE WHEN o.TaxAmountHandling = 0
                                  AND o.TaxAmountShipping = 0
                                  AND t.ProductTax = 0 THEN 0.0
                             WHEN ( o.TaxAmountShipping > 0
                                    OR o.TaxAmountHandling > 0
                                    OR t.[ProductTax] > 0
                                  )
                             THEN t.[ProductTax] + o.TaxAmountShipping
                                  + o.TaxAmountHandling
                        END AS [LogicalTaxAmount] ,
                        CASE WHEN o.TaxAmountHandling = 0
                                  AND o.TaxAmountShipping = 0
                                  AND ro.[ProductTax] = 0 THEN 0.0
                             WHEN ( o.TaxAmountShipping > 0
                                    OR o.TaxAmountHandling > 0
                                    OR ro.[ProductTax] > 0
                                  )
                             THEN ro.[ProductTax] + o.TaxAmountShipping
                                  + o.TaxAmountHandling
                        END AS [LogicalTaxAmountWithinRFLReturns]
               FROM     RFOperations.Hybris.ReturnOrder ro
                        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.ReturnOrderID
                        JOIN ReturnIetmTax t ON ro.ReturnOrderID = t.ReturnOrderID
               WHERE    ro.CompletionDate >= '2016-01-01'
             )
    SELECT  *
    INTO    #Error
    FROM    CalculatedTax t
    WHERE   [LogicalTaxAmount] <> CAST([ReturnOrderTotalTax] AS MONEY);
								
						
								
SELECT  *
FROM    #Error;
 --798 from January 1st 2016 and 9665 from Historical records.

SELECT  COUNT(*) ,
        t.ReturnStatusID
FROM    #Error t
GROUP BY t.ReturnStatusID;
								

								

								---Scenario 1 (OrderItemtax,ShippingTax and HandlingTax=0) Records:90K
SELECT  *
FROM    #Error
WHERE   ( [Item ProductTax] = 0
          AND TaxAmountHandling = 0
          AND TaxAmountShipping = 0
          AND [ReturnOrderTotalTax] <> 0
        )
        OR ( [ReturnOrderTotalTax] = 0
             AND ( TaxAmountHandling > 0
                   OR TaxAmountShipping > 0
                   OR [Item ProductTax] > 0
                 )
           );
                                     
								
SELECT  *
INTO    #E1
FROM    #Error
WHERE   ( [Item ProductTax] = 0
          AND TaxAmountHandling = 0
          AND TaxAmountShipping = 0
          AND [ReturnOrderTotalTax] <> 0
        )
        OR ( [ReturnOrderTotalTax] = 0
             AND ( TaxAmountHandling > 0
                   OR TaxAmountShipping > 0
                   OR [Item ProductTax] > 0
                 )
           );
                                     
                                     
								--Scenario 2 (ProductTax>=999999.00) Records:15,610
SELECT  *
FROM    #Error t
WHERE   NOT EXISTS ( SELECT 1
                     FROM   #E1 t1
                     WHERE  t.ReturnOrderID = t1.ReturnOrderID );
                                 


SELECT  *
INTO    #E2
FROM    #Error t
WHERE   NOT EXISTS ( SELECT 1
                     FROM   #E1 t1
                     WHERE  t.ReturnOrderID = t1.ReturnOrderID );
                                  
								
								
								
															 



                             




				
				-- Tax within RFL returnOrder(Orders) table  Producttax +shippingtax+handlingTax<>totaltax in Orders table.
			

SELECT  ro.OrderID ,
        ro.TotalTax ,
        o.TaxAmountHandling ,
        o.TaxAmountShipping ,
        ro.ProductTax ,
        ISNULL(o.TaxAmountHandling, 0) + ISNULL(o.TaxAmountShipping, 0) [TotalShipHandTax] ,
        ro.ProductTax + ISNULL(o.TaxAmountHandling, 0)
        + ISNULL(o.TaxAmountShipping, 0) AS [Calculated Total Tax]
FROM    RFOperations.Hybris.ReturnOrder ro
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.ReturnOrderID
WHERE   ro.CountryID = 236
        AND ro.ProductTax + ISNULL(o.TaxAmountHandling, 0)
        + ISNULL(o.TaxAmountShipping, 0) <> ro.TotalTax;


							/* RFO ReturnOrderTax VS TaxonShipment in RFL ReturnOrders Table */


							--Shipping TaxAmount Validation.
SELECT  ro.OrderID ,
        t.TaxAmount AS [ShippingTaxAmount]
FROM    RFOperations.Hybris.ReturnOrderTax t
        JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = t.ReturnOrderID
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.ReturnOrderID
WHERE   ro.CountryID = 236
        AND t.TaxedItem = 'Shipping'
        AND t.TaxAmount <> o.TaxAmountShipping;
                                           
                                     -- Handling tax amount validation RFL ReturnOrders Vs OrderTax.  
SELECT  ro.OrderID ,
        t.TaxAmount AS [HandlingTaxAmount]
FROM    RFOperations.Hybris.ReturnOrderTax t
        JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = t.ReturnOrderID
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.ReturnOrderID
WHERE   ro.CountryID = 236
        AND t.TaxedItem = 'Handling'
        AND t.TaxAmount <> o.TaxAmountHandling;

                          


							/* RFL ReturnItem Table Vs ReturnItemTax Table */

SELECT  'OrderItem TotalTax Vs ItemTax TaxAmount ' AS [Validation] ,
        COUNT(*) [Total] ,
        CASE WHEN COUNT(*) > 0
             THEN 'Validatoin Failed,ReturnItem TatolTax Vs TaxAmount ReturnItemTax'
             ELSE 'Validation Passed, ReturnItem TotalTax Vs TaxAmount  ReturnItemTax'
        END AS [Result]
FROM    RFOperations.Hybris.ReturnOrder ro
        JOIN RFOperations.Hybris.ReturnItem oi ON ro.ReturnOrderID = oi.ReturnOrderID
        JOIN RodanFieldsLive.dbo.OrderItems i ON i.OrderItemID = oi.ReturnItemID
        JOIN RFOperations.Hybris.ReturnItemTax t ON t.ReturnItemID = oi.ReturnItemID
WHERE   ro.CountryID = 236
        AND i.TaxAmount <> t.TaxAmount;



WITH    ItemTaxTotal
          AS ( SELECT   i.OrderItemID AS [ReturnItemID] ,
                        SUM(t.TaxAmount) [TotalTax]
               FROM     RFOperations.Hybris.ReturnItemTax t
                        JOIN RFOperations.Hybris.ReturnItem oi ON oi.OrderItemID = t.ReturnItemID
                        JOIN RodanFieldsLive.dbo.OrderItems i ON i.OrderItemID = oi.OrderItemID
                        JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = oi.ReturnOrderID
               WHERE    ro.CountryID = 236
               GROUP BY i.OrderItemID
             )
    SELECT  'OrderItem TotalTax Vs ItemTax TaxAmount ' AS [Validation] ,
            COUNT(*) [Total] ,
            CASE WHEN COUNT(*) > 0
                 THEN 'Validatoin Failed,ReturnItem TatolTax Vs TaxAmount ReturnItemTax'
                 ELSE 'Validation Passed, ReturnItem TotalTax Vs TaxAmount  ReturnItemTax'
            END AS [Result]
    FROM    RodanFieldsLive.dbo.OrderItems oi
            JOIN ItemTaxTotal t ON t.[ReturnItemID] = oi.OrderItemID
    WHERE   t.TotalTax <> oi.TaxAmount;
                             



							


							--============================================================================
							-- Validating ReturnOrders table  ProductTax<>Sum(taxAmount) in ItemTax Table.
							--============================================================================

WITH    ProductTax
          AS ( SELECT   ro.ReturnOrderID ,
                        SUM(oi.TaxAmount) AS [ProductSumTax]
               FROM     RFOperations.Hybris.ReturnItem ri
                        JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = ri.ReturnOrderID
                        JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderItemID = ri.ReturnItemID
               WHERE    ro.CountryID = 236
               GROUP BY ro.ReturnOrderID
             )
    SELECT  p.* ,
            ABS(ro.ProductTax) AS ABSProductTaxReturnTable
    FROM    RFOperations.Hybris.ReturnOrder ro
            JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.ReturnOrderID
                                                 AND o.OrderTypeID = 9
            JOIN ProductTax p ON p.ReturnOrderID = ro.ReturnOrderID
    WHERE   p.[ProductSumTax] <> ABS(ro.[ProductTax]);
 --AND p.ProductTax <999999.00 AND o.ProductTax<>0
					
			

						 

						/* RETRIEVING TAX FIELDS FROM DIFFERENT TABLES */

SELECT TOP 10
        *
FROM    RFOperations.Hybris.ReturnOrder
WHERE   CountryID = 236
ORDER BY 1 DESC;

DECLARE @ReturnOrderID INT = 22876851;
SELECT  ' RFO ReturnOrderTax' [Table] ,
        *
FROM    RFOperations.Hybris.ReturnOrderTax
WHERE   ReturnOrderID = @ReturnOrderID;
SELECT  'RFL ReturnOrder ' [Table] ,
        *
FROM    RodanFieldsLive.dbo.Orders
WHERE   OrderID = @ReturnOrderID;

SELECT  'RFO  ReturnOrder ' [Table] ,
        *
FROM    RFOperations.Hybris.ReturnOrder
WHERE   ReturnOrderID = @ReturnOrderID;
						                       
SELECT  'RFL ReturnItem  ' [Table] ,
        *
FROM    RodanFieldsLive.dbo.Orders o
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
WHERE   o.OrderID = @ReturnOrderID;

SELECT  'RFO ReturnItem  ' [Table] ,
        *
FROM    RFOperations.Hybris.ReturnItem
WHERE   ReturnOrderID = @ReturnOrderID;


SELECT  'ReturnItemTax' [Table] ,
        *
FROM    RFOperations.Hybris.ReturnItemTax
WHERE   ReturnItemID IN ( SELECT    ReturnItemID
                          FROM      RFOperations.Hybris.ReturnItem
                          WHERE     ReturnOrderID = @ReturnOrderID );


                     
		