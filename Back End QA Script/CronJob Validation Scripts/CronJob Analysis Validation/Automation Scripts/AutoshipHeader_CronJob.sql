


                                SELECT  t1.PK ,
                                        t2.AutoshipID ,
                                        CASE WHEN t1.PK IS NULL
                                             THEN 'missing in Hybris'
                                             WHEN t2.AutoshipID IS NULL
                                             THEN 'missing in RFO'
                                        END AS Results
                                FROM    ( SELECT    a.PK
                                          FROM      Hybris.dbo.orders a ,
                                                    Hybris.dbo.CRP_Bulk_Run_1 b
                                          WHERE     a.PK = b.PK
                                        ) t1
                                        FULL OUTER JOIN ( SELECT
                                                              a.AutoshipID
                                                          FROM
                                                              RFOSERVER.RFOperations.Hybris.Autoship  a,
                                                              Hybris.dbo.CRP_Bulk_Run_1 b
															  WHERE a.autoshipID=b.pk
                                                        ) t2 ON t1.PK = t2.AutoshipID
                                WHERE   t1.PK IS NULL
                                        OR t2.AutoshipID IS NULL 


							
		----Column2Column Validation that doesn't have transformation - Autoship



        DELETE  FROM Hybris.dbo.dm_log
        WHERE   test_area = '824-Autoship';

        IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
            DROP TABLE #tempact;



        SELECT  a.AutoshipID ,
                a.AutoshipNumber ,
                a.AccountID ,
                u.PK ,
                a.AutoshipStatusID ,
                a.AutoshipTypeID ,
                a.StartDate ,
                a.TotalTax
        INTO    #tempact
        FROM    rfoserver.RFOperations.Hybris.Autoship a
                JOIN Hybris.dbo.CRP_Bulk_Run_1 b ON a.AutoshipID = b.pk
                JOIN Hybris.dbo.users u ON u.p_rfaccountid = CAST(a.AccountID AS NVARCHAR)
        WHERE   a.CountryID = 236
        GROUP BY a.AutoshipID ,
                a.AutoshipNumber ,
                a.AccountID ,
                u.PK ,
                a.AutoshipStatusID ,
                a.AutoshipTypeID ,
                a.StartDate ,
                a.TotalTax;

CREATE CLUSTERED INDEX ct_1 ON #tempact(AutoshipID)


--==================================
--COLUMN TO COLUMN VALIDATION 
--==================================





SELECT DISTINCT  '824-Autoship', 'c2c', 'enddate' as rfo_column, 'p_cancelationdate' as hybris_column, A.pk as hyb_key, A.p_cancelationdate as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.p_cancelationdate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.EndDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, a.EndDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_cancelationdate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', 'enddate', 'p_cancelationdate', A.pk, A.p_cancelationdate, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.p_cancelationdate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.EndDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, a.EndDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_cancelationdate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID






SELECT DISTINCT  '824-Autoship', 'c2c', 'active' as rfo_column, 'p_active' as hybris_column, A.pk as hyb_key, A.p_active as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.p_active FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, active as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, active as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_active FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', 'active', 'p_active', A.pk, A.p_active, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.p_active FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, active as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, active as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_active FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID






SELECT DISTINCT  '824-Autoship', 'c2c', '[Total]' as rfo_column, 'totalprice' as hybris_column, A.pk as hyb_key, A.totalprice as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.totalprice FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, [Total] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, [Total] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.totalprice FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', '[Total]', 'totalprice', A.pk, A.totalprice, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.totalprice FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, [Total] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, [Total] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.totalprice FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID

          

SELECT DISTINCT  '824-Autoship', 'c2c', '[TotalDiscount] ' as rfo_column, 'totaldiscounts' as hybris_column, A.pk as hyb_key, A.totaldiscounts as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.totaldiscounts FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, [TotalDiscount]  as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, [TotalDiscount]  as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.totaldiscounts FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', '[TotalDiscount] ', 'totaldiscounts', A.pk, A.totaldiscounts, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.totaldiscounts FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, [TotalDiscount]  as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, [TotalDiscount]  as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.totaldiscounts FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID





SELECT DISTINCT  '824-Autoship', 'c2c', '[TotalTax]' as rfo_column, 'totaltax' as hybris_column, A.pk as hyb_key, A.totaltax as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.totaltax FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.TotalTax as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, a.TotalTax as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.totaltax FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', '[TotalTax]', 'totaltax', A.pk, A.totaltax, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.totaltax FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.TotalTax as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, a.TotalTax as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.totaltax FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID





SELECT DISTINCT  '824-Autoship', 'c2c', 'CompletionDate' as rfo_column, 'p_ordercompletiondate' as hybris_column, A.pk as hyb_key, A.p_ordercompletiondate as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.p_ordercompletiondate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.CompletionDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, a.CompletionDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_ordercompletiondate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', 'CompletionDate', 'p_ordercompletiondate', A.pk, A.p_ordercompletiondate, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.p_ordercompletiondate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.CompletionDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, a.CompletionDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_ordercompletiondate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID






SELECT DISTINCT  '824-Autoship', 'c2c', 'testorder' as rfo_column, 'p_testorder' as hybris_column, A.pk as hyb_key, A.p_testorder as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.p_testorder FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.testorder as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, a.testorder as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_testorder FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', 'testorder', 'p_testorder', A.pk, A.p_testorder, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.p_testorder FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.testorder as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, a.testorder as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_testorder FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID





SELECT DISTINCT  '824-Autoship', 'c2c', 'donotship' as rfo_column, 'p_donotship' as hybris_column, A.pk as hyb_key, A.p_donotship as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.p_donotship FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.donotship as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, a.donotship as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_donotship FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', 'donotship', 'p_donotship', A.pk, A.p_donotship, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.p_donotship FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.donotship as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID,a.donotship AS RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_donotship FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID





SELECT DISTINCT  '824-Autoship', 'c2c', 'ConsultantID' as rfo_column, 'p_consultantIdReceivingCommiss' as hybris_column, A.pk as hyb_key, A.p_consultantIdReceivingCommiss as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.p_consultantIdReceivingCommiss FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, ConsultantID as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, ConsultantID as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_consultantIdReceivingCommiss FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', 'ConsultantID', 'p_consultantIdReceivingCommiss', A.pk, A.p_consultantIdReceivingCommiss, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.p_consultantIdReceivingCommiss FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, ConsultantID as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, ConsultantID as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_consultantIdReceivingCommiss FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID





SELECT DISTINCT  '824-Autoship', 'c2c', 'NextRunDate' as rfo_column, 'p_schedulingdate' as hybris_column, A.pk as hyb_key, A.p_schedulingdate as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.p_schedulingdate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.NextRunDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, a.NextRunDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_schedulingdate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', 'NextRunDate', 'p_schedulingdate', A.pk, A.p_schedulingdate, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.p_schedulingdate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.NextRunDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, a.NextRunDate as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_schedulingdate FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID





SELECT DISTINCT  '824-Autoship', 'c2c', '[QV]' as rfo_column, 'p_totalqv' as hybris_column, A.pk as hyb_key, A.p_totalqv as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.p_totalqv FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, [QV] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, [QV] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_totalqv FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', '[QV]', 'p_totalqv', A.pk, A.p_totalqv, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.p_totalqv FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, [QV] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, [QV] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_totalqv FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID





SELECT DISTINCT  '824-Autoship', 'c2c', 'CV' as rfo_column, 'p_totalcv' as hybris_column, A.pk as hyb_key, A.p_totalcv as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.p_totalcv FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, CV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, CV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_totalcv FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', 'CV', 'p_totalcv', A.pk, A.p_totalcv, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.p_totalcv FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, CV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, CV as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_totalcv FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID




SELECT DISTINCT  '824-Autoship', 'c2c', '[SubTotal]' as rfo_column, 'subtotal' as hybris_column, A.pk as hyb_key, A.subtotal as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.subtotal FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, [SubTotal] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, [SubTotal] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.subtotal FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', '[SubTotal]', 'subtotal', A.pk, A.subtotal, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.subtotal FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, [SubTotal] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, [SubTotal] as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.subtotal FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID




SELECT DISTINCT  '824-Autoship', 'c2c', 'taxExempt' as rfo_column, 'p_taxexempt' as hybris_column, A.pk as hyb_key, A.p_taxexempt as hyb_value, B.autoshipID as rfo_key, B.RFO_Col as rfo_value

			FROM (SELECT a.pk, a.p_taxexempt FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, coalesce(taxExempt, '0') as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			LEFT JOIN

			(SELECT a.autoshipID, coalesce(taxExempt, '0') as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_taxexempt FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT '824-Autoship', 'c2c', 'taxExempt', 'p_taxexempt', A.pk, A.p_taxexempt, B.autoshipID,B.RFO_Col

			FROM (SELECT a.pk, a.p_taxexempt FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, coalesce(taxExempt, '0') as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID) A  

			RIGHT JOIN

			(SELECT a.autoshipID, coalesce(taxExempt, '0') as RFO_Col FROM RFOSERVER.rfoperations.Hybris.Autoship a, #tempact b where a.autoshipID=b.autoshipID
			except
			SELECT a.pk, a.p_taxexempt FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID





				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_versionid' as hybris_column, a.pk, p_versionid, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_versionid is not null

                
				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_fraudulent' as hybris_column, a.pk, p_fraudulent, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_fraudulent <> '0'




				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_originalversion' as hybris_column, a.pk, p_originalversion, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_originalversion is not null




				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_potentiallyfraudulent' as hybris_column, a.pk, p_potentiallyfraudulent, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_potentiallyfraudulent <> '0'




				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'deliverystatuspk' as hybris_column, a.pk, deliverystatuspk, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and deliverystatuspk is not null



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_salesapplication' as hybris_column, a.pk, p_salesapplication, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_salesapplication <> '8796105932891'



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'paymentmodepk' as hybris_column, a.pk, paymentmodepk, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and paymentmodepk is not null




				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'currencypk' as hybris_column, a.pk, currencypk, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and currencypk <> '8796125855777'


				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'paymentstatuspk' as hybris_column, a.pk, paymentstatuspk, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and paymentstatuspk is not null


				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'paymentcost' as hybris_column, a.pk, paymentcost, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and paymentcost <> '0'



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_exportstatus' as hybris_column, a.pk, p_exportstatus, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_exportstatus is not null



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_istaxcalculationfailed' as hybris_column, a.pk, p_istaxcalculationfailed, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_istaxcalculationfailed <> '0'



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'statusinfo' as hybris_column, a.pk, statusinfo, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and statusinfo is not null


	select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_store' as hybris_column, a.pk, p_store, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_store <> '8796093056989'



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_trackingid' as hybris_column, a.pk, p_trackingid, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_trackingid is not null


				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_totalamountwithouttax' as hybris_column, a.pk, p_totalamountwithouttax, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_totalamountwithouttax <> '0'



	select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_rfmodifiedtime' as hybris_column, a.pk, p_rfmodifiedtime, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_rfmodifiedtime is not null



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_taxmanuallychanged' as hybris_column, a.pk, p_taxmanuallychanged, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_taxmanuallychanged <> '0'



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_previousdeliverymode' as hybris_column, a.pk, p_previousdeliverymode, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_previousdeliverymode is not null


	select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_warehouse' as hybris_column, a.pk, p_warehouse, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_warehouse <> '8796093056981'



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_departmentselect' as hybris_column, a.pk, p_departmentselect, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_departmentselect is not null



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_origination' as hybris_column, a.pk, p_origination, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_origination <> 'www.rodanandfields.com'


				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_overridetype' as hybris_column, a.pk, p_overridetype, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_overridetype is not null


				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_taxmanuallychangedvalue' as hybris_column, a.pk, p_taxmanuallychangedvalue, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_taxmanuallychangedvalue <> '0'



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_overridereason' as hybris_column, a.pk, p_overridereason, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_overridereason is not null


				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_deliverycostmanuallychanged' as hybris_column, a.pk, p_deliverycostmanuallychanged, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_deliverycostmanuallychanged <> '0'


				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_associatedorder' as hybris_column, a.pk, p_associatedorder, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_associatedorder is not null



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_handlingcostmanuallychanged' as hybris_column, a.pk, p_handlingcostmanuallychanged, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_handlingcostmanuallychanged <> '0'



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_associatedtemplate' as hybris_column, a.pk, p_associatedtemplate, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_associatedtemplate is not null



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_template' as hybris_column, a.pk, p_template, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_template <> '1'



				select distinct '824-Autoship' as test_area, 'defaults' as test_type, null as rfo_column, 'p_associatedtemplate' as hybris_column, a.pk, p_associatedtemplate, null as rfo_key, null as rfo_value
				from hybris.dbo.Orders a, #tempact b 
				where a.pk=b.autoshipID
				and p_associatedtemplate is not null






--========================================
--MANUAL VALIDATION transformed fields 
--========================================


SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'AutoshipPaymentID' as rfo_column, 'paymentinfopk' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipID as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, paymentinfopk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.AutoshipPaymentID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.autoshippaymentid
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPayment b
on a.autoshipid= b.autoshipid 

join hybris.dbo.paymentinfos c
on b.autoshippaymentid=c.pk
and b.autoshipid=c.ownerpkstring
and duplicate = 1
group by a.autoshipnumber, a.autoshipid, b.autoshippaymentid)a) A  

			LEFT JOIN

			(SELECT a.autoshipID, a.AutoshipPaymentID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.autoshippaymentid
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPayment b
on a.autoshipid= b.autoshipid 

join hybris.dbo.paymentinfos c
on b.autoshippaymentid=c.pk
and b.autoshipid=c.ownerpkstring
and duplicate = 1
group by a.autoshipnumber, a.autoshipid, b.autoshippaymentid)a
			except
			SELECT a.pk, paymentinfopk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'AutoshipPaymentID', 'paymentinfopk', A.pk, A.Hyb_Trans_col, B.autoshipID, B.RFO_Trans_Col

			FROM (SELECT a.pk, paymentinfopk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID
			except
			SELECT a.autoshipID, a.AutoshipPaymentID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.autoshippaymentid
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPayment b
on a.autoshipid= b.autoshipid 

join hybris.dbo.paymentinfos c
on b.autoshippaymentid=c.pk
and b.autoshipid=c.ownerpkstring
and duplicate = 1
group by a.autoshipnumber, a.autoshipid, b.autoshippaymentid)a) A  

			RIGHT JOIN

			(SELECT a.autoshipID, a.AutoshipPaymentID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.autoshippaymentid
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPayment b
on a.autoshipid= b.autoshipid 

join hybris.dbo.paymentinfos c
on b.autoshippaymentid=c.pk
and b.autoshipid=c.ownerpkstring
and duplicate = 1
group by a.autoshipnumber, a.autoshipid, b.autoshippaymentid)a
			except
			SELECT a.pk, paymentinfopk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipID) B
			ON A.pk=B.autoshipID






SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'ordercount' as rfo_column, 'p_ordercount' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, p_ordercount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, coalesce(a.ordercount, 0) as RFO_Trans_Col FROM (select asp.autoshipid, asp.autoshipnumber, count(*) ordercount
from #tempact asp,
RFOSERVER.RFOperations.hybris.orders ord 
where asp.autoshipid = ord.autoshipid 
and ord.orderstatusid not in (1,3) 
group by asp.autoshipid, asp.autoshipnumber)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, coalesce(a.ordercount, 0) as RFO_Trans_Col FROM (select asp.autoshipid, asp.autoshipnumber, count(*) ordercount
from #tempact asp,
RFOSERVER.RFOperations.hybris.orders ord 
where asp.autoshipid = ord.autoshipid 
and ord.orderstatusid not in (1,3) 
group by asp.autoshipid, asp.autoshipnumber)a
			except
			SELECT a.pk, p_ordercount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'ordercount', 'p_ordercount', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, p_ordercount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, coalesce(a.ordercount, 0) as RFO_Trans_Col FROM (select asp.autoshipid, asp.autoshipnumber, count(*) ordercount
from #tempact asp,
RFOSERVER.RFOperations.hybris.orders ord
where asp.autoshipid = ord.autoshipid 
and ord.orderstatusid not in (1,3) 
group by asp.autoshipid, asp.autoshipnumber)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, coalesce(a.ordercount, 0) as RFO_Trans_Col FROM (select asp.autoshipid, asp.autoshipnumber, count(*) ordercount
from #tempact asp,
RFOSERVER.RFOperations.hybris.orders ord
where asp.autoshipid = ord.autoshipid 
and ord.orderstatusid not in (1,3) 
group by asp.autoshipid, asp.autoshipnumber)a
			except
			SELECT a.pk, p_ordercount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid





SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'failurecount' as rfo_column, 'p_ccfailurecount' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, p_ccfailurecount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, coalesce(a.failurecount, 0) as RFO_Trans_Col FROM (select asp.autoshipid, asp.autoshipnumber, count(*) failurecount
from #tempact asp,
RFOSERVER.RFOperations.hybris.orders ord
where asp.autoshipid = ord.autoshipid 
and ord.orderstatusid in (1,3) 
group by asp.autoshipid, asp.autoshipnumber)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, coalesce(a.failurecount, 0) as RFO_Trans_Col FROM (select asp.autoshipid, asp.autoshipnumber, count(*) failurecount
from #tempact asp,
RFOSERVER.RFOperations.hybris.orders ord
where asp.autoshipid = ord.autoshipid 
and ord.orderstatusid in (1,3) 
group by asp.autoshipid, asp.autoshipnumber)a
			except
			SELECT a.pk, p_ccfailurecount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'failurecount', 'p_ccfailurecount', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, p_ccfailurecount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, coalesce(a.failurecount, 0) as RFO_Trans_Col FROM (select asp.autoshipid, asp.autoshipnumber, count(*) failurecount
from #tempact asp,
RFOSERVER.RFOperations.hybris.orders ord
where asp.autoshipid = ord.autoshipid 
and ord.orderstatusid in (1,3) 
group by asp.autoshipid, asp.autoshipnumber)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, coalesce(a.failurecount, 0) as RFO_Trans_Col FROM (select asp.autoshipid, asp.autoshipnumber, count(*) failurecount
from #tempact asp,
RFOSERVER.RFOperations.hybris.orders ord
where asp.autoshipid = ord.autoshipid 
and ord.orderstatusid in (1,3) 
group by asp.autoshipid, asp.autoshipnumber)a
			except
			SELECT a.pk, p_ccfailurecount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid





SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'Use Hybris pk from user table based on RFO accountid' as rfo_column, 'p_customer' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, p_customer as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a
			except
			SELECT a.pk, p_customer as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'Use Hybris pk from user table based on RFO accountid', 'p_customer', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, p_customer as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a
			except
			SELECT a.pk, p_customer as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid




SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'AutoshipPaymentID' as rfo_column, 'paymentinfopk' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, paymentinfopk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.AutoshipPaymentID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.autoshippaymentid
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPayment b
on a.autoshipid= b.autoshipid 

join hybris.dbo.paymentinfos c
on b.autoshippaymentid=c.pk
and b.autoshipid=c.ownerpkstring
and duplicate = 1
group by a.autoshipnumber, a.autoshipid, b.autoshippaymentid)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.AutoshipPaymentID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.autoshippaymentid
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPayment b
on a.autoshipid= b.autoshipid 

join hybris.dbo.paymentinfos c
on b.autoshippaymentid=c.pk
and b.autoshipid=c.ownerpkstring
and duplicate = 1
group by a.autoshipnumber, a.autoshipid, b.autoshippaymentid)a
			except
			SELECT a.pk, paymentinfopk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'AutoshipPaymentID', 'paymentinfopk', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, paymentinfopk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.AutoshipPaymentID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.autoshippaymentid
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPayment b
on a.autoshipid= b.autoshipid 

join hybris.dbo.paymentinfos c
on b.autoshippaymentid=c.pk
and b.autoshipid=c.ownerpkstring
and duplicate = 1
group by a.autoshipnumber, a.autoshipid, b.autoshippaymentid)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.AutoshipPaymentID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.autoshippaymentid
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPayment b
on a.autoshipid= b.autoshipid 

join hybris.dbo.paymentinfos c
on b.autoshippaymentid=c.pk
and b.autoshipid=c.ownerpkstring
and duplicate = 1
group by a.autoshipnumber, a.autoshipid, b.autoshippaymentid)a
			except
			SELECT a.pk, paymentinfopk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid



SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'status' as rfo_column, 'statuspk' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, statuspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM (SELECT  t1.AutoshipID ,
        CASE WHEN t2.Name = 'Submitted' THEN '8796135030875'
             WHEN t2.Name = 'Cancelled' THEN '8796093284443'
             WHEN t2.Name = 'Failed' THEN '8796135030875'
        END AS PK
FROM    #tempact t1 ,
        RFOSERVER.RFOperations.RFO_Reference.AutoshipStatus t2 ,
        Hybris.dbo.vEnumerationValues t3
WHERE   t1.AutoshipStatusId = t2.AutoshipStatusId
        AND t2.Name = t3.Value
        AND t3.[Type] = 'orderstatus'
GROUP BY t1.AutoshipID ,
        CASE WHEN t2.Name = 'Submitted' THEN '8796135030875'
             WHEN t2.Name = 'Cancelled' THEN '8796093284443'
             WHEN t2.Name = 'Failed' THEN '8796135030875'
        END )a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM (SELECT  t1.AutoshipID ,
        CASE WHEN t2.Name = 'Submitted' THEN '8796135030875'
             WHEN t2.Name = 'Cancelled' THEN '8796093284443'
             WHEN t2.Name = 'Failed' THEN '8796135030875'
        END AS PK
FROM    #tempact t1 ,
         RFOSERVER.RFOperations.RFO_Reference.AutoshipStatus t2 ,
        Hybris.dbo.vEnumerationValues t3
WHERE   t1.AutoshipStatusId = t2.AutoshipStatusId
        AND t2.Name = t3.Value
        AND t3.[Type] = 'orderstatus'
GROUP BY t1.AutoshipID ,
        CASE WHEN t2.Name = 'Submitted' THEN '8796135030875'
             WHEN t2.Name = 'Cancelled' THEN '8796093284443'
             WHEN t2.Name = 'Failed' THEN '8796135030875'
        END )a
			except
			SELECT a.pk, statuspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'status', 'statuspk', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, statuspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM (SELECT  t1.AutoshipID ,
        CASE WHEN t2.Name = 'Submitted' THEN '8796135030875'
             WHEN t2.Name = 'Cancelled' THEN '8796093284443'
             WHEN t2.Name = 'Failed' THEN '8796135030875'
        END AS PK
FROM    #tempact t1 ,
         RFOSERVER.RFOperations.RFO_Reference.AutoshipStatus t2 ,
        Hybris.dbo.vEnumerationValues t3
WHERE   t1.AutoshipStatusId = t2.AutoshipStatusId
        AND t2.Name = t3.Value
        AND t3.[Type] = 'orderstatus'
GROUP BY t1.AutoshipID ,
        CASE WHEN t2.Name = 'Submitted' THEN '8796135030875'
             WHEN t2.Name = 'Cancelled' THEN '8796093284443'
             WHEN t2.Name = 'Failed' THEN '8796135030875'
        END )a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM (SELECT  t1.AutoshipID ,
        CASE WHEN t2.Name = 'Submitted' THEN '8796135030875'
             WHEN t2.Name = 'Cancelled' THEN '8796093284443'
             WHEN t2.Name = 'Failed' THEN '8796135030875'
        END AS PK
FROM    #tempact t1 ,
     RFOSERVER.RFOperations.RFO_Reference.AutoshipStatus t2 ,
        Hybris.dbo.vEnumerationValues t3
WHERE   t1.AutoshipStatusId = t2.AutoshipStatusId
        AND t2.Name = t3.Value
        AND t3.[Type] = 'orderstatus'
GROUP BY t1.AutoshipID ,
        CASE WHEN t2.Name = 'Submitted' THEN '8796135030875'
             WHEN t2.Name = 'Cancelled' THEN '8796093284443'
             WHEN t2.Name = 'Failed' THEN '8796135030875'
        END )a
			except
			SELECT a.pk, statuspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid




SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, '' as rfo_column, 'p_createdby' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, p_createdby as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a
			except
			SELECT a.pk, p_createdby as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', '', 'p_createdby', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, p_createdby as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a
			except
			SELECT a.pk, p_createdby as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid




SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'Use Hybris pk from user table based on RFO accountid' as rfo_column, 'userpk' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, userpk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a
			except
			SELECT a.pk, userpk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'Use Hybris pk from user table based on RFO accountid', 'userpk', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, userpk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.pk as RFO_Trans_Col FROM #tempact a
			except
			SELECT a.pk, userpk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid





SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'Lastprocessingdate' as rfo_column, 'p_lastprocessingdate' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, p_lastprocessingdate as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.completiondate as RFO_Trans_Col FROM (SELECT   ash.AutoShipID ,
                al.completiondate
       FROM     #tempact ash
                JOIN ( SELECT   AutoShipID ,
                                MAX(CompletionDate) AS completiondate
                                            --OrderStatusID
                       FROM     RFOSERVER.RFOperations.Hybris.Orders
                       WHERE    CountryID = 236--@CountryID
                                AND OrderStatusID NOT IN ( 1, 3 )
                       GROUP BY AutoShipID
                     ) al ON al.AutoShipID = ash.AutoShipID)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.completiondate as RFO_Trans_Col FROM (SELECT   ash.AutoShipID ,
                al.completiondate
       FROM     #tempact ash
                JOIN ( SELECT   AutoShipID ,
                                MAX(CompletionDate) AS completiondate
                                            --OrderStatusID
                       FROM     RFOSERVER.RFOperations.Hybris.Orders
                       WHERE    CountryID = 236--@CountryID
                                AND OrderStatusID NOT IN ( 1, 3 )
                       GROUP BY AutoShipID
                     ) al ON al.AutoShipID = ash.AutoShipID)a
			except
			SELECT a.pk, p_lastprocessingdate as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'Lastprocessingdate', 'p_lastprocessingdate', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, p_lastprocessingdate as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.completiondate as RFO_Trans_Col FROM (SELECT   ash.AutoShipID ,
                al.completiondate
       FROM     #tempact ash
                JOIN ( SELECT   AutoShipID ,
                                MAX(CompletionDate) AS completiondate
                                            --OrderStatusID
                       FROM     RFOSERVER.RFOperations.Hybris.Orders
                       WHERE    CountryID = 236--@CountryID
                                AND OrderStatusID NOT IN ( 1, 3 )
                       GROUP BY AutoShipID
                     ) al ON al.AutoShipID = ash.AutoShipID)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.completiondate as RFO_Trans_Col FROM (SELECT   ash.AutoShipID ,
                al.completiondate
       FROM     #tempact ash
                JOIN ( SELECT   AutoShipID ,
                                MAX(CompletionDate) AS completiondate
                                            --OrderStatusID
                       FROM     RFOSERVER.RFOperations.Hybris.Orders
                       WHERE    CountryID = 236--@CountryID
                                AND OrderStatusID NOT IN ( 1, 3 )
                       GROUP BY AutoShipID
                     ) al ON al.AutoShipID = ash.AutoShipID)a
			except
			SELECT a.pk, p_lastprocessingdate as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid






SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'frequency' as rfo_column, 'p_frequency' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, p_frequency as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, case when autoshiptypeid = 1 then 8796134473819 else 8796134506587 end as RFO_Trans_Col FROM #tempact a) A  

			LEFT JOIN

			(SELECT a.autoshipid, case when autoshiptypeid = 1 then 8796134473819 else 8796134506587 end as RFO_Trans_Col FROM #tempact a
			except
			SELECT a.pk, p_frequency as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'frequency', 'p_frequency', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, p_frequency as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, case when autoshiptypeid = 1 then 8796134473819 else 8796134506587 end as RFO_Trans_Col FROM #tempact a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, case when autoshiptypeid = 1 then 8796134473819 else 8796134506587 end as RFO_Trans_Col FROM #tempact a
			except
			SELECT a.pk, p_frequency as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid





SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'startdate' as rfo_column, 'p_firstscheduledautoshipdate' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, p_firstscheduledautoshipdate as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, case when autoshiptypeid = 1 then startdate else dateadd(month, 1, startdate) end as RFO_Trans_Col FROM #tempact a) A  

			LEFT JOIN

			(SELECT a.autoshipid, case when autoshiptypeid = 1 then startdate else dateadd(month, 1, startdate) end as RFO_Trans_Col FROM #tempact a
			except
			SELECT a.pk, p_firstscheduledautoshipdate as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'startdate', 'p_firstscheduledautoshipdate', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, p_firstscheduledautoshipdate as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, case when autoshiptypeid = 1 then startdate else dateadd(month, 1, startdate) end as RFO_Trans_Col FROM #tempact a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, case when autoshiptypeid = 1 then startdate else dateadd(month, 1, startdate) end as RFO_Trans_Col FROM #tempact a
			except
			SELECT a.pk, p_firstscheduledautoshipdate as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid




SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'recent consecutivedelaycount' as rfo_column, 'p_consicutivenoofdelay' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, p_consicutivenoofdelay as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.ConsecutiveDelayCount as RFO_Trans_Col FROM (select * from (select distinct autoshipid, autoshipnumber, templateid, consecutivedelaycount, row_number() over (partition by templateid order by adcl.servermodifieddate desc) rn
from #tempact asp,
 RFOSERVER.RFOperations.logging.autoshipdelaycancellationlog adcl
 where asp.autoshipid=adcl.templateid) tmp
where tmp.rn = 1)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.ConsecutiveDelayCount as RFO_Trans_Col FROM (select * from (select distinct autoshipid, autoshipnumber, templateid, consecutivedelaycount, row_number() over (partition by templateid order by adcl.servermodifieddate desc) rn
from #tempact asp,
 RFOSERVER.RFOperations.logging.autoshipdelaycancellationlog adcl
 where asp.autoshipid=adcl.templateid) tmp
where tmp.rn = 1)a
			except
			SELECT a.pk, p_consicutivenoofdelay as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'recent consecutivedelaycount', 'p_consicutivenoofdelay', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, p_consicutivenoofdelay as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.ConsecutiveDelayCount as RFO_Trans_Col FROM (select * from (select distinct autoshipid, autoshipnumber, templateid, consecutivedelaycount, row_number() over (partition by templateid order by adcl.servermodifieddate desc) rn
from #tempact asp,
 RFOSERVER.RFOperations.logging.autoshipdelaycancellationlog adcl
 where asp.autoshipid=adcl.templateid) tmp
where tmp.rn = 1)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.ConsecutiveDelayCount as RFO_Trans_Col FROM (select * from (select distinct autoshipid, autoshipnumber, templateid, consecutivedelaycount, row_number() over (partition by templateid order by adcl.servermodifieddate desc) rn
from #tempact asp,
 RFOSERVER.RFOperations.logging.autoshipdelaycancellationlog adcl
 where asp.autoshipid=adcl.templateid) tmp
where tmp.rn = 1)a
			except
			SELECT a.pk, p_consicutivenoofdelay as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid





SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'delaycount' as rfo_column, 'p_delaycount' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, p_delaycount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.DelayCount as RFO_Trans_Col FROM (select a.autoshipid, a.autoshipnumber, b.*
from #tempact a,
RFOSERVER.RFOperations.logging.AutoshipDelayCancellationLog b
where a.autoshipid= b.templateid)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.DelayCount as RFO_Trans_Col FROM (select a.autoshipid, a.autoshipnumber, b.*
from #tempact a,
RFOSERVER.RFOperations.logging.AutoshipDelayCancellationLog b
where a.autoshipid= b.templateid)a
			except
			SELECT a.pk, p_delaycount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'delaycount', 'p_delaycount', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, p_delaycount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.DelayCount as RFO_Trans_Col FROM (select a.autoshipid, a.autoshipnumber, b.*
from #tempact a,
RFOSERVER.RFOperations.logging.AutoshipDelayCancellationLog b
where a.autoshipid= b.templateid)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.DelayCount as RFO_Trans_Col FROM (select a.autoshipid, a.autoshipnumber, b.*
from #tempact a,
RFOSERVER.RFOperations.logging.AutoshipDelayCancellationLog b
where a.autoshipid= b.templateid)a
			except
			SELECT a.pk, p_delaycount as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid






SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'AutoshipPaymentAddressID' as rfo_column, 'paymentaddresspk' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, paymentaddresspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.AutoshipPaymentAddressID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.AutoshipPaymentAddressID
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPaymentAddress b
on a.autoshipid= b.autoshipid)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.AutoshipPaymentAddressID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.AutoshipPaymentAddressID
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPaymentAddress b
on a.autoshipid= b.autoshipid)a
			except
			SELECT a.pk, paymentaddresspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'AutoshipPaymentAddressID', 'paymentaddresspk', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, paymentaddresspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.AutoshipPaymentAddressID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.AutoshipPaymentAddressID
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPaymentAddress b
on a.autoshipid= b.autoshipid)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.AutoshipPaymentAddressID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.AutoshipPaymentAddressID
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipPaymentAddress b
on a.autoshipid= b.autoshipid)a
			except
			SELECT a.pk, paymentaddresspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid





SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'ShippingCost' as rfo_column, 'deliverycost' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, deliverycost as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.ShippingCost as RFO_Trans_Col FROM (select a.autoshipnumber, a.Autoshipid, coalesce(b.ShippingCost,0) as ShippingCost
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShipment b
on a.autoshipid= b.autoshipid 
group by a.autoshipnumber, a.Autoshipid, b.ShippingCost)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.ShippingCost as RFO_Trans_Col FROM (select a.autoshipnumber, a.Autoshipid, coalesce(b.ShippingCost,0) as ShippingCost
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShipment b
on a.autoshipid= b.autoshipid 
group by a.autoshipnumber, a.Autoshipid, b.ShippingCost)a
			except
			SELECT a.pk, deliverycost as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'ShippingCost', 'deliverycost', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, deliverycost as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.ShippingCost as RFO_Trans_Col FROM (select a.autoshipnumber, a.Autoshipid, coalesce(b.ShippingCost,0) as ShippingCost
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShipment b
on a.autoshipid= b.autoshipid 
group by a.autoshipnumber, a.Autoshipid, b.ShippingCost)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.ShippingCost as RFO_Trans_Col FROM (select a.autoshipnumber, a.Autoshipid, coalesce(b.ShippingCost,0) as ShippingCost
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShipment b
on a.autoshipid= b.autoshipid 
group by a.autoshipnumber, a.Autoshipid, b.ShippingCost)a
			except
			SELECT a.pk, deliverycost as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid




SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'taxvalues' as rfo_column, 'totaltaxvalues' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, totaltaxvalues as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, '[<TV<0_1_0_US TOTAL TAX#' + CAST(coalesce(a.totaltax, 0) AS NVARCHAR(20)) + '#true#' + '0.0' + '#USD>VT>|
<TV<1_1_100001_US SHIPPING TAX#' + CAST(coalesce(a.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#true#'
                + CAST(coalesce(a.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#USD>VT>|
<TV<2_1_300001_US HANDLING TAX#'
                + CAST(coalesce(a.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#true#'
                + CAST(coalesce(a.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#USD>VT>]' as RFO_Trans_Col FROM (select distinct asp.autoshipid, asp.autoshipnumber, ash.TaxOnShippingCost, ash.TaxOnHandlingCost, asp.totaltax
from #tempact asp
left join RFOSERVER.RFOperations.hybris.autoshipshipment ash
on asp.autoshipid=ash.autoshipid)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, '[<TV<0_1_0_US TOTAL TAX#' + CAST(coalesce(a.totaltax, 0) AS NVARCHAR(20)) + '#true#' + '0.0' + '#USD>VT>|
<TV<1_1_100001_US SHIPPING TAX#' + CAST(coalesce(a.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#true#'
                + CAST(coalesce(a.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#USD>VT>|
<TV<2_1_300001_US HANDLING TAX#'
                + CAST(coalesce(a.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#true#'
                + CAST(coalesce(a.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#USD>VT>]' as RFO_Trans_Col FROM (select distinct asp.autoshipid, asp.autoshipnumber, ash.TaxOnShippingCost, ash.TaxOnHandlingCost, asp.totaltax
from #tempact asp
left join RFOSERVER.RFOperations.hybris.autoshipshipment ash
on asp.autoshipid=ash.autoshipid)a
			except
			SELECT a.pk, totaltaxvalues as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'taxvalues', 'totaltaxvalues', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, totaltaxvalues as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, '[<TV<0_1_0_US TOTAL TAX#' + CAST(coalesce(a.totaltax, 0) AS NVARCHAR(20)) + '#true#' + '0.0' + '#USD>VT>|
<TV<1_1_100001_US SHIPPING TAX#' + CAST(coalesce(a.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#true#'
                + CAST(coalesce(a.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#USD>VT>|
<TV<2_1_300001_US HANDLING TAX#'
                + CAST(coalesce(a.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#true#'
                + CAST(coalesce(a.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#USD>VT>]' as RFO_Trans_Col FROM (select distinct asp.autoshipid, asp.autoshipnumber, ash.TaxOnShippingCost, ash.TaxOnHandlingCost, asp.totaltax
from #tempact asp
left join RFOSERVER.RFOperations.hybris.autoshipshipment ash
on asp.autoshipid=ash.autoshipid)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, '[<TV<0_1_0_US TOTAL TAX#' + CAST(coalesce(a.totaltax, 0) AS NVARCHAR(20)) + '#true#' + '0.0' + '#USD>VT>|
<TV<1_1_100001_US SHIPPING TAX#' + CAST(coalesce(a.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#true#'
                + CAST(coalesce(a.TaxOnShippingCost, 0) AS NVARCHAR(20)) + '#USD>VT>|
<TV<2_1_300001_US HANDLING TAX#'
                + CAST(coalesce(a.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#true#'
                + CAST(coalesce(a.TaxOnHandlingCost, 0) AS NVARCHAR(20)) + '#USD>VT>]' as RFO_Trans_Col FROM (select distinct asp.autoshipid, asp.autoshipnumber, ash.TaxOnShippingCost, ash.TaxOnHandlingCost, asp.totaltax
from #tempact asp
left join RFOSERVER.RFOperations.hybris.autoshipshipment ash
on asp.autoshipid=ash.autoshipid)a
			except
			SELECT a.pk, totaltaxvalues as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid




SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'HandlingCost' as rfo_column, 'handlingcost' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, handlingcost as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.HandlingCost as RFO_Trans_Col FROM (select a.autoshipnumber, a.Autoshipid, coalesce(b.HandlingCost,0) as HandlingCost
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShipment b
on a.autoshipid= b.autoshipid 
group by a.autoshipnumber, a.Autoshipid, b.HandlingCost)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.HandlingCost as RFO_Trans_Col FROM (select a.autoshipnumber, a.Autoshipid, coalesce(b.HandlingCost,0) as HandlingCost
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShipment b
on a.autoshipid= b.autoshipid 
group by a.autoshipnumber, a.Autoshipid, b.HandlingCost)a
			except
			SELECT a.pk, handlingcost as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'HandlingCost', 'handlingcost', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, handlingcost as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.HandlingCost as RFO_Trans_Col FROM (select a.autoshipnumber, a.Autoshipid, coalesce(b.HandlingCost,0) as HandlingCost
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShipment b
on a.autoshipid= b.autoshipid 
group by a.autoshipnumber, a.Autoshipid, b.HandlingCost)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.HandlingCost as RFO_Trans_Col FROM (select a.autoshipnumber, a.Autoshipid, coalesce(b.HandlingCost,0) as HandlingCost
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShipment b
on a.autoshipid= b.autoshipid 
group by a.autoshipnumber, a.Autoshipid, b.HandlingCost)a
			except
			SELECT a.pk, handlingcost as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid





SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'AutoshipShippingAddressID' as rfo_column, 'deliveryaddresspk' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, deliveryaddresspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.AutoshipShippingAddressID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.AutoshipShippingAddressID
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShippingAddress b
on a.autoshipid= b.autoshipid)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.AutoshipShippingAddressID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.AutoshipShippingAddressID
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShippingAddress b
on a.autoshipid= b.autoshipid)a
			except
			SELECT a.pk, deliveryaddresspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'AutoshipShippingAddressID', 'deliveryaddresspk', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, deliveryaddresspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.AutoshipShippingAddressID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.AutoshipShippingAddressID
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShippingAddress b
on a.autoshipid= b.autoshipid)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.AutoshipShippingAddressID as RFO_Trans_Col FROM (select a.autoshipnumber, a.autoshipid, b.AutoshipShippingAddressID
from #tempact a
left join RFOSERVER.RFOperations.hybris.AutoshipShippingAddress b
on a.autoshipid= b.autoshipid)a
			except
			SELECT a.pk, deliveryaddresspk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid




SELECT DISTINCT  '824-Autoship' as test_area, 'manual' as test_type, 'ShippingMethod' as rfo_column, 'deliverymodepk' as hybris_column, A.pk as hyb_key, A.Hyb_Trans_col as hyb_value, B.autoshipid as rfo_key, B.RFO_Trans_Col as rfo_value

			FROM (SELECT a.pk, deliverymodepk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.ShippingMethod as RFO_Trans_Col FROM (select distinct asp.autoshipid, asp.autoshipnumber,dm.pk as shippingmethod
from #tempact asp
left join RFOSERVER.RFOperations.hybris.autoshipshipment ash
on asp.autoshipid=ash.autoshipid
left join RFOSERVER.RFOperations.[RFO_Reference].[ShippingMethod] sm
on ash.shippingmethodid=sm.shippingmethodid
left join hybris.[dbo].[deliverymodes] dm
on sm.name=dm.code)a) A  

			LEFT JOIN

			(SELECT a.autoshipid, a.ShippingMethod as RFO_Trans_Col FROM (select distinct asp.autoshipid, asp.autoshipnumber,dm.pk as shippingmethod
from #tempact asp
left join RFOSERVER.RFOperations.hybris.autoshipshipment ash
on asp.autoshipid=ash.autoshipid
left join RFOSERVER.RFOperations.[RFO_Reference].[ShippingMethod] sm
on ash.shippingmethodid=sm.shippingmethodid
left join hybris.[dbo].[deliverymodes] dm
on sm.name=dm.code)a
			except
			SELECT a.pk, deliverymodepk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid
			UNION
			SELECT DISTINCT  '824-Autoship', 'manual', 'ShippingMethod', 'deliverymodepk', A.pk, A.Hyb_Trans_col, B.autoshipid, B.RFO_Trans_Col

			FROM (SELECT a.pk, deliverymodepk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid
			except
			SELECT a.autoshipid, a.ShippingMethod as RFO_Trans_Col FROM (select distinct asp.autoshipid, asp.autoshipnumber,dm.pk as shippingmethod
from #tempact asp
left join RFOSERVER.RFOperations.hybris.autoshipshipment ash
on asp.autoshipid=ash.autoshipid
left join RFOSERVER.RFOperations.[RFO_Reference].[ShippingMethod] sm
on ash.shippingmethodid=sm.shippingmethodid
left join hybris.[dbo].[deliverymodes] dm
on sm.name=dm.code)a) A  

			RIGHT JOIN

			(SELECT a.autoshipid, a.ShippingMethod as RFO_Trans_Col FROM (select distinct asp.autoshipid, asp.autoshipnumber,dm.pk as shippingmethod
from #tempact asp
left join RFOSERVER.RFOperations.hybris.autoshipshipment ash
on asp.autoshipid=ash.autoshipid
left join RFOSERVER.RFOperations.[RFO_Reference].[ShippingMethod] sm
on ash.shippingmethodid=sm.shippingmethodid
left join hybris.[dbo].[deliverymodes] dm
on sm.name=dm.code)a
			except
			SELECT a.pk, deliverymodepk as Hyb_Trans_col FROM hybris.dbo.Orders a, #tempact b where a.pk=b.autoshipid) B
			ON A.pk=B.autoshipid





