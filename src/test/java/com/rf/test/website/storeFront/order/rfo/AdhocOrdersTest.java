package com.rf.test.website.storeFront.order.rfo;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
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
			.getLogger(AdhocOrdersTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private String RFL_DB = null;
	private String RFO_DB = null;

	// Hybris Phase 2-1878 :: Version : 1 :: Create Adhoc Order For The Consultant Customer
	//Issue using random user card number and card expiry date issue after click on place order.
	@Test
	public void testCreateAdhocOrderConsultant() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";

		//------------------------------- Random User part is commented for now-----------------------------------------------	
		 /*randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");*/
		//---------------------------------------------------------------------------------------------------------------------
        
		consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A Hard Coded User
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnShopLink();
		storeFrontConsultantPage.clickOnAllProductsLink();
		//storeFrontConsultantPage.selectAttribute();
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

		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateSubtotal(subtotal),"AdHoc Orders Template Subtotal is not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateHandlingCharges(handlingCharges),"AdHoc Orders Template Handling charges are not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTotal(total),"AdHoc Orders Template Total is not as expected for this order");

		logout();
		s_assert.assertAll();

	}

	// Hybris Phase 2-1877 :: Version : 1 :: Create Adhoc Order For The Preferred Customer 
	//Issue using random user card number and card expiry date issue after click on place order.
	@Test
	public void testCreateAdhocOrderPC() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		
		List<Map<String, Object>> randomPCList =  null;
		String pcUserEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";

		//------------------------------- Random  User part is commented for now-----------------------------------------------	
		/*randomPCList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFO,RFO_DB);
		pcUserEmailID = (String) getValueFromQueryResult(randomPCList, "Username");*/
		//---------------------------------------------------------------------------------------------------------------------
		
		pcUserEmailID = TestConstants.PC_EMAIL_ID_TST4;//A hard code user
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnShopLink();
		storeFrontPCUserPage.clickOnAllProductsLink();
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

		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateSubtotal(subtotal),"AdHoc Orders Template Subtotal is not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateHandlingCharges(handlingCharges),"AdHoc Orders Template Handling charges are not as expected for this order");
		s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderTemplateTotal(total),"AdHoc Orders Template Total is not as expected for this order");

		logout();
		s_assert.assertAll();
	}

}
