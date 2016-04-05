package com.rf.test.website.storeFront.legacy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyHomePage;
import com.rf.test.website.RFLegacyStoreFrontWebsiteBaseTest;

public class OrderVerificationTest extends RFLegacyStoreFrontWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(OrderVerificationTest.class.getName());

	private StoreFrontLegacyHomePage storeFrontLegacyHomePage;

	//storeFrontLegacyTest:Place an adhoc order from biz PWS
	@Test
	public void testPlaceAnAdhocOrderFromBizPWS(){
		String consultantEmailID = TestConstantsRFL.USERNAME_CONSULTANT;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.openPWSSite(TestConstantsRFL.BIZ_PWS);
		storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnPWSSite(),"consultant is not logged in successfully");
		storeFrontLegacyHomePage.clickShopSkinCareHeader();
		storeFrontLegacyHomePage.selectRegimenAfterLogin("REDEFINE");
		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertAll();
	}

	//storeFrontLegacyTest:Place an adhoc order from com PWS
	@Test
	public void testPlaceAnAdhocOrderFromComPWS(){
		String consultantEmailID = TestConstantsRFL.USERNAME_CONSULTANT;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.openPWSSite(TestConstantsRFL.COM_PWS);
		storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnPWSSite(),"consultant is not logged in successfully");
		storeFrontLegacyHomePage.clickShopSkinCareHeader();
		storeFrontLegacyHomePage.selectRegimenAfterLogin("REDEFINE");
		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertAll();
	}

	//Placed adhoc order for PC from biz site after login 
	@Test
	public void testPlacedAdhocOrderFromBizSiteForPC(){
		String username = TestConstantsRFL.USERNAME_PC;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.openPWSSite(TestConstantsRFL.BIZ_PWS);
		storeFrontLegacyHomePage.loginAsPCUser(username, password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnPWSSite(), "PC user not logged in successfully");
		storeFrontLegacyHomePage.clickShopSkinCareHeader();
		storeFrontLegacyHomePage.selectRegimenAfterLogin(regimen);
		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		storeFrontLegacyHomePage.clickOKBtnOnPopup();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertAll();
	}

	//Placed adhoc order for PC from com site after login 
	@Test
	public void testPlacedAdhocOrderFromComSiteForPC(){
		String username = TestConstantsRFL.USERNAME_PC;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.openPWSSite(TestConstantsRFL.COM_PWS);
		storeFrontLegacyHomePage.loginAsPCUser(username, password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnPWSSite(), "PC user not logged in successfully");
		storeFrontLegacyHomePage.clickShopSkinCareHeader();
		storeFrontLegacyHomePage.selectRegimenAfterLogin(regimen);
		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		storeFrontLegacyHomePage.clickOKBtnOnPopup();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertAll();
	}

	//RC Adhoc Order from corp site.
	@Test
	public void testRCAdhocOrder(){
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		String username = TestConstantsRFL.USERNAME_RC;
		//Login as RC and place adhoc order.
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.loginAsRCUser(username, password);
		storeFrontLegacyHomePage.clickProductsBtn();
		storeFrontLegacyHomePage.selectRegimen(regimen);
		storeFrontLegacyHomePage.clickAddToCartBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueWithoutConsultantLink();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
		s_assert.assertAll();
	}

	//Placed adhoc order for RC from com site after login 
	@Test
	public void placedAdhocOrderFromComSiteForRC(){
		String username = TestConstantsRFL.USERNAME_RC;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.openPWSSite(TestConstantsRFL.COM_PWS);
		storeFrontLegacyHomePage.loginAsRCUser(username, password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnPWSSite(), "RC user not logged in successfully");
		storeFrontLegacyHomePage.clickShopSkinCareHeader();
		storeFrontLegacyHomePage.selectRegimenAfterLogin(regimen);
		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from com site for RC user.");
		s_assert.assertAll();
	}

	//Placed adhoc order for RC from biz site after login 
	@Test
	public void placedAdhocOrderFromBizSiteForRC(){
		String username = TestConstantsRFL.USERNAME_RC;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.openPWSSite(TestConstantsRFL.BIZ_PWS);
		storeFrontLegacyHomePage.loginAsRCUser(username, password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnPWSSite(), "RC user not logged in successfully");
		storeFrontLegacyHomePage.clickShopSkinCareHeader();
		storeFrontLegacyHomePage.selectRegimenAfterLogin(regimen);
		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from biz site for RC user.");
		s_assert.assertAll();
	}
}
