

USE DM_QA
GO
CREATE PROCEDURE dbqa.uspOrderConsignmentNEntry_delta
    (
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
    BEGIN


        IF OBJECT_ID('TEMPDB.dbo. #RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#Hybris') IS NOT NULL
            DROP TABLE #Hybris;
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing
        IF OBJECT_ID('tempdb.dbo.#MixMatch') IS NOT NULL
            DROP TABLE #MixMatch
        IF OBJECT_ID('tempdb.dbo.#DupHybris') IS NOT NULL
            DROP TABLE #DupHybris
        IF OBJECT_ID('tempdb.dbo.#DupRFO') IS NOT NULL
            DROP TABLE #DupRFO


        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Delta NVARCHAR(150) ,
            @Flows NVARCHAR(50) ,
            @owner NVARCHAR(50)= 'Order_Consignment_N_Entry' ,
            @Key NVARCHAR(25)= 'OrderNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts'
        SET @EndDate = ISNULL(@EndDate, GETDATE())
        SET @Delta = CONCAT('_BETWEEN_', FORMAT(@StartDate, 'MMdd'), '_AND_',
                            FORMAT(@EndDate, 'MMdd'))
        SET @Flows = CONCAT('DM_Delta_', @Delta)



        SET NOCOUNT ON 
        SET @message = CONCAT(' STEP: 1.RFOSource Table Started to Load.',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  Quantity ,--	p_shippedquantity
                TrackingNumber ,--	p_trackingid
                ShipDate ,--	p_shippingdate
                ro.OrderNumber AS [RFOKey] ,--	P_code
                st.Name AS ShipStatus ,--	p_status
                sm.Name AS ShippingMethodID ,--	p_deliverymode
                sm.Carrier AS ShippingCarrier --	p_carrier     
        INTO    #RFO
        FROM    RFOperations.Hybris.OrderShipmentPackageItem osp
                JOIN RFOperations.Hybris.OrderShipment os ON os.OrderShipmentID = osp.OrderShipmentID
                JOIN RFOperations.Hybris.orders ro ON ro.OrderID = os.OrderID
                JOIN RFOperations.RFO_Reference.OrderShipmentStatus st ON st.OrderShipmentStatusId = osp.ShipStatus
                JOIN RFOperations.RFO_Reference.ShippingMethod sm ON sm.ShippingMethodID = os.ShippingMethodID
        WHERE   EXISTS ( SELECT 1
                         FROM   Hybris.dbo.orders ho
                                JOIN Hybris.dbo.Composedtypes ct ON ct.pk = ho.typepkstring
                                                              AND ct.internalcode = 'Order' )
                AND osp.ServerModifiedDate BETWEEN @StartDate
                                           AND     @EndDate



        SET @message = CONCAT('STEP: 2. Target Table Started to Load',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  p_quantity ,--	Quantity
                p_shippedquantity ,--	Quantity
                p_trackingid ,--	TrackingNumber
                p_shippingdate ,--	ShipDate
                c.P_code AS [HybrisKey] ,--	OrderID
                v.Code AS p_status ,--	ShipStatus
                m.p_code AS p_deliverymode ,--	ShippingMethodID
                p_carrier	--	ShippingMethodID 
        INTO    #Hybris
        FROM    Hybris.dbo.consignments c
                JOIN Hybris.dbo.consignmententries ce ON ce.p_consignment = c.PK
                JOIN Hybris.dbo.orders ho ON ho.pk = c.p_order
                JOIN Hybris.dbo.composedtypes ct ON ct.pk = ho.TypePkString
                JOIN Hybris.dbo.enumerationvalues v ON v.pk = c.p_status
                JOIN Hybris.dbo.deliverymodes m ON m.pk = c.p_deliverymode
        WHERE   ct.InternalCode = 'Order'
                AND ( ( c.ModifiedTS BETWEEN @StartDate AND @EndDate )
                      OR ( ce.ModifiedTS BETWEEN @StartDate AND @EndDate )
                    )

        SET @message = CONCAT(' STEP:3.Creating Indexes in Temps', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message     
	
        CREATE NONCLUSTERED INDEX cls_RFO ON #RFO(RFOKey)  
        CREATE NONCLUSTERED INDEX cls_Hybris ON  #Hybris(HybrisKey)   



--++++++++++++++++++++++++++++++++++++
-- TOTAL COUNT VALIDATION
--++++++++++++++++++++++++++++++++++++

        SET @message = CONCAT(' STEP: 4.initiating COUNT Validation  ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  @SourceCount = COUNT([RFOKey])
        FROM    #RFO
        SELECT  @TargetCount = COUNT([HybrisKey])
        FROM    #Hybris



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
-- RFO  Duplicate Vaidation.
--++++++++++++++++++--++++++++++++++++++
        SET @message = CONCAT(' STEP: 5.initiating DUPLICATE Need to updaete Scripts and Delete from EndToEnd  ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

--SET @SourceCount = 0
--SET @TargetCount = 0
--SET @ValidationType = 'Duplicate'

--SELECT  RFOKey ,
--        'RFO' AS SourceFrom
--INTO    #DupRFO
--FROM    #RFO
--GROUP BY [RFOKey] ,
--        LineItemNo ,
--        ProductID
--HAVING  COUNT([RFOKey]) > 1
    

--SELECT  @SourceCount = COUNT(t.Ct)
--FROM    #DupRFO
--WHERE   SourceFrom = 'RFO'



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
--              @owner , -- Owner - nvarchar(50)
--              @SourceCount , -- SourceCount - int         
--              CONCAT('RFO has Duplicate', @key) ,
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
--              @owner , -- Owner - nvarchar(50)
--              CONCAT('RFO has NO Duplicate', @key) ,
--              'PASSED'
--            )

		



--++++++++++++++++++--+++++++++++++++++++++
--  HYBRIS   DUPLICATE VAIDATION.
--++++++++++++++++++--+++++++++++++++++++++

--SELECT  [HybrisKey] ,
--        'Hybris' AS SourceFrom
--INTO    #DupHybris
--FROM    #Hybris
--GROUP BY [HybrisKey]
--HAVING  COUNT([HybrisKey]) > 1

--SELECT  @TargetCount = COUNT(t.Ct)
--FROM    #DupHybris
--WHERE   SourceFrom = 'Hybris'



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
--              @owner , -- Owner - nvarchar(50)
--              @TargetCount , -- SourceCount - int         
--              CONCAT('Hybris has Duplicate', @key) ,
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
--              @owner , -- Owner - nvarchar(50)
--              CONCAT('Hybris has No Duplicate', @key) ,
--              'PASSED'
--            )

		



--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++
        SET @message = CONCAT(' STEP: 6.initiating MISSING Validation ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message
 
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
      
			
-- MixNMatch
        SET @message = CONCAT(' STEP: 7.initiating MIXMATCH Validation ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  a.RFOKey ,
                b.[HybrisKey] ,
                RFC ,
                HBC ,
                CASE WHEN a.RFC > b.HBC
                     THEN CONCAT(@owner, '_ Not Loaded in hybris')
                     WHEN a.RFC < b.HBC
                     THEN CONCAT(@owner, '_ Extra Loaded in hybris')
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
                        @Owner , -- Owner - nvarchar(50)
                        @ValidationType , -- Flag - nvarchar(10)
                        RFOKey , -- SourceColumn - nvarchar(50)
                        HybrisKey , -- TargetColumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        RFC , -- SourceValue - nvarchar(50)
                        HBC
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

        IF OBJECT_ID('tempdb.dbo.#DupRFO') IS NOT NULL
            DELETE  a
            FROM    #RFO a
                    JOIN #DupRFO m ON m.RFOkey = a.RFOKey


        IF OBJECT_ID('tempdb.dbo.#DupHybris') IS NOT NULL
            DELETE  a
            FROM    #Hybris a
                    JOIN #DupHybris m ON m.HybrisKey = a.HybrisKey	


        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing
        IF OBJECT_ID('tempdb.dbo.#MixMatch') IS NOT NULL
            DROP TABLE #MixMatch
        IF OBJECT_ID('tempdb.dbo.#DupHybris') IS NOT NULL
            DROP TABLE #DupHybris
        IF OBJECT_ID('tempdb.dbo.#DupRFO') IS NOT NULL
            DROP TABLE #DupRFO
 

        SET @sourceCount = 0
        SELECT  @sourceCount = COUNT(RFOKey)
        FROM    #RFO a
                JOIN #Hybris b ON a.RFOKey = b.HybrisKey

        SET @message = CONCAT(' STEP: 9. Total Record Counts for End to End = ',
                              CAST(@SourceCount AS NVARCHAR(25)), CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp			
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'OrderConsignments'
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
                SET @Message = CONCAT('STEP: 10. Validation Started For Columnt To Column with  total fields= ',
                                      CAST(@MaxRow AS NVARCHAR(20)), CHAR(10),
                                      '-----------------------------------------------')
                   
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