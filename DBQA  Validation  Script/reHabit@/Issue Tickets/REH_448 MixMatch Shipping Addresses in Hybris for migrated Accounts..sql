--**************************
--	HYBRIS LOADED EXTRAS
--***************************
-- Checking Whether Accounts been Migrated or Not In Hybris.

SELECT  *
FROM    Hybris.dbo.users
WHERE   p_customerid IN ( '1000350', '1000433', '1000749', '1000785',
                          '1000980', '1001174', '1001215', '1001486',
                          '1001979', '1002004' )
						  
-- Checking Shipping Address Available or Not in RFO.

SELECT  ac.AccountId ,COUNT(ad.AddressID)[RFOTotalCount]
FROM    RFOperations.RFO_Accounts.AccountContacts ac
        JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
        JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
WHERE   ad.AddressTypeID = 2-- Shipping Adddress
        AND ac.AccountContactTypeId = 1 -- Main AccountType
        AND ac.AccountId IN ( 1000350, 1000433, 1000749, 1000785, 1000980,
                              1001174, 1001215, 1001486, 1001979, 1002004 )
							  GROUP BY ac.AccountId


		-- Checking Shipping Addresses been Loaded Counts for each Customers in Hybris .

        SELECT  u.p_customerid ,
                COUNT(ad.pk) [MigratedCount]
        FROM    Hybris.dbo.addresses ad
                JOIN Hybris.dbo.users u ON u.pk = ad.OwnerPkString
        WHERE   ad.p_shippingaddress = 1 -- Shipping Address.
                AND u.p_customerid IN ( '1000350', '1000433', '1000749',
                                        '1000785', '1000980', '1001174',
                                        '1001215', '1001486', '1001979',
                                        '1002004' )
        GROUP BY u.p_customerid



--******************************
--		HYBRIS MISSING 
--*******************************

-- Checking Whether Accounts been Migrated or Not In Hybris.

SELECT  *
FROM    Hybris.dbo.users
WHERE   p_customerid IN ( '1000350' )


-- Checking Shipping Address Available or Not in RFO.

SELECT  ad.*
FROM    RFOperations.RFO_Accounts.AccountContacts ac
        JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
        JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
WHERE   ad.AddressTypeID = 2-- Shipping Adddress
     AND ac.AccountContactTypeId = 1 -- Main AccountType
        AND ac.AccountId IN ( 1000350 )


		-- Checking Shipping Addresses been Loaded in Hybris or Not.

SELECT ad.* FROM Hybris.dbo.addresses ad 
JOIN Hybris.dbo.users u ON u.pk=ad.OwnerPkString
WHERE ad.p_shippingaddress=1 -- Shipping Address.
AND u.p_customerid IN ( '1000350' )




--******************************
--		HYBRIS Extra Migrated
--*******************************

-- Checking Whether Accounts been Migrated or Not In Hybris.

SELECT  *
FROM    Hybris.dbo.users
WHERE   p_customerid IN ( '1002004' )


-- Checking Shipping Address Available or Not in RFO.

SELECT  ad.*
FROM    RFOperations.RFO_Accounts.AccountContacts ac
        JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
        JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
WHERE   ad.AddressTypeID = 2-- Shipping Adddress
     AND ac.AccountContactTypeId = 1 -- Main AccountType
        AND ac.AccountId IN ( 1002004 )


		-- Checking Shipping Addresses been Loaded in Hybris or Not.

SELECT ad.* FROM Hybris.dbo.addresses ad 
JOIN Hybris.dbo.users u ON u.pk=ad.OwnerPkString
WHERE ad.p_shippingaddress=1 -- Shipping Address.
AND u.p_customerid IN ( '1002004' )