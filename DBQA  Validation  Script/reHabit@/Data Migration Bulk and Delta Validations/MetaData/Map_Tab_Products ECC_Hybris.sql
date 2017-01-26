USE DM_QA
GO 

IF OBJECT_ID('dbqa.Map_tab') IS NULL 
CREATE TABLE  dbqa.Map_tab
(MapID INT IDENTITY(1,1) PRIMARY KEY,
SourceObject NVARCHAR(100),--RFOperations.dbo.Accounts
SourceColumn NVARCHAR(50),--FirstName
SourceDataTypes NVARCHAR(50),--Nvarchar(20)
SourceRef NVARCHAR(MAX)-- Direct or Transform
,TargetObject NVARCHAR(100),--Hybris.dbo.Users
TargetColumn NVARCHAR(50),--P_FirstName
TargetDataTypes NVARCHAR(50),-- Nvarchar(20)
[Key] NVARCHAR(50),--AccountNumber
TargetRef NVARCHAR(MAX),--ComposedTypes
Flag NVARCHAR(10), --C2C or Default or Manual 
[Owner] NVARCHAR(50),
[SQL Stmt] NVARCHAR(Max)--'Select * from TableA a join TabelB b on a.ID=b.ID Where a.Fields<>b.Fields'
)

--********************************************************************************************
--												Products
--********************************************************************************************

INSERT INTO dbqa.Map_tab
        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
VALUES 
/* Products Table in Hybris.*/
(N'ECC_Product_Master' ,N'MaterialTypes',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'InternalCode' ,N'bigint' ,N'CatalogNumber' ,N'Hybris.dbo.composedtypes' , N'ref' ,N'Products',N'') 
,--[Material Types] -TypepkString 
(N'ECC_Product_Master' ,N'CatalogNumber',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_code' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'Key' ,N'Products',N'') 
,-- CatalogNumber -p_code
(N'ECC_Product_Master' ,N'Status ',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_approvalstatus' ,N'bigint' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- Status- -p_approvalstatus
(N'ECC_Product_Master' ,N'UPC',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_ean' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- UPC- -P_ean
(N'ECC_Product_Master' ,N'QV',N'money' ,N'' ,N'Hybris.dbo.pricerows' ,N'p_qv' ,N'decimal(30,8)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- QV
(N'ECC_Product_Master' ,N'SV',N'money' ,N'' ,N'Hybris.dbo.pricerows' ,N'p_sv' ,N'decimal(30,8)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- SV
(N'ECC_Product_Master' ,N'ConsultantsPrice',N'money' ,N'' ,N'Hybris.dbo.pricerows' ,N'CPrice' ,N'decimal(30,8)' ,N'CatalogNumber' ,N'Where UserGroup=Consultnats' , N'ref' ,N'Products',N'') 
,-- Consultant's price
(N'ECC_Product_Master' ,N'PCPrice',N'money' ,N'' ,N'Hybris.dbo.pricerows' ,N'PPrice' ,N'decimal(30,8)' ,N'CatalogNumber' ,N'Where UserGroup=PC' , N'ref' ,N'Products',N'') 
,-- PC's price
(N'ECC_Product_Master' ,N'Currency',N'money' ,N'' ,N'Hybris.dbo.pricerows' ,N'p_currency' ,N'bigint' ,N'CatalogNumber' ,N'Hybris.dbo.currencies' , N'ref' ,N'Products',N'') 
,-- Currency
(N'ECC_Product_Master' ,N'StartTime',N'Datetime' ,N'' ,N'Hybris.dbo.pricerows' ,N'STime' ,N'datetime' ,N'CatalogNumber' ,N'Hybris.dbo.currencies' , N'c2c' ,N'Products',N'') 
,-- Start Time
(N'ECC_Product_Master' ,N'EndTime',N'Datetime' ,N'' ,N'Hybris.dbo.pricerows' ,N'ETime' ,N'datetime' ,N'CatalogNumber' ,N'Hybris.dbo.currencies' , N'c2c' ,N'Products',N'') 
,-- End Time
(N'ECC_Product_Master' ,N'ProductName',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_name' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- [ProductName]
(N'ECC_Product_Master' ,N'shortDescription',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_summary' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- [short Description]
(N'ECC_Product_Master' ,N'LongDescriptions',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_description' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- [Long Descriptions]
(N'ECC_Product_Master' ,N' UsageNotes',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_usagenotes' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,--  [Usage Notes]
(N'ECC_Product_Master' ,N' Ingredients',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_ingredients' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,--  [Ingredients]
(N'ECC_Product_Master' ,N' Weight',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_weight' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'p_weight' , N'c2c' ,N'Products',N'') 
,--  [Weight]
(N'ECC_Product_Master' ,N' VariantType',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_varianttype' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'p_varianttype' , N'c2c' ,N'Products',N'') 
,--  [VariantType]
(N'ECC_Product_Master' ,N' Returnable',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_returnable' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'p_returnable' , N'c2c' ,N'Products',N'') 
,--  [Returnable]
(N'ECC_Product_Master' ,N' unit',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_conversion' ,N'bigint' ,N'CatalogNumber' ,N'Hybris.dbo.units' , N'ref' ,N'Products',N'') 
,--  [unit]
(N'ECC_Product_Master' ,N' CATALOG',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_catalog' ,N'bigint' ,N'CatalogNumber' ,N'Hybris.dbo.Catalogs' , N'ref' ,N'Products',N'') 
,--  [CATALOG]
(N'ECC_Product_Master' ,N' CatalogVersion',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_catalogversion' ,N'bigint' ,N'CatalogNumber' ,N'Hybris.dbo.catalogversions' , N'ref' ,N'Products',N'') 
,--  [CatalogVersion]
(N'ECC_Product_Master' ,N' Manufacturer',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_manufactureraid' ,N'bigint' ,N'CatalogNumber' ,N'p_manufactureraid' , N'c2c' ,N'Products',N'') 
,--  [Manufacturer]
(N'ECC_Product_Master' ,N' ManufacturerName',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_manufacturername' ,N'bigint' ,N'CatalogNumber' ,N'p_manufacturername' , N'c2c' ,N'Products',N'') 
--  [ManufacturerName]

/*
Pending Concerns and Attributes will be added here.
*/



