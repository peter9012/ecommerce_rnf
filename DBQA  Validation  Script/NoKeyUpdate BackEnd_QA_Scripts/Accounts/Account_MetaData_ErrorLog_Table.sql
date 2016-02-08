USE DataMigration
GO 
 IF OBJECT_ID ('DataMigration.Migration.Metadata_Accounts') IS NOT NULL 
 DROP TABLE  DataMigration.Migration.Metadata_Accounts

  IF OBJECT_ID ('DataMigration.Migration.ErrorLog_Accounts  ') IS NOT NULL 
 DROP TABLE  DataMigration.Migration.ErrorLog_Accounts  


CREATE TABLE DataMigration.Migration.Metadata_Accounts
(
MapID INT IDENTITY (1,1) PRIMARY KEY
,Skip BIT 
,HybrisObject NVARCHAR (50)
,RFO_Key NVARCHAR(50)
,RFO_Column NVARCHAR (MAX)
,Hybris_Column NVARCHAR (MAX)
,sqlstmt NVARCHAR (MAX)
) 




INSERT INTO DataMigration.Migration.Metadata_Accounts
(
Skip
,HybrisObject 
,RFO_Key
,RFO_Column 
,Hybris_Column
)

VALUES 

(0 , 'Users', 'AccountID', 'Birthday','p_dateofbirth'),
(0 , 'Users', 'AccountID', 'HardTerminationDate','p_hardterminationdate'),
(0 , 'Users', 'AccountID', 'IsTaxExempt','p_excemptfromtax'),
(0 , 'Users', 'AccountID', 'EmailAddress','p_customeremail'),
(0 , 'Users', 'AccountID', 'AccountNumber','p_rfaccountnumber'),
(0 , 'Users', 'AccountID', 'SponsorID','p_sponsorid'),
(0 , 'Users', 'AccountID', 'CoApplicant','p_enrollspousename'),
(0 , 'Users', 'AccountID', 'PhoneNumberRaw','p_mainphone'),
(0 , 'Users', 'AccountID', 'NextRenewalDate','p_expirationdate'),
(0 , 'Users', 'AccountID', 'LastRenewalDate','p_renewlatertime'),
(0 , 'Users', 'AccountID', 'ConsEnrollmentDate','p_consultantsince'),
(0 , 'Users', 'AccountID', 'PCEnrollmentDate','p_preferredcustomersince'),
(0 , 'Users', 'AccountID', 'HasOrder','p_hasorder'),
(0 , 'Users', 'AccountID', 'EnrollSpouseAllow','p_enrollallowspouse'),
(0 , 'Users', 'AccountID', 'p_logindisabled','p_logindisabled'),
(0 , 'Users', 'AccountID', 'Gender','p_gender'),
(0 , 'Users', 'AccountID', 'Country','p_country'),
(0 , 'Users', 'AccountID', 'Currency','p_SessionCurrency'),
(0 , 'Users', 'AccountID', 'AccountStatus','p_AccountStatus'),
(0 , 'Users', 'AccountID', 'PreferredLanguage','p_PreferredLanguage'),
(0 , 'Users', 'AccountID', 'SessionLanguage','p_SessionLanguage'),
(0 , 'Users', 'AccountID', 'UniqueID','UniqueID'),
(1 , 'Users', 'AccountID', 'Password','Passwd'),
(1 , 'Users', 'AccountID', 'DefaultShippingAddress','p_DefaultShippingAddress'),
(1 , 'Users', 'AccountID', 'DefaultBillingAddress','p_DefaultPaymentAddress'),
(1 , 'Users', 'AccountID', 'DefaultPaymentInfo','p_DefaultPaymentInfo'),
(0 , 'Users', 'AccountID', 'Name','Name'),
(0 , 'Users', 'AccountID', 'ConsultantState','p_consultantstate'),
(0 , 'Users', 'AccountID', 'ConsultantTown','p_consultanttown'),
(0 , 'Users', 'AccountID', 'enrolledaspc','p_enrolledaspc'),
(0 , 'Users', 'AccountID', 'enrolledascrp','p_enrolledascrp'),
(0 , 'Users', 'AccountID', 'enrolledaspulse','p_enrolledaspulse'),
(0 , 'Users', 'AccountID', 'CustomerGroup','CustomerGroup'),
(0 , 'Addresses', 'AddressID', 'AccountID','p_rfAccountID'),
(0 , 'Addresses', 'AddressID', 'AddressID','p_rfaddressid'),
(0 , 'Addresses', 'AddressID', 'PhoneNumberRaw','p_phone1'),
(0 , 'Addresses', 'AddressID', 'FirstName','p_firstname'),
(0 , 'Addresses', 'AddressID', 'EmailAddress','p_email'),
(0 , 'Addresses', 'AddressID', 'Locale','p_town'),
(0 , 'Addresses', 'AddressID', 'AddressLine1','p_streetname'),
(0 , 'Addresses', 'AddressID', 'LastName','p_lastname'),
(0 , 'Addresses', 'AddressID', 'MiddleName','p_middlename'),
(0 , 'Addresses', 'AddressID', 'PostalCode','p_postalcode'),
(0 , 'Addresses', 'AddressID', 'Birthday','p_dateofbirth'),
(0 , 'Addresses', 'AddressID', 'AddressLine2','p_streetnumber'),
(0 , 'Addresses', 'AddressID', 'Gender','p_Gender'),
(0 , 'Addresses', 'AddressID', 'Region','regionpk'),
(0 , 'Addresses', 'AddressID', 'Country','Countrypk'),
(0 , 'Addresses', 'AddressID', 'p_BillingAddress','p_billingaddress'),
(0 , 'Addresses', 'AddressID', 'p_ShippingAddress','p_shippingaddress'),
(0 , 'Addresses', 'AddressID', 'p_ContactAddress','p_contactaddress'),
(0 , 'Addresses', 'AddressID', 'Hybris_Owner','OwnerPKString'),
(0 , 'PaymentInfos', 'PaymentProfileID', 'PaymentProfileID','p_RFAccountPaymentMethodID'),
(0 , 'PaymentInfos', 'PaymentProfileID', 'PaymentProfileID_Code','code'),
(0 , 'PaymentInfos', 'PaymentProfileID', 'rfAccountID','p_rfAccountId'),
(0 , 'PaymentInfos', 'PaymentProfileID', 'Hybris_Owner','OwnerPKString'),
(0 , 'PaymentInfos', 'PaymentProfileID', 'Hybris_User','UserPK'),
(0 , 'PaymentInfos', 'PaymentProfileID', 'ProfileName','ccowner'),
(0 , 'PaymentInfos', 'PaymentProfileID', 'CardNumber','number'),
(0 , 'PaymentInfos', 'PaymentProfileID', 'ExpMonth','p_validtomonth'),
(0 , 'PaymentInfos', 'PaymentProfileID', 'ExpYear','p_validtoyear'),
(0 , 'PaymentInfos', 'PaymentProfileID', 'BillingAddressID','p_billingaddress'),
(0 , 'PaymentInfos', 'PaymentProfileID', 'Vendor','p_type')


--GO 


CREATE TABLE DataMigration.Migration.ErrorLog_Accounts
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, MapID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
, Hybris_Value NVARCHAR (MAX)
)

CREATE NONCLUSTERED INDEX IX_MapID ON DataMigration.Migration.ErrorLog_Accounts
(MapID)

CREATE NONCLUSTERED INDEX IX_RecordID ON DataMigration.Migration.ErrorLog_Accounts
(RecordID)



----Generate SQL Compare Statements

DECLARE @I INT = 1,
@C INT =
     ( SELECT   MAX(MapID)
               FROM    DataMigration.Migration.Metadata_Accounts
             );


DECLARE @SRCCol NVARCHAR(MAX);

DECLARE @DesCol NVARCHAR(MAX);

DECLARE @DesKey NVARCHAR(50); 

DECLARE @SrcKey NVARCHAR(50); 

DECLARE @SrcTemp NVARCHAR(50);

DECLARE @DesTemp NVARCHAR(50);

DECLARE @Temp NVARCHAR (50);

DECLARE @Transform BIT;

WHILE ( @I <= @C )
            BEGIN 

                SET @SrcTemp = ( SELECT    CASE WHEN HybrisObject = 'Users'
                                             THEN '#RFO_Accounts'
                                             WHEN HybrisObject = 'Addresses'
                                             THEN '#RFO_Addresses'
                                             WHEN HybrisObject = 'PaymentInfos'
                                             THEN '#RFO_PayInfo'
                                        END
                              FROM      DataMigration.Migration.Metadata_Accounts
                              WHERE     MapID = @I
                            ); 


                           SET @DesTemp = ( SELECT    CASE WHEN HybrisObject = 'Users'
                                             THEN '#Hybris_Accounts'
                                             WHEN HybrisObject = 'Addresses'
                                             THEN '#Hybris_Addresses'
                                             WHEN HybrisObject = 'PaymentInfos'
                                             THEN '#Hybris_PayInfo'
                                        END
                              FROM      DataMigration.Migration.Metadata_Accounts
                              WHERE     MapID = @I
                            ); 

                           SET @Temp = ( SELECT    CASE WHEN HybrisObject = 'Users'
                                                                                         THEN '#Accounts'
                                                                                         WHEN HybrisObject = 'Addresses'
                                                                                         THEN '#Addresses'
                                                                                         WHEN HybrisObject = 'PaymentInfos'
                                                                                         THEN '#PayInfo'
                                                                                  END
                                                                FROM      DataMigration.Migration.Metadata_Accounts
                                                                WHERE     MapID = @I
                                                              ); 


                SET @SrcKey = ( SELECT  RFO_Key
                                FROM    DataMigration.Migration.Metadata_Accounts
                                WHERE   MapID = @I
                              );

                SET @DesKey = ( SELECT  CASE WHEN HybrisObject= 'Users'
                                             THEN 'p_rfAccountID'
                                             WHEN HybrisObject = 'Addresses'
                                             THEN 'p_rfAddressID'
                                             WHEN HybrisObject = 'PaymentInfos'
                                             THEN 'p_rfAccountPaymentMethodID'
                                        END
                                FROM    DataMigration.Migration.Metadata_Accounts
                                WHERE   MapID = @I
                              ); 




                SET @SRCCol = ( SELECT  RFO_Column
                                FROM   DataMigration.Migration.Metadata_Accounts
                                WHERE   MapID = @I
                              );


                SET @DesCol = ( SELECT  Hybris_Column
                                FROM  DataMigration.Migration.Metadata_Accounts
                                WHERE   MapID = @I
                              );

                DECLARE @SQL2 NVARCHAR(MAX) = 'SELECT ' + ''''
                    + CAST(@SrcKey AS NVARCHAR) + '''' + ' , '
                    + CAST(@I AS NVARCHAR) + ' , ' + @SrcKey + ', ISNULL(' + @SRCCol
                    + ', '''') FROM ' + @SRCTemp + ' WHERE ' + @SrcKey
                    + ' IN (SELECT ' + @SrcKey + ' FROM ' + @Temp
                    + ' )'
                DECLARE @SQL3 NVARCHAR(MAX) = ' EXCEPT  SELECT ' + ''''
                    + CAST(@SrcKey AS NVARCHAR) + '''' + ' , '
                    + CAST(@I AS NVARCHAR) + ' , ' + @DesKey + ', ISNULL(' + @DesCol
                    + ','''') FROM ' + @DesTemp +' WHERE ' + @DesKey
                    + ' IN (SELECT ' + @SrcKey + ' FROM ' + @Temp
                    + ' )'; 

                DECLARE @SQL1 NVARCHAR(MAX)= @SQL2 + @SQL3; 

                           SELECT @SQL1

                UPDATE  DataMigration.Migration.Metadata_Accounts
                SET     sqlstmt = @SQL1
                WHERE   MapID = @I;


                SET @I = @I + 1;
            END
