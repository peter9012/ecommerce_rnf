DECLARE @LastRun DATETIME '2014-06-01';
DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-06-01' ,
    @RFOCount BIGINT ,
    @RowCount BIGINT ,
    @HybrisPtCount BIGINT ,
    @HybrisCount BIGINT; 
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
--Consignment and Consignment Entries counts Hybris & RFO
------------------------------------------------------------------------------------------------------------------------
SELECT  @RFOCount = COUNT(DISTINCT oi.OrderShipmentPackageItemID)
FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.ETL.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.OrderID
        INNER JOIN RFOperations.Hybris.OrderItem (NOLOCK) ri ON ri.OrderId = o.OrderID
        INNER JOIN Hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderShipmentPackageItem oi ON oi.OrderID = o.OrderID
                                                              --AND ri.OrderItemID = oi.OrderItemId --o.OrderID
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.OrderNumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.AutoshipID IS NULL
        AND od.Startdate >= @ServerMod;

		       

      
SELECT  @HybrisCount = COUNT(*)
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..consignments pt ON o.PK = pt.p_order
        INNER JOIN Hybris..consignmententries pte ON pte.p_consignment = pt.PK
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry;
      

SELECT  'OrderShipmentPackageItems' ,
        @RFOCount AS RFO_Count ,
        @HybrisCount AS Hybris_Count ,
        @RFOCount - @HybrisCount AS Diff;



-------------------------------------------------------------
-- MISSING KEYS
-------------------------------------------------------------

IF OBJECT_ID('Datamigration.migration.Diff_consignment') IS NOT NULL
    DROP TABLE Datamigration.migration.Diff_consignment;
-----------------------------------------------------------------------------------



		SELECT O.ORDERNUMBER+'-'+CAST(OI.PRODUCTID AS NVARCHAR)+'-'+CAST(OSI.TRACKINGNUMBER AS NVARCHAR) AS ConsignmentCombo
		INTO Datamigration.migration.Diff_consignment
		FROM
		RFOPERATIONS.HYBRIS.ORDERSHIPMENT OS,
		RFOPERATIONS.HYBRIS.ORDERSHIPMENTPACKAGEITEM OSI , 
		RFOPERATIONS.HYBRIS.ORDERITEM OI,
		RFOPERATIONS.HYBRIS.ORDERS O
		INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderID = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		WHERE 
			O.ORDERID=OI.ORDERID AND
			CAST(O.ACCOUNTID AS NVARCHAR) IN (SELECT P_RFACCOUNTID FROM HYBRIS.DBO.USERS WHERE P_SOURCeNAME='Hybris-DM') AND
			O.ORDERID IN (SELECT ORDERID FROM RFOPERATIONS.ETL.ORDERDATE WHERE STARTDATE> '2014-05-01') AND
			OI.ORDERITEMID=OSI.ORDERITEMID AND
			OSI.ORDERSHIPMENTID=OS.ORDERSHIPMENTID
			AND O.COUNTRYID=236
		EXCEPT
		SELECT  SUBSTRING(pt.p_code,CHARINDEX('a',pt.p_code,1)+1,100)+'-'+CAST(p.p_rflegacyproductid as NVARCHAR)+'-'+ pte.p_trackingnumber
        FROM      Hybris.dbo.orders o ( NOLOCK )
                  INNER JOIN Hybris..consignments pt ON o.PK = pt.p_order
                  INNER JOIN Hybris..consignmententries pte ON pte.p_consignment = pt.PK
				  INNER JOIN hybris..orderentries OE ON OE.PK=pte.p_orderentry
				  INNER JOIN hybris..products p ON p.pk=OE.productpk
                  WHERE     ( p_template = 0
                                      OR p_template IS NULL
                                    )
                                    AND o.TypePkString <> @ReturnOrderType
                                    AND currencypk = @HybCountry


	SELECT  @RowCount = COUNT(*)
	FROM    DataMigration.Migration.MissingConsignment;
    
	SELECT  ' Total Missing ' + @Country + ' Consignments in Hybris' , @RowCount;
	
	SELECT  ' Query Datamigration.migration.Diff_consignment table to 
	the list of missing consignment and entries. Column value has format OrderNumber-ProductId-TrackingNumber';
	
--p_requestId is unique column across.