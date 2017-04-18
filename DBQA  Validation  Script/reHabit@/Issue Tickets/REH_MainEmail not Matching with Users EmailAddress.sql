     SELECT 
        ab.AccountID ,
        p_customerid ,
        p_email ,main.EmailAddress[main],PWE.EmailAddress[pws],CONCAT('test_',accs.Username)[userName],
        COALESCE(main.EmailAddress, pwe.EmailAddress, CONCAT('test_', Username)) [Coalesce(Main,PSW,UserName)],CONCAT(ac.FirstName, SPACE(1), ac.LastName) AS RFName ,
        u.p_name ,
        s.Name [status] ,
        t.Name [accounttype] ,
        rf.Active  
FROM    RFOperations.RFO_Accounts.AccountBase ab
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID    --AND ab.AccountID=1000511
        JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
                                                             AND AccountContactTypeId = 1
        JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
        JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = ab.AccountTypeID
        JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ab.CountryID
        JOIN Hybris.dbo.users u ON u.p_customerid = CAST(ab.AccountID AS NVARCHAR(225))
                                   --AND u.p_email = 'test_spatalon@myrandf.com'
        LEFT JOIN RFOperations.[Security].AccountSecurity accs ON accs.AccountID = ab.AccountID
        LEFT JOIN ( SELECT  ac.AccountId,EmailAddress,ac.AccountContactId
                    FROM    RFOperations.RFO_Accounts.AccountEmails ae
                            JOIN RFOperations.RFO_Accounts.EmailAddresses me ON me.EmailAddressID = ae.EmailAddressId
                                                              AND me.EmailAddressTypeID = 1 AND IsDefault=1
							JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = ae.AccountContactId
							GROUP BY ac.AccountId,EmailAddress,ac.AccountContactId
                  ) main ON main.AccountId = ac.AccountId AND main.AccountContactId = ac.AccountContactId
        LEFT JOIN ( SELECT  ac.AccountId,EmailAddress,ac.AccountContactId
                    FROM    RFOperations.RFO_Accounts.AccountEmails ae
                            JOIN RFOperations.RFO_Accounts.EmailAddresses pe ON pe.EmailAddressID = ae.EmailAddressId
                                                              AND EmailAddressTypeID = 5 AND IsDefault=1
							JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = ae.AccountContactId
							GROUP BY ac.AccountId,EmailAddress,ac.AccountContactId
                  ) PWE ON PWE.AccountId = ac.AccountId AND PWE.AccountContactId = ac.AccountContactId	                                                       
		
		WHERE u.p_email<> COALESCE(main.EmailAddress, pwe.EmailAddress, CONCAT('test_', Username))


  --SOURCE DATA SET    
	 
	     SELECT ab.AccountID ,               
                main.EmailAddress [main] ,
                PWE.EmailAddress [pws] ,
                CONCAT('test_', accs.Username) [userName] ,
                COALESCE(main.EmailAddress, pwe.EmailAddress,
                         CONCAT('test_', Username)) [Coalesce(Main,PSW,UserName)] ,
                CONCAT(ac.FirstName, SPACE(1), ac.LastName) AS RFName ,              
                s.Name [status] ,
                t.Name [accounttype] ,
                rf.Active
         FROM   RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID    --AND ab.AccountID=1000511
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
                                                              AND AccountContactTypeId = 1
                JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
                JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = ab.AccountTypeID
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ab.CountryID
                LEFT JOIN RFOperations.[Security].AccountSecurity accs ON accs.AccountID = ab.AccountID
                LEFT JOIN ( SELECT  ac.AccountId ,
                                    EmailAddress ,
                                    ac.AccountContactId
                            FROM    RFOperations.RFO_Accounts.AccountEmails ae
                                    JOIN RFOperations.RFO_Accounts.EmailAddresses me ON me.EmailAddressID = ae.EmailAddressId
                                                              AND me.EmailAddressTypeID = 1 AND IsDefault=1
                                    JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = ae.AccountContactId
                            GROUP BY ac.AccountId ,
                                    EmailAddress ,
                                    ac.AccountContactId
                          ) main ON main.AccountId = ac.AccountId
                                    AND main.AccountContactId = ac.AccountContactId
                LEFT JOIN ( SELECT  ac.AccountId ,
                                    EmailAddress ,
                                    ac.AccountContactId
                            FROM    RFOperations.RFO_Accounts.AccountEmails ae
                                    JOIN RFOperations.RFO_Accounts.EmailAddresses pe ON pe.EmailAddressID = ae.EmailAddressId
                                                              AND EmailAddressTypeID = 5 AND IsDefault=1
                                    JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = ae.AccountContactId
                            GROUP BY ac.AccountId ,
                                    EmailAddress ,
                                    ac.AccountContactId
                          ) PWE ON PWE.AccountId = ac.AccountId
                                   AND PWE.AccountContactId = ac.AccountContactId
         WHERE  ab.AccountID IN ( 4650614, 4651858, 4659683, 4648105, 4648903,
                                  4653614 )

-- Target Result Set.
         SELECT  p_customerid ,
                p_email ,
                u.p_name               
         FROM   Hybris.dbo.users u 
         WHERE  u.p_customerid IN ( 4650614, 4651858, 4659683, 4648105, 4648903,
                                  4653614 )


