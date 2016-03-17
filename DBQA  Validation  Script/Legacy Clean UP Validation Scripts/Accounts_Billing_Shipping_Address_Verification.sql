DROP TABLE DataMigration.Migration.SHIP_ADD_CLEANUP;
SELECT  *
INTO    DataMigration.Migration.SHIP_ADD_CLEANUP
FROM    ( SELECT DISTINCT
                    ACCOUNTID ,
                    CONCAT(Address1, ' ', AddressLine2, ' ', AddressLine3, ' ',
                           AddressLine4, ' ', AddressLine5) AS AD
          FROM      RFOperations.HYBRIS.ORDERSHIPPINGADDRESS OSA ,
                    RFOperations.HYBRIS.ORDERS O ,
                    RodanFieldsLive.dbo.Orders rfl
          WHERE     O.OrderID = rfl.OrderNumber
                    AND rfl.OrderTypeID NOT IN ( 4, 5, 9 )
                    AND O.OrderID = OSA.OrderID
                    AND O.CountryId = 236
                    AND ACCOUNTID NOT IN ( 1, 2 )
                    AND O.OrderNumber IN ( SELECT   OrderID
                                           FROM     RFOperations.ETL.ORDERDATE
                                           WHERE    StartDate > '06/01/2014' )
          UNION
          SELECT DISTINCT
                    O.AccountID ,
                    CONCAT(Address1, ' ', AddressLine2, ' ', AddressLine3, ' ',
                           AddressLine4, ' ', AddressLine5) AS AD
          FROM      RFOperations.HYBRIS.AUTOSHIPSHIPPINGADDRESS OSA ,
                    RFOperations.HYBRIS.AUTOSHIP O ,
                    RodanFieldsLive.dbo.AutoshipOrders ao
          WHERE     O.AUTOSHIPNumber = OSA.AUTOSHIPID
                    AND O.COUNTRYID = 236
                    AND ao.TemplateOrderID = O.AutoshipID
                    AND ao.AccountID = O.AccountID
                    AND O.AccountID NOT IN ( 1, 2 )
        ) A
EXCEPT
SELECT DISTINCT
        A.ACCOUNTID ,
        CONCAT(AddressLine1, ' ', AddressLine2, ' ', AddressLine3, ' ',
               AddressLine4, ' ', AddressLine5)
FROM    RFOperations.RFO_ACCOUNTS.ACCOUNTBASE A ,
        RFOperations.RFO_ACCOUNTS.ACCOUNTCONTACTS AC ,
        RFOperations.RFO_ACCOUNTS.AccountContactAddresses ACA ,
        RFOperations.RFO_ACCOUNTS.Addresses AD
WHERE   A.ACCOUNTID = AC.ACCOUNTID
        AND A.COUNTRYID = 236
        AND AC.ACCOUNTCONTACTID = ACA.ACCOUNTCONTACTID
        AND ACA.ADDRESSID = AD.ADDRESSID
        AND AD.ADDRESSTYPEID = 2
        AND A.ACCOUNTID NOT IN ( 1, 2 );

				
DROP TABLE DataMigration.Migration.BILL_ADD_CLEANUP;

				--VERIFY BILLING ADDRESS
SELECT  *
INTO    DataMigration.Migration.BILL_ADD_CLEANUP
FROM    ( SELECT DISTINCT
                    ACCOUNTID ,
                    CONCAT(Address1, ' ', AddressLine2, ' ', AddressLine3, ' ',
                           AddressLine4, ' ', AddressLine5) AS AD
          FROM      RFOperations.HYBRIS.ORDERBILLINGADDRESS OSA ,
                    RFOperations.HYBRIS.ORDERS O ,
                    RodanFieldsLive.dbo.Orders rfl
          WHERE     O.OrderNumber = rfl.OrderID
                    AND rfl.OrderTypeID NOT IN ( 4, 5, 9 )
                    AND O.OrderID = OSA.OrderID
                    AND O.CountryId = 236
                    AND ACCOUNTID NOT IN ( 1, 2 )
                    AND O.OrderNumber IN ( SELECT   OrderID
                                           FROM     RFOperations.ETL.ORDERDATE
                                           WHERE    StartDate > '06/01/2014' )
          UNION
          SELECT DISTINCT
                    O.AccountID ,
                    CONCAT(Address1, ' ', AddressLine2, ' ', AddressLine3, ' ',
                           AddressLine4, ' ', AddressLine5) AS AD
          FROM      RFOperations.HYBRIS.AUTOSHIPPAYMENTADDRESS OSA ,
                    RFOperations.HYBRIS.AUTOSHIP O ,
                    RodanFieldsLive.dbo.AutoshipOrders ao
          WHERE     O.AUTOSHIPNumber = OSA.AUTOSHIPID
                    AND O.COUNTRYID = 236
                    AND ao.TemplateOrderID = O.AutoshipID
                    AND ao.AccountID = O.AccountID
                    AND O.AccountID NOT IN ( 1, 2 )
        ) A
EXCEPT
SELECT DISTINCT
        A.ACCOUNTID ,
        CONCAT(AddressLine1, ' ', AddressLine2, ' ', AddressLine3, ' ',
               AddressLine4, ' ', AddressLine5)
FROM    RFOperations.RFO_ACCOUNTS.ACCOUNTBASE A ,
        RFOperations.RFO_ACCOUNTS.ACCOUNTCONTACTS AC ,
        RFOperations.RFO_ACCOUNTS.AccountContactAddresses ACA ,
        RFOperations.RFO_ACCOUNTS.Addresses AD
WHERE   A.ACCOUNTID = AC.ACCOUNTID
        AND A.COUNTRYID = 236
        AND AC.ACCOUNTCONTACTID = ACA.ACCOUNTCONTACTID
        AND ACA.ADDRESSID = AD.ADDRESSID
        AND AD.ADDRESSTYPEID = 3
        AND A.ACCOUNTID NOT IN ( 1, 2 );

