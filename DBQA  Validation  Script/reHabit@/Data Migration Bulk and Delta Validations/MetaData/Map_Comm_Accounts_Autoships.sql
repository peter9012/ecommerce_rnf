USE DM_QA
GO 


--********************************************************************************************
--						Hybris>>			Comm_Accounts		
--********************************************************************************************

INSERT INTO dbqa.Map_tab
        ( SourceObject,SourceColumn,SourceDataTypes,SourceRef,TargetObject,TargetColumn ,TargetDataTypes ,[Key],TargetRef ,Flag ,[Owner],[SQL Stmt])		
VALUES 
/* Products Table in Hybris.*/
(N'Hybris.dbo.users' ,N'p_customerid',N'nvarchar(255)' ,N'RFOperations.RFO_Accounts.AccountBase' ,N'Commissions.etl.Accounts' ,N'AccountID' ,N'int' ,N'AccountID' ,N'' , N'Key' ,N'Comm_Accounts',N'') 
,-- AccountID 
(N'Hybris.dbo.users' ,N'p_sponsorid',N'bigint' ,N'RFOperations.RFO_Accounts.AccountBase' ,N'Commissions.etl.Accounts' ,N'SponsorID' ,N'int' ,N'AccountID' ,N'' , N'c2c' ,N'Comm_Accounts',N'') 
,-- SponsorID 
(N'Hybris.dbo.users' ,N'P_enrollerId',N'bigint' ,N'RFOperations.RFO_Accounts.AccountBase' ,N'Commissions.etl.Accounts' ,N'EnrollerID' ,N'int' ,N'AccountID' ,N'' , N'c2c' ,N'Comm_Accounts',N'') 
,-- EnrollerID 
(N'Hybris.dbo.users' ,N'p_enrollmentdate',N'datetime' ,N'covert(datetimeoffset ,p_enrollmentdate )' ,N'Commissions.etl.Accounts' ,N'EnrollmentDate' ,N'datetimeoffset' ,N'AccountID' ,N'' , N'c2c' ,N'Comm_Accounts',N'') 
,-- EnrollmentDate 
(N'Hybris.dbo.users' ,N'p_accountstatus',N'bigint' ,N'Hybris.dbo.enumrationvalues' ,N'Commissions.etl.Accounts' ,N'StatusID' ,N'int' ,N'AccountID' ,N'' , N'Ref' ,N'Comm_Accounts',N'') 
,-- StatusID 
(N'Hybris.dbo.users' ,N'p_AccountType',N'bigint' ,N'Hybris.dbo.enumrationvalues' ,N'Commissions.etl.Accounts' ,N'AccountTypeID' ,N'int' ,N'AccountID' ,N'' , N'Ref' ,N'Comm_Accounts',N'') 
,-- AccountTypeID 
(N'Hybris.dbo.users' ,N'p_currency',N'bigint' ,N'Hybris.dbo.Currencies' ,N'Commissions.etl.Accounts' ,N'CurrencyCode' ,N'nvarchar(3)' ,N'AccountID' ,N'' , N'Ref' ,N'Comm_Accounts',N'') 
,-- CurrencyCode 
(N'Hybris.dbo.users' ,N'p_country',N'bigint' ,N'Hybris.dbo.Countries' ,N'Commissions.etl.Accounts' ,N'CountryCode' ,N'nvarchar(3)' ,N'AccountID' ,N'' , N'Ref' ,N'Comm_Accounts',N'') 
,-- CountryCode
(N'Hybris.dbo.users' ,N'p_softterminatedate',N'datetime' ,N'covert(datetimeoffset ,p_enrollmentdate )' ,N'Commissions.etl.Accounts' ,N'SoftTerminationDate' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'Comm_Accounts',N'') 
,-- SoftTerminationDate 
(N'Hybris.dbo.users' ,N'p_hardterminatedate',N'datetime' ,N'' ,N'Commissions.etl.Accounts' ,N'HardTerminationDate' ,N'datetime' ,N'AccountID' ,N'' , N'c2c' ,N'Comm_Accounts',N'') 
,-- HardTerminationDate 
(N'Hybris.dbo.users' ,N'CreatedTS',N'datetime' ,N'' ,N'Commissions.etl.Accounts' ,N'CreatedOn' ,N'datetime2' ,N'AccountID' ,N'' , N'c2c' ,N'Comm_Accounts',N'') 
,-- CreatedOn 
(N'Hybris.dbo.users' ,N'modifiedTS',N'datetime' ,N'' ,N'Commissions.etl.Accounts' ,N'UpdatedOn' ,N'datetime2' ,N'AccountID' ,N'' , N'c2c' ,N'Comm_Accounts',N'') 
,-- UpdatedOn 
(N'Hybris.dbo.users' ,N'P_active',N'tinyint' ,N'' ,N'Commissions.etl.Accounts' ,N'Active' ,N'bit' ,N'AccountID' ,N'' , N'c2c' ,N'Comm_Accounts',N'') 
,-- Active 

--********************************************************************************************
--							Hybris>>		Comm_Autoship 
--********************************************************************************************
(N'Hybris.dbo.carts' ,N'p_code',N'nvarchar(255)' ,N'' ,N'Commissions.etl.Autoships' ,N'AutoshipID' ,N'bigint' ,N'AutoshipNumber' ,N'' , N'Key' ,N'Comm_Autoships',N'') 
,-- AutoshipID 
(N'Hybris.dbo.carts' ,N'p_type',N'nvarchar(255)' ,N'' ,N'Commissions.etl.Autoships' ,N'AutoshipTypeID' ,N'bigint' ,N'AutoshipNumber' ,N'' , N'ref' ,N'Comm_Autoships',N'') 
,-- AutoshipTypeID 
(N'Hybris.dbo.carts' ,N'p_userpk',N'bigint' ,N'Hybris.dbo.users' ,N'Commissions.etl.Autoships' ,N'AccountID' ,N'bigint' ,N'AutoshipNumber' ,N'' , N'ref' ,N'Comm_Autoships',N'') 
,-- AccountID 
(N'Hybris.dbo.carts' ,N'CreatedTS',N'datetime' ,N' ' ,N'Commissions.etl.Autoships' ,N'CreationDate' ,N'datetime2' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Comm_Autoships',N'') 
,-- CreationDate 
(N'Hybris.dbo.carts' ,N'P_status',N'datetime' ,N' ' ,N'Commissions.etl.Autoships' ,N'AutoshipStatusID' ,N'datetimeoffset' ,N'AutoshipNumber' ,N'' , N'ref' ,N'Comm_Autoships',N'') 
,-- AutoshipStatusID 
(N'Hybris.dbo.carts' ,N'modifiedTS',N'datetime' ,N' ' ,N'Commissions.etl.Autoships' ,N'UpdatedOn' ,N'datetime2' ,N'AutoshipNumber' ,N'' , N'c2c' ,N'Comm_Autoships',N'') 
,-- UpdatedOn 



--********************************************************************************************
--							RFO Orders =		Comm_Orders	
--********************************************************************************************


(N'RFOperations.Hybris.Orders' ,N'r_OrderID',N'bigint' ,N'' ,N'Commissions.etl.Orders' ,N'OrderID' ,N'int' ,N'OrderID' ,N'' , N'Key' ,N'RFO_Comm_Orders',N'') 
,-- OrderID
(N'RFOperations.Hybris.Orders' ,N'r_OrderStatusID',N'int' ,N'' ,N'Commissions.etl.Orders' ,N'OrderStatusID' ,N'int' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- OrderStatusID
(N'RFOperations.Hybris.Orders' ,N'r_OrderTypeID',N'int' ,N'' ,N'Commissions.etl.Orders' ,N'OrderTypeID' ,N'int' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- OrderTypeID  
(N'RFOperations.Hybris.Orders' ,N'r_CurrencyCode',N'nvarchar' ,N'' ,N'Commissions.etl.Orders' ,N'CurrencyCode' ,N'nvarchar' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- CurrencyCode 
(N'RFOperations.Hybris.Orders' ,N'r_AccountID',N'int' ,N'' ,N'Commissions.etl.Orders' ,N'AccountID' ,N'int' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- AccountID 
(N'RFOperations.Hybris.Orders' ,N'r_ConsultantID',N'int' ,N'' ,N'Commissions.etl.Orders' ,N'ConsultantID' ,N'int' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- ConsultantID 
(N'RFOperations.Hybris.Orders' ,N'r_CompleteDate',N'Datetime' ,N'' ,N'Commissions.etl.Orders' ,N'CompleteDate' ,N'datetimeoffset' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- CompleteDate
(N'RFOperations.Hybris.Orders' ,N'r_CommissionDate',N'Datetime' ,N'' ,N'Commissions.etl.Orders' ,N'CommissionDate' ,N'datetimeoffset' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- CommissionDate
(N'RFOperations.Hybris.Orders' ,N'r_PeriodID',N'int' ,N'' ,N'Commissions.etl.Orders' ,N'PeriodID' ,N'int' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- PeriodID 
(N'RFOperations.Hybris.Orders' ,N'r_TotalCV',N'money' ,N'' ,N'Commissions.etl.Orders' ,N'TotalCV' ,N'money' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- TotalCV 
(N'RFOperations.Hybris.Orders' ,N'r_TotalQV',N'money' ,N'' ,N'Commissions.etl.Orders' ,N'TotalQV' ,N'money' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- TotalQV 
(N'RFOperations.Hybris.Orders' ,N'r_CountryCode',N'nvarchar' ,N'' ,N'Commissions.etl.Orders' ,N'CountryCode' ,N'nvarchar' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- CountryCode 
(N'RFOperations.Hybris.Orders' ,N'r_CreatedOn',N'Datetime' ,N'' ,N'Commissions.etl.Orders' ,N'CreatedOn' ,N'datetime2' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- CreatedOn
(N'RFOperations.Hybris.Orders' ,N'r_UpdatedOn',N'Datetime' ,N'' ,N'Commissions.etl.Orders' ,N'UpdatedOn' ,N'datetime2' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- UpdatedOn
(N'RFOperations.Hybris.Orders' ,N'r_ServerModifiedDate',N'Datetime' ,N'' ,N'Commissions.etl.Orders' ,N'ServerModifiedDate' ,N'datetime2' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- ServerModifiedDate
(N'RFOperations.Hybris.Orders' ,N'r_CancellationReasonID',N'int' ,N'' ,N'Commissions.etl.Orders' ,N'CancellationReasonID' ,N'int' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- CancellationReasonID
(N'RFOperations.Hybris.Orders' ,N'r_AccountNumber',N'Nvarchar(225)' ,N'' ,N'Commissions.etl.Orders' ,N'AccountNumber' ,N'nvarchar(100)' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- AccountNumber
(N'RFOperations.Hybris.Orders' ,N'r_OrderSponsorAccountNumber',N'Nvarchar(225)' ,N'' ,N'Commissions.etl.Orders' ,N'OrderSponsorAccountNumber' ,N'nvarchar(100)' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_Orders',N'') 
,-- OrderSponsorAccountNumber


--********************************************************************************************
--							RFO Orders =		Comm_OrderItems 
--********************************************************************************************


(N'RFOperations.Hybris.OrderItems' ,N'r_OrderID',N'bigint' ,N'' ,N'Commissions.etl.OrderItems' ,N'OrderID' ,N'int' ,N'OrderID' ,N'' , N'Key' ,N'RFO_Comm_OrderItems',N'') 
,-- OrderID
(N'RFOperations.Hybris.OrderItems' ,N'r_OrderItemID',N'bigint' ,N'' ,N'Commissions.etl.OrderItems' ,N'OrderItemID' ,N'int' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- OrderItemID
(N'RFOperations.Hybris.OrderItems' ,N'r_SKU',N'nvarchar' ,N'' ,N'Commissions.etl.OrderItems' ,N'SKU' ,N'nvarchar' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- SKU
(N'RFOperations.Hybris.OrderItems' ,N'r_Quantity',N'int' ,N'' ,N'Commissions.etl.OrderItems' ,N'Quantity' ,N'int' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- Quantity
(N'RFOperations.Hybris.OrderItems' ,N'r_CV',N'money' ,N'' ,N'Commissions.etl.OrderItems' ,N'CV' ,N'money' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- CV
(N'RFOperations.Hybris.OrderItems' ,N'r_QV',N'money' ,N'' ,N'Commissions.etl.OrderItems' ,N'QV' ,N'money' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- QV
(N'RFOperations.Hybris.OrderItems' ,N'r_WholesalePrice',N'money' ,N'' ,N'Commissions.etl.OrderItems' ,N'WholesalePrice' ,N'money' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- WholesalePrice
(N'RFOperations.Hybris.OrderItems' ,N'r_PricePaid',N'money' ,N'' ,N'Commissions.etl.OrderItems' ,N'PricePaid' ,N'money' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- PricePaid
(N'RFOperations.Hybris.OrderItems' ,N'r_TotalCV',N'money' ,N'' ,N'Commissions.etl.OrderItems' ,N'TotalCV' ,N'money' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- TotalCV
(N'RFOperations.Hybris.OrderItems' ,N'r_TotalQV',N'money' ,N'' ,N'Commissions.etl.OrderItems' ,N'TotalQV' ,N'money' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- TotalQV
(N'RFOperations.Hybris.OrderItems' ,N'r_TotalWholesalePrice',N'money' ,N'' ,N'Commissions.etl.OrderItems' ,N'TotalWholesalePrice' ,N'money' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- TotalWholesalePrice
(N'RFOperations.Hybris.OrderItems' ,N'r_TotalPricePaid',N'money' ,N'' ,N'Commissions.etl.OrderItems' ,N'TotalPricePaid' ,N'money' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- TotalPricePaid
(N'RFOperations.Hybris.OrderItems' ,N'r_CreatedOn',N'Datetime' ,N'' ,N'Commissions.etl.OrderItems' ,N'CreatedOn' ,N'Datetime2' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- CreatedOn
(N'RFOperations.Hybris.OrderItems' ,N'r_EtlOrderItemID',N'int' ,N'' ,N'Commissions.etl.OrderItems' ,N'EtlOrderItemID' ,N'int' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- EtlOrderItemID
(N'RFOperations.Hybris.OrderItems' ,N'r_PeriodId',N'int' ,N'' ,N'Commissions.etl.OrderItems' ,N'PeriodId' ,N'int' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- PeriodId
(N'RFOperations.Hybris.OrderItems' ,N'r_UpdatedOn',N'Datetime' ,N'' ,N'Commissions.etl.OrderItems' ,N'UpdatedOn' ,N'Datetime2' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- UpdatedOn
(N'RFOperations.Hybris.OrderItems' ,N'r_CancellationReasonID',N'int' ,N'' ,N'Commissions.etl.OrderItems' ,N'CancellationReasonID' ,N'int' ,N'OrderID' ,N'' , N'c2c' ,N'RFO_Comm_OrderItems',N'') 
,-- CancellationReasonID


--********************************************************************************************
--							RFO Orders =		Comm_ReturnOrders
--********************************************************************************************


(N'RFOperations.Hybris.ReturnOrders' ,N'r_OrderReturnID',N'bigint' ,N'' ,N'Commissions.etl.OrderReturns' ,N'OrderReturnID' ,N'int' ,N'OrderReturnID' ,N'' , N'Key' ,N'RFO_Comm_OrderReturns',N'') 
,-- OrderReturnID
(N'RFOperations.Hybris.ReturnOrders' ,N'r_OrderID',N'bigint' ,N'' ,N'Commissions.etl.OrderReturns' ,N'OrderID' ,N'int' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturns',N'') 
,-- OrderID
(N'RFOperations.Hybris.ReturnOrders' ,N'r_TransactionDate',N'datetimeoffset' ,N'' ,N'Commissions.etl.OrderReturns' ,N'TransactionDate' ,N'datetimeoffset' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturns',N'') 
,-- TransactionDate
(N'RFOperations.Hybris.ReturnOrders' ,N'r_ReturnCommissionDate',N'datetimeoffset' ,N'' ,N'Commissions.etl.OrderReturns' ,N'ReturnCommissionDate' ,N'datetimeoffset' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturns',N'') 
,-- ReturnCommissionDate
(N'RFOperations.Hybris.ReturnOrders' ,N'r_PeriodID',N'bigint' ,N'' ,N'Commissions.etl.OrderReturns' ,N'PeriodID' ,N'int' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturns',N'') 
,-- PeriodID
(N'RFOperations.Hybris.ReturnOrders' ,N'r_CreatedOn',N'datetime' ,N'' ,N'Commissions.etl.OrderReturns' ,N'CreatedOn' ,N'datetime' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturns',N'') 
,-- CreatedOn
(N'RFOperations.Hybris.ReturnOrders' ,N'r_UpdatedOn',N'datetime' ,N'' ,N'Commissions.etl.OrderReturns' ,N'UpdatedOn' ,N'datetime' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturns',N'') 
,-- UpdatedOn
(N'RFOperations.Hybris.ReturnOrders' ,N'r_ServerModifiedDate',N'datetime' ,N'' ,N'Commissions.etl.OrderReturns' ,N'ServerModifiedDate' ,N'datetime' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturns',N'') 
,-- ServerModifiedDate
(N'RFOperations.Hybris.ReturnOrders' ,N'r_ReturnStatusID',N'bigint' ,N'' ,N'Commissions.etl.OrderReturns' ,N'ReturnStatusID' ,N'int' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturns',N'') 
,-- ReturnStatusID
(N'RFOperations.Hybris.ReturnOrders' ,N'r_ReturnReasonID',N'bigint' ,N'' ,N'Commissions.etl.OrderReturns' ,N'ReturnReasonID' ,N'int' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturns',N'') 
,-- ReturnReasonID

--********************************************************************************************
--							RFO Orders =		Comm_OrderReturnItems
--********************************************************************************************


(N'RFOperations.Hybris.ReturnItems' ,N'r_OrderReturnID',N'bigint' ,N'' ,N'Commissions.etl.OrderReturnItems' ,N'OrderReturnID' ,N'int' ,N'OrderReturnID' ,N'' , N'Key' ,N'RFO_Comm_OrderReturnItems',N'') 
,-- OrderReturnID
(N'RFOperations.Hybris.ReturnItems' ,N'r_OrderID',N'bigint' ,N'' ,N'Commissions.etl.OrderReturnItems' ,N'OrderID' ,N'int' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturnItems',N'') 
,-- OrderID
(N'RFOperations.Hybris.ReturnItems' ,N'r_OrderItemID',N'bigint' ,N'' ,N'Commissions.etl.OrderReturnItems' ,N'OrderItemID' ,N'int' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturnItems',N'') 
,-- OrderItemID
(N'RFOperations.Hybris.ReturnItems' ,N'r_Quantity',N'bigint' ,N'' ,N'Commissions.etl.OrderReturnItems' ,N'Quantity' ,N'int' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturnItems',N'') 
,-- Quantity
(N'RFOperations.Hybris.ReturnItems' ,N'r_ReturnItemID',N'bigint' ,N'' ,N'Commissions.etl.OrderReturnItems' ,N'ReturnItemID' ,N'int' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturnItems',N'') 
,-- ReturnItemID
(N'RFOperations.Hybris.ReturnItems' ,N'r_CreatedOn',N'datetime' ,N'' ,N'Commissions.etl.OrderReturnItems' ,N'CreatedOn' ,N'datetime2' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturnItems',N'') 
,-- CreatedOn
(N'RFOperations.Hybris.ReturnItems' ,N'r_PeriodId',N'bigint' ,N'' ,N'Commissions.etl.OrderReturnItems' ,N'PeriodId' ,N'int' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturnItems',N'') 
,-- PeriodId
(N'RFOperations.Hybris.ReturnItems' ,N'r_UpdatedOn',N'datetime' ,N'' ,N'Commissions.etl.OrderReturnItems' ,N'UpdatedOn' ,N'datetime2' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturnItems',N'') 
,-- UpdatedOn
(N'RFOperations.Hybris.ReturnItems' ,N'r_ReturnStatusID',N'bigint' ,N'' ,N'Commissions.etl.OrderReturnItems' ,N'ReturnStatusID' ,N'int' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturnItems',N'') 
,-- ReturnStatusID
(N'RFOperations.Hybris.ReturnItems' ,N'r_ReturnReasonID',N'bigint' ,N'' ,N'Commissions.etl.OrderReturnItems' ,N'ReturnReasonID' ,N'int' ,N'OrderReturnID' ,N'' , N'c2c' ,N'RFO_Comm_OrderReturnItems',N'') 
-- ReturnReasonID





	USE DM_QA
GO 

DECLARE @sourceColumn NVARCHAR(30) ,
    @TargetColumn NVARCHAR(30) ,
    @flag NVARCHAR(10) ,
    @Owner NVARCHAR(50) ,
    @key NVARCHAR(50) ,
    @int INT ,
    @Max INT
			
			

SELECT  @Max = MAX(MapID)
FROM    dbqa.Map_tab
			
          

SET @int = 1
WHILE @Max >= @int
    BEGIN
        SELECT  @sourceColumn = SourceColumn ,
                @TargetColumn = TargetColumn ,
                @Owner = [Owner] ,
                @flag = [flag] ,
                @Key = [Key]
        FROM    dbqa.Map_tab
        WHERE   MapID = @int	
				
        UPDATE  dbqa.Map_tab
        SET     [SQL Stmt] = CASE WHEN sourceDatatypes LIKE ( 'nvarchar%' )
                                  THEN 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE ISNULL(a.' + @sourceColumn + ','' '' ) <> ISNULL(b.'
                                       + @TargetColumn + ','' '' ) '
                                  WHEN sourceDatatypes LIKE 'date%'
                                  THEN 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE CAST(ISNULL(a.' + @sourceColumn
                                       + ',''2018-01-01'' )AS DATE) <> CAST(ISNULL(b.'
                                       + @TargetColumn
                                       + ',''2018-01-01 '' )AS DATE) '
                                  WHEN sourceDatatypes IN ( 'money', 'int',
                                                            'bigint' )
                                  THEN 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE ISNULL(a.' + @sourceColumn + ',0 ) <> ISNULL(b.'
                                       + @TargetColumn + ',0 ) '
                                  ELSE 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE a.' + @sourceColumn + ' <> b.' + @TargetColumn + ' '
                             END
        WHERE   MapID = @int
                AND flag IN ('c2c')
                AND [Owner] IN ('Comm_Accounts','Comm_Autoships','RFO_Comm_Orders','RFO_Comm_OrderReturns')
			
        SET @int = @int + 1
    END
	GO




	
DECLARE @sourceColumn NVARCHAR(30) ,
    @TargetColumn NVARCHAR(30) ,
    @flag NVARCHAR(10) ,
    @int INT ,
    @Max INT
			
			--SELECT ROW_NUMBER() OVER ( ORDER BY MapID) rn ,SourceColumn,TargetColumn,flag INTO #temp FROM #map_tab WHERE flag NOT IN  ('Key','default')
SELECT  @Max = MAX(MapID)
FROM    dbqa.Map_tab
			


SET @int = 1
WHILE @Max >= @int
    BEGIN
        SELECT  @sourceColumn = SourceColumn ,
                @TargetColumn = TargetColumn
        FROM    dbqa.Map_tab
        WHERE   MapID = @int	
				
        UPDATE  dbqa.Map_tab
        SET     [SQL Stmt] = CASE WHEN sourceDatatypes LIKE 'date%'
                                  THEN ' SELECT COALESCE(A.RFOKey,B.TargetKey) AS [Key] ,A.'
                                       + @sourceColumn + ' ,B.'
                                       + @TargetColumn + ' FROM (
		  SELECT RFOKey,CAST(' + @SourceColumn + ' AS DATE) AS '
                                       + @sourceColumn + '  FROM #RFO 
		  EXCEPT 
		  SELECT TargetKey,CAST(' + @TargetColumn + ' AS DATE) AS '
                                       + @TargetColumn + '  FROM #Target) A
		   LEFT JOIN 
		 ( SELECT TargetKey,CAST(' + @TargetColumn + ' AS DATE) AS '
                                       + @TargetColumn + '  FROM #Target
		  EXCEPT 
		  SELECT SourceKey,CAST(' + @sourceColumn + ' AS DATE) AS '
                                       + @sourceColumn
                                       + '  FROM #Source)B on  A.SourceKey=B.TargetKey

		  UNION 
		   SELECT COALESCE(A.SourceKey,B.TargetKey) AS [Key],A.' + @sourceColumn
                                       + ',B.' + @TargetColumn + '    FROM 
		 ( SELECT SourceKey,CAST(' + @sourceColumn + ' AS DATE) AS '
                                       + @sourceColumn + '  FROM #Source
		  EXCEPT 
		  SELECT TargetKey,CAST(' + @TargetColumn + ' AS DATE) AS '
                                       + @TargetColumn + '  FROM #Target)A
		  RIGHT JOIN 
		  (
		  SELECT TargetKey,CAST(' + @TargetColumn + ' AS DATE) AS '
                                       + @TargetColumn + '  FROM #Target
		  EXCEPT 
		  SELECT SourceKey,CAST(' + @sourceColumn + ' AS DATE) AS '
                                       + @sourceColumn + '  FROM #Source) 
		  B on  A.SourceKey=B.TargetKey  '
                                  ELSE ' SELECT COALESCE(A.SourceKey,B.TargetKey) AS [Key] ,A.'
                                       + @sourceColumn + ' ,B.'
                                       + @TargetColumn + ' FROM (
		  SELECT SourceKey,' + @SourceColumn + ' FROM #Source 
		  EXCEPT 
		  SELECT TargetKey,' + @TargetColumn + ' FROM #Target) A
		   LEFT JOIN 
		 ( SELECT TargetKey,' + @TargetColumn + ' FROM #Target
		  EXCEPT 
		  SELECT SourceKey,' + @sourceColumn
                                       + ' FROM #Source)B on  A.SourceKey=B.TargetKey

		  UNION ALL 
		   SELECT COALESCE(A.SourceKey,B.TargetKey) AS [Key],A.' + @sourceColumn
                                       + ',B.' + @TargetColumn + '    FROM 
		 ( SELECT SourceKey,' + @sourceColumn + ' FROM #Source
		  EXCEPT 
		  SELECT TargetKey,' + @TargetColumn + ' FROM #Target)A
		  RIGHT JOIN 
		  (
		  SELECT TargetKey,' + @TargetColumn + ' FROM #Target
		  EXCEPT 
		  SELECT SourceKey,' + @sourceColumn + ' FROM #Source) 
		  B on  A.SourceKey=B.TargetKey  '
                             END
        WHERE   MapID = @int
                AND [owner] IN ( 'RFO_Comm_OrderItems','RFO_Comm_OrderReturnItems' )
                AND flag IN ('c2c','ref','upd')
        SET @int = @int + 1
    END
