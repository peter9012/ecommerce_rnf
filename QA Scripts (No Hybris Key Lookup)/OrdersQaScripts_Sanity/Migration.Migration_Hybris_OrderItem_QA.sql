DECLARE @LastRun DATETIME = '2014-06-01'
        DECLARE @Country NVARCHAR(20)= 'US';
        DECLARE @ServerMod DATETIME = '2014-06-01' ,
            @RFOCount BIGINT ,
            @RowCount BIGINT ,
            @HybrisCount BIGINT 
        DECLARE @RFOCountry INT = ( SELECT  CountryID
                                    FROM    RFOperations.RFO_Reference.Countries (NOLOCK)
                                    WHERE   Alpha2Code = @Country
                                  ) ,
            @HybCountry BIGINT = ( SELECT   PK
                                   FROM     Hybris.dbo.currencies (NOLOCK)
                                   WHERE    isocode = 'USD'
                                 );
        DECLARE @ReturnOrderType BIGINT = ( SELECT  PK
                                            FROM    Hybris.dbo.composedtypes (NOLOCK)
                                            WHERE   InternalCode = 'RFReturnOrder'
                                          );


-----------------------------------------------------------------------------------------------------------------------
--Orderitem counts HYbris & RFO

        SELECT  @RFOCount = 
		COUNT(DISTINCT oi.OrderItemID)
        FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
                INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
				INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR) AND U.P_SOURCENAME='Hybris-DM'
                INNER JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
                --INNER JOIN hybris..products p ON p.p_rflegacyproductid = oi.ProductID
                --INNER JOIN Hybris..orders ho ON ho.code = o.OrderNumber
                LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
        WHERE   o.CountryID = @RFOCountry
                AND a.autoshipid IS NULL
                AND od.startdate >= @ServerMod
				AND od.ServerModifiedDate> @LastRun
                --AND p.p_catalog = 8796093088344
                --AND p.p_catalogversion = 8796093153881

       

        SELECT  @HybrisCount = 
		COUNT(DISTINCT oe.PK)
        FROM    Hybris.dbo.orders o ( NOLOCK )
                INNER JOIN Hybris..orderentries oe ON oe.orderpk = o.pk AND O.userpk in (select pk from hybris..users where P_SOURCENAME='Hybris-DM')
        WHERE   ( p_template = 0
                  OR p_template IS NULL
                )
                AND o.TypePkString <> @ReturnOrderType
                AND currencypk = @HybCountry 
				AND oe.modifiedTS> @LastRun
        
		
        SELECT  'OrderItems' ,
                @RFOCount AS RFO_Count ,
                @HybrisCount AS Hybris_Count ,
                @RFOCount - @HybrisCount AS Diff
				
-------------------------------------------------------------------------------------------------------------------------
-- Missing 
 -----------------------------------------------------------------------------------------------------------------------------


        IF OBJECT_ID('DataMigration.Migration.OrderItemsMissing') IS NOT NULL
            DROP TABLE DataMigration.Migration.OrderItemsMissing;
			
	    IF OBJECT_ID('DATAMIGRATION.MIGRATION.DIFF_COMBO') IS NOT NULL
            DROP TABLE DATAMIGRATION.MIGRATION.DIFF_COMBO;
			
		IF OBJECT_ID('DATAMIGRATION.MIGRATION.RFO_COMBO') IS NOT NULL
            DROP TABLE DATAMIGRATION.MIGRATION.RFO_COMBO;
			
		IF OBJECT_ID('DATAMIGRATION.MIGRATION.HYB_COMBO') IS NOT NULL
            DROP TABLE DATAMIGRATION.MIGRATION.HYB_COMBO;



        SELECT CAST(O.ORDERNUMBER AS NVARCHAR(MAX))+'-'+CAST(OI.PRODUCTID AS NVARCHAR(MAX))+'-'+CAST(CAST(QUANTITY AS DECIMAL(30,8)) AS NVARCHAR(MAX))+'-'+CAST(CAST(TOTALPRICE AS DECIMAL(30,8)) AS NVARCHAR(MAX)) as OrderItemId
			INTO DATAMIGRATION.MIGRATION.RFO_COMBO
                  FROM      RFOperations.Hybris.Orders o WITH ( NOLOCK )
                            INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
							INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
                            INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR) AND U.P_SOURCENAME='Hybris-DM'
                            INNER JOIN RFOperations.Hybris.OrderItem oi ON oi.OrderId = o.OrderID
                            LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
                  WHERE     o.CountryID = @RFOCountry
                            AND a.autoshipid IS NULL
                            AND od.startdate >= @ServerMod
                            
                
        SELECT  CAST(o.code AS NVARCHAR(MAX))+'-'+CAST(p_rflegacyproductid AS NVARCHAR(MAX))+'-'+CAST(QUANTITY AS NVARCHAR(MAX))+'-'+CAST(oe.totalprice AS NVARCHAR(MAX)) as PK
			INTO DATAMIGRATION.MIGRATION.HYB_COMBO
                FROM      Hybris.dbo.orders o ( NOLOCK )
                          INNER JOIN Hybris..orderentries oe ON oe.orderpk = o.pk AND O.userpk in (select pk from hybris..users where P_SOURCENAME='Hybris-DM')
                WHERE     ( p_template = 0
							OR p_template IS NULL
                           )
                AND o.TypePkString <> @ReturnOrderType
                AND currencypk = @HybCountry
               
        SELECT *
		INTO  DATAMIGRATION.MIGRATION.DIFF_COMBO
		FROM  DATAMIGRATION.MIGRATION.RFO_COMBO
		EXCEPT
		SELECT * FROM DATAMIGRATION.MIGRATION.HYB_COMBO

        SELECT  @RowCount = COUNT(*)
        FROM    DataMigration.Migration.DIFF_COMBO;

        
              