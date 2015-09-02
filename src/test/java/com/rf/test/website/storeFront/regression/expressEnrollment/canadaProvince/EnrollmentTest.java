package com.rf.test.website.storeFront.regression.expressEnrollment.canadaProvince;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class EnrollmentTest extends RFWebsiteBaseTest{

	private StoreFrontHomePage storeFrontHomePage;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String postalCode = null;
	private String phoneNumber = null;

	//[Hybris Project-3612,Hybris Project-1670,Hybris Project-1669,Hybris Project-1668,Hybris Project-1667,Hybris Project-1665,Hybris Project-1664,Hybris Project-1663,Hybris Project-1662,Hybris Project-1661]
	@Test(dataProvider="rfTestData")
	public void testExpressEnrollmentCanadaProvince(String province) throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		kitName = TestConstants.KIT_PRICE_PERSONAL_CA;			 
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME, TestConstants.PASSWORD, addressLine1, city,province, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForTermsAndConditions(), "PopUp for terms and conditions is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		s_assert.assertAll();
	}

	//Hybris Project-1699 :: Version : 1 :: Express Enrollment for Nunavut province and tryto ship adhoc order at quebec address. 
	@Test(enabled=false) //WIP
	public void testExpressEnrollmentNunavutProvince_1699() throws InterruptedException{

		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		kitName = TestConstants.KIT_PRICE_PERSONAL_CA;			 
		addressLine1 = TestConstants.ADDRESS_LINE_1_QUEBEC;
		city = TestConstants.CITY_QUEBEC;
		postalCode = TestConstants.POSTAL_CODE_QUEBEC;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME, TestConstants.PASSWORD, addressLine1, city,TestConstants.PROVINCE_NUNAVUT, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForTermsAndConditions(), "PopUp for terms and conditions is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		s_assert.assertAll();
	}
	
	//Hybris Project-1698 :: Version : 1 :: Express Enrollment for Northwest territories province and adding qubec address and make it default.
	@Test(enabled=false) //WIP
	public void testExpressEnrollmentNorthWestProvince_1698() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		kitName = TestConstants.KIT_PRICE_PERSONAL_CA;			 
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME, TestConstants.PASSWORD, addressLine1, city,TestConstants.PROVINCE_NORTHWEST_TERRITORIES, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForTermsAndConditions(), "PopUp for terms and conditions is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//In progress
		s_assert.assertAll();
	}
	
	//Hybris Project-1292 :: Version : 1 :: Customer living in Quebec cannot be enrolled as consultant. 
	@Test(enabled=false) //WIP
	public void testCustomerLivingInQuebecCannotEnroll_1292() throws InterruptedException{
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		kitName = TestConstants.KIT_PRICE_PERSONAL_CA;			 
		addressLine1 = TestConstants.ADDRESS_LINE_1_QUEBEC;
		city = TestConstants.CITY_QUEBEC;
		postalCode = TestConstants.POSTAL_CODE_QUEBEC;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);		
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		s_assert.assertTrue(storeFrontHomePage.verifyQuebecProvinceIsDisabled(),"Quebec province in the province drop down is not disabled");
		s_assert.assertAll();
	}
	
}
