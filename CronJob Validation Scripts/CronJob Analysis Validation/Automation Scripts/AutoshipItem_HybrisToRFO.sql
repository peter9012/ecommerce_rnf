--Counts check on Hybris side for US
select Hybris_CNT, RFO_CNT, case when hybris_cnt > rfo_cnt then 'Hybris count more than RFO count'
			when rfo_cnt > hybris_cnt then 'RFO count more than Hybris count' else 'Count matches - validation passed' end Results
from (select count(distinct d.pk) hybris_cnt 
		from hybris.dbo.orders a,
		hybris.dbo.users b,
		hybris.dbo.countries c,
        hybris.dbo.orderentries d,
		hybris.dbo.CRP_Bulk_Run_1 e		
		where a.userpk=b.pk
		and b.p_country=c.pk
		and a.pk=d.orderpk
		AND e.pk=a.pk
		and c.isocode = 'US'
		and a.p_template = 1
		and p_sourcename = 'Hybris-DM')t1, --4139
		(select COUNT(c.AutoshipItemID)
		from RFOSERVER.rfoperations.hybris.autoship a,
		hybris.dbo.users b,
		RFOSERVER.rfoperations.hybris.autoshipitem c,
		hybris.dbo.CRP_Bulk_Run_1 d
		where a.accountid=b.p_rfaccountid
		and a.autoshipid=c.autoshipid
		AND d.code=a.AutoshipNumber
		and countryid = 236
		and p_sourcename = 'Hybris-DM') t2 --1712145
	

--Column2Column Validation that doesn't have transformation - Autoship

delete from Hybris.dbo.dm_log where test_area = '824-AutoshipItem'
IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
drop table #tempact

select a.autoshipid, autoshipnumber, b.pk, c.productid, c.lineitemno, cast(c.totaltax as float) as [totaltax]
into #tempact 
from RFOSERVER.rfoperations.hybris.autoship a,
hybris.dbo.users b,
RFOSERVER.rfoperations.hybris.autoshipitem c,
hybris.dbo.CRP_Bulk_Run_1 d
where a.accountid=b.p_rfaccountid
and a.autoshipid=c.autoshipid
AND d.code=a.autoshipnumber
and countryid = 236 
and p_sourcename = 'Hybris-DM'

create clustered index as_cls1 on #tempact (autoshipid)








SELECT DISTINCT  '824-AutoshipItem', 'c2c', 'BasePrice' as rfo_column, 'baseprice' as hybris_column, A.orderpk as hyb_key, A.baseprice as hyb_value, B.autoshipid as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.orderpk, baseprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.BasePrice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT a.autoshipid, a.BasePrice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, baseprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT '824-AutoshipItem', 'c2c', 'BasePrice', 'baseprice', A.orderpk, A.baseprice, B.autoshipid,B.RFO_Col

FROM (SELECT a.orderpk, baseprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.BasePrice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT a.autoshipid, a.BasePrice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, baseprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid






SELECT DISTINCT  '824-AutoshipItem', 'c2c', 'LineItemNo' as rfo_column, 'entrynumber' as hybris_column, A.orderpk as hyb_key, A.entrynumber as hyb_value, B.autoshipid as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.orderpk, entrynumber FROM hybris.dbo.OrderEntries a, #tempact b WHERE a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.LineItemNo-1 as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT a.autoshipid, a.LineItemNo-1 as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, entrynumber FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT '824-AutoshipItem', 'c2c', 'LineItemNo', 'entrynumber', A.orderpk, A.entrynumber, B.autoshipid,B.RFO_Col

FROM (SELECT a.orderpk, entrynumber FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.LineItemNo-1 as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT a.autoshipid, a.LineItemNo-1 as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, entrynumber FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid







SELECT DISTINCT  '824-AutoshipItem', 'c2c', 'Quantity' as rfo_column, 'quantity' as hybris_column, A.orderpk as hyb_key, A.quantity as hyb_value, B.autoshipid as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.orderpk, quantity FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.Quantity as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT a.autoshipid, a.Quantity as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, quantity FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT '824-AutoshipItem', 'c2c', 'Quantity', 'quantity', A.orderpk, A.quantity, B.autoshipid,B.RFO_Col

FROM (SELECT a.orderpk, quantity FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.Quantity as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT a.autoshipid, a.Quantity as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, quantity FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid






SELECT DISTINCT  '824-AutoshipItem', 'c2c', 'totalprice' as rfo_column, 'totalprice' as hybris_column, A.orderpk as hyb_key, A.totalprice as hyb_value, B.autoshipid as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.orderpk, totalprice FROM hybris.dbo.OrderEntries a, #tempact b WHERE a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.totalprice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT a.autoshipid, a.totalprice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, totalprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT '824-AutoshipItem', 'c2c', 'totalprice', 'totalprice', A.orderpk, A.totalprice, B.autoshipid,B.RFO_Col

FROM (SELECT a.orderpk, totalprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.totalprice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT a.autoshipid, a.totalprice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, totalprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid






SELECT DISTINCT  '824-AutoshipItem', 'c2c', 'AutoshipId' as rfo_column, 'orderpk' as hybris_column, A.orderpk as hyb_key, A.orderpk as hyb_value, B.autoshipid as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.orderpk, orderpk FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, Autoship as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT a.autoshipid, Autoship as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, orderpk FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT '824-AutoshipItem', 'c2c', 'AutoshipId', 'orderpk', A.orderpk, A.orderpk, B.autoshipid,B.RFO_Col

FROM (SELECT a.orderpk, orderpk FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, Autoship as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT a.autoshipid, Autoship as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, orderpk FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid






SELECT DISTINCT  '824-AutoshipItem', 'c2c', 'ModifiedTime' as rfo_column, 'p_rfmodifiedtime' as hybris_column, A.orderpk as hyb_key, A.p_rfmodifiedtime as hyb_value, B.autoshipid as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.orderpk, p_rfmodifiedtime FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.ServerModifiedDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT a.autoshipid, a.ServerModifiedDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_rfmodifiedtime FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT '824-AutoshipItem', 'c2c', 'ModifiedTime', 'p_rfmodifiedtime', A.orderpk, A.p_rfmodifiedtime, B.autoshipid,B.RFO_Col

FROM (SELECT a.orderpk, p_rfmodifiedtime FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.ServerModifiedDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT a.autoshipid, a.ServerModifiedDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_rfmodifiedtime FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid





SELECT DISTINCT  '824-AutoshipItem', 'c2c', 'QV' as rfo_column, 'p_qv' as hybris_column, A.orderpk as hyb_key, A.p_qv as hyb_value, B.autoshipid as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.orderpk, p_qv FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
EXCEPT
SELECT a.autoshipid, a.QV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT a.autoshipid, a.QV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_qv FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT '824-AutoshipItem', 'c2c', 'QV', 'p_qv', A.orderpk, A.p_qv, B.autoshipid,B.RFO_Col

FROM (SELECT a.orderpk, p_qv FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.QV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT a.autoshipid, a.QV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_qv FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid





SELECT DISTINCT  '824-AutoshipItem', 'c2c', 'CV' as rfo_column, 'p_cv' as hybris_column, A.orderpk as hyb_key, A.p_cv as hyb_value, B.autoshipid as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.orderpk, p_cv FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
EXCEPT
SELECT a.autoshipid, a.CV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT a.autoshipid, a.CV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_cv FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT '824-AutoshipItem', 'c2c', 'CV', 'p_cv', A.orderpk, A.p_cv, B.autoshipid,B.RFO_Col

FROM (SELECT a.orderpk, p_cv FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.CV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT a.autoshipid, a.CV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_cv FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid





SELECT DISTINCT  '824-AutoshipItem', 'c2c', 'RetailProfit' as rfo_column, 'p_retailprofit' as hybris_column, A.orderpk as hyb_key, A.p_retailprofit as hyb_value, B.autoshipid as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.orderpk, p_retailprofit FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.RetailProfit as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT a.autoshipid, a.RetailProfit as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_retailprofit FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT '824-AutoshipItem', 'c2c', 'RetailProfit', 'p_retailprofit', A.orderpk, A.p_retailprofit, B.autoshipid,B.RFO_Col

FROM (SELECT a.orderpk, p_retailprofit FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.RetailProfit as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT a.autoshipid, a.RetailProfit as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_retailprofit FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid





SELECT DISTINCT  '824-AutoshipItem', 'c2c', 'WholeSaleprice' as rfo_column, 'p_wholesaleprice' as hybris_column, A.orderpk as hyb_key, A.p_wholesaleprice as hyb_value, B.autoshipid as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.orderpk, p_wholesaleprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, coalesce(a.WholeSaleprice, 0) as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT a.autoshipid, coalesce(a.WholeSaleprice, 0) as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_wholesaleprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT '824-AutoshipItem', 'c2c', 'WholeSaleprice', 'p_wholesaleprice', A.orderpk, A.p_wholesaleprice, B.autoshipid,B.RFO_Col

FROM (SELECT a.orderpk, p_wholesaleprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, coalesce(a.WholeSaleprice, 0) as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT a.autoshipid, coalesce(a.WholeSaleprice, 0) as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_wholesaleprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid





SELECT DISTINCT  '824-AutoshipItem', 'c2c', 'TaxablePrice' as rfo_column, 'p_taxableprice' as hybris_column, A.orderpk as hyb_key, A.p_taxableprice as hyb_value, B.autoshipid as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.orderpk, p_taxableprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.TaxablePrice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

LEFT JOIN

(SELECT a.autoshipid, a.TaxablePrice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_taxableprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT '824-AutoshipItem', 'c2c', 'TaxablePrice', 'p_taxableprice', A.orderpk, A.p_taxableprice, B.autoshipid,B.RFO_Col

FROM (SELECT a.orderpk, p_taxableprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.TaxablePrice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid) A  

RIGHT JOIN

(SELECT a.autoshipid, a.TaxablePrice as RFO_Col FROM RFOSERVER.rfoperations.Hybris.AutoshipItem a, #tempact b where a.autoshipid=b.autoshipid
except
SELECT a.orderpk, p_taxableprice FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid



		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'p_chosenvendor' as hybris_column, a.orderpk, p_chosenvendor, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and p_chosenvendor is not null



		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'calculatedflag' as hybris_column, a.orderpk, calculatedflag, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and calculatedflag <> '0'


		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'p_deliveryaddress' as hybris_column, a.orderpk, p_deliveryaddress, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and p_deliveryaddress is not null


		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'discountvalues' as hybris_column, a.orderpk, discountvalues, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and discountvalues <> '[]'


		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'p_deliverymode' as hybris_column, a.orderpk, p_deliverymode, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and p_deliverymode is not null


		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'unitpk' as hybris_column, a.orderpk, unitpk, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and unitpk <> '8796093054986'

		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'p_nameddeliverydate' as hybris_column, a.orderpk, p_nameddeliverydate, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and p_nameddeliverydate is not null


		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'giveawayflag' as hybris_column, a.orderpk, giveawayflag, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and giveawayflag <> '0'


		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'p_quantitystatus' as hybris_column, a.orderpk, p_quantitystatus, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and p_quantitystatus is not null


		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'rejectedflag' as hybris_column, a.orderpk, rejectedflag, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and rejectedflag <> '0'

        
		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'oldprice' as hybris_column, a.orderpk, oldprice, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and oldprice is not null


		select distinct '824-AutoshipItem' as test_area, 'defaults' as test_type, null as rfo_column, 'p_isreturned' as hybris_column, a.orderpk, p_isreturned, null as rfo_key, null as rfo_value
		from hybris.dbo.OrderEntries a, #tempact b 
		where a.orderpk=b.autoshipid
		and p_isreturned <> '0'




SELECT DISTINCT  '824-AutoshipItem' as test_area, 'manual' as test_type, 'ProductID' as rfo_column, 'productpk' as hybris_column, A.orderpk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

FROM (SELECT a.orderpk, productpk as Hyb_Trans_col FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM (select a.autoshipid, b.pk
from #tempact a
join Hybris.dbo.products b
on b.p_rflegacyproductid = a.ProductID 
where p_catalognumber IS NOT NULL
and p_catalog = '8796093088344'
and p_catalogversion = '8796093153881'
group by a.autoshipid, b.pk) a) A  

LEFT JOIN

(SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM (select a.autoshipid, b.pk
from #tempact a
join Hybris.dbo.products b
on b.p_rflegacyproductid = a.ProductID 
where p_catalognumber IS NOT NULL
and p_catalog = '8796093088344'
and p_catalogversion = '8796093153881'
group by a.autoshipid, b.pk) a
except
SELECT a.orderpk, productpk as Hyb_Trans_col FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT  '824-AutoshipItem', 'manual', 'ProductID', 'productpk', A.orderpk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

FROM (SELECT a.orderpk, productpk as Hyb_Trans_col FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM (select a.autoshipid, b.pk
from #tempact a
join Hybris.dbo.products b
on b.p_rflegacyproductid = a.ProductID 
where p_catalognumber IS NOT NULL
and p_catalog = '8796093088344'
and p_catalogversion = '8796093153881'
group by a.autoshipid, b.pk) a) A  

RIGHT JOIN

(SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM (select a.autoshipid, b.pk
from #tempact a
join Hybris.dbo.products b
on b.p_rflegacyproductid = a.ProductID 
where p_catalognumber IS NOT NULL
and p_catalog = '8796093088344'
and p_catalogversion = '8796093153881'
group by a.autoshipid, b.pk) a
except
SELECT a.orderpk, productpk as Hyb_Trans_col FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid






SELECT DISTINCT  '824-AutoshipItem' as test_area, 'manual' as test_type, 'taxvalues' as rfo_column, 'taxvalues' as hybris_column, A.orderpk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

FROM (SELECT a.orderpk, taxvalues as Hyb_Trans_col FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, '[<TV<'+cast(a.lineitemno-1 as nvarchar(5))+'_1_'+cast(a.lineitemno-1 as nvarchar(5))+'_US TOTAL TAX#'+ CAST(coalesce(a.TotalTax, 0) AS NVARCHAR(20)) + '#true#' + '0.0' + '#USD>VT>]' as RFO_Trans_Col FROM #tempact a) A  

LEFT JOIN

(SELECT a.autoshipid, '[<TV<'+cast(a.lineitemno-1 as nvarchar(5))+'_1_'+cast(a.lineitemno-1 as nvarchar(5))+'_US TOTAL TAX#'+ CAST(coalesce(a.TotalTax, 0) AS NVARCHAR(20)) + '#true#' + '0.0' + '#USD>VT>]' as RFO_Trans_Col FROM #tempact a
except
SELECT a.orderpk, taxvalues as Hyb_Trans_col FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid
UNION
SELECT DISTINCT  '824-AutoshipItem', 'manual', 'taxvalues', 'taxvalues', A.orderpk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

FROM (SELECT a.orderpk, taxvalues as Hyb_Trans_col FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid
except
SELECT a.autoshipid, '[<TV<'+cast(a.lineitemno-1 as nvarchar(5))+'_1_'+cast(a.lineitemno-1 as nvarchar(5))+'_US TOTAL TAX#'+ CAST(coalesce(a.TotalTax, 0) AS NVARCHAR(20)) + '#true#' + '0.0' + '#USD>VT>]' as RFO_Trans_Col FROM #tempact a) A  

RIGHT JOIN

(SELECT a.autoshipid, '[<TV<'+cast(a.lineitemno-1 as nvarchar(5))+'_1_'+cast(a.lineitemno-1 as nvarchar(5))+'_US TOTAL TAX#'+ CAST(coalesce(a.TotalTax, 0) AS NVARCHAR(20)) + '#true#' + '0.0' + '#USD>VT>]' as RFO_Trans_Col FROM #tempact a
except
SELECT a.orderpk, taxvalues as Hyb_Trans_col FROM hybris.dbo.OrderEntries a, #tempact b where a.orderpk=b.autoshipid) B
ON A.orderpk=B.autoshipid

