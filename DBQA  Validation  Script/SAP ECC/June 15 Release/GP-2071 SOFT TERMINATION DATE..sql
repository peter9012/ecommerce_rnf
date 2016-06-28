
/* Getting total counts of modified by this Tag Softtermination cleanUp GP-2071 */

;
WITH    MaxOrderComptionDate
          AS ( SELECT DISTINCT
                        a.AccountID ,
                        a.AccountTypeID ,
                        MAX(ho.CompletionDate) AS MaxOrdCompletion
               FROM     RodanFieldsLive.dbo.Accounts (NOLOCK) a
                        JOIN RFOperations.RFO_Accounts.AccountRF (NOLOCK) b ON a.AccountID = b.AccountID
                        JOIN RFOperations.Hybris.Orders (NOLOCK) ho ON ho.AccountID = b.AccountID
                        JOIN RodanFieldsLive.dbo.Orders o ON ho.OrderID = o.OrderID
                                                             AND o.OrderTypeID NOT IN (
                                                             9 )
                                                             AND ISNULL(CompleteDate,
                                                              '1900-01-01') > '1900-01-02'
               WHERE    a.Active = 0 
                        AND ho.CountryId = 236
               GROUP BY a.AccountID ,
                        a.AccountTypeID
             )--SELECT count(*) FROM MaxOrderComptionDate
			 ,
        MaxActiveDate
          AS ( SELECT   a.AccountID ,
                        MAX(aa.AuditDate) AS MaxActiveDate
               FROM     MaxOrderComptionDate a
                        JOIN RodanFieldsLive.dbo.AccountsAudit (NOLOCK) aa ON a.AccountID = aa.AccountID
               WHERE    aa.Active = 1 
               GROUP BY a.AccountID
             )  --SELECT count(*) FROM MaxActiveDate
			 ,
        MinActiveDate
          AS ( SELECT   a.AccountID ,
                        MIN(aa.AuditDate) AS MinActiveDate
               FROM     MaxActiveDate a
                        JOIN RodanFieldsLive.dbo.AccountsAudit (NOLOCK) aa ON a.AccountID = aa.AccountID
               WHERE    Active = 0
                        AND aa.AuditDate > a.MaxActiveDate
               GROUP BY a.AccountID
             )
    --SELECT count(*) FROM MinActiveDate
    SELECT  a.AccountID ,
            a.AccountTypeID ,
            a.MaxOrdCompletion ,
            m.MaxActiveDate ,
            n.MinActiveDate ,
            rf.SoftTerminationDate ,
            rf.ChangedByUser ,
			rf.Active,
            CAST(rf.ServerModifiedDate AS DATE) ServerModifiedDate
    FROM    MaxOrderComptionDate a
            JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = a.AccountID
            LEFT JOIN MaxActiveDate m ON m.AccountID = a.AccountID
            LEFT JOIN MinActiveDate n ON n.AccountID = a.AccountID
    WHERE   rf.Active = 0
            AND CAST(rf.SoftTerminationDate AS DATE) <> CAST(CASE
                                                              WHEN m.MaxActiveDate IS NOT NULL
                                                              THEN MinActiveDate
                                                              WHEN a.AccountTypeID = 1
                                                              THEN DATEADD(dd,
                                                              30,
                                                              MaxOrdCompletion)
                                                              WHEN a.AccountTypeID = 2
                                                              THEN DATEADD(dd,
                                                              60,
                                                              MaxOrdCompletion)
                                                              WHEN a.AccountTypeID = 3
                                                              THEN DATEADD(dd,
                                                              30,
                                                              MaxOrdCompletion)
                                                             END AS DATE);
										-- AND rf.ChangedByUser LIKE '%GP-2071%'
    --GROUP BY rf.ChangedByUser,CAST(rf.ServerModifiedDate AS DATE)

	--643969


--9215 should be using CastDate.
--397735 without Cast date.


	/* Getting details of Not matching Softtermination Date */
	   
--9215 should be using CastDate.
--397735 without Cast date.
                                          


SELECT  COUNT(*) [Total Modified Records] ,
        CAST(rf.ServerModifiedDate AS DATE) [ServerModified Date]	
FROM    RFOperations.RFO_Accounts.AccountRF rf
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
WHERE   ab.CountryID = 236
AND rf.Active=1
        AND rf.ChangedByUser LIKE '%GP-2071%'
GROUP BY CAST(rf.ServerModifiedDate AS DATE);

SELECT * FROM RFOperations.Audit.RFOAccountsAccountRF WHERE AccountID=2577532 ORDER BY ServerModifiedDate
SELECT * FROM RFOperations.etl.Reftermination WHERE Accountid=2577532

--430061 Records been added SoftTermination Date In STG2. 


		
		/* ETL PROCESS VALIDATION CHECKING IF SOFTTERMINATIONDATE IS INSERTED OR NOT FOR INACTIVE ACCOUNTS. */

		IF OBJECT_ID(N'Tempdb.dbo.#t')IS NOT NULL 
		DROP TABLE #t


SELECT  *
INTO    #t
FROM    ( SELECT TOP 2
                    RF.AccountID
          FROM      RFOperations.RFO_Accounts.AccountRF RF
                    JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
          WHERE     Active = 1
                    AND CAST(RF.ServerModifiedDate AS DATE)>= CAST(GETDATE()
                    - 3 AS DATE)
                    AND AB.CountryID = 236
                    AND AB.AccountTypeID = 2
          ORDER BY  AB.ServerModifiedDate DESC
        ) t
UNION
SELECT  *
FROM    ( SELECT TOP 2
                    RF.AccountID
          FROM      RFOperations.RFO_Accounts.AccountRF RF
                    JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
          WHERE     Active = 1
                    AND CAST(RF.ServerModifiedDate AS DATE) >= CAST(GETDATE()
                    - 3 AS DATE)
                    AND AB.CountryID = 236
                    AND AB.AccountTypeID = 3
          ORDER BY  AB.ServerModifiedDate DESC
        ) t
UNION
SELECT  *
FROM    ( SELECT TOP 2
                    RF.AccountID
          FROM      RFOperations.RFO_Accounts.AccountRF RF
                    JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
          WHERE     Active = 1
                    AND CAST(RF.ServerModifiedDate AS DATE) >= CAST(GETDATE()
                    - 3 AS DATE)
                    AND AB.CountryID = 236
                    AND AB.AccountTypeID = 1
          ORDER BY  AB.ServerModifiedDate DESC
        ) t;


		--SELECT * FROM #t 



SELECT  Active ,
        *
FROM    RodanFieldsLive.dbo.Accounts
WHERE   AccountID IN ( 3365497, 3365500, 3365508, 3365509, 3365516, 3365517 );




BEGIN TRAN; 
UPDATE  RodanFieldsLive.dbo.Accounts
SET     Active = 0
WHERE   AccountID IN ( 3365497, 3365500, 3365508, 3365509, 3365516, 3365517 );
IF @@ERROR <> 0
    BEGIN
        PRINT 'Tran Rolled Back';
        ROLLBACK TRAN;
    END;
ELSE
    BEGIN
        PRINT 'Tran Committed';
        COMMIT TRAN;
    END;


	

	 
 SELECT s.Name [Account Status ] ,
        rf.*
 FROM   RFOperations.RFO_Accounts.AccountRF rf
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
        JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = ab.AccountStatusID
 WHERE  rf.AccountID IN ( 3365497, 3365500, 3365508, 3365509, 3365516, 3365517 );



;
WITH    MaxOrderComptionDate
          AS ( SELECT   a.AccountID ,
                        a.AccountTypeID ,
                        MAX(ho.CompletionDate) AS MaxOrdCompletion
               FROM     RodanFieldsLive.dbo.Accounts (NOLOCK) a
                        JOIN RFOperations.RFO_Accounts.AccountRF (NOLOCK) b ON a.AccountID = b.AccountID
                        JOIN RFOperations.Hybris.Orders (NOLOCK) ho ON ho.AccountID = b.AccountID
                        JOIN RodanFieldsLive.dbo.Orders o ON ho.OrderID = o.OrderID
                                                             AND o.OrderTypeID NOT IN (
                                                             9 )
                                                             AND ISNULL(CompleteDate,
                                                              '1900-01-01') > '1900-01-02'
               WHERE    a.Active = 0
                        AND ho.CountryId = 236
               GROUP BY a.AccountID ,
                        a.AccountTypeID
             )--SELECT count(*) FROM MaxOrderComptionDate
			 ,
        MaxActiveDate
          AS ( SELECT   a.AccountID ,
                        MAX(aa.AuditDate) AS MaxActiveDate
               FROM     MaxOrderComptionDate a
                        JOIN RodanFieldsLive.dbo.AccountsAudit (NOLOCK) aa ON a.AccountID = aa.AccountID
               WHERE    aa.Active = 1
               GROUP BY a.AccountID
             )-- SELECT count(*) FROM MaxActiveDate
			 ,
        MinActiveDate
          AS ( SELECT   a.AccountID ,
                        MIN(aa.AuditDate) AS MinActiveDate
               FROM     MaxActiveDate a
                        JOIN RodanFieldsLive.dbo.AccountsAudit (NOLOCK) aa ON a.AccountID = aa.AccountID
               WHERE    Active = 0
                        AND aa.AuditDate > a.MaxActiveDate
               GROUP BY a.AccountID
             )
    --SELECT count(*) FROM MinActiveDate
    SELECT  a.AccountID ,
            a.AccountTypeID ,
            rf.Active [RFO Active] ,
            rf.HardTerminationDate [RFO HardTermination Date] ,
            CASE WHEN m.MaxActiveDate IS NOT NULL THEN MinActiveDate
                 WHEN a.AccountTypeID = 1
                 THEN DATEADD(dd, 30, MaxOrdCompletion)
                 WHEN a.AccountTypeID = 2
                 THEN DATEADD(dd, 60, MaxOrdCompletion)
                 WHEN a.AccountTypeID = 3
                 THEN DATEADD(dd, 30, MaxOrdCompletion)
            END AS seftTerminateDate_New ,
            rf.SoftTerminationDate [RFO SoftTermination Date] ,
            a.MaxOrdCompletion ,
            m.MaxActiveDate ,
            n.MinActiveDate ,
            rf.ChangedByUser ,
            rf.ServerModifiedDate
    FROM    MaxOrderComptionDate a
            LEFT JOIN MaxActiveDate m ON m.AccountID = a.AccountID
            LEFT JOIN MinActiveDate n ON n.AccountID = a.AccountID
            JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = a.AccountID
    WHERE   rf.AccountID IN ( 3365497, 3365500, 3365508, 3365509, 3365516, 3365517 )
            AND CAST(rf.SoftTerminationDate AS DATE) = CAST(CASE
                                                              WHEN m.MaxActiveDate IS NOT NULL
                                                              THEN MinActiveDate
                                                              WHEN a.AccountTypeID = 1
                                                              THEN DATEADD(dd,
                                                              30,
                                                              MaxOrdCompletion)
                                                              WHEN a.AccountTypeID = 2
                                                              THEN DATEADD(dd,
                                                              60,
                                                              MaxOrdCompletion)
                                                              WHEN a.AccountTypeID = 3
                                                              THEN DATEADD(dd,
                                                              30,
                                                              MaxOrdCompletion)
                                                            END AS DATE);
	


/* Run the ETL SP */

USE [RFOperations];
GO
EXEC [ETL].[UpdateReftermination];  -- AccountStatus


USE [RFOperations];
GO
EXEC [ETL].[USPExceptStagingAccounts] -- Account Except 



/* Comparision Between BI and RFO */


SELECT  RF.AccountID ,
        RF.Active ,
        RF.SoftTerminationDate ,
        T.SoftTermDate ,
        RF.HardTerminationDate ,
        RF.ChangedByUser ,
        T.Notes
FROM    RFOperations.RFO_Accounts.AccountRF RF
        JOIN RodanFieldsBI.[dbo].[TerminationDates] T ON T.AccountID = RF.AccountID
WHERE   RF.SoftTerminationDate <> T.SoftTermDate;  


-- Casting Date.

SELECT  RF.AccountID ,
        RF.Active ,
        RF.SoftTerminationDate [RFO SoftTermination] ,
        T.SoftTermDate [BI SoftTermination] ,
        RF.HardTerminationDate [RFO HardTerminationDate] ,
        RF.ChangedByUser ,
        T.Notes AS [BI Account Notes]
FROM    RFOperations.RFO_Accounts.AccountRF RF
        JOIN RodanFieldsBI.[dbo].[TerminationDates] T ON T.AccountID = RF.AccountID
WHERE   CAST(RF.SoftTerminationDate AS DATE) <> CAST(T.SoftTermDate AS DATE);



SELECT  ab.*
FROM    RFOperations.RFO_Accounts.AccountRF (NOLOCK)rf
        JOIN RFOperations.RFO_Accounts.AccountBase(NOLOCK) ab ON ab.AccountID = rf.AccountID
WHERE   rf.Active =1
        AND ab.AccountStatusID <> 1;

        SELECT  ab.AccountStatusID ,
		ab.CountryID,
                rf.*
        FROM    RFOperations.RFO_Accounts.AccountRF (NOLOCK) rf
                JOIN RFOperations.RFO_Accounts.AccountBase (NOLOCK) ab ON ab.AccountID = rf.AccountID
        WHERE   Active = 1
                AND SoftTerminationDate IS NOT NULL; 













	--###############################################
	--- TEST ACCOUNTS SAMPLE FOR FRONT END VALIDATION 
	
	
	
	
        IF OBJECT_ID(N'Tempdb.dbo.#t') IS NOT NULL
            DROP TABLE #t;
	

        SELECT  *
INTO    #t
        FROM    ( SELECT TOP 5
                            'ACTIVE ACCOUNTS ' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            RF.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     ac.Active = 1
                            AND CAST(RF.ServerModifiedDate AS DATE) > CAST(GETDATE()
                            - 10 AS DATE)
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 2
                            AND ac.EmailAddress NOT LIKE '%test%'
                            AND ac.EmailAddress NOT LIKE '%terminated%'
                  ORDER BY  AB.ServerModifiedDate DESC
                ) t
        UNION
        SELECT  *
        FROM    ( SELECT TOP 5
                            'ACTIVE ACCOUNTS ' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            RF.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 1
                            AND CAST(RF.ServerModifiedDate AS DATE) >= CAST(GETDATE()
                            - 3 AS DATE)
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 3
                            AND ac.EmailAddress NOT LIKE '%test%'
                            AND ac.EmailAddress NOT LIKE '%terminated%'
                  ORDER BY  AB.ServerModifiedDate DESC
                ) t
        UNION
        SELECT  *
        FROM    ( SELECT TOP 5
                            'ACTIVE ACCOUNTS ' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            RF.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 1
                            AND CAST(RF.ServerModifiedDate AS DATE) >= CAST(GETDATE()
                            - 3 AS DATE)
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 1
                            AND ac.EmailAddress NOT LIKE '%test%'
                            AND ac.EmailAddress NOT LIKE '%terminated%'
                  ORDER BY  AB.ServerModifiedDate DESC
                ) t
        UNION
        SELECT  *
        FROM    ( SELECT TOP 5
                            'SOFT TERMINATED ' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            RF.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND RF.ChangedByUser LIKE '%GP-2071%'
                            AND RF.HardTerminationDate IS NULL
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 2
                  ORDER BY  AB.ServerModifiedDate DESC
                ) t
        UNION
        SELECT  *
        FROM    ( SELECT TOP 5
                            'SOFT TERMINATED ' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            RF.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND RF.ChangedByUser LIKE '%GP-2071%'
                            AND RF.HardTerminationDate IS NULL
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 3
                  ORDER BY  AB.ServerModifiedDate DESC
                ) t
        UNION
        SELECT  *
        FROM    ( SELECT TOP 5
                            'SOFT TERMINATED ' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            RF.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND RF.ChangedByUser LIKE '%GP-2071%'
                            AND RF.HardTerminationDate IS NULL
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 1
                  ORDER BY  AB.ServerModifiedDate DESC
                ) t
        UNION
        SELECT  *
        FROM    ( SELECT TOP 5
                            'SOFT N HARD TERMINATED ' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            RF.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND RF.ChangedByUser LIKE '%GP-2071%'
                            AND RF.ChangedByUser LIKE '%GP-2070%'
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 2
                  ORDER BY  AB.ServerModifiedDate DESC
                ) t
        UNION
        SELECT  *
        FROM    ( SELECT TOP 5
                            'SOFT N HARD TERMINATED ' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            RF.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND RF.ChangedByUser LIKE '%GP-2071%'
                            AND RF.ChangedByUser LIKE '%GP-2070%'
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 3
                  ORDER BY  AB.ServerModifiedDate DESC
                ) t
        UNION
        SELECT  *
        FROM    ( SELECT TOP 5
                            'SOFT N HARD TERMINATED ' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            RF.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND RF.ChangedByUser LIKE '%GP-2071%'
                            AND RF.ChangedByUser LIKE '%GP-2070%'
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 1
                  ORDER BY  AB.ServerModifiedDate DESC
                ) t
        UNION
        SELECT  *
        FROM    ( SELECT TOP 2
                            'SOFTTERMINATED STATUS' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            AB.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND AB.ChangedByUser LIKE '%GP-%'
                            AND AB.AccountStatusID = 8
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 3
         -- ORDER BY  AB.ServerModifiedDate DESC
                  UNION
                  SELECT TOP 2
                            'SOFTTERMINATED STATUS' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            AB.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND AB.ChangedByUser LIKE '%GP-%'
                            AND AB.AccountStatusID = 8
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 2
         -- ORDER BY  AB.ServerModifiedDate DESC
                  UNION
                  SELECT TOP 2
                            'SOFTTERMINATED STATUS' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            AB.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND AB.ChangedByUser LIKE '%GP-%'
                            AND AB.AccountStatusID = 8
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 1
          --ORDER BY  AB.ServerModifiedDate DESC
                ) t
        UNION
        SELECT  *
        FROM    ( SELECT TOP 2
                            'HARDTERMINATED STATUS' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            AB.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND AB.ChangedByUser LIKE '%GP-%'
                            AND AB.AccountStatusID = 10
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 2
         -- ORDER BY  AB.ServerModifiedDate DESC
                  UNION
                  SELECT TOP 2
                            'HARDTERMINATED STATUS' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            AB.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND AB.ChangedByUser LIKE '%GP-%'
                            AND AB.AccountStatusID = 10
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 3
         -- ORDER BY  AB.ServerModifiedDate DESC
                  UNION
                  SELECT TOP 2
                            'HARDTERMINATED STATUS' [SCENARIOS] ,
                            RF.AccountID ,
                            ac.EmailAddress ,
                            s.Name [Account Status] ,
                            t.Name [Account Types] ,
                            RF.SoftTerminationDate ,
                            RF.HardTerminationDate ,
                            RF.Active ,
                            AB.ChangedByUser
                  FROM      RFOperations.RFO_Accounts.AccountRF RF
                            JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                            JOIN RFOperations.RFO_Reference.AccountStatus s ON s.AccountStatusID = AB.AccountStatusID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = AB.AccountID
                            JOIN RFOperations.RFO_Reference.AccountType t ON t.AccountTypeID = AB.AccountTypeID
                  WHERE     RF.Active = 0
                            AND AB.ChangedByUser LIKE '%GP-%'
                            AND AB.AccountStatusID = 10
                            AND AB.CountryID = 236
                            AND AB.AccountTypeID = 1
          --ORDER BY  AB.ServerModifiedDate DESC
                ) t;



				SELECT * FROM #t ORDER BY [SCENARIOS],[Account Status],[Account Types]			