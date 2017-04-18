SELECT  u.p_customerid ,
        ab.AccountID ,
        rf.SoftTerminationDate ,
        rf.HardTerminationDate ,
       CASE WHEN  s.Name ='Hard Terminated' THEN 'HARDTERMINATED'
	   ELSE s.Name END AS  [RFOStatus] ,
        v.Code AS [HybrisStatus]
FROM    RFOperations.RFO_Accounts.AccountBase ab
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
        JOIN Hybris.dbo.users u ON u.p_customerid = CAST(ab.AccountID AS NVARCHAR(225))
        JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_accountstatus
        JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
WHERE    CASE WHEN  s.Name ='Hard Terminated' THEN 'HARDTERMINATED'
	   ELSE s.Name END <> v.Code--138554

	  --  AND s.Name='Soft Terminated Voluntary'--137,551 
		--AND s.Name='Begun Enrollment'--826
		AND s.Name='Soft Terminated Involuntary'--117




		--- Source Data Set 

        SELECT  ab.AccountID ,
                rf.SoftTerminationDate ,
                rf.HardTerminationDate ,
                CASE WHEN s.Name = 'Hard Terminated' THEN 'HARDTERMINATED'
                     ELSE s.Name
                END AS [RFOStatus]
        FROM    RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
                JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
        WHERE   ab.AccountID IN ( 500089, 500093, 500267, 4540552, 4540623,
                                  4540769, 6817664, 6818934, 6824906 )



-- Targete Result set

        SELECT  u.p_customerid ,
                u.p_name ,
                v.Code AS [HybrisStatus]
        FROM    Hybris.dbo.users u
                JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_accountstatus
        WHERE   u.p_customerid IN ( '500089', '500093', '500267', '4540552',
                                    '4540623', '4540769', '6817664', '6818934',
                                    '6824906' )



