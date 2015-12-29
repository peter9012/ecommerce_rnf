
	

USE RFOperations;
SET STATISTICS TIME ON;
GO

				

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;


IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
    DROP TABLE #tempact;
					
IF OBJECT_ID('tempdb..#MissingCount') IS NOT NULL
    DROP TABLE #MissingCount;

DECLARE @HYB_key VARCHAR(100) = 'Code ';
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

	/**Validation of AUTOSHIP Counts, Dups & Columns without transformations  ***/

		--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    COUNT(*) DupCount
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris..users u ON u.PK = ho.userpk
                                            AND u.p_sourcename = 'Hybris-DM'
                                            AND u.p_country = 8796100624418
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
          WHERE     hpi.duplicate = 1
                    AND ho.p_template IS NULL
                    AND ho.TypePkString = 8796127723602
          GROUP BY  hpi.OwnerPkString ,
                    hpi.code
          HAVING    COUNT(*) > 1
        ) t3;

			--Counts check on Hybris side for US
SELECT  hybris_cnt ,
        rfo_cnt ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(hpi.code) hybris_cnt
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris..users u ON u.PK = ho.userpk
                                            AND u.p_sourcename = 'Hybris-DM'
                                            AND u.p_country = 8796100624418
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
          WHERE     hpi.duplicate = 1
                    AND ISNULL(ho.p_template,0)=0 
                    AND ho.TypePkString = 8796127723602
        ) t1 , --119320
        ( SELECT  COUNT(rp.ReturnPaymentId) rfo_cnt
          FROM      RFOperations.Hybris.ReturnOrder ro
					JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderID = rfl.orderID
                                                             AND rfl.orderTypeID = 9
                    JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
                                               AND ro.CountryID = 236
                                               AND u.p_sourcename = 'Hybris-DM'
                                               AND ro.ReturnStatusID = 5
                    JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID
                    JOIN Hybris..orders ho ON ho.code = ro.ReturnOrderID AND ho.TypePkString=8796127723602 --AND ho.p_template IS NULL 
					--JOIN RodanFieldsLive.dbo.OrderPayments rrp ON rrp.OrderPaymentID=rp.ReturnPaymentId
	    ) t2;
--120368
  
	IF OBJECT_ID('tempdb..#missingCount') IS NOT NULL
	DROP TABLE #missingCount 

SELECT  t1.code ,t2.ReturnPaymentId ,
        CASE WHEN t1.code IS NULL THEN 'Missing In Hybris'
             WHEN t2.ReturnPaymentId IS NULL THEN 'Missing in RFO'
        END Results
INTO    #MissingCount
FROM    ( SELECT    hpi.code
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris..users u ON u.PK = ho.userpk
                                            AND u.p_sourcename = 'Hybris-DM'
                                            AND u.p_country = 8796100624418
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
          WHERE     hpi.duplicate = 1
                    AND ho.p_template IS NULL
                    AND ho.TypePkString = 8796127723602
        ) t1
        FULL OUTER JOIN ( SELECT    CAST(rp.ReturnPaymentId AS NVARCHAR) ReturnPaymentId
                          FROM      RFOperations.Hybris.ReturnOrder ro
									 JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderID = rfl.orderID
                                                             AND rfl.orderTypeID = 9
                                    JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
                                                              AND ro.CountryID = 236
                                                              AND u.p_sourcename = 'Hybris-DM'
                                    JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID
                                    JOIN Hybris..orders ho ON ho.code = ro.ReturnOrderID AND ho.TypePkString = 8796127723602
									JOIN RodanFieldsLive.dbo.OrderPayments rrp ON rrp.OrderPaymentID=rp.ReturnPaymentId
                        ) t2 ON t1.code  = t2.ReturnPaymentId
						WHERE t1.code IS NULL OR t2.ReturnPaymentId IS NULL 
						
DECLARE @keyCount INT;						
SELECT  @keyCount = COUNT(*)
FROM    #MissingCount;
							
UPDATE  DataMigration..map_tab
SET     prev_run_err = @keyCount
WHERE   [owner] = '853-ReturnPaymentInfo'
        AND [RFO_Column ] = 'Returnpaymentid'
        AND [Hybris_Column ] = 'Code';
SELECT TOP 10
        *
FROM    #MissingCount
WHERE   Results = 'Missing in Hybris'
UNION ALL
SELECT TOP 10
        *
FROM    #MissingCount
WHERE   Results = 'Missing in RFO';
    
	

				--Column2Column Validation that doesn't have transformation - Autoship





SELECT  ro.ReturnOrderID ,
        ro.AccountID ,
        u.PK ,
        CAST(rp.ReturnPaymentId AS NVARCHAR(255)) ReturnPaymentID ,
        rp.VendorID ,       
        hro.paymentinfopk AS RFOOriginalPK ,
        CAST(rp.ExpYear AS NVARCHAR(25)) ExpYear ,
        CAST(rp.Expmonth AS NVARCHAR(25)) Expmonth
INTO    #tempact
FROM    RFOperations.Hybris.ReturnOrder ro
		JOIN RodanFieldsLive.dbo.Orders rfl ON ro.ReturnOrderID = rfl.orderID
                                                             AND rfl.orderTypeID = 9
        JOIN Hybris.dbo.users u ON ro.AccountID = u.p_rfaccountid
                                   AND CountryID = 236
                                   AND p_sourcename = 'Hybris-DM'
        JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID        
        JOIN Hybris.dbo.orders hro ON hro.code = ro.ReturnOrderID
                                      AND hro.TypePkString = 8796127723602       
       -- JOIN RFOperations.Hybris.ReturnItem rit ON rit.ReturnOrderID = ro.ReturnOrderID
WHERE   ro.ReturnOrderNumber NOT IN (
        SELECT  a.ReturnOrderNumber
        FROM    Hybris.ReturnOrder a
                JOIN Hybris.Orders b ON a.ReturnOrderNumber = b.OrderNumber
                                        AND a.CountryID = 236 )
GROUP BY ro.ReturnOrderID ,
        ro.AccountID ,
        u.PK ,
        rp.ReturnPaymentId ,
        rp.VendorID ,       
        hro.paymentinfopk ,
        rp.ExpYear ,
        rp.Expmonth

		
		


create clustered index as_cls1 on #tempact (returnorderid)

select 'Validation of column to column with no transformation in progress' as [Step-2 Validation], getdate() as StartTime

set @cnt = 1
select @lt_1 = count(*)  FROM datamigration.dbo.map_tab  where flag = 'c2c' and rfo_column <> @RFO_key and [owner] = '853-ReturnPaymentInfo'

while @cnt<=@lt_1
begin

select  @sql_gen_1 = 'SELECT DISTINCT  '''+[owner]+''' as test_area, '''+flag+''' as test_type, '''+[RFO_Reference Table]+''' as rfo_column, '''+Hybris_Column+''' as hybris_column, A.'+@HYB_key+' as hyb_key, A.Hyb_Trans_col as hyb_value, B.'+@RFO_key+' as rfo_key, B.RFO_Trans_Col as rfo_value

FROM (SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+'
except
SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM  #tempact a) A  

LEFT JOIN

(SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM #tempact a
except
SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+') B
ON A.'+@HYB_key+'=B.'+@RFO_key+'
UNION
SELECT DISTINCT  '''+[owner]+''', '''+flag+''', '''+[RFO_Reference Table]+''', '''+Hybris_Column+''', A.'+@HYB_key+', A.Hyb_Trans_col, B.'+@RFO_key+', B.RFO_Trans_Col

FROM (SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+'
except
SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM #tempact a) A  

RIGHT JOIN

(SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM #tempact a
except
SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+') B
ON A.'+@HYB_key+'=B.'+@RFO_key+''
from (select *, row_number() over (order by [owner]) rn
from datamigration.dbo.map_tab
where flag = 'c2c'
and rfo_column <> @RFO_key 
--and id not in (69,70) --Data types are image on Hybris end. This value is generated by the system
--and id not in (3,4,64,65) --order not migrated yet
--AND id NOT IN (414,422)--validates from key updates scripts
and [owner] = '853-ReturnPaymentInfo')temp where rn = @cnt

print @sql_gen_1
insert into @temp (test_area, test_type, rfo_column, hybris_column, hyb_key, hyb_value, rfo_key, rfo_value)
EXEC sp_executesql @sql_gen_1


if(select count(*) from @temp) >1
begin
	declare @err_cnt int
	select @err_cnt = case when hyb_cnt = 0 then rfo_cnt else hyb_cnt end from (select count(distinct hyb_key) hyb_cnt, count(distinct rfo_key) rfo_cnt from @temp)t1

	update a
	set [prev_run_err] = @err_cnt
	from datamigration.dbo.map_tab a,
	@temp b
	where a.hybris_column=b.hybris_column
	and [owner] = '853-ReturnPaymentInfo' 
end	
insert into datamigration..dm_log
select top 5 test_area, test_type, rfo_column, hybris_column, hyb_key, hyb_value, rfo_key, rfo_value
from @temp
where ((coalesce(hyb_key, '~') = coalesce(rfo_key,'~'))
and (coalesce(hyb_value,'~') <> coalesce(rfo_value,'~')))
union 
select top 5 test_area, test_type, rfo_column, hybris_column, hyb_key, hyb_value, rfo_key, rfo_value 
from @temp
where ((coalesce(hyb_key, '~') <> coalesce(rfo_key,'~'))
or (coalesce(hyb_value,'~') <> coalesce(rfo_value,'~')))


delete from @temp

set @cnt=@cnt+1

END


UPDATE  DataMigration.dbo.map_tab
SET     [prev_run_err] = 0
WHERE   [owner] = '853-ReturnPaymentInfo'
        AND flag = 'c2c'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '853-ReturnPaymentInfo'
                AND test_type = 'c2c' )


select 'Step-2 Completed, Validation of default columns in progress' as [Step-3 Validation],
getdate() as StartTime

----Defaults Check
set @cnt = 1
select @lt_2 =  COUNT(*) from datamigration.dbo.map_tab  where flag = 'defaults' and [owner] = '853-ReturnPaymentInfo' and [rfo_reference table]<> 'NULL'

while ( @cnt<=@lt_2)
begin
	

	--select * from datamigration.dbo.dm_log where test_type = 'defaults'

	if (select count(*) from datamigration.dbo.map_tab  where flag = 'defaults' and [owner] = '853-ReturnPaymentInfo' and [rfo_reference table] <> 'NULL') >1
	begin
		select @sql_gen_2 = 'use rfoperations
		select distinct '''+[owner]+''' as test_area, '''+flag+''' as test_type, null as rfo_column, '''+Hybris_Column+''' as hybris_column, a.'+@HYB_key+', '+hybris_column+', null as rfo_key, null as rfo_value
		from hybris.dbo.'+hybris_table+' a, #tempact b 
		where a.'+@HYB_key+' =b.'+@RFO_key+'
		and  cast('+hybris_column+' as nvarchar) <> '''+[rfo_reference table]+'''' 
		from (select *, row_number() over (order by [owner]) rn
		from datamigration.dbo.map_tab 
		where flag = 'defaults' 
		and [rfo_reference table] <> 'NULL'
		and [owner] = '853-ReturnPaymentInfo')temp where rn = @cnt
	end

	print @sql_gen_2
    insert into @temp (test_area, test_type, rfo_column, hybris_column, hyb_key, hyb_value, rfo_key, rfo_value)
	EXEC sp_executesql @sql_gen_2

	if(select count(*) from @temp) >1
	begin
		--declare @err_cnt int
		select @err_cnt = case when hyb_cnt = 0 then rfo_cnt else hyb_cnt end from (select count(distinct hyb_key) hyb_cnt, count(distinct rfo_key) rfo_cnt from @temp )t1

		update a
		set [prev_run_err] = @err_cnt
		from datamigration.dbo.map_tab a,
		@temp b
		where a.hybris_column=b.hybris_column
		and [owner] = '853-ReturnPaymentInfo' 
	end	

	insert into datamigration.dbo.dm_log
	select top 5 test_area, test_type, rfo_column, hybris_column, hyb_key, hyb_value, rfo_key, rfo_value 
	from @temp
	where ((coalesce(hyb_key, '~') = coalesce(rfo_key,'~'))
	and (coalesce(hyb_value,'~') <> coalesce(rfo_value,'~')))
	union 
	select top 5 test_area, test_type, rfo_column, hybris_column, hyb_key, hyb_value, rfo_key, rfo_value 
	from @temp
	where ((coalesce(hyb_key, '~') <> coalesce(rfo_key,'~'))
	or (coalesce(hyb_value,'~') <> coalesce(rfo_value,'~')))

delete from @temp

set @cnt=@cnt+1

end

update datamigration.dbo.map_tab
set [prev_run_err] = 0
where [owner] = '853-ReturnPaymentInfo' and flag = 'defaults'
and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '853-ReturnPaymentInfo' and test_type = 'defaults')



select 'Step-3 completed, Validation of transformed columns in progress' as [Step-4 Validation], getdate() as StartTime


set @cnt = 1
select @lt_1 = count(*)  FROM datamigration.dbo.map_tab  where flag = 'manual' and rfo_column <> @RFO_key and [owner] = '853-ReturnPaymentInfo'

while @cnt<=@lt_1
begin

select  @sql_gen_1 = 'SELECT DISTINCT  '''+[owner]+''' as test_area, '''+flag+''' as test_type, '''+[RFO_Reference Table]+''' as rfo_column, '''+Hybris_Column+''' as hybris_column, A.'+@HYB_key+' as hyb_key, A.Hyb_Trans_col as hyb_value, B.'+@RFO_key+' as rfo_key, B.RFO_Trans_Col as rfo_value

FROM (SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+'
except
SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+temp.RFO_Table+') A  

LEFT JOIN

(SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+temp.RFO_Table+' 
except
SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+') B
ON A.'+@HYB_key+'=B.'+@RFO_key+'
UNION
SELECT DISTINCT  '''+[owner]+''', '''+flag+''', '''+[RFO_Reference Table]+''', '''+Hybris_Column+''', A.'+@HYB_key+', A.Hyb_Trans_col, B.'+@RFO_key+', B.RFO_Trans_Col

FROM (SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+'
except
SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+temp.RFO_Table+' ) A  

RIGHT JOIN

(SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+temp.RFO_Table+'
except
SELECT a.'+@HYB_key+', '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+') B
ON A.'+@HYB_key+'=B.'+@RFO_key+''
from (select *, row_number() over (order by [owner]) rn
from datamigration.dbo.map_tab
where flag = 'manual'
and rfo_column <> @RFO_key 
--and id not in (69,70) --Data types are image on Hybris end. This value is generated by the system
--and id not in (3,4,64,65) --order not migrated yet
--AND id NOT IN (414,422)--validates from key updates scripts
and [owner] = '853-ReturnPaymentInfo')temp where rn = @cnt

print @sql_gen_1
insert into @temp (test_area, test_type, rfo_column, hybris_column, hyb_key, hyb_value, rfo_key, rfo_value)
EXEC sp_executesql @sql_gen_1


if(select count(*) from @temp) >1
begin
	--declare @err_cnt int
	select @err_cnt = case when hyb_cnt = 0 then rfo_cnt else hyb_cnt end from (select count(distinct hyb_key) hyb_cnt, count(distinct rfo_key) rfo_cnt from @temp)t1

	update a
	set [prev_run_err] = @err_cnt
	from datamigration.dbo.map_tab a,
	@temp b
	where a.hybris_column=b.hybris_column
	and [owner] = '853-ReturnPaymentInfo' 
end	
insert into datamigration..dm_log
select top 5 test_area, test_type, rfo_column, hybris_column, hyb_key, hyb_value, rfo_key, rfo_value
from @temp
where ((coalesce(hyb_key, '~') = coalesce(rfo_key,'~'))
and (coalesce(hyb_value,'~') <> coalesce(rfo_value,'~')))
union 
select top 5 test_area, test_type, rfo_column, hybris_column, hyb_key, hyb_value, rfo_key, rfo_value 
from @temp
where ((coalesce(hyb_key, '~') <> coalesce(rfo_key,'~'))
or (coalesce(hyb_value,'~') <> coalesce(rfo_value,'~')))


delete from @temp

set @cnt=@cnt+1

END


UPDATE  DataMigration.dbo.map_tab
SET     [prev_run_err] = 0
WHERE   [owner] = '853-ReturnPaymentInfo'
        AND flag = 'c2c'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '853-ReturnPaymentInfo'
                AND test_type = 'manual' )



select 'VALIDATION COMPLETED' [Status], [total no of columns], [columns passed],[total no of columns]-[columns passed] as [Required Analysis],
getdate() as EndTime
from 
	(select count(cnt) as [columns passed] from (select distinct hybris_column as cnt from datamigration.dbo.map_tab where [owner] = '853-ReturnPaymentInfo' and flag in ('c2c', 'defaults','manual')
	except
	select distinct hybris_column from DataMigration..dm_log where test_area = '853-ReturnPaymentInfo')a) tab1,

	(select count(id) as [total no of columns] from  datamigration.dbo.map_tab where [owner] = '853-ReturnPaymentInfo' and flag in ('c2c','defaults','manual'))tab2

set statistics time off
go



