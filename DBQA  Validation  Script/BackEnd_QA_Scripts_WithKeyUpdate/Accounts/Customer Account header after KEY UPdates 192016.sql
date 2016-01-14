USE [DataMigration];
GO
SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO
SET NOCOUNT ON;
SET ANSI_WARNINGS OFF; 

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;


/*=================================================================================================
-- Part 1: Counts, Missing Keys, Duplicates 
================================================================================================= */



IF OBJECT_ID('DataMigration.Migration.AccountsMissing') IS NOT NULL
    DROP TABLE DataMigration.Migration.AccountsMissing;


IF OBJECT_ID('TEMPDB.dbo.#Accounts_Dups') IS NOT NULL
    DROP TABLE #Accounts_Dups; 

IF OBJECT_ID('TEMPDB.dbo.#AccountIDs') IS NOT NULL
    DROP TABLE #AccountIDs;

SET ANSI_WARNINGS OFF;
 
IF OBJECT_ID('TEMPDB.dbo.#PCNoTemplate') IS NOT NULL
    DROP TABLE #PCNoTemplate;

SELECT  ab.AccountID
INTO    #PCNoTemplate
FROM    RFOperations.RFO_Accounts.AccountBase ab
        LEFT JOIN ( SELECT a.AccountID FROM  RFOperations.[Hybris].[Autoship] A 
		 JOIN RodanFieldsLive.dbo.AutoshipOrders ao  ON ao.TemplateOrderID = a.AutoshipNumber
         JOIN RFOperations.Hybris.AutoshipItem AI ON AI.AutoshipId = A.AutoshipID ) sub ON sub.AccountID = ab.AccountID

WHERE   ab.CountryID = 236
        AND ab.AccountTypeID = 2
        AND (sub.AccountID IS NULL); 



DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @RFOCountry INT = ( SELECT  CountryID
                            FROM    RFOperations.RFO_Reference.Countries (NOLOCK)
                            WHERE   Alpha2Code = @Country
                          ) ,
    @RowCount BIGINT ,
    @HybCountry BIGINT = ( SELECT   PK
                           FROM     Hybris.dbo.countries (NOLOCK)
                           WHERE    isocode = @Country
                         );
							 --SELECT @HybCountry



--DECLARE @Loaddate DATETIME2 = ( SELECT TOP 1
--                                        DateValue
--                                FROM    DataMigration.dbo.Configuration
--                                WHERE   Name = 'OrderLoadStartDate'
--                              );
				





SELECT   DISTINCT
        a.AccountID
INTO    #AccountIDs --COUNT( DISTINCT a.AccountID)
FROM    RFOperations.RFO_Accounts.AccountRF (NOLOCK) a
        JOIN RFOperations.RFO_Accounts.AccountBase (NOLOCK) b ON a.AccountID = b.AccountID
        JOIN RFOperations.RFO_Accounts.AccountContacts (NOLOCK) d ON b.AccountID = d.AccountID
        JOIN RFOperations.RFO_Accounts.AccountEmails (NOLOCK) e ON e.AccountContactId = d.AccountContactId
        JOIN RFOperations.RFO_Accounts.AccountContactAddresses (NOLOCK) g ON g.AccountContactId = d.AccountContactId
        JOIN RFOperations.RFO_Accounts.AccountContactPhones (NOLOCK) j ON j.AccountContactId = d.AccountContactId
        LEFT JOIN RFOperations.RFO_Accounts.Phones (NOLOCK) p ON j.PhoneId = p.PhoneId
                                                              AND p.PhoneTypeID = 1
        LEFT JOIN RFOperations.RFO_Accounts.Addresses (NOLOCK) i ON i.AddressID = g.AddressID
                                                              AND i.AddressTypeID = 1
                                                              AND i.IsDefault = 1
        LEFT JOIN RFOperations.RFO_Accounts.EmailAddresses (NOLOCK) f ON f.EmailAddressID = e.EmailAddressId
                                                              AND EmailAddressTypeID = 1
        LEFT JOIN RFOperations.Security.AccountSecurity (NOLOCK) k ON k.AccountID = a.AccountID
        LEFT JOIN ( SELECT DISTINCT
                            AccountID
                    FROM    RFOperations.Hybris.Orders (NOLOCK) d
                            JOIN RFOperations.ETL.OrderDate (NOLOCK) od ON d.OrderID = od.Orderid
                    WHERE   od.Startdate >= '06/01/2014'
                  ) c ON b.AccountID = c.AccountID
WHERE   1 = 1
        AND b.CountryID = @RFOCountry
        AND ( SoftTerminationDate IS NULL
              OR SoftTerminationDate >= '06/01/2014'
            )
        AND ( b.AccountStatusID <> 3
              OR c.AccountID IS NOT NULL
            )
        AND f.EmailAddressID IS NOT NULL
        AND i.AddressID IS NOT NULL
        AND p.PhoneId IS NOT NULL
        AND k.AccountID IS NOT NULL
        AND NOT EXISTS ( SELECT 1
                         FROM   #PCNoTemplate p
                         WHERE  p.AccountID = a.AccountID );
--AND NOT EXISTS (SELECT 1 FROM Hybris.dbo.Users u WHERE u.p_rfAccountID = CAST (a.AccountID AS VARCHAR) AND p_sourcename IS NULL)
--AND NOT EXISTS (SELECT 1 FROM DataMigration.dbo.EmailAddressinHybris ea WHERE ea.uid = k.Username)



DECLARE @RFOAccount BIGINT, @HYBAccount BIGINT
SELECT @RFOAccount =COUNT( DISTINCT AccountID) FROM RFOPerations.RFO_Accounts.AccountBase (NOLOCK)
WHERE CountryID =@RFOCountry AND AccountID IN (SELECT AccountID FROM #AccountIDs)


SELECT @HYBAccount=COUNT(PK) FROM Hybris.dbo.Users (NOLOCK) WHERE  p_sourcename = 'Hybris-DM' --p_country =@HybCountry AND

SELECT 'Accounts', @RFOAccount AS RFO_Accounts, @HYBAccount AS Hybris_Accounts, @RFOAccount - @HYBAccount AS Differences


/******************************************************************************************************************************************************************************
-- FOR TROUBLESHOOTING COUNT DIFFERENCES

SELECT p_rfaccountID FROM Hybris.dbo.Users (NOLOCK) u WHERE p_country =8796100624418  AND p_sourcename = 'Hybris-DM'
AND NOT EXISTS (SELECT 1 FROM #AccountIDs aid WHERE u.p_rfAccountID = CAST (aid.AccountID AS VARCHAR))

SELECT AccountID FROM #AccountIDs  aid
WHERE NOT EXISTS (SELECT 1 FROM Hybris.dbo.Users  (NOLOCK) u WHERE p_country =8796100624418  AND p_sourcename = 'Hybris-DM' AND u.p_rfAccountID = CAST (aid.AccountID AS VARCHAR))
**********************************************************************************************************************************************************************************/

----------------------------------------------------------------------------------------------------------------------------------
-- Missing Keys 
----------------------------------------------------------------------------------------------------------------------------------

--DROP TABLE DataMigration.Migration.AccountsMissing


SELECT  AccountID AS RFO_AccountID,
 b.p_rfaccountid AS Hybris_rfAccountID , CASE WHEN b.p_rfaccountid IS NULL THEN 'Destination'
											  WHEN a.AccountID IS NULL THEN 'Source' END AS MissingFROM
INTO DataMigration.Migration.AccountsMissing
FROM 


( SELECT AccountID FROM #AccountIDs) a
  
    FULL OUTER JOIN 
   
   ( SELECT p_rfAccountID FROM  Hybris.dbo.Users  (NOLOCK)
    WHERE p_country = @HybCountry AND p_sourcename = 'Hybris-DM') b 
	
	ON CAST (a.AccountID AS VARCHAR) =b.p_rfaccountid
WHERE (a.AccountID IS NULL OR b.p_rfaccountid IS NULL) 

SELECT ' Total Missing ' + @Country + ' Accounts', @@ROWCOUNT

SELECT MissingFROM, COUNT(*)
FROM DataMigration.Migration.AccountsMissing  
GROUP BY MissingFROM


/***************************************************************************************************************
--- TROUBLESHOOTING Missing Records ---

SELECT * FROM RFOperations.RFO_Accounts.Accountbase
	WHERE AccountID  IN (SELECT RFO_AccountID FROM  DataMigration.Migration.AccountsMissing 
								WHERE MissingFROM = 'Destination'AND AccountTypeId <>'2')

SELECT * FROM Hybris.dbo.USERS
	WHERE p_rfAccountID  IN (SELECT CAST(RFO_AccountID AS VARCHAR) FROM  DataMigration.Migration.AccountsMissing 
								WHERE MissingFROM = 'Destination') 
								
 SELECT * FROM Hybris.dbo.USERs
	WHERE p_rfAccountID  IN (SELECT CAST(RFO_AccountID AS VARCHAR) FROM  DataMigration.Migration.AccountsMissing 
								WHERE MissingFROM = 'Destination') 
							
********************************************************************************************************/	
							



--------------------------------------------------------------------------------------
-- Duplicates 
--------------------------------------------------------------------------------------

    SELECT  AccountID ,
            COUNT(PK) AS CountofDups
    INTO    #Accounts_Dups
    FROM    Hybris.dbo.users a
            JOIN RFOperations.RFO_Accounts.AccountBase b ON a.p_rfaccountid = CAST(b.AccountID AS VARCHAR)
    WHERE   CountryID = @RFOCountry
    GROUP BY AccountID
    HAVING  COUNT(PK) > 1; 


        SELECT  'Duplicate ' + CAST(@@ROWCOUNT AS VARCHAR) + ' Accounts in Hybris';

            SELECT  *
            FROM    #Accounts_Dups;

       
SELECT a.p_rfaccountid, PK, p_sourcename, uniqueid, modifiedts, * FROM Hybris.dbo.Users a
WHERE p_rfAccountID IN (SELECT CAST (AccountID AS VARCHAR) FROM #Accounts_Dups)
ORDER BY a.p_rfaccountid


	/*
/*=================================================================================================
-- Part 2 EXCEPTS TESTS 
================================================================================================= */
IF OBJECT_ID('TEMPDB.dbo.#Accounts') IS NOT NULL
    DROP TABLE #Accounts; 
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Accounts') IS NOT NULL
    DROP TABLE #Hybris_Accounts;
IF OBJECT_ID('TEMPDB.dbo.#RFO_Accounts') IS NOT NULL
    DROP TABLE #RFO_Accounts;

DECLARE @RFOCountry INT = (SELECT CountryID  FROM RFOperations.RFO_Reference.Countries WHERE Alpha2Code = 'US'),
			@HybCountry BIGINT = (SELECT PK FROM Hybris.dbo.Countries WHERE isocode = 'US' );


DECLARE @Loaddate DATETIME2 = ( SELECT TOP 1
                                        DateValue
                                FROM    DataMigration.dbo.Configuration
                                WHERE   Name = 'OrderLoadStartDate'
                              );

----------------------------------------------------------------------------------------------------------------------

--- Load Accounts Excepts 
---------------------------------------------------------------------------------------------------------------------

  ;WITH  PayInfo
          AS ( SELECT   a.AccountID , 
                       a.PaymentProfileID, ad.AddressID
               FROM     RFOperations.RFO_Accounts.PaymentProfiles a
			   JOIN RodanFieldsLive.dbo.AccountPaymentMethods apm ON a.PaymentProfileID =apm.AccountPaymentMethodID
			   JOIN   RFoperations.RFO_accounts.CreditCardProfiles ccp ON ccp.PaymentProfileID = a.PaymentProfileID
			   JOIN  RFOperations.RFO_Accounts.Addresses Ad ON ccp.BillingAddressID =ad.AddressID
               WHERE    a.IsDefault = 1 AND ad.IsDefault=1 

             ),
        Shipping
          AS ( SELECT   AC.AccountId ,
                        MAX(A.AddressID) AS AddressID
               FROM     RFOperations.RFO_Accounts.Addresses A
                        JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.AddressID = A.AddressID
                        JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountContactId = ACA.AccountContactId
               WHERE    AddressTypeID = 2
                        AND IsDefault = 1
               GROUP BY ac.AccountID     

             ),
        --Billing
        --  AS ( SELECT   ac.AccountId ,
        --                MAX(ccp.BillingAddressID) AS AddressID
        --       FROM     RFOperations.RFO_Accounts.Addresses A
        --                JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.AddressID = A.AddressID
        --                JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountContactId = ACA.AccountContactId
        --       WHERE    AddressTypeID = 3
        --                AND IsDefault = 1 
        --       GROUP BY ac.AccountID 
        --     ),
			 
			 
			  Main
          AS ( SELECT   AccountId ,
                        A.AddressID ,
                        A.Locale ,
                        A.Region
               FROM     RFOperations.RFO_Accounts.Addresses A
                        JOIN RFOperations.RFO_Accounts.AccountContactAddresses ACA ON ACA.AddressID = A.AddressID
                        JOIN RFOperations.RFO_Accounts.AccountContacts AC ON AC.AccountContactId = ACA.AccountContactId
               WHERE    AddressTypeID = 1
                        AND IsDefault = 1
                        AND A.CountryID = @RFOCountry)
       ,
        EnrolledPC
          AS ( SELECT   AccountID ,
                        1 AS EnrolledASPC
               FROM     RFOperations.Hybris.Autoship
               WHERE    Active = 1
                        AND AutoshipTypeID = 1
             ),
        EnrolledCRP
          AS ( SELECT   AccountID ,
                        1 AS EnrolledASCRP
               FROM     RFOperations.Hybris.Autoship
               WHERE    Active = 1
                        AND AutoshipTypeID = 2
             ),
        EnrolledPulse
          AS ( SELECT   AccountID ,
                        1 AS EnrolledASPulse
               FROM     RFOperations.Hybris.Autoship
               WHERE    Active = 1
                        AND AutoshipTypeID = 3
             )
    SELECT DISTINCT
            CAST (AB.AccountID AS NVARCHAR(100)) AS AccountID				--p_rfaccountid
            ,
            CASE WHEN Birthday = 'Jan  1 1900 12:00AM'
                      OR Birthday = '' THEN NULL
                 ELSE CAST (AC.Birthday AS NVARCHAR(100))
            END AS Birthday					--,p_dateofbirth
            ,
            CAST (AR.HardTerminationDate AS NVARCHAR(100)) AS HardTerminationDate		--,p_hardterminationdate
            ,
            CAST (AR.IsTaxExempt AS NVARCHAR(100)) AS IsTaxExempt					--	,p_excemptfromtax
            ,
            CAST (LTRIM(RTRIM(EA.EmailAddress)) AS NVARCHAR(255)) AS EmailAddress ,
            AB.AccountNumber 				--,p_rfaccountnumber
            ,
            CAST (AR.SponsorId AS NVARCHAR(100)) AS SponsorID					--,p_sponsorid
            ,
            CAST (LTRIM(RTRIM(AR.CoApplicant)) AS NVARCHAR(100)) AS CoApplicant					--	,p_enrollspousename
            ,
            CAST(LTRIM(RTRIM(PH.PhoneNumberRaw)) AS NVARCHAR(100)) AS PhoneNumberRaw				--,p_mainphone
            ,
            CAST (AR.NextRenewalDate AS NVARCHAR(100)) AS NextRenewalDate			--	,p_expirationdate
            
            --,NULL AS LastRenewalDate --CAST (AR.LastRenewalDate AS NVARCHAR(100)) AS LastRenewalDate			--	,p_renewlatertime
            ,
            CASE WHEN AccountTypeID = 1
                 THEN CAST (EnrollmentDate AS NVARCHAR(100)) 	--	,p_consultantsince
                 ELSE NULL
            END AS ConsEnrollmentDate ,
            CASE WHEN AccountTypeID = 2
                 THEN CAST (EnrollmentDate AS NVARCHAR(100))	--	,p_consultantsince
                 ELSE NULL
            END AS PCEnrollmentDate ,
            CAST (0 AS NVARCHAR(100)) AS HasOrder ,
            CASE WHEN LTRIM(RTRIM(AR.CoApplicant)) IS NOT NULL						--p_enrollallowspouse
                      AND AR.CoApplicant <> '' THEN CAST (1 AS NVARCHAR(100))
                 ELSE CAST (0 AS NVARCHAR(100))
            END AS EnrollSpouseAllow ,
            CASE WHEN AR.Active = 0 THEN CAST (1 AS NVARCHAR(100))	                            --p_logindisabled
                 ELSE CAST (0 AS NVARCHAR(100))
            END AS p_logindisabled ,
            CASE WHEN AC.GenderId = 1 THEN CAST ('Female' AS NVARCHAR(100))						--p_Gender
                 WHEN AC.GenderId = 2 THEN CAST ('Male' AS NVARCHAR(100))
                 ELSE NULL
            END AS Gender ,
            CASE WHEN AB.CountryID = 236 THEN CAST('US' AS NVARCHAR(100))
                 WHEN AB.CountryID = 40 THEN CAST('CA' AS NVARCHAR(100))
                 ELSE NULL
            END AS Country ,
            CASE WHEN AB.CurrencyID = 38 THEN CAST('CAD' AS NVARCHAR(100))
                 WHEN AB.CurrencyID = 4 THEN CAST ('USD' AS NVARCHAR(100))
                 ELSE NULL
            END AS Currency ,
            CASE WHEN AST.Name = 'Begun Enrollment'
                 THEN CAST ('Pending' AS NVARCHAR(100))	     --p_AccountStatus
                 ELSE CAST (AST.Name AS NVARCHAR(100))
            END AS AccountStatus ,
            CASE WHEN LanguageId = 4 THEN CAST ('en' AS NVARCHAR(100))
                 ELSE NULL
            END AS PreferredLanguage  -- p_preferredlanguage
            ,
            CASE WHEN LanguageId = 4 THEN CAST ('en' AS NVARCHAR(100))
                 ELSE NULL
            END AS SessionLanguage    -- p_sessionlanguage
            ,
            CAST (LTRIM(RTRIM(EA.EmailAddress)) AS NVARCHAR(100)) AS UniqueID
			--,CAST (ASE.Password AS NVARCHAR(100)) AS Passwd -- P_Passwd
                        
            ,CAST (LTRIM(RTRIM(CONCAT(AC.FirstName,' ',
                         AC.LastName))) AS NVARCHAR(255))
						 AS Name,
            CASE WHEN LEN(M.Region) > 2 THEN CAST (R.Region AS NVARCHAR(100))
                 ELSE CAST (M.Region AS NVARCHAR(100))
            END AS ConsultantState ,
            CAST (M.Locale AS NVARCHAR(100)) AS ConsultantTown ,
            CAST (ISNULL(EPC.EnrolledASPC, 0) AS NVARCHAR(100)) AS EnrolledASPC ,
            CAST (ISNULL(EC.EnrolledASCRP, 0) AS NVARCHAR(100)) AS EnrolledASCRP ,
            CAST (ISNULL(EPU.EnrolledASPulse, 0) AS NVARCHAR(100)) AS EnrolledASPulse ,
            CASE WHEN AB.AccountTypeID = 1
                 THEN CAST ('Consultant' AS NVARCHAR(100))
                 WHEN AB.AccountTypeID = 2 THEN CAST ('PC' AS NVARCHAR(100))
                 WHEN AB.AccountTypeID = 3 THEN CAST ('RC' AS NVARCHAR(100))
                 ELSE NULL
            END AS CustomerGroup 
            ,CAST(LTRIM(RTRIM(ASE.Username)) AS NVARCHAR(255)) AS UserName
			,CAST(LTRIM(RTRIM(AC.FirstName)) AS NVARCHAR(255)) AS FirstName 
			,CAST(LTRIM(RTRIM(AC.LastName)) AS NVARCHAR(255)) AS LastName 
			,CAST(EnrollmentDate AS NVARCHAR(255)) AS Createdts  
			, CAST(s.AddressID AS NVARCHAR(255)) AS  DefaultShippingAddress
			, CAST(p.AddressID AS NVARCHAR(255)) AS  DefaultBillingAddress
		, CAST(p.PaymentProfileID AS NVARCHAR(255)) AS  DefaultPaymentInfo

    INTO    #RFO_Accounts
    FROM    RFOperations.RFO_Accounts.AccountBase (NOLOCK) AB --JOIN RFOperations.RFO_Reference.Countries (NOLOCK) C ON c.CountryID =ab.CountryID 
            JOIN RFOperations.RFO_Reference.AccountStatus (NOLOCK) AST ON AST.AccountStatusID = AB.AccountStatusID
            JOIN RFOperations.RFO_Accounts.AccountContacts (NOLOCK) AC ON AC.AccountId = AB.AccountID
            JOIN RFOperations.RFO_Accounts.AccountContactPhones (NOLOCK) ACPH ON ACPH.AccountContactId = AC.AccountContactId
            JOIN RFOperations.RFO_Accounts.AccountRF (NOLOCK) AR ON AB.AccountID = AR.AccountID
			JOIN RFOperations.RFO_Accounts.Phones (NOLOCK) PH ON PH.PhoneID = ACPH.PhoneId
                                                              AND PH.PhoneTypeID = 1
          JOIN RFOperations.RFO_Accounts.AccountEmails (NOLOCK) AE ON AE.AccountContactId = AC.AccountContactId
            JOIN RFOperations.RFO_Accounts.EmailAddresses (NOLOCK) EA ON EA.EmailAddressID = AE.EmailAddressId
                                                              AND EmailAddressTypeID = 1
         JOIN RFOperations.Security.AccountSecurity (NOLOCK) ASE ON ASE.AccountID = AB.AccountID
            LEFT JOIN Shipping S ON S.AccountId = AB.AccountID
            --LEFT JOIN Billing B ON B.AccountId = AB.AccountID
            LEFT JOIN PayInfo P ON P.AccountID = AB.AccountID
          LEFT JOIN Main M ON M.AccountId = AB.AccountID
            LEFT JOIN EnrolledCRP EC ON EC.AccountID = AB.AccountID
            LEFT JOIN EnrolledPC EPC ON EPC.AccountID = AB.AccountID
            LEFT JOIN EnrolledPulse EPU ON EPU.AccountID = AB.AccountID
            LEFT JOIN DataMigration.Migration.RegionMapping R ON R.Region = M.Region
    WHERE   AB.CountryID = @RFOCountry --AND AR.SoftTerminationDate> @Loaddate
            AND EXISTS ( SELECT 1
                         FROM   Hybris.dbo.users HU 
                         WHERE  HU.p_rfaccountid = CAST(AB.AccountID AS VARCHAR) AND p_sourcename = 'Hybris-DM' )


		
		 
UPDATE  a
SET     HasOrder = CAST ('1' AS NVARCHAR(100))
FROM    RFOperations.Hybris.Orders b
        JOIN RFOperations.ETL.OrderDate c ON b.OrderNumber = CAST(c.OrderID AS VARCHAR)
       RIGHT JOIN #RFO_Accounts a ON a.AccountID = b.AccountID
WHERE   b.AccountID IS NOT NULL
        AND Startdate >= @Loaddate;




SELECT  CAST (p_rfaccountid AS NVARCHAR(100)) AS p_rfAccountID ,
        CAST (HU.p_dateofbirth AS NVARCHAR(100)) AS p_dateofbirth ,
        CAST (p_hardterminationdate AS NVARCHAR(100)) AS p_hardterminationdate ,
        CAST (p_excemptfromtax AS NVARCHAR(100)) AS p_excemptfromtax ,
        CAST (LTRIM(RTRIM(p_customeremail)) AS NVARCHAR(255)) AS p_customeremail ,
        CAST (p_rfaccountnumber AS NVARCHAR(100)) AS p_rfaccountnumber ,
        CAST (p_sponsorid AS NVARCHAR(100)) AS p_sponsorid ,
        CAST (LTRIM(RTRIM(HU.p_enrollspousename)) AS NVARCHAR(100)) AS p_enrollspousename ,
        CAST (LTRIM(RTRIM(p_mainphone)) AS NVARCHAR(100)) AS p_mainphone ,
        CAST (p_expirationdate AS NVARCHAR(100)) AS p_expirationdate 
     --  , CAST (p_renewlatertime AS NVARCHAR(100)) AS p_renewlatertime 
        ,CAST (p_consultantsince AS NVARCHAR(100)) AS p_consultantsince ,
        CAST (HU.p_preferredcustomersince AS NVARCHAR(100)) AS p_preferredcustomersince ,
        CAST (p_hasorder AS NVARCHAR(100)) AS p_hasorder ,
        CAST (p_enrollallowspouse AS NVARCHAR(100)) AS p_enrollallowspouse ,
        CAST (p_logindisabled AS NVARCHAR(100)) p_logindisabled ,
        CASE WHEN HU.p_gender = 8796093874267
             THEN CAST ('Female' AS NVARCHAR(100))
             WHEN HU.p_gender = 8796093841499
             THEN CAST ('Male' AS NVARCHAR(100))
             ELSE NULL
        END AS p_gender ,
        CASE WHEN HU.p_country = 8796100624418
             THEN CAST ('US' AS NVARCHAR(100))
             WHEN HU.p_country = 8796094300194
             THEN CAST ('CA' AS NVARCHAR(100))
             ELSE NULL
        END AS p_country ,
        CASE WHEN HU.p_sessioncurrency = 8796125855777
             THEN CAST ('USD' AS NVARCHAR(100))
             WHEN HU.p_sessioncurrency = 8796125888545
             THEN CAST ('CAD' AS NVARCHAR(100))
             ELSE NULL
        END AS p_SessionCurrency ,
        CASE WHEN p_accountstatus = 8796135915611
             THEN CAST ('PENDING' AS NVARCHAR(100))
             WHEN p_accountstatus = 8796135948379
             THEN CAST ('ACTIVE' AS NVARCHAR(100))
             WHEN p_accountstatus = 8796135981147
             THEN CAST ('ONHOLD' AS NVARCHAR(100))
             WHEN p_accountstatus = 8796136013915
             THEN CAST ('SUSPENDED' AS NVARCHAR(100))
             WHEN p_accountstatus = 8796136046683
             THEN CAST ('HIATUS' AS NVARCHAR(100))
             WHEN p_accountstatus = 8796136079451
             THEN CAST ('SOFT TERMINATED VOLUNTARY' AS NVARCHAR(100))
             WHEN p_accountstatus = 8796136112219
             THEN CAST ('SOFT TERMINATED INVOLUNTARY' AS NVARCHAR(100))
             WHEN p_accountstatus = 8796136144987
             THEN CAST ('HARD TERMINATED' AS NVARCHAR(100))
        END AS p_AccountStatus ,
        CASE WHEN p_preferredlanguage = 8796093055008 THEN 'en'
             ELSE CAST (p_preferredlanguage AS NVARCHAR(100))
        END AS p_PreferredLanguage ,
        CASE WHEN p_sessionlanguage = 8796093055008 THEN 'en'
             ELSE CAST (p_sessionlanguage AS NVARCHAR(100))
        END AS p_SessionLanguage ,
        CAST (LTRIM(RTRIM(HU.uniqueid)) AS NVARCHAR(100)) AS UniqueID ,
		--CAST (Passwd AS NVARCHAR (100)) AS Passwd,
 
        CAST (LTRIM(RTRIM(HU.name)) AS NVARCHAR(100)) AS Name ,
        CAST (R.isocode AS NVARCHAR(100)) AS p_consultantstate ,
        CAST (HU.p_consultanttown AS NVARCHAR(100)) AS p_consultanttown ,
        CAST (HU.p_enrolledaspc AS NVARCHAR(100)) AS p_enrolledaspc ,
        CAST (HU.p_enrolledascrp AS NVARCHAR(100)) AS p_enrolledascrp ,
        CAST (p_enrolledaspulse AS NVARCHAR(100)) AS p_enrolledaspulse ,
        CASE WHEN TargetPK = 8796289662981 THEN CAST ('PC' AS NVARCHAR(100))
             WHEN TargetPK = 8796126150661
             THEN CAST ('Consultant' AS NVARCHAR(100))
             WHEN TargetPK = 8796126085125 THEN CAST ('RC' AS NVARCHAR(100))
             ELSE NULL
        END AS CustomerGroup
		 ,CAST(LTRIM(RTRIM(HU.p_customerusername)) AS NVARCHAR(255)) AS p_customerusername
			,CAST(LTRIM(RTRIM(hu.p_firstname)) AS NVARCHAR(255)) AS FirstName 
			,CAST(LTRIM(RTRIM(hu.p_lastname)) AS NVARCHAR(255)) AS LastName 
			,CAST(hu.createdTS AS NVARCHAR(255)) AS Createdts  
			, CAST (HAS.p_rfaddressid AS NVARCHAR(100)) AS p_DefaultShippingAddress ,
        CAST (HAB.p_rfaddressid AS NVARCHAR(100)) AS p_DefaultPaymentAddress ,
        CAST (HP.p_rfaccountpaymentmethodid AS NVARCHAR(100)) AS p_DefaultPaymentInfo 
INTO    #Hybris_Accounts
FROM    Hybris.dbo.users (NOLOCK) HU
        LEFT JOIN Hybris.dbo.pgrels (NOLOCK) PG ON HU.PK = PG.SourcePK
        LEFT JOIN Hybris.dbo.countries (NOLOCK) C ON C.PK = HU.p_country
        LEFT JOIN Hybris.dbo.paymentinfos (NOLOCK) HP ON HP.PK = HU.p_defaultpaymentinfo
        LEFT JOIN Hybris.dbo.addresses (NOLOCK) HAS ON HAS.PK = HU.defaultshippingaddress
        LEFT JOIN Hybris.dbo.addresses (NOLOCK) HAB ON HAB.PK = HU.defaultpaymentaddress
        LEFT JOIN Hybris.dbo.regions (NOLOCK) R ON R.PK = HU.p_consultantstate
WHERE   HU.p_sourcename = 'Hybris-DM'

      
      

CREATE CLUSTERED INDEX MIX_AccountID ON #RFO_Accounts (AccountID);
CREATE CLUSTERED INDEX MIX_rfAccountID ON #Hybris_Accounts (p_rfAccountID);



SELECT  *
INTO    #Accounts
FROM    #RFO_Accounts
EXCEPT
SELECT  *
FROM    #Hybris_Accounts;



CREATE CLUSTERED INDEX MIX_AccountID1 ON #Accounts (AccountID);


SELECT  'RFOAccounts' ,
        COUNT(DISTINCT AccountID)
FROM    #RFO_Accounts; 

SELECT  'Hybris_Accounts' ,
        COUNT(DISTINCT p_rfAccountID)
FROM    #Hybris_Accounts;  

SELECT  'Excepts' ,
        COUNT(DISTINCT AccountID)
FROM    #Accounts; 


/*

SELECT * FROM #Hybris_Accounts a 
WHERE NOT EXISTS (SELECT 1 FROM #RFO_Accounts ra WHERE CAST(ra.AccountId AS VARCHAR)= a.p_rfAccountID)

SELECT AccountID INTO #Accounts1 FROM #RFO_Accounts ra 
WHERE NOT EXISTS (SELECT 1 FROM #Hybris_Accounts a WHERE CAST(ra.AccountId AS VARCHAR)= a.p_rfAccountID)


*/ 


*/


/*
	
/*=================================================================================================
-- Part 3 Column to Column Comparisons 
================================================================================================= */


 --TRUNCATE TABLE DataMigration.Migration.Metadata_Accounts 

DECLARE @I INT = (SELECT MIN(MapID) FROM  DataMigration.Migration.Metadata_Accounts WHERE HybrisObject = 'Users') , 
@C INT =  (SELECT MAX(MapID) FROM  DataMigration.Migration.Metadata_Accounts WHERE HybrisObject = 'Users') 


DECLARE @DesKey NVARCHAR (50) 

DECLARE @SrcKey NVARCHAR (50) 

DECLARE @Skip  BIT 

WHILE (@I <=@c)

BEGIN 

        SELECT  @Skip = ( SELECT   Skip
                               FROM     DataMigration.Migration.Metadata_Accounts
                               WHERE    MapID = @I
                             );


        IF ( @Skip = 1 )

            SET @I = @I + 1;

        ELSE
BEGIN 



DECLARE @SrcCol NVARCHAR (50) =(SELECT RFO_Column FROM DataMigration.Migration.Metadata_Accounts WHERE MapID = @I)

DECLARE @DesTemp NVARCHAR (50) =(SELECT CASE WHEN HybrisObject = 'Users' THEN '#Hybris_Accounts'
										     WHEN HybrisObject = 'Addresses' THEN '#Hybris_Addresses'
										     WHEN HybrisObject = 'PaymentInfos' THEN '#Hybris_PayInfo'
											 END
			FROM  DataMigration.Migration.Metadata_Accounts 
			  WHERE MapID =@I
								) 

DECLARE @DesCol NVARCHAR (50) =(SELECT Hybris_Column FROM DataMigration.Migration.Metadata_Accounts WHERE MapID = @I)

SET @SrcKey= (SELECT RFO_Key
			  FROM DataMigration.Migration.Metadata_Accounts 
			  WHERE MapID =@I
								)

                SET @DesKey = ( SELECT  CASE WHEN HybrisObject= 'Users'
                                             THEN 'p_rfAccountID'
                                             WHEN HybrisObject = 'Addresses'
                                             THEN 'p_rfAddressID'
                                             WHEN HybrisObject = 'PaymentInfos'
                                             THEN 'p_rfaccountPaymentMethodID'
                                        END
                                FROM    DataMigration.Migration.Metadata_Accounts
                                WHERE   MapID = @I
                              ); 


DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM  DataMigration.Migration.Metadata_Accounts WHERE MapID = @I)
DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.Hybris_Value = b. ' + @DesCol +
' FROM DataMigration.Migration.ErrorLog_Accounts a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.MAPID = ' + CAST(@I AS NVARCHAR)



DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO DataMigration.Migration.ErrorLog_Accounts (Identifier,MapID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

  BEGIN TRY
   EXEC sp_executesql @SQL3, N'@ServerMod DATETIME', @ServerMod= '05/01/1900'

 SET @I = @I + 1

 END TRY

 BEGIN CATCH

 SELECT @SQL3

 SET @I = @I + 1

 END CATCH
END 

END 


SELECT b.MapID,  b.RFO_column, COUNT(*) AS Counts
FROM DataMigration.Migration.ErrorLog_Accounts A JOIN DataMigration.Migration.Metadata_Accounts B ON a.MapID =b.MapID
GROUP BY b.MapID, RFO_Column


*/ 


 /********************************************************************************************************************
-- FOR TROUBLESHOOTING INDIVIDUAL Accounts 

SELECT MapID, RecordID, '['+ RFO_Value + ']', '['+ Hybris_Value + ']' FROM DataMigration.Migration.ErrorLog_Accounts
WHERE MapID =6

-- SponsorID 

 
SELECT SponsorID, * FROM RFoperations.RFO_Accounts.AccountRF
WHERE AccountID = 1240439

SELECT p_sponsorid, p_sourcename,CAST (P_hardterminationdate AS NVARCHAR(255)), *FROM Hybris.dbo.Users 
WHERE p_rfAccountID ='1240439'

--- HardTermination 

SELECT * FROM RFOperations.Audit.RFOAccountsAccountRF 
WHERE AccountID =1494406
ORDER BY AuditDate DESC 
 
SELECT CAST (HardTerminationDate AS NVARCHAR(255)), * FROM RFoperations.RFO_Accounts.AccountRF
WHERE AccountID = 1494406

SELECT P_hardterminationdate, p_sourcename,CAST (P_hardterminationdate AS NVARCHAR(255)), *FROM Hybris.dbo.Users 
WHERE p_rfAccountID ='1494406'

-- EmailAddress 
SELECT CAST (EmailAddress AS NVARCHAR(255)), * FROM RFoperations.RFO_Accounts.AccountContacts a JOIN RFOPerations.RFO_Accounts.AccountEmails b ON a.AccountContactID = b.AccountContactID 
JOIN RFOperations.RFO_Accounts.EmailAddresses c ON c.EmailAddressID =b.EmailAddressID
WHERE AccountID = 1122067 AND EmailAddressTypeID =  1

SELECT p_customeremail, p_sourcename,CAST (p_customeremail AS NVARCHAR(255)), *FROM Hybris.dbo.Users 
WHERE p_rfAccountID ='1122067'

--PhoneNumber
SELECT CAST (PhoneNumberRaw AS NVARCHAR(255)), * FROM RFoperations.RFO_Accounts.AccountContacts a JOIN RFOPerations.RFO_Accounts.AccountContactPhones b ON a.AccountContactID = b.AccountContactID 
JOIN RFOperations.RFO_Accounts.Phones c ON c.PhoneID =b.PhoneID
WHERE AccountID = 648462 

SELECT p_mainphone, p_sourcename,CAST (p_customeremail AS NVARCHAR(255)), *FROM Hybris.dbo.Users 
WHERE p_rfAccountID ='648462'

--EnrollmentDate

SELECT CAST (EnrollmentDate AS NVARCHAR(255)), * FROM RFoperations.RFO_Accounts.AccountRF
WHERE AccountID = 965195

SELECT createdts, p_sourcename,CAST (createdts AS NVARCHAR(255)), *FROM Hybris.dbo.Users 
WHERE p_rfAccountID ='965195'

--Has Order 

SELECT CompletionDate, * FROM RFOperations.Hybris.Orders 
WHERE AccountID = 2695935

SELECT p_hasorder FROM Hybris.dbo.Users 
WHERE p_rfAccountID = '2695935'


SELECT HasOrder,* FROM #RFO_Accounts
WHERE AccountID = 2695935

-- AccountStatusID 

SELECT AccountStatusID, * FROM RFoperations.RFO_Accounts.AccountBase
WHERE AccountID = 1780506

SELECT p_accountstatus, ev.code, p_sourcename,CAST (P_hardterminationdate AS NVARCHAR(255)), *FROM Hybris.dbo.Users a JOIN Hybris.dbo.enumerationvalues ev ON a.p_accountstatus = ev.PK
WHERE p_rfAccountID ='1780506'


--Default Billing Address 

SELECT pk, defaultpaymentaddress,* FROM Hybris.dbo.Users
WHERE p_rfAccountID = '1030572'

SELECT * FROM RFOperations.RFO_accounts.AccountContacts a JOIN RFOperations.RFO_accounts.AccountContactAddresses aca ON a.AccountContactID = aca.AccountcontactID 
JOIn RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID =aca.AddressID 
WHERE AccountID =1030572

SELECT * FROM Hybris.dbo.PaymentInfos
WHERE OwnerpkString = 8814270939140

SELECT * FROM Hybris.dbo.Addresses 
WHERE p_rfAddressID = '2966206'

SELECT * FROM DataMigration.dbo.Addresses_billing
WHERE [rfAddressId[unique=true]]] = '1118247'


SELECT * FROM RFOperations.RFO_Accounts.Creditcardprofiles 
WHERE BillingAddressID IN (2966206) 
**************************************************************************************************************************/


