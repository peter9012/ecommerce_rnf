--*******************************************************************************************************
--					ORDER PAYMENNTS  VALIDATIONS
--*******************************************************************************************************

USE DM_QA
GO
CREATE PROCEDURE dbqa.upsOrderPayment_BULK
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
    @Flows NVARCHAR(50)= 'Data Migration' ,
    @owner NVARCHAR(50)= 'Order_Payment' ,
    @Key NVARCHAR(25)= 'OrderNumber' ,
    @ValidationType NVARCHAR(50)= 'Counts'

SET NOCOUNT ON 
SET @message = ' STEP: 1.RFOSource Table Started to Load.'
EXECUTE dbqa.uspPrintMessage @message

SELECT  OrderPaymentID ,--	p_code
        ro.OrderNumber AS [RFOKey] ,--	OwnerpkString
        AmountTobeAuthorized ,--	p_plannedAmout
        ExpYear ,--	p_validtoyear
        Expmonth ,--	p_validtomonth
        VendorID ,--	p_type
        paymentprovider ,--	paymentprovider
        op.DisplayNumber AS AccountNumber ,--	p_number
        CONCAT(ob.FirstName, SPACE(1), ob.LastName) BillingName   --	p_ccowner
INTO    #RFO
FROM    RFOperations.Hybris.OrderPayment op
        JOIN RFOperations.Hybris.orders ro ON ro.OrderID = op.OrderID
        JOIN RFOperations.Hybris.OrderBillingAddress ob ON ob.OrderID = ro.OrderID
WHERE   EXISTS ( SELECT 1
                 FROM   Hybris.dbo.orders ho
                        JOIN Hybris.dbo.composedtypes c ON c.pk = ho.TypepkString
                                                           AND c.InternalCode = 'ReturnOrder'-->>>>>>Please Upddate properly.
                 WHERE  ho.p_code = ro.OrderNumber )

SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
EXECUTE dbqa.uspPrintMessage @message


SELECT  pa.p_code ,--	OrderPaymentID
        ho.p_code AS [HybrisKey] ,--	 
        pt.p_plannedAmount ,--	AmountTobeAuthorized
        p_validtoyear ,--	ExpYear
        p_validtomonth ,--	Expmonth
        v.Code AS p_type ,--	VendorID
--paymentprovider	,--	paymentprovider
        p_number ,--	AccountNumber
        p_ccowner	--	BillingFirstName,LastName
INTO    #Hybris
FROM    Hybris.dbo.paymentinfos pa
        JOIN Hybris.dbo.Orders ho ON ho.pk = pa.OwnerPkString
                                     AND pa.p_user = ho.p_user
                                     AND pa.p_duplicate = 1
        JOIN Hybris.dbo.paymenttransactions pt ON pt.p_info = pa.pk
        JOIN Hybris.dbo.enumerationvalues v ON v.pk = pa.p_type

SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
EXECUTE dbqa.uspPrintMessage @message       
		
CREATE CLUSTERED INDEX cls_RFO ON #RFO(RFOKey)
CREATE CLUSTERED INDEX cls_Hybris ON  #Hybris(HybrisKey)



--++++++++++++++++++++++++++++++++++++
-- TOTAL COUNT VALIDATION
--++++++++++++++++++++++++++++++++++++

SET @message = ' STEP: 4.initiating COUNT Validation  '
EXECUTE dbqa.uspPrintMessage @message

SELECT  @SourceCount = COUNT(DISTINCT [RFOKey])
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
               THEN CONCAT('RFO Count More than Target BY ',
                           CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
               WHEN @SourceCount < @TargetCount
               THEN CONCAT('Target Count More than RFO Count By ',
                           CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
               ELSE 'Source and Target Counts are equal'
          END ,
          CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
               ELSE 'FAILED'
          END
        )
		


--++++++++++++++++++--++++++++++++++++++
-- RFO  Duplicate Vaidation.
--++++++++++++++++++--++++++++++++++++++
SET @message = ' STEP: 5.initiating DUPLICATE Need to updaete Scripts   '
EXECUTE dbqa.uspPrintMessage @message

SET @SourceCount = 0
SET @TargetCount = 0
SET @ValidationType = 'Duplicate'

SELECT  RFOKey ,
        'RFO' AS SourceFrom
INTO    #DupRFO
FROM    #RFO
GROUP BY [RFOKey] ,
        paymentprofileID ,
        AmountTobeAuthorized
HAVING  COUNT([RFOKey]) > 1
    

SELECT  @SourceCount = COUNT(t.Ct)
FROM    #DupRFO
WHERE   SourceFrom = 'RFO'



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
              CONCAT('Source has Duplicate', @key) ,
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
              CONCAT('Source has NO Duplicate', @key) ,
              'PASSED'
            )

		



--++++++++++++++++++--+++++++++++++++++++++
-- HYBRIS  DUPLICATE VAIDATION.
--++++++++++++++++++--+++++++++++++++++++++

SELECT  [HybrisKey] ,
        'Hybris' AS SourceFrom
INTO    #DupHybris
FROM    #Hybris
GROUP BY [HybrisKey] ,
        p_code ,
        p_plannedamount
HAVING  COUNT([HybrisKey]) > 1

SELECT  @TargetCount = COUNT(t.Ct)
FROM    #DupHybris
WHERE   SourceFrom = 'Hybris'



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
              CONCAT('Target has Duplicate', @key) ,
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
              CONCAT('Target has No Duplicate', @key) ,
              'PASSED'
            )

		



--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++
SET @message = ' STEP: 6.initiating MISSING Validation now '
EXECUTE dbqa.uspPrintMessage @message


SELECT  a.[RFOKey] ,
        b.[HybrisKey] ,
        CASE WHEN a.[RFOKey] IS NULL THEN 'MissingInSource'
             WHEN b.[HybrisKey] IS NULL
             THEN 'MissingInTarget'
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
WHERE   [Missing From ] = 'MissingInSource'
SELECT  @TargetCount = COUNT(*)
FROM    #Missing
WHERE   [Missing From ] = 'MissingInTarget'
	
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
              CONCAT('RFO Missing Count=', CAST(@SourceCount AS NVARCHAR(10)),
                     ' and Hybris Counts=', CAST(@TargetCount AS NVARCHAR(10))) ,
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
              'No Missing from source to target' ,
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
                [Missing From ] , -- Flag - nvarchar(10)
                N'' , -- SourceColumn - nvarchar(50)
                N'' , -- TargetCoulumn - nvarchar(50)
                @Key , -- Key - nvarchar(50)
                [RFOKey] , -- SourceValue - nvarchar(50)
                [HybrisKey]
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInSource'
        UNION
        SELECT TOP 10
                @Flows , -- FlowTypes - nvarchar(50)
                @Owner , -- Owner - nvarchar(50)
                [Missing From ] , -- Flag - nvarchar(10)
                N'' , -- SourceColumn - nvarchar(50)
                N'' , -- TargetCoulumn - nvarchar(50)
                @Key , -- Key - nvarchar(50)
                [RFOKey] , -- SourceValue - nvarchar(50)
                [HybrisKey]
        FROM    #Missing
        WHERE   [Missing From ] = 'MissingInTarget'
      
			
-- Default Validtion in Target Attributes




-- MixNMatch



SET @message = ' STEP: 7.initiating MIXMATCH Validation '
EXECUTE dbqa.uspPrintMessage @message

SELECT  a.RFOKey ,
        b.[HybrisKey] ,
        RFC ,
        HBC ,
        CASE WHEN a.RFC > b.HBC THEN 'AutoshipItem Not Loaded in hybris'
             WHEN a.RFC < b.HBC THEN 'AutoshipItem Extra Loaded in hybris'
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
              CONCAT('RFO MixMatch Count=', CAST(@SourceCount AS NVARCHAR(10)),
                     ' and Target Counts=', CAST(@TargetCount AS NVARCHAR(10))) ,
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
WHERE   [Owner] = 'OrderPayment'
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
                                      CAST(@RowNumber AS NVARCHAR(20)), '. ',
                                      @TargetColumn)
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
                                  CONCAT(@SourceColumn, ' Vs ', @TargetColumn) , -- Defference - nvarchar(100)
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
                                  CONCAT(@SourceColumn, ' Vs ', @TargetColumn) , -- Defference - nvarchar(100)
                                  'PASSED'
                                )
								

                    END 

                DELETE  @temp
               -- PRINT @Stmt
                SET @RowNumber = @RowNumber + 1

            END 



    END
	END  
