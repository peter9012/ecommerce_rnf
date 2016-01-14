----------------------------------------------------------------------------------
USE RFOperations
---------------------------------------------------------------------------------------------

GO 
CREATE PROCEDURE Migration_Hybris_ConsignmentsOneOrder_QA   @OrderID BIGINT
AS

BEGIN 

DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-06-01' ,
    @RFOCount BIGINT ,
    @RowCount BIGINT , @OrderId BIGINT =8957172056109
,
    @HybrisPtCount BIGINT ,
    @HybrisCount BIGINT; 
DECLARE @RFOCountry INT = ( SELECT  CountryID
                            FROM    RFOperations.RFO_Reference.Countries (NOLOCK)
                            WHERE   Alpha2Code = @Country
                          ) ,
    @HybCountry BIGINT = ( SELECT   PK
                           FROM     Hybris.dbo.currencies (NOLOCK)
                           WHERE    isocode = 'USD'
                         );
DECLARE @ReturnOrderType BIGINT = ( SELECT  PK
                                    FROM    Hybris.dbo.composedtypes (NOLOCK)
                                    WHERE   InternalCode = 'RFReturnOrder'
                                  );

-----------------------------------------------------------------------------------

IF OBJECT_ID('TEMPDB.dbo.#LoadedConsignment') IS NOT NULL
    DROP TABLE #LoadedConsignment;

SELECT  pte.PK
INTO    #LoadedConsignment
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..consignments pt ON o.PK = pt.p_order
        INNER JOIN Hybris..consignmententries pte ON pte.p_consignment = pt.PK
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
		AND o.PK = @OrderId;			
			
			
--------------------------------------------------------------------------------------			
			

IF OBJECT_ID('TEMPDB.dbo.#SPItem') IS NOT NULL
    DROP TABLE #SPItem;

IF OBJECT_ID('TEMPDB.dbo.#Hybris_SPItem') IS NOT NULL
    DROP TABLE #Hybris_SPItem;

IF OBJECT_ID('TEMPDB.dbo.#RFO_SPItem') IS NOT NULL
    DROP TABLE #RFO_SPItem;

SELECT DISTINCT CAST(TrackingNumber AS NVARCHAR(100)) AS TrackingID ,
        CAST(ShipDate AS NVARCHAR(100)) AS ShipDate ,
        CAST(ShipNumber AS NVARCHAR(100)) AS ShipNumber ,
        CAST(OrderShipmentPackageItemID AS NVARCHAR(100)) AS OrderShipmentPackageItemID ,
        CAST(a.Quantity AS NVARCHAR(100)) AS Quantity ,
        CAST(TrackingNumber AS NVARCHAR(100)) AS TrackingNumber
        , CAST(a.OrderItemId AS NVARCHAR(100)) AS OrderItemId ,
       CAST (c.Name AS NVARCHAR(100)) AS ShippingMethod1 ,
        CAST (CONCAT('a', OrderNumber) AS NVARCHAR(100)) AS OrderNumber ,
        CAST(ShipStatus AS NVARCHAR(100)) AS ShipStatus ,
        CAST (Carrier AS NVARCHAR(100)) AS Carrier ,
       CAST (e.OrderShippingAddressID AS NVARCHAR(100)) AS ShippingAddress ,
        CAST (b.OrderID AS NVARCHAR(100)) AS OrderID ,
        CAST(c.Name AS NVARCHAR(100)) AS ShippingMethod
INTO    #RFO_SPItem
FROM    RFOperations.Hybris.OrderShipmentPackageItem a
        	JOIN RFOperations.Hybris.OrderItem f ON f.OrderItemID =a.OrderItemID AND f.OrderID =a.OrderID
			 JOIN RFOperations.Hybris.Orders b ON a.OrderID = b.OrderID
	        JOIN RFOperations.Hybris.OrderShipment d ON d.OrderID = b.OrderID AND d.OrderShipmentID =a.OrderShipmentID      
			JOIN RFOperations.Hybris.OrderShippingAddress e ON e.OrderID = b.OrderID 
			JOIN Hybris.dbo.Addresses ad ON ad.PK =e.OrdershippingaddressID AND ad.p_rfAddressID = d.OrderShipmentID 
		LEFT JOIN RFOperations.RFO_Reference.ShippingMethod c ON d.ShippingMethodID = c.ShippingMethodID
	
		WHERE   EXISTS (SELECT 1 FROM #LoadedConsignment lc WHERE lc.PK =a.OrderShipmentPackageItemID)

CREATE CLUSTERED INDEX MIX_RFSPItem ON #RFO_SPItem (OrderID);

--SELECT * FROM  RFOperations.RFO_Reference.ShippingMethod

SELECT  CAST(p_trackingid AS NVARCHAR(100)) AS p_trackingid ,
        CAST(p_shippingdate AS NVARCHAR(100)) AS p_shippingdate ,
        CAST(p_shipnumber AS NVARCHAR(100)) AS p_shipnumber
		,CAST( b.PK AS NVARCHAR (100)) AS  PK
,CAST( p_quantity AS NVARCHAR (100)) AS  p_quantity
,CAST( p_trackingnumber AS NVARCHAR (100)) AS  p_trackingnumber

        ,CAST( p_orderentry AS NVARCHAR (100)) AS  p_orderentry
,CASE WHEN s.PK = 8796093251624 THEN CAST('UPS Ground(HD)' AS NVARCHAR(100))
	  WHEN s.PK = 8796093284392 THEN CAST('Canada Post Ground' AS NVARCHAR(100))
	  WHEN s.PK = 8796093317160 THEN CAST('Pickup' AS NVARCHAR(100))

ELSE CAST( LTRIM(RTRIM(s.Code)) AS NVARCHAR (100)) END AS p_shippingmethod


       , CAST(p_code AS NVARCHAR(100)) AS p_code ,
        CASE WHEN p_status = 8796102000731 THEN CAST('3' AS NVARCHAR(100))
             ELSE NULL 
        END AS p_status ,
        CAST(a.p_carrier AS NVARCHAR(100)) AS p_carrier ,
      CAST(a.p_shippingaddress AS NVARCHAR(100)) AS p_shippingaddress ,
        CAST(p_order AS NVARCHAR(100)) AS p_order ,
        CASE WHEN s.PK = 8796093251624 
             THEN CAST('UPS Ground(HD)' AS NVARCHAR(100))
             WHEN s.PK = 8796093284392
             THEN CAST('Canada Post Ground' AS NVARCHAR(100))
             WHEN s.PK = 8796093317160 THEN CAST('Pickup' AS NVARCHAR(100))
             ELSE CAST(s.code AS NVARCHAR(100))
        END AS p_deliverymode
INTO    #Hybris_SPItem
FROM    Hybris.dbo.consignments a JOIN Hybris.dbo.consignmententries b ON a.PK =b.p_consignment
        JOIN Hybris.dbo.deliverymodes s ON a.p_deliverymode = s.PK
		LEFT JOIN Hybris.dbo.Addresses ad ON a.p_shippingaddress = ad.PK
WHERE EXISTS (SELECT 1 FROM #LoadedConsignment lc WHERE lc.PK =b.PK)
CREATE CLUSTERED INDEX MIX_HYSPItem ON #Hybris_SPItem (p_order);



SELECT  *
FROM    #RFO_SPItem
EXCEPT
SELECT  *
FROM    #Hybris_SPItem


END 