package com.rf.test.website.storeFront.dataMigration.rfl.order;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
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
	private StoreFrontConsultantPage storeFrontConsultantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontRCUserPage storeFrontRCUserPage; 
	private StoreFrontPCUserPage storeFrontPCUserPage;

	private String RFL_DB = null;
	private String RFO_DB = null;


	//Hybris Phase 2-4286 :: Version : 1 :: Verify order details of CRP autoship order
	@Test
	public void testOrderDetailsOfCRPAutoShipOrder_HP2_4286() throws SQLException, InterruptedException{
		RFL_DB = driver.getDBNameRFL();
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
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String shippingMethod = null;
		//String consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A hard code user
		String consultantEmailID = null;
		String lastName = null;
		String shippingMethodId = null;
		String shippingMethodDB = null;

		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> orderIdAccountIdDetailsList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");


		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.verifyCRPAutoShipHeader(),"CRP Autoship template header is not as expected");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		//verify shipping address in RFL
		shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_ADDRESS_DETAILS_FOR_CONSULTANT_RFL_4289, autoshipNumber),RFL_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		city = (String) getValueFromQueryResult(shippingAddressList, "City");
		state = (String) getValueFromQueryResult(shippingAddressList, "State");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingMethodId = String.valueOf(getValueFromQueryResult(shippingAddressList, "ShippingMethodID"));
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		System.out.println("Created ***"+shippingAddressFromDB);

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, autoshipNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		/*//verify shipping address in RFO
		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
			shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
	   firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
	   lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
	   addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "AddressLine1");
	   postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
	   locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
	   region = (String) getValueFromQueryResult(shippingAddressList, "Region");
	   country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
	   if(country.equals("236")){
	    country = "United States"; 
	   }
	   shippingAddressFromDB = firstName+" "+lastName+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n";
	   assertTrue("Shipping Address is not as expected",  storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));

			System.out.println("Shipping address in RFL");
		}*/
		//verify autoship item details in RFL
		DecimalFormat df = new DecimalFormat("#.00");
		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_CONSULTANT_RFL_4289, autoshipNumber),RFL_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "TaxAmountTotal")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "Total")));

		// Assert shipping Amount in RFL
		if(assertTrueDB("CRP Autoship Shipping value is NOT as in RFL DB",storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			//verify shipping amount in RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// Assert handling Amount with RFL
		if(assertTrueDB("CRP Autoship Handling value NOT as in RFL DB",storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			//verify handling amount in RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// assert Shipping Method with RFL
		shippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_FOR_CONSULTANT_RFL_4289, shippingMethodId),RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(shippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(shippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}
		shippingMethod = methodName+" "+"("+methodShortName+")";
		System.out.println("Method from DB "+shippingMethod);
		if(assertTrueDB("Shipping Method is NOT as in RFL DB", storeFrontOrdersPage.verifyShippingMethod(shippingMethod),RFL_DB)==false){
			// assert shipping method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
			methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
			assertTrue("Shipping Method is NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));
		}


		// assert Subtotal with RFL
		if(assertTrueDB("Subtotal is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB) == false){
			// assert subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}

		// assert Tax with RFL
		if(assertTrueDB("Tax is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateTax(taxDB), RFL_DB) == false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		}

		// assert Grand Total with RFL
		if(assertTrueDB("GrandTotal is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB), RFL_DB) == false){
			// assert Grand total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			assertTrue("Grand Total is not as expected", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));
		}

		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2 4287 -> Verify order details of consultant order
	@Test
	public void testOrdersDetailsOfConsultant_HP2_4287() throws SQLException, InterruptedException{
		RFL_DB = driver.getDBNameRFL();
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
		String payeeNameDB = null;
		String cardTypeDB = null;
		String consultantEmailID = null;     //"anneginsberg@hotmail.com";
		String lastName = null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_ACTIVE_CONSULTANT_USER_HAVING_ADHOC_ORDER_RFL_4287,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);


		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_ACTIVE_CONSULTANT_USER_HAVING_ADHOC_ORDER_RFL, orderHistoryNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "BillingFirstName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "TaxAmount")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "GrandTotal")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));
		String paymentTypeId =  String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "PayMentTypeID"));

		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		shippingMethodDB = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));

		DecimalFormat dff = new DecimalFormat("#.00");

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, orderHistoryNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		/*// assert shipping Address with RFL
	  if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
	  //verify shipping address in RFO
	   verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
     firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
     lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
     addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
     city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
     state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
     postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
     shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
     assertTrue(storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), "Shipping Address is not as expected");
	  }*/

		//Assert Subtotal with RFL
		if(assertTrueDB("SubTotal is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Shipping Amount is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Handling Value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}
		// Assert Tax with RFL
		if(assertTrueDB("Tax is NOT as in RFL DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Grand Total is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));
		}

		// assert for shipping Method with RFL
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}
		shippingMethodDB = methodName+" "+"("+methodShortName+")";

		if(assertTrueDB("Shipping Method is NOT as in RFL DB", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
			methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
			assertTrue("Shipping Method is NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));
		}

		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4291 :: Version : 1 :: Verify PC autoship order. 
	@Test
	public void testOrderDetailsForAutoshipOrdersForPC_4291() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO(); 

		String autoShipItemTotalPriceFromDB = null;
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
		String lastName = null;

		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String,Object>> verifyAllDetailsList = null;


		List<Map<String, Object>> randomPCList =DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		String pcEmailID = (String) getValueFromQueryResult(randomPCList, "Username");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_ACTIVE_PC_USER_HAVING_AUTOSHIP_ORDER_RFL, autoshipNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "BillingFirstName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "TaxAmount")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "GrandTotal")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));
		String paymentTypeId =  String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "PayMentTypeID"));


		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, autoshipNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		// assert shipping Address with RFL
		/*	if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), RFL_DB)== false){
					// Assert Shipping Address with RFo
			verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		   firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		   lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		   addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		   city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
		   state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
		   postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
		   shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		   assertTrue(storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), "Shipping Address is not as expected");
			}*/


		// Assert for PC Perks SubTotal with RFL
		if(assertTrueDB("PC Perks SubTotal value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB) == false){
			// assert for PC Perks SubTotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}


		// assert PC Perks Shipping value with RFL
		if(assertTrueDB("PC Perks Shipping value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB), RFL_DB)== false){
			// assert PC Perks Shipping value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// assert PC Handling value with RFL
		if(assertTrueDB("PC Perks Handling value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB), RFL_DB) == false){
			// assert PC Perks Handling value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// assert PC Tax Amount with RFL
		if(assertTrueDB("PC Perks Tax value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateTax(taxDB), RFL_DB)==false){
			// assert PC Tax Amount with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		}

		//assert PC GrandTotal Amount with RFL
		if(assertTrueDB("PC Perks Grand Total value NOT as in RFL DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB), RFL_DB)==false){
			// assert PC Grand Total Amount with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));
		}

		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}
		shippingMethodDB = methodName+" "+"("+methodShortName+")";

		if(assertTrueDB("Shipping Method is NOT as in RFL DB", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
			methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
			assertTrue("Shipping Method is NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));
		}

		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4292 :: Version : 1 :: Verify order details of Consultant Order (i.e. Adhoc Order)
	@Test
	public void testOrderDetailsForAdhocOrdersForPC_4292() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
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
		String consultantEmailID = null;
		String lastName = null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_ACTIVE_PC_USER_WITH_ADHOC_ORDER_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");


		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksOrderPageHeader(),"Order Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ADDRESS_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format((getValueFromQueryResult(verifyOrderDetailsList, "Subtotal"))));
		shippingDB =  String.valueOf(df.format((getValueFromQueryResult(verifyOrderDetailsList, "ShippingAmount"))));
		handlingDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "HandlingAmount")));
		taxDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount")));
		grandTotalDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "Total")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, orderHistoryNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));


		/*// assert shipping Address with rfl
		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
		//verify shipping address in RFO
		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
     firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
     lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
     addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
     city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
     state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
     postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
     shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
     assertTrue(storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), "Shipping Address is not as expected");		}*/

		//Assert Subtotal with RFL
		if(assertTrueDB("SubTotal is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Shipping Amount is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Handling Value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// Assert Tax with RFL
		if(assertTrueDB("Tax is NOT as in RFL DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Grand Total is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));
		}

		// assert for shipping Method with RFL
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}
		shippingMethodDB = methodName+" "+"("+methodShortName+")";
		System.out.println("Method from DB "+shippingMethodDB);
		if(assertTrueDB("Shipping Method is NOT as in RFL DB", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert shipping method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
			methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
			assertTrue("Shipping Method is NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));
		}		

		logout();
		s_assert.assertAll();

	}


	//Hybris Phase 2-4293 :: Version : 1 :: Verify details of retail order. 
	@Test
	public void testOrderDetailsForAdhocOrdersForRC() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
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
		String rcUser = null;

		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;

		/*randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_ACTIVE_RC_USER_WITH_ADHOC_ORDER_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");*/
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_ACTIVE_RC_USER_WITH_ADHOC_ORDER_RFL,RFL_DB);
		rcUser = (String) getValueFromQueryResult(randomRCList, "UserName");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUser, TestConstants.CONSULTANT_PASSWORD_TST4);
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ADDRESS_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal"))));
		shippingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "ShippingAmount"))));
		handlingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "HandlingAmount"))));
		taxDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount"))));
		grandTotalDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Total"))));
		String shippingMethodId = String.valueOf((Number)getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, orderHistoryNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		/*// assert shipping Address with rfl
		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
		//verify shipping address in RFO
		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
     firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
     lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
     addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
     city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
     state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
     postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
     shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
     assertTrue(storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), "Shipping Address is not as expected");
		}*/

		//Assert Subtotal with RFL
		if(assertTrueDB("SubTotal is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Shipping Amount is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Handling Value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// Assert Tax with RFL
		if(assertTrueDB("Tax is NOT as in RFL DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Grand Total is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));
		}

		// assert for shipping Method with RFL
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}
		shippingMethodDB = methodName+" "+"("+methodShortName+")";
		System.out.println("Method from DB "+shippingMethodDB);
		if(assertTrueDB("Shipping Method is NOT as in RFL DB", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert shipping method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
			methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
			assertTrue("Shipping Method is NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));
		}

		logout();
		s_assert.assertAll();
	}

	//Hybris Project-4294:Verify details of failed consultant autoship orders.
	@Test
	public void testDetailsOfFailedConsultantAutoshipOrder() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
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
		String consultantEmailID = null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String, Object>> getOrderIdDetailsList = null;
		List<Map<String, Object>> getOrderNumberDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_ACTIVE_CONSULTANT_HAVING_FAILED_CRP_ORDER_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String failedOrderNumber = storeFrontOrdersPage.getOrderNumberFromOrderHistoryForFailedAutoshipOrdersForConsultant();

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ADDRESS_DETAILS_FOR_RFL, failedOrderNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		System.out.println("Shipping address from Created **"+shippingAddressFromDB+"**");
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_RFL, failedOrderNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal"))));
		shippingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "ShippingAmount"))));
		handlingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "HandlingAmount"))));
		taxDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount"))));
		grandTotalDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Total"))));
		String shippingMethodId = String.valueOf((Number)getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, failedOrderNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		/*// assert shipping Address with RFL
				if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
				//verify shipping address in RFO
			verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
     firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
     lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
     addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
     city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
     state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
     postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
     shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
     assertTrue(storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), "Shipping Address is not as expected");
				}*/

		//Assert Subtotal with RFL
		if(assertTrueDB("SubTotal is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Shipping Amount NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Handling Value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// Assert Tax with RFL
		if(assertTrueDB("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Grand Total is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));
		}

		// assert for shipping Method with RFL
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}

		shippingMethodDB = methodName+" "+"("+methodShortName+")";
		System.out.println("Method from DB "+shippingMethodDB);
		if(assertTrueDB("Shipping Method is NOT as in RFL DB", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
			methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
			if(methodName.contains("FedEx Grnd")){
				methodName = "FedEx Ground (HD)";
			}
			assertTrue("Shipping Method is NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));
		}

		logout();
		s_assert.assertAll();

	}

	//Hybris Project-4295:Verify details of failed pc autoship orders.
	@Test
	public void testDetilsOfFailedAutoshipOrdersForPC() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
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

		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String, Object>> getOrderIdDetailsList = null;
		List<Map<String, Object>> getOrderNumberDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		String PCEmailID = null;

		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_PC_USER_FOR_FAILED_AUTOSHIP_ORDER, accountIdForPCFailedOrder),RFL_DB);
		PCEmailID = String.valueOf(getValueFromQueryResult(randomPCList, "Username"));

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(PCEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String failedOrderNumber = storeFrontOrdersPage.getOrderNumberFromOrderHistoryForFailedAutoshipOrdersForPC();				

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ADDRESS_DETAILS_FOR_RFL, failedOrderNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		System.out.println("Shipping address from Created **"+shippingAddressFromDB+"**");
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_RFL, failedOrderNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal"))));
		shippingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "ShippingAmount"))));
		handlingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "HandlingAmount"))));
		taxDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount"))));
		grandTotalDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Total"))));
		String shippingMethodId = String.valueOf((Number)getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, failedOrderNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));	

		/*// assert shipping Address with RFL
				if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
				//verify shipping address in RFO
			verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
     firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
     lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
     addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
     city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
     state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
     postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
     shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
     assertTrue(storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), "Shipping Address is not as expected");
				}*/

		//Assert Subtotal with RFL
		if(assertTrueDB("SubTotal is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Shipping Amount is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Handling Value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// Assert Tax with RFL
		if(assertTrueDB("Tax is NOT as in RFL DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Grand Total is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));
		}

		// assert for shipping Method with RFL
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}

		shippingMethodDB = methodName+" "+"("+methodShortName+")";
		System.out.println("Method from DB "+shippingMethodDB);
		if(assertTrueDB("Shipping Method is NOT as in RFL DB", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
			methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
			String methodNameRFO = null;;
			if(methodName.contains("FedEx Grnd")){
				methodNameRFO = "FedEx Ground (HD)";
			}
			assertTrue("Shipping Method is NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodNameRFO));
		}

		logout();
		s_assert.assertAll();

	}

	//Hybris Project-4296::Verify details of failed retail orders.
	@Test(enabled=false) // QUERY giving anonymous result with no username
	public void testDetailsOfFailedAdhocOrdersForRC() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
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

		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String, Object>> getOrderIdDetailsList = null;
		List<Map<String, Object>> getOrderNumberDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ACTIVE_RC_USER_HAVING_FAILED_ORDERS, accountIdForPCFailedOrder),RFL_DB);
		RCEmailID = String.valueOf(getValueFromQueryResult(randomRCList, "Username"));

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(RCEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String failedOrderNumber = storeFrontOrdersPage.getOrderNumberFromOrderHistoryForFailedAdhocOrdersForRC();				

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ADDRESS_DETAILS_FOR_RFL, failedOrderNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		System.out.println("Shipping address from Created **"+shippingAddressFromDB+"**");
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_FAILED_ORDER_FOR_RC_RFL_, failedOrderNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal")));
		shippingDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "ShippingTotal")));
		handlingDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "HandlingTotal")));
		taxDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount")));
		grandTotalDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "GrandTotal")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, failedOrderNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		/*// assert shipping Address with RFL
				if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
				//verify shipping address in RFO
	verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
     firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
     lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
     addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
     city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
     state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
     postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
     shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
     assertTrue(storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), "Shipping Address is not as expected");
				}*/

		//Assert Subtotal with RFL
		if(assertTrueDB("SubTotal is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Shipping Amount is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Handling Value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// Assert Tax with RFL
		if(assertTrueDB("Tax is NOT as in RFL DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Grand Total is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));
		}

		// assert for shipping Method with RFL
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}

		shippingMethodDB = methodName+" "+"("+methodShortName+")";
		System.out.println("Method from DB "+shippingMethodDB);
		if(assertTrueDB("Shipping Method is NOT as in RFL DB", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
			methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
			if(methodName.contains("FedEx Grnd")){
				methodName = "FedEx Ground (HD)";
			}
			assertTrue("Shipping Method NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));
		}
		logout();
		s_assert.assertAll();

	}
}
