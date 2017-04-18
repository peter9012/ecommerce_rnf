
SELECT TOP 10 * FROM Commissions.etl.Accounts

USE [DM_QA]
GO

/****** Object:  StoredProcedure [dbqa].[uspAccountsHeader_BULK]    Script Date: 2/9/2017 4:44:47 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbqa].[uspComm_Accounts] ( @LoadDate DATE )
AS
    BEGIN 
        SET NOCOUNT ON;


        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'DM_Hybris_Comm' ,
            @owner NVARCHAR(50)= 'Comm_Accounts' ,
            @Key NVARCHAR(25)= 'AccountID' ,
            @ValidationType NVARCHAR(50)= 'Counts' ,
            @OrderDate DATE = DATEADD(MONTH, -18, @LoadDate) ,
            @HardTermDate DATE= DATEADD(MONTH, -6, @LoadDate)




        IF OBJECT_ID('TEMPDB.dbo.#Source') IS NOT NULL
            DROP TABLE  #Source;
        IF OBJECT_ID('TEMPDB.dbo.#TargetAccounts') IS NOT NULL
            DROP TABLE #Target;        
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing  
	

        SET @message = CONCAT(' STEP:1. Source Table Started to Load.',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


		  SELECT  p_dateofbirth ,--	Birthday
                p_hardterminateddate ,--	HardTerminationDate
                p_softterminateddate ,--	SoftTerminationDate
                p_accountnumber ,--	AccountNumber
                p_countrysecuritycode ,--TaxNumber
                g.Code AS p_gender ,--	GenderID
                p_taxexempt ,--	IsTaxExempt
                p_businessentity ,--	IsBusinessEntity
                p_email ,--	EmailAddress
                co.p_isocode AS p_country ,--	CountryID
                p_customerid AS [TargetKey] ,--	AccountID
                p_newsponsorid ,--	SponsorID
                s.Code AS p_accountstatus ,--	AccountStatusID
                tp.Code AS p_type ,--	AccountTypes
                p_spousefirstname ,--	CoAppFistName
                p_spouselastname ,--	CoAppLastName
                p_nextrenewaldate ,--	NextRenewalDate
                p_lastrenewaldate ,--	LastRenewalDate
                u.createdTS ,--	EnrollmentDate
                p_name ,--	Name
                p_uid ,--	UserName
                CAST(passwd AS NVARCHAR(4000)) passwd ,--	password
                p_defaultpaymentinfo ,--	PaymentProfileID
                p_defaultshipmentaddress ,--	ShippingAddressId
                P_defaultpaymentAddress ,--	BillingAddressID
                cu.p_isocode AS p_sessioncurrency ,--	CurrencyID
                la.p_isocode AS p_sessionlanguage ,--	LanguageID
                u.p_active ,--	Active
                p_pulsesubscription ,--	IsPulseSubscrption
                p_pcperksactive ,--	IsPCActive
               -- p_token ,--	TokenID
                t.InternalCode AS Typepkstring	--	
        INTO    #Target	
        FROM    Hybris.dbo.Users u
		LEFT JOIN Hybris.dbo.users sp ON sp.pk=u.p_newsponsorid
               LEFT JOIN Hybris.dbo.countries co ON co.pk = u.p_country
                LEFT JOIN Hybris.dbo.composedtypes t ON t.pk = u.TypePkString
                LEFT JOIN Hybris.dbo.enumerationvalues s ON s.pk = u.p_accountstatus
                LEFT JOIN Hybris.dbo.enumerationvalues g ON g.pk = u.p_gender
                LEFT JOIN Hybris.dbo.enumerationvalues tp ON tp.pk = u.p_type
                LEFT JOIN Hybris.dbo.currencies cu ON cu.pk = u.p_sessioncurrency
                LEFT JOIN Hybris.dbo.languages la ON la.pk = u.p_sessionlanguage
        WHERE   NOT EXISTS ( SELECT 1
                             FROM   DM_QA.dbo.AccountIssue ai
                             WHERE  CAST(ai.AccountID AS NVARCHAR(225)) = u.p_customerid ) -- ADDED Excluding Email Sharing  Accounts.
							 AND tp.Code IN ('PREFERRED','RETAIL','CONSULTANT')
	

        SELECT  IIF(CAST(ac.Birthday AS DATE) = '1900-01-01', NULL, ac.Birthday) AS Birthday ,--	p_dateofbirth
                rf.HardTerminationDate ,--	p_hardterminateddate
                rf.SoftTerminationDate ,--	p_softterminateddate
                ac.SecuredTaxNumber AS TaxNumber ,--	p_countrysecuritycode,
                ab.AccountNumber ,--p_accountnumber
                g.Name AS GenderID ,--	p_gender
                rf.IsTaxExempt ,--	p_taxexempt
                ISNULL(rf.IsBusinessEntity, 0) IsBusinessEntity ,--	p_businessentity
                ea.EmailAddress ,--	p_email
                co.Alpha2Code AS CountryID ,--	p_country
                CAST(ab.AccountID AS NVARCHAR(255)) AS [SourceKey] ,--	p_customerid
                sp.pk AS SponsorID ,--	p_newsponsorid
                CASE WHEN s.Name = 'Hard Terminated' THEN 'HARDTERMINATED'
                     ELSE s.Name
                END AS AccountStatusID ,--	p_status
                CASE WHEN t.Name = 'Retail Customer' THEN 'RETAIL'
                     WHEN t.Name = 'Preferred Customer' THEN 'PREFERRED'
                     ELSE t.Name
                END AS AccountTypes ,--	p_type 
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
                                      - ( CHARINDEX(' ',
                                                    LTRIM(RTRIM(rf.CoApplicant)))
                                          - 1 ) ))
                     ELSE NULL
                END CoAppLastName ,--	p_spouselastname
                rf.NextRenewalDate ,--	p_nextrenewaldate
                rf.LastRenewalDate ,--	p_lastrenewaldate
                rf.EnrollmentDate ,--	p_enrolementdate
                CONCAT(ac.FirstName, SPACE(1), ac.LastName) Name ,--	p_name
                CONCAT('test_', accs.UserName) UserName ,--	p_uid
                accs.[password] ,--	PassWord
                p.PaymentProfileID ,--	p_defaultpaymentinfo
                ds.AddressID AS ShippingAddressId ,--	p_defaultshipmentaddress
                p.AddressID AS BillingAddressID ,--	P_defaultpaymentAddress
                cu.Code AS CurrencyID ,--	p_sessioncurrency       
                nl.ISOCode AS LanguageID ,--	p_sessionlanguage
                rf.Active ,--	p_active
                Pulse.IsPulseSubscrption ,--	p_pulsesubscription
                PC.IsPCActive ,--	p_pcperksactive
               -- ISNULL(p.TokenID, '') TokenID ,--	p_token
                'customer' AS Typepkstring
        INTO    #Source
    --SELECT  ab.AccountNumber , COUNT(AccountNumber)
	--SELECT top 10 *
        FROM    RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID --AND ab.AccountNumber='001762'
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
                JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
                JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = ab.AccountTypeID
                JOIN RFOperations.RFO_Reference.NativeLanguage nl ON nl.LanguageID = ab.LanguageId
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ab.CountryID
                JOIN RFOperations.RFO_Reference.Currency cu ON cu.CurrencyID = ab.CurrencyID
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
                LEFT JOIN RFOperations.[Security].AccountSecurity accs ON accs.AccountID = ab.AccountID
                LEFT JOIN RFOperations.RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
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
                                                AND CAST(ro.CompletionDate AS DATE) >= @OrderDate )--Other Status with 18 months orders
                         )
                    )
                AND NOT EXISTS ( SELECT 1
                                 FROM   DM_QA.dbo.AccountIssue ai
                                 WHERE  ai.AccountID = ab.AccountID ) -- ADDED Excluding Email Sharing  Accounts.

				
    --GROUP BY ab.AccountNumber
    --HAVING  COUNT(AccountNumber) > 1


        SET @message = CONCAT(' STEP: 2.Target Table Started to Load',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message



	


      

	





	
        SET @message = CONCAT('STEP: 3. Index Creating Source N Target.',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message
      

/* Source to Target Total Counts */


--++++++++++++++++++++++++++++++++++++
-- USA_CAN Total Count Validation
--++++++++++++++++++++++++++++++++++++

      



        CREATE CLUSTERED INDEX cls_RFO ON  #Source([SourceKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #Target([TargetKey])


        SET @ValidationType = 'Count'
        SET @message = CONCAT(' STEP: 4 Count Validations Started. ', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  @SourceCount = COUNT(DISTINCT [SourceKey])
        FROM    #Source
        SELECT  @TargetCount = COUNT([TargetKey])
        FROM    #Target

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


        SELECT  @SourceCount = COUNT([SourceKey])
        FROM    #Source
        WHERE   CountryID = 'US'
  


        SELECT  @TargetCount = COUNT([TargetKey])
        FROM    #Target
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


        SELECT  @SourceCount = COUNT([SourceKey])
        FROM    #Source
        WHERE   CountryID = 'CA'
  


        SELECT  @TargetCount = COUNT([TargetKey])
        FROM    #Target
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

        SELECT  @SourceCount = COUNT([SourceKey])
        FROM    #Source
        WHERE   CountryID = 'AU'   


        SELECT  @TargetCount = COUNT([TargetKey])
        FROM    #Target
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


		

        SET @message = CONCAT(' STEP: 5. Duplicate validation started.',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message




--++++++++++++++++++--++++++++++++++++++
--USA RFO Accounts Duplicate Vaidation.
--++++++++++++++++++--++++++++++++++++++
        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Duplicate'




        SELECT  @SourceCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([SourceKey]) Ct
                  FROM      #Source
                  WHERE     countryID = 'US'
                  GROUP BY  [SourceKey]
                  HAVING    COUNT([SourceKey]) > 1
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
        FROM    ( SELECT    COUNT([SourceKey]) Ct
                  FROM      #Source
                  WHERE     countryID = 'CA'
                  GROUP BY  [SourceKey]
                  HAVING    COUNT([SourceKey]) > 1
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
        FROM    ( SELECT    COUNT([TargetKey]) Ct
                  FROM      #Target
                  WHERE     p_country = 'US'
                  GROUP BY  [TargetKey]
                  HAVING    COUNT([TargetKey]) > 1
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
        FROM    ( SELECT    COUNT([TargetKey]) Ct
                  FROM      #Target
                  WHERE     p_country = 'CA'
                  GROUP BY  [TargetKey]
                  HAVING    COUNT([TargetKey]) > 1
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


        SET @message = CONCAT(' STEP: 6. Missing Validaiton Started ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


--US ACCOUNTS SCTOTG  MISSING VALIDATION 
        SELECT  a.SourceKey ,
                b.[TargetKey] ,
                COALESCE(a.countryID, b.p_country) AS Country ,
                CASE WHEN a.[SourceKey] IS NULL
                     THEN 'MissingInRFO-LoadedExtraInHybris'
                     WHEN b.[TargetKey] IS NULL
                     THEN 'MissingInHybris-NotLoadedInHybris'
                END AS [Missing From ]
        INTO    #Missing
        FROM    #Source a
                FULL OUTER JOIN #Target b ON a.SourceKey = b.[TargetKey]
        WHERE   a.SourceKey IS NULL
                OR b.[TargetKey] IS NULL

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


					

-- LOADING ERROR WITH SAMPLE DATA.

        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetColumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetColumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [SourceKey] , -- SourceValue - nvarchar(50)
                        [TargetKey]
                FROM    #Missing
                WHERE   Country = 'US'
                        AND [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetColumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [SourceKey] , -- SourceValue - nvarchar(50)
                        [TargetKey]
                FROM    #Missing
                WHERE   Country = 'US'
                        AND [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetColumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [SourceKey] , -- SourceValue - nvarchar(50)
                        [TargetKey]
                FROM    #Missing
                WHERE   Country = 'CA'
                        AND [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetColumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [SourceKey] , -- SourceValue - nvarchar(50)
                        [TargetKey]
                FROM    #Missing
                WHERE   Country = 'CA'
                        AND [Missing From ] = 'MissingInHybris-NotLoadedInHybris'


--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
		
        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp

    

        SET @message = CONCAT(' STEP: 7. Removing Isuues Key from Temps. ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


         
        DELETE  a
        FROM    #Source a
                JOIN #missing m ON m.SourceKey = a.SourceKey


        DELETE  a
        FROM    #Target a
                JOIN #missing m ON m.TargetKey = a.TargetKey	



        DROP TABLE #missing

        SET @sourceCount = 0
        SELECT  @sourceCount = COUNT(SourceKey)
        FROM    #Source a
                JOIN #Target b ON a.SourceKey = b.TargetKey

        SET @message = CONCAT(' STEP: 6. Total Record Counts for End to End = ',
                              CAST(@SourceCount AS NVARCHAR(25)), CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

   
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
                SET @Message = CONCAT('STEP: 7. Validation Started For Columnt To Column with  total fields= ',
                                      CAST(@MaxRow AS NVARCHAR(20)), CHAR(10),
                                      '-----------------------------------------------')
                   
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
                                              '. ', @TargetColumn, CHAR(10),
                                              '-----------------------------------------------')

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
                                                      ' for ', @TargetColumn,
                                                      CHAR(10),
                                                      '-----------------------------------------------')
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
                                VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
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
                                          TargetColumn ,
                                          [Key] ,
                                          SourceValue ,
                                          TargetValue
                                        )
                                        SELECT TOP 10
                                                @Flows ,
                                                @Owner ,
                                                CONCAT(@Flag, '_EndToEnd') ,
                                                @SourceColumn ,
                                                @TargetColumn ,
                                                CONCAT(@Key, '=', [Key]) ,
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


