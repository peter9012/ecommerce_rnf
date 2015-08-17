package com.rf.test.website.storeFront.dataMigration.rfo.order;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontRCUserPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class ViewOrderDetailsTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(ViewOrderDetailsTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	
	private String RFO_DB = null;

	//Hybris Phase 2-4286 :: Version : 1 :: Verify order details of CRP autoship order
	@Test
	public void testOrderDetailsOfCRPAutoShipOrder_HP2_4286() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO();

		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String phoneNumber = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String payeeNameDB = null;
		String cardTypeDB = null;
		String consultantEmailID = null;
		String lastName = null;
		String orderId = null;
		String accountId = null;
		String shippingMethodId =null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyCardTypeList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");

		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_ORDER_ID_ACCOUNT_ID_4286_RFO,RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));
		accountId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "AccountID"));
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACTIVE_CONSULTANT_USER_WITH_ACTIVE_CRP_AUTOSHIP_4286_RFO,accountId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");


		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number for assert
		String orderHistoryNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//			verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_ACTIVE_CONSULTANT_USER_HAVING_AUTOSHIP_ORDER_RFL, orderHistoryNumber), RFL_DB);
		//			firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		//			lastName =  (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		//			addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		//			city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		//			state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		//			postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		//			phoneNumber = (String) getValueFromQueryResult(verifyAllDetailsList, "BillingPhoneNumber");
		//			country = df.format((Number)(getValueFromQueryResult(verifyAllDetailsList, "CountryID")));
		//			if(country.equals("1")){
		//				country = "United States"; 
		//			}
		//			shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		// assert shipping Address with RFO
		//assertTrue("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4286_RFO,orderId),RFO_DB);
		subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
		taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
		grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));
		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4286_RFO,orderId),RFO_DB);
		shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
		handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));

		//Assert Subtotal with RFO
		assertTrue("SubTotal is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));

		// Assert Tax with RFO
		assertTrue("Tax is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateTax(taxDB));

		// Assert Grand Total with RFO
		assertTrue("Grand Total is not as expected", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));

		// assert shipping amount with RFO
		assertTrue("Shipping Amount is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));

		// assert Handling Value with RFO
		assertTrue("Handling Value is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));

		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));	

		// assert for shipping Method with RFO
		assertTrue("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(methodName));


		logout();
		s_assert.assertAll();

	}
	//- - - - - -- - - - -- -******************************REASON FOR COMMENTED CODE*****************- - - - - - - - - - - - - - - - - -
	//Assertion for	autoShipItemDetailsList for subTotalDB value query result is not as in UI for both RFO and RFL.
	//Assertion for	autoShipItemDetailsList for taxDB value query result is not as in UI for both RFO and RFL.
	//Assertion for	autoShipItemDetailsList for grantTotalDB value query result is not as in UI for both RFO and RFL.
	//Unable to find query for shipping method for RFO database.

	// phase 2 4287 -> Verify order details of consultant order
	@Test
	public void testOrdersDetailsOfConsultant_HP2_4287() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO();

		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String phoneNumber = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String consultantEmailID = null;
		String orderId = null;
		String accountId = null;
		String shippingMethodId =null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyCardTypeList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String,Object>> getOrderIDList = null;
		DecimalFormat df = new DecimalFormat("#.00");

		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_ORDER_ID_ACCOUNT_ID_4287_RFO,RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));
		accountId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "AccountID"));
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACTIVE_CONSULTANT_WITH_ADHOC_ORDER_4287_RFO,accountId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");


		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		// Get Order Id
		getOrderIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderIDList, "OrderID"));

		//   verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_ACTIVE_CONSULTANT_USER_HAVING_ADHOC_ORDER_RFL, orderHistoryNumber), RFL_DB);
		//   firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "BillingFirstName");
		//   addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		//   city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		//   state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		//   postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		//   phoneNumber = (String) getValueFromQueryResult(verifyAllDetailsList, "PhoneNumber");
		//   country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		//   if(country.equals("1")){
		//    country = "United States"; 
		//   }
		//   shippingAddressFromDB = firstName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		// assert shipping Address with RFO
		//   assertTrue("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));


		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4287_RFO,orderId),RFO_DB);
		subTotalDB = df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal"));

		taxDB = df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax"));

		grandTotalDB = df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total"));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4287_RFO,orderId),RFO_DB);
		shippingDB = df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost"));

		handlingDB = df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost"));

		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));

		//Assert Subtotal with RFO
		assertTrue("SubTotal is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		// Assert Tax with RFO
		assertTrue("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		// Assert Grand Total with RFO
		assertTrue("Grand Total is not as expected", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));
		// assert shipping amount with RFO
		assertTrue("Shipping Amount is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		// assert Handling Value with RFO
		assertTrue("Handling Value is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));

		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));

		// assert for shipping Method with RFL
		assertTrue("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(methodName));

		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4292 :: Version : 1 :: Verify order details of pc Order (i.e. Adhoc Order)
	@Test
	public void testOrderDetailsForAdhocOrdersForPC_4292() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();

		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String phoneNumber = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String pcEmailID = null;
		String lastName = null;
		String orderId = null;
		String accountId = null;
		String shippingMethodId =null;
		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");

		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_ORDER_ID_ACCOUNT_ID_4292_RFO,RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		accountId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "AccountID"));

		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_USERNAME_BY_ACCOUNT_ID_RFO,accountId),RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, TestConstants.PC_PASSWORD_TST4);

		s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC user Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		// Get Order Id
		List<Map<String, Object>> getOrderIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderIDList, "OrderID"));

		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksOrderPageHeader(),"Order Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		//  verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ADDRESS_DETAILS_FOR_4292_RFL, orderHistoryNumber), RFL_DB);
		//  firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		//  lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		//  addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		//  city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		//  state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		//  postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		//  phoneNumber = (String) getValueFromQueryResult(verifyAllDetailsList, "PhoneNumber");
		//  country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		//  if(country.equals("1")){
		//   country = "United States"; 
		//  }
		//  shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		// assert shipping Address with RFO
		//  assertTrue("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4292_RFO,orderId),RFO_DB);
		subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));

		taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));

		grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4292_RFO,orderId),RFO_DB);
		shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));

		handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));

		//Assert Subtotal with RFO
		assertTrue("SubTotal is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));

		// Assert Tax with RFO
		//assertTrue("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		assertTrue("Tax is not as expected",storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));

		// Assert Grand Total with RFO
		assertTrue("Grand Total is not as expected", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));

		// assert shipping amount with RFO
		assertTrue("CRP Autoship Shipping value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));

		// assert Handling Value with RFO
		assertTrue("Handling Value is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));

		// assert for shipping Method with RFO
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		assertTrue("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(methodName));  

		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4291 :: Version : 1 :: Verify PC autoship order. 
	@Test
	public void testOrderDetailsForAutoshipOrdersForPC() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String phoneNumber = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String payeeNameDB = null;
		String cardTypeDB = null;
		String consultantEmailID = null;
		String lastName = null;
		String orderId = null;
		String accountId = null;
		String shippingMethodId = null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_ORDER_ID_ACCOUNT_ID_4291_RFO,RFO_DB);
		orderId = String.valueOf( getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));       
		accountId = String.valueOf( getValueFromQueryResult(orderIdAccountIdDetailsList, "AccountID"));

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACTIVE_CONSULTANT_WITH_AUTOSHIP_ORDER_4291_RFO,accountId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");


		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String autoshipOrderNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksAutoShipHeader(),"Order Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		//		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ADDRESS_DETAILS_FOR_RFL, autoshipOrderNumber), RFL_DB);
		//		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		//		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		//		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		//		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		//		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		//		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		//		phoneNumber = (String) getValueFromQueryResult(verifyAllDetailsList, "PhoneNumber");
		//		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		//		if(country.equals("1")){
		//			country = "United States"; 
		//		}
		//		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		// assert shipping Address with RFO
		//		assertTrue("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));


		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4291_RFO,orderId),RFO_DB);
		subTotalDB =df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "SubTotal"));

		taxDB = df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "TotalTax"));

		grandTotalDB = df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total"));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4291_RFO,orderId),RFO_DB);
		shippingDB = df.format((Number) getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost"));

		handlingDB = df.format((Number) getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost"));

		shippingMethodId =  String.valueOf( getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));

		//Assert Subtotal with RFO
		assertTrue("SubTotal is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));

		// Assert Tax with RFO
		assertTrue("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));

		// Assert Grand Total with RFO
		assertTrue("Grand Total is not as expected", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));


		// assert shipping amount with RFO
		assertTrue("Shipping Amount is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));

		// assert Handling Value with RFL
		assertTrue("Handling Value is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));

		// assert for shipping Method with RFO
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		//		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		//		if(methodName.equalsIgnoreCase("FedEx Grnd")){
		//			methodName = "FedEx Ground";
		//		}
		//
		//		shippingMethodDB = methodName+" "+"("+methodShortName+")";
		//		System.out.println("Method from DB "+shippingMethodDB);
		// assert Shipping Method with RFO
		assertTrue("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(methodName));

		logout();
		s_assert.assertAll();
	}

	//Hybris Phase 2-4293 :: Version : 1 :: Verify details of retail order. 
	@Test
	public void testOrderDetailsForAdhocOrdersForRC() throws InterruptedException{
		 RFO_DB = driver.getDBNameRFO();

		  String firstName = null;
		  String addressLine1 = null;
		  String postalCode = null;
		  String locale = null;
		  String region = null;
		  String country = null;
		  String shippingAddressFromDB =null;
		  String city = null;
		  String state = null;
		  String phoneNumber = null;
		  String subTotalDB = null;
		  String shippingDB = null;
		  String handlingDB = null;
		  String taxDB = null; 
		  String grandTotalDB = null;
		  String shippingMethodDB = null;
		  String payeeNameDB = null;
		  String cardTypeDB = null;
		  //String consultantEmailID = "Arlene.vaccaro@yahoo.com";
		  String lastName = null;
		  String orderId = null;
		  String accountId = null;
		  String shippingMethodId = null;
		  String rcEmailID = null;
		  List<Map<String, Object>> randomRCList =  null;
		  List<Map<String, Object>> verifyAllDetailsList = null;
		  List<Map<String, Object>> verifyShippingMethodList = null;
		  List<Map<String, Object>> verifyOrderDetailsList = null;
		  List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		  List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		  List<Map<String,Object>> getOtherDetailValuesList = null;
		  DecimalFormat df = new DecimalFormat("#.00");
		  orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_ORDER_ID_ACCOUNT_ID_4293_RFO,RFO_DB);
		  orderId = String.valueOf( getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		  accountId = String.valueOf( getValueFromQueryResult(orderIdAccountIdDetailsList, "AccountID"));

		  randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_USERNAME_BY_ACCOUNT_ID_RFO,accountId),RFO_DB);
		  rcEmailID = (String) getValueFromQueryResult(randomRCList, "Username");

		  storeFrontHomePage = new StoreFrontHomePage(driver);
		  storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID, TestConstants.RC_PASSWORD_TST4);
		  s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcEmailID),"RC user Page doesn't contain Welcome User Message");
		  logger.info("login is successful");
		  storeFrontRCUserPage.clickOnWelcomeDropDown();
		  storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		  s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		  // Get Order Number
		  String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		  storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		  // Get Order Id
		  List<Map<String, Object>> getOrderIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		  orderId = String.valueOf(getValueFromQueryResult(getOrderIDList, "OrderID"));

		  //  verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ADDRESS_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		  //  firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		  //  lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		  //  addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		  //  city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		  //  state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		  //  postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		  //  phoneNumber = (String) getValueFromQueryResult(verifyAllDetailsList, "PhoneNumber");
		  //  country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		  //  if(country.equals("1")){
		  //   country = "United States"; 
		  //  }
		  //  shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		  // assert shipping Address with RFO
		  // assertTrue("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));


		  getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4293_RFO,orderId),RFO_DB);
		  subTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));

		  taxDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));

		  grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		  shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4293_RFO,orderId),RFO_DB);
		  shippingDB = String.valueOf(df.format((Number) getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));

		  handlingDB = String.valueOf(df.format((Number) getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		  shippingMethodId = String.valueOf( getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));


		  //Assert Subtotal with RFO
		  assertTrue("SubTotal is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));

		  // Assert Tax with RFO
		  assertTrue("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));

		  // Assert Grand Total with RFO
		  assertTrue("Grand Total is not as expected", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));

		  // assert shipping amount with RFO
		  assertTrue("Shipping Amount is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));

		  // assert Handling Value with RFO
		  assertTrue("Handling Value is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));


		  // assert for shipping Method with RFO
		  verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		  String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		  System.out.println("Shipping Method"+methodName);
		  assertTrue("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(methodName));
		  logout();
		  s_assert.assertAll();
	}

}

