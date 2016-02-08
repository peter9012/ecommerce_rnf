			
				 /**
				 DataMigration..productexcelsheet is the table Where data loaded from Excel Sheet to validate with Hybris..products.
				  DataMigration..productDetails is the Table where data cleaned and modifed as Hybris data type and default Values.
				  like 0 for Null , or Pk for Active something like this.

				  *******
				 
				 
				 
				USE [DataMigration]
GO

SELECT  [ProductID] ,
        [Catalog] ,
        CASE [Status]
          WHEN 'Active' THEN '8796096954459'
          WHEN 'Inactive' THEN '8796096987227'
        END AS [Status] ,
        [Item#] ,
        [UPC] ,
        [Primary Name] ,
        [Secondary Name] ,
        [Full Name] ,
        [Product Type] ,
        ( SELECT    PK
          FROM      Hybris..enumerationvalues
          WHERE     Code IN ( [AvaTax System Tax Code] )
        ) AS [AvaTax System Tax Code] ,
        [AvaTax System Tax Code Description] ,
        ISNULL([Membership fee of total price], 0) AS [Membership fee of total price] ,
        ISNULL([Service fee of total price], 0) AS [Service fee of total price] ,
        ISNULL([Shipping fee of total price], 0) AS [Shipping fee of total price] ,
        ISNULL([CV],0) AS [CV] ,
        ISNULL([QV],0) AS [QV] ,
        ISNULL([Consultant],0)AS [Consultant] ,
        ISNULL([PreferredCustomer],0) AS [PreferredCustomer]  ,
        ISNULL([Retail],0) AS [Retail] ,
        ISNULL([Consultant_Taxable],0)AS[Consultant_Taxable] ,
        ISNULL([Wholesale Price],0)AS [Wholesale Price] ,
        [Summary] ,
        [Description] ,
        [Usage Notes] ,
        [Ingredients],
        ISNULL([Base], 0) AS [Base] ,
        ISNULL([CRP], 0) AS [CRP] ,
        ISNULL([PC Perks], 0) AS [PC Perks] ,
        ISNULL([Enrollment CRP Only], 0) AS [Enrollment CRP Only] ,
        ISNULL([Admin], 0) AS [Admin] ,
        ISNULL([POS], 0) AS [POS] ,
        CASE [CAT_AMP MD] WHEN 1 THEN 'rf0091'ELSE 0 END AS [CAT_AMP MD] ,
        CASE [CAT_REDEFINE] WHEN 1 THEN 'rf0011'ELSE 0 END AS [CAT_REDEFINE] ,
        CASE [CAT_REVERSE] WHEN 1 THEN 'rf0021'ELSE 0 END AS [CAT_REVERSE] ,
        CASE [CAT_UNBLEMISH] WHEN 1 THEN 'rf0041'ELSE 0 END AS [CAT_UNBLEMISH] ,
        CASE [CAT_SOOTHE] WHEN 1 THEN 'rf0041'ELSE 0 END AS [CAT_SOOTHE] ,
        CASE [CAT_ENHANCEMENTS] WHEN 1 THEN 'rf0041'ELSE 0 END AS [CAT_ENHANCEMENTS] ,
        CASE [CAT_ESSENTIALS] WHEN 1 THEN 'rf0041'ELSE 0 END AS [CAT_ESSENTIALS] ,
       CASE [CAT_ConsultantsOnly] WHEN 1 THEN 'rf0041'ELSE 0 END AS [CAT_ConsultantsOnly] ,
        CASE [CAT_Consultant-Only Business Promotion] WHEN 1 THEN 'rf0041'ELSE 0 END AS [CAT_Consultant-Only Business Promotion] ,
        CASE [CAT_UNBLEMISH] WHEN 1 THEN 'rf0041'ELSE 0 END AS [CAT_Consultant-Only Event Support] ,
        CASE [CAT_Consultant-Only Product Promotion] WHEN 1 THEN 'rf0041'ELSE 0 END AS [CAT_Consultant-Only Product Promotion] ,
        CASE [CAT_Consultant-Only Products] WHEN 1 THEN 'rf0041'ELSE 0 END AS [CAT_Consultant-Only Products] ,
        CASE [Country Catalog]
          WHEN 'US' THEN '8796093088344'
          WHEN 'CA' THEN '8796093121112'
        END AS [Country Catalog] ,
        Isnull([Priority] ,0) as [Priority],
        CASE [IsDefaultCRP]
          WHEN 'YES' THEN 1
          ELSE 0
        END [IsDefaultCRP] ,
        CASE [Autoship Replenishable]
          WHEN 'NO' THEN 0
          ELSE 1
        END AS [Autoship Replenishable] ,
         [PC Replacement Status] ,
         [CRP Replacement Status] ,
        [Replacement Products for PC Perks] --AS [Replacement Products for PC Perks]
        ,
        [Replacement Products for CRP] -- AS [Replacement Products for CRP]
        ,
       [Non-Replenishable Message for PC Perks], ---'') AS [Non-Replenishable Message for PC Perks] ,
       [Non-Replenishable Message for CRP], --- '') AS [Non-Replenishable Message for CRP] ,
        [Non-Replenishable email paragraph for PC Perks],----'') AS [Non-Replenishable email paragraph for PC Perks] ,
        [Non-Replenishable email paragraph for CRP], ----'') AS [Non-Replenishable email paragraph for CRP] ,
        [Replacement Reason]  -- AS [Replacement Reason]
        ,
        ISNULL([Best Seller], 0) AS [Best Seller] ,
        ISNULL([New], 0) AS [New] ,
        ISNULL([Not Available], 0) AS [Not Available] ,
        [Not Available Message],   ---'') AS [Not Available Message] ,
        ISNULL([Special], 0) AS [Special] ,
        [Special Message],  -- '') AS [Special Message] ,
        ISNULL([Low Quantity Level],1) AS [Low Quantity Level] ,
        ISNULL([Restockable], 1) AS [Restockable] ,
        ISNULL([Weight], 0) AS [Weight] ,
        ISNULL([Shippable], 1) AS [Shippable] ,
        ISNULL([Charge Shipping], 1) AS [Charge Shipping] ,
        ISNULL([Charge Handling], 1) AS [Charge Handling] ,
        [hybrisType] ,
        ISNULL([IsVariant], 0) AS [IsVariant] ,
        [IsBase],  --AS [IsBase] ,
        CASE [Variant type]
          WHEN 'RFKitProduct' THEN '8796124020818'
          WHEN 'RFShadeVariantProduct' THEN '8796123988050'
          WHEN 'RFTonerVariantProduct' THEN '8796123955282'
        END AS [Variant type] ,
        [Variant Parent] ,
        [Variant Name]
INTO    #products
FROM    [dbo].[ProductExcelSheet]

*****************************************************************************************/



  
	--		DROP TABLE #products
	
				 
  --RF_Retail_PriceGroup=8796164358235
  --RF_PreferredCustomer_PriceGroup=8796164391003
  --RF_Consultant_PriceGroup=8796164423771
  
	--Inactive=  8796096987227
	--Active =	8796096954459
	---8796093121113  ---Stage
	---8796093153881  --Online
	--8796093186649-Stage
	---8796093088344 -UsProductCatalog
	---'RFKitProduct'=8796124020818
	--RFShadeVariantProduct=8796123988050
	--RFTonerVariantProduct=8796123955282
  
 --
 --SELECT a.* from
 --(SELECT DISTINCT a.pk,c.* FROM Hybris.dbo.products a,
 --Hybris.dbo.pricerows c
 --WHERE a.Pk=C.P_product
 --AND C.P_ug IN (SELECT DISTINCT c.p_ug FROM hybris.dbo.pricerows))a
 
 
 --SELECT * FROM hybris.dbo.enumerationvalues WHERE pk IN (SELECT DISTINCT p_ug FROM Hybris.dbo.pricerows)
 --SELECT * FROM Hybris.dbo.pricerows 
 --WHERE p_product=8796503605249  --- TO CHECK P_UG TYPES IN PRICEROWS FOR DIFFERENT TYPES OF CUSTOMERS




				/****** THIS IS THE QUERY TO LOAD DATAMIGRATION.DBO.PRODUCTDETAILS TABLE

				
				USE [DataMigration]
				
				SELECT CASE b.p_catalogversion WHEN '8796093153881' THEN 'Stage'
												WHEN '8796093121113' THEN 'Online'
												END AS CatalogVersion,
						b.pk AS HybrisPK,a.* INTO  DataMigration.dbo.productDetails
				FROM #products a
				  JOIN hybris.dbo.products b
				  ON a.productid=b.p_rflegacyproductid
				 -- WHERE b.p_catalogversion ='8796093121113'
				 ORDER BY a.productid

				 --DROP TABLE DataMigration..productDetails

		****************************************************************************/

 

			

					---==============================================================================
					--			Counts source to target
					--===============================================================================
			SET STATISTICS TIME ON
            SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED

                        DECLARE @MESSAGE NVARCHAR(MAX);
                        DECLARE @COUNTSr INT;
                        DECLARE @COUNTTg INT;
						 
                        SELECT  @COUNTSr = ( SELECT COUNT(DISTINCT ProductID)
                                             FROM   DataMigration..productDetails
                                           );
										---  Total US Products=566
                        SELECT  @MESSAGE = ( 'Total Products Counts from Excel Sheet='
                                             + CONVERT(NVARCHAR(255), @COUNTSr) );
									
                        SELECT  @COUNTTg = ( SELECT COUNT(DISTINCT p_rflegacyproductid)
                                             FROM   Hybris..products
                                             WHERE  p_catalog IN (
                                                    SELECT  PK
                                                    FROM    Hybris.dbo.catalogs
                                                    WHERE   p_id = 'usProductCatalog' )
                                                    AND p_catalognumber IS NOT NULL
                                           );
										---  Total US Products=566
                        SELECT  @MESSAGE = @MESSAGE
                                + ( ' And Total Products Counts In Hybris migrated from Sheet='
                                    + CONVERT(NVARCHAR(255), @COUNTTg) );
                        PRINT @MESSAGE;

                        SELECT  @MESSAGE = 'Column2Column Validation is Starting Now.';
                        PRINT @MESSAGE;



								--		DROP TABLE #productid
                               

					--=========================================================================
					--COLUMN2COLUMN VALIDATIONS
					--===============================================================================
					
                    DELETE  DataMigration..dm_log
                    WHERE   test_area = '1174-products';
						
						
                    DROP TABLE #TEMPACT;


                    BEGIN
						
				
                        SELECT DISTINCT
                                HybrisPK
                        INTO    #tempact
                        FROM    DataMigration..productDetails;


                        DECLARE @HYB_key VARCHAR(100) = 'pk';
                        DECLARE @RFO_key VARCHAR(100) = 'HybrisPK';
                        DECLARE @sql_gen_1 NVARCHAR(MAX);
                        DECLARE @cnt INT;
                        DECLARE @lt_1 INT;
                        DECLARE @lt_2 INT;
                        DECLARE @temp TABLE
                            (
                              test_area VARCHAR(MAX) ,
                              test_type VARCHAR(MAX) ,
                              rfo_column VARCHAR(MAX) ,
                              hybris_column VARCHAR(MAX) ,
                              hyb_key VARCHAR(MAX) ,
                              hyb_value VARCHAR(MAX) ,
                              rfo_key VARCHAR(MAX) ,
                              rfo_value VARCHAR(MAX)
                            );

                        SET @cnt = 1;
                        SELECT  @lt_1 = COUNT(*)
                        FROM    [DataMigration].[dbo].[map_tab]
                        WHERE   flag = 'c2c'
                                AND rfo_column <> @RFO_key
                                AND [owner] = '1174-Products';

                        WHILE @cnt <= @lt_1
                            BEGIN

                                SELECT  @sql_gen_1 = 'SELECT DISTINCT  '''
                                        + [owner] + ''', ''' + flag + ''', '''
                                        + RFO_Column + ''' as rfo_column, '''
                                        + Hybris_Column
                                        + ''' as hybris_column, A.' + @HYB_key
                                        + ' as hyb_key, A.' + Hybris_Column
                                        + ' as hyb_value, B.' + @RFO_key
                                        + ' as rfo_key, B.' + RFO_Column
                                        + ' as rfo_value

					FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column
                                        + ' FROM hybris.dbo.' + Hybris_Table
                                        + ' a, #tempact b where a.' + @HYB_key
                                        + '=b.' + @RFO_key + '
					except
					SELECT a.' + @RFO_key + ', ' + RFO_Column
                                        + ' FROM Datamigration.' + [Schema]
                                        + '.' + RFO_Table
                                        + ' a, #tempact b where a.' + @RFO_key
                                        + '=b.' + @RFO_key + ') A  

					LEFT JOIN

					(SELECT a.' + @RFO_key + ', ' + RFO_Column
                                        + ' FROM Datamigration.' + [Schema]
                                        + '.' + RFO_Table
                                        + ' a, #tempact b where a.' + @RFO_key
                                        + '=b.' + @RFO_key + '
					except
					SELECT ' + @HYB_key + ', ' + Hybris_Column
                                        + ' FROM hybris.dbo.' + Hybris_Table
                                        + ' a, #tempact b where a.' + @HYB_key
                                        + '=b.' + @RFO_key + ') B
					ON A.' + @HYB_key + '=B.' + @RFO_key + '
					UNION
					SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', '''
                                        + RFO_Column + ''', '''
                                        + Hybris_Column + ''', A.' + @HYB_key
                                        + ', A.' + Hybris_Column + ', B.'
                                        + @RFO_key + ',B.' + RFO_Column + '

					FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column
                                        + ' FROM hybris.dbo.' + Hybris_Table
                                        + ' a, #tempact b where a.' + @HYB_key
                                        + '=b.' + @RFO_key + '
					except
					SELECT a.' + @RFO_key + ', ' + RFO_Column
                                        + ' FROM Datamigration.' + [Schema]
                                        + '.' + RFO_Table
                                        + ' a, #tempact b where a.' + @RFO_key
                                        + '=b.' + @RFO_key + ') A  

					RIGHT JOIN

					(SELECT a.' + @RFO_key + ', ' + RFO_Column
                                        + ' FROM Datamigration.' + [Schema]
                                        + '.' + RFO_Table
                                        + ' a, #tempact b where a.' + @RFO_key
                                        + '=b.' + @RFO_key + '
					except
					SELECT ' + @HYB_key + ', ' + Hybris_Column
                                        + ' FROM hybris.dbo.' + Hybris_Table
                                        + ' a, #tempact b where a.' + @HYB_key
                                        + '=b.' + @RFO_key + ') B
					ON A.' + @HYB_key + '=B.' + @RFO_key + ''
                                FROM    ( SELECT    * ,
                                                    ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                                          FROM      [DataMigration].[dbo].[map_tab]
                                          WHERE     flag = 'c2c'
                                                    AND rfo_column <> @RFO_key
                                                    AND [owner] = '1174-products'
                                        ) temp
                                WHERE   rn = @cnt;

					--print @sql_gen_1
                                INSERT  INTO @temp
                                        ( test_area ,
                                          test_type ,
                                          rfo_column ,
                                          hybris_column ,
                                          hyb_key ,
                                          hyb_value ,
                                          rfo_key ,
                                          rfo_value
                                        )
                                        EXEC sp_executesql @sql_gen_1;


					/* Added this code to get Counting the total of defects.****/

                                IF ( SELECT COUNT(*)
                                     FROM   @temp
                                   ) > 1
                                    BEGIN
                                        DECLARE @err_cnt INT;
      
                                        SELECT  @err_cnt = CASE
                                                              WHEN hyb_cnt = 0
                                                              THEN rfo_cnt
                                                              ELSE hyb_cnt
                                                           END
                                        FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
                                                            COUNT(DISTINCT rfo_key) rfo_cnt
                                                  FROM      @temp
                                                ) t1;

                                        UPDATE  a
                                        SET     [prev_run_err] = @err_cnt
                                        FROM    DataMigration.dbo.map_tab a ,
                                                @temp b
                                        WHERE   a.hybris_column = b.hybris_column
                                                AND [owner] = '1174-products'; 
                                    END;   

                                INSERT  INTO [DataMigration].[dbo].[dm_log]
                                        SELECT TOP 5
                                                test_area ,
                                                test_type ,
                                                rfo_column ,
                                                hybris_column ,
                                                hyb_key ,
                                                hyb_value ,
                                                rfo_key ,
                                                rfo_value
                                        FROM    @temp
                                        WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key,
                                                              '~') )
                                                  AND ( COALESCE(hyb_value,
                                                              '~') <> COALESCE(rfo_value,
                                                              '~') )
                                                )
                                        UNION
                                        SELECT TOP 5
                                                test_area ,
                                                test_type ,
                                                rfo_column ,
                                                hybris_column ,
                                                hyb_key ,
                                                hyb_value ,
                                                rfo_key ,
                                                rfo_value
                                        FROM    @temp
                                        WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key,
                                                              '~') )
                                                  OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                                                );

                                DELETE  FROM @temp;

                                SET @cnt = @cnt + 1;



                            END;


                    END;
								

								
                            UPDATE  DataMigration.dbo.map_tab
                            SET     [prev_run_err] = 0
                            WHERE   [owner] = '1174-Products'
                                    AND flag = 'C2C'
                                    AND Hybris_column NOT IN (
                                    SELECT DISTINCT
                                            hybris_column
                                    FROM    DataMigration.dbo.dm_log
                                    WHERE   test_area = '1174-Products'
                                            AND test_type = 'C2C' );


								

								
						-----================================================
						-- Referential Tables:Productslp and pricerows
						--===================================================
						
						
                    BEGIN
						
					

					--declare @HYB_key varchar(100) = 'pk'
					--declare @RFO_key varchar(100) = 'Hybrispk'
					--declare @sql_gen_1 nvarchar(max)
					--declare @cnt int
					--declare @lt_1 int
					--declare @lt_2 int
					--declare @temp table(test_area varchar(max), test_type varchar(max), rfo_column varchar(max), hybris_column varchar(max), hyb_key varchar(max), hyb_value varchar(max), rfo_key varchar(max), rfo_value varchar(max))

                        SET @cnt = 1;
                        SELECT  @lt_1 = COUNT(*)
                        FROM    [DataMigration].[dbo].[map_tab]
                        WHERE   flag = 'ref'
                                AND rfo_column <> @RFO_key
                                AND [owner] = '1174-Products';

                        WHILE @cnt <= @lt_1
                            BEGIN

                                SELECT  @sql_gen_1 = 'SELECT DISTINCT  '''
                                        + [owner] + ''', ''' + flag + ''', '''
                                        + RFO_Column + ''' as rfo_column, '''
                                        + Hybris_Column
                                        + ''' as hybris_column, A.' + @HYB_key
                                        + ' as hyb_key, A.' + Hybris_Column
                                        + ' as hyb_value, B.' + @RFO_key
                                        + ' as rfo_key, B.' + RFO_Column
                                        + ' as rfo_value

					FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column
                                        + ' FROM ' + Hybris_Table
                                        + ' , #tempact b where a.' + @HYB_key
                                        + '=b.' + @RFO_key + '
					except
					SELECT a.' + @RFO_key + ', ' + RFO_Column
                                        + ' FROM Datamigration.' + [Schema]
                                        + '.' + RFO_Table
                                        + ' a, #tempact b where a.' + @RFO_key
                                        + '=b.' + @RFO_key + ') A  

					LEFT JOIN

					(SELECT a.' + @RFO_key + ', ' + RFO_Column
                                        + ' FROM Datamigration.' + [Schema]
                                        + '.' + RFO_Table
                                        + ' a, #tempact b where a.' + @RFO_key
                                        + '=b.' + @RFO_key + '
					except
					SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM '
                                        + Hybris_Table
                                        + ' , #tempact b where a.' + @HYB_key
                                        + '=b.' + @RFO_key + ') B
					ON A.' + @HYB_key + '=B.' + @RFO_key + '
					UNION
					SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', '''
                                        + RFO_Column + ''', '''
                                        + Hybris_Column + ''', A.' + @HYB_key
                                        + ', A.' + Hybris_Column + ', B.'
                                        + @RFO_key + ',B.' + RFO_Column + '

					FROM (SELECT ' + @HYB_key + ', ' + Hybris_Column
                                        + ' FROM ' + Hybris_Table
                                        + ' , #tempact b where a.' + @HYB_key
                                        + '=b.' + @RFO_key + '
					except
					SELECT a.' + @RFO_key + ', ' + RFO_Column
                                        + ' FROM Datamigration.' + [Schema]
                                        + '.' + RFO_Table
                                        + ' a, #tempact b where a.' + @RFO_key
                                        + '=b.' + @RFO_key + ') A  

					RIGHT JOIN

					(SELECT a.' + @RFO_key + ', ' + RFO_Column
                                        + ' FROM Datamigration.' + [Schema]
                                        + '.' + RFO_Table
                                        + ' a, #tempact b where a.' + @RFO_key
                                        + '=b.' + @RFO_key + '
					except
					SELECT ' + @HYB_key + ', ' + Hybris_Column + ' FROM '
                                        + Hybris_Table
                                        + ' , #tempact b where a.' + @HYB_key
                                        + '=b.' + @RFO_key + ') B
					ON A.' + @HYB_key + '=B.' + @RFO_key + ''
                                FROM    ( SELECT    * ,
                                                    ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                                          FROM      [DataMigration].[dbo].[map_tab]
                                          WHERE     flag = 'ref'
                                                    AND rfo_column <> @RFO_key
                                                    AND [owner] = '1174-products'
                                        ) temp
                                WHERE   rn = @cnt;

					---print @sql_gen_1
                                INSERT  INTO @temp
                                        ( test_area ,
                                          test_type ,
                                          rfo_column ,
                                          hybris_column ,
                                          hyb_key ,
                                          hyb_value ,
                                          rfo_key ,
                                          rfo_value
                                        )
                                        EXEC sp_executesql @sql_gen_1;


					/* Added this code to get Counting the total of defects.****/

                                IF ( SELECT COUNT(*)
                                     FROM   @temp
                                   ) > 1
                                    BEGIN
						  --declare @err_cnt int
      
                                        SELECT  @err_cnt = CASE
                                                              WHEN hyb_cnt = 0
                                                              THEN rfo_cnt
                                                              ELSE hyb_cnt
                                                           END
                                        FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
                                                            COUNT(DISTINCT rfo_key) rfo_cnt
                                                  FROM      @temp
                                                ) t1;

                                        UPDATE  a
                                        SET     [prev_run_err] = @err_cnt
                                        FROM    DataMigration.dbo.map_tab a ,
                                                @temp b
                                        WHERE   a.hybris_column = b.hybris_column
                                                AND [owner] = '1174-products'; 
                                    END;   

                                INSERT  INTO [DataMigration].[dbo].[dm_log]
                                        SELECT TOP 5
                                                test_area ,
                                                test_type ,
                                                rfo_column ,
                                                hybris_column ,
                                                hyb_key ,
                                                hyb_value ,
                                                rfo_key ,
                                                rfo_value
                                        FROM    @temp
                                        WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key,
                                                              '~') )
                                                  AND ( COALESCE(hyb_value,
                                                              '~') <> COALESCE(rfo_value,
                                                              '~') )
                                                )
                                        UNION
                                        SELECT TOP 5
                                                test_area ,
                                                test_type ,
                                                rfo_column ,
                                                hybris_column ,
                                                hyb_key ,
                                                hyb_value ,
                                                rfo_key ,
                                                rfo_value
                                        FROM    @temp
                                        WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key,
                                                              '~') )
                                                  OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                                                );

                                DELETE  FROM @temp;

                                SET @cnt = @cnt + 1;
							
                            END;
                    END;
						
						
                            UPDATE  DataMigration.dbo.map_tab
                            SET     [prev_run_err] = 0
                            WHERE   [owner] = '1174-Products'
                                    AND flag = 'ref'
                                    AND [RFO_Column ] NOT IN (
                                    SELECT DISTINCT
                                            rfo_column
                                    FROM    DataMigration.dbo.dm_log
                                    WHERE   test_area = '1174-Products'
                                            AND test_type = 'ref' );

										
                                            
							




																								
													
											  
			----- To Report Defects as a Result--------------->							
							

                            SELECT  [RFO_Column ] AS ExcelFields ,
                                    ISNULL([RFO_Reference Table], '') AS Comments ,
                                    CASE WHEN Hybris_Table LIKE '%pricerows%'
                                         THEN 'pricerows'
                                         WHEN Hybris_Table LIKE '%productslp%'
                                         THEN 'Productslp'
                                         ELSE 'Products'
                                    END AS Hybris_Tables ,
                                    [Hybris_Column ] ,
                                    flag ,
                                    prev_run_err AS ErrorCounts ,
                                    CASE WHEN (prev_run_err = 0 OR [RFO_Reference Table] IS NOT NULL ) THEN 'Passed'
										WHEN prev_run_err > 0 THEN 'Defects'
                                         ELSE 'N/A'
                                    END AS [Status] 
									--,
         --                           CASE WHEN prev_run_err > 0
         --                                THEN 'Need Validate Defects.'
         --                                ELSE ''
         --                           END AS [ActionProcessed]
                            FROM    DataMigration..map_tab
                            WHERE   [owner] = '1174-products'
                                    AND CASE WHEN (prev_run_err = 0 OR [RFO_Reference Table] IS NOT NULL)
                                             THEN 'Passed'
                                             WHEN prev_run_err > 0
                                             THEN 'Defects'
                                             ELSE 'Progress'
                                        END = 'defects'
										AND [RFO_Reference Table] IS NULL ;

		