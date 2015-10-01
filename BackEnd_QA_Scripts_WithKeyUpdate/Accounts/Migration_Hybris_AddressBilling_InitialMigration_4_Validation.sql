USE [DataMigration]
GO
/****** Object:  StoredProcedure [Migration].[Migration_Hybris_AddressBilling_InitialMigration_4_Validation]    Script Date: 7/13/2015 10:26:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [Migration].[Migration_Hybris_Address_Billing_InitialMigration_4_Validation] @LastRun DATETIME = '05/01/1901'
WITH RECOMPILE
AS 
BEGIN 

SET NOCOUNT ON
SET ANSI_WARNINGS OFF 

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED

IF OBJECT_ID('TEMPDB.dbo.#Addresses_Missing') IS NOT NULL 
DROP TABLE #Addresses_Missing 

IF OBJECT_ID('DataMigration.Migration.Addresses_Billing_Missing') IS NOT NULL 
DROP TABLE DataMigration.Migration.Addresses_Billing_Missing 


IF OBJECT_ID('TEMPDB.dbo.#Addresses_Dups') IS NOT NULL 
DROP TABLE #Addresses_Dups 

IF OBJECT_ID ('TEMPDB.dbo.#AccountIDs') IS NOT NULL 
DROP TABLE #AccountIDs

SET ANSI_WARNINGS OFF 
DECLARE @Country NVARCHAR(20)= 'US'
DECLARE @ServerMod DATETIME ='05/01/1901'
DECLARE @RFOCountry INT = (SELECT CountryID  FROM RFOperations.RFO_Reference.Countries (NOLOCK) WHERE Alpha2Code = @Country),  @RowCount BIGINT ,
		@HybCountry BIGINT = (SELECT PK FROM Hybris.dbo.Countries (NOLOCK) WHERE isocode =  @Country );

------------------------------------------------------------------------------------------------------------------------------
-- Addresses
-----------------------------------------------------------------------------------------------------------------------------

SELECT   DISTINCT a.AccountID INTO #AccountIDs --COUNT( DISTINCT a.AccountID)
FROM RFOperations.RFO_Accounts.AccountRF (NOLOCK)a 
JOIN RFOperations.RFO_Accounts.AccountBase (NOLOCK)  b ON a.AccountID =b.AccountID 
 JOIN RFOperations.RFO_Accounts.AccountContacts  (NOLOCK) d ON b.AccountID =d.AccountID 
 JOIN RFOperations.RFO_Accounts.AccountEmails (NOLOCK) e ON e.AccountContactID = D.AccountContactID 
 JOIN RFOperations.RFO_Accounts.AccountContactAddresses  (NOLOCK) g ON g.AccountContactID = d.AccountContactID 
 JOIN RFOperations.RFO_Accounts.AccountContactPhones  (NOLOCK)j ON j.AccountContactID = d.AccountContactID 
 LEFT JOIN RFOperations.RFO_Accounts.Phones  (NOLOCK) p ON j.PhoneID =p.PhoneID AND p.PhoneTypeID = 1 
 LEFT JOIN RFOperations.RFO_Accounts.Addresses  (NOLOCK) i ON i.AddressID =g.AddressID AND i.AddressTypeID =1 AND i.IsDefault= 1 
 LEFT JOIN RFOperations.RFO_Accounts.EmailAddresses (NOLOCK)  f ON f.EmailAddressID =E.EmailAddressId AND EmailAddressTypeID =1 
 LEFT JOIN RFOperations.Security.AccountSecurity  (NOLOCK) k ON k.AccountID =a.AccountID 
LEFT JOIN 

(SELECT DISTINCT AccountID
FROM RFoperations.Hybris.Orders  (NOLOCK)  d 
JOIN RFOperations.etl.OrderDate  (NOLOCK) od ON d.OrderID =od.OrderID 
WHERE od.StartDate >=  DATEADD (Month, -18, GETDATE())
)  c ON b.AccountID =c.AccountID 

WHERE 1=1 
AND b.CountryID =@RFOCountry 
AND (SoftTerminationDate IS NULL OR SoftTerminationDate >= '2014-05-01') 
AND (b.AccountStatusID <>3 OR  c.AccountID IS NOT NULL ) 
AND f.EmailAddressID IS NOT NULL 
AND i.AddressID IS NOT NULL 
AND p.PhoneID IS NOT NULL 
AND k.AccountID IS NOT NULL 
--AND a.AccountID IN (SELECT p_rfAccountID FROM Hybris.dbo.Users)
--1,084,566 Rows 

--SELECT  DATEADD (Month, -18, GETDATE())
----------------------------------------------------------------------------------------------

IF OBJECT_ID('TEMPDB.dbo.#Addresses_Missing') IS NOT NULL 
DROP TABLE #Addresses_Missing


DECLARE @RFOAddress BIGINT,  @HYBAddress BIGINT

SELECT @RFOAddress =COUNT ( *) FROM RFOperations.RFO_Accounts.AccountContacts a 
 JOIN RFOperations.RFO_Accounts.AccountContactAddresses b ON a.AccountContactID =b.AccountContactID 
 JOIN RFOperations.RFO_Accounts.Addresses c ON b.AddressID =c.AddressID AND AddressTypeID = 3 
 JOIN RFOPerations.RFO_Accounts.CreditCardProfiles ccp ON ccp.BillingAddressID =c.AddressID 
 JOIN RodanFieldsLive.dbo.AccountPaymentMethods apm ON apm.AccountPaymentMethodID = ccp.PaymentProfileID 

 WHERE a.AccountID IN (SELECT AccountID FROM #AccountIDs) AND CountryID =@RFOCountry 



SELECT @HybAddress = COUNT(*)

 FROM 
 ( 
SELECT a.PK FROM Hybris.dbo.Addresses (NOLOCK) a JOIN Hybris.dbo.PaymentInfos b ON a.OwnerPkString = b.PK
WHERE countrypk =@HybCountry  AND b.OwnerPKString =b.userPK AND a.p_billingaddress= 1 AND a.p_rfaddressid IS NOT NULL
) SUB

SELECT 'Addresses',  @RFOAddress AS RFO_Count, @HYBAddress AS Hybris_Count



-------------------------------------------------------------------------------------------------------------
-- Missing Keys 
-------------------------------------------------------------------------------------------------------------


SELECT AddressID AS RFO_AddressID,
 b.p_rfAddressid AS Hybris_rfAddressID , CASE WHEN b.p_rfAddressid IS NULL THEN 'Destination'
											  WHEN a.AddressID IS NULL THEN 'Source' END AS MissingFROM
INTO DataMigration.Migration.Addresses_Billing_Missing
FROM 
(SELECT CAST (c.AddressID AS NVARCHAR) AS AddressID FROM RFOperations.RFO_Accounts.AccountContacts a 
 JOIN RFOperations.RFO_Accounts.AccountContactAddresses b ON a.AccountContactID =b.AccountContactID 
 JOIN RFOperations.RFO_Accounts.Addresses c ON b.AddressID =c.AddressID AND AddressTypeID = 3 
 JOIN RFoperations.RFO_Accounts.CreditCardProfiles ccp ON ccp.BillingAddressID =c.AddressID 
 JOIN RodanFieldsLive.dbo.AccountPaymentMethods apm ON apm.AccountPaymentMethodID = ccp.PaymentProfileID 

 WHERE a.AccountID IN (SELECT AccountID FROM #AccountIDs) AND CountryID =@RFOCountry )  a

 FULL OUTER JOIN 
 
 --(SELECT DISTINCT p_rfAddressID FROM Hybris.dbo.Addresses (NOLOCK) a JOIN Hybris.dbo.users c ON a.OwnerPkString = c.PK
 --WHERE CountryPk = @HybCountry AND p_rfaddressid IS NOT NULL
 
( 
SELECT a.PK,a.p_rfaddressID FROM Hybris.dbo.Addresses (NOLOCK) a JOIN Hybris.dbo.PaymentInfos b ON a.OwnerPkString = b.PK
WHERE countrypk =@HybCountry  AND a.p_billingaddress =1 AND b.OwnerPKString =b.userPK 

 )  b 
 
 ON  b.p_rfAddressid = a.AddressID 

WHERE (a.AddressID IS NULL OR b.p_rfAddressid IS NULL)

/*

SELECT @RowCount = COUNT(*) FROM #Addresses_Missing

IF @RowCount > 0
BEGIN 

SELECT 'Total ' + @Country +' Addresses Missing', @ROWCOUNT

SELECT MissingFrom, COUNT(*)
FROM #Addresses_Missing
GROUP BY MissingFROM 

--SELECT AddressTypeID, COUNT (*) FROM RFO_Accounts.Addresses 
--WHERE AddressID IN (SELECT RFO_AddressID FROM #Addresses_Missing)
--GROUP BY AddressTypeID 

SELECT * FROM #Addresses_Missing
ORDER BY MissingFrom

END  

IF OBJECT_ID('TEMPDB.dbo.#Address_Dups') IS NOT NULL 
DROP TABLE #Address_Dups 

*/

SELECT AddressID , COUNT(*) AS AddressDups
INTO #Address_Dups
FROM Hybris.dbo.Addresses a JOIN Hybris.dbo.Users c ON c.PK =  a.OwnerPkString
JOIN RFOperations.RFO_Accounts.Addresses b ON a.p_rfaddressid = b.AddressID
 WHERE CountryID =@RFOCountry
GROUP BY AddressID 
HAVING COUNT(*) > 1 

SELECT @RowCount = COUNT(*) FROM #Address_Dups

IF @RowCount > 0
BEGIN 

SELECT  'Duplicate ' + @Country+' Addresses in Hybris' , @ROWCOUNT

 
SELECT * FROM #Address_Dups

END  





---------------------------------------------------------------------------------------------

-- Billing Addresses Framework 


---------------------------------------------------------------------------------------------
IF OBJECT_ID('TEMPDB.dbo.#Accounts') IS NOT NULL DROP TABLE #Accounts 
IF OBJECT_ID('TEMPDB.dbo.#ExceptReport') IS NOT NULL DROP TABLE #ExceptReport 
IF OBJECT_ID('TEMPDB.dbo.#Column_Excepts') IS NOT NULL DROP TABLE #Column_Excepts 
IF OBJECT_ID('TEMPDB.dbo.#Addresses') IS NOT NULL DROP TABLE #Addresses
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Accounts') IS NOT NULL DROP TABLE #Hybris_Accounts
IF OBJECT_ID('TEMPDB.dbo.#RFO_Accounts') IS NOT NULL DROP TABLE #RFO_Accounts
IF OBJECT_ID('TEMPDB.dbo.#RFO_Addresses') IS NOT NULL DROP TABLE #RFO_Addresses
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Addresses') IS NOT NULL DROP TABLE #Hybris_Addresses
IF OBJECT_ID('TEMPDB.dbo.#RFO_PayInfo') IS NOT NULL DROP TABLE #RFO_PayInfo
IF OBJECT_ID('TEMPDB.dbo.#Hybris_PayInfo') IS NOT NULL DROP TABLE #Hybris_PayInfo
IF OBJECT_ID('TEMPDB.dbo.#PayInfo') IS NOT NULL DROP TABLE #PayInfo

TRUNCATE TABLE  DataMigration.Migration.ErrorLog_Accounts

-----------------------------------------------------------------------------------------------


--DECLARE @RFOCountry INT = (SELECT CountryID  FROM RFOperations.RFO_Reference.Countries WHERE Alpha2Code = 'US'),
--			@HybCountry BIGINT = (SELECT PK FROM Hybris.dbo.Countries WHERE isocode = 'US' );

--DECLARE @LastRUN DATETIME ='05/01/1901'


----------------------------------------------------------------------------------------------------------------------

--- Load Accounts Excepts 
---------------------------------------------------------------------------------------------------------------------


  SELECT DISTINCT 
CAST(AC.AccountID AS NVARCHAR(100)) as AccountID 	 				--p_rfAccountID
,CAST (u.PK AS NVARCHAR) AS Hybris_Owner
--, CAST (pf.PayInfo AS NVARCHAR(100)) AS Hybris_PayInfo
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

AND a.ServerModifiedDate > @LastRun


--;WITH CTE AS 
--(
--SELECT  MAX(PK) AS PK
--FROM Hybris.dbo.Addresses
--GROUP BY p_rfaddressid
--)


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
			AND hu.userpk =hu.ownerpkstring 
--AND HA.CreatedTS >  @LastRun



CREATE CLUSTERED INDEX MIX_AddressID ON #RFO_Addresses (AddressID)
CREATE CLUSTERED INDEX MIX_rfAddressID ON #Hybris_Addresses (p_rfaddressid)



SELECT * INTO #Addresses
FROM #RFO_Addresses
EXCEPT
SELECT * FROM #Hybris_Addresses


--INSERT INTO  #ExceptReport
--VALUES ('Addresses',@@ROWCOUNT)

CREATE CLUSTERED INDEX MIX_AddressID1 ON #Addresses (AddressID)

SELECT 'RFOADDRESSES', COUNT(*) FROM #RFO_ADDRESSES  ---252,354

SELECT 'Hybris_ADDRESSES',  COUNT(*) FROM #Hybris_ADDRESSES  ---252,354

SELECT 'Excepts', COUNT(*) FROM #ADDRESSES---252,354

--SELECT 'Excepts_rev', COUNT(*)  FROM #ADDRESSES_rev



--SELECT * FROM  #Addresses


----------------------------------------------------------------------------------------------------------------------------
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
FROM RFoperations.dbo.ErrorLog_Accounts A JOIN RFOperations.dbo.Metadata_Accounts B ON a.MapID =b.MapID
GROUP BY b.MapID, RFO_Column



drop index MIX_AddressID ON #RFO_Addresses 
drop index MIX_rfAddressID ON #Hybris_Addresses 
drop index MIX_AddressID1 ON #Addresses



END 




