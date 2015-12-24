USE RFOperations;
SET STATISTICS TIME ON;
GO

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

DECLARE @HYB_key VARCHAR(100) = 'Code';
DECLARE @RFO_key VARCHAR(100) = 'ReturnOrderId';
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

/*
SELECT d.* INTO Hybris..OrderEntries
from hybris.dbo.orders a,
hybris.dbo.users b,
hybris.dbo.countries c,
hybris.dbo.orderentries d
where a.userpk=b.pk--and b.p_country=c.pk
and a.pk=d.orderpk
and c.isocode = 'US'
and p_sourcename = 'Hybris-DM'
and a.p_template is NULL AND a.TypePkString=8796127723602
*/

--Duplicate check on Hybris side for US
SELECT  CASE WHEN COUNT(1) > 0 THEN 'Duplicates Found'
             ELSE 'No duplicates - Validation Passed'
        END AS [Step-1 Validation]
FROM    ( SELECT    COUNT(*) cnt ,
                    e.p_returnrequest ,
                    e.p_orderentry
          FROM      Hybris.dbo.orders a ,
                    Hybris.dbo.users b ,
                    Hybris.dbo.countries c ,
                    Hybris.dbo.returnrequest d ,
                    Hybris.dbo.returnentry e
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND a.PK = d.p_returnorder
                    AND d.PK = e.p_returnrequest
                    AND c.isocode = 'US'
                    AND p_sourcename = 'Hybris-DM'
                    AND a.p_template IS NULL
                    AND a.TypePkString = 8796127723602
          GROUP BY  e.p_returnrequest ,
                    e.p_orderentry
          HAVING    COUNT(*) > 1
        ) t1;

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
        ) t1;

--Counts check on Hybris side for OrderEntries US
SELECT  hybris_cnt ,
        RFO_CNT ,
        CASE WHEN hybris_cnt > RFO_CNT THEN 'Hybris count more than RFO count'
             WHEN RFO_CNT > hybris_cnt THEN 'RFO count more than Hybris count'
             ELSE 'Count matches - validation passed'
        END Results
FROM    ( SELECT    COUNT(d.PK) hybris_cnt
          FROM      Hybris.dbo.orders a ,
                    Hybris.dbo.users b ,
                    Hybris.dbo.countries c ,
                    Hybris.dbo.orderentries d
          WHERE     a.userpk = b.PK
                    AND b.p_country = c.PK
                    AND a.PK = d.orderpk
                    AND c.isocode = 'US'
                    AND p_sourcename = 'Hybris-DM'
                    AND a.p_template IS NULL
                    AND a.TypePkString = 8796127723602
        ) t1 , --106994
        ( SELECT    COUNT(*) RFO_CNT
          FROM      ( SELECT    MAX(ReturnItemID) id ,
                                d.ReturnOrderID ,
                                ProductID ,
                                OrderItemID
                      FROM      Hybris.ReturnOrder a ,
                                Hybris.dbo.orders b ,
                                Hybris.dbo.users c ,
                                Hybris.ReturnItem d ,
                                Hybris.dbo.products e
								--,Hybris.dbo.orders r
                      WHERE     a.ReturnOrderID = b.Code
                                AND b.userpk = c.PK
                                AND a.ReturnOrderID = d.ReturnOrderID
                                AND d.ProductID = e.p_rflegacyproductid
								---AND a.OrderID=r.code
                                AND p_catalog = '8796093088344'
                                AND p_catalogversion = '8796093153881'
                                --AND a.ReturnOrderNumber NOT IN (
                                --SELECT  a.ReturnOrderNumber
                                --FROM    Hybris.ReturnOrder a
                                --        JOIN Hybris.Orders b ON a.ReturnOrderNumber = b.OrderNumber
                                --                              AND a.CountryID = 236 )
                                --AND a.ReturnOrderNumber <> '11030155' --AS no same as Return no
                                AND CountryID = 236
                                AND p_sourcename = 'Hybris-DM'
                      GROUP BY  d.ReturnOrderID ,
                                ProductID ,
                                OrderItemID
                    ) a
        ) t2;  --107014


delete from datamigration.dbo.dm_log where test_area = '853-Returnitems'
IF OBJECT_ID('tempdb..#tempact') IS NOT NULL
drop table #tempact


SELECT DISTINCT
        d.ReturnItemID ,
        a.ReturnOrderID ,
        d.OrderItemID ,
        d.Action ,
        d.ReturnStatusID ,
        d.ReturnReasonID ,
        e.LineItemNo - 1 AS LineItemNo ,
        f.PK ,
        e.RetailProfit ,
        e.WholesalePrice ,
        e.TaxablePrice ,
        a.TotalTax
INTO    #tempact
FROM    Hybris.ReturnOrder a ,
        Hybris.dbo.orders b ,
        Hybris.dbo.users c ,
        Hybris.ReturnItem d ,
        Hybris.OrderItem e ,
        Hybris.dbo.products f
WHERE   a.ReturnOrderID = b.code
        AND b.userpk = c.PK
        AND a.ReturnOrderID = d.ReturnOrderID
        AND d.OrderItemID = e.OrderItemID
        AND e.ProductID = f.p_rflegacyproductid
--AND d.OrderItemID NOT IN (8913859641390,
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
        AND p_catalognumber IS NOT NULL
        AND p_catalog = '8796093088344'
        AND p_catalogversion = '8796093153881';
--AND a.returnordernumber NOT IN (SELECT a.returnordernumber FROM hybris.ReturnOrder a JOIN hybris.orders b ON a.ReturnOrderNumber=b.OrderNumber AND a.countryid = 236)
--AND a.returnordernumber <> '11030155' --AS no same as Return no
--and countryid = 236 and p_sourcename = 'hybris-dm'
--AND ReturnItemID IN (SELECT  MAX(ReturnItemID) ReturnItemID
--                            FROM    RFOperations.Hybris.ReturnItem
--                            GROUP BY ReturnOrderID ,
--                                    OrderItemID ,
--                                    ProductID)  
--AND d.ReturnStatusID <> 6



create clustered index as_cls1 on #tempact (returnorderid)

SELECT  'Validation of column to column with no transformation in progress' AS [Step-1 Validation] ,
        GETDATE() AS StartTime;

SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
 FROM    DataMigration.dbo.map_tab
WHERE   flag = 'c2c'
        AND rfo_column <> @RFO_key
        AND [owner] = '853-Returnitems'
		AND Hybris_Table = 'OrderEntries';

WHILE @cnt <= @lt_1
    BEGIN

        SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner] + ''', ''' + flag
                + ''', ''' + [RFO_Reference Table] + ''' as rfo_column, '''
                + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                + ' as hyb_key, A.' + Hybris_Column + ' as hyb_value, B.'
                + @RFO_key + ' as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                        from hybris.dbo.' + Hybris_Table + ' t, 
                        hybris.dbo.users u,
						Hybris.dbo.countries c,
                        hybris.dbo.orders b
                                    where t.orderpk=b.pk
									and b.userpk=u.pk
                                    and u.P_country=8796100624418
                                            and b.typepkstring=8796127723602 and p_template is null  ) a
except
SELECT b.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.ReturnOrderId=b.ReturnOrderId) A  

LEFT JOIN

(SELECT b.' + @RFO_key + ', ' + RFO_Column
                + ' as RFO_Col FROM rfoperations.' + [Schema] + '.'
                + RFO_Table + ' a, #tempact b where a.ReturnOrderId=b.ReturnOrderId
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                                    from hybris.dbo.' + Hybris_Table + ' t, 
                                    hybris.dbo.users u,
                                    hybris.dbo.orders b
                                                where t.orderpk=b.pk 
                                                and b.userpk=u.pk and u.P_country=8796100624418
                                                    and b.typepkstring=8796127723602  and p_template is null  ) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.' + Hybris_Column + ', B.' + @RFO_key
                + ',B.RFO_Col

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                                from hybris.dbo.' + Hybris_Table + ' t, 
                                hybris.dbo.users u,
                                hybris.dbo.orders b
                                            where t.orderpk=b.pk 
                                            and b.userpk=u.pk and u.P_country=8796100624418
                                    and b.typepkstring=8796127723602 and p_template is null  ) a
except
SELECT b.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.ReturnOrderId=b.ReturnOrderId) A  

RIGHT JOIN

(SELECT b.' + @RFO_key + ', ' + RFO_Column
                + ' as RFO_Col FROM rfoperations.' + [Schema] + '.'
                + RFO_Table + ' a, #tempact b where a.ReturnOrderId=b.ReturnOrderId
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                                from hybris.dbo.' + Hybris_Table + ' t, 
                                hybris.dbo.users u ,
                                hybris.dbo.orders b
                                            where t.orderpk=b.pk 
                                            and b.userpk=u.pk and u.P_country=8796100624418
                                    and b.typepkstring=8796127723602 and p_template is null ) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + ''
        FROM    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'c2c'
                            AND rfo_column <> @RFO_key
                            AND [owner] = '853-Returnitems'
							AND Hybris_Table = 'OrderEntries'
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
                        AND [owner] = '853-Returnitems'
						AND Hybris_Table = 'OrderEntries'; 
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
WHERE   [owner] = '853-Returnitems'
        AND flag = 'c2c'
		AND Hybris_Table = 'OrderEntries'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '853-Returnitems'
                AND test_type = 'c2c' );


--Defaults Check
SELECT  'Step-1 Completed, Validation of default columns in progress' AS [Step-2 Validation] ,
        GETDATE() AS StartTime;

SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'defaults'
        AND [owner] = '853-Returnitems'
		AND Hybris_Table = 'OrderEntries'
        AND [RFO_Reference Table] <> 'NULL';

WHILE ( @cnt <= @lt_1 )
    BEGIN

        IF ( SELECT COUNT(*)
             FROM   DataMigration.dbo.map_tab
             WHERE  flag = 'defaults'
                    AND [owner] = '853-Returnitems'
					AND Hybris_Table = 'OrderEntries'
                    AND [RFO_Reference Table] <> 'NULL'
           ) > 1
            BEGIN
                SELECT  @sql_gen_1 = 'select distinct ''' + [owner]
                        + ''' as test_area, ''' + flag
                        + ''' as test_type, null as rfo_column, '''
                        + Hybris_Column + ''' as hybris_column, a.' + @HYB_key
                        + ', ' + hybris_column
                        + ', null as rfo_key, null as rfo_value
from (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                    from hybris.dbo.' + Hybris_Table + ' t, 
                    hybris.dbo.users u,
                    hybris.dbo.orders b
                                where t.orderpk=b.pk 
                                and b.userpk=u.pk and u.P_country=8796100624418
                                    and b.typepkstring=8796127723602 and p_template is null ) a
where ' + hybris_column + ' <> ''' + [RFO_Reference Table] + ''''
                FROM    ( SELECT    * ,
                                    ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                          FROM      DataMigration.dbo.map_tab
                          WHERE     flag = 'defaults'
                                    AND [RFO_Reference Table] <> 'NULL'
                                    AND [owner] = '853-Returnitems'
									AND Hybris_Table = 'OrderEntries'
                        ) temp
                WHERE   rn = @cnt;
            END;

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
            --declare @err_cnt int
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
                        AND [owner] = '853-Returnitems'
						AND Hybris_Table = 'OrderEntries'; 
            END;  

        INSERT  INTO DataMigration.dbo.dm_log
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

        SET @cnt = @cnt + 1;

    END;

UPDATE  DataMigration.dbo.map_tab
SET     [prev_run_err] = 0
WHERE   [owner] = '853-Returnitems'
        AND flag = 'defaults'
		AND Hybris_Table = 'OrderEntries'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '853-Returnitems'
                AND test_type = 'defaults' );


--update datamigration.dbo.map_tab
--set [prev_run_err] = 0
--where [owner] = '824-Autoship' and flag = 'defaults' and [prev_run_err] is null

SELECT  'Step-2 completed, Validation of transformed columns in progress' AS [Step-3 Validation] ,
        GETDATE() AS StartTime;

SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'manual'
        AND rfo_column <> @RFO_key
        AND [owner] = '853-Returnitems'
		AND Hybris_Table = 'OrderEntries';

WHILE @cnt <= @lt_1
    BEGIN

        SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner]
                + ''' as test_area, ''' + flag + ''' as test_type, '''
                + [RFO_Reference Table] + ''' as rfo_column, '''
                + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                + ' as hyb_key, A.Hyb_Trans_col as hyb_value, B.' + @RFO_key
                + ' as rfo_key, B.RFO_Trans_Col as rfo_value

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                from hybris.dbo.' + Hybris_Table + ' t, 
                hybris.dbo.users u,
                hybris.dbo.orders b
                            where t.orderpk=b.pk 
                            and b.userpk=u.pk and u.P_country=8796100624418
                                    and b.typepkstring=8796127723602 and p_template is null ) a
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + ') A  

LEFT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                from hybris.dbo.' + Hybris_Table + ' t, 
                hybris.dbo.users u,
                hybris.dbo.orders b
                            where t.orderpk=b.pk 
                            and b.userpk=u.pk and U.P_country=8796100624418
                                  and b.typepkstring=8796127723602 and p_template is null  ) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT  ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.Hyb_Trans_col, B.' + @RFO_key
                + ', B.RFO_Trans_Col

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                                from hybris.dbo.' + Hybris_Table + ' t, 
                                hybris.dbo.users u,
								 hybris.dbo.orders b
								where t.orderpk=b.pk 
								and b.userpk=u.pk 
								and u.P_country=8796100624418
                                 and b.typepkstring=8796127723602
								  and p_template is null ) a
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + ') A  

RIGHT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
            from hybris.dbo.' + Hybris_Table + ' t, 
            hybris.dbo.users u,
            hybris.dbo.orders b
			where t.orderpk=b.pk 
			and b.userpk=u.pk 
			and u.P_country=8796100624418
			and p_template is null
			and b.typepkstring=8796127723602  ) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + ''
        FROM    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'manual'
                            AND rfo_column <> @RFO_key 
--and id not in (3,4,64,65) --order not migrated yet
                            AND [owner] = '853-Returnitems'
							AND Hybris_Table = 'OrderEntries'
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
      --declare @err_cnt int
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
                        AND [owner] = '853-Returnitems'
						AND Hybris_Table = 'OrderEntries'; 
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
WHERE   [owner] = '853-Returnitems'
        AND flag = 'manual'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '853-Returnitems'
                AND test_type = 'manual' 
				AND Hybris_Table = 'OrderEntries');

SELECT  'VALIDATION COMPLETED' [Status] ,
        [total no of columns] ,
        [columns passed] ,
        [total no of columns] - [columns passed] AS [Required Analysis] ,
        GETDATE() AS EndTime
FROM    ( SELECT    COUNT(cnt) AS [columns passed]
          FROM      ( SELECT DISTINCT
                                hybris_column AS cnt
                      FROM      DataMigration.dbo.map_tab
                      WHERE     [owner] = '853-Returnitems'
					  AND Hybris_Table = 'OrderEntries'
                                AND flag IN ( 'c2c', 'manual', 'defaults' )
                      EXCEPT
                      SELECT DISTINCT
                                hybris_column
                      FROM      DataMigration..dm_log
                      WHERE     test_area = '853-Returnitems'
                    ) a
        ) tab1 ,
        ( SELECT    COUNT(id) AS [total no of columns]
          FROM      DataMigration.dbo.map_tab
          WHERE     [owner] = '853-Returnitems'
                    AND flag IN ( 'c2c', 'manual', 'defaults' )
        ) tab2;



--===========================================================================================================================================




SELECT  'Validation of column to column with no transformation in progress' AS [Step-1 Validation] ,
        GETDATE() AS StartTime;

SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'c2c'
        AND rfo_column <> @RFO_key
        AND [owner] = '853-Returnitems'
		AND Hybris_Table = 'ReturnEntry';

WHILE @cnt <= @lt_1
    BEGIN

        SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner] + ''', ''' + flag
                + ''', ''' + [RFO_Reference Table] + ''' as rfo_column, '''
                + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                + ' as hyb_key, A.' + Hybris_Column + ' as hyb_value, B.'
                + @RFO_key + ' as rfo_key, B.RFO_Col as rfo_value

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                        from hybris.dbo.' + Hybris_Table + ' t,
						Hybris.dbo.ReturnRequest q, 
                        hybris.dbo.users u,
						Hybris.dbo.countries c,
                        hybris.dbo.orders b
                        where t.p_returnrequest=q.pk
						and q.P_returnOrder=b.pk
						and b.userpk=u.pk
                        and u.P_country=8796100624418
                        and b.typepkstring=8796127723602
						and p_template is null  ) a
except
SELECT b.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.ReturnOrderId=b.ReturnOrderId) A  

LEFT JOIN

(SELECT b.' + @RFO_key + ', a.' + RFO_Column
                + ' as RFO_Col FROM rfoperations.' + [Schema] + '.'
                + RFO_Table + ' a, #tempact b where a.ReturnOrderId=b.ReturnOrderId
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                    from hybris.dbo.' + Hybris_Table + ' t, 
                    Hybris.dbo.ReturnRequest q, 
                        hybris.dbo.users u,
						Hybris.dbo.countries c,
                        hybris.dbo.orders b
                        where t.p_returnrequest=q.pk
						and q.P_returnOrder=b.pk
						and b.userpk=u.pk
                        and u.P_country=8796100624418
                        and b.typepkstring=8796127723602
						and p_template is null  ) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.' + Hybris_Column + ', B.' + @RFO_key
                + ',B.RFO_Col

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                    from hybris.dbo.' + Hybris_Table + ' t, 
                    Hybris.dbo.ReturnRequest q, 
                        hybris.dbo.users u,
						Hybris.dbo.countries c,
                        hybris.dbo.orders b
                        where t.p_returnrequest=q.pk
						and q.P_returnOrder=b.pk
						and b.userpk=u.pk
                        and u.P_country=8796100624418
                        and b.typepkstring=8796127723602
						and p_template is null  ) a
except
SELECT b.' + @RFO_key + ', a.' + RFO_Column + ' as RFO_Col FROM rfoperations.'
                + [Schema] + '.' + RFO_Table + ' a, #tempact b where a.ReturnOrderId=b.ReturnOrderId) A  

RIGHT JOIN

(SELECT b.' + @RFO_key + ', a.' + RFO_Column
                + ' as RFO_Col FROM rfoperations.' + [Schema] + '.'
                + RFO_Table + ' a, #tempact b where a.ReturnOrderId=b.ReturnOrderId
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column + ' FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                            from hybris.dbo.' + Hybris_Table + ' t, 
                            Hybris.dbo.ReturnRequest q, 
                        hybris.dbo.users u,
						Hybris.dbo.countries c,
                        hybris.dbo.orders b
                        where t.p_returnrequest=q.pk
						and q.P_returnOrder=b.pk
						and b.userpk=u.pk
                        and u.P_country=8796100624418
                        and b.typepkstring=8796127723602
						and p_template is null  ) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + ''
        FROM    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'c2c'
                            AND rfo_column <> @RFO_key
                            AND [owner] = '853-Returnitems'
							AND Hybris_Table = 'ReturnEntry'
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
               -- DECLARE @err_cnt INT;
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
                        AND [owner] = '853-Returnitems'
						AND Hybris_Table = 'ReturnEntry'; 
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
WHERE   [owner] = '853-Returnitems'
        AND flag = 'c2c'
		AND Hybris_Table = 'ReturnEntry'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '853-Returnitems'
                AND test_type = 'c2c' );


--Defaults Check
SELECT  'Step-1 Completed, Validation of default columns in progress' AS [Step-2 Validation] ,
        GETDATE() AS StartTime;

SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'defaults'
        AND [owner] = '853-Returnitems'
		AND Hybris_Table = 'ReturnEntry'
        AND [RFO_Reference Table] <> 'NULL';

WHILE ( @cnt <= @lt_1 )
    BEGIN

        IF ( SELECT COUNT(*)
             FROM   DataMigration.dbo.map_tab
             WHERE  flag = 'defaults'
                    AND [owner] = '853-Returnitems'
					AND Hybris_Table = 'ReturnEntry'
                    AND [RFO_Reference Table] <> 'NULL'
           ) > 0
            BEGIN
                SELECT  @sql_gen_1 = 'select distinct ''' + [owner]
                        + ''' as test_area, ''' + flag
                        + ''' as test_type, null as rfo_column, '''
                        + Hybris_Column + ''' as hybris_column, a.' + @HYB_key
                        + ', ' + hybris_column
                        + ', null as rfo_key, null as rfo_value
            from (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                from hybris.dbo.' + Hybris_Table + ' t, 
               Hybris.dbo.ReturnRequest q, 
                        hybris.dbo.users u,
						Hybris.dbo.countries c,
                        hybris.dbo.orders b
                        where t.p_returnrequest=q.pk
						and q.P_returnOrder=b.pk
						and b.userpk=u.pk
                        and u.P_country=8796100624418
                        and b.typepkstring=8796127723602
						and p_template is null  ) a
where ' + hybris_column + ' <> ''' + [RFO_Reference Table] + ''''
                FROM    ( SELECT    * ,
                                    ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                          FROM      DataMigration.dbo.map_tab
                          WHERE     flag = 'defaults'
                                    AND [RFO_Reference Table] <> 'NULL'
                                    AND [owner] = '853-Returnitems'
									AND Hybris_Table = 'ReturnEntry'
                        ) temp
                WHERE   rn = @cnt;
            END;

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
            --declare @err_cnt int
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
                        AND [owner] = '853-Returnitems'
						AND Hybris_Table = 'ReturnEntry'; 
            END;  

        INSERT  INTO DataMigration.dbo.dm_log
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

        SET @cnt = @cnt + 1;

    END;

UPDATE  DataMigration.dbo.map_tab
SET     [prev_run_err] = 0
WHERE   [owner] = '853-Returnitems'
AND Hybris_Table = 'ReturnEntry'
        AND flag = 'defaults'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '853-Returnitems'
		AND Hybris_Table = 'ReturnEntry'
                AND test_type = 'defaults' );

--update datamigration.dbo.map_tab
--set [prev_run_err] = 0
--where [owner] = '824-Autoship' and flag = 'defaults' and [prev_run_err] is null

SELECT  'Step-2 completed, Validation of transformed columns in progress' AS [Step-3 Validation] ,
        GETDATE() AS StartTime;

SET @cnt = 1;
SELECT  @lt_1 = COUNT(*)
FROM    DataMigration.dbo.map_tab
WHERE   flag = 'manual'
        AND rfo_column <> @RFO_key
        AND [owner] = '853-Returnitems'
		AND Hybris_Table = 'ReturnEntry';

WHILE @cnt <= @lt_1
    BEGIN

        SELECT  @sql_gen_1 = 'SELECT DISTINCT  ''' + [owner]
                + ''' as test_area, ''' + flag + ''' as test_type, '''
                + [RFO_Reference Table] + ''' as rfo_column, '''
                + Hybris_Column + ''' as hybris_column, A.' + @HYB_key
                + ' as hyb_key, A.Hyb_Trans_col as hyb_value, B.' + @RFO_key
                + ' as rfo_key, B.RFO_Trans_Col as rfo_value

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
            from hybris.dbo.' + Hybris_Table + ' t, 
			Hybris.dbo.ReturnRequest q,
            hybris.dbo.users u,
            hybris.dbo.orders b
            where t.p_returnrequest=q.pk
			and  q.p_returnorder=b.pk 
            and b.userpk=u.pk
			and u.P_country=8796100624418			
            and b.typepkstring=8796127723602 
			and p_template is null ) a
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + ') A  

LEFT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                    from hybris.dbo.' + Hybris_Table + ' t, 
                    Hybris.dbo.ReturnRequest q,
					hybris.dbo.users u,
					hybris.dbo.orders b
					where t.p_returnrequest=q.pk
					and  q.p_returnorder=b.pk 
					and b.userpk=u.pk
					and u.P_country=8796100624418			
					and b.typepkstring=8796127723602 
					and p_template is null ) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + '
UNION
SELECT DISTINCT  ''' + [owner] + ''', ''' + flag + ''', '''
                + [RFO_Reference Table] + ''', ''' + Hybris_Column + ''', A.'
                + @HYB_key + ', A.Hyb_Trans_col, B.' + @RFO_key
                + ', B.RFO_Trans_Col

FROM (SELECT a.' + @HYB_key + ', ' + Hybris_Column
                + ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
                            from hybris.dbo.' + Hybris_Table + ' t, 
                           Hybris.dbo.ReturnRequest q,
							hybris.dbo.users u,
							hybris.dbo.orders b
							where t.p_returnrequest=q.pk
							and  q.p_returnorder=b.pk 
							and b.userpk=u.pk
							and u.P_country=8796100624418			
							and b.typepkstring=8796127723602 
							and p_template is null ) a
except
SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + ') A  

RIGHT JOIN

(SELECT a.' + @RFO_key + ', ' + RFO_Column + ' as RFO_Trans_Col FROM '
                + RFO_Table + '
except
SELECT a.' + @HYB_key + ', ' + Hybris_Column
+ ' as Hyb_Trans_col FROM (select b.'+ @HYB_key + ' , t.' + Hybris_Column + '
            from hybris.dbo.' + Hybris_Table + ' t, 
            Hybris.dbo.ReturnRequest q,
            hybris.dbo.users u,
            hybris.dbo.orders b
            where t.p_returnrequest=q.pk
			and  q.p_returnorder=b.pk 
            and b.userpk=u.pk
			and u.P_country=8796100624418			
            and b.typepkstring=8796127723602 
			and p_template is null ) a) B
ON A.' + @HYB_key + '=B.' + @RFO_key + ''
        FROM    ( SELECT    * ,
                            ROW_NUMBER() OVER ( ORDER BY [owner] ) rn
                  FROM      DataMigration.dbo.map_tab
                  WHERE     flag = 'manual'
				  AND Hybris_Table = 'ReturnEntry'
                            AND rfo_column <> @RFO_key 
--and id not in (3,4,64,65) --order not migrated yet
                            AND [owner] = '853-Returnitems'
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
      --declare @err_cnt int
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
                        AND [owner] = '853-Returnitems'
						AND Hybris_Table = 'ReturnEntry'; 
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
WHERE   [owner] = '853-Returnitems'
        AND flag = 'manual'
		AND Hybris_Table = 'ReturnEntry'
        AND hybris_column NOT IN (
        SELECT DISTINCT
                hybris_column
        FROM    DataMigration..dm_log
        WHERE   test_area = '853-Returnitems'
		AND Hybris_Table = 'ReturnEntry'
                AND test_type = 'manual' );

SELECT  'VALIDATION COMPLETED' [Status] ,
        [total no of columns] ,
        [columns passed] ,
        [total no of columns] - [columns passed] AS [Required Analysis] ,
        GETDATE() AS EndTime
FROM    ( SELECT    COUNT(cnt) AS [columns passed]
          FROM      ( SELECT DISTINCT
                                hybris_column AS cnt
                      FROM      DataMigration.dbo.map_tab
                      WHERE     [owner] = '853-Returnitems'
					  AND Hybris_Table = 'ReturnEntry'
                                AND flag IN ( 'c2c', 'manual', 'defaults' )
                      EXCEPT
                      SELECT DISTINCT
                                hybris_column
                      FROM      DataMigration..dm_log
                      WHERE     test_area = '853-Returnitems'
					 -- AND Hybris_Table = 'ReturnEntry'
                    ) a
        ) tab1 ,
        ( SELECT    COUNT(id) AS [total no of columns]
          FROM      DataMigration.dbo.map_tab
          WHERE     [owner] = '853-Returnitems'
		  AND Hybris_Table = 'ReturnEntry'
                    AND flag IN ( 'c2c', 'manual', 'defaults' )
        ) tab2;