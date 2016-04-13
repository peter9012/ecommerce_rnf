package com.rf.test.website.storeFront.legacy.regression;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyConsultantPage;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyHomePage;
import com.rf.test.website.RFLegacyStoreFrontWebsiteBaseTest;

public class OrderVerificationTest extends RFLegacyStoreFrontWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(OrderVerificationTest.class.getName());

	private StoreFrontLegacyHomePage storeFrontLegacyHomePage;
	private StoreFrontLegacyConsultantPage storeFrontLegacyConsultantPage;
	String RFL_DB = null;

	//	//Adhoc Order - Consultant corp
	//	@Test
	//	public void testConsultantAdhocOrder(){
	//		String username = TestConstantsRFL.USERNAME_CONSULTANT;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsConsultant(username,password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
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
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_EMAILID,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "EmailAddress");
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		storeFrontLegacyHomePage.clickShopSkinCareHeader();
		storeFrontLegacyHomePage.selectRegimen(regimen);
		storeFrontLegacyHomePage.clickAddToCartBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.loginAsUserOnCheckoutPage(pcEmailID, password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(), "PC user not logged in successfully");
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickChangeBillingInformationBtn();
		storeFrontLegacyHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickUseThisBillingInformationBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		storeFrontLegacyHomePage.clickOKBtnOnPopup();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
		s_assert.assertAll();
	}

	//Adhoc order - RC corp
	@Test(enabled=true)
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
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_EMAILID,RFL_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "EmailAddress");
		storeFrontLegacyHomePage.clickShopSkinCareHeader();
		storeFrontLegacyHomePage.selectRegimen(regimen);
		storeFrontLegacyHomePage.clickAddToCartBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.loginAsUserOnCheckoutPage(rcEmailID, password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(), "RC user not logged in successfully");
		storeFrontLegacyHomePage.clickContinueWithoutConsultantLink();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickChangeBillingInformationBtn();
		storeFrontLegacyHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickUseThisBillingInformationBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from com site for RC user.");
		s_assert.assertAll();
	}

	//Adhoc Order - Consultant Only Products
	@Test(enabled=true)
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
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyConsultantPage = storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyConsultantPage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		storeFrontLegacyConsultantPage.clickShopSkinCareBtn();
		storeFrontLegacyConsultantPage.selectConsultantOnlyProductsRegimen();
		s_assert.assertTrue(storeFrontLegacyConsultantPage.getCurrentURL().toLowerCase().contains("consultantsonly"), "Expected regimen name is: consultantsonly Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		storeFrontLegacyConsultantPage.clickConsultantOnlyProduct(TestConstantsRFL.CONSULTANT_ONLY_BUSINESS_PROMOTION);
		storeFrontLegacyConsultantPage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
		storeFrontLegacyConsultantPage.clickCheckoutBtn();
		storeFrontLegacyConsultantPage.clickContinueBtn();
		storeFrontLegacyHomePage.clickChangeBillingInformationBtn();
		storeFrontLegacyHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickUseThisBillingInformationBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		storeFrontLegacyConsultantPage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyConsultantPage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertTrue(storeFrontLegacyConsultantPage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		s_assert.assertAll();
	}
	//	//Redefine Adhoc Order - Consultant corp (S.No 4)
	//	@Test
	//	public void testConsultantRedefineAdhocOrder(){
	//		String username = TestConstantsRFL.USERNAME_CONSULTANT;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsConsultant(username,password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
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
	//
	//	//Reverse Adhoc Order - Consultant corp (S.No 8)
	//	@Test
	//	public void testConsultantReverseAdhocOrder(){
	//		String username = TestConstantsRFL.USERNAME_CONSULTANT;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsConsultant(username,password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
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
	//
	//	//Reverse Adhoc Order - PC corp(S.No 9)
	//	@Test
	//	public void testPlacedAdhocOrderFromCorpSiteUsingReverseRegimenForPC(){
	//		String username = TestConstantsRFL.USERNAME_PC;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsPCUser(username, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartBtn();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		storeFrontLegacyHomePage.clickOKBtnOnPopup();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();
	//	}
	//
	//	//Reverse adhoc order - RC corp (S.No 10)
	//	@Test
	//	public void placedAdhocOrderFromComSiteForRC(){
	//		String username = TestConstantsRFL.USERNAME_RC;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
	//
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.openPWSSite(TestConstantsRFL.COM_PWS);
	//		storeFrontLegacyHomePage.loginAsRCUser(username, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnPWSSite(), "RC user not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareHeader();
	//		storeFrontLegacyHomePage.selectRegimenAfterLogin(regimen);
	//		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
	//		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from com site for RC user.");
	//		s_assert.assertAll();
	//	}
	//
	//	//Unblemish Adhoc Order- Consultant corp (S.No 12)
	//	@Test(enabled=false)//WIP
	//	public void testConsultantUnblemishAdhocOrder(){
	//		String username = TestConstantsRFL.USERNAME_CONSULTANT;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_UNBLEMISH;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsConsultant(username,password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
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
	//
	//	//Unblemish adhoc order - PC corp(S.No 13)
	//	@Test(enabled=false)//WIP
	//	public void testPlacedAdhocOrderFromCorpSiteUsingUnblemishRegimenForPC(){
	//		String username = TestConstantsRFL.USERNAME_PC;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_UNBLEMISH;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsPCUser(username, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartBtn();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		storeFrontLegacyHomePage.clickOKBtnOnPopup();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();
	//	}
	//
	//	//Unblemish Adhoc Order - RC corp (S.No 14)
	//	@Test(enabled=false)//WIP
	//	public void testRCAdhocOrderUsingUnblemishRegimen(){
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_UNBLEMISH;
	//		String username = TestConstantsRFL.USERNAME_RC;
	//		//Login as RC and place adhoc order.
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsRCUser(username, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartBtn();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueWithoutConsultantLink();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();
	//	}
	//
	//	//Soothe Adhoc Order- Consultant Corp (S.No 16)
	//	@Test(enabled=false)//WIP
	//	public void testConsultantSootheAdhocOrder(){
	//		String username = TestConstantsRFL.USERNAME_CONSULTANT;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_SOOTHE;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsConsultant(username,password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
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
	//
	//	//Soothe Adhoc order - PC Corp (S.no 17)
	//	@Test(enabled=false)//WIP
	//	public void testPlacedAdhocOrderFromCorpSiteUsingSootheRegimenForPC(){
	//		String username = TestConstantsRFL.USERNAME_PC;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_SOOTHE;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsPCUser(username, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartBtn();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		storeFrontLegacyHomePage.clickOKBtnOnPopup();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();
	//	}
	//
	//
	//	//Soothe Adhoc Order - RC corp (S.No 18)
	//	@Test(enabled=false)//WIP
	//	public void testRCAdhocOrderUsingSootheRegimen(){
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_SOOTHE;
	//		String username = TestConstantsRFL.USERNAME_RC;
	//		//Login as RC and place adhoc order.
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsRCUser(username, password);
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartBtn();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueWithoutConsultantLink();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();
	//	}
	//
	//	//Essentials Adhoc Order - Consultant corp (S.no 20)
	//	@Test(enabled=false)//WIP
	//	public void testConsultantEssentialsAdhocOrder(){
	//		String username = TestConstantsRFL.USERNAME_CONSULTANT;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_ESSENTIALS;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsConsultant(username,password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();
	//	}
	//
	//	//Essentials Adhoc order - PC corp (S.No 21)
	//	@Test(enabled=false)//WIP
	//	public void testPlacedAdhocOrderFromCorpSiteUsingEssentialsRegimenForPC(){
	//		String username = TestConstantsRFL.USERNAME_PC;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_ESSENTIALS;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsPCUser(username, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		storeFrontLegacyHomePage.clickOKBtnOnPopup();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();
	//	}
	//
	//	//Essentials Adhoc Order - RC corp (S.No 22)
	//	@Test(enabled=false)//WIP
	//	public void testRCAdhocOrderUsingEssentialsRegimen(){
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_ESSENTIALS;
	//		String username = TestConstantsRFL.USERNAME_RC;
	//		//Login as RC and place adhoc order.
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsRCUser(username, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"RC is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueWithoutConsultantLink();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();
	//	}
	//
	//	//Enhancements Adhoc Order - Consultant Corp(S.No 24)
	//	@Test(enabled=false)//WIP
	//	public void testConsultantEnhancementsAdhocOrder(){
	//		String username = TestConstantsRFL.USERNAME_CONSULTANT;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_ENHANCEMENTS;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsConsultant(username,password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();
	//	}
	//
	//	//Enhancements adhoc order - PC corp (S.No 25)
	//	@Test(enabled=false)//WIP
	//	public void testPlacedAdhocOrderFromCorpSiteUsingEnhancementsRegimenForPC(){
	//		String username = TestConstantsRFL.USERNAME_PC;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_ENHANCEMENTS;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsPCUser(username, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"consultant is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		storeFrontLegacyHomePage.clickOKBtnOnPopup();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();
	//	}
	//
	//	//Enhancements Adhoc Order - RC corp (S.no 26)
	//	@Test(enabled=false)//WIP
	//	public void testRCAdhocOrderUsingEnhancementRegimen(){
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_ENHANCEMENTS;
	//		String username = TestConstantsRFL.USERNAME_RC;
	//		//Login as RC and place adhoc order.
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.loginAsRCUser(username, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"RC is not logged in successfully");
	//		storeFrontLegacyHomePage.clickShopSkinCareBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
	//		storeFrontLegacyHomePage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickContinueWithoutConsultantLink();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
	//		s_assert.assertAll();
	//	}
	//
	//
	//
	//
	//	//
	//	//	//Consultant Place an adhoc order from biz PWS
	//	//	@Test
	//	//	public void testPlaceAnAdhocOrderFromBizPWS(){
	//	//		String consultantEmailID = TestConstantsRFL.USERNAME_CONSULTANT;
	//	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//	//		storeFrontLegacyHomePage.openPWSSite(TestConstantsRFL.BIZ_PWS);
	//	//		storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
	//	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnPWSSite(),"consultant is not logged in successfully");
	//	//		storeFrontLegacyHomePage.clickShopSkinCareHeader();
	//	//		storeFrontLegacyHomePage.selectRegimenAfterLogin("REDEFINE");
	//	//		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
	//	//		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
	//	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//	//		storeFrontLegacyHomePage.clickContinueBtn();
	//	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
	//	//		s_assert.assertAll();
	//	//	}
	//	//
	//	//	//Consultant Place an adhoc order from com PWS
	//	//	@Test
	//	//	public void testPlaceAnAdhocOrderFromComPWS(){
	//	//		String consultantEmailID = TestConstantsRFL.USERNAME_CONSULTANT;
	//	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//	//		storeFrontLegacyHomePage.openPWSSite(TestConstantsRFL.COM_PWS);
	//	//		storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
	//	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnPWSSite(),"consultant is not logged in successfully");
	//	//		storeFrontLegacyHomePage.clickShopSkinCareHeader();
	//	//		storeFrontLegacyHomePage.selectRegimenAfterLogin("REDEFINE");
	//	//		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
	//	//		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
	//	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//	//		storeFrontLegacyHomePage.clickContinueBtn();
	//	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
	//	//		s_assert.assertAll();
	//	//	}
	//	//
	//	//	//Placed adhoc order for PC from biz site after login 
	//	//	@Test
	//	//	public void testPlacedAdhocOrderFromBizSiteForPC(){
	//	//		String username = TestConstantsRFL.USERNAME_PC;
	//	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
	//	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//	//		storeFrontLegacyHomePage.openPWSSite(TestConstantsRFL.BIZ_PWS);
	//	//		storeFrontLegacyHomePage.loginAsPCUser(username, password);
	//	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnPWSSite(), "PC user not logged in successfully");
	//	//		storeFrontLegacyHomePage.clickShopSkinCareHeader();
	//	//		storeFrontLegacyHomePage.selectRegimenAfterLogin(regimen);
	//	//		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
	//	//		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
	//	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//	//		storeFrontLegacyHomePage.clickContinueBtn();
	//	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//	//		storeFrontLegacyHomePage.clickOKBtnOnPopup();
	//	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
	//	//		s_assert.assertAll();
	//	//	}
	//	//
	//	//	//Placed adhoc order for RC from biz site after login 
	//	//	@Test
	//	//	public void placedAdhocOrderFromBizSiteForRC(){
	//	//		String username = TestConstantsRFL.USERNAME_RC;
	//	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
	//	//
	//	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//	//		storeFrontLegacyHomePage.openPWSSite(TestConstantsRFL.BIZ_PWS);
	//	//		storeFrontLegacyHomePage.loginAsRCUser(username, password);
	//	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnPWSSite(), "RC user not logged in successfully");
	//	//		storeFrontLegacyHomePage.clickShopSkinCareHeader();
	//	//		storeFrontLegacyHomePage.selectRegimenAfterLogin(regimen);
	//	//		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
	//	//		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
	//	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//	//		storeFrontLegacyHomePage.clickContinueBtn();
	//	//		storeFrontLegacyHomePage.clickContinueBtnOnBillingPage();
	//	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from biz site for RC user.");
	//	//		s_assert.assertAll();
	//	//	}
}