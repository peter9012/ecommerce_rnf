USE RFOPERATIONS

GO

CREATE PROCEDURE Dbo.VerifyAccountNotes @LastRunDate DATETIME
AS

BEGIN

IF OBJECT_ID('Rfoperations.sfdc.AccountNotesMissing') IS NOT NULL  DROP TABLE Rfoperations.sfdc.AccountNotesMissing
IF OBJECT_ID('Rfoperations.sfdc.CRM_AccountNotes') IS NOT NULL DROP TABLE Rfoperations.sfdc.CRM_AccountNotes
IF OBJECT_ID('Rfoperations.sfdc.RFO_AccountNotes') IS NOT NULL DROP TABLE Rfoperations.sfdc.RFO_AccountNotes
IF OBJECT_ID('Rfoperations.sfdc.ErrorLog_AccountNotes') IS NOT NULL DROP TABLE Rfoperations.sfdc.ErrorLog_AccountNotes
IF OBJECT_ID('rfoperations.sfdc.AccountNotesDifference') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountNotesDifference
IF OBJECT_ID('rfoperations.sfdc.AccountNotes_Dups') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountNotes_Dups
IF OBJECT_ID('tempdb.dbo.#AccountNotes') IS NOT NULL DROP TABLE #AccountNotes

SET ANSI_WARNINGS OFF 

DECLARE @ServerMod DATETIME =@LastRunDate
DECLARE @RFOPP BIGINT, @CRMPP BIGINT

DECLARE @RowCount BIGINT 

------------------------------------------------------------------------------------------------------------------------------
-- Accounts 
-----------------------------------------------------------------------------------------------------------------------------
SELECT @RFOPP=COUNT(DISTINCT AN.AccountNoteId)  --COUNT( DISTINCT a.AccountID)
FROM    RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB
		JOIN RFOPERATIONS.RFO_ACCOUNTS.AccountNotes AN ON AN.AccountId=AB.Accountid AND ab.CountryID =40
		LEFT JOIN RFOPERATIONS.RFO_REFERENCE.NotesReasonType NR on NR.ReasonTypeId=AN.ReasonTypeId
		left JOIN RFOPERATIONS.RFO_REFERENCE.NotesChannelType NC on NC.ChannelTypeId=AN.ChannelTypeId
		left JOIN RFOPERATIONS.RFO_REFERENCE.NotesDetailType ND on ND.DetailTypeId=AN.DetailTypeId
		AND AN.ServerModifiedDate>=@LastRunDate
		

--SELECT * FROM RFOPERATIONS.RFO_ACCOUNTS.AccountNotes
SELECT @CRMPP=COUNT(RFOAccountNotesId__c) FROM sfdcbackup.SFDCBKP.AccountNotes PP,
										  SFDCBACKUP.SFDCBKP.Accounts A ,
										  SFDCBACKUP.SFDCBKP.COUNTRY C
								 		  WHERE PP.ACCOUNT__C=A.ID AND
										  CAST(PP.LASTMODIFIEDDATE AS DATE) >=	@LastRunDate AND C.ID=A.COUNTRY__C AND C.NAME='Canada'


SELECT  @RFOPP AS RFO_AccountNotesCount, @CRMPP AS CRM_AccountNotesCount, (@RFOPP - @CRMPP) AS Difference 
INTO  rfoperations.sfdc.AccountNotesDifference;

SELECT * FROM rfoperations.sfdc.AccountNotesDifference

SELECT  AccountNoteId AS RFO_AccountNoteId,
 b.RFOAccountNotesId__c , 
 CASE WHEN b.RFOAccountNotesId__c IS NULL THEN 'Destination'
      WHEN a.AccountNoteId IS NULL THEN 'Source' 
 END AS MissingFROM
INTO Rfoperations.sfdc.AccountNotesMissing
FROM 
    (SELECT AccountNoteId FROM RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB JOIN RFOPERATIONS.RFO_ACCOUNTS.AccountNotes AN ON AN.AccountId=AB.Accountid AND AN.ServerModifiedDate>=@LastRunDate and ab.countryid=40 ) a
    FULL OUTER JOIN 
    (SELECT RFOAccountNotesId__c FROM  sfdcbackup.SFDCBKP.AccountNotes PP,SFDCBACKUP.SFDCBKP.Accounts A  , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE PP.ACCOUNT__C=A.ID AND CAST(PP.LASTMODIFIEDDATE AS DATE) >=	@LastRunDate AND A.COUNTRY__C=C.ID AND C.NAME='Canada') b 
	ON a.AccountNoteId =b.RFOAccountNotesId__c
 WHERE (a.AccountNoteId IS NULL OR b.RFOAccountNotesId__c IS NULL) 

 --SELECT * FROM Rfoperations.sfdc.AccountNotesMissing WHERE MISSINGFROM='Destination'

SELECT MissingFrom ,COUNT(*) from Rfoperations.SFDC.AccountNotesMissing GROUP BY MISSINGFROM;

SELECT 'Query Rfoperations.sfdc.AccountNotesMissing to get list of AccountIDs missing from Source/Destination'

--------------------------------------------------------------------------------------
-- Duplicates 
--------------------------------------------------------------------------------------

SELECT AccountNoteId, COUNT (RFOAccountNotesId__c) AS CountofDups
INTO rfoperations.sfdc.AccountNotes_Dups
	 FROM RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB , RFOPERATIONS.RFO_ACCOUNTS.AccountNotes AN ,
	 sfdcbackup.SFDCBKP.AccountNotes PP, SFDCBACKUP.SFDCBKP.Accounts A WHERE PP.ACCOUNT__C=A.ID 
	 and AN.AccountId=AB.Accountid AND AN.ServerModifiedDate>=@LastRunDate AND PP.RFOAccountNotesId__c=AN.ACCOUNTNOTEID
	 AND AB.COUNTRYID=40
GROUP BY AccountNoteId
HAVING COUNT (RFOAccountNotesId__c)> 1 


SELECT @RowCount = COUNT(*) FROM rfoperations.sfdc.AccountNotes_Dups

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
		CAST(AccountNoteId AS NVARCHAR(MAX)) as AccountNoteId,
		CAST(AN.AccountId  AS NVARCHAR(MAX)) as Account__c,
		an.Notes as Description__C,
		DATEADD(HH,(SELECT OFFSET FROM  RFOPERATIONS.SFDC.GMT_DST M WHERE AN.EffectiveDate >= M.DST_START AND AN.EffectiveDate < M.DST_END),AN.EffectiveDate) as ActivityDate__c,
		NR.Name NotesReasonType__c,
		nc.name as ChannelType__c,
		ND.NAME AS NotesDetailType__c,
		AN.ChangedByApplication  ChangedByApplication__c,
		AN.ChangedByUser as ChangedByUser__c
		INTO RFOPERATIONS.SFDC.RFO_AccountNotes
		-- join address table here.
		FROM  RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB
		JOIN RFOPERATIONS.RFO_ACCOUNTS.AccountNotes AN ON AN.AccountId=AB.Accountid AND AB.COUNTRYID=40
		LEFT JOIN RFOPERATIONS.RFO_REFERENCE.NotesReasonType NR on NR.ReasonTypeId=AN.ReasonTypeId
		LEFT JOIN RFOPERATIONS.RFO_REFERENCE.NotesChannelType NC on NC.ChannelTypeId=AN.ChannelTypeId
		LEFT JOIN RFOPERATIONS.RFO_REFERENCE.NotesDetailType ND on ND.DetailTypeId=AN.DetailTypeId
		where  AN.ServerModifiedDate>=@LastRunDate AND
		NOT EXISTS (SELECT 1 FROM rfoperations.sfdc.AccountNotesMissing PPM WHERE PPM.RFO_AccountNoteId=an.AccountNoteId AND missingFrom='Destination')
        
		--SELECT * FROM RFOPERATIONS.SFDC.RFO_AccountNotes WHERE ACCOUNTNOTEID IN (SELECT RFO_AccountNoteId FROM RFOPERATIONS.SFDC.AccountNotesMissing)
		
		--Loading CRM data
		SELECT
		RFOAccountNotesId__c,
		RFOAccountID__C,
		Description__c,
		ActivityDate__c,
		NotesReasonType__c,
		ChannelType__c,
		NotesDetailType__c,
		PP.ChangedByApplication__c,
		PP.ChangedByUser__c
		INTO RFOPERATIONS.SFDC.CRM_AccountNotes
		FROM sfdcbackup.SFDCBKP.AccountNotes PP,SFDCBACKUP.SFDCBKP.Accounts A  , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE PP.ACCOUNT__C=A.ID AND CAST(PP.LASTMODIFIEDDATE AS DATE) >=	@LastRunDate AND A.COUNTRY__C=C.ID AND C.NAME='Canada'

		
--Load Comparison Candidates.
SELECT * INTO  #AccountNotes FROM rfoperations.sfdc.RFO_AccountNotes
EXCEPT 
SELECT * FROM rfoperations.sfdc.CRM_AccountNotes

--SELECT * from #AccountNotes

CREATE TABLE rfoperations.sfdc.ErrorLog_AccountNotes
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, ColID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
,CRM_Value NVARCHAR (MAX)
)


DECLARE @I INT = (SELECT MIN(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'AccountNotes') , 
@C INT =  (SELECT MAX(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'AccountNotes') 


DECLARE @DesKey NVARCHAR (50) = 'RFOAccountNotesId__c'; 

DECLARE @SrcKey NVARCHAR (50) ='AccountNoteId'

DECLARE @DesTemp NVARCHAR (50) ='RFOPERATIONS.SFDC.CRM_AccountNotes' 

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
' FROM rfoperations.sfdc.ErrorLog_AccountNotes a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.ColID = ' + CAST(@I AS NVARCHAR)

DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO rfoperations.sfdc.ErrorLog_AccountNotes (Identifier,ColID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

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
FROM rfoperations.sfdc.ErrorLog_AccountNotes A JOIN Rfoperations.sfdc.CRM_METADATA B ON a.ColID =b.ColID
GROUP BY b.ColID, RFO_Column


END


GO


