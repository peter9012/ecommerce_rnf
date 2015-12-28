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


-----------------------------------------------------------------------------------------------------------------------
--Orderitem counts HYbris & RFO

        SELECT  @RFOCount = COUNT(DISTINCT oi.OrderItemID)
        FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
                INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
				INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderNumber
                INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR)
                INNER JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
                INNER JOIN hybris..products p ON p.p_rflegacyproductid = oi.ProductID
                INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
                LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
        WHERE   o.CountryID = @RFOCountry
                AND a.autoshipid IS NULL
                AND od.startdate >= @ServerMod
                AND p.p_catalog = 8796093088344
                AND p.p_catalogversion = 8796093153881

       

        SELECT  @HybrisCount = COUNT(DISTINCT oe.PK)
        FROM    Hybris.dbo.orders o ( NOLOCK )
                INNER JOIN Hybris..orderentries oe ON oe.orderpk = o.pk
        WHERE   ( p_template = 0
                  OR p_template IS NULL
                )
                AND o.TypePkString <> @ReturnOrderType
                AND currencypk = @HybCountry 
        

        SELECT  'OrderItems' ,
                @RFOCount AS RFO_Count ,
                @HybrisCount AS Hybris_Count ,
                @RFOCount - @HybrisCount AS Diff
---------------------------------------------------------------------------------------------------------------------------------

-- Missing 
 -----------------------------------------------------------------------------------------------------------------------------


        IF OBJECT_ID('DataMigration.Migration.OrderItemsMissing') IS NOT NULL
            DROP TABLE DataMigration.Migration.OrderItemsMissing;



        SELECT  OrderItemID AS RFO_OrderItemID ,
                PK AS Hybris_OrderItemID ,
                CASE WHEN b.PK IS NULL THEN 'Destination'
                     WHEN a.OrderItemID IS NULL THEN 'Source'
                END AS MissingFROM
        INTO    DataMigration.Migration.OrderItemsMissing
        FROM    ( SELECT    OrderItemID
                  FROM      RFOperations.Hybris.Orders o WITH ( NOLOCK )
                            INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
							INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderNumber
                            INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR)
                            INNER JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
                            INNER JOIN hybris..products p ON p.p_rflegacyproductid = oi.ProductID
                            INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
                            LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
                  WHERE     o.CountryID = @RFOCountry
                            AND a.autoshipid IS NULL
                            AND od.startdate >= @ServerMod
                            AND p.p_catalog = 8796093088344
                            AND p.p_catalogversion = 8796093153881
                ) a
                FULL OUTER JOIN ( SELECT    oe.PK
                                  FROM      Hybris.dbo.orders o ( NOLOCK )
                                            INNER JOIN Hybris..orderentries oe ON oe.orderpk = o.pk
                                  WHERE     ( p_template = 0
                                              OR p_template IS NULL
                                            )
                                            AND o.TypePkString <> @ReturnOrderType
                                            AND currencypk = @HybCountry
                                ) b ON a.OrderItemID = b.PK
        WHERE   a.OrderItemID IS NULL
                OR b.PK IS NULL; 


        SELECT  @RowCount = COUNT(*)
        FROM    DataMigration.Migration.OrderItemsMissing;

        IF ( @RowCount > 0 )
            BEGIN 

                SELECT  ' Total Missing ' + @Country + ' OrderItems' ,
                        @RowCount;

                SELECT  'MissingItems' ,
                        MissingFROM ,
                        COUNT(*)
                FROM    DataMigration.Migration.OrderItemsMissing
                GROUP BY MissingFROM;

                            END;  
