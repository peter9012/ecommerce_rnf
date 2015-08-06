package com.rf.test.website.storeFront.order;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class ViewOrderDetailsTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(ViewOrderDetailsTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
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
		String shippingMethodDB = null;
		String consultantEmailID = null;
		String lastName = null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_ACTIVE_CONSULTANT_USER_WITH_ACTIVE_CRP_AUTOSHIP_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number for assert
		String orderHistoryNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		DecimalFormat df = new DecimalFormat("#.00");
		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DETAILS_ACTIVE_CONSULTANT_USER_HAVING_AUTOSHIP_ORDER_RFL, orderHistoryNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName =  (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = df.format((Number)(getValueFromQueryResult(verifyAllDetailsList, "CountryID")));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";

		subTotalDB = df.format((Number)(getValueFromQueryResult(verifyAllDetailsList, "Subtotal")));
		shippingDB = df.format((Number)(getValueFromQueryResult(verifyAllDetailsList, "ShippingAmount")));
		handlingDB = df.format((Number)(getValueFromQueryResult(verifyAllDetailsList, "HandlingAmount")));
		taxDB = df.format((Number)(getValueFromQueryResult(verifyAllDetailsList, "TaxAmount")));
		grandTotalDB = df.format((Number)(getValueFromQueryResult(verifyAllDetailsList, "GrandTotal")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		shippingMethodDB = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));

		DecimalFormat dff = new DecimalFormat("#.00");

		// assert shipping Address with RFL
//		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
//			//verify shipping address in RFO
//			// waiting for RFO queries
//		}

		//Assert Subtotal with RFL
		if(assertTrueDB("SubTotal is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			// waiting for RFO Queries
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Shipping Amount is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			// Waiting for RFO Queries
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Handling Value is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			// Waiting for RFO Queries
		}

		// Assert Tax with RFL
		if(assertTrueDB("Tax is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateTax(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			// Waiting for RFO Queries
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Grand Total is not as expected", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			// Waiting for RFO Queries
		}

		// assert for shipping Method with RFL
		if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert Grand Total with RFO
			// Waiting for RFO Queries
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

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyCardTypeList = null;

		/*randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_ACTIVE_CONSULTANT_USER_HAVING_ADHOC_ORDER_RFL_4287,RFL_DB);
	  consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");
		 */

		String consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A hard code user
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();


		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DETAILS_ACTIVE_CONSULTANT_USER_HAVING_ADHOC_ORDER_RFL, orderHistoryNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "BillingFirstName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		phoneNumber = (String) getValueFromQueryResult(verifyAllDetailsList, "PhoneNumber");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		subTotalDB = String.valueOf((getValueFromQueryResult(verifyAllDetailsList, "Subtotal")));
		shippingDB = String.valueOf((getValueFromQueryResult(verifyAllDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf((getValueFromQueryResult(verifyAllDetailsList, "HandlingAmount")));
		taxDB = String.valueOf((getValueFromQueryResult(verifyAllDetailsList, "TaxAmount")));
		grandTotalDB = String.valueOf((getValueFromQueryResult(verifyAllDetailsList, "GrandTotal")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));
		String paymentTypeId =  String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "PayMentTypeID"));

		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		shippingMethodDB = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));


		DecimalFormat dff = new DecimalFormat("#.00");

		// assert shipping Address with rfl
		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
			//verify shipping address in RFO
			// waiting for RFO queries
		}

		//Assert Subtotal with RFL
		if(assertTrueDB("SubTotal is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			// waiting for RFO Queries
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Shipping Amount is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			// Waiting for RFO Queries
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Handling Value is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			// Waiting for RFO Queries
		}

		// Assert Tax with RFL
		if(assertTrueDB("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			// Waiting for RFO Queries
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Grand Total is not as expected", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			// Waiting for RFO Queries
		}

		// assert for shipping Method with RFL
		if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert Grand Total with RFO
			// Waiting for RFO Queries
		}

		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4291 :: Version : 1 :: Verify PC autoship order. 
	@Test
	public void testOrderDetailsForAutoshipOrdersForPC_4291() throws InterruptedException{
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

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_ACTIVE_PC_USER_WITH_AUTOSHIP_ORDER_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String autoshipOrderNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksAutoShipHeader(),"Order Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ADDRESS_DETAILS_FOR_RFL, autoshipOrderNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		phoneNumber = (String) getValueFromQueryResult(verifyAllDetailsList, "PhoneNumber");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DETAILS_FOR_RFL, autoshipOrderNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal"))));
		shippingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "ShippingAmount"))));
		handlingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "HandlingAmount"))));
		taxDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount"))));
		grandTotalDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Total"))));
		String shippingMethodId = String.valueOf((Number)getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));


		// assert shipping Address with RFL
		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
			//verify shipping address in RFO
			// waiting for RFO queries
		}

		//Assert Subtotal with RFL
		if(assertTrueDB("SubTotal is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			// waiting for RFO Queries
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Shipping Amount is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			// Waiting for RFO Queries
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Handling Value is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			// Waiting for RFO Queries
		}

		// Assert Tax with RFL
		if(assertTrueDB("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			// Waiting for RFO Queries
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Grand Total is not as expected", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			// Waiting for RFO Queries
		}

		// assert for shipping Method with RFL
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}

		shippingMethodDB = methodName+" "+"("+methodShortName+")";
		System.out.println("Method from DB "+shippingMethodDB);
		if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			// Waiting for RFO Queries
		}

		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4292 :: Version : 1 :: Verify order details of Consultant Order (i.e. Adhoc Order)
	@Test
	public void testOrderDetailsForAdhocOrdersForConsultant_4292() throws InterruptedException{
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

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_ACTIVE_PC_USER_WITH_ADHOC_ORDER_RFL,RFL_DB);
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
		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksAutoShipHeader(),"Order Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ADDRESS_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		phoneNumber = (String) getValueFromQueryResult(verifyAllDetailsList, "PhoneNumber");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal"))));
		shippingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "ShippingAmount"))));
		handlingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "HandlingAmount"))));
		taxDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount"))));
		grandTotalDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Total"))));
		String shippingMethodId = String.valueOf((Number)getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));
		//String paymentTypeId =  String.valueOf(getValueFromQueryResult(verifyOrderDetailsList, "PayMentTypeID"));




		/*// assert shipping Address with rfl
	   if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
	   //verify shipping address in RFO
	   // waiting for RFO queries
	   }*/

		//Assert Subtotal with RFL
		if(assertTrueDB("SubTotal is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			// waiting for RFO Queries
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Shipping Amount is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			// Waiting for RFO Queries
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Handling Value is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			// Waiting for RFO Queries
		}

		// Assert Tax with RFL
		if(assertTrueDB("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			// Waiting for RFO Queries
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Grand Total is not as expected", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			// Waiting for RFO Queries
		}

		// assert for shipping Method with RFL
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}
		shippingMethodDB = methodName+" "+"("+methodShortName+")";
		System.out.println("Method from DB "+shippingMethodDB);
		if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert Grand Total with RFO
			// Waiting for RFO Queries
		}
		logout();
		s_assert.assertAll();
	}

	//Hybris Phase 2-4293 :: Version : 1 :: Verify details of retail order. 
	@Test
	public void testOrderDetailsForAdhocOrdersForRC_4293() throws InterruptedException{
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
		String phoneNumber = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String payeeNameDB = null;
		String cardTypeDB = null;
		String rcEmailID = "Arlene.vaccaro@yahoo.com";
		String lastName = null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;

		/*randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_ACTIVE_RC_USER_WITH_ADHOC_ORDER_RFL,RFL_DB);
	   consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");*/

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(rcEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ADDRESS_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		phoneNumber = (String) getValueFromQueryResult(verifyAllDetailsList, "PhoneNumber");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal"))));
		shippingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "ShippingAmount"))));
		handlingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "HandlingAmount"))));
		taxDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount"))));
		grandTotalDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Total"))));
		String shippingMethodId = String.valueOf((Number)getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		/*// assert shipping Address with rfl
	   if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
	   //verify shipping address in RFO
	   // waiting for RFO queries
	   }*/

		//Assert Subtotal with RFL
		if(assertTrueDB("SubTotal is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			// waiting for RFO Queries
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Shipping Amount is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			// Waiting for RFO Queries
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Handling Value is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			// Waiting for RFO Queries
		}

		// Assert Tax with RFL
		if(assertTrueDB("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			// Waiting for RFO Queries
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Grand Total is not as expected", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			// Waiting for RFO Queries
		}

		// assert for shipping Method with RFL
		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}
		shippingMethodDB = methodName+" "+"("+methodShortName+")";
		System.out.println("Method from DB "+shippingMethodDB);
		if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert Grand Total with RFO
			// Waiting for RFO Queries
		}

		logout();
		s_assert.assertAll();
	}

}
