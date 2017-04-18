USE [DM_QA]
GO
 


CREATE PROCEDURE [dbqa].[uspRFO_Comm_Order_Delta]
    (
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
    BEGIN

		
--+++++++++++++++++++++++++++++++++++++++++
-- LOADING TEMP TABLES SOURCE AND TARGET
--+++++++++++++++++++++++++++++++++++++++++



        IF OBJECT_ID('TEMPDB.dbo.#Source') IS NOT NULL
            DROP TABLE  #Source;
        IF OBJECT_ID('TEMPDB.dbo.#Target') IS NOT NULL
            DROP TABLE #Target;
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing

        SET @EndDate = ISNULL(@EndDate, GETDATE() - 1)

        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'RFO_Comn_Delta' ,
            @owner NVARCHAR(50)= 'Comm_Orders' ,
            @Key NVARCHAR(25)= 'OrderNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts'

        SET NOCOUNT ON 

        SET @message = CONCAT('STEP: 1. Source Data is  Loading.', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message



        SELECT  ro.OrderID [SourceKey] ,
                ro.OrderStatusID AS r_OrderStatusID ,
                ro.OrderTypeID AS r_OrderTypeID ,
                cu.Name AS r_CurrencyID ,
                ro.AccountID AS r_AccountID ,
                ro.ConsultantID AS r_ConsultantID ,
                ro.CompletionDate AS r_CompletionDate ,
                ro.CommissionDate AS r_CommissionDate ,
                201702 AS r_PeriodID ,
                ro.CV r_TotalCV ,
                ro.QV r_TotalQV ,
                co.Alpha3Code AS r_CountryCode ,
                ro.CompletionDate AS r_CreatedOn ,
                ro.ModifiedDate r_UpdatedOn ,
                GETDATE() AS r_ServerModifiedDate ,
                5 r_CancellationReasonID ,
                ab.AccountNumber AS r_AccountNumber ,
                sp.AccountNumber AS r_OrderSponsorAccountNumber
        INTO    #Source
        FROM    RFOperations.hybris.orders ro
                JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = ro.AccountID
                JOIN RFOperations.RFO_Accounts.accountbase sp ON sp.AccountID = ro.ConsultantID
                LEFT JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
                LEFT JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ro.CountryID
                LEFT JOIN RFOperations.RFO_Reference.Currency cu ON cu.CurrencyID = ro.CurrencyID
        WHERE   ro.ServerModifiedDate BETWEEN @StartDate
                                      AND     @EndDate

      
    
		
	
        SET @message = CONCAT('STEP: 2. Target Data is  Loading.', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  [OrderID] [TargetKey] ,
                [OrderStatusID] ,
                [OrderTypeID] ,
                [CurrencyCode] ,
                [AccountID] ,
                [ConsultantID] ,
                [CompleteDate] ,
                [CommissionDate] ,
                [PeriodID] ,
                [TotalCV] ,
                [TotalQV] ,
                [CountryCode] ,
                [CreatedOn] ,
                [UpdatedOn] ,
                [ServerModifiedDate] ,
                [CancellationReasonID] ,
                [AccountNumber] ,
                [OrderSponsorAccountNumber]
        INTO    #Target
        FROM    [Commissions].[etl].[Orders]
        WHERE   UpdatedOn BETWEEN @StartDate AND @EndDate





        SET @message = CONCAT(' STEP:3.Creating Indexes in temps', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message  
		
		
        CREATE CLUSTERED INDEX cls_Source ON #Source(SourceKey)
        CREATE CLUSTERED INDEX cls_Target ON  #Target(TargetKey)

	


--++++++++++++++++++++++++++++++++++++
-- TOTAL COUNT VALIDATION
--++++++++++++++++++++++++++++++++++++

        SET @message = CONCAT('STEP: 4.initiating COUNT Validation  ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  @SourceCount = COUNT(DISTINCT [SourceKey])
        FROM    #Source
        SELECT  @TargetCount = COUNT(DISTINCT [TargetKey])
        FROM    #Target

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
                       THEN CONCAT('Source Count More than Target BY ',
                                   CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
                       WHEN @SourceCount < @TargetCount
                       THEN CONCAT('Target Count More than Source Count By ',
                                   CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
                       ELSE 'Source and Target Counts are equal'
                  END ,
                  CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
                       ELSE 'FAILED'
                  END
                )
		


--++++++++++++++++++--++++++++++++++++++
-- Source  Duplicate Vaidation.
--++++++++++++++++++--++++++++++++++++++

        SET @message = CONCAT(' STEP: 5. DUPLICATE Validation', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Duplicate'


		
        DECLARE @DUP TABLE
            (
              [key] NVARCHAR(225) ,
              SourceFrom NVARCHAR(225)
            );

        INSERT  INTO @DUP
                ( [key] ,
                  SourceFrom
                )
                SELECT  SourceKey ,
                        'Source' AS [SourceFrom]
                FROM    #Source
                GROUP BY SourceKey
                HAVING  COUNT(SourceKey) > 1

        INSERT  INTO @DUP
                ( [key] ,
                  SourceFrom
                )
                SELECT  TargetKey ,
                        'Target' AS [SourceFrom]
                FROM    #Target
                GROUP BY TargetKey
                HAVING  COUNT(TargetKey) > 1
		
        SELECT  @SourceCount = COUNT([key])
        FROM    @DUP
        WHERE   SourceFrom = 'Source'
        SELECT  @TargetCount = COUNT([key])
        FROM    @DUP
        WHERE   SourceFrom = 'Target'

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
                  CASE WHEN @SourceCount >= 1
                            AND @TargetCount >= 1
                       THEN 'Source and Target has  Duplicates'
                       WHEN @SourceCount >= 1 THEN 'Source  has  Duplicates'
                       WHEN @TargetCount >= 1 THEN 'Target has  Duplicates'
                       ELSE 'Both has No duplicate'
                  END ,
                  CASE WHEN @SourceCount >= 1
                            OR @TargetCount >= 1 THEN 'Fail'
                       ELSE 'Passed'
                  END
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
                        @Owner , -- Owner - nvarchar(50)
                        @ValidationType , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetColumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [key] , -- SourceValue - nvarchar(50)
                        NULL
                FROM    @DUP
                WHERE   SourceFrom = 'Source'
                UNION ALL
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        @ValidationType , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetColumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        NULL , -- SourceValue - nvarchar(50)
                        [key]
                FROM    @DUP
                WHERE   SourceFrom = 'Target'         
                
				


--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++

        SET @message = CONCAT('STEP: 6.  MISSING Validation ', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

--US Autoship SCTOTG  MISSING VALIDATION 
        SELECT  a.[SourceKey] ,
                b.[TargetKey] ,
                CASE WHEN a.[SourceKey] IS NULL
                     THEN 'MissingInSource-LoadedExtraInTarget'
                     WHEN b.[TargetKey] IS NULL
                     THEN 'MissingInTarget-NotLoadedInTarget'
                END AS [Missing From ]
        INTO    #Missing
        FROM    #Source a
                FULL OUTER JOIN #Target b ON a.[SourceKey] = b.[TargetKey]
        WHERE   a.[SourceKey] IS NULL
                OR b.[TargetKey] IS NULL

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Missing'

        SELECT  @SourceCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInSource-LoadedExtraInTarget'
        SELECT  @TargetCount = COUNT(*)
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInTarget-NotLoadedInTarget'
	
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
                      CONCAT('Source Missing Count=',
                             CAST(@SourceCount AS NVARCHAR(10)),
                             ' and Target Counts=',
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
                      'No Missing from Source and Target' ,
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
                        [SourceKey] , -- SourceValue - nvarchar(50)
                        [TargetKey]
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInSource-LoadedExtraInTarget'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @Owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetColumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [SourceKey] , -- SourceValue - nvarchar(50)
                        [TargetKey]
                FROM    #Missing
                WHERE   [Missing From ] = 'MissingInTarget-NotLoadedInTarget'
      
		


--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
		
      	





        SET @message = CONCAT('STEP: 7.Removing Issues for END TO END Validaion',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

        DELETE  a
        FROM    #Source a
                JOIN #missing m ON m.SourceKey = a.SourceKey


        DELETE  a
        FROM    #Target a
                JOIN #missing m ON m.TargetKey = a.TargetKey	


        DELETE  a
        FROM    #Source a
                JOIN @Dup m ON m.[key] = a.SourceKey
        DELETE  a
        FROM    #Target a
                JOIN @Dup m ON m.[key] = a.Targetkey

        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing  

        DELETE  @Dup;

        SET @sourceCount = 0
        SELECT  @sourceCount = COUNT(SourceKey)
        FROM    #Source a
                JOIN #Target b ON a.SourceKey = b.TargetKey

        SET @message = CONCAT(' STEP: 8. Total Record Counts for End to End = ',
                              CAST(@SourceCount AS NVARCHAR(25)), CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message



  
        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp			
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    DM_QA.dbqa.Map_tab
        WHERE   [Owner] = 'RFO_comm_Orders'
                AND [flag] IN ( 'c2c', 'ref', 'default' )


		
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
                        SELECT  @Flag = [flag] ,
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
               -- PRINT @Stmt
                        SET @RowNumber = @RowNumber + 1

                    END 



            END
    END 