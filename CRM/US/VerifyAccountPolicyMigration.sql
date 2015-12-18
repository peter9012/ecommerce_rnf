USE RFOPERATIONS

GO

CREATE PROCEDURE VerifyAccountPolicyMigration @LastRunDate DATETIME  ='2000-01-01'
AS
BEGIN

IF OBJECT_ID('CRM.SFDC.AccountPolicyMissing') IS NOT NULL  DROP TABLE CRM.SFDC.AccountPolicyMissing
IF OBJECT_ID('CRM.SFDC.CRM_Policy') IS NOT NULL DROP TABLE CRM.SFDC.CRM_Policy
IF OBJECT_ID('CRM.SFDC.RFO_Policy') IS NOT NULL DROP TABLE CRM.SFDC.RFO_Policy
IF OBJECT_ID('CRM.SFDC.ErrorLog_AccountPolicy') IS NOT NULL DROP TABLE CRM.SFDC.ErrorLog_AccountPolicy
IF OBJECT_ID('CRM.SFDC.AccountPolicyDifference') IS NOT NULL DROP TABLE CRM.SFDC.AccountPolicyDifference
IF OBJECT_ID('CRM.SFDC.AccountPolicy_Dups') IS NOT NULL DROP TABLE CRM.SFDC.AccountPolicy_Dups
IF OBJECT_ID('tempdb.dbo.#Policy') IS NOT NULL DROP TABLE #Policy

SET ANSI_WARNINGS OFF 

DECLARE @ServerMod DATETIME =@LastRunDate
DECLARE @RFOPP BIGINT, @CRMPP BIGINT

DECLARE @RowCount BIGINT 

------------------------------------------------------------------------------------------------------------------------------
-- Accounts 
-----------------------------------------------------------------------------------------------------------------------------
SELECT @RFOPP=COUNT(DISTINCT ACL.AccountId)  --COUNT( DISTINCT a.AccountID)
FROM    RFOperations.RFO_Accounts.AccountPolicy acl , RFOPERATIONS.rfo_accounts.accountbase ab WHERE AB.ACCOUNTID=ACL.ACCOUNTID AND AB.COUNTRYID=236

SELECT @CRMPP=COUNT(DISTINCT Account__C) FROM sfdcbackup.SFDCBKP.AccountPolicy AP , SFDCBACKUP.SFDCBKP.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE AP.ACCOUNT__C=A.ID AND A.COUNTRY__C=C.ID AND C.NAME='United States' 

										   
SELECT  @RFOPP AS RFO_AccountPolicyCount, @CRMPP AS CRM_AccountPolicyCount, (@RFOPP - @CRMPP) AS Difference 
INTO CRM.SFDC.AccountPolicyDifference;

SELECT  AccountId AS RFO_AccountId,
 b.RFOACcountID__C as CRM_AccountId , 
 CASE WHEN b.RFOACcountID__C IS NULL THEN 'Destination'
      WHEN a.AccountId IS NULL THEN 'Source' 
 END AS MissingFROM
INTO CRM.SFDC.AccountPolicyMissing
FROM 
    (SELECT ACL.AccountId FROM RFOperations.RFO_Accounts.AccountPolicy acl , RFOPERATIONS.rfo_accounts.accountbase ab WHERE AB.ACCOUNTID=ACL.ACCOUNTID AND AB.COUNTRYID=236) a
    FULL OUTER JOIN 
    (SELECT RFOACcountID__C FROM  sfdcbackup.SFDCBKP.AccountPolicy AP ,sfdcbackup.SFDCBKP.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE AP.Account__C=A.ID and a.country__c=c.id and c.name='United States') b 
	ON cast(a.AccountId as nvarchar(max)) =b.RFOACcountID__C
 WHERE (cast(a.AccountId as nvarchar(max)) IS NULL OR b.RFOACcountID__C IS NULL) 


SELECT MissingFrom ,COUNT(*) from CRM.SFDC.AccountPolicyMissing GROUP BY MISSINGFROM;

SELECT 'Query CRM.SFDC.AccountPolicyMissing to get list of AccountIDs missing from Source/Destination'

-------------------------------------------------------------------------------------------
 --Account Policy Framework 
-------------------------------------------------------------------------------------------

--Loading RFO Data
		SELECT
		CAST(aB.AccountID AS NVARCHAR(MAX)) AS Account__c,
		CAST(ACL.PolicyId AS NVARCHAR(MAX)) as Policy__c,
		CAST(DATEADD(HH,(SELECT OFFSET FROM  CRM.SFDC.GMT_DST M WHERE ACL.DateAccepted >= M.DST_START AND ACL.DateAccepted < M.DST_END),ACL.DateAccepted) AS DATE) as DateAccepted__C,
		ACL.ChangedByApplication as ChangedByApplication__C,
		ACL.ChangedByUser as ChangedByUser__C
		INTO CRM.SFDC.RFO_Policy
		FROM  Rfoperations.RFO_accounts.AccountPolicy ACL, RFOPERATIONS.rfo_accounts.accountbase ab WHERE AB.ACCOUNTID=ACL.ACCOUNTID AND AB.COUNTRYID=236
        AND NOT EXISTS (SELECT 1 FROM CRM.SFDC.AccountPolicyMissing APL WHERE APL.RFO_ACCOUNTID=ACL.ACCOUNTID AND MISSINGFROM='Destination')        
		
		--Loading CRM data
		SELECT
		A.RFOAccountId__c as Account__C,
		Policy__c as Policy__c,
		CAST(DateAccepted__c AS DATE) as DateAccepted__C,
		PP.ChangedByApplication__c as ChangedByApplication__c,
		PP.ChangedByUser__c as ChangedByUser__c
		INTO CRM.SFDC.CRM_Policy
		FROM sfdcbackup.SFDCBKP.AccountPolicy PP,SFDCBACKUP.SFDCBKP.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE PP.ACCOUNT__C=A.ID AND A.COUNTRY__C=C.ID AND C.NAME='United States'


--Load Comparison Candidates.
SELECT * INTO  #Policy FROM CRM.SFDC.RFO_Policy
EXCEPT 
SELECT * FROM CRM.SFDC.CRM_Policy

--DROP TABLE #poLICY
--SELECT * from CRM.SFDC.CRM_Policy

CREATE TABLE CRM.SFDC.ErrorLog_AccountPolicy
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, ColID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
,CRM_Value NVARCHAR (MAX)
)


DECLARE @I INT = (SELECT MIN(ColID) FROM  CRM.SFDC.CRM_METADATA WHERE CRMObject = 'Policy') , 
@C INT =  (SELECT MAX(ColID) FROM  CRM.SFDC.CRM_METADATA WHERE CRMObject = 'Policy') 


DECLARE @DesKey NVARCHAR (50) = 'Account__C'; 

DECLARE @SrcKey NVARCHAR (50) ='Account__C'

DECLARE @DesTemp NVARCHAR (50) ='CRM.SFDC.CRM_Policy' 

DECLARE @Skip  BIT 

WHILE (@I <=@c)

BEGIN 

        SELECT  @Skip = ( SELECT   Skip
                               FROM     CRM.SFDC.CRM_METADATA
                               WHERE    ColID = @I
                             );


        IF ( @Skip = 1 )

            SET @I = @I + 1;

        ELSE
BEGIN 



DECLARE @SrcCol NVARCHAR (50) =(SELECT RFO_Column FROM CRM.SFDC.CRM_METADATA WHERE ColID = @I)
DECLARE @DesCol NVARCHAR (50) =(SELECT CRM_Column FROM CRM.SFDC.CRM_METADATA WHERE ColID = @I)
DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM  CRM.SFDC.CRM_METADATA WHERE ColID = @I)

DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.crm_Value = b. ' + @DesCol +
' FROM CRM.SFDC.ErrorLog_AccountPolicy a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.ColID = ' + CAST(@I AS NVARCHAR)

DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO CRM.SFDC.ErrorLog_AccountPolicy (Identifier,ColID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

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
FROM CRM.SFDC.ErrorLog_AccountPolicy A JOIN CRM.SFDC.CRM_METADATA B ON a.ColID =b.ColID
GROUP BY b.ColID, RFO_Column

END

GO