--#############################################################################################
-- FINAL EXCEPTION LIST OF ACCOUNTADDRESS FOR ACTIVE ACCOUNTS WITH MAIN OR DEFAULT ADDRESSES.
--#############################################################################################

--- FROM RFL DB.
SELECT DISTINCT
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
        ad.PhoneNumber
FROM    RodanFieldsLive.dbo.Accounts (NOLOCK) A
        JOIN RodanFieldsLive.dbo.AccountAddresses (NOLOCK) ad ON A.AccountID = ad.AccountID
        JOIN RodanFieldsLive.dbo.AddressType (NOLOCK) t ON t.AddressTypeID = ad.AddressTypeID
        JOIN RodanFieldsLive.dbo.AccountType (NOLOCK) at ON at.AccountTypeID = A.AccountTypeID
WHERE   1 = 1
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
            );-- We are Only considering MainAddress Types and Default.
		--5998 in Prod.




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
        FROM    RFOperations.RFO_Accounts.AccountBase (NOLOCK) ab
                JOIN RFOperations.RFO_Accounts.AccountRF (NOLOCK) rf ON rf.AccountID = ab.AccountID
                JOIN RFOperations.RFO_Accounts.AccountContacts (NOLOCK) ac ON ac.AccountId = ab.AccountID
                JOIN RFOperations.RFO_Accounts.AccountContactAddresses (NOLOCK) acd ON acd.AccountContactId = ac.AccountContactId
                JOIN RFOperations.RFO_Accounts.Addresses (NOLOCK) ad ON ad.AddressID = acd.AddressID
                JOIN RFOperations.RFO_Reference.AddressType (NOLOCK) t ON t.AddressTypeID = ad.AddressTypeID
        WHERE   ab.AccountID <> 2
                AND rf.Active = 1
                AND ( ad.AddressTypeID = 1
                      OR ad.IsDefault = 1
                    )
                AND ( ISNULL(ad.AddressLine1, '') = ''
                      OR ISNULL(ad.Locale, '') = ''
                      OR ISNULL(ad.Region, '') = ''
                      OR ISNULL(ad.PostalCode, '') = ''
                    );

					--5716 in pord.


--#######################################################################
		--Vlidation IF RFO still not having the Value updated in RFL
--#######################################################################


	WITH MissingRFO AS (	
SELECT  
DISTINCT
        cust.AccountID ,
        ab.AccountNumber ,       
        ad.*
FROM    RFOperations.RFO_Accounts.AccountRF cust ( NOLOCK )
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ( NOLOCK ) ON cust.AccountID = ab.AccountID
        INNER JOIN RodanFieldsLive.dbo.AccountAddresses addr ( NOLOCK ) ON addr.AccountID = cust.AccountID
        INNER JOIN RFOperations.RFO_Accounts.Addresses ad ( NOLOCK ) ON --acd.AddressID = ad.AddressID
		 addr.AccountAddressID=ad.AddressID
WHERE   ( ISNULL(ad.Region, '') = ''
          OR ISNULL(ad.Locale, '') = ''
         OR ISNULL(ad.AddressLine1, '') = ''
         OR ISNULL(ad.AddressLine1, '') = ' '
          OR ISNULL(ad.PostalCode, '') = ''
          OR ISNULL(ad.Region, '') = 'null'
          OR ISNULL(ad.Locale, '') = 'null'
        )
        AND ad.CountryID = 236
        AND cust.AccountID<>2 AND  Active = 1
		AND (ad.AddressTypeID=1
		OR  ad.IsDefault=1)
		
		
		
		  ),

	MissingRFL
	AS (
SELECT DISTINCT
		A.AccountId, ad.AccountAddressId,t.Name AS [Types],ad.IsDefault,
		ad.City, ad.[State], ad.PostalCode, ad.Address1, ad.PhoneNumber 
		
	FROM 
		RodanFieldsLive.dbo.Accounts( NOLOCK ) A JOIN
		RodanFieldsLive.dbo.AccountAddresses( NOLOCK ) ad ON A.AccountId = ad.AccountId
		JOIN RodanFieldsLive.dbo.AddressType( NOLOCK ) t ON t.AddressTypeID=ad.AddressTypeID	
	WHERE 
		1=1
		AND 
		(
			ISNULL(ad.State,'') = '' 
			OR ISNULL (ad.PostalCode,'') = '' 
			OR ISNULL(ad.Address1, '')= ''
			OR ISNULL(ad.city, '')=''
			OR ISNULL(ad.City,'null') ='null'		--City Found like 'NULL' Strings.
		) 	
		AND A.Accountid <> 2
		AND A.Active=1	)	
		--AND (ad.AddressTypeID=1 OR ad.IsDefault=1
		-- We are Only considering MainAddress Types and Default.
		

		SELECT * INTO #t FROM MissingRFO r WHERE NOT  EXISTS (SELECT 1 FROM MissingRFL l WHERE r.AddressID=l.AccountAddressID)



        SELECT  COUNT(*) [RFL Counts]
        FROM    RodanFieldsLive.dbo.AccountAddresses( NOLOCK )
        WHERE   AccountAddressID IN ( SELECT    AddressID
                                      FROM      #t );
        SELECT  COUNT(*) [RFO Counts]
        FROM    RFOperations.RFO_Accounts.Addresses( NOLOCK )
        WHERE   AddressID IN ( SELECT   AddressID
                               FROM     #t );





	--====================================================================
			---MISSING  VALIDATION USING ADDRESS1 OR BILLINGSTREETADDRESS 
	--====================================================================

		
	
		;
        WITH    MissingFields
                  AS ( SELECT   ac.AccountID ,
                                ad.AccountAddressID ,
                                ad.City ,
                                ad.[State] ,
                                ad.PostalCode ,
                                ad.Address1 ,
                                ad.PhoneNumber
                       FROM     RodanFieldsLive.dbo.Accounts( NOLOCK ) ac
                                JOIN RodanFieldsLive.dbo.AccountAddresses ( NOLOCK )ad ON ac.AccountID = ad.AccountID
                       WHERE    1 = 1
                                AND ( ISNULL(ad.[State], '') = ''
                                      OR ISNULL(ad.PostalCode, '') = ''
                                      OR ISNULL(ad.Address1, '') = ''
                                      OR ISNULL(ad.City, '') = ''
									  OR ad.City='null'
                                    )
                                AND ac.AccountID <> 2
								AND ac.Active=1
								AND (ad.AddressTypeID=1 OR ad.IsDefault=1)
                     )
            SELECT DISTINCT
                    a.AccountID ,
                    ad.Address1 ,
                    t.Address1 [BackUp Address1] ,                   
                    t.City [BacuUP City] ,
                    ad.City [City] ,                   
                    t.[State] AS [BackUp State] ,
                    ad.[State] ,                   
                    t.PostalCode AS [BackUp PostalCode] ,
                    ad.PostalCode 
            FROM    RodanFieldsLive.dbo.Accounts( NOLOCK ) a
                    JOIN RodanFieldsLive.dbo.AccountAddresses( NOLOCK ) ad ON ad.AccountID = a.AccountID
                    JOIN MissingFields t ON t.AccountID = ad.AccountID
                                            AND ad.Address1 = t.Address1                  
            WHERE   1 = 1
                    AND a.AccountID <> 2
                    AND ISNULL(ad.Address1, '') <> ''
					--AND ISNULL(t.Address1,'')=''
                    AND ( ( isnull(ad.[State], '') <> ''
                            AND ISNULL(t.[State], '') = ''
                          )
                          OR ( isnull(ad.PostalCode, '') <> ''
                               AND ISNULL(t.PostalCode, '') = ''
                             )
                          OR ( isnull(ad.City, '') <> ''
                               AND ISNULL(t.City, '') = ''
                             )
                        );
			--25,856 RECORDS


				;
        WITH    MissingFields
                  AS ( SELECT   ac.AccountID ,
                                ad.AccountAddressID ,
                                ad.City ,
                                ad.[State] ,
                                ad.PostalCode ,
                                ad.Address1 ,
                                ad.PhoneNumber
                       FROM     RodanFieldsLive.dbo.Accounts( NOLOCK ) ac
                                JOIN RodanFieldsLive.dbo.AccountAddresses ( NOLOCK )ad ON ac.AccountID = ad.AccountID
                       WHERE    1 = 1
                                AND ( ISNULL(ad.[State], '') = ''
                                      OR ISNULL(ad.PostalCode, '') = ''
                                      OR ISNULL(ad.Address1, '') = ''
                                      OR ISNULL(ad.City, '') = ''
									  OR ad.City='null'
                                    )
                                AND ac.AccountID <> 2
								AND ac.Active=1
								AND (ad.AddressTypeID=1 OR ad.IsDefault=1)
                     )
            SELECT DISTINCT
                    a.AccountID ,
                    ad.Address1 ,
                    t.Address1 [BackUp Address1] ,
                    p.BillingStreetAddress ,
                    t.City [BacuUP City] ,
                    ad.City [City] ,
                    p.BillingCity ,
                    t.[State] AS [BackUp State] ,
                    ad.[State] ,
                    p.BillingState ,
                    t.PostalCode AS [BackUp PostalCode] ,
                    ad.PostalCode ,
                    p.BillingPostalCode ,
                    CASE WHEN ISNULL(ad.Address1, '') <> ''
                         THEN 'Account Address1'
                         WHEN ISNULL(p.BillingStreetAddress, '') <> ''
                         THEN 'BillingStreetAddress'
                    END AS [SOURCE]
            FROM    RodanFieldsLive.dbo.Accounts( NOLOCK ) a
                    JOIN RodanFieldsLive.dbo.AccountAddresses( NOLOCK ) ad ON ad.AccountID = a.AccountID
                    JOIN MissingFields t ON t.AccountID = ad.AccountID
                                            AND ad.Address1 = t.Address1
                    JOIN RodanFieldsLive.dbo.OrderCustomers( NOLOCK ) oc ON oc.AccountID = t.AccountID
                    JOIN RodanFieldsLive.dbo.OrderPayments( NOLOCK ) p ON p.OrderCustomerID = oc.OrderCustomerID
                                                              AND p.BillingStreetAddress = t.Address1
            WHERE   1 = 1
                    AND a.AccountID <> 2
                    AND COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
					--AND ISNULL(t.Address1,'')=''
                    AND ( ( COALESCE(ad.[State], p.BillingState, '') <> ''
                            AND ISNULL(t.[State], '') = ''
                          )
                          OR ( COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                               AND ISNULL(t.PostalCode, '') = ''
                             )
                          OR ( COALESCE(ad.City, p.BillingCity, '') <> ''
                               AND ISNULL(t.City, '') = ''
                             )
                        );
			--25,856 RECORDS



			--===========================================================
			---MISSING  VALIDATION USING PostalCode OR BILLINGPostalCode 
			--=============================================================

		
		
		;
        WITH    MissingFields
                  AS ( SELECT   ac.AccountID ,
                                ad.AccountAddressID ,
                                ad.City ,
                                ad.[State] ,
                                ad.PostalCode ,
                                ad.Address1 ,
                                ad.PhoneNumber
                       FROM     RodanFieldsLive.dbo.Accounts( NOLOCK ) ac
                                JOIN RodanFieldsLive.dbo.AccountAddresses( NOLOCK ) ad ON ac.AccountID = ad.AccountID
                       WHERE    1 = 1
                                AND ( ISNULL(ad.[State], '') = ''
                                      OR ISNULL(ad.PostalCode, '') = ''
                                      OR ISNULL(ad.Address1, '') = ''
                                      OR ISNULL(ad.City, '') = ''
									  OR ad.City='null'
                                    )
                                AND ac.AccountID <> 2
								AND ac.Active=1
								AND (ad.AddressTypeID=1 OR ad.IsDefault=1)
                     )
		
SELECT DISTINCT a.AccountID ,
        ad.Address1 ,
        t.Address1 [Missing Address1] ,
		p.BillingStreetAddress,
        t.City [Missing City] ,
        ad.City [City],
		p.BillingCity,
        t.[State] AS [Missing State] ,
        ad.[State] ,
		p.BillingState,
        t.PostalCode AS [Missing PostalCode] ,
        ad.PostalCode,
		p.BillingPostalCode,
		CASE WHEN ISNULL(ad.PostalCode,'')<>'' THEN 'Account  PostalCode'
		WHEN ISNULL(p.BillingPostalCode,'')<>''THEN ' BillingPostalCode'
		END AS [SOURCE]
FROM    RodanFieldsLive.dbo.Accounts( NOLOCK ) a
        JOIN RodanFieldsLive.dbo.AccountAddresses( NOLOCK )ad ON ad.AccountID = a.AccountID
        JOIN MissingFields t ON t.AccountID = ad.AccountID
                                                              AND ad.PostalCode = t.PostalCode
		JOIN RodanFieldsLive.dbo.OrderCustomers( NOLOCK ) oc ON oc.AccountID = t.AccountID
		JOIN RodanFieldsLive.dbo.OrderPayments( NOLOCK ) p ON p.OrderCustomerID=oc.OrderCustomerID
		AND p.BillingPostalCode=t.PostalCode

WHERE   1 = 1
        AND a.AccountID <> 2
        AND COALESCE(ad.PostalCode,p.BillingPostalCode, '') <> ''
        AND ( ( COALESCE(ad.[State],p.BillingState, '') <> ''
                AND COALESCE(t.[State],p.BillingState, '') = ''
              )
              OR ( COALESCE(ad.Address1,p.BillingStreetAddress, '') <> ''
                   AND ISNULL(t.Address1, '') = ''
                 )
             
              OR ( COALESCE(ad.City,p.BillingCity, '') <> ''
                   AND ISNULL(t.City, '') = ''
                 )
            );



			
			--==========================================================================
			---MISSING  VALIDATION USING CitywithPostalCode OR BILLING CityWithPostalCode 
			--===========================================================================

		
		
		;
        WITH    MissingFields
                  AS ( SELECT   ac.AccountID ,
                                ad.AccountAddressID ,
                                ad.City ,
                                ad.[State] ,
                                ad.PostalCode ,
                                ad.Address1 ,
                                ad.PhoneNumber
                       FROM     RodanFieldsLive.dbo.Accounts( NOLOCK ) ac
                                JOIN RodanFieldsLive.dbo.AccountAddresses( NOLOCK ) ad ON ac.AccountID = ad.AccountID
                       WHERE    1 = 1
                                AND ( ISNULL(ad.[State], '') = ''
                                      OR ISNULL(ad.PostalCode, '') = ''
                                      OR ISNULL(ad.Address1, '') = ''
                                      OR ISNULL(ad.City, '') = ''
									  OR ad.City='null'
                                    )
                                AND ac.AccountID <> 2
								AND ac.Active=1
                     )
		
SELECT DISTINCT a.AccountID ,
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
FROM    RodanFieldsLive.dbo.Accounts( NOLOCK ) a
        JOIN RodanFieldsLive.dbo.AccountAddresses( NOLOCK ) ad ON ad.AccountID = a.AccountID
        JOIN MissingFields t ON t.AccountID = ad.AccountID
                                                              AND ad.City = t.City AND ad.PostalCode LIKE '%'+t.PostalCode+'%'
		JOIN RodanFieldsLive.dbo.OrderCustomers( NOLOCK ) oc ON oc.AccountID = t.AccountID
		JOIN RodanFieldsLive.dbo.OrderPayments( NOLOCK ) p ON p.OrderCustomerID=oc.OrderCustomerID
		AND p.BillingCity=t.City AND p.BillingCity LIKE '%'+t.PostalCode+'%'

WHERE   1 = 1
        AND a.AccountID <> 2
        AND COALESCE(ad.City,p.BillingCity, '') <> ''
        AND ( ( COALESCE(ad.[State],p.BillingState, '') <> ''
                AND COALESCE(t.[State],p.BillingState, '') = ''
              )
              OR ( COALESCE(ad.Address1,p.BillingStreetAddress, '') <> ''
                   AND ISNULL(t.Address1, '') = ''
                 )
             
              OR ( COALESCE(ad.PostalCode,p.BillingPostalCode, '') <> ''
                   AND ISNULL(t.PostalCode, '') = ''
                 )
            );




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
            FROM    RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad
                    JOIN RodanFieldsLive.dbo.Accounts (NOLOCK)ac ON ac.AccountID = ad.AccountID
                    JOIN RFOperations.RFO_Accounts.Addresses(NOLOCK) a ON a.AddressID = ad.AccountAddressID
                                                              --AND a.AddressTypeID = ad.AddressTypeID
            WHERE   ( ( ISNULL(a.AddressLine1, '') = ''
                        AND ISNULL(ad.Address1, '') <> ''
                      )
                      OR ( ISNULL(a.Locale, '') = ''
                           AND ISNULL(ad.City, '') <> ''
                         )
                      OR ( ISNULL(a.Region, '') = ''
                           AND ISNULL(ad.State, '') <> ''
                         )
                      OR ( ISNULL(a.PostalCode, '') = ''
                           AND ISNULL(ad.PostalCode, '') <> ''
                         )
                    );
			--1 Records in Prod now.

            SELECT  COUNT(*) [Total Counts]
            FROM    RFOperations.RFO_Accounts.Addresses(NOLOCK)
            WHERE   ChangedByUser = 'MAIN-4486';


	---************************************************************************************************************************
	




    SELECT  a.AccountID ,
            a.AutoshipID ,
            a.Active [Template Active] ,
            t.Name [Template Types] ,
            asd.Address1 ,
            asd.Locale ,
            asd.Region ,
            asd.PostalCode
    FROM    RFOperations.Hybris.Autoship (NOLOCK) a
            JOIN RFOperations.RFO_Reference.AutoShipType (NOLOCK) t ON t.AutoShipTypeID = a.AutoshipTypeID
            JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) asd ON asd.AutoShipID = a.AutoshipID
    WHERE   a.Active = 1 AND a.AccountID<>2
            AND ( ISNULL(asd.Address1, '') = ''
                  OR ISNULL(asd.Locale, '') = ''
                  OR asd.Locale = 'null'
                  OR ISNULL(asd.Region, '') = ''
                  OR ISNULL(asd.PostalCode, '') = ''
                );


-- 2437 in Prod now .


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
FROM    RodanFieldsLive.dbo.OrderShipments(NOLOCK) OS
        JOIN RodanFieldsLive.dbo.Orders(NOLOCK) O ON OS.OrderId = O.OrderId
        JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) OC ON O.OrderId = OC.OrderId
        JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) A ON OC.AccountId = A.AccountId
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

	--2370 counts in Prod 


---FINAL LIST OF ORDERS AND RETURNORDERS 
		
SELECT  A.AccountId ,
        OS.OrderShipmentId ,
        O.OrderTypeId ,
		t.Name AS [OrderType],
        OS.City ,
        OS.State ,
        OS.PostCode ,
        OS.Address1 ,
        OS.DayPhone --INTO RFOperations.Hybris.Orders_AddressIssues
FROM    RodanFieldsLive.dbo.OrderShipments(NOLOCK) OS
        JOIN RodanFieldsLive.dbo.Orders(NOLOCK) O ON OS.OrderId = O.OrderId
		JOIN RodanFieldsLive.dbo.OrderTypes(NOLOCK) t ON t.OrderTypeID = O.OrderTypeID
        JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) OC ON O.OrderId = OC.OrderId
        JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) A ON OC.AccountId = A.AccountId
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
		
		
		--6294 Records in Prod.

		
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
        FROM    RodanFieldsLive.dbo.OrderShipments(NOLOCK) OS
                JOIN RodanFieldsLive.dbo.Orders(NOLOCK) O ON OS.OrderID = O.OrderID
                JOIN RFOperations.Hybris.Autoship(NOLOCK) au ON au.AutoshipID = O.OrderID
                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) OC ON O.OrderID = OC.OrderID
                JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) A ON OC.AccountID = A.AccountID
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
				m.[Template Active],
				m.TemplateID,
                m.OrderShipmentID ,
                m.Address1 ,
                COALESCE(ad.Address1, p.BillingStreetAddress, '') AS [Derived Streetname] ,
                m.City ,
                COALESCE(ad.City, p.BillingCity, '') AS [Derived City] ,
				
                COALESCE( p.BillingCity,ad.City, '') AS [Derived City] ,
                m.[State] ,
                COALESCE(ad.State, p.BillingState, '') AS [Derived State] ,
                m.PostCode ,
                COALESCE(ad.PostalCode, p.BillingPostalCode, '') AS [Derived PostalCode]
		 --FROM MissingAddress m
        FROM    #MissingAddress m				
                JOIN RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad ON ad.AccountID = m.AccountID
                                                              AND m.Address1 = ad.Address1
                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments(NOLOCK) p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND p.BillingStreetAddress = m.Address1
        WHERE   m.OrderTypeID IN ( 4, 5 )		
                AND ISNULL(m.Address1, '') <> ''
                AND ( COALESCE(ad.State, p.BillingState, '') <> ''
                      OR COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                      OR COALESCE(ad.City, p.BillingCity, '') <> ''
					    OR COALESCE( p.BillingCity,ad.City, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    )

	-- 3914 in Prod



 
 -- MATCHING POSTALCODE WITH GET MISSING CITY AND STATE.
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
                JOIN RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments(NOLOCK) p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
        WHERE   ISNULL(m.PostCode, '') <> ''
                AND ( COALESCE(ad.State, p.BillingState, '') <> ''
                      OR COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                      OR COALESCE(ad.City, p.BillingCity, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    ) AND m.Address1= COALESCE(ad.Address1, p.BillingStreetAddress, '')

					-- 4157 in Prod
	
		
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
                JOIN RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad ON ad.AccountID = m.AccountID
                                                              AND m.City = ad.City
                                                              AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments(NOLOCK) p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND p.BillingCity = m.City
                                                            AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
        WHERE   ISNULL(m.City, '') <> ''
                AND ( COALESCE(ad.State, p.BillingState, '') <> ''
                      OR COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                      OR COALESCE(ad.City, p.BillingCity, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    );

	--64 Records in Prod.


	
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
    FROM    RodanFieldsLive.dbo.OrderShipments(NOLOCK) OS
            JOIN RodanFieldsLive.dbo.Orders(NOLOCK) O ON OS.OrderID = O.OrderID
            JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) OC ON O.OrderID = OC.OrderID
            JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) A ON OC.AccountID = A.AccountID
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

			--6294 record in Prod

		-- CREATING INDEX ON TEMP TABLE 
		CREATE CLUSTERED INDEX cls ON #MissingAddress(OrderShipmentID)

		
		-- FROM MATCHING STREET NAME TO GET OTHERS FIELDS.
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
                JOIN RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad ON ad.AccountID = m.AccountID
                                                              AND m.Address1 = ad.Address1
                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments(NOLOCK) p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND p.BillingStreetAddress = m.Address1
        WHERE   m.OrderTypeID IN ( 4, 5 )		
                AND ISNULL(m.Address1, '') <> ''
                AND ( COALESCE(ad.State, p.BillingState, '') <> ''
                      OR COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                      OR COALESCE(ad.City, p.BillingCity, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    );

	--  NULL in Prod.



 
 -- MATCHING POSTALCODE WITH GET MISSING CITY AND STATE.
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
                JOIN RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments(NOLOCK) p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
        WHERE   ISNULL(m.PostCode, '') <> ''
                AND ( COALESCE(ad.State, p.BillingState, '') <> ''
                      OR COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                      OR COALESCE(ad.City, p.BillingCity, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    );

					--11,721 in Prod.
	
		
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
                JOIN RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad ON ad.AccountID = m.AccountID
                                                              AND m.City = ad.City
                                                              AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments(NOLOCK) p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND p.BillingCity = m.City
                                                            AND LEFT(m.PostCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
        WHERE   ISNULL(m.City, '') <> ''
                AND ( COALESCE(ad.State, p.BillingState, '') <> ''
                      OR COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                      OR COALESCE(ad.City, p.BillingCity, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    );

	--172 Records in Prod.




	
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
        JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) ac ON a.AccountID = ac.AccountID
        JOIN RFOperations.Hybris.AutoshipShippingAddress(NOLOCK) ap ON ap.AutoShipID = a.AutoshipID
        JOIN RodanFieldsLive.dbo.OrderShipments(NOLOCK) op ON op.OrderID = a.AutoshipID
                                                     AND op.OrderShipmentID = ap.AutoshipShippingAddressID
WHERE   a.CountryID = 236
        AND a.Active = 1
        AND ac.Active = 1
        AND ac.AccountID <> 2      
        AND ( ( ISNULL(ap.Locale, '') = ''
                AND ISNULL(op.City, '') <> ''
              )
              OR ( ISNULL(ap.Locale, '') = 'null'
                AND ISNULL(op.City, '') <> 'null'
              )
              OR
			  ( ISNULL(ap.Region, '') = ''
                   AND ISNULL(op.State, '') <> ''
                 )
              OR ( ISNULL(ap.Address1, '') = ''
                   AND ISNULL(op.Address1, '') <> ''
                 )
              OR ( ISNULL(ap.PostalCode, '') = ''
                   AND ISNULL(op.PostCode, '') <> ''
                 )
            );

-- 8 with this Issues not to cover all in Prod.


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
FROM    RFOperations.Hybris.Orders(NOLOCK) O
        JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) ac ON O.AccountID = ac.AccountID
        JOIN RFOperations.Hybris.OrderBillingAddress(NOLOCK) ap ON ap.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderShipments(NOLOCK)op ON op.OrderID = O.OrderID
                                                     AND op.OrderShipmentID = ap.OrderBillingAddressID
WHERE   1 = 1
        AND O.CompletionDate >= '2016-01-01'
        AND O.OrderStatusID IN ( 2, 4, 5 )
        AND O.CountryID = 236
        AND ac.Active = 1
        AND ac.AccountID <> 2       
        AND ( ( ISNULL(ap.Locale, '') = 'null'
                AND ISNULL(op.City, '') <> 'null'
              )
              OR( ISNULL(ap.Locale, '') = ''
                AND ISNULL(op.City, '') <> ''
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

			-- NONE in Prod.
	
	---##############################################################################################################



	

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
FROM    RodanFieldsLive.dbo.OrderPayments(NOLOCK) Op
        JOIN RodanFieldsLive.dbo.Orders(NOLOCK) O ON Op.OrderID = O.OrderID
		JOIN RFOperations.Hybris.Autoship(NOLOCK) at ON at.AutoshipID=o.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) OC ON O.OrderID = OC.OrderID
        JOIN RodanFieldsLive.dbo.Accounts (NOLOCK)A ON OC.AccountID = A.AccountID
		JOIN RodanFieldsLive.dbo.OrderTypes(NOLOCK) t ON t.OrderTypeID=o.OrderTypeID
WHERE   1 = 1
        AND O.OrderStatusID = 7
        AND O.OrderTypeID IN ( 4, 5 )
        AND ( ISNULL(Op.BillingState, '') = ''
              OR ISNULL(Op.BillingPostalCode, '') = ''
              OR ISNULL(Op.BillingStreetAddress, '') = ''
              OR ISNULL(Op.BillingCity, '') = ''
			  OR op.BillingCity='null'
            )
        AND A.AccountID <> 2
        AND A.Active = 1
		AND at.Active=1

		-- 6733 records in Prod.
		
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
FROM    RodanFieldsLive.dbo.OrderPayments(NOLOCK) Op
        JOIN RodanFieldsLive.dbo.Orders(NOLOCK) O ON Op.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) OC ON O.OrderID = OC.OrderID
        JOIN RodanFieldsLive.dbo.Accounts(NOLOCK)A ON OC.AccountID = A.AccountID
			JOIN RodanFieldsLive.dbo.OrderTypes(NOLOCK) t ON t.OrderTypeID=o.OrderTypeID
WHERE   1 = 1
       AND O.CompleteDate >= '2016-01-01'
		AND O.OrderStatusID IN (4,7 )
		AND O.OrderTypeId NOT  IN (4,5,9)
        AND ( ISNULL(Op.BillingState, '') = ''
              OR ISNULL(Op.BillingPostalCode, '') = ''
              OR ISNULL(Op.BillingStreetAddress, '') = ''
              OR ISNULL(Op.BillingCity, '') = ''
            )
        AND A.AccountID <> 2
        AND A.Active = 1;

		--37099 in Prod.

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
FROM    RodanFieldsLive.dbo.OrderPayments(NOLOCK) Op
        JOIN RodanFieldsLive.dbo.Orders(NOLOCK) O ON Op.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) OC ON O.OrderID = OC.OrderID
        JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) A ON OC.AccountID = A.AccountID
			JOIN RodanFieldsLive.dbo.OrderTypes(NOLOCK) t ON t.OrderTypeID=o.OrderTypeID
WHERE   1 = 1
       AND O.CompleteDate >= '2016-01-01'
		AND O.OrderStatusID IN ( 4 )
		AND O.OrderTypeId  IN ( 9)
        AND ( ISNULL(Op.BillingState, '') = ''
              OR ISNULL(Op.BillingPostalCode, '') = ''
              OR ISNULL(Op.BillingStreetAddress, '') = ''
              OR ISNULL(Op.BillingCity, '') = ''
            )
        AND A.AccountID <> 2
        AND A.Active = 1;


		--- 511 records in Prod.

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
FROM    RodanFieldsLive.dbo.OrderPayments(NOLOCK) Op
        JOIN RodanFieldsLive.dbo.Orders(NOLOCK) O ON Op.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) OC ON O.OrderID = OC.OrderID
        JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) A ON OC.AccountID = A.AccountID
WHERE   1 = 1
        AND O.OrderStatusID = 7
        AND O.OrderTypeID IN ( 4, 5 )
        AND ( ISNULL(Op.BillingState, '') = ''
              OR ISNULL(Op.BillingPostalCode, '') = ''
              OR ISNULL(Op.BillingStreetAddress, '') = ''
              OR ISNULL(Op.BillingCity, '') = ''
			  OR OP.BillingCity='null'
            )
        AND A.AccountID <> 2
        AND A.Active = 1;
		--6732 Records in Prod.
        
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
                JOIN RodanFieldsLive.dbo.AccountAddresses (NOLOCK) ad ON ad.AccountID = m.AccountID
                                                              AND m.BillingStreetAddress = ad.Address1
                JOIN RodanFieldsLive.dbo.OrderCustomers (NOLOCK) oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments (NOLOCK) os ON os.OrderCustomerID = oc.OrderCustomerID
                                                              AND os.Address1 = m.BillingStreetAddress
        WHERE   ISNULL(m.BillingStreetAddress, '') <> ''
                AND ( ( COALESCE(ad.State, os.State, '') <> ''
                        AND ISNULL(m.BillingState, '') = ''
                      )
                      OR ( COALESCE(ad.PostalCode, os.PostCode, '') <> ''
                           AND ISNULL(m.BillingPostalCode, '') = ''
                         )
                      OR ( COALESCE(os.Address1, ad.Address1, '') <> ''
                           AND ISNULL(m.BillingStreetAddress, '') = ''
                         )
                      OR ( COALESCE(ad.City, os.City, '') <> ''
                           AND ISNULL(m.BillingCity, '') = ''
                         )
                      OR ( COALESCE(os.City, ad.City, '') <> 'null'
                           AND ISNULL(m.BillingState, '') = 'null'
                         )
                    );

	--  6732 in STG2



 
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
        JOIN RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad ON ad.AccountID = m.AccountID
                                                        AND LEFT(m.BillingPostalCode,
                                                              5) = LEFT(ad.PostalCode,
                                                              5)
        JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.AccountID = m.AccountID
        JOIN RodanFieldsLive.dbo.OrderShipments(NOLOCK) os ON os.OrderCustomerID = oc.OrderCustomerID
                                                      AND LEFT(os.PostCode, 5) = LEFT(m.BillingPostalCode,
                                                              5)
 WHERE  ISNULL(m.BillingPostalCode, '') <> ''
        AND ( ( COALESCE(ad.State, os.State, '') <> ''
                AND ISNULL(m.BillingState, '') = ''
              )
              OR ( COALESCE(ad.PostalCode, os.PostCode, '') <> ''
                   AND ISNULL(m.BillingPostalCode, '') = ''
                 )
              OR ( COALESCE(os.Address1, ad.Address1, '') <> ''
                   AND ISNULL(m.BillingStreetAddress, '') = ''
                 )
              OR ( COALESCE(ad.City, os.City, '') <> ''
                   AND ISNULL(m.BillingCity, '') = ''
                 )
              OR ( COALESCE(os.City, ad.City, '') <> 'null'
                   AND ISNULL(m.BillingCity, '') = 'null'
                 )
            );

	

	
		
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
                JOIN RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.BillingPostalCode,5) = LEFT(ad.PostalCode,5)
															  AND m.BillingCity=ad.City
                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments(NOLOCK) os ON os.OrderCustomerID = oc.OrderCustomerID
                                                            AND LEFT(os.PostCode,5) = LEFT(m.BillingPostalCode,5)
															AND m.BillingCity=os.City
        WHERE  ISNULL(m.BillingCity, '') <> ''
                AND ( COALESCE(ad.State, os.State, '') <> ''
                      OR COALESCE(ad.PostalCode, os.PostCode, '') <> ''
                      OR COALESCE( os.Address1,ad.Address1, '') <> ''
                      OR COALESCE(ad.City,os.City, '') <> ''
					    OR COALESCE( os.city,ad.City, '') <> ''
                      OR  COALESCE(ad.Address1, os.Address1, '') <> ''
                    )


		
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
FROM    RodanFieldsLive.dbo.OrderPayments(NOLOCK) Op
        JOIN RodanFieldsLive.dbo.Orders(NOLOCK) O ON Op.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) OC ON O.OrderID = OC.OrderID
        JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) A ON OC.AccountID = A.AccountID
WHERE   1 = 1
       AND O.CompleteDate >= '2016-01-01'
		AND O.OrderStatusID IN ( 4, 5 )
		AND O.OrderTypeId  IN ( 4, 5, 9, 11)
        AND ( ISNULL(Op.BillingState, '') = ''
              OR ISNULL(Op.BillingPostalCode, '') = ''
              OR ISNULL(Op.BillingStreetAddress, '') = ''
              OR ISNULL(Op.BillingCity, '') = ''
            )
        AND A.AccountID <> 2
        AND A.Active = 1;

        
		-- CREATING INDEX ON TEMP TABLE 
        CREATE CLUSTERED INDEX cls ON #MissingAddress(OrderPaymentID);
		--1024 Records in Prod.
		
		-- FROM MATCHING STREET NAME TO GET OTHERS FIELDS.
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
                JOIN RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad ON ad.AccountID = m.AccountID
                                                              AND m.BillingStreetAddress = ad.Address1
                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments(NOLOCK) os ON os.OrderCustomerID = oc.OrderCustomerID
                                                            AND os.Address1 = m.BillingStreetAddress
        WHERE   ISNULL(m.BillingStreetAddress, '') <> ''
                AND ( COALESCE(ad.State, os.State, '') <> ''
                      OR COALESCE(ad.PostalCode, os.PostCode, '') <> ''
                      OR COALESCE( os.Address1,ad.Address1, '') <> ''
                      OR COALESCE(ad.City,os.City, '') <> ''
					    OR COALESCE( os.city,ad.City, '') <> ''
                      OR  COALESCE(ad.Address1, os.Address1, '') <> ''
                    )

	--298,963in STG2



 
 -- MATCHING POSTALCODE WITH GET MISSING CITY AND STATE.
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
                JOIN RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.BillingPostalCode,5) = LEFT(ad.PostalCode,5)
                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments(NOLOCK) os ON os.OrderCustomerID = oc.OrderCustomerID
                                                            AND LEFT(os.PostCode,5) = LEFT(m.BillingPostalCode,5)
        WHERE  ISNULL(m.BillingPostalCode, '') <> ''
                AND ( COALESCE(ad.State, os.State, '') <> ''
                      OR COALESCE(ad.PostalCode, os.PostCode, '') <> ''
                      OR COALESCE( os.Address1,ad.Address1, '') <> ''
                      OR COALESCE(ad.City,os.City, '') <> ''
					    OR COALESCE( os.city,ad.City, '') <> ''
                      OR  COALESCE(ad.Address1, os.Address1, '') <> ''
                    )

	

	
		
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
                JOIN RodanFieldsLive.dbo.AccountAddresses(NOLOCK) ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.BillingPostalCode,5) = LEFT(ad.PostalCode,5)
															  AND m.BillingCity=ad.City
                JOIN RodanFieldsLive.dbo.OrderCustomers(NOLOCK) oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments(NOLOCK) os ON os.OrderCustomerID = oc.OrderCustomerID
                                                            AND LEFT(os.PostCode,5) = LEFT(m.BillingPostalCode,5)
															AND m.BillingCity=os.City
        WHERE  ISNULL(m.BillingCity, '') <> ''
                AND ( COALESCE(ad.State, os.State, '') <> ''
                      OR COALESCE(ad.PostalCode, os.PostCode, '') <> ''
                      OR COALESCE( os.Address1,ad.Address1, '') <> ''
                      OR COALESCE(ad.City,os.City, '') <> ''
					    OR COALESCE( os.city,ad.City, '') <> ''
                      OR  COALESCE(ad.Address1, os.Address1, '') <> ''
                    )


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
FROM    RFOperations.Hybris.Autoship(NOLOCK) a
        JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) ac ON a.AccountID = ac.AccountID
        JOIN RFOperations.Hybris.AutoshipPaymentAddress(NOLOCK) ap ON ap.AutoShipID = a.AutoshipID
        JOIN RodanFieldsLive.dbo.OrderPayments(NOLOCK) op ON op.OrderID = a.AutoshipID
                                                     AND op.OrderPaymentID = ap.AutoshipPaymentAddressID
WHERE   a.CountryID = 236
        AND a.Active = 1
        AND ac.Active = 1
        AND ac.AccountID <> 2      
        AND ( ( ISNULL(ap.Locale, '') = ''
                AND ISNULL(op.BillingCity, '') <> ''
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

-- 34 with this Issues not to cover all in STG2.


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
FROM    RFOperations.Hybris.Orders(NOLOCK) O
        JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) ac ON O.AccountID = ac.AccountID
        JOIN RFOperations.Hybris.OrderBillingAddress(NOLOCK) ap ON ap.OrderID = O.OrderID
        JOIN RodanFieldsLive.dbo.OrderPayments(NOLOCK) op ON op.OrderID = O.OrderID
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

	
	--39 with this Issues in STG2.
	
	
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
FROM    RFOperations.Hybris.ReturnOrder(NOLOCK) O
        JOIN RodanFieldsLive.dbo.Accounts(NOLOCK) ac ON O.AccountID = ac.AccountID
        JOIN RFOperations.Hybris.ReturnBillingAddress(NOLOCK) ap ON ap.ReturnOrderID = O.ReturnOrderID
        JOIN RodanFieldsLive.dbo.OrderPayments(NOLOCK) op ON op.OrderID = O.ReturnOrderID
                                                     AND op.OrderPaymentID = ap.ReturnBillingAddressID
WHERE   1 = 1
        AND O.CompletionDate >= '2016-01-01'      
        AND O.CountryID = 236
        AND ac.Active = 1
        AND ac.AccountID <> 2     
        AND ( ( ISNULL(ap.Locale, '') = ''
                AND ISNULL(op.BillingCity, '') <> ''
              )
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