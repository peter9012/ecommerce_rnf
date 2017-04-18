
	-- Source Date 	
SELECT  ac.AccountId ,
        ac.FirstName ,
        LastName ,
        g.Name [RFOGender]
FROM    RFOperations.RFO_Accounts.AccountContacts ac
        JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
WHERE   ac.AccountId IN ( 723199, 572517, 726568, 548593, 735187, 626505,
                          552712, 597143, 900247 )

-- Target Data 
		
SELECT  u.p_customerid ,
        u.p_name ,
        v.Code [HybrisGender]
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_gender
WHERE   u.p_customerid IN ( '723199', '572517', '726568', '548593', '735187',
                            '626505', '552712', '597143', '900247' )


-- To retrieve all.

SELECT  u.p_customerid ,
        ac.AccountId ,
        u.p_name ,
        ac.FirstName ,
        LastName ,
        v.Code [HybrisGender] ,
        g.Name [RFOGender]
FROM    Hybris.dbo.users u
        JOIN RFOperations.RFO_Accounts.AccountContacts ac ON CAST(ac.AccountId AS NVARCHAR(225)) = u.p_customerid
        JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
        JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_gender
		WHERE v.Code<>g.Name
		ORDER BY g.Name
