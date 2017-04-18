
--########################################################################
--					ACCOUNT DEFAULT BILLING ADDRESSES
--########################################################################



-- DEFAULT BILLING ADDRESS FOR ACCOUNT CHECKING BY MATCHING STREETNAME


SELECT  *
FROM    ( SELECT   DISTINCT
                    ac.AccountId ,
                    u.p_defaultshipmentaddress ,
                    CONCAT(ac.FirstName, SPACE(1), ac.MiddleNAme, SPACE(1),
                           ac.LastName) RFOName ,
                    ad.AddressLine1 ,
                    ad.Locale ,
                    PostalCode ,
                   t.Name [AddresType ] ,
                    AccountContactTypeId ,
                    ad.IsDefault [RFODefault] ,
                    u.pk AS UserPk,ad.ServerModifiedDate
          FROM      RFOperations_02022017.RFO_Accounts.AccountContacts ac
                    JOIN RFOperations_02022017.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
                                                            -- AND ac.accountid = 6814523
                                                              AND ac.AccountContactTypeId = 1
                    JOIN RFOperations_02022017.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
                                                              AND ad.AddressTypeID = 3
                    JOIN Hybris.dbo.users u ON CAST(u.p_customerid AS BIGINT) = ac.AccountID  --AND ac.AccountId=2512497
					JOIN RFOperations_02022017.RFO_Reference.AddressType t ON t.AddressTypeID=ad.AddressTypeID
          WHERE     ad.IsDefault = 1
        ) RFO
        JOIN ( SELECT   u.p_customerid ,
                        u.pk [UserPK] ,
						ada.p_billingaddress,
                        u.p_name ,
                        ada.pk [addresspk] ,
                        ada.OwnerPkString [AdOwnerPk] ,
                        u.p_defaultpaymentaddress ,
                        t.Code [CustomerTypes] ,
                        ada.p_streetname ,
                        ada.p_postalcode ,
                        ada.p_town
               FROM     Hybris.dbo.addresses ada
                        JOIN Hybris.dbo.users u ON p_defaultpaymentaddress = ada.PK
                                                   AND ada.p_billingaddress = 1
                                                   --AND u.pk = 8903406190596
                        LEFT JOIN Hybris.dbo.enumerationvalues t ON t.pk = u.p_type
               WHERE    t.Code IN ( 'CONSULTANT', 'PREFERRED', 'RETAIL' )
                     --AND u.p_customerid = '6814523'
             ) Hybris ON Hybris.UserPK = RFO.UserPk
WHERE   RFO.AddressLine1 <> Hybris.p_streetname 



-- RFO HAS DEFAULT AND HYBRIS NOT HAVING DEFAULT ADDRESSES .

SELECT  *
FROM    ( SELECT    ac.AccountId ,
                    u.pk HybrisUserPk ,
                    u.p_uid AS Hybris_puid ,
                    u.p_defaultshipmentaddress ,
                    CONCAT(ac.FirstName, SPACE(1), ac.MiddleNAme, SPACE(1),
                           ac.LastName) RFOName ,
                    aca.AddressID ,
                    ad.AddressLine1 ,
                    ad.Locale ,
                    PostalCode ,
                    ad.AddressTypeID [AddresType 3=Billing] ,
                    ad.IsDefault [RFODefault]
          FROM      RFOperations_02022017.RFO_Accounts.AccountContacts ac
                    JOIN RFOperations_02022017.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
                    JOIN RFOperations_02022017.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
                                                              AND ad.AddressTypeID = 3
                    JOIN Hybris.dbo.users u ON CAST(u.p_customerid AS BIGINT) = ac.AccountID
          WHERE     ad.IsDefault = 1
                    AND EXISTS ( SELECT 1
                                 FROM   RFOperations_02022017.RFO_Accounts.AccountBase ab
                                        JOIN RFOperations_02022017.RFO_Accounts.AccountRF rf ON ab.AccountID = rf.AccountID
                                 WHERE  ab.accountID = ac.AccountId
                                        AND ab.AccountNumber <> 'AnonymousRetailAccount'
                                        AND ( ab.AccountStatusID IN ( 1, 2, 8,
                                                              9 ) -- Active,Inactive,SoftVoluntary,SoftInvoluntary
                                              OR ( ab.AccountStatusID = 10
                                                   AND EXISTS ( SELECT
                                                              1
                                                              FROM
                                                              RFOperations_02022017.Hybris.orders ro
                                                              WHERE
                                                              ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= DATEADD(MONTH,
                                                              -6, '2017-02-28') ) --HardTerm with 6 months order
                                                 )
                                              OR ( ab.AccountStatusID IN ( 5,
                                                              6, 7 )
                                                   AND EXISTS ( SELECT
                                                              1
                                                              FROM
                                                              RFOperations_02022017.Hybris.orders ro
                                                              WHERE
                                                              ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= DATEADD(MONTH,
                                                              -18,
                                                              '2017-02-28') )--Other Status with 18 months orders
                                                 )
                                            ) )
        ) RFO
        LEFT JOIN ( SELECT  u.p_customerid ,
                            u.pk [UserPK] ,
                            u.p_name ,
                            u.p_defaultpaymentaddress ,
                            u.p_defaultshipmentaddress ,
                            t.Code [CustomerTypes] ,
                            u.pk
                    FROM    Hybris.dbo.users u
                            JOIN hybris.dbo.enumerationvalues t ON t.pk = u.p_type
                    WHERE   EXISTS ( SELECT 1
                                     FROM   Hybris.dbo.addresses a
                                     WHERE  a.pk = u.p_defaultpaymentaddress
                                            AND a.OwnerPkString = u.pk
                                            AND p_billingaddress = 1 ) --AND u.p_customerid='2599101'
                            AND t.Code IN ( 'CONSULTANT', 'PREFERRED',
                                            'RETAIL' )
                            AND EXISTS ( SELECT 1
                                         FROM   RFOperations_02022017.RFO_Accounts.AccountBase ab
                                                JOIN RFOperations_02022017.RFO_Accounts.AccountRF rf ON ab.AccountID = rf.AccountID
                                         WHERE  ab.accountID = CAST(u.p_customerid AS BIGINT)
                                                AND ab.AccountNumber <> 'AnonymousRetailAccount'
                                                AND ( ab.AccountStatusID IN (
                                                      1, 2, 8, 9 ) -- Active,Inactive,SoftVoluntary,SoftInvoluntary
                                                      OR ( ab.AccountStatusID = 10
                                                           AND EXISTS ( SELECT
                                                              1
                                                              FROM
                                                              RFOperations_02022017.Hybris.orders ro
                                                              WHERE
                                                              ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= DATEADD(MONTH,
                                                              -6, '2017-02-28') ) --HardTerm with 6 months order
                                                         )
                                                      OR ( ab.AccountStatusID IN (
                                                           5, 6, 7 )
                                                           AND EXISTS ( SELECT
                                                              1
                                                              FROM
                                                              RFOperations_02022017.Hybris.orders ro
                                                              WHERE
                                                              ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= DATEADD(MONTH,
                                                              -18,
                                                              '2017-02-28') )--Other Status with 18 months orders
                                                         )
                                                    ) )
                  ) Hybris ON RFO.HybrisUserPk = Hybris.PK
WHERE   Hybris.pk IS NULL 

-- RFO HAS NO DEFAULT AND HYBRIS  HAVING DEFAULT ADDRESSES .

SELECT  *
FROM    ( SELECT    ac.AccountId ,
                    u.pk ,
                    u.p_defaultshipmentaddress ,
                    CONCAT(ac.FirstName, SPACE(1), ac.MiddleNAme, SPACE(1),
                           ac.LastName) RFOName ,
                    aca.AddressID ,
                    ad.AddressLine1 ,
                    ad.Locale ,
                    PostalCode ,
                    ad.AddressTypeID [AddresType 2=Shipping] ,
                    ad.IsDefault [RFODefault]
          FROM      RFOperations_02022017.RFO_Accounts.AccountContacts ac
                    JOIN RFOperations_02022017.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
                    JOIN RFOperations_02022017.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
                                                              AND ad.AddressTypeID = 3
                    JOIN Hybris.dbo.users u ON u.p_customerid = CAST(ac.AccountID AS NVARCHAR(225))
          WHERE     ad.IsDefault = 1
        ) RFO
        RIGHT JOIN ( SELECT u.p_customerid ,
                            u.pk [UserPK] ,
                            u.p_name ,
                            u.p_defaultshipmentaddress ,
                            u.p_defaultpaymentaddress ,
                            t.Code [CustomerTypes] ,
                            u.pk
                     FROM   Hybris.dbo.users u
                            JOIN hybris.dbo.enumerationvalues t ON t.pk = u.p_type
                     WHERE  EXISTS ( SELECT 1
                                     FROM   Hybris.dbo.addresses a
                                     WHERE  a.pk = u.p_defaultpaymentaddress
                                            AND a.OwnerPkString = u.pk
                                            AND p_billingaddress = 1 ) --AND u.p_customerid='2599101'
                            AND t.Code IN ( 'CONSULTANT', 'PREFERRED',
                                            'RETAIL' )
                   ) Hybris ON RFO.PK = Hybris.PK
WHERE   RFO.PK IS NULL 




--- Checking whether all the default Billing  Address exist in Address table  with that Users or not.
SELECT  u.p_customerid ,
        u.pk [UserPK] ,
        u.p_name ,
        u.p_defaultpaymentaddress ,
        t.Code [CustomerTypes],
		s.Code[AccountStatus]
FROM    Hybris.dbo.users u
        JOIN hybris.dbo.enumerationvalues t ON t.pk = u.p_type
		JOIN Hybris.dbo.enumerationvalues s ON s.pk=u.p_accountstatus
WHERE   NOT EXISTS ( SELECT 1
                     FROM   Hybris.dbo.addresses a
                     WHERE  a.pk = u.p_defaultpaymentaddress
                            AND a.OwnerPkString = u.pk
                            AND p_billingaddress = 1 )
        AND t.Code IN ( 'CONSULTANT', 'PREFERRED', 'RETAIL' )
        AND u.p_defaultpaymentaddress IS NOT NULL









--AddressTypeID	Code	Name
--1	1	Main
--2	2	Shipping
--3	3	Billing



SELECT  ac.AccountId ,
        u.pk ,
        u.p_defaultshipmentaddress ,
        CONCAT(ac.FirstName, SPACE(1), ac.MiddleNAme, SPACE(1), ac.LastName) RFOName ,
        aca.AddressID ,
        ad.AddressLine1 ,
        ad.Locale ,
        PostalCode ,
        ad.AddressTypeID [AddresType 2=Shipping] ,
        ad.IsDefault [RFODefault]
FROM    RFOperations_02022017.RFO_Accounts.AccountContacts ac
        JOIN RFOperations_02022017.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
        JOIN RFOperations_02022017.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
                                                       AND ad.AddressTypeID = 2 -- Shipping Addresstype
        JOIN Hybris.dbo.users u ON u.p_customerid = CAST(ac.AccountID AS NVARCHAR(225))
                                   AND ac.AccountId = 2599101
WHERE   ad.IsDefault = 1



SELECT  u.p_customerid ,
        u.pk [UserPK] ,
        u.p_name ,
        u.p_defaultshipmentaddress ,
        u.p_defaultpaymentaddress ,
        t.Code [CustomerTypes] ,
        u.pk
FROM    Hybris.dbo.users u
        JOIN hybris.dbo.enumerationvalues t ON t.pk = u.p_type
WHERE   EXISTS ( SELECT 1
                 FROM   Hybris.dbo.addresses a
                 WHERE  a.pk = u.p_defaultshipmentaddress
                        AND a.OwnerPkString = u.pk
                        AND p_shippingaddress = 1 )
        AND u.p_customerid = '2599101'
        AND t.Code IN ( 'CONSULTANT', 'PREFERRED', 'RETAIL' )
               

--########################################################################
--					ACCOUNT DEFAULT SHIPPING  ADDRESSES
--########################################################################


-- Checking Whether Accounts Having  duplicate Default Shipping Addresses
SELECT  ac.AccountId ,
        COUNT(ac.AccountId) [Repeat]
FROM    RFOperations_02022017.RFO_Accounts.AccountContacts ac
        JOIN RFOperations_02022017.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
                                                          -- AND AC.ACCOUNTID = 3898059
                                                              AND ac.AccountContactTypeId = 1
        JOIN RFOperations_02022017.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
                                                       AND ad.AddressTypeID = 2
        JOIN Hybris.dbo.users u ON u.p_customerid = CAST(ac.AccountID AS NVARCHAR(225))
WHERE   ad.IsDefault = 1
GROUP BY ac.AccountId
HAVING  COUNT(ac.AccountId) > 1;





WITH    Shipping
          AS ( SELECT   ac.AccountId ,
                        u.p_defaultshipmentaddress ,
                        CONCAT(ac.FirstName, SPACE(1), ac.MiddleNAme, SPACE(1),
                               ac.LastName) RFOName ,
                        aca.AddressID ,
                        ad.AddressLine1 ,
                        ad.Locale ,
                        PostalCode ,
                        ad.AddressTypeID [AddresType 2=Shipping] ,
                        ad.IsDefault [RFODefault] ,
                        AccountContactTypeId ,
                        u.PK AS UserPK ,
                        ad.ServerModifiedDate ,
                        ROW_NUMBER() OVER ( PARTITION BY AccountId ORDER BY ad.ServerModifiedDate DESC ) RowNum
               FROM     RFOperations_02022017.RFO_Accounts.AccountContacts ac
                        JOIN Hybris.dbo.users u ON u.p_customerid = CAST(ac.AccountID AS NVARCHAR(225)) --AND accountid=1144861
                        JOIN RFOperations_02022017.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
                        JOIN RFOperations_02022017.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
                                                              AND ad.AddressTypeID = 2
               WHERE    ad.IsDefault = 1
			    AND EXISTS ( SELECT 1
                                 FROM   RFOperations_02022017.RFO_Accounts.AccountBase ab
                                        JOIN RFOperations_02022017.RFO_Accounts.AccountRF rf ON ab.AccountID = rf.AccountID
                                 WHERE  ab.accountID = ac.AccountId
                                        AND ab.AccountNumber <> 'AnonymousRetailAccount'
                                        AND ( ab.AccountStatusID IN ( 1, 2, 8,
                                                              9 ) -- Active,Inactive,SoftVoluntary,SoftInvoluntary
                                              OR ( ab.AccountStatusID = 10
                                                   AND EXISTS ( SELECT
                                                              1
                                                              FROM
                                                              RFOperations_02022017.Hybris.orders ro
                                                              WHERE
                                                              ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= DATEADD(MONTH,
                                                              -6, '2017-02-28') ) --HardTerm with 6 months order
                                                 )
                                              OR ( ab.AccountStatusID IN ( 5,
                                                              6, 7 )
                                                   AND EXISTS ( SELECT
                                                              1
                                                              FROM
                                                              RFOperations_02022017.Hybris.orders ro
                                                              WHERE
                                                              ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= DATEADD(MONTH,
                                                              -18,
                                                              '2017-02-28') )--Other Status with 18 months orders
                                                 )
                                            ) )
             ),
        shippingDefault
          AS ( SELECT   *
               FROM     Shipping
               WHERE    RowNum = 1
             )
    SELECT  *
    FROM    shippingDefault RFO
            JOIN ( SELECT   u.p_customerid ,
                            u.pk [UserPK] ,
                            u.p_name ,
                            u.p_defaultshipmentaddress ,
                            t.Code [CustomerTypes] ,
                            ada.p_streetname ,
                            ada.p_postalcode ,
                            ada.p_town
                   FROM     Hybris.dbo.users u
                            JOIN Hybris.dbo.addresses ada ON ada.pk = u.p_defaultshipmentaddress
                                                             AND p_shippingaddress = 1
                            JOIN hybris.dbo.enumerationvalues t ON t.pk = u.p_type
                   WHERE    EXISTS ( SELECT 1
                                     FROM   Hybris.dbo.addresses a
                                     WHERE  a.pk = u.p_defaultshipmentaddress-- AND u.p_customerid='1144861'
                                            AND a.OwnerPkString = u.pk
                                            AND p_shippingaddress = 1 )
                            AND t.Code IN ( 'CONSULTANT', 'PREFERRED',
                                            'RETAIL' )  AND EXISTS ( SELECT 1
                                 FROM   RFOperations_02022017.RFO_Accounts.AccountBase ab
                                        JOIN RFOperations_02022017.RFO_Accounts.AccountRF rf ON ab.AccountID = rf.AccountID
                                 WHERE  ab.accountID = CAST(u.p_customerid AS BIGINT)
                                        AND ab.AccountNumber <> 'AnonymousRetailAccount'
                                        AND ( ab.AccountStatusID IN ( 1, 2, 8,
                                                              9 ) -- Active,Inactive,SoftVoluntary,SoftInvoluntary
                                              OR ( ab.AccountStatusID = 10
                                                   AND EXISTS ( SELECT
                                                              1
                                                              FROM
                                                              RFOperations_02022017.Hybris.orders ro
                                                              WHERE
                                                              ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= DATEADD(MONTH,
                                                              -6, '2017-02-28') ) --HardTerm with 6 months order
                                                 )
                                              OR ( ab.AccountStatusID IN ( 5,
                                                              6, 7 )
                                                   AND EXISTS ( SELECT
                                                              1
                                                              FROM
                                                              RFOperations_02022017.Hybris.orders ro
                                                              WHERE
                                                              ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= DATEADD(MONTH,
                                                              -18,
                                                              '2017-02-28') )--Other Status with 18 months orders
                                                 )
                                            ) )
                 ) Hybris ON Hybris.UserPK = RFO.userPk
    WHERE   ( RFO.AddressLine1 <> Hybris.p_streetname )



-- RFO HAS DEFAULT AND HYBRIS NOT HAVING DEFAULT ADDRESSES .

;
WITH    Shipping
          AS ( SELECT   ac.AccountId ,
                        u.p_defaultshipmentaddress ,
                        CONCAT(ac.FirstName, SPACE(1), ac.MiddleNAme, SPACE(1),
                               ac.LastName) RFOName ,
                        aca.AddressID ,
                        ad.AddressLine1 ,
                        ad.Locale ,
                        PostalCode ,
                        ad.AddressTypeID [AddresType 2=Shipping] ,
                        ad.IsDefault [RFODefault] ,
                        AccountContactTypeId ,
                        u.PK AS UserPK ,
                        ad.ServerModifiedDate ,
                        ROW_NUMBER() OVER ( PARTITION BY AccountId ORDER BY ad.ServerModifiedDate DESC ) RowNum
               FROM     RFOperations_02022017.RFO_Accounts.AccountContacts ac
                        JOIN Hybris.dbo.users u ON u.p_customerid = CAST(ac.AccountID AS NVARCHAR(225)) --AND accountid=1144861
                        JOIN RFOperations_02022017.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
                        JOIN RFOperations_02022017.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
                                                              AND ad.AddressTypeID = 2
               WHERE    ad.IsDefault = 1
             ),
        shippingDefault
          AS ( SELECT   *
               FROM     Shipping
               WHERE    RowNum = 1
             )
    SELECT  *
    FROM    shippingDefault RFO
            LEFT JOIN ( SELECT  u.p_customerid ,
                                u.pk [UserPK] ,
                                u.p_name ,
                                u.p_defaultshipmentaddress ,
                                u.p_defaultpaymentaddress ,
                                t.Code [CustomerTypes] ,
                                u.pk
                        FROM    Hybris.dbo.users u
                                JOIN hybris.dbo.enumerationvalues t ON t.pk = u.p_type
                        WHERE   EXISTS ( SELECT 1
                                         FROM   Hybris.dbo.addresses a
                                         WHERE  a.pk = u.p_defaultshipmentaddress
                                                AND a.OwnerPkString = u.pk
                                                AND p_shippingaddress = 1 )
                                AND u.p_customerid = '2599101'
                                AND t.Code IN ( 'CONSULTANT', 'PREFERRED',
                                                'RETAIL' )
                      ) Hybris ON RFO.PK = Hybris.PK
    WHERE   Hybris.PK IS NULL 

-- RFO HAS DEFAULT AND HYBRIS NOT HAVING DEFAULT ADDRESSES .

;
WITH    Shipping
          AS ( SELECT   ac.AccountId ,
                        u.p_defaultshipmentaddress ,
                        CONCAT(ac.FirstName, SPACE(1), ac.MiddleNAme, SPACE(1),
                               ac.LastName) RFOName ,
                        aca.AddressID ,
                        ad.AddressLine1 ,
                        ad.Locale ,
                        PostalCode ,
                        ad.AddressTypeID [AddresType 2=Shipping] ,
                        ad.IsDefault [RFODefault] ,
                        AccountContactTypeId ,
                        u.PK AS UserPK ,
                        ad.ServerModifiedDate ,
                        ROW_NUMBER() OVER ( PARTITION BY AccountId ORDER BY ad.ServerModifiedDate DESC ) RowNum
               FROM     RFOperations_02022017.RFO_Accounts.AccountContacts ac
                        JOIN Hybris.dbo.users u ON u.p_customerid = CAST(ac.AccountID AS NVARCHAR(225)) --AND accountid=1144861
                        JOIN RFOperations_02022017.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
                        JOIN RFOperations_02022017.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
                                                              AND ad.AddressTypeID = 2
               WHERE    ad.IsDefault = 1
             ),
        shippingDefault
          AS ( SELECT   *
               FROM     Shipping
               WHERE    RowNum = 1
             )
    SELECT  *
    FROM    shippingDefault RFO
            RIGHT JOIN ( SELECT u.p_customerid ,
                                u.pk [UserPK] ,
                                u.p_name ,
                                u.p_defaultshipmentaddress ,
                                u.p_defaultpaymentaddress ,
                                t.Code [CustomerTypes] ,
                                u.pk
                         FROM   Hybris.dbo.users u
                                JOIN hybris.dbo.enumerationvalues t ON t.pk = u.p_type
                         WHERE  EXISTS ( SELECT 1
                                         FROM   Hybris.dbo.addresses a
                                         WHERE  a.pk = u.p_defaultshipmentaddress
                                                AND a.OwnerPkString = u.pk
                                                AND p_shippingaddress = 1 )
                                AND u.p_customerid = '2599101'
                                AND t.Code IN ( 'CONSULTANT', 'PREFERRED',
                                                'RETAIL' )
                       ) Hybris ON RFO.PK = Hybris.PK
    WHERE   RFO.PK IS NULL 




--- Checking whether all the default shipping  Address exist in Address table  with that Users or not.
SELECT  u.p_customerid ,
        u.pk [UserPK] ,
        u.p_name ,
        u.p_defaultshipmentaddress ,
        t.Code [CustomerTypes]
FROM    Hybris.dbo.users u
        JOIN hybris.dbo.enumerationvalues t ON t.pk = u.p_type
WHERE   NOT EXISTS ( SELECT 1
                     FROM   Hybris.dbo.addresses a
                     WHERE  a.pk = u.p_defaultshipmentaddress
                            AND a.OwnerPkString = u.pk
                            AND p_shippingaddress = 1 )
        AND t.Code IN ( 'CONSULTANT', 'PREFERRED', 'RETAIL' )








--########################################################################
--					ACCOUNT DEFAULT PaymentInfos
--########################################################################





--- CA PAYMENTPROFILE

;
WITH    CAProfile
          AS ( SELECT   ab.AccountID ,
                        cp.NameOnCard ,
                        cp.Token ,
                        cp.ExpMonth ,
                        cp.ExpYear ,
                        u.p_defaultpaymentinfo [DefaultPayment] ,
                        u.p_name [UserNme] ,
                        pp.IsDefault ,
                        ROW_NUMBER() OVER ( PARTITION BY ab.AccountID ORDER BY cp.ServerModifiedDate DESC ) rn
               FROM     RFOperations_02022017.RFO_Accounts.AccountBase ab
                        JOIN hybris.dbo.users u ON u.p_customerid = CAST(ab.AccountID AS NVARCHAR(225))
                        JOIN RFOperations_02022017.RFO_Accounts.PaymentProfiles pp ON pp.AccountID = ab.AccountId
                                                              AND CountryID = 40
                        JOIN RFOperations_02022017.RFO_Accounts.CreditCardProfiles cp ON cp.PaymentProfileID = pp.PaymentProfileID
               WHERE    EXISTS ( SELECT 1
                                 FROM   RFOperations_02022017.RFO_Accounts.Addresses ad
                                 WHERE  ad.AddressID = cp.BillingAddressID )
                        AND pp.IsDefault = 1
             )
    SELECT  *
    FROM    CAProfile a
            LEFT JOIN hybris.dbo.paymentinfos p ON a.DefaultPayment = p.PK
    WHERE   a.Token <> p.p_number







	-- US PAYMENTPROFILES



    
	 
	 
	;
WITH    USprofile
          AS ( SELECT   ab.AccountID ,
                        cp.NameOnCard ,
                        cp.DisplayNumber ,
                        cp.ExpMonth ,
                        cp.ExpYear ,
                        ISNULL(u.p_defaultpaymentinfo, 123) [DefaultPayment] ,
                        u.p_name [UserNme] ,
                        pp.IsDefault ,
                        ROW_NUMBER() OVER ( PARTITION BY ab.AccountID ORDER BY cp.ServerModifiedDate DESC ) rn
               FROM     RFOperations_02022017.RFO_Accounts.AccountBase ab
                        JOIN hybris.dbo.users u ON u.p_customerid = CAST(ab.AccountID AS NVARCHAR(225))
                        JOIN RFOperations_02022017.RFO_Accounts.PaymentProfiles pp ON pp.AccountID = ab.AccountId
                                                              AND ab.CountryID = 236
                        JOIN RFOperations_02022017.RFO_Accounts.CreditCardProfiles cp ON cp.PaymentProfileID = pp.PaymentProfileID
               WHERE    EXISTS ( SELECT 1
                                 FROM   RFOperations_02022017.RFO_Accounts.Addresses ad
                                 WHERE  ad.AddressID = cp.BillingAddressID )
             ),
        USDefault
          AS ( SELECT   *
               FROM     usProfile
               WHERE    rn = 1
             )
    SELECT  *
    FROM    USDefault a
            LEFT JOIN hybris.dbo.paymentinfos p ON a.DefaultPayment = p.PK
    WHERE   a.DisplayNumber = p.p_number
