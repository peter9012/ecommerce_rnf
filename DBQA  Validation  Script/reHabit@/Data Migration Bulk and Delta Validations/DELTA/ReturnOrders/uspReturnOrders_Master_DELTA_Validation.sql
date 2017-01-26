USE DM_QA
GO


CREATE  PROCEDURE dbqa.uspReturnOrders_Master_DELTA_Validation
    (
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
    BEGIN
        DECLARE @message NVARCHAR(MAX)



        SET @message = 'ReturnOrderHeader Validation Started.'
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnOrderHeader_delta @StartDate, @EndDate

        SET @message = 'ReturnPayment  Validation Started.'
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnPayment_delta @StartDate, @EndDate


        SET @message = 'ReturnItems Validation Started.'
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnItem_delta @StartDate, @EndDate


        SET @message = 'ReturnPaymentAddress Validation Started.'
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnPaymentAddress_delta @StartDate, @EndDate  



        SET @message = 'ReturnPaymentTransactions Validation Started.'
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnPaymentTransaction_delta @StartDate, @EndDate





    END 
 