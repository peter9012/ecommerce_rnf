
CREATE  PROCEDURE dbqa.uspAccount_Master_DELTA_Validation  (
      @LoadDate DATE ,
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message=CONCAT('***********************************************',
                           CHAR(10), 'AccountHeader Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspAccountHeader_Delta @LoadDate, @StartDate,@EndDate

SET @message=CONCAT('***********************************************',
                           CHAR(10), 'AccountPaymentProfile Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAccountPaymentProfiles_delta  @LoadDate, @StartDate,@EndDate


SET @message=CONCAT('***********************************************',
                           CHAR(10), 'AccountContactAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAccountContactAddress_delta @LoadDate, @StartDate,@EndDate


 SET @message=CONCAT('***********************************************',
                           CHAR(10), 'AccountBillingAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAccountsBillingAddress_delta @LoadDate, @StartDate,@EndDate



SET @message=CONCAT('***********************************************',
                           CHAR(10), 'AccountShippingAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAccountsShippingAddress_delta @LoadDate, @StartDate,@EndDate


END 

