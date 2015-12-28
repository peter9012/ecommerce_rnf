DECLARE @LastRun DATETIME = '2014-06-01';

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
--OrderNotes  counts Hybris & RFO
------------------------------------------------------------------------------------------------------------------------
SELECT  @RFOCount= COUNT(oi.OrderNoteID)
FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = o.orderNumber
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = cast(o.AccountId as nvarchar)
        INNER JOIN RFOperations.Hybris.OrderNotes oi ON o.OrderID=oi.OrderID
        INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod
                     

SELECT  @HybrisCount= COUNT(DISTINCT one.PK )
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..ordernotes ONe ON ONe.p_order = o.pk
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry




SELECT  'OrderNotes' ,
        @RFOCount AS RFO_Count ,
        @HybrisCount AS Hybris_Count ,
        @RFOCount - @HybrisCount AS Diff;
	
-------------------------------------------------------------
-- MISSING KEYS
-------------------------------------------------------------

IF OBJECT_ID('DataMigration.Migration.MissingNotes') IS NOT NULL
    DROP TABLE DataMigration.Migration.MissingNotes;
-----------------------------------------------------------------------------------



SELECT  a.OrderNoteID AS RFO_NoteID,
        p_noteid AS Hybris_NoteID,
	 CASE WHEN c.p_noteid IS NULL THEN 'Destination'
             WHEN a.OrderNoteID IS NULL THEN 'Source'
        END AS MissingFROM
INTO    DataMigration.Migration.MissingNotes
FROM    ( SELECT   oi.OrderNoteID
          FROM    RFOperations.Hybris.Orders o WITH ( NOLOCK )
        INNER JOIN RodanFieldsLive.dbo.Orders rfl ON O.OrderNumber = rfl.orderID
                                                             AND rfl.orderTypeID NOT IN (4, 5, 9 )
                                                             AND rfl.StartDate >= @ServerMod
                                                             AND O.CountryID = @RFOCountry 
		INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = CAST(o.OrderNumber AS INT)
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON u.p_rfaccountid = CAST(o.AccountID AS NVARCHAR)
        INNER JOIN RFOperations.Hybris.OrderNotes oi ON o.OrderID=oi.OrderID
        INNER JOIN Hybris..orders ho ON ho.pk = o.OrderID
        LEFT JOIN RFOperations.Hybris.Autoship a WITH ( NOLOCK ) ON CAST(a.AutoshipNumber AS INT) = CAST (o.ordernumber AS INT)
WHERE   o.CountryID = @RFOCountry
        AND a.autoshipid IS NULL
        AND od.startdate >= @ServerMod

        ) a

		 FULL OUTER JOIN 

       ( SELECT    one.p_noteid
        FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..ordernotes ONe ON ONe.p_order = o.pk
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry


                     ) c ON   a.OrderNoteID = c.p_noteid --a.OrderPaymentID = c.p_paymenttransaction AND
WHERE   a.OrderNoteID IS NULL
        OR c.p_noteid IS NULL; 



SELECT  @RowCount = COUNT(*)
FROM    DataMigration.Migration.MissingNotes;

IF ( @RowCount > 0 )
    BEGIN 

        SELECT  ' Total Missing ' + @Country + ' Payment Notes' ,
                @RowCount;

        SELECT  MissingFROM ,
                COUNT(*)
        FROM    DataMigration.Migration.MissingNotes
        GROUP BY MissingFROM;

            END;  
	
	