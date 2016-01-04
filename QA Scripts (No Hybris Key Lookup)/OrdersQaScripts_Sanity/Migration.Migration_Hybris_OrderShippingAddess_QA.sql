DECLARE @LastRun DATETIME = '2014-06-01';
DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-06-01' ,
    @RFOCount BIGINT ,
    @RowCount BIGINT ,
    @HybrisCount BIGINT 
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
								  
SELECT  @RFOCount = COUNT(*)
FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR) and U.P_SOURCENAME='Hybris-DM'
        INNER JOIN RFOperations.Hybris.OrderShippingAddress oi ON oi.OrderId = o.OrderID
        --INNER JOIN Hybris..orders ho ON ho.code = o.OrderNumber
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
		AND oi.ServerModifiedDate> @LastRun
		       

SELECT  @HybrisCount = COUNT(DISTINCT a.PK)
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..addresses a ON o.pk = a.OwnerPkString AND a.modifiedTS> @LAstRun AND O.userpk in (select pk from hybris..users where P_SOURCENAME='Hybris-DM')
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
        AND p_shippingaddress = 1 
        

SELECT  'OrderShippingaddress' ,
        @RFOCount AS RFO_Count ,
        @HybrisCount AS Hybris_Count ,
        @RFOCount - @HybrisCount AS Diff

-------------------------------------------------------------
-- MISSING KEYS
-------------------------------------------------------------

IF OBJECT_ID('DataMigration.Migration.MissingSHipAddress') IS NOT NULL
    DROP TABLE DataMigration.Migration.MissingSHipAddress;

SELECT  OrderShippingAddressID AS RFO_OrderShippingAddressID ,
        p_rfaddressid AS Hybris_OrderShippingAddressID ,
        CASE WHEN c.p_rfaddressid IS NULL THEN 'Destination'
             WHEN a.OrderShippingAddressID IS NULL THEN 'Source'
        END AS MissingFROM
INTO    DataMigration.Migration.MissingSHipAddress
FROM    ( SELECT    OrderShippingAddressID ,
                    O.OrderID
          FROM      RFOperations.Hybris.Orders o WITH ( NOLOCK )
                    INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
					INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                    INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid= cast(o.AccountID as nvarchar) U.P_SOURCENAME='Hybris-DM'
                    INNER JOIN RFOperations.Hybris.OrderShippingAddress oi ON oi.OrderId = o.OrderID
                    LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
          WHERE     o.CountryID = @RFOCountry
                    AND a.autoshipid IS NULL
                    AND od.startdate >= @ServerMod
					AND oi.ServerModifiedDate> @LastRun
        ) a
        FULL OUTER JOIN ( SELECT    a.p_rfaddressid 
                          FROM      Hybris.dbo.orders o ( NOLOCK )
                                    INNER JOIN Hybris..addresses a ON o.pk = a.OwnerPkString AND a.modifiedTS> @LastRun AND O.userpk in (select pk from hybris..users where P_SOURCENAME='Hybris-DM')
                          WHERE     ( p_template = 0
                                      OR p_template IS NULL
                                    )
                                    AND o.TypePkString <> @ReturnOrderType
                                    AND currencypk = @HybCountry
                                    AND p_shippingaddress = 1
                        ) c ON a.OrderShippingAddressID = c.p_rfaddressid
WHERE   a.OrderShippingAddressID IS NULL
        OR c.p_rfaddressid IS NULL; 

				
SELECT  @RowCount = COUNT(*)
FROM    DataMigration.Migration.MissingSHipAddress;

IF ( @RowCount > 0 )
    BEGIN 

        SELECT  ' Total Missing ' + @Country + ' ShipmentAddresses' ,
                @RowCount;

        SELECT  MissingFROM ,
                COUNT(*)
        FROM    DataMigration.Migration.MissingSHipAddress
        GROUP BY MissingFROM;

        
    END;  

	