




USE Hybris 
GO 


DECLARE @Pk BIGINT ,
    @Fail NVARCHAR(225)= 'PAY_FAIL_FIRST'
	,@OrderType NVARCHAR(225)='CONSULTANT AUTOSHIP TEMPLATE'  --PC AUTOSHIP TEMPLATE, CONSULTANT AUTOSHIP TEMPLATE,PULSE AUTOSHIP
	,@AccountType NVARCHAR(225)='CONSULTANT'  --CONSULTANT,PREFERRED,RETAIL
	
	DECLARE @Email TABLE (p_uid NVARCHAR(225))
INSERT INTO @Email
    
 
VALUES 
('autoconswpwcrp20170315104221@mailinator.com')



--SELECT *  FROM @Email


/*

SELECT  u.p_uid
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.enumerationvalues s ON s.pk = u.p_accountstatus
                                               AND s.Code = 'Active'
        JOIN Hybris.dbo.enumerationvalues t ON t.pk = u.p_type
                                               AND t.Code = @AccountType
WHERE   u.p_name LIKE 'auto%'
        AND EXISTS ( SELECT 1
                     FROM   Hybris.dbo.carts c
                     WHERE  c.p_user = u.pk
                            AND c.p_ordertype = @OrderType )




*/

	
SET @Pk = ( SELECT  v.pk
            FROM    Hybris.dbo.enumerationvalues v
                    JOIN Hybris.dbo.composedtypes t ON t.pk = v.TypePkString
                                                       AND InternalCode = 'CartCronJobStatus'
            WHERE   Code = @Fail
          )
SELECT @Pk



--UPDATE  c
--SET     modifiedTS = GETDATE() ,
--        p_cartcronjobstatus = @Pk
	   SELECT u.p_uid, c.*
FROM    Hybris.dbo.users u
        JOIN Hybris.dbo.carts c ON c.p_user = u.PK
WHERE   u.p_uid IN ( SELECT p_uid
                     FROM   @Email )  
        AND ISNUMERIC(c.p_code) = 1 AND c.p_ordertype=@OrderType
	

		
