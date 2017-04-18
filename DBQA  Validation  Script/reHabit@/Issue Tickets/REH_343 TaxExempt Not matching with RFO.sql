
	-- Source Date 	
    SELECT  ac.AccountId ,
            CONCAT(ac.FirstName, SPACE(1), ac.LastName) AS RFOName ,
            rf.IsTaxExempt AS RFOIsTaxExempt
    FROM    RFOperations.RFO_Accounts.AccountContacts ac
            JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ac.AccountId
    WHERE   ac.AccountId IN ( 551645, 552050, 556016, 574655, 976241, 1261405 )

-- Target Data 
		
    SELECT  u.p_customerid ,
            u.p_name ,
            u.p_taxexempt
    FROM    Hybris.dbo.users u
    WHERE   u.p_customerid IN ( '551645', '552050', '556016', '574655',
                                '976241', '1261405' )


-- To retrieve all.

SELECT  u.p_customerid ,
        ac.AccountId ,
        u.p_name ,  
		CONCAT(ac.FirstName,SPACE(1),ac.LastName) AS RFOName,
       rf.IsTaxExempt AS RFOIsTaxExempt,
	   u.p_taxexempt
FROM    Hybris.dbo.users u
        JOIN RFOperations.RFO_Accounts.AccountContacts ac ON CAST(ac.AccountId AS NVARCHAR(225)) = u.p_customerid
		JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ac.AccountId           
		WHERE ISNULL(rf.IsTaxExempt,0)<>u.p_taxexempt
