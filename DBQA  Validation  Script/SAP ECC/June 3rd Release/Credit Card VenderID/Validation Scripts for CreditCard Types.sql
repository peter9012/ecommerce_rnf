

---- ACCOUNT LEVEL VALIDATION 
SELECT DISTINCT TOP 10
        p.PaymentProfileID ,
        v.Name ,
        p.VendorID ,
        ap.AuditDate
FROM    RodanFieldsLive.dbo.AccountPaymentMethods a
        JOIN RFOperations.RFO_Accounts.PaymentProfiles b ON a.AccountPaymentMethodID = b.PaymentProfileID
        JOIN RFOperations.RFO_Accounts.CreditCardProfiles p ON p.PaymentProfileID = b.PaymentProfileID
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON p.VendorID = v.VendorID
        JOIN RodanFieldsLive.dbo.AccountPaymentMethodsAudit ap ON ap.AccountPaymentMethodID = b.PaymentProfileID
WHERE   v.Name <> 'Unknown'
ORDER BY ap.AuditDate DESC; 


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
WHERE   AccountPaymentMethodID IN ( 3642852, 3642858 );
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


-- Retrieving from RFL 102 now.


SELECT  *
FROM    RodanFieldsLive.dbo.AccountPaymentMethodsAudit
WHERE   AccountPaymentMethodID IN ( 3383398, 3424152, 3449792, 3551451,
                                    3642852, 3642858, 3642867, 3642885,
                                    2971494, 1940605, 3643025 );




SELECT  cvv.Name ,
        scp.*
FROM    RFOperations.ETL.StagingCardProfiles scp
        JOIN RFOperations.RFO_Reference.CreditCardVendors cvv ON cvv.VendorID = scp.VendorId
WHERE   scp.CreditCardProfileId IN ( 3383398, 3424152, 3449792, 3551451,
                                     3642852, 3642858, 3642867, 3642885,
                                     2971494, 1940605, 3643025 );


SELECT  *
FROM    RFOperations.ETL.StagingCardProfiles;

SELECT  v.Name ,
        c.*
FROM    RFOperations.RFO_Accounts.CreditCardProfiles c
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = c.VendorID
WHERE   c.CreditCardProfilesID IN ( 3383398, 3424152, 3449792, 3551451,
                                    3642852, 3642858, 3642867, 3642885,
                                    2971494, 1940605, 3643025 );







---- AUTOSHIP  LEVEL VALIDATION 

SELECT DISTINCT TOP 2
        a.OrderPaymentID ,
        v.Name ,
        b.VendorID ,
        b.DisplayNumber ,
        u.AuditDate
FROM    RodanFieldsLive.dbo.OrderPayments a
        JOIN RodanFieldsLive.dbo.OrdersAudit u ON u.OrderID = a.OrderID
        JOIN RFOperations.Hybris.AutoshipPayment b ON b.AutoshipID = a.OrderID
                                                      AND b.AutoshipPaymentID = a.OrderPaymentID
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = b.VendorID
WHERE   v.Name <> 'Visa'
ORDER BY u.AuditDate DESC;



SELECT DISTINCT TOP 10
        a.OrderPaymentID ,
        v.Name ,
        b.VendorID ,
        b.DisplayNumber ,
        u.AuditDate
FROM    RodanFieldsLive.dbo.OrderPayments a
        JOIN RodanFieldsLive.dbo.OrdersAudit u ON u.OrderID = a.OrderID
        JOIN RFOperations.Hybris.AutoshipPayment b ON b.AutoshipID = a.OrderID
                                                      AND b.AutoshipPaymentID = a.OrderPaymentID
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = b.VendorID
WHERE   v.Name = 'Visa'
ORDER BY u.AuditDate DESC;


 --- Updating in RFL Source 
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'bkw4tIaxgWgIfIKKPz66LRwwpuRfRy2eiclqZ6FVYi8=' --SELECT dbo.EncryptTripleDES(4111111111111111)
WHERE   OrderPaymentID IN ( 17037266, 17037267 );
 --Visa
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'VsZTVm/PdG3/qT3GNqbwCRwwpuRfRy2eiclqZ6FVYi8=' --SELECT  dbo.EncryptTripleDES(5105105105105100)
WHERE   OrderPaymentID IN ( 318315, 14994756 );
 --MasterCards
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'emrmGun1FYItNqFv3lRyF+0Hi/d+kIS8GetEHlq9Y3k=' -- SELECT dbo.EncryptTripleDES(378282246310005)
WHERE   OrderPaymentID IN ( 15702079, 11937064 );
--Amex
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'emrmGun1FYItNqFv3lRyF+0Hi/d+kIS8GetEHlq9Y3k=' --SELECT dbo.EncryptTripleDES(6011111111111117)
WHERE   OrderPaymentID IN ( 17037246 );
 --DISCOVER 
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = '2DUxCfOgDWhjsdUfZO5pYIQpiiL2KiqHQpvkxMaHO14=' --SELECT dbo.EncryptTripleDES(30569309025904)
WHERE   OrderPaymentID IN ( 17037247 );
 --DINNERS 
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'CHyCij8+ui3mAPIowOAieZVGi6in7T9fbhN7tuKONK8=' --SELECT dbo.EncryptTripleDES(000011111111111)
WHERE   OrderPaymentID IN ( 2448776 );
 -- INVALID
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'NULL '
WHERE   OrderPaymentID IN ( 14951643 );
 -- UNKNOWN


-- Updating Autoshp to pick ETL 
UPDATE  o
SET     o.OrderStatusID = 7 ,
        o.CommissionDate = '2015-07-19 18:05:46.000'
FROM    RodanFieldsLive.dbo.OrderPayments a
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = a.OrderID
WHERE   OrderPaymentID IN ( 17037266, 17037267, 318315, 14994756, 15702079,
                            11937064, 17037246, 17037247, 2448776, 14951643 );

SELECT  a.AuditDate ,
        a.*
FROM    RodanFieldsLive.dbo.OrdersAudit a
        JOIN RodanFieldsLive.dbo.OrderPayments b ON b.OrderID = a.OrderID
WHERE   b.OrderPaymentID IN ( 17037266, 17037267, 318315, 14994756, 15702079,
                              11937064, 17037246, 17037247, 2448776, 14951643 )
ORDER BY a.AuditDate DESC;




SELECT  cvv.Name ,
        scp.*
FROM    RFOperations.ETL.StagingAutoship scp
        JOIN RFOperations.RFO_Reference.CreditCardVendors cvv ON CAST(cvv.VendorID AS VARCHAR(12)) = scp.VendorID
WHERE   scp.AutoshipPaymentID IN ( 17037266, 17037267, 318315, 14994756,
                                   15702079, 11937064, 17037246, 17037247,
                                   2448776, 14951643 );


									--SELECT * FROM RFOperations.etl.StagingCardProfiles

SELECT  v.Name ,
        c.*
FROM    RFOperations.Hybris.AutoshipPayment c
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = c.VendorID
WHERE   c.AutoshipPaymentID IN ( 17037266, 17037267, 318315, 14994756,
                                 15702079, 11937064, 17037246, 17037247,
                                 2448776, 14951643 );


USE RFOperations;
EXEC ETL.[USPExceptStagingCardProfiles];












 

---- ORDERS LEVEL VALIDATION 

SELECT DISTINCT TOP 10
        a.OrderPaymentID ,
        v.Name ,
        b.VendorID ,
        b.DisplayNumber ,
        u.AuditDate
FROM    RodanFieldsLive.dbo.OrderPayments a
        JOIN RodanFieldsLive.dbo.OrdersAudit u ON u.OrderID = a.OrderID
        JOIN RFOperations.Hybris.OrderPayment b ON b.OrderID = a.OrderID
                                                   AND b.OrderPaymentID = a.OrderPaymentID
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = b.VendorID
WHERE   v.Name = 'Visa'
        AND u.AuditDate > DATEADD(DAY, -30, GETDATE());


SELECT DISTINCT TOP 2
        a.OrderPaymentID ,
        v.Name ,
        b.VendorID ,
        b.DisplayNumber ,
        u.AuditDate
FROM    RodanFieldsLive.dbo.OrderPayments a
        JOIN RodanFieldsLive.dbo.OrdersAudit u ON u.OrderID = a.OrderID
        JOIN RFOperations.Hybris.OrderPayment b ON b.OrderID = a.OrderID
                                                   AND b.OrderPaymentID = a.OrderPaymentID
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = b.VendorID
WHERE   v.Name <> 'Visa'
        AND u.AuditDate > DATEADD(DAY, -30, GETDATE());


 --- Updating in RFL Source 

UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'bkw4tIaxgWgIfIKKPz66LRwwpuRfRy2eiclqZ6FVYi8=' --SELECT dbo.EncryptTripleDES(4111111111111111)
WHERE   OrderPaymentID IN ( 6209258, 13475682 );
 --Visa
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'VsZTVm/PdG3/qT3GNqbwCRwwpuRfRy2eiclqZ6FVYi8=' --SELECT  dbo.EncryptTripleDES(5105105105105100)
WHERE   OrderPaymentID IN ( 9842096, 11151323 );
 --MasterCards
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'emrmGun1FYItNqFv3lRyF+0Hi/d+kIS8GetEHlq9Y3k=' -- SELECT dbo.EncryptTripleDES(378282246310005)
WHERE   OrderPaymentID IN ( 13365496, 14262400 );
--Amex
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'emrmGun1FYItNqFv3lRyF+0Hi/d+kIS8GetEHlq9Y3k=' --SELECT dbo.EncryptTripleDES(6011111111111117)
WHERE   OrderPaymentID IN ( 14884287 );
 --DISCOVER 
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = '2DUxCfOgDWhjsdUfZO5pYIQpiiL2KiqHQpvkxMaHO14=' --SELECT dbo.EncryptTripleDES(30569309025904)
WHERE   OrderPaymentID IN ( 14932655 );
 --DINNERS 
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'CHyCij8+ui3mAPIowOAieZVGi6in7T9fbhN7tuKONK8=' --SELECT dbo.EncryptTripleDES(000011111111111)
WHERE   OrderPaymentID IN ( 15865710 );
 -- INVALID
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'NULL '
WHERE   OrderPaymentID IN ( 15872859 );
 -- UNKNOWN

--- Updating to Pick ETL 

UPDATE  o
SET     o.OrderStatusID = 4 ,
        o.CompleteDate = DATEADD(DAY, -30, GETDATE())
 --SELECT o.startdate, o.orderstatusid
FROM    RodanFieldsLive.dbo.OrderPayments a
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = a.OrderID
WHERE   OrderPaymentID IN ( 6209258, 13475682, 9842096, 11151323, 13365496,
                            14262400, 14884287, 14932655, 15865710, 15872859 );




SELECT  u.AuditDate ,
        u.SystemUser ,
        u.*
FROM    RodanFieldsLive.dbo.OrderPayments a
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = a.OrderID
        JOIN RodanFieldsLive.dbo.OrdersAudit u ON u.OrderID = o.OrderID
WHERE   OrderPaymentID IN ( 6209258, 13475682, 9842096, 11151323, 13365496,
                            14262400, 14884287, 14932655, 15865710, 15872859 )
ORDER BY u.AuditDate DESC;


SELECT  cvv.Name ,
        scp.*
FROM    RFOperations.ETL.StagingOrders scp
        JOIN RFOperations.RFO_Reference.CreditCardVendors cvv ON CAST(cvv.VendorID AS VARCHAR(12)) = scp.VendorID
WHERE   scp.OrderPaymentID IN ( 6209258, 13475682, 9842096, 11151323, 13365496,
                                14262400, 14884287, 14932655, 15865710,
                                15872859 );


									--SELECT * FROM RFOperations.etl.StagingCardProfiles

SELECT  v.Name ,
        c.*
FROM    RFOperations.Hybris.OrderPayment c
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = c.VendorID
WHERE   c.OrderPaymentID IN ( 6209258, 13475682, 9842096, 11151323, 13365496,
                              14262400, 14884287, 14932655, 15865710, 15872859 );






---- RETURNS ORDERS  LEVEL VALIDATION 

SELECT DISTINCT TOP 10
        a.OrderPaymentID ,
        v.Name ,
        b.VendorID ,
        b.DisplayNumber ,
        u.AuditDate
FROM    RodanFieldsLive.dbo.OrderPayments a
        JOIN RodanFieldsLive.dbo.OrdersAudit u ON u.OrderID = a.OrderID
        JOIN RFOperations.Hybris.ReturnPayment b ON b.ReturnOrderID = a.OrderID
                                                    AND b.ReturnPaymentId = a.OrderPaymentID
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = b.VendorID
WHERE   v.Name = 'Visa'
        AND u.AuditDate > DATEADD(DAY, -30, GETDATE());


UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'bkw4tIaxgWgIfIKKPz66LRwwpuRfRy2eiclqZ6FVYi8=' --SELECT dbo.EncryptTripleDES(4111111111111111)
WHERE   OrderPaymentID IN ( 16133316, 16134195 );
 --Visa

UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'VsZTVm/PdG3/qT3GNqbwCRwwpuRfRy2eiclqZ6FVYi8=' --SELECT  dbo.EncryptTripleDES(5105105105105100)
WHERE   OrderPaymentID IN ( 16078410, 16133267 );
 --MasterCards
UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'emrmGun1FYItNqFv3lRyF+0Hi/d+kIS8GetEHlq9Y3k=' -- SELECT dbo.EncryptTripleDES(378282246310005)
WHERE   OrderPaymentID IN ( 15981140, 16053597 );
--Amex

UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'msxh/DwgUr7EZTcuSMZ6uhwwpuRfRy2eiclqZ6FVYi8=' --SELECT rfoperations.dbo.EncryptTripleDES(6011111111111117)
WHERE   OrderPaymentID IN ( 15980127 );
 --DISCOVER 

UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = '2DUxCfOgDWhjsdUfZO5pYIQpiiL2KiqHQpvkxMaHO14=' --SELECT dbo.EncryptTripleDES(30569309025904)
WHERE   OrderPaymentID IN ( 15974926 );
 --DINNERS 

UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'CHyCij8+ui3mAPIowOAieZVGi6in7T9fbhN7tuKONK8=' --SELECT dbo.EncryptTripleDES(000011111111111)
WHERE   OrderPaymentID IN ( 15947742 );
 -- INVALID

UPDATE  RodanFieldsLive..OrderPayments
SET     AccountNumber = 'NULL '
WHERE   OrderPaymentID IN ( 15946579 );
 -- UNKNOWN



UPDATE  o
SET     o.OrderStatusID = 4
FROM    RodanFieldsLive.dbo.OrderPayments a
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = a.OrderID
WHERE   OrderPaymentID IN ( 16133316, 16134195, 16078410, 16133267, 15981140,
                            16053597, 15980127, 15974926, 15947742, 15946579 );




SELECT  o.*
FROM    RodanFieldsLive.dbo.OrderPayments a
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = a.OrderID
WHERE   OrderPaymentID IN ( 16133316, 16134195, 16078410, 16133267, 15981140,
                            16053597, 15980127, 15974926, 15947742, 15946579 );




SELECT  u.AuditDate ,
        u.SystemUser ,
        u.*
FROM    RodanFieldsLive.dbo.OrderPayments a
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = a.OrderID
        JOIN RodanFieldsLive.dbo.OrdersAudit u ON u.OrderID = o.OrderID
WHERE   OrderPaymentID IN ( 16133316, 16134195, 16078410, 16133267, 15981140,
                            16053597, 15980127, 15974926, 15947742, 15946579 )
ORDER BY u.AuditDate DESC;


SELECT  cvv.Name ,
        scp.*
FROM    RFOperations.ETL.StagingReturns scp
        JOIN RFOperations.RFO_Reference.CreditCardVendors cvv ON CAST(cvv.VendorID AS VARCHAR(12)) = scp.VendorID
WHERE   scp.ReturnOrderID IN ( 16133316, 16134195, 16078410, 16133267,
                               15981140, 16053597, 15980127, 15974926,
                               15947742, 15946579 );


									--SELECT * FROM RFOperations.etl.StagingCardProfiles

SELECT  v.Name ,
        c.*
FROM    RFOperations.Hybris.ReturnPayment c
        JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = c.VendorID
WHERE   c.ReturnPaymentId IN ( 16133316, 16134195, 16078410, 16133267,
                               15981140, 16053597, 15980127, 15974926,
                               15947742, 15946579 );
