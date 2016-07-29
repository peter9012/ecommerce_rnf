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
	public static String GET_FIRST_NAME_FROM_ACCOUNT_ID="select top 1 * from RFO_Accounts.AccountContacts where AccountId ='%s'";
	public static String GET_PC_PERKS_AUTOSHIP_ITEM_DETAILS_QUERY = "select * from Hybris.productbase where productID IN (select productId from Hybris.AutoshipItem where AutoshipId IN ( select autoshipid from Hybris.Autoship where AccountID IN (select accountid from RFO_Accounts.AccountContacts where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))))";
	public static String GET_ACCOUNT_NAME_DETAILS_QUERY = "select top 1 * from RFO_Accounts.AccountContacts where LegalName='%s'";
	public static String GET_ACCOUNT_ADDRESS_DETAILS_QUERY = "select top 3 * from RFO_Accounts.Addresses where addressTypeID ='1' and addressId IN (select top 3 AddressID from RFO_Accounts.AccountContactAddresses where accountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))";
	public static String GET_ACCOUNT_PHONE_NUMBER_QUERY_RFO = "select top 1 PhoneNumberRaw from RFO_Accounts.Phones where PhoneTypeID='1' and PhoneID IN (select PhoneId from RFO_Accounts.AccountContactPhones where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))";
	public static String GET_ACCOUNT_MOBILE_NUMBER_QUERY_RFO = "select top 1 * from RFO_Accounts.Phones where PhoneTypeID='1' and PhoneID IN (select PhoneId from RFO_Accounts.AccountContactPhones where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))";
	public static String GET_RETURN_ORDER_DETAILS_QUERY = "select Top 1 ReturnOrderNumber,total from Hybris.ReturnOrder where AccountID IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s')))";
	public static String GET_RETURN_ORDER_STATUS_QUERY = "select Name from RFO_Reference.ReturnStatus where ReturnStatusId IN (select Top 1 ReturnStatusId from Hybris.ReturnOrder where AccountID IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))))";
	public static String GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO = "select Top 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))) order by CompletionDate desc";
	public static String GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFO = "select CompletionDate from Hybris.Orders where OrderNumber IN (select Top 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))) order by CompletionDate desc)";
	public static String GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFO = "select AmountTobeAuthorized from Hybris.OrderPayment where OrderID IN(select OrderId from Hybris.Orders where OrderNumber IN (select Top 1 OrderNumber from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress= '%s'))) order by CompletionDate desc))";
	public static String GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFO = "select Name from RFO_Reference.OrderStatus where orderStatusId IN (select Top 1 OrderStatusID from Hybris.Orders where accountId IN (select Top 1 AccountId from RFO_Accounts.AccountContacts where AccountContactId IN (select Top 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressId IN (select Top 1 EmailAddressId from RFO_Accounts.EmailAddresses where EmailAddress='%s')))order by CompletionDate desc)";
	public static String GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO = "select top 1 * from RFO_Accounts.Addresses where addressId IN (select top 3 AddressID from RFO_Accounts.AccountContactAddresses where accountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s')))";
	public static String GET_ACCOUNT_ADDRESS_RFO = "select top 1 * from RFO_Accounts.Addresses where AddressProfileName='%s'";
	public static String GET_PULSE_STATUS_FROM_ACCOUNT_ID = "select * from Hybris.Autoship as AuS where AuS.AutoshipTypeID = 3 and AuS.active=1 and AuS.AccountID like '%s'";
	public static String GET_RANDOM_INACTIVE_PC_EMAILID_MORE_THAN_90_DAYS="SELECT TOP 1 "+
			"rf.AccountID , "+
			" rf.HardTerminationDate , "+
			"rf.Active , "+
			" ab.AccountTypeID,ead.* "+
			" FROM    RFOperations.RFO_Accounts.AccountRF rf "+
			"JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID "+
			"JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID "+
			" JOIN RFOperations.RFO_Accounts.AccountContactAddresses ad ON ad.AccountContactId = ac.AccountContactId "+   
			"JOIN RFOperations.RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId "+ 
			"JOIN RFOperations.RFO_Accounts.EmailAddresses ead ON ead.EmailAddressID=ae.EmailAddressId "+
			"WHERE   ab.CountryID = %s "+
			" AND ab.AccountTypeID = 2 "+
			"AND rf.Active = 0 "+
			" AND rf.HardTerminationDate < DATEADD(DAY, -90, GETDATE()) "+
			" AND ab.AccountStatusID IN ( 2 ,10) "+
			" ORDER BY 1 DESC ";
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
			Query on RFO having active (i.e statusId =�1� ) pc only with active pc-autoship template with pending autoship and pending/submitted adhoc orders.
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
			"WHERE   ab.CountryID = %s "+
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

	public static String GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO =
			"USE RFOperations "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+ 
					"BEGIN TRANSACTION "+ 
					"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username "+ 
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+ 
					"WHERE   ab.CountryID = %s "+
					"AND ab.AccountTypeID = 2 "+/*Preferred Customer*/
					/*Active Accounts*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS ar "+ 
					"WHERE  ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL "+ 
					"AND ar.AccountID = ab.AccountID ) "+  
					"AND EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.EmailAddresses AS ea "+
					"WHERE  ea.EmailAddress = [as].Username) "+  
					/*Active Template*/
					"AND EXISTS ( SELECT 1 "+
					"FROM   Hybris.Autoship AS a "+
					"WHERE  a.AccountID = ab.AccountID "+ 
					"AND a.AutoshipTypeID = 1 "+/*PC Auto-ship Template*/
					"AND a.Active = 1 ) "+ 
					"ORDER BY NEWID();";

	public static String GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO =
			"USE RFOperations "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
					"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"WHERE   ab.CountryID = %s "+
					"AND ab.AccountTypeID = 3 /*Retail Customer*/ "+
					"AND ar.Active = 1 "+

			       /* Orders Check*/
			       "AND EXISTS ( SELECT 1 "+
			       "FROM   Hybris.Orders AS o, Hybris.dbo.orders o1 "+
			       "WHERE  o.AccountID = ab.AccountID "+
			       "AND o.orderid=o1.pk "+   
			       "AND o.OrderTypeID = 1) "+
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
			"WHERE   ab.CountryID = %s "+
			"AND ab.AccountTypeID = 3 "+/*Retail Customer*/
			"AND ab.AccountStatusID = 1" +
			/*Active Accounts*/
			"AND EXISTS ( SELECT 1 "+
			"FROM   RFO_Accounts.AccountRF AS ar "+
			"WHERE  ar.Active = 1 "+
			"AND ar.HardTerminationDate IS NULL "+
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
					"WHERE   ab.CountryID = %s "+ 
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
			"[as].Username "+ 
			"FROM    RFO_Accounts.AccountBase AS ab "+
			"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE   ab.AccountTypeID = 1 "+/*Consultant*/
			"AND     ab.CountryID = %s "+
			/*Active Accounts*/
			"AND NOT EXISTS ( SELECT 1 "+
			"FROM   RFO_Accounts.AccountRF AS ar "+
			"WHERE  ar.Active = 0 "+
			"AND ar.HardTerminationDate IS NOT NULL "+
			"AND ar.AccountID = ab.AccountID ) "+
			/*Pulse*/
			"AND NOT EXISTS ( SELECT 1 "+
			"FROM   Hybris.Autoship AS a "+
			"WHERE  a.AccountID = ab.AccountID "+
			"AND a.AutoshipTypeID = 3 "+
			"AND a.Active = 1 ) "+
			"AND NOT EXISTS (SELECT 1 "+
			"FROM  RFO_Accounts.ConsultantPWSInfo AS CPI "+ 
			"WHERE CPI.AccountId = ab.AccountID) "+
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
					"WHERE   ab.CountryID = %s "+
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
					"AND AB.CountryID = %s "+
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
					"AND AB.CountryID = %s "+
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
					"WHERE   AB.CountryID = %s "+
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

	public static String GET_ORDERID_RFO = "select * from Hybris.orders where OrderNumber = '%s'";
	public static String GET_ORDER_STATUS = "select Name from RFO_Reference.OrderStatus where orderStatusId='%s'";

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
					"WHERE   AB.CountryID = %s "+
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
		     "WHERE   A.CountryID = %s "+
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
					"ab.AccountID , "+
					"[as].Username "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"WHERE   ab.CountryID = %s "+
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
					"AND o.OrderTypeID = 1 "+/*RC*/ 
					"AND o.OrderStatusID IN (1,2) ) "+ 
					"ORDER BY NEWID(); ";

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
			   "WHERE   ab.CountryID = %s AND ab.AccountTypeID = 1 "+
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

	//	public static String GET_RANDOM_CONSULTANT_WITH_PENDING_STATUS_RFO =
	//			"USE RFOperations "+
	//					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
	//					"BEGIN TRANSACTION "+
	//					"SELECT TOP 1 "+
	//					"ab.AccountID , "+
	//					"AT.Name AS AccountType , "+
	//					"[as].Username "+
	//					"FROM    RFO_Accounts.AccountBase AS ab "+
	//					"JOIN    RFO_Reference.AccountType AS AT ON AT.AccountTypeID = ab.AccountTypeID "+
	//					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
	//					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
	//					"WHERE   ab.CountryID = %s AND ab.AccountTypeID = 1 "+
	//					"AND AccountStatusID = 4 "+
	//					/*Active Accounts*/
	//					"AND EXISTS ( SELECT 1 "+
	//					"FROM   RFO_Accounts.AccountRF AS ar "+
	//					"WHERE  ar.Active = 1 "+
	//					"AND ar.HardTerminationDate IS NOT NULL "+
	//					"AND ar.AccountID = ab.AccountID ) "+
	//					"ORDER BY NEWID() ";


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
			   "WHERE   ab.CountryID = %s AND ab.AccountTypeID = 2 "+
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
		     "WHERE   A.CountryID = %s "+
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
				"WHERE   AB.CountryID = %s "+
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

	public static String GET_ACTIVE_CONSULTANT_HAVING_FAILED_CRP_ORDER_RFO = "USE RFOperations "+
			"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
			"BEGIN TRANSACTION "+
			"SELECT TOP 1 "+
			"ab.AccountID , "+
			"[as].Username "+
			"FROM    RFO_Accounts.AccountBase AS ab "+
			"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE   ab.CountryID = %s "+
			"AND ab.AccountTypeID = 1 /*Consultant*/ "+

			        "AND NOT EXISTS ( SELECT 1 "+
			        "FROM   RFO_Accounts.AccountRF AS ar "+
			        "WHERE  ar.Active = 0 "+
			        "AND ar.HardTerminationDate IS NOT NULL "+
			        "AND ar.AccountID = ab.AccountID )  "+
			        /*Pending/Submitted Orders */
			        "AND EXISTS ( SELECT 1 "+
			        "FROM   Hybris.Orders AS o "+
			        "WHERE  o.AccountID = ab.AccountID "+
			        "AND o.OrderTypeID = 10 /*Consultant Auto-ship*/ "+
			        "AND o.OrderStatusID = 1 )  "+
			        /*Active Template*/
			        "AND EXISTS ( SELECT 1 "+
			        "FROM   Hybris.Autoship AS a "+
			        "WHERE  a.AccountID = ab.AccountID "+
			        "AND a.AutoshipTypeID = 2 /*Consultant Auto-ship Template*/ "+
			        "AND a.Active = 1 ) "+
			        "ORDER BY NEWID()";

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
	"WHERE   AB.CountryID = %s "+
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
					"WHERE   ab.CountryID = %s "+
					"AND ab.AccountTypeID = 3 /*RC*/ "+
					"AND ar.active = 1 "+
					/*Failed orders*/
					"AND EXISTS ( SELECT 1 "+
					"FROM   Hybris.Orders o, Hybris.dbo.orders o1 "+
					"WHERE  o.AccountID = ab.AccountID "+
					"AND o.orderid=o1.pk "+
					"AND o.OrderTypeID = 1 "+
					"AND o.OrderStatusID = 1 ) "+
					"ORDER BY NEWID()";

	public static String GET_SUBTOTAL_GRANDTOTAL_TAX_DETAILS_FOR_4296_RFO =
			"SELECT  * "+
					"FROM    Hybris.Orders AS O "+
					"WHERE   O.OrderID = '%s' ";

	public static String GET_SHIPPING_HANDLING_SHIPPINGMETHODID_DETAILS_FOR_4296_RFO =	
			"SELECT  * "+
					"FROM    Hybris.OrderShipment AS OS "+
					"WHERE   OS.OrderID = '%s' ";							

	public static String GET_ORDERID_FOR_ALL_RFO = "select top 1 * from hybris.orders where OrderNumber='%s'";

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
			    "WHERE   ab.CountryID = %s "+
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
			"WHERE ab.CountryID = %s AND ab.AccountTypeID = 1 "+ /*Consultant*/
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
			"WHERE ab.CountryID = %s AND ab.AccountTypeID = 1 "+ /*Consultant*/
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
					"WHERE a.Active = 0 AND a.AutoshipStatusID = 2 "+ /*Submitted*//*Inactive Accounts*/
					"AND EXISTS ( SELECT 1 FROM RFO_Accounts.AccountRF AS ar "+
					"WHERE ar.Active = 0 AND ar.HardTerminationDate IS NOT NULL "+
					"AND ar.AccountID = a.AccountID) AND a.NextRunDate >= GETDATE() "+ 
					"ORDER BY a.NextRunDate ";

	public static String GET_RANDOM_CONSULTANT_INACTIVE_RFO_4184= "SELECT TOP 1 ab.AccountID,[as].Username "+
			"FROM RFO_Accounts.AccountBase AS ab "+
			"JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"WHERE ab.CountryID = %s AND ab.AccountTypeID = 1  "+ /*Consultant*/
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
			"WHERE ab.CountryID = %s AND ab.AccountTypeID = 1 "+ /*Consultant*/
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
			"WHERE ab.CountryID = %s AND ab.AccountTypeID = 1 "+ /*Consultant*/ 
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
			"WHERE ab.CountryID = %s AND ab.AccountTypeID = 1 "+ /*Consultant*/
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
			"WHERE ab.CountryID = %s AND ab.AccountTypeID = 1 "+ /*Consultant*/
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
			" WHERE ab.CountryID = %s AND ab.AccountTypeID = 1 "+ /*Consultant*/
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
			   "WHERE ab.CountryID = %s "+
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
			 "WHERE ab.CountryID = %s "+
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
			 "WHERE ab.CountryID = %s "+
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
			 "WHERE ab.CountryID = %s "+
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
			 "WHERE   ab.CountryID = %s "+
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
			 "WHERE   ab.CountryID = %s "+  
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
			 "WHERE ab.CountryID = %s "+
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
			 "WHERE   ab.CountryID = %s "+
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
			 "WHERE   ab.CountryID = %s "+
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
			   "WHERE   ab.CountryID = %s "+
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
			   "WHERE   ab.CountryID = %s "+
			   "AND ab.AccountTypeID = 3 "+ /*Retail Customer*/
			   /*Active Accounts*/
			   "AND EXISTS ( SELECT 1 "+
			   "FROM   RFO_Accounts.AccountRF AS ar "+
			   "WHERE  ar.Active = 1 "+
			   "AND ar.HardTerminationDate IS NULL "+
			   "AND ar.AccountID = ab.AccountID ) "+
			   "ORDER BY NEWID()";

	public static String GET_RANDOM_RC_EMAIL_ID_HAVING_ACTIVE_ORDER_RFO = 
			"USE RFOperations "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+
					"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"WHERE   ab.CountryID = %s "+
					"AND ab.AccountTypeID = 3 /*Retail Customer*/ "+
					"AND ar.Active = 1 "+

			       /* Orders Check*/
			       "AND EXISTS ( SELECT 1 "+
			       "FROM   Hybris.Orders AS o, Hybris.dbo.orders o1 "+
			       "WHERE  o.AccountID = ab.AccountID "+
			       "AND o.orderid=o1.pk "+   
			       "AND o.OrderTypeID = 1) "+
			       "ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANT_NO_PWS_RFO =
			"USE RFOperations "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+
					"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"JOIN	 RFO_Accounts.AccountRF As arf ON arf.AccountID = ab.AccountID	"+			
					"WHERE   ab.AccountTypeID = 1 "+/*Consultant*/
					"AND	 ab.CountryID = '40' "+
					/*Active Accounts*/
					"AND 	 arf.Active = 1 "+
					/*Pulse*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   Hybris.Autoship AS a "+
					"WHERE  a.AccountID = ab.AccountID "+
					"AND a.AutoshipTypeID = 3 "+
					"AND a.Active = 1 ) "+
					"AND NOT EXISTS (SELECT 1 "+
					"FROM  RFO_Accounts.ConsultantPWSInfo AS CPI "+ 
					"WHERE CPI.AccountId = ab.AccountID) "+
					"ORDER BY NEWID() ";

	public static String GET_RANDOM_CONSULTANT_NO_PWS_WITH_COUNTRY_RFO =
			"USE RFOperations "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+
					"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"WHERE   ab.AccountTypeID = 1 "+/*Consultant*/
					"AND   ab.CountryID = %s "+
					/*Active Accounts*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS ar "+
					"WHERE  ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL "+
					"AND ar.AccountID = ab.AccountID ) "+
					/*Pulse*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   Hybris.Autoship AS a "+
					"WHERE  a.AccountID = ab.AccountID "+
					"AND a.AutoshipTypeID = 3 "+
					"AND a.Active = 1 ) "+
					"AND NOT EXISTS (SELECT 1 "+
					"FROM  RFO_Accounts.ConsultantPWSInfo AS CPI "+ 
					"WHERE CPI.AccountId = ab.AccountID) "+
					"ORDER BY NEWID() ";


	public static String GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO = "select * from Hybris.OrderShippingAddress where OrderID='%s'";

	public static String GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO =
			/*********************************************************************************************
			Query on RFO having active(i.e statusId =�1� ) consultant only with active consultant-autoship template with pending autoship and pending/submitted adhoc orders.
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
			"JOIN	 RFOperations.Hybris.Sites AS st on st.AccountID = ab.AccountID "+
			
			"WHERE   ab.CountryID = %s "+
			"AND ab.AccountTypeID = 1 "+/*Consultant*/
			"AND st.SitePrefix IS NOT NULL "+
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

	public static String GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO =
			"USE RFOperations "+
					"SET TRANSACTION  ISOLATION LEVEL READ UNCOMMITTED; "+ 
					"BEGIN TRANSACTION "+ 
					"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+ 
					"WHERE   ab.CountryID = %s "+
					"AND ab.AccountTypeID = 1 "+/*Consultant*/
					/*Active Accounts*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS ar "+ 
					"WHERE  ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL "+ 
					"AND ar.AccountID = ab.AccountID ) "+  
					"AND EXISTS ( SELECT 1 "+
					"FROM RFO_Accounts.EmailAddresses AS ea "+
					"WHERE  ea.EmailAddress = [as].Username) "+
					/*Active Template*/
					"AND EXISTS ( SELECT 1 "+
					"FROM   Hybris.Autoship AS a "+
					"WHERE  a.AccountID = ab.AccountID "+ 
					"AND a.AutoshipTypeID = 2 "+ /*Consultant Auto-ship Template*/
					"AND a.Active = 1 ) "+ 
					"ORDER BY NEWID();";

	public static String GET_RANDOM_CONSULTANT_WITH_PWS_RFO = 
			"USE RFOperations "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+
					"BEGIN TRANSACTION "+
					"SELECT TOP 1 "+
					"ab.AccountID , "+
					"[as].Username , "+
					"'http://' + S.SitePrefix + '.' + REPLACE(SD.Name,'stgmyrandf','myrfo%s') + '/%s' AS URL "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Reference.AccountType AS AT ON AT.AccountTypeID = ab.AccountTypeID "+
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"JOIN    RFO_Accounts.ConsultantPWSInfo AS CPI ON CPI.AccountId = ab.AccountID "+
					"JOIN    Hybris.Sites AS S ON S.AccountID = ab.AccountID "+
					"JOIN    Hybris.SiteURLs AS SUL ON SUL.SiteID = S.SiteID "+
					"JOIN    Hybris.SiteDomain AS SD ON SD.SiteDomainID = SUL.SiteDomainID "+
					"WHERE   ab.AccountTypeID = 1 "+/*Consultant*/
					"AND ab.CountryID = 40 "+
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
			"WHERE   ab.CountryID = %s "+
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
			"WHERE   ab.CountryID = %s "+
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
			"WHERE   ab.CountryID = %s "+
			"AND ab.AccountTypeID = 2 /*Preferred Customer*/ "+
			"AND ar.HardTerminationDate < DATEADD(DAY, -90,CONVERT(DATE, GETDATE())) "+
			"ORDER BY NEWID()";

	public static String GET_SPONSOR_ID = "select top 1 * from RFO_Accounts.AccountBase where accountId IN ( "+
			"SELECT TOP 1 "+
			"ab.AccountID "+
			"FROM    RFO_Accounts.AccountBase AS ab "+ 
			"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+ 
			"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+ 
			"WHERE   ab.CountryID = %s "+
			"AND ab.AccountTypeID = 1 "+/*Consultant*/
			/*Active Accounts*/
			"AND NOT EXISTS ( SELECT 1 "+ 
			"FROM   RFO_Accounts.AccountRF AS ar "+ 
			"WHERE  ar.Active = 0 "+
			"AND ar.HardTerminationDate IS NOT NULL "+ 
			"AND ar.AccountID = ab.AccountID )  "+
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
			"ORDER BY NEWID() )";

	public static String GET_RANDOM_ACTIVE_CONSULTANT_WITH_RETURN_ORDERS_RFO = 
			"SELECT AB.ACCOUNTID,EA.EMAILADDRESS "+
					"FROM "+
					"(SELECT DISTINCT ACCOUNTID "+
					"FROM RFOPERATIONS.HYBRIS.RETURNORDER WHERE ORDERID IN (SELECT ORDERID FROM RFOPERATIONS.HYBRIS.ORDERS WHERE AUTOSHIPID IS NOT NULL )) ARO, "+
					"RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTRF AB , "+
					"RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTBASE A , "+
					"RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTCONTACTS AC , "+
					"RFOPERATIONS.RFO_ACCOUNTS.ACCOUNTEMAILS ACE , "+
					"RFOPERATIONS.RFO_ACCOUNTS.EMAILADDRESSES EA "+
					"WHERE ARO.ACCOUNTID=AB.ACCOUNTID AND AB.ACCOUNTID=A.ACCOUNTID AND A.COUNTRYID=%s "+
					"AND  AB.ACCOUNTID=AC.ACCOUNTID  AND ACE.ACCOUNTCONTACTID=AC.ACCOUNTCONTACTID "+
					"AND EA.EMAILADDRESSID=ACE.EMAILADDRESSID AND ISDEFAULT=1 AND A.ACCOUNTTYPEID=1 AND AB.ACTIVE=1";

	public static String GET_ORDER_DETAILS_RFO = "select  * from Hybris.ReturnOrder where ReturnOrderNumber='%s'";

	public static String GET_RETURN_ORDER_STATUS_RFO = "select * from RFO_Reference.ReturnStatus where ReturnStatusId =%s";

	public static String GET_ACCOUNT_ID_OF_RETURN_CONSULTANT_AUTOSHIP_ORDER = " select Top 1 * from Hybris.ReturnOrder where accountID IN( "+
			"SELECT "+ 
			"ab.AccountID "+    
			"FROM    RFO_Accounts.AccountBase AS ab "+ 
			"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+ 
			"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+ 
			"WHERE   ab.CountryID = %s "+ 
			"AND ab.AccountTypeID = 1  "+ /*Consultant*/
			/*Active Accounts*/
			"AND NOT EXISTS ( SELECT 1  "+
			"FROM   RFO_Accounts.AccountRF AS ar "+ 
			"WHERE  ar.Active = 0  "+
			"AND ar.HardTerminationDate IS NOT NULL "+ 
			"AND ar.AccountID = ab.AccountID ) "+  
			/*Pending/Submitted Orders */ 
			"AND EXISTS ( SELECT 1  "+
			"FROM   Hybris.Orders AS o "+ 
			"WHERE  o.AccountID = ab.AccountID "+ 
			"AND o.OrderTypeID = 10) "+/*Consultant*/ 

	   /*Active Template*/
	   "AND EXISTS ( SELECT 1  "+
	   "FROM   Hybris.Autoship AS a "+ 
	   "WHERE  a.AccountID = ab.AccountID "+ 
	   "AND a.AutoshipTypeID = 2 "+ /*Consultant Auto-ship Template*/ 
	   "AND a.Active = 1 )) order by newID()";


	public static String GET_ACCOUNT_ID_OF_RETURN_PC_AUTOSHIP_ORDER = " select Top 1 * from Hybris.ReturnOrder where accountID IN( "+
			"SELECT "+ 
			"ab.AccountID "+    
			"FROM    RFO_Accounts.AccountBase AS ab "+ 
			"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+ 
			"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+ 
			"WHERE   ab.CountryID = %s "+ 
			"AND ab.AccountTypeID = 1  "+ /*Consultant*/
			/*Active Accounts*/
			"AND NOT EXISTS ( SELECT 1  "+
			"FROM   RFO_Accounts.AccountRF AS ar "+ 
			"WHERE  ar.Active = 0  "+
			"AND ar.HardTerminationDate IS NOT NULL "+ 
			"AND ar.AccountID = ab.AccountID ) "+  
			/*Pending/Submitted Orders */ 
			"AND EXISTS ( SELECT 1  "+
			"FROM   Hybris.Orders AS o "+ 
			"WHERE  o.AccountID = ab.AccountID "+ 
			"AND o.OrderTypeID = 9) "+/*Consultant*/ 

	     /*Active Template*/
	     "AND EXISTS ( SELECT 1  "+
	     "FROM   Hybris.Autoship AS a "+ 
	     "WHERE  a.AccountID = ab.AccountID "+ 
	     "AND a.AutoshipTypeID = 2 "+ /*Consultant Auto-ship Template*/ 
	     "AND a.Active = 1 )) order by newID()";

	public static String GET_ACCOUNT_TYPE_ID_FROM_ACCOUNT_NUMBER = "select AccountTypeID from  RFO_Accounts.AccountBase where AccountNumber='%s'";
	public static String GET_ACTIVE_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS = "select  * from RFO_Accounts.AccountBase join RFO_Accounts.AccountContacts ON RFO_Accounts.AccountContacts.AccountId = RFO_Accounts.AccountBase.AccountID join RFO_Accounts.AccountEmails ON RFO_Accounts.AccountEmails.AccountContactId = RFO_Accounts.AccountContacts.AccountContactId join RFO_Accounts.EmailAddresses ON RFO_Accounts.EmailAddresses.EmailAddressID = RFO_Accounts.AccountEmails.EmailAddressId where RFO_Accounts.EmailAddresses.EmailAddress like '%s' and RFO_Accounts.AccountBase.AccountStatusID='1'";

	public static String GET_CONSULTANT_EMAIL_ID_FROM_ACCOUNT_ID = "select Top 1 EmailAddress from RFO_Accounts.EmailAddresses where EmailAddressId IN (select EmailAddressId from RFO_Accounts.AccountEmails where AccountContactId IN (select AccountContactId from RFO_Accounts.AccountContacts where AccountId = '%s'))";

	public static String GET_ACCOUNT_CONTACT_ID_RFO = "select top 1 * from RFO_Accounts.AccountContacts where AccountId = '%s'";

	public static String GET_ACCOUNT_FirstName_RFO = "select top 1 FirstName from RFO_Accounts.AccountContacts where FirstName IS not NULL order by NEWID()";

	public static String GET_EMAIL_ADDRESS_ID_RFO = "select top 1 * from RFO_Accounts.AccountEmails where AccountContactId = '%s'";

	public static String GET_EMAIL_ID_RFO = "select top 1 * from RFO_Accounts.EmailAddresses WHERE EmailAddressID = '%S'";

	public static String GET_RANDOM_100_USERS_RFO = "select top 100 * from RFO_Accounts.EmailAddresses order by NEWID()";

	public static String GET_RANDOM_ACTIVE_SITE_PREFIX_RFO = "select top 1 SitePrefix from Hybris.Sites where accountID IN (select top 1 accountID from RFO_Accounts.AccountBase where countryID=%s and AccountStatusID=1 and AccountTypeID=1 order by newId())";

	public static String GET_ACCOUNT_NUMBER_FOR_PWS = "select top 1 * from RFO_Accounts.AccountBase where AccountID='%s'";

	public static String GET_ACCOUNT_CONTACT_ID_FROM_ACCOUNT_ID_RFO ="select * from RFO_Accounts.AccountContacts where accountId='%s'";
	public static String GET_EMAIL_ADDRESS_ID_FROM_ACCOUNT_CONTACT_ID_RFO ="select * from RFO_Accounts.AccountEmails where accountContactId='%s'";
	public static String GET_PRIMARY_EMAIL_ADDRESS_FROM_EMAIL_ADDRESS_ID_RFO ="select * from RFO_Accounts.EmailAddresses where EmailAddressId='%s' and EmailAddressTypeID='1'";

	public static String GET_ACCOUNT_ID = "select top 1 * from RFO_Accounts.AccountContacts where AccountContactId IN (select AccountContactId from RFO_Accounts.AccountContactAddresses  where AddressId IN (select AddressID from RFO_Accounts.Addresses where ( addresstypeid = '3' and IsDefault='1' and EndDate IS NULL and AddressId in  (select addressid from RFO_Accounts.AccountContactAddresses where AccountContactId IN (select TOP 1 AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))))))";

	public static String UPDATE_ENROLL_DATE = "update RFO_Accounts.AccountRF SET "+
			"EnrollmentDate = '3/12/2014' "+
			"WHERE AccountID = %s";

	public static String UPDATE_SOFT_TERMINATION_DATE = "update RFO_Accounts.AccountRF SET "+
			"SoftTerminationDate = '3/12/2015' "+
			"WHERE AccountID = %s";

	public static String GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS = "select * from RFO_Accounts.AccountBase join RFO_Accounts.AccountContacts ON RFO_Accounts.AccountContacts.AccountId = RFO_Accounts.AccountBase.AccountID join RFO_Accounts.AccountEmails ON RFO_Accounts.AccountEmails.AccountContactId = RFO_Accounts.AccountContacts.AccountContactId join RFO_Accounts.EmailAddresses ON RFO_Accounts.EmailAddresses.EmailAddressID = RFO_Accounts.AccountEmails.EmailAddressId where RFO_Accounts.EmailAddresses.EmailAddress like '%s'";

	public static String GET_ACCOUNT_DETAILS_QUERY = "select top 1 * from RFO_Accounts.AccountContacts where AccountContactId IN (select AccountContactId from RFO_Accounts.AccountEmails where EmailAddressID IN (select EmailAddressID from RFO_Accounts.EmailAddresses where EmailAddress='%s'))";

	public static String GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO = 
			"USE RFOperations "+
					"SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; "+ 
					"BEGIN TRANSACTION "+ 
					"SELECT TOP 1 "+
					"ab.AccountID , "+ 
					"[as].Username , "+                                                   
					"'http://' + S.SitePrefix + '.' + REPLACE(SD.Name,SUBSTRING(SD.Name,6,LEN(SD.Name)-6+1),'%s') + '/%s' AS URL "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Reference.AccountType AS AT ON AT.AccountTypeID = ab.AccountTypeID "+ 
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"JOIN    RFO_Accounts.ConsultantPWSInfo AS CPI ON CPI.AccountId = ab.AccountID "+
					"JOIN    Hybris.Sites AS S ON S.AccountID = ab.AccountID "+
					"JOIN    Hybris.SiteURLs AS SUL ON SUL.SiteID = S.SiteID "+
					"JOIN    Hybris.SiteDomain AS SD ON SD.SiteDomainID = SUL.SiteDomainID "+
					"WHERE   ab.AccountTypeID = 1 "+/*Consultant*/
					"AND ab.CountryID = 40 "+
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

	public static String GET_ALREADY_EXISTING_SITE_PREFIX_RFO =
			"SELECT TOP 1 ab.AccountID ,AT.Name AS AccountType ,S.SitePrefix,[as].Username "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Reference.AccountType AS AT ON AT.AccountTypeID = ab.AccountTypeID "+
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"join    Hybris.Sites AS S ON [as].AccountID = S.AccountID "+
					"WHERE   ab.CountryID = %s "+
					/*Active Accounts*/
					"AND NOT EXISTS ( SELECT 1 "+
					"FROM   RFO_Accounts.AccountRF AS ar "+
					"WHERE  ar.Active = 0 "+
					"AND ar.HardTerminationDate IS NOT NULL "+
					"AND ar.AccountID = ab.AccountID ) "+

			    /*PWS*/
			    "AND EXISTS ( SELECT 1 "+
			    "FROM   RFO_Accounts.ConsultantPWSInfo AS CPI "+
			    "WHERE  CPI.AccountId = ab.AccountID ) "+
			    "and [as].AccountID = %s "+


			    "ORDER BY NEWID()";

	public static String GET_ACTIVE_CONSULTANT_WITH_RETURN_AUTOSHIP_ORDER = "SELECT top 1 a.AccountID,a.Username,au.AutoshipTypeID,r.ReturnStatusID,o.OrdertypeID,* FROM RFOperations.RFO_Accounts.AccountBase account "+
			"INNER JOIN RFOperations.RFO_Accounts.AccountRF ARF ON account.AccountID = ARF.AccountID AND account.CountryID =%s "+
			"INNER JOIN RFOperations.Security.AccountSecurity (NOLOCK) a ON ARF.AccountID = a.AccountID "+
			"INNER JOIN RFOperations.Hybris.Autoship au ON account.AccountID = au.AccountID  and au.AutoshipstatusID =2 "+
			"INNER JOIN RFOperations.Hybris.Orders o ON au.AutoshipID = o.AutoshipId AND o.OrderTypeID  IN (9,10,12) "+
			"INNER JOIN RFOperations.Hybris.ReturnOrder r ON o.orderID = r.OrderID and r.ReturnStatusID = 5 "+
			"WHERE account.AccountTypeID = 1 and account.AccountStatusID =1 order by newID()";

	public static String GET_ORDER_ID_FROM_ORDER_NUMBER_FOR_RETURN_ORDER_RFO = "select * from Hybris.ReturnOrder where ReturnOrderNumber = '%s'";

	public static String GET_ACTIVE_PC_WITH_RETURN_PC_PERKS_AUTOSHIP_ORDER = "SELECT top 1 a.AccountID,a.Username,au.AutoshipTypeID,au.autoshipstatusID,r.ReturnStatusID,o.OrdertypeID,* FROM RFOperations.RFO_Accounts.AccountBase account "+
			"INNER JOIN RFOperations.RFO_Accounts.AccountRF ARF ON account.AccountID = ARF.AccountID AND account.CountryID =%s "+
			"INNER JOIN RFOperations.Security.AccountSecurity (NOLOCK) a ON ARF.AccountID = a.AccountID "+
			"INNER JOIN RFOperations.Hybris.Autoship au ON account.AccountID = au.AccountID  and au.AutoshipstatusID =2 and au.AutoshipTypeID = 1 "+
			"INNER JOIN RFOperations.Hybris.Orders o ON au.AutoshipID = o.AutoshipId AND o.OrderTypeID = 9 "+
			"INNER JOIN RFOperations.Hybris.ReturnOrder r ON o.orderID = r.OrderID and r.ReturnStatusID = 5 "+
			"WHERE account.AccountTypeID = 2 and account.AccountStatusID =1 order by newID()";

	public static String GET_ORDER_NUMBER_FROM_ACCOUNT_ID="select top 1 * from Hybris.Orders where AccountID='%s' ";

	public static String GET_USER_DETAIL_FROM_ACCOUNTID_RFO = "select top 1 * from RFO_Accounts.AccountContacts where AccountId='%s'";

	public static String GET_CONSULTANT_DETAILS_RFO = "SELECT top 1 ab.AccountID , AC.FirstName, AC.LastName,AT.Name AS AccountType,[as].Username "+
			"FROM    RFO_Accounts.AccountBase AS ab "+
			"JOIN    RFO_Reference.AccountType AS AT ON AT.AccountTypeID = ab.AccountTypeID "+
			"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"JOIN   RFO_Accounts.AccountContacts AS AC ON AC.AccountId = ab.AccountID "+
			"WHERE ab.CountryID = '%s' and ar.Active = 1 and ab.AccountTypeID = 1  "+
			"ORDER BY NEWID()";

	public static String GET_PENDING_CONSULTANT_DETAILS_RFO = "SELECT top 1 ab.AccountID , AC.FirstName, AC.LastName,AT.Name AS AccountType,[as].Username "+
			"FROM    RFO_Accounts.AccountBase AS ab "+
			"JOIN    RFO_Reference.AccountType AS AT ON AT.AccountTypeID = ab.AccountTypeID "+
			"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
			"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
			"JOIN   RFO_Accounts.AccountContacts AS AC ON AC.AccountId = ab.AccountID "+
			"WHERE ab.CountryID = '%s' and ar.Active = 1 and ab.AccountTypeID = 1 and ab.AccountStatusID = 4 "+
			"ORDER BY NEWID()";

	public static String GET_CONSULTANT_DETAILS_WITH_PWS_RFO = 
			"Use RFOperations "+
					"SELECT top 1 ab.AccountID , AC.FirstName, AC.LastName,AT.Name AS AccountType,[as].Username "+
					"FROM    RFO_Accounts.AccountBase AS ab "+
					"JOIN    RFO_Reference.AccountType AS AT ON AT.AccountTypeID = ab.AccountTypeID "+
					"JOIN    RFO_Accounts.ConsultantPWSInfo AS CPI ON CPI.AccountId = ab.AccountId "+
					"JOIN    RFO_Accounts.AccountRF AS ar ON ar.AccountID = ab.AccountID "+
					"JOIN    Security.AccountSecurity AS [as] ON ab.AccountID = [as].AccountID "+
					"JOIN   RFO_Accounts.AccountContacts AS AC ON AC.AccountId = ab.AccountID "+
					"WHERE ab.CountryID = '%s' and ar.Active = 1 and ab.AccountTypeID = 1  "+
					"ORDER BY NEWID()";

	public static String GET_RANDOM_CONSULTANTS_INACTIVE_MORE_THAN_180_DAYS =       
			"SELECT TOP 1 "+
					"rf.AccountID  , "+
					"rf.HardTerminationDate , "+
					"rf.Active , "+
					"ab.AccountTypeID,ead.* "+
					"FROM    RFOperations.RFO_Accounts.AccountRF rf "+
					"JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID "+
					"JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID "+
					"JOIN RFOperations.RFO_Accounts.AccountContactAddresses ad ON ad.AccountContactId = ac.AccountContactId "+   
					"JOIN RFOperations.RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId "+ 
					"JOIN RFOperations.RFO_Accounts.EmailAddresses ead ON ead.EmailAddressID=ae.EmailAddressId "+
					"WHERE   ab.CountryID = %s "+
					"AND ab.AccountTypeID = 1 "+
					"AND rf.Active = 0 "+
					"AND rf.HardTerminationDate < DATEADD(DAY, -180, GETDATE()) "+
					"AND ab.AccountStatusID IN ( 2 ,10) "+
					"ORDER BY NEWID()";

	public static String GET_RANDOM_INACTIVE_CONSULTANT_EMAILID_MORE_THAN_90_DAYS="SELECT TOP 1 "+
			"rf.AccountID , "+
			"rf.HardTerminationDate , "+
			"rf.Active , "+
			"ab.AccountTypeID,ead.* "+
			"FROM    RFOperations.RFO_Accounts.AccountRF rf "+
			"JOIN RFOperations.RFO_Accounts.AccountBase ab ON ab.AccountID = rf.AccountID "+
			"JOIN RFOperations.RFO_Accounts.AccountContacts ac ON ac.AccountId = ab.AccountID "+
			"JOIN RFOperations.RFO_Accounts.AccountContactAddresses ad ON ad.AccountContactId = ac.AccountContactId "+   
			"JOIN RFOperations.RFO_Accounts.AccountEmails ae ON ae.AccountContactId = ac.AccountContactId "+ 
			"JOIN RFOperations.RFO_Accounts.EmailAddresses ead ON ead.EmailAddressID=ae.EmailAddressId "+
			"WHERE   ab.CountryID = %s "+
			"AND ab.AccountTypeID = 1 "+ 
			"AND rf.Active = 0 "+
			"AND rf.HardTerminationDate < DATEADD(DAY, -90, GETDATE()) "+
			"AND ab.AccountStatusID IN ( 2 ,10) "+
			" ORDER BY NEWID()";

	public static String GET_USER_DETAILS_FROM_ACCOUNTID_RFO = "select top 1 * from RFO_Accounts.AccountContacts where AccountId='%s'";
	public static String GET_ACCOUNT_ID_FOR_PWS = "select top 1 * from RFO_Accounts.AccountBase where AccountNumber='%s'";
	public static String GET_EMAIL_ID_FROM_ACCOUNT_ID = "select EmailAddress from RFO_Accounts.AccountContacts acTB INNER JOIN RFO_Accounts.AccountEmails aeTB  "+
			"on acTB.AccountContactId=aeTB.AccountContactId INNER JOIN RFO_Accounts.EmailAddresses aEMTB on aeTB.EmailAddressID=aEMTB.EmailAddressID WHERE acTB.AccountID='%s' and aEMTB.EmailAddressTypeID=1 ";
	public static String GET_RETURN_ORDER_DETAILS_FROM_RMA_NUMBER ="select top 1 * from Hybris.ReturnOrder where returnordernumber='%s'";
	public static String GET_ACTIVE_US_CONSULTANT_WITHOUT_PWS_AND_PULSE="SELECT TOP 1 HYBRIS.dbo.USERS.P_CUSTOMEREMAIL ,HYBRIS.dbo.USERS.P_RFACCOUNTID, RFO_Accounts.AccountRF.Active, "+
			"HYBRIS.dbo.users.p_rfaccountnumber, RFO_Accounts.AccountBase.AccountNumber "+
			"FROM HYBRIS.dbo.USERS "+ 
			"left join RFO_Accounts.AccountBase on HYBRIS.dbo.users.p_rfaccountid=RFO_accounts.Accountbase.AccountID "+
			"left join RFO_Accounts.AccountRF ON RFO_Accounts.AccountRF.AccountID = RFO_Accounts.AccountBase.AccountID "+
			"WHERE HYBRIS.dbo.USERS.P_LOGINDISABLED=0 AND HYBRIS.dbo.USERS.P_SOURCENAME='Hybris-DM' "+
			"AND HYBRIS.dbo.USERS.P_ENROLLEDASPULSE=0 AND HYBRIS.dbo.USERS.P_COUNTRY= 8796100624418 "+
			"AND HYBRIS.dbo.USERS.P_CUSTOMURLPREFIX IS NULL AND RFO_Accounts.AccountRF.Active=1 "+
			"AND RFO_Accounts.AccountBase.AccountTypeID = 1 order by newid()";

	public static String GET_ACTIVE_CA_CONSULTANT_WITHOUT_PWS_AND_PULSE="SELECT TOP 1 HYBRIS.dbo.USERS.P_CUSTOMEREMAIL ,HYBRIS.dbo.USERS.P_RFACCOUNTID, RFO_Accounts.AccountRF.Active, "+
			"HYBRIS.dbo.users.p_rfaccountnumber, RFO_Accounts.AccountBase.AccountNumber "+
			"FROM HYBRIS.dbo.USERS "+ 
			"left join RFO_Accounts.AccountBase on HYBRIS.dbo.users.p_rfaccountid=RFO_accounts.Accountbase.AccountID "+
			"left join RFO_Accounts.AccountRF ON RFO_Accounts.AccountRF.AccountID = RFO_Accounts.AccountBase.AccountID "+
			"WHERE HYBRIS.dbo.USERS.P_LOGINDISABLED=0 AND HYBRIS.dbo.USERS.P_SOURCENAME='Hybris-DM' "+
			"AND HYBRIS.dbo.USERS.P_ENROLLEDASPULSE=0 AND HYBRIS.dbo.USERS.P_COUNTRY= 8796094300194 "+
			"AND HYBRIS.dbo.USERS.P_CUSTOMURLPREFIX IS NULL AND RFO_Accounts.AccountRF.Active=1 "+
			"AND RFO_Accounts.AccountBase.AccountTypeID = 1 order by newid()";

	public static String GET_ACTIVE_CONSULTANT_WITHOUT_PWS_AND_PULSE="SELECT TOP 1 HYBRIS.dbo.USERS.P_CUSTOMEREMAIL ,HYBRIS.dbo.USERS.P_RFACCOUNTID, RFO_Accounts.AccountRF.Active, "+ 
			"HYBRIS.dbo.users.p_rfaccountnumber, RFO_Accounts.AccountBase.AccountNumber "+
			"FROM HYBRIS.dbo.USERS "+
			"left join RFO_Accounts.AccountBase on HYBRIS.dbo.users.p_rfaccountid=RFO_accounts.Accountbase.AccountID "+
			"left join RFO_Accounts.AccountRF ON RFO_Accounts.AccountRF.AccountID = RFO_Accounts.AccountBase.AccountID "+
			"WHERE HYBRIS.dbo.USERS.P_LOGINDISABLED=0 AND HYBRIS.dbo.USERS.P_SOURCENAME='Hybris-DM' "+
			"AND HYBRIS.dbo.USERS.P_ENROLLEDASPULSE=0 AND RFO_Accounts.AccountBase.CountryID = '%s' "+
			"AND HYBRIS.dbo.USERS.P_CUSTOMURLPREFIX IS NULL AND RFO_Accounts.AccountRF.Active=1 "+ 
			"AND RFO_Accounts.AccountBase.AccountTypeID = 1 order by newid()";

	public static String GET_RANDOM_ACTIVE_CONSULTANT_WITHOUT_PULSE_RFO = "Select top 1 EA.EmailAddress, AB.AccountNumber "+ 
			"from RFO_Accounts.AccountBase as AB "+
			"join RFO_Accounts.AccountRF as ARF ON ARF.AccountID = AB.AccountID "+
			"join RFO_Accounts.AccountContacts as AC ON AC.AccountId = AB.AccountID "+
			"join RFO_Accounts.AccountEmails as AE ON AE.AccountContactId = AC.AccountContactId "+
			"join RFO_Accounts.EmailAddresses as EA ON EA.EmailAddressID = AE.EmailAddressId "+
			"where ARF.EnrollmentDate < dateadd(day, -30, getdate()) "+ 
			"AND AB.CountryID = %s "+ 
			"AND AB.AccountStatusID = 1 "+ 
			"AND AB.AccountTypeID = 1 "+
			"AND AB.AccountID in "+ 
			"(Select accountid from Hybris.autoship "+ 
			"where accountid not in "+
			"(select accountid from hybris.autoship "+ 
			"where AutoshipTypeID = 3 and active = 1) "+ 
			"group by AccountID "+
			") AND AB.AccountID in "+
			"(select accountid from hybris.sites where EndDate<getdate() "+
			")order by newid()";

	public static String GET_UPDATED_USERNAME_FROM_RFO = "select * from security.accountsecurity where AccountId='%s'";

	public static String GET_ORDER_DETAILS_FROM_ORDER_NUMBER=
			"select * from Hybris.Orders "+
					"join Hybris.OrderItem ON Hybris.OrderItem.OrderId = Hybris.Orders.OrderID "+
					"join Hybris.ProductBase ON Hybris.ProductBase.productID = Hybris.OrderItem.ProductID "+
					"join Hybris.OrderBillingAddress ON Hybris.OrderBillingAddress.OrderID = Hybris.Orders.OrderID "+
					"join Hybris.OrderShippingAddress ON Hybris.OrderShippingAddress.OrderID = Hybris.Orders.OrderID "+
					"where Hybris.Orders.OrderNumber like '%s'";

	public static String GET_ACCOUNT_ID_FOR_PENDING_USER="select tOP 1 * from RFO_Accounts.AccountBase where AccountStatusID = 4 ORDER BY newid()";


	/**
	 * 
	 * @param query
	 * @param value
	 * @return
	 */

	public static String callQueryWithArguement(String query,String value){
		return String.format(query, value);
	}

	public static String callQueryWithArguementPWS(String query,String env,String country){
		return String.format(query, env,country);
	}

	public static String callQueryWithArguementPWS(String query,String env,String country,String countryID){
		return String.format(query, env,country,countryID);
	}
}



