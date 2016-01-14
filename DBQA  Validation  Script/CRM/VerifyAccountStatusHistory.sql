USE RFOPERATIONS

GO

CREATE PROCEDURE Dbo.VerifyAccountStatusHistory @LastRunDate DATETIME = '2000-01-01'
AS

BEGIN

IF OBJECT_ID('Rfoperations.sfdc.AccountStatusHistoryMissing') IS NOT NULL  DROP TABLE Rfoperations.sfdc.AccountStatusHistoryMissing
IF OBJECT_ID('Rfoperations.sfdc.CRM_AccountStatusHistory') IS NOT NULL DROP TABLE Rfoperations.sfdc.CRM_AccountStatusHistory
IF OBJECT_ID('Rfoperations.sfdc.RFO_AccountStatusHistory') IS NOT NULL DROP TABLE Rfoperations.sfdc.RFO_AccountStatusHistory
IF OBJECT_ID('Rfoperations.sfdc.ErrorLog_AccountStatusHistory') IS NOT NULL DROP TABLE Rfoperations.sfdc.ErrorLog_AccountStatusHistory
IF OBJECT_ID('rfoperations.sfdc.AccountStatusHistoryDifference') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountStatusHistoryDifference
IF OBJECT_ID('rfoperations.sfdc.AccountStatusHistory_Dups') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountStatusHistory_Dups
IF OBJECT_ID('tempdb.dbo.#AccountStatusHistory') IS NOT NULL DROP TABLE #AccountStatusHistory

SET ANSI_WARNINGS OFF 

DECLARE @ServerMod DATETIME =@LastRunDate
DECLARE @RFOPP BIGINT, @CRMPP BIGINT

DECLARE @RowCount BIGINT 

------------------------------------------------------------------------------------------------------------------------------
-- Accounts 
-----------------------------------------------------------------------------------------------------------------------------
SELECT @RFOPP=COUNT(DISTINCT AccountId)  --COUNT( DISTINCT a.AccountID)
FROM    RFOperations.logging.AccountChangeLog ACL

SELECT @CRMPP=COUNT(Account__C) FROM sfdcbackup.SFDCBKP.AccountStatus

										   
SELECT  @RFOPP AS RFO_AccountStatusHistoryCount, @CRMPP AS CRM_AccountStatusHistoryCount, (@RFOPP - @CRMPP) AS Difference 
INTO rfoperations.sfdc.AccountStatusHistoryDifference;

SELECT  AccountId AS RFO_AccountId,
 b.RFOACcountID__C as CRM_AccountId , 
 CASE WHEN b.RFOACcountID__C IS NULL THEN 'Destination'
      WHEN a.AccountId IS NULL THEN 'Source' 
 END AS MissingFROM
INTO Rfoperations.sfdc.AccountStatusHistoryMissing
FROM 
    (SELECT AccountId FROM RFOperations.logging.AccountChangeLog ) a
    FULL OUTER JOIN 
    (SELECT RFOACcountID__C FROM  sfdcbackup.SFDCBKP.AccountStatus AP ,sfdcbackup.SFDCBKP.Accounts A WHERE AP.Account__C=A.ID) b 
	ON cast(a.AccountId as nvarchar(max)) =b.RFOACcountID__C
 WHERE (cast(a.AccountId as nvarchar(max)) IS NULL OR b.RFOACcountID__C IS NULL) 


SELECT MissingFrom ,COUNT(*) from Rfoperations.SFDC.AccountStatusHistoryMissing GROUP BY MISSINGFROM;

SELECT 'Query Rfoperations.sfdc.AccountStatusHistoryMissing to get list of AccountIDs missing from Source/Destination'

-------------------------------------------------------------------------------------------
 --Account Status Framework 
-------------------------------------------------------------------------------------------

--Loading RFO Data
		SELECT
		CAST(AccountID AS NVARCHAR(MAX)) AS AccountId,
		AAS.NAME as AccountStatus__C,
		RT.NAME AS Reason__C,
		CAST(ACL.ServerModifiedDate AS DATE) ServerModifiedDate, 
		ACL.ChangedByApplication as ChangedByApplication__C,
		ACL.ChangedByUser as ChangedByUser__C
		INTO RFOPERATIONS.SFDC.RFO_AccountStatusHistory
		-- join address table here.
		FROM  Rfoperations.logging.AccountChangeLog ACL, RFOPERATIONS.RFO_REFERENCE.ACCOUNTSTATUS AAS , RFOPERATIONS.RFO_REFERENCE.REASONTYPE RT
		WHERE AAS.ACCOUNTSTATUSID=ACL.ACCOUNTSTATUSID AND ACL.REASONID=RT.REASONtypeID
        
		
		--Loading CRM data
		SELECT
		RFOAccountid__C,
		PP.AccountStatus__c,
		Reason__c,
		CAST(PP.LastModifiedDate AS DATE) LastModifiedDate,	
		PP.ChangedByApplication__c,
		ChangedByUser__c
		INTO RFOPERATIONS.SFDC.CRM_AccountStatusHistory
		FROM sfdcbackup.SFDCBKP.AccountStatus PP,SFDCBACKUP.SFDCBKP.Accounts A WHERE PP.ACCOUNT__C=A.ID 

		
--Load Comparison Candidates.
SELECT * INTO  #AccountStatusHistory FROM rfoperations.sfdc.RFO_AccountStatusHistory
EXCEPT 
SELECT * FROM rfoperations.sfdc.CRM_AccountStatusHistory

--SELECT * from #AccountStatusHistory

CREATE TABLE rfoperations.sfdc.ErrorLog_AccountStatusHistory
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, ColID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
,CRM_Value NVARCHAR (MAX)
)


DECLARE @I INT = (SELECT MIN(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'AccountStatusHistory') , 
@C INT =  (SELECT MAX(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'AccountStatusHistory') 


DECLARE @DesKey NVARCHAR (50) = 'RFOAccountID__C'; 

DECLARE @SrcKey NVARCHAR (50) ='AccountId'

DECLARE @DesTemp NVARCHAR (50) ='RFOPERATIONS.SFDC.CRM_AccountStatusHistory' 

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
' FROM rfoperations.sfdc.ErrorLog_AccountStatusHistory a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.ColID = ' + CAST(@I AS NVARCHAR)

DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO rfoperations.sfdc.ErrorLog_AccountStatusHistory (Identifier,ColID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

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
FROM rfoperations.sfdc.ErrorLog_AccountStatusHistory A JOIN Rfoperations.sfdc.CRM_METADATA B ON a.ColID =b.ColID
GROUP BY b.ColID, RFO_Column

END


GO


