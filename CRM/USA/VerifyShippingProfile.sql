	USE [RFOperations]
	GO

	--****** Object:  StoredProcedure [dbo].[VerifyShippingProfileMigration]    Script Date: 10/30/2015 10:39:24 AM ******/
	SET ANSI_NULLS ON
	GO

	SET QUOTED_IDENTIFIER ON
	GO



	CREATE PROCEDURE [dbo].VerifyShippingProfileMigration @LastRunDate DATETIME ='2000-01-01'

		DECLARE @LASTRUNDATE DATETIME ='2000-01-01';

	IF OBJECT_ID('Rfoperations.sfdc.ShippingProfilesMissing') IS NOT NULL  DROP TABLE Rfoperations.sfdc.ShippingProfilesMissing
	IF OBJECT_ID('Rfoperations.sfdc.CRM_ShippingProfiles') IS NOT NULL DROP TABLE Rfoperations.sfdc.CRM_ShippingProfiles
	IF OBJECT_ID('Rfoperations.sfdc.RFO_ShippingProfiles') IS NOT NULL DROP TABLE Rfoperations.sfdc.RFO_ShippingProfiles
	IF OBJECT_ID('Rfoperations.sfdc.ErrorLog_ShippingProfiles') IS NOT NULL DROP TABLE Rfoperations.sfdc.ErrorLog_ShippingProfiles
	IF OBJECT_ID('rfoperations.sfdc.ShippingProfilesDifference') IS NOT NULL DROP TABLE rfoperations.sfdc.ShippingProfilesDifference
	IF OBJECT_ID('rfoperations.sfdc.ShippingProfile_Dups') IS NOT NULL DROP TABLE rfoperations.sfdc.ShippingProfile_Dups
	IF OBJECT_ID('tempdb.dbo.#ShippingProfiles') IS NOT NULL DROP TABLE #ShippingProfiles

	SET ANSI_WARNINGS OFF 

	DECLARE @ServerMod DATETIME =@LastRunDate
	DECLARE @RFOSP BIGINT, @CRMSP BIGINT

	DECLARE @RowCount BIGINT 

	------------------------------------------------------------------------------------------------------------------------------
	-- ShippingAddressProfiles 
	-----------------------------------------------------------------------------------------------------------------------------
	SELECT @RFOSP=COUNT(DISTINCT AA.AddressId)  --COUNT( DISTINCT a.AccountID)
	FROM    RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB
			JOIN RFOperations.RFO_Reference.Countries (NOLOCK) C ON c.CountryID =ab.CountryID AND AB.COUNTRYID=236
			JOIN RFOperations.RFO_Accounts.AccountContacts (NOLOCK) AC ON AC.AccountId = AB.AccountID
			JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.ACCOUNTCONTACTID=AC.ACCOUNTCONTACTID
			JOIN RFOPERATIONS.RFO_ACCOUNTS.ADDRESSES AA ON ACA.ADDRESSID=AA.ADDRESSID AND ADDRESSTYPEID=2
			WHERE AA.ServerModifiedDate>=@LastRunDate

	SELECT @CRMSP=COUNT(SP.RFOAddressProfileId__c) FROM SFDCBACKUP.SFDCBKP.ShippingProfile SP,
												SFDCBACKUP.SFDCBKP.Accounts A ,
												SFDCBACKUP.SFDCBKP.COUNTRY C 
												WHERE SP.ACCOUNT__C=A.ID AND A.COUNTRY__C=C.ID AND C.NAME='United States' and
											   CAST(SP.LASTMODIFIEDDATE AS DATE) >=	@LastRunDate


	SELECT  @RFOSP AS RFO_ShippingProfile, @CRMSP AS CRM_ShippingProfile, (@RFOSP - @CRMSP) AS Difference 
	INTO rfoperations.sfdc.ShippingProfilesDifference;

	--SELECT * FROM rfoperations.sfdc.ShippingProfilesDifference;


	SELECT  AddressID AS RFO_AddressId,
	 b.RFOAddressProfileId__C AS CRM_AddressId , 
	 CASE WHEN b.RFOAddressProfileId__C IS NULL THEN 'Destination'
		  WHEN a.AddressID IS NULL THEN 'Source' 
	 END AS MissingFROM
	INTO Rfoperations.sfdc.ShippingProfilesMissing
	FROM 
		(SELECT A.AddressID FROM Rfoperations.rfo_accounts.AccountBase AB, RFOPERATIONS.RFO_ACCOUNTS.AccountContacts AC , RFOPERATIONS.RFO_ACCOUNTS.AccountContactAddresses ACA , RFOPERATIONS.RFO_ACCOUNTS.Addresses A WHERE AB.ACCOUNTID=AC.ACCOUNTID AND AC.ACCOUNTCONTACTID=ACA.ACCOUNTCONTACTID AND ACA.ADDRESSID=A.ADDRESSID AND A.AddressTypeiD=2 and ab.countryid=236) a
		FULL OUTER JOIN 
		(SELECT SP.RFOAddressProfileId__C FROM sfdcbackup.SFDCBKP.ShippingProfile SP,SFDCBACKUP.SFDCBKP.Accounts A , SFDCBACKUP.SFDCBKP.country c WHERE SP.ACCOUNT__C=A.ID AND CAST(SP.LASTMODIFIEDDATE AS DATE) >= @LastRunDate and a.country__c=c.id and c.name='United States') b 
		ON a.AddressID =b.RFOAddressProfileId__C
	 WHERE (a.AddressID IS NULL OR b.RFOAddressProfileId__C IS NULL) 


	SELECT MissingFrom ,COUNT(*) from Rfoperations.SFDC.ShippingProfilesMissing GROUP BY MISSINGFROM;

	SELECT 'Query Rfoperations.sfdc.ShippingProfilesMissing to get list of AccountIDs missing from Source/Destination'

	--------------------------------------------------------------------------------------
	-- Duplicate Shipping Addresses
	--------------------------------------------------------------------------------------

	SELECT A.AddressId, COUNT (SP.RFOAddressProfileId__C) AS CountofDups
	INTO rfoperations.sfdc.ShippingProfile_Dups
	FROM Rfoperations.rfo_accounts.AccountBase AB, 
		 RFOPERATIONS.RFO_ACCOUNTS.AccountContacts AC , 
		 RFOPERATIONS.RFO_ACCOUNTS.AccountContactAddresses ACA , 
		 RFOPERATIONS.RFO_ACCOUNTS.Addresses A ,
		 SFDCBACKUP.SFDCBKP.Accounts HA ,
		 SFDCBACKUP.SFDCBKP.ShippingProfile SP
		 WHERE AB.ACCOUNTID=AC.ACCOUNTID AND AB.COUNTRYID=236 AND
		 AC.ACCOUNTCONTACTID=ACA.ACCOUNTCONTACTID AND 
		 ACA.ADDRESSID=A.ADDRESSID AND A.AddressTypeiD=2
		 AND SP.ACCOUNT__C=HA.ID
		 AND HA.RFOAccountID__C=AB.ACCOUNTID AND A.AddressId=SP.RFOAddressProfileId__C
		 AND A.ServerModifiedDate<=@LastRunDate
	GROUP BY a.addressid
	HAVING COUNT (SP.RFOAddressProfileId__C)> 1 


	SELECT @RowCount = COUNT(*) FROM rfoperations.sfdc.ShippingProfile_Dups

	IF @RowCount > 0
		BEGIN 

				SELECT  cast(@ROWCOUNT as nvarchar) + ' Duplicate ShippingAddressProfiles in CRM. Query rfoperations.sfdc.ShippingProfile_Dups to get list of duplicate PaymentProfileIds' 
		END 

	ELSE 

	SELECT 'No Duplicates'

	-------------------------------------------------------------------------------------------
	 --Shipping Profile Framework 
	-------------------------------------------------------------------------------------------

	--Loading RFO Data
	SELECT
			CAST(AA.ADDRESSID AS NVARCHAR(MAX)) AS AddressID,
			CAST(AA.CHANGEDBYAPPLICATION AS NVARCHAR(MAX)) CHANGEDBYAPPLICATION_c,
			CAST(AB.AccountID AS NVARCHAR (MAX)) AS Account__c,
			CAST(AA.AddressLine1 AS NVARCHAR(MAX)) AddressLine1__c,
			CAST(CASE WHEN LEN(AA.AddressLine2)<1 THEN NULL ELSE AA.AddressLine2 END AS NVARCHAR(MAX)) AddressLine2__c,
			CAST(CASE WHEN LEN(AA.AddressLine3)<1 THEN NULL ELSE AA.AddressLine3 END AS NVARCHAR(MAX)) AddressLine3__c,
			CAST(CASE WHEN LEN(AA.AddressLine4)<1 THEN NULL ELSE AA.AddressLine4 END AS NVARCHAR(MAX)) AddressLine4__c,
			CAST(CASE WHEN LEN(AA.AddressLine5)<1 THEN NULL ELSE AA.AddressLine5 END AS NVARCHAR(MAX)) AddressLine5__c,
			CAST(C.NAME AS NVARCHAR(MAX)) Country__c,
			casE WHEN AA.IsDefault= 1 then 'true' ELSE 'false' END AS IsDefault,
			CAST(CAST(CASE WHEN LEN(AA.latitude)<1 THEN NULL ELSE AA.latitude END AS DECIMAL(10,1)) AS NVARCHAR(MAX)) latitude__c,
			CAST(CASE WHEN LEN(AA.locale) <1 THEN NULL ELSE AA.locale END AS NVARCHAR(MAX)) locale__c,
			CAST(CAST(CASE WHEN LEN(AA.longitude)<1 THEN NULL ELSE AA.longitude END AS DECIMAL(10,1)) AS NVARCHAR(MAX)) longitude__c,
			CAST(CASE WHEN LEN(AA.Region)<1 THEN NULL ELSE AA.Region END AS NVARCHAR(MAX)) Region__c,
			CAST(CASE WHEN LEN(AA.PostalCode)<1 THEN NULL ELSE AA.PostalCode END AS NVARCHAR(MAX)) PostalCode__c,
			CAST(CASE WHEN LEN(AA.SubRegion)< 1 THEN NULL ELSE AA.SubRegion END AS NVARCHAR(MAX)) SubRegion__c,
			CAST(AA.AddressProfileName AS NVARCHAR(MAX)) ProfileName__c
			INTO RFOPERATIONS.SFDC.RFO_ShippingProfiles
			-- join address table here.
			FROM  RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB
			JOIN RFOperations.RFO_Reference.Countries (NOLOCK) C ON c.CountryID =ab.CountryID AND AB.COUNTRYID=236
			JOIN RFOperations.RFO_Accounts.AccountContacts (NOLOCK) AC ON AC.AccountId = AB.AccountID
			JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.ACCOUNTCONTACTID=AC.ACCOUNTCONTACTID
			JOIN RFOPERATIONS.RFO_ACCOUNTS.ADDRESSES AA ON ACA.ADDRESSID=AA.ADDRESSID AND AA.ADDRESSTYPEID=2
			WHERE AA.ServerModifiedDate>= @LastRunDate AND
			NOT EXISTS (SELECT 1 FROM rfoperations.sfdc.ShippingProfilesMissing PPM WHERE PPM.RFO_AddressId=AA.ADDRESSID AND missingFrom='Destination')
        
			--SELECT CAST(0 AS DECIMAL(10,1))
		--Loading CRM data
		SELECT
		PP.RFOAddressProfileId__c,
		'NULL' AS ChangedByApplication__c,
		A.RFOAccountID__C as Account__C,
		PP.AddressLine1__c,
		PP.AddressLine2__c,
		PP.AddressLine3__c,
		PP.AddressLine4__c,
		PP.AddressLine5__c,
		C.NAME AS Country__c,
		IsDefault__c,
		PP.Latitude__c,
		PP.Locale__c,
		PP.Longitude__c,
		PP.Region__c,
		PP.PostalCode__c,
		PP.SubRegion__c,
		pp.ProfileName__c
		INTO RFOPERATIONS.SFDC.CRM_ShippingProfiles
		FROM sfdcbackup.SFDCBKP.ShippingProfile PP, SFDCBACKUP.SFDCBKP.COUNTRY C ,
		 SFDCBACKUP.SFDCBKP.Accounts A WHERE PP.ACCOUNT__C=A.ID AND C.ID=PP.Country__c AND C.NAME='United States' and
		 PP.LastModifiedDate>= @LastRunDate

			--SELECT * FROM sfdcbackup.SFDCBKP.ShippingProfile
	--Load Comparison Candidates.
	SELECT * INTO  #ShippingProfiles FROM rfoperations.sfdc.RFO_ShippingProfiles
	EXCEPT 
	SELECT * FROM rfoperations.sfdc.CRM_ShippingProfiles

	--SELECT * FROM RFOPERATIONS.SFDC.RFO_ShippingProfiles

	CREATE TABLE rfoperations.sfdc.ErrorLog_ShippingProfiles
	(
	ErrorID INT  IDENTITY(1,1) PRIMARY KEY
	, ColID INT 
	,Identifier NVARCHAR (50)
	, RecordID BIGINT 
	, RFO_Value NVARCHAR (MAX)
	,CRM_Value NVARCHAR (MAX)
	)


	DECLARE @I INT = (SELECT MIN(ColID) FROM  Rfoperations.sfdc.CRM_Metadata WHERE CRMObject = 'ShippingProfile') , 
	@C INT =  (SELECT MAX(ColID) FROM  Rfoperations.sfdc.CRM_Metadata WHERE CRMObject = 'ShippingProfile') 


	DECLARE @DesKey NVARCHAR (50) = 'RFOAddressProfileID__C'; 

	DECLARE @SrcKey NVARCHAR (50) ='AddressId'

	DECLARE @DesTemp NVARCHAR (50) ='RFOPERATIONS.SFDC.CRM_ShippingPROFILES' 

	DECLARE @Skip  BIT 

	WHILE (@I <=@c)

	BEGIN 

			SELECT  @Skip = ( SELECT   Skip
								   FROM     Rfoperations.sfdc.CRM_Metadata_Accounts
								   WHERE    ColID = @I
								 );


			IF ( @Skip = 1 )

				SET @I = @I + 1;

			ELSE
	BEGIN 



	DECLARE @SrcCol NVARCHAR (50) =(SELECT RFO_Column FROM Rfoperations.sfdc.CRM_Metadata WHERE ColID = @I)
	DECLARE @DesCol NVARCHAR (50) =(SELECT CRM_Column FROM Rfoperations.sfdc.CRM_Metadata WHERE ColID = @I)
	DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM  Rfoperations.sfdc.CRM_Metadata WHERE ColID = @I)

	DECLARE @SQL2 NVARCHAR (MAX) = ' 
	 UPDATE A 
	SET a.crm_Value = b. ' + @DesCol +
	' FROM rfoperations.sfdc.ErrorLog_ShippingProfiles a  JOIN ' +@DesTemp+
	  ' b  ON a.RecordID= b.' + @DesKey+  
	  ' WHERE a.ColID = ' + CAST(@I AS NVARCHAR)

	DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
	' INSERT INTO rfoperations.sfdc.ErrorLog_ShippingProfiles (Identifier,ColID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

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


END
GO