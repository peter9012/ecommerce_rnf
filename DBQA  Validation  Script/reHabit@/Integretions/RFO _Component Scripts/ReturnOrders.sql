

DECLARE @RetrunNumber NVARCHAR(225)='1200002633'

SELECT 'ReturnOrder',ro.* FROM RFOperations.RFO_Accounts.AccountBase ab 
JOIN RFOperations.Hybris.ReturnOrder ro ON ro.AccountID = ab.AccountID
WHERE ro.ReturnOrderNumber=@RetrunNumber

SELECT 'ReturnOrder_Taxes',rp.* FROM RFOperations.RFO_Accounts.AccountBase ab 
JOIN RFOperations.Hybris.ReturnOrder ro ON ro.AccountID = ab.AccountID
LEFT JOIN RFOperations.Hybris.ReturnOrderTax rt ON rt.ReturnOrderID = ro.ReturnOrderID
WHERE ro.ReturnOrderNumber=@RetrunNumber




SELECT 'ReturnOrder_Items',ri.* FROM RFOperations.RFO_Accounts.AccountBase ab 
JOIN RFOperations.Hybris.ReturnOrder ro ON ro.AccountID = ab.AccountID
LEFT JOIN RFOperations.Hybris.ReturnItem ri ON ri.ReturnOrderID = ro.ReturnOrderID
WHERE ro.ReturnOrderNumber=@RetrunNumber


SELECT 'ReturnOrder_Items_Taxes',rp.* FROM RFOperations.RFO_Accounts.AccountBase ab 
JOIN RFOperations.Hybris.ReturnOrder ro ON ro.AccountID = ab.AccountID
LEFT JOIN RFOperations.Hybris.ReturnItem ri ON ri.ReturnOrderID = ro.ReturnOrderID
LEFT JOIN RFOperations.Hybris.ReturnItemTax rit ON rit.ReturnItemID = ri.ReturnItemID
WHERE ro.ReturnOrderNumber=@RetrunNumber

SELECT 'ReturnOrder_Payment',rp.* FROM RFOperations.RFO_Accounts.AccountBase ab 
JOIN RFOperations.Hybris.ReturnOrder ro ON ro.AccountID = ab.AccountID
LEFT JOIN RFOperations.Hybris.ReturnPayment rp ON rp.ReturnOrderID = ro.ReturnOrderID
WHERE ro.ReturnOrderNumber=@RetrunNumber



SELECT 'ReturnOrder_Payment',tpt.* FROM RFOperations.RFO_Accounts.AccountBase ab 
JOIN RFOperations.Hybris.ReturnOrder ro ON ro.AccountID = ab.AccountID
LEFT JOIN RFOperations.Hybris.ReturnPayment  rp ON rp.ReturnOrderID = ro.ReturnOrderID
LEFT JOIN RFOperations.Hybris.ReturnPaymentTransaction tpt ON tpt.ReturnPaymentId = rp.ReturnPaymentId
WHERE ro.ReturnOrderNumber=@RetrunNumber



SELECT 'ReturnOrder_Payment_Addresses',rp.* FROM RFOperations.RFO_Accounts.AccountBase ab 
JOIN RFOperations.Hybris.ReturnOrder ro ON ro.AccountID = ab.AccountID
LEFT JOIN RFOperations.Hybris.ReturnBillingAddress rba ON rba.ReturnOrderID = ro.ReturnOrderID
WHERE ro.ReturnOrderNumber=@RetrunNumber



SELECT 'ReturnOrder_Notes',rp.* FROM RFOperations.RFO_Accounts.AccountBase ab 
JOIN RFOperations.Hybris.ReturnOrder ro ON ro.AccountID = ab.AccountID
LEFT JOIN RFOperations.Hybris.ReturnNotes rn ON rn.ReturnOrderID = ro.ReturnOrderID
WHERE ro.ReturnOrderNumber=@RetrunNumber






