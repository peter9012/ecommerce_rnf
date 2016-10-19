package com.rf.test.website.storeFront.legacy;

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
import com.rf.test.website.RFBrandRefreshWebsiteBaseTest;
import com.rf.test.website.RFLegacyStoreFrontWebsiteBaseTest;

public class ComPWSTest extends RFBrandRefreshWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(ComPWSTest.class.getName());

	private StoreFrontLegacyHomePage storeFrontLegacyHomePage;
	private StoreFrontLegacyConsultantPage storeFrontLegacyConsultantPage;
	private String RFL_DB = null;

	//Shop Skincare-menu navigation
	@Test
	public void testShopSkinCareMenuNavigation(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_COM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage.clickShopSkinCareBtnOnPWS();
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_REDEFINE),"Redefine regimen name is not present on pws site");
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_REVERSE),"Reverse regimen name is not present on pws site");
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_UNBLEMISH),"Unblemish regimen name is not present on pws site");
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_SOOTHE),"Soothe regimen name is not present on pws site");
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_ENHANCEMENTS),"Enhancements regimen name is not present on pws site");
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_ESSENTIALS),"Essentials regimen name is not present on pws site");
		s_assert.assertAll();
	}

	//Shop Skincare-Consultants Only -buy business promotion
	@Test(enabled=true)
	public void testShopSkinCareConsultantsOnlyBuyBusinessPromotion(){
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
		String regimen = "Promotions";
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		String consultantEmailID = null;
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_COM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyConsultantPage = storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyConsultantPage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		storeFrontLegacyConsultantPage.clickShopSkinCareBtnOnPWS();
		storeFrontLegacyHomePage.clickRegimenOnPWS(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("promotions"), "Expected regimen name is: promotions Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
		storeFrontLegacyConsultantPage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickChangeBillingInformationBtn();
		storeFrontLegacyHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickUseThisBillingInformationBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from biz pws for consultant user.");
		s_assert.assertTrue(storeFrontLegacyConsultantPage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		logout();
		s_assert.assertAll();
	}
	
	//Shop Skincare-Consultants Only -buy only products (enhancements micro dermabrasion paste packets)
	@Test
	public void testShopSkinCareConsultantsOnlyBuyProductPromotion(){
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
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		String consultantEmailID = null;
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_COM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyConsultantPage = storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyConsultantPage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		storeFrontLegacyConsultantPage.mouseHoverOnShopSkinCareAndClickOnConsultantOnlyProductsLink();
		s_assert.assertTrue(storeFrontLegacyConsultantPage.getCurrentURL().toLowerCase().contains("consultantsonly"), "Expected regimen name is: consultantsonly Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		storeFrontLegacyConsultantPage.clickConsultantOnlyProductOnPWS(TestConstantsRFL.CONSULTANT_ONLY_PRODUCT);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("consultant-only"), "Expected url contains is:Consultant-Only Products but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
		storeFrontLegacyConsultantPage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickChangeBillingInformationBtn();
		storeFrontLegacyHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickUseThisBillingInformationBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyConsultantPage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertTrue(storeFrontLegacyConsultantPage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		logout();
		s_assert.assertAll();
	}

	//Forgot_Password_PWS
	@Test
	public void testForgotPasswordPWS(){
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_COM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		//click 'forgot password' on biz home page
		storeFrontLegacyHomePage.clickForgotPasswordLinkOnBizHomePage();
		//verify a message prompt to change the password displayed?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateChangePasswordMessagePrompt(),"Message not prompted for 'change password'");
		//verify user is able to change the password and email is being sent?
		storeFrontLegacyHomePage.enterEmailAddressToRecoverPassword(consultantEmailID);
		storeFrontLegacyHomePage.clickSendEmailButton();
		s_assert.assertTrue(storeFrontLegacyHomePage.validatePasswordChangeAndEmailSent(),"resetting your password mail is not displayed!!");
		s_assert.assertAll();
	}

	//Shop_Skincare_PWS
	@Test
	public void testShopSkinCarePWS(){
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);

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
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_COM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		//click on 'our products' in tha nav menu
		storeFrontLegacyHomePage.mouseHoverOnShopSkinCareAndClickOnConsultantOnlyProductsLink();
		//select a product and add to cart
		storeFrontLegacyHomePage.clickConsultantOnlyProductOnPWS(TestConstantsRFL.CONSULTANT_ONLY_PRODUCT);
		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickChangeBillingInformationBtn();
		storeFrontLegacyHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickUseThisBillingInformationBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertAll();
	}

}
