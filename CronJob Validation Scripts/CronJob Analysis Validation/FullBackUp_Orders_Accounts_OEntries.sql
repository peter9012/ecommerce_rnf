USE RFOperations
GO 
CREATE PROCEDURE usp_CRjob_FullBackUp_Orders_Accounts_OEntries
AS 
BEGIN

IF OBJECT_ID('Hybris.dbo.Orders_Bkp') IS NOT NULL 
DROP TABLE Hybris.dbo.Orders_bkp

IF OBJECT_ID('Hybris.dbo.Orderentries_bkp') IS NOT NULL 
DROP TABLE Hybris.dbo.Orderentries_bkp


IF OBJECT_ID('Hybris.dbo.Users_bkp') IS NOT NULL 
DROP TABLE Hybris.dbo.Users_bkp

SELECT 'Orders BackUp Started' AS Step ,GETDATE() AS StartedTime

SELECT  *
INTO    Hybris.dbo.Orders_bkp
FROM    Hybris..orders
WHERE   p_template = 1 AND currencypk= 8796125855777 ;

SELECT 'Orders BackUp Completed' AS Step ,GETDATE() AS CompletedTime

SELECT  b.*
INTO    Hybris.dbo.Orderentries_bkp
FROM    Hybris.dbo.orders a
        JOIN Hybris.dbo.orderentries b ON a.pk = b.orderpk
WHERE   a.p_template = 1 AND currencypk= 8796125855777  ;

SELECT 'OrdersEntries BackUp Completed' AS Step ,GETDATE() AS CompletedTime


SELECT  *
INTO    Hybris.dbo.Users_bkp
FROM    Hybris..users
WHERE   p_country = 8796100624418;

SELECT 'Users BackUp Completed' AS Step ,GETDATE() AS CompletedTime

END 

