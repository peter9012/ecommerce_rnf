package com.rf.core.website.constants.dbQueries;

public class DBQueries_RFO {

	//RFO Queries
	public static String GET_EMAIL_ACCOUNT_QUERY_RFO= "select count(*) as count from RFO_Accounts.AccountBase "+
			"join RFO_Accounts.AccountContacts ON RFO_Accounts.AccountContacts.AccountId = RFO_Accounts.AccountBase.AccountID "+
			"join RFO_Accounts.AccountEmails ON RFO_Accounts.AccountEmails.AccountContactId = RFO_Accounts.AccountContacts.AccountContactId "+
			"join RFO_Accounts.EmailAddresses ON RFO_Accounts.EmailAddresses.EmailAddressID = RFO_Accounts.AccountEmails.EmailAddressId "+
			"where RFO_Accounts.EmailAddresses.EmailAddress like '%s'";

	public static String GET_ORDER_DETAILS_QUERY_RFO = "select * from Hybris.Orders "+
			"join Hybris.OrderItem ON Hybris.OrderItem.OrderId = Hybris.Orders.OrderID "+
			"join Hybris.ProductBase ON Hybris.ProductBase.productID = Hybris.OrderItem.ProductID "+
			"join Hybris.OrderBillingAddress ON Hybris.OrderBillingAddress.OrderID = Hybris.Orders.OrderID "+
			"join Hybris.OrderShippingAddress ON Hybris.OrderShippingAddress.OrderID = Hybris.Orders.OrderID "+
			"where Hybris.Orders.OrderNumber like '%s'";

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



