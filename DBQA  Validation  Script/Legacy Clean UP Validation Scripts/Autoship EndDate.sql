------Autoship  EndDate ------

------ RFO Autoships END Date Cleanup Script ------

USE RFOperations
GO 



/* Checking If  Active Templates EndDate Is not NULL */


SELECT  COUNT(*)--Get the total Counts Should be Zero after update.
FROM    RFOperations.Hybris.Autoship
WHERE   Active = 1
        AND CountryID = 236
        AND EndDate IS NOT NULL; 

		--1,026,758

/* Making sure that RFL Autoship may have Orderstatus: Submitted(not only Template Submitted)
, Picking those templates from Autoship in RFO  if Account is Active in in RFL  . */


DECLARE @Getdate DATETIME2 = GETDATE();
SELECT  a.AutoshipID ,
        a.Active ,
        1 AS New_ActiveFlag ,
        a.ServerModifiedDate ,
        @Getdate AS newServerModifiedDate
INTO    RFOperations.dbo.Cleanup_autoship_enddate_1
FROM    RFOperations.Hybris.Autoship a
        JOIN RodanFieldsLive.dbo.Orders b ON a.AutoshipID = b.OrderID
        JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = a.AccountID
WHERE   ac.Active = 1
        AND b.OrderStatusID = 4
        AND a.CountryID = 236
		

		/* Taking Counts of those missing Autoship with Legacy Issues*/


		SELECT COUNT(*) FROM  RFOperations.dbo.Cleanup_autoship_enddate_1
		--31 

		/* Updating Those Templates Active in RFO which 
		was Submitted Status(Not TemplateSubmitted)
		and Active Account in RFL   */


BEGIN TRAN 

DECLARE @Getdate DATETIME2 = GETDATE();
UPDATE  a
SET     Active = 1 ,
        a.ServerModifiedDate = @Getdate
FROM    RFOperations.Hybris.Autoship a
        JOIN RodanFieldsLive.dbo.Orders b ON a.AutoshipID = b.OrderID
        JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = a.AccountID
WHERE   ac.Active = 1
        AND b.OrderStatusID = 4
        AND a.CountryID = 236

COMMIT
ROLLBACK

		/* Validation Legacy issue fixed in above query:*/


SELECT COUNT(*) --Should be equal counts of backup table RFOperations.dbo.Cleanup_autoship_enddate_1
FROM    RFOperations.Hybris.Autoship a
        JOIN RodanFieldsLive.dbo.Orders b ON a.AutoshipID = b.OrderID
        JOIN RodanFieldsLive.dbo.Accounts ac ON ac.AccountID = a.AccountID
WHERE   ac.Active = 1
        AND b.OrderStatusID = 4
        AND a.CountryID = 236
		



		/* Updating All Active Templates EndDate=NULL in RFO */

BEGIN TRAN 
DECLARE @Getdate DATETIME2 = GETDATE();
UPDATE  RFOperations.Hybris.Autoship
SET     EndDate = NULL 
        ,ServerModifiedDate = @Getdate
WHERE   Active = 1
        AND CountryID = 236

		--1027597 Affected Records

COMMIT
ROLLBACK




/* Validating After Update if there is any Active Templates having EndDate */



SELECT  COUNT(*)-- Should be Zero after update.
FROM    RFOperations.Hybris.Autoship
WHERE   Active = 1
        AND CountryID = 236
        AND EndDate IS NOT NULL; 






--- ETL Modificatons : StoredProcedure [ETL].[uspStageAutoships] 




/*BOOMI-501 Next run date*/


		 /*GP-2077 Updating enddate to null for active autoship*/

            UPDATE  a
            SET     a.EndDate = CASE WHEN a.Active = 1 THEN NULL
                                     WHEN a.Active <> 1
                                          AND a.NextRundate IS NULL
                                     THEN au.EndDate
                                     ELSE a.NextRundate
                                END
            FROM    ETL.StagingAutoship a
                    LEFT OUTER JOIN Hybris.Autoship au ON a.AutoshipID = au.AutoshipID





					
                    SELECT a.*
                    FROM    RFOperations.Hybris.Autoship a
					--JOIN RFOperations.Hybris.orders o ON o.AutoShipID = a.AutoshipID
                    WHERE   a.CountryID = 236
                           AND EndDate IS NULL
                            AND Active = 1
                            AND NextRunDate IS NULL 
							ORDER BY a.CompletionDate DESC

                    SELECT o.*
                    FROM    RFOperations.Hybris.Autoship a
					JOIN RFOperations.Hybris.orders o ON o.AutoShipID = a.AutoshipID
                    WHERE   a.CountryID = 236
                            AND EndDate IS NULL
                            AND Active = 1
                            AND NextRunDate IS NULL 
							ORDER BY a.CompletionDate DESC

						

					/******************************************

TestScenario :Generating Autoship Templates IN RFL

Good Insert into all mandatory and non-mandatory tables
***********************************************************/

BEGIN TRAN

DECLARE @OrdID BIGINT,
@OrdCust BIGINT,
@OrdShip BIGINT,
@OrdItem BIGINT,
@OrdPay BIGINT,
@OrdNote BIGINT 

--SELECT * FROM RodanFieldsLive.dbo.OrderStatus
--SELECT * FROM RodanFieldsLive.dbo.OrderTypes

----SELECT * FROM RodanFieldsLive.dbo.Orders

INSERT INTO RodanFieldsLive.dbo.Orders (OrderNumber,OrderStatusID,OrderTypeID,CurrencyId,CountryId,
DistributorID,StartDate,CompleteDate,DateModified,Subtotal,GrandTotal,TaxAmountOrderItems,TaxAmountTotal,DiscountTotal,CommissionableTotal,	
IsTaxExempt,ApplicationSourceID,SiteID,CreatedByUserId,ModifiedbyUserID,ShippingTotal,TaxableTotal, TaxAmountHandling, TaxAmountShipping, HandlingTotal)
VALUES 
('5555555557',1,4,1,1,1170672,
'2016-03-01 00:00:00.000','2016-02-25 00:00:00.000','2016-03-25 00:00:00.000', 45.28
,58.60,3.83,4.60,0.00,0.00,1,2,420,218,218,2.00,
395.00, 0.00, 0.00, 0.00)


SET @OrdID = (SELECT TOP 1 OrderID FROM RodanFieldsLive.dbo.Orders ORDER BY 1 DESC)
SELECT  @OrdID AS Template
--21067597
---------------------------------------------------------------------------------
--OrderCustomers
---------------------------------------------------------------------------------

 INSERT INTO RodanFieldsLive.dbo.OrderCustomers
        ( AccountID ,OrderID ,TaxAmountTotal ,TaxAmountCity ,TaxAmountState,
		TaxAmountCounty ,TaxAmountDistrict ,TaxableTotal ,IsTaxExempt ,ShippingAmount ,
		DiscountAmount, Subtotal ,PaymentTotal ,Balance ,Total ,BookingCredit ,FutureBookingDate ,
		DataVersion ,TaxAmountOrderItems ,TaxAmountShipping ,CommissionableTotal ,TaxAmountHandling 
		,HandlingAmount
        )
VALUES  ( 1562696, -- AccountID - int
		 @OrdID, -- OrderID - int
          NULL , -- TaxAmountTotal - money
          NULL , -- TaxAmountCity - money
          NULL , -- TaxAmountState - money
          NULL , -- TaxAmountCounty - money
          NULL , -- TaxAmountDistrict - money
          0 , -- TaxableTotal - money
          0 , -- IsTaxExempt - bit
          0 , -- ShippingAmount - money
          0 , -- DiscountAmount - money
          0 , -- Subtotal - money
          0 , -- PaymentTotal - money
          0 , -- Balance - money
          0 , -- Total - money
          0 , -- BookingCredit - int
          GETDATE() , -- FutureBookingDate - datetime
          NULL , -- DataVersion - timestamp
          0 , -- TaxAmountOrderItems - money
          0 , -- TaxAmountShipping - money
          0 , -- CommissionableTotal - money
          0 , -- TaxAmountHandling - money
          0  -- HandlingAmount - money
        )

SET @OrdCust = (SELECT TOP 1 OrderCustomerID  FROM RodanFieldsLive.dbo.OrderCustomers  ORDER BY 1 DESC)
SELECT  @OrdCust  OrderCustomer
 ------------------------------------------------------------------------------------
 --dbo.OrderItems
			-- mapped to OrderItem
----------------------------------------------------------------------------------------

INSERT INTO RodanFieldsLive.dbo.OrderItems
        ( OrderCustomerID ,
          ProductID,
          OrderItemTypeID ,
          Quantity ,
          ItemPrice ,
          Discount ,
          DiscountPercent ,
          AdjustedPrice ,
          CommissionableTotal ,
          ChargeTax ,
          ChargeShipping ,
          Points ,
          MinCustomerSubtotal ,
          MaxCustomerSubtotal ,
          ParentOrderItemID ,
          HostessRewardRuleID ,
          DataVersion ,
          ProductPriceTypeID ,
          ProductName ,
          SKU ,
          CityLocalSalesTax ,
          CitySalesTax ,
          CountyLocalSalesTax ,
          CountySalesTax ,
          StateSalesTax ,
          CountrySalesTax ,
          TaxPercent ,
          TaxAmount ,
          TaxPercentCity ,
          TaxAmountCity ,
          TaxPercentState ,
          TaxAmountState ,
          TaxPercentCounty ,
          TaxAmountCounty ,
          TaxPercentDistrict ,
          TaxAmountDistrict ,
          TaxableTotal
        )
VALUES  (@OrdCust, -- OrderCustomerID - int
          2 , -- ProductID - int
          1 , -- OrderItemTypeID - int
         1 , -- Quantity - int
          20.00 , -- ItemPrice - money
          0 , -- Discount - money
          0 , -- DiscountPercent - money
          0.00 , -- AdjustedPrice - money
          0.00 , -- CommissionableTotal - money
          0 , -- ChargeTax - bit
          0 , -- ChargeShipping - bit
          0 , -- Points - int
          0 , -- MinCustomerSubtotal - money
          0 , -- MaxCustomerSubtotal - money
          NULL , -- ParentOrderItemID - int
          NULL , -- HostessRewardRuleID - int
          NULL , -- DataVersion - timestamp
          2 , -- ProductPriceTypeID - int
          N'' , -- ProductName - nvarchar(250)
          N'' , -- SKU - nvarchar(50)
          0 , -- CityLocalSalesTax - money
          0 , -- CitySalesTax - money
          0 , -- CountyLocalSalesTax - money
          0 , -- CountySalesTax - money
          0 , -- StateSalesTax - money
          0 , -- CountrySalesTax - money
          0.0 , -- TaxPercent - money
          0.0 , -- TaxAmount - money
          0 , -- TaxPercentCity - money
          0 , -- TaxAmountCity - money
          0.0 , -- TaxPercentState - money
          0 , -- TaxAmountState - money
          0 , -- TaxPercentCounty - money
          0 , -- TaxAmountCounty - money
          0 , -- TaxPercentDistrict - money
          0 , -- TaxAmountDistrict - money
          0.00  -- TaxableTotal - money
        )


SET @OrdItem = (SELECT TOP 1 OrderItemID FROM RodanFieldsLive.dbo.OrderItems ORDER BY 1 DESC )
SELECT @OrdItem AS OrderItem


------------------------------------------------------------------------------------------
--dbo.OrderPayment 
		--mapped to OrderBillingAddress and OrderPayment
-------------------------------------------------------------------------------------------

	
INSERT INTO RodanFieldsLive.dbo.OrderPayments
        ( OrderID ,
          OrderCustomerID ,
          PaymentTypeID ,
          AccountNumber ,
          BillingFirstName ,
          BillingLastName ,
          BillingStreetAddress , 
          BillingCity ,
          BillingState ,
          BillingPostalCode ,
          BillingPhoneNumber ,
          IdentityNumber ,
          IdentityState ,
          Amount ,
          RoutingNumber ,
          OrderPaymentStatusID ,
          IsDeferred ,
          ProcessOnDate ,
          ProcessedDate ,
          TransactionID ,
          DataVersion ,
          BillingCountry ,
          DeferredAmount ,
          DeferredTransactionID ,
          BillingCountryID ,
          ExpirationDate ,
          PaymentGateway
        )
VALUES  ( @OrdID, -- OrderID - int
		  @OrdCust, -- OrderCustomerID - int
          1 , -- PaymentTypeID - int
          N'' , -- AccountNumber - nvarchar(200)
          N'Bill' , -- BillingFirstName - nvarchar(200)
          N'Joe' , -- BillingLastName - nvarchar(200)
		 '121 Powell St.' , -- BillingStreetAddress - nvarchar(500)
          N'SF' , -- BillingCity - nvarchar(200)
          N'CA' , -- BillingState - nvarchar(100)
          N'94105-1506' , -- BillingPostalCode - nvarchar(50)
          N'0179886786' , -- BillingPhoneNumber - nvarchar(50)
          N'' , -- IdentityNumber - nvarchar(100)
          N'' , -- IdentityState - nvarchar(100)
         55.78 , -- Amount - money
          N'' , -- RoutingNumber - nvarchar(50)
          2 , -- OrderPaymentStatusID - int
          0 , -- IsDeferred - bit
          GETDATE() , -- ProcessOnDate - datetime
          GETDATE() , -- ProcessedDate - datetime
          N'' , -- TransactionID - nvarchar(50)
          NULL , -- DataVersion - timestamp
          N'' , -- BillingCountry - nvarchar(100)
          0 , -- DeferredAmount - money
          N'' , -- DeferredTransactionID - nvarchar(100)
          0 , -- BillingCountryID - int
          GETDATE() , -- ExpirationDate - datetime
          N'Liltle' 
		  )
 
SET @OrdPay = (SELECT TOP 1 OrderPaymentID FROM RodanFieldsLive.dbo.OrderPayments ORDER BY 1 DESC )

SELECT @OrdPay AS OrderPayment
---------------------------------------------------------------------------------
--dbo.OrderPaymentResults 
 --- mapped to OrderPaymentTransaction

 -------------------------------------------------------------------------------------
INSERT INTO RodanFieldsLive.dbo.OrderPaymentResults
        ( Date ,
          OrderPaymentID ,
          OrderID ,
          OrderCustomerID ,
          Testing ,
          AuthorizeType ,
          RoutingNumber ,
          AccountNumber ,
          Amount ,
          ErrorLevel ,
          ErrorMessage ,
          ResponseCode ,
          ResponseSubCode ,
          ResponseReasonCode ,
          ResponseReasonText ,
          AVSResult ,
          CardCodeResponse ,
          ApprovalCode ,
          Response ,
          TransactionID ,
          UserID ,
          ExpDate ,
          PaymentProviderId
        )
VALUES  ( GETDATE() , -- Date - datetime
		  @OrdPay , -- OrderPaymentID - int
	      @OrdID, -- OrderID - int
		  @OrdCust, -- OrderCustomerID - int
          0 , -- Testing - bit
          N'CREDIT' , -- AuthorizeType - nvarchar(100)
          '' , -- RoutingNumber - varchar(100)
          N'' , -- AccountNumber - nvarchar(200)
          100.00 , -- Amount - money
          0 , -- ErrorLevel - int
          N'' , -- ErrorMessage - nvarchar(100)
          'MMM', -- ResponseCode - nvarchar(3) 
          N'' , -- ResponseSubCode - nvarchar(3)
          N'' , -- ResponseReasonCode - nvarchar(3)
          N'' , -- ResponseReasonText - nvarchar(100)
          N'' , -- AVSResult - nvarchar(1)
          N'' , -- CardCodeResponse - nvarchar(1)
          N'' , -- ApprovalCode - nvarchar(20)
          '' , -- Response - text
          N'' , -- TransactionID - nvarchar(20)
          0 , -- UserID - int
          GETDATE() , -- ExpDate - datetime
          1  -- PaymentProviderId - int
        )


-------------------------------------------------------------------------
--dbo.Notes and dbo.OrderNotes
	--mapped to OrderNotes
-------------------------------------------------------------------------

INSERT INTO RodanFieldsLive.dbo.Notes
        ( AccountID ,
          DateCreated ,
          DateModified ,
          NoteText ,
          NotesTypesID,
          UserName ,
          ParentID ,
          Subject
        )
VALUES  ( 1562696, -- AccountID - int
          GETDATE() , -- DateCreated - datetime
          GETDATE() , -- DateModified - datetime
         'This is a good insert', -- NoteText - nvarchar(max) -- Nulled here which is not nullable in OrderItems 
          3 , -- NotesTypesID - int
          N'Test' , -- UserName - nvarchar(255)
          NULL , -- ParentID - int
        NULL  -- Subject - nvarchar(126)
        )

SET @OrdNote = (SELECT TOP 1 NotesID FROM RodanFieldsLive.dbo.Notes ORDER BY 1 DESC)


INSERT INTO RodanFieldsLive.dbo.OrderNotes
        ( OrderID, NoteID, OrderNoteTypeID )
VALUES  ( @OrdID, -- OrderID - int
          @OrdNote , -- NoteID - int
          1 -- OrderNoteTypeID - int
          )
SAVE TRAN Roll1

COMMIT TRAN 

ROLLBACK TRAN


SELECT MAX  (OrderID) FROM RodanFieldsLive.dbo.Orders ORDER BY 1 DESC 
/****************************************************
Check to see if row inserted 

****************************************************/

USE RodanFieldsLive;

SELECT  oc.*
FROM    Orders o
        JOIN OrderCustomers oc ON o.OrderID = oc.OrderID
        JOIN OrderItems oi ON oc.OrderCustomerID = oi.OrderCustomerID
        JOIN OrderPayments op ON op.OrderID = o.OrderID
                                 AND oc.OrderID = op.OrderID
        JOIN OrderPaymentResults opr ON opr.OrderPaymentID = op.OrderPaymentID
                                        AND opr.OrderID = o.OrderID
        JOIN OrderNotes ono ON o.OrderID = ono.OrderID
        JOIN Notes n ON n.NotesID = ono.NoteID
WHERE   o.OrderID BETWEEN 21067598 AND 21067606;


SELECT AutoshipID,AutoshipTypeID,Active,EndDate,NextRundate FROM RFOperations.etl.StagingAutoship
WHERE AutoshipID BETWEEN 21067598 AND 21067606;


SELECT AutoshipID,AutoshipTypeID,Active,NextRunDate,EndDate FROM RFOperations.Hybris.Autoship 
WHERE AutoshipID BETWEEN 21067598 AND 21067606;





INSERT INTO RodanFieldsLive.dbo.AutoshipOrders
        ( TemplateOrderID ,
          AccountID ,
          AutoshipScheduleID ,
          DateLastCreated ,
          IntervalDayOverride ,
          ConsecutiveOrders ,
          StartDate ,
          EndDate
        )
SELECT o.OrderID,oc.AccountID,2,o.CompleteDate,10,0,DATEADD(MONTH,1,GETDATE()),NULL
FROM    Orders o
        JOIN OrderCustomers oc ON o.OrderID = oc.OrderID
        JOIN OrderItems oi ON oc.OrderCustomerID = oi.OrderCustomerID
        JOIN OrderPayments op ON op.OrderID = o.OrderID
                                 AND oc.OrderID = op.OrderID
        JOIN OrderPaymentResults opr ON opr.OrderPaymentID = op.OrderPaymentID
                                        AND opr.OrderID = o.OrderID
        JOIN OrderNotes ono ON o.OrderID = ono.OrderID
        JOIN Notes n ON n.NotesID = ono.NoteID
WHERE   o.OrderID BETWEEN 21067598 AND 21067606;




--SELECT TOP 10 a.* FROM RFOperations.RFO_Accounts.AccountBase a
--JOIN RFOperations.RFO_Accounts.AccountRF b ON b.AccountID = a.AccountID 
--LEFT JOIN RFOperations.Archive.Autoship c ON a.AccountID=c.AccountID
--WHERE b.Active=1 AND a.CountryID=236 AND c.AutoshipId IS NULL 
--ORDER BY NEWID() DESC




INSERT INTO RodanFieldsLive.dbo.AutoshipDelay
        ( TemplateOrderId ,
          DelayDate ,
          AccountId ,
          DelayTypeId ,
          DelayMethodId ,
          NumConsecutiveDelays ,
          NumDaysSinceLastAutoship ,
          NewDueDate ,
          SelfService ,
          CreatedByUserId
        )
SELECT o.OrderID,DATEADD(MONTH,2,GETDATE()),oc.AccountID,60,1,1,10,DATEADD(MONTH,3,GETDATE()),1,0
FROM    Orders o
        JOIN OrderCustomers oc ON o.OrderID = oc.OrderID
        JOIN OrderItems oi ON oc.OrderCustomerID = oi.OrderCustomerID
        JOIN OrderPayments op ON op.OrderID = o.OrderID
                                 AND oc.OrderID = op.OrderID
        JOIN OrderPaymentResults opr ON opr.OrderPaymentID = op.OrderPaymentID
                                        AND opr.OrderID = o.OrderID
        JOIN OrderNotes ono ON o.OrderID = ono.OrderID
        JOIN Notes n ON n.NotesID = ono.NoteID
		WHERE o.OrderID IN (21067602,21067603)


		SELECT TOP 2 b.AccountID FROM RFOperations.RFO_Accounts.AccountRF a 
		JOIN RFOperations.Hybris.Autoship b ON b.AccountID = a.AccountID 
		WHERE a.Active=1 AND b.Active=1 AND b.AutoshipTypeID=1 AND b.NextRunDate IS NULL AND b.EndDate IS NULL 
		ORDER BY a.ServerModifiedDate DESC 
			
		
		SELECT TOP 2 a.AccountID FROM RFOperations.RFO_Accounts.AccountRF a 
		JOIN RFOperations.Hybris.Autoship b ON b.AccountID = a.AccountID 
		WHERE a.Active=1 AND b.Active=1 AND b.AutoshipTypeID=2 AND b.NextRunDate IS NULL AND b.EndDate IS NULL 
			ORDER BY a.ServerModifiedDate DESC 
		
		SELECT TOP 2 a.AccountID FROM RFOperations.RFO_Accounts.AccountRF a 
		JOIN RFOperations.Hybris.Autoship b ON b.AccountID = a.AccountID 
		WHERE a.Active=1 AND b.Active=1 AND b.AutoshipTypeID=3 AND b.NextRunDate IS NULL AND b.EndDate IS NULL 
	


	
	SELECT * FROM RodanFieldsLive.dbo.orders WHERE OrderID IN  (11830510,10648937,11481188,14718917,10522970,21067602)

	SELECT AutoshipID,Active,EndDate,NextRundate FROM RFOperations.etl.StagingAutoship WHERE AutoshipID IN   (11830510,10648937,11481188,14718917,10522970,21067602)

	SELECT AutoshipID,Active,EndDate,NextRunDate FROM RFOperations.hybris.Autoship WHERE AutoshipID IN   (11830510,10648937,11481188,14718917,10522970,21067602)


	/* Updating Active Template to Inactive In RFL having EndDate IS NULL and NextRunDate IS NULL */