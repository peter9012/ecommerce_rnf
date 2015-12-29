
/* Autoship Clean Up Validation Scripts */

--  8.Multiple Active Autoships_Cleanup

/* Checking Templates made Active for Inactive*/
SELECT  COUNT(*) -- Should be NULL 
FROM    RFOperations.Hybris.Autoship a
WHERE   CAST(a.ServerModifiedDate AS DATE) = CAST(GETDATE() AS DATE)
        AND a.Active = 1; 


		/* Checking if Any Accounts have Multiple Active Templates type */

SELECT  a.AccountID ,
        a.AutoshipTypeID AS AS_Type ,
        COUNT(*) AS Counts
FROM    RFOperations.Hybris.Autoship a
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
WHERE   a.CountryID = 236
        AND ab.AccountTypeID = 2
        AND a.AutoshipTypeID = 1
        AND a.Active = 1
GROUP BY a.AccountID ,
        a.AutoshipTypeID
HAVING  COUNT(*) > 1
UNION ALL
SELECT  a.AccountID ,
        a.AutoshipTypeID AS_Type ,
        COUNT(*) AS Counts
FROM    RFOperations.Hybris.Autoship a
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
WHERE   a.CountryID = 236
        AND ab.AccountTypeID = 1
        AND a.AutoshipTypeID = 2
        AND a.Active = 1
GROUP BY a.AccountID ,
        a.AutoshipTypeID
HAVING  COUNT(*) > 1
UNION ALL
SELECT  a.AccountID ,
        a.AutoshipTypeID AS_Type ,
        COUNT(*) AS Counts
FROM    RFOperations.Hybris.Autoship a
        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
WHERE   a.CountryID = 236
        AND ab.AccountTypeID = 1
        AND a.AutoshipTypeID = 3
        AND a.Active = 1
GROUP BY a.AccountID ,
        a.AutoshipTypeID
HAVING  COUNT(*) > 1;
		


		/* Checking if updated Acive those templates having Recent Orders. */



WITH    Autoship
          AS ( SELECT   a.AutoshipID ,
                        a.AccountID ,
                        a.AutoshipTypeID
               FROM     RFOperations.Hybris.Autoship a
               WHERE    CAST(a.ServerModifiedDate AS DATE) = CAST(GETDATE() AS DATE)
                        AND a.Active = 0
             ),
        MAXAutoDate
          AS ( SELECT   a.AccountID ,
                        a.AutoshipID ,
                        MAX(o.CompletionDate) AS MaxOrderCompletion
               FROM     RFOperations.Hybris.Autoship a
                        JOIN Autoship b ON b.AccountID = a.AccountID
                                           AND a.AutoshipTypeID = b.AutoshipTypeID
                        LEFT JOIN RFOperations.Hybris.Orders o ON o.AutoShipID = a.AutoshipID
               GROUP BY a.AccountID ,
                        a.AutoshipID
             )
    SELECT  a.AutoshipID ,
            a.AccountID ,
            a.AutoshipTypeID ,
            a.Active ,
            b.MaxOrderCompletion ,
            ROW_NUMBER() OVER ( PARTITION BY a.AccountID ORDER BY b.MaxOrderCompletion DESC ) AS rown
    FROM    RFOperations.Hybris.Autoship a
            JOIN MAXAutoDate b ON a.AutoshipID = b.AutoshipID
    WHERE   a.Active <> 1
            AND ROW_NUMBER() OVER ( PARTITION BY a.AccountID ORDER BY b.MaxOrderCompletion DESC ) = 1
    UNION ALL
    SELECT  a.AutoshipID ,
            a.AccountID ,
            a.AutoshipTypeID ,
            a.Active ,
            b.MaxOrderCompletion ,
            ROW_NUMBER() OVER ( PARTITION BY a.AccountID ORDER BY b.MaxOrderCompletion DESC ) AS rown
    FROM    RFOperations.Hybris.Autoship a
            JOIN MAXAutoDate b ON a.AutoshipID = b.AutoshipID
    WHERE   a.Active = 1
            AND ROW_NUMBER() OVER ( PARTITION BY a.AccountID ORDER BY b.MaxOrderCompletion DESC ) > 1;



		
		
		
		
			------5.Autoship Clean EndDate ------



SELECT  COUNT(*)--shoul be NULL after Clean UP
FROM    RFOperations.Hybris.Autoship
WHERE   Active = 1
        AND CountryID = 236
        AND EndDate IS NOT NULL; 


SELECT  COUNT(*)-- Should be NULL after Clean UP
FROM    RFOperations.Hybris.Autoship a
        JOIN RodanFieldsLive.dbo.Orders b ON a.AutoshipID = b.OrderID
        JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = a.AccountID
WHERE   ac.Active = 1
        AND b.OrderStatusID = 4
        AND a.Active=1
        AND a.CountryID = 236;



SELECT  COUNT(*)-- Should be NULL after Clean UP
FROM    RFOperations.Hybris.Autoship a
        JOIN RodanFieldsLive.dbo.Orders b ON a.AutoshipID = b.OrderID
        JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = a.AccountID
WHERE   ac.Active = 1
        AND b.OrderStatusID = 4
        AND a.EndDate IS NOT NULL
        AND a.CountryID = 236;












