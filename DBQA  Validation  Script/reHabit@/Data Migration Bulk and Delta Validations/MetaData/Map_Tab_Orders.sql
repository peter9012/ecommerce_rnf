USE DM_QA
GO 

IF OBJECT_ID('dbqa.Map_tab') IS NULL 
CREATE TABLE  dbqa.Map_tab
(MapID INT IDENTITY(1,1) PRIMARY KEY,
SourceObject NVARCHAR(100),--RFOperations.dbo.Accounts
SourceColumn NVARCHAR(50),--FirstName
SourceDataTypes NVARCHAR(50),--Nvarchar(20)
SourceRef NVARCHAR(MAX)-- Direct or Transform
,TargetObject NVARCHAR(100),--Hybris.dbo.Users
TargetColumn NVARCHAR(50),--P_FirstName
TargetDataTypes NVARCHAR(50),-- Nvarchar(20)
[Key] NVARCHAR(50),--AccountNumber
TargetRef NVARCHAR(MAX),--ComposedTypes
Flag NVARCHAR(10), --C2C or Default or Manual 
[Owner] NVARCHAR(50),
[SQL Stmt] NVARCHAR(Max)--'Select * from TableA a join TabelB b on a.ID=b.ID Where a.Fields<>b.Fields'
)

--********************************************************************************************
--												Orders
--********************************************************************************************

INSERT INTO dbqa.Map_tab
        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
VALUES 

/* Orders Header Level */

(N'RFOperations.Hybris.Orders' ,N'OrderNumber',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.orders' ,N'p_code' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'Key' ,N'Orders',N'') 
,--p_code
(N'RFOperations.Hybris.Orders' ,N'AutoShipID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_associatedOrders' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.Carts' , N'ref' ,N'Orders',N'') 
,--p_associatedOrders

(N'RFOperations.Hybris.Orders' ,N'OrderStatusID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_status' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumerationvalues' , N'ref' ,N'Orders',N'') 
,--p_associatedOrders
(N'RFOperations.Hybris.Orders' ,N'OrderTypeID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_type' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumerationvalues' , N'ref' ,N'Orders',N'') 
,--p_type
(N'RFOperations.Hybris.Orders' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_country' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.countries' , N'ref' ,N'Orders',N'') 
,--p_country
(N'RFOperations.Hybris.Orders' ,N'CurrencyID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_currency' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.Currencies' , N'ref' ,N'Orders',N'') 
,--p_currency
(N'RFOperations.Hybris.Orders' ,N'ConsultantID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_ordersponsorid' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.users' , N'ref' ,N'Orders',N'') 
,--p_ordersponsorid
(N'RFOperations.Hybris.Orders' ,N'AccountID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_userpk' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.users' , N'ref' ,N'Orders',N'') 
,--p_userpk
(N'RFOperations.Hybris.Orders' ,N'CompletionDate',N'datetime' ,N' ' ,N'Hybris.dbo.orders' ,N'createdTS' ,N'datetime' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--createdTS
(N'RFOperations.Hybris.Orders' ,N'CommissionDate',N'datetime' ,N' ' ,N'Hybris.dbo.orders' ,N'p_commissiondate' ,N'datetime' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_commissiondate
(N'RFOperations.Hybris.Orders' ,N'ModifiedDate',N'datetime' ,N' ' ,N'Hybris.dbo.orders' ,N'modifiedTS' ,N'datetime' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--modifiedTS
(N'RFOperations.Hybris.Orders' ,N'SubTotal',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_subtotal' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_subtotal
(N'RFOperations.Hybris.Orders' ,N'Total',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totalprice' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_totalprice
(N'RFOperations.Hybris.Orders' ,N'TotalTax',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totaltax' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_totaltax
(N'RFOperations.Hybris.Orders' ,N'TotalDiscount',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totaldiscount' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_totaldiscount
(N'RFOperations.Hybris.Orders' ,N'CV',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_cv' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_cv
(N'RFOperations.Hybris.Orders' ,N'QV',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totalsv' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_totalsv
(N'RFOperations.Hybris.Orders' ,N'TaxExempt',N'bit' ,N' ' ,N'Hybris.dbo.orders' ,N'isTaxExempt' ,N'boolean' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--isTaxExempt
(N'RFOperations.Hybris.Orders' ,N'donotship',N'bit' ,N' ' ,N'Hybris.dbo.orders' ,N'p_donotship' ,N'boolean' ,N'OrderNumber' ,N' ' , N'c2c' ,N'Orders',N'') 
,--p_donotship
(N'RFOperations.Hybris.OrderShipment' ,N'ShippingCost',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_deliverycost' ,N'decimal(30,8)' ,N'OrderNumber' ,N' Sum with handlingCost' , N'c2c' ,N'Orders',N'') 
,--p_deliverycost
(N'RFOperations.Hybris.OrderShipment' ,N'TaxOnShippingCost',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_taxonshippingcost' ,N'decimal(30,8)' ,N'OrderNumber' ,N' Sum with TaxOnHandling' , N'c2c' ,N'Orders',N'') 
,--p_deliverycost


/* 
 Hybris system default Orders attributes will be here .
*/



/* Order Items Level */

(N'RFOperations.Hybris.OrderItems' ,N'OrderId',N'bigint' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_order' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.orders ' , N'key' ,N'OrderItems',N'') 
,--p_order
(N'RFOperations.Hybris.OrderItems' ,N'LineItemNo',N'int' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_entrynumber' ,N'int' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_entrynumber
(N'RFOperations.Hybris.OrderItems' ,N'ProductID',N'bigint' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_product' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.Products ' , N'ref' ,N'OrderItems',N'') 
,--p_product
(N'RFOperations.Hybris.OrderItems' ,N'Quantity',N'int' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_quantity' ,N'int' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_quantity
(N'RFOperations.Hybris.OrderItems' ,N'BasePrice',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_baseprice' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_baseprice
(N'RFOperations.Hybris.OrderItems' ,N'TotalPrice',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_totalprice' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_totalprice
(N'RFOperations.Hybris.OrderItems' ,N'TotalTax',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_taxvaluesinternal' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_taxvaluesinternal
(N'RFOperations.Hybris.OrderItems' ,N'CV',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_cv' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_cv
(N'RFOperations.Hybris.OrderItems' ,N'QV',N'money' ,N' ' ,N'Hybris.dbo.Orderenries' ,N'p_qv' ,N'double' ,N'OrderNumber' ,N' ' , N'c2c' ,N'OrderItems',N'') 
,--p_qv


/* 
 Hybris system default Order Items  attributes will be here .
*/



/* Order Payments Level */

(N'RFOpetaions.Hybris.OrderPayment' ,N'OrderPaymentID',N'bigint' ,N' ' ,N'Hybris.dbo.paymentInfos' ,N'p_code' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'key' ,N'OrderPayment',N'') 
,--p_code
(N' ' ,N' ',N'' ,N'Hybris Reference Attribute' ,N'Hybris.dbo.paymentInfos' ,N'OwnerpkString' ,N'bigint' ,N'OrderNumber' ,N'hybris.dbo.orders' , N'key' ,N'OrderPayment',N'') 
,--Ownerpkstring
(N'RFOpetaions.Hybris.OrderPayment' ,N'AmountTobeAuthorized',N'money' ,N'Hybris Reference Attribute ' ,N'Hybris.dbo.paymentInfos' ,N'p_plannedAmount' ,N'decimal(30,8)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--p_plannedAmount
(N'RFOpetaions.Hybris.OrderPayment' ,N'ExpYear',N'bigint' ,N' ' ,N'Hybris.dbo.paymentInfos' ,N'p_validtoyear' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--p_validtoyear
(N'RFOpetaions.Hybris.OrderPayment' ,N'Expmonth',N'bigint' ,N'  ' ,N'Hybris.dbo.paymentInfos' ,N'p_validtomonth' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--p_validtomonth
(N'RFOpetaions.Hybris.OrderPayment' ,N'VendorID',N'bigint' ,N'Reference.CreditCardTypes' ,N'Hybris.dbo.paymentInfos' ,N'p_type' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enusValue' , N'ref' ,N'OrderPayment',N'') 
,--p_type
(N'RFOpetaions.Hybris.OrderPayment' ,N'paymentprovider',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.paymentTrasactions' ,N'paymentprovider' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--paymentprovider
(N'RodanFieldsLive.dbo.OrderPayments' ,N'AccountNumber',N'nvarchar' ,N'' ,N'Hybris.dbo.paymentInfos' ,N'p_number' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--p_number
(N'RodanFieldsLive.dbo.OrderPayments' ,N'BillingName',N'nvarchar' ,N'' ,N'Hybris.dbo.paymentInfos' ,N'p_ccowner' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPayment',N'') 
,--p_ccowner


/* 
 Hybris system default Order payments  attributes will be here .
*/



/* Order BillingAddress Level */

(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_country' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.countries' , N'ref' ,N'OrderPaymentAddress',N'') 
,--p_country
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'FirstName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_firstname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_firstname
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'LastName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_lastname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_lastname
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'Address1',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_streetname
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'AddressLine2',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetNumber' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_streetNumber
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'PostalCode',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_postalcode' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_postalcode
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'Locale',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_town' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_town
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'Region',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_region' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.regions' , N'ref' ,N'OrderPaymentAddress',N'') 
,--p_region
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'Telephone',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_phone1' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_phone1
(N'RFOpetaions.Hybris.OrderBillingAddress' ,N'MiddleName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_middlename' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_middlename
(N'RFOpetaions.RFO_Accounts.Accountcontacts' ,N'BirthDay',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_dateofbirth' ,N'datetime' ,N'OrderNumber' ,N'If 1900 then Null' , N'c2c' ,N'OrderPaymentAddress',N'') 
,--p_dateofbirth
(N'RFOpetaions.RFO_Accounts.Accountcontacts' ,N'GenderID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_gender' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumValues' , N'ref' ,N'OrderPaymentAddress',N'') 
,--p_gender	
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'p_duplicate' ,N'tinyint' ,N'OrderNumber' ,N'Defaulted to 1' , N'def' ,N'OrderPaymentAddress',N'') 
,--p_duplicate	
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'p_billingaddresses' ,N'tinyint' ,N'OrderNumber' ,N'Defaulted to 1' , N'def' ,N'OrderPaymentAddress',N'') 
,--p_billingaddresses	
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'Ownerpkstring' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.paymentinfos' , N'Key' ,N'OrderPaymentAddress',N'') 
,--Ownerpkstring	


/* 
 Hybris system default OrderPaymentAddress  attributes will be here .
*/



/* Order ShippingAddress Level */

(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_country' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.countries' , N'ref' ,N'OrderShippingAddress',N'') 
,--p_country
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'FirstName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_firstname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_firstname
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'LastName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_lastname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_lastname
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'Address1',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetname' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_streetname
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'AddressLine2',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetNumber' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_streetNumber
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'PostalCode',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_postalcode' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_postalcode
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'Locale',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_town' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_town
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'Region',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_region' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.regions' , N'ref' ,N'OrderShippingAddress',N'') 
,--p_region
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'Telephone',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_phone1' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_phone1
(N'RFOpetaions.Hybris.OrderShippingAddress' ,N'MiddleName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_middlename' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_middlename
(N'RFOpetaions.RFO_Accounts.Accountcontacts' ,N'BirthDay',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_dateofbirth' ,N'datetime' ,N'OrderNumber' ,N'If 1900 then Null' , N'c2c' ,N'OrderShippingAddress',N'') 
,--p_dateofbirth
(N'RFOpetaions.RFO_Accounts.Accountcontacts' ,N'GenderID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_gender' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumValues' , N'ref' ,N'OrderShippingAddress',N'') 
,--p_gender	
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'p_duplicate' ,N'tinyint' ,N'OrderNumber' ,N'Defaulted to 1' , N'def' ,N'OrderShippingAddress',N'') 
,--p_duplicate	
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'p_Shippingaddress' ,N'tinyint' ,N'OrderNumber' ,N'Defaulted to 1' , N'def' ,N'OrderShippingAddress',N'') 
,--p_Shippingaddress				
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.addresses' ,N'Ownerpkstring' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.orders' , N'Key' ,N'OrderShippingAddress',N'') 
,--Ownerpkstring	
		


/* 
 Hybris system default Order ShippingAddress  attributes will be here .
*/



/* OrderPaymentTransaction and Entries Level */

(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'OrderPaymentID',N'bigint' ,N' ' ,N'Hybris.dbo.PaymentTransactions' ,N'OwnerPkstring' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.paymentinfos' , N'Key' ,N'OrderPaymentTransaction',N'') 
,--OwnerPkstring
(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'AmountAuthorized',N'money' ,N' ' ,N'Hybris.dbo.PaymentTransactions' ,N'p_plannedAmount' ,N'decimal(30,8)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_plannedAmount
(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'TransactionID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.PaymentTransactions' ,N'p_requestID' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_requestID
(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'ProcessDate',N'datetime' ,N' ' ,N'Hybris.dbo.PaymentTransactionEntries' ,N'p_time' ,N'datetime' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_time
(N'RFOperations.Hybris.OrderPayment' ,N'PaymentProvider',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.PaymentTransactions' ,N'p_paymentprovider' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_paymentprovider
(N'RFOperations.Hybris.OrderPayment' ,N'OrderPaymentTransactionID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.PaymentTransactionEntries' ,N'p_code' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_code
(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'AmountAuthorized',N'money' ,N' ' ,N'Hybris.dbo.PaymentTransactionEntries' ,N'p_amount' ,N'decimal(30,8)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_amount
(N'RFOperations.Hybris.OrderPaymentTransaction' ,N'TransactionID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.PaymentTransactionEntries' ,N'p_requestID' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderPaymentTransaction',N'') 
,--p_requestID
(N'' ,N'',N'' ,N'Hybris Referece Value ' ,N'Hybris.dbo.PaymentTransactionEntries' ,N'p_paymenttransaction' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.PaymentTransaction' , N'Key' ,N'OrderPaymentTransaction',N'') 
,--p_paymenttransaction





/* 
 Hybris system default OrderPaymentTransaction and Entries  attributes will be here .
*/



/* Order Consignments and Entries Level */

(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'Quantity',N'int' ,N' ' ,N'Hybris.dbo.consignmentEntries' ,N'p_quantity' ,N'bigint' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderConsignments',N'') 
,--p_quantity
(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'Quantity',N'int' ,N' ' ,N'Hybris.dbo.consignmentEntries' ,N'p_shippedquantity' ,N'bigint' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderConsignments',N'') 
,--p_shippedquantity
(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'TrackingNumber',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.consignments' ,N'p_trackingid' ,N'nvarchar(255)' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderConsignments',N'') 
,--p_trackingid
(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'ShipDate',N'datetime' ,N' ' ,N'Hybris.dbo.consignments' ,N'p_shippingdate' ,N'datetime' ,N'OrderNumber' ,N'' , N'c2c' ,N'OrderConsignments',N'') 
,--p_shippingdate
(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'OrderID',N'bigint' ,N' ' ,N'Hybris.dbo.consignments' ,N'P_code' ,N'nvarchar(255)' ,N'OrderNumber' ,N'Hybris.dbo.orders' , N'ref' ,N'OrderConsignments',N'') 
,--P_code
(N'RFOperations.Hybris.OrderShipmentPackageItem' ,N'ShipStatus',N'nvarchar(25)' ,N'Reference.ShippingStatus' ,N'Hybris.dbo.consignments' ,N'p_status' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumValues' , N'ref' ,N'OrderConsignments',N'') 
,--p_status
(N'RFOperations.Hybris.OrderShipment' ,N'ShippingMethodID',N'bigint' ,N'Reference.ShippingMethod' ,N'Hybris.dbo.consignments' ,N'p_deliverymode' ,N'bigint' ,N'OrderNumber' ,N'Hybris.dbo.enumValues' , N'ref' ,N'OrderConsignments',N'') 
,--p_deliverymode
(N'RFOperations.Hybris.OrderShipment' ,N'ShippingCarrier',N'bigint' ,N'Reference.ShippingMethod' ,N'Hybris.dbo.consignments' ,N'p_carrier' ,N'nvarchar(255)' ,N'OrderNumber' ,N'Hybris.dbo.enumValues' , N'ref' ,N'OrderConsignments',N'') 
,--p_carrier
(N'' ,N'',N'' ,N'Hybris.Reference Attributes' ,N'Hybris.dbo.consignmentEntries' ,N'p_consignment' ,N'bigint' ,N'OrderNumber' ,N'Hyrbis.dbo.Consignment' , N'key' ,N'OrderConsignments',N'') 
--p_carrier





