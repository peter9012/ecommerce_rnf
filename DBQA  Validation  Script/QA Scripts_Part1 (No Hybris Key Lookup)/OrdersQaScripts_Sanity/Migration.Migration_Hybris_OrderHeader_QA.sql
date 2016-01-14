DECLARE @LastRun DATETIME = '06/01/2014';DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = @LastRun ,
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

    
----------------------------Validate Order Header -----------------------------------------

IF OBJECT_ID('DataMigration.Migration.OrdersMissing') IS NOT NULL
    DROP TABLE DataMigration.Migration.OrdersMissing; 

IF OBJECT_ID('TEMPDB.dbo.#Orders_Dups') IS NOT NULL
    DROP TABLE #Orders_Dups; 

--------------------------------------------
-- OrderHeader  COUNTS 
-----------------------------------------

SELECT  COUNT(*)
FROM    hybris..orders
WHERE   currencypk IS NULL
--It should be zero

SELECT  COUNT(*)
FROM    RFOperations.etl.OrderDate
WHERE   Startdate IS NULL
--It should be zero 

/* This section commented out since we do not want to refer to Hybris PK in this phase of validation
SELECT  COUNT(*)
FROM    RFOperations.Hybris.Orders a
        JOIN Hybris.dbo.Orders b ON a.OrderID = b.PK
        LEFT JOIN RFOperations.Hybris.AutoShip c ON c.AutoshipID = a.OrderID
WHERE   p_template = 1
        AND c.AutoshipID IS NULL
        AND a.CountryID = 236
--It should be zero 

SELECT  COUNT(*)
FROM    RFOperations.Hybris.Orders a
        JOIN Hybris.dbo.Orders b ON a.OrderID = b.PK
        LEFT JOIN RFOperations.Hybris.AutoShip c ON c.AutoshipID = a.OrderID
WHERE   b.p_origination <> 'www.rodanandfields.com'
        AND c.AutoshipID IS NULL
        AND a.CountryID = 236
--It should be zero 

*/
SELECT  @RFOCount = COUNT(DISTINCT o.OrderID)
FROM    RFOperations.Hybris.Orders o
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od ON od.Orderid = o.OrderID
        INNER JOIN hybris..users u ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
        LEFT JOIN RFOperations.Hybris.Autoship a ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
		AND u.p_sourcename='Hybris-DM' 
        AND od.startdate >= @ServerMod
		AND o.ServerModifiedDate> @LastRun
             
SELECT  @HybrisCount = COUNT(DISTINCT PK)
FROM    Hybris.dbo.orders (NOLOCK)
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry 
		AND modifiedTS > @LastRun
        
       
  


SELECT  'Orders(Header)' ,
        @RFOCount AS RFO_Count ,
        @HybrisCount AS Hybris_Count ,
        @RFOCount - @HybrisCount AS DIFF; 


----------------------------------------------------------
       -- Missing Keys 
----------------------------------------------------------

SELECT  OrderNumber AS RFO_OrderID ,
        code AS Hybris_OrderID ,
        CASE WHEN b.code IS NULL THEN 'Destination'
             WHEN a.OrderNumber IS NULL THEN 'Source'
        END AS MissingFROM
INTO    DataMigration.Migration.OrdersMissing
FROM    ( SELECT  DISTINCT
                    o.OrderNumber
          FROM      RFOperations.Hybris.Orders o
                    INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
					INNER JOIN RFOperations.etl.OrderDate od ON od.Orderid = o.OrderID
                    INNER JOIN hybris..users u ON u.p_rfaccountid  = CAST(o.AccountID AS NVARCHAR)
                    LEFT JOIN RFOperations.Hybris.Autoship a ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
          WHERE     o.CountryID = @RFOCountry
                    AND a.autoshipid IS NULL
					AND u.p_sourcename='Hybris-DM'
                    AND od.startdate >= @ServerMod
        ) a
        FULL OUTER JOIN ( SELECT    code
                          FROM      Hybris.dbo.orders(NOLOCK)
                          WHERE     ( p_template = 0
                                      OR p_template IS NULL
                                    )
                                    AND TypePkString <> @ReturnOrderType
                                    AND currencypk = @HybCountry
									AND modifiedTS>@ServerMod
                        ) b ON a.OrderNumber = b.code
WHERE   ( a.OrderNumber IS NULL
          OR b.code IS NULL
        ); 

SELECT  @RowCount = COUNT(*)
FROM    DataMigration.Migration.OrdersMissing

IF ( @RowCount > 0 )
    BEGIN 

        SELECT  ' Total Missing ' + @Country + ' Orders' ,
                @RowCount;

        SELECT  'MissingOrders' ,
                MissingFROM ,
                COUNT(*)
        FROM    DataMigration.Migration.OrdersMissing
        GROUP BY MissingFROM;


    END;  
