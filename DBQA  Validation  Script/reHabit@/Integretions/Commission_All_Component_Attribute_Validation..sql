USE Commissions 
GO


DECLARE @AccountID BIGINT =98806918,  --->>>>>>>>> Please Change the AccountID Which CID or p_customerid


@RecordCount INT=0


SET @RecordCount=(SELECT 
     COUNT( a.AccountID)
FROM commissions.dropoff.Accounts a
LEFT JOIN RFO.synAccountStatus s ON s.AccountStatusID=a.AccountStatusID
LEFT JOIN rfo.synAccountType t ON t.AccounttypeId=a.AccountTypeID  WHERE AccountID=@AccountID)
IF(@RecordCount<1)
BEGIN
SELECT 'No Account Flown Yet'
END 

ELSE
BEGIN

SELECT'Commission_Accounts'[Table]
SELECT s.name[AccountStatus],t.name[AccountType],a. * FROM commissions.dropoff.Accounts a
LEFT JOIN RFO.synAccountStatus s ON s.AccountStatusID=a.AccountStatusID
LEFT JOIN rfo.synAccountType t ON t.AccounttypeId=a.AccountTypeID  WHERE AccountID=@AccountID


SELECT'Commission_Accounts'[Table]
SELECT a.* FROM rfo.synvw_GetAccount_Reporting a 
WHERE a.AccountID=@AccountID

SET @RecordCount=0
SET @RecordCount=(SELECT 
     COUNT( o.OrderNumber)
FROM commissions.dropoff.orders o
LEFT JOIN rfo.synOrderType t ON t.ordertypeid=o.OrderTypeID
LEFT JOIN rfo.synOrderStatus s ON s.orderstatusid=o.OrderStatusID
WHERE AccountID=@AccountID)


IF(@RecordCount<1)
BEGIN
SELECT 'No Orders Yet'
END 

ELSE
BEGIN

SELECT 'Commission_Orders'[Table]
SELECT o.AccountID,s.name[OrderStatus],t.name[OrderTypes],o.* FROM commissions.dropoff.orders o
LEFT JOIN rfo.synOrderType t ON t.ordertypeid=o.OrderTypeID
LEFT JOIN rfo.synOrderStatus s ON s.orderstatusid=o.OrderStatusID
WHERE AccountID=@AccountID


SELECT 'Commission_OrderItems'[Table]
SELECT oi.* FROM commissions.dropoff.orders o
LEFT JOIN commissions.dropoff.OrderItems oi ON oi.OrderNumber=o.OrderNumber
WHERE AccountID=@AccountID
ORDER BY o.OrderNumber, LineItemNo

SET @RecordCount=0
SET @RecordCount=(SELECT 
     COUNT( re.OrderNumber)
FROM    Commissions.dropoff.Accounts ac
JOIN commissions.dropoff.Orders ro ON ro.AccountID = ac.AccountID
LEFT JOIN commissions.dropoff.OrderReturns re ON re.OrderNumber=ro.OrderNumber
WHERE ac.AccountID=@AccountID)

IF(@RecordCount<1)
BEGIN
SELECT 'No ReturnOrders Yet'
END 
ELSE
BEGIN

SELECT  'Commission_ReturnOrders' [Table] 


SELECT        re. *
FROM    Commissions.dropoff.Accounts ac
JOIN commissions.dropoff.Orders ro ON ro.AccountID = ac.AccountID
LEFT JOIN commissions.dropoff.OrderReturns re ON re.OrderNumber=ro.OrderNumber
WHERE ac.AccountID=@AccountID

SELECT  'Commission_ReturnOrderItems' [Table] 
SELECT        ri. *
FROM    Commissions.dropoff.Accounts ac
JOIN commissions.dropoff.Orders ro ON ro.AccountID = ac.AccountID
LEFT JOIN commissions.dropoff.OrderReturns re ON re.OrderNumber=ro.OrderNumber
LEFT JOIN commissions.dropoff.Orderreturnitems ri ON ri.OrderNumber=re.OrderNumber
WHERE ac.AccountID=@AccountID

END 

END 
END 