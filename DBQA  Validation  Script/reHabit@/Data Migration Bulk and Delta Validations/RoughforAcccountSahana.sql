SELECT  AccountID INTO #T
FROM    RFOperations.RFO_Accounts.AccountBase
WHERE   AccountID IN ( 966155, 516637, 584961, 505660, 782216, 882660, 529456,
                       706994, 732630, 588938, 1035292, 1014385, 6811424,
                       677097, 1010510, 506126, 1219899, 1148604, 1032755,
                       1330453, 634213, 504186, 781950, 1354704, 591643,
                       568915, 670996, 688111, 504611, 500149, 1208832, 625064,
                       566790, 518383, 779974, 674911, 512556, 725308, 525878,
                       663382, 610570, 795510, 503680, 1152003, 506603, 512168,
                       798471, 931718, 850611, 927408, 907116, 824741, 525701,
                       950758, 5316, 779747, 766068, 561426, 1490, 776139,
                       1777608, 1785220, 621657, 782404, 711335, 589901,
                       523092, 1496, 818363, 675335, 506141, 577869, 581304,
                       919821, 1000511, 548716, 619565, 826944, 598473, 521946,
                       920689, 683403, 504798, 1492874, 984548, 1189623,
                       552730, 580574 )




                       SELECT   AccountID INTO #TM
                       FROM     RFOperations.RFO_Accounts.AccountBase
                       WHERE    AccountID IN ( 500425, 1496, 515565, 500149,
                                               506393, 1499, 5351, 509031,
                                               512320, 503178, 518286, 511074,
                                               509329, 519512, 1548, 512695,
                                               513913, 519501, 517415, 517900,
                                               518712, 516265, 252, 508979,
                                               4112, 519045, 510628, 513874,
                                               513247, 2354, 507683, 514607,
                                               1753, 1974, 505235, 506367,
                                               506377, 510384, 511053, 512257,
                                               512504, 512556, 512667, 513496,
                                               514143, 515604, 517967, 518383,
                                               519203, 519429 )


											   SELECT * FROM #T t 
											   JOIN #TM m ON m.AccountID = t.AccountID



                                                   SELECT    ab.AccountID  ,--	p_customerid
                                                            rf.HardTerminationDate ,--	p_hardterminateddate
                                                            rf.SoftTerminationDate ,--	p_softterminateddate
                                                            ac.SecuredTaxNumber AS TaxNumber ,--	p_countrysecuritycode,
                                                            ab.AccountNumber ,--p_accountnumber
                                                            g.Name AS GenderID ,--	p_gender                                                           
                                                            ea.EmailAddress ,--	p_email
                                                            co.Alpha2Code AS CountryID ,--	p_country
                                                           
                                                           rf. SponsorID ,--	p_newsponsorid
                                                            CASE
                                                              WHEN s.Name = 'Hard Terminated'
                                                              THEN 'HARDTERMINATED'
                                                              ELSE s.Name
                                                            END AS AccountStatusID ,--	p_status
                                                            CASE
                                                              WHEN t.Name = 'Retail Customer'
                                                              THEN 'RETAIL'
                                                              WHEN t.Name = 'Preferred Customer'
                                                              THEN 'PREFERRED'
                                                              ELSE t.Name
                                                            END AS AccountTypes ,--	p_type 
                                                               rf.NextRenewalDate ,--	p_nextrenewaldate
                                                            rf.LastRenewalDate ,--	p_lastrenewaldate
                                                            rf.EnrollmentDate ,--	p_enrolementdate
                                                            CONCAT(ac.FirstName,
                                                              SPACE(1),
                                                              ac.LastName) Name ,--	p_name
                                                           
                                                              accs.UserName ,--	p_uid
                                                            accs.[password] ,--	PassWord                                                           
                                                            cu.Code AS CurrencyID ,--	p_sessioncurrency       
                                                            nl.ISOCode AS LanguageID ,--	p_sessionlanguage
                                                            rf.Active 
    --SELECT  ab.AccountNumber , COUNT(AccountNumber)
	--SELECT top 10 *
                                                   FROM     RFOperations.RFO_Accounts.AccountBase ab
                                                            JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID --AND ab.AccountNumber='001762'
                                                            JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
                                                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
                                                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = ab.AccountTypeID
                                                            JOIN RFOperations.RFO_Reference.NativeLanguage nl ON nl.LanguageID = ab.LanguageId
                                                            JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ab.CountryID
                                                            JOIN RFOperations.RFO_Reference.Currency cu ON cu.CurrencyID = ab.CurrencyID
                                                            JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
                                                            LEFT JOIN RFOperations.[Security].AccountSecurity accs ON accs.AccountID = ab.AccountID
                                                            LEFT JOIN RFOperations.RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId
                                                              AND ac.AccountContactTypeId = 1 AND ae.AccountEmailID=1
                                                            LEFT JOIN RFOperations.RFO_Accounts.EmailAddresses ea ON ea.EmailAddressID = ae.EmailAddressId
                                                           
                                                   WHERE    EXISTS ( SELECT
                                                              1
                                                              FROM
                                                              #t t
                                                              WHERE
                                                              t.accountid = ab.AccountID )



	
        SELECT     b.AccountID ,
		pp.PaymentProfileID ,--PaymentProfileID_Code
                b.AccountNumber AS [p_customerNumber] ,
          
                LTRIM(RTRIM(ccp.NameOnCard)) ProfileName , --	p_ccowner
                DisplayNumber , --	p_number	
                pp.IsDefault AS IsDefaultPayInfo ,
              --  apm.AccountNumber AS RFLCardNumber ,			 
                v.Name AS VendorID , --	p_type
                ExpMonth , --	p_validtomonth
                ExpYear , --	p_validtoyear
                ccp.BillingAddressID , --	p_billingaddress
                SUBSTRING(REVERSE(LTRIM(RTRIM(CCP.DisplayNumber))), 1, 4) AS LastFourNumber 
               
      --  INTO    #RFO
        FROM    RFOperations.RFO_Accounts.AccountBase b
                JOIN RFOperations.RFO_Accounts.PaymentProfiles (NOLOCK) PP ON b.AccountID = PP.AccountID
                --JOIN RodanFieldsLive.dbo.AccountPaymentMethods apm ON apm.AccountID = b.AccountID
                --                                              AND apm.AccountPaymentMethodID = PP.PaymentProfileID
                JOIN RFOperations.RFO_Accounts.CreditCardProfiles (NOLOCK) CCP ON PP.PaymentProfileID = CCP.PaymentProfileID
                JOIN RFOperations.RFO_Reference.CreditCardVendors (NOLOCK) V ON V.VendorID = CCP.VendorID
                JOIN Hybris.dbo.users hu ON CAST(PP.AccountID AS VARCHAR(255)) = hu.p_customerid
        WHERE   EXISTS ( SELECT 1
                         FROM   #t t
                         WHERE  t.accountid = b.AccountID )









						   SELECT  a.addressID ,
               CAST(ac.AccountId AS NVARCHAR(225)) AS AccountId ,
                co.Alpha2Code AS CountryID ,
                a.Region ,
                apm.PhoneNumber AS MainNumber ,
                adh.PhoneNumnber AS HomeNumber ,
                adc.PhoneNumnber AS CellNumber ,
                CASE WHEN g.Name IN ( 'male', 'female' ) THEN g.Name
                     ELSE NULL
                END AS GenderID , --	p_gender
                CASE WHEN a.AddressTypeID = 3 THEN 1
                     ELSE 0
                END AS IsBillingAddress , --	p_billingaddress
                CASE WHEN a.AddressTypeID = 1 THEN 1
                     ELSE 0
                END AS IsContactAddress , --	p_contactaddress
                CASE WHEN a.AddressTypeID = 2 THEN 1
                     ELSE 0
                END AS IsShippingAddress , --	p_shippingaddress
                a.Locale , --	p_town
                a.AddressLine1 , --	p_streetname
                a.AddressLine2 , --	p_streennumber
                ac.FirstName , --	p_firstname
                ac.LastName , --	p_lastname
                ac.MiddleName , --	p_MiddleName
                a.PostalCode , --	p_postalcode
                IIF(CAST(ac.BirthDay AS DATE) = '1900-01-01', NULL, ac.Birthday) AS BirthDay ,
				IsDefault
		--SELECT COUNT(a.AddressID)[repeat]--3331294		
        FROM    RFOperations.RFO_Accounts.Addresses a
                JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AddressID = a.AddressID
                                                              AND AddressTypeID = 1 -- Main AddressType
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = a.CountryID
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = aca.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
                LEFT JOIN ( SELECT  AccountcontactID ,
                                    ph.PhoneNumberRaw AS PhoneNumber
                            FROM    RFOperations.RFO_Accounts.AccountContactPhones aph
                                    JOIN RFOperations.RFO_Accounts.Phones ph ON ph.PhoneID = aph.PhoneId
                                                              AND PhoneTypeID = 1
                          ) apm ON apm.AccountContactId = ac.AccountContactId
                LEFT JOIN ( SELECT  adp.addressID ,
                                    p.PhoneNumberRaw AS PhoneNumnber
                            FROM    RFOperations.RFO_Accounts.AddressPhones adp
                                    JOIN RFOperations.RFO_Accounts.Phones p ON p.PhoneID = adp.PhoneId
                                                              AND p.PhoneTypeID = 2
                          ) adh ON adh.AddressId = a.AddressID
                LEFT JOIN ( SELECT  app.addressID ,
                                    p.PhoneNumberRaw AS PhoneNumnber
                            FROM    RFOperations.RFO_Accounts.AddressPhones app
                                    JOIN RFOperations.RFO_Accounts.Phones p ON p.PhoneID = app.PhoneId
                                                              AND p.PhoneTypeID = 3
                          ) adc ON adc.AddressId = a.AddressID
        WHERE   EXISTS ( SELECT 1
                         FROM   #t t
                         WHERE  t.accountid = ac.AccountID )









						   SELECT  
                CAST(ac.AccountId AS NVARCHAR(225)) AS AccountId ,
                co.Alpha2Code AS CountryID ,
                a.Region ,
                apm.PhoneNumber AS MainNumber ,
                adh.PhoneNumnber AS HomeNumber ,
                adc.PhoneNumnber AS CellNumber ,
                CASE WHEN g.Name IN ( 'male', 'female' ) THEN g.Name
                     ELSE NULL
                END AS GenderID , --	p_gender
                CASE WHEN a.AddressTypeID = 3 THEN 1
                     ELSE 0
                END AS IsBillingAddress , --	p_billingaddress
                CASE WHEN a.AddressTypeID = 1 THEN 1
                     ELSE 0
                END AS IsContactAddress , --	p_contactaddress
                CASE WHEN a.AddressTypeID = 2 THEN 1
                     ELSE 0
                END AS IsShippingAddress , --	p_shippingaddress
                a.Locale , --	p_town
                a.AddressLine1 , --	p_streetname
                a.AddressLine2 , --	p_streennumber
                ac.FirstName , --	p_firstname
                ac.LastName , --	p_lastname
                ac.MiddleName , --	p_MiddleName
                a.PostalCode , --	p_postalcode
                IIF(CAST(ac.BirthDay AS DATE) = '1900-01-01', NULL, ac.Birthday) AS BirthDay , --	p_dateofbirth
                IsDefault
       
        FROM    RFOperations.RFO_Accounts.Addresses a
                JOIN RFOperations.RFO_Accounts.AccountContactAddresses aca ON aca.AddressID = a.AddressID
                                                              AND AddressTypeID = 2 -- Shipping AddressType
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = a.CountryID
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountContactId = aca.AccountContactId
                                                              AND ac.AccountContactTypeId = 1
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
                LEFT JOIN ( SELECT  AccountcontactID ,
                                    ph.PhoneNumberRaw AS PhoneNumber
                            FROM    RFOperations.RFO_Accounts.AccountContactPhones aph
                                    JOIN RFOperations.RFO_Accounts.Phones ph ON ph.PhoneID = aph.PhoneId
                                                              AND PhoneTypeID = 1
                          ) apm ON apm.AccountContactId = ac.AccountContactId
                LEFT JOIN ( SELECT  adp.addressID ,
                                    p.PhoneNumberRaw AS PhoneNumnber
                            FROM    RFOperations.RFO_Accounts.AddressPhones adp
                                    JOIN RFOperations.RFO_Accounts.Phones p ON p.PhoneID = adp.PhoneId
                                                              AND p.PhoneTypeID = 2
                          ) adh ON adh.AddressId = a.AddressID
                LEFT JOIN ( SELECT  app.addressID ,
                                    p.PhoneNumberRaw AS PhoneNumnber
                            FROM    RFOperations.RFO_Accounts.AddressPhones app
                                    JOIN RFOperations.RFO_Accounts.Phones p ON p.PhoneID = app.PhoneId
                                                              AND p.PhoneTypeID = 3
                          ) adc ON adc.AddressId = a.AddressID
        
        WHERE    EXISTS ( SELECT 1
                         FROM   #t t
                         WHERE  t.accountid = ac.AccountID )




        SELECT  OrderNumber AS OrderNumber , --	p_code
                AutoShipID , --	p_associatedOrders
                s.Name AS OrderStatusID , --	p_status
                t.Name AS OrderTypeID , --	p_type
                co.Alpha2Code AS CountryID , --	p_country
                cu.Code AS CurrencyID , --	p_currency
                sa.AccountNumber AS ConsultantID , --	p_ordersponsorid /*  This one Converted to AccountNumber*/
                ab.AccountID , --	p_userpk  /*  This one Converted to AccountNumber*/
                CompletionDate , --	createdTS
                CommissionDate , --	p_commissiondate
                ro.ModifiedDate , --	modifiedTS
                SubTotal , --	p_subtotal
                Total , --	p_totalprice
                TotalTax , --	p_totaltax
                TotalDiscount , --	p_totaldiscount
                CV , --	p_cv
                QV , --	p_qv
                TaxExempt , --	isTaxExempt
                donotship , --	p_donotship,
                ( os.ShippingCost + os.HandlingCost ) AS ShippingCost , --	p_deliverycost
                ( os.TaxOnShippingCost + os.TaxOnHandlingCost ) AS TaxOnShippingCost  --	p_taxonshippingcost       
				SELECT DISTINCT ro.OrderID INTO #O
        FROM    RFOperations.Hybris.Orders ro
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ro.CountryID
                JOIN RFOperations.RFO_Reference.Currency cu ON cu.CurrencyID = ro.CurrencyID
                JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = ro.AccountID
                JOIN RFOperations.RFO_Accounts.AccountBase sa ON sa.AccountID = ConsultantID
                JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = ro.OrderStatusID
                JOIN RFOperations.RFO_Reference.OrderType t ON t.OrderTypeID = ro.OrderTypeID
                LEFT JOIN ( SELECT  OrderID ,
                                    SUM(ShippingCost) ShippingCost ,
                                    SUM(HandlingCost) HandlingCost ,
                                    SUM(TaxOnHandlingCost) TaxOnHandlingCost ,
                                    SUM(TaxOnShippingCost) TaxOnShippingCost
                            FROM    RFOperations.Hybris.OrderShipment
                            GROUP BY OrderID
                          ) os ON os.OrderID = ro.OrderID
        WHERE        EXISTS ( SELECT 1
                         FROM   #t t
                         WHERE  t.accountid = ro.AccountID )
                AND CAST(ro.CompletionDate AS DATE) > DATEADD(MONTH,-18,'2017-02-28')

				ORDER BY ab.AccountID

				
   SELECT --DISTINCT  --- OrderBillingAddress has a duplicates records that's why to eliminate
                ro.OrderNumber ,
                co.Alpha2Code AS CountryID ,--	 p_country
                oba.FirstName ,--	p_firstname
                oba.LastName ,--	p_lastname
                Address1 ,--	p_streetname
                AddressLine2 ,--	p_streetNumber
                PostalCode ,--	p_postalcode
                Locale ,--	p_town
                Region ,--	p_region
                Telephone ,--	p_phone1
               ISNULL(oba.MiddleName,'')MiddleName ,--	p_middlename
                IIF(CAST(ac.BirthDay AS DATE) = '1900-01-01', NULL, ac.Birthday) AS BirthDay ,--	p_dateofbirth
                g.Name AS GenderID --	p_gender  
       
        FROM    RFOperations.Hybris.OrderBillingAddress oba
                --JOIN RFOperations.Hybris.OrderPayment op ON op.OrderID = oba.OrderID
                JOIN RFOperations.Hybris.orders ro ON ro.OrderID = oba.OrderID
                --JOIN RFOperations.Hybris.OrderPaymentTransaction opt ON opt.OrderPaymentID = op.OrderPaymentID
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ro.AccountID AND ac.AccountContactTypeId=1
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = oba.CountryID
        WHERE   EXISTS ( SELECT 1
                         FROM   #o o WHERE o.OrderID=ro.OrderID )



						      SELECT  ro.OrderNumber ,--	p_order
                LineItemNo - 1 AS LineItemNo ,--	p_entrynumber
                p.SKU AS ProductID ,--	p_product
                oi.Quantity ,--	p_quantity
                oi.BasePrice ,--	p_baseprice
                oi.TotalPrice ,--	p_totalprice
                oi.TotalTax ,--	p_taxvaluesinternal
                oi.CV ,--	p_cv
                oi.QV	--	p_qv
      
        FROM    RFOperations.Hybris.OrderItem oi
                JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = oi.OrderId
                JOIN RFOperations.Hybris.ProductBase p ON p.productID = oi.ProductID
         WHERE   EXISTS ( SELECT 1
                         FROM   #o o WHERE o.OrderID=ro.OrderID )








						  SELECT --DISTINCT -- There are duplicate records in the RFO itself we should use distinct to filter out
                OrderPaymentID ,--	p_code
                ro.OrderNumber  ,--	OwnerpkString
                AmountTobeAuthorized ,--	p_plannedAmout
                ExpYear ,--	p_validtoyear
                Expmonth ,--	p_validtomonth
                v.Name AS VendorID ,--	p_type
                paymentprovider ,--	paymentprovider
                op.DisplayNumber AS AccountNumber ,--	p_number
                CONCAT(ob.FirstName, SPACE(1), ob.LastName) CCowner   
        FROM    RFOperations.Hybris.OrderPayment op
                JOIN RFOperations.Hybris.orders ro ON ro.OrderID = op.OrderID
                JOIN ( SELECT   OrderID ,
                                MAX(FirstName) FirstName ,
                                MAX(Lastname) Lastname
                       FROM     RFOperations.Hybris.OrderBillingAddress
                       GROUP BY OrderID
                     ) ob ON ob.OrderID = ro.OrderID
                JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = op.VendorID
				-- At lease those Orderpayment should have a transactions.
        WHERE   EXISTS ( SELECT 1
                         FROM   RFOperations.Hybris.OrderPaymentTransaction opt
                         WHERE  opt.OrderPaymentID = op.OrderPaymentID )AND
						 -- Only from migrated Orders.
                 EXISTS ( SELECT 1
                         FROM   #o o WHERE o.OrderID=ro.OrderID )

  SELECT  ro.OrderNumber  ,
                co.Alpha2Code AS CountryID ,--	p_country
                osa.FirstName ,--	p_firstname
                osa.LastName ,--	p_lastname
                Address1 ,--	p_streetname
                AddressLine2 ,--	p_streetNumber
                PostalCode ,--	p_postalcode
                Locale ,--	p_town
                Region ,--	p_region
                Telephone ,--	p_phone1
                osa.MiddleName ,--	p_middlename
                BirthDay ,--	p_dateofbirth
                GenderID --	p_gender	       	
        FROM    RFOperations.Hybris.OrderShippingAddress osa
                JOIN RFOperations.Hybris.orders ro ON ro.OrderID = osa.OrderID 
                --JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
                LEFT JOIN RFOperations.RFO_Accounts.accountcontacts ac ON ac.AccountId = ro.AccountID
                                                              AND ac.AccountContactTypeId = 1 
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = osa.CountryID
        WHERE     EXISTS ( SELECT 1
                         FROM   #o o WHERE o.OrderID=ro.OrderID )









						 	
        SELECT  CASE WHEN a.AutoshipTypeID = 1 THEN 'PC'
                     WHEN a.AutoshipTypeID = 2 THEN 'CRP'
                     WHEN a.AutoshipTypeID = 3 THEN 'PULSE'
                END AS AutoshipTypeID ,--	Typepkstring
                CAST(a.AccountID AS NVARCHAR(225)) AccountID ,--	p_user
                a.Active ,--	p_active
                a.SubTotal ,--	p_subtotal
                a.Total ,--	p_totalprice
                a.QV ,--	p_qv
                a.CV ,--	p_cv
                a.StartDate ,--	createdTS
                a.NextRunDate ,--	p_nextrundate
                a.TaxExempt ,--	p_istaxexempt
                a.ServerModifiedDate ,--	ModifiedTS
                CAST(a.ConsultantID AS NVARCHAR(225)) ConsultantID ,--	p_ordersponsorid
                a.AutoshipNumber ,--	p_code
                a.TotalTax ,--	p_totaltax
                a.TotalDiscount ,--	P_totaldiscounts
                a.donotship ,--	p_donotship
                ( aus.HandlingCost + aus.ShippingCost ) AS ShippingCost ,--	p_deliverycost
                ( aus.TaxOnHandlingCost + aus.TaxOnShippingCost ) AS TaxOnShippingCost--	p_taxonshippingcost     
       SELECT DISTINCT a.AutoshipID INTO #a
        FROM    RFOperations.Hybris.Autoship a
                JOIN ( SELECT DISTINCT
                                ap.AutoshipID
                       FROM     RFOperations.Hybris.AutoshipPayment ap
                                JOIN RFOperations.Hybris.AutoshipShipment os ON os.AutoshipID = ap.AutoshipID
                                JOIN RFOperations.Hybris.AutoshipItem oi ON oi.AutoshipId = ap.AutoshipID
                                JOIN RFOperations.Hybris.AutoshipShippingAddress asa ON asa.AutoShipID = ap.AutoshipID
                                JOIN RFOperations.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = ap.AutoshipID
                     ) ap ON ap.AutoshipID = a.AutoshipID  --AND a.AccountID = 2083918
                LEFT  JOIN ( SELECT DISTINCT
                                    AutoshipID ,
                                    SUM(ShippingCost) ShippingCost ,
                                    SUM(TaxOnShippingCost) TaxOnShippingCost ,
                                    SUM(HandlingCost) HandlingCost ,
                                    SUM(TaxOnHandlingCost) TaxOnHandlingCost
                             FROM   RFOperations.Hybris.AutoshipShipment
                             GROUP BY AutoshipID
                           ) aus ON aus.AutoshipID = a.AutoshipID
        WHERE   EXISTS ( SELECT 1 FROM #t t WHERE t.accountid=a.AccountID )
                AND a.Active = 1
				ORDER BY AccountID ,a.AutoshipID
				--GROUP BY a.AutoshipID
				--HAVING count(a.AutoshipID)>1
   
   
   
   SELECT  a.AutoshipNumber  , --	Ownerpkstring
                LineItemNo , --	p_entrynumber
                p.SKU AS ProductID , --	p_product
                ai.Quantity , --	p_quantity
                ai.BasePrice , --	p_baseprice
                ai.TotalPrice , --	p_totalprice
                ai.TotalTax , --	p_taxvaluesinternal
                ai.CV , --	p_cv
                ai.QV , --	p_qv
                ai.ServerModifiedDate	 --	modifiedTS
      
		--SELECT COUNT(ai.AutoshipId)--2929710
        FROM    RFOperations.Hybris.AutoshipItem ai
                JOIN RFOperations.Hybris.Autoship a ON a.AutoshipID = ai.AutoshipId
                                                       AND a.Active = 1
                LEFT JOIN RFOperations.Hybris.ProductBase p ON p.productID = ai.ProductID
        WHERE   EXISTS ( SELECT  1 FROM #a a1 WHERE a1.AutoshipID=a.AutoshipID) -- Migrated Templates Only.



		  SELECT  a.AccountID ,
                a.AutoshipNumber AS [RFOKey] , --	Ownerpkstring
                AutoshipPaymentID , --	p_code
                cc.name AS VendorID , --	p_type
                ap.Expmonth , --	p_validtomonth
                ap.ExpYear , --	p_validtoyear
                ap.DisplayNumber , --	p_number
                LTRIM(RTRIM(CONCAT(FirstName, ' ', LastName))) AS Name , --	p_ccowner
               -- o.paymentprofileID
                apa.AutoshipPaymentAddressID AS BillingAddressID 
     
        FROM    RFOperations.Hybris.AutoshipPayment ap
                JOIN RFOperations.Hybris.Autoship a ON a.AutoshipID = ap.AutoshipId
                                                       AND a.Active = 1
                                                      -- AND AutoshipNumber = 52148
                JOIN RFOperations.RFO_reference.CreditCardVendors cc ON cc.VendorID = ap.VendorID
                JOIN ( SELECT   AutoshipID ,
                                MAX(firstName) firstName ,
                                MAX(LastName) LastName ,
                                MAX(AutoshipPaymentAddressID) AutoshipPaymentAddressID
                       FROM     RFOperations.Hybris.AutoshipPaymentAddress
                       GROUP BY AutoShipID
                     ) apa ON apa.AutoShipID = ap.AutoshipID
                --LEFT JOIN #Original o ON o.DisplayNumber = ap.DisplayNumber
                --                         AND o.ExpMonth = ap.Expmonth
                --                         AND o.ExpYear = ap.ExpYear
        WHERE   EXISTS ( SELECT  1 FROM #a a1 WHERE a1.AutoshipID=a.AutoshipID) -- Migrated Templates Only.


		
        SELECT  a.AutoshipNumber,
                apa.FirstName , --	p_firstname
                apa.LastName , --	p_lastname
                co.Alpha2Code AS CountryID , --	p_country
                Address1 , --	p_streetname
                AddressLine2 , --	p_streetNumber
                PostalCode , --	p_postalcode
                Locale , --	p_town
                Region , --	p_region
                Telephone , --	p_phone1
                apa.MiddleName , --	p_middlename
                IIF(CAST(ac.Birthday AS DATE) = '1900-01-01', NULL, ac.Birthday) DateOfBirth , --	p_dateofbirth
                CASE WHEN g.Name IN ( 'Male', 'Female' ) THEN g.Name
                     ELSE NULL
                END AS GenderID  --	p_gender 
     
        FROM    RFOperations.Hybris.AutoshipPaymentAddress apa
                JOIN RFOperations.Hybris.Autoship a ON a.AutoshipID = apa.AutoshipId
                                                       AND a.Active = 1
                LEFT JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = a.AccountID
                                                              AND ac.AccountContactTypeId = 1
                LEFT JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
                LEFT JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = apa.CountryID
      WHERE   EXISTS ( SELECT  1 FROM #a a1 WHERE a1.AutoshipID=a.AutoshipID) -- Migrated Templates Only.


	  
        SELECT  a.AutoshipNumber AS [RFOKey] ,
                asa.FirstName , --	p_firstname
                asa.LastName , --	p_lastname
                co.Alpha2Code AS CountryID , --	p_country
                Address1 , --	p_streetname
                AddressLine2 , --	p_streetNumber
                PostalCode , --	p_postalcode
                Locale , --	p_town
                Region , --	p_region
                Telephone , --	p_phone1
                asa.MiddleName , --	p_middlename
                IIF(CAST(ac.Birthday AS DATE) = '1900-01-01', NULL, ac.Birthday) DateOfBirth , --	p_dateofbirth
                CASE WHEN g.Name IN ( 'Male', 'Female' ) THEN g.Name
                     ELSE NULL
                END AS GenderID  --	p_gender 	
  
        FROM    RFOperations.Hybris.AutoshipShippingAddress asa
                JOIN RFOperations.Hybris.Autoship a ON a.AutoshipID = asa.AutoshipId
                LEFT JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = a.AccountID
                                                              AND ac.AccountContactTypeId = 1
                LEFT JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
                LEFT JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = asa.CountryID
           WHERE   EXISTS ( SELECT  1 FROM #a a1 WHERE a1.AutoshipID=a.AutoshipID) -- Migrated Templates Only.



		        SELECT  ReturnOrderNumber ,--	p_code
            
                o.OrderNumber ,--	p_parent
                rs.Name AS ReturnStatusID ,--	p_status
               CAST(ro.AccountID AS NVARCHAR(225))AccountID ,--	p_user
                CAST(ro.ConsultantID AS NVARCHAR(225))ConsultantID ,--	p_spronsorid
                co.Alpha2Code AS CountryID ,--	p_country
                cu.Code AS CurrencyID ,--	p_currency
                ro.CompletionDate ,--	createdTS
                ro.CommissionDate ,--	p_commissiondate
                ABS(ro.SubTotal) AS SubTotal ,--	p_subtotal
                ABS(ro.Total) AS Total ,--	p_totalprice
                ABS(ro.TotalTax) AS TotalTax ,--	p_tataltax
                ABS(ro.TotalDiscount) AS TotalDiscount ,--	p_totaldiscounts
                ro.CV ,--	p_cv
                ro.QV ,--	p_totalsv
                ABS(( ro.RefundedHandlingCost + ro.RefundedShippingCost )) AS DeliveryCost ,--	p_deliverycost
                CAST(ISNULL(tax.TaxAmount,0) AS MONEY) AS DeliveryTax --	p_handlingcost 
				SELECT DISTINCT ro.ReturnOrderID INTO #r
        FROM    RFOperations.Hybris.ReturnOrder ro
                JOIN RFOperations.RFO_Reference.ReturnStatus rs ON rs.ReturnStatusId = ro.ReturnStatusID
                JOIN RFOperations.Hybris.orders o ON o.OrderID = ro.OrderID
                LEFT JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ro.CountryID
               LEFT JOIN RFOperations.RFO_Reference.Currency cu ON cu.CurrencyID = ro.CurrencyID
                LEFT JOIN ( SELECT  ReturnOrderID ,
                                    SUM(Taxamount) AS TaxAmount
                            FROM    RFOperations.Hybris.ReturnOrderTax
                            GROUP BY ReturnOrderID
                          ) tax ON tax.ReturnOrderID = ro.ReturnOrderID
        WHERE   ro.ReturnStatusID = 5 -- Success Return only.
                AND EXISTS ( SELECT 1 FROM #o o1 WHERE o1.OrderID=o.OrderID)



  SELECT    ro.ReturnOrderNumber  ,--	p_order 
  ri.ReturnItemID ,
                ri.ExpectedQuantity ,--	p_expectedquantity
                ri.CV ,--	p_cv
                ri.QV ,--	p_qv
                --ri.ReceivedQuantity ,--	p_Receivedquantity
                ABS(ri.BasePrice) BasePrice ,--	p_baseprice
                ABS(ri.TotalPrice) TotalPrice ,--	p_totalprice
                ISNULL(Tax.TaxAmount,'0.0000') AS totaltax ,--	p_taxvaluesinternal
                ri.ReceivedQuantity ,--	p_quentity
              
                ri.ReturnReasonID ,--	p_reason
                ri.ReturnStatusID ,--	p_status
                rn.Notes AS ReturnNotes ,--	p_notes
                [Action] ,--	p_action
                p.SKU AS ProductID ,--	p_product
                --	p_returnrequest
                ri.OrderItemID --	p_orderentry
                ,
                ro.OrderID
    
	  
        FROM    RFOperations.Hybris.ReturnItem ri
                JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = ri.ReturnOrderID 
                /*
				JOIN RFOperations.Hybris.orders o ON o.OrderID = ro.OrderID
                                                   --  AND ro.ReturnOrderNumber = '28255022'
                JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
                                                         AND oi.OrderItemID = ri.OrderItemID -- Only those items which is in OrderItems table.
														 */
                JOIN RFOperations.Hybris.ProductBase p ON p.productID = ri.ProductID
                LEFT  JOIN ( SELECT ReturnOrderID ,
                                    MAX(Notes) Notes
                             FROM   RFOperations.Hybris.ReturnNotes
                             GROUP BY ReturnOrderID
                           ) rn ON rn.ReturnOrderID = ro.ReturnOrderID  -- ReturnOrder Has Duplicate Notes.
                LEFT JOIN ( SELECT  SUM(TaxAmount) TaxAmount ,
                                    ReturnItemID
                            FROM    RFOperations.Hybris.ReturnItemTax
                            GROUP BY ReturnItemID
                          ) tax ON tax.ReturnItemID = ri.ReturnItemID
        WHERE   ro.ReturnStatusID = 5 -- Success Return only.
                AND EXISTS ( SELECT 1
                             FROM  #r r WHERE r.ReturnOrderID=ro.ReturnOrderID)--586156






        SELECT  ro.ReturnOrderNumber ,--	OwnerPkstring
                v.Name AS VendorID ,--	p_type
                rp.ExpYear ,--	p_validtoyear
                rp.Expmonth ,--	p_validtomonth
                rp.AmountToBeAuthorized ,--	p_plannedamount
                CONCAT(rp.DisplayNumber, '_', rp.Expmonth, '_', rp.ExpYear) SubscriptionID ,--	p_subscriptionid
                 CONCAT(rba.FirstName, SPACE(1),rba.MiddleName,SPACE(1), rba.LastName) AS Name ,--	p_ccowner
                rp.DisplayNumber AS AccountNumber ,--	p_number
                rp.ReturnPaymentId ,
                CAST(ro.AccountID AS NVARCHAR(225)) AS AccountID
        FROM    RFOperations.Hybris.ReturnPayment rp
                JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = rp.ReturnOrderID
                LEFT JOIN ( SELECT  ReturnOrderID ,
                                    MAX(FirstName) FirstName ,
                                    MAX(LastName) LastName,
									MAX(MiddleName)MiddleName
                            FROM    RFOperations.Hybris.ReturnBillingAddress
                            GROUP BY ReturnOrderID
                          ) rba ON rba.ReturnOrderID = ro.ReturnOrderID
                LEFT JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = rp.VendorID
        WHERE   ro.ReturnStatusID = 5 -- Success Return only.
               AND EXISTS ( SELECT 1
                             FROM  #r r WHERE r.ReturnOrderID=ro.ReturnOrderID)--586156







		   SELECT  ro.ReturnOrderNumber  ,
                rp.ReturnPaymentId ,--	p_code
                ABS(rpt.AmountAuthorized) AS AmountAuthorized ,--	p_plannedamount
                rpt.TransactionID ,--	p_requestid
                AuthorizeType ,--	p_transactionStatus       
                ProcessDate ,--	p_time
               --TransactionID ,--	p_requestid       
                v.Name AS PaymentTypeID --	p_type
      
        FROM    RFOperations.Hybris.ReturnPayment rp
                LEFT JOIN RFOperations.Hybris.ReturnPaymentTransaction rpt ON rpt.ReturnPaymentId = rp.ReturnPaymentId
                JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = rp.ReturnOrderID
                JOIN RFOperations.RFO_Reference.ReturnStatus rs ON rs.ReturnStatusId = ro.ReturnStatusID
                JOIN RFOperations.Hybris.orders o ON o.OrderID = ro.OrderID
                JOIN RFOperations.RFO_Reference.CreditCardVendors v ON v.VendorID = rp.PaymentTypeID
        WHERE   ro.ReturnStatusID = 5 -- Success Return only.
                AND EXISTS ( SELECT 1
                             FROM  #r r WHERE r.ReturnOrderID=ro.ReturnOrderID)--586156



							  SELECT  ra.ReturnBillingAddressID ,
                ro.ReturnOrderNumber ,
                ra.FirstName ,--	p_firstname
                ISNULL(ra.MiddleName, '') MiddleName ,--	p_middlename
                ra.LastName ,--	p_lastname
                AddressLine1 ,--	p_streetname
                AddressLine2 ,--	p_streetNumber
                PostalCode ,--	p_postalcode
                Locale ,--	p_town
                Region ,--	p_region
                co.Alpha2Code AS CountryID ,--	p_country
                Telephone ,--	p_phone1
                IIF(CAST(BirthDay AS DATE) = '1900-01-01', NULL, Birthday) AS BirthDay ,--	p_dateofbirth
                CASE WHEN g.Name IN ( 'male', 'female' ) THEN g.Name
                     ELSE NULL
                END AS GenderID  
        FROM    RFOperations.Hybris.ReturnBillingAddress ra
                JOIN RFOperations.Hybris.ReturnOrder ro ON ro.ReturnOrderID = ra.ReturnOrderID
                --JOIN RFOperations.RFO_Reference.ReturnStatus rs ON rs.ReturnStatusId = ro.ReturnStatusID
                --JOIN RFOperations.Hybris.orders o ON o.OrderID = ro.OrderID
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ra.CountryID
                LEFT JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ro.AccountID
                                                              AND ac.AccountContactTypeId = 1
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
        WHERE   ro.ReturnStatusID = 5 -- Success Return only.
                AND EXISTS ( SELECT 1
                             FROM  #r r WHERE r.ReturnOrderID=ro.ReturnOrderID)--586156


