
USE DM_QA
GO 
CREATE  PROCEDURE dbqa.uspAccountPaymentProfiles_delta
    (
      @LoadDate DATE ,
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
    BEGIN

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
            @Delta NVARCHAR(50) ,
            @Flows NVARCHAR(50) ,
            @HardTermDate DATE ,
            @OrdersDate DATETIME ,
            @owner NVARCHAR(50)= 'AccountPaymentProfiles' ,
            @Key NVARCHAR(25)= 'AccountNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts'	

        SET @EndDate = ISNULL(@EndDate, GETDATE())  
        SET @Delta = CONCAT(FORMAT(@startDate, 'MMdd'), '-> ',
                            FORMAT(@EndDate, 'MMdd'))
        SET @Flows = CONCAT('DM Delta_', @Delta)
        SET @HardTermDate = DATEADD(MONTH, -6, @LoadDate)
        SET @OrdersDate = DATEADD(MONTH, -18, @LoadDate)


        SET @message = CONCAT('STEP: 1.RFOSource Table Started to Load.',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


	
	
        SELECT  pp.PaymentProfileID ,--PaymentProfileID_Code
                b.AccountNumber AS [p_customerNumber] ,
                CAST(b.AccountID AS NVARCHAR(225)) AS RFOKey ,
                LTRIM(RTRIM(ccp.NameOnCard)) ProfileName , --	p_ccowner
                DisplayNumber , --	p_number	
                pp.IsDefault AS IsDefaultPayInfo ,
              --  apm.AccountNumber AS RFLCardNumber ,			 
                v.Name AS VendorID , --	p_type
                ExpMonth , --	p_validtomonth
                ExpYear , --	p_validtoyear
                ccp.BillingAddressID , --	p_billingaddress
                SUBSTRING(REVERSE(LTRIM(RTRIM(CCP.DisplayNumber))), 1, 4) AS LastFourNumber , --	p_lastfourdigit
                0 AS cctoken	 --	p_cctoken     
        INTO    #RFO
        FROM    RFOperations.RFO_Accounts.AccountBase b
                JOIN RFOperations.RFO_Accounts.PaymentProfiles (NOLOCK) PP ON b.AccountID = PP.AccountID
                --JOIN RodanFieldsLive.dbo.AccountPaymentMethods apm ON apm.AccountID = b.AccountID
                --                                              AND apm.AccountPaymentMethodID = PP.PaymentProfileID
                JOIN RFOperations.RFO_Accounts.CreditCardProfiles (NOLOCK) CCP ON PP.PaymentProfileID = CCP.PaymentProfileID
                JOIN RFOperations.RFO_Reference.CreditCardVendors (NOLOCK) V ON V.VendorID = CCP.VendorID
                JOIN Hybris.dbo.users hu ON CAST(PP.AccountID AS VARCHAR(255)) = hu.p_customerid
        WHERE   EXISTS ( SELECT 1
                         FROM   RFoperations.RFo_Accounts.Addresses ad
                         WHERE  ad.AddressID = ccp.BillingAddressID )
            /* EXISTS ( SELECT 1
                         FROM   RFOperations.RFO_Accounts.AccountBase ab
                                JOIN RFOperations.RFO_Accounts.AccountRF rf ON ab.AccountID = rf.AccountID
                                JOIN RFOperations.RFO_Reference.AccountContactType t ON t.AccountContactTypeID = ab.AccountTypeID
                         WHERE  ab.accountID = b.AcccountID
                                AND ea.EmailAddressTypeID = 1
                                AND ab.AccountNumber <> 'AnonymousRetailAccount'
                                AND ( ab.AccountStatusID IN ( 1, 2, 8, 9 ) -- Active,Inactive,SoftVoluntary,SoftInvoluntary
                                      OR ( ab.AccountStatusID = 10
                                           AND EXISTS ( SELECT
                                                              1
                                                        FROM  RFOperations.Hybris.orders ro
                                                        WHERE ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= '2016-08-28' ) --HardTerm with 6 months order
                                         )
                                      OR ( ab.AccountStatusID IN ( 3, 4, 5, 6,
                                                              7 )
                                           AND EXISTS ( SELECT
                                                              1
                                                        FROM  RFOperations.Hybris.orders ro
                                                        WHERE ro.AccountID = ab.AccountID
                                                              AND CAST(ro.CompletionDate AS DATE) >= '2015-08-28' )--Other Status with 18 months orders
                                         )
                                    ) ) */
                AND ( ( pp.ServerModifiedDate BETWEEN @StartDate
                                              AND     @EndDate )
                      OR ( CCP.ServerModifiedDate BETWEEN @StartDate
                                                  AND     @EndDate )
                    )

/*
SELECT  *
FROM    RFOperations.RFO_Accounts.Addresses a
        RIGHT JOIN RFOperations.RFO_Accounts.CreditCardProfiles b ON a.AddressID = b.BillingAddressID
WHERE   a.AddressID IS NULL  

*/



        SET @message = CONCAT(' STEP: 2. Target Table Loading.', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message



	

        SELECT  p.pk AS paymentInfoPK ,
                u.p_customerid AS HybrisKey ,
                p_ccowner , --	ProfileName
                p_number , --	DisplayNumber
                v.Code AS p_type , --	VendorID
                p_validtomonth , --	ExpMonth
                p_validtoyear , --	ExpYear
                p.p_billingaddress , --	BillingAddressID
                p_lastfourdigit , --	LastFourNumber
                ISNULL(p_cctoken, 0) p_cctoken , --	cctoken
                p.p_original ,
                p.OwnerPkString ,
                CASE WHEN u.p_defaultpaymentinfo = p.pk THEN 1
                     ELSE 0
                END AS IsDefaultInfo
        INTO    #Hybris
        FROM    Hybris.dbo.users (NOLOCK) u
                JOIN Hybris.dbo.paymentinfos (NOLOCK) p ON p.p_user = u.PK
                LEFT JOIN Hybris.dbo.enumerationvalues v ON v.pk = p.p_type
                LEFT JOIN Hybris.dbo.addresses (NOLOCK) a ON a.PK = p.p_billingaddress
                                                             AND a.p_duplicate = 0
        WHERE   p.p_duplicate = 0
                AND u.p_uid <> 'anonymous'
                AND ( u.modifiedTS BETWEEN @StartDate AND @EndDate )
                OR ( p.modifiedTS BETWEEN @StartDate AND @EndDate ) 
               







        SET @message = CONCAT(' STEP:3.Creating Index in Temp tables',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

		

        CREATE CLUSTERED INDEX cls_RFO ON  #RFO([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #Hybris([HybrisKey])

/* Source to Target Total Counts */


--++++++++++++++++++++++++++++++++++++
--  Count Validation
--++++++++++++++++++++++++++++++++++++

     




        SET @message = CONCAT(' STEP: 4. COUNT Validation Started ', CHAR(10),
                              '-----------------------------------------------')
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
		




        SET @message = CONCAT('STEP: 5. DUPLICATE Validation Started ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

--++++++++++++++++++--++++++++++++++++++
--  RFO  DUPLICATE VALIDATION
--++++++++++++++++++--++++++++++++++++++

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Duplicate'


        SELECT  @SourceCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([RFOKey]) Ct
                  FROM      #RFO
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
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @SourceCount , -- SourceCount - int         
                      'RFO has Duplicate PaymentInfo' ,
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
                      'RFO has NO Duplicate PaymentInfos' ,
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
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @Owner , -- Owner - nvarchar(50)
                      @TargetCount , -- SourceCount - int         
                      'HYBRIS has Duplicate PaymentInfo' ,
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
                      'HYBRIS has NO Duplicate PaymentInfos' ,
                      'PASSED'
                    )

		



--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++

        SET @message = CONCAT('STEP: 6. MISSING Validation Started ', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


--US ACCOUNTS SCTOTG  MISSING VALIDATION 

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

					

-- LOADING ERROR WITH SAMPLE DATA.

        INSERT  INTO dbqa.ErrorLog
                ( FlowTypes ,
                  Owner ,
                  Flag ,
                  SourceColumn ,
                  TargetColumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetColumn - nvarchar(50)
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
                        N'' , -- TargetColumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
			




-- MixNMatch

        SET @message = CONCAT(' STEP: 7. MIXMATCH Validation Started',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  a.RFOKey ,
                b.[HybrisKey] ,
                RFC ,
                HBC ,
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
                  TargetColumn ,
                  [Key] ,
                  SourceValue ,
                  TargetValue
                )
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @ValidationType , -- ValidationTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        RFOKey , -- SourceColumn - nvarchar(50)
                        HybrisKey , -- TargetColumn - nvarchar(50)
                        @key , -- Key - nvarchar(50)
                        ISNULL(RFC, 0) , -- SourceValue - nvarchar(50)
                        ISNULL(HBC, 0)
                FROM    #MixMatch




--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
	


        SET @message = CONCAT(' STEP: 8.Removing Issues for END TO END Validaion  ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


        DELETE  a
        FROM    #RFO a
                JOIN #Missing m ON m.RFOKey = a.RFOKey 
		

        DELETE  a
        FROM    #RFO a
                JOIN #MixMatch m ON m.RFOKey = a.RFOKey 				   
						 
			


        DELETE  a
        FROM    #Hybris a
                JOIN #Missing m ON m.HybrisKey = a.HybrisKey 

        DELETE  a
        FROM    #Hybris a
                JOIN #MixMatch m ON m.HybrisKey = a.HybrisKey 

       



       
        DROP TABLE #missing
  -- Droping Table for performace
        DROP TABLE #MixMatch
  -- Droping Table for performace
        SET @SourceCount = ( SELECT COUNT(HybrisKey)
                             FROM   #Hybris a
                                    JOIN #RFO b ON a.HybrisKey = b.RFOKey
                           )
  
      
      
        SET @message = CONCAT(' STEP: 9.Total Records for End to End= ',
                              CAST(@SourceCount AS NVARCHAR(10)), CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp
			
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'AccountPaymentProfiles'
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
                SET @Message = CONCAT('STEP: 9.Validation Started For Columnt To Column with  total fields= '
                                      + CAST(@MaxRow AS NVARCHAR(20)),
                                      CHAR(10),
                                      '-----------------------------------------------')              
                EXECUTE dbqa.uspPrintMessage @message
-- Print Message no wait.

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
                                              '. ', @TargetColumn, CHAR(10),
                                              '-----------------------------------------------')
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
                                                      ' for ', @TargetColumn,
                                                      CHAR(10),
                                                      '-----------------------------------------------')


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
                                          'FAILED'
                                        )

                                INSERT  INTO dbqa.ErrorLog
                                        ( FlowTypes ,
                                          [Owner] ,
                                          Flag ,
                                          SourceColumn ,
                                          TargetColumn ,
                                          [Key] ,
                                          SourceValue ,
                                          TargetValue
                                        )
                                        SELECT TOP 10
                                                @Flows ,
                                                @Owner ,
                                                CONCAT(@Flag, '_EndToEnd') ,
                                                @SourceColumn ,
                                                @TargetColumn ,
                                                CONCAT(@Key, '=', [Key]) ,
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

