 USE rfoperations
 GO 
 
 IF OBJECT_ID ('rfoperations.sfdc.CRM_Metadata') IS NOT NULL 
 DROP TABLE  rfoperations.sfdc.CRM_Metadata

  IF OBJECT_ID ('CRM_ErrorLog_Accounts') IS NOT NULL 
 DROP TABLE  CRM_ErrorLog_Accounts

 

CREATE TABLE sfdc.CRM_Metadata
(
ColID INT IDENTITY (1,1) PRIMARY KEY
,Skip BIT
,CRMObject NVARCHAR (50)
,RFO_Key NVARCHAR(50)
,RFO_Column NVARCHAR (MAX)
,CRM_Column NVARCHAR (MAX)
,sqlstmt NVARCHAR (MAX)
) 


INSERT INTO SFDC.CRM_Metadata
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
,(0,'Contacts','RFAccountContactID__C','ContactType__c,','ContactType__c,')
,(0,'Contacts','RFAccountContactID__C','Account,','Account,')
,(0,'Contacts','RFAccountContactID__C','BirthDate,','BirthDate,')
,(0,'Contacts','RFAccountContactID__C','ChangedByApplication__c,','ChangedByApplication__c,')
,(0,'Contacts','RFAccountContactID__C','ChangedByUser__C,','ChangedByUser__C,')
,(0,'Contacts','RFAccountContactID__C','DISPLAYTAXNUMBER__c,','DISPLAYTAXNUMBER__c,')
,(0,'Contacts','RFAccountContactID__C','FirstName  ,','FirstName  ,')
,(0,'Contacts','RFAccountContactID__C','Gender__c,','Gender__c,')
,(0,'Contacts','RFAccountContactID__C','LastName ,','LastName ,')
,(0,'Contacts','RFAccountContactID__C','LegalName__C ,','LegalName__C ,')
,(0,'Contacts','RFAccountContactID__C','MiddleName ,','MiddleName ,')
,(0,'Contacts','RFAccountContactID__C','NickName__c ,','NickName__c ,')
,(0,'Contacts','RFAccountContactID__C','TaxNumber__c,','TaxNumber__c,')
,(0,'Contacts','RFAccountContactID__C','LastModifiedDate,','LastModifiedDate,')
,(0,'Contacts','RFAccountContactID__C','MainPhone__c,','MainPhone__c,')
,(0,'Contacts','RFAccountContactID__C','MobilePhone,','MobilePhone,')
,(0,'Contacts','RFAccountContactID__C','MainEmail__c,','MainEmail__c,')
,(0,'Contacts','RFAccountContactID__C','SecondaryEmail__c ','SecondaryEmail__c ')
,(0,'PaymentProfile','PaymentProfileId','PAYMENTPROFILEID','RFOPaymentProfileId__c')
,(0,'PaymentProfile','PaymentProfileId','CHANGEDBYAPPLICATION','ChangedByApplication__c')
,(0,'PaymentProfile','PaymentProfileId','AccountID','Account__c')
,(0,'PaymentProfile','PaymentProfileId','DisplayNumber','DisplayNumber__c')
,(0,'PaymentProfile','PaymentProfileId','ExpMonth','ExpMonth__c')
,(0,'PaymentProfile','PaymentProfileId','ExpYear','ExpYear__c')
,(0,'PaymentProfile','PaymentProfileId','NameOnCard','NameOnCard__c')
,(0,'PaymentProfile','PaymentProfileId','Vendor','PaymentVendor__c')
,(0,'PaymentProfile','PaymentProfileId','StartDate','StartDate__c')
,(0,'PaymentProfile','PaymentProfileId','EndDate','EndDate__c')
,(0,'PaymentProfile','PaymentProfileId','IsDefault','IsDefault__c')
,(0,'PaymentProfile','PaymentProfileId','PaymentType','PaymentType__c')
,(0,'PaymentProfile','PaymentProfileId','PROFILENAME','ProfileName__c')
,(0,'PaymentProfile','PaymentProfileId','RFO_AddressProfileID','RFOAddressProfileId__c')
,(0,'PaymentProfile','PaymentProfileId','AddressLine1','AddressLine1__c')
,(0,'PaymentProfile','PaymentProfileId','AddressLine2','AddressLine2__c')
,(0,'PaymentProfile','PaymentProfileId','AddressLine3','AddressLine3__c')
,(0,'PaymentProfile','PaymentProfileId','AddressLine4','AddressLine4__c')
,(0,'PaymentProfile','PaymentProfileId','AddressLine5','AddressLine5__c')
,(0,'PaymentProfile','PaymentProfileId','CountryId','Country__c')
,(0,'PaymentProfile','PaymentProfileId','latitude','Latitude__c')
,(0,'PaymentProfile','PaymentProfileId','locale','Locale__c')
,(0,'PaymentProfile','PaymentProfileId','longitude','Longitude__c')
,(0,'PaymentProfile','PaymentProfileId','Region','Region__c')
,(0,'PaymentProfile','PaymentProfileId','PostalCode','Postal_Code__c')
,(0,'PaymentProfile','PaymentProfileId','SubRegion','Sub_Region__c')
,(0,'ShippingProfile','AddressID','Account__C','Account__c')
,(0,'ShippingProfile','AddressID','AddressLine1__C','AddressLine1__c')
,(0,'ShippingProfile','AddressID','AddressLine2__C','AddressLine2__c')
,(0,'ShippingProfile','AddressID','IsDefault','IsDefault__c')
,(0,'ShippingProfile','AddressID','AddressLine3__C','AddressLine3__c')
,(0,'ShippingProfile','AddressID','AddressLine4__C','AddressLine4__c')
,(0,'ShippingProfile','AddressID','AddressLine5__C','AddressLine5__c')
,(0,'ShippingProfile','AddressID','Country__C','Country__c')
,(0,'ShippingProfile','AddressID','latitude__C','Latitude__c')
,(0,'ShippingProfile','AddressID','locale__C','Locale__c')
,(0,'ShippingProfile','AddressID','longitude__C','Longitude__c')
,(0,'ShippingProfile','AddressID','Region__C','Region__c')
,(0,'ShippingProfile','AddressID','PostalCode__C','PostalCode__c')
,(0,'ShippingProfile','AddressID','SubRegion__C','SubRegion__c')
,(0,'ShippingProfile','AddressID','ProfileName__c','ProfileName__c')
,(0,'AccountNotes','AccountNoteId','Account__c','Account__c')
,(0,'AccountNotes','AccountNoteId','Description__c','Description__c')
,(0,'AccountNotes','AccountNoteId','ActivityDate__c','ActivityDate__c')
,(0,'AccountNotes','AccountNoteId','NotesReasonType__c','NotesReasonType__c')
,(0,'AccountNotes','AccountNoteId','ChannelType__c','ChannelType__c')
,(0,'AccountNotes','AccountNoteId','NotesDetailType__c','NotesDetailType__c')
,(0,'AccountStatusHistory','AccountId','ChangedByApplication__c','ChangedByApplication__c')
,(0,'AccountStatusHistory','AccountId','ChangedByUser__c','ChangedByUser__c')
,(0,'AccountStatusHistory','AccountId','AccountStatus__C','AccountStatus__C')
,(0,'AccountStatusHistory','AccountId','ServerModifiedDate','ServerModifiedDate')
,(0,'AccountStatusHistory','AccountId','ChangedByApplication__C','ChangedByApplication__C')
,(0,'AccountStatusHistory','AccountId','Reason__C','Reason__C')
,(0,'KPI','Account__C','Account__c','Account__c')
,(0,'KPI','Account__C','PeriodStartDate__c','PeriodStartDate__c')
,(0,'KPI','Account__C','PeriodEndDate__c','PeriodEndDate__c')
,(0,'KPI','Account__C','PeriodClosedDate__c','PeriodClosedDate__c')
,(0,'KPI','Account__C','SalesVolume__c','SalesVolume__c')
,(0,'KPI','Account__C','EstimatedSV__c','EstimatedSV__c')
,(0,'KPI','Account__C','PSQV__c','PSQV__c')
,(0,'KPI','Account__C','EstimatedPSQV__c','EstimatedPSQV__c')
,(0,'KPI','Account__C','ECLegCurrent__c','ECLegCurrent__c')
,(0,'KPI','Account__C','ECLegPriorMonth__c','ECLegPriorMonth__c')
,(0,'KPI','Account__C','TitleRecognized__c','TitleRecognized__c')
,(0,'KPI','Account__C','TitleQualified__c','TitleQualified__c')
,(0,'KPI','Account__C','TitlePaidAs__c','TitlePaidAs__c')
,(0,'KPI','Account__C','RFx_ECLegs__c','RFx_ECLegs__c')
,(0,'KPI','Account__C','RFx_LV_ECLegs__c','RFx_LV_ECLegs__c')
,(0,'KPI','Account__C','RFx_L1L2_Volume__c','RFx_L1L2_Volume__c')
,(0,'KPI','Account__C','RFx_L1L6_Volume__c','RFx_L1L6_Volume__c')
,(0,'Site','RFOAccountID__C','RFOSiteId__c','RFOSiteId__c')
,(0,'Site','RFOAccountID__C','SitePrefix__c','SitePrefix__c')
,(0,'Site','RFOAccountID__C','StartDate__c','StartDate__c')
,(0,'Site','RFOAccountID__C','EndDate__c','EndDate__c')
,(0,'Site','RFOAccountID__C','LastModifiedDate','LastModifiedDate')
,(0,'Site','RFOAccountID__C','ChangedByApplication__c','ChangedByApplication__c')
,(0,'Site','RFOAccountID__C','ChangedByUser__c','ChangedByUser__c')
,(0,'Site','RFOAccountID__C','IsActive__c','IsActive__c')
,(0,'Site','RFOAccountID__C','ExpirationDate__c','ExpirationDate__c')
,(0,'Site','RFOAccountID__C','PrimaryDomain__c','PrimaryDomain__c')
,(0,'Site','RFOAccountID__C','BusinessDomain__c','BusinessDomain__c')
,(0,'Site','RFOAccountID__C','EmailAddress__c','EmailAddress__c')





GO 



----Generate SQL Compare Statements

DECLARE @I INT = 1,
@C INT =
     ( SELECT   MAX(ColID)
               FROM     rfoperations.sfdc.CRM_METADATA
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

                SET @Temp = ( SELECT CASE 
										WHEN CRMObject = 'Accounts' THEN '#Accounts'
										WHEN CRMObject='Contacts' THEN '#Contacts'
										WHEN CRMObject='PaymentProfile' THEN '#PaymentProfiles'
										WHEN CRMObject='ShippingProfile' THEN '#ShippingProfiles'
										WHEN CRMObject='AccountNotes' THEN '#AccountNotes'
										WHEN CRMObject='AccountStatusHistory' THEN '#AccountStatusHistory'
										WHEN CRMObject='KPI' THEN '#KPI'
										WHEN CRMObject='Site' THEN '#Site'
                                     END
                              FROM      rfoperations.sfdc.CRM_METADATA
                              WHERE     ColID = @I
                            ); 


                SET @SrcKey = ( SELECT  RFO_Key
                                FROM    rfoperations.sfdc.CRM_METADATA
                                WHERE   ColID = @I
                              );

                SET @DesKey = ( SELECT   
									CASE WHEN CRMObject = 'Accounts' THEN 'RFOAccountId__c'
									     WHEN CRMObject = 'Contacts' THEN 'RFAccountContactId__c'
										 WHEN CRMObject = 'PaymentProfile' THEN 'RFOPaymentProfileID__C'
										 WHEN CRMObject='ShippingProfile' THEN 'RFOAddressPRofileID__C'
										 WHEN CRMObject='AccountNotes' THEN 'RFOAccountNotesID__c'
										 WHEN CRMObject='AccountStatusHistory' THEN 'RFOAccountID__c'
										 WHEN CRMObject='KPI' THEN 'Account__C'
										 WHEN CRMObject='Site' THEN 'RFOAccountID__C'
									END
                                FROM     rfoperations.sfdc.CRM_METADATA
                                WHERE   ColID = @I
                              ); 

                SET @SRCCol = ( SELECT  RFO_Column
                                FROM   rfoperations.sfdc.CRM_METADATA
                                WHERE   ColID = @I
                              );


                SET @DesCol = ( SELECT  CRM_Column
                                FROM    rfoperations.sfdc.CRM_METADATA
                                WHERE   ColID = @I
                              );

				   SET @SrcTemp = ( SELECT  CASE WHEN CRMObject = 'Accounts' THEN 'rfoperations.sfdc.RFO_Accounts'
												 WHEN CRMObject = 'Contacts' THEN 'rfoperations.sfdc.RFO_Contacts'
												 WHEN CRMObject = 'PaymentProfile' THEN 'rfoperations.sfdc.RFO_PaymentProfiles'
												 WHEN CRMObject='ShippingProfile' THEN 'Rfoperations.sfdc.RFO_ShippingProfiles'
												 WHEN CRMObject='AccountNotes' THEN 'Rfoperations.sfdc.RFO_AccountNotes'
												 WHEN CRMObject='AccountStatusHistory' THEN 'Rfoperations.sfdc.RFO_AccountStatusHistory'
												 WHEN CRMObject='KPI' THEN 'rfoperations.sfdc.rfo_kpi'
												 WHEN CRMObject='Site' THEN 'rfoperations.sfdc.rfo_Site'
                                             END
									FROM     rfoperations.sfdc.CRM_METADATA
									WHERE    ColID = @I
                            ); 


				SET @DesTemp = ( SELECT      CASE WHEN CRMObject = 'Accounts' THEN 'rfoperations.sfdc.CRM_Accounts'
												  WHEN CRMObject = 'Contacts' THEN 'rfoperations.sfdc.CRM_Contacts'
												  WHEN CRMObject = 'PaymentProfile' THEN 'rfoperations.sfdc.CRM_PaymentProfiles'
												  WHEN CRMObject ='ShippingProfile' THEN 'Rfoperations.sfdc.CRM_ShippingProfiles'
												  WHEN CRMObject='AccountNotes' THEN 'Rfoperations.sfdc.CRM_AccountNotes'
												  WHEN CRMObject='AccountStatusHistory' THEN 'Rfoperations.sfdc.CRM_AccountStatusHistory'
												  WHEN CRMObject='KPI' THEN 'rfoperations.sfdc.crm_kpi'
												  WHEN CRMObject='Site' THEN 'rfoperations.sfdc.crm_Site'
											 END
									FROM     rfoperations.sfdc.CRM_METADATA
                         
                              WHERE     ColID = @I
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


                UPDATE   rfoperations.sfdc.CRM_METADATA
                SET     sqlstmt = @SQL1
                WHERE   ColID = @I;


                SET @I = @I + 1;
            END; 





