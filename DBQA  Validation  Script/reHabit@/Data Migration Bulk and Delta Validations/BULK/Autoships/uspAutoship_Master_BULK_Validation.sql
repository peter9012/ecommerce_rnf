
CREATE  PROCEDURE dbqa.uspAutoship_Master_BULK_Validation  
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message='AutoshipHeader Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspAutoshipHeader_BULK

SET @message='AutoshipPaymentProfile Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAutoshipPayment_BULK  


SET @message='AutoshipItems Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAutoshipItem_BULK


 SET @message='AutoshipBillingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAutoshipBillingAddress_BULK  



SET @message='AutoshipShippingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAutoshipShippingAddress_BULK  


END 
   