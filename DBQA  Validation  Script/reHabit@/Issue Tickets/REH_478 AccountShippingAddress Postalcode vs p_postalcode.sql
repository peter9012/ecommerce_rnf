


--******************************
--		HYBRIS different Country 
--*******************************

-- Checking Whether Accounts been Migrated .

SELECT  pk,p_country,p_name,p_customerid,p_accountnumber,p_defaultshipmentaddress,p_defaultpaymentaddress,p_defaultshipmentaddress
FROM    Hybris.dbo.users
WHERE   p_customerid IN ( '1000068' )


-- Checking Shipping Address Available  in RFO.

SELECT ac.AccountId, ad.*
FROM    RFOperations.RFO_Accounts.AccountContacts ac
        JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
        JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
WHERE   ad.AddressTypeID = 2-- Shipping Adddress
     --AND ac.AccountContactTypeId = 1 -- Main AccountType
        AND ac.AccountId IN ( 1000068 )

		
	SELECT * FROM Hybris.dbo.addresses a
	JOIN Hybris.dbo.users u ON u.pk=a.OwnerPkString AND a.p_shippingaddress=1
	WHERE u.p_customerid IN ( '1000068' )



		-- Checking Shipping Addresses Regions diffrent than RFO .
        SELECT  *
        FROM    ( SELECT  ad.p_postalcode [HybrisPostalCode] ,
                            u.p_customerid ,
                            p_streetname ,
                            ad.OwnerPkString ,
                            ad.p_shippingaddress ,
                            ad.p_country
                  FROM      Hybris.dbo.addresses ad
                            JOIN Hybris.dbo.users u ON u.pk = ad.OwnerPkString                           
                  WHERE     ad.p_shippingaddress = 1
                ) Hybris
                JOIN ( SELECT   ac.AccountId ,
                                PostalCode ,
                                ad.AddressLine1 ,
                                CountryID
                       FROM     RFOperations.RFO_Accounts.AccountContacts ac
                                JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
                                JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
                       WHERE    ad.AddressTypeID = 2-- Shipping Adddress
                                AND ac.AccountContactTypeId = 1 -- Main AccountType
                     ) RFO ON CAST(rfo.AccountId AS NVARCHAR(225)) = Hybris.p_customerid
                              AND rfo.AddressLine1 = Hybris.p_streetname
							  WHERE ISNULL(Hybris.[HybrisPostalCode],'')<>ISNULL(rfo.PostalCode,'')


