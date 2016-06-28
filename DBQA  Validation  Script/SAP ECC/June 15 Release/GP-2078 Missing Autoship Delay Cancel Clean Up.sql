--###############################################################
--		 VALIDATING MISSING AUTOSHIP CANCEL RECORDS BEEN LOADED 
--###############################################################


USE RFOperations
GO 

SELECT  oc.AccountID
FROM    [ETL].[synRFLiveAccountCancellation] ad
        INNER JOIN [ETL].[synRFLiveOrderCustomers] oc ON ad.AccountId = oc.AccountID
        INNER JOIN [ETL].[synRFLiveOrders] o ON oc.OrderID = o.OrderID
        INNER JOIN [ETL].[synRFLiveAccounts] a ON a.AccountID = ad.AccountId
        LEFT JOIN [ETL].[synRFLiveUsers] AS u ON u.UserID = ad.CreatedByUserId
WHERE   o.OrderTypeID IN ( 4, 5 )
        AND o.OrderStatusID = 5
EXCEPT
SELECT  ac.accountId
FROM    RFOperations.Logging.autoshipDelayCancellationLog ac
        JOIN RFOperations.Hybris.Autoship a ON a.AccountID = ac.accountId
WHERE   status = 'Cancel'
        AND a.CountryID = 236
		--103,686 Records in STG2.


--###############################################################
--	VALIDATING MISSING AUTOSHIP DELAY RECORDS NOT  BEEN LOADED 
--###############################################################
	
	
	
		USE RFOperations;
	 GO 
DECLARE @Autoshiplogid BIGINT;          


SELECT  @Autoshiplogid = MAX(autoshipDelayCancellationLogId)
FROM    RFOperations.Logging.autoshipDelayCancellationLog adc
        JOIN RFOperations.Hybris.Autoship a ON adc.templateId = CAST(a.AutoshipNumber AS BIGINT)
WHERE   status = 'Delay'
        AND a.CountryID = 236;  

		--PRINT @Autoshiplogid
		       
 
SELECT COUNT(DISTINCT ad.AutoshipDelayId) --Shold be Zero.
FROM    [ETL].[synRFLiveAutoshipDelay] ad
        INNER JOIN [ETL].[synRFLiveOrders] o ON o.OrderID = ad.TemplateOrderId
        INNER JOIN [ETL].[synRFLiveAccounts] a ON a.AccountID = ad.AccountId
        LEFT JOIN [ETL].[synRFLiveUsers] AS u ON u.UserID = ad.CreatedByUserId
WHERE   AutoshipDelayId >@Autoshiplogid

    --    2571967 RECORDS IN STG2.