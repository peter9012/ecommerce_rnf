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
		
        INNER JOIN RFOperations.ETL.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderNumber
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
		
					INNER JOIN RFOperations.ETL.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderNumber
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
		
