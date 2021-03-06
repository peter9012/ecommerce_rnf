USE [DM_QA]
GO
/****** Object:  StoredProcedure [dbqa].[uspProducts_BULK]    Script Date: 3/1/2017 12:45:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER  PROCEDURE [dbqa].[uspProducts_BULK]  
AS
    BEGIN 
        SET NOCOUNT ON;


        DECLARE @SourceCount INT ,
            @TargetCount INT ,
            @message NVARCHAR(MAX) ,
            @Flows NVARCHAR(50)= 'Data Migration' ,
            @owner NVARCHAR(50)= 'Products' ,
            @Key NVARCHAR(25)= 'CatalogNumber[SKU]' ,
            @ValidationType NVARCHAR(50)= 'Counts' 




        IF OBJECT_ID('TEMPDB.dbo.#RFO') IS NOT NULL
            DROP TABLE  #RFO;
        IF OBJECT_ID('TEMPDB.dbo.#Hybris') IS NOT NULL
            DROP TABLE #Hybris;       
        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing  
	

        SET @message = CONCAT(' STEP:1. Source Table Started to Load.',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

        

       

SELECT  pe.SKU_Code AS [RFOKey] ,
        ISNULL(pe.ProductName, '') ProductName ,
        ISNULL(pe.ApprovedStatus, '') ApprovedStatus ,
       'Product'AS MaterialTypes ,
        pe.Country ,
        pe.ActiveStatus AS [Status] ,
        ISNULL(pe.[Description], '') AS [Description] ,
        ISNULL(pe.Summary, '') Summary ,
        ISNULL(pe.UsageNotes, '') UsageNotes ,
        ISNULL(pe.Ingredients, '')   Ingredients ,
        ISNULL(pr.consultantPrice, 0) consultantsPrice ,
        ISNULL(pr.PreferredPrice, 0) PCPrice ,
        ISNULL(pr.RetailPrice, 0) RetailPrice ,
        pr.currency ,
        ISNULL(pr.CV, 0) AS SV ,
        ISNULL(pr.QV, 0) QV ,
        pe.Unit ,
        GETDATE() AS StartTime ,
        GETDATE() - 60 AS EndTime ,
        1 [Weight] ,
        '' VariantType ,
        1 AS Returnable ,
        CASE WHEN Country = 'US' THEN 'rodanandfieldsProductCatalog'
             WHEN Country = 'CA' THEN 'rodanandfieldsCANProductCatalog'
             WHEN Country = 'AU' THEN 'rodanandfieldsAUSProductCatalog'
             ELSE 'Invalid'
        END AS [CATALOG] ,
        'RodanFields' AS Manufacturer ,
        'Rodan & Fields' AS ManufacturerName
 INTO    #RFO
FROM   DM_QA.dbo.ProductEnrich pe
        LEFT JOIN DM_QA.dbo.productprice pr ON pr.sku_code = pe.SKU_Code


        SET @message = CONCAT(' STEP: 2.Target Table Started to Load',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message



	


    SELECT p.pk AS [ProductPk] ,
        p.p_code [HybrisKey] ,
		CASE  t.InternalCode WHEN 'RnFVariantProduct' THEN 'Product'
		ELSE t.InternalCode END AS InternalCode,
        u.p_code p_conversion ,
        v.Code AS p_approvalstatus ,
          CASE v.Code WHEN 'approved' THEN 'Active'
		  WHEN 'unapproved'THEN 'Inactive'END  AS p_status ,
        CASE cat.p_id
          WHEN 'rodanandfieldsProductCatalog'  THEN 'US'
          WHEN 'rodanandfieldsCANProductCatalog' THEN 'CA'
          WHEN 'rodanandfieldsAUSProductCatalog' THEN 'AU'
        END AS p_country ,
		cat.p_id AS p_catalog,
        Catv.p_version [CatalogVersion] ,
        ISNULL(lp.p_name,'') AS p_name ,
        ISNULL(lp.p_description,'') AS  p_description,
        ISNULL(lp.p_summary,'')  AS p_summary,
        ISNULL(lp.p_usagenotes,'') AS p_usagenotes,
       ISNULL(lp.p_ingredients,'') AS p_ingredients,
        ISNULL(Consultant.p_price,0) AS CPrice ,
        ISNULL(Consultant.p_sv,0) AS p_sv ,
        ISNULL(Consultant.p_qv,0)AS p_qv ,
        ISNULL(pc.p_price,0) AS PPrice ,
        ISNULL(Rc.p_price,0) AS Rprice ,
        COALESCE(Consultant.p_isocode, pc.p_isocode, RC.p_isocode) AS p_currency ,
        COALESCE(Consultant.p_starttime, pc.p_starttime, RC.p_starttime) AS Stime ,
        COALESCE(Consultant.p_endtime, pc.p_endtime, RC.p_endtime) AS Etime ,
        p.p_weight ,
        p.p_varianttype ,
        p.p_variantcode ,
        p.p_returnable ,
		p.p_manufactureraid,
		p.p_manufacturername
INTO    #Hybris
FROM    Hybris.dbo.products p
        LEFT JOIN Hybris.dbo.units u ON u.pk = p.p_unit
        LEFT JOIN Hybris.dbo.composedtypes t ON t.pk = p.TypePkString
        LEFT JOIN Hybris.dbo.catalogs cat ON cat.pk = p.p_catalog
        LEFT JOIN hybris.dbo.catalogversions catv ON catv.pk = p.p_catalogversion
        LEFT JOIN Hybris.dbo.enumerationvalues v ON v.pk = p.p_approvalstatus
        LEFT JOIN ( SELECT  lp.ITEMPK ,
                            lp.p_name ,
                            lp.p_description ,
                            lp.p_summary ,
                            lp.p_usagenotes ,
                            lp.p_ingredients
                    FROM    Hybris.dbo.productslp lp
                            JOIN Hybris.dbo.languages l ON l.pk = lp.LANGPK
                    WHERE   l.p_isocode = 'en'
                  ) lp ON lp.ITEMPK = p.PK
        LEFT JOIN ( SELECT  pr.p_product ,
                            pr.p_price ,
                            pr.p_sv ,
                            pr.p_qv ,
                            cu.p_isocode ,
                            pr.p_starttime ,
                            pr.p_endtime
                    FROM    Hybris.dbo.pricerows pr
                            JOIN hybris.dbo.enumerationvalues v ON v.pk = pr.p_ug
                            LEFT JOIN Hybris.dbo.currencies cu ON cu.pk = pr.p_currency
                    WHERE   v.Code = '01'
                  ) Consultant ON Consultant.p_product = p.PK
        LEFT JOIN ( SELECT  pr.p_product ,
                            pr.p_price ,
                            cu.p_isocode ,
                            pr.p_starttime ,
                            pr.p_endtime
                    FROM    Hybris.dbo.pricerows pr
                            JOIN hybris.dbo.enumerationvalues v ON v.pk = pr.p_ug
                            LEFT JOIN Hybris.dbo.currencies cu ON cu.pk = pr.p_currency
                    WHERE   v.Code = '02'
                  ) PC ON PC.p_product = p.PK
        LEFT JOIN ( SELECT  pr.p_product ,
                            pr.p_price ,
                            cu.p_isocode ,
                            pr.p_starttime ,
                            pr.p_endtime
                    FROM    Hybris.dbo.pricerows pr
                            JOIN hybris.dbo.enumerationvalues v ON v.pk = pr.p_ug
                            LEFT JOIN Hybris.dbo.currencies cu ON cu.pk = pr.p_currency
                    WHERE   v.Code = '03'
                  ) RC ON RC.p_product = p.PK
WHERE   catv.p_version = 'Online'

	




	
        SET @message = CONCAT('STEP: 3. Index Creating Source N Target.',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message
      

/* Source to Target Total Counts */


--++++++++++++++++++++++++++++++++++++
-- USA_CAN Total Count Validation
--++++++++++++++++++++++++++++++++++++

    


        CREATE CLUSTERED INDEX cls_RFO ON  #RFO([RFOKey])
        CREATE CLUSTERED INDEX cls_Hybris ON #Hybris([HybrisKey])


        SET @ValidationType = 'Count'
        SET @message = CONCAT(' STEP: 4 Count Validations Started. ', CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


        SELECT  @SourceCount = COUNT(DISTINCT [RFOKey])
        FROM    #RFO
        SELECT  @TargetCount = COUNT(DISTINCT [HybrisKey])
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
                  @owner, -- Owner - nvarchar(50)
                  @SourceCount , -- SourceCount - int
                  @TargetCount , -- TargetCounts - int
                  CASE WHEN @SourceCount > @TargetCount
                       THEN CONCAT('Source Count More than Target BY ',
                                   CAST(@SourceCount - @TargetCount AS NVARCHAR(10)))
                       WHEN @SourceCount < @TargetCount
                       THEN CONCAT('Target Count More than Source Count By ',
                                   CAST(@TargetCount - @SourceCount AS NVARCHAR(10)))
                       ELSE 'Source to Target Counts are equal'
                  END ,
                  CASE WHEN @SourceCount = @TargetCount THEN 'PASSED'
                       ELSE 'FAILED'
                  END
                )
		




		

        SET @message = CONCAT(' STEP: 5. Duplicate validation started.',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message




--++++++++++++++++++--++++++++++++++++++
--  Duplicate Vaidation.
--++++++++++++++++++--++++++++++++++++++
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
                SELECT  RFOKey ,
                        'RFO' AS [SourceFrom]
                FROM    #RFO
               GROUP BY  [RFOKey]--,Country
                  HAVING    COUNT([RFOKey]) > 1

        INSERT  INTO @DUP
                ( [key] ,
                  SourceFrom
                )
                SELECT  HybrisKey ,
                        'Hybris' AS [SourceFrom]
                FROM    #Hybris
                GROUP BY  [HybrisKey]--,P_country
                  HAVING    COUNT([HybrisKey]) > 1


         SELECT  @SourceCount = COUNT([key])
        FROM    @DUP
        WHERE   SourceFrom = 'RFO'
        SELECT  @TargetCount = COUNT([key])
        FROM    @DUP
        WHERE   SourceFrom = 'Hybris'

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
                WHERE   SourceFrom = 'RFO'
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
                WHERE   SourceFrom = 'Hybris'         
                


--++++++++++++++++++++++++++++++++++++++++++
-- MISSING IN SOURCE AND MISSING IN TARGET 
--++++++++++++++++++++++++++++++++++++++++++


        SET @message = CONCAT(' STEP: 6. Missing Validaiton Started ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


--US ACCOUNTS SCTOTG  MISSING VALIDATION 
        SELECT  a.RFOKey ,
                b.[HybrisKey] ,               
                CASE WHEN a.[RFOKey] IS NULL
                     THEN 'MissingInSource'
                     WHEN b.[HybrisKey] IS NULL
                     THEN 'MissingInTarget'
                END AS [Missing From ]
        INTO    #Missing
        FROM    #RFO a
                FULL OUTER JOIN #Hybris b ON a.RFOKey = b.[HybrisKey]
        WHERE   a.RFOKey IS NULL
                OR b.[HybrisKey] IS NULL

        SET @SourceCount = 0
        SET @TargetCount = 0
        SET @ValidationType = 'Missing'

        SELECT  @SourceCount = COUNT(*)
        FROM    #Missing
        WHERE    [Missing From ] = 'MissingInSource'
        SELECT  @TargetCount = COUNT(*)
        FROM    #Missing
        WHERE    [Missing From ] = 'MissingInTarget'
	
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
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetColumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [RFOKey] , -- SourceValue - nvarchar(50)
                        [HybrisKey]
                FROM    #Missing
                WHERE    [Missing From ] = 'MissingInSource'
                UNION
                SELECT TOP 10
                        @Flows , -- FlowTypes - nvarchar(50)
                        @owner , -- Owner - nvarchar(50)
                        [Missing From ] , -- Flag - nvarchar(10)
                        N'' , -- SourceColumn - nvarchar(50)
                        N'' , -- TargetColumn - nvarchar(50)
                        @Key , -- Key - nvarchar(50)
                        [RFOKey] , -- SourceValue - nvarchar(50)
                        [HybrisKey]
                FROM    #Missing
                WHERE    [Missing From ] = 'MissingInTarget'
               

--++++++++++++++++++++++++++++++++++++++++++++++++++
 --VALIDATION STARTING FOR END TO END 
--++++++++++++++++++++++++++++++++++++++++++++++++++
		
        IF OBJECT_ID('Tempdb.dbo.#Temp') IS NOT NULL
            DROP TABLE #Temp

    

        SET @message = CONCAT(' STEP: 7. Removing Isuues Key from Temps. ',
                              CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message


         
        DELETE  a
        FROM    #RFO a
                JOIN #missing m ON m.RFOkey = a.RFOKey


        DELETE  a
        FROM    #Hybris a
                JOIN #missing m ON m.HybrisKey = a.HybrisKey	
 DELETE  a
        FROM    #RFO a
                JOIN @Dup m ON m.[key] = a.RFOKey
        DELETE  a
        FROM    #Hybris a
                JOIN @Dup m ON m.[key] = a.Hybriskey


        IF OBJECT_ID('tempdb.dbo.#Missing') IS NOT NULL
            DROP TABLE #Missing  
			  DELETE  @Dup;


        SET @sourceCount = 0
        SELECT  @sourceCount = COUNT(RFOKey)
        FROM    #RFO a
                JOIN #Hybris b ON a.RFOKey = b.HybrisKey

        SET @message = CONCAT(' STEP: 6. Total Record Counts for End to End = ',
                              CAST(@SourceCount AS NVARCHAR(25)), CHAR(10),
                              '-----------------------------------------------')
        EXECUTE dbqa.uspPrintMessage @message

   
        SELECT  * ,
                ROW_NUMBER() OVER ( ORDER BY mapID ) AS [RowNumber]
        INTO    #Temp
        FROM    dbqa.Map_tab
        WHERE   [Owner] = 'Products'
                AND [flag] IN ( 'c2c', 'ref', 'def', 'upd' )


		

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
                SET @Message = CONCAT('STEP: 7. Validation Started For Columnt To Column with  total fields= ',
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
               PRINT @Stmt
                        SET @RowNumber = @RowNumber + 1

                    END 



            END
    END 

		











