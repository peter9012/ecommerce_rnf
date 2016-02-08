--- QA team solution value returned should be zero


SELECT  pp.AccountID, ccp.BillingAddressID, ad.AddressID, ad.AddressTypeID, aca.AddressID,*
FROM RFOPerations.RFO_Accounts.CreditcardProfiles ccp
JOIN RFOperations.RFO_Accounts.PaymentProfiles pp ON pp.PaymentProfileID = ccp.PaymentProfileID
JOIN Hybris.dbo.users u ON u.p_rfaccountID = CAST (pp.AccountID AS VARCHAR) AND u.p_sourcename = 'Hybris-DM'
--JOIN Hybris.dbo.paymentinfos d ON u.pk = d.userpk
INNER JOIN RFOPerations.RFO_Accounts.Addresses ad ON ad.addressid = ccp.billingaddressid 
INNER JOIN RFOPERATIONS.RFO_ACCOUnts.AccountContactAddresses ACA ON ACA.AddressID = AD.AddressiD
WHERE ad.addresstypeid <> 3 -- OR (ad.AddressID IS NOT NULL AND aca.AddressID IS NULL ) 