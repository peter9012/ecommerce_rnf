

 SELECT u.p_customerid ,
        rf.AccountID ,
        u.p_name ,
        u.p_softterminateddate ,
        rf.HardTerminationDate
 FROM   Hybris.dbo.users u
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON u.p_customerid = CAST(rf.AccountID AS NVARCHAR(225))
 WHERE  CAST(u.p_hardterminateddate AS DATE) <> CAST(rf.HardTerminationDate AS DATE)