
/*==========================================================================================================================
Accounts level changes for VendorID 
==========================================================================================================================*/
SELECT TOP 10
        apm.AccountID ,
        apm.AccountPaymentMethodID ,
        dbo.GetCreditCardType(( dbo.DecryptTripleDES(apm.AccountNumber) )) AS CCtype,
		ccp.VendorID
FROM    RFOperations.RFO_Accounts.PaymentProfiles pp
        JOIN RFOperations.RFO_Accounts.CreditCardProfiles ccp ON ccp.PaymentProfileID = pp.PaymentProfileID
        JOIN RodanFieldsLive..AccountPaymentMethods apm ON pp.PaymentProfileID = apm.AccountPaymentMethodID
ORDER BY pp.ServerModifiedDate DESC 


UPDATE RodanFieldsLive..AccountPaymentMethods
SET AccountNumber =  dbo.EncryptTripleDES(30569309025904)
WHERE AccountPaymentMethodID = 3949141 -- Diners 


UPDATE RodanFieldsLive..AccountPaymentMethods
SET AccountNumber =  dbo.EncryptTripleDES(4111111111111111)
WHERE AccountPaymentMethodID = 3591748 -- VISA 


UPDATE RodanFieldsLive..AccountPaymentMethods
SET AccountNumber =  dbo.EncryptTripleDES(5105105105105100)
WHERE AccountPaymentMethodID = 3591747 -- mastercard  


UPDATE RodanFieldsLive..AccountPaymentMethods
SET AccountNumber =  dbo.EncryptTripleDES(378282246310005)
WHERE AccountPaymentMethodID = 3591745 -- AMEX 


UPDATE RodanFieldsLive..AccountPaymentMethods
SET AccountNumber =  dbo.EncryptTripleDES(6011111111111117)
WHERE AccountPaymentMethodID = 3591744 -- discover  

UPDATE RodanFieldsLive..AccountPaymentMethods
SET AccountNumber =   dbo.EncryptTripleDES(60111)
WHERE AccountPaymentMethodID = 3591744 -- Invalid   (should return invalid)


UPDATE RodanFieldsLive..AccountPaymentMethods
SET AccountNumber =   dbo.EncryptTripleDES(null)
WHERE AccountPaymentMethodID = 3591737 -- Null ( should return Unknown)  


SELECT scp.PaymentProfileID,ccv.VendorID,ccv.Name FROM etl.StagingCardProfiles scp JOIN 
RFOperations.RFO_Reference.CreditCardVendors ccv ON ccv.VendorID = scp.VendorId

/*==========================================================================================================================
Autoship level changes for VendorID 
==========================================================================================================================*/

SELECT TOP 10 * FROM RFOperations.hybris.AutoshipPayment ap
JOIN RodanFieldsLive..OrderPayments op ON op.OrderPaymentID = ap.AutoshipPaymentID ORDER BY ap.AutoshipPaymentID DESC 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(30569309025904)
WHERE OrderPaymentID = 18535794 -- Diners 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(4111111111111111)
WHERE OrderPaymentID = 18535784 -- VISA 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(5105105105105100)
WHERE OrderPaymentID = 18535777 -- mastercard  


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(378282246310005)
WHERE OrderPaymentID = 18535776 -- AMEX 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(6011111111111117)
WHERE OrderPaymentID = 18535622 -- discover  

UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =   dbo.EncryptTripleDES(60111)
WHERE OrderPaymentID = 18535616 -- Invalid   (should return invalid)


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =   dbo.EncryptTripleDES(null)
WHERE OrderPaymentID = 18535599 -- Null ( should return Unknown)  

--Update datemodified to getdate RodanFieldsLive..Orders table for above autoships

SELECT DISTINCT AutoshipPaymentID,VendorID FROM  ETL.StagingAutoship(NOLOCK)

/*==========================================================================================================================
Orders level changes for VendorID 
==========================================================================================================================*/
SELECT TOP 10 * FROM RFOperations.hybris.OrderPayment ap
JOIN RodanFieldsLive..OrderPayments op ON op.OrderPaymentID = ap.OrderPaymentID ORDER BY ap.OrderPaymentID DESC 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(30569309025904)
WHERE OrderPaymentID = 18535839 -- Diners 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(4111111111111111)
WHERE OrderPaymentID = 18535837 -- VISA 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(5105105105105100)
WHERE OrderPaymentID = 18535834 -- mastercard  


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(378282246310005)
WHERE OrderPaymentID = 18535833 -- AMEX 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(6011111111111117)
WHERE OrderPaymentID = 18535831 -- discover  

UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =   dbo.EncryptTripleDES(60111)
WHERE OrderPaymentID = 18535827 -- Invalid   (should return invalid)


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =   dbo.EncryptTripleDES(null)
WHERE OrderPaymentID = 18535821 -- Null ( should return Unknown)  



SELECT DISTINCT OrderID,VendorID FROM  ETL.StagingOrders(NOLOCK) WHERE OrderID IN (
24771989,24772060,24772055,24772022,24772020,24772015,24772014,24772006,24771939,24772000 ) 

/*==========================================================================================================================
Returns level changes for VendorID 
==========================================================================================================================*/
SELECT TOP 10 * FROM RFOperations.hybris.ReturnPayment(NOLOCK) ap
JOIN RodanFieldsLive..OrderPayments(NOLOCK) op ON op.OrderPaymentID = ap.ReturnPaymentId ORDER BY ap.ReturnPaymentId DESC 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(30569309025904)
WHERE OrderPaymentID = 18532125 -- Diners 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(4111111111111111)
WHERE OrderPaymentID = 18532012 -- VISA 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(5105105105105100)
WHERE OrderPaymentID = 18526855 -- mastercard  


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(378282246310005)
WHERE OrderPaymentID = 18526845 -- AMEX 


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =  dbo.EncryptTripleDES(6011111111111117)
WHERE OrderPaymentID = 18526838 -- discover  

UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =   dbo.EncryptTripleDES(60111)
WHERE OrderPaymentID = 18526826 -- Invalid   (should return invalid)


UPDATE RodanFieldsLive..OrderPayments
SET AccountNumber =   dbo.EncryptTripleDES(null)
WHERE OrderPaymentID = 18526823 -- Null ( should return Unknown)  


SELECT DISTINCT ReturnOrderID,VendorID FROM ETL.StagingReturns WHERE ReturnOrderID IN (
24761422,24761268,24756036,24756026,24756009,24755997,24755991,24755588,24755571,24755391)

