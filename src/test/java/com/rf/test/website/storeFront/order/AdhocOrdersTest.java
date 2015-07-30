package com.rf.test.website.storeFront.order;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontCartAutoShipPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class AdhocOrdersTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(ViewOrderDetailsTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private String RFL_DB = null;
	private String RFO_DB = null;

	// Hybris Phase 2-1878 :: Version : 1 :: Create Adhoc Order For The Consultant Customer
	@Test(enabled=false)
	public void testCreateAdhocOrderConsultant() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
//		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
//		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		consultantEmailID = "nishazenoff@gmail.com";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
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
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		System.out.println("BillingAddress ="+BillingAddress);
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-1877 :: Version : 1 :: Create Adhoc Order For The Preferred Customer 
	@Test(enabled=false)
	public void testCreateAdhocOrderPC() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
//		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_USER_EMAIL_ID_RFL,RFL_DB);
//		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "EmailAddress");
		pcUserEmailID = "wigginsk@comcast.net";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, TestConstants.PC_USER_PASSWORD_RFL);
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
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		logout();
		s_assert.assertAll();
	}

}

