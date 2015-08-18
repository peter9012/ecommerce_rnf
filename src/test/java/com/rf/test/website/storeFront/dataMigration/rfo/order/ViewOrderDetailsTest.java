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
		String consultantEmailID = null;
		String lastName = null;
		String orderId = null;
		String accountId = null;
		String shippingMethodId =null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyShippingMethodList = null;
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
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"CRP autoship template subTotal on RFO is "+subTotalDB+"and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"CRP autoship template tax amount on RFO is "+taxDB+"and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"CRP autoship template grand total on RFO is "+grandTotalDB+"and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"CRP autoship template shipping amount on RFO is "+shippingDB+"and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"CRP autoship template handling amount on RFO is "+handlingDB+"and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));	

		// assert for shipping Method with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(methodName),"CRP autoship template shipping method on RFO is "+methodName+"and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

		logout();
		s_assert.assertAll();

	}


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
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc Order template subtotal on RFO is "+subTotalDB+"and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),"Adhoc Order template tax on RFO is "+taxDB+"and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc Order template grand total on RFO is "+grandTotalDB+"and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc Order template shipping amount on RFO is "+shippingDB+"and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc Order template handling amount on RFO is "+handlingDB+"and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));

		// assert for shipping Method with RFL
		assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(methodName),"Adhoc Order template shipping method on RFO is "+methodName+"and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

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
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc order template subTotal on RFO is "+subTotalDB+"and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		//assertTrue("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),"Adhoc order template tax amount on RFO is "+taxDB+"and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc order template grand total on RFO is "+grandTotalDB+"and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc order template shipping amount on RFO is "+shippingDB+"and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc order template handling amount on RFO is "+handlingDB+"and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(methodName),"Adhoc order template shipping method on RFO is "+methodName+"and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate());  

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
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"PC autoship template subTotal is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),"PC autoship template tax amount is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"PC autoship template grand total is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());


		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"PC autoship template shipping amount is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFL
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"PC autoship template handling amount is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(methodName),"PC autoship template shipping method is "+methodName+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

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
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate().contains(subTotalDB),"Adhoc Order template subTotal from RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),"Adhoc Order template tax amount from RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate().contains(grandTotalDB),"Adhoc Order template grand total from RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate().contains(shippingDB),"Adhoc Order template shipping amount from RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate().contains(handlingDB),"Adhoc Order template handling amount from RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate());


		// assert for shipping Method with RFO
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(methodName),"Adhoc Order template shipping method from RFO is "+methodName+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate());
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-4294:Verify details of failed consultant autoship orders.
	@Test
	public void testVerifyDetailsForFailedAutoshipOrdersForConsultant() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String lastName = null;
		String consultantEmailID =  null;//"mwrankin@verizon.net";
		String orderId = null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String, Object>> verifyOrderDetailsShippingAndHandlingList = null;
		List<Map<String, Object>> getOrderIdDetailsList = null;
		List<Map<String, Object>> getOrderNumberDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String,Object>> orderIdDetailsList = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_ACTIVE_CONSULTANT_HAVING_FAILED_CRP_ORDER_4294_RFO,RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		// Get Order Number
		String failedOrderNumber = storeFrontOrdersPage.getOrderNumberFromOrderHistoryForFailedAutoshipOrdersForConsultant();
		//Get order Id from order number
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, failedOrderNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("236")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		//verify shipping address in RFO
		//assertTrue(storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), "Shipping Address is not as expected");
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SUBTOTAL_GRANDTOTAL_TAX_DETAILS_FOR_4294_RFO, orderId), RFO_DB);
		subTotalDB = String.valueOf((df.format(getValueFromQueryResult(verifyOrderDetailsList, "SubTotal"))));
		taxDB =  String.valueOf((df.format(getValueFromQueryResult(verifyOrderDetailsList, "TotalTax"))));
		grandTotalDB =  String.valueOf((df.format(getValueFromQueryResult(verifyOrderDetailsList, "Total"))));
		verifyOrderDetailsShippingAndHandlingList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_HANDLING_SHIPPINGMETHODID_DETAILS_FOR_4294_RFO, orderId), RFO_DB);
		shippingDB =  String.valueOf((df.format(getValueFromQueryResult(verifyOrderDetailsShippingAndHandlingList, "ShippingCost"))));
		handlingDB =  String.valueOf((df.format(getValueFromQueryResult(verifyOrderDetailsShippingAndHandlingList, "HandlingCost"))));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyOrderDetailsShippingAndHandlingList, "ShippingMethodID"));

		//Assert Subtotal with RFO
		assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));

		// assert shipping amount with RFO
		assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));

		// assert Handling Value with RFO
		assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));

		// assert Tax with RFO
		assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));

		// assert Grand Total with RFO
		assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));

		// assert Shipping Method with RFO
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		if(methodName.contains("FedEx Grnd")){
			methodName = "FedEx Ground (HD)";
		}
		assertTrue("Shipping Method is NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));

		logout();
		s_assert.assertAll();


	}
	//Hybris Project-4295:Verify details of failed pc autoship orders.
	@Test
	public void testVerifyDetailsForFailedAutoshipOrdersForPC() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String lastName = null;
		String orderIDForFailedOrders = null;
		String failedorderNumber = null;
		String accountIdForPCFailedOrder = null;
		String orderId = null;
		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String, Object>> getOrderIdDetailsList = null;
		List<Map<String, Object>> getOrderNumberDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String,Object>> orderIdDetailsList = null;
		List<Map<String,Object>> verifyOrderDetailsShippingAndHandlingList = null;
		String PCEmailID = null;
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_PC_USER_FOR_FAILED_AUTOSHIP_ORDER_RFO,RFO_DB);
		PCEmailID = String.valueOf(getValueFromQueryResult(randomPCList, "Username"));

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(PCEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String failedOrderNumber = storeFrontOrdersPage.getOrderNumberFromOrderHistoryForFailedAutoshipOrdersForPC();				
		//Get order Id from order number
		orderIdDetailsList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO,failedOrderNumber),RFO_DB);
		orderId = String.valueOf( getValueFromQueryResult(orderIdDetailsList,"OrderID")); 

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("236")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		// assertTrue(storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), "Shipping Address is not as expected");
		DecimalFormat df = new DecimalFormat("#.00");
		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SUBTOTAL_GRANDTOTAL_TAX_DETAILS_FOR_4295_RFO, orderId), RFO_DB); 
		subTotalDB = String.valueOf((df.format(getValueFromQueryResult(verifyOrderDetailsList, "SubTotal"))));
		taxDB =  String.valueOf((df.format(getValueFromQueryResult(verifyOrderDetailsList, "TotalTax"))));
		grandTotalDB =  String.valueOf((df.format(getValueFromQueryResult(verifyOrderDetailsList, "Total"))));
		verifyOrderDetailsShippingAndHandlingList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_HANDLING_SHIPPINGMETHODID_DETAILS_FOR_4295_RFO, orderId), RFO_DB);
		shippingDB =  String.valueOf((df.format(getValueFromQueryResult(verifyOrderDetailsShippingAndHandlingList, "ShippingCost"))));
		handlingDB =  String.valueOf((df.format(getValueFromQueryResult(verifyOrderDetailsShippingAndHandlingList, "HandlingCost"))));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyOrderDetailsShippingAndHandlingList, "ShippingMethodID"));

		//Assert Subtotal with RFO
		assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));

		// assert shipping amount with RFO

		assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));


		// assert Handling Value with RFO

		assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));



		// assert Tax with RFO

		assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));



		// assert Grand Total with RFO

		assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));



		// assert Shipping Method with RFO
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		//String methodNameRFO = null;
		if(methodName.contains("FedEx Grnd")){
			methodName = "FedEx Ground (HD)";
		}
		assertTrue("Shipping Method is NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));

		logout();
		s_assert.assertAll();

	}
	//Hybris Project-4296::Verify details of failed retail orders
	@Test
	public void testVerifyDetailsForFailedAutoshipOrdersForRC() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String RCEmailID = null;
		String lastName = null;
		String orderIDForFailedOrders = null;
		String accountIdForPCFailedOrder = null;
		String orderId = null;
		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String, Object>> getOrderIdDetailsList = null;
		List<Map<String, Object>> getOrderNumberDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String,Object>> orderIdDetailsList = null;
		List<Map<String,Object>> verifyOrderDetailsShippingAndHandlingList = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_ACTIVE_RC_USER_HAVING_FAILED_ORDERS_RFO,RFO_DB);
		RCEmailID = String.valueOf(getValueFromQueryResult(randomRCList, "Username"));

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(RCEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String failedOrderNumber = storeFrontOrdersPage.getOrderNumberFromOrderHistoryForFailedAdhocOrdersForRC();
		//Get order Id from order number
		orderIdDetailsList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO,failedOrderNumber),RFO_DB);
		orderId = String.valueOf( getValueFromQueryResult(orderIdDetailsList,"OrderID")); 

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("236")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		//assertTrue(storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), "Shipping Address is not as expected");
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SUBTOTAL_GRANDTOTAL_TAX_DETAILS_FOR_4296_RFO, orderId), RFO_DB); 
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal")));
		taxDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "TotalTax")));
		grandTotalDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "Total")));
		verifyOrderDetailsShippingAndHandlingList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_HANDLING_SHIPPINGMETHODID_DETAILS_FOR_4296_RFO, orderId), RFO_DB);
		shippingDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsShippingAndHandlingList, "ShippingCost")));
		handlingDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsShippingAndHandlingList, "HandlingCost")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyOrderDetailsShippingAndHandlingList, "ShippingMethodID"));
		//Assert Subtotal with RFO
		assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));

		// assert shipping amount with RFO
		assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));



		// assert Handling Value with RFO
		assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));


		// assert Tax with RFO
		assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));



		// assert Grand Total with RFO
		assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));

		// assert Shipping Method with RFO
		verifyShippingMethodList =	DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		if(methodName.contains("FedEx Grnd")){
			methodName = "FedEx Ground (HD)";
		}
		assertTrue("Shipping Method NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));

		logout();
		s_assert.assertAll();

	}

}

