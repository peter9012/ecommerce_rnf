package com.rf.test.website.storeFront.regression.account;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontAccountTerminationPage;
import com.rf.pages.website.StoreFrontBillingInfoPage;
import com.rf.pages.website.StoreFrontCartAutoShipPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersAutoshipStatusPage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontRCUserPage;
import com.rf.pages.website.StoreFrontReportOrderComplaintPage;
import com.rf.pages.website.StoreFrontReportProblemConfirmationPage;
import com.rf.pages.website.StoreFrontShippingInfoPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class MyAccountTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(MyAccountTest.class.getName());
	public String emailID=null;
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontAccountTerminationPage storeFrontAccountTerminationPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontReportOrderComplaintPage storeFrontReportOrderComplaintPage;
	private StoreFrontReportProblemConfirmationPage storeFrontReportProblemConfirmationPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontOrdersAutoshipStatusPage storeFrontOrdersAutoshipStatusPage;
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

	// Test Case Hybris Phase 2-3720 :: Version : 1 :: Perform Consultant Account termination through my account
	@Test
	public void testAccountTerminationPageForConsultant_3720() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
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
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Terminated User doesn't get Login failed");  
		s_assert.assertAll(); 
	}

	//Test Case Hybris Phase 2-3719 :: Version : 1 :: Perform PC Account termination through my account
	@Test
	public void testAccountTerminationPageForPCUser_3719() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontPCUserPage.clickOnYourAccountDropdown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
		storeFrontPCUserPage.cancelMyPCPerksAct();
		storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();	
	}

	// Hybris Phase 2-1980 :: Version : 1 :: Order >>Actions >>Report problems
	@Test
	public void testOrdersReportProblems_1980() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
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
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
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
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
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

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage.enterMainPhoneNumber(TestConstants.CONSULTANT_INVALID_11_DIGIT_MAIN_PHONE_NUMBER);
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyValidationMessageOfPhoneNumber(TestConstants.CONSULTANT_VALIDATION_MESSAGE_OF_MAIN_PHONE_NUMBER),"Validation Message has not been displayed ");
		storeFrontAccountInfoPage.enterMainPhoneNumber(TestConstants.CONSULTANT_VALID_11_DIGITMAIN_PHONE_NUMBER);
		s_assert.assertFalse(storeFrontAccountInfoPage.verifyValidationMessageOfPhoneNumber(TestConstants.CONSULTANT_VALIDATION_MESSAGE_OF_MAIN_PHONE_NUMBER),"Validation Message has been displayed For correct Phone Number");
		storeFrontAccountInfoPage.enterMainPhoneNumber(TestConstants.CONSULTANT_VALID_10_DIGIT_MAIN_PHONE_NUMBER);
		s_assert.assertFalse(storeFrontAccountInfoPage.verifyValidationMessageOfPhoneNumber(TestConstants.CONSULTANT_VALIDATION_MESSAGE_OF_MAIN_PHONE_NUMBER),"Validation Message has been displayed for ten digit phone number");
		s_assert.assertAll();
	}

	// Hybris Phase 2-1977 :: verify with Valid credentials and Logout.
	@Test
	public void testVerifyLogoutwithValidCredentials_1977() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
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

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		logout();
		s_assert.assertTrue(driver.getCurrentUrl().contains(".com/"+driver.getCountry()+"/"), "current url doesn't contains expected .com but actual URL is "+driver.getCurrentUrl());
		s_assert.assertAll();
	}

	//	//Hybris Project-2512 :: Version : 1 :: Username validations.
	//	@Test(enabled=false) //Test case in test link not updated as per the functionality
	//	public void testUsernameValidations_2512() throws InterruptedException	{
	//		RFO_DB = driver.getDBNameRFO();
	//		List<Map<String, Object>> randomConsultantList =  null;
	//		String consultantEmailID = null;
	//		String accountID = null;
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		while(true){
	//			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
	//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
	//			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
	//			logger.info("Account Id of the user is "+accountID);
	//
	//			storeFrontHomePage = new StoreFrontHomePage(driver);
	//			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
	//			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
	//			if(isSiteNotFoundPresent){
	//				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
	//				driver.get(driver.getURL());
	//			}
	//			else
	//				break;
	//		}
	//
	//		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
	//		logger.info("login is successful");
	//		storeFrontConsultantPage.clickOnWelcomeDropDown();
	//
	//		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
	//		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
	//		//Enter data less than 8 characters in 'username' field and verify the error message
	//		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_BELOW_8_DIGITS);
	//		//hit save btn
	//		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
	//		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter at least 6 characters."),"Validation for username less than 6 characters IS NOT PRESENT");
	//		//Enter data more than 8 chars with space and verify error msg
	//		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_8_DIGITS);
	//		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
	//		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 8 characters and space IS NOT PRESENT");
	//		//Enter data more than 8 chars with just numbers and verify
	//		/* storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAMEMORETHAN8NUMBERS);
	//		  storeFrontAccountInfoPage.clickSaveAccountPageInfo();
	//		  s_assert.assertFalse(storeFrontAccountInfoPage.checkErrorMessage());*/
	//		//Enter data more than 8 chars with only special chars and validate error msg
	//		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_8_SPECIAL_CHARS);
	//		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
	//		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 8 special characters IS NOT PRESENT");
	//		//Enter data more than 8 chars with alphabets and validate
	//		/* storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAMEMORETHAN8ALPHABETS);
	//		  storeFrontAccountInfoPage.clickSaveAccountPageInfo();
	//		  s_assert.assertFalse(storeFrontAccountInfoPage.checkErrorMessage());*/
	//		//Enter data more than 8 chars(alphanumeric with atleast a special char)
	//		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_8_ALPHANUMERIC_CHARS_WITH_SPCLCHAR);
	//		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
	//		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 8 alphanumneric characters with atleast a special char IS NOT PRESENT");
	//
	//		s_assert.assertAll();
	//	}


	//Hybris Project-2512 :: Version : 1 :: Username validations.
	@Test //Test case in test link not updated as per the functionality
	public void testUsernameValidations_2512() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String anotherConsultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();

		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		//Enter data less than 8 characters in 'username' field and verify the error message
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_BELOW_6_DIGITS);
		//hit save btn
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter at least 6 characters."),"Validation for username less than 6 characters IS NOT PRESENT");
		//Enter data more than 8 chars with space and verify error msg
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_DIGITS);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 8 characters and space IS NOT PRESENT");
		//Enter data more than 8 chars with just numbers and verify
		/* storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAMEMORETHAN8NUMBERS);
	    storeFrontAccountInfoPage.clickSaveAccountPageInfo();
	    s_assert.assertFalse(storeFrontAccountInfoPage.checkErrorMessage());*/
		//Enter data more than 8 chars with only special chars and validate error msg
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_SPECIAL_CHARS);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 8 special characters IS NOT PRESENT");
		//Enter data more than 8 chars with alphabets and validate
		/* storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAMEMORETHAN8ALPHABETS);
	    storeFrontAccountInfoPage.clickSaveAccountPageInfo();
	    s_assert.assertFalse(storeFrontAccountInfoPage.checkErrorMessage());*/
		//Enter data more than 8 chars(alphanumeric with atleast a special char)
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_ALPHANUMERIC_CHARS_WITH_SPCLCHAR);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 8 alphanumneric characters with atleast a special char IS NOT PRESENT");
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		anotherConsultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");

		//  s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"This username has been assigned to other user");
		//  enter data more then 8 characters with few special characters.
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_ALPHA_WITH_SPCL_CHAR);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 8 alphanumneric characters with atleast a special char IS NOT PRESENT");
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_ALPHA_WITH_SPCL_CHAR_COMB);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 8 alphanumneric characters with atleast a special char IS NOT PRESENT");
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_ALPHA_WITH_SINGLE_SPCL_CHAR);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.enterUserNameWithSpclChar(TestConstants.CONSULTANT_USERNAME_PREFIX),"validity message not present");
		storeFrontAccountInfoPage.enterUserName(anotherConsultantEmailID);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.errorMessageForExistingUser(),"Unable to update account details message not present on UI");
		logout();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_ALPHA_WITH_SINGLE_SPCL_CHAR, password);

		s_assert.assertAll();
	}

	//	//Hybris Project-1367 :: Version : 1 :: Enroll as consultant with a empty card number.
	//	
	//	public void testEnrollConsultantWithEmptyCardNumber_1367() throws InterruptedException{
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
	//		country = driver.getCountry();
	//		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
	//		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
	//
	//		if(country.equalsIgnoreCase("CA")){
	//			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
	//			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
	//			city = TestConstants.CITY_CA;
	//			postalCode = TestConstants.POSTAL_CODE_CA;
	//			phoneNumber = TestConstants.PHONE_NUMBER_CA;
	//		}else{
	//			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
	//			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
	//			city = TestConstants.NEW_ADDRESS_CITY_US;
	//			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
	//			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
	//
	//		}
	//
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
	//		storeFrontHomePage.searchCID();
	//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
	//		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
	//		storeFrontHomePage.clickEnrollmentNextBtn();
	//		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop(); 
	//		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
	//		storeFrontHomePage.selectNewBillingCardExpirationDate();
	//		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
	//		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
	//		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
	//		storeFrontHomePage.clickOnEnrollmentNextButton();
	//		s_assert.assertTrue(storeFrontHomePage.validateEmptyCreditCardMessage(), "Please enter a valid credit card Number");
	//
	//		s_assert.assertAll(); 
	//	}

	//Hybris Project-1361:Enroll as consultant using invalid card numbers
	@Test
	public void testEnrollAsConsultantUsingInvalidCardNumbers_1361() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.INVALID_CARD_NUMBER_15DIGITS);
		s_assert.assertTrue(storeFrontHomePage.validateInvalidCreditCardMessage(), "Please enter a valid credit card message is displayed");
		storeFrontHomePage.clearCreditCardNumber();
		storeFrontHomePage.enterCardNumber(TestConstants.INVALID_CARD_NUMBER_17DIGITS);
		s_assert.assertTrue(storeFrontHomePage.validateInvalidCreditCardMessage(), "Please enter a valid credit card message is displayed");
		storeFrontHomePage.clearCreditCardNumber();
		storeFrontHomePage.enterCardNumber(TestConstants.INVALID_CARD_NUMBER_15DIGITS_WITH_SPECIAL_CHAR);
		s_assert.assertTrue(storeFrontHomePage.validateInvalidCreditCardMessage(), "Please enter a valid credit card message is displayed");
		storeFrontHomePage.clearCreditCardNumber();

		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.validateEmptyCreditCardMessage(),"This field is required message is displayed");
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.selectInvalidNewBillingCardExpirationDate();
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.validateInvalidCreditCardExpiryDate(),"Must be a valid expiration date message is displayed");

		s_assert.assertAll(); 
	}
	//	//Test Case Hybris Project-3718 ::Standard EnrollmentTest (No CRP, No Pulse) as consultant using invalid card numbers
	//	
	//	public void testStandardEnrollmentWithoutCRPAndPulseWithInvalidCardAsConsultant_3718() throws InterruptedException{
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		country = driver.getCountry();
	//		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
	//		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
	//
	//		if(country.equalsIgnoreCase("CA")){
	//			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
	//			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
	//			city = TestConstants.CITY_CA;
	//			postalCode = TestConstants.POSTAL_CODE_CA;
	//			phoneNumber = TestConstants.PHONE_NUMBER_CA;
	//		}else{
	//			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
	//			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
	//			city = TestConstants.NEW_ADDRESS_CITY_US;
	//			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
	//			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
	//		}
	//
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
	//		storeFrontHomePage.searchCID();
	//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
	//		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
	//		storeFrontHomePage.clickEnrollmentNextBtn();
	//		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
	//		storeFrontHomePage.enterCardNumber(TestConstants.INVALID_CARD_NUMBER_15DIGITS);
	//		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
	//		s_assert.assertTrue(storeFrontHomePage.validateInvalidCreditCardMessage(), "Please enter a valid credit card message is displayed");
	//
	//		s_assert.assertAll();
	//	}

	//	// Hybris Project-1368:Enroll as consultant using Expired Date card
	//	
	//	public void testEnrollAsConsultantUsingExpiredDataCard_1368() throws InterruptedException  {
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000); 
	//		country = driver.getCountry();
	//		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
	//		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
	//
	//		if(country.equalsIgnoreCase("CA")){
	//			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
	//			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
	//			city = TestConstants.CITY_CA;
	//			postalCode = TestConstants.POSTAL_CODE_CA;
	//			phoneNumber = TestConstants.PHONE_NUMBER_CA;
	//		}else{
	//			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
	//			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
	//			city = TestConstants.NEW_ADDRESS_CITY_US;
	//			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
	//			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
	//		}
	//
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();	  
	//		storeFrontHomePage.searchCID();
	//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
	//		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
	//		storeFrontHomePage.clickEnrollmentNextBtn();
	//		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
	//		//Enter the expired date at billing section and validate the error message
	//		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
	//		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
	//		storeFrontHomePage.selectNewBillingCardExpirationDateAsExpiredDate();
	//		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
	//		storeFrontHomePage.selectNewBillingCardAddress();
	//		storeFrontHomePage.clickOnEnrollmentNextButton();
	//		//validate  expired date error message
	//		s_assert.assertTrue(storeFrontHomePage.validateExpiredDateMessage());
	//		s_assert.assertAll(); 
	//	}

	//Hybris Project-1274:9. Express enrollment -fields validation
	@Test
	public void testExpressEnrollmentFieldsValidation_1274() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String invalidSocialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(10000000, 99999999));
		String country = driver.getCountry();
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		//validate error message for invalid sponser id
		storeFrontHomePage.searchCID(TestConstants.SPONSOR_ID_US);
		s_assert.assertTrue(storeFrontHomePage.getErrorMessageForInvalidSponser().contains("No result found"), "Error message for invalid sponser id does not visible");
		//validate error message for invalid sponser name
		storeFrontHomePage.searchCID(TestConstants.INVALID_SPONSOR_NAME);
		s_assert.assertTrue(storeFrontHomePage.getErrorMessageForInvalidSponser().contains("No result found"), "Error message for invalid sponser Name does not visible");
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectKitAndEnrollmentType(kitName, regimenName, enrollmentType);
		//validate error message for invalid password
		storeFrontHomePage.enterInvalidPassword(TestConstants.PASSWORD_BELOW_6CHARS);
		s_assert.assertTrue(storeFrontHomePage.getInvalidPasswordMessage().contains("Please enter 6 characters or more"), "Error message for invalid password does not visible");
		//validate error message for invalid confirm password
		storeFrontHomePage.enterPassword(password);
		storeFrontHomePage.enterInvalidConfirmPassword(TestConstants.PASSWORD_BELOW_6CHARS);
		s_assert.assertTrue(storeFrontHomePage.getInvalidPasswordMessage().contains("Your passwords do not match"), "Error message for invalid confirm password does not visible");
		storeFrontHomePage.enterUserInformationForEnrollment( TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		//validate that the user is able to see the section for 'Recurring monthly charges' with the ability to enter their PWS prefix
		s_assert.assertTrue(storeFrontHomePage.recurringMonthlyChargesSection(), "Recurring Monthly Charges Section should be displayed ");
		s_assert.assertTrue(storeFrontHomePage.pulseSubscriptionTextbox(), "user can enter their PWS prefix");
		//validate for invalid social security number
		storeFrontHomePage.enterSocialInsuranceNumber(invalidSocialInsuranceNumber);
		s_assert.assertTrue(storeFrontHomePage.getErrorMessageForInvalidSSN().contains("Please enter a valid Social Security Number")||storeFrontHomePage.getErrorMessageForInvalidSSN().contains("Please enter a valid Social Insurance Number"), "Error message for invalid SSN does not visible");
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);

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


	// Hybris Project-82- Version : 1 :: Allow my Spouse through EnrollmentTest 
	@Test
	public void testAllowMySpouseThroughEnrollment_82() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String country = driver.getCountry();
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();		
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
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
		storeFrontHomePage.clickEnrollmentNextBtn();

		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		//validate the error message 
		s_assert.assertTrue(storeFrontHomePage.validateErrorMessageWithoutSelectingAllCheckboxes(), "A proper error message should be displayed when continuining without selecting all the checkboxes");
		storeFrontHomePage.closePopUp();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");

		s_assert.assertAll(); 
	}

	// Hybris Project-1294:10. Standard EnrollmentTest switch to Express EnrollmentTest
	@Test
	public void testStandardEnrollmentSwitchToExpresEnrollment_1294() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
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

		s_assert.assertAll();
	}

	//Hybris Project-1296:12.Standard EnrollmentTest switch to Express EnrollmentTest - Step 5
	@Test
	public void testStandardEnrollmentSwitchToExpresEnrollmentStep5_1296() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.clickOnSwitchToExpressEnrollmentLink();

		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
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

	//Hybris Project-1304 :: Version : 1 :: Switch from RC to PC (Not existing user) 
	@Test
	public void testSwitchFromRCToPC_1304() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();		

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
		storeFrontHomePage.enterNewRCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);
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

	//	//Hybris Project-2196 :: Version : 1 :: Switch from PC to Consultant (Under Same Consultant)
	//	
	//	public void testSwitchFromPCToConsultantUnderSameConsultant_2196() throws InterruptedException {
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
	//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
	//		String lastName = "lN";
	//
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		// Click on our product link that is located at the top of the page and then click in on quick shop
	//
	//
	//		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
	//
	//		// Products are displayed?
	//		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
	//		logger.info("Quick shop products are displayed");
	//
	//		//Select a product with the price less than $80 and proceed to buy it
	//		storeFrontHomePage.applyPriceFilterLowToHigh();
	//		storeFrontHomePage.selectProductAndProceedToBuy();
	//
	//		//Cart page is displayed?
	//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
	//		logger.info("Cart page is displayed");
	//
	//		//1 product is in the Shopping Cart?
	//		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
	//		logger.info("1 product is successfully added to the cart");
	//
	//		//Click on Check out
	//		storeFrontHomePage.clickOnCheckoutButton();
	//
	//		//Log in or create an account page is displayed?
	//		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
	//		logger.info("Login or Create Account page is displayed");
	//
	//		//Enter the User information and DO  check the "Become a Preferred Customer" checkbox and click the create account button
	//		String newPCName= storeFrontHomePage.createNewPC(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);
	//
	//		//Pop for PC threshold validation
	//		s_assert.assertTrue(storeFrontHomePage.isPopUpForPCThresholdPresent(),"Threshold poup for PC validation NOT present");
	//
	//		//In the Cart page add one more product
	//		storeFrontHomePage.addAnotherProduct();
	//
	//		//Click on Check out
	//		storeFrontHomePage.clickOnCheckoutButton();
	//
	//		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
	//		storeFrontHomePage.enterMainAccountInfo();
	//		logger.info("Main account details entered");
	//
	//		storeFrontHomePage.enterSponsorIdDuringCreationOfPC(TestConstants.SPONSOR_ID_FOR_PC);
	//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPC();
	//		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
	//
	//		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
	//		//Enter Billing Profile
	//		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
	//		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
	//		storeFrontHomePage.selectNewBillingCardExpirationDate();
	//		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
	//		storeFrontHomePage.selectNewBillingCardAddress();
	//		storeFrontHomePage.clickOnSaveBillingProfile();
	//		storeFrontHomePage.clickOnBillingNextStepBtn();
	//		storeFrontHomePage.clickPlaceOrderBtn();
	//		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
	//		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
	//		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
	//		storeFrontHomePage.clickPlaceOrderBtn();
	//		
	//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
	//		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
	//		storeFrontPCUserPage=new StoreFrontPCUserPage(driver);
	//		storeFrontPCUserPage.clickOnWelcomeDropDown();
	//		storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
	//		storeFrontPCUserPage.clickOnYourAccountDropdown();
	//		storeFrontPCUserPage.clickOnPCPerksStatus();
	//		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
	//		storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
	//		storeFrontPCUserPage.cancelMyPCPerksAct();
	//		//Proceed with consultant enrollment, following pc to consultant flow
	//		//   storeFrontHomePage.clickOnOurBusinessLink();
	//		//   storeFrontHomePage.clickOnOurEnrollNowLink();
	//		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
	//		storeFrontHomePage.searchCID();
	//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
	//		storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_NAME_BIG_BUSINESS, TestConstants.REGIMEN_NAME_REVERSE);  
	//		storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
	//		storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
	//		storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
	//
	//		storeFrontHomePage.enterEmailAddress(newPCName);
	//		s_assert.assertFalse(storeFrontHomePage.verifySwitchPCToUnderDifferentConsultant(), "PC is able to move under different consultant");
	//
	//		s_assert.assertAll();
	//	}

	//	//Hybris Project-2362 :: Version : 1 :: Switch from PC to Consultant (Under Diff Consultant)
	//	
	//	public void testSwitchFromPCToConsultantUnderDifferentConsultant_2362() throws InterruptedException {
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
	//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
	//		String lastName = "lN";
	//
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		// Click on our product link that is located at the top of the page and then click in on quick shop
	//
	//
	//		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
	//
	//		// Products are displayed?
	//		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
	//		logger.info("Quick shop products are displayed");
	//
	//		//Select a product with the price less than $80 and proceed to buy it
	//		storeFrontHomePage.applyPriceFilterLowToHigh();
	//		storeFrontHomePage.selectProductAndProceedToBuy();
	//
	//		//Cart page is displayed?
	//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
	//		logger.info("Cart page is displayed");
	//
	//		//1 product is in the Shopping Cart?
	//		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
	//		logger.info("1 product is successfully added to the cart");
	//
	//		//Click on place order
	//		storeFrontHomePage.clickOnCheckoutButton();
	//
	//		//Log in or create an account page is displayed?
	//		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
	//		logger.info("Login or Create Account page is displayed");
	//
	//		//Enter the User information and DO  check the "Become a Preferred Customer" checkbox and click the create account button
	//		String newPCName= storeFrontHomePage.createNewPC(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);
	//
	//		//Pop for PC threshold validation
	//		s_assert.assertTrue(storeFrontHomePage.isPopUpForPCThresholdPresent(),"Threshold poup for PC validation NOT present");
	//
	//		//In the Cart page add one more product
	//		storeFrontHomePage.addAnotherProduct();
	//
	//		//Click on Check out
	//		storeFrontHomePage.clickOnCheckoutButton();
	//
	//		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
	//		storeFrontHomePage.enterMainAccountInfo();
	//		logger.info("Main account details entered");
	//
	//		storeFrontHomePage.enterSponsorIdDuringCreationOfPC(TestConstants.SPONSOR_ID_FOR_PC);
	//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPC();
	//
	//		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
	//
	//		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
	//		//Enter Billing Profile
	//		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
	//		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
	//		storeFrontHomePage.selectNewBillingCardExpirationDate();
	//		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
	//		storeFrontHomePage.selectNewBillingCardAddress();
	//		storeFrontHomePage.clickOnSaveBillingProfile();
	//		storeFrontHomePage.clickOnBillingNextStepBtn();
	//		storeFrontHomePage.clickPlaceOrderBtn();
	//		
	//		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
	//		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
	//		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
	//		storeFrontHomePage.clickPlaceOrderBtn();
	//		
	//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
	//		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
	//		storeFrontPCUserPage=new StoreFrontPCUserPage(driver);
	//		storeFrontPCUserPage.clickOnWelcomeDropDown();
	//		storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
	//		storeFrontPCUserPage.clickOnYourAccountDropdown();
	//		storeFrontPCUserPage.clickOnPCPerksStatus();
	//		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
	//		storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
	//		storeFrontPCUserPage.cancelMyPCPerksAct();
	//		//Proceed with consultant enrollment, following pc to consultant flow
	//		//			storeFrontHomePage.clickOnOurBusinessLink();
	//		//			storeFrontHomePage.clickOnOurEnrollNowLink(); 
	//		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
	//		storeFrontHomePage.searchCID("cid");
	//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
	//		storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_NAME_BIG_BUSINESS, TestConstants.REGIMEN_NAME_REVERSE);  
	//		storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
	//		storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
	//		storeFrontHomePage.enterLastName(TestConstants.LAST_NAME+randomNum);
	//
	//		storeFrontHomePage.enterEmailAddress(newPCName);
	//		s_assert.assertTrue(storeFrontHomePage.verifySwitchPCToUnderDifferentConsultant(), "PC is able to move under different consultant");
	//
	//		s_assert.assertAll();
	//	}

	// Hybris Phase 2-2228 :: Version : 1 :: Perform RC Account termination through my account
	@Test
	public void testAccountTerminationPageForRCUser_2228() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		//s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserEmailID),"RC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
		//s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationPageIsDisplayed(),"Account Termination Page has not been displayed");
		storeFrontAccountTerminationPage.selectTerminationReason();
		storeFrontAccountTerminationPage.enterTerminationComments();
		storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		s_assert.assertFalse(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is Present");
		storeFrontHomePage.loginAsRCUser(rcUserEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");  
		s_assert.assertAll();
	}

	// Hybris Project-1975 :: Version : 1 :: Retail user termination
	@Test
	public void testRetailUserTermination_1975() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		//s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserEmailID),"RC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
		//s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationPageIsDisplayed(),"Account Termination Page has not been displayed");
		storeFrontAccountTerminationPage.selectTerminationReason();
		storeFrontAccountTerminationPage.enterTerminationComments();
		storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		s_assert.assertFalse(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is Present");
		storeFrontHomePage.loginAsRCUser(rcUserEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");  
		s_assert.assertAll();
	}

	//Hybris Project-2199 :: Version : 1 :: Verify Existing Preferred Customer popup during RC registration.
	@Test
	public void testExistingPCPopUpDuringRCRegistration_2199() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on place order
		storeFrontHomePage.clickOnPlaceOrderButton();

		//Validate Existing PC Popup during RC Registration by entering existing PC mailid
		s_assert.assertTrue(storeFrontHomePage.validateExistingUserPopUp("pc",TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password,countryId), "Existing PC PopUp is not displayed");
		s_assert.assertFalse(storeFrontHomePage.validateCancelEnrollmentFunctionalityPC(),"Cancel Registration button not working");	

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on place order
		storeFrontHomePage.clickOnPlaceOrderButton();

		//Validate Existing PC Popup during RC Registration by entering existing PC mailid
		s_assert.assertTrue(storeFrontHomePage.validateExistingUserPopUp("pc",TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password,countryId), "Existing PC PopUp is not displayed");
		s_assert.assertFalse(storeFrontHomePage.validateSendMailToResetMyPasswordFunctionalityPC(),"Cancel Registration button not working");	

		s_assert.assertAll(); 
	}


	//Hybris Project-2200 :: Version : 1 :: Verify Existing Retail Customer popup during RC registration
	@Test
	public void testExistingRetailCustomerPopupDuringRCRegistration_2200() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on place order
		storeFrontHomePage.clickOnPlaceOrderButton();

		//Validate Existing RC Popup during RC Registration by entering existing RC mailid
		s_assert.assertTrue(storeFrontHomePage.validateExistingUserPopUp("rc",TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password,countryId), "Existing RC PopUp is not displayed");
		s_assert.assertTrue(storeFrontHomePage.validateCancelEnrollmentFunctionality(),"Cancel Registration button not working");	

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on place order
		storeFrontHomePage.clickOnPlaceOrderButton();

		//Validate Existing RC Popup during RC Registration by entering existing RC mailid
		s_assert.assertTrue(storeFrontHomePage.validateExistingUserPopUp("rc",TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password,countryId), "Existing RC PopUp is not displayed");
		s_assert.assertTrue(storeFrontHomePage.validateSendMailToResetMyPasswordFunctionalityRC(),"Cancel Registration button not working");	

		s_assert.assertAll(); 
	}


	//Hybris Project-2201 :: Version : 1 :: Verify Existing Consultant popup during RC registration
	@Test
	public void testExistingConsultantPopupDuringRCRegistration_2201() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on place order
		storeFrontHomePage.clickOnPlaceOrderButton();

		//Validate Existing Consultant Popup during RC Registration by entering existing Consultant mailid
		s_assert.assertTrue(storeFrontHomePage.validateExistingUserPopUp("consultant",TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password,countryId), "Existing Consultant PopUp is not displayed");
		s_assert.assertTrue(storeFrontHomePage.validateCancelEnrollmentFunctionalityConsultant(),"Cancel Registration button not working");	

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on place order
		storeFrontHomePage.clickOnPlaceOrderButton();

		//Validate Existing Consultant Popup during RC Registration by entering existing Consultant mailid
		s_assert.assertTrue(storeFrontHomePage.validateExistingUserPopUp("consultant",TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password,countryId), "Existing PC PopUp is not displayed");
		s_assert.assertTrue(storeFrontHomePage.validateSendMailToResetMyPasswordFunctionalityConsultant(),"Send Mail To Reset My Password button not working");	

		s_assert.assertAll(); 
	}


	//Hybris Project-2198 :: Version : 1 :: Switch from RC to PC (Under Same Consultant) 
	@Test
	public void testSwitchFromRCToPCUnderSameConsultant_2198() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop


		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on PlaceOrder
		//storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.clickOnPlaceOrderButton();

		//Log in or create an account page is displayed?
		/* s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
	  logger.info("Login or Create Account page is displayed");*/

		//Enter the User information and DONOT  check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);
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
	public void testVerifySpecialCharactersAreNotAllowed_4155() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName =  TestConstants.REGIMEN_NAME_REDEFINE;
		String sRandName = RandomStringUtils.randomAlphabetic(12);


		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_CA;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_US;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, sRandName, TestConstants.PASSWORD, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
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
		storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontConsultantPage.clickOnYourAccountDropdown();
		storeFrontConsultantPage.clickOnAutoshipStatusLink();
		storeFrontConsultantPage.subscribeToPulse();
		s_assert.assertTrue(storeFrontConsultantPage.validateErrorMessageWithSpclCharsOnPulseSubscription(), "Error/Warning message is not displayed ");
		s_assert.assertAll(); 
	}

	// Hybris Project-1295 :: Version : 1 :: Express Enrollment switch to Standard Enrollment - Step 4
	@Test
	public void testExpressEnrollmentSwitchToStandardEnrollment_1295() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		////storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//click on switch to 'standard enrollment' link
		storeFrontHomePage.clickOnSwitchToStandardEnrollmentLink(); 
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

		s_assert.assertAll(); 
	}

	// Hybris Project-1276:Email field validation for Active/Inactive users
	@Test(enabled=false)//Wrong results from database
	public void testEmailValidationsDuringEnroll_1276() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> consultantEmailList =  null;
		List<Map<String, Object>> pcEmailList =  null;
		List<Map<String, Object>> accountIDList =  null;
		List<Map<String, Object>> accountContactIDList =  null;
		List<Map<String, Object>> emailAddressIDList =  null;
		String consultantEmailID = null;
		String pcEmailID= null;
		String accountID = null;
		String accountContactID = null;
		String emailAddressID = null;
		String country = driver.getCountry();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_NAME_PERSONAL, TestConstants.REGIMEN_NAME);  
		storeFrontHomePage.chooseEnrollmentOption(TestConstants.STANDARD_ENROLLMENT);
		storeFrontHomePage.enterFirstName(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.enterLastName(TestConstants.LAST_NAME);
		storeFrontHomePage.enterPassword(password);
		storeFrontHomePage.enterConfirmPassword(password);
		storeFrontHomePage.enterAddressLine1(addressLine1);
		storeFrontHomePage.enterCity(city);
		storeFrontHomePage.selectProvince();
		storeFrontHomePage.enterPostalCode(postalCode);
		storeFrontHomePage.enterPhoneNumber(phoneNumber);

		//Code for email field validation
		// assertion for Inactive consultant less than 6 month
		accountIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_INACTIVE_CONSULTANT_LESS_THAN_6_MONTH_RFO,RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(accountIDList, "AccountID"));

		accountContactIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_CONTACT_ID_RFO,accountID),RFO_DB);
		accountContactID = String.valueOf(getValueFromQueryResult(accountContactIDList, "AccountConTactId"));

		emailAddressIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ADDRESS_ID_RFO,accountContactID),RFO_DB);
		emailAddressID = String.valueOf(getValueFromQueryResult(emailAddressIDList, "EmailAddressId"));

		consultantEmailList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_RFO,emailAddressID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(consultantEmailList, "EmailAddress"));

		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForExistingActiveCCLessThan6Month() , "Existing Active Consultant User email id should not be acceptable");

		// assertion for Active PC
		storeFrontHomePage.enterEmailAddress(TestConstants.EMAIL_ACTIVE_PC_USER);
		logger.info(TestConstants.EMAIL_ACTIVE_PC_USER);
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForExistingActivePC() , "Existing Active PC User email id should not be acceptable");

		// assertion for Active RC
		storeFrontHomePage.enterEmailAddress(TestConstants.EMAIL_ACTIVE_RC_USER);
		logger.info(TestConstants.EMAIL_ACTIVE_RC_USER);
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForExistingActiveRC() , "Existing Active RC User email id should not be acceptable");

		// assertion for Inactive PC less than 90 days
		/* storeFrontHomePage.enterEmailAddress(TestConstants.EMAIL_INACTIVE_PC_USER_LESS_THAN_90_DAYS_USER);
	    s_assert.assertTrue(storeFrontHomePage.verifyPopUpForExistingInactivePC90Days() , "Existing Inactive PC User email id before 90 days should not be acceptable");*/

		// assertion for Inactive PC greater than 90 days
		accountIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_INACTIVE_PC_MORE_THAN_90_DAYS_RFO,RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(accountIDList, "AccountID"));

		accountContactIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_CONTACT_ID_RFO,accountID),RFO_DB);
		accountContactID = String.valueOf(getValueFromQueryResult(accountContactIDList, "AccountConTactId"));

		emailAddressIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ADDRESS_ID_RFO,accountContactID),RFO_DB);
		emailAddressID = String.valueOf(getValueFromQueryResult(emailAddressIDList, "EmailAddressId"));

		pcEmailList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_RFO,emailAddressID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(pcEmailList, "EmailAddress"));
		storeFrontHomePage.enterEmailAddress(pcEmailID);
		s_assert.assertFalse(storeFrontHomePage.verifyPopUpForExistingInactivePC90Days(), "Existing Inactive PC User email id After 90 days should be acceptable");

		// assertion for Inactive consultant greater than 6 month
		accountIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_INACTIVE_CONSULTANT_MORE_THAN_6_MONTH_RFO,RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(accountIDList, "AccountID"));

		accountContactIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_CONTACT_ID_RFO,accountID),RFO_DB);
		accountContactID = String.valueOf(getValueFromQueryResult(accountContactIDList, "AccountConTactId"));

		emailAddressIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ADDRESS_ID_RFO,accountContactID),RFO_DB);
		emailAddressID = String.valueOf(getValueFromQueryResult(emailAddressIDList, "EmailAddressId"));

		consultantEmailList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_RFO,emailAddressID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(consultantEmailList, "EmailAddress"));

		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		s_assert.assertFalse(storeFrontHomePage.verifyPopUpForExistingInactiveCC180Days() , "Existing Inactive Consultant User email id before 180 days should not be acceptable");

		s_assert.assertAll();
	}

	// Hybris Project-1982:Order >>Actions >>Details
	@Test
	public void testCheckOrdersDetailsFromActionsTab_1982() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 

		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;

		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String orderId = null;
		String shippingMethodId =null;
		String lastName = null;

		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String,Object>> getOrderIDList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage=storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		//select details under Actions tab on the side of the first order no.
		String firstOrderNo=storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickDetailsUnderActionsForFirstOrderUnderOrderHistory();
		//validate product details,payment method,address,status,total,currency on the order details page
		// Get Order Id
		getOrderIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,firstOrderNo),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderIDList, "OrderID"));

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		//assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4287_RFO,orderId),RFO_DB);
		subTotalDB = df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal"));

		taxDB = df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax"));

		grandTotalDB = df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total"));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4287_RFO,orderId),RFO_DB);
		shippingDB = df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost"));

		handlingDB = df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost"));

		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc Order template subtotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"Adhoc Order template tax on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc Order template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc Order template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc Order template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"Adhoc Order template shipping method on RFO is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

		s_assert.assertAll();
	}

	//Hybris Project-4156:Consultant who cancels the pulse subscription should have their prefix active (only for the month)
	@Test
	public void testConsultantCancelPulseSubscriptionPrefixActive_4156() throws InterruptedException  {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName = TestConstants.FIRST_NAME+randomNum;
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
		storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
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
		//validate on the confirmation page user lands on .biz site
		s_assert.assertTrue(storeFrontHomePage.validateUserLandsOnPWSbizSite(), "user didn't land on PWS .biz site");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		//Fetch the PWS url
		String currentPWSUrl=driver.getCurrentUrl();
		logger.info("PWS of the user is "+currentPWSUrl);
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		//goto .com site and navigate to Account Info->Autoship tatus->Cancel pulse subscription
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontConsultantPage.clickOnYourAccountDropdown();
		storeFrontConsultantPage.clickOnAutoshipStatusLink();
		storeFrontConsultantPage.cancelPulseSubscription();
		//  logout();
		//driver.close();
		//validate with the current PWSUrl
		driver.get(currentPWSUrl);

		s_assert.assertTrue(storeFrontHomePage.verifyCurrentUrlContainBizSite(), "PWS is not contain biz url");  

		String currentComUrl= storeFrontHomePage.createBizToCom(currentPWSUrl);
		driver.get(currentComUrl);
		s_assert.assertTrue(storeFrontHomePage.verifyCurrentUrlContainComSite(), "PWS is not contain com url");
		s_assert.assertAll();
	}

	//Hybris Project-1976 :: Version : 1 :: Autoship Module. Check My Pulse UI 
	@Test
	public void testAutoshipModuleCheckMyPulseUI_1976() throws InterruptedException	 {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickCheckMyPulseLinkPresentOnWelcomeDropDown();
		//pass driver control to child window
		storeFrontConsultantPage.switchToChildWindow();
		//validate home page for pulse
		s_assert.assertTrue(storeFrontConsultantPage.validatePulseHomePage(),"Home Page for pulse is not displayed");
		storeFrontConsultantPage.switchToPreviousTab();
		s_assert.assertAll();
	}

	//Hybris Project-52 :: Version : 1 :: BIZ:Standard Enroll Kit USD $45 Business Portfolio (CRP:Y P:Y) 
	@Test
	public void testStandardEnrollmentBusinessPortfolio_52() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_PORTFOLIO;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_PORTFOLIO;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

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
		storeFrontHomePage.selectProvince();
		storeFrontHomePage.enterPostalCode(postalCode);
		storeFrontHomePage.enterPhoneNumber(phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.clickEnrollmentNextBtn();

		// assert enrollment is standard or not
		s_assert.assertTrue(storeFrontHomePage.verifyCRPSelectionpageIsDisplayed(), "It is not standard enrollment");
		s_assert.assertAll();
	}


	// Hybris Project-3009 :: Version : 1 :: Reset the password from the storefront and check login with new password
	@Test 
	public void testResetPasswordFromStorefrontAndRecheckLogin_3009() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){

			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
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
		//Reset New Password from the store front
		storeFrontAccountInfoPage.enterOldPassword(password);
		storeFrontAccountInfoPage.enterNewPassword(TestConstants.CONSULTANT_NEW_PASSWORD);
		storeFrontAccountInfoPage.enterConfirmedPassword(TestConstants.CONSULTANT_NEW_PASSWORD);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		logout();
		//validate login with new password
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_NEW_PASSWORD);   
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		//Reset old password
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage.enterOldPassword(TestConstants.CONSULTANT_NEW_PASSWORD);
		storeFrontAccountInfoPage.enterNewPassword(password);
		storeFrontAccountInfoPage.enterConfirmedPassword(password);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertAll(); 
	}

	// Hybris Project-4260 :: Version : 1 :: UserName Field: Edit & Login 
	@Test
	public void testUserNameFieldEditAndLogin_4260() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
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
		//Enter new user name logout and validate the new user
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_EMAIL_ID_FOR_ACCOUNTINFO);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		logout();
		//login with the same user
		//storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_FOR_ACCOUNTINFO,password);   
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		//validate the new user
		s_assert.assertTrue(TestConstants.CONSULTANT_EMAIL_ID_FOR_ACCOUNTINFO.equalsIgnoreCase(storeFrontAccountInfoPage.getUserName()),"Username didn't match");
		logout();
		//reset username
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_FOR_ACCOUNTINFO,password);   
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage.enterUserName(consultantEmailID);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertAll();
	}

	//Hybris Project-86 :: Version : 1 :: Edit Allow my spouse in My Account  
	@Test
	public void testEditAllowMySpouseInMyAccount_86() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("error");
			if(isSiteNotFoundPresent){
				logger.info("login error for the user "+consultantEmailID);
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
		//click on Allow my Spouse to manage my account
		storeFrontAccountInfoPage.checkAllowMySpouseCheckBox();
		//Enter first,last name and click accept on the popup and validate
		s_assert.assertTrue(storeFrontAccountInfoPage.validateEnterSpouseDetailsAndAccept(),"Accept button not working in the popup");
		//click cancel on the 'Allow my spouse' popup and validate
		s_assert.assertTrue(storeFrontAccountInfoPage.validateClickCancelOnProvideAccessToSpousePopup(),"Cancel button not working in the popup");
		s_assert.assertAll();
	}

	//Hybris Project-2304 :: Version : 1 :: check Cart from Mini cart after adding product
	@Test
	public void testCheckCartFromMiniCartAfterAddingProduct_2304() throws InterruptedException {
		//Navigate to the website
		storeFrontHomePage = new StoreFrontHomePage(driver);

		//Add a item to the cart and validate the mini cart in the header section
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		s_assert.assertTrue(storeFrontHomePage.validateMiniCart(), "mini cart is not being displayed");

		//click on mini cart and validate the cart page with pre-added products
		s_assert.assertTrue(storeFrontHomePage.clickMiniCartAndValidatePreaddedProductsOnCartPage(), "preadded products on cart page is not displayed");  
		s_assert.assertAll();
	}

	//Hybris Project-2327 :: Version : 1 :: check Mini Cart - Not Logged In user
	@Test
	public void testCheckMiniCartForNotLoggedInUser_2327() throws InterruptedException {
		//Navigate to the website
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//validate no mini cart is shown for not logged in user
		s_assert.assertFalse(storeFrontHomePage.validateMiniCart(), "mini cart is displayed for not registered user");
		//Add a item to the cart and validate the mini cart in the header section
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		s_assert.assertTrue(storeFrontHomePage.validateMiniCart(), "mini cart is not being displayed");
		s_assert.assertAll();
	}


	//Hybris Project-2329 :: Version : 1 :: check Mini Cart - Consultant 
	@Test
	public void testCheckMiniCartForConsultant_2329() throws InterruptedException  {
		//Login as consultant and validate the 'Next CRP' mini cart in the header section
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
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
		s_assert.assertTrue(storeFrontConsultantPage.validateNextCRPMiniCart(), "next CRP Mini cart is not displayed");
		//Add a item to the cart and validate the mini cart in the header section
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		s_assert.assertTrue(storeFrontHomePage.validateMiniCart(), "mini cart is not being displayed");
		s_assert.assertAll();
	}

	//Hybris Project-2328 :: Version : 1 :: check Mini Cart - PC 
	@Test
	public void testCheckMiniCartForPC_2328() throws InterruptedException {
		//Login as consultant and validate the 'Next PC PERKS' mini cart in the header section
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){

			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);  

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		logger.info("login is successful");

		s_assert.assertTrue(storeFrontPCUserPage.validateNextPCPerksMiniCart(), "next PC Perks  Mini cart is not displayed");
		//Add a item to the cart and validate the mini cart in the header section
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		s_assert.assertTrue(storeFrontHomePage.validateMiniCart(), "mini cart is not being displayed");
		s_assert.assertAll();
	}


	// Hybris Project-87 :: Version : 1 :: Standard Enrollment Billing Profile Edit Shipping Info 
	@Test
	public void testStandardEnrollmentExpressBusinessKitRedefineRegimenEdit_87() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String newAddressLine1=null;
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			newAddressLine1 = TestConstants.NEW_ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			newAddressLine1 = TestConstants.NEW_ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
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
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.clickOnReviewAndConfirmShippingEditBtn();
		String newFirstName = TestConstants.FIRST_NAME+randomNum;
		String newLastName = TestConstants.LAST_NAME+randomNum;
		storeFrontHomePage.enterUserInformationForEnrollment(newFirstName, newLastName, password, newAddressLine1, city, postalCode, phoneNumber);
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


	//Hybris Project-4281 :: Version : 1 :: Terminate User and Login with User Name
	@Test 
	public void terminateUserAndLoginWithSameUsername_4281() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Terminated User doesn't get Login failed");  
		s_assert.assertAll();
	}

	//Hybris Project-4161 :: Version : 1 :: During enrollment customer not opt for pulse but they still able to access the pws as a trial version 
	@Test
	public void testCustomerEnrollWithoutPulseCanAccessPws_4161() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName = TestConstants.FIRST_NAME+randomNum;

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
		//storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
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
		//validate on the confirmation page user lands on .biz site
		s_assert.assertTrue(storeFrontHomePage.validateUserLandsOnPWSbizSite(), "user didn't land on PWS .biz site");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		//Fetch the PWS url
		String currentPWSUrl=driver.getCurrentUrl();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		logout();
		//validate with the current PWSUrl on .com site
		driver.get(storeFrontHomePage.navigateToCommercialWebsite(currentPWSUrl));
		s_assert.assertTrue(storeFrontHomePage.validatePWS(), "PWS is not active");  
		s_assert.assertAll();
	}

	// Hybris Project-1306 :: Version : 1 :: Biz: PC Enroll- Not my sponsor link 
	@Test
	public void testPCEnrollNotMySponsorLink_1306() throws InterruptedException	 {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		String firstName = TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");
		//Enter the User information and DO check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
		storeFrontHomePage.enterMainAccountInfo();
		storeFrontHomePage.clickOnRequestASponsorBtn();
		storeFrontHomePage.clickOnNotYourSponsorLink();
		storeFrontHomePage.clickOKOnSponsorInformationPopup();
		storeFrontHomePage.searchCIDForPCAndRC();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();

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
		s_assert.assertTrue(storeFrontHomePage.validatePCPerksCheckBoxIsDisplayed());
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		s_assert.assertAll(); 
	}

	// Hybris Project-2153 :: Version : 1 :: Check the shipping method disclaimers for " UPS Standard Overnight/FedEx Standard Overnight" 
	@Test
	public void testCheckShippingMethodDisclaimersForUPSStandardOvernight_2153() throws InterruptedException	 {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
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
		//storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn(); 
		// Check the shipping method disclaimers for " UPS Ground (HD)/FedEx Standard Overnight" as Consultant on Checkout screen>>Shipment section
		s_assert.assertTrue(storeFrontHomePage.validateShippingMethodDisclaimersForUPSGroundHD());
		s_assert.assertAll(); 
	}

	// Hybris Project-1305:From Corporate user should able to change sponsor RC
	@Test
	public void testCorporateUserShouldAbleToChangeSponsorRC_1305() throws InterruptedException	 {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");
		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
		storeFrontHomePage.enterMainAccountInfo();
		//Enter Invalid sponsor id & name and validate error message
		storeFrontHomePage.enterSponsorIdDuringCreationOfPC(TestConstants.INVALID_SPONSOR_ID);
		s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(), "Invalid sponsor ID accepted");
		storeFrontHomePage.clickSearchAgain();

		storeFrontHomePage.enterSponsorIdDuringCreationOfPC(TestConstants.INVALID_SPONSOR_NAME);
		s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(), "Invalid sponsor Name accepted");
		storeFrontHomePage.clickSearchAgain();

		storeFrontHomePage.searchCIDForPCAndRC();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();

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
		//validate pc perks terms and condition check box is displayed
		s_assert.assertTrue(storeFrontHomePage.validatePCPerksCheckBoxIsDisplayed());
		//check the check box
		storeFrontHomePage.checkPCPerksCheckBox();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();


		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		s_assert.assertAll(); 
	}

	//Hybris Project-2489:Verify if Consultant can use same prefix, when he is subscribing to pulse. ( Consultant's trial version)
	@Test
	public void testVerifyConsultantCanUaseSamePrefixWhenSubscribingToPulse_2489() throws InterruptedException	  {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		String firstName=TestConstants.FIRST_NAME+randomNum;
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
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		storeFrontHomePage.clickEnrollmentNextBtn();
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
		s_assert.assertAll(); 
	}

	// Hybris Phase 2-2047 :: Version : 1 :: Edit billing profile on 'Billing Profile' page 
	@Test
	public void testEditBillingProfileOnBillingProfilePage_2047() throws InterruptedException, SQLException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 

		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;

		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}


		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

		storeFrontBillingInfoPage.clickOnEditBillingProfile();
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate();
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();

		//--------------- Verify that Newly edited Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on the page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template contains the newly edited billing profile------------------------------------------------------------		

		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"Autoship Template Payment Method doesn't contains the newly edited billing profile when future autoship checkbox is selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		//------------------ Verify that adhoc orders template doesn't contains the newly edited billing profile ------------------------------------------------------------

		s_assert.assertFalse(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"AdHoc Orders Template Payment Method contains the newly edited billing profile when future autoship checkbox is selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		s_assert.assertAll();
	}


	//Hybris Project-93 :: Version : 1 :: Express Enrollment Billing Profile Main Account Info - Edit 
	@Test
	public void testExpressEnrollmentBillingProfileMainAccountInfoEdit_93() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//click edit next to main account info and validate setup account page is displayed
		storeFrontHomePage.clickOnReviewAndConfirmShippingEditBtn();
		s_assert.assertTrue(storeFrontHomePage.validateSetUpAccountPageIsDisplayed(), "SetUp account page is not displayed");
		//Edit contact Info and re-enter password
		storeFrontHomePage.reEnterContactInfoAndPassword();
		//validate updated Account Info Details on review and confirmation page
		s_assert.assertTrue(storeFrontHomePage.validateUpdatedMainAccountInfo());
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		s_assert.assertAll(); 
	}

	// Hybris Project-143:CRP Template - Check Threshold by Add/remove products and increase/decrease qty
	@Test
	public void testCheckThresholdByAddRemoveProductsAndIncreaseDecreaseQty_143() throws InterruptedException {
		String qtyOfProducts="10";
		String newQtyOfProducts="5";
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		String firstName = TestConstants.FIRST_NAME+randomNum;
		emailID = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum,emailID, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		////storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
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
		storeFrontHomePage.clickOnContinueShoppingLink();
		//add a product and add it to crp
		storeFrontHomePage.selectAnotherProductAndAddItToCRP();
		storeFrontHomePage.removeSecondProductFromTheCart();
		s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is "+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);
		storeFrontHomePage.removeFirstProductFromTheCart();
		storeFrontHomePage.clickOnContinueShoppingLink();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct(qtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected msg is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);
		storeFrontHomePage.addQuantityOfProduct(newQtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();
		storeFrontHomePage.loginAsConsultant(emailID, password);
		storeFrontHomePage.clickOnAutoshipCart();
		storeFrontHomePage.clickOnContinueShoppingLink();
		storeFrontHomePage.selectDifferenetProductAndAddItToCRP();
		storeFrontHomePage.removeSecondProductFromTheCart();
		storeFrontHomePage.removeFirstProductFromTheCart();
		if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG),"Error message for threshold condition from UI is "+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG);
		}else
			s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_CA),"Error message for threshold condition from UI is "+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_CA);

		storeFrontHomePage.addQuantityOfProduct(qtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG);
		storeFrontHomePage.addQuantityOfProduct(newQtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG);
		newQtyOfProducts ="0";
		storeFrontHomePage.addQuantityOfProduct(newQtyOfProducts);
		if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG),"Error message for threshold condition from UI is "+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG);
		}else
			s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_CA),"Error message for threshold condition from UI is "+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_CA);

		s_assert.assertAll(); 
	}

	// Hybris Project-142 Autoship template - manage products in cart - PC perk 
	@Test
	public void testAutoshipTemplateManagePoductsInCartPCPerk_142() throws InterruptedException	{
		String QtyOfProducts="10";
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = TestConstants.PC_USERNAME;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);  

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
			storeFrontPCUserPage=storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			logger.info("login is successful"); 
			//click on auto ship cart
			storeFrontHomePage.clickOnAutoshipCart();
			//validate update pc perks cart page is displayed
			s_assert.assertTrue(storeFrontHomePage.validateUpdateCartPageIsDisplayed(),"pc perks update cart page is not displayed");
			//click on continue shopping link
			storeFrontHomePage.clickOnContinueShoppingLink();
			//add a product and add it to pc perks
			storeFrontHomePage.selectAProductAndAddItToPCPerks();
			//validate pc perks update cart message
			s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG);
			//click on continue shopping link
			storeFrontHomePage.clickOnContinueShoppingLink();
			//add a product and add it to pc perks
			storeFrontHomePage.selectAProductAndAddItToPCPerks();
			//validate pc perks update cart message
			s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG);
			//Remove products until SV is less than threshold US - SV is less than $80 CA - SV is less than CAD $90
			s_assert.assertTrue(storeFrontHomePage.validateErrorMsgAfterRemovingProductsFromPcCart(),"Error message is not being displayed");
			//update the product Qty by Increasing it and validate the  pc perks update cart message
			storeFrontHomePage.addQuantityOfProduct(QtyOfProducts);
			s_assert.assertTrue(storeFrontHomePage.validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(),"pc perks update cart message is not displayed");
			//update the product Qty Decreasing it and validate the  Error message
			s_assert.assertTrue(storeFrontHomePage.validateErrorMsgAfterRemovingProductsFromPcCart(),"Error message is not being displayed");
			s_assert.assertAll(); }
	}

	//Hybris Project-4350:Verify "Join my team" button on the .com and .biz site
	@Test
	public void testJoinMyTeamButtonPresentOnTheComAndBizSite_4350(){
		country = driver.getCountry();
		env = driver.getEnvironment();		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String bizPws = storeFrontHomePage.getBizPWS(country, env);
		String comPws = storeFrontHomePage.convertBizToComPWS(bizPws);
		storeFrontHomePage.openPWS(comPws);
		storeFrontHomePage.clickOnUserName();
		s_assert.assertFalse(storeFrontHomePage.verifyJoinMyTeamLinkPresent(), "Join My Team Link present on the Com page");
		storeFrontHomePage.openPWS(bizPws);
		storeFrontHomePage.clickOnUserName();
		s_assert.assertTrue(storeFrontHomePage.verifyJoinMyTeamLinkPresent(), "Join My Team Link is not present on the Biz page");
		s_assert.assertAll(); 
	}

	//Hybris Project-4332:Verify Meet Your Consultant Page from .biz site after clicking on Personalize My Profile link
	@Test
	public void testMeetyourConsultantPageFromBizSiteAfterClickOnPersonalizeMyProfileLink_4332(){
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null; //TestConstants.CONSULTANT_USERNAME;
		String consultantPWSURL = null; //TestConstants.CONSULTANT_BIZ_URL;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry()),RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.getHomtownNamePresentOnAfterClickonPersinalizeLink().length()>0, "HomeTown text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getConsultantSinceTextPresentAfterClickonPersinalizeLink().length()>0, "Consultant since text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getFavoriteProductNameIsPresentAfterClickonPersinalizeLink().length()>0, "Favorite product text is not present on meet your consultant page");
		s_assert.assertAll(); 
	}

	//Hybris Project-4333:Verify Meet Your Consultant Page from .com site after clicking on Personalize My Profile link.
	@Test
	public void testMeetyourConsultantPageFromComSiteAfterClickOnPersonalizeMyProfileLink_4333(){
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null; //TestConstants.CONSULTANT_USERNAME;
		String consultantPWSURL = null; //TestConstants.CONSULTANT_COM_URL;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry()),RFO_DB);

		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
		consultantPWSURL = storeFrontHomePage.convertBizToComPWS(consultantPWSURL);

		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.getHomtownNamePresentOnAfterClickonPersinalizeLink().length()>0, "HomeTown text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getConsultantSinceTextPresentAfterClickonPersinalizeLink().length()>0, "Consultant since text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getFavoriteProductNameIsPresentAfterClickonPersinalizeLink().length()>0, "Favorite product text is not present on meet your consultant page");
		s_assert.assertAll();
	}

	//Hybris Project-3832:Verify ABOUT SECTION of Meet Your Consultant Page
	@Test
	public void testAboutSectionOfMeetYourConsultantPage_3832(){
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null; //TestConstants.CONSULTANT_USERNAME;
		String consultantPWSURL = null; //TestConstants.CONSULTANT_COM_URL;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry()),RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");

		// For .com site
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.getHomtownNamePresentOnAfterClickonPersinalizeLink().length()>0, "HomeTown text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getConsultantSinceTextPresentAfterClickonPersinalizeLink().length()>0, "Consultant since text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getFavoriteProductNameIsPresentAfterClickonPersinalizeLink().length()>0, "Favorite product text is not present on meet your consultant page");

		// For .biz site
		consultantPWSURL = storeFrontHomePage.convertBizToComPWS(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.getHomtownNamePresentOnAfterClickonPersinalizeLink().length()>0, "HomeTown text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getConsultantSinceTextPresentAfterClickonPersinalizeLink().length()>0, "Consultant since text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getFavoriteProductNameIsPresentAfterClickonPersinalizeLink().length()>0, "Favorite product text is not present on meet your consultant page");
		s_assert.assertAll();
	}

	//Hybris Project-3836:Verify 3 Content Block SECTION of Meet Your Consultant Page
	@Test
	public void testContentBlockSectionOfMeetYourConsultantPage_3836(){
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null; //TestConstants.CONSULTANT_USERNAME;
		String consultantPWSURL = null; //TestConstants.CONSULTANT_COM_URL;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry()),RFO_DB);

		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
		//consultantPWSURL = storeFrontHomePage.convertBizToComPWS(consultantPWSURL);

		// for .com site
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfWhyIJoinedOnMeetYourConsultantPage(), "Why I joined Rodan + Fields block is not Present on meet your consultant page ");
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfProductsOnMeetYourConsultantPage(), "What I love most about R+F products block is not Present on meet your consultant page ");

		// For .biz site
		consultantPWSURL = storeFrontHomePage.convertBizToComPWS(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfWhyIJoinedOnMeetYourConsultantPage(), "Why I joined Rodan + Fields block is not Present on meet your consultant page ");
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfProductsOnMeetYourConsultantPage(), "What I love most about R+F products block is not Present on meet your consultant page ");
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfBusinessOnMeetYourConsultantPage(), "What I love most about my R+F Business block is not Present on meet your consultant page ");
		s_assert.assertAll();
	}

	// Hybris Project-3854:Register as RC with Different CA Sponsor WITH Pulse
	@Test
	public void testRegisterAsRCWithDifferentCASponserWithPulse_3854() throws InterruptedException {

		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			RFO_DB = driver.getDBNameRFO();  
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = "lN";
			List<Map<String, Object>> canadianSponserList =  null;
			String canadianSponserHavingPulse = null;
			country = driver.getCountry();
			String firstName=TestConstants.FIRST_NAME+randomNum;
			enrollmentType = TestConstants.STANDARD_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

			storeFrontHomePage = new StoreFrontHomePage(driver);
			// Get Canadian sponser with PWS from database
			canadianSponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry()),RFO_DB);
			canadianSponserHavingPulse = String.valueOf(getValueFromQueryResult(canadianSponserList, "AccountID"));

			// sponser search by Account Number
			List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,canadianSponserHavingPulse),RFO_DB);
			String idForConsultant = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

			storeFrontHomePage.openPWSSite(country, driver.getEnvironment());

			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
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

			//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
			storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);

			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");
			storeFrontHomePage.clickOnNotYourSponsorLink();
			//assert continue without sponser link is not present and request your sponser button
			s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue without sponser link present");
			s_assert.assertFalse(storeFrontHomePage.verifyRequestASponsorBtn(),"Request a sponser button is present");
			//storeFrontHomePage.clickOKOnSponsorInformationPopup();
			storeFrontHomePage.searchCIDForSponserHavingPulseSubscribed(idForConsultant);
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
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
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			//Fetch the PWS url and assert
			String currentPWSUrl=driver.getCurrentUrl();
			s_assert.assertFalse(storeFrontHomePage.verifyPWSAfterSuccessfulEnrollment(TestConstants.SPONSER_PWS,currentPWSUrl),"PWS after enrollment are same");
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			s_assert.assertAll(); 

		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}


	//Hybris Project-2250:Corporate Sponsor: PC and RC can create an account without a Sponsor -- will be set to Corporate
	@Test
	public void testPCAndRCAccountCreationWithoutASponsor_2250() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		//RC-Enrollment
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

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
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);

		//Enter the Main account info  and click next
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
		logout();
		//click on the rodanfields Logo
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		//PC-Enrollment
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

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
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);

		//Pop for PC threshold validation
		s_assert.assertTrue(storeFrontHomePage.isPopUpForPCThresholdPresent(),"Threshold poup for PC validation NOT present");

		//In the Cart page add one more product
		storeFrontHomePage.addAnotherProduct();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Enter the Main account info and click next
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
		s_assert.assertAll(); 
	}

	// Hybris Project-3774:Register as RC with Different CA Sponsor WITH Pulse
	@Test
	public void testRegisterAsRCWithDifferentCASponserWithPulse_3774() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();	
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);

		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		List<Map<String, Object>> canadianSponserList =  null;
		List<Map<String, Object>> sponsorIdList =  null;

		String CCS = null;
		country = driver.getCountry();
		String firstName=TestConstants.FIRST_NAME+randomNum;
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		storeFrontHomePage = new StoreFrontHomePage(driver);

		// Get Canadian sponser with PWS from database

		canadianSponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry()),RFO_DB);
		//canadianSponserHavingPulse = (String) getValueFromQueryResult(canadianSponserList, "AccountID");
		String pws = String.valueOf(getValueFromQueryResult(canadianSponserList, "URL"));
		storeFrontHomePage.openPWS(pws);

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
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

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.clickOnNotYourSponsorLink();
		//assert continue without sponser link is not present and request your sponser button
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue without sponser link present");
		s_assert.assertFalse(storeFrontHomePage.verifyRequestASponsorBtn(),"Request a sponser button is present");
		//storeFrontHomePage.clickOKOnSponsorInformationPopup();

		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SPONSOR_ID,countryId),RFO_DB);
		CCS = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");

		storeFrontHomePage.searchCIDForSponserHavingPulseSubscribed(CCS);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
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
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		//Fetch the PWS url and assert
		String currentPWSUrl=driver.getCurrentUrl();
		s_assert.assertFalse(storeFrontHomePage.verifyPWSAfterSuccessfulEnrollment(pws,currentPWSUrl),"PWS after enrollment are same");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll(); 
	}

	//Hybris Project-3881:CORP:Join PCPerk in the Order Summary section - CA Spsonor with Pulse
	@Test
	public void testJoinPCPerkWhileEnrollingWithCASponserWithPulse_3881() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String rcUserEmailID = null;
		country = driver.getCountry();
		env = driver.getEnvironment();  

		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		String firstName = TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

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
		rcUserEmailID = firstName+"@xyz.com";
		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, rcUserEmailID, password);

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

		// open pws site
		storeFrontHomePage.openPWSSite(country, env);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);

		//s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserEmailID),"RC Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		// Click on Shop link and select All product link  
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();

		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		storeFrontHomePage.clickOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontHomePage.verifyEditPcPerksIsPresentInWelcomDropdownForUpgrade(), "User NOT registered successfully");
		s_assert.assertAll(); 
	}


	//Hybris Project-3878 :: Version : 1 :: CORP:Join PCPerk in the shipment -CA Sposnor WITHOUT Pulse
	@Test 
	public void testJoinPCPerkWhileEnrollingWithCASponserWithoutPulse_3878() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);

		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		String rcUserEmailID = null;
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

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
		rcUserEmailID = firstName+"@xyz.com";
		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, rcUserEmailID, password);

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
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		// Click on Shop link and select All product link  
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		// get pws from query for assertion
		List<Map<String, Object>> canadianSponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_NO_PWS_RFO,driver.getEnvironment(),driver.getCountry()),RFO_DB);
		String sponserWithoutPulse = String.valueOf(getValueFromQueryResult(canadianSponserList, "AccountID"));

		// sponser search by Account Number
		List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserWithoutPulse),RFO_DB);
		String CCS = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");

		storeFrontHomePage.searchCIDForSponserHavingPulseSubscribed(CCS);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		storeFrontHomePage.clickOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontHomePage.verifyEditPcPerksIsPresentInWelcomDropdownForUpgrade(), "User NOT registered successfully");

		s_assert.assertAll(); 
	}

	//	//Hybris Project-3757 :: Version : 1 :: Join PCPerk in the shipment section - CA Spsonor with Pulse
	//	@Test(enabled=false)
	//	public void testJoinPCPerkWhileEnrollingWithCASponserWithPulse_3757() throws InterruptedException {
	//		RFO_DB = driver.getDBNameRFO();
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
	//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
	//		String firstName=TestConstants.FIRST_NAME+randomNum;
	//		String lastName = "lN";
	//		country = driver.getCountry();
	//		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
	//		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
	//		env = driver.getEnvironment();  
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		storeFrontHomePage.openPWSSite(country, env);
	//
	//		if(country.equalsIgnoreCase("CA")){
	//			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
	//			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
	//			city = TestConstants.CITY_CA;
	//			postalCode = TestConstants.POSTAL_CODE_CA;
	//			phoneNumber = TestConstants.PHONE_NUMBER_CA;
	//		}else{
	//			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
	//			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
	//			city = TestConstants.CITY_US;
	//			postalCode = TestConstants.POSTAL_CODE_US;
	//			phoneNumber = TestConstants.PHONE_NUMBER_US;
	//		}
	//		// Click on Shop link and select All product link  
	//		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
	//		// Products are displayed?
	//		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
	//		logger.info("Quick shop products are displayed");
	//
	//		//Select a product and proceed to buy it
	//		storeFrontHomePage.selectProductAndProceedToBuy();
	//		//Click on Check out
	//		storeFrontHomePage.clickOnCheckoutButton();
	//
	//		//Log in or create an account page is displayed?
	//		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
	//		logger.info("Login or Create Account page is displayed");
	//		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
	//		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
	//		//select checkbox for join pc perks in order summary section
	//		storeFrontHomePage.checkPCPerksCheckBox();
	//		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
	//		storeFrontHomePage.enterMainAccountInfo();
	//		logger.info("Main account details entered");
	//		storeFrontHomePage.clickOnNotYourSponsorLink();
	//		//assert continue without sponser link is not present and request your sponser button
	//		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue without sponser link present");
	//		s_assert.assertFalse(storeFrontHomePage.verifyRequestASponsorBtn(),"Request a sponser button is present");
	//		//storeFrontHomePage.clickOKOnSponsorInformationPopup();
	//
	//		// get pws from query for assertion
	//		List<Map<String, Object>> canadianSponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry()),RFO_DB);
	//		String canadianSponserHavingPulse = String.valueOf(getValueFromQueryResult(canadianSponserList, "AccountID"));
	//		String pws = String.valueOf(getValueFromQueryResult(canadianSponserList, "URL"));
	//		String comPws = storeFrontHomePage.createBizToCom(pws);
	//		System.out.println("com PWS is "+comPws);
	//
	//		// sponser search by Account Number
	//		List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,canadianSponserHavingPulse),RFO_DB);
	//		String CCS = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
	//
	//
	//		storeFrontHomePage.searchCIDForSponserHavingPulseSubscribed(CCS);
	//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
	//		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
	//		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
	//		//Enter Billing Profile
	//		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
	//		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
	//		storeFrontHomePage.selectNewBillingCardExpirationDate();
	//		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
	//		storeFrontHomePage.selectNewBillingCardAddress();
	//		storeFrontHomePage.clickOnSaveBillingProfile();
	//		storeFrontHomePage.clickOnBillingNextStepBtn();
	//		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
	//		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
	//		storeFrontHomePage.clickPlaceOrderBtn();
	//		
	//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
	//		//Fetch the PWS url and assert
	//		String currentPWSUrl=driver.getCurrentUrl();
	//		logger.info("Fetched Pws After enrollment is "+currentPWSUrl);
	//		s_assert.assertTrue(storeFrontHomePage.verifyPCUserIsOnSponserPWSAfterSuccessfulEnrollment(comPws,currentPWSUrl),"PC User is not on sponser pws after enrollment");
	//		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
	//		s_assert.assertAll(); 
	//	}

	//Hybris Project-2249 :: Version : 1 :: Verify Change Sponsor functionality
	@Test
	public void testVerifyChangeSponsorFunctionality_2249() throws InterruptedException	{
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.clickNotYourSponsorLinkOnKitPage();
		storeFrontHomePage.searchCIDOnSelectASponsorPopUp("cid");
		String sponsorname = storeFrontHomePage.clickOnSelectAndContinueOnSelectASponsorPopUp();
		s_assert.assertTrue(storeFrontHomePage.getSponsorNameFromHeaderKitPage().equalsIgnoreCase(sponsorname), "Sponsor Name on Kit Page Header is "+storeFrontHomePage.getSponsorNameFromHeaderKitPage()+" while selected sponsor name was "+sponsorname);
		s_assert.assertAll();
	}

	//Hybris Project-2251 :: Version : 1 :: Global Sponsorship: Cross Country Sponsor
	@Test
	public void testGlobalSponsorshipCrossCountrySponsor_2251() throws InterruptedException	{
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REVERSE;
		String CCS = null;
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> sponsorIdList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		if(country.equalsIgnoreCase("CA")){
			sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SPONSOR_ID,countryId),RFO_DB);
			CCS = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");

		}else{
			sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SPONSOR_ID,countryId),RFO_DB);
			CCS = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
		}
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID(CCS);
		s_assert.assertTrue(storeFrontHomePage.isSponsorPresentInSearchList(), "No Sponsor present in search results");

		s_assert.assertAll();
	}

	// Hybris Project-1291:5. Terms and Conditions- Standard enrollment with out CRP or Pulse
	@Test
	public void testStandardEnrollmentTermsAndConditionsWithoutCRPOrPulse_1291() throws InterruptedException{
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
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.checkCRPCheckBox();
		storeFrontHomePage.checkPulseCheckBox();
		s_assert.assertFalse(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsSelected(), "Subscribe to pulse checkbox not selected");
		s_assert.assertFalse(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsSelected(), "Enroll to CRP checkbox not selected");
		storeFrontHomePage.clickEnrollmentNextBtn();
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

	//Hybris Project-2279:Add Multiple Billing profiles and during checkout
	@Test
	public void testAddMultipeBillingProfileDuringCheckout_2279() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);

		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		//storeFrontUpdateCartPage.clickOnUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		//s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		//storeFrontUpdateCartPage.clickOnConfirmationOK();
		//storeFrontUpdateCartPage.clickOnUpdateCartButton();

		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();

		for(int i=0;i<2;i++){
			storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
			storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
			storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
			storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontUpdateCartPage.selectNewBillingCardAddress();
			storeFrontUpdateCartPage.clickOnSaveBillingProfile();
			s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefaultAfterClickOnEdit(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
		}

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickBillingEditAfterSave();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefaultAfterClickOnEdit(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		//------------------ Verify that adhoc orders template doesn't contains the newly created billing profile by verifying by name------------------------------------------------------------

		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"AdHoc Orders Template Payment Method contains new billing profile when future autoship checkbox not selected");


		s_assert.assertAll();

	}

	//Hybris Project-2297:Remove one of the product from Multiple Orderline Cart--> Subtotal is recalculated
	@Test
	public void testModifyQtyInCartAndValidateSubTotal_2297() throws InterruptedException	 {
		RFO_DB = driver.getDBNameRFO(); 
		String country = driver.getCountry();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;

		if(country.equalsIgnoreCase("us")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
		}
		else if(country.equalsIgnoreCase("ca")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
		} 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

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
		//update qty to 2 of the first product
		storeFrontHomePage.addQuantityOfProduct("2"); 
		//add another product in the cart
		storeFrontHomePage.addAnotherProduct();

		logger.info("2 products are successfully added to the cart");
		//update qty to 2 of the second product
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("2"); 

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//click on edit shopping cart link
		storeFrontHomePage.clickEditShoppingCartLink();
		//Increase the qty of product 1 to 3
		storeFrontHomePage.addQuantityOfProduct("3"); 
		//Decrease the qty of product2 to 1
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("1");

		//get the sub-total of the first product
		double subtotal1=storeFrontHomePage.getSubTotalOfFirstProduct();
		//get the sub-total of the second product
		double subtotal2=storeFrontHomePage.getSubTotalOfSecondProduct();
		//validate sub-total is recalculated accordingly to the updated qty of product(s)
		s_assert.assertTrue(storeFrontHomePage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		//Remove product 2 from the cart
		storeFrontHomePage.removefirstProductFromTheCart();
		//validate product has been removed from the cart
		s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is "+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);
		subtotal2=0;
		//validate sub-total is recalculated accordingly after removing product 2
		s_assert.assertTrue(storeFrontHomePage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		//click on check-out button
		storeFrontHomePage.clickOnCheckoutButton();
		s_assert.assertAll(); 
	}

	//Hybris Project-2248 :: Version : 1 :: Verify Request a sponsor functionality
	@Test
	public void testVerifyRequestASponsorFunctionality_2248() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		country = driver.getCountry();
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		String emailId = firstName+lastName+"@xyz.com";
		String postalCode = null;
		if(country.equalsIgnoreCase("CA")){			
			postalCode = TestConstants.POSTAL_CODE_CA;			
		}else{			
			postalCode = TestConstants.POSTAL_CODE_US;			
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.clickOnBelowFieldsSponsorLink();
		storeFrontHomePage.enterDetailsInRequestASponsorForm(firstName,lastName,emailId,postalCode);
		s_assert.assertTrue(storeFrontHomePage.verifyConfirmationMessageAfterSubmission(), "your request has not been submitted");
		s_assert.assertAll();
	}

	//Hybris Project-2302:REmove Product from CArt
	@Test
	public void testValidateRemoveProductsFromCart_2302() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		String country = driver.getCountry();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		if(country.equalsIgnoreCase("us")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
		}
		else if(country.equalsIgnoreCase("ca")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
		} 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		//Add multiple quantities of multiple product in the cart
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
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
		//update qty to 2 of the first product
		storeFrontHomePage.addQuantityOfProduct("3"); 
		//add another product in the cart
		storeFrontHomePage.addAnotherProduct();

		//update qty to 2 of the second product
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("3"); 
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//click on edit shopping cart link
		storeFrontHomePage.clickEditShoppingCartLink();
		//get the sub-total of the first product
		double subtotal1=storeFrontHomePage.getSubTotalOfFirstProduct();
		//Remove product 2 from the cart and validate the subtotal
		storeFrontHomePage.removefirstProductFromTheCart();
		//validate product has been removed from the cart
		s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is "+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);
		double subtotal2=0;
		//validate sub-total is recalculated accordingly after removing product 2
		s_assert.assertTrue(storeFrontHomePage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		//Remove all the products from the cart
		storeFrontHomePage.removeFirstProductFromTheCart();
		//validate empty shopping cart page is displayed
		storeFrontHomePage.validateEmptyShoppingCartPageIsDisplayed();
		//click on continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLinkOnEmptyShoppingCartPage();
		//Select a product  and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		//update qty to 2 of the first product
		storeFrontHomePage.addQuantityOfProduct("2"); 
		//add another product in the cart
		storeFrontHomePage.addSecondProduct();

		logger.info("2 products are successfully added to the cart");
		//update qty to 2 of the second product
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("2"); 
		//Decrease quantities of both the product added to 1
		storeFrontHomePage.addQuantityOfProduct("1"); 
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("1"); 
		//validate the sub-total according to the updated quantities
		//get the sub-total of the first product
		subtotal1=storeFrontHomePage.getSubTotalOfFirstProduct();
		//get the sub-total of the second product
		subtotal2=storeFrontHomePage.getSubTotalOfSecondProduct();
		//validate sub-total is recalculated accordingly to the updated qty of product(s)
		s_assert.assertTrue(storeFrontHomePage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		storeFrontHomePage.removefirstProductFromTheCart();
		//add one more product to the shopping cart
		storeFrontHomePage.addSecondProduct();
		//re-validate the sub-total
		//get the sub-total of the first product
		subtotal1=storeFrontHomePage.getSubTotalOfFirstProduct();
		//get the sub-total of the second product
		subtotal2=storeFrontHomePage.getSubTotalOfSecondProduct();
		//validate sub-total is recalculated accordingly to the updated qty of product(s)
		s_assert.assertTrue(storeFrontHomePage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		//click on check-out button
		storeFrontHomePage.clickOnCheckoutButton();
		s_assert.assertAll(); 
	}

	// Hybris Project-138:Enroll and manage my autoships in Canada - PC
	@Test
	public void testEnrollAndManageMyAutoshipsInCanada_138() throws InterruptedException {
		if(driver.getCountry().equalsIgnoreCase("ca")){
			String previousOrderDate=null;
			String newOrderDate=null;
			country = driver.getCountry();
			RFO_DB = driver.getDBNameRFO();
			env = driver.getEnvironment();  
			List<Map<String, Object>> randomPCUserList =  null;

			String pcUserEmailID = null;
			String accountId = null;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			while(true){
				randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
				accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
				logger.info("Account Id of the user is "+accountId);

				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
				boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("error");
				if(isSiteNotFoundPresent){
					logger.info("Login Error for the user "+pcUserEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}	
			//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
			storeFrontPCUserPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.changeMainAddressToQuebec();
			storeFrontAccountInfoPage.clickOnSaveButtonAfterAddressChange();
			//s_assert.assertTrue(storeFrontAccountInfoPage.getMainAddressFromUI().equalsIgnoreCase(""),"Main address on ui is not selected address");
			s_assert.assertTrue(storeFrontAccountInfoPage.getAddressUpdateConfirmationMessageFromUI().equalsIgnoreCase("Your profile has been updated"),"Main address on ui is updated successfully");
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontShippingInfoPage=storeFrontAccountInfoPage.clickOnShippingInfoOnAccountInfoPage();
			storeFrontShippingInfoPage.clickOnEditForFirstAddress();
			storeFrontShippingInfoPage.changeMainAddressToQuebec();
			storeFrontShippingInfoPage.selectFirstCardNumber();
			storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontShippingInfoPage.clickOnSaveShippingProfile();
			s_assert.assertTrue(storeFrontShippingInfoPage.getAddressUpdateConfirmationMessageFromUI().equalsIgnoreCase("Your address has been updated."),"Shipping address on ui is updated successfully");
			storeFrontShippingInfoPage.clickOnEditForFirstAddress();
			storeFrontShippingInfoPage.changeAddressToUSAddress();
			//storeFrontShippingInfoPage.clickOnSaveShippingProfile();
			s_assert.assertTrue(storeFrontShippingInfoPage.getErrorMessageForUSAddressFromUI().equalsIgnoreCase("Please enter valid postal code."),"Accepting the us address for shipping info");
			storeFrontPCUserPage.clickOnWelcomeDropDown();
			storeFrontOrdersPage=storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
			previousOrderDate=storeFrontOrdersPage.getAutoshipOrderDate();
			storeFrontShippingInfoPage.clickOnYourAccountDropdown();
			storeFrontPCUserPage.clickOnPCPerksStatus();
			storeFrontPCUserPage.clickDelayOrCancelPCPerks();
			storeFrontPCUserPage.clickChangeMyAutoshipDateButton();
			storeFrontPCUserPage.selectSecondAutoshipDateAndClickSave();
			newOrderDate=storeFrontOrdersPage.getAutoshipOrderDate();
			s_assert.assertFalse(previousOrderDate.equalsIgnoreCase(newOrderDate),"unable to delay the pc perks");
			storeFrontShippingInfoPage.clickOnYourAccountDropdown();
			storeFrontPCUserPage.clickOnPCPerksStatus();
			storeFrontPCUserPage.clickDelayOrCancelPCPerks();
			storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
			storeFrontPCUserPage.cancelMyPCPerksAct();	
			s_assert.assertAll();

		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-4309 :: Version : 1 :: Soft-Terminated Consultant Cancel Reactivation
	@Test
	public void testTerminateConsultantAndCancelReactivation_4309() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName =  TestConstants.REGIMEN_NAME_REDEFINE;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();

		//Again enroll the consultant with same eamil id
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_CA;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_US;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();

		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);		
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");
		storeFrontHomePage.clickOnCnacelEnrollment();

		//verify that user is inactive
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Terminated User doesn't get Login failed");  
		s_assert.assertAll();
	}

	// Hybris Project-4318 :: Version : 1 :: Soft-Terminated PC Customer reactivates his PC account and perform Ad Hoc order
	@Test
	public void testTerminatePCReactivatePCAccountAndPerformAdhocOrder_4318() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnPcPerksStatus();
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickOnCancelPCPerks();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTerminationForPC();
		storeFrontAccountTerminationPage.clickOnConfirmAccountTermination();
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();

		// Enroll The PC Again
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

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

		storeFrontHomePage.enterEmailAddress(pcUserEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyReactiveYourPCAccountPopup(), "Reactivate Your PC account popup is not present");
		storeFrontHomePage.enterPasswordAfterTermination();
		storeFrontHomePage.clickOnLoginToReactiveMyAccount();

		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();

		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		logger.info("subtotal ="+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		logger.info("handlingCharges ="+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		logger.info("tax ="+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		logger.info("total ="+total);
		//String totalSV = storeFrontUpdateCartPage.getTotalSV();
		//logger.info("totalSV ="+totalSV);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		logger.info("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		logger.info("BillingAddress ="+BillingAddress);

		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		//String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");

		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		//storeFrontOrdersPage.clickOrderNumber(orderNumber);
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subtotal),"Adhoc Order template subtotal "+subtotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"Adhoc Order template tax "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(total),"Adhoc Order template grand total "+total+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCharges),"Adhoc Order template handling amount "+handlingCharges+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		s_assert.assertTrue(shippingMethod.contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()),"Adhoc Order template shipping method "+shippingMethod+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

		s_assert.assertAll();

	}

	//Hybris Project-4319:Soft-Terminated PC Cancel Reactivation
	@Test
	public void testTerminatePCAndCancelReactivation_4319() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnPcPerksStatus();
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickOnCancelPCPerks();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTerminationForPC();
		storeFrontAccountTerminationPage.clickOnConfirmAccountTermination();
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();

		// Enroll The PC Again
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

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

		storeFrontHomePage.enterEmailAddress(pcUserEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyReactiveYourPCAccountPopup(), "Reactivate Your PC account popup is not present");
		storeFrontHomePage.clickOnCnacelEnrollmentForPC();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		//verify that user is inactive
		storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Terminated User doesn't get Login failed");  
		s_assert.assertAll();
	}

	// Hybris Project-2233:Verify that user can cancel Pulse subscription through my account.
	@Test
	public void testVerifyUserCanCancelPulseSubscriptionThroughMyAccount_2233() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO();  
		List<Map<String, Object>> randomConsultantList =  null;
		//List<Map<String, Object>> randomConsultantPWSList =  null;
		String consultantWithPWSEmailID = null;
		String consultantPWSURL = null;
		String env = driver.getEnvironment();
		String country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Get Consultant with PWS from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,env,country),RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");

		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		s_assert.assertTrue(!consultantPWSURL.contains(".biz"),"Consultant is not on her own .com PWS");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		//Verify  user can cancel Pulse subscription through my account.
		storeFrontAccountInfoPage.cancelPulseSubscription();
		s_assert.assertTrue(storeFrontAccountInfoPage.validatePulseCancelled(),"pulse subscription is not cancelled for the user");
		s_assert.assertAll();

	}

	//Hybris Project-2232:Verify that user can cancel CRP subscription through my account.
	@Test
	public void testVerifyUserCanCancelCRPSubscriptionThroughMyAccount_2232() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
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
		storeFrontAccountInfoPage.clickOnCancelMyCRP();
		//validate CRP has been cancelled..
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");
		s_assert.assertAll();
	}

	// Hybris Project-2134:EC-789- To Verify subscribe to pulse
	@Test
	public void testVerifySubscribeToPulse_2134() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO();  
		List<Map<String, Object>> randomConsultantList =  null;
		//List<Map<String, Object>> randomConsultantPWSList =  null;
		String consultantWithPWSEmailID = null;
		String consultantPWSURL = null;
		String env = driver.getEnvironment();
		String country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Get Consultant with PWS from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,env,country),RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");

		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		//Verify subscribe to pulse(pulse already subscribed)
		s_assert.assertTrue(storeFrontAccountInfoPage.validateSubscribeToPulse(),"pulse is not subscribed for the user");
		s_assert.assertAll();
	}

	//Hybris Project-3856 :: Version : 1 :: Register as RC WITHOUT creating an ORDER
	@Test
	public void testRegisterAsRcWithoutCreatingAnOrder_3856() throws InterruptedException{
		int randomNum =  CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		country = driver.getCountry();
		env = driver.getEnvironment();
		String lastName = TestConstants.LAST_NAME+randomNum;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, env);
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.selectProductAndProceedToBuy();
		storeFrontHomePage.clickOnPlaceOrderButton();
		storeFrontHomePage.enterNewRCDetails(firstName, lastName, password);
		storeFrontHomePage.enterMainAccountInfo();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(),"Order is not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		s_assert.assertAll();

	}


	//Hybris Project-3619 :: Version : 1 :: CCS CA consultant Express Enrollment for Yukon province with US sponsor
	@Test
	public void ccsCAConsultantExpressEnrollmentWithUSSponsor_3619() throws InterruptedException	{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			country = driver.getCountry();
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REVERSE;
			String firstName = TestConstants.FIRST_NAME+randomNum;
			String CCS = null;
			RFO_DB = driver.getDBNameRFO(); 
			List<Map<String, Object>> sponsorIdList =  null;

			storeFrontHomePage = new StoreFrontHomePage(driver);

			kitName = TestConstants.KIT_NAME_EXPRESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SPONSOR_ID,countryId),RFO_DB);
			CCS = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID(CCS);
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, TestConstants.PROVINCE_YUKON, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
			//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
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
			storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
			storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
			storeFrontHomePage.clickOnConfirmAutomaticPayment();
			s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}



	//Hybris Project-4660 :: Version : 1 :: Change the username of RC user and Login with updated username
	@Test
	public void testchangeUsernameOfRcUserWithUpdatedUserName_4660() throws InterruptedException{
		int randomNumber =  CommonUtils.getRandomNum(10000, 1000000);
		String newUserName = TestConstants.NEW_RC_USER_NAME+randomNumber;
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "Username");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID,password);
		String oldUserNameOnUI = storeFrontHomePage.fetchingUserName();
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.enterNewUserNameAndClicKOnSaveButton(newUserName);
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Your Profile has not been Updated");
		logout();
		storeFrontHomePage.loginAsRCUser(newUserName, password);
		String newUserNameOnUI = storeFrontHomePage.fetchingUserName();
		s_assert.assertTrue(storeFrontHomePage.verifyUserNameAfterLoginAgain(oldUserNameOnUI,newUserNameOnUI),"Login is not successful with new UserName");
		s_assert.assertAll();
	}

	//Hybris Project-1289:3. Terms and Conditions - Standard Enrollment only Pulse
	@Test
	public void testTermsAndConditionsForConsultantStandardEnrollmentForPulse_1289() throws InterruptedException	{
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
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, profileName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//At autoship options page check only the pulse check box and proceed..
		storeFrontHomePage.uncheckCRPCheckBox();
		storeFrontHomePage.clickEnrollmentNextBtn();
		//validate that 'I have read and accepted all Terms and Conditions for the Consultant Application, Pulse and Policies and Procedures' is displayed
		s_assert.assertTrue(storeFrontHomePage.validateTermsAndConditionsForConsultantApplicationPulse(), "' I have read and accepted all Terms and Conditions for the Consultant Application, Pulse and Policies and Procedures.' is not present"); 
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		s_assert.assertAll();
	}

	// Hybris Project-1290:4. Terms and Conditions- Standard Enrollment only CRP
	@Test
	public void testTermsAndConditionsForConsultantStandardEnrollmentForCRP_1290() throws InterruptedException	 {
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
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, profileName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//At autoship options page check only the CRP check box and proceed..
		storeFrontHomePage.uncheckPulseCheckBox(); 
		storeFrontHomePage.clickEnrollmentNextBtn();
		//Add a product to CRP..
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickNextOnCRPCartPage();
		//validate that 'I have read and accepted all Terms and Conditions for the Consultant Application, CRP and Policies and Procedures' is displayed
		s_assert.assertTrue(storeFrontHomePage.validateTermsAndConditionsForConsultantApplicationCRP(), "' I have read and accepted all Terms and Conditions for the Consultant Application, CRP and Policies and Procedures.' is not present"); 
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		s_assert.assertAll();
	}

	//Hybris Project-4308 :: Version : 1 :: Soft-Terminated Consultant reactivates his account and perform Ad Hoc order 
	@Test
	public void testTerminateConsultantAndReactivateAndPerformAdhocOrder_4308() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		String lastName = "lN";
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName =  TestConstants.REGIMEN_NAME_REDEFINE;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		//Again enroll the consultant with same eamil id
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_CA;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_US;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);		
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");
		storeFrontHomePage.clickOnEnrollUnderLastUpline();
		// For enroll the same consultant
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);		
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.enterPasswordForReactivationForConsultant();
		storeFrontHomePage.clickOnLoginToReactiveMyAccountForConsultant();

		// Create Adhoc Order For Same Consultant
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		StoreFrontUpdateCartPage storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		//s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		//storeFrontUpdateCartPage.clickOnConfirmationOK();

		String subtotal = String.valueOf(storeFrontUpdateCartPage.getSubtotal());
		logger.info("subtotal ="+subtotal);
		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		String tax = String.valueOf(storeFrontUpdateCartPage.getTax());
		logger.info("tax ="+tax);
		String total = String.valueOf(storeFrontUpdateCartPage.getTotal());
		logger.info("total ="+total);
		String totalSV = String.valueOf(storeFrontUpdateCartPage.getTotalSV());
		logger.info("totalSV ="+totalSV);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		logger.info("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		logger.info("BillingAddress ="+BillingAddress);

		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		//String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subtotal),"Adhoc Order template subtotal "+subtotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"Adhoc Order template tax "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(total),"Adhoc Order template grand total "+total+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCharges),"Adhoc Order template handling amount "+handlingCharges+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		s_assert.assertTrue(shippingMethod.contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()),"Adhoc Order template shipping method "+shippingMethod+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		s_assert.assertTrue(totalSV.contains(storeFrontOrdersPage.getTotalSVValue()), "Adhoc order template total sv value "+totalSV+"and on UI is "+storeFrontOrdersPage.getTotalSVValue());
		s_assert.assertAll();
	}

	//Hybris Project-4310 :: Version : 1 :: Soft-Terminate Consultant is not available for Sponsor's search
	@Test
	public void testTerminatedConsultantIsNotAvailableAsSponsor_4310() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName =  TestConstants.REGIMEN_NAME_REDEFINE;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		String CCS = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			// Get Account Number
			List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
			CCS = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();

		// search consultant as sponsor
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID(CCS);
		s_assert.assertTrue(storeFrontHomePage.verifyTerminatedConsultantIsNotInSponsorList(), "Terminated Consultant is present in sponsor's list");
		s_assert.assertAll();
	}

	//Hybris Project-4311 :: Version : 1 :: Reactivated Soft-Terminated Consultant should be in the search for Sponsor list
	@Test
	public void ReactivateTerminatedConsultantAndSearchInSponsorList_4311() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName =  TestConstants.REGIMEN_NAME_REDEFINE;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		String  CCS = null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			// Get Account Number
			List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
			CCS = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
			logger.info("Account number of the consultant is "+CCS);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();

		//Again enroll the consultant with same eamil id
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_CA;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_US;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);		
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");
		storeFrontHomePage.clickOnEnrollUnderLastUpline();

		// For enroll the same consultant
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);		
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.enterPasswordForReactivationForConsultant();
		storeFrontHomePage.clickOnLoginToReactiveMyAccountForConsultant();
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());
		// search the same consultant in sponsor's list 
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID(CCS);
		s_assert.assertTrue(storeFrontHomePage.verifyTerminatedConsultantPresentInSponsorList(), "Terminated Consultant is not present in sponsor's list");
		s_assert.assertAll();
	}

	// Hybris Project-4775 :: Version : 1 :: Consultant to PC downgrade and PC to Consultant for same User for US
	@Test
	public void testConsultantToPCDowngradeANdPCToConsultantForSameForUS_4775() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("us")){
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			RFO_DB = driver.getDBNameRFO();	
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			//List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;	
			//String accountID = null;
			String lastName = "lN";
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName =  TestConstants.REGIMEN_NAME_REVERSE;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			country = driver.getCountry();
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
			String firstName=TestConstants.FIRST_NAME+randomNum;

			// Enroll new consultant--

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;

			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
			consultantEmailID = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
			storeFrontHomePage.enterEmailAddress(consultantEmailID);
			storeFrontHomePage.clickEnrollmentNextBtn();
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
			storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
			storeFrontHomePage.clickOnEnrollMeBtn();
			storeFrontHomePage.clickOnConfirmAutomaticPayment();
			s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
			storeFrontHomePage.clickOnRodanAndFieldsLogo();

			//Deactivate the newly created consultant
			storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
			storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();

			// Enroll the PC Again
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();

			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");

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

			storeFrontHomePage.enterEmailAddress(consultantEmailID);
			s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");
			storeFrontHomePage.clickOnEnrollUnderLastUpline();

			// Enroll Deactivated Consultant as PC

			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();

			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");

			storeFrontHomePage.selectProductAndProceedToBuy();

			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();
			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");

			//storeFrontHomePage.enterEmailAddress(consultantEmailID);
			storeFrontHomePage.enterNewPCDetails(firstName, lastName, password, consultantEmailID);

			//Click on Check out
			//storeFrontHomePage.clickOnCheckoutButton();

			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");

			//storeFrontHomePage.clickOnContinueWithoutSponsorLink();
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
			storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
			storeFrontHomePage.clickPlaceOrderBtn();
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

			// Upgrade the PC as Consultant
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);		
			storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
			storeFrontHomePage.enterEmailAddress(consultantEmailID);
			s_assert.assertTrue(storeFrontHomePage.verifyUpradingToConsulTantPopup(), "Upgrading to a consultant popup is not present");
			storeFrontHomePage.enterPasswordForUpgradePcToConsultant();
			storeFrontHomePage.clickOnLoginToTerminateToMyPCAccount();
			storeFrontHomePage.enterFirstName(firstName);
			storeFrontHomePage.enterLastName(lastName);
			storeFrontHomePage.enterEmailAddress(consultantEmailID);
			storeFrontHomePage.enterPassword(password);
			storeFrontHomePage.enterConfirmPassword(password);
			storeFrontHomePage.enterAddressLine1(addressLine1);
			storeFrontHomePage.enterCity(city);
			storeFrontHomePage.selectProvince();
			storeFrontHomePage.enterPostalCode(postalCode);
			storeFrontHomePage.enterPhoneNumber(phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
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
			storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
			storeFrontHomePage.clickOnEnrollMeBtn();
			storeFrontHomePage.clickOnConfirmAutomaticPayment();
			s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
			storeFrontHomePage.clickOnRodanAndFieldsLogo();

			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();

			// Verify Order is present
			s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderIsPresent(), "Adhoc Order is not present");
			s_assert.assertAll();

		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for US env");
		}
	}

	//Hybris Project-4821:Consultant To RC downgrade US
	@Test
	public void testConsultantToRCDowngrade_4821() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("us")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			String  consultantEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
			kitName = TestConstants.KIT_NAME_PERSONAL;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME,consultantEmailAddress, password, addressLine1, city, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
			//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
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

			//Terminate consultant Account
			storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationPageIsDisplayed(),"Account Termination Page has not been displayed");
			storeFrontAccountTerminationPage.selectTerminationReason();
			storeFrontAccountTerminationPage.enterTerminationComments();
			storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
			storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is Present");
			storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();
			storeFrontHomePage.loginAsConsultant(consultantEmailAddress, password);
			s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");

			//navigate to US Corp Site And start enrolling RC user.
			driver.get(driver.getURL()+"/"+driver.getCountry());
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = "lN";
			storeFrontHomePage = new StoreFrontHomePage(driver);
			// Click on our product link that is located at the top of the page and then click in on quick shop
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");
			//Select a product and proceed to buy it
			storeFrontHomePage.selectProductAndProceedToBuy();
			//Cart page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
			logger.info("Cart page is displayed");
			//one products are in the Shopping Cart?
			s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
			logger.info("1 product is successfully added to the cart");
			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();
			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");
			//Enter the email address and DO NOT check the "Become a Preferred Customer" checkbox 
			storeFrontHomePage.enterEmailAddress(consultantEmailAddress);
			//Assert for enroll under last upline popup
			s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");
			//Handle popup for enroll under last upline
			storeFrontHomePage.clickOnEnrollUnderLastUpline();
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");
			//Select a product and proceed to buy it
			storeFrontHomePage.selectProductAndProceedToBuy();
			//Cart page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
			logger.info("Cart page is displayed");
			//one products are in the Shopping Cart?
			s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("2"), "number of products in the cart is NOT 1");
			logger.info("1 product is successfully added to the cart");
			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();
			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");
			//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
			storeFrontHomePage.enterNewRCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME,consultantEmailAddress, password);
			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");
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
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			logout();
			//check login with enrolled RC user
			driver.get(driver.getURL()+"/"+driver.getCountry());
			storeFrontRCUserPage=storeFrontHomePage.loginAsRCUser(consultantEmailAddress, password);
			s_assert.assertFalse(storeFrontHomePage.isCurrentURLShowsError(),"Downgraded RC not able to login");  
			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for US env");
		}

	}

	// Hybris Project-4822:Consultant to RC downgrade Canada
	@Test
	public void testConsultantToRCDowngrade_4822() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			String  consultantEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
			kitName = TestConstants.KIT_NAME_PERSONAL;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();

			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME,consultantEmailAddress, password, addressLine1, city,TestConstants.PROVINCE_ALBERTA, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
			//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
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

			//Terminate consultant Account
			storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationPageIsDisplayed(),"Account Termination Page has not been displayed");
			storeFrontAccountTerminationPage.selectTerminationReason();
			storeFrontAccountTerminationPage.enterTerminationComments();
			storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
			storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is Present");
			storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();
			storeFrontHomePage.loginAsConsultant(consultantEmailAddress, password);
			s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");

			//navigate to Canada Corp Site And start enrolling RC user.
			driver.get(driver.getURL()+"/"+driver.getCountry());
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = "lN";
			storeFrontHomePage = new StoreFrontHomePage(driver);
			// Click on our product link that is located at the top of the page and then click in on quick shop
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");

			//Select a product and proceed to buy it
			storeFrontHomePage.selectProductAndProceedToBuy();

			//Cart page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
			logger.info("Cart page is displayed");

			//one products are in the Shopping Cart?
			s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
			logger.info("1 product is successfully added to the cart");

			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();

			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");

			//Enter the email address and DO NOT check the "Become a Preferred Customer" checkbox 
			storeFrontHomePage.enterEmailAddress(consultantEmailAddress);

			//Assert for enroll under last upline popup
			s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");

			//Handle popup for enroll under last upline
			storeFrontHomePage.clickOnEnrollUnderLastUpline();

			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");

			//Select a product and proceed to buy it
			storeFrontHomePage.selectProductAndProceedToBuy();

			//Cart page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
			logger.info("Cart page is displayed");

			//one products are in the Shopping Cart?
			s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("2"), "number of products in the cart is NOT 1");
			logger.info("1 product is successfully added to the cart");

			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();

			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");

			//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
			storeFrontHomePage.enterNewRCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME,consultantEmailAddress, password);
			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");

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
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			logout();

			//check login with enrolled RC user
			driver.get(driver.getURL()+"/"+driver.getCountry());
			storeFrontRCUserPage=storeFrontHomePage.loginAsRCUser(consultantEmailAddress, password);
			s_assert.assertFalse(storeFrontHomePage.isCurrentURLShowsError(),"unable to login successfully");
			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}

	}

	//Hybris Project-4777:Consultant to RC downgrade(Cross Country)
	@Test 
	public void testConsultantToRCDowngrade_4777() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			String  consultantEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
			kitName = TestConstants.KIT_NAME_PERSONAL;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();

			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME,consultantEmailAddress, password, addressLine1, city,TestConstants.PROVINCE_ALBERTA, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
			//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
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

			//Terminate consultant Account
			storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationPageIsDisplayed(),"Account Termination Page has not been displayed");
			storeFrontAccountTerminationPage.selectTerminationReason();
			storeFrontAccountTerminationPage.enterTerminationComments();
			storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
			storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is Present");
			storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();


			//navigate to US Corp Site And start enrolling RC user.
			storeFrontHomePage.navigateToCountry();
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = "lN";
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			String province = TestConstants.PROVINCE_ALABAMA_US;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			// Click on our product link that is located at the top of the page and then click in on quick shop
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");

			//Select a product and proceed to buy it
			storeFrontHomePage.clickAddToBagButton();

			//Cart page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
			logger.info("Cart page is displayed");

			//one products are in the Shopping Cart?
			s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
			logger.info("1 product is successfully added to the cart");

			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();

			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");

			//Enter the email address and DO NOT check the "Become a Preferred Customer" checkbox 
			storeFrontHomePage.enterEmailAddress(consultantEmailAddress);

			//Assert for enroll under last upline popup
			s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");

			//Handle popup for enroll under last upline
			storeFrontHomePage.clickOnEnrollUnderLastUpline();

			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");

			//Select a product and proceed to buy it
			storeFrontHomePage.clickAddToBagButton();

			//Cart page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
			logger.info("Cart page is displayed");

			//Two products are in the Shopping Cart?
			s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("2"), "number of products in the cart is NOT 1");
			logger.info("1 product is successfully added to the cart");

			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();

			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");

			//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
			storeFrontHomePage.enterNewRCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME,consultantEmailAddress, password);
			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo(addressLine1,city,province,postalCode,phoneNumber);
			logger.info("Main account details entered");

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
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			storeFrontHomePage.navigateToCountry();
			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}

	}

	//Hybris Project-1747:To verify edit shipping address functionality in the CRP Edit Autoship template Page
	@Test
	public void testEditShippingFunctionalityAtEditAutoshipTemplatePage_1747() throws InterruptedException{
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		//List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null; 
		//String accountID = null;
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName =  TestConstants.REGIMEN_NAME_REVERSE;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		String firstName=TestConstants.FIRST_NAME+randomNum;

		// Enroll new consultant--
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_CA;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_US;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		consultantEmailID = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.clickOnReviewAndConfirmShippingEditBtn();

		// assert the fields that are prepopulated or not

		s_assert.assertTrue(storeFrontHomePage.isFirstNamePrepopulated(), "First name is not prepopulated");
		s_assert.assertTrue(storeFrontHomePage.isLastNamePrepopulated(), "Last name is not prepopulated");
		s_assert.assertTrue(storeFrontHomePage.isEmailAddressPrepopulated(), "Email Address is not prepopulated");
		s_assert.assertTrue(storeFrontHomePage.isAddressLine1Prepopulated(), "Address line 1 is not prepopulated");
		s_assert.assertTrue(storeFrontHomePage.isCityPrepopulated(), "City is not prepopulated");
		s_assert.assertTrue(storeFrontHomePage.isSelectProvincePrepopulated(), "Province is not prepopulated");
		s_assert.assertTrue(storeFrontHomePage.isEnterPostalCodePrepopulated(), "Postal Code is not prepopulated");
		s_assert.assertTrue(storeFrontHomePage.isPhoneNumberPrepopulated(), "Phone number is not prepopulated");

		storeFrontHomePage.enterPassword(password);
		storeFrontHomePage.enterConfirmPassword(password);
		storeFrontHomePage.enterAddressLine1(TestConstants.NEW_ADDRESS_LINE_1_US);
		storeFrontHomePage.clickEnrollmentNextBtnWithoutClickOnUseAsEnteredAddress();
		storeFrontHomePage.clickOnCrossIconForAddressPopup();

		// clear the any mandatory fiela for assertion
		storeFrontHomePage.clearAddressLine1();
		storeFrontHomePage.clickEnrollmentNextBtnWithoutClickOnUseAsEnteredAddress();
		s_assert.assertTrue(storeFrontHomePage.verifyEnterValueForMandatoryFieldPopup(), "Enter value for mandatory field popup is not present");
		storeFrontHomePage.enterAddressLine1(TestConstants.NEW_ADDRESS_LINE_1_US);
		storeFrontHomePage.clickEnrollmentNextBtn();

		// On Billing Address Page verify assertion for mandatory fields
		storeFrontHomePage.clickOnReviewAndConfirmBillingEditBtn();
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isEnterNameOnCardPrepopulated(), "Name on Card is not prepopulated");
		s_assert.assertTrue(storeFrontHomePage.isEnterCardNumberPrepopulated(), "Card Number is not prepopulated");  
		s_assert.assertTrue(storeFrontHomePage.verifyEnterValueForMandatoryFieldPopup(), "Enter value for mandatory field popup is not present");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");

		s_assert.assertAll(); 
	}

	//Hybris Phase 2-1981:Orders page UI for RC
	@Test
	public void testOrdersPageUIForRCUser_HP2_1981() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> orderStatusList =  null;
		List<Map<String, Object>> orderGrandTotalList =  null;
		List<Map<String, Object>> orderDateList =  null;
		List<Map<String, Object>> randomRCList =  null;
		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;
		String rcUserEmailAddress = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_HAVING_ACTIVE_ORDER_RFO,countryId),RFO_DB);
		rcUserEmailAddress = (String) getValueFromQueryResult(randomRCList, "Username");

		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailAddress, password);
		//s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserEmailAddress),"RC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();

		// Get Order Id
		List<Map<String,Object>> getOrderIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(getOrderIDList, "OrderID"));

		/*		//assert for order number with RFO
		  orderNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO,  rcUserEmailAddress),RFO_DB);
		  orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
		  logger.info("Order Number from RFO DB is "+orderNumberDB);
		  s_assert.assertTrue(storeFrontOrdersPage.verifyOrderNumber(orderNumberDB),"Order Number on UI is different from RFO DB");
		 */
		//assert for order status with RFO
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFO, rcUserEmailAddress),RFO_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		logger.info("Order Status from RFO DB is "+orderStatusDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrderStatus(orderStatusDB),"Order Status on UI is different from RFO DB");

		//assert for grand total with RFO
		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4287_RFO, orderId),RFO_DB);
		DecimalFormat dff = new DecimalFormat("#.00");
		orderGrandTotalDB = String.valueOf(dff.format(getValueFromQueryResult(orderGrandTotalList, "Total"))); 
		logger.info("Order GrandTotal from RFO DB is "+orderGrandTotalDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),"Grand total on UI is different from RFO DB");

		//assert for order date with RFO
		orderDateList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFO, rcUserEmailAddress),RFO_DB);
		orderDateDB = String.valueOf (getValueFromQueryResult(orderDateList, "CompletionDate"));
		logger.info("Order Scheduled Date from RFO DB is "+orderDateDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyScheduleDate(orderDateDB),"Scheduled date on UI is different from RFO DB");

		s_assert.assertAll();  
	}

	//Hybris Project-2142:Check Shipping and Handling Fee for UPS Ground for Order total 0-999999
	@Test
	public void testCheckShippingAndHandlingFeeForUPSGround_2142() throws InterruptedException	{
		if(driver.getCountry().equalsIgnoreCase("ca")){			
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;			
			String accountId = null;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);

			while(true){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
				accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				logger.info("Account Id of the user is "+accountId);
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
				boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
				if(isSiteNotFoundPresent){
					logger.info("SITE NOT FOUND for the user "+consultantEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}
			s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
			logger.info("login is successful");
			storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
			storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
			storeFrontUpdateCartPage.clickOnCheckoutButton();

			storeFrontUpdateCartPage.selectShippingMethodUPSGroundInOrderSummary();

			double subtotal = storeFrontUpdateCartPage.getSubtotalValue();
			logger.info("subtotal ="+subtotal);
			String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
			logger.info("deliveryCharges ="+deliveryCharges);
			String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
			logger.info("handlingCharges ="+handlingCharges);

			if(subtotal<=999999){
				//Assert  shipping cost from UI
				s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 15.00"),"Shipping charges on UI is not As per shipping method selected");
				//Handling charges
				s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");

			}else{
				logger.info(" Order total is not in required range");
			}
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-4312:Soft-Terminated Consultant becomes a PC
	@Test
	public void testSoftTerminatedConsultantBecomesAPC_4312() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 

		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		country=driver.getCountry();

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");

		//Terminate the Consultant..
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		//s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Terminated User doesn't get Login failed");  
		//click RF logo
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();

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

		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");
		//click Enroll under last upline..
		storeFrontHomePage.clickOnEnrollUnderLastUpline();

		// Enroll Deactivated Consultant as PC
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();

		storeFrontHomePage.selectProductAndProceedToBuy();
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		storeFrontHomePage.enterNewPCDetails(firstName, lastName, password,consultantEmailID);

		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		/*  storeFrontHomePage.clickOnNotYourSponsorLink();
	   storeFrontHomePage.clickOnContinueWithoutSponsorLink();*/
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
		//storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
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
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, firstName,TestConstants.LAST_NAME, emailAddress, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		comPWS = storeFrontHomePage.getDotComPWS();
		bizPWS = storeFrontHomePage.getDotBizPWS();
		storeFrontHomePage.clickEnrollmentNextBtn();
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
		logout();
		//login with .biz pws of consultant
		driver.get(bizPWS);
		storeFrontConsultantPage=storeFrontHomePage.loginAsConsultant(emailAddress,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Unable to login with .biz pws of consultant");
		//Cancell the pulse of enrolled consultant
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontOrdersAutoshipStatusPage=storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.cancelPulseSubscription();
		s_assert.assertTrue(storeFrontAccountInfoPage.validatePulseCancelled(),"pulse has not been cancelled for consultant");
		logout();
		//Retry to login on .com pws of consultant after cancelled Paid pulse and check login successful
		driver.get(comPWS);
		storeFrontConsultantPage=storeFrontHomePage.loginAsConsultant(emailAddress,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Unable to login with .com pws of consultant");
		logout();
		//Retry to login in to .biz pws of consultant after cancelled Paid pulse and check login is successful
		driver.get(bizPWS);
		storeFrontConsultantPage=storeFrontHomePage.loginAsConsultant(emailAddress,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Unable to login with .biz pws of consultant");
		logout();

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
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName,TestConstants.LAST_NAME, emailAddress, password, addressLine1, city,TestConstants.PROVINCE_ALABAMA_US, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		comPWS = storeFrontHomePage.getDotComPWS();
		bizPWS = storeFrontHomePage.getDotBizPWS();
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
		logout();
		//login with .biz pws of consultant
		driver.get(bizPWS);
		storeFrontConsultantPage=storeFrontHomePage.loginAsConsultant(emailAddress,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Unable to login with .biz pws of consultant");
	}

	//Hybris Project-2234:Verify that user can cancel PC Perks subscription through my account
	@Test
	public void testPCUserCancelPcPerks_2234() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);		
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		emailID = firstName+randomNum+"@xyz.com";
		// Click on our product link that is located at the top of the page and then click in on quick shop

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

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

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password,emailID);

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
		/*storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");*/
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontPCUserPage=new StoreFrontPCUserPage(driver);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontPCUserPage.clickOnYourAccountDropdown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
		storeFrontPCUserPage.cancelMyPCPerksAct();
		storeFrontHomePage.loginAsConsultant(emailID, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();
	}

	//Hybris Phase 2-2235:Verify that user can change the information in 'my account info'.
	@Test
	public void testAccountInformationForUpdate_2235() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> accountNameDetailsList = null;
		List<Map<String, Object>> accountAddressDetailsList = null;
		List<Map<String, Object>> randomConsultantList =  null;
		String firstNameDB = null;
		String lastNameDB = null;
		String genderDB = null;
		String cityDB = null;
		String provinceDB = null;
		String postalCodeDB = null;
		String dobDB = null;
		String country = null;
		//String stateDB = null;

		String consultantEmailID = null;
		String accountID = null;
		String city = null;
		String postalCode = null;
		String phoneNumber = null;

		country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			city = TestConstants.CONSULTANT_CITY_FOR_ACCOUNT_INFORMATION_CA;
			postalCode = TestConstants.CONSULTANT_POSTAL_CODE_FOR_ACCOUNT_INFORMATION_CA;
			phoneNumber = TestConstants.CONSULTANT_MAIN_PHONE_NUMBER_FOR_ACCOUNT_INFORMATION_CA;
		}else{
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		logger.info("Account Id of the user is "+accountID);

		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(), "Account Info page has not been displayed");
		storeFrontAccountInfoPage.updateAccountInformation(TestConstants.CONSULTANT_FIRST_NAME_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_LAST_NAME_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_ADDRESS_LINE_1_FOR_ACCOUNT_INFORMATION,city,postalCode,phoneNumber);
		//storeFrontAccountInfoPage.handleSpouseDetail();

		//assert First Name with RFO
		accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, consultantEmailID), RFO_DB);
		firstNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "FirstName");
		assertTrue("First Name on UI is different from DB", storeFrontAccountInfoPage.verifyFirstNameFromUIForAccountInfo(firstNameDB));

		// assert Last Name with RFO
		accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, consultantEmailID), RFO_DB);
		lastNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "LastName");
		assertTrue("Last Name on UI is different from DB", storeFrontAccountInfoPage.verifyLasttNameFromUIForAccountInfo(lastNameDB) );

		// assert Address Line 1 with RFO
		//		accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, consultantEmailID), RFO_DB);
		//		addressLine1DB = (String) getValueFromQueryResult(accountAddressDetailsList, "AddressLine1");
		//		assertTrue("Address Line 1 on UI is different from DB", storeFrontAccountInfoPage.verifyAddressLine1FromUIForAccountInfo(addressLine1DB));


		// assert City with RFO
		accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, consultantEmailID), RFO_DB);
		cityDB = (String) getValueFromQueryResult(accountAddressDetailsList, "Locale");
		assertTrue("City on UI is different from DB", storeFrontAccountInfoPage.verifyCityFromUIForAccountInfo(cityDB));

		// assert State with RFO
		accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, consultantEmailID), RFO_DB);
		provinceDB = (String) getValueFromQueryResult(accountAddressDetailsList, "Region");
		/*if(provinceFromDB.equalsIgnoreCase("TX")){
		    provinceDB = "Texas"; 
		   }*/
		assertTrue("Province on UI is different from DB", storeFrontAccountInfoPage.verifyProvinceFromUIForAccountInfo(provinceDB));

		//assert Postal Code with RFO
		accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, consultantEmailID), RFO_DB);
		postalCodeDB = (String) getValueFromQueryResult(accountAddressDetailsList, "PostalCode");
		assertTrue("Postal Code on UI is different from DB", storeFrontAccountInfoPage.verifyPostalCodeFromUIForAccountInfo(postalCodeDB));

		// assert Main Phone Number with RFO
		//		mainPhoneNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_PHONE_NUMBER_QUERY_RFO, consultantEmailID), RFO_DB);
		//		mainPhoneNumberDB = (String) getValueFromQueryResult(mainPhoneNumberList, "PhoneNumberRaw");
		//		assertTrue("Main Phone Number on UI is different from DB", storeFrontAccountInfoPage.verifyMainPhoneNumberFromUIForAccountInfo(mainPhoneNumberDB));

		// assert Gender Id with RFO
		accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, consultantEmailID), RFO_DB);
		genderDB = String.valueOf(getValueFromQueryResult(accountNameDetailsList, "GenderId"));
		if(genderDB.equals("2")){
			genderDB = "male";
		}
		else{
			genderDB = "female";
		}
		assertTrue("Gender on UI is different from DB", storeFrontAccountInfoPage.verifyGenderFromUIAccountInfo(genderDB));

		// assert BirthDay with RFO
		accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, consultantEmailID), RFO_DB);
		dobDB = String.valueOf(getValueFromQueryResult(accountNameDetailsList, "BirthDay"));
		assertTrue("DOB on UI is different from DB", storeFrontAccountInfoPage.verifyBirthDateFromUIAccountInfo(dobDB));  

		s_assert.assertAll();
	}

	//Hybris Project-1979:Orders page UI for PC and edit cart
	@Test
	public void testOrderPageUIAndEditCartForPC_1979() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		List<Map<String, Object>> orderStatusList =  null;
		List<Map<String, Object>> orderGrandTotalList =  null;
		List<Map<String, Object>> orderDateList =  null;
		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotal = null;
		String subTotal = null;
		String shipping = null;
		String handling = null;
		String tax = null; 
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String lastName = null;
		String orderId = null;
		String accountId = null;
		String shippingMethodId =null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
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
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		// Get Order Id
		List<Map<String,Object>> getOrderIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderIDList, "OrderID"));
		//assert for order status with RFO
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFO, pcUserEmailID),RFO_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		logger.info("Order Status from RFO DB is "+orderStatusDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrderStatus(orderStatusDB),"Order Status on UI is different from RFO DB");
		//assert for grand total with RFO
		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4287_RFO, orderId),RFO_DB);
		DecimalFormat dff = new DecimalFormat("#.00");
		orderGrandTotalDB = String.valueOf(dff.format(getValueFromQueryResult(orderGrandTotalList, "Total"))); 
		logger.info("Order GrandTotal from RFO DB is "+orderGrandTotalDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),"Grand total on UI is different from RFO DB");

		//assert for order date with RFO
		orderDateList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFO, pcUserEmailID),RFO_DB);
		orderDateDB = String.valueOf (getValueFromQueryResult(orderDateList, "CompletionDate"));
		logger.info("Order Scheduled Date from RFO DB is "+orderDateDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyScheduleDate(orderDateDB),"Scheduled date on UI is different from RFO DB");

		// click on edit
		storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate();
		storeFrontHomePage.navigateToBackPage();
		storeFrontOrdersPage.clickOnEditAtAutoshipTemplate();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		String quantityBeforeUpdate = storeFrontUpdateCartPage.getQuantityOfProductOnCartPage();
		String updatedQuantity = storeFrontUpdateCartPage.upgradeQuantityOfProduct(quantityBeforeUpdate);
		storeFrontHomePage.addQuantityOfProduct(updatedQuantity);

		// assert update quantity on cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.getQuantityOfProductOnCartPage().contains(updatedQuantity),"CRP autoship template subTotal on RFO is "+updatedQuantity+" and on UI is "+storeFrontUpdateCartPage.getQuantityOfProductOnCartPage());

		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();

		// values for assertion for pending order autoship template
		subTotal = storeFrontUpdateCartPage.getSubtotalFromCart();
		shipping = storeFrontUpdateCartPage.getDeliveyFromCart();
		handling = storeFrontUpdateCartPage.getHandlingFromCart();
		tax = storeFrontUpdateCartPage.getTaxFromCart();
		grandTotal = storeFrontUpdateCartPage.getGrandTotalFromCart();

		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		// assert update quantity on autoship template 
		s_assert.assertTrue(storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate().contains(updatedQuantity),"CRP autoship template subTotal on RFO is "+updatedQuantity+" and on UI is "+storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate());

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotal),"PC autoship cart subTotal is "+subTotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"PC autoship cart tax amount is "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotal),"PC autoship cart grand total is "+grandTotal+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(shipping.contains(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate()),"PC autoship cart shipping amount is "+shipping+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handling),"PC autoship cart handling amount is "+handling+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// Now verify the details of orders
		storeFrontHomePage.navigateToBackPage();

		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksOrderPageHeader(),"Order Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();

		// assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4292_RFO,orderId),RFO_DB);
		subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));

		taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));

		grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4292_RFO,orderId),RFO_DB);
		shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));

		handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc order template subTotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		//assertTrue("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),"Adhoc order template tax amount on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc order template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc order template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc order template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB),"Adhoc order template shipping method on RFO is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate());  

		s_assert.assertAll();  	

	}

	//Hybris Project-137:Enroll and manage my autoships in Canada - consultant
	@Test
	public void testEnrollAndManageMyAutoshipsInCanada_137() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			country = driver.getCountry();
			enrollmentType = TestConstants.STANDARD_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
			String firstName = TestConstants.FIRST_NAME+randomNum;
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
			storeFrontHomePage.clickNextButton();
			storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.selectNewBillingCardExpirationDate();
			storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
			storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
			storeFrontHomePage.clickEnrollmentNextBtn();
			storeFrontHomePage.uncheckPulseAndCRPEnrollment();
			s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsNotSelected(), "Subscribe to pulse checkbox selected after uncheck");
			s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsNotSelected(), "Enroll to CRP checkbox selected after uncheck");
			storeFrontHomePage.clickEnrollmentNextBtn();
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
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyConsultantCantShipToQuebecMsg()," Consultants cannot ship to Quebec message not present on UI");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-4725:Consultant to PC downgrade for US User
	@Test
	public void testDowngradeConsultantToPC_4725() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		env = driver.getEnvironment();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName =  TestConstants.REGIMEN_NAME_REDEFINE;
		country = driver.getCountry();
		String lastName = "lN";
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		storeFrontHomePage.openPWSSite(country, env);

		// Enroll the PC Again
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");
		storeFrontHomePage.clickOnEnrollUnderLastUpline();

		// Enroll Deactivated Consultant as PC
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//storeFrontHomePage.enterEmailAddress(consultantEmailID);
		storeFrontHomePage.enterNewPCDetails(firstName, lastName, password, consultantEmailID);

		//Click on Check out
		//storeFrontHomePage.clickOnCheckoutButton();

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//storeFrontHomePage.clickOnContinueWithoutSponsorLink();
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
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyCurrentUrlContainComSite(), "Pc user not redirected to com site");
		s_assert.assertAll();
	}

	//Hybris Project-4301:In DB, check details of cancelled CRP autoship for active Consultant.
	@Test
	public void testCancelledCRPAutoshipForActiveConsultant_4301() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		String accountID = null;
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String consultantEmailID = null;
		String lastName = null;
		String shippingMethodId =null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
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
		storeFrontAccountInfoPage.clickOnCancelMyCRP();
		//validate CRP has been cancelled..
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");

		// verify autoship details
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number for assert
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//get Autoship Id Fro RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));
		System.out.println("Autoship id "+autoshipID);
		List<Map<String,Object>> shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
		region = (String) getValueFromQueryResult(shippingAddressList, "Region");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);

		//assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate());

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"CRP autoship template subTotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"CRP autoship template tax amount on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"CRP autoship template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"CRP autoship template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"CRP autoship template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"CRP autoship template shipping method on RFO is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

		s_assert.assertAll();
	}

	// Hybris Project-146:Autoship Update - Flow & Review and Confirm step - consultant
	@Test
	public void testAutoshipUpdateFlowReviewAndConfirm_146() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		country=driver.getCountry();

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontCartAutoShipPage=new StoreFrontCartAutoShipPage(driver);
		storeFrontUpdateCartPage=new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login Error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//click on auto ship cart
		storeFrontHomePage.clickOnAutoshipCart();
		//click update more info btn
		storeFrontUpdateCartPage=storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		//select a product and add it to crp
		//storeFrontHomePage.selectAProductAndAddItToCRP();

		//click the edit link in the payment section
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		//click the edit link in the billing section
		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		//update the CC Expiration date and re-enter the CC security code..
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDateAsExpiredDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		//click the edit link in the shipping section
		storeFrontUpdateCartPage.clickOnEditShipping();
		//Add a new shipping address..
		storeFrontUpdateCartPage.clickAddNewShippingProfileLink();
		if(country.equalsIgnoreCase("us")){
			storeFrontUpdateCartPage.enterNewShippingAddressName(TestConstants.NEW_SHIPPING_PROFILE_NAME_US);
			storeFrontUpdateCartPage.enterNewShippingAddressLine1(TestConstants.NEW_ADDRESS_LINE1_US);
			storeFrontUpdateCartPage.enterNewShippingAddressCity(TestConstants.NEW_ADDRESS_CITY_US);
			storeFrontUpdateCartPage.selectNewShippingAddressState();
			storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(TestConstants.NEW_ADDRESS_POSTAL_CODE_US);
			storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(TestConstants.NEW_ADDRESS_PHONE_NUMBER_US);
		}
		else{
			storeFrontUpdateCartPage.enterNewShippingAddressName(TestConstants.NEW_SHIPPING_PROFILE_NAME_CA);
			storeFrontUpdateCartPage.enterNewShippingAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
			storeFrontUpdateCartPage.enterNewShippingAddressCity(TestConstants.CITY_CA);
			storeFrontUpdateCartPage.selectNewShippingAddressState();
			storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(TestConstants.POSTAL_CODE_CA);
			storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER_CA);
		}
		//change the shipping method and proceed to next stp
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		storeFrontUpdateCartPage.clickOnNextStepBtnShippingAddress();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		//validate header..
		s_assert.assertTrue(storeFrontUpdateCartPage.validateHeaderContent(), "header content is not displayed properly");
		//click on update cart button
		//storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		//validate cart has been updated?
		s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(), "cart is not updated!! ");
		s_assert.assertAll();
	}

	//Hybris Project-2144:Check Shipping and Handling Fee for UPS 2Day for Order total 0- 0-999999
	@Test
	public void testCheckShippingAndHandlingFee_2144() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();

		double orderTotal = storeFrontUpdateCartPage.getOrderTotal();
		logger.info("subtotal ="+orderTotal);

		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);

		if(orderTotal<=999999){
			if(driver.getCountry().equalsIgnoreCase("CA")){
				//Assert of shipping cost from UI
				s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 20.00"),"Shipping charges on UI is not As per shipping method selected");
				s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
			}else if(driver.getCountry().equalsIgnoreCase("US")){
				s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$19.00"),"Shipping charges on UI is not As per shipping method selected");
				s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
			}

		}else{
			logger.info(" Order total is not in required range");
		}
		s_assert.assertAll();
	}


	//Hybris Project-2145:Check Shipping and Handling Fee for UPS 2Day for Order total 0-999999-CRP Autoship
	@Test
	public void testCheckShippingAndHandlingFeeAsPerOrderTotal_2145() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnAutoShipButton();
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
		storeFrontUpdateCartPage.clickOnEditShipping();
		storeFrontUpdateCartPage.selectShippingMethod2Day();
		storeFrontUpdateCartPage.clickOnNextStepBtnShippingAddress();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		String orderNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickOrderNumber(orderNumber);
		double orderGrandTotal =storeFrontOrdersPage.getOrderGrandTotal();

		if(orderGrandTotal<=999999){
			if(driver.getCountry().equalsIgnoreCase("CA")){
				s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");

				s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains("22"),"Shipping charges on UI is not As per shipping method selected");
			}
			else if(driver.getCountry().equalsIgnoreCase("US")){
				s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains("$2.50"),"Handling charges on UI is not As per shipping method selected");

				s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(TestConstants.SHIPPING_CHARGES_ON_UI_FOR_US),"Shipping charges on UI is not As per shipping method selected");
			}
		}else{
			logger.info(" Order total is not in required range");
		}
		s_assert.assertAll();

	}

	// Hybris Project-4313:Soft-Terminated Consultant becomes a RC
	@Test
	public void testSoftTerminatedConsultantBecomesRC_4313() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		List<Map<String, Object>> accountIdList =  null;
		String accountID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String  consultantEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME,consultantEmailAddress, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
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
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		//Terminate consultant Account
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationPageIsDisplayed(),"Account Termination Page has not been displayed");
		storeFrontAccountTerminationPage.selectTerminationReason();
		storeFrontAccountTerminationPage.enterTerminationComments();
		storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is Present");
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		// update the enroll date and terminated date in DB
		accountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ID,consultantEmailAddress),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(accountIdList, "AccountId"));
		DBUtil.performDatabaseQueryForUpdate(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.UPDATE_ENROLL_DATE,accountID),RFO_DB);
		DBUtil.performDatabaseQueryForUpdate(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.UPDATE_SOFT_TERMINATION_DATE,accountID),RFO_DB);
		// Rc enrollment using consultant email id
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
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
		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, consultantEmailAddress, password);
		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		//storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
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
		s_assert.assertAll(); 
	}

	//Hybris Project-4793:Ad-hoc Scenarios- Checking the consultant > consultant > PC for US User
	@Test
	public void testRegisterAsConsultantWithDifferentConsultantAsSponser_4793() throws InterruptedException {
		if(driver.getCountry().equalsIgnoreCase("us")){
			RFO_DB = driver.getDBNameRFO();	
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			String socialInsuranceNumbers = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String firstName=TestConstants.FIRST_NAME+randomNum;
			String lastName = "lN";

			List<Map<String, Object>> randomConsultantList =  null;
			List<Map<String, Object>> sponsorIdList = null;
			List<Map<String, Object>> consultantSponsorIdList = null;
			String firstConsultantEmailID = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
			int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
			String secondConsultantEmailID = TestConstants.FIRST_NAME+randomNumber+TestConstants.EMAIL_ADDRESS_SUFFIX;
			country = driver.getCountry();
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REVERSE;
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			//get pws from query for assertion
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			String emailIdOfFirstConsultantSponser = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			String accountIDOfFirstConsultantSponser = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			// sponser search by Account Number
			sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountIDOfFirstConsultantSponser),RFO_DB);
			String sponserIdOfFirstConsultant = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
			//Enroll consultant one who will be Sponser of Second Enrolled Consultant
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID(sponserIdOfFirstConsultant);
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType,firstName, TestConstants.LAST_NAME,firstConsultantEmailID, password, addressLine1, city, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
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
			logout();
			driver.get(driver.getURL()+"/"+driver.getCountry());
			//Enroll Second Consultant with consultant one as sponser
			// sponser search by Account Number
			consultantSponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,firstConsultantEmailID),RFO_DB);
			String sponserIdOfSecondConsultant = (String) getValueFromQueryResult(consultantSponsorIdList, "AccountNumber");
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID(sponserIdOfSecondConsultant);
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType,TestConstants.FIRST_NAME+randomNumber, TestConstants.LAST_NAME,secondConsultantEmailID, password, addressLine1, city, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
			storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNumber);
			storeFrontHomePage.selectNewBillingCardExpirationDate();
			storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumbers);
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
			logout();
			driver.get(driver.getURL()+"/"+driver.getCountry());
			//terminate First consultant account who is sponser of second consultant
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(firstConsultantEmailID, password);
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
			storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();
			storeFrontHomePage.loginAsConsultant(firstConsultantEmailID, password);
			s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Terminated User doesn't get Login failed"); 
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			//terminate second consultant account
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(secondConsultantEmailID, password);
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
			storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();
			storeFrontHomePage.loginAsConsultant(secondConsultantEmailID, password);
			s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Terminated User doesn't get Login failed"); 
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			//register as PC with email id of second terminate consultant
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");
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
			storeFrontHomePage.enterEmailAddress(secondConsultantEmailID);
			s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");
			storeFrontHomePage.clickOnEnrollUnderLastUpline();
			// Enroll Deactivated Consultant as PC
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");
			storeFrontHomePage.selectProductAndProceedToBuy();
			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();
			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");
			//storeFrontHomePage.enterEmailAddress(consultantEmailID);
			storeFrontHomePage.enterNewPCDetails(firstName, lastName, password, secondConsultantEmailID);
			//verify for the sponser of pc user should be the sponser of terminated consultant one
			s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(emailIdOfFirstConsultantSponser),"Sponser for pc user is not as expected");
			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");
			//storeFrontHomePage.clickOnContinueWithoutSponsorLink();
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
			storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
			storeFrontHomePage.clickPlaceOrderBtn();
			if(country.equalsIgnoreCase("CA"))
				s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for US env");
		}
	}

	//Hybris Project-4792:Ad-hoc Scenarios- Checking the consultant > consultant > PC for Canada User
	@Test
	public void testRegisterAsConsultantWithDifferentConsultantAsSponser_4792() throws InterruptedException {
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();	
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			String socialInsuranceNumbers = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String firstName=TestConstants.FIRST_NAME+randomNum;
			String lastName = "lN";
			List<Map<String, Object>> randomConsultantList =  null;
			List<Map<String, Object>> sponsorIdList = null;
			List<Map<String, Object>> consultantSponsorIdList = null;
			String firstConsultantEmailID = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
			int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
			String secondConsultantEmailID = TestConstants.FIRST_NAME+randomNumber+TestConstants.EMAIL_ADDRESS_SUFFIX;
			country = driver.getCountry();
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REVERSE;
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			//get pws from query for assertion
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			String emailIdOfFirstConsultantSponser = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			String accountIDOfFirstConsultantSponser = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			// sponser search by Account Number
			sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountIDOfFirstConsultantSponser),RFO_DB);
			String sponserIdOfFirstConsultant = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
			//Enroll consultant one who will be Sponser of Second Enrolled Consultant
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID(sponserIdOfFirstConsultant);
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType,firstName, TestConstants.LAST_NAME,firstConsultantEmailID, password, addressLine1, city, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
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
			logout();
			driver.get(driver.getURL()+"/"+driver.getCountry());
			//Enroll Second Consultant with consultant one as sponser
			// sponser search by Account Number
			consultantSponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,firstConsultantEmailID),RFO_DB);
			String sponserIdOfSecondConsultant = (String) getValueFromQueryResult(consultantSponsorIdList, "AccountNumber");
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID(sponserIdOfSecondConsultant);
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType,TestConstants.FIRST_NAME+randomNumber, TestConstants.LAST_NAME,secondConsultantEmailID, password, addressLine1, city, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
			storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNumber);
			storeFrontHomePage.selectNewBillingCardExpirationDate();
			storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumbers);
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
			logout();
			driver.get(driver.getURL()+"/"+driver.getCountry());
			//terminate First consultant account who is sponser of second consultant
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(firstConsultantEmailID, password);
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
			storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();
			storeFrontHomePage.loginAsConsultant(firstConsultantEmailID, password);
			s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Terminated User doesn't get Login failed"); 
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			//terminate second consultant account
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(secondConsultantEmailID, password);
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
			storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();
			storeFrontHomePage.loginAsConsultant(secondConsultantEmailID, password);
			s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Terminated User doesn't get Login failed"); 
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			//register as PC with email id of second terminate consultant
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");
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
			storeFrontHomePage.enterEmailAddress(secondConsultantEmailID);
			s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");
			storeFrontHomePage.clickOnEnrollUnderLastUpline();
			// Enroll Deactivated Consultant as PC
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");
			storeFrontHomePage.selectProductAndProceedToBuy();
			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();
			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");
			//storeFrontHomePage.enterEmailAddress(consultantEmailID);
			storeFrontHomePage.enterNewPCDetails(firstName, lastName, password, secondConsultantEmailID);
			//verify for the sponser of pc user should be the sponser of terminated consultant one
			s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(emailIdOfFirstConsultantSponser),"Sponser for pc user is not as expected");
			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");
			//storeFrontHomePage.clickOnContinueWithoutSponsorLink();
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
			storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
			storeFrontHomePage.clickPlaceOrderBtn();
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	// Hybris Project-4724:Consultant to PC downgrade for Canadian User
	@Test
	public void testConsultantToPCDownGradeforCanadianUser_4724() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){	
			RFO_DB = driver.getDBNameRFO();	
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;
			String accountID = null;
			env = driver.getEnvironment();
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName =  TestConstants.REGIMEN_NAME_REDEFINE;
			country = driver.getCountry();
			String lastName = "lN";
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
			String firstName=TestConstants.FIRST_NAME+randomNum;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			while(true){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
				accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				logger.info("Account Id of the user is "+accountID);
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
				boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
				if(isSiteNotFoundPresent){
					logger.info("SITE NOT FOUND for the user "+consultantEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}

			//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
			storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();
			storeFrontHomePage.openPWSSite(country, env);
			// Enroll the PC Again
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");
			storeFrontHomePage.selectProductAndProceedToBuy();
			//Cart page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
			logger.info("Cart page is displayed");
			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();
			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");
			storeFrontHomePage.enterEmailAddress(consultantEmailID);
			s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");
			storeFrontHomePage.clickOnEnrollUnderLastUpline();
			// Enroll Deactivated Consultant as PC
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");
			storeFrontHomePage.selectProductAndProceedToBuy();
			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();
			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");
			storeFrontHomePage.enterEmailAddress(consultantEmailID);
			storeFrontHomePage.clickOnEnrollUnderLastUplineProcessToPopupDisappear(consultantEmailID);
			storeFrontHomePage.enterNewPCDetails(firstName, lastName, password, consultantEmailID);
			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");
			//storeFrontHomePage.clickOnContinueWithoutSponsorLink();
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
			storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
			storeFrontHomePage.clickPlaceOrderBtn();
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			s_assert.assertTrue(storeFrontHomePage.verifyCurrentUrlContainComSite(), "Pc user not redirected to com site");
			s_assert.assertAll();

		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}

	}

	//Hybris Project-1978:Orders page UI for Consultant - Cart - history and autoships
	@Test
	public void testOrderPageUIAndEditCartForConsultant_1978() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		List<Map<String, Object>> orderStatusList =  null;
		List<Map<String, Object>> orderGrandTotalList =  null;
		List<Map<String, Object>> orderDateList =  null;

		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotal = null;
		String subTotal = null;
		String shipping = null;
		String handling = null;
		String tax = null; 
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String lastName = null;
		String orderId = null;
		String shippingMethodId =null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		// Get Order Id
		List<Map<String,Object>> getOrderIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderIDList, "OrderID"));
		//assert for order status with RFO
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFO, consultantEmailID),RFO_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		logger.info("Order Status from RFO DB is "+orderStatusDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrderStatus(orderStatusDB),"Order Status on UI is different from RFO DB");
		//assert for grand total with RFO
		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4287_RFO, orderId),RFO_DB);
		DecimalFormat dff = new DecimalFormat("#.00");
		orderGrandTotalDB = String.valueOf(dff.format(getValueFromQueryResult(orderGrandTotalList, "Total"))); 
		logger.info("Order GrandTotal from RFO DB is "+orderGrandTotalDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),"Grand total on UI is different from RFO DB");
		//assert for order date with RFO
		orderDateList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFO, consultantEmailID),RFO_DB);
		orderDateDB = String.valueOf (getValueFromQueryResult(orderDateList, "CompletionDate"));
		logger.info("Order Scheduled Date from RFO DB is "+orderDateDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyScheduleDate(orderDateDB),"Scheduled date on UI is different from RFO DB");
		// click on edit
		storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate();
		storeFrontHomePage.navigateToBackPage();
		storeFrontOrdersPage.clickOnEditAtAutoshipTemplate();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		String quantityBeforeUpdate = storeFrontUpdateCartPage.getQuantityOfProductOnCartPage();
		String updatedQuantity = storeFrontUpdateCartPage.upgradeQuantityOfProduct(quantityBeforeUpdate);
		storeFrontHomePage.addQuantityOfProduct(updatedQuantity);
		// assert update quantity on cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.getQuantityOfProductOnCartPage().contains(updatedQuantity),"CRP autoship template subTotal on RFO is "+updatedQuantity+" and on UI is "+storeFrontUpdateCartPage.getQuantityOfProductOnCartPage());
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
		// values for assertion for pending order autoship template
		subTotal = storeFrontUpdateCartPage.getSubtotalFromCart();
		shipping = storeFrontUpdateCartPage.getDeliveyFromCart();
		handling = storeFrontUpdateCartPage.getHandlingFromCart();
		tax = storeFrontUpdateCartPage.getTaxFromCart();
		grandTotal = storeFrontUpdateCartPage.getGrandTotalFromCart();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		// assert update quantity on autoship template 
		s_assert.assertTrue(storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate().contains(updatedQuantity),"CRP autoship template subTotal on RFO is "+updatedQuantity+" and on UI is "+storeFrontOrdersPage.getQuantityOfProductFromAutoshipTemplate());
		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotal),"PC autoship cart subTotal is "+subTotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"PC autoship cart tax amount is "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());
		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotal),"PC autoship cart grand total is "+grandTotal+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(shipping.contains(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate()),"PC autoship cart shipping amount is "+shipping+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handling),"PC autoship cart handling amount is "+handling+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		// Now verify the details of orders
		storeFrontHomePage.navigateToBackPage();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksOrderPageHeader(),"Order Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");
		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		// assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());
		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4292_RFO,orderId),RFO_DB);
		subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
		taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
		grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));
		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4292_RFO,orderId),RFO_DB);
		shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
		handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc order template subTotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// Assert Tax with RFO
		//assertTrue("Tax is not as expected", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),"Adhoc order template tax amount on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc order template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc order template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc order template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		// assert for shipping Method with RFO
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB),"Adhoc order template shipping method on RFO is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate());  
		s_assert.assertAll();  	
	}

	// Hybris Project-2273:Adhoc Orders for Consultant and PC and RC --> Multiple line Item
	@Test
	public void testAdhocOrdersForMultiplsLineItem_2273() throws InterruptedException	 {
		RFO_DB = driver.getDBNameRFO(); 
		String country = driver.getCountry();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		if(country.equalsIgnoreCase("us")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
		}
		else if(country.equalsIgnoreCase("ca")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
		} 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontOrdersPage=new StoreFrontOrdersPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
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
		//update qty to 2 of the first product
		storeFrontHomePage.addQuantityOfProduct("2"); 
		//add another product in the cart
		storeFrontHomePage.addAnotherProduct();
		logger.info("2 products are successfully added to the cart");
		//update qty to 1 of the second product
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("1"); 
		//get the quantity value for the first product in cart
		int qtyProduct1=storeFrontHomePage.getQuantityValueForTheFirstProduct();
		//get the quantity value for the Second product in cart
		int qtyProduct2=storeFrontHomePage.getQuantityValueForTheSecondProduct();
		//get the sub-total of the first product
		double subtotal1=storeFrontHomePage.getSubTotalOfFirstProduct();
		//get the sub-total of the second product
		double subtotal2=storeFrontHomePage.getSubTotalOfSecondProduct();
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//click next button
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//click next button
		storeFrontHomePage.clickOnBillingNextStepBtn();
		//click place-order button
		storeFrontHomePage.clickPlaceOrderBtn();
		//Navigate to orders section
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontOrdersPage=storeFrontHomePage.clickOrdersLinkPresentOnWelcomeDropDown();
		//click on first adhoc order placed
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//On orders page validate the sub-total for the order placed
		s_assert.assertTrue(storeFrontOrdersPage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		//validate with the no. of quantitie(s) of both the products
		s_assert.assertTrue(storeFrontOrdersPage.validateQuantityForFirstProduct(qtyProduct1),"quantitie(s) for product 1 didn't match");
		s_assert.assertTrue(storeFrontOrdersPage.validateQuantityForSecondProduct(qtyProduct2),"quantitie(s) for product 2 didn't match");
		s_assert.assertAll();
	}

	//Hybris Project-2272:Adhoc Orders fro Consultant and PC and RC --> Single line Item
	@Test
	public void testAdhocOrdersFromConsultantAndPc_2272() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.clickAddToBagButton();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		String quantityOfProductsOrdered = storeFrontUpdateCartPage.getQuantityOfProductOnCartPage();
		String totalPrice = storeFrontUpdateCartPage.getTotalPriceOfProduct();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.isOrderPlacedSuccessfully(),"order is not placed successfully");
		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontHomePage.clickOrdersLinkPresentOnWelcomeDropDown();
		String orderNumber=storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderNumber); 
		s_assert.assertTrue(storeFrontOrdersPage.verifyQuantityOnOrdersDetails(quantityOfProductsOrdered),"quantity is not matched in order detail page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyTotalValueOfProductOnOrderDetails(totalPrice));
		s_assert.assertAll();
	}

	// Hybris Project-2130:To verify Change date functionality for PC shouldnt be present on the storefront
	@Test
	public void testVerifyChangeDateFunctionalityForPCUser_2130() throws InterruptedException	  {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);  
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
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
		storeFrontCartAutoShipPage=storeFrontPCUserPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage=storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		s_assert.assertFalse(storeFrontUpdateCartPage.checkDateFunctionality(), "check date functionality is present");
		s_assert.assertAll();
	}

	//	//Hybris Project-2131:To verify the change date functionality for consultant on the storefront
	//	@Test(enabled=false) //WIP
	//	public void testDateFunctionalityForConsultant_2131() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO(); 
	//		List<Map<String, Object>> randomConsultantList =  null;
	//		String consultantEmailID = null;
	//		String accountID = null;
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		while(true){
	//			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
	//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
	//			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
	//			logger.info("Account Id of the user is "+accountID);
	//			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
	//			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
	//			if(isSiteNotFoundPresent){
	//				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
	//				driver.get(driver.getURL());
	//			}
	//			else
	//				break;
	//		}
	//
	//		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
	//		logger.info("login is successful");
	//		storeFrontConsultantPage.clickOnWelcomeDropDown();
	//		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
	//		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
	//		storeFrontUpdateCartPage.clickOnEditShipping();
	//		storeFrontUpdateCartPage.clickOnChangeNextShipDate();
	//		int noOfDay = storeFrontUpdateCartPage.getCountofDateFromCalendar();
	//		for(int i=1; i<=17; i++){
	//			System.out.println("value of i "+i);
	//			s_assert.assertTrue(storeFrontUpdateCartPage.verifyEnabledDatesOfTheCalendar(i),"This date -"+storeFrontUpdateCartPage.verifyEnabledDatesOfTheCalendar(i)+" is not enabled");
	//			System.out.println("Done");
	//		}
	//		for(int i=18; i<=noOfDay; i++){
	//			s_assert.assertTrue(storeFrontUpdateCartPage.verifyDisabledDatesOfTheCalendar(i),"This date -"+storeFrontUpdateCartPage.verifyDisabledDatesOfTheCalendar(i)+" is not enabled");
	//		}
	//		s_assert.assertAll();		
	//	}

	// Hybris Project-4304:In DB, check details of CRP autoship for inactive consultant
	@Test
	public void checkDetailsOfCRpAutoshipForInactiveConsultant_4304() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String consultantEmailID = null;
		String lastName = null;
		String accountId = null;
		String shippingMethodId =null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}		
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		// Get Order Number for assert
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		//get Autoship Id Fro RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));
		//get values from UI before termination
		String shippingAddressFromUI =storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate();
		String subTotalFromUI = storeFrontOrdersPage.getSubTotalFromAutoshipTemplate();
		String shippingFromUI = storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate();
		String handlingFromUI = storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate();
		String taxFromUI = 	storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate();
		String grandTotalFromUI = storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate();
		String shippingMethodFromUI = storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate();
		// terminate the consultant
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Terminated User doesn't get Login failed");
		// get values from DB for assertion 
		List<Map<String,Object>> shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
		region = (String) getValueFromQueryResult(shippingAddressList, "Region");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1.trim()+"\n"+locale.trim()+", "+region.trim()+" "+postalCode.trim()+"\n"+country.toUpperCase().trim();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		//assert shipping Address with RFO
		s_assert.assertTrue(shippingAddressFromUI.contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+shippingAddressFromUI);
		//Assert Subtotal with RFO
		s_assert.assertTrue(subTotalFromUI.contains(subTotalDB),"CRP autoship template subTotal on RFO is "+subTotalDB+" and on UI is "+subTotalFromUI);
		// Assert Tax with RFO
		s_assert.assertTrue(taxFromUI.contains(taxDB),"CRP autoship template tax amount on RFO is "+taxDB+" and on UI is "+taxFromUI);
		// Assert Grand Total with RFO
		s_assert.assertTrue(grandTotalFromUI.contains(grandTotalDB),"CRP autoship template grand total on RFO is "+grandTotalDB+" and on UI is "+grandTotalFromUI);
		// assert shipping amount with RFO
		s_assert.assertTrue(shippingFromUI.contains(shippingDB),"CRP autoship template shipping amount on RFO is "+shippingDB+" and on UI is "+shippingFromUI);
		// assert Handling Value with RFO
		s_assert.assertTrue(handlingFromUI.contains(handlingDB),"CRP autoship template handling amount on RFO is "+handlingDB+" and on UI is "+handlingFromUI);
		// assert for shipping Method with RFO
		s_assert.assertTrue(shippingMethodFromUI.contains(shippingMethodDB),"CRP autoship template shipping method on RFO is "+shippingMethodDB+" and on UI is "+shippingMethodFromUI);
		s_assert.assertAll();
	}
}
