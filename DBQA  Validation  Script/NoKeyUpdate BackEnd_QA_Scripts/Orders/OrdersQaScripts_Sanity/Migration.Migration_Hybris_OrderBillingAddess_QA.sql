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
							



-----------------------------------------------------------------------------------------------------------------------
--OrderBillingAddress counts Hybris & RFO
------------------------------------------------------------------------------------------------------------------------
        SELECT  @RFOCount = COUNT(*)
        FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
                INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
				INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
                INNER JOIN RFOperations.Hybris.OrderBillingAddress oi ON oi.OrderId = o.OrderID
                INNER JOIN Hybris..orders ho ON ho.code = o.OrderNumber
				INNER JOIN Hybris..PaymentInfos HPI ON hO.PK = HPI.OwnerPKString 
                LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
        WHERE   o.CountryID = @RFOCountry
                AND a.autoshipid IS NULL
                AND od.startdate >= @ServerMod
				AND oi.ServerModifiedDate> @LastRun

		       

        SELECT  @HybrisCount = COUNT(DISTINCT a.PK)
        FROM    Hybris.dbo.orders o ( NOLOCK )
                INNER JOIN Hybris.dbo.PaymentInfos pio ON pio.OwnerPkString = o.PK
                INNER JOIN Hybris..addresses a ON pio.pk = a.OwnerPkString
        WHERE   ( p_template = 0
                  OR p_template IS NULL
                )
                AND o.TypePkString <> @ReturnOrderType
                AND currencypk = @HybCountry
                AND a.p_billingaddress = 1 
				AND a.modifiedTS> @LastRun
        

        SELECT  'OrderBillingAddress' ,
                @RFOCount AS RFO_Count ,
                @HybrisCount AS Hybris_Count ,
                @RFOCount - @HybrisCount AS Diff

-------------------------------------------------------------
-- MISSING KEYS
-------------------------------------------------------------

        IF OBJECT_ID('DataMigration.Migration.MissingBillAddress') IS NOT NULL
            DROP TABLE DataMigration.Migration.MissingBillAddress;

        SELECT  OrderBillingAddressID AS RFO_OrderBillingAddressID ,
                c.p_rfaddressid AS Hybris_OrderBillingAddressID ,
                CASE WHEN c.p_rfaddressid IS NULL THEN 'Destination'
                     WHEN a.OrderBillingAddressID IS NULL THEN 'Source'
                END AS MissingFROM
        INTO    DataMigration.Migration.MissingBillAddress
        FROM    ( SELECT    OrderBillingAddressID ,
                            o.OrderID
                  FROM      RFOperations.Hybris.Orders o WITH ( NOLOCK )
                            INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
							INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                            INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
                            INNER JOIN RFOperations.Hybris.OrderBillingAddress oi ON oi.OrderId = o.OrderID
                            INNER JOIN Hybris..orders ho ON ho.code = o.OrderNumber
							INNER JOIN Hybris..PaymentInfos HPI ON hO.PK = HPI.OwnerPKString 
                            LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
                  WHERE     o.CountryID = @RFOCountry
                            AND a.autoshipid IS NULL
                            AND od.startdate >= @ServerMod
							AND oi.ServerModifiedDate> @LastRun
                ) a
                FULL OUTER JOIN ( SELECT    a.p_rfaddressid
                                  FROM      Hybris.dbo.orders o ( NOLOCK )
                                            INNER JOIN Hybris.dbo.PaymentInfos pio ON pio.OwnerPkString = o.PK
                                            INNER JOIN Hybris..addresses a ON pio.pk = a.OwnerPkString
                                  WHERE     ( p_template = 0
                                              OR p_template IS NULL
                                            )
                                            AND o.TypePkString <> @ReturnOrderType
                                            AND currencypk = @HybCountry
                                            AND a.p_billingaddress = 1
											AND a.modifiedTS>@LastRun
                                ) c ON a.OrderBillingAddressID = c.p_rfaddressid
        WHERE   a.OrderBillingAddressID IS NULL
                OR c.p_rfaddressid IS NULL; 




        SELECT  @RowCount = COUNT(*)
        FROM    DataMigration.Migration.MissingBillAddress;

        IF ( @RowCount > 0 )
            BEGIN 

                SELECT  ' Total Missing ' + @Country + ' BillingAddresses' ,
                        @RowCount;

                SELECT  MissingFROM ,
                        COUNT(*)
                FROM    DataMigration.Migration.MissingBillAddress
                GROUP BY MissingFROM;

        
            END;  
