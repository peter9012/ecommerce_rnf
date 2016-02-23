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
        CCV.Name;
SELECT  *
FROM    RodanFieldsLive..OrderPayments OP ,
        RFOperations.Hybris.Orders A ,
        RFOperations.RFO_Reference.CreditCardVendors CCV
WHERE   AP.AutoshipPaymentID = OP.OrderPaymentID
        AND A.OrderID = AP.AutoShipID
        AND CCV.VendorID = AP.VendorID
        AND CCV.VendorID = 8;



		

   /*=============================================================================================
    ACCOUNT LEVEL CC CLEANUP
	==============================================================================================  */

	 --DROP TABLE  #TempAccountProfile

WITH    CTE
          AS ( SELECT   app.AccountPaymentMethodID ,
                        app.AccountID ,
					--	RFOperations.dbo.DecryptTripleDES(app.AccountNumber) AS Numbers,
                        RFOperations.dbo.GetCreditCardType(RFOperations.dbo.DecryptTripleDES(app.AccountNumber)) AS CCtype ,
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
                        CCtype ,
                        Old_VendorID ,
                        ccv.VendorID AS New_vendorId
               FROM     CTE T
                        JOIN RFOperations.RFO_Reference.CreditCardVendors ccv ON ccv.Name = T.CCtype
             )
    SELECT
           CASE WHEN t.CCtype = 'diners' THEN 5
                              ELSE T.New_vendorId
                         END AS New_vendorId ,
            t.Old_VendorID AS Original_VendorID ,
            t.CCtype ,
            ccp.CreditCardProfilesID
    INTO    RFOperations.dbo.Cleanup_CreditcardProfiles_Cleanup
    FROM    RFOperations.RFO_Accounts.CreditCardProfiles ccp
            JOIN CTE_1 t ON t.AccountPaymentMethodID = ccp.PaymentProfileID
            JOIN RFOperations.RFO_Reference.CreditCardVendors ccv ON ISNULL(t.CCtype,
                                                              'Unknown') = ccv.Name
		
		
		--DROP TABLE RFOperations.dbo.Cleanup_CreditcardProfiles_Cleanup
  
  SELECT    Original_VendorID ,
            new_VendorID ,
            b.Name AS Old_Vender ,
            c.Name AS Now_Vender 
  FROM      RFOperations.dbo.Cleanup_CreditcardProfiles_Cleanup a
            JOIN RFOperations.RFO_Reference.CreditCardVendors b ON a.Original_VendorID = b.VendorID
            JOIN RFOperations.RFO_Reference.CreditCardVendors c ON a.new_VendorID = c.VendorID
  WHERE     New_vendorid = 13
            AND New_vendorId <> Original_VendorID AND b.Name='visa'
  GROUP BY  Original_VendorID ,
            new_VendorID ,
            b.Name ,
            c.Name;


 -------------Updating account CC Profiles            
       
              
   BEGIN TRAN 
   UPDATE   RFOperations.RFO_Accounts.CreditCardProfiles
   SET      --SELECT DISTINCT
            VendorID = New_vendorId ,
            ChangedByApplication = 'USLaunch-Hybris'
   FROM     RFOperations.RFO_Accounts.CreditCardProfiles ccp
            JOIN RFOperations.dbo.Cleanup_CreditcardProfiles_Cleanup t ON t.CreditCardProfilesID = ccp.PaymentProfileID
                     WHERE t.New_vendorId <> T.Original_VendorID
                                                                                         
   COMMIT 



         SELECT COUNT(*) Counts ,
                CCP.VendorID ,
                b.Name
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
    
    





	

