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
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyHomePage;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyPCUserPage;
import com.rf.test.website.RFLegacyStoreFrontWebsiteBaseTest;

public class EnrollmentTest extends RFLegacyStoreFrontWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(EnrollmentTest.class.getName());

	private StoreFrontLegacyHomePage storeFrontLegacyHomePage;
	private StoreFrontLegacyPCUserPage storeFrontLegacyPCUserPage;
	String RFL_DB = null;

	//PC Enrollment From Corp site
	@Test(enabled=true)//smoke
	public void testPCEnrollment(){
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
		String sponsorID = TestConstantsRFL.CID_CONSULTANT;
		String addressName = "Home";
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
		String billingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		storeFrontLegacyHomePage.selectRegimen(regimen);
		storeFrontLegacyHomePage.clickAddToCartBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickClickHereLinkForPC();
		storeFrontLegacyHomePage.clickEnrollNowBtnForPCAndRC();
		storeFrontLegacyHomePage.enterProfileDetailsForPCAndRC(firstName,lastName,emailAddress,password,phnNumber,gender);
		storeFrontLegacyHomePage.clickContinueBtnForPCAndRC();
		storeFrontLegacyHomePage.enterIDNumberAsSponsorForPCAndRC(sponsorID);
		storeFrontLegacyHomePage.clickBeginSearchBtn();
		storeFrontLegacyHomePage.selectSponsorRadioBtn();
		storeFrontLegacyHomePage.clickSelectAndContinueBtnForPCAndRC();
		storeFrontLegacyHomePage.checkTermsAndConditionChkBoxForPCAndRC();
		storeFrontLegacyHomePage.clickContinueBtnForPCAndRC();
		storeFrontLegacyHomePage.clickContinueBtnOnAutoshipSetupPageForPC();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.enterShippingProfileDetails(addressName, shippingProfileFirstName,shippingProfileLastName, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		storeFrontLegacyHomePage.enterBillingInfoDetails(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		storeFrontLegacyHomePage.clickCompleteEnrollmentBtn();
		storeFrontLegacyHomePage.clickOKBtnOfJavaScriptPopUp();
		s_assert.assertTrue(storeFrontLegacyHomePage.isEnrollmentCompletedSuccessfully(), "PC enrollment is not completed successfully");
		s_assert.assertAll();
	}

	//RC Enrollment from corp site.
	@Test(enabled=true)//smoke
	public void testRCEnrollment(){
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
		String sponsorID = TestConstantsRFL.CID_CONSULTANT;
		String addressName = "Home";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		storeFrontLegacyHomePage.selectRegimen(regimen);
		storeFrontLegacyHomePage.clickAddToCartBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickClickHereLinkForRC();
		storeFrontLegacyHomePage.enterProfileDetailsForPCAndRC(firstName,lastName,emailAddress,password,phnNumber,gender);
		storeFrontLegacyHomePage.clickCreateMyAccountBtnOnCreateRetailAccountPage();
		//s_assert.assertTrue(storeFrontLegacyHomePage.getJavaScriptPopUpText().contains(javaScriptPopupTxt),"Java Script Popup for RC account confirmation not present");
		//storeFrontLegacyHomePage.clickOKBtnOfJavaScriptPopUp();
		storeFrontLegacyHomePage.enterIDNumberAsSponsorForPCAndRC(sponsorID);
		storeFrontLegacyHomePage.clickBeginSearchBtn();
		storeFrontLegacyHomePage.selectSponsorRadioBtn();
		storeFrontLegacyHomePage.clickSelectAndContinueBtnForPCAndRC();
		storeFrontLegacyHomePage.enterShippingProfileDetails(addressName, shippingProfileFirstName,shippingProfileLastName, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		storeFrontLegacyHomePage.enterBillingInfoDetails(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Enrollment is not completed successfully");
		s_assert.assertAll();	
	}

	//Consultant Express Enrollment from Corp
	@Test(enabled=true)//smoke
	public void testConsultantExpressEnrollment(){
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
		String CID = TestConstantsRFL.CID_CONSULTANT;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		storeFrontLegacyHomePage.clickEnrollNowBtnOnBusinessPage();
		storeFrontLegacyHomePage.enterCID(CID);
		storeFrontLegacyHomePage.clickSearchResults();
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		//storeFrontLegacyHomePage.enterPWS(firstName+lastName+randomNum);
		storeFrontLegacyHomePage.clickCompleteAccountNextBtn();
		storeFrontLegacyHomePage.clickTermsAndConditions();
		storeFrontLegacyHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"");
		s_assert.assertAll();
	}

	//Consultant Standard Enrollment
	@Test(enabled=true)//smoke
	public void testConsultantStandardEnrollment(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(00, 99);
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
		String CID = TestConstantsRFL.CID_CONSULTANT;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Standard";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		storeFrontLegacyHomePage.clickEnrollNowBtnOnBusinessPage();
		storeFrontLegacyHomePage.enterCID(CID);
		storeFrontLegacyHomePage.clickSearchResults();
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);

		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontLegacyHomePage.clickBillingInfoNextBtn();
		storeFrontLegacyHomePage.clickYesSubscribeToPulseNow();
		storeFrontLegacyHomePage.clickYesEnrollInCRPNow();
		storeFrontLegacyHomePage.clickAutoShipOptionsNextBtn();
		storeFrontLegacyHomePage.selectProductToAddToCart();
		storeFrontLegacyHomePage.clickYourCRPOrderPopUpNextBtn();
		storeFrontLegacyHomePage.clickTermsAndConditions();
		storeFrontLegacyHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"Congratulations Message not appeared");
		s_assert.assertAll();
	}

	//My account options as RC customer
	@Test
	public void testMyAccountOptionAsRCCustomer(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS,RFL_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");
		storeFrontLegacyHomePage.loginAsRCUser(rcEmailID, password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedInOnCorpSite(),"RC user is not logged in successfully");
		storeFrontLegacyHomePage.clickMyAccountLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyLinkPresentUnderMyAccount("Order History"),"order history link is not present");
		storeFrontLegacyHomePage.clickOrderManagementSublink("Order History");
		int orderNumber =storeFrontLegacyHomePage.clickViewDetailsForOrderAndReturnOrderNumber();
		if(orderNumber!=0){
			s_assert.assertTrue(storeFrontLegacyHomePage.isOrderDetailsPopupPresent(),"Order details popup not present after clicking view order details link.");
			storeFrontLegacyHomePage.clickCloseOfOrderDetailsPopup();
		}
		s_assert.assertAll();
	}

	//Search for a Sponsor
	@Test
	public void testSearchForASponser(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomConsultantList =  null;
		String accountNumber = null;
		//String CID = TestConstantsRFL.CID_CONSULTANT;
		String PWS = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		accountNumber = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountNumber"));

		storeFrontLegacyHomePage.clickFindAConsultantImageLink();
		storeFrontLegacyHomePage.enterIDNumberAsSponsorForPCAndRC(accountNumber);
		storeFrontLegacyHomePage.clickBeginSearchBtn();
		PWS = storeFrontLegacyHomePage.clickAndReturnPWSFromFindConsultantPage();
		s_assert.assertTrue(driver.getCurrentUrl().contains(PWS.split("//")[1]), "User has not been navigated to pws site for searched consultant Expected: "+PWS.split("//")[1]+" While actual: "+driver.getCurrentUrl());
		s_assert.assertAll();
	}

	// My account options as PC customer
	@Test
	public void testMyAccountOptionsAs_PC_Customer(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPCList = null;
		String pcEmailId = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
			pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
			storeFrontLegacyPCUserPage = storeFrontLegacyHomePage.loginAsPCUser(pcEmailId, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyPCUserPage.clickMyAccountLink();
		s_assert.assertTrue(storeFrontLegacyPCUserPage.verifyLinkPresentUnderMyAccount("Order History"),"order history link is not present");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.verifyLinkPresentUnderMyAccount("Edit Order"),"Edit Order link is not present");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.verifyLinkPresentUnderMyAccount("Change my PC Perks Status"),"Change my PC Perks Status link is not present");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.verifyLinkPresentUnderMyAccount("PC Perks FAQs"),"PC Perks FAQs Status link is not present");
		storeFrontLegacyPCUserPage.clickOrderManagementSublink("Order History");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.verifyOrderHistoryPresent("Order History"),"section order history not present");
		storeFrontLegacyPCUserPage.navigateToBackPage();
		storeFrontLegacyPCUserPage.clickOrderManagementSublink("Edit Order");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.isEditOrderPagePresent(),"Edit order page is not present");
		storeFrontLegacyPCUserPage.clickMyAccountLink();
		storeFrontLegacyPCUserPage.clickOrderManagementSublink("Change my PC Perks Status");
		/*		s_assert.assertTrue(storeFrontLegacyPCUserPage.verifyCurrentPage("PcPerksStatus"),"URL does not contain pcPerksStatus");*/
		s_assert.assertTrue(storeFrontLegacyPCUserPage.isPcPerksStatusLinkPresent(),"Delay or Cancel PC Perks link is not present");
		storeFrontLegacyHomePage.clickBackToMyAccountBtn();
		storeFrontLegacyPCUserPage.clickOrderManagementSublink("PC Perks FAQs");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.verifyFaqPagePresent(),"This is not faq's page");
		s_assert.assertAll();
	}

	//PC Edit order as a PC User
	@Test(enabled=true)
	public void testPCEditOrderAsAPCUser(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPCList = null;
		String pcEmailId = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
			pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
			storeFrontLegacyPCUserPage = storeFrontLegacyHomePage.loginAsPCUser(pcEmailId, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyPCUserPage.clickMyAccountLink();
		s_assert.assertTrue(storeFrontLegacyPCUserPage.verifyLinkPresentUnderMyAccount("Order History"),"order history link is not present");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.verifyLinkPresentUnderMyAccount("Edit Order"),"Edit Order link is not present");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.verifyLinkPresentUnderMyAccount("Change my PC Perks Status"),"Change my PC Perks Status link is not present");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.verifyLinkPresentUnderMyAccount("PC Perks FAQs"),"PC Perks FAQs Status link is not present");
		storeFrontLegacyPCUserPage.clickOrderManagementSublink("Edit Order");
		storeFrontLegacyHomePage.clickEditOrderbtn();
		storeFrontLegacyHomePage.clickAddToCartBtnForHighPriceItems();
		storeFrontLegacyHomePage.clickOnUpdateOrderBtn();
		storeFrontLegacyHomePage.handleAlertAfterUpdateOrder();
		s_assert.assertTrue(storeFrontLegacyHomePage.getConfirmationMessage().contains("Replenishment Order items successfully updated!"),"No Message appearing after order update");
		s_assert.assertAll();
	}

}
