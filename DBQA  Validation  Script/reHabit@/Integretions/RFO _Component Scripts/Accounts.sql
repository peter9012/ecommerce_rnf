DECLARE @AccountID BIGINT= 0007109723 
  -- Please Change AccountID


 SELECT 'AccountBase' AS [Table] ,
        t.Name [AccountType] ,
        s.Name [AccountStatus] ,
        co.Alpha2Code [Country] ,
        cu.Name [currency] ,
        n.Native [Language] ,
        ab.*
 FROM   RFOperations.RFO_Accounts.AccountBase ab
        LEFT JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = ab.AccountTypeID
        LEFT JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
        LEFT JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ab.CountryID
        LEFT JOIN RFOperations.RFO_Reference.Currency cu ON cu.CurrencyID = ab.CurrencyID
        LEFT JOIN RFOperations.RFO_Reference.NativeLanguage n ON n.LanguageID = ab.LanguageId
	
 WHERE  AccountID = @AccountID


 SELECT 'AccountRF' AS [Table] ,
        rf.*
 FROM   RFOperations.RFO_Accounts.AccountBase ab
        LEFT JOIN RFOperations.RFO_Accounts.AccountRF rf ON ab.AccountID = rf.AccountID
 WHERE  ab.AccountID = @AccountID


 SELECT 'AccountSecurity' AS [Table] ,
      --  [dbo].[DecryptTripleDES](Password) AS [DecryptedPassword] ,
        *
 FROM   RFOperations.RFO_Accounts.AccountBase ab
        LEFT JOIN RFOperations.Security.AccountSecurity acs ON acs.AccountID = ab.AccountID
 WHERE  ab.AccountID = @AccountID

 SELECT 'AccountContacts' AS [Table] ,
		g.Name[Gender],
        t.Name [AccountContactType] ,
        ac.*
 FROM   RFOperations.RFO_Accounts.AccountBase ab
        LEFT JOIN RFOperations.RFO_Accounts.Accountcontacts ac ON ab.AccountID = ac.AccountID
        LEFT JOIN RFOperations.RFO_Reference.AccountContactType T ON T.AccountContactTypeID = ac.AccountContactTypeId
			LEFT JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID=ac.GenderId		
 WHERE  ab.AccountID = @AccountID

	

 SELECT 'AccountNotes ' AS [Table] ,
        d.Name [DetailTypes] ,
        n.*
 FROM   RFOperations.RFO_Accounts.AccountBase ab
        LEFT JOIN RFOperations.RFO_Accounts.AccountNotes n ON n.AccountID = ab.AccountID
        LEFT JOIN RFOperations.RFO_Reference.NotesDetailType d ON d.DetailTypeID = n.DetailTypeID
 WHERE  ab.AccountID = @AccountID

	
 SELECT 'AccountPolicy' AS [Table] ,
        ap.* ,
        ' Policy' AS [Table] ,
        p.*
 FROM   RFOperations.RFO_Accounts.AccountBase ab
        LEFT JOIN RFOperations.RFO_Accounts.AccountPolicy ap ON ap.AccountID = ab.AccountID
        LEFT JOIN RFOperations.RFO_Accounts.Policies p ON p.PolicyID = ap.PolicyID
 WHERE  ab.AccountID = @AccountID




 SELECT 'PaymentProfile ' AS [Table] ,
        pp.* ,
        'CreditCard ' AS [Table] ,
        cp.*
 FROM   RFOperations.RFO_Accounts.AccountBase ab
        LEFT JOIN RFOperations.RFO_Accounts.PaymentProfiles pp ON pp.AccountID = ab.AccountID
        LEFT JOIN RFOperations.RFO_Accounts.CreditCardProfiles cp ON cp.PaymentProfileID = pp.PaymentProfileID
 WHERE  ab.AccountID = @AccountID




 SELECT 'AccountContactAddresses' AS [Table] ,
        aca.* ,
        'AccountContactAddresses' AS [Table] ,
        t.Name [AddressType] ,
        ad.*
 FROM   RFOperations.RFO_Accounts.AccountBase ab
        JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
        LEFT JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
        LEFT JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
        LEFT JOIN RFOperations.RFO_Reference.AddressType t ON t.AddressTypeID = ad.AddressTypeID
 WHERE  ab.AccountID = @AccountID






 SELECT 'AccountContactsAddresse' AS [Table] ,
        aca.* ,
        'ContactAddressePhones' AS [Table] ,
        t.Name [PhoneTypes] ,
        p.*
 FROM   RFOperations.RFO_Accounts.AccountBase ab
        JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
        LEFT JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AccountContactId = ac.AccountContactId
        LEFT JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = aca.AddressID
        LEFT JOIN RFOperations.RFO_Accounts.AddressPhones ap ON ap.AddressId = ad.AddressID
        LEFT JOIN RFOperations.RFO_Accounts.Phones p ON p.PhoneID = ap.PhoneId
        LEFT JOIN RFOperations.RFO_Reference.PhoneType T ON T.PhoneTypeID = p.PhoneTypeID
 WHERE  ab.AccountID = @AccountID



 SELECT 'AccountChangeLog' [Table] ,
        ab.AccountID ,
        l.*
 FROM   RFOperations.RFO_Accounts.AccountBase ab
        LEFT JOIN RFOperations.Logging.AccountChangeLog l ON l.AccountId = ab.AccountID
 WHERE  ab.AccountId = @AccountID


 
 SELECT 'Account_Audit' [Table] ,
        ab.AccountID ,
        l.*
 FROM   RFOperations.RFO_Accounts.AccountBase ab
        LEFT JOIN RFOperations.Audit.RFOAccountsAccountRF l ON l.AccountId = ab.AccountID
 WHERE  ab.AccountId = @AccountID

	