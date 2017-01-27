


USE DM_QA
GO 

CREATE PROCEDURE dbqa.uspAccountShippingAddress_BULK ( @LoadDate DATE )
AS
    BEGIN

	
--*******************************************************************************************************
--					ACCOUNT SHIPPING ADDRESSES VALIDATIONS
--*******************************************************************************************************
        SET NOCOUNT ON;

        IF OBJECT_ID('TEMPDB.dbo. #RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#Hybris') IS NOT NULL
            DROP TABLE #Hybris;
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing  
        IF OBJECT_ID('tempdb.dbo.#MixMatch') IS NOT NULL
            DROP TABLE #MixMatch  

        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'Data Migration' ,
            @owner NVARCHAR(50)= 'Accounts_ShippingAddress' ,
            @Key NVARCHAR(25)= 'AccountNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts' ,
            @OrderDate DATE= DATEADD(MONTH, -18, @LoadDate) ,
            @HardTermDate DATE= DATEADD(MONTH, -6, @LoadDate)


        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  a.addressID ,
                ac.AccountId AS [RFOKey] ,
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
        INTO    #RFO
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
        WHERE   EXISTS ( SELECT 1
                         FROM   Hybris.dbo.users u
                         WHERE  u.P_accountID = ac.accountId )
	--OR 
          /*     AND EXISTS ( SELECT 1
		   FROM      RFOperations.RFO_Accounts.AccountBase ab
                                                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID --AND ab.AccountNumber='001762'
                                                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID
                                                JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
                                                LEFT JOIN RFOperations.RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId
                                                LEFT JOIN RFOperations.RFO_Accounts.EmailAddresses ea ON ea.EmailAddressID = ae.EmailAddressId
                                       WHERE  ab.accountID = ac.AcccountID
                            AND ea.EmailAddressTypeID = 1
                            AND ab.AccountNumber <> 'AnonymousRetailAccount'
                            AND ( ab.AccountStatusID IN ( 1, 2, 8, 9 ) -- Active,Inactive,SoftVoluntary,SoftInvoluntary
                                  OR ( ab.AccountStatusID = 10
                                       AND EXISTS ( SELECT  1
                                                    FROM    RFOperations.Hybris.orders ro
                                                    WHERE   ro.AccountID = ab.AccountID
                                                            AND CAST(ro.CompletionDate AS DATE) >= @HardTermDate ) --HardTerm with 6 months order
                                     )
                                  OR ( ab.AccountStatusID IN ( 3, 4, 5, 6, 7 )
                                       AND EXISTS ( SELECT  1
                                                    FROM    RFOperations.Hybris.orders ro
                                                    WHERE   ro.AccountID = ab.AccountID
                                                            AND CAST(ro.CompletionDate AS DATE) >= @OrderDate )--Other Status with 18 months orders
                                     )
                                ) )
								*/


			
        SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
        EXECUTE dbqa.uspPrintMessage @message
	



        SELECT  u.p_accountnumber [HybrisKey] ,
                co.p_isocode AS p_counrty , --	CountryID
                r.p_isocodeshort AS p_region , --	region
                p_phone1 , --	MainNumber
                p_phone2 , --	HomeNumber
                p_cellphone , --	CellNumber
                g.Code AS p_gender , --	GenderID
                p_billingaddress , --	IsBillingAddress
                p_contactaddress , --	IsContactAddress
                p_shippingaddress , --	IsShippingID
                p_town , --	Locale
                p_streetname , --	AddressLine1
                p_streetnumber , --	AddressLine2
                p_firstname , --	FirstName
                p_lastname , --	LastName
                p_MiddleName , --	MiddleName
                p_postalcode , --	PostalCode
                a.p_dateofbirth --	BirthDay    
        INTO    #Hybris
        FROM    Hybris.dbo.addresses a
                JOIN Hybris.dbo.users u ON u.pk = a.OwnerPkString
                                           AND a.p_duplicate = 0
                                           AND a.p_shippingaddress = 1
                JOIN Hybris.dbo.countries co ON co.pk = a.p_country
                JOIN Hybris.dbo.regions r ON r.pk = a.p_region
                LEFT JOIN Hybris.dbo.enumerationvalues g ON g.pk = a.p_gender


			

			
					
        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message       


        CREATE CLUSTERED INDEX cls_RFO ON #RFOAddress([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #HybrisAddress([HybrisKey])
		
	-- Count Validaiton 

        SET @message = ' STEP: 4.initiating COUNT Validation  '
        EXECUTE dbqa.uspPrintMessage @message



        SELECT  @SourceCount = COUNT(RFOKey)
        FROM    #RFOAddress
          
        SELECT  @TargetCount = COUNT(HybrisKey)
        FROM    #HybrisAddress
           

        INSERT  INTO dbqa.SourceTargetLog
                ( FlowTypes ,
                  ValidationTypes ,
                  [Owner] ,
                  SourceCount ,
                  TargetCount ,
                  Comments ,
                  ExecutionStatus 
                )
        VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                  @ValidationType , -- ValidationTypes - nvarchar(50)
                  @Owner , -- Owner - nvarchar(50)
                  @SourceCount , -- SourceCount - int
                  @TargetCount , -- TargetCounts - int
                  CASE WHEN @SourceCount > @TargetCount
                       THEN CONCAT('RFO Count More than HYBRIS BY ',
                                   CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
                       WHEN @SourceCount < @TargetCount
                       THEN CONCAT('HYBRIS Count More than RFO Count By ',
                                   CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
                       ELSE 'RFO and HYBRIS Counts are equal'
                  END ,
                  CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
                       ELSE 'FAILED'
                  END
                )
		
		

--++++++++++++++++++--++++++++++++++++++
--  RFO  DUPLICATE VALIDATION
--++++++++++++++++++--++++++++++++++++++
        SET @message = ' STEP: 5.initiating DUPLICATE Need to updaete Scripts   '
        EXECUTE dbqa.uspPrintMessage @message


--SET @SourceCount = 0
--SET @TargetCount = 0
--SET @ValidationType = 'Duplicate'


--SELECT  @SourceCount = COUNT(t.Ct)
--FROM    ( SELECT    COUNT([RFOKey]) Ct
--          FROM      #RFO
--          GROUP BY  [RFOKey] ,
--                    PaymentProfileID
--          HAVING    COUNT([RFOKey]) > 1
--        ) t



--IF ISNULL(@SourceCount, 0) > 0
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              SourceCount ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              @SourceCount , -- SourceCount - int         
--              'RFO has Duplicate PaymentInfo' ,
--              'FAILED'
--            )
--ELSE
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              'RFO has NO Duplicate PaymentInfos' ,
--              'PASSED'
--            )


--SELECT  @TargetCount = COUNT(t.Ct)
--FROM    ( SELECT    COUNT([HybrisKey]) Ct
--          FROM      #Hybris
--          GROUP BY  [HybrisKey] ,
--                    paymentInfoPK
--          HAVING    COUNT([HybrisKey]) > 1
--        ) t



--IF ISNULL(@TargetCount, 0) > 0
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              TargetCount ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              @TargetCount , -- SourceCount - int         
--              'HYBRIS has Duplicate PaymentInfo' ,
--              'FAILED'
--            )

	




--ELSE
--    INSERT  INTO dbqa.SourceTargetLog
--            ( FlowTypes ,
--              ValidationTypes ,
--              [Owner] ,
--              Comments ,
--              ExecutionStatus
--            )
--    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
--              @ValidationType , -- ValidationTypes - nvarchar(50)
--              @Owner , -- Owner - nvarchar(50)
--              'HYBRIS has NO Duplicate PaymentInfos' ,
--              'PASSED'
--            )

--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++


        SET @message = ' STEP: 6.initiating MISSING Validation now '
        EXECUTE dbqa.uspPrintMessage @message
		

		 
    
        SELECT  a.RFOKey ,
                b.HybrisKey ,
                CASE WHEN a.RFOkey IS NULL
                     THEN 'MissingInRFO-LoadedExtraInHybris'
                     WHEN b.HybrisKey IS NULL
                     THEN 'MissingInHybris-NotLoadedInHybris'
                END AS [Missing From ]
        INTO    #Missing
        FROM    #RFOAddress a
                FULL OUTER JOIN #HybrisAddress b ON a.RFOKey = b.HybrisKey
        WHERE   a.RFOKey IS NULL
                OR b.HybrisKey IS NULL
                   
					

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Missing'

        SELECT  @SourceCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
        SELECT  @TargetCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO Missing Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'No Missing from RFO and Hybris' ,
                      'PASSED'
                    )

   -- Loading Sample data
   
        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetCoulumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'



				

-- MixNMatch

        SET @message = ' STEP: 7.initiating MIXMATCH Validation '
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  a.RFOKey ,
                b.[HybrisKey] ,
                RFC ,
                HBC ,
                CASE WHEN a.RFC > b.HBC
                     THEN 'AccShippingAddress Not Loaded in hybris'
                     WHEN a.RFC < b.HBC
                     THEN 'AccShippingAddress Extra Loaded in hybris'
                END AS MixMatch
        INTO    #MixMatch
        FROM    ( SELECT    RFOKey ,
                            COUNT(*) AS [RFC]
                  FROM      #RFOAddress
                  GROUP BY  RFOKey
                ) a
                JOIN ( SELECT   HybrisKey ,
                                COUNT(*) AS [HBC]
                       FROM     #HybrisAddress
                       GROUP BY HybrisKey
                     ) b ON a.RFOKey = b.[HybrisKey]
        WHERE   a.RFC <> b.HBC

		

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'MixMatch'

        SELECT  @SourceCount = COUNT(*)
        FROM    #MixMatch
 
        SELECT  @TargetCount = COUNT(*)
        FROM    #MixMatch

	
        IF ( ISNULL(@TargetCount, 0) > 0
             OR ISNULL(@SourceCount, 0) > 0
           )
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount ,
                      @TargetCount , -- SourceCount - int         
                      CONCAT('RFO MixMatch Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Hybris Counts=',
                             CAST(@TargetCount AS NVARCHAR(10))) ,
                      'FAILED'
                    )
        ELSE
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      'No MixMatch Counts' ,
                      'PASSED'
                    )




   
        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetCoulumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        @ValidationType , -- Flag - nvarchar(10)
                        RFOKey , -- SourceColumn - nvarchar(50)
                        HybrisKey , -- TargetCoulumn - nvarchar(50)
                        @key , -- Key - nvarchar(50)
                        RFC , -- SourceValue - nvarchar(50)
                        HBC
                FROM    #MixMatch





--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
	


        SET @message = ' STEP: 8.Removing Issues for END TO END Validaion  '
        EXECUTE dbqa.uspPrintMessage @message

        DELETE  a
        FROM    #RFO a
                JOIN #missing m ON m.RFOkey = a.RFOKey
		
        DELETE  a
        FROM    #RFO a
                JOIN #MixMatch m ON m.RFOkey = a.RFOKey


        DELETE  a
        FROM    #Hybris a
                JOIN #missing m ON m.HybrisKey = a.HybrisKey	
		

        DELETE  a
        FROM    #Hybris a
                JOIN #MixMatch m ON m.HybrisKey = a.HybrisKey	



        DROP TABLE #missing

        DROP TABLE #MixMatch




        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp
			
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'AccountAddresses'
                AND [flag] IN ( 'c2c', 'ref', 'def' )


		

		
        DECLARE @MaxRow INT ,
            @RowNumber INT ,
            @rowCounts INT= 0 ,
            @TargetColumn NVARCHAR(50) ,
            @SourceColumn NVARCHAR(25) ,
            @flag NVARCHAR(50) ,
            @Stmt NVARCHAR(MAX);
        DECLARE @temp TABLE
            (
              [key] VARCHAR(50) ,--[Key]
              SourceValue VARCHAR(MAX) ,--[SourceColumn]
              TargetValue VARCHAR(MAX) --[TargetColumn] 
            );



        SELECT  @MaxRow = MAX(RowNumber)
        FROM    #Temp
        IF ISNULL(@MaxRow, 0) > 0
            BEGIN
                SET @Message = 'STEP: 9.Validation Started For Columnt To Column with  total fields= '
                    + CAST(@MaxRow AS NVARCHAR(20))              
                EXECUTE dbqa.uspPrintMessage @message


                SET @RowNumber = 1
                WHILE ( @MaxRow >= @RowNumber )
                    BEGIN
                        SELECT  @Owner = [owner] ,
                                @Flag = [flag] ,
                                @key = [Key] ,
                                @TargetColumn = TargetColumn ,
                                @SourceColumn = SourceColumn ,
                                @RowNumber = RowNumber ,
                                @Stmt = [SQL Stmt]
                        FROM    #Temp
                        WHERE   RowNumber = @RowNumber

                        SET @Message = CONCAT('Column Validation Started For ',
                                              CAST(@RowNumber AS NVARCHAR(20)),
                                              '. ', @TargetColumn)
                        EXECUTE dbqa.uspPrintMessage @message

                        INSERT  INTO @temp
                                ( [key], SourceValue,--[SourceColumn]
                                  TargetValue  --[TargetColumn]  
                                  )
                                EXEC sp_executesql @stmt 
                        SELECT  @rowCounts = COUNT([key])
                        FROM    @temp
						
                        IF @rowCounts <> 0
                            BEGIN
                                SET @Message = CONCAT('Total IssueCount=',
                                                      CAST(@rowCounts AS NVARCHAR(12)),
                                                      ' for ', @TargetColumn)


                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Owner , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'FAILED'
                                        )

                                INSERT  INTO dbqa.ErrorLog
                                        ( FlowTypes ,
                                          [Owner] ,
                                          Flag ,
                                          SourceColumn ,
                                          TargetCoulumn ,
                                          [Key] ,
                                          SourceValue ,
                                          TargetValue
                                        )
                                        SELECT TOP 10
                                                @Owner ,
                                                @Owner ,
                                                CONCAT(@Flag, '_EndToEnd') ,
                                                @SourceColumn ,
                                                @TargetColumn ,
                                                @Key ,
                                                SourceValue ,
                                                TargetValue
                                        FROM    @temp
                            END 
                        ELSE
                            BEGIN 
                                INSERT  INTO dbqa.SourceTargetLog
                                        ( FlowTypes ,
                                          ValidationTypes ,
                                          Owner ,
                                          SourceCount ,
                                          TargetCount ,
                                          comments ,
                                          ExecutionStatus
                                        )
                                VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                                          CONCAT(@Flag, '_EndToEnd') , -- ValidationTypes - nvarchar(50)
                                          @Owner , -- Owner - nvarchar(50)
                                          @rowCounts , -- SourceCount - int
                                          @rowCounts , -- TargetCounts - int
                                          CONCAT(@SourceColumn, ' Vs ',
                                                 @TargetColumn) , -- Defference - nvarchar(100)
                                          'PASSED'
                                        )
								

                            END 

                        DELETE  @temp
               -- PRINT @Stmt
                        SET @RowNumber = @RowNumber + 1

                    END 



            END
    END 