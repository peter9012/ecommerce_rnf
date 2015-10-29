
--SELECT * FROM datamigration..dm_log
--WHERE test_area ='824-AutoshipShippingAddress'

--SELECT * FROM datamigration..map_tab
--WHERE [owner]='824-AutoshipShippingAddress' AND [Hybris_Column ]='P_gender'






SELECT * FROM RFO_Reference.Gender
USE rfoperations
set transaction isolation level read uncommitted

declare @HYB_key varchar(100) = 'pk'
declare @RFO_key varchar(100) = 'AutoshipShippingAddressID'
declare @sql_gen_1 nvarchar(max)
declare @cnt int
declare @lt_1 int
declare @temp table(test_area varchar(max), test_type varchar(max), rfo_column varchar(max), hybris_column varchar(max), hyb_key varchar(max), hyb_value varchar(max), rfo_key varchar(max), rfo_value varchar(max))







	----Counts check on Hybris side for US
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

		SELECT   DISTINCT a.AutoshipID
				INTO    #LoadedAutoshipID
		FROM    RFOperations.Hybris.Autoship (NOLOCK) a
				INNER JOIN RFOperations.Hybris.AutoshipItem (NOLOCK) ai ON ai.AutoshipId = a.AutoshipID
				INNER JOIN RFOperations.Hybris.AutoshipPayment (NOLOCK) ap ON ap.AutoshipID = a.AutoshipID
				INNER JOIN RFOperations.Hybris.AutoshipShipment (NOLOCK) ash ON ash.AutoshipID = a.AutoshipID
				INNER JOIN RFOperations.Hybris.AutoshipPaymentAddress (NOLOCK) apa ON apa.AutoShipID = a.AutoshipID
				INNER JOIN RFOperations.Hybris.AutoshipShippingAddress (NOLOCK) asha ON asha.AutoShipID = a.AutoshipID
				INNER JOIN Hybris.dbo.users u ON a.AccountID = u.p_rfaccountid
												 AND u.p_sourcename = 'Hybris-DM'
		WHERE   a.CountryID = 236
				AND a.AutoshipID NOT IN ( SELECT    AutoshipID
										  FROM      #DuplicateAutoship )

										  

		 IF OBJECT_ID('tempdb..#extra') IS NOT NULL
			DROP TABLE #extra;
										  
									SELECT pk  INTO #extra 
									 FROM Hybris..orders ho
									JOIN Hybris.autoship ato ON ho.code=ato.AutoshipNumber
									WHERE ato.CountryID=236
									AND ato.AutoshipID NOT IN (SELECT AutoshipID FROM HYbris.autoshipitem)	   








--Duplicate check on Hybris side for US
select case when count(1)>0 then 'Duplicates Found' else 'No duplicates - Validation Passed' end as [Step-1 Validation]
from (SELECT ownerpkstring 
	  FROM hybris.dbo.addresses 
	  WHERE duplicate=1 AND OwnerPkString IN (SELECT DISTINCT autoshipid FROM Hybris.Autoship WHERE CountryID=236) 
	  AND p_shippingaddress=1 
	  GROUP BY OwnerPkString HAVING COUNT(*)>1)t1



	--				--Counts check on Hybris side for US
		SELECT  hybris_cnt ,
				rfo_cnt ,
				CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
					 WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
					 ELSE 'Count matches - validation passed'
				END Results
		FROM    ( SELECT    COUNT(DISTINCT ad.PK) hybris_cnt
				  FROM      Hybris.dbo.orders ho
							JOIN Hybris.dbo.users u ON u.PK = ho.userpk
													   AND u.p_sourcename = 'Hybris-DM'
							JOIN Hybris.dbo.countries c ON c.PK = u.p_country
														   AND c.isocode = 'US'
							JOIN Hybris.dbo.addresses ad ON ad.OwnerPkString = ho.PK
															AND ho.p_template = 1
															AND ad.duplicate = 1
															AND ad.p_shippingaddress = 1
				  WHERE     ho.PK NOT IN ( SELECT   PK
										   FROM     #extra )
				) t1 , --909344
				( SELECT  COUNT(DISTINCT asa.AutoshipShippingAddressID) rfo_cnt
				  FROM      RFOperations.Hybris.Autoship rat
							JOIN Hybris.dbo.users u ON u.p_rfaccountid = rat.AccountID
													   AND rat.CountryID = 236
							JOIN Hybris.AutoshipShippingAddress asa ON asa.AutoShipID = rat.AutoshipID
							JOIN #LoadedAutoshipID lo ON lo.AutoshipID = rat.AutoshipID
														 AND u.p_sourcename = 'Hybris-DM'
				) t2; --909463



				

				IF OBJECT_ID('tempdb..#missing') IS NOT NULL
				DROP TABLE #missing




                        SELECT  t1.PK ,
                                t2.ReturnBillingAddressID ,
                                CASE WHEN t1.PK IS NULL
                                     THEN 'Missing in Hybris'
                                     WHEN t2.ReturnBillingAddressID IS NULL
                                     THEN 'Missing in RFO'
                                END AS results INTO #missing 
                        FROM    ( SELECT    ad.PK
                                  FROM      Hybris.dbo.orders ho
                                            JOIN Hybris.dbo.users u ON ho.userpk = u.PK
                                                              AND u.p_sourcename = 'Hybris-DM'
                                                              AND ho.TypePkString = 8796127723602
                                            JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                              AND c.isocode = 'US'
                                            JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                            JOIN Hybris.dbo.addresses ad ON ad.OwnerPkString = hpi.PK
                                  WHERE     ad.duplicate = 1
                                            AND hpi.duplicate = 1
                                            AND ho.p_template IS NULL
                                            AND ad.p_billingaddress = 1
                                ) t1
                                JOIN ( SELECT   ba.ReturnBillingAddressID
                                       FROM     RFOperations.Hybris.ReturnOrder ro
                                                JOIN Hybris.dbo.users u ON u.p_rfaccountid = ro.AccountID
                                                              AND u.p_sourcename = 'Hybris-DM'
                                                JOIN Hybris.ReturnBillingAddress ba ON ba.ReturnOrderID = ro.ReturnOrderID
                                                JOIN Hybris..orders ho ON ho.PK = ro.OrderID
                                                              AND ro.CountryID = 236
                                                              AND ho.paymentinfopk IS NOT NULL
                                     ) t2 ON t1.PK = t2.ReturnBillingAddressID
                        WHERE   t1.PK IS NULL
                                OR t2.ReturnBillingAddressID IS NULL; 

								SELECT COUNT(*) FROM #missing
								




delete from datamigration.dbo.dm_log where test_area = '824-AutoshipShippingAddress'
IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
drop table #tempact

select a.autoshipid, a.autoshipnumber, a.accountid, b.pk, c.AutoshipShippingAddressID
into #tempact 
from rfoperations.hybris.autoship a
join hybris.dbo.users b
on a.accountid=b.p_rfaccountid
JOIN #LoadedAutoshipID lo ON lo.AutoshipID=a.AutoshipID
left join rfoperations.Hybris.AutoshipShippingAddress c
on a.autoshipid=c.autoshipid
and a.countryid = 236 and p_sourcename = 'Hybris-DM'
group by a.autoshipid, a.autoshipnumber, a.accountid, b.pk, c.AutoshipShippingAddressID

create clustered index as_cls1 on #tempact (AutoshipShippingAddressID)

select 'Validation of column to column with no transformation in progress' as [Step-1 Validation], getdate() as StartTime

set @cnt = 1
select @lt_1 = count(*) from datamigration.dbo.map_tab   where flag = 'c2c' and rfo_column <> @RFO_key and [owner] = '824-AutoshipShippingAddress'

while @cnt<=@lt_1
begin

select @sql_gen_1= 'SELECT DISTINCT  '''+[owner]+''', '''+flag+''', '''+[RFO_Reference Table]+''' as rfo_column, '''+Hybris_Column+''' as hybris_column, A.'+@HYB_key+' as hyb_key, A.'+Hybris_Column+' as hyb_value, B.'+@RFO_key+' as rfo_key, B.RFO_Col as rfo_value

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
and [owner] = '824-AutoshipShippingAddress')temp where rn = @cnt

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
	and [owner] = '824-AutoshipShippingAddress' 
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

end

update datamigration.dbo.map_tab
set [prev_run_err] = 0
where [owner] = '824-AutoshipShippingAddress' and flag = 'c2c'
and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '824-AutoshipShippingAddress' and test_type = 'c2c')


--Defaults Check
select 'Step-1 Completed, Validation of default columns in progress' as [Step-2 Validation],
getdate() as StartTime

set @cnt = 1
select @lt_1 = count(*) from datamigration.dbo.map_tab  where flag = 'defaults' and [owner] = '824-AutoshipShippingAddress' and [rfo_reference table]<> 'NULL'

while (@cnt<=@lt_1)
begin

	if (select count(*) from datamigration.dbo.map_tab  where flag = 'defaults' and [owner] = '824-AutoshipShippingAddress' and [rfo_reference table] <> 'NULL') >1
	begin
		select @sql_gen_1= 'select distinct '''+[owner]+''' as test_area, '''+flag+''' as test_type, null as rfo_column, '''+Hybris_Column+''' as hybris_column, a.'+@HYB_key+', '+hybris_column+', null as rfo_key, null as rfo_value
		from hybris.dbo.'+hybris_table+' a, #tempact b 
		where a.'+@HYB_key+'=b.'+@RFO_key+'
		and '+hybris_column+' <> '''+[rfo_reference table]+'''' 
		from (select *, row_number() over (order by [owner]) rn
		from datamigration.dbo.map_tab 
		where flag = 'defaults' 
		and [rfo_reference table] <> 'NULL'
		and [owner] = '824-AutoshipShippingAddress')temp where rn = @cnt
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
		and [owner] = '824-AutoshipShippingAddress' 
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

set @cnt=@cnt+1

end

update datamigration.dbo.map_tab
set [prev_run_err] = 0
where [owner] = '824-AutoshipShippingAddress' and flag = 'defaults'
and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '824-AutoshipShippingAddress' and test_type = 'defaults')

select 'Step-2 completed, Validation of transformed columns in progress' as [Step-3 Validation], getdate() as StartTime

set @cnt = 1
select @lt_1 = count(*) from datamigration.dbo.map_tab  where flag = 'manual' and rfo_column <> @RFO_key and [owner] = '824-AutoshipShippingAddress'

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
--and id not in (3,4,64,65) --order not migrated yet
and [owner] = '824-AutoshipShippingAddress')temp where rn = @cnt

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
	and [owner] = '824-AutoshipShippingAddress' 
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

end

update datamigration.dbo.map_tab
set [prev_run_err] = 0
where [owner] = '824-AutoshipShippingAddress' and flag = 'manual'
and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '824-AutoshipShippingAddress' and test_type = 'manual')


select 'VALIDATION COMPLETED' [Status], [total no of columns], [columns passed],[total no of columns]-[columns passed] as [Required Analysis],
getdate() as EndTime
from 
	(select count(cnt) as [columns passed] from (select distinct hybris_column as cnt from datamigration.dbo.map_tab where [owner] = '824-AutoshipShippingAddress' and flag in ('c2c', 'manual', 'defaults')
	except
	select distinct hybris_column from DataMigration..dm_log where test_area = '824-AutoshipShippingAddress')a) tab1,

	(select count(id) as [total no of columns] from  datamigration.dbo.map_tab where [owner] = '824-AutoshipShippingAddress' and flag in ('c2c', 'manual','defaults'))tab2


