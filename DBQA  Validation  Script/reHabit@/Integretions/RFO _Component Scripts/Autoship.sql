
   
   DECLARE @AccountID BIGINT=0007107710 --<<<<<------------>>> PLEASE USER CID ACCOUNTID HERE .



    SELECT 
            'Autoship' AS [TotalName] ,
           ab.AccountID, s.Name[AutoshipStatus],t.Name[AutoshipTypes],a.* 
    FROM    RFOperations.RFO_Accounts.AccountBase ab
            LEFT JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ab.AccountID
			LEFT JOIN RFOperations.RFO_Reference.AutoshipStatus s ON s.AutoshipStatusId = a.AutoshipStatusID
			LEFT JOIN RFOperations.RFO_Reference.AutoShipType t ON t.AutoShipTypeID=a.AutoshipTypeID
    WHERE   ab.AccountID = @AccountID



	
    SELECT  
            'Autoship_Items' AS [TotalName] ,  
			p.SKU,         
            p.SKU,ai.* 
    FROM    RFOperations.RFO_Accounts.AccountBase ab
            LEFT JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ab.AccountID
            LEFT JOIN RFOperations.Hybris.AutoshipItem ai ON ai.AutoshipId = a.AutoshipID
			LEFT JOIN RFOperations.Hybris.ProductBase p ON p.productID=ai.ProductID
    WHERE   ab.AccountID = @AccountID


	
    SELECT 
            'Autoship_Payment' AS [TotalName] ,v.Name[CreditCardType],t.Name[PaymentType],
            ap.* 
    FROM    RFOperations.RFO_Accounts.AccountBase ab
            LEFT JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ab.AccountID
            LEFT JOIN RFOperations.Hybris.AutoshipPayment ap ON ap.AutoshipID = a.AutoshipID
			LEFT JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID=ap.VendorID
			LEFT JOIN RFOperations.RFO_Reference.PaymentType t ON t.PaymentTypeID=ap.PaymentTypeID
    WHERE   ab.AccountID = @AccountID
	 


    SELECT 
            'AutoshipBillingAddress' AS [TotalName] ,co.Alpha2Code [Country],
            apa.* 
    FROM    RFOperations.RFO_Accounts.AccountBase ab
            LEFT JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ab.AccountID
            LEFT JOIN RFOperations.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = a.AutoshipID
			LEFT JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID=apa.CountryID
    WHERE   ab.AccountID = @AccountID




   SELECT
            'AutoshipShipment' AS [TotalName] ,
           m.Carrier,m.Name, os.* 
    FROM    RFOperations.RFO_Accounts.AccountBase ab
            LEFT JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ab.AccountID
            LEFT JOIN RFOperations.Hybris.AutoshipShipment os ON os.AutoshipID = a.AutoshipID
			LEFT JOIN RFOperations.RFO_Reference.ShippingMethod m ON m.ShippingMethodID = os.ShippingMethodID
    WHERE   ab.AccountID = @AccountID



	   SELECT  
            'AutoshipShippingAddress' AS [TotalName] ,co.Alpha2Code[Country],
            asa.*
    FROM    RFOperations.RFO_Accounts.AccountBase ab
            LEFT JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ab.AccountID
            LEFT JOIN RFOperations.Hybris.AutoshipShippingAddress asa ON asa.AutoShipID = a.AutoshipID
			LEFT JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID=asa.CountryID
    WHERE   ab.AccountID = @AccountID


	   SELECT  
            'AutoshipCancellation' AS [TotalName] ,
            alc.*
    FROM    RFOperations.RFO_Accounts.AccountBase ab
	LEFT JOIN RFOperations.Logging.autoshipDelayCancellationLog alc ON alc.accountId = ab.AccountID
    WHERE   ab.AccountID = @AccountID



  SELECT  
            'AutoshipDelay' AS [TotalName] ,
            alc.*
    FROM    RFOperations.RFO_Accounts.AccountBase ab
	LEFT JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ab.AccountID
	LEFT JOIN RFOperations.Logging.autoshipDelayCancellationLog alc ON alc.accountId = ab.AccountID AND a.AutoshipID=alc.templateId
    WHERE   ab.AccountID = @AccountID




  SELECT  
            'AutoshipLog' AS [TotalName] ,
            al.*
    FROM    RFOperations.RFO_Accounts.AccountBase ab
	LEFT JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ab.AccountID
	LEFT JOIN RFOperations.Logging.autoshipLog al ON al.AutoshipID = a.AutoshipID
    WHERE   ab.AccountID = @AccountID


	  SELECT  
            'AutoshipSwapLog' AS [TotalName] ,
            al.*
    FROM    RFOperations.RFO_Accounts.AccountBase ab
	LEFT JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ab.AccountID
	LEFT JOIN RFOperations.Logging.autoshipSwapLog al ON al.AccountId = a.AccountID AND al.TemplateId=a.AutoshipID
    WHERE   ab.AccountID = @AccountID