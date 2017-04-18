
--  rnftestpc@mailinator.com , 


DECLARE @Email NVARCHAR(225)= 'rftestapr8con2@mailinator.com' --,@CID NVARCHAR(255) ='0007104760'

SELECT  'AccountLevel' ,
        CASE t.Code
          WHEN 'CONSULTANT' THEN con.SponsorID
          ELSE pc.SponsorID
        END AS SponSorID ,
		u.pk,
        t.Code [AccountType] ,
        s.Code [AccountStatus] ,
        co.p_isocode [country] ,
        cu.p_isocode [Currency] ,
        p_uid [Email] ,
        p_customerid [AccountID] ,
        u.p_accountnumber [AccountNumber] ,
        p_enrollmentdate [EnrollmentDate] ,
        p_softterminateddate [SofterminationDAte] ,
        p_hardterminateddate [HardTerminationDate] ,
        u.p_active ,
        p_name [Name],
		g.Code[Gender],
		u.p_dateofbirth,
		p_defaultpaymentaddress,
		p_defaultpaymentinfo,
		p_defaultshipmentaddress,u.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.countries co ON co.pk = u.p_country
        LEFT JOIN Hybris.dbo.currencies cu ON cu.pk = u.p_sessioncurrency
        LEFT JOIN Hybris.dbo.enumerationvalues t ON t.pk = u.p_type
        LEFT JOIN Hybris.dbo.enumerationvalues s ON s.pk = u.p_accountstatus
        LEFT JOIN ( SELECT  pg.SourcePK ,
                            su.p_customerid [SponsorID]
                    FROM    Hybris.dbo.pgrels pg
                            JOIN Hybris.dbo.users su ON su.p_defaultb2bunit = pg.TargetPK
                            JOIN Hybris.dbo.enumerationvalues v ON su.p_type = v.PK
                                                              AND v.Code = 'Consultant'
                  ) Con ON con.SourcePK = u.p_defaultb2bunit
        LEFT JOIN ( SELECT  su.p_defaultb2bunit ,
                            su.p_customerid [SponsorID]
                    FROM    Hybris.dbo.users su
                            JOIN Hybris.dbo.enumerationvalues v ON su.p_type = v.PK
                                                              AND v.Code = 'Consultant'
                  ) PC ON PC.p_defaultb2bunit = u.p_defaultb2bunit
				  LEFT JOIN Hybris.dbo.enumerationvalues g ON g.pk=u.p_gender
				  
WHERE   u.p_uid = @Email  
         

SELECT  'AccountPaymenntProfile' ,
u.p_customerid,
        v.Code [CreditCard] ,
        p.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.paymentinfos p ON p.p_user = u.PK
        LEFT JOIN Hybris.dbo.enumerationvalues v ON v.pk = p.p_type
WHERE   u.p_uid = @Email 





SELECT  'Account PaymentAddress By Payment' ,
        a.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.paymentinfos p ON p.p_user = u.PK
        LEFT JOIN Hybris.dbo.addresses a ON a.pk = p.p_billingaddress
WHERE   p_uid = @Email 


SELECT  'AccountContact Address' ,
        a.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.addresses a ON a.OwnerPkString = u.pk
WHERE   p_uid = @Email
        AND a.p_contactaddress = 1


SELECT  'AccountShipping Addresses' ,
        a.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.addresses a ON a.OwnerPkString = u.pk
WHERE   p_uid = @Email
        AND a.p_shippingaddress = 1



		SELECT 'DefaultPaymentInfos',p.* FROM Hybris.dbo.users u 
		LEFT JOIN Hybris.dbo.paymentinfos p ON p.pk=u.p_defaultpaymentinfo
		WHERE u.p_uid=@Email 



		SELECT 'DefaultBillingAddresses',ad.* FROM Hybris.dbo.users u 
		LEFT JOIN Hybris.dbo.addresses ad ON ad.pk=u.p_defaultpaymentaddress
		WHERE u.p_uid=@Email 

