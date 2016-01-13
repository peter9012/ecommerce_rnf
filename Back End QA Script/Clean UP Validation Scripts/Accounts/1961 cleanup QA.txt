-- US 		    
			
			SELECT  
                EA.EmailAddress, COUNT(*) AS Count -- INTO #AllAccounts
              
        FROM    RFOperations.RFO_Accounts.AccountBase account
                RIGHT JOIN RFOperations.RFO_Accounts.AccountRF ARF ON account.AccountID = ARF.AccountID
                RIGHT  JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountId = account.AccountID
                RIGHT JOIN RFOperations.RFO_Accounts.AccountEmails AE ON AE.AccountContactId = AC.AccountContactId
               RIGHT  JOIN RFOperations.RFO_Accounts.EmailAddresses EA ON EA.EmailAddressID = AE.EmailAddressId
			          --JOIN Hybris.dbo.Users u ON u.p_rfaccountid = CAST(arf.accountid AS VARCHAR) 
         WHERE  EA.EmailAddressTypeID = 1 AND account.CountryID = 236 AND EA.EmailAddress <> ''
      GROUP BY EA.EmailAddress  

	  HAVING COUNT(*)> 1


---------------------------------------------------------------------------------------------------------------------
	DROP TABLE #AllAccounts 
	
	-- US Sharing with Canada   	
			SELECT  
                EA.EmailAddress,account.CountryID, COUNT(*) AS Count  INTO #AllAccounts
              
        FROM    RFOperations.RFO_Accounts.AccountBase account
                RIGHT JOIN RFOperations.RFO_Accounts.AccountRF ARF ON account.AccountID = ARF.AccountID
                RIGHT  JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountId = account.AccountID
                RIGHT JOIN RFOperations.RFO_Accounts.AccountEmails AE ON AE.AccountContactId = AC.AccountContactId
               RIGHT  JOIN RFOperations.RFO_Accounts.EmailAddresses EA ON EA.EmailAddressID = AE.EmailAddressId
			          --JOIN Hybris.dbo.Users u ON u.p_rfaccountid = CAST(arf.accountid AS VARCHAR) 
         WHERE  EA.EmailAddressTypeID = 1  AND EA.EmailAddress <> ''
      GROUP BY EA.EmailAddress,account.CountryID  

	  HAVING COUNT(*)> 1

	  --SELECT * FROM #ALLAccounts

; WITH CTE AS 
(

SELECT a.EmailAddress 
 FROM    RFOperations.RFO_Accounts.AccountBase account
                RIGHT JOIN RFOperations.RFO_Accounts.AccountRF ARF ON account.AccountID = ARF.AccountID
                RIGHT  JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountId = account.AccountID
                RIGHT JOIN RFOperations.RFO_Accounts.AccountEmails AE ON AE.AccountContactId = AC.AccountContactId
               RIGHT  JOIN RFOperations.RFO_Accounts.EmailAddresses EA ON EA.EmailAddressID = AE.EmailAddressId
			   JOIN #AllAccounts a ON a.EmailAddress =ea.emailaddress
         WHERE  EA.EmailAddressTypeID = 1  AND account.CountryID =40
) 


	SELECT * 	
	FROM 
	RFOperations.RFO_Accounts.AccountBase account
                RIGHT JOIN RFOperations.RFO_Accounts.AccountRF ARF ON account.AccountID = ARF.AccountID
                RIGHT  JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountId = account.AccountID
                RIGHT JOIN RFOperations.RFO_Accounts.AccountEmails AE ON AE.AccountContactId = AC.AccountContactId
               RIGHT  JOIN RFOperations.RFO_Accounts.EmailAddresses EA ON EA.EmailAddressID = AE.EmailAddressId
			   JOIN cte a ON a.EmailAddress =ea.emailaddress
			     WHERE  EA.EmailAddressTypeID = 1  AND account.CountryID =236

---------------------------------------------------------------------------------------------------
				 SELECT arf.AccountID, EmailAddress, CountryID, Active, ARF.EnrollmentDate, AccountTypeID FROM RFOperations.RFO_Accounts.AccountBase account
                RIGHT JOIN RFOperations.RFO_Accounts.AccountRF ARF ON account.AccountID = ARF.AccountID
                RIGHT  JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountId = account.AccountID
                RIGHT JOIN RFOperations.RFO_Accounts.AccountEmails AE ON AE.AccountContactId = AC.AccountContactId
               RIGHT  JOIN RFOperations.RFO_Accounts.EmailAddresses EA ON EA.EmailAddressID = AE.EmailAddressId
			   WHERE  EA.EmailAddressTypeID = 1 AND EA.EmailAddress LIKE '%michelle.tryon@yahoo.com'



		SELECT p_rfaccountid, p_customeremail, uniqueid,p_logindisabled, p_country,p_customerusername, * FROM Hybris.dbo.Users 
		WHERE uniqueid LIKE '%michelle.tryon@yahoo.com'



--------------------------------------------------------------------------------------------------------------------------

		SELECT  a.Username, COUNT( *)
               FROM     RFOperations.Security.AccountSecurity (NOLOCK) a
                        JOIN RFOperations.RFO_Accounts.AccountRF (NOLOCK)arf ON arf.AccountID = a.AccountID
                        JOIN RFOperations.RFO_Accounts.AccountBase (NOLOCK)ab ON ab.AccountID = arf.AccountID
               WHERE    ab.CountryID = 236
    			 GROUP BY UserName 
			 HAVING COUNT(*)> 1 


		SELECT  a.Username, COUNT( *)
               FROM     RFOperations.Security.AccountSecurity (NOLOCK) a
                        JOIN RFOperations.RFO_Accounts.AccountRF (NOLOCK)arf ON arf.AccountID = a.AccountID
                        JOIN RFOperations.RFO_Accounts.AccountBase (NOLOCK)ab ON ab.AccountID = arf.AccountID
               WHERE    ab.CountryID = 40
    			 GROUP BY UserName 
			 HAVING COUNT(*)> 1 
--------------------------------------------------------------------
--QA Validation using test data
SELECT arf.AccountID,a.Username,EmailAddress, CountryID, Active, arf.EnrollmentDate, AccountTypeID FROM RFOperations.RFO_Accounts.AccountBase account

                RIGHT JOIN RFOperations.RFO_Accounts.AccountRF arf ON account.AccountID = arf.AccountID
				            Right JOIN RFOperations.Security.AccountSecurity (NOLOCK) a ON  arf.AccountID = a.AccountID 
                RIGHT  JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountId = account.AccountID
                RIGHT JOIN RFOperations.RFO_Accounts.AccountEmails AE ON AE.AccountContactId = AC.AccountContactId
               RIGHT  JOIN RFOperations.RFO_Accounts.EmailAddresses EA ON EA.EmailAddressID = AE.EmailAddressId
			   WHERE  EA.EmailAddressTypeID = 1 AND EA.EmailAddress LIKE '%michelehazzard@shaw.ca'



		SELECT p_rfaccountid, p_customeremail, uniqueid,p_logindisabled, p_country,p_customerusername, * FROM Hybris.dbo.Users 
		WHERE uniqueid LIKE '%michelehazzard@shaw.ca'
-----------------------------------------------------------------------------------------------------------------