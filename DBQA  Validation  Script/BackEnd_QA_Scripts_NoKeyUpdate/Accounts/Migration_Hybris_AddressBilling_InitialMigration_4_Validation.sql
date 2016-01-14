USE [DataMigration]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO 

SET NOCOUNT ON
SET ANSI_WARNINGS OFF 

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED




/*=================================================================================================
-- Part 1: Counts, Missing Keys, Duplicates 
================================================================================================= */


IF OBJECT_ID('DataMigration.Migration.Addresses_Billing_Missing') IS NOT NULL
    DROP TABLE DataMigration.Migration.Addresses_Billing_Missing 

IF OBJECT_ID('TEMPDB.dbo.#Addresses_Dups') IS NOT NULL
    DROP TABLE #Addresses_Dups 


SET ANSI_WARNINGS OFF 

DECLARE @Country NVARCHAR(20)= 'US' ,
    @LastRun DATETIME = '05/01/1901'
DECLARE @RFOCountry INT = ( SELECT  CountryID
                            FROM    RFOperations.RFO_Reference.Countries (NOLOCK)
                            WHERE   Alpha2Code = @Country
                          ) ,
    @RowCount BIGINT ,
    @HybCountry BIGINT = ( SELECT   PK
                           FROM     Hybris.dbo.countries (NOLOCK)
                           WHERE    isocode = @Country
                         );

-------------------------------------------------------------------------------------------------------------
-- Counts 
-------------------------------------------------------------------------------------------------------------


DECLARE @RFOAddress BIGINT ,
    @HYBAddress BIGINT

SELECT  @RFOAddress = COUNT(DISTINCT b.AddressID)
FROM    RFOperations.RFO_Accounts.AccountContacts a
        JOIN Hybris.dbo.users u ON CAST(a.AccountId AS NVARCHAR) = u.p_rfaccountid
        JOIN RFOperations.RFO_Accounts.AccountContactAddresses b ON a.AccountContactId = b.AccountContactId
        JOIN RFOperations.RFO_Accounts.Addresses c ON b.AddressID = c.AddressID
                                                      AND AddressTypeID = 3
        JOIN RFOperations.RFO_Accounts.CreditCardProfiles ccp ON ccp.BillingAddressID = c.AddressID
        JOIN RodanFieldsLive.dbo.AccountPaymentMethods apm ON apm.AccountPaymentMethodID = ccp.PaymentProfileID
WHERE   CountryID = @RFOCountry
        AND u.p_sourcename = 'Hybris-DM'



SELECT  @HYBAddress = COUNT(DISTINCT p_rfaddressid)
FROM    ( SELECT    a.PK ,
                    a.p_rfaddressid
          FROM      Hybris.dbo.addresses (NOLOCK) a
                    JOIN Hybris.dbo.paymentinfos b ON a.OwnerPkString = b.PK
                    JOIN Hybris.dbo.users u ON b.OwnerPkString = u.PK
          WHERE     countrypk = @HybCountry
                    AND b.OwnerPkString = b.userpk
                    AND a.p_billingaddress = 1
                    AND u.p_sourcename = 'Hybris-DM'
                    AND a.duplicate = 0
                    AND b.p_sourcename = 'Hybris-DM'
        ) SUB

SELECT  'Addresses' ,
        @RFOAddress AS RFO_Count ,
        @HYBAddress AS Hybris_Count ,
        @RFOAddress - @HYBAddress



/*													STG1
Stat:													
 Time Taken:																					
 RFO Counts:									
 Hybris Counts:											
 Diff:												
 Results:												
*/

-------------------------------------------------------------------------------------------------------------
-- Missing Keys 
-------------------------------------------------------------------------------------------------------------


SELECT  AddressID AS RFO_AddressID ,
        b.p_rfaddressid AS Hybris_rfAddressID ,
        CASE WHEN b.p_rfaddressid IS NULL THEN 'Destination'
             WHEN a.AddressID IS NULL THEN 'Source'
        END AS MissingFROM
INTO    DataMigration.Migration.Addresses_Billing_Missing
FROM    ( SELECT    CAST (c.AddressID AS NVARCHAR) AS AddressID
          FROM      RFOperations.RFO_Accounts.AccountContacts a
                    JOIN Hybris.dbo.users u ON CAST(a.AccountId AS NVARCHAR) = u.p_rfaccountid
                    JOIN RFOperations.RFO_Accounts.AccountContactAddresses b ON a.AccountContactId = b.AccountContactId
                    JOIN RFOperations.RFO_Accounts.Addresses c ON b.AddressID = c.AddressID
                                                              AND AddressTypeID = 3
                    JOIN RFOperations.RFO_Accounts.CreditCardProfiles ccp ON ccp.BillingAddressID = c.AddressID
                    JOIN RodanFieldsLive.dbo.AccountPaymentMethods apm ON apm.AccountPaymentMethodID = ccp.PaymentProfileID
          WHERE     CountryID = @RFOCountry
                    AND u.p_sourcename = 'Hybris-DM'
        ) a
        FULL OUTER JOIN ( SELECT    a.PK ,
                                    a.p_rfaddressid
                          FROM      Hybris.dbo.addresses (NOLOCK) a
                                    JOIN Hybris.dbo.paymentinfos b ON a.OwnerPkString = b.PK
                                    JOIN Hybris.dbo.users u ON b.OwnerPkString = u.PK
                          WHERE     countrypk = @HybCountry
                                    AND b.OwnerPkString = b.userpk
                                    AND a.p_billingaddress = 1
                                    AND a.p_rfaddressid IS NOT NULL
                                    AND u.p_sourcename = 'Hybris-DM'
                        ) b ON b.p_rfaddressid = CAST(a.AddressID AS VARCHAR)
WHERE   ( a.AddressID IS NULL
          OR b.p_rfaddressid IS NULL
        )

--------------------------------------------------------------------------------------------------------------------------
SELECT  AddressID ,
        COUNT(*) AS AddressDups
INTO    #Address_Dups
FROM    Hybris.dbo.addresses a
        JOIN Hybris.dbo.users c ON c.PK = a.OwnerPkString
        JOIN RFOperations.RFO_Accounts.Addresses b ON a.p_rfaddressid = b.AddressID
WHERE   CountryID = 236
        AND b.AddressTypeID = 3
GROUP BY AddressID
HAVING  COUNT(*) > 1 


	/*
/*=================================================================================================
-- Part 2 EXCEPTS TESTS 
================================================================================================= */

---------------------------------------------------------------------------------------------
IF OBJECT_ID('TEMPDB.dbo.#Addresses') IS NOT NULL DROP TABLE #Addresses
IF OBJECT_ID('TEMPDB.dbo.#RFO_Addresses') IS NOT NULL DROP TABLE #RFO_Addresses
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Addresses') IS NOT NULL DROP TABLE #Hybris_Addresses


-----------------------------------------------------------------------------------------------
--DECLARE @RFOCountry INT = (SELECT CountryID  FROM RFOperations.RFO_Reference.Countries WHERE Alpha2Code = 'US'),
--			@HybCountry BIGINT = (SELECT PK FROM Hybris.dbo.Countries WHERE isocode = 'US' );
-----------------------------------------------------------------------------------------------

  SELECT DISTINCT 
CAST(AC.AccountID AS NVARCHAR(100)) as AccountID 	 				--p_rfAccountID
,CAST (u.PK AS NVARCHAR) AS Hybris_Owner
,CAST(A.AddressID	AS NVARCHAR(100)) AS AddressID 					--p_rfaddressid
, CAST(ISNULL (p.PhoneNumberRaw, ContPH.PhoneNumberRaw) AS NVARCHAR(100)) AS PhoneNumberRaw
,CAST (AC.FirstName AS NVARCHAR(100)) AS FirstName                   --p_firstname
,CAST (EA.EmailAddress AS NVARCHAR(100)) AS	EmailAddress			--p_email
,CAST(A.Locale	AS NVARCHAR(100)) AS Locale 					--p_town
,CAST(A.AddressLine1	AS NVARCHAR(100)) AS AddressLine1		--p_streetname
,CAST(AC.LastName AS NVARCHAR(100)) AS	LastName				--p_lastname
,CAST(AC.MiddleName	AS NVARCHAR(100)) AS MiddleName				--p_middlename
,CAST(A.PostalCode	AS NVARCHAR(100)) AS PostalCode				--p_postalcode
, CASE WHEN Birthday = 'Jan  1 1900 12:00AM' THEN NULL 
		ELSE CAST (AC.Birthday AS NVARCHAR (100)) END AS Birthday					--,p_dateofbirth
,CAST(A.AddressLine2	AS NVARCHAR(100)) AS AddressLine2			--p_streetnumber
,CASE WHEN AC.GenderID =1  THEN CAST('Female' AS NVARCHAR(100))						--p_Gender
	  WHEN AC.GenderID =2  THEN CAST('Male' AS NVARCHAR(100))    
	 ELSE NULL END AS Gender	
,CASE WHEN LEN (a.Region)> 2 THEN  CAST (r.code AS NVARCHAR(100)) 
		       ELSE CAST (a.Region AS NVARCHAR(100)) END AS Region		-- RegionPk 
,CASE WHEN CountryID = 236 THEN CAST('US' AS NVARCHAR(100))
	  WHEN CountryID = 40 THEN CAST('CA' AS NVARCHAR(100))
			  ELSE NULL END AS Country
, CASE WHEN AddressTypeID =3 THEN CAST (1 AS NVARCHAR(100)) ELSE  CAST (0 AS NVARCHAR(100)) END AS p_BillingAddress
, CASE WHEN AddressTypeID =2 THEN  CAST (1 AS NVARCHAR(100)) ELSE  CAST (0 AS NVARCHAR(100)) END AS p_ShippingAddress
, CASE WHEN AddressTypeID =1 THEN  CAST (1 AS NVARCHAR(100)) ELSE  CAST (0 AS NVARCHAR(100)) END AS p_ContactAddress
INTO #RFO_Addresses
FROM  RFOperations.RFO_Accounts.Addresses (NOLOCK) a
	JOIN RFOperations.RFO_Accounts.AccountContactAddresses  (NOLOCK) ACA ON A.AddressID = ACA.AddressID AND AddressTypeID = 3
	JOIN RFOPerations.RFO_Accounts.AccountContacts (NOLOCK) AC ON ACA.AccountContactID =AC.AccountContactID 
	JOIN RFOperations.RFO_Accounts.AccountEmails (NOLOCK) AE ON AE.AccountContactID =AC.AccountContactID
	JOIN RFOperations.RFO_Accounts.EmailAddresses (NOLOCK) EA ON EA.EmailAddressID =AE.EmailAddressID  AND EmailAddressTypeID = 1                                                             
	JOIN RFOperations.RFO_Accounts.AccountContactPhones (NOLOCK) APH ON APH.AccountContactID =AC.AccountContactId 
	JOIN RFOperations.RFO_Accounts.Phones (NOLOCK) ContPH ON ContPH.PhoneID = APH.PhoneId AND PhoneTypeID = 1
	JOIN RFOperations.RFO_Accounts.AddressPhones (NOLOCK) AP ON AP.AddressId = A.AddressID 
	JOIN RFOperations.RFO_Accounts.Phones (NOLOCK) P ON P.PhoneID = AP.PhoneId --AND P.IsDefault = 1
	JOIN RFOperations.RFO_Accounts.CreditCardProfiles (NOLOCK) ccp ON ccp.BillingAddressID = a.AddressID AND a.AddressTypeID = 3 
	JOIN RodanFieldsLive.dbo.AccountPaymentMethods (NOLOCK)apm ON apm.AccountPaymentMethodID =ccp.PaymentProfileID 
	JOIN Hybris.dbo.Users (NOLOCK) U ON u.p_rfAccountID = ac.AccountID
	LEFT JOIN DataMigration.Migration.RegionMapping R ON R.Region= a.region 


WHERE A.CountryID =@RFOCountry
AND EXISTS (SELECT 1 
			FROM Hybris.dbo.Addresses (NOLOCK)HA  --JOIN CTE ON HA.PK=CTE.PK
 JOIN Hybris.dbo.PaymentInfos  (NOLOCK) HU ON HU.PK =HA.OwnerPkString
JOIN Hybris.dbo.Users HP ON HP.PK =HU.OwnerPKString
			WHERE Ha.p_rfAddressID=A.AddressID and ha.p_billingaddress = 1)

AND U.p_sourcename = 'Hybris-DM'




SELECT  
CAST (p_rfAccountID AS NVARCHAR (100)) AS p_rfAccountID
,CAST (hU.OwnerPKString AS NVARCHAR) AS OwnerPKString
,CAST (HA.p_rfaddressid AS NVARCHAR (100)) AS p_rfaddressid
,CAST (p_phone1 AS NVARCHAR (100)) AS  p_phone1
,CAST (ha.p_firstname AS NVARCHAR (100)) AS p_firstname
,CAST (p_email AS NVARCHAR (100)) AS p_email
,CAST (p_town AS NVARCHAR (100)) AS p_town
,CAST (p_streetname AS NVARCHAR (100)) AS p_streetname
,CAST (ha.p_lastname AS NVARCHAR (100)) AS p_lastname
,CAST (p_middlename AS NVARCHAR (100)) AS p_middlename
,CAST (p_postalcode AS NVARCHAR (100)) AS p_postalcode
,CAST (HA.p_dateofbirth AS NVARCHAR (100)) AS p_dateofbirth
,CAST (p_streetnumber AS NVARCHAR (100)) AS p_streetnumber
, CASE WHEN  HA.p_gender = 8796093874267	THEN CAST ('Female' AS NVARCHAR (100)) 
	   WHEN  HA.p_gender = 8796093841499	THEN CAST ('Male' AS NVARCHAR (100))  END AS p_Gender
,CAST (r.isocode AS NVARCHAR (100)) AS regionpk
,CASE WHEN HA.countrypk = 8796100624418 THEN CAST ('US' AS NVARCHAR (100)) 
		     WHEN HA.countrypk = 8796094300194 THEN CAST ('CA' AS NVARCHAR (100)) 
			  ELSE NULL END AS Countrypk 
,CAST (ha.p_billingaddress AS NVARCHAR (100))  AS p_billingaddress
,CAST (p_shippingaddress AS NVARCHAR (100))  AS p_shippingaddress
,CAST (p_contactaddress AS NVARCHAR (100)) AS p_contactaddress

INTO #Hybris_Addresses 
FROM Hybris.dbo.Addresses (NOLOCK)HA  --JOIN CTE ON HA.PK=CTE.PK
LEFT JOIN Hybris.dbo.PaymentInfos  (NOLOCK) HU ON HU.PK =HA.OwnerPkString
LEFT JOIN Hybris.dbo.Users HP ON HP.PK =HU.OwnerPKString
--JOIN Hybris.dbo.Countries HC ON HC.PK = HA.CountryPK
LEFT JOIN Hybris.dbo.regions (NOLOCK) R ON R.PK = HA.regionpk

WHERE HA.countrypk = @HybCountry 
AND EXISTS (SELECT 1 
			FROM RFOperations.RFO_Accounts.Addresses (NOLOCK) A
			WHERE A.AddressID =HA.p_rfAddressID ) AND ha.p_billingaddress=1 
			AND hu.userpk =hu.ownerpkstring AND hp.p_sourcename = 'Hybris-DM'


CREATE CLUSTERED INDEX MIX_AddressID ON #RFO_Addresses (AddressID)
CREATE CLUSTERED INDEX MIX_rfAddressID ON #Hybris_Addresses (p_rfaddressid)



SELECT * INTO #Addresses
FROM #RFO_Addresses
EXCEPT
SELECT * FROM #Hybris_Addresses


CREATE CLUSTERED INDEX MIX_AddressID1 ON #Addresses (AddressID)

SELECT 'RFOADDRESSES', COUNT(*) FROM #RFO_ADDRESSES 

SELECT 'Hybris_ADDRESSES',  COUNT(*) FROM #Hybris_ADDRESSES  

SELECT 'Excepts', COUNT(*) FROM #ADDRESSES


SELECT COUNT (*) FROM  #Addresses


/*=================================================================================================
-- Part 3 Column to Column Comparisons 
================================================================================================= */


TRUNCATE TABLE  DataMigration.Migration.ErrorLog_Accounts

DECLARE @LastRUN DATETIME ='05/01/1901'

DECLARE @I INT = (SELECT MIN(MapID) FROM  DataMigration.Migration.Metadata_Accounts WHERE HybrisObject = 'Addresses') , 
@C INT =  (SELECT MAX(MapID) FROM  DataMigration.Migration.Metadata_Accounts WHERE HybrisObject = 'Addresses') 


DECLARE @DesKey NVARCHAR (50) 

DECLARE @SrcKey NVARCHAR (50) 

DECLARE @Skip  BIT 

WHILE (@I <=@c)

BEGIN 

        SELECT  @Skip = ( SELECT   Skip
                               FROM     DataMigration.Migration.Metadata_Accounts
                               WHERE    MapID = @I
                             );


        IF ( @Skip = 1 )

            SET @I = @I + 1;

        ELSE
BEGIN 



DECLARE @SrcCol NVARCHAR (50) =(SELECT RFO_Column FROM DataMigration.Migration.Metadata_Accounts WHERE MapID = @I)

DECLARE @DesTemp NVARCHAR (50) =(SELECT CASE WHEN HybrisObject = 'Users' THEN '#Hybris_Accounts'
										     WHEN HybrisObject = 'Addresses' THEN '#Hybris_Addresses'
										     WHEN HybrisObject = 'PaymentInfos' THEN '#Hybris_PayInfo'
											 END
			  FROM  DataMigration.Migration.Metadata_Accounts 
			  WHERE MapID =@I
								) 

DECLARE @DesCol NVARCHAR (50) =(SELECT Hybris_Column FROM DataMigration.Migration.Metadata_Accounts WHERE MapID = @I)

SET @SrcKey= (SELECT RFO_Key
			  FROM DataMigration.Migration.Metadata_Accounts 
			  WHERE MapID =@I
								)

                SET @DesKey = ( SELECT  CASE WHEN HybrisObject= 'Users'
                                             THEN 'p_rfAccountID'
                                             WHEN HybrisObject = 'Addresses'
                                             THEN 'p_rfAddressID'
                                             WHEN HybrisObject = 'PaymentInfos'
                                             THEN 'p_rfaccountPaymentMethodID'
                                        END
                                FROM    DataMigration.Migration.Metadata_Accounts
                                WHERE   MapID = @I
                              ); 


DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM  DataMigration.Migration.Metadata_Accounts WHERE MapID = @I)
DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.Hybris_Value = b. ' + @DesCol +
' FROM DataMigration.Migration.ErrorLog_Accounts a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.MAPID = ' + CAST(@I AS NVARCHAR)



DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO DataMigration.Migration.ErrorLog_Accounts (Identifier,MapID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

  BEGIN TRY
   EXEC sp_executesql @SQL3, N'@ServerMod DATETIME', @ServerMod= @LastRun

 SET @I = @I + 1

 END TRY

 BEGIN CATCH

 SELECT @SQL3

 SET @I = @I + 1

 END CATCH
END 

END 


SELECT  b.RFO_column, COUNT(*) AS Counts
FROM DataMigration.dbo.ErrorLog_Accounts A JOIN DataMigration.dbo.Metadata_Accounts B ON a.MapID =b.MapID
GROUP BY b.MapID, RFO_Column

SELECT MapID, RecordID, '['+ RFO_Value + ']', '['+ Hybris_Value + ']' FROM DataMigration.Migration.ErrorLog_Accounts
WHERE MapID =6


drop index MIX_AddressID ON #RFO_Addresses 
drop index MIX_rfAddressID ON #Hybris_Addresses 
drop index MIX_AddressID1 ON #Addresses






