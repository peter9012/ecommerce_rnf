USE RFOperations
GO 

CREATE PROCEDURE usp_CRjob_FullRestoringBack_Orders_Accounts_Oentries
AS 
BEGIN

UPDATE  a
SET     a.p_ccfailurecount = b.p_ccfailurecount ,
        a.p_istaxcalculationfailed = b.p_istaxcalculationfailed ,
        a.totaltaxvalues = b.totaltaxvalues ,
        a.p_schedulingdate = b.p_schedulingdate ,
        a.p_lastprocessingdate = b.p_lastprocessingdate ,
        a.subtotal = b.subtotal ,
        a.totalprice = b.totalprice ,
        a.totaltax = b.totaltax ,
        a.p_totalcv = b.p_totalcv ,
        a.p_totalqv = b.p_totalqv ,
        a.p_cancelationdate = b.p_cancelationdate ,
        a.p_totalamountwithouttax = b.p_totalamountwithouttax ,
        a.p_commissiondate = b.p_commissiondate ,
        a.statuspk = b.statuspk ,
        a.p_taxexempt = b.p_taxexempt ,
        a.p_taxstatus = b.p_taxstatus ,
        a.modifiedTS = b.modifiedTS ,
        a.createdTS = b.createdTS,
		a.p_active=b.p_active
FROM    Hybris..orders a
        JOIN Hybris..orders_bkp b ON a.pk = b.pk		
		AND a.p_template=1


      


 

		SELECT 'Orders Backup Completed' AS Step, GETDATE() AS CompletionTime


UPDATE  a
SET     a.totalprice = b.totalprice ,
        a.p_wholesaleprice = b.p_wholesaleprice ,
        a.p_cv = b.p_cv ,
        a.p_qv = b.p_qv ,
        a.p_taxableprice = b.p_taxableprice ,
        a.productpk = b.productpk ,
        a.quantity = b.quantity ,
        a.p_quantitystatus = b.p_quantitystatus ,
        a.baseprice = b.baseprice ,
        a.taxvalues = b.taxvalues ,
        a.entrynumber = b.entrynumber ,
        a.unitpk = b.unitpk ,
        a.modifiedTS = b.modifiedTS ,
        a.createdTS = b.createdTS
FROM    Hybris..orderentries a
        JOIN Hybris..orderentries_bkp b ON a.PK = b.PK
		JOIN Hybris..orders_bkp c ON c.pk=b.orderpk AND c.p_template=1;

		SELECT 'OrdersEntries Backup Completed' AS Step, GETDATE() AS CompletionTime


UPDATE  a
SET     a.p_rfaccountid = b.p_rfaccountid ,
        a.p_rfaccountnumber = b.p_rfaccountnumber ,
        a.p_expirationdate = b.p_expirationdate ,
        a.p_renewlatertime = b.p_renewlatertime ,
        a.p_accountstatus = b.p_accountstatus ,
        a.p_pwsaccessenddate = a.p_pwsaccessenddate ,
        a.p_pwscustomername = b.p_pwscustomername ,
        a.p_consultantsince = b.p_consultantsince ,
        a.p_terminationreason = b.p_terminationreason ,
        a.p_enrolledaspc = b.p_enrolledaspc ,
        a.p_enrolledascrp = b.p_enrolledascrp ,
        a.p_enrolledaspulse = b.p_enrolledaspulse ,
        a.p_sponsorid = b.p_sponsorid ,
        a.uniqueid = b.uniqueid ,
        a.p_type = b.p_type ,
        a.p_hardterminationdate = b.p_hardterminationdate
FROM    Hybris..users a
        JOIN Hybris..users_bkp b ON a.PK = b.pk 

		SELECT 'Users Backup Completed' AS Step, GETDATE() AS CompletionTime



		  UPDATE  ab
        SET     AccountStatusID = 1 ,
                ab.ServerModifiedDate = GETDATE()
        FROM    RFOperations.RFO_Accounts.AccountRF ar
                INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = ar.AccountID
        WHERE   CountryID = 236
                AND Active = 1
                AND AccountStatusID <> 1;



        DECLARE @ActivePK BIGINT = ( SELECT TOP 1
                                            a.PK
                                     FROM   Hybris.dbo.enumerationvalues a
                                            JOIN Hybris.dbo.users u ON u.p_accountstatus = a.PK
                                     WHERE  Code = 'Active'
                                   );

        UPDATE  u
        SET     u.p_accountstatus = @ActivePK
        FROM    RFOperations.RFO_Accounts.AccountRF ar
                INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = ar.AccountID
                JOIN Hybris.dbo.users u ON p_rfaccountid = CAST(ab.AccountID AS VARCHAR)
        WHERE   CountryID = 236
                AND Active = 1
                AND AccountStatusID = 1
                AND p_accountstatus <> @ActivePK;

		END
        