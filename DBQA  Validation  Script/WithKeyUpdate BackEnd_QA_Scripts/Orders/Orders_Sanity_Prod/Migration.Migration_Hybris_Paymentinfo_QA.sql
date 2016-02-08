DECLARE @LastRun DATETIME '2014-06-01' ;
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
--OrderPayment counts Hybris & RFO
------------------------------------------------------------------------------------------------------------------------
SELECT  @RFOCount = COUNT(*)
FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.orderNumber
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = cast(o.AccountID as nvarchar)
        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o.OrderID
        INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
		INNER JOIN Hybris..PaymentInfos HPI ON HO.PK = HPI.OwnerPKString AND HPI.pk = Oi.OrderPaymentID 
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod

		       

SELECT  @HybrisCount = COUNT(DISTINCT a.PK)
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..paymentInfos a ON o.pk = a.OwnerPkString
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
      
        

SELECT  'OrderPayment' ,
        @RFOCount AS RFO_Count ,
        @HybrisCount AS Hybris_Count ,
        @RFOCount - @HybrisCount AS Diff

-------------------------------------------------------------
-- MISSING KEYS
-------------------------------------------------------------

IF OBJECT_ID('DataMigration.Migration.MissingPayment') IS NOT NULL
    DROP TABLE DataMigration.Migration.MissingPayment;

SELECT  orderPaymentID AS RFO_OrderPaymentID ,
        PK AS Hybris_OrderPaymentID ,
        CASE WHEN c.PK IS NULL THEN 'Destination'
             WHEN a.orderPaymentID IS NULL THEN 'Source'
        END AS MissingFROM
INTO     DataMigration.Migration.MissingPayment
FROM    ( SELECT    OrderPaymentID 
               
          FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid =  o.orderNumber
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o.OrderID
        INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
		INNER JOIN Hybris..PaymentInfos HPI ON HO.PK = HPI.OwnerPKString --AND HPI.Code = CAST (Oi.OrderPaymentID AS NVARCHAR)
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod

        ) a
        FULL OUTER JOIN ( SELECT    a.PK
                          FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..paymentInfos a ON o.pk = a.OwnerPkString
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry


                        ) c ON CAST(a.orderPaymentID  AS NVARCHAR) = c.PK  
WHERE   a.orderPaymentID IS NULL
        OR c.PK IS NULL; 




SELECT  @RowCount = COUNT(*)
FROM     DataMigration.Migration.MissingPayment;

IF ( @RowCount > 0 )
    BEGIN 

        SELECT  ' Total Missing ' + @Country + ' Payments' ,
                @RowCount;

        SELECT  MissingFROM ,
                COUNT(*)
        FROM    DataMigration.Migration.MissingPayment
        GROUP BY MissingFROM;

            END;  

