
CREATE PROCEDURE dbqa.uspMasterDELTAValidationAccountsAutoshipOrdersReturns
    (
      @LoadDate DATE ,
      @StartDate DATE ,
      @EndDate DATETIME = NULL
    )
AS
    BEGIN
        DECLARE @Message NVARCHAR(100)


        SET @Message = 'Accounts Master Started'
        EXECUTE dbqa.uspPrintMessage @Message  
        EXECUTE dbqa.uspAccount_Master_DELTA_Validation @LoadDate, @StartDate,
            @EndDate



        SET @Message = 'Autoship Master Started'
        EXECUTE dbqa.uspPrintMessage @Message  
        EXECUTE dbqa.uspAutoship_Master_DELTA_Validation @StartDate, @EndDate



        SET @Message = 'Orders Master Started'
        EXECUTE dbqa.uspPrintMessage @Message  
        EXECUTE dbqa.uspOrders_Master_DELTA_Validation @StartDate, @EndDate



        SET @Message = 'ReturnOrder Master Started'
        EXECUTE dbqa.uspPrintMessage @Message  
        EXECUTE dbqa.uspReturnOrders_Master_DELTA_Validation @StartDate,
            @EndDate

    END 