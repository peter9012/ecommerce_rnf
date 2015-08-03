package com.rf.core.website.constants;

public class DBQueries {

	//RFO Queries
	public static String GET_BILLING_ADDRESS_COUNT_QUERY = "select count(*) as count from RFO_Accounts.Addresses where ( addresstypeid = '3' and EndDate IS NULL and AddressId in  (select addressid from RFO_Accounts.AccountContactAddresses where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))";
	public static String GET_SHIPPING_ADDRESS_COUNT_QUERY = "select count(*) as count from RFO_Accounts.Addresses where ( addresstypeid = '2' and EndDate IS NULL and AddressId in  (select addressid from RFO_Accounts.AccountContactAddresses where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))";
	public static String GET_DEFAULT_BILLING_ADDRESS_QUERY = "select * from RFO_Accounts.Addresses where ( addresstypeid = '3' and EndDate IS NULL and IsDefault='1' and AddressId in  (select addressid from RFO_Accounts.AccountContactAddresses where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))";
	public static String GET_DEFAULT_SHIPPING_ADDRESS_FIRST_NAME_QUERY = "select FirstName from RFO_Accounts.AccountContacts where AccountContactId IN (select AccountContactId from RFO_Accounts.AccountContactAddresses  where AddressId IN (select AddressID from RFO_Accounts.Addresses where ( addresstypeid = '2' and IsDefault='1' and EndDate IS NULL and AddressId in  (select addressid from RFO_Accounts.AccountContactAddresses where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))))";
	public static String GET_DEFAULT_BIILING_ADDRESS_PROFILE_NAME_QUERY = "select Top 1 ProfileName from RFO_Accounts.PaymentProfiles where AccountID IN (select AccountID from RFO_Accounts.AccountContacts where AccountContactId IN (select AccountContactId from RFO_Accounts.AccountContactAddresses  where AddressId IN (select AddressID from RFO_Accounts.Addresses where ( addresstypeid = '3' and IsDefault='1' and EndDate IS NULL and AddressId in  (select addressid from RFO_Accounts.AccountContactAddresses where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))))))";
	public static String GET_EXPIRATION_DATE_QUERY = "select * from RFO_Accounts.CreditCardProfiles where PaymentProfileID IN ( select Top 1 paymentprofileid from RFO_Accounts.PaymentProfiles where isDefault=1 and AccountID IN (select accountid from RFO_Accounts.AccountContacts where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))";
	public static String GET_AUTOSHIP_ITEM_DETAILS_QUERY = "select * from Hybris.productbase where productID IN (select productId from Hybris.AutoshipItem where AutoshipId IN ( select autoshipid from Hybris.Autoship where AutoshipTypeID = '2' and AccountID IN (select accountid from RFO_Accounts.AccountContacts where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))))";
	public static String GET_AUTOSHIP_ITEM_TOTAL_PRICE_QUERY = "select TOP 1 price from Hybris.ProductPrice where ProductID IN (select productId from Hybris.AutoshipItem where AutoshipId IN ( select autoshipid from Hybris.Autoship where AutoshipTypeID = '2' and AccountID IN (select accountid from RFO_Accounts.AccountContacts where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))))";	
	public static String GET_ITEMS_IN_ORDER_DESC_QUERY = "select * from Hybris.productbase where productID IN (select productId from Hybris.AutoshipItem where AutoshipId IN ( select autoshipid from Hybris.Autoship where AutoshipTypeID = '2' and AccountID IN (select accountid from RFO_Accounts.AccountContacts where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%S')))))";
	//public static String GET_SHIPPING_ADDRESS_QUERY = "select * from RFO_Accounts.Addresses where ( addresstypeid = '2' and IsDefault='1' and EndDate IS NULL and AddressId in  (select addressid from RFO_Accounts.AccountContactAddresses where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))";
	public static String GET_PC_PERKS_AUTOSHIP_ITEM_DETAILS_QUERY = "select * from Hybris.productbase where productID IN (select productId from Hybris.AutoshipItem where AutoshipId IN ( select autoshipid from Hybris.Autoship where AccountID IN (select accountid from RFO_Accounts.AccountContacts where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))))";
	public static String GET_ACCOUNT_NAME_DETAILS_QUERY = "select top 1 * from RFO_Accounts.AccountContacts where AccountContactId IN (select AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))";
	public static String GET_ACCOUNT_ADDRESS_DETAILS_QUERY = "select top 3 * from RFO_Accounts.Addresses where addressTypeID ='1' and addressId IN (select top 3 AddressID from RFO_Accounts.AccountContactAddresses where accountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))";
	public static String GET_ACCOUNT_PHONE_NUMBER_QUERY_RFO = "select top 1 PhoneNumberRaw from RFO_Accounts.Phones where PhoneID IN (select top 1 PhoneId from RFO_Accounts.AccountContactPhones where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))";
	public static String GET_RETURN_ORDER_DETAILS_QUERY = "select Top 1 ReturnOrderNumber,total from Hybris.ReturnOrder where AccountID IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s')))";
	public static String GET_RETURN_ORDER_STATUS_QUERY = "select Name from RFO_Reference.ReturnStatus where ReturnStatusId IN (select Top 1 ReturnStatusId from Hybris.ReturnOrder where AccountID IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))))";
	public static String GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO = "select Top 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))) order by CompletionDate desc";
	public static String GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFO = "select CompletionDate from Hybris.Orders where OrderNumber IN (select Top 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))) order by CompletionDate desc)";
	public static String GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFO = "select AmountTobeAuthorized from Hybris.OrderPayment where OrderID IN(select OrderId from Hybris.Orders where OrderNumber IN (select Top 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))) order by CompletionDate desc))";
	public static String GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFO = "select Name from RFO_Reference.OrderStatus where orderStatusId IN (select Top 1 OrderStatusID from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress='%s')))order by CompletionDate desc)";
	public static String GET_SHIPPING_ADDRESS_DETAILS_FOR_CONSULTANT_RFL_4289 = "SELECT  os.* "+
			   "FROM    dbo.OrderShipments AS os "+
			   "WHERE   os.OrderID = '%s'";

			 public static String GET_ORDER_DETAILS_FOR_CONSULTANT_RFL_4289 = "SELECT  oc.* "+
			"FROM    dbo.Orders AS o "+
			"JOIN    dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			"WHERE   o.OrderID = '%s'";
			 
			 public static String GET_SHIPPING_METHOD_FOR_CONSULTANT_RFL_4289 = "select * from dbo.ShippingMethods where ShippingMethodID='%s'";
	
	public static String GET_SHIPPING_ADDRESS_QUERY_RFO = "select * from RFO_Accounts.Addresses where "+ 
			"(IsDefault='1' and EndDate IS NULL and AddressId in "+
			"(select top 1 addressid from RFO_Accounts.AccountContactAddresses where AccountContactId IN "+
			"(select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN "+
			"(select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))";

	public static String GET_SUBTOTAL_VALUE_QUERY_RFO = "select SubTotal from Hybris.Autoship where AutoshipID IN "+
			"(select autoshipid from Hybris.Autoship where AccountID IN "+
			"(select accountid from RFO_Accounts.AccountContacts where AccountContactId IN "+
			"(select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN "+ 
			"(select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))";

	public static String GET_SHIPPING_COST_QUERY_RFO = 	"select Top 1 ShippingCost from Hybris.AutoshipShipment where AutoshipID IN "+
			"(select autoshipid from Hybris.Autoship where AccountID IN "+ 
			"(select accountid from RFO_Accounts.AccountContacts where AccountContactId IN "+ 
			"(select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN "+ 
			"(select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))";

	public static String GET_HANDLING_COST_QUERY_RFO = "select top 1 HandlingCost from Hybris.AutoshipShipment where AutoshipID IN "+
			"(select autoshipid from Hybris.Autoship where AccountID IN "+
			"(select accountid from RFO_Accounts.AccountContacts where AccountContactId IN "+ 
			"(select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN "+ 
			"(select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))";

	public static String GET_TAX_AMOUNT_TOTAL_QUERY_RFO ="select * from Hybris.Autoship where AutoshipID IN "+
			"(select top 1 autoshipid from Hybris.Autoship where AccountID IN "+
			"(select accountid from RFO_Accounts.AccountContacts where AccountContactId IN "+
			"(select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN "+ 
			"(select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))";

	public static String GET_GRAND_TOTAL_QUERY_RFO = "select * from Hybris.Autoship where AutoshipID IN "+
			"(select top 1 autoshipid from Hybris.Autoship where AccountID IN "+ 
			"(select accountid from RFO_Accounts.AccountContacts where AccountContactId IN "+ 
			"(select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN "+ 
			"(select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))"; 

	public static String GET_CARD_TYPE_QUERY_RFO = "select Name FROM RFO_Reference.CreditCardVendors where VendorID In "+ 
			"(select VendorID from Hybris.AutoshipPayment where AutoshipID IN "+
			"(select autoshipid from Hybris.Autoship where AccountID IN "+
			"(select accountid from RFO_Accounts.AccountContacts where AccountContactId IN "+ 
			"(select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN "+ 
			"(select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))))";

	//	

	public static String GET_ORDER_NUMBER_QUERY = "select TOP 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress='%s')))order by CompletionDate desc";
	public static String GET_ORDER_STATUS_QUERY = "select Name from RFO_Reference.OrderStatus where orderStatusId IN (select Top 1 OrderStatusID from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress='%s')))order by CompletionDate desc)";
	public static String GET_ORDER_GRAND_TOTAL_QUERY = "select AmountTobeAuthorized from Hybris.OrderPayment where OrderID IN(select OrderId from Hybris.Orders where OrderNumber IN (select Top 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))) order by CompletionDate desc))";
	public static String GET_ORDER_DATE_QUERY = "select CompletionDate from Hybris.Orders where OrderNumber IN (select Top 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))) order by CompletionDate desc)";

	//RFL Queries
	public static String GET_ACCOUNT_DETAILS_QUERY_TST4 = "select top 1 * from dbo.Accounts where emailAddress = '%s'";
	public static String GET_SHIPPING_ADDRESS_COUNT_QUERY_TST4 = "select count(*) as count from dbo.AccountAddresses where addressTypeId = '2' and AccountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')";
	public static String GET_BILLING_ADDRESS_COUNT_QUERY_TST4 = "select count(*) as count from dbo.AccountAddresses where addressTypeId = '3' and AccountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')";
	public static String GET_DEFAULT_SHIPPING_ADDRESS_QUERY_TST4 = "select * from dbo.AccountAddresses where addressTypeId = '2' and IsDefault = '1' and AccountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')";
	public static String GET_DEFAULT_BILLING_ADDRESS_QUERY_TST4 = "select * from dbo.AccountAddresses where addressTypeId = '3' and IsDefault = '1' and AccountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')";
	public static String GET_AUTOSHIP_ORDER_DETAILS_QUERY_TST4 = "select * from dbo.OrderCustomers where OrderID IN (select OrderID from dbo.Orders where orderNumber='%s') ";
	public static String GET_AUTOSHIP_PAYMENT_DETAILS_QUERY_TST4 = "select * from dbo.AccountPaymentMethods where IsDefault='1' and accountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')";
	public static String GET_AUTOSHIP_SHIPPING_METHOD_QUERY_TST4 = "select * from dbo.ShippingMethods where ShippingMethodID IN (select shippingMethodID from dbo.OrderShipments where orderId IN (select orderID from dbo.Orders where OrderNumber = '%s'))";
	public static String GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFL = "select top 1 OrderNumber from dbo.Orders where OrderID IN (select OrderID from dbo.orderCustomers where AccountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')) order by OrderID desc";
	public static String GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFL = "select top 1 StartDate from dbo.Orders where OrderID IN (select OrderID from dbo.orderCustomers where AccountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')) order by OrderID desc";
	public static String GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFL = "select top 1 GrandTotal from dbo.Orders where OrderID IN (select OrderID from dbo.orderCustomers where AccountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')) order by OrderID desc";
	public static String GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFL = "select Name from dbo.OrderStatus where OrderStatusID IN (select top 1 OrderStatusID from dbo.Orders where OrderID IN (select OrderID from dbo.orderCustomers where AccountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')) order by OrderID desc)";
	public static String GET_ORDER_DETAILS_ACTIVE_CONSULTANT_USER_HAVING_ADHOC_ORDER_RFL ="select top 20 * from dbo.Orders"+
			   " join dbo.OrderCustomers ON dbo.OrderCustomers.OrderID = dbo.Orders.OrderID"+
			   " join dbo.OrderShipments ON dbo.OrderShipments.OrderID = dbo.Orders.OrderID"+
			   " join dbo.OrderPayments ON dbo.OrderPayments.OrderID = dbo.Orders.OrderID"+
			   " join dbo.Accounts ON dbo.Accounts.AccountID = dbo.OrderCustomers.AccountID"+
			   " where dbo.Orders.OrderTypeID=7 and dbo.Orders.OrderNumber='%s'";
	public static String GET_SHIPPING_METHOD_QUERY_RFL="select * from dbo.ShippingMethods where ShippingMethodID = '%S'";
	public static String GET_ACCOUNT_NAME_DETAILS_FOR_ACCOUNT_INFO_QUERY_RFL = "select * from dbo.Accounts where AccountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')";
	public static String GET_ACCOUNT_ADDRESS_DETAILS_FOR_ACCOUNT_INFO_QUERY_RFL = "select top 1 * from dbo.AccountAddresses where AddressTypeId='1' and AccountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')";
	public static String GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO = "select top 1 * from RFO_Accounts.Addresses where addressId IN (select top 3 AddressID from RFO_Accounts.AccountContactAddresses where accountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))";
	public static String GET_AUTOSHIP_ADDRESS_QUERY_TST4 = "select top 1 * from dbo.AccountAddresses where addressTypeId = '2' and AccountID IN (select AccountID from dbo.Accounts where emailAddress = '%s')";
	public static String GET_RANDOM_CONSULTANT_EMAIL_ID_RFL = "select top 1 acc.EmailAddress from dbo.accounts acc where  acc.Active=1 and acc.AccountTypeID=1 order by NEWID()";
	public static String GET_RANDOM_PC_USER_EMAIL_ID_RFL = "select top 1 acc.EmailAddress from dbo.accounts acc where  acc.Active=1 and acc.AccountTypeID=2 order by NEWID()";
	public static String GET_RANDOM_RC_EMAIL_ID_RFL = "select top 1 acc.EmailAddress from dbo.accounts acc where  acc.Active=1 and acc.AccountTypeID=3 order by NEWID()";
	public static String GET_PC_USER_HAVING_AUTOSHIP_ORDER_RFL_4300 =

			"USE RodanFieldsLive "+

			   /*
			   STEP #1

			       Pick a random PC order, for an active user
			    */
			    "DECLARE @Orderid INT "+

			   "DECLARE @Accountid INT "+

			   "SELECT TOP 1 @Orderid = o.OrderID "+
			   ",@Accountid = oc.AccountID "+
			   "FROM dbo.Orders AS o "+
			   "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			   "WHERE o.OrderTypeID = 2 "+
			   "AND o.OrderStatusID = 4 "+
			   "AND NOT EXISTS ( "+
			   "SELECT 1 "+
			   "FROM dbo.Accounts AS a "+
			   "WHERE a.Active = 0 "+
			   "AND a.StatusID = 2 "+
			   "AND a.AccountID = oc.AccountID) "+ 
			   "ORDER BY NEWID() "+

			   /*
			   STEP #2
			       Return login to use
			    */
			    "SELECT Username "+
			    ",[Password] "+
			    "FROM RFOperations.[Security].[AccountSecurity] "+
			    "WHERE AccountID = @Accountid "+

			   /*
			   STEP #3
			       Return order header details
			    */
			    "SELECT * "+
			    "FROM dbo.Orders AS o "+
			    "WHERE o.OrderID = @Orderid "+

			   "SELECT * "+
			   "FROM dbo.OrderCustomers AS oc "+
			   "WHERE oc.OrderID = @Orderid "+

			   /*
			   STEP #4
			       Return order items details
			    */
			    "SELECT oi.* "+
			    "FROM dbo.OrderCustomers AS oc "+
			    "JOIN dbo.OrderItems AS oi ON oi.OrderCustomerID = oc.OrderCustomerID "+
			    "WHERE oc.OrderID = @Orderid "+

			   /*
			   STEP #4
			       Return order shipment details
			    */
			    "SELECT * "+
			    "FROM dbo.OrderShipments AS os "+
			    "WHERE os.OrderID = @Orderid "+

			   /*
			   STEP #5
			       Return order shipment items details
			    */
			    "SELECT osi.* "+
			    "FROM dbo.OrderShipments AS os "+
			    "JOIN dbo.OrderShipmentItems AS osi ON osi.OrderShipmentID = os.OrderShipmentID "+
			    "WHERE os.OrderID = @Orderid ";

	public static String GET_RANDOM_CONSULTANT_AUTOSHIP_HEADER_DETAILS_RFL =
			"DECLARE @Orderid INT "+
					"SET @OrderID = '%s' "+
					"select Name from dbo.OrderStatus where OrderStatusID IN (SELECT OrderStatusID "+
					"FROM dbo.Orders AS o "+
					"WHERE o.OrderID = @Orderid )";

	public static String GET_RANDOM_CONSULTANT_AUTOSHIP_PAYMENT_METHOD_DETAILS_RFL =
			"DECLARE @Orderid INT "+
					"SET @OrderID = '%s' "+
					"select shortName FROM ShippingMethods where ShippingMethodId IN (SELECT ShippingMethodId "+
					"FROM dbo.OrderShipments AS os "+
					"WHERE os.OrderID = @Orderid)";

	public static String GET_RANDOM_CONSULTANT_AUTOSHIP_ADDRESS_DETAILS_RFL =
			"DECLARE @Orderid INT "+
					"SET @OrderID = '%s' "+
					"SELECT os.* "+
					"FROM dbo.OrderShipments AS os "+
					"WHERE os.OrderID = @Orderid";

	public static String GET_RANDOM_USER_MULTIPLE_PAYMENTS_RFL = "SELECT TOP 1 a.EmailAddress FROM dbo.Accounts AS a JOIN dbo.AccountSecurity AS [as] ON [as].AccountID=a.AccountID WHERE EXISTS(SELECT 1 FROM dbo.AccountPaymentMethods AS apm WHERE apm.AccountID=a.AccountID GROUP BY apm.AccountID HAVING COUNT(*)>1) ORDER BY NEWID()";
	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_ORDERS_RFL = 
			"SELECT TOP 1 [as].UserName "
					+"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+ 
					"AND a.EnrollmentDate IS NOT NULL "+
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2 "+ 
					"AND a3.AccountID = a.AccountID) "+
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5) "+
					"AND o.OrderStatusID=4) "+ 
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"JOIN dbo.Orders o2 ON o2.ParentOrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+
					"AND ao.AutoshipScheduleID <> 3) "+ 
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"JOIN dbo.Orders o2 ON o2.ParentOrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+
					"AND ao.AutoshipScheduleID = 3) "+ 
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_INACTIVE_RFL_4179 = 
			"SELECT TOP 1 a.EmailAddress "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+/* Consultant*/
					"AND a.EnrollmentDate IS NOT NULL "+
					/*Inactive Account*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2 "+
					"AND a3.AccountID = a.AccountID) "+ 
					/*No Downline*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					/*No Orders */
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5)) "+ 
					/*No CRP*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 1 "+ /*Consultant Replenishment*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					/*No PULSE*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 3 "+ /*Pulse Monthly Subscription*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_INACTIVE_RFL_4181 =

			"SELECT TOP 1 a.EmailAddress "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+/* Consultant*/
					"AND a.EnrollmentDate IS NOT NULL "+
					/*Inactive Account*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2  "+
					"AND a3.AccountID = a.AccountID) "+ 
					/*No Downline*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					/*Failed Orders/ */
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5) "+
					"AND o.OrderStatusID = 2) "+ 
					/*No CRP*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					/*No PULSE*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 3 "+ /*Pulse Monthly Subscription*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_INACTIVE_RFL_4184 =
			"SELECT TOP 1 a.EmailAddress "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+/* Consultant*/
					"AND a.EnrollmentDate IS NOT NULL "+
					/*Inactive Account*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2 "+
					"AND a3.AccountID = a.AccountID) "+ 
					/*No Downline*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					/*No Orders/ */
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5)) "+ 
					/*Has CRP*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					/*No PULSE*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULATNT_CRP_NO_ORDERS_INACTIVE_RFL =

			"SELECT TOP 10 a.emailAddress "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+/* Consultant*/
					"AND a.EnrollmentDate IS NOT NULL "+
					/*Inactive Account*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2 "+
					"AND a3.AccountID = a.AccountID) "+ 
					/*No Downline*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					/*No Orders/ */
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5)) "+ 
					/*Has CRP*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
					"AND o.OrderStatusID=7) "+ 
					/*No PULSE*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
					"AND o.OrderStatusID=7 ) "+/*Submitted Template*/ 
					"ORDER BY NEWID()";



	public static String GET_RANDOM_CONSULATNT_CRP_NO_ORDERS_ACTIVE_RFL =
			"SELECT TOP 1 a.EmailAddress "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+/* Consultant*/
					"AND a.EnrollmentDate IS NOT NULL "+
					/*Inactive Account*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2 "+ 
					"AND a3.AccountID = a.AccountID) "+ 
					/*No Downline*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					/*No Orders/ */
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5)) "+ 
					/*Has CRP*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
					"AND o.OrderStatusID=7/*Submitted Template*/) "+ 
					/*No PULSE*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/ 
					"AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULATNT_NOCRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4186 =

			"SELECT TOP 1 a.EmailAddress "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+/* Consultant*/
					"AND a.EnrollmentDate IS NOT NULL "+
					/*Inactive Account*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2 "+
					"AND a3.AccountID = a.AccountID) "+ 
					/*No Downline*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					/*No Orders/ */
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5)) "+ 
					/*No CRP*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					/*Has PULSE*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4188 =
			"SELECT TOP 1 a.EmailAddress "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+/* Consultant*/
					"AND a.EnrollmentDate IS NOT NULL "+
					/*Inactive Account*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2 "+
					"AND a3.AccountID = a.AccountID) "+ 
					/*No Downline*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					/*No Orders/ */
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5)) "+ 
					/*Has CRP*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					/*Has PULSE*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					"ORDER BY NEWID()";


	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_INACTIVE_RFL_4189 =

			"USE RodanFieldsLive; "+

			"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			"BEGIN TRANSACTION "+

			/*********************************************************************************************
			STEP #1
			    Return 1 random active consultants accounts, with failed orders, has CRP, has PULSE and no downlines 
			 **********************************************************************************************/
			 "SELECT TOP 1 a.AccountID ,[as].UserName "+
			 "FROM dbo.Accounts AS a "+
			 "JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
			 "WHERE a.AccountTypeID = 1 "+/* Consultant*/
			 "AND a.EnrollmentDate IS NOT NULL "+
			 /*Active Account*/
			 "AND NOT EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Accounts AS a3 "+
			 "WHERE a3.Active = 0 "+
			 "AND a3.StatusID = 2 "+
			 "AND a3.AccountID = a.AccountID) "+ 
			 /*No Downline*/
			 "AND NOT EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Accounts AS a2 "+
			 "WHERE a2.SponsorID = a.AccountID) "+ 
			 /*Failed Orders/ */
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID NOT IN (4,5) "+
			 "AND o.OrderStatusID=2) "+ 
			 /*Has CRP*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
			 "AND oc.AccountID=ao.AccountID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
			 "AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
			 "AND o.OrderStatusID=7) "+/*Submitted Template*/ 
			 /*Has PULSE*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
			 "AND oc.AccountID=ao.AccountID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
			 "AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
			 "AND o.OrderStatusID=7) "+/*Submitted Template*/ 
			 "ORDER BY NEWID() "+
			 "COMMIT";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4190 =

			"SELECT TOP 1 a.EmailAddress "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+/* Consultant*/
					"AND a.EnrollmentDate IS NOT NULL "+
					/*Inactive Account*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2 "+
					"AND a3.AccountID = a.AccountID) "+ 
					/*No Downline*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					/*Failed Orders */
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5) "+
					"AND o.OrderStatusID=2) "+ 
					/*Has CRP*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					/*Has PULSE*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_SUBMITTED_ORDERS_ACTIVE_RFL_4191 =

			"USE RodanFieldsLive "+

			"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			"BEGIN TRANSACTION "+
			/*********************************************************************************************
			STEP #1
			    Return 1000 random active consultants accounts, with submitted orders, has CRP, has PULSE and no downlines 
			 **********************************************************************************************/
			 "SELECT TOP 1 a.AccountID "+
			 ",[as].UserName "+
			 "FROM dbo.Accounts AS a "+
			 "JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
			 "WHERE a.AccountTypeID = 1 "+/* Consultant*/
			 "AND a.EnrollmentDate IS NOT NULL "+
			 /*Active Account*/ 
			 "AND NOT EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Accounts AS a3 "+
			 "WHERE a3.Active = 0 "+
			 "AND a3.StatusID = 2 "+
			 "AND a3.AccountID = a.AccountID) "+ 
			 /*No Downline*/
			 "AND NOT EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Accounts AS a2 "+
			 "WHERE a2.SponsorID = a.AccountID) "+ 
			 /*Submitted Orders/ */
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID NOT IN (4,5) "+
			 "AND o.OrderStatusID=4) "+ 
			 /*Has CRP*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
			 "AND oc.AccountID=ao.AccountID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
			 "AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
			 "AND o.OrderStatusID=7) "+/*Submitted Template*/ 
			 /*Has PULSE*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
			 "AND oc.AccountID=ao.AccountID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
			 "AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
			 "AND o.OrderStatusID=7) "+/*Submitted Template*/ 
			 "ORDER BY NEWID()";


	public static String GET_RANDOM_PWS_RFL = 

			"USE RodanFieldsLive "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+

/*********************************************************************************************
STEP #1
    Return 1 random site urls
 **********************************************************************************************/

"SELECT TOP 1 "+
"SUL.URL "+
"FROM    dbo.SiteURLs AS SUL "+
"ORDER BY NEWID()";



	public static String GET_RANDOM_CONSULTANT_NO_PWS_RFL =
			"SELECT TOP 1 "+
					"EmailAddress "+ 
					"FROM    dbo.Accounts AS A "+
					"JOIN    dbo.AccountType AS AT ON AT.AccountTypeID = A.AccountTypeID "+
					"JOIN    dbo.AccountSecurity AS [as] ON [as].AccountID = A.AccountID "+
					"WHERE "+  /*No PWS*/
					"NOT EXISTS ( SELECT 1 "+
					"FROM   dbo.AccountContactInfo AS ACI "+
					"WHERE  A.AccountID = ACI.AccountID ) "+
					/*Active Account*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   dbo.Accounts AS a2 "+
					"WHERE  a2.Active = 0 "+
					"AND a2.StatusID = 2 "+
					"AND a2.AccountID = A.AccountID ) "+
					"AND A.AccountTypeID ='1' "+
					"ORDER BY NEWID() ";



	public static String GET_RANDOM_CONSULTANT_WITH_PWS_RFL =

			"USE RodanFieldsLive "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+

			"SELECT TOP 1 "+
			"A.AccountID , "+
			"S.SiteName , "+
			"[as].UserName "+
			"FROM    dbo.Accounts AS A "+
			"JOIN    dbo.AccountSecurity AS [as] ON [as].AccountID = A.AccountID "+
			"JOIN    dbo.AccountContactInfo AS ACI ON ACI.AccountID = A.AccountID "+
			"JOIN    dbo.Sites AS S ON S.DistributorID = A.AccountID "+
			"WHERE   A.AccountTypeID = 1 "+/*Consultant*/
			/*Active Account*/
			"AND NOT EXISTS ( SELECT 1 "+
			"FROM   dbo.Accounts AS A2 "+
			"WHERE  A2.AccountID = A.AccountID "+
			"AND A2.Active = 0 "+
			"AND A2.StatusID = 2 ) "+
			"ORDER BY NEWID()";


	public static String GET_RANDOM_PC_WHOSE_SPONSOR_HAS_PWS =
			"USE RodanFieldsLive "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+

			"BEGIN TRANSACTION "+
			"SELECT TOP 1 "+
			"A.AccountID , "+
			"[as].UserName , "+
			"S2.SiteName AS Sponsor_PWS "+
			"FROM    dbo.Accounts AS A "+
			"JOIN    dbo.AccountSecurity AS [as] ON [as].AccountID = A.AccountID "+
			"JOIN    dbo.Accounts AS A2 ON A2.AccountID = A.SponsorID "+
			"JOIN    dbo.AccountContactInfo AS ACI2 ON ACI2.AccountID = A2.AccountID "+
			"JOIN    dbo.Sites AS S2 ON S2.DistributorID = A2.AccountID "+
			"WHERE   A.AccountTypeID = 2 "+/*Preferred Customer*/
			/*Active Account*/
			"AND NOT EXISTS ( SELECT 1 "+
			"FROM   dbo.Accounts AS A2 "+
			"WHERE  A2.AccountID = A.AccountID "+
			"AND A2.Active = 0 "+
			"AND A2.StatusID = 2 ) "+
			"ORDER BY NEWID()";


	public static String GET_RANDOM_PC_WITH_NO_SPONSOR = 
			"USE RodanFieldsLive "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+
					"SELECT TOP 1 "+
					"A.AccountID , "+
					"[as].UserName "+
					"FROM    dbo.Accounts AS A "+
					"JOIN    dbo.AccountSecurity AS [as] ON [as].AccountID = A.AccountID "+
					"WHERE   A.AccountTypeID = 2 "+/*Preferred Customer*/
					/*No sponsor*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   dbo.Accounts AS A2 "+
					"WHERE  A2.AccountID = A.SponsorID ) "+
					/*Active Account*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   dbo.Accounts AS A2 "+
					"WHERE  A2.AccountID = A.AccountID "+
					"AND A2.Active = 0 "+
					"AND A2.StatusID = 2 ) "+
					"ORDER BY NEWID()";


	public static String GET_RANDOM_PC_WHOSE_SPONSOR_HAS_NOPWS =
			"USE RodanFieldsLive "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+

	"BEGIN TRANSACTION "+

	"SELECT TOP 1 "+
	"A.AccountID , "+
	"[as].UserName , "+
	"A.SponsorID "+
	"FROM    dbo.Accounts AS A "+
	"JOIN    dbo.AccountSecurity AS [as] ON [as].AccountID = A.AccountID "+
	"WHERE   A.AccountTypeID = 2 "+/*Preferred Customer*/
	/*No PWS sponsor*/
	"AND NOT EXISTS ( SELECT 1 "+
	"FROM   dbo.Accounts AS A2 "+
	"JOIN   dbo.AccountContactInfo AS ACI2 ON ACI2.AccountID = A2.AccountID "+
	"JOIN   dbo.Sites AS S2 ON S2.DistributorID = A2.AccountID "+
	"WHERE  A2.AccountID = A.SponsorID ) "+
	/*Active Account*/
	"AND NOT EXISTS ( SELECT 1 "+
	"FROM   dbo.Accounts AS A2 "+
	"WHERE  A2.AccountID = A.AccountID "+
	"AND A2.Active = 0 "+
	"AND A2.StatusID = 2 ) "+
	"ORDER BY NEWID()";

	public static String GET_RANDOM_RC_RFL = "select top 1 emailAddress from dbo.accounts  where accountTypeId='3' and active = '1' order by newID()";

	public static String GET_RANDOM_RC_HAVING_SUBMITTED_ORDERS_RFL =

			"USE RodanFieldsLive "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+
					"SELECT TOP 1 "+
					"EmailAddress "+
					"FROM    dbo.Accounts AS a "+
					"JOIN    dbo.AccountType AS AT ON AT.AccountTypeID = a.AccountTypeID "+
					"JOIN    dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE "+  /*Active Account*/
					"EXISTS ( SELECT 1 "+
					"FROM   dbo.Accounts AS a3 "+
					"WHERE  a3.Active = 1 "+
					"AND a3.StatusID = 1 "+
					"AND a3.AccountID = a.AccountID ) "+
					/*Active template*/
					"AND EXISTS ( SELECT 1 "+
					"FROM   dbo.Orders AS O "+
					"JOIN   dbo.OrderCustomers OC ON OC.OrderID = O.OrderID "+
					"WHERE  OC.AccountID = a.AccountID "+
					"AND O.OrderTypeID IN ( 4, 5 ) "+/*PC Auto-ship Template, Consultant Auto-ship Template*/
					"AND O.OrderStatusID = 7 ) "+/*Submitted Template*/
					/*Submitted orders*/
					"AND EXISTS ( SELECT 1 "+
					"FROM   dbo.Orders AS O "+
					"JOIN   dbo.OrderCustomers OC ON OC.OrderID = O.OrderID "+
					"WHERE  OC.AccountID = a.AccountID "+
					"AND O.OrderStatusID = 4 ) "+/*Submitted*/
					/*Failed orders*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   dbo.Orders AS O "+
					"JOIN   dbo.OrderCustomers OC ON OC.OrderID = O.OrderID "+
					"WHERE  OC.AccountID = a.AccountID "+
					"AND O.OrderStatusID = 2 ) "+/*Pending Error*/
					/*Returned orders*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   dbo.Orders AS O "+
					"JOIN   dbo.OrderCustomers OC ON OC.OrderID = O.OrderID "+
					"WHERE  OC.AccountID = a.AccountID "+
					"AND O.OrderTypeID = 9) "+/*Return*/
					"AND a.AccountTypeID = 3 "+
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_HAVING_SUBMITTED_ORDERS_RFL =

			"USE RodanFieldsLive "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+
					"SELECT TOP 1 "+
					"EmailAddress "+
					"FROM    dbo.Accounts AS a "+
					"JOIN    dbo.AccountType AS AT ON AT.AccountTypeID = a.AccountTypeID "+
					"JOIN    dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE "+  /*Active Account*/
					"EXISTS ( SELECT 1 "+
					"FROM   dbo.Accounts AS a3 "+
					"WHERE  a3.Active = 1 "+
					"AND a3.StatusID = 1 "+
					"AND a3.AccountID = a.AccountID ) "+
					/*Active template*/
					"AND EXISTS ( SELECT 1 "+
					"FROM   dbo.Orders AS O "+
					"JOIN   dbo.OrderCustomers OC ON OC.OrderID = O.OrderID "+
					"WHERE  OC.AccountID = a.AccountID "+
					"AND O.OrderTypeID IN ( 4, 5 ) "+/*PC Auto-ship Template, Consultant Auto-ship Template*/
					"AND O.OrderStatusID = 7 ) "+/*Submitted Template*/
					/*Submitted orders*/
					"AND EXISTS ( SELECT 1 "+
					"FROM   dbo.Orders AS O "+
					"JOIN   dbo.OrderCustomers OC ON OC.OrderID = O.OrderID "+
					"WHERE  OC.AccountID = a.AccountID "+
					"AND O.OrderStatusID = 4 ) "+/*Submitted*/
					/*Failed orders*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   dbo.Orders AS O "+
					"JOIN   dbo.OrderCustomers OC ON OC.OrderID = O.OrderID "+
					"WHERE  OC.AccountID = a.AccountID "+
					"AND O.OrderStatusID = 2 ) "+/*Pending Error*/
					/*Returned orders*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   dbo.Orders AS O "+
					"JOIN   dbo.OrderCustomers OC ON OC.OrderID = O.OrderID "+
					"WHERE  OC.AccountID = a.AccountID "+
					"AND O.OrderTypeID = 9) "+/*Return*/
					"AND a.AccountTypeID = 1 "+
					"ORDER BY NEWID()";




	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_SUBMITTED_ORDERS_INACTIVE_RFL_4192 =
			"SELECT TOP 1 a.EmailAddress "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+/* Consultant*/
					"AND a.EnrollmentDate IS NOT NULL "+
					/*Inactive Account*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2 "+
					"AND a3.AccountID = a.AccountID) "+ 
					/*No Downline*/
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					/*Submitted Orders/ */
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5) "+
					"AND o.OrderStatusID=4) "+ 
					/*Has CRP*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					/*Has PULSE*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 3 "+ /*Pulse Monthly Subscription*/
					"AND o.OrderStatusID=7) "+ /*Submitted Template*/ 
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_ACTIVE_RFL_4193 = 
			"USE RodanFieldsLive "+

			"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			"BEGIN TRANSACTION "+
			/*********************************************************************************************
			STEP #1
			    Return 1 random active consultants accounts, with failed orders, has CRP, has PULSE and downlines 
			 **********************************************************************************************/
			 "SELECT TOP 1 a.AccountID "+
			 ",[as].UserName "+
			 "FROM dbo.Accounts AS a "+
			 "JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
			 "WHERE a.AccountTypeID = 1 "+/* Consultant*/
			 "AND a.EnrollmentDate IS NOT NULL "+
			 /*Active Account*/
			 "AND NOT EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Accounts AS a3 "+
			 "WHERE a3.Active = 0 "+
			 "AND a3.StatusID = 2 "+
			 "AND a3.AccountID = a.AccountID) "+ 
			 /*Has Downline*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Accounts AS a2 "+
			 "WHERE a2.SponsorID = a.AccountID) "+
			 /*Failed Orders/ */
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID NOT IN (4,5) "+
			 "AND o.OrderStatusID=2) "+ 
			 /*Has CRP*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
			 "AND oc.AccountID=ao.AccountID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
			 "AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
			 "AND o.OrderStatusID=7) "+/*Submitted Template*/ 
			 /*Has PULSE*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
			 "AND oc.AccountID=ao.AccountID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
			 "AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
			 "AND o.OrderStatusID=7) "+/*Submitted Template*/ 
			 "ORDER BY NEWID() "+
			 "COMMIT";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_INACTIVE_RFL_4194 =

			"SELECT TOP 1 a.EmailAddress "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+/* Consultant*/
					"AND a.EnrollmentDate IS NOT NULL "+
					/*Inactive Account*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2 "+
					"AND a3.AccountID = a.AccountID) "+ 
					/*Has Downline*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					/*Failed Orders/ */
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5) "+
					"AND o.OrderStatusID=2) "+ 
					/*Has CRP*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					/*Has PULSE*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_SUBMITTED_ORDERS_INACTIVE_RFL_4195 =
			"USE RodanFieldsLive "+

			"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			"BEGIN TRANSACTION "+
			/*********************************************************************************************
			STEP #1
			    Return 1 random active consultants accounts, with submitted orders, has CRP, has PULSE and downlines 
			 **********************************************************************************************/
			 "SELECT TOP 1 a.AccountID "+
			 ",[as].UserName "+
			 "FROM dbo.Accounts AS a "+
			 "JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
			 "WHERE a.AccountTypeID = 1 "+/* Consultant*/
			 "AND a.EnrollmentDate IS NOT NULL "+
			 /*Active Account*/
			 "AND NOT EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Accounts AS a3 "+
			 "WHERE a3.Active = 0 "+
			 "AND a3.StatusID = 2 "+
			 "AND a3.AccountID = a.AccountID) "+ 
			 /*Has Downline*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Accounts AS a2 "+
			 "WHERE a2.SponsorID = a.AccountID) "+ 
			 /*Submitted Orders/ */
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID NOT IN (4,5) "+
			 "AND o.OrderStatusID=4) "+ 
			 /*Has CRP*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
			 "AND oc.AccountID=ao.AccountID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
			 "AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
			 "AND o.OrderStatusID=7) "+/*Submitted Template*/ 
			 /*Has PULSE*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
			 "AND oc.AccountID=ao.AccountID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
			 "AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
			 "AND o.OrderStatusID=7) "+/*Submitted Template*/ 
			 "ORDER BY NEWID() "+
			 "COMMIT";


	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_HAS_SUBMITTED_ORDERS_INACTIVE_RFL_4196 =

			"SELECT TOP 1 a.EmailAddress "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					"WHERE a.AccountTypeID = 1 "+/* Consultant*/
					"AND a.EnrollmentDate IS NOT NULL "+
					/*Inactive Account*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a3 "+
					"WHERE a3.Active = 0 "+
					"AND a3.StatusID = 2 "+
					"AND a3.AccountID = a.AccountID) "+ 
					/*Has Downline*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a2 "+
					"WHERE a2.SponsorID = a.AccountID) "+ 
					/*Submitted Orders/ */
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID NOT IN (4,5) "+
					"AND o.OrderStatusID=4) "+ 
					/*Has CRP*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 1 "+/*Consultant Replenishment*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					/*Has PULSE*/
					"AND EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
					"AND oc.AccountID=ao.AccountID "+
					"WHERE oc.AccountID = a.AccountID "+
					"AND o.OrderTypeID =5 "+/*Consultant Auto-ship Template*/
					"AND ao.AutoshipScheduleID = 3 "+/*Pulse Monthly Subscription*/
					"AND o.OrderStatusID=7) "+/*Submitted Template*/ 
					"ORDER BY NEWID()";

	public static String GET_ACCOUNTS_WITH_NULL_EMAIL_ADDRESS =
			"SELECT TOP 1 a.AccountID "+
					", [as].UserName "+
					"FROM dbo.Accounts AS a "+
					"JOIN dbo.AccountSecurity AS [as] ON [as].AccountID=a.AccountID "+
					"WHERE a.EmailAddress IS NULL "+
					"ORDER BY NEWID()";

	public static String GET_RANDOM_PWS_SITE_URL_RFL =
			"SELECT TOP 1 "+
					"SUL.URL "+
					"FROM    dbo.SiteURLs AS SUL where URL Like '%.biz'"+
					"ORDER BY NEWID() ";

	public static String GET_RANDOM_INACTIVE_ACCOUNT_NO_AUTOSHIP_TEMPLATE_4182 = 

			"USE RodanFieldsLive "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+

			/*********************************************************************************************
			STEP #1
			    Return 1000 random inactive accounts.
			 **********************************************************************************************/
			 "SELECT TOP 1 a.EmailAddress "+
			 "FROM dbo.Accounts AS a "+
			 "JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
			 "WHERE a.Active = 0 "+
			 "AND a.StatusID = 2 "+
			 "ORDER BY NEWID() "+

			/*********************************************************************************************
			STEP #2
			    Load the list of Auto-ships, for an inactive user
			 **********************************************************************************************/
			 "IF OBJECT_ID('tempdb..#AccountList') IS NOT NULL "+
			 "BEGIN "+
			 "DROP TABLE #AccountList "+
			 "END "+

			"IF OBJECT_ID('tempdb..#DS') IS NOT NULL "+
			"BEGIN "+
			"DROP TABLE #DS "+
			"END "+

			"SELECT DISTINCT oc.AccountID "+
			",o.OrderID INTO #AccountList "+
			"FROM dbo.Orders AS o "+
			"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			"WHERE o.OrderTypeID IN (4,5) "+/*PC Auto-ship Template,Consultant Auto-ship Template*/
			"AND o.OrderStatusID = 7 "+/*Submitted Template*/
			"AND EXISTS ( "+
			"SELECT 1 "+
			"FROM dbo.Accounts AS a "+
			"WHERE a.Active = 0 "+
			"AND a.StatusID = 2 "+
			"AND a.AccountID = oc.AccountID) "+

			/*********************************************************************************************
			STEP #3
			    Get the next due date for the auto ships
			 **********************************************************************************************/
			 "SELECT al.AccountID "+
			 ",ao.TemplateOrderID "+
			 ",dbo.udf_get_autoship_nextduedate (ao.TemplateOrderID,[as].IntervalTypeID,ao.DateLastCreated,ao.IntervalDayOverride) AS NextDueDate "+
			 ",ac.Username INTO #DS "+
			 "FROM dbo.AccountSecurity AS ac "+
			 "JOIN #AccountList AS al ON ac.AccountID = al.AccountID "+
			 "JOIN dbo.AutoshipOrders AS ao ON al.OrderID = ao.TemplateOrderID "+
			 "JOIN dbo.AutoshipSchedules AS [as] ON [as].AutoshipScheduleID = ao.AutoshipScheduleID "+

			/*********************************************************************************************
			STEP #4
			    Return data ordered by most recent due date, fill free to remove the filter if needed, the filter is used to get only future autoships
			 **********************************************************************************************/
			 "SELECT AccountID "+
			 ",Username "+
			 ",TemplateOrderID "+
			 ",NextDueDate "+
			 "FROM #DS "+
			 "WHERE NextDueDate >= GETDATE() "+ 
			 "ORDER BY NextDueDate";

	public static String GET_RANDOM_ENROLLED_RC_USER_HAS_FAILED_ORDER_RFL_4200 =
			"USE RodanFieldsLive "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+
					/*
			   STEP #1
			       Return 1 random RC accounts, with failed orders
					 */
					 "SELECT TOP 1 a.AccountID "+
					 ",[as].UserName "+
					 "FROM dbo.Accounts AS a "+
					 "JOIN dbo.AccountSecurity AS [as] ON [as].AccountID = a.AccountID "+
					 "WHERE a.AccountTypeID = 3 "+ /*Retail Customer*/
					 "AND a.EnrollmentDate IS NOT NULL "+
					 /*Failed orders*/
					 "AND EXISTS ( "+
					 "SELECT 1 "+
					 "FROM dbo.Orders AS o "+
					 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					 "WHERE oc.AccountID = a.AccountID "+
					 "AND o.OrderTypeID NOT IN (4,5) "+ 
					 "AND o.OrderStatusID = 2) "+ 
					 "ORDER BY NEWID() ";

	public static String GET_RANDOM_PC_HAS_CRP_PULSE_SUBMITTED_ORDERS_RFL_4205 = 
			/*
			   Test Case Hybris Phase 2-4205 :: Version : 1 :: Enrolled PC, Has CRP/ Has Pulse, Has Submitted Orders 
			   Author: Adrian Calvo
			   Date:   2015-07-16
			 */
			"USE RodanFieldsLive "+
			"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
			"BEGIN TRANSACTION "+
			/*
			   STEP #1
			       Return 1000 random pc accounts, with submitted orders, has CRP, has PULSE. 
			 */
			 "SELECT TOP 1000 a.AccountID "+
			 ", [as].UserName "+
			 "FROM dbo.Accounts AS a "+
			 "JOIN dbo.AccountSecurity AS [as] ON [as].AccountID=a.AccountID "+
			 "WHERE a.AccountTypeID=2 "+ /*Preferred Customer*/
			 "AND a.EnrollmentDate IS NOT NULL "+
			 /*Submitted Orders */
			 "AND EXISTS( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID=o.OrderID "+
			 "WHERE oc.AccountID=a.AccountID "+
			 "AND o.OrderTypeID NOT IN(4, 5) "+
			 "AND o.OrderStatusID=4) "+
			 /*Has CRP*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
			 "AND oc.AccountID=ao.AccountID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID =4 "+ /*PC Auto-ship Template*/
			 "AND ao.AutoshipScheduleID = 2 "+/*PC Bi-Monthly Replenishment*/
			 "AND o.OrderStatusID=7 )"+/*Submitted Template*/
			 /*Has PULSE*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM dbo.Orders AS o "+
			 "JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
			 "JOIN dbo.AutoshipOrders ao ON ao.TemplateOrderID = o.OrderID "+
			 "AND oc.AccountID=ao.AccountID "+
			 "WHERE oc.AccountID = a.AccountID "+
			 "AND o.OrderTypeID =4 "+ /*PC Auto-ship Template*/
			 "AND ao.AutoshipScheduleID = 3 "+ /*Pulse Monthly Subscription*/
			 "AND o.OrderStatusID=7) "+/*Submitted Template*/ 
			 "ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_FAILED_ORDERID_AND_ACCOUNT_ID_RFL_4294 = 

			"SELECT TOP 1 o.OrderID "+
					",oc.AccountID "+
					"FROM dbo.Orders AS o "+
					"JOIN dbo.OrderCustomers AS oc ON oc.OrderID = o.OrderID "+
					"WHERE o.OrderTypeID = 7 "+
					"AND o.OrderStatusID = 2 "+
					"AND NOT EXISTS ( "+
					"SELECT 1 "+
					"FROM dbo.Accounts AS a "+
					"WHERE a.Active = 0 "+
					"AND a.StatusID = 2 "+
					"AND a.AccountID = oc.AccountID) "+ 
					"ORDER BY NEWID() ";


	/**
	 * 
	 * @param query
	 * @param value
	 * @return
	 */

	public static String callQueryWithArguement(String query,String value){
		return String.format(query, value);
	}
}



