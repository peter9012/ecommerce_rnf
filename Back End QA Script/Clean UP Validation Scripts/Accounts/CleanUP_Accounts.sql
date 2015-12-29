

/* 2. Clean UP AccountStatusSincRFO */
--Please Capture the counts before Clean up to Validate after Cleanup.

SELECT  COUNT(*)-- Counts Should be Captured
FROM    RFOperations.RFO_Accounts.AccountRF ar
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = ar.AccountID
WHERE   CountryID = 236
        AND Active = 0
        AND SoftTerminationDate IS NOT NULL
        AND HardTerminationDate IS NULL
        AND ab.AccountStatusID NOT IN ( 8, 9 );
		
		
SELECT  ab.AccountStatusID ,
        ar.*
FROM    RFOperations.RFO_Accounts.AccountRF ar
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = ar.AccountID
WHERE   CountryID = 236
        AND Active = 0
        AND SoftTerminationDate IS NOT NULL
        AND HardTerminationDate IS NOT NULL
        AND ab.AccountStatusID NOT IN ( 10, 2 );
		
SELECT  COUNT(*)
FROM    RFOperations.RFO_Accounts.AccountRF ar
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = ar.AccountID
WHERE   CountryID = 236
        AND Active = 1
        AND AccountStatusID <> 1;


SELECT  COUNT(*)
FROM    RFOperations.RFO_Accounts.AccountRF (NOLOCK) ar
        INNER JOIN RFOperations.RFO_Accounts.AccountBase (NOLOCK) ab ON ab.AccountID = ar.AccountID
WHERE   CountryID = 236
        AND Active = 0
        AND SoftTerminationDate IS NOT NULL
        AND HardTerminationDate IS NOT NULL
        AND ( ab.AccountStatusID <> 10
              AND ab.AccountStatusID <> 2
            );

SELECT  COUNT(*)
FROM    RFOperations.RFO_Accounts.AccountRF (NOLOCK) ar
        INNER JOIN RFOperations.RFO_Accounts.AccountBase (NOLOCK) ab ON ab.AccountID = ar.AccountID
WHERE   CountryID = 236
        AND Active = 0
        AND SoftTerminationDate IS NOT NULL
        AND HardTerminationDate IS NULL
        AND ab.AccountStatusID <> 8;




/*****    3.Account NextRenewal Date *************/

SELECT  COUNT(*)--capture the Counts of Total.
FROM    RFOperations.RFO_Accounts.AccountRF a
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
        JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ab.AccountID = ac.AccountId
WHERE   NextRenewalDate IS NOT NULL
        AND ab.CountryID = 236
        AND FirstName NOT LIKE '%test%';
		   
		  
SELECT  COUNT(*) --Should be NULL 
FROM    RFOperations.RFO_Accounts.AccountRF RF
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = RF.AccountID
WHERE   ab.CountryID = 236
        AND ab.AccountTypeID = 1
        AND DATEADD(YEAR, 1, COALESCE(RF.LastRenewalDate, RF.EnrollmentDate)) <> RF.NextRenewalDate;

		   
SELECT  COUNT(*) ,
        CAST(RF.ServerModifiedDate AS DATE) ModifiedDate --Should be Modified as the same day.
FROM    RFOperations.RFO_Accounts.AccountRF RF
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = RF.AccountID
WHERE   ab.CountryID = 236
        AND ab.AccountTypeID = 1
        AND DATEADD(YEAR, 1, COALESCE(RF.LastRenewalDate, RF.EnrollmentDate)) = RF.NextRenewalDate
GROUP BY CAST(RF.ServerModifiedDate AS DATE);
		   

	
							
 		 
SELECT  COUNT(*)-- Should be NULL for Consultants after Clean up .
FROM    RFOperations.RFO_Accounts.AccountRF RF
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = RF.AccountID
WHERE   ab.CountryID = 236
        AND ab.AccountTypeID = 1
        AND RF.NextRenewalDate IS NULL;

		   

SELECT  COUNT(*)-- Should be NULL for Others.
FROM    RFOperations.RFO_Accounts.AccountRF RF
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = RF.AccountID
WHERE   ab.CountryID = 236
        AND ab.AccountTypeID <> 1
        AND RF.NextRenewalDate IS NOT NULL;

		
--9.PC RC Hard TerminationDate Cleanup
 

SELECT  COUNT(*) --Should be NULL after CleanUP
FROM    RFOperations.RFO_Accounts.AccountBase a
        JOIN RFOperations.RFO_Accounts.AccountRF b ON b.AccountID = a.AccountID
WHERE   a.CountryID = 236
        AND b.HardTerminationDate IS NULL
        AND DATEADD(s, -1,
                    DATEADD(mm, DATEDIFF(m, 0, SoftTerminationDate) + 2, 0)) < GETDATE()
        AND a.AccountTypeID IN ( 2, 3 );
--420344
   