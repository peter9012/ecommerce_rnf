DECLARE @LastRun DATETIME= '2014-06-01';
AS 

BEGIN 

DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-06-01' ,
    @RFOCount BIGINT ,
    @RowCount BIGINT , @HybrisPtCount BIGINT,
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
		
-------------------------------------------------------------
-- COUNTS
-------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------
--OrderPaymentTransaction counts Hybris & RFO
------------------------------------------------------------------------------------------------------------------------
SELECT  @RFOCount = COUNT(Opt.OrderPaymentTransactionID)
FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o. OrderID
		INNER JOIN RFOperations.Hybris.OrderPaymentTransaction opt ON opt.OrderPaymentID = oi.OrderPaymentID
        INNER JOIN Hybris..orders ho ON ho.code = o.OrderNumber
        LEFT JOIN RFOperations.Hybris.Autoship a  WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
		AND opt.ServerModifiedDate > @LastRun

		       

SELECT  @HybrisPtCount = COUNT(*)
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..PaymentTransactions pt ON o.pk = pt.p_order
		--INNER JOIN Hybris..paymnttrnsctentries pte ON pte.p_paymenttransaction = pt.PK
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
      
SELECT  @HybrisCount = COUNT(*)
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..PaymentTransactions pt ON o.pk = pt.p_order
		INNER JOIN Hybris..paymnttrnsctentries pte ON pte.p_paymenttransaction = pt.PK AND pte.modifiedTS>@LastRun
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
      

SELECT  'OrderPaymentTransaction' ,
        @RFOCount AS RFO_Count ,
		--@HybrisPtCount AS Hybris_PtCount,
        @HybrisCount AS Hybris_Count ,
        @RFOCount - @HybrisCount AS Diff

-------------------------------------------------------------
-- MISSING KEYS
-------------------------------------------------------------

IF OBJECT_ID('DataMigration.Migration.MissingTransaction') IS NOT NULL
    DROP TABLE DataMigration.Migration.MissingTransaction;
-----------------------------------------------------------------------------------



SELECT  OrderPaymentTransactionID AS RFO_PaymentTransactionID,
        PK AS Hybris_PaymentTransactionID,
	 CASE WHEN c.PK IS NULL THEN 'Destination'
             WHEN a.OrderPaymentTransactionID IS NULL THEN 'Source'
        END AS MissingFROM
INTO    DataMigration.Migration.MissingTransaction
FROM    ( SELECT   opt.OrderPaymentTransactionID
          FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o.OrderID
		INNER JOIN RFOperations.Hybris.OrderPaymentTransaction opt ON opt.OrderPaymentID = oi.OrderPaymentID
        INNER JOIN Hybris..orders ho ON ho.code = o.OrderNumber
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
		AND opt.ServerModifiedDate > @LastRun
        ) a

		 FULL OUTER JOIN 

       ( SELECT    pte.PK
         FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..PaymentTransactions pt ON o.pk = pt.p_order
		INNER JOIN Hybris..paymnttrnsctentries pte ON pte.p_paymenttransaction = pt.PK AND pte.modifiedTS>@LastRun
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry

                     ) c ON   a.OrderPaymentTransactionID = c.PK --a.OrderPaymentID = c.p_paymenttransaction AND
WHERE   a.OrderPaymentTransactionID IS NULL
        OR c.PK IS NULL; 

		--SELECT * FROM Hybris.dbo.paymnttrnsctentries
		--WHERE PK IN (SELECT RFO_PaymentTransactionID FROM datamigration.migration.missingtransaction WHERE MissingFrom = 'Destination')

SELECT  @RowCount = COUNT(*)
FROM    DataMigration.Migration.MissingTransaction;

IF ( @RowCount > 0 )
    BEGIN 

        SELECT  ' Total Missing ' + @Country + ' Payment Transactions' ,
                @RowCount;

        SELECT  MissingFROM ,
                COUNT(*)
        FROM    DataMigration.Migration.MissingTransaction
        GROUP BY MissingFROM;

        --SELECT  *
        --FROM    DataMigration.Migration.MissingTransaction
        --ORDER BY MissingFROM;

    END;  


