USE DataMigration
go

CREATE PROCEDURE Migration.Migration_Hybris_OrderShippingAddess_QA
AS 
BEGIN

DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-05-01' ,
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
--OrderShippingaddress counts Hybris & RFO
------------------------------------------------------------------------------------------------------------------------
SELECT  @RFOCount = COUNT(*)
FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderShippingAddress oi ON oi.OrderId = o.OrderID
        INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod

		       

SELECT  @HybrisCount = COUNT(DISTINCT a.PK)
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..addresses a ON o.pk = a.OwnerPkString
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
        AND p_shippingaddress = 1 
        

SELECT  'OrderShippingaddress' ,
        @RFOCount AS RFO_Count ,
        @HybrisCount AS Hybris_Count ,
        @RFOCount - @HybrisCount AS Diff

-------------------------------------------------------------
-- MISSING KEYS
-------------------------------------------------------------

IF OBJECT_ID('DataMigration.Migration.MissingSHipAddress') IS NOT NULL
    DROP TABLE DataMigration.Migration.MissingSHipAddress;

SELECT  OrderShippingAddressID AS RFO_OrderShippingAddressID ,
        PK AS Hybris_OrderShippingAddressID ,
        CASE WHEN c.PK IS NULL THEN 'Destination'
             WHEN a.OrderShippingAddressID IS NULL THEN 'Source'
        END AS MissingFROM
INTO    DataMigration.Migration.MissingSHipAddress
FROM    ( SELECT    OrderShippingAddressID 
          FROM      RFOperations.Hybris.Orders o WITH ( NOLOCK )
                    INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                    INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid= cast(o.AccountID as nvarchar)
                    INNER JOIN RFOperations.Hybris.OrderShippingAddress oi ON oi.OrderId = o.OrderID
                    INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
                    LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
          WHERE     o.CountryID = @RFOCountry
                    AND a.autoshipid IS NULL
                    AND od.startdate >= @ServerMod
        ) a
        FULL OUTER JOIN ( SELECT    a.PK
                          FROM      Hybris.dbo.orders o ( NOLOCK )
                                    INNER JOIN Hybris..addresses a ON o.pk = a.OwnerPkString
                          WHERE     ( p_template = 0
                                      OR p_template IS NULL
                                    )
                                    AND o.TypePkString <> @ReturnOrderType
                                    AND currencypk = @HybCountry
                                    AND p_shippingaddress = 1
                        ) c ON a.OrderShippingAddressID = c.PK  
WHERE   a.OrderShippingAddressID IS NULL
        OR c.PK IS NULL; 




SELECT  @RowCount = COUNT(*)
FROM    DataMigration.Migration.MissingSHipAddress;

IF ( @RowCount > 0 )
    BEGIN 

        SELECT  ' Total Missing ' + @Country + ' ShipmentAddresses' ,
                @RowCount;

        SELECT  MissingFROM ,
                COUNT(*)
        FROM    DataMigration.Migration.MissingSHipAddress
        GROUP BY MissingFROM;

        SELECT  *
        FROM    DataMigration.Migration.MissingSHipAddress
        ORDER BY MissingFROM;

    END;  



----------------------------------------------------
-- DUPLICATES 
-----------------------------------------------------
IF OBJECT_ID('TEMPDB.dbo.#ShipAdr_Dups') IS NOT NULL
    DROP TABLE #ShipAdr_Dups;



SELECT  p_rfaddressid ,
        COUNT(B.PK) AS Hybris_Duplicates
INTO    #ShipAdr_Dups
FROM    RFOperations.Hybris.Orders (NOLOCK) a
        INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = A.OrderID
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = cast(a.AccountID as nvarchar)
        INNER JOIN RFOperations.Hybris.OrderShippingAddress oi ON oi.OrderId = a.OrderID
        INNER JOIN Hybris..orders ho ON ho.pk = a.OrderID
        JOIN Hybris.dbo.Addresses (NOLOCK) b ON oi.OrderShippingAddressID = b.PK
        LEFT JOIN RFOperations.Hybris.Autoship aas WITH ( NOLOCK ) ON CAST(aas.AutoshipNumber AS INT) = CAST (a.ordernumber AS INT)
WHERE   a.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
GROUP BY p_rfaddressid
HAVING  COUNT(B.PK) > 1; 




SELECT  @RowCount = COUNT(*)
FROM    #ShipAdr_Dups;



IF @RowCount > 0
    BEGIN 

        SELECT  'Duplicate ' + @Country + ' Shipment Addresses in Hybris' ,
                @RowCount;

        SELECT  *
        FROM    #SHipAdr_Dups;

    END; 
	
	
-------------------------------------------------------------------------------------	--------------------------------------------------------------------------------------------------------
--- Load OrderShippingAddress
----------------------------------------------------------------------------------------------------------------------


IF OBJECT_ID('TEMPDB.dbo.#Shadr') IS NOT NULL
    DROP TABLE #Shadr
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Shadr') IS NOT NULL
    DROP TABLE #Hybris_Shadr
IF OBJECT_ID('TEMPDB.dbo.#RFO_Shadr') IS NOT NULL
    DROP TABLE #RFO_Shadr


---------------------------------------------------------------------------------------------------------------------


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

SELECT 0, GETDATE()
--------------------------------------------------------------------------------------------------------------------------

SELECT  CAST (OrderShippingAddressID AS NVARCHAR(100)) AS OrderShippingAddressID ,
        CASE WHEN Address1 = ' ' THEN CAST ('0' AS NVARCHAR (100))
			ELSE
		CAST(LTRIM(RTRIM(ISNULL (Address1, '0'))) AS NVARCHAR(100)) END AS Address1 ,
        CAST ('0' AS NVARCHAR(100)) AS p_contactAddress ,
        CAST (LTRIM(RTRIM(Telephone))AS NVARCHAR(100)) AS Telephone ,
        CAST (LTRIM(RTRIM (FirstName)) AS NVARCHAR(100)) AS FirstName ,
        CAST ('1' AS NVARCHAR(100)) AS p_shippingaddress ,
        CAST (LTRIM(RTRIM(ISNULL (AddressLine2, 'NA'))) AS NVARCHAR(100)) AS AddressLine2 ,
        --CAST (SubRegion AS NVARCHAR(100)) AS SubRegion ,
        CAST (LTRIM(RTRIM (ISNULL(LastName,'NA'))) AS NVARCHAR(100)) AS LastName ,
        CAST (LTRIM(RTRIM(ISNULL(Locale,'NA'))) AS NVARCHAR(100)) AS Locale ,
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
INTO    #RFO_Shadr
FROM    RFoperations.Hybris.OrderShippingAddress a
        JOIN RFOPerations.Hybris.Orders b ON a.OrderID = b.OrderID
        JOIN RFOperations.RFO_Reference.Countries e ON e.CountryID = b.CountryID
WHERE   EXISTS ( SELECT 1
                 FROM   #LoadedShipaddress lsa
                 WHERE  lsa.PK = a.OrderShippingAddressID )


SELECT 1, GETDATE()

CREATE CLUSTERED INDEX MIX_RFShadr ON #RFO_Shadr (OrderShippingAddressID)


SELECT  CAST (a.PK AS NVARCHAR(100)) AS PK ,
        CAST (ISNULL (p_streetname, '0') AS NVARCHAR(100)) AS p_streetname ,
        CAST (p_contactaddress AS NVARCHAR(100)) AS p_contactaddress ,
        CAST (p_phone1 AS NVARCHAR(100)) AS p_phone1 ,
        CAST (ISNULL(p_firstname,'') AS NVARCHAR(100)) AS p_firstname ,
        CAST (p_shippingaddress AS NVARCHAR(100)) AS p_shippingaddress ,
        CAST (ISNULL(p_streetnumber,'NA') AS NVARCHAR(100)) AS p_streetnumber ,
        --CAST (p_district AS NVARCHAR(100)) AS p_district ,
        CAST (ISNULL(p_lastname,'NA') AS NVARCHAR(100)) AS p_lastname ,
        CAST (ISNULL(p_town,'NA') AS NVARCHAR(100)) AS p_town ,
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
        CAST (ISNULL(b.isocode,'') AS NVARCHAR(100)) AS region ,
        CAST (c.isocode AS NVARCHAR(100)) AS country
INTO    #Hybris_Shadr
FROM    Hybris.dbo.Addresses a
        JOIN Hybris.dbo.countries c ON a.countrypk = c.PK
        LEFT JOIN Hybris.dbo.Regions b ON a.regionpk = b.PK
WHERE   EXISTS ( SELECT 1
                 FROM   #LoadedShipaddress lsa
                 WHERE  lsa.PK = a.PK )
        AND p_shippingaddress = 1 

CREATE CLUSTERED INDEX MIX_HYShadr ON #Hybris_Shadr (PK)

SELECT 3, GETDATE()

SELECT  *INTO    #Shadr
FROM    #RFO_Shadr
EXCEPT
SELECT  *FROM    #Hybris_Shadr

CREATE CLUSTERED INDEX MIX_shadr ON #Shadr (OrderShippingAddressID)

SELECT  COUNT(*) FROM    #Shadr

SELECT  TOP 2 * FROM    #Shadr



TRUNCATE TABLE DataMigration.Migration.ErrorLog_Orders

DECLARE @LastRUN DATETIME = '05/01/1901'

DECLARE @I INT = ( SELECT   MIN(MapID)
                   FROM     DataMigration.Migration.Metadata_Orders
				   WHERE HybrisObject = 'Addresses'
                 ) ,
    @C INT = ( SELECT   MAX(MapID)
               FROM     DataMigration.Migration.Metadata_Orders
			    WHERE HybrisObject = 'Addresses'
             ) 


DECLARE @DesKey NVARCHAR(50) 

DECLARE @SrcKey NVARCHAR(50) 

DECLARE @Skip BIT 

WHILE ( @I <= @c )
    BEGIN 

        SELECT  @Skip = ( SELECT    Skip
                          FROM      DataMigration.Migration.Metadata_Orders
                          WHERE     MapID = @I
                        );


        IF ( @Skip = 1 )
            SET @I = @I + 1;

        ELSE
            BEGIN 



                --DECLARE @SrcCol NVARCHAR(50) = ( SELECT RFO_Column
                --                                 FROM   RFOperations.dbo.Metadata_Accounts
                --                                 WHERE  MapID = @I
                --                               )

                DECLARE @DesTemp NVARCHAR(50)  = ( SELECT   CASE
                                                              WHEN HybrisObject = 'Orders'
                                                              THEN '#Hybris_Orders'
                                                              WHEN HybrisObject = 'OrderEntries'
                                                              THEN '#Hybris_Item'
                                                              WHEN HybrisObject = 'Consignments'
                                                              OR HybrisObject = 'ConsignmentEntries'
                                                              THEN '#Hybris_SPItem'
                                                              WHEN HybrisObject = 'PaymentInfos'
                                                              OR HybrisObject = 'PaymentTransaction'
                                                              THEN '#Hybris_Pay'
                                                              WHEN HybrisObject = 'paymnttrnsctentries'
                                                              THEN '#Hybris_Tran'
                                                              WHEN HybrisObject = 'Addresses_Billing'
                                                              THEN '#Hybris_BlAdr'
                                                              WHEN HybrisObject = 'Addresses'
                                                              THEN '#Hybris_ShAdr'
                                                              WHEN HybrisObject = 'OrderNotes'
                                                              THEN '#Hybris_Note'
                                                            END
                                                   FROM     DataMigration.Migration.Metadata_Orders
                                                   WHERE    MapID = @I
                                                 );  

                DECLARE @DesCol NVARCHAR(50) = ( SELECT Hybris_Column
                                                 FROM   DataMigration.Migration.Metadata_Orders
                                                 WHERE  MapID = @I
                                               )

                SET @SrcKey = ( SELECT  RFO_Key
                                FROM    DataMigration.Migration.Metadata_Orders
                                WHERE   MapID = @I
                              )

    
                SET @DesKey = ( SELECT  CASE WHEN HybrisObject = 'Consignments'
                                             THEN 'P_order'
										--	 WHEN HybrisObject = 'OrderEntries' THEN 'PK'
                                             ELSE 'PK'
                                        END
                                FROM    DataMigration.Migration.Metadata_Orders
                                WHERE   MapID = @I
                              ); 


                DECLARE @SQL1 NVARCHAR(MAX) = ( SELECT  SqlStmt
                                                FROM    DataMigration.Migration.Metadata_Orders
                                                WHERE   MapID = @I
                                              )
                DECLARE @SQL2 NVARCHAR(MAX) = ' 
 UPDATE A 
SET a.Hybris_Value = b. ' + @DesCol
                    + ' FROM DAtaMigration.Migration.ErrorLog_Orders a  JOIN '
                    + @DesTemp + ' b  ON a.RecordID= b.' + @DesKey
                    + ' WHERE a.MAPID = ' + CAST(@I AS NVARCHAR)



                DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
                    ' INSERT INTO DAtaMigration.Migration.ErrorLog_Orders (Identifier,MapID,RecordID,RFO_Value) '
                    + @SQL1 + @SQL2

                BEGIN TRY
                    EXEC sp_executesql @SQL3, N'@ServerMod DATETIME',
                        @ServerMod = @LastRun

                    SELECT  @SQL3

                    SELECT  @I ,
                            GETDATE()
                    SET @I = @I + 1


                END TRY

                BEGIN CATCH

                    SELECT  @SQL3

                    SET @I = @I + 1

                END CATCH
            END 

    END 

--DROP INDEX MIX_SPItem ON #Item
--DROP INDEX MIX_HyItem ON #Hybris_Item 
--DROP INDEX MIX_RFItem ON #RFO_Item

SELECT  a.MapID, RFO_column ,
        COUNT(*)
FROM    DataMigration.Migration.ErrorLog_Orders a
        JOIN DataMigration.Migration.Metadata_orders b ON a.MapID = b.MapID-- AND b.MapID =24
GROUP BY RFO_column, a.MapID


END