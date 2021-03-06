USE [DM_QA]
GO
/****** Object:  StoredProcedure [dbqa].[uspReturnOrders_Master_BULK_Validation]    Script Date: 1/31/2017 10:33:16 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


ALTER  PROCEDURE [dbqa].[uspReturnOrders_Master_BULK_Validation]
AS
    BEGIN
        DECLARE @message NVARCHAR(MAX)



        SET @message = CONCAT('***********************************************',
                           CHAR(10), 'ReturnOrderHeader Validation Started.',
                           CHAR(10),
                           '************************************************')
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnOrderHeader_BULK

        SET @message = CONCAT('***********************************************',
                           CHAR(10), 'ReturnPayment  Validation Started.',
                           CHAR(10),
                           '************************************************')
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnPayment_BULK


        SET @message = CONCAT('***********************************************',
                           CHAR(10), 'ReturnItems Validation Started.',
                           CHAR(10),
                           '************************************************')
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnItem_BULK


        SET @message = CONCAT('***********************************************',
                           CHAR(10), 'ReturnPaymentAddress Validation Started.',
                           CHAR(10),
                           '************************************************')
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnPaymentAddress_BULK  





        SET @message = CONCAT('**************************************************',
                           CHAR(10), 'ReturnPaymentTransactions Validation Started.',
                           CHAR(10),
                           '****************************************************')
        EXECUTE dbqa.uspPrintMessage @message
        EXECUTE dbqa.uspReturnPaymentTransaction_BULK





    END 
 