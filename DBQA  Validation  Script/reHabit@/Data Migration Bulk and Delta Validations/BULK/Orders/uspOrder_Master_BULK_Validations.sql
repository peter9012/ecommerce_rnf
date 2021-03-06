USE [DM_QA]
GO
/****** Object:  StoredProcedure [dbqa].[uspOrders_Master_BULK_Validation]    Script Date: 1/30/2017 5:58:32 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


ALTER  PROCEDURE [dbqa].[uspOrders_Master_BULK_Validation]  (@LoadDate DATE)
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrderHeader Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspOrderHeader_BULK @LoadDate

SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrdersPayment  Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderPayment_BULK


SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrderItems Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderItems_BULK


 SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrderPaymentAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspOrderPaymentAddress_BULK  



SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrderShippingAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspOrderShippingAddress_BULK

SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrdersPaymentTransactions Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderPaymentTransactionNEntry_BULK


 SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrderConsignmentNEntry Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderConsignmentNEntry_BULK
 


END 
 
