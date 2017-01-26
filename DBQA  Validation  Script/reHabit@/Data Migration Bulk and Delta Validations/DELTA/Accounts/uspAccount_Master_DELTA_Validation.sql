
CREATE  PROCEDURE dbqa.uspAccount_Master_DELTA_Validation  (
      @LoadDate DATE ,
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message='AccountHeader Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.upsAccountHeader_Delta @LoadDate, @StartDate,@EndDate

SET @message='AccountPaymentProfile Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAccountPaymentProfiles_delta  @LoadDate, @StartDate,@EndDate


SET @message='AccountContactAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAccountContactAddress_delta @LoadDate, @StartDate,@EndDate


 SET @message='AccountBillingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAccountsBillingAddress_delta @LoadDate, @StartDate,@EndDate



SET @message='AccountShippingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAccountsShippingAddress_delta @LoadDate, @StartDate,@EndDate


END 

