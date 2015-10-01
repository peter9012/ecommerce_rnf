
USE DataMigration
go

CREATE PROCEDURE Migration.Migration_Hybris_OrderNotes_QA
AS
BEGIN
 

SET NOCOUNT ON

SET ANSI_WARNINGS OFF 

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED






DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-05-01' ,
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
        INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = CAST(o.OrderNumber AS INT)
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON CAST (u.p_rfaccountid AS BIGINT) = o.AccountID
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
        INNER JOIN RFOperations.etl.OrderDate od WITH ( NOLOCK ) ON od.Orderid = CAST(o.OrderNumber AS INT)
        INNER JOIN hybris..users u WITH ( NOLOCK ) ON CAST (u.p_rfaccountid AS BIGINT) = o.AccountID
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

        SELECT  *
        FROM    DataMigration.Migration.MissingNotes
        ORDER BY MissingFROM;

    END;  
	
	

--------------------------------------------------------------------------------------------------------------------------

IF OBJECT_ID('TEMPDB.dbo.#Note') IS NOT NULL DROP TABLE #Note
IF OBJECT_ID('TEMPDB.dbo.#Hybris_Note') IS NOT NULL DROP TABLE #Hybris_Note
IF OBJECT_ID('TEMPDB.dbo.#RFO_Note') IS NOT NULL DROP TABLE #RFO_Note


IF OBJECT_ID('TEMPDB.dbo.#OrderNotesMigrate') IS NOT NULL DROP TABLE #OrderNotesMigrate


SELECT DISTINCT one.p_noteid AS PK INTO #OrderNotesMigrate
FROM    Hybris.dbo.orders o ( NOLOCK )
        INNER JOIN Hybris..ordernotes ONe ON ONe.p_order = o.pk
WHERE   ( p_template = 0
          OR p_template IS NULL
        )
        AND o.TypePkString <> @ReturnOrderType
        AND currencypk = @HybCountry




-----------------------------------------------------------------------------------------------------------------------------
SELECT
CAST( OrderNoteID AS NVARCHAR (100)) AS  OrderNoteID
,CAST( Notes AS NVARCHAR (100)) AS  Notes
--,CAST( LTRIM(RTRIM(Username)) AS NVARCHAR (100)) AS  Username
,CAST( LTRIM(RTRIM(Subject))AS NVARCHAR (100)) AS  Subject


----------------------------------------------------------
--Derived Columns 
----------------------------------------------------------
,CAST (OrderID AS NVARCHAR(100)) AS OrderID

INTO #RFO_Note
FROM RFoperations.Hybris.OrderNotes a 
WHERE EXISTS   (SELECT 1 FROM #OrderNotesMigrate hon WHERE hon.pk=a.OrderNoteID )

CREATE CLUSTERED INDEX MIX_RFNote ON #RFO_Note (OrderNoteID)


SELECT 
CAST( p_noteid AS NVARCHAR (100)) AS  p_noteid
,CAST( ISNULL(p_ordernotes,'') AS NVARCHAR (100)) AS  p_ordernotes
--,CAST( p_uid AS NVARCHAR (100)) AS  p_uid
,CAST( LTRIM(RTRIM(p_subject)) AS NVARCHAR (100)) AS  p_subject

-------------------------------------------------------------------
--Derived Columns 
-------------------------------------------------------------------
,CAST( p_order AS NVARCHAR (100)) AS  p_order

INTO #Hybris_Note
FROM Hybris.dbo.OrderNotes a
WHERE EXISTS (SELECT 1 FROM #OrderNotesMigrate hon WHERE  hon.pk=a.p_noteid)


CREATE CLUSTERED INDEX MIX_HYNote ON #Hybris_Note (p_noteid)


SELECT * INTO #Note
FROM #RFO_Note
EXCEPT
SELECT * FROM #Hybris_Note


--SELECT COUNT(*) FROM #Note

--SELECT TOP 2 * FROM #Note


CREATE CLUSTERED INDEX MIX_Note ON #Note (OrderNoteID)





TRUNCATE TABLE DataMigration.Migration.ErrorLog_Orders


DECLARE @LastRUN DATETIME ='05/01/1901'

DECLARE @I INT = (SELECT MIN(MapID) FROM DataMigration.Migration.Metadata_Orders WHERE HybrisObject = 'OrderNotes'),
@C INT =  (SELECT MAX(MapID) FROM DataMigration.Migration.Metadata_Orders  WHERE HybrisObject = 'OrderNotes') 


DECLARE @DesKey NVARCHAR (50) 

DECLARE @SrcKey NVARCHAR (50) 

DECLARE @Skip  BIT 

WHILE (@I <=@c)

BEGIN 

        SELECT  @Skip = ( SELECT   Skip
                               FROM    DataMigration.Migration.Metadata_Orders
                               WHERE    MapID = @I
                             );


        IF ( @Skip = 1 )

            SET @I = @I + 1;

        ELSE
BEGIN 



--DECLARE @SrcCol NVARCHAR (50) =(SELECT RFO_Column FROM RFOperations.dbo.Metadata_Accounts WHERE MapID = @I)

DECLARE @DesTemp NVARCHAR (50)  = ( SELECT      CASE WHEN HybrisObject = 'Orders'
                                             THEN '#Hybris_Orders'
                                                                             WHEN HybrisObject = 'OrderEntries' THEN '#Hybris_Item'
                                          WHEN HybrisObject = 'Consignments' OR HybrisObject = 'ConsignmentEntries'     THEN '#Hybris_SPItem'
                                         WHEN HybrisObject = 'PaymentInfos' OR HybrisObject = 'PaymentTransaction'    THEN '#Hybris_Pay'
                                           WHEN HybrisObject = 'paymnttrnsctentries' THEN '#Hybris_Tran'
                                              WHEN HybrisObject = 'Addresses_Billing' THEN '#Hybris_BlAdr'
                                                                              WHEN HybrisObject = 'Addresses' THEN '#Hybris_Note'
                                                                              WHEN HybrisObject = 'OrderNotes' THEN '#Hybris_Note'
                                            
                                        END
                                                              FROM     DataMigration.Migration.Metadata_Orders
                         
                              WHERE     MapID = @I
                            );  

DECLARE @DesCol NVARCHAR (50) =(SELECT Hybris_Column FROM DataMigration.Migration.Metadata_Orders WHERE MapID = @I)

SET @SrcKey= (SELECT RFO_Key
                       FROM DataMigration.Migration.Metadata_Orders
                       WHERE MapID =@I
                                                       )

    
                SET @DesKey = ( SELECT   
                                                              CASE WHEN HybrisObject = 'Consignments'
                                           THEN 'P_order'
                                                                        WHEN HybrisObject = 'OrderNotes'
                                           THEN 'P_noteid'
                                                                     --     WHEN HybrisObject = 'OrderEntries' THEN 'PK'
                                                                     ELSE 'PK'
                                        END
                                FROM     DataMigration.Migration.Metadata_Orders
                                WHERE   MapID = @I
                              ); 


DECLARE @SQL1 NVARCHAR (MAX) = (SELECT SqlStmt FROM DataMigration.Migration.Metadata_Orders WHERE MapID = @I)
DECLARE @SQL2 NVARCHAR (MAX) = ' 
 UPDATE A 
SET a.Hybris_Value = b. ' + @DesCol +
' FROM DAtaMigration.Migration.ErrorLog_Orders a  JOIN ' +@DesTemp+
  ' b  ON a.RecordID= b.' + @DesKey+  
  ' WHERE a.MAPID = ' + CAST(@I AS NVARCHAR)



DECLARE @SQL3 NVARCHAR(MAX) = --'DECLARE @ServerMod DATETIME= ' + ''''+ CAST (@ServMod AS NVARCHAR) + ''''+
' INSERT INTO DAtaMigration.Migration.ErrorLog_Orders (Identifier,MapID,RecordID,RFO_Value) ' + @SQL1  + @SQL2

  BEGIN TRY
   EXEC sp_executesql @SQL3, N'@ServerMod DATETIME', @ServerMod= @LastRun

    --SELECT @SQL3

SELECT @I, GETDATE()
SET @I = @I + 1


END TRY

BEGIN CATCH

SELECT @SQL3

SET @I = @I + 1

END CATCH
END 

END 

--DROP INDEX MIX_SPItem ON #Item
--DROP INDEX MIX_HyItem ON #Hybris_Item 
--DROP INDEX MIX_RFItem ON #RFO_Item

SELECT a.MapID, RFO_column, COUNT(*) FROM DataMigration.Migration.ErrorLog_Orders a JOIN DataMigration.Migration.Metadata_orders b ON a.MapID =b.MapID-- AND b.MapID =24
GROUP BY a.MapID,RFO_column


END