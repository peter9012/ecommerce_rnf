--=====================================================
-- MANUAL VALIDATION OF RETURNORDERS TYPE TAXES IN RFO
--=====================================================

	DECLARE @ReturnNumber NVARCHAR(25)= 1000510063 ,
		@OrderPK BIGINT ,
		@Returnpk BIGINT;

	SELECT  @Returnpk = PK ,
			@OrderPK = p_associatedorder
	FROM    Hybris..orders
	WHERE   code = @ReturnNumber
			AND ISNULL(p_template, 0) <> 1;
                      
  
	SELECT  @Returnpk Returnpk ,
			@OrderPK AS OriginalOrder ,
			@ReturnNumber AS ReturnNumber; 
  

	SELECT  'Hybris OrdersReturn' AS Entity ,
			PK AS ReturnPk ,
			modifiedTS ,
			totaltaxvalues ,
			totaltax ,
			modifiedTS ,
			p_template
	FROM    Hybris..orders
	WHERE   PK = @Returnpk;
	SELECT  'Hybris Orders' AS Entity ,
			PK AS OrderPK ,
			code AS OrderNumber ,
			totaltaxvalues ,
			totaltax ,
			modifiedTS ,
			p_template
	FROM    Hybris..orders
	WHERE   PK = @OrderPK;
	SELECT  'RFO ReturnOrders' AS Entity ,
			ReturnOrderID ,
			OrderID ,
			ProductTax ,
			TotalTax ,
			TotalGST ,
			TotalPST ,
			TotalHST ,
			CountryID
	FROM    RFOperations.Hybris.ReturnOrder
	WHERE   ReturnOrderID = @Returnpk; 
	SELECT  'RFO Orders' AS Entity ,
			OrderID ,
			ProductTax ,
			TotalTax ,
			TotalGST ,
			TotalPST ,
			TotalHST ,
			CountryID
	FROM    RFOperations.Hybris.Orders
	WHERE   OrderID = @OrderPK; 
   
       
	SELECT  'RFO OrderShipment' Entity ,
			OrderShipmentID ,
			OrderID ,
			TaxOnShippingCost ,
			TaxOnHandlingCost ,
			ShippingMethodID
	FROM    RFOperations.Hybris.OrderShipment
	WHERE   OrderID = @OrderPK; 
	SELECT  'Hybris ReturnOrderEntries' AS Entity ,
			PK AS ReturnEntriesPK ,
			taxvalues ,
			REPLACE(SUBSTRING(taxvalues, CHARINDEX('#', taxvalues) + 1, 5), '#', 0) AS DerivedValue ,
			p_restockingfeetax ,
			p_taxableprice ,
			productpk
	FROM    Hybris..orderentries
	WHERE   orderpk = @Returnpk;
	SELECT  'Hybris OrderEntries' AS Entity ,
			PK AS OrderEntriesPK ,
			taxvalues ,
			REPLACE(SUBSTRING(taxvalues, CHARINDEX('#', taxvalues) + 1, 5), '#', 0) AS DerivedValue ,
			p_restockingfeetax ,
			p_taxableprice ,
			productpk
	FROM    Hybris..orderentries
	WHERE   orderpk = @OrderPK;
	SELECT  'RFO ReturnOrderItem' AS Entity ,
			ReturnItemID ,
			ReturnOrderID ,
			OrderItemID ,
			ReceivedQuantity ,
			[ReStockingFeeTax]
	FROM    RFOperations.Hybris.ReturnItem
	WHERE   ReturnOrderID = @Returnpk;

	SELECT  'RFO ReturnOrderTax' AS Entity ,
			*
	FROM    RFOperations.Hybris.ReturnOrderTax
	WHERE   ReturnOrderID = @Returnpk;
        
	SELECT  'RFO OrderTax' AS Entity ,
			*
	FROM    RFOperations.Hybris.OrdersTax
	WHERE   OrderID = @OrderPK;
  
  
	SELECT  'RFO ReturnOrderItemTax' AS Entity ,
			*
	FROM    RFOperations.Hybris.ReturnItemTax
	WHERE   ReturnItemID IN ( SELECT    PK
							  FROM      Hybris..orderentries
							  WHERE     orderpk = @Returnpk );

								
	SELECT  'RFO OrderItemTax' AS Entity ,
			*
	FROM    RFOperations.Hybris.OrderItemTax
	WHERE   OrderItemID IN ( SELECT PK
							 FROM   Hybris..orderentries
							 WHERE  orderpk = @OrderPK );

	---++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



  
	SELECT  'Checking TotalReturnItemtax Vs OrderTotalItemTax' AS TEST;
	SELECT  *
	FROM    ( SELECT    SUM(rt.TaxAmount) AS Returntax ,
						o.OrderID ,
						ro.CountryID
			  FROM      RFOperations.Hybris.ReturnOrder ro
						JOIN RFOperations.Hybris.Orders o ON o.OrderID = ro.OrderID
						JOIN RFOperations.Hybris.ReturnItem ri ON ri.ReturnOrderID = ro.ReturnOrderID
																  AND ro.CountryID = 40
						JOIN RFOperations.Hybris.ReturnItemTax rt ON rt.ReturnItemID = ri.ReturnItemID
			  WHERE     EXISTS ( SELECT 1
								 FROM   RFOperations.Hybris.OrderItem oi
								 WHERE  oi.OrderItemID = ri.OrderItemID )
						AND ro.ReturnOrderID = @Returnpk
			  GROUP BY  o.OrderID ,
						ro.CountryID
			) t
			JOIN ( SELECT   o.OrderID ,
							SUM(ot.TaxAmount) OrderTax
				   FROM     RFOperations.Hybris.Orders o
							JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
							JOIN RFOperations.Hybris.OrderItemTax ot ON ot.OrderItemID = oi.OrderItemID
																  AND o.CountryID = 40
				   WHERE    EXISTS ( SELECT 1
									 FROM   RFOperations.Hybris.ReturnItem ri
									 WHERE  ri.OrderItemID = oi.OrderItemID )
							AND o.OrderID = @OrderPK
				   GROUP BY o.OrderID ,
							o.CountryID
				 ) t1 ON t1.OrderID = t.OrderID; 
					
				
						--==================================================================
						--Validation Total Tax from Both Tax tables to Order Total Taxtotal.
						--==================================================================


	SELECT  'Checking TotalReturnHeadertax Vs OrderTotalHeaderax' AS TEST;
						;
	WITH    ReturnSumShipHandling
			  AS ( SELECT   ro.ReturnOrderID ,
							SUM(ISNULL(os.TaxAmount, 0)) ReturnSumShipHandling
				   FROM     RFOperations.Hybris.ReturnOrder ro
							JOIN RFOperations.Hybris.ReturnOrderTax os ON os.ReturnOrderID = ro.ReturnOrderID
				   WHERE    ro.ReturnOrderID = @Returnpk
				   GROUP BY ro.ReturnOrderID
				 ),
			OrderShippingHandling
			  AS ( SELECT   ro.ReturnOrderID AS ReturnOrderID ,
							SUM(ISNULL(ot.TaxAmount, 0)) OrderShippingHandling
				   FROM     RFOperations.Hybris.Orders o
							JOIN RFOperations.Hybris.OrdersTax ot ON o.OrderID = ot.OrderID
							JOIN RFOperations.Hybris.ReturnOrder ro ON ro.OrderID = o.OrderID
				   WHERE    ro.ReturnOrderID = @Returnpk
				   GROUP BY ro.ReturnOrderID
				 )
		SELECT  *
		FROM    ReturnSumShipHandling r
				JOIN OrderShippingHandling o ON o.ReturnOrderID = r.ReturnOrderID
		WHERE   r.ReturnSumShipHandling = o.OrderShippingHandling;

			
			

			

  
  
				/* BULK VALIDATION OF RETURNS **/

	--==========================================
	--ORDERPRODUCT TAX VS RETURN PRODUCT TAX
	--==========================================

	SELECT  *
	FROM    ( SELECT    ro.OrderID ,
						SUM(ot.TaxAmount) AS OrderItemTax
			  FROM      RFOperations.Hybris.OrderItemTax ot
						JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderItemID = ot.OrderItemID
						JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = oi.OrderId
															  AND ro.CountryID = 40 
			  WHERE     EXISTS ( SELECT 1
								 FROM   RFOperations.Hybris.ReturnItem ri
								 WHERE  ri.OrderItemID = oi.OrderItemID
										--AND ri.ReceivedQuantity = oi.Quantity 
										)
			  GROUP BY  ro.OrderID
			) Orders
			JOIN ( SELECT   o.OrderID ,
							SUM(rt.TaxAmount) AS ReturnItemTax
				   FROM     RFOperations.Hybris.ReturnItemTax rt
							JOIN RFOperations.Hybris.ReturnItem ri ON ri.ReturnItemID = rt.ReturnItemID
							JOIN RFOperations.Hybris.ReturnOrder r ON r.ReturnOrderID = ri.ReturnOrderID
							JOIN RFOperations.Hybris.Orders o ON o.OrderID = r.OrderID
																 AND o.CountryID = 40
				   GROUP BY o.OrderID
				 ) [Returns] ON [Returns].OrderID = Orders.OrderID
	WHERE   Orders.OrderItemTax<> [Returns].ReturnItemTax; --- Change <> to = to see valid result set.


-- Checking if there is any ReturnOrders not havng any type of taxes.

	SELECT  DISTINCT
			ro.ReturnOrderID ,
			ro.ReturnOrderNumber
	FROM    RFOperations.Hybris.ReturnOrder ro
			JOIN RFOperations.Hybris.ReturnItem ri ON ri.ReturnOrderID = ro.ReturnOrderID
	WHERE   NOT EXISTS ( SELECT 1
						 FROM   RFOperations.Hybris.ReturnOrderTax rt
						 WHERE  rt.ReturnOrderID = ro.ReturnOrderID )
			AND NOT EXISTS ( SELECT 1
							 FROM   RFOperations.Hybris.ReturnItemTax it
							 WHERE  it.ReturnItemID = ri.ReturnItemID )
			AND ro.CountryID = 40
			AND ro.CompletionDate >= '2016-05-06';



	DECLARE @ReturnID BIGINT = 8809299935277;

	SELECT  *
	FROM    RFOperations.Hybris.ReturnOrder
	WHERE   ReturnOrderID = @ReturnID;
	SELECT  *
	FROM    RFOperations.Hybris.ReturnItem
	WHERE   ReturnOrderID = @ReturnID;
	SELECT  *
	FROM    RFOperations.Hybris.ReturnOrderTax
	WHERE   ReturnOrderID = @ReturnID;
	SELECT  *
	FROM    RFOperations.Hybris.ReturnItemTax
	WHERE   ReturnItemID IN ( SELECT    ReturnItemID
							  FROM      RFOperations.Hybris.ReturnItem
							  WHERE     ReturnOrderID = @ReturnID );


		

	SELECT  DISTINCT
			ro.ReturnOrderID ,
			ro.ReturnOrderNumber
	FROM    RFOperations.Hybris.ReturnOrder ro
			JOIN RFOperations.Hybris.ReturnItem ri ON ri.ReturnOrderID = ro.ReturnOrderID
	WHERE   NOT EXISTS ( SELECT 1
						 FROM   RFOperations.Hybris.ReturnItemTax it
						 WHERE  it.ReturnItemID = ri.ReturnItemID )
			AND ro.CountryID = 40
			AND ro.CompletionDate > GETDATE() - 5; --- should be just a shipping and handling tax returns.

			--==================================================
			--Validating DISTINCT VALUES TO CHECK IF ANY UNKNOW
			--==================================================

            SELECT DISTINCT
                    TaxType ,
                    TaxedItem
            FROM    RFOperations.Hybris.ReturnOrderTax T
            WHERE   EXISTS ( SELECT 1
                             FROM   RFOperations.Hybris.ReturnOrder ri
                             WHERE  ri.ReturnOrderID = T.ReturnOrderID
                                    AND ri.CountryID = 40 );

			SELECT DISTINCT
                    TaxType ,
                    TaxedItem FROM RFOperations.Hybris.ReturnItemTax rt
					JOIN RFOperations.hybris.ReturnItem ri ON ri.ReturnItemID = rt.ReturnItemID
					WHERE EXISTS ( SELECT 1
                             FROM   RFOperations.Hybris.ReturnOrder ro
                             WHERE  ro.ReturnOrderID = ri.ReturnOrderID
                                    AND ro.CountryID = 40 );
		

	--======================================================================================
	--CHECKING DUPLICATES TAXTYPES AND TAXEDITEM IN RETURNORDERSTAX TABLE FOR CANADA ORDERS.
	--======================================================================================

	SELECT  ro.ReturnOrderID ,
			TaxType ,
			TaxedItem ,
			COUNT(*)
	FROM    RFOperations.Hybris.ReturnOrderTax A
			JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = A.ReturnOrderID
													   AND ro.CountryID = 40
	GROUP BY ro.ReturnOrderID ,
			TaxType ,
			TaxedItem
	HAVING  COUNT(*) > 1;
				
	--====================================================================================
	--CHECKING DUPLICATES TAXTYPES AND TAXEDITEM IN RETURNORDERSTAX TABLE FOR US ORDERS.
	--====================================================================================

	SELECT  ro.ReturnOrderID ,
			TaxType ,
			TaxedItem ,
			COUNT(*)
	FROM    RFOperations.Hybris.ReturnOrderTax A
			JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = A.ReturnOrderID
													   AND ro.CountryID = 236
	GROUP BY ro.ReturnOrderID ,
			TaxType ,
			TaxedItem
	HAVING  COUNT(*) > 1;

	--======================================================================================
	--CHECKING DUPLICATES TAXTYPES AND TAXEDITEM IN RERURNITEM  TABLE FOR CANADA PRODUCTTAX.
	--======================================================================================

	SELECT  a.ReturnItemID ,
			TaxType ,
			TaxedItem ,
			COUNT(*)
	FROM    RFOperations.Hybris.ReturnItemTax a
			JOIN RFOperations.Hybris.ReturnItem oi ON oi.ReturnItemID = a.ReturnItemID
			JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = oi.ReturnOrderID
													   AND ro.CountryID = 40
	GROUP BY a.ReturnItemID ,
			TaxType ,
			TaxedItem
	HAVING  COUNT(*) > 1;


	--======================================================================================
	--CHECKING DUPLICATES TAXTYPES AND TAXEDITEM IN RERURNITEM  TABLE FOR CANADA PRODUCTTAX.
	--======================================================================================
	SELECT  a.ReturnItemID ,
			TaxType ,
			TaxedItem ,
			COUNT(*)
	FROM    RFOperations.Hybris.ReturnItemTax a
			JOIN RFOperations.Hybris.ReturnItem oi ON oi.ReturnItemID = a.ReturnItemID
			JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = oi.ReturnOrderID
													   AND ro.CountryID = 236
	GROUP BY a.ReturnItemID ,
			TaxType ,
			TaxedItem
	HAVING  COUNT(*) > 1;



	--================================================================================================
	-- CHECKING TAXEDITEM<>'' OR TAXAMOUNT<>0.0 IN RETURNORDER HEADER AND RETURNITEM TAX TABLE IN RFO.
	--=================================================================================================


	SELECT  *
	FROM    RFOperations.Hybris.ReturnOrderTax
	WHERE   TaxType <> 'US'
			AND ( CAST(TaxAmount AS MONEY) = 0.0
				  OR ISNULL(TaxedItem, '') = ''
				);

	SELECT  *
	FROM    RFOperations.Hybris.ReturnItemTax
	WHERE   TaxType <> 'US'
			AND ( CAST(TaxAmount AS MONEY) = 0.0
				  OR ISNULL(TaxedItem, '') = ''
				);







	--=================================================
	--		TO RETRIEVE ORDERS TO RETURN AND VALIDATION 
	--=================================================
	DECLARE @Date DATE = CAST(GETDATE()-2 AS DATE);

	WITH    sigleItemCRP
			  AS ( SELECT TOP 6
							ro.OrderID ,
							ro.OrderNumber ,
							'SingleItemCRP' [OrderScenario]
				   FROM     RFOperations.Hybris.Orders ro
							JOIN Hybris..orders ho ON ho.PK = ro.OrderID
													  AND ho.TypePkString = 8796124676178
							JOIN  RFOperations.dbo.[Templates_2016-05-06_BackUp] b ON ro.AutoShipID = b.PK
							JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
				   WHERE    CAST(ro.CompletionDate AS DATE) = @Date
				   GROUP BY ro.OrderID ,
							ro.OrderNumber
				   HAVING   COUNT(ro.OrderID) = 1
				 ),
			SingleItemPC
			  AS ( SELECT TOP 6
							ro.OrderID ,
							ro.OrderNumber ,
							'SingleItemPC' [OrderScenario]
				   FROM     RFOperations.Hybris.Orders ro
							JOIN Hybris..orders ho ON ho.PK = ro.OrderID
													  AND ho.TypePkString = 8796124708946
							JOIN  RFOperations.dbo.[Templates_2016-05-06_BackUp] b ON ro.AutoShipID = b.PK
							JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
				   WHERE    CAST(ro.CompletionDate AS DATE) = @Date
				   GROUP BY ro.OrderID ,
							ro.OrderNumber
				   HAVING   COUNT(ro.OrderID) = 1
				 ),
			TWOItemCRP
			  AS ( SELECT TOP 6
							ro.OrderID ,
							ro.OrderNumber ,
							'TwoItemPC' [OrderScenario]
				   FROM     RFOperations.Hybris.Orders ro
							JOIN Hybris..orders ho ON ho.PK = ro.OrderID
													  AND ho.TypePkString = 8796124676178
							JOIN  RFOperations.dbo.[Templates_2016-05-06_BackUp] b ON ro.AutoShipID = b.PK
							JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
				   WHERE    CAST(ro.CompletionDate AS DATE) =@Date
				   GROUP BY ro.OrderID ,
							ro.OrderNumber
				   HAVING   COUNT(ro.OrderID) = 2
				 ),
			TWOItemPC
			  AS ( SELECT TOP 6
							ro.OrderID ,
							ro.OrderNumber ,
							'TwoItemPC' [OrderScenario]
				   FROM     RFOperations.Hybris.Orders ro
							JOIN Hybris..orders ho ON ho.PK = ro.OrderID
													  AND ho.TypePkString = 8796124708946
							JOIN  RFOperations.dbo.[Templates_2016-05-06_BackUp] b ON ro.AutoShipID = b.PK
							JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
				   WHERE    CAST(ro.CompletionDate AS DATE) = @Date
				   GROUP BY ro.OrderID ,
							ro.OrderNumber
				   HAVING   COUNT(ro.OrderID) = 2
				 ),
			MoreThan2ItemCRP
			  AS ( SELECT TOP 16
							ro.OrderID ,
							ro.OrderNumber ,
							'MultipleItemCRP' [OrderScenario]
				   FROM     RFOperations.Hybris.Orders ro
							JOIN Hybris..orders ho ON ho.PK = ro.OrderID
													  AND ho.TypePkString = 8796124676178
							JOIN  RFOperations.dbo.[Templates_2016-05-06_BackUp] b ON ro.AutoShipID = b.PK
							JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
				   WHERE    CAST(ro.CompletionDate AS DATE) =@Date
				   GROUP BY ro.OrderID ,
							ro.OrderNumber
				   HAVING   COUNT(ro.OrderID) > 2
				 ),
			MoreThan2ItemPC
			  AS ( SELECT TOP 16
							ro.OrderID ,
							ro.OrderNumber ,
							'MultipleItemPC' [OrderScenario]
				   FROM     RFOperations.Hybris.Orders ro
							JOIN Hybris..orders ho ON ho.PK = ro.OrderID
													  AND ho.TypePkString = 8796124708946
							JOIN  RFOperations.dbo.[Templates_2016-05-06_BackUp] b ON ro.AutoShipID = b.PK
							JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = ro.OrderID
				   WHERE    CAST(ro.CompletionDate AS DATE) = @Date
				   GROUP BY ro.OrderID ,
							ro.OrderNumber
				   HAVING   COUNT(ro.OrderID) > 2
				 ),
			CollectedTable
			  AS ( SELECT   *
				   FROM     sigleItemCRP
				   UNION
				   SELECT   *
				   FROM     SingleItemPC
				   UNION
				   SELECT   *
				   FROM     TWOItemCRP
				   UNION
				   SELECT   *
				   FROM     TWOItemPC
				   UNION
				   SELECT   *
				   FROM     MoreThan2ItemCRP
				   UNION
				   SELECT   *
				   FROM     MoreThan2ItemPC
				 )
		SELECT  *
		FROM    CollectedTable 
		ORDER BY OrderScenario ;
