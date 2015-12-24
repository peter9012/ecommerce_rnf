			
			
			
			USE rfoperations
			set statistics time on
			go

			set transaction isolation level read uncommitted

			declare @HYB_key varchar(100) = 'pk'
			declare @RFO_key varchar(100) = 'ReturnItemId'
			declare @sql_gen_1 nvarchar(max)
			declare @sql_gen_2 nvarchar(max)
			declare @cnt int
			declare @lt_1 int
			declare @lt_2 int
			declare @temp table(test_area varchar(max), test_type varchar(max), rfo_column varchar(max), hybris_column varchar(max), hyb_key varchar(max), hyb_value varchar(max), rfo_key varchar(max), rfo_value varchar(max))


							--SELECT d.* INTO Hybris..OrderEntries_bkp
							--from hybris.dbo.orders a,
							--hybris.dbo.users b,
							--hybris.dbo.countries c,
							--hybris.dbo.orderentries d
							--where a.userpk=b.pk--and b.p_country=c.pk
							--and a.pk=d.orderpk
							--and c.isocode = 'US'
							--and p_sourcename = 'Hybris-DM'
							--and a.p_template is NULL AND a.TypePkString=8796127723602

			--Duplicate check on Hybris side for US
			select case when count(1)>0 then 'Duplicates Found' else 'No duplicates - Validation Passed' end as [Step-1 Validation]
			from (select count(*) cnt, e.p_returnrequest, e.p_orderentry
			from hybris.dbo.orders a,
			hybris.dbo.users b,
			hybris.dbo.countries c,
			hybris.dbo.returnrequest d,
			hybris.dbo.returnentry e
			where a.userpk=b.pk
			and b.p_country=c.pk
			and a.pk=d.p_returnorder
			AND d.pk=e.p_returnrequest
			and c.isocode = 'US'
			and p_sourcename = 'Hybris-DM'
			and a.p_template is null AND a.TypePkString=8796127723602
			group by e.p_returnrequest, e.p_orderentry
			having count(*) > 1)t1

			--Counts check between ReturnRequest & ReturnEntry
			SELECT  CASE WHEN COUNT(1) > 0
						 THEN 'Count Comparison between ReturnRequest & ReturnEntry - Failed!'
						 ELSE 'Count Comparison between ReturnRequest & ReturnEntry - Passed'
					END Results
			FROM    ( SELECT    d.PK
					  FROM      Hybris.dbo.orders a ,
								Hybris.dbo.users b ,
								Hybris.dbo.countries c ,
								Hybris.dbo.returnrequest d
					  WHERE     a.userpk = b.PK
								AND b.p_country = c.PK
								AND c.isocode = 'US'
								AND a.PK = d.p_returnorder
								AND a.p_template IS NULL
								AND a.TypePkString = 8796127723602 --Returns
								AND p_sourcename = 'Hybris-DM'
					  EXCEPT
					  SELECT    p_returnrequest
					  FROM      Hybris..returnentry
					  WHERE     p_returnrequest IN (
								SELECT  d.PK
								FROM    Hybris.dbo.orders a ,
										Hybris.dbo.users b ,
										Hybris.dbo.countries c ,
										Hybris.dbo.returnrequest d
								WHERE   a.userpk = b.PK
										AND b.p_country = c.PK
										AND c.isocode = 'US'
										AND a.PK = d.p_returnorder
										AND a.p_template IS NULL
										AND a.TypePkString = 8796127723602 --Returns
										AND p_sourcename = 'Hybris-DM' )
					) t1

			--Counts check on Hybris side for OrderEntries US
			select Hybris_CNT, RFO_CNT, case when hybris_cnt > rfo_cnt then 'Hybris count more than RFO count'
						when rfo_cnt > hybris_cnt then 'RFO count more than Hybris count' else 'Count matches - validation passed' end Results
			from (select COUNT(d.pk) hybris_cnt 
					from hybris.dbo.orders a,
					hybris.dbo.users b,
					hybris.dbo.countries c,
					hybris.dbo.orderentries d
					where a.userpk=b.pk
					and b.p_country=c.pk
					and a.pk=d.orderpk
					and c.isocode = 'US'
					and p_sourcename = 'Hybris-DM'
					and a.p_template is NULL AND a.TypePkString=8796127723602)t1, --106994

					(select COUNT(*) RFO_CNT from (select MAX(returnitemid) id, d.Returnorderid, Productid, orderitemid
					from hybris.ReturnOrder a,
					hybris.dbo.orders b,
					hybris.dbo.users c,
					hybris.Returnitem d,
					Hybris.dbo.products e
					where a.orderid=b.pk
					and b.userpk=c.pk 
					AND a.ReturnOrderID=d.returnorderid
					AND d.ProductID=e.p_rflegacyproductid
					AND p_catalog = '8796093088344'
					and p_catalogversion = '8796093153881'
					AND a.returnordernumber NOT IN (SELECT a.returnordernumber FROM hybris.ReturnOrder a JOIN hybris.orders b ON a.ReturnOrderNumber=b.OrderNumber AND a.countryid = 236)
					AND a.returnordernumber <> '11030155' --AS no same as Return no
					and countryid = 236 and p_sourcename = 'Hybris-DM'
					GROUP BY d.Returnorderid, Productid, orderitemid) a)t2  --107014


			delete from datamigration.dbo.dm_log where test_area = '853-Returnitems'
			IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
			drop table #tempact
			



			SELECT
			COUNT(*)
			--distinct ri.returnitemid, ro.returnorderid, ri.orderitemid, ri.action, ri.returnstatusid, ri.returnreasonid, oi.LineItemNo, hp.pk, oi.RetailProfit, oi.WholesalePrice, oi.TaxablePrice, ro.totaltax
			--INTO #tempact 
			from hybris.returnorder ro
			JOIN hybris.dbo.orders ho ON ro.ReturnOrderID=ho.PK
			JOIN hybris.dbo.users u ON u.p_rfaccountid=CAST(ro.AccountID AS NVARCHAR)
			JOIN hybris.returnitem ri ON ri.ReturnOrderID=ro.ReturnOrderID
			 JOIN hybris.orderitem oi ON oi.OrderItemID=ri.OrderItemID 
			 JOIN Hybris.dbo.products hp ON hp.pk=ri.ProductID
			WHERE--- d.OrderItemID NOT IN (8913859641390,
			--8907235459118,
			--9142116909102,
			--9099577524270,
			--9025632272430,
			--8913859543086,
			--9026156003374,
			--9142897410094,
			--8913859477550,
			--8998625640494,
			--8879388393518,
			--9025632501806,
			--8923076263982,
			--8923076001838,
			--8923075903534,
			--8907235590190)
			--AND p_catalognumber IS NOT NULL  and
			 p_catalog = '8796093088344'
			and p_catalogversion = '8796093153881'
			AND ro.returnordernumber NOT IN (SELECT a.returnordernumber FROM hybris.ReturnOrder a JOIN hybris.orders b ON a.ReturnOrderNumber=b.OrderNumber AND a.countryid = 236)
			AND ro.returnordernumber <> '11030155' --AS no same as Return no
			and countryid = 236 and p_sourcename = 'hybris-dm'
			AND ReturnItemID IN (SELECT  MAX(ReturnItemID) ReturnItemID
										FROM    RFOperations.Hybris.ReturnItem
										GROUP BY ReturnOrderID ,
												OrderItemID ,
												ProductID)  
			--AND d.ReturnStatusID <> 6

			SELECT * FROM #tempact


			create nonclustered index as_cls1 on #tempact (returnorderid)

			select 'Validation of column to column with no transformation in progress' as [Step-2 Validation], getdate() as StartTime
			set @cnt = 1
			select @lt_1 = count(*) from datamigration.dbo.map_tab where flag = 'c2c' and rfo_column <> @RFO_key and [owner] = '853-Returnitems' AND Hybris_Table = 'OrderEntries_bkp' --and prev_run_err > 0

			while @cnt<=@lt_1
			begin

			select @sql_gen_1 = 'SELECT DISTINCT  '''+[owner]+''', '''+flag+''', '''+[RFO_Reference Table]+''' as rfo_column, '''+Hybris_Column+''' as hybris_column, A.'+@HYB_key+' as hyb_key, A.'+Hybris_Column+' as hyb_value, B.'+@RFO_key+' as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.'+@HYB_key+', a.'+Hybris_Column+' FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+'
			except
			SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Col FROM rfoperations.'+[Schema]+'.'+RFO_Table+' a, #tempact b where a.'+@RFO_key+'=b.'+@RFO_key+') A  

			LEFT JOIN

			(SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Col FROM rfoperations.'+[Schema]+'.'+RFO_Table+' a, #tempact b where a.'+@RFO_key+'=b.'+@RFO_key+'
			except
			SELECT a.'+@HYB_key+', a.'+Hybris_Column+' FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+') B
			ON A.'+@HYB_key+'=B.'+@RFO_key+'
			UNION
			SELECT DISTINCT '''+[owner]+''', '''+flag+''', '''+[RFO_Reference Table]+''', '''+Hybris_Column+''', A.'+@HYB_key+', A.'+Hybris_Column+', B.'+@RFO_key+',B.RFO_Col

			FROM (SELECT a.'+@HYB_key+', a.'+Hybris_Column+' FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+'
			except
			SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Col FROM rfoperations.'+[Schema]+'.'+RFO_Table+' a, #tempact b where a.'+@RFO_key+'=b.'+@RFO_key+') A  

			RIGHT JOIN

			(SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Col FROM rfoperations.'+[Schema]+'.'+RFO_Table+' a, #tempact b where a.'+@RFO_key+'=b.'+@RFO_key+'
			except
			SELECT a.'+@HYB_key+', a.'+Hybris_Column+' FROM hybris.dbo.'+Hybris_Table+' a, #tempact b where a.'+@HYB_key+'=b.'+@RFO_key+') B
			ON A.'+@HYB_key+'=B.'+@RFO_key+''
			from (select *, row_number() over (order by [owner]) rn
			from datamigration.dbo.map_tab
			where flag = 'c2c' 
			and hybris_column <> @HYB_key
			--and prev_run_err > 0
			AND Hybris_Table = 'OrderEntries_bkp'
			and [owner] = '853-Returnitems')temp where rn = @cnt 

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
				and [owner] = '853-Returnitems'    
				AND Hybris_Table = 'OrderEntries_bkp' 
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
			where [owner] = '853-Returnitems' and flag = 'c2c' AND Hybris_Table = 'OrderEntries_bkp'
			and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '853-Returnitems' and test_type = 'c2c')


			--Defaults Check
			select 'Step-2 Completed, Validation of default columns in progress' as [Step-3 Validation],
			getdate() as StartTime

			set @cnt = 1
			select @lt_1 = COUNT(*) from datamigration.dbo.map_tab   where flag = 'defaults' and [owner] = '853-Returnitems' and [rfo_reference table]= 'NULL' AND Hybris_Table = 'OrderEntries_bkp'--and prev_run_err > 0
			select @lt_2 = COUNT(*) from datamigration.dbo.map_tab  where flag = 'defaults' and [owner] = '853-Returnitems' and [rfo_reference table]<> 'NULL' AND Hybris_Table = 'OrderEntries_bkp'--and prev_run_err > 0

			while (@cnt<=@lt_1 and @cnt<=@lt_2)
			begin
				if (select count(*) from datamigration.dbo.map_tab   where flag = 'defaults' and [owner] = '853-Returnitems' and [rfo_reference table] = 'NULL' AND Hybris_Table = 'OrderEntries_bkp') >1
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
					--and prev_run_err > 0
					AND Hybris_Table = 'OrderEntries_bkp'
					and [owner] = '853-Returnitems')temp where rn = @cnt
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
					and [owner] = '853-Returnitems' 
					AND Hybris_Table = 'OrderEntries_bkp'
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

				if (select count(*) from datamigration.dbo.map_tab  where flag = 'defaults' and [owner] = '853-Returnitems' and [rfo_reference table] <> 'NULL' AND Hybris_Table = 'OrderEntries_bkp') >1
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
					--and prev_run_err > 0
					AND Hybris_Table = 'OrderEntries_bkp'
					and [owner] = '853-Returnitems')temp where rn = @cnt
				end

				print @sql_gen_2
				insert into @temp (test_area, test_type, rfo_column, hybris_column, hyb_key, hyb_value, rfo_key, rfo_value)
				EXEC sp_executesql @sql_gen_2

				if(select count(*) from @temp) >1
				begin
					--declare @err_cnt int
					select @err_cnt = case when hyb_cnt = 0 then rfo_cnt else hyb_cnt end from (select count(distinct hyb_key) hyb_cnt, count(distinct rfo_key) rfo_cnt from @temp)t1

					update a
					set [prev_run_err] = @err_cnt
					from datamigration.dbo.map_tab a,
					@temp b
					where a.hybris_column=b.hybris_column
					and [owner] = '853-Returnitems' 
					AND Hybris_Table = 'OrderEntries_bkp'
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
			where [owner] = '853-Returnitems' and flag = 'defaults' AND Hybris_Table = 'OrderEntries_bkp'
			and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '853-Returnitems' and test_type = 'defaults')



			select 'Step-3 completed, Validation of transformed columns in progress' as [Step-4 Validation], getdate() as StartTime

			--Transformed Columns Validation 
			set @cnt = 1
			select @lt_1 = count(*) from datamigration.dbo.map_tab  where flag = 'manual' and rfo_column <> @RFO_key and [owner] = '853-Returnitems' AND Hybris_Table = 'OrderEntries_bkp'--and prev_run_err > 0

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
			and hybris_column <> @HYB_key 
			--and prev_run_err > 0
			AND id <> 448
			AND Hybris_Table = 'OrderEntries_bkp'
			and [owner] = '853-Returnitems')temp where rn = @cnt

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
				and [owner] = '853-Returnitems' 
				AND Hybris_Table = 'OrderEntries_bkp'
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
			where [owner] = '853-Returnitems' and flag = 'manual' AND Hybris_Table = 'OrderEntries_bkp'
			and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '853-Returnitems' and test_type = 'manual')



			select CASE WHEN ([total no of columns]-[columns passed]) >0 THEN 'VALIDATION INPROGRESS' ELSE 'VALIDATION COMPLETED' END [Status], [total no of columns], [columns passed],[total no of columns]-[columns passed] as [Required Analysis],
			getdate() as EndTime
			from 
				(select count(cnt) as [columns passed] from (select distinct hybris_column as cnt from datamigration.dbo.map_tab where [owner] = '853-Returnitems' AND Hybris_Table = 'OrderEntries_bkp' AND flag in ('c2c', 'manual', 'defaults')
				except
				select distinct hybris_column from DataMigration..dm_log where test_area = '853-Returnitems')a) tab1,

				(select count(id) as [total no of columns] from  datamigration.dbo.map_tab where [owner] = '853-Returnitems' AND Hybris_Table = 'OrderEntries_bkp' AND flag in ('c2c', 'manual','defaults'))tab2

			set statistics time off
			go

			--DROP TABLE Hybris..OrderEntries_bkp --Only for OrderEntry validation (due to huge count)


