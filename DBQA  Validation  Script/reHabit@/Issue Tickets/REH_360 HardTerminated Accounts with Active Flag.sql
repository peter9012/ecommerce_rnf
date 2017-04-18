SELECT  u.p_customerid ,
        ab.AccountID ,
		u.p_name,
		CONCAT(ac.FirstName,' ',ac.LastName)RFOName,
       rf.Active[RFOActive],
	    u.p_active,
	   rf.SoftTerminationDate,rf.HardTerminationDate,
	  s.Name AS [RFOAccountStatus],v.Code AS [HybrisAccountStatus]
FROM    RFOperations.RFO_Accounts.AccountBase ab
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
		JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
        JOIN Hybris.dbo.users u ON u.p_customerid = CAST(ab.AccountID AS NVARCHAR(225)) 
		JOIN Hybris.dbo.enumerationvalues v ON v.pk=u.p_accountstatus  
		JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID  
WHERE  u.p_active <> rf.Active





		--- Source Data Set 

       SELECT   ab.AccountID ,
                CONCAT(ac.FirstName, ' ', ac.LastName) RFOName ,
                rf.Active [RFOActive] ,
                rf.SoftTerminationDate ,
                rf.HardTerminationDate ,
                s.Name AS [RFOAccountStatus]
       FROM     RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID               
                JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
       WHERE    ab.AccountID IN ( 508246, 508269, 508278, 508294, 508335,
                                  508371 )



-- Targete Result set

          SELECT    u.p_customerid ,
                    u.p_name ,
                    u.p_active ,
                    v.Code AS [HybrisAccountStatus]
          FROM      Hybris.dbo.users u
                    JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_accountstatus
          WHERE     u.p_customerid IN ( '508246', '508269', '508278', '508294',
                                        '508335', '508371' )


