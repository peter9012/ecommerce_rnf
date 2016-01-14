
USE DataMigration
go
CREATE PROCEDURE Migration.Migration_Hybris_OrderItemOneOrder_QA @OrdID Bigint 
AS 
BEGIN



IF OBJECT_ID('TEMPDB.dbo.#LoadedOrderItems') IS NOT NULL
    DROP TABLE #LoadedOrderItems
	IF OBJECT_ID('TEMPDB.dbo.#Item') IS NOT NULL
    DROP TABLE #Item
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Item') IS NOT NULL
    DROP TABLE #Hybris_Item
IF OBJECT_ID('TEMPDB.dbo.#RFO_Item') IS NOT NULL
    DROP TABLE #RFO_Item



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
DECLARE @orderid BIGINT
SET @orderid = @OrdID
SELECT   DISTINCT
        oe.PK
INTO    #LoadedOrderItems
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..orderentries oe ON oe.orderpk = o.pk
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
        AND o.pk = @orderid

		--SELECT * FROM #LoadedOrderItems
---------------------------------------------------------------------------------------------------------------------
--- Load Order Items 
----------------------------------------------------------------------------------------------------------------------
SELECT  CAST (OrderItemID AS NVARCHAR(100)) AS OrderItemID ,
        --CAST('[]' AS NVARCHAR(100)) AS DiscountValues ,
        CAST(TotalPrice AS NVARCHAR(100)) AS TotalPrice ,
        CAST(WholesalePrice AS NVARCHAR(100)) AS WholesalePrice ,
        CAST(CV AS NVARCHAR(100)) AS CV ,
        CAST(QV AS NVARCHAR(100)) AS QV ,
        CAST(TaxablePrice AS NVARCHAR(100)) AS TaxablePrice ,
        CAST(Quantity AS NVARCHAR(100)) AS Quantity ,
        CAST(BasePrice AS NVARCHAR(100)) AS BasePrice ,
        CAST(( LineItemNo - 1 ) AS NVARCHAR(100)) AS LineItemNo ,
        CAST(RetailProfit AS NVARCHAR(100)) AS RetailProfit

-- TotalTaxValues 

-------------------------------------------------------------------------------
--Derived Columns 
-------------------------------------------------------------------------------
        ,
        CAST (OrderID AS NVARCHAR(100)) AS OrderID ,
        CAST (SKU AS NVARCHAR(100)) AS SKU ,
        CAST (CONCAT('product "', SKU, '_', b.ProductID, '" with Name "{}"') AS NVARCHAR(MAX)) AS Info ,
        CAST ('8796093054986' AS NVARCHAR(100)) AS UnitPK
INTO    #RFO_Item
FROM    RFOperations.Hybris.OrderItem a
        JOIN RFOperations.Hybris.ProductBase b ON a.ProductID = b.ProductID
WHERE   a.OrderItemID IN ( SELECT   PK
                           FROM     #LoadedOrderItems )





SELECT  CAST (a.PK AS NVARCHAR(100)) AS PK ,
       CAST(CAST(totalprice AS MONEY) AS NVARCHAR(100)) AS totalprice ,
        CAST(CAST(p_wholesaleprice AS MONEY) AS NVARCHAR(100)) AS p_wholesaleprice ,
        CAST(CAST (p_cv AS DECIMAL(20, 2)) AS NVARCHAR(100)) AS p_cv ,
        CAST(CAST(p_qv AS DECIMAL(20, 2)) AS NVARCHAR(100)) AS p_qv ,
        CAST(CAST(p_taxableprice AS MONEY) AS NVARCHAR(100)) AS p_taxableprice ,
        CAST(FLOOR(quantity) AS NVARCHAR(100)) AS quantity ,
        CAST(CAST (baseprice AS MONEY) AS NVARCHAR(100)) AS baseprice ,
        CAST(entrynumber AS NVARCHAR(100)) AS entrynumber ,
        CAST(CAST(p_retailprofit AS MONEY) AS NVARCHAR(100)) AS p_retailprofit

-- , CAST (taxvalues AS NVARCHAR(100)) AS taxvalues

-------------------------------------------------------------------------------
--Derived Columns 
-------------------------------------------------------------------------------
        ,
        CAST (orderpk AS NVARCHAR(100)) AS OrderPK ,
        CAST (p_catalogNumber AS NVARCHAR(100)) AS p_catalognumber ,
        CAST(info AS NVARCHAR(100)) AS info ,
        CAST(a.UnitPk AS NVARCHAR(100)) AS UnitPk
INTO    #Hybris_Item
FROM    Hybris.dbo.OrderEntries a
        JOIN Hybris.dbo.Products b ON a.productpk = b.pk
WHERE   a.pk IN ( SELECT    pk
                  FROM      #LoadedOrderItems )
				  
SELECT  *
FROM    #RFO_item
--EXCEPT
SELECT  *
FROM    #Hybris_Item


END

