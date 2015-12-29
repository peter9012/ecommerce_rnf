
--------------------------------------------
-- OrderHeader  COUNTS 
-----------------------------------------

 
 SELECT COUNT(*) FROM hybris.dbo.CRP_Bulk_Run_1
     
SELECT  a.pk INTO #missed
FROM    Hybris.dbo.orders (NOLOCK) a
JOIN hybris..CRP_Bulk_Run_1 b ON b.pk=a.p_associatedtemplate 
JOIN Hybris..enumerationvalues v ON  a.statuspk=v.pk
   WHERE CAST(a.createdTS AS DATE )='2015-10-27'  
       EXCEPT   
SELECT o.OrderID
FROM    RFOSERVER.RFOperations.Hybris.Orders o    
        JOIN Hybris.dbo.CRP_Bulk_Run_1 a ON a.pk=o.AutoShipID		
		WHERE CAST(o.CompletionDate AS DATE)='2015-10-27'
		
        
		SELECT b.ordernumber  FROM RFOSERVER.RFOperations.Logging.BoomiError e
		JOIN (SELECT  ho.pk,ho.code AS ordernumber,v.Code FROM Hybris..orders ho
		JOIN Hybris..enumerationvalues v ON v.pk=ho.statuspk
		 WHERE ho.pk IN (SELECT pk FROM #missed) AND v.Code='failed') b ON e.RecordID=b.PK
		 GROUP BY b.ordernumber, b.code,e.ErrorMessage
	

		select * from 
    (SELECT *, CONVERT(varchar(MAX), xmlpayload) as xmlstring 
		FROM RFOSERVER.RFOperations.Boomi.tblXMLPayload 
			where xmlsource in ('Hybris Orders')
			) t1 
where xmlstring like '%1000439018%' 
order by 4 desc
		
		     
SELECT  COUNT(*),v.Code
FROM    Hybris.dbo.orders (NOLOCK) a
JOIN hybris..CRP_Bulk_Run_1 b ON b.pk=a.p_associatedtemplate 
JOIN Hybris..enumerationvalues v ON  a.statuspk=v.pk
  WHERE CAST(a.createdTS AS DATE )='2015-10-27'
  GROUP BY v.Code
        
  
SELECT  COUNT(*),s.Name
FROM    RFOSERVER.RFOperations.Hybris.Orders o    
        JOIN Hybris.dbo.CRP_Bulk_Run_1 a ON a.pk=o.AutoShipID
		JOIN RFOSERVER.RFOperations.RFO_Reference.OrderStatus s
		ON s.OrderStatusId=o.OrderStatusID
		WHERE CAST(o.CompletionDate AS DATE)='2015-10-27'
		GROUP BY s.Name

	

----------------------------------------------------------
       -- Missing Keys 
----------------------------------------------------------

SELECT  OrderID AS RFO_OrderID ,
       b.PK AS Hybris_OrderID ,
        CASE WHEN b.PK IS NULL THEN 'Destination'
             WHEN a.OrderID IS NULL THEN 'Source'
        END AS MissingFROM
--INTO  #OrdersMissing
FROM    ( SELECT  DISTINCT
                    o.OrderID
          FROM      RFOSERVER.RFOperations.Hybris.Orders o
                    JOIN Hybris.dbo.CRP_Bulk_Run_1 a ON a.PK = o.AutoShipID
          WHERE     o.CompletionDate < GETDATE() - 3
        ) a
        FULL OUTER JOIN ( SELECT   a. PK
                          FROM      Hybris.dbo.orders (NOLOCK) a
                                    JOIN Hybris..CRP_Bulk_Run_1 b ON a.PK = b.p_associatedtemplate
                          WHERE     a.createdTS > GETDATE() - 3
                        ) b ON a.OrderID = b.PK
WHERE   ( a.OrderID IS NULL
          OR b.PK IS NULL
        ); 



SELECT  COUNT(*)
FROM    #OrdersMissing;

DECLARE @rowcount INT

IF ( @RowCount > 0 )
    BEGIN 

        SELECT  ' Total Missing  Orders' ,
                @RowCount;

        SELECT  'MissingOrders' ,
                MissingFROM ,
                COUNT(*)
        FROM    #OrdersMissing
        GROUP BY MissingFROM;

        SELECT  *
        FROM    #OrdersMissing
        ORDER BY MissingFROM;

    END;  


----------------------------------------------------------
       --Duplicates  Keys 
----------------------------------------------------------

SELECT  a.OrderNumber ,
        COUNT(pk) AS Hybris_Duplicates
INTO    #Orders_Dups
FROM    RFOSERVER.RFOperations.Hybris.Orders (NOLOCK) a
        JOIN Hybris.dbo.orders (NOLOCK) b ON a.OrderNumber = b.code
WHERE   CountryID = 236
GROUP BY a.OrderNumber
HAVING  COUNT(PK) > 1; 

SELECT  @RowCount = COUNT(*)
FROM    #Orders_Dups;


IF ( @RowCount > 0 )
    BEGIN 

        SELECT  'Duplicate ' + @Country + ' Orders in Hybris' ,
                @RowCount;

        SELECT  'Orders Duplicates' ,
                *
        FROM    #Orders_Dups

    END  

	

----------------------------------------------------------------------------------------------------------------------
--- Load Orders Except 
----------------------------------------------------------------------------------------------------------------------

IF OBJECT_ID('TEMPDB.dbo.#Orders') IS NOT NULL
    DROP TABLE #Orders
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Orders') IS NOT NULL
    DROP TABLE #Hybris_Orders
IF OBJECT_ID('TEMPDB.dbo.#RFO_Orders') IS NOT NULL
    DROP TABLE #RFO_Orders
IF OBJECT_ID('TEMPDB.dbo.#LoadedOrders') IS NOT NULL
    DROP TABLE #LoadedOrders

-----------------------------------------------------------------------------------------------------------------------
--The Data loaded to Hybris ,we need to compare only those order for data comparison.


 
SELECT  DISTINCT
    a.pk
INTO    #LoadedOrders
FROM    Hybris.dbo.orders (NOLOCK) a
JOIN hybris.dbo.CRP_Bulk_Run_1 t ON t.pk=a.p_associatedtemplate
JOIN RFOSERVER.RFOperations.hybris.orders b ON b.OrderID=a.pk
WHERE -- a.TypePkString=8796124676178 
  CAST(a.createdTS AS DATE )='2015-10-27'

-- @HybCountry 
   
CREATE CLUSTERED INDEX MIX_RFLoad ON #LoadedOrders (PK );
        
-----------------------------------------------------------------------------------------------------------------------

--WITH    OrderShippingAddress
--          AS ( SELECT   orderid ,
--                        OrderShippingAddressID ,
--                        ROW_NUMBER() OVER ( PARTITION BY OrderID ORDER BY OrderShippingAddressID ) AS ROWnumber
--               FROM     RFOperations.Hybris.OrderShippingAddress oba
--             ),
--        OrderBillingAddress
--          AS ( SELECT   orderid ,
--                        OrderBillingAddressID ,
--                        ROW_NUMBER() OVER ( PARTITION BY OrderID ORDER BY OrderBillingAddressID ) AS ROWnumber
--               FROM     RFOperations.Hybris.OrderBillingAddress oba
--             ),
--        OrderPayment
--          AS ( SELECT   orderid ,
--                        OrderPaymentID ,
--                        ROW_NUMBER() OVER ( PARTITION BY OrderID ORDER BY OrderPaymentID ) AS ROWnumber
--               FROM     RFOperations.Hybris.OrderPayment oba
--             )


    SELECT  CAST (a.OrderID AS NVARCHAR(100)) AS OrderID ,
            CAST(ISNULL(ShippingCost, 0.00) AS NVARCHAR(100)) AS ShippingCost ,
            CAST(OrderNumber AS NVARCHAR(100)) AS OrderNumber ,
       -- CAST(AutoShipID AS NVARCHAR(100)) AS AutoShipID ,
            CASE WHEN CommissionDate IS NULL
                 THEN CAST (CAST('1900-01-01 00:00:00.000' AS DATETIME) AS NVARCHAR(100))
                 ELSE CAST(CommissionDate AS NVARCHAR(100))
            END AS CommissionDate ,
            CAST(TotalDiscount AS NVARCHAR(100)) AS TotalDiscount ,
            CAST(CV AS NVARCHAR(100)) AS CV ,
            --CAST(CompletionDate AS NVARCHAR(100)) AS CompletionDate ,
            CAST(donotship AS NVARCHAR(100)) AS donotship ,
            CAST(a.SubTotal AS NVARCHAR(100)) AS SubTotal ,
            CAST(a.TotalTax AS NVARCHAR(100)) AS TotalTax ,
            CAST(QV AS NVARCHAR(100)) AS QV ,
            CAST(Total AS NVARCHAR(100)) AS Total ,
            CAST(testorder AS NVARCHAR(100)) AS testorder ,
            CAST(ISNULL(c.HandlingCost, 0.00) AS NVARCHAR(100)) AS HandlingCost ,
            CAST(ConsultantID AS NVARCHAR(100)) AS p_consultantidreceivingcommiss ,
            CAST(ISNULL(TaxExempt, 0) AS NVARCHAR(100)) AS TaxExempt ,
		--CAST (a.ModifiedDate AS NVARCHAR(100)) AS ModifiedDate,
	
-----------------------------------------------------------------
-- Derived/Transformed Columns 
-----------------------------------------------------------------
            CAST(AccountID AS NVARCHAR(100)) AS AccountID ,
            CASE WHEN OrderTypeID IN ( 1, 2, 3 )
                 THEN CAST('Order' AS NVARCHAR(100))
                 WHEN OrderTypeID IN ( 9 )
                 THEN CAST('PCPerksOrder' AS NVARCHAR(100))
                 WHEN OrderTypeID IN ( 12 )
                 THEN CAST ('PulseOrder' AS NVARCHAR(100))
                 WHEN OrderTypeID IN ( 11 )
                 THEN CAST('OverrideOrder' AS NVARCHAR(100))
                 WHEN OrderTypeID IN ( 10 )
                 THEN CAST ('CRPOrder' AS NVARCHAR(100))
                 WHEN OrderTypeID IN ( 5 )
                 THEN CAST('POSOrder' AS NVARCHAR(100))
                 WHEN OrderTypeID IN ( 8 )
                 THEN CAST ('POS Drop-Ship' AS NVARCHAR(100))
                 ELSE NULL
            END AS OrderType, 
            CAST(e.Name AS NVARCHAR(100)) AS OrderStatus, 
            CAST(f.Code AS NVARCHAR(100)) AS Currency 
            --CAST(OrderBillingAddressID AS NVARCHAR(100)) AS BillingAddress ,
            --CAST(OrderPaymentID AS NVARCHAR(100)) AS OrderPayment ,
            --CAST(LTRIM(RTRIM(s.Name)) AS NVARCHAR(100)) AS ShippingMethod ,
            --CAST(OrderShippingAddressID AS NVARCHAR(100)) AS ShippingAddress


	--,CAST ( '<TV<0_1_0_US TOTAL TAX#' + CAST(ISNULL(a.totaltax, 0) AS NVARCHAR(20)) + '#true#'
 --  + '0.0' + '#USD>VT>,<TV<1_1_100001_US SHIPPING TAX#' + CAST(ISNULL(c.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#true#'
 --  + CAST(ISNULL(c.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#USD>VT>,<TV<2_1_300001_CA HANDLING TAX#'
 --  + CAST(ISNULL(c.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#true#'
 --  + CAST(ISNULL(c.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#USD>VT>' AS NVARCHAR(MAX)) AS TotalTaxValue 
 --  , CAST ('<TV<1_1_100001_US General TAX#' + CAST(ISNULL(c.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#true#'
 --   + CAST(ISNULL(c.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#USD>VT>' AS NVARCHAR(MAX))  AS deliverytaxvalues
	--, CAST('<TV<2_1_300001_CA GENERAL TAX#' + CAST(ISNULL(c.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#true#'
 --  + CAST(ISNULL(c.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#USD>VT>' AS NVARCHAR(MAX)) AS HandlingCosttaxvalue

       -- CAST(SiteId AS NVARCHAR(100)) AS SiteId
    INTO    #RFO_Orders
    FROM    RFOSERVER.RFOPerations.Hybris.Orders a --JOIN RFOperations.RFO_Reference.OrderType d ON d.OrderTypeID =a.OrderTypeID 
            JOIN RFOSERVER.RFOperations.RFO_Reference.OrderStatus e ON e.OrderStatusID = a.orderStatusID
            JOIN RFOSERVER.RFOperations.RFO_Reference.Currency f ON f.CurrencyID = a.CurrencyID
			--JOIN #LoadedOrders ho ON ho.pk=a.OrderID
          --  LEFT JOIN RFOperations.etl.OrderDate b ON a.OrderID = b.OrderID
          LEFT JOIN RFOSERVER.RFOperations.Hybris.OrderShipment c ON c.OrderID = a.OrderID
           -- LEFT JOIN OrderBillingAddress g ON a.OrderID = g.OrderID
           -- LEFT JOIN OrderShippingAddress h ON a.OrderID = h.OrderID
            --LEFT JOIN OrderPayment i ON a.OrderID = i.OrderID
           -- LEFT JOIN RFOperations.RFO_reference.ShippingMethod s ON s.ShippingMethodID = c.ShippingMethodID
    WHERE  --a.OrderID=9035234377773
	 EXISTS ( SELECT 1
                     FROM   #LoadedOrders lo
                     WHERE  lo.pk = a.orderid )
            --AND g.ROWnumber = 1
            --AND h.rownumber = 1
           -- AND i.rownumber = 1
      
SELECT  1 ,
        GETDATE()


CREATE CLUSTERED INDEX MIX_RFOrder ON #RFO_Orders (OrderID )

SELECT  CAST (a.PK AS NVARCHAR(100)) AS PK ,
        CAST(CAST (a.deliverycost AS MONEY) AS NVARCHAR(100)) AS deliverycost ,
        CAST(a.code AS NVARCHAR(100)) AS code ,
      -- CAST(p_associatedtemplate AS NVARCHAR(100)) AS p_associatedtemplate ,
        CAST(a.p_commissiondate AS NVARCHAR(100)) AS p_commissiondate ,
        CAST(CAST (a.totaldiscounts AS MONEY) AS NVARCHAR(100)) AS totaldiscounts ,
        CAST(CAST(a.p_totalcv AS MONEY) AS NVARCHAR(100)) AS p_totalcv ,
       -- CAST(a.p_ordercompletiondate AS NVARCHAR(100)) AS p_ordercompletiondate ,
        CAST(ISNULL(a.p_donotship, 0) AS NVARCHAR(100)) AS p_donotship ,
        CAST(CAST (a.subtotal AS MONEY) AS NVARCHAR(100)) AS subtotal ,
        CAST(CAST (a.totaltax AS MONEY) AS NVARCHAR(100)) AS totaltax ,
        CAST(CAST(a.p_totalqv AS MONEY) AS NVARCHAR(100)) AS p_totalqv ,
        CAST(CAST (a.totalprice AS MONEY) AS NVARCHAR(100)) AS totalprice ,
        CAST(ISNULL(a.p_testorder, 0) AS NVARCHAR(100)) AS testorder ,
        CAST(CAST (ISNULL(a.handlingcost, 0) AS MONEY) AS NVARCHAR(100)) AS handlingcost ,
        CAST(a.p_consultantidreceivingcommiss AS NVARCHAR(100)) AS p_consultantidreceivingcommiss ,
        CAST(ISNULL(a.p_taxexempt, 0) AS NVARCHAR(100)) AS p_taxexempt
	--	CAST (a.p_rfmodifiedtime AS NVARCHAR(100))  AS ModifiedTs 




----------------------------------------------------------------------------------------------
-- Derived/transformed/Reference  Columns 
----------------------------------------------------------------------------------------------
        ,
        CAST(b.p_rfAccountID AS NVARCHAR(100)) AS p_rfAccountID ,
        CAST (c.InternalCode AS NVARCHAR(100)) AS OrderType ,
        CASE WHEN a.statuspk = 8796134146139
             THEN CAST ('PARTIALLY SHIPPED' AS NVARCHAR(100))
             ELSE CAST(d.Code AS NVARCHAR(100))
        END AS statuspk ,
        CAST(f.isocode AS NVARCHAR(100)) AS Currency 
        --CAST(paymentaddresspk AS NVARCHAR(100)) AS paymentaddresspk ,
        --CAST(paymentinfopk AS NVARCHAR(100)) AS paymentinfopk ,
        --CASE WHEN s.PK = 8796093251624
        --     THEN CAST('UPS Ground(HD)' AS NVARCHAR(100))
        --     WHEN s.PK = 8796093284392
        --     THEN CAST('Canada Post Ground' AS NVARCHAR(100))
        --     WHEN s.PK = 8796093317160 THEN CAST('Pickup' AS NVARCHAR(100))
        --     ELSE CAST(LTRIM(RTRIM(s.Code)) AS NVARCHAR(100))
        --END AS deliverymodepk ,
        --CAST(deliveryaddresspk AS NVARCHAR(100)) AS deliveryaddresspk
		--,CAST (a.totaltaxvalues AS NVARCHAR(MAX)), 
		--CAST (a.p_deliverytaxvalues AS NVARCHAR(MAX)), 
		--CAST(a.p_handlingcosttaxvalues AS NVACHAR(MAX))

--,CAST( p_siteid AS NVARCHAR (100)) AS p_siteid
INTO    #Hybris_Orders
FROM    Hybris.dbo.Orders a
		--JOIN #LoadedOrders ho ON ho.pk=a.pk
        LEFT JOIN Hybris.dbo.ComposedTypes c ON a.TypePKString = c.PK
        LEFT JOIN Hybris.dbo.EnumerationValues d ON a.statuspk = d.PK
        LEFT JOIN Hybris.dbo.Currencies f ON f.PK = a.CurrencyPK
        LEFT JOIN Hybris.dbo.DeliveryModes s ON s.PK = a.deliverymodepk
        LEFT JOIN Hybris.dbo.Users b ON a.userpk = b.PK
		--WHERE a.pk=9035234377773
WHERE   EXISTS ( SELECT 1
                 FROM   #LoadedOrders lo
                 WHERE  lo.pk = a.PK )
        

SELECT  2 ,
        GETDATE()


CREATE CLUSTERED INDEX MIX_HYOrder ON #Hybris_Orders (PK)


DROP TABLE #orders

SELECT  *
FROM    #Hybris_Orders 

EXCEPT

SELECT  *
--INTO    #Orders
FROM    #RFO_Orders 

SELECT  3 ,
        GETDATE()


SELECT  COUNT(*)
FROM    #Orders


SELECT TOP 2
        *
FROM    #Orders


CREATE CLUSTERED INDEX MIX_Order ON #Orders (OrderID)



----------------------------------------------------------------------------------------
--Column by Column Comparisons 
----------------------------------------------------------------------------------------


TRUNCATE TABLE Hybris.dbo.ErrorLog_Orders


DECLARE @LastRUN DATETIME = '05/01/1901'

DECLARE @I INT = 2 ,
    @C INT = ( SELECT   MAX(MapID)
               FROM     Hybris.dbo.Metadata_Orders
               WHERE    HybrisObject = 'Orders'
             ) 
	



DECLARE @DesKey NVARCHAR(50) 

DECLARE @SrcKey NVARCHAR(50) 

DECLARE @Skip BIT 

WHILE ( @I <= @c )
    BEGIN 

        SELECT  @Skip = ( SELECT    Skip
                          FROM      Hybris.dbo.Metadata_Orders
                          WHERE     MapID = @I
                        );


        IF ( @Skip = 1 )
            SET @I = @I + 1;

        ELSE
            BEGIN 



                DECLARE @DesTemp NVARCHAR(50) = ( SELECT    CASE
                                                              WHEN HybrisObject = 'Orders'
                                                              THEN '#Hybris_orders'
                                                            END
                                                  FROM      Hybris.dbo.Metadata_Orders
                                                  WHERE     MapID = @I
                                                ) 

                DECLARE @DesCol NVARCHAR(50) = ( SELECT Hybris_Column
                                                 FROM   Hybris.dbo.Metadata_Orders
                                                 WHERE  MapID = @I
                                               )

                SET @SrcKey = ( SELECT  RFO_Key
                                FROM    Hybris.dbo.Metadata_Orders
                                WHERE   MapID = @I
                              )

                SET @DesKey = ( SELECT  CASE WHEN HybrisObject = 'Orders'
                                             THEN 'PK'
                                        END
                                FROM    Hybris.dbo.Metadata_Orders
                                WHERE   MapID = @I
                              ); 


                DECLARE @SQL1 NVARCHAR(MAX) = ( SELECT  SqlStmt
                                                FROM    Hybris.dbo.Metadata_Orders
                                                WHERE   MapID = @I
                                              )
                DECLARE @SQL2 NVARCHAR(MAX) = ' 
 UPDATE A 
SET a.Hybris_Value = b. ' + @DesCol
                    + ' FROM Hybris.dbo.ErrorLog_Orders a  JOIN '
                    + @DesTemp + ' b  ON a.RecordID= b.' + @DesKey
                    + ' WHERE a.MAPID = ' + CAST(@I AS NVARCHAR)



                DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
                    ' INSERT INTO Hybris.dbo.ErrorLog_Orders (Identifier,MapID,RecordID,RFO_Value) '
                    + @SQL1 + @SQL2

                BEGIN TRY
                    EXEC sp_executesql @SQL3, N'@ServerMod DATETIME',
                        @ServerMod = @LastRun



                    SET @I = @I + 1
                    SELECT  @I ,
                            GETDATE()

                END TRY

                BEGIN CATCH

                    SELECT  @SQL3

                    SET @I = @I + 1

                END CATCH
            END 

    END 
       

DROP INDEX MIX_Order ON #Orders
DROP INDEX MIX_HyOrder ON #Hybris_Orders 
DROP INDEX MIX_RFOrder ON #RFO_Orders 
DROP INDEX MIX_RFLoad ON #LoadedOrders


SELECT  b.RFO_Column ,
        a.mapid ,
        COUNT(*)
FROM    Hybris.dbo.ErrorLog_Orders a
        JOIN Hybris.dbo.Metadata_Orders b ON b.MapID = a.MapID
GROUP BY b.RFO_Column ,
        a.MapID 


		END


		

