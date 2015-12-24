USE DataMigration
go

CREATE PROCEDURE Migration.Migration_Hybris_OrderBillingAddess_QA
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
--OrderBillingAddress counts Hybris & RFO
------------------------------------------------------------------------------------------------------------------------
        SELECT  @RFOCount = COUNT(*)
        FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
		INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		
                INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
                INNER JOIN RFOperations.Hybris.OrderBillingAddress oi ON oi.OrderId = o.OrderID
                INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
				INNER JOIN Hybris..PaymentInfos HPI ON hO.PK = HPI.OwnerPKString 
                LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
        WHERE   o.CountryID = @RFOCountry
                AND a.autoshipid IS NULL
                AND od.startdate >= @ServerMod

		       

        SELECT  @HybrisCount = COUNT(DISTINCT a.PK)
        FROM    Hybris.dbo.orders o ( NOLOCK )
                INNER JOIN Hybris.dbo.PaymentInfos pio ON pio.OwnerPkString = o.PK
                INNER JOIN Hybris..addresses a ON pio.pk = a.OwnerPkString
        WHERE   ( p_template = 0
                  OR p_template IS NULL
                )
                AND o.TypePkString <> @ReturnOrderType
                AND currencypk = @HybCountry
                AND a.p_billingaddress = 1 
        

        SELECT  'OrderBillingAddress' ,
                @RFOCount AS RFO_Count ,
                @HybrisCount AS Hybris_Count ,
                @RFOCount - @HybrisCount AS Diff

-------------------------------------------------------------
-- MISSING KEYS
-------------------------------------------------------------

        IF OBJECT_ID('DataMigration.Migration.MissingBillAddress') IS NOT NULL
            DROP TABLE DataMigration.Migration.MissingBillAddress;

        SELECT  OrderBillingAddressID AS RFO_OrderBillingAddressID ,
                PK AS Hybris_OrderBillingAddressID ,
                CASE WHEN c.PK IS NULL THEN 'Destination'
                     WHEN a.OrderBillingAddressID IS NULL THEN 'Source'
                END AS MissingFROM
        INTO    DataMigration.Migration.MissingBillAddress
        FROM    ( SELECT    OrderBillingAddressID ,
                            o.OrderID
                  FROM      RFOperations.Hybris.Orders o WITH ( NOLOCK )
                            INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		
							INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                            INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
                            INNER JOIN RFOperations.Hybris.OrderBillingAddress oi ON oi.OrderId = o.OrderID
                            INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
                            LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
                  WHERE     o.CountryID = @RFOCountry
                            AND a.autoshipid IS NULL
                            AND od.startdate >= @ServerMod
                ) a
                FULL OUTER JOIN ( SELECT    a.PK
                                  FROM      Hybris.dbo.orders o ( NOLOCK )
                                            INNER JOIN Hybris.dbo.PaymentInfos pio ON pio.OwnerPkString = o.PK
                                            INNER JOIN Hybris..addresses a ON pio.pk = a.OwnerPkString
                                  WHERE     ( p_template = 0
                                              OR p_template IS NULL
                                            )
                                            AND o.TypePkString <> @ReturnOrderType
                                            AND currencypk = @HybCountry
                                            AND a.p_billingaddress = 1
                                ) c ON a.OrderBillingAddressID = c.PK
        WHERE   a.OrderBillingAddressID IS NULL
                OR c.PK IS NULL; 




        SELECT  @RowCount = COUNT(*)
        FROM    DataMigration.Migration.MissingBillAddress;

        IF ( @RowCount > 0 )
            BEGIN 

                SELECT  ' Total Missing ' + @Country + ' BillingAddresses' ,
                        @RowCount;

                SELECT  MissingFROM ,
                        COUNT(*)
                FROM    DataMigration.Migration.MissingBillAddress
                GROUP BY MissingFROM;

                SELECT  *
                FROM    DataMigration.Migration.MissingBillAddress
                ORDER BY MissingFROM;

            END;  



----------------------------------------------------
-- DUPLICATES 
-----------------------------------------------------
        IF OBJECT_ID('TEMPDB.dbo.#BillAdr_Dups') IS NOT NULL
            DROP TABLE #BillAdr_Dups;



        SELECT  p_rfaddressid ,
                COUNT(b.PK) AS Hybris_Duplicates
        INTO    #BillAdr_Dups
        FROM    RFOperations.Hybris.Orders (NOLOCK) a
                INNER JOIN RodanFieldsLive.dbo.Orders rfl ON a.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		
				INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = CAST(a.OrderNumber AS INT)
                INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid  = CAST(A.AccountID AS NVARCHAR)
                INNER JOIN RFOperations.Hybris.OrderBillingAddress oi ON oi.OrderId = a.OrderID
                INNER JOIN Hybris..orders ho ON ho.pk = a.OrderID
                INNER JOIN Hybris..paymentinfos pio ON pio.OwnerPkString = ho.PK
                JOIN Hybris.dbo.Addresses (NOLOCK) b ON oi.OrderBillingAddressID = b.PK
                LEFT JOIN RFOperations.Hybris.Autoship ra WITH ( NOLOCK ) ON CAST(ra.AutoshipNumber AS INT) = CAST (a.ordernumber AS INT)
        WHERE   a.CountryID = @RFOCountry
                AND ra.autoshipid IS NULL
                AND od.startdate >= @ServerMod
        GROUP BY p_rfaddressid
        HAVING  COUNT(b.PK) > 1; 




        SELECT  @RowCount = COUNT(*)
        FROM    #BillAdr_Dups;



        IF @RowCount > 0
            BEGIN 

                SELECT  'Duplicate ' + @Country
                        + ' Billing Addresses in Hybris' ,
                        @RowCount;

                SELECT  *
                FROM    #BillAdr_Dups;

            END; 
	
----------------------------------------------------------------------------------------------------------------------------


---------------------------------------------------------------------------------------------------------------------
--- Load OrdersBillingaddress
----------------------------------------------------------------------------------------------------------------------
        IF OBJECT_ID('TEMPDB.dbo.#BlAdr') IS NOT NULL
            DROP TABLE #BlAdr
        IF OBJECT_ID('TEMPDB.dbo.#Hybris_BlAdr') IS NOT NULL
            DROP TABLE #Hybris_BlAdr
        IF OBJECT_ID('TEMPDB.dbo.#RFO_BlAdr') IS NOT NULL
            DROP TABLE #RFO_BlAdr
        IF OBJECT_ID('TEMPDB.dbo.#LoadedBillingAddress') IS NOT NULL
            DROP TABLE #LoadedBillingAddress

        SELECT  a.Pk
        INTO    #LoadedBillingAddress
        FROM    Hybris.dbo.orders o ( NOLOCK )
                INNER JOIN Hybris.dbo.PaymentInfos pio ON pio.OwnerPkString = o.PK
                INNER JOIN Hybris..addresses a ON pio.pk = a.OwnerPkString
        WHERE   ( p_template = 0
                  OR p_template IS NULL
                )
                AND o.TypePkString <> @ReturnOrderType
                AND currencypk = @HybCountry
                AND a.p_billingaddress = 1 



--------------------------------------------------------------------------------------------------------------------------


        SELECT  CAST (OrderBillingAddressID AS NVARCHAR(100)) AS OrderBillingAddressID ,
                CAST (b.OrderID AS NVARCHAR(100)) AS orderID ,
                CAST (LTRIM(RTRIM(Address1)) AS NVARCHAR(100)) AS Address1 ,
                CAST ('0' AS NVARCHAR(100)) AS p_contactAddress ,
                CAST (LTRIM(RTRIM(Telephone)) AS NVARCHAR(100)) AS Telephone ,
                CAST (LTRIM(RTRIM(FirstName)) AS NVARCHAR(100)) AS FirstName ,
                CAST ('0' AS NVARCHAR(100)) AS p_shippingaddress ,
                CAST (LTRIM(RTRIM(AddressLine2)) AS NVARCHAR(100)) AS AddressLine2
--,CAST (SubRegion AS NVARCHAR(100)) AS SubRegion
                ,
                CAST (LTRIM(RTRIM(LastName)) AS NVARCHAR(100)) AS LastName ,
                CAST (Locale AS NVARCHAR(100)) AS Locale ,
                CAST (MiddleName AS NVARCHAR(100)) AS MiddleName ,
                CAST ('1' AS NVARCHAR(100)) AS p_billingaddress ,
                CAST (ISNULL(PostalCode, '') AS NVARCHAR(100)) AS PostalCode ,
                CAST ('1' AS NVARCHAR(100)) AS duplicate ,
                CAST ('0' AS NVARCHAR(100)) AS p_unloadingaddress

----------------------------------------------------------
--Derived Columns 
----------------------------------------------------------
                ,
                CAST (OrderPaymentID AS NVARCHAR(100)) AS OrderPaymentID ,
                CAST (rp.Code AS NVARCHAR(100)) AS Region ,
                CAST (Alpha2Code AS NVARCHAR(100)) AS CountryID
        INTO    #RFO_BlAdr
        FROM    RFoperations.Hybris.OrderBillingAddress a
                JOIN RFOPerations.Hybris.Orders b ON a.OrderID = b.OrderID
				INNER JOIN RodanFieldsLive.dbo.Orders rfl ON b.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		
                JOIN DataMigration.Migration.RegionMapping rp ON rp.region = a.region
                JOIN RFOperations.Hybris.OrderPayment d ON d.OrderID = b.OrderID
                JOIN RFOperations.RFO_Reference.Countries e ON e.CountryID = b.CountryID
        WHERE   EXISTS ( SELECT 1
                         FROM   #LoadedBillingAddress lba
                         WHERE  lba.PK = a.OrderBillingAddressID )

        CREATE CLUSTERED INDEX MIX_RFBlAdr ON #RFO_BlAdr (OrderBillingAddressID)


        SELECT  CAST (a.PK AS NVARCHAR(100)) AS PK ,
                CAST(o.PK AS NVARCHAR(100)) AS OrderPK ,
                CAST (ISNULL(p_streetname, '') AS NVARCHAR(100)) AS p_streetname ,
                CAST (p_contactaddress AS NVARCHAR(100)) AS p_contactaddress ,
                CAST (p_phone1 AS NVARCHAR(100)) AS p_phone1 ,
                CAST (ISNULL(p_firstname, '') AS NVARCHAR(100)) AS p_firstname ,
                CAST (p_shippingaddress AS NVARCHAR(100)) AS p_shippingaddress ,
                CAST (p_streetnumber AS NVARCHAR(100)) AS p_streetnumber
--,CAST (p_district AS NVARCHAR(100)) AS p_district
                ,
                CAST (ISNULL(p_lastname, '') AS NVARCHAR(100)) AS p_lastname ,
                CAST (ISNULL(p_town, '') AS NVARCHAR(100)) AS p_town ,
                CAST (p_middlename AS NVARCHAR(100)) AS p_middlename ,
                CAST (a.p_billingaddress AS NVARCHAR(100)) AS p_billingaddress ,
                CAST (ISNULL(p_postalcode, '') AS NVARCHAR(100)) AS p_postalcode ,
                CAST (a.duplicate AS NVARCHAR(100)) AS duplicate ,
                CAST (p_unloadingaddress AS NVARCHAR(100)) AS p_unloadingaddress

-------------------------------------------------------------------
--Derived Columns 
-------------------------------------------------------------------
                ,
                CAST (a.OwnerPkString AS NVARCHAR(100)) AS OwnerPkString ,
                CAST (ISNULL(b.isocode, '') AS NVARCHAR(100)) AS region ,
                CAST (c.isocode AS NVARCHAR(100)) AS country
        INTO    #Hybris_BlAdr
        FROM    Hybris.dbo.Addresses a
                LEFT JOIN Hybris.dbo.Regions b ON a.regionpk = b.PK
                JOIN Hybris.dbo.countries c ON a.countrypk = c.PK
                JOIN Hybris.dbo.PaymentInfos s ON a.OwnerPKString = s.PK
                JOIN Hybris.dbo.Orders o ON s.OwnerPKString = o.PK
        WHERE   EXISTS ( SELECT 1
                         FROM   #LoadedBillingAddress lba
                         WHERE  lba.PK = a.pk )


        CREATE CLUSTERED INDEX MIX_HYBladr ON #Hybris_Bladr (PK)


        SELECT  *
        INTO    #Bladr
        FROM    #RFO_Bladr
        EXCEPT
        SELECT  *
        FROM    #Hybris_Bladr

        SELECT  COUNT(*)
        FROM    #Bladr

        SELECT TOP 2
                *
        FROM    #Bladr


        CREATE CLUSTERED INDEX MIX_Bladr ON #Bladr (OrderBillingAddressID)




-----------------------------------------------------------------------------------------------------------------------------

        TRUNCATE TABLE DataMigration.Migration.ErrorLog_Orders

        DECLARE @LastRUN DATETIME = '05/01/1901'

        DECLARE @I INT = ( SELECT   MIN(MapID)
                           FROM     DataMigration.Migration.Metadata_Orders
                           WHERE    HybrisObject = 'Addresses_Billing'
                         ) ,
            @C INT = ( SELECT   MAX(MapID)
                       FROM     DataMigration.Migration.Metadata_Orders
                       WHERE    HybrisObject = 'Addresses_Billing'
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




                        DECLARE @DesTemp NVARCHAR(50)  = ( SELECT
                                                              CASE
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
                                                           FROM
                                                              DataMigration.Migration.Metadata_Orders
                                                           WHERE
                                                              MapID = @I
                                                         );  

                        DECLARE @DesCol NVARCHAR(50) = ( SELECT
                                                              Hybris_Column
                                                         FROM DataMigration.Migration.Metadata_Orders
                                                         WHERE
                                                              MapID = @I
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


                        DECLARE @SQL1 NVARCHAR(MAX) = ( SELECT
                                                              SqlStmt
                                                        FROM  DataMigration.Migration.Metadata_Orders
                                                        WHERE MapID = @I
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

                    --SELECT  @SQL3

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

        SELECT  a.MapID ,
                RFO_column ,
                COUNT(*)
        FROM    DataMigration.Migration.ErrorLog_Orders a
                JOIN DataMigration.Migration.Metadata_orders b ON a.MapID = b.MapID-- AND b.MapID =24
GROUP BY        RFO_column ,
                a.MapID


        SELECT DISTINCT
                a.mapid ,
                RFO_column ,
                Identifier ,
                a.RecordID ,
                '[' + a.RFO_Value + ']' AS RFOvalue ,
                '[' + a.Hybris_Value + ']' AS Hybrisvalue
        FROM    DataMigration.Migration.ErrorLog_Orders a
                JOIN DataMigration.Migration.Metadata_orders b ON a.MapID = b.MapID-- AND b.MapID =24
        WHERE   a.MapID IN ( 63, 67 )


    END
