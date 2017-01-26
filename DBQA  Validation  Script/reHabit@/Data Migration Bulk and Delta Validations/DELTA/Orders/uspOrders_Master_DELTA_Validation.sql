USE DM_QA
GO


CREATE  PROCEDURE dbqa.uspOrders_Master_DELTA_Validation  (@StartDate DATE, @EndDate DATETIME)
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message='OrderHeader Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspOrderHeader_delta @StartDate ,@EndDate

SET @message='OrdersPayment  Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderPayment_delta   @StartDate,@EndDate

 


SET @message='OrderItems Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderItem_delta @StartDate,@EndDate


 SET @message='OrderPaymentAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderPaymentAddress_delta @StartDate,@EndDate




SET @message='OrderShippingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspOrderShippingAddress_delta @StartDate,@EndDate

SET @message='OrdersPaymentTransactions Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderPaymentTransactionNEntry_delta @StartDate,@EndDate


 SET @message='OrderConsignmentNEntry Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderConsignmentNEntry_delta @StartDate ,@EndDate
 


END 
 
