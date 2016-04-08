package com.rf.test.website.storeFront.legacy.smoke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyConsultantPage;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyHomePage;
import com.rf.test.website.RFLegacyStoreFrontWebsiteBaseTest;

public class OrderVerificationTest extends RFLegacyStoreFrontWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(OrderVerificationTest.class.getName());

	private StoreFrontLegacyHomePage storeFrontLegacyHomePage;
	private StoreFrontLegacyConsultantPage storeFrontLegacyConsultantPage;

	//	//Adhoc Order - Consultant corp
	//	@Test(enabled=true)
	//	public void testConsultantAdhocOrder(){
	//		String username = TestConstantsRFL.USERNAME_CONSULTANT;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsConsultant(username,password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickProductsBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartBtn();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();  
	//	}

	//Adhoc Order - PC corp
	@Test(enabled=true)
	public void testPCAdhocOrderFromCorp(){
		String username = TestConstantsRFL.USERNAME_PC;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.loginAsPCUser(username, password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
		storeFrontLegacyHomePage.clickProductsBtn();
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		storeFrontLegacyHomePage.clickAddToCartBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		storeFrontLegacyHomePage.clickOKBtnOnPopup();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
		s_assert.assertAll();
	}

	//Adhoc Order - Consultant Only Products
	@Test(enabled=true)
	public void AdhocOrderConsultantsOnlyProducts(){
		String consultantEmailID = TestConstantsRFL.USERNAME_CONSULTANT;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyConsultantPage = storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyConsultantPage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		storeFrontLegacyConsultantPage.clickProductsBtn();
		storeFrontLegacyConsultantPage.selectConsultantOnlyProductsRegimen();
		s_assert.assertTrue(storeFrontLegacyConsultantPage.getCurrentURL().toLowerCase().contains("consultantsonly"), "Expected regimen name is: consultantsonly Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		storeFrontLegacyConsultantPage.clickConsultantOnlyProduct(TestConstantsRFL.CONSULTANT_ONLY_BUSINESS_PROMOTION);
		storeFrontLegacyConsultantPage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
		storeFrontLegacyConsultantPage.clickCheckoutBtn();
		storeFrontLegacyConsultantPage.clickContinueBtn();
		storeFrontLegacyConsultantPage.clickContinueBtnOnBillingPage();
		storeFrontLegacyConsultantPage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyConsultantPage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertTrue(storeFrontLegacyConsultantPage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		s_assert.assertAll();
	}
}