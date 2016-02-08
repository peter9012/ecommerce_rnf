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
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderNumber
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o. OrderID
		INNER JOIN RFOperations.Hybris.OrderPaymentTransaction opt ON opt.OrderPaymentID = oi.OrderPaymentID
        INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
        LEFT JOIN RFOperations.Hybris.Autoship a  WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod

--Payment Count
SELECT  @RFOPCount = COUNT(Oi.OrderPaymentID)
FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderNumber
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o. OrderID
		INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
        LEFT JOIN RFOperations.Hybris.Autoship a  WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod


	--SELECT * FROM RFOPERATIONS.HYBRIS.ORDERPAYMENTTRANSACTION WHERE ORDERPAYMENTTRANSACTIONID IN (SELECT PK FROM HYBRIS.dbo.paymnttrnsctentries)

SELECT  @HybrisCount = COUNT(*)
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..PaymentTransactions pt ON o.pk = pt.p_order
		--INNER JOIN Hybris..paymnttrnsctentries pte ON pte.p_paymenttransaction = pt.PK
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
      
SELECT  @HybrisPtCount = COUNT(*)
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..PaymentTransactions pt ON o.pk = pt.p_order
		INNER JOIN Hybris..paymnttrnsctentries pte ON pte.p_paymenttransaction = pt.PK
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
      

SELECT  'OrderPaymentTransaction' ,
        @RFOCount AS RFO_Count ,
		--@HybrisPtCount AS Hybris_PtCount,
        @HybrisPtCount AS Hybris_Count ,
        @RFOCount - @HybrisPtCount AS Diff

SELECT  'OrderPayment' ,
        @RFOPCount AS RFO_Count ,
		--@HybrisPtCount AS Hybris_PtCount,
        @HybrisCount AS Hybris_Count ,
        @RFOPCount - @HybrisCount AS Diff

-------------------------------------------------------------
-- MISSING KEYS
-------------------------------------------------------------

IF OBJECT_ID('DataMigration.Migration.MissingTransaction') IS NOT NULL
    DROP TABLE DataMigration.Migration.MissingTransaction;

IF OBJECT_ID('DataMigration.Migration.MissingOrderPayment') IS NOT NULL
    DROP TABLE DataMigration.Migration.MissingOrderPayment;
-----------------------------------------------------------------------------------

--Order Payment
SELECT  OrderPaymentID AS RFO_PaymentID,
        PK AS Hybris_PaymentTransactionID,
	 CASE WHEN c.PK IS NULL THEN 'Destination'
             WHEN a.OrderPaymentID IS NULL THEN 'Source'
        END AS MissingFROM
INTO    DataMigration.Migration.MissingOrderPayment
FROM    ( SELECT   oi.OrderPaymentID
          FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderNumber
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o.OrderID
		        INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
        ) a

		 FULL OUTER JOIN 

       ( SELECT    pt.PK
         FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..PaymentTransactions pt ON o.pk = pt.p_order
		WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry

                     ) c ON   a.OrderPaymentID = c.PK --a.OrderPaymentID = c.p_paymenttransaction AND
WHERE   a.OrderPaymentID IS NULL
        OR c.PK IS NULL; 

SELECT  @RowCount = COUNT(*)
FROM    DataMigration.Migration.MissingOrderPayment;

IF ( @RowCount > 0 )
    BEGIN 

        SELECT  ' Total Missing ' + @Country + ' Payment Transactions' ,
                @RowCount;

        SELECT  MissingFROM ,
                COUNT(*)
        FROM    DataMigration.Migration.MissingOrderPayment
        GROUP BY MissingFROM;

end;

--Payment Transaction
SELECT  OrderPaymentTransactionID AS RFO_PaymentTransactionID,
        PK AS Hybris_PaymentTransactionID,
	 CASE WHEN c.PK IS NULL THEN 'Destination'
             WHEN a.OrderPaymentTransactionID IS NULL THEN 'Source'
        END AS MissingFROM
INTO    DataMigration.Migration.MissingTransaction
FROM    ( SELECT   opt.OrderPaymentTransactionID
          FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderNumber
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o.OrderID
		INNER JOIN RFOperations.Hybris.OrderPaymentTransaction opt ON opt.OrderPaymentID = oi.OrderPaymentID
        INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
        ) a

		 FULL OUTER JOIN 

       ( SELECT    pte.PK
         FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..PaymentTransactions pt ON o.pk = pt.p_order
		INNER JOIN Hybris..paymnttrnsctentries pte ON pte.p_paymenttransaction = pt.PK
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
		/*
		SELECT * FROM RFOPERATIONS.HYBRIS.ORDERPAYMENTTRANSACTION WHERE ORDERPAYMENTTRANSACTIONID IN (SELECT RFO_PAYMENTTRANSACTIONID FROM DataMigration.Migration.MissingTransaction WHERE MISSINGFROM='Destination')
		ORDERPAYMENTID IN (SELECT PK FROM HYBRIS.DBO.PAYMENTTRANSACTIONS)

		 */
SELECT * FROM DataMigration.Migration.MissingTransaction

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