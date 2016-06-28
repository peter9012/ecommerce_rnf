   --	MANUAL VALIDATION WITH DIFFERENT ENTITIES  
   --	=========================================

  DECLARE @orderNumber NVARCHAR(25)= 1000331487 ,
            @orderpk BIGINT;

        SET @orderpk = ( SELECT PK
                         FROM   Hybris..orders
                         WHERE  code = @orderNumber
                                AND ISNULL(p_template, 0) <> 1
                       );
  
        SELECT  @orderpk OrderPK ,
                @orderNumber AS Code; 
  

        SELECT  'Hybris Orders' AS Entity ,
                totaltaxvalues ,
                totaltax ,
                modifiedTS ,
                p_template
        FROM    Hybris..orders
        WHERE   PK = @orderpk;
        SELECT  'RFO Orders' AS Entity ,
                OrderID ,
                ProductTax ,
                TotalTax ,
                TotalGST ,
                TotalPST ,
                TotalHST ,
                CountryID
        FROM    RFOperations.Hybris.Orders
        WHERE   OrderID = @orderpk; 
        SELECT  'RFO OrderShipment' Entity ,
                OrderShipmentID ,
                OrderID ,
                TaxOnShippingCost ,
                TaxOnHandlingCost ,
                ShippingMethodID
        FROM    RFOperations.Hybris.OrderShipment
        WHERE   OrderID = @orderpk; 
        SELECT  'Hybris OrderEntries' AS Entity ,
                PK AS OrderItemID ,
                taxvalues ,
                REPLACE(SUBSTRING(taxvalues, CHARINDEX('#', taxvalues) + 1, 5),
                        '#', 0) AS DerivedValue ,
                p_restockingfeetax ,
                p_taxableprice ,
                productpk
        FROM    Hybris..orderentries
        WHERE   orderpk = @orderpk;
        SELECT  'RFO OrderItem' AS Entity ,
                OrderItemID ,
                OrderId ,
                Quantity ,
                TotalTax ,
                TotalGST ,
                TotalPST ,
                TotalHST
        FROM    RFOperations.Hybris.OrderItem
        WHERE   OrderId = @orderpk;
        SELECT  'RFO OrderTax' AS Entity ,
                *
        FROM    RFOperations.Hybris.OrdersTax
        WHERE   OrderID = @orderpk;
        
  
  
  
        SELECT  'RFO OrderItemTax' AS Entity ,
                *
        FROM    RFOperations.Hybris.OrderItemTax
        WHERE   OrderItemID IN ( SELECT PK
                                 FROM   Hybris..orderentries
                                 WHERE  orderpk = @orderpk );



	-- Checking if Totaltax is matching with ItemTotalTax plus Shipping and handling for CANADA Orders.
			-------------------------------------------------------------------------------------------	
				-- Should be NULL if there is not any Such records.
               
                SELECT  'Checking Totaltax is matching with ItemTotalTax plus Shipping and handling' AS TEST;
                SELECT  OrderID ,
                        CASE WHEN ( t.Itemtax + t.shippingTax + TaxOnHandling ) <> OrderTotalTax
                             THEN 'TotalTax is not equal to ItemTotalTax and ShipmentHandlingTax'
                             ELSE 'TotalTax is  equal to ItemTotalTax and ShipmentHandlingTax'
                        END AS Result
                FROM    ( SELECT    SUM(oi.TotalTax) AS Itemtax ,
                                    AVG(os.TaxOnShippingCost) shippingTax ,
                                    AVG(os.TaxOnHandlingCost) AS TaxOnHandling ,
                                    AVG(ro.TotalTax) OrderTotalTax ,
                                    ro.OrderID ,
                                    ro.CountryID
                          FROM      RFOperations.Hybris.Orders ro
                                    JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
                                    JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
                          WHERE     ro.OrderID = @orderpk
                          GROUP BY  ro.OrderID ,
                                    ro.CountryID            
                        ) t
					
					
					
					----Validation Total Tax from Both Tax tables to Order Total Taxtotal.
						
					;
                    WITH    SumShipHandling
                              AS ( SELECT   ro.OrderID ,
                                            SUM(ISNULL(os.TaxAmount,0)) SumShipHandling
                                   FROM     RFOperations.Hybris.Orders ro
                                           LEFT JOIN RFOperations.Hybris.OrdersTax os ON os.OrderID = ro.OrderID
                                   WHERE    ro.OrderID = @orderpk
                                   GROUP BY ro.OrderID
                                 ),
                            ProductTax
                              AS ( SELECT   oi.OrderId ,
                                            SUM(ISNULL(oit.TaxAmount, 0)) ProductTax
                                   FROM     RFOperations.Hybris.OrderItem oi
                                            JOIN RFOperations.Hybris.OrderItemTax oit ON oit.OrderItemID = oi.OrderItemID
                                   WHERE    oi.OrderId = @orderpk
                                   GROUP BY oi.OrderId
                                 ),
                            CalculatedTotalTax
                              AS ( SELECT   a.OrderId ,
                                            a.ProductTax + b.SumShipHandling AS Totaltax
                                   FROM     ProductTax a
                                            JOIN SumShipHandling b ON a.OrderId = b.OrderID
                                 )
                        SELECT  a.OrderId ,
                                a.Totaltax ,
                                ro.TotalTax
                        FROM    CalculatedTotalTax a
                                JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = a.OrderId
                        WHERE  ro.CountryID=40 AND  ro.TotalTax <> a.Totaltax;

				
			--	8808971239469 OrderID 
			
			
            SELECT  'Checking ItemTax Total Not Equals to sum of ItemTaxTypes' AS TEST;
            SELECT  *
            FROM    ( SELECT    OrderItemID ,
                                TotalTax
                      FROM      RFOperations.Hybris.OrderItem
                      WHERE     OrderId = @orderpk
                    ) Item
                    JOIN ( SELECT   oi.OrderItemID ,
                                    SUM(ot.TaxAmount) ItemTaxTotal
                           FROM     RFOperations.Hybris.Orders ro
                                    JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
                                    JOIN RFOperations.Hybris.OrderItemTax ot ON ot.OrderItemID = oi.OrderItemID
                           WHERE    ro.OrderID = @orderpk
                           GROUP BY oi.OrderItemID
                         ) Tax ON Tax.OrderItemID = Item.OrderItemID;
				




                    SELECT  'Checking ItemTax Total Not Equals to sum of ItemTaxTypes' AS TEST;
                    SELECT  *
                    FROM    ( SELECT    OrderId ,
                                        SUM(TotalTax) ItemtableTotalTax
                              FROM      RFOperations.Hybris.OrderItem
                              WHERE     OrderId = @orderpk
                              GROUP BY  OrderId
                            ) Item
                            JOIN ( SELECT   oi.OrderId ,
                                            SUM(ot.TaxAmount) ItemTaxTableTotal
                                   FROM     RFOperations.Hybris.Orders ro
                                            JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
                                            JOIN RFOperations.Hybris.OrderItemTax ot ON ot.OrderItemID = oi.OrderItemID
                                   WHERE    ro.OrderID = @orderpk
                                   GROUP BY oi.OrderId
                                 ) Tax ON Tax.OrderId = Item.OrderId;
				
					
                SELECT  'TotalTax OrderShipment VS TotalTax OrdersTax ';

               SELECT * FROM (
			    SELECT  OrderID ,
                       TaxOnShippingCost+ TaxOnHandlingCost AS  TotalTaxShipment
                FROM    RFOperations.Hybris.OrderShipment
                WHERE   OrderID = @orderpk
				) t
				JOIN (
                SELECT  OrderID ,
                        SUM(TaxAmount) TotalTax
                FROM    RFOperations.Hybris.OrdersTax
                WHERE   OrderID = @orderpk
				GROUP BY OrderID)t1 ON t1.OrderID = t.OrderID
				WHERE t.TotalTaxShipment=CAST(t1.TotalTax AS MONEY)


			
	--=================================================================================================================				
					
					
					
					
						
						
						-- AUTOMATICALLY VALIDATING ALL THE ORDERS HAVING TOTAL TAX CALCULATION FOR CANADA ORDERS.			
                        SELECT  OrderID ,
                                OrderNumber ,
								t.OrderStatusID,
								t.Itemtax,t.shippingTax,t.shippingTax,( t.Itemtax + t.shippingTax
                                            + TaxOnHandling ) AS CalculatedTotal,t.OrderTotalTax,
                                CASE WHEN ( t.Itemtax + t.shippingTax
                                            + TaxOnHandling ) <> OrderTotalTax
                                     THEN 'TotalOrderTax is not equal to ItemTotalTax and ShipmentHandlingTax'
                                     ELSE 'TotalOrderTax is  equal to ItemTotalTax and ShipmentHandlingTax'
                                END AS Result
                        FROM    ( SELECT    SUM(oi.TotalTax) AS Itemtax ,
                                            AVG(os.TaxOnShippingCost) shippingTax ,
                                            AVG(os.TaxOnHandlingCost) AS TaxOnHandling ,
                                            AVG(ro.TotalTax) OrderTotalTax ,
                                            ro.OrderID ,
                                            ro.OrderNumber ,
                                            ro.CountryID,
											ro.OrderStatusID
                                  FROM      RFOperations.Hybris.Orders ro
                                            JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
                                                             AND ro.OrderStatusID IN ( 2, 4, 5 )
                                                             
                                            JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
                       WHERE   ro.CompletionDate>='2016-05-06'
                                  GROUP BY  ro.OrderID ,
                                            ro.OrderNumber ,
                                            ro.CountryID,
											ro.OrderStatusID
											
                                ) t
                        WHERE   CASE WHEN ( t.Itemtax + t.shippingTax
                                            + TaxOnHandling ) <> OrderTotalTax  ---Change to equal to see valid result set
                                     THEN 'TotalOrderTax is not equal to ItemTotalTax and ShipmentHandlingTax'
                                     ELSE 'TotalOrderTax is  equal to ItemTotalTax and ShipmentHandlingTax'
                                END ='TotalOrderTax is not equal to ItemTotalTax and ShipmentHandlingTax'
                                AND t.CountryID = 40;
						--SELECT * FROM RFOperations.Hybris.orders WHERE OrderID=11877258	
						
							
					--===============================================================================
					----Validation Total Tax from Both Tax tables to Order Total Taxtotal Within RFO.
					--===============================================================================
								
						
					;
                    WITH    SumShipHandling
                              AS ( SELECT   ro.OrderID ,
                                            SUM(ISNULL(os.TaxAmount, 0)) SumShipHandling
                                   FROM     RFOperations.Hybris.Orders ro
                                            LEFT  JOIN RFOperations.Hybris.OrdersTax os ON os.OrderID = ro.OrderID
                                   GROUP BY ro.OrderID
                                 ),
                            ProductTax
                              AS ( SELECT   oi.OrderId ,
                                            SUM(ISNULL(oit.TaxAmount, 0)) ProductTax
                                   FROM     RFOperations.Hybris.OrderItem oi
                                            JOIN RFOperations.Hybris.OrderItemTax oit ON oit.OrderItemID = oi.OrderItemID
                                   GROUP BY oi.OrderId
                                 ),
                            CalculatedTotalTax
                              AS ( SELECT   a.OrderId ,
                                            a.ProductTax + b.SumShipHandling AS Totaltax
                                   FROM     ProductTax a
                                            JOIN SumShipHandling b ON a.OrderId = b.OrderID
                                 )
                        SELECT  a.OrderId ,
                                CAST(a.Totaltax AS MONEY) CalculatedTotaltax ,
                                ro.TotalTax
                        FROM    CalculatedTotalTax a
                                JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = a.OrderId
                                                              AND ro.CountryID = 40
                        WHERE   ro.TotalTax <> a.Totaltax				 ---Change to equal to see valid result set
                                AND ro.CompletionDate>='2016-05-06'


								
								
                                    SELECT  'Checking ItemTax Total Not Equals to sum of ItemTaxTypes' AS TEST
                                    SELECT  *
                                    FROM    ( SELECT    OrderItemID ,
                                                        TotalTax
                                              FROM      RFOperations.Hybris.OrderItem oi
                                              WHERE     EXISTS ( SELECT
                                                              1
                                                              FROM
                                                              RFOperations.Hybris.Orders ro
                                                              WHERE
                                                              ro.OrderID = oi.OrderID
                                                              and ro.countryID = 40 )--Validation Only Canada Orders
                                            ) Item
                                            JOIN ( SELECT   oi.OrderItemID ,
                                                            SUM(ot.TaxAmount) ItemTaxTotal
                                                   FROM     RFOperations.Hybris.Orders ro
                                                            JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
                                                              AND ro.CountryID = 40
                                                            JOIN RFOperations.Hybris.OrderItemTax ot ON ot.OrderItemID = oi.OrderItemID
															WHERE ro.CompletionDate>='2016-05-06'
                                                   GROUP BY oi.OrderItemID
                                                 ) Tax ON Tax.OrderItemID = Item.OrderItemID
                                    WHERE   Item.TotalTax <>Tax.ItemTaxTotal	 ---Change to equal to see valid result set
				




                    SELECT  'Checking ItemTax Total Vs sum of ItemTaxTypes' AS TEST
                    SELECT  *
                    FROM    ( SELECT    OrderId ,
                                        SUM(TotalTax) ItemtableTotalTax
                              FROM      RFOperations.Hybris.OrderItem
                              GROUP BY  OrderId
                            ) Item
                            JOIN ( SELECT   oi.OrderId  ,      SUM(ot.TaxAmount) ItemTaxTableTotal
                                   FROM     RFOperations.Hybris.Orders ro
                                            JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID AND ro.CountryID=40
                                            JOIN RFOperations.Hybris.OrderItemTax ot ON ot.OrderItemID = oi.OrderItemID
                                   GROUP BY oi.OrderId
                                 ) Tax ON Tax.OrderId =Item.OrderId  
								 WHERE Item.ItemtableTotalTax<>Tax.ItemTaxTableTotal			 ---Change to equal to see valid result set

					

					
--- TOTAL TAX (SHIPPING AND HANDLING TAX ) ORDERSHIPMENT VS TOTALTAX (SHIPPING AND HANDLING TAX) ORDERSTAX TABLES.


         SELECT * FROM (
			    SELECT  OrderID ,
                       TaxOnShippingCost+ TaxOnHandlingCost AS  TotalTaxShipment
                FROM    RFOperations.Hybris.OrderShipment                
				) t
				JOIN (
                SELECT  OrderID ,
                        SUM(TaxAmount) TotalTax
                FROM    RFOperations.Hybris.OrdersTax  WHERE ServerModifiedDate>='2016-05-06'          
				GROUP BY OrderID)t1 ON t1.OrderID = t.OrderID
				WHERE t.TotalTaxShipment<>CAST(t1.TotalTax AS MONEY)  ---Change to equal to see valid result set



------------------------------------------------------------------------------------




--CHECKING DUPLICATES TAXTYPES AND TAXEDITEM IN ORDERSTAX TABLE FOR CANADA ORDERS.

                                 SELECT ro.OrderID ,
                                        TaxType ,
                                        TaxedItem ,
                                        COUNT(*)
                                 FROM   RFOperations.Hybris.OrdersTax A
								 JOIN RFOperations.Hybris.orders ro ON ro.OrderID = A.OrderID AND ro.CountryID=40
                                 GROUP BY ro.OrderID ,
                                        TaxType ,
                                        TaxedItem
                                 HAVING COUNT(*) > 1;
				

--CHECKING DUPLICATES TAXTYPES AND TAXEDITEM IN ORDERSTAX TABLE FOR US ORDERS.

                                 SELECT ro.OrderID ,
                                        TaxType ,
                                        TaxedItem ,
                                        COUNT(*)
                                 FROM   RFOperations.Hybris.OrdersTax A
								 JOIN RFOperations.Hybris.orders ro ON ro.OrderID = A.OrderID AND ro.CountryID=236
                                 GROUP BY ro.OrderID ,
                                        TaxType ,
                                        TaxedItem
                                 HAVING COUNT(*) > 1;


--CHECKING DUPLICATES TAXTYPES AND TAXEDITEM IN ORDERITEM  TABLE FOR CANADA PRODUCTTAX.

                                 SELECT a.orderitemID ,
                                        TaxType ,
                                        TaxedItem ,
                                        COUNT(*)
                                 FROM   RFOperations.Hybris.OrderItemTax a
								 JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID=a.orderItemId
								 JOIN RFOperations.Hybris.orders ro ON ro.OrderID = oi.OrderID AND ro.CountryID=40
                                 GROUP BY a.orderitemID ,
                                        TaxType ,
                                        TaxedItem
                                 HAVING COUNT(*) > 1;



---ISSUE NO 1: 205K DUPLICATE RECORDS IN ORDERSHIPMENT TABLE FOR US.

                                    SELECT  COUNT(*) ,
                                            OrderID ,
                                            ShippingCost ,
                                            HandlingCost
                                    FROM    RFOperations.Hybris.OrderShipment (NOLOCK)os
                                    WHERE   EXISTS ( SELECT 1
                                                     FROM   RFOperations.Hybris.Orders(NOLOCK) ro
                                                     WHERE  ro.OrderID = os.OrderID
                                                            AND ro.CountryID = 236
															 )
                                    GROUP BY OrderID ,
                                            ShippingCost ,
                                            HandlingCost
                                    HAVING  COUNT(*) > 1 -- 205K DUPLICATES IN PROD , there is Multiple BUG ticket for this Issues.

									 DECLARE @OrderID INT= 7324415

									SELECT * FROM RFOperations.Hybris.OrderShipment WHERE OrderID=@OrderID

--ISSUE NO 2: DUPLICATES TAXTYPES AND TAXEDITEM IN ORDERITEM  TABLE FOR US  PRODUCTTAX.

                                 SELECT a.orderitemID ,
                                        TaxType ,
                                        TaxedItem ,
                                        COUNT(*)
                                 FROM   RFOperations.Hybris.OrderItemTax a
								 JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID=a.orderItemId
								 JOIN RFOperations.Hybris.orders ro ON ro.OrderID = oi.OrderID AND ro.CountryID=236
                                 GROUP BY a.orderitemID ,
                                        TaxType ,
                                        TaxedItem
                                 HAVING COUNT(*) > 1;	--83 RECORDS IN STG2. there is Multiple BUG ticket for this Issues.
								
								
                                 DECLARE @OrderID INT= 22860073
								 
                                 SELECT 'Orders' AS [Entity] ,
                                        *
                                 FROM   RFOperations.Hybris.Orders
                                 WHERE  OrderID = @OrderID
                                 SELECT 'OrdersShipment' AS [Entity] ,
                                        *
                                 FROM   RFOperations.Hybris.OrderShipment
                                 WHERE  OrderID = @OrderID
                                 SELECT 'OrderItem' AS [Entity] ,
                                        *
                                 FROM   RFOperations.Hybris.OrderItem
                                 WHERE  OrderId = @OrderID
                                 SELECT 'OrdersTax' AS [Entity] ,
                                        *
                                 FROM   RFOperations.Hybris.OrdersTax
                                 WHERE  OrderID = @OrderID
                                 SELECT 'OrdersItemTax' AS [Entity] ,
                                        *
                                 FROM   RFOperations.Hybris.OrderItemTax
                                 WHERE  OrderItemID IN (
                                        SELECT  OrderItemID
                                        FROM    RFOperations.Hybris.OrderItem
                                        WHERE   OrderId = @OrderID )
								 


--CHECKING CANADA TAXEDITEM<>'' AND TAXAMOUNT<>0.0 FOR ORDER HEADER AND ORDERITEM  LEVEL TAX TABLE.

SELECT * FROM RFOperations.hYBRIS.OrdersTax WHERE TaxType<>'US' AND 
(CAST(TaxAmount AS MONEY)=0.0 OR ISNULL(TaxedItem,'')='')


SELECT * FROM RFOperations.hYBRIS.OrderItemTax WHERE TaxType<>'US' AND 
(CAST(TaxAmount AS MONEY)=0.0 OR ISNULL(TaxedItem,'')='')

--CHECKING US TAXEDITEM<>'' AND TAXAMOUNT<>0.0 FOR ORDER HEADER AND ORDERITEM  LEVEL TAX TABLE.

SELECT * FROM RFOperations.hYBRIS.OrdersTax WHERE TaxType='US' AND 
(CAST(TaxAmount AS MONEY)=0.0 OR ISNULL(TaxedItem,'')='')


SELECT * FROM RFOperations.hYBRIS.OrderItemTax WHERE TaxType='US' AND 
(CAST(TaxAmount AS MONEY)=0.0 OR ISNULL(TaxedItem,'')='')



	
						
								 
								 */
								 
										