-----------------------------------------------------------------------------------
-- Consignment + Consignment Entries Framework 
----------------------------------------------------------------------------------
USE DataMigration; 

GO

CREATE PROCEDURE Migration.Migration_Hybris_Consignments_QA 
AS 

BEGIN 
SET NOCOUNT ON

SET ANSI_WARNINGS OFF 
---------------------------------------------------------------------------------------------
DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-06-01' ,
    @RFOCount BIGINT ,
    @RowCount BIGINT ,
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
						
-----------------------------------------------------------------------------------------------------------------------
--Consignment and Consignment Entries counts Hybris & RFO
------------------------------------------------------------------------------------------------------------------------
SELECT  @RFOCount = COUNT(DISTINCT oi.OrderShipmentPackageItemID)
FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
		INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		
        INNER JOIN RFOperations.ETL.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
        INNER JOIN RFOperations.Hybris.OrderItem (NOLOCK) ri ON ri.OrderId = o.OrderID
        INNER JOIN Hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderShipmentPackageItem oi ON oi.OrderID = o.OrderID
                                                              AND ri.OrderItemID = oi.OrderItemId --o.OrderID
        INNER JOIN Hybris..orders ho ON ho.PK = o.OrderID
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.OrderNumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.AutoshipID IS NULL
        AND od.Startdate >= @ServerMod;

		       

      
SELECT  @HybrisCount = COUNT(*)
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..consignments pt ON o.PK = pt.p_order
        INNER JOIN Hybris..consignmententries pte ON pte.p_consignment = pt.PK
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry;
      

SELECT  'OrderShipmentPackageItems' ,
        @RFOCount AS RFO_Count ,
        @HybrisCount AS Hybris_Count ,
        @RFOCount - @HybrisCount AS Diff;



-------------------------------------------------------------
-- MISSING KEYS
-------------------------------------------------------------

IF OBJECT_ID('DataMigration.Migration.MissingConsignment') IS NOT NULL
    DROP TABLE DataMigration.Migration.MissingConsignment;
-----------------------------------------------------------------------------------



SELECT  OrderShipmentPackageItemID AS RFO_ShipmentID ,
        PK AS Hybris_ShipmentID ,
        CASE WHEN c.PK IS NULL THEN 'Destination'
             WHEN a.OrderShipmentPackageItemID IS NULL THEN 'Source'
        END AS MissingFROM
INTO    DataMigration.Migration.MissingConsignment
FROM    ( SELECT    oi.OrderShipmentPackageItemID
          FROM      RFOperations.Hybris.Orders o WITH ( NOLOCK )
                    INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		
					INNER JOIN RFOperations.ETL.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                    INNER JOIN RFOperations.Hybris.OrderItem (NOLOCK) ri ON ri.OrderId = o.OrderID
                    INNER JOIN Hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR)
                    INNER JOIN RFOperations.Hybris.OrderShipmentPackageItem oi ON oi.OrderID = o.OrderID--CAST(o.OrderNumber AS INT)
                                                              AND ri.OrderItemID = oi.OrderItemId --o.OrderID
                    INNER JOIN Hybris..orders ho ON ho.PK = o.OrderID
                    LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.OrderNumber AS INT)
          WHERE     o.CountryID = @RFOCountry
                    AND a.AutoShipID IS NULL
                    AND od.Startdate >= @ServerMod
        ) a
        FULL OUTER JOIN ( SELECT    pte.PK, p_orderentry, p_order 
                          FROM      Hybris.dbo.orders o ( NOLOCK )
                                    INNER JOIN Hybris..consignments pt ON o.PK = pt.p_order
                                    INNER JOIN Hybris..consignmententries pte ON pte.p_consignment = pt.PK
                          WHERE     ( p_template = 0
                                      OR p_template IS NULL
                                    )
                                    AND o.TypePkString <> @ReturnOrderType
                                    AND currencypk = @HybCountry
                        ) c ON a.OrderShipmentPackageItemID = c.PK --a.OrderPaymentID = c.p_paymenttransaction AND
WHERE   a.OrderShipmentPackageItemID IS NULL
        OR c.PK IS NULL; 



SELECT  @RowCount = COUNT(*)
FROM    DataMigration.Migration.MissingConsignment;

IF ( @RowCount > 0 )
    BEGIN 

        SELECT  ' Total Missing ' + @Country + ' Consignments' ,
                @RowCount;

        SELECT  MissingFROM ,
                COUNT(*)
        FROM    DataMigration.Migration.MissingConsignment
        GROUP BY MissingFROM;

        SELECT  *
        FROM    DataMigration.Migration.MissingConsignment
        ORDER BY MissingFROM;

    END;  

	

	------------------------------------------------------------------------------


IF OBJECT_ID('TEMPDB.dbo.#Ship') IS NOT NULL
    DROP TABLE #Ship;
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Ship') IS NOT NULL
    DROP TABLE #Hybris_Ship;
IF OBJECT_ID('TEMPDB.dbo.#RFO_Ship') IS NOT NULL
    DROP TABLE #RFO_Ship;


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
        AND currencypk = @HybCountry;


		--SELECT COUNT(*) FROM #Hybris_SPItem w2'
		

-----------------------------------------------------------------------------------------------------------------------
--Load Consignments 
--------------------------------------------------------------------------------------------------------------------------
IF OBJECT_ID('TEMPDB.dbo.#SPItem') IS NOT NULL
    DROP TABLE #SPItem;
IF OBJECT_ID('TEMPDB.dbo.#Hybris_SPItem') IS NOT NULL
    DROP TABLE #Hybris_SPItem;
IF OBJECT_ID('TEMPDB.dbo.#RFO_SPItem') IS NOT NULL
    DROP TABLE #RFO_SPItem;

SELECT  CAST(TrackingNumber AS NVARCHAR(100)) AS TrackingID ,
        CAST(ShipDate AS NVARCHAR(100)) AS ShipDate ,

		CAST (a.OrderItemId AS NVARCHAR (100)) AS OrderItem,
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
			 INNER JOIN RodanFieldsLive.dbo.Orders rfl ON b.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		
	        JOIN RFOperations.Hybris.OrderShipment d ON d.OrderID = b.OrderID AND d.OrderShipmentID =a.OrderShipmentID      
			JOIN RFOperations.Hybris.OrderShippingAddress e ON e.OrderID = b.OrderID 
			JOIN Hybris.dbo.Addresses ad ON ad.PK =e.OrdershippingaddressID AND ad.p_rfAddressID = d.OrderShipmentID
		LEFT JOIN RFOperations.RFO_Reference.ShippingMethod c ON d.ShippingMethodID = c.ShippingMethodID

WHERE   EXISTS (SELECT 1 FROM #LoadedConsignment lc WHERE lc.PK =a.OrderShipmentPackageItemID)

CREATE CLUSTERED INDEX MIX_RFSPItem ON #RFO_SPItem (OrderID);

--SELECT * FROM  RFOperations.RFO_Reference.ShippingMethod

SELECT  CAST(p_trackingid AS NVARCHAR(100)) AS p_trackingid ,
        CAST(p_shippingdate AS NVARCHAR(100)) AS p_shippingdate ,
				CAST (p_orderentry AS NVARCHAR (100)) AS OrderEntry,
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
        CAST(p_shippingaddress AS NVARCHAR(100)) AS p_shippingaddress ,
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
WHERE EXISTS (SELECT 1 FROM #LoadedConsignment lc WHERE lc.PK =b.PK)
CREATE CLUSTERED INDEX MIX_HYSPItem ON #Hybris_SPItem (p_order);


SELECT  *
INTO    #SPItem
FROM    #RFO_SPItem
EXCEPT
SELECT  *
FROM    #Hybris_SPItem;

SELECT TOP 2 * FROM #SPItem

SELECT COUNT (*) FROM #SpItem 


CREATE CLUSTERED INDEX MIX_Item ON #SPItem (OrderID);

TRUNCATE TABLE DataMigration.Migration.ErrorLog_Orders


DECLARE @I INT =  (SELECT  MIN (MapID)     FROM     DataMigration.Migration.Metadata_Orders
			       WHERE HybrisObject = 'Consignments' ),
		  @C INT = ( SELECT   MAX(MapID)
              FROM     DataMigration.Migration.Metadata_Orders
			  WHERE HybrisObject = 'ConsignmentEntries'
            ); 

DECLARE @DesKey NVARCHAR(50); 

DECLARE @SrcKey NVARCHAR(50); 

DECLARE @Skip BIT; 

WHILE ( @I <= @C )
    BEGIN 

        SELECT  @Skip = ( SELECT    Skip
                          FROM      DataMigration.Migration.Metadata_Orders
                          WHERE     MapID = @I
                        );


        IF ( @Skip = 1 )
            SET @I = @I + 1;

        ELSE
            BEGIN 


    
	
	            DECLARE @DesTemp NVARCHAR(50)  = ( SELECT   CASE
                                                              WHEN HybrisObject = 'Orders'
                                                              THEN '#Hybris_Orders'
                                                              WHEN HybrisObject = 'OrderEntries'
                                                              THEN '#Hybris_Item'
                                                              WHEN HybrisObject = 'Consignments'
                                                              OR HybrisObject = 'ConsignmentEntries'
                                                              THEN '#Hybris_SPItem'
                                                              WHEN HybrisObject = 'PaymentInfos'
                                                              OR HybrisObject = 'PaymentTransaction'
                                                              THEN '#Hybris_Pay'
                                                              WHEN HybrisObject = 'paymnttrnsctentries'
                                                              THEN '#Hybris_Tran'
                                                              WHEN HybrisObject = 'Addresses_Billing'
                                                              THEN '#Hybris_BlAdr'
                                                              WHEN HybrisObject = 'Addresses'
                                                              THEN '#Hybris_ShAdr'
                                                              WHEN HybrisObject = 'OrderNotes'
                                                              THEN '#Hybris_Note'
                                                            END
                                                   FROM     DataMigration.Migration.Metadata_Orders
                                                   WHERE    MapID = @I
                                                 );  

                DECLARE @DesCol NVARCHAR(50) = ( SELECT Hybris_Column
                                                 FROM   DataMigration.Migration.Metadata_Orders
                                                 WHERE  MapID = @I
                                               );

                SET @SrcKey = ( SELECT  RFO_Key
                                FROM    DataMigration.Migration.Metadata_Orders
                                WHERE   MapID = @I
                              );

    
                SET @DesKey = ( SELECT  CASE WHEN HybrisObject = 'Consignments'
                                             THEN 'P_order'
										--	 WHEN HybrisObject = 'OrderEntries' THEN 'PK'
                                             ELSE 'PK'
                                        END
                                FROM    DataMigration.Migration.Metadata_Orders
                                WHERE   MapID = @I
                              ); 


                DECLARE @SQL1 NVARCHAR(MAX) = ( SELECT  sqlstmt
                                                FROM    DataMigration.Migration.Metadata_Orders
                                                WHERE   MapID = @I
                                              );
                DECLARE @SQL2 NVARCHAR(MAX) = ' 
 UPDATE A 
SET a.Hybris_Value = b. ' + @DesCol
                    + ' FROM DAtaMigration.Migration.ErrorLog_Orders a  JOIN '
                    + @DesTemp + ' b  ON a.RecordID= b.' + @DesKey
                    + ' WHERE a.MAPID = ' + CAST(@I AS NVARCHAR);



                DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
                    ' INSERT INTO DAtaMigration.Migration.ErrorLog_Orders (Identifier,MapID,RecordID,RFO_Value) '
                    + @SQL1 + @SQL2;

                BEGIN TRY
                    EXEC sp_executesql @SQL3, N'@ServerMod DATETIME',
                        @ServerMod = @LastRUN


                    SET @I = @I + 1;
                    SELECT  @I ,
                            GETDATE();

                END TRY

                BEGIN CATCH

                    SELECT  @SQL3;

                    SET @I = @I + 1;

                END CATCH;
            END; 

    END; 


	SELECT a.MapID, RFO_Column, COUNT(*)
	FROM DataMigration.Migration.Metadata_Orders a JOIN DataMigration.Migration.Errorlog_orders b ON a.MapID =b.MapID 
	GROUP BY a.MapID, RFO_Column

	END 









