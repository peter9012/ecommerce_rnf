--*******************************************************************************************************
--					ORDER HEADER VALIDATION 
--*******************************************************************************************************
USE DM_QA
GO

CREATE PROCEDURE dbqa.uspOrderHeader_BULK ( @LoadDate DATE )
AS
    BEGIN


        IF OBJECT_ID('TEMPDB.dbo. #RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#Hybris') IS NOT NULL
            DROP TABLE #Hybris;
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing



        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'Data Migration' ,
            @owner NVARCHAR(50)= 'Orders' ,
            @Key NVARCHAR(25)= 'OrderNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts'
        SET NOCOUNT ON 


        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  OrderNumber AS [RFOKey] , --	p_code
                AutoShipID , --	p_associatedOrders
                s.Name AS OrderStatusID , --	p_status
                t.Name AS OrderTypeID , --	p_type
                co.Alpha2Code AS CountryID , --	p_country
                cu.Code AS CurrencyID , --	p_currency
                sa.AccountNumber AS ConsultantID , --	p_ordersponsorid /*  This one Converted to AccountNumber*/
                ab.AccountNumber AS AccountID , --	p_userpk  /*  This one Converted to AccountNumber*/
                CompletionDate , --	createdTS
                CommissionDate , --	p_commissiondate
                ro.ModifiedDate , --	modifiedTS
                SubTotal , --	p_subtotal
                Total , --	p_totalprice
                TotalTax , --	p_totaltax
                TotalDiscount , --	p_totaldiscount
                CV , --	p_cv
                QV , --	p_qv
                TaxExempt , --	isTaxExempt
                donotship , --	p_donotship,
                ( os.ShippingCost + os.HandlingCost ) AS ShippingCost , --	p_deliverycost
                ( os.TaxOnShippingCost + os.TaxOnHandlingCost ) AS TaxOnShippingCost  --	p_taxonshippingcost       
        INTO    #RFOOrders
        FROM    RFOperations.Hybris.Orders ro
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = ro.CountryID
                JOIN RFOperations.RFO_Reference.Currency cu ON cu.CurrencyID = ro.CurrencyID
                JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = ro.AccountID
                JOIN RFOperations.RFO_Accounts.AccountBase sa ON sa.AccountID = ConsultantID
                JOIN RFOperations.RFO_Reference.OrderStatus s ON s.OrderStatusId = ro.OrderStatusID
                JOIN RFOperations.RFO_Reference.OrderType t ON t.OrderTypeID = ro.OrderTypeID
                LEFT JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
        WHERE   EXISTS ( SELECT 1
                         FROM   Hybris.dbo.users u
                         WHERE  u.p_customerid = ab.AccountNumber )
                AND CAST(ro.CompletionDate AS DATE) >= @LoadDate
													   


        SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
        EXECUTE dbqa.uspPrintMessage @message
	

        SELECT  o.p_code [HybrisKey] , --	OrderNumber
                c.p_code AS p_associatedOrders , --	AutoShipID Or P_parent ?????????????????
                s.Code AS p_status , --	OrderStatusID
                ct.Code AS p_type , --	OrderTypeID
                co.p_isocode AS p_country , --	CountryID
                cu.p_isocode AS p_currency , --	CurrencyID
                cons.p_customerid AS p_ordersponsorid , --	ConsultantID
                u.p_customerid AS p_userpk , --	AccountID
                o.createdTS , --	CompletionDate
                o.p_commissiondate , --	CommissionDate
                o.modifiedTS , --	ModifiedDate
                o.p_subtotal , --	SubTotal
                o.p_totalprice , --	Total
                o.p_totaltax , --	TotalTax
                o.p_totaldiscounts AS p_totaldiscount , --	TotalDiscount
                o.p_cv , --	CV
                o.p_totalsv AS p_qv , --	QV
                c.p_taxexempt AS isTaxExempt , --	TaxExempt
                o.p_donotship , --	donotship
                o.p_deliverycost , --	ShippingCost
                o.p_taxondeliverycost AS p_taxonshippingcost ,--	TaxOnShippingCost
                o.p_calculated ,--SET True        
                o.p_discountsincludedeliverycost ,-- SET 0.
                o.p_discountsincludepaymentcost ,-- SET 0.
                o.p_fraudulent , -- SET False.
                o.p_net ,-- SET 0.
                o.p_paymentcost , -- SET 0
                o.p_ptentiallyfraudulent -- SET 0
        INTO    #HybrisOrders
       --SELECT * 
        FROM    Hybris.dbo.orders o
                JOIN Hybris.dbo.enumerationvalues s ON s.pk = o.p_status
                JOIN Hybris.dbo.countries co ON co.pk = o.p_country
                JOIN Hybris.dbo.currencies cu ON cu.pk = o.p_currency
                JOIN Hybris.dbo.enumerationvalues ct ON ct.pk = o.p_carttype
                JOIN Hybris.dbo.users u ON u.pk = o.p_user
                LEFT JOIN Hybris.dbo.users cons ON cons.pk = o.p_consultantdetails
                LEFT JOIN Hybris.dbo.enumerationvalues t ON t.PK = o.p_deliverystatus
                LEFT JOIN Hybris.dbo.carts c ON c.pk = o.typepkString



--Orders 
/*
TypePkString--- Orders for all. >>Composedtypes
P_exportStatus-- exported 
P_cartType- adhoc_cart and autoship_cart  Enums
P_deliveryMode --11 different mode in DeliveryModes table
P_deliveryStatus--
Scenario Validation for Orders:
1. P_currency : Whether Currency is matching for that country and users or not?
2. Shipping  Addresses is matching for that Orders or not as RFO ?
3. PaymentProfile is Matching or Not as RFO?
4. Billing Adddress is matching or not as RFO?
5. If Orders is Shipped Status then What is the Orders Status and Delivery Status in Hybris?
6. p_language validations for all orders ? 

*/



		
		
        CREATE CLUSTERED INDEX cls_RFO ON #RFO(RFOKey)
        CREATE CLUSTERED INDEX cls_Hybris ON  #Hybris(HybrisKey)

        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message  

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

        SET @message = ' STEP: 5.initiating DUPLICATE Need to updaete Scripts   '
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
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @owner , -- Owner - nvarchar(50)
                      CONCAT('RFO has NO Duplicate', @key) ,
                      'PASSED'
                    )

		



--++++++++++++++++++--+++++++++++++++++++++
--  HYBRIS  DUPLICATE VAIDATION.
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
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @owner , -- Owner - nvarchar(50)
                      CONCAT('Hybris has No Duplicate', @key) ,
                      'PASSED'
                    )

		



--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++
        SET @message = ' STEP: 6.initiating MISSING Validation now '
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
                WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
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
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
      
			


--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
		
        SET @message = ' STEP: 7.Removing Issues for END TO END Validaion  '
        EXECUTE dbqa.uspPrintMessage @message

      	
        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp
	


        DELETE  a
        FROM    #RFO a
                JOIN #missing m ON m.RFOkey = a.RFOKey


        DELETE  a
        FROM    #Hybris a
                JOIN #missing m ON m.HybrisKey = a.HybrisKey	



        DROP TABLE #missing
 


        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'Orders'
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
GO


--*******************************************************************************************************
--					ORDER ITEM VAIDATIONS.
--*******************************************************************************************************
USE DM_QA
GO
CREATE PROCEDURE dbqa.uspOrderItems_BULK
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
    @owner NVARCHAR(50)= 'Order_Item' ,
    @Key NVARCHAR(25)= 'OrderNumber' ,
    @ValidationType NVARCHAR(50)= 'Counts';

SET NOCOUNT ON;

SET @message = ' STEP: 1.RFOSource Table Started to Load.'
EXECUTE dbqa.uspPrintMessage @message

SELECT  ro.OrderNumber AS [RFOKey] ,--	p_order
        LineItemNo - 1 AS LineItemNo ,--	p_entrynumber
        p.SKU AS ProductID ,--	p_product
        oi.Quantity ,--	p_quantity
        oi.BasePrice ,--	p_baseprice
        oi.TotalPrice ,--	p_totalprice
        oi.TotalTax ,--	p_taxvaluesinternal
        oi.CV ,--	p_cv
        oi.QV	--	p_qv
INTO    #RFO
FROM    RFOperations.Hybris.OrderItem oi
        JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = oi.OrderId
        JOIN RFOperations.Hybris.ProductBase p ON p.productID = oi.ProductID
WHERE   EXISTS ( SELECT 1
                 FROM   Hybris.dbo.Orders ho
                 WHERE  ho.code = ro.OrderNumber )
-- Only Orders loaded in Hybris.

SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
EXECUTE dbqa.uspPrintMessage @message


SELECT  ho.p_code AS [HybrisKey] ,--	OrderId
        p_entrynumber ,--	LineItemNo
        p_product ,--	ProductID
        p_quantity ,--	Quantity
        p_baseprice ,--	BasePrice
        oe.p_totalprice ,--	TotalPrice
        p_taxvaluesinternal ,--	TotalTax
        p_cv ,--	CV
        p_qv	--	QV
INTO    #Hybris
FROM    Hybris.dbo.orderentries oe
        JOIN Hybris.dbo.orders ho ON ho.pk = oe.p_order

SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
EXECUTE dbqa.uspPrintMessage @message    
		
CREATE NONCLUSTERED INDEX  cls_RFO ON #RFO(RFOKey) 
CREATE NONCLUSTERED INDEX  cls_Hybris ON  #Hybris(HybrisKey)



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
        LineItemNo ,
        ProductID
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
    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
              @ValidationType , -- ValidationTypes - nvarchar(50)
              @owner , -- Owner - nvarchar(50)
              CONCAT('RFO has NO Duplicate', @key) ,
              'PASSED'
            )

		



--++++++++++++++++++--+++++++++++++++++++++
-- HYBRIS  DUPLICATE VAIDATION.
--++++++++++++++++++--+++++++++++++++++++++

SELECT  [HybrisKey] ,
        'Hybris' AS SourceFrom
INTO    #DupHybris
FROM    #Hybris
GROUP BY [HybrisKey]
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
    VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
              @ValidationType , -- ValidationTypes - nvarchar(50)
              @owner , -- Owner - nvarchar(50)
              CONCAT('Hybris has No Duplicate', @key) ,
              'PASSED'
            )

		



--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++
SET @message = ' STEP: 6.initiating MISSING Validation now '
EXECUTE dbqa.uspPrintMessage @message

SELECT  a.[RFOKey] ,
        b.[HybrisKey] ,
        CASE WHEN a.[RFOKey] IS NULL THEN 'MissingInRFO-LoadedExtraInHybris'
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
              'No Missing from RFO and Hybris' ,
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
        WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
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
        WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
      
			
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
WHERE   [Owner] = 'OrderItems'
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

	GO
    
	
USE DM_QA
GO
CREATE PROCEDURE dbqa.uspOrderShippingAddress_BULK
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
            @owner NVARCHAR(50)= 'Order_Shipping_Address' ,
            @Key NVARCHAR(25)= 'OrderNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts'

        SET NOCOUNT ON 
        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  ro.OrderNumber AS [RFOKey] ,
                CountryID ,--	p_country
                FirstName ,--	p_firstname
                LastName ,--	p_lastname
                Address1 ,--	p_streetname
                AddressLine2 ,--	p_streetNumber
                PostalCode ,--	p_postalcode
                Locale ,--	p_town
                Region ,--	p_region
                Telephone ,--	p_phone1
                MiddleName ,--	p_middlename
                BirthDay ,--	p_dateofbirth
                GenderID --	p_gender	
        INTO    #RFO
        FROM    RFOperations.Hybris.OrderShippingAddress osa
                JOIN RFOperations.Hybris.orders ro ON ro.OrderID = osa.OrderID
                JOIN RFOperations.Hybris.OrderShipment os ON os.OrderID = ro.OrderID
                JOIN RFOperations.RFO_Accounts.accountcontacts ac ON ac.AccountId = ro.AccountID
        WHERE   EXISTS ( SELECT 1
                         FROM   Hybris.dbo.Orders ho
                                JOIN Hybris.dbo.composedtype ct ON ct.pk = ho.Typepkstring
                                                              AND ct.internalcode <> 'returnOrder'
                         WHERE  ho.p_code = ro.OrderNumber )

        SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  co.p_isocode AS p_country ,--	CountryID
                p_firstname ,--	FirstName
                p_lastname ,--	LastName
                p_streetname ,--	Address1
                p_streetNumber ,--	AddressLine2
                p_postalcode ,--	PostalCode
                p_town ,--	Locale
                re.p_isocodeshort AS p_region ,--	Region
                p_phone1 ,--	Telephone
                p_middlename ,--	MiddleName
                p_dateofbirth ,--	BirthDay
                g.Code AS p_gender ,--	GenderID
                p_duplicate ,--	
                p_Shippingaddress ,--	
                ho.p_code AS [HybrisKey]
        INTO    #Hybris
        FROM    Hybris.dbo.addresses ad
                JOIN Hybris.dbo.orders ho ON ho.pk = ad.OwnerPkString
                                             AND ad.p_duplicate = 1
                JOIN Hybris.dbo.composedtypes ct ON ct.pk = ho.TypePkString
                                                    AND ct.InternalCode <> 'ReturnOrder'
                JOIN Hybris.dbo.countries co ON co.pk = ad.p_country
                JOIN Hybris.dbo.regions re ON re.pk = ad.p_region
                JOIN Hybris.dbo.enumerationvalues g ON g.pk = ad.p_gender 


	
        CREATE NONCLUSTERED INDEX cls_RFO ON #RFO(RFOKey)  
        CREATE NONCLUSTERED INDEX cls_Hybris ON  #Hybris(HybrisKey)   

        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message  

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
        SET @message = ' STEP: 5.initiating DUPLICATE Need to updaete Scripts   '
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
--USA HYBRIS Autoship DUPLICATE VAIDATION.
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
        SET @message = ' STEP: 6.initiating MISSING Validation now '
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
                WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
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
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
      
			
-- Default Validtion in Target Attributes




-- MixNMatch

        SET @message = ' STEP: 7.initiating MIXMATCH Validation '
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
        WHERE   [Owner] = 'OrderShippingAddress'
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
	GO
    
	
USE DM_QA
GO
CREATE PROCEDURE dbqa.uspOrderPaymentTransactionNEntry_BULK
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
            @owner NVARCHAR(50)= 'Order_PaymentTransation_N_Entry' ,
            @Key NVARCHAR(25)= 'OrderNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts'

        SET NOCOUNT ON 
        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  ro.OrderNumber AS [RFOKey] ,
                opt.OrderPaymentID ,--	OwnerPkstring
                AmountAuthorized ,--	p_plannedAmount
                TransactionID ,--	p_requestID
                ProcessDate ,--	p_time
                PaymentProvider ,--	p_paymentprovider
                CONCAT(ro.OrderNumber, '_', op.OrderPaymentID) AS OrderPaymentTransactionID --	p_code    
        INTO    #RFO
        FROM    RFOperations.Hybris.OrderPaymentTransaction opt
                JOIN RFOperations.Hybris.OrderPayment op ON op.OrderPaymentID = opt.OrderPaymentID
                JOIN RFOperations.Hybris.Orders ro ON ro.OrderID = op.OrderID
        WHERE   EXISTS ( SELECT 1
                         FROM   Hybris.dbo.orders ho
                                JOIN Hybris.dbo.composedtype ct ON ct.pk = ho.typepkstring
                                                              AND ct.internalcode = 'returnOrder'
                         WHERE  ho.p_code = ro.OrderNumber )

        SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  ho.p_code AS [HybrisKey] ,
                OwnerPkstring ,--	OrderPaymentID
                p.p_plannedAmount ,--	AmountAuthorized
                p.p_requestID ,--	TransactionID
                pte.p_time ,--	ProcessDate
                v.Code AS p_paymentprovider ,--	PaymentProvider
                pte.p_code ,--	OrderNumber,OrderPaymentID
                pte.p_amount	--	AmountAuthorized
        INTO    #Hybris
        FROM    Hybris.dbo.PaymentTransactions p
                JOIN Hybris.dbo.enumerationvalues v ON v.pk = p.p_paymentprovider
                JOIN Hybris.dbo.paymenttransactionsEntries pte ON pte.p_paymenttransaction = p.PK
                                                              AND p.p_requestID = pte.p_requestID
                JOIN Hybris.dbo.orders ho ON ho.pk = a.p_order
                JOIN Hybris.dbo.composedtypes ct ON ct.pk = ho.TypePkString
                                                    AND ct.InternalCode <> 'returnOrder'


 
	
        CREATE NONCLUSTERED INDEX cls_RFO ON #RFO(RFOKey)  
        CREATE NONCLUSTERED INDEX cls_Hybris ON  #Hybris(HybrisKey)   

        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message  

--++++++++++++++++++++++++++++++++++++
-- TOTAL COUNT VALIDATION
--++++++++++++++++++++++++++++++++++++
        SET @message = ' STEP: 4.initiating COUNT Validation  '
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  @SourceCount = COUNT(DISTINCT [RFOKey])
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
-- RFO   Duplicate Vaidation.
--++++++++++++++++++--++++++++++++++++++
        SET @message = ' STEP: 5.initiating DUPLICATE Need to updaete Scripts   '
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
        SET @message = ' STEP: 6.initiating MISSING Validation now '
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
                WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
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
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
      

-- MixNMatch

        SET @message = ' STEP: 7.initiating MIXMATCH Validation '
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
        WHERE   [Owner] = 'OrderPaymentTransaction'
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


GO



USE DM_QA
GO
CREATE PROCEDURE dbqa.uspOrderConsignmentNEntry_BULK
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
            @owner NVARCHAR(50)= 'Order_Consignment_N_Entry' ,
            @Key NVARCHAR(25)= 'OrderNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts'

        SET NOCOUNT ON 
        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  Quantity ,--	p_quantity
                Quantity ,--	p_shippedquantity
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
                                                              AND ct.internalcode <> 'returnOrder' )
        SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
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
                                                    AND ct.InternalCode <> 'returnOrder'
                JOIN Hybris.dbo.enumerationvalues v ON v.pk = c.p_status
                JOIN Hybris.dbo.deliverymodes m ON m.pk = c.p_deliverymode

        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message     
	
        CREATE NONCLUSTERED INDEX cls_RFO ON #RFO(RFOKey)  
        CREATE NONCLUSTERED INDEX cls_Hybris ON  #Hybris(HybrisKey)   



--++++++++++++++++++++++++++++++++++++
-- TOTAL COUNT VALIDATION
--++++++++++++++++++++++++++++++++++++

        SET @message = ' STEP: 4.initiating COUNT Validation  '
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
        SET @message = ' STEP: 5.initiating DUPLICATE Need to updaete Scripts   '
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
        SET @message = ' STEP: 6.initiating MISSING Validation now '
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
                WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
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
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
      
			
-- MixNMatch
        SET @message = ' STEP: 7.initiating MIXMATCH Validation '
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
														
GO
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

GO

USE DM_QA
GO
CREATE PROCEDURE dbqa.uspOrderPaymentAddress_BULK
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
            @owner NVARCHAR(50)= 'Order_Payment_Address' ,
            @Key NVARCHAR(25)= 'OrderNumber' ,
            @ValidationType NVARCHAR(50)= 'Counts'

        SET NOCOUNT ON 
        SET @message = ' STEP: 1.RFOSource Table Started to Load.'
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  ro.OrderNumber AS [RFOKey] ,
                co.Alpha2Code AS CountryID ,--	p_country
                oba.FirstName ,--	p_firstname
                oba.LastName ,--	p_lastname
                Address1 ,--	p_streetname
                AddressLine2 ,--	p_streetNumber
                PostalCode ,--	p_postalcode
                Locale ,--	p_town
                Region ,--	p_region
                Telephone ,--	p_phone1
                oba.MiddleName ,--	p_middlename
                IIF(CAST(ac.BirthDay AS DATE) = '1900-01-01', NULL, ac.Birthday) AS BirthDay ,--	p_dateofbirth
                g.Name AS GenderID --	p_gender  
        INTO    #RFO
        FROM    RFOperations.Hybris.OrderBillingAddress oba
                JOIN RFOperations.Hybris.OrderPayment op ON op.OrderID = oba.OrderID
                JOIN RFOperations.Hybris.orders ro ON ro.OrderID = oba.OrderID
                JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ro.AccountID
                JOIN RFOperations.RFO_Reference.Gender g ON g.GenderID = ac.GenderId
                JOIN RFOperations.RFO_Reference.Countries co ON co.CountryID = oba.CountryID
        WHERE   EXISTS ( SELECT 1
                         FROM   Hybris.dbo.orders ho
                                JOIN Hybris.dbo.Composedtypes c ON c.pk = ho.TypepkString
                                                              AND c.internalcode <> 'ReturnOrder'
                         WHERE  ho.p_code = ro.OrderNumber )

        SET @message = 'STEP: 2.RFOSource Table Loaded and Target Table Started to Load'
        EXECUTE dbqa.uspPrintMessage @message

        SELECT  ho.p_code AS [HybrisKey] ,
                co.p_isocode AS p_country ,--	CountryID
                p_firstname ,--	FirstName
                p_lastname ,--	LastName
                p_streetname ,--	Address1
                p_streetNumber ,--	AddressLine2
                p_postalcode ,--	PostalCode
                p_town ,--	Locale
                re.p_isocodeshort AS p_region ,--	Region
                p_phone1 ,--	Telephone
                p_middlename ,--	MiddleName
                p_dateofbirth ,--	BirthDay
                g.Code AS p_gender ,--	GenderID
                p_duplicate ,--	
                p_billingaddress ,--	
                ad.Ownerpkstring
        INTO    #Hybris
        FROM    Hybris.dbo.addresses ad
                JOIN Hybris.dbo.paymentinfos pa ON pa.PK = ad.OwnerPkString
                JOIN Hybris.dbo.orders ho ON ho.pk = pa.OwnerPkString
                                             AND ho.p_user = pa.p_user
                JOIN Hybris.dbo.composedtypes ct ON ct.pk = ho.TypePkString
                                                    AND ct.InternalCode <> 'ReturnOrder'
                JOIN Hybris.dbo.countries co ON co.pk = ad.p_country
                JOIN Hybris.dbo.regions re ON re.pk = ad.p_region
                JOIN Hybris.dbo.enumerationvalues g ON g.pk = ad.p_gender


	
        CREATE NONCLUSTERED INDEX cls_RFO ON #RFO(RFOKey) INCLUDE(CountryID,BirthDay,GenderID)
        CREATE NONCLUSTERED INDEX cls_Hybris ON  #Hybris(HybrisKey) INCLUDE(p_country,p_firstname,p_lastname,p_postalcode,p_town,p_region,p_phone1,p_streetname)

        SET @message = ' STEP:3.Hybris Table Loaded and Starting to Validate'
        EXECUTE dbqa.uspPrintMessage @message    

--++++++++++++++++++++++++++++++++++++
-- TOTAL COUNT VALIDATION
--++++++++++++++++++++++++++++++++++++

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
                LineItemNo ,
                ProductID
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
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @owner , -- Owner - nvarchar(50)
                      CONCAT('RFO has NO Duplicate', @key) ,
                      'PASSED'
                    )

		



--++++++++++++++++++--+++++++++++++++++++++
-- HYBRIS  DUPLICATE VAIDATION.
--++++++++++++++++++--+++++++++++++++++++++

        SELECT  [HybrisKey] ,
                'Hybris' AS SourceFrom
        INTO    #DupHybris
        FROM    #Hybris
        GROUP BY [HybrisKey]
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
            VALUES  ( @Flows , -- FlowTypes - nvarchar(50)
                      @ValidationType , -- ValidationTypes - nvarchar(50)
                      @owner , -- Owner - nvarchar(50)
                      CONCAT('Hybris has No Duplicate', @key) ,
                      'PASSED'
                    )

		



--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++

        SET @message = ' STEP: 6.initiating MISSING Validation now '
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
                WHERE   [Missing From ] = 'MissingInRFO-LoadedExtraInHybris'
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
                WHERE   [Missing From ] = 'MissingInHybris-NotLoadedInHybris'
      
	
-- MixNMatch

        SET @message = ' STEP: 7.initiating MIXMATCH Validation '
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
        WHERE   [Owner] = 'OrderPaymentAddress'
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

	
	GO
    
USE DM_QA
GO


CREATE  PROCEDURE dbqa.uspOrders_Master_BULK_Validation  (@LoadDate DATE)
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message='OrderHeader Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspOrderHeader_BULK @LoadDate

SET @message='OrdersPayment  Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.upsOrderPayment_BULK


SET @message='OrderItems Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderItems_BULK


 SET @message='OrderPaymentAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspOrderPaymentAddress_BULK  



SET @message='OrderShippingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspOrderShippingAddress_BULK

SET @message='OrdersPaymentTransactions Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderPaymentTransactionNEntry_BULK


 SET @message='OrderConsignmentNEntry Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderConsignmentNEntry_BULK
 


END 
 
GO



