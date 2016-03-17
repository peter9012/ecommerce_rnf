
--*********************************************************************************************************
-- Step:1 

/*

/* Full BackUp of Orders, Items and Users Very FirstTime. */

IF OBJECT_ID('DataMigration.dbo.Orders_Bkp') IS NOT NULL
    DROP TABLE DataMigration.dbo.Orders_bkp

IF OBJECT_ID('DataMigration.dbo.Orderentries_bkp') IS NOT NULL
    DROP TABLE DataMigration.dbo.Orderentries_bkp


IF OBJECT_ID('DataMigration.dbo.Users_bkp') IS NOT NULL
    DROP TABLE DataMigration.dbo.Users_bkp

SELECT  'Orders BackUp Started' AS Step ,
        GETDATE() AS StartedTime

SELECT  *
INTO    DataMigration.dbo.Orders_bkp
FROM    Hybris..orders
WHERE   p_template = 1
        AND currencypk = 8796125855777;

CREATE CLUSTERED INDEX Cls ON DataMigration.dbo.Orders_bkp(PK)
CREATE NONCLUSTERED INDEX clsn ON DataMigration.dbo.Orders_bkp(code)

SELECT  'Orders BackUp Completed' AS Step ,
        GETDATE() AS CompletedTime

SELECT  b.*
INTO    DataMigration.dbo.Orderentries_bkp
FROM    Hybris.dbo.orders a
        JOIN Hybris.dbo.orderentries b ON a.PK = b.orderpk
WHERE   a.p_template = 1
        AND currencypk = 8796125855777;

CREATE CLUSTERED INDEX cls ON DataMigration.dbo.Orderentries_bkp(PK)

SELECT  'OrdersEntries BackUp Completed' AS Step ,
        GETDATE() AS CompletedTime


SELECT  *
INTO    DataMigration.dbo.Users_bkp
FROM    Hybris..users
WHERE   p_country = 8796100624418;

CREATE CLUSTERED INDEX cls ON  DataMigration.dbo.Users_bkp(PK)
CREATE NONCLUSTERED INDEX cls1 ON  DataMigration.dbo.Users_bkp(p_rfaccountid)

SELECT  'Users BackUp Completed' AS Step ,
        GETDATE() AS CompletedTime

	*/

--*********************************************************************************************************************

-- Step2:  

/* Taking Back UP Templates and Items to Compare Orders NextDay.*/
 
DECLARE @BackupTemplateName NVARCHAR(255) ,
    @BackupItemsName NVARCHAR(255) ,
    @Date DATE;  
 
SELECT  @Date = CAST(GETDATE() AS DATE);
    
SET @BackupTemplateName = CONCAT('Datamigration.dbo.[Templates', '_', @Date,
                                 '_BackUp]'); 


SELECT  @BackupTemplateName;
EXECUTE
('SELECT   *   INTO ' + @BackupTemplateName +
'FROM  Hybris.dbo.orders
WHERE     p_template = 1
AND currencypk = 8796125855777
AND p_active = 1               
AND CAST(p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)'); 

EXECUTE ('CREATE CLUSTERED INDEX cls ON ' +@BackupTemplateName+'(PK)');
EXECUTE ('CREATE NONCLUSTERED INDEX cls1 ON ' +@BackupTemplateName+'(code)');

EXECUTE
('SELECT   count(*),b.InternalCode from  ' + @BackupTemplateName +
'a join   Hybris.dbo.composedtypes b on a.TypepkString=b.pk
WHERE     p_template = 1
AND currencypk = 8796125855777
AND p_active = 1               
AND CAST(p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)
group By b.InternalCode '); 



				     
SET @BackupItemsName = CONCAT('Datamigration.dbo.[TemplateItems', '_', @Date,
                              '_BackUp]'); 
EXECUTE
('SELECT   oi.*   INTO ' + @BackupItemsName +
' FROM '+ @BackupTemplateName + ' ho
JOIN Hybris..orderentries oi ON ho.pk=oi.orderpk
WHERE ho.p_template=1 AND ho.currencypk=8796125855777
AND ho.p_active = 1               
AND CAST(ho.p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)'); 

SELECT  @BackupItemsName;

EXECUTE ('CREATE CLUSTERED INDEX cls ON ' +@BackupItemsName+'(PK)');


EXECUTE
(' SELECT   count(a.pk)  from  ' + @BackupItemsName +
'a  join ' + @BackupTemplateName + ' ho on a.orderpk=ho.pk
join  Hybris.dbo.composedtypes b on ho.TypepkString=b.pk
WHERE ho.p_template = 1
AND ho.currencypk = 8796125855777
AND ho.p_active = 1               
AND CAST(ho.p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)
'); 





/* Taking Back UP ONLY Eligible Templates and Items to Compare Orders NextDay.*/
/*
 
DECLARE @BackupTemplateName NVARCHAR(255) ,
    @BackupItemsName NVARCHAR(255) ,
    @Date DATE;  
 
SELECT  @Date = CAST(GETDATE() AS DATE);
    
SET @BackupTemplateName = CONCAT('Datamigration.dbo.[Templates', '_', @Date,
                                 '_BackUp]'); 


SELECT  @BackupTemplateName;
EXECUTE
('SELECT ho.*   INTO ' + @BackupTemplateName +
'FROM  Hybris.dbo.orders ho
join Hybris..users u on u.pk=ho.userpk
join Hybris..EnumerationValues v on v.pk=u.p_accountstatus AND v.Code=''ACTIVE''
WHERE ho.p_template=1
 AND ho.currencypk=8796125855777
AND ho.p_active = 1           
 AND ho.p_ccfailurecount < 3
 AND ho.TypepkString IN( 8796124676178, 8796124741714 )
			AND CAST(ISNULL(ho.p_lastprocessingdate,''1900-01-01'') AS DATE)<CAST(DATEADD(DAY,-5,GETDATE()) AS DATE)
            AND CAST(ho.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              CAST(GETDATE() AS DATE))
                                                 AND    CAST(GETDATE() AS DATE)
UNION ALL
SELECT  ho.*    FROM  Hybris.dbo.orders ho
join Hybris..users u on u.pk=ho.userpk
join Hybris..EnumerationValues v on v.pk=u.p_accountstatus AND v.Code=''ACTIVE''
WHERE ho.p_template=1
 AND ho.currencypk=8796125855777
AND ho.p_active = 1           
 AND ho.p_ccfailurecount < 4
 AND ho.TypepkString IN( 8796124708946 )
			AND CAST(ISNULL(ho.p_lastprocessingdate,''1900-01-01'') AS DATE)<CAST(DATEADD(DAY,-5,GETDATE()) AS DATE)
            AND CAST(ho.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              CAST(GETDATE() AS DATE))
                                                 AND    CAST(GETDATE() AS DATE)

												 '); 

EXECUTE ('CREATE CLUSTERED INDEX cls ON ' +@BackupTemplateName+'(PK)');
EXECUTE ('CREATE NONCLUSTERED INDEX cls1 ON ' +@BackupTemplateName+'(code)');


EXECUTE
('SELECT   count(*),b.InternalCode from  ' + @BackupTemplateName +
'a join   Hybris.dbo.composedtypes b on a.TypepkString=b.pk
WHERE     p_template = 1
AND currencypk = 8796125855777
AND p_active = 1 
group By b.InternalCode '); 



				     
SET @BackupItemsName = CONCAT('Datamigration.dbo.[TemplateItems', '_', @Date,
                              '_BackUp]'); 
EXECUTE
('SELECT   oi.*   INTO ' + @BackupItemsName +
' FROM '+ @BackupTemplateName + ' ho
JOIN Hybris..orderentries oi ON ho.pk=oi.orderpk
WHERE ho.p_template=1
 AND ho.currencypk=8796125855777
AND ho.p_active = 1           
 AND ho.p_ccfailurecount < 3
 AND ho.TypepkString IN( 8796124676178, 8796124741714 )
			AND CAST(ISNULL(ho.p_lastprocessingdate,''1900-01-01'') AS DATE)<CAST(DATEADD(DAY,-5,GETDATE()) AS DATE)
            AND CAST(ho.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              CAST(GETDATE() AS DATE))
                                                 AND    CAST(GETDATE() AS DATE)
UNION ALL 
SELECT   oi.*  
 FROM '+ @BackupTemplateName + ' ho
JOIN Hybris..orderentries oi ON ho.pk=oi.orderpk
WHERE ho.p_template=1 AND ho.currencypk=8796125855777
AND ho.p_active = 1               
AND CAST(ho.p_schedulingdate AS DATE) <= CAST(GETDATE() AS DATE)
 AND ho.p_ccfailurecount < 4
 AND ho.TypepkString IN(8796124708946)
			AND CAST(ISNULL(ho.p_lastprocessingdate,''1900-01-01'') AS DATE)<CAST(DATEADD(DAY,-5,GETDATE()) AS DATE)
            AND CAST(ho.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              CAST(GETDATE() AS DATE))
                                                 AND    CAST(GETDATE() AS DATE)
												 '); 

SELECT  @BackupItemsName;

EXECUTE ('CREATE CLUSTERED INDEX cls ON ' +@BackupItemsName+'(PK)');


EXECUTE
('SELECT   COUNT(a.pk) AS TotalCountsItem  from  ' + @BackupItemsName +
'a  join ' + @BackupTemplateName + ' ho on a.orderpk=ho.pk
join  Hybris.dbo.composedtypes b on ho.TypepkString=b.pk
WHERE ho.p_template = 1
AND ho.currencypk = 8796125855777
  '); 


  ************************/


  

  /* Getting Counts of Eligible Templates to Run */

DECLARE @Date DATE = CAST(GETDATE() AS DATE);


SELECT  COUNT(*) ,
        c.InternalCode--,CAST(b.p_schedulingdate AS DATE)
FROM    Hybris..orders  b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
WHERE   b.p_template = 1
        AND b.currencypk = 8796125855777
        AND b.TypePkString IN ( 8796124676178, 8796124741714 )
        AND b.p_active = 1
        AND b.p_ccfailurecount < 3
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
        AND CAST(b.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30, @Date)
                                             AND     @Date
GROUP BY c.InternalCode--,CAST(b.p_schedulingdate AS DATE)
UNION ALL
SELECT  COUNT(*) ,
        c.InternalCode--,CAST(b.p_schedulingdate AS DATE)
FROM    Hybris..orders b --<<<<<<<<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
WHERE   b.p_template = 1
        AND b.currencypk = 8796125855777
        AND b.TypePkString IN ( 8796124708946 )
        AND b.p_active = 1
        AND b.p_ccfailurecount < 4
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
        AND CAST(b.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30, @Date)
                                             AND     @Date
GROUP BY c.InternalCode;
--,CAST(b.p_schedulingdate AS DATE)


/*

/* Forwarding UnWanted  templates SchedulingDate to Future */

--CRP and Pulse
DECLARE @Date DATE = CAST(GETDATE() AS DATE);

UPDATE b 
SET b.p_schedulingdate=DATEADD(MONTH,1,GETDATE())
FROM   Hybris..orders b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
WHERE   b.p_template = 1
       -- AND b.currencypk = 8796125855777
        AND b.TypePkString IN ( 8796124676178, 8796124741714 )
        AND b.p_active = 1
        AND b.p_ccfailurecount < 3
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
        AND CAST(b.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30, @Date)
                                             AND     @Date


	--PCPerks
UPDATE b 
SET b.p_schedulingdate=DATEADD(MONTH,1,GETDATE()) 
FROM   Hybris..orders b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
		
WHERE   b.p_template = 1
       -- AND b.currencypk = 8796125855777
        AND b.TypePkString IN ( 8796124708946 )
        AND b.p_active = 1
        AND b.p_ccfailurecount < 4
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
        AND CAST(b.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30, @Date)
                                             AND     @Date



/* Getiing Sample DATA AND Loading to Temp */
IF OBJECT_ID('Tempdb..#TEMP') IS NOT NULL 
DROP TABLE #TEMP

DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT TOP 1000  b.pk INTO #TEMP
FROM   Hybris..orders b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
WHERE   b.p_template = 1
       AND b.currencypk = 8796125855777
        AND b.TypePkString IN ( 8796124676178)
        AND b.p_active = 1
        AND b.p_ccfailurecount < 3
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -7, @Date)
       
UNION 
SELECT TOP 1000   b.pk 
FROM   Hybris..orders b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
WHERE   b.p_template = 1
       AND b.currencypk = 8796125855777
        AND b.TypePkString IN (8796124741714 )
        AND b.p_active = 1
        AND b.p_ccfailurecount < 3
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -7, @Date)       

UNION 
SELECT TOP 1000  b.pk 
FROM   Hybris..orders b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
WHERE   b.p_template = 1
        AND b.currencypk = 8796125855777
        AND b.TypePkString IN (8796124708946)
        AND b.p_active = 1
        AND b.p_ccfailurecount < 4
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -7, @Date)
       
	   /* Modifying SchedulingDate to Selected Temp */

	   UPDATE ho
	   SET ho.p_schedulingdate=DATEADD(DAY,-10,GETDATE())
	   FROM Hybris..orders ho 
	   JOIN #Temp t ON t.pk=ho.PK

	  
	  
	   /* Checking Eligible Templates with Types */

	   DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) ,
        c.InternalCode ,CAST(b.p_schedulingdate AS DATE),b.currencypk
FROM   Hybris..orders b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
WHERE   b.p_template = 1
       -- AND b.currencypk = 8796125855777
        AND b.TypePkString IN ( 8796124676178, 8796124741714 )
        AND b.p_active = 1
        AND b.p_ccfailurecount < 3
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
        AND CAST(b.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30, @Date)
                                             AND     @Date
GROUP BY c.InternalCode ,CAST(b.p_schedulingdate AS DATE),b.currencypk
UNION ALL
SELECT  COUNT(*) ,
        c.InternalCode ,CAST(b.p_schedulingdate AS DATE),b.currencypk
FROM    Hybris..orders  b --<<<<<<<<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
WHERE   b.p_template = 1
       -- AND b.currencypk = 8796125855777
        AND b.TypePkString IN ( 8796124708946 )
        AND b.p_active = 1
        AND b.p_ccfailurecount < 4
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
        AND CAST(b.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30, @Date)
                                             AND     @Date
GROUP BY c.InternalCode
 ,CAST(b.p_schedulingdate AS DATE),b.currencypk;

														*/


--*****************************************************************************************************************
--Step3:  Template Processing Job  Analysis.

/* Comparing Orders with Eligible Templates */		

	/*Task-1 Checking If Non-Eligible Templates generated Orders.*/
	
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  ho.PK ,
        b.code ,
        ho.createdTS
FROM    Hybris..orders ho
        JOIN DataMigration.dbo.Temp_01_11 b --<<<<<<<<<<<<<<<<<<<<<<Get BackUpTable
        ON ho.p_associatedtemplate = b.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
WHERE   b.p_template = 1
        AND b.currencypk = 8796125855777
        AND b.TypePkString IN ( 8796124676178, 8796124741714 )
        AND CAST(ho.createdTS AS DATE) = @Date
        AND ( b.p_active = 0
              OR b.p_ccfailurecount >= 3
              OR ho.currencypk <> 8796125855777
              OR CAST(b.p_schedulingdate AS DATE) < DATEADD(DAY, -30, @Date)
              OR v.Code <> 'ACTIVE'
            )
UNION ALL
SELECT  ho.PK ,
        b.code ,
        ho.createdTS
FROM    Hybris..orders ho
        JOIN DataMigration.dbo.Temp_01_11 b --<<<<<<<<<<<<<<<<<<<<<<<<<<<<Get BackUpTable
        ON ho.p_associatedtemplate = b.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
WHERE   b.p_template = 1
        AND b.currencypk = 8796125855777
        AND b.TypePkString IN ( 8796124708946 )
        AND CAST(ho.createdTS AS DATE) = @Date
        AND ( b.p_active = 0
              OR b.p_ccfailurecount >= 4
              OR ho.currencypk <> 8796125855777
              OR CAST(b.p_schedulingdate AS DATE) < DATEADD(DAY, -30, @Date)
              OR v.Code <> 'ACTIVE'
            );





	/* Task-2 Getting OrdersCount Templates-TypeWise */
	
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) ,
        c.InternalCode
FROM    Hybris..orders ho
        JOIN DataMigration.dbo.Temp_01_11 b --<<<<<<<<<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        ON ho.p_associatedtemplate = b.PK
        JOIN Hybris..composedtypes c ON c.PK = b.TypePkString
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND b.currencypk = 8796125855777
GROUP BY c.InternalCode;

	
	
	/* Checking Eligible Templates not been Picked.*/
	
	/* Task 3 : Counts By Templates types.8 */
		
	
    DECLARE @Date DATE = CAST(GETDATE() AS DATE);
    SELECT  a.Code AS Templates ,
            c.InternalCode AS TemplateType
    FROM    RFOperations.dbo.[Templates_2016-03-10_BackUp] a --<<<<<<<<<<<<<<<<<<<<<  BackUpTable.   
            JOIN Hybris..composedtypes c ON c.PK = a.TypePkString
            JOIN Hybris..users u ON u.PK = a.userpk
            JOIN Hybris..enumerationvalues v ON v.PK = u.p_accountstatus
                                                AND v.Code = 'ACTIVE'
    WHERE   a.TypePkString IN ( 8796124676178 )
            AND a.p_active = 1
            AND a.p_template = 1
            AND a.currencypk = 8796125855777
            AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
            AND CAST(a.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              @Date)
                                                 AND     @Date
            AND a.p_ccfailurecount < 3
            AND NOT EXISTS ( SELECT 1
                             FROM   Hybris..orders ho
                             WHERE  ho.p_associatedtemplate = a.PK
                                    AND ho.TypePkString <> 8796127723602
                                    AND ho.p_template = 0
                                    AND CAST(ho.createdTS AS DATE) = @Date )
    UNION ALL
    SELECT  a.Code AS Templates ,
            c.InternalCode AS TemplateType
    FROM    RFOperations.dbo.[Templates_2016-03-10_BackUp] a --<<<<<<<<<<<<<<<<<<<<<  BackUpTable.   
            JOIN Hybris..composedtypes c ON c.PK = a.TypePkString
            JOIN Hybris..users u ON u.PK = a.userpk
            JOIN Hybris..enumerationvalues v ON v.PK = u.p_accountstatus
                                                AND v.Code = 'ACTIVE'
    WHERE   a.TypePkString IN ( 8796124741714 )
            AND a.p_active = 1
            AND a.p_template = 1
            AND a.currencypk = 8796125855777
            AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
            AND CAST(a.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              @Date)
                                                 AND     @Date
            AND a.p_ccfailurecount < 3
            AND NOT EXISTS ( SELECT 1
                             FROM   Hybris..orders ho
                             WHERE  ho.p_associatedtemplate = a.PK
                                    AND ho.TypePkString <> 8796127723602
                                    AND ho.p_template = 0
                                    AND CAST(ho.createdTS AS DATE) = @Date )
    UNION ALL
    SELECT  a.Code AS Templates ,
            c.InternalCode AS TemplateType
    FROM    RFOperations.dbo.[Templates_2016-03-10_BackUp] a --<<<<<<<<<<<<<<<<<<<<<  BackUpTable.   
            JOIN Hybris..composedtypes c ON c.PK = a.TypePkString
            JOIN Hybris..users u ON u.PK = a.userpk
            JOIN Hybris..enumerationvalues v ON v.PK = u.p_accountstatus
                                                AND v.Code = 'ACTIVE'
    WHERE   a.TypePkString IN ( 8796124708946 )
            AND a.p_active = 1
            AND a.p_template = 1
            AND a.currencypk = 8796125855777
            AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
            AND CAST(a.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              @Date)
                                                 AND     @Date
            AND a.p_ccfailurecount < 4
            AND NOT EXISTS ( SELECT 1
                             FROM   Hybris..orders ho
                             WHERE  ho.p_associatedtemplate = a.PK
                                    AND ho.TypePkString <> 8796127723602
                                    AND ho.p_template = 0
                                    AND CAST(ho.createdTS AS DATE) = @Date );
	


	/* Task 4: Details of Not Picked Eligible Templates*/
	
	/*
	
    DECLARE @Date DATE = CAST(GETDATE() AS DATE);
    SELECT   a.code Templates,
            a.p_template ,
            a.p_active ,
            a.p_ccfailurecount ,
            CAST(a.p_schedulingdate AS DATE) ScheduleDate ,
            CAST(a.p_lastprocessingdate AS DATE) LastPDate ,
            c.InternalCode,
			u.p_rfaccountid,
			v.Code AS AccountStatus,
			u.p_sourcename
    FROM    RFOperations.dbo.[Templates_2016-03-10_BackUp] a --<<<<<<<<<<<<<<<<<<<<<  BackUpTable.   
            JOIN Hybris..composedtypes c ON c.PK = a.TypePkString
            JOIN Hybris..users u ON u.PK = a.userpk
            JOIN Hybris..enumerationvalues v ON v.PK = u.p_accountstatus
                                                AND v.Code = 'ACTIVE'
    WHERE   a.TypePkString IN ( 8796124676178 )
            AND a.p_active = 1
            AND a.p_template = 1
            AND a.currencypk = 8796125855777
            AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
            AND CAST(a.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              @Date)
                                                 AND     @Date
            AND a.p_ccfailurecount < 3
            AND NOT EXISTS ( SELECT 1
                             FROM   Hybris..orders ho
                             WHERE  ho.p_associatedtemplate = a.PK
                                    AND ho.TypePkString <> 8796127723602
                                    AND ho.p_template = 0
                                    AND CAST(ho.createdTS AS DATE) = @Date )
    UNION ALL
    SELECT  a.code Templates,
            a.p_template ,
            a.p_active ,
            a.p_ccfailurecount ,
            CAST(a.p_schedulingdate AS DATE) ScheduleDate ,
            CAST(a.p_lastprocessingdate AS DATE) LastPDate ,
            c.InternalCode,
			u.p_rfaccountid,
			v.Code AS AccountStatus,
			u.p_sourcename
    FROM    RFOperations.dbo.[Templates_2016-03-10_BackUp] a --<<<<<<<<<<<<<<<<<<<<<  BackUpTable.   
            JOIN Hybris..composedtypes c ON c.PK = a.TypePkString
            JOIN Hybris..users u ON u.PK = a.userpk
            JOIN Hybris..enumerationvalues v ON v.PK = u.p_accountstatus
                                                AND v.Code = 'ACTIVE'
    WHERE   a.TypePkString IN ( 8796124741714 )
            AND a.p_active = 1
            AND a.p_template = 1
            AND a.currencypk = 8796125855777
            AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
            AND CAST(a.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              @Date)
                                                 AND     @Date
            AND a.p_ccfailurecount < 3
            AND NOT EXISTS ( SELECT 1
                             FROM   Hybris..orders ho
                             WHERE  ho.p_associatedtemplate = a.PK
                                    AND ho.TypePkString <> 8796127723602
                                    AND ho.p_template = 0
                                    AND CAST(ho.createdTS AS DATE) = @Date )
    UNION ALL
    SELECT   a.code Templates,
            a.p_template ,
            a.p_active ,
            a.p_ccfailurecount ,
            CAST(a.p_schedulingdate AS DATE) ScheduleDate ,
            CAST(a.p_lastprocessingdate AS DATE) LastPDate ,
            c.InternalCode,
			u.p_rfaccountid,
			v.Code AS AccountStatus,
			u.p_sourcename
    FROM    RFOperations.dbo.[Templates_2016-03-10_BackUp] a --<<<<<<<<<<<<<<<<<<<<<  BackUpTable.   
            JOIN Hybris..composedtypes c ON c.PK = a.TypePkString
            JOIN Hybris..users u ON u.PK = a.userpk
            JOIN Hybris..enumerationvalues v ON v.PK = u.p_accountstatus
                                                AND v.Code = 'ACTIVE'
    WHERE   a.TypePkString IN ( 8796124708946 )
            AND a.p_active = 1
            AND a.p_template = 1
            AND a.currencypk = 8796125855777
            AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
            AND CAST(a.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              @Date)
                                                 AND     @Date
            AND a.p_ccfailurecount < 4
            AND NOT EXISTS ( SELECT 1
                             FROM   Hybris..orders ho
                             WHERE  ho.p_associatedtemplate = a.PK
                                    AND ho.TypePkString <> 8796127723602
                                    AND ho.p_template = 0
                                    AND CAST(ho.createdTS AS DATE) = @Date );

					*/

					
					
			
				/* autoshipCRPProcessesJob Analysis: */
			--==============================================
           
		  
		   /* Task 5 : CountsWithOrderTypes.*/

DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) NoOfOrders ,
        v.Code AS OrderType
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND a.TypePkString = 8796124676178  --CRP 
GROUP BY v.Code;

				   
					/* Task 6: Checking RFO Flown (Not for Created Status) */

DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) NoOfOrders ,
        v.Code AS OrderType
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN RFOperations.Hybris.Orders b ON ho.PK = b.OrderID
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND a.TypePkString = 8796124676178
GROUP BY v.Code;


					/* Task 7: Checking BoomiError  */
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) NoOfOrders ,
        v.Code AS OrderType ,
        r.ErrorMessage
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN RFOperations.Logging.BoomiError r ON ho.PK = r.RecordID
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND a.TypePkString = 8796124676178
GROUP BY v.Code ,
        r.ErrorMessage;

				  
				  /* Task 8 : Created Status Details */
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  a.code AS Templates ,
        ho.createdTS ,
        ho.PK AS OrderPk ,
        ho.code AS OrderNumber ,
        v.Code AS OrderType
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
                                             AND v.Code = 'CREATED'
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND a.TypePkString = 8796124676178; 

							/* Task 9: FailReason with Counts*/
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) Counts ,
        n.p_ordernotes
FROM    Hybris..orders a --<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.ordernotes n ON n.p_order = ho.PK
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND v.Code = 'FAILED'
        AND a.TypePkString = 8796124676178
GROUP BY n.p_ordernotes;
				 
					/* Task 10: Failed Status Details */
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  a.code AS Templates ,
        ho.createdTS ,
        ho.PK AS OrderPk ,
        ho.code AS OrderNumber ,
        v.Code AS OrderType ,
        n.p_ordernotes
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.ordernotes n ON n.p_order = ho.PK
                                        AND v.Code = 'FAILED'
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND a.TypePkString = 8796124676178; 


							/* Task 11 : Checking Templates been Updated afterPicking 'Submitted or Failed' */
                            
							--Submitted Status:
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  ho.PK AS OrderPK ,
        ho.code AS OrderNumber ,
        v.Code ,
        ho.createdTS ,
        b.code AS Templates ,
        b.modifiedTS ,
        b.p_lastprocessingdate ,
        b.p_schedulingdate ,
        b.p_ccfailurecount
FROM    Hybris..orders a -- This is Back up Table.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris..orders b ON a.PK = b.PK
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND v.Code = 'SUBMITTED'
        AND b.TypePkString = 8796124676178
        AND ( CAST(b.p_lastprocessingdate AS DATE) <> @Date
              OR CAST(b.p_schedulingdate AS DATE) <> DATEADD(MONTH, 1, @Date)
              OR b.p_ccfailurecount <> 0
            );
                          --Failed Status 
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  ho.PK AS OrderPK ,
        ho.code AS OrderNumber ,
        v.Code ,
        ho.createdTS ,
        b.modifiedTS ,
        b.code AS Templates ,
        b.p_lastprocessingdate ,
        b.p_schedulingdate ,
        b.p_ccfailurecount
FROM    Hybris..orders a -- This is Back up Table.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris..orders b ON a.PK = b.PK
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND v.Code = 'FAILED'
        AND b.TypePkString = 8796124676178
        AND ( CAST(b.p_lastprocessingdate AS DATE) <> @Date
              OR CAST(b.p_schedulingdate AS DATE) <> CAST(a.p_schedulingdate AS DATE)
              OR b.p_ccfailurecount <> a.p_ccfailurecount + 1
            );


					

			/*  autoshipPulseRenewalProcessesJob Analysis */

 			--==============================================
			

			 /* Task 12: CountsWithOrderTypes.*/

DECLARE @Date DATE = CAST(GETDATE() AS DATE);	 
SELECT  COUNT(*) NoOfOrders ,
        v.Code AS OrderType
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND a.TypePkString = 8796124741714  --PULSE 
GROUP BY v.Code;

					
					/* Task 13: Checking RFO Flown (Not for Created Status)	*/
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) NoOfOrders ,
        v.Code AS OrderType
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN RFOperations.Hybris.Orders b ON ho.PK = b.OrderID
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND a.TypePkString = 8796124741714
GROUP BY v.Code;

					/* Task 14: Checking BoomiError */
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) NoOfOrders ,
        v.Code AS OrderType ,
        r.ErrorMessage
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN RFOperations.Logging.BoomiError r ON ho.PK = r.RecordID
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND a.TypePkString = 8796124741714
GROUP BY v.Code ,
        r.ErrorMessage;

					--Failed Status:

					/* Task 15:  FailReason with Counts */
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) Counts ,
        n.p_ordernotes
FROM    Hybris..orders a --<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.ordernotes n ON n.p_order = ho.PK
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND v.Code = 'FAILED'
        AND a.TypePkString = 8796124741714
GROUP BY n.p_ordernotes;

							/* Task 16: Failed Status Details:*/
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  a.code AS Templates ,
        ho.createdTS ,
        ho.PK AS OrderPk ,
        ho.code AS OrderNumber ,
        v.Code AS OrderType ,
        n.p_ordernotes
FROM    Hybris..orders a --<<<<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        LEFT JOIN Hybris.dbo.ordernotes n ON n.p_order = ho.PK
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND v.Code = 'FAILED'
        AND a.TypePkString = 8796124741714; 

							
							/* Task 17: Checking Templates been Updated afterPicking 'Submitted or Failed'*/


							--Submitted Status:
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  ho.PK AS OrderPK ,
        ho.code AS OrderNumber ,
        v.Code ,
        ho.createdTS ,
        b.code AS Templates ,
        b.p_lastprocessingdate ,
        b.p_schedulingdate ,
        b.p_ccfailurecount
FROM    Hybris..orders a --<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris..orders b ON a.PK = b.PK
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND v.Code = 'COMPLETED'
        AND b.TypePkString = 8796124741714
        AND ( CAST(b.p_lastprocessingdate AS DATE) <> @Date
              OR CAST(b.p_schedulingdate AS DATE) <> DATEADD(MONTH, 1, @Date)
              OR b.p_ccfailurecount <> 0
            );
                                
								--Failed Status:
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  ho.PK AS OrderPK ,
        ho.code AS OrderNumber ,
        v.Code ,
        ho.createdTS ,
        b.code AS Templates ,
        b.p_lastprocessingdate ,
        b.p_schedulingdate ,
        b.p_ccfailurecount
FROM    Hybris..orders a -- <<<<<<<<<<<<<<<<<<<<<<<< BackTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris..orders b ON a.PK = b.PK
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND v.Code = 'FAILED'
        AND b.TypePkString = 8796124741714
        AND ( CAST(b.p_lastprocessingdate AS DATE) <> @Date
              OR CAST(b.p_schedulingdate AS DATE) <> CAST(a.p_schedulingdate AS DATE)
              OR b.p_ccfailurecount <> a.p_ccfailurecount + 1
            );




					/*  autoshipPCPerksProcessesJob Analysis:*/

				--==============================================
				
				/* Task 18: CountsWithOrderTypes.*/

DECLARE @Date DATE= CAST(GETDATE() AS DATE);	 
SELECT  COUNT(*) NoOfOrders ,
        v.Code AS OrderType
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
WHERE   CAST(ho.createdTS AS DATE) >= @Date
        AND a.TypePkString = 8796124708946			--PCPERKS  
GROUP BY v.Code;


					/* Task 19: Checking RFO Flown (Not for Created Status)*/
								
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) NoOfOrders ,
        v.Code AS OrderType
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN RFOperations.Hybris.Orders b ON ho.PK = b.OrderID
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND a.TypePkString = 8796124708946
GROUP BY v.Code;

					/* Task 20: Checking BoomiError */

DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) NoOfOrders ,
        v.Code AS OrderType ,
        r.ErrorMessage
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN RFOperations.Logging.BoomiError r ON ho.PK = r.RecordID
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND a.TypePkString = 8796124708946
GROUP BY v.Code ,
        r.ErrorMessage;
				

					/*Task 21: FailReason with Counts*/
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*) Counts ,
        n.p_ordernotes
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        LEFT JOIN Hybris.dbo.ordernotes n ON n.p_order = ho.PK
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND v.Code = 'FAILED'
        AND a.TypePkString = 8796124708946
GROUP BY n.p_ordernotes;

							/* Task 22: FAILED with  Details */
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  a.code AS Templates ,
        ho.createdTS ,
        ho.PK AS OrderPk ,
        ho.code AS OrderNumber ,
        v.Code AS OrderType ,
        n.p_ordernotes
FROM    DataMigration.dbo.Temp_01_11 a --<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        LEFT JOIN Hybris.dbo.ordernotes n ON n.p_order = ho.PK
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND v.Code = 'FAILED'
        AND a.TypePkString = 8796124708946; 


							
							/* Task Checking Templates been Updated afterPicking 'Submitted or Failed' */


							-- Submitted Status:
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  ho.PK AS OrderPK ,
        ho.code AS OrderNumber ,
        v.Code ,
        ho.createdTS ,
        b.code AS Templates ,
        b.p_lastprocessingdate ,
        b.p_schedulingdate ,
        b.p_ccfailurecount
FROM    Hybris..orders a --  <<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris..orders b ON a.PK = b.PK
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND v.Code = 'SUBMITTED'
        AND b.TypePkString = 8796124708946
        AND ( CAST(b.p_lastprocessingdate AS DATE) <> @Date
              OR CAST(b.p_schedulingdate AS DATE) <> DATEADD(MONTH, 2, @Date)
              OR b.p_ccfailurecount <> 0
            );
                            
							-- Failed Status:
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  ho.PK AS OrderPK ,
        ho.code AS OrderNumber ,
        v.Code ,
        ho.createdTS ,
        b.code AS Templates ,
        b.p_lastprocessingdate ,
        b.p_schedulingdate ,
        b.p_ccfailurecount
FROM    Hybris..orders a --		<<<<<<<<<<<<<<<<<<<<< BackUpTable.
        JOIN Hybris..orders ho ON a.PK = ho.p_associatedtemplate
        JOIN Hybris..EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris..orders b ON a.PK = b.PK
WHERE   CAST(ho.createdTS AS DATE) = @Date
        AND v.Code = 'FAILED'
        AND b.TypePkString = 8796124708946
        AND ( CAST(b.p_lastprocessingdate AS DATE) <> @Date
              OR CAST(b.p_schedulingdate AS DATE) <> CAST(a.p_schedulingdate AS DATE)
              OR b.p_ccfailurecount <> a.p_ccfailurecount + 1
            );

--***************************************************************************************************************************


		--Step 4: Template Termination 


		/* crpPulseSoftTerminationCronJob: (CRP and Pulse Template Termination because of CC Failure Scenarios)*/
		--=======================================================================================================

						--CRP TEMPLATE TERMINATION -
						--==========================

			/*Task 1: GettingCounts for Terminated Templates.*/

DECLARE @FirstDayofMonth DATE= '2016-01-01' ,
    @RunDate DATE= CAST(GETDATE() AS DATE);
						--Always firstDay of the Month..

SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
WHERE   a.TypePkString = 8796124676178
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate;

								
			/* Task 2: Getting Counts of EligibleTemplates Not Terminated */

DECLARE @FirstDayofMonth DATE= '2016-01-01' ,
    @RunDate DATE= CAST(GETDATE() AS DATE);
						--Always firstDay of the Month..

SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
WHERE   a.TypePkString = 8796124676178
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ( ho.p_active <> 0
              OR v.Code <> 'CANCELLED'
              OR CAST(ho.modifiedTS AS DATE) <> @RunDate
              OR CAST(ho.p_cancelationdate AS DATE) <> @RunDate
            );


								
				/* Task 3: Getting Details of Eligible Templates Not Terminated */

DECLARE @FirstDayofMonth DATE= '2016-01-01' ,
    @RunDate DATE= CAST(GETDATE() AS DATE);
								--Always firstDay of the Month..

SELECT  a.code AS Templates ,
        a.p_ccfailurecount AS OldccFailure ,
        a.p_active AS OldActiveFlag ,
        CAST(a.p_schedulingdate AS DATE) OldScheduled ,
        CAST(a.p_lastprocessingdate AS DATE) OldLastPDate ,
        ho.p_ccfailurecount ,
        ho.p_active ,
        v.Code AS TemplateStatus ,
        CAST(ho.p_schedulingdate AS DATE) AS ScheduledDate ,
        CAST(ho.p_lastprocessingdate AS DATE) LastpDate
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
WHERE   a.TypePkString = 8796124676178
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ( ho.p_active <> 0
              OR v.Code <> 'CANCELLED'
              OR CAST(ho.modifiedTS AS DATE) <> @RunDate
              OR CAST(ho.p_cancelationdate AS DATE) <> @RunDate
            );
											
				/*Task 4:Getting Counts RFO Side Flown back Updated Properly:*/
DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate); 
SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.users u ON u.PK = a.userpk
        JOIN Hybris.dbo.EnumerationValues n ON n.PK = u.p_accountstatus
        JOIN RFOperations.Hybris.Autoship at ON at.AutoshipID = a.PK
WHERE   a.TypePkString = 8796124676178
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate
        AND n.Code = 'SOFTTERMINATEDINVOLUNTARY'
        AND CAST(u.modifiedTS AS DATE) = @RunDate
        AND at.Active = 0
        AND CAST(at.EndDate AS DATE) = @RunDate;
															
			/* Task 5:Getting Counts for  RFO Side Not Updated Properly */

DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate); 
SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.users u ON u.PK = a.userpk
        JOIN Hybris.dbo.EnumerationValues n ON n.PK = u.p_accountstatus
        JOIN RFOperations.Hybris.Autoship at ON at.AutoshipID = a.PK
WHERE   a.TypePkString = 8796124676178
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate
        AND n.Code = 'SOFTTERMINATEDINVOLUNTARY'
        AND CAST(u.modifiedTS AS DATE) = @RunDate
        AND ( at.Active = 0
              OR CAST(at.EndDate AS DATE) = @RunDate
            );


						/* Task 6: Getting Details for  RFO Side Not Update Properly*/
DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate); 
SELECT  a.code AS Templates ,
        ho.p_active ,
        v.Code AS HybrisTempStatus ,
        ho.modifiedTS ,
        ho.p_cancelationdate ,
        u.p_rfaccountid ,
        n.Code AS UserStatus ,
        u.modifiedTS AS UserModified ,
        at.Active AS RFOTempFlag ,
        at.EndDate AS RfoEndDate
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.users u ON u.PK = a.userpk
        JOIN Hybris.dbo.EnumerationValues n ON n.PK = u.p_accountstatus
        JOIN RFOperations.Hybris.Autoship at ON at.AutoshipID = a.PK
WHERE   a.TypePkString = 8796124676178
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate
        AND n.Code = 'SOFTTERMINATEDINVOLUNTARY'
        AND CAST(u.modifiedTS AS DATE) = @RunDate
        AND ( at.Active = 0
              OR CAST(at.EndDate AS DATE) = @RunDate
            );
								



												--PULSE TEMPLATE TERMINATION -
												--==========================


						/*Task 7: GettingCounts for Terminated Templates.*/

DECLARE @FirstDayofMonth DATE= '2016-01-01' ,
    @RunDate DATE= CAST(GETDATE() AS DATE);
									--Always firstDay of the Month..
SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
WHERE   a.TypePkString = 8796124741714
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate;

								
								/* Task 8: Getting Counts of EligibleTemplates Not Terminated */

DECLARE @FirstDayofMonth DATE= '2016-01-01' ,
    @RunDate DATE= CAST(GETDATE() AS DATE);
										--Always firstDay of the Month..

SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
WHERE   a.TypePkString = 8796124741714
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ( ho.p_active <> 0
              OR v.Code <> 'CANCELLED'
              OR CAST(ho.modifiedTS AS DATE) <> @RunDate
              OR CAST(ho.p_cancelationdate AS DATE) <> @RunDate
            );


								
								/* Task 9: Getting Details of Eligible Templates Not Terminated */

DECLARE @FirstDayofMonth DATE= '2016-01-01' ,
    @RunDate DATE= CAST(GETDATE() AS DATE);
								--Always firstDay of the Month..

SELECT  a.code AS Templates ,
        a.p_ccfailurecount AS OldccFailure ,
        a.p_active AS OldActiveFlag ,
        CAST(a.p_schedulingdate AS DATE) OldScheduled ,
        CAST(a.p_lastprocessingdate AS DATE) OldLastPDate ,
        ho.p_ccfailurecount ,
        ho.p_active ,
        v.Code AS TemplateStatus ,
        CAST(ho.p_schedulingdate AS DATE) AS ScheduledDate ,
        CAST(ho.p_lastprocessingdate AS DATE) LastpDate
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
WHERE   a.TypePkString = 8796124741714
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ( ho.p_active <> 0
              OR v.Code <> 'CANCELLED'
              OR CAST(ho.modifiedTS AS DATE) <> @RunDate
              OR CAST(ho.p_cancelationdate AS DATE) <> @RunDate
            );
										
										
						/*Task 10:  Getting Counts RFO Side Flown back Updated Properly:*/
DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate); 
SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.users u ON u.PK = a.userpk
        JOIN Hybris.dbo.EnumerationValues n ON n.PK = u.p_accountstatus
        JOIN RFOperations.Hybris.Autoship at ON at.AutoshipID = a.PK
WHERE   a.TypePkString = 8796124741714
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate
        AND at.Active = 0
        AND CAST(at.EndDate AS DATE) = @RunDate;
															
			/*Task 11:Getting Counts for  RFO Side Not Update Properly*/

DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate); 
SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.users u ON u.PK = a.userpk
        JOIN Hybris.dbo.EnumerationValues n ON n.PK = u.p_accountstatus
        JOIN RFOperations.Hybris.Autoship at ON at.AutoshipID = a.PK
WHERE   a.TypePkString = 8796124741714
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate
        AND ( at.Active = 0
              OR CAST(at.EndDate AS DATE) = @RunDate
            );


						/* Task 12:	 Getting Details for  RFO Side Not Update Properly*/
DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate); 
SELECT  a.code AS Templates ,
        ho.p_active ,
        v.Code AS HybrisTempStatus ,
        ho.modifiedTS ,
        ho.p_cancelationdate ,
        u.p_rfaccountid ,
        n.Code AS UserStatus ,
        at.Active AS RFOTempFlag ,
        at.EndDate AS RfoEndDate
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.users u ON u.PK = a.userpk
        JOIN Hybris.dbo.EnumerationValues n ON n.PK = u.p_accountstatus
        JOIN RFOperations.Hybris.Autoship at ON at.AutoshipID = a.PK
WHERE   a.TypePkString = 8796124741714
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 3
        AND CAST(a.p_schedulingdate AS DATE) < @FirstDayofMonth
        AND CAST(ISNULL(a.p_lastprocessingdate, '1900-01-01') AS DATE) < @FirstDayofMonth
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate
        AND ( at.Active = 0
              OR CAST(at.EndDate AS DATE) = @RunDate
            );
								



		/* pcPerksSoftTerminationCronJob: (PC Template Termination and Account Inactive due to CC Failure Scenarios)*/
		--===========================================================================================================
					
												--PCPERKS TEMPLATE TERMINATION -
												 --==========================

									/*Task 13: GettingCounts for Terminated Templates.*/


									--Eligible Templates Counts

DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate);
 --10 Dayspast and  Beyond will be picked up for Termination
							
SELECT  COUNT(*)
FROM    DataMigration.dbo.TempTermNew a
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = a.statuspk
        JOIN Hybris.dbo.users u ON u.PK = a.userpk
        JOIN Hybris.dbo.EnumerationValues n ON n.PK = u.p_accountstatus
WHERE   a.TypePkString = 8796124708946
        AND a.p_active = 1
        AND a.p_template = 1
        AND a.p_ccfailurecount >= 4
        AND v.Code = 'Pending'
        AND CAST(a.p_schedulingdate AS DATE) < @Date;
														

						          --Hybris Side Counts Terminated Templates
DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate);
 --10 Dayspast and  Beyond will be picked up for Termination
								  
SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.users u ON u.PK = a.userpk
        JOIN Hybris.dbo.EnumerationValues n ON n.PK = u.p_accountstatus
WHERE   a.TypePkString = 8796124708946
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 4
        AND CAST(a.p_schedulingdate AS DATE) < @Date
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate
        AND n.Code = 'SOFTTERMINATEDINVOLUNTARY'
        AND CAST(u.modifiedTS AS DATE) = @RunDate;
														
								/* Task 14: Getting Counts of EligibleTemplates Not Terminated */

                                               
DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate); 						

SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
WHERE   a.TypePkString = 8796124708946
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 4
        AND CAST(a.p_schedulingdate AS DATE) < @Date
        AND ( ho.p_active <> 0
              OR v.Code <> 'CANCELLED'
              OR CAST(ho.modifiedTS AS DATE) <> @RunDate
              OR CAST(ho.p_cancelationdate AS DATE) <> @RunDate
            );


								
								/* Task 15: Getting Details of Eligible Templates Not Terminated */

                                              
DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate); 

SELECT  a.code AS Templates ,
        a.p_ccfailurecount AS OldccFailure ,
        a.p_active AS OldActiveFlag ,
        CAST(a.p_schedulingdate AS DATE) OldScheduled ,
        CAST(a.p_lastprocessingdate AS DATE) OldLastPDate ,
        ho.p_ccfailurecount ,
        ho.p_active ,
        v.Code AS TemplateStatus ,
        CAST(ho.p_schedulingdate AS DATE) AS ScheduledDate ,
        CAST(ho.p_lastprocessingdate AS DATE) LastpDate
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
WHERE   a.TypePkString = 8796124708946
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 4
        AND CAST(a.p_schedulingdate AS DATE) < @Date
        AND ( ho.p_active <> 0
              OR v.Code <> 'CANCELLED'
              OR CAST(ho.modifiedTS AS DATE) <> @RunDate
              OR CAST(ho.p_cancelationdate AS DATE) <> @RunDate
            );

													
						/*Task 16:	 Getting Counts RFO Side Flown back Updated Properly:*/
DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate); 
SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.users u ON u.PK = a.userpk
        JOIN Hybris.dbo.EnumerationValues n ON n.PK = u.p_accountstatus
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON CAST(rf.AccountID AS NVARCHAR) = u.p_rfaccountid
        JOIN RFOperations.Hybris.Autoship at ON at.AutoshipID = a.PK
WHERE   a.TypePkString = 8796124708946
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 4
        AND CAST(a.p_schedulingdate AS DATE) < @Date
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate
        AND n.Code = 'SOFTTERMINATEDINVOLUNTARY'
        AND CAST(u.modifiedTS AS DATE) = @RunDate
        AND CAST(rf.SoftTerminationDate AS DATE) = @RunDate
        AND CAST(rf.ServerModifiedDate AS DATE) > -@RunDate
        AND at.Active = 0
        AND CAST(at.EndDate AS DATE) = @RunDate;
															
			/* Taslk 17: Getting Counts for  RFO Side Not Update Properly*/
DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate); 
SELECT  COUNT(*)
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.users u ON u.PK = a.userpk
        JOIN Hybris.dbo.EnumerationValues n ON n.PK = u.p_accountstatus
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON CAST(rf.AccountID AS NVARCHAR) = u.p_rfaccountid
        JOIN RFOperations.Hybris.Autoship at ON at.AutoshipID = a.PK
WHERE   a.TypePkString = 8796124708946
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 4
        AND CAST(a.p_schedulingdate AS DATE) < @Date
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate
        AND n.Code = 'SOFTTERMINATEDINVOLUNTARY'
        AND CAST(u.modifiedTS AS DATE) = @RunDate
        AND ( CAST(rf.SoftTerminationDate AS DATE) = @RunDate
              OR CAST(rf.ServerModifiedDate AS DATE) >= @RunDate
              OR at.Active = 0
              OR CAST(at.EndDate AS DATE) = @RunDate
            );


						/*Task 18: Getting Details for  RFO Side Not Update Properly*/
DECLARE @RunDate DATE= CAST(GETDATE() AS DATE); 
DECLARE @Date DATE= DATEADD(DAY, -10, @RunDate); 
SELECT  a.code AS Templates ,
        ho.p_active ,
        v.Code AS HybrisTempStatus ,
        ho.modifiedTS ,
        ho.p_cancelationdate ,
        u.p_rfaccountid ,
        n.Code AS UserStatus ,
        u.modifiedTS AS UserModified ,
        rf.SoftTerminationDate ,
        rf.Active AS RFOAccountStatus ,
        rf.ServerModifiedDate ,
        at.Active AS RFOTempFlag ,
        at.EndDate AS RfoEndDate
FROM    Hybris.dbo.orders a -- This is a Back Up Table.
        JOIN Hybris.dbo.orders ho ON a.PK = ho.PK
                                     AND ho.p_template = 1
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = ho.statuspk
        JOIN Hybris.dbo.users u ON u.PK = a.userpk
        JOIN Hybris.dbo.EnumerationValues n ON n.PK = u.p_accountstatus
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON CAST(rf.AccountID AS NVARCHAR) = u.p_rfaccountid
        JOIN RFOperations.Hybris.Autoship at ON at.AutoshipID = a.PK
WHERE   a.TypePkString = 8796124708946
        AND a.p_active = 1
        AND a.p_ccfailurecount >= 4
        AND CAST(a.p_schedulingdate AS DATE) < @Date
        AND ho.p_active = 0
        AND v.Code = 'CANCELLED'
        AND CAST(ho.modifiedTS AS DATE) = @RunDate
        AND CAST(ho.p_cancelationdate AS DATE) = @RunDate
        AND n.Code = 'SOFTTERMINATEDINVOLUNTARY'
        AND CAST(u.modifiedTS AS DATE) = @RunDate
        AND ( CAST(rf.SoftTerminationDate AS DATE) = @RunDate
              OR CAST(rf.ServerModifiedDate AS DATE) >= @RunDate
              OR at.Active = 0
              OR CAST(at.EndDate AS DATE) = @RunDate
            );
								


--********************************************************************************************************************************

		--Step 5: Consultants Renewal 
		/* autoRenewalEmailJob:  Auto Renewal when the consultant has 100 SV order any month in last 3 months  */

		

--++++++++++++++++++++++++++++++++++++++++++
--		Calculation of QV from Commission DB
--+++++++++++++++++++++++++++++++++++++++++++
USE RFOperations 
GO 
SELECT TOP 500
        Cal.SV ,
        Cal.PeriodID ,
        vga.LastRenewalDate ,
        vga.* ,
        at.*
SELECT DISTINCT TOP 10 vga.AccountID INTO #t1 
FROM   RFO_Accounts.vw_GetAccount AS vga
        INNER JOIN RFO_Reference.synVWCalculations AS Cal ON Cal.AccountID = vga.AccountID
        INNER JOIN RFO_Reference.synAccountTitles AS at ON at.AccountID = Cal.AccountID
                                                           AND at.PeriodID = Cal.PeriodID
 WHERE  vga.AccountTypeCode = 1 -- @AccountTypeCode
        AND vga.Active = 1
        AND vga.LastRenewalDate < '2015-2-05'
        AND Cal.PeriodID IN (201511,201512,201601,201602)
              AND cal.SV > 100
			  AND vga.CountryID=40
              --AND at.TitleTypeID = 2
              --AND at.TitleID =2
 ORDER BY vga.LastRenewalDate DESC; 


			/* Task 1: Getting Details for  Renewed Eligible Consultants.*/

DECLARE @Date DATE = CAST(GETDATE() AS DATE);
DECLARE @OrderDate DATE= '2015-10-01';

SELECT  COUNT(*) TotalRenewedConsultants
	--CAST(u.modifiedTS AS DATE) ModifiedTS ,
 --       v.Code AS AccountStatus ,
 --       CAST(u.p_expirationdate AS DATE) ExpirationDate ,
 --       t.QV AS MaxofQV
	--	,t.expdate AS OldExpDate		
FROM    Hybris..users u
        JOIN ( SELECT   a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate ,
                        MAX(QV) QV
               FROM     ( SELECT    u.p_rfaccountid ,
                                    SUM(ISNULL(ho.p_totalqv, 0)) AS QV ,
                                    DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) AS OrderMonth ,
                                    CAST(u.p_expirationdate AS DATE) expdate ,
                                    v.Code AS Accountstatus
                          FROM      Hybris..users u -- This is BackUp Teble.
                                    LEFT  JOIN Hybris..orders ho ON ho.userpk = u.PK
                                    LEFT JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                          WHERE     ISNULL(ho.p_template, 0) = 0
                                    AND u.p_consultantsince IS NOT NULL
                                    AND CAST(u.p_expirationdate AS DATE) = @Date
                                    AND CAST(ho.createdTS AS DATE) > @OrderDate						 --Make Sure the Dates.
                                    AND ho.statuspk IN ( 8796134146139,
                                                         8796134178907,
                                                         8796134113371,
                                                         8796093251675 )
                                    AND ho.PK NOT IN (
                                    SELECT  p_associatedorder
                                    FROM    Hybris..orders
                                    WHERE   ISNULL(ho.p_template, 0) = 0
                                            AND TypePkString = 8796127723602 )
                          GROUP BY  DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) ,
                                    u.p_rfaccountid ,
                                    CAST(u.p_expirationdate AS DATE) ,
                                    v.Code
                        ) a
               GROUP BY a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate
               HAVING   MAX(QV) >= 100
                        AND a.Accountstatus IN ( 'Active' )
             ) t ON u.p_rfaccountid = t.p_rfaccountid
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
WHERE   CAST(u.modifiedTS AS DATE) = @Date
        AND v.Code = 'Active'
        AND u.p_expirationdate = DATEADD(YEAR, 1, t.expdate);

	  
	   /* Task 2: Checking RFO been UPdated as Expected for Renewed Consultants*/
	   
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
DECLARE @OrderDate DATE= '2015-10-01';
SELECT  COUNT(*) TotalRenewedInRFO
FROM    Hybris.dbo.users a --<<<<<<<<<<<<<<<<<<< This is BackUp Table.
        JOIN Hybris.dbo.users u ON a.p_rfaccountid = u.p_rfaccountid
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = u.p_accountstatus
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON CAST(rf.AccountID AS NVARCHAR) = u.p_rfaccountid
WHERE   CAST(u.modifiedTS AS DATE) = @Date
        AND u.p_expirationdate = DATEADD(YEAR, 1, a.p_expirationdate)
        AND v.Code = 'Active'
        AND CAST(u.p_expirationdate AS DATE) = CAST(rf.NextRenewalDate AS DATE)
        AND rf.Active = 1; 


				/* Task 3: Details of RFO NOT been Updated for Renewed Consultants*/

DECLARE @Date DATE = CAST(GETDATE() AS DATE);
DECLARE @OrderDate DATE= '2015-10-01';
SELECT  u.p_rfaccountid ,
        u.p_expirationdate ,
        v.Code AS HybrisUserStatus ,
        rf.NextRenewalDate ,
        rf.Active ,
        rf.ServerModifiedDate
FROM    Hybris.dbo.users a --<<<<<<<<<<<<<<<<<<< This is BackUp Table.
        JOIN Hybris.dbo.users u ON a.p_rfaccountid = u.p_rfaccountid
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = u.p_accountstatus
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON CAST(rf.AccountID AS NVARCHAR) = u.p_rfaccountid
WHERE   CAST(u.modifiedTS AS DATE) = @Date
        AND u.p_expirationdate = DATEADD(YEAR, 1, a.p_expirationdate)
        AND v.Code = 'Active'
        AND ( CAST(u.p_expirationdate AS DATE) <> CAST(rf.NextRenewalDate AS DATE)
              OR rf.Active <> 1
            );

	   	   

	   /* Task 4: Getting Details of NOT  Renewed for Eligible Consultants.*/

DECLARE @Date DATE = CAST(GETDATE() AS DATE) ,
    @OrderDate DATE= '2015-10-01';

SELECT  CAST(u.modifiedTS AS DATE) ModifiedTS ,
        v.Code AS AccountStatus ,
        CAST(u.p_expirationdate AS DATE) ExpirationDate ,
        t.QV AS MaxofQV ,
        t.expdate AS OldExpDate
FROM    Hybris..users u
        JOIN ( SELECT   a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate ,
                        MAX(QV) QV
               FROM     ( SELECT    u.p_rfaccountid ,
                                    SUM(ISNULL(ho.p_totalqv, 0)) AS QV ,
                                    DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) AS OrderMonth ,
                                    CAST(u.p_expirationdate AS DATE) expdate ,
                                    v.Code AS Accountstatus
                          FROM      Hybris..users u -- This is BackUp Teble.
                                    LEFT  JOIN Hybris..orders ho ON ho.userpk = u.PK
                                    LEFT JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                          WHERE     ISNULL(ho.p_template, 0) = 0
                                    AND u.p_consultantsince IS NOT NULL
                                    AND CAST(u.p_expirationdate AS DATE) = @Date
                                    AND CAST(ho.createdTS AS DATE) > @OrderDate						 --Make Sure the Dates.
                                    AND ho.statuspk IN ( 8796134146139,
                                                         8796134178907,
                                                         8796134113371,
                                                         8796093251675 )
                                    AND ho.PK NOT IN (
                                    SELECT  p_associatedorder
                                    FROM    Hybris..orders
                                    WHERE   ISNULL(ho.p_template, 0) = 0
                                            AND TypePkString = 8796127723602 )
                          GROUP BY  DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) ,
                                    u.p_rfaccountid ,
                                    CAST(u.p_expirationdate AS DATE) ,
                                    v.Code
                        ) a
               GROUP BY a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate
               HAVING   MAX(QV) >= 100
                        AND a.Accountstatus IN ( 'Active' )
             ) t ON u.p_rfaccountid = t.p_rfaccountid
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
WHERE   CAST(u.modifiedTS AS DATE) <> @Date
        OR v.Code <> 'Active'
        OR u.p_expirationdate <> DATEADD(YEAR, 1, t.expdate);



	   /* Task 5:  Details for  Renewed NON-Eligible Consultants.*/

DECLARE @Date DATE = CAST(GETDATE() AS DATE) ,
    @OrderDate DATE= '2015-10-01';

SELECT  CAST(u.modifiedTS AS DATE) ModifiedTS ,
        v.Code AS AccountStatus ,
        CAST(u.p_expirationdate AS DATE) ExpirationDate ,
        t.QV AS MaxofQV ,
        t.expdate AS OldExpDate
FROM    Hybris..users u
        JOIN ( SELECT   a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate ,
                        MAX(QV) QV
               FROM     ( SELECT    u.p_rfaccountid ,
                                    SUM(ISNULL(ho.p_totalqv, 0)) AS QV ,
                                    DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) AS OrderMonth ,
                                    CAST(u.p_expirationdate AS DATE) expdate ,
                                    v.Code AS Accountstatus
                          FROM      Hybris..users u --<<<<<<<<<<<<<<<<<<<<<<<<This is BackUp Teble.
                                    LEFT  JOIN Hybris..orders ho ON ho.userpk = u.PK
                                    LEFT JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                          WHERE     ISNULL(ho.p_template, 0) = 0
                                    AND CAST(u.p_expirationdate AS DATE) = @Date
                                    AND CAST(ho.createdTS AS DATE) > @OrderDate	--  <<<<<<<<<<<<<<<<<<<<<<<<<Make Sure the Dates.
                                    AND ho.statuspk IN ( 8796134146139,
                                                         8796134178907,
                                                         8796134113371,
                                                         8796093251675 )
                                    AND ho.PK NOT IN (
                                    SELECT  p_associatedorder
                                    FROM    Hybris..orders
                                    WHERE   ISNULL(ho.p_template, 0) = 0
                                            AND TypePkString = 8796127723602 )
                          GROUP BY  DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) ,
                                    u.p_rfaccountid ,
                                    CAST(u.p_expirationdate AS DATE) ,
                                    v.Code
                        ) a
               GROUP BY a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate
               HAVING   MAX(QV) < 100
                        AND a.Accountstatus IN ( 'Active' )
             ) t ON u.p_rfaccountid = t.p_rfaccountid
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
WHERE   CAST(u.modifiedTS AS DATE) = @Date
        AND v.Code = 'Active'
        AND u.p_expirationdate = DATEADD(YEAR, 1, t.expdate);

--********************************************************************************************************************************
	-- Step 6: Consultants Termination 
		/* terminationNoticeEmailJob:  (Consultant Doesn't have 100 SV order any month in last 3 months,and
										he has NOT Renewed by paying $25 fee even after 30days grace)*/

										

--++++++++++++++++++++++++++++++++++++++++++
--		Calculation of QV from Commission DB
--+++++++++++++++++++++++++++++++++++++++++++
USE RFOperations 
GO 
SELECT TOP 500
        Cal.SV ,
        Cal.PeriodID ,
        vga.LastRenewalDate ,
        vga.* ,
        at.*
SELECT DISTINCT TOP 10 vga.AccountID INTO #t1 
FROM   RFO_Accounts.vw_GetAccount AS vga
        INNER JOIN RFO_Reference.synVWCalculations AS Cal ON Cal.AccountID = vga.AccountID
        INNER JOIN RFO_Reference.synAccountTitles AS at ON at.AccountID = Cal.AccountID
                                                           AND at.PeriodID = Cal.PeriodID
 WHERE  vga.AccountTypeCode = 1 -- @AccountTypeCode
        AND vga.Active = 1
        AND vga.LastRenewalDate < '2015-2-05'
        AND Cal.PeriodID IN (201511,201512,201601,201602)
              AND cal.SV > 100
			  AND vga.CountryID=40
              --AND at.TitleTypeID = 2
              --AND at.TitleID =2
 ORDER BY vga.LastRenewalDate DESC; 



	/* Task 1: Getting Counts/Details for  Soft-Terminated   Consultants.*/

DECLARE @Date DATE = CAST(GETDATE() AS DATE);
DECLARE @OrderDate DATE= '2015-09-01';

SELECT  COUNT(*) TotalRenewedConsultants
	--CAST(u.modifiedTS AS DATE) ModifiedTS ,
 --       v.Code AS AccountStatus ,
 --       CAST(u.p_expirationdate AS DATE) ExpirationDate ,
 --       t.QV AS MaxofQV
	--	,t.expdate AS OldExpDate		
FROM    Hybris..users u
        JOIN ( SELECT   a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate ,
                        MAX(QV) QV
               FROM     ( SELECT    u.p_rfaccountid ,
                                    SUM(ISNULL(ho.p_totalqv, 0)) AS QV ,
                                    DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) AS OrderMonth ,
                                    CAST(u.p_expirationdate AS DATE) expdate ,
                                    v.Code AS Accountstatus
                          FROM      Hybris..users u --<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  BackUp Teble.
                                    LEFT  JOIN Hybris..orders ho ON ho.userpk = u.PK
                                    LEFT JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                          WHERE     ISNULL(ho.p_template, 0) = 0
                                    AND u.p_consultantsince IS NOT NULL
                                    AND CAST(u.p_expirationdate AS DATE) = DATEADD(MONTH,
                                                              -1, @Date)
                                    AND CAST(ho.createdTS AS DATE) > @OrderDate						 --<<<<<<<<<<<<<<<<<<<<<<<<<<Make Sure the Dates.
                                    AND ho.statuspk IN ( 8796134146139,
                                                         8796134178907,
                                                         8796134113371,
                                                         8796093251675 )
                                    AND ho.PK NOT IN (
                                    SELECT  p_associatedorder
                                    FROM    Hybris..orders
                                    WHERE   ISNULL(ho.p_template, 0) = 0
                                            AND TypePkString = 8796127723602 )
                          GROUP BY  DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) ,
                                    u.p_rfaccountid ,
                                    CAST(u.p_expirationdate AS DATE) ,
                                    v.Code
                        ) a
               GROUP BY a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate
               HAVING   MAX(QV) < 100
                        AND a.Accountstatus IN ( 'Active' )
             ) t ON u.p_rfaccountid = t.p_rfaccountid
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
WHERE   CAST(u.modifiedTS AS DATE) = @Date
        AND v.Code = 'SOFTTERMINATEDINVOLUNTARY'
        AND CAST(u.p_expirationdate AS DATE) = t.expdate;

	
	   /* Task 2: Checking RFO been UPdated as Expected for Terminated Consultants*/
	   
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
DECLARE @OrderDate DATE= '2015-10-01';
												--<<<<<<<<<<<<<<<<<<<<<<<<<<Make Sure the Dates.
SELECT  COUNT(*) TotalRenewedInRFO
FROM    Hybris.dbo.users a --<<<<<<<<<<<<<<<<<<< This is BackUp Table.
        JOIN Hybris.dbo.users u ON a.p_rfaccountid = u.p_rfaccountid
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = u.p_accountstatus
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON CAST(rf.AccountID AS NVARCHAR) = u.p_rfaccountid
WHERE   CAST(u.modifiedTS AS DATE) = @Date
        AND u.p_expirationdate = a.p_expirationdate
        AND v.Code = 'SOFTTERMINATEDINVOLUNTARY'
        AND CAST(rf.SoftTerminationDate AS DATE) = CAST(u.modifiedTS AS DATE)
        AND rf.Active = 1; 

		

				/* Task 3: Details of RFO NOT been Updated for Terminated Consultants*/

DECLARE @Date DATE = CAST(GETDATE() AS DATE);
DECLARE @OrderDate DATE= '2015-10-01';
SELECT  u.p_rfaccountid ,
        u.p_expirationdate ,
        v.Code AS HybrisUserStatus ,
        rf.NextRenewalDate ,
        rf.Active ,
        rf.ServerModifiedDate
FROM    Hybris.dbo.users a --<<<<<<<<<<<<<<<<<<< This is BackUp Table.
        JOIN Hybris.dbo.users u ON a.p_rfaccountid = u.p_rfaccountid
        JOIN Hybris.dbo.EnumerationValues v ON v.PK = u.p_accountstatus
        JOIN RFOperations.RFO_Accounts.AccountRF rf ON CAST(rf.AccountID AS NVARCHAR) = u.p_rfaccountid
WHERE   CAST(u.modifiedTS AS DATE) = @Date
        AND u.p_expirationdate = DATEADD(YEAR, 1, a.p_expirationdate)
        AND v.Code = 'SOFTTERMINATEDINVOLUNTARY'
        AND ( CAST(u.p_expirationdate AS DATE) <> CAST(rf.NextRenewalDate AS DATE)
              OR rf.Active <> 1
              OR rf.SoftTerminationDate IS NOT NULL
              OR CAST(rf.ServerModifiedDate AS DATE) <> CAST(u.modifiedTS AS DATE)
            );

	   	   

	   /* Task 4: Getting Details of NOT  Terminated for Eligible Consultants.*/
	   
DECLARE @Date DATE = CAST(GETDATE() AS DATE);
DECLARE @OrderDate DATE= '2015-09-01';

SELECT  COUNT(*) TotalRenewedConsultants
	--CAST(u.modifiedTS AS DATE) ModifiedTS ,
 --       v.Code AS AccountStatus ,
 --       CAST(u.p_expirationdate AS DATE) ExpirationDate ,
 --       t.QV AS MaxofQV
	--	,t.expdate AS OldExpDate		
FROM    Hybris..users u
        JOIN ( SELECT   a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate ,
                        MAX(QV) QV
               FROM     ( SELECT    u.p_rfaccountid ,
                                    SUM(ISNULL(ho.p_totalqv, 0)) AS QV ,
                                    DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) AS OrderMonth ,
                                    CAST(u.p_expirationdate AS DATE) expdate ,
                                    v.Code AS Accountstatus
                          FROM      Hybris..users u --<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  BackUp Teble.
                                    LEFT  JOIN Hybris..orders ho ON ho.userpk = u.PK
                                    LEFT JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                          WHERE     ISNULL(ho.p_template, 0) = 0
                                    AND u.p_consultantsince IS NOT NULL
                                    AND CAST(u.p_expirationdate AS DATE) = DATEADD(MONTH,
                                                              -1, @Date)
                                    AND CAST(ho.createdTS AS DATE) > @OrderDate						 --<<<<<<<<<<<<<<<<<<<<<<<<<<Make Sure the Dates.
                                    AND ho.statuspk IN ( 8796134146139,
                                                         8796134178907,
                                                         8796134113371,
                                                         8796093251675 )
                                    AND ho.PK NOT IN (
                                    SELECT  p_associatedorder
                                    FROM    Hybris..orders
                                    WHERE   ISNULL(ho.p_template, 0) = 0
                                            AND TypePkString = 8796127723602 )
                          GROUP BY  DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) ,
                                    u.p_rfaccountid ,
                                    CAST(u.p_expirationdate AS DATE) ,
                                    v.Code
                        ) a
               GROUP BY a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate
               HAVING   MAX(QV) < 100
                        AND a.Accountstatus IN ( 'Active' )
             ) t ON u.p_rfaccountid = t.p_rfaccountid
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
WHERE   CAST(u.modifiedTS AS DATE) <> @Date
        OR v.Code <> 'SOFTTERMINATEDINVOLUNTARY'
        OR CAST(u.p_expirationdate AS DATE) <> t.expdate;


	   /* Task 5:  Details for  Terminated for  NON-Eligible Consultants.*/

DECLARE @Date DATE = CAST(GETDATE() AS DATE) ,
    @OrderDate DATE= '2015-10-01';

SELECT  CAST(u.modifiedTS AS DATE) ModifiedTS ,
        v.Code AS AccountStatus ,
        CAST(u.p_expirationdate AS DATE) ExpirationDate ,
        t.QV AS MaxofQV ,
        t.expdate AS OldExpDate
FROM    Hybris..users u
        JOIN ( SELECT   a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate ,
                        MAX(QV) QV
               FROM     ( SELECT    u.p_rfaccountid ,
                                    SUM(ISNULL(ho.p_totalqv, 0)) AS QV ,
                                    DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) AS OrderMonth ,
                                    CAST(u.p_expirationdate AS DATE) expdate ,
                                    v.Code AS Accountstatus
                          FROM      Hybris..users u --<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  BackUp Teble.
                                    LEFT  JOIN Hybris..orders ho ON ho.userpk = u.PK
                                    LEFT JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                          WHERE     ISNULL(ho.p_template, 0) = 0
                                    AND u.p_consultantsince IS NOT NULL
                                    AND CAST(u.p_expirationdate AS DATE) = DATEADD(MONTH,
                                                              -1, @Date)
                                    AND CAST(ho.createdTS AS DATE) > @OrderDate						 --<<<<<<<<<<<<<<<<<<<<<<<<<<Make Sure the Dates.
                                    AND ho.statuspk IN ( 8796134146139,
                                                         8796134178907,
                                                         8796134113371,
                                                         8796093251675 )
                                    AND ho.PK NOT IN (
                                    SELECT  p_associatedorder
                                    FROM    Hybris..orders
                                    WHERE   ISNULL(ho.p_template, 0) = 0
                                            AND TypePkString = 8796127723602 )
                          GROUP BY  DATEPART(MONTH,
                                             CAST(ISNULL(ho.createdTS, 1) AS DATE)) ,
                                    u.p_rfaccountid ,
                                    CAST(u.p_expirationdate AS DATE) ,
                                    v.Code
                        ) a
               GROUP BY a.p_rfaccountid ,
                        a.Accountstatus ,
                        a.expdate
               HAVING   MAX(QV) > 100
                        AND a.Accountstatus IN ( 'Active' )
             ) t ON u.p_rfaccountid = t.p_rfaccountid
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
WHERE   CAST(u.modifiedTS AS DATE) = @Date
        AND v.Code = 'SOFTTERMINATEDINVOLUNTARY'
        AND CAST(u.p_expirationdate AS DATE) = t.expdate;

--*******************************************************************************************************************************