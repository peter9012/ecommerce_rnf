USE DataMigration
go

CREATE PROCEDURE Migration.Migration_Hybris_OrderShippingAddess_QA @LastRun DATETIME = '2014-05-01'
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
								  
SELECT  @RFOCount = COUNT(*)
FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR) and U.P_SOURCENAME='Hybris-DM'
        INNER JOIN RFOperations.Hybris.OrderShippingAddress oi ON oi.OrderId = o.OrderID
        --INNER JOIN Hybris..orders ho ON ho.code = o.OrderNumber
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
		AND oi.ServerModifiedDate> @LastRun
		       

SELECT  @HybrisCount = COUNT(DISTINCT a.PK)
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..addresses a ON o.pk = a.OwnerPkString AND a.modifiedTS> @LAstRun AND O.userpk in (select pk from hybris..users where P_SOURCENAME='Hybris-DM')
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
        p_rfaddressid AS Hybris_OrderShippingAddressID ,
        CASE WHEN c.p_rfaddressid IS NULL THEN 'Destination'
             WHEN a.OrderShippingAddressID IS NULL THEN 'Source'
        END AS MissingFROM
INTO    DataMigration.Migration.MissingSHipAddress
FROM    ( SELECT    OrderShippingAddressID ,
                    O.OrderID
          FROM      RFOperations.Hybris.Orders o WITH ( NOLOCK )
                    INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
					INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                    INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid= cast(o.AccountID as nvarchar) U.P_SOURCENAME='Hybris-DM'
                    INNER JOIN RFOperations.Hybris.OrderShippingAddress oi ON oi.OrderId = o.OrderID
                    LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
          WHERE     o.CountryID = @RFOCountry
                    AND a.autoshipid IS NULL
                    AND od.startdate >= @ServerMod
					AND oi.ServerModifiedDate> @LastRun
        ) a
        FULL OUTER JOIN ( SELECT    a.p_rfaddressid 
                          FROM      Hybris.dbo.orders o ( NOLOCK )
                                    INNER JOIN Hybris..addresses a ON o.pk = a.OwnerPkString AND a.modifiedTS> @LastRun AND O.userpk in (select pk from hybris..users where P_SOURCENAME='Hybris-DM')
                          WHERE     ( p_template = 0
                                      OR p_template IS NULL
                                    )
                                    AND o.TypePkString <> @ReturnOrderType
                                    AND currencypk = @HybCountry
                                    AND p_shippingaddress = 1
                        ) c ON a.OrderShippingAddressID = c.p_rfaddressid
WHERE   a.OrderShippingAddressID IS NULL
        OR c.p_rfaddressid IS NULL; 

				
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
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = A.OrderID
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = cast(a.AccountID as nvarchar) AND U.P_SOURCENAME='Hybris-DM'
        INNER JOIN RFOperations.Hybris.OrderShippingAddress oi ON oi.OrderId = a.OrderID
        INNER JOIN Hybris..orders ho ON ho.code = a.OrderNumber AND ( p_template = 0 OR p_template IS NULL)
        JOIN Hybris.dbo.Addresses (NOLOCK) b ON oi.OrderShippingAddressID = b.p_rfaddressid AND b.ownerpkstring=ho.pk
        LEFT JOIN RFOperations.Hybris.Autoship aas WITH ( NOLOCK ) ON CAST(aas.AutoshipNumber AS INT) = CAST (a.ordernumber AS INT)
WHERE   a.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
		AND oi.ServerModifiedDate> @LAstRun
		AND B.p_shippingaddress = 1 
GROUP BY p_rfaddressid
HAVING  COUNT(B.PK) > 1; 

SELECT * FROM #ShipAdr_Dups




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
        a.p_rfaddressid
INTO    #LoadedShipAddress
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..Addresses a ON o.pk = a.OwnerPkString AND O.userpk in (select pk from hybris..users where P_SOURCENAME='Hybris-DM')
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
        AND p_shippingaddress = 1 
		AND a.modifiedTS> @LAstRun

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
		INNER JOIN RodanFieldsLive.dbo.Orders rfl ON B.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND B.CountryID = @RFOCountry 
        JOIN RFOperations.RFO_Reference.Countries e ON e.CountryID = b.CountryID
WHERE   EXISTS ( SELECT 1
                 FROM   #LoadedShipaddress lsa
                 WHERE  lsa.P_RFADDRESSID = a.OrderShippingAddressID )
		
		
CREATE CLUSTERED INDEX MIX_RFShadr ON #RFO_Shadr (OrderShippingAddressID)


SELECT  CAST (a.P_rfaddressid AS NVARCHAR(100)) AS P_rfaddressid ,
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
                 WHERE  lsa.p_rfaddressid = a.p_rfaddressid )
        AND p_shippingaddress = 1 

CREATE CLUSTERED INDEX MIX_HYShadr ON #Hybris_Shadr (p_rfaddressid)


SELECT  *INTO    #Shadr
FROM    #RFO_Shadr
EXCEPT
SELECT  * FROM    #Hybris_Shadr

CREATE CLUSTERED INDEX MIX_shadr ON #Shadr (OrderShippingAddressID)

SELECT  COUNT(*) FROM    #Shadr

SELECT * FROM    #Shadr



TRUNCATE TABLE DataMigration.Migration.ErrorLog_Orders


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

--					SELECT * FROM DAtaMigration.Migration.ErrorLog_Orders

                DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
                    ' INSERT INTO DAtaMigration.Migration.ErrorLog_Orders (Identifier,MapID,RecordID,RFO_Value) '
                    + @SQL1 + @SQL2

                BEGIN TRY
                    EXEC sp_executesql @SQL3, N'@ServerMod DATETIME',
                        @ServerMod = @LastRun

                    --SELECT  @SQL3

                    SELECT  @I ,
                            GETDATE()
                    SET @I = @I + 1


                END TRY

                BEGIN CATCH

                    --SELECT  @SQL3

                    SET @I = @I + 1

                END CATCH
            END 

    END 


END