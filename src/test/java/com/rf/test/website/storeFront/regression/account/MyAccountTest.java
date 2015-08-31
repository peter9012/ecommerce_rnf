package com.rf.test.website.storeFront.regression.account;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontAccountTerminationPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontRCUserPage;
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
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private String RFO_DB = null;

	// Test Case Hybris Phase 2-3720 :: Version : 1 :: Perform Consultant Account termination through my account
	// Will just verify till termination popup and NOT terminate the account
	@Test
	public void testAccountTerminationPageForConsultant_3720() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");	
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 

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

		s_assert.assertAll();			
	}

	//Test Case Hybris Phase 2-3719 :: Version : 1 :: Perform PC Account termination through my account
	@Test
	public void testAccountTerminationPageForPCUser_3719() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);		

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, TestConstants.PC_USER_PASSWORD_RFL);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		s_assert.assertFalse(storeFrontAccountInfoPage.verifyAccountTerminationLink(),"Account Termination Link Is Present");

		s_assert.assertAll();
	}

	// Hybris Phase 2-1980 :: Version : 1 :: Order >>Actions >>Report problems
	@Test
	public void testOrdersReportProblems_1980() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

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
	@Test
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
			storeFrontHomePage.selectProvince();
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
	@Test
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
			storeFrontHomePage.selectProvince();
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
	@Test
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
			storeFrontHomePage.selectProvince();
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
	@Test
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
			storeFrontHomePage.selectProvince();
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
			storeFrontHomePage.selectProvince();
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
	@Test
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
			storeFrontHomePage.selectProvince();
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

	//	// Hybris Project-1306  Biz: PC Enroll- Not my sponsor link
	//	@Test(enabled=true)
	//	public void testPCEnrollNotMySponsorLink() throws InterruptedException	 {
	//
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		// Click on our product link that is located at the top of the page and then click in on quick shop
	//		storeFrontHomePage.clickOnShopLink();
	//		storeFrontHomePage.clickOnAllProductsLink();
	//
	//		// Products are displayed?
	//		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
	//		logger.info("Quick shop products are displayed");
	//
	//		//Select a product and proceed to buy it
	//		storeFrontHomePage.selectProductAndProceedToBuy();
	//
	//		//Cart page is displayed?
	//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
	//		logger.info("Cart page is displayed");
	//		//Click on Check out
	//		storeFrontHomePage.clickOnCheckoutButton();
	//
	//		//Log in or create an account page is displayed?
	//		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
	//		logger.info("Login or Create Account page is displayed");
	//
	//		//Enter the User information and DO  check the "Become a Preferred Customer" checkbox and click the create account button
	//		storeFrontHomePage.enterNewPCDetails();
	//
	//		//Click on Request a Sponsor
	//		//storeFrontHomePage.clickOnNotYourSponsorLink();
	//
	//		//click on request a Sponsor btn
	//		storeFrontHomePage.clickOnRequestASponsorBtn();
	//
	//		//click on 'OK' on sponsor Information pop up
	//		storeFrontHomePage.clickOKOnSponsorInformationPopup();
	//		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();  
	//	}

	// Hybris Project-1294:10. Standard Enrollment switch to Express Enrollment
	@Test
	public void testStandardEnrollmentSwitchToExpresEnrollment() throws InterruptedException
	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_CA, TestConstants.REGIMEN_NAME);  
		storeFrontHomePage.chooseEnrollmentOption(TestConstants.STANDARD_ENROLLMENT);
		storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
		storeFrontHomePage.enterPassword(TestConstants.PASSWORD);
		storeFrontHomePage.enterConfirmPassword(TestConstants.PASSWORD);
		storeFrontHomePage.enterAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
		storeFrontHomePage.enterCity(TestConstants.CITY_CA);
		storeFrontHomePage.selectProvince();
		storeFrontHomePage.enterPostalCode(TestConstants.POSTAL_CODE_CA);
		storeFrontHomePage.enterPhoneNumber(TestConstants.PHONE_NUMBER);
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
		//click on switch to 'express enrollment' link
		storeFrontHomePage.clickSwitchToExpressEnrollmentLink();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
	}

	//Hybris Project-1296:12.Standard Enrollment switch to Express Enrollment - Step 5
	@Test
	public void testStandardEnrollmentSwitchToExpresEnrollmentStep5() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_CA, TestConstants.REGIMEN_NAME);  
		storeFrontHomePage.chooseEnrollmentOption(TestConstants.STANDARD_ENROLLMENT);
		storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
		storeFrontHomePage.enterPassword(TestConstants.PASSWORD);
		storeFrontHomePage.enterConfirmPassword(TestConstants.PASSWORD);
		storeFrontHomePage.enterAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
		storeFrontHomePage.enterCity(TestConstants.CITY_CA);
		storeFrontHomePage.selectProvince();
		storeFrontHomePage.enterPostalCode(TestConstants.POSTAL_CODE_CA);
		storeFrontHomePage.enterPhoneNumber(TestConstants.PHONE_NUMBER);
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
		//check the pulse and CRP check box and proceed
		storeFrontHomePage.checkPulseCheckBox();
		storeFrontHomePage.checkCRPCheckBox();
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickNextOnCRPCartPage();
		//switch to Express Enrollment on 'Recurring Monthly Charges' section
		storeFrontHomePage.clickSwitchToExpressEnrollmentOnRecurringMonthlyChargesSection();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
	}

	//Hybris Project-1304 :: Version : 1 :: Switch from RC to PC (Not existing user) 
	@Test

	public void testSwitchFromRCToPC() throws InterruptedException
	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
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

		//Enter the User information and DONOT  check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails();
		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		storeFrontHomePage.enterMainAccountInfo();
		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		//Validate welcome PC Perks Message
		s_assert.assertTrue(storeFrontHomePage.isWelcomePCPerksMessageDisplayed(), "welcome PC perks message should be displayed");
		s_assert.assertAll(); 
	}

	//Hybris Project-2196 :: Version : 1 :: Switch from PC to Consultant (Under Same Consultant)
	@Test
	public void testSwitchFromPCToConsultantUnderSameConsultant() throws InterruptedException
	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.clickOnShopLink();
		storeFrontHomePage.clickOnAllProductsLink();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO  check the "Become a Preferred Customer" checkbox and click the create account button
		String newPCName= storeFrontHomePage.createNewPC();

		//Pop for PC threshold validation
		s_assert.assertTrue(storeFrontHomePage.isPopUpForPCThresholdPresent(),"Threshold poup for PC validation NOT present");

		//In the Cart page add one more product
		storeFrontHomePage.addAnotherProduct();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		storeFrontPCUserPage=new StoreFrontPCUserPage(driver);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
		storeFrontPCUserPage.cancelMyPCPerksAct();
		//Proceed with consultant enrollment, following pc to consultant flow
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink(); 
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_BIG_BUSINESS_CA, TestConstants.REGIMEN_NAME_REVERSE);  
		storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
		storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
		storeFrontHomePage.enterPassword(TestConstants.PASSWORD);
		storeFrontHomePage.enterConfirmPassword(TestConstants.PASSWORD);
		storeFrontHomePage.enterAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
		storeFrontHomePage.enterCity(TestConstants.CITY_CA);
		storeFrontHomePage.selectProvince();
		storeFrontHomePage.enterPostalCode(TestConstants.POSTAL_CODE_CA);
		storeFrontHomePage.enterPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontHomePage.enterEmailAddress(newPCName);
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
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		s_assert.assertAll();
	}

	//Hybris Project-2362 :: Version : 1 :: Switch from PC to Consultant (Under Diff Consultant)
	@Test
	public void testSwitchFromPCToConsultantUnderDifferentConsultant() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.clickOnShopLink();
		storeFrontHomePage.clickOnAllProductsLink();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO  check the "Become a Preferred Customer" checkbox and click the create account button
		String newPCName= storeFrontHomePage.createNewPC();

		//Pop for PC threshold validation
		s_assert.assertTrue(storeFrontHomePage.isPopUpForPCThresholdPresent(),"Threshold poup for PC validation NOT present");

		//In the Cart page add one more product
		storeFrontHomePage.addAnotherProduct();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		storeFrontPCUserPage=new StoreFrontPCUserPage(driver);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
		storeFrontPCUserPage.cancelMyPCPerksAct();
		//Proceed with consultant enrollment, following pc to consultant flow
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink(); 
		storeFrontHomePage.searchCID(TestConstants.CID);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_BIG_BUSINESS_CA, TestConstants.REGIMEN_NAME_REVERSE);  
		storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
		storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
		storeFrontHomePage.enterPassword(TestConstants.PASSWORD);
		storeFrontHomePage.enterConfirmPassword(TestConstants.PASSWORD);
		storeFrontHomePage.enterAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
		storeFrontHomePage.enterCity(TestConstants.CITY_CA);
		storeFrontHomePage.selectProvince();
		storeFrontHomePage.enterPostalCode(TestConstants.POSTAL_CODE_CA);
		storeFrontHomePage.enterPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontHomePage.enterEmailAddress(newPCName);
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
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		s_assert.assertAll();
	}

	// Hybris Phase 2-2228 :: Version : 1 :: Perform RC Account termination through my account
	@Test
	public void testAccountTerminationPageForRCUser_2228() throws InterruptedException{

		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
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

		//In the Cart page add one more product
		storeFrontHomePage.addAnotherProduct();

		//Two products are in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("2"), "number of products in the cart is NOT 2");
		logger.info("2 products are successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String newRCName=storeFrontHomePage.createNewRC();

		//CheckoutPage is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCheckoutPageDisplayed(), "Checkout page has NOT displayed");
		logger.info("Checkout page has displayed");

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();
		//  RFO_DB = driver.getDBNameRFO();
		//  List<Map<String, Object>> randomRCUserList =  null;
		//  String rcUserEmailID = null;
		//  String accountID = null;
		//  randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,RFO_DB);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(newRCName, TestConstants.PASSWORD);
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationPageIsDisplayed(),"Account Termination Page has not been displayed");
		storeFrontAccountTerminationPage.selectTerminationReason();
		storeFrontAccountTerminationPage.enterTerminationComments();
		storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		s_assert.assertFalse(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is Present");
		storeFrontHomePage.loginAsRCUser(newRCName,TestConstants.PASSWORD);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");  
		s_assert.assertAll();
	}

	// Hybris Project-1975 :: Version : 1 :: Retail user termination
	@Test
	public void testRetailUserTermination_1975() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
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

		//In the Cart page add one more product
		storeFrontHomePage.addAnotherProduct();

		//Two products are in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("2"), "number of products in the cart is NOT 2");
		logger.info("2 products are successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String newRCName=storeFrontHomePage.createNewRC();

		//CheckoutPage is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCheckoutPageDisplayed(), "Checkout page has NOT displayed");
		logger.info("Checkout page has displayed");

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();
		//  RFO_DB = driver.getDBNameRFO();
		//  List<Map<String, Object>> randomRCUserList =  null;
		//  String rcUserEmailID = null;
		//  String accountID = null;
		//  randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,RFO_DB);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(newRCName, TestConstants.PASSWORD);
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationPageIsDisplayed(),"Account Termination Page has not been displayed");
		storeFrontAccountTerminationPage.selectTerminationReason();
		storeFrontAccountTerminationPage.enterTerminationComments();
		storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		s_assert.assertFalse(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is Present");
		storeFrontHomePage.loginAsRCUser(newRCName,TestConstants.PASSWORD);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");  
		s_assert.assertAll();
	}

	//Hybris Project-2199 :: Version : 1 :: Verify Existing Preferred Customer popup during RC registration.
	@Test
	public void testExistingPCPopUpDuringRCRegistration() throws InterruptedException
	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
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

		//Validate Existing PC Popup during RC Registration by entering existing PC mailid
		s_assert.assertTrue(storeFrontHomePage.validateExistingUserPopUp("pc"), "Existing PC PopUp is not displayed");

		//validate 'send mail to reset my password' functionality
		s_assert.assertTrue(storeFrontHomePage.validateSendMailToResetMyPasswordFunctionalityPC(), "send mail functionality is not working properly");
		//validate cancel enrollment functionality
		s_assert.assertTrue(storeFrontHomePage.validateCancelEnrollmentFunctionalityPC(), "cancel enrollment functionality is not working properly");
		s_assert.assertAll(); 
	}

	//Hybris Project-2200 :: Version : 1 :: Verify Existing Retail Customer popup during RC registration
	@Test
	public void testExistingRetailCustomerPopupDuringRCRegistration() throws InterruptedException
	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
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

		//Validate Existing RC Popup during RC Registration by entering existing RC mailid
		s_assert.assertTrue(storeFrontHomePage.validateExistingUserPopUp("rc"), "Existing RC PopUp is not displayed");

		//validate 'send mail to reset my password' functionality
		s_assert.assertTrue(storeFrontHomePage.validateSendMailToResetMyPasswordFunctionalityRC(), "send mail functionality is not working properly");
		//validate cancel enrollment functionality
		s_assert.assertTrue(storeFrontHomePage.validateCancelEnrollmentFunctionalityRC(), "cancel enrollment functionality is not working properly");
		s_assert.assertAll(); 
	}

	//Hybris Project-2201 :: Version : 1 :: Verify Existing Consultant popup during RC registration
	@Test
	public void testExistingConsultantPopupDuringRCRegistration() throws InterruptedException
	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
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

		//Validate Existing Consulatnt popup during RC Registration by entering existing consultant mailid
		s_assert.assertTrue(storeFrontHomePage.validateExistingUserPopUp("consultant"), "Existing RC PopUp is not displayed");

		//validate 'send mail to reset my password' functionality
		s_assert.assertTrue(storeFrontHomePage.validateSendMailToResetMyPasswordFunctionalityConsultant(), "send mail functionality is not working properly");
		//validate cancel enrollment functionality
		s_assert.assertTrue(storeFrontHomePage.validateCancelEnrollmentFunctionalityConsultant(), "cancel enrollment functionality is not working properly");
		s_assert.assertAll(); 
	}

	//Hybris Project-2198 :: Version : 1 :: Switch from RC to PC (Under Same Consultant) 
	@Test
	public void testSwitchFromRCToPCUnderSameConsultant() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
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

		//Enter the User information and DONOT  check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails();
		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		storeFrontHomePage.enterMainAccountInfo();
		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		//Validate welcome PC Perks Message
		s_assert.assertTrue(storeFrontHomePage.isWelcomePCPerksMessageDisplayed(), "welcome PC perks message should be displayed");
		s_assert.assertAll(); 
	}
	
	 // Hybris Project-4155 :: Version : 1 :: Verify special characters are not allowing
	 @Test
	 public void testVerifySpecialCharactersAreNotAllowed() throws InterruptedException{
	  int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	  String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
	  storeFrontHomePage = new StoreFrontHomePage(driver);
	  storeFrontHomePage.clickOnOurBusinessLink();
	  storeFrontHomePage.clickOnOurEnrollNowLink();
	  storeFrontHomePage.searchCID();
	  storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
	  storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_PRICE_PORTFOLIO_CA);  
	  //storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
	  storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
	  storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
	  storeFrontHomePage.enterPassword(TestConstants.PASSWORD);
	  storeFrontHomePage.enterConfirmPassword(TestConstants.PASSWORD);
	  storeFrontHomePage.enterAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
	  storeFrontHomePage.enterCity(TestConstants.CITY_CA);
	  storeFrontHomePage.selectProvince();
	  storeFrontHomePage.enterPostalCode(TestConstants.POSTAL_CODE_CA);
	  storeFrontHomePage.enterPhoneNumber(TestConstants.PHONE_NUMBER);
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
	  s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsSelected(), "Subscribe to pulse checkbox not selected");
	  s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsSelected(), "Enroll to CRP checkbox not selected");
	  storeFrontHomePage.clickEnrollmentNextBtn();  
	  storeFrontHomePage.selectProductAndProceedToAddToCRP();
	  storeFrontHomePage.addQuantityOfProduct("5");
	  storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
	  storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
	  storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
	  storeFrontHomePage.checkTheIAgreeCheckBox();
	  storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
	  storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
	  storeFrontHomePage.clickOnConfirmAutomaticPayment();
	  s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
	  storeFrontHomePage.clickOnRodanAndFieldsLogo();
	  storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
	  storeFrontConsultantPage.clickOnWelcomeDropDown();
	  storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
	  storeFrontConsultantPage.clickOnAutoshipStatusLink();
	  storeFrontConsultantPage.subscribeToPulse();
	  s_assert.assertTrue(storeFrontConsultantPage.validateErrorMessageWithSpclCharsOnPulseSubscription(), "Error/Warning message is not displayed ");
	  s_assert.assertAll(); 
	 }
}

