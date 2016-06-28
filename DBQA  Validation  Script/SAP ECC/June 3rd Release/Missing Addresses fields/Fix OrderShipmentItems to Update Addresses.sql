-- ================================================
-- Script to update Address Account table.
-- Some addresses are missing the Postal Code, City, State, or Address 1
-- and this need to be fixed for migration process.
--
-- This script try to match the incorrect addresses with one from the same 
-- account that contains the complete information, it used the following 
-- validation to compare the data:
-- 1. Wrong Account Address Address1 = Address 1 from a complete account address
-- 2. Wrong Account Address Postal Code = Postal Code from a complete account address
-- 3. Wrong Account Address City = City and Postal Code like Postal Code from a complete account address
-- 4. Wrong Account Address Address1 = Address 1 from a complete order payment
-- 5. Wrong Account Address Postal Code = Postal Code from a complete order payment
-- 6. Wrong Account Address City = City and Postal Code like Postal Code from a complete order payment
-- ================================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Harold Nicaragua
-- Create date: December 5, 2015
-- Description:	Update Address Account missing values (Postal Code, City, State, Address 1, PhoneNumber) 
-- =============================================

USE RodanFieldsLive

DECLARE @TransactionName varchar(20) = 'Transaction1';

BEGIN TRAN @TransactionName

BEGIN TRY

--DROP TABLE #AddressFix 
CREATE TABLE #AddressFix (AccountId int, OrderShipmentId int, OrderTypeId int, City nvarchar(50), State nvarchar(50), PostalCode nvarchar(50), Address1 nvarchar(150), PhoneNumber nvarchar(100), NewCity nvarchar(50), NewState nvarchar(50), NewPostalCode nvarchar(50), NewAddress1 nvarchar(50), NewPhoneNumber nvarchar(100), FilteredBy char(2), BasedOnAccountAddressId int, BasedOnAccountId int, BasedOnOrderPaymentId int)

--Only Active Autoships
INSERT INTO #AddressFix 
	SELECT 
		A.AccountId, OS.OrderShipmentId, O.OrderTypeId, OS.City, OS.State, OS.PostCode, OS.Address1, OS.DayPhone, null,null,null,null,null,null,null,null,null
	FROM 
		OrderShipments OS JOIN
		Orders O ON OS.OrderId = O.OrderId JOIN
		OrderCustomers OC ON O.OrderId = OC.OrderId JOIN
		Accounts A ON OC.AccountId = A.AccountId 
	WHERE 
		1=1
		AND O.OrderStatusID = 7
		AND O.OrderTypeId IN ( 4, 5)
		AND 
		(
			ISNULL(OS.State,'') = '' 
			OR ISNULL (OS.PostCode,'') = '' 
			OR ISNULL(OS.Address1, '')= ''
			OR ISNULL (OS.city, '') = ''
		) 	
		AND A.Active = 1
		AND A.Accountid <> 2;


INSERT INTO #AddressFix 
	SELECT 
		A.AccountId, OS.OrderShipmentId, O.OrderTypeId, OS.City, OS.State, OS.PostCode, OS.Address1, OS.DayPhone, null,null,null,null,null,null,null,null,null
	FROM 
		OrderShipments OS JOIN
		Orders O ON OS.OrderId = O.OrderId JOIN
		OrderCustomers OC ON O.OrderId = OC.OrderId JOIN
		Accounts A ON OC.AccountId = A.AccountId 
	WHERE 
		1=1
		AND O.CompleteDate >= '2016-01-01'
		AND O.OrderStatusID IN ( 4, 5 )
		AND O.OrderTypeId NOT IN ( 4, 5, 9, 11)
		AND 
		(
			ISNULL(OS.State,'') = '' 
			OR ISNULL (OS.PostCode,'') = '' 
			OR ISNULL(OS.Address1, '')= ''
			OR ISNULL (OS.city, '') = ''
		) 	
		AND A.Active = 1
		AND A.Accountid <> 2;
 
Select * from #AddressFix;

--Select * from #AddressFix where ISNULL(PhoneNumber,'') = '';

WITH Address_To_Fix_Error (AccountId, AccountAddressId, State, PostalCode, Address1, City, PhoneNumber, RK)
AS
(
	SELECT 
	A.AccountId, AA.AccountAddressId, AA.State, AA.PostalCode, AA.Address1, AA.City,AA.PhoneNumber,
	ROW_NUMBER() OVER(PARTITION BY A.AccountId 
                            ORDER BY AA.AccountAddressId DESC) AS rk
	FROM 
		Accounts A JOIN
		AccountAddresses AA ON A.AccountId = AA.AccountId JOIN
		#AddressFix AWE ON A.AccountId = AWE.AccountId AND AA.Address1 = AWE.Address1
	WHERE 
	1=1
	AND ISNULL(AA.State,'') <> '' 
	AND ISNULL (AA.PostalCode,'') <> '' 
	AND ISNULL(AA.Address1, '')<> ''
	AND ISNULL (AA.City, '') <> ''
	AND A.AccountId <> 2
)

UPDATE #AddressFix
SET NewState = ATFE.State, 
	NewPostalCode = ATFE.PostalCode, 
	NewAddress1 = ATFE.Address1, 
	NewCity = ATFE.City,
	NewPhoneNumber = ATFE.PhoneNumber,
	FilteredBy = 'A',
	BasedOnAccountAddressId = ATFE.AccountAddressId, 
	BasedOnAccountId = ATFE.AccountId
FROM #AddressFix AWE
	JOIN Address_To_Fix_Error ATFE ON AWE.AccountId = ATFE.AccountId AND AWE.Address1 = ATFE.Address1 AND ATFE.RK = 1;


WITH Address_To_Fix_Error (AccountId, AccountAddressId, State, PostalCode, Address1, City, PhoneNumber, RK)
AS
(
	SELECT 
	A.AccountId, AA.AccountAddressId, AA.State, AA.PostalCode, AA.Address1, AA.City,AA.PhoneNumber,
	ROW_NUMBER() OVER(PARTITION BY A.AccountId 
                            ORDER BY AA.AccountAddressId DESC) AS rk
	FROM 
		Accounts A JOIN
		AccountAddresses AA ON A.AccountId = AA.AccountId JOIN
		#AddressFix AWE ON A.AccountId = AWE.AccountId AND AA.PostalCode = AWE.PostalCode
	WHERE 
	1=1
	AND ISNULL(AA.State,'') <> '' 
	AND ISNULL (AA.PostalCode,'') <> '' 
	AND ISNULL(AA.Address1, '')<> ''
	AND ISNULL (AA.City, '') <> ''
	AND A.AccountId <> 2
	AND 
	(
		ISNULL(AWE.NewState,'') = '' 
		OR ISNULL (AWE.NewPostalCode,'') = '' 
		OR ISNULL(AWE.NewAddress1, '')= ''
		OR ISNULL (AWE.NewCity, '') = ''
	)
)

UPDATE #AddressFix
SET NewState = ATFE.State, 
	NewPostalCode = ATFE.PostalCode, 
	NewAddress1 = ATFE.Address1, 
	NewCity = ATFE.City,
	NewPhoneNumber = ATFE.PhoneNumber,
	FilteredBy = 'P',
	BasedOnAccountAddressId = ATFE.AccountAddressId, 
	BasedOnAccountId = ATFE.AccountId
FROM #AddressFix AWE
	JOIN Address_To_Fix_Error ATFE ON AWE.AccountId = ATFE.AccountId AND AWE.PostalCode = ATFE.PostalCode AND ATFE.RK = 1;

WITH Address_To_Fix_Error (AccountId, AccountAddressId, State, PostalCode, Address1, City, PhoneNumber, RK)
AS
(
	SELECT 
	A.AccountId, AA.AccountAddressId, AA.State, AA.PostalCode, AA.Address1, AA.City,AA.PhoneNumber,
	ROW_NUMBER() OVER(PARTITION BY A.AccountId 
                            ORDER BY AA.AccountAddressId DESC) AS rk
	FROM 
		Accounts A JOIN
		AccountAddresses AA ON A.AccountId = AA.AccountId JOIN
		#AddressFix AWE ON A.AccountId = AWE.AccountId AND AA.City = AWE.City
	WHERE 
	1=1
	AND ISNULL(AA.State,'') <> '' 
	AND ISNULL (AA.PostalCode,'') <> '' 
	AND ISNULL(AA.Address1, '')<> ''
	AND ISNULL (AA.City, '') <> ''
	AND A.AccountId <> 2
	AND 
	(
		ISNULL(AWE.NewState,'') = '' 
		OR ISNULL (AWE.NewPostalCode,'') = '' 
		OR ISNULL(AWE.NewAddress1, '')= ''
		OR ISNULL (AWE.NewCity, '') = ''
	)
)

UPDATE #AddressFix
SET NewState = ATFE.State, 
	NewPostalCode = ATFE.PostalCode, 
	NewAddress1 = ATFE.Address1, 
	NewCity = ATFE.City,
	NewPhoneNumber = ATFE.PhoneNumber,
	FilteredBy = 'C',
	BasedOnAccountAddressId = ATFE.AccountAddressId, 
	BasedOnAccountId = ATFE.AccountId
FROM #AddressFix AWE
	JOIN Address_To_Fix_Error ATFE ON AWE.AccountId = ATFE.AccountId AND AWE.City = ATFE.City AND ATFE.PostalCode like '%'+AWE.PostalCode+'%' AND ATFE.RK = 1
WHERE
	ISNULL (AWE.PostalCode,'') <> '' ;

-- Trying Using Order Payments
WITH Address_To_Fix_Error (AccountId, OrderPaymentId, State, PostalCode, Address1, City, PhoneNumber, RK)
AS
(
	SELECT
		A.AccountId,MAX(OP.OrderPaymentId) AS OrderPaymentMaxId, OP.BillingState, OP.BillingPostalCode, OP.BillingStreetAddress, OP.BillingCity, OP.BillingPhoneNumber,
		ROW_NUMBER() OVER(PARTITION BY A.AccountId 
                            ORDER BY OP.OrderPaymentId DESC) AS RK
	FROM
		Accounts A JOIN 
		OrderCustomers OC ON A.AccountId = OC.AccountId JOIN
		OrderPayments OP ON OC.OrderCustomerId = OP.OrderCustomerId JOIN
		#AddressFix AWE ON A.AccountId = AWE.AccountId
	WHERE
		1=1		
		AND ISNULL(OP.BillingState,'') <> '' 
		AND ISNULL (OP.BillingPostalCode,'') <> '' 
		AND ISNULL(OP.BillingStreetAddress, '')<> ''
		AND ISNULL ( OP.BillingCity, '') <> ''
		AND A.AccountId <> 2
		AND 
		(
			ISNULL(AWE.NewState,'') = '' 
			OR ISNULL (AWE.NewPostalCode,'') = '' 
			OR ISNULL(AWE.NewAddress1, '')= ''
			OR ISNULL (AWE.NewCity, '') = ''
		)
	GROUP BY A.AccountId,OP.OrderPaymentId,OP.BillingState, OP.BillingPostalCode, OP.BillingStreetAddress, OP.BillingCity, OP.BillingPhoneNumber
)

UPDATE #AddressFix
SET NewState = ATFE.State, 
	NewPostalCode = ATFE.PostalCode, 
	NewAddress1 = ATFE.Address1, 
	NewCity = ATFE.City,
	NewPhoneNumber = ATFE.PhoneNumber,
	FilteredBy = 'OA',
	BasedOnOrderPaymentId = ATFE.OrderPaymentId, 
	BasedOnAccountId = ATFE.AccountId
FROM #AddressFix AWE
	JOIN Address_To_Fix_Error ATFE ON AWE.AccountId = ATFE.AccountId AND AWE.Address1 = ATFE.Address1 AND ATFE.RK = 1
WHERE
	AWE.BasedOnAccountAddressId IS NULL;

-- Trying Using Order Payments
WITH Address_To_Fix_Error (AccountId, OrderPaymentId, State, PostalCode, Address1, City, PhoneNumber, RK)
AS
(
	SELECT
		A.AccountId,MAX(OP.OrderPaymentId) AS OrderPaymentMaxId, OP.BillingState, OP.BillingPostalCode, OP.BillingStreetAddress, OP.BillingCity, OP.BillingPhoneNumber,
		ROW_NUMBER() OVER(PARTITION BY A.AccountId 
                            ORDER BY OP.OrderPaymentId DESC) AS RK
	FROM
		Accounts A JOIN 
		OrderCustomers OC ON A.AccountId = OC.AccountId JOIN
		OrderPayments OP ON OC.OrderCustomerId = OP.OrderCustomerId JOIN
		#AddressFix AWE ON A.AccountId = AWE.AccountId
	WHERE
		1=1		
		AND ISNULL(OP.BillingState,'') <> '' 
		AND ISNULL (OP.BillingPostalCode,'') <> '' 
		AND ISNULL(OP.BillingStreetAddress, '')<> ''
		AND ISNULL ( OP.BillingCity, '') <> ''
		AND A.AccountId <> 2
		AND 
		(
			ISNULL(AWE.NewState,'') = '' 
			OR ISNULL (AWE.NewPostalCode,'') = '' 
			OR ISNULL(AWE.NewAddress1, '')= ''
			OR ISNULL (AWE.NewCity, '') = ''
		)
	GROUP BY A.AccountId,OP.OrderPaymentId,OP.BillingState, OP.BillingPostalCode, OP.BillingStreetAddress, OP.BillingCity, OP.BillingPhoneNumber
)

UPDATE #AddressFix
SET NewState = ATFE.State, 
	NewPostalCode = ATFE.PostalCode, 
	NewAddress1 = ATFE.Address1, 
	NewCity = ATFE.City,
	NewPhoneNumber = ATFE.PhoneNumber,
	FilteredBy = 'OP',
	BasedOnOrderPaymentId = ATFE.OrderPaymentId, 
	BasedOnAccountId = ATFE.AccountId
FROM #AddressFix AWE
	JOIN Address_To_Fix_Error ATFE ON AWE.AccountId = ATFE.AccountId AND AWE.PostalCode = ATFE.PostalCode AND ATFE.RK = 1
WHERE
	AWE.BasedOnAccountAddressId IS NULL;

-- Trying Using Order Payments
WITH Address_To_Fix_Error (AccountId, OrderPaymentId, State, PostalCode, Address1, City, PhoneNumber, RK)
AS
(
	SELECT
		A.AccountId,MAX(OP.OrderPaymentId) AS OrderPaymentMaxId, OP.BillingState, OP.BillingPostalCode, OP.BillingStreetAddress, OP.BillingCity, OP.BillingPhoneNumber,
		ROW_NUMBER() OVER(PARTITION BY A.AccountId 
                            ORDER BY OP.OrderPaymentId DESC) AS RK
	FROM
		Accounts A JOIN 
		OrderCustomers OC ON A.AccountId = OC.AccountId JOIN
		OrderPayments OP ON OC.OrderCustomerId = OP.OrderCustomerId JOIN
		#AddressFix AWE ON A.AccountId = AWE.AccountId
	WHERE
		1=1		
		AND ISNULL(OP.BillingState,'') <> '' 
		AND ISNULL (OP.BillingPostalCode,'') <> '' 
		AND ISNULL(OP.BillingStreetAddress, '')<> ''
		AND ISNULL ( OP.BillingCity, '') <> ''
		AND A.AccountId <> 2
		AND 
		(
			ISNULL(AWE.NewState,'') = '' 
			OR ISNULL (AWE.NewPostalCode,'') = '' 
			OR ISNULL(AWE.NewAddress1, '')= ''
			OR ISNULL (AWE.NewCity, '') = ''
		)
	GROUP BY A.AccountId,OP.OrderPaymentId,OP.BillingState, OP.BillingPostalCode, OP.BillingStreetAddress, OP.BillingCity, OP.BillingPhoneNumber
)

UPDATE #AddressFix
SET NewState = ATFE.State, 
	NewPostalCode = ATFE.PostalCode, 
	NewAddress1 = ATFE.Address1, 
	NewCity = ATFE.City,
	NewPhoneNumber = ATFE.PhoneNumber,
	FilteredBy = 'OC',
	BasedOnOrderPaymentId = ATFE.OrderPaymentId, 
	BasedOnAccountId = ATFE.AccountId
FROM #AddressFix AWE
	JOIN Address_To_Fix_Error ATFE ON AWE.AccountId = ATFE.AccountId AND AWE.City = ATFE.City AND ATFE.PostalCode like '%'+AWE.PostalCode+'%' AND ATFE.RK = 1
WHERE
	ISNULL (AWE.PostalCode,'') <> '' 
	AND AWE.BasedOnAccountAddressId IS NULL;


UPDATE OrderShipments
SET State = CASE 
				WHEN ISNULL(OS.State,'') = '' THEN AWE.NewState
				ELSE OS.State
			END, 
	PostCode = 
			CASE 
				WHEN ISNULL(OS.PostCode,'') = '' THEN AWE.NewPostalCode
				ELSE OS.PostCode
			END, 
	Address1 = 
			CASE 
				WHEN ISNULL(OS.Address1,'') = '' THEN AWE.NewAddress1
				ELSE OS.Address1
			END,
	City = 
			CASE 
				WHEN ISNULL(OS.City,'') = '' THEN AWE.NewCity
				ELSE OS.City
			END
FROM
	OrderShipments OS JOIN 
	#AddressFix AWE ON OS.OrderShipmentId = AWE.OrderShipmentId JOIN
	Accounts A ON AWE.AccountId = A.AccountId 
WHERE	
	1=1
	AND ISNULL(AWE.NewState,'') <> '' 
	AND ISNULL (AWE.NewPostalCode,'') <> '' 
	AND ISNULL(AWE.NewAddress1, '')<> ''
	AND ISNULL (AWE.NewCity, '') <> ''
	AND A.AccountId <> 2


-- Result
Select * from #AddressFix where BasedOnAccountId is null;

Select OrderTypeId, Count(AccountId) from #AddressFix where BasedOnAccountId is null Group by OrderTypeId;

Select * from #AddressFix where BasedOnAccountId is not null;

Select OrderTypeId, Count(AccountId) from #AddressFix where BasedOnAccountId is not null Group by OrderTypeId;

--Select	AA.AccountAddressId,
--		AWE.AcountAddressesId,
--		AWE.NewState, 
--		AWE.NewPostalCode, 
--		AWE.NewAddress1, 
--		AWE.NewCity
--FROM AccountAddresses  AA
--	JOIN #AddressFix AWE ON AA.AccountId = AWE.AccountId AND AA.AccountAddressId = AWE.AcountAddressesId
--WHERE	
--	1=1
--	AND ISNULL(AWE.NewState,'') <> '' 
--	AND ISNULL (AWE.NewPostalCode,'') <> '' 
--	AND ISNULL(AWE.NewAddress1, '')<> ''
--	AND ISNULL (AWE.NewCity, '') <> ''
--	AND AA.AccountId <> 2

DROP TABLE #AddressFix 

ROLLBACK TRAN @TransactionName;

END TRY
BEGIN CATCH
   SELECT
    ERROR_NUMBER() AS ErrorNumber
    ,ERROR_SEVERITY() AS ErrorSeverity
    ,ERROR_STATE() AS ErrorState
    ,ERROR_PROCEDURE() AS ErrorProcedure
    ,ERROR_LINE() AS ErrorLine
    ,ERROR_MESSAGE() AS ErrorMessage;

	ROLLBACK TRAN @TransactionName;
END CATCH

