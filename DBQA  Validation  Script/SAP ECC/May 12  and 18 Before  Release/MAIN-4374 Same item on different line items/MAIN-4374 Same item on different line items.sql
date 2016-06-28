


--====================================================================================
--CHECKING DUPLICATE IETMS IN  PC Auto-ship Template and Consultant Auto-ship Template
--====================================================================================
USE RodanFieldsLive
SELECT  o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU ,
        COUNT(*) AS 'Repeat'
FROM    dbo.Orders o
        JOIN OrderCustomers(NOLOCK) oc ON o.OrderID = oc.OrderID
        JOIN OrderItems (NOLOCK)oi ON oi.OrderCustomerID = oc.OrderCustomerID
WHERE   o.OrderTypeID IN ( 4, 5 )
        AND o.OrderStatusID IN (4,7)
GROUP BY o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU
HAVING  COUNT(*) > 1
ORDER BY oc.OrderCustomerID DESC
--43

--===================================================================
--CHECKING DUPLICATE IETMS IN  PC Auto-ship Consultant Auto-ship – 8
--===================================================================

SELECT  o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU ,
        COUNT(*) AS 'Repeat'
FROM    dbo.Orders o
        JOIN OrderCustomers oc ON o.OrderID = oc.OrderID
        JOIN OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
WHERE   o.OrderTypeID IN ( 6, 7 )
        AND o.OrderStatusID IN (4,7)
GROUP BY o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU
HAVING  COUNT(*) > 1
ORDER BY oc.OrderCustomerID DESC

--8 only 7 
--1210 with 4,7 
--===================================================================
--–CHECKING DUPLICATE IETMS IN  Retail PC Consultant – 150 Rows
--===================================================================

SELECT  o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
		o.OrderStatusID,
        oi.SKU ,
        COUNT(*) AS 'Repeat' ,
        o.StartDate ,
        o.CommissionDate
FROM    dbo.Orders o
        JOIN OrderCustomers oc ON o.OrderID = oc.OrderID
        JOIN OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
WHERE   o.OrderTypeID IN ( 1, 2, 3 )
        AND o.OrderStatusID = 4 --AND o.StartDate > '2016-01-01 00:00:00.000'
GROUP BY o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
			o.OrderStatusID,
        oi.SKU ,
        o.StartDate ,
        o.CommissionDate
HAVING  COUNT(*) > 1
ORDER BY oc.OrderCustomerID DESC;

--150


--==============================================================
--			RETURN ORDERS HAVING DUPLICATE ITEMS IN ITEMS TABLE
--==============================================================

USE RodanFieldsLive
GO 
--–CHECKING DUPLICATE IETMS IN  RETURN ITEM :
SELECT  o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
			o.OrderStatusID,
        oi.SKU ,
        COUNT(*) AS 'Repeat' ,
        o.StartDate ,
        o.CommissionDate
FROM    dbo.Orders o
        JOIN OrderCustomers oc ON o.OrderID = oc.OrderID
        JOIN OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
WHERE   o.OrderTypeID IN ( 9)
        AND o.OrderStatusID = 4 --AND o.StartDate > '2016-01-01 00:00:00.000'
GROUP BY o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
			o.OrderStatusID,
        oi.SKU ,
        o.StartDate ,
        o.CommissionDate
HAVING  COUNT(*) > 1
ORDER BY oc.OrderCustomerID DESC;

--67

SELECT * FROM RFOperations.Hybris.ReturnOrder WHERE ReturnOrderID=11378684
SELECT * FROM RFOperations.Hybris.ReturnItem WHERE ReturnOrderID=11378684
SELECT * FROM RFOperations.Hybris.OrderItem WHERE OrderId=11040821


SELECT ro.CompleteDate,ro.OrderID,ro.OrderStatusID,ro.OrderTypeID, ot.* FROM RodanFieldsLive.dbo.orders ro
JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID=ro.OrderID
JOIN RodanFieldsLive.dbo.OrderItems ot ON ot.OrderCustomerID=oc.OrderCustomerID 
WHERE ro.OrderID=15892122
SELECT ro.CompleteDate,ro.ParentOrderID,ro.OrderStatusID,ro.OrderTypeID, ot.* FROM RodanFieldsLive.dbo.orders ro
JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID=ro.OrderID
JOIN RodanFieldsLive.dbo.OrderItems ot ON ot.OrderCustomerID=oc.OrderCustomerID 
WHERE ro.OrderID=16303254
--======================================================================================

SELECT  oi.*
FROM    RodanFieldsLive.dbo.Orders o
        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON o.OrderID = oc.OrderID
        JOIN RodanFieldsLive.dbo.OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
WHERE   o.OrderTypeID IN ( 4, 5 )
        AND o.OrderStatusID = 7
		AND  o.OrderID=19342561

--===========================================================================

--DROP TABLE RFOperations.dbo.[MAIN-4374]

--STEP 1: TAKING BACK UP OF TABLE BEFORE UPDATE. (STG2--102: RFOperations.dbo.[MAIN-4374])

USE RodanFieldsLive
SELECT  o.OrderID ,
        oc.OrderCustomerID ,
		o.OrderStatusID,
		o.OrderTypeID,
        oi.ProductID ,
		oi.Quantity,
        oi.SKU ,CASE WHEN o.OrderTypeID IN ( 4, 5) THEN 'TEMPLATES Orders'
		WHEN o.OrderTypeID IN (6,7) THEN 'PC/C Orders'
		WHEN o.OrderTypeID IN (1,2,3) THEN 'RC Orders'
		WHEN o.OrderTypeID IN (9) THEN 'Return Orders' END AS OrderTypes,
        COUNT(*) AS 'Repeat'  --INTO RFOperations.dbo.[MAIN-4374]
FROM    dbo.Orders o
        JOIN OrderCustomers oc ON o.OrderID = oc.OrderID
        JOIN OrderItems oi ON oi.OrderCustomerID = oc.OrderCustomerID
WHERE   ( o.OrderTypeID IN ( 4, 5, 6, 7,1, 2, 3,9 )
          AND o.OrderStatusID IN (4, 7)
        )
       
GROUP BY o.OrderID ,
        oc.OrderCustomerID ,
		o.OrderStatusID,
		o.OrderTypeID,
        oi.ProductID ,
		oi.Quantity,
        oi.SKU,CASE WHEN o.OrderTypeID IN ( 4, 5) THEN 'TEMPLATES Orders'
		WHEN o.OrderTypeID IN (6,7) THEN 'PC/C Orders'
		WHEN o.OrderTypeID IN (1,2,3) THEN 'RC Orders'
		WHEN o.OrderTypeID IN (9) THEN 'Return Orders' END
HAVING  COUNT(*) > 1
ORDER BY oc.OrderCustomerID DESC

--==============================================================
--		BACK UP TABLE IN STG2 
--==============================================================


SELECT 'BackUpTable' [Table],* FROM RFOperations.dbo.[MAIN-4374] o
WHERE o.OrderTypeID IN ( 4, 5 )
        AND o.OrderStatusID IN (4,7) AND o.OrderID=7933451

	
USE RodanFieldsLive;
SELECT  DISTINCT o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU ,
		B.SKU AS BackUpSKU,
		oi.Quantity,
		B.Quantity BackUpQuantity,
		B.[Repeat],
		B.Quantity*B.[Repeat] AS DerivedQuantity,
        CASE WHEN o.OrderTypeID IN ( 4, 5 ) THEN 'TEMPLATES Orders'
             WHEN o.OrderTypeID IN ( 6, 7 ) THEN 'PC/C Orders'
             WHEN o.OrderTypeID IN ( 1, 2, 3 ) THEN 'RC Orders'
             WHEN o.OrderTypeID IN ( 9 ) THEN 'Return Orders'
        END AS OrderTypes 
       
FROM    dbo.Orders (NOLOCK) o
        JOIN dbo.OrderCustomers (NOLOCK) oc ON o.OrderID = oc.OrderID
        JOIN dbo.OrderItems (NOLOCK) oi ON oi.OrderCustomerID = oc.OrderCustomerID
        JOIN RFOperations.dbo.[MAIN-4374] B ON B.OrderID = o.OrderID
WHERE   oi.SKU = B.SKU
        AND  oi.Quantity<>B.Quantity*B.[Repeat]
        AND o.OrderTypeID IN ( 4, 5 )
        AND o.OrderStatusID IN ( 4, 7 )


-- CHECKING IF THERE ARE ANY TEMPLATES HAVING DUPLICATE ISSUES 

USE RodanFieldsLive;
SELECT  DISTINCT o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU ,
		B.SKU AS BackUpSKU,
		oi.Quantity,
		B.Quantity BackUpQuantity,
		B.[Repeat],
		B.Quantity*B.[Repeat] AS DerivedQuantity,
        CASE WHEN o.OrderTypeID IN ( 4, 5 ) THEN 'TEMPLATES Orders'
             WHEN o.OrderTypeID IN ( 6, 7 ) THEN 'PC/C Orders'
             WHEN o.OrderTypeID IN ( 1, 2, 3 ) THEN 'RC Orders'
             WHEN o.OrderTypeID IN ( 9 ) THEN 'Return Orders'
        END AS OrderTypes 
       
FROM    dbo.Orders (NOLOCK) o
        JOIN dbo.OrderCustomers (NOLOCK) oc ON o.OrderID = oc.OrderID
        JOIN dbo.OrderItems (NOLOCK) oi ON oi.OrderCustomerID = oc.OrderCustomerID
        JOIN RFOperations.dbo.[MAIN-4374] B ON B.OrderID = o.OrderID
WHERE   oi.SKU = B.SKU
        AND  oi.Quantity<>B.Quantity*B.[Repeat]
        AND o.OrderTypeID IN ( 4, 5 )
        AND o.OrderStatusID IN ( 4, 7 )
      

	  SELECT * FROM RodanFieldsLive.dbo.OrderItems WHERE OrderCustomerID=7874908 AND SKU='UNWA125'

-- CHECKING IF THERE ARE ANY NEW ORDERS HAVING THE SAME ISSUES 

USE RodanFieldsLive
SELECT  o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU ,CASE WHEN o.OrderTypeID IN ( 4, 5) THEN 'TEMPLATES Orders'
		WHEN o.OrderTypeID IN (6,7) THEN 'PC/C Orders'
		WHEN o.OrderTypeID IN (1,2,3) THEN 'RC Orders'
		WHEN o.OrderTypeID IN (9) THEN 'Return Orders' END AS OrderTypes,
        COUNT(*) AS 'Repeat' 
FROM    dbo.Orders(NOLOCK)o
        JOIN dbo.OrderCustomers (NOLOCK) oc ON o.OrderID = oc.OrderID
        JOIN dbo.OrderItems (NOLOCK) oi ON oi.OrderCustomerID = oc.OrderCustomerID
WHERE  O.CompleteDate>GETDATE()-5 AND 
 (( o.OrderTypeID IN ( 4, 5, 6, 7 )
          AND o.OrderStatusID = 7
        )
        OR ( o.OrderTypeID IN ( 1, 2, 3,9 )
             AND o.OrderStatusID = 4
           ))
GROUP BY o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU,CASE WHEN o.OrderTypeID IN ( 4, 5) THEN 'TEMPLATES Orders'
		WHEN o.OrderTypeID IN (6,7) THEN 'PC/C Orders'
		WHEN o.OrderTypeID IN (1,2,3) THEN 'RC Orders'
		WHEN o.OrderTypeID IN (9) THEN 'Return Orders' END
HAVING  COUNT(*) > 1
ORDER BY oc.OrderCustomerID DESC


--============================================================
--CHECKING RFO SIDES FOR TEMPLATE HAVING DUPLICATE LINE ITEM.
--============================================================

SELECT  ai.AutoshipId ,
        ai.ProductID ,
        pb.SKU ,
        COUNT(*) AS 'Repeat' ---INTO #t 
FROM    RFOperations.Hybris.Autoship a
        JOIN RFOperations.Hybris.AutoshipItem ai ON ai.AutoshipId = a.AutoshipID
        JOIN RFOperations.Hybris.ProductBase pb ON pb.productID = ai.ProductID
WHERE   a.CountryID = 236
        AND a.Active = 1
        AND a.AutoshipStatusID = 2
        AND EXISTS ( SELECT 1
                     FROM   RFOperations.RFO_Accounts.AccountRF rf
                     WHERE  rf.AccountID = a.AccountID
                            AND rf.Active = 1 )
GROUP BY ai.AutoshipId ,
        ai.ProductID ,
        pb.SKU
HAVING  COUNT(*) > 1;
        -- 124 IN DM .




--=====================================================================
--TAKING BACK UP THOSE TEMPLATES HAVING DUPLICATE ITEM ISSUES FROM STG2
--=====================================================================


IF OBJECT_ID(N'TEMPDB..#STG2')IS NOT NULL 
DROP TABLE #STG2


CREATE TABLE #STG2
    (
      OrderId INT ,
      OrderCumstorID INT ,
      ProductID INT ,
      SKU VARCHAR(10) ,
      Repeat INT
    );

INSERT  INTO #STG2
        ( OrderId, OrderCumstorID, ProductID, SKU, Repeat )
VALUES  ( 19342561, 19279727, 232, 'AAWA125', 2 ),
        ( 19342561, 19279727, 233, 'AATN125', 2 ),
        ( 19311358, 19248524, 232, 'AAWA125', 2 ),
        ( 19311358, 19248524, 233, 'AATN125', 2 ),
        ( 19311358, 19248524, 332, 'ESLS002', 2 ),
        ( 18753921, 18691133, 21, 'SORG001', 2 ),
        ( 18563551, 18500775, 19, 'UNTT030', 2 ),
        ( 18563551, 18500775, 167, 'UNSS030', 2 ),
        ( 18367399, 18304637, 15, 'AASR060', 2 ),
        ( 17282187, 17219482, 14, 'AAEY015', 2 ),
        ( 16935681, 16872989, 610, 'AAPRS01', 2 ),
        ( 16582753, 16520087, 709, 'AAAC010', 2 ),
        ( 16377852, 16315211, 332, 'ESLS002', 2 ),
        ( 16365357, 16302721, 14, 'AAEY015', 2 ),
        ( 16365357, 16302721, 159, 'ENPM004', 2 ),
        ( 15870784, 15808192, 333, 'ESST125', 2 ),
        ( 15546987, 15484433, 618, 'RVRGG01', 2 ),
        ( 14935901, 14873467, 709, 'AAAC010', 2 ),
        ( 14898198, 14835769, 256, 'AARG001', 2 ),
        ( 14818690, 14756976, 14, 'AAEY015', 2 ),
        ( 14809741, 14748030, 167, 'UNSS030', 2 ),
        ( 14809741, 14748030, 319, 'UNWA125', 2 ),
        ( 14113220, 14051626, 393, 'AALS060', 2 ),
        ( 13827021, 13765540, 14, 'AAEY015', 2 ),
        ( 13750027, 13688557, 610, 'AAPRS01', 2 ),
        ( 13557133, 13495700, 256, 'AARG001', 2 ),
        ( 11997307, 11936429, 256, 'AARG001', 2 ),
        ( 11242473, 11182082, 19, 'UNTT030', 2 ),
        ( 11242473, 11182082, 233, 'AATN125', 2 ),
        ( 11242473, 11182082, 232, 'AAWA125', 2 ),
        ( 11011914, 10951690, 256, 'AARG001', 2 ),
        ( 8834515, 8775664, 256, 'AARG001', 2 ),
        ( 7933451, 7874908, 319, 'UNWA125', 2 ),
        ( 7185235, 7132394, 95, 'AAEY030', 2 ),
        ( 7061036, 7008215, 618, 'RVRGG01', 2 ),
        ( 6975035, 6922222, 233, 'AATN125', 2 ),
        ( 6975035, 6922222, 333, 'ESST125', 2 ),
        ( 6975035, 6922222, 232, 'AAWA125', 2 ),
        ( 6775188, 6722397, 759, 'UNRCAC1', 2 ),
        ( 5049680, 4997182, 233, 'AATN125', 2 ),
        ( 4633621, 4581155, 237, 'AAPM030', 2 ),
        ( 4633621, 4581155, 236, 'AATT030', 2 ),
        ( 3734283, 3681987, 617, 'RVTTG50', 2 );
		--43 

		--SELECT * FROM #STG2

		--================================================
		--VALIDATING IN QA BOX WHERE THE FIX APPLIED.
		--================================================

		USE RodanFieldsLive
SELECT  o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU,ST.[Repeat],oi.Quantity
FROM    dbo.Orders o
        JOIN OrderCustomers(NOLOCK) oc ON o.OrderID = oc.OrderID
        JOIN OrderItems (NOLOCK)oi ON oi.OrderCustomerID = oc.OrderCustomerID
		JOIN #STG2 ST ON ST.OrderId=o.OrderID AND ST.SKU=oi.SKU
WHERE   o.OrderTypeID IN ( 4, 5 )
        AND o.OrderStatusID IN(4,7)
		AND oi.Quantity<>ST.[Repeat]
	--There was Two Template having duplicates , it looks like data inconsistency 


	
--=====================================================================
--TAKING BACK UP THOSE TEMPLATES HAVING DUPLICATE ITEM ISSUES FROM QA
--=====================================================================


IF OBJECT_ID(N'TEMPDB..#QA')IS NOT NULL 
DROP TABLE #QA;


CREATE TABLE #QA
    (
      OrderId INT ,
      OrderCumstorID INT ,
      ProductID INT ,
      SKU VARCHAR(10) ,
      Repeat INT
    );

INSERT  INTO #QA
        ( OrderId, OrderCumstorID, ProductID, SKU, Repeat )
VALUES  ( 20229867, 20166929, 14, 'AAEY015', 2 )	,
        ( 20229867, 20166929, 95, 'AAEY030', 2 )	,
        ( 19311358, 19248524, 332, 'ESLS002', 2 )	,
        ( 19311358, 19248524, 233, 'AATN125', 2 )	,
        ( 19311358, 19248524, 232, 'AAWA125', 2 )	,
        ( 18899395, 18836594, 604, 'RVWA125', 2 )	,
        ( 18735005, 18672220, 616, 'RVTG125', 2 )	,
        ( 18735005, 18672220, 617, 'RVTTG50', 2 )	,
        ( 18367399, 18304637, 15, 'AASR060', 2 )	,
        ( 16582753, 16520087, 709, 'AAAC010', 2 )	,
        ( 16377852, 16315211, 332, 'ESLS002', 2 )	,
        ( 16365357, 16302721, 14, 'AAEY015', 2 )	,
        ( 16365357, 16302721, 159, 'ENPM004', 2 )	,
        ( 15870784, 15808192, 333, 'ESST125', 2 )	,
        ( 15546987, 15484433, 618, 'RVRGG01', 2 )	,
        ( 14935901, 14873467, 709, 'AAAC010', 2 )	,
        ( 14809741, 14748030, 167, 'UNSS030', 2 )	,
        ( 14809741, 14748030, 319, 'UNWA125', 2 )	,
        ( 14113220, 14051626, 393, 'AALS060', 2 )	,
        ( 13827021, 13765540, 14, 'AAEY015', 2 )	,
        ( 13557133, 13495700, 256, 'AARG001', 2 )	,
        ( 11997307, 11936429, 256, 'AARG001', 2 )	,
        ( 11742252, 11681518, 232, 'AAWA125', 2 )	,
        ( 11322403, 11261928, 14, 'AAEY015', 2 )	,
        ( 11322403, 11261928, 29, 'ESGA001', 2 )	,
        ( 11322403, 11261928, 333, 'ESST125', 2 )	,
        ( 11242473, 11182082, 232, 'AAWA125', 2 )	,
        ( 11242473, 11182082, 233, 'AATN125', 2 )	,
        ( 11242473, 11182082, 19, 'UNTT030', 2 )	,
        ( 8834515, 8775664, 256, 'AARG001', 2 )	,
        ( 7933451, 7874908, 319, 'UNWA125', 2 )	,
        ( 7061036, 7008215, 618, 'RVRGG01', 2 )	,
        ( 6975035, 6922222, 333, 'ESST125', 2 )	,
        ( 6975035, 6922222, 232, 'AAWA125', 2 )	,
        ( 6975035, 6922222, 233, 'AATN125', 2 )	,
        ( 5049680, 4997182, 233, 'AATN125', 2 )	,
        ( 3734283, 3681987, 617, 'RVTTG50', 2 );	

		--SELECT * FROM #QA



			USE RodanFieldsLive
SELECT  o.OrderID ,
        oc.OrderCustomerID ,
        oi.ProductID ,
        oi.SKU,ST.[Repeat],oi.Quantity,
		CASE WHEN ST.[Repeat]=oi.Quantity THEN 'Passed'
		ELSE 'Failed' END AS Rusult
FROM    dbo.Orders o
        JOIN OrderCustomers(NOLOCK) oc ON o.OrderID = oc.OrderID
        JOIN OrderItems (NOLOCK)oi ON oi.OrderCustomerID = oc.OrderCustomerID
		JOIN #QA ST ON ST.OrderId=o.OrderID AND ST.SKU=oi.SKU
WHERE   o.OrderTypeID IN ( 4, 5 )
        AND o.OrderStatusID IN(4,7)


	
		
	