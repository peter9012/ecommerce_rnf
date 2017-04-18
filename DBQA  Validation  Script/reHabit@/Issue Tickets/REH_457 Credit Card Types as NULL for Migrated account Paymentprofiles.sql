-- CHECKING WHETHER ACCOUNTS BEEN MIGRATED IN HYBRIS OR NOT 

SELECT  *
FROM    Hybris.dbo.users
WHERE   p_customerid IN ( '1002733' )

-- CHECKING RFO PAYMENTPROFILE 
SELECT  v.Name[CreditCardTypes],
        p.IsDefault ,
        cp.*
FROM    RFOperations.RFO_Accounts.PaymentProfiles p
        JOIN RFOperations.RFO_Accounts.CreditCardProfiles cp ON cp.PaymentProfileID = p.PaymentProfileID
        JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = cp.BillingAddressID -- Making Sure Profile has Billing too.
		JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = cp.VendorID
WHERE   p.AccountID IN ( 1002733 )

--SELECT * FROM RFOperations.RFO_Accounts.CreditCardProfiles WHERE DisplayNumber='517805**********1291'

-- CHECKING HYBRIS PAYMENTPROFILE

SELECT v.Code[CreditCardTypes],p.*
FROM    Hybris.dbo.paymentinfos p
        JOIN Hybris.dbo.users u ON u.pk = p.OwnerPkString
		LEFT JOIN Hybris.dbo.enumerationvalues v ON v.pk=p.p_type
WHERE   u.p_customerid IN ( '1002733' )


SELECT TOP 1 p.*
FROM    Hybris.dbo.paymentinfos p
        JOIN Hybris.dbo.users u ON u.pk = p.OwnerPkString
		LEFT JOIN Hybris.dbo.enumerationvalues v ON v.pk=p.p_type
		WHERE v.Code IS NOT NULL
