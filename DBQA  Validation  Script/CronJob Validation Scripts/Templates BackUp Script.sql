DECLARE @BackupTemplateName NVARCHAR(255) ,
    @BackupItemsName NVARCHAR(255) ,
    @Date DATE;  
 
SELECT  @Date = CAST(GETDATE() AS DATE);
    
SET @BackupTemplateName = CONCAT('RFOperations.dbo.[Templates', '_', @Date,
                                 '_BackUp]'); 


SELECT  @BackupTemplateName;
EXECUTE
('SELECT ho.*   INTO ' + @BackupTemplateName +
'FROM  Hybris.dbo.orders ho
join Hybris..users u on u.pk=ho.userpk
join Hybris..EnumerationValues v on v.pk=u.p_accountstatus AND v.Code=''ACTIVE''
WHERE ho.p_template=1
 AND ho.currencypk=8796125888545
AND ho.p_active = 1           
 AND ho.p_ccfailurecount < 3
 AND ho.TypepkString IN(8796124676178,8796124741714,8796124708946)
			AND CAST(ISNULL(ho.p_lastprocessingdate,''1900-01-01'') AS DATE)<CAST(DATEADD(DAY,-5,GETDATE()) AS DATE)
            AND CAST(ho.p_schedulingdate AS DATE) BETWEEN DATEADD(DAY, -30,
                                                              CAST(GETDATE() AS DATE))
                                                 AND    CAST(GETDATE() AS DATE)
												  AND EXISTS ( SELECT 1
                             FROM   Hybris.dbo.paymentinfos p
                             WHERE  p.OwnerPkString = ho.PK
                                    AND p.duplicate = 1 )
                AND EXISTS ( SELECT 1
                             FROM   Hybris.dbo.addresses ad
                             WHERE  ad.OwnerPkString = ho.PK
                                    AND ad.duplicate = 1
                                    AND ad.p_shippingaddress = 1 ) '); 

EXECUTE ('CREATE CLUSTERED INDEX cls ON ' +@BackupTemplateName+'(PK)');
EXECUTE ('CREATE NONCLUSTERED INDEX cls1 ON ' +@BackupTemplateName+'(code)');


EXECUTE
('SELECT   count(*)[Templates Total],b.InternalCode[Templates Type],CAST(p_schedulingdate AS DATE) ScheduledDate from  ' + @BackupTemplateName +
'a join   Hybris.dbo.composedtypes b on a.TypepkString=b.pk
WHERE     p_template = 1
AND currencypk = 8796125888545
AND p_active = 1 
group By b.InternalCode,CAST(p_schedulingdate AS DATE) '); 
