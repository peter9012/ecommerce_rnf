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

public class OrderVerificationTest extends RFBrandRefreshWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(OrderVerificationTest.class.getName());

	private StoreFrontBrandRefreshHomePage storeFrontBrandRefreshHomePage;
	private StoreFrontBrandRefreshConsultantPage storeFrontBrandRefreshConsultantPage;

	String RFL_DB = null;

	public OrderVerificationTest() {
		storeFrontBrandRefreshHomePage = new StoreFrontBrandRefreshHomePage(driver);
		storeFrontBrandRefreshConsultantPage = new StoreFrontBrandRefreshConsultantPage(driver);	
	}

	//Add Product to the Cart - Checkout as PC
	@Test(priority=1)
	public void testPCAdhocOrderFromCorp(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
		String billingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
		List<Map<String, Object>> randomPCList =  null;
		String pcEmailID = null;

		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_EMAILID,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "EmailAddress");
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		storeFrontBrandRefreshHomePage.clickShopSkinCareHeader();
		storeFrontBrandRefreshHomePage.selectRegimen(regimen);
		storeFrontBrandRefreshHomePage.clickAddToCartBtn();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.loginAsUserOnCheckoutPage(pcEmailID, password);
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(), "PC user not logged in successfully");
		s_assert.assertFalse(storeFrontBrandRefreshHomePage.isSignInButtonPresent(), "PC user not logged in successfully");
		storeFrontBrandRefreshHomePage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.clickCompleteOrderBtn();
		storeFrontBrandRefreshHomePage.clickOKBtnOnPopup();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
		storeFrontBrandRefreshHomePage.clickOnRodanAndFieldsLogo();
		logout();
		s_assert.assertAll();
	}

	//Adhoc order - RC corp
	@Test(priority=2)
	public void placedAdhocOrderFromComSiteForRC(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
		String billingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_EMAILID,RFL_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "EmailAddress");
		//  storeFrontBrandRefreshHomePage.clickShopSkinCareHeader();
		//  storeFrontBrandRefreshHomePage.selectRegimen(regimen);
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(regimen);
		storeFrontBrandRefreshHomePage.clickAddToCartBtn();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.loginAsUserOnCheckoutPage(rcEmailID, password);
		s_assert.assertFalse(storeFrontBrandRefreshHomePage.isSignInButtonPresent(), "RC user not logged in successfully");
		s_assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("consultantlocator"), "RC user not redirected to consultant locator after login");
		storeFrontBrandRefreshHomePage.clickContinueWithoutConsultantLink();
		storeFrontBrandRefreshHomePage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from com site for RC user.");
		storeFrontBrandRefreshHomePage.clickOnRodanAndFieldsLogo();
		logout();
		s_assert.assertAll();
	}

	//Adhoc Order - Consultant Only Products
	@Test(priority=3)
	public void AdhocOrderConsultantsOnlyProducts(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
		String billingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		storeFrontBrandRefreshConsultantPage = storeFrontBrandRefreshHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		//  storeFrontLegacyConsultantPage.clickShopSkinCareBtn();
		//  storeFrontLegacyConsultantPage.selectConsultantOnlyProductsRegimen();
		storeFrontBrandRefreshConsultantPage.mouseHoverShopSkinCareAndClickLink("CONSULTANT-ONLY PRODUCTS");
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.getCurrentURL().toLowerCase().contains("consultantsonly"), "Expected regimen name is: consultantsonly Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshConsultantPage.clickConsultantOnlyProduct(TestConstantsRFL.CONSULTANT_ONLY_BUSINESS_PROMOTION);
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

	//PC Adhoc Order – Adding new billing profile with existing user
	@Test
	public void testAddNewBillingProfileDuringPCAdhocOrderFromCorp(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.BILLING_PROFILE_FIRST_NAME;
		String billingProfileLastName = TestConstantsRFL.BILLING_PROFILE_LAST_NAME+randomNumber;
		List<Map<String, Object>> randomPCList =  null;
		String pcEmailID = null;

		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_EMAILID,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "EmailAddress");
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		storeFrontBrandRefreshHomePage.clickShopSkinCareHeader();
		storeFrontBrandRefreshHomePage.selectRegimen(regimen);
		storeFrontBrandRefreshHomePage.clickAddToCartBtn();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.loginAsUserOnCheckoutPage(pcEmailID, password);
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(), "PC user not logged in successfully");
		s_assert.assertFalse(storeFrontBrandRefreshHomePage.isSignInButtonPresent(), "PC user not logged in successfully");
		storeFrontBrandRefreshHomePage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getBillingAddressName().contains(billingProfileFirstName), "Newly created billing profile is not selected for new order.");
		storeFrontBrandRefreshHomePage.clickCompleteOrderBtn();
		storeFrontBrandRefreshHomePage.clickOKBtnOnPopup();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
		storeFrontBrandRefreshHomePage.clickOnRodanAndFieldsLogo();
		logout();
		s_assert.assertAll();
	}

	//PC Adhoc Order – with existing billing profile
	@Test
	public void testPCAdhocOrderFromCorpWithExistingBillingProfile(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomPCList =  null;
		String pcEmailID = null;
		String firstName = TestConstantsRFL.FIRST_NAME;
		String cardNumber = TestConstantsRFL.CARD_NUMBER_SECOND;
		String nameOnCard = firstName;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.BILLING_PROFILE_FIRST_NAME+randomNumber;
		String billingProfileLastName = TestConstantsRFL.BILLING_PROFILE_LAST_NAME;

		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_EMAILID,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "EmailAddress");
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		storeFrontBrandRefreshHomePage.clickShopSkinCareHeader();
		storeFrontBrandRefreshHomePage.selectRegimen(regimen);
		storeFrontBrandRefreshHomePage.clickAddToCartBtn();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.loginAsUserOnCheckoutPage(pcEmailID, password);
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(), "PC user not logged in successfully");
		s_assert.assertFalse(storeFrontBrandRefreshHomePage.isSignInButtonPresent(), "PC user not logged in successfully");
		storeFrontBrandRefreshHomePage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		//String existingBillingProfile = storeFrontBrandRefreshHomePage.getExistingBillingProfileName();
		//storeFrontBrandRefreshHomePage.clickContinueBtnOnBillingPage();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getBillingAddressName().contains(billingProfileFirstName)||storeFrontBrandRefreshHomePage.getBillingAddress().contains(addressLine1), "Existing billing profile is not selected for new order.");
		storeFrontBrandRefreshHomePage.clickCompleteOrderBtn();
		storeFrontBrandRefreshHomePage.clickOKBtnOnPopup();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
		storeFrontBrandRefreshHomePage.clickOnRodanAndFieldsLogo();
		logout();
		s_assert.assertAll();
	}

}