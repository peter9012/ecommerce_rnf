USE DM_QA
GO


CREATE  PROCEDURE dbqa.uspOrders_Master_DELTA_Validation  (@StartDate DATE, @EndDate DATETIME=NULL )
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrderHeader Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspOrderHeader_delta @StartDate ,@EndDate

SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrdersPayment  Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderPayment_delta   @StartDate,@EndDate

 


SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrderItems Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderItem_delta @StartDate,@EndDate


 SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrderPaymentAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderPaymentAddress_delta @StartDate,@EndDate




SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrderShippingAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspOrderShippingAddress_delta @StartDate,@EndDate

SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrdersPaymentTransactions Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderPaymentTransactionNEntry_delta @StartDate,@EndDate


 SET @message=CONCAT('***********************************************',
                           CHAR(10),'OrderConsignmentNEntry Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspOrderConsignmentNEntry_delta @StartDate ,@EndDate
 


END 
 
