--####################################################################################
--		VALIDATING IF MISSING OR EXTRA  SKUWAPAUDIT RECORDS FROM RFL TO RFO
--###################################################################################

SELECT  CASE WHEN RFL.TemplateOrderID IS NULL THEN 'MISSING IN RFL'
             WHEN RFO.TemplateId IS NULL THEN 'MISSING IN RFO'
			 ELSE 'No Missing'
        END AS [MISSING RECORDS FROM] ,
        RFL.TemplateOrderID ,
        RFO.TemplateId ,
        RFL.AccountID ,
        RFO.AccountId ,
        RFL.ProductID ,
        RFO.ProductId ,
        RFL.LogDate ,
        RFO.LogDate
FROM    RodanFieldsLive.dbo.SKUSwapAudit RFL
        FULL OUTER JOIN RFOperations.Logging.autoshipSwapLog RFO ON RFO.AccountId = RFL.AccountID
                                                              AND RFO.TemplateId = RFL.TemplateOrderID
															 --AND  RFL.LogDate =  RFO.LogDate  
                                                         AND LEFT (RFL.LogDate ,14) = LEFT (RFO.LogDate ,14)
WHERE  EXISTS (SELECT 1 FROM RFOperations.Hybris.Autoship A WHERE A.AutoshipID=RFO.TemplateId AND A.CountryID=236)
AND  (RFL.TemplateOrderID IS NULL
        OR RFO.TemplateId IS NULL); 
		--773,036 RECORDS IN STG2.


		SELECT LogDate,ProductId,[Status],DateLastCreated,ServerModifiedDate FROM RFOperations.Logging.autoshipSwapLog WHERE TemplateId=6611085

		SELECT LogDate,ProductId,[Status],DateLastCreated FROM RodanFieldsLive.dbo.SKUSwapAudit WHERE TemplateOrderID=6611085


---792,918 RECORDS WITHOUT CASTING.
--- 648,026 AFTER CASTING TO DATE


---NEED TO COMMENT TO TICKET ABOUT CASTING LOGDATE TO LEFT 14 OR UPDATE DATE WITH HOURS.?????????

SELECT  CASE WHEN RFL.TemplateOrderID IS NULL THEN 'MISSING IN RFL'
             WHEN RFO.TemplateId IS NULL THEN 'MISSING IN RFO'
             ELSE 'No Missing'
        END AS [MISSING RECORDS FROM] ,
        RFL.TemplateOrderID ,
        RFO.TemplateId ,
        RFL.AccountID ,
        RFO.AccountId ,
        RFL.ProductID ,
        RFO.ProductId ,
        RFL.LogDate ,
        RFO.LogDate
FROM    RodanFieldsLive.dbo.SKUSwapAudit RFL
        FULL OUTER JOIN RFOperations.Logging.autoshipSwapLog RFO ON RFO.AccountId = RFL.AccountID
                                                              AND RFO.TemplateId = RFL.TemplateOrderID
                                                              --AND CAST(RFL.LogDate AS DATE) = CAST(RFO.LogDate AS DATE)
															     AND  RFL.LogDate =  RFO.LogDate  
WHERE   EXISTS ( SELECT 1
                 FROM   RFOperations.Hybris.Autoship A
                 WHERE  A.AutoshipID = RFO.TemplateId
                        AND A.CountryID = 236 )
        AND ( RFL.TemplateOrderID IS NULL
              OR RFO.TemplateId IS NULL
            )
        AND RFO.LogDate > '2016-06-15';

		SELECT * FROM RFOperations.Logging.autoshipSwapLog WHERE TemplateId=8624267
		SELECT * FROM RodanFieldsLive.dbo.SKUSwapAudit WHERE TemplateOrderID=8624267

--#############################################################
--		REPLICATION VALIDATION 
--#############################################################


-- UPDAING IN SOURCE RFL 
SELECT LogDate,*
FROM    RodanFieldsLive.dbo.SKUSwapAudit
WHERE CAST(LogDate AS DATE) >= '2016-06-15'
    AND TemplateOrderID IN ( 24258131, 24047542 )




UPDATE  RodanFieldsLive.dbo.SKUSwapAudit
SET     LogDate = GETDATE()
WHERE   CAST(LogDate AS DATE) = '2016-06-15'
        AND TemplateOrderID IN ( 24258131, 24047542 );




SELECT  *
FROM    RodanFieldsLive.dbo.SKUSwapAudit
WHERE   CAST(LogDate AS DATE) = CAST(GETDATE()-1 AS DATE)
       AND TemplateOrderID IN ( 24258131, 24047542 )
ORDER BY LogDate DESC;-- RUN IN SOURCE 

---CHECKING UPDATED FROM SOURCE RFL TO TARGET RFL 

SELECT  *
FROM    RodanFieldsLive.dbo.SKUSwapAudit
WHERE   CAST(LogDate AS DATE) = CAST(GETDATE()-1  AS DATE)
       AND TemplateOrderID IN ( 24258131, 24047542 )
ORDER BY LogDate DESC;  -- RUN IN TARGET


    SELECT  COUNT(*),LogDate,TemplateOrderID
    FROM    RodanFieldsLive.dbo.SKUSwapAudit
    WHERE   CAST(LogDate AS DATE) = CAST(GETDATE() AS DATE)
	GROUP BY LogDate,TemplateOrderID

	 SELECT   COUNT(*),LogDate,a.TemplateId
    FROM    RFOperations.Logging.autoshipSwapLog a
    WHERE   a.ChangedByApplication = 'RFLive'
            AND CAST(LogDate AS DATE) = CAST(GETDATE() AS DATE)
	GROUP BY LogDate,a.TemplateId
	
	
	 SELECT  DISTINCT *
    FROM    RodanFieldsLive.dbo.SKUSwapAudit
    WHERE   CAST(LogDate AS DATE) = CAST(GETDATE() AS DATE) AND TemplateOrderID=4020251

	--EXCEPT
    SELECT *
    FROM    RFOperations.Logging.autoshipSwapLog a
    WHERE   a.ChangedByApplication = 'RFLive'
            AND CAST(LogDate AS DATE) = CAST(GETDATE() AS DATE)AND a.TemplateId=4020251

     


--#############################################################
--	 RFL AUTOSHIP SKUSWAP VS RFO AUTOSHIP SKUSWAP VALIDATION 
--#############################################################



SELECT  MAX(LogDate) [RFL Max LodDate]
FROM    RodanFieldsLive.dbo.SKUSwapAudit; 
SELECT  MAX(a.LogDate) [RFO Max LogDate]
FROM    RFOperations.Logging.autoshipSwapLog a
        JOIN RFOperations.Hybris.Autoship b ON a.TemplateId = b.AutoshipID
WHERE   b.CountryID = 236;


---CHECKING UPDATED FROM SOURCE RFL TO TARGET RFL 

SELECT  COUNT(*)
FROM    RodanFieldsLive.dbo.SKUSwapAudit
WHERE    LogDate  > '2016-06-23 15:29:52.377';



SELECT  COUNT(*)
FROM    RodanFieldsLive.dbo.SKUSwapAudit
WHERE   CAST(LogDate AS DATE) >= '2016-05-11';


SELECT  COUNT(*)
FROM    RFOperations.Logging.autoshipSwapLog a
WHERE   LogDate >= '2016-06-23 15:43:22.820'
        AND a.ChangedByApplication = 'RFlive';


			
SELECT  COUNT(*)
FROM    RodanFieldsLive.dbo.SKUSwapAudit
WHERE   LogDate >= '2016-06-23 15:43:22.820';




--DELETE RFOperations.Logging.autoshipSwapLog
--WHERE   CAST(LogDate AS DATE) >= '2016-05-11';

SELECT COUNT(*) FROM 
--DELETE 
RodanFieldsLive.dbo.SKUSwapAudit
WHERE   CAST(LogDate AS DATE) >= '2016-05-11';

 

SELECT a.LogDate,a.TemplateId
FROM    RFOperations.Logging.autoshipSwapLog a
WHERE    LogDate >= '2016-06-23 15:43:22.820'  
AND a.ChangedByApplication='RFlive'
 
EXCEPT
SELECT LogDate,TemplateOrderID
FROM    RodanFieldsLive.dbo.SKUSwapAudit
WHERE   LogDate >= '2016-06-23 15:43:22.820' 





































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































-- CHECKING BACK TO RFO RECORDS BEEN FLOWN OR NOT 
--DELETE
SELECT  LogDate ,
        *
FROM    RFOperations.Logging.autoshipSwapLog
WHERE   TemplateId IN ( 24258131, 24047542 );


