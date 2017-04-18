SELECT  u.p_customerid ,
        ab.AccountID ,
        u.p_name ,
        CONCAT(ac.FirstName, ' ', ac.LastName) RFOName ,
        rf.Active [RFOActive] ,
        u.p_active ,
        rf.SoftTerminationDate ,
        rf.HardTerminationDate ,
        s.Name AS [RFOAccountStatus] ,
		t.Name[AccountTypes],
        v.Code AS [HybrisAccountStatus],
		a.AutoshipNumber[RFPCTemplate#],
		u.p_pcperksactive,
		a.Active[RFIsPCActive]
FROM    RFOperations.RFO_Accounts.AccountBase ab
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
        JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
		JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = ab.AccountTypeID
        JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ab.AccountID
        JOIN Hybris.dbo.users u ON u.p_customerid = CAST(ab.AccountID AS NVARCHAR(225))
        JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_accountstatus
        JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
WHERE   a.AutoshipTypeID = 1 -- PC Templates types
        AND a.Active = 1
		AND p_pcperksactive<>1





		--- Source Data Set 

   SELECT   ab.AccountID ,
            CONCAT(ac.FirstName, ' ', ac.LastName) RFOName ,
            rf.Active [RFOActive] ,
            rf.SoftTerminationDate ,
            rf.HardTerminationDate ,
            s.Name AS [RFOAccountStatus] ,
            a.AutoshipNumber [RFPulseTemplate#] ,
            a.Active [RFOIsPulseActive]
   FROM     RFOperations.RFO_Accounts.AccountBase ab
            JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
            JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
            JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ab.AccountID
            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
   WHERE    ab.AccountID IN ( 505658, 505994, 506061, 508649, 509586 )
            AND a.Active = 1 --Active Templates
            AND a.AutoshipTypeID = 1 --PC 

			



-- Targete Result set

       SELECT   u.p_customerid ,
                u.p_name ,
                u.p_active ,
                v.Code AS [HybrisAccountStatus] ,
                u.p_pulsesubscription
       FROM     Hybris.dbo.users u
                JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_accountstatus
       WHERE    u.p_customerid IN ( '505658', '505994', '506061', '508649',
                                    '509586' )


