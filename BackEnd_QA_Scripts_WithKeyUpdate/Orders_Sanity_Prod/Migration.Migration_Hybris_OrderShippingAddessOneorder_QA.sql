USE DataMigration
go

CREATE PROCEDURE Migration.Migration_Hybris_OrderShippingAddessOneorder_QA @OrdID BIGINT
AS
BEGIN


DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-06-01' ,
    @RFOCount BIGINT ,
    @RowCount BIGINT ,
    @HybrisCount BIGINT ,
@Orderid BIGINT  =@OrdID
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

IF OBJECT_ID('TEMPDB.dbo.#LoadedShipaddress') IS NOT NULL
    DROP TABLE #LoadedShipAddress

SELECT   DISTINCT
        a.PK
INTO    #LoadedShipAddress
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..Addresses a ON o.pk = a.OwnerPkString
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
        AND p_shippingaddress = 1 
		AND o.PK = @OrderID 


IF OBJECT_ID('TEMPDB.dbo.#RFO_Shadr') IS NOT NULL
    DROP TABLE #RFO_Shadr

SELECT  CAST (OrderShippingAddressID AS NVARCHAR(100)) AS OrderShippingAddressID ,
        CAST (Address1 AS NVARCHAR(100)) AS Address1 ,
        CAST ('0' AS NVARCHAR(100)) AS p_contactAddress ,
        CAST (Telephone AS NVARCHAR(100)) AS Telephone ,
        CAST (FirstName AS NVARCHAR(100)) AS FirstName ,
        CAST ('1' AS NVARCHAR(100)) AS p_shippingaddress ,
        CAST (AddressLine2 AS NVARCHAR(100)) AS AddressLine2 ,
        --CAST (SubRegion AS NVARCHAR(100)) AS SubRegion ,
        CAST (LastName AS NVARCHAR(100)) AS LastName ,
        CAST (Locale AS NVARCHAR(100)) AS Locale ,
        CAST (MiddleName AS NVARCHAR(100)) AS MiddleName ,
        CAST ('0' AS NVARCHAR(100)) AS p_billingaddress ,
        CAST (PostalCode AS NVARCHAR(100)) AS PostalCode ,
        CAST ('1' AS NVARCHAR(100)) AS duplicate ,
        CAST ('0' AS NVARCHAR(100)) AS p_unloadingaddress

----------------------------------------------------------
--Derived Columns 
----------------------------------------------------------
        ,
        CAST (b.OrderID AS NVARCHAR(100)) AS OrderID ,
        CAST (Region AS NVARCHAR(100)) AS Region ,
        CAST (Alpha2Code AS NVARCHAR(100)) AS CountryID
FROM    RFoperations.Hybris.OrderShippingAddress a
        JOIN RFOPerations.Hybris.Orders b ON a.OrderID = b.OrderID
        JOIN RFOperations.RFO_Reference.Countries e ON e.CountryID = b.CountryID
WHERE   EXISTS ( SELECT 1
                 FROM   #LoadedShipaddress lsa
                 WHERE  lsa.PK = a.OrderShippingAddressID )

--EXCEPT


SELECT  CAST (a.PK AS NVARCHAR(100)) AS PK ,
        CAST (p_streetname AS NVARCHAR(100)) AS p_streetname ,
        CAST (p_contactaddress AS NVARCHAR(100)) AS p_contactaddress ,
        CAST (p_phone1 AS NVARCHAR(100)) AS p_phone1 ,
        CAST (p_firstname AS NVARCHAR(100)) AS p_firstname ,
        CAST (p_shippingaddress AS NVARCHAR(100)) AS p_shippingaddress ,
        CAST (p_streetnumber AS NVARCHAR(100)) AS p_streetnumber ,
       -- CAST (p_district AS NVARCHAR(100)) AS p_district ,
        CAST (p_lastname AS NVARCHAR(100)) AS p_lastname ,
        CAST (p_town AS NVARCHAR(100)) AS p_town ,
        CAST (p_middlename AS NVARCHAR(100)) AS p_middlename ,
        CAST (a.p_billingaddress AS NVARCHAR(100)) AS p_billingaddress ,
        CAST (p_postalcode AS NVARCHAR(100)) AS p_postalcode ,
        CAST (a.duplicate AS NVARCHAR(100)) AS duplicate ,
        CAST (p_unloadingaddress AS NVARCHAR(100)) AS p_unloadingaddress

-------------------------------------------------------------------
--Derived Columns 
-------------------------------------------------------------------
        ,
        CAST (a.OwnerPkString AS NVARCHAR(100)) AS OwnerPkString ,
        CAST (b.isocode AS NVARCHAR(100)) AS region ,
        CAST (c.isocode AS NVARCHAR(100)) AS country
FROM    Hybris.dbo.Addresses a
        JOIN Hybris.dbo.countries c ON a.countrypk = c.PK
        LEFT JOIN Hybris.dbo.Regions b ON a.regionpk = b.PK
WHERE   EXISTS ( SELECT 1
                 FROM   #LoadedShipaddress lsa
                 WHERE  lsa.PK = a.PK )
        AND p_shippingaddress = 1 


--		SELECT  *FROM    #RFO_Shadr
----EXCEPT
--SELECT  *
--FROM    #Hybris_Shadr
END 