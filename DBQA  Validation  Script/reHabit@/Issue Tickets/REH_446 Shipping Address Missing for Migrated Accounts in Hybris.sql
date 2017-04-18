



-- Checking Whether Accounts been Migrated or Not In Hybris.

SELECT  *
FROM    Hybris.dbo.users
WHERE   p_customerid IN ( '1000004', '1000005', '1000009', '1000030',
                          '1000032' )


-- Checking Shipping Address Available or Not in RFO.

SELECT  ad.*
FROM    RFOperations.RFO_Accounts.AccountContacts ac
        JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
        JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
WHERE   ad.AddressTypeID = 2-- Shipping Adddress
     AND ac.AccountContactTypeId = 1 -- Main AccountType
        AND ac.AccountId IN ( 1000004, 1000005, 1000009, 1000030, 1000032 )


		-- Checking Shipping Addresses been Loaded in Hybris or Not.

SELECT ad.* FROM Hybris.dbo.addresses ad 
JOIN Hybris.dbo.users u ON u.pk=ad.OwnerPkString
WHERE ad.p_shippingaddress=1 -- Shipping Address.
AND u.p_customerid IN ( '1000004', '1000005', '1000009', '1000030',
                          '1000032' )
--


-- Total missing counts of Addresses=2522867