




DECLARE @OrderNumber NVARCHAR(255)= '1200002689'

SELECT  'OrderDetails' ,p_customerid[AccountID],
        p_customerid [AccountID] ,
        o.p_ordertype [OrderTyep] ,
        s.Code [OrderStatu] ,
        o.p_code [orderNumebr] ,
        m.p_code [DeliveryMode] ,
        o.*
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.orders o ON o.p_user = u.PK
        LEFT JOIN Hybris.dbo.enumerationvalues s ON s.pk = o.p_status
        LEFT JOIN Hybris.dbo.deliverymodes m ON m.pk = o.p_deliverymode
WHERE   o.p_code = @OrderNumber

SELECT  'Order_Entries' ,
        p_customerid [AccountID] ,
        o.p_code [orderNumebr] ,
        oe.*
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.orders o ON o.p_user = u.PK
        LEFT JOIN Hybris.dbo.orderentries oe ON oe.p_order = o.pk
WHERE   o.p_code = @OrderNumber


SELECT  'OrderPayments' ,
        p_customerid [AccountID] ,
        p.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.orders o ON o.p_user = u.PK
        LEFT JOIN Hybris.dbo.paymentinfos p ON p.pk = o.p_paymentinfo
WHERE   o.p_code = @OrderNumber


SELECT  'OrderPayments_PaymentTransaction' ,
        o.p_code [orderNumber] ,
        pt.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.orders o ON o.p_user = u.PK
        LEFT JOIN Hybris.dbo.paymentinfos p ON p.pk = o.p_paymentinfo
        LEFT JOIN Hybris.dbo.paymenttransactions pt ON pt.p_order = o.pk
WHERE   o.p_code = @OrderNumber


SELECT  'OrderPayments_PaymentTransaction_Entries' ,
        o.p_code [orderNumber] ,
        pte.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.orders o ON o.p_user = u.PK
        LEFT JOIN Hybris.dbo.paymentinfos p ON p.pk = o.p_paymentinfo
        LEFT JOIN Hybris.dbo.paymenttransactions pt ON pt.p_order = o.pk
        LEFT JOIN Hybris.dbo.paymnttrnsctentries pte ON pte.p_paymenttransaction = pt.PK
WHERE   o.p_code = @OrderNumber


SELECT  'Order_Payments_Addresses' ,
        p_customerid [AccountID] ,
        ad.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.orders o ON o.p_user = u.PK
        LEFT JOIN Hybris.dbo.paymentinfos p ON p.pk = o.p_paymentinfo
        LEFT JOIN Hybris.dbo.addresses ad ON ad.pk = p.p_billingaddress
WHERE   o.p_code = @OrderNumber





SELECT  'Order_Shipping_Addresses' ,
        p_customerid [AccountID] ,
        ad.*
FROM    Hybris.dbo.users u
        LEFT JOIN Hybris.dbo.orders o ON o.p_user = u.PK
        LEFT JOIN Hybris.dbo.addresses ad ON o.p_deliveryaddress = ad.pk
WHERE   o.p_code = @OrderNumber
        AND ad.p_shippingaddress = 1



SELECT  'Order_Consignments' ,
        p_customerid [AccountID] ,
        o.p_code ,
        oc.*
FROM    Hybris.dbo.users u
        LEFT  JOIN Hybris.dbo.orders o ON o.p_user = u.PK
        LEFT JOIN Hybris.dbo.consignments oc ON oc.p_order = o.pk
WHERE   o.p_code = @OrderNumber




SELECT TOP 1
        'Order_Consignments_Entries' ,
        p_customerid [AccountID] ,
        o.p_code ,
        oce.*
FROM    Hybris.dbo.users u
        LEFT  JOIN Hybris.dbo.orders o ON o.p_user = u.PK
        LEFT JOIN Hybris.dbo.consignments oc ON oc.p_order = o.pk
        LEFT JOIN Hybris.dbo.consignmententries oce ON oce.p_consignment = oc.pk
WHERE   o.p_code = @OrderNumber


SELECT TOP 1
        'Order_Consignments_Entries' ,
        p_customerid [AccountID] ,
        o.p_code ,
        oce.*
FROM    Hybris.dbo.users u
        LEFT  JOIN Hybris.dbo.orders o ON o.p_user = u.PK
        LEFT JOIN Hybris.dbo.consignments oc ON oc.p_order = o.pk
        LEFT JOIN Hybris.dbo.consignmententries oce ON oce.p_consignment = oc.pk
WHERE   o.p_code = @OrderNumber        