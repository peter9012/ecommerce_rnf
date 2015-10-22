 USE rfoperations
 GO 
 /*
 IF OBJECT_ID ('CRM_Metadata_Accounts') IS NOT NULL 
 DROP TABLE  CRM_Metadata_Accounts

  IF OBJECT_ID ('CRM_ErrorLog_Accounts') IS NOT NULL 
 DROP TABLE  CRM_ErrorLog_Accounts


CREATE TABLE rfoperations.sfdc.CRM_Metadata_Accounts
(
ColID INT IDENTITY (1,1) PRIMARY KEY
,Skip BIT
,CRMObject NVARCHAR (50)
,RFO_Key NVARCHAR(50)
,RFO_Column NVARCHAR (MAX)
,CRM_Column NVARCHAR (MAX)
,sqlstmt NVARCHAR (MAX)
) 


INSERT INTO CRM_Metadata_Accounts
(
Skip 
,CRMObject 
,RFO_Key 
,RFO_Column 
,CRM_Column 
)
VALUES



(0,'Accounts','AccountID','AccountNumber','AccountNumber')
,(0,'Accounts','AccountID','AccountStatus','AccountStatus__c')
,(0,'Accounts','AccountID','AccountType','AccountType__c')
,(0,'Accounts','AccountID','AddressLine1','AddressLine1__c')
,(0,'Accounts','AccountID','AddressLine2','AddressLine2__c')
,(0,'Accounts','AccountID','AddressLine3','AddressLine3__c')
,(0,'Accounts','AccountID','AddressLine4','AddressLine4__c')
,(0,'Accounts','AccountID','AddressLine5','AddressLine5__c')
,(0,'Accounts','AccountID','ChangedByApplication','ChangedByApplication__c')
,(0,'Accounts','AccountID','ChangedByUser','ChangeByUser__c')
,(0,'Accounts','AccountID','CoApplicant','Coapplicant__c')
,(0,'Accounts','AccountID','CountryId','Country__c')
,(0,'Accounts','AccountID','CreationDate','CreatedDate')
,(0,'Accounts','AccountID','CurrencyId','Currency__c')
,(0,'Accounts','AccountID','EnrollerId','ParentEnroller__c')
,(0,'Accounts','AccountID','EnrollmentDate','EnrollmentDate__c')
,(0,'Accounts','AccountID','HardTerminationDate','HardTerminationDate__c')
,(0,'Accounts','AccountID','IsActive','IsActive__c')
,(0,'Accounts','AccountID','IsBusinessEntity','IsBusinessEntity__c')
,(0,'Accounts','AccountID','IsTaxExempt','IsTaxExempt__c')
,(0,'Accounts','AccountID','Languageid','Language__c')
,(0,'Accounts','AccountID','LastAutoAssignmenDate','LastAutoAssignmentDate__c')
,(0,'Accounts','AccountID','LastModifiedDate','LastModifiedDate')
,(0,'Accounts','AccountID','LastModifiedDate_1','LastModifiedDate_1')
,(0,'Accounts','AccountID','LastRenewalDate','LastRenewalDate__c')
,(0,'Accounts','AccountID','latitude','Latitude__c')
,(0,'Accounts','AccountID','LegalName','LegalTaxName__c')
,(0,'Accounts','AccountID','locale','Locale__c')
,(0,'Accounts','AccountID','longitude','Longitude__c')
,(0,'Accounts','AccountID','MainAddressCountry','MainAddressCountry__c')
,(0,'Accounts','AccountID','MainContact','MainContact__c')
,(0,'Accounts','AccountID','Name','Name')
,(0,'Accounts','AccountID','NextRenewalDate','NextRenewalDate__c')
,(0,'Accounts','AccountID','PostalCode','PostalCode__c')
,(0,'Accounts','AccountID','RecordTypeID','RecordTypeId')
,(0,'Accounts','AccountID','Region','Region__c')
,(0,'Accounts','AccountID','RFO_AddressProfileID','RFOAddressProfileId__c')
,(0,'Accounts','AccountID','SoftTerminationDate','SoftTerminationDate__c')
,(0,'Accounts','AccountID','SponsorId','ParentSponsor__c')
,(0,'Accounts','AccountID','SubRegion','SubRegion__c')
,(0,'Accounts','AccountID','TimeZoneId','Timezone__c')


GO 



CREATE TABLE rfoperations.sfdc.ErrorLog_Accounts
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, ColID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
, Hybris_Value NVARCHAR (MAX)
)


----Generate SQL Compare Statements

DECLARE @I INT = 1,
@C INT =
     ( SELECT   MAX(MapID)
               FROM     rfoperations.sfdc.Metadata_Orders
             );


DECLARE @SRCCol NVARCHAR(MAX);


DECLARE @DesCol NVARCHAR(MAX);

DECLARE @DesKey NVARCHAR(50); 

DECLARE @SrcKey NVARCHAR(50); 

DECLARE @Temp NVARCHAR(50);

DECLARE @SrcTemp NVARCHAR(50);

DECLARE @DesTemp NVARCHAR(50);

WHILE ( @I <= @C )

            BEGIN 

                SET @Temp = ( SELECT    CASE WHEN HybrisObject = 'Accounts' THEN 
                                        END
                              FROM      rfoperations.sfdc.Metadata_Accounts
                              WHERE     MapID = @I
                            ); 


                SET @SrcKey = ( SELECT  RFO_Key
                                FROM    rfoperations.sfdc.Metadata_Accounts
                                WHERE   ColID = @I
                              );

                SET @DesKey = ( SELECT   
									CASE WHEN HybrisObject = 'Accounts'
                                           THEN 'RFOAccountId__c'
										
                                        END
                                FROM     rfoperations.sfdc.Metadata_Accounts
                                WHERE   MapID = @I
                              ); 

                SET @SRCCol = ( SELECT  RFO_Column
                                FROM   rfoperations.sfdc.Metadata_Accounts
                                WHERE   MapID = @I
                              );


                SET @DesCol = ( SELECT  Hybris_Column
                                FROM    rfoperations.sfdc.Metadata_Accounts
                                WHERE   MapID = @I
                              );

				   SET @SrcTemp = ( SELECT   CASE WHEN HybrisObject = 'Accounts' THEN 'rfoperations.sfdc.RFO_Orders'
                                             END
									FROM     DataMigration.Migration.Metadata_Accounts
									WHERE    MapID = @I
                            ); 


				SET @DesTemp = ( SELECT      CASE WHEN HybrisObject = 'Accounts'
                                             THEN 'rfoperations.sfdc.CRM_Accounts'
											                                             
                                        END
									FROM     DataMigration.Migration.Metadata_Accounts
                         
                              WHERE     MapID = @I
                            ); 

                DECLARE @SQL2 NVARCHAR(MAX) = 'SELECT ' + ''''
                    + CAST(@SrcKey AS NVARCHAR) + '''' + ' , '
                    + CAST(@I AS NVARCHAR) + ' , ' + @SrcKey + ', ISNULL(LTRIM(RTRIM(' + @SRCCol
                    + ')), '''') FROM ' + @SRCTemp + ' WHERE ' + @SrcKey
                    + ' IN (SELECT ' + @SrcKey + ' FROM ' + @Temp
                    + ' )'
                DECLARE @SQL3 NVARCHAR(MAX) = ' EXCEPT  SELECT ' + ''''
                    + CAST(@SrcKey AS NVARCHAR) + '''' + ' , '
                    + CAST(@I AS NVARCHAR) + ' , ' + @DesKey + ', ISNULL(LTRIM(RTRIM(' + @DesCol
                    + ')),'''') FROM ' + @DesTemp +' WHERE ' + @DesKey
                    + ' IN (SELECT ' + @SrcKey + ' FROM ' + @Temp
                    + ' )'; 

                DECLARE @SQL1 NVARCHAR(MAX)= @SQL2 + @SQL3; 

SELECT @SQL1
--SELECT @SQL2
--SELECT @SQL3


                UPDATE   DataMigration.Migration.Metadata_Orders
                SET     sqlstmt = @SQL1
                WHERE   MapID = @I;


                SET @I = @I + 1;
            END; 



SELECT * FROM  DataMigration.Migration.Metadata_Orders


