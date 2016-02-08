USE RFOPERATIONS;

GO

CREATE PROCEDURE sfdc.VerifySiteMigration @LastRunDate DATETIME ='2000-01-01'

AS
BEGIN
DECLARE @LASTRUNDATE DATETIME='2000-01-01';
DECLARE @ServerMod DATETIME =@LastRunDate
DECLARE @RFOPP BIGINT, @CRMPP BIGINT
DECLARE @RowCount BIGINT 


IF OBJECT_ID('rfoperations.sfdc.SiteDifference') IS NOT NULL  DROP TABLE rfoperations.sfdc.SiteDifference
IF OBJECT_ID('Rfoperations.sfdc.SiteMissing') IS NOT NULL DROP TABLE Rfoperations.sfdc.SiteMissing
IF OBJECT_ID('Rfoperations.sfdc.RFO_Site') IS NOT NULL DROP TABLE Rfoperations.sfdc.RFO_Site
IF OBJECT_ID('Rfoperations.sfdc.CRM_Site') IS NOT NULL DROP TABLE Rfoperations.sfdc.CRM_Site
IF OBJECT_ID('Rfoperations.sfdc.ErrorLog_Site') IS NOT NULL DROP TABLE Rfoperations.sfdc.ErrorLog_Site
IF OBJECT_ID('rfoperations.sfdc.AccountPolicyDifference') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountPolicyDifference
IF OBJECT_ID('rfoperations.sfdc.AccountPolicy_Dups') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountPolicy_Dups
IF OBJECT_ID('tempdb.dbo.#Site') IS NOT NULL DROP TABLE #Site

------------------------------------------------------------------------------------------------------------------------------
-- Accounts 
-----------------------------------------------------------------------------------------------------------------------------
SELECT @RFOPP=COUNT(DISTINCT S.AccountId)  --COUNT( DISTINCT a.AccountID)
FROM    Rfoperations.hybris.sites s , rfoperations.rfo_accounts.accountbase ab where ab.accountid=s.accountid and ab.countryid=40

--SELECT * FROM sfdcbackup.SFDCBKP.AccountSite
SELECT @CRMPP=COUNT(DISTINCT Account__C) FROM sfdcbackup.SFDCBKP.AccountSite AAS, sfdcbackup.SFDCBKP.Accounts S  , sfdcbackup.SFDCBKP.COUNTRY C WHERE AAS.ACCOUNT__C=S.ID AND S.COUNTRY__C=C.ID AND C.NAME='Canada'


										   
SELECT  @RFOPP AS RFO_AccountSiteCount, @CRMPP AS CRM_AccountSiteCount, (@RFOPP - @CRMPP) AS Difference 
INTO rfoperations.sfdc.SiteDifference;

SELECT  AccountId AS RFO_AccountId,
 b.RFOACcountID__C as CRM_AccountId , 
 CASE WHEN b.RFOACcountID__C IS NULL THEN 'Destination'
      WHEN a.AccountId IS NULL THEN 'Source' 
 END AS MissingFROM
INTO Rfoperations.sfdc.SiteMissing
FROM 
    (SELECT S.AccountId FROM Rfoperations.hybris.sites s , rfoperations.rfo_accounts.accountbase ab where ab.accountid=s.accountid and ab.countryid=40 ) a
    FULL OUTER JOIN 
    (SELECT RFOACcountID__C FROM  sfdcbackup.SFDCBKP.AccountSite AP ,sfdcbackup.SFDCBKP.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE AP.Account__C=A.ID AND C.ID = A.COUNTRY__C AND C.NAME='Canada') b 
	ON cast(a.AccountId as nvarchar(max)) =b.RFOACcountID__C
 WHERE (cast(a.AccountId as nvarchar(max)) IS NULL OR b.RFOACcountID__C IS NULL) 


SELECT MissingFrom ,COUNT(*) from Rfoperations.sfdc.SiteMissing GROUP BY MISSINGFROM;

SELECT 'Query Rfoperations.sfdc.SiteMissing to get list of AccountIDs missing from Source/Destination'


SELECT 
S.SiteID as RFOSiteId__c,
S.AccountID as RFOAccountID__C,
SitePrefix as SitePrefix__c,
CAST(DATEADD(HH,(SELECT OFFSET FROM  RFOPERATIONS.SFDC.GMT_DST M WHERE StartDate >= M.DST_START AND StartDate < M.DST_END),StartDate) AS DATE) as StartDate__c,
CAST(DATEADD(HH,(SELECT OFFSET FROM  RFOPERATIONS.SFDC.GMT_DST M WHERE EndDate >= M.DST_START AND EndDate< M.DST_END),EndDate) AS DATE) as EndDate__c,
CAST(DATEADD(HH,(SELECT OFFSET FROM  RFOPERATIONS.SFDC.GMT_DST M WHERE s.ServerModifiedDate >= M.DST_START AND s.ServerModifiedDate< M.DST_END),s.ServerModifiedDate) AS DATE) as ServerModifiedDate__c,
s.ChangedByApplication as ChangedByApplication__c,
s.ChangedByUser as ChangedByUser__c,
CASE WHEN LEN(ISActive__c)< 1 then NULL ELSE IsActive__C end as IsActive__C,
CAST(DATEADD(HH,(SELECT OFFSET FROM  RFOPERATIONS.SFDC.GMT_DST M WHERE Expirationdate >= M.DST_START AND Expirationdate< M.DST_END),Expirationdate) AS DATE) as Expirationdate__c,
PrimaryDomain__C,
BusinessDomain__c,
MailAddress as EMailAddress__c
INTO RFOPERATIONS.SFDC.RFO_Site
FROM Rfoperations.hybris.sites S 
JOIN RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTBASE AB ON AB.ACCOUNTID=S.ACCOUNTID AND AB.COUNTRYID=40
LEFT JOIN Rfoperations.rfo_accounts.SiteMail SM ON S.siteid=sm.siteid
LEFT JOIN 
(SELECT SU.SITEID,MAX(CASE WHEN SU.SITEDOMAINID=1 THEN SD.NAME ELSE NULL END) AS PRIMARYDOMAIN__C,MAX(CASE WHEN SU.SITEDOMAINID=2 THEN SD.NAME ELSE NULL END) AS BusinessDOMAIN__C
 FROM rfoperations.hybris.SiteURLs SU , RFoperations.hybris.sitedomain SD where  SU.SITEDOMAINID=SD.SiteDomainID  GROUP BY SU.SITEID) SD
 ON S.SITEID=SD.SITEID
 WHERE NOT EXISTS (SELECT 1 FROM Rfoperations.sfdc.SiteMissing SD WHERE SD.RFO_AccountId=S.ACCOUNTID and missingfrom='Destination')


SELECT 
RFOSiteId__c,
RFOAccountID__C,
SitePrefix__c,
CAST(StartDate__c AS DATE) StartDate__c,
CAST(EndDate__c AS DATE) EndDate__c,
CAST(site.LastModifiedDate AS DATE) LastModifiedDate,
site.ChangedByApplication__c,
site.ChangedByUser__c,
site.IsActive__c,
CAST(ExpirationDate__c AS DATE) ExpirationDate__c,
PrimaryDomain__c,
BusinessDomain__c,
A.EmailAddress__c
INTO RFOPERATIONS.SFDC.CRM_Site
FROM SFDCBACKUP.SFDCBKP.AccountSite Site , SFDCBACKUP.SFDCBKP.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE Site.ACCOUNT__C=A.ID AND C.ID=A.COUNTRY__C AND C.NAME='Canada'

SELECT * INTO #Site FROM RFOPERATIONS.SFDC.RFO_Site
EXCEPT 
SELECT * FROM RFOPERATIONS.SFDC.CRM_Site

--SELECT * FROM RFOPERATIONS.SFDC.CRM_Site
--SELECT * FROM RFOPERATIONS.SFDC.RFO_Site

CREATE TABLE rfoperations.sfdc.ErrorLog_Site
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, ColID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
, CRM_Value NVARCHAR (MAX)
)


DECLARE @I INT = (SELECT MIN(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'Site') , 
@C INT =  (SELECT MAX(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'Site') 


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

DECLARE @DesKey NVARCHAR (50) = 'RFOAccountID__C'; 
DECLARE @SrcKey NVARCHAR (50) ='RFOAccountID__C';
DECLARE @DesTemp NVARCHAR (50) ='RFOPERATIONS.SFDC.CRM_Site' 
DECLARE @SrcCol NVARCHAR (50) =(SELECT RFO_Column FROM Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)
DECLARE @DesCol NVARCHAR (50) =(SELECT CRM_Column FROM Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)
DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM  Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)
DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.CRM_Value = b. ' + @DesCol +
' FROM rfoperations.sfdc.ErrorLog_Site a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.ColID = ' + CAST(@I AS NVARCHAR)



DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO rfoperations.sfdc.ErrorLog_Site (Identifier,ColID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

  BEGIN TRY
  SELECT @SQL3
   EXEC sp_executesql @SQL3, N'@ServerMod DATETIME', @ServerMod= '2014-05-01'

 SET @I = @I + 1

 END TRY

 BEGIN CATCH

 SELECT ' This SQL Failed' + @SQL3

 SET @I = @I + 1

 END CATCH
END 

END 



SELECT  B.COLID,b.RFO_column, COUNT(*) AS Counts
FROM rfoperations.sfdc.ErrorLog_Site A JOIN Rfoperations.sfdc.CRM_METADATA B ON a.ColID =b.ColID
GROUP BY b.ColID, RFO_Column



end

GO