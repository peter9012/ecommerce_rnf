 
 
 SELECT u.p_customerid ,
        ab.AccountID ,
        u.p_name ,
		CONCAT('test_',ac.Username) [prefixWith test_],
		ac.Username,
		u.p_uid
 FROM   Hybris.dbo.users u
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON u.p_customerid = CAST(ab.AccountID AS NVARCHAR(225))
		JOIN RFOperations.Security.AccountSecurity ac ON ac.AccountID = ab.AccountID
 WHERE  CONCAT('test_',ac.Username)<>u.p_uid