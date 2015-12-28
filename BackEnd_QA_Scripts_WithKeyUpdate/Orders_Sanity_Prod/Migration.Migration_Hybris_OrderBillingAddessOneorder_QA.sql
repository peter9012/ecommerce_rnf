

USE DataMigration
go

CREATE PROCEDURE  Migration.Migration_Hybris_OrderBillingAddessOneorder_QA @OrdId BIGINT
AS 
BEGIN 
DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-06-01' ,
    @RFOCount BIGINT ,
    @RowCount BIGINT , @OrderID BIGINT = @OrdId,
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
							

---------------------------------------------------------------------------------------------------------------------
--- Load OrdersBillingaddress
----------------------------------------------------------------------------------------------------------------------
IF OBJECT_ID('TEMPDB.dbo.#BlAdr') IS NOT NULL DROP TABLE #BlAdr
IF OBJECT_ID('TEMPDB.dbo.#Hybris_BlAdr') IS NOT NULL DROP TABLE #Hybris_BlAdr
IF OBJECT_ID('TEMPDB.dbo.#RFO_BlAdr') IS NOT NULL DROP TABLE #RFO_BlAdr
IF OBJECT_ID('TEMPDB.dbo.#LoadedBillingAddress') IS NOT NULL DROP TABLE #LoadedBillingAddress

SELECT a.Pk INTO #LoadedBillingAddress
FROM    Hybris.dbo.orders o ( NOLOCK )
		INNER JOIN Hybris.dbo.PaymentInfos pio ON pio.OwnerPkString =o.PK
        INNER JOIN Hybris..addresses a ON pio.pk = a.OwnerPkString
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
        AND a.p_billingaddress = 1 
		AND o.PK =@OrderID


--------------------------------------------------------------------------------------------------------------------------


SELECT
CAST (OrderBillingAddressID AS NVARCHAR(100)) AS OrderBillingAddressID
,CAST (b.OrderID AS NVARCHAR(100)) AS orderID
,CAST (LTRIM(RTRIM(Address1)) AS NVARCHAR(100)) AS Address1
,CAST ('0' AS NVARCHAR(100)) AS p_contactAddress
,CAST (LTRIM(RTRIM(Telephone)) AS NVARCHAR(100)) AS Telephone
,CAST (LTRIM(RTRIM(FirstName))AS NVARCHAR(100)) AS FirstName
,CAST ('0' AS NVARCHAR(100)) AS p_shippingaddress
,CAST (LTRIM(RTRIM(AddressLine2)) AS NVARCHAR(100)) AS AddressLine2
--,CAST (SubRegion AS NVARCHAR(100)) AS SubRegion
,CAST (LTRIM(RTRIM(LastName)) AS NVARCHAR(100)) AS LastName
,CAST (Locale AS NVARCHAR(100)) AS Locale
,CAST (MiddleName AS NVARCHAR(100)) AS MiddleName
,CAST ('1' AS NVARCHAR(100)) AS p_billingaddress
,CAST (PostalCode AS NVARCHAR(100)) AS PostalCode
,CAST ('1' AS NVARCHAR(100)) AS duplicate
,CAST ('0' AS NVARCHAR(100)) AS p_unloadingaddress

----------------------------------------------------------
--Derived Columns 
----------------------------------------------------------
--,CAST (OrderPaymentID AS NVARCHAR(100)) AS OrderPaymentID
,CAST (rp.CODE AS NVARCHAR(100)) AS Region
,CAST (Alpha2Code AS NVARCHAR(100)) AS CountryID

INTO #RFO_BlAdr
FROM RFoperations.Hybris.OrderBillingAddress a 
JOIN DataMigration.Migration.RegionMapping rp ON rp.Region =a.Region
 JOIN RFOPerations.Hybris.Orders b ON a.OrderID =b.OrderID
 JOIN RFOperations.Hybris.OrderPayment d ON d.OrderID =b.OrderID
 JOIN RFOperations.RFO_Reference.Countries e ON e.CountryID =b.CountryID 
WHERE EXISTS (SELECT 1 FROM #LoadedBillingAddress lba WHERE lba.PK = a.OrderBillingAddressID)


SELECT 
CAST (a.PK AS NVARCHAR(100)) AS PK
,CAST(o.PK AS NVARCHAR(100)) AS OrderPK
,CAST (ISNULL (p_streetname,'') AS NVARCHAR(100)) AS p_streetname
,CAST (p_contactaddress AS NVARCHAR(100)) AS p_contactaddress
,CAST (p_phone1 AS NVARCHAR(100)) AS p_phone1
,CAST (p_firstname AS NVARCHAR(100)) AS p_firstname
,CAST (p_shippingaddress AS NVARCHAR(100)) AS p_shippingaddress
,CAST (p_streetnumber AS NVARCHAR(100)) AS p_streetnumber
--,CAST (p_district AS NVARCHAR(100)) AS p_district
,CAST (p_lastname AS NVARCHAR(100)) AS p_lastname
,CAST (ISNULL(p_town, '') AS NVARCHAR(100)) AS p_town
,CAST (p_middlename AS NVARCHAR(100)) AS p_middlename
,CAST (a.p_billingaddress AS NVARCHAR(100)) AS p_billingaddress
,CAST (ISNULL(p_postalcode,'') AS NVARCHAR(100)) AS p_postalcode
,CAST (a.duplicate AS NVARCHAR(100)) AS duplicate
,CAST (p_unloadingaddress AS NVARCHAR(100)) AS p_unloadingaddress

-------------------------------------------------------------------
--Derived Columns 
-------------------------------------------------------------------
--,CAST (a.OwnerPkString AS NVARCHAR(100)) AS OwnerPkString

,CAST (ISNULL(b.isocode,'') AS NVARCHAR(100)) AS region
,CAST (c.isocode AS NVARCHAR(100)) AS country
INTO #Hybris_BlAdr 
FROM Hybris.dbo.Addresses a
 LEFT JOIN Hybris.dbo.Regions b ON a.regionpk =b.PK
JOIN Hybris.dbo.countries c ON a.countrypk =c.PK
JOIN Hybris.dbo.PaymentInfos s ON a.OwnerPKString =s.PK
JOIN Hybris.dbo.Orders o ON s.OwnerPKString = o.PK
WHERE EXISTS (SELECT 1 FROM #LoadedBillingAddress lba WHERE lba.PK = a.pk)


SELECT *
FROM #RFO_Bladr
--EXCEPT
SELECT * FROM #Hybris_Bladr

END 