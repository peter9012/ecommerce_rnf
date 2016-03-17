------- RFO Cleanup Script for NextRenewalDate  GP-2070 ------
USE RFOperations
GO 


/* Developer's Code 


------- RFO Cleanup Script for NextRenewalDate------
USE RFOperations;
GO 

--DISABLE TRIGGER RFO_Accounts.[utAuditRFOAccountsAccountRF] ON RFO_Accounts.AccountRF


SELECT  COUNT(*)
FROM    RFOperations.RFO_Accounts.AccountRF a
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
        JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ab.AccountID = ac.AccountId
WHERE   NextRenewalDate IS NOT NULL
        AND ab.CountryID = 236
        AND FirstName NOT LIKE '%test%';
		   --15 records in STG2 before Update
		   --168360 records in STG2 after Update.

		   
DECLARE @Getdate DATETIME2 = GETDATE();
SELECT  RF.AccountID ,
        NextRenewalDate ,
        CASE WHEN LastRenewalDate IS NOT NULL

             THEN DATEADD(YEAR, 1, RF.LastRenewalDate)
             WHEN EnrollmentDate >= DATEADD(YEAR, -1, CAST(GETDATE() AS DATE))
             THEN DATEADD(YEAR, 1, EnrollmentDate)
             ELSE NextRenewalDate
        END AS New_NextRenewalDate ,
        RF.ServerModifiedDate ,
        @Getdate AS newServerModifiedDate
INTO    RFOperations..Cleaup_AccountNextRenewalDate_1
FROM    RFOperations.RFO_Accounts.AccountRF RF
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = RF.AccountID
WHERE   ab.CountryID = 236
        AND ab.AccountTypeID = 1; 

		--298477 Counts before Update in STG2

		   -- 0 Records 

BEGIN TRAN;  
DECLARE @Getdate DATETIME2 = GETDATE();	   
UPDATE  RF
SET     NextRenewalDate = New_NextRenewalDate ,
        --RF.ServerModifiedDate = @Getdate ,
        RF.ChangedByApplication = 'US-HybrisLaunch' ,
        RF.ChangedByUser = 'GP-1790'
FROM    RFOperations.RFO_Accounts.AccountRF RF
        JOIN RFOperations..Cleaup_AccountNextRenewalDate_1 ab ON ab.AccountID = RF.AccountID;
		   --WHERE ab.CountryID =236 AND ab.AccountTypeID = 1 


-- COMMIT 

--ENABLE TRIGGER RFO_Accounts.[utAuditRFOAccountsAccountRF] ON RFO_Accounts.AccountRF


*/



/* Checking if Any Accounts with NextRenewalDate is NULL after Update. */



    SELECT  COUNT(*)--Should be Null after updates.
	--ab.AccountID,ab.AccountTypeID,NextRenewalDate,LastRenewalDate,EnrollmentDate,ab.CountryID 
    FROM    RFOperations.RFO_Accounts.AccountRF RF
            JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = RF.AccountID
    WHERE   ab.CountryID = 236
            AND ab.AccountTypeID = 1
            AND RF.NextRenewalDate IS NULL; 




		  /* Checking IF Any Accounts Updating  NextRenewalDate equal to Plus 1 Year Of  LastRenewal date 
		  OR plus 1 Year of Enrollmentdate if LastRenewalDate is NULL    */


		  -- for Business Rule: https://rodanandfields.atlassian.net/wiki/display/GP/US+Hybris+Launch+Data+Migration%3A+Business+Rules+Implemented

    SELECT  COUNT(*) --Should be Null after update.
	--SELECT ab.AccountID,ab.AccountTypeID,NextRenewalDate,LastRenewalDate,EnrollmentDate,ab.CountryID 
    FROM    RFOperations.RFO_Accounts.AccountRF RF
            JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = RF.AccountID
			JOIN Hybris..users u ON u.p_rfaccountid=CAST(rf.accountID AS NVARCHAR)
    WHERE   ab.CountryID = 236
            AND ab.AccountTypeID = 1
            AND ( ( LastRenewalDate IS NULL
                    AND NextRenewalDate <> DATEADD(YEAR, 1, EnrollmentDate)
                    AND EnrollmentDate >= DATEADD(YEAR, -1,
                                                  CAST(GETDATE() AS DATE))
                  )
                  OR ( LastRenewalDate IS NOT NULL
                       AND NextRenewalDate <> DATEADD(YEAR, 1,
                                                      RF.LastRenewalDate)
                     )
                  OR ( LastRenewalDate IS   NULL
                       AND NextRenewalDate IS NOT NULL 
                       AND EnrollmentDate < DATEADD(YEAR, -2,
                                                    CAST(GETDATE() AS DATE)))
				OR (lastRenewalDate IS NULL 
				AND NextRenewalDate IS NULL 
				AND DATEPART(YEAR, EnrollmentDate)=DATEPART(YEAR,GETDATE()))
                     
                );



		--AND rf.ChangedByApplication = 'US-HybrisLaunch'AND  rf.ChangedByUser = 'GP-1790'



		/* Delta Handling with adding One more fields in Staging */


		--Related StoredProcedure loading Staging :  RFOperations.[ETL].[USPExceptStagingAccounts]  



		--FirstStep to Add fields in Staging Table.

        ALTER TABLE RFOperations.ETL.StagingAccounts 
        ADD NextRenewalDate DATETIME;


	/* Validation added Column in Staging Accounts table.*/

	
SELECT * FROM RFOperations.INFORMATION_SCHEMA.Columns 
WHERE TABLE_NAME LIKE '%StagingAccounts' AND COLUMN_NAME='NextRenewalDate'




		/* Modification within SP.*/
			
		
		/*GP-2072 RFO-CleanUp Next renewal date*/


		--2nd Step to Update Null with using Logic.

        UPDATE  a
        SET     NextRenewalDate = CASE WHEN a.LastRenewal IS NOT NULL
                                       THEN DATEADD(YEAR, 1, a.LastRenewal)
                                       WHEN a.EnrollmentDate >= DATEADD(YEAR,
                                                              -1,
                                                              CAST(GETDATE() AS DATE))
                                       THEN DATEADD(YEAR, 1, a.EnrollmentDate)
                                       ELSE a.NextRenewalDate
                                  END
        FROM    RFOperations.ETL.StagingAccounts a
                INNER JOIN [ETL].[synRFLiveAccounts] asu ON a.AccountID = asu.AccountID
        WHERE   a.AccountTypeID = 1 



		 SELECT  COUNT(*) --Should be Null after update.
	--SELECT ab.AccountID,ab.AccountTypeID,NextRenewalDate,LastRenewalDate,EnrollmentDate,ab.CountryID 
    FROM    RFOperations.RFO_Accounts.AccountRF RF
            JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = RF.AccountID
    WHERE   ab.CountryID = 236
            AND ab.AccountTypeID = 1
            AND ( ( LastRenewalDate IS NULL
                    AND NextRenewalDate <> DATEADD(YEAR, 1, EnrollmentDate)
                    AND EnrollmentDate >= DATEADD(YEAR, -1,
                                                  CAST(GETDATE() AS DATE))
                  )
                  OR ( LastRenewalDate IS NOT NULL
                       AND NextRenewalDate <> DATEADD(YEAR, 1,
                                                      RF.LastRenewalDate)
                     )
                  OR ( LastRenewalDate IS   NULL
                       AND NextRenewalDate IS NOT NULL 
                       AND EnrollmentDate < DATEADD(YEAR, -1,
                                                    CAST(GETDATE() AS DATE))
                     )
                );