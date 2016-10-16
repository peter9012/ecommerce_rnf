package com.rf.test.website.storeFront.hybris;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCartTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCheckoutTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerTabPage;
import com.rf.pages.website.cscockpit.CSCockpitLoginPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderTabPage;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontAccountTerminationPage;
import com.rf.pages.website.storeFront.StoreFrontCartAutoShipPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFStoreFrontWebsiteBaseTest;
import com.rf.test.website.RFWebsiteBaseTest;

public class StandardEnrollmentValidationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(EnrollmentValidationTest.class.getName());
	public String emailID=null;
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontAccountTerminationPage storeFrontAccountTerminationPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitCustomerSearchTabPage cscockpitCustomerSearchTabPage;
	private CSCockpitOrderTabPage cscockpitOrderTabPage;
	private CSCockpitCustomerTabPage cscockpitCustomerTabPage;

	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String postalCode = null;
	private String phoneNumber = null;
	private String country = null;
	private String RFO_DB = null;
	private String env = null;
	private String state = null; 
	private int randomNum; 	

	@BeforeClass
	public void setupDataForEnrollmentValidationTest() throws InterruptedException{	
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);

		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 
		env = driver.getEnvironment();
		country = driver.getCountry();

		kitName = TestConstants.KIT_NAME_BIG_BUSINESS; 	
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){				 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{			
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			state = TestConstants.STATE_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;			
		}
	}

	//Hybris Project-2489:Verify if Consultant can use same prefix, when he is subscribing to pulse. ( Consultant's trial version)
	@Test
	public void testVerifyConsultantCanUaseSamePrefixWhenSubscribingToPulse_2489() throws InterruptedException   {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		String comPWS=storeFrontHomePage.getDotComPWS(driver.getCountry());
		String bizPWS=storeFrontHomePage.getDotBizPWS(driver.getCountry());
		String emailID=storeFrontHomePage.getEmailId(driver.getCountry());
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		// storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		//validate on the confirmation page user lands on .biz site
		//s_assert.assertTrue(storeFrontHomePage.validateUserLandsOnPWSbizSite(), "user didn't land on PWS .biz site");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		//Fetch the PWS url
		//String currentPWSUrl=driver.getCurrentUrl();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		//goto .com site and navigate to Account Info->Autoship tatus->Cancel pulse subscription
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontHomePage.clickOnYourAccountDropdown();
		storeFrontConsultantPage.clickOnAutoshipStatusLink();
		storeFrontConsultantPage.subscribeToPulse();
		//verify default pulse prefix suggestions
		s_assert.assertTrue(storeFrontConsultantPage.validatePulsePrefixSuggestionsAvailable(), "pulse prefix suggestions are not available");
		//verify each suggestion of pulse prefix
		s_assert.assertTrue(storeFrontConsultantPage.getDotComPWS(driver.getCountry()).contains(comPWS), "pulse prefix dot com pws is not present");
		s_assert.assertTrue(storeFrontConsultantPage.getDotBizPWS(driver.getCountry()).contains(bizPWS), "pulse prefix dot biz pws is not present");
		s_assert.assertTrue(storeFrontConsultantPage.getEmailId(driver.getCountry()).contains(emailID), "Email id with pulse prefix is not present");
		s_assert.assertAll(); 
	}

	// Hybris Project-1294:10. Standard EnrollmentTest switch to Express EnrollmentTest
	@Test
	public void testStandardEnrollmentSwitchToExpresEnrollment_1294() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, state,postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		//click on switch to 'express enrollment' link
		storeFrontHomePage.clickSwitchToExpressEnrollmentLink();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		s_assert.assertAll();
	}

	//Hybris Project-3955:BIZ:During enrollment process, switch from Standard Enrollment to Express Enrollment
	//Hybris Project-1296:12.Standard EnrollmentTest switch to Express EnrollmentTest - Step 5
	@Test
	public void testStandardEnrollmentSwitchToExpresEnrollmentStep5_1296_3955() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		storeFrontHomePage.openPWSSite(country, env);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		//storeFrontHomePage.searchCID();
		//storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, state,postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		//click on switch to 'express enrollment' link
		storeFrontHomePage.clickSwitchToExpressEnrollmentLink();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		s_assert.assertAll();
	}

	// Hybris Project-4155 :: Version : 1 :: Verify special characters are not allowing
	@Test
	public void testVerifySpecialCharactersAreNotAllowed_4155() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		String enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		String sRandName = RandomStringUtils.randomAlphabetic(12);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, sRandName, TestConstants.PASSWORD, addressLine1, city, state,postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		int randomNumber = CommonUtils.getRandomNum(100, 1000);
		String prefixNameWithSpecialChar = TestConstants.FIRST_NAME+randomNumber+"$";
		String prefixName = TestConstants.FIRST_NAME+randomNumber;
		storeFrontHomePage.enterWebsitePrefixName(prefixNameWithSpecialChar);
		s_assert.assertTrue(storeFrontHomePage.verifySpecialCharNotAcceptInPrefixName(), "Special Char is accepcted by prefix name 1");
		storeFrontHomePage.enterWebsitePrefixName(prefixName);
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoshipStatusLink();
		storeFrontAccountInfoPage.subscribeToPulse();
		storeFrontAccountInfoPage.enterWebsitePrefixName(prefixNameWithSpecialChar);
		s_assert.assertTrue(storeFrontAccountInfoPage.verifySpecialCharNotAcceptInPrefixName(), "Special Char is accepcted by prefix name 2");
		s_assert.assertAll(); 
	}

	//Hybris Project-52 :: Version : 1 :: BIZ:Standard Enroll Kit USD $45 Business Portfolio (CRP:Y P:Y) 
	@Test
	public void testStandardEnrollmentBusinessPortfolio_52() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String kitName = TestConstants.KIT_NAME_PORTFOLIO;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName);
		// assert portfolio kit is direct redirect to setup account page
		s_assert.assertTrue(storeFrontHomePage.verifyCreateAccountpageIsDisplayed(), "Setup account page is not displayed");
		storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.enterLastName(TestConstants.LAST_NAME+randomNum);
		storeFrontHomePage.enterEmailAddress(TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX);
		storeFrontHomePage.enterPassword(password);
		storeFrontHomePage.enterConfirmPassword(password);
		storeFrontHomePage.enterAddressLine1(addressLine1);
		storeFrontHomePage.enterCity(city);
		storeFrontHomePage.selectProvince(state);
		storeFrontHomePage.enterPostalCode(postalCode);
		storeFrontHomePage.enterPhoneNumber(phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.clickNextButton();
		// assert enrollment is standard or not
		s_assert.assertTrue(storeFrontHomePage.verifyCRPSelectionpageIsDisplayed(), "It is not standard enrollment");
		s_assert.assertAll();
	}

	// Hybris Project-87 :: Version : 1 :: Standard Enrollment Billing Profile Edit Shipping Info 
	@Test(enabled=false)// Covered in TC-45
	public void testStandardEnrollmentExpressBusinessKitRedefineRegimenEdit_87() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String newAddressLine1=null;
		String enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, state,postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.checkPulseAndCRPEnrollment();
		storeFrontHomePage.clickNextButton();		
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.clickOnReviewAndConfirmShippingEditBtn();
		String newFirstName = TestConstants.FIRST_NAME+randomNum;
		String newLastName = TestConstants.LAST_NAME+randomNum;
		storeFrontHomePage.enterUserInformationForEnrollment(newFirstName, newLastName, password, newAddressLine1, city,state,postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.isReviewAndConfirmPageContainsShippingAddress(newAddressLine1), "Shiiping address is not updated on Review and Confirm page after EDIT");
		s_assert.assertTrue(storeFrontHomePage.isReviewAndConfirmPageContainsFirstAndLastName(newFirstName+" "+newLastName), "First and last Name is not updated on Review and Confirm page after EDIT");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		//storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-4161 :: Version : 1 :: During enrollment customer not opt for pulse but they still able to access the pws as a trial version 
	@Test
	public void testCustomerEnrollWithoutPulseCanAccessPws_4161() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		String firstName = TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, state,postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		String comPWS=storeFrontHomePage.getDotComPWS();
		String bizPWS=storeFrontHomePage.getDotBizPWS();
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		//storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		//validate on the confirmation page user lands on .biz site
		s_assert.assertTrue(storeFrontHomePage.validateUserLandsOnPWSbizSite(), "user didn't land on PWS .biz site");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		//Fetch the PWS url
		//String currentPWSUrl=driver.getCurrentUrl();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		logout();
		//validate with the current PWSUrl on .com site
		//driver.get(storeFrontHomePage.navigateToCommercialWebsite(currentPWSUrl));
		driver.get(bizPWS);
		s_assert.assertTrue(storeFrontHomePage.validatePWS(), "Dot biz PWS is not active");  
		driver.get(comPWS);
		s_assert.assertTrue(storeFrontHomePage.validatePWS(), "Dot com PWS is not active");  
		s_assert.assertAll();
	}


	// Hybris Project-1291:5. Terms and Conditions- Standard enrollment with out CRP or Pulse
	@Test
	public void testStandardEnrollmentTermsAndConditionsWithoutCRPOrPulse_1291() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, state,postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.checkCRPCheckBox();
		storeFrontHomePage.checkPulseCheckBox();
		s_assert.assertFalse(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsSelected(), "Subscribe to pulse checkbox not selected");
		s_assert.assertFalse(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsSelected(), "Enroll to CRP checkbox not selected");
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-1289:3. Terms and Conditions - Standard Enrollment only Pulse
	@Test
	public void testTermsAndConditionsForConsultantStandardEnrollmentForPulse_1289() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String profileName = TestConstants.FIRST_NAME+randomNum;
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{

			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, profileName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		//At autoship options page check only the pulse check box and proceed..
		//storeFrontHomePage.checkPulseAndCRPEnrollment();
		storeFrontHomePage.uncheckCRPCheckBox();
		storeFrontHomePage.clickNextButton();
		//validate that 'I have read and accepted all Terms and Conditions for the Consultant Application, Pulse and Policies and Procedures' is displayed
		//s_assert.assertTrue(storeFrontHomePage.validateTermsAndConditionsForConsultantApplicationPulse(), "' I have read and accepted all Terms and Conditions for the Consultant Application, Pulse and Policies and Procedures.' is not present"); 
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP is present after uncheck the CRP");
		s_assert.assertAll();
	}

	// Hybris Project-1290:4. Terms and Conditions- Standard Enrollment only CRP
	@Test
	public void testTermsAndConditionsForConsultantStandardEnrollmentForCRP_1290() throws InterruptedException  {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		String profileName = TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, profileName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		//At autoship options page check only the CRP check box and proceed..
		//storeFrontHomePage.checkPulseAndCRPEnrollment();
		storeFrontHomePage.uncheckPulseCheckBox(); 
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.applyPriceFilterHighToLow();
		//Add a product to CRP..
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickNextOnCRPCartPage();
		//validate that 'I have read and accepted all Terms and Conditions for the Consultant Application, CRP and Policies and Procedures' is displayed
		//s_assert.assertTrue(storeFrontHomePage.validateTermsAndConditionsForConsultantApplicationCRP(), "' I have read and accepted all Terms and Conditions for the Consultant Application, CRP and Policies and Procedures.' is not present");
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontAccountInfoPage.validatePulseCancelled(), "Pulse is subscribed after uncheck the pulse");
		s_assert.assertAll();
	}

	// Hybris Project-2253:Pulse Prefix Logic
	@Test
	public void testPulsePrefixLogicForConsultant_2253() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String emailAddress = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String comPWS = null;
		String bizPWS = null;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		//enroll a consultant with standard enrollment
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, firstName,TestConstants.LAST_NAME, emailAddress, password, addressLine1, city, state,postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		comPWS = storeFrontHomePage.getDotComPWS();
		bizPWS = storeFrontHomePage.getDotBizPWS();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		//storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		logout();
		//login with .com pws of consultant
		driver.get(comPWS);
		storeFrontConsultantPage=storeFrontHomePage.loginAsConsultant(emailAddress,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Unable to login with .com pws of consultant");
		driver.get(driver.getURL()+"/"+driver.getCountry());
		//login with .biz pws of consultant
		driver.get(bizPWS);
		storeFrontConsultantPage=storeFrontHomePage.loginAsConsultant(emailAddress,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Unable to login with .biz pws of consultant");
		//Cancell the pulse of enrolled consultant
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.cancelPulseSubscription();
		s_assert.assertTrue(storeFrontAccountInfoPage.validatePulseCancelled(),"pulse has not been cancelled for consultant");
		driver.get(driver.getURL()+"/"+driver.getCountry());
		//Retry to login on .com pws of consultant after cancelled Paid pulse and check login successful
		driver.get(comPWS);
		storeFrontConsultantPage=storeFrontHomePage.loginAsConsultant(emailAddress,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Unable to login with .com pws of consultant");
		driver.get(driver.getURL()+"/"+driver.getCountry());
		//Retry to login in to .biz pws of consultant after cancelled Paid pulse and check login is successful
		driver.get(bizPWS);
		storeFrontConsultantPage=storeFrontHomePage.loginAsConsultant(emailAddress,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Unable to login with .biz pws of consultant");
		//enroll a consultant with express enrollment
		driver.get(driver.getURL()+"/"+driver.getCountry());
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		storeFrontHomePage = new StoreFrontHomePage(driver);
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		firstName=TestConstants.FIRST_NAME+randomNum;
		emailAddress = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		if(country.equalsIgnoreCase("CA")){

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		if(country.equalsIgnoreCase("us"))
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName,TestConstants.LAST_NAME, emailAddress, password, addressLine1, city,TestConstants.PROVINCE_ALABAMA_US, postalCode, phoneNumber);
		else
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName,TestConstants.LAST_NAME, emailAddress, password, addressLine1, city,TestConstants.PROVINCE_CA, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		comPWS = storeFrontHomePage.getDotComPWS();
		bizPWS = storeFrontHomePage.getDotBizPWS();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForTermsAndConditions(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		logout();
		//login with .com pws of consultant
		driver.get(comPWS);
		storeFrontConsultantPage=storeFrontHomePage.loginAsConsultant(emailAddress,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Unable to login with .com pws of consultant");
		driver.get(driver.getURL()+"/"+driver.getCountry());
		//login with .biz pws of consultant
		driver.get(bizPWS);
		storeFrontConsultantPage=storeFrontHomePage.loginAsConsultant(emailAddress,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Unable to login with .biz pws of consultant");
	}

	//Hybris Project-137:Enroll and manage my autoships in Canada - consultant
	@Test
	public void testEnrollAndManageMyAutoshipsInCanada_137() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName = TestConstants.FIRST_NAME+randomNum;
		if(country.equalsIgnoreCase("CA")){

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		//   kitName = TestConstants.KIT_NAME_EXPRESS;    
		//   addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		//   city = TestConstants.CITY_CA;
		//   postalCode = TestConstants.POSTAL_CODE_CA;
		//   phoneNumber = TestConstants.PHONE_NUMBER_CA;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsNotSelected(), "Subscribe to pulse checkbox selected after uncheck");
		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsNotSelected(), "Enroll to CRP checkbox selected after uncheck");
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontConsultantPage.clickOnYourAccountDropdown();
		storeFrontConsultantPage.clickOnAutoshipStatusLink();
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
		storeFrontAccountInfoPage.clickOnEnrollInCRP();
		storeFrontHomePage.clickOnAddToCRPButtonCreatingCRPUnderBizSite();
		storeFrontHomePage.clickOnCRPCheckout();
		storeFrontHomePage.clickOnBillingNextStepButton();
		storeFrontHomePage.clickOnSetupCRPAccountBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyOrderConfirmation(), "Order Confirmation Message has not been displayed");
		storeFrontHomePage.clickOnGoToMyAccountToCheckStatusOfCRP();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCurrentCRPStatus(), "Current CRP Status has not been Enrolled");
		storeFrontAccountInfoPage.clickOnSubscribeToPulseBtn();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnAccountInfoNextButton();
		storeFrontUpdateCartPage.clickOnSubscribePulseTermsAndConditionsChkbox();
		storeFrontUpdateCartPage.clickOnSubscribeBtn();
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account info page has not been displayed");
		if(driver.getCountry().equalsIgnoreCase("ca")){
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyConsultantCantShipToQuebecMsg()," Consultants cannot ship to Quebec message not present on UI");
		}
		s_assert.assertAll();
	}

	//Hybris Project-4665:consultant re-enrollment within 6 month
	@Test(enabled=false)//INVALID Test
	public void testConsultantRe_enrollmentWithin6month_4665() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		String currentCRPStatus = storeFrontAccountInfoPage.getCRPStatusFromUI();
		System.out.println(currentCRPStatus);
		String currentPulseStatus = storeFrontAccountInfoPage.getPulseStatusFromUI();
		System.out.println(currentPulseStatus);
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.selectTerminationReason();
		storeFrontAccountTerminationPage.enterTerminationComments();
		storeFrontAccountTerminationPage.clickOnAgreementCheckBox();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.clickOnEnrollUnderLastUpline();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		//  storeFrontHomePage.clickOnEnrollmentNextButton();
		storeFrontHomePage.enterPasswordForReactivationForConsultant();
		storeFrontHomePage.clickOnLoginToReactiveMyAccountForConsultant();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontAccountInfoPage.getCRPStatusFromUI().equalsIgnoreCase(currentCRPStatus),"CRP Status is expected to be enrolled");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyPulseStatusAfterReactivation(currentPulseStatus),"pulse status is not same as old status");	
		s_assert.assertAll();
	}


	//Hybris Project-3925:CORP: Enroll CA/US User, Sponsor without PWS & User Enrolls without Pulse
	@Test(enabled=false) //test needs update 
	public void testEnrollCAAndUsUserSponsorWithoutPWSAndUserEnrollsWithoutPulse_3925() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String sponserHavingPulse = null;
		while(true){
			List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));
			String consultantEmailID = String.valueOf(getValueFromQueryResult(sponserList, "Username"));
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontHomePage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyPulseSubscription();
		s_assert.assertTrue(storeFrontAccountInfoPage.validatePulseCancelled(), "Pulse have been not cancled");
		logout();

		driver.get(driver.getURL()+"/"+driver.getCountry());

		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();

		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
		String sponsorId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		storeFrontHomePage.searchCID(sponsorId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		s_assert.assertTrue(storeFrontHomePage.verifyCurrentUrlContainCorp(), "Current URL dose not contain Corp");
		s_assert.assertAll();
	}

	// Hybris Project-3926:CORP: Enroll CA/US User, Sponsor with PWS & User Enrolls without Pulse
	@Test
	public void testEnrollCAOrUSUserSponsorWithPWSAndEnrollWithoutPulse_3926() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String socialInsuranceNumber1 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		String countryNameForUS = "us";
		String countryNameForCA = "ca";
		String countryIdForUS = "236";
		String countryIdForCA = "40";
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			state = TestConstants.PROVINCE_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		List<Map<String, Object>> sponserList = null;
		String sponserHavingPulse = null;
		String sponsorPWS = null;
		List<Map<String, Object>> sponsorIdList = null;
		String idForConsultant = null;
		String PWSAfterSelectSponsor = null;
		String userPWS = null;
		String PWSAfterEnrollment = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		// Get Canadian sponser with PWS from database
		sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));
		sponsorPWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
		idForConsultant = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
		storeFrontHomePage.searchCID(idForConsultant);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		PWSAfterSelectSponsor = driver.getCurrentUrl();
		s_assert.assertTrue(PWSAfterSelectSponsor.contains(sponsorPWS.split("\\:")[1].toLowerCase()), "CA sponsor PWS for ca corp"+sponsorPWS+" and on UI after select ca sponsor from ca corp site is "+PWSAfterSelectSponsor);
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		userPWS = storeFrontHomePage.getDotBizPWS();
		storeFrontHomePage.checkPulseCheckBox();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		PWSAfterEnrollment =driver.getCurrentUrl();
		s_assert.assertTrue(PWSAfterEnrollment.contains(userPWS.split("\\:")[1]), "CA User PWS for ca corp"+userPWS+" and on UI after complete the enrollment ca sponsor from ca corp site is "+PWSAfterEnrollment);
		logout();
		//***THIS TEST IS DONE HERE ***
		driver.get(driver.getURL()+"/"+driver.getCountry());
		s_assert.assertAll();
	}


	// Hybris Project-1281:15.North Dakota - Standard enrollment
	@Test
	public void testNorthDakota_StandardEnrollment_1281() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("us")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			country = driver.getCountry();
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
			String firstName=TestConstants.FIRST_NAME+randomNum;
			String provinceOther = TestConstants.PROVINCE_US;
			String provinceNorthDakota = TestConstants.PROVINCE_NORTH_DAKOTA;
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.clickOnLiveInNorthDakotaLink();
			storeFrontHomePage.enterUserInformationOnAccountInfo(firstName,password,addressLine1,city,postalCode,phoneNumber,provinceOther);
			storeFrontHomePage.clickNextButton();
			s_assert.assertTrue(storeFrontHomePage.verifyingMessageForNextDakotaPresent(),"Enter address from North Dakota or purchase a kit to go next message is not present");
			storeFrontHomePage.enterPassword(password);
			storeFrontHomePage.enterConfirmPassword(password);
			storeFrontHomePage.selectProvince(provinceNorthDakota);
			storeFrontHomePage.clickNextButton();
			storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
			storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
			storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
			storeFrontHomePage.clickNextButton();
			storeFrontHomePage.uncheckPulseAndCRPEnrollment();
			s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsNotSelected(), "Subscribe to pulse checkbox selected after uncheck");
			s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsNotSelected(), "Enroll to CRP checkbox selected after uncheck");
			storeFrontHomePage.clickNextButton();
			s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
			storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
			storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
			storeFrontHomePage.checkTheIAgreeCheckBox();
			storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
			s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
			storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
			storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
			s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");

			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for US env");
		}
	}


	// Hybris Project-88:Standard Enrollment Billing Profile Billing Info - Edit
	@Test(enabled=false)// Covered in TC-45
	public void testStandardEnrollmentBillingProfileBillingInfo_Edit_88() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName=TestConstants.FIRST_NAME+randomNum;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		storeFrontHomePage.clickOnReviewAndConfirmBillingEditBtn();
		storeFrontHomePage.selectNewBillingCardExpirationDateAfterEdit();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-89:Standard Enrollment Billing Profile Main Account Info 
	@Test(enabled=false)//Unexpected alert
	public void testStandardEnrollmentBillingProfileMainAccountInfo_89() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		String newAddressLine1 = null;
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName=TestConstants.FIRST_NAME+randomNum;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			newAddressLine1 = TestConstants.NEW_ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			newAddressLine1 = TestConstants.NEW_ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, driver.getEnvironment());
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		storeFrontHomePage.clickOnReviewAndConfirmShippingEditBtn();
		storeFrontHomePage.enterPassword(password);
		storeFrontHomePage.enterConfirmPassword(password);
		storeFrontHomePage.enterNewShippingAddressLine1DuringEnrollment(newAddressLine1);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.verifyNewAddressPresentInMainAccountInfo(newAddressLine1),"new address not present in main account info");
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-90:Standard Enrollment Billing Profile - Main Account Info - New
	@Test
	public void testStandardEnrollmentBillingProfile_MainAccountInfo_New_90() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		String newBillingAddName = firstName+randomNum;
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		storeFrontHomePage.clickOnReviewAndConfirmBillingEditBtn();
		storeFrontHomePage.clickAddNewAddressLink();
		storeFrontHomePage.enterNewBillingAddressNameDuringEnrollment(newBillingAddName+" "+lastName);
		storeFrontHomePage.enterNewBillingAddressLine1DuringEnrollment(addressLine1);
		storeFrontHomePage.enterNewBillingAddressCityDuringEnrollment(city);
		storeFrontHomePage.selectNewBillingAddressStateDuringEnrollment();
		storeFrontHomePage.enterNewBillingAddressZipCodeDuringEnrollment(postalCode);
		storeFrontHomePage.enterNewBillingNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.validateNewBillingAddressPresentOnReviewPage(newBillingAddName),"new billing address is not present on Review and confirm page");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}


	//Hybris Project-3813:Click "Enroll Now" on BIZ site for a Logged in user - Page directed to "Select a Kit" page
	@Test(enabled=false)//Covered in TC 62
	public void testEnrollNowOnBizSiteForLoggedInUser_3813(){
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		storeFrontHomePage.openPWS(PWS);
		//click Enroll now link..
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		//validate 'select sponsor page' is skipped and is not displayed
		s_assert.assertTrue(!driver.getCurrentUrl().contains("SelectSponsorPage"), "select sponsor page is displayed!!");
		//validate 'select kit' page is displayed..
		s_assert.assertTrue(driver.getCurrentUrl().contains("kitproduct"), "Select Kit page is not displayed!!");
		s_assert.assertAll();
	}

	// Hybris Project-3973:Verify if Consultant can use same prefix, when he is subscribing to pulse again
	@Test
	public void testVerifyConsultantCanUseSamePrefixWhileSubscribingToPulse_3973() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);

		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton(); 
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		String websitePrefixDuringEnroll=storeFrontHomePage.getExistingWebsitePrefixName();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		//storeFrontHomePage
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//cancel pulse of consultant and subscribe again and verify prefix suggestion.
		storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontHomePage.clickOnYourAccountDropdown();
		storeFrontHomePage.clickOnAutoshipStatusLink();
		s_assert.assertTrue(storeFrontAccountInfoPage.validateSubscribeToPulse(),"This user does not have pulse subscribed");
		storeFrontHomePage.cancelPulseSubscription();
		s_assert.assertTrue(storeFrontAccountInfoPage.validatePulseCancelled(),"pulse subscription is not cancelled for the user");
		storeFrontAccountInfoPage.clickOnOnlySubscribeToPulseBtn();
		s_assert.assertTrue(storeFrontAccountInfoPage.getWebsitePrefixName().contains(websitePrefixDuringEnroll),"While subscribing to pulse the same website prefix is not suggested for user");
		storeFrontAccountInfoPage.clickOnNextDuringPulseSubscribtion();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnAccountInfoNextButton();
		storeFrontUpdateCartPage.clickOnSubscribePulseTermsAndConditionsChkbox();
		storeFrontUpdateCartPage.clickOnSubscribeBtn();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontAccountInfoPage.validateSubscribeToPulse(),"pulse is not subscribed for the user");
		s_assert.assertAll();
	}

	//Hybris Project-2372:Email Validation for consultant trying to enroll under valid sponsor and complete enrollment.
	@Test
	public void testEmailValidationForConsultantTryingToEnrollUnderValidSponsor_2372() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		if(driver.getCountry().equalsIgnoreCase("US")){
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String  consultantEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME, consultantEmailAddress, password, addressLine1, city,state, postalCode, phoneNumber);
		//validate existing Consultant popup should not be displayed..
		s_assert.assertFalse(storeFrontHomePage.validateExistingConsultantPopup(), "Existing user popup is present!!");
		storeFrontHomePage.clickNextButton();
		s_assert.assertAll(); 
	}


	//Hybris Project-3955:BIZ:During enrollment process, switch from Standard Enrollment to Express Enrollment
	@Test(enabled=false) //Covered in TC 1296
	public void testDuringEnrollmentProcess_switchFromStandardToExpressEnrollment_3955() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickSwitchToExpressEnrollmentLink();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForTermsAndConditions(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-2214:CC enrollment who has terminated his account for LESS than 6 months enrolling under different sponso
	@Test
	public void testTerminatedConsultantEnrollmentWithIn6MonthUnderDifferentSponsor_2214() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			consultantEmailID= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.selectTerminationReason();
		storeFrontAccountTerminationPage.enterTerminationComments();
		storeFrontAccountTerminationPage.clickOnAgreementCheckBox();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		//get sponsor
		List<Map<String, Object>> randomConsultantListForSponsor = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantListForSponsor, "AccountID"));
		logger.info("Account Id of the user is "+accountID);
		// Get Account Number
		List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String sponsorID = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
		storeFrontHomePage.searchCID(sponsorID);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.clickOnEnrollUnderLastUpline();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		//  storeFrontHomePage.clickOnEnrollmentNextButton();
		storeFrontHomePage.enterPasswordForReactivationForConsultant();
		storeFrontHomePage.clickOnLoginToReactiveMyAccountForConsultant();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();

	}

	//Hybris Project-4768:consultant can use a previously used prefix if user re-enrolling with in six months
	@Test
	public void testConsultantCanUsePreviousPrefixIfEnrollingWithin6Months_4768() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		final String firstName=TestConstants.FIRST_NAME+randomNum;
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		String comPWS=storeFrontHomePage.getDotComPWS(driver.getCountry());
		/*  String bizPWS=storeFrontHomePage.getDotBizPWS(driver.getCountry());
					  String emailID=storeFrontHomePage.getEmailId(driver.getCountry());*/
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		// storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		//Fetch the PWS url
		//String currentPWSUrl=driver.getCurrentUrl();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		//goto .com site and navigate to Account Info->Autoship tatus->Cancel pulse subscription
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontHomePage.clickOnYourAccountDropdown();
		storeFrontConsultantPage.clickOnAutoshipStatusLink();
		storeFrontConsultantPage.subscribeToPulse();
		//verify default pulse prefix suggestions
		s_assert.assertTrue(storeFrontConsultantPage.validatePulsePrefixSuggestionsAvailable(), "pulse prefix suggestions are not available");
		//verify each suggestion of pulse prefix
		s_assert.assertTrue(storeFrontConsultantPage.getDotComPWS(driver.getCountry()).contains(comPWS), "pulse prefix dot com pws is not present");
		//Terminate the Consultant..
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickConfirmTerminationBtn();  
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		//ReEnroll consultant with same mailID..
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);  
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(firstName+TestConstants.EMAIL_ADDRESS_SUFFIX);
		storeFrontHomePage.enterPasswordForReactivationForConsultant();
		storeFrontHomePage.clickOnLoginToReactiveMyAccountForConsultant();
		s_assert.assertAll(); 
	}

	// Hybris Project-98:SSN Validation for active consultant trying to re enroll
	@Test
	public void testValidateActiveConsultantReEnroll_98() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		country = driver.getCountry();
		env = driver.getEnvironment();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}
		//Get Sponser from database.
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		String consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String sponsorId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID(sponsorId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum,emailAddress, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.checkPulseAndCRPEnrollment();
		storeFrontHomePage.uncheckPulseCheckBox();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.applyPriceFilterHighToLow();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickNextOnCRPCartPage();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		//Get Status and account id of newly created Consultant from database.
		List<Map<String, Object>> consultantDetails=DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,emailAddress),RFO_DB);
		String accountStatusId= String.valueOf(getValueFromQueryResult(consultantDetails, "AccountStatusId"));
		String accountNumberOfEnrolled = String.valueOf(getValueFromQueryResult(consultantDetails, "AccountNumber"));
		//assert account status is active in dataBase.
		s_assert.assertTrue(storeFrontHomePage.verifyEnrolledUserStatus(accountStatusId), "Status of enrolled user is not active in database");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID(accountNumberOfEnrolled);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNumber, TestConstants.LAST_NAME+randomNumber, TestConstants.FIRST_NAME+randomNumber+TestConstants.EMAIL_ADDRESS_SUFFIX, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNumber);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		s_assert.assertTrue(storeFrontHomePage.validateExistingConsultantPopup(), "Existing consultant popup is not present");
		s_assert.assertAll();
	}

	// Hybris Project-99:SSN Validation for consultant trying to enroll under Sponsor
	@Test
	public void testSSNValidationForConsultantEnrollingUnderSponser_99() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		country = driver.getCountry();
		env = driver.getEnvironment();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}
		//Get Sponser from database.
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		String consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String sponsorId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID(sponsorId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum,emailAddress, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.checkPulseAndCRPEnrollment();
		storeFrontHomePage.uncheckPulseCheckBox();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.applyPriceFilterHighToLow();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickNextOnCRPCartPage();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		//Get Status id of newly created Consultant from database.
		List<Map<String, Object>> consultantDetails=DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,emailAddress),RFO_DB);
		String accountStatusId= String.valueOf(getValueFromQueryResult(consultantDetails, "AccountStatusId"));
		//assert account status is active in dataBase.
		s_assert.assertTrue(storeFrontHomePage.verifyEnrolledUserStatus(accountStatusId), "Status of enrolled user is not active in database");
		s_assert.assertAll();
	}	

	//Hybris Project-1273:18.Change Social Security Number to Social Insurance Number at Standard Enrollment for Canada users
	@Test
	public void testChangeSocialSecurityNumberToSocialInsuranceNumberAtStandardEnrForCanUsers_1273() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName=TestConstants.FIRST_NAME+randomNum;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		//Validate at Account Info Section on payment page Social Insurance No is displayed and not Social Security No
		s_assert.assertTrue(storeFrontHomePage.getSocialInsuranceNumberTxtFldPlaceHolderValue().equalsIgnoreCase("Social Insurance Number"),"Social Insurance Number is not displayed in the respective field!!");
		//Enter Social Insurance Number..
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.applyPriceFilterHighToLow();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-1288 :: Version : 1 :: 2. Terms and Conditions - Standard EnrollmentTest with both CRP and Pulse
	@Test(enabled=false)//Covered in TC 5284
	public void testStandardEnrollmentTermsAndConditions_1288() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName=TestConstants.FIRST_NAME+randomNum;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		/*storeFrontHomePage.clickOnOurBusinessLink();
					storeFrontHomePage.clickOnOurEnrollNowLink();*/
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		//		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsSelected(), "Subscribe to pulse checkbox not selected");
		//		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsSelected(), "Enroll to CRP checkbox not selected");
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	// Hybris Project-2230 :: Version : 1 :: Verify that user can enroll in CRP through my account.
	@Test 
	public void testUserEnrollCRPThroughMyAccount_2230() throws InterruptedException{  
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();		
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsNotSelected(), "Subscribe to pulse checkbox selected after uncheck");
		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsNotSelected(), "Enroll to CRP checkbox selected after uncheck");
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();//added
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnEnrollInCRP();
		storeFrontHomePage.clickOnAddToCRPButtonCreatingCRPUnderBizSite();
		storeFrontHomePage.clickOnCRPCheckout();
		storeFrontHomePage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnSetupCRPAccountBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyOrderConfirmation(), "Order Confirmation Message has not been displayed");
		storeFrontHomePage.clickOnGoToMyAccountToCheckStatusOfCRP();
		storeFrontHomePage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCurrentCRPStatus(), "Current CRP Status has not been Enrolled");
		s_assert.assertAll();
	}


	//Hybris Project-150:Demo_Enroll in CRP but not Pulse - next month- pulse and CRP
	@Test
	public void testDemo_EnrollInCRPButNotPulseShipNextMonth_150() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REVERSE;
		String sRandName = RandomStringUtils.randomAlphabetic(12);

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_PERSONAL;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_PERSONAL;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, sRandName, TestConstants.PASSWORD, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		//Only Enroll in CRP but not pulse
		storeFrontHomePage.checkPulseAndCRPEnrollment();
		storeFrontHomePage.uncheckPulseCheckBox();
		s_assert.assertFalse(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsSelected(), "Subscribe to pulse checkbox selected");
		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsSelected(), "Enroll to CRP checkbox not selected");
		storeFrontHomePage.clickNextButton();
		//Add product to 'CRP' and ship 'Next Month'
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.selectShipNextMonthOnCRPSummaryPage();
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
		s_assert.assertAll();
	}

	//Hybris Project-149:Demo_Enroll in CRP but not Pulse - ship immediately - pulse and CRP
	@Test
	public void testDemo_EnrollInCRPButNotPulseShipImmediately_149() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REVERSE;
		String sRandName = RandomStringUtils.randomAlphabetic(12);

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_PERSONAL;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_PERSONAL;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, sRandName, TestConstants.PASSWORD, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		//Only Enroll in CRP but not pulse
		storeFrontHomePage.checkPulseAndCRPEnrollment();
		storeFrontHomePage.uncheckPulseCheckBox();
		s_assert.assertFalse(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsSelected(), "Subscribe to pulse checkbox selected");
		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsSelected(), "Enroll to CRP checkbox not selected");
		storeFrontHomePage.clickNextButton();
		//Add product to 'CRP' and ship immediately
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
		s_assert.assertAll();
	}

	//Hybris Project-132:Enroll in CRP from my account - Ship inmediately
	@Test//(enabled=true)//Test Needs update
	public void testEnrollInCRPFromMyAccountShipImmediately_132() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String accountId = null;
		List<Map<String, Object>> randomConsultantList =  null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String billingProfileName = TestConstants.BILLING_ADDRESS_NAME+randomNum;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			String userName = (String) getValueFromQueryResult(randomConsultantList, "Username"); 
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(userName, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+userName);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}  
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountId),RFO_DB);
		String consultantEmailID = (String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");  
		logger.info("emaild of consultant username "+consultantEmailID);	
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontConsultantPage.clickOnYourAccountDropdown();
		storeFrontConsultantPage.clickOnAutoshipStatusLink();
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
		if(storeFrontAccountInfoPage.verifyCRPCancelled()==false){
			storeFrontAccountInfoPage.clickOnCancelMyCRP();
		}
		storeFrontAccountInfoPage.clickOnEnrollInCRP();
		storeFrontAccountInfoPage.applyPriceFilterLowToHigh();
		storeFrontAccountInfoPage.clickOnAddToCRPButtonAfterCancelMyCRP();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyThresholdErrorMsgPresent(),"Message for threshold not present on UI");
		storeFrontAccountInfoPage.clickOnContinueShoppingLink();
		storeFrontAccountInfoPage.clickOnAddToCRPButtonAfterCancelMyCRP();
		storeFrontAccountInfoPage.updateQuantityOfProductToTheSecondProduct("3");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyConfirmationMessagePresentOnUI(),"Your next autoship cart has been updated message not present");
		storeFrontAccountInfoPage.clickOnCRPCheckout();
		storeFrontAccountInfoPage.clickOnShippingAddressNextStepBtn();
		storeFrontAccountInfoPage.clickAddNewBillingProfileLink();
		storeFrontAccountInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontAccountInfoPage.enterNewBillingNameOnCard(billingProfileName);
		storeFrontAccountInfoPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontAccountInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontAccountInfoPage.selectNewBillingCardAddress();
		storeFrontAccountInfoPage.clickOnSaveBillingProfile();
		storeFrontAccountInfoPage.clickOnBillingNextStepButtonDuringEnrollInCRP();
		storeFrontAccountInfoPage.clickOnSetupCRPAccountBtn();
		s_assert.assertTrue(storeFrontAccountInfoPage.isOrderPlacedSuccessfully(), "Order is not placed successfully");
		storeFrontAccountInfoPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontAccountInfoPage.clickOrdersLinkPresentOnWelcomeDropDown();
		//Verify Status of CRP autoship template
		s_assert.assertTrue(storeFrontOrdersPage.getStatusOfFirstAutoshipTemplateID().toLowerCase().contains("pending"), "Expected status of first autoship id is: pending and actual on UI is: "+storeFrontOrdersPage.getStatusOfFirstAutoshipTemplateID().toLowerCase());
		String CRPAutoshipID = storeFrontOrdersPage.getAutoshipOrderNumber();
		String autoshipDate = storeFrontOrdersPage.getAutoshipOrderDate();
		System.out.println(autoshipDate+"===autoshipDate");
		s_assert.assertTrue(storeFrontOrdersPage.validateSameDatePresentForAutoship(autoshipDate),"Same date is not present");
		logout();
		//verify on CSCockpit
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		s_assert.assertTrue(cscockpitCustomerTabPage.getStatusOfAutoshipID(CRPAutoshipID).toLowerCase().contains("pending"), "Expected status of autoship id in CSCockpit is: pending and actual on UI is: "+cscockpitCustomerTabPage.getStatusOfAutoshipID(CRPAutoshipID).toLowerCase());
		s_assert.assertAll();
	}

	//Test Case Hybris Project-3255 :: Version : 1 :: Standard EnrollmentTest without CRP and Pulse
	@Test
	public void testStandardEnrollmentWithoutCRPAndPulse_3255() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName = TestConstants.FIRST_NAME+randomNum;
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		/*storeFrontHomePage.clickOnOurBusinessLink();
			   storeFrontHomePage.clickOnOurEnrollNowLink();*/
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();

		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsNotSelected(), "Subscribe to pulse checkbox selected after uncheck");
		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsNotSelected(), "Enroll to CRP checkbox selected after uncheck");
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		s_assert.assertAll();
	}

	// Hybris Project-3964:Use email id of existing inactive consultant (less than 180 days) during consult
	@Test
	public void testUseEmailIdOfExistingInactiveConsultantDuringConsult_3964() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		String consultantPWS = null;
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		kitName = TestConstants.KIT_NAME_EXPRESS;    
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.selectTerminationReason();
		storeFrontAccountTerminationPage.enterTerminationComments();
		storeFrontAccountTerminationPage.clickOnAgreementCheckBox();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		randomConsultantList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",country,countryId), RFO_DB);
		consultantPWS = (String) getValueFromQueryResult(randomConsultantList, "URL");
		driver.get(consultantPWS);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.clickOnEnrollUnderLastUpline();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.enterPasswordForReactivationForConsultant();
		storeFrontHomePage.clickOnLoginToReactiveMyAccountForConsultant();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

}
