USE [RFOperations]
GO

/****** Object:  StoredProcedure [dbo].[VerifyPaymentProfileMigration]    Script Date: 10/29/2015 10:05:30 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


ALTER PROCEDURE [dbo].[VerifyPaymentProfileMigration] @LastRunDate DATETIME
AS
BEGIN 


DECLARE @LASTRUNDATE DATETIME='2000-01-01';
IF OBJECT_ID('Rfoperations.sfdc.PaymentProfilesMissing') IS NOT NULL  DROP TABLE Rfoperations.sfdc.PaymentProfilesMissing
IF OBJECT_ID('Rfoperations.sfdc.CRM_PaymentProfiles') IS NOT NULL DROP TABLE Rfoperations.sfdc.CRM_PaymentProfiles
IF OBJECT_ID('Rfoperations.sfdc.RFO_PaymentProfiles') IS NOT NULL DROP TABLE Rfoperations.sfdc.RFO_PaymentProfiles
IF OBJECT_ID('Rfoperations.sfdc.ErrorLog_PaymentProfiles') IS NOT NULL DROP TABLE Rfoperations.sfdc.ErrorLog_PaymentProfiles
IF OBJECT_ID('rfoperations.sfdc.PaymentProfileDifference') IS NOT NULL DROP TABLE rfoperations.sfdc.PaymentProfileDifference
IF OBJECT_ID('rfoperations.sfdc.PaymentProfile_Dups') IS NOT NULL DROP TABLE rfoperations.sfdc.PaymentProfile_Dups
IF OBJECT_ID('tempdb.dbo.#PaymentProfiles') IS NOT NULL DROP TABLE #PaymentProfiles

SELECT ' Refer Following tables to view test results.
		1. Rfoperations.dbo.PaymentProfileDifference--> Difference in RFO and CRM Accounts
		2. Rfoperations.dbo.AccountsMissing --> List of Missing AccountIDs from Source and Destination.
		3. Rfoperations.dbo.Accounts_Dups --> List of Duplicate Account IDs in CRM.
		4. Rfoperations.dbo.BusinessRuleFailure --> List of Accounts failed due to business rule failure.
		5. Rfoperations.sfdc.ErrorLog_Accounts --> List of All failures due to field mismatch. This table also provided RFO and CRM side values
		6. Rfoperations.sfdc.CRM_METADATA --> Mapping of RFO and CRM fields to be compared. Use this table in conjunction with above table to generate final test result'
		

SET ANSI_WARNINGS OFF 

DECLARE @ServerMod DATETIME =@LastRunDate
DECLARE @RFOPP BIGINT, @CRMPP BIGINT

DECLARE @RowCount BIGINT 

------------------------------------------------------------------------------------------------------------------------------
-- Accounts 
-----------------------------------------------------------------------------------------------------------------------------
SELECT @RFOPP=COUNT(DISTINCT PP.PAYMENTPROFILEID)  --COUNT( DISTINCT a.AccountID)
FROM    RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB
		JOIN RFOperations.RFO_Reference.Countries (NOLOCK) C ON c.CountryID =ab.CountryID AND AB.COUNTRYID=40
		JOIN RFOperations.RFO_Reference.Currency (NOLOCK) CY ON cy.CurrencyID =ab.CurrencyID 
		JOIN RFOperations.RFO_Accounts.AccountContacts (NOLOCK) AC ON AC.AccountId = AB.AccountID
		JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.ACCOUNTCONTACTID=AC.ACCOUNTCONTACTID
		JOIN RFOPERATIONS.RFO_ACCOUNTS.ADDRESSES AA ON ACA.ADDRESSID=AA.ADDRESSID AND ADDRESSTYPEID=1
		JOIN RFOPERATIONS.RFO_ACCOUNTS.PAYMENTPROFILES PP ON PP.ACCOUNTID=AB.ACCOUNTID
		JOIN RFOPERATIONS.RFO_Accounts.CreditCardProfiles CCP ON CCP.PAYMENTPROFILEID=PP.PAYMENTPROFILEID
		JOIN RFOPERATIONS.RFO_REFERENCE.CREDITCARDVENDORS CCV ON CCV.VENDORID=CCP.VENDORID
		JOIN RFOPERATIONS.RFO_REFERENCE.paymenttype PT ON PT.PAYMENTTYPEID=PP.PAYMENTTYPEID
  AND PP.ServerModifiedDate>=@LastRunDate
--AND b.CountryID =236


SELECT @CRMPP=COUNT(RFOPaymentProfileId__c) FROM sfdcbackup.SFDCBKP.PaymentProfile PP,
										  SFDCBACKUP.SFDCBKP.Accounts A ,
										  SFDCBACKUP.SFDCBKP.COUNTRY C
									 WHERE PP.ACCOUNT__C=A.ID AND
										   CAST(PP.LASTMODIFIEDDATE AS DATE) >=	@LastRunDate AND A.COUNTRY__C=C.ID AND C.NAME='Canada'


SELECT  @RFOPP AS RFO_PaymentProfile, @CRMPP AS CRM_PaymentProfile, (@RFOPP - @CRMPP) AS Difference 
INTO rfoperations.sfdc.PaymentProfileDifference;

SELECT  PaymentProfileId AS RFO_PaymentProfileId,
 b.RFOPaymentProfileId__C AS CRM , 
 CASE WHEN b.RFOPaymentProfileId__C IS NULL THEN 'Destination'
      WHEN a.PaymentProfileId IS NULL THEN 'Source' 
 END AS MissingFROM
INTO Rfoperations.sfdc.PaymentProfilesMissing
FROM 
    (SELECT PaymentProfileId FROM Rfoperations.rfo_accounts.AccountBase AB, RFOPERATIONS.RFO_ACCOUNTS.PaymentProfiles pp WHERE PP.ACCOUNTID=AB.ACCOUNTID AND AB.COUNTRYID=40) a
    FULL OUTER JOIN 
    (SELECT RFOPaymentProfileId__C FROM sfdcbackup.SFDCBKP.PaymentProfile PP,SFDCBACKUP.SFDCBKP.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE PP.ACCOUNT__C=A.ID AND CAST(PP.LASTMODIFIEDDATE AS DATE) >= @LastRunDate AND A.COUNTRY__C=C.ID AND C.NAME='Canada') b 
	ON a.PaymentProfileId =b.RFOPaymentProfileId__C
 WHERE (a.PaymentProfileId IS NULL OR b.RFOPaymentProfileId__C IS NULL) 


SELECT MissingFrom ,COUNT(*) from Rfoperations.SFDC.PaymentProfilesMissing GROUP BY MISSINGFROM;

SELECT 'Query Rfoperations.sfdc.PaymentProfilesMissing to get list of AccountIDs missing from Source/Destination'

--------------------------------------------------------------------------------------
-- Duplicates 
--------------------------------------------------------------------------------------

SELECT PaymentProfileId, COUNT (RFOPaymentProfileId__C) AS CountofDups
INTO rfoperations.sfdc.PaymentProfile_Dups
FROM Rfoperations.rfo_accounts.AccountBase AB, RFOPERATIONS.RFO_ACCOUNTS.PaymentProfiles Rpp ,sfdcbackup.SFDCBKP.PaymentProfile PP,
										  SFDCBACKUP.SFDCBKP.Accounts A 
									 WHERE PP.ACCOUNT__C=A.ID AND AB.COUNTRYID=40 
								     AND RPP.ACCOUNTID=AB.ACCOUNTID AND RPP.PaymentProfileId=RFOPaymentProfileId__C
GROUP BY PaymentProfileId
HAVING COUNT (RFOPaymentProfileId__C)> 1 


SELECT @RowCount = COUNT(*) FROM rfoperations.sfdc.PaymentProfile_Dups

--SELECT * FROM rfoperations.sfdc.PaymentProfile_Dups
--SELECT * FROM SFDCBACKUP.SFDCBKP.PAYMENTPROFILE WHERE RFOPAYMENTPROFILEID__C='1057811'
--select * from RFOPERATIONS.RFO_ACCOUNTS.PAYMENTPROFILES WHERE PAYMENTPROFILEID=1057811
--SELECT * FROM RFOPERATIONS.RFO_ACCOUNTS.CREDITCARDPROFILES WHERE PAYMENTPROFILEID=1057811

IF @RowCount > 0
	BEGIN 

			SELECT  cast(@ROWCOUNT as nvarchar) + ' Duplicate PaymentProfiles in CRM. Query rfoperations.sfdc.PaymentProfile_Dups to get list of duplicate PaymentProfileIds' 
	END 

ELSE 

SELECT 'No Duplicates'

-------------------------------------------------------------------------------------------
 --Accounts Framework 
-------------------------------------------------------------------------------------------

--Loading RFO Data
SELECT
		CAST(PP.PAYMENTPROFILEID AS NVARCHAR(MAX)) PAYMENTPROFILEID,
		CAST(PP.CHANGEDBYAPPLICATION AS NVARCHAR(MAX)) CHANGEDBYAPPLICATION,
		CAST(AB.AccountID AS NVARCHAR (100)) AS AccountID,
		CAST(SUBSTRING(CCP.DisplayNumber,LEN(CCP.DisplayNumber)-3 ,4 ) AS NVARCHAR(MAX)) DisplayNumber,
		CAST(CCP.ExpMonth AS NVARCHAR(MAX)) ExpMonth,
		CAST(CCP.ExpYear AS NVARCHAR(MAX)) ExpYear,
		REPLACE(CCP.NameOnCard,'  ',' ') AS NameOnCard,
		CCV.NAME AS Vendor,
		CAST(CASE WHEN DATEADD(HH,8,PP.StartDate) IS NULL THEN '1900-01-01' ELSE pp.StartDate END AS DATE) StartDate,
		CAST(CASE WHEN DATEADD(HH,8,PP.EndDate) IS NULL THEN '1900-01-01' ELSE pp.EndDate END AS DATE) EndDate,
		casE WHEN PP.IsDefault= 1 then 'true' ELSE 'false' END AS IsDefault,
		PT.NAME PaymentType,
		CASE WHEN LEN(PP.PROFILENAME)<1 THEN NULL ELSE PP.PROFILENAME END AS PROFILENAME,
		CAST(AA.AddressID AS NVARCHAR(MAX)) RFO_AddressProfileID,
		CAST(AA.AddressLine1 AS NVARCHAR(MAX)) AddressLine1,
		CAST(CASE WHEN LEN(AA.AddressLine2)<1 THEN NULL ELSE AA.AddressLine2 END AS NVARCHAR(MAX)) AddressLine2,
		CAST(CASE WHEN LEN(AA.AddressLine3)<1 THEN NULL ELSE AA.AddressLine3 END AS NVARCHAR(MAX)) AddressLine3,
		CAST(CASE WHEN LEN(AA.AddressLine4)<1 THEN NULL ELSE AA.AddressLine4 END AS NVARCHAR(MAX)) AddressLine4,
		CAST(CASE WHEN LEN(AA.AddressLine5)<1 THEN NULL ELSE AA.AddressLine5 END AS NVARCHAR(MAX)) AddressLine5,
		CAST(C.NAME AS NVARCHAR(MAX)) CountryId,
		CAST(CAST(CASE WHEN LEN(AA.latitude)<1 THEN NULL ELSE AA.latitude END AS DECIMAL(10,1)) AS NVARCHAR(MAX)) latitude,
		CAST(CASE WHEN LEN(AA.locale) <1 THEN NULL ELSE AA.locale END AS NVARCHAR(MAX)) locale,
		CAST(CAST(CASE WHEN LEN(AA.longitude)<1 THEN NULL ELSE AA.longitude END AS DECIMAL(10,1)) AS NVARCHAR(MAX)) longitude,
		CAST(CASE WHEN LEN(AA.Region)<1 THEN NULL ELSE AA.Region END AS NVARCHAR(MAX)) Region,
		CAST(CASE WHEN LEN(AA.PostalCode)<1 THEN NULL ELSE AA.PostalCode END AS NVARCHAR(MAX)) PostalCode,
		CAST(CASE WHEN LEN(AA.SubRegion)< 1 THEN NULL ELSE AA.SubRegion END AS NVARCHAR(MAX)) SubRegion
		INTO RFOPERATIONS.SFDC.RFO_PAYMENTPROFILES
		-- join address table here.
		FROM  RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB
		JOIN RFOperations.RFO_Reference.Countries (NOLOCK) C ON c.CountryID =ab.CountryID  AND AB.COUNTRYID=40
		JOIN RFOperations.RFO_Reference.Currency (NOLOCK) CY ON cy.CurrencyID =ab.CurrencyID 
		JOIN RFOperations.RFO_Accounts.AccountContacts (NOLOCK) AC ON AC.AccountId = AB.AccountID
		JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.ACCOUNTCONTACTID=AC.ACCOUNTCONTACTID
		JOIN RFOPERATIONS.RFO_ACCOUNTS.ADDRESSES AA ON ACA.ADDRESSID=AA.ADDRESSID AND ADDRESSTYPEID=3
		JOIN RFOPERATIONS.RFO_ACCOUNTS.PAYMENTPROFILES PP ON PP.ACCOUNTID=AB.ACCOUNTID
		JOIN RFOPERATIONS.RFO_Accounts.CreditCardProfiles CCP ON CCP.PAYMENTPROFILEID=PP.PAYMENTPROFILEID
		JOIN RFOPERATIONS.RFO_REFERENCE.CREDITCARDVENDORS CCV ON CCV.VENDORID=CCP.VENDORID
		JOIN RFOPERATIONS.RFO_REFERENCE.paymenttype PT ON PT.PAYMENTTYPEID=PP.PAYMENTTYPEID
		WHERE PP.ServerModifiedDate>= @LastRunDate AND
		NOT EXISTS (SELECT 1 FROM rfoperations.sfdc.PaymentProfilesMissing PPM WHERE PPM.RFO_PaymentProfileId=PP.PAYMENTPROFILEID AND missingFrom='Destination')
        
		
	--Loading CRM data
	SELECT
	RFOPaymentProfileId__c,
	PP.ChangedByApplication__c,
	A.RFOAccountID__C as Account__C,
	SUBSTRING(DisplayNumber__c,LEN(DisplayNumber__c)-3,4) AS DisplayNumber__c,
	ExpMonth__c,
	ExpYear__c,
	NameOnCard__c,
	PaymentVendor__c,
	CAST(ISNULL(StartDate__c,'1900-01-01') AS DATE) StartDate__c,
	CAST(ISNULL(EndDate__c,'1900-01-01') AS DATE) EndDate__c,
	IsDefault__c,
	PaymentType__c,
	ProfileName__c,
	PP.RFOAddressProfileId__c,
	PP.AddressLine1__c,
	PP.AddressLine2__c,
	PP.AddressLine3__c,
	PP.AddressLine4__c,
	PP.AddressLine5__c,
	C.NAME AS Country__c,
	PP.Latitude__c,
	PP.Locale__c,
	PP.Longitude__c,
	PP.Region__c,
	PP.Postal_Code__c,
	PP.Sub_Region__c
	INTO RFOPERATIONS.SFDC.CRM_PaymentProfiles
FROM sfdcbackup.SFDCBKP.PaymentProfile PP, SFDCBACKUP.SFDCBKP.COUNTRY C ,
	 SFDCBACKUP.SFDCBKP.Accounts A WHERE PP.ACCOUNT__C=A.ID AND C.ID=PP.Country__c AND C.NAME='Canada'

		
--Load Comparison Candidates.
SELECT * INTO  #PaymentProfiles FROM rfoperations.sfdc.RFO_PaymentProfiles
EXCEPT 
SELECT * FROM rfoperations.sfdc.CRM_PaymentProfiles

CREATE TABLE rfoperations.sfdc.ErrorLog_PaymentProfiles
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, ColID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
,CRM_Value NVARCHAR (MAX)
)


DECLARE @I INT = (SELECT MIN(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'PaymentProfile') , 
@C INT =  (SELECT MAX(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'PaymentProfile') 


DECLARE @DesKey NVARCHAR (50) = 'RFOPaymentProfileID__C'; 

DECLARE @SrcKey NVARCHAR (50) ='PaymentProfileId'

DECLARE @DesTemp NVARCHAR (50) ='RFOPERATIONS.SFDC.CRM_PAYMENTPROFILES' 

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
DECLARE @DesCol NVARCHAR (50) =(SELECT CRM_Column FROM Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)
DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM  Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)

DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.crm_Value = b. ' + @DesCol +
' FROM rfoperations.sfdc.ErrorLog_PaymentProfiles a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.ColID = ' + CAST(@I AS NVARCHAR)

DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO rfoperations.sfdc.ErrorLog_PaymentProfiles (Identifier,ColID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

  BEGIN TRY
--  SELECT @SQL3
   EXEC sp_executesql @SQL3, N'@ServerMod DATETIME', @ServerMod= '2014-05-01'

 SET @I = @I + 1

 END TRY

 BEGIN CATCH

 SELECT 'This SQL Failed' +@SQL3

 SET @I = @I + 1

 END CATCH
END 

END 


SELECT  B.COLID,b.RFO_column, COUNT(*) AS Counts
FROM rfoperations.sfdc.ErrorLog_PaymentProfiles A JOIN Rfoperations.sfdc.CRM_METADATA B ON a.ColID =b.ColID
GROUP BY b.ColID, RFO_Column


END


GO


