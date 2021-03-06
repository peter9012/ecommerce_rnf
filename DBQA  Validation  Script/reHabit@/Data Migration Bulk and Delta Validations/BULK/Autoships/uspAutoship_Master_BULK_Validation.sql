USE [DM_QA]
GO
/****** Object:  StoredProcedure [dbqa].[uspAutoship_Master_BULK_Validation]    Script Date: 1/30/2017 4:05:55 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE  PROCEDURE [dbqa].[uspAutoship_Master_BULK_Validation]  
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message=CONCAT('***********************************************',
                           CHAR(10), 'AutoshipHeader Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspAutoshipHeader_BULK

SET @message=CONCAT('***********************************************',
                           CHAR(10), 'AutoshipPaymentProfile Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAutoshipPayment_BULK  


SET @message=CONCAT('***********************************************',
                           CHAR(10), 'AutoshipItems Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAutoshipItem_BULK


 SET @message=CONCAT('***********************************************',
                           CHAR(10), 'AutoshipBillingAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAutoshipBillingAddress_BULK  



SET @message=CONCAT('***********************************************',
                           CHAR(10), 'AutoshipShippingAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAutoshipShippingAddress_BULK  


END 
   