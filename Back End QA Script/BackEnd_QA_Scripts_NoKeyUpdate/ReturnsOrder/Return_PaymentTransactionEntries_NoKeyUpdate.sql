USE RFOperations;
SET STATISTICS TIME ON;
GO

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

DECLARE @HYB_key VARCHAR(100) = 'code';
DECLARE @RFO_key VARCHAR(100) = 'returnorderID';
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

--Validation of AUTOSHIP Counts, Dups & Columns without transformations

--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    COUNT(*) cnt ,
                    f.p_paymenttransaction
          FROM      Hybris.dbo.paymentinfos (NOLOCK) a ,
                    Hybris.dbo.users b ,
                    Hybris.dbo.countries c ,
                    Hybris.dbo.orders (NOLOCK) d ,
                    Hybris.dbo.paymenttransactions e ,
                    Hybris.dbo.paymnttrnsctentries f
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND d.PK = a.OwnerPkString
                    AND a.PK = e.p_info
                    AND e.PK = f.p_paymenttransaction
                    AND c.isocode = 'US'
                    AND b.p_sourcename = 'Hybris-DM'
                    AND d.p_template IS NULL
                    AND d.TypePkString = 8796127723602
                    AND a.duplicate = 1 --R profile
GROUP BY            f.p_paymenttransaction
          HAVING    COUNT(*) > 1
        ) t1;


--Counts check on Hybris side for US
SELECT  hybris_cnt ,
        rfo_cnt ,
        CASE WHEN hybris_cnt > rfo_cnt THEN 'Hybris count more than RFO count'
             WHEN rfo_cnt > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END ResultsFromPKtoReturnPaymentTransaction
FROM    ( SELECT    COUNT(hpe.PK) hybris_cnt
          FROM      Hybris.dbo.orders ho
                    JOIN Hybris.dbo.users u ON u.PK = ho.userpk
                                               AND ho.TypePkString = 8796127723602
                                               AND ISNULL(ho.p_template, 0) = 0
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN Hybris.dbo.countries c ON c.PK = u.p_country
                                                   AND c.isocode = 'US'
                    JOIN Hybris.dbo.paymentinfos hpi ON hpi.OwnerPkString = ho.PK
                                                        AND hpi.duplicate = 1
                    JOIN Hybris.dbo.paymenttransactions hpt ON hpt.p_order = ho.PK
                    JOIN Hybris.dbo.paymnttrnsctentries hpe ON hpe.p_paymenttransaction = hpt.PK
        ) t1 , --110286
        ( SELECT    COUNT([ReturnPaymentTransactionId]) rfo_cnt
          FROM      RFOperations.Hybris.ReturnOrder ro
                    JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(ro.AccountID AS NVARCHAR)
                                               AND u.p_sourcename = 'Hybris-DM'
                    JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID
                    JOIN Hybris..orders ho ON ho.code = ro.ReturnOrderID
                                              AND ro.CountryID = 236
                    JOIN Hybris.ReturnPaymentTransaction rpt ON rpt.ReturnPaymentId = rp.ReturnPaymentId
					JOIN RodanFieldsLive.dbo.OrderPayments rfl ON rfl.OrderPaymentID=rp.ReturnPaymentId
					WHERE (LTRIM(RTRIM(rfl.BillingFirstName))<>'' OR LTRIM(RTRIM(rfl.BillingLastName))<>'')
        ) t2; --149286
	

--Column2Column Validation that doesn't have transformation - Autoship


DELETE  FROM DataMigration.dbo.dm_log
WHERE   test_area = '853-ReturnPaymentTransactionEntries';
IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
    DROP TABLE #tempact;

SELECT  f.ReturnPaymentTransactionId ,
        a.ReturnOrderID ,
        ReturnOrderNumber ,
        c.ReturnPaymentId ,
        a.OrderID ,
        e.PK AS paymenttransaction ,
        f.AmountAuthorized ,
        f.ProcessDate ,
        f.AvsResult ,
        f.AuthorizeType ,
        f.TransactionID ,
        CASE f.AuthorizeType
          WHEN 'CREDIT' THEN 'SUCCESFULL'
          WHEN 'AUTH_CAPTURE' THEN 'SUCCESFULL'
          WHEN 'SALE' THEN 'SUCCESFULL'
          WHEN 'VOID' THEN 'UNKNOWN_CODE'
        END AS transactionStatusDetails ,
        CASE f.AuthorizeType
          WHEN 'CREDIT' THEN 'ACCEPTED'
          WHEN 'AUTH_CAPTURE' THEN 'ACCEPTED'
          WHEN 'SALE' THEN 'ACCEPTED'
          WHEN 'VOID' THEN 'ERROR'
        END AS transactionStatus ,
        f.ResponseCode ,
        f.CardCodeResponse ,
        f.ApprovalCode
INTO    #tempact
FROM    RFOperations.Hybris.ReturnOrder a ,
        Hybris.dbo.users b ,
        RFOperations.Hybris.ReturnPayment c ,
        Hybris.dbo.paymentinfos d ,
        Hybris..orders o ,
        Hybris.dbo.[paymenttransactions] e ,
		 Hybris.ReturnPaymentTransaction f
WHERE   CAST(a.AccountID AS NVARCHAR) = b.p_rfaccountid
        AND a.ReturnOrderID = c.ReturnOrderID
        AND CAST(c.ReturnPaymentId AS NVARCHAR) = CAST(d.code AS NVARCHAR)
--AND d.pk=e.p_info
        AND CAST(o.code AS NVARCHAR) = CAST(a.ReturnOrderID AS NVARCHAR)
        AND o.PK = e.p_order
        AND c.ReturnPaymentId = f.ReturnPaymentId
        AND CountryID = 236
        AND b.p_sourcename = 'Hybris-DM'
GROUP BY f.ReturnPaymentTransactionId ,
        a.ReturnOrderID ,
        ReturnOrderNumber ,
        c.ReturnPaymentId ,
        a.OrderID ,
        e.PK ,
        f.AmountAuthorized ,
        f.ProcessDate ,
        f.AvsResult ,
        f.AuthorizeType ,
        f.TransactionID ,
        CASE f.AuthorizeType
          WHEN 'CREDIT' THEN 'SUCCESFULL'
          WHEN 'AUTH_CAPTURE' THEN 'SUCCESFULL'
          WHEN 'SALE' THEN 'SUCCESFULL'
          WHEN 'VOID' THEN 'UNKNOWN_CODE'
        END ,
        CASE f.AuthorizeType
          WHEN 'CREDIT' THEN 'ACCEPTED'
          WHEN 'AUTH_CAPTURE' THEN 'ACCEPTED'
          WHEN 'SALE' THEN 'ACCEPTED'
          WHEN 'VOID' THEN 'ERROR'
        END ,
        f.ResponseCode ,
        f.CardCodeResponse ,
        f.ApprovalCode;

CREATE CLUSTERED INDEX as_cls1 ON #tempact (ReturnPaymentTransactionId)


select 'Validation of default columns in progress' as [Step-1 Validation], getdate() as StartTime

----Defaults Check
set @cnt = 1
select @lt_1 = count(*) from datamigration.dbo.map_tab   where flag = 'defaults' and [owner] = '853-ReturnPaymentTransactionEntries' and [rfo_reference table]= 'NULL'
select @lt_2 = count(*) from datamigration.dbo.map_tab  where flag = 'defaults' and [owner] = '853-ReturnPaymentTransactionEntries' and [rfo_reference table]<> 'NULL'

while (@cnt<=@lt_1 and @cnt<=@lt_2)
begin
	if (select count(*) from datamigration.dbo.map_tab   where flag = 'defaults' and [owner] = '853-ReturnPaymentTransactionEntries' and [rfo_reference table] = 'NULL') >=1
	begin
        SELECT  @sql_gen_1 = 'use rfoperations
		select distinct ''' + [owner] + ''' as test_area, ''' + flag
                + ''' as test_type, null as rfo_column, ''' + Hybris_Column
                + ''' as hybris_column, ho.' + @HYB_key + ', a.' + hybris_column
                + ', null as rfo_key, null as rfo_value
		from hybris.dbo.' + Hybris_Table + ' a, 
		Hybris.dbo.paymenttransactions hpt,
		Hybris.dbo.orders ho,
	    Hybris.dbo.users u,
		Hybris.dbo.paymentinfos hpi,
		#tempact b 
		where ho.' + @HYB_key + '=b.' + @RFO_key + '
		 and a.p_paymenttransaction = hpt.PK
		 and hpt.p_order = ho.PK
		 AND hpi.OwnerPkString = ho.PK
		 AND u.PK = ho.userpk
		 AND u.P_country= 8796100624418
		 and ho.TypePkString = 8796127723602
		and a.' + hybris_column + ' is not null'
        FROM    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'defaults'
                            AND [RFO_Reference Table] = 'NULL'
                            AND [owner] = '853-ReturnPaymentTransactionEntries'
                ) temp
        WHERE   rn = @cnt;
	end

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
		and [owner] = '853-ReturnPaymentTransactionEntries' 
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

	if (select count(*) from datamigration.dbo.map_tab  where flag = 'defaults' and [owner] = '853-ReturnPaymentTransactionEntries' and [rfo_reference table] <> 'NULL') >=1
	begin
		select @sql_gen_2 = 'use rfoperations
		select distinct '''+[owner]+''' as test_area, '''+flag+''' as test_type, null as rfo_column, '''+Hybris_Column+''' as hybris_column, ho.'+@HYB_key+',a. '+hybris_column+', null as rfo_key, null as rfo_value
		from hybris.dbo.'+hybris_table+' a,Hybris.dbo.paymenttransactions hpt,
		Hybris.dbo.orders ho,
	    Hybris.dbo.users u,
		Hybris.dbo.paymentinfos hpi,
		#tempact b 
		where ho.' + @HYB_key + '=b.' + @RFO_key + '
		 and a.p_paymenttransaction = hpt.PK
		 and hpt.p_order = ho.PK
		 AND hpi.OwnerPkString = ho.PK
		 AND u.PK = ho.userpk
		 AND u.P_country= 8796100624418
		 and ho.TypePkString = 8796127723602
		and a.'+hybris_column+' <> '''+[rfo_reference table]+'''' 
		from (select *, row_number() over (order by [owner]) rn
		from datamigration.dbo.map_tab 
		where flag = 'defaults' 
		and [rfo_reference table] <> 'NULL'
		and [owner] = '853-ReturnPaymentTransactionEntries')temp where rn = @cnt
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
		and [owner] = '853-ReturnPaymentTransactionEntries' 
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
where [owner] = '853-ReturnPaymentTransactionEntries' and flag = 'defaults'
and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '853-ReturnPaymentTransactionEntries' and test_type = 'defaults')



select 'Step-1 completed, Validation of transformed columns in progress' as [Step-4 Validation], getdate() as StartTime

--Transformed Columns Validation --10:16 mins
set @cnt = 1
select @lt_1 = count(*) from datamigration.dbo.map_tab  where flag = 'manual' and rfo_column <> @RFO_key and [owner] = '853-ReturnPaymentTransactionEntries'

while @cnt<=@lt_1
begin

select  @sql_gen_1 = 'SELECT DISTINCT  '''+[owner]+''' as test_area, '''+flag+''' as test_type, '''+[RFO_Reference Table]+''' as rfo_column, '''+Hybris_Column+''' as hybris_column, A.'+@HYB_key+' as hyb_key, A.Hyb_Trans_col as hyb_value, B.'+@RFO_key+' as rfo_key, B.RFO_Trans_Col as rfo_value

FROM (SELECT ho.'+@HYB_key+',a. '+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a,Hybris.dbo.paymenttransactions hpt,
		Hybris.dbo.orders ho,
	    Hybris.dbo.users u,
		Hybris.dbo.paymentinfos hpi,
		#tempact b 
		where ho.' + @HYB_key + '=b.' + @RFO_key + '
		 and a.p_paymenttransaction = hpt.PK
		 and hpt.p_order = ho.PK
		 AND hpi.OwnerPkString = ho.PK
		 AND u.PK = ho.userpk
		 AND u.P_country= 8796100624418
		 and ho.TypePkString = 8796127723602
except
SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+RFO_Table+') A  

LEFT JOIN

(SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+RFO_Table+'
except
SELECT ho.'+@HYB_key+', a.'+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, Hybris.dbo.paymenttransactions hpt,
		Hybris.dbo.orders ho,
	    Hybris.dbo.users u,
		Hybris.dbo.paymentinfos hpi,
		#tempact b 
		where ho.' + @HYB_key + '=b.' + @RFO_key + '
		 and a.p_paymenttransaction = hpt.PK
		 and hpt.p_order = ho.PK
		 AND hpi.OwnerPkString = ho.PK
		 AND u.PK = ho.userpk
		 AND u.P_country= 8796100624418
		 and ho.TypePkString = 8796127723602) B
ON A.'+@HYB_key+'=B.'+@RFO_key+'
UNION
SELECT DISTINCT  '''+[owner]+''', '''+flag+''', '''+[RFO_Reference Table]+''', '''+Hybris_Column+''', A.'+@HYB_key+', A.Hyb_Trans_col, B.'+@RFO_key+', B.RFO_Trans_Col

FROM (SELECT ho.'+@HYB_key+', a.'+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a, Hybris.dbo.paymenttransactions hpt,
		Hybris.dbo.orders ho,
	    Hybris.dbo.users u,
		Hybris.dbo.paymentinfos hpi,
		#tempact b 
		where ho.' + @HYB_key + '=b.' + @RFO_key + '
		 and a.p_paymenttransaction = hpt.PK
		 and hpt.p_order = ho.PK
		 AND hpi.OwnerPkString = ho.PK
		 AND u.PK = ho.userpk
		 AND u.P_country= 8796100624418
		 and ho.TypePkString = 8796127723602
except
SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+RFO_Table+') A  

RIGHT JOIN

(SELECT a.'+@RFO_key+', '+RFO_Column+' as RFO_Trans_Col FROM '+RFO_Table+'
except
SELECT ho.'+@HYB_key+',a.'+Hybris_Column+' as Hyb_Trans_col FROM hybris.dbo.'+Hybris_Table+' a,Hybris.dbo.paymenttransactions hpt,
		Hybris.dbo.orders ho,
	    Hybris.dbo.users u,
		Hybris.dbo.paymentinfos hpi,
		#tempact b 
		where ho.' + @HYB_key + '=b.' + @RFO_key + '
		 and a.p_paymenttransaction = hpt.PK
		 and hpt.p_order = ho.PK
		 AND hpi.OwnerPkString = ho.PK
		 AND u.PK = ho.userpk
		 AND u.P_country= 8796100624418
		 and ho.TypePkString = 8796127723602) B
ON A.'+@HYB_key+'=B.'+@RFO_key+''
from (select *, row_number() over (order by [owner]) rn
from datamigration.dbo.map_tab
where flag = 'manual'
and Hybris_column <> @HYB_key
AND id <> 327 --orders related
and [owner] = '853-ReturnPaymentTransactionEntries')temp where rn = @cnt

print @sql_gen_1
insert into @temp (test_area, test_type, rfo_column, hybris_column, hyb_key, hyb_value, rfo_key, rfo_value)
EXEC sp_executesql @sql_gen_1


if(select count(*) from @temp) >=1
begin
	--declare @err_cnt int
	select @err_cnt = case when hyb_cnt = 0 then rfo_cnt else hyb_cnt end from (select count(distinct hyb_key) hyb_cnt, count(distinct rfo_key) rfo_cnt from @temp)t1

	update a
	set [prev_run_err] = @err_cnt
	from datamigration.dbo.map_tab a,
	@temp b
	where a.hybris_column=b.hybris_column
	and [owner] = '853-ReturnPaymentTransactionEntries' 
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
where [owner] = '853-ReturnPaymentTransactionEntries' and flag = 'manual'
and hybris_column not in (select distinct hybris_column from datamigration..dm_log where test_area = '853-ReturnPaymentTransactionEntries' and test_type = 'manual')

delete from @temp

set @cnt=@cnt+1

end

select 'VALIDATION COMPLETED' [Status], [total no of columns], [columns passed],[total no of columns]-[columns passed] as [Required Analysis],
getdate() as EndTime
from 
	(select count(cnt) as [columns passed] from (select distinct hybris_column as cnt from datamigration.dbo.map_tab where [owner] = '853-ReturnPaymentTransactionEntries' and flag in ('c2c', 'manual', 'defaults')
	except
	select distinct hybris_column from DataMigration..dm_log where test_area = '853-ReturnPaymentTransactionEntries')a) tab1,

	(select count(id) as [total no of columns] from  datamigration.dbo.map_tab where [owner] = '853-ReturnPaymentTransactionEntries' and flag in ('c2c', 'manual','defaults'))tab2

set statistics time off
go


