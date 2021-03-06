USE [RFOperations]
GO

/****** Object:  StoredProcedure [dbo].[VerifyAccountMigration]    Script Date: 10/27/2015 6:53:02 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


ALTER PROCEDURE [dbo].[VerifyAccountMigration]  @LastRunDate DATETIME
AS
BEGIN

IF OBJECT_ID('Rfoperations.dbo.AccountsMissing') IS NOT NULL  DROP TABLE Rfoperations.dbo.AccountsMissing
IF OBJECT_ID('Rfoperations.dbo.Accounts_Dups') IS NOT NULL  DROP TABLE Rfoperations.dbo.Accounts_Dups 
IF OBJECT_ID ('Rfoperations.dbo.AccountIDs') IS NOT NULL  DROP TABLE Rfoperations.dbo.AccountIDs
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
IF OBJECT_ID('Rfoperations.sfdc.RFO_Accounts') IS NOT NULL DROP TABLE Rfoperations.sfdc.RFO_Accounts
IF OBJECT_ID('Rfoperations.sfdc.crm_Accounts') IS NOT NULL DROP TABLE Rfoperations.sfdc.crm_Accounts
IF OBJECT_ID('Rfoperations.sfdc.ErrorLog_Accounts') IS NOT NULL DROP TABLE Rfoperations.sfdc.ErrorLog_Accounts
IF OBJECT_ID('rfoperations.sfdc.BusinessRuleFailure') IS NOT NULL DROP TABLE rfoperations.sfdc.BusinessRuleFailure
IF OBJECT_ID('rfoperations.sfdc.AccountDifference') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountDifference


SELECT ' Refer Following tables to view test results.
		1. Rfoperations.sfdc.AccountDifference--> Difference in RFO and CRM Accounts
		2. Rfoperations.sfdc.AccountsMissing --> List of Missing AccountIDs from Source and Destination.
		3. Rfoperations.sfdc.Accounts_Dups --> List of Duplicate Account IDs in CRM.
		4. Rfoperations.sfdc.BusinessRuleFailure --> List of Accounts failed due to business rule failure.
		5. Rfoperations.sfdc.ErrorLog_Accounts --> List of All failures due to field mismatch. This table also provided RFO and CRM side values
		6. Rfoperations.sfdc.CRM_METADATA --> Mapping of RFO and CRM fields to be compared. Use this table in conjunction with above table to generate final test result'
		

SET ANSI_WARNINGS OFF 

DECLARE @ServerMod DATETIME =@LastRunDate
DECLARE @RFOAccount BIGINT, @CRMAccount BIGINT

DECLARE @RowCount BIGINT 

------------------------------------------------------------------------------------------------------------------------------
-- Accounts 
-----------------------------------------------------------------------------------------------------------------------------
SELECT DISTINCT a.AccountID INTO Rfoperations.dbo.AccountIDs --COUNT( DISTINCT a.AccountID)
FROM RFOperations.RFO_Accounts.AccountRF (NOLOCK)a 
JOIN RFOperations.RFO_Accounts.AccountBase (NOLOCK)  b ON a.AccountID =b.AccountID  AND b.CountryID =236
 JOIN RFOperations.RFO_Accounts.AccountContacts  (NOLOCK) d ON b.AccountID =d.AccountID 
 JOIN RFOperations.RFO_Accounts.AccountEmails (NOLOCK) e ON e.AccountContactID = D.AccountContactID 
 JOIN RFOperations.RFO_Accounts.AccountContactAddresses  (NOLOCK) g ON g.AccountContactID = d.AccountContactID 
 JOIN RFOperations.RFO_Accounts.AccountContactPhones  (NOLOCK)j ON j.AccountContactID = d.AccountContactID 
 LEFT JOIN RFOperations.RFO_Accounts.Phones  (NOLOCK) p ON j.PhoneID =p.PhoneID AND p.PhoneTypeID = 1 
 LEFT JOIN RFOperations.RFO_Accounts.Addresses  (NOLOCK) i ON i.AddressID =g.AddressID AND i.AddressTypeID =1 AND i.IsDefault= 1 
 LEFT JOIN RFOperations.RFO_Accounts.EmailAddresses (NOLOCK)  f ON f.EmailAddressID =E.EmailAddressId AND EmailAddressTypeID =1 
 AND B.ServerModifiedDate>=@LastRunDate



SELECT @RFOAccount =COUNT( DISTINCT AccountID) FROM RFOPerations.RFO_Accounts.AccountBase (NOLOCK) WHERE AccountID IN (SELECT AccountID FROM Rfoperations.dbo.AccountIDs) AND ServerModifiedDate> @LastRunDate AND CountryID =236

SELECT @CRMAccount=COUNT(RFOAccountID__C) FROM sfdcbackup.SFDCbkp.Accounts A , SFDCBACKUP.SFDCBKP.Country c WHERE A.COUNTRY__C=C.ID AND C.Name='United States'

SELECT  @RFOAccount AS RFO_Accounts, @CRMAccount AS CRM_Accounts, (@RFOAccount - @CRMAccount) AS Difference 
INTO rfoperations.sfdc.AccountDifference;

SELECT  AccountID AS RFO_AccountID,
 b.RFOAccountID__C AS CRM_rfAccountID , 
 CASE WHEN b.RFOAccountID__C IS NULL THEN 'Destination'
      WHEN a.AccountID IS NULL THEN 'Source' 
 END AS MissingFROM
INTO Rfoperations.dbo.AccountsMissing
FROM 
    (SELECT AccountID FROM Rfoperations.dbo.AccountIDs) a
    FULL OUTER JOIN 
    (SELECT RFOAccountID__C FROM  SFDCBACKUP.SFDCBKP.Accounts (NOLOCK) A , SFDCBACKUP.SFDCBKP.Country c WHERE A.COUNTRY__C=C.ID AND C.Name='United States') b 
	ON a.AccountID =b.RFOAccountID__C
 WHERE (a.AccountID IS NULL OR b.RFOAccountID__C IS NULL) 


SELECT MissingFrom ,COUNT(*) from Rfoperations.dbo.AccountsMissing GROUP BY MISSINGFROM;

SELECT 'Query Rfoperations.dbo.AccountsMissing to get list of AccountIDs missing from Source/Destination'

--------------------------------------------------------------------------------------
-- Duplicates 
--------------------------------------------------------------------------------------

SELECT AccountID, COUNT (RFOAccountID__C) AS CountofDups
INTO rfoperations.dbo.Accounts_Dups
FROM Rfoperations.dbo.AccountIDs a JOIN SFDCBACKUP.SFDCBKP.Accounts b ON a.accountid = b.RFOAccountID__C
GROUP BY  AccountID
HAVING COUNT (RFOAccountID__C)> 1 


SELECT @RowCount = COUNT(*) FROM rfoperations.dbo.Accounts_Dups

IF @RowCount > 0
	BEGIN 

			SELECT  cast(@ROWCOUNT as nvarchar) + ' Duplicate Account(s) in CRM. Query rfoperations.dbo.Accounts_Dups to get list of duplicate AccountIDs' 
	END 

ELSE 

SELECT 'No Duplicates'

---------------------------------------------------------------------------------------------
-- Accounts Framework 
---------------------------------------------------------------------------------------------

--Loading RFO Data
SELECT DISTINCT 
		CAST (AB.AccountID AS NVARCHAR (100)) AS AccountID	,			--p_rfaccountid
       	REPLACE(AC.FIRSTNAME+' '+ AC.LASTNAME,'  ',' ') AS Name  ,
		AB.AccountNumber,
		CAST(ISNULL(AB.ServerModifiedDate,'1900-01-01') AS DATE) as LastModifiedDate,
		CAST(ISNULL(AB.ServerModifiedDate,'1900-01-01') AS DATE) as CreationDate,
		AST.NAME as AccountStatus,
		ACT.NAME as AccountType,
		CAST(ACT.AccountTypeId AS NVARCHAR) as RecordTypeID,
		AB.ChangedByApplication,
		AB.ChangedByUser,
		CAST(C.NAME AS NVARCHAR(MAX)) CountryId,
		CAST(CY.name AS NVARCHAR(MAX)) CurrencyId,
		CAST(lg.English AS NVARCHAR(MAX)) Languageid,
		CAST(tz.name AS NVARCHAR(MAX)) TimeZoneId,
		CASE WHEN AR.ACTIVE=1 THEN 'true' ELSE 'false' END  as IsActive,
		CAST(AR.CoApplicant AS NVARCHAR(MAX)) CoApplicant,
		CAST(AR.EnrollerId AS NVARCHAR(MAX)) EnrollerId,
		CAST(AR.SponsorId AS NVARCHAR(MAX)) SponsorId, 	
		CAST(ISNULL(AR.EnrollmentDate,'1900-01-01') AS DATE) EnrollmentDate,
		CAST(ISNULL(AR.HardTerminationDate,'1900-01-01') AS DATE) HardTerminationDate,
		CAST(CASE WHEN AR.IsBusinessEntity=1 THEN 'true' WHEN AR.IsBusinessEntity=0 THEN 'false' END AS NVARCHAR(MAX)) IsBusinessEntity,
		CAST(CASE WHEN AR.IsTaxExempt= 1 THEN 'true' WHEN AR.IsTaxExempt=0 THEN 'false' END AS NVARCHAR(MAX)) IsTaxExempt,
		CAST(ISNULL(AR.LastAutoAssignmenDate,'1900-01-01') AS DATE) LastAutoAssignmenDate,
		CAST(ISNULL(AR.LastRenewalDate,'1900-01-01') AS DATE) LastRenewalDate,
		CAST(ISNULL(AR.NextRenewalDate,'1900-01-01') AS DATE) NextRenewalDate,
		CAST(ISNULL(AR.SoftTerminationDate,'1900-01-01') AS DATE) SoftTerminationDate,
		CAST(ISNULL(AR.ServerModifiedDate,'1900-01-01') AS DATE) as LastModifiedDate_1,
		REPLACE(CAST(AC.LegalName AS NVARCHAR(MAX)),'  ',' ') LegalName,
		CAST(AC.AccountContactID AS NVARCHAR(MAX)) As MainContact,
		CAST(AA.AddressID AS NVARCHAR(MAX)) RFO_AddressProfileID,
		CAST(AA.AddressLine1 AS NVARCHAR(MAX)) AddressLine1,
		CAST(AA.AddressLine2 AS NVARCHAR(MAX)) AddressLine2,
		CAST(AA.AddressLine3 AS NVARCHAR(MAX)) AddressLine3,
		CAST(AA.AddressLine4 AS NVARCHAR(MAX)) AddressLine4,
		CAST(AA.AddressLine5 AS NVARCHAR(MAX)) AddressLine5,
		CAST(C.NAME AS NVARCHAR(MAX)) as MainAddressCountry,
		CAST(CAST(AA.latitude AS DECIMAL(10,1)) AS NVARCHAR(MAX)) latitude,
		CAST(AA.locale AS NVARCHAR(MAX)) locale,
		CAST(CAST(AA.longitude AS DECIMAL(10,1)) AS NVARCHAR(MAX)) longitude,
		CAST(AA.Region AS NVARCHAR(MAX)) Region,
		CAST(AA.PostalCode AS NVARCHAR(MAX)) PostalCode,
		CAST(AA.SubRegion AS NVARCHAR(MAX)) SubRegion,
		'' as TaxNumber__c		  
		INTO rfoperations.sfdc.RFO_ACCOUNTS  
		  -- join address table here.
		FROM  RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB
		JOIN RFOperations.RFO_Reference.Countries (NOLOCK) C ON c.CountryID =ab.CountryID  AND AB.CountryID =236
		JOIN RFOperations.RFO_Reference.Currency (NOLOCK) CY ON cy.CurrencyID =ab.CurrencyID 
		JOIN RFOperations.RFO_Reference.NativeLanguage (NOLOCK) LG ON lg.languageid =ab.languageID 
		JOIN RFOPERATIONS.RFO_REFERENCE.TIMEZONE TZ on AB.TIMEZONEID=TZ.TIMEZONEID
		JOIN RFOperations.RFO_Reference.AccountType (NOLOCK) ACT ON ACT.AccountTypeID = AB.AccountTypeID
		JOIN RFOperations.RFO_Reference.AccountStatus (NOLOCK) AST ON AST.AccountStatusID = AB.AccountStatusID
		JOIN RFOperations.RFO_Accounts.AccountContacts (NOLOCK) AC ON AC.AccountId = AB.AccountID
		JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.ACCOUNTCONTACTID=AC.ACCOUNTCONTACTID
		JOIN RFOPERATIONS.RFO_ACCOUNTS.ADDRESSES AA ON ACA.ADDRESSID=AA.ADDRESSID AND ADDRESSTYPEID=1
		JOIN RFOperations.RFO_Accounts.AccountContactPhones  (NOLOCK) ACPH ON ACPH.AccountContactId = AC.AccountContactId
        JOIN RFOperations.RFO_Accounts.Phones (NOLOCK) PH ON PH.PhoneID = ACPH.PhoneId
                                                    AND PH.PhoneTypeID = 1
                                                    AND PH.IsDefault = 1
        JOIN RFOperations.RFO_Accounts.AccountEmails  (NOLOCK) AE ON AE.AccountContactId = AC.AccountContactId
        JOIN RFOperations.RFO_Accounts.EmailAddresses (NOLOCK) EA ON EA.EmailAddressID = AE.EmailAddressId
                                                            AND EmailAddressTypeID = 1
                                                            AND EA.IsDefault = 1
        JOIN RFOperations.RFO_Accounts.AccountRF (NOLOCK) AR ON AB.AccountID = AR.AccountID
		JOIN RFOperations.Security.AccountSecurity (NOLOCK) ASE ON ASE.AccountID = ab.AccountID
		WHERE AB.ServerModifiedDate>= @LastRunDate AND
		NOT EXISTS (SELECT 1 FROM RFOPERATIONS.DBO.AccountsMissing AM WHERE MISSINGFROM ='Destination' AND AM.RFO_ACCOUNTID=AB.ACCOUNTID)
		
        
		--SELECT * FROM SFDCBACKUP.SFDCBKP.language ORDER BY NAME
		--SELECT * FROM RFOPERATIONS.SFDC.RFO_ACCOUNTS
		--SELECT * FROM RFOPERATIONS.RFO_REFERENCE.nativelanguage ORDER BY 3
	
	--Loading Hybris data
	SELECT 
	A.RFOAccountId__c,
	A.Name, 
	A.AccountNumber,
	CAST(A.LastModifiedDate AS DATE ) LastModifiedDate,
	CAST (A.CreatedDate AS DATE) CreatedDate,
	A.AccountStatus__c, -- To be mapped.
	CASE WHEN A.AccountType__c = 'Preferred Customer' THEN 'PreferredCustomer' ELSE A.AccountType__c END AS AccountType__c,
	CASE WHEN RT.NAME ='Preferred Customer' THEN 2 WHEN RT.NAME ='Consultant' THEN 1 WHEN RT.NAME ='Retail Customer' then 3 END AS RecordTypeId, -- To be mapped
	A.ChangedByApplication__c,
	A.ChangeByUser__c,
	C.NAME Country__c,
	CY.NAME Currency__c,
	LG.NAME Language__c,
	TZ.NAME Timezone__c,
	A.IsActive__c,
	A.Coapplicant__c,
	ASP.RFOAccountId__c as ParentSponsor__c,
	AEN.RFOAccountId__c as ParentEnroller__c,
	CAST(ISNULL(A.EnrollmentDate__c,'1900-01-01') AS DATE) EnrollmentDate__c,
	CAST(ISNULL(A.HardTerminationDate__c,'1900-01-01') AS DATE) HardTerminationDate__c,
	A.IsBusinessEntity__c,
	A.IsTaxExempt__c,
	CAST(ISNULL(A.LastAutoAssignmentDate__c,'1900-01-01') AS DATE) LastAutoAssignmentDate__c,
	CAST(ISNULL(A.LastRenewalDate__c,'1900-01-01') AS DATE) LastRenewalDate__c,
	CAST(ISNULL(A.NextRenewalDate__c,'1900-01-01') AS DATE) NextRenewalDate__c,
	CAST(ISNULL(A.SoftTerminationDate__c,'1900-01-01') AS DATE) SoftTerminationDate__c ,
	CAST(ISNULL(A.LastModifiedDate,'1900-01-01') AS DATE) AS LastModifiedDate_1,
	A.LegalTaxName__c,
	CO.RFAccountContactID__c as MainContact__c,
	A.RFOAddressProfileId__c,
	A.AddressLine1__c,
	A.AddressLine2__c,
	A.AddressLine3__c,
	A.AddressLine4__c,
	A.AddressLine5__c,
	C.NAME AS MainAddressCountry__c,
	A.Latitude__c,
	A.Locale__c,
	A.Longitude__c,
	A.Region__c,
	A.PostalCode__c,
	A.SubRegion__c,
	A.TaxNumber__c
	INTO rfoperations.sfdc.CRM_ACCOUNTS
	FROM SFDCBACKUP.SFDCBKP.Accounts A LEFT JOIN SFDCBACKUP.SFDCBKP.Accounts ASP ON ASP.ID=A.ParentSponsor__c 
		 LEFT JOIN SFDCBACKUP.SFDCBKP.Accounts AEN ON AEN.ID=A.ParentEnroller__c,
	SFDCBACKUP.SFDCBKP.COUNTRY C,
	SFDCBACKUP.SFDCBKP.CURRENCY CY,
	SFDCBACKUP.SFDCBKP.LANGUAGE LG,
	SFDCBACKUP.SFDCBKP.TIMEZONE TZ,
	SFDCBACKUP.SFDCBKP.RECORDTYPE RT,
	SFDCBACKUP.SFDCBKP.CONTACT CO
	--SFDCBACKUP.SFDCBKP.ACCOUNTSTATUS AAS
	WHERE A.COUNTRY__C=C.ID AND
	A.CURRENCY__C=CY.ID AND
	A.LANGUAGE__C=LG.ID AND
	A.TIMEZONE__C=TZ.ID AND
	RT.ID=A.RECORDTYPEID AND
	A.MAINCONTACT__C=CO.ID AND 
	CO.ContactType__C='Primary' AND
	C.NAME='United States'

		
CREATE CLUSTERED INDEX MIX_AccountID ON rfoperations.sfdc.RFO_Accounts (AccountID)
CREATE CLUSTERED INDEX MIX_rfAccountID ON rfoperations.sfdc.CRM_Accounts (RFOAccountID__c)


--Load Comparison Candidates.
SELECT * INTO  #Accounts FROM rfoperations.sfdc.RFO_Accounts
EXCEPT 
SELECT * FROM rfoperations.sfdc.CRM_Accounts

CREATE CLUSTERED INDEX MIX_AccountID ON #Accounts (AccountID)

---------------------------------------------------------------------------------------------
-- Business Rule Validations
---------------------------------------------------------------------------------------------

CREATE TABLE RFOPERATIONS.SFDC.BusinessRuleFailure
(AccountId BIGINT,
 FailureReason NVARCHAR(MAX),
 FailedValue NVARCHAR(MAX)
 )
  
--Identify Missing Enroller/Sponsor for a PC/Consultant
INSERT INTO rfoperations.sfdc.BusinessRuleFailure
SELECT ACCOUNTID , ' Enroller/Sponsor is not populated for a PC or a Consultant', EnrollerId
FROM rfoperations.sfdc.Rfo_accounts RFO,
rfoperations.sfdc.crm_accounts CRM
where RFO.ACCOUNTID=CRM.RFOAccountId__c AND RFO.RECORDTYPEID IN (1,2) AND (EnrollerID IS NULL OR SponsorId IS NULL)

--Identify Missing Enroller/Sponsor for a RC
INSERT INTO rfoperations.sfdc.BusinessRuleFailure
SELECT ACCOUNTID , ' Enroller/Sponsor is not mapped to Corporate RF Account for a RC. Comma Separated value for Enroller and Sponsor is loaded in FailureValue column', EnrollerId+','+SponsorId
FROM rfoperations.sfdc.Rfo_accounts RFO,
rfoperations.sfdc.crm_accounts CRM
where RFO.ACCOUNTID=CRM.RFOAccountId__c AND RFO.RECORDTYPEID IN (3) AND isActive='true' AND (EnrollerID <>2 OR SponsorId <>2)

--SoftTerminationDate , HardTerminationDate, Active and AccountStatus Integrity
INSERT INTO rfoperations.sfdc.BusinessRuleFailure
SELECT ACCOUNTID , 'SoftTerminationDate , HardTerminationDate, Active and AccountStatus values are not in Synch', ''
FROM rfoperations.sfdc.Rfo_accounts RFO,
rfoperations.sfdc.crm_accounts CRM
where RFO.ACCOUNTID=CRM.RFOAccountId__c AND 
	(
	 (CRM.AccountStatus__c ='Active' AND (CRM.isActive__c='false' OR SoftTerminationDate__c IS NOT NULL OR HardTerminationDate__c IS NOT NULL))
	 OR
	 (CRM.AccountStatus__c ='Hard Terminated' AND (CRM.isActive__c='true' OR SoftTerminationDate__c IS NULL OR HardTerminationDate__c IS NULL OR (HardTerminationDate__c < SoftTerminationDate__c)))
	 OR
	 (CRM.AccountStatus__c ='Soft Terminated' AND (CRM.isActive__c='true' OR SoftTerminationDate__c IS NULL OR HardTerminationDate__c IS NOT NULL OR (HardTerminationDate__c < SoftTerminationDate__c)))
	)

--Identify missing Tax ID value
INSERT INTO rfoperations.sfdc.BusinessRuleFailure
SELECT ACCOUNTID , ' Tax Number is not populated', CRM.TAXNUMBER__C 
FROM rfoperations.sfdc.Rfo_accounts RFO,
rfoperations.sfdc.crm_accounts CRM
where RFO.ACCOUNTID=CRM.RFOAccountId__c AND LEN(CRM.TAXNUMBER__C)<1 AND AccountType='Consultant'

SELECT ' Business Rule Failures loaded in Error table'

CREATE TABLE rfoperations.sfdc.ErrorLog_Accounts
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, ColID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
, CRM_Value NVARCHAR (MAX)
)


DECLARE @I INT = (SELECT MIN(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'Accounts') , 
@C INT =  (SELECT MAX(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'Accounts') 


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

DECLARE @DesTemp NVARCHAR (50) =(SELECT CASE WHEN CRMObject = 'Accounts' THEN 'rfoperations.sfdc.CRM_Accounts' END
			FROM  Rfoperations.sfdc.CRM_METADATA 
			  WHERE ColID =@I
								) 

DECLARE @DesCol NVARCHAR (50) =(SELECT CRM_Column FROM Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)

SET @SrcKey= (SELECT RFO_Key
			  FROM Rfoperations.sfdc.CRM_METADATA 
			  WHERE ColID =@I
								)

                SET @DesKey = ( SELECT  CASE WHEN CRMObject= 'Accounts' THEN 'RFOAccountId__c'
                                        END
                                FROM    Rfoperations.sfdc.CRM_METADATA
                                WHERE   ColID = @I
                              ); 


DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM  Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)
DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.CRM_Value = b. ' + @DesCol +
' FROM rfoperations.sfdc.ErrorLog_Accounts a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.ColID = ' + CAST(@I AS NVARCHAR)



DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO rfoperations.sfdc.ErrorLog_Accounts (Identifier,ColID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

  BEGIN TRY
  SELECT @SQL3
   EXEC sp_executesql @SQL3, N'@ServerMod DATETIME', @ServerMod= '2014-05-01'

 SET @I = @I + 1

 END TRY

 BEGIN CATCH

 SELECT @SQL3

 SET @I = @I + 1

 END CATCH
END 

END 



SELECT  B.COLID,b.RFO_column, COUNT(*) AS Counts
FROM rfoperations.sfdc.ErrorLog_Accounts A JOIN Rfoperations.sfdc.CRM_METADATA B ON a.ColID =b.ColID
GROUP BY b.ColID, RFO_Column


drop index MIX_AccountID ON #RFO_Accounts 
drop index MIX_rfAccountID ON #Hybris_Accounts
drop index MIX_AccountID1 ON #Accounts

END






GO


