
CREATE   PROCEDURE dbqa.uspMasterBulkValidationAccountsAutoshipOrdersReturns (@LoadDate DATE)
AS
BEGIN
DECLARE @Message NVARCHAR(100)


SET @Message='Accounts Master Started'
EXECUTE dbqa.uspPrintMessage @Message  
EXECUTE dbqa.uspAccount_Master_BULK_Validation @LoadDate



SET @Message='Autoship Master Started'
EXECUTE dbqa.uspPrintMessage @Message  
EXECUTE dbqa.uspAutoship_Master_BULK_Validation @LoadDate



SET @Message='Orders Master Started'
EXECUTE dbqa.uspPrintMessage @Message  
EXECUTE dbqa.uspOrders_Master_BULK_Validation  @LoadDate



SET @Message='ReturnOrder Master Started'
EXECUTE dbqa.uspPrintMessage @Message  
EXECUTE dbqa.uspReturnOrders_Master_BULK_Validation @LoadDate

END 
