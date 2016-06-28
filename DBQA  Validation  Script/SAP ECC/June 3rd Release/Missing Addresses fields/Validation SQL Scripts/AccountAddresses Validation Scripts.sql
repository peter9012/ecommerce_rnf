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
  --1452 RECORDS IN STG2.



        SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #ADDRESSES
        WHERE   AddressTypeID = 1
                AND IsDefault = 1; --1360 RECORDS IN STG2.



    SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #ADDRESSES
        WHERE  City='NULL' --22 RECORDS IN STG2.



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
  --1352 RECORDS IN STG2.



        SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #RFOADDRESSES
        WHERE   AddressTypeID = 1
                AND IsDefault = 1; --1292 RECORDS IN STG2.



    SELECT  COUNT(*) [TOTAL COUNTS]
        FROM    #RFOADDRESSES
        WHERE  Locale='NULL' --25 RECORDS IN STG2.

		




--================================================
-- MISSING COUNT BY FIELDS BEFORE AND AFTER UPDATES 
--=================================================

--Loading Issues Addresses NOW to a Temp Table 

IF OBJECT_ID(N'Tempdb..#Temp') IS NOT NULL
DROP TABLE #Temp
IF OBJECT_ID(N'Tempdb..#TempAll') IS NOT NULL
DROP TABLE #TempAll

IF OBJECT_ID(N'Tempdb..#Result') IS NOT NULL
DROP TABLE #Result

SELECT DISTINCT
		A.AccountId,A.Active, ad.AccountAddressId,ad.AddressTypeID,t.Name AS [Types],ad.IsDefault,
		ad.City, ad.[State], ad.PostalCode, ad.Address1, ad.PhoneNumber INTO #Temp
		
	FROM 
		RodanFieldsLive.dbo.Accounts A JOIN
		RodanFieldsLive.dbo.AccountAddresses ad ON A.AccountId = ad.AccountId
		JOIN RodanFieldsLive.dbo.AddressType t ON t.AddressTypeID=ad.AddressTypeID	
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
		AND A.Active=1		
		AND (ad.AddressTypeID=1 OR ad.IsDefault=1)-- We are Only considering MainAddress Types and Default.

		SELECT *  INTO #TempAll
		 FROM #Temp 
		WHERE Active=1
		AND (AddressTypeID=1 OR IsDefault=1)




        DECLARE @TotalBefore INT ,
            @TotalBeforeActive INT ,
            @TotalAfter INT ,
            @TotalAfterActive INT; 

        SET @TotalBefore = ( SELECT COUNT(*)
                             FROM   RFOperations.Hybris.AccountAddress_MissingFields
                           );
        SET @TotalAfter = ( SELECT  COUNT(*)
                            FROM    #Temp
                          );
        SET @TotalBeforeActive = ( SELECT   COUNT(*)
                                   FROM     RFOperations.Hybris.AccountAddress_MissingFields a
                                            JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountAddressID = a.AccountAddressID
                                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = ad.AccountID
                                   WHERE    ac.AccountID <> 2
                                            AND ac.Active = 1
                                            AND ( ad.AddressTypeID = 1
                                                  OR ad.IsDefault = 1
                                                )
                                 );
        SET @TotalAfterActive = ( SELECT    COUNT(*)
                                  FROM      #TempAll
                                  WHERE  AccountID <> 2
                                            AND Active = 1
                                            AND ( AddressTypeID = 1
                                                  OR IsDefault = 1
                                                )
                                );

        SELECT  'All Fields ' AS [Field Types ] ,
                @TotalBefore [Total Before Count] ,
				 @TotalAfter AS [Total After] ,
                @TotalBeforeActive AS [Total Before Active MainNDefault] ,               
                @TotalAfterActive AS [Total After Active ]
        INTO    #Result;


		        SET @TotalBefore = ( SELECT COUNT(*)
                             FROM   RFOperations.Hybris.AccountAddress_MissingFields
                             WHERE  ISNULL(Address1, '') = ''
                           );
        SET @TotalAfter = ( SELECT  COUNT(*)
                            FROM    #Temp
                            WHERE   ISNULL(Address1, '') = ''
                          );
        SET @TotalBeforeActive = ( SELECT   COUNT(*)
                                   FROM     RFOperations.Hybris.AccountAddress_MissingFields a
                                            JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountAddressID = a.AccountAddressID
                                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = ad.AccountID
                                   WHERE    ac.AccountID <> 2
                                            AND ac.Active = 1
                                            AND ISNULL(a.Address1, '') = ''
                                            AND ( ad.AddressTypeID = 1
                                                  OR ad.IsDefault = 1
                                                )
                                 );
        SET @TotalAfterActive = ( SELECT    COUNT(*)
                                  FROM      #Temp a
                                  WHERE     a.AccountID <> 2
                                            AND a.Active = 1
                                            AND ISNULL(a.Address1, '') = ''
                                            AND ( a.AddressTypeID = 1
                                                  OR a.IsDefault = 1
                                                )
                                );
        INSERT  INTO #Result
                SELECT  'Street Name ' AS [Fiel[Field Types ] ,
                @TotalBefore [Total Before Count] ,
				 @TotalAfter AS [Total After] ,
                @TotalBeforeActive AS [Total Before Active MainNDefault] ,               
                @TotalAfterActive AS [Total After Active ] 
     

				


        SET @TotalBefore = ( SELECT COUNT(*)
                             FROM   RFOperations.Hybris.AccountAddress_MissingFields
                             WHERE  ISNULL(City, '') = ''
                           );
        SET @TotalAfter = ( SELECT  COUNT(*)
                            FROM    #Temp
                            WHERE   ISNULL(City, '') = ''
                          );
        SET @TotalBeforeActive = ( SELECT   COUNT(*)
                                   FROM     RFOperations.Hybris.AccountAddress_MissingFields a
                                            JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountAddressID = a.AccountAddressID
                                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = ad.AccountID
                                   WHERE    ac.AccountID <> 2
                                            AND ac.Active = 1
                                            AND ISNULL(a.City, '') = ''
                                            AND ( ad.AddressTypeID = 1
                                                  OR ad.IsDefault = 1
                                                )
                                 );
        SET @TotalAfterActive = ( SELECT    COUNT(*)
                                  FROM      #Temp a
                                  WHERE     a.AccountID <> 2
                                            AND a.Active = 1
                                            AND ISNULL(a.City, '') = ''
                                            AND ( a.AddressTypeID = 1
                                                  OR a.IsDefault = 1
                                                )
                                );	
									
									
        INSERT  INTO #Result
                SELECT  'City ' AS [Field Types ] ,
                @TotalBefore [Total Before Count] ,
				 @TotalAfter AS [Total After] ,
                @TotalBeforeActive AS [Total Before Active MainNDefault] ,               
                @TotalAfterActive AS [Total After Active ] 
																	
        SET @TotalBefore = ( SELECT COUNT(*)
                             FROM   RFOperations.Hybris.AccountAddress_MissingFields
                             WHERE  ISNULL([State], '') = ''
                           );
        SET @TotalAfter = ( SELECT  COUNT(*)
                            FROM    #Temp
                            WHERE   ISNULL([State], '') = ''
                          );
        SET @TotalBeforeActive = ( SELECT   COUNT(*)
                                   FROM     RFOperations.Hybris.AccountAddress_MissingFields a
                                            JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountAddressID = a.AccountAddressID
                                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = ad.AccountID
                                   WHERE    ac.AccountID <> 2
                                            AND ac.Active = 1
                                            AND ISNULL(a.[State], '') = ''
                                            AND ( ad.AddressTypeID = 1
                                                  OR ad.IsDefault = 1
                                                )
                                 );
        SET @TotalAfterActive = ( SELECT    COUNT(*)
                                  FROM      #Temp a
                                  WHERE     a.AccountID <> 2
                                            AND a.Active = 1
                                            AND ISNULL(a.[State], '') = ''
                                            AND ( a.AddressTypeID = 1
                                                  OR a.IsDefault = 1
                                                )
                                );		
									 
        INSERT  INTO #Result
                SELECT  'State ' AS [Field Types ] ,
                @TotalBefore [Total Before Count] ,
				 @TotalAfter AS [Total After] ,
                @TotalBeforeActive AS [Total Before Active MainNDefault] ,               
                @TotalAfterActive AS [Total After Active ]; 

        SET @TotalBefore = ( SELECT COUNT(*)
                             FROM   RFOperations.Hybris.AccountAddress_MissingFields
                             WHERE  ISNULL(PostalCode, '') = ''
                           );
        SET @TotalAfter = ( SELECT  COUNT(*)
                            FROM    #Temp
                            WHERE   ISNULL(PostalCode, '') = ''
                          );
        SET @TotalBeforeActive = ( SELECT   COUNT(*)
                                   FROM     RFOperations.Hybris.AccountAddress_MissingFields a
                                            JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountAddressID = a.AccountAddressID
                                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = ad.AccountID
                                   WHERE    ac.AccountID <> 2
                                            AND ac.Active = 1
                                            AND ISNULL(a.PostalCode, '') = ''
                                            AND ( ad.AddressTypeID = 1
                                                  OR ad.IsDefault = 1
                                                )
                                 );
        SET @TotalAfterActive = ( SELECT    COUNT(*)
                                  FROM      #Temp a
                                  WHERE     a.AccountID <> 2
                                            AND a.Active = 1
                                            AND ISNULL(a.PostalCode, '') = ''
                                            AND ( a.AddressTypeID = 1
                                                  OR a.IsDefault = 1
                                                )
                                );

        INSERT  INTO #Result
                SELECT  'PostalCode' AS [Field Types ] ,
                @TotalBefore [Total Before Count] ,
				 @TotalAfter AS [Total After] ,
                @TotalBeforeActive AS [Total Before Active MainNDefault] ,               
                @TotalAfterActive AS [Total After Active ]; 
        SELECT  *
        FROM    #Result;





---FINAL EXCEPTATION LISTS FOR ADDRESSES ISSUES:

SELECT DISTINCT
		A.AccountId, ad.AccountAddressId,t.Name AS [Types],ad.IsDefault,
		ad.City, ad.[State], ad.PostalCode, ad.Address1, ad.PhoneNumber 
		
	FROM 
		RodanFieldsLive.dbo.Accounts A JOIN
		RodanFieldsLive.dbo.AccountAddresses ad ON A.AccountId = ad.AccountId
		JOIN RodanFieldsLive.dbo.AddressType t ON t.AddressTypeID=ad.AddressTypeID	
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
		AND A.Active=1		
		AND (ad.AddressTypeID=1 OR ad.IsDefault=1)-- We are Only considering MainAddress Types and Default.

		

-- COMPLETELY JUNK DATA.

				
 SELECT a.*
 FROM   #Temp a
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
FROM    #Temp
WHERE   AccountID <> 2
        AND Active = 1
        AND ( AddressTypeID = 1
              OR IsDefault = 1
            )
        AND ISNULL(Address1, '') <> ''
        AND ISNULL(PostalCode, '') <> '';






		--Vlidation IF RFO still not having the Value updated in RFL

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
		RodanFieldsLive.dbo.Accounts A JOIN
		RodanFieldsLive.dbo.AccountAddresses ad ON A.AccountId = ad.AccountId
		JOIN RodanFieldsLive.dbo.AddressType t ON t.AddressTypeID=ad.AddressTypeID	
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

		
	
		;
        WITH    MissingFields
                  AS ( SELECT   ac.AccountID ,
                                ad.AccountAddressID ,
                                ad.City ,
                                ad.[State] ,
                                ad.PostalCode ,
                                ad.Address1 ,
                                ad.PhoneNumber
                       FROM     RodanFieldsLive.dbo.Accounts ac
                                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ac.AccountID = ad.AccountID
                       WHERE    1 = 1
                                AND ( ISNULL(ad.[State], '') = ''
                                      OR ISNULL(ad.PostalCode, '') = ''
                                      OR ISNULL(ad.Address1, '') = ''
                                      OR ISNULL(ad.City, '') = ''
                                    )
                                AND ac.AccountID <> 2
								AND ac.Active=1
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
            FROM    RodanFieldsLive.dbo.Accounts a
                    JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
                    JOIN MissingFields t ON t.AccountID = ad.AccountID
                                            AND ad.Address1 = t.Address1
                    JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = t.AccountID
                    JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
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
                       FROM     RodanFieldsLive.dbo.Accounts ac
                                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ac.AccountID = ad.AccountID
                       WHERE    1 = 1
                                AND ( ISNULL(ad.[State], '') = ''
                                      OR ISNULL(ad.PostalCode, '') = ''
                                      OR ISNULL(ad.Address1, '') = ''
                                      OR ISNULL(ad.City, '') = ''
                                    )
                                AND ac.AccountID <> 2
								AND ac.Active=1
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
FROM    RodanFieldsLive.dbo.Accounts a
        JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
        JOIN MissingFields t ON t.AccountID = ad.AccountID
                                                              AND ad.PostalCode = t.PostalCode
		JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = t.AccountID
		JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID=oc.OrderCustomerID
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
                       FROM     RodanFieldsLive.dbo.Accounts ac
                                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ac.AccountID = ad.AccountID
                       WHERE    1 = 1
                                AND ( ISNULL(ad.[State], '') = ''
                                      OR ISNULL(ad.PostalCode, '') = ''
                                      OR ISNULL(ad.Address1, '') = ''
                                      OR ISNULL(ad.City, '') = ''
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
FROM    RodanFieldsLive.dbo.Accounts a
        JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
        JOIN MissingFields t ON t.AccountID = ad.AccountID
                                                              AND ad.City = t.City AND ad.PostalCode LIKE '%'+t.PostalCode+'%'
		JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = t.AccountID
		JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID=oc.OrderCustomerID
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


--##############################################################################
-- Validating from BackUp Table if Missing fields been updated from Address1.
--##############################################################################



		  --Address1 
		    SELECT  DISTINCT
                    a.Address1 AS [BackUp Address1] ,
                    ad.Address1 ,
                    p.BillingStreetAddress ,
                    ad.City ,
                    ad.[State] ,
                    ad.PostalCode
					,a.*
            FROM    RFOperations.Hybris.AccountAddress_MissingFields a
                    JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
                                                              AND ad.Address1 = ad.Address1
                    JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = a.AccountID
                    JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                              AND p.BillingStreetAddress = a.Address1
            WHERE   1 = 1
                    AND ISNULL(a.Address1, '') <> ''
                    AND COALESCE(ad.City, p.BillingCity, '') = ISNULL(ad.City,
                                                              '')
                    AND COALESCE(ad.PostalCode, p.BillingPostalCode, '') = ISNULL(ad.PostalCode,
                                                              '')
                    AND COALESCE(ad.[State], p.BillingState, '') = ISNULL(ad.[State],
                                                              '')
					
                    AND ( ISNULL(a.City, '') = ''
                          OR ISNULL(a.PostalCode, '') = ''                         
						    OR ISNULL(a.[State],'')=''
                        );
			

			--PostalCode
		 SELECT  DISTINCT
                    a.PostalCode AS [BackUp PostalCode] ,
                    ad.PostalCode ,
                    p.BillingPostalCode ,
                    ad.City ,
                    ad.[State] ,
                    ad.PostalCode
					,a.*
            FROM    RFOperations.Hybris.AccountAddress_MissingFields a
                    JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
                                                              AND ad.PostalCode = ad.PostalCode
                    JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = a.AccountID
                    JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                              AND p.BillingPostalCode = a.postalCode
            WHERE   1 = 1
                    AND ISNULL(a.PostalCode, '') <> ''
                    AND COALESCE(ad.City, p.BillingCity, '') = ISNULL(ad.City,
                                                              '')
                    AND COALESCE(ad.Address1, p.BillingStreetAddress, '') = ISNULL(ad.Address1,
                                                              '')
                    AND COALESCE(ad.[State], p.BillingState, '') = ISNULL(ad.[State],
                                                              '')
                    AND ( ISNULL(a.City, '') = ''                         
                          OR ISNULL(a.Address1, '') = ''
						  OR ISNULL(a.[State],'')=''
                        );
			

			--City wit PostalCode
			 SELECT  DISTINCT
                    a.City AS [BackUp City] ,
                    ad.City ,
                    p.BillingCity ,
                    ad.City ,
                    ad.[State] ,
                    ad.PostalCode
					,a.*
            FROM    RFOperations.Hybris.AccountAddress_MissingFields a
                    JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
                                                              AND ad.City = ad.City AND ad.PostalCode LIKE '%'+a.PostalCode+'%'
                    JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = a.AccountID
                    JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                              AND p.BillingPostalCode = a.City AND p.BillingPostalCode LIKE '%'+a.PostalCode+'%'
            WHERE   1 = 1
                    AND ISNULL(a.City, '') <> ''
                    AND COALESCE(ad.PostalCode, p.BillingPostalCode, '') = ISNULL(ad.PostalCode,
                                                              '')
                    AND COALESCE(ad.Address1, p.BillingStreetAddress, '') = ISNULL(ad.Address1,
                                                              '')
                    AND COALESCE(ad.[State], p.BillingState, '') = ISNULL(ad.[State],
                                                              '')
                    AND ( ISNULL(a.[State], '') = ''
                          OR ISNULL(a.PostalCode, '') = ''
                          OR ISNULL(a.Address1, '') = ''
                        );

--=============================================================================================
-- Checking If Matching  Address1 and BillingStreenAddress AccountAddress And BillingAddress
--=============================================================================================



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
		CASE WHEN ISNULL(ad.address1,'')<>'' THEN 'Account Address1'
		WHEN ISNULL(p.BillingStreetAddress,'')<>''THEN 'BillingStreetAddress'
		END AS [SOURCE]
FROM    RodanFieldsLive.dbo.Accounts a
        JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
        JOIN RFOperations.Hybris.AccountAddress_MissingFields t ON t.AccountID = ad.AccountID
                                                              AND ad.Address1 = t.Address1
		JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = t.AccountID
		JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID=oc.OrderCustomerID
		AND p.BillingStreetAddress=t.Address1		
WHERE   1 = 1
        AND a.AccountID <> 2
        AND COALESCE(ad.Address1,p.BillingStreetAddress, '') <> ''
        AND ( ( COALESCE(ad.[State],p.BillingState, '') <> ''
                AND COALESCE(t.[State],p.BillingState, '') = ''
              )
              OR ( COALESCE(ad.PostalCode,p.BillingPostalCode, '') <> ''
                   AND ISNULL(t.PostalCode, '') = ''
                 )
             
              OR ( COALESCE(ad.City,p.BillingCity, '') <> ''
                   AND ISNULL(t.City, '') = ''
                 )
            );


--=============================================================================================
-- Checking If Matching  PostalCode  and BillingPostalCode AccountAddress And BillingAddress.
--=============================================================================================


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
FROM    RodanFieldsLive.dbo.Accounts a
        JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
        JOIN RFOperations.Hybris.AccountAddress_MissingFields t ON t.AccountID = ad.AccountID
                                                              AND ad.PostalCode = t.PostalCode
		JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = t.AccountID
		JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID=oc.OrderCustomerID
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





--=============================================================================================
-- Checking If Matching  BillingCity And Postal Code AccountAddress And BillingAddress
--=============================================================================================


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
FROM    RodanFieldsLive.dbo.Accounts a
        JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = a.AccountID
        JOIN RFOperations.Hybris.AccountAddress_MissingFields t ON t.AccountID = ad.AccountID
                                                              AND ad.City = t.City AND ad.PostalCode LIKE '%'+t.PostalCode+'%'
		JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = t.AccountID
		JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID=oc.OrderCustomerID
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


		