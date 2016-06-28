

/* 2. Clean UP AccountStatusSincRFO */

--##############################################################################################################################
--CHECKING IF ANY INACTIVE ACCOUNTS HAVING SOFTTERMINATE DATE WIH NO HARDTERMINATED IS STILL OTHER STATUS THAN SOFTTERMINATED
--##############################################################################################################################


SELECT  --COUNT(*) [TOTAL RECOREDS] -- SHOULD BE NULL AFTER UPDATE
		ar.AccountID,ar.SoftTerminationDate,ar.HardTerminationDate,ar.Active,ab.AccountStatusID,s.Name [Account Status]
FROM    RFOperations.RFO_Accounts.AccountRF ar
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = ar.AccountID
        INNER JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
WHERE   CountryID = 236
        AND Active = 0
        AND SoftTerminationDate IS NOT NULL
        AND HardTerminationDate IS NULL
        AND ab.AccountStatusID NOT IN ( 8, 9 );--SOFTTERMINATED VOLUNTARY/INVOLUNTAR.
		
		-- 777108 RECORDS IN STG2.

	

--##############################################################################################################################
--CHECKING IF ANY INACTIVE ACCOUNTS HAVING SOFTTERMINATE DATE WIH  HARDTERMINATED IS STILL OTHER STATUS THAN HARDTERMINATED
--##############################################################################################################################		
		

SELECT   --COUNT(*) [TOTAL RECOREDS] -- SHOULD BE NULL AFTER UPDATE
		ab.AccountTypeID,ar.AccountID,ar.SoftTerminationDate,ar.HardTerminationDate,ar.Active,ab.AccountStatusID,s.Name [Account Status]
FROM    RFOperations.RFO_Accounts.AccountRF ar
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = ar.AccountID
		JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
WHERE   CountryID = 236
        AND Active =0
        AND SoftTerminationDate IS NOT NULL
        AND HardTerminationDate IS NOT NULL
        AND ab.AccountStatusID NOT IN ( 10, 2 );
		--232115 RECORDS IN STG2.




--############################################################################################
--CHECKING IF ANY ACTIVE ACCOUNTS  NOT HAVING ACCOUNTSTATUS ACTIVE IN ACCOUNT BASE TABLE
--#############################################################################################


SELECT COUNT(*) [TOTAL RECOREDS] -- SHOULD BE NULL AFTER UPDATE
		--ar.AccountID,ar.SoftTerminationDate,ar.HardTerminationDate,ar.Active,ab.AccountStatusID,s.Name [Account Status]
FROM    RFOperations.RFO_Accounts.AccountRF ar
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = ar.AccountID
WHERE   CountryID = 236
        AND Active = 1
        AND AccountStatusID NOT IN (1); 





--############################################################################################
--CHECKING IF ANY ACTIVE ACCOUNTS HAVING HARDTERMINATE DATE IS POPULATED.
--#############################################################################################
		  
         SELECT --COUNT(*) [TOTAL RECOREDS] -- SHOULD BE NULL AFTER UPDATE
		ab.AccountID,rf.SoftTerminationDate,rf.HardTerminationDate,rf.Active,ab.AccountStatusID,s.Name [Account Status],rf.ServerModifiedDate,ab.ServerModifiedDate
         FROM   RFOperations.RFO_Accounts.AccountBase ab
                JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
				JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
         WHERE  ab.AccountStatusID = 1
               AND   rf.Active = 0
                AND (rf.HardTerminationDate IS NOT NULL
                OR rf.SoftTerminationDate IS NOT NULL); 
		  
          SELECT    COUNT(*) [Error Counts] -- SHOULD NOT BE ANY.
          FROM      RFOperations.RFO_Accounts.AccountRF rf
                    JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
          WHERE     ab.AccountID = 236
                    AND rf.Active = 1
                    AND ( rf.SoftTerminationDate IS NOT NULL
                          OR rf.HardTerminationDate IS NOT NULL
                        );


		  --******************************************************
		  		  -- STAGING TABLE VALIDATION 
		  --******************************************************

		  -- ACCOUNTSTATUS=8
		
SELECT  COUNT(*)[RECORDS NOT UPDATED]
FROM    RFOperations..Cleaup_AccountStatus_1 a
        JOIN RFOperations.RFO_Accounts.AccountBase b ON a.AccountID = b.AccountID
WHERE   a.new_accountstatusid <> b.AccountStatusID;

		
 		  -- ACCOUNTSTATUS=10

SELECT   COUNT(*)[RECORDS NOT UPDATED]
FROM    RFOperations..Cleaup_AccountStatus_2 a
        JOIN RFOperations.RFO_Accounts.AccountBase b ON a.AccountID = b.AccountID
WHERE   a.new_accountstatusid <> b.AccountStatusID; 
		   

		    -- ACCOUNTSTATUS=1
SELECT   COUNT(*)[RECORDS NOT UPDATED]
FROM    RFOperations..Cleaup_AccountStatus_3 a
        JOIN RFOperations.RFO_Accounts.AccountBase b ON a.AccountID = b.AccountID
WHERE   a.new_accountstatusid <> b.AccountStatusID;



--#############################################################
--  ETL VALIDATION FOR ACCOUNT STATUS
--#############################################################

USE RFOperations
GO 
[ETL].[USPExceptStagingAccounts] 