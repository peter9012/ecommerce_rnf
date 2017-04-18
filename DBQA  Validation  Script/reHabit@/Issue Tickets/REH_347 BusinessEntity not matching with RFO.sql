
	-- Source Date 	
    SELECT  ac.AccountId ,
            CONCAT(ac.FirstName, SPACE(1), ac.LastName) AS RFOName ,
            rf.IsBusinessEntity AS RFOIsTaxExempt
    FROM    RFOperations.RFO_Accounts.AccountContacts ac
            JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ac.AccountId
    WHERE   ac.AccountId IN ( 506393, 505630, 510814, 517471, 529931, 529456 )

-- Target Data 
		
    SELECT  u.p_customerid ,
            u.p_name ,
            u.p_businessentity
    FROM    Hybris.dbo.users u
    WHERE   u.p_customerid IN ( '506393', '505630', '510814', '517471',
                                '529931', '529456' )

-- To retrieve all.

SELECT  u.p_customerid ,
        ac.AccountId ,
        u.p_name ,  
		CONCAT(ac.FirstName,SPACE(1),ac.LastName) AS RFOName,
       rf.IsBusinessEntity AS RFOIsBusinessEntity,
	   u.p_businessentity,
	   v.Code AS [CustomerTypes]
FROM    Hybris.dbo.users u
        JOIN RFOperations.RFO_Accounts.AccountContacts ac ON CAST(ac.AccountId AS NVARCHAR(225)) = u.p_customerid
		JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ac.AccountId 
		JOIN Hybris.dbo.enumerationvalues v ON v.pk=u.p_type          
		WHERE ISNULL(rf.IsBusinessEntity,0)<>u.p_businessentity
