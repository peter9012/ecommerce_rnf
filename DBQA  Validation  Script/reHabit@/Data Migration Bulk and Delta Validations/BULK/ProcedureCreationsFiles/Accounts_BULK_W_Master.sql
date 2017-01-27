

USE DM_QA
GO 

CREATE PROCEDURE dbqa.uspAccountsHeader_BULK ( @LoadDate DATE )
AS
    BEGIN 

        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'Data Migration' ,
            @owner NVARCHAR(50)= 'Accounts' ,
            @Key NVARCHAR(25)= 'AccountNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts',
			@OrderDate DATE =DATEADD(MONTH,-18,@LoadDate),
			@HardTermDate DATE=DATEADD(MONTH,-6,@LoadDate)




        IF OBJECT_ID('TEMPDB.dbo. #RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#HybrisAccounts') IS NOT NULL
            DROP TABLE #Hybris;
        IF OBJECT_ID('TEMPDB.dbo.#DPaymentInfo') IS NOT NULL
            DROP TABLE #DPaymentInfo;
        IF OBJECT_ID('TEMPDB.dbo.#DShipping') IS NOT NULL
            DROP TABLE #DShipping;
        IF OBJECT_ID('TEMPDB.dbo.#Sponsered') IS NOT NULL
            DROP TABLE #Sponsered;
	
	

        SET @message = ' STEP:1. Source Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message



	
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
        ab.AccountNumber AS [RFOKey],--	p_customerid
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
INTO     #RFO
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
WHERE  ea.EmailAddressTypeID = 1
        AND ab.AccountNumber <> 'AnonymousRetailAccount'
        AND ( ab.AccountStatusID IN ( 1, 2, 8, 9 ) -- Active,Inactive,SoftVoluntary,SoftInvoluntary
              OR ( ab.AccountStatusID = 10
                   AND EXISTS ( SELECT  1
                                FROM    RFOperations.Hybris.orders ro
                                WHERE   ro.AccountID = ab.AccountID
                                        AND CAST(ro.CompletionDate AS DATE) >= @HardTermDate ) --HardTerm with 6 months order
                 )
              OR ( ab.AccountStatusID IN ( 3, 4, 5, 6, 7 )
                   AND EXISTS ( SELECT  1
                                FROM    RFOperations.Hybris.orders ro
                                WHERE   ro.AccountID = ab.AccountID
                                        AND CAST(ro.CompletionDate AS DATE) >=@OrderDate )--Other Status with 18 months orders
                 )
            )
    --GROUP BY ab.AccountNumber
    --HAVING  COUNT(AccountNumber) > 1


	SET @message=' STEP: 2.Target Table Started to Load'
	EXECUTE dbqa.uspPrintMessage @message



	


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
p_customerid AS [HybrisKey]	,--	AccountNumber
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
INTO #Hybris
FROM Hybris.dbo.Users u
JOIN Hybris.dbo.countries co ON co.pk=u.p_country
JOIN Hybris.dbo.composedtypes t ON t.pk=u.TypePkString
LEFT JOIN Hybris.dbo.enumerationvalues g ON g.pk=u.p_gender
LEFT JOIN Hybris.dbo.enumerationvalues tp ON tp.pk=u.p_type
LEFT JOIN Hybris.dbo.currencies cu ON cu.pk=u.p_sessioncurrency
LEFT JOIN Hybris.dbo.languages la ON la.pk=u.p_sessionlanguage

	





	

      

/* Source to Target Total Counts */


--++++++++++++++++++++++++++++++++++++
-- USA_CAN Total Count Validation
--++++++++++++++++++++++++++++++++++++

        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing  



        CREATE CLUSTERED INDEX cls_RFO ON  #RFO([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #Hybris([HybrisKey])


        SET @ValidationType = 'Count'
        SET @message = ' STEP: 3 Count Validations Started. '
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  @SourceCount = COUNT(DISTINCT [RFOKey])
        FROM    #RFO
        SELECT  @TargetCount = COUNT([HybrisKey])
        FROM    #Hybris

-- INSERTING LOG FOR TOTALCOUNT VALIDATION.

        INSERT  INTO dbqa.SourceTargetLog
                ( FlowTypes ,
                  ValidationTypes ,
                  [Owner] ,
                  SourceCount ,
                  TargetCount ,
                  Comments ,
                  ExecutionStatus 
                )
        VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                  @ValidationType , -- ValidationTypes - nvarchar(50)
                  CONCAT('ALL_', @owner) , -- Owner - nvarchar(50)
                  @SourceCount , -- SourceCount - int
                  @TargetCount , -- TargetCounts - int
                  CASE WHEN @SourceCount > @TargetCount
                       THEN CONCAT('RFO Count More than Target BY ',
                                   CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
                       WHEN @SourceCount < @TargetCount
                       THEN CONCAT('Target Count More than Source Count By ',
                                   CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
                       ELSE 'Source to Target Counts are equal'
                  END ,
                  CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
                       ELSE 'FAILED'
                  END
                )
		




--+++++++++++++++++++++++++++++++++++++++++
-- USA Accounts Total Counts Validation
--+++++++++++++++++++++++++++++++++++++++++


        SELECT  @SourceCount = COUNT([RFOKey])
        FROM    #RFO
        WHERE   CountryID = 'US'
  


        SELECT  @TargetCount = COUNT([HybrisKey])
        FROM    #Hybris
        WHERE   p_country = 'US'


-- INSERTING LOG FOR US ACCOUNT TOTAL COUNT VALIDATION
        INSERT  INTO dbqa.SourceTargetLog
                ( FlowTypes ,
                  ValidationTypes ,
                  [Owner] ,
                  SourceCount ,
                  TargetCount ,
                  Comments ,
                  ExecutionStatus 
                )
        VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                  @ValidationType , -- ValidationTypes - nvarchar(50)
                  CONCAT('US_', @owner) , -- Owner - nvarchar(50)
                  @SourceCount , -- SourceCount - int
                  @TargetCount , -- TargetCounts - int
                  CASE WHEN @SourceCount > @TargetCount
                       THEN CONCAT('RFO Count More than Target BY ',
                                   CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
                       WHEN @SourceCount < @TargetCount
                       THEN CONCAT('Target Count More than Source Count By ',
                                   CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
                       ELSE 'Source to Target Counts are equal'
                  END ,
                  CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
                       ELSE 'FAILED'
                  END
                )
		
        

-- Print Message Saying Counts Completed for USAccounts

--++++++++++++++++++--++++++++++++++++++
-- CAN Accounts Total Count Validation
--++++++++++++++++++--++++++++++++++++++


        SELECT  @SourceCount = COUNT([RFOKey])
        FROM    #RFO
        WHERE   CountryID = 'CA'
  


        SELECT  @TargetCount = COUNT([HybrisKey])
        FROM    #Hybris
        WHERE   p_country = 'CA'

-- Hybris CA Counts Here .


        INSERT  INTO dbqa.SourceTargetLog
                ( FlowTypes ,
                  ValidationTypes ,
                  [Owner] ,
                  SourceCount ,
                  TargetCount ,
                  Comments ,
                  ExecutionStatus
                )
        VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                  @ValidationType , -- ValidationTypes - nvarchar(50)
                  CONCAT('CA_', @owner) , -- Owner - nvarchar(50)
                  @SourceCount , -- SourceCount - int
                  @TargetCount , -- TargetCounts - int
                  CASE WHEN @SourceCount > @TargetCount
                       THEN CONCAT('RFO Count More than Target BY ',
                                   CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
                       WHEN @SourceCount < @TargetCount
                       THEN CONCAT('Target Count More than Source Count By ',
                                   CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
                       ELSE 'Source to Target Counts are equal'
                  END ,
                  CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
                       ELSE 'FAILED'
                  END
                )



--++++++++++++++++++--++++++++++++++++++
-- AUS Accounts  Total Count Validation 
--++++++++++++++++++--++++++++++++++++++

        SELECT  @SourceCount = COUNT([RFOKey])
        FROM    #RFO
        WHERE   CountryID = 'AU'   


        SELECT  @TargetCount = COUNT([HybrisKey])
        FROM    #Hybris
        WHERE   p_country = 'AU'

        INSERT  INTO dbqa.SourceTargetLog
                ( FlowTypes ,
                  ValidationTypes ,
                  [Owner] ,
                  SourceCount ,
                  TargetCount ,
                  Comments ,
                  ExecutionStatus
                )
        VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                  @ValidationType , -- ValidationTypes - nvarchar(50)
                  CONCAT('AU_', @owner) , -- Owner - nvarchar(50)
                  @SourceCount , -- SourceCount - int
                  @TargetCount , -- TargetCounts - int
                  CASE WHEN @SourceCount > @TargetCount
                       THEN CONCAT('RFO Count More than Target BY ',
                                   CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
                       WHEN @SourceCount < @TargetCount
                       THEN CONCAT('Target Count More than Source Count By ',
                                   CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
                       ELSE 'Source to Target Counts are equal'
                  END ,
                  CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
                       ELSE 'FAILED'
                  END
                )


		

        SET @message = ' STEP: 4. Duplicate validation started.'
        EXECUTE dbqa.uspPrintMessage @message




--++++++++++++++++++--++++++++++++++++++
--USA RFO Accounts Duplicate Vaidation.
--++++++++++++++++++--++++++++++++++++++
        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Duplicate'




        SELECT  @SourceCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([RFOKey]) Ct
                  FROM      #RFO
                  WHERE     countryID = 'US'
                  GROUP BY  [RFOKey]
                  HAVING    COUNT([RFOKey]) > 1
                ) t



        IF ISNULL(@SourceCount, 0) > 0
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('US_RFO_', @owner) , -- Owner - nvarchar(50)
                      @SourceCount , -- SourceCount - int         
                      'RFO has Duplicate AccountNumbers' ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('US_RFO_', @owner) , -- Owner - nvarchar(50)
                      'RFO has NO Duplicate AccountNumbers' ,
                      'PASSED'
                    )

		

--++++++++++++++++++--++++++++++++++++++
-- CAN RFO ACCOUNTS DUPLICATE VALIDATION
--++++++++++++++++++--++++++++++++++++++

        SET @SourceCount = 0
        SELECT  @SourceCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([RFOKey]) Ct
                  FROM      #RFO
                  WHERE     countryID = 'CA'
                  GROUP BY  [RFOKey]
                  HAVING    COUNT([RFOKey]) > 1
                ) t



        IF ISNULL(@SourceCount, 0) > 0
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('CA_RFO_', @owner) , -- Owner - nvarchar(50)
                      @SourceCount , -- SourceCount - int         
                      'RFO has Duplicate AccountNumbers' ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('CA_RFO_', @owner) , -- Owner - nvarchar(50)
                      'RFO has NO Duplicate AccountNumbers' ,
                      'PASSED'
                    )




--++++++++++++++++++--+++++++++++++++++++++
--USA HYBRIS ACCOUNTS DUPLICATE VAIDATION.
--++++++++++++++++++--+++++++++++++++++++++

   

        SELECT  @TargetCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([HybrisKey]) Ct
                  FROM      #Hybris
                  WHERE     p_country = 'US'
                  GROUP BY  [HybrisKey]
                  HAVING    COUNT([HybrisKey]) > 1
                ) t



        IF ISNULL(@TargetCount, 0) > 0
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('US_Hybris_', @owner) , -- Owner - nvarchar(50)
                      @TargetCount , -- SourceCount - int         
                      'HYBRIS has Duplicate AccountNumbers' ,
                      'FAILED'
                    )

	




        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('US_Hybris_', @owner) , -- Owner - nvarchar(50)
                      'HYBRIS has NO Duplicate AccountNumbers' ,
                      'PASSED'
                    )

		


--++++++++++++++++++--++++++++++++++++++
-- CAN HYBRIS DUPLICATE VALIDATION
--++++++++++++++++++--++++++++++++++++++

        SET @TargetCount = 0

        SELECT  @TargetCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([HybrisKey]) Ct
                  FROM      #Hybris
                  WHERE     p_country = 'CA'
                  GROUP BY  [HybrisKey]
                  HAVING    COUNT([HybrisKey]) > 1
                ) t


        IF ISNULL(@TargetCount, 0) > 0
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('CA_Hybris_', @owner) , -- Owner - nvarchar(50)
                      @TargetCount , -- SourceCount - int         
                      'HYBIRS has Duplicate AccountNumbers' ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('CA_Hybris_', @owner) , -- Owner - nvarchar(50)
                      'Hybris has NO Duplicate AccountNumbers' ,
                      'PASSED'
                    )



--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++


        SET @message = ' STEP: 5. Missing Validaiton Started '
        EXECUTE dbqa.uspPrintMessage @message


--US ACCOUNTS SCTOTG  MISSING VALIDATION 
        SELECT  a.RFOKey ,
                b.[HybrisKey] ,
                COALESCE(a.countryID, b.p_country) AS Country ,
                CASE WHEN a.[RFOKey] IS NULL
                     THEN 'MissingInRFO-LoadedExtraInHybris'
                     WHEN b.[HybrisKey] IS NULL
                     THEN 'MissingInHybris-NotLoadedInHybris'
                END AS [Missing From ]
        INTO    #Missing
        FROM    #RFO a
                FULL OUTER JOIN #Hybris b ON a.RFOKey = b.[HybrisKey]
        WHERE   a.RFOKey IS NULL
                OR b.[HybrisKey] IS NULL

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Missing'

        SELECT  @SourceCount = COUNT(*)
        FROM    #Missing
        WHERE   Country = 'US'
                AND [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
        SELECT  @TargetCount = COUNT(*)
        FROM    #Missing
        WHERE   Country = 'US'
                AND [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('US_', @owner) , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO Missing Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('US_', @owner) , -- Owner - nvarchar(50)
                      'No Missing from RFO and Hybris' ,
                      'PASSED'
                    )


--CAN ACCOUNTS SCTOTG  MISSING VALIDATION 

        SET @SourceCount = 0
        SET @TargetCount = 0

        SELECT  @SourceCount = COUNT(*)
        FROM    #Missing
        WHERE   Country = 'CA'
                AND [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
        SELECT  @TargetCount = COUNT(*)
        FROM    #Missing
        WHERE   Country = 'CA'
                AND [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes,
					ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('CA_', @owner) , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO Missing Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT('CA_', @owner) , -- Owner - nvarchar(50)
                      'No Missing from RFO and Hybris' ,
                      'PASSED'
                    )


					

-- LOADING ERROR WITH SAMPLE DATA.

        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetCoulumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [RFOKey] , -- SourceValue - nvarchar(50)
                        [HybrisKey]
                FROM    #Missing
                WHERE   Country = 'US'
                        AND [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [RFOKey] , -- SourceValue - nvarchar(50)
                        [HybrisKey]
                FROM    #Missing
                WHERE   Country = 'US'
                        AND [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [RFOKey] , -- SourceValue - nvarchar(50)
                        [HybrisKey]
                FROM    #Missing
                WHERE   Country = 'CA'
                        AND [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [RFOKey] , -- SourceValue - nvarchar(50)
                        [HybrisKey]
                FROM    #Missing
                WHERE   Country = 'CA'
                        AND [Missing From ] = 'MissingInHybris-NotLoadedInHybris'


--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
		
        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp

    

        SET @message = ' STEP: 6. Removing Isuues Key from Temps. '
        EXECUTE dbqa.uspPrintMessage @message


         
        DELETE  a
        FROM    #RFO a
                JOIN #missing m ON m.RFOkey = a.RFOKey


        DELETE  a
        FROM    #Hybris a
                JOIN #missing m ON m.HybrisKey = a.HybrisKey	



        DROP TABLE #missing


   
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'Accounts'
                AND [flag] IN ( 'c2c', 'ref', 'def', 'upd' )


		

        DECLARE @MaxRow INT ,
            @RowNumber INT ,
            @rowCounts INT= 0 ,
            @TargetColumn NVARCHAR(50) ,
            @SourceColumn NVARCHAR(25) ,
            @flag NVARCHAR(50) ,
            @Stmt NVARCHAR(MAX);
        DECLARE @temp TABLE
            (
              [key] VARCHAR(50) ,--[Key]
              SourceValue VARCHAR(MAX) ,--[SourceColumn]
              TargetValue VARCHAR(MAX) --[TargetColumn] 
            );



       
        SELECT  @MaxRow = MAX(RowNumber)
        FROM    #Temp
        IF ISNULL(@MaxRow, 0) > 0
            BEGIN
                SET @Message = 'STEP: 7. Validation Started For Columnt To Column with  total fields= '
                    + CAST(@MaxRow AS NVARCHAR(20))
                EXECUTE dbqa.uspPrintMessage @message
-- Print Message no wait.

                SET @RowNumber = 1
                WHILE ( @MaxRow >= @RowNumber )
                    BEGIN
                        SELECT  @Owner = [owner] ,
                                @Flag = [flag] ,
                                @key = [Key] ,
                                @TargetColumn = TargetColumn ,
                                @SourceColumn = SourceColumn ,
                                @RowNumber = RowNumber ,
                                @Stmt = [SQL Stmt]
                        FROM    #Temp
                        WHERE   RowNumber = @RowNumber

                        SET @Message = CONCAT('Column Validation Started For ',
                                              CAST(@RowNumber AS NVARCHAR(20)),
                                              '. ', @TargetColumn)

                        EXECUTE dbqa.uspPrintMessage @message
 

                        INSERT  INTO @temp
                                ( [key], SourceValue,--[SourceColumn]
                                  TargetValue  --[TargetColumn]  
                                  )
                                EXEC sp_executesql @stmt 
                        SELECT  @rowCounts = COUNT([key])
                        FROM    @temp
						
                        IF @rowCounts <> 0
                            BEGIN
                                SET @Message = CONCAT('Total IssueCount=',
                                                      CAST(@rowCounts AS NVARCHAR(12)),
                                                      ' for ', @TargetColumn)
													   EXECUTE dbqa.uspPrintMessage @message

                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Owner , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'FAILED'
                                        )

                                INSERT  INTO dbqa.ErrorLog
                                        ( FlowTypes ,
                                          [Owner] ,
                                          Flag ,
                                          SourceColumn ,
                                          TargetCoulumn ,
                                          [Key] ,
                                          SourceValue ,
                                          TargetValue
                                        )
                                        SELECT TOP 10
                                                @Owner ,
                                                @Owner ,
                                                CONCAT(@Flag, '_EndToEnd') ,
                                                @SourceColumn ,
                                                @TargetColumn ,
                                                @Key ,
                                                SourceValue ,
                                                TargetValue
                                        FROM    @temp
                            END 
                        ELSE
                            BEGIN 
                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'PASSED'
                                        )
								

                            END 

                        DELETE  @temp
               -- PRINT @Stmt
                        SET @RowNumber = @RowNumber + 1

                    END 



            END
    END 

		
GO 

		
USE DM_QA
GO 
CREATE PROCEDURE dbqa.uspAccountPaymentProfile_BULK ( @LoadDate DATE )
AS
    BEGIN


        SET NOCOUNT ON;

        IF OBJECT_ID('TEMPDB.dbo. #RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#Hybris') IS NOT NULL
            DROP TABLE #Hybris;
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing  
        IF OBJECT_ID('tempdb.dbo.#MixMatch') IS NOT NULL
            DROP TABLE #MixMatch  


      
        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'Data Migration' ,
            @owner NVARCHAR(50)= 'AccountPaymentProfiles' ,
            @Key NVARCHAR(25)= 'AccountNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts' ,
            @OrderDate DATE= DATEADD(MONTH, -18, @LoadDate) ,
            @HardTermDate DATE= DATEADD(MONTH, -6, @LoadDate)

        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message



		
        SELECT  pp.PaymentProfileID ,--PaymentProfileID_Code
                b.AccountNumber AS [p_customerid] ,
                b.AccountID AS AccountID ,
                LTRIM(RTRIM(ccp.NameOnCard)) ProfileName , --	p_ccowner
                DisplayNumber , --	p_number	
                pp.IsDefault AS IsDefaultPayInfo ,
                apm.AccountNumber AS RFLCardNumber ,
                v.Name AS VendorID , --	p_type
                ExpMonth , --	p_validtomonth
                ExpYear , --	p_validtoyear
                ccp.BillingAddressID , --	p_billingaddress
                SUBSTRING(REVERSE(LTRIM(RTRIM(CCP.DisplayNumber))), 1, 4) AS LastFourNumber  --	p_lastfourdigit
--,cctoken	 --	p_cctoken     
        INTO    #RFO
        FROM    RFOperations.RFO_Accounts.AccountBase b
                JOIN RFOperations.RFO_Accounts.PaymentProfiles (NOLOCK) PP ON b.AccountID = PP.AccountID
                JOIN RodanFieldsLive.dbo.AccountPaymentMethods apm ON apm.AccountID = b.AccountID
                                                              AND apm.AccountPaymentMethodID = PP.PaymentProfileID
                JOIN RFOperations.RFO_Accounts.CreditCardProfiles (NOLOCK) CCP ON PP.PaymentProfileID = CCP.PaymentProfileID
                JOIN RFOperations.RFO_Reference.CreditCardVendors (NOLOCK) V ON V.VendorID = CCP.VendorID
        --JOIN Hybris.dbo.users hu ON CAST(PP.AccountID AS VARCHAR) = hu.p_rfaccountid
        WHERE   EXISTS ( SELECT 1
                         FROM   RFoperations.RFo_Accounts.Addresses ad
                         WHERE  ad.AddressID = ccp.BillingAddressID )
                AND EXISTS ( SELECT 1
                             FROM   RFOperations.RFO_Accounts.AccountBase ab
                                    JOIN RFOperations.RFO_Accounts.AccountRF rf ON ab.AccountID = rf.AccountID
                                    JOIN RFOperations.RFO_Reference.AccountContactType t ON t.AccountContactTypeID = ab.AccountTypeID
                             WHERE  ab.accountID = b.AcccountID
                                    AND ea.EmailAddressTypeID = 1
                                    AND ab.AccountNumber <> 'AnonymousRetailAccount'
                                    AND ( ab.AccountStatusID IN ( 1, 2, 8, 9 ) -- Active,Inactive,SoftVoluntary,SoftInvoluntary
                                          OR ( ab.AccountStatusID = 10
                                               AND EXISTS ( SELECT
                                                              1
                                                            FROM
                                                              RFOperations.Hybris.orders ro
                                                            WHERE
                                                              ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= @HardTermDate ) --HardTerm with 6 months order
                                             )
                                          OR ( ab.AccountStatusID IN ( 3, 4, 5,
                                                              6, 7 )
                                               AND EXISTS ( SELECT
                                                              1
                                                            FROM
                                                              RFOperations.Hybris.orders ro
                                                            WHERE
                                                              ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= @OrderDate )--Other Status with 18 months orders
                                             )
                                        ) )

/*
SELECT  *
FROM    RFOperations.RFO_Accounts.Addresses a
        RIGHT JOIN RFOperations.RFO_Accounts.CreditCardProfiles b ON a.AddressID = b.BillingAddressID
WHERE   a.AddressID IS NULL  

*/



        SET @message = ' STEP: 2. Target Table Loading.'
        EXECUTE dbqa.uspPrintMessage @message



	

        SELECT  p.pk AS paymentInfoPK ,
                u.p_customerid AS HyrbisKey ,
                p_ccowner , --	ProfileName
                p_number , --	DisplayNumber
                v.Code AS p_type , --	VendorID
                p_validtomonth , --	ExpMonth
                p_validtoyear , --	ExpYear
                p.p_billingaddress , --	BillingAddressID
                p_lastfourdigit , --	LastFourNumber
                p_cctoken , --	cctoken
                p.p_original ,
                p.OwnerPkString ,
                CASE WHEN u.p_defaultpaymentinfo = p.pk THEN 1
                     ELSE 0
                END AS IsDefaultInfo
        INTO    #Hybris
        FROM    Hybris.dbo.users (NOLOCK) u
                JOIN Hybris.dbo.paymentinfos (NOLOCK) p ON p.p_user = u.PK
                LEFT JOIN Hybris.dbo.enumerationvalues v ON v.pk = p.p_type
                LEFT JOIN Hybris.dbo.addresses (NOLOCK) a ON a.PK = p.p_billingaddress
        WHERE   p.p_duplicate = 0
                AND a.p_duplicate = 0;
               







        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message

		

        CREATE CLUSTERED INDEX cls_RFO ON  #RFO([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #Hybris([HybrisKey])

/* Source to Target Total Counts */


--++++++++++++++++++++++++++++++++++++
--  Count Validation
--++++++++++++++++++++++++++++++++++++

     




        SET @message = ' STEP: 4.initiating COUNT Validation  '
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  @SourceCount = COUNT([RFOKey])
        FROM    #RFO
        SELECT  @TargetCount = COUNT([HybrisKey])
        FROM    #Hybris

-- INSERTING LOG FOR TOTALCOUNT VALIDATION.

        INSERT  INTO dbqa.SourceTargetLog
                ( FlowTypes ,
                  ValidationTypes ,
                  [Owner] ,
                  SourceCount ,
                  TargetCount ,
                  Comments ,
                  ExecutionStatus 
                )
        VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                  @ValidationType , -- ValidationTypes - nvarchar(50)
                  @Owner , -- Owner - nvarchar(50)
                  @SourceCount , -- SourceCount - int
                  @TargetCount , -- TargetCounts - int
                  CASE WHEN @SourceCount > @TargetCount
                       THEN CONCAT('RFO Count More than HYBRIS BY ',
                                   CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
                       WHEN @SourceCount < @TargetCount
                       THEN CONCAT('HYBRIS Count More than RFO Count By ',
                                   CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
                       ELSE 'RFO and HYBRIS Counts are equal'
                  END ,
                  CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
                       ELSE 'FAILED'
                  END
                )
		




        SET @message = ' STEP: 5.initiating DUPLICATE Validation  '
        EXECUTE dbqa.uspPrintMessage @message

--++++++++++++++++++--++++++++++++++++++
--  RFO  DUPLICATE VALIDATION
--++++++++++++++++++--++++++++++++++++++

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Duplicate'


        SELECT  @SourceCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([RFOKey]) Ct
                  FROM      #RFO
                  GROUP BY  [RFOKey] ,
                            PaymentProfileID
                  HAVING    COUNT([RFOKey]) > 1
                ) t



        IF ISNULL(@SourceCount, 0) > 0
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount , -- SourceCount - int         
                      'RFO has Duplicate PaymentInfo' ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'RFO has NO Duplicate PaymentInfos' ,
                      'PASSED'
                    )




--++++++++++++++++++--+++++++++++++++++++++
-- HYBRIS DUPLICATE VAIDATION.
--++++++++++++++++++--+++++++++++++++++++++

   

        SELECT  @TargetCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([HybrisKey]) Ct
                  FROM      #Hybris
                  GROUP BY  [HybrisKey] ,
                            paymentInfoPK
                  HAVING    COUNT([HybrisKey]) > 1
                ) t



        IF ISNULL(@TargetCount, 0) > 0
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @TargetCount , -- SourceCount - int         
                      'HYBRIS has Duplicate PaymentInfo' ,
                      'FAILED'
                    )

	




        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'HYBRIS has NO Duplicate PaymentInfos' ,
                      'PASSED'
                    )

		



--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++

        SET @message = ' STEP: 6.initiating MISSING Validation now '
        EXECUTE dbqa.uspPrintMessage @message


--US ACCOUNTS SCTOTG  MISSING VALIDATION 

        SELECT  a.RFOKey ,
                b.HybrisKey ,
                CASE WHEN a.RFOkey IS NULL
                     THEN 'MissingInRFO-LoadedExtraInHybris'
                     WHEN b.HybrisKey IS NULL
                     THEN 'MissingInHybris-NotLoadedInHybris'
                END AS [Missing From ]
        INTO    #Missing
        FROM    #RFO a
                FULL OUTER JOIN #Hybris b ON a.RFOKey = b.HybrisKey
        WHERE   a.RFOKey IS NULL
                OR b.HybrisKey IS NULL 



        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Missing'

        SELECT  @SourceCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
        SELECT  @TargetCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO Missing Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'No Missing from RFO and Hybris' ,
                      'PASSED'
                    )

					

-- LOADING ERROR WITH SAMPLE DATA.

        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetCoulumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
			




-- MixNMatch

        SET @message = ' STEP: 7.initiating MIXMATCH Validation '
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  a.RFOKey ,
                b.[HybrisKey] ,
                CASE WHEN a.RFC > b.HBC
                     THEN 'paymentInfos Not Loaded in hybris'
                     WHEN a.RFC < b.HBC
                     THEN 'paymentInfos Extra Loaded in hybris'
                END AS MixMatch
        INTO    #MixMatch
        FROM    ( SELECT    RFOKey ,
                            COUNT(*) AS [RFC]
                  FROM      #RFO
                  GROUP BY  RFOKey
                ) a
                JOIN ( SELECT   HybrisKey ,
                                COUNT(*) AS [HBC]
                       FROM     #Hybris
                       GROUP BY HybrisKey
                     ) b ON a.RFOKey = b.[HybrisKey]
        WHERE   a.RFC <> b.HBC



        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'MixMatch'

        SELECT  @SourceCount = COUNT(*)
        FROM    #MixMatch
 
        SELECT  @TargetCount = COUNT(*)
        FROM    #MixMatch

	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO MixMatch Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'No MixMatch Counts' ,
                      'PASSED'
                    )




   
        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetCoulumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @ValidationType , -- ValidationTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        RFOKey , -- SourceColumn - nvarchar(50)
                        HybrisKey , -- TargetCoulumn - nvarchar(50)
                        @key , -- Key - nvarchar(50)
                        RFC , -- SourceValue - nvarchar(50)
                        HBC
                FROM    #MixMatch




--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
	


        SET @message = ' STEP: 8.Removing Issues for END TO END Validaion  '
        EXECUTE dbqa.uspPrintMessage @message


        DELETE  a
        FROM    #RFO a
                JOIN #Missing m ON m.RFOKey = a.RFOKey 
		

        DELETE  a
        FROM    #RFO a
                JOIN #MixMatch m ON m.RFOKey = a.RFOKey 				   
						 
			


        DELETE  a
        FROM    #Hybris a
                JOIN #Missing m ON m.HybrisKey = a.HybrisKey 

        DELETE  a
        FROM    #Hybris a
                JOIN #MixMatch m ON m.HybrisKey = a.HybrisKey 

       



       
        DROP TABLE #missing
  -- Droping Table for performace
        DROP TABLE #MixMatch
  -- Droping Table for performace

      
      

--DECLARE @message NVARCHAR(100)



        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp
			
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'AccountPaymentProfiles'
                AND [flag] IN ( 'c2c', 'ref', 'def' )


		
		
        DECLARE @MaxRow INT ,
            @RowNumber INT ,
            @rowCounts INT= 0 ,
            @TargetColumn NVARCHAR(50) ,
            @SourceColumn NVARCHAR(25) ,
            @flag NVARCHAR(50) ,
            @Stmt NVARCHAR(MAX);
        DECLARE @temp TABLE
            (
              [key] VARCHAR(50) ,--[Key]
              SourceValue VARCHAR(MAX) ,--[SourceColumn]
              TargetValue VARCHAR(MAX) --[TargetColumn] 
            );


        SELECT  @MaxRow = MAX(RowNumber)
        FROM    #Temp
        IF ISNULL(@MaxRow, 0) > 0
            BEGIN
                SET @Message = 'STEP: 9.Validation Started For Columnt To Column with  total fields= '
                    + CAST(@MaxRow AS NVARCHAR(20))              
                EXECUTE dbqa.uspPrintMessage @message
-- Print Message no wait.

                SET @RowNumber = 1
                WHILE ( @MaxRow >= @RowNumber )
                    BEGIN
                        SELECT  @Owner = [owner] ,
                                @Flag = [flag] ,
                                @key = [Key] ,
                                @TargetColumn = TargetColumn ,
                                @SourceColumn = SourceColumn ,
                                @RowNumber = RowNumber ,
                                @Stmt = [SQL Stmt]
                        FROM    #Temp
                        WHERE   RowNumber = @RowNumber

                        SET @Message = CONCAT('Column Validation Started For ',
                                              CAST(@RowNumber AS NVARCHAR(20)),
                                              '. ', @TargetColumn)
                        EXECUTE dbqa.uspPrintMessage @message

                        INSERT  INTO @temp
                                ( [key], SourceValue,--[SourceColumn]
                                  TargetValue  --[TargetColumn]  
                                  )
                                EXEC sp_executesql @stmt 
                        SELECT  @rowCounts = COUNT([key])
                        FROM    @temp
						
                        IF @rowCounts <> 0
                            BEGIN
                                SET @Message = CONCAT('Total IssueCount=',
                                                      CAST(@rowCounts AS NVARCHAR(12)),
                                                      ' for ', @TargetColumn)


                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Owner , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'FAILED'
                                        )

                                INSERT  INTO dbqa.ErrorLog
                                        ( FlowTypes ,
                                          [Owner] ,
                                          Flag ,
                                          SourceColumn ,
                                          TargetCoulumn ,
                                          [Key] ,
                                          SourceValue ,
                                          TargetValue
                                        )
                                        SELECT TOP 10
                                                @Owner ,
                                                @Owner ,
                                                CONCAT(@Flag, '_EndToEnd') ,
                                                @SourceColumn ,
                                                @TargetColumn ,
                                                @Key ,
                                                SourceValue ,
                                                TargetValue
                                        FROM    @temp
                            END 
                        ELSE
                            BEGIN 
                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'PASSED'
                                        )
								

                            END 

                        DELETE  @temp
               -- PRINT @Stmt
                        SET @RowNumber = @RowNumber + 1

                    END 



            END
			




    END 


	GO
	
--*******************************************************************************************************
--					ACCOUNT Contact ADDRESSES VALIDATIONS
--*******************************************************************************************************

USE DM_QA
GO 

CREATE PROCEDURE dbqa.uspAccountContractAddress_BULK
AS
    BEGIN

        SET NOCOUNT ON;

        IF OBJECT_ID('TEMPDB.dbo. #RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#Hybris') IS NOT NULL
            DROP TABLE #Hybris;
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing  
        IF OBJECT_ID('tempdb.dbo.#MixMatch') IS NOT NULL
            DROP TABLE #MixMatch  

        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @LoadDate DATE= '2017-02-28' ,
            @Flows NVARCHAR(50)= 'Data Migration' ,
            @owner NVARCHAR(50)= 'Accounts_ContactAddress' ,
            @Key NVARCHAR(25)= 'AccountNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts' ,
            @OrderDate DATE ,
            @HardTermDate DATE

        SET @OrderDate = DATEADD(MONTH, -18, @LoadDate)
        SET @HardTermDate = DATEADD(MONTH, -6, @LoadDate)	
        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  a.addressID ,
                ac.AccountId AS [RFOKey] ,
                co.Alpha2Code AS CountryID ,
                a.Region ,
                apm.PhoneNumber AS MainNumber ,
                adh.PhoneNumnber AS HomeNumber ,
                adc.PhoneNumnber AS CellNumber ,
                CASE WHEN g.Name IN ( 'male', 'female' ) THEN g.Name
                     ELSE NULL
                END AS GenderID , --	p_gender
                CASE WHEN a.AddressTypeID = 3 THEN 1
                     ELSE 0
                END AS IsBillingAddress , --	p_billingaddress
                CASE WHEN a.AddressTypeID = 1 THEN 1
                     ELSE 0
                END AS IsContactAddress , --	p_contactaddress
                CASE WHEN a.AddressTypeID = 2 THEN 1
                     ELSE 0
                END AS IsShippingAddress , --	p_shippingaddress
                a.Locale , --	p_town
                a.AddressLine1 , --	p_streetname
                a.AddressLine2 , --	p_streennumber
                ac.FirstName , --	p_firstname
                ac.LastName , --	p_lastname
                ac.MiddleName , --	p_MiddleName
                a.PostalCode , --	p_postalcode
                IIF(CAST(ac.BirthDay AS DATE) = '1900-01-01', NULL, ac.Birthday) AS BirthDay , --	p_dateofbirth
                IsDefault
        INTO    #RFO
        FROM    RFOperations.RFO_Accounts.Addresses a
                JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AddressID = a.AddressID
                                                              AND AddressTypeID = 1 -- Main AddressType
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = a.CountryID
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = aca.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
                LEFT JOIN ( SELECT  AccountcontactID ,
                                    ph.PhoneNumberRaw AS PhoneNumber
                            FROM    RFOperations.RFO_Accounts.AccountContactPhones aph
                                    JOIN RFOperations.RFO_Accounts.Phones ph ON ph.PhoneID = aph.PhoneId
                                                              AND PhoneTypeID = 1
                          ) apm ON apm.AccountContactId = ac.AccountContactId
                LEFT JOIN ( SELECT  adp.addressID ,
                                    p.PhoneNumberRaw AS PhoneNumnber
                            FROM    RFOperations.RFO_Accounts.AddressPhones adp
                                    JOIN RFOperations.RFO_Accounts.Phones p ON p.PhoneID = adp.PhoneId
                                                              AND p.PhoneTypeID = 2
                          ) adh ON adh.AddressId = a.AddressID
                LEFT JOIN ( SELECT  app.addressID ,
                                    p.PhoneNumberRaw AS PhoneNumnber
                            FROM    RFOperations.RFO_Accounts.AddressPhones app
                                    JOIN RFOperations.RFO_Accounts.Phones p ON p.PhoneID = app.PhoneId
                                                              AND p.PhoneTypeID = 3
                          ) adc ON adc.AddressId = a.AddressID
        WHERE   EXISTS ( SELECT 1
                         FROM   Hybris.dbo.users u
                         WHERE  u.P_accountID = ac.accountId )
	--OR 
       /* AND EXISTS ( SELECT 1
                     FROM   RFOperations.RFO_Accounts.AccountBase ab
                            JOIN RFOperations.RFO_Accounts.AccountRF rf ON ab.AccountID = rf.AccountID
                            JOIN RFOperations.RFO_Reference.AccountContactType t ON t.AccountContactTypeID = ab.AccountTypeID
                     WHERE  ab.accountID = ac.AcccountID
                            AND ea.EmailAddressTypeID = 1
                            AND ab.AccountNumber <> 'AnonymousRetailAccount'
                            AND ( ab.AccountStatusID IN ( 1, 2, 8, 9 ) -- Active,Inactive,SoftVoluntary,SoftInvoluntary
                                  OR ( ab.AccountStatusID = 10
                                       AND EXISTS ( SELECT  1
                                                    FROM    RFOperations.Hybris.orders ro
                                                    WHERE   ro.AccountID = ab.AccountID
                                                            AND CAST(ro.CompletionDate AS DATE) >= @HardTermDate ) --HardTerm with 6 months order
                                     )
                                  OR ( ab.AccountStatusID IN ( 3, 4, 5, 6, 7 )
                                       AND EXISTS ( SELECT  1
                                                    FROM    RFOperations.Hybris.orders ro
                                                    WHERE   ro.AccountID = ab.AccountID
                                                            AND CAST(ro.CompletionDate AS DATE) >= @OrderDate )--Other Status with 18 months orders
                                     )
                                ) )
					 */


	
			
        SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
        EXECUTE dbqa.uspPrintMessage @message



        SELECT  u.p_accountnumber [HybrisKey] ,
                co.p_isocode AS p_counrty , --	CountryID
                r.p_isocodeshort AS p_region , --	region
                p_phone1 , --	MainNumber
                p_phone2 , --	HomeNumber
                p_cellphone , --	CellNumber
                g.Code AS p_gender , --	GenderID
                p_billingaddress , --	IsBillingAddress
                p_contactaddress , --	IsContactAddress
                p_shippingaddress , --	IsShippingID
                p_town , --	Locale
                p_streetname , --	AddressLine1
                p_streetnumber , --	AddressLine2
                p_firstname , --	FirstName
                p_lastname , --	LastName
                p_MiddleName , --	MiddleName
                p_postalcode , --	PostalCode
                a.p_dateofbirth --	BirthDay    
        INTO    #Hybris
        FROM    Hybris.dbo.addresses a
                JOIN Hybris.dbo.users u ON u.pk = a.OwnerPkString
                                           AND a.p_duplicate = 0
                                           AND a.p_contactaddress = 1
                JOIN Hybris.dbo.countries co ON co.pk = a.p_country
                JOIN Hybris.dbo.regions r ON r.pk = a.p_region
                LEFT JOIN Hybris.dbo.enumerationvalues g ON g.pk = a.p_gender


			
			
        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message      


        CREATE CLUSTERED INDEX cls_RFO ON #RFOAddress([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #HybrisAddress([HybrisKey])
		
-- Count Validaiton 

        SET @message = ' STEP: 4.initiating COUNT Validation  '
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  @SourceCount = COUNT(RFOKey)
        FROM    #RFOAddress
          

        SELECT  @TargetCount = COUNT(HybrisKey)
        FROM    #HybrisAddress
           

			
     

-- INSERTING LOG FOR TOTALCOUNT VALIDATION.

        INSERT  INTO dbqa.SourceTargetLog
                ( FlowTypes ,
                  ValidationTypes ,
                  [Owner] ,
                  SourceCount ,
                  TargetCount ,
                  Comments ,
                  ExecutionStatus 
                )
        VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                  @ValidationType , -- ValidationTypes - nvarchar(50)
                  @Owner , -- Owner - nvarchar(50)
                  @SourceCount , -- SourceCount - int
                  @TargetCount , -- TargetCounts - int
                  CASE WHEN @SourceCount > @TargetCount
                       THEN CONCAT('RFO Count More than HYBRIS BY ',
                                   CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
                       WHEN @SourceCount < @TargetCount
                       THEN CONCAT('HYBRIS Count More than RFO Count By ',
                                   CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
                       ELSE 'RFO and HYBRIS Counts are equal'
                  END ,
                  CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
                       ELSE 'FAILED'
                  END
                )
		
		

--++++++++++++++++++--++++++++++++++++++
--  RFO  DUPLICATE VALIDATION
--++++++++++++++++++--++++++++++++++++++
        SET @message = ' STEP: 5.initiating DUPLICATE Need to updaete Scripts   '
        EXECUTE dbqa.uspPrintMessage @message

   -- We should user some strong logic to validate Duplicate Address , Now making Pending

   --Contact Addresses: 
        SET @SourceCount = 0
        SET @ValidationType = 'Duplicate'
        SELECT  @SourceCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([RFOKey]) Ct
                  FROM      #RFOAddress
                  WHERE     IsContactAddress = 1
                  GROUP BY  [RFOKey] ,
                            AddressID
                  HAVING    COUNT([RFOKey]) > 1
                ) t




   -- We should user some strong logic to validate Duplicate Address , Now making Pending



					
        IF ISNULL(@TargetCount, 0) > 0
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @TargetCount , -- SourceCount - int         
                      'HYBRIS has Duplicate PaymentInfo' ,
                      'FAILED'
                    )
					
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'HYBRIS has NO Duplicate PaymentInfos' ,
                      'PASSED'
                    )

		
		
		
--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++

        SET @message = ' STEP: 6.initiating MISSING Validation now '
        EXECUTE dbqa.uspPrintMessage @message
		

		 
    
        SELECT  a.RFOKey ,
                b.HybrisKey ,
                CASE WHEN a.RFOkey IS NULL
                     THEN 'MissingInRFO-LoadedExtraInHybris'
                     WHEN b.HybrisKey IS NULL
                     THEN 'MissingInHybris-NotLoadedInHybris'
                END AS [Missing From ]
        INTO    #Missing
        FROM    #RFOAddress a
                FULL OUTER JOIN #HybrisAddress b ON a.RFOKey = b.HybrisKey
        WHERE   a.RFOKey IS NULL
                OR b.HybrisKey IS NULL
                  
					

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Missing'

        SELECT  @SourceCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
        SELECT  @TargetCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO Missing Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'No Missing from RFO and Hybris' ,
                      'PASSED'
                    )

   -- Loading Sample data
   
        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetCoulumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'



				

-- MixNMatch


        SET @message = ' STEP: 7.initiating MIXMATCH Validation '
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  a.RFOKey ,
                b.[HybrisKey] ,
                RFC ,
                HBC ,
                CASE WHEN a.RFC > b.HBC
                     THEN 'AccContactAddress Not Loaded in hybris'
                     WHEN a.RFC < b.HBC
                     THEN 'AccContactAddress Extra Loaded in hybris'
                END AS MixMatch
        INTO    #MixMatch
        FROM    ( SELECT    RFOKey ,
                            COUNT(*) AS [RFC]
                  FROM      #RFOAddress
                  GROUP BY  RFOKey
                ) a
                JOIN ( SELECT   HybrisKey ,
                                COUNT(*) AS [HBC]
                       FROM     #HybrisAddress
                       GROUP BY HybrisKey
                     ) b ON a.RFOKey = b.[HybrisKey]
        WHERE   a.RFC <> b.HBC

		

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'MixMatch'

        SELECT  @SourceCount = COUNT(*)
        FROM    #MixMatch
 
        SELECT  @TargetCount = COUNT(*)
        FROM    #MixMatch

	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO MixMatch Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'No MixMatch Counts' ,
                      'PASSED'
                    )




   
        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetCoulumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        @ValidationType , -- Flag - nvarchar(10)
                        RFOKey , -- SourceColumn - nvarchar(50)
                        HybrisKey , -- TargetCoulumn - nvarchar(50)
                        @key , -- Key - nvarchar(50)
                        RFC , -- SourceValue - nvarchar(50)
                        HBC
                FROM    #MixMatch





--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
	

	
        SET @message = ' STEP: 8.Removing Issues for END TO END Validaion  '
        EXECUTE dbqa.uspPrintMessage @message
				

        DELETE  a
        FROM    #RFO a
                JOIN #missing m ON m.RFOkey = a.RFOKey
		
        DELETE  a
        FROM    #RFO a
                JOIN #MixMatch m ON m.RFOkey = a.RFOKey


        DELETE  a
        FROM    #Hybris a
                JOIN #missing m ON m.HybrisKey = a.HybrisKey	
		

        DELETE  a
        FROM    #Hybris a
                JOIN #MixMatch m ON m.HybrisKey = a.HybrisKey	



        DROP TABLE #missing

        DROP TABLE #MixMatch


        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp
			
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'AccountAddresses'
                AND [flag] IN ( 'c2c', 'ref', 'def' )


		
		
        DECLARE @MaxRow INT ,
            @RowNumber INT ,
            @rowCounts INT= 0 ,
            @TargetColumn NVARCHAR(50) ,
            @SourceColumn NVARCHAR(25) ,
            @flag NVARCHAR(50) ,
            @Stmt NVARCHAR(MAX);
        DECLARE @temp TABLE
            (
              [key] VARCHAR(50) ,--[Key]
              SourceValue VARCHAR(MAX) ,--[SourceColumn]
              TargetValue VARCHAR(MAX) --[TargetColumn] 
            );


        SELECT  @MaxRow = MAX(RowNumber)
        FROM    #Temp
        IF ISNULL(@MaxRow, 0) > 0
            BEGIN

                SET @Message = 'STEP: 9.Validation Started For Columnt To Column with  total fields= '
                    + CAST(@MaxRow AS NVARCHAR(20))              
                EXECUTE dbqa.uspPrintMessage @message

                SET @RowNumber = 1
                WHILE ( @MaxRow >= @RowNumber )
                    BEGIN
                        SELECT  @Owner = [owner] ,
                                @Flag = [flag] ,
                                @key = [Key] ,
                                @TargetColumn = TargetColumn ,
                                @SourceColumn = SourceColumn ,
                                @RowNumber = RowNumber ,
                                @Stmt = [SQL Stmt]
                        FROM    #Temp
                        WHERE   RowNumber = @RowNumber

                        SET @Message = CONCAT('Column Validation Started For ',
                                              CAST(@RowNumber AS NVARCHAR(20)),
                                              '. ', @TargetColumn)
                        EXECUTE dbqa.uspPrintMessage @message

                        INSERT  INTO @temp
                                ( [key], SourceValue,--[SourceColumn]
                                  TargetValue  --[TargetColumn]  
                                  )
                                EXEC sp_executesql @stmt 
                        SELECT  @rowCounts = COUNT([key])
                        FROM    @temp
						
                        IF @rowCounts <> 0
                            BEGIN
                                SET @Message = CONCAT('Total IssueCount=',
                                                      CAST(@rowCounts AS NVARCHAR(12)),
                                                      ' for ', @TargetColumn)


                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Owner , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'FAILED'
                                        )

                                INSERT  INTO dbqa.ErrorLog
                                        ( FlowTypes ,
                                          [Owner] ,
                                          Flag ,
                                          SourceColumn ,
                                          TargetCoulumn ,
                                          [Key] ,
                                          SourceValue ,
                                          TargetValue
                                        )
                                        SELECT TOP 10
                                                @Owner ,
                                                @Owner ,
                                                CONCAT(@Flag, '_EndToEnd') ,
                                                @SourceColumn ,
                                                @TargetColumn ,
                                                @Key ,
                                                SourceValue ,
                                                TargetValue
                                        FROM    @temp
                            END 
                        ELSE
                            BEGIN 
                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'PASSED'
                                        )
								

                            END 

                        DELETE  @temp
               -- PRINT @Stmt
                        SET @RowNumber = @RowNumber + 1

                    END 



            END
    END 

	GO
	
--*******************************************************************************************************
--					ACCOUNT BILLING ADDRESSES VALIDATIONS
--*******************************************************************************************************


USE DM_QA
GO 

CREATE  PROCEDURE dbqa.uspAccountBillingAddress_BULK ( @LoadDate DATE )
AS
    BEGIN


        SET NOCOUNT ON;

        IF OBJECT_ID('TEMPDB.dbo. #RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#Hybris') IS NOT NULL
            DROP TABLE #Hybris;
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing  
        IF OBJECT_ID('tempdb.dbo.#MixMatch') IS NOT NULL
            DROP TABLE #MixMatch  

        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'Data Migration' ,
            @owner NVARCHAR(50)= 'Accounts_BillingAddress' ,
            @Key NVARCHAR(25)= 'AccountNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts' ,
            @OrderDate DATE= DATEADD(MONTH, -18, @LoadDate) ,
            @HardTermDate DATE= DATEADD(MONTH, -6, @LoadDate)

        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  a.addressID ,
                ac.AccountId AS [RFOKey] ,
                co.Alpha2Code AS CountryID ,
                a.Region ,
                apm.PhoneNumber AS MainNumber ,
                adh.PhoneNumnber AS HomeNumber ,
                adc.PhoneNumnber AS CellNumber ,
                CASE WHEN g.Name IN ( 'male', 'female' ) THEN g.Name
                     ELSE NULL
                END AS GenderID , --	p_gender
                CASE WHEN a.AddressTypeID = 3 THEN 1
                     ELSE 0
                END AS IsBillingAddress , --	p_billingaddress
                CASE WHEN a.AddressTypeID = 1 THEN 1
                     ELSE 0
                END AS IsContactAddress , --	p_contactaddress
                CASE WHEN a.AddressTypeID = 2 THEN 1
                     ELSE 0
                END AS IsShippingAddress , --	p_shippingaddress
                a.Locale , --	p_town
                a.AddressLine1 , --	p_streetname
                a.AddressLine2 , --	p_streennumber
                ac.FirstName , --	p_firstname
                ac.LastName , --	p_lastname
                ac.MiddleName , --	p_MiddleName
                a.PostalCode , --	p_postalcode
                IIF(CAST(ac.BirthDay AS DATE) = '1900-01-01', NULL, ac.Birthday) AS BirthDay , --	p_dateofbirth
                IsDefault
        INTO    #RFO
        FROM    RFOperations.RFO_Accounts.Addresses a
                JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AddressID = a.AddressID
                                                              AND AddressTypeID = 3 -- Billing AddressType
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = a.CountryID
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = aca.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
                LEFT JOIN ( SELECT  AccountcontactID ,
                                    ph.PhoneNumberRaw AS PhoneNumber
                            FROM    RFOperations.RFO_Accounts.AccountContactPhones aph
                                    JOIN RFOperations.RFO_Accounts.Phones ph ON ph.PhoneID = aph.PhoneId
                                                              AND PhoneTypeID = 1
                          ) apm ON apm.AccountContactId = ac.AccountContactId
                LEFT JOIN ( SELECT  adp.addressID ,
                                    p.PhoneNumberRaw AS PhoneNumnber
                            FROM    RFOperations.RFO_Accounts.AddressPhones adp
                                    JOIN RFOperations.RFO_Accounts.Phones p ON p.PhoneID = adp.PhoneId
                                                              AND p.PhoneTypeID = 2
                          ) adh ON adh.AddressId = a.AddressID
                LEFT JOIN ( SELECT  app.addressID ,
                                    p.PhoneNumberRaw AS PhoneNumnber
                            FROM    RFOperations.RFO_Accounts.AddressPhones app
                                    JOIN RFOperations.RFO_Accounts.Phones p ON p.PhoneID = app.PhoneId
                                                              AND p.PhoneTypeID = 3
                          ) adc ON adc.AddressId = a.AddressID
                JOIN RFOperations.RFO_Accounts.CreditCardProfiles cp ON cp.BillingAddressID = a.AddressID -- Only addresses with CreditCardProfiles
        WHERE   EXISTS ( SELECT 1
                         FROM   Hybris.dbo.users u
                         WHERE  u.P_accountID = ac.accountId )
	--OR 
       /* AND EXISTS ( SELECT 1
                     FROM   RFOperations.RFO_Accounts.AccountBase ab
                            JOIN RFOperations.RFO_Accounts.AccountRF rf ON ab.AccountID = rf.AccountID
                            JOIN RFOperations.RFO_Reference.AccountContactType t ON t.AccountContactTypeID = ab.AccountTypeID
                     WHERE  ab.accountID = ac.AcccountID
                            AND ea.EmailAddressTypeID = 1
                            AND ab.AccountNumber <> 'AnonymousRetailAccount'
                            AND ( ab.AccountStatusID IN ( 1, 2, 8, 9 ) -- Active,Inactive,SoftVoluntary,SoftInvoluntary
                                  OR ( ab.AccountStatusID = 10
                                       AND EXISTS ( SELECT  1
                                                    FROM    RFOperations.Hybris.orders ro
                                                    WHERE   ro.AccountID = ab.AccountID
                                                            AND CAST(ro.CompletionDate AS DATE) >= @HardTermDate ) --HardTerm with 6 months order
                                     )
                                  OR ( ab.AccountStatusID IN ( 3, 4, 5, 6, 7 )
                                       AND EXISTS ( SELECT  1
                                                    FROM    RFOperations.Hybris.orders ro
                                                    WHERE   ro.AccountID = ab.AccountID
                                                            AND CAST(ro.CompletionDate AS DATE) >= @OrderDate )--Other Status with 18 months orders
                                     )
                                ) )
					 */

			
        SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
        EXECUTE dbqa.uspPrintMessage @message
	

        SELECT  u.p_accountnumber [HybrisKey] ,
                co.p_isocode AS p_counrty , --	CountryID
                r.p_isocodeshort AS p_region , --	region
                p_phone1 , --	MainNumber
                p_phone2 , --	HomeNumber
                p_cellphone , --	CellNumber
                g.Code AS p_gender , --	GenderID
                p_billingaddress , --	IsBillingAddress
                p_contactaddress , --	IsContactAddress
                p_shippingaddress , --	IsShippingID
                p_town , --	Locale
                p_streetname , --	AddressLine1
                p_streetnumber , --	AddressLine2
                p_firstname , --	FirstName
                p_lastname , --	LastName
                p_MiddleName , --	MiddleName
                p_postalcode , --	PostalCode
                a.p_dateofbirth --	BirthDay    
        INTO    #Hybris
        FROM    Hybris.dbo.addresses a
                JOIN Hybris.dbo.paymentinfos p ON p.pk = a.OwnerPkString
                                                  AND a.p_duplicate = 0
                                                  AND p.p_duplicate = 0
                JOIN Hybris.dbo.countries co ON co.pk = a.p_country
                JOIN Hybris.dbo.regions r ON r.pk = a.p_region
                LEFT JOIN Hybris.dbo.enumerationvalues g ON g.pk = a.p_gender


			

			
			
     


        CREATE CLUSTERED INDEX cls_RFO ON #RFOAddress([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #HybrisAddress([HybrisKey])

        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message  


		-- Count Validaiton 

        SET @message = ' STEP: 4.initiating COUNT Validation  '
        EXECUTE dbqa.uspPrintMessage @message



        SELECT  @SourceCount = COUNT(RFOKey)
        FROM    #RFOAddress
          
        SELECT  @TargetCount = COUNT(HybrisKey)
        FROM    #HybrisAddress
           

			
     

-- INSERTING LOG FOR TOTALCOUNT VALIDATION.

        INSERT  INTO dbqa.SourceTargetLog
                ( FlowTypes ,
                  ValidationTypes ,
                  [Owner] ,
                  SourceCount ,
                  TargetCount ,
                  Comments ,
                  ExecutionStatus 
                )
        VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                  @ValidationType , -- ValidationTypes - nvarchar(50)
                  @Owner , -- Owner - nvarchar(50)
                  @SourceCount , -- SourceCount - int
                  @TargetCount , -- TargetCounts - int
                  CASE WHEN @SourceCount > @TargetCount
                       THEN CONCAT('RFO Count More than HYBRIS BY ',
                                   CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
                       WHEN @SourceCount < @TargetCount
                       THEN CONCAT('HYBRIS Count More than RFO Count By ',
                                   CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
                       ELSE 'RFO and HYBRIS Counts are equal'
                  END ,
                  CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
                       ELSE 'FAILED'
                  END
                )
		
		

--++++++++++++++++++--++++++++++++++++++
--  RFO  DUPLICATE VALIDATION
--++++++++++++++++++--++++++++++++++++++
        SET @message = ' STEP: 5.initiating DUPLICATE Need to updaete Scripts   '
        EXECUTE dbqa.uspPrintMessage @message

--SET @SourceCount = 0
--SET @TargetCount = 0
--SET @ValidationType = 'Duplicate'


--SELECT  @SourceCount = COUNT(t.Ct)
--FROM    ( SELECT    COUNT([RFOKey]) Ct
--          FROM      #RFO
--          GROUP BY  [RFOKey] ,
--                    PaymentProfileID
--          HAVING    COUNT([RFOKey]) > 1
--        ) t



--IF ISNULL(@SourceCount, 0) > 0
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              SourceCount ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              @SourceCount , -- SourceCount - int         
--              'RFO has Duplicate PaymentInfo' ,
--              'FAILED'
--            )
--ELSE
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              'RFO has NO Duplicate PaymentInfos' ,
--              'PASSED'
--            )


--SELECT  @TargetCount = COUNT(t.Ct)
--FROM    ( SELECT    COUNT([HybrisKey]) Ct
--          FROM      #Hybris
--          GROUP BY  [HybrisKey] ,
--                    paymentInfoPK
--          HAVING    COUNT([HybrisKey]) > 1
--        ) t



--IF ISNULL(@TargetCount, 0) > 0
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              TargetCount ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              @TargetCount , -- SourceCount - int         
--              'HYBRIS has Duplicate PaymentInfo' ,
--              'FAILED'
--            )

	




--ELSE
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              'HYBRIS has NO Duplicate PaymentInfos' ,
--              'PASSED'
--            )
		
		
--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++


        SET @message = ' STEP: 6.initiating MISSING Validation now '
        EXECUTE dbqa.uspPrintMessage @message
		

		 
    
        SELECT  a.RFOKey ,
                b.HybrisKey ,
                CASE WHEN a.RFOkey IS NULL
                     THEN 'MissingInRFO-LoadedExtraInHybris'
                     WHEN b.HybrisKey IS NULL
                     THEN 'MissingInHybris-NotLoadedInHybris'
                END AS [Missing From ]
        INTO    #Missing
        FROM    #RFOAddress a
                FULL OUTER JOIN #HybrisAddress b ON a.RFOKey = b.HybrisKey
        WHERE   a.RFOKey IS NULL
                OR b.HybrisKey IS NULL
                   
					

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Missing'

        SELECT  @SourceCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
        SELECT  @TargetCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO Missing Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'No Missing from RFO and Hybris' ,
                      'PASSED'
                    )

   -- Loading Sample data
   
        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetCoulumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'



				

-- MixNMatch

        SET @message = ' STEP: 7.initiating MIXMATCH Validation '
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  a.RFOKey ,
                b.[HybrisKey] ,
                RFC ,
                HBC ,
                CASE WHEN a.RFC > b.HBC
                     THEN 'AccountBillingAddress Not Loaded in hybris'
                     WHEN a.RFC < b.HBC
                     THEN 'AccountBillingAddress Extra Loaded in hybris'
                END AS MixMatch
        INTO    #MixMatch
        FROM    ( SELECT    RFOKey ,
                            COUNT(*) AS [RFC]
                  FROM      #RFOAddress
                  GROUP BY  RFOKey
                ) a
                JOIN ( SELECT   HybrisKey ,
                                COUNT(*) AS [HBC]
                       FROM     #HybrisAddress
                       GROUP BY HybrisKey
                     ) b ON a.RFOKey = b.[HybrisKey]
        WHERE   a.RFC <> b.HBC

		

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'MixMatch'

        SELECT  @SourceCount = COUNT(*)
        FROM    #MixMatch
 
        SELECT  @TargetCount = COUNT(*)
        FROM    #MixMatch

	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO MixMatch Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'No MixMatch Counts' ,
                      'PASSED'
                    )




   
        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetCoulumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        @ValidationType , -- Flag - nvarchar(10)
                        RFOKey , -- SourceColumn - nvarchar(50)
                        HybrisKey , -- TargetCoulumn - nvarchar(50)
                        @key , -- Key - nvarchar(50)
                        RFC , -- SourceValue - nvarchar(50)
                        HBC
                FROM    #MixMatch





--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
	

        SET @message = ' STEP: 8.Removing Issues for END TO END Validaion  '
        EXECUTE dbqa.uspPrintMessage @message
		

        DELETE  a
        FROM    #RFO a
                JOIN #missing m ON m.RFOkey = a.RFOKey
		
        DELETE  a
        FROM    #RFO a
                JOIN #MixMatch m ON m.RFOkey = a.RFOKey


        DELETE  a
        FROM    #Hybris a
                JOIN #missing m ON m.HybrisKey = a.HybrisKey	
		

        DELETE  a
        FROM    #Hybris a
                JOIN #MixMatch m ON m.HybrisKey = a.HybrisKey	



        DROP TABLE #missing

        DROP TABLE #MixMatch
  -- Droping Table for performace

         
		 

        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp
			
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'AccountAddresses'
                AND [flag] IN ( 'c2c', 'ref', 'def' )


		
		
        DECLARE @MaxRow INT ,
            @RowNumber INT ,
            @rowCounts INT= 0 ,
            @TargetColumn NVARCHAR(50) ,
            @SourceColumn NVARCHAR(25) ,
            @flag NVARCHAR(50) ,
            @Stmt NVARCHAR(MAX);
        DECLARE @temp TABLE
            (
              [key] VARCHAR(50) ,--[Key]
              SourceValue VARCHAR(MAX) ,--[SourceColumn]
              TargetValue VARCHAR(MAX) --[TargetColumn] 
            );


        SELECT  @MaxRow = MAX(RowNumber)
        FROM    #Temp
        IF ISNULL(@MaxRow, 0) > 0
            BEGIN
        
                SET @Message = 'STEP: 9.Validation Started For Columnt To Column with  total fields= '
                    + CAST(@MaxRow AS NVARCHAR(20))              
                EXECUTE dbqa.uspPrintMessage @message



                SET @RowNumber = 1
                WHILE ( @MaxRow >= @RowNumber )
                    BEGIN
                        SELECT  @Owner = [owner] ,
                                @Flag = [flag] ,
                                @key = [Key] ,
                                @TargetColumn = TargetColumn ,
                                @SourceColumn = SourceColumn ,
                                @RowNumber = RowNumber ,
                                @Stmt = [SQL Stmt]
                        FROM    #Temp
                        WHERE   RowNumber = @RowNumber

                        SET @Message = CONCAT('Column Validation Started For ',
                                              CAST(@RowNumber AS NVARCHAR(20)),
                                              '. ', @TargetColumn)
                        EXECUTE dbqa.uspPrintMessage @message

                        INSERT  INTO @temp
                                ( [key], SourceValue,--[SourceColumn]
                                  TargetValue  --[TargetColumn]  
                                  )
                                EXEC sp_executesql @stmt 
                        SELECT  @rowCounts = COUNT([key])
                        FROM    @temp
						
                        IF @rowCounts <> 0
                            BEGIN
                                SET @Message = CONCAT('Total IssueCount=',
                                                      CAST(@rowCounts AS NVARCHAR(12)),
                                                      ' for ', @TargetColumn)


                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Owner , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'FAILED'
                                        )

                                INSERT  INTO dbqa.ErrorLog
                                        ( FlowTypes ,
                                          [Owner] ,
                                          Flag ,
                                          SourceColumn ,
                                          TargetCoulumn ,
                                          [Key] ,
                                          SourceValue ,
                                          TargetValue
                                        )
                                        SELECT TOP 10
                                                @Owner ,
                                                @Owner ,
                                                CONCAT(@Flag, '_EndToEnd') ,
                                                @SourceColumn ,
                                                @TargetColumn ,
                                                @Key ,
                                                SourceValue ,
                                                TargetValue
                                        FROM    @temp
                            END 
                        ELSE
                            BEGIN 
                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'PASSED'
                                        )
								

                            END 

                        DELETE  @temp
               -- PRINT @Stmt
                        SET @RowNumber = @RowNumber + 1

                    END 



            END
    END 




	GO
	
--*******************************************************************************************************
--					ACCOUNT SHIPPING ADDRESSES VALIDATIONS
--*******************************************************************************************************


USE DM_QA
GO 

CREATE PROCEDURE dbqa.uspAccountShippingAddress_BULK ( @LoadDate DATE )
AS
    BEGIN

        SET NOCOUNT ON;

        IF OBJECT_ID('TEMPDB.dbo. #RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#Hybris') IS NOT NULL
            DROP TABLE #Hybris;
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing  
        IF OBJECT_ID('tempdb.dbo.#MixMatch') IS NOT NULL
            DROP TABLE #MixMatch  

        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'Data Migration' ,
            @owner NVARCHAR(50)= 'Accounts_ShippingAddress' ,
            @Key NVARCHAR(25)= 'AccountNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts' ,
            @OrderDate DATE= DATEADD(MONTH, -18, @LoadDate) ,
            @HardTermDate DATE= DATEADD(MONTH, -6, @LoadDate)


        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  a.addressID ,
                ac.AccountId AS [RFOKey] ,
                co.Alpha2Code AS CountryID ,
                a.Region ,
                apm.PhoneNumber AS MainNumber ,
                adh.PhoneNumnber AS HomeNumber ,
                adc.PhoneNumnber AS CellNumber ,
                CASE WHEN g.Name IN ( 'male', 'female' ) THEN g.Name
                     ELSE NULL
                END AS GenderID , --	p_gender
                CASE WHEN a.AddressTypeID = 3 THEN 1
                     ELSE 0
                END AS IsBillingAddress , --	p_billingaddress
                CASE WHEN a.AddressTypeID = 1 THEN 1
                     ELSE 0
                END AS IsContactAddress , --	p_contactaddress
                CASE WHEN a.AddressTypeID = 2 THEN 1
                     ELSE 0
                END AS IsShippingAddress , --	p_shippingaddress
                a.Locale , --	p_town
                a.AddressLine1 , --	p_streetname
                a.AddressLine2 , --	p_streennumber
                ac.FirstName , --	p_firstname
                ac.LastName , --	p_lastname
                ac.MiddleName , --	p_MiddleName
                a.PostalCode , --	p_postalcode
                IIF(CAST(ac.BirthDay AS DATE) = '1900-01-01', NULL, ac.Birthday) AS BirthDay , --	p_dateofbirth
                IsDefault
        INTO    #RFO
        FROM    RFOperations.RFO_Accounts.Addresses a
                JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AddressID = a.AddressID
                                                              AND AddressTypeID = 2 -- Shipping AddressType
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = a.CountryID
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = aca.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
                LEFT JOIN ( SELECT  AccountcontactID ,
                                    ph.PhoneNumberRaw AS PhoneNumber
                            FROM    RFOperations.RFO_Accounts.AccountContactPhones aph
                                    JOIN RFOperations.RFO_Accounts.Phones ph ON ph.PhoneID = aph.PhoneId
                                                              AND PhoneTypeID = 1
                          ) apm ON apm.AccountContactId = ac.AccountContactId
                LEFT JOIN ( SELECT  adp.addressID ,
                                    p.PhoneNumberRaw AS PhoneNumnber
                            FROM    RFOperations.RFO_Accounts.AddressPhones adp
                                    JOIN RFOperations.RFO_Accounts.Phones p ON p.PhoneID = adp.PhoneId
                                                              AND p.PhoneTypeID = 2
                          ) adh ON adh.AddressId = a.AddressID
                LEFT JOIN ( SELECT  app.addressID ,
                                    p.PhoneNumberRaw AS PhoneNumnber
                            FROM    RFOperations.RFO_Accounts.AddressPhones app
                                    JOIN RFOperations.RFO_Accounts.Phones p ON p.PhoneID = app.PhoneId
                                                              AND p.PhoneTypeID = 3
                          ) adc ON adc.AddressId = a.AddressID
        WHERE   EXISTS ( SELECT 1
                         FROM   Hybris.dbo.users u
                         WHERE  u.P_accountID = ac.accountId )
	--OR 
          /*     AND EXISTS ( SELECT 1
		   FROM      RFOperations.RFO_Accounts.AccountBase ab
                                                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID --AND ab.AccountNumber='001762'
                                                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
                                                JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
                                                LEFT JOIN RFOperations.RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId
                                                LEFT JOIN RFOperations.RFO_Accounts.EmailAddresses ea ON ea.EmailAddressID = ae.EmailAddressId
                                       WHERE  ab.accountID = ac.AcccountID
                            AND ea.EmailAddressTypeID = 1
                            AND ab.AccountNumber <> 'AnonymousRetailAccount'
                            AND ( ab.AccountStatusID IN ( 1, 2, 8, 9 ) -- Active,Inactive,SoftVoluntary,SoftInvoluntary
                                  OR ( ab.AccountStatusID = 10
                                       AND EXISTS ( SELECT  1
                                                    FROM    RFOperations.Hybris.orders ro
                                                    WHERE   ro.AccountID = ab.AccountID
                                                            AND CAST(ro.CompletionDate AS DATE) >= @HardTermDate ) --HardTerm with 6 months order
                                     )
                                  OR ( ab.AccountStatusID IN ( 3, 4, 5, 6, 7 )
                                       AND EXISTS ( SELECT  1
                                                    FROM    RFOperations.Hybris.orders ro
                                                    WHERE   ro.AccountID = ab.AccountID
                                                            AND CAST(ro.CompletionDate AS DATE) >= @OrderDate )--Other Status with 18 months orders
                                     )
                                ) )
								*/


			
        SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
        EXECUTE dbqa.uspPrintMessage @message
	



        SELECT  u.p_accountnumber [HybrisKey] ,
                co.p_isocode AS p_counrty , --	CountryID
                r.p_isocodeshort AS p_region , --	region
                p_phone1 , --	MainNumber
                p_phone2 , --	HomeNumber
                p_cellphone , --	CellNumber
                g.Code AS p_gender , --	GenderID
                p_billingaddress , --	IsBillingAddress
                p_contactaddress , --	IsContactAddress
                p_shippingaddress , --	IsShippingID
                p_town , --	Locale
                p_streetname , --	AddressLine1
                p_streetnumber , --	AddressLine2
                p_firstname , --	FirstName
                p_lastname , --	LastName
                p_MiddleName , --	MiddleName
                p_postalcode , --	PostalCode
                a.p_dateofbirth --	BirthDay    
        INTO    #Hybris
        FROM    Hybris.dbo.addresses a
                JOIN Hybris.dbo.users u ON u.pk = a.OwnerPkString
                                           AND a.p_duplicate = 0
                                           AND a.p_shippingaddress = 1
                JOIN Hybris.dbo.countries co ON co.pk = a.p_country
                JOIN Hybris.dbo.regions r ON r.pk = a.p_region
                LEFT JOIN Hybris.dbo.enumerationvalues g ON g.pk = a.p_gender


			

			
					
        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message       


        CREATE CLUSTERED INDEX cls_RFO ON #RFOAddress([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #HybrisAddress([HybrisKey])
		
	-- Count Validaiton 

        SET @message = ' STEP: 4.initiating COUNT Validation  '
        EXECUTE dbqa.uspPrintMessage @message



        SELECT  @SourceCount = COUNT(RFOKey)
        FROM    #RFOAddress
          
        SELECT  @TargetCount = COUNT(HybrisKey)
        FROM    #HybrisAddress
           

        INSERT  INTO dbqa.SourceTargetLog
                ( FlowTypes ,
                  ValidationTypes ,
                  [Owner] ,
                  SourceCount ,
                  TargetCount ,
                  Comments ,
                  ExecutionStatus 
                )
        VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                  @ValidationType , -- ValidationTypes - nvarchar(50)
                  @Owner , -- Owner - nvarchar(50)
                  @SourceCount , -- SourceCount - int
                  @TargetCount , -- TargetCounts - int
                  CASE WHEN @SourceCount > @TargetCount
                       THEN CONCAT('RFO Count More than HYBRIS BY ',
                                   CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
                       WHEN @SourceCount < @TargetCount
                       THEN CONCAT('HYBRIS Count More than RFO Count By ',
                                   CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
                       ELSE 'RFO and HYBRIS Counts are equal'
                  END ,
                  CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
                       ELSE 'FAILED'
                  END
                )
		
		

--++++++++++++++++++--++++++++++++++++++
--  RFO  DUPLICATE VALIDATION
--++++++++++++++++++--++++++++++++++++++
        SET @message = ' STEP: 5.initiating DUPLICATE Need to updaete Scripts   '
        EXECUTE dbqa.uspPrintMessage @message


--SET @SourceCount = 0
--SET @TargetCount = 0
--SET @ValidationType = 'Duplicate'


--SELECT  @SourceCount = COUNT(t.Ct)
--FROM    ( SELECT    COUNT([RFOKey]) Ct
--          FROM      #RFO
--          GROUP BY  [RFOKey] ,
--                    PaymentProfileID
--          HAVING    COUNT([RFOKey]) > 1
--        ) t



--IF ISNULL(@SourceCount, 0) > 0
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              SourceCount ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              @SourceCount , -- SourceCount - int         
--              'RFO has Duplicate PaymentInfo' ,
--              'FAILED'
--            )
--ELSE
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              'RFO has NO Duplicate PaymentInfos' ,
--              'PASSED'
--            )


--SELECT  @TargetCount = COUNT(t.Ct)
--FROM    ( SELECT    COUNT([HybrisKey]) Ct
--          FROM      #Hybris
--          GROUP BY  [HybrisKey] ,
--                    paymentInfoPK
--          HAVING    COUNT([HybrisKey]) > 1
--        ) t



--IF ISNULL(@TargetCount, 0) > 0
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              TargetCount ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              @TargetCount , -- SourceCount - int         
--              'HYBRIS has Duplicate PaymentInfo' ,
--              'FAILED'
--            )

	




--ELSE
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              'HYBRIS has NO Duplicate PaymentInfos' ,
--              'PASSED'
--            )

--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++


        SET @message = ' STEP: 6.initiating MISSING Validation now '
        EXECUTE dbqa.uspPrintMessage @message
		

		 
    
        SELECT  a.RFOKey ,
                b.HybrisKey ,
                CASE WHEN a.RFOkey IS NULL
                     THEN 'MissingInRFO-LoadedExtraInHybris'
                     WHEN b.HybrisKey IS NULL
                     THEN 'MissingInHybris-NotLoadedInHybris'
                END AS [Missing From ]
        INTO    #Missing
        FROM    #RFOAddress a
                FULL OUTER JOIN #HybrisAddress b ON a.RFOKey = b.HybrisKey
        WHERE   a.RFOKey IS NULL
                OR b.HybrisKey IS NULL
                   
					

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Missing'

        SELECT  @SourceCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
        SELECT  @TargetCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO Missing Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'No Missing from RFO and Hybris' ,
                      'PASSED'
                    )

   -- Loading Sample data
   
        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetCoulumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'



				

-- MixNMatch

        SET @message = ' STEP: 7.initiating MIXMATCH Validation '
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  a.RFOKey ,
                b.[HybrisKey] ,
                RFC ,
                HBC ,
                CASE WHEN a.RFC > b.HBC
                     THEN 'AccShippingAddress Not Loaded in hybris'
                     WHEN a.RFC < b.HBC
                     THEN 'AccShippingAddress Extra Loaded in hybris'
                END AS MixMatch
        INTO    #MixMatch
        FROM    ( SELECT    RFOKey ,
                            COUNT(*) AS [RFC]
                  FROM      #RFOAddress
                  GROUP BY  RFOKey
                ) a
                JOIN ( SELECT   HybrisKey ,
                                COUNT(*) AS [HBC]
                       FROM     #HybrisAddress
                       GROUP BY HybrisKey
                     ) b ON a.RFOKey = b.[HybrisKey]
        WHERE   a.RFC <> b.HBC

		

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'MixMatch'

        SELECT  @SourceCount = COUNT(*)
        FROM    #MixMatch
 
        SELECT  @TargetCount = COUNT(*)
        FROM    #MixMatch

	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO MixMatch Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'No MixMatch Counts' ,
                      'PASSED'
                    )




   
        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetCoulumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        @ValidationType , -- Flag - nvarchar(10)
                        RFOKey , -- SourceColumn - nvarchar(50)
                        HybrisKey , -- TargetCoulumn - nvarchar(50)
                        @key , -- Key - nvarchar(50)
                        RFC , -- SourceValue - nvarchar(50)
                        HBC
                FROM    #MixMatch





--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
	


        SET @message = ' STEP: 8.Removing Issues for END TO END Validaion  '
        EXECUTE dbqa.uspPrintMessage @message

        DELETE  a
        FROM    #RFO a
                JOIN #missing m ON m.RFOkey = a.RFOKey
		
        DELETE  a
        FROM    #RFO a
                JOIN #MixMatch m ON m.RFOkey = a.RFOKey


        DELETE  a
        FROM    #Hybris a
                JOIN #missing m ON m.HybrisKey = a.HybrisKey	
		

        DELETE  a
        FROM    #Hybris a
                JOIN #MixMatch m ON m.HybrisKey = a.HybrisKey	



        DROP TABLE #missing

        DROP TABLE #MixMatch




        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp
			
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'AccountAddresses'
                AND [flag] IN ( 'c2c', 'ref', 'def' )


		

		
        DECLARE @MaxRow INT ,
            @RowNumber INT ,
            @rowCounts INT= 0 ,
            @TargetColumn NVARCHAR(50) ,
            @SourceColumn NVARCHAR(25) ,
            @flag NVARCHAR(50) ,
            @Stmt NVARCHAR(MAX);
        DECLARE @temp TABLE
            (
              [key] VARCHAR(50) ,--[Key]
              SourceValue VARCHAR(MAX) ,--[SourceColumn]
              TargetValue VARCHAR(MAX) --[TargetColumn] 
            );



        SELECT  @MaxRow = MAX(RowNumber)
        FROM    #Temp
        IF ISNULL(@MaxRow, 0) > 0
            BEGIN
                SET @Message = 'STEP: 9.Validation Started For Columnt To Column with  total fields= '
                    + CAST(@MaxRow AS NVARCHAR(20))              
                EXECUTE dbqa.uspPrintMessage @message


                SET @RowNumber = 1
                WHILE ( @MaxRow >= @RowNumber )
                    BEGIN
                        SELECT  @Owner = [owner] ,
                                @Flag = [flag] ,
                                @key = [Key] ,
                                @TargetColumn = TargetColumn ,
                                @SourceColumn = SourceColumn ,
                                @RowNumber = RowNumber ,
                                @Stmt = [SQL Stmt]
                        FROM    #Temp
                        WHERE   RowNumber = @RowNumber

                        SET @Message = CONCAT('Column Validation Started For ',
                                              CAST(@RowNumber AS NVARCHAR(20)),
                                              '. ', @TargetColumn)
                        EXECUTE dbqa.uspPrintMessage @message

                        INSERT  INTO @temp
                                ( [key], SourceValue,--[SourceColumn]
                                  TargetValue  --[TargetColumn]  
                                  )
                                EXEC sp_executesql @stmt 
                        SELECT  @rowCounts = COUNT([key])
                        FROM    @temp
						
                        IF @rowCounts <> 0
                            BEGIN
                                SET @Message = CONCAT('Total IssueCount=',
                                                      CAST(@rowCounts AS NVARCHAR(12)),
                                                      ' for ', @TargetColumn)


                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Owner , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'FAILED'
                                        )

                                INSERT  INTO dbqa.ErrorLog
                                        ( FlowTypes ,
                                          [Owner] ,
                                          Flag ,
                                          SourceColumn ,
                                          TargetCoulumn ,
                                          [Key] ,
                                          SourceValue ,
                                          TargetValue
                                        )
                                        SELECT TOP 10
                                                @Owner ,
                                                @Owner ,
                                                CONCAT(@Flag, '_EndToEnd') ,
                                                @SourceColumn ,
                                                @TargetColumn ,
                                                @Key ,
                                                SourceValue ,
                                                TargetValue
                                        FROM    @temp
                            END 
                        ELSE
                            BEGIN 
                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'PASSED'
                                        )
								

                            END 

                        DELETE  @temp
               -- PRINT @Stmt
                        SET @RowNumber = @RowNumber + 1

                    END 



            END
    END 


	GO
    





	
CREATE  PROCEDURE dbqa.uspAccount_Master_BULK_Validation (@LoadDate DATE)
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message='AccountHeader Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspAccountsHeader_BULK @LoadDate

SET @message='AccountPaymentProfile Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAccountPaymentProfile_BULK @LoadDate


SET @message='AccountContactAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAccountContractAddress_BULK


 SET @message='AccountBillingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAccountBillingAddress_BULK @LoadDate



SET @message='AccountShippingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAccountShippingAddress_BULK @LoadDate


END 




GO 
