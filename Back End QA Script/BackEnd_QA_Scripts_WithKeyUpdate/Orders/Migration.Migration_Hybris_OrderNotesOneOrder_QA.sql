USE DataMigration
go

CREATE PROCEDURE  Migration.Migration_Hybris_OrderNotesOneOrder_QA @OrdID BIGINT
AS
BEGIN



SET NOCOUNT ON

SET ANSI_WARNINGS OFF 

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED

---------------------------------------------------------------------------------------------




DECLARE @Country NVARCHAR(20)= 'US';
DECLARE @ServerMod DATETIME = '2014-06-01' ,
    @RFOCount BIGINT , @OrderID BIGINT =@OrdID,
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
		AND o.PK =@OrderID

-----------------------------------------------------------------------------------------------------------------------------
SELECT
CAST( OrderNoteID AS NVARCHAR (100)) AS  OrderNoteID
,CAST( LTRIM(RTRIM(Notes)) AS NVARCHAR (100)) AS  Notes
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
,CAST( p_subject AS NVARCHAR (100)) AS  p_subject

-------------------------------------------------------------------
--Derived Columns 
-------------------------------------------------------------------
,CAST( p_order AS NVARCHAR (100)) AS  p_order

INTO #Hybris_Note
FROM Hybris.dbo.OrderNotes a
WHERE EXISTS (SELECT 1 FROM #OrderNotesMigrate hon WHERE  hon.pk=a.p_noteid)


CREATE CLUSTERED INDEX MIX_HYNote ON #Hybris_Note (p_noteid)


SELECT * FROM #RFO_Note
--EXCEPT
SELECT * FROM #Hybris_Note
END 