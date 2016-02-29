


	/******************************************
	Account Credit Card Types Clean Up  GP-2069
 AUTOSHIP  Credit Card Types Clean Up ( Pending )
 ORDERS Credit Card Types Clean Up ( Pending )
RETURNORDERS Credit Card Types Clean Up ( Pending)  
****************************************************/




/* 
Card Type	Card Number Prefix	Length
MASTERCARD	51-55	16
VISA	4	13, 16
AMEX	34 ,37	15
Diners Club/Carte Blanche	300-305,36,38	14
Discover	6011	16
enRoute	2014,2149	15
JCB	3	16
JCB	2131,1800	15

*/

USE RFOperations 
GO 


--Checking Account level CC. Diner is translated to discover so we should not see any record with Vendorid=8(diners)


SELECT  CCP.VendorID ,
        CCV.*
FROM    RFOperations.RFO_Accounts.CreditCardProfiles CCP ,
        RodanFieldsLive..AccountPaymentMethods app ,
        RFOperations.RFO_Reference.CreditCardVendors CCV
WHERE   CCP.PaymentProfileID = app.AccountPaymentMethodID
        AND CCV.VendorID = CCP.VendorID
        AND CCV.VendorID = 8;

		--Checking Autoship level CC. Diner is translated to discover so we should not see any record with Vendorid=8(diners)
SELECT  AP.* ,
        CCV.Name
FROM    RFOperations.Hybris.AutoshipPayment AP ,
        RodanFieldsLive..OrderPayments OP ,
        RFOperations.Hybris.Autoship A ,
        RFOperations.RFO_Reference.CreditCardVendors CCV
WHERE   AP.AutoshipPaymentID = OP.OrderPaymentID
        AND A.AutoshipID = AP.AutoshipID
        AND CCV.VendorID = AP.VendorID
        AND CCV.VendorID = 8;

SELECT  AP.* ,
        CCV.Name ,
        *
FROM    RodanFieldsLive..OrderPayments OP ,
        RFOperations.Hybris.Orders A ,
        RFOperations.RFO_Reference.CreditCardVendors CCV
WHERE   AP.AutoshipPaymentID = OP.OrderPaymentID
        AND A.OrderID = AP.AutoShipID
        AND CCV.VendorID = AP.VendorID
        AND CCV.VendorID = 8;



		


/*=============================================================================================
    ACCOUNT LEVEL Credit Cards Types  CLEANUP
==============================================================================================  */

/* Checking the total Credit Cards types with its counts before updating the proper user of Funcions */
		 
         SELECT COUNT(*) Counts , CCP.VendorID , CCV.Name
         FROM   RodanFieldsLive..AccountPaymentMethods app
                INNER JOIN RFOperations.RFO_Accounts.PaymentProfiles PP ON PP.PaymentProfileID = app.AccountPaymentMethodID
                INNER JOIN RFOperations.RFO_Accounts.CreditCardProfiles CCP ON CCP.PaymentProfileID = PP.PaymentProfileID
                INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = PP.AccountID
                INNER JOIN RFOperations.RFO_Accounts.AccountRF arf ON arf.AccountID = ab.AccountID
                JOIN RFOperations.RFO_Reference.CreditCardVendors CCV ON CCV.VendorID = CCP.VendorID			

         WHERE  ab.CountryID = 236
                AND ( arf.SoftTerminationDate IS NULL
                      OR arf.SoftTerminationDate >= DATEADD(M, -18, GETDATE())
                    )
         GROUP BY CCP.VendorID ,
                CCV.Name; 
				--It holds Dinner Credit Types before Updating it.

	 /*Taking Back up of previoius type with newtypes to update the proper Credit Card Types.*/
;
WITH    CTE
          AS ( SELECT   app.AccountPaymentMethodID ,
                        app.AccountID ,
						RFOperations.dbo.DecryptTripleDES(app.AccountNumber)AS Number ,
                        ISNULL(RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(app.AccountNumber)),'Unknown') AS CCtype ,						
                        CCP.VendorID AS Old_VendorID
   --INTO     #TempAccountProfile
               FROM     RodanFieldsLive..AccountPaymentMethods app
                        INNER JOIN RFOperations.RFO_Accounts.PaymentProfiles PP ON PP.PaymentProfileID = app.AccountPaymentMethodID
                        INNER JOIN RFOperations.RFO_Accounts.CreditCardProfiles CCP ON CCP.PaymentProfileID = PP.PaymentProfileID
                        INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = PP.AccountID
                        INNER JOIN RFOperations.RFO_Accounts.AccountRF arf ON arf.AccountID = ab.AccountID
               WHERE    ab.CountryID = 236
                        AND ( arf.SoftTerminationDate IS NULL
                              OR arf.SoftTerminationDate >= DATEADD(M, -18,GETDATE())
                            )
             ),
        CTE_1
          AS ( SELECT   AccountPaymentMethodID ,
                        AccountID ,
						T.Number,
                        CCtype ,
                        Old_VendorID ,
                        ccv.VendorID AS New_vendorId
               FROM     CTE T
                        JOIN RFOperations.RFO_Reference.CreditCardVendors ccv ON ccv.Name = T.CCtype
             )
    SELECT  CASE WHEN t.CCtype = 'diners' THEN 5
                 ELSE t.New_vendorId
            END AS New_vendorId ,
            t.Old_VendorID AS Original_VendorID ,
            t.CCtype ,
            ccp.CreditCardProfilesID
    INTO    RFOperations..Cleanup_CreditcardProfiles_Cleanup
    FROM    RFOperations.RFO_Accounts.CreditCardProfiles ccp
            JOIN CTE_1 t ON t.AccountPaymentMethodID = ccp.PaymentProfileID
            JOIN RFOperations.RFO_Reference.CreditCardVendors ccv ON ISNULL(t.CCtype,'Unknown') = ccv.Name
			

			--SELECT * FROM  RFOperations..Cleanup_CreditcardProfiles_Cleanup
			--DROP TABLE  RFOperations..Cleanup_CreditcardProfiles_Cleanup


 /* Updating the Proper VenderId Using Proper VenderId(Credit Card type) using the back up.*/ 
	
		
BEGIN TRAN 
UPDATE  RFOperations.RFO_Accounts.CreditCardProfiles
SET     --SELECT DISTINCT
        VendorID = New_vendorId ,
        ChangedByApplication = 'USLaunch-Hybris'
FROM    RFOperations.RFO_Accounts.CreditCardProfiles ccp
        JOIN  RFOperations..Cleanup_CreditcardProfiles_Cleanup t ON t.CreditCardProfilesID = ccp.PaymentProfileID
													
COMMIT 
ROLLBACK

	
	
		
		/* Checking the Updated results after updating the proper.*/
		 
         SELECT COUNT(*) Counts , CCP.VendorID , b.Name
         FROM   RodanFieldsLive..AccountPaymentMethods app
                INNER JOIN RFOperations.RFO_Accounts.PaymentProfiles PP ON PP.PaymentProfileID = app.AccountPaymentMethodID
                INNER JOIN RFOperations.RFO_Accounts.CreditCardProfiles CCP ON CCP.PaymentProfileID = PP.PaymentProfileID
                INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = PP.AccountID
                INNER JOIN RFOperations.RFO_Accounts.AccountRF arf ON arf.AccountID = ab.AccountID
                JOIN RFOperations.RFO_Reference.CreditCardVendors b ON b.VendorID = CCP.VendorID			

         WHERE  ab.CountryID = 236
                AND ( arf.SoftTerminationDate IS NULL
                      OR arf.SoftTerminationDate >= DATEADD(M, -18, GETDATE())
                    )
         GROUP BY CCP.VendorID ,
                b.Name; 
				--There should not be any Dinner Credit Cards types .



/* Delta Validation which updates the RFL Credit Card Types and will see its follow till RFO .*/


				-- Retalted SP: [ETL].[USPExceptStagingCardProfiles]
			--Modificaton within Sp<

                WITH    CTE
                          AS ( SELECT   apm.AccountPaymentMethodID ,
                                        dbo.GetCreditCardType(dbo.DecryptTripleDES(apm.AccountNumber)) AS CCtype
                               FROM     ETL.StagingCardProfiles a
                                        JOIN [ETL].[synRFLiveAccountPaymentMethods] apm ON a.PaymentProfileID = apm.AccountPaymentMethodID
                             )
                    SELECT  AccountPaymentMethodID ,
                            CASE WHEN CCtype = 'diners' THEN 'discover'
                                 WHEN CCtype IS NULL THEN 'unknown'
                                 ELSE CCtype
                            END AS CCtype
                    INTO    #Temp_CCType
                    FROM    CTE
				            

                    --Updating Cardtype and displaynumber   
          
                UPDATE  ETL.StagingCardProfiles
                SET     DisplayNumber = CASE WHEN dbo.MaskDisplayNumber(apm.AccountNumber) = ''
                                             THEN NULL
                                             ELSE dbo.MaskDisplayNumber(apm.AccountNumber)
                                        END ,
                        VendorId = ccv.VendorID
                FROM    ETL.StagingCardProfiles a
                        JOIN [ETL].[synRFLiveAccountPaymentMethods] apm ON a.PaymentProfileID = apm.AccountPaymentMethodID
                        JOIN #Temp_CCType t ON t.AccountPaymentMethodID = apm.AccountPaymentMethodID
                        JOIN RFOperations.RFO_Reference.CreditCardVendors ccv ON ccv.Name = t.CCType;


    

/* First Approach to Update RFL Credit Cards */

SELECT TOP 500 * INTO #T FROM RodanFieldsLive..AccountPaymentMethodsAudit ORDER BY AuditDate DESC 

SELECT ROW_NUMBER() OVER (ORDER BY AuditID) AS ROWN ,* INTO RFOperations..Cleanup_CreditcardProfiles_BackUp  FROM #T

SELECT  AccountPaymentMethodID
FROM    #t1
WHERE   ROWN BETWEEN 1 AND 100
 --Updte RFL  RodanFieldsLive..AccountPaymentMethods AccountNumber=VisaSampleNumber Matching AccountPaymentMethodID.
SELECT  AccountPaymentMethodID
FROM    #t1
WHERE   ROWN BETWEEN 101 AND 200
 --Updte RFL  RodanFieldsLive..AccountPaymentMethods AccountNumber=MasterCard's Number Matching AccountPaymentMethodID.
SELECT  AccountPaymentMethodID
FROM    #t1
WHERE   ROWN BETWEEN 201 AND 300
 --Updte RFL  RodanFieldsLive..AccountPaymentMethods AccountNumber=Amex Sample Number Matching AccountPaymentMethodID.
SELECT  AccountPaymentMethodID
FROM    #t1
WHERE   ROWN BETWEEN 301 AND 400
  --Updte RFL  RodanFieldsLive..AccountPaymentMethods AccountNumber=Dinner Sample Number Matching AccountPaymentMethodID.
SELECT  AccountPaymentMethodID
FROM    #t1
WHERE   ROWN BETWEEN 401 AND 500
 --Updte RFL  RodanFieldsLive..AccountPaymentMethods AccountNumber=Discover Sample Number Matching AccountPaymentMethodID.




/* 2nd Approach To modify RFL Credit Cards types */



;
WITH    Visa
          AS ( SELECT TOP 100
                        a.AccountPaymentMethodID ,
                        b.AccountNumber ,
                        a.AuditDate,
						'Visa' AS CardType
               FROM     RodanFieldsLive..AccountPaymentMethodsAudit a
                        JOIN RodanFieldsLive..AccountPaymentMethods b ON a.AccountPaymentMethodID = b.AccountPaymentMethodID
             ),
--SELECT * FROM Visa
        MasterCard
          AS ( SELECT TOP 100
                        a.AccountPaymentMethodID ,
                        b.AccountNumber ,
                        a.AuditDate,
						'MasterCard' AS CardType
               FROM     RodanFieldsLive..AccountPaymentMethodsAudit a
                        JOIN RodanFieldsLive..AccountPaymentMethods b ON a.AccountPaymentMethodID = b.AccountPaymentMethodID
               WHERE    a.AccountPaymentMethodID NOT IN (
                        SELECT  AccountPaymentMethodID
                        FROM    Visa )
             ),
        Amex
          AS ( SELECT TOP 100
                        a.AccountPaymentMethodID ,
                        b.AccountNumber ,
                        a.AuditDate,
						'Amex' AS CardType
               FROM     RodanFieldsLive..AccountPaymentMethodsAudit a
                        JOIN RodanFieldsLive..AccountPaymentMethods b ON a.AccountPaymentMethodID = b.AccountPaymentMethodID
               WHERE    a.AccountPaymentMethodID NOT IN (
                        SELECT  AccountPaymentMethodID
                        FROM    Visa )
                        AND a.AccountPaymentMethodID NOT IN (
                        SELECT  AccountPaymentMethodID
                        FROM    MasterCard )
             ),
        Dinner
          AS ( SELECT TOP 100
                        a.AccountPaymentMethodID ,
                        b.AccountNumber ,
                        a.AuditDate,
						'Dinner' AS CardType
               FROM     RodanFieldsLive..AccountPaymentMethodsAudit a
                        JOIN RodanFieldsLive..AccountPaymentMethods b ON a.AccountPaymentMethodID = b.AccountPaymentMethodID
               WHERE    a.AccountPaymentMethodID NOT IN (
                        SELECT  AccountPaymentMethodID
                        FROM    Visa )
                        AND a.AccountPaymentMethodID NOT IN (
                        SELECT  AccountPaymentMethodID
                        FROM    MasterCard )
                        AND a.AccountPaymentMethodID NOT IN (
                        SELECT  AccountPaymentMethodID
                        FROM    Amex )
             ),
        Discover
          AS ( SELECT TOP 100
                        a.AccountPaymentMethodID ,
                        b.AccountNumber ,
                        a.AuditDate,
						'Discover' AS CardType
               FROM     RodanFieldsLive..AccountPaymentMethodsAudit a
                        JOIN RodanFieldsLive..AccountPaymentMethods b ON a.AccountPaymentMethodID = b.AccountPaymentMethodID
               WHERE    a.AccountPaymentMethodID NOT IN (
                        SELECT  AccountPaymentMethodID
                        FROM    Visa )
                        AND a.AccountPaymentMethodID NOT IN (
                        SELECT  AccountPaymentMethodID
                        FROM    MasterCard )
                        AND a.AccountPaymentMethodID NOT IN (
                        SELECT  AccountPaymentMethodID
                        FROM    Amex )
						 AND a.AccountPaymentMethodID NOT IN (
                        SELECT  AccountPaymentMethodID
                        FROM    Dinner )
             )
    SELECT  * --INTO  RFOperations..Cleanup_CreditcardProfiles_BackUp
    FROM    Visa
    UNION
    SELECT  *
    FROM    Amex
    UNION
    SELECT  *
    FROM    MasterCard
    UNION
    SELECT  *
    FROM    Dinner
	 UNION
    SELECT  *
    FROM    Discover;




	UPDATE a
	SET AuditDate >= DATEADD(DAY, -20, GETDATE())
	FROM  RodanFieldsLive..AccountPaymentMethodsAudit a 
	JOIN RFOperations..Cleanup_CreditcardProfiles_BackUp b ON a.AccountPaymentMethodID = b.AccountPaymentMethodID;


	
UPDATE  a
SET     AccountNumber = CASE WHEN b.CardType = 'Visa'
                             THEN RFOperations.dbo.EncryptTripleDES(4111111111111111)
                             WHEN b.CardType = 'Amex'
                             THEN RFOperations.dbo.EncryptTripleDES(378282246310005)
                             WHEN b.CardType = 'MasterCard'
                             THEN RFOperations.dbo.EncryptTripleDES(5555555555554444)
                             WHEN b.CardType = 'Dinner'
                             THEN RFOperations.dbo.EncryptTripleDES(30565763333331)
                             WHEN b.CardType = 'Discover'
                             THEN RFOperations.dbo.EncryptTripleDES(6011546565684562)
                        END
FROM    RodanFieldsLive..AccountPaymentMethods a
        JOIN RFOperations..Cleanup_CreditcardProfiles_BackUp b ON a.AccountPaymentMethodID = b.AccountPaymentMethodID;

;

/* Getting Results of Modified Credit Card types in RFO before BOOMI Sync */
 
         SELECT COUNT(*) Counts , CCP.VendorID , CCV.Name,ccp.displaynumber
		-- ccp.*
         FROM   RodanFieldsLive..AccountPaymentMethods app
                INNER JOIN RFOperations.RFO_Accounts.PaymentProfiles PP ON PP.PaymentProfileID = app.AccountPaymentMethodID
                INNER JOIN RFOperations.RFO_Accounts.CreditCardProfiles CCP ON CCP.PaymentProfileID = PP.PaymentProfileID
                INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = PP.AccountID
                INNER JOIN RFOperations.RFO_Accounts.AccountRF arf ON arf.AccountID = ab.AccountID
                JOIN RFOperations.RFO_Reference.CreditCardVendors CCV ON CCV.VendorID = CCP.VendorID
				JOIN RFOperations..Cleanup_CreditcardProfiles_BackUp d ON d.AccountPaymentMethodID=app.AccountPaymentMethodID

         WHERE  ab.CountryID = 236
                AND ( arf.SoftTerminationDate IS NULL
                      OR arf.SoftTerminationDate >= DATEADD(M, -18, GETDATE())
                    ) AND CAST(ccp.ServerModifiedDate AS DATE)=CAST(GETDATE()AS DATE)
         GROUP BY CCP.VendorID ,
                CCV.Name,ccp.displaynumber; 
				
/* Before Boomi Sync 
				
				Counts	VendorID	Name
143	1	mastercard
284	2	visa
43	3	amex
27	5	discover
1	12	unknown
2	13	Invalid
********/


/* After Boomi Run to get the final result */


        
         SELECT COUNT(*) Counts , CCP.VendorID , CCV.Name
         FROM   RodanFieldsLive..AccountPaymentMethods app
                INNER JOIN RFOperations.RFO_Accounts.PaymentProfiles PP ON PP.PaymentProfileID = app.AccountPaymentMethodID
                INNER JOIN RFOperations.RFO_Accounts.CreditCardProfiles CCP ON CCP.PaymentProfileID = PP.PaymentProfileID
                INNER JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = PP.AccountID
                INNER JOIN RFOperations.RFO_Accounts.AccountRF arf ON arf.AccountID = ab.AccountID
                JOIN RFOperations.RFO_Reference.CreditCardVendors CCV ON CCV.VendorID = CCP.VendorID
				JOIN RFOperations..Cleanup_CreditcardProfiles_BackUp d ON d.AccountPaymentMethodID=app.AccountPaymentMethodID
				JOIN RFOperations.ETL.StagingCardProfiles st ON st.PaymentProfileID=pp.PaymentProfileID
         WHERE  ab.CountryID = 236
                AND ( arf.SoftTerminationDate IS NULL
                      OR arf.SoftTerminationDate >= DATEADD(M, -18, GETDATE())
                    )
         GROUP BY CCP.VendorID ,
                CCV.Name; 


/* After Boomi Sync 
				
				This results should be the exact count of modified with RFL and should not be any Dinner Credit Card Types.
*/

 

/*=============================================================================================
AUTOSHIP LEVEL CC CLEANUP
==============================================================================================  */
	 --DROP TABLE  #Tempautoshipprofile

 ;
 WITH   CTE
          AS ( SELECT   OP.OrderPaymentID ,
                        A.AccountID ,
                        RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)) AS CCtype ,
                        AP.VendorID AS Old_VendorId
               FROM     RodanFieldsLive..OrderPayments OP
                        INNER JOIN RFOperations.Hybris.AutoshipPayment AP ON AP.AutoshipPaymentID = OP.OrderPaymentID
                        INNER JOIN RFOperations.Hybris.Autoship A ON A.AutoshipID = AP.AutoshipID
                        JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = A.AccountID
                        JOIN RFOperations.RFO_Accounts.AccountRF arf ON arf.AccountID = ab.AccountID
               WHERE    A.CountryID = 236
                        AND ( arf.SoftTerminationDate IS NULL
                              OR arf.SoftTerminationDate >= DATEADD(M, -18,
                                                              GETDATE())
                            )
             ),
        CTE_1
          AS ( SELECT   OrderPaymentID ,
                        AccountID ,
                        CCtype ,
                        Old_VendorId ,
                        ccv.VendorID AS New_VendorID
               FROM     CTE T
                        JOIN RFOperations.RFO_Reference.CreditCardVendors ccv ON ISNULL(T.CCtype,'Unknown') = ccv.Name
             )
    SELECT  VendorID = ( CASE WHEN T.CCtype = 'diners' THEN 5
                              ELSE T.New_VendorID
                         END ) ,
            T.Old_VendorId ,
            AP.AutoshipPaymentID
    INTO    DataMigration..Cleanup_AutoshipPayment
    FROM    CTE_1 T
            INNER JOIN RFOperations.Hybris.AutoshipPayment AP ON AP.AutoshipPaymentID = T.OrderPaymentID
    WHERE   T.Old_VendorId <> T.New_VendorID

		
		

   BEGIN TRAN 

   UPDATE   RFOperations.Hybris.AutoshipPayment
   SET      VendorID = C.VendorID ,
            ChangedByApplication = 'USLaunch-Hybris'
   FROM     RFOperations.Hybris.AutoshipPayment AP
            JOIN DataMigration..Cleanup_AutoshipPayment_Cleanup C ON C.AutoshipPaymentID = AP.AutoshipPaymentID		

   COMMIT 
		 --DROP TABLE  #Tempautoshipprofile
  
    /*=============================================================================================
    ORDER LEVEL CC CLEANUP
	==============================================================================================  */ 
  
   --DROP TABLE  #TempOrdersProfile

   SELECT   OP.OrderPaymentID ,
            O.AccountID ,
            RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)) AS CCtype
   INTO     #TempOrdersProfile
   FROM     RodanFieldsLive..OrderPayments OP
            INNER JOIN RFOperations.Hybris.OrderPayment ROP ON ROP.OrderPaymentID = OP.OrderPaymentID
            INNER JOIN RFOperations.Hybris.Orders O ON O.OrderID = ROP.OrderID
            INNER JOIN RFOperations.ETL.OrderDate od ON od.Orderid = O.OrderID
   WHERE    O.CountryID = 236
            AND od.Startdate >= '2014-08-01'


 /*=============================================================================================
  Backup for Cleanup
  ==============================================================================================  */

   SELECT DISTINCT
            VendorID = ( CASE WHEN t.CCtype = 'diners' THEN 5
                              ELSE ccv.VendorID
                         END ) ,
            ccv.VendorID AS Original_VendorID ,
            ccp.OrderPaymentID
   INTO     DataMigration..Cleanup_OrderPayment_Cleanup
   FROM     RFOperations.Hybris.OrderPayment OP
            JOIN #TempOrdersProfile t ON t.OrderPaymentID = OP.OrderPaymentID
            JOIN RFOperations.RFO_Reference.CreditCardVendors ccv ON ISNULL(t.CCtype,
                                                              'Unknown') = ccv.Name	
        

   BEGIN TRAN 

   UPDATE   RFOperations.Hybris.OrderPayment
   SET      VendorID = ( CASE WHEN t.CCtype = 'diners' THEN 5
                              ELSE ccv.VendorID
                         END ) ,
            ChangedByApplication = 'USLaunch-Hybris'
   FROM     RFOperations.Hybris.OrderPayment OP
            JOIN #TempOrdersProfile t ON t.OrderPaymentID = OP.OrderPaymentID
            JOIN RFOperations.RFO_Reference.CreditCardVendors ccv ON ISNULL(t.CCtype,
                                                              'Unknown') = ccv.Name		

   COMMIT 
    --DROP TABLE  #TempOrdersProfile
   /*=============================================================================================
    RETURN LEVEL CC CLEANUP
	==============================================================================================  */	   
	      --DROP TABLE  #TempReturnProfile

   SELECT   OP.OrderPaymentID ,
            O.AccountID ,
            RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(OP.AccountNumber)) AS CCtype
   INTO     #TempReturnProfile
   FROM     RodanFieldsLive..OrderPayments OP
            INNER JOIN RFOperations.Hybris.ReturnPayment ROP ON ROP.ReturnPaymentId = OP.OrderPaymentID
            INNER JOIN RFOperations.Hybris.ReturnOrder O ON O.ReturnOrderID = ROP.ReturnOrderID
   WHERE    O.CountryID = 236


   /*=============================================================================================
	Backup for Cleanup
	==============================================================================================  */

   SELECT DISTINCT
            VendorID = ( CASE WHEN t.CCtype = 'diners' THEN 5
                              ELSE ccv.VendorID
                         END ) ,
            ccv.VendorID AS Original_VendorID ,
            ccp.ReturnPaymentId
   INTO     Cleanup_ReturnPayment_Cleanup
   FROM     RFOperations.Hybris.ReturnPayment OP
            JOIN #TempReturnProfile t ON t.OrderPaymentID = OP.ReturnPaymentId
            JOIN RFOperations.RFO_Reference.CreditCardVendors ccv ON ISNULL(t.CCtype,
                                                              'Unknown') = ccv.Name	
        
        

   BEGIN TRAN 

   UPDATE   RFOperations.Hybris.ReturnPayment
   SET      VendorID = ( CASE WHEN t.CCtype = 'diners' THEN 5
                              ELSE ccv.VendorID
                         END ),
			ChangedByApplication = 'USLaunch-Hybris'
   FROM     RFOperations.Hybris.ReturnPayment OP
            JOIN #TempReturnProfile t ON t.OrderPaymentID = OP.ReturnPaymentId
            JOIN RFOperations.RFO_Reference.CreditCardVendors ccv ON ISNULL(t.CCtype,
                                                              'Unknown') = ccv.Name		

   COMMIT 
    
	   --DROP TABLE  #TempReturnProfile




	

