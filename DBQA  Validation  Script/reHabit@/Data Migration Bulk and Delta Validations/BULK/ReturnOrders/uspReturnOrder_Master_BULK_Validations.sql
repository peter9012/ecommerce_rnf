USE DM_QA
GO


CREATE  PROCEDURE dbqa.uspReturnOrders_Master_BULK_Validation
AS
    BEGIN
        DECLARE @message NVARCHAR(MAX)



        SET @message = 'ReturnOrderHeader Validation Started.'
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnOrderHeader_BULK

        SET @message = 'ReturnPayment  Validation Started.'
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnPayment_BULK


        SET @message = 'ReturnItems Validation Started.'
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnItem_BULK


        SET @message = 'ReturnPaymentAddress Validation Started.'
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnPaymentAddress_BULK  





        SET @message = 'ReturnPaymentTransactions Validation Started.'
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnPaymentTransaction_BULK





    END 
 