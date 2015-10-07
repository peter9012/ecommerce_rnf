 USE DataMigration 
 GO 
 
 IF OBJECT_ID ('DataMigration.Migration.Metadata_Orders') IS NOT NULL 
 DROP TABLE  Migration.Metadata_Orders

  IF OBJECT_ID ('DataMigration.Migration.ErrorLog_Orders') IS NOT NULL 
 DROP TABLE  Migration.ErrorLog_Orders  


CREATE TABLE  Migration.Metadata_Orders
(
MapID INT IDENTITY (1,1) PRIMARY KEY
,Skip BIT
,HybrisObject NVARCHAR (50)
,RFO_Key NVARCHAR(50)
,RFO_Column NVARCHAR (MAX)
,Hybris_Column NVARCHAR (MAX)
,sqlstmt NVARCHAR (MAX)
) 


INSERT INTO DataMigration.Migration.Metadata_Orders
(
Skip 
,HybrisObject 
,RFO_Key 
,RFO_Column 
,Hybris_Column 
)
VALUES

( 0,'Orders','OrderID','OrderID','PK')
,( 0,'Orders','OrderID','ShippingCost','deliverycost')
,( 0,'Orders','OrderID','OrderNumber','code')
,( 0,'Orders','OrderID','AutoShipID','p_associatedtemplate')
,( 0,'Orders','OrderID','CommissionDate','p_commissiondate')
,( 0,'Orders','OrderID','TotalDiscount','totaldiscounts')
,( 0,'Orders','OrderID','CV','p_totalcv')
,( 0,'Orders','OrderID','CompletionDate','p_ordercompletiondate')
,( 0,'Orders','OrderID','donotship','p_donotship')
,( 0,'Orders','OrderID','SubTotal','subtotal')
,( 0,'Orders','OrderID','TotalTax','totaltax')
,( 0,'Orders','OrderID','QV','p_totalqv')
,( 0,'Orders','OrderID','Total','totalprice')
,( 0,'Orders','OrderID','testorder','testorder')
,( 0,'Orders','OrderID','HandlingCost','handlingcost')
,( 0,'Orders','OrderID','p_consultantidreceivingcommiss','p_consultantidreceivingcommiss')
,( 0,'Orders','OrderID','TaxExempt','p_taxexempt')
,( 0,'Orders','OrderID','AccountID','p_rfAccountID')
,( 0,'Orders','OrderID','OrderType','OrderType')
,( 0,'Orders','OrderID','OrderStatus','statuspk')
,( 0,'Orders','OrderID','Currency','Currency')
,( 0,'Orders','OrderID','BillingAddress','paymentaddresspk')
,( 0,'Orders','OrderID','OrderPayment','paymentinfopk')
,( 0,'Orders','OrderID','ShippingMethod','deliverymodepk')
,( 0,'Orders','OrderID','ShippingAddress','deliveryaddresspk')
,( 0,'Orders','OrderID','TotalTaxValues','TotalTaxValues')
,( 0,'Orders','OrderID','DeliveryTaxValues','DeliveryTaxValues')
,( 0,'Orders','OrderID','HandlingCostTaxValues','HandlingCostTaxValues')
,( 0,'Orders','OrderID','ModifiedDate','Modifiedts')
,( 0,'OrderEntries ','OrderItemID','OrderItemID','PK')
,( 0,'OrderEntries ','OrderItemID','DiscountValues','discountvalues')
,( 0,'OrderEntries ','OrderItemID','TotalPrice','totalprice')
,( 0,'OrderEntries ','OrderItemID','WholesalePrice','p_wholesaleprice')
,( 0,'OrderEntries ','OrderItemID','CV','p_cv')
,( 0,'OrderEntries ','OrderItemID','QV','p_qv')
,( 0,'OrderEntries ','OrderItemID','TaxablePrice','p_taxableprice')
,( 0,'OrderEntries ','OrderItemID','Quantity','quantity')
,( 0,'OrderEntries ','OrderItemID','BasePrice','baseprice')
,( 0,'OrderEntries ','OrderItemID','LineItemNo','entrynumber')
,( 0,'OrderEntries ','OrderItemID','RetailProfit','p_retailprofit')
,( 0,'OrderEntries ','OrderItemID','OrderID','OrderPK')
,( 0,'OrderEntries ','OrderItemID','SKU','p_catalognumber')
,( 0,'OrderEntries ','OrderItemID','Info','info')
,( 0,'OrderEntries ','OrderItemID','UnitPK','UnitPk')
,( 0,'Consignments','OrderID','TrackingID','p_trackingid')
,( 0,'Consignments','OrderID','ShipDate','p_shippingdate')
,( 0,'Consignments','OrderID','ShipNumber','p_shipnumber')
,( 0,'Consignments','OrderID','OrderNumber','p_code')
,( 0,'Consignments','OrderID','ShipStatus','p_status')
,( 0,'Consignments','OrderID','Carrier','p_carrier')
,( 0,'Consignments','OrderID','ShippingAddress','p_shippingaddress')
,( 0,'Consignments','OrderID','OrderID','p_order')
,( 0,'Consignments','OrderID','ShippingMethod','p_deliverymode')
,( 0,'ConsignmentEntries','OrderShipmentPackageItemID','OrderShipmentPackageItemID','PK')
,( 0,'ConsignmentEntries','OrderShipmentPackageItemID','Quantity','p_quantity')
,( 0,'ConsignmentEntries','OrderShipmentPackageItemID','TrackingNumber','p_trackingnumber')
,( 0,'ConsignmentEntries','OrderShipmentPackageItemID','OrderItemId','p_orderentry')
,( 0,'ConsignmentEntries','OrderShipmentPackageItemID','ShippingMethod','p_shippingmethod')
,( 0,'Addresses_Billing','OrderBillingAddressID','OrderBillingAddressID','p_rfaddressid')
,( 0,'Addresses_Billing','OrderBillingAddressID','Address1','p_streetname')
,( 0,'Addresses_Billing','OrderBillingAddressID','p_contactAddress','p_contactaddress')
,( 0,'Addresses_Billing','OrderBillingAddressID','Telephone','p_phone1')
,( 0,'Addresses_Billing','OrderBillingAddressID','FirstName','p_firstname')
,( 0,'Addresses_Billing','OrderBillingAddressID','p_shippingaddress','p_shippingaddress')
,( 0,'Addresses_Billing','OrderBillingAddressID','AddressLine2','p_streetnumber')
,( 0,'Addresses_Billing','OrderBillingAddressID','SubRegion','p_district')
,( 0,'Addresses_Billing','OrderBillingAddressID','LastName','p_lastname')
,( 0,'Addresses_Billing','OrderBillingAddressID','Locale','p_town')
,( 0,'Addresses_Billing','OrderBillingAddressID','MiddleName','p_middlename')
,( 0,'Addresses_Billing','OrderBillingAddressID','p_billingaddress','p_billingaddress')
,( 0,'Addresses_Billing','OrderBillingAddressID','PostalCode','p_postalcode')
,( 0,'Addresses_Billing','OrderBillingAddressID','duplicate','duplicate')
,( 0,'Addresses_Billing','OrderBillingAddressID','p_unloadingaddress','p_unloadingaddress')
,( 0,'Addresses_Billing','OrderBillingAddressID','OrderPaymentID','OwnerPkString')
,( 0,'Addresses_Billing','OrderBillingAddressID','Region','region')
,( 0,'Addresses_Billing','OrderBillingAddressID','CountryID','country')
,( 0,'Addresses','OrderShippingAddressID ','OrderShippingAddressID','PK')
,( 0,'Addresses','OrderShippingAddressID ','Address1','p_streetname')
,( 0,'Addresses','OrderShippingAddressID ','p_contactAddress','p_contactaddress')
,( 0,'Addresses','OrderShippingAddressID ','Telephone','p_phone1')
,( 0,'Addresses','OrderShippingAddressID ','FirstName','p_firstname')
,( 0,'Addresses','OrderShippingAddressID ','p_shippingaddress','p_shippingaddress')
,( 0,'Addresses','OrderShippingAddressID ','AddressLine2','p_streetnumber')
,( 0,'Addresses','OrderShippingAddressID ','SubRegion','p_district')
,( 0,'Addresses','OrderShippingAddressID ','LastName','p_lastname')
,( 0,'Addresses','OrderShippingAddressID ','Locale','p_town')
,( 0,'Addresses','OrderShippingAddressID ','MiddleName','p_middlename')
,( 0,'Addresses','OrderShippingAddressID ','p_billingaddress','p_billingaddress')
,( 0,'Addresses','OrderShippingAddressID ','PostalCode','p_postalcode')
,( 0,'Addresses','OrderShippingAddressID ','duplicate','duplicate')
,( 0,'Addresses','OrderShippingAddressID ','p_unloadingaddress','p_unloadingaddress')
,( 0,'Addresses','OrderShippingAddressID ','OrderPaymentID','OwnerPkString')
,( 0,'Addresses','OrderShippingAddressID ','Region','region')
,( 0,'Addresses','OrderShippingAddressID ','CountryID','country')
,( 0,'OrderNotes','OrderNoteID','OrderNoteID','p_noteid')
,( 0,'OrderNotes','OrderNoteID','Notes','p_ordernotes')
,( 0,'OrderNotes','OrderNoteID','Username','p_uid')
,( 0,'OrderNotes','OrderNoteID','Subject','p_subject')
,( 0,'OrderNotes','OrderNoteID','OrderNumber','code')
,( 0,'PaymentTransactions','OrderID','OrderID','p_order')
,( 0,'PaymentTransactions','OrderID','currency','p_currency')
,( 0,'PaymentTransactions','OrderID','TransactionID','p_requestid')
,( 0,'PaymentTransactions','OrderID','SubscriptionID','P_SubscriptionID')
,( 0,'PaymentTransactions','OrderID','PaymentProvider','p_paymentprovider')
,( 0,'PaymentTransactions','OrderID','Amount','p_plannedamount')
,( 0,'PaymentInfos','OrderPaymentID ','OrderPaymentID','CODE')
,( 0,'PaymentInfos','OrderPaymentID ','OrderPaymentID_Code','code')
,( 0,'PaymentInfos','OrderPaymentID ','PaymentProfileID','p_rfaccountpaymentmethodid')
,( 1,'PaymentInfos','OrderPaymentID ','CardNumber','p_number')
,( 1,'PaymentInfos','OrderPaymentID ','Expmonth','p_validtomonth')
,( 1,'PaymentInfos','OrderPaymentID ','ExpYear','p_validtoyear')
,( 0,'PaymentInfos','OrderPaymentID ','SubscriptionID','P_SubscriptionID')
,( 1,'PaymentInfos','OrderPaymentID ','ccowner','ccowner')
,( 0,'PaymentInfos','OrderPaymentID ','OrderID','owner')
,( 0,'PaymentInfos','OrderPaymentID ','AccountID','userpk')
,( 0,'paymnttrnsctentries','OrderPaymentTransactionID','OrderPaymentTransactionID','PK')
,( 0,'paymnttrnsctentries','OrderPaymentTransactionID','AmountAuthorized','p_amount')
,( 0,'paymnttrnsctentries','OrderPaymentTransactionID','TransactionID','p_transactionId')
,( 0,'paymnttrnsctentries','OrderPaymentTransactionID','ProcessDate','p_time')
,( 0,'paymnttrnsctentries','OrderPaymentTransactionID','AvsResult','p_avsresult')
,( 0,'paymnttrnsctentries','OrderPaymentTransactionID','PaymentType','p_type')
,( 0,'paymnttrnsctentries','OrderPaymentTransactionID','Currency', 'p_currency')
,( 0,'paymnttrnsctentries','OrderPaymentTransactionID','SubscriptionID', 'p_subscriptionid')



--TRUNCATE TABLE DataMigration.Migration.Metadata_Orders


GO 


CREATE TABLE Migration.ErrorLog_Orders 
(
ErrorID INT  IDENTITY(1,1) PRIMARY KEY
, MapID INT 
,Identifier NVARCHAR (50)
, RecordID BIGINT 
, RFO_Value NVARCHAR (MAX)
, Hybris_Value NVARCHAR (MAX)
)

CREATE NONCLUSTERED INDEX IX_MapID ON Migration.ErrorLog_Orders 
(MapID)

CREATE NONCLUSTERED INDEX IX_RecordID ON Migration.ErrorLog_Orders 
(RecordID)


----Generate SQL Compare Statements

DECLARE @I INT = 1,
@C INT =
     ( SELECT   MAX(MapID)
               FROM     DataMigration.Migration.Metadata_Orders
             );


DECLARE @SRCCol NVARCHAR(MAX);


DECLARE @DesCol NVARCHAR(MAX);

DECLARE @DesKey NVARCHAR(50); 

DECLARE @SrcKey NVARCHAR(50); 

DECLARE @Temp NVARCHAR(50);

DECLARE @SrcTemp NVARCHAR(50);

DECLARE @DesTemp NVARCHAR(50);

WHILE ( @I <= @C )

            BEGIN 

                SET @Temp = ( SELECT    CASE WHEN HybrisObject = 'Orders'
                                             THEN '#Orders'
                                              WHEN HybrisObject = 'OrderEntries' THEN '#Item'
                                          WHEN HybrisObject = 'Consignments' OR HybrisObject = 'ConsignmentEntries'     THEN '#SPItem'
                                         WHEN HybrisObject = 'PaymentInfos' OR HybrisObject = 'PaymentTransactions'    THEN '#Pay'
                                           WHEN HybrisObject = 'paymnttrnsctentries' THEN '#Tran'
                                              WHEN HybrisObject = 'Addresses_Billing' THEN '#BlAdr'
											   WHEN HybrisObject = 'Addresses' THEN '#ShAdr'
											   WHEN HybrisObject = 'OrderNotes' THEN '#Note'
                                        END
                              FROM      DataMigration.Migration.Metadata_Orders
                              WHERE     MapID = @I
                            ); 


                SET @SrcKey = ( SELECT  RFO_Key
                                FROM    DataMigration.Migration.Metadata_Orders
                                WHERE   MapID = @I
                              );

                SET @DesKey = ( SELECT   
									CASE WHEN HybrisObject = 'Consignments'
                                           THEN 'P_order'
										 WHEN HybrisObject = 'OrderNotes'
                                           THEN 'P_noteid'
										WHEN HybrisObject = 'PaymentTransactions'
                                           THEN 'P_order'
										--	 WHEN HybrisObject = 'OrderEntries' THEN 'PK'
										ELSE 'PK'
                                        END
                                FROM     DataMigration.Migration.Metadata_Orders
                                WHERE   MapID = @I
                              ); 

                SET @SRCCol = ( SELECT  RFO_Column
                                FROM   DataMigration.Migration.Metadata_Orders
                                WHERE   MapID = @I
                              );


                SET @DesCol = ( SELECT  Hybris_Column
                                FROM    DataMigration.Migration.Metadata_Orders
                                WHERE   MapID = @I
                              );

				   SET @SrcTemp = ( SELECT   CASE WHEN HybrisObject = 'Orders'THEN '#RFO_Orders'
                                              WHEN HybrisObject = 'OrderEntries' THEN '#RFO_Item'
                                          WHEN HybrisObject = 'Consignments' OR HybrisObject = 'ConsignmentEntries'     THEN '#RFO_SPItem'
                                         WHEN HybrisObject = 'PaymentInfos' OR HybrisObject = 'PaymentTransactions'    THEN '#RFO_Pay'
                                           WHEN HybrisObject = 'paymnttrnsctentries' THEN '#RFO_Tran'
                                              WHEN HybrisObject = 'Addresses_Billing' THEN '#RFO_BlAdr'
											   WHEN HybrisObject = 'Addresses' THEN '#RFO_ShAdr'
											   WHEN HybrisObject = 'OrderNotes' THEN '#RFO_Note'

                                        END
									FROM     DataMigration.Migration.Metadata_Orders
									WHERE    MapID = @I
                            ); 


				SET @DesTemp = ( SELECT      CASE WHEN HybrisObject = 'Orders'
                                             THEN '#Hybris_Orders'
											  WHEN HybrisObject = 'OrderEntries' THEN '#Hybris_Item'
                                          WHEN HybrisObject = 'Consignments' OR HybrisObject = 'ConsignmentEntries'     THEN '#Hybris_SPItem'
                                         WHEN HybrisObject = 'PaymentInfos' OR HybrisObject = 'PaymentTransactions'    THEN '#Hybris_Pay'
                                           WHEN HybrisObject = 'paymnttrnsctentries' THEN '#Hybris_Tran'
                                              WHEN HybrisObject = 'Addresses_Billing' THEN '#Hybris_BlAdr'
											   WHEN HybrisObject = 'Addresses' THEN '#Hybris_ShAdr'
											   WHEN HybrisObject = 'OrderNotes' THEN '#Hybris_Note'
                                            
                                        END
									FROM     DataMigration.Migration.Metadata_Orders
                         
                              WHERE     MapID = @I
                            ); 

                DECLARE @SQL2 NVARCHAR(MAX) = 'SELECT ' + ''''
                    + CAST(@SrcKey AS NVARCHAR) + '''' + ' , '
                    + CAST(@I AS NVARCHAR) + ' , ' + @SrcKey + ', ISNULL(LTRIM(RTRIM(' + @SRCCol
                    + ')), '''') FROM ' + @SRCTemp + ' WHERE ' + @SrcKey
                    + ' IN (SELECT ' + @SrcKey + ' FROM ' + @Temp
                    + ' )'
                DECLARE @SQL3 NVARCHAR(MAX) = ' EXCEPT  SELECT ' + ''''
                    + CAST(@SrcKey AS NVARCHAR) + '''' + ' , '
                    + CAST(@I AS NVARCHAR) + ' , ' + @DesKey + ', ISNULL(LTRIM(RTRIM(' + @DesCol
                    + ')),'''') FROM ' + @DesTemp +' WHERE ' + @DesKey
                    + ' IN (SELECT ' + @SrcKey + ' FROM ' + @Temp
                    + ' )'; 

                DECLARE @SQL1 NVARCHAR(MAX)= @SQL2 + @SQL3; 

SELECT @SQL1
--SELECT @SQL2
--SELECT @SQL3


                UPDATE   DataMigration.Migration.Metadata_Orders
                SET     sqlstmt = @SQL1
                WHERE   MapID = @I;


                SET @I = @I + 1;
            END; 



SELECT * FROM  DataMigration.Migration.Metadata_Orders


