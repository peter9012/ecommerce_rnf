SELECT  a.AccountID ,
        a.AutoshipID ,
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
              OR ISNULL(asd.PostalCode, '') = ''
            );





---FINAL LIST OF ADDRESS ISSUES FOR AUTOSHIP TEMPLATES.

SELECT  A.AccountId ,
        OS.OrderShipmentId ,
		o.OrderID AS [Templates],
        O.OrderTypeId ,
        OS.City ,
        OS.State ,
        OS.PostCode ,
        OS.Address1 ,
        OS.DayPhone  --INTO  RFOperations.Hybris.Template_AddressIssues
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
            )
        AND A.Active = 1
        AND A.Accountid <> 2;

		SELECT COUNT(*) FROM RFOperations.Hybris.Template_AddressIssues --2081 Records in STG2.


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
		) 	
		AND A.Active = 1
		AND A.Accountid <> 2; 
		
		
		--3521 Records in STG2.

		SELECT * FROM RFOperations.Hybris.Orders_AddressIssues

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
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND m.Address1 = ad.Address1
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
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

	--298,963in STG2



 
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
                AND ( COALESCE(ad.State, p.BillingState, '') <> ''
                      OR COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                      OR COALESCE(ad.City, p.BillingCity, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    ) AND m.Address1= COALESCE(ad.Address1, p.BillingStreetAddress, '')


	
		
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
                AND ( COALESCE(ad.State, p.BillingState, '') <> ''
                      OR COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                      OR COALESCE(ad.City, p.BillingCity, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    );

	--2663 Records in STG2.


	
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
                )
            AND A.AccountID <> 2
            AND A.Active = 1;

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
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND m.Address1 = ad.Address1
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderPayments p ON p.OrderCustomerID = oc.OrderCustomerID
                                                            AND p.BillingStreetAddress = m.Address1
        WHERE   m.OrderTypeID IN ( 4, 5 )		
                AND ISNULL(m.Address1, '') <> ''
                AND ( COALESCE(ad.State, p.BillingState, '') <> ''
                      OR COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                      OR COALESCE(ad.City, p.BillingCity, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    );

	--298,963in STG2



 
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
                AND ( COALESCE(ad.State, p.BillingState, '') <> ''
                      OR COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                      OR COALESCE(ad.City, p.BillingCity, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    );


	
		
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
                AND ( COALESCE(ad.State, p.BillingState, '') <> ''
                      OR COALESCE(ad.PostalCode, p.BillingPostalCode, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                      OR COALESCE(ad.City, p.BillingCity, '') <> ''
                      OR COALESCE(ad.Address1, p.BillingStreetAddress, '') <> ''
                    );

	--2663 Records in STG2.




	
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

-- 31 with this Issues not to cover all in STG2.


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
        AND O.OrderStatusID IN ( 2, 4, 5 )
        AND O.CountryID = 236
        AND ac.Active = 1
        AND ac.AccountID <> 2       
        AND ( ( ISNULL(ap.Locale, '') = ''
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

	
	
	
---===================================================
-- RFL TO RFO ETL FLOW VALIDATION FOR RECENT RETURNS.  
--====================================================
			
SELECT  O.ReturnOrderID ,
        ac.Active [Account Active],
        ac.AccountID ,
        op.OrderShipmentID ,
        ap.ReturnBillingAddressID ,
        op.City ,
        ap.Locale ,
        op.Address1 ,
        ap.AddressLine1 ,
        op.State ,
        ap.Region ,
        op.PostCode [RFL PostalCode] ,
        ap.PostalCode ,
        ap.ServerModifiedDate
FROM    RFOperations.Hybris.ReturnOrder O
        JOIN RodanFieldsLive.dbo.Accounts ac ON O.AccountID = ac.AccountID
        JOIN RFOperations.Hybris.ReturnBillingAddress ap ON ap.ReturnOrderID = O.ReturnOrderID
         JOIN RodanFieldsLive.dbo.OrderShipments op ON op.OrderID = O.OrderID
                                                     AND op.OrderShipmentID = ap.ReturnBillingAddressID
WHERE    1 = 1
        AND O.CompletionDate >= '2016-01-01'     
        AND O.CountryID = 236
        AND ac.Active = 1
        AND ac.AccountID <> 2       
        AND ( ( ISNULL(ap.Locale, '') = ''
                AND ISNULL(op.City, '') <> ''
              )
              OR ( ISNULL(ap.Region, '') = ''
                   AND ISNULL(op.State, '') <> ''
                 )
              OR ( ISNULL(ap.AddressLine1, '') = ''
                   AND ISNULL(op.Address1, '') <> ''
                 )
              OR ( ISNULL(ap.PostalCode, '') = ''
                   AND ISNULL(op.PostCode, '') <> ''
                 )
            );

	-- NONE OF THESE ISSUES IN STG2.