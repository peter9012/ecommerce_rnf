
CREATE  PROCEDURE dbqa.uspAccount_Master_BULK_Validation (@LoadDate DATE)
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message='AccountHeader Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspAccountsHeader_BULK @LoadDate

SET @message='AccountPaymentProfile Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAccountPaymentProfile_BULK @LoadDate


SET @message='AccountContactAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAccountContractAddress_BULK


 SET @message='AccountBillingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAccountBillingAddress_BULK @LoadDate



SET @message='AccountShippingAddress Validation Started.'
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAccountShippingAddress_BULK @LoadDate


END 

