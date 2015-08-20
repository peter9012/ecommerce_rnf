package com.rf.test.website.storeFront.regression.expressEnrollment.biz;

import org.testng.annotations.Test;
import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class PersonalResultsKitTest extends RFWebsiteBaseTest{

	private StoreFrontHomePage storeFrontHomePage;

	//Hybris Project-65 :: Version : 1 :: Express Enrollment USD 395 Personal Results Kit, Personal Regimen REDEFINE REGIMEN  
	@Test
	public void testExpressEnrollmentPerosnalResultsKitRedefineRegimen_65() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			driver.get(TestConstants.CONSULTANT1_PWS_URL_CA);
		}else{
			//TODO for the US
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		if(country.equalsIgnoreCase("CA")){
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_PERSONAL_CA, TestConstants.REGIMEN_NAME_REDEFINE);		
			storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
			storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
			storeFrontHomePage.enterPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterConfirmPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
			storeFrontHomePage.enterCity(TestConstants.CITY_CA);
			storeFrontHomePage.selectProvince(TestConstants.PROVINCE_CA);
			storeFrontHomePage.enterPostalCode(TestConstants.POSTAL_CODE_CA);
			storeFrontHomePage.enterPhoneNumber(TestConstants.PHONE_NUMBER);
		}else{
			//TODO the US part
		}
		storeFrontHomePage.enterEmailAddress(TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-66 :: Version : 1 :: Express Enrollment USD 395 Personal Results Kit, Personal Regimen REVERSE REGIMEN  
	@Test
	public void testExpressEnrollmentPersonalRegimenKitReverseRegimen_66() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			driver.get(TestConstants.CONSULTANT1_PWS_URL_CA);
		}else{
			//TODO for the US
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		if(country.equalsIgnoreCase("CA")){
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_PERSONAL_CA, TestConstants.REGIMEN_NAME_REVERSE);		
			storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
			storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
			storeFrontHomePage.enterPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterConfirmPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
			storeFrontHomePage.enterCity(TestConstants.CITY_CA);
			storeFrontHomePage.selectProvince(TestConstants.PROVINCE_CA);
			storeFrontHomePage.enterPostalCode(TestConstants.POSTAL_CODE_CA);
			storeFrontHomePage.enterPhoneNumber(TestConstants.PHONE_NUMBER);
		}else{
			//TODO the US part
		}
		storeFrontHomePage.enterEmailAddress(TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-67 :: Version : 1 :: Express Enrollment USD 395 Personal Results Kit, Personal Regimen UNBLEMISH REGIMEN  
	@Test
	public void testExpressEnrollmentPersonalResultsKitUnblemishRegimen_67() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			driver.get(TestConstants.CONSULTANT1_PWS_URL_CA);
		}else{
			//TODO for the US
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		if(country.equalsIgnoreCase("CA")){
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_PERSONAL_CA, TestConstants.REGIMEN_NAME_UNBLEMISH);		
			storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
			storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
			storeFrontHomePage.enterPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterConfirmPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
			storeFrontHomePage.enterCity(TestConstants.CITY_CA);
			storeFrontHomePage.selectProvince(TestConstants.PROVINCE_CA);
			storeFrontHomePage.enterPostalCode(TestConstants.POSTAL_CODE_CA);
			storeFrontHomePage.enterPhoneNumber(TestConstants.PHONE_NUMBER);
		}else{
			//TODO the US part
		}
		storeFrontHomePage.enterEmailAddress(TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-68 :: Version : 1 :: Express Enrollment USD 395 Personal Results Kit, Personal Regimen SOOTHE REGIMEN 
	@Test
	public void testExpressEnrollmentPersonalKitSootheRegimen_68() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			driver.get(TestConstants.CONSULTANT1_PWS_URL_CA);
		}else{
			//TODO for the US
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		if(country.equalsIgnoreCase("CA")){
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_PERSONAL_CA, TestConstants.REGIMEN_NAME_SOOTHE);		
			storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
			storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
			storeFrontHomePage.enterPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterConfirmPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
			storeFrontHomePage.enterCity(TestConstants.CITY_CA);
			storeFrontHomePage.selectProvince(TestConstants.PROVINCE_CA);
			storeFrontHomePage.enterPostalCode(TestConstants.POSTAL_CODE_CA);
			storeFrontHomePage.enterPhoneNumber(TestConstants.PHONE_NUMBER);
		}else{
			//TODO the US part
		}
		storeFrontHomePage.enterEmailAddress(TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		logout();
		s_assert.assertAll();
	}

}
