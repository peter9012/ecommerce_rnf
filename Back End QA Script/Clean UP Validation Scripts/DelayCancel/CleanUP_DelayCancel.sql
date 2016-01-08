

/* 6. Clean Up For AutoshipDelayCancellation -Cancel */

USE RFOperations;
GO 

SELECT  oc.AccountID  --Shoul be NULL after Cleaned UP.
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
        AND a.CountryID = 236;








/* 7.Clean up for AutoshipDelayCancellation -Delay  */


USE RFOperations;
	 GO 
DECLARE @Autoshiplogid BIGINT;          


SELECT  @Autoshiplogid = MAX(autoshipDelayCancellationLogId)
FROM    RFOperations.Logging.autoshipDelayCancellationLog adc
        JOIN RFOperations.Hybris.Autoship a ON adc.templateId = CAST(a.AutoshipNumber AS BIGINT)
WHERE   status = 'Delay'
        AND a.CountryID = 236;  
		       
 
SELECT COUNT(DISTINCT ad.AutoshipDelayId) --Shold be Zero.
FROM    [ETL].[synRFLiveAutoshipDelay] ad
        INNER JOIN [ETL].[synRFLiveOrders] o ON o.OrderID = ad.TemplateOrderId
        INNER JOIN [ETL].[synRFLiveAccounts] a ON a.AccountID = ad.AccountId
        LEFT JOIN [ETL].[synRFLiveUsers] AS u ON u.UserID = ad.CreatedByUserId
WHERE   AutoshipDelayId >@Autoshiplogid
      




	  /* 10.Delete Scripts for Duplicate Delay records */

SELECT  COUNT(*) DuplicateDelayCounts -- Should be Zero.
FROM    RFOperations.Logging.autoshipDelayCancellationLog adl
        JOIN RFOperations.Hybris.Autoship a ON adl.templateId = CAST(a.AutoshipNumber AS INT)
WHERE   CountryID = 236
        AND status = 'Delay'
GROUP BY templateId ,
        adl.accountId ,
        [status] ,
        logDate ,
        daysSinceLastAutoship ,
        originalOrderDate ,
        newDate ,
        consecutiveDelayCount ,
        userType ,
        userName
HAVING  COUNT(*) > 1;
           


		   SELECT 
		   COUNT(*) DuplicateCancelCounts --Should be Zero.
FROM    RFOperations.Logging.autoshipDelayCancellationLog adl
        JOIN RFOperations.Hybris.Autoship a ON adl.templateId = CAST(a.AutoshipNumber AS INT)
WHERE   CountryID = 236
        AND status = 'Cancel'
GROUP BY templateId ,
        adl.accountId ,
        [status] ,
        logDate ,
        daysSinceLastAutoship ,
        originalOrderDate ,
        newDate ,
        consecutiveDelayCount ,
        userType ,
        userName
HAVING  COUNT(*) > 1;
           