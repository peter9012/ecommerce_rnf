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

public class ComPWSTest extends RFBrandRefreshWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(ComPWSTest.class.getName());

	private StoreFrontBrandRefreshHomePage storeFrontBrandRefreshHomePage;
	private StoreFrontBrandRefreshConsultantPage storeFrontBrandRefreshConsultantPage;
	private String RFL_DB = null;

	public ComPWSTest() {
		storeFrontBrandRefreshHomePage = new StoreFrontBrandRefreshHomePage(driver);
		storeFrontBrandRefreshConsultantPage = new StoreFrontBrandRefreshConsultantPage(driver);		
	}
	
	//Shop Skincare-menu navigation
	@Test
	public void testShopSkinCareMenuNavigation(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		
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
	//	storeFrontBrandRefreshHomePage.clickShopSkinCareBtnOnPWS();
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareOnPWS();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_REDEFINE),"Redefine regimen name is not present on pws site");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_REVERSE),"Reverse regimen name is not present on pws site");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_UNBLEMISH),"Unblemish regimen name is not present on pws site");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_SOOTHE),"Soothe regimen name is not present on pws site");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_ENHANCEMENTS),"Enhancements regimen name is not present on pws site");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresent(TestConstantsRFL.REGIMEN_NAME_ESSENTIALS),"Essentials regimen name is not present on pws site");
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
		
		storeFrontBrandRefreshConsultantPage = storeFrontBrandRefreshHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		storeFrontBrandRefreshConsultantPage.mouseHoverShopSkinCareAndClickLinkOnPWS(regimen);
//		storeFrontBrandRefreshConsultantPage.clickShopSkinCareBtnOnPWS();
//		storeFrontBrandRefreshHomePage.clickRegimenOnPWS(regimen);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("promotions"), "Expected regimen name is: promotions Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshHomePage.clickAddToCartButtonAfterLogin();
		storeFrontBrandRefreshConsultantPage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.clickContinueBtn();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from biz pws for consultant user.");
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
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
		
		storeFrontBrandRefreshConsultantPage = storeFrontBrandRefreshHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		storeFrontBrandRefreshConsultantPage.mouseHoverOnShopSkinCareAndClickOnConsultantOnlyProductsLink();
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.getCurrentURL().toLowerCase().contains("consultantsonly"), "Expected regimen name is: consultantsonly Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshConsultantPage.clickConsultantOnlyProductOnPWS(TestConstantsRFL.CONSULTANT_ONLY_PRODUCT);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("consultant-only"), "Expected url contains is:Consultant-Only Products but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshHomePage.clickAddToCartButtonAfterLogin();
		storeFrontBrandRefreshConsultantPage.mouseHoverOnShopSkinCareAndClickOnConsultantOnlyProductsLink();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.clickContinueBtn();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertTrue(storeFrontBrandRefreshConsultantPage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		logout();
		s_assert.assertAll();
	}

	//Forgot_Password_PWS
	@Test
	public void testForgotPasswordPWS(){
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		
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
		storeFrontBrandRefreshHomePage.clickForgotPasswordLinkOnBizHomePage();
		//verify a message prompt to change the password displayed?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateChangePasswordMessagePrompt(),"Message not prompted for 'change password'");
		//verify user is able to change the password and email is being sent?
		storeFrontBrandRefreshHomePage.enterEmailAddressToRecoverPassword(consultantEmailID);
		storeFrontBrandRefreshHomePage.clickSendEmailButton();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validatePasswordChangeAndEmailSent(),"resetting your password mail is not displayed!!");
		s_assert.assertAll();
	}

	//Shop_Skincare_PWS
	@Test
	public void testShopSkinCarePWS(){
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		

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
		storeFrontBrandRefreshHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		//click on 'our products' in tha nav menu
		storeFrontBrandRefreshHomePage.mouseHoverOnShopSkinCareAndClickOnConsultantOnlyProductsLink();
		//select a product and add to cart
		storeFrontBrandRefreshHomePage.clickConsultantOnlyProductOnPWS(TestConstantsRFL.CONSULTANT_ONLY_PRODUCT);
		storeFrontBrandRefreshHomePage.clickAddToCartButtonAfterLogin();
		storeFrontBrandRefreshHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.clickContinueBtn();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isThankYouTextPresentAfterOrderPlaced(), "Order is not placed successfully");
		s_assert.assertAll();
	}

}
