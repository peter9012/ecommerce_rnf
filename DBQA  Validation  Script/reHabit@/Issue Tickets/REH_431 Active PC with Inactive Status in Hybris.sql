SELECT  u.p_customerid ,
        ab.AccountID ,
        rf.SoftTerminationDate ,
        rf.HardTerminationDate ,
		rf.Active AS RFActive,
		u.p_active,
       CASE WHEN  s.Name ='Hard Terminated' THEN 'HARDTERMINATED'
	   ELSE s.Name END AS  [RFOStatus] ,
        v.Code AS [HybrisStatus],ab.AccountTypeID
FROM    RFOperations_02022017.RFO_Accounts.AccountBase ab
        JOIN RFOperations_02022017.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
        JOIN Hybris.dbo.users u ON u.p_customerid = ab.AccountID 
        JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_accountstatus
        JOIN RFOperations_02022017.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
WHERE    CASE WHEN  s.Name ='Hard Terminated' THEN 'HARDTERMINATED'
	   ELSE s.Name END <> v.Code--138554
	 AND s.Name='Active'