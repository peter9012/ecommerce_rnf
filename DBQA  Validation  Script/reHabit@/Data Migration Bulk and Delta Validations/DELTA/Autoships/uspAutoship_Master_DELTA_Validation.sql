CREATE  PROCEDURE dbqa.uspAutoship_Master_DELTA_Validation  (      
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message=CONCAT('***********************************************',
                           CHAR(10),'AutoshipHeader Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspAutoshipHeader_delta  @StartDate,@EndDate

SET @message=CONCAT('***********************************************',
                           CHAR(10),'AutoshipItems Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspAutoship_Item_delta  @StartDate,@EndDate


SET @message=CONCAT('***********************************************',
                           CHAR(10),'AutoshipPayment  Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAutoshipPayment_delta   @StartDate,@EndDate




 SET @message=CONCAT('***********************************************',
                           CHAR(10),'AutoshipBillingAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAutoshipPaymentAddresses_delta  @StartDate,@EndDate



SET @message=CONCAT('***********************************************',
                           CHAR(10),'AutoshipShippingAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAutoshipShippingAddress_delta  @StartDate,@EndDate


END 

