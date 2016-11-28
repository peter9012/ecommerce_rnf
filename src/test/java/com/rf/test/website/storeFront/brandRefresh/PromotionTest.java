package com.rf.test.website.storeFront.brandRefresh;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshConsultantPage;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshHomePage;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshPCUserPage;
import com.rf.test.website.RFBrandRefreshWebsiteBaseTest;

public class PromotionTest extends RFBrandRefreshWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(PromotionTest.class.getName());

	private StoreFrontBrandRefreshHomePage storeFrontBrandRefreshHomePage;
	private StoreFrontBrandRefreshConsultantPage storeFrontBrandRefreshConsultantPage;
	private StoreFrontBrandRefreshPCUserPage storeFrontBrandRefreshPCUserPage;

	private String RFL_DB = null;

	public PromotionTest() {
		storeFrontBrandRefreshHomePage = new StoreFrontBrandRefreshHomePage(driver);
		storeFrontBrandRefreshConsultantPage = new StoreFrontBrandRefreshConsultantPage(driver);
		storeFrontBrandRefreshPCUserPage = new StoreFrontBrandRefreshPCUserPage(driver);
	}

	//Consultants Only - buy business promotion 
	@Test(enabled=false)//test no longer valid
	public void testConsultantsOnlyBuyBusinessPromotion(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.BILLING_PROFILE_FIRST_NAME;
		String billingProfileLastName = TestConstantsRFL.BILLING_PROFILE_LAST_NAME+randomNumber;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = TestConstantsRFL.FIRST_NAME;;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		storeFrontBrandRefreshHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		//  storeFrontBrandRefreshHomePage.clickShopSkinCareBtn();
		//  storeFrontBrandRefreshHomePage.selectPromotionRegimen();
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink("Promotions");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("promotions"), "Expected regimen name is: promotions Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshHomePage.clickAddToCartBtn();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		storeFrontBrandRefreshHomePage.clickOnRodanAndFieldsLogo();
		logout();
		s_assert.assertAll();
	}

	//Consultants Only -buy event support pack 
	@Test(enabled=true)//needs updation
	public void consultantsOnlyBuyEventSupportPack(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.BILLING_PROFILE_FIRST_NAME;
		String billingProfileLastName = TestConstantsRFL.BILLING_PROFILE_LAST_NAME+randomNumber;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = TestConstantsRFL.FIRST_NAME;;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		storeFrontBrandRefreshConsultantPage = storeFrontBrandRefreshHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		//  storeFrontBrandRefreshConsultantPage.clickShopSkinCareBtn();
		//  storeFrontBrandRefreshConsultantPage.selectConsultantOnlyProductsRegimen();
		storeFrontBrandRefreshConsultantPage.mouseHoverShopSkinCareAndClickLink("CONSULTANT-ONLY PRODUCTS");
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.getCurrentURL().toLowerCase().contains("consultantsonly"), "Expected regimen name is: consultantsonly Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshConsultantPage.clickConsultantOnlyProduct(TestConstantsRFL.CONSULTANT_ONLY_EVENT_SUPPORT);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("event"), "Expected url contains is: Event but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshConsultantPage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
		storeFrontBrandRefreshConsultantPage.clickCheckoutBtn();
		storeFrontBrandRefreshConsultantPage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshConsultantPage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		storeFrontBrandRefreshHomePage.clickOnRodanAndFieldsLogo();
		logout();
		s_assert.assertAll();
	}

	//Consultants Only -buy product promotion 
	@Test(enabled=true)//needs updation
	public void testconsultantsOnlyBuyProductPromotion(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.BILLING_PROFILE_FIRST_NAME;
		String billingProfileLastName = TestConstantsRFL.BILLING_PROFILE_LAST_NAME+randomNumber;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = TestConstantsRFL.FIRST_NAME;;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		storeFrontBrandRefreshConsultantPage = storeFrontBrandRefreshHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		//  storeFrontBrandRefreshConsultantPage.clickShopSkinCareBtn();
		//  storeFrontBrandRefreshConsultantPage.selectConsultantOnlyProductsRegimen();
		storeFrontBrandRefreshConsultantPage.mouseHoverShopSkinCareAndClickLink("CONSULTANT-ONLY PRODUCTS");
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.getCurrentURL().toLowerCase().contains("consultantsonly"), "Expected regimen name is: consultantsonly Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshConsultantPage.clickConsultantOnlyProduct(TestConstantsRFL.CONSULTANT_ONLY_PRODUCT_PROMOTION);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("promotion"), "Expected url contains is: promotion but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshConsultantPage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
		storeFrontBrandRefreshConsultantPage.clickCheckoutBtn();
		storeFrontBrandRefreshConsultantPage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshConsultantPage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		storeFrontBrandRefreshHomePage.clickOnRodanAndFieldsLogo();
		logout();
		s_assert.assertAll();
	}

	//The Events section Upcoming events is displayed
	@Test(enabled=false)
	public void testEventsSectionUpcomingEventsIsDisplayed(){		
		storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		//verify the sub-menu title under the title business system?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateEnrollNowLinkPresent(),"Enroll Now Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateMeetOurCommunityLinkPresent(),"Meet Our Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateEventsLinkPresent(),"Events Link is not present");
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateIncomeIllustratorLinkPresent(),"Income Illustrator Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateProgramsAndIncentivesLinkPresent(),"ProgramIncentives Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateWhyRFLinkPresent(),"Why RF Link is not present");
		//Navigate to Events Section
		storeFrontBrandRefreshHomePage.clickEventsLinkUnderBusinessSystem();
		//verify UPCOMING EVENTS is displayed?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateUpcomingEventsLinkIsPresent(),"Upcoming Events Link is not present");
		s_assert.assertAll();
	}
}