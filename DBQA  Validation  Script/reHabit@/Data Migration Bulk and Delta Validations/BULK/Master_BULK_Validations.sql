USE [DM_QA]
GO
/****** Object:  StoredProcedure [dbqa].[uspMasterBulkValidationAccountsAutoshipOrdersReturns]    Script Date: 2/1/2017 8:43:27 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


ALTER   PROCEDURE [dbqa].[uspMasterBulkValidationAccountsAutoshipOrdersReturns] (@LoadDate DATE)
AS
BEGIN
DECLARE @Message NVARCHAR(100)


SET @Message=CONCAT('***********************************************',
                           CHAR(10),'Accounts Master Started',
                           CHAR(10),
                           '************************************************')
EXECUTE dbqa.uspPrintMessage @Message  
EXECUTE dbqa.uspAccount_Master_BULK_Validation @LoadDate



SET @Message=CONCAT('***********************************************',
                           CHAR(10),'Autoship Master Started',
                           CHAR(10),
                           '************************************************')
EXECUTE dbqa.uspPrintMessage @Message  
EXECUTE dbqa.uspAutoship_Master_BULK_Validation 



SET @Message=CONCAT('***********************************************',
                           CHAR(10),'Orders Master Started',
                           CHAR(10),
                           '************************************************')
EXECUTE dbqa.uspPrintMessage @Message  
EXECUTE dbqa.uspOrders_Master_BULK_Validation  @LoadDate



SET @Message=CONCAT('***********************************************',
                           CHAR(10),'ReturnOrder Master Started',
                           CHAR(10),
                           '************************************************')
EXECUTE dbqa.uspPrintMessage @Message  
EXECUTE dbqa.uspReturnOrders_Master_BULK_Validation 

END 
