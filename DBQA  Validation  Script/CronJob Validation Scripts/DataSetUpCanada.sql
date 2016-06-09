

  /* Getting Counts of Eligible Templates to Run */



DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT  COUNT(*)[Total Templates] ,
        c.InternalCode [Template Types]
FROM    Hybris..orders  b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
WHERE   b.p_template = 1
        AND b.currencypk = 8796125888545
        AND b.TypePkString IN ( 8796124676178, 8796124741714,8796124708946 )
        AND b.p_active = 1
        AND b.p_ccfailurecount < 3
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
        AND CAST(b.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30, @Date)
                                             AND     @Date
GROUP BY c.InternalCode






/* Forwarding UnWanted  templates SchedulingDate to Future */




BEGIN TRAN TRANSACTIONONE; 
DECLARE @Date DATE = CAST(GETDATE() AS DATE);

UPDATE  b
SET     b.p_schedulingdate = DATEADD(MONTH, 1, GETDATE())
FROM    Hybris..orders b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..enumerationvalues v ON v.PK = u.p_accountstatus
                                            AND v.Code = 'ACTIVE'
WHERE   b.p_template = 1
        AND b.currencypk = 8796125888545
        AND b.TypePkString IN ( 8796124676178, 8796124741714, 8796124708946 )
        AND b.p_active = 1
        AND b.p_ccfailurecount < 3
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
        AND CAST(b.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30, @Date)
                                             AND     @Date;

IF @@ERROR <> 0
    BEGIN
        PRINT 'Transaction Rolled Back';
        ROLLBACK TRAN TRANSACTIONONE;
    END;
ELSE
    BEGIN
        PRINT 'Transaction Committed Successfully';
        COMMIT TRAN TRANSACTIONONE; 
    END;






/* Getiing Sample DATA AND Loading to Temp */


IF OBJECT_ID('Tempdb..#TEMP') IS NOT NULL 
DROP TABLE #TEMP

DECLARE @Date DATE = CAST(GETDATE() AS DATE);
SELECT DISTINCT TOP 3000  b.pk INTO #TEMP
FROM   Hybris..orders b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
		JOIN Hybris.dbo.paymentinfos p ON p.OwnerPkString=b.pk AND p.duplicate=1
		JOIN Hybris.dbo.addresses ad ON ad.OwnerPkString=b.pk AND p.duplicate=1 AND ad.p_shippingaddress=1
WHERE   b.p_template = 1
       AND b.currencypk = 8796125888545
        AND b.TypePkString IN ( 8796124676178)
        AND b.p_active = 1
        AND b.p_ccfailurecount < 3
        AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,-5, @Date)
       
UNION 
SELECT DISTINCT TOP 3000   b.pk 
FROM   Hybris..orders b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
		JOIN Hybris.dbo.paymentinfos p ON p.OwnerPkString=b.pk AND p.duplicate=1
		--JOIN Hybris.dbo.addresses ad ON ad.OwnerPkString=b.pk AND p.duplicate=1 AND ad.p_shippingaddress=1
WHERE   b.p_template = 1
       AND b.currencypk = 8796125888545
        AND b.TypePkString IN (8796124741714 )
        AND b.p_active = 1
        AND b.p_ccfailurecount < 3
         AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,-5, @Date)     

UNION 
SELECT DISTINCT TOP 3000  b.pk 
FROM   Hybris..orders b --<<<<<<Get BackUpTable
        JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
        JOIN Hybris..users u ON u.PK = b.userpk
        JOIN Hybris..EnumerationValues v ON v.PK = u.p_accountstatus
                                             AND v.Code = 'ACTIVE'
		JOIN Hybris.dbo.paymentinfos p ON p.OwnerPkString=b.pk AND p.duplicate=1
		JOIN Hybris.dbo.addresses ad ON ad.OwnerPkString=b.pk AND p.duplicate=1 AND ad.p_shippingaddress=1
WHERE   b.p_template = 1
        AND b.currencypk = 8796125888545
        AND b.TypePkString IN (8796124708946)
        AND b.p_active = 1
        AND b.p_ccfailurecount < 3
       AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,-5, @Date)





       
	   /* Modifying SchedulingDate to Selected Temp */
	   
BEGIN TRAN TRANSACTIONTWO

	   UPDATE ho
	   SET ho.p_schedulingdate=DATEADD(DAY,-10,GETDATE())
	   FROM Hybris..orders ho 
	   JOIN #Temp t ON t.pk=ho.PK
	   
	IF @@ERROR<>0
	BEGIN
	PRINT 'Transaction Rolled Back'
	ROLLBACK TRAN TRANSACTIONTWO
	END
    ELSE
	BEGIN
	PRINT 'Transaction Committed Successfully'
	COMMIT TRAN TRANSACTIONTWO 
	END
	  
	  
	   /* Checking Eligible Templates with Types */



       DECLARE @Date DATE = CAST(GETDATE() AS DATE);
       SELECT   c.InternalCode [Templates Types] ,
                COUNT(*) [Total Templates] ,
                CAST(b.p_schedulingdate AS DATE) [ScheduledDate] ,
                cu.isocode AS [Country]
       FROM     Hybris..orders b --<<<<<<Get BackUpTable
                JOIN Hybris..composedtypes c ON b.TypePkString = c.PK
                JOIN Hybris..users u ON u.PK = b.userpk
                JOIN Hybris..enumerationvalues v ON v.PK = u.p_accountstatus
                                                    AND v.Code = 'ACTIVE'
                JOIN Hybris..currencies cu ON cu.PK = b.currencypk
       WHERE    b.p_template = 1
       -- AND b.currencypk = 8796125855777
                AND b.TypePkString IN ( 8796124676178, 8796124741714,
                                        8796124708946 )
                AND b.p_active = 1
                AND b.p_ccfailurecount < 3
                AND CAST(ISNULL(b.p_lastprocessingdate, '1900-01-01') AS DATE) < DATEADD(DAY,
                                                              -5, @Date)
                AND CAST(b.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              @Date)
                                                     AND     @Date
       GROUP BY c.InternalCode ,
                CAST(b.p_schedulingdate AS DATE) ,
                cu.isocode;