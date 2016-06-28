


/******************************************
Account Credit Card Types Clean Up  GP-2069
AUTOSHIP  Credit Card Types Clean Up ( GP-2126 )
ORDERS Credit Card Types Clean Up ( GP-2127 )
RETURNORDERS Credit Card Types Clean Up ( GP-2128)  

CleanUp Critera: For Foundation Project
Accounts: Only Active accounts
Autoships: Only Active Autoships
Orders: From 01/01/2016 onwards
returns: From 01/01/2016 onwards


************************************************/




/* 
Card Type	Card Number Prefix	Length
MASTERCARD	51-55	16
VISA	4	13, 16
AMEX	34 ,37	15
Diners Club/Carte Blanche	300-305,36,38	14
Discover	6011	16
enRoute	2014,2149	15
JCB	3	16
JCB	2131,1800	15

*/



--#######################################################################################################################
--CHECKING ACCOUNT LEVEL CC. DINER IS TRANSFORMED TO DISCOVER SO WE SHOULD NOT SEE ANY RECORD WITH VENDORID=8(DINERS)
--#######################################################################################################################

DECLARE @start DATETIME


USE RFOperations 

BEGIN
SELECT  CCV.Name[CreditCart Types] ,
        CCP.*
FROM    RFOperations.RFO_Accounts.CreditCardProfiles CCP
        JOIN RodanFieldsLive.dbo.AccountPaymentMethods app ON app.AccountPaymentMethodID = CCP.CreditCardProfilesID
		JOIN RodanFieldsLive.dbo.accounts ac ON ac.AccountID = app.AccountID AND ac.Active=1
        JOIN RFOperations.RFO_Reference.CreditCardVendors CCV ON CCV.VendorID = CCP.VendorID
WHERE   CCV.VendorID = 8; -- 39,439 RECORDS IN STG2 .

SELECT @start =GETDATE() 

USE RFOperations;
SELECT  COUNT(*) [Total Records]
	/**ccp.CreditCardProfilesID ,
        ccp.VendorID ,
        ccv.Name [credit Card Name] ,
        AP.* ,
        RFOperations.dbo.DecryptTripleDES(AP.AccountNumber) AS Number ,
        ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(AP.AccountNumber)),
               'Unknown') AS CCtype  
			   --***/
FROM    RodanFieldsLive.dbo.AccountPaymentMethods AP
        JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AP.AccountID
                                                AND ac.Active = 1
        JOIN RFOperations.RFO_Accounts.CreditCardProfiles ccp ON AP.AccountPaymentMethodID = ccp.CreditCardProfilesID
        JOIN RFOperations.RFO_Reference.CreditCardVendors ccv ON ccv.VendorID = ccp.VendorID
WHERE   ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(AP.AccountNumber)),
                 'Unknown') = 'visa'
          AND ccp.VendorID <> 2
        )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(AP.AccountNumber)),
                    'Unknown') = 'mastercard'
             AND ccp.VendorID <> 1
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(AP.AccountNumber)),
                    'Unknown') = 'amex'
             AND ccp.VendorID <> 3
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(AP.AccountNumber)),
                    'Unknown') = 'discover'
             AND ccp.VendorID <> 5
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(AP.AccountNumber)),
                    'Unknown') = 'diners'
             AND ccp.VendorID <> 5
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(AP.AccountNumber)),
                    'Unknown') = 'unknown'
             AND ccp.VendorID <> 12
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(AP.AccountNumber)),
                    'Unknown') = 'Invalid'
             AND ccp.VendorID <> 13
           );

		   SELECT 'Account level validation ' AS [Validation Types],@start AS [Start Datetime],GETDATE() AS [End Datetime]
END 


--#######################################################################################################################
--CHECKING AUTOSHIP LEVEL CC. DINER IS TRANSFORMED TO DISCOVER SO WE SHOULD NOT SEE ANY RECORD WITH VENDORID=8(DINERS)
--#######################################################################################################################


BEGIN
SELECT @start =GETDATE()

SELECT  COUNT(*) [Total Counts]
--SELECT  AP.* ,  CCV.Name
FROM    RFOperations.Hybris.AutoshipPayment AP
        JOIN RodanFieldsLive..OrderPayments OP ON OP.OrderPaymentID = AP.AutoshipPaymentID
        JOIN RFOperations.Hybris.Autoship A ON A.AutoshipID = AP.AutoshipID
                                               AND A.Active = 1
        JOIN RFOperations.RFO_Reference.CreditCardVendors CCV ON CCV.VendorID = AP.VendorID
WHERE   A.CountryID = 236
        AND CCV.VendorID = 8; ---96,388 RECORDS IN STG2. 

SELECT 'Autoship Level Validation ' AS [Validation Types ]

USE RFOperations;
 
SELECT    COUNT(*) [Total Records]
	/** AP.AutoshipPaymentID ,
        AP.AutoshipID ,
        CCV.Name [CreditCard Name] ,
        RFOperations.dbo.DecryptTripleDES(OP.AccountNumber) AS Number ,
        ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
               'Unknown') AS CCtype ,
        OP.*
		--  ***/
FROM    RodanFieldsLive.dbo.OrderPayments OP
        JOIN RFOperations.Hybris.AutoshipPayment AP ON AP.AutoshipPaymentID = OP.OrderPaymentID
        JOIN RFOperations.Hybris.Autoship A ON A.AutoshipID = AP.AutoshipID
                                               AND A.Active = 1
        JOIN RFOperations.RFO_Reference.CreditCardVendors CCV ON CCV.VendorID = AP.VendorID
WHERE   A.CountryID = 236
        AND ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                     'Unknown') = 'visa'
              AND AP.VendorID <> 2
            )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'mastercard'
             AND AP.VendorID <> 1
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'amex'
             AND AP.VendorID <> 3
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'discover'
             AND AP.VendorID <> 5
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'diners'
             AND AP.VendorID <> 5
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'unknown'
             AND AP.VendorID <> 12
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'Invalid'
             AND AP.VendorID <> 13
           );

		   SELECT 'Autoship level validation ' AS [Validation Types],@start AS [Start Datetime],GETDATE() AS [End DateTime]
		   END
           

--#######################################################################################################################
--CHECKING ORDERS LEVEL CC. DINER IS TRANSFORMED TO DISCOVER SO WE SHOULD NOT SEE ANY RECORD WITH VENDORID=8(DINERS)
--#######################################################################################################################

BEGIN

SELECT COUNT(*) [Total Counts]
--SELECT  CCV.Name [CreditCard Types] , o.startdate,o.OrderTypeID,op.*
FROM    RFOperations.Hybris.OrderPayment OP
        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = OP.OrderID
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.OrderID 
		AND o.StartDate>'01/01/2016'
		--AND ro.CompletionDate >'01/01/2016'
        JOIN RFOperations.RFO_Reference.CreditCardVendors CCV ON CCV.VendorID = OP.VendorID
WHERE   CCV.VendorID = 8;-- 555,734 Records in STG2. 

SELECT @start =GETDATE()

SELECT 'Orders level Validation ' AS [Validation Types]

-- DETAILS VALIDATION 
SELECT    COUNT(*) [Total Records]
	/** CCV.Name [CreditCard Types] ,
        o.StartDate ,
        o.OrderTypeID ,
        RFOperations.dbo.DecryptTripleDES(OP.AccountNumber) AS Number ,
        ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
               'Unknown') AS CCtype ,
        AP.*
		-- ***/
FROM    RodanFieldsLive.dbo.OrderPayments OP
        JOIN RFOperations.Hybris.OrderPayment AP ON OP.OrderPaymentID = AP.OrderPaymentID
        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = OP.OrderID
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.OrderID
                                             AND o.StartDate > '01/01/2016'
		--AND ro.CompletionDate >'01/01/2016'
        JOIN RFOperations.RFO_Reference.CreditCardVendors CCV ON CCV.VendorID = AP.VendorID
WHERE   ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                 'Unknown') = 'visa'
          AND AP.VendorID <> 2
        )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'mastercard'
             AND AP.VendorID <> 1
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'amex'
             AND AP.VendorID <> 3
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'discover'
             AND AP.VendorID <> 5
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'diners'
             AND AP.VendorID <> 5
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'unknown'
             AND AP.VendorID <> 12
           )
        OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                    'Unknown') = 'Invalid'
             AND AP.VendorID <> 13
           );
		   SELECT 'Orders level validation ' AS [Validation Types],@start AS [Start Datetime],GETDATE() AS [End DateTime]

		   END 
--#######################################################################################################################
--CHECKING RETURNORDERS LEVEL CC. DINER IS TRANSFORMED TO DISCOVER SO WE SHOULD NOT SEE ANY RECORD WITH VENDORID=8(DINERS)
--#######################################################################################################################

BEGIN

SELECT COUNT(*) [Total Counts]
--SELECT  CCV.Name [CreditCard Types] ,OP.*
FROM    RFOperations.Hybris.ReturnPayment OP
        JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = OP.ReturnOrderID
        JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.ReturnOrderID AND o.StartDate>'01/01/2016'
        JOIN RFOperations.RFO_Reference.CreditCardVendors CCV ON CCV.VendorID = OP.VendorID
WHERE   CCV.VendorID = 8;	--- 15433 Records in STG2.	
		

		-- DETAILS VALIDATOIN 
		SELECT @start =GETDATE()

		SELECT 'Return Level Validation ' AS [Validation Types]


        SELECT   COUNT(*) [Total Records]
	/**  RP.ReturnPaymentId ,
                RP.ReturnOrderID ,
                CCV.Name [CreditCard Types] ,
                RFOperations.dbo.DecryptTripleDES(OP.AccountNumber) AS Number ,
                ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                       'Unknown') AS CCtype ,
                OP.*
				--  **/
        FROM    RodanFieldsLive.dbo.OrderPayments OP
                JOIN RFOperations.Hybris.ReturnPayment RP ON RP.ReturnPaymentId = OP.OrderPaymentID
                JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = RP.ReturnOrderID
                JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = ro.ReturnOrderID
                                                     AND o.StartDate > '01/01/2016'
                JOIN RFOperations.RFO_Reference.CreditCardVendors CCV ON CCV.VendorID = RP.VendorID
        WHERE   ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                         'Unknown') = 'visa'
                  AND RP.VendorID <> 2
                )
                OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                            'Unknown') = 'mastercard'
                     AND RP.VendorID <> 1
                   )
                OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                            'Unknown') = 'amex'
                     AND RP.VendorID <> 3
                   )
                OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                            'Unknown') = 'discover'
                     AND RP.VendorID <> 5
                   )
                OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                            'Unknown') = 'diners'
                     AND RP.VendorID <> 5
                   )
                OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                            'Unknown') = 'unknown'
                     AND RP.VendorID <> 12
                   )
                OR ( ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)),
                            'Unknown') = 'Invalid'
                     AND RP.VendorID <> 13
                   );


				   SELECT 'Returns level validation ' AS [Validation Types],@start AS [Start DateTime],GETDATE() AS [End DateTime]
				   END 