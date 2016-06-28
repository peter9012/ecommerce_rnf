IF OBJECT_ID( N'Tempdb..#ValidAccount') IS NOT NULL
DROP TABLE #ValidAccount
IF OBJECT_ID( N'Tempdb..#EmailHard') IS NOT NULL
DROP TABLE #EmailHard
IF OBJECT_ID( N'Tempdb..#Notes') IS NOT NULL
DROP TABLE #Notes
IF OBJECT_ID( N'Tempdb..#Email') IS NOT NULL
DROP TABLE #Email



 SELECT  DISTINCT
        ab.AccountID
 INTO   #ValidAccount
 FROM   RFOperations.RFO_Accounts.AccountBase ab WITH ( NOLOCK )
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
        JOIN RFOperations.Hybris.Orders o WITH ( NOLOCK ) ON o.AccountID = ab.AccountID
        JOIN RodanFieldsLive.dbo.Orders ro WITH ( NOLOCK ) ON ro.OrderID = o.OrderID
        JOIN RodanFieldsLive.dbo.OrderCustomers oc WITH ( NOLOCK ) ON oc.OrderID = ro.OrderID
                                                              AND oc.AccountID = ab.AccountID
 WHERE  ab.CountryID = 236
        AND ro.OrderStatusID IN ( 4, 5, 9 )
        AND ro.OrderTypeID NOT IN ( 4, 5, 9 );


						
   -- SELECT  COUNT(*) FROM    #ValidAccount  --2159193 Records in STG2.


 SELECT v.AccountID ,
        MIN(n.DateCreated) AS NoteHardTerminatedDate
 INTO   #Notes
 FROM   #ValidAccount v       
        JOIN RodanFieldsLive.dbo.Notes n WITH ( NOLOCK ) ON n.AccountID = v.AccountID
 WHERE  n.NoteText LIKE '%hard terminating%'
 GROUP BY v.AccountID;


 --SELECT COUNT(*) FROM  #Notes -- 743400 Records in STG2.


           
 SELECT a.AccountID ,
        MIN(aa.AuditDate) EmailHardTerminatedDate
 INTO   #EmailHard
 FROM   RodanFieldsLive.dbo.Accounts a
        JOIN RodanFieldsLive.dbo.AccountsAudit aa ON aa.AccountID = a.AccountID
        JOIN #ValidAccount v ON v.AccountID = a.AccountID
        JOIN ( SELECT DISTINCT
                        aa.AccountID ,
                        MAX(aa.AuditDate) AS MaxAuditdate
               FROM     RodanFieldsLive.dbo.AccountsAudit aa WITH ( NOLOCK )
               WHERE    aa.Active = 1
               GROUP BY aa.AccountID
             ) had ON had.AccountID = a.AccountID
 WHERE  NOT EXISTS ( SELECT 1
                     FROM   #Notes n
                     WHERE  n.AccountID = a.AccountID )-- Excluding records for having notes.
        AND aa.EmailAddress LIKE '%terminat%'
        AND aa.Active = 0
        AND aa.AuditDate > had.MaxAuditdate
 GROUP BY a.AccountID;
          
 
 --SELECT COUNT(*) FROM  #EmailHard -- 908 Records in STG2.       


 SELECT a.AccountID ,
        MIN(aa.AuditDate) EmailHardTerminatedDate
 INTO   #Email
 FROM   RodanFieldsLive.dbo.Accounts a
        JOIN RodanFieldsLive.dbo.AccountsAudit aa ON aa.AccountID = a.AccountID
        JOIN #ValidAccount v ON v.AccountID = a.AccountID
        JOIN ( SELECT DISTINCT
                        aa.AccountID ,
                        MAX(aa.AuditDate) AS MaxAuditdate
               FROM     RodanFieldsLive.dbo.AccountsAudit aa WITH ( NOLOCK )
               WHERE    aa.Active = 1
               GROUP BY aa.AccountID
             ) mad ON mad.AccountID = a.AccountID
 WHERE  NOT EXISTS ( SELECT 1
                     FROM   #Notes n
                     WHERE  n.AccountID = a.AccountID )-- Excluding records for having notes.
        AND NOT EXISTS ( SELECT 1
                         FROM   #EmailHard h
                         WHERE  h.AccountID = a.AccountID )
        AND ( aa.EmailAddress LIKE '%cancel%'
              OR aa.EmailAddress LIKE '%optout%'
              OR aa.EmailAddress LIKE '%opout%'
              OR aa.EmailAddress LIKE '%inactive%'
              OR aa.EmailAddress LIKE '%inative%'
              OR aa.EmailAddress LIKE '%invalid%'
            )
        AND aa.AuditDate > mad.MaxAuditdate
 GROUP BY a.AccountID;


 
 --SELECT COUNT(*) FROM #Email -- 11 Records in STG2.  



 --FOR NEGATIVE RECORDS 
 
IF OBJECT_ID(N'Tempdb..#ClenUp_hardtermination')IS NOT NULL
DROP TABLE  #ClenUp_hardtermination

 SELECT rf.AccountID ,
 ab.AccountTypeID,
        COALESCE(n.NoteHardTerminatedDate, h.EmailHardTerminatedDate,
                 m.EmailHardTerminatedDate) AS [New Hardteminationdate] ,
        rf.HardTerminationDate [RF Hard Date] ,
		rf.SoftTerminationDate,
		rf.ServerModifiedDate,
        n.NoteHardTerminatedDate [Note Date] ,
        h.EmailHardTerminatedDate [Terminated Date] ,
        m.EmailHardTerminatedDate [Cancel Date]
 INTO    #ClenUp_hardtermination -- Clean Up Back Up table.
 FROM   RFOperations.RFO_Accounts.AccountRF rf
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
        LEFT JOIN #Notes n ON n.AccountID = rf.AccountID
        LEFT JOIN #EmailHard h ON h.AccountID = rf.AccountID
        LEFT JOIN #Email m ON m.AccountID = rf.AccountID
 WHERE  ab.CountryID = 236
        --AND CAST(COALESCE(n.NoteHardTerminatedDate, h.EmailHardTerminatedDate,
        --             m.EmailHardTerminatedDate) AS DATE)<>CAST(rf.HardTerminationDate AS DATE)

		AND  COALESCE(n.NoteHardTerminatedDate, h.EmailHardTerminatedDate,
                     m.EmailHardTerminatedDate) <> rf.HardTerminationDate 
        AND ( n.AccountID IS NOT NULL
              OR h.AccountID IS NOT NULL
              OR m.AccountID IS NOT NULL
            );
				


SELECT * FROM  #ClenUp_hardtermination a 
JOIN RFOperations.RFO_Accounts.AccountRF b ON b.AccountID = a.AccountID





 ---FOR  POSITIVE COUNTS 
           
IF OBJECT_ID(N'Tempdb..#ClenUp_hardtermination')IS NOT NULL
DROP TABLE  #ClenUp_hardtermination

 SELECT rf.AccountID ,
 ab.AccountTypeID,
        COALESCE(n.NoteHardTerminatedDate, h.EmailHardTerminatedDate,
                 m.EmailHardTerminatedDate) AS [New Hardteminationdate] ,
        rf.HardTerminationDate [RF Hard Date] ,
		rf.SoftTerminationDate,
		rf.ServerModifiedDate,
        n.NoteHardTerminatedDate [Note Date] ,
        h.EmailHardTerminatedDate [Terminated Date] ,
        m.EmailHardTerminatedDate [Cancel Date]
 INTO    #ClenUp_hardtermination -- Clean Up Back Up table.
 FROM   RFOperations.RFO_Accounts.AccountRF rf
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
        LEFT JOIN #Notes n ON n.AccountID = rf.AccountID
        LEFT JOIN #EmailHard h ON h.AccountID = rf.AccountID
        LEFT JOIN #Email m ON m.AccountID = rf.AccountID
 WHERE  ab.CountryID = 236 
        AND CAST(COALESCE(n.NoteHardTerminatedDate, h.EmailHardTerminatedDate,
                     m.EmailHardTerminatedDate) AS DATE) =CAST(rf.HardTerminationDate AS DATE)
        AND ( n.AccountID IS NOT NULL
              OR h.AccountID IS NOT NULL
              OR m.AccountID IS NOT NULL
            );
			
			
			

SELECT * FROM  #ClenUp_hardtermination a 
JOIN RFOperations.RFO_Accounts.AccountRF b ON b.AccountID = a.AccountID


			   
       SELECT   AB.AccountTypeID ,
                T.HardTermDate [BI HardTermDate] ,
                T.SoftTermDate [BI SoftDate] ,
                RF.AccountID ,
                RF.[New Hardteminationdate] AS DerivedDate ,
                RF.[RF Hard Date] ,
                RF.[Note Date] ,
                RF.[Terminated Date] ,
                RF.[Cancel Date]
       FROM     #ClenUp_hardtermination RF
                JOIN RodanFieldsBI.[dbo].[TerminationDates] T ON T.AccountID = RF.AccountID
                JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                                                              AND AB.AccountTypeID <> 2
       WHERE    ISNULL(CAST([New Hardteminationdate] AS DATE), '1900-01-01') <> ISNULL(T.HardTermDate,
                                                              '1900-01-01');
              
			  -- 102,510

		


			  --SELECT * FROM RodanFieldsLive.DBO.NOTES WHERE ACCOUNTID=990711


			  --- NOT HAVING NOTES , TERMINATED EMAILS OR OTHER EMAILS WILL UPDATE HARDTERMINATONDATE =SOFT TERMINATIONDATE

			  
 SELECT rf.AccountID ,
        ab.AccountTypeID ,
        rf.Active ,
        rf.SoftTerminationDate AS [New Hardteminationdate] ,
        rf.HardTerminationDate [RF Hard Date] ,
        rf.ServerModifiedDate ,
        rf.ChangedByApplication
 FROM   RFOperations.RFO_Accounts.AccountRF rf
		JOIN  #ValidAccount v ON v.AccountID = rf.AccountID
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
        LEFT JOIN #Notes n ON n.AccountID = rf.AccountID
        LEFT JOIN #EmailHard h ON h.AccountID = rf.AccountID
        LEFT JOIN #Email m ON m.AccountID = rf.AccountID
 WHERE  ab.CountryID = 236   --AND ab.AccountTypeID<>3 --- Need a comments on ticket to exclude Retails.
        AND rf.SoftTerminationDate <> rf.HardTerminationDate
        AND n.AccountID IS  NULL
        AND h.AccountID IS  NULL
        AND m.AccountID IS  NULL;
           

		   DECLARE @AccountID INT=4845

           SELECT  DISTINCT ab.AccountID
           FROM     RFOperations.RFO_Accounts.AccountBase ab WITH ( NOLOCK )
                    JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
                    JOIN RFOperations.Hybris.Orders o WITH ( NOLOCK ) ON o.AccountID = ab.AccountID
                    JOIN RodanFieldsLive.dbo.Orders ro WITH ( NOLOCK ) ON ro.OrderID = o.OrderID
                    JOIN RodanFieldsLive.dbo.OrderCustomers oc WITH ( NOLOCK ) ON oc.OrderID = ro.OrderID
                                                              AND oc.AccountID = ab.AccountID
           WHERE    ab.CountryID = 236
                    AND ab.AccountID = @AccountID
                    AND ro.OrderStatusID IN ( 4, 5, 9 )
                    AND ro.OrderTypeID NOT IN ( 4, 5, 9 );


           SELECT   *
           FROM     RodanFieldsLive.dbo.notes
           WHERE    accountid = @AccountID;
           SELECT   *
           FROM     RodanFieldsLive.dbo.accountsaudit
           WHERE    accountid = @AccountID; 
           SELECT   *
           FROM     RFOperations.RFO_Accounts.AccountRF
           WHERE    AccountID = @AccountID;




           SELECT   *
           FROM     RFOperations.Cleanup.AccountsRf_Hardtermination a
                    JOIN RFOperations.RFO_Accounts.AccountBase ab ON a.AccountID = ab.AccountID
           WHERE    ab.AccountTypeID = 3;



		--SELECT COUNT(*) FROM RFOperations.Cleanup.AccountsRf_Hardtermination_PC 



       --##############################################
	   --	 COMPARING WITH BI DATABASE RodanFieldsBI
	   --##############################################
	   
              SELECT    AB.AccountTypeID ,
                        T.HardTermDate [BI HardTermDate] ,
                        T.SoftTermDate [BI SoftDate] ,
                        RF.*
              FROM      RFOperations.RFO_Accounts.AccountRF RF
                        JOIN RodanFieldsBI.[dbo].[TerminationDates] T ON T.AccountID = RF.AccountID
                        JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
              WHERE     ISNULL(CAST(RF.HardTerminationDate AS DATE),
                               '1900-01-01') <> ISNULL(T.HardTermDate,
                                                       '1900-01-01');
             
			 -- RF  HAVING HARDTERMINATIN DATE IS NULL BUT NOT BI

              SELECT    AB.AccountTypeID ,
                        T.HardTermDate [BI HardTermDate] ,
                        T.SoftTermDate [BI SoftDate] ,
                        RF.*
              FROM      RFOperations.RFO_Accounts.AccountRF RF
                        JOIN RodanFieldsBI.[dbo].[TerminationDates] T ON T.AccountID = RF.AccountID
                        JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                                                              AND AB.AccountTypeID <> 2
              WHERE     ISNULL(CAST(RF.HardTerminationDate AS DATE),
                               '1900-01-01') <> ISNULL(T.HardTermDate,
                                                       '1900-01-01')
                        AND ISNULL(CAST(RF.HardTerminationDate AS DATE),
                                   '1900-01-01') = '1900-01-01';


	    -- BI  HAVING HARDTERMINATIN DATE IS NULL BUT NOT RF

              SELECT    AB.AccountTypeID ,
                        T.HardTermDate [BI HardTermDate] ,
                        T.SoftTermDate [BI SoftDate] ,
                        RF.*
              FROM      RFOperations.RFO_Accounts.AccountRF RF
                        JOIN RodanFieldsBI.[dbo].[TerminationDates] T ON T.AccountID = RF.AccountID
                        JOIN RFOperations.RFO_Accounts.AccountBase AB ON AB.AccountID = RF.AccountID
                                                              AND AB.AccountTypeID NOT IN (2,3)
              WHERE     ISNULL(CAST(RF.HardTerminationDate AS DATE),
                               '1900-01-01') <> ISNULL(T.HardTermDate,
                                                       '1900-01-01')
                        AND ISNULL(T.HardTermDate, '1900-01-01') = '1900-01-01';

			  
      


	   


			
/* Checking If Accounts with  Hard Terminated Dates but not soft Terminated Date */

SELECT  COUNT(*)--Should not be Any in this case.
FROM    RFOperations.RFO_Accounts.AccountBase ab
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
WHERE   ab.CountryID = 236
        AND rf.HardTerminationDate IS NOT NULL
        AND (rf.SoftTerminationDate IS NULL OR rf.Active=1)
		
		


/* Checking if Any Accounts hardteminatedDate With Account Active*/

SELECT  COUNT(*)--Should NOT be ANY IN this CASE 
FROM    RFOperations.RFO_Accounts.AccountRF rf
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
WHERE   ab.CountryID = 236
        AND HardTerminationDate IS NOT  NULL
        AND Active = 1;

		
/* Checking if Any Accounts SoftTerminationDate With Account Active*/

SELECT  --COUNT(*) -- Should not be any after Clean up.
        rf.SoftTerminationDate ,
        ab.AccountTypeID ,
		rf.Active,
        DATEADD(s, -1, DATEADD(mm, DATEDIFF(m, 0, SoftTerminationDate) + 2, 0)) AS NewHardTerminationDate ,
        rf.HardTerminationDate
FROM    RFOperations.RFO_Accounts.AccountRF rf
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
WHERE   rf.SoftTerminationDate IS NOT  NULL
        AND Active = 1 AND ab.AccountTypeID=1
        AND rf.SoftTerminationDate <  DATEADD(mm, DATEDIFF(m, 0, GETDATE()) - 1,
                                             0);
-- SoftTerminated lastMonth.

SELECT  --COUNT(*) -- Should not be any after Clean up.
       rf.AccountID,
	    rf.SoftTerminationDate ,
        ab.AccountTypeID ,
		rf.Active,
        DATEADD(s, -1, DATEADD(mm, DATEDIFF(m, 0, SoftTerminationDate) + 3, 0)) AS NewHardTerminationDate ,
        rf.HardTerminationDate,
		rf.ServerModifiedDate
FROM    RFOperations.RFO_Accounts.AccountRF rf
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
WHERE   rf.SoftTerminationDate IS NOT  NULL
        AND Active = 1 
		AND ab.CountryID=236
        AND rf.SoftTerminationDate < DATEADD(mm, DATEDIFF(m, 0, GETDATE()) - 2,
                                             0);


--SELECT DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()) - 2,0)
		
		/* Checking if Softteminated Inactive Accounts not having hard Terminated Date */

SELECT  --COUNT(*)
        rf.SoftTerminationDate ,
        ab.AccountTypeID ,
        ab.AccountID ,
        DATEADD(s, -1, DATEADD(mm, DATEDIFF(m, 0, SoftTerminationDate) + 2, 0)) AS NewHardTerminationDate ,
        rf.HardTerminationDate
FROM    RFOperations.RFO_Accounts.AccountRF rf
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
        JOIN RodanFieldsLive.dbo.Notes n ON n.AccountID = ab.AccountID
        JOIN RodanFieldsLive.dbo.AccountCancellation c ON c.AccountId = ab.AccountID
		JOIN ( SELECT DISTINCT ac.AccountID
                     FROM   RodanFieldsLive.dbo.Orders o
                            JOIN RodanFieldsLive.dbo.OrderCustomers oc ON o.OrderID = oc.OrderID
                            JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = oc.AccountID
                     WHERE  o.OrderTypeID NOT IN ( 4, 5, 9 )
                            AND o.OrderStatusID IN ( 4, 5, 9  )
							AND ac.Active=0 ) rl ON rl.AccountID = ab.AccountID
WHERE   ab.CountryID = 236
        AND ab.AccountTypeID = 2 
        AND rf.SoftTerminationDate IS NOT  NULL
        AND Active = 0
        AND rf.HardTerminationDate IS NULL
        AND rf.SoftTerminationDate < DATEADD(mm, DATEDIFF(m, 0, GETDATE()) - 2,
                                             0);
-- SoftTerminated lastMonth.

SELECT COUNT(*) FROM RodanFieldsLive.dbo.accounts 
INNER JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = Accounts.AccountID
INNER JOIN RodanFieldsLive.dbo.Orders o ON o.orderid = oc.OrderID
INNER JOIN RFOperations.rfo_accounts.accountrf rf ON rf.accountid = oc.accountid
WHERE accounts.active = 0 AND
 AccountTypeID = 2 AND 
o.OrderStatusID IN (4,5,9) AND
 O.OrderTypeID NOT IN (4,5,9)
AND o.CompleteDate < '2016-05-01 '
AND Accounts.AccountID IN (1874860 ) 


-- 1913644 RECORDS IN STG2.



	   
	   ---##############################################################################################



/*
STEPS FOLLOWED FOR HARDTERMINATION CLEANUP

1) CONSIDERING THE ACCOUNTS FOR CLEANUP  WHICH HAS SUBMITTED ORDER AND A HARDTERMINATIONDATE IN RFO.
2) DETERMINING THE HARDTERMINATIONDATE FIRST BY CHECKING FOR '%HARD TERMINATING%' KEY WORD IN RFL NOTES TABLE .
3) DETERMINING THE HARDTERMINATIONDATE SECONDLY  BY CHECKING IN ACCOUNTSAUDIT TABLE IN RFL  WITH EMAIL CONCATINATED WITH ''%TERMINAT%'.
4) ACCOUNTS WHICH ARE NOT IN ABOVE 2 AND 3,THEN  HARDTERMINATIONDATE IS BASED ON SOFTTERMINATIONDATE.

--**/




IF OBJECT_ID(N'tempdb..#ValidPCAccounts') IS NOT NULL
DROP TABLE #ValidPCAccounts

SELECT DISTINCT
        ac.AccountID --INTO #ValidPCAccounts
FROM    RodanFieldsLive.dbo.Accounts ac
        INNER JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.AccountID = ac.AccountID
        INNER JOIN RodanFieldsLive.dbo.Orders o ON o.OrderID = oc.OrderID
        INNER JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ac.AccountID
WHERE   ac.Active = 0 
        AND AccountTypeID = 2
        AND o.OrderStatusID IN ( 4, 5, 9 )
        AND o.OrderTypeID NOT IN ( 4, 5, 9 )
        AND o.CompleteDate < '2016-05-01 ';

		CREATE CLUSTERED INDEX cls ON #ValidPCAccounts(AccountID)
		--SELECT COUNT(*) [Total Counts] FROM #ValidPCAccounts -- Records counts 630560 in STG2.


IF OBJECT_ID(N'tempdb..#HardTerminationDate') IS NOT NULL
DROP TABLE #HardTerminationDate
    
SELECT  DISTINCT
        a.AccountID ,
        MIN(n.DateCreated) AS 'MinHardTerminationDate',CAST('NOTES' AS NVARCHAR(50))AS [SOURCE]
INTO    #HardTerminationDate
FROM    #ValidPCAccounts a 
        JOIN RodanFieldsLive.dbo.Notes n WITH ( NOLOCK ) ON a.AccountID = n.AccountID       
WHERE    n.NoteText LIKE '%hard terminating%'
GROUP BY a.AccountID;  -- RECORDS 623542 IN STG2.


INSERT  INTO #HardTerminationDate
        SELECT DISTINCT
                a.AccountID ,
                MIN(aa.AuditDate) AS 'MinHardTerminationDate' ,'Termninated Email' AS [SOURCE]
        FROM    #ValidPCAccounts a WITH ( NOLOCK )
                JOIN RodanFieldsLive.dbo.AccountsAudit aa WITH ( NOLOCK ) ON aa.AccountID = a.AccountID                
                LEFT JOIN #HardTerminationDate HD WITH ( NOLOCK ) ON HD.AccountID = aa.AccountID
                JOIN ( SELECT DISTINCT
                              aa.AccountID ,
                              MAX(aa.AuditDate) AS MaxAuditdate
                       FROM   RodanFieldsLive.dbo.AccountsAudit aa WITH ( NOLOCK )
                       WHERE  aa.Active = 1
                       GROUP BY aa.AccountID
                     ) mad ON mad.AccountID = a.AccountID
        WHERE    aa.EmailAddress LIKE '%terminated%'               
                --AND arf.HardTerminationDate IS NULL
                AND HD.AccountID IS NULL
                AND mad.MaxAuditdate < aa.AuditDate
        GROUP BY a.AccountID;
		-- INSERTED 30 RECORDS IN STG2.


INSERT  INTO #HardTerminationDate
        SELECT DISTINCT
                a.AccountID ,
                MIN(aa.AuditDate) AS 'MinHardTerminationDate','Others Email' AS [SOURCE]
        FROM    #ValidPCAccounts a
                JOIN RodanFieldsLive.dbo.AccountsAudit aa WITH ( NOLOCK ) ON aa.AccountID = a.AccountID               
                LEFT JOIN #HardTerminationDate HD ON HD.AccountID = aa.AccountID               
                JOIN ( SELECT DISTINCT
                              aa.AccountID ,
                              MAX(aa.AuditDate) AS MaxAuditdate
                       FROM   RodanFieldsLive.dbo.AccountsAudit aa WITH ( NOLOCK )
                       WHERE  aa.Active = 1
                       GROUP BY aa.AccountID
                     ) mad ON mad.AccountID = a.AccountID
        WHERE ( aa.EmailAddress LIKE '%cancel%'
                      OR aa.EmailAddress LIKE '%optout%'
                      OR aa.EmailAddress LIKE '%opout%'
                      OR aa.EmailAddress LIKE '%inactive%'
                      OR aa.EmailAddress LIKE '%inative%'
                      OR aa.EmailAddress LIKE '%invalid%'
                    )
                AND HD.AccountID IS NULL
                --AND arf.HardTerminationDate IS NULL
                AND mad.MaxAuditdate < aa.AuditDate              
                AND aa.Active = 0
        GROUP BY a.AccountID; 
		-- RECORDS 48  IN STG2.




SELECT COUNT(*) [Total Counts]
FROM    #HardTerminationDate     
--623115


       SELECT   RF.HardTerminationDate ,
                RF.SoftTerminationDate ,
				RF.Active,
                ab.AccountTypeID ,
                h.*,rf.ChangedByUser
       FROM     RFOperations.RFO_Accounts.AccountBase ab 
                JOIN RFOperations.RFO_Accounts.AccountRF RF  ON RF.AccountID = ab.AccountID
				--JOIN RodanFieldsBI.dbo.TerminationDates t ON t.AccountID = ab.AccountID
                JOIN #HardTerminationDate h ON h.AccountID = ab.AccountID                                              
       WHERE    CAST(ISNULL(h.MinHardTerminationDate, '1900-01-01')AS DATE) = CAST(ISNULL(RF.HardTerminationDate,
                                                              '1900-01-01')AS DATE);





SELECT  --COUNT(*)
      DISTINCT
        rf.SoftTerminationDate ,
        rf.AccountID ,
		ac.MaxStart,
        rf.Active ,
        ab.AccountTypeID ,
        rf.HardTerminationDate
FROM    RFOperations.RFO_Accounts.AccountRF rf 
--JOIN #HardTerminationDate h ON h.AccountID = rf.AccountID
        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
        JOIN RodanFieldsLive.dbo.Notes n ON n.AccountID = ab.AccountID
        JOIN RodanFieldsLive.dbo.AccountCancellation c ON c.AccountId = ab.AccountID
        JOIN ( SELECT
                        ac.AccountID, MAX(o.StartDate) MaxStart
               FROM     RodanFieldsLive.dbo.Orders o
                        JOIN RodanFieldsLive.dbo.OrderCustomers oc ON oc.OrderID = o.OrderID
                        JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = oc.AccountID
               WHERE    o.OrderTypeID NOT IN ( 4, 5, 9 )
                        AND o.OrderStatusID IN ( 4, 5, 9 )
                        AND ac.Active = 0
						GROUP BY ac.AccountID
             ) ac ON ac.AccountID = rf.AccountID
WHERE   ab.CountryID = 236
        AND ab.AccountTypeID = 2
        AND rf.SoftTerminationDate IS NOT  NULL
        AND Active = 0
        AND rf.HardTerminationDate IS NULL
        AND rf.SoftTerminationDate < '2016-04-01'
		AND ac.MaxStart<DATEADD(DAY,-75,GETDATE())


   



	   ---##############################################################################################



;
WITH    ValidAccount
          AS ( SELECT  DISTINCT
                        ab.AccountID
               FROM     RFOperations.RFO_Accounts.AccountBase ab WITH ( NOLOCK )
                        JOIN RFOperations.RFO_Accounts.AccountRF rf ON rf.AccountID = ab.AccountID
                        JOIN RFOperations.Hybris.Orders o WITH ( NOLOCK ) ON o.AccountID = ab.AccountID
                        JOIN RodanFieldsLive.dbo.Orders ro WITH ( NOLOCK ) ON ro.OrderID = o.OrderID
                        JOIN RodanFieldsLive.dbo.OrderCustomers oc WITH ( NOLOCK ) ON oc.OrderID = ro.OrderID
                                                              AND oc.AccountID = ab.AccountID
               WHERE    ab.CountryID = 236
                        AND ro.OrderStatusID IN ( 4, 5, 9 )
                        AND ro.OrderTypeID NOT IN ( 4, 5, 9 )
						--AND rf.HardTerminationDate IS NOT NULL                         
             ),
   -- SELECT  COUNT(*) FROM    ValidAccount
        Notes
          AS ( SELECT   ac.AccountID ,
                        MIN(n.DateCreated) AS NoteHardTerminatedDate
               FROM     ValidAccount v
                        JOIN RodanFieldsLive.dbo.AccountNotes ac WITH ( NOLOCK ) ON ac.AccountID = v.AccountID
                        JOIN RodanFieldsLive.dbo.Notes n WITH ( NOLOCK ) ON n.AccountID = ac.AccountID
               WHERE    n.NoteText LIKE '%hard terminating%'
               GROUP BY ac.AccountID
             ),
   -- SELECT  COUNT(*) FROM    Notes
        EmailHard
          AS ( SELECT   a.AccountID ,
                        MIN(aa.AuditDate) EmailHardTerminatedDate
               FROM     RodanFieldsLive.dbo.Accounts a
                        JOIN RodanFieldsLive.dbo.AccountsAudit aa ON aa.AccountID = a.AccountID
                        JOIN ValidAccount v ON v.AccountID = a.AccountID
                        JOIN ( SELECT DISTINCT
                                        aa.AccountID ,
                                        MAX(aa.AuditDate) AS MaxAuditdate
                               FROM     RodanFieldsLive.dbo.AccountsAudit aa
                                        WITH ( NOLOCK )
                               WHERE    aa.Active = 1
                               GROUP BY aa.AccountID
                             ) had ON had.AccountID = a.AccountID
               WHERE    NOT EXISTS ( SELECT 1
                                     FROM   Notes n
                                     WHERE  n.AccountID = a.AccountID )-- Excluding records for having notes.
                        AND aa.EmailAddress LIKE '%terminat%'
                        AND aa.Active = 0
                        AND aa.AuditDate > had.MaxAuditdate
               GROUP BY a.AccountID
             ),
        Email
          AS ( SELECT   a.AccountID ,
                        MIN(aa.AuditDate) EmailHardTerminatedDate
               FROM     RodanFieldsLive.dbo.Accounts a
                        JOIN RodanFieldsLive.dbo.AccountsAudit aa ON aa.AccountID = a.AccountID
                        JOIN ValidAccount v ON v.AccountID = a.AccountID
                        JOIN ( SELECT DISTINCT
                                        aa.AccountID ,
                                        MAX(aa.AuditDate) AS MaxAuditdate
                               FROM     RodanFieldsLive.dbo.AccountsAudit aa
                                        WITH ( NOLOCK )
                               WHERE    aa.Active = 1
                               GROUP BY aa.AccountID
                             ) mad ON mad.AccountID = a.AccountID
               WHERE    NOT EXISTS ( SELECT 1
                                     FROM   Notes n
                                     WHERE  n.AccountID = a.AccountID )-- Excluding records for having notes.
                        AND NOT EXISTS ( SELECT 1
                                         FROM   EmailHard h
                                         WHERE  h.AccountID = a.AccountID )
                        AND ( aa.EmailAddress LIKE '%cancel%'
                              OR aa.EmailAddress LIKE '%optout%'
                              OR aa.EmailAddress LIKE '%opout%'
                              OR aa.EmailAddress LIKE '%inactive%'
                              OR aa.EmailAddress LIKE '%inative%'
                              OR aa.EmailAddress LIKE '%invalid%'
                            )
                        AND aa.AuditDate > mad.MaxAuditdate
               GROUP BY a.AccountID
             )
    SELECT  rf.AccountID ,
            COALESCE(n.NoteHardTerminatedDate, h.EmailHardTerminatedDate,
                     m.EmailHardTerminatedDate) AS [New Hardteminationdate] ,
            rf.HardTerminationDate [RF Hard Date] ,
            n.NoteHardTerminatedDate [Note Date] ,
            h.EmailHardTerminatedDate [Terminated Date] ,
            m.EmailHardTerminatedDate [Cancel Date]
   --INTO    #ClenUp_hardtermination -- Clean Up Back Up table.
    FROM    RFOperations.RFO_Accounts.AccountRF rf
            JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID
            LEFT JOIN Notes n ON n.AccountID = rf.AccountID
            LEFT JOIN EmailHard h ON h.AccountID = n.AccountID
            LEFT JOIN Email m ON m.AccountID = n.AccountID
    WHERE   ab.CountryID = 236
            AND DATEDIFF(dd,
                         COALESCE(n.NoteHardTerminatedDate,
                                  h.EmailHardTerminatedDate,
                                  m.EmailHardTerminatedDate),
                         rf.HardTerminationDate) <> 0
            AND ( n.AccountID IS NOT NULL
                  OR h.AccountID IS NOT NULL
                  OR m.AccountID IS NOT NULL
                );
            
			
			