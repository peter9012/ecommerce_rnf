-- This clean up script finds Credit card details that are missing and then creates new credit card data for such records. End result of the exercise is to have NO PAYMENTPROFILE AND NO CREDITCARD Data to be missing. Hence this count should return 0.

SELECT  COUNT(*)
        FROM    RFOperations.Hybris.AutoshipPayment AP
                JOIN RodanFieldsLive.dbo.OrderPayments OP ON OP.OrderPaymentID = AP.AutoshipPaymentID
                JOIN RFOperations.Hybris.Autoship A ON A.AutoshipID = AP.AutoshipID
                JOIN HYBRIS..USERS U ON U.P_RFACCOUNTID=CAST(A.ACCOUNTID AS NVARCHAR)              
			    JOIN RFOperations.RFO_Accounts.PaymentProfiles pp ON pp.AccountID = A.AccountID
                JOIN RodanFieldsLive.dbo.AccountPaymentMethods APM ON APM.AccountID = A.AccountID

                LEFT JOIN RFOperations.RFO_Accounts.CreditCardProfiles cp ON cp.PaymentProfileID = pp.PaymentProfileID
                                                              AND AP.Expmonth = cp.ExpMonth
                                                              AND AP.ExpYear = cp.ExpYear
                                                              AND APM.AccountNumber = OP.AccountNumber

        WHERE   A.CountryID = 236
                AND cp.PaymentProfileID IS NULL