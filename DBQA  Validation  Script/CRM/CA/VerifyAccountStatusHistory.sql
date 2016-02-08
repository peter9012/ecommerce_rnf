USE RFOPERATIONS

GO

CREATE PROCEDURE Dbo.VerifyAccountStatusHistory @LastRunDate DATETIME = '2000-01-01'
AS

BEGIN

DECLARE @LASTRUNDATE DATETIME='2000-01-01';

IF OBJECT_ID('CRM.sfdc.AccountStatusHistoryMissing') IS NOT NULL  DROP TABLE CRM.sfdc.AccountStatusHistoryMissing
IF OBJECT_ID('CRM.sfdc.CRM_AccountStatusHistory') IS NOT NULL DROP TABLE CRM.sfdc.CRM_AccountStatusHistory
IF OBJECT_ID('CRM.sfdc.RFO_AccountStatusHistory') IS NOT NULL DROP TABLE CRM.sfdc.RFO_AccountStatusHistory
IF OBJECT_ID('CRM.sfdc.ErrorLog_AccountStatusHistory') IS NOT NULL DROP TABLE CRM.sfdc.ErrorLog_AccountStatusHistory
IF OBJECT_ID('CRM.sfdc.AccountStatusHistoryDifference') IS NOT NULL DROP TABLE CRM.sfdc.AccountStatusHistoryDifference
IF OBJECT_ID('CRM.sfdc.AccountStatusHistory_Dups') IS NOT NULL DROP TABLE CRM.sfdc.AccountStatusHistory_Dups
IF OBJECT_ID('tempdb.dbo.#AccountStatusHistory') IS NOT NULL DROP TABLE #AccountStatusHistory

SET ANSI_WARNINGS OFF 

DECLARE @ServerMod DATETIME =@LastRunDate
DECLARE @RFOPP BIGINT, @CRMPP BIGINT

DECLARE @RowCount BIGINT 

------------------------------------------------------------------------------------------------------------------------------
-- Accounts 
-----------------------------------------------------------------------------------------------------------------------------
SELECT @RFOPP=COUNT(DISTINCT ACL.AccountId)  --COUNT( DISTINCT a.AccountID)
FROM    RFOperations.logging.AccountChangeLog ACL , RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTBASE AB WHERE ACL.ACCOUNTID=AB.ACCOUNTID AND AB.COUNTRYID=40

SELECT @CRMPP=COUNT(Account__C) FROM sfdcbackup.SFDCBKP.AccountStatus AAS, SFDCBACKUP.SFDCBKP.ACCOUNTS A , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE AAS.ACCOUNT__C=A.ID AND A.COUNTRY__C=C.ID AND C.NAME='Canada'

--SELECT * FROM sfdcbackup.SFDCBKP.AccountStatus
										   
SELECT  @RFOPP AS RFO_AccountStatusHistoryCount, @CRMPP AS CRM_AccountStatusHistoryCount, (@RFOPP - @CRMPP) AS Difference 
INTO CRM.sfdc.AccountStatusHistoryDifference;

SELECT  AccountId AS RFO_AccountId,
 b.RFOACcountID__C as CRM_AccountId , 
 CASE WHEN b.RFOACcountID__C IS NULL THEN 'Destination'
      WHEN a.AccountId IS NULL THEN 'Source' 
 END AS MissingFROM
INTO CRM.sfdc.AccountStatusHistoryMissing
FROM 
    (SELECT ACL.AccountId FROM RFOperations.logging.AccountChangeLog ACL, RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTBASE AB WHERE ACL.ACCOUNTID=AB.ACCOUNTID AND AB.COUNTRYID=40) a
    FULL OUTER JOIN 
    (SELECT A.RFOACcountID__C FROM  sfdcbackup.SFDCBKP.AccountStatus AP ,sfdcbackup.SFDCBKP.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE AP.Account__C=A.ID AND A.COUNTRY__C=C.ID AND C.NAME='Canada') b 
	ON cast(a.AccountId as nvarchar(max)) =b.RFOACcountID__C
 WHERE (cast(a.AccountId as nvarchar(max)) IS NULL OR b.RFOACcountID__C IS NULL) 


SELECT MissingFrom ,COUNT(*) from CRM.sfdc.AccountStatusHistoryMissing GROUP BY MISSINGFROM;

SELECT 'Query CRM.sfdc.AccountStatusHistoryMissing to get list of AccountIDs missing from Source/Destination'

-------------------------------------------------------------------------------------------
 --Account Status Framework 
-------------------------------------------------------------------------------------------

--Loading RFO Data
		SELECT
		CAST(ACL.AccountID AS NVARCHAR(MAX)) AS AccountId,
		AAS.NAME as AccountStatus__C,
		RT.NAME AS Reason__C,
		CAST(DATEADD(HH,8,ACL.ServerModifiedDate) AS DATE) ServerModifiedDate, 
		ACL.ChangedByApplication as ChangedByApplication__C,
		ACL.ChangedByUser as ChangedByUser__C
		INTO CRM.sfdc.RFO_AccountStatusHistory
		-- join address table here.
		FROM  Rfoperations.logging.AccountChangeLog ACL
		JOIN RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTBASE AB ON AB.ACCOUNTID=ACL.ACCOUNTID AND AB.COUNTRYID=40
		LEFT JOIN RFOPERATIONS.RFO_REFERENCE.ACCOUNTSTATUS AAS ON AAS.ACCOUNTSTATUSID=ACL.ACCOUNTSTATUSID
		LEFT JOIN RFOPERATIONS.RFO_REFERENCE.REASONTYPE RT ON ACL.REASONID=RT.REASONtypeID

        
		
		--Loading CRM data
		SELECT
		RFOAccountid__C,
		PP.AccountStatus__c,
		Reason__c,
		CAST(PP.LastModifiedDate AS DATE) LastModifiedDate,	
		PP.ChangedByApplication__c,
		PP.ChangedByUser__c
		INTO CRM.sfdc.CRM_AccountStatusHistory
		FROM sfdcbackup.SFDCBKP.AccountStatus PP,SFDCBACKUP.SFDCBKP.Accounts A  , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE PP.ACCOUNT__C=A.ID AND A.COUNTRY__C=C.ID AND C.NAME='Canada'

		
--Load Comparison Candidates.
SELECT * INTO  #AccountStatusHistory FROM CRM.sfdc.RFO_AccountStatusHistory
EXCEPT 
SELECT * FROM CRM.sfdc.CRM_AccountStatusHistory

--SELECT * from #AccountStatusHistory

CREATE TABLE CRM.sfdc.ErrorLog_AccountStatusHistory
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, ColID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
,CRM_Value NVARCHAR (MAX)
)


DECLARE @I INT = (SELECT MIN(ColID) FROM  CRM.sfdc.CRM_METADATA WHERE CRMObject = 'AccountStatusHistory') , 
@C INT =  (SELECT MAX(ColID) FROM  CRM.sfdc.CRM_METADATA WHERE CRMObject = 'AccountStatusHistory') 


DECLARE @DesKey NVARCHAR (50) = 'RFOAccountID__C'; 

DECLARE @SrcKey NVARCHAR (50) ='AccountId'

DECLARE @DesTemp NVARCHAR (50) ='CRM.sfdc.CRM_AccountStatusHistory' 

DECLARE @Skip  BIT 

WHILE (@I <=@c)

BEGIN 

        SELECT  @Skip = ( SELECT   Skip
                               FROM     CRM.sfdc.CRM_METADATA
                               WHERE    ColID = @I
                             );


        IF ( @Skip = 1 )

            SET @I = @I + 1;

        ELSE
BEGIN 



DECLARE @SrcCol NVARCHAR (50) =(SELECT RFO_Column FROM CRM.sfdc.CRM_METADATA WHERE ColID = @I)
DECLARE @DesCol NVARCHAR (50) =(SELECT CRM_Column FROM CRM.sfdc.CRM_METADATA WHERE ColID = @I)
DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM  CRM.sfdc.CRM_METADATA WHERE ColID = @I)

DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.crm_Value = b. ' + @DesCol +
' FROM CRM.sfdc.ErrorLog_AccountStatusHistory a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.ColID = ' + CAST(@I AS NVARCHAR)

DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO CRM.sfdc.ErrorLog_AccountStatusHistory (Identifier,ColID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

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
FROM CRM.sfdc.ErrorLog_AccountStatusHistory A JOIN CRM.sfdc.CRM_METADATA B ON a.ColID =b.ColID
GROUP BY b.ColID, RFO_Column

END


GO


