USE DM_QA
GO 

IF OBJECT_ID('dbqa.Map_tab') IS NULL 
CREATE TABLE  dbqa.Map_tab
(MapID INT IDENTITY(1,1) PRIMARY KEY,
SourceObject NVARCHAR(100),--RFOperations.dbo.Accounts
SourceColumn NVARCHAR(50),--FirstName
SourceDataTypes NVARCHAR(50),--Nvarchar(20)
SourceRef NVARCHAR(MAX)-- Direct or Transform
,TargetObject NVARCHAR(100),--Hybris.dbo.Users
TargetColumn NVARCHAR(50),--P_FirstName
TargetDataTypes NVARCHAR(50),-- Nvarchar(20)
[Key] NVARCHAR(50),--AccountNumber
TargetRef NVARCHAR(MAX),--ComposedTypes
Flag NVARCHAR(10), --C2C or Default or Manual 
[Owner] NVARCHAR(50),
[SQL Stmt] NVARCHAR(Max)--'Select * from TableA a join TabelB b on a.ID=b.ID Where a.Fields<>b.Fields'
)

--********************************************************************************************
--												Accounts
--********************************************************************************************

INSERT INTO dbqa.Map_tab
        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
VALUES 
/* Users Table in Hybris.*/
(N'RFOperations.RFO_Accounts.AccountContacts' ,N'Birthday',N'DATETIME' ,N'NULL THEN ''1900-01-01''' ,N'Hybris.dbo.Users' ,N'p_dateofbirth' ,N'DATETIME' ,N'AccountNumber' ,N'' , N'C2C' ,N'Accounts',N'') 
,--p_dateofbirth
(N'RFOperations.RFO_Accounts.AccountRF',N'HardTerminationDate',N'DATETIME',N'Load as NULL',N'Hybris.dbo.Users',N'p_hardterminateddate',N'DATETIME',N'AccountNumber',N'', N'C2C',N'Accounts',N'') 
,--p_hardterminationdate
(N'RFOperations.RFO_Accounts.AccountRF',N'SoftTerminationDate',N'DATETIME',N'Load as NULL',N'Hybris.dbo.Users',N'p_softterminateddate',N'DATETIME',N'AccountNumber',N'', N'C2C',N'Accounts',N'') 
,--p_softterminationdate
(N'RFOperations.RFO_Accounts.AccountContacts',N'TaxNumber',N'Nvarchar',N' ',N'Hybris.dbo.Users',N'p_accountnumber',N'string',N'AccountNumber',N' ', N'C2C',N'Accounts',N'') 
,--p_taxnumber
(N'RFOperation.RFO_Account.AccountContacts',N'GenderID',N'BIGINT',N'RFOperations.RFO_reference.Genders',N'Hybris.dbo.Users',N'p_gender',N'BIGINT',N'AccountNumber',N'Hybris.EnumerationValues', N'ref',N'Accounts',N'') 
,--p_gender
(N'RFOperations.RFO_Accounts.AccountRF',N'IsTaxExempt',N'BIT',N'1 True N 0 False',N'Hybris.dbo.Users',N'p_taxexempt',N'tinyint',N'AccountNumber',N'1 True N 0 False', N'C2C',N'Accounts',N'') 
,--IsTaxExempt
(N'RFOperations.RFO_Accounts.AccountRF',N'IsBusinessEntity',N'BIT',N'1 True N 0 False',N'Hybris.dbo.Users',N'p_businessentity',N'tinyint',N'AccountNumber',N'1 True N 0 False', N'C2C',N'Accounts',N'') 
,--p_businessentity
(N'RFOperations.RFO_Accounts.EmailAddresses',N'EmailAddress',N'NVARCHA',N'EmailTypeID=1',N'Hybris.dbo.Users',N'p_email',N'nvarchar',N'AccountNumber',N'', N'C2C',N'Accounts',N'') 
,--p_email
(N'RFOperation.RFO_Account.AccountBase',N'CountryID',N'BIGINT',N'RFOperations.RFO_reference.Countries',N'Hybris.dbo.Users',N'p_country',N'BIGINT',N'AccountNumber',N'Hybris.dbo.countries', N'ref',N'Accounts',N'') 
,--p_country
(N'RFOperations.RFO_Accounts.AccountRF',N'AccountNumber',N'NVARCHAR',N' Account SourceKey',N'Hybris.dbo.Users',N'p_customerid',N'string',N'AccountNumber',N'Users TargetKey', N'Key',N'Accounts',N'') 
,--p_customerid
(N'RFOperations.RFO_Accounts.AccountRF',N'SponsorID',N'BIGINT',N' ',N'Hybris.dbo.Users',N'p_newsponsorid',N'Bigint',N'AccountNumber',N'', N'upd',N'Accounts',N'') 
,--p_newsponsorid
(N'RFOperations.RFO_Accounts.AccountBase',N'AccountStatusID',N'BIGINT',N'RFO_References.AccountStatus',N'Hybris.dbo.Users',N'p_status',N'NVARCHAR',N'AccountNumber',N'Hybris..venumerationvalues', N'ref',N'Accounts',N'') 
,--p_status
(N'RFOperations.Hybris.AccountBasee',N'AccountTypes',N'BIT',N'',N'Hybris.dbo.users',N'p_type',N'BIGINT',N'AccountNumber',N'PK of internalcode in enums. ', N'Ref',N'Accounts',N'') 
,--p_type
(N'RFOperations.RFO_Accounts.AccountRF',N'CoAppFistName',N'NVARCHAR',N'NuLL THEN  '' '' Splitting p_spousefirstname with first space as delimiter. ',N'Hybris.dbo.Users',N'p_spousefirstname',N'STRING',N'AccountNumber',N' ', N'C2C',N'Accounts',N'') 
,--p_spouseFirstName
(N'RFOperations.RFO_Accounts.AccountRF',N'CoAppLastName',N'NVARCHAR',N'NuLL THEN  '' '' Splitting p_spouselastname  with first space as delimiter. ',N'Hybris.dbo.Users',N'p_spouselastname',N'STRING',N'AccountNumber',N' ', N'C2C',N'Accounts',N'') 
,--p_spouselastname
--(N'RFOperations.RFO_Accounts.Phones',N'PhoneNumberRaw',N'NVARCHAR',N'PhoneType=Main ',N'Hybris.dbo.Users',N'',N'BIGINT',N'AccountNumber=p_rfaccountID',N'? ', N'C2C',N'Accounts',N'') 
--,--PoneNumberRaw
(N'RFOperations.RFO_Accounts.AccountRF',N'NextRenewalDate',N'DATETIME',N'NULL for RC and PC and DEFAULT NULL ',N'Hybris.dbo.Users',N'p_nextrenewaldate',N'DATETIME',N'AccountNumber',N'', N'c2c',N'Accounts',N'') 
,--p_nextrenewaldate
(N'RFOperations.RFO_Accounts.AccountRF',N'LastRenewalDate',N'DATETIME',N'NULL for RC and PC and DEFAULT NULL',N'Hybris.dbo.Users',N'p_lastrenewaldate',N'DATETIME',N'AccountNumber',N' ', N'c2c',N'Accounts',N'') 
,--p_lastrenewaldate
(N'RFOperations.RFO_Accounts.AccountBase',N'EnrollmentDate',N'BIGINT',N'RFO_References.AccountStatus',N'Hybris.dbo.Users',N'p_enrolementdate',N'NVARCHAR',N'AccountNumber',N'Hybris..venumerationvalues', N'ref',N'Accounts',N'') 
,--p_enrolementdate
(N'RFOperations.RFO_Accounts.AccountRF',N'Name',N'NVARCHAR',N'',N'Hybris.dbo.Users',N'p_name',N'NVARCHAR',N'AccountNumber=p_rfaccountID',N' ', N'C2C',N'Accounts',N'') 
,--Name
(N'RFOperations.RFO_Accounts.Emailaddresses',N'UserName',N'NVARCHAR',N'',N'Hybris.dbo.Users',N'p_uid',N'nvarchar',N' ',N'AccountNumber', N'C2C',N'Accounts',N'') 
,--p_uid
(N'RFOperations.Security.AccountSecurity',N'password',N'NVARCHAR',N'',N'Hybris.dbo.Users',N'passwd',N'nvarchar',N' ',N'AccountNumber', N'C2C',N'Accounts',N'') 
,--PassWord
(N'RFOperations.RFO_Accounts.PaymentProfiles',N'PaymentProfileID',N'bigint',N'PaymentProfile Where IsDefault= 1',N'Hybris.dbo.Users',N'p_defaultpaymentinfo',N'BIGINT',N'AccountNumber',N'PK of Default PaymentInfos ', N'upd',N'Accounts',N'') 
,--p_defaultpaymentinfo
(N'RFOperations.RFO_Accounts.Addresses',N'ShippingAddressId',N'bigint',N'ShippingAddress Where IsDefault= 1',N'Hybris.dbo.Users',N'p_defaultshipmentaddress',N'BIGINT',N'AccountNumber',N'PK ofAddresses Where  Default Shiiping=1 ', N'upd',N'Accounts',N'') 
,--DefaultShippingAddress
(N'RFOperations.RFO_Accounts.Addresses',N'BillingAddressID',N'bigint',N'BillingAddress Where IsDefault= 1',N'Hybris.dbo.Users',N'P_defaultpaymentAddress',N'BIGINT',N'AccountNumber',N'PK of Default BillingAddresses ', N'upd',N'Accounts',N'') 
,--P_defaultpaymentAddress
(N'RFOperations.Hybris.AccountBasee',N'CurrencyID',N'Bigint',N'',N'Hybris.dbo.users',N'p_sessioncurrency',N'BIGINT',N'AccountNumber',N'Hybris.dbo.Currencies', N'Ref',N'Accounts',N'') 
,--p_sessioncurrency
(N'RFOperations.Hybris.AccountBasee',N'LanguageID',N'Bigint',N'',N'Hybris.dbo.users',N'p_sessionlanguage',N'BIGINT',N'AccountNumber',N'Hybris.dbo.languages', N'ref',N'Accounts',N'') 
,--p_sessionlanguage
(N'RFOperations.RFO_Accounts.AccountRF',N'Active',N'BIT',N'Active is 1 then True else False',N'Hybris.dbo.Users',N'p_active',N'tinyint',N'AccountNumber=p_rfaccountID ',N's', N'C2C',N'Accounts',N'') 
,--p_active
(N'RFOperations.Hybris.Autoship',N'IsPulseSubscrption',N'BIT',N'',N'Hybris.dbo.users',N'p_pulsesubscription',N'bolean',N'AccountNumber',N'', N'c2c',N'Accounts',N'') 
,--p_pulsesubscription
(N'RFOperations.Hybris.Autoship',N'IsPCActive',N'BIT',N'',N'Hybris.dbo.users',N'p_pcperksactive',N'bolean',N'AccountNumber',N'', N'c2c',N'Accounts',N'') 
,--p_pcperksactive
(N'RFOperations.RFO_Accounts.profilePictures',N'profilepicture',N'BIT',N'',N'Hybris.dbo.users',N'p_profilepicture',N'bigint',N'AccountNumber',N'PK of Hybris.dbo.userprofiles ', N'c2c',N'PWS',N'') 
,--p_profilepicture
(N'RFOperations.RFO_Accounts.profilePictures',N'TokenID',N'nvarchar(255)',N'',N'Hybris.dbo.users',N'p_token',N'nvarchar(255)',N'AccountNumber',N' ', N'upd',N'Accounts',N'') 
,--p_token
(N' ',N'',N'',N'Hybris System Attribute',N'Hybris.dbo.users',N'Typepkstring',N'bigint',N'AccountNumber',N'PK from Hybris.dbo.composedtypes for Customers', N'def',N'Accounts',N'SELECT  u.p_customerid HybrisKey ,
        u.TypePkString ,
        t.InternalCode [Customer]
FROM    Hybris.dbo.users u
		JOIN #Hybris h ON u.p_customerid=h.HybrisKey
        JOIN Hybris.dbo.composedtypes t ON t.pk = u.TypePkString
WHERE   t.InternalCode <>''Customer''') 
,--Typepkstring.

/* Pending from Thiru 
(N'RFOperations.RFO_Accounts.AccountRF',N'Active',N'BIT',N'Active is 1 then True else False',N'Hybris.dbo.Users',N'p_logindisabled',N'Bolean',N'AccountNumber=p_rfaccountID ',N's', N'C2C',N'Accounts',N'') 
,--p_logindisabled
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--OwnerPkString
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--createdTS	datetime  >>> WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--modifiedTS	datetime  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_description	nvarchar(255) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_backofficelogindisabled	tinyint  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_passwordencoding	nvarchar(255)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_passwordanswer	nvarchar(max) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_passwordquestion	nvarchar(max) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_logindisabled	tinyint  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)? IS THERE ANY ATTRIBUTES BEpEN MAPPED FROM RFO??
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_lastlogin	datetime >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_hmclogindisabled	tinyint  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_europe1pricefactory_udg	bigint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_europe1pricefactory_upg	bigint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_europe1pricefactory_utg	bigint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_authorizedtounlockpages	tinyint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_previewcatalogversions	nvarchar(max)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_title	bigint  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_token	nvarchar(max)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_originaluid	nvarchar(255)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_sapconsumerid	nvarchar(255)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_sapcontactid	nvarchar(255) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_sapisreplicated	tinyint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_sapreplicationinfo	nvarchar(255) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_pdfversion	nvarchar(255) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_countrysecuritycode	nvarchar(255)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_sponsorstartdate	datetime  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_pcperksactive	tinyint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM) for different Accounttype than PC?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccountNumber',N'', N'def',N'Accounts',N'') 
,--p_defaultb2bunit	bigint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
*/
   


			
/* Accounts paymentinfos Table in Hybris*/


--INSERT INTO #Map_Tab
--        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
--VALUES 
--(N'RFOperation.RFO_Account.PaymentProfiles',N'PaymentProfileID',N'bigint',N' ',N'Hybris.dbo.Paymentinfos',N'PK',N'Bigint',N'AccountNumber',N' ', N'C2C',N'PaymentProfiles',N'') 
--,--PaymentProfileID 
(N'RFOperation.RFO_Account.PaymentProfiles',N'ProfileName',N'nvarchar',N' ',N'Hybris.dbo.Paymentinfos',N'p_ccowner',N'nvarchar',N'AccountNumber',N' ', N'C2C',N'AccountPaymentProfiles',N'') 
,--ProfileName
 (N'RFOperation.RFO_Account.PaymentProfiles',N'DisplayNumber',N'nvarchar',N' ',N'Hybris.dbo.Paymentinfos',N'p_number',N'string',N'AccountNumber=p_rfaccountID',N' ', N'C2C',N'AccountPaymentProfiles',N'') 
,--DisplayNumber
 (N'RFOperation.RFO_Account.PaymentProfiles',N'VendorID',N'bigint',N' ',N'RFOperations.RFO_Reference.CreditcartTypes',N'p_type',N'string',N'AccountNumber',N'Hybris.dbo.enumerationvalues ', N'ref',N'AccountPaymentProfiles',N'') 
,--VendorID
 (N'RFOperation.RFO_Account.PaymentProfiles',N'ExpMonth',N'',N' ',N'Hybris.dbo.Paymentinfos',N'p_validtomonth',N'',N'AccountNumber',N' ', N'C2C',N'AccountPaymentProfiles',N'') 
,--ExpMonth
 (N'RFOperation.RFO_Account.PaymentProfiles',N'ExpYear',N'',N' ',N'Hybris.dbo.Paymentinfos',N'p_validtoyear',N'',N'AccountNumber',N' ', N'C2C',N'AccountPaymentProfiles',N'') 
,--ExpYear
 (N'RFOperation.RFO_Account.CreditCardProfiles',N'BillingAddressID',N'bigint',N' ',N'Hybris.dbo.Paymentinfos',N'p_billingaddress',N'bigint',N'AccountNumber',N' PK from Addresses', N'ref',N'AccountPaymentProfiles',N'') 
,--p_billingaddress
 (N'RFOperation.RFO_Account.CreditCardProfiles',N'LastFourNumber',N'nvarchar',N'LastFour Digit of Number ',N'Hybris.dbo.Paymentinfos',N'p_lastfourdigit',N'nvarchar',N'AccountNumber',N' PK from Addresses', N'c2c',N'AccountPaymentProfiles',N'') 
,--p_lastfourdigit
 (N'RFOperation.RFO_Account.CreditCardProfiles',N'cctoken',N'nvarchar',N' ',N'Hybris.dbo.Paymentinfos',N'p_cctoken',N'nvarchar',N'AccountNumber',N' PK from Addresses', N'c2c',N'AccountPaymentProfiles',N'') 
,--p_cctoken
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'p_original',N'bigint',N'AccountNumber',N'Null for AccountLevel PaymentInfos', N'def',N'AccountPaymentProfiles',N'') 
,--p_original
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'p_duplicate',N'bigint',N'AccountNumber',N'Null for AccountLevel PaymentInfos', N'def',N'AccountPaymentProfiles',N'') 
,--p_duplicate
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'typepkstring',N'bigint',N'AccountNumber',N'pk from composedtypes Where iternalCode=CreditCardPaymentInfo', N'def',N'AccountPaymentProfiles',N'') 
,--typepkstring
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'OwnerPkString',N'bigint',N'AccountNumber',N'Null for Accountlevel paymentinfos', N'def',N'AccountPaymentProfiles',N'') 
,--OwnerPkString
/* Pending Concern to following Attributes
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'OwnerPkString',N'bigint',N'AccountNumber',N'Null for Accountlevel paymentinfos', N'def',N'AccountPaymentProfiles',N'') 
,--OwnerPkString
--createdTS	datetime
--modifiedTS	datetime
--p_code	nvarchar(255)
--p_saved	tinyint
--p_validfrommonth	nvarchar(255)
--p_validfromyear	nvarchar(255)
--p_subscriptionid	nvarchar(255)
--p_issuenumber	int
--p_subscriptionvalidated	tinyint
--p_subscriptionserviceid	nvarchar(255)
--p_bankidnumber	nvarchar(255)
--p_bank	nvarchar(255)
--p_accountnumber	nvarchar(255)
--p_baowner	nvarchar(255)

*/


			
			
/*  Accounts Addresses Table in Hybris*/


(N'RFOperations.RFO_Accounts.Addresses',N'CountryID',N'nvarchar',N'PK from Hybris.dbo.regions',N'Hybris.dbo.addresses',N'p_counrty',N'BIGINT',N'AccountNumber',N'PK of Default PaymentInfos ', N'ref',N'AccountAddresses',N'') 
,--p_counrty
(N'RFOperations.RFO_Accounts.Addresses',N'region',N'nvarchar',N'PK from Hybris.dbo.regions',N'Hybris.dbo.addresses',N'p_region',N'BIGINT',N'AccountNumber',N'PK of Hybris.dbo.regions Where p_isocodeshort=State ', N'ref',N'AccountAddresses',N'') 
,--p_region
(N'RFOperations.RFO_Accounts.Phones',N'MainNumber',N'NVARCHAR',N'RFOperations.Phones Where PhoneType=Main',N'Hybris.dbo.Addresses',N'p_phone1',N'nvarchar',N'AccountNumber',N'', N'C2C',N'AccountAddresses',N'') 
,--p_phone1
(N'RFOperations.RFO_Accounts.Phones',N'HomeNumber',N'NVARCHAR',N'RFOperations.Phones Where PhoneType=Home',N'Hybris.dbo.Addresses',N'p_phone2',N'nvarchar',N'AccountNumber',N'', N'C2C',N'AccountAddresses',N'') 
,--p_phone2
(N'RFOperations.RFO_Accounts.Phones',N'CellNumber',N'NVARCHAR',N'RFOperations.Phones Where PhoneType=Mobile',N'Hybris.dbo.Addresses',N'p_cellphone',N'nvarchar',N'AccountNumber',N'', N'C2C',N'AccountAddresses',N'') 
,--p_cellphone
(N'RFOperations.RFO_Accounts.AccountContacts',N'GenderID',N'NVARCHAR',N'RFOperations.RFO_reference.Genders',N'Hybris.dbo.Addresses',N'p_gender',N'BIGINT',N'AccountNumber',N'Hybris.dbo.enumurationValues ', N'ref',N'AccountAddresses',N'') 
,--p_gender
(N'RFOperations.RFO_Accounts.Addresses',N'IsBillingAddress',N'BIT',N'RFOperations.RFO_References.AddressTypes',N'Hybris.dbo.addresses',N'p_billingaddress',N'BIGINT',N'AccountNumber',N'True=1 False=0', N'c2c',N'AccountAddresses',N'') 
,--p_billingaddress
(N'RFOperations.RFO_Accounts.Addresses',N'IsContactAddress',N'BIT',N'RFOperations.RFO_References.AddressTypes',N'Hybris.dbo.addresses',N'p_contactaddress',N'BIGINT',N'AccountNumber',N'True=1 False=0', N'c2c',N'AccountAddresses',N'') 
,--p_contactaddress
(N'RFOperations.RFO_Accounts.Addresses',N'IsShippingAddress',N'BIT',N'RFOperations.RFO_References.AddressTypes',N'Hybris.dbo.addresses',N'p_shippingaddress',N'BIGINT',N'AccountNumber',N'True=1 False=0', N'c2c',N'AccountAddresses',N'') 
,-- p_shippingaddress
(N'RFOperations.RFO_Accounts.Addresses',N'Locale',N'nvarchar',N'T ',N'Hybris.dbo.addresses',N'p_town',N'nvarchar',N'AccountNumber',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_town
--(N'RFOperations.RFO_Accounts.Addresses',N'AddressID',N'nvarchar',N'T ',N'Hybris.dbo.Users',N'p_rfaddressid',N'BIGINT',N'AccountNumber=p_rfaccountID',N' ', N'C2C',N'AccountAddresses',N'') 
--,--AddressID
(N'RFOperation.RFO_Account.Addresses',N'AddressLine1',N'nvarchar',N'StreenName only Excluding Number ',N'Hybris.dbo.addresses',N'p_streetname',N'nvarchar(255)',N'AccountNumber',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_streetname
(N'RFOperation.RFO_Account.Addresses',N'AddressLine2',N'nvarchar',N'Numbers only Excluding Name ',N'Hybris.dbo.addresses',N'p_streetnumber',N'nvarchar(255)',N'AccountNumber',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_streetnumber 
(N'RFOperation.RFO_Account.Addresses',N'FirstName',N'nvarchar',N' ',N'Hybris.dbo.addresses',N'p_firstname',N'nvarchar(255)',N'AccountNumber',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_firstname
(N'RFOperation.RFO_Account.Addresses',N'LastName',N'nvarchar',N' ',N'Hybris.dbo.addresses',N'p_lastname',N'nvarchar(255)',N'AccountNumber',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_lastname
(N'RFOperation.RFO_Account.Addresses',N'MiddleName',N'nvarchar',N' ',N'Hybris.dbo.addresses',N'p_MiddleName',N'nvarchar(255)',N'AccountNumber',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_MiddleName
(N'RFOperation.RFO_Account.Addresses',N'PostalCode',N'nvarchar',N' ',N'Hybris.dbo.addresses',N'p_postalcode',N'nvarchar(255)',N'AccountNumber',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_postalcode
(N'RFOperation.RFO_Account.Addresses',N'BirthDay',N'nvarchar',N' ',N'Hybris.dbo.addresses',N'p_dateofbirth',N'nvarchar(255)',N'AccountNumber',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_dateofbirth
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'typepkstring',N'bigint',N'AccountNumber',N'pk from composedtypes Where iternalCode=Address', N'default',N'AccountAddresses',N'') 
,--typepkstring
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'p_duplicate',N'tityint',N'AccountNumber',N'0 for AccountsLevel Addresses', N'default',N'AccountAddresses',N'') 
,--p_duplicate
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'p_original',N'bigint',N'AccountNumber',N'Null for AccountLevel addresses', N'default',N'AccountAddresses',N'') 
--p_original

/* Pending Concerns From Thiru

 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'createdTS',N'datetime',N'AccountNumber',N'Getdate for AccountLevel addresses', N'def',N'AccountAddresses',N'') 
,--createdTS	datetime
--modifiedTS	datetime
--p_appartment	nvarchar(255)
--p_building	nvarchar(255)
--p_company	nvarchar(255)
--p_department	nvarchar(255)
--p_district	nvarchar(255)
--p_email	nvarchar(255)
--p_fax	nvarchar(255)
--p_middlename2	nvarchar(255) > What is the with p_middlename1?
--p_pobox	nvarchar(255)
--p_postalcode	nvarchar(255)
--p_title	bigint
--p_remarks	nvarchar(255)
--p_url	nvarchar(255)
--p_unloadingaddress	tinyint
--p_visibleinaddressbook	tinyint
--p_sapcustomerid	nvarchar(255)
--p_sapaddressusage	nvarchar(255)
--p_sapaddressusagecounter	nvarchar(255)

 */



 
--********************************************************************************************
--												Autoships
--********************************************************************************************


 USE DM_QA
GO 

INSERT INTO  dbqa.Map_tab
        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
VALUES 

/* Autoship Header Level */

(N'RFOperations.Hybris.Autoship' ,N'AutoshipTypeID',N'bigint' ,N' ' ,N'Hybris.dbo.carts' ,N'Typepkstring' ,N'bigint' ,N'AutoshipNumber' ,N'Hybris.dbo.composedTypes' , N'ref' ,N'Autoship',N'') 
,--Typepkstring
(N'RFOperations.Hybris.Autoship' ,N'AccountID',N'bigint' ,N'pk Bridge table ' ,N'Hybris.dbo.carts' ,N'p_user' ,N'bigint' ,N'AutoshipNumber' ,N'Hybris.dbo.users' , N'ref' ,N'Autoship',N'') 
,--p_user
(N'RFOperations.Hybris.Autoship' ,N'Active',N'bit' ,N'pk Bridge table ' ,N'Hybris.dbo.carts' ,N'p_active' ,N'bigint' ,N'AutoshipNumber' ,N'defaulted to 1' , N'c2c' ,N'Autoship',N'') 
,--p_active
(N'RFOperations.Hybris.Autoship' ,N'SubTotal',N'money' ,N' ' ,N'Hybris.dbo.carts' ,N'p_subtotal' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--p_subtotal
(N'RFOperations.Hybris.Autoship' ,N'Total',N'money' ,N' ' ,N'Hybris.dbo.carts' ,N'p_totalprice' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--p_totalprice
(N'RFOperations.Hybris.Autoship' ,N'QV',N'money' ,N' ' ,N'Hybris.dbo.carts' ,N'p_totalsv' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--p_totalsv
(N'RFOperations.Hybris.Autoship' ,N'CV',N'money' ,N' ' ,N'Hybris.dbo.carts' ,N'p_cv' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--p_cv
(N'RFOperations.Hybris.Autoship' ,N'StartDate',N'datetime' ,N' ' ,N'Hybris.dbo.carts' ,N'createdTS' ,N'datetime' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--createdTS
(N'RFOperations.Hybris.Autoship' ,N'NextRunDate',N'datetime' ,N' ' ,N'Hybris.dbo.carts' ,N'p_nextrundate' ,N'datetime' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--p_nextrundate			
(N'RFOperations.Hybris.Autoship' ,N'TaxExempt',N'bit' ,N' ' ,N'Hybris.dbo.carts' ,N'p_istaxexempt' ,N'bolean' ,N'AutoshipNumber' ,N'defaulted to 0' , N'c2c' ,N'Autoship',N'') 
,--p_istaxexempt
(N'RFOperations.Hybris.Autoship' ,N'ServerModifiedDate',N'datetime' ,N' ' ,N'Hybris.dbo.carts' ,N'ModifiedTS' ,N'datetime' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--ModifiedTS
(N'RFOperations.Hybris.Autoship' ,N'ConsultantID',N'bigint' ,N' ' ,N'Hybris.dbo.carts' ,N'p_ordersponsorid' ,N'bigint' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--p_ordersponsorid
(N'RFOperations.Hybris.Autoship' ,N'AutoshipNumber',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.carts' ,N'p_code' ,N'nvarchar(250)' ,N'AutoshipNumber' ,N'' , N'Key' ,N'Autoship',N'') 
,--p_code
(N'RFOperations.Hybris.Autoship' ,N'TotalTax',N'money' ,N' ' ,N'Hybris.dbo.carts' ,N'p_totaltax' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--p_totaltax
(N'RFOperations.Hybris.Autoship' ,N'TotalDiscount',N'money' ,N' ' ,N'Hybris.dbo.carts' ,N'P_totaldiscounts' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--P_totaldiscounts
(N'RFOperations.Hybris.Autoship' ,N'donotship',N'bit' ,N' ' ,N'Hybris.dbo.carts' ,N'p_donotship' ,N'bolean' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--p_donotship
(N'RFOperations.Hybris.AutoshipShipment' ,N'ShippingCost',N'money' ,N' ' ,N'Hybris.dbo.carts' ,N'p_deliverycost' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--p_deliverycost
(N'RFOperations.Hybris.AutoshipShipment' ,N'TaxOnShippingCost',N'money' ,N' ' ,N'Hybris.dbo.carts' ,N'p_taxonshippingcost' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Autoship',N'') 
,--p_taxonshippingcost

/* 
 Hybris system default Autoship attributes will be here .
*/

/* Autoship Items Level */

(N'RFOperations.Hybris.AutoshipItem' ,N'AutoshipId',N'bigint' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'Ownerpkstring' ,N'bigint' ,N'AutoshipNumber' ,N'Hybris.dbo.carts' , N'ref' ,N'AutoshipItem',N'') 
,--Typepkstring
(N'RFOperations.Hybris.AutoshipItem' ,N'LineItemNo',N'int' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_entrynumber' ,N'int' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipItem',N'') 
,--Typepkstring				
(N'RFOperations.Hybris.AutoshipItem' ,N'ProductID',N'bigint' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_product' ,N'bigint' ,N'AutoshipNumber' ,N'PK of Products with Catlog and CatlogVersion' , N'ref' ,N'AutoshipItem',N'') 
,--p_product		
(N'RFOperations.Hybris.AutoshipItem' ,N'Quantity',N'int' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_quantity' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipItem',N'') 
,--p_quantity	
(N'RFOperations.Hybris.AutoshipItem' ,N'BasePrice',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_baseprice' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipItem',N'') 
,--p_baseprice	
(N'RFOperations.Hybris.AutoshipItem' ,N'TotalPrice',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_totalprice' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipItem',N'') 
,--p_totalprice	
(N'RFOperations.Hybris.AutoshipItem' ,N'TotalTax',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_taxvaluesinternal' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipItem',N'') 
,--p_taxvaluesinternal	
(N'RFOperations.Hybris.AutoshipItem' ,N'CV',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_cv' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipItem',N'') 
,--p_cv	
(N'RFOperations.Hybris.AutoshipItem' ,N'QV',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_totalsv' ,N'decimal(30,8)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipItem',N'') 
,--p_totalsv	
(N'RFOperations.Hybris.AutoshipItem' ,N'ServerModifiedDate',N'datetime' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'modifiedTS' ,N'datetime' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipItem',N'') 
,--modifiedTS			
/* 
 Hybris system default AutoshipItem attributes will be here .
*/



/* Autoship Items Level */

(N'RFOperations.Hybris.AutoshipPayment' ,N'AutoshipId',N'bigint' ,N'PK of Autoship Templates ' ,N'Hybris.dbo.Paymentinfos' ,N'Ownerpkstring' ,N'bigint' ,N'AutoshipNumber' ,N'Hybris.dbo.carts' , N'ref' ,N'AutoshipPayment',N'') 
,--Typepkstring
(N'RFOperations.Hybris.AutoshipPayment' ,N'AutoshipPaymentID',N'bigint' ,N'  ' ,N'Hybris.dbo.Paymentinfos' ,N'p_code' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipPayment',N'') 
,--p_code
(N'RFOperations.Hybris.AutoshipPayment' ,N'VendorID',N'bigint' ,N'  ' ,N'Hybris.dbo.Paymentinfos' ,N'p_type' ,N'bigint' ,N'AutoshipNumber' ,N'Hybris.dbo.enumsvalues' , N'ref' ,N'AutoshipPayment',N'') 
,--p_type
(N'RFOperations.Hybris.AutoshipPayment' ,N'Expmonth',N'bigint' ,N'  ' ,N'Hybris.dbo.Paymentinfos' ,N'p_validtomonth' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipPayment',N'') 
,--p_validtomonth
(N'RFOperations.Hybris.AutoshipPayment' ,N'ExpYear',N'bigint' ,N'  ' ,N'Hybris.dbo.Paymentinfos' ,N'p_validtoyear' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipPayment',N'') 
,--p_validtoyear
(N'RFOperations.Hybris.AutoshipPayment' ,N'DisplayNumber',N'nvarchar(400)' ,N'  ' ,N'Hybris.dbo.Paymentinfos' ,N'p_number' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipPayment',N'') 
,--p_number
(N'RFOperations.Hybris.AutoshipPaymentAddress' ,N'Name',N'nvarchar(400)' ,N' LTRIM(RTRIM(CONCAT(BillingFirstName, '' '', BillingLastName))) ' ,N'Hybris.dbo.Paymentinfos' ,N'p_ccowner' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipPayment',N'') 
,--p_ccowner
(N'RFOperations.Hybris.paymentprofiles' ,N'paymentprofileID',N'bigint' ,N'  ' ,N'Hybris.dbo.Paymentinfos' ,N'p_original' ,N'bigint' ,N'AutoshipNumber' ,N' PK of users paymentinfos' , N'ref' ,N'AutoshipPayment',N'') 
,--p_original	
(N'' ,N'',N'' ,N' Hybris Update Attributes ' ,N'Hybris.dbo.Paymentinfos' ,N'p_billingaddress' ,N'bigint' ,N'AutoshipNumber' ,N' PK of BillingAddress' , N'ref' ,N'AutoshipPayment',N'') 
,--p_billingaddress							
	
					
/* 
 Hybris system default AutoshipPayment attributes will be here .
*/



/* Autoship PaymentAddress Level */

(N'RFOperations.Hybris.AutoshipPaymentAddresses' ,N'FirstName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_firstname' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipPaymentAddress',N'') 
,--p_firstname
(N'RFOperations.Hybris.AutoshipPaymentAddresses' ,N'LastName',N'nvarchar(250)' ,N'  ' ,N'Hybris.dbo.addresses' ,N'p_lastname' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipPaymentAddress',N'') 
,--p_lastname
(N'RFOperations.Hybris.AutoshipPaymentAddresses' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_country' ,N'bigint' ,N'AutoshipNumber' ,N' Hybris.dbo.countries' , N'ref' ,N'AutoshipPaymentAddress',N'') 
,--p_country								
(N'RFOperations.Hybris.AutoshipPaymentAddresses' ,N'Address1',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetname' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipPaymentAddress',N'') 
,--p_streetname								
(N'RFOperations.Hybris.AutoshipPaymentAddresses' ,N'AddressLine2',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetNumber' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipPaymentAddress',N'') 
,--p_streetNumber	
(N'RFOperations.Hybris.AutoshipPaymentAddresses' ,N'PostalCode',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_postalcode' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipPaymentAddress',N'') 
,--p_postalcode
(N'RFOperations.Hybris.AutoshipPaymentAddresses' ,N'Locale',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_town' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'AutoshipPaymentAddress',N'') 
,--p_town
(N'RFOperations.Hybris.AutoshipPaymentAddresses' ,N'Region',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_region' ,N'bigint' ,N'AutoshipNumber' ,N' Hybris.dbo.regions' , N'ref' ,N'AutoshipPaymentAddress',N'') 
,--p_region
(N'RFOperations.Hybris.AutoshipPaymentAddresses' ,N'Telephone',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_phone1' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'AutoshipPaymentAddress',N'') 
,--p_phone1
(N'RFOperations.Hybris.AutoshipPaymentAddresses' ,N'MiddleName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_middlename' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'AutoshipPaymentAddress',N'') 
,--p_middlename
(N'RFOperations.RFO_Account.Accountcontacts' ,N'DateOfBirth',N'datetime' ,N'if 1900 then NULL ' ,N'Hybris.dbo.addresses' ,N'p_dateofbirth' ,N'datetime' ,N'AutoshipNumber' ,N'if 1900 then NULL' , N'c2c' ,N'AutoshipPaymentAddress',N'') 
,--p_dateofbirth
(N'RFOperations.RFO_Account.Accountcontacts' ,N'GenderID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_gender' ,N'bigint' ,N'AutoshipNumber' ,N' Hybris.dbo.enumsValues' , N'ref' ,N'AutoshipPaymentAddress',N'') 
,--p_gender
(N' ' ,N' ',N' ' ,N' Hybris Reference attributes' ,N'Hybris.dbo.addresses' ,N'OwnerPkString' ,N'bigint' ,N'AutoshipNumber' ,N'Pk of Paymentinfos' , N'n/a' ,N'AutoshipPaymentAddress',N'') 
,--OwnerPkString
(N' ' ,N' ',N' ' ,N' Hybris Reference attributes' ,N'Hybris.dbo.addresses' ,N'p_billingaddress' ,N'tinyint' ,N'AutoshipNumber' ,N'def to 1' , N'def' ,N'AutoshipPaymentAddress',N'') 
,--p_billingaddress
(N' ' ,N' ',N' ' ,N' Hybris default attributes' ,N'Hybris.dbo.addresses' ,N'p_duplicate' ,N'tinyint' ,N'AutoshipNumber' ,N'def to 1' , N'def' ,N'AutoshipPaymentAddress',N'') 
,--p_duplicate



/* 
 Hybris system default Autoship PaymentAddress attributes will be here .
*/



/* Autoship ShippingAddress Level */

(N'RFOperations.RFO_Accounts.AutoshipShippingAddress' ,N'FirstName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_firstname' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipShippingAddress',N'') 
,--p_firstname
(N'RFOperations.RFO_Accounts.AutoshipShippingAddress' ,N'LastName',N'nvarchar(250)' ,N'  ' ,N'Hybris.dbo.addresses' ,N'p_lastname' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipShippingAddress',N'') 
,--p_lastname
(N'RFOperations.RFO_Accounts.AutoshipShippingAddress' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_country' ,N'bigint' ,N'AutoshipNumber' ,N' Hybris.dbo.countries' , N'ref' ,N'AutoshipShippingAddress',N'') 
,--p_country								
(N'RFOperations.RFO_Accounts.AutoshipShippingAddress' ,N'Address1',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetname' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipShippingAddress',N'') 
,--p_streetname								
(N'RFOperations.RFO_Accounts.AutoshipShippingAddress' ,N'AddressLine2',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetNumber' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipShippingAddress',N'') 
,--p_streetNumber	
(N'RFOperations.RFO_Accounts.AutoshipShippingAddress' ,N'PostalCode',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_postalcode' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N' ' , N'c2c' ,N'AutoshipShippingAddress',N'') 
,--p_postalcode
(N'RFOperations.RFO_Accounts.AutoshipShippingAddress' ,N'Locale',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_town' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'AutoshipShippingAddress',N'') 
,--p_town
(N'RFOperations.RFO_Accounts.AutoshipShippingAddress' ,N'Region',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_region' ,N'bigint' ,N'AutoshipNumber' ,N' Hybris.dbo.regions' , N'ref' ,N'AutoshipShippingAddress',N'') 
,--p_region
(N'RFOperations.RFO_Accounts.AutoshipShippingAddress' ,N'Telephone',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_phone1' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'AutoshipShippingAddress',N'') 
,--p_phone1
(N'RFOperations.RFO_Accounts.AutoshipShippingAddress' ,N'MiddleName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_middlename' ,N'nvarchar(255)' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'AutoshipShippingAddress',N'') 
,--p_middlename
(N'RFOperations.RFO_Account.Accountcontacts' ,N'DateOfBirth',N'datetime' ,N'if 1900 then NULL ' ,N'Hybris.dbo.addresses' ,N'p_dateofbirth' ,N'datetime' ,N'AutoshipNumber' ,N'if 1900 then NULL' , N'c2c' ,N'AutoshipShippingAddress',N'') 
,--p_dateofbirth
(N'RFOperations.RFO_Account.Accountcontacts' ,N'GenderID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_gender' ,N'bigint' ,N'AutoshipNumber' ,N' Hybris.dbo.enumsValues' , N'ref' ,N'AutoshipShippingAddress',N'') 
,--p_gender
(N' ' ,N' ',N' ' ,N' Hybris Reference attributes' ,N'Hybris.dbo.addresses' ,N'OwnerPkString' ,N'bigint' ,N'AutoshipNumber' ,N'Pk of AutoshipTemplates' , N'n/a' ,N'AutoshipShippingAddress',N'') 
,--OwnerPkString
(N' ' ,N' ',N' ' ,N' Hybris Reference attributes' ,N'Hybris.dbo.addresses' ,N'p_shippingaddress' ,N'tinyint' ,N'AutoshipNumber' ,N'def to 1' , N'def' ,N'AutoshipShippingAddress',N'') 
,--p_shippingaddress
(N' ' ,N' ',N' ' ,N' Hybris default attributes' ,N'Hybris.dbo.addresses' ,N'p_duplicate' ,N'tinyint' ,N'AutoshipNumber' ,N'def to 1' , N'def' ,N'AutoshipShippingAddress',N'') 
--p_duplicate


/* 
 Hybris system default Autoship ShippingAddress attributes will be here .
*/



--********************************************************************************************
--												Orders 
--********************************************************************************************


USE DM_QA
GO 


INSERT INTO  dbqa.Map_tab
        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
VALUES 

/* Orders Header Level */

(N'RFOperations.Hybris.Orders' ,N'OrderNumber',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.orders' ,N'p_code' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'Key' ,N'Orders',N'') 
,--p_code
(N'RFOperations.Hybris.Orders' ,N'AutoShipID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_associatedOrders' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.Carts' , N'ref' ,N'Orders',N'') 
,--p_associatedOrders

(N'RFOperations.Hybris.Orders' ,N'OrderStatusID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_status' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumerationvalues' , N'ref' ,N'Orders',N'') 
,--p_associatedOrders
(N'RFOperations.Hybris.Orders' ,N'OrderTypeID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_type' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumerationvalues' , N'ref' ,N'Orders',N'') 
,--p_type
(N'RFOperations.Hybris.Orders' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_country' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.countries' , N'ref' ,N'Orders',N'') 
,--p_country
(N'RFOperations.Hybris.Orders' ,N'CurrencyID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_currency' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.Currencies' , N'ref' ,N'Orders',N'') 
,--p_currency
(N'RFOperations.Hybris.Orders' ,N'ConsultantID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_ordersponsorid' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.users' , N'ref' ,N'Orders',N'') 
,--p_ordersponsorid
(N'RFOperations.Hybris.Orders' ,N'AccountID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_userpk' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.users' , N'ref' ,N'Orders',N'') 
,--p_userpk
(N'RFOperations.Hybris.Orders' ,N'CompletionDate',N'datetime' ,N' ' ,N'Hybris.dbo.orders' ,N'createdTS' ,N'datetime' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--createdTS
(N'RFOperations.Hybris.Orders' ,N'CommissionDate',N'datetime' ,N' ' ,N'Hybris.dbo.orders' ,N'p_commissiondate' ,N'datetime' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_commissiondate
(N'RFOperations.Hybris.Orders' ,N'ModifiedDate',N'datetime' ,N' ' ,N'Hybris.dbo.orders' ,N'modifiedTS' ,N'datetime' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--modifiedTS
(N'RFOperations.Hybris.Orders' ,N'SubTotal',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_subtotal' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_subtotal
(N'RFOperations.Hybris.Orders' ,N'Total',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totalprice' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_totalprice
(N'RFOperations.Hybris.Orders' ,N'TotalTax',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totaltax' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_totaltax
(N'RFOperations.Hybris.Orders' ,N'TotalDiscount',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totaldiscount' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_totaldiscount
(N'RFOperations.Hybris.Orders' ,N'CV',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_cv' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_cv
(N'RFOperations.Hybris.Orders' ,N'QV',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totalsv' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_totalsv
(N'RFOperations.Hybris.Orders' ,N'TaxExempt',N'bit' ,N' ' ,N'Hybris.dbo.orders' ,N'isTaxExempt' ,N'boolean' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--isTaxExempt
(N'RFOperations.Hybris.Orders' ,N'donotship',N'bit' ,N' ' ,N'Hybris.dbo.orders' ,N'p_donotship' ,N'boolean' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_donotship
(N'RFOperations.Hybris.OrderShipment' ,N'ShippingCost',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_deliverycost' ,N'decimal(30,8)' ,N'OrderNumber' ,N' Sum with handlingCost' , N'c2c' ,N'Orders',N'') 
,--p_deliverycost
(N'RFOperations.Hybris.OrderShipment' ,N'TaxOnShippingCost',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_taxonshippingcost' ,N'decimal(30,8)' ,N'OrderNumber' ,N' Sum with TaxOnHandling' , N'c2c' ,N'Orders',N'') 
,--p_deliverycost


/* 
 Hybris system default Orders attributes will be here .
*/



/* Order Items Level */

(N'RFOperations.Hybris.OrderItems' ,N'OrderId',N'bigint' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_order' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.orders ' , N'key' ,N'OrderItems',N'') 
,--p_order
(N'RFOperations.Hybris.OrderItems' ,N'LineItemNo',N'int' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_entrynumber' ,N'int' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_entrynumber
(N'RFOperations.Hybris.OrderItems' ,N'ProductID',N'bigint' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_product' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.Products ' , N'ref' ,N'OrderItems',N'') 
,--p_product
(N'RFOperations.Hybris.OrderItems' ,N'Quantity',N'int' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_quantity' ,N'int' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_quantity
(N'RFOperations.Hybris.OrderItems' ,N'BasePrice',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_baseprice' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_baseprice
(N'RFOperations.Hybris.OrderItems' ,N'TotalPrice',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_totalprice' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_totalprice
(N'RFOperations.Hybris.OrderItems' ,N'TotalTax',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_taxvaluesinternal' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_taxvaluesinternal
(N'RFOperations.Hybris.OrderItems' ,N'CV',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_cv' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_cv
(N'RFOperations.Hybris.OrderItems' ,N'QV',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_totalsv' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_totalsv


/* 
 Hybris system default Order Items  attributes will be here .
*/



/* Order Payments Level */

(N'RFOpetaions.Hybris.OrderPayment' ,N'OrderPaymentID',N'bigint' ,N' ' ,N'Hybris.dbo.paymentInfos' ,N'p_code' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'key' ,N'OrderPayment',N'') 
,--p_code
(N' ' ,N' ',N'' ,N'Hybris Reference Attribute' ,N'Hybris.dbo.paymentInfos' ,N'OwnerpkString' ,N'bigint' ,N'OrderNumber' ,N'hybris.dbo.orders' , N'key' ,N'OrderPayment',N'') 
,--Ownerpkstring
(N'RFOpetaions.Hybris.OrderPayment' ,N'AmountTobeAuthorized',N'money' ,N'Hybris Reference Attribute ' ,N'Hybris.dbo.paymentInfos' ,N'p_plannedAmount' ,N'decimal(30,8)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--p_plannedAmount
(N'RFOpetaions.Hybris.OrderPayment' ,N'ExpYear',N'bigint' ,N' ' ,N'Hybris.dbo.paymentInfos' ,N'p_validtoyear' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--p_validtoyear
(N'RFOpetaions.Hybris.OrderPayment' ,N'Expmonth',N'bigint' ,N'  ' ,N'Hybris.dbo.paymentInfos' ,N'p_validtomonth' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--p_validtomonth
(N'RFOpetaions.Hybris.OrderPayment' ,N'VendorID',N'bigint' ,N'Reference.CreditCardTypes' ,N'Hybris.dbo.paymentInfos' ,N'p_type' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enusValue' , N'ref' ,N'OrderPayment',N'') 
,--p_type
(N'RFOpetaions.Hybris.OrderPayment' ,N'paymentprovider',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.paymentTrasactions' ,N'paymentprovider' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--paymentprovider
(N'RodanFieldsLive.dbo.OrderPayments' ,N'AccountNumber',N'nvarchar' ,N'' ,N'Hybris.dbo.paymentInfos' ,N'p_number' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--p_number
(N'RodanFieldsLive.dbo.OrderPayments' ,N'BillingName',N'nvarchar' ,N'' ,N'Hybris.dbo.paymentInfos' ,N'p_ccowner' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--p_ccowner


/* 
 Hybris system default Order payments  attributes will be here .
*/



/* Order BillingAddress Level */

(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_country' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.countries' , N'ref' ,N'OrderPaymentAddress',N'') 
,--p_country
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'FirstName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_firstname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_firstname
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'LastName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_lastname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_lastname
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'Address1',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_streetname
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'AddressLine2',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetNumber' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_streetNumber
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'PostalCode',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_postalcode' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_postalcode
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'Locale',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_town' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_town
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'Region',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_region' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.regions' , N'ref' ,N'OrderPaymentAddress',N'') 
,--p_region
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'Telephone',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_phone1' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_phone1
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'MiddleName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_middlename' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_middlename
(N'RFOpetaions.RFO_Accounts.Accountcontacts' ,N'BirthDay',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_dateofbirth' ,N'datetime' ,N'OrderNumber' ,N'If 1900 then Null' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_dateofbirth
(N'RFOpetaions.RFO_Accounts.Accountcontacts' ,N'GenderID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_gender' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumValues' , N'ref' ,N'OrderPaymentAddress',N'') 
,--p_gender	
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'p_duplicate' ,N'tinyint' ,N'OrderNumber' ,N'Defaulted to 1' , N'def' ,N'OrderPaymentAddress',N'') 
,--p_duplicate	
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'p_billingaddress' ,N'tinyint' ,N'OrderNumber' ,N'Defaulted to 1' , N'def' ,N'OrderPaymentAddress',N'') 
,--p_billingaddresses	
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'Ownerpkstring' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.paymentinfos' , N'Key' ,N'OrderPaymentAddress',N'') 
,--Ownerpkstring	


/* 
 Hybris system default OrderPaymentAddress  attributes will be here .
*/



/* Order ShippingAddress Level */

(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_country' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.countries' , N'ref' ,N'OrderShippingAddress',N'') 
,--p_country
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'FirstName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_firstname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_firstname
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'LastName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_lastname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_lastname
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'Address1',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_streetname
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'AddressLine2',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetNumber' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_streetNumber
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'PostalCode',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_postalcode' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_postalcode
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'Locale',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_town' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_town
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'Region',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_region' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.regions' , N'ref' ,N'OrderShippingAddress',N'') 
,--p_region
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'Telephone',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_phone1' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_phone1
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'MiddleName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_middlename' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_middlename
(N'RFOpetaions.RFO_Accounts.Accountcontacts' ,N'BirthDay',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_dateofbirth' ,N'datetime' ,N'OrderNumber' ,N'If 1900 then Null' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_dateofbirth
(N'RFOpetaions.RFO_Accounts.Accountcontacts' ,N'GenderID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_gender' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumValues' , N'ref' ,N'OrderShippingAddress',N'') 
,--p_gender	
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'p_duplicate' ,N'tinyint' ,N'OrderNumber' ,N'Defaulted to 1' , N'def' ,N'OrderShippingAddress',N'') 
,--p_duplicate	
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'p_Shippingaddress' ,N'tinyint' ,N'OrderNumber' ,N'Defaulted to 1' , N'def' ,N'OrderShippingAddress',N'') 
,--p_Shippingaddress				
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'Ownerpkstring' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.orders' , N'Key' ,N'OrderShippingAddress',N'') 
,--Ownerpkstring	
		


/* 
 Hybris system default Order ShippingAddress  attributes will be here .
*/



/* OrderPaymentTransaction and Entries Level */

(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'OrderPaymentID',N'bigint' ,N' ' ,N'Hybris.dbo.PaymentTransactions' ,N'OwnerPkstring' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.paymentinfos' , N'Key' ,N'OrderPaymentTransaction',N'') 
,--OwnerPkstring
(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'AmountAuthorized',N'money' ,N' ' ,N'Hybris.dbo.PaymentTransactions' ,N'p_plannedAmount' ,N'decimal(30,8)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_plannedAmount
(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'TransactionID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.PaymentTransactions' ,N'p_requestID' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_requestID
(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'ProcessDate',N'datetime' ,N' ' ,N'Hybris.dbo.PaymentTransactionEntries' ,N'p_time' ,N'datetime' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_time
(N'RFOperations.Hybris.OrderPayment' ,N'PaymentProvider',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.PaymentTransactions' ,N'p_paymentprovider' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_paymentprovider
(N'RFOperations.Hybris.OrderPayment' ,N'OrderPaymentTransactionID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.PaymentTransactionEntries' ,N'p_code' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_code
(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'AmountAuthorized',N'money' ,N' ' ,N'Hybris.dbo.PaymentTransactionEntries' ,N'p_amount' ,N'decimal(30,8)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_amount
(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'TransactionID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.PaymentTransactionEntries' ,N'p_requestID' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_amount
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.PaymentTransactionEntries' ,N'p_paymenttransaction' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.PaymentTransaction' , N'Key' ,N'OrderPaymentTransaction',N'') 
,--p_paymenttransaction





/* 
 Hybris system default OrderPaymentTransaction and Entries  attributes will be here .
*/



/* Order Consignments and Entries Level */

(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'Quantity',N'int' ,N' ' ,N'Hybris.dbo.consignmentEntries' ,N'p_quantity' ,N'bigint' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderConsignments',N'') 
,--p_quantity
(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'Quantity',N'int' ,N' ' ,N'Hybris.dbo.consignmentEntries' ,N'p_shippedquantity' ,N'bigint' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderConsignments',N'') 
,--p_shippedquantity
(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'TrackingNumber',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.consignments' ,N'p_trackingid' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderConsignments',N'') 
,--p_trackingid
(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'ShipDate',N'datetime' ,N' ' ,N'Hybris.dbo.consignments' ,N'p_shippingdate' ,N'datetime' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderConsignments',N'') 
,--p_shippingdate
(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'OrderID',N'bigint' ,N' ' ,N'Hybris.dbo.consignments' ,N'P_code' ,N'nvarchar(255)' ,N'OrderNumber' ,N'Hybris.dbo.orders' , N'ref' ,N'OrderConsignments',N'') 
,--P_code
(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'ShipStatus',N'nvarchar(25)' ,N'Reference.ShippingStatus' ,N'Hybris.dbo.consignments' ,N'p_status' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumValues' , N'ref' ,N'OrderConsignments',N'') 
,--p_status
(N'RFOperations.Hybris.OrderShipment' ,N'ShippingMethodID',N'bigint' ,N'Reference.ShippingMethod' ,N'Hybris.dbo.consignments' ,N'p_deliverymode' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumValues' , N'ref' ,N'OrderConsignments',N'') 
,--p_deliverymode
(N'RFOperations.Hybris.OrderShipment' ,N'ShippingCarrier',N'bigint' ,N'Reference.ShippingMethod' ,N'Hybris.dbo.consignments' ,N'p_carrier' ,N'nvarchar(255)' ,N'OrderNumber' ,N'Hybris.dbo.enumValues' , N'ref' ,N'OrderConsignments',N'') 
,--p_carrier
(N'' ,N'',N'' ,N'Hybris.Reference Attributes' ,N'Hybris.dbo.consignmentEntries' ,N'p_consignment' ,N'bigint' ,N'OrderNumber' ,N'Hyrbis.dbo.Consignment' , N'key' ,N'OrderConsignments',N'') 
--p_carrier




--********************************************************************************************
--												Products 
--********************************************************************************************


USE DM_QA
GO 

INSERT INTO  dbqa.Map_tab
        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
VALUES 
/* Products Table in Hybris.*/
(N'ECC_Product_Master' ,N'MaterialTypes',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'InternalCode' ,N'bigint' ,N'CatalogNumber' ,N'Hybris.dbo.composedtypes' , N'ref' ,N'Products',N'') 
,--[Material Types] -TypepkString 
(N'ECC_Product_Master' ,N'CatalogNumber',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_code' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'Key' ,N'Products',N'') 
,-- CatalogNumber -p_code
(N'ECC_Product_Master' ,N'Status ',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_approvalstatus' ,N'bigint' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- Status- -p_approvalstatus
(N'ECC_Product_Master' ,N'UPC',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_ean' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- UPC- -P_ean
(N'ECC_Product_Master' ,N'QV',N'money' ,N'' ,N'Hybris.dbo.pricerows' ,N'p_qv' ,N'decimal(30,8)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- QV
(N'ECC_Product_Master' ,N'SV',N'money' ,N'' ,N'Hybris.dbo.pricerows' ,N'p_sv' ,N'decimal(30,8)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- SV
(N'ECC_Product_Master' ,N'ConsultantsPrice',N'money' ,N'' ,N'Hybris.dbo.pricerows' ,N'CPrice' ,N'decimal(30,8)' ,N'CatalogNumber' ,N'Where UserGroup=Consultnats' , N'ref' ,N'Products',N'') 
,-- Consultant's price
(N'ECC_Product_Master' ,N'PCPrice',N'money' ,N'' ,N'Hybris.dbo.pricerows' ,N'PPrice' ,N'decimal(30,8)' ,N'CatalogNumber' ,N'Where UserGroup=PC' , N'ref' ,N'Products',N'') 
,-- PC's price
(N'ECC_Product_Master' ,N'Currency',N'money' ,N'' ,N'Hybris.dbo.pricerows' ,N'p_currency' ,N'bigint' ,N'CatalogNumber' ,N'Hybris.dbo.currencies' , N'ref' ,N'Products',N'') 
,-- Currency
(N'ECC_Product_Master' ,N'StartTime',N'Datetime' ,N'' ,N'Hybris.dbo.pricerows' ,N'STime' ,N'datetime' ,N'CatalogNumber' ,N'Hybris.dbo.currencies' , N'c2c' ,N'Products',N'') 
,-- Start Time
(N'ECC_Product_Master' ,N'EndTime',N'Datetime' ,N'' ,N'Hybris.dbo.pricerows' ,N'ETime' ,N'datetime' ,N'CatalogNumber' ,N'Hybris.dbo.currencies' , N'c2c' ,N'Products',N'') 
,-- End Time
(N'ECC_Product_Master' ,N'ProductName',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_name' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- [ProductName]
(N'ECC_Product_Master' ,N'shortDescription',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_summary' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- [short Description]
(N'ECC_Product_Master' ,N'LongDescriptions',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_description' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,-- [Long Descriptions]
(N'ECC_Product_Master' ,N' UsageNotes',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_usagenotes' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,--  [Usage Notes]
(N'ECC_Product_Master' ,N' Ingredients',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_ingredients' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'' , N'c2c' ,N'Products',N'') 
,--  [Ingredients]
(N'ECC_Product_Master' ,N' Weight',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Productslp' ,N'p_weight' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'p_weight' , N'c2c' ,N'Products',N'') 
,--  [Weight]
(N'ECC_Product_Master' ,N' VariantType',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_varianttype' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'p_varianttype' , N'c2c' ,N'Products',N'') 
,--  [VariantType]
(N'ECC_Product_Master' ,N' Returnable',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_returnable' ,N'nvarchar(255)' ,N'CatalogNumber' ,N'p_returnable' , N'c2c' ,N'Products',N'') 
,--  [Returnable]
(N'ECC_Product_Master' ,N' unit',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_conversion' ,N'bigint' ,N'CatalogNumber' ,N'Hybris.dbo.units' , N'ref' ,N'Products',N'') 
,--  [unit]
(N'ECC_Product_Master' ,N' CATALOG',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_catalog' ,N'bigint' ,N'CatalogNumber' ,N'Hybris.dbo.Catalogs' , N'ref' ,N'Products',N'') 
,--  [CATALOG]
(N'ECC_Product_Master' ,N' CatalogVersion',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_catalogversion' ,N'bigint' ,N'CatalogNumber' ,N'Hybris.dbo.catalogversions' , N'ref' ,N'Products',N'') 
,--  [CatalogVersion]
(N'ECC_Product_Master' ,N' Manufacturer',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_manufactureraid' ,N'bigint' ,N'CatalogNumber' ,N'p_manufactureraid' , N'c2c' ,N'Products',N'') 
,--  [Manufacturer]
(N'ECC_Product_Master' ,N' ManufacturerName',N'nvarchar(255)' ,N'' ,N'Hybris.dbo.Products' ,N'p_manufacturername' ,N'bigint' ,N'CatalogNumber' ,N'p_manufacturername' , N'c2c' ,N'Products',N'') 
--  [ManufacturerName]

/*
Pending Concerns and Attributes will be added here.
*/



--********************************************************************************************
--												ReturnOrders 
--********************************************************************************************


USE DM_QA
GO

INSERT INTO   dbqa.Map_tab
        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
VALUES 

/* ReturnOrder Header Level */

(N'RFOperations.Hybris.ReturnOrder' ,N'ReturnOrderNumber',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.orders' ,N'p_code' ,N'nvarcahr(255)' ,N'ReturnNumber' ,N'' , N'Key' ,N'ReturnOrder',N'') 
,--p_code
(N'RFOperations.Hybris.ReturnOrder' ,N'OrderNumber',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_parent' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.orders' , N'ref' ,N'ReturnOrder',N'') 
,--p_parent
(N'RFOperations.Hybris.ReturnOrder' ,N'ReturnStatusID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_status' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.enumeratonvalues' , N'ref' ,N'ReturnOrder',N'') 
,--p_status
(N'RFOperations.Hybris.ReturnOrder' ,N'AccountID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_user' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.users' , N'ref' ,N'ReturnOrder',N'') 
,--p_user
(N'RFOperations.Hybris.ReturnOrder' ,N'ConsultantID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_spronsorid' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.users' , N'ref' ,N'ReturnOrder',N'') 
,--p_spronsorid
(N'RFOperations.Hybris.ReturnOrder' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_country' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.countries' , N'ref' ,N'ReturnOrder',N'') 
,--p_country
(N'RFOperations.Hybris.ReturnOrder' ,N'CurrencyID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_currency' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.currencies' , N'ref' ,N'ReturnOrder',N'') 
,--p_currency
(N'RFOperations.Hybris.ReturnOrder' ,N'CompletionDate',N'datetime' ,N' ' ,N'Hybris.dbo.orders' ,N'createdTS' ,N'datetime' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--createdTS
(N'RFOperations.Hybris.ReturnOrder' ,N'CommissionDate',N'datetime' ,N' ' ,N'Hybris.dbo.orders' ,N'p_commissiondate' ,N'datetime' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_commissiondate
(N'RFOperations.Hybris.ReturnOrder' ,N'SubTotal',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_subtotal' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_subtotal
(N'RFOperations.Hybris.ReturnOrder' ,N'Total',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totalprice' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_totalprice
(N'RFOperations.Hybris.ReturnOrder' ,N'TotalTax',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totaltax' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_totaltax
(N'RFOperations.Hybris.ReturnOrder' ,N'TotalDiscount',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totaldiscounts' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_totaldiscounts
(N'RFOperations.Hybris.ReturnOrder' ,N'CV',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_cv' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_cv
(N'RFOperations.Hybris.ReturnOrder' ,N'QV',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totalsv' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_totalsv
(N'RFOperations.Hybris.ReturnOrder' ,N'DeliveryCost',N'money' ,N' ShippingCost+HandlingCost' ,N'Hybris.dbo.orders' ,N'p_deliverycost' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_deliverycost
(N'RFOperations.Hybris.ReturnOrder' ,N'DeliveryTax',N'money' ,N'ShippingTax+HandlingTax ' ,N'Hybris.dbo.orders' ,N'p_taxondeliverycost' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'Sum with shipping Cost' , N'c2c' ,N'ReturnOrder',N'') 
,--p_taxondeliverycost
(N'RFOperations.Hybris.ReturnOrder' ,N'ReturnNumber',N'nvarchar(255)' ,N' ' ,N'Hybris.dbo.returnrequest' ,N'p_code' ,N'nvarchar(255)' ,N'ReturnNumber' ,N'' , N'key' ,N'ReturnOrder',N'') 
,--p_code- ReturnRequest
(N'RFOperations.Hybris.ReturnOrder' ,N'ReturnNumber',N'nvarchar(255)' ,N' ' ,N'Hybris.dbo.returnrequest' ,N'p_rma' ,N'nvarchar(255)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_rma- ReturnRequest
(N'RFOperations.Hybris.ReturnOrder' ,N'OrderID',N'bigint' ,N' ' ,N'Hybris.dbo.returnrequest' ,N'p_order' ,N'nvarchar(255)' ,N'ReturnNumber' ,N'Hybris.dbo.orders' , N'ref' ,N'ReturnOrder',N'') 
,--p_order- ReturnRequest



/* 
 Hybris system default ReturnOrder attributes will be here .
*/

/* ReturnOrder Items Level */

(N'RFOperations.Hybris.ReturnItem' ,N'ExpectedQuantity',N'int' ,N' ' ,N'Hybris.dbo.returnentry' ,N'p_expectedquantity' ,N'int' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_expectedquantity
(N'RFOperations.Hybris.ReturnItem' ,N'CV',N'money' ,N' ' ,N'Hybris.dbo.orderentries' ,N'p_cv' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_cv
(N'RFOperations.Hybris.ReturnItem' ,N'QV',N'money' ,N' ' ,N'Hybris.dbo.orderentries' ,N'p_totalsv' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_totalsv
(N'RFOperations.Hybris.ReturnItem' ,N'ReceivedQuantity',N'int' ,N' ' ,N'Hybris.dbo.returnentry' ,N'p_Receivedquantity' ,N'int' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_Receivedquantity
(N'RFOperations.Hybris.ReturnItem' ,N'BasePrice',N'money' ,N' ' ,N'Hybris.dbo.orderentries' ,N'p_baseprice' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_baseprice
(N'RFOperations.Hybris.ReturnItem' ,N'TotalPrice',N'money' ,N' ' ,N'Hybris.dbo.orderentries' ,N'p_totalprice' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_totalprice
(N'RFOperations.Hybris.ReturnItem' ,N'totaltax',N'nvarchar(255)' ,N' '  ,N'Hybris.dbo.orderentries' ,N'p_taxvaluesinternal' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'ref' ,N'ReturnItem',N'') 
,--p_taxvaluesinternal
(N'RFOperations.Hybris.ReturnItem' ,N'ReceivedQuantity',N'int' ,N' '  ,N'Hybris.dbo.orderentries' ,N'p_quantity' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_quantity
(N'RFOperations.Hybris.ReturnItem' ,N'ReturnOrderID',N'bigint' ,N' '  ,N'Hybris.dbo.orderentries' ,N'p_order' ,N'bigint' ,N'ReturnNumber' ,N' ' , N'key' ,N'ReturnItem',N'') 
,--p_order
(N'RFOperations.Hybris.ReturnItem' ,N'ReturnReasonID',N'bigint' ,N' '  ,N'Hybris.dbo.returnentry' ,N'p_reason' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.enumVaue value=RFRefund ' , N'ref' ,N'ReturnItem',N'') 
,--p_reason
(N'RFOperations.Hybris.ReturnItem' ,N'ReturnStatusID',N'bigint' ,N' '  ,N'Hybris.dbo.returnentry' ,N'p_status' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.enumValue Value=ReturnStatus ' , N'ref' ,N'ReturnItem',N'') 
,--p_status	
(N'RFOperations.Hybris.ReturnItem' ,N'ReturnNotes',N'bigint' ,N' '  ,N'Hybris.dbo.returnentry' ,N'p_notes' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.enumValue Value=returnnotes ' , N'ref' ,N'ReturnItem',N'') 
,--p_notes	
(N'RFOperations.Hybris.ReturnItem' ,N'Action',N'nvarchar(255)' ,N' '  ,N'Hybris.dbo.returnentry' ,N'p_action' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.enumvalue value=ReturnAction ' , N'ref' ,N'ReturnItem',N'') 
,--p_action	
(N'RFOperations.Hybris.ReturnItem' ,N'ProductID',N'bigint' ,N' '  ,N'Hybris.dbo.orderentries' ,N'p_product' ,N'bigint' ,N'ReturnNumber' ,N' ' , N'ref' ,N'ReturnItem',N'') 
,--p_product
(N'' ,N'',N'' ,N'Hybris System Attributes '  ,N'Hybris.dbo.Returnentry' ,N'p_returnrequest' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.ReturnRequest' , N'key' ,N'ReturnItem',N'') 
,--p_returnrequest
(N'RFOperations.Hybris.ReturnItem' ,N'OrderItemID',N'bigint' ,N' '  ,N'Hybris.dbo.Returnentry' ,N'p_orderentry' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.orderEntries(orginal order)' , N'ref' ,N'ReturnItem',N'') 
,--p_product




/* 
 Hybris system default ReturnOrder Items attributes will be here .
*/

/* ReturnPayment  Level */

(N'RFOpetions.Hybris.ReturnPayment' ,N'ReturnOrderID',N'bigint' ,N' ' ,N'Hybris.dbo.paymentinfos' ,N'OwnerPkstring' ,N'bigint' ,N'ReturnNumber' ,N' ' , N'Key' ,N'ReturnPayment',N'') 
,--OwnerPkstring
(N'RFOpetions.Hybris.ReturnPayment' ,N'VendorID',N'bigint' ,N' Reference.CreditCardTypes' ,N'Hybris.dbo.paymentinfos' ,N'p_type' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.enumValue' , N'ref' ,N'ReturnPayment',N'') 
,--p_type
(N'RFOpetions.Hybris.ReturnPayment' ,N'ExpYear',N'bigint' ,N' ' ,N'Hybris.dbo.paymentinfos' ,N'p_validtoyear' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_validtoyear
(N'RFOpetions.Hybris.ReturnPayment' ,N'Expmonth',N'bigint' ,N' ' ,N'Hybris.dbo.paymentinfos' ,N'p_validtomonth' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_validtomonth
(N'RFOpetions.Hybris.ReturnPayment' ,N'AmountToBeAuthorized',N'bigint' ,N' ' ,N'Hybris.dbo.paymenttransactions' ,N'p_plannedamount' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_plannedamount
(N'RodanFieldslive.dbo.Orderpayment' ,N'number,Validtomonth,validtoyear',N'nvarchar' ,N' CONCAT(SUBSTRING(number, 1, 6),SUBSTRING(number,( LEN(number) - 3 ), 4),validToMonth, validToYear)' ,N'Hybris.dbo.paymentinfos' ,N'p_subscriptionid' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_subscriptionid
(N'RodanFieldslive.dbo.Orderpayment' ,N'Name',N'nvarchar(255)' ,N'BillingFirstName,LastName ' ,N'Hybris.dbo.paymentinfos' ,N'p_ccowner' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_ccowner
(N'RodanFieldslive.dbo.Orderpayment' ,N'AccountNumber',N'nvarchar(255)' ,N'BillingFirstName,LastName ' ,N'Hybris.dbo.paymentinfos' ,N'p_number' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_number
(N'' ,N'',N'' ,N' Hybris System Attributes' ,N'Hybris.dbo.paymentinfos' ,N'p_user' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.users' , N'ref' ,N'ReturnPayment',N'') 
,--p_user
(N'' ,N'',N'' ,N' Hybris System Attributes' ,N'Hybris.dbo.paymentinfos' ,N'p_duplicate' ,N'tinyint' ,N'ReturnNumber' ,N' Defaulted to 1' , N'def' ,N'ReturnPayment',N'') 
,--p_duplicate						
(N'' ,N'',N'' ,N' Hybris System Attributes' ,N'Hybris.dbo.paymentinfos' ,N'p_original' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.paymentinfos (Orginal Orders)' , N'def' ,N'ReturnPayment',N'') 
,--p_original			


/* 
 Hybris system default ReturnPayment  attributes will be here .
*/



/* ReturnPayment Transactions Level */

(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'ReturnPaymentId',N'bigint' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_code' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'Key' ,N'ReturnPaymentTransactions',N'') 
,--p_code
(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'AmountAuthorized',N'money' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_plannedamount' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_plannedamount
(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'TransactionID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.payementtransactions' ,N'p_requestid' ,N'	nvarchar(255) ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_requestid
(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'AuthorizeType',N'nvarchar(50)' ,N' CASE AuthorizeType
 WHEN CREDIT THEN ACCEPTED
WHEN AUTH_CAPTURE THEN ACCEPTED
 WHEN SALE THEN ACCEPTED
  WHEN VOID THEN ERROR' ,N'Hybris.dbo.payementtransactionentries' ,N'p_transactionStatus' ,N'	bigint ' ,N'ReturnNumber' ,N'Hybris.dbo.enumvalues ' , N'ref' ,N'ReturnPaymentTransactions',N'') 
,--p_transactionStatus						
(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'AuthorizeType',N'nvarchar(50)' ,N' CASE AuthorizeType
WHEN CREDIT THEN SUCCESFULL
WHEN AUTH_CAPTURE THEN SUCCESFULL
WHEN SALE THEN SUCCESFULL
WHEN VOID THEN UNKNOWN_CODE' ,N'Hybris.dbo.payementtransactionentries' ,N'p_transactionStatusdetails' ,N'	bigint ' ,N'ReturnNumber' ,N'Hybris.dbo.enumvalues ' , N'ref' ,N'ReturnPaymentTransactions',N'') 
,--p_transactionStatusdetails	
(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'ProcessDate',N'datetime' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_time' ,N'	datetime ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_time	
(N'' ,N'',N'' ,N' Hybris system Attributes' ,N'Hybris.dbo.payementtransactions' ,N'p_order' ,N'bigint ' ,N'ReturnNumber' ,N' Hybris.dbo.paymentinfos originalpk' , N'key' ,N'ReturnPaymentTransactions',N'') 
,--p_order	
(N'' ,N'',N'' ,N' Hybris system Attributes' ,N'Hybris.dbo.payementtransactions' ,N'p_info' ,N'bigint ' ,N'ReturnNumber' ,N' Hybris.dbo.paymentinfos originalpk' , N'key' ,N'ReturnPaymentTransactions',N'') 
,--p_info	
(N'RFOpetions.Hybris.ReturnPayment' ,N'TokenID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.payementtransactions' ,N'p_requesttoken' ,N'	nvarchar(255) ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_requesttoken
(N'RFOpetions.Hybris.ReturnPayment' ,N'TransactionID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_requestid' ,N'	nvarchar(255) ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_requesttoken-Entries
(N'RFOpetions.Hybris.ReturnPayment' ,N'AmountAuthorized',N'money' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_amount' ,N'	decimal(30,8) ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_amount-Entries
(N'RFOpetions.Hybris.ReturnPayment' ,N'TokenID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_requesttoken' ,N'	nvarchar(255) ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_requesttoken-Entries
(N'RFOpetions.Hybris.ReturnPayment' ,N'PaymentTypeID',N'bigint' ,N' ' ,N'Hybris.dbo.payementtransactions' ,N'p_type' ,N'bigint ' ,N'ReturnNumber' ,N' Hybris.dbo.enumvalues' , N'key' ,N'ReturnPaymentTransactions',N'') 
,--p_type   payementtransactions  Types


/* 
 Hybris system default ReturnPayment Transactions  attributes will be here .
*/



/* ReturnPayment Addresses  Level */

(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'FirstName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_firstname' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_firstname
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'MiddleName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_middlename' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_middlename
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'LastName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_lastname' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_lastname
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'AddressLine1',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetname' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_streetname
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'AddressLine2',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetNumber' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_streetNumber
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'PostalCode',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_postalcode' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_postalcode
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'Locale',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_town' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_town
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'Region',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_region' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.regions ' , N'ref' ,N'ReturnPaymentAddress',N'') 
,--p_region
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_country' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.countries ' , N'ref' ,N'ReturnPaymentAddress',N'') 
,--p_country
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'Telephone',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_phone1' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_phone1
(N'RFOpetions.RFOperations.RFO_Accounts.Accountcontracts' ,N'BirthDay',N'datetime' ,N'When 1900 then Null ' ,N'Hybris.dbo.addresses' ,N'p_dateofbirth' ,N'datetime' ,N'ReturnNumber' ,N'When 1900 then Null ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_dateofbirth
(N'RFOpetions.RFOperations.RFO_Accounts.Accountcontracts' ,N'GenderID',N'bigint' ,N'When 1900 then Null ' ,N'Hybris.dbo.addresses' ,N'p_gender' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.enumvalues' , N'ref' ,N'ReturnPaymentAddress',N'') 
,--p_gender
(N' ' ,N' ',N' ' ,N' Hybris System Attributes ' ,N'Hybris.dbo.addresses' ,N'p_billingaddress' ,N'tinyint' ,N'ReturnNumber' ,N' defaulted to 1' , N'def' ,N'ReturnPaymentAddress',N'') 
,--p_billingaddress
(N' ' ,N' ',N' ' ,N' Hybris System Attributes ' ,N'Hybris.dbo.addresses' ,N'p_duplicate' ,N'tinyint' ,N'ReturnNumber' ,N' defaulted to 1' , N'def' ,N'ReturnPaymentAddress',N'') 
,--p_duplicate
(N' ' ,N' ',N' ' ,N' Hybris System Attributes ' ,N'Hybris.dbo.addresses' ,N'ownerpkstring' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.paymentinfos' , N'key' ,N'ReturnPaymentAddress',N'') 
,--ownerpkstring

/* 
 Hybris system default ReturnPayment Addresses  attributes will be here .
*/


 
/* OKTA Mapping .*/
(N'RFOperations.RFO_Accounts.AccountBase' ,N'AccountID',N'Bigint' ,N'' ,N'OKTA' ,N'p_AccountID' ,N'Bigint' ,N'AccountID' ,N'' , N'Key' ,N'OKTA',N'') 
,-- AccountID 
(N'RFOperations.Security.AccountSecurity' ,N'FirstName',N'Nvarchar(255)' ,N'' ,N'OKTA' ,N'p_firstName' ,N'Nvarchar(255)' ,N'AccountID' ,N'' , N'c2c' ,N'OKTA',N'') 
,-- FirstName 
(N'RFOperations.Security.AccountSecurity' ,N'LasttName',N'Nvarchar(255)' ,N'' ,N'OKTA' ,N'p_lasttName' ,N'Nvarchar(255)' ,N'AccountID' ,N'' , N'c2c' ,N'OKTA',N'') 
,-- LasttName 
(N'RFOperations.Security.AccountSecurity' ,N'Password',N'Nvarchar(255)' ,N'' ,N'OKTA' ,N'p_Password' ,N'Nvarchar(255)' ,N'AccountID' ,N'' , N'c2c' ,N'OKTA',N'') 
,-- Password 
(N'RFOperations.Security.AccountSecurity' ,N'UserName',N'Nvarchar(255)' ,N'' ,N'OKTA' ,N'p_UserName' ,N'Nvarchar(255)' ,N'AccountID' ,N'' , N'c2c' ,N'OKTA',N'') 
,-- UserName 
(N'RFOperations.RFO_Accounts.EmailAddresses' ,N'Email',N'Nvarchar(255)' ,N'' ,N'OKTA' ,N'p_Email' ,N'Nvarchar(255)' ,N'AccountID' ,N'' , N'c2c' ,N'OKTA',N'') 
-- Email 