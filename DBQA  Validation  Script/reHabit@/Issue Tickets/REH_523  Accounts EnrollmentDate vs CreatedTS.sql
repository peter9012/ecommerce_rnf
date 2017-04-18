 SELECT u.p_customerid ,
        rf.AccountID ,
        u.p_name ,
        u.createdTS ,
        rf.EnrollmentDate
 FROM   Hybris.dbo.users u
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON u.p_customerid = CAST(rf.AccountID AS NVARCHAR(225))
 WHERE  CAST(u.createdTS AS DATE) <> CAST(rf.EnrollmentDate AS DATE)