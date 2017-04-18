

DECLARE @Email NVARCHAR(225)= 'pcprodus2@mailinator.com'
SELECT  'Carts' ,
        c.p_ordertype ,
		m.p_code[DeliveryMode],
        c.p_code [Template] ,
		v.Code[templateStatus],
		cjs.Code[CronJobStatus],
		cj.p_active,
        c.*
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.PK
		LEFT JOIN Hybris.dbo.deliverymodes m ON m.pk=c.p_deliverymode
		LEFT JOIN Hybris.dbo.enumerationvalues v ON v.pk=c.p_status
		LEFT JOIN Hybris.dbo.enumerationvalues cjs ON cjs.pk=cj.p_status
WHERE   u.p_uid = @Email
        AND cj.p_code NOT LIKE 'RC%'

		
SELECT  'Carts Entries' ,
        c.p_ordertype ,
        c.p_code [Template] ,
        ce.*
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.PK
        JOIN Hybris.dbo.cartentries ce ON ce.p_order = c.pk
WHERE   u.p_uid = @Email
        AND cj.p_code NOT LIKE 'RC%'

SELECT  'Pulse Template' ,
        s.p_id [PulseTemplate] ,
        s.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.subscriptions s ON s.p_customerid = u.p_customerid
WHERE   u.p_uid = @Email


		
SELECT  'Pulse Order Details' ,
        s.p_id [PulseTemplate] ,
        o.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.subscriptions s ON s.p_customerid = u.p_customerid
        LEFT JOIN Hybris.dbo.orders o ON o.pk = s.p_order
WHERE   u.p_uid = @Email


		
SELECT  'Pulse OrderItem Details' ,
        s.p_id [PulseTemplate] ,
        oe.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.subscriptions s ON s.p_customerid = u.p_customerid
        LEFT JOIN Hybris.dbo.orders o ON o.pk = s.p_order
        LEFT JOIN Hybris.dbo.orderentries oe ON oe.p_order = o.pk
WHERE   u.p_uid = @Email



SELECT  'CartsPaymentInfos' ,
        c.p_ordertype ,
        c.p_code [Template] ,
        p.*
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.PK
		JOIN Hybris.dbo.paymentinfos p ON p.pk=c.p_paymentinfo
WHERE   u.p_uid = @Email
        AND cj.p_code NOT LIKE 'RC%'




SELECT  'CartsPaymentAddresses' ,
        c.p_ordertype ,
        c.p_code [Template] ,
        ad.*
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.PK
		JOIN Hybris.dbo.paymentinfos p ON p.pk=c.p_paymentinfo
		JOIN Hybris.dbo.addresses ad ON ad.pk=c.p_paymentaddress
WHERE   u.p_uid = @Email
        AND cj.p_code NOT LIKE 'RC%'




SELECT  'CartsShippingAddress' ,
        c.p_ordertype ,
        c.p_code [Template] ,
        ad.*
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.carts c ON c.p_user = u.PK
        JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.PK
		JOIN Hybris.dbo.paymentinfos p ON p.pk=c.p_paymentinfo
		JOIN Hybris.dbo.addresses ad ON ad.pk=c.p_deliveryaddress
WHERE   u.p_uid = @Email
        AND cj.p_code NOT LIKE 'RC%'




SELECT  'CartsPaymentInfos' ,
        c.p_ordertype ,
        p.*
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.carts c ON c.p_user = u.PK
		JOIN Hybris.dbo.paymentinfos p ON p.OwnerPkString=c.pk
WHERE   u.p_uid = @Email-- AND ISNUMERIC(c.p_code)=1