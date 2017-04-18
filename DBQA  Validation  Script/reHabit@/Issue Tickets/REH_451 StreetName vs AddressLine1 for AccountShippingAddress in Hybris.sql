


--******************************
--		HYBRIS different Country 
--*******************************

-- Checking Whether Accounts been Migrated .

SELECT  pk,p_country,p_name,p_customerid,p_accountnumber,p_defaultshipmentaddress,p_defaultpaymentaddress,p_defaultshipmentaddress
FROM    Hybris.dbo.users
WHERE   p_customerid IN ( '1000419' )


-- Checking Shipping Address Available  in RFO.

SELECT ac.AccountId, ad.*
FROM    RFOperations.RFO_Accounts.AccountContacts ac
        JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
        JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
WHERE   ad.AddressTypeID = 2-- Shipping Adddress
     --AND ac.AccountContactTypeId = 1 -- Main AccountType
        AND ac.AccountId IN ( 1000419 )

		

SELECT u.p_customerid,a.* FROM Hybris.dbo.addresses a 
JOIN Hybris.dbo.users u ON u.pk=a.OwnerPkString
WHERE u.p_customerid IN ('1000419')
AND p_shippingaddress=1


	