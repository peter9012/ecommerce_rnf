------Autoship  EndDate ------



SELECT  COUNT(*)--shoul be NULL after Clean UP
FROM    RFOperations.Hybris.Autoship
WHERE   Active = 1
        AND CountryID = 236
        AND EndDate IS NOT NULL; 


SELECT  COUNT(*)-- Should be NULL after Clean UP
FROM    RFOperations.Hybris.Autoship a
        JOIN RodanFieldsLive.dbo.Orders b ON a.AutoshipID = b.OrderID
        JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = a.AccountID
WHERE   ac.Active <> 1
        AND b.OrderStatusID = 4 --Submitted
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

------ RFO Autoships END Date Cleanup Script ------

USE RFOperations
GO 


GO


DECLARE @Getdate DATETIME2 = GETDATE();
SELECT  a.AutoshipID ,
        a.Active ,
        1 AS New_ActiveFlag ,
        a.ServerModifiedDate ,
        @Getdate AS newServerModifiedDate
INTO    Cleanup_autoship_enddate_1
FROM    RFOperations.Hybris.Autoship a
        JOIN RodanFieldsLive.dbo.Orders b ON a.AutoshipID = b.OrderID
        JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = a.AccountID
WHERE   ac.Active = 1
        AND b.OrderStatusID = 4
        AND a.CountryID = 236

BEGIN TRAN 

DECLARE @Getdate DATETIME2 = GETDATE();
UPDATE  a
SET     Active = 1 
        --a.ServerModifiedDate = @Getdate
FROM    RFOperations.Hybris.Autoship a
        JOIN RodanFieldsLive.dbo.Orders b ON a.AutoshipID = b.OrderID
        JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = a.AccountID
WHERE   ac.Active = 1
        AND b.OrderStatusID = 4
        AND a.CountryID = 236


DECLARE @Getdate DATETIME2 = GETDATE();
UPDATE  RFOperations.Hybris.Autoship
SET     EndDate = NULL 
        --ServerModifiedDate = @Getdate
WHERE   Active = 1
        AND CountryID = 236



-- COMMIT 



--- ETL Modificatons : StoredProcedure [ETL].[uspStageAutoships] 




/*BOOMI-501 Next run date*/
		 /*GP-2077 Updating enddate to null for active autoship*/

            UPDATE  a
            SET     a.EndDate = CASE WHEN a.Active = 1 THEN NULL
                                     WHEN a.Active <> 1
                                          AND a.NextRundate IS NULL
                                     THEN au.EndDate
                                     ELSE a.NextRundate
                                END
            FROM    ETL.StagingAutoship a
                    LEFT OUTER JOIN Hybris.Autoship au ON a.AutoshipID = au.AutoshipID