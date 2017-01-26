DECLARE @Bool INT
SET @Bool = 0
IF  @Bool <100
    BEGIN
        GOTO UPDATED 
		
    END
  ELSE
    BEGIN
        GOTO UNUPDATED 
    END 
	


UPDATED:
SELECT  'Updated'
SELECT @Bool AS Bool 
GOTO EXIT_Point

UNUPDATED:
SELECT  'Not Updated'
SELECT @Bool AS Bool 
GOTO EXIT_Point

EXIT_Point:
SELECT  'Execution Done '





IF OBJECT_ID('TEMPDB.dbo.#RFOAccounts') IS NOT NULL
    DROP TABLE #RFOAccounts;
IF OBJECT_ID('TEMPDB.dbo.#HybrisAccounts') IS NOT NULL
    DROP TABLE #HybrisAccounts;
IF OBJECT_ID('TEMPDB.dbo.#DPaymentInfo') IS NOT NULL
    DROP TABLE #DPaymentInfo;
IF OBJECT_ID('TEMPDB.dbo.#DShipping') IS NOT NULL
    DROP TABLE #DShipping;
	IF OBJECT_ID('TEMPDB.dbo.#Sponsered') IS NOT NULL
    DROP TABLE #Sponsered;


    SELECT  a.AccountID ,
            a.PaymentProfileID ,
            ad.AddressID,
			TokenID
  INTO    #DPaymentInfo
    FROM    RFOperations.RFO_Accounts.PaymentProfiles a
            --JOIN RodanFieldsLive.dbo.AccountPaymentMethods apm ON a.PaymentProfileID = apm.AccountPaymentMethodID
            JOIN RFoperations.RFO_accounts.CreditCardProfiles ccp ON ccp.PaymentProfileID = a.PaymentProfileID
            JOIN RFOperations.RFO_Accounts.Addresses Ad ON ccp.BillingAddressID = ad.AddressID
    WHERE   a.IsDefault = 1
            AND ad.IsDefault = 1 
			--GROUP BY a.AccountID ,
   --         a.PaymentProfileID ,
   --         ad.AddressID
			--HAVING count(a.AccountID)>1

         
         SELECT AC.AccountId ,
                MAX(A.AddressID) AS AddressID
         INTO   #DShipping
         FROM   RFOperations.RFO_Accounts.Addresses A
                JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.AddressID = A.AddressID
                JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountContactId = ACA.AccountContactId
         WHERE  AddressTypeID = 2
                AND IsDefault = 1
         GROUP BY ac.AccountID  
		    
         SELECT ab.AccountID ,
                BridgeTable.SponsorId [HybrisPK]
         INTO   #Sponsered
         FROM   RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.BridgeTable BridgeTable ON BridgeTable.AccountID = ab.AccountID          



SELECT  IIF(CAST(ac.Birthday AS DATE) = '1900-01-01', NULL, ac.Birthday) AS Birthday ,--	p_dateofbirth
        rf.HardTerminationDate ,--	p_hardterminateddate
        rf.SoftTerminationDate ,--	p_softterminateddate
        ac.SecuredTaxNumber AS TaxNumber ,--	p_accountnumber
        g.Name AS GenderID ,--	p_gender
        rf.IsTaxExempt ,--	p_taxexempt
        rf.IsBusinessEntity ,--	p_businessentity
        ea.EmailAddress ,--	p_email
        co.Alpha2Code AS CountryID ,--	p_country
        ab.AccountNumber ,--	p_customerid
        sp.[HybrisPK] AS SponsorID ,--	p_newsponsorid
        s.Name AS AccountStatusID ,--	p_status
        t.Name AS AccountTypes ,--	p_type 
        CASE WHEN CHARINDEX(' ', LTRIM(RTRIM(rf.CoApplicant))) > 1
             THEN SUBSTRING(LTRIM(RTRIM(rf.CoApplicant)), 1,
                            CHARINDEX(' ', LTRIM(RTRIM(rf.CoApplicant))))
             ELSE LTRIM(RTRIM(rf.CoApplicant))
        END CoAppFistName ,--	p_spousefirstname
        CASE WHEN CHARINDEX(' ', LTRIM(RTRIM(rf.CoApplicant))) > 1
             THEN SUBSTRING(LTRIM(RTRIM(rf.CoApplicant)),
                            CHARINDEX(' ', LTRIM(RTRIM(rf.CoApplicant))),
                            ( LEN(RTRIM(LTRIM(rf.CoApplicant)))
                              - CHARINDEX(' ', LTRIM(RTRIM(rf.CoApplicant))) ))
             ELSE NULL
        END CoAppLastName ,--	p_spouselastname
        rf.NextRenewalDate ,--	p_nextrenewaldate
        rf.LastRenewalDate ,--	p_lastrenewaldate
        rf.EnrollmentDate ,--	p_enrolementdate
        CONCAT(ac.FirstName, '', ac.LastName) Name ,--	p_name
        accs.UserName ,--	p_uid
        accs.[password] ,--	PassWord
        p.PaymentProfileID ,--	p_defaultpaymentinfo
        -ds.AddressID AS ShippingAddressId ,--	p_defaultshipmentaddress
        p.AddressID AS BillingAddressID ,--	P_defaultpaymentAddress
        cu.Name AS CurrencyID ,--	p_sessioncurrency       
        nl.ISOCode AS LanguageID ,--	p_sessionlanguage
        rf.Active ,--	p_active
        Pulse.IsPulseSubscrption ,--	p_pulsesubscription
        PC.IsPCActive ,--	p_pcperksactive
        p.TokenID ,--	p_token
        'customer' AS Typepkstring
INTO    #RFOAccounts
    --SELECT  ab.AccountNumber , COUNT(AccountNumber)
	--SELECT top 10 *
FROM    RFOperations.RFO_Accounts.AccountBase ab
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID --AND ab.AccountNumber='001762'
        JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
        JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
        JOIN RFOperations.RFO_Reference.AccountContactType t ON t.AccountContactTypeID = ab.AccountTypeID
        JOIN RFOperations.RFO_Reference.NativeLanguage nl ON nl.LanguageID = ab.LanguageId
        JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ab.CountryID
        JOIN RFOperations.RFO_Reference.Currency cu ON cu.CurrencyID = ab.CurrencyID
        JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
        LEFT JOIN RFOperations.[Security].AccountSecurity accs ON accs.AccountID = ab.AccountID
        --LEFT JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId AND ac.AccountContactTypeId=1
        LEFT JOIN RFOperations.RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId
        LEFT JOIN RFOperations.RFO_Accounts.EmailAddresses ea ON ea.EmailAddressID = ae.EmailAddressId
        LEFT JOIN ( SELECT DISTINCT
                            A.AccountID ,
                            1 AS IsPulseSubscrption
                    FROM    RFOperations.[Hybris].[Autoship] A
                            INNER JOIN RFOperations.Hybris.AutoshipItem (NOLOCK) ai ON ai.AutoshipId = A.AutoshipID
                    WHERE   Active = 1
                            AND -- AnyStatus??
                            AutoshipTypeID = 3
                  ) Pulse ON Pulse.AccountID = ab.AccountID
        LEFT JOIN ( SELECT DISTINCT
                            A.AccountID ,
                            1 AS IsPCActive
                    FROM    RFOperations.[Hybris].[Autoship] A
                            INNER JOIN RFOperations.Hybris.AutoshipItem (NOLOCK) ai ON ai.AutoshipId = A.AutoshipID
                    WHERE   Active = 1
                            AND -- AnyStatus??
                            AutoshipTypeID = 1
                  ) PC ON PC.AccountID = ab.AccountID
        LEFT JOIN #Sponsered sp ON sp.AccountID = rf.SponsorId
        LEFT JOIN #DPaymentInfo p ON p.accountID = ab.AccountID
        LEFT JOIN #DShipping ds ON ds.AccountID = ab.AccountID
WHERE   EXISTS ( SELECT 1
                 FROM   RFOperations.Hybris.orders ro
                 WHERE  ro.AccountID = ab.AccountID
                        AND ro.CompletionDate >= DATEADD(MONTH, -18,
                                                         '2017-02-28') )
        AND ab.AccountStatusID NOT IN ( 3, 4 ) --Begin and Pending 
        AND ea.EmailAddressTypeID = 1
    --GROUP BY ab.AccountNumber
    --HAVING  COUNT(AccountNumber) > 1









SELECT 
p_dateofbirth	,--	Birthday
p_hardterminateddate	,--	HardTerminationDate
p_softterminateddate	,--	SoftTerminationDate
p_accountnumber	,--	TaxNumber
g.Code AS  p_gender	,--	GenderID
p_taxexempt	,--	IsTaxExempt
p_businessentity	,--	IsBusinessEntity
p_email	,--	EmailAddress
co.p_isocode AS p_country	,--	CountryID
p_customerid	,--	AccountNumber
p_newsponsorid	,--	SponsorID
--p_status	,--	AccountStatusID
tp.Code AS p_type	,--	AccountTypes
p_spousefirstname	,--	CoAppFistName
p_spouselastname	,--	CoAppLastName
p_renewaldate AS p_nextrenewaldate	,--	NextRenewalDate
--p_lastrenewaldate	,--	LastRenewalDate
--p_enrolementdate	,--	EnrollmentDate
p_name	,--	Name
p_uid	,--	UserName
passwd AS  PassWord	,--	password
p_defaultpaymentinfo	,--	PaymentProfileID
p_defaultshipmentaddress	,--	ShippingAddressId
P_defaultpaymentAddress	,--	BillingAddressID
cu.p_isocode AS p_sessioncurrency	,--	CurrencyID
la.p_isocode AS  p_sessionlanguage	,--	LanguageID
u.p_active	,--	Active
p_pulsesubscription	,--	IsPulseSubscrption
p_pcperksactive	,--	IsPCActive
p_token	,--	TokenID
t.InternalCode AS Typepkstring	--	
INTO #HybrisAccounts
FROM Hybris.dbo.Users u
JOIN Hybris.dbo.countries co ON co.pk=u.p_country
JOIN Hybris.dbo.composedtypes t ON t.pk=u.TypePkString
LEFT JOIN Hybris.dbo.enumerationvalues g ON g.pk=u.p_gender
LEFT JOIN Hybris.dbo.enumerationvalues tp ON tp.pk=u.p_type
LEFT JOIN Hybris.dbo.currencies cu ON cu.pk=u.p_sessioncurrency
LEFT JOIN Hybris.dbo.languages la ON la.pk=u.p_sessionlanguage