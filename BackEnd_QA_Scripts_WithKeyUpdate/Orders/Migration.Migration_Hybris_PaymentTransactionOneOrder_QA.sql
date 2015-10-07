USE RFOperations 
GO 

CREATE PROCEDURE Migration.Migration_Hybris_PaymentTransactionOneOrder_QA @OrderID BIGINT 
AS 

BEGIN 
DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-05-01' ,
    @RFOCount BIGINT , @OrderID BIGINT  ,
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
-------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------


IF OBJECT_ID('TEMPDB.dbo.#Pay') IS NOT NULL DROP TABLE #Pay
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Pay') IS NOT NULL DROP TABLE #Hybris_Pay
IF OBJECT_ID('TEMPDB.dbo.#RFO_Pay') IS NOT NULL DROP TABLE #RFO_Pay


IF OBJECT_ID('TEMPDB.dbo.#LoadedTransaction') IS NOT NULL DROP TABLE #LoadedTransaction

SELECT pte.PK INTO #LoadedTransaction
FROM    Hybris.dbo.orders o ( NOLOCK )
    INNER JOIN Hybris..PaymentTransactions pt ON o.pk = pt.p_order
	INNER JOIN Hybris..paymnttrnsctentries pte ON pte.p_paymenttransaction = pt.PK 
WHERE   ( p_template = 0
        OR p_template IS NULL
    )
    AND o.TypePkString <> @ReturnOrderType
    AND currencypk = @HybCountry
	AND o.PK = @OrderID

------------------------------------------------------------------------------------------------------------------------

SELECT
CAST( OrderID AS NVARCHAR (100)) AS  OrderID
,CAST( '8796125855777' AS NVARCHAR (100)) AS  Currency
--,CAST(b.TransactionID AS NVARCHAR (100)) AS  TransactionID
,CAST( PaymentProvider AS NVARCHAR (100)) AS  PaymentProvider
,CAST( a.AmountTobeAuthorized AS NVARCHAR (100)) AS  Amount
,CAST( OrderPaymentTransactionID AS NVARCHAR (100)) AS  OrderPaymentTransactionID
,CAST( AmountAuthorized AS NVARCHAR (100)) AS  AmountAuthorized
--,CAST( TransactionID AS NVARCHAR (100)) AS  TransactionID
,CAST( ProcessDate AS NVARCHAR (100)) AS  ProcessDate
,CAST( AvsResult AS NVARCHAR (100)) AS  AvsResult

--------------------------------------------------------------------
--,CAST( 'USD' AS NVARCHAR (100)) AS Currency 
, CASE WHEN AuthorizeType = 'SALE' THEN 'CAPTURE' 
 WHEN AuthorizeType = '' THEN 'CANCEL' ELSE  CAST(AuthorizeType AS NVARCHAR (100)) END AS PaymentType
 ,  CAST (a.OrderPaymentID AS NVARCHAR (100)) AS OrderPaymentID

INTO #RFO_Pay
FROM RFoperations.Hybris.OrderPayment a JOIN 
RFOperations.Hybris.OrderPaymentTransaction b ON a.OrderPaymentID =b.OrderPaymentID 
WHERE EXISTS (SELECT 1 FROM #LoadedTransaction lt WHERE lt.PK =b.OrderPaymentTransactionID)

CREATE CLUSTERED INDEX MIX_RFPay ON #RFO_Pay (OrderID)


SELECT 
CAST( p_order AS NVARCHAR (100)) AS  p_order
,CAST( pt.p_currency AS NVARCHAR (100)) AS  p_currency
--,CAST( p_requestid AS NVARCHAR (100)) AS  p_requestid
,CAST( p_paymentprovider AS NVARCHAR (100)) AS  p_paymentprovider
,CAST( cast ( p_plannedamount AS DECIMAL (8,2)) AS NVARCHAR (100)) AS  p_plannedamount
,CAST (pte.PK AS NVARCHAR (100)) AS PK 
,CAST( cast ( p_amount AS DECIMAL (8,2)) AS NVARCHAR (100)) AS  p_amount
--,CAST( p_subscriptionid AS NVARCHAR (100)) AS  p_subscriptionid
,CAST( p_time AS NVARCHAR (100)) AS  p_time
,CAST( ISNULL(p_avsresult,'') AS NVARCHAR (100)) AS  p_avsresult
----------------------------------------------------------------------------
--,CAST( c.Code AS NVARCHAR (100)) AS  p_currency
,CAST( ev.Code AS NVARCHAR (100)) AS  p_type
, CAST( pf.Code AS NVARCHAR (100)) AS P_info


INTO #Hybris_Pay
FROM Hybris.dbo.PaymentTransactions pt 
JOIN Hybris.dbo.paymnttrnsctentries pte ON pte.p_paymenttransaction = pt.PK
JOIN Hybris.dbo.paymentinfos pf ON pf.Code = pt.p_info
JOIN Hybris.dbo.enumerationvalues ev ON ev.PK =pte.p_type
WHERE EXISTS (SELECT 1 FROM #LoadedTransaction lt WHERE lt.PK = pte.PK)



SELECT * FROM #RFO_Pay
EXCEPT
SELECT * FROM #Hybris_Pay

END 
