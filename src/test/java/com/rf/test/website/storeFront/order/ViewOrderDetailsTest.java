package com.rf.test.website.storeFront.order;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
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
	@Test(enabled=true)
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
		String lastName = null;
		String shippingMethodId = null;
		String shippingMethodDB = null;

		List<Map<String, Object>> expirationDateOfCardList = null;
		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String, Object>> autoShipItemTotalPriceList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> autoShipPaymentDetailsList = null;
		List<Map<String, Object>> randomConsultantList =  null;
		/*randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAVING_SUBMITTED_ORDERS_RFL,RFL_DB);
	  consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");*/

		String consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A hard code user
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
		shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_DETAILS_FOR_CONSULTANT_RFL_4289, autoshipNumber),RFL_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		city = (String) getValueFromQueryResult(shippingAddressList, "City");
		state = (String) getValueFromQueryResult(shippingAddressList, "State");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostCode");
		phoneNumber = (String) getValueFromQueryResult(shippingAddressList, "PhoneNumber");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingMethodId = String.valueOf(getValueFromQueryResult(shippingAddressList, "ShippingMethodID"));
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		System.out.println("Created ***"+shippingAddressFromDB);
		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
			//verify shipping address in RFO
			/*shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_QUERY_RFO,consultantEmailID),RFO_DB);
	   firstName = (String) getValueFromQueryResult(shippingAddressList, "AddressProfileName");
	   addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "AddressLine1");
	   postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
	   locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
	   region = (String) getValueFromQueryResult(shippingAddressList, "Region");
	   country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
	   if(country.equals("236")){
	    country = "United States"; 
	   }
	   shippingAddressFromDB = firstName+" "+lastName+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
	   //shippingAddressFromDB = addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
	   assertTrue("Shipping Address is not as expected",  storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));
			 */
			System.out.println("Shipping address in RFL");
		}
		//verify autoship item details in RFL
		DecimalFormat df = new DecimalFormat("#.00");
		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DETAILS_FOR_CONSULTANT_RFL_4289, autoshipNumber),RFL_DB);
		subTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "TaxAmountTotal")));
		grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "PaymentTotal")));
		DecimalFormat dff = new DecimalFormat("#.00");

		// Assert shipping Amount in RFL
		if(assertTrueDB("CRP Autoship Shipping value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			//verify shipping amount in RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_COST_QUERY_RFO,consultantEmailID),RFO_DB);
			shippingDB = String.valueOf(dff.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingCost")));
			assertTrue("CRP Autoship Shipping value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// Assert handling Amount with RFL
		if(assertTrueDB("CRP Autoship Handling value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			//verify handling amount in RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_HANDLING_COST_QUERY_RFO,consultantEmailID),RFO_DB);
			handlingDB = String.valueOf(dff.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingCost")));
			assertTrue("CRP Autoship Handling value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// assert Shipping Method with RFL
		shippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_METHOD_FOR_CONSULTANT_RFL_4289, shippingMethodId),RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(shippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(shippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}
		shippingMethod = methodName+" "+"("+methodShortName+")";
		System.out.println("Method from DB "+shippingMethod);
		if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethod),RFL_DB)==false){
			// assert shipping method with RFO
			// waiting for RFo Queries
			//     assertTrue("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethod));
		}


		// assert Subtotal with RFL
		if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB) == false){
			// assert subtotal with RFO
			// waiting for RFO Queries.
		}

		/*// assert Tax with RFL
	  if(assertTrueDB("", storeFrontOrdersPage.verifyAutoShipTemplateTax(taxDB), RFL_DB) == false){
	   // assert subtotal with RFO
	   // waiting for RFO Queries.
	  }

	  // assert Grand Total with RFL
	  if(assertTrueDB("", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB), RFL_DB) == false){
	   // assert subtotal with RFO
	   // waiting for RFO Queries.
	  }*/

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

	//Hybris Phase 2-4294 :: Version : 1 :: Verify details of failed consultant autoship orders.
	@Test(enabled=false)
	public void testConsultantFailedOrderDetails() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String failedOrderNumber = null;
		String subTotal = null;
		String shippingCharges = null;
		String handlingCharges = null;
		String tax = null;
		String total = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_FAILED_ORDERID_AND_ACCOUNT_ID_RFL_4294,RFL_DB);
		String accountId = (String) getValueFromQueryResult(randomConsultantList, "AccountId");
		String orderId = (String) getValueFromQueryResult(randomConsultantList, "OrderId");

		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		failedOrderNumber = (String) getValueFromQueryResult(randomConsultantList, "OrderNumber");
		subTotal = (String) getValueFromQueryResult(randomConsultantList, "Subtotal");
		shippingCharges = (String) getValueFromQueryResult(randomConsultantList, "ShippingTotal");
		handlingCharges = (String) getValueFromQueryResult(randomConsultantList, "HandlingTotal");
		tax = (String) getValueFromQueryResult(randomConsultantList, "TaxAmount");
		total = (String) getValueFromQueryResult(randomConsultantList, "GrandTotal");

		//consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A Hard Coded User		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOrderNumber(failedOrderNumber);
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateSubtotal(subTotal));
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateShippingCharges(shippingCharges));
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateHandlingCharges(handlingCharges));
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTax(tax));
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTotal(total));

		logout();
		s_assert.assertAll();
	}

}
