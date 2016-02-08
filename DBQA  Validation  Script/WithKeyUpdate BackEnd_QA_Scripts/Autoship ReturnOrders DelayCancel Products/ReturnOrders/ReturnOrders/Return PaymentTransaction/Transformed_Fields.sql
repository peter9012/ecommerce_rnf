
 USE RFOperations;
 SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
	
 DECLARE @StartedTime TIME;
 DECLARE @EndTime TIME; 
 SELECT 'Return Payment Transaction Loading Temp Table ' AS EntityName ,
        GETDATE() AS StartedTime;
 SELECT @StartedTime = CAST(GETDATE() AS TIME);
		
		
 DECLARE @modifiedDate DATE= '2016-01-04';
		
 DELETE FROM DataMigration.dbo.dm_log
 WHERE  test_area = '853-ReturnPaymentTransaction'
        AND test_type IN ( 'c2c', 'defaults' );
 IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
    DROP TABLE #tempact;

 SELECT a.ReturnOrderID ,
        a.OrderID ,
        ReturnOrderNumber ,
        a.AccountID ,
        b.PK ,
        c.ReturnPaymentId ,
        c.VendorID ,
        c.AmountToBeAuthorized ,
        c.ProcessOnDate
 INTO   #tempact
 FROM   RFOperations.Hybris.ReturnOrder a
        JOIN RodanFieldsLive.dbo.Orders rfl ON a.ReturnOrderNumber = rfl.OrderID
                                               AND rfl.OrderTypeID = 9 ,
        Hybris.dbo.users b ,
        RFOperations.Hybris.ReturnPayment c ,
        Hybris.dbo.paymentinfos d
 WHERE  a.AccountID = b.p_rfaccountid
        AND a.ReturnOrderID = c.ReturnOrderID
        AND c.ReturnPaymentId = d.PK
        AND a.CountryID = 236
        AND b.p_sourcename = 'Hybris-DM'
        AND a.ReturnOrderNumber NOT IN (
        SELECT  a.ReturnOrderNumber
        FROM    Hybris.ReturnOrder a
                JOIN Hybris.Orders b ON a.ReturnOrderNumber = b.OrderNumber
                                        AND a.CountryID = 236 )
        AND a.ReturnOrderID IN ( SELECT ReturnOrderID
                                 FROM   Hybris.ReturnItem )
 GROUP BY a.ReturnOrderID ,
        a.OrderID ,
        ReturnOrderNumber ,
        a.AccountID ,
        b.PK ,
        c.ReturnPaymentId ,
        c.VendorID ,
        d.code ,
        c.AmountToBeAuthorized ,
        c.ProcessOnDate;

 CREATE CLUSTERED INDEX as_cls1 ON #tempact (ReturnOrderID);

				
 SELECT 'Return Payment Transaction Temp table load is Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

 SELECT @EndTime = CAST(GETDATE() AS TIME);
 SELECT @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return Payment Transaction  Temp Table Loading ' AS Entity; 
		
		


		

 DECLARE @HYB_key VARCHAR(100) = 'P_Order';
 DECLARE @RFO_key VARCHAR(100) = 'ReturnorderId';
 DECLARE @sql_gen_1 NVARCHAR(MAX);
 DECLARE @lt_2 INT;
 DECLARE @sql_gen_2 NVARCHAR(MAX);
 DECLARE @cnt INT;
 DECLARE @lt_1 INT;
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


	

 SELECT 'Return Payment Transaction Transformed Column Validation Started ' AS EntityName ,
        GETDATE() AS StartedTime;
 SELECT @StartedTime = CAST(GETDATE() AS TIME);

		--Transformed Columns Validation --10:16 mins
 
set @cnt = 1
select @lt_1 = count(*) from datamigration.dbo.map_tab  where flag = 'manual' and rfo_column <> @RFO_key and [owner] = '853-ReturnPaymentTransaction'

while @cnt<=@lt_1
begin

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
from   ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'manual'
                            AND Hybris_column <> @HYB_key
                            --AND id <> 327 --orders related
                            --AND id <> 323--keys for both side
                            AND [owner] = '853-ReturnPaymentTransaction'
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
                        AND [owner] = '853-ReturnPaymentTransaction'; 
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
        WHERE   [owner] = '853-ReturnPaymentTransaction'
                AND flag = 'manual'
                AND hybris_column NOT IN (
                SELECT DISTINCT
                        hybris_column
                FROM    DataMigration..dm_log
                WHERE   test_area = '853-ReturnPaymentTransaction'
                        AND test_type = 'manual' );

				
SELECT  'Return Payment Transaction   Transformed  Column Validation Completed' AS EntityName ,
        GETDATE() AS CompletionTime;

SELECT  @EndTime = CAST(GETDATE() AS TIME);
SELECT  @StartedTime AS StartedTime ,
        @EndTime AS CompletionTime ,
        DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
        'Return Payment Transaction  Transformed Column Validation' AS Entity; 
		
		

INSERT  INTO DataMigration.dbo.ExecResult
        SELECT  'Return PaymentTransaction WithKey' AS Entity ,
                'Transformed' AS Types ,
                @StartedTime AS StartedTime ,
                @EndTime AS CompletionTime ,
                DATEDIFF(MINUTE, @StartedTime, @EndTime) AS [Total Time (MM)] ,
                USER_NAME() AS UserName ,
                CAST(GETDATE() AS DATE) AS RunDate;