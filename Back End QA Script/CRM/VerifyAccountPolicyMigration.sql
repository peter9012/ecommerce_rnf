USE RFOPERATIONS

GO

CREATE PROCEDURE VerifyAccountPolicyMigration @LastRunDate DATETIME  ='2000-01-01'

AS

BEGIN

IF OBJECT_ID('Rfoperations.sfdc.AccountPolicyMissing') IS NOT NULL  DROP TABLE Rfoperations.sfdc.AccountPolicyMissing
IF OBJECT_ID('Rfoperations.sfdc.CRM_Policy') IS NOT NULL DROP TABLE Rfoperations.sfdc.CRM_Policy
IF OBJECT_ID('Rfoperations.sfdc.RFO_Policy') IS NOT NULL DROP TABLE Rfoperations.sfdc.RFO_Policy
IF OBJECT_ID('Rfoperations.sfdc.ErrorLog_AccountPolicy') IS NOT NULL DROP TABLE Rfoperations.sfdc.ErrorLog_AccountPolicy
IF OBJECT_ID('rfoperations.sfdc.AccountPolicyDifference') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountPolicyDifference
IF OBJECT_ID('rfoperations.sfdc.AccountPolicy_Dups') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountPolicy_Dups
IF OBJECT_ID('tempdb.dbo.#Policy') IS NOT NULL DROP TABLE #Policy

SET ANSI_WARNINGS OFF 

DECLARE @ServerMod DATETIME =@LastRunDate
DECLARE @RFOPP BIGINT, @CRMPP BIGINT

DECLARE @RowCount BIGINT 

------------------------------------------------------------------------------------------------------------------------------
-- Accounts 
-----------------------------------------------------------------------------------------------------------------------------
SELECT @RFOPP=COUNT(DISTINCT AccountId)  --COUNT( DISTINCT a.AccountID)
FROM    RFOperations.RFO_Accounts.AccountPolicy ACL

SELECT @CRMPP=COUNT(DISTINCT Account__C) FROM sfdcbackup.SFDCBKP.AccountPolicy

										   
SELECT  @RFOPP AS RFO_AccountPolicyCount, @CRMPP AS CRM_AccountPolicyCount, (@RFOPP - @CRMPP) AS Difference 
INTO rfoperations.sfdc.AccountPolicyDifference;

SELECT  AccountId AS RFO_AccountId,
 b.RFOACcountID__C as CRM_AccountId , 
 CASE WHEN b.RFOACcountID__C IS NULL THEN 'Destination'
      WHEN a.AccountId IS NULL THEN 'Source' 
 END AS MissingFROM
INTO Rfoperations.sfdc.AccountPolicyMissing
FROM 
    (SELECT AccountId FROM RFOperations.RFO_Accounts.AccountPolicy ) a
    FULL OUTER JOIN 
    (SELECT RFOACcountID__C FROM  sfdcbackup.SFDCBKP.AccountPolicy AP ,sfdcbackup.SFDCBKP.Accounts A WHERE AP.Account__C=A.ID) b 
	ON cast(a.AccountId as nvarchar(max)) =b.RFOACcountID__C
 WHERE (cast(a.AccountId as nvarchar(max)) IS NULL OR b.RFOACcountID__C IS NULL) 


SELECT MissingFrom ,COUNT(*) from Rfoperations.SFDC.AccountPolicyMissing GROUP BY MISSINGFROM;

SELECT 'Query Rfoperations.sfdc.AccountPolicyMissing to get list of AccountIDs missing from Source/Destination'

-------------------------------------------------------------------------------------------
 --Account Policy Framework 
-------------------------------------------------------------------------------------------

--Loading RFO Data
		SELECT
		CAST(AccountID AS NVARCHAR(MAX)) AS Account__c,
		ACL.PolicyId as Policy__c,
		CAST(ACL.DateAccepted AS DATE) as DateAccepted__C,
		ACL.ChangedByApplication as ChangedByApplication__C,
		ACL.ChangedByUser as ChangedByUser__C
		INTO RFOPERATIONS.SFDC.RFO_Policy
		FROM  Rfoperations.RFO_accounts.AccountPolicy ACL
        WHERE NOT EXISTS (SELECT 1 FROM RFOPERATIONS.SFDC.AccountPolicyMissing APL WHERE APL.RFO_ACCOUNTID=ACL.ACCOUNTID AND MISSINGFROM='Destination')        
		
		--Loading CRM data
		SELECT
		A.RFOAccountId__c as Account__C,
		Policy__c as Policy__c,
		CAST(DateAccepted__c AS DATE) as DateAccepted__C,
		PP.ChangedByApplication__c as ChangedByApplication__c,
		PP.ChangedByUser__c as ChangedByUser__c
		INTO RFOPERATIONS.SFDC.CRM_Policy
		FROM sfdcbackup.SFDCBKP.AccountPolicy PP,SFDCBACKUP.SFDCBKP.Accounts A WHERE PP.ACCOUNT__C=A.ID 


--Load Comparison Candidates.
SELECT * INTO  #Policy FROM rfoperations.sfdc.RFO_Policy
EXCEPT 
SELECT * FROM rfoperations.sfdc.CRM_Policy

--DROP TABLE #poLICY
--SELECT * from rfoperations.sfdc.CRM_Policy

CREATE TABLE rfoperations.sfdc.ErrorLog_AccountPolicy
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, ColID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
,CRM_Value NVARCHAR (MAX)
)


DECLARE @I INT = (SELECT MIN(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'Policy') , 
@C INT =  (SELECT MAX(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'Policy') 


DECLARE @DesKey NVARCHAR (50) = 'Account__C'; 

DECLARE @SrcKey NVARCHAR (50) ='Account__C'

DECLARE @DesTemp NVARCHAR (50) ='RFOPERATIONS.SFDC.CRM_Policy' 

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
' FROM rfoperations.sfdc.ErrorLog_AccountPolicy a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.ColID = ' + CAST(@I AS NVARCHAR)

DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO rfoperations.sfdc.ErrorLog_AccountPolicy (Identifier,ColID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

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
FROM rfoperations.sfdc.ErrorLog_AccountPolicy A JOIN Rfoperations.sfdc.CRM_METADATA B ON a.ColID =b.ColID
GROUP BY b.ColID, RFO_Column

END

GO