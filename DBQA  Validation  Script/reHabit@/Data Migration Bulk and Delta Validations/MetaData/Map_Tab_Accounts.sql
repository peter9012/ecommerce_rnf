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
(N'RFOperations.RFO_Accounts.AccountContacts' ,N'Birthday',N'DATETIME' ,N'NULL THEN ''1900-01-01''' ,N'Hybris.dbo.Users' ,N'p_dateofbirth' ,N'DATETIME' ,N'AccouuntID' ,N'' , N'C2C' ,N'Accounts',N'') 
,--p_dateofbirth
(N'RFOperations.RFO_Accounts.AccountRF',N'HardTerminationDate',N'DATETIME',N'Load as NULL',N'Hybris.dbo.Users',N'p_hardterminateddate',N'DATETIME',N'AccouuntID',N'', N'C2C',N'Accounts',N'') 
,--p_hardterminationdate
(N'RFOperations.RFO_Accounts.AccountRF',N'SoftTerminationDate',N'DATETIME',N'Load as NULL',N'Hybris.dbo.Users',N'p_softterminateddate',N'DATETIME',N'AccouuntID',N'', N'C2C',N'Accounts',N'') 
,--p_softterminationdate
(N'RFOperations.RFO_Accounts.AccountContacts',N'TaxNumber',N'Nvarchar',N' ',N'Hybris.dbo.Users',N'p_countrysecuritycode',N'string',N'AccouuntID',N' ', N'C2C',N'Accounts',N'') 
,--p_taxnumber
(N'RFOperations.RFO_Accounts.AccountBase',N'AccountNumber',N'Nvarchar',N' ',N'Hybris.dbo.Users',N'p_accountnumber',N'string',N'AccouuntID',N' ', N'C2C',N'Accounts',N'') 
,--P_accountnumber
(N'RFOperation.RFO_Account.AccountContacts',N'GenderID',N'BIGINT',N'RFOperations.RFO_reference.Genders',N'Hybris.dbo.Users',N'p_gender',N'BIGINT',N'AccouuntID',N'Hybris.EnumerationValues', N'ref',N'Accounts',N'') 
,--p_gender
(N'RFOperations.RFO_Accounts.AccountRF',N'IsTaxExempt',N'BIT',N'1 True N 0 False',N'Hybris.dbo.Users',N'p_taxexempt',N'tinyint',N'AccouuntID',N'1 True N 0 False', N'C2C',N'Accounts',N'') 
,--IsTaxExempt
(N'RFOperations.RFO_Accounts.AccountRF',N'IsBusinessEntity',N'BIT',N'1 True N 0 False',N'Hybris.dbo.Users',N'p_businessentity',N'tinyint',N'AccouuntID',N'1 True N 0 False', N'C2C',N'Accounts',N'') 
,--p_businessentity
(N'RFOperations.RFO_Accounts.EmailAddresses',N'EmailAddress',N'NVARCHA',N'EmailTypeID=1',N'Hybris.dbo.Users',N'p_email',N'nvarchar',N'AccouuntID',N'', N'C2C',N'Accounts',N'') 
,--p_email
(N'RFOperation.RFO_Account.AccountBase',N'CountryID',N'BIGINT',N'RFOperations.RFO_reference.Countries',N'Hybris.dbo.Users',N'p_country',N'BIGINT',N'AccouuntID',N'Hybris.dbo.countries', N'ref',N'Accounts',N'') 
,--p_country
(N'RFOperations.RFO_Accounts.AccountRF',N'AccouuntID',N'NVARCHAR',N' Account SourceKey',N'Hybris.dbo.Users',N'p_customerid',N'string',N'AccouuntID',N'Users TargetKey', N'Key',N'Accounts',N'') 
,--p_customerid
(N'RFOperations.RFO_Accounts.AccountRF',N'SponsorID',N'BIGINT',N' ',N'Hybris.dbo.Users',N'p_newsponsorid',N'Bigint',N'AccouuntID',N'', N'upd',N'Accounts',N'') 
,--p_newsponsorid
(N'RFOperations.RFO_Accounts.AccountBase',N'AccountStatusID',N'BIGINT',N'RFO_References.AccountStatus',N'Hybris.dbo.Users',N'p_accountstatus',N'NVARCHAR',N'AccouuntID',N'Hybris..venumerationvalues', N'ref',N'Accounts',N'') 
,--p_accountstatus
(N'RFOperations.Hybris.AccountBasee',N'AccountTypes',N'BIT',N'',N'Hybris.dbo.users',N'p_type',N'BIGINT',N'AccouuntID',N'PK of internalcode in enums. ', N'Ref',N'Accounts',N'') 
,--p_type
(N'RFOperations.RFO_Accounts.AccountRF',N'CoAppFistName',N'NVARCHAR',N'NuLL THEN  '' '' Splitting p_spousefirstname with first space as delimiter. ',N'Hybris.dbo.Users',N'p_spousefirstname',N'STRING',N'AccouuntID',N' ', N'C2C',N'Accounts',N'') 
,--p_spouseFirstName
(N'RFOperations.RFO_Accounts.AccountRF',N'CoAppLastName',N'NVARCHAR',N'NuLL THEN  '' '' Splitting p_spouselastname  with first space as delimiter. ',N'Hybris.dbo.Users',N'p_spouselastname',N'STRING',N'AccouuntID',N' ', N'C2C',N'Accounts',N'') 
,--p_spouselastname
--(N'RFOperations.RFO_Accounts.Phones',N'PhoneNumberRaw',N'NVARCHAR',N'PhoneType=Main ',N'Hybris.dbo.Users',N'',N'BIGINT',N'AccouuntID',N'? ', N'C2C',N'Accounts',N'') 
--,--PoneNumberRaw
(N'RFOperations.RFO_Accounts.AccountRF',N'NextRenewalDate',N'DATETIME',N'NULL for RC and PC and DEFAULT NULL ',N'Hybris.dbo.Users',N'p_nextrenewaldate',N'DATETIME',N'AccouuntID',N'', N'c2c',N'Accounts',N'') 
,--p_nextrenewaldate
(N'RFOperations.RFO_Accounts.AccountRF',N'LastRenewalDate',N'DATETIME',N'NULL for RC and PC and DEFAULT NULL',N'Hybris.dbo.Users',N'p_lastrenewaldate',N'DATETIME',N'AccouuntID',N' ', N'c2c',N'Accounts',N'') 
,--p_lastrenewaldate
(N'RFOperations.RFO_Accounts.AccountBase',N'EnrollmentDate',N'DATETIME',N'',N'Hybris.dbo.Users',N'createdTS',N'DATETIME',N'AccouuntID',N'', N'c2c',N'Accounts',N'') 
,--p_enrolementdate
(N'RFOperations.RFO_Accounts.AccountRF',N'Name',N'NVARCHAR',N'',N'Hybris.dbo.Users',N'p_name',N'NVARCHAR',N'AccouuntID',N' ', N'C2C',N'Accounts',N'') 
,--Name
(N'RFOperations.RFO_Accounts.Emailaddresses',N'UserName',N'NVARCHAR',N'',N'Hybris.dbo.Users',N'p_uid',N'nvarchar',N' ',N'AccouuntID', N'C2C',N'Accounts',N'') 
,--p_uid
(N'RFOperations.Security.AccountSecurity',N'password',N'NVARCHAR',N'',N'Hybris.dbo.Users',N'passwd',N'nvarchar',N' ',N'AccouuntID', N'C2C',N'Accounts',N'') 
,--PassWord
(N'RFOperations.RFO_Accounts.PaymentProfiles',N'PaymentProfileID',N'bigint',N'PaymentProfile Where IsDefault= 1',N'Hybris.dbo.Users',N'p_defaultpaymentinfo',N'BIGINT',N'AccouuntID',N'PK of Default PaymentInfos ', N'upd',N'Accounts',N'') 
,--p_defaultpaymentinfo
(N'RFOperations.RFO_Accounts.Addresses',N'ShippingAddressId',N'bigint',N'ShippingAddress Where IsDefault= 1',N'Hybris.dbo.Users',N'p_defaultshipmentaddress',N'BIGINT',N'AccouuntID',N'PK ofAddresses Where  Default Shiiping=1 ', N'upd',N'Accounts',N'') 
,--DefaultShippingAddress
(N'RFOperations.RFO_Accounts.Addresses',N'BillingAddressID',N'bigint',N'BillingAddress Where IsDefault= 1',N'Hybris.dbo.Users',N'P_defaultpaymentAddress',N'BIGINT',N'AccouuntID',N'PK of Default BillingAddresses ', N'upd',N'Accounts',N'') 
,--P_defaultpaymentAddress
(N'RFOperations.Hybris.AccountBasee',N'CurrencyID',N'Bigint',N'',N'Hybris.dbo.users',N'p_sessioncurrency',N'BIGINT',N'AccouuntID',N'Hybris.dbo.Currencies', N'Ref',N'Accounts',N'') 
,--p_sessioncurrency
(N'RFOperations.Hybris.AccountBasee',N'LanguageID',N'Bigint',N'',N'Hybris.dbo.users',N'p_sessionlanguage',N'BIGINT',N'AccouuntID',N'Hybris.dbo.languages', N'ref',N'Accounts',N'') 
,--p_sessionlanguage
(N'RFOperations.RFO_Accounts.AccountRF',N'Active',N'BIT',N'Active is 1 then True else False',N'Hybris.dbo.Users',N'p_active',N'tinyint',N'AccouuntID ',N's', N'C2C',N'Accounts',N'') 
,--p_active
(N'RFOperations.Hybris.Autoship',N'IsPulseSubscrption',N'BIT',N'',N'Hybris.dbo.users',N'p_pulsesubscription',N'bolean',N'AccouuntID',N'', N'c2c',N'Accounts',N'') 
,--p_pulsesubscription
(N'RFOperations.Hybris.Autoship',N'IsPCActive',N'BIT',N'',N'Hybris.dbo.users',N'p_pcperksactive',N'bolean',N'AccouuntID',N'', N'c2c',N'Accounts',N'') 
,--p_pcperksactive
(N'RFOperations.RFO_Accounts.profilePictures',N'profilepicture',N'BIT',N'',N'Hybris.dbo.users',N'p_profilepicture',N'bigint',N'AccouuntID',N'PK of Hybris.dbo.userprofiles ', N'c2c',N'PWS',N'') 
,--p_profilepicture
(N'RFOperations.RFO_Accounts.profilePictures',N'TokenID',N'nvarchar(255)',N'',N'Hybris.dbo.users',N'p_token',N'nvarchar(255)',N'AccouuntID',N' covered with Tokenized projects.', N'n/a',N'Accounts',N'') 
,--p_token
(N' ',N'',N'',N'Hybris System Attribute',N'Hybris.dbo.users',N'Typepkstring',N'bigint',N'AccouuntID',N'PK from Hybris.dbo.composedtypes for Customers', N'def',N'Accounts',N'SELECT  u.p_customerid HybrisKey ,
        u.TypePkString ,
        t.InternalCode [Customer]
FROM    Hybris.dbo.users u
		JOIN #Hybris h ON u.p_customerid=h.HybrisKey
        JOIN Hybris.dbo.composedtypes t ON t.pk = u.TypePkString
WHERE   t.InternalCode <>''Customer''') 
,--Typepkstring.

/* Pending from Thiru 
(N'RFOperations.RFO_Accounts.AccountRF',N'Active',N'BIT',N'Active is 1 then True else False',N'Hybris.dbo.Users',N'p_logindisabled',N'Bolean',N'AccouuntID ',N's', N'C2C',N'Accounts',N'') 
,--p_logindisabled
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--OwnerPkString
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--createdTS	datetime  >>> WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--modifiedTS	datetime  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_description	nvarchar(255) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_backofficelogindisabled	tinyint  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_passwordencoding	nvarchar(255)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_passwordanswer	nvarchar(max) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_passwordquestion	nvarchar(max) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_logindisabled	tinyint  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)? IS THERE ANY ATTRIBUTES BEpEN MAPPED FROM RFO??
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_lastlogin	datetime >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_hmclogindisabled	tinyint  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_europe1pricefactory_udg	bigint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_europe1pricefactory_upg	bigint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_europe1pricefactory_utg	bigint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_authorizedtounlockpages	tinyint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_previewcatalogversions	nvarchar(max)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_title	bigint  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_token	nvarchar(max)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_originaluid	nvarchar(255)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_sapconsumerid	nvarchar(255)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_sapcontactid	nvarchar(255) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_sapisreplicated	tinyint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_sapreplicationinfo	nvarchar(255) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_pdfversion	nvarchar(255) >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_countrysecuritycode	nvarchar(255)  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_sponsorstartdate	datetime  >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_pcperksactive	tinyint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM) for different Accounttype than PC?
(N' ',N'',N'BIT',N'',N'Hybris.dbo.users',N'OwnerPkString',N'bigint',N'AccouuntID',N'', N'def',N'Accounts',N'') 
,--p_defaultb2bunit	bigint >>>WHAT WILL BE THE VALUE FOR BULK LOAD(DM)?
*/
   


			
/* Accounts paymentinfos Table in Hybris*/


--INSERT INTO #Map_Tab
--        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
--VALUES 
--(N'RFOperation.RFO_Account.PaymentProfiles',N'PaymentProfileID',N'bigint',N' ',N'Hybris.dbo.Paymentinfos',N'PK',N'Bigint',N'AccouuntID',N' ', N'C2C',N'PaymentProfiles',N'') 
--,--PaymentProfileID 
(N'RFOperation.RFO_Account.PaymentProfiles',N'ProfileName',N'nvarchar',N' ',N'Hybris.dbo.Paymentinfos',N'p_ccowner',N'nvarchar',N'AccouuntID',N' ', N'C2C',N'AccountPaymentProfiles',N'') 
,--ProfileName
 (N'RFOperation.RFO_Account.PaymentProfiles',N'DisplayNumber',N'nvarchar',N' ',N'Hybris.dbo.Paymentinfos',N'p_number',N'string',N'AccouuntID',N' ', N'C2C',N'AccountPaymentProfiles',N'') 
,--DisplayNumber
 (N'RFOperation.RFO_Account.PaymentProfiles',N'VendorID',N'bigint',N' ',N'RFOperations.RFO_Reference.CreditcartTypes',N'p_type',N'string',N'AccouuntID',N'Hybris.dbo.enumerationvalues ', N'ref',N'AccountPaymentProfiles',N'') 
,--VendorID
 (N'RFOperation.RFO_Account.PaymentProfiles',N'ExpMonth',N'',N' ',N'Hybris.dbo.Paymentinfos',N'p_validtomonth',N'',N'AccouuntID',N' ', N'C2C',N'AccountPaymentProfiles',N'') 
,--ExpMonth
 (N'RFOperation.RFO_Account.PaymentProfiles',N'ExpYear',N'',N' ',N'Hybris.dbo.Paymentinfos',N'p_validtoyear',N'',N'AccouuntID',N' ', N'C2C',N'AccountPaymentProfiles',N'') 
,--ExpYear
 (N'RFOperation.RFO_Account.CreditCardProfiles',N'BillingAddressID',N'bigint',N' ',N'Hybris.dbo.Paymentinfos',N'p_billingaddress',N'bigint',N'AccouuntID',N' PK from Addresses', N'ref',N'AccountPaymentProfiles',N'') 
,--p_billingaddress
 (N'RFOperation.RFO_Account.CreditCardProfiles',N'LastFourNumber',N'nvarchar',N'LastFour Digit of Number ',N'Hybris.dbo.Paymentinfos',N'p_lastfourdigit',N'nvarchar',N'AccouuntID',N' PK from Addresses', N'c2c',N'AccountPaymentProfiles',N'') 
,--p_lastfourdigit
 (N'RFOperation.RFO_Account.CreditCardProfiles',N'cctoken',N'nvarchar',N' ',N'Hybris.dbo.Paymentinfos',N'p_cctoken',N'nvarchar',N'AccouuntID',N' PK from Addresses', N'c2c',N'AccountPaymentProfiles',N'') 
,--p_cctoken
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'p_original',N'bigint',N'AccouuntID',N'Null for AccountLevel PaymentInfos', N'def',N'AccountPaymentProfiles',N'') 
,--p_original
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'p_duplicate',N'bigint',N'AccouuntID',N'Null for AccountLevel PaymentInfos', N'def',N'AccountPaymentProfiles',N'') 
,--p_duplicate
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'typepkstring',N'bigint',N'AccouuntID',N'pk from composedtypes Where iternalCode=CreditCardPaymentInfo', N'def',N'AccountPaymentProfiles',N'') 
,--typepkstring
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'OwnerPkString',N'bigint',N'AccouuntID',N'Null for Accountlevel paymentinfos', N'def',N'AccountPaymentProfiles',N'') 
,--OwnerPkString
/* Pending Concern to following Attributes
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'OwnerPkString',N'bigint',N'AccouuntID',N'Null for Accountlevel paymentinfos', N'def',N'AccountPaymentProfiles',N'') 
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


(N'RFOperations.RFO_Accounts.Addresses',N'CountryID',N'nvarchar',N'PK from Hybris.dbo.regions',N'Hybris.dbo.addresses',N'p_counrty',N'BIGINT',N'AccouuntID',N'PK of Default PaymentInfos ', N'ref',N'AccountAddresses',N'') 
,--p_counrty
(N'RFOperations.RFO_Accounts.Addresses',N'region',N'nvarchar',N'PK from Hybris.dbo.regions',N'Hybris.dbo.addresses',N'p_region',N'BIGINT',N'AccouuntID',N'PK of Hybris.dbo.regions Where p_isocodeshort=State ', N'ref',N'AccountAddresses',N'') 
,--p_region
(N'RFOperations.RFO_Accounts.Phones',N'MainNumber',N'NVARCHAR',N'RFOperations.Phones Where PhoneType=Main',N'Hybris.dbo.Addresses',N'p_phone1',N'nvarchar',N'AccouuntID',N'', N'C2C',N'AccountAddresses',N'') 
,--p_phone1
(N'RFOperations.RFO_Accounts.Phones',N'HomeNumber',N'NVARCHAR',N'RFOperations.Phones Where PhoneType=Home',N'Hybris.dbo.Addresses',N'p_phone2',N'nvarchar',N'AccouuntID',N'', N'C2C',N'AccountAddresses',N'') 
,--p_phone2
(N'RFOperations.RFO_Accounts.Phones',N'CellNumber',N'NVARCHAR',N'RFOperations.Phones Where PhoneType=Mobile',N'Hybris.dbo.Addresses',N'p_cellphone',N'nvarchar',N'AccouuntID',N'', N'C2C',N'AccountAddresses',N'') 
,--p_cellphone
(N'RFOperations.RFO_Accounts.AccountContacts',N'GenderID',N'NVARCHAR',N'RFOperations.RFO_reference.Genders',N'Hybris.dbo.Addresses',N'p_gender',N'BIGINT',N'AccouuntID',N'Hybris.dbo.enumurationValues ', N'ref',N'AccountAddresses',N'') 
,--p_gender
(N'RFOperations.RFO_Accounts.Addresses',N'IsBillingAddress',N'BIT',N'RFOperations.RFO_References.AddressTypes',N'Hybris.dbo.addresses',N'p_billingaddress',N'BIGINT',N'AccouuntID',N'True=1 False=0', N'c2c',N'AccountAddresses',N'') 
,--p_billingaddress
(N'RFOperations.RFO_Accounts.Addresses',N'IsContactAddress',N'BIT',N'RFOperations.RFO_References.AddressTypes',N'Hybris.dbo.addresses',N'p_contactaddress',N'BIGINT',N'AccouuntID',N'True=1 False=0', N'c2c',N'AccountAddresses',N'') 
,--p_contactaddress
(N'RFOperations.RFO_Accounts.Addresses',N'IsShippingAddress',N'BIT',N'RFOperations.RFO_References.AddressTypes',N'Hybris.dbo.addresses',N'p_shippingaddress',N'BIGINT',N'AccouuntID',N'True=1 False=0', N'c2c',N'AccountAddresses',N'') 
,-- p_shippingaddress
(N'RFOperations.RFO_Accounts.Addresses',N'Locale',N'nvarchar',N'T ',N'Hybris.dbo.addresses',N'p_town',N'nvarchar',N'AccouuntID',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_town
--(N'RFOperations.RFO_Accounts.Addresses',N'AddressID',N'nvarchar',N'T ',N'Hybris.dbo.Users',N'p_rfaddressid',N'BIGINT',N'AccouuntID',N' ', N'C2C',N'AccountAddresses',N'') 
--,--AddressID
(N'RFOperation.RFO_Account.Addresses',N'AddressLine1',N'nvarchar',N'StreenName only Excluding Number ',N'Hybris.dbo.addresses',N'p_streetname',N'nvarchar(255)',N'AccouuntID',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_streetname
(N'RFOperation.RFO_Account.Addresses',N'AddressLine2',N'nvarchar',N'Numbers only Excluding Name ',N'Hybris.dbo.addresses',N'p_streetnumber',N'nvarchar(255)',N'AccouuntID',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_streetnumber 
(N'RFOperation.RFO_Account.Addresses',N'FirstName',N'nvarchar',N' ',N'Hybris.dbo.addresses',N'p_firstname',N'nvarchar(255)',N'AccouuntID',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_firstname
(N'RFOperation.RFO_Account.Addresses',N'LastName',N'nvarchar',N' ',N'Hybris.dbo.addresses',N'p_lastname',N'nvarchar(255)',N'AccouuntID',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_lastname
(N'RFOperation.RFO_Account.Addresses',N'MiddleName',N'nvarchar',N' ',N'Hybris.dbo.addresses',N'p_MiddleName',N'nvarchar(255)',N'AccouuntID',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_MiddleName
(N'RFOperation.RFO_Account.Addresses',N'PostalCode',N'nvarchar',N' ',N'Hybris.dbo.addresses',N'p_postalcode',N'nvarchar(255)',N'AccouuntID',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_postalcode
(N'RFOperation.RFO_Account.Addresses',N'BirthDay',N'nvarchar',N' ',N'Hybris.dbo.addresses',N'p_dateofbirth',N'nvarchar(255)',N'AccouuntID',N' ', N'C2C',N'AccountAddresses',N'') 
,--p_dateofbirth
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'typepkstring',N'bigint',N'AccouuntID',N'pk from composedtypes Where iternalCode=Address', N'default',N'AccountAddresses',N'') 
,--typepkstring
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'p_duplicate',N'tityint',N'AccouuntID',N'0 for AccountsLevel Addresses', N'default',N'AccountAddresses',N'') 
,--p_duplicate
 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'p_original',N'bigint',N'AccouuntID',N'Null for AccountLevel addresses', N'default',N'AccountAddresses',N'') 
--p_original

/* Pending Concerns From Thiru

 (N'',N'',N'',N'Hybris System Attributes',N'Hybris.dbo.Paymentinfos',N'createdTS',N'datetime',N'AccouuntID',N'Getdate for AccountLevel addresses', N'def',N'AccountAddresses',N'') 
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



