USE [DM_QA]
GO
/****** Object:  StoredProcedure [dbqa].[uspAccountsHeader_BULK]    Script Date: 3/9/2017 8:02:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER PROCEDURE [dbqa].[RFO_Hybris_Account_Level_Integrations] 
AS
    BEGIN 
        SET NOCOUNT ON;


        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'RFO_Hybris_Integration' ,
            @owner NVARCHAR(50)= 'Accounts' ,
            @Key NVARCHAR(25)= 'AccountID' ,
            @ValidationType NVARCHAR(50)= 'Counts'



        IF OBJECT_ID('TEMPDB.dbo.#RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#HybrisAccounts') IS NOT NULL
            DROP TABLE #Hybris;
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing  
	

        SET @message = CONCAT(' STEP:1. Source Table Started to Load.',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


	

        SELECT  ac.Birthday ,--	p_dateofbirth
                rf.HardTerminationDate ,--	p_hardterminateddate
                rf.SoftTerminationDate ,--	p_softterminateddate
                ac.SecuredTaxNumber AS TaxNumber ,--	p_countrysecuritycode,
                ab.AccountNumber ,--p_accountnumber
                g.Name AS GenderID ,--	p_gender
                rf.IsTaxExempt ,--	p_taxexempt
                rf.IsBusinessEntity ,--	p_businessentity
                ea.EmailAddress ,--	p_email
                co.Alpha2Code AS CountryID ,--	p_country
                ab.AccountID AS [RFOKey] ,--	p_customerid
                rf.SponsorID ,--	p_newsponsorid
                CASE WHEN s.Name = 'Hard Terminated' THEN 'HARDTERMINATED'
                     ELSE s.Name
                END AS AccountStatusID ,--	p_status
                CASE WHEN t.Name = 'Retail Customer' THEN 'RETAIL'
                     WHEN t.Name = 'Preferred Customer' THEN 'PREFERRED'
                     ELSE t.Name
                END AS AccountTypes ,--	p_type 
                rf.CoApplicant AS CoAppName ,
                rf.NextRenewalDate ,--	p_nextrenewaldate
                rf.LastRenewalDate ,--	p_lastrenewaldate
                rf.EnrollmentDate ,--	p_enrolementdate
                CONCAT(ac.FirstName, SPACE(1), ac.LastName) Name ,--	p_name
                accs.UserName ,--	p_uid
                accs.[password] ,--	PassWord
                p.PaymentProfileID ,--	p_defaultpaymentinfo
                Shipping.AddressID AS ShippingAddressId ,--	p_defaultshipmentaddress
                Billing.AddressID AS BillingAddressID ,--	P_defaultpaymentAddress
                cu.Code AS CurrencyID ,--	p_sessioncurrency       
                nl.ISOCode AS LanguageID ,--	p_sessionlanguage
                rf.Active ,--	p_active
                co.Alpha2Code AS CountryID
				INTO #RFO
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
                LEFT JOIN RFOperations.RFO_Accounts.PaymentProfiles p ON p.AccountID = ab.AccountID
                                                              AND p.IsDefault = 1
                LEFT JOIN ( SELECT  ac.AccountId ,
                                    ad.AddressID
                            FROM    RFOperations.RFO_Accounts.Addresses ad
                                    JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AddressID = ad.AddressID
                                                              AND ad.AddressTypeID = 2
                                                              AND ad.IsDefault = 1
                                    JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = aca.AccountContactId
                          ) Shipping ON Shipping.AccountId = ab.AccountID
                LEFT JOIN ( SELECT  ac.AccountId ,
                                    ad.AddressID
                            FROM    RFOperations.RFO_Accounts.Addresses ad
                                    JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AddressID = ad.AddressID
                                                              AND ad.AddressTypeID = 3
                                                              AND ad.IsDefault = 1
                                    JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = aca.AccountContactId
                          ) Billing ON Billing.AccountId = ab.AccountID
        WHERE   ea.EmailAddressTypeID = 1
		AND CAST(rf.EnrollmentDate AS DATE)>='2017-03-01'
         

		 	/* 
		
SELECT  CAST(u.p_customerid AS BIGINT) AS [HybrisKey] ,
        u.p_hardterminateddate ,
        u.p_softterminateddate ,
        p_countrysecuritycode ,
        u.p_accountnumber ,
        g.Code [p_gender] ,
        u.p_taxexempt ,
        u.p_businessentity ,
        u.p_email ,
        co.p_isocode ,
        CASE t.code
          WHEN 'Consultant' THEN cons.p_customerid
          ELSE Pref.p_customerid
        END AS p_newsponsorid ,
        s.Code [p_accountstatus] ,
        t.Code [p_AccountType] ,
        CONCAT(u.p_spousefirstname, SPACE(1), u.p_spouselastname) AS p_spousename ,
        p_nextrenewaldate ,
        p_lastrenewaldate ,
        co.createdTS ,
        p_name ,
        p_uid ,
        passwd ,
        p_defaultpaymentinfo ,
        p_defaultpaymentaddress ,
        p_defaultshipmentaddress ,
        cu.p_isocode [p_sessioncurrency] ,
        la.p_isocode [p_sessionlanguage] ,
        co.p_isocode [p_country] ,
        co.p_active
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.enumerationvalues g ON g.pk = u.p_gender 
        LEFT JOIN Hybris.dbo.countries co ON co.pk = u.p_country
        LEFT JOIN Hybris.dbo.enumerationvalues s ON s.pk = u.p_accountstatus
        LEFT JOIN Hybris.dbo.enumerationvalues t ON t.pk = u.p_type
        LEFT JOIN Hybris.dbo.languages la ON la.pk = u.p_sessionlanguage
        LEFT JOIN Hybris.dbo.currencies cu ON cu.pk = u.p_sessioncurrency
        LEFT JOIN ( SELECT  pg.SourcePK ,
                            su.p_customerid
                    FROM    Hybris.dbo.pgrels pg
                            JOIN Hybris.dbo.usergroups Sug ON Sug.PK = pg.TargetPK
                            JOIN Hybris.dbo.users su ON su.p_defaultb2bunit = sug.PK
                            JOIN Hybris.dbo.enumerationvalues v ON su.p_type = v.PK
                                                              AND v.Code = 'Consultant'
                  ) Cons ON Cons.SourcePK = u.p_defaultb2bunit
        LEFT JOIN ( SELECT  pg.SourcePK ,
                            su.p_customerid
                    FROM    Hybris.dbo.pgrels pg
                            JOIN Hybris.dbo.users su ON su.p_defaultb2bunit = pg.sourcePK
                            JOIN Hybris.dbo.enumerationvalues v ON v.pk = su.p_type
                                                              AND v.code = 'Consultant'
                  ) Pref ON Pref.SourcePK = u.p_defaultb2bunit
WHERE   t.Code IN ( 'PREFERRED', 'RETAIL', 'CONSULTANT' )  AND CAST(u.createdTS AS DATE) >= '2017-03-01'
	


		
		*/

	
	

        SET @message = CONCAT(' STEP: 2.Target Table Started to Load',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message



		SELECT * INTO #Hybris FROM DM_QA.dbo.HybrisUsers

	
        SET @message = CONCAT('STEP: 3. Index Creating Source N Target.',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message
      

/* Source to Target Total Counts */


--++++++++++++++++++++++++++++++++++++
-- USA_CAN Total Count Validation
--++++++++++++++++++++++++++++++++++++

      



        CREATE CLUSTERED INDEX cls_RFO ON  #RFO([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #Hybris([HybrisKey])


        SET @ValidationType = 'Count'
        SET @message = CONCAT(' STEP: 4 Count Validations Started. ', CHAR(10),
                              '-----------------------------------------------')
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


        SET @message = CONCAT(' STEP: 6. Missing Validaiton Started ',
                              CHAR(10),
                              '-----------------------------------------------')
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
                        N'' , -- TargetColumn - nvarchar(50)
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
                        N'' , -- TargetColumn - nvarchar(50)
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
                        N'' , -- TargetColumn - nvarchar(50)
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

    

        SET @message = CONCAT(' STEP: 7. Removing Isuues Key from Temps. ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


         
        DELETE  a
        FROM    #RFO a
                JOIN #missing m ON m.RFOkey = a.RFOKey


        DELETE  a
        FROM    #Hybris a
                JOIN #missing m ON m.HybrisKey = a.HybrisKey	



        DROP TABLE #missing

        SET @sourceCount = 0
        SELECT  @sourceCount = COUNT(RFOKey)
        FROM    #RFO a
                JOIN #Hybris b ON a.RFOKey = b.HybrisKey

        SET @message = CONCAT(' STEP: 6. Total Record Counts for End to End = ',
                              CAST(@SourceCount AS NVARCHAR(25)), CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

   
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'Accounts'
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

		










