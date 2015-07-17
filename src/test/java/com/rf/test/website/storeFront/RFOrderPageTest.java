package com.rf.test.website.storeFront;

import org.testng.annotations.Test;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontOrdersAutoshipStatusPage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontRCUserPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class RFOrderPageTest extends RFWebsiteBaseTest{
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontOrdersAutoshipStatusPage storeFrontOrdersAutoshipStatusPage;

	//Hybris Phase 2-4289 : Verify CRP autoship template details
	@Test
	public void testCRPAutoShipTemplateDetails_HP2_4289() throws InterruptedException, SQLException , ClassNotFoundException{
		ResultSet expirationDateOfCard_resultSet = null;
		ResultSet autoShipItemDetails_resultSet = null;
		ResultSet autoShipItemTotalPrice_resultSet = null;
		ResultSet shippingAddress_resultSet = null;

		String expirationYear = null;
		String expirationMonth = null;
		String autoShipItemSKUValue = null;
		String autoShipItemDescription = null;
		String expirationDateOfCardFromDB = null;
		String autoShipItemTotalPriceFromDB = null;
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;

		expirationDateOfCard_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_EXPIRATION_DATE_QUERY, TestConstants.CONSULTANT_EMAIL_ID));
		autoShipItemDetails_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ITEM_DETAILS_QUERY, TestConstants.CONSULTANT_EMAIL_ID));
		autoShipItemTotalPrice_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ITEM_TOTAL_PRICE_QUERY, TestConstants.CONSULTANT_EMAIL_ID));
		shippingAddress_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_QUERY, TestConstants.CONSULTANT_EMAIL_ID));

		while(expirationDateOfCard_resultSet.next()) {
			expirationYear = expirationDateOfCard_resultSet.getString("ExpYear");
			expirationMonth = expirationDateOfCard_resultSet.getString("ExpMonth");
		}

		while(autoShipItemDetails_resultSet.next()){
			autoShipItemSKUValue = autoShipItemDetails_resultSet.getString("SKU");
			autoShipItemDescription = autoShipItemDetails_resultSet.getString("ShortDescription");
		}

		while(autoShipItemTotalPrice_resultSet.next()) {
			autoShipItemTotalPriceFromDB = autoShipItemTotalPrice_resultSet.getString("price");
		}

		while(shippingAddress_resultSet.next()){
			firstName = shippingAddress_resultSet.getString("AddressProfileName");
			addressLine1 = shippingAddress_resultSet.getString("AddressLine1");
			postalCode = shippingAddress_resultSet.getString("PostalCode");
			locale = shippingAddress_resultSet.getString("Locale");
			region = shippingAddress_resultSet.getString("Region");
			country = shippingAddress_resultSet.getString("CountryID");
			if(country.equals("40")){
				country = "CANADA"; 
			}			
		}

		DBUtil.closeConnection();

		expirationDateOfCardFromDB = expirationMonth + "/" + expirationYear; 
		shippingAddressFromDB = firstName+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID, TestConstants.CONSULTANT_PASSWORD);
		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsulatantPage.verifyConsultantPage());
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		assertTrue("Orders page has not been displayed", storeFrontOrdersPage.verifyOrdersPageIsDisplayed());
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		assertTrue("CRP Autoship template header is not as expected",storeFrontOrdersPage.verifyCRPAutoShipHeader());
		assertTrue("CRP Autoship template SV value is not as expected",storeFrontOrdersPage.verifySVValue("consultant"));
		assertTrue("Schedule Date Text is not present on the Page",storeFrontOrdersPage.verifyPresenceOfScheduleDateText());
		assertTrue("Order Status Text is not present on the Page", storeFrontOrdersPage.verifyPresenceOfOrderStatusText());
		assertTrue("CRP Total Price value is not as expected",storeFrontOrdersPage.verifyAutoshipTotalPrice(autoShipItemTotalPriceFromDB));
		assertTrue("Expiration Date of VISA Card is not as expected",storeFrontOrdersPage.verifyExpirationDate(expirationDateOfCardFromDB));
		assertTrue("SKU Value Of AutoShip Item is not as expected",storeFrontOrdersPage.verifySKUValueOfItemInOrder(autoShipItemSKUValue));
		assertTrue("Long Description of Autoship Item is not as expected",storeFrontOrdersPage.verifyDescriptionOfItemInOrder(autoShipItemDescription));
		//	assertTrue("Shipping Address is not as expected",storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));
		assertTrue("CRP Autoship template header is not as expected",storeFrontOrdersPage.verifyCRPAutoShipHeader());
		assertTrue("CRP Autoship template SV value is not as expected",storeFrontOrdersPage.verifySVValue("consultant"));		
	}

	//Hybris Phase 2-1981:Orders page UI for RC
	@Test
	public void testOrdersPageUIForRCUser_HP2_1981() throws SQLException{
		ResultSet orderNumber_resultSet = null;
		ResultSet orderstatus_resultSet = null;
		ResultSet orderGrandTotal_resultSet = null;
		ResultSet orderDate_resultSet = null;

		String orderNumberDB = null;
		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;

		orderNumber_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_QUERY, TestConstants.RCUSER_EMAIL_ID));
		orderstatus_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_STATUS_QUERY, TestConstants.RCUSER_EMAIL_ID));
		orderGrandTotal_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_GRAND_TOTAL_QUERY, TestConstants.RCUSER_EMAIL_ID));
		orderDate_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DATE_QUERY, TestConstants.RCUSER_EMAIL_ID));

		while (orderNumber_resultSet.next()) {
			orderNumberDB = orderNumber_resultSet.getString("OrderNumber");
			System.out.println(orderNumberDB);

		}
		while (orderstatus_resultSet.next()) {
			orderStatusDB = orderstatus_resultSet.getString("Name");
			System.out.println(orderStatusDB);
		}
		while (orderGrandTotal_resultSet.next()) {
			orderGrandTotalDB = orderGrandTotal_resultSet.getString("AmountToBeAuthorized");
			System.out.println(orderGrandTotalDB);
		}

		while (orderDate_resultSet.next()) {
			orderDateDB = orderDate_resultSet.getString("CompletionDate");	
			System.out.println(orderDateDB);
		}		
		DBUtil.closeConnection();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(TestConstants.RCUSER_EMAIL_ID, TestConstants.RCUSER_PASSWORD);
		assertTrue("RC User Page doesn't contain Welcome User Message",storeFrontRCUserPage.verifyRCUserPage(TestConstants.RCUSER_USERNAME));
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		assertTrue("Orders page has not been displayed", storeFrontOrdersPage.verifyOrdersPageIsDisplayed());
		assertTrue("Order Number on UI is different from DB",storeFrontOrdersPage.verifyOrderNumber(orderNumberDB));
		assertTrue("Scheduled date is not as expected for RC User",storeFrontOrdersPage.verifyScheduleDate(orderDateDB));
		assertTrue("Grand total is not as expected for RC User",storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB));
		assertTrue("Order status is not as expected for RC User",storeFrontOrdersPage.verifyOrderStatus(orderStatusDB));		
	}

	//Hybris Phase 2-4286 :: Version : 1 :: Verify order details of CRP autoship order
	@Test
	public void testOrderDetailsOfCRPAutoShipOrder_HP2_4286() throws SQLException, InterruptedException{
		ResultSet expirationYear_resultSet = null;
		//ResultSet expirationMonth_resultSet = null;
		ResultSet skuValueAndShortDesc_resultSet = null;
		ResultSet totalPriceValue_resultSet = null;

		String expirationYear = null;
		String expirationMonth = null;
		String expirationDateFromDB = null;
		String skuValueDB = null;
		String shortDescription = null;
		String totalPriceValue = null;

		skuValueAndShortDesc_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ITEMS_IN_ORDER_DESC_QUERY, TestConstants.CONSULTANT_EMAIL_ID));
		expirationYear_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_EXPIRATION_DATE_QUERY, TestConstants.CONSULTANT_EMAIL_ID));
		totalPriceValue_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ITEM_TOTAL_PRICE_QUERY, TestConstants.CONSULTANT_EMAIL_ID));

		while (expirationYear_resultSet.next()) {
			expirationYear = expirationYear_resultSet.getString("ExpYear");
			expirationMonth = expirationYear_resultSet.getString("ExpMonth");
			System.out.println(expirationYear);
			System.out.println(expirationMonth);
		}
		while(skuValueAndShortDesc_resultSet.next()){
			skuValueDB = skuValueAndShortDesc_resultSet.getString("SKU");
			shortDescription = skuValueAndShortDesc_resultSet.getString("ShortDescription");
		}
		while(totalPriceValue_resultSet.next()){
			totalPriceValue = totalPriceValue_resultSet.getString("price");
			System.out.println(totalPriceValue);
		}
		DBUtil.closeConnection();

		expirationDateFromDB = expirationMonth + "/" + expirationYear;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID, TestConstants.CONSULTANT_PASSWORD);
		assertTrue("Consultant User Page doesn't contain Welcome User Message",storeFrontConsulatantPage.verifyConsultantPage());
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		assertTrue("Orders page has not been displayed", storeFrontOrdersPage.verifyOrdersPageIsDisplayed());
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		assertTrue("CRP Autoship template header is not as expected",storeFrontOrdersPage.verifyCRPAutoShipHeader());
		assertTrue("CRP Autoship template SV value is not as expected",storeFrontOrdersPage.verifySVValue("consultant"));
		assertTrue("Schedule Date Text is not present on the Page",storeFrontOrdersPage.verifyPresenceOfScheduleDateText());
		assertTrue("Order Status Text is not present on the Page", storeFrontOrdersPage.verifyPresenceOfOrderStatusText());
		assertTrue("CRP Total Price value is not as expected",storeFrontOrdersPage.verifyCRPTotalPrice(totalPriceValue));
		assertTrue("Expiration Date of VISA Card is not as expected  ",storeFrontOrdersPage.verifyExpirationDate(expirationDateFromDB));
		assertTrue("Shipping Address is not present as expected",storeFrontOrdersPage.verifyCRPAutoShipAddressFromDB());
		assertTrue("sku value of Items In Order is not present on the page as expected",storeFrontOrdersPage.verifySKUValueOfItemInOrder(skuValueDB));
		assertTrue("item in items in Order is not present as expected",storeFrontOrdersPage.verifyDescriptionOfItemInOrder(shortDescription));
	}

	
	// phase 2 4287 -> Verify order details of consultant order
	@Test
	public void testOrdersDetailsOfConsultant_HP2_4287() throws SQLException, InterruptedException{
		ResultSet orderNumber_resultSet = null;
		ResultSet orderstatus_resultSet = null;
		ResultSet orderGrandTotal_resultSet = null;
		ResultSet orderDate_resultSet = null;

		String orderNumberDB = null;
		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;

		orderNumber_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY, TestConstants.CONSULTANT_EMAIL_ID));
		orderstatus_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY, TestConstants.CONSULTANT_EMAIL_ID));
		orderGrandTotal_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_GRANT_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY, TestConstants.CONSULTANT_EMAIL_ID));
		orderDate_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY, TestConstants.CONSULTANT_EMAIL_ID));

		while (orderNumber_resultSet.next()) {
			orderNumberDB = orderNumber_resultSet.getString("OrderNumber");			
		}
		while (orderstatus_resultSet.next()) {
			orderStatusDB = orderstatus_resultSet.getString("Name");
			System.out.println(orderStatusDB);
		}
		while (orderGrandTotal_resultSet.next()) {
			orderGrandTotalDB = orderGrandTotal_resultSet.getString("AmountToBeAuthorized");
			System.out.println(orderGrandTotalDB);
		}

		while (orderDate_resultSet.next()) {
			orderDateDB = orderDate_resultSet.getString("CompletionDate");	
			System.out.println(orderDateDB);
		}		
		DBUtil.closeConnection();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID, TestConstants.CONSULTANT_PASSWORD);
		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsulatantPage.verifyConsultantPage());
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		assertTrue("Orders page has not been displayed", storeFrontOrdersPage.verifyOrdersPageIsDisplayed());
		assertTrue("Order Number on UI is different from DB", storeFrontOrdersPage.verifyOrderNumber(orderNumberDB));
		assertTrue("Scheduled date is not as expected for Consultant", storeFrontOrdersPage.verifyScheduleDate(orderDateDB));
		assertTrue("Grand total is not as expected for Consultant", storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB));
		assertTrue("Order status is not as expected for Consultant", storeFrontOrdersPage.verifyOrderStatus(orderStatusDB));
	}
	
	
	// Hybris Phase 2-4195 : Enrolled Consultant, Has CRP/ Has Pulse, Has Submitted Orders
	@Test
	public void testEnrolledConsultantHasCRPPULSESubmittedOrders_HP2_4195() throws InterruptedException, SQLException{
		ResultSet orderNumber_resultSet = null;
		String orderNumberDB = null;
		
		orderNumber_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_QUERY, TestConstants.CONSULTANT_EMAIL_ID));
		
		while (orderNumber_resultSet.next()) {
			orderNumberDB = orderNumber_resultSet.getString("OrderNumber");
		}
		DBUtil.closeConnection();
		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID, TestConstants.CONSULTANT_PASSWORD);
		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsulatantPage.verifyConsultantPage());
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		assertTrue("Orders page has not been displayed", storeFrontOrdersPage.verifyOrdersPageIsDisplayed());
		assertTrue("Status of Order Number is not found as Submitted",storeFrontOrdersPage.verifyOrderStatusToBeSubmitted(orderNumberDB));
		storeFrontAccountInfoPage = storeFrontOrdersPage.clickOnAccountInfoFromLeftPanel();
		storeFrontOrdersAutoshipStatusPage = storeFrontAccountInfoPage.clickOnAutoShipStatus();
		assertTrue("Autoship status header is not as expected", storeFrontOrdersAutoshipStatusPage.verifyAutoShipStatusHeader());
		assertTrue("AutoShip CRP Status is not as expected", storeFrontOrdersAutoshipStatusPage.verifyAutoShipCRPStatus());
		assertTrue("AutoShip Pulse Subscription Status is not as expected", storeFrontOrdersAutoshipStatusPage.verifyAutoShipPulseSubscriptionStatus());
		
	}
	
	
}

