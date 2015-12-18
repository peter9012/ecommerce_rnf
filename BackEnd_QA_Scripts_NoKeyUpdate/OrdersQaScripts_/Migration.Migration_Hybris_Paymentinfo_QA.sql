	USE DataMigration
go

CREATE PROCEDURE Migration.Migration_Hybris_Paymentinfo_QA @LastRun '2014-05-01'
AS
    BEGIN 


        SET NOCOUNT ON

        SET ANSI_WARNINGS OFF 

        SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED


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
--OrderPayment counts Hybris & RFO
------------------------------------------------------------------------------------------------------------------------
SELECT  @RFOCount = COUNT(*)
FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.orderid
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON p_rfaccountid = cast(o.AccountID as nvarchar)
        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o.OrderID
        INNER JOIN Hybris..orders ho ON ho.code = o.OrderNumber
		INNER JOIN Hybris..PaymentInfos HPI ON HO.PK = HPI.OwnerPKString AND HPI.Code = CAST (Oi.OrderPaymentID AS NVARCHAR)
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
		AND oi.ServerModifiedDate>@LastRun

		       

SELECT  @HybrisCount = COUNT(DISTINCT a.PK)
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..paymentInfos a ON o.pk = a.OwnerPkString AND a.modifiedTS>@LastRun
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry
      
        

SELECT  'OrderPayment' ,
        @RFOCount AS RFO_Count ,
        @HybrisCount AS Hybris_Count ,
        @RFOCount - @HybrisCount AS Diff

-------------------------------------------------------------
-- MISSING KEYS
-------------------------------------------------------------

IF OBJECT_ID('DataMigration.Migration.MissingPayment') IS NOT NULL
    DROP TABLE DataMigration.Migration.MissingPayment;

SELECT  orderPaymentID AS RFO_OrderPaymentID ,
        CODE AS Hybris_OrderPaymentID ,
        CASE WHEN c.CODE IS NULL THEN 'Destination'
             WHEN a.orderPaymentID IS NULL THEN 'Source'
        END AS MissingFROM
INTO     DataMigration.Migration.MissingPayment
FROM    ( SELECT    OrderPaymentID 
               
          FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = CAST(o.OrderNumber AS INT)
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o.OrderID
        INNER JOIN Hybris..orders ho ON ho.code = o.OrderNumber
		INNER JOIN Hybris..PaymentInfos HPI ON HO.PK = HPI.OwnerPKString --AND HPI.Code = CAST (Oi.OrderPaymentID AS NVARCHAR)
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
		AND oi.ServerModifiedDate>@LastRun

        ) a
        FULL OUTER JOIN ( SELECT    a.code
                          FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..paymentInfos a ON o.pk = a.OwnerPkString AND a.modifiedTS> @LastRun
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry


                        ) c ON CAST(a.orderPaymentID AS NVARCHAR) = c.CODE  
WHERE   a.orderPaymentID IS NULL
        OR c.CODE IS NULL; 




SELECT  @RowCount = COUNT(*)
FROM     DataMigration.Migration.MissingPayment;

IF ( @RowCount > 0 )
    BEGIN 

        SELECT  ' Total Missing ' + @Country + ' Payments' ,
                @RowCount;

        SELECT  MissingFROM ,
                COUNT(*)
        FROM    DataMigration.Migration.MissingPayment
        GROUP BY MissingFROM;

        SELECT  *
        FROM    DataMigration.Migration.MissingPayment
        ORDER BY MissingFROM;

    END;  



----------------------------------------------------
-- DUPLICATES 
-----------------------------------------------------
IF OBJECT_ID('TEMPDB.dbo.#Pay_Dups') IS NOT NULL
    DROP TABLE #Pay_Dups;



SELECT  p.code,
        COUNT(p.PK) AS Hybris_Duplicates
INTO    #Pay_Dups
FROM   RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = cast(o.AccountID as nvarchar)
        INNER JOIN RFOperations.Hybris.OrderPayment oi ON oi.OrderId = o.OrderID
        INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
		INNER JOIN Hybris..paymentinfos p ON p.OwnerPkString =ho.PK AND P.Code = CAST (Oi.OrderPaymentID AS NVARCHAR)
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
GROUP BY p.code
HAVING  COUNT(p.PK) > 1; 




SELECT  @RowCount = COUNT(*)
FROM    #Pay_Dups;



IF @RowCount > 0
    BEGIN 

        SELECT  'Duplicate ' + @Country + ' Payment in Hybris' ,
                @RowCount;

        SELECT  *
        FROM    #Pay_Dups;

    END; 

	
---------------------------------------------------------------------------------------------
--Load Payments 
---------------------------------------------------------------------------------------------

        IF OBJECT_ID('TEMPDB.dbo.#Pay') IS NOT NULL
            DROP TABLE #Pay
        IF OBJECT_ID('TEMPDB.dbo.#Hybris_Pay') IS NOT NULL
            DROP TABLE #Hybris_Pay
        IF OBJECT_ID('TEMPDB.dbo.#RFO_Pay') IS NOT NULL
            DROP TABLE #RFO_Pay
        IF OBJECT_ID('TEMPDB.dbo.#LoadedPayInfo') IS NOT NULL
            DROP TABLE #LoadedPayInfo

        SELECT  a.CODE
        INTO    #LoadedPayInfo
        FROM    Hybris.dbo.orders o ( NOLOCK )
                INNER JOIN Hybris..paymentInfos a ON o.pk = a.OwnerPkString  AND a.modifiedTS> @LastRun
        WHERE   ( p_template = 0
                  OR p_template IS NULL
                )
                AND o.TypePkString <> @ReturnOrderType
                AND currencypk = @HybCountry



--------------------------------------------------------------------------------------------------------------------------

;
        WITH    CTE
                  AS ( SELECT   OrderPaymentID ,
                                c.AccountID ,
                                MAX(b.PaymentProfileID) AS PaymentProfileID
                       FROM     RFoperations.Hybris.OrderPayment a
                                JOIN RFOperations.RFO_Accounts.CreditCardProfiles b ON a.DisplayNumber = b.DisplayNumber
                                                              AND b.ExpMonth = a.Expmonth
                                                              AND b.ExpYear = a.ExpYear
                                JOIN RFoperations.Hybris.Orders c ON a.OrderID = c.OrderID
                                JOIN RFOperations.RFO_Accounts.PaymentProfiles pp ON pp.PaymentProfileID = b.PaymentProfileID
                                                              AND c.AccountID = pp.AccountID
                       GROUP BY c.AccountID ,
                                c.OrderID ,
                                OrderPaymentID
                     )
            SELECT  CAST (a.OrderPaymentID AS NVARCHAR(100)) AS OrderPaymentID ,
                    CAST(b.PaymentProfileID AS NVARCHAR(100)) AS PaymentProfileID,
					CONCAT (SUBSTRING (a.DisplayNumber, 1, 6),SUBSTRING (a.DisplayNumber, (LEN(a.DisplayNumber) -3), 4), ExpMonth,ExpYear) AS SubscriptionID,
--,CAST( RFOperations.[dbo].[DecryptTripleDES] (f.AccountNumber) AS NVARCHAR (200)) AS  CardNumber
                    CAST(a.Expmonth AS NVARCHAR(100)) AS Expmonth ,
                    CAST(a.ExpYear AS NVARCHAR(100)) AS ExpYear ,
                    CAST(LTRIM(RTRIM(CONCAT(FirstName, ' ', LastName))) AS NVARCHAR(100)) AS ccowner

--DerivedColumns 
                    ,
                    CAST(a.OrderID AS NVARCHAR(100)) AS OrderID ,
                    CAST (b.AccountID AS NVARCHAR(100)) AS AccountID
            INTO    #RFO_Pay
            FROM    RFoperations.Hybris.OrderPayment a
					INNER JOIN RodanFieldsLive.dbo.Orders rfl ON A.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND A.CountryID = @RFOCountry 
			
                    JOIN cte b ON a.OrderPaymentID = b.OrderPaymentID
                    JOIN RFOperations.Hybris.OrderBillingAddress ob ON ob.OrderID = a.OrderID
                    LEFT JOIN RodanFieldsLive.dbo.OrderPayments f ON a.OrderPaymentID = f.OrderPaymentID
            WHERE   EXISTS ( SELECT 1
                             FROM   #LoadedPayInfo lpi
                             WHERE  lpi.CODE = CAST(a.OrderPaymentID AS NVARCHAR)) 








        CREATE CLUSTERED INDEX MIX_RFPay ON #RFO_Pay (OrderPaymentID)


        SELECT  CAST (a.CODE AS NVARCHAR(100)) AS CODE ,
                CAST(p_rfaccountpaymentmethodid AS NVARCHAR(100)) AS p_rfaccountpaymentmethodid,
				CAST(p_subscriptionID as nvarchar(100)) AS p_subscriptionID,
				--,CAST( p_number AS NVARCHAR (100)) AS  p_number
                 CAST(p_validtomonth AS NVARCHAR(100)) AS p_validtomonth ,
                CAST(p_validtoyear AS NVARCHAR(100)) AS p_validtoyear ,
                CAST(p_ccowner AS NVARCHAR(100)) AS ccowner
--,CAST( sourcename AS NVARCHAR (100)) AS  sourcename
------------------------------------------------------------
--DerivedColumns 
                ,
                CAST(a.ownerpkstring AS NVARCHAR(100)) AS owner ,
                CAST(b.p_rfaccountid AS NVARCHAR(100)) AS Userpk
        INTO    #Hybris_Pay
        FROM    Hybris.dbo.PaymentInfos a
                INNER JOIN Hybris..Users b ON a.userpk = b.PK
        WHERE   EXISTS ( SELECT 1
                         FROM   #LoadedPayInfo lpi
                         WHERE  lpi.CODE = a.CODE )

        
        SELECT  *
        INTO    #Pay
        FROM    #RFO_Pay
        EXCEPT
        SELECT  *
        FROM    #Hybris_Pay

        CREATE CLUSTERED INDEX MIX_Pay ON #Pay (OrderPaymentID)
		
		CREATE CLUSTERED INDEX MIX_HBPay ON #Hybris_Pay (code)


        TRUNCATE TABLE DataMigration.Migration.ErrorLog_Orders


        DECLARE @I INT = ( SELECT   MIN(MapID)
                           FROM     DataMigration.Migration.Metadata_Orders
                           WHERE    HybrisObject = 'PaymentInfos'
                         ) ,
            @C INT = ( SELECT   MAX(MapID)
                       FROM     DataMigration.Migration.Metadata_Orders
                       WHERE    HybrisObject = 'PaymentInfos'
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



--DECLARE @SrcCol NVARCHAR (50) =(SELECT RFO_Column FROM DataMigration.Migration.Metadata_Orders WHERE MapID = @I)

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
                                                              OR HybrisObject = 'PaymentTransactions'
                                                              THEN '#Hybris_Pay'
                                                              WHEN HybrisObject = 'paymnttrnsctentries'
                                                              THEN '#Hybris_Tran'
                                                              WHEN HybrisObject = 'Addresses_Billing'
                                                              THEN '#Hybris_BlAdr'
                                                              WHEN HybrisObject = 'Addresses'
                                                              THEN '#Hybris_Shadr'
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
                                                     WHEN HybrisObject = 'OrderNotes'
                                                     THEN 'P_noteid'
                                                     WHEN HybrisObject = 'PaymentTransactions'
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

   -- SELECT @SQL3

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


/*

        SELECT  *
        FROM    DataMigration.Migration.ErrorLog_Orders a
                JOIN DataMigration.Migration.Metadata_Orders b ON a.MapID = b.MapID 
--WHERE a.MapID =107
GROUP BY        mapID


*/
    END 


