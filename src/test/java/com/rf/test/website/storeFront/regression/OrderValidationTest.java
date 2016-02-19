package com.rf.test.website.storeFront.regression;

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
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontAccountTerminationPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontReportOrderComplaintPage;
import com.rf.pages.website.storeFront.StoreFrontReportProblemConfirmationPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class OrderValidationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(OrderValidationTest.class.getName());
	public String emailID=null;
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontAccountTerminationPage storeFrontAccountTerminationPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontReportOrderComplaintPage storeFrontReportOrderComplaintPage;
	private StoreFrontReportProblemConfirmationPage storeFrontReportProblemConfirmationPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private String RFO_DB = null;

	// Hybris Phase 2-1980 :: Version : 1 :: Order >>Actions >>Report problems
	@Test
	public void testOrdersReportProblems_1980() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
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
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyEmailAddAtReportConfirmationPage(consultantEmailID),"Email Address is not present as expected" );
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyOrderNumberAtReportConfirmationPage(),"Order number not present as expected");
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyBackToOrderButtonAtReportConfirmationPage(),"Back To Order button is not present");
		s_assert.assertAll();
	}

	// Hybris Project-1982:Order >>Actions >>Details
	@Test
	public void testCheckOrdersDetailsFromActionsTab_1982() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 

		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;

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
		String orderId = null;
		String shippingMethodId =null;
		String lastName = null;

		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String,Object>> getOrderIDList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage=storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		//select details under Actions tab on the side of the first order no.
		String firstOrderNo=storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickDetailsUnderActionsForFirstOrderUnderOrderHistory();
		//validate product details,payment method,address,status,total,currency on the order details page
		// Get Order Id
		getOrderIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,firstOrderNo),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderIDList, "OrderID"));

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		//assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4287_RFO,orderId),RFO_DB);
		subTotalDB = df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal"));

		taxDB = df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax"));

		grandTotalDB = df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total"));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4287_RFO,orderId),RFO_DB);
		shippingDB = df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost"));

		handlingDB = df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost"));

		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc Order template subtotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"Adhoc Order template tax on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc Order template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc Order template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc Order template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		if(!(shippingMethodId=="null")){
			assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"Adhoc Order template shipping method on RFO is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		}else{
			logger.info("Shipping method id is null from database");
		}

		s_assert.assertAll();
	}

	//Hybris Phase 2-1981:Orders page UI for RC
	@Test
	public void testOrdersPageUIForRCUser_HP2_1981() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> orderStatusList =  null;
		List<Map<String, Object>> orderGrandTotalList =  null;
		List<Map<String, Object>> randomRCList =  null;
		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;
		String rcUserEmailAddress = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_HAVING_ACTIVE_ORDER_RFO,countryId),RFO_DB);
		rcUserEmailAddress = (String) getValueFromQueryResult(randomRCList, "Username");

		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailAddress, password);
		//s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserEmailAddress),"RC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();

		// Get Order Id
		List<Map<String,Object>> getOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderID"));
		String orderStatusId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderStatusID"));

		/*  //assert for order number with RFO
		     orderNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO,  rcUserEmailAddress),RFO_DB);
		     orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
		     logger.info("Order Number from RFO DB is "+orderNumberDB);
		     s_assert.assertTrue(storeFrontOrdersPage.verifyOrderNumber(orderNumberDB),"Order Number on UI is different from RFO DB");
		 */
		//assert for order status with RFO
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_STATUS, orderStatusId),RFO_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		logger.info("Order Status from RFO DB is "+orderStatusDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrderStatus(orderStatusDB),"Order Status on UI is different from RFO DB");

		//assert for grand total with RFO
		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4287_RFO, orderId),RFO_DB);

		orderGrandTotalDB = String.valueOf(getValueFromQueryResult(orderGrandTotalList, "Total"));
		int grandTotal = Integer.valueOf(orderGrandTotalDB.split("\\.")[0]);
		String gTotal = orderGrandTotalDB.split("\\.")[1];

		if(grandTotal == 0){
			orderGrandTotalDB = "0"+"."+gTotal;
		}
		logger.info("Order GrandTotal from RFO DB is "+orderGrandTotalDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),"Grand total on UI is different from RFO DB");

		//assert for order date with RFO
		orderDateDB = String.valueOf (getValueFromQueryResult(getOrderDetailsList, "CompletionDate"));
		logger.info("Order Scheduled Date from RFO DB is "+orderDateDB);
		logger.info("Order Scheduled Date from RFO DB is "+orderDateDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyScheduleDate(orderDateDB),"Scheduled date on UI is different from RFO DB");

		s_assert.assertAll();  
	}

	//Hybris Project-1979:Orders page UI for PC and edit cart
	@Test
	public void testOrderPageUIAndEditCartForPC_1979() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		List<Map<String, Object>> orderStatusList =  null;
		List<Map<String, Object>> orderGrandTotalList =  null;
		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;
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
		String grandTotal = null;
		String subTotal = null;
		String shipping = null;
		String handling = null;
		String tax = null; 
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String lastName = null;
		String orderId = null;
		String accountId = null;
		String shippingMethodId =null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		// Get Order Id
		List<Map<String,Object>> getOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderID"));
		String orderStatusId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderStatusID"));
		//assert for order status with RFO
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_STATUS, orderStatusId),RFO_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		logger.info("Order Status from RFO DB is "+orderStatusDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrderStatus(orderStatusDB, orderHistoryNumber),"Order Status on UI is different from RFO DB");

		//assert for grand total with RFO
		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4287_RFO, orderId),RFO_DB);
		DecimalFormat dff = new DecimalFormat("#.00");
		orderGrandTotalDB = String.valueOf(dff.format(getValueFromQueryResult(orderGrandTotalList, "Total"))); 
		logger.info("Order GrandTotal from RFO DB is "+orderGrandTotalDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),"Grand total on UI is different from RFO DB");

		//assert for order date with RFO
		orderDateDB = String.valueOf (getValueFromQueryResult(getOrderDetailsList, "CompletionDate"));
		logger.info("Order Scheduled Date from RFO DB is "+orderDateDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyScheduleDate(orderDateDB),"Scheduled date on UI is different from RFO DB");

		// click on edit
		storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate();
		storeFrontHomePage.navigateToBackPage();
		storeFrontOrdersPage.clickOnEditAtAutoshipTemplate();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		String quantityBeforeUpdate = storeFrontUpdateCartPage.getQuantityOfProductOnCartPage();
		String updatedQuantity = storeFrontUpdateCartPage.upgradeQuantityOfProduct(quantityBeforeUpdate);
		storeFrontHomePage.addQuantityOfProduct(updatedQuantity);

		// assert update quantity on cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.getQuantityOfProductOnCartPage().contains(updatedQuantity),"CRP autoship template subTotal on RFO is "+updatedQuantity+" and on UI is "+storeFrontUpdateCartPage.getQuantityOfProductOnCartPage());

		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();

		// values for assertion for pending order autoship template
		subTotal = storeFrontUpdateCartPage.getSubtotalFromCart();
		shipping = storeFrontUpdateCartPage.getDeliveyFromCart();
		handling = storeFrontUpdateCartPage.getHandlingFromCart();
		tax = storeFrontUpdateCartPage.getTaxFromCart();
		grandTotal = storeFrontUpdateCartPage.getGrandTotalFromCart();

		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		// assert update quantity on autoship template 
		s_assert.assertTrue(storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate().contains(updatedQuantity),"CRP autoship template subTotal on RFO is "+updatedQuantity+" and on UI is "+storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate());

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotal),"PC autoship cart subTotal is "+subTotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"PC autoship cart tax amount is "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotal),"PC autoship cart grand total is "+grandTotal+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(shipping.contains(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate()),"PC autoship cart shipping amount is "+shipping+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handling),"PC autoship cart handling amount is "+handling+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// Now verify the details of orders
		storeFrontHomePage.navigateToBackPage();

		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksOrderPageHeader(),"Order Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();

		// assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4292_RFO,orderId),RFO_DB);
		subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));

		taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));

		grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4292_RFO,orderId),RFO_DB);
		shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));

		handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc order template subTotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		//assertTrue("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),"Adhoc order template tax amount on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc order template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc order template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc order template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB),"Adhoc order template shipping method on RFO is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate());  

		s_assert.assertAll();  	

	}

	//Hybris Project-4301:In DB, check details of cancelled CRP autoship for active Consultant.
	@Test
	public void testCancelledCRPAutoshipForActiveConsultant_4301() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		String accountID = null;
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String consultantEmailID = null;
		String lastName = null;
		String shippingMethodId =null;
		String shippingAddressFromUI =null;
		String subTotalUI = null;
		String shippingUI = null;
		String handlingUI = null;
		String taxUI = null;	
		String grandTotalUI = null;
		String shippingMethodUI = null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number for assert
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		// get order details before cancel CRP
		shippingAddressFromUI = storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate();
		subTotalUI = storeFrontOrdersPage.getSubTotalFromAutoshipTemplate();
		taxUI = storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate();
		grandTotalUI = storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate();
		shippingUI = storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate();
		handlingUI = storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate();
		shippingMethodUI = storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate();

		storeFrontOrdersPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyCRP();
		//validate CRP has been cancelled..
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");

		//get Autoship Id Fro RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));
		System.out.println("Autoship id "+autoshipID);
		List<Map<String,Object>> shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
		region = (String) getValueFromQueryResult(shippingAddressList, "Region");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);

		//assert shipping Address with RFO
		s_assert.assertTrue(shippingAddressFromUI.trim().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+shippingAddressFromUI);

		//Assert Subtotal with RFO
		s_assert.assertTrue(subTotalUI.trim().contains(subTotalDB.trim()),"CRP autoship template subTotal on RFO is "+subTotalDB+" and on UI is "+subTotalUI);

		// Assert Tax with RFO
		s_assert.assertTrue(taxUI.trim().contains(taxDB.trim()),"CRP autoship template tax amount on RFO is "+taxDB+" and on UI is "+taxUI);

		// Assert Grand Total with RFO
		s_assert.assertTrue(grandTotalUI.trim().contains(grandTotalDB.trim()),"CRP autoship template grand total on RFO is "+grandTotalDB+" and on UI is "+grandTotalUI);

		// assert shipping amount with RFO
		s_assert.assertTrue(shippingUI.trim().contains(shippingDB.trim()),"CRP autoship template shipping amount on RFO is "+shippingDB+" and on UI is "+shippingUI);

		// assert Handling Value with RFO
		s_assert.assertTrue(handlingUI.trim().contains(handlingDB.trim()),"CRP autoship template handling amount on RFO is "+handlingDB+" and on UI is "+handlingUI);

		// assert for shipping Method with RFO
		s_assert.assertTrue(shippingMethodUI.trim().contains(shippingMethodDB.trim()),"CRP autoship template shipping method on RFO is "+shippingMethodDB+" and on UI is "+shippingMethodUI);

		s_assert.assertAll();
	}

	// Hybris Project-2273:Adhoc Orders for Consultant and PC and RC --> Multiple line Item
	@Test
	public void testAdhocOrdersForMultiplsLineItem_2273() throws InterruptedException  {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontOrdersPage=new StoreFrontOrdersPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//Select a product with the price less than $80 and proceed to buy it
		//storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		//update qty to 2 of the first product
		storeFrontHomePage.addQuantityOfProduct("2"); 
		//add another product in the cart
		storeFrontHomePage.addAnotherProduct();
		logger.info("2 products are successfully added to the cart");
		//update qty to 1 of the second product
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("1"); 
		//get the quantity value for the first product in cart
		int qtyProduct1=storeFrontHomePage.getQuantityValueForTheFirstProduct();
		//get the quantity value for the Second product in cart
		int qtyProduct2=storeFrontHomePage.getQuantityValueForTheSecondProduct();
		//get the sub-total of the first product
		double subtotal1=storeFrontHomePage.getSubTotalOfFirstProduct();
		//get the sub-total of the second product
		double subtotal2=storeFrontHomePage.getSubTotalOfSecondProduct();
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//click next button
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//click next button
		storeFrontHomePage.clickOnBillingNextStepBtn();
		//click place-order button
		storeFrontHomePage.clickPlaceOrderBtn();
		//Navigate to orders section
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontOrdersPage=storeFrontHomePage.clickOrdersLinkPresentOnWelcomeDropDown();
		//click on first adhoc order placed
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//On orders page validate the sub-total for the order placed
		s_assert.assertTrue(storeFrontOrdersPage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		//validate with the no. of quantitie(s) of both the products
		s_assert.assertTrue(storeFrontOrdersPage.validateQuantityForFirstProduct(qtyProduct1),"quantitie(s) for product 1 didn't match");
		s_assert.assertTrue(storeFrontOrdersPage.validateQuantityForSecondProduct(qtyProduct2),"quantitie(s) for product 2 didn't match");
		s_assert.assertAll();
	}

	//Hybris Project-2272:Adhoc Orders from Consultant and PC and RC --> Single line Item
	@Test
	public void testAdhocOrdersFromConsultantAndPc_2272() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.clickAddToBagButton();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		String quantityOfProductsOrdered = storeFrontUpdateCartPage.getQuantityOfProductOnCartPage();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		String totalPrice = storeFrontUpdateCartPage.getSubtotal();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.isOrderPlacedSuccessfully(),"order is not placed successfully");
		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontHomePage.clickOrdersLinkPresentOnWelcomeDropDown();
		String orderNumber=storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderNumber); 
		s_assert.assertTrue(storeFrontOrdersPage.verifyQuantityOnOrdersDetails(quantityOfProductsOrdered),"quantity is not matched in order detail page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyTotalValueOfProductOnOrderDetails(totalPrice),"Price mismatch");
		s_assert.assertAll();
	}

	// Hybris Project-4304:In DB, check details of CRP autoship for inactive consultant
	@Test
	public void checkDetailsOfCRpAutoshipForInactiveConsultant_4304() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String consultantEmailID = null;
		String lastName = null;
		String accountId = null;
		String shippingMethodId =null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}		
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		// Get Order Number for assert
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		//get Autoship Id Fro RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));
		//get values from UI before termination
		String shippingAddressFromUI =storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate();
		String subTotalFromUI = storeFrontOrdersPage.getSubTotalFromAutoshipTemplate();
		String shippingFromUI = storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate();
		String handlingFromUI = storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate();
		String taxFromUI = 	storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate();
		String grandTotalFromUI = storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate();
		String shippingMethodFromUI = storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate();
		// terminate the consultant
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
		//s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Terminated User doesn't get Login failed");
		// get values from DB for assertion 
		List<Map<String,Object>> shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
		region = (String) getValueFromQueryResult(shippingAddressList, "Region");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1.trim()+"\n"+locale.trim()+", "+region.trim()+" "+postalCode.trim()+"\n"+country.toUpperCase().trim();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		//assert shipping Address with RFO
		s_assert.assertTrue(shippingAddressFromUI.contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+shippingAddressFromUI);
		//Assert Subtotal with RFO
		s_assert.assertTrue(subTotalFromUI.contains(subTotalDB),"CRP autoship template subTotal on RFO is "+subTotalDB+" and on UI is "+subTotalFromUI);
		// Assert Tax with RFO
		s_assert.assertTrue(taxFromUI.contains(taxDB),"CRP autoship template tax amount on RFO is "+taxDB+" and on UI is "+taxFromUI);
		// Assert Grand Total with RFO
		s_assert.assertTrue(grandTotalFromUI.contains(grandTotalDB),"CRP autoship template grand total on RFO is "+grandTotalDB+" and on UI is "+grandTotalFromUI);
		// assert shipping amount with RFO
		s_assert.assertTrue(shippingFromUI.contains(shippingDB),"CRP autoship template shipping amount on RFO is "+shippingDB+" and on UI is "+shippingFromUI);
		// assert Handling Value with RFO
		s_assert.assertTrue(handlingFromUI.contains(handlingDB),"CRP autoship template handling amount on RFO is "+handlingDB+" and on UI is "+handlingFromUI);
		// assert for shipping Method with RFO
		s_assert.assertTrue(shippingMethodFromUI.contains(shippingMethodDB),"CRP autoship template shipping method on RFO is "+shippingMethodDB+" and on UI is "+shippingMethodFromUI);
		s_assert.assertAll();
	}

	// Hybris Phase 2-4300 :: Version : 1 :: Verify PC autoship template details. 
	@Test
	public void testPCAutoShipTemplateDetails_HP2_4300() throws InterruptedException, SQLException , ClassNotFoundException{
		RFO_DB = driver.getDBNameRFO();
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String pcUserEmailID = null;
		String lastName = null;
		String accountId = null;
		String shippingMethodId = null;
		List<Map<String, Object>> randomPCUserList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksAutoShipHeader(),"Order Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		//get Autoship Id Fro RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));

		List<Map<String,Object>> shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
		region = (String) getValueFromQueryResult(shippingAddressList, "Region");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);

		//assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate());

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"PC autoship template subTotal is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"PC autoship template tax amount is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"PC autoship template grand total is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"PC autoship template shipping amount is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"PC autoship template handling amount is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"PC autoship template shipping method is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

		s_assert.assertAll();
	}

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
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String consultantEmailID = null;
		String lastName = null;
		String accountId = null;
		String shippingMethodId =null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}		

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number for assert
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//get Autoship Id Fro RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));
		System.out.println("Autoship id "+autoshipID);
		List<Map<String,Object>> shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
		region = (String) getValueFromQueryResult(shippingAddressList, "Region");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);

		//assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate());

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"CRP autoship template subTotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"CRP autoship template tax amount on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"CRP autoship template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"CRP autoship template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"CRP autoship template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"CRP autoship template shipping method on RFO is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		s_assert.assertAll();
	}

	// phase 2 4287 -> Verify order details of consultant order
	@Test
	public void testOrdersDetailsOfConsultant_HP2_4287() throws SQLException, InterruptedException{
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
		String orderId = null;
		String accountId = null;
		String shippingMethodId =null;
		String lastName = null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String,Object>> getOrderIDList = null;
		DecimalFormat df = new DecimalFormat("#.00");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		// Get Order Id
		getOrderIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderIDList, "OrderID"));

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		//assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4287_RFO,orderId),RFO_DB);
		subTotalDB = df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal"));

		taxDB = df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax"));

		grandTotalDB = df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total"));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4287_RFO,orderId),RFO_DB);
		shippingDB = df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost"));

		handlingDB = df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost"));

		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc Order template subtotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"Adhoc Order template tax on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc Order template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc Order template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc Order template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"Adhoc Order template shipping method on RFO is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		s_assert.assertAll();
	}

	// Hybris Phase 2-4291 :: Version : 1 :: Verify PC autoship order. 
	@Test
	public void testOrderDetailsForAutoshipOrdersForPC_4291() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String pcUserEmailID = null;
		String lastName = null;
		String accountId = null;
		String shippingMethodId = null;
		List<Map<String, Object>> randomPCUserList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");

		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksAutoShipHeader(),"Order Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		//get Autoship Id Fro RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));

		List<Map<String,Object>> shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
		region = (String) getValueFromQueryResult(shippingAddressList, "Region");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);

		//assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate());

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"PC autoship template subTotal is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"PC autoship template tax amount is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"PC autoship template grand total is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"PC autoship template shipping amount is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"PC autoship template handling amount is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"PC autoship template shipping method is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		s_assert.assertAll();
	}

	//Hybris Phase 2-4293 :: Version : 1 :: Verify details of retail order. 
	@Test
	public void testOrderDetailsForAdhocOrdersForRC_4293() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		//			String locale = null;
		//			String region = null;
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
		String orderId = null;
		String accountId = null;
		String shippingMethodId = null;
		String rcUserEmailID = null;
		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String,Object>> getShippingMethodBasedOnMethodId = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		//s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserEmailID),"RC Page doesn't contain Welcome User Message");
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

		verifyAllDetailsList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		//assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());


		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4293_RFO,orderId),RFO_DB);
		subTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));

		taxDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));

		grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4293_RFO,orderId),RFO_DB);
		shippingDB = String.valueOf(df.format((Number) getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));

		handlingDB = String.valueOf(df.format((Number) getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		shippingMethodId = String.valueOf( getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		//GET_SHIPPING_METHOD_QUERY_RFO
		getShippingMethodBasedOnMethodId =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO,shippingMethodId),RFO_DB);
		shippingMethodDB = (String) getValueFromQueryResult(getShippingMethodBasedOnMethodId, "Name");
		String[] name = shippingMethodDB.split("\\(");
		String shippingMethodName = name[0]+" "+"("+name[1];
		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc Order template subTotal from RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"Adhoc Order template tax amount from RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc Order template grand total from RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc Order template shipping amount from RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc Order template handling amount from RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		//shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodName.trim()),"Adhoc Order template shipping method from RFO is "+shippingMethodName+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate());

		s_assert.assertAll();
	}

	//Hybris Project-1983:Return order history
	@Test
	public void testReturnOrderHistory_1983() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String subTotalDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String consultantEmailID = null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> getOrderDetailsList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_RETURN_ORDERS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");		
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number for assert
		String returnOrderNumber = storeFrontOrdersPage.getReturnOrderNumber();
		storeFrontOrdersPage.clickReturnOrderNumber();

		// Get Order Id
		getOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_RFO,returnOrderNumber),RFO_DB);
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "Total")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "TotalTax")));
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "SubTotal")));
		String orderStatusID = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "ReturnStatusID"));
		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc Order template subtotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"Adhoc Order template tax on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc Order template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		//		// assert shipping amount with RFO
		//		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc Order template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		//		// assert Handling Value with RFO
		//		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc Order template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		String createdOrderStatus = storeFrontOrdersPage.convertOrderStatusForReturnOrder(orderStatusID);				
		s_assert.assertTrue(storeFrontOrdersPage.getOrderStatusFromUI().toLowerCase().trim().contains(createdOrderStatus.toLowerCase().trim()),"Adhoc Order template handling amount on RFO is "+createdOrderStatus+" and on UI is "+storeFrontOrdersPage.getOrderStatusFromUI());

		s_assert.assertAll();
	}

	//Hybris Project-4459:Check Return order information on the Order history page. (Returning PC Perks Autoship Order)
	@Test
	public void testPCAutoshipReturnOrderInformation_4459() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String subTotalDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String consultantEmailID = null;
		String orderId = null;
		String returnOrderNumber = null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> getOrderDetailsList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACTIVE_PC_WITH_RETURN_PC_PERKS_AUTOSHIP_ORDER,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");  
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		//get size of return orders
		int noOfReturnOrders = storeFrontOrdersPage.getSizeOfReturnOrders();
		for(int i=3; i<=noOfReturnOrders; i=i+2){
			returnOrderNumber = storeFrontOrdersPage.getReturnOrderNumber(i);
			System.out.println("Order number is "+returnOrderNumber);
			getOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_RFO,returnOrderNumber),RFO_DB);
			orderId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderID"));
			//get order type ID
			List<Map<String,Object>> orderTypeIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4286_RFO,orderId),RFO_DB);
			String ordertTypeID = String.valueOf(getValueFromQueryResult(orderTypeIdList, "OrderTypeID"));
			if(ordertTypeID.contains("9")){
				break;
			}else{
				continue;
			}
		}

		// Get Order Number for assert

		storeFrontOrdersPage.clickReturnOrderNumber(returnOrderNumber);
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "Total")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "TotalTax")));
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "SubTotal")));
		String orderStatusID = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "ReturnStatusID"));

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc Order template subtotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"Adhoc Order template tax on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc Order template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		//s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc Order template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		//s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc Order template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		String createdOrderStatus = storeFrontOrdersPage.convertOrderStatusForReturnOrder(orderStatusID);    
		s_assert.assertTrue(storeFrontOrdersPage.getOrderStatusFromUI().toLowerCase().trim().contains(createdOrderStatus.toLowerCase().trim()),"Adhoc Order template handling amount on RFO is "+createdOrderStatus+" and on UI is "+storeFrontOrdersPage.getOrderStatusFromUI());

		s_assert.assertAll();
	}

	

	//Hybris Project-1978:Orders page UI for Consultant - Cart - history and autoships
	//	@Test(enabled=false) //Tax value doesn't get updated while automation executes
	//	public void testOrderPageUIAndEditCartForConsultant_1978() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();
	//		List<Map<String, Object>> randomConsultantList =  null;
	//		String consultantEmailID = null;
	//		String accountID = null;
	//		List<Map<String, Object>> orderStatusList =  null;
	//
	//		String orderStatusDB = null;
	//		String orderGrandTotalDB = null;
	//		String orderDateDB = null;
	//		String firstName = null;
	//		String addressLine1 = null;
	//		String postalCode = null;
	//		String country = null;
	//		String shippingAddressFromDB =null;
	//		String city = null;
	//		String state = null;
	//		String subTotalDB = null;
	//		String shippingDB = null;
	//		String handlingDB = null;
	//		String taxDB = null; 
	//		String grandTotal = null;
	//		String subTotal = null;
	//		String shipping = null;
	//		String handling = null;
	//		String tax = null; 
	//		String grandTotalDB = null;
	//		String shippingMethodDB = null;
	//		String lastName = null;
	//		String orderId = null;
	//		String shippingMethodId =null;
	//		List<Map<String, Object>> verifyAllDetailsList = null;
	//		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
	//		List<Map<String,Object>> getOtherDetailValuesList = null;
	//		DecimalFormat df = new DecimalFormat("#.00");
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		while(true){
	//			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
	//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
	//			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
	//			logger.info("Account Id of the user is "+accountID);
	//			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
	//			boolean isError = driver.getCurrentUrl().contains("error");
	//			if(isError){
	//				logger.info("login error for the user "+consultantEmailID);
	//				driver.get(driver.getURL());
	//			}
	//			else
	//				break;
	//		}
	//		logger.info("login is successful");
	//		storeFrontConsultantPage.clickOnWelcomeDropDown();
	//		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
	//		// Get Order Number
	//		//		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
	//		//		// Get Order Id
	//		//		List<Map<String,Object>> getOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
	//		//		orderId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderID"));
	//		//		String orderStatusId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderStatusID"));
	//		//
	//		//		//assert for order status with RFO
	//		//		orderStatusList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_STATUS, orderStatusId),RFO_DB);
	//		//		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
	//		//		logger.info("Order Status from RFO DB is "+orderStatusDB);
	//		//		s_assert.assertTrue(storeFrontOrdersPage.verifyOrderStatus(orderStatusDB, orderHistoryNumber),"Order Status on UI is different from RFO DB");
	//		//
	//		//		//assert for grand total with RFO
	//		//		DecimalFormat dff = new DecimalFormat("#.00");
	//		//		orderGrandTotalDB = String.valueOf(dff.format(getValueFromQueryResult(getOrderDetailsList, "Total"))); 
	//		//		logger.info("Order GrandTotal from RFO DB is "+orderGrandTotalDB);
	//		//		s_assert.assertTrue(storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),"Grand total on UI is different from RFO DB");
	//
	//		//		//assert for order date with RFO
	//		//		orderDateDB = String.valueOf (getValueFromQueryResult(getOrderDetailsList, "CompletionDate"));
	//		//		logger.info("Order Scheduled Date from RFO DB is "+orderDateDB);
	//		//		s_assert.assertTrue(storeFrontOrdersPage.verifyScheduleDate(orderDateDB),"Scheduled date on UI is different from RFO DB");
	//		// click on edit
	//		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
	//		logger.info("Autoship Number is "+autoshipNumber);
	//		//		storeFrontOrdersPage.clickAutoshipOrderNumber();
	//		//		storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate();
	//		//		storeFrontHomePage.navigateToBackPage();
	//		storeFrontOrdersPage.clickOnEditAtAutoshipTemplate();
	//		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
	//		String quantityBeforeUpdate = storeFrontUpdateCartPage.getQuantityOfProductOnCartPage();
	//		String updatedQuantity = storeFrontUpdateCartPage.upgradeQuantityOfProduct(quantityBeforeUpdate);
	//		storeFrontHomePage.addQuantityOfProduct(updatedQuantity);
	//		// assert update quantity on cart page
	//		s_assert.assertTrue(storeFrontUpdateCartPage.getQuantityOfProductOnCartPage().contains(updatedQuantity),"CRP autoship template subTotal on RFO is "+updatedQuantity+" and on UI is "+storeFrontUpdateCartPage.getQuantityOfProductOnCartPage());
	//		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
	//		storeFrontUpdateCartPage.clickUpdateCartBtn();
	//
	//		// values for assertion for pending order autoship template
	//		subTotal = storeFrontUpdateCartPage.getSubtotalFromCart();
	//		shipping = storeFrontUpdateCartPage.getDeliveyFromCart();
	//		handling = storeFrontUpdateCartPage.getHandlingFromCart();
	//		tax = storeFrontUpdateCartPage.getTaxFromCart();
	//		grandTotal = storeFrontUpdateCartPage.getGrandTotalFromCart();
	//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
	//		storeFrontConsultantPage.clickOnWelcomeDropDown();
	//		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
	//		storeFrontOrdersPage.clickAutoshipOrderNumber(autoshipNumber);
	//		// assert update quantity on autoship template 
	//		//s_assert.assertTrue(storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate().contains(updatedQuantity),"CRP autoship template subTotal on RFO is "+updatedQuantity+" and on UI is "+storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate());
	//		//Assert Subtotal with RFO
	//		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotal),"autoship cart subTotal is "+subTotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
	//		// Assert Tax with RFO
	//		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"autoship cart tax amount is "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());
	//		// Assert Grand Total with RFO
	//		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotal),"autoship cart grand total is "+grandTotal+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
	//		// assert shipping amount with RFO
	//		s_assert.assertTrue(shipping.contains(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate()),"autoship cart shipping amount is "+shipping+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
	//		// assert Handling Value with RFO
	//		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handling),"autoship cart handling amount is "+handling+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
	//		// Now verify the details of orders
	//		//		storeFrontHomePage.navigateToBackPage();
	//		//		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
	//		//		storeFrontHomePage.clickOnYourAccountDropdown();
	//		//		storeFrontHomePage.clickOnAutoshipStatusLink();
	//		//		if(storeFrontAccountInfoPage.validateSubscribeToPulse()==true){
	//		//			storeFrontHomePage.cancelPulseSubscription();
	//		//		}
	//		//
	//		//		s_assert.assertTrue(storeFrontAccountInfoPage.validatePulseCancelled(),"pulse subscription is not cancelled for the user");
	//		//		storeFrontAccountInfoPage.clickOnSubscribeToPulseBtn();
	//		//
	//		//		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
	//		//		storeFrontUpdateCartPage.clickOnAccountInfoNextButton();
	//		//
	//		//		subTotal = storeFrontUpdateCartPage.getSubtotalFromCart();
	//		//		shipping = storeFrontUpdateCartPage.getDeliveyFromCart();
	//		//		handling = storeFrontUpdateCartPage.getHandlingFromCart();
	//		//		tax = storeFrontUpdateCartPage.getTaxFromCart();
	//		//		grandTotal = storeFrontUpdateCartPage.getGrandTotalFromCart();
	//		//		storeFrontUpdateCartPage.clickOnSubscribePulseTermsAndConditionsChkbox();
	//		//		storeFrontUpdateCartPage.clickOnSubscribeBtn();
	//		//
	//		//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
	//		//		storeFrontConsultantPage.clickOnWelcomeDropDown();
	//		//		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
	//		//		storeFrontOrdersPage.clickAutoshipOrderNumberOfPulse();
	//		//
	//		//		//Assert Subtotal with RFO
	//		//		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotal),"autoship cart subTotal is "+subTotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
	//		//		// Assert Tax with RFO
	//		//		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"autoship cart tax amount is "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());
	//		//		// Assert Grand Total with RFO
	//		//		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotal),"autoship cart grand total is "+grandTotal+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
	//		//		// assert shipping amount with RFO
	//		//		s_assert.assertTrue(shipping.contains(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate()),"autoship cart shipping amount is "+shipping+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
	//		//		// assert Handling Value with RFO
	//		//		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handling),"autoship cart handling amount is "+handling+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
	//		//		// Now verify the details of orders
	//		//		storeFrontHomePage.navigateToBackPage();
	//		//
	//		//		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
	//		//		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksOrderPageHeader(),"Order Text is not present on the Page");
	//		//		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderDateText(),"Schedule Date Text is not present on the Page");
	//		//		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");
	//		//		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
	//		//		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
	//		//		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
	//		//		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
	//		//		city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
	//		//		state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
	//		//		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
	//		//		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
	//		//		if(country.equals("40")){
	//		//			country = "canada"; 
	//		//		}else if(country.equals("236")){
	//		//			country = "United States";
	//		//		}
	//		//		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase();
	//		//		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
	//		//		// assert shipping Address with RFO
	//		//		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());
	//		//		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4292_RFO,orderId),RFO_DB);
	//		//		subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
	//		//		taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
	//		//		grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));
	//		//		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4292_RFO,orderId),RFO_DB);
	//		//		shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
	//		//		handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
	//		//		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
	//		//		//Assert Subtotal with RFO
	//		//		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc order template subTotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
	//		//		// Assert Tax with RFO
	//		//		//assertTrue("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
	//		//		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),"Adhoc order template tax amount on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
	//		//		// Assert Grand Total with RFO
	//		//		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc order template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
	//		//		// assert shipping amount with RFO
	//		//		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc order template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
	//		//		// assert Handling Value with RFO
	//		//		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc order template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
	//		//		// assert for shipping Method with RFO
	//		//		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
	//		//		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB),"Adhoc order template shipping method on RFO is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate());  
	//		s_assert.assertAll();  	
	//	}


}

