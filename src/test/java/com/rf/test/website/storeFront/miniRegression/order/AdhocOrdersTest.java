package com.rf.test.website.storeFront.miniRegression.order;

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
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class AdhocOrdersTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AdhocOrdersTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private String RFO_DB = null;

	//Hybris Project 1878-Mini Regression-Create Adhoc Orders for Consultant
	@Test
	public void testCreateAdhocOrderConsultantReg() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_CONSULTANT_EMAIL_ID_RFO,RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.PASSWORD);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnShopLink();
		storeFrontConsultantPage.clickOnAllProductsLink();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		storeFrontUpdateCartPage.clickOnConfirmationOK();
		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		System.out.println("subtotal ="+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		System.out.println("deliveryCharges ="+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		System.out.println("handlingCharges ="+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		System.out.println("tax ="+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		System.out.println("total ="+total);
		String totalSV = storeFrontUpdateCartPage.getTotalSV();
		System.out.println("totalSV ="+totalSV);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		System.out.println("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.waitForSaveShippingInfoBtn();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		System.out.println("BillingAddress ="+BillingAddress);
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		//select newly created adhoc order and verify the details
		storeFrontOrdersPage.clickOnFirstAdhocOrder();
		s_assert.assertTrue(storeFrontOrdersPage.orderDetails_getSubTotal().trim().equalsIgnoreCase(subtotal), "subTotal should be same");
		s_assert.assertTrue(storeFrontOrdersPage.orderDetails_ShippingCharges().trim().equalsIgnoreCase(deliveryCharges), "shipping charges should be same");
		s_assert.assertTrue(storeFrontOrdersPage.orderDetails_HandlingCharges().trim().equalsIgnoreCase(handlingCharges), "Handling charges should be same");
		s_assert.assertTrue(storeFrontOrdersPage.orderDetails_grandTotal().trim().equalsIgnoreCase(total), "Grand Total charges should be same");
		s_assert.assertTrue(storeFrontOrdersPage.orderDetails_getTax().trim().equalsIgnoreCase(tax), " Tax charges should be same");
		s_assert.assertTrue(shippingMethod.trim().contains(storeFrontOrdersPage.orderDetails_getShippingMethodName().trim()), "shipping Method name should match");
		logout();
		s_assert.assertAll();
	}
	//Hybris Project 1878-Mini Regression-Create Adhoc Orders for PC User
	@Test
	public void testCreateAdhocOrderPCReg() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_PC_USER_EMAIL_ID_RFO,RFO_DB);
		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "Username");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, TestConstants.PASSWORD);
		s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnShopLink();
		storeFrontPCUserPage.clickOnAllProductsLink();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		storeFrontUpdateCartPage.clickOnConfirmationOK();
		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		String tax = storeFrontUpdateCartPage.getTax();
		String total = storeFrontUpdateCartPage.getTotal();
		String totalSV = storeFrontUpdateCartPage.getTotalSV();
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		storeFrontUpdateCartPage.waitForSaveShippingInfoBtn();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		storeFrontUpdateCartPage.waitForpaymentNextBtn();  
		Thread.sleep(3500);
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		//select newly created adhoc order and verify the details
		storeFrontOrdersPage.clickOnFirstAdhocOrder();
		//s_assert.assertTrue(storeFrontOrdersPage.orderDetails_getTotalSV().equalsIgnoreCase(totalSV), "TotalSV should be same");
		s_assert.assertTrue(storeFrontOrdersPage.orderDetails_getSubTotal().trim().equalsIgnoreCase(subtotal), "subTotal should be same");
		s_assert.assertTrue(storeFrontOrdersPage.orderDetails_ShippingCharges().trim().equalsIgnoreCase(deliveryCharges), "shipping charges should be same");
		s_assert.assertTrue(storeFrontOrdersPage.orderDetails_HandlingCharges().trim().equalsIgnoreCase(handlingCharges), "Handling charges should be same");
		s_assert.assertTrue(storeFrontOrdersPage.orderDetails_grandTotal().trim().equalsIgnoreCase(total), "Grand Total charges should be same");
		s_assert.assertTrue(storeFrontOrdersPage.orderDetails_getTax().trim().equalsIgnoreCase(tax), " Tax charges should be same");
		s_assert.assertTrue(shippingMethod.trim().contains(storeFrontOrdersPage.orderDetails_getShippingMethodName().trim()), "shipping Method name should match");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project 1878-Mini Regression-Create Adhoc Orders for RC User
	 @Test
	 public void testCreateAdhocOrderRCReg() throws InterruptedException{
	   RFO_DB = driver.getDBNameRFO();
	  List<Map<String, Object>> randomRCUserList =  null;
	  String rcuserEmailId = null;
	  randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_PC_USER_EMAIL_ID_RFO,RFO_DB);
	  rcuserEmailId = (String) getValueFromQueryResult(randomRCUserList, "Username");
	  storeFrontHomePage = new StoreFrontHomePage(driver);
	  storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
	  storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcuserEmailId, TestConstants.PASSWORD);
	  s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcuserEmailId),"RC User Page doesn't contain Welcome User Message");
	  logger.info("login is successful");
	  storeFrontRCUserPage.clickOnShopLink();
	  storeFrontRCUserPage.clickOnAllProductsLink();
	  storeFrontUpdateCartPage.clickOnBuyNowButton();
	  storeFrontUpdateCartPage.clickOnCheckoutButton();
	  String subtotal = storeFrontUpdateCartPage.getSubtotal();
	  String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
	  String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
	  String tax = storeFrontUpdateCartPage.getTax();
	  String total = storeFrontUpdateCartPage.getTotal();
	  String totalSV = storeFrontUpdateCartPage.getTotalSV();
	  String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
	  storeFrontUpdateCartPage.clickOnContinueWithoutSponsorLink();
	  //storeFrontUpdateCartPage.waitForSaveShippingInfoBtn();
	  storeFrontUpdateCartPage.clickOnNextButtonAfterSelectingSponsor();
	  storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
	  String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
	  storeFrontUpdateCartPage.waitForpaymentNextBtn();  
	  Thread.sleep(3500);
	  storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
	  storeFrontUpdateCartPage.clickPlaceOrderBtn();
	  storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
	  storeFrontRCUserPage.clickOnWelcomeDropDown();
	  storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
	  s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
	  //select newly created adhoc order and verify the details
	  storeFrontOrdersPage.clickOnFirstAdhocOrder();
	  s_assert.assertTrue(storeFrontOrdersPage.orderDetails_getSubTotal().trim().equalsIgnoreCase(subtotal), "subTotal should be same");
	  s_assert.assertTrue(storeFrontOrdersPage.orderDetails_ShippingCharges().trim().equalsIgnoreCase(deliveryCharges), "shipping charges should be same");
	  s_assert.assertTrue(storeFrontOrdersPage.orderDetails_HandlingCharges().trim().equalsIgnoreCase(handlingCharges), "Handling charges should be same");
	  s_assert.assertTrue(storeFrontOrdersPage.orderDetails_grandTotal().trim().equalsIgnoreCase(total), "Grand Total charges should be same");
	  s_assert.assertTrue(storeFrontOrdersPage.orderDetails_getTax().trim().equalsIgnoreCase(tax), " Tax charges should be same");
	  s_assert.assertTrue(shippingMethod.trim().contains(storeFrontOrdersPage.orderDetails_getShippingMethodName().trim()), "shipping Method name should match");
	  logout();
	  s_assert.assertAll();
	 }
}
