USE [RFOperations]
GO

/****** Object:  StoredProcedure [dbo].[VerifyContactsMigration]    Script Date: 10/27/2015 6:45:14 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO




CREATE PROCEDURE [sfdc].[VerifyContactsMigration]
AS
BEGIN

IF OBJECT_ID('Rfoperations.dbo.ContactMissing') IS NOT NULL  DROP TABLE Rfoperations.dbo.ContactMissing
IF OBJECT_ID('TEMPDB.dbo.#Contacts') IS NOT NULL DROP TABLE #Contacts 
IF OBJECT_ID('Rfoperations.sfdc.RFO_Contacts') IS NOT NULL DROP TABLE Rfoperations.sfdc.RFO_Contacts
IF OBJECT_ID('Rfoperations.sfdc.crm_Contacts') IS NOT NULL DROP TABLE Rfoperations.sfdc.crm_Contacts
IF OBJECT_ID('rfoperations.sfdc.ErrorLog_Contacts') IS NOT NULL DROP TABLE rfoperations.sfdc.ErrorLog_Contacts
IF OBJECT_ID('rfoperations.sfdc.ContactDifference') IS NOT NULL DROP TABLE rfoperations.sfdc.ContactDifference
IF OBJECT_ID('rfoperations.sfdc.MissingCoApplicants') IS NOT NULL DROP TABLE rfoperations.sfdc.MissingCoApplicants
IF OBJECT_ID ('Rfoperations.dbo.AccountIDs') IS NOT NULL  DROP TABLE Rfoperations.dbo.AccountIDs

--IF OBJECT_ID('Rfoperations.sfdc.ErrorLog_Accounts') IS NOT NULL DROP TABLE Rfoperations.sfdc.ErrorLog_Accounts
--IF OBJECT_ID('rfoperations.sfdc.BusinessRuleFailure') IS NOT NULL DROP TABLE rfoperations.sfdc.BusinessRuleFailure
--IF OBJECT_ID('rfoperations.sfdc.AccountDifference') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountDifference


SELECT ' Refer Following tables to view test results.
		1. Rfoperations.dbo.ContactDifference--> Difference in RFO and CRM Accounts
		2. Rfoperations.dbo.ContactMissing --> List of Missing AccountIDs from Source and Destination.
		3. Rfoperations.dbo.Accounts_Dups --> List of Duplicate Account IDs in CRM.
		4. Rfoperations.dbo.MissingCoApplicants --> List of CoApplicants that did not get created in CRM.
		5. Rfoperations.sfdc.ErrorLog_Contacts --> List of All failures due to field mismatch. This table also provided RFO and CRM side values
		6. Rfoperations.sfdc.CRM_METADATA --> Mapping of RFO and CRM fields to be compared. Use this table in conjunction with above table to generate final test result'
		

SET ANSI_WARNINGS OFF 


DECLARE @ServerMod DATETIME =@LastRunDate
DECLARE @RFOAccount BIGINT, @CRMAccount BIGINT


DECLARE @RowCount BIGINT 

SELECT DISTINCT a.AccountID INTO Rfoperations.dbo.AccountIDs --COUNT( DISTINCT a.AccountID)
FROM RFOperations.RFO_Accounts.AccountRF (NOLOCK)a 
JOIN RFOperations.RFO_Accounts.AccountBase (NOLOCK)  b ON a.AccountID =b.AccountID  AND b.CountryID =40
 JOIN RFOperations.RFO_Accounts.AccountContacts  (NOLOCK) d ON b.AccountID =d.AccountID 
 JOIN RFOperations.RFO_Accounts.AccountEmails (NOLOCK) e ON e.AccountContactID = D.AccountContactID 
 JOIN RFOperations.RFO_Accounts.AccountContactAddresses  (NOLOCK) g ON g.AccountContactID = d.AccountContactID 
 JOIN RFOperations.RFO_Accounts.AccountContactPhones  (NOLOCK)j ON j.AccountContactID = d.AccountContactID 
 LEFT JOIN RFOperations.RFO_Accounts.Phones  (NOLOCK) p ON j.PhoneID =p.PhoneID AND p.PhoneTypeID = 1 
 LEFT JOIN RFOperations.RFO_Accounts.Addresses  (NOLOCK) i ON i.AddressID =g.AddressID AND i.AddressTypeID =1 AND i.IsDefault= 1 
 LEFT JOIN RFOperations.RFO_Accounts.EmailAddresses (NOLOCK)  f ON f.EmailAddressID =E.EmailAddressId AND EmailAddressTypeID =1 
 AND B.ServerModifiedDate>=@LastRunDate



--Compare Primary Account Count
SELECT @RFOAccount =COUNT( DISTINCT AccountID) FROM RFOPerations.RFO_Accounts.AccountBase (NOLOCK) WHERE AccountID IN (SELECT AccountID FROM Rfoperations.dbo.AccountIDs) AND ServerModifiedDate> @LastRunDate AND countryid=40
SELECT @CRMAccount=COUNT(RFOAccountID__C) FROM sfdcbackup.SFDCbkp.Contact C , sfdcbackup.SFDCbkp.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY CO where A.ID=c.Accountid AND c.ContactType__c='Primary' AND A.COUNTRY__C=CO.ID AND CO.NAME='Canada'

SELECT  @RFOAccount AS RFO_Accounts, @CRMAccount AS Hybris_Accounts, (@RFOAccount - @CRMAccount) AS Difference , 'Primary Contact' as ContactType INTO rfoperations.sfdc.ContactDifference;

--Compare Secondary Applicant Count
SELECT @RFOAccount =COUNT(CoApplicant) FROM RFOPerations.RFO_Accounts.AccountRF (NOLOCK) WHERE AccountID IN (SELECT AccountID FROM Rfoperations.dbo.AccountIDs) AND LEN(COAPPLICANT)>1 AND ServerModifiedDate> @LastRunDate
SELECT @CRMAccount=COUNT(RFOAccountID__C) FROM sfdcbackup.SFDCbkp.Contact C, sfdcbackup.SFDCbkp.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY CO where A.ID=c.Accountid AND c.ContactType__c = 'Spouse'  AND A.COUNTRY__C=CO.ID AND CO.NAME='Canada'
INSERT INTO rfoperations.sfdc.ContactDifference
SELECT  @RFOAccount AS RFO_Accounts, @CRMAccount AS Hybris_Accounts, (@RFOAccount - @CRMAccount) AS Difference, 'Secondary' as ContactType ;



--Load missing Primary Account Contacts
SELECT  AccountContactID AS RFO_AccountContactID,
 b.RFAccountContactID__c AS Hybris_rfAccountID , 
 CASE WHEN b.RFAccountContactID__c IS NULL THEN 'Destination'
      WHEN a.AccountContactID IS NULL THEN 'Source' 
 END AS MissingFROM
INTO Rfoperations.dbo.ContactMissing
FROM 
    (SELECT AccountContactID FROM Rfoperations.dbo.AccountIDs AID , Rfoperations.rfo_accounts.accountcontacts AC where aid.accountid=ac.accountid AND AccountContactTypeId=1) a
    FULL OUTER JOIN 
    (SELECT RFAccountContactID__c FROM sfdcbackup.SFDCbkp.Contact C , sfdcbackup.SFDCbkp.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY CO where A.ID=c.Accountid AND c.ContactType__c='Primary'  AND A.COUNTRY__C=CO.ID AND CO.NAME='Canada') b 
	ON a.AccountContactID =b.RFAccountContactID__c
 WHERE (a.AccountContactID IS NULL OR b.RFAccountContactID__c IS NULL) 

SELECT MissingFrom ,COUNT(*) from Rfoperations.dbo.ContactMissing GROUP BY MISSINGFROM;

SELECT 'Query Rfoperations.dbo.ContactMissing to get list of AccountContactIds missing from Source/Destination'

--------------------------------------------------------------------------------------
-- Missing CoApplicants 
--------------------------------------------------------------------------------------

SELECT ACCOUNTID,REPLACE(COAPPLICANT ,'  ',' ') AS CoApplicant
INTO rfoperations.sfdc.MissingCoApplicants
	FROM RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTRF 
	WHERE LEN(COAPPLICANT)>1 AND ACCOUNTID IN (SELECT ACCOUNTID FROM RFOPERATIONS.DBO.ACCOUNTIDS)
	EXCEPT
SELECT rfoAccountid__c,REPLACE(c.name,'  ',' ') AS Name 
	FROM SFDCBACKUP.SFDCBKP.CONTACT C,
		 SFDCBACKUP.SFDCBKP.Accounts A,
		 SFDCBACKUP.SFDCBKP.COUNTRY CO
	WHERE C.ACCOUNTID=A.ID and c.contacttype__c='Spouse' AND A.COUNTRY__C=CO.ID AND CO.NAME='Canada'

SELECT 'Query Rfoperations.dbo.MissingCoApplicants to get list of Parent Account ID and associated CoApplicant that were not migrated'


--------------------------------------------------------------------------------------
-- Duplicates 
--------------------------------------------------------------------------------------

--SELECT AccountID, COUNT (RFOAccountID__C) AS CountofDups
--INTO rfoperations.dbo.Accounts_Dups
--FROM Rfoperations.dbo.AccountIDs a JOIN SFDCBACKUP.SFDCBKP.Accounts b ON a.accountid = b.RFOAccountID__C
--GROUP BY  AccountID
--HAVING COUNT (RFOAccountID__C)> 1 


--SELECT @RowCount = COUNT(*) FROM rfoperations.dbo.Accounts_Dups

--IF @RowCount > 0
--	BEGIN 

--			SELECT  cast(@ROWCOUNT as nvarchar) + ' Duplicate Account(s) in CRM. Query rfoperations.dbo.Accounts_Dups to get list of duplicate AccountIDs' 
--	END 

--ELSE 

--SELECT 'No Duplicates'

---------------------------------------------------------------------------------------------
-- Contacts Framework 
---------------------------------------------------------------------------------------------
		--Loading RFO Data
			SELECT DISTINCT 
		CAST (Ac.AccountContactID AS NVARCHAR (100)) AS RFAccountContactId__c,			--p_rfaccountid
       	'Primary' as ContactType__c,
		AB.ACCOUNTID as Account,
		CAST(CASE WHEN AC.Birthday='1900-01-01' THEN NULL ELSE AC.Birthday END AS DATE) as BirthDate,
		CASE WHEN LEN(Ac.ChangedByApplication) <1 THEN NULL ELSE Ac.ChangedByApplication END AS ChangedByApplication__c,
		CASE WHEN LEN(AC.ChangedByUser) <1 THEN NULL ELSE AC.ChangedByUser END AS ChangedByUser__C,
		CASE WHEN LEN(AC.DISPLAYTAXNUMBER) <1 THEN NULL ELSE AC.DISPLAYTAXNUMBER END AS DISPLAYTAXNUMBER__c,
		CASE WHEN LEN(AC.FIRSTNAME) <1 THEN NULL ELSE REPLACE(AC.FIRSTNAME,'  ',' ') END AS FirstName  ,
		CASE WHEN LEN(g.name) <1 THEN NULL ELSE g.name END AS Gender__c,
		CASE WHEN LEN(AC.LastNAME) <1 THEN NULL ELSE REPLACE(AC.LastNAME,'  ',' ') END AS LastName ,
		CASE WHEN LEN(AC.LegalName) <1 THEN NULL ELSE REPLACE(AC.LegalName,'  ',' ') END AS LegalName__C ,
		CASE WHEN LEN(AC.MiddleNAME) <1 THEN NULL ELSE AC.MiddleNAME END AS MiddleName ,
		CASE WHEN LEN(AC.NickNAME) <1 THEN NULL ELSE REPLACE(AC.NickNAME,'  ',' ') END AS NickName__c ,
		CASE WHEN LEN(AC.SecuredTaxNumber) <1 THEN NULL ELSE AC.SecuredTaxNumber END TaxNumber__c,
		CAST(ISNULL(DATEADD(HH,8,AC.ServerModifiedDate),'1900-01-01') AS DATE) as LastModifiedDate,
		CASE WHEN LEN(MPH.PhoneNumberRaw) <1 THEN NULL ELSE MPH.PhoneNumberRaw END as MainPhone__c,
		CASE WHEN LEN(MOB.PhoneNumberRaw) <1 THEN NULL ELSE MOB.PhoneNumberRaw END as MobilePhone,
		CASE WHEN LEN(PEA.EmailAddress) <1 THEN NULL ELSE PEA.EmailAddress END as MainEmail__c,
		'NULL' as SecondaryEmail__c 
		INTO rfoperations.sfdc.RFO_Contacts 
		  -- join address table here.
		FROM  RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB
		JOIN RFOperations.RFO_Reference.AccountType (NOLOCK) ACT ON ACT.AccountTypeID = AB.AccountTypeID AND AB.COUNTRYID=40
		JOIN RFOperations.RFO_Reference.AccountStatus (NOLOCK) AST ON AST.AccountStatusID = AB.AccountStatusID
		JOIN RFOperations.RFO_Accounts.AccountContacts (NOLOCK) AC ON AC.AccountId = AB.AccountID
		JOIN RFOPERATIONS.RFO_REFERENCE.Gender G ON AC.GENDERID=G.GENDERID
		JOIN RFOperations.RFO_Accounts.AccountContactPhones  (NOLOCK) ACPH ON ACPH.AccountContactId = AC.AccountContactId
        JOIN RFOperations.RFO_Accounts.Phones (NOLOCK) MPH ON MPH.PhoneID = ACPH.PhoneId
                                                    AND MPH.PhoneTypeID = 1
                                                    AND MPH.IsDefault = 1
        LEFT JOIN RFOperations.RFO_Accounts.Phones (NOLOCK) MOB ON MOB.PhoneID = ACPH.PhoneId
                                                    AND MOB.PhoneTypeID = 2
                                                    --AND MOB.IsDefault = 1
        JOIN RFOperations.RFO_Accounts.AccountEmails  (NOLOCK) AE ON AE.AccountContactId = AC.AccountContactId
        JOIN RFOperations.RFO_Accounts.EmailAddresses (NOLOCK) PEA ON PEA.EmailAddressID = AE.EmailAddressId
                                                            AND EmailAddressTypeID = 1
                                                            AND PEA.IsDefault = 1
        JOIN RFOperations.RFO_Accounts.AccountRF (NOLOCK) AR ON AB.AccountID = AR.AccountID
		WHERE AB.ServerModifiedDate>= @LastRunDate
        AND NOT EXISTS (SELECT 1 FROM Rfoperations.dbo.ContactMissing CM WHERE CM.RFO_ACCOUNTCONTACTID=AC.ACCOUNTCONTACTID AND MISSINGFROM='Destination')

	--Load Hybris Data    
	SELECT 
	C.RFAccountContactId__c as RFAccountContactId__c,
	C.ContactType__c,
	A.RFOAccountID__C as Account, 
	CASE WHEN LEN(c.BirthDate) <1 THEN NULL ELSE CAST(c.BirthDate AS DATE) END AS BirthDate,
	CASE WHEN LEN(c.ChangedByApplication__c) <1 THEN NULL ELSE c.ChangedByApplication__c END AS ChangedByApplication__C,
	CASE WHEN LEN(c.ChangedByUser__c) <1 THEN NULL ELSE c.ChangedByUser__c END AS ChangedByUser__c,
	CASE WHEN LEN(C.DisplayTaxNumber__c) <1 THEN NULL ELSE C.DisplayTaxNumber__c END AS DisplayTaxNumber__c,
	CASE WHEN LEN(C.FirstName) <1 THEN NULL ELSE REPLACE(C.FirstName,'  ',' ') END AS FirstName,
	CASE WHEN LEN(C.Gender__c) <1 THEN NULL ELSE C.Gender__c END AS Gender__c,
	CASE WHEN LEN(c.LastName) <1 THEN NULL ELSE REPLACE(c.LastName,'  ',' ') END AS LastName,
	CASE WHEN LEN(c.LegalName__c) <1 THEN NULL ELSE c.LegalName__c END AS LegalName__c,
	CASE WHEN LEN(c.MiddleName) <1 THEN NULL ELSE c.MiddleName END AS MiddleName,
	CASE WHEN LEN(C.NickName__c) <1 THEN NULL ELSE REPLACE(C.NickName__c,'  ',' ') END as NickName__c,
	CASE WHEN LEN(c.TaxNumber__c) <1 THEN NULL ELSE c.TaxNumber__c END AS TaxNumber__c,
	CAST(c.LastModifiedDate AS DATE) AS LastModifiedDate,
	CASE WHEN LEN(c.MainPhone__c) <1 THEN NULL ELSE c.MainPhone__c END AS MainPhone__c,
	CASE WHEN LEN(c.MobilePhone) <1 THEN NULL ELSE c.MobilePhone END AS MobilePhone,
	CASE WHEN LEN(c.MainEmail__c) <1 THEN NULL ELSE c.MainEmail__c END AS MainEmail__c,
	CASE WHEN LEN(c.SecondaryEmail__c) <1 THEN NULL ELSE c.SecondaryEmail__c END AS SecondaryEmail__c
	INTO rfoperations.sfdc.CRM_Contacts
	FROM 
	SFDCBACKUP.SFDCBKP.Accounts A,
	SFDCBACKUP.SFDCBKP.CONTACT C,
	SFDCBACKUP.SFDCBKP.COUNTRY CO
	WHERE A.MAINCONTACT__C=C.ID AND 
	C.ContactType__C='Primary' AND
	A.COUNTRY__C=CO.ID AND
	CO.NAME='Canada'
		
CREATE CLUSTERED INDEX RF_ContactID ON rfoperations.sfdc.RFO_Contacts (RFAccountContactId__c)
CREATE CLUSTERED INDEX Hyb_ContactID ON rfoperations.sfdc.CRM_Contacts (RFAccountContactId__c)


--Load Comparison Candidates.
		SELECT
		RFAccountContactId__c,			--p_rfaccountid
       	ContactType__c,
		Account,
		BirthDate,
		ChangedByApplication__c,
		ChangedByUser__C,
		DISPLAYTAXNUMBER__c,
		FirstName  ,
		Gender__c,
		LastName ,
		LegalName__C ,
		MiddleName ,
		CASE WHEN LEN(NickName__c)< 1 THEN NULL ELSE NickName__c END AS NickName__c,
		TaxNumber__c,
		LastModifiedDate,
		MainPhone__c,
		MobilePhone,
		MainEmail__c,
		SecondaryEmail__c 
		INTO  #Contacts 
		FROM rfoperations.sfdc.RFO_Contacts
EXCEPT 
SELECT RFAccountContactId__c,			--p_rfaccountid
       	ContactType__c,
		Account,
		CAST(CASE WHEN BirthDate='1900-01-01' THEN NULL ELSE Birthdate END AS DATE) AS BirthDate,
		ChangedByApplication__c,
		ChangedByUser__C,
		DISPLAYTAXNUMBER__c,
		FirstName  ,
		Gender__c,
		LastName ,
		REPLACE(LegalName__C,'  ','') AS LegalName__c ,
		MiddleName ,
		NickName__c ,
		TaxNumber__c,
		LastModifiedDate,
		MainPhone__c,
		MobilePhone,
		MainEmail__c,
		SecondaryEmail__c  FROM rfoperations.sfdc.crm_Contacts

CREATE CLUSTERED INDEX MIX_ContactID ON #Contacts (RFAccountContactId__c)

--SELECT * FROM RFOPERATIONS.SFDC.RFO_CONTACTS WHERE RFACCOUNTCONTACTID__C='100075356665746356'

--SELECT * FROM RFOPERATIONS.SFDC.CRM_CONTACTS WHERE RFACCOUNTCONTACTID__C='100075356665746356'

--SELECT * FROM #Contacts
---------------------------------------------------------------------------------------------
-- Business Rule Validations
---------------------------------------------------------------------------------------------


CREATE TABLE rfoperations.sfdc.ErrorLog_Contacts
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, ColID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
, CRM_Value NVARCHAR (MAX)
)

DECLARE @I INT = (SELECT MIN(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'Contacts') , 
@C INT =  (SELECT MAX(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'Contacts') 


DECLARE @DesKey NVARCHAR (50) 

DECLARE @SrcKey NVARCHAR (50) 

DECLARE @Skip  BIT 

WHILE (@I <=@c)

BEGIN 

        SELECT  @Skip = ( SELECT   Skip
                               FROM     Rfoperations.sfdc.CRM_METADATA
                               WHERE    ColID = @I
                             );


        IF ( @Skip = 1 )

            SET @I = @I + 1;

        ELSE
BEGIN 



DECLARE @SrcCol NVARCHAR (50) =(SELECT RFO_Column FROM Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)

DECLARE @DesTemp NVARCHAR (50) =(SELECT CASE WHEN CRMObject = 'Accounts' THEN 'rfoperations.sfdc.CRM_Accounts' 
											 WHEN CRMObject = 'Contacts' THEN 'rfoperations.sfdc.CRM_Contacts'
										END
			FROM  Rfoperations.sfdc.CRM_METADATA 
			  WHERE ColID =@I
								) 

DECLARE @DesCol NVARCHAR (50) =(SELECT CRM_Column FROM Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)

SET @SrcKey= (SELECT RFO_Key
			  FROM Rfoperations.sfdc.CRM_METADATA 
			  WHERE ColID =@I
								)

                SET @DesKey = ( SELECT  CASE WHEN CRMObject= 'Accounts' THEN 'RFOAccountId__c'
											 WHEN CRMObject= 'Contacts' THEN 'RFAccountContactId__c'
                                        END
                                FROM    Rfoperations.sfdc.CRM_METADATA
                                WHERE   ColID = @I
                              ); 


DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM  Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)
DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.CRM_Value = b. ' + @DesCol +
' FROM rfoperations.sfdc.ErrorLog_Contacts a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.ColID = ' + CAST(@I AS NVARCHAR)



DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO rfoperations.sfdc.ErrorLog_Contacts (Identifier,ColID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

  BEGIN TRY
  --SELECT @SQL3
   EXEC sp_executesql @SQL3, N'@ServerMod DATETIME', @ServerMod= '2014-05-01'

 SET @I = @I + 1

 END TRY

 BEGIN CATCH

 SELECT ' This SQL Failed - ' + @SQL3

 SET @I = @I + 1

 END CATCH
END 

END 


SELECT  B.COLID,b.RFO_column, COUNT(*) AS Counts
FROM rfoperations.sfdc.ErrorLog_Contacts A JOIN Rfoperations.sfdc.CRM_METADATA B ON a.ColID =b.ColID
GROUP BY b.ColID, RFO_Column

drop index RF_ContactID ON rfoperations.sfdc.RFO_Contacts
drop index Hyb_ContactID ON rfoperations.sfdc.crm_Contacts
drop index MIX_ContactID ON #Contacts


END




GO


