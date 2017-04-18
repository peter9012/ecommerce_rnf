-- CHECKING WHETHER ACCOUNTS BEEN MIGRATED IN HYBRIS OR NOT 

SELECT  *
FROM    Hybris.dbo.users
WHERE   p_customerid IN ( '1000006', '1000115', '1000123', '1000174',
                          '1000175', '1000210', '1000220' )

-- CHECKING RFO PAYMENTPROFILE 
SELECT  cp.*
FROM    RFOperations.RFO_Accounts.PaymentProfiles p
        JOIN RFOperations.RFO_Accounts.CreditCardProfiles cp ON cp.PaymentProfileID = p.PaymentProfileID
        JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = cp.BillingAddressID -- Making Sure Profile has Billing too.
WHERE   p.AccountID IN ( 1000006, 1000115, 1000123, 1000174, 1000175, 1000210,
                         1000220 )



-- CHECKING HYBRIS PAYMENTPROFILE

SELECT  *
FROM    Hybris.dbo.paymentinfos p
        JOIN Hybris.dbo.users u ON u.pk = p.OwnerPkString 
WHERE   u.p_customerid IN ( '1000006', '1000115', '1000123', '1000174',
                            '1000175', '1000210', '1000220' )