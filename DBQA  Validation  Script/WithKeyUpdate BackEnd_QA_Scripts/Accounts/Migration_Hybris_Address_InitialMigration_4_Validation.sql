USE [DataMigration];
GO
/****** Object:  StoredProcedure [Migration].[Migration_Hybris_Address_InitialMigration_4_Validation] 
   Revised Date: 12/21/2015  ******/
SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO



SET NOCOUNT ON;
SET ANSI_WARNINGS OFF; 

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;



/*=================================================================================================
-- Part 1: Counts, Missing Keys, Duplicates 
================================================================================================= */


IF OBJECT_ID('DataMigration.Migration.MissingAddresses') IS NOT NULL
    DROP TABLE DataMigration.Migration.MissingAddresses; 

IF OBJECT_ID('TEMPDB.dbo.#Addresses_Dups') IS NOT NULL
    DROP TABLE #Accounts_Dups; 

DECLARE @LastRun DATETIME = '06/01/1901';

SET ANSI_WARNINGS OFF; 
DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '06/01/1901';
DECLARE @RFOCountry INT = ( SELECT  CountryID
                            FROM    RFOperations.RFO_Reference.Countries (NOLOCK)
                            WHERE   Alpha2Code = @Country
                          ) ,
    @RowCount BIGINT ,
    @HybCountry BIGINT = ( SELECT   PK
                           FROM     Hybris.dbo.countries (NOLOCK)
                           WHERE    isocode = @Country
                         );


DECLARE @RFOAddress BIGINT ,
    @HYBAddress BIGINT;

SELECT  @RFOAddress = COUNT(DISTINCT b.AddressID)
FROM    RFOperations.RFO_Accounts.AccountContacts a
        JOIN RFOperations.RFO_Accounts.AccountContactAddresses b ON a.AccountContactId = b.AccountContactId
        JOIN RFOperations.RFO_Accounts.Addresses c ON b.AddressID = c.AddressID
                                                      AND AddressTypeID IN ( 1,
                                                              2 )
        JOIN Hybris.dbo.users d ON d.p_rfaccountid = CAST(a.AccountId AS VARCHAR)
WHERE   CountryID = @RFOCountry
        AND d.p_sourcename = 'Hybris-DM';


SELECT  @HYBAddress = COUNT(*)
FROM    ( SELECT DISTINCT
                    a.PK
          FROM      Hybris.dbo.addresses (NOLOCK) a
                    JOIN Hybris.dbo.users b ON a.OwnerPkString = b.PK
          WHERE     countrypk = @HybCountry
                    AND b.p_sourcename = 'Hybris-DM'
                    AND duplicate = 0
        ) SUB;

SELECT  'Addresses' ,
        @RFOAddress AS RFO_Count ,
        @HYBAddress AS Hybris_Count ,
        @RFOAddress - @HYBAddress AS Differences;

---------------------------------------------------------------------------------------------------------------
---- Missing Keys 
---------------------------------------------------------------------------------------------------------------
--DROP TABLE DataMigration.Migration.MissingAddresses

SELECT  AddressID AS RFO_AddressID ,
        b.p_rfaddressid AS Hybris_rfAddressID ,
        CASE WHEN b.p_rfaddressid IS NULL THEN 'Destination'
             WHEN a.AddressID IS NULL THEN 'Source'
        END AS MissingFROM
INTO    DataMigration.Migration.MissingAddresses
FROM    ( SELECT    CAST (c.AddressID AS NVARCHAR) AS AddressID
          FROM      RFOperations.RFO_Accounts.AccountContacts a
                    JOIN RFOperations.RFO_Accounts.AccountContactAddresses b ON a.AccountContactId = b.AccountContactId
                    JOIN RFOperations.RFO_Accounts.Addresses c ON b.AddressID = c.AddressID
                                                              AND AddressTypeID IN (
                                                              1, 2 )
                    JOIN Hybris.dbo.users d ON d.p_rfaccountid = CAST(a.AccountId AS VARCHAR)
                                               AND CountryID = @RFOCountry
                                               AND d.p_sourcename = 'Hybris-DM'
        ) a
        FULL OUTER JOIN ( SELECT    a.PK ,
                                    a.p_rfaddressid
                          FROM      Hybris.dbo.addresses (NOLOCK) a
                                    JOIN Hybris.dbo.users b ON a.OwnerPkString = b.PK
                          WHERE     countrypk = @HybCountry
                                    AND b.p_sourcename = 'Hybris-DM'
                                    AND duplicate = 0
                        ) b ON b.p_rfaddressid = CAST(a.AddressID AS VARCHAR)
WHERE   ( a.AddressID IS NULL
          OR b.p_rfaddressid IS NULL
        );

SELECT  MissingFrom ,
        COUNT(*)
FROM    DataMigration.Migration.MissingAddresses
GROUP BY MissingFROM; 


/***************************************************************************************************************
--- TROUBLESHOOTING Missing Records ---

SELECT a. AccountID, c.AddressID FROM RFOperations.RFO_Accounts.AccountContacts a 
 JOIN Hybris.dbo.Users d ON d.p_rfAccountID = CAST(a.AccountID AS VARCHAR)
 JOIN RFOperations.RFO_Accounts.AccountContactAddresses b ON a.AccountContactID =b.AccountContactID 
 JOIN RFOperations.RFO_Accounts.Addresses c ON b.AddressID =c.AddressID
WHERE c.AddressID IN (
SELECT Hybris_rfAddressID FROM DataMigration.Migration.MissingAddresses
WHERE MissingFrom ='Source') AND d.p_sourcename = 'Hybris-DM'

SELECT a. AccountID, c.AddressID, c.AddressTypeID FROM RFOperations.RFO_Accounts.AccountContacts a 
 JOIN Hybris.dbo.Users d ON d.p_rfAccountID = CAST(a.AccountID AS VARCHAR)
 JOIN RFOperations.RFO_Accounts.AccountContactAddresses b ON a.AccountContactID =b.AccountContactID 
 JOIN RFOperations.RFO_Accounts.Addresses c ON b.AddressID =c.AddressID
WHERE c.AddressID IN (
SELECT RFO_AddressID FROM DataMigration.Migration.MissingAddresses
WHERE MissingFrom ='Destination') AND d.p_sourcename = 'Hybris-DM'

SELECT * FROM Hybris.dbo.Users
WHERE p_rfAccountID ='765049'

p_rfAddressID = '5936063'--
SELECT * FROM Hybris.dbo.Addresses 
WHERE  OwnerPKString =8807072497668

SELECT c.AddressID, AddressTypeID
FROM RFOperations.RFO_Accounts.AccountContacts a 
 JOIN Hybris.dbo.Users d ON d.p_rfAccountID = CAST(a.AccountID AS VARCHAR)
 JOIN RFOperations.RFO_Accounts.AccountContactAddresses b ON a.AccountContactID =b.AccountContactID 
 JOIN RFOperations.RFO_Accounts.Addresses c ON b.AddressID =c.AddressID
WHERE a.AccountID =765049

--964719
********************************************************************************************************/	



IF OBJECT_ID('TEMPDB.dbo.#Address_Dups') IS NOT NULL
    DROP TABLE #Address_Dups; 

SELECT  AddressID ,
        COUNT(*) AS AddressDups
INTO    #Address_Dups
FROM    Hybris.dbo.addresses a
        JOIN Hybris.dbo.users c ON c.PK = a.OwnerPkString
        JOIN RFOperations.RFO_Accounts.Addresses b ON a.p_rfaddressid = b.AddressID
                                                      AND b.AddressTypeID IN (
                                                      1, 2 )
WHERE   CountryID = 236
GROUP BY AddressID
HAVING  COUNT(*) > 1; 

SELECT  *
FROM    #Address_Dups;


/*
/*=================================================================================================
-- Part 2 EXCEPTS TESTS 
================================================================================================= */


-----------------------------------------------------------------------------------------------
IF OBJECT_ID('TEMPDB.dbo.#Addresses') IS NOT NULL
    DROP TABLE #Addresses;
IF OBJECT_ID('TEMPDB.dbo.#RFO_Addresses') IS NOT NULL
    DROP TABLE #RFO_Addresses;
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Addresses') IS NOT NULL
    DROP TABLE #Hybris_Addresses;



DECLARE @RFOCountry INT = ( SELECT  CountryID
                            FROM    RFOperations.RFO_Reference.Countries
                            WHERE   Alpha2Code = 'US'
                          ) ,
    @HybCountry BIGINT = ( SELECT   PK
                           FROM     Hybris.dbo.countries
                           WHERE    isocode = 'US'
                         );


-----------------------------------------------------------------------------------------------



SELECT DISTINCT
        CAST(AC.AccountId AS NVARCHAR(100)) AS AccountID 	 				--p_rfAccountID
        ,
        CAST (PK AS NVARCHAR) AS Hybris_Owner ,
        CAST(A.AddressID AS NVARCHAR(100)) AS AddressID 					--p_rfaddressid
        ,
        CAST(ISNULL(LTRIM(RTRIM(P.PhoneNumberRaw)),
                    LTRIM(RTRIM(ContPH.PhoneNumberRaw))) AS NVARCHAR(100)) AS PhoneNumberRaw ,
        CAST (LTRIM(RTRIM(AC.FirstName)) AS NVARCHAR(100)) AS FirstName                   --p_firstname
        ,
        CAST (LTRIM(RTRIM(EA.EmailAddress)) AS NVARCHAR(100)) AS EmailAddress			--p_email
        ,
        CAST(A.Locale AS NVARCHAR(100)) AS Locale 					--p_town
        ,
        CAST(LTRIM(RTRIM(A.AddressLine1)) AS NVARCHAR(100)) AS AddressLine1		--p_streetname
        ,
        CAST(LTRIM(RTRIM(AC.LastName)) AS NVARCHAR(100)) AS LastName				--p_lastname
        ,
        CAST(LTRIM(RTRIM(AC.MiddleNAme)) AS NVARCHAR(100)) AS MiddleName				--p_middlename
        ,
        CAST(A.PostalCode AS NVARCHAR(100)) AS PostalCode				--p_postalcode
        ,
        CASE WHEN Birthday = 'Jan  1 1900 12:00AM' THEN ''
             ELSE CAST (AC.Birthday AS NVARCHAR(100))
        END AS Birthday					--,p_dateofbirth
        ,
        CAST(LTRIM(RTRIM(A.AddressLine2)) AS NVARCHAR(100)) AS AddressLine2			--p_streetnumber
        ,
        CASE WHEN AC.GenderId = 1 THEN CAST('Female' AS NVARCHAR(100))						--p_Gender
             WHEN AC.GenderId = 2 THEN CAST('Male' AS NVARCHAR(100))
             ELSE NULL
        END AS Gender ,
        CASE WHEN LEN(A.Region) > 2 THEN CAST (R.CODE AS NVARCHAR(100))
             ELSE CAST (A.Region AS NVARCHAR(100))
        END AS Region		-- RegionPk 
        ,
        CASE WHEN CountryID = 236 THEN CAST('US' AS NVARCHAR(100))
             WHEN CountryID = 40 THEN CAST('CA' AS NVARCHAR(100))
             ELSE NULL
        END AS Country ,
        CASE WHEN AddressTypeID = 3 THEN CAST (1 AS NVARCHAR(100))
             ELSE CAST (0 AS NVARCHAR(100))
        END AS p_BillingAddress ,
        CASE WHEN AddressTypeID = 2 THEN CAST (1 AS NVARCHAR(100))
             ELSE CAST (0 AS NVARCHAR(100))
        END AS p_ShippingAddress ,
        CASE WHEN AddressTypeID = 1 THEN CAST (1 AS NVARCHAR(100))
             ELSE CAST (0 AS NVARCHAR(100))
        END AS p_ContactAddress
INTO    #RFO_Addresses
FROM    RFOperations.RFO_Accounts.Addresses (NOLOCK) A
        JOIN RFOperations.RFO_Accounts.AccountContactAddresses (NOLOCK) ACA ON A.AddressID = ACA.AddressID
                                                              AND A.AddressTypeID NOT IN (
                                                              3 )--,5)
        JOIN RFOperations.RFO_Accounts.AccountContacts (NOLOCK) AC ON ACA.AccountContactId = AC.AccountContactId
        JOIN Hybris.dbo.users (NOLOCK) u ON u.p_rfaccountid = AC.AccountId
        JOIN RFOperations.RFO_Accounts.AccountEmails (NOLOCK) AE ON AE.AccountContactId = AC.AccountContactId
        JOIN RFOperations.RFO_Accounts.EmailAddresses (NOLOCK) EA ON EA.EmailAddressID = AE.EmailAddressId
                                                              AND EmailAddressTypeID = 1
        JOIN RFOperations.RFO_Accounts.AccountContactPhones (NOLOCK) APH ON APH.AccountContactId = AC.AccountContactId
        JOIN RFOperations.RFO_Accounts.Phones (NOLOCK) ContPH ON ContPH.PhoneID = APH.PhoneId
                                                              AND PhoneTypeID = 1
        LEFT JOIN RFOperations.RFO_Accounts.AddressPhones (NOLOCK) AP ON AP.AddressId = A.AddressID
        LEFT JOIN RFOperations.RFO_Accounts.Phones (NOLOCK) P ON P.PhoneID = AP.PhoneId -- AND PhoneTypeID IN (1,8,9)
        LEFT JOIN DataMigration.Migration.RegionMapping R ON R.Region = A.Region
WHERE   A.CountryID = @RFOCountry
        AND EXISTS ( SELECT 1
                     FROM   Hybris.dbo.addresses (NOLOCK) HA
                            JOIN Hybris.dbo.users (NOLOCK) HU ON HU.PK = HA.OwnerPkString
                     WHERE  HA.p_rfaddressid = CAST(A.AddressID AS VARCHAR) )
        AND u.p_sourcename = 'hybris-dm';




SELECT  CAST (p_rfaccountid AS NVARCHAR(100)) AS p_rfAccountID
,CAST (ha.OwnerPKString AS NVARCHAR) AS OwnerPKString
        ,
        CAST (HA.p_rfaddressid AS NVARCHAR(100)) AS p_rfaddressid ,
        CAST (p_phone1 AS NVARCHAR(100)) AS p_phone1 ,
        CAST (HA.p_firstname AS NVARCHAR(100)) AS p_firstname ,
        CAST (p_email AS NVARCHAR(100)) AS p_email ,
        CAST (p_town AS NVARCHAR(100)) AS p_town ,
        CAST (p_streetname AS NVARCHAR(100)) AS p_streetname ,
        CAST (HA.p_lastname AS NVARCHAR(100)) AS p_lastname ,
        CAST (p_middlename AS NVARCHAR(100)) AS p_middlename ,
        CAST (p_postalcode AS NVARCHAR(100)) AS p_postalcode ,
        CAST (HA.p_dateofbirth AS NVARCHAR(100)) AS p_dateofbirth ,
        CAST (p_streetnumber AS NVARCHAR(100)) AS p_streetnumber ,
        CASE WHEN HA.p_gender = 8796093874267
             THEN CAST ('Female' AS NVARCHAR(100))
             WHEN HA.p_gender = 8796093841499
             THEN CAST ('Male' AS NVARCHAR(100))
        END AS p_Gender ,
        CAST (R.isocode AS NVARCHAR(100)) AS regionpk ,
        CASE WHEN HA.countrypk = 8796100624418
             THEN CAST ('US' AS NVARCHAR(100))
             WHEN HA.countrypk = 8796094300194
             THEN CAST ('CA' AS NVARCHAR(100))
             ELSE NULL
        END AS Countrypk ,
        CAST (p_billingaddress AS NVARCHAR(100)) AS p_billingaddress ,
        CAST (p_shippingaddress AS NVARCHAR(100)) AS p_shippingaddress ,
        CAST (p_contactaddress AS NVARCHAR(100)) AS p_contactaddress
INTO    #Hybris_Addresses
FROM    Hybris.dbo.addresses (NOLOCK) HA 
        JOIN Hybris.dbo.users (NOLOCK) HU ON HU.PK = HA.OwnerPkString
        LEFT JOIN Hybris.dbo.regions (NOLOCK) R ON R.PK = HA.regionpk
WHERE   HA.countrypk = @HybCountry
        AND EXISTS ( SELECT 1
                     FROM   RFOperations.RFO_Accounts.Addresses (NOLOCK) A
                     WHERE  CAST(A.AddressID AS VARCHAR) = HA.p_rfaddressid );

CREATE CLUSTERED INDEX MIX_AddressID ON #RFO_Addresses (AddressID);
CREATE CLUSTERED INDEX MIX_rfAddressID ON #Hybris_Addresses (p_rfaddressid);


SELECT  *
INTO    #Addresses
FROM    #RFO_Addresses
EXCEPT
SELECT  *
FROM    #Hybris_Addresses;



SELECT  'RFOADDRESSES' ,
        COUNT(*)
FROM    #RFO_Addresses;  

SELECT  'Hybris_ADDRESSES' ,
        COUNT(*)
FROM    #Hybris_Addresses; 

SELECT  'Excepts' ,
        COUNT(*)
FROM    #Addresses;


CREATE CLUSTERED INDEX MIX_AddressID1 ON #Addresses (AddressID);

*/

/*
/*=================================================================================================
-- Part 3 Column to Column Comparisons 
================================================================================================= */
TRUNCATE TABLE  DataMigration.Migration.ErrorLog_Accounts;


DECLARE @I INT = ( SELECT   MIN(MapID)
                   FROM     DataMigration.Migration.Metadata_Accounts
                   WHERE    HybrisObject = 'Addresses'
                 ) ,
    @C INT = ( SELECT   MAX(MapID)
               FROM     DataMigration.Migration.Metadata_Accounts
               WHERE    HybrisObject = 'Addresses'
             ); 
DECLARE @LastRun DATETIME = '01/01/1900';


DECLARE @DesKey NVARCHAR(50); 

DECLARE @SrcKey NVARCHAR(50); 

DECLARE @Skip BIT; 

WHILE ( @I <= @C )
    BEGIN 

        SELECT  @Skip = ( SELECT    Skip
                          FROM      DataMigration.Migration.Metadata_Accounts
                          WHERE     MapID = @I
                        );


        IF ( @Skip = 1 )
            SET @I = @I + 1;

        ELSE
            BEGIN 



                DECLARE @SrcCol NVARCHAR(50) = ( SELECT RFO_Column
                                                 FROM   DataMigration.Migration.Metadata_Accounts
                                                 WHERE  MapID = @I
                                               );

                DECLARE @DesTemp NVARCHAR(50) = ( SELECT    CASE
                                                              WHEN HybrisObject = 'Users'
                                                              THEN '#Hybris_Accounts'
                                                              WHEN HybrisObject = 'Addresses'
                                                              THEN '#Hybris_Addresses'
                                                              WHEN HybrisObject = 'PaymentInfos'
                                                              THEN '#Hybris_PayInfo'
                                                            END
                                                  FROM      DataMigration.Migration.Metadata_Accounts
                                                  WHERE     MapID = @I
                                                ); 

                DECLARE @DesCol NVARCHAR(50) = ( SELECT Hybris_Column
                                                 FROM   DataMigration.Migration.Metadata_Accounts
                                                 WHERE  MapID = @I
                                               );

                SET @SrcKey = ( SELECT  RFO_Key
                                FROM    DataMigration.Migration.Metadata_Accounts
                                WHERE   MapID = @I
                              );

                SET @DesKey = ( SELECT  CASE WHEN HybrisObject = 'Users'
                                             THEN 'p_rfAccountID'
                                             WHEN HybrisObject = 'Addresses'
                                             THEN 'p_rfAddressID'
                                             WHEN HybrisObject = 'PaymentInfos'
                                             THEN 'p_rfaccountPaymentMethodID'
                                        END
                                FROM    DataMigration.Migration.Metadata_Accounts
                                WHERE   MapID = @I
                              ); 


                DECLARE @SQL1 NVARCHAR(MAX) = ( SELECT  sqlstmt
                                                FROM    DataMigration.Migration.Metadata_Accounts
                                                WHERE   MapID = @I
                                              );
                DECLARE @SQL2 NVARCHAR(MAX) = ' 
 UPDATE A 
SET a.Hybris_Value = b. ' + @DesCol
                    + ' FROM DataMigration.Migration.ErrorLog_Accounts a  JOIN '
                    + @DesTemp + ' b  ON a.RecordID= b.' + @DesKey
                    + ' WHERE a.MAPID = ' + CAST(@I AS NVARCHAR);



                DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
                    ' INSERT INTO DataMigration.Migration.ErrorLog_Accounts (Identifier,MapID,RecordID,RFO_Value) '
                    + @SQL1 + @SQL2;

                BEGIN TRY
                    EXEC sp_executesql @SQL3, N'@ServerMod DATETIME',
                        @ServerMod = @LastRun;

                    SET @I = @I + 1;

                END TRY

                BEGIN CATCH

                    SELECT  @SQL3;

                    SET @I = @I + 1;

                END CATCH;
            END; 

    END; 


SELECT  B.MapID ,
        B.RFO_Column ,
        COUNT(*) AS Counts
FROM    DataMigration.Migration.ErrorLog_Accounts A
        JOIN DataMigration.Migration.Metadata_Accounts B ON A.MapID = B.MapID
GROUP BY B.MapID ,
        RFO_Column;


SELECT  MapID ,
        RecordID ,
        '[' + RFO_Value + ']' ,
        '[' + Hybris_Value + ']'
FROM    DataMigration.Migration.ErrorLog_Accounts
WHERE   MapID = 6;


DROP INDEX MIX_AddressID ON #RFO_Addresses; 
DROP INDEX MIX_rfAddressID ON #Hybris_Addresses; 
DROP INDEX MIX_AddressID1 ON #Addresses;



*/
