USE [DM_QA]
GO
/****** Object:  StoredProcedure [dbqa].[uspAutoshipHeader_PULSE_BULK]    Script Date: 3/16/2017 9:24:59 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER PROCEDURE [dbqa].[uspAutoshipHeader_PULSE_BULK]
AS
    BEGIN

		
--+++++++++++++++++++++++++++++++++++++++++
-- LOADING TEMP TABLES SOURCE AND TARGET
--+++++++++++++++++++++++++++++++++++++++++



        IF OBJECT_ID('TEMPDB.dbo.#RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#Hybris') IS NOT NULL
            DROP TABLE #Hybris;
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing



        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'Data Migration' ,
            @owner NVARCHAR(50)= 'PULSE_Autoship' ,
            @Key NVARCHAR(25)= 'AutoshipNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts' 

        SET NOCOUNT ON 

        SET @message = CONCAT('STEP: 1. RFO Data is  Loading.', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  a.AutoshipID ,
                MAX(AutoshipPaymentID) AutoshipPaymentID ,
                MAX(apa.AutoshipPaymentAddressID) AutoshipPaymentAddressID
        INTO    #AutoshipPayment
        FROM    RFOperations_02022017.Hybris.Autoship a
                JOIN RFOperations_02022017.Hybris.AutoshipPayment p ON p.AutoshipID = a.AutoshipID
                JOIN RFOperations_02022017.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = a.AutoshipID
        WHERE   a.Active = 1 AND ISNULL(p.Token,'')<>''
                AND a.AutoshipTypeID = 3
        GROUP BY a.AutoshipID

        CREATE CLUSTERED INDEX cls_id ON #AutoshipPayment(AutoshipID)


        SELECT   a.AccountID [RFOKey] ,
                a.ConsultantID ,
                a.ServerModifiedDate ,
                a.AutoshipNumber  ,
                a.StartDate ,
                a.EndDate ,
                CASE a.Active
                  WHEN 1 THEN 'ACTIVE'
                  ELSE 'INACTIVE'
                END AS Active ,
                co.Alpha2Code AS CountryID ,
                p.SKU AS ProductID ,
                a.NextRunDate ,
                a.SubTotal ,
                IIF(a.Total=0,24.95,a.Total) Total,
                0 Qv ,
                0 CV ,
                apa.Token AS TokenID ,
                apa.Expmonth ,
                apa.ExpYear ,
                CASE v.Name
                  WHEN 'mastercard' THEN 'master'
                  ELSE v.name
                END AS VendorID ,
                atpa.FirstName ,
                atpa.LastName ,
                atpa.Address1 ,
                atpa.AddressLine2 ,
                atpa.Locale ,
                atpa.PostalCode ,
                atpa.Region ,
                ROW_NUMBER() OVER ( PARTITION BY AccountID, AutoshipTypeID ORDER BY a.ServerModifiedDate DESC, apa.ServerModifiedDate DESC ) AS RN
        INTO    #RFO
        FROM    RFOperations_02022017.Hybris.Autoship a
                JOIN #AutoshipPayment m ON m.AutoshipID = a.AutoshipID
                JOIN RFOperations_02022017.Hybris.AutoshipPaymentAddress atpa ON atpa.AutoShipID = a.AutoshipID
                                                              AND m.AutoshipPaymentAddressID = atpa.AutoshipPaymentAddressID
                JOIN RFOperations_02022017.Hybris.AutoshipPayment apa ON apa.AutoshipID = a.AutoshipID
                                                              AND apa.AutoshipPaymentID = m.AutoshipPaymentID
                JOIN RFOperations_02022017.Hybris.AutoshipItem ai ON ai.AutoshipId = a.AutoshipID
                LEFT JOIN RFOperations_02022017.RFO_Reference.Countries co ON co.CountryID = a.CountryID
                LEFT JOIN RFOperations_02022017.Hybris.ProductBase p ON p.productID = ai.ProductID
                LEFT JOIN RFOperations_02022017.RFO_Reference.CreditCardVendors v ON v.VendorID = apa.VendorID
        WHERE   a.AutoshipTypeID = 3
                AND Active = 1
                AND EXISTS ( SELECT 1
                             FROM   Hybris.dbo.users u
                                    LEFT JOIN Hybris.dbo.enumerationvalues v ON v.pk = u.p_accountstatus
                                                              AND v.Code = 'Active'
                             WHERE  u.p_customerid = a.accountId )
                AND NOT EXISTS ( SELECT 1
                                 FROM   RFOperations_02022017.Hybris.Autoship at
                                        JOIN RFOperations_02022017.RFO_Accounts.AccountBase b ON b.AccountID = at.AccountID
                                                             
                                 WHERE  at.AutoshipID = a.AutoshipID
                                        AND at.AutoshipTypeID = 3
                                                   AND b.AccountTypeID <> 1
                                                 )
                                                      
			   
			
        DELETE  FROM #RFO
        WHERE   RN > 1  --- Deleting Duplicate active tempates.
              


	
        SET @message = CONCAT('STEP: 2. Hybris Data is  Loading.', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

        SELECT   s.p_customerid [HybrisKey] ,
                su.p_customerid AS p_ordersponsorid ,
                s.modifiedTS ,
                s.p_id AS p_code,
                s.p_startdate ,
                s.p_enddate ,
                p_subscriptionstatus p_active ,
                co.p_isocode p_country ,
                s.p_productcode p_product ,
                s.p_nextbillingdate p_nextrundate ,
                o.p_subtotal ,
                o.p_totalprice ,
                o.p_cv ,
                o.p_totalsv ,
                pif.p_number AS p_cctoken ,
                pif.p_validtomonth ,
                pif.p_validtoyear ,
                ct.Code AS p_type ,
                pad.p_firstname ,
                pad.p_lastname ,
                pad.p_streetname ,
                pad.p_streetnumber ,
                pad.p_town ,
                pad.p_postalcode ,
                rp.p_isocodeshort AS p_region
        INTO    #Hybris
        FROM    Hybris.dbo.users u
                JOIN Hybris.dbo.subscriptions s ON u.p_customerid = s.p_customerid
                JOIN Hybris.dbo.orders o ON o.pk = s.p_order
                LEFT JOIN Hybris.dbo.countries co ON co.pk = u.p_country
                LEFT JOIN Hybris.dbo.paymentinfos pif ON pif.OwnerPkString = o.pk
                LEFT JOIN Hybris.dbo.enumerationvalues ct ON ct.pk = pif.p_type
                LEFT JOIN Hybris.dbo.addresses pad ON pad.OwnerPkString = o.pk
                                                      AND pad.p_billingaddress = 1
                LEFT JOIN Hybris.dbo.users su ON su.pk = o.p_consultantdetails
                LEFT JOIN Hybris.dbo.regions rp ON rp.pk = pad.p_region
                LEFT JOIN Hybris.dbo.enumerationvalues act ON act.pk = u.p_type
                LEFT JOIN Hybris.dbo.enumerationvalues acs ON acs.pk = u.p_accountstatus
        WHERE   act.Code = 'Consultant'
                AND acs.Code = 'Active'

           


        SET @message = CONCAT(' STEP:3.Creating Indexes in temps', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message  
		
		
        CREATE CLUSTERED INDEX cls_RFO ON #RFO(RFOKey)
        CREATE CLUSTERED INDEX cls_Hybris ON  #Hybris(HybrisKey)

	


--++++++++++++++++++++++++++++++++++++
-- TOTAL COUNT VALIDATION
--++++++++++++++++++++++++++++++++++++

        SET @message = CONCAT('STEP: 4.initiating COUNT Validation  ',
                              CHAR(10),
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
                  @owner , -- Owner - nvarchar(50)
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
-- RFO Autoship Duplicate Vaidation.
--++++++++++++++++++--++++++++++++++++++

        SET @message = CONCAT(' STEP: 5.initiating DUPLICATE Need to updaete Scripts ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Duplicate'

        SELECT  @SourceCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([RFOKey]) Ct
                  FROM      #RFO
                  GROUP BY  [RFOKey]
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
                      @owner , -- Owner - nvarchar(50)
                      @SourceCount , -- SourceCount - int         
                      CONCAT('RFO has Duplicate ', @key) ,
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
                      @owner , -- Owner - nvarchar(50)
                      CONCAT('RFO has NO Duplicate ', @key) ,
                      'PASSED'
                    )

		



--++++++++++++++++++--+++++++++++++++++++++
--USA HYBRIS Autoship DUPLICATE VAIDATION.
--++++++++++++++++++--+++++++++++++++++++++

   

        SELECT  @TargetCount = COUNT(t.Ct)
        FROM    ( SELECT    COUNT([HybrisKey]) Ct
                  FROM      #Hybris
                  GROUP BY  [HybrisKey]
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
                      @owner , -- Owner - nvarchar(50)
                      @TargetCount , -- SourceCount - int         
                      CONCAT('Hybris has Duplicate ', @key) ,
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
                      @owner , -- Owner - nvarchar(50)
                      CONCAT('Hybris  has NO Duplicate ', @key) ,
                      'PASSED'
                    )

		



--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++

        SET @message = CONCAT('STEP: 6.initiating MISSING Validation ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

--US Autoship SCTOTG  MISSING VALIDATION 
        SELECT  a.[RFOKey] ,
                b.[HybrisKey] ,
                CASE WHEN a.[RFOKey] IS NULL
                     THEN 'MissingInRFO-LoadedExtraInHybris'
                     WHEN b.[HybrisKey] IS NULL
                     THEN 'MissingInHybris-NotLoadedInHybris'
                END AS [Missing From ]
        INTO    #Missing
        FROM    #RFO a
                FULL OUTER JOIN #Hybris b ON a.[RFOKey] = b.[HybrisKey]
        WHERE   a.[RFOKey] IS NULL
                OR b.[HybrisKey] IS NULL

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
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
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
                        [RFOKey] , -- SourceValue - nvarchar(50)
                        [HybrisKey]
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
                        [RFOKey] , -- SourceValue - nvarchar(50)
                        [HybrisKey]
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
      
		


--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
		
      	





        SET @message = CONCAT('STEP: 7.Removing Issues for END TO END Validaion',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

        DELETE  a
        FROM    #RFO a
                JOIN #missing m ON m.RFOkey = a.RFOKey


        DELETE  a
        FROM    #Hybris a
                JOIN #missing m ON m.HybrisKey = a.HybrisKey	



        DROP TABLE #missing




        SET @sourceCount = 0
        SELECT  @sourceCount = COUNT(RFOKey)
        FROM    #RFO a
                JOIN #Hybris b ON a.RFOKey = b.HybrisKey

        SET @message = CONCAT(' STEP: 8. Total Record Counts for End to End = ',
                              CAST(@SourceCount AS NVARCHAR(25)), CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message



  
        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp			
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'Autoship_Pulse'


		
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
                SET @Message = CONCAT('STEP: 9. Validation Started For Columnt To Column with  total fields= ',
                                      CAST(@MaxRow AS NVARCHAR(20)), CHAR(10),
                                      '-----------------------------------------------')
                   
                EXECUTE dbqa.uspPrintMessage @message


                SET @RowNumber = 1
                WHILE ( @MaxRow >= @RowNumber )
                    BEGIN
                        SELECT  --@Owner = [owner] ,
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
                                EXECUTE dbqa.uspPrintMessage @message

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
                        PRINT @Stmt
                        SET @RowNumber = @RowNumber + 1

                    END 



            END
    END 


