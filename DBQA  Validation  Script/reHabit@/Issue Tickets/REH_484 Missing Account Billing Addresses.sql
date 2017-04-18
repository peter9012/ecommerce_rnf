-- CHECKING WHETHER ACCOUNTS BEEN MIGRATED IN HYBRIS OR NOT.

SELECT  *
FROM    Hybris.dbo.users
WHERE   p_customerid IN ( '1000004', '1000005', '1000009', '1000030',
                          '1000032', '1000033' )



--- CHECKING RFO BILLING ADDRESS FOR THOSE ACCOUNTS 
SELECT  A.*
FROM    RFOperations.RFO_Accounts.Addresses a
        JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AddressID = a.AddressID
                                                              AND AddressTypeID = 3 -- Billing AddressType
        JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = a.CountryID
        JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = aca.AccountContactId
WHERE   ac.AccountId IN ( 1000004, 1000005, 1000009, 1000030, 1000032, 1000033 )

--CHECKING WHETHER HYBRIS ADDRESSES MIGRATED OR NOT .

SELECT  a.*
FROM    Hybris.dbo.addresses a
        JOIN hybris.dbo.users u ON u.pk = a.OwnerPkString
WHERE   a.p_billingaddress = 1
        AND u.p_customerid IN ( '1000004', '1000005', '1000009', '1000030',
                                '1000032', '1000033' )