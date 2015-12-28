USE RFOPERATIONS;

GO

CREATE PROCEDURE sfdc.VerifyKPIMigration @LastRunDate DATETIME ='2000-01-01'

AS
BEGIN

USE RFOPERATIONS;

GO

CREATE PROCEDURE sfdc.VerifyKPIMigration @LastRunDate DATETIME ='2000-01-01'

AS
BEGIN

DECLARE @ServerMod DATETIME =@LastRunDate
DECLARE @RFOPP BIGINT, @CRMPP BIGINT
DECLARE @RowCount BIGINT 


IF OBJECT_ID('rfoperations.sfdc.KPIDifference') IS NOT NULL  DROP TABLE rfoperations.sfdc.KPIDifference
IF OBJECT_ID('Rfoperations.sfdc.KPIMissing') IS NOT NULL DROP TABLE Rfoperations.sfdc.KPIMissing
IF OBJECT_ID('Rfoperations.sfdc.RFO_KPI') IS NOT NULL DROP TABLE Rfoperations.sfdc.RFO_KPI
IF OBJECT_ID('Rfoperations.sfdc.CRM_KPI') IS NOT NULL DROP TABLE Rfoperations.sfdc.CRM_KPI
IF OBJECT_ID('Rfoperations.sfdc.ErrorLog_KPI') IS NOT NULL DROP TABLE Rfoperations.sfdc.ErrorLog_KPI
IF OBJECT_ID('rfoperations.sfdc.AccountPolicyDifference') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountPolicyDifference
IF OBJECT_ID('rfoperations.sfdc.AccountPolicy_Dups') IS NOT NULL DROP TABLE rfoperations.sfdc.AccountPolicy_Dups
IF OBJECT_ID('tempdb.dbo.#kpi') IS NOT NULL DROP TABLE #KPI

------------------------------------------------------------------------------------------------------------------------------
-- Accounts 
-----------------------------------------------------------------------------------------------------------------------------
SELECT @RFOPP=COUNT(DISTINCT AccountId)  --COUNT( DISTINCT a.AccountID)
FROM    commissions.sfdc.stg_commissionskpi ACL , RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTBASE AB WHERE AB.ACCOUNTID=ACL.ACCOUNTID AND AB.COUNTRYID=236

SELECT @CRMPP=COUNT(DISTINCT Account__C) FROM sfdcbackup.SFDCBKP.PerformanceKPI KPI , SFDCBACKUP.SFDCBKP.ACCOUNTS A , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE KPI.ACCOUNT__C=A.ID AND A.COUNTRY__C=C.ID AND C.NAME='United States'

--SELECT * FROM sfdcbackup.SFDCBKP.PerformanceKPI

										   
SELECT  @RFOPP AS RFO_KPICount, @CRMPP AS CRM_KPICountCount, (@RFOPP - @CRMPP) AS Difference 
INTO rfoperations.sfdc.KPIDifference;

SELECT  AccountId AS RFO_AccountId,
 b.RFOACcountID__C as CRM_AccountId , 
 CASE WHEN b.RFOACcountID__C IS NULL THEN 'Destination'
      WHEN a.AccountId IS NULL THEN 'Source' 
 END AS MissingFROM
INTO Rfoperations.sfdc.KPIMissing
FROM 
    (SELECT AccountId FROM commissions.sfdc.stg_commissionskpi ACL , RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTBASE AB WHERE AB.ACCOUNTID=ACL.ACCOUNTID AND AB.COUNTRYID=236) a
    FULL OUTER JOIN 
    (SELECT RFOACcountID__C FROM  sfdcbackup.SFDCBKP.PerformanceKPI AP ,sfdcbackup.SFDCBKP.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE AP.Account__C=A.ID AND A.COUNTRY__C=C.ID AND C.NAME='United States') b 
	ON cast(a.AccountId as nvarchar(max)) =b.RFOACcountID__C
 WHERE (cast(a.AccountId as nvarchar(max)) IS NULL OR b.RFOACcountID__C IS NULL) 


SELECT MissingFrom ,COUNT(*) from Rfoperations.sfdc.KPIMissing GROUP BY MISSINGFROM;

SELECT 'Query Rfoperations.sfdc.KPIMissing to get list of AccountIDs missing from Source/Destination'


SELECT 
accountid as account__c,
CAST(ISNULL(startdate,'1900-01-01') AS DATE) as PeriodStartDate__C,
CAST(ISNULL(Enddate,'1900-01-01') AS DATE) as PeriodEndDate__C,
CAST(ISNULL(Closeddate,'1900-01-01') AS DATE) as PeriodClosedDate__C,
[Sales Volume] as SalesVolume__C,
estimatedSV as estimatedSV__C,
[My PSQV – No Estimate] as PSQV__c,
[My PSQV – Estimate] as EstimatedPSQV__c,
[EC Leg Qualification – Current Progress] as ECLegCurrent__c,
[EC Leg Qualification – Prior Month] as ECLegPriorMonth__c,
[EC Leg Qualification – Recognized Title] as TitleRecognized__c,
[EC Leg Qualification – Qualification Title] as TitleQualified__c,
[EC Leg Qualification – Paid As Title] as TitlePaidAs__c,
[Road to RFx - EC Legs] as RFx_ECLegs__c,
[Road to RFx – LV EC Legs] as RFx_LV_ECLegs__c,
[Road to RFx – L1+L2 Volume] as RFx_L1L2_Volume__c,
[Road to RFx – L1-L6 Volume] as RFx_L1L6_Volume__c
INTO RFOPERATIONS.SFDC.RFO_KPI
FROM commissions.sfdc.stg_commissionskpi ACL , RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTBASE AB WHERE AB.ACCOUNTID=ACL.ACCOUNTID AND AB.COUNTRYID=236

SELECT 
A.RFOAccountID__C as Account__c,
CAST(ISNULL(KPI.PeriodStartDate__c,'1900-01-01') AS DATE) AS PeriodStartDate__c,
CAST(ISNULL(KPI.PeriodEndDate__c,'1900-01-01') AS DATE) AS PeriodEndDate__c,
CAST(ISNULL(KPI.PeriodClosedDate__c,'1900-01-01') AS DATE)  AS PeriodClosedDate__c,
KPI.SalesVolume__c,
KPI.EstimatedSV__c,
KPI.PSQV__c,
KPI.EstimatedPSQV__c,
KPI.ECLegCurrent__c,
KPI.ECLegPriorMonth__c,
KPI.TitleRecognized__c,
KPI.TitleQualified__c,
KPI.TitlePaidAs__c,
KPI.RFx_ECLegs__c,
KPI.RFx_LV_ECLegs__c,
KPI.RFx_L1L2_Volume__c,
KPI.RFx_L1L6_Volume__c
INTO RFOPERATIONS.SFDC.CRM_KPI
FROM SFDCBACKUP.SFDCBKP.PerformanceKPI KPI , SFDCBACKUP.SFDCBKP.Accounts A , SFDCBACKUP.SFDCBKP.COUNTRY C WHERE KPI.ACCOUNT__C=A.ID AND A.COUNTRY__C=C.ID AND C.NAME='United States'

SELECT * INTO #KPI FROM RFOPERATIONS.SFDC.RFO_KPI
EXCEPT 
SELECT * FROM RFOPERATIONS.SFDC.CRM_KPI

CREATE TABLE rfoperations.sfdc.ErrorLog_KPI
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, ColID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
, CRM_Value NVARCHAR (MAX)
)


DECLARE @I INT = (SELECT MIN(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'KPI') , 
@C INT =  (SELECT MAX(ColID) FROM  Rfoperations.sfdc.CRM_METADATA WHERE CRMObject = 'KPI') 


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

DECLARE @DesKey NVARCHAR (50) = 'Account__c'; 
DECLARE @SrcKey NVARCHAR (50) ='Account__c';
DECLARE @DesTemp NVARCHAR (50) ='RFOPERATIONS.SFDC.CRM_KPI' 
DECLARE @SrcCol NVARCHAR (50) =(SELECT RFO_Column FROM Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)
DECLARE @DesCol NVARCHAR (50) =(SELECT CRM_Column FROM Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)
DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM  Rfoperations.sfdc.CRM_METADATA WHERE ColID = @I)
DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.CRM_Value = b. ' + @DesCol +
' FROM rfoperations.sfdc.ErrorLog_KPI a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.ColID = ' + CAST(@I AS NVARCHAR)



DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO rfoperations.sfdc.ErrorLog_KPI (Identifier,ColID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

  BEGIN TRY
  --SELECT @SQL3
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
FROM rfoperations.sfdc.ErrorLog_kpi A JOIN Rfoperations.sfdc.CRM_METADATA B ON a.ColID =b.ColID
GROUP BY b.ColID, RFO_Column

end

GO
end

GO