

USE DM_QA
GO 
CREATE PROCEDURE dbqa.upsAccountHeader_Delta
    (
      @LoadDate DATE ,
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
    BEGIN





        IF OBJECT_ID('TEMPDB.dbo.#RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#Hybris') IS NOT NULL
            DROP TABLE #Hybris;
        IF OBJECT_ID('TEMPDB.dbo.#DPaymentInfo') IS NOT NULL
            DROP TABLE #DPaymentInfo;
        IF OBJECT_ID('TEMPDB.dbo.#DShipping') IS NOT NULL
            DROP TABLE #DShipping;
        IF OBJECT_ID('TEMPDB.dbo.#Sponsered') IS NOT NULL
            DROP TABLE #Sponsered;		
        IF OBJECT_ID('TEMPDB.dbo.#missing') IS NOT NULL
            DROP TABLE #missing;

			-- USA_CAN Total Count Validation



	---++++++++++++++++++++++++++++++++++++++++++++++++
	--			DIRECT KEY FIELD APPROACH TO VALIDATE 
	---++++++++++++++++++++++++++++++++++++++++++++++++


        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Delta NVARCHAR(50) ,
            @Flows NVARCHAR(50) ,
            @HardTermDate DATE ,
            @OrdersDate DATETIME ,
            @owner NVARCHAR(50)= 'Accounts' ,
            @Key NVARCHAR(25)= 'AccountNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts'	

        SET @EndDate = ISNULL(@EndDate, GETDATE())  
        SET @Delta = CONCAT(FORMAT(@startDate, 'MMdd'), '-> ',
                            FORMAT(@EndDate, 'MMdd'))
        SET @Flows = CONCAT('DM Delta_', @Delta)
        SET @HardTermDate = DATEADD(MONTH, -6, @LoadDate)
        SET @OrdersDate = DATEADD(MONTH, -18, @LoadDate)
	

        SET NOCOUNT ON;

        SET @message = 'STEP: 1. RFO Accounts Loading.'
        EXECUTE dbqa.uspPrintMessage @message

	---+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	--			TEMP WITH ALL FIELDS APPROACH TO VALIDATE 
	---+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
        SELECT  a.AccountID ,
                a.PaymentProfileID ,
                ad.AddressID ,
                TokenID
        INTO    #DPaymentInfo
        FROM    RFOperations.RFO_Accounts.PaymentProfiles a --JOIN RodanFieldsLive.dbo.AccountPaymentMethods apm ON a.PaymentProfileID = apm.AccountPaymentMethodID
                JOIN RFoperations.RFO_accounts.CreditCardProfiles ccp ON ccp.PaymentProfileID = a.PaymentProfileID
                JOIN RFOperations.RFO_Accounts.Addresses Ad ON ccp.BillingAddressID = ad.AddressID
        WHERE   a.IsDefault = 1
                AND ad.IsDefault = 1 

         
        SELECT  AC.AccountId ,
                MAX(A.AddressID) AS AddressID
        INTO    #DShipping
        FROM    RFOperations.RFO_Accounts.Addresses A
                JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.AddressID = A.AddressID
                JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountContactId = ACA.AccountContactId
        WHERE   AddressTypeID = 2
                AND IsDefault = 1
        GROUP BY ac.AccountID  
		    
        SELECT  ab.AccountID ,
                BridgeTable.SponsorId [HybrisPK]
        INTO    #Sponsered
        FROM    RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.BridgeTable BridgeTable ON BridgeTable.AccountID = ab.AccountID          




        SELECT  IIF(CAST(ac.Birthday AS DATE) = '1900-01-01', NULL, ac.Birthday) AS Birthday ,--	p_dateofbirth
                rf.HardTerminationDate ,--	p_hardterminateddate
                rf.SoftTerminationDate ,--	p_softterminateddate
                ab.AccountNumber ,
                ac.SecuredTaxNumber AS TaxNumber ,--	p_accountnumber
                g.Name AS GenderID ,--	p_gender
                rf.IsTaxExempt ,--	p_taxexempt
                rf.IsBusinessEntity ,--	p_businessentity
                ea.EmailAddress ,--	p_email
                co.Alpha2Code AS CountryID ,--	p_country
                ab.AccountID AS [RFOKey] ,--	p_customerid
                sp.[HybrisPK] AS SponsorID ,--	p_newsponsorid
                s.Name AS AccountStatusID ,--	p_status
                t.Name AS AccountTypes ,--	p_type 
                CASE WHEN CHARINDEX(' ', LTRIM(RTRIM(rf.CoApplicant))) > 1
                     THEN SUBSTRING(LTRIM(RTRIM(rf.CoApplicant)), 1,
                                    CHARINDEX(' ',
                                              LTRIM(RTRIM(rf.CoApplicant))))
                     ELSE LTRIM(RTRIM(rf.CoApplicant))
                END CoAppFistName ,--	p_spousefirstname
                CASE WHEN CHARINDEX(' ', LTRIM(RTRIM(rf.CoApplicant))) > 1
                     THEN SUBSTRING(LTRIM(RTRIM(rf.CoApplicant)),
                                    CHARINDEX(' ',
                                              LTRIM(RTRIM(rf.CoApplicant))),
                                    ( LEN(RTRIM(LTRIM(rf.CoApplicant)))
                                      - CHARINDEX(' ',
                                                  LTRIM(RTRIM(rf.CoApplicant))) ))
                     ELSE NULL
                END CoAppLastName ,--	p_spouselastname
                rf.NextRenewalDate ,--	p_nextrenewaldate
                rf.LastRenewalDate ,--	p_lastrenewaldate
                rf.EnrollmentDate ,--	p_enrolementdate
                CONCAT(ac.FirstName, SPACE(1), ac.LastName) Name ,--	p_name
                accs.UserName ,--	p_uid
                accs.[password] ,--	PassWord
                p.PaymentProfileID ,--	p_defaultpaymentinfo
                -ds.AddressID AS ShippingAddressId ,--	p_defaultshipmentaddress
                p.AddressID AS BillingAddressID ,--	P_defaultpaymentAddress
                cu.Name AS CurrencyID ,--	p_sessioncurrency       
                nl.ISOCode AS LanguageID ,--	p_sessionlanguage
                rf.Active ,--	p_active
                ISNULL(Pulse.IsPulseSubscrption, 0) IsPulseSubscrption ,--	p_pulsesubscription
                ISNULL(PC.IsPCActive, 0) AS IsPCActive ,--	p_pcperksactive
                p.TokenID ,--	p_token
                'customer' AS Typepkstring
        INTO    #RFO
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
        WHERE   ea.EmailAddressTypeID = 1
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
                                                AND CAST(ro.CompletionDate AS DATE) >= @OrdersDate )--Other Status with 18 months orders
                         )
                    )
                AND ( ( ab.ServerModifiedDate BETWEEN @StartDate
                                              AND     @EndDate )
                      OR ( rf.ServerModifiedDate BETWEEN @StartDate
                                                 AND     @EndDate )
                      OR ( ac.ServerModifiedDate BETWEEN @StartDate
                                                 AND     @EndDate )
                    )


        SET @message = 'STEP: 2. HYBRIS Accounts Loading.'
        EXECUTE dbqa.uspPrintMessage @message
	


        SELECT  p_dateofbirth ,--	Birthday
                p_hardterminateddate ,--	HardTerminationDate
                p_softterminateddate ,--	SoftTerminationDate
                p_accountnumber ,--	AccountNumber
                g.Code AS p_gender ,--	GenderID
                p_taxexempt ,--	IsTaxExempt
                p_businessentity ,--	IsBusinessEntity
                p_email ,--	EmailAddress
                co.p_isocode AS p_country ,--	CountryID
                p_customerid AS [HybrisKey] ,--	AccountID
                p_newsponsorid ,--	SponsorID
--p_status	,--	AccountStatusID
                tp.Code AS p_type ,--	AccountTypes
                p_spousefirstname ,--	CoAppFistName
                p_spouselastname ,--	CoAppLastName
                p_renewaldate AS p_nextrenewaldate ,--	NextRenewalDate
--p_lastrenewaldate	,--	LastRenewalDate
--p_enrolementdate	,--	EnrollmentDate
                p_name ,--	Name
                p_uid ,--	UserName
                passwd ,--	password
                p_defaultpaymentinfo ,--	PaymentProfileID
                p_defaultshipmentaddress ,--	ShippingAddressId
                P_defaultpaymentAddress ,--	BillingAddressID
                cu.p_isocode AS p_sessioncurrency ,--	CurrencyID
                la.p_isocode AS p_sessionlanguage ,--	LanguageID
                u.p_active ,--	Active
                p_pulsesubscription ,--	IsPulseSubscrption
                p_pcperksactive ,--	IsPCActive
                p_token ,--	TokenID
                t.InternalCode AS Typepkstring	--	
        INTO    #Hybris
        FROM    Hybris.dbo.Users u
                JOIN Hybris.dbo.countries co ON co.pk = u.p_country
                JOIN Hybris.dbo.composedtypes t ON t.pk = u.TypePkString
                LEFT JOIN Hybris.dbo.enumerationvalues g ON g.pk = u.p_gender
                LEFT JOIN Hybris.dbo.enumerationvalues tp ON tp.pk = u.p_type
                LEFT JOIN Hybris.dbo.currencies cu ON cu.pk = u.p_sessioncurrency
                LEFT JOIN Hybris.dbo.languages la ON la.pk = u.p_sessionlanguage
        WHERE   u.modifiedTS BETWEEN @StartDate AND @EndDate

        SET @message = 'STEP: 3. Index Creating RFO N HYBRIS.'
        EXECUTE dbqa.uspPrintMessage @message


	
	
        CREATE CLUSTERED INDEX cls_RFO ON  #RFO([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #Hybris([HybrisKey])



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
                  CONCAT('All_', @owner) , -- Owner - nvarchar(50)
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
		

        SET @message = 'STEP: 4. Counts ALL_Accounts Completed.'
        EXECUTE dbqa.uspPrintMessage @message


--+++++++++++++++++++++++++++++++++++++++++
-- USA Accounts Total Counts Validation
--+++++++++++++++++++++++++++++++++++++++++
        SET @sourceCount = 0
        SET @TargetCount = 0

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
		
        

        SET @message = 'STEP: 5. Counts US_Accounts Validated.'
        EXECUTE dbqa.uspPrintMessage @message

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
        VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                  @ValidationType , -- ValidationTypes - nvarchar(50)
                  CONCAT('CA_', @owner) , -- Owner - nvarchar(50)
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




        SET @message = 'STEP: 6. Counts CA_Accounts Validated.'
        EXECUTE dbqa.uspPrintMessage @message


----++++++++++++++++++--++++++++++++++++++
---- AUS Accounts  Total Count Validation 
----++++++++++++++++++--++++++++++++++++++

        SELECT  @SourceCount = COUNT([RFOKey])
        FROM    #RFO
        WHERE   CountryID = 'AU'
     


        SELECT  @TargetCount = COUNT([HybrisKey])
        FROM    #Hybris
        WHERE   p_country = 'AU'

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
        VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                  @ValidationType , -- ValidationTypes - nvarchar(50)
                  CONCAT('AU_', @owner) , -- Owner - nvarchar(50)
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




        SET @message = 'STEP: 7. Counts AU_Accounts Validated.'
        EXECUTE dbqa.uspPrintMessage @message


-- DUPLICATION VALIDATION  IN SOURCE AND TARGET/BRIDGETABLE VALIDATION 
--++++++++++++++++++--++++++++++++++++++
--  RFO Accounts Duplicate Vaidation.
--++++++++++++++++++--++++++++++++++++++
        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Duplicate'

        SELECT  @SourceCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([RFOKey]) Ct
                  FROM      #RFO
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
                      CONCAT(' RFO_', @owner) , -- Owner - nvarchar(50)
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
            VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      CONCAT(' RFO_', @owner) , -- Owner - nvarchar(50) 
                      'RFO has NO Duplicate AccountNumbers' ,
                      'PASSED'
                    )

	
        SET @message = 'STEP: 8. Duplicate RFO_Accounts Validated .'
        EXECUTE dbqa.uspPrintMessage @message	



--++++++++++++++++++--+++++++++++++++++++++
-- HYBRIS ACCOUNTS DUPLICATE VAIDATION.
--++++++++++++++++++--+++++++++++++++++++++

   

        SELECT  @TargetCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([HybrisKey]) Ct
                  FROM      #Hybris
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
                      CONCAT('Hybris_', @owner) , -- Owner - nvarchar(50)
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
                      CONCAT('Hybris_', @owner) , -- Owner - nvarchar(50)
                      'HYBRIS has NO Duplicate AccountNumbers' ,
                      'PASSED'
                    )

        SET @message = 'STEP: 9. Duplicate Hybris_Accounts Validated .'
        EXECUTE dbqa.uspPrintMessage @message	




--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++

--US ACCOUNTS SCTOTG  MISSING VALIDATION 
        SELECT  a.[RFOKey] ,
                b.[HybrisKey] ,
                COALESCE(a.CountryID, b.p_country) AS Country ,
                CASE WHEN a.[RFOKey] IS NULL
                     THEN 'MissingInRFO-LoadedExtraInHybris'
                     WHEN b.[HybrisKey] IS NULL
                     THEN 'MissingInHybris-NotLoadedInHybris'
                END AS [Missing From ]
        INTO    #Missing
        FROM    #RFO a
                FULL OUTER JOIN #Hybris b ON a.[RFOKey] = b.[HybrisKey]
        WHERE   a.[RFOKey] IS NULL
                OR b.[HybrisKey] IS NULL

--CREATE NONCLUSTERED INDEX cla_m ON #Missing(HybrisKey,RFOKey)

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


        SET @message = 'STEP: 10. Missing US_Accounts Validated .'
        EXECUTE dbqa.uspPrintMessage @message	
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

        SET @message = 'STEP: 11. Missing CA_Accounts Validated .'
        EXECUTE dbqa.uspPrintMessage @message	
					
--AUS ACCOUNTS SCTOTG  MISSING VALIDATION 

        SET @SourceCount = 0
        SET @TargetCount = 0

        SELECT  @SourceCount = COUNT(*)
        FROM    #Missing
        WHERE   Country = 'AU'
                AND [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
        SELECT  @TargetCount = COUNT(*)
        FROM    #Missing
        WHERE   Country = 'AU'
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
                      CONCAT('AU_', @owner) , -- Owner - nvarchar(50)
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
                      CONCAT('AU_', @owner) , -- Owner - nvarchar(50)
                      'No Missing from RFO and Hybris' ,
                      'PASSED'
                    )

        SET @message = 'STEP: 12. Missing AUS_Accounts Validated .'
        EXECUTE dbqa.uspPrintMessage @message	

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
                        @key , -- Key - nvarchar(50)
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
                        @key , -- Key - nvarchar(50)
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
                        @key , -- Key - nvarchar(50)
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
                        @key , -- Key - nvarchar(50)
                        [RFOKey] , -- SourceValue - nvarchar(50)
                        [HybrisKey]
                FROM    #Missing
                WHERE   Country = 'CA'
                        AND [Missing From ] = 'MissingInHybris-NotLoadedInHybris'


        SET @message = 'STEP: 13. Missing Accounts Sample Logged  .'
        EXECUTE dbqa.uspPrintMessage @message	







--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
		
      	
        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp


        SET @message = 'STEP: 14. COUNT Missing  completed and Removing Issues from  #RFO and #Hybris  '
        EXECUTE dbqa.uspPrintMessage @message


        DELETE  a
        FROM    #RFO a
                JOIN #missing m ON m.RFOkey = a.RFOKey


        DELETE  a
        FROM    #Hybris a
                JOIN #missing m ON m.HybrisKey = a.HybrisKey	



        DROP TABLE #missing
  -- Droping Table for performace



			
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
                SET @Message = 'STEP: 15.Validation Started For Columnt To Column with  total fields= '
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
-- Print Message no wait.

--Loading Issues Records in Table Variables

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
