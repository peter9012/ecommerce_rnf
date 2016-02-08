USE RFOperations;
SET STATISTICS TIME ON;
GO		

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;


IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
    DROP TABLE #tempact;
					

DECLARE @HYB_key VARCHAR(100) = 'Pk ';
DECLARE @RFO_key VARCHAR(100) = 'Returnpaymentid';
DECLARE @sql_gen_1 NVARCHAR(MAX);
DECLARE @sql_gen_2 NVARCHAR(MAX);
DECLARE @cnt INT;
DECLARE @lt_1 INT;
DECLARE @lt_2 INT;
DECLARE @temp TABLE
    (
      test_area VARCHAR(MAX) ,
      test_type VARCHAR(MAX) ,
      rfo_column VARCHAR(MAX) ,
      hybris_column VARCHAR(MAX) ,
      hyb_key VARCHAR(MAX) ,
      hyb_value VARCHAR(MAX) ,
      rfo_key VARCHAR(MAX) ,
      rfo_value VARCHAR(MAX)
    );

DECLARE @ModifiedDate DATE= '2016-01-04';
DECLARE @StartedTime TIME;
DECLARE @EndTime TIME; 

SELECT  'Return PaymentInfos Loading TempTable ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);

IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
    DROP TABLE #tempact;

		
SELECT  ro.ReturnOrderID ,
        ro.AccountID ,
        u.PK ,
        ReturnPaymentId ,
        rp.VendorID ,
        ho.paymentinfopk AS RFOOriginalPK ,
        CAST(rp.ExpYear AS NVARCHAR(25)) ExpYear ,
        CAST(rp.Expmonth AS NVARCHAR(25)) Expmonth
		INTO #tempact
FROM    RFOperations.Hybris.ReturnOrder ro
        JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderNumber = rfl.OrderID
                                               AND rfl.OrderTypeID = 9
        JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
                                   AND ro.CountryID = 236
                                   AND u.p_sourcename = 'Hybris-DM'
        JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID
        JOIN Hybris..orders ho ON ho.pk = ro.ReturnOrderID
                                  AND ho.TypePkString = 8796127723602
        JOIN RodanFieldsLive.dbo.OrderPayments rrp ON rrp.OrderPaymentID = rp.ReturnPaymentId
        JOIN Hybris.dbo.paymentinfos hpi ON hpi.pk = rp.ReturnPaymentId

GROUP BY ro.ReturnOrderID ,
        ro.AccountID ,
        u.PK ,
       ReturnPaymentId  ,
        rp.VendorID ,
        ho.paymentinfopk ,
        CAST(rp.ExpYear AS NVARCHAR(25)) ,
        CAST(rp.Expmonth AS NVARCHAR(25)); 


CREATE CLUSTERED INDEX as_cls1 ON #tempact (ReturnPaymentId);


		
SELECT  'Return  PaymentInfos Tempact table load is Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return PaymentInfos Temp Table Loading ' AS Entity; 
		
		
		
		

SELECT  'Return PaymentInfos Transformed  Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
SELECT  @StartedTime = CAST(GETDATE() AS TIME);




SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'manual'
        AND rfo_column <> @RFO_key
        AND id NOT IN ( 69, 70 )
        AND [owner] = '853-ReturnPaymentInfo';

WHILE @cnt <= @lt_1
    BEGIN

       
select  @sql_gen_1 = 'SELECT DISTINCT  '''+[owner]+''' as test_area, '''+flag+''' as test_type, '''+[RFO_Reference Table]+''' as rfo_column, '''+Hybris_Column+''' as hybris_column, A.'+@HYB_key+' as hyb_key, A.Hyb_Trans_col as hyb_value, B.'+@RFO_key+' as rfo_key, B.RFO_Trans_Col as rfo_value

FROM (SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+'
except
SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+RFO_Table+') A  

LEFT JOIN

(SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+RFO_Table+'
except
SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+') B
ON A.'+@HYB_key+'=B.'+@RFO_key+'
UNION
SELECT DISTINCT  '''+[owner]+''', '''+flag+''', '''+[RFO_Reference Table]+''', '''+Hybris_Column+''', A.'+@HYB_key+', A.Hyb_Trans_col, B.'+@RFO_key+', B.RFO_Trans_Col

FROM (SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+'
except
SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+RFO_Table+') A  

RIGHT JOIN

(SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+RFO_Table+'
except
SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+') B
ON A.'+@HYB_key+'=B.'+@RFO_key+''
from    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'manual'
                            AND rfo_column <> @RFO_key
                            AND id NOT IN ( 69, 70 ) --Data types are image on Hybris end. This value is generated by the system
--and id not in (3,4,64,65) --order not migrated yet
--AND id NOT IN (414,422)--validates from key updates scripts
                            AND [owner] = '853-ReturnPaymentInfo'
                ) temp
        WHERE   rn = @cnt;

        PRINT @sql_gen_1;
        INSERT  INTO @temp
                ( test_area ,
                  test_type ,
                  rfo_column ,
                  hybris_column ,
                  hyb_key ,
                  hyb_value ,
                  rfo_key ,
                  rfo_value
                )
                EXEC sp_executesql @sql_gen_1;


        IF ( SELECT COUNT(*)
             FROM   @temp
           ) > 1
            BEGIN
                DECLARE @err_cnt INT;
                SELECT  @err_cnt = CASE WHEN hyb_cnt = 0 THEN rfo_cnt
                                        ELSE hyb_cnt
                                   END
                FROM    ( SELECT    COUNT(DISTINCT hyb_key) hyb_cnt ,
                                    COUNT(DISTINCT rfo_key) rfo_cnt
                          FROM      @temp
                        ) t1;

                UPDATE  a
                SET     [prev_run_err] = @err_cnt
                FROM    DataMigration.dbo.map_tab a ,
                        @temp b
                WHERE   a.hybris_column = b.hybris_column
                        AND [owner] = '853-ReturnPaymentInfo'; 
            END;	
        INSERT  INTO DataMigration..dm_log
                SELECT TOP 5
                        test_area ,
                        test_type ,
                        rfo_column ,
                        hybris_column ,
                        hyb_key ,
                        hyb_value ,
                        rfo_key ,
                        rfo_value
                FROM    @temp
                WHERE   ( ( COALESCE(hyb_key, '~') = COALESCE(rfo_key, '~') )
                          AND ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                        )
                UNION
                SELECT TOP 5
                        test_area ,
                        test_type ,
                        rfo_column ,
                        hybris_column ,
                        hyb_key ,
                        hyb_value ,
                        rfo_key ,
                        rfo_value
                FROM    @temp
                WHERE   ( ( COALESCE(hyb_key, '~') <> COALESCE(rfo_key, '~') )
                          OR ( COALESCE(hyb_value, '~') <> COALESCE(rfo_value,
                                                              '~') )
                        );


        DELETE  FROM @temp;

        SET @cnt = @cnt + 1;

    END;


UPDATE  DataMigration.dbo.map_tab
SET     [prev_run_err] = 0
WHERE   [owner] = '853-ReturnPaymentInfo'
        AND flag = 'c2c'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '853-ReturnPaymentInfo'
                AND test_type = 'manual' );

	
						
SELECT  'Return PaymentInfos   Transformed  Column Validation Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return PaymentInfos Transformed Column Validation' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Return PaymentInfos WithKey' AS Entity ,
                'Transformed' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;