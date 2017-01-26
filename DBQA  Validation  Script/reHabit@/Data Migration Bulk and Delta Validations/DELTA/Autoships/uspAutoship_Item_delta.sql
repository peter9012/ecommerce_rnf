
USE DM_QA
GO 
CREATE PROCEDURE dbqa.uspAutoship_Item_delta
    (
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
    BEGIN

--*******************************************************************************************************
--					AUTOSHIP ITEMS VALIDATIONS
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
            @Delta NVARCHAR(150) ,
            @Flows NVARCHAR(50) ,
            @owner NVARCHAR(50)= 'Autoship_Item' ,
            @Key NVARCHAR(25)= 'AutoshipNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts'
        SET @EndDate = ISNULL(@EndDate, GETDATE())
        SET @Delta = CONCAT('_BETWEEN_', FORMAT(@StartDate, 'MMdd'), '_AND_',
                            FORMAT(@EndDate, 'MMdd'))
        SET @Flows = CONCAT('DM_Delta_', @Delta)


        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  a.AutoshipNumber AS RFOKey , --	Ownerpkstring
                LineItemNo - 1 AS LineItemNo , --	p_entrynumber
                p.SKU AS ProductID , --	p_product
                ai.Quantity , --	p_quantity
                ai.BasePrice , --	p_baseprice
                ai.TotalPrice , --	p_totalprice
                ai.TotalTax , --	p_taxvaluesinternal
                ai.CV , --	p_cv
                ai.QV , --	p_qv
                ai.ServerModifiedDate	 --	modifiedTS
        INTO    #RFO
        FROM    RFOperations.Hybris.AutoshipItem ai
                JOIN RFOperations.Hybris.Autoship a ON a.AutoshipID = ai.AutoshipId
                LEFT JOIN RFOperations.Hybris.ProductBase p ON p.productID = ai.ProductID
        WHERE   EXISTS ( SELECT 1
                         FROM   RFOperations.Hybris.AutoshipShippingAddress asp
                                JOIN RFOperations.Hybris.AutoshipPayment ap ON ap.AutoshipID = asp.AutoShipID
                                JOIN RFOperations.Hybris.AutoshipPaymentAddress apa ON apa.AutoShipID = asp.AutoShipID
                         WHERE  asp.AutoShipID = a.AutoshipID )
                AND a.Active = 1
                AND ai.ServerModifiedDate BETWEEN @StartDate
                                          AND     @EndDate


        SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
        EXECUTE dbqa.uspPrintMessage @message
	
        SELECT  c.p_code AS [HybrisKey] , --	AutoshipId
                p_entrynumber , --	LineItemNo
                p.p_code AS p_product , --	ProductID
                CONVERT(INT, p_quantity) p_quantity , --	Quantity
                CONVERT(MONEY, p_baseprice) p_baseprice , --	BasePrice
                CONVERT(MONEY, ce.p_totalprice) p_totalprice , --	TotalPrice
                p_taxvaluesinternal , --	TotalTax
                p_cv , --	CV
                p_qv , --	QV
                ce.modifiedTS  --	ServerModifiedDate     
        INTO    #Hybris
        FROM    Hybris.dbo.cartentries ce
                JOIN Hybris.dbo.carts c ON c.pk = ce.p_order
                JOIN Hybris.dbo.enumerationvalues ct ON ct.pk = c.p_carttype
                                                        AND ct.Code = 'AUTOSHIP_CART'
                JOIN Hybris.dbo.cronjobs cj ON cj.p_cart = c.PK
                JOIN Hybris.dbo.composedtypes jt ON jt.pk = cj.TypePkString
                                                    AND jt.InternalCode = 'CartToOrderCronJob'
                JOIN Hybris.dbo.triggerscj tcj ON tcj.p_cronjob = cj.PK
                JOIN Hybris.dbo.products p ON p.pk = ce.p_product
        WHERE   ce.modifiedTS BETWEEN @StartDate AND @EndDate

			

			
        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message   
			




        CREATE CLUSTERED INDEX cls_RFO ON #RFOAutoshipItem([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #HybrisAutoshipItem([HybrisKey])
		
		-- Count Validaiton 

        SET @message = ' STEP: 4.initiating COUNT Validation  '
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  @SourceCount = COUNT(RFOKey)
        FROM    #RFO 
          
        SELECT  @TargetCount = COUNT(HybrisKey)
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

        IF OBJECT_ID('Tempdb.dbo.#DupRFO') IS NOT NULL
            DROP TABLE #DupRFO
        IF OBJECT_ID('Tempdb.dbo.#DupHybris') IS NOT NULL
            DROP TABLE #DupHybris

			
        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @validationType = 'Duplicate'

        SELECT  RFOKey ,
                'RFO' AS [SourceFrom]
        INTO    #DupRFO
        FROM    #RFOAutoshipItem
        GROUP BY RFOKey ,
                LineItem ,
                ProductID
        HAVING  COUNT(RFOKey) > 1


        SELECT  HybrisKey ,
                'Hybris' AS [SourceFrom]
        INTO    #DupHybris
        FROM    #HybrisAutoshipItem
        GROUP BY HybrisKey ,
                p_entrynumber ,
                p_product
        HAVING  COUNT(HybrisKey) > 1
		
        SELECT  @SourceCount = COUNT(RFOKey)
        FROM    #DupRFO
        WHERE   SourceFrom = 'RFO'
        SELECT  @TargetCount = COUNT(HybrisKey)
        FROM    #DupHybris
        WHERE   SourceFrom = 'Hybris'

        INSERT  INTO dbqa.SourceTargetLog
                ( FlowTypes, ValidationTypes, [Owner], SourceCount,
                  TargetCount, Comments, ExecutionStatus )
        VALUES  ( @flows, -- FlowTypes - nvarchar(50)
                  @validationType, -- ValidationTypes - nvarchar(50)
                  @Owner, -- Owner - nvarchar(50)
                  @SourceCount, -- SourceCount - int
                  @TargetCount, -- TargetCounts - int
                  CASE WHEN @SourceCount >= 1
                       THEN 'RFO Autoship Items Duplicates'
                       ELSE 'RFO Autoship No duplicate'
                  END, CASE WHEN @SourceCount >= 1 THEN 'Fail'
                            ELSE 'Passed'
                       END ),
                ( @flows, -- FlowTypes - nvarchar(50)
                  @validationType, -- ValidationTypes - nvarchar(50)
                  @Owner, -- Owner - nvarchar(50)
                  @SourceCount, -- SourceCount - int
                  @TargetCount, -- TargetCounts - int
                  CASE WHEN @TargetCount >= 1
                       THEN 'Hybris Autoship Items Duplicates'
                       ELSE 'Hydris Autoship No duplicate'
                  END, CASE WHEN @TargetCount >= 1 THEN 'Fail'
                            ELSE 'Passed'
                       END )


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
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #DupRFO
                UNION ALL
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        @ValidationType , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetCoulumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFOKey , -- SourceValue - nvarchar(50)
                        HybrisKey
                FROM    #DupRFO                 
                

	
		
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
                     THEN 'AutoshipItem Not Loaded in hybris'
                     WHEN a.RFC < b.HBC
                     THEN 'AutoshipItem Extra Loaded in hybris'
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
                        @Key , -- Key - nvarchar(50)
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
        FROM    #RFO a
                JOIN #DupRFO m ON m.RFOkey = a.RFOKey


        DELETE  a
        FROM    #Hybris a
                JOIN #missing m ON m.HybrisKey = a.HybrisKey	

        DELETE  a
        FROM    #Hybris a
                JOIN #MixMatch m ON m.HybrisKey = a.HybrisKey	
        DELETE  a
        FROM    #Hybris a
                JOIN #DupHybris m ON m.HybrisKey = a.HybrisKey	

        DROP TABLE #missing
        DROP TABLE #MixMatch
        DROP TABLE #DupRFO
        DROP TABLE #DupHybris



        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp
			
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'AutoshipItem'
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