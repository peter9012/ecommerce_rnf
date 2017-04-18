--Missing Eligible PREFFERED CUSTOMERS :

--Step: 1 Loading IN Temp

DECLARE @SourceCount INT ,
    @TargetCount INT ,
    @message NVARCHAR(MAX) ,
    @Flows NVARCHAR(50)= 'Data Migration' ,
    @owner NVARCHAR(50)= 'Accounts' ,
    @Key NVARCHAR(25)= 'AccountID' ,
    @ValidationType NVARCHAR(50)= 'Counts' ,
    @loaddate DATE = '2017-02-28' ,
    @OrderDate DATE ,
    @HardTermDate DATE


SET @OrderDate = DATEADD(MONTH, -18, @LoadDate) 
SET @HardTermDate = DATEADD(MONTH, -6, @LoadDate)



SELECT  CAST(ab.AccountID AS NVARCHAR(225)) AS AccountID
INTO    #temp
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
        --LEFT JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId AND ac.AccountContactTypeId=1
        LEFT JOIN RFOperations.RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId
        LEFT JOIN RFOperations.RFO_Accounts.EmailAddresses ea ON ea.EmailAddressID = ae.EmailAddressId
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
        AND ab.AccountTypeID = 2
EXCEPT
SELECT  p_customerid
FROM    Hybris.dbo.Users u
        JOIN Hybris.dbo.countries co ON co.pk = u.p_country
        JOIN Hybris.dbo.composedtypes t ON t.pk = u.TypePkString
        JOIN Hybris.dbo.enumerationvalues s ON s.pk = u.p_accountstatus
		---Total counts=2632529



		--Step: 2 Retrieving ALL FROM RFO:


		
  DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'Data Migration' ,
            @owner NVARCHAR(50)= 'Accounts' ,
            @Key NVARCHAR(25)= 'AccountID' ,
            @ValidationType NVARCHAR(50)= 'Counts' ,
			@loaddate DATE ='2017-02-28',
			 @OrderDate DATE,
            @HardTermDate DATE


          SET   @OrderDate  = DATEADD(MONTH, -18, @LoadDate) 
          SET   @HardTermDate = DATEADD(MONTH, -6, @LoadDate)


  SELECT  
   CAST(ab.AccountID AS NVARCHAR(255)) AS [RFOKey] ,--	p_customerid    
  IIF(CAST(ac.Birthday AS DATE) = '1900-01-01', NULL, ac.Birthday) AS Birthday ,--	p_dateofbirth
                rf.HardTerminationDate ,--	p_hardterminateddate
                rf.SoftTerminationDate ,--	p_softterminateddate
                --ac.SecuredTaxNumber AS TaxNumber ,--	p_countrysecuritycode,
                ab.AccountNumber ,--p_accountnumber
                g.Name AS GenderID ,--	p_gender
                rf.IsTaxExempt ,--	p_taxexempt
                ISNULL(rf.IsBusinessEntity, 0) IsBusinessEntity ,--	p_businessentity
                ea.EmailAddress ,--	p_email
                co.Alpha2Code AS CountryID ,--	p_country                          
                s.Name AS AccountStatusID ,--	p_status
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
                                      - (CHARINDEX(' ',
                                                  LTRIM(RTRIM(rf.CoApplicant)))-1) ))
                     ELSE NULL
                END CoAppLastName ,--	p_spouselastname
                rf.NextRenewalDate ,--	p_nextrenewaldate
                rf.LastRenewalDate ,--	p_lastrenewaldate
                rf.EnrollmentDate ,--	p_enrolementdate
                CONCAT(ac.FirstName, SPACE(1), ac.LastName) Name ,--	p_name
               CONCAT('test_' ,accs.UserName ) UserName,--	p_uid
                accs.[password] ,--	PassWord
                --cu.Code AS CurrencyID ,--	p_sessioncurrency       
                --nl.ISOCode AS LanguageID ,--	p_sessionlanguage
                rf.Active ,--	p_active
                'customer' AS Typepkstring
      
        FROM    RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID --AND ab.AccountNumber='001762'
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
                JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
                JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = ab.AccountTypeID
                JOIN RFOperations.RFO_Reference.NativeLanguage nl ON nl.LanguageID = ab.LanguageId
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ab.CountryID
                JOIN RFOperations.RFO_Reference.Currency cu ON cu.CurrencyID = ab.CurrencyID
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
				JOIN #temp tm ON tm.accountid=CAST(ab.AccountID AS NVARCHAR(225))
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


--Step: 3 Checking IN Hybris just TO make sure
                    SELECT  *
                    FROM    Hybris.dbo.users
                    WHERE   p_customerid IN ( '1044735', '1047371', '1047380',
                                              '1047405', '1047439', '1047441',
                                              '1047446', '1047480', '1047521',
                                              '1047523' )


SELECT s.Name,O.* FROM RFOperations.Hybris.orders o
JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = o.OrderStatusID
WHERE o.AccountID=1044735
ORDER BY o.CompletionDate DESC


--SELECT  COUNT(*) ,
--        Code
--FROM    Hybris.DBO.USERS U
--        JOIN Hybris.DBO.enumerationvalues V ON V.PK = U.p_type
--GROUP BY v.Code
----187791 only Loaded as of now.



				--Step: 4 Just Retriving REQUIRED Fields only

						--Step: 2 Retrieving ALL FROM RFO:


		
  DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'Data Migration' ,
            @owner NVARCHAR(50)= 'Accounts' ,
            @Key NVARCHAR(25)= 'AccountID' ,
            @ValidationType NVARCHAR(50)= 'Counts' ,
			@loaddate DATE ='2017-02-28',
			 @OrderDate DATE,
            @HardTermDate DATE


          SET   @OrderDate  = DATEADD(MONTH, -18, @LoadDate) 
          SET   @HardTermDate = DATEADD(MONTH, -6, @LoadDate)


  SELECT   
   CAST(ab.AccountID AS NVARCHAR(255)) AS AccountID ,--	p_customerid    
  --IIF(CAST(ac.Birthday AS DATE) = '1900-01-01', NULL, ac.Birthday) AS Birthday ,--	p_dateofbirth
                rf.HardTerminationDate ,--	p_hardterminateddate
                rf.SoftTerminationDate ,--	p_softterminateddate
                --ac.SecuredTaxNumber AS TaxNumber ,--	p_countrysecuritycode,
                ab.AccountNumber ,--p_accountnumber
                --g.Name AS GenderID ,--	p_gender
                --rf.IsTaxExempt ,--	p_taxexempt
                --ISNULL(rf.IsBusinessEntity, 0) IsBusinessEntity ,--	p_businessentity
                ea.EmailAddress ,--	p_email
                co.Alpha2Code AS CountryID ,--	p_country                          
                s.Name AS AccountStatusID ,--	p_status
                CASE WHEN t.Name = 'Retail Customer' THEN 'RETAIL'
                     WHEN t.Name = 'Preferred Customer' THEN 'PREFERRED'
                     ELSE t.Name
                END AS AccountTypes ,--	p_type                 
                rf.NextRenewalDate ,--	p_nextrenewaldate
                rf.LastRenewalDate ,--	p_lastrenewaldate
                rf.EnrollmentDate ,--	p_enrolementdate
                CONCAT(ac.FirstName, SPACE(1), ac.LastName) Name ,--	p_name
               CONCAT('test_' ,accs.UserName ) UserName,--	p_uid
                --accs.[password] ,--	PassWord
                --cu.Code AS CurrencyID ,--	p_sessioncurrency       
                --nl.ISOCode AS LanguageID ,--	p_sessionlanguage
                rf.Active 
      
        FROM    RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID --AND ab.AccountNumber='001762'
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
                JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
                JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = ab.AccountTypeID
                JOIN RFOperations.RFO_Reference.NativeLanguage nl ON nl.LanguageID = ab.LanguageId
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ab.CountryID
                JOIN RFOperations.RFO_Reference.Currency cu ON cu.CurrencyID = ab.CurrencyID
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
				JOIN #temp tm ON tm.accountid=CAST(ab.AccountID AS NVARCHAR(225))
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
