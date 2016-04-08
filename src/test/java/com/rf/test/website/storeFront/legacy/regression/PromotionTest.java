package com.rf.test.website.storeFront.legacy.regression;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyConsultantPage;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyHomePage;
import com.rf.test.website.RFLegacyStoreFrontWebsiteBaseTest;

public class PromotionTest extends RFLegacyStoreFrontWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(PromotionTest.class.getName());

	private StoreFrontLegacyHomePage storeFrontLegacyHomePage;
	private StoreFrontLegacyConsultantPage storeFrontLegacyConsultantPage;

	//Consultants Only - buy business promotion 
	@Test(enabled=true)
	public void testConsultantsOnlyBuyBusinessPromotion(){
		String consultantEmailID = TestConstantsRFL.USERNAME_CONSULTANT;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		storeFrontLegacyHomePage.clickProductsBtn();
		storeFrontLegacyHomePage.selectPromotionRegimen();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("promotions"), "Expected regimen name is: promotions Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		storeFrontLegacyHomePage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertTrue(storeFrontLegacyHomePage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		s_assert.assertAll();
	}

	//Consultants Only -buy event support pack 
	@Test(enabled=true)
	public void consultantsOnlyBuyEventSupportPack(){
		String consultantEmailID = TestConstantsRFL.USERNAME_CONSULTANT;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyConsultantPage = storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyConsultantPage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		storeFrontLegacyConsultantPage.clickProductsBtn();
		storeFrontLegacyConsultantPage.selectConsultantOnlyProductsRegimen();
		s_assert.assertTrue(storeFrontLegacyConsultantPage.getCurrentURL().toLowerCase().contains("consultantsonly"), "Expected regimen name is: consultantsonly Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		storeFrontLegacyConsultantPage.clickConsultantOnlyProduct(TestConstantsRFL.CONSULTANT_ONLY_EVENT_SUPPORT);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("event"), "Expected url contains is: Event but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		storeFrontLegacyConsultantPage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
		storeFrontLegacyConsultantPage.clickCheckoutBtn();
		storeFrontLegacyConsultantPage.clickContinueBtn();
		storeFrontLegacyConsultantPage.clickContinueBtnOnBillingPage();
		storeFrontLegacyConsultantPage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyConsultantPage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertTrue(storeFrontLegacyConsultantPage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		s_assert.assertAll();
	}

	//Consultants Only -buy product promotion 
	@Test(enabled=true)
	public void consultantsOnlyBuyProductPromotion(){
		String consultantEmailID = TestConstantsRFL.USERNAME_CONSULTANT;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyConsultantPage = storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyConsultantPage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		storeFrontLegacyConsultantPage.clickProductsBtn();
		storeFrontLegacyConsultantPage.selectConsultantOnlyProductsRegimen();
		s_assert.assertTrue(storeFrontLegacyConsultantPage.getCurrentURL().toLowerCase().contains("consultantsonly"), "Expected regimen name is: consultantsonly Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		storeFrontLegacyConsultantPage.clickConsultantOnlyProduct(TestConstantsRFL.CONSULTANT_ONLY_PRODUCT_PROMOTION);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("promotion"), "Expected url contains is: promotion but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		storeFrontLegacyConsultantPage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
		storeFrontLegacyConsultantPage.clickCheckoutBtn();
		storeFrontLegacyConsultantPage.clickContinueBtn();
		storeFrontLegacyConsultantPage.clickContinueBtnOnBillingPage();
		storeFrontLegacyConsultantPage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyConsultantPage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertTrue(storeFrontLegacyConsultantPage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		s_assert.assertAll();
	}

	//The Events section Upcoming events is displayed
	@Test(enabled=true)
	public void testEventsSectionUpcomingEventsIsDisplayed(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBusinessSystemBtn();
		//verify the sub-menu title under the title business system?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateEnrollNowLinkPresent(),"Enroll Now Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateMeetOurCommunityLinkPresent(),"Meet Our Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateEventsLinkPresent(),"Events Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateIncomeIllustratorLinkPresent(),"Income Illustrator Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateProgramsAndIncentivesLinkPresent(),"ProgramIncentives Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateWhyRFLinkPresent(),"Why RF Link is not present");
		//Navigate to Events Section
		storeFrontLegacyHomePage.clickEventsLinkUnderBusinessSystem();
		//verify UPCOMING EVENTS is displayed?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateUpcomingEventsLinkIsPresent(),"Upcoming Events Link is not present");
		s_assert.assertAll();
	}
}