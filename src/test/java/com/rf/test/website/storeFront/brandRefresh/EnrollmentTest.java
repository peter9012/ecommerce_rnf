package com.rf.test.website.storeFront.brandRefresh;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshHomePage;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshPCUserPage;
import com.rf.test.website.RFBrandRefreshWebsiteBaseTest;

public class EnrollmentTest extends RFBrandRefreshWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(EnrollmentTest.class.getName());

	private StoreFrontBrandRefreshHomePage storeFrontBrandRefreshHomePage;
	private StoreFrontBrandRefreshPCUserPage storeFrontBrandRefreshPCUserPage;
	String RFL_DB = null;
	
	public EnrollmentTest() {
		storeFrontBrandRefreshHomePage = new StoreFrontBrandRefreshHomePage(driver);
		storeFrontBrandRefreshPCUserPage = new StoreFrontBrandRefreshPCUserPage(driver);
	}

	//PC Enrollment From Corp site
	@Test(priority=2)//smoke
	public void testPCEnrollment(){
		RFL_DB = driver.getDBNameRFL();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String shippingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
		String shippingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		String gender = TestConstantsRFL.GENDER_MALE;
		//String sponsorID = TestConstantsRFL.CID_CONSULTANT;
		String sponsorID = null;
		String addressName = "Home";
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
		String billingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;

		//Get PC Sponser from database
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		sponsorID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountNumber"));

		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(regimen);
		storeFrontBrandRefreshHomePage.clickAddToCartBtn();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.clickClickHereLinkForPC();
		s_assert.assertTrue(driver.getCurrentUrl().contains("PCPerks"), "After clicking click here link for PC not navigated to PC Enrollment page.");
		storeFrontBrandRefreshHomePage.clickEnrollNowBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.enterProfileDetailsForPCAndRC(firstName,lastName,emailAddress,password,phnNumber,gender);
		storeFrontBrandRefreshHomePage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.enterIDNumberAsSponsorForPCAndRC(sponsorID);
		storeFrontBrandRefreshHomePage.clickBeginSearchBtn();
		storeFrontBrandRefreshHomePage.selectSponsorRadioBtn();
		storeFrontBrandRefreshHomePage.clickSelectAndContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickContinueBtnForPCAndRC();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyErrorMessageForTermsAndConditionsForPCAndRC(), "Terms and candition error message not present for PC User.");
		storeFrontBrandRefreshHomePage.checkTermsAndConditionChkBoxForPCAndRC();
		storeFrontBrandRefreshHomePage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickContinueBtnOnAutoshipSetupPageForPC();
		storeFrontBrandRefreshHomePage.clickContinueBtn();
		storeFrontBrandRefreshHomePage.enterShippingProfileDetails(addressName, shippingProfileFirstName,shippingProfileLastName, addressLine1, postalCode, phnNumber);
		storeFrontBrandRefreshHomePage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfoDetails(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickContinueBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.clickCompleteEnrollmentBtn();
		storeFrontBrandRefreshHomePage.clickOKBtnOfJavaScriptPopUp();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isPCEnrollmentCompletedSuccessfully(), "PC enrollment is not completed successfully");
		s_assert.assertAll();
	}

	//RC Enrollment from corp site.
	@Test(priority=1)//smoke
	public void testRCEnrollment(){
		RFL_DB = driver.getDBNameRFL();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumbers = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String shippingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
		String shippingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.BILLING_PROFILE_FIRST_NAME+randomNumbers;
		String billingProfileLastName = TestConstantsRFL.BILLING_PROFILE_LAST_NAME;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		String gender = TestConstantsRFL.GENDER_MALE;
		String javaScriptPopupTxt = TestConstantsRFL.RC_ACCOUNT_CONFIRMATION_POPUP_TXT;
		//String sponsorID = TestConstantsRFL.CID_CONSULTANT;
		String sponsorID = null;
		String addressName = "Home";

		//Get RC Sponser from database
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		sponsorID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountNumber"));
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(regimen);
		storeFrontBrandRefreshHomePage.clickAddToCartBtn();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.clickClickHereLinkForRC();
		s_assert.assertTrue(driver.getCurrentUrl().contains("Retail"), "After clicking click here link for RC not navigated to RC Enrollment page.");
		storeFrontBrandRefreshHomePage.enterProfileDetailsForPCAndRC(firstName,lastName,emailAddress,password,phnNumber,gender);
		storeFrontBrandRefreshHomePage.clickCreateMyAccountBtnOnCreateRetailAccountPage();
		storeFrontBrandRefreshHomePage.enterIDNumberAsSponsorForPCAndRC(sponsorID);
		storeFrontBrandRefreshHomePage.clickBeginSearchBtn();
		storeFrontBrandRefreshHomePage.selectSponsorRadioBtn();
		storeFrontBrandRefreshHomePage.clickSelectAndContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.enterShippingProfileDetails(addressName, shippingProfileFirstName,shippingProfileLastName, addressLine1, postalCode, phnNumber);
		storeFrontBrandRefreshHomePage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfoDetails(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickContinueBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isThankYouTextPresentAfterOrderPlaced(), "Enrollment is not completed successfully");
		s_assert.assertAll(); 
	}

	//Consultant Express Enrollment from Corp
	@Test(priority=3)//smoke
	public void testConsultantExpressEnrollment(){
		RFL_DB = driver.getDBNameRFL();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(10, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		//storeFrontBrandRefreshHomePage.closeTheChildWindow();
		//  storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		//  storeFrontBrandRefreshHomePage.clickEnrollNowBtnOnBusinessPage();
		//Get Sponser details for Consultant enrollment.

		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		String sponsorID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountNumber"));
		storeFrontBrandRefreshHomePage.mouseHoverBeAConsultantAndClickLink("Enroll Now");
		storeFrontBrandRefreshHomePage.enterCID(sponsorID);
		storeFrontBrandRefreshHomePage.clickSearchResults();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear, CVV);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		//storeFrontBrandRefreshHomePage.enterPWS(firstName+lastName+randomNum);
		storeFrontBrandRefreshHomePage.clickCompleteAccountNextBtn();
		storeFrontBrandRefreshHomePage.clickChargeMyCardAndEnrollMeWithOutConfirmAutoship();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyErrorMessageForTermsAndConditionsForConsultant(), "Error message is not present for consultant for terms & conditions");
		storeFrontBrandRefreshHomePage.clickTermsAndConditions();
		storeFrontBrandRefreshHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isCongratulationsMessageAppeared(),"");
		s_assert.assertAll();
	}

	//Consultant Standard Enrollment
	@Test(priority=4)//smoke
	public void testConsultantStandardEnrollment(){
		RFL_DB = driver.getDBNameRFL();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(11, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Standard";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		storeFrontBrandRefreshHomePage.mouseHoverBeAConsultantAndClickLink("Enroll Now");
		//  storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		//  storeFrontBrandRefreshHomePage.clickEnrollNowBtnOnBusinessPage();
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		String sponsorID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountNumber"));
		storeFrontBrandRefreshHomePage.enterCID(sponsorID);
		storeFrontBrandRefreshHomePage.clickSearchResults();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear, CVV);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontBrandRefreshHomePage.clickBillingInfoNextBtn();
		storeFrontBrandRefreshHomePage.clickYesSubscribeToPulseNow();
		storeFrontBrandRefreshHomePage.clickYesEnrollInCRPNow();
		storeFrontBrandRefreshHomePage.clickAutoShipOptionsNextBtn();
		storeFrontBrandRefreshHomePage.selectProductToAddToCart();
		storeFrontBrandRefreshHomePage.clickYourCRPOrderPopUpNextBtn();
		storeFrontBrandRefreshHomePage.clickChargeMyCardAndEnrollMeWithOutConfirmAutoship();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyErrorMessageForTermsAndConditionsForConsultant(), "Error message is not present for consultant for terms & conditions");
		storeFrontBrandRefreshHomePage.clickTermsAndConditions();
		storeFrontBrandRefreshHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isCongratulationsMessageAppeared(),"Congratulations Message not appeared");
		s_assert.assertAll();
	}

	//My account options as RC customer
	@Test(priority=5)
	public void testMyAccountOptionAsRCCustomer(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS,RFL_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");
		storeFrontBrandRefreshHomePage.loginAsRCUser(rcEmailID, password);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"RC user is not logged in successfully");
		storeFrontBrandRefreshHomePage.clickMyAccountLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyLinkPresentUnderMyAccount("Order History"),"order history link is not present");
		storeFrontBrandRefreshHomePage.clickOrderManagementSublink("Order History");
		int orderNumber =storeFrontBrandRefreshHomePage.clickViewDetailsForOrderAndReturnOrderNumber();
		if(orderNumber!=0){
			s_assert.assertTrue(storeFrontBrandRefreshHomePage.isOrderDetailsPopupPresent(),"Order details popup not present after clicking view order details link.");
			storeFrontBrandRefreshHomePage.clickCloseOfOrderDetailsPopup();
		}
		s_assert.assertAll();
	}

	//Search for a Sponsor
	@Test(enabled=false)
	public void testSearchForASponser(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomConsultantList =  null;
		String accountNumber = null;
		//String CID = TestConstantsRFL.CID_CONSULTANT;
		String PWS = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		accountNumber = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountNumber"));
		storeFrontBrandRefreshHomePage.clickFindAConsultantImageLink();
		storeFrontBrandRefreshHomePage.enterIDNumberAsSponsorForPCAndRC(accountNumber);
		storeFrontBrandRefreshHomePage.clickBeginSearchBtn();
		PWS = storeFrontBrandRefreshHomePage.clickAndReturnPWSFromFindConsultantPage();
		s_assert.assertTrue(driver.getCurrentUrl().contains(PWS.split("//")[1]), "User has not been navigated to pws site for searched consultant Expected: "+PWS.split("//")[1]+" While actual: "+driver.getCurrentUrl());
		s_assert.assertAll();
	}

	// My account options as PC customer
	@Test(priority=6)
	public void testMyAccountOptionsAs_PC_Customer(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPCList = null;
		String pcEmailId = null;
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
			pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
			storeFrontBrandRefreshPCUserPage = storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontBrandRefreshPCUserPage.clickMyAccountLink();
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.verifyLinkPresentUnderMyAccount("Order History"),"order history link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.verifyLinkPresentUnderMyAccount("Edit Order"),"Edit Order link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.verifyLinkPresentUnderMyAccount("Change my PC Perks Status"),"Change my PC Perks Status link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.verifyLinkPresentUnderMyAccount("PC Perks FAQs"),"PC Perks FAQs Status link is not present");
		storeFrontBrandRefreshPCUserPage.clickOrderManagementSublink("Order History");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.verifyOrderHistoryPresent("Order History"),"section order history not present");
		storeFrontBrandRefreshPCUserPage.navigateToBackPage();
		storeFrontBrandRefreshPCUserPage.clickOrderManagementSublink("Edit Order");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.isEditOrderPagePresent(),"Edit order page is not present");
		storeFrontBrandRefreshPCUserPage.navigateToBackPage();
		//		storeFrontBrandRefreshPCUserPage.clickMyAccountLink();
		storeFrontBrandRefreshPCUserPage.clickOrderManagementSublink("Change my PC Perks Status");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.verifyCurrentPage("PcPerksStatus"),"URL does not contain pcPerksStatus");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.isPcPerksStatusLinkPresent(),"Delay or Cancel PC Perks link is not present");
		storeFrontBrandRefreshHomePage.clickBackToMyAccountBtn();
		storeFrontBrandRefreshPCUserPage.clickOrderManagementSublink("PC Perks FAQs");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.verifyFaqPagePresent(),"This is not faq's page");
		s_assert.assertAll();
	}

	//PC Edit order as a PC User
	@Test(priority=7)
	public void testPCEditOrderAsAPCUser(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPCList = null;
		String pcEmailId = null;
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
			pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
			storeFrontBrandRefreshPCUserPage = storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontBrandRefreshPCUserPage.clickMyAccountLink();
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.verifyLinkPresentUnderMyAccount("Order History"),"order history link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.verifyLinkPresentUnderMyAccount("Edit Order"),"Edit Order link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.verifyLinkPresentUnderMyAccount("Change my PC Perks Status"),"Change my PC Perks Status link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.verifyLinkPresentUnderMyAccount("PC Perks FAQs"),"PC Perks FAQs Status link is not present");
		storeFrontBrandRefreshPCUserPage.clickOrderManagementSublink("Edit Order");
		storeFrontBrandRefreshHomePage.clickEditOrderbtn();
		storeFrontBrandRefreshHomePage.clickAddToCartBtnForHighPriceItems();
		storeFrontBrandRefreshHomePage.clickOnUpdateOrderBtn();
		storeFrontBrandRefreshHomePage.handleAlertAfterUpdateOrder();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getConfirmationMessage().contains("Replenishment Order items successfully updated!"),"No Message appearing after order update");
		s_assert.assertAll();
	}
}