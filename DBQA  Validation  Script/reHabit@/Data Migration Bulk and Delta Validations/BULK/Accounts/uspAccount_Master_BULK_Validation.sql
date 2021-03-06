USE [DM_QA]
GO
/****** Object:  StoredProcedure [dbqa].[uspAccount_Master_BULK_Validation]    Script Date: 3/22/2017 10:06:38 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
    





	
ALTER  PROCEDURE [dbqa].[uspAccount_Master_BULK_Validation] (@LoadDate DATE)
AS
BEGIN
DECLARE @message NVARCHAR(max)



SET @message=CONCAT('***********************************************',
                           CHAR(10), 'AccountHeader Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE  dbqa.uspAccountsHeader_BULK 

SET @message=CONCAT('***********************************************',
                           CHAR(10),'AccountPaymentProfile Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAccountPaymentProfile_BULK @LoadDate


SET @message=CONCAT('***********************************************',
                           CHAR(10),'AccountContactAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
 EXECUTE dbqa.uspAccountContractAddress_BULK @LoadDate


 SET @message=CONCAT('***********************************************',
                           CHAR(10),'AccountBillingAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAccountBillingAddress_BULK @LoadDate



SET @message=CONCAT('***********************************************',
                           CHAR(10),'AccountShippingAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE dbqa.uspAccountShippingAddress_BULK @LoadDate


 SET @message=CONCAT('***********************************************',
                           CHAR(10),'US Consultants PWS Validtions Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE [dbqa].[uspPWS_US_BULK]


 SET @message=CONCAT('***********************************************',
                           CHAR(10),'CA Consultants PWS  Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE [dbqa].[uspPWS_CA_BULK]


 SET @message=CONCAT('***********************************************',
                           CHAR(10),'Vertex TaxExempt  Validation Started.',
                           CHAR(10),
                           '************************************************')
 EXECUTE dbqa.uspPrintMessage @message
EXECUTE [dbqa].[uspVertex_TaxExempt_BULK]

END 




