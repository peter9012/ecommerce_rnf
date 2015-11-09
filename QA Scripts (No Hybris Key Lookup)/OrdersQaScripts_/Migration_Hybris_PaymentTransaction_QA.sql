-----------------------------------------------------------------------------------
-- Payment Transaction + Payment Transaction Entries Framework 
----------------------------------------------------------------------------------
USE RFOperations 
---------------------------------------------------------------------------------------------
GO 

CREATE PROCEDURE Migration.Migration_Hybris_PaymentTransaction_QA  @LastRun '2014-05-01'
AS 

BEGIN 

DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-05-01' ,
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



----------------------------------------------------
-- DUPLICATES 
-----------------------------------------------------

--IF OBJECT_ID('TEMPDB.dbo.#Tran_Dups') IS NOT NULL
--    DROP TABLE #Tran_Dups;
-----------------------------------------------------

--SELECT  OrderPaymentTransactionID ,
--        COUNT(pte.PK) AS Hybris_Duplicates
--INTO    #Tran_Dups
--FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
--        INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
--        INNER JOIN hybris..users u WITH ( NOLOCK ) ON CAST (u.p_rfaccountid AS BIGINT) = o.AccountID
--        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o.OrderID
--		INNER JOIN RFOperations.Hybris.OrderPaymentTransaction opt ON opt.OrderPaymentID = oi.OrderPaymentID
--        INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
--		INNER JOIN Hybris.dbo.paymnttrnsctentries pte ON pte.PK =opt.OrderPaymentTransactionID
--        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
--WHERE   o.CountryID = @RFOCountry
--        AND a.autoshipid IS NULL
--        AND od.startdate >= @ServerMod
--GROUP BY OrderPaymentTransactionID

--SELECT  @RowCount = COUNT(*)
--FROM    #Tran_Dups;



--IF @RowCount > 0
--    BEGIN 

--        SELECT  'Duplicate ' + @Country + ' Payment Transactions in Hybris' ,
--                @RowCount;

--        SELECT  * 
--        FROM    #Tran_Dups;


--    END;

--------------------------------------------------------------------------------


IF OBJECT_ID('TEMPDB.dbo.#tran') IS NOT NULL DROP TABLE #tran
IF OBJECT_ID('TEMPDB.dbo.#Hybris_tran') IS NOT NULL DROP TABLE #Hybris_tran
IF OBJECT_ID('TEMPDB.dbo.#RFO_tran') IS NOT NULL DROP TABLE #RFO_tran


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

--------------------------------------------------------------------------------------------------------------------------

SELECT
CAST( OrderID AS NVARCHAR (100)) AS  OrderID
,CAST( '8796125855777' AS NVARCHAR (100)) AS  Currency
,CAST(b.TransactionID AS NVARCHAR (100)) AS  TransactionID
,CAST( PaymentProvider AS NVARCHAR (100)) AS  PaymentProvider
,CAST( a.AmountTobeAuthorized AS NVARCHAR (100)) AS  Amount
,CAST( OrderPaymentTransactionID AS NVARCHAR (100)) AS  OrderPaymentTransactionID
,CAST( AmountAuthorized AS NVARCHAR (100)) AS  AmountAuthorized
,CONCAT (SUBSTRING (a.DisplayNumber, 1, 6),SUBSTRING (a.DisplayNumber, (LEN(a.DisplayNumber) -3), 4), ExpMonth,ExpYear) AS SubscriptionID
,CAST( ProcessDate AS NVARCHAR (100)) AS  ProcessDate
,CAST( AvsResult AS NVARCHAR (100)) AS  AvsResult
--------------------------------------------------------------------
--,CAST( 'USD' AS NVARCHAR (100)) AS Currency 
, CASE WHEN AuthorizeType = 'SALE' THEN 'CAPTURE' 
 WHEN AuthorizeType = '' THEN 'CANCEL' ELSE  CAST(AuthorizeType AS NVARCHAR (100)) END AS PaymentType

INTO #RFO_tran
FROM RFoperations.Hybris.OrderPayment a JOIN 
RFOperations.Hybris.OrderPaymentTransaction b ON a.OrderPaymentID =b.OrderPaymentID 
WHERE EXISTS (SELECT 1 FROM #LoadedTransaction lt WHERE lt.PK =b.OrderPaymentTransactionID) 
AND b.ServerModifiedDate > @LastRun

CREATE CLUSTERED INDEX MIX_RFtran ON #RFO_tran (OrderID)


SELECT 
CAST( p_order AS NVARCHAR (100)) AS  p_order
,CAST( pt.p_currency AS NVARCHAR (100)) AS  p_currency
,CAST( PTE.p_requestid AS NVARCHAR (100)) AS  p_requestid
,CAST( p_paymentprovider AS NVARCHAR (100)) AS  p_paymentprovider
,CAST( cast ( p_plannedamount AS DECIMAL (8,2)) AS NVARCHAR (100)) AS  p_plannedamount
,CAST (pte.PK AS NVARCHAR (100)) AS PK 
,CAST( cast ( p_amount AS DECIMAL (8,2)) AS NVARCHAR (100)) AS  p_amount
,CAST( Pte.p_subscriptionid AS NVARCHAR (100)) AS  p_subscriptionid
,CAST( p_time AS NVARCHAR (100)) AS  p_time
,CAST( ISNULL(p_avsresult,'') AS NVARCHAR (100)) AS  p_avsresult
----------------------------------------------------------------------------
--,CAST( c.Code AS NVARCHAR (100)) AS  p_currency
,CAST( ev.Code AS NVARCHAR (100)) AS  p_type
--, CAST( pf.code AS NVARCHAR (100)) AS  payinfo

--INTO #Hybris_tran
FROM Hybris.dbo.PaymentTransactions pt 
JOIN Hybris.dbo.PaymentInfos pf ON pt.p_info =pf.PK
JOIN Hybris.dbo.paymnttrnsctentries pte ON pte.p_paymenttransaction = pt.PK
JOIN Hybris.dbo.enumerationvalues ev ON ev.PK =pte.p_type
WHERE EXISTS (SELECT 1 FROM #LoadedTransaction lt WHERE lt.PK = pte.PK)
AND pte.modifiedTS>@LastRun





 CREATE CLUSTERED INDEX MIX_HYTran ON #Hybris_Tran (p_order)


SELECT * INTO #Tran
FROM #RFO_Tran
EXCEPT
SELECT * FROM #Hybris_Tran

SELECT TOP 2 * FROM #Tran

SELECT COUNT(*) FROM #Tran

CREATE CLUSTERED INDEX MIX_Tran ON #Tran (OrderID)


-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

TRUNCATE TABLE DataMigration.Migration.ErrorLog_Orders


DECLARE @I INT = (SELECT  MIN (MapID)FROM DataMigration.Migration.Metadata_Orders WHERE HybrisObject = 'PaymentTransactions'), 
@C INT =  (SELECT MAX(MapID) FROM DataMigration.Migration.Metadata_Orders) 


DECLARE @DesKey NVARCHAR (50) 

DECLARE @SrcKey NVARCHAR (50) 

DECLARE @Skip  BIT 

WHILE (@I <=@c)

BEGIN 

        SELECT  @Skip = ( SELECT   Skip
                               FROM    DataMigration.Migration.Metadata_Orders
                               WHERE    MapID = @I
                             );


        IF ( @Skip = 1 )

            SET @I = @I + 1;

        ELSE
BEGIN 




DECLARE @DesTemp NVARCHAR (50)  = ( SELECT      CASE WHEN HybrisObject = 'Orders'
                                             THEN '#Hybris_Orders'
											  WHEN HybrisObject = 'OrderEntries' THEN '#Hybris_Item'
                                          WHEN HybrisObject = 'Consignments' OR HybrisObject = 'ConsignmentEntries'     THEN '#Hybris_SPItem'
                                         WHEN HybrisObject = 'PaymentInfos' OR HybrisObject = 'PaymentTransactions'    THEN '#Hybris_Pay'
                                           WHEN HybrisObject = 'paymnttrnsctentries' THEN '#Hybris_Tran'
                                              WHEN HybrisObject = 'Addresses_Billing' THEN '#Hybris_BlAdr'
											   WHEN HybrisObject = 'Addresses' THEN '#Hybris_Shadr'
											   WHEN HybrisObject = 'OrderNotes' THEN '#Hybris_Note'
                                            
                                        END
									FROM     DataMigration.Migration.Metadata_Orders
                         
                              WHERE     MapID = @I
                            );  

DECLARE @DesCol NVARCHAR (50) =(SELECT Hybris_Column FROM DataMigration.Migration.Metadata_Orders WHERE MapID = @I)

SET @SrcKey= (SELECT RFO_Key
			  FROM DataMigration.Migration.Metadata_Orders
			  WHERE MapID =@I
								)

    
                SET @DesKey = ( SELECT   
									CASE WHEN HybrisObject = 'Consignments'
                                           THEN 'P_order'
										   WHEN HybrisObject = 'OrderNotes'
                                           THEN 'P_noteid'
										   WHEN HybrisObject = 'PaymentTransactions'
                                           THEN 'P_order'
										--	 WHEN HybrisObject = 'OrderEntries' THEN 'PK'
										ELSE 'PK'
                                        END
                                FROM     DataMigration.Migration.Metadata_Orders
                                WHERE   MapID = @I
                              ); 


DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM DataMigration.Migration.Metadata_Orders WHERE MapID = @I)
DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.Hybris_Value = b. ' + @DesCol +
' FROM DAtaMigration.Migration.ErrorLog_Orders a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.MAPID = ' + CAST(@I AS NVARCHAR)



DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO DAtaMigration.Migration.ErrorLog_Orders (Identifier,MapID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

  BEGIN TRY
   EXEC sp_executesql @SQL3, N'@ServerMod DATETIME', @ServerMod= @LastRun

    --SELECT @SQL3

 SELECT @I, GETDATE()
 SET @I = @I + 1


 END TRY

 BEGIN CATCH

 SELECT @SQL3

 SET @I = @I + 1

 END CATCH
END 

END 

SELECT RFO_column, a.* FROM DataMigration.Migration.ErrorLog_Orders a JOIN DataMigration.Migration.Metadata_orders b ON a.MapID =b.MapID-- AND b.MapID =24
SELECT RFO_column, a.MapID, COUNT(*) FROM DataMigration.Migration.ErrorLog_Orders a JOIN DataMigration.Migration.Metadata_orders b ON a.MapID =b.MapID-- AND b.MapID =24
GROUP BY RFO_Column, a.MapID

END 
