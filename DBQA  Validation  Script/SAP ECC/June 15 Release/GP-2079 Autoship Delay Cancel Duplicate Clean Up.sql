--##############################################################
--		CHECKING DUPLICATE RECORDS IN AUTOSHIP DELAY 
--#############################################################

-- COMPARING TO DATETIME LOGDATE.
SELECT  COUNT(*) DuplicateDelayCounts,adl.templateId -- Should be Zero.
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
           
		   -- 10 RECORDS.

-- COMPARING TO DATE LOGDATE.

SELECT  COUNT(*) DuplicateDelayCounts,adl.templateId -- Should be Zero.
FROM    RFOperations.Logging.autoshipDelayCancellationLog adl
        JOIN RFOperations.Hybris.Autoship a ON adl.templateId = CAST(a.AutoshipNumber AS INT)
WHERE   CountryID = 236
        AND status = 'Delay' 
GROUP BY templateId ,
        adl.accountId ,
        [status] ,
       CAST( logDate AS DATE),
        daysSinceLastAutoship ,
        originalOrderDate ,
        newDate ,
        consecutiveDelayCount ,
        userType ,
        userName
HAVING  COUNT(*) > 1;



SELECT adl.* 
FROM    RFOperations.Logging.autoshipDelayCancellationLog adl
        JOIN RFOperations.Hybris.Autoship a ON adl.templateId = CAST(a.AutoshipNumber AS INT)
WHERE   CountryID = 236
        AND status = 'Delay'  AND adl.templateId=8587389




SELECT * FROM RodanFieldsLive.dbo.AutoshipDelay WHERE TemplateOrderId=8587389




--##############################################################
--		CHECKING DUPLICATE RECORDS IN AUTOSHIP CANCEL 
--#############################################################




           SELECT   COUNT(adl.accountId) DuplicateCancelCounts ,
                    adl.accountId --Should be Zero.
           FROM     RFOperations.Logging.autoshipDelayCancellationLog adl
           WHERE    EXISTS ( SELECT 1
                             FROM   RFOperations.Hybris.Autoship A
                             WHERE  A.AccountID = adl.accountId
                                    AND A.CountryID = 236 )
                    AND [status] = 'Cancel'
                    --AND adl.accountId = 2029152
           GROUP BY adl.accountId ,
                    [status] ,
                    logDate
           HAVING   COUNT(*) > 1;
           

		   
