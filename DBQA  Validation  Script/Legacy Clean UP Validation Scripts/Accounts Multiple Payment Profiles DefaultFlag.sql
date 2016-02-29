
/*
Payment Profile Check.
This script will return Accounts having MULTIPLE PaymentProfiles with IsDefault=1. 
Ideally result should be 0 but if more records are returned, then it shuold be reported for analysis
*/
SELECT  PP.*
FROM    RFOperations.RFO_Accounts.PaymentProfiles PP ,
        RFOperations.RFO_Accounts.AccountBase AB
WHERE   AB.AccountID = PP.AccountID
        AND IsDefault = 1
        AND AB.CountryID = 236
        AND AB.AccountID IN (
        SELECT  AB.AccountID
        FROM    RFOperations.RFO_Accounts.PaymentProfiles PP ,
                RFOperations.RFO_Accounts.AccountBase AB
        WHERE   AB.AccountID = PP.AccountID
                AND IsDefault = 1
                AND AB.CountryID = 236
        GROUP BY AB.AccountID
        HAVING  COUNT(*) > 1 )
--AND PP.CHANGEDBYUSER='GP-2029'
ORDER BY AccountID;

/*
Biling Address Check
This script will return Accounts having MULTIPLE BillingAddresses with IsDefault=1. 
Ideally result should be 0 but if more records are returned, then it shuold be reported for analysis
*/
SELECT  AB.AccountID ,
        AD.*
FROM    RFOperations.RFO_Accounts.AccountBase AB ,
        RFOperations.RFO_Accounts.AccountContacts AC ,
        RFOperations.RFO_Accounts.AccountContactAddresses ACA ,
        RFOperations.RFO_Accounts.Addresses AD
WHERE   AB.AccountID = AC.AccountId
        AND AC.AccountContactId = ACA.AccountContactId
        AND ACA.AddressID = AD.AddressID
        AND AD.AddressTypeID = 3
        AND AD.IsDefault = 1
        AND AB.AccountID IN (
        SELECT  AB.AccountID
        FROM    RFOperations.RFO_Accounts.AccountBase AB ,
                RFOperations.RFO_Accounts.AccountContacts AC ,
                RFOperations.RFO_Accounts.AccountContactAddresses ACA ,
                RFOperations.RFO_Accounts.Addresses AD
        WHERE   AB.AccountID = AC.AccountId
                AND AC.AccountContactId = ACA.AccountContactId
                AND ACA.AddressID = AD.AddressID
                AND AD.AddressTypeID = 3
                AND AD.IsDefault = 1
        GROUP BY AB.AccountID
        HAVING  COUNT(*) > 1 )
ORDER BY AB.AccountID;

/*
Shipping Address check
This script will return Accounts having MULTIPLE Shipping Addresses with IsDefault=1. 
Ideally result should be 0 but if more records are returned, then it shuold be reported for analysis
*/
SELECT  AB.AccountID ,
        AD.*
FROM    RFOperations.RFO_Accounts.AccountBase AB ,
        RFOperations.RFO_Accounts.AccountContacts AC ,
        RFOperations.RFO_Accounts.AccountContactAddresses ACA ,
        RFOperations.RFO_Accounts.Addresses AD
WHERE   AB.AccountID = AC.AccountId
        AND AC.AccountContactId = ACA.AccountContactId
        AND ACA.AddressID = AD.AddressID
        AND AD.AddressTypeID = 2
        AND AD.IsDefault = 1
        AND AD.ChangedByUser = 'GP-2029'
        AND AB.AccountID IN (
        SELECT  AB.AccountID
        FROM    RFOperations.RFO_Accounts.AccountBase AB ,
                RFOperations.RFO_Accounts.AccountContacts AC ,
                RFOperations.RFO_Accounts.AccountContactAddresses ACA ,
                RFOperations.RFO_Accounts.Addresses AD
        WHERE   AB.AccountID = AC.AccountId
                AND AC.AccountContactId = ACA.AccountContactId
                AND ACA.AddressID = AD.AddressID
                AND AD.AddressTypeID = 2
                AND AD.IsDefault = 1
        GROUP BY AB.AccountID
        HAVING  COUNT(*) > 1 )
ORDER BY AB.AccountID;