

        DECLARE @LoadDate DATE='2017-02-28' ,
            @OrderDate DATE ,
            @HardTermDate DATE


        SET @OrderDate = DATEADD(MONTH, -18, @LoadDate)
        SET @HardTermDate = DATEADD(MONTH, -6, @LoadDate)





        SELECT  ab.AccountID ,
                ab.AccountNumber ,--p_accountnumber
                rf.SponsorId ,
                CONCAT(ac.FirstName, SPACE(1), ac.LastName) Name ,
                ea.EmailAddress ,--	p_email
                rf.Active ,--	p_active
                CASE WHEN s.Name = 'Hard Terminated' THEN 'HARDTERMINATED'
                     ELSE s.Name
                END AS AccountStatusID ,--	p_status
                CASE WHEN t.Name = 'Retail Customer' THEN 'RETAIL'
                     WHEN t.Name = 'Preferred Customer' THEN 'PREFERRED'
                     ELSE t.Name
                END AS AccountTypes ,--	p_type              
                rf.HardTerminationDate ,--	p_hardterminateddate
                rf.SoftTerminationDate ,--	p_softterminateddate
                IIF(CAST(ac.Birthday AS DATE) = '1900-01-01', NULL, ac.Birthday) AS Birthday ,--	p_dateofbirth
                co.Alpha2Code AS CountryID ,--	p_country               
                rf.NextRenewalDate ,--	p_nextrenewaldate
                rf.LastRenewalDate ,--	p_lastrenewaldate
                rf.EnrollmentDate
       INTO DM_QA.dbo.MissingAccounts
        FROM    RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID --AND ab.AccountNumber='001762'
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
                JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
                JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = ab.AccountTypeID
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ab.CountryID
                LEFT JOIN RFOperations.RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
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
                AND NOT EXISTS ( SELECT 1
                                 FROM   DM_QA.dbo.AccountIssue ai
                                 WHERE  ai.AccountID = ab.AccountID ) -- ADDED Excluding Email Sharing  Accounts.
                AND NOT EXISTS ( SELECT 1
                                 FROM   Hybris.dbo.users u
                                 WHERE  u.p_customerid = CAST(ab.AccountID AS NVARCHAR(225)) ) -- Not Loaded in Hybris. 

								
						
				