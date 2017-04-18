

 SELECT u.p_customerid ,
        rf.AccountID ,
        u.p_name ,
        u.p_softterminateddate ,
        rf.SoftTerminationDate
 FROM   Hybris.dbo.users u
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON u.p_customerid = CAST(rf.AccountID AS NVARCHAR(225))
 WHERE  CAST(u.p_softterminateddate AS DATE) <> CAST(rf.SoftTerminationDate AS DATE)