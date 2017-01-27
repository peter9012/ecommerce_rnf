USE DM_QA
GO 
CREATE PROCEDURE dbqa.uspAutoshipPaymentAddresses_delta
    (
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
    BEGIN

--***********************************************************************
--				AUTOSHIPPAYMENTADDRESS
--***********************************************************************




        SET NOCOUNT ON;

        IF OBJECT_ID('TEMPDB.dbo.#RFO') IS NOT NULL
            DROP TABLE #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#Hybris') IS NOT NULL
            DROP TABLE  #Hybris;
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing
        IF OBJECT_ID('tempdb.dbo.#MixMatch') IS NOT NULL
            DROP TABLE #MixMatch  

	

        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Delta NVARCHAR(150) ,
            @Flows NVARCHAR(50) ,
            @owner NVARCHAR(50)= 'Autoship_PaymentAddresses' ,
            @Key NVARCHAR(25)= 'AutoshipNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts'

        SET @EndDate = ISNULL(@EndDate, GETDATE())
        SET @Delta = CONCAT('_BETWEEN_', FORMAT(@StartDate, 'MMdd'), '_AND_',
                            FORMAT(@EndDate, 'MMdd'))
        SET @Flows = CONCAT('DM_Delta_', @Delta)


        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message



        SELECT  a.AutoshipNumber AS [RFOKey] ,
                apa.FirstName , --	p_firstname
                apa.LastName , --	p_lastname
                apa.CountryID , --	p_country
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
        INTO    #RFO
        FROM    RFOperations.Hybris.AutoshipPaymentAddress apa
                JOIN RFOperations.Hybris.Autoship a ON a.AutoshipID = apa.AutoshipId
                LEFT JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = a.AccountID
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
        WHERE   EXISTS ( SELECT 1
                         FROM   RFOperations.Hybris.AutoshipShippingAddress asp
                                JOIN RFOperations.Hybris.AutoshipItem ai ON ai.AutoshipID = asp.AutoShipID
                                JOIN RFOperations.Hybris.AutoshipPayment ap ON ap.AutoShipID = asp.AutoshipID
                         WHERE  asp.AutoShipID = apa.AutoshipID )
                AND EXISTS ( SELECT 1
                             FROM   Hybris.dbo.users u
                             WHERE  u.p_customerid = ac.AccountID )
                AND a.Active = 1
                AND apa.ServerModifiedDate BETWEEN @StartDate
                                           AND     @EndDate 

        SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
        EXECUTE dbqa.uspPrintMessage @message
	

        SELECT  ca.p_code AS [HybrisKey] ,
                p_firstname , --	FirstName
                p_lastname , --	LastName
                co.p_isocode AS p_country , --	CountryID
                p_streetname , --	Address1
                p_streetNumber , --	AddressLine2
                p_postalcode , --	PostalCode
                p_town , --	Locale
                p_region , --	Region
                p_phone1 , --	Telephone
                p_middlename , --	MiddleName
                p_dateofbirth , --	DateOfBirth
                p_gender , --	GenderID
                pa.p_code AS OwnerPkString , --	 
                ad.p_billingaddress , --	 
                ad.p_duplicate	 --	 
        INTO    #Hybris
        FROM    Hybris.dbo.addresses ad
                JOIN Hybris.dbo.paymentinfos pa ON pa.PK = ad.OwnerPkString
                                                   AND ad.p_billingaddress = 1
                                                   AND ad.p_duplicate = 1
                JOIN Hybris.dbo.carts ca ON ca.pk = pa.p_original
                JOIN Hybris.dbo.enumerationvalues ct ON ct.pk = ca.p_carttype
                                                        AND ct.Code = 'AUTOSHIP_CART'
                JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = ca.PK
                JOIN Hybris.dbo.composedtypes jt ON jt.pk = cj.TypePkString
                                                    AND jt.InternalCode = 'CartToOrderCronJob'
                JOIN Hybris.dbo.triggerscj tcj ON tcj.p_cronjob = cj.PK
                LEFT JOIN Hybris.dbo.countries co ON co.pk = ad.p_country
        WHERE   ad.modifiedTS BETWEEN @StartDate AND @EndDate 


        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message  

	
        CREATE CLUSTERED INDEX cls_RFO ON #RFO ([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON   #Hybris ([HybrisKey])
	
		


--++++++++++++++++++++++++++++++++++++++++++++++++++++
--  COUNT, DUPLICATE AND MISSING  VALIDATION
--++++++++++++++++++++++++++++++++++++++++++++++++++++


        SET @message = ' STEP: 4.initiating COUNT Validation  '
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  @SourceCount = COUNT([RFOKey])
        FROM    #RFO 
        SELECT  @TargetCount = COUNT([HybrisKey])
        FROM    #Hybris 

-- INSERTING LOG FOR TOTALCOUNT VALIDATION.

        INSERT  INTO dbqa.SourceTargetLog
                ( FlowTypes ,
                  ValidationTypes ,
                  [Owner] ,
                  SourceCount ,
                  TargetCount ,
                  Comments ,
                  ExecutionStatus 
                )
        VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                  @validationType , -- ValidationTypes - nvarchar(50)
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


        SET @SourceCount = 0
        SET @TargetCount = 0   
        SET @validationType = 'Duplicate'
        SELECT  @SourceCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([RFOKey]) Ct
                  FROM      #RFOAutoshipPaymentAddress
                  GROUP BY  [RFOKey] ,
                            PaymentProfileID
                  HAVING    COUNT([RFOKey]) > 1
                ) t



        IF ISNULL(@SourceCount, 0) > 0
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      SourceCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                      @validationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount , -- SourceCount - int         
                      CONCAT('RFO has Duplicate', @key) ,
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
            VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                      @validationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50) 
                      CONCAT('RFO has no Duplicate', @key) ,
                      'PASSED'
                    )




--++++++++++++++++++--+++++++++++++++++++++
-- HYBRIS DUPLICATE VAIDATION.
--++++++++++++++++++--+++++++++++++++++++++
		
		
	

        SELECT  @TargetCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([HybrisKey]) Ct
                  FROM      #Hybris
                  GROUP BY  [HybrisKey] ,
                            paymentInfoPK
                  HAVING    COUNT([HybrisKey]) > 1
                ) t



        IF ISNULL(@TargetCount, 0) > 0
            INSERT  INTO dbqa.SourceTargetLog
                    ( FlowTypes ,
                      ValidationTypes ,
                      [Owner] ,
                      TargetCount ,
                      Comments ,
                      ExecutionStatus
                    )
            VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                      @validationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @TargetCount , -- SourceCount - int         
                      CONCAT('Hybris has Duplicate', @key) ,
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
            VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                      @validationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      CONCAT('Hybris has no  Duplicate', @key) ,
                      'PASSED'
                    )

		



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
        FROM    #RFO a
                FULL OUTER JOIN #Hybris b ON a.RFOKey = b.HybrisKey
        WHERE   a.RFOKey IS NULL
                OR b.HybrisKey IS NULL 



        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @validationtype = 'Missing'

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
            VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                      @validationType , -- ValidationTypes - nvarchar(50)
                      @owner , -- Owner - nvarchar(50)
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
            VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                      @validationType , -- ValidationTypes - nvarchar(50)
                      @owner , -- Owner - nvarchar(50)
                      'No Missing from RFO and Hybris' ,
                      'PASSED'
                    )

					

-- LOADING ERROR WITH SAMPLE DATA.

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
                        @flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
                UNION
                SELECT TOP 10
                        @flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
			




-- MixNMatch

        SET @message = ' STEP: 7.initiating MIXMATCH Validation '
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  a.RFOKey ,
                b.[HybrisKey] ,
                CASE WHEN a.RFC > b.HBC
                     THEN 'paymentInfos Not Loaded in hybris'
                     WHEN a.RFC < b.HBC
                     THEN 'paymentInfos Extra Loaded in hybris'
                END AS MixMatch
        INTO    #MixMatch
        FROM    ( SELECT    RFOKey ,
                            COUNT(*) AS [RFC]
                  FROM      #RFO
                  GROUP BY  RFOKey
                ) a
                JOIN ( SELECT   HybrisKey ,
                                COUNT(*) AS [HBC]
                       FROM     #Hybris
                       GROUP BY HybrisKey
                     ) b ON a.RFOKey = b.[HybrisKey]
        WHERE   a.RFC <> b.HBC



        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @validationType = 'MixMatch'

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
            VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                      @validationtype , -- ValidationTypes - nvarchar(50)
                      @owner , -- Owner - nvarchar(50)
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
            VALUES  ( @flows , -- FlowTypes - nvarchar(50)
                      @validationtype , -- ValidationTypes - nvarchar(50)
                      @owner , -- Owner - nvarchar(50)
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
                        @flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        @validationtype , -- Flag - nvarchar(10)
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
        WHERE   [Owner] = 'AutoshipPaymentAddress'
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
