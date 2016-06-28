--===============================================
--Reverting back those Quantity=Original Quantity.
--==============================================

--THIS UPDATE FOR THOSE ORDERS HAVING QUANTITY=1

BEGIN TRAN

;WITH OrderItems 
 AS (
SELECT DISTINCT ai.OrderItemID
FROM    RodanFieldsLive.dbo.OrderItemsAudit (NOLOCK)ai
JOIN RodanFieldsLive.dbo.orderitems (NOLOCK) oi ON oi.OrderCustomerID=ai.OrderCustomerID AND oi.OrderItemID=ai.OrderItemID
WHERE  oi. OrderCustomerID IN ( 7874908, 3681987, 4581155, 4581155, 4997182,
                             6722397, 6922222, 6922222, 6922222, 7008215,
                             7132394, 8775664, 10951690, 11182082, 11182082,
                             11182082, 11936429, 13495700, 13688557, 13765540,
                             14051626, 14748030, 14748030, 14756976, 14835769,
                             14873467, 15484433, 15808192, 16302721, 16302721,
                             16315211, 16520087, 16872989, 17219482, 18304637,
                             18500775, 18500775, 18691133, 19248524, 19248524,
                             19248524, 19279727, 19279727 )
        AND Operation = 'u' AND CAST(AuditDate AS DATE)=CAST(GETDATE()-1 AS DATE) AND ai.Quantity=2
)

UPDATE oi
SET oi.Quantity=1
FROM RodanFieldsLive.dbo.OrderItems oi
JOIN OrderItems it ON oi.OrderItemId=it.OrderItemID
WHERE OrderCustomerID IN  ( 3681987, 4581155, 4581155, 4997182,
                             6722397, 6922222, 6922222, 6922222, 7008215,
                             7132394, 8775664, 10951690, 11182082, 11182082,
                             11182082, 11936429, 13495700, 13688557, 13765540,
                             14051626, 14748030, 14748030, 14756976, 14835769,
                             14873467, 15484433, 15808192, 16302721, 16302721,
                             16315211, 16520087, 16872989, 17219482, 18304637,
                             18500775, 18500775, 18691133, 19248524, 19248524,
                             19248524, 19279727, 19279727 )



IF @@ERROR<>0
BEGIN 
PRINT 'Transaction Rolled Back'
ROLLBACK TRAN
END
ELSE
BEGIN
PRINT 'Transaction Committed Successfully'
COMMIT TRAN
END


--THIS UPDATE FOR THOSE ORDERS HAVING QUANTITY=3

BEGIN TRAN


;WITH OrderItems 
AS (
SELECT DISTINCT ai.OrderItemID
FROM    RodanFieldsLive.dbo.OrderItemsAudit (NOLOCK)ai
JOIN RodanFieldsLive.dbo.orderitems (NOLOCK) oi ON oi.OrderCustomerID=ai.OrderCustomerID AND oi.OrderItemID=ai.OrderItemID
WHERE  oi. OrderCustomerID IN ( 7874908, 3681987, 4581155, 4581155, 4997182,
                             6722397, 6922222, 6922222, 6922222, 7008215,
                             7132394, 8775664, 10951690, 11182082, 11182082,
                             11182082, 11936429, 13495700, 13688557, 13765540,
                             14051626, 14748030, 14748030, 14756976, 14835769,
                             14873467, 15484433, 15808192, 16302721, 16302721,
                             16315211, 16520087, 16872989, 17219482, 18304637,
                             18500775, 18500775, 18691133, 19248524, 19248524,
                             19248524, 19279727, 19279727 )
        AND Operation = 'u' AND CAST(AuditDate AS DATE)=CAST(GETDATE()-1 AS DATE) AND ai.Quantity=2
)


UPDATE oi
SET oi.Quantity=3
FROM RodanFieldsLive.dbo.OrderItems oi
JOIN Orderitems it ON oi.OrderItemId=it.orderItemId
WHERE OrderCustomerID =7874908 

IF @@ERROR<>0
BEGIN 
PRINT 'Transaction Rolled Back'
ROLLBACK TRAN
END
ELSE
BEGIN
PRINT 'Transaction Committed Successfully'
COMMIT TRAN
END


--=======================================================
-- INSERTING BACK THOSE DELETED RECORDS FROM AUDIT TABLE
--=======================================================


BEGIN TRAN

USE [RodanFieldsLive]
GO

INSERT INTO [dbo].[OrderItems]
           ([OrderCustomerID]
           ,[ProductID]
           ,[OrderItemTypeID]
           ,[Quantity]
           ,[ItemPrice]
           ,[Discount]
           ,[DiscountPercent]
           ,[AdjustedPrice]
           ,[CommissionableTotal]
           ,[ChargeTax]
           ,[ChargeShipping]
           ,[Points]
           ,[MinCustomerSubtotal]
           ,[MaxCustomerSubtotal]
           ,[ParentOrderItemID]
           ,[HostessRewardRuleID]
           ,[ProductPriceTypeID]
           ,[ProductName]
           ,[SKU]
           ,[CityLocalSalesTax]
           ,[CitySalesTax]
           ,[CountyLocalSalesTax]
           ,[CountySalesTax]
           ,[StateSalesTax]
           ,[CountrySalesTax]
           ,[TaxPercent]
           ,[TaxAmount]
           ,[TaxPercentCity]
           ,[TaxAmountCity]
           ,[TaxPercentState]
           ,[TaxAmountState]
           ,[TaxPercentCounty]
           ,[TaxAmountCounty]
           ,[TaxPercentDistrict]
           ,[TaxAmountDistrict]
           ,[TaxableTotal])
		   
SELECT [OrderCustomerID]
           ,[ProductID]
           ,[OrderItemTypeID]
           ,[Quantity]
           ,[ItemPrice]
           ,[Discount]
           ,[DiscountPercent]
           ,[AdjustedPrice]
           ,[CommissionableTotal]
           ,[ChargeTax]
           ,[ChargeShipping]
           ,[Points]
           ,[MinCustomerSubtotal]
           ,[MaxCustomerSubtotal]
           ,[ParentOrderItemID]
           ,[HostessRewardRuleID]
           ,[ProductPriceTypeID]
           ,[ProductName]
           ,[SKU]
           ,[CityLocalSalesTax]
           ,[CitySalesTax]
           ,[CountyLocalSalesTax]
           ,[CountySalesTax]
           ,[StateSalesTax]
           ,[CountrySalesTax]
           ,[TaxPercent]
           ,[TaxAmount]
           ,[TaxPercentCity]
           ,[TaxAmountCity]
           ,[TaxPercentState]
           ,[TaxAmountState]
           ,[TaxPercentCounty]
           ,[TaxAmountCounty]
           ,[TaxPercentDistrict]
           ,[TaxAmountDistrict]
           ,[TaxableTotal]
FROM    RodanFieldsLive.dbo.OrderItemsAudit (NOLOCK)
WHERE   OrderCustomerID IN ( 7874908, 3681987, 4581155, 4581155, 4997182,
                             6722397, 6922222, 6922222, 6922222, 7008215,
                             7132394, 8775664, 10951690, 11182082, 11182082,
                             11182082, 11936429, 13495700, 13688557, 13765540,
                             14051626, 14748030, 14748030, 14756976, 14835769,
                             14873467, 15484433, 15808192, 16302721, 16302721,
                             16315211, 16520087, 16872989, 17219482, 18304637,
                             18500775, 18500775, 18691133, 19248524, 19248524,
                             19248524, 19279727, 19279727 )
        AND Operation = 'd' AND CAST(AuditDate AS DATE)=CAST(GETDATE()-1 AS DATE)
ORDER BY AuditDate DESC;
    
GO


IF @@ERROR<>0
BEGIN 
PRINT 'Transaction Rolled Back'
ROLLBACK TRAN
END
ELSE
BEGIN
PRINT 'Transaction Committed Successfully'
COMMIT TRAN
END