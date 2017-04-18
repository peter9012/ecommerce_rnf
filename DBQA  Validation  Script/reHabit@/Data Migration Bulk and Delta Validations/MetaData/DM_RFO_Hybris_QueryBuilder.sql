USE DM_QA
GO 

DECLARE @sourceColumn NVARCHAR(30) ,
    @TargetColumn NVARCHAR(30) ,
    @flag NVARCHAR(10) ,
    @Owner NVARCHAR(50) ,
    @key NVARCHAR(50) ,
    @int INT ,
    @Max INT
			
			

SELECT  @Max = MAX(MapID)
FROM    dbqa.Map_tab
			
          

SET @int = 1
WHILE @Max >= @int
    BEGIN
        SELECT  @sourceColumn = SourceColumn ,
                @TargetColumn = TargetColumn ,
                @Owner = [Owner] ,
                @flag = [flag] ,
                @Key = [Key]
        FROM    dbqa.Map_tab
        WHERE   MapID = @int	
				
        UPDATE  dbqa.Map_tab
        SET     [SQL Stmt] = CASE WHEN sourceDatatypes LIKE ( 'nvarchar%' )
                                  THEN 'SELECT  b.HybrisKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #RFO a 
			JOIN #Hybris b ON a.RFOKey=b.HybrisKey 
			WHERE ISNULL(a.' + @sourceColumn + ','' '' ) <> ISNULL(b.'
                                       + @TargetColumn + ','' '' ) '
                                  WHEN sourceDatatypes LIKE 'date%'
                                  THEN 'SELECT  b.HybrisKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #RFO a 
			JOIN #Hybris b ON a.RFOKey=b.HybrisKey 
			WHERE CAST(ISNULL(a.' + @sourceColumn
                                       + ',''2018-01-01'' )AS DATE) <> CAST(ISNULL(b.'
                                       + @TargetColumn
                                       + ',''2018-01-01 '' )AS DATE) '
                                  WHEN sourceDatatypes IN ( 'money', 'int',
                                                            'bigint' )
                                  THEN 'SELECT  b.HybrisKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #RFO a 
			JOIN #Hybris b ON a.RFOKey=b.HybrisKey 
			WHERE ISNULL(a.' + @sourceColumn + ',0 ) <> ISNULL(b.'
                                       + @TargetColumn + ',0 ) '
                                  ELSE 'SELECT  b.HybrisKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #RFO a 
			JOIN #Hybris b ON a.RFOKey=b.HybrisKey 
			WHERE a.' + @sourceColumn + ' <> b.' + @TargetColumn + ' '
                             END
        WHERE   MapID = @int
                AND flag IN ('c2c','ref','upd')
                AND [Owner] IN ( 'Accounts', 'Autoship', 'Orders',
                                 'ReturnOrder', 'Products' )
			
        SET @int = @int + 1
    END
	GO

				--SELECT * FROM dbqa.Map_tab WHERE [Owner] IN ( 'Accounts','Autoship','Orders','ReturnOrder')




--- VALIDATION QUERIES FOR CHILD ENTITIES.


DECLARE @sourceColumn NVARCHAR(30) ,
    @TargetColumn NVARCHAR(30) ,
    @flag NVARCHAR(10) ,
    @int INT ,
    @Max INT
			
			--SELECT ROW_NUMBER() OVER ( ORDER BY MapID) rn ,SourceColumn,TargetColumn,flag INTO #temp FROM #map_tab WHERE flag NOT IN  ('Key','default')
SELECT  @Max = MAX(MapID)
FROM    dbqa.Map_tab
			


SET @int = 1
WHILE @Max >= @int
    BEGIN
        SELECT  @sourceColumn = SourceColumn ,
                @TargetColumn = TargetColumn
        FROM    dbqa.Map_tab
        WHERE   MapID = @int	
				
        UPDATE  dbqa.Map_tab
        SET     [SQL Stmt] = CASE WHEN sourceDatatypes LIKE 'date%'
                                  THEN ' SELECT COALESCE(A.RFOKey,B.HybrisKey) AS [Key] ,A.'
                                       + @sourceColumn + ' ,B.'
                                       + @TargetColumn + ' FROM (
		  SELECT RFOKey,CAST(' + @SourceColumn + ' AS DATE) AS '
                                       + @sourceColumn + '  FROM #RFO 
		  EXCEPT 
		  SELECT HybrisKey,CAST(' + @TargetColumn + ' AS DATE) AS '
                                       + @TargetColumn + '  FROM #Hybris) A
		   LEFT JOIN 
		 ( SELECT HybrisKey,CAST(' + @TargetColumn + ' AS DATE) AS '
                                       + @TargetColumn + '  FROM #Hybris
		  EXCEPT 
		  SELECT RFOKey,CAST(' + @sourceColumn + ' AS DATE) AS '
                                       + @sourceColumn
                                       + '  FROM #RFO)B on  A.RFOKey=B.HybrisKey

		  UNION 
		   SELECT COALESCE(A.RFOKey,B.HybrisKey) AS [Key],A.' + @sourceColumn
                                       + ',B.' + @TargetColumn + '    FROM 
		 ( SELECT RFOKey,CAST(' + @sourceColumn + ' AS DATE) AS '
                                       + @sourceColumn + '  FROM #RFO
		  EXCEPT 
		  SELECT HybrisKey,CAST(' + @TargetColumn + ' AS DATE) AS '
                                       + @TargetColumn + '  FROM #Hybris)A
		  RIGHT JOIN 
		  (
		  SELECT HybrisKey,CAST(' + @TargetColumn + ' AS DATE) AS '
                                       + @TargetColumn + '  FROM #Hybris
		  EXCEPT 
		  SELECT RFOKey,CAST(' + @sourceColumn + ' AS DATE) AS '
                                       + @sourceColumn + '  FROM #RFO) 
		  B on  A.RFOKey=B.HybrisKey  '
                                  ELSE ' SELECT COALESCE(A.RFOKey,B.HybrisKey) AS [Key] ,A.'
                                       + @sourceColumn + ' ,B.'
                                       + @TargetColumn + ' FROM (
		  SELECT RFOKey,' + @SourceColumn + ' FROM #RFO 
		  EXCEPT 
		  SELECT HybrisKey,' + @TargetColumn + ' FROM #Hybris) A
		   LEFT JOIN 
		 ( SELECT HybrisKey,' + @TargetColumn + ' FROM #Hybris
		  EXCEPT 
		  SELECT RFOKey,' + @sourceColumn
                                       + ' FROM #RFO)B on  A.RFOKey=B.HybrisKey

		  UNION ALL 
		   SELECT COALESCE(A.RFOKey,B.HybrisKey) AS [Key],A.' + @sourceColumn
                                       + ',B.' + @TargetColumn + '    FROM 
		 ( SELECT RFOKey,' + @sourceColumn + ' FROM #RFO
		  EXCEPT 
		  SELECT HybrisKey,' + @TargetColumn + ' FROM #Hybris)A
		  RIGHT JOIN 
		  (
		  SELECT HybrisKey,' + @TargetColumn + ' FROM #Hybris
		  EXCEPT 
		  SELECT RFOKey,' + @sourceColumn + ' FROM #RFO) 
		  B on  A.RFOKey=B.HybrisKey  '
                             END
        WHERE   MapID = @int
                AND [owner] IN ( 'AccountPaymentProfiles', 'AccountAddresses',
                                 'AutoshipItem', 'AutoshipPayment',
                                 'AutoshipPaymentAddress',
                                 'AutoshipShippingAddress',
                                 'OrderConsignments', 'OrderItems',
                                 'OrderPayment', 'OrderPaymentAddress',
                                 'OrderPaymentTransaction',
                                 'OrderShippingAddress', 'ReturnItem',
                                 'ReturnPayment', 'ReturnPaymentAddress',
                                 'ReturnPaymentTransactions' )
                AND flag IN ('c2c','ref','upd')
        SET @int = @int + 1
    END








	USE DM_QA
GO 

DECLARE @sourceColumn NVARCHAR(30) ,
    @TargetColumn NVARCHAR(30) ,
    @flag NVARCHAR(10) ,
    @Owner NVARCHAR(50) ,
    @key NVARCHAR(50) ,
    @int INT ,
    @Max INT
			
			

SELECT  @Max = MAX(MapID)
FROM    dbqa.Map_tab
			
          

SET @int = 1
WHILE @Max >= @int
    BEGIN
        SELECT  @sourceColumn = SourceColumn ,
                @TargetColumn = TargetColumn ,
                @Owner = [Owner] ,
                @flag = [flag] ,
                @Key = [Key]
        FROM    dbqa.Map_tab
        WHERE   MapID = @int	
				
        UPDATE  dbqa.Map_tab
        SET     [SQL Stmt] = CASE WHEN sourceDatatypes LIKE ( 'nvarchar%' )
                                  THEN 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE ISNULL(a.' + @sourceColumn + ','' '' ) <> ISNULL(b.'
                                       + @TargetColumn + ','' '' ) '
                                  WHEN sourceDatatypes LIKE 'date%'
                                  THEN 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE CAST(ISNULL(a.' + @sourceColumn
                                       + ',''2018-01-01'' )AS DATE) <> CAST(ISNULL(b.'
                                       + @TargetColumn
                                       + ',''2018-01-01 '' )AS DATE) '
                                  WHEN sourceDatatypes IN ( 'money', 'int',
                                                            'bigint' )
                                  THEN 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE ISNULL(a.' + @sourceColumn + ',0 ) <> ISNULL(b.'
                                       + @TargetColumn + ',0 ) '
                                  ELSE 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE a.' + @sourceColumn + ' <> b.' + @TargetColumn + ' '
                             END
        WHERE   MapID = @int
                AND flag IN ('c2c','ref','upd')
                AND [Owner] IN ( 'OKTA' )
			
        SET @int = @int + 1
    END
	GO







	USE DM_QA
GO 

DECLARE @sourceColumn NVARCHAR(30) ,
    @TargetColumn NVARCHAR(30) ,
    @flag NVARCHAR(10) ,
    @Owner NVARCHAR(50) ,
    @key NVARCHAR(50) ,
    @int INT ,
    @Max INT
			
			

SELECT  @Max = MAX(MapID)
FROM    dbqa.Map_tab
			
          

SET @int = 1
WHILE @Max >= @int
    BEGIN
        SELECT  @sourceColumn = SourceColumn ,
                @TargetColumn = TargetColumn ,
                @Owner = [Owner] ,
                @flag = [flag] ,
                @Key = [Key]
        FROM    dbqa.Map_tab
        WHERE   MapID = @int	
				
        UPDATE  dbqa.Map_tab
        SET     [SQL Stmt] = CASE WHEN sourceDatatypes LIKE ( 'nvarchar%' )
                                  THEN 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE ISNULL(a.' + @sourceColumn + ','' '' ) <> ISNULL(b.'
                                       + @TargetColumn + ','' '' ) '
                                  WHEN sourceDatatypes LIKE 'date%'
                                  THEN 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE CAST(ISNULL(a.' + @sourceColumn
                                       + ',''2018-01-01'' )AS DATE) <> CAST(ISNULL(b.'
                                       + @TargetColumn
                                       + ',''2018-01-01 '' )AS DATE) '
                                  WHEN sourceDatatypes IN ( 'money', 'int',
                                                            'bigint' )
                                  THEN 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE ISNULL(a.' + @sourceColumn + ',0 ) <> ISNULL(b.'
                                       + @TargetColumn + ',0 ) '
                                  ELSE 'SELECT  b.TargetKey, ' + @sourceColumn
                                       + ', ' + @TargetColumn + ' from #Source a 
			JOIN #Target b ON a.SourceKey=b.TargetKey 
			WHERE a.' + @sourceColumn + ' <> b.' + @TargetColumn + ' '
                             END
        WHERE   MapID = @int
                AND flag IN ('c2c')
                AND [Owner] IN ( 'PWS_US', 'PWS_CA')
			
        SET @int = @int + 1
    END
	GO





				

--SELECT * FROM dbqa.Map_tab Where flag='def'


		 
--		SELECT * FROM dbqa.ErrorLog

--DROP TABLE #RFO 
--DROP TABLE #Hybris

--CREATE TABLE #RFO
--(AccountID INT IDENTITY(1,1)
--,FirstName NVARCHAR(20),LastName NVARCHAR(20)
--)

--CREATE TABLE #Hybris
--(PK BIGINT IDENTITY(1,1)
--,P_FirstName NVARCHAR(20),p_LastName NVARCHAR(20)
--)
  
--INSERT INTO #RFO
--        (  FirstName,LastName )
--VALUES  ( 
--          N'Nand'  -- FirstName - nvarchar(20)
--		  ,'Khadka'
--          ),
--		  (N'Rajendra','Khadka')
--		  ,(N'Sharad',NULL)


		  
--INSERT INTO #Hybris
--        ( P_FirstName,p_LastName )
--VALUES  ( 
--          N'Nand'  -- FirstName - nvarchar(20)
--		   ,'Khadka'
--          ),
--		  (N'Shishir' ,'Khadka')
--		  ,(N'Shishir' ,'Khadka')
--		  ,(N'Mana',NULL)

--DELETE  #Hybris 
--WHERE PK=3


--SELECT * FROM #RFO  --- Load the Data Set with Indexing key.
--SELECT * FROM #Hybris--- LoadThe DataSet with Indexing Key.

--SELECT * FROM #Hybris a
--JOIN #RFO b ON a.PK=b.AccountID
--WHERE a.P_FirstName<>b.FirstName


--SELECT * FROM #Hybris a
--JOIN #RFO b ON a.PK=b.AccountID
--WHERE a.p_LastName<>b.LastName



--DELETE  #Hybris
--WHERE PK=3

--UPDATE  #Hybris 
--SET p_LastName=NULL
--WHERE PK=1

--SELECT * FROM #RFO  --- Load the Data Set with Indexing key.
--SELECT * FROM #Hybris--- LoadThe DataSet with Indexing Key.

--SELECT * FROM #Hybris a
--JOIN #RFO b ON a.PK=b.AccountID
--WHERE ISNULL(a.p_LastName,'')<>ISNULL(b.LastName,'')


--SELECT * FROM #RFO  --- Load the Data Set with Indexing key.
--SELECT * FROM #Hybris--- LoadThe DataSet with Indexing Key

--SELECT * FROM #RFO a
--FULL OUTER JOIN #Hybris b ON a.AccountID=b.PK
--WHERE a.AccountID IS NULL OR  b.pk IS NULL 




--SELECT * FROM #RFO  --- Load the Data Set with Indexing key.
--SELECT * FROM #Hybris--- LoadThe DataSet with Indexing Key

-- SELECT * FROM (
-- SELECT  AccountID,FirstName FROM #RFO  		   
--		  EXCEPT 
--		  SELECT PK,P_FirstName  FROM #Hybris 		  
--		  )SourceA
--		    JOIN 
--		 ( SELECT PK,P_FirstName FROM #Hybris 
--		  EXCEPT 
--		 SELECT AccountID,FirstName FROM #RFO
--		 )TargetB on  SourceA.AccountID=TargetB.PK

		 
-- SELECT *
-- FROM   ( SELECT    AccountID ,
--                    LastName
--          FROM      #RFO
--          EXCEPT
--          SELECT    PK ,
--                    p_LastName
--          FROM      #Hybris
--        ) SourceA
--        JOIN ( SELECT   PK ,
--                        p_LastName
--               FROM     #Hybris
--               EXCEPT
--               SELECT   AccountID ,
--                        LastName
--               FROM     #RFO
--             ) TargetB ON SourceA.AccountID = TargetB.PK

		 
--DROP TABLE #RFO 
--DROP TABLE #Hybris

--CREATE TABLE #RFO
--(AccountID INT DEFAULT 1
--,FirstName NVARCHAR(20),LastName NVARCHAR(20),rn INT IDENTITY(1,1)
--)

--CREATE TABLE #Hybris
--(PK BIGINT DEFAULT 1
--,P_FirstName NVARCHAR(20),p_LastName NVARCHAR(20),rn INT IDENTITY(1,1)
--)
  
--INSERT INTO #RFO
--        (  FirstName,LastName )
--VALUES  ( 
--          N'Nand'  -- FirstName - nvarchar(20)
--		  ,'Khadka'
--          ),
--		  (N'Rajendra','Khadka')
--		  ,(N'Sharad',NULL)


		  
--INSERT INTO #Hybris
--        ( P_FirstName,p_LastName )
--VALUES  ( 
--          N'Nand'  -- FirstName - nvarchar(20)
--		   ,'Khadka'
--          ),
--		  (N'Shishir' ,'Khadka')
--		  ,(N'Shishir' ,'Khadka')
--		  ,(N'Mana',NULL)



--INSERT INTO #RFO
--        ( AccountID, FirstName,LastName )
--VALUES  ( 5,
--          N'Lemon'  
--		  ,'Khadka'
--          )

		  
--INSERT INTO #Hybris
--        ( pk, P_FirstName,p_LastName )
--VALUES  ( 5,
--          N'Mango'  -- FirstName - nvarchar(20)
--		   ,'Khadka'
--          )


--INSERT INTO #RFO
--        ( AccountID, FirstName,LastName )
--VALUES  ( 7,
--          N'Lemon'  
--		  ,'Khadka'
--          )


																																												  
--INSERT INTO #Hybris
--        ( pk, P_FirstName,p_LastName )
--VALUES  ( 11,
--          N'Mango'  -- FirstName - nvarchar(20)
--		   ,'Khadka'
--          )


--INSERT INTO #RFO
--        ( AccountID, FirstName,LastName )
--VALUES  ( 11,
--          N'Lemon'  
--		  ,'Khadka'
--          )

--SELECT * FROM #RFO 
--SELECT * FROM #Hybris

----- Records Matching or not.

-- SELECT *
-- FROM   ( SELECT    AccountID ,
--                    COUNT(AccountID) Ct
--          FROM      #RFO
--          GROUP BY  AccountID
--        ) SourceA
--     JOIN ( SELECT    PK ,
--                                    COUNT(pk) pt
--                          FROM      #Hybris
--                          GROUP BY  pk
--                        ) TargetB ON SourceA.AccountID = TargetB.PK
--						WHERE SourceA.ct<>TargetB.pt

----- Missing from Source to Target.

-- SELECT *
-- FROM   ( SELECT    AccountID ,
--                    COUNT(AccountID) Ct
--          FROM      #RFO
--          GROUP BY  AccountID
--        ) SourceA
--   FULL OUTER  JOIN ( SELECT    PK ,
--                                    COUNT(pk) pt
--                          FROM      #Hybris
--                          GROUP BY  pk
--                        ) TargetB ON SourceA.AccountID = TargetB.PK
--						WHERE TargetB.pk IS NULL OR SourceA.AccountID IS NULL 





--DECLARE @SourceKey NVARCHAR(20)= 'AccountID',
--    @DestinationKey NVARCHAR(20)= 'PK' ,
--    @SourceColumn NVARCHAR(20)= 'FirstName' ,
--    @DestinationColumn NVARCHAR(20)= 'P_FirstName' ,
--    @sql NVARCHAR(MAX),
--	@Record INT

--SELECT  @sql = ' select * from (SELECT  ' + @SourceKey + ',' + @SourceColumn + ' FROM #RFO  		   
--		  EXCEPT 
--		  SELECT '+ @DestinationKey+','+@DestinationColumn+ '  FROM #Hybris 		  
--		  )SourceA
--		   left JOIN 
--		 ( SELECT ' +@DestinationKey+ ','+@DestinationColumn+ ' FROM #Hybris 
--		  EXCEPT 
--		 SELECT '+@SourceKey+','+@SourceColumn+' FROM #RFO
--		 )TargetB on  SourceA.AccountID=TargetB.PK'

--		 PRINT @sql

--		 EXECUTE sp_executeSQl @sql
--		 SELECT @Record=@@ROWCOUNT
--		 SELECT  @Record totalRow

--		 -- OR
--SET NOCOUNT ON 
		 
--DECLARE @SourceKey NVARCHAR(20)= 'AccountID',
--    @DestinationKey NVARCHAR(20)= 'PK' ,
--    @SourceColumn NVARCHAR(20)= 'FirstName' ,
--    @DestinationColumn NVARCHAR(20)= 'P_FirstName' ,
   
   
--   DECLARE  @sql NVARCHAR(MAX)
--   DECLARE @Record INT

--SELECT  @sql =  [Sql Stmt] FROM #map_tab	   
		  

--		 PRINT @sql

--		 EXECUTE sp_executeSQl @sql
--		 SELECT @Record=@@ROWCOUNT
--		 SELECT  @Record totalRow
		 
		 
		

--		DELETE #Hybris 
--		WHERE pk NOT IN (11)
--		DELETE #RFO 
--		WHERE AccountID NOT IN (11)

--	SELECT AccountID,FirstName,PK,P_FirstName FROM #RFO a ,
--		 #Hybris b WHERE a.AccountID=b.PK AND a.rn=b.rn
--		AND  a.FirstName<>b.P_FirstName

	


--	-- You should be created Row Number for that child tables to get properly.


--	DELETE #Hybris
--	WHERE pk<>9

--	DELETE #RFO 
--	WHERE AccountID<>9


----- Child Entities Validation ;


-- DECLARE @sourceColumn NVARCHAR(25)= 'FirstName' ,
--    @TargetColumn NVARCHAR(50)= 'P_FirstName'
		 


-- SELECT ' SELECT COALESCE(A.RFOKey,B.HybrisKey) AS [Key] ,A.' + @sourceColumn
--        + ' ,B.' + @TargetColumn + ' FROM (
--		  SELECT RFOKey,' + @SourceColumn + ' FROM #RFO 
--		  EXCEPT 
--		  SELECT HybrisKey,' + @TargetColumn + ' FROM #Hybris) A
--		   LEFT JOIN 
--		 ( SELECT HybrisKey,' + @TargetColumn + 'FROM #Hybris
--		  EXCEPT 
--		  SELECT RFOKey,' + @sourceColumn
--        + ' FROM #RFO)B on  A.RFOKey=B.HybrisKey

--		  UNION ALL 
--		   SELECT COALESCE(A.RFOKey,B.HybrisKey) AS [Key],A.' + @sourceColumn
--        + ',B.' + @TargetColumn + '    FROM 
--		 ( SELECT RFOKey,' + @sourceColumn + ' FROM #RFO
--		  EXCEPT 
--		  SELECT HybrisKey,' + @TargetColumn + ' FROM #Hybris)A
--		  RIGHT JOIN 
--		  (
--		  SELECT HybrisKey,' + @TargetColumn + ' FROM #Hybris
--		  EXCEPT 
--		  SELECT RFOKey,' + @sourceColumn + ' FROM #RFO) 
--		  B on  A.RFOKey=B.HybrisKey  '   


		   
		  	  


--		 SELECT  SourceA.AccountID,SourceA.FirstName ,TargetB.PK,TargetB.P_FirstName
--		 FROM (
--		  SELECT AccountID,FirstName FROM #RFO 
--		  EXCEPT 
--		  SELECT PK,P_FirstName FROM #Hybris) SourceA
--		   LEFT JOIN 
--		 ( SELECT PK,P_FirstName FROM #Hybris
--		  EXCEPT 
--		  SELECT AccountID,FirstName FROM #RFO)TargetB on  SourceA.AccountID=TargetB.PK

--		  UNION ALL 
--		   SELECT SourceA.AccountID,SourceA.FirstName ,TargetB.PK,TargetB.P_FirstName    FROM 
--		 ( SELECT AccountID,FirstName FROM #RFO
--		  EXCEPT 
--		  SELECT PK,P_FirstName FROM #Hybris)SourceA
--		  RIGHT JOIN 
--		  (
--		  SELECT PK,P_FirstName FROM #Hybris
--		  EXCEPT 
--		  SELECT AccountID,FirstName FROM #RFO)TargetB on  SourceA.AccountID=TargetB.PK 




		  	  
-- SELECT DISTINCT *  FROM (

--		 SELECT SourceA.AccountID,SourceA.FirstName ,TargetB.PK,TargetB.P_FirstName
--		 FROM (
--		  SELECT AccountID,FirstName FROM #RFO 
--		  EXCEPT 
--		  SELECT PK,P_FirstName FROM #Hybris) SourceA
--		   LEFT JOIN 
--		 ( SELECT PK,P_FirstName FROM #Hybris
--		  EXCEPT 
--		  SELECT AccountID,FirstName FROM #RFO)TargetB on  SourceA.AccountID=TargetB.PK

--		  UNION ALL
--		   SELECT SourceA.AccountID,SourceA.FirstName ,TargetB.PK,TargetB.P_FirstName    FROM 
--		 ( SELECT AccountID,FirstName FROM #RFO
--		  EXCEPT 
--		  SELECT PK,P_FirstName FROM #Hybris)SourceA
--		  RIGHT JOIN 
--		  (
--		  SELECT PK,P_FirstName FROM #Hybris
--		  EXCEPT 
--		  SELECT AccountID,FirstName FROM #RFO) 
--		  TargetB on  A.AccountID=TargetB.pk) T