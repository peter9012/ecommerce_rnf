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
--												ReturnOrders 
--********************************************************************************************

INSERT INTO dbqa.Map_tab
        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
VALUES 
/* ReturnOrder Header Level */

(N'RFOperations.Hybris.ReturnOrder' ,N'ReturnOrderNumber',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.orders' ,N'p_code' ,N'nvarcahr(255)' ,N'ReturnNumber' ,N'' , N'Key' ,N'ReturnOrder',N'') 
,--p_code
(N'RFOperations.Hybris.ReturnOrder' ,N'OrderNumber',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_parent' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.orders' , N'ref' ,N'ReturnOrder',N'') 
,--p_parent
(N'RFOperations.Hybris.ReturnOrder' ,N'ReturnStatusID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_status' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.enumeratonvalues' , N'ref' ,N'ReturnOrder',N'') 
,--p_status
(N'RFOperations.Hybris.ReturnOrder' ,N'AccountID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_user' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.users' , N'ref' ,N'ReturnOrder',N'') 
,--p_user
(N'RFOperations.Hybris.ReturnOrder' ,N'ConsultantID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_spronsorid' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.users' , N'ref' ,N'ReturnOrder',N'') 
,--p_spronsorid
(N'RFOperations.Hybris.ReturnOrder' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_country' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.countries' , N'ref' ,N'ReturnOrder',N'') 
,--p_country
(N'RFOperations.Hybris.ReturnOrder' ,N'CurrencyID',N'bigint' ,N' ' ,N'Hybris.dbo.orders' ,N'p_currency' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.currencies' , N'ref' ,N'ReturnOrder',N'') 
,--p_currency
(N'RFOperations.Hybris.ReturnOrder' ,N'CompletionDate',N'datetime' ,N' ' ,N'Hybris.dbo.orders' ,N'createdTS' ,N'datetime' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--createdTS
(N'RFOperations.Hybris.ReturnOrder' ,N'CommissionDate',N'datetime' ,N' ' ,N'Hybris.dbo.orders' ,N'p_commissiondate' ,N'datetime' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_commissiondate
(N'RFOperations.Hybris.ReturnOrder' ,N'SubTotal',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_subtotal' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_subtotal
(N'RFOperations.Hybris.ReturnOrder' ,N'Total',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totalprice' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_totalprice
(N'RFOperations.Hybris.ReturnOrder' ,N'TotalTax',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totaltax' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_totaltax
(N'RFOperations.Hybris.ReturnOrder' ,N'TotalDiscount',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totaldiscounts' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_totaldiscounts
(N'RFOperations.Hybris.ReturnOrder' ,N'CV',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_cv' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_cv
(N'RFOperations.Hybris.ReturnOrder' ,N'QV',N'money' ,N' ' ,N'Hybris.dbo.orders' ,N'p_totalsv' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_totalsv
(N'RFOperations.Hybris.ReturnOrder' ,N'DeliveryCost',N'money' ,N' ShippingCost+HandlingCost' ,N'Hybris.dbo.orders' ,N'p_deliverycost' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_deliverycost
(N'RFOperations.Hybris.ReturnOrder' ,N'DeliveryTax',N'money' ,N'ShippingTax+HandlingTax ' ,N'Hybris.dbo.orders' ,N'p_taxondeliverycost' ,N'decimal(30,8)' ,N'ReturnNumber' ,N'Sum with shipping Cost' , N'c2c' ,N'ReturnOrder',N'') 
,--p_taxondeliverycost
(N'RFOperations.Hybris.ReturnOrder' ,N'ReturnNumber',N'nvarchar(255)' ,N' ' ,N'Hybris.dbo.returnrequest' ,N'p_code' ,N'nvarchar(255)' ,N'ReturnNumber' ,N'' , N'key' ,N'ReturnOrder',N'') 
,--p_code- ReturnRequest
(N'RFOperations.Hybris.ReturnOrder' ,N'ReturnNumber',N'nvarchar(255)' ,N' ' ,N'Hybris.dbo.returnrequest' ,N'p_rma' ,N'nvarchar(255)' ,N'ReturnNumber' ,N'' , N'c2c' ,N'ReturnOrder',N'') 
,--p_rma- ReturnRequest
(N'RFOperations.Hybris.ReturnOrder' ,N'OrderID',N'bigint' ,N' ' ,N'Hybris.dbo.returnrequest' ,N'p_order' ,N'nvarchar(255)' ,N'ReturnNumber' ,N'Hybris.dbo.orders' , N'ref' ,N'ReturnOrder',N'') 
,--p_order- ReturnRequest



/* 
 Hybris system default ReturnOrder attributes will be here .
*/

/* ReturnOrder Items Level */

(N'RFOperations.Hybris.ReturnItem' ,N'ExpectedQuantity',N'int' ,N' ' ,N'Hybris.dbo.returnentry' ,N'p_expectedquantity' ,N'int' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_expectedquantity
(N'RFOperations.Hybris.ReturnItem' ,N'CV',N'money' ,N' ' ,N'Hybris.dbo.orderentries' ,N'p_cv' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_cv
(N'RFOperations.Hybris.ReturnItem' ,N'QV',N'money' ,N' ' ,N'Hybris.dbo.orderentries' ,N'p_qv' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_qv
(N'RFOperations.Hybris.ReturnItem' ,N'ReceivedQuantity',N'int' ,N' ' ,N'Hybris.dbo.returnentry' ,N'p_Receivedquantity' ,N'int' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_Receivedquantity
(N'RFOperations.Hybris.ReturnItem' ,N'BasePrice',N'money' ,N' ' ,N'Hybris.dbo.orderentries' ,N'p_baseprice' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_baseprice
(N'RFOperations.Hybris.ReturnItem' ,N'TotalPrice',N'money' ,N' ' ,N'Hybris.dbo.orderentries' ,N'p_totalprice' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_totalprice
(N'RFOperations.Hybris.ReturnItem' ,N'totaltax',N'nvarchar(255)' ,N' '  ,N'Hybris.dbo.orderentries' ,N'p_taxvaluesinternal' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'ref' ,N'ReturnItem',N'') 
,--p_taxvaluesinternal
(N'RFOperations.Hybris.ReturnItem' ,N'ReceivedQuantity',N'int' ,N' '  ,N'Hybris.dbo.orderentries' ,N'p_quantity' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnItem',N'') 
,--p_quantity
(N'RFOperations.Hybris.ReturnItem' ,N'ReturnOrderID',N'bigint' ,N' '  ,N'Hybris.dbo.orderentries' ,N'p_order' ,N'bigint' ,N'ReturnNumber' ,N' ' , N'key' ,N'ReturnItem',N'') 
,--p_order
(N'RFOperations.Hybris.ReturnItem' ,N'ReturnReasonID',N'bigint' ,N' '  ,N'Hybris.dbo.returnentry' ,N'p_reason' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.enumVaue value=RFRefund ' , N'ref' ,N'ReturnItem',N'') 
,--p_reason
(N'RFOperations.Hybris.ReturnItem' ,N'ReturnStatusID',N'bigint' ,N' '  ,N'Hybris.dbo.returnentry' ,N'p_status' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.enumValue Value=ReturnStatus ' , N'ref' ,N'ReturnItem',N'') 
,--p_status	
(N'RFOperations.Hybris.ReturnItem' ,N'ReturnNotes',N'bigint' ,N' '  ,N'Hybris.dbo.returnentry' ,N'p_notes' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.enumValue Value=returnnotes ' , N'ref' ,N'ReturnItem',N'') 
,--p_notes	
(N'RFOperations.Hybris.ReturnItem' ,N'Action',N'nvarchar(255)' ,N' '  ,N'Hybris.dbo.returnentry' ,N'p_action' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.enumvalue value=ReturnAction ' , N'ref' ,N'ReturnItem',N'') 
,--p_action	
(N'RFOperations.Hybris.ReturnItem' ,N'ProductID',N'bigint' ,N' '  ,N'Hybris.dbo.orderentries' ,N'p_product' ,N'bigint' ,N'ReturnNumber' ,N' ' , N'ref' ,N'ReturnItem',N'') 
,--p_product
(N'' ,N'',N'' ,N'Hybris System Attributes '  ,N'Hybris.dbo.Returnentry' ,N'p_returnrequest' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.ReturnRequest' , N'key' ,N'ReturnItem',N'') 
,--p_returnrequest
(N'RFOperations.Hybris.ReturnItem' ,N'OrderItemID',N'bigint' ,N' '  ,N'Hybris.dbo.Returnentry' ,N'p_orderentry' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.orderEntries(orginal order)' , N'ref' ,N'ReturnItem',N'') 
,--p_product




/* 
 Hybris system default ReturnOrder Items attributes will be here .
*/

/* ReturnPayment  Level */

(N'RFOpetions.Hybris.ReturnPayment' ,N'ReturnOrderID',N'bigint' ,N' ' ,N'Hybris.dbo.paymentinfos' ,N'OwnerPkstring' ,N'bigint' ,N'ReturnNumber' ,N' ' , N'Key' ,N'ReturnPayment',N'') 
,--OwnerPkstring
(N'RFOpetions.Hybris.ReturnPayment' ,N'VendorID',N'bigint' ,N' Reference.CreditCardTypes' ,N'Hybris.dbo.paymentinfos' ,N'p_type' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.enumValue' , N'ref' ,N'ReturnPayment',N'') 
,--p_type
(N'RFOpetions.Hybris.ReturnPayment' ,N'ExpYear',N'bigint' ,N' ' ,N'Hybris.dbo.paymentinfos' ,N'p_validtoyear' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_validtoyear
(N'RFOpetions.Hybris.ReturnPayment' ,N'Expmonth',N'bigint' ,N' ' ,N'Hybris.dbo.paymentinfos' ,N'p_validtomonth' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_validtomonth
(N'RFOpetions.Hybris.ReturnPayment' ,N'AmountToBeAuthorized',N'bigint' ,N' ' ,N'Hybris.dbo.paymenttransactions' ,N'p_plannedamount' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_plannedamount
(N'RodanFieldslive.dbo.Orderpayment' ,N'number,Validtomonth,validtoyear',N'nvarchar' ,N' CONCAT(SUBSTRING(number, 1, 6),SUBSTRING(number,( LEN(number) - 3 ), 4),validToMonth, validToYear)' ,N'Hybris.dbo.paymentinfos' ,N'p_subscriptionid' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_subscriptionid
(N'RodanFieldslive.dbo.Orderpayment' ,N'Name',N'nvarchar(255)' ,N'BillingFirstName,LastName ' ,N'Hybris.dbo.paymentinfos' ,N'p_ccowner' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_ccowner
(N'RodanFieldslive.dbo.Orderpayment' ,N'AccountNumber',N'nvarchar(255)' ,N'BillingFirstName,LastName ' ,N'Hybris.dbo.paymentinfos' ,N'p_number' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPayment',N'') 
,--p_number
(N'' ,N'',N'' ,N' Hybris System Attributes' ,N'Hybris.dbo.paymentinfos' ,N'p_user' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.users' , N'ref' ,N'ReturnPayment',N'') 
,--p_user
(N'' ,N'',N'' ,N' Hybris System Attributes' ,N'Hybris.dbo.paymentinfos' ,N'p_duplicate' ,N'tinyint' ,N'ReturnNumber' ,N' Defaulted to 1' , N'def' ,N'ReturnPayment',N'') 
,--p_duplicate						
(N'' ,N'',N'' ,N' Hybris System Attributes' ,N'Hybris.dbo.paymentinfos' ,N'p_original' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.paymentinfos (Orginal Orders)' , N'def' ,N'ReturnPayment',N'') 
,--p_original			


/* 
 Hybris system default ReturnPayment  attributes will be here .
*/



/* ReturnPayment Transactions Level */

(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'ReturnPaymentId',N'bigint' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_code' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'Key' ,N'ReturnPaymentTransactions',N'') 
,--p_code
(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'AmountAuthorized',N'money' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_plannedamount' ,N'decimal(30,8)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_plannedamount
(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'TransactionID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.payementtransactions' ,N'p_requestid' ,N'	nvarchar(255) ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_requestid
(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'AuthorizeType',N'nvarchar(50)' ,N' CASE AuthorizeType
 WHEN CREDIT THEN ACCEPTED
WHEN AUTH_CAPTURE THEN ACCEPTED
 WHEN SALE THEN ACCEPTED
  WHEN VOID THEN ERROR' ,N'Hybris.dbo.payementtransactionentries' ,N'p_transactionStatus' ,N'	bigint ' ,N'ReturnNumber' ,N'Hybris.dbo.enumvalues ' , N'ref' ,N'ReturnPaymentTransactions',N'') 
,--p_transactionStatus						
(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'AuthorizeType',N'nvarchar(50)' ,N' CASE AuthorizeType
WHEN CREDIT THEN SUCCESFULL
WHEN AUTH_CAPTURE THEN SUCCESFULL
WHEN SALE THEN SUCCESFULL
WHEN VOID THEN UNKNOWN_CODE' ,N'Hybris.dbo.payementtransactionentries' ,N'p_transactionStatusdetails' ,N'	bigint ' ,N'ReturnNumber' ,N'Hybris.dbo.enumvalues ' , N'ref' ,N'ReturnPaymentTransactions',N'') 
,--p_transactionStatusdetails	
(N'RFOpetions.Hybris.ReturnPaymentTransaction' ,N'ProcessDate',N'datetime' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_time' ,N'	datetime ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_time	
(N'' ,N'',N'' ,N' Hybris system Attributes' ,N'Hybris.dbo.payementtransactions' ,N'p_order' ,N'bigint ' ,N'ReturnNumber' ,N' Hybris.dbo.paymentinfos originalpk' , N'key' ,N'ReturnPaymentTransactions',N'') 
,--p_order	
(N'' ,N'',N'' ,N' Hybris system Attributes' ,N'Hybris.dbo.payementtransactions' ,N'p_info' ,N'bigint ' ,N'ReturnNumber' ,N' Hybris.dbo.paymentinfos originalpk' , N'key' ,N'ReturnPaymentTransactions',N'') 
,--p_info	
(N'RFOpetions.Hybris.ReturnPayment' ,N'TokenID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.payementtransactions' ,N'p_requesttoken' ,N'	nvarchar(255) ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_requesttoken
(N'RFOpetions.Hybris.ReturnPayment' ,N'TransactionID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_requestid' ,N'	nvarchar(255) ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_requesttoken-Entries
(N'RFOpetions.Hybris.ReturnPayment' ,N'AmountAuthorized',N'money' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_amount' ,N'	decimal(30,8) ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_amount-Entries
(N'RFOpetions.Hybris.ReturnPayment' ,N'TokenID',N'nvarchar(50)' ,N' ' ,N'Hybris.dbo.payementtransactionentries' ,N'p_requesttoken' ,N'	nvarchar(255) ' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentTransactions',N'') 
,--p_requesttoken-Entries
(N'RFOpetions.Hybris.ReturnPayment' ,N'PaymentTypeID',N'bigint' ,N' ' ,N'Hybris.dbo.payementtransactions' ,N'p_type' ,N'bigint ' ,N'ReturnNumber' ,N' Hybris.dbo.enumvalues' , N'key' ,N'ReturnPaymentTransactions',N'') 
,--p_type   payementtransactions  Types


/* 
 Hybris system default ReturnPayment Transactions  attributes will be here .
*/



/* ReturnPayment Addresses  Level */

(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'FirstName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_firstname' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_firstname
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'MiddleName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_middlename' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_middlename
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'LastName',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_lastname' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_lastname
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'AddressLine1',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetname' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_streetname
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'AddressLine2',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_streetNumber' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_streetNumber
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'PostalCode',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_postalcode' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_postalcode
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'Locale',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_town' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_town
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'Region',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_region' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.regions ' , N'ref' ,N'ReturnPaymentAddress',N'') 
,--p_region
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'CountryID',N'bigint' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_country' ,N'bigint' ,N'ReturnNumber' ,N'Hybris.dbo.countries ' , N'ref' ,N'ReturnPaymentAddress',N'') 
,--p_country
(N'RFOpetions.Hybris.ReturnBillingAddress' ,N'Telephone',N'nvarchar(250)' ,N' ' ,N'Hybris.dbo.addresses' ,N'p_phone1' ,N'nvarchar(255)' ,N'ReturnNumber' ,N' ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_phone1
(N'RFOpetions.RFOperations.RFO_Accounts.Accountcontracts' ,N'BirthDay',N'datetime' ,N'When 1900 then Null ' ,N'Hybris.dbo.addresses' ,N'p_dateofbirth' ,N'datetime' ,N'ReturnNumber' ,N'When 1900 then Null ' , N'c2c' ,N'ReturnPaymentAddress',N'') 
,--p_dateofbirth
(N'RFOpetions.RFOperations.RFO_Accounts.Accountcontracts' ,N'GenderID',N'bigint' ,N'When 1900 then Null ' ,N'Hybris.dbo.addresses' ,N'p_gender' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.enumvalues' , N'ref' ,N'ReturnPaymentAddress',N'') 
,--p_gender
(N' ' ,N' ',N' ' ,N' Hybris System Attributes ' ,N'Hybris.dbo.addresses' ,N'p_billingaddress' ,N'tinyint' ,N'ReturnNumber' ,N' defaulted to 1' , N'def' ,N'ReturnPaymentAddress',N'') 
,--p_billingaddress
(N' ' ,N' ',N' ' ,N' Hybris System Attributes ' ,N'Hybris.dbo.addresses' ,N'p_duplicate' ,N'tinyint' ,N'ReturnNumber' ,N' defaulted to 1' , N'def' ,N'ReturnPaymentAddress',N'') 
,--p_duplicate
(N' ' ,N' ',N' ' ,N' Hybris System Attributes ' ,N'Hybris.dbo.addresses' ,N'ownerpkstring' ,N'bigint' ,N'ReturnNumber' ,N' Hybris.dbo.paymentinfos' , N'key' ,N'ReturnPaymentAddress',N'') 
--ownerpkstring

/* 
 Hybris system default ReturnPayment Addresses  attributes will be here .
*/




