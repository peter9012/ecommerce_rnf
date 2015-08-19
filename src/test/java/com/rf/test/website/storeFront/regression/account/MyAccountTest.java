package com.rf.test.website.storeFront.regression.account;


import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontAccountTerminationPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontReportOrderComplaintPage;
import com.rf.pages.website.StoreFrontReportProblemConfirmationPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class MyAccountTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(MyAccountTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontAccountTerminationPage storeFrontAccountTerminationPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontReportOrderComplaintPage storeFrontReportOrderComplaintPage;
	private StoreFrontReportProblemConfirmationPage storeFrontReportProblemConfirmationPage;
	
	
	//Test Case Hybris Phase 2-3719 :: Version : 1 :: Perform PC Account termination through my account
	@Test
	public void testAccountTerminationPageForPCUser_3719() throws InterruptedException{
		String pcUserEmailID = TestConstants.PC_EMAIL_ID_STG2;		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, TestConstants.PC_USER_PASSWORD_RFL);
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		s_assert.assertFalse(storeFrontAccountInfoPage.verifyAccountTerminationLink(),"Account Termination Link Is Present");
		logout();
		s_assert.assertAll();
	}

	// Test Case Hybris Phase 2-3720 :: Version : 1 :: Perform Consultant Account termination through my account
	// Will just verify till termination popup and NOT terminate the account
	@Test
	public void testAccountTerminationPageForConsultant_3720() throws InterruptedException {
		String consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_STG2;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);			
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationPageIsDisplayed(),"Account Termination Page has not been displayed");
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is not Present");
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupCancelTerminationButton(),"Account termination page Pop up cancel termination button is not present");
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupConfirmTerminationButton(),"Account termination Page Pop Up Confirm termination button is not present");
		storeFrontAccountTerminationPage.clickCancelTerminationButton();
		logout();
		s_assert.assertAll();			
	}

	//	// Hybris Phase 2-2228 :: Version : 1 :: Perform RC Account termination through my account
	//	@Test(enabled=false) // Will terminate the account
	//	public void testAccountTerminationPageForRCUser_2228() throws InterruptedException{
	//		RFL_DB = driver.getDBNameRFL();
	//		RFO_DB = driver.getDBNameRFO();
	//		List<Map<String, Object>> randomRCList =  null;
	//		String rcUserEmailID = null;
	//		String accountID = null;
	//
	//		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS,RFL_DB);
	//		rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");
	//		accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
	//		logger.info("Account ID of the user is "+accountID);
	//
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, TestConstants.RC_PASSWORD_TST4);
	//		logger.info("login is successful");
	//		storeFrontRCUserPage.clickOnWelcomeDropDown();
	//		storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
	//		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
	//		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
	//		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationPageIsDisplayed(),"Account Termination Page has not been displayed");
	//		storeFrontAccountTerminationPage.selectTerminationReason();
	//		storeFrontAccountTerminationPage.enterTerminationComments();
	//		storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
	//		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
	//		s_assert.assertFalse(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is Present");
	//		storeFrontHomePage.loginAsRCUser(rcUserEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
	//		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");		
	//		s_assert.assertAll();
	//
	//	}


	// Hybris Phase 2-1980 :: Version : 1 :: Order >>Actions >>Report problems
	@Test
	public void testOrdersReportProblems_1980() throws SQLException, InterruptedException{
		String consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_STG2;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_STG2);			
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		storeFrontOrdersPage.orderNumberForOrderHistory();
		storeFrontReportOrderComplaintPage = storeFrontOrdersPage.clickOnActions();
		s_assert.assertTrue(storeFrontReportOrderComplaintPage.VerifyOrderNumberOnReportPage(),"OrderNumber is different on ReportOrderComplaintPage");
		storeFrontReportOrderComplaintPage.clickOnCheckBox();
		s_assert.assertTrue(storeFrontReportOrderComplaintPage.verifyCountOfDropDownOptionsOnReportPage(),"DropDown Options are not present as expected");
		storeFrontReportOrderComplaintPage.selectOptionFromDropDown();
		storeFrontReportProblemConfirmationPage = storeFrontReportOrderComplaintPage.enterYourProblemAndSubmit(TestConstants.TELL_US_ABOUT_YOUR_PROBLEM);

		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyHeaderAtReportConfirmationPage("REPORT A PROBLEM"),"Report a problem is not present at header");
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyThankYouTagAtReportConfirmationPage("THANK YOU"),"Thank you tag is not present on the page");
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyEmailAddAtReportConfirmationPage(consultantEmailID),"Email Address is not present as expected" );
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyOrderNumberAtReportConfirmationPage(),"Order number not present as expected");
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyBackToOrderButtonAtReportConfirmationPage(),"Back To Order button is not present");
		//logout(); // not working
		s_assert.assertAll();
	}

	// Hybris Phase 2-2241 :: version 1 :: Verify the various field validations
	@Test
	public void testPhoneNumberFieldValidationForConsultant_2241() throws InterruptedException{
		String consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_STG2;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_ACC_TERMINATION_PASSWORD_TST4);			
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage.enterMainPhoneNumber(TestConstants.CONSULTANT_INVALID_11_DIGIT_MAIN_PHONE_NUMBER);
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyValidationMessageOfPhoneNumber(TestConstants.CONSULTANT_VALIDATION_MESSAGE_OF_MAIN_PHONE_NUMBER),"Validation Message has not been displayed ");
		storeFrontAccountInfoPage.enterMainPhoneNumber(TestConstants.CONSULTANT_VALID_11_DIGITMAIN_PHONE_NUMBER);
		s_assert.assertFalse(storeFrontAccountInfoPage.verifyValidationMessageOfPhoneNumber(TestConstants.CONSULTANT_VALIDATION_MESSAGE_OF_MAIN_PHONE_NUMBER),"Validation Message has been displayed");
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-1977 :: verify with Valid credentials and Logout.
	@Test
	public void testVerifyLogoutwithValidCredentials_1977() throws InterruptedException{
		String consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_STG2;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_ACC_TERMINATION_PASSWORD_TST4);			
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-2512 :: Version : 1 :: Username validations.
	@Test
	public void testUsernameValidations_2512() throws InterruptedException	{
		String consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_STG2;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);   
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		//Enter data less than 8 characters in 'username' field and verify the error message
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_BELOW_8_DIGITS);
		//hit save btn
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("enter at least"));
		//Enter data more than 8 chars with space and verify error msg
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_8_DIGITS);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.checkErrorMessage(), "error message should exist");
		//Enter data more than 8 chars with just numbers and verify
		/* storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAMEMORETHAN8NUMBERS);
		  storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		  s_assert.assertFalse(storeFrontAccountInfoPage.checkErrorMessage());*/
		//Enter data more than 8 chars with only special chars and validate error msg
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_8_SPECIAL_CHARS);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.checkErrorMessage(), "error message should exist");
		//Enter data more than 8 chars with alphabets and validate
		/* storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAMEMORETHAN8ALPHABETS);
		  storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		  s_assert.assertFalse(storeFrontAccountInfoPage.checkErrorMessage());*/
		//Enter data more than 8 chars(alphanumeric with atleast a special char)
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_8_ALPHANUMERIC_CHARS_WITH_SPCLCHAR);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertFalse(storeFrontAccountInfoPage.checkErrorMessage());
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1367 :: Version : 1 :: Enroll as consultant with a empty card number.
	@Test(enabled=true)
	public void testEnrollConsultantWithEmptyCardNumber() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink(); 
		storeFrontHomePage.searchCID(TestConstants.CID);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		if(country.equalsIgnoreCase("CA")){
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_EXPRESS_CA, TestConstants.REGIMEN_NAME_REVERSE);  
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
		}
		else{
			//TODO the US part
		}
		String consultantEmailID =  TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop(); 
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickOnEnrollmentNextButton();
		s_assert.assertTrue(storeFrontHomePage.validateEmptyCreditCardMessage(), "Please enter a valid credit card Number");
		s_assert.assertAll(); 
	}

	//Hybris Project-1361:Enroll as consultant using invalid card numbers
	@Test(enabled=true)
	public void testEnrollAsConsultantUsingInvalidCardNumbers() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink(); 
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		if(country.equalsIgnoreCase("CA")){
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_EXPRESS_CA, TestConstants.REGIMEN_NAME_REVERSE);  
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
		}
		else{
			//TODO the US part
		}
		String consultantEmailID = TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.INVALID_CARD_NUMBER_15DIGITS);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		s_assert.assertTrue(storeFrontHomePage.validateInvalidCreditCardMessage(), "Please enter a valid credit card message is displayed");
		storeFrontHomePage.clearCreditCardNumber();
		storeFrontHomePage.enterCardNumber(TestConstants.INVALID_CARD_NUMBER_17DIGITS);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		s_assert.assertTrue(storeFrontHomePage.validateInvalidCreditCardMessage(), "Please enter a valid credit card message is displayed");
		s_assert.assertAll(); 
	}

	//Test Case Hybris Project-3718 ::Standard Enrollment (No CRP, No Pulse) as consultant using invalid card numbers
	@Test(enabled=true)
	public void testStandardEnrollmentWithoutCRPAndPulseWithInvalidCardAsConsultant() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		if(country.equalsIgnoreCase("CA")){
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_CA, TestConstants.REGIMEN_NAME);  
			storeFrontHomePage.chooseEnrollmentOption(TestConstants.STANDARD_ENROLLMENT);
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
			//TDOD the US part
		}
		storeFrontHomePage.enterEmailAddress(TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.INVALID_CARD_NUMBER_15DIGITS);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		s_assert.assertTrue(storeFrontHomePage.validateInvalidCreditCardMessage(), "Please enter a valid credit card message is displayed");
		s_assert.assertAll();
	}

	// Hybris Project-1368:Enroll as consultant using Expired Date card
	@Test(enabled=false)
	public void testEnrollAsConsultantUsingExpiredDataCard() throws InterruptedException	 {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink(); 
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		if(country.equalsIgnoreCase("CA")){
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_BIG_BUSINESS_CA, TestConstants.REGIMEN_NAME_REVERSE);  
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
		String consultantEmailID = TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		//Enter the expired date at billing section and validate the error message
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		s_assert.assertTrue(!storeFrontHomePage.selectNewBillingCardExpirationDateAsExpiredDate(), "User should not be able to select an expired date in Expiration date Drop down list");
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		//  storeFrontHomePage.clickOnSaveBillingProfile();
		s_assert.assertAll(); 
	}

	//Hybris Project-1274:9. Express enrollment -fields validation
	@Test(enabled=false)
	public void testExpressEnrollmentFieldsValidation() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String country = driver.getCountry();
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink(); 
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		if(country.equalsIgnoreCase("CA")){
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_BIG_BUSINESS_CA, TestConstants.REGIMEN_NAME_REVERSE);  
			storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
			//validate with password(<6 chars)
			storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
			storeFrontHomePage.enterPassword(TestConstants.PASSWORD_BELOW_6CHARS);
			storeFrontHomePage.enterAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
			s_assert.assertTrue(storeFrontHomePage.validatePasswordFieldMessage(), "Please enter 6 characters or more, with at least 1 number and 1 character message should be displayed");
			storeFrontHomePage.clearPasswordField();
			storeFrontHomePage.enterPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterConfirmPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterCity(TestConstants.CITY_CA);
			storeFrontHomePage.selectProvince(TestConstants.PROVINCE_CA);
			storeFrontHomePage.enterPostalCode(TestConstants.POSTAL_CODE_CA);
			storeFrontHomePage.enterPhoneNumber(TestConstants.PHONE_NUMBER);
		}
		else{
			//TODO the US part
		}
		String consultantEmailID = TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		//validate that the user is able to see the section for 'Recurring monthly charges' with the ability to enter their PWS prefix
		s_assert.assertTrue(storeFrontHomePage.recurringMonthlyChargesSection(), "Recurring Monthly Charges Section should be displayed ");
		s_assert.assertTrue(storeFrontHomePage.pulseSubscriptionTextbox(), "user can enter their PWS prefix");
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickOnAllowMySpouseOrDomesticPartnerCheckbox();
		storeFrontHomePage.enterSpouseFirstName(TestConstants.SPOUSE_FIRST_NAME);
		storeFrontHomePage.enterSpouseLastName(TestConstants.SPOUSE_LAST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		//validate the error message 
		s_assert.assertTrue(storeFrontHomePage.validateErrorMessageWithoutSelectingAllCheckboxes(), "A proper error message should be displayed when continuining without selecting all the checkboxes");
		storeFrontHomePage.closePopUp();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		s_assert.assertAll(); 
	}

	// Hybris Project-82- Version : 1 :: Allow my Spouse through Enrollment 
	@Test(enabled=true)
	public void testAllowMySpouseThroughEnrollment() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String country = driver.getCountry();
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink(); 
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		if(country.equalsIgnoreCase("CA")){
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_BIG_BUSINESS_CA, TestConstants.REGIMEN_NAME_REVERSE);  
			storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
			storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
			storeFrontHomePage.enterPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterConfirmPassword(TestConstants.PASSWORD);
			storeFrontHomePage.enterAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
			storeFrontHomePage.enterCity(TestConstants.CITY_CA);
			storeFrontHomePage.selectProvince(TestConstants.PROVINCECA);
			storeFrontHomePage.enterPostalCode(TestConstants.POSTAL_CODE_CA);
			storeFrontHomePage.enterPhoneNumber(TestConstants.PHONE_NUMBER);
		}
		String consultantEmailID = TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickOnAllowMySpouseOrDomesticPartnerCheckbox();
		storeFrontHomePage.enterSpouseFirstName(TestConstants.SPOUSE_FIRST_NAME);
		storeFrontHomePage.enterSpouseLastName(TestConstants.SPOUSE_LAST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheProvideAccessToSpousePopup();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		//validate the error message 
		s_assert.assertTrue(storeFrontHomePage.validateErrorMessageWithoutSelectingAllCheckboxes(), "A proper error message should be displayed when continuining without selecting all the checkboxes");
		storeFrontHomePage.closePopUp();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		s_assert.assertAll(); 
	}
	
	 // Hybris Project-1306  Biz: PC Enroll- Not my sponsor link
		 @Test(enabled=true)
		 public void testPCEnrollNotMySponsorLink() throws InterruptedException	 {
				
		  storeFrontHomePage = new StoreFrontHomePage(driver);
		  // Click on our product link that is located at the top of the page and then click in on quick shop
		  storeFrontHomePage.clickOnShopLink();
		  storeFrontHomePage.clickOnAllProductsLink();

		  // Products are displayed?
		  s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		  logger.info("Quick shop products are displayed");

		  //Select a product and proceed to buy it
		  storeFrontHomePage.selectProductAndProceedToBuy();

		  //Cart page is displayed?
		  s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		  logger.info("Cart page is displayed");
		  //Click on Check out
		  storeFrontHomePage.clickOnCheckoutButton();

		  //Log in or create an account page is displayed?
		  s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		  logger.info("Login or Create Account page is displayed");

		  //Enter the User information and DO  check the "Become a Preferred Customer" checkbox and click the create account button
		  storeFrontHomePage.enterNewPCDetails();
		  
		  //Click on Request a Sponsor
		  //storeFrontHomePage.clickOnNotYourSponsorLink();
		  
		  //click on request a Sponsor btn
		  storeFrontHomePage.clickOnRequestASponsorBtn();
		  
		  //click on 'OK' on sponsor Information pop up
		  storeFrontHomePage.clickOKOnSponsorInformationPopup();
		  storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();  
		 }
		

}

