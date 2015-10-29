-----Autoship PaymentInfos



SELECT * FROM datamigration..dm_log
WHERE test_area='824-AutoshipPaymentInfo'




USE rfoperations
set statistics time on
go

set transaction isolation level read uncommitted

declare @HYB_key varchar(100) = 'pk'
declare @RFO_key varchar(100) = 'autoshippaymentid'
declare @sql_gen_1 nvarchar(max)
declare @sql_gen_2 nvarchar(max)
declare @cnt int
declare @lt_1 int
declare @lt_2 int
declare @temp table(test_area varchar(max), test_type varchar(max), rfo_column varchar(max), hybris_column varchar(max), hyb_key varchar(max), hyb_value varchar(max), rfo_key varchar(max), rfo_value varchar(max))






				--Counts check on Hybris side for US
					IF OBJECT_ID('tempdb..#DuplicateAutoship') IS NOT NULL
						DROP TABLE #DuplicateAutoship;

					SELECT  AutoshipID
					INTO    #DuplicateAutoship  ---Loading Duplicates Autoship into Temp Table.425 records 
					FROM    Hybris.Autoship
					WHERE   AccountID IN (
							SELECT  a.AccountID
							FROM    Hybris.Autoship a
									INNER JOIN RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
							WHERE   ab.AccountTypeID = 1
									AND a.CountryID = 236
									AND a.AutoshipTypeID = 2
									AND a.Active = 1
							GROUP BY a.AccountID
							HAVING  COUNT(*) > 1 )
							AND Active = 1
							AND AutoshipTypeID = 2--total 809
					EXCEPT
					SELECT  MAX(AutoshipID) AutoshipID-- INTO #maxautoship
					FROM    Hybris.Autoship a
							INNER JOIN RFO_Accounts.AccountBase ab ON ab.AccountID = a.AccountID
					WHERE   ab.AccountTypeID = 1
							AND a.CountryID = 236
							AND a.AutoshipTypeID = 2
							AND a.Active = 1
					GROUP BY a.AccountID
					HAVING  COUNT(*) > 1;
					 --total 386



					 IF OBJECT_ID('tempdb..#LoadedAutoshipID') IS NOT NULL
						DROP TABLE #LoadedAutoshipID;

					SELECT    DISTINCT
							a.AutoshipID
					INTO    #LoadedAutoshipID
					FROM    RFOperations.Hybris.Autoship (NOLOCK) a
							--INNER JOIN RFOperations.Hybris.AutoshipItem (NOLOCK) ai ON ai.AutoshipId = a.AutoshipID
							INNER JOIN RFOperations.Hybris.AutoshipPayment (NOLOCK) ap ON ap.AutoshipID = a.AutoshipID
							INNER JOIN RFOperations.Hybris.AutoshipShipment (NOLOCK) ash ON ash.AutoshipID = a.AutoshipID
							INNER JOIN RFOperations.Hybris.AutoshipPaymentAddress (NOLOCK) apa ON apa.AutoShipID = a.AutoshipID
							INNER JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) asha ON asha.AutoShipID = a.AutoshipID
							INNER JOIN Hybris.dbo.users u ON a.AccountID = u.p_rfaccountid
															 AND u.p_sourcename = 'Hybris-DM'
					WHERE   a.CountryID = 236
							AND a.AutoshipID NOT IN ( SELECT    AutoshipID
													  FROM      #DuplicateAutoship );




										  
												IF OBJECT_ID('tempdb..#extra') IS NOT NULL 
												DROP TABLE #extra
												SELECT pk INTO #extra  FROM Hybris..orders ho
												JOIN Hybris.autoship ato ON ho.code=ato.AutoshipNumber
												WHERE ato.CountryID=236
												AND ato.AutoshipID NOT IN (SELECT AutoshipID FROM HYbris.autoshipitem)	




			--Validation of AUTOSHIP Counts, Dups & Columns without transformations

			--Duplicate check on Hybris side for US
			select case when count(1)>0 then 'Duplicates Found' else 'No duplicates - Validation Passed' end as [Step-1 Validation]
			from (select count(a.pk) cnt --, a.ownerpkstring  
			from hybris.dbo.paymentinfos (nolock) a,
			hybris.dbo.users b,
			hybris.dbo.countries c,
			hybris.dbo.orders (nolock) d
			where a.userpk=b.pk
			and b.p_country=c.pk
			and d.pk=a.ownerpkstring
			and c.isocode = 'US'
			and b.p_sourcename = 'Hybris-DM'
			and d.p_template = 1 and a.duplicate = 1 --AS/AS profile
			group by a.ownerpkstring, a.code
			having count(*) > 1)t1

			--Counts check on Hybris side for US
			select Hybris_CNT, RFO_CNT, case when hybris_cnt > rfo_cnt then 'Hybris count more than RFO count'
						when rfo_cnt > hybris_cnt then 'RFO count more than Hybris count' else 'Count matches - validation passed' end Results
			from (select count(distinct d.pk) hybris_cnt 
					from hybris.dbo.orders a,
					hybris.dbo.users b,
					hybris.dbo.countries c,
			        hybris.dbo.paymentinfos d
					where a.userpk=b.pk
					and b.p_country=c.pk
					and a.pk=d.ownerpkstring
					and c.isocode = 'US'
					and a.p_template = 1 and d.duplicate = 1
					and b.p_sourcename = 'Hybris-DM'
					AND d.pk NOT IN (SELECT pk FROM #extra)
					)t1, --1135025
					(select count(distinct autoshippaymentid) rfo_cnt
					 FROM rfoperations.hybris.autoship a,
					hybris.dbo.users b,
					 rfoperations.hybris.autoshippayment c
					,#LoadedAutoshipID lo		
					where a.accountid=b.p_rfaccountid
					and a.autoshipid=c.autoshipid
					and b.p_country=8796100624418
					and p_sourcename = 'Hybris-DM'
					AND a.AutoshipID =lo.AutoshipID
					) t2 --1270461


					----Checking the mismatch back in RFL                       
			SELECT  CASE WHEN COUNT(1) > 0 THEN 'Payments having issue'
			             ELSE 'No Issues - Validation Passed'
			        END AS [Step-1 Validation]
			FROM    ( SELECT  -- COUNT(*) CNT
								OrderPaymentID INTO #test

			          FROM      RodanFieldsLive..OrderPayments(NOLOCK)

			          WHERE     OrderPaymentID IN (
			                    SELECT  c.AutoshipPaymentID
			                    FROM   RFOperations.Hybris.AutoshipPayment c ,                       
			                           	#LoadedAutoshipID lo 
			                    WHERE   c.autoshipID=lo.AutoshipID
			                    EXCEPT
			                    SELECT  d.PK
			                    FROM    Hybris.dbo.orders a ,
			                            Hybris.dbo.users b ,
			                            Hybris.dbo.countries c ,
			                            Hybris.dbo.paymentinfos d
			                    WHERE   a.userpk = b.PK
			                            AND b.p_country = c.PK
			                            AND a.PK = d.OwnerPkString
			                            AND c.isocode = 'US'
			                            AND a.p_template = 1
			                            AND d.duplicate = 1
			                            AND b.p_sourcename = 'Hybris-DM' )
			                    AND OrderID IN ( SELECT code
			                                     FROM   Hybris..orders
			                                     WHERE  p_template = 1 )
			                    AND AccountNumber <> 'HDCm5F9HLZ6JyWpnoVViLw=='
			                    AND ( LTRIM(RTRIM(BillingFirstName)) <> ''
			                          OR LTRIM(RTRIM(BillingLastName)) <> ''
			                        )
			        ) b;


					--DROP TABLE #test
	
					IF OBJECT_ID('tempdb..#missing') IS NOT NULL 
					DROP TABLE #missing


		SELECT t1.OwnerPkString,t2.AutoshipID,CASE WHEN t1.OwnerPkString IS NULL  THEN 'Missing in Hybris'
		WHEN t2.AutoshipID IS NULL THEN 'Missing in RFO' END AS Results INTO #missing
		 from (select distinct d.OwnerPkString
		from hybris.dbo.orders a,
		hybris.dbo.users b,
		hybris.dbo.countries c,
        hybris.dbo.paymentinfos d
		where a.userpk=b.pk
		and b.p_country=c.pk
		and a.pk=d.ownerpkstring
		and c.isocode = 'US'
		and a.p_template = 1 and d.duplicate = 1
		and b.p_sourcename = 'Hybris-DM'
		AND a.pk NOT IN (SELECT PK FROM #extra)
		)t1 FULL OUTER JOIN 
		(select distinct c.AutoshipID
		from rfoperations.hybris.autoship a,
		hybris.dbo.users b,
		rfoperations.hybris.autoshippayment c,
		#LoadedAutoshipID lo 
		where a.accountid=b.p_rfaccountid
		and a.autoshipid=c.autoshipid
		and b.p_country=8796100624418
		and p_sourcename = 'Hybris-DM'
		AND a.AutoshipID =lo.AutoshipID
		) t2 ON t1.OwnerPkString=t2.AutoshipID
		WHERE t1.OwnerPkString IS NULL OR t2.AutoshipID IS NULL 

		SELECT COUNT(*) counts FROM #missing 
		SELECT *FROM #missing 



--------Column2Column Validation that doesn't have transformation - Autoship

delete from datamigration.dbo.dm_log where test_area = '824-AutoshipPaymentInfo'
IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
drop table #tempact

select a.autoshipid, autoshipnumber, a.accountid, b.pk, c.autoshippaymentid, c.Vendorid, d.code
into #tempact 
from rfoperations.hybris.autoship a,
hybris.dbo.users b,
rfoperations.hybris.autoshippayment c,
hybris.dbo.orders d
where a.accountid=b.p_rfaccountid
and a.autoshipid=c.autoshipid
and a.AutoshipID=d.pk AND d.p_template=1
and countryid = 236 and b.p_sourcename = 'Hybris-DM'
AND c.AutoshipID IN (SELECT AutoshipID FROM #LoadedAutoshipID)--Taking Loading only.
AND c.AutoshipPaymentID NOT IN (SELECT OrderPaymentID FROM #test)--Excluding Missing.
--and a.autoshipid not in (8809794175021, 8816660840493) --offshore team updated these values
group by a.autoshipid, autoshipnumber, a.accountid, b.pk, c.autoshippaymentid, c.Vendorid, d.code


create clustered index as_cls1 on #tempact (autoshipid)

select 'Validation of column to column with no transformation in progress' as [Step-2 Validation], getdate() as StartTime
set @cnt = 1
select @lt_1 = count(*) from datamigration.dbo.map_tab where flag = 'c2c' and rfo_column <> @RFO_key and [owner] = '824-AutoshipPaymentInfo'
AND id NOT IN (215,216)--encrypted Months

while @cnt<=@lt_1
begin

select @sql_gen_1 = 'SELECT DISTINCT  '''+[owner]+''', '''+flag+''', '''+[RFO_Reference Table]+''' as rfo_column, '''+Hybris_Column+''' as hybris_column, A.'+@HYB_key+' as hyb_key, A.'+Hybris_Column+' as hyb_value, B.'+@RFO_key+' as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.'+@HYB_key+', '+Hybris_Column+' FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+'
except
SELECT a.'+@RFO_key+', a.'+RFO_Column+' as RFO_Col FROM rfoperations.'+[Schema]+'.'+RFO_Table+' a, #tempact b where a.'+@RFO_key+'=b.'+@RFO_key+') A  

LEFT JOIN

(SELECT a.'+@RFO_key+', a.'+RFO_Column+' as RFO_Col FROM rfoperations.'+[Schema]+'.'+RFO_Table+' a, #tempact b where a.'+@RFO_key+'=b.'+@RFO_key+'
except
SELECT a.'+@HYB_key+', '+Hybris_Column+' FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+') B
ON A.'+@HYB_key+'=B.'+@RFO_key+'
UNION
SELECT DISTINCT '''+[owner]+''', '''+flag+''', '''+[RFO_Reference Table]+''', '''+Hybris_Column+''', A.'+@HYB_key+', A.'+Hybris_Column+', B.'+@RFO_key+',B.RFO_Col

FROM (SELECT a.'+@HYB_key+', '+Hybris_Column+' FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+'
except
SELECT a.'+@RFO_key+', a.'+RFO_Column+' as RFO_Col FROM rfoperations.'+[Schema]+'.'+RFO_Table+' a, #tempact b where a.'+@RFO_key+'=b.'+@RFO_key+') A  

RIGHT JOIN

(SELECT a.'+@RFO_key+', a.'+RFO_Column+' as RFO_Col FROM rfoperations.'+[Schema]+'.'+RFO_Table+' a, #tempact b where a.'+@RFO_key+'=b.'+@RFO_key+'
except
SELECT a.'+@HYB_key+', '+Hybris_Column+' FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+') B
ON A.'+@HYB_key+'=B.'+@RFO_key+''
from (select *, row_number() over (order by [owner]) rn
from datamigration.dbo.map_tab
where flag = 'c2c' 
and rfo_column <> @RFO_key
and [owner] = '824-AutoshipPaymentInfo'
AND id NOT IN (215,216)--encrypted Months
)temp where rn = @cnt 

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
	and [owner] = '824-AutoshipPaymentInfo' 
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
where [owner] = '824-AutoshipPaymentInfo' and flag = 'c2c'
and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '824-AutoshipPaymentInfo' and test_type = 'c2c')


select 'Step-2 Completed, Validation of default columns in progress' as [Step-3 Validation],
getdate() as StartTime

----Defaults Check
set @cnt = 1
select @lt_1 = count(*) from datamigration.dbo.map_tab   where flag = 'defaults' and [owner] = '824-AutoshipPaymentInfo' and [rfo_reference table]= 'NULL'
select @lt_2 = count(*) from datamigration.dbo.map_tab  where flag = 'defaults' and [owner] = '824-AutoshipPaymentInfo' and [rfo_reference table]<> 'NULL'

while (@cnt<=@lt_1 and @cnt<=@lt_2)
begin
	if (select count(*) from datamigration.dbo.map_tab   where flag = 'defaults' and [owner] = '824-AutoshipPaymentInfo' and [rfo_reference table] = 'NULL') >1
	begin
		select @sql_gen_1 = 'use rfoperations
		select distinct '''+[owner]+''' as test_area, '''+flag+''' as test_type, null as rfo_column, '''+Hybris_Column+''' as hybris_column, a.'+@HYB_key+', '+hybris_column+', null as rfo_key, null as rfo_value
		from hybris.dbo.'+hybris_table+' a, #tempact b 
		where a.'+@HYB_key+'=b.'+@RFO_key+'
		and '+hybris_column+' is not null' 
		from (select *, row_number() over (order by [owner]) rn
		from datamigration.dbo.map_tab 
		where flag = 'defaults' 
		and [rfo_reference table] = 'NULL'
		and [owner] = '824-AutoshipPaymentInfo')temp where rn = @cnt
	end

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
		and [owner] = '824-AutoshipPaymentInfo' 
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

	--select * from datamigration.dbo.dm_log where test_type = 'defaults'

	if (select count(*) from datamigration.dbo.map_tab  where flag = 'defaults' and [owner] = '824-AutoshipPaymentInfo' and [rfo_reference table] <> 'NULL') >1
	begin
		select @sql_gen_2 = 'use rfoperations
		select distinct '''+[owner]+''' as test_area, '''+flag+''' as test_type, null as rfo_column, '''+Hybris_Column+''' as hybris_column, a.'+@HYB_key+', '+hybris_column+', null as rfo_key, null as rfo_value
		from hybris.dbo.'+hybris_table+' a, #tempact b 
		where a.'+@HYB_key+'=b.'+@RFO_key+'
		and '+hybris_column+' <> '''+[rfo_reference table]+'''' 
		from (select *, row_number() over (order by [owner]) rn
		from datamigration.dbo.map_tab 
		where flag = 'defaults' 
		and [rfo_reference table] <> 'NULL'
		and [owner] = '824-AutoshipPaymentInfo')temp where rn = @cnt
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
		and [owner] = '824-AutoshipPaymentInfo' 
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
where [owner] = '824-AutoshipPaymentInfo' and flag = 'defaults'
and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '824-AutoshipPaymentInfo' and test_type = 'defaults')



select 'Step-3 completed, Validation of transformed columns in progress' as [Step-4 Validation], getdate() as StartTime

--Transformed Columns Validation --10:16 mins
set @cnt = 1
select @lt_1 = count(*) from datamigration.dbo.map_tab  where flag = 'manual' and rfo_column <> @RFO_key and [owner] = '824-AutoshipPaymentInfo'

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
from (select *, row_number() over (order by [owner]) rn
from datamigration.dbo.map_tab
where flag = 'manual'
and rfo_column <> @RFO_key 
and id not in (69,70) --Data types are image on Hybris end. This value is generated by the system
and id not in (3,4,64,65) --order not migrated yet
and [owner] = '824-AutoshipPaymentInfo')temp where rn = @cnt

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
	and [owner] = '824-AutoshipPaymentInfo' 
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


update datamigration.dbo.map_tab
set [prev_run_err] = 0
where [owner] = '824-AutoshipPaymentInfo' and flag = 'manual'
and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '824-AutoshipPaymentInfo' and test_type = 'manual')

delete from @temp

set @cnt=@cnt+1

end

select 'VALIDATION COMPLETED' [Status], [total no of columns], [columns passed],[total no of columns]-[columns passed] as [Required Analysis],
getdate() as EndTime
from 
	(select count(cnt) as [columns passed] from (select distinct hybris_column as cnt from datamigration.dbo.map_tab where [owner] = '824-AutoshipPaymentInfo' and flag in ('c2c', 'manual', 'defaults')
	except
	select distinct hybris_column from DataMigration..dm_log where test_area = '824-AutoshipPaymentInfo')a) tab1,

	(select count(id) as [total no of columns] from  datamigration.dbo.map_tab where [owner] = '824-AutoshipPaymentInfo' and flag in ('c2c', 'manual','defaults'))tab2

set statistics time off
go




