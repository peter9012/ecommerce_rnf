SELECT DISTINCT
        A.AccountNumber ,
        A.AccountID ,
        A.Active ,
        at.Name AS [Account Types] ,
        ad.AccountAddressID ,
        ad.AddressTypeID ,
        t.Name AS [Types] ,
        ad.IsDefault ,
        ad.City ,
        ad.[State] ,
        ad.PostalCode ,
        ad.Address1 ,
        ad.PhoneNumber INTO   #PostalCode
FROM    RodanFieldsLive.dbo.Accounts A
        JOIN RodanFieldsLive.dbo.AccountAddresses ad ON A.AccountID = ad.AccountID
        JOIN RodanFieldsLive.dbo.AddressType t ON t.AddressTypeID = ad.AddressTypeID
        JOIN RodanFieldsLive.dbo.AccountType at ON at.AccountTypeID = A.AccountTypeID
WHERE   1 = 1
        AND ( ( ISNUMERIC(ad.PostalCode) = 1
                AND LEN(LTRIM(RTRIM(ad.PostalCode))) <> 5
              )
              OR ( ISNUMERIC(ad.PostalCode) = 0
                   AND LEN(LTRIM(RTRIM(ad.PostalCode))) <> 10
                 )
              OR ad.PostalCode LIKE '%[A-Z]%'
              OR ad.PostalCode LIKE '%[!@#$%^&*()_+=.,/;:]%'
              OR ISNULL(ad.PostalCode, '') = ''
              OR ad.PostalCode = 'null'
            )
        AND A.AccountID <> 2
        AND A.Active = 1
		AND ad.IsDefault=1
        AND  ad.AddressTypeID = 1
             ;

			 --DROP TABLE #PostalCode

			  --SELECT *  FROM #PostalCode
--######################
-- FROM ACCOUNTADDRESSES
--######################

            SELECT DISTINCT
                    ad.AccountID ,
                    p.Active ,
                    p.IsDefault ,
                    p.AccountID ,
                    ad.AccountAddressID ,
                    p.AccountAddressID ,
                    ad.City ,
                    p.City ,
                    ad.State ,
                    p.State ,
                    ad.PostalCode ,
                    p.PostalCode ,
                    ad.Address1 ,
                    p.Address1
            FROM    RodanFieldsLive.dbo.AccountAddresses ad
                    JOIN #PostalCode p ON p.AccountID = ad.AccountID
                                          AND ad.City = p.City
                                          AND ad.State = p.State
                                          AND ad.Address1 = p.Address1
            WHERE   ISNULL(ad.City, 'null') <> 'null'
                    AND ISNULL(ad.State, 'null') <> 'null'
                    AND ad.PostalCode <> p.PostalCode
                    AND ( ( ISNUMERIC(ad.PostalCode) = 1
                            AND LEN(LTRIM(RTRIM(ad.PostalCode))) = 5
                          )
                          OR ( (ISNUMERIC(ad.PostalCode) = 0
                               AND LEN(LTRIM(RTRIM(ad.PostalCode))) = 10
                               AND ( ad.PostalCode NOT LIKE '%[A-Z]%'
                                     OR ad.PostalCode NOT  LIKE '%[!@#$%^&*()_+=.,/;:]%'
                                     OR ISNULL(ad.PostalCode, '') <> ''
                                     OR ad.PostalCode <> 'null'
                                   ))
                             )
                        );

--######################
-- FROM ORDERPAYMENTS
--######################




            SELECT DISTINCT
                    p.Active ,
                    p.IsDefault ,
                    p.AccountID ,
                    OP.OrderPaymentID ,
                    p.AccountAddressID ,
                    OP.BillingCity ,
                    p.City ,
                    OP.BillingState ,
                    p.State ,
                    OP.BillingPostalCode ,
                    p.PostalCode ,
                    OP.BillingStreetAddress ,
                    p.Address1
            FROM    RodanFieldsLive.dbo.OrderPayments OP
                    JOIN RodanFieldsLive.dbo.OrderCustomers OC ON OC.OrderCustomerID = OP.OrderCustomerID
                    JOIN #PostalCode p ON p.AccountID = OC.AccountID
                                          AND p.City = OP.BillingCity
                                          AND p.State = OP.BillingState
                                          --AND p.Address1 = OP.BillingStreetAddress
            WHERE   ISNULL(OP.BillingCity, 'null') <> 'null'
                    AND ISNULL(OP.BillingState, 'null') <> 'null'
                    AND p.PostalCode <> OP.BillingPostalCode
                    AND ( ( ISNUMERIC(OP.BillingPostalCode) = 1
                            AND LEN(LTRIM(RTRIM(OP.BillingPostalCode))) = 5
                          )
                          OR ( (ISNUMERIC(OP.BillingPostalCode) = 0
                               AND LEN(LTRIM(RTRIM(OP.BillingPostalCode))) = 10
                               AND ( OP.BillingPostalCode NOT LIKE '%[A-Z]%'
                                     OR OP.BillingPostalCode NOT  LIKE '%[!@#$%^&*()_+=.,/;:]%'
                                     OR ISNULL(OP.BillingPostalCode, '') <> ''
                                     OR OP.BillingPostalCode <> 'null'
                                   ))
                             )
                        );
			
	


--######################
-- FROM ORDERSHIPMENTS
--######################




            SELECT DISTINCT
                    p.Active ,
                    p.IsDefault ,
                    p.AccountID ,
                    OS.OrderShipmentID,
                    p.AccountAddressID ,
                    OS.City ,
                    p.City ,
                   OS.State,
                    p.State ,
                    OS.PostCode,
                    p.PostalCode ,
                    OS.Address1 ,
                    p.Address1
            FROM    RodanFieldsLive.dbo.OrderShipments OS
                    JOIN RodanFieldsLive.dbo.OrderCustomers OC ON OC.OrderCustomerID = OS.OrderCustomerID
                    JOIN #PostalCode p ON p.AccountID = OC.AccountID
                                          AND p.City = OS.City
                                          AND p.State = OS.State
                                           AND p.Address1 = OS.Address1
            WHERE   ISNULL(OS.City, 'null') <> 'null'
                    AND ISNULL(OS.State, 'null') <> 'null'
                    AND p.PostalCode <> OS.PostCode
                    AND ( ( ISNUMERIC(OS.PostCode) = 1
                            AND LEN(LTRIM(RTRIM(OS.PostCode))) = 5
                          )
                          OR ( (ISNUMERIC(OS.PostCode) = 0
                               AND LEN(LTRIM(RTRIM(OS.PostCode))) = 10
                               AND ( OS.PostCode NOT LIKE '%[A-Z]%'
                                     OR OS.PostCode NOT  LIKE '%[!@#$%^&*()_+=.,/;:]%'
                                     OR ISNULL(OS.PostCode, '') <> ''
                                     OR OS.PostCode <> 'null'
                                   ))
                             )
                        );
			


--#############################################################################################
-- FINAL EXCEPTION LIST OF ACCOUNTADDRESS FOR ACTIVE ACCOUNTS WITH MAIN OR DEFAULT ADDRESSES.
--#############################################################################################


IF OBJECT_ID(N'Tempdb..#ADDRESSES') IS NOT NULL
DROP TABLE #ADDRESSES

--- FROM RFL DB.
SELECT DISTINCT
		A.AccountId,A.Active,at.Name AS [Account Types], ad.AccountAddressId,ad.AddressTypeID,t.Name AS [Types],ad.IsDefault,
		ad.City, ad.[State], ad.PostalCode, ad.Address1, ad.PhoneNumber INTO #ADDRESSES
		
	FROM 
		RodanFieldsLive.dbo.Accounts A JOIN
		RodanFieldsLive.dbo.AccountAddresses ad ON A.AccountId = ad.AccountId
		JOIN RodanFieldsLive.dbo.AddressType t ON t.AddressTypeID=ad.AddressTypeID	
		JOIN RodanFieldsLive.dbo.AccountType at ON at.AccountTypeID = A.AccountTypeID
	WHERE 
		1=1
		AND 
		(
			ISNULL(ad.State,'') = '' 
			OR ISNULL (ad.PostalCode,'') = '' 
			OR ISNULL(ad.Address1, '')= ''
			OR ISNULL(ad.city, '')=''
			OR ad.City ='null'		--City Found like 'NULL' Strings.
		) 	
		AND A.Accountid <> 2
		AND A.Active=1		
		AND (ad.AddressTypeID=1 OR ad.IsDefault=1)
		

        SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #ADDRESSES;
  --1452 RECORDS IN STG2. -- 345 IN TST4



        SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #ADDRESSES
        WHERE   AddressTypeID = 1
                AND IsDefault = 1; --1360 RECORDS IN STG2. 164 IN TST4



    SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #ADDRESSES
        WHERE  City='NULL' --22 RECORDS IN STG2. 17 IN TST4



		IF OBJECT_ID(N'Tempdb..#RFOADDRESSES') IS NOT NULL
DROP TABLE #RFOADDRESSES


		--- FROM RFO SIDE.
        SELECT  ab.AccountID ,
                rf.Active ,
                ad.AddressTypeID ,
                t.Name AS [Address Types] ,
                ad.IsDefault ,
                ad.Locale ,
                ad.Region ,
                ad.PostalCode ,
                ad.AddressLine1 INTO #RFOADDRESSES
        FROM    RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
                JOIN RFOperations.RFO_Accounts.AccountContactAddresses acd ON acd.AccountContactId = ac.AccountContactId
                JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = acd.AddressID
                JOIN RFOperations.RFO_Reference.AddressType t ON t.AddressTypeID = ad.AddressTypeID
        WHERE   ab.AccountID <> 2
                AND rf.Active = 1
                AND ( ad.AddressTypeID = 1
                      OR ad.IsDefault = 1
                    )
                AND ( ISNULL(ad.AddressLine1, '') = ''
                      OR ISNULL(ad.Locale, '') = ''
                      OR ISNULL(ad.Region, '') = ''
                      OR ISNULL(ad.PostalCode, '') = ''
					  OR ad.Locale='null'					 
                    );



					 SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #RFOADDRESSES;
  --1352 RECORDS IN STG2.  --236 IN TST4 NOW



        SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #RFOADDRESSES
        WHERE   AddressTypeID = 1
                AND IsDefault = 1; --1292 RECORDS IN STG2. 122 IN TST4 NOW.



    SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #RFOADDRESSES
        WHERE  Locale='NULL' --25 RECORDS IN STG2. -19 IN TST4 NOW.

		






		

-- COMPLETELY JUNK DATA.

				
 SELECT a.*
 FROM   #ADDRESSES a
 WHERE  a.AccountID <> 2
        AND a.Active = 1
        AND ( ( ISNULL(a.PostalCode, '') = ''
                AND ISNULL(a.State, '') = ''
              )
              OR ( ISNULL(a.Address1, '') = ''
                   AND ISNULL(a.PostalCode, '') = ''
                 )
              OR ( ISNULL(a.Address1, '') = ''
                   AND ISNULL(a.PostalCode, '') = ''
                 )
            )
        AND ( a.AddressTypeID = 1
              OR a.IsDefault = 1
            );

--- MIGHT GET SOME FIELDS

SELECT  *
FROM    #ADDRESSES
WHERE   AccountID <> 2
        AND Active = 1
        AND ( AddressTypeID = 1
              OR IsDefault = 1
            )
        AND (ISNULL(City, '')= '' OR ISNULL([State],'')='')
        AND ISNULL(PostalCode, '') <> '';

		-- 1329 TOTAL RECORDS IN STG2. 213 RECORDS IN TST4.


--####################################################
--	 CHECKING MISSNG RECOREDS FROM RFL TO RFO 
--####################################################

		--Vlidation IF RFO still not having the Value updated in RFL

        WITH    MissingRFO
                  AS ( SELECT  
DISTINCT                        cust.AccountID ,
                                ab.AccountNumber ,
                                ad.*
                       FROM     RFOperations.RFO_Accounts.AccountRF cust ( NOLOCK )
                                INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ( NOLOCK ) ON cust.AccountID = ab.AccountID
                                INNER JOIN RodanFieldsLive.dbo.AccountAddresses addr ( NOLOCK ) ON addr.AccountID = cust.AccountID
                                INNER JOIN RFOperations.RFO_Accounts.Addresses ad ( NOLOCK ) ON --acd.AddressID = ad.AddressID
		 addr.AccountAddressID = ad.AddressID
                       WHERE    ( ISNULL(ad.Region, '') = ''
                                  OR ISNULL(ad.Locale, '') = ''
                                  OR ISNULL(ad.AddressLine1, '') = ''
                                  OR ISNULL(ad.AddressLine1, '') = ' '
                                  OR ISNULL(ad.PostalCode, '') = ''
                                  OR ISNULL(ad.Locale, '') = 'null'
                                )
                                AND ad.CountryID = 236
                                AND cust.AccountID <> 2
                                AND Active = 1
                                AND ( ad.AddressTypeID = 1
                                      OR ad.IsDefault = 1
                                    )
                     ),
                MissingRFL
                  AS ( SELECT DISTINCT
                                A.AccountID ,
                                ad.AccountAddressID ,
                                t.Name AS [Types] ,
                                ad.IsDefault ,
                                ad.City ,
                                ad.[State] ,
                                ad.PostalCode ,
                                ad.Address1 ,
                                ad.PhoneNumber
                       FROM     RodanFieldsLive.dbo.Accounts A
                                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON A.AccountID = ad.AccountID
                                JOIN RodanFieldsLive.dbo.AddressType t ON t.AddressTypeID = ad.AddressTypeID
                       WHERE    1 = 1
                                AND ( ISNULL(ad.State, '') = ''
                                      OR ISNULL(ad.PostalCode, '') = ''
                                      OR ISNULL(ad.Address1, '') = ''
                                      OR ISNULL(ad.City, '') = ''
                                      OR ISNULL(ad.City, 'null') = 'null'		--City Found like 'NULL' Strings.
                                    )
                                AND A.AccountID <> 2
                                AND A.Active = 1
                                AND ( ad.AddressTypeID = 1
                                      OR ad.IsDefault = 1
                                    )
                     )
            -- We are Only considering MainAddress Types and Default.
		

		SELECT  *
       -- INTO    #t
        FROM    MissingRFO r
        WHERE   NOT EXISTS ( SELECT 1
                             FROM   MissingRFL l
                             WHERE  r.AddressID = l.AccountAddressID );



        SELECT  COUNT(*) [RFL Counts]
        FROM    RodanFieldsLive.dbo.AccountAddresses
        WHERE   AccountAddressID IN ( SELECT    AddressID
                                      FROM      #t );
        SELECT  COUNT(*) [RFO Counts]
        FROM    RFOperations.RFO_Accounts.Addresses
        WHERE   AddressID IN ( SELECT   AddressID
                               FROM     #t );





	--====================================================================
			---MISSING  VALIDATION USING ADDRESS1 OR BILLINGSTREETADDRESS 
	--====================================================================


	IF OBJECT_ID(N'Tempdb..#MissingFields') IS NOT NULL
	DROP TABLE #MissingFields

		
	-- Loading into Temp Table 
	 SELECT   ac.AccountID ,
                                ad.AccountAddressID ,
                                ad.City ,
                                ad.[State] ,
                                ad.PostalCode ,
                                ad.Address1 ,
                                ad.PhoneNumber INTO #MissingFields
                       FROM     RodanFieldsLive.dbo.Accounts ac
                                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ac.AccountID = ad.AccountID
                       WHERE    1 = 1
                                AND ( ISNULL(ad.[State], '') = ''
                                      OR ISNULL(ad.PostalCode, '') = ''
                                      OR ISNULL(ad.Address1, '') = ''
                                      OR ISNULL(ad.City, '') = ''
                                      OR ad.City = 'null'
                                    )
                                AND ac.AccountID <> 2
                                AND ac.Active = 1								   
                    AND ( ad.AddressTypeID = 1
                          OR ad.IsDefault = 1
                        );
                   
				   CREATE CLUSTERED INDEX CLS ON #MissingFields(AccountAddressID)


            		
SELECT  DISTINCT a.AccountID ,
        ad.AccountAddressID ,
		t.AccountAddressID,
		p.OrderPaymentID,
        ad.Address1 ,
        t.Address1 [BackUp Address1] ,
		p.BillingStreetAddress,
        t.City [BacuUP City] ,
        ad.City [City],
		p.BillingCity,
        t.[State] AS [BackUp State] ,
        ad.[State] ,
		p.BillingState,
        t.PostalCode AS [BackUp PostalCode] ,
        ad.PostalCode,
		p.BillingPostalCode,
                    CASE WHEN ISNULL(ad.Address1, '') <> ''
                         THEN 'Account Address1'
                         WHEN ISNULL(p.BillingStreetAddress, '') <> ''
                         THEN 'BillingStreetAddress'
                    END AS [SOURCE]

					--**************/
            FROM    RodanFieldsLive.dbo.Accounts a
                    JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
                    JOIN #MissingFields t ON t.AccountID = ad.AccountID
                                            AND ad.Address1 = t.Address1
                    JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = t.AccountID
                    JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                              AND p.BillingStreetAddress = t.Address1
            WHERE   1 = 1
                    AND a.AccountID <> 2
                    AND COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    AND ISNULL(t.Address1, '') <> ''
                    AND ( ( COALESCE(ad.[State], p.BillingState, '') <> ''
                            AND ISNULL(t.[State], '') = ''
                          )
                          OR ( COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                               AND ISNULL(t.PostalCode, '') = ''
                             )
                          OR ( COALESCE(ad.City, p.BillingCity, '') <> ''
                               AND ISNULL(t.City, '') = ''
                             )
                          OR ( COALESCE(ad.City, p.BillingCity, 'null') <> 'null'
                               AND t.City = 'null'
                             )
                        ) 
                  
					
			
			--8473 RECORDS IN STG2.

			  SELECT  *
                FROM    RodanFieldsLive.dbo.AccountAddresses
                WHERE   AccountAddressID = 10199020;
	
                SELECT  *
                FROM    RodanFieldsLive.dbo.AccountAddresses
                WHERE   AccountAddressID = 10199021;
              
                SELECT  *
                FROM    RodanFieldsLive.dbo.OrderPayments
                WHERE   OrderPaymentID = 19103525;


			--===========================================================
			---MISSING  VALIDATION USING PostalCode OR BILLINGPostalCode 
			--=============================================================

		
		
	
		
SELECT   DISTINCT a.AccountID ,
        ad.AccountAddressID ,
        ad.Address1 ,
        t.Address1 [BackUp Address1] ,
		p.BillingStreetAddress,
        t.City [BacuUP City] ,
        ad.City [City],
		p.BillingCity,
        t.[State] AS [BackUp State] ,
        ad.[State] ,
		p.BillingState,
        t.PostalCode AS [BackUp PostalCode] ,
        ad.PostalCode,
		p.BillingPostalCode,
        CASE WHEN ISNULL(ad.PostalCode, '') <> '' THEN 'Account  PostalCode'
             WHEN ISNULL(p.BillingPostalCode, '') <> ''
             THEN ' BillingPostalCode'
        END AS [SOURCE]
		--*******/
FROM    RodanFieldsLive.dbo.Accounts a
        JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
        JOIN #MissingFields t ON t.AccountID = ad.AccountID
                                 AND LEFT(ad.PostalCode,5) =LEFT(t.PostalCode ,5)
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = t.AccountID
        JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                    AND LEFT(p.BillingCity,5) =LEFT(t.PostalCode ,5)
WHERE   1 = 1
        AND a.AccountID <> 2
        AND COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
        AND ( ( COALESCE(ad.[State], p.BillingState, '') <> ''
                AND COALESCE(t.[State], '') = ''
              )
              OR ( COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                   AND ISNULL(t.Address1, '') = ''
                 )
              OR ( COALESCE(ad.City, p.BillingCity, '') <> ''
                   AND ISNULL(t.City, '') = ''
                 )
              OR ( COALESCE(ad.City, p.BillingCity, 'null') <> 'null'
                   AND t.City = 'null'
                 )
             
            );

			  
					
			
			--8394 RECORDS IN STG2.


			
			--==========================================================================
			---MISSING  VALIDATION USING CitywithPostalCode OR BILLING CityWithPostalCode 
			--===========================================================================

		
SELECT DISTINCT a.AccountID ,
        ad.AccountAddressID ,
        ad.Address1 ,
        t.Address1 [BackUp Address1] ,
		p.BillingStreetAddress,
        t.City [BacuUP City] ,
        ad.City [City],
		p.BillingCity,
        t.[State] AS [BackUp State] ,
        ad.[State] ,
		p.BillingState,
        t.PostalCode AS [BackUp PostalCode] ,
        ad.PostalCode,
		p.BillingPostalCode,
		CASE WHEN ISNULL(ad.PostalCode,'')<>'' THEN 'Account  PostalCode'
		WHEN ISNULL(p.BillingPostalCode,'')<>''THEN ' BillingPostalCode'
		END AS [SOURCE]
		 --******/
FROM    RodanFieldsLive.dbo.Accounts a
        JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
        JOIN #MissingFields t ON t.AccountID = ad.AccountID
                                 AND ad.City = t.City
                                 AND LEFT(ad.PostalCode,5) =LEFT(t.PostalCode ,5)
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = t.AccountID
        JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                    AND p.BillingCity = t.City
                                                    AND LEFT(p.BillingCity,5) =LEFT(t.PostalCode ,5)
WHERE   1 = 1
        AND a.AccountID <> 2
        AND COALESCE(ad.City, p.BillingCity, '') <> ''
        AND COALESCE(ad.City, p.BillingCity, '') <> 'null'
        AND COALESCE(p.BillingCity, ad.City, '') <> 'null'
        AND ( ( COALESCE(ad.[State], p.BillingState, '') <> ''
                AND COALESCE(t.[State], '') = ''
              )
              OR ( COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                   AND ISNULL(t.Address1, '') = ''
                 )
              OR ( COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                   AND ISNULL(t.PostalCode, '') = ''
                 )
            );
			-- 16 RECORDS FROM STG2.

			
			--==================================================================
			--	 VALIDATION OF RFL TO RFO ADDRESSES BEEN FLOWN.
			--=================================================================

            SELECT  ad.AccountAddressID ,
                    a.AddressID ,
                    a.AddressLine1 ,
                    ad.Address1 ,
                    a.Locale ,
                    ad.City ,
                    a.Region ,
                    ad.State ,
                    a.PostalCode ,
                    ad.PostalCode
            FROM    RodanFieldsLive.dbo.AccountAddresses ad
                    JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = ad.AccountID
                    JOIN RFOperations.RFO_Accounts.Addresses a ON a.AddressID = ad.AccountAddressID
                                                              --AND a.AddressTypeID = ad.AddressTypeID
            WHERE   ( ( ISNULL(a.AddressLine1, '') = ''
                        AND ISNULL(ad.Address1, '') <> ''
                      )
                      OR ( ISNULL(a.Locale, '') = ''
                           AND ISNULL(ad.City, '') <> ''
                         )
						 OR (  a.Locale  = 'null'
                           AND  ad.City  <> 'null'
                         )
                      OR ( ISNULL(a.Region, '') = ''
                           AND ISNULL(ad.State, '') <> ''
                         )
                      OR ( ISNULL(a.PostalCode, '') = ''
                           AND ISNULL(ad.PostalCode, '') <> ''
                         )
                    );
			

            SELECT  COUNT(*) [Total Counts]
            FROM    RFOperations.RFO_Accounts.Addresses
            WHERE   ChangedByUser = 'MAIN-4486';


		-- 14734 in STG2 Now. 21,118 in TST4 

--++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++






-- EXCEPTION LIST FROM RFOPERATIONS 

SELECT  a.AccountID ,
        a.AutoshipID ,
		asd.AutoshipShippingAddressID,
        a.Active [Template Active] ,
        t.Name [Template Types] ,
        asd.Address1 ,
        asd.Locale ,
        asd.Region ,
        asd.PostalCode
FROM    RFOperations.Hybris.Autoship a
        JOIN RFOperations.RFO_Reference.AutoShipType t ON t.AutoShipTypeID = a.AutoshipTypeID
        JOIN RFOperations.Hybris.AutoshipShippingAddress asd ON asd.AutoShipID = a.AutoshipID
WHERE   a.Active = 1
        AND ( ISNULL(asd.Address1, '') = ''
              OR ISNULL(asd.Locale, '') = ''
              OR ISNULL(asd.Region, '') = ''
			  OR asd.Region='null'
              OR ISNULL(asd.PostalCode, '') = ''
            );

			-- 389 RECORDS IN STG2.  205 in TST4.



---FINAL LIST OF ADDRESS ISSUES FOR AUTOSHIP TEMPLATES.

SELECT  A.AccountId ,
        OS.OrderShipmentId ,
		o.OrderID AS [Templates],
        O.OrderTypeId ,
        OS.City ,
        OS.State ,
        OS.PostCode ,
        OS.Address1 ,
        OS.DayPhone  
FROM    RodanFieldsLive.dbo.OrderShipments OS
        JOIN RodanFieldsLive.dbo.Orders O ON OS.OrderId = O.OrderId
        JOIN RodanFieldsLive.dbo.OrderCustomers OC ON O.OrderId = OC.OrderId
        JOIN RodanFieldsLive.dbo.Accounts A ON OC.AccountId = A.AccountId
WHERE   1 = 1
        AND O.OrderStatusID = 7
        AND O.OrderTypeId IN ( 4, 5 )
        AND ( ISNULL(OS.State, '') = ''
              OR ISNULL(OS.PostCode, '') = ''
              OR ISNULL(OS.Address1, '') = ''
              OR ISNULL(OS.city, '') = ''
			  OR OS.City='null'
            )
        AND A.Active = 1
        AND A.Accountid <> 2;

		-- 462 TEMPLATES IN STG2. -- 274 in TST4 


---FINAL LIST OF ORDERS AND RETURNORDERS IN RFL.
		
SELECT  A.AccountId ,
        OS.OrderShipmentId ,
        O.OrderTypeId ,
		t.Name AS [OrderType],
        OS.City ,
        OS.State ,
        OS.PostCode ,
        OS.Address1 ,
        OS.DayPhone --INTO RFOperations.Hybris.Orders_AddressIssues
FROM    RodanFieldsLive.dbo.OrderShipments OS
        JOIN RodanFieldsLive.dbo.Orders O ON OS.OrderId = O.OrderId
		JOIN RodanFieldsLive.dbo.OrderTypes t ON t.OrderTypeID = O.OrderTypeID
        JOIN RodanFieldsLive.dbo.OrderCustomers OC ON O.OrderId = OC.OrderId
        JOIN RodanFieldsLive.dbo.Accounts A ON OC.AccountId = A.AccountId
WHERE   1=1
		AND O.CompleteDate >= '2016-01-01'
		AND O.OrderStatusID IN ( 4, 5 )
		AND O.OrderTypeId NOT  IN ( 4, 5, 9, 11)
		AND 
		(
			ISNULL(OS.State,'') = '' 
			OR ISNULL (OS.PostCode,'') = '' 
			OR ISNULL(OS.Address1, '')= ''
			OR ISNULL (OS.city, '') = ''
			OR OS.City='null'
		) 	
		AND A.Active = 1
		AND A.Accountid <> 2; 
		
		
		--698 Records in STG2. --1099 in TST4.

		

	 --#######################################################
		-- LOADING Autoship ADDRESSES WITH ISSUES AFTER UPDATE
	 --#######################################################
	
        IF OBJECT_ID(N'Tempdb..#MissingAddress') IS NOT NULL
            DROP TABLE #MissingAddress;
		
        SELECT  DISTINCT
                A.AccountID ,
                A.Active AS [Account Active] ,
                au.Active AS [Template Active] ,
                O.OrderID AS [TemplateID] ,
                O.StartDate ,
                OS.OrderShipmentID ,
                O.OrderTypeID ,
                OS.City ,
                OS.State ,
                OS.PostCode ,
                OS.Address1 ,
                OS.DayPhone
      INTO    #MissingAddress
        FROM    RodanFieldsLive.dbo.OrderShipments OS
                JOIN RodanFieldsLive.dbo.Orders O ON OS.OrderID = O.OrderID
                JOIN RFOperations.Hybris.Autoship au ON au.AutoshipID = O.OrderID
                JOIN RodanFieldsLive.dbo.OrderCustomers OC ON O.OrderID = OC.OrderID
                JOIN RodanFieldsLive.dbo.Accounts A ON OC.AccountID = A.AccountID
        WHERE   1 = 1
                AND O.OrderStatusID = 7
                AND O.OrderTypeID IN ( 4, 5 )
                AND ( ISNULL(OS.State, '') = ''
                      OR ISNULL(OS.PostCode, '') = ''
                      OR ISNULL(OS.Address1, '') = ''
                      OR ISNULL(OS.City, '') = ''
					  OR OS.City='null'
                    )
                AND A.AccountID <> 2
                AND A.Active = 1
                AND au.Active = 1;
		

		-- CREATING INDEX ON TEMP TABLE 
        CREATE CLUSTERED INDEX cls ON #MissingAddress(OrderShipmentID);

		
		-- FROM MATCHING STREET NAME TO GET OTHERS FIELDS.
        SELECT DISTINCT
                m.AccountID ,
                m.[Template Active] ,
                m.TemplateID ,
                m.OrderShipmentID ,
				ad.AccountAddressID,
				p.OrderPaymentID,
                m.Address1 ,
                COALESCE(ad.Address1, p.BillingStreetAddress, '') AS [Derived Streetname] ,
                m.City ,
                COALESCE(ad.City, p.BillingCity, '') AS [Derived City] ,
                COALESCE(p.BillingCity, ad.City, '') AS [Derived City] ,
                m.[State] ,
                COALESCE(ad.State, p.BillingState, '') AS [Derived State] ,
                m.PostCode ,
                COALESCE(ad.PostalCode, p.BillingPostalCode, '') AS [Derived PostalCode]
		 --FROM MissingAddress m
        FROM    #MissingAddress m
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND m.Address1 = ad.Address1
															  --AND LEFT(ad.PostalCode,5)=LEFT(m.PostCode,5)
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND p.BillingStreetAddress = m.Address1
															--AND LEFT(p.BillingPostalCode,5)=LEFT(m.PostCode,5)
        WHERE   ISNULL(m.Address1, '') <> ''
                AND ( ( COALESCE(ad.State, p.BillingState, '') <> ''
                        AND ISNULL(m.[State], '') = ''
                      )
                      OR ( COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                           AND ISNULL(m.PostCode, '') = ''
                         )
                      OR ( COALESCE(ad.City, p.BillingCity, '') <> ''
                           AND ISNULL(m.City, '') = ''
                         )
                      OR ( COALESCE(p.BillingCity, ad.City, '') <> ''
                           AND ISNULL(m.[State], '') = ''
                         )
                      OR ( COALESCE(ad.City, p.BillingCity, 'null') <> 'null'
                           AND ISNULL(m.[State], 'null') = 'null'
                         )
                    );

	--  1 Records in STG2 .

 
 -- MATCHING POSTALCODE WITH GET MISSING CITY AND STATE.

       SELECT DISTINCT
                m.AccountID ,
                m.OrderShipmentID ,
				ad.AccountAddressID,
				p.OrderPaymentID,
                m.Address1 ,
                 ad.Address1[AccountAddress1], p.BillingStreetAddress ,
                m.City ,
                 ad.City[AccountCity], p.BillingCity ,
                m.[State] ,
                 ad.State [AccountState], p.BillingState ,
                m.PostCode ,
                ad.PostalCode[AccountPostalCode], p.BillingPostalCode 
		 --FROM MissingAddress m
        FROM    #MissingAddress m
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
        WHERE   ISNULL(m.PostCode, '') <> ''
                 AND ( ( COALESCE(ad.State, p.BillingState, '') <> ''
                        AND ISNULL(m.[State], '') = ''
                      )                     
                      OR ( COALESCE(ad.City, p.BillingCity, '') <> ''
                           AND ISNULL(m.City, '') = ''
                         )
                      OR ( COALESCE(p.BillingCity, ad.City, '') <> ''
                           AND ISNULL(m.[State], '') = ''
                         )
                      OR ( COALESCE(ad.City, p.BillingCity, 'null') <> 'null'
                           AND ISNULL(m.[State], 'null') = 'null'
                         )
                    );
					

					-- 4 Records in STG2.

	
		
		-- WITH MATCHING POSTALCODE AND CITY TO UPDATE MISSING FIELDS 
        SELECT DISTINCT
                m.AccountID ,
                m.OrderShipmentID ,
                m.Address1 ,
                COALESCE(ad.Address1, p.BillingStreetAddress, '') AS [Derived Streetname] ,
                m.City ,
                COALESCE(ad.City, p.BillingCity, '') AS [Derived City] ,
                m.[State] ,
                COALESCE(ad.State, p.BillingState, '') AS [Derived State] ,
                m.PostCode ,
                COALESCE(ad.PostalCode, p.BillingPostalCode, '') AS [Derived PostalCode]
		 --FROM MissingAddress m
        FROM    #MissingAddress m
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND m.City = ad.City
                                                              AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND p.BillingCity = m.City
                                                            AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
        WHERE   ISNULL(m.City, '') <> ''
                AND ( ( COALESCE(ad.State, p.BillingState, '') <> ''
                        AND ISNULL(m.[State], '') = ''
                      )
					  OR ( COALESCE(ad.State, p.BillingState, '') <> 'null'
                        AND  m.[State]  = 'null'
                      )
                      OR ( COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                           AND ISNULL(m.Address1, '') = ''
                         )
                      OR ( COALESCE(p.BillingStreetAddress, ad.Address1, '') <> ''
                           AND ISNULL(m.Address1, '') = ''
                         )
						
                    );

					     

	--   0 Record   in STG2.


	
	 --###################################################
		-- LOADING Orders/Returns  ADDRESSES WITH ISSUES AFTER UPDATE
	 --###################################################
	
    IF OBJECT_ID(N'Tempdb..#MissingAddress') IS NOT NULL
        DROP TABLE #MissingAddress;
		
    SELECT  A.AccountID ,
            O.OrderID ,
            O.StartDate ,
            OS.OrderShipmentID ,
            O.OrderTypeID ,
            OS.City ,
            OS.State ,
            OS.PostCode ,
            OS.Address1 ,
            OS.DayPhone
    INTO    #MissingAddress
    FROM    RodanFieldsLive.dbo.OrderShipments OS
            JOIN RodanFieldsLive.dbo.Orders O ON OS.OrderID = O.OrderID
            JOIN RodanFieldsLive.dbo.OrderCustomers OC ON O.OrderID = OC.OrderID
            JOIN RodanFieldsLive.dbo.Accounts A ON OC.AccountID = A.AccountID
    WHERE   1 = 1
            AND O.CompleteDate >= '2016-01-01'
            AND O.OrderStatusID IN ( 4, 5 )
            AND O.OrderTypeID NOT IN ( 4, 5, 9, 11 )
            AND ( ISNULL(OS.State, '') = ''
                  OR ISNULL(OS.PostCode, '') = ''
                  OR ISNULL(OS.Address1, '') = ''
                  OR ISNULL(OS.City, '') = ''
				  OR OS.City='null'
                )
            AND A.AccountID <> 2
            AND A.Active = 1;

			-- 698 RECORDS IN STG2. 

		-- CREATING INDEX ON TEMP TABLE 
		CREATE CLUSTERED INDEX cls ON #MissingAddress(OrderShipmentID)

		
		-- FROM MATCHING STREET NAME TO GET OTHERS FIELDS.
        SELECT COUNT( DISTINCT m.OrderShipmentID) [TOTAL COUNTS]
		      /**  DISTINCT	
                m.AccountID ,
                m.OrderShipmentID ,
                m.Address1 ,
                COALESCE(ad.Address1, p.BillingStreetAddress, '') AS [Derived Streetname] ,
                m.City ,
                COALESCE(ad.City, p.BillingCity, '') AS [Derived City] ,
                m.[State] ,
                COALESCE(ad.State, p.BillingState, '') AS [Derived State] ,
                m.PostCode ,
                COALESCE(ad.PostalCode, p.BillingPostalCode, '') AS [Derived PostalCode]
				--***/
		 --FROM MissingAddress m
        FROM    #MissingAddress m
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND m.Address1 = ad.Address1
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND p.BillingStreetAddress = m.Address1
        WHERE   ISNULL(m.Address1, '') <> ''
                AND ( ( COALESCE(ad.State, p.BillingState, '') <> ''
                        AND ISNULL(m.[State], '') = ''
                      )
                      OR ( COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                           AND ISNULL(m.PostCode, '') = ''
                         )
                      OR ( COALESCE(p.BillingCity, ad.City, 'null') <> 'null'
                           AND ISNULL(m.City, 'null') = 'null'
                         )
                     
                    );

	--88 RECORDS IN STG2. 




 
 -- MATCHING POSTALCODE WITH GET MISSING CITY AND STATE.
       SELECT COUNT( DISTINCT m.OrderShipmentID) [TOTAL COUNTS]
		      /**  DISTINCT	
                m.AccountID ,
                m.OrderShipmentID ,
                m.Address1 ,
                COALESCE(ad.Address1, p.BillingStreetAddress, '') AS [Derived Streetname] ,
                m.City ,
                COALESCE(ad.City, p.BillingCity, '') AS [Derived City] ,
                m.[State] ,
                COALESCE(ad.State, p.BillingState, '') AS [Derived State] ,
                m.PostCode ,
                COALESCE(ad.PostalCode, p.BillingPostalCode, '') AS [Derived PostalCode]
		 --**********/
       FROM     #MissingAddress m
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
       WHERE    ISNULL(m.PostCode, '') <> ''
                AND ( ( COALESCE(ad.State, p.BillingState, '') <> ''
                        AND ISNULL(m.[State], '') = ''
                      )
                      OR ( COALESCE(p.BillingCity, ad.City, 'null') <> 'null'
                           AND ISNULL(m.City, 'null') = 'null'
                         )
                    );
                  
-- 171 RECORDS IN STG2. 
					
		
		-- WITH MATCHING POSTALCODE AND CITY TO UPDATE MISSING FIELDS 
        SELECT  COUNT(DISTINCT m.OrderShipmentID) [TOTAL COUNTS]
		      /**  DISTINCT	
                m.AccountID ,
                m.OrderShipmentID ,
                m.Address1 ,
                COALESCE(ad.Address1, p.BillingStreetAddress, '') AS [Derived Streetname] ,
                m.City ,
                COALESCE(ad.City, p.BillingCity, '') AS [Derived City] ,
                m.[State] ,
                COALESCE(ad.State, p.BillingState, '') AS [Derived State] ,
                m.PostCode ,
                COALESCE(ad.PostalCode, p.BillingPostalCode, '') AS [Derived PostalCode]
		 --**********/
        FROM    #MissingAddress m
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND m.City = ad.City
                                                              AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND p.BillingCity = m.City
                                                            AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
        WHERE   ISNULL(m.City, '') <> ''
                AND m.City <> 'null'
                AND ( ( COALESCE(ad.State, p.BillingState, '') <> ''
                        AND ISNULL(m.[State], '') = ''
                      )
                      OR ( COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                           AND ISNULL(m.Address1, '') = ''
                         )
                    );

				

	--   0 Records in STG2.




	
---================================================
-- RFL TO RFO ETL FLOW VALIDATION For Autoship 
--=================================================

SELECT  a.AutoshipID ,
        a.Active ,
        ac.Active ,
        ac.AccountID ,
        op.OrderShipmentID ,
        ap.AutoshipShippingAddressID ,
        op.City ,
        ap.Locale ,
        op.Address1 ,
        ap.Address1 ,
        op.State ,
        ap.Region ,
        op.PostCode ,
        ap.PostalCode ,
        ap.ServerModifiedDate
FROM    RFOperations.Hybris.Autoship a
        JOIN RodanFieldsLive.dbo.Accounts ac ON a.AccountID = ac.AccountID
        JOIN RFOperations.Hybris.AutoshipShippingAddress ap ON ap.AutoShipID = a.AutoshipID
        JOIN RodanFieldsLive.dbo.OrderShipments op ON op.OrderID = a.AutoshipID
                                                      AND op.OrderShipmentID = ap.AutoshipShippingAddressID
WHERE   a.CountryID = 236
        AND a.Active = 1
        AND ac.Active = 1
        AND ac.AccountID <> 2
        AND ( ( ISNULL(ap.Locale, '') = ''
                AND ISNULL(op.City, '') <> ''
              )
              OR ( ap.Locale = 'null'
                   AND op.City <> 'null'
                 )
              OR ( ISNULL(ap.Region, '') = ''
                   AND ISNULL(op.State, '') <> ''
                 )
              OR ( ISNULL(ap.Address1, '') = ''
                   AND ISNULL(op.Address1, '') <> ''
                 )
              OR ( ISNULL(ap.PostalCode, '') = ''
                   AND ISNULL(op.PostCode, '') <> ''
                 )
            );

-- 0  with this Issues not to cover all in STG2.


---================================================
-- RFL TO RFO ETL FLOW VALIDATION FOR RECENT ORDERS  
--=================================================
			
SELECT  O.OrderID ,
        ac.Active ,
        ac.AccountID ,
        op.OrderShipmentID ,
        ap.OrderBillingAddressID ,
        op.City ,
        ap.Locale ,
        op.Address1[RFL Address1] ,
        ap.Address1 ,
        op.State ,
        ap.Region ,
        op.PostCode [RFL PostalCode] ,
        ap.PostalCode ,
        ap.ServerModifiedDate
FROM    RFOperations.Hybris.Orders O
        JOIN RodanFieldsLive.dbo.Accounts ac ON O.AccountID = ac.AccountID
        JOIN RFOperations.Hybris.OrderBillingAddress ap ON ap.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments op ON op.OrderID = O.OrderID
                                                     AND op.OrderShipmentID = ap.OrderBillingAddressID
WHERE   1 = 1
        AND O.CompletionDate >= '2016-01-01'
        AND O.OrderStatusID IN ( 2, 4, 5 ,9)
        AND O.CountryID = 236
        AND ac.Active = 1
        AND ac.AccountID <> 2       
        AND ( ( ISNULL(ap.Locale, '') = ''
                AND ISNULL(op.City, '') <> ''
              )
			   OR ( ap.Locale = 'null'
                   AND op.City <> 'null'
                 )
              OR ( ISNULL(ap.Region, '') = ''
                   AND ISNULL(op.State, '') <> ''
                 )
              OR ( ISNULL(ap.Address1, '') = ''
                   AND ISNULL(op.Address1, '') <> ''
                 )
              OR ( ISNULL(ap.PostalCode, '') = ''
                   AND ISNULL(op.PostCode, '') <> ''
                 )
            );

	
	-- 0 In STG2.


	---########################################################################################################################### 



	

-- FINAL LIST OF AUTOSHIP BILLINGADDRESS


-- RFL Sides

SELECT  A.AccountID ,
        Op.OrderPaymentID ,
		o.OrderID AS [Templates],
        O.OrderTypeId ,
		t.Name AS [Order Types],
        Op.BillingCity ,
        Op.BillingState ,
        Op.BillingPostalCode ,
        Op.BillingStreetAddress ,
        Op.BillingPhoneNumber 
FROM    RodanFieldsLive.dbo.OrderPayments Op
        JOIN RodanFieldsLive.dbo.Orders O ON Op.OrderID = O.OrderID
		JOIN RFOperations.Hybris.Autoship at ON at.AutoshipID=o.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers OC ON O.OrderID = OC.OrderID
        JOIN RodanFieldsLive.dbo.Accounts A ON OC.AccountID = A.AccountID
		JOIN RodanFieldsLive.dbo.OrderTypes t ON t.OrderTypeID=o.OrderTypeID
WHERE   1 = 1
        AND O.OrderStatusID = 7
        AND O.OrderTypeID IN ( 4, 5 )
        AND ( ISNULL(Op.BillingState, '') = ''
              OR ISNULL(Op.BillingPostalCode, '') = ''
              OR ISNULL(Op.BillingStreetAddress, '') = ''
              OR ISNULL(Op.BillingCity, '') = ''
			  OR Op.BillingCity='null'
            )
        AND A.AccountID <> 2
        AND A.Active = 1
		AND at.Active=1

		-- 1140 records in STG2
		
---FINAL LISTS OF ORDRES BILLING ADDRESS MISSING FIELDS

SELECT  A.AccountID ,
        Op.OrderPaymentID ,
		o.OrderID ,
		O.OrderStatusID,
        O.OrderTypeId ,
		t.Name AS [Ordre Types],
        Op.BillingCity ,
        Op.BillingState ,
        Op.BillingPostalCode ,
        Op.BillingStreetAddress ,
        Op.BillingPhoneNumber 
FROM    RodanFieldsLive.dbo.OrderPayments Op
        JOIN RodanFieldsLive.dbo.Orders O ON Op.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers OC ON O.OrderID = OC.OrderID
        JOIN RodanFieldsLive.dbo.Accounts A ON OC.AccountID = A.AccountID
			JOIN RodanFieldsLive.dbo.OrderTypes t ON t.OrderTypeID=o.OrderTypeID
WHERE   1 = 1
       AND O.CompleteDate >= '2016-01-01'
		AND O.OrderStatusID IN (4,7 )
		AND O.OrderTypeId NOT  IN (4,5,9)
        AND ( ISNULL(Op.BillingState, '') = ''
              OR ISNULL(Op.BillingPostalCode, '') = ''
              OR ISNULL(Op.BillingStreetAddress, '') = ''
              OR ISNULL(Op.BillingCity, '') = ''
			  OR Op.BillingCity='null'
            )
        AND A.AccountID <> 2
        AND A.Active = 1;
		-- 2200 RECORDS IN STG2.



---FINAL LISTS OF Returns BILLING ADDRESS MISSING FIELDS

SELECT  A.AccountID ,
        Op.OrderPaymentID ,
		o.OrderID ,
		O.OrderStatusID,
        O.OrderTypeId ,
		t.Name AS [Ordre Types],
        Op.BillingCity ,
        Op.BillingState ,
        Op.BillingPostalCode ,
        Op.BillingStreetAddress ,
        Op.BillingPhoneNumber 
FROM    RodanFieldsLive.dbo.OrderPayments Op
        JOIN RodanFieldsLive.dbo.Orders O ON Op.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers OC ON O.OrderID = OC.OrderID
        JOIN RodanFieldsLive.dbo.Accounts A ON OC.AccountID = A.AccountID
			JOIN RodanFieldsLive.dbo.OrderTypes t ON t.OrderTypeID=o.OrderTypeID
WHERE   1 = 1
       AND O.CompleteDate >= '2016-01-01'
		AND O.OrderStatusID IN ( 4 )
		AND O.OrderTypeId  IN ( 9)
        AND ( ISNULL(Op.BillingState, '') = ''
              OR ISNULL(Op.BillingPostalCode, '') = ''
              OR ISNULL(Op.BillingStreetAddress, '') = ''
              OR ISNULL(Op.BillingCity, '') = ''
			  OR Op.BillingCity='null'
            )
        AND A.AccountID <> 2
        AND A.Active = 1;


		--- 235 records in STG2

		SELECT * FROM RodanFieldsLive.dbo.OrderStatus
	 --#######################################################
-- LOADING Autoship Billing ADDRESSES WITH ISSUES AFTER UPDATE
	 --#######################################################
	
        IF OBJECT_ID(N'Tempdb..#MissingAddress') IS NOT NULL
            DROP TABLE #MissingAddress;
	
SELECT  A.AccountID ,
        Op.OrderPaymentID ,
		o.OrderID ,
        O.OrderTypeId ,
        Op.BillingCity,
        Op.BillingState ,
        Op.BillingPostalCode ,
        Op.BillingStreetAddress ,
        Op.BillingPhoneNumber INTO  #MissingAddress
FROM    RodanFieldsLive.dbo.OrderPayments Op
        JOIN RodanFieldsLive.dbo.Orders O ON Op.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers OC ON O.OrderID = OC.OrderID
        JOIN RodanFieldsLive.dbo.Accounts A ON OC.AccountID = A.AccountID
WHERE   1 = 1
        AND O.OrderStatusID = 7
        AND O.OrderTypeID IN ( 4, 5 )
        AND ( ISNULL(Op.BillingState, '') = ''
              OR ISNULL(Op.BillingPostalCode, '') = ''
              OR ISNULL(Op.BillingStreetAddress, '') = ''
              OR ISNULL(Op.BillingCity, '') = ''
			  OR Op.BillingCity='null'
            )
        AND A.AccountID <> 2
        AND A.Active = 1;
        
		-- CREATING INDEX ON TEMP TABLE 
        CREATE CLUSTERED INDEX cls ON #MissingAddress(OrderPaymentID);

		
		-- FROM MATCHING STREET NAME TO GET OTHERS FIELDS.
        SELECT DISTINCT
                m.AccountID ,
                m.OrderID ,
                m.OrderPaymentID ,
                m.BillingStreetAddress ,
                COALESCE(ad.Address1, os.Address1, '') AS [Derived Streetname] ,
                m.BillingCity ,
                COALESCE(ad.City, os.City, '') AS [Derived City] ,
                COALESCE(os.City, ad.City, '') AS [Derived City] ,
                m.BillingCity ,
                COALESCE(ad.State, os.State, '') AS [Derived State] ,
                m.BillingPostalCode ,
                COALESCE(ad.PostalCode, os.PostCode, '') AS [Derived PostalCode]
		 --FROM MissingAddress m
        FROM    #MissingAddress m
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND m.BillingStreetAddress = ad.Address1
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
                                                              AND os.Address1 = m.BillingStreetAddress
        WHERE   ISNULL(m.BillingStreetAddress, '') <> ''
                AND ( ( COALESCE(ad.State, os.State, '') <> ''
                        AND ISNULL(m.[BillingState], '') = ''
                      )
                      OR ( COALESCE(ad.PostalCode, os.PostCode, '') <> ''
                           AND ISNULL(m.BillingPostalCode, '') = ''
                         )
                      OR ( COALESCE(ad.City, os.City, 'null') <> 'null'
                           AND ISNULL(m.BillingCity, '') = 'null'
                         )
                      OR ( COALESCE(os.City, ad.City, 'null') <> 'null'
                           AND ISNULL(m.BillingCity, '') = 'null'
                         )
                    );

	--0 RECORDS IN STG2 



 
 -- MATCHING POSTALCODE WITH GET MISSING CITY AND STATE.
 SELECT DISTINCT
        m.AccountID ,
        m.OrderID ,
        m.OrderPaymentID ,
        m.BillingStreetAddress ,
        COALESCE(ad.Address1, os.Address1, '') AS [Derived Streetname] ,
        m.BillingCity ,
        COALESCE(ad.City, os.City, '') AS [Derived City] ,
        COALESCE(os.City, ad.City, '') AS [Derived City] ,
        m.BillingCity ,
        COALESCE(ad.State, os.State, '') AS [Derived State] ,
        m.BillingPostalCode ,
        COALESCE(ad.PostalCode, os.PostCode, '') AS [Derived PostalCode]
		 --FROM MissingAddress m
 FROM   #MissingAddress m
        JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                        AND LEFT(m.BillingPostalCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
        JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
                                                      AND LEFT(os.PostCode, 5) = LEFT(m.BillingPostalCode,
                                                              5)
 WHERE  ISNULL(m.BillingPostalCode, '') <> ''
        AND ( ( COALESCE(ad.State, os.State, '') <> ''
                AND ISNULL(m.[BillingState], '') = ''
              )
              OR ( COALESCE(ad.City, os.City, 'null') <> 'null'
                   AND ISNULL(m.BillingCity, '') = 'null'
                 )
              OR ( COALESCE(os.City, ad.City, 'null') <> 'null'
                   AND ISNULL(m.BillingCity, '') = 'null'
                 )
            );

			-- 0 RECORDS IN STG2. 

	
		
		-- WITH MATCHING POSTALCODE AND CITY TO UPDATE MISSING FIELDS 


         SELECT DISTINCT
                m.AccountID ,
                m.OrderID ,
                m.OrderPaymentID ,
                m.BillingStreetAddress ,
                COALESCE(ad.Address1, os.Address1, '') AS [Derived Streetname] ,
                m.BillingCity ,
                COALESCE(ad.City, os.City, '') AS [Derived City] ,
                COALESCE(os.City, ad.City, '') AS [Derived City] ,
                m.BillingCity ,
                COALESCE(ad.State, os.State, '') AS [Derived State] ,
                m.BillingPostalCode ,
                COALESCE(ad.PostalCode, os.PostCode, '') AS [Derived PostalCode]
		 --FROM MissingAddress m
         FROM   #MissingAddress m
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.BillingPostalCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
                                                              AND m.BillingCity = ad.City
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
                                                              AND LEFT(os.PostCode,
                                                              5) = LEFT(m.BillingPostalCode,
                                                              5)
                                                              AND m.BillingCity = os.City
         WHERE  ISNULL(m.BillingCity, 'NULL') <> 'NULL'
                AND ( COALESCE(ad.State, os.State, '') <> ''
                      AND ISNULL(m.BillingState, '') = 'null'
                    );
		
                    -- 0 RECORDS IN STG2.
		
	 --#######################################################
		-- LOADING Orders BillingADDRESSES WITH ISSUES AFTER UPDATE
	 --#######################################################
	
        IF OBJECT_ID(N'Tempdb..#MissingAddress') IS NOT NULL
            DROP TABLE #MissingAddress;
	
SELECT  A.AccountID ,
        Op.OrderPaymentID ,
		o.OrderID ,
		O.OrderStatusID,
        O.OrderTypeId ,
        Op.BillingCity ,
        Op.BillingState ,
        Op.BillingPostalCode ,
        Op.BillingStreetAddress ,
        Op.BillingPhoneNumber  INTO #MissingAddress
FROM    RodanFieldsLive.dbo.OrderPayments Op
        JOIN RodanFieldsLive.dbo.Orders O ON Op.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers OC ON O.OrderID = OC.OrderID
        JOIN RodanFieldsLive.dbo.Accounts A ON OC.AccountID = A.AccountID
WHERE   1 = 1
       AND O.CompleteDate >= '2016-01-01'
		AND O.OrderStatusID IN ( 4, 5 )
		AND O.OrderTypeId  IN ( 4, 5, 9, 11)
        AND ( ISNULL(Op.BillingState, '') = ''
              OR ISNULL(Op.BillingPostalCode, '') = ''
              OR ISNULL(Op.BillingStreetAddress, '') = ''
              OR ISNULL(Op.BillingCity, '') = ''
			  OR Op.BillingCity='null'
            )
        AND A.AccountID <> 2
        AND A.Active = 1;

        
		-- CREATING INDEX ON TEMP TABLE 
        CREATE CLUSTERED INDEX cls ON #MissingAddress(OrderPaymentID);

		
		-- FROM MATCHING STREET NAME TO GET OTHERS FIELDS.
        SELECT DISTINCT
                m.AccountID ,
                m.OrderID ,
                m.OrderPaymentID ,
                m.BillingStreetAddress ,
                COALESCE(ad.Address1, os.Address1, '') AS [Derived Streetname] ,
                m.BillingCity ,
                COALESCE(ad.City, os.City, '') AS [Derived City] ,
                COALESCE(os.City, ad.City, '') AS [Derived City] ,
                m.BillingCity ,
                COALESCE(ad.State, os.State, '') AS [Derived State] ,
                m.BillingPostalCode ,
                COALESCE(ad.PostalCode, os.PostCode, '') AS [Derived PostalCode]
		 --FROM MissingAddress m
        FROM    #MissingAddress m
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND m.BillingStreetAddress = ad.Address1
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
                                                              AND os.Address1 = m.BillingStreetAddress
        WHERE   ISNULL(m.BillingStreetAddress, '') <> ''
                AND ( ( COALESCE(ad.State, os.State, '') <> ''
                        AND ISNULL(m.BillingState, '') = ''
                      )
                      OR ( COALESCE(ad.PostalCode, os.PostCode, '') <> ''
                           AND ISNULL(m.BillingPostalCode, '') = ''
                         )
                      OR ( COALESCE(ad.City, os.City, 'null') <> 'null'
                           AND ISNULL(m.BillingCity, 'null') = 'null'
                         )
                      OR ( COALESCE(os.City, ad.City, 'null') <> 'null'
                           AND ISNULL(m.BillingCity, '') = 'null'
                         )
                      OR ( COALESCE(ad.City, os.City, ' ') <> ' '
                           AND ISNULL(m.BillingCity, 'null') = 'null'
                         )
                    );

	--0 RECORDS IN  STG2



 
 -- MATCHING POSTALCODE WITH GET MISSING CITY AND STATE.
 SELECT DISTINCT
        m.AccountID ,
        m.OrderID ,
        m.OrderPaymentID ,
        m.BillingStreetAddress ,
        COALESCE(ad.Address1, os.Address1, '') AS [Derived Streetname] ,
        m.BillingCity ,
        COALESCE(ad.City, os.City, '') AS [Derived City] ,
        COALESCE(os.City, ad.City, '') AS [Derived City] ,
        m.BillingCity ,
        COALESCE(ad.State, os.State, '') AS [Derived State] ,
        m.BillingPostalCode ,
        COALESCE(ad.PostalCode, os.PostCode, '') AS [Derived PostalCode]
		 --FROM MissingAddress m
 FROM   #MissingAddress m
        JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                        AND LEFT(m.BillingPostalCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
        JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
                                                      AND LEFT(os.PostCode, 5) = LEFT(m.BillingPostalCode,
                                                              5)
 WHERE  ISNULL(m.BillingPostalCode, '') <> ''
        AND ( ( COALESCE(ad.State, os.State, '') <> ''
                AND ISNULL(m.BillingState, '') = ''
              )
              OR ( COALESCE(ad.City, os.City, 'null') <> 'null'
                   AND ISNULL(m.BillingCity, 'null') = 'null'
                 )
              OR ( COALESCE(os.City, ad.City, 'null') <> 'null'
                   AND ISNULL(m.BillingCity, '') = 'null'
                 )
              OR ( COALESCE(ad.City, os.City, ' ') <> ' '
                   AND ISNULL(m.BillingCity, 'null') = 'null'
                 )
            )

	-- 0 RECORDS IN STG2. 

	
		
		-- WITH MATCHING POSTALCODE AND CITY TO UPDATE MISSING FIELDS 


		 SELECT DISTINCT
                m.AccountID ,
				m.OrderID,
                m.OrderPaymentID ,
                m.BillingStreetAddress ,
                COALESCE(ad.Address1, os.Address1, '') AS [Derived Streetname] ,
                m.BillingCity ,
                COALESCE(ad.City,os.City, '') AS [Derived City] ,
				
                COALESCE( os.city,ad.City, '') AS [Derived City] ,
                m.BillingCity ,
                COALESCE(ad.State,os.State, '') AS [Derived State] ,
                m.BillingPostalCode ,
                COALESCE(ad.PostalCode, os.PostCode, '') AS [Derived PostalCode]
		 --FROM MissingAddress m
        FROM    #MissingAddress m				
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.BillingPostalCode,5) = LEFT(ad.PostalCode,5)
															  AND m.BillingCity=ad.City
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
                                                            AND LEFT(os.PostCode,5) = LEFT(m.BillingPostalCode,5)
															AND m.BillingCity=os.City
        WHERE  ISNULL(m.BillingCity, '') <> '' AND m.BillingCity<>'null'
		AND  COALESCE(ad.State, os.State, '') <> ''
                AND ISNULL(m.BillingState, '') = ''
              --0 RECORDS IN STG2. 


---================================================
-- RFL TO RFO ETL FLOW VALIDATION For Autoship 
--=================================================

SELECT  a.AutoshipID ,
        a.Active ,
        ac.Active ,
        ac.AccountID ,
        op.OrderPaymentID ,
        ap.AutoshipPaymentAddressID ,
        op.BillingCity ,
        ap.Locale ,
        op.BillingStreetAddress ,
        ap.Address1 ,
        op.BillingState ,
        ap.Region ,
        op.BillingPostalCode ,
        ap.PostalCode ,
        ap.ServerModifiedDate
FROM    RFOperations.Hybris.Autoship a
        JOIN RodanFieldsLive.dbo.Accounts ac ON a.AccountID = ac.AccountID
        JOIN RFOperations.Hybris.AutoshipPaymentAddress ap ON ap.AutoShipID = a.AutoshipID
        JOIN RodanFieldsLive.dbo.OrderPayments op ON op.OrderID = a.AutoshipID
                                                     AND op.OrderPaymentID = ap.AutoshipPaymentAddressID
WHERE   a.CountryID = 236
        AND a.Active = 1
        AND ac.Active = 1
        AND ac.AccountID <> 2      
        AND ( ( ISNULL(ap.Locale, '') = ''
                AND ISNULL(op.BillingCity, '') <> ''
              )
			  OR ( ISNULL(ap.Locale, 'null') = 'null'
                AND  op.BillingCity  <> 'null'
              )
              OR ( ISNULL(ap.Region, '') = ''
                   AND ISNULL(op.BillingState, '') <> ''
                 )
              OR ( ISNULL(ap.Address1, '') = ''
                   AND ISNULL(op.BillingStreetAddress, '') <> ''
                 )
              OR ( ISNULL(ap.PostalCode, '') = ''
                   AND ISNULL(op.BillingPostalCode, '') <> ''
                 )
            );

-- 1 with this Issues not to cover all in STG2.


---================================================
-- RFL TO RFO ETL FLOW VALIDATION FOR RECENT ORDERS  
--=================================================
			
SELECT  O.OrderID ,
        ac.Active ,
        ac.AccountID ,
        op.OrderPaymentID ,
        ap.OrderBillingAddressID ,
        op.BillingCity ,
        ap.Locale ,
        op.BillingStreetAddress ,
        ap.Address1 ,
        op.BillingState ,
        ap.Region ,
        op.BillingPostalCode ,
        ap.PostalCode ,
        ap.ServerModifiedDate
FROM    RFOperations.Hybris.Orders O
        JOIN RodanFieldsLive.dbo.Accounts ac ON O.AccountID = ac.AccountID
        JOIN RFOperations.Hybris.OrderBillingAddress ap ON ap.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderPayments op ON op.OrderID = O.OrderID
                                                     AND op.OrderPaymentID = ap.OrderBillingAddressID
WHERE   1 = 1
        AND O.CompletionDate >= '2016-01-01'
        AND O.OrderStatusID IN ( 2, 4, 5 )
        AND O.CountryID = 236
        AND ac.Active = 1
        AND ac.AccountID <> 2       
        AND ( ( ISNULL(ap.Locale, '') = ''
                AND ISNULL(op.BillingCity, '') <> ''
              )
			  OR ( ISNULL(ap.Locale, 'null') = 'null'
                AND  op.BillingCity  <> 'null')
              OR ( ISNULL(ap.Region, '') = ''
                   AND ISNULL(op.BillingState, '') <> ''
                 )
              OR ( ISNULL(ap.Address1, '') = ''
                   AND ISNULL(op.BillingStreetAddress, '') <> ''
                 )
              OR ( ISNULL(ap.PostalCode, '') = ''
                   AND ISNULL(op.BillingPostalCode, '') <> ''
                 )
            );

	
	--1 with this Issues in STG2.
	
	
---===================================================
-- RFL TO RFO ETL FLOW VALIDATION FOR RECENT RETURNS.  
--====================================================
			
SELECT  O.ReturnOrderID ,
        ac.Active [Account Active],
        ac.AccountID ,
        op.OrderPaymentID ,
        ap.ReturnBillingAddressID ,
        op.BillingCity ,
        ap.Locale ,
        op.BillingStreetAddress ,
        ap.AddressLine1 ,
        op.BillingState ,
        ap.Region ,
        op.BillingPostalCode ,
        ap.PostalCode ,
        ap.ServerModifiedDate
FROM    RFOperations.Hybris.ReturnOrder O
        JOIN RodanFieldsLive.dbo.Accounts ac ON O.AccountID = ac.AccountID
        JOIN RFOperations.Hybris.ReturnBillingAddress ap ON ap.ReturnOrderID = O.ReturnOrderID
        JOIN RodanFieldsLive.dbo.OrderPayments op ON op.OrderID = O.ReturnOrderID
                                                     AND op.OrderPaymentID = ap.ReturnBillingAddressID
WHERE   1 = 1
        AND O.CompletionDate >= '2016-01-01'      
        AND O.CountryID = 236
        AND ac.Active = 1
        AND ac.AccountID <> 2     
        AND ( ( ISNULL(ap.Locale, '') = ''
                AND ISNULL(op.BillingCity, '') <> ''
              )
			  OR ( ISNULL(ap.Locale, 'null') = 'null'
                AND  op.BillingCity  <> 'null')
              OR ( ISNULL(ap.Region, '') = ''
                   AND ISNULL(op.BillingState, '') <> ''
                 )
              OR ( ISNULL(ap.AddressLine1, '') = ''
                   AND ISNULL(op.BillingStreetAddress, '') <> ''
                 )
              OR ( ISNULL(ap.PostalCode, '') = ''
                   AND ISNULL(op.BillingPostalCode, '') <> ''
                 )
            );
		

	-- NONE OF THESE ISSUES IN STG2.







	--======================================================================
	-- VALIDATING WITH THIRD PARTY CITY WITH ZIPCODE DATABASE
	--======================================================================

	
IF OBJECT_ID(N'Tempdb..#ADDRESSES') IS NOT NULL
DROP TABLE #ADDRESSES1

--- FROM RFL DB.
SELECT DISTINCT
		A.AccountId,A.Active,at.Name AS [Account Types], ad.AccountAddressId,ad.AddressTypeID,t.Name AS [Types],ad.IsDefault,
		ad.City, ad.[State], ad.PostalCode, ad.Address1, ad.PhoneNumber INTO #ADDRESSES1
		
	FROM 
		RodanFieldsLive.dbo.Accounts A JOIN
		RodanFieldsLive.dbo.AccountAddresses ad ON A.AccountId = ad.AccountId
		JOIN RodanFieldsLive.dbo.AddressType t ON t.AddressTypeID=ad.AddressTypeID	
		JOIN RodanFieldsLive.dbo.AccountType at ON at.AccountTypeID = A.AccountTypeID
	WHERE 
		1=1
		AND 
		(
			ISNULL(ad.State,'') = '' 
			OR ISNULL (ad.PostalCode,'') = '' 
			OR ISNULL(ad.Address1, '')= ''
			OR ISNULL(ad.city, '')=''
			OR ad.City ='null'		--City Found like 'NULL' Strings.
		) 	
		AND A.Accountid <> 2
		AND A.Active=1		
		AND  ad.AddressTypeID=1 AND  ad.IsDefault=1
		

        SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #ADDRESSES1;
  --1360 RECORDS IN STG2.

  SELECT    B.Zipcode AS [BACKUP ZIPCODE] ,
            A.PostalCode ,
            B.City [BACKUP CITY ] ,
            A.City ,
            A.*
  FROM      #ADDRESSES1 A
            JOIN RFOperations.dbo.FederalDB_AddressCorrection B --ON LEFT( A.PostalCode ,5)= B.Zipcode
            ON LEFT(CAST(A.PostalCode AS NVARCHAR(12)), 5) = CAST(B.Zipcode AS NVARCHAR(10))
  WHERE     -- A.AccountAddressID=8483622 AND 
           ISNULL(A.City,'null') <> B.City;



    




        IF OBJECT_ID(N'Tempdb..#RFOADDRESSES') IS NOT NULL
            DROP TABLE #RFOADDRESSES;


		--- FROM RFO SIDE.
        SELECT  ab.AccountID ,
                rf.Active ,
                ad.AddressTypeID ,
                t.Name AS [Address Types] ,
                ad.IsDefault ,
                ad.Locale ,
                ad.Region ,
                ad.PostalCode ,
                ad.AddressLine1
        INTO    #RFOADDRESSES1
        FROM    RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
                JOIN RFOperations.RFO_Accounts.AccountContactAddresses acd ON acd.AccountContactId = ac.AccountContactId
                JOIN RFOperations.RFO_Accounts.Addresses ad ON ad.AddressID = acd.AddressID
                JOIN RFOperations.RFO_Reference.AddressType t ON t.AddressTypeID = ad.AddressTypeID
        WHERE   ab.AccountID <> 2
                AND rf.Active = 1
                AND ad.AddressTypeID = 1
                AND ad.IsDefault = 1
                AND ( ISNULL(ad.AddressLine1, '') = ''
                      OR ISNULL(ad.Locale, '') = ''
                      OR ISNULL(ad.Region, '') = ''
                      OR ISNULL(ad.PostalCode, '') = ''
                      OR ad.Locale = 'null'
                    );



					 SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #RFOADDRESSES1;
  --1352 RECORDS IN STG2.

  
					 SELECT * FROM    #RFOADDRESSES1;

  
  SELECT    B.Zipcode AS [BACKUP ZIPCODE] ,
            A.PostalCode ,
            B.City [BACKUP CITY ] ,
            A.Locale ,
			B.state,
            A.Region,
			A.AccountID,
			A.Active,
			A.AddressTypeID,
			A.IsDefault,
			A.AddressLine1
  FROM      #RFOADDRESSES1 A
            JOIN RFOperations.dbo.FederalDB_AddressCorrection B --ON LEFT( A.PostalCode ,5)= B.Zipcode
           ON LEFT(CAST(A.PostalCode AS NVARCHAR(12)), 5) = CAST(B.Zipcode AS NVARCHAR(10)) AND A.Region=b.[state]
  WHERE     -- A.AccountAddressID=8483622 AND 
           ISNULL(A.Locale,'') ='' OR a.Locale='null' OR ISNULL(A.Region,'')=''
		 