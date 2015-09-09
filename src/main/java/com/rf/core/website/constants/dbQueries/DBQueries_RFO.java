package com.rf.core.website.constants.dbQueries;

public class DBQueries_RFO {

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
	public static String GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO = "select top 1 * from RFO_Accounts.Addresses where addressId IN (select top 3 AddressID from RFO_Accounts.AccountContactAddresses where accountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))";
	public static String GET_AUTOSHIP_ID_FOR_RFO = "SELECT * "+
			"FROM Hybris.Autoship AS A "+
			"WHERE A.AutoshipNumber='%s'";

	public static String GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO = "SELECT  * "+
			"FROM    Hybris.AutoshipShippingAddress AS ASA "+
			"WHERE   ASA.AutoShipID = '%s'";

	public static String GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO = "SELECT  SM.Name AS ShippingMethod , "+
			"ASH.* "+
			"FROM    Hybris.AutoshipShipment AS ASH "+
			"JOIN    RFO_Reference.ShippingMethod AS SM ON SM.ShippingMethodID = ASH.ShippingMethodID "+
			"WHERE   ASH.AutoshipID = '%s'";

	public static String GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO = "SELECT  AST.Name AS AutoShipType , "+
			"AST2.Name AS AutoshipStatus , "+
			"A.* "+
			"FROM    Hybris.Autoship AS A "+
			"JOIN    RFO_Reference.AutoShipType AS AST ON AST.AutoShipTypeID = A.AutoshipTypeID "+
			"JOIN    RFO_Reference.AutoshipStatus AS AST2 ON AST2.AutoshipStatusId = A.AutoshipStatusID "+
			"WHERE   A.AutoshipID = '%s'";

	public static String GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO =
			/*********************************************************************************************
			Query on RFO having active (i.e statusId =’1’ ) pc only with active pc-autoship template with pending autoship and pending/submitted adhoc orders.
			 **********************************************************************************************/
			"USE RFOperations "+
			"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
			"BEGIN TRANSACTION "+
			"SELECT TOP 1 "+
			"ab.AccountID , "+
			"[as].Username "+
			"FROM    RFO_Accounts.AccountBase AS ab "+
			"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE   ab.CountryID = 236 "+
			"AND ab.AccountTypeID = 2 "+/*Preferred Customer*/
			/*Active Accounts*/
			"AND NOT EXISTS ( SELECT 1 "+
			"FROM   RFO_Accounts.AccountRF AS ar "+
			"WHERE  ar.Active = 0 "+
			"AND ar.HardTerminationDate IS NOT NULL "+
			"AND ar.AccountID = ab.AccountID ) "+ 
			/*Pending/Submitted Orders */
			"AND EXISTS ( SELECT 1 "+
			"FROM   Hybris.Orders AS o "+
			"WHERE  o.AccountID = ab.AccountID "+
			"AND o.OrderTypeID = 2 "+/*PC*/
			"AND o.OrderStatusID = 2 ) "+ 
			/*Active Template*/
			"AND EXISTS ( SELECT 1 "+
			"FROM   Hybris.Autoship AS a "+
			"WHERE  a.AccountID = ab.AccountID "+
			"AND a.AutoshipTypeID = 1 "+/*PC Auto-ship Template*/
			"AND a.Active = 1 ) "+
			"ORDER BY NEWID()";

	public static String GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO =
			/*********************************************************************************************
			Need a query on RFO having active (i.e statusId =’1’ ) RC only  having pending/submitted adhoc orders.
			 **********************************************************************************************/
			"USE RFOperations "+
			"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
			"BEGIN TRANSACTION "+
			"SELECT TOP 1 "+
			"ab.AccountID , "+
			"[as].Username "+
			"FROM    RFO_Accounts.AccountBase AS ab "+
			"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE   ab.CountryID = 236 "+
			"AND ab.AccountTypeID = 3 "+/*Retail Customer*/
			/*Active Accounts*/
			"AND NOT EXISTS ( SELECT 1 "+
			"FROM   RFO_Accounts.AccountRF AS ar "+
			"WHERE  ar.Active = 0 "+
			"AND ar.HardTerminationDate IS NOT NULL "+
			"AND ar.AccountID = ab.AccountID ) "+ 
			/*Pending/Submitted Orders */
			"AND EXISTS ( SELECT 1 "+
			"FROM   Hybris.Orders AS o "+
			"WHERE  o.AccountID = ab.AccountID "+
			"AND o.OrderTypeID = 1 "+/*PC*/
			"AND o.OrderStatusID = 2 ) "+ 
			"ORDER BY NEWID()";

	public static String GET_RANDOM_RC_RFO = 
			"USE RFOperations "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			"BEGIN TRANSACTION "+
			"SELECT TOP 1 "+
			"ab.AccountID , "+
			"[as].Username "+
			"FROM    RFO_Accounts.AccountBase AS ab "+
			"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE   ab.CountryID = 236 "+
			"AND ab.AccountTypeID = 3 "+/*Retail Customer*/
			/*Active Accounts*/
			"AND NOT EXISTS ( SELECT 1 "+
			"FROM   RFO_Accounts.AccountRF AS ar "+
			"WHERE  ar.Active = 0 "+
			"AND ar.HardTerminationDate IS NOT NULL "+
			"AND ar.AccountID = ab.AccountID ) "+
			"ORDER BY NEWID()";

	public static String GET_RANDOM_PC_WHOSE_SPONSOR_HAS_PWS_RFO =

			"USE RFOperations "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+
					"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username , "+
					"'http://' + S2.SitePrefix + '.' + SD2.Name AS Sponsor_PWS "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"JOIN    RFO_Accounts.AccountBase AS AB2 ON AB2.AccountID = ar.SponsorId "+
					"JOIN    RFO_Accounts.ConsultantPWSInfo AS CPI2 ON CPI2.AccountId = AB2.AccountID "+
					"JOIN    Hybris.Sites AS S2 ON S2.AccountID = AB2.AccountID "+
					"JOIN    Hybris.SiteURLs AS SUL2 ON SUL2.SiteID = S2.SiteID "+
					"JOIN    Hybris.SiteDomain AS SD2 ON SD2.SiteDomainID = SUL2.SiteDomainID "+
					"WHERE   ab.CountryID = 236 "+
					"AND ab.AccountTypeID = 2 "+/*Preferred Customer*/
					/*Active Account*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS AR2 "+
					"WHERE  AR2.AccountID = ar.AccountID "+
					"AND ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL ) "+
					"ORDER BY NEWID()";

	public static String GET_RANDOM_PC_WHOSE_SPONSOR_HAS_NOPWS_RFO =
			"USE RFOperations "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+
					"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username , "+
					"ar.SponsorId "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"WHERE   ab.CountryID = 236 "+
					"AND ab.AccountTypeID = 2 "+/*Preferred Customer*/
					/*No PWS sponsor*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountBase AS AB2 "+
					"JOIN   RFO_Accounts.ConsultantPWSInfo AS CPI2 ON CPI2.AccountId = AB2.AccountID "+
					"JOIN   Hybris.Sites AS S2 ON S2.AccountID = AB2.AccountID "+
					"WHERE  AB2.AccountID = ar.SponsorId ) "+
					/*Active Account*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS AR2 "+
					"WHERE  AR2.AccountID = ar.AccountID "+
					"AND ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL ) "+
					"ORDER BY NEWID()";

	public static String GET_RANDOM_PC_WITH_NO_SPONSOR_RFO = 
			"USE RFOperations "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+
					"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"WHERE   ab.CountryID = 236 "+
					"AND ab.AccountTypeID = 2 "+/*Preferred Customer*/
					/*No Sponsor*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountBase AS AB2 "+
					"WHERE  AB2.AccountID = ar.SponsorId ) "+
					/*Active Account*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS AR2 "+
					"WHERE  AR2.AccountID = ar.AccountID "+
					"AND ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL ) "+
					"ORDER BY NEWID()";


	public static String GET_ORDER_ID_ACCOUNT_ID_4286_RFO =
			"SELECT TOP 1 "+
					"O.OrderID , "+
					"AB.AccountID "+
					"FROM    Hybris.Orders AS O "+
					"JOIN    RFO_Accounts.AccountBase AS AB ON AB.AccountID = O.AccountID "+
					"WHERE   O.OrderTypeID = 10 "+/*Consultant Auto-ship*/
					"AND O.OrderStatusID = 2 "+ /*Submitted*/
					"AND AB.CountryID = 236 "+
					/*Active Accounts*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS ar "+
					"WHERE  ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL "+
					"AND ar.AccountID = AB.AccountID ) "+
					"ORDER BY NEWID() ";

	public static String GET_ACTIVE_CONSULTANT_USER_WITH_ACTIVE_CRP_AUTOSHIP_4286_RFO =
			"SELECT  Username "+
					"FROM    RFOperations.[Security].[AccountSecurity] "+
					"WHERE   AccountID ='%s' ";

	public static String GET_ORDER_DETAILS_FOR_4286_RFO =  
			"SELECT  * "+
					"FROM    Hybris.Orders AS O "+
					"WHERE   O.OrderID ='%s' ";
	public static String GET_SHIPPING_COST_HANDLING_COST_FOR_4286_RFO =
			"SELECT  * "+
					"FROM    Hybris.OrderShipment AS OS "+
					"WHERE   OS.OrderID = '%s' ";

	public static String GET_SHIPPING_METHOD_QUERY_RFO = 
			"select * from RFO_Reference.ShippingMethod where shippingmethodid='%s' ";


	public static String GET_ORDER_ID_ACCOUNT_ID_4287_RFO =
			"SELECT TOP 1 "+
					"O.OrderID , "+
					"AB.AccountID "+
					"FROM    Hybris.Orders AS O "+
					"JOIN    RFO_Accounts.AccountBase AS AB ON AB.AccountID = O.AccountID "+
					"WHERE   O.OrderTypeID = 3 "+/*Consultant*/
					"AND AB.CountryID = 236 "+
					"AND O.OrderStatusID = 2 "+ /*Submitted*/
					/*Active Accounts*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS ar "+
					"WHERE  ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL "+
					"AND ar.AccountID = AB.AccountID ) "+
					"ORDER BY NEWID() ";

	public static String GET_ORDER_ID_ACCOUNT_ID_4292_RFO = 
			"SELECT TOP 1 "+
					"O.OrderID , "+
					"AB.AccountID "+
					"FROM    Hybris.Orders AS O "+
					"JOIN    Hybris.Autoship AS A ON A.AutoshipID = O.AutoShipID "+
					"JOIN    RFO_Accounts.AccountBase AS AB ON AB.AccountID = O.AccountID "+
					"WHERE   AB.CountryID = 236 "+
					"AND A.AutoshipTypeID = 1 "+/*PC Auto-ship Template*/
					"AND O.OrderStatusID = 2 "+ /*Submitted*/
					/*Active Accounts*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS ar "+
					"WHERE  ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL "+
					"AND ar.AccountID = AB.AccountID ) "+
					"ORDER BY NEWID() ";
	public static String GET_SHIPPING_COST_HANDLING_COST_FOR_4292_RFO = 
			"SELECT  * "+
					"FROM    Hybris.OrderShipment AS OS "+
					"WHERE   OS.OrderID = '%s' ";
	public static String GET_ORDER_DETAILS_FOR_4292_RFO = 
			"SELECT  * "+
					"FROM    Hybris.Orders AS O "+
					"WHERE   O.OrderID = '%s' ";

	public static String GET_USERNAME_BY_ACCOUNT_ID_RFO = 
			"SELECT  Username "+
					"FROM    RFOperations.[Security].[AccountSecurity] "+
					"WHERE   AccountID ='%s' ";

	public static String GET_ORDERID_RFO = "select OrderID from Hybris.orders where OrderNumber = '%S'";

	public static String GET_ACTIVE_CONSULTANT_WITH_ADHOC_ORDER_4287_RFO =
			"SELECT  Username "+
					"FROM    RFOperations.[Security].[AccountSecurity] "+
					"WHERE   AccountID ='%s' ";

	public static String GET_ORDER_DETAILS_FOR_4287_RFO = 
			"SELECT  * "+
					"FROM    Hybris.Orders AS O "+
					"WHERE   O.OrderID = '%s' "; 

	public static String GET_SHIPPING_COST_HANDLING_COST_FOR_4287_RFO = 
			"SELECT  * "+
					"FROM    Hybris.OrderShipment AS OS "+
					"WHERE   OS.OrderID = '%s' ";

	public static String GET_ORDER_ID_ACCOUNT_ID_4291_RFO = 
			"SELECT TOP 1 "+
					"O.OrderID , "+
					"AB.AccountID "+
					"FROM    Hybris.Orders AS O "+
					"JOIN    Hybris.Autoship AS A ON A.AutoshipID = O.AutoShipID "+
					"JOIN    RFO_Accounts.AccountBase AS AB ON AB.AccountID = O.AccountID "+
					"WHERE   AB.CountryID = 236 "+
					"AND A.AutoshipTypeID = 1 "+/*PC Auto-ship Template*/
					"AND O.OrderStatusID = 2 "+ /*Submitted*/
					/*Active Accounts*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS ar "+
					"WHERE  ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL "+
					"AND ar.AccountID = AB.AccountID ) "+
					"ORDER BY NEWID() ";

	public static String GET_ACCOUNT_ID_4289_RFO =

			"USE RFOperations "+

		       "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

		     "BEGIN TRANSACTION "+

		     "DECLARE @AutoshipID BIGINT "+

		     "SELECT TOP 1 "+
		     "@AutoshipID = A.AutoshipID "+
		     "FROM    Hybris.Autoship AS A "+
		     "WHERE   A.CountryID = 236 "+
		     "AND A.Active = 1 "+
		     "AND A.AutoshipTypeID = 2 "+/*Consultant Auto-ship Template*/
		     "ORDER BY NEWID() "+
		     "SELECT  * "+
		     "FROM    Hybris.Autoship AS A "+
		     "WHERE   A.AutoshipID = @AutoshipID "+

		     "SELECT  AI.* , "+
		     "PB.* "+
		     "FROM    Hybris.Autoship AS A "+
		     "JOIN Hybris.AutoshipItem AS AI ON AI.AutoshipId = A.AutoshipID "+
		     "LEFT JOIN Hybris.ProductBase AS PB ON PB.productID = AI.ProductID "+
		     "WHERE   A.AutoshipID = @AutoshipID "+

		     "SELECT  * "+
		     "FROM    Hybris.AutoshipShipment AS ASH "+
		     "JOIN Hybris.AutoshipShippingAddress AS ASA ON ASA.AutoShipID = ASH.AutoshipID "+
		     "WHERE   ASH.AutoshipID = @AutoshipID "+
		     "SELECT  * "+
		     "FROM    Hybris.AutoshipPayment AS AP "+
		     "JOIN Hybris.AutoshipPaymentAddress AS APA ON APA.AutoShipID = AP.AutoshipID "+
		     "WHERE   AP.AutoshipID = @AutoshipID ";

	public static String GET_CONSULTANT_HAVING_SUBMITTED_ORDER_4289_RFO =
			"SELECT  Username "+ 
					"FROM    RFOperations.[Security].[AccountSecurity] "+ 
					"WHERE   AccountID = '%s' ";

	public static String GET_SHIPPING_ADDRESS_QUERY_4289_RFO = "";
	public static String GET_AUTOSHIP_ORDER_DETAILS_SUBTOTAL_4289_RFO = 
			"SELECT  * "+
					"FROM    Hybris.Autoship AS A "+
					"WHERE   A.AutoshipID ='%s' ";
	public static String GET_AUTOSHIP_ORDER_DETAILS_QUERY_4289_RFO =
			"SELECT  * "+
					"FROM    Hybris.AutoshipShipment AS ASH "+
					"JOIN Hybris.AutoshipShippingAddress AS ASA ON ASA.AutoShipID = ASH.AutoshipID "+
					"WHERE   ASH.AutoshipID = '%s' ";

	public static String GET_ACTIVE_CONSULTANT_WITH_AUTOSHIP_ORDER_4291_RFO = 
			"SELECT  Username "+
					"FROM    RFOperations.[Security].[AccountSecurity] "+
					"WHERE   AccountID = '%s' ";

	public static String GET_ORDER_DETAILS_FOR_4291_RFO =
			"SELECT  * "+
					"FROM    Hybris.Orders AS O "+
					"WHERE   O.OrderID = '%s' ";
	public static String GET_SHIPPING_COST_HANDLING_COST_FOR_4291_RFO = 
			"SELECT  * "+
					"FROM    Hybris.OrderShipment AS OS "+
					"WHERE   OS.OrderID = '%s' ";

	public static String GET_ORDER_ID_ACCOUNT_ID_4293_RFO =
			"SELECT TOP 1 "+
					"O.OrderID , "+
					"AB.AccountID "+
					"FROM    Hybris.Orders AS O "+
					"JOIN    RFO_Accounts.AccountBase AS AB ON AB.AccountID = O.AccountID "+
					"WHERE   AB.CountryID = 236 "+
					"AND O.OrderTypeID = 1 "+/*Retail*/
					"AND O.OrderStatusID = 2 "+ /*Submitted*/
					/*Active Accounts*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS ar "+
					"WHERE  ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL "+
					"AND ar.AccountID = AB.AccountID ) "+
					"ORDER BY NEWID() ";

	public static String GET_ACTIVE_CONSULTANT_WITH_AUTOSHIP_ORDER_4293_RFO = 
			"SELECT  Username "+
					"FROM    RFOperations.[Security].[AccountSecurity] "+
					"WHERE   AccountID = '%s' ";

	public static String GET_ORDER_DETAILS_FOR_4293_RFO =
			"SELECT  * "+
					"FROM    Hybris.Orders AS O "+
					"WHERE   O.OrderID = '%s' "; 

	public static String GET_SHIPPING_COST_HANDLING_COST_FOR_4293_RFO = 
			"SELECT  * "+
					"FROM    Hybris.OrderShipment AS OS "+
					"WHERE   OS.OrderID = '%s' ";

	public static String GET_RANDOM_CONSULTANT_EMAIL_ID_HAVING_ACTIVE_ORDERS_RFO =

			"USE RFOperations "+

			   "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			   "BEGIN TRANSACTION "+

			   "SELECT TOP 1 "+
			   "ab.AccountID , "+
			   "AT.Name AS AccountType , "+
			   "[as].Username "+
			   "FROM    RFO_Accounts.AccountBase AS ab "+
			   "JOIN    RFO_Reference.AccountType AS AT ON AT.AccountTypeID = ab.AccountTypeID "+
			   "JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			   "JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			   "WHERE   ab.CountryID = 236 AND ab.AccountTypeID = 1 "+
			   /*Active Accounts*/
			   "AND NOT EXISTS ( SELECT 1 "+
			   "FROM   RFO_Accounts.AccountRF AS ar "+
			   "WHERE  ar.Active = 0 "+
			   "AND ar.HardTerminationDate IS NOT NULL "+
			   "AND ar.AccountID = ab.AccountID ) "+
			   /*Active Template*/
			   "AND EXISTS ( SELECT 1 "+
			   "FROM   Hybris.Autoship AS A "+
			   "WHERE  A.AccountID = ab.AccountID "+
			   "AND A.Active = 1 ) "+
			   /*Submitted orders*/
			   "AND EXISTS ( SELECT 1 "+
			   "FROM   Hybris.Orders AS O "+
			   "WHERE  O.AccountID = ab.AccountID "+
			   "AND O.OrderStatusID = 2) "+ /*Submitted*/ 
			   /*Failed orders*/
			   "AND EXISTS ( SELECT 1 "+
			   "FROM   Hybris.Orders AS O "+
			   "WHERE  O.AccountID = ab.AccountID "+
			   "AND O.OrderStatusID = 1) "+ /*Failed*/ 
			   "ORDER BY NEWID() ";

	public static String GET_RANDOM_PC_EMAIL_ID_HAVING_ACTIVE_ORDER_RFO = 
			"USE RFOperations "+

			   "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			   "BEGIN TRANSACTION "+

			   "SELECT TOP 1 "+
			   "ab.AccountID , "+
			   "AT.Name AS AccountType , "+
			   "[as].Username "+
			   "FROM    RFO_Accounts.AccountBase AS ab "+
			   "JOIN    RFO_Reference.AccountType AS AT ON AT.AccountTypeID = ab.AccountTypeID "+
			   "JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			   "JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			   "WHERE   ab.CountryID = 236 AND ab.AccountTypeID = 2 "+
			   /*Active Accounts*/
			   "AND NOT EXISTS ( SELECT 1 "+
			   "FROM   RFO_Accounts.AccountRF AS ar "+
			   "WHERE  ar.Active = 0 "+
			   "AND ar.HardTerminationDate IS NOT NULL "+
			   "AND ar.AccountID = ab.AccountID ) "+
			   /*Active Template*/
			   "AND EXISTS ( SELECT 1 "+
			   "FROM   Hybris.Autoship AS A "+
			   "WHERE  A.AccountID = ab.AccountID "+
			   "AND A.Active = 1 ) "+
			   /*Submitted orders*/
			   "AND EXISTS ( SELECT 1 "+
			   "FROM   Hybris.Orders AS O "+
			   "WHERE  O.AccountID = ab.AccountID "+
			   "AND O.OrderStatusID = 2) "+ /*Submitted*/ 
			   /*Failed orders*/
			   "AND EXISTS ( SELECT 1 "+
			   "FROM   Hybris.Orders AS O "+
			   "WHERE  O.AccountID = ab.AccountID "+
			   "AND O.OrderStatusID = 1) "+ /*Failed*/ 
			   "ORDER BY NEWID() ";

	public static String GET_ACCOUNT_ID_4300_RFO =

			"USE RFOperations "+

		       "SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+

		     "BEGIN TRANSACTION "+

		     "DECLARE @AutoshipID BIGINT "+

		     "SELECT TOP 1 "+
		     "@AutoshipID = A.AutoshipID "+
		     "FROM    Hybris.Autoship AS A "+
		     "WHERE   A.CountryID = 236 "+
		     "AND A.Active = 1 "+
		     "AND A.AutoshipTypeID = 1 "+/*PC Auto-ship Template*/
		     "ORDER BY NEWID() "+

		     "SELECT  * "+
		     "FROM    Hybris.Autoship AS A "+
		     "WHERE   A.AutoshipID = @AutoshipID "+

		     "SELECT  AI.* , "+
		     "PB.* "+
		     "FROM    Hybris.Autoship AS A "+
		     "JOIN Hybris.AutoshipItem AS AI ON AI.AutoshipId = A.AutoshipID "+
		     "LEFT JOIN Hybris.ProductBase AS PB ON PB.productID = AI.ProductID "+
		     "WHERE   A.AutoshipID = @AutoshipID "+

		     "SELECT  * "+
		     "FROM    Hybris.AutoshipShipment AS ASH "+
		     "JOIN Hybris.AutoshipShippingAddress AS ASA ON ASA.AutoShipID = ASH.AutoshipID "+
		     "WHERE   ASH.AutoshipID = @AutoshipID "+

		     "SELECT  * "+
		     "FROM    Hybris.AutoshipPayment AS AP "+
		     "JOIN Hybris.AutoshipPaymentAddress AS APA ON APA.AutoShipID = AP.AutoshipID "+
		     "WHERE   AP.AutoshipID = @AutoshipID ";

	public static String GET_PC_USER_HAVING_AUTOSHIP_ORDER_4300_RFO =
			"SELECT  Username "+ 
					"FROM    RFOperations.[Security].[AccountSecurity] "+ 
					"WHERE   AccountID = '%s' ";
	public static String GET_SHIPPING_ADDRESS_QUERY_4300_RFO = 
			"SELECT  * "+
					"FROM    Hybris.AutoshipPayment AS AP "+
					"JOIN Hybris.AutoshipPaymentAddress AS APA ON APA.AutoShipID = AP.AutoshipID "+
					"WHERE   AP.AutoshipID = '%s' ";
	public static String GET_AUTOSHIP_ORDER_DETAILS_QUERY_4300_RFO =
			"SELECT  * "+
					"FROM    Hybris.AutoshipShipment AS ASH "+
					"JOIN Hybris.AutoshipShippingAddress AS ASA ON ASA.AutoShipID = ASH.AutoshipID "+
					"WHERE   ASH.AutoshipID = '%s' ";
	public static String GET_AUTOSHIP_ORDER_DETAILS_SUBTOTAL_4300_RFO =
			"SELECT  * "+
					"FROM    Hybris.Autoship AS A "+
					"WHERE   A.AutoshipID = '%s' ";

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



	public static String GET_ORDER_NUMBER_QUERY = "select TOP 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress='%s')))order by CompletionDate desc";
	public static String GET_ORDER_STATUS_QUERY = "select Name from RFO_Reference.OrderStatus where orderStatusId IN (select Top 1 OrderStatusID from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress='%s')))order by CompletionDate desc)";
	public static String GET_ORDER_GRAND_TOTAL_QUERY = "select AmountTobeAuthorized from Hybris.OrderPayment where OrderID IN(select OrderId from Hybris.Orders where OrderNumber IN (select Top 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))) order by CompletionDate desc))";
	public static String GET_ORDER_DATE_QUERY = "select CompletionDate from Hybris.Orders where OrderNumber IN (select Top 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))) order by CompletionDate desc)";


	public static String GET_ACTIVE_CONSULTANT_HAVING_FAILED_CRP_ORDER_4294_RFO = 
			"USE RFOperations "+

				"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED "+

				"BEGIN TRANSACTION "+
				"DECLARE @Orderid BIGINT "+

				"DECLARE @Accountid BIGINT "+

				"SELECT TOP 1 "+
				"@Orderid = O.OrderID , "+
				"@Accountid = AB.AccountID "+
				"FROM    Hybris.Orders AS O "+
				"JOIN    RFO_Accounts.AccountBase AS AB ON AB.AccountID = O.AccountID "+
				"WHERE   AB.CountryID = 236 "+
				"AND O.OrderTypeID = 10 "+/*Consultant Auto-ship*/
				"AND O.OrderStatusID = 1 "+ /*Failed*/
				/*Active Accounts*/
				"AND NOT EXISTS ( SELECT 1 "+
				"FROM   RFO_Accounts.AccountRF AS ar "+
				"WHERE  ar.Active = 0 "+
				"AND ar.HardTerminationDate IS NOT NULL "+
				"AND ar.AccountID = AB.AccountID ) "+
				"ORDER BY NEWID() "+
				"SELECT  Username "+
				"FROM    RFOperations.[Security].[AccountSecurity] "+
				"WHERE   AccountID = @Accountid "+
				"SELECT  * "+
				"FROM    Hybris.Orders AS O "+
				"WHERE   O.OrderID = @Orderid "+
				"SELECT  OI.* , "+
				"PB.* "+
				"FROM    Hybris.Orders AS O "+
				"JOIN    Hybris.OrderItem AS OI ON OI.OrderId = O.OrderID "+
				"JOIN    Hybris.ProductBase AS PB ON PB.productID = OI.ProductID "+
				"WHERE   O.OrderID = @Orderid "+

								"SELECT  * "+
								"FROM    Hybris.OrderShipment AS OS "+
								"WHERE   OS.OrderID = @Orderid "+

								"SELECT  * "+
								"FROM    Hybris.OrderShipmentPackageItem AS OSPI "+
								"WHERE   OSPI.OrderID = @Orderid ";

	public static String GET_SUBTOTAL_GRANDTOTAL_TAX_DETAILS_FOR_4294_RFO =
			"SELECT  * "+
					"FROM    Hybris.Orders AS O "+
					"WHERE   O.OrderID = '%S' ";

	public static String GET_SHIPPING_HANDLING_SHIPPINGMETHODID_DETAILS_FOR_4294_RFO =	
			"SELECT  * "+
					"FROM    Hybris.OrderShipment AS OS "+
					"WHERE   OS.OrderID = '%s' ";	



	public static String GET_PC_USER_FOR_FAILED_AUTOSHIP_ORDER_RFO = 
			"USE RFOperations "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED "+

	"BEGIN TRANSACTION "+

	"DECLARE @Orderid BIGINT "+

	"DECLARE @Accountid BIGINT "+

	"SELECT TOP 1 "+
	"@Orderid = O.OrderID , "+
	"@Accountid = AB.AccountID "+
	"FROM    Hybris.Orders AS O "+
	"JOIN    RFO_Accounts.AccountBase AS AB ON AB.AccountID = O.AccountID "+
	"WHERE   AB.CountryID = 236 "+
	"AND O.OrderTypeID = 9 "+/*PC Auto-ship*/
	"AND O.OrderStatusID = 1 "+ /*Failed*/
	/*Active Accounts*/
	"AND NOT EXISTS ( SELECT 1 "+
	"FROM   RFO_Accounts.AccountRF AS ar "+
	"WHERE  ar.Active = 0 "+
	"AND ar.HardTerminationDate IS NOT NULL "+
	"AND ar.AccountID = AB.AccountID ) "+
	"ORDER BY NEWID() "+
	"SELECT  Username "+
	"FROM    RFOperations.[Security].[AccountSecurity] "+
	"WHERE   AccountID = @Accountid "+

					"SELECT  * "+
					"FROM    Hybris.Orders AS O "+
					"WHERE   O.OrderID = @Orderid "+
					"SELECT  OI.* , "+
					"PB.* "+
					"FROM    Hybris.Orders AS O "+
					"JOIN    Hybris.OrderItem AS OI ON OI.OrderId = O.OrderID "+
					"JOIN    Hybris.ProductBase AS PB ON PB.productID = OI.ProductID "+
					"WHERE   O.OrderID = @Orderid "+

					"SELECT  * "+
					"FROM    Hybris.OrderShipment AS OS "+
					"WHERE   OS.OrderID = @Orderid "+
					"SELECT  * "+
					"FROM    Hybris.OrderShipmentPackageItem AS OSPI "+
					"WHERE   OSPI.OrderID = @Orderid ";

	public static String GET_SUBTOTAL_GRANDTOTAL_TAX_DETAILS_FOR_4295_RFO =
			"SELECT  * "+
					"FROM    Hybris.Orders AS O "+
					"WHERE   O.OrderID = '%s' ";

	public static String GET_SHIPPING_HANDLING_SHIPPINGMETHODID_DETAILS_FOR_4295_RFO =	
			"SELECT  * "+
					"FROM    Hybris.OrderShipment AS OS "+
					"WHERE   OS.OrderID = '%s' ";		


	public static String GET_ACTIVE_RC_USER_HAVING_FAILED_ORDERS_RFO = 

			"USE RFOperations "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

				   "BEGIN TRANSACTION "+

				   "SELECT TOP 1 "+
				   "ab.AccountID , "+
				   "[as].Username "+
				   "FROM    RFO_Accounts.AccountBase AS ab "+
				   "JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
				   "JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
				   "WHERE   ab.CountryID = 236 "+
				   "AND ab.AccountTypeID = 3 "+
				   "AND NOT EXISTS ( SELECT 1 "+
				   "FROM   RFO_Accounts.AccountRF AS ar "+
				   "WHERE  ar.Active = 0 "+
				   "AND ar.HardTerminationDate IS NOT NULL "+
				   "AND ar.AccountID = ab.AccountID )  "+
				   /*Pending/Submitted Orders */
				   "AND EXISTS ( SELECT 1 "+
				   "FROM   Hybris.Orders AS o "+
				   "WHERE  o.AccountID = ab.AccountID "+
				   "AND o.OrderTypeID = 1 "+ /*Retail*/
				   "AND o.OrderStatusID = 1) "+ 
				   "ORDER BY NEWID()";

	public static String GET_SUBTOTAL_GRANDTOTAL_TAX_DETAILS_FOR_4296_RFO =
			"SELECT  * "+
					"FROM    Hybris.Orders AS O "+
					"WHERE   O.OrderID = '%s' ";

	public static String GET_SHIPPING_HANDLING_SHIPPINGMETHODID_DETAILS_FOR_4296_RFO =	
			"SELECT  * "+
					"FROM    Hybris.OrderShipment AS OS "+
					"WHERE   OS.OrderID = '%s' ";							

	public static String GET_ORDERID_FOR_ALL_RFO = "select top 1 * from hybris.orders where OrderNumber='%S'";

	public static String GET_ORDER_DETAILS_FOR_RFO = 
			"SELECT  * "+
					"FROM    Hybris.Orders AS O "+
					"WHERE   O.OrderID = '%s' "; 
	public static String GET_SHIPPING_COST_HANDLING_COST_FOR_RFO = 
			"SELECT  * "+
					"FROM    Hybris.OrderShipment AS OS "+
					"WHERE   OS.OrderID = '%s' ";

	public static String GET_SHIPPING_ADDRESS_RFO = "SELECT  * "+
			"FROM    Hybris.OrderBillingAddress AS OBA "+
			"WHERE   OBA.OrderID = '%s'";


	public static String GET_RANDOM_CONSULTANT_EMAIL_ID_RFO = 
			"USE RFOperations "+

			   "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			   "BEGIN TRANSACTION "+

			   /*
			   STEP #1
			       Return 1 random active consultants accounts
			    */
			    "SELECT TOP 1 "+
			    "ab.AccountID , "+
			    "[as].Username "+
			    "FROM    RFO_Accounts.AccountBase AS ab "+
			    "JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			    "JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			    "WHERE   ab.CountryID = 236 "+
			    "AND ab.AccountTypeID = 1 "+ /*Consultant*/
			    /*Active Accounts*/
			    "AND NOT EXISTS ( SELECT 1 "+
			    "FROM   RFO_Accounts.AccountRF AS ar "+
			    "WHERE  ar.Active = 0 "+
			    "AND ar.HardTerminationDate IS NOT NULL "+
			    "AND ar.AccountID = ab.AccountID ) "+
			    "ORDER BY NEWID() ";


	public static String GET_RANDOM_CONSULTANT_INACTIVE_RFO_4179= "SELECT TOP 1 ab.AccountID,[as].Username "+
			"FROM RFO_Accounts.AccountBase AS ab "+
			"JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE ab.CountryID = 236 AND ab.AccountTypeID = 1 "+ /*Consultant*/
			"AND ar.EnrollmentDate IS NOT NULL "+ /*Inactive Accounts*/
			"AND EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar "+
			"WHERE ar.Active = 0 AND ar.HardTerminationDate IS NOT NULL "+
			"AND ar.AccountID = ab.AccountID) "+ /*No Orders*/
			"AND NOT EXISTS (SELECT 1 FROM Hybris.Orders AS o "+
			"WHERE o.AccountID = ab.AccountID) "+ /*No Downlines*/
			"AND NOT EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar2 "+
			"WHERE ar2.SponsorId = ab.AccountID) "+ /*No CRP/Pulse*/
			"AND NOT EXISTS (SELECT 1 FROM Hybris.Autoship AS a "+
			"WHERE a.AccountID = ab.AccountID AND a.AutoshipTypeID IN (2,3) "+
			"AND a.Active=1) ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_INACTIVE_RFO_4181="SELECT TOP 1 ab.AccountID,[as].Username "+
			"FROM RFO_Accounts.AccountBase AS ab "+
			"JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE ab.CountryID = 236 AND ab.AccountTypeID = 1 "+ /*Consultant*/
			"AND ar.EnrollmentDate IS NOT NULL "+ /*Inactive Accounts*/
			"AND EXISTS ( SELECT 1 FROM RFO_Accounts.AccountRF AS ar "+
			"WHERE ar.Active = 0 AND ar.HardTerminationDate IS NOT NULL "+
			"AND ar.AccountID = ab.AccountID) "+ /*Failed Orders*/
			"AND EXISTS (  SELECT 1 FROM Hybris.Orders AS o "+
			"WHERE o.AccountID = ab.AccountID AND o.OrderStatusId = 1)"+ /*No Downlines*/
			"AND NOT EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar2 "+
			"WHERE ar2.SponsorId = ab.AccountID) "+ /*No CRP/Pulse*/
			"AND NOT EXISTS (SELECT 1 FROM Hybris.Autoship AS a WHERE a.AccountID = ab.AccountID "+
			"AND a.AutoshipTypeID IN (2,3) AND a.Active=1) ORDER BY NEWID()";

	public static String GET_RANDOM_INACTIVE_ACCOUNT_NO_AUTOSHIP_TEMPLATE_4182_RFO = 
			"SELECT TOP 1 ar.AccountID,[as].Username FROM RFO_Accounts.AccountRF AS ar "+
					"JOIN Security.AccountSecurity AS [as] ON [as].AccountID = ar.AccountID "+
					"WHERE ar.Active = 0 AND ar.HardTerminationDate IS NOT NULL "+
					"ORDER BY NEWID() "+
					"SELECT a.AccountID,[as].Username,a.AutoshipID,a.NextRunDate "+
					"FROM Hybris.Autoship AS a JOIN Security.AccountSecurity AS [as] ON a.AccountID = [as].AccountID "+
					"WHERE a.Active = 1 AND a.AutoshipStatusID = 2 "+ /*Submitted*//*Inactive Accounts*/
					"AND EXISTS ( SELECT 1 FROM RFO_Accounts.AccountRF AS ar "+
					"WHERE ar.Active = 0 AND ar.HardTerminationDate IS NOT NULL "+
					"AND ar.AccountID = a.AccountID) AND a.NextRunDate >= GETDATE() "+ 
					"ORDER BY a.NextRunDate ";

	public static String GET_RANDOM_CONSULTANT_INACTIVE_RFO_4184= "SELECT TOP 1 ab.AccountID,[as].Username "+
			"FROM RFO_Accounts.AccountBase AS ab "+
			"JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE ab.CountryID = 236 AND ab.AccountTypeID = 1  "+ /*Consultant*/
			"AND ar.EnrollmentDate IS NOT NULL "+ /*Inactive Accounts*/
			"AND EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar "+
			"WHERE ar.Active = 0 AND ar.HardTerminationDate IS NOT NULL "+
			"AND ar.AccountID = ab.AccountID) "+ /*No Orders*/
			"AND NOT EXISTS (SELECT 1 FROM Hybris.Orders AS o "+
			"WHERE o.AccountID = ab.AccountID) "+ /*No Downlines*/
			"AND NOT EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar2 "+
			"WHERE ar2.SponsorId = ab.AccountID) "+ /*No Pulse*/
			"AND NOT EXISTS (SELECT 1 FROM Hybris.Autoship AS a "+
			"WHERE a.AccountID = ab.AccountID AND a.AutoshipTypeID = 3 "+
			"AND a.Active=1) "+ /*CRP*/
			"AND EXISTS (SELECT 1 FROM Hybris.Autoship AS a "+
			"WHERE a.AccountID = ab.AccountID AND a.AutoshipTypeID = 2 AND a.Active=1) "+ 
			"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULATNT_NOCRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFO_4186="SELECT TOP 1 ab.AccountID,[as].Username "+
			"FROM RFO_Accounts.AccountBase AS ab JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE ab.CountryID = 236 AND ab.AccountTypeID = 1 "+ /*Consultant*/
			"AND ar.EnrollmentDate IS NOT NULL "+ /*Inactive Accounts*/
			"AND EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar "+
			"WHERE ar.Active = 0 AND ar.HardTerminationDate IS NOT NULL "+
			"AND ar.AccountID = ab.AccountID) "+ /*No Orders*/ 
			"AND NOT EXISTS (SELECT 1 FROM Hybris.Orders AS o WHERE o.AccountID = ab.AccountID) "+ /*No Downlines*/
			"AND NOT EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar2 WHERE ar2.SponsorId = ab.AccountID) "+ /*Pulse*/
			"AND EXISTS (SELECT 1 FROM Hybris.Autoship AS a WHERE a.AccountID = ab.AccountID "+
			"AND a.AutoshipTypeID = 3 AND a.Active=1) "+ /*No CRP*/
			"AND NOT EXISTS (SELECT 1 FROM Hybris.Autoship AS a WHERE a.AccountID = ab.AccountID "+
			"AND a.AutoshipTypeID = 2 AND a.Active=1) ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFO_4188= "SELECT TOP 1 ab.AccountID ,[as].Username "+
			"FROM RFO_Accounts.AccountBase AS ab JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE ab.CountryID = 236 AND ab.AccountTypeID = 1 "+ /*Consultant*/ 
			"AND ar.EnrollmentDate IS NOT NULL "+ /*Inactive Accounts*/
			"AND EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar "+
			"WHERE ar.Active = 0 AND ar.HardTerminationDate IS NOT NULL "+
			"AND ar.AccountID = ab.AccountID) "+ /*No Orders*/
			"AND NOT EXISTS (SELECT 1 FROM Hybris.Orders AS o WHERE o.AccountID = ab.AccountID) "+ /*No Downlines*/
			"AND NOT EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar2 WHERE ar2.SponsorId = ab.AccountID) "+ /*Pulse*/
			"AND EXISTS (SELECT 1 FROM Hybris.Autoship AS a WHERE a.AccountID = ab.AccountID "+
			"AND a.AutoshipTypeID = 3 AND a.Active=1) "+ /*CRP*/
			"AND EXISTS (SELECT 1 FROM Hybris.Autoship AS a "+
			"WHERE a.AccountID = ab.AccountID AND a.AutoshipTypeID = 2 AND a.Active=1) ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_INACTIVE_RFO_4189= "SELECT TOP 1 ab.AccountID,[as].Username FROM RFO_Accounts.AccountBase AS ab "+
			"JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE ab.CountryID = 236 AND ab.AccountTypeID = 1 "+ /*Consultant*/
			" AND ar.EnrollmentDate IS NOT NULL "+ /*Active Accounts*/
			" AND NOT EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar "+
			" WHERE ar.Active = 0 AND ar.HardTerminationDate IS NOT NULL "+
			" AND ar.AccountID = ab.AccountID) "+ /*Failed Orders*/
			" AND EXISTS (SELECT 1 FROM Hybris.Orders AS o "+
			"WHERE o.AccountID = ab.AccountID AND o.OrderStatusId = 1) "+ /*No Downlines*/
			"AND NOT EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar2 "+
			"WHERE ar2.SponsorId = ab.AccountID) "+ /*Pulse*/
			"AND EXISTS (SELECT 1 FROM Hybris.Autoship AS a WHERE a.AccountID = ab.AccountID "+
			"AND a.AutoshipTypeID = 3 AND a.Active=1) "+ /*CRP*/
			"AND EXISTS (SELECT 1 FROM Hybris.Autoship AS a WHERE a.AccountID = ab.AccountID "+
			"AND a.AutoshipTypeID = 2 AND a.Active=1) ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_INACTIVE_RFO_4191 = "SELECT TOP 1 ab.AccountID ,[as].Username "+
			"FROM RFO_Accounts.AccountBase AS ab JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE ab.CountryID = 236 AND ab.AccountTypeID = 1 "+ /*Consultant*/
			"AND ar.EnrollmentDate IS NOT NULL "+ /*Active Accounts*/
			"AND NOT EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar "+
			"WHERE ar.Active = 0 AND ar.HardTerminationDate IS NOT NULL "+
			"AND ar.AccountID = ab.AccountID) "+ /*Submitted Orders*/
			"AND EXISTS (SELECT 1 FROM Hybris.Orders AS o "+
			"WHERE o.AccountID = ab.AccountID AND o.OrderStatusId = 2) "+ /*No Downlines*/
			"AND NOT EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar2 WHERE ar2.SponsorId = ab.AccountID) "+ /*Pulse*/
			"AND EXISTS (SELECT 1 FROM Hybris.Autoship AS a WHERE a.AccountID = ab.AccountID AND a.AutoshipTypeID = 3 "+
			" AND a.Active=1) "+ /*CRP*/
			"AND EXISTS (SELECT 1 FROM Hybris.Autoship AS a WHERE a.AccountID = ab.AccountID AND a.AutoshipTypeID = 2 "+
			"AND a.Active=1) ORDER BY NEWID() ";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFO_4190= "SELECT TOP 1 ab.AccountID ,[as].Username "+
			" FROM RFO_Accounts.AccountBase AS ab JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			" JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			" WHERE ab.CountryID = 236 AND ab.AccountTypeID = 1 "+ /*Consultant*/
			" AND ar.EnrollmentDate IS NOT NULL "+ /*Inactive Accounts*/
			" AND EXISTS (SELECT 1 FROM RFO_Accounts.AccountRF AS ar WHERE ar.Active = 0 "+
			" AND ar.HardTerminationDate IS NOT NULL AND ar.AccountID = ab.AccountID) "+ /*Failed Orders*/
			" AND EXISTS ( SELECT 1 FROM Hybris.Orders AS o WHERE o.AccountID = ab.AccountID "+
			" AND o.OrderStatusId = 1) /*No Downlines*/ AND NOT EXISTS ( "+
			" SELECT 1 FROM RFO_Accounts.AccountRF AS ar2 WHERE ar2.SponsorId = ab.AccountID) "+ /*Pulse*/
			" AND EXISTS (SELECT 1 FROM Hybris.Autoship AS a WHERE a.AccountID = ab.AccountID AND a.AutoshipTypeID = 3 AND a.Active=1) "+  /*CRP*/
			" AND EXISTS (SELECT 1 FROM Hybris.Autoship AS a WHERE a.AccountID = ab.AccountID AND a.AutoshipTypeID = 2 AND a.Active=1) ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_SUBMITTED_ORDERS_INACTIVE_RFO_4192 =
			"USE RFOperations "+

			   "SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+

			   "BEGIN TRANSACTION "+
			   "SELECT TOP 1 ab.AccountID "+
			   ",[as].Username "+
			   "FROM RFO_Accounts.AccountBase AS ab "+
			   "JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			   "JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			   "WHERE ab.CountryID = 236 "+
			   "AND ab.AccountTypeID = 1 "+ /*Consultant*/
			   "AND ar.EnrollmentDate IS NOT NULL "+
			   /*Inactive Accounts*/
			   "AND EXISTS ( "+
			   "SELECT 1 "+
			   "FROM RFO_Accounts.AccountRF AS ar "+
			   "WHERE ar.Active = 0 "+
			   "AND ar.HardTerminationDate IS NOT NULL "+
			   "AND ar.AccountID = ab.AccountID) "+ 
			   /*Submitted Orders*/
			   "AND EXISTS ( "+
			   "SELECT 1 "+
			   "FROM Hybris.Orders AS o "+
			   "WHERE o.AccountID = ab.AccountID "+
			   "AND o.OrderStatusId = 2) "+ 
			   /*No Downlines*/
			   "AND NOT EXISTS ( "+
			   "SELECT 1 "+
			   "FROM RFO_Accounts.AccountRF AS ar2 "+
			   "WHERE ar2.SponsorId = ab.AccountID) "+ 
			   /*Pulse*/
			   "AND EXISTS ( "+
			   "SELECT 1 "+
			   "FROM Hybris.Autoship AS a "+
			   "WHERE a.AccountID = ab.AccountID "+
			   "AND a.AutoshipTypeID = 3 "+
			   "AND a.Active=1) "+ 
			   /*CRP*/
			   "AND EXISTS ( "+
			   "SELECT 1 "+
			   "FROM Hybris.Autoship AS a "+
			   "WHERE a.AccountID = ab.AccountID "+
			   "AND a.AutoshipTypeID = 2 "+
			   "AND a.Active=1) "+ 
			   "ORDER BY NEWID() ";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_ACTIVE_RFO_4193 = 

			"USE RFOperations "+

			   "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			 "BEGIN TRANSACTION "+
			 "SELECT TOP 1 ab.AccountID "+
			 ",[as].Username "+
			 "FROM RFO_Accounts.AccountBase AS ab "+
			 "JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			 "JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			 "WHERE ab.CountryID = 236 "+
			 "AND ab.AccountTypeID = 1 "+ /*Consultant*/
			 "AND ar.EnrollmentDate IS NOT NULL "+
			 /*Active Accounts*/
			 "AND NOT EXISTS ( "+
			 "SELECT 1 "+
			 "FROM RFO_Accounts.AccountRF AS ar "+
			 "WHERE ar.Active = 0 "+
			 "AND ar.HardTerminationDate IS NOT NULL "+
			 "AND ar.AccountID = ab.AccountID) "+ 
			 /*Failed Orders*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Orders AS o "+
			 "WHERE o.AccountID = ab.AccountID "+
			 "AND o.OrderStatusId = 1) "+ 
			 /*Has Downlines*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM RFO_Accounts.AccountRF AS ar2 "+
			 "WHERE ar2.SponsorId = ab.AccountID) "+ 
			 /*Pulse*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Autoship AS a "+
			 "WHERE a.AccountID = ab.AccountID "+
			 "AND a.AutoshipTypeID = 3 "+
			 "AND a.Active=1) "+ 
			 /*CRP*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Autoship AS a "+
			 "WHERE a.AccountID = ab.AccountID "+
			 "AND a.AutoshipTypeID = 2 "+
			 "AND a.Active=1) "+ 
			 "ORDER BY NEWID() ";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_INACTIVE_RFO_4194 =

			"USE RFOperations "+

			   "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			 "BEGIN TRANSACTION "+

			 "SELECT TOP 1 ab.AccountID "+
			 ",[as].Username "+
			 "FROM RFO_Accounts.AccountBase AS ab "+
			 "JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			 "JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			 "WHERE ab.CountryID = 236 "+
			 "AND ab.AccountTypeID = 1 "+ /*Consultant*/
			 "AND ar.EnrollmentDate IS NOT NULL "+
			 /*Inactive Accounts*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM RFO_Accounts.AccountRF AS ar "+
			 "WHERE ar.Active = 0 "+
			 "AND ar.HardTerminationDate IS NOT NULL "+
			 "AND ar.AccountID = ab.AccountID) "+ 
			 /*Failed Orders*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Orders AS o "+
			 "WHERE o.AccountID = ab.AccountID "+
			 "AND o.OrderStatusId = 1) "+ 
			 /*Has Downlines*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM RFO_Accounts.AccountRF AS ar2 "+
			 "WHERE ar2.SponsorId = ab.AccountID) "+ 
			 /*Pulse*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Autoship AS a "+
			 "WHERE a.AccountID = ab.AccountID "+
			 "AND a.AutoshipTypeID = 3 "+
			 "AND a.Active=1) "+ 
			 /*CRP*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Autoship AS a "+
			 "WHERE a.AccountID = ab.AccountID "+
			 "AND a.AutoshipTypeID = 2 "+
			 "AND a.Active=1) "+ 
			 "ORDER BY NEWID() ";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_HAS_SUBMITTED_ORDERS_INACTIVE_RFO_4196 =

			"USE RFOperations "+

			   "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			 "BEGIN TRANSACTION "+

			 "SELECT TOP 1 ab.AccountID "+
			 ",[as].Username "+
			 "FROM RFO_Accounts.AccountBase AS ab "+
			 "JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			 "JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			 "WHERE ab.CountryID = 236 "+
			 "AND ab.AccountTypeID = 1 "+ /*Consultant*/
			 "AND ar.EnrollmentDate IS NOT NULL "+
			 /*Inactive Accounts*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM RFO_Accounts.AccountRF AS ar "+
			 "WHERE ar.Active = 0 "+
			 "AND ar.HardTerminationDate IS NOT NULL "+
			 "AND ar.AccountID = ab.AccountID) "+ 
			 /*Submitted Orders*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Orders AS o "+
			 "WHERE o.AccountID = ab.AccountID "+
			 "AND o.OrderStatusId = 2) "+ 
			 /*Has Downlines*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM RFO_Accounts.AccountRF AS ar2 "+
			 "WHERE ar2.SponsorId = ab.AccountID) "+ 
			 /*Pulse*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Autoship AS a "+
			 "WHERE a.AccountID = ab.AccountID "+
			 "AND a.AutoshipTypeID = 3 "+
			 "AND a.Active=1) "+ 
			 /*CRP*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Autoship AS a "+
			 "WHERE a.AccountID = ab.AccountID "+
			 "AND a.AutoshipTypeID = 2 "+
			 "AND a.Active=1) "+ 
			 "ORDER BY NEWID() ";

	public static String GET_ACCOUNTS_WITH_NULL_EMAIL_ADDRESS_4227_RFO =

			"USE RFOperations "+

			   "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			 "BEGIN TRANSACTION "+

			 "SELECT TOP 1 "+
			 "ab.AccountID , "+
			 "[as].Username "+
			 "FROM    RFO_Accounts.AccountBase AS ab "+
			 "JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			 "JOIN RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID "+
			 "JOIN RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId "+
			 "JOIN RFO_Accounts.EmailAddresses ea ON ea.EmailAddressID = ae.EmailAddressId "+
			 "WHERE   ab.CountryID = 236 "+
			 "AND ( ea.EmailAddress IS NULL "+
			 "OR NOT EXISTS ( SELECT    1 "+
			 "FROM      RFO_Accounts.AccountContacts ac_s "+
			 "JOIN RFO_Accounts.AccountEmails ae_s ON ae_s.AccountContactId = ac_s.AccountContactId "+
			 "JOIN RFO_Accounts.EmailAddresses ea_s ON ea_s.EmailAddressID = ae_s.EmailAddressId "+
			 "WHERE     ac_s.AccountId = ab.AccountID ) "+
			 ") "+
			 "ORDER BY NEWID() ";

	public static String GET_RANDOM_USER_MULTIPLE_PAYMENTS_RFO_4223 = 
			"USE RFOperations "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+

			 "SELECT TOP 1 "+
			 "ab.AccountID , "+
			 "[as].Username "+
			 "FROM    RFO_Accounts.AccountBase AS ab "+
			 "JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			 "WHERE   ab.CountryID = 236 "+  
			 /*Multiple payment profiles*/
			 "AND EXISTS ( SELECT 1 "+
			 "FROM   RFO_Accounts.PaymentProfiles pp "+
			 "WHERE  pp.AccountID = ab.AccountID "+
			 "GROUP BY pp.AccountID "+
			 "HAVING COUNT(*) > 1 ) "+
			 "ORDER BY NEWID() ";

	public static String GET_RANDOM_CONSULTANT_HAS_CRP_HAS_ORDERS_RFO_4195 = 

			"USE RFOperations "+

			   "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			 "BEGIN TRANSACTION "+
			 "SELECT TOP 1 ab.AccountID "+
			 ",[as].Username "+
			 "FROM RFO_Accounts.AccountBase AS ab "+
			 "JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			 "JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			 "WHERE ab.CountryID = 236 "+
			 "AND ab.AccountTypeID = 1 "+ /*Consultant*/
			 "AND ar.EnrollmentDate IS NOT NULL "+
			 /*Active Accounts*/
			 "AND NOT EXISTS ( "+
			 "SELECT 1 "+
			 "FROM RFO_Accounts.AccountRF AS ar "+
			 "WHERE ar.Active = 0 "+
			 "AND ar.HardTerminationDate IS NOT NULL "+
			 "AND ar.AccountID = ab.AccountID) "+ 
			 /*Submitted Orders*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Orders AS o "+
			 "WHERE o.AccountID = ab.AccountID "+
			 "AND o.OrderStatusId = 2) "+ 
			 /*Has Downlines*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM RFO_Accounts.AccountRF AS ar2 "+
			 "WHERE ar2.SponsorId = ab.AccountID) "+ 
			 /*Pulse*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Autoship AS a "+
			 "WHERE a.AccountID = ab.AccountID "+
			 "AND a.AutoshipTypeID = 3 "+
			 "AND a.Active=1) "+ 
			 /*CRP*/
			 "AND EXISTS ( "+
			 "SELECT 1 "+
			 "FROM Hybris.Autoship AS a "+
			 "WHERE a.AccountID = ab.AccountID "+
			 "AND a.AutoshipTypeID = 2 "+
			 "AND a.Active=1) "+ 
			 "ORDER BY NEWID() ";

	public static String GET_RANDOM_PC_HAS_CRP_PULSE_SUBMITTED_ORDERS_RFO_4205 = 

			"USE RFOperations "+

			   "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			 "BEGIN TRANSACTION "+

			 "SELECT TOP 1 "+
			 "ab.AccountID , "+
			 "[as].Username "+
			 "FROM    RFO_Accounts.AccountBase AS ab "+
			 "JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			 "JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			 "WHERE   ab.CountryID = 236 "+
			 "AND ab.AccountTypeID = 2 "+ /*Preferred Customer*/
			 "AND ar.EnrollmentDate IS NOT NULL "+
			 /*Submitted Orders */
			 "AND EXISTS ( SELECT 1 "+
			 "FROM   Hybris.Orders AS o "+
			 "WHERE  o.AccountID = ab.AccountID "+
			 "AND o.OrderStatusID = 2 ) "+
			 /*Has Pulse */
			 "AND EXISTS ( SELECT 1 "+
			 "FROM   Hybris.Autoship AS a "+
			 "WHERE  a.AccountID = ab.AccountID "+
			 "AND a.AutoshipTypeID = 3 "+
			 "AND a.Active = 1 ) "+
			 /*Has CRP*/
			 "AND EXISTS ( SELECT 1 "+
			 "FROM   Hybris.Autoship AS a "+
			 "WHERE  a.AccountID = ab.AccountID "+
			 "AND a.AutoshipTypeID = 1 "+
			 "AND a.Active = 1 ) "+
			 "ORDER BY NEWID() ";

	public static String GET_RANDOM_ENROLLED_RC_USER_HAS_FAILED_ORDER_RFO_4200 =
			"USE RFOperations; "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+

			 "BEGIN TRANSACTION; "+
			 "SELECT TOP 1 "+
			 "ab.AccountID , "+
			 "[as].Username "+
			 "FROM    RFO_Accounts.AccountBase AS ab "+
			 "JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			 "JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			 "WHERE   ab.CountryID = 236 "+
			 "AND ab.AccountTypeID = 3 "+ /*Retail Customer*/
			 "AND ar.EnrollmentDate IS NOT NULL "+
			 /*Failed Orders */
			 "AND EXISTS ( SELECT 1 "+
			 "FROM   Hybris.Orders AS o "+
			 "WHERE  o.AccountID = ab.AccountID "+
			 "AND o.OrderStatusID = 1 ) "+
			 "ORDER BY NEWID() ";

	public static String GET_RANDOM_PC_USER_EMAIL_ID_RFO = 
			"USE RFOperations "+

			   "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			   "BEGIN TRANSACTION "+

			   "SELECT TOP 1 "+
			   "ab.AccountID , "+
			   "[as].Username "+
			   "FROM    RFO_Accounts.AccountBase AS ab "+
			   "JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			   "JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			   "WHERE   ab.CountryID = 236 "+
			   "AND ab.AccountTypeID = 2 "+ /*Preferred Customer*/
			   /*Active Accounts*/
			   "AND NOT EXISTS ( SELECT 1 "+
			   "FROM   RFO_Accounts.AccountRF AS ar "+
			   "WHERE  ar.Active = 0 "+
			   "AND ar.HardTerminationDate IS NOT NULL "+
			   "AND ar.AccountID = ab.AccountID ) "+
			   "ORDER BY NEWID() ";

	public static String GET_RANDOM_RC_EMAIL_ID_RFO = 
			"USE RFOperations "+

			   "SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			   "BEGIN TRANSACTION "+

			   "SELECT TOP 1 "+
			   "ab.AccountID , "+
			   "[as].Username "+
			   "FROM    RFO_Accounts.AccountBase AS ab "+
			   "JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			   "JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			   "WHERE   ab.CountryID = 236 "+
			   "AND ab.AccountTypeID = 3 "+ /*Retail Customer*/
			   /*Active Accounts*/
			   "AND NOT EXISTS ( SELECT 1 "+
			   "FROM   RFO_Accounts.AccountRF AS ar "+
			   "WHERE  ar.Active = 0 "+
			   "AND ar.HardTerminationDate IS NOT NULL "+
			   "AND ar.AccountID = ab.AccountID ) "+
			   "ORDER BY NEWID()";

	public static String GET_RANDOM_RC_EMAIL_ID_HAVING_ACTIVE_ORDER_RFO = 
			"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"WHERE   ab.CountryID = 236 "+
					"AND ab.AccountTypeID = 3  "+
					/*Active Accounts*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS ar "+
					"WHERE  ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL "+
					"AND ar.AccountID = ab.AccountID ) "+
					/*Pending/Submitted Orders */
					"AND EXISTS ( SELECT 1 "+
					"FROM   Hybris.Orders AS o "+
					"WHERE  o.AccountID = ab.AccountID "+
					"AND o.OrderTypeID = 1 "+ 
					"AND o.OrderStatusID = 1 /*Failed*/) "+ 
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_NO_PWS_RFO =
			""; // Waiting for query

	public static String GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO = "select * from Hybris.OrderShippingAddress where OrderID='%s'";

	public static String GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO =
			/*********************************************************************************************
			Query on RFO having active(i.e statusId =’1’ ) consultant only with active consultant-autoship template with pending autoship and pending/submitted adhoc orders.
			 **********************************************************************************************/
			"USE RFOperations "+
			"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
			"BEGIN TRANSACTION "+
			"SELECT TOP 1 "+
			"ab.AccountID , "+
			"[as].Username "+
			"FROM    RFO_Accounts.AccountBase AS ab "+
			"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE   ab.CountryID = 236 "+
			"AND ab.AccountTypeID = 1 "+/*Consultant*/
			/*Active Accounts*/
			"AND NOT EXISTS ( SELECT 1 "+
			"FROM   RFO_Accounts.AccountRF AS ar "+
			"WHERE  ar.Active = 0 "+
			"AND ar.HardTerminationDate IS NOT NULL "+
			"AND ar.AccountID = ab.AccountID ) "+ 
			/*Pending/Submitted Orders */
			"AND EXISTS ( SELECT 1 "+
			"FROM   Hybris.Orders AS o "+
			"WHERE  o.AccountID = ab.AccountID "+
			"AND o.OrderTypeID = 3 "+/*Consultant*/
			"AND o.OrderStatusID = 2 ) "+ 
			/*Active Template*/
			"AND EXISTS ( SELECT 1 "+
			"FROM   Hybris.Autoship AS a "+
			"WHERE  a.AccountID = ab.AccountID "+
			"AND a.AutoshipTypeID = 2 "+/*Consultant Auto-ship Template*/
			"AND a.Active = 1 ) "+
			"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_WITH_PWS_RFO = 
			"USE RFOperations "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+
					"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username , "+
					"'http://' + S.SitePrefix + '.' + REPLACE(SD.Name,'myrandf','myrfotst4') + '/us' AS URL "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Reference.AccountType AS AT ON AT.AccountTypeID = ab.AccountTypeID "+
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"JOIN    RFO_Accounts.ConsultantPWSInfo AS CPI ON CPI.AccountId = ab.AccountID "+
					"JOIN    Hybris.Sites AS S ON S.AccountID = ab.AccountID "+
					"JOIN    Hybris.SiteURLs AS SUL ON SUL.SiteID = S.SiteID "+
					"JOIN    Hybris.SiteDomain AS SD ON SD.SiteDomainID = SUL.SiteDomainID "+
					"WHERE   ab.AccountTypeID = 1 "+/*Consultant*/
					/*Active Accounts*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS ar "+
					"WHERE  ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL "+
					"AND ar.AccountID = ab.AccountID ) "+
					/*Pulse*/
					"AND EXISTS ( SELECT 1 "+
					"FROM   Hybris.Autoship AS a "+
					"WHERE  a.AccountID = ab.AccountID "+
					"AND a.AutoshipTypeID = 3 "+
					"AND a.Active = 1 ) "+
					"ORDER BY NEWID()";

	public static String GET_INACTIVE_CONSULTANT_LESS_THAN_6_MONTH_RFO = 
			"USE RFOperations "+

			"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			"BEGIN TRANSACTION "+
			"SELECT TOP 1 "+
			"ab.AccountID , "+
			"[as].Username "+
			"FROM    RFO_Accounts.AccountBase AS ab "+
			"JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE   ab.CountryID = 236 "+
			"AND ab.AccountTypeID = 1 /*Consultant*/ "+
			"AND ar.HardTerminationDate > DATEADD(MONTH, -6,CONVERT(DATE, GETDATE())) "+
			"ORDER BY NEWID()";

	public static String GET_INACTIVE_CONSULTANT_MORE_THAN_6_MONTH_RFO =
			"USE RFOperations "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			"BEGIN TRANSACTION "+
			"SELECT TOP 1 "+
			"ab.AccountID , "+
			"[as].Username "+
			"FROM    RFO_Accounts.AccountBase AS ab "+
			"JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE   ab.CountryID = 236 "+
			"AND ab.AccountTypeID = 1 /*Consultant*/ "+
			"AND ar.HardTerminationDate < DATEADD(MONTH, -6,CONVERT(DATE, GETDATE())) "+
			"ORDER BY NEWID()";

	public static String GET_INACTIVE_PC_LESS_THAN_90_DAYS_RFO ="";
	// waiting for query

	public static String GET_INACTIVE_PC_MORE_THAN_90_DAYS_RFO ="USE RFOperations "+
			"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+

			"BEGIN TRANSACTION "+
			"SELECT TOP 1 "+
			"ab.AccountID , "+
			"[as].Username "+
			"FROM    RFO_Accounts.AccountBase AS ab "+
			"JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE   ab.CountryID = 236 "+
			"AND ab.AccountTypeID = 2 /*Preferred Customer*/ "+
			"AND ar.HardTerminationDate < DATEADD(DAY, -90,CONVERT(DATE, GETDATE())) "+
			"ORDER BY NEWID()";

	public static String GET_ACCOUNT_CONTACT_ID_RFO = "select top 1 * from RFO_Accounts.AccountContacts where AccountId = '%s'";

	public static String GET_EMAIL_ADDRESS_ID_RFO = "select top 1 * from RFO_Accounts.AccountEmails where AccountContactId = '%s'";

	public static String GET_EMAIL_ID_RFO = "select top 1 * from RFO_Accounts.EmailAddresses WHERE EmailAddressID = '%S'";

	public static String GET_RANDOM_100_USERS_RFO = "select top 100 * from RFO_Accounts.EmailAddresses order by NEWID()";

	public static String GET_RANDOM_ACTIVE_SITE_PREFIX_RFO = "select top 1 SitePrefix from Hybris.Sites where accountID IN (select top 1 accountID from RFO_Accounts.AccountBase where countryID=%s and AccountStatusID=1 and AccountTypeID=1 order by newId())";
	
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



