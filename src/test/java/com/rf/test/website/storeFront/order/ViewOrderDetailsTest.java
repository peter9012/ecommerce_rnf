package com.rf.test.website.storeFront.order;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

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
	private StoreFrontConsultantPage storeFrontConsulatantPage;	
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
		String phoneNumber = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethod = null;
		String payeeNameDB = null;
		String cardTypeDB = null;
		String consultantEmailID = null;

		List<Map<String, Object>> expirationDateOfCardList = null;
		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String, Object>> autoShipItemTotalPriceList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> autoShipPaymentDetailsList = null;
		List<Map<String, Object>> randomConsultantList =  null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAVING_SUBMITTED_ORDERS_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
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
		shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ADDRESS_QUERY_TST4, consultantEmailID),RFL_DB);
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
			shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_QUERY_RFO,consultantEmailID),RFO_DB);
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
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_COST_QUERY_RFO,consultantEmailID),RFO_DB);
			shippingDB = String.valueOf(dff.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingCost")));
			assertTrue("CRP Autoship Shipping value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}
		if(assertTrueDB("CRP Autoship Handling value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			//verify handling amount in RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_HANDLING_COST_QUERY_RFO,consultantEmailID),RFO_DB);
			handlingDB = String.valueOf(dff.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingCost")));
			assertTrue("CRP Autoship Handling value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_GRAND_TOTAL_QUERY_RFO, consultantEmailID),RFO_DB);
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

	// Hybris Phase 2 4287 -> Verify order details of consultant order
	@Test
	public void testOrdersDetailsOfConsultant_HP2_4287() throws SQLException, InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> orderNumberList =  null;
		List<Map<String, Object>> orderStatusList =  null;
		List<Map<String, Object>> orderGrandTotalList =  null;
		List<Map<String, Object>> orderDateList =  null;
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAVING_SUBMITTED_ORDERS_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		String orderNumberDB = null;
		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		//Assert for Order Number with RFL  
		orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFL, consultantEmailID), RFL_DB);
		orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
		if(assertTrueDB("Order Number on UI is different from DB", storeFrontOrdersPage.verifyOrderNumber(orderNumberDB), RFL_DB)==false){
			//assert for Order Number with RFO
			orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO, consultantEmailID), RFO_DB);
			orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
			assertTrue("Order Number on UI is different from DB", storeFrontOrdersPage.verifyOrderNumber(orderNumberDB));
		}

		// Assert For Order Date with RFL

		orderDateList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFL, consultantEmailID), RFL_DB);
		orderDateDB = String.valueOf(getValueFromQueryResult(orderDateList, "StartDate"));
		if(assertTrueDB("Order date on UI as different from DB", storeFrontOrdersPage.verifyScheduleDate(orderDateDB), RFL_DB)==false){
			// Assert For Order Date with RFO
			orderDateList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFO, consultantEmailID),RFO_DB);
			orderDateDB = String.valueOf(getValueFromQueryResult(orderDateList, "CompletionDate"));
			assertTrue("Order Date on UI is different from DB", storeFrontOrdersPage.verifyScheduleDate(orderDateDB));
		}

		// Assert For Grand Total with RFL

		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFL, consultantEmailID), RFL_DB);
		DecimalFormat df = new DecimalFormat("#.00");
		orderGrandTotalDB = df.format((Number)getValueFromQueryResult(orderGrandTotalList, "GrandTotal"));
		if(assertTrueDB("Grand total is not as expected for Consultant", storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB), RFL_DB)==false){
			// Assert For Grand Total with RFO
			orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFO, consultantEmailID), RFO_DB);
			orderGrandTotalDB = df.format((Number)getValueFromQueryResult(orderGrandTotalList, "AmountToBeAuthorized"));
			assertTrue("Grand total is not as expected for Consultant", storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB));
		}

		// assert for Order Status with RFL
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFL, consultantEmailID), RFL_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		if(assertTrueDB("Order status is not as expected for Consultant", storeFrontOrdersPage.verifyOrderStatus(orderStatusDB), RFL_DB)== false){
			orderStatusList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFO, consultantEmailID), RFO_DB);
			orderStatusDB = String.valueOf(getValueFromQueryResult(orderStatusList, "Name"));
			assertTrue("Order status is not as expected for Consultant", storeFrontOrdersPage.verifyOrderStatus(orderStatusDB));
		}
		logout();
		s_assert.assertAll();
	}

}
