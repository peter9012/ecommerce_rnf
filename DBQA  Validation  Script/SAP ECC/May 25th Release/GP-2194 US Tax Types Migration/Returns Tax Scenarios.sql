 
  -- LOADING ALL RETURNORDERS WITH HAVING TAX AGRREGATION ISSUES.

				   IF OBJECT_ID(N'Tempdb..#Error') IS NOT NULL
				   DROP TABLE #Error

							
							;
                            WITH    ReturnIetmTax
                                      --Product tax
                                      AS ( SELECT   ro.ReturnOrderID ,
                                                    SUM(ISNULL(i.TaxAmount,0)) [ProductTax]
                                           FROM     RFOperations.Hybris.ReturnOrder ro
                                                   LEFT JOIN RFOperations.Hybris.ReturnItem oi ON oi.ReturnOrderID = ro.ReturnOrderID
													LEFT JOIN RodanFieldsLive.dbo.OrderItems i ON i.OrderItemID=oi.OrderItemID
                                           WHERE    ro.CountryID = 236   -- AND ro.CompletionDate>='2016-01-01'                                               
                                           GROUP BY ro.ReturnOrderID
                                         ),
                                    CalculatedTax
                                      AS ( SELECT   ro.ReturnOrderID ,
                                                    ro.ReturnStatusID ,
                                                    o.TaxAmountTotal AS [ReturnOrderTotalTax] ,
													ro.ProductTax AS [Orders Product Tax ],
                                                    t.[ProductTax] AS [Item ProductTax] ,
													o.TaxAmountShipping,
													o.TaxAmountHandling,
                                                    CASE WHEN o.TaxAmountHandling = 0
                                                              AND o.TaxAmountShipping = 0
                                                              AND t.ProductTax = 0
                                                         THEN 0.0
                                                         WHEN (o.TaxAmountShipping > 0
                                                              OR o.TaxAmountHandling > 0
                                                              OR t.[ProductTax] > 0
                                                              )
                                                         THEN t.[ProductTax]
                                                              +o.TaxAmountShipping
                                                              + o.TaxAmountHandling
                                                    END AS [LogicalTaxAmount]
													,
                                                    CASE WHEN o.TaxAmountHandling = 0
                                                              AND o.TaxAmountShipping = 0
                                                              AND ro.[ProductTax] = 0
                                                         THEN 0.0
                                                         WHEN (o.TaxAmountShipping > 0
                                                              OR o.TaxAmountHandling > 0
                                                              OR ro.[ProductTax] > 0
                                                              )
                                                         THEN ro.[ProductTax]
                                                              +o.TaxAmountShipping
                                                              + o.TaxAmountHandling
                                                    END AS [LogicalTaxAmountWithinRFLReturnsProductTaxIncd]
                                           FROM     RFOperations.Hybris.ReturnOrder ro
										   JOIN RodanFieldsLive.dbo.orders o ON o.orderID=ro.ReturnOrderID
                                                    JOIN ReturnIetmTax t ON ro.ReturnOrderID = t.ReturnOrderID                                                   
													--WHERE ro.CompletionDate>='2016-01-01'
                                         )

                                SELECT  *  INTO #Error 
                                FROM    CalculatedTax t
                                WHERE  [LogicalTaxAmount] <> CAST([ReturnOrderTotalTax] AS MONEY);

								SELECT * FROM #Error

								---TO GET THE ISSUES WITH YEARWISE 


                                SELECT  COUNT(*) [Total ReturnOrders] ,
                                        DATEPART(YEAR, o.StartDate) AS [YEAR]
                                FROM    #Error e
                                        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = e.ReturnOrderID
                                GROUP BY DATEPART(YEAR, o.StartDate);


								--- TO FIND THIS ISSUES AFTER JANUARY 1ST 2015.
								    SELECT  COUNT(*) [Total ReturnOrders] ,
                                        DATEPART(YEAR, o.StartDate) AS [YEAR]
                                FROM    #Error e
                                        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = e.ReturnOrderID
										WHERE o.StartDate>='2015-01-01'
                                GROUP BY DATEPART(YEAR, o.StartDate);

			--#############################################################################################################
			--Scenario 1:TO FIND RETURNS WITH TOTALTAX=0 BUT ONE OF THE TAX FROM PRODUCTTAX,SHIPPINGTAX AND HANDLINGTAX >0
			--#############################################################################################################


            SELECT COUNT(*) AS [Total Returns]--  127 RECORDS
			--o.OrderID AS [ReturnOrder], o.OrderTypeID,o.TaxAmountTotal,o.TaxableTotal,e.*
            FROM    #Error e
                    JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = e.ReturnOrderID
            WHERE   o.StartDate >= '2015-01-01'
                    AND o.TaxAmountTotal = 0
                    AND ( e.[Item ProductTax] > 0
                          OR e.TaxAmountShipping > 0
                          OR e.TaxAmountHandling > 0
                        );


						
  --##############################################################################
  -- SCENARIO 2: RETURNS TOTALTAX=0 BUT ORDERS TOTALTAXABLE>0 FOR THAT PERIOD
  --##############################################################################


    SELECT COUNT(*) AS [Total Returns] --0 RECORDS
			--o.OrderID AS [ReturnOrder], o.OrderTypeID,o.TaxAmountTotal,o.TaxableTotal,e.*
            FROM    #Error e
                    JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = e.ReturnOrderID
            WHERE   o.StartDate >= '2015-01-01'
                    AND o.TaxAmountTotal = 0 AND O.TaxAmountTotal>0
                    AND ( e.[Item ProductTax] > 0
                          OR e.TaxAmountShipping > 0
                          OR e.TaxAmountHandling > 0
                        );


			--##################################################################################
			-- SCENARIO 3 : RETURNS WITH ONE RETURNITEM WITH THIS ISSUES WHERE TOTALTAX IS ZERO
			--##################################################################################

            WITH    singleItem
                      AS ( SELECT   ri.ReturnOrderID
                           FROM     #Error e
                                    JOIN RFOperations.Hybris.ReturnItem ri ON ri.ReturnOrderID = e.ReturnOrderID
                           GROUP BY ri.ReturnOrderID
                           HAVING   COUNT(*) = 1
                         )
                SELECT  COUNT(*) AS [Total Returns]--  100 RECORDS
			--o.OrderID AS [ReturnOrder], o.OrderTypeID,o.TaxAmountTotal,o.TaxableTotal,e.*
                FROM    #Error e
                        JOIN singleItem s ON s.ReturnOrderID = e.ReturnOrderID
                        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = e.ReturnOrderID
                WHERE   o.StartDate >= '2015-01-01'
                        AND o.TaxAmountTotal = 0
                        AND ( e.[Item ProductTax] > 0
                              OR e.TaxAmountShipping > 0
                              OR e.TaxAmountHandling > 0
                            );
	--###############################################################################################
			-- SCENARIO 4 : RETURNS WITH MULTIPLE RETURNITEM WITH THIS ISSUES WHERE TOTALTAX IS ZERO
	--###############################################################################################


	  WITH    MultipleItem
                      AS ( SELECT   ri.ReturnOrderID
                           FROM     #Error e
                                    JOIN RFOperations.Hybris.ReturnItem ri ON ri.ReturnOrderID = e.ReturnOrderID
                           GROUP BY ri.ReturnOrderID
                           HAVING   COUNT(*) > 1
                         )
                SELECT  COUNT(*) AS [Total Returns]--  27 RECORDS
			--o.OrderID AS [ReturnOrder], o.OrderTypeID,o.TaxAmountTotal,o.TaxableTotal,e.*
                FROM    #Error e
                        JOIN MultipleItem s ON s.ReturnOrderID = e.ReturnOrderID
                        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = e.ReturnOrderID
                WHERE   o.StartDate >= '2015-01-01'
                        AND o.TaxAmountTotal = 0
                        AND ( e.[Item ProductTax] > 0
                              OR e.TaxAmountShipping > 0
                              OR e.TaxAmountHandling > 0
                            );


			--##########################################################################
			-- SCENARIO 5 : RETURNS TOTALPRODUCTTAX <> SUM OF PRODUCTTAX FROM RETURNITEM 
			--##########################################################################


			 SELECT  COUNT(*) AS [Total Returns]--  2663 RECORDS
			--o.OrderID AS [ReturnOrder], o.OrderTypeID,o.TaxAmountTotal,o.TaxableTotal,e.*
                FROM    #Error e                       
                        JOIN RFOperations.Hybris.ReturnOrder o ON o.ReturnOrderID = e.ReturnOrderID
                WHERE   o.completionDate >= '2015-01-01'
                       AND ABS(o.productTax)<>e.[Item ProductTax]

--############################################################################################################
	-- SCENARIO 6 : RETURNS TOTALTAX VS PORDUCTTAX(RETURNS),TAXONSHIPPING(RETURNS),TAXONHANDLING(RETURNS)
--############################################################################################################





 SELECT COUNT(*) AS [Total Returns]--  555 RECORDS
			--o.OrderID AS [ReturnOrder], o.OrderTypeID,o.TaxAmountTotal,o.TaxableTotal,e.*
 FROM   #Error e
        JOIN RFOperations.Hybris.ReturnOrder o ON o.ReturnOrderID = e.ReturnOrderID
 WHERE  o.CompletionDate >= '2015-01-01'
        AND o.TotalTax<>ABS(o.ProductTax)+e.TaxAmountShipping+e.TaxAmountHandling