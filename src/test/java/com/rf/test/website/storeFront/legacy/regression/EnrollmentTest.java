package com.rf.test.website.storeFront.legacy.regression;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyHomePage;
import com.rf.test.website.RFLegacyStoreFrontWebsiteBaseTest;

public class EnrollmentTest extends RFLegacyStoreFrontWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(EnrollmentTest.class.getName());

	private StoreFrontLegacyHomePage storeFrontLegacyHomePage;

	//PC Enrollment From Corp site
	@Test(enabled=true)
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
		storeFrontLegacyHomePage.clickProductsBtn();
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
	@Test(enabled=true)
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
		storeFrontLegacyHomePage.clickProductsBtn();
		storeFrontLegacyHomePage.selectRegimen(regimen);
		storeFrontLegacyHomePage.clickAddToCartBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickClickHereLinkForRC();
		storeFrontLegacyHomePage.enterProfileDetailsForPCAndRC(firstName,lastName,emailAddress,password,phnNumber,gender);
		storeFrontLegacyHomePage.clickCreateMyAccountBtnOnCreateRetailAccountPage();
		s_assert.assertTrue(storeFrontLegacyHomePage.getJavaScriptPopUpText().contains(javaScriptPopupTxt),"Java Script Popup for RC account confirmation not present");
		storeFrontLegacyHomePage.clickOKBtnOfJavaScriptPopUp();
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
	@Test(enabled=true)
	public void testConsultantExpressEnrollment(){
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
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBusinessSystemBtn();
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
	@Test(enabled=true)
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
		storeFrontLegacyHomePage.clickBusinessSystemBtn();
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

	//	//Redefine RC Enrollment from corp site(S.No 6)
	//	@Test
	//	public void testRCEnrollmentUsingRedefineRegimen(){
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
	//		int randomNumbers = CommonUtils.getRandomNum(10000, 1000000);
	//		String firstName = TestConstantsRFL.FIRST_NAME;
	//		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
	//		String shippingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
	//		String shippingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
	//		String emailAddress = firstName+randomNum+"@xyz.com";
	//		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
	//		String postalCode = TestConstantsRFL.POSTAL_CODE;
	//		String cardNumber = TestConstantsRFL.CARD_NUMBER;
	//		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
	//		String billingProfileFirstName = TestConstantsRFL.BILLING_PROFILE_FIRST_NAME+randomNumbers;
	//		String billingProfileLastName = TestConstantsRFL.BILLING_PROFILE_LAST_NAME;
	//		String nameOnCard = firstName;
	//		String expMonth = TestConstantsRFL.EXP_MONTH;
	//		String expYear = TestConstantsRFL.EXP_YEAR;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
	//		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
	//		String gender = TestConstantsRFL.GENDER_MALE;
	//		String javaScriptPopupTxt = TestConstantsRFL.RC_ACCOUNT_CONFIRMATION_POPUP_TXT;
	//		String sponsorID = TestConstantsRFL.CID_CONSULTANT;
	//		String addressName = "Home";
	//
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.clickProductsBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		storeFrontLegacyHomePage.clickAddToCartBtn();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickClickHereLinkForRC();
	//		storeFrontLegacyHomePage.enterProfileDetailsForPCAndRC(firstName,lastName,emailAddress,password,phnNumber,gender);
	//		storeFrontLegacyHomePage.clickCreateMyAccountBtnOnCreateRetailAccountPage();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getJavaScriptPopUpText().contains(javaScriptPopupTxt),"Java Script Popup for RC account confirmation not present");
	//		storeFrontLegacyHomePage.clickOKBtnOfJavaScriptPopUp();
	//		storeFrontLegacyHomePage.enterIDNumberAsSponsorForPCAndRC(sponsorID);
	//		storeFrontLegacyHomePage.clickBeginSearchBtn();
	//		storeFrontLegacyHomePage.selectSponsorRadioBtn();
	//		storeFrontLegacyHomePage.clickSelectAndContinueBtnForPCAndRC();
	//		storeFrontLegacyHomePage.enterShippingProfileDetails(addressName, shippingProfileFirstName,shippingProfileLastName, addressLine1, postalCode, phnNumber);
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
	//		storeFrontLegacyHomePage.enterBillingInfoDetails(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Enrollment is not completed successfully");
	//		s_assert.assertAll(); 
	//	}


	////	//Consultant Express Enrollment from PWS(S.No 69)
	////	@Test
	////	public void testConsultantExpressEnrollmentBizSite(){
	////		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	////		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
	////		int ssnRandomNum2 = CommonUtils.getRandomNum(00, 99);
	////		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
	////		String firstName = TestConstantsRFL.FIRST_NAME;
	////		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
	////		String emailAddress = firstName+randomNum+"@xyz.com";
	////		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
	////		String postalCode = TestConstantsRFL.POSTAL_CODE;
	////		String cardNumber = TestConstantsRFL.CARD_NUMBER;
	////		String nameOnCard = firstName;
	////		String expMonth = TestConstantsRFL.EXP_MONTH;
	////		String expYear = TestConstantsRFL.EXP_YEAR;
	////		String CID = TestConstantsRFL.CID_CONSULTANT;
	////		String kitName = "Big Business Launch Kit";
	////		String regimen = "Redefine";
	////		String enrollemntType = "Express";
	////		String phnNumber1 = "415";
	////		String phnNumber2 = "780";
	////		String phnNumber3 = "9099";
	////		String PWS = TestConstantsRFL.BIZ_PWS;
	////		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	////		//Navigate to biz PWS
	////		storeFrontLegacyHomePage.openBizPWS(PWS);
	////		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
	////		storeFrontLegacyHomePage.clickEnrollNowBtnOnWhyRFPage();
	////		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
	////		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
	////		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
	////		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
	////		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
	////		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
	////		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
	////		//storeFrontLegacyHomePage.enterPWS(firstName+lastName+randomNum);
	////		storeFrontLegacyHomePage.clickCompleteAccountNextBtn();
	////		storeFrontLegacyHomePage.clickTermsAndConditions();
	////		storeFrontLegacyHomePage.chargeMyCardAndEnrollMe();
	////		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"Congratulation message is not displayed");
	////		s_assert.assertAll();
	////	}

	//
	//	//PC Enrollment from corp -redefine regimen(S.No 5)
	//	@Test
	//	public void testPCEnrollmentUsingRedefineRegimen(){
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
	//		String firstName = TestConstantsRFL.FIRST_NAME;
	//		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
	//		String shippingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
	//		String shippingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
	//		String emailAddress = firstName+randomNum+"@xyz.com";
	//		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
	//		String postalCode = TestConstantsRFL.POSTAL_CODE;
	//		String cardNumber = TestConstantsRFL.CARD_NUMBER;
	//		String nameOnCard = firstName;
	//		String expMonth = TestConstantsRFL.EXP_MONTH;
	//		String expYear = TestConstantsRFL.EXP_YEAR;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
	//		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
	//		String gender = TestConstantsRFL.GENDER_MALE;
	//		String sponsorID = TestConstantsRFL.CID_CONSULTANT;
	//		String addressName = "Home";
	//		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
	//		String billingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
	//		String billingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.clickProductsBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		storeFrontLegacyHomePage.clickAddToCartBtn();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickClickHereLinkForPC();
	//		storeFrontLegacyHomePage.clickEnrollNowBtnForPCAndRC();
	//		storeFrontLegacyHomePage.enterProfileDetailsForPCAndRC(firstName,lastName,emailAddress,password,phnNumber,gender);
	//		storeFrontLegacyHomePage.clickContinueBtnForPCAndRC();
	//		storeFrontLegacyHomePage.enterIDNumberAsSponsorForPCAndRC(sponsorID);
	//		storeFrontLegacyHomePage.clickBeginSearchBtn();
	//		storeFrontLegacyHomePage.selectSponsorRadioBtn();
	//		storeFrontLegacyHomePage.clickSelectAndContinueBtnForPCAndRC();
	//		storeFrontLegacyHomePage.checkTermsAndConditionChkBoxForPCAndRC();
	//		storeFrontLegacyHomePage.clickContinueBtnForPCAndRC();
	//		storeFrontLegacyHomePage.clickContinueBtnOnAutoshipSetupPageForPC();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.enterShippingProfileDetails(addressName, shippingProfileFirstName,shippingProfileLastName, addressLine1, postalCode, phnNumber);
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
	//		storeFrontLegacyHomePage.enterBillingInfoDetails(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
	//		storeFrontLegacyHomePage.clickCompleteEnrollmentBtn();
	//		storeFrontLegacyHomePage.clickOKBtnOfJavaScriptPopUp();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isEnrollmentCompletedSuccessfully(), "PC enrollment is not completed successfully");
	//		s_assert.assertAll();
	//	}
	//
	//	// Consultant Express Enrollment - Business Portfolio (S.No 71)
	//	@Test
	//	public void testExpressEnrollmentBusinessPortfolio(){
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
	//		int ssnRandomNum2 = CommonUtils.getRandomNum(00, 99);
	//		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
	//		String firstName = TestConstantsRFL.FIRST_NAME;
	//		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
	//		String emailAddress = firstName+randomNum+"@xyz.com";
	//		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
	//		String postalCode = TestConstantsRFL.POSTAL_CODE;
	//		String cardNumber = TestConstantsRFL.CARD_NUMBER;
	//		String nameOnCard = firstName;
	//		String expMonth = TestConstantsRFL.EXP_MONTH;
	//		String expYear = TestConstantsRFL.EXP_YEAR;
	//		String CID = TestConstantsRFL.CID_CONSULTANT;
	//		String kitName = "Business Portfolio";
	//		String regimen = "Redefine";
	//		String enrollemntType = "Express";
	//		String phnNumber1 = "415";
	//		String phnNumber2 = "780";
	//		String phnNumber3 = "9099";
	//
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.clickBusinessSystemBtn();
	//		storeFrontLegacyHomePage.clickEnrollNowBtnOnBusinessPage();
	//		storeFrontLegacyHomePage.enterCID(CID);
	//		storeFrontLegacyHomePage.clickSearchResults();
	//		storeFrontLegacyHomePage.selectBusinessPortfolioEnrollmentKit();
	//		storeFrontLegacyHomePage.clickNextBtnOnSelectEnrollmentKitPage();
	//		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
	//		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
	//		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
	//		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
	//		//storeFrontLegacyHomePage.enterPWS(firstName+lastName+randomNum);
	//		storeFrontLegacyHomePage.clickAccountInformationNextBtn();
	//		storeFrontLegacyHomePage.clickCompleteAccountNextBtn();
	//		storeFrontLegacyHomePage.clickTermsAndConditions();
	//		storeFrontLegacyHomePage.clickChargeMyCardAndEnrollMeWithOutConfirmAutoship();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"");
	//		s_assert.assertAll();
	//	}
	//
	//	//Consultant Enrollment & Login (S.No 52)
	//	@Test
	//	public void testExpressConsultantEnrollmentAndLogin(){
	//		//Enroll a consultant
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
	//		int ssnRandomNum2 = CommonUtils.getRandomNum(00, 99);
	//		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
	//		String firstName = TestConstantsRFL.FIRST_NAME;
	//		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
	//		String emailAddress = firstName+randomNum+"@xyz.com";
	//		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
	//		String postalCode = TestConstantsRFL.POSTAL_CODE;
	//		String cardNumber = TestConstantsRFL.CARD_NUMBER;
	//		String nameOnCard = firstName;
	//		String expMonth = TestConstantsRFL.EXP_MONTH;
	//		String expYear = TestConstantsRFL.EXP_YEAR;
	//		String CID = TestConstantsRFL.CID_CONSULTANT;
	//		String kitName = "Big Business Launch Kit";
	//		String regimen = "Redefine";
	//		String enrollemntType = "Express";
	//		String phnNumber1 = "415";
	//		String phnNumber2 = "780";
	//		String phnNumber3 = "9099";
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.clickBusinessSystemBtn();
	//		storeFrontLegacyHomePage.clickEnrollNowBtnOnBusinessPage();
	//		storeFrontLegacyHomePage.enterCID(CID);
	//		storeFrontLegacyHomePage.clickSearchResults();
	//		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
	//		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
	//		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
	//		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
	//		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
	//		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
	//		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
	//		//storeFrontLegacyHomePage.enterPWS(firstName+lastName+randomNum);
	//		storeFrontLegacyHomePage.clickCompleteAccountNextBtn();
	//		storeFrontLegacyHomePage.clickTermsAndConditions();
	//		storeFrontLegacyHomePage.chargeMyCardAndEnrollMe();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"");
	//		storeFrontLegacyHomePage.clickOnRodanAndFieldsLogo();
	//		storeFrontLegacyHomePage.logOut();
	//		//login with same enrolled consultant and verify it's successful
	//		storeFrontLegacyHomePage.loginAsConsultant(emailAddress, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedIn(),"User is not successfully loggedIN");
	//		s_assert.assertAll();
	//	}
	//
	//	//PC Enrollment From Corp site And login (S.No 54)
	//	@Test
	//	public void testPCEnrollmentAndLoging(){
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
	//		String firstName = TestConstantsRFL.FIRST_NAME;
	//		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
	//		String shippingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
	//		String shippingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
	//		String emailAddress = firstName+randomNum+"@xyz.com";
	//		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
	//		String postalCode = TestConstantsRFL.POSTAL_CODE;
	//		String cardNumber = TestConstantsRFL.CARD_NUMBER;
	//		String nameOnCard = firstName;
	//		String expMonth = TestConstantsRFL.EXP_MONTH;
	//		String expYear = TestConstantsRFL.EXP_YEAR;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
	//		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
	//		String gender = TestConstantsRFL.GENDER_MALE;
	//		String sponsorID = TestConstantsRFL.CID_CONSULTANT;
	//		String addressName = "Home";
	//		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
	//		String billingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
	//		String billingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.clickProductsBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		storeFrontLegacyHomePage.clickAddToCartBtn();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickClickHereLinkForPC();
	//		storeFrontLegacyHomePage.clickEnrollNowBtnForPCAndRC();
	//		storeFrontLegacyHomePage.enterProfileDetailsForPCAndRC(firstName,lastName,emailAddress,password,phnNumber,gender);
	//		storeFrontLegacyHomePage.clickContinueBtnForPCAndRC();
	//		storeFrontLegacyHomePage.enterIDNumberAsSponsorForPCAndRC(sponsorID);
	//		storeFrontLegacyHomePage.clickBeginSearchBtn();
	//		storeFrontLegacyHomePage.selectSponsorRadioBtn();
	//		storeFrontLegacyHomePage.clickSelectAndContinueBtnForPCAndRC();
	//		storeFrontLegacyHomePage.checkTermsAndConditionChkBoxForPCAndRC();
	//		storeFrontLegacyHomePage.clickContinueBtnForPCAndRC();
	//		storeFrontLegacyHomePage.clickContinueBtnOnAutoshipSetupPageForPC();
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.enterShippingProfileDetails(addressName, shippingProfileFirstName,shippingProfileLastName, addressLine1, postalCode, phnNumber);
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
	//		storeFrontLegacyHomePage.enterBillingInfoDetails(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
	//		storeFrontLegacyHomePage.clickCompleteEnrollmentBtn();
	//		storeFrontLegacyHomePage.clickOKBtnOfJavaScriptPopUp();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isEnrollmentCompletedSuccessfully(), "PC enrollment is not completed successfully");
	//		logout();
	//		storeFrontLegacyHomePage.loginAsPCUser(emailAddress, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedIn(),"User is not logged in successfully After enrollment");
	//		s_assert.assertAll();
	//	}
	//
	//	//RC Enrollment from corp and login (S.No 56)
	//	@Test
	//	public void testRCEnrollmentAndCheckLogin(){
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
	//		int randomNumbers = CommonUtils.getRandomNum(10000, 1000000);
	//		String firstName = TestConstantsRFL.FIRST_NAME;
	//		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
	//		String shippingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
	//		String shippingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
	//		String emailAddress = firstName+randomNum+"@xyz.com";
	//		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
	//		String postalCode = TestConstantsRFL.POSTAL_CODE;
	//		String cardNumber = TestConstantsRFL.CARD_NUMBER;
	//		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
	//		String billingProfileFirstName = TestConstantsRFL.BILLING_PROFILE_FIRST_NAME+randomNumbers;
	//		String billingProfileLastName = TestConstantsRFL.BILLING_PROFILE_LAST_NAME;
	//		String nameOnCard = firstName;
	//		String expMonth = TestConstantsRFL.EXP_MONTH;
	//		String expYear = TestConstantsRFL.EXP_YEAR;
	//		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
	//		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
	//		String gender = TestConstantsRFL.GENDER_MALE;
	//		String javaScriptPopupTxt = TestConstantsRFL.RC_ACCOUNT_CONFIRMATION_POPUP_TXT;
	//		String sponsorID = TestConstantsRFL.CID_CONSULTANT;
	//		String addressName = "Home";
	//
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.clickProductsBtn();
	//		storeFrontLegacyHomePage.selectRegimen(regimen);
	//		storeFrontLegacyHomePage.clickAddToCartBtn();
	//		storeFrontLegacyHomePage.clickCheckoutBtn();
	//		storeFrontLegacyHomePage.clickClickHereLinkForRC();
	//		storeFrontLegacyHomePage.enterProfileDetailsForPCAndRC(firstName,lastName,emailAddress,password,phnNumber,gender);
	//		storeFrontLegacyHomePage.clickCreateMyAccountBtnOnCreateRetailAccountPage();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.getJavaScriptPopUpText().contains(javaScriptPopupTxt),"Java Script Popup for RC account confirmation not present");
	//		storeFrontLegacyHomePage.clickOKBtnOfJavaScriptPopUp();
	//		storeFrontLegacyHomePage.enterIDNumberAsSponsorForPCAndRC(sponsorID);
	//		storeFrontLegacyHomePage.clickBeginSearchBtn();
	//		storeFrontLegacyHomePage.selectSponsorRadioBtn();
	//		storeFrontLegacyHomePage.clickSelectAndContinueBtnForPCAndRC();
	//		storeFrontLegacyHomePage.enterShippingProfileDetails(addressName, shippingProfileFirstName,shippingProfileLastName, addressLine1, postalCode, phnNumber);
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
	//		storeFrontLegacyHomePage.enterBillingInfoDetails(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
	//		storeFrontLegacyHomePage.clickContinueBtn();
	//		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
	//		storeFrontLegacyHomePage.clickCompleteOrderBtn();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Enrollment is not completed successfully");
	//		logout();
	//		storeFrontLegacyHomePage.loginAsRCUser(emailAddress, password);
	//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedIn(),"User is not logged in successfully After enrollment");
	//		s_assert.assertAll(); 
	//	}

	//	@Test
	//	public void testExistingConsultantEnrollment(){
	//		//Enroll a consultant
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
	//		int ssnRandomNum2 = CommonUtils.getRandomNum(00, 99);
	//		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
	//		String firstName = TestConstantsRFL.FIRST_NAME;
	//		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
	//		String emailAddress = firstName+randomNum+"@xyz.com";
	//		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
	//		String postalCode = TestConstantsRFL.POSTAL_CODE;
	//		String cardNumber = TestConstantsRFL.CARD_NUMBER;
	//		String nameOnCard = firstName;
	//		String expMonth = TestConstantsRFL.EXP_MONTH;
	//		String expYear = TestConstantsRFL.EXP_YEAR;
	//		String CID = TestConstantsRFL.CID_CONSULTANT;
	//		String kitName = "Big Business Launch Kit";
	//		String regimen = "Redefine";
	//		String enrollemntType = "Express";
	//		String phnNumber1 = "415";
	//		String phnNumber2 = "780";
	//		String phnNumber3 = "9099";
	//		storeFrontLegacyHomePage =  new NSCore3HomePage(driver);
	//		storeFrontLegacyHomePage.clickBusinessSystemBtn();
	//		storeFrontLegacyHomePage.clickEnrollNowBtnOnBusinessPage();
	//		storeFrontLegacyHomePage.enterCID(CID);
	//		storeFrontLegacyHomePage.clickSearchResults();
	//		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
	//		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
	//		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
	//		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
	//		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
	//		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
	//		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
	//		//storeFrontLegacyHomePage.enterPWS(firstName+lastName+randomNum);
	//		storeFrontLegacyHomePage.clickCompleteAccountNextBtn();
	//		storeFrontLegacyHomePage.clickTermsAndConditions();
	//		storeFrontLegacyHomePage.chargeMyCardAndEnrollMe();
	//		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"");
	//		storeFrontLegacyHomePage.clickOnRodanAndFieldsLogo();
	//		storeFrontLegacyHomePage.logOut();
	//		//Enroll with the existing email ID
	//		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
	//		storeFrontLegacyHomePage.clickEnrollNowBtnOnWhyRFPage();
	//		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
	//		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
	//		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
	//		//verify popup for existing consultant
	//		s_assert.assertTrue(storeFrontLegacyHomePage.validateExistingConsultantPopUp(emailAddress),"Existing Consultant Popup is not present");
	//		s_assert.assertAll();
	//	}




}
