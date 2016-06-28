  
  --TO LOAD ALL THE TAX ISSUES ORDERS IN TEMP TABLE
  
  IF OBJECT_ID(N'Tempdb..#Temp') IS NOT NULL
    DROP TABLE #Temp;
  WITH  OrderIetmTax
          --Product tax
          AS ( SELECT   o.OrderID ,
                        SUM(ISNULL(oi.TaxAmount, 0)) [ProductTax]
               FROM     RodanFieldsLive.dbo.Orders o
                        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON o.OrderID = oc.OrderID
                        JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
              -- WHERE    o.OrderTypeID NOT IN ( 4, 5, 9 )
               GROUP BY o.OrderID
             ),
        CalculatedTax
          AS ( SELECT   ro.OrderID ,
                        ro.OrderStatusID ,
                        ro.OrderTypeID ,
                        ro.TaxAmountTotal AS [OrderTotalTax] ,
                        t.[ProductTax] ,
                        ro.TaxAmountShipping ,
                        ro.TaxAmountHandling ,
                        CASE WHEN ro.TaxAmountShipping = 0
                                  AND ro.TaxAmountHandling = 0
                                  AND t.[ProductTax] = 0 THEN 0.0
                             WHEN ( ro.TaxAmountShipping > 0
                                    OR ro.TaxAmountHandling > 0
                                    OR t.[ProductTax] > 0
                                  )
                             THEN t.[ProductTax] + ro.TaxAmountShipping
                                  + ro.TaxAmountHandling
                        END AS [LogicalTaxAmount]
               FROM     RodanFieldsLive.dbo.Orders ro
                        JOIN RFOperations.Hybris.Orders o ON o.OrderID = ro.OrderID
                        JOIN OrderIetmTax t ON t.OrderID = ro.OrderID
               --WHERE    ro.StartDate >= '2015-01-01'
               --         AND o.OrderStatusID IN ( 2, 4, 6 )
             )
    SELECT  *
    INTO    #temp
    FROM    CalculatedTax t
    WHERE   [LogicalTaxAmount] <> CAST([OrderTotalTax] AS MONEY);

	--Select * from #temp

	---TO FIND THE COUNT BY YEARS

  SELECT    COUNT(*) AS [Total Orders] ,
            DATEPART(YEAR, o.CompletionDate) AS [Year] ,
            s.Name AS [OrderStatus]
  FROM      #temp T
            JOIN RFOperations.Hybris.Orders o ON o.OrderID = T.OrderID
            JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = o.OrderStatusID
  WHERE     o.CountryID = 236
  GROUP BY  DATEPART(YEAR, o.CompletionDate) ,            s.Name
  ORDER BY  Year;


			---TO FIND THE STATISTICS OF SUMMITTED AND SHIPPED ORDERS FROM JANUARY 1ST 2015 ONLY

  SELECT    COUNT(*) AS [Total Orders] ,
            DATEPART(YEAR, o.CompletionDate) AS [Year] ,
            s.Name AS [OrderStatus]
  FROM      #temp T
            JOIN RFOperations.Hybris.Orders o ON o.OrderID = T.OrderID
            JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = o.OrderStatusID
  WHERE     o.CountryID = 236
            AND o.OrderStatusID IN ( 2, 4, 5 )
            AND o.CompletionDate >= '2015-01-01'
  GROUP BY  DATEPART(YEAR, o.CompletionDate) ,
            s.Name
  ORDER BY  Year;
			--30162
			
			
			--#############################################################################################################
			--Scenario 1:TO FIND ORDERS WITH TOTALTAX=0 BUT ONE OF THE TAX FROM PRODUCTTAX,SHIPPINGTAX AND HANDLINGTAX >0
			--#############################################################################################################
			

								
  SELECT  COUNT(*) AS [Total Orders] --19696 Total Orders.
    /*
             o.OrderID ,
            o.OrderStatusID ,
            o.TaxableTotal AS [Orders Taxable Total] ,
            o.TotalTax [Orders Total Tax] ,
            T.ProductTax ,
            T.TaxAmountShipping ,
            T.TaxAmountHandling ,
            T.ProductTax + T.TaxAmountShipping + T.TaxAmountHandling AS [Logical TotalTax]
	 --  */
  FROM      #temp T
            JOIN RFOperations.Hybris.Orders o ON o.OrderID = T.OrderID
            JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = o.OrderStatusID
  WHERE     o.CountryID = 236
            AND o.OrderStatusID IN ( 2, 4, 5 )
            AND o.CompletionDate >= '2015-01-01'
            AND T.OrderTotalTax = 0
            AND ( T.ProductTax > 0
                  OR T.TaxAmountShipping > 0
                  OR T.TaxAmountHandling > 0
                );

  --##############################################################################
  -- SCENARIO 2: ORDERS TOTALTAX=0 BUT ORDERS TOTALTAXABLE>0 FOR THAT PERIOD
  --##############################################################################

  SELECT  COUNT(*) AS [Total Orders] --17605 Total Orders.
    /*
             o.OrderID ,
            o.OrderStatusID ,
            o.TaxableTotal ,
            o.TotalTax ,
            T.ProductTax ,
            T.TaxAmountShipping ,
            T.TaxAmountHandling ,
            T.ProductTax + T.TaxAmountShipping + T.TaxAmountHandling AS [Logical TotalTax]
	 --  */
  FROM      #temp T
            JOIN RFOperations.Hybris.Orders o ON o.OrderID = T.OrderID
            JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = o.OrderStatusID
  WHERE     o.CountryID = 236
            AND o.OrderStatusID IN ( 2, 4, 5 )
            AND o.CompletionDate >= '2015-01-01'
            AND T.OrderTotalTax = 0
            AND o.TaxableTotal > 0
            AND ( T.ProductTax > 0
                  OR T.TaxAmountShipping > 0
                  OR T.TaxAmountHandling > 0
                );



			--###############################################################################
			-- SCENARIO 3 : ORDERS WITH ONE ORDERITEM WITH THIS ISSUES WHERE TOTALTAX IS ZERO
			--###############################################################################

			
			;
  WITH  SingleItem
          AS ( SELECT   oi.OrderId
               FROM     #temp t
                        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = t.OrderID
               GROUP BY oi.OrderId
               HAVING   COUNT(*) = 1
             )
    SELECT  --COUNT(*) AS [Total Orders] --15900 Total Orders. 
			--  /*
  
         o.OrderID ,
        o.OrderStatusID ,
		o.TaxableTotal,
		o.TotalTax,
        T.ProductTax ,
        T.TaxAmountShipping ,
        T.TaxAmountHandling ,
        T.ProductTax + T.TaxAmountShipping + T.TaxAmountHandling AS [Logical TotalTax]
	 --  */
    FROM    #temp T
            JOIN SingleItem i ON i.OrderId = T.OrderID
            JOIN RFOperations.Hybris.Orders o ON o.OrderID = T.OrderID
            JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = o.OrderStatusID
    WHERE   o.CountryID = 236
            AND o.OrderStatusID IN ( 2, 4, 5 )
            AND o.CompletionDate >= '2015-01-01'
            AND T.OrderTotalTax = 0
            AND o.TaxableTotal > 0
            AND ( T.ProductTax > 0
                  OR T.TaxAmountShipping > 0
                  OR T.TaxAmountHandling > 0
                );


			--#####################################################################################
			-- SCENARIO 4 : ORDERS WITH MULTIPLE ORDERITEM WITH THIS ISSUES WHERE TOTALTAX IS ZERO
			--#####################################################################################

			
			;
  WITH  SingleItem
          AS ( SELECT   oi.OrderId
               FROM     #temp t
                        JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = t.OrderID
               GROUP BY oi.OrderId
               HAVING   COUNT(*) > 1
             )
    SELECT  --COUNT(*) AS [Total Orders] --1705 Total Orders. 
			--  /*
  
         o.OrderID ,
        o.OrderStatusID ,
		o.TaxableTotal,
		o.TotalTax,
        T.ProductTax ,
        T.TaxAmountShipping ,
        T.TaxAmountHandling ,
        T.ProductTax + T.TaxAmountShipping + T.TaxAmountHandling AS [Logical TotalTax]
	 --  */
    FROM    #temp T
            JOIN SingleItem i ON i.OrderId = T.OrderID
            JOIN RFOperations.Hybris.Orders o ON o.OrderID = T.OrderID
            JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = o.OrderStatusID
    WHERE   o.CountryID = 236
            AND o.OrderStatusID IN ( 2, 4, 5 )
            AND o.CompletionDate >= '2015-01-01'
            AND T.OrderTotalTax = 0
            AND o.TaxableTotal > 0
            AND ( T.ProductTax > 0
                  OR T.TaxAmountShipping > 0
                  OR T.TaxAmountHandling > 0
                );



	
			--##########################################################################
			-- SCENARIO 5 : ORDERS TOTALPRODUCTTAX <> SUM OF PRODUCTTAX FROM ORDERITEM 
			--##########################################################################

			
		

  SELECT    --COUNT(*) AS [Total Orders] --12465 Total Orders. 
		--   /*
  
         o.OrderID ,
        o.OrderStatusID ,
		o.TaxableTotal,
		o.TotalTax,
		o.ProductTax AS [Order ProductTax],
        T.ProductTax [Sum of OrderITem ProductTax],
        T.TaxAmountShipping ,
        T.TaxAmountHandling ,
        T.ProductTax + T.TaxAmountShipping + T.TaxAmountHandling AS [Logical TotalTax]
	 --  */
  FROM      #temp T
            JOIN RFOperations.Hybris.Orders o ON o.OrderID = T.OrderID
            JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = o.OrderStatusID
  WHERE     o.CountryID = 236
            AND o.OrderStatusID IN ( 2, 4, 5 )
            AND o.CompletionDate >= '2015-01-01'
            AND T.ProductTax <> o.ProductTax;	
					   
					   
					   
					   
					   
	
			--#################################################################################################
			-- SCENARIO 6 : ORDERS TOTALTAX VS PORDUCTTAX(ORDERS),TAXONSHIPPING(ORDERS),TAXONHANDLING(ORDERS)
			--#################################################################################################

			
		

  SELECT   -- COUNT(*) AS [Total Orders] --12465 Total Orders. 
			 -- /*
  
         o.OrderID ,
        o.OrderStatusID ,
		o.TaxableTotal,
		o.TotalTax,
		o.ProductTax AS [Order ProductTax],
        T.ProductTax [Sum of OrderITem ProductTax],
        T.TaxAmountShipping ,
        T.TaxAmountHandling ,
        T.ProductTax + T.TaxAmountShipping + T.TaxAmountHandling AS [Logical TotalTax]
	 --  */
  FROM      #temp T
            JOIN RFOperations.Hybris.Orders o ON o.OrderID = T.OrderID
            JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = o.OrderStatusID
  WHERE     o.CountryID = 236
            AND o.OrderStatusID IN ( 2, 4, 5 )
            AND o.CompletionDate >= '2015-01-01'
            AND o.TotalTax <> o.ProductTax + T.TaxAmountShipping
            + T.TaxAmountHandling;			