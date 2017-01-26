USE DM_QA
GO


CREATE  PROCEDURE dbqa.uspOrders_Master_BULK_Validation  (@LoadDate DATE)
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message='OrderHeader Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspOrderHeader_BULK @LoadDate

SET @message='OrdersPayment  Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.upsOrderPayment_BULK


SET @message='OrderItems Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderItems_BULK


 SET @message='OrderPaymentAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspOrderPaymentAddress_BULK  



SET @message='OrderShippingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspOrderShippingAddress_BULK

SET @message='OrdersPaymentTransactions Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderPaymentTransactionNEntry_BULK


 SET @message='OrderConsignmentNEntry Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderConsignmentNEntry_BULK
 


END 
 
