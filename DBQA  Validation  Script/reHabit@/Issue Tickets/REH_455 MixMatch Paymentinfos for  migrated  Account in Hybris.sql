-- CHECKING WHETHER ACCOUNTS BEEN MIGRATED IN HYBRIS OR NOT 

SELECT  *
FROM    Hybris.dbo.users
WHERE   p_customerid IN ('1000350')

-- CHECKING RFO PAYMENTPROFILE 
SELECT  p.IsDefault,cp.*
FROM    RFOperations.RFO_Accounts.PaymentProfiles p
        JOIN RFOperations.RFO_Accounts.CreditCardProfiles cp ON cp.PaymentProfileID = p.PaymentProfileID
       -- JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = cp.BillingAddressID -- Making Sure Profile has Billing too.
WHERE   p.AccountID IN (1000350 )

--SELECT * FROM RFOperations.RFO_Accounts.CreditCardProfiles WHERE DisplayNumber='517805**********1291'

-- CHECKING HYBRIS PAYMENTPROFILE

SELECT  *
FROM    Hybris.dbo.paymentinfos p
        JOIN Hybris.dbo.users u ON u.pk = p.OwnerPkString
WHERE   u.p_customerid IN ( '1000350')


	-- Hybris loaded more
	
	
	SELECT  *
FROM    Hybris.dbo.users
WHERE   p_customerid IN ('1000980')

-- CHECKING RFO PAYMENTPROFILE 
SELECT  p.IsDefault,cp.*
FROM    RFOperations.RFO_Accounts.PaymentProfiles p
        JOIN RFOperations.RFO_Accounts.CreditCardProfiles cp ON cp.PaymentProfileID = p.PaymentProfileID
     JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = cp.BillingAddressID -- Making Sure Profile has Billing too.
WHERE   p.AccountID IN (1000980 )

--SELECT * FROM RFOperations.RFO_Accounts.CreditCardProfiles WHERE DisplayNumber='517805**********1291'

-- CHECKING HYBRIS PAYMENTPROFILE

SELECT  *
FROM    Hybris.dbo.paymentinfos p
        JOIN Hybris.dbo.users u ON u.pk = p.OwnerPkString
WHERE   u.p_customerid IN ( '1000980')		



SELECT  *
FROM    ( SELECT    u.p_customerid ,
                    COUNT(p.pk) [HybrisCount]
          FROM      Hybris.dbo.users u
                    JOIN Hybris.dbo.paymentinfos p ON u.pk = p.OwnerPkString
					GROUP BY u.p_customerid
        ) Hybris
        JOIN ( SELECT   p.AccountID ,
                        COUNT(cp.CreditCardProfilesID) [RFOCount]
               FROM     RFOperations.RFO_Accounts.PaymentProfiles p
                        JOIN RFOperations.RFO_Accounts.CreditCardProfiles cp ON cp.PaymentProfileID = p.PaymentProfileID
                        JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = cp.BillingAddressID -- Making Sure Profile has Billing too.	
						GROUP BY p.AccountID
             ) RFO ON Hybris.p_customerid = CAST(RFO.AccountID AS NVARCHAR(225))
WHERE   Hybris.HybrisCount <> RFO.RFOCount