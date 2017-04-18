-- CHECKING WHETHER ACCOUNTS BEEN MIGRATED IN HYBRIS OR NOT 

SELECT  *
FROM    Hybris.dbo.users
WHERE   p_customerid IN ('1001164')

-- CHECKING RFO PAYMENTPROFILE 
SELECT  p.ProfileName,p.IsDefault,cp.*
FROM    RFOperations.RFO_Accounts.PaymentProfiles p
        JOIN RFOperations.RFO_Accounts.CreditCardProfiles cp ON cp.PaymentProfileID = p.PaymentProfileID
       -- JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = cp.BillingAddressID -- Making Sure Profile has Billing too.
WHERE   p.AccountID IN (1001164 )

--SELECT * FROM RFOperations.RFO_Accounts.CreditCardProfiles WHERE DisplayNumber='517805**********1291'

-- CHECKING HYBRIS PAYMENTPROFILE

SELECT  *
FROM    Hybris.dbo.paymentinfos p
        JOIN Hybris.dbo.users u ON u.pk = p.OwnerPkString
WHERE   u.p_customerid IN ( '1001164')

