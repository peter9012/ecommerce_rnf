

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
            )
        AND A.AccountID <> 2
        AND A.Active = 1
		AND at.Active=1

		-- 5321 records in STG2
		
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
		AND O.OrderTypeId NOT  IN (4,5, 9)
        AND ( ISNULL(Op.BillingState, '') = ''
              OR ISNULL(Op.BillingPostalCode, '') = ''
              OR ISNULL(Op.BillingStreetAddress, '') = ''
              OR ISNULL(Op.BillingCity, '') = ''
            )
        AND A.AccountID <> 2
        AND A.Active = 1;



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
            )
        AND A.AccountID <> 2
        AND A.Active = 1;


		SELECT * FROM RodanFieldsLive.dbo.OrderStatus
		SELECT * FROM RodanFieldsLive.dbo.OrderTypes
		--- 10,803 records in STG2

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
            )
        AND A.AccountID <> 2
        AND A.Active = 1;
        
		-- CREATING INDEX ON TEMP TABLE 
        CREATE CLUSTERED INDEX cls ON #MissingAddress(OrderPaymentID);

		
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
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND m.BillingStreetAddress = ad.Address1
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
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
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.BillingPostalCode,5) = LEFT(ad.PostalCode,5)
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
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
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.BillingPostalCode,5) = LEFT(ad.PostalCode,5)
															  AND m.BillingCity=ad.City
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
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
            )
        AND A.AccountID <> 2
        AND A.Active = 1;

        
		-- CREATING INDEX ON TEMP TABLE 
        CREATE CLUSTERED INDEX cls ON #MissingAddress(OrderPaymentID);

		
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
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND m.BillingStreetAddress = ad.Address1
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
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
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.BillingPostalCode,5) = LEFT(ad.PostalCode,5)
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
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
                JOIN RodanFieldsLive.dbo.AccountAddresses ad ON ad.AccountID = m.AccountID
                                                              AND LEFT(m.BillingPostalCode,5) = LEFT(ad.PostalCode,5)
															  AND m.BillingCity=ad.City
                JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = m.AccountID
                JOIN RodanFieldsLive.dbo.OrderShipments os ON os.OrderCustomerID = oc.OrderCustomerID
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