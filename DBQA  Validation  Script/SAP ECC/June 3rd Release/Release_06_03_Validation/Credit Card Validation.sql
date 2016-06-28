--- Account Level Validation .
USE RFOperations;
GO 
SELECT DISTINCT
        a.AccountPaymentMethodID ,
        b.PaymentProfileID ,
        a.AccountID ,
        a.AccountNumber ,
        dbo.DecryptTripleDES(a.AccountNumber) AS [RFL DisplayNumber] ,
        p.DisplayNumber [RFO  DisplayNumber] ,
        dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) AS CCtype ,
        CASE WHEN dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) = 'diners'
             THEN 'Discover'
             ELSE dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber))
        END AS [Calculate Cctypes] ,
        v.Name [RFO CreditCartType] ,
        p.VendorID ,
        ap.AuditDate,p.ServerModifiedDate
FROM    RodanFieldsLive.dbo.AccountPaymentMethods (NOLOCK) a
        JOIN RFOperations.RFO_Accounts.PaymentProfiles (NOLOCK) b ON a.AccountPaymentMethodID = b.PaymentProfileID
        JOIN RFOperations.RFO_Accounts.CreditCardProfiles (NOLOCK) p ON p.PaymentProfileID = b.PaymentProfileID
        JOIN RFOperations.RFO_Reference.CreditCardVendors (NOLOCK) v ON p.VendorID = v.VendorID
        JOIN RodanFieldsLive.dbo.AccountPaymentMethodsAudit (NOLOCK) ap ON ap.AccountPaymentMethodID = b.PaymentProfileID
WHERE   CASE WHEN dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) = 'diners'
             THEN 'Discover'
             ELSE dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber))
        END <> v.Name AND p.ServerModifiedDate>='2016-06-04 23:00:00:000'
ORDER BY ap.AuditDate DESC; 


--- Autoship Leve Validation 


SELECT DISTINCT  
        a.OrderPaymentID ,
        b.AutoshipPaymentID ,
        a.AccountNumber [RFL Number] ,
        dbo.DecryptTripleDES(a.AccountNumber) AS [RFL DisplayNumber] ,
        b.DisplayNumber [RFO  DisplayNumber] ,
        dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) AS CCtype ,
        CASE WHEN dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) = 'diners'
             THEN 'Discover'
             ELSE dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber))
        END AS [Calculate Cctypes] ,
        v.Name [RFO CreditCartType] ,
        b.VendorID ,
        u.AuditDate ,
        b.ServerModifiedDate
FROM    RodanFieldsLive.dbo.OrderPayments(NOLOCK) a
        JOIN RodanFieldsLive.dbo.OrdersAudit(NOLOCK) u ON u.OrderID = a.OrderID
        JOIN RFOperations.Hybris.AutoshipPayment(NOLOCK) b ON b.AutoshipID = a.OrderID
                                                      AND b.AutoshipPaymentID = a.OrderPaymentID
        JOIN RFOperations.RFO_Reference.CreditCardVendors(NOLOCK) v ON v.VendorID = b.VendorID
WHERE   CASE WHEN dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) = 'diners'
             THEN 'Discover'
             ELSE dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber))
        END <> v.Name AND u.AuditDate>='2016-06-04 23:00:00:000'
ORDER BY b.ServerModifiedDate DESC;





--- Validation of Orders 

SELECT DISTINCT
        a.OrderPaymentID [RFL OrderpaymentID] ,
        b.OrderPaymentID [RFO OrderpaymentId] ,
        a.AccountNumber [RFL Number] ,
        dbo.DecryptTripleDES(a.AccountNumber) AS [RFL DisplayNumber] ,
        b.DisplayNumber [RFO  DisplayNumber] ,
        dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) AS CCtype ,
        CASE WHEN dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) = 'diners'
             THEN 'Discover'
             ELSE dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber))
        END AS [Calculate Cctypes] ,
        v.Name [RFO CreditCartType] ,
        u.AuditDate ,
        b.ServerModifiedDate
FROM    RodanFieldsLive.dbo.OrderPayments (NOLOCK) a
        JOIN RodanFieldsLive.dbo.OrdersAudit (NOLOCK) u ON u.OrderID = a.OrderID
        JOIN RFOperations.Hybris.OrderPayment (NOLOCK) b ON b.OrderID = a.OrderID
                                                            AND b.OrderPaymentID = a.OrderPaymentID
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = b.VendorID
WHERE   CASE WHEN dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) = 'diners'
             THEN 'Discover'
             ELSE dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber))
        END <> v.Name AND u.AuditDate>='2016-06-04 23:00:00:000'
      




---- RETURNS ORDERS  LEVEL VALIDATION 

SELECT DISTINCT
        a.OrderPaymentID [RFL OrderpaymentID] ,
        b.ReturnPaymentId [RFO OrderpaymentID] ,
        a.AccountNumber [RFL Number] ,
        dbo.DecryptTripleDES(a.AccountNumber) AS [RFL DisplayNumber] ,
        b.DisplayNumber [RFO  DisplayNumber] ,
        dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) AS CCtype ,
        CASE WHEN dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) = 'diners'
             THEN 'Discover'
             ELSE dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber))
        END AS [Calculate Cctypes] ,
        v.Name [RFO CreditCartType] ,
        u.AuditDate ,
        b.ServerModifiedDate
FROM    RodanFieldsLive.dbo.OrderPayments (NOLOCK) a
        JOIN RodanFieldsLive.dbo.OrdersAudit (NOLOCK) u ON u.OrderID = a.OrderID
        JOIN RFOperations.Hybris.ReturnPayment (NOLOCK) b ON b.ReturnOrderID = a.OrderID
                                                             AND b.ReturnPaymentId = a.OrderPaymentID
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = b.VendorID
WHERE   CASE WHEN dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber)) = 'diners'
             THEN 'Discover'
             ELSE dbo.GetCreditCardType(dbo.DecryptTripleDES(a.AccountNumber))
        END <> v.Name AND u.AuditDate>='2016-06-04 23:00:00:000'
      
    



	/*******************************************

	
USE RFOperations;
GO 

SELECT  dbo.GetCreditCardType(dbo.DecryptTripleDES('emrmGun1FYItNqFv3lRyF+0Hi/d+kIS8GetEHlq9Y3k=')) AS CCtype;

 --- Updating in RFL Source 

UPDATE  RodanFieldsLive..AccountPaymentMethods
SET     AccountNumber = 'bkw4tIaxgWgIfIKKPz66LRwwpuRfRy2eiclqZ6FVYi8=' --SELECT dbo.EncryptTripleDES(4111111111111111)
WHERE   AccountPaymentMethodID IN ( 3383398, 3424152 );
 --Visa

UPDATE  RodanFieldsLive..AccountPaymentMethods
SET     AccountNumber = 'VsZTVm/PdG3/qT3GNqbwCRwwpuRfRy2eiclqZ6FVYi8=' --SELECT  dbo.EncryptTripleDES(5105105105105100)
WHERE   AccountPaymentMethodID IN ( 3449792, 3551451 );
 --MasterCards

UPDATE  RodanFieldsLive..AccountPaymentMethods
SET     AccountNumber = 'emrmGun1FYItNqFv3lRyF+0Hi/d+kIS8GetEHlq9Y3k=' -- SELECT dbo.EncryptTripleDES(378282246310005)
WHERE   AccountPaymentMethodID IN ( 3642880,318315 );
--Amex

UPDATE  RodanFieldsLive..AccountPaymentMethods
SET     AccountNumber = 'msxh/DwgUr7EZTcuSMZ6uhwwpuRfRy2eiclqZ6FVYi8=' --SELECT rfoperations.dbo.EncryptTripleDES(6011111111111117)
WHERE   AccountPaymentMethodID IN ( 3642867 );
 --DISCOVER 

UPDATE  RodanFieldsLive..AccountPaymentMethods
SET     AccountNumber = '2DUxCfOgDWhjsdUfZO5pYIQpiiL2KiqHQpvkxMaHO14=' --SELECT dbo.EncryptTripleDES(30569309025904)
WHERE   AccountPaymentMethodID IN ( 3642885 );
 --DINNERS 

UPDATE  RodanFieldsLive..AccountPaymentMethods
SET     AccountNumber = 'CHyCij8+ui3mAPIowOAieZVGi6in7T9fbhN7tuKONK8=' --SELECT dbo.EncryptTripleDES(000011111111111)
WHERE   AccountPaymentMethodID IN ( 2971494, 1940605 );
 -- INVALID

UPDATE  RodanFieldsLive..AccountPaymentMethods
SET     AccountNumber = NULL
WHERE   AccountPaymentMethodID IN ( 3643025 ); 
-- UNKNOWN




SELECT  *
FROM    RodanFieldsLive.dbo.AccountPaymentMethodsAudit
WHERE   AccountPaymentMethodID IN  ( 3642880,318315 );




SELECT  cvv.Name ,
        scp.*
FROM    RFOperations.ETL.StagingCardProfiles scp
        JOIN RFOperations.RFO_Reference.CreditCardVendors cvv ON cvv.VendorID = scp.VendorId
WHERE   scp.CreditCardProfileId IN  ( 3642880,318315 );


SELECT  *
FROM    RFOperations.ETL.StagingCardProfiles;

SELECT  v.Name ,
        c.*
FROM    RFOperations.RFO_Accounts.CreditCardProfiles c
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = c.VendorID
WHERE   c.CreditCardProfilesID IN  ( 3642880,318315 );

USE RFOperations
SELECT  dbo.GetCreditCardType(dbo.DecryptTripleDES(AccountNumber)) AS CCtype ,
        dbo.DecryptTripleDES(AccountNumber) [Number],
        *
FROM    RodanFieldsLive.dbo.AccountPaymentMethods
WHERE   AccountPaymentMethodID IN ( 3642880, 318315 );




******************************/