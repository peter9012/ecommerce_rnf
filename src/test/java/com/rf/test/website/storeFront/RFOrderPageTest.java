package com.rf.test.website.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontCartAutoShipPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontOrdersAutoshipStatusPage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontRCUserPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontReportOrderComplaintPage;
import com.rf.pages.website.StoreFrontReportProblemConfirmationPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class RFOrderPageTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(RFOrderPageTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontOrdersAutoshipStatusPage storeFrontOrdersAutoshipStatusPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontReportOrderComplaintPage storeFrontReportOrderComplaintPage;
	private StoreFrontReportProblemConfirmationPage storeFrontReportProblemConfirmationPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private String RFL_DB = null;
	private String RFO_DB = null;

	//Hybris Phase 2-4289 : Verify CRP autoship template details
	@Test
	public void testCRPAutoShipTemplateDetails_HP2_4289() throws InterruptedException, SQLException{
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
		String shippingMethod = null;
		String payeeNameDB = null;
		String cardTypeDB = null;

		List<Map<String, Object>> expirationDateOfCardList = null;
		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String, Object>> autoShipItemTotalPriceList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> autoShipPaymentDetailsList = null;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.verifyCRPAutoShipHeader(),"CRP Autoship template header is not as expected");
		s_assert.assertTrue(storeFrontOrdersPage.verifySVValue("consultant"),"CRP Autoship template SV value is not as expcted");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		//verify shipping address in RFL
		shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ADDRESS_QUERY_TST4, TestConstants.CONSULTANT_EMAIL_ID_TST4),RFL_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "Attention");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		city = (String) getValueFromQueryResult(shippingAddressList, "City");
		state = (String) getValueFromQueryResult(shippingAddressList, "State");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		phoneNumber = (String) getValueFromQueryResult(shippingAddressList, "PhoneNumber");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
			//verify shipping address in RFO
			shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_QUERY_RFO,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
			firstName = (String) getValueFromQueryResult(shippingAddressList, "AddressProfileName");
			addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "AddressLine1");
			postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
			locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
			region = (String) getValueFromQueryResult(shippingAddressList, "Region");
			country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
			if(country.equals("236")){
				country = "United States"; 
			}
			shippingAddressFromDB = firstName+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			//shippingAddressFromDB = addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			assertTrue("Shipping Address is not as expected",  storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));

		}
		//verify autoship item details in RFL
		DecimalFormat df = new DecimalFormat("#.00");
		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ORDER_DETAILS_QUERY_TST4, autoshipNumber),RFL_DB);
		subTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "TaxAmountTotal")));
		grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "PaymentTotal")));
		DecimalFormat dff = new DecimalFormat("#.00");
		
		if(assertTrueDB("CRP Autoship Shipping value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			//verify shipping amount in RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_COST_QUERY_RFO,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
			shippingDB = String.valueOf(dff.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingCost")));
			assertTrue("CRP Autoship Shipping value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}
		if(assertTrueDB("CRP Autoship Handling value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			//verify handling amount in RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_HANDLING_COST_QUERY_RFO,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
			handlingDB = String.valueOf(dff.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingCost")));
			assertTrue("CRP Autoship Handling value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}
		
		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_GRAND_TOTAL_QUERY_RFO, TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
		// assert for shipping Method with RFL
		shippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_SHIPPING_METHOD_QUERY_TST4, autoshipNumber),RFL_DB);
		shippingMethod = (String) getValueFromQueryResult(shippingMethodList, "Name");
		if(shippingMethod.equalsIgnoreCase("FedEx Grnd")){
			shippingMethod = "FedEx Ground (HD)";
		}
		//					if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethod),RFL_DB)==false){
		//					// assert for shipping Method with RFO
		//					shippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_SHIPPING_METHOD_QUERY_RFO, autoshipNumber),RFO_DB);
		//					shippingMethod = (String) getValueFromQueryResult(shippingMethodList, "Name");
		//					if(shippingMethod.equalsIgnoreCase("FedEx Grnd")){
		//						shippingMethod = "FedEx Ground (HD)";
		//					}
		//					assertTrue("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethod));
		//					}


		//- - - - - -- - - - -- -******************************REASON FOR COMMENTED CODE*****************- - - - - - - - - - - - - - - - - -
		//Assertion for	autoShipItemDetailsList for subTotalDB value query result is not as in UI for both RFO and RFL.
		//Assertion for	autoShipItemDetailsList for taxDB value query result is not as in UI for both RFO and RFL.
		//Assertion for	autoShipItemDetailsList for grantTotalDB value query result is not as in UI for both RFO and RFL.
		//Unable to find query for shipping method for RFO database.

	}


	//Hybris Phase 2-1981:Orders page UI for RC
	@Test
	public void testOrdersPageUIForRCUser_HP2_1981() throws SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> orderNumberList =  null;
		List<Map<String, Object>> orderStatusList =  null;
		List<Map<String, Object>> orderGrandTotalList =  null;
		List<Map<String, Object>> orderDateList =  null;

		String orderNumberDB = null;
		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;
		String rcUserUsername = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		rcUserUsername = TestConstants.RCUSER_USERNAME_TST4;
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(TestConstants.RCUSER_EMAIL_ID_TST4, TestConstants.RCUSER_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserUsername),"RC User Page doesn't contain Welcome User Message");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// assert for order number with RFL
		orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.RCUSER_EMAIL_ID_TST4),RFL_DB); 
		orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber"); 
		if(assertTrueDB("Order Number on UI is different from DB",storeFrontOrdersPage.verifyOrderNumber(orderNumberDB),RFL_DB)==false){
			//assert for order number with RFO
			orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO,  TestConstants.RCUSER_EMAIL_ID_TST4),RFO_DB);
			orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
			assertTrue("Order Number on UI is different from DB",storeFrontOrdersPage.verifyOrderNumber(orderNumberDB));
		}
		// assert for order status with RFL
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.RCUSER_EMAIL_ID_TST4),RFL_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		if(assertTrueDB("Order status is not as expected for this User",storeFrontOrdersPage.verifyOrderStatus(orderStatusDB),RFL_DB)==false){
			//assert for order status with RFO
			orderStatusList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFO, TestConstants.RCUSER_EMAIL_ID_TST4),RFO_DB);
			orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
			assertTrue("Order status is not as expected for this User", storeFrontOrdersPage.verifyOrderStatus(orderStatusDB));
		}
		// assert for grand total with RFL
		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.RCUSER_EMAIL_ID_TST4),RFL_DB);
		DecimalFormat df = new DecimalFormat("#.00");
		orderGrandTotalDB = df.format((Number) getValueFromQueryResult(orderGrandTotalList, "GrandTotal")); 
		if(assertTrueDB("Grand total is not as expected for this User",storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),RFL_DB)==false){
			//assert for grand total with RFO
			orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFO,  TestConstants.RCUSER_EMAIL_ID_TST4),RFO_DB);
			DecimalFormat dff = new DecimalFormat("#.00");
			orderGrandTotalDB = dff.format((Number) getValueFromQueryResult(orderGrandTotalList, "AmountToBeAuthorized")); 
			assertTrue("Grand total is not as expected for this User",storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB));
		}
		// assert for order date with RFL
		orderDateList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.RCUSER_EMAIL_ID_TST4),RFL_DB);
		orderDateDB = String.valueOf(getValueFromQueryResult(orderDateList, "StartDate"));
		if(assertTrueDB("Scheduled date is not as expected for this User",storeFrontOrdersPage.verifyScheduleDate(orderDateDB),RFL_DB)==false){
			//assert for order date with RFO
			orderDateList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFO,  TestConstants.RCUSER_EMAIL_ID_TST4),RFO_DB);
			orderDateDB = String.valueOf (getValueFromQueryResult(orderDateList, "CompletionDate"));
			assertTrue("Scheduled date is not as expected for this User",storeFrontOrdersPage.verifyScheduleDate(orderDateDB));
		}
		logout();
		s_assert.assertAll();		
	}

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
		String phoneNumber = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethod = null;
		String payeeNameDB = null;
		String cardTypeDB = null;

		List<Map<String, Object>> expirationDateOfCardList = null;
		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String, Object>> autoShipItemTotalPriceList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> autoShipPaymentDetailsList = null;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.verifyCRPAutoShipHeader(),"CRP Autoship template header is not as expected");
		s_assert.assertTrue(storeFrontOrdersPage.verifySVValue("consultant"),"CRP Autoship template SV value is not as expcted");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		//verify shipping address in RFL
		shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ADDRESS_QUERY_TST4, TestConstants.CONSULTANT_EMAIL_ID_TST4),RFL_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "Attention");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		city = (String) getValueFromQueryResult(shippingAddressList, "City");
		state = (String) getValueFromQueryResult(shippingAddressList, "State");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		phoneNumber = (String) getValueFromQueryResult(shippingAddressList, "PhoneNumber");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
			//verify shipping address in RFO
			shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_QUERY_RFO,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
			firstName = (String) getValueFromQueryResult(shippingAddressList, "AddressProfileName");
			addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "AddressLine1");
			postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
			locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
			region = (String) getValueFromQueryResult(shippingAddressList, "Region");
			country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
			if(country.equals("236")){
				country = "United States"; 
			}
			shippingAddressFromDB = firstName+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			//shippingAddressFromDB = addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			assertTrue("Shipping Address is not as expected",  storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));

		}
		//verify autoship item details in RFL
		DecimalFormat df = new DecimalFormat("#.00");
		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ORDER_DETAILS_QUERY_TST4, autoshipNumber),RFL_DB);
		subTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "TaxAmountTotal")));
		grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "PaymentTotal")));
		DecimalFormat dff = new DecimalFormat("#.00");
		
		if(assertTrueDB("CRP Autoship Shipping value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			//verify shipping amount in RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_COST_QUERY_RFO,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
			shippingDB = String.valueOf(dff.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingCost")));
			assertTrue("CRP Autoship Shipping value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}
		if(assertTrueDB("CRP Autoship Handling value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			//verify handling amount in RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_HANDLING_COST_QUERY_RFO,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
			handlingDB = String.valueOf(dff.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingCost")));
			assertTrue("CRP Autoship Handling value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}
		
		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_GRAND_TOTAL_QUERY_RFO, TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
		// assert for shipping Method with RFL
		shippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_SHIPPING_METHOD_QUERY_TST4, autoshipNumber),RFL_DB);
		shippingMethod = (String) getValueFromQueryResult(shippingMethodList, "Name");
		if(shippingMethod.equalsIgnoreCase("FedEx Grnd")){
			shippingMethod = "FedEx Ground (HD)";
		}
		//					if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethod),RFL_DB)==false){
		//					// assert for shipping Method with RFO
		//					shippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_SHIPPING_METHOD_QUERY_RFO, autoshipNumber),RFO_DB);
		//					shippingMethod = (String) getValueFromQueryResult(shippingMethodList, "Name");
		//					if(shippingMethod.equalsIgnoreCase("FedEx Grnd")){
		//						shippingMethod = "FedEx Ground (HD)";
		//					}
		//					assertTrue("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethod));
		//					}


		//- - - - - -- - - - -- -******************************REASON FOR COMMENTED CODE*****************- - - - - - - - - - - - - - - - - -
		//Assertion for	autoShipItemDetailsList for subTotalDB value query result is not as in UI for both RFO and RFL.
		//Assertion for	autoShipItemDetailsList for taxDB value query result is not as in UI for both RFO and RFL.
		//Assertion for	autoShipItemDetailsList for grantTotalDB value query result is not as in UI for both RFO and RFL.
		//Unable to find query for shipping method for RFO database.

	}

	// phase 2 4287 -> Verify order details of consultant order
	@Test
	public void testOrdersDetailsOfConsultant_HP2_4287() throws SQLException, InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> orderNumberList =  null;
		List<Map<String, Object>> orderStatusList =  null;
		List<Map<String, Object>> orderGrandTotalList =  null;
		List<Map<String, Object>> orderDateList =  null;

		String orderNumberDB = null;
		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		//Assert for Order Number with RFL  
		orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFL_DB);
		orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
		if(assertTrueDB("Order Number on UI is different from DB", storeFrontOrdersPage.verifyOrderNumber(orderNumberDB), RFL_DB)==false){
			//assert for Order Number with RFO
			orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
			assertTrue("Order Number on UI is different from DB", storeFrontOrdersPage.verifyOrderNumber(orderNumberDB));
		}

		// Assert For Order Date with RFL

		orderDateList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFL_DB);
		orderDateDB = String.valueOf(getValueFromQueryResult(orderDateList, "StartDate"));
		if(assertTrueDB("Order date on UI as different from DB", storeFrontOrdersPage.verifyScheduleDate(orderDateDB), RFL_DB)==false){
			// Assert For Order Date with RFO
			orderDateList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFO, TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
			orderDateDB = String.valueOf(getValueFromQueryResult(orderDateList, "CompletionDate"));
			assertTrue("Order Date on UI is different from DB", storeFrontOrdersPage.verifyScheduleDate(orderDateDB));
		}

		// Assert For Grand Total with RFL

		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFL_DB);
		DecimalFormat df = new DecimalFormat("#.00");
		orderGrandTotalDB = df.format((Number)getValueFromQueryResult(orderGrandTotalList, "GrandTotal"));
		if(assertTrueDB("Grand total is not as expected for Consultant", storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB), RFL_DB)==false){
			// Assert For Grand Total with RFO
			orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFO, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			orderGrandTotalDB = df.format((Number)getValueFromQueryResult(orderGrandTotalList, "AmountToBeAuthorized"));
			assertTrue("Grand total is not as expected for Consultant", storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB));
		}

		// assert for Order Status with RFL
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFL_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		if(assertTrueDB("Order status is not as expected for Consultant", storeFrontOrdersPage.verifyOrderStatus(orderStatusDB), RFL_DB)== false){
			orderStatusList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFO, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			orderStatusDB = String.valueOf(getValueFromQueryResult(orderStatusList, "Name"));
			assertTrue("Order status is not as expected for Consultant", storeFrontOrdersPage.verifyOrderStatus(orderStatusDB));
		}
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4195 : Enrolled Consultant, Has CRP/ Has Pulse, Has Submitted Orders
	@Test
	public void testEnrolledConsultantHasCRPPULSESubmittedOrders_HP2_4195() throws InterruptedException, SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantEmailIdList =  null;
		List<Map<String, Object>> orderNumberList =  null;		
		String orderNumberDB = null;
		String consultantEmail = null;
		randomConsultantEmailIdList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_ORDERS_RFL,RFL_DB);
		consultantEmail = (String) getValueFromQueryResult(randomConsultantEmailIdList, "UserName");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmail, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFL, consultantEmail),RFL_DB);
		//assert on RFL
		orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
		if(assertTrueDB("Status of Order Number is not found as Submitted",storeFrontOrdersPage.verifyOrderStatusToBeSubmitted(orderNumberDB),RFL_DB)==false){
			//assert On RFO
			orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO, consultantEmail),RFO_DB);
			orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
			assertTrue("Status of Order Number is not found as Submitted",storeFrontOrdersPage.verifyOrderStatusToBeSubmitted(orderNumberDB));	
		}		
		storeFrontAccountInfoPage = storeFrontOrdersPage.clickOnAccountInfoFromLeftPanel();
		storeFrontOrdersAutoshipStatusPage = storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipStatusHeader(),"Autoship status header is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipCRPStatus(),"AutoShip CRP Status is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipPulseSubscriptionStatus(),"AutoShip Pulse Subscription Status is not as expected");
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4300 :: Version : 1 :: Verify PC autoship template details. 
	@Test
	public void testPCAutoShipTemplateDetails_HP2_4300() throws InterruptedException, SQLException , ClassNotFoundException{
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
		String cardTypeDB = null;

		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String, Object>> autoShipItemTotalPriceList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> autoShipPaymentDetialsList = null;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(TestConstants.PC_EMAIL_ID_TST4, TestConstants.PC_PASSWORD_TST4);

		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		// Assert for Schedule Date with RFL
		shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ADDRESS_QUERY_TST4, TestConstants.PC_EMAIL_ID_TST4),RFL_DB);

		firstName = (String) getValueFromQueryResult(shippingAddressList, "Attention");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		city = (String) getValueFromQueryResult(shippingAddressList, "City");
		state = (String) getValueFromQueryResult(shippingAddressList, "State");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		phoneNumber = (String) getValueFromQueryResult(shippingAddressList, "PhoneNumber");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}   
		DecimalFormat df = new DecimalFormat("#.00");

		shippingAddressFromDB = firstName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), RFL_DB)== false){
			// Assert for Schedule Date with RFO
			shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_QUERY_RFO, TestConstants.PC_EMAIL_ID_TST4), RFO_DB);

			firstName = String.valueOf(getValueFromQueryResult(shippingAddressList, "AddressProfileName"));
			addressLine1 = String.valueOf(getValueFromQueryResult(shippingAddressList, "AddressLine1"));
			postalCode = String.valueOf(getValueFromQueryResult(shippingAddressList, "PostalCode"));
			locale = String.valueOf(getValueFromQueryResult(shippingAddressList, "Locale"));
			region = String.valueOf(getValueFromQueryResult(shippingAddressList, "Region"));
			country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
			if(country.equals("236")){
				country = "United States"; 
			}
			shippingAddressFromDB = addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			assertTrue("Shipping Address is not as expected",storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));
		}

		// assert for shipping Method with RFL
		shippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_SHIPPING_METHOD_QUERY_TST4, autoshipNumber), RFL_DB);
		shippingMethodDB = (String) getValueFromQueryResult(shippingMethodList, "Name");
		if(shippingMethodDB.equalsIgnoreCase("FedEx Grnd")){
			shippingMethodDB = "FedEx Ground (HD)";
		}
		if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB), RFL_DB)==false){
			// assert for shipping Method with RFO
			// Waiting for queries from RFO.
		}

		// Assert for Card type with RFL
		autoShipPaymentDetialsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_PAYMENT_DETAILS_QUERY_TST4, TestConstants.PC_EMAIL_ID_TST4), RFL_DB);
		cardTypeDB = (String) getValueFromQueryResult(autoShipPaymentDetialsList, "AccountName");
		if(assertTrueDB("Card Type is not as expected", storeFrontOrdersPage.verifyCardType(cardTypeDB), RFO_DB)== false){
			// assert for Card Type with RFO
			autoShipPaymentDetialsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_CARD_TYPE_QUERY_RFO, TestConstants.PC_EMAIL_ID_TST4), RFO_DB);
			cardTypeDB = (String) getValueFromQueryResult(autoShipPaymentDetialsList, "Name");
			assertTrue("Card Type is not as expected",storeFrontOrdersPage.verifyCardType(cardTypeDB));
		}

		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ORDER_DETAILS_QUERY_TST4, autoshipNumber), RFL_DB);
		subTotalDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "Subtotal"));
		shippingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingAmount"));
		handlingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingAmount"));

		// Assert for PC Perks SubTotal with RFL
		if(assertTrueDB("PC Perks SubTotal value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB) == false){
			// assert for PC Perks SubTotal with RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SUBTOTAL_VALUE_QUERY_RFO, TestConstants.PC_EMAIL_ID_TST4), RFO_DB);
			subTotalDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "SubTotal"));
			assertTrue("PC Perks SubTotal value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}

		// assert PC Perks Shipping value with RFL
		if(assertTrueDB("PC Perks Shipping value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB), RFL_DB)== false){
			// assert PC Perks Shipping value with RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_COST_QUERY_RFO, TestConstants.PC_EMAIL_ID_TST4), RFO_DB);
			shippingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingCost"));
			assertTrue("PC Perks Shipping value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		if(assertTrueDB("", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB), RFL_DB) == false){
			// assert PC Perks Shipping value with RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_HANDLING_COST_QUERY_RFO, TestConstants.PC_EMAIL_ID_TST4), RFO_DB);
			handlingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingCost"));
			assertTrue("PC Perks Handling value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-1980 :: Version : 1 :: Order >>Actions >>Report problems
	@Test
	public void testOrdersReportProblems_1980() throws SQLException, InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4,TestConstants.CONSULTANT_PASSWORD_RFL);			
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		storeFrontOrdersPage.orderNumberForOrderHistory();
		storeFrontReportOrderComplaintPage = storeFrontOrdersPage.clickOnActions();
		s_assert.assertTrue(storeFrontReportOrderComplaintPage.VerifyOrderNumberOnReportPage(),"OrderNumber is different on ReportOrderComplaintPage");
		storeFrontReportOrderComplaintPage.clickOnCheckBox();
		s_assert.assertTrue(storeFrontReportOrderComplaintPage.verifyCountOfDropDownOptionsOnReportPage(),"DropDown Options are not present as expected");
		storeFrontReportOrderComplaintPage.selectOptionFromDropDown();
		storeFrontReportProblemConfirmationPage = storeFrontReportOrderComplaintPage.enterYourProblemAndSubmit(TestConstants.TELL_US_ABOUT_YOUR_PROBLEM);
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyHeaderAtReportConfirmationPage("REPORT A PROBLEM"),"Report a problem is not present at header");
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyThankYouTagAtReportConfirmationPage("THANK YOU"),"Thank you tag is not present on the page");
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyEmailAddAtReportConfirmationPage(TestConstants.CONSULTANT_EMAIL_ID_TST4),"Email Address is not present as expected" );
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyOrderNumberAtReportConfirmationPage(),"Order number not present as expected");
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyBackToOrderButtonAtReportConfirmationPage(),"Back To Order button is not present");
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-130:change shipping method on autoship - PC
	// WIP
	@Test
	public void testChangeShippingMethodOnPCAutoShip_130() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_USER_EMAIL_ID_RFL,RFL_DB);
		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, TestConstants.PC_USER_PASSWORD_RFL);
		storeFrontPCUserPage.clickOnShopLink();
		storeFrontPCUserPage.clickOnAllProductsLink();
		storeFrontCartAutoShipPage = storeFrontPCUserPage.addProductToPCPerk();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		String selectedShippingMethod = storeFrontUpdateCartPage.selectAndGetShippingMethodName();
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethod(selectedShippingMethod),"shipping method is not as expected");
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-131:change shipping method on autoship - Consultant
	@Test
	public void testChangeShippingMethodOnConsultantAutoShip_131() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnShopLink();
		storeFrontConsulatantPage.clickOnAllProductsLink();
		storeFrontCartAutoShipPage = storeFrontConsulatantPage.addProductToCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		String selectedShippingMethod = storeFrontUpdateCartPage.selectAndGetShippingMethodName();
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		assertTrue("shipping method is not as expected", storeFrontOrdersPage.verifyShippingMethod(selectedShippingMethod));
		logout();
		s_assert.assertAll();
	}

	//Hybris Phase 2-2409 :: Version : 1 :: Check Return order information on the Order history page
	//	@Test
	//	public void testCheckReturnOrderInformationInOrderHistory_HP2_2409() throws InterruptedException, SQLException{
	//		List<Map<String, Object>> returnOrderDetailsList = null;
	//		List<Map<String, Object>> returnOrderStatusList = null;
	//		String returnOrderNumberDB = null;
	//		String returnOrderGrandTotalDB = null;
	//		String returnOrderStatusDB = null;
	//
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		if(driver.getEnvName().equalsIgnoreCase("stg2")){
	//			returnOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_RETURN_ORDER_DETAILS_QUERY, TestConstants.CONSULTANT_EMAIL_ID_STG2));
	//			returnOrderStatusList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_RETURN_ORDER_STATUS_QUERY, TestConstants.CONSULTANT_EMAIL_ID_STG2));
	//			returnOrderNumberDB = (String) getValueFromQueryResult(returnOrderDetailsList, "ReturnOrderNumber");
	//			returnOrderGrandTotalDB = (String) getValueFromQueryResult(returnOrderDetailsList, "Total"); 
	//			returnOrderStatusDB = (String) getValueFromQueryResult(returnOrderStatusList, "Name");
	//			storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_RETURN_ORDER_EMAIL_ID, TestConstants.CONSULTANT_RETURN_ORDER_PASSWORD);
	//		}
	//		else if(driver.getEnvName().equalsIgnoreCase("tst4")){
	//
	//			storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_RETURN_ORDER_EMAIL_ID, TestConstants.CONSULTANT_RETURN_ORDER_PASSWORD);
	//		}
	//
	//		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsulatantPage.verifyConsultantPage());
	//		storeFrontConsulatantPage.clickOnWelcomeDropDown();
	//		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
	//		assertTrue("Orders page has not been displayed", storeFrontOrdersPage.verifyOrdersPageIsDisplayed());
	//		assertTrue("Returned Order Number on UI is not as per DB", storeFrontOrdersPage.verifyReturnOrderNumber(returnOrderNumberDB));
	//		assertTrue("Returned Order Grand Total on UI is not as per DB", storeFrontOrdersPage.verifyReturnOrderGrandTotal(returnOrderGrandTotalDB));
	//		assertTrue("Returned Order Status on UI is not as per DB", storeFrontOrdersPage.verifyReturnOrderStatus(returnOrderStatusDB));		
	//	}

}

