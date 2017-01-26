CREATE  PROCEDURE dbqa.uspAutoship_Master_DELTA_Validation  (      
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message='AutoshipHeader Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspAutoshipHeader_delta  @StartDate,@EndDate

SET @message='AutoshipItems Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspAutoship_Item_delta  @StartDate,@EndDate


SET @message='AutoshipPayment  Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAutoshipPayment_delta   @StartDate,@EndDate




 SET @message='AutoshipBillingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAutoshipPaymentAddresses_delta  @StartDate,@EndDate



SET @message='AutoshipShippingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAutoshipShippingAddress_delta  @StartDate,@EndDate


END 

