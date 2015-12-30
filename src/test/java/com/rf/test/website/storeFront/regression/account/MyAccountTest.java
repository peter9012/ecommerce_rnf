package com.rf.test.website.storeFront.regression.account;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
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
		s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickConfirmTerminationBtn();		
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
		String username = storeFrontAccountInfoPage.getUsernameFromAccountInfoPage();
		storeFrontAccountInfoPage.checkAllowMySpouseCheckBox();
		//Enter first,last name and click accept on the popup and validate
		s_assert.assertTrue(storeFrontAccountInfoPage.validateEnterSpouseDetailsAndAccept(),"Accept button not working in the popup");
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
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 6 alphanumneric characters with atleast a special char IS NOT PRESENT");
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		anotherConsultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");

		//  s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"This username has been assigned to other user");
		//  enter data more then 8 characters with few special characters.
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_ALPHA_WITH_SPCL_CHAR);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 6 alphanumneric characters with atleast a special char IS NOT PRESENT");
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_ALPHA_WITH_SPCL_CHAR_COMB);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 6 alphanumneric characters combination with atleast a special char IS NOT PRESENT");
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_ALPHA_WITH_SINGLE_SPCL_CHAR);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.enterUserNameWithSpclChar(TestConstants.CONSULTANT_USERNAME_PREFIX),"validity message not present");
		storeFrontAccountInfoPage.enterUserName(username);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		storeFrontAccountInfoPage.enterUserName(anotherConsultantEmailID);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.errorMessageForExistingUser(),"username is getting renamed with the username of existing user");
		logout();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(username, password);

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
		env = driver.getEnvironment();
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
		storeFrontHomePage.openPWSSite(country,env );
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
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
		s_assert.assertTrue(storeFrontHomePage.getInvalidPasswordNotmatchingMessage().contains("Your passwords do not match"), "Error message for invalid confirm password does not visible");
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
	public void testSwitchFromRCToPC_1304() throws InterruptedException {
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
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is not Present");
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
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
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is not Present");
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
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
		s_assert.assertFalse(storeFrontHomePage.validateSendMailToResetMyPasswordFunctionalityPC(),"Send Mail to reset password' button not working");	

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
		s_assert.assertTrue(storeFrontHomePage.validateSendMailToResetMyPasswordFunctionalityRC(),"'Send mail to reset my password' button not working");	

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
		RFO_DB = driver.getDBNameRFO(); 
		// create RC User
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

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String emailAddress = firstName+randomNum+"@xyz.com";
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, emailAddress, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC();
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
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(), "Yes I want to join pc perks checkboz is not selected");

		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
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
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
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
		int randomNumber = CommonUtils.getRandomNum(100, 1000);
		String prefixNameWithSpecialChar = TestConstants.FIRST_NAME+randomNumber+"$";
		String prefixName = TestConstants.FIRST_NAME+randomNumber;
		storeFrontHomePage.enterWebsitePrefixName(prefixNameWithSpecialChar);
		s_assert.assertTrue(storeFrontHomePage.verifySpecialCharNotAcceptInPrefixName(), "Special Char is accepcted by prefix name 1");
		storeFrontHomePage.enterWebsitePrefixName(prefixName);

		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		storeFrontHomePage.clickEnrollmentNextBtn();
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
		storeFrontHomePage.enterWebsitePrefixName(prefixName);
		s_assert.assertFalse(storeFrontAccountInfoPage.verifySpecialCharNotAcceptInPrefixName(), "Special Char is accepcted by prefix name 3");
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
		logout();
		//driver.close();
		//validate with the current .biz PWSUrl
		driver.get(currentPWSUrl);
		s_assert.assertTrue(storeFrontHomePage.validatePWS(), "Dot biz PWS is not active");   
		//validate the .com PWSURL.
		String currentComUrl= storeFrontHomePage.createBizToCom(currentPWSUrl);
		driver.get(currentComUrl);
		s_assert.assertTrue(storeFrontHomePage.validatePWS(), "Dot com PWS is not active"); 
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
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
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		String newUsername = TestConstants.CONSULTANT_EMAIL_ID_FOR_ACCOUNTINFO+randomNum;
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

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		//Enter new user name logout and validate the new user
		storeFrontAccountInfoPage.enterUserName(newUsername);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		logout();
		//login with the same user
		//storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(newUsername,password);   
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		//validate the new user
		s_assert.assertTrue(newUsername.equalsIgnoreCase(storeFrontAccountInfoPage.getUserName()),"Username didn't match");
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
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
		storeFrontAccountInfoPage.checkAllowMySpouseCheckBox();
		//click cancel on the 'Allow my spouse' popup and validate
		s_assert.assertFalse(storeFrontAccountInfoPage.validateClickCancelOnProvideAccessToSpousePopup(),"Cancel button not working in the popup");
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
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
		/*s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		  storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();*/
		s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
		storeFrontAccountTerminationPage.clickConfirmTerminationBtn();
		s_assert.assertFalse(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is still displayed");
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
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
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
		String comPWS=storeFrontHomePage.getDotComPWS();
		String bizPWS=storeFrontHomePage.getDotBizPWS();
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		storeFrontHomePage.clickEnrollmentNextBtn();
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
	public void testVerifyConsultantCanUaseSamePrefixWhenSubscribingToPulse_2489() throws InterruptedException   {
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
		String comPWS=storeFrontHomePage.getDotComPWS(driver.getCountry());
		String bizPWS=storeFrontHomePage.getDotBizPWS(driver.getCountry());
		String emailID=storeFrontHomePage.getEmailId(driver.getCountry());
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
		//verify each suggestion of pulse prefix
		s_assert.assertTrue(storeFrontConsultantPage.getDotComPWS(driver.getCountry()).contains(comPWS), "pulse prefix dot com pws is not present");
		s_assert.assertTrue(storeFrontConsultantPage.getDotBizPWS(driver.getCountry()).contains(bizPWS), "pulse prefix dot biz pws is not present");
		s_assert.assertTrue(storeFrontConsultantPage.getEmailId(driver.getCountry()).contains(emailID), "Email id with pulse prefix is not present");
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
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
		env = driver.getEnvironment();
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
		storeFrontHomePage.openPWSSite(country, env);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
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
	@Test(enabled=true) //WIP
	public void testCheckThresholdByAddRemoveProductsAndIncreaseDecreaseQty_143() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 

		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String qtyOfProducts="10";
		String newQtyOfProducts="5";

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		storeFrontHomePage.clickOnAutoshipCart();
		storeFrontUpdateCartPage= new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
		storeFrontUpdateCartPage.selectDifferenetProductAndAddItToCRP();   
		int noOfProduct = storeFrontUpdateCartPage.getNoOfProductInCart();   
		for(int i=noOfProduct; i>=1; i--){
			boolean flag = storeFrontUpdateCartPage.getValueOfFlag(i);
			if(flag==true){
				double SVValue = Double.parseDouble(storeFrontUpdateCartPage.getSVValueFromCart().trim());
				String SVValueOfRemovedProduct = storeFrontUpdateCartPage.removeProductSFromCart(i);

				double remainingSVValue = storeFrontUpdateCartPage.compareSVValue(SVValueOfRemovedProduct, SVValue);
				if(driver.getCountry().equalsIgnoreCase("us")){
					if(remainingSVValue>=80.00){
						s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is "+i+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);

					}else{
						s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_FOR_CONSULTANT),"Error message for threshold condition for zero quantity from UI is  "+i+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_FOR_CONSULTANT);
						break;
					}
				}else 
					if(remainingSVValue>=100.00){
						s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is 3 "+i+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);

					}else{
						s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_CA),"Error message for threshold condition for zero quantity from UI is 4 "+i+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG);
						break;
					}

			}else{
				logger.info("SV value is null");
			}
		}

		storeFrontHomePage.addQuantityOfProduct(qtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected msg is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);
		storeFrontHomePage.addQuantityOfProduct(newQtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);

		s_assert.assertAll(); 
	}

	// Hybris Project-142 Autoship template - manage products in cart - PC perk 
	@Test
	public void testAutoshipTemplateManagePoductsInCartPCPerk_142() throws InterruptedException {
		String qtyOfProducts="10";
		String newQtyOfProducts="5";
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		logger.info("login is successful"); 
		//click on auto ship cart

		storeFrontHomePage.clickOnAutoshipCart();
		storeFrontUpdateCartPage= new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
		storeFrontUpdateCartPage.selectProductAndProceedToBuyForPC();   
		int noOfProduct = storeFrontUpdateCartPage.getNoOfProductInCart();   
		for(int i=noOfProduct; i>=1; i--){
			boolean flag = storeFrontUpdateCartPage.getValueOfFlagForPC(i);
			if(flag==true){
				double SVValue = Double.parseDouble(storeFrontUpdateCartPage.getSubtotalFromCart().split("\\$")[1].trim());
				String SVValueOfRemovedProduct = storeFrontUpdateCartPage.removeProductsFromCartForPC(i);

				double remainingSVValue = storeFrontUpdateCartPage.compareSubtotalValue(SVValueOfRemovedProduct, SVValue);
				if(driver.getCountry().equalsIgnoreCase("us")){
					if(remainingSVValue>=80.00){
						s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is "+i+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);

					}else{
						s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG),"Error message for threshold condition for zero quantity from UI is  "+i+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG);
						break;
					}
				}else 
					if(remainingSVValue>=90.00){
						s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is 3 "+i+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);

					}else{
						s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_CA_FOR_PC),"Error message for threshold condition for zero quantity from UI is 4 "+i+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG);
						break;
					}

			}else{
				logger.info("SV value is null");
			}
		}

		storeFrontHomePage.addQuantityOfProduct(qtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected msg is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);
		storeFrontHomePage.addQuantityOfProduct(newQtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);

		s_assert.assertAll(); 
	}
	//Hybris Project-4350:Verify "Join my team" button on the .com and .biz site
	@Test
	public void testJoinMyTeamButtonPresentOnTheComAndBizSite_4350(){
		country = driver.getCountry();
		env = driver.getEnvironment();		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
		//String comPws = storeFrontHomePage.convertBizToComPWS(bizPws);
		storeFrontHomePage.openPWS(PWS);
		storeFrontHomePage.clickOnUserName();
		s_assert.assertFalse(storeFrontHomePage.verifyJoinMyTeamLinkPresent(), "Join My Team Link present on the Com page");
		PWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		storeFrontHomePage.clickOnUserName();
		s_assert.assertTrue(storeFrontHomePage.verifyJoinMyTeamLinkPresent(), "Join My Team Link is not present on the Biz page");
		storeFrontHomePage.clickOnJoinMyTeamBtn();
		s_assert.assertTrue(driver.getCurrentUrl().contains(".biz/"+driver.getCountry()+"/consultant/enrollment/kitproduct"),"user is not on biz enrollment kit page after clicking 'Join My Team' btn");
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
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
			consultantPWSURL = storeFrontHomePage.convertComSiteToBizSite(consultantPWSURL);
			storeFrontHomePage.openPWS(consultantPWSURL);
			if(driver.getCurrentUrl().contains("sitenotfound"))
				continue;
			else
				break;
		}
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
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
			consultantPWSURL = storeFrontHomePage.convertBizSiteToComSite(consultantPWSURL);
			storeFrontHomePage.openPWS(consultantPWSURL);
			if(driver.getCurrentUrl().contains("sitenotfound"))
				continue;
			else
				break;
		}
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
		randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");

		// For .com site
		consultantPWSURL = storeFrontHomePage.convertBizSiteToComSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.getHomtownNamePresentOnAfterClickonPersinalizeLink().length()>0, "HomeTown text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getConsultantSinceTextPresentAfterClickonPersinalizeLink().length()>0, "Consultant since text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getFavoriteProductNameIsPresentAfterClickonPersinalizeLink().length()>0, "Favorite product text is not present on meet your consultant page");

		// For .biz site
		consultantPWSURL = storeFrontHomePage.convertComSiteToBizSite(consultantPWSURL);
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
		randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);

		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
		//consultantPWSURL = storeFrontHomePage.convertBizToComPWS(consultantPWSURL);

		// for .com site
		consultantPWSURL = storeFrontHomePage.convertBizSiteToComSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfWhyIJoinedOnMeetYourConsultantPage(), "Why I joined Rodan + Fields block is not Present on meet your consultant page ");
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfProductsOnMeetYourConsultantPage(), "What I love most about R+F products block is not Present on meet your consultant page ");
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfBusinessOnMeetYourConsultantPage(), "What I love most about my R+F Business block is not Present on meet your consultant page ");

		// For .biz site
		consultantPWSURL = storeFrontHomePage.convertComSiteToBizSite(consultantPWSURL);
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
	public void testRegisterAsRCWithDifferentSponserWithPulse_3854() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		List<Map<String, Object>> sponserList =  null;
		String sponserHavingPulse = null;
		country = driver.getCountry();
		String firstName=TestConstants.FIRST_NAME+randomNum;
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Get Canadian sponser with PWS from database
		sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));

		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
		String idForConsultant = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		String PWS = storeFrontHomePage.openPWSSite(country, driver.getEnvironment());

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
		s_assert.assertFalse(storeFrontHomePage.verifyPWSAfterSuccessfulEnrollment(PWS,currentPWSUrl),"PWS of the RC after enrollment is same as the one it started enrollment");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll(); 
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
		//click on the rodanfields Logo
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(driver.getCurrentUrl().contains("corprfo"), "RC is not registered to corporate site");
		logout();

		//PC-Enrollment
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

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

		//Enter the User information and DO  check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);

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
		s_assert.assertTrue(driver.getCurrentUrl().contains("corprfo"), "PC is not registered to corporate site");
		s_assert.assertAll(); 
	}

	// Hybris Project-3774:Register as RC with Different CA Sponsor WITH Pulse
	@Test 
	public void testRegisterAsRCWithDifferentCASponserWithPulse_3774() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		List<Map<String, Object>> sponserList =  null;
		String sponserHavingPulse = null;
		country = driver.getCountry();
		String firstName=TestConstants.FIRST_NAME+randomNum;
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Get Canadian sponser with PWS from database
		sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));

		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
		String idForConsultant = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		String PWS = storeFrontHomePage.openPWSSite(country, driver.getEnvironment());

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
		s_assert.assertFalse(storeFrontHomePage.verifyPWSAfterSuccessfulEnrollment(PWS,currentPWSUrl),"PWS of the RC after enrollment is same as the one it started enrollment");
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
		List<Map<String, Object>> canadianSponserList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
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
	public void testVerifyChangeSponsorFunctionality_2249() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);

		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		String PCUserEmailID = null;
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
		PCUserEmailID = firstName+"@xyz.com";
		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum,password,PCUserEmailID);
		storeFrontHomePage.clickOnRequestASponsorBtn();
		storeFrontHomePage.clickOKOnSponsorInformationPopup();
		storeFrontHomePage.clickOnNotYourSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue Without sponser link is not present");
		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifyRFCorporateSponsorPresent(),"RF Corporate sponsor not present");
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

		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isWelcomePCPerksMessageDisplayed(), "welcome PC perks message should be displayed");
		s_assert.assertAll();
	}

	//Hybris Project-2251 :: Version : 1 :: Global Sponsorship: Cross Country Sponsor
	@Test
	public void testGlobalSponsorshipCrossCountrySponsor_2251() throws InterruptedException	{
		country = driver.getCountry();
		String countryIDS=null;
		String CCS = null;
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> sponsorIdList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REVERSE;
		String firstName=TestConstants.FIRST_NAME+randomNum;

		if(country.equalsIgnoreCase("CA")){
			countryIDS="236";
			kitName = TestConstants.KIT_NAME_EXPRESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SPONSOR_ID,countryIDS),RFO_DB);
			CCS = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
		}else{
			countryIDS="40";
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SPONSOR_ID,countryIDS),RFO_DB);
			CCS = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
		}
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID(CCS);
		s_assert.assertTrue(storeFrontHomePage.isSponsorPresentInSearchList(), "No Sponsor present in search results");
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum,password, addressLine1, city,postalCode, phoneNumber);
		//storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, TestConstants.PROVINCE_YUKON, postalCode, phoneNumber);
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
		int i=0;
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		//storeFrontUpdateCartPage.clickOnUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		//s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		//storeFrontUpdateCartPage.clickOnConfirmationOK();
		//storeFrontUpdateCartPage.clickOnUpdateCartButton();

		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();

		for(i=0;i<2;i++){
			storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
			storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+i+" "+lastName);
			storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
			storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontUpdateCartPage.selectNewBillingCardAddress();
			storeFrontUpdateCartPage.clickOnSaveBillingProfile();
			s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedBillingProfileIsSelectedByDefault(newBillingProfileName+i),"New Billing Profile is not selected by default on CRP cart page");
		}
		i=1;
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickBillingEditAfterSave();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedBillingProfileIsSelectedByDefault(newBillingProfileName+i),"New Billing Profile is not selected by default on CRP cart page");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(),"Order is not placed successfully");
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		//------------------ Verify that adhoc orders template doesn't contains the newly created billing profile by verifying by name------------------------------------------------------------

		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName+i),"AdHoc Orders Template Payment Method contains new billing profile when future autoship checkbox not selected");
		//------------------Verify that billing info page contains the newly created billing profile
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName+i),"Newly added Billing profile is NOT listed on the billing page");

		s_assert.assertAll();
		//change method-clickOnFirstAdHocOrder,,isTheBillingAddressPresentOnPage,,isNewlyCreatedBillingProfileIsSelectedByDefault

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
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);

		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		String PCUserEmailID = null;
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
		PCUserEmailID = firstName+"@xyz.com";
		//Enter the User information and check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum,password,PCUserEmailID);
		storeFrontHomePage.clickOnRequestASponsorBtn();
		storeFrontHomePage.clickOKOnSponsorInformationPopup();
		storeFrontHomePage.clickOnNotYourSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue Without sponser link is not present");
		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifyRFCorporateSponsorPresent(),"RF Corporate sponsor not present");
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

		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isWelcomePCPerksMessageDisplayed(), "welcome PC perks message should be displayed");
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
				boolean isError = driver.getCurrentUrl().contains("error");
				if(isError){
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
			//storeFrontShippingInfoPage.selectFirstCardNumber();
			//storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.SECURITY_CODE);
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
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		/*s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		  storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();*/
		s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
		storeFrontAccountTerminationPage.clickConfirmTerminationBtn();
		s_assert.assertFalse(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is still displayed");
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
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
	public void testVerifyUserCanCancelPulseSubscriptionThroughMyAccount_2233() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();  
		List<Map<String, Object>> randomConsultantList =  null;
		//List<Map<String, Object>> randomConsultantPWSList =  null;
		String consultantWithPWSEmailID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Get Consultant with PWS from database
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantWithPWSEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(!consultantPWSURL.contains(".biz"),"Consultant is not on her own .com PWS");
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
		storeFrontAccountInfoPage.clickOnCancelMyCRP();
		//validate CRP has been cancelled..
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontHomePage.clickOnAddToCRPButtonAfterCancelMyCRP();
		s_assert.assertTrue(storeFrontHomePage.verifyEnrollInCRPPopupAfterClickOnAddToCRP(), "Autoship Order get generated After cancel CRP");

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
		randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
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
		else {
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
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
			rcEmailID = (String) getValueFromQueryResult(randomRCList, "Username");
			//   consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			//   accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			//   logger.info("Account Id of the user is "+accountID);

			//   storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID,password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//  randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		//  rcEmailID = (String) getValueFromQueryResult(randomRCList, "Username");
		//  storeFrontHomePage = new StoreFrontHomePage(driver);
		//  storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID,password);
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
		//s_assert.assertTrue(storeFrontHomePage.validateTermsAndConditionsForConsultantApplicationPulse(), "' I have read and accepted all Terms and Conditions for the Consultant Application, Pulse and Policies and Procedures.' is not present"); 
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
		//s_assert.assertTrue(storeFrontHomePage.validateTermsAndConditionsForConsultantApplicationCRP(), "' I have read and accepted all Terms and Conditions for the Consultant Application, CRP and Policies and Procedures.' is not present");
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
		storeFrontAccountTerminationPage.clickConfirmTerminationBtn();
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
		//login Again
		driver.get(driver.getURL()+"/"+driver.getCountry());
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		// Create Adhoc Order For Same Consultant
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		StoreFrontUpdateCartPage storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		//storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
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
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		/*s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		  storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();*/
		s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
		storeFrontAccountTerminationPage.clickConfirmTerminationBtn();
		s_assert.assertFalse(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is still displayed");
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
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
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

		driver.get(driver.getURL()+"/"+driver.getCountry());
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		logger.info("Account Id of the user is "+accountID);

		// Get Account Number
		List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		CCS = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");


		storeFrontHomePage.searchCID(CCS);
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
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String socialInsuranceNumberForReEnrollment = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
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
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
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
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumberForReEnrollment);
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

	//Hybris Project-4821:Consultant To RC downgrade US
	@Test
	public void testConsultantToRCDowngrade_4821() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}else{
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String consultantEmailID = null;
		while(true){
			List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
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
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
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

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");
		//Enter the email address and DO NOT check the "Become a Preferred Customer" checkbox 
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
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

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");
		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME,consultantEmailID, password);
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
		storeFrontRCUserPage=storeFrontHomePage.loginAsRCUser(consultantEmailID, password);
		s_assert.assertFalse(storeFrontHomePage.isCurrentURLShowsError(),"Downgraded RC not able to login");  
		s_assert.assertAll();

	}

	// Hybris Project-4822:Consultant to RC downgrade Canada
	@Test
	public void testConsultantToRCDowngrade_4822() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String  consultantEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		country = driver.getCountry();
		String province = null;
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_PERSONAL;   
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			province = TestConstants.PROVINCE_ALBERTA;
		}else{

			kitName = TestConstants.KIT_NAME_PERSONAL;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			province = TestConstants.PROVINCE_US;
		}
		//   kitName = TestConstants.KIT_NAME_PERSONAL;    
		//   addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		//   city = TestConstants.CITY_CA;
		//   postalCode = TestConstants.POSTAL_CODE_CA;
		//   phoneNumber = TestConstants.PHONE_NUMBER_CA;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();

		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME,consultantEmailAddress, password, addressLine1, city,province, postalCode, phoneNumber);
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
			storeFrontHomePage.clickAddToBagButton("us");

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
			storeFrontHomePage.clickAddToBagButton("us");

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

	/*//Hybris Project-1747:To verify edit shipping address functionality in the CRP Edit Autoship template Page
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

		// clear the any mandatory fields for assertion
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
		s_assert.assertTrue(storeFrontHomePage.getErrorMessageForInvalidSSN().contains("Please enter a valid Social Insurance Number")||storeFrontHomePage.getErrorMessageForInvalidSSN().contains("Please enter a valid Social Security Number"), "Please enter a valid Social Insurance/Security Number message not present");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");

		s_assert.assertAll(); 
	}*/

	//Hybris Phase 2-1981:Orders page UI for RC
	@Test
	public void testOrdersPageUIForRCUser_HP2_1981() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> orderStatusList =  null;
		List<Map<String, Object>> orderGrandTotalList =  null;
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
		List<Map<String,Object>> getOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderID"));
		String orderStatusId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderStatusID"));

		/*  //assert for order number with RFO
	     orderNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO,  rcUserEmailAddress),RFO_DB);
	     orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
	     logger.info("Order Number from RFO DB is "+orderNumberDB);
	     s_assert.assertTrue(storeFrontOrdersPage.verifyOrderNumber(orderNumberDB),"Order Number on UI is different from RFO DB");
		 */
		//assert for order status with RFO
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_STATUS, orderStatusId),RFO_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		logger.info("Order Status from RFO DB is "+orderStatusDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrderStatus(orderStatusDB),"Order Status on UI is different from RFO DB");

		//assert for grand total with RFO
		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4287_RFO, orderId),RFO_DB);

		orderGrandTotalDB = String.valueOf(getValueFromQueryResult(orderGrandTotalList, "Total"));
		int grandTotal = Integer.valueOf(orderGrandTotalDB.split("\\.")[0]);
		String gTotal = orderGrandTotalDB.split("\\.")[1];

		if(grandTotal == 0){
			orderGrandTotalDB = "0"+"."+gTotal;
		}
		logger.info("Order GrandTotal from RFO DB is "+orderGrandTotalDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),"Grand total on UI is different from RFO DB");

		//assert for order date with RFO
		orderDateDB = String.valueOf (getValueFromQueryResult(getOrderDetailsList, "CompletionDate"));
		logger.info("Order Scheduled Date from RFO DB is "+orderDateDB);
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
				boolean isLoginError = driver.getCurrentUrl().contains("error");
				if(isLoginError){
					logger.info("Login error for the user "+consultantEmailID);
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
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		/*s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		  storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();*/
		s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
		storeFrontAccountTerminationPage.clickConfirmTerminationBtn();
		s_assert.assertFalse(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is still displayed");
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
		//storeFrontHomePage.applyPriceFilterLowToHigh();
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
		//s_assert.assertTrue(storeFrontHomePage.verifyInvalidSponsorPopupIsPresent(), "Invalid Sponsor popup is not present");
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
		if(country.equalsIgnoreCase("us"))
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName,TestConstants.LAST_NAME, emailAddress, password, addressLine1, city,TestConstants.PROVINCE_ALABAMA_US, postalCode, phoneNumber);
		else
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName,TestConstants.LAST_NAME, emailAddress, password, addressLine1, city,TestConstants.PROVINCE_CA, postalCode, phoneNumber);
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
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
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
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(), "Account Info page has not been displayed");
		String firstName = TestConstants.CONSULTANT_FIRST_NAME_FOR_ACCOUNT_INFORMATION+randomNum;
		String lastName = TestConstants.CONSULTANT_LAST_NAME_FOR_ACCOUNT_INFORMATION;
		String addressProfileName = firstName+" "+lastName;
		storeFrontAccountInfoPage.updateAccountInformation(firstName, lastName, TestConstants.CONSULTANT_ADDRESS_LINE_1_FOR_ACCOUNT_INFORMATION,city,postalCode,phoneNumber);
		//storeFrontAccountInfoPage.handleSpouseDetail();

		//assert First Name with RFO
		accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, addressProfileName), RFO_DB);
		firstNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "FirstName");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyFirstNameFromUIForAccountInfo(firstNameDB), "First Name on UI is different from DB");

		// assert Last Name with RFO
		lastNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "LastName");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyLasttNameFromUIForAccountInfo(lastNameDB), "Last Name on UI is different from DB");

		// assert Address Line 1 with RFO
		//  accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, consultantEmailID), RFO_DB);
		//  addressLine1DB = (String) getValueFromQueryResult(accountAddressDetailsList, "AddressLine1");
		//  assertTrue("Address Line 1 on UI is different from DB", storeFrontAccountInfoPage.verifyAddressLine1FromUIForAccountInfo(addressLine1DB));


		// assert City with RFO
		accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ADDRESS_RFO, addressProfileName), RFO_DB);
		cityDB = (String) getValueFromQueryResult(accountAddressDetailsList, "Locale");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCityFromUIForAccountInfo(cityDB), "City on UI is different from DB");

		// assert State with RFO
		provinceDB = (String) getValueFromQueryResult(accountAddressDetailsList, "Region");
		/*if(provinceFromDB.equalsIgnoreCase("TX")){
	       provinceDB = "Texas"; 
	      }*/
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProvinceFromUIForAccountInfo(provinceDB), "Province on UI is different from DB");

		//assert Postal Code with RFO
		postalCodeDB = (String) getValueFromQueryResult(accountAddressDetailsList, "PostalCode");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyPostalCodeFromUIForAccountInfo(postalCodeDB), "Postal Code on UI is different from DB");

		// assert Main Phone Number with RFO
		//  mainPhoneNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_PHONE_NUMBER_QUERY_RFO, consultantEmailID), RFO_DB);
		//  mainPhoneNumberDB = (String) getValueFromQueryResult(mainPhoneNumberList, "PhoneNumberRaw");
		//  assertTrue("Main Phone Number on UI is different from DB", storeFrontAccountInfoPage.verifyMainPhoneNumberFromUIForAccountInfo(mainPhoneNumberDB));

		genderDB = String.valueOf(getValueFromQueryResult(accountNameDetailsList, "GenderId"));
		if(genderDB.equals("2")){
			genderDB = "male";
		}
		if(genderDB.equals("1")){
			genderDB = "female";
		}
		if(genderDB.equals("3"))
			genderDB = "others";

		s_assert.assertTrue(storeFrontAccountInfoPage.verifyGenderFromUIAccountInfo(genderDB), "Gender on UI is different from DB");

		// assert BirthDay with RFO
		dobDB = String.valueOf(getValueFromQueryResult(accountNameDetailsList, "BirthDay"));
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyBirthDateFromUIAccountInfo(dobDB), "DOB on UI is different from DB");  

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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
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
		List<Map<String,Object>> getOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderID"));
		String orderStatusId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderStatusID"));
		//assert for order status with RFO
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_STATUS, orderStatusId),RFO_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		logger.info("Order Status from RFO DB is "+orderStatusDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrderStatus(orderStatusDB, orderHistoryNumber),"Order Status on UI is different from RFO DB");

		//assert for grand total with RFO
		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4287_RFO, orderId),RFO_DB);
		DecimalFormat dff = new DecimalFormat("#.00");
		orderGrandTotalDB = String.valueOf(dff.format(getValueFromQueryResult(orderGrandTotalList, "Total"))); 
		logger.info("Order GrandTotal from RFO DB is "+orderGrandTotalDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),"Grand total on UI is different from RFO DB");

		//assert for order date with RFO
		orderDateDB = String.valueOf (getValueFromQueryResult(getOrderDetailsList, "CompletionDate"));
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
		if(driver.getCountry().equalsIgnoreCase("ca")){
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyConsultantCantShipToQuebecMsg()," Consultants cannot ship to Quebec message not present on UI");
		}
		s_assert.assertAll();
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
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
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
		String shippingAddressFromUI =null;
		String subTotalUI = null;
		String shippingUI = null;
		String handlingUI = null;
		String taxUI = null;	
		String grandTotalUI = null;
		String shippingMethodUI = null;

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

		// Get Order Number for assert
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		// get order details before cancel CRP
		shippingAddressFromUI = storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate();
		subTotalUI = storeFrontOrdersPage.getSubTotalFromAutoshipTemplate();
		taxUI = storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate();
		grandTotalUI = storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate();
		shippingUI = storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate();
		handlingUI = storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate();
		shippingMethodUI = storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate();

		storeFrontOrdersPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyCRP();
		//validate CRP has been cancelled..
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");

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
		s_assert.assertTrue(shippingAddressFromUI.trim().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+shippingAddressFromUI);

		//Assert Subtotal with RFO
		s_assert.assertTrue(subTotalUI.trim().contains(subTotalDB.trim()),"CRP autoship template subTotal on RFO is "+subTotalDB+" and on UI is "+subTotalUI);

		// Assert Tax with RFO
		s_assert.assertTrue(taxUI.trim().contains(taxDB.trim()),"CRP autoship template tax amount on RFO is "+taxDB+" and on UI is "+taxUI);

		// Assert Grand Total with RFO
		s_assert.assertTrue(grandTotalUI.trim().contains(grandTotalDB.trim()),"CRP autoship template grand total on RFO is "+grandTotalDB+" and on UI is "+grandTotalUI);

		// assert shipping amount with RFO
		s_assert.assertTrue(shippingUI.trim().contains(shippingDB.trim()),"CRP autoship template shipping amount on RFO is "+shippingDB+" and on UI is "+shippingUI);

		// assert Handling Value with RFO
		s_assert.assertTrue(handlingUI.trim().contains(handlingDB.trim()),"CRP autoship template handling amount on RFO is "+handlingDB+" and on UI is "+handlingUI);

		// assert for shipping Method with RFO
		s_assert.assertTrue(shippingMethodUI.trim().contains(shippingMethodDB.trim()),"CRP autoship template shipping method on RFO is "+shippingMethodDB+" and on UI is "+shippingMethodUI);

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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
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
				s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$21.00"),"Shipping charges on UI is not As per shipping method selected");
				s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
			}

		}else{
			logger.info("Order total is not in required range");
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
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
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
			country = driver.getCountry();
			List<Map<String, Object>> randomConsultantList =  null;
			List<Map<String, Object>> sponsorIdList = null;
			List<Map<String, Object>> consultantSponsorIdList = null;
			String firstConsultantEmailID = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
			int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
			String secondConsultantEmailID = TestConstants.FIRST_NAME+randomNumber+TestConstants.EMAIL_ADDRESS_SUFFIX;
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REVERSE;
			if(driver.getCountry().equalsIgnoreCase("us")){
				kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
				addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
				city = TestConstants.NEW_ADDRESS_CITY_US;
				postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
				phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
			}else{
				kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
				addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
				city = TestConstants.CITY_CA;
				postalCode = TestConstants.POSTAL_CODE_CA;
				phoneNumber = TestConstants.PHONE_NUMBER_CA;
			}
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
			storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
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
			storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
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
			storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
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
			storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
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
			storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
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
		List<Map<String,Object>> getOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderID"));
		String orderStatusId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderStatusID"));

		//assert for order status with RFO
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_STATUS, orderStatusId),RFO_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		logger.info("Order Status from RFO DB is "+orderStatusDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrderStatus(orderStatusDB, orderHistoryNumber),"Order Status on UI is different from RFO DB");

		//assert for grand total with RFO
		DecimalFormat dff = new DecimalFormat("#.00");
		orderGrandTotalDB = String.valueOf(dff.format(getValueFromQueryResult(getOrderDetailsList, "Total"))); 
		logger.info("Order GrandTotal from RFO DB is "+orderGrandTotalDB);
		s_assert.assertTrue(storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),"Grand total on UI is different from RFO DB");

		//assert for order date with RFO
		orderDateDB = String.valueOf (getValueFromQueryResult(getOrderDetailsList, "CompletionDate"));
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
		storeFrontUpdateCartPage.clickUpdateCartBtn();

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
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
		storeFrontHomePage.clickOnYourAccountDropdown();
		storeFrontHomePage.clickOnAutoshipStatusLink();
		if(storeFrontAccountInfoPage.validateSubscribeToPulse()==true){
			storeFrontHomePage.cancelPulseSubscription();
		}

		s_assert.assertTrue(storeFrontAccountInfoPage.validatePulseCancelled(),"pulse subscription is not cancelled for the user");
		storeFrontAccountInfoPage.clickOnSubscribeToPulseBtn();

		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnAccountInfoNextButton();

		subTotal = storeFrontUpdateCartPage.getSubtotalFromCart();
		shipping = storeFrontUpdateCartPage.getDeliveyFromCart();
		handling = storeFrontUpdateCartPage.getHandlingFromCart();
		tax = storeFrontUpdateCartPage.getTaxFromCart();
		grandTotal = storeFrontUpdateCartPage.getGrandTotalFromCart();
		storeFrontUpdateCartPage.clickOnSubscribePulseTermsAndConditionsChkbox();
		storeFrontUpdateCartPage.clickOnSubscribeBtn();

		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumberOfPulse();

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
	public void testAdhocOrdersForMultiplsLineItem_2273() throws InterruptedException  {
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
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
		//storeFrontHomePage.applyPriceFilterLowToHigh();
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
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
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
		//s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
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

	//Hybris Project-2132:Verify The Mini Functionality
	@Test
	public void testMiniFunctionality_2132() throws InterruptedException{
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		//storeFrontHomePage.applyPriceFilterLowToHigh();
		String selectedProductName=storeFrontHomePage.getProductName();
		storeFrontHomePage.selectProductAndProceedToBuy();


		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//click on continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();
		//verify the number of products in mini cart
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInMiniCart("1"),"number of products in the mini cart is not 1");

		//click on product link for product detail page
		storeFrontHomePage.clickProductLinkForProductDetail();

		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProductName),"Product name is not as expected");

		//navigate back to quick shop page
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		storeFrontHomePage.updateProductQuantityOnModalWindowAndProceedToBuy("3");
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("4"), "number of products in the cart is NOT 4");
		//click on continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();

		//click on mini cart icon
		storeFrontHomePage.clickMiniCart();
		//remove all products from mini cart
		storeFrontHomePage.removeFirstProductFromTheCart();
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("0"), "number of products in the cart is NOT 0");
		storeFrontHomePage.clickOnContinueShoppingLinkOnEmptyShoppingCartPage();
		//storeFrontHomePage.verifyNumberOfProductsInMiniCart("0");
		s_assert.assertAll();	

	}

	//Hybris Project-2170:Login as Existing Consultant and Place an Adhoc Order - check Alert Message
	@Test
	public void testCreateAdhocOrderConsultant_2170() throws InterruptedException{
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
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage=new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnPlaceOrderButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"checkout confirmation popup not present");
		storeFrontUpdateCartPage.clickOnConfirmationOK();
		storeFrontUpdateCartPage.verifyConsultantIsAbleToContinueCheckoutProcess();
		s_assert.assertAll();
	}

	//Hybris Project-2113:Reduce the Quantity
	@Test
	public void testReduceTheQuantity_2113() throws InterruptedException{
		String qtyIncrease = "2";
		String qtyReduce = "1";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.clickAddToBagButton();
		double subTotalOfAddedProduct = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		storeFrontHomePage.addQuantityOfProduct(qtyIncrease);
		s_assert.assertTrue(storeFrontHomePage.validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(),"update message not coming as expected");
		double subTotalOfAfterUpdate = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		s_assert.assertTrue(storeFrontHomePage.verifySubTotalAccordingToQuantity(qtyIncrease,subTotalOfAddedProduct,subTotalOfAfterUpdate),"subTotal is not updated with increased quantity");
		storeFrontHomePage.addQuantityOfProduct(qtyReduce);
		s_assert.assertTrue(storeFrontHomePage.validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(),"update message not coming as expected");
		double subTotalAfterReduce = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		s_assert.assertTrue(storeFrontHomePage.verifySubTotalAccordingToQuantity(qtyReduce,subTotalOfAddedProduct,subTotalAfterReduce),"subTotal is not updated with reduced quantity");
		s_assert.assertAll();	  

	}

	//Hybris Project-2120:Increase the Quantity
	@Test
	public void testIncreseTheQuantity_2120() throws InterruptedException{
		String qtyIncrease = "2";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.clickAddToBagButton();
		double subTotalOfAddedProduct = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		storeFrontHomePage.addQuantityOfProduct(qtyIncrease);
		s_assert.assertTrue(storeFrontHomePage.validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(),"update message not coming as expected");
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains("Product quantity has been updated."),"update message not coming as expected");
		double subTotalOfAfterUpdate = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		s_assert.assertTrue(storeFrontHomePage.verifySubTotalAccordingToQuantity(qtyIncrease,subTotalOfAddedProduct,subTotalOfAfterUpdate),"subTotal is not updated with increased quantity");
		s_assert.assertAll();
	}

	//Hybris Project-2121:Remove Product from the cart
	@Test
	public void testRemoveProductFromTheCart_2121() throws InterruptedException{
		String qtyIncrease = "2";
		String qtyReduce = "1";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.clickAddToBagButton();
		double subTotalOfAddedProduct = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		storeFrontHomePage.addQuantityOfProduct(qtyIncrease);
		s_assert.assertTrue(storeFrontHomePage.validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(),"update message not coming as expected");
		double subTotalOfAfterUpdate = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		s_assert.assertTrue(storeFrontHomePage.verifySubTotalAccordingToQuantity(qtyIncrease,subTotalOfAddedProduct,subTotalOfAfterUpdate),"subTotal is not updated with increased quantity");
		storeFrontHomePage.addQuantityOfProduct(qtyReduce);
		s_assert.assertTrue(storeFrontHomePage.validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(),"update message not coming as expected");
		double subTotalAfterReduce = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		s_assert.assertTrue(storeFrontHomePage.verifySubTotalAccordingToQuantity(qtyReduce,subTotalOfAddedProduct,subTotalAfterReduce),"subTotal is not updated with reduced quantity");
		s_assert.assertAll();

	}

	//Hybris Project-2293 withoutloggin in add product to cart--> click ContinueShopping
	@Test
	public void testWithOutLoginQuickShopScreen_2293() throws InterruptedException	{
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();

		//validate quick-shop screen
		s_assert.assertTrue(storeFrontHomePage.validateQuickShopScreen(),"QuickShop page is not displayed");
		s_assert.assertAll();
	}


	// Hybris Project-2292:Continue Shopping - logged in
	@Test
	public void testQuickShopScreenWithRegisteredUser_2292() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//For Consultant
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
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();
		//validate quick-shop screen
		s_assert.assertTrue(storeFrontHomePage.validateQuickShopScreen(),"QuickShop page is not displayed");
		logout();

		//For PC User
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
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
		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();
		//validate quick-shop screen
		s_assert.assertTrue(storeFrontHomePage.validateQuickShopScreen(),"QuickShop page is not displayed");
		logout();

		//For RC User
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
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
		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();
		//validate quick-shop screen
		s_assert.assertTrue(storeFrontHomePage.validateQuickShopScreen(),"QuickShop page is not displayed");

		s_assert.assertAll();
	}

	//Hybris Project-2307:Product Listing Scren should have Buy Now button --> click Buy Now--> product is added to the cart
	@Test 
	public void testProductListHaveBuyNowButtonAndProductAddedToCart_2307() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and verify Buy Now Button.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Select a product  and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());
		//login as RC User And Verify Buy Now Button.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Select a product  and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());
		//login as consultant and verify Buy Now Button.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("loginError for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Select a product  and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//verify Buy now button for all users without login.
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button without login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Select a product  and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
	}

	//Hybris Project-2309:Add Product froom Quick Info Screen
	@Test
	public void testAddProductFromQuickInfoScreen_2309() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and Add Product From Quick Info Screen
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		storeFrontHomePage.clickAddToBagButtonOnQuickInfoPopup();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as RC User And Verify Buy Now Button.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		storeFrontHomePage.clickAddToBagButtonOnQuickInfoPopup();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as consultant and verify Buy Now Button.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		storeFrontHomePage.clickAddToBagButtonOnQuickInfoPopup();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
	}

	//Hybris Project-2323:Check Buy Now Link
	@Test
	public void testCheckBuyNowLink_2323() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and verify Buy Now Button.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());


		//login as RC User And Verify Buy Now Button.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as consultant and verify Buy Now Button.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		//verify add to bag button on quick info popup 
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify add to bag button on product detail page
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
	}

	//Hybris Project-2319:Check Quick Info Screen layout and informations
	@Test
	public void testVerifyQuickInfoScreenLayoutAndProductDetails_2319() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and verify Buy Now Button.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String selectedProductName=storeFrontHomePage.getProductName();
		String selectedProductPrice=storeFrontHomePage.getProductPrice();

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProductName),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(selectedProductPrice),"Product price is not as expected");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());


		//login as RC User And Verify Buy Now Button.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String selectedProduct=storeFrontHomePage.getProductName();
		String priceOfProduct=storeFrontHomePage.getProductPrice();
		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProduct),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(priceOfProduct),"Product price is not as expected");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as consultant and verify Buy Now Button.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String ProductName=storeFrontHomePage.getProductName();
		String productPrice=storeFrontHomePage.getProductPrice();
		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		//verify add to bag button on quick info popup 
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(ProductName),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(productPrice),"Product price is not as expected");
	}

	// Hybris Project-3844:Verify links in Meet Your consultant Banner
	@Test
	public void testVerifyLinksInMeetYourConsultantBanner_3844() {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
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
		//click meet your consultant banner link
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		//validate we are navigated to "Meet your Consultant" page on .COM
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
		s_assert.assertAll();
	}

	//Hybris Project-3847:Verify Footer Links on .COM home Page
	@Test
	public void testVerifyFooterLinksOnHomePage_3847()	 {
		//Navgate to app home page
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//validate RF Connection link
		s_assert.assertTrue(storeFrontHomePage.validateRFConnectionLink(),"RF Connection link is not redirecting to proper screen");
		//validate Carrers link
		s_assert.assertTrue(storeFrontHomePage.validateCareersLink(),"Careers link is not redirecting to proper screen");
		//validtae Contact-Us Link
		s_assert.assertTrue(storeFrontHomePage.validateContactUsLink(),"Contact Us link is not redirecting to proper screen");
		//validate Disclaimer link
		s_assert.assertTrue(storeFrontHomePage.validateDisclaimerLink(),"Disclaimer link is not redirecting to proper screen");
		//validate Privacy-policy link
		s_assert.assertTrue(storeFrontHomePage.validatePrivacyPolicyLink(),"PrivacyPolicy link is not redirecting to proper screen");
		//validate Satisfaction-Guarantee link
		s_assert.assertTrue(storeFrontHomePage.validateSatisfactionGuaranteeLink(),"SatisfactionGuarantee link is not redirecting to proper screen");
		//validate Terms&Conditions link
		s_assert.assertTrue(storeFrontHomePage.validateTermsConditionsLink(),"TermsConditions link is not redirecting to proper screen");
		//validate pressroom link
		s_assert.assertTrue(storeFrontHomePage.validatePressRoomLink(),"PressRoom link is not redirecting to proper screen");
		//validate country flag dropdown
		s_assert.assertTrue(storeFrontHomePage.validateCountryFlagDropDownBtn(),"Country flag Drop down button is not present in UI on homepage");
		//validate Derm RF Link
		s_assert.assertTrue(storeFrontHomePage.validateDermRFLink()," Derm RF link is not redirecting to proper screen");
		s_assert.assertAll();
	}

	//Hybris Project-3845:Verify the Links in the top Navigation on .COM home Page
	@Test
	public void testVerifyLinksInTheTopNavigationOnHomePage_3845()	 {
		//Navgate to app home page
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//click on logo and validate the homepage
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.validateHomePage(),"Home page is not displayed ");
		//click login  link and validate username,password,login button & forgot pwd link
		s_assert.assertTrue(storeFrontHomePage.validateLoginFunctionality(),"All the Elements of login functionality are not displayed!");
		//validate 'shop','About' &'Become a Consultant' Menu 
		s_assert.assertTrue(storeFrontHomePage.validateTopNavigationMenuElements(),"Top Menu Navigation Elements are not displayed/present");
		//validate country flag dropdown
		s_assert.assertTrue(storeFrontHomePage.validateCountryFlagDropDownBtn(),"Country flag Drop down button is not present in UI on homepage");
		s_assert.assertAll();
	}

	//Hybris Project-4663:Consultant Account Termination
	@Test
	public void testConsultantAccountTermination_4663() throws InterruptedException{
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
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();

		//1st combination for validation
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyFieldValidatonForReason(),"Field Validation Is not Present for Reason");
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyMessageWithoutComments(),"comment is required message not present on UI without select any reason");

		storeFrontAccountTerminationPage.selectTerminationReason();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();

		//2nd combination for validation
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyMessageWithoutComments(),"comment is required message not present on UI without select any reason");

		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.enterTerminationComments();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();

		// 3rd combination for validation
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyFieldValidatonForReason(),"Field Validation Is not Present for Reason After enter the comments");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.selectTerminationReason();
		storeFrontAccountTerminationPage.enterTerminationComments();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();

		//4th combination for validation
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyCheckBoxValidationIsPresent(),"Please click on checkbox to agree on the Terms and Conditions not present on UI");
		storeFrontAccountTerminationPage.clickOnAgreementCheckBox();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is Present");
		storeFrontAccountTerminationPage.clickCancelTerminationButton();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyWelcomeDropdownToCheckUserRegistered(),"Account is Terminated");
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		storeFrontHomePage.loginAsRCUser(consultantEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed"); 

		s_assert.assertAll();
	}

	//Hybris Project-4665:consultant re-enrollment within 6 month
	@Test
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
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			consultantEmailID= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
			String bizPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
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
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCrpStatusAfterReactivation(),"CRP Status is present");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyPulseStatusAfterReactivation(currentPulseStatus),"pulse status is not same as old status");
		//  storeFrontHomePage.clickNextButton();
		s_assert.assertAll();
	}

	//Hybris Project-4803:PC account termination for US new PC
	@Test
	public void testPCAccountTermination_4803() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		// Click on our product link that is located at the top of the page and then click in on quick shop
		/*storeFrontHomePage.clickOnShopLink();
		   storeFrontHomePage.clickOnAllProductsLink();*/
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		//  storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		//  s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String emailAddress = firstName+randomNum+"@xyz.com";
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password,emailAddress);

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
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is	"+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver); 
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontPCUserPage.clickOnYourAccountDropdown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
		storeFrontPCUserPage.cancelMyPCPerksAct();
		storeFrontHomePage.loginAsPCUser(emailAddress, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll(); 
	}

	//Hybris Project-2324:Category Product List page
	@Test
	public void testVerifyCategoryProductListPage_2324() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(1,3);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User User and verify Product Details and category.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//verify the different categories and verify the products in present in each category.
		s_assert.assertTrue(storeFrontHomePage.verifyRedefineCategory(),"Redefine category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInRedefineCategory(),"No Product is there in redefine category");
		s_assert.assertTrue(storeFrontHomePage.verifyReverseCategory(),"Reverse category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInReverseCategory(),"No Product is there in reverse category");
		s_assert.assertTrue(storeFrontHomePage.verifySootheCategory(),"Soothe category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInSootheCategory(),"No Product is there in Soothe category");
		s_assert.assertTrue(storeFrontHomePage.verifyUnblemishCategory(),"Unblemish category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInUnblemishCategory(),"No Product is there in Unblemish category");
		if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(storeFrontHomePage.verifyEssentialCategory(),"Essentials category is not present");
			s_assert.assertTrue(storeFrontHomePage.verifyProductsInEssentialCategory(),"No Product is there in Essentials category");
		}
		s_assert.assertTrue(storeFrontHomePage.verifyEnhancementCategory(),"Enhancement category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInEnhancementCategory(),"No Product is there in Enhancement category");

		//Select Random Product in Redefine Section And verify Its Details on product list page.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(randomNum),"Add to Bag Button is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyRetailPriceOfProduct(randomNum),"Retail price of product is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyMyPriceOfProduct(randomNum),"My price of Product is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToPCPerksButton(randomNum),"Add to PC Perks button is not present");
		s_assert.assertAll();
	}

	//Hybris Project-2325:CAtegory Product List for Consultant
	@Test
	public void testVerifyCategoryProductListPage_2325() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(1,6);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As Consultant User and verify Product Details and category.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//verify the different categories and verify the products in present in each category.
		s_assert.assertTrue(storeFrontHomePage.verifyRedefineCategory(),"Redefine category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInRedefineCategory(),"No Product is there in redefine category");
		s_assert.assertTrue(storeFrontHomePage.verifyReverseCategory(),"Reverse category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInReverseCategory(),"No Product is there in reverse category");
		s_assert.assertTrue(storeFrontHomePage.verifySootheCategory(),"Soothe category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInSootheCategory(),"No Product is there in Soothe category");
		s_assert.assertTrue(storeFrontHomePage.verifyUnblemishCategory(),"Unblemish category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInUnblemishCategory(),"No Product is there in Unblemish category");
		if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(storeFrontHomePage.verifyEssentialCategory(),"Essentials category is not present");
			s_assert.assertTrue(storeFrontHomePage.verifyProductsInEssentialCategory(),"No Product is there in Essentials category");
		}
		s_assert.assertTrue(storeFrontHomePage.verifyEnhancementCategory(),"Enhancement category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInEnhancementCategory(),"No Product is there in Enhancement category");

		//Select Random Product in Redefine Section And verify Its Details on product list page.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(randomNum),"Add to Bag Button is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyRetailPriceOfProduct(randomNum),"Retail price of product is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyMyPriceOfProduct(randomNum),"My price of Product is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToCRPButton(randomNum),"Add to CRP button is not present");
		s_assert.assertTrue(storeFrontHomePage.verifySVValue(randomNum),"SV Value for consultant is not present");
		s_assert.assertAll();
	}

	// Hybris Project-2326:Category Product List for Retail Customers & PC
	@Test
	public void testVerifyCategoryProductListPage_2326() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(1,6);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As RC User User and verify Product Details and category.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//verify the different categories and verify the products in present in each category.
		s_assert.assertTrue(storeFrontHomePage.verifyRedefineCategory(),"Redefine category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInRedefineCategory(),"No Product is there in redefine category");
		s_assert.assertTrue(storeFrontHomePage.verifyReverseCategory(),"Reverse category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInReverseCategory(),"No Product is there in reverse category");
		s_assert.assertTrue(storeFrontHomePage.verifySootheCategory(),"Soothe category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInSootheCategory(),"No Product is there in Soothe category");
		s_assert.assertTrue(storeFrontHomePage.verifyUnblemishCategory(),"Unblemish category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInUnblemishCategory(),"No Product is there in Unblemish category");
		if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(storeFrontHomePage.verifyEssentialCategory(),"Essentials category is not present");
			s_assert.assertTrue(storeFrontHomePage.verifyProductsInEssentialCategory(),"No Product is there in Essentials category");
		}
		s_assert.assertTrue(storeFrontHomePage.verifyEnhancementCategory(),"Enhancement category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInEnhancementCategory(),"No Product is there in Enhancement category");

		//Select Random Product in Redefine Section And verify Its Details on product list page.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(randomNum),"Add to Bag Button is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyRetailPriceOfProduct(randomNum),"Retail price of product is not present");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());
		//login As PC User User and verify Product Details and category.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//verify the different categories and verify the products in present in each category.
		s_assert.assertTrue(storeFrontHomePage.verifyRedefineCategory(),"Redefine category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInRedefineCategory(),"No Product is there in redefine category");
		s_assert.assertTrue(storeFrontHomePage.verifyReverseCategory(),"Reverse category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInReverseCategory(),"No Product is there in reverse category");
		s_assert.assertTrue(storeFrontHomePage.verifySootheCategory(),"Soothe category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInSootheCategory(),"No Product is there in Soothe category");
		s_assert.assertTrue(storeFrontHomePage.verifyUnblemishCategory(),"Unblemish category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInUnblemishCategory(),"No Product is there in Unblemish category");
		if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(storeFrontHomePage.verifyEssentialCategory(),"Essentials category is not present");
			s_assert.assertTrue(storeFrontHomePage.verifyProductsInEssentialCategory(),"No Product is there in Essentials category");
		}
		s_assert.assertTrue(storeFrontHomePage.verifyEnhancementCategory(),"Enhancement category is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductsInEnhancementCategory(),"No Product is there in Enhancement category");

		//Select Random Product in Redefine Section And verify Its Details on product list page.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(randomNum),"Add to Bag Button is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyRetailPriceOfProduct(randomNum),"Retail price of product is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyMyPriceOfProduct(randomNum),"My price of Product is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToPCPerksButton(randomNum),"Add to PC Perks button is not present");
		s_assert.assertAll();
	}

	//Hybris Project-4648:Cancellation Message for PC account Termination
	@Test
	public void testCancellationMessageforPCaccountTermination_4648() throws InterruptedException{
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
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
		storeFrontAccountInfoPage.clickOndelayOrCancelPCPerks();
		s_assert.assertTrue(storeFrontAccountInfoPage.isYesChangeMyAutoshipDateButtonPresent(),"Yes Change My Autoship Date is Not Presnt on UI");
		s_assert.assertTrue(storeFrontAccountInfoPage.isCancelPCPerksLinkPresent(),"Cancel PC Perks link Not Present");
		s_assert.assertAll();
	}

	//Hybris Project-4765:Consultant can choose a default PWS prefix upon enrolling in Pulse
	@Test
	public void testConsultantCanChooseDefaultPWSPrefixUponEnrollingInPulse_4765() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_CONSULTANT_NO_PWS_RFO,RFO_DB);
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
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyPulseSubscription();
		storeFrontAccountInfoPage.clickOnOnlySubscribeToPulseBtn();

		s_assert.assertTrue(storeFrontAccountInfoPage.verifyWebsitePrefixSuggestionIsPresent(), "There are no suggestions for website prefix");
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

	//Hybris Project-4766:Consultant can choose a custom PWS prefix upon enrolling in Pulse
	@Test
	public void testVerifyConsultantCanChooseCustomPWSPrefixUponEnrollingInPulse_4766() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_CONSULTANT_NO_PWS_RFO,RFO_DB);
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
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyPulseSubscription();
		storeFrontAccountInfoPage.clickOnOnlySubscribeToPulseBtn();
		int randomNum = CommonUtils.getRandomNum(1000, 100000);
		String websitePrefixName = TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage.enterWebsitePrefixName(websitePrefixName);
		//storeFrontAccountInfoPage.clickOnNextDuringPulseSubscribtion();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyWebsitePrefixSuggestionIsPresent(), "There are no suggestions for website prefix");
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

	// Hybris Project-4647:PC Account Termination
	@Test
	public void testPCAccountTermination_4647() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(1,6);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User User and verify Product Details and category.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontPCUserPage.clickOnYourAccountDropdown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
		//verify account termination reasons present on dropdown.
		s_assert.assertTrue(storeFrontPCUserPage.getAccountterminationReasonTooMuchProduct().equalsIgnoreCase(TestConstants.TOO_MUCH_PRODUCT),"Too Much product option is not present");
		s_assert.assertTrue(storeFrontPCUserPage.getAccountterminationReasonTooExpensive().equalsIgnoreCase(TestConstants.TOO_EXPENSIVE),"Too Much Expensive option is not present");
		s_assert.assertTrue(storeFrontPCUserPage.getAccountterminationReasonEnrolledInAutoShipProgram().equalsIgnoreCase(TestConstants.ENROLLED_IN_AUTOSHIP_PROGRAM),"Did not know i was enrolled in autoship program option is not present");
		s_assert.assertTrue(storeFrontPCUserPage.getAccountterminationReasonProductNotRight().equalsIgnoreCase(TestConstants.PRODUCT_NOT_RIGHT),"Products were not right for me option is not present");
		s_assert.assertTrue(storeFrontPCUserPage.getAccountterminationReasonUpgradingToConsultant().equalsIgnoreCase(TestConstants.UPGRADING_TO_CONSULTANT),"I am upgrading to a Consultant option is not present");
		s_assert.assertTrue(storeFrontPCUserPage.getAccountterminationReasonReceiveProductTooOften().equalsIgnoreCase(TestConstants.RECEIVE_PRODUCT_TOO_OFTEN),"I received products too often option is not present");
		s_assert.assertTrue(storeFrontPCUserPage.getAccountterminationReasonDoNotWantToObligated().equalsIgnoreCase(TestConstants.DO_NOT_WANT_TO_OBLIGATED_TO_ORDER_PRODUCT),"Do not want to be obligated to order product option is not present");
		s_assert.assertTrue(storeFrontPCUserPage.getAccountterminationReasonOther().equalsIgnoreCase(TestConstants.OTHER_REASON),"Other option is not present");

		//assert Check Send cancellation message section 
		s_assert.assertTrue(storeFrontPCUserPage.verifyToSectionInSendcancellationMessageSection(),"To option in not present in send cancellation message section");
		s_assert.assertTrue(storeFrontPCUserPage.verifySubjectSectionInSendcancellationMessageSection(),"Subject option in not present in send cancellation message section");
		s_assert.assertTrue(storeFrontPCUserPage.verifyMessageSectionInSendcancellationMessageSection(),"Message option in not present in send cancellation message section");

		//assert Check 2 buttons at the bottom of the page
		s_assert.assertTrue(storeFrontPCUserPage.verifyIHaveChangedMyMindButton(),"To option in not present in send cancellation message section");
		s_assert.assertTrue(storeFrontPCUserPage.verifySendAnEmailToCancelAccountButton(),"Subject option in not present in send cancellation message section");

		//continue with pc User Account termination process.
		storeFrontPCUserPage.cancelMyPCPerksAct();
		storeFrontHomePage.loginAsConsultant(pcUserEmailID, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();	
	}


	//Hybris Project-2317:heck View Product Details button from Quick Info pop-up
	@Test
	public void testViewProductDetailsFromQuickInfoPopup_2317() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(1,4);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and Verify product Details.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String selectedProduct=storeFrontHomePage.getProductName(randomNum);
		String priceOfProduct=storeFrontHomePage.getProductPrice(randomNum);

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo(randomNum);

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToPCPerksButtonOnQuickInfoPopup(),"Add to PC Perks Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProduct),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToPCPerksButtonOnProductDetailPage(),"Add to PC Perks Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(priceOfProduct),"Product price is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyProductLongDescription(),"Product Description is not present on product detail page");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as RC User And Verify Product Details.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String selectedProductName=storeFrontHomePage.getProductName(randomNum);
		String selectedProductPrice=storeFrontHomePage.getProductPrice(randomNum);

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo(randomNum);

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProductName),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(selectedProductPrice),"Product price is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyProductLongDescription(),"Product Description is not present on product detail page");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());


		//login as consultant and verify Product Details.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String ProductName=storeFrontHomePage.getProductName(randomNum);
		String productPrice=storeFrontHomePage.getProductPrice(randomNum);

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo(randomNum);

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToCRPButtonOnQuickInfoPopup(),"Add to CRP Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(ProductName),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToCRPButtonOnProductDetailPage(),"Add to CRP Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(productPrice),"Product price is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyProductLongDescription(),"Product Description is not present on product detail page");
		s_assert.assertAll(); 
	}

	//Hybris Project-2316:check Product Details APge Layout and information
	@Test
	public void testVerifyProductDetailsFromQuickInfoPopup_2316() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(1,6);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and Verify product Details.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String selectedProduct=storeFrontHomePage.getProductName(randomNum);
		String priceOfProduct=storeFrontHomePage.getProductPrice(randomNum);

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo(randomNum);

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToPCPerksButtonOnQuickInfoPopup(),"Add to PC Perks Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProduct),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToPCPerksButtonOnProductDetailPage(),"Add to PC Perks Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(priceOfProduct),"Product price is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyProductLongDescription(),"Product Description is not present on product detail page");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as RC User And Verify Product Details.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String selectedProductName=storeFrontHomePage.getProductName(randomNum);
		String selectedProductPrice=storeFrontHomePage.getProductPrice(randomNum);

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo(randomNum);

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProductName),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(selectedProductPrice),"Product price is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyProductLongDescription(),"Product Description is not present on product detail page");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as consultant and verify Product Details.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String ProductName=storeFrontHomePage.getProductName(randomNum);
		String productPrice=storeFrontHomePage.getProductPrice(randomNum);

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo(randomNum);

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToCRPButtonOnQuickInfoPopup(),"Add to CRP Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(ProductName),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToCRPButtonOnProductDetailPage(),"Add to CRP Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(productPrice),"Product price is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyProductLongDescription(),"Product Description is not present on product detail page");
		s_assert.assertAll(); 
	}

	//Hybris Project-2262:View QV/SV value in the cart
	@Test
	public void testCreateAdhocOrderConsultantAndVerifySVValue_2262() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);

		//login as consultant and verify Product Details.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		//Place order.
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		s_assert.assertTrue(storeFrontUpdateCartPage.verifySVValueOnCartPage(),"SV Value is not present on cart page");
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifySVValueOnOrderSummaryPage(),"SV Value is not present on order summary page");
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifySVValueOnOrderConfirmationPage(), "SV Value is not present on order confirmation page");
		//String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");

		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.verifySVValueOnOrderPage(),"Product SV Value is not present on order page.");
		s_assert.assertAll();
	}

	//Hybris Project-3823:Verify Top Nav as Logged in Consultant(.com)
	@Test
	public void testVerifyTopNavLoggedInConsultant_3823() throws InterruptedException	 {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
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
		//validate(after logging as consultant) on homapage CRP cart is displayed
		s_assert.assertTrue(storeFrontConsultantPage.validateCRPCartDisplayed(), "CRP Cart is not displayed");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate CRP Cart is displayed?
		s_assert.assertTrue(storeFrontConsultantPage.validateCRPCartDisplayed(), "CRP Cart is not displayed");
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontConsultantPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontConsultantPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}

	//Hybris Project-3841:Verify Top Nav as Logged in Consultant(.biz)
	@Test
	public void testVerifyTopNavLoggedInConsultantbizSite_3841() throws InterruptedException{
		country = driver.getCountry();
		env = driver.getEnvironment(); 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		RFO_DB = driver.getDBNameRFO();
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
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
		//validate(after logging as consultant) on homapage CRP cart is displayed
		s_assert.assertTrue(storeFrontConsultantPage.validateCRPCartDisplayed(), "CRP Cart is not displayed");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate CRP Cart is displayed?
		s_assert.assertTrue(storeFrontConsultantPage.validateCRPCartDisplayed(), "CRP Cart is not displayed");
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontConsultantPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontConsultantPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}

	//Hybris Project-3824:Verify Top Nav as Logged in PC User(.com)
	@Test
	public void testVerifyTopNavLoggedInPCUser_3824() throws InterruptedException{
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

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
		//validate(after logging as PC) on homapage PC Perks cart is displayed
		s_assert.assertTrue(storeFrontPCUserPage.validatePCPerkCartDisplayed(), "CRP Cart is not displayed");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate PC Perks Cart is displayed?
		s_assert.assertTrue(storeFrontPCUserPage.validatePCPerkCartDisplayed(), "CRP Cart is not displayed");
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontPCUserPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontPCUserPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}

	//Hybris Project-3842:Verify Top Nav as Logged in PC User(.biz)
	@Test
	public void testVerifyTopNavLoggedInPCUserbizSite_3842() throws InterruptedException	{
		country = driver.getCountry();
		env = driver.getEnvironment(); 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		RFO_DB = driver.getDBNameRFO();
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

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
		//validate(after logging as PC) on homapage PC Perks cart is displayed
		s_assert.assertTrue(storeFrontPCUserPage.validatePCPerkCartDisplayed(), "CRP Cart is not displayed");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate PC Perks Cart is displayed?
		s_assert.assertTrue(storeFrontPCUserPage.validatePCPerkCartDisplayed(), "CRP Cart is not displayed");
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontPCUserPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontPCUserPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}


	//Hybris Project-3840:Verify Top Nav as Logged in RC User(.biz)
	@Test
	public void testVerifyTopNavLoggedInRCUserbizSite_3840() throws InterruptedException	{
		country = driver.getCountry();
		env = driver.getEnvironment(); 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		RFO_DB = driver.getDBNameRFO();
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontRCUserPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontRCUserPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}

	//Hybris Project-3819 Verify links in meet your consultant banner
	@Test
	public void testVerifyLinksInMeetYourConsultantBanner_3819() {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
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
		//click meet your consultant banner link
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		//validate we are navigated to "Meet your Consultant" page on .COM
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
		s_assert.assertAll();
	}

	//Hybris Project-3825:Verify Top Nav as Logged in RC User(.com)
	@Test
	public void testVerifyTopNavLoggedInRCUser_3825() throws InterruptedException{
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//validate home page
		s_assert.assertTrue(storeFrontRCUserPage.validateHomePage(),"home -page is not displayed");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontRCUserPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontRCUserPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}


	//Hybris Project-3765:RC2PC User - Place Adhoc Order from spsonsor's PWS site check Sposnor of Order
	@Test
	public void testPlaceAdhocOrderFromSponsorPWSSiteCheckSponsorOfOrder_3765() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		// create RC User
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

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String emailAddress = firstName+randomNum+"@xyz.com";
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, emailAddress, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC();
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
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(), "Yes I want to join pc perks checkboz is not selected");


		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);

		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String accountnumber = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountnumber);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		//Validate welcome PC Perks Message
		s_assert.assertTrue(storeFrontHomePage.isWelcomePCPerksMessageDisplayed(), "welcome PC perks message should be displayed");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		String pws = driver.getCurrentUrl();
		System.out.println("before  "+pws);

		logout();
		storeFrontHomePage.openPWS(pws);
		//storeFrontHomePage.openPWSSite(driver.getCountry(), driver.getEnvironment());
		storeFrontHomePage.loginAsPCUser(emailAddress, password);
		// again placed adhoc order
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		String pwsAfterOrderPlaced = driver.getCurrentUrl();
		System.out.println("After "+pwsAfterOrderPlaced);
		s_assert.assertTrue(storeFrontHomePage.verifyUrlAfterplacedAnAdhocOrder(pws, pwsAfterOrderPlaced), "pws before and are not equal");
		s_assert.assertAll(); 
	}

	// Hybris Project-2305:Filter Product based on Price
	@Test
	public void testFilterProductBasedOnPrice_2305() throws InterruptedException{
		storeFrontHomePage = new StoreFrontHomePage(driver);

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String priceBeforeApplyFilter = storeFrontHomePage.getProductPriceBeforeApplyFilter();

		// apply filter low to high
		storeFrontHomePage.applyPriceFilterLowToHigh();
		s_assert.assertTrue(storeFrontHomePage.verifyPriceFromLowToHigh(), "Prices are not in format from low to high");

		// apply filter high to low
		storeFrontHomePage.applyPriceFilterHighToLow();
		s_assert.assertTrue(storeFrontHomePage.verifyPriceFromHighTolow(), "Prices are not in format from high to low");

		//deselect the price filter
		storeFrontHomePage.deselectPriceFilter();
		s_assert.assertTrue(storeFrontHomePage.verifyPriceAfterDeselectThefilter(priceBeforeApplyFilter), "Price is not as before after deselect the filter");

		s_assert.assertAll(); 

	}

	//Hybris Project-2306:Filter Product on Category
	@Test
	public void testFilterProductOnCategory_2306(){
		storeFrontHomePage = new StoreFrontHomePage(driver);

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		String productNameBeforeApplyFilter = storeFrontHomePage.getProductNameBeforeApplyProductFilter();

		int sizeOfProductFilter = storeFrontHomePage.getSizeOfProductFilter();
		for(int i=1; i<=sizeOfProductFilter; i++){
			s_assert.assertTrue(storeFrontHomePage.verifyProductFilterIsApply(i), "Product name is not similar as product filter"+" "+i );
		}

		s_assert.assertTrue(storeFrontHomePage.verifyProductNameAfterRemoveProductFilter(productNameBeforeApplyFilter), "Product name is not same as before after remove filter");
		s_assert.assertAll();
	}


	// Hybris Project-3766:RC2PC User - Place Adhoc Order from Different PWS site check Sposnor of Order
	@Test
	public void testPlaceAdhocOrderFromDifferentPWSCheckSponsorOfOrder_3766() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		// create RC User
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

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String emailAddress = firstName+randomNum+"@xyz.com";
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, emailAddress, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC();
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
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(), "Yes I want to join pc perks checkboz is not selected");


		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);

		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String accountnumber = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountnumber);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		//Validate welcome PC Perks Message
		s_assert.assertTrue(storeFrontHomePage.isWelcomePCPerksMessageDisplayed(), "welcome PC perks message should be displayed");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		String pws = driver.getCurrentUrl();
		System.out.println("before  "+pws);

		logout();
		storeFrontHomePage.openPWSSite(driver.getCountry(), driver.getEnvironment());
		storeFrontHomePage.loginAsPCUser(emailAddress, password);
		// again placed adhoc order
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		String pwsAfterOrderPlaced = driver.getCurrentUrl();
		System.out.println("After "+pwsAfterOrderPlaced);
		s_assert.assertTrue(storeFrontHomePage.verifyUrlAfterplacedAnAdhocOrder(pws, pwsAfterOrderPlaced), "pws before and are not equal");
		s_assert.assertAll(); 
	}

	//Hybris Project-3822:Verify Footer Links on .COM home Page
	@Test
	public void testVerifyFooterLinksOnHomePage_3822(){
		//Navgate to app home page
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//validate RF Connection link
		s_assert.assertTrue(storeFrontHomePage.validateRFConnectionLink(),"RF Connection link is not redirecting to proper screen");
		//validate Carrers link
		s_assert.assertTrue(storeFrontHomePage.validateCareersLink(),"Careers link is not redirecting to proper screen");
		//validtae Contact-Us Link
		s_assert.assertTrue(storeFrontHomePage.validateContactUsLink(),"Contact Us link is not redirecting to proper screen");
		//validate Disclaimer link
		s_assert.assertTrue(storeFrontHomePage.validateDisclaimerLink(),"Disclaimer link is not redirecting to proper screen");
		//validate Privacy-policy link
		s_assert.assertTrue(storeFrontHomePage.validatePrivacyPolicyLink(),"PrivacyPolicy link is not redirecting to proper screen");
		//validate Satisfaction-Guarantee link
		s_assert.assertTrue(storeFrontHomePage.validateSatisfactionGuaranteeLink(),"SatisfactionGuarantee link is not redirecting to proper screen");
		//validate Terms&Conditions link
		s_assert.assertTrue(storeFrontHomePage.validateTermsConditionsLink(),"TermsConditions link is not redirecting to proper screen");
		//validate pressroom link
		s_assert.assertTrue(storeFrontHomePage.validatePressRoomLink(),"PressRoom link is not redirecting to proper screen");
		//validate country flag dropdown
		s_assert.assertTrue(storeFrontHomePage.validateCountryFlagDropDownBtn(),"Country flag Drop down button is not present in UI on homepage");
		//validate Derm RF Link
		s_assert.assertTrue(storeFrontHomePage.validateDermRFLink()," Derm RF link is not redirecting to proper screen");
		s_assert.assertAll();
	}

	//Hybris Project-3820:Verify the Links in the top Navigation on .COM home Page
	@Test
	public void testVerifyLinksInTheTopNavigationOnHomePage_3820()	{
		//Navgate to app home page
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//click on logo and validate the homepage
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.validateHomePage(),"Home page is not displayed ");
		//click login  link and validate username,password,login button & forgot pwd link
		s_assert.assertTrue(storeFrontHomePage.validateLoginFunctionality(),"All the Elements of login functionality are not displayed!");
		//validate 'shop','About' &'Become a Consultant' Menu 
		s_assert.assertTrue(storeFrontHomePage.validateTopNavigationMenuElements(),"Top Menu Navigation Elements are not displayed/present");
		//validate country flag dropdown
		s_assert.assertTrue(storeFrontHomePage.validateCountryFlagDropDownBtn(),"Country flag Drop down button is not present in UI on homepage");
		s_assert.assertAll();
	}	

	//Hybris Project-3768:Update PCPerk Template from Different PWS site(Other Than Sponsor's PWS)
	@Test
	public void testUpdatePCPerkTemplateFromDifferentPWS_3768() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		// create RC User
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

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String emailAddress = firstName+randomNum+"@xyz.com";
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, emailAddress, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC();
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
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(), "Yes I want to join pc perks checkboz is not selected");


		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);

		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String accountnumber = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountnumber);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		//Validate welcome PC Perks Message
		s_assert.assertTrue(storeFrontHomePage.isWelcomePCPerksMessageDisplayed(), "welcome PC perks message should be displayed");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		String pws = driver.getCurrentUrl();
		logout();
		storeFrontHomePage.openPWS(pws);
		//storeFrontHomePage.openPWSSite(driver.getCountry(), driver.getEnvironment());
		storeFrontHomePage.loginAsPCUser(emailAddress, password);
		storeFrontHomePage.clickOnAutoshipCart();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
		storeFrontUpdateCartPage.selectAProductAndAddItToPCPerks();
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
		storeFrontUpdateCartPage.clickUpdateCartBtn();

		//validate cart has been updated?
		s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(), "cart is not updated!! ");
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		String pwsAfterUpdateCart = driver.getCurrentUrl();
		System.out.println("After "+pwsAfterUpdateCart);
		s_assert.assertTrue(storeFrontHomePage.verifyUrlAfterplacedAnAdhocOrder(pws, pwsAfterUpdateCart), "pws before and are not equal");

		s_assert.assertAll(); 
	}

	//Hybris Project-2267:Login as Existing PC and Place an Adhoc Order, Check for Alert message
	@Test
	public void testExistingPCPlaceAnAdhocOrderAndCheckForAlertMessage_2267() throws InterruptedException{
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
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}  

		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontPCUserPage.clickAddToBagButton();
		storeFrontPCUserPage.clickOnPlaceOrderButton();
		s_assert.assertTrue(storeFrontPCUserPage.verifyCheckoutConfirmationPOPupPresent(),"Checkout Confirmation pop up not present");
		s_assert.assertTrue(storeFrontPCUserPage.verifyCheckoutConfirmationPopUpMessagePC(),"Checkout Confirmation pop up message is not present as expected");
		storeFrontPCUserPage.clickOnOkButtonOnCheckoutConfirmationPopUp();
		s_assert.assertTrue(storeFrontPCUserPage.verifyAccountInfoPageHeaderPresent(),"Account info page header is not present");
		s_assert.assertAll();
	}

	//Hybris Project-2264:Check Savings Per Product as a PC user
	@Test
	public void testSavingsPerProductAsAPCUser_2264() throws InterruptedException{
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

		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();

		//assert Retail price on products page
		s_assert.assertTrue(storeFrontPCUserPage.verifyRetailPriceIsAvailableOnProductsPage(),"Retail Price is not available");

		//assert your price on products page
		s_assert.assertTrue(storeFrontPCUserPage.verifyYourPriceIsAvailableOnProductsPage(),"Your Price is not available");
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnBuyNowButton();

		//assert Retail price on Adhoc cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyRetailPriceIsAvailableOnAdhocCart(),"Retail Price is not available on Adhoc cart");

		//assert your price on Adhoc cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyYourPriceIsAvailableOnAdhocCart(),"Your Price is not available on Adhoc cart");
		storeFrontUpdateCartPage.clickOnCheckoutButton();

		//assert total saving on Adhoc cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyTotalSavingsIsAvailableOnAdhocCart(),"Total Savings is not available on Adhoc cart");

		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontUpdateCartPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnAddToPcPerksButton();

		//assert Retail price on Autoship cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyRetailPriceIsAvailableOnAutoshipCart(),"Retail Price is not available on Autoship cart");

		//assert your price on Autoship cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyYourPriceIsAvailableOnAutoshipCart(),"Your Price is not available on Autoship cart");

		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();

		//assert total saving on Autoship cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyTotalSavingsIsAvailableOnAutoshipCart(),"Total Savings is not available on Autoship cart");

		s_assert.assertAll();
	}

	// Hybris Project-4718: RC 2 PC , PC to consultant under Same & Different sponsor
	@Test
	public void testRCToPcAndPCToConsultantUnderSameAndDifferentSponsor_4718() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName =  TestConstants.REGIMEN_NAME_REVERSE;

		kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
		if(driver.getCountry().equalsIgnoreCase("ca")){   
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		} else{
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US; 
		}
		// create RC User
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

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String rcEmailAddress = firstName+randomNum+"@xyz.com";
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, rcEmailAddress, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		// Get Canadian sponser with PWS from database
		List<Map<String, Object>> sponserList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		String sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));

		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
		String sponsorId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponsorId);
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
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(), "Yes I want to join pc perks checkboz is not selected");
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponsorId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		//Validate welcome PC Perks Message
		s_assert.assertTrue(storeFrontHomePage.isWelcomePCPerksMessageDisplayed(), "welcome PC perks message should be displayed");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		//Upgrade pe to consultant
		driver.get(driver.getURL()+"/"+"ca");
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		// Get Canadian sponser with PWS from database
		sponserList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));

		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
		String sponsorIdForConsultant = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		storeFrontHomePage.searchCID(sponsorIdForConsultant);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);		
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(rcEmailAddress);
		s_assert.assertTrue(storeFrontHomePage.verifyUpradingToConsulTantPopup(), "Upgrading to a consultant popup is not present");
		storeFrontHomePage.enterPasswordForUpgradePcToConsultant();
		storeFrontHomePage.clickOnLoginToTerminateToMyPCAccount();
		storeFrontHomePage.enterEmailAddress(rcEmailAddress);
		storeFrontHomePage.clickOnEnrollUnderLastUpline();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);		
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(rcEmailAddress);
		storeFrontHomePage.enterFirstName(firstName);
		storeFrontHomePage.enterLastName(lastName);
		storeFrontHomePage.enterEmailAddress(rcEmailAddress);
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
		s_assert.assertAll(); 

	}


	//Hybris Project-2170:Login as Existing Consultant and Place an Adhoc Order - check Alert Message
	@Test
	public void testExistingConsultantPlaceAnAdhocOrderAndCheckForAlertMessage_2170() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);

		//login as consultant and verify Product Details.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontConsultantPage.clickAddToBagButton();
		storeFrontConsultantPage.clickOnPlaceOrderButton();
		s_assert.assertTrue(storeFrontConsultantPage.verifyCheckoutConfirmationPOPupPresent(),"Checkout Confirmation pop up not present");
		s_assert.assertTrue(storeFrontConsultantPage.verifyCheckoutConfirmationPopUpMessageConsultant(),"Checkout Confirmation pop up message is not present as expected");

		storeFrontConsultantPage.clickOnOkButtonOnCheckoutConfirmationPopUp();
		s_assert.assertTrue(storeFrontConsultantPage.verifyAccountInfoPageHeaderPresent(),"Account info page header is not present");
		s_assert.assertAll();

	}

	//Hybris Project-2318:PC Perks Message
	@Test
	public void testVerifyPCPerksMessageOnModalPopup_2318() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country = driver.getCountry();
			RFO_DB = driver.getDBNameRFO();
			storeFrontHomePage = new StoreFrontHomePage(driver);

			// Click on our product link that is located at the top of the page and then click in on quick shop
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");

			//Mouse hover product and click quick info
			storeFrontHomePage.mouseHoverProductAndClickQuickInfo();
			//Assert for modal window
			s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
			//verify pc perks message on modal popup
			s_assert.assertTrue(storeFrontHomePage.getPCPerksMessageFromModalPopup().contains(TestConstants.PC_PERKS_MESSAGE_ON_MODAL_POPUP),"PC Perks message is not comming on modal popup");
			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-3898:Downgrade to RC on the Payment / Order summary page
	@Test
	public void testDowngradePCToRCAtPaymentOrderSummaryPage_3898() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			country = driver.getCountry();
			int randomNum =  CommonUtils.getRandomNum(10000, 1000000);
			env = driver.getEnvironment();
			String firstName = TestConstants.FIRST_NAME+randomNum;
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = TestConstants.LAST_NAME+randomNum;
			String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			String PWS = storeFrontHomePage.getBizPWS(country, env);
			PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
			storeFrontHomePage.openPWS(PWS);
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
			storeFrontHomePage.selectProductAndProceedToBuy();
			storeFrontHomePage.clickOnCheckoutButton();
			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");
			storeFrontHomePage.enterNewPCDetails(firstName, lastName, password, emailAddress);
			//Enter main account info
			storeFrontHomePage.enterMainAccountInfo();
			storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
			storeFrontHomePage.clickOnShippingAddressNextStepBtn();
			//Enter billing info
			storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
			storeFrontHomePage.selectNewBillingCardExpirationDate();
			storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.selectNewBillingCardAddress();
			storeFrontHomePage.clickOnSaveBillingProfile();
			//Uncheck PC Perks Checkbox on Payment/order summary page
			s_assert.assertTrue(storeFrontHomePage.validatePCPerksCheckBoxIsDisplayed(),"PC Perks checkbox is not present");
			s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is not selected");
			storeFrontHomePage.checkPCPerksCheckBox();
			storeFrontHomePage.clickOnShippingAddressNextStepBtn();
			s_assert.assertFalse(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is selected");
			storeFrontHomePage.clickOnBillingNextStepBtn();
			storeFrontHomePage.clickPlaceOrderBtn();
			s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(),"Order is not placed successfully");
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			storeFrontHomePage.clickOnWelcomeDropDown();
			s_assert.assertFalse(storeFrontHomePage.verifyEditPcPerksIsPresentInWelcomDropdownForUpgrade(),"Edit Pc Perks Link is present for RC User");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-3900:Downgrade to RC on the Review / Order summary page
	@Test
	public void testDowngradePCToRCAtReviewOrderSummaryPage_3900() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			country = driver.getCountry();
			int randomNum =  CommonUtils.getRandomNum(10000, 1000000);
			env = driver.getEnvironment();
			String firstName = TestConstants.FIRST_NAME+randomNum;
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = TestConstants.LAST_NAME+randomNum;
			String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			String PWS = storeFrontHomePage.getBizPWS(country, env);
			PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
			storeFrontHomePage.openPWS(PWS);
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
			storeFrontHomePage.selectProductAndProceedToBuy();
			storeFrontHomePage.clickOnCheckoutButton();
			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");
			storeFrontHomePage.enterNewPCDetails(firstName, lastName, password, emailAddress);
			storeFrontHomePage.enterMainAccountInfo();
			storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
			storeFrontHomePage.clickOnShippingAddressNextStepBtn();
			//Enter billing info
			storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
			storeFrontHomePage.selectNewBillingCardExpirationDate();
			storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.selectNewBillingCardAddress();
			storeFrontHomePage.clickOnSaveBillingProfile();
			storeFrontHomePage.clickOnBillingNextStepBtn();
			//Uncheck PC Perks Checkbox on Review/order summary page
			s_assert.assertTrue(storeFrontHomePage.validatePCPerksCheckBoxIsDisplayed(),"PC Perks checkbox is not present");
			s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is not selected");
			storeFrontHomePage.checkPCPerksCheckBox();
			storeFrontHomePage.clickOnShippingAddressNextStepBtn();
			storeFrontHomePage.clickOnBillingNextStepBtn();
			s_assert.assertFalse(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is selected");
			storeFrontHomePage.clickPlaceOrderBtn();
			s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(),"Order is not placed successfully");
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			storeFrontHomePage.clickOnWelcomeDropDown();
			s_assert.assertFalse(storeFrontHomePage.verifyEditPcPerksIsPresentInWelcomDropdownForUpgrade(),"Edit Pc Perks Link is present for RC User");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}

	}

	// Hybris Project-3896:Downgrade to RC on the Main Account page
	@Test
	public void testDowngradePCToRCAtMainAccountInfoPage_3900() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			country = driver.getCountry();
			int randomNum =  CommonUtils.getRandomNum(10000, 1000000);
			env = driver.getEnvironment();
			String firstName = TestConstants.FIRST_NAME+randomNum;
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = TestConstants.LAST_NAME+randomNum;
			String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			String PWS = storeFrontHomePage.getBizPWS(country, env);
			PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
			storeFrontHomePage.openPWS(PWS);
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
			storeFrontHomePage.selectProductAndProceedToBuy();
			storeFrontHomePage.clickOnCheckoutButton();
			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");
			storeFrontHomePage.enterNewPCDetails(firstName, lastName, password, emailAddress);
			//Uncheck PC Perks Checkbox on Main Account Info/order summary page
			s_assert.assertTrue(storeFrontHomePage.validatePCPerksCheckBoxIsDisplayed(),"PC Perks checkbox is not present");
			s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is not selected");
			storeFrontHomePage.checkPCPerksCheckBox();
			s_assert.assertFalse(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is selected");
			//Enter main account info
			storeFrontHomePage.enterMainAccountInfo();
			storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
			storeFrontHomePage.clickOnShippingAddressNextStepBtn();
			//Enter billing info
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
			storeFrontHomePage.clickOnWelcomeDropDown();
			s_assert.assertFalse(storeFrontHomePage.verifyEditPcPerksIsPresentInWelcomDropdownForUpgrade(),"Edit Pc Perks Link is present for RC User");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	// Hybris Project-139:PC Perks - Delay
	@Test
	public void testDelayPCPerks_139() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO(); 
			List<Map<String, Object>> accountNameDetailsList = null;
			List<Map<String, Object>> accountInfoDetailsList = null;
			List<Map<String, Object>> delayCancellationLogDetailsList = null;
			List<Map<String, Object>> delayCountAndNextScheduleDetailsList = null;
			country = driver.getCountry();
			int randomNum = CommonUtils.getRandomNum(1,6);
			storeFrontHomePage = new StoreFrontHomePage(driver);
			//login As PC User and Verify product Details.
			List<Map<String, Object>> randomPCUserList =  null;
			String pcUserEmailID = null;
			String accountIdForPCUser = null;
			String firstNameDB = null;
			String lastNameDB = null;
			String accountId = null;
			String autoshipDelayCancellationLog = null;
			String nextScheduleDate = null;
			String delayCount = null;
			String autoshipNumber = null;
			while(true){
				randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
				accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
				logger.info("Account Id of the user is "+accountIdForPCUser);

				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
				boolean isError = driver.getCurrentUrl().contains("error");
				if(isError){
					logger.info("login error for the user "+pcUserEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}
			logger.info("login is successful");
			//Verify Order Page
			storeFrontPCUserPage.clickOnWelcomeDropDown();
			storeFrontOrdersPage=storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Order Page Is not Displayed");
			//verify autoship order section on order page.
			s_assert.assertTrue(storeFrontOrdersPage.verifyAutoshipOrderSectionOnOrderPage(),"Autoship order section is not on order page for pc user");
			//verify Order History section on order page.
			s_assert.assertTrue(storeFrontOrdersPage.verifyOrderHistorySectionOnOrderPage(),"Order history section is not on order page for pc user");
			//verify Return order section on order page.
			s_assert.assertTrue(storeFrontOrdersPage.verifyReturnOrderSectionOnOrderPage(),"Return order section is not on order page for pc user ");

			//Verify Delay And Cancel PC Perks Message
			storeFrontPCUserPage.clickOnYourAccountDropdown();
			storeFrontPCUserPage.clickOnPCPerksStatus();
			s_assert.assertTrue(storeFrontPCUserPage.verifyPCPerksStatus(),"PC perks status page is not present");
			storeFrontPCUserPage.clickDelayOrCancelPCPerks();
			storeFrontPCUserPage.clickChangeMyAutoshipDateButton();
			s_assert.assertTrue(storeFrontPCUserPage.verifyNextAutoshipDateRadioButtons(),"Next AutoShip Date radio button are not present");
			String nextBillDate=storeFrontPCUserPage.getNextBillAndShipDate();
			String dateAfterOneMonth=storeFrontPCUserPage.getOneMonthOutDate(nextBillDate);
			String dateAfterTwoMonth=storeFrontPCUserPage.getOneMonthOutDate(dateAfterOneMonth);
			String dateAfterThreeMonth=storeFrontPCUserPage.getOneMonthOutDate(dateAfterTwoMonth);

			//assert for date after one month and after two month
			s_assert.assertTrue(storeFrontPCUserPage.getShipAndBillDateAfterOneMonthFromUI().contains(dateAfterOneMonth),"Date After one month is not as expected");
			s_assert.assertTrue(storeFrontPCUserPage.getShipAndBillDateAfterTwoMonthFromUI().contains(dateAfterTwoMonth),"Date after two month is not as expected");

			//select autoship date one month Later.
			storeFrontPCUserPage.selectFirstAutoshipDateAndClickSave();
			//String autoshipDateFromOrderPage=storeFrontOrdersPage.getAutoshipOrderDate();

			//verify One month later autoship date on order page
			s_assert.assertTrue(storeFrontOrdersPage.verifyAutoShipOrderDate(dateAfterOneMonth),"Next Selected autoship order date is not updated on order page");
			//verify one month later autoship date on PC Perks Status Page
			storeFrontOrdersPage.clickOnYourAccountDropdown();
			storeFrontPCUserPage.clickOnPCPerksStatus();
			s_assert.assertTrue(storeFrontPCUserPage.getNextBillAndShipDate().equalsIgnoreCase(dateAfterOneMonth),"Next Bill and ship date one month later is not the updated one");

			//Verify the updated autoship date record one month later is saved in autoship delay Cancellationlog.
			//get first name and last name from RFO DB based on email address.
			//					accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, pcUserEmailID), RFO_DB);
			//					firstNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "FirstName");
			//					lastNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "LastName");
			//					accountInfoDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ID_BASED_ON_FIRST_LAST_NAME, firstNameDB,lastNameDB), RFO_DB);
			//					accountId = (String) getValueFromQueryResult(accountInfoDetailsList, "AccountId");
			//					delayCancellationLogDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_DELAY_CANCELLATION_LOG_BASED_ON_ACCOUNT_ID,accountId), RFO_DB);
			//					autoshipDelayCancellationLog = (String) getValueFromQueryResult(delayCancellationLogDetailsList, "reason");//Autoship Delayed by 30 days, next due date: 10/06/2017
			//					autoshipNumber = (String) getValueFromQueryResult(delayCancellationLogDetailsList, "templateId");
			//					delayCountAndNextScheduleDetailsList= DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_NEXT_SCHEDULE_DATE_AND_DELAY_COUNT,autoshipNumber), RFO_DB);
			//					nextScheduleDate = (String) getValueFromQueryResult(delayCancellationLogDetailsList, "NextRunDate");
			//					delayCount = (String) getValueFromQueryResult(delayCancellationLogDetailsList, "DelayCount");

			//select autoship date Two month Later.
			storeFrontPCUserPage.clickDelayOrCancelPCPerks();
			storeFrontPCUserPage.clickChangeMyAutoshipDateButton();
			storeFrontPCUserPage.selectSecondAutoshipDateAndClickSave();
			//verify Two month later autoship date on order page
			s_assert.assertTrue(storeFrontOrdersPage.verifyAutoShipOrderDate(dateAfterThreeMonth),"Next Selected autoship order date is not updated on order page");
			//verify two month later autoship date on PC Perks Status Page
			storeFrontOrdersPage.clickOnYourAccountDropdown();
			storeFrontPCUserPage.clickOnPCPerksStatus();
			s_assert.assertTrue(storeFrontPCUserPage.getNextBillAndShipDate().equalsIgnoreCase(dateAfterThreeMonth),"Next Bill and ship date is two month later not the updated one");

			//verify the updated autoship date record two month later is saved in autoship delay Cancellationlog.
			//get first name and last name from RFO DB based on email address.
			//					accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, pcUserEmailID), RFO_DB);
			//					firstNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "FirstName");
			//					lastNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "LastName");
			//					accountInfoDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ID_BASED_ON_FIRST_LAST_NAME, firstNameDB,lastNameDB), RFO_DB);
			//					accountId = (String) getValueFromQueryResult(accountInfoDetailsList, "AccountId");
			//					delayCancellationLogDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_DELAY_CANCELLATION_LOG_BASED_ON_ACCOUNT_ID,accountId), RFO_DB);
			//					autoshipDelayCancellationLog = (String) getValueFromQueryResult(delayCancellationLogDetailsList, "reason");//Autoship Delayed by 30 days, next due date: 10/06/2017
			//					autoshipNumber = (String) getValueFromQueryResult(delayCancellationLogDetailsList, "templateId");
			//					delayCountAndNextScheduleDetailsList= DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_NEXT_SCHEDULE_DATE_AND_DELAY_COUNT,autoshipNumber), RFO_DB);
			//					nextScheduleDate = (String) getValueFromQueryResult(delayCancellationLogDetailsList, "NextRunDate");
			//					delayCount = (String) getValueFromQueryResult(delayCancellationLogDetailsList, "DelayCount");

			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}


	//Hybris Project-2090:10% off, free shipping for terms and conditions for the PC Perks Program
	@Test
	public void testFreeShippingTermsAndConditionsForPCPerks_2090() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String emailAddress = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		// Click on our product link that is located at the top of the page and then click in on quick shop
		/*storeFrontHomePage.clickOnShopLink();
	       storeFrontHomePage.clickOnAllProductsLink();*/
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		//  storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		//  s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password,emailAddress);

		//validate 10% discount for PC User account in order summary section
		s_assert.assertTrue(storeFrontHomePage.validateDiscountForEnrollingAsPCUser(TestConstants.DISCOUNT_TEXT_FOR_PC_USER),"Discount text Checkbox is not checked for pc User");

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
		//validate bill to this card radio button selected under billing profile section
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Bill to this card radio button is not selected under billing profile");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(newBillingProfileName),"Bill to this card radio button is not selected under billing profile");
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopupPresent(),"PC Perks terms and condition popup is not present");
		s_assert.assertTrue(storeFrontHomePage.getPCPerksTermsAndConditionsPopupText().toLowerCase().contains(TestConstants.PC_PERKS_TERMS_CONDITION_POPUP_TEXT.toLowerCase()),"PC perks terms and candition popup text is not as expected");
		storeFrontHomePage.clickPCPerksTermsAndConditionPopupOkay();
		//verify for popup saying select terms and candition to avail 10 percent discount
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(),"Congrats message is not present");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-2267:check Modal window with PCPerks info as RC
	@Test
	public void testCheckModalWindowWithPCPerksInfoAsRC_2267() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}  

		logger.info("login is successful");
		storeFrontRCUserPage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.clickAddToBagButton();
		storeFrontHomePage.clickOnPCPerksPromoLink();
		s_assert.assertTrue(storeFrontHomePage.verifyModalWindowIsPresent(),"Modal Window is not present");
		storeFrontHomePage.clickOnModalWindowCloseIcon();
		storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnOrderSummaryPCPerksPromoLink();
		s_assert.assertTrue(storeFrontHomePage.verifyModalWindowIsPresent(),"Modal Window is not present");
		s_assert.assertAll();
	}

	//Hybris Project-2160:Place and Order and Enroll for PC during Checkout and check disclaimers
	@Test
	public void testPlaceOrderAndEnrollForPCDuringCheckout_2160() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String emailAddress = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		// Click on our product link that is located at the top of the page and then click in on quick shop
		/*storeFrontHomePage.clickOnShopLink();
		       storeFrontHomePage.clickOnAllProductsLink();*/
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		//  storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		//  s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password,emailAddress);

		//validate 10% discount for PC User account in order summary section
		s_assert.assertTrue(storeFrontHomePage.validateDiscountForEnrollingAsPCUser(TestConstants.DISCOUNT_TEXT_FOR_PC_USER),"Discount text Checkbox is not checked for pc User");

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
		//validate bill to this card radio button selected under billing profile section
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Bill to this card radio button is not selected under billing profile");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(newBillingProfileName),"Bill to this card radio button is not selected under billing profile");
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopupPresent(),"PC Perks terms and condition popup is not present");
		s_assert.assertTrue(storeFrontHomePage.getPCPerksTermsAndConditionsPopupText().toLowerCase().contains(TestConstants.PC_PERKS_TERMS_CONDITION_POPUP_TEXT.toLowerCase()),"PC perks terms and candition popup text is not as expected");
		s_assert.assertTrue(storeFrontHomePage.getPCPerksTermsAndConditionsPopupHeaderText().toLowerCase().contains(TestConstants.PC_PERKS_TERMS_CONDITION_POPUP_HEADER_TEXT.toLowerCase()),"PC perks terms and candition popup Header text is not as expected");
		storeFrontHomePage.clickPCPerksTermsAndConditionPopupOkay();
		//verify for popup saying select terms and candition to avail 10 percent discount
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(),"Congrats message is not present");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-145:Update PC Template -EDIT Cart , Shipping info, billing info and Save
	@Test
	public void testUpdatePCTemplate_145() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomPCUserList =  null;
			String pcUserEmailID = null;
			String accountID = null;
			String lastName = "ln";
			String newShipingAddressName = TestConstants.ADDRESS_NAME+randomNum;
			String newBillingAddressName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			while(true){
				randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
				accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
				logger.info("Account Id of the user is "+accountID);
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
				boolean isError = driver.getCurrentUrl().contains("error");
				if(isError){
					logger.info("login error for the user "+pcUserEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}  
			logger.info("login is successful");
			storeFrontPCUserPage.clickOnAutoshipCart();
			storeFrontPCUserPage.clickOnContinueShoppingLink();
			storeFrontPCUserPage.clickOnAddtoPCPerksButton();
			String updatedMsg = storeFrontPCUserPage.getAutoshipTemplateUpdatedMsg();
			s_assert.assertTrue(storeFrontPCUserPage.verifyUpdateCartMessage(updatedMsg),"Autoship Cart has not been Updated");
			storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
			storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
			storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
			storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
			storeFrontUpdateCartPage.clickOnEditDefaultBillingProfile();
			storeFrontUpdateCartPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH_OPTION,TestConstants.CARD_EXP_YEAR_OPTION);
			storeFrontUpdateCartPage.clickOnSaveBillingProfile();
			s_assert.assertTrue(storeFrontUpdateCartPage.verifyErrorMessageForCreditCardSecurityCode(),"Error message for credit card security code is not present");
			storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontUpdateCartPage.clickOnSaveBillingProfile();
			//s_assert.assertTrue(storeFrontUpdateCartPage.verifyBillingProfileIsUpdatedSuccessfully(),"Billing profile is not been updated successfully");
			storeFrontUpdateCartPage.clickOnEditDefaultBillingProfile();
			storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingAddressName);
			storeFrontUpdateCartPage.clickOnSaveBillingProfile();
			s_assert.assertTrue(storeFrontUpdateCartPage.verifyErrorMessageForCreditCardSecurityCode(),"Error message for credit card security code is not present");
			storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			//storeFrontUpdateCartPage.selectNewBillingCardAddress();
			storeFrontUpdateCartPage.clickOnSaveBillingProfile();
			//s_assert.assertTrue(storeFrontUpdateCartPage.verifyBillingProfileIsUpdatedSuccessfully(),"Billing profile is not been updated successfully");
			//verify selected billing profile is default
			s_assert.assertTrue(storeFrontUpdateCartPage.verifySelectedbillingProfileIsDefault(newBillingAddressName),"selected billing profile is not default");
			storeFrontUpdateCartPage.clickOnEditShipping();
			storeFrontUpdateCartPage.clickOnEditForDefaultShippingAddress();
			storeFrontUpdateCartPage.enterNewShippingAddressName(newShipingAddressName+" "+lastName);
			storeFrontUpdateCartPage.clickOnSaveShippingProfileAfterEditDuringEnrollment();
			s_assert.assertTrue(storeFrontUpdateCartPage.verifySelectedShippingAddressIsDefault(newShipingAddressName),"selected shipping address is not default");
			String selectedMethodName = storeFrontUpdateCartPage.selectAndGetShippingMethodName();
			storeFrontUpdateCartPage.clickOnNextStepBtnShippingAddress();
			s_assert.assertTrue(storeFrontUpdateCartPage.verifySelectedShippingMethodNameOnUI(selectedMethodName),"Selected Shipping method name is not present on UI");
			storeFrontUpdateCartPage.clickOnNextStepBtn();
			storeFrontUpdateCartPage.clickUpdateCartBtn();
			s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(),"Your Next cart has been updated message not present on UI");
			s_assert.assertTrue(storeFrontUpdateCartPage.verifyUpdatedAddressPresentUpdateCartPg(newShipingAddressName+" "+lastName),"updated address not present on Updated cart page");
			s_assert.assertAll();

		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}


	//Hybris Project-5138:Verify Change Sponsor functionality for RC
	@Test
	public void testChangeSponsorFunctionalityForRC_5138() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
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

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);

		/*//CheckoutPage is displayed?
	   s_assert.assertTrue(storeFrontHomePage.isCheckoutPageDisplayed(), "Checkout page has NOT displayed");
	   logger.info("Checkout page has displayed");*/

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//enter sponsor by CID
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		logger.info("Account Id of the user is "+accountID);

		// Get Account Number
		List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String sponsorID = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponsorID);

		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNotYourSponsorLink();
		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifySponsorNameContainRFCorporate(), "sponsor Name does not contain RF Corporate");
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
		s_assert.assertTrue(driver.getCurrentUrl().contains("corp"), "After successful");
		s_assert.assertAll(); 
	}

	//Hybris Project-5003:Add Birthday and gender and allow my sponsor information
	@Test
	public void testAddBirthDayAndGenderAndAllowMySponsorInformation_5003() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			storeFrontHomePage = new StoreFrontHomePage(driver);
			List<Map<String, Object>> accountNameDetailsList = null;
			String dobDB = null;
			//login as consultant and verify Product Details.
			List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;
			String firstNameFromUI=null;
			String lastNameFromUI=null;
			String legalName=null;
			String accountIdForConsultant = null;
			while(true){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
				accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				logger.info("Account Id of the user is "+accountIdForConsultant);

				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
				boolean isError = driver.getCurrentUrl().contains("error");
				if(isError){
					logger.info("login error for the user "+consultantEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}
			//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			firstNameFromUI=storeFrontAccountInfoPage.getFirstNameFromAccountInfo();
			lastNameFromUI=storeFrontAccountInfoPage.getLastNameFromAccountInfo();
			legalName=firstNameFromUI+" "+lastNameFromUI; 
			storeFrontAccountInfoPage.enterBirthDateOnAccountInfoPage();
			storeFrontAccountInfoPage.clickOnGenderRadioButton(TestConstants.CONSULTANT_GENDER);
			storeFrontAccountInfoPage.clickOnAllowMySpouseOrDomesticPartnerCheckbox();
			storeFrontAccountInfoPage.enterSpouseFirstName(TestConstants.SPOUSE_FIRST_NAME);
			storeFrontAccountInfoPage.enterSpouseLastName(TestConstants.SPOUSE_LAST_NAME);
			storeFrontAccountInfoPage.clickOnSaveAfterEnterSpouseDetails();
			//storeFrontAccountInfoPage.clickOnSaveAfterEnterSpouseDetails();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not present on UI");
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, legalName), RFO_DB);
			String genderDB = String.valueOf(getValueFromQueryResult(accountNameDetailsList, "GenderId"));
			if(genderDB.equals("2")){
				genderDB = "male";
			}
			else{
				genderDB = "female";
			}
			assertTrue("Gender on UI is different from DB", storeFrontAccountInfoPage.verifyGenderFromUIAccountInfo(genderDB));
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, legalName), RFO_DB);
			dobDB = String.valueOf(getValueFromQueryResult(accountNameDetailsList, "Birthday"));
			assertTrue("DOB on UI is different from DB", storeFrontAccountInfoPage.verifyEnteredBirthDateFromDB(dobDB,TestConstants.CONSULTANT_DAY_OF_BIRTH,TestConstants.CONSULTANT_MONTH_OF_BIRTH,TestConstants.CONSULTANT_YEAR_OF_BIRTH));  

			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}


	//Hybris Project-5002:Modify Main Phone Number and mobile phone information
	@Test
	public void testModifyMainPhoneNmberAndMobilePhoneInfo_5002() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			storeFrontHomePage = new StoreFrontHomePage(driver);

			String mainPhoneNumberDB = null;
			List<Map<String, Object>> mainPhoneNumberList =  null;
			List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;
			String accountIdForConsultant = null;
			while(true){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
				accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				logger.info("Account Id of the user is "+accountIdForConsultant);
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
				boolean isError = driver.getCurrentUrl().contains("error");
				if(isError){
					logger.info("login error for the user "+consultantEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}
			//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.enterMainPhoneNumber(TestConstants.CONSULTANT_MAIN_PHONE_NUMBER_FOR_ACCOUNT_INFORMATION_CA);
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not present on UI");
			storeFrontAccountInfoPage.enterMobileNumber(TestConstants.PHONE_NUMBER_CA);
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not present on UI");
			mainPhoneNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_PHONE_NUMBER_QUERY_RFO, consultantEmailID), RFO_DB);
			mainPhoneNumberDB = (String) getValueFromQueryResult(mainPhoneNumberList, "PhoneNumberRaw");
			assertTrue("Main Phone Number on UI is different from DB", storeFrontAccountInfoPage.verifyMainPhoneNumberFromUIForAccountInfo(mainPhoneNumberDB));
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-4999:Modify name of the users
	@Test
	public void testModifyNameOfUser_4999() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
			String lastName = "lN"; 
			String lastNameDB = null;
			String firstNameDB = null;
			List<Map<String, Object>> accountNameDetailsList =  null;
			List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;
			String accountIdForConsultant = null;
			while(true){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
				accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				logger.info("Account Id of the user is "+accountIdForConsultant);

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
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.enterNameOfUser(TestConstants.FIRST_NAME+randomNum,lastName);
			storeFrontAccountInfoPage.clickSaveAccountPageInfo();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not present on UI");
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, TestConstants.FIRST_NAME+randomNum+" "+lastName), RFO_DB);
			firstNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "FirstName");
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyFirstNameFromUIForAccountInfo(firstNameDB),"First Name on UI is different from DB");
			// assert Last Name with RFO
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, TestConstants.FIRST_NAME+randomNum+" "+lastName), RFO_DB);
			lastNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "LastName");
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyLasttNameFromUIForAccountInfo(lastNameDB),"Last Name on UI is different from DB" );
			storeFrontAccountInfoPage.clickMeetYourConsultantLink();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyFirstNameAndLastNameAtMeetYourConsultantSection(TestConstants.FIRST_NAME+randomNum,lastName),"First name and last name is not updated at meet your consultant section");
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
			storeFrontHomePage.clickAddToBagButton(driver.getCountry());
			storeFrontHomePage.clickOnCheckoutButton();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyFirstNameAndLastNameAtMainAccountInfoduringPlacingAdhocOrder(TestConstants.FIRST_NAME+randomNum,lastName),"First name and last name is not updated at main account info during placing adhoc order");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-4781:Update PC Template -ADD Cart , Shipping info, billing info and Save
	@Test
	public void testUpdatePCTemplateAddCart_ShippingInfo_billingInfo_Save_4781() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomPCUserList =  null;
			String pcUserEmailID = null;
			String accountID = null;
			String lastName = "lN";
			String newShipingAddressName = TestConstants.ADDRESS_NAME+randomNum;
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String secondNewBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
			String newBillingAddressName = TestConstants.BILLING_ADDRESS_NAME+randomNumber;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			while(true){
				randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
				accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
				logger.info("Account Id of the user is "+accountID);

				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
				boolean isError = driver.getCurrentUrl().contains("error");
				if(isError){
					logger.info("login error for the user "+pcUserEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}  

			logger.info("login is successful");
			storeFrontPCUserPage.clickOnAutoshipCart();
			storeFrontPCUserPage.clickOnContinueShoppingLink();
			storeFrontPCUserPage.clickOnAddtoPCPerksButton();
			String updatedMsg = storeFrontPCUserPage.getAutoshipTemplateUpdatedMsg();
			s_assert.assertTrue(storeFrontPCUserPage.verifyUpdateCartMessage(updatedMsg),"Autoship Cart has not been Updated");
			storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
			storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
			storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
			storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
			String defaultBillingProfile=storeFrontUpdateCartPage.getDefaultSelectedBillingAddressName();
			logger.info("default billing profile is "+defaultBillingProfile);
			//storeFrontUpdateCartPage.clickOnEditDefaultBillingProfile();
			//Add new billing profile
			storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
			storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
			storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
			storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontUpdateCartPage.selectNewBillingCardAddress();
			storeFrontUpdateCartPage.clickOnSaveBillingProfile();
			s_assert.assertTrue(storeFrontUpdateCartPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly created billing address is not present on page");
			//Add new billing address.
			storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
			storeFrontUpdateCartPage.enterNewBillingNameOnCard(secondNewBillingProfileName+" "+lastName);
			storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
			storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontUpdateCartPage.selectNewBillingCardAddress();
			storeFrontUpdateCartPage.clickAddANewAddressLink();
			storeFrontUpdateCartPage.enterNewBillingAddressName(newBillingAddressName+" "+lastName);
			storeFrontUpdateCartPage.enterNewBillingAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
			storeFrontUpdateCartPage.enterNewBillingAddressCity(TestConstants.CITY_CA);
			storeFrontUpdateCartPage.selectNewBillingAddressState();
			storeFrontUpdateCartPage.enterNewBillingAddressPostalCode(TestConstants.POSTAL_CODE_CA);
			storeFrontUpdateCartPage.enterNewBillingAddressPhoneNumber(TestConstants.PHONE_NUMBER_CA);
			storeFrontUpdateCartPage.clickOnSaveBillingProfile();
			s_assert.assertTrue(storeFrontUpdateCartPage.isTheBillingAddressPresentOnPage(secondNewBillingProfileName),"Newly created billing address is not present on page");
			s_assert.assertTrue(storeFrontUpdateCartPage.isBillingProfileIsSelectedByDefault(defaultBillingProfile),"Default billing profile is not as expected");

			//Add new shipping profile
			storeFrontUpdateCartPage.clickOnEditShipping();
			storeFrontUpdateCartPage.clickOnAddANewShippingAddress();
			storeFrontShippingInfoPage = new StoreFrontShippingInfoPage(driver);
			storeFrontShippingInfoPage.enterNewShippingAddressName(newShipingAddressName+" "+lastName);
			storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
			storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.CITY_CA);
			storeFrontShippingInfoPage.selectNewShippingAddressState();
			storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.POSTAL_CODE_CA);
			storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER_CA);
			storeFrontUpdateCartPage.clickOnSaveCRPShippingInfo();
			s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewlyCreatedShippingAddressIsSelectedByDefault(newShipingAddressName));
			s_assert.assertTrue(storeFrontUpdateCartPage.verifySelectedShippingAddressIsDefault(newShipingAddressName),"selected shipping address is not default");
			String selectedMethodName = storeFrontUpdateCartPage.selectAndGetShippingMethodName();
			storeFrontUpdateCartPage.clickOnNextStepBtnShippingAddress();
			s_assert.assertTrue(storeFrontUpdateCartPage.verifySelectedShippingMethodNameOnUI(selectedMethodName),"Selected Shipping method name is not present on UI");
			storeFrontUpdateCartPage.clickOnNextStepBtn();
			storeFrontUpdateCartPage.clickUpdateCartBtn();
			s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(),"Your Next cart has been updated message not present on UI");
			s_assert.assertTrue(storeFrontUpdateCartPage.verifyUpdatedAddressPresentUpdateCartPg(newShipingAddressName+" "+lastName),"updated address not present on Updated cart page");

			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//  Hybris Project-5000:Modify Email Address for User
	@Test
	public void testModifyEmailAddressForUser_5000() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			RFO_DB = driver.getDBNameRFO();
			String newUserName = TestConstants.CONSULTANT_USERNAME_PREFIX+randomNum+"xyz.com";
			List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;
			String accountIdForConsultant = null;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			while(true){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
				accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				logger.info("Account Id of the user is "+accountIdForConsultant);

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
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyEmailAddressOnAccountInfoPage(consultantEmailID),"Email address is not "+consultantEmailID+"");
			storeFrontAccountInfoPage.enterUserName(newUserName);
			storeFrontAccountInfoPage.clickOnSaveAfterEnterSpouseDetails();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not present on UI");
			logout();
			storeFrontHomePage.loginAsConsultant(newUserName, password);
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyEmailAddressOnAccountInfoPage(newUserName),"Email address is not "+newUserName+"");
			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}
	//Hybris Project-3963:Inactive RC email address, during consultant enrollment under same/different sponsor(BIZ & CORP)
	@Test
	public void testInactiveRCEmailAddressDuringConsultantEnrollment_3963() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			List<Map<String, Object>> randomRCList =  null;
			String rcUserEmailID =null;
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName =  TestConstants.REGIMEN_NAME_REVERSE;
			String firstName=TestConstants.FIRST_NAME+randomNum;
			String lastName = "lN";

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

			storeFrontHomePage = new StoreFrontHomePage(driver);
			while(true){
				randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
				rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
				boolean isError = driver.getCurrentUrl().contains("error");
				if(isError){
					logger.info("login error for the user "+rcUserEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			} 

			logger.info("login is successful");
			storeFrontRCUserPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();

			storeFrontAccountTerminationPage.selectTerminationReason();
			storeFrontAccountTerminationPage.enterTerminationComments();
			storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
			storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();

			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is Present");
			storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
			//Enroll consultant with inactive rc user
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);  
			storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
			storeFrontHomePage.enterEmailAddress(rcUserEmailID);
			storeFrontHomePage.enterFirstName(firstName);
			storeFrontHomePage.enterLastName(lastName);
			storeFrontHomePage.enterEmailAddress(rcUserEmailID);
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
			storeFrontHomePage.clickOnWelcomeDropDown();
			storeFrontOrdersPage = storeFrontHomePage.clickOrdersLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderIsPresent(), "Adhoc Order is not present");

			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-1498:Active RC email address, during consultant enrollment under Same & Different sponsor(BIZ and CORP)
	@Test
	public void testActiveRCEnailAddressDuringConsultantEnrollment_1498() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName =  TestConstants.REGIMEN_NAME_REVERSE;
		kitName = TestConstants.KIT_NAME_BIG_BUSINESS; 
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		if(driver.getCountry().equalsIgnoreCase("ca")){   
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		} else{
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US; 
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
		rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  

		//Enroll consultant with inactive rc user
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);  
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(rcUserEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyUpradingToConsulTantPopup(), "Upgrading to a consultant popup is not present");
		storeFrontHomePage.enterPasswordForUpgradeRCToConsultant();
		storeFrontHomePage.clickOnLoginToTerminateToMyRCAccount();
		s_assert.assertTrue(storeFrontHomePage.verifyAccountTerminationMessage(), "Rc user is not terminated successfully");
		s_assert.assertAll();
	}

	//Hybris Project-3958:CORP: Active PC emial id during consultant enrollment under same sponsor.
	@Test
	public void testActivePCEmailIdDuringConsultantEnrollmentUnderSameSponsor_3958() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName =  TestConstants.REGIMEN_NAME_REVERSE;

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			String firstName=TestConstants.FIRST_NAME+randomNum;
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = "lN";

			storeFrontHomePage = new StoreFrontHomePage(driver);

			// Click on our product link that is located at the top of the page and then click in on quick shop
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");

			//Select a product with the price less than $80 and proceed to buy it
			//storeFrontHomePage.applyPriceFilterLowToHigh();
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
			String pcEmailId= storeFrontHomePage.createNewPC(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);

			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");

			storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPC();
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

			logout();
			driver.get(driver.getURL()+"/"+driver.getCountry());
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_NAME_BIG_BUSINESS, TestConstants.REGIMEN_NAME_REVERSE);  
			storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
			storeFrontHomePage.enterEmailAddress(pcEmailId);
			s_assert.assertTrue(storeFrontHomePage.verifyUpradingToConsulTantPopup(), "Upgrading to a consultant popup is not present");
			storeFrontHomePage.enterPasswordForUpgradePcToConsultant();
			storeFrontHomePage.clickOnLoginToTerminateToMyPCAccount();
			s_assert.assertTrue(storeFrontHomePage.verifyAccountTerminationMessage(), "Rc user is not terminated successfully");
			storeFrontHomePage.enterFirstName(firstName);
			storeFrontHomePage.enterLastName(lastName);
			storeFrontHomePage.enterEmailAddress(pcEmailId);
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

			storeFrontHomePage.clickOnWelcomeDropDown();
			storeFrontOrdersPage = storeFrontHomePage.clickOrdersLinkPresentOnWelcomeDropDown();

			// Verify Order is present
			s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderIsPresent(), "Adhoc Order is not present");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	// Hybris Phase 2-4300 :: Version : 1 :: Verify PC autoship template details. 
	@Test
	public void testPCAutoShipTemplateDetails_HP2_4300() throws InterruptedException, SQLException , ClassNotFoundException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
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
			String pcUserEmailID = null;
			String lastName = null;
			String accountId = null;
			String shippingMethodId = null;
			List<Map<String, Object>> randomPCUserList =  null;
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
			storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

			// Get Order Number
			String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
			storeFrontOrdersPage.clickAutoshipOrderNumber();
			s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksAutoShipHeader(),"Order Text is not present on the Page");
			s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
			s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

			//get Autoship Id Fro RFO
			List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
			String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));

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
			s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"PC autoship template subTotal is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

			// Assert Tax with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"PC autoship template tax amount is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

			// Assert Grand Total with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"PC autoship template grand total is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

			// assert shipping amount with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"PC autoship template shipping amount is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

			// assert Handling Value with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"PC autoship template handling amount is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

			// assert for shipping Method with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"PC autoship template shipping method is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}
	// Hybris Project-4359:New autoship date for Reactivated Consultant
	@Test
	public void testAutoshipDateForReactivatedConsultant_4359() throws InterruptedException, Exception{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO(); 
			List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;
			String accountID = null;
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName =  TestConstants.REGIMEN_NAME_REDEFINE;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			country = driver.getCountry();
			while(true){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
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
			storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();
			//Again enroll the consultant with same eamil id
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_CA;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

			// Get Canadian sponser with PWS from database
			List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			String sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));

			// sponser search by Account Number
			List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
			String sponsorId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID(sponsorId);
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

			storeFrontHomePage.clickOnWelcomeDropDown();
			storeFrontOrdersPage = storeFrontHomePage.clickOrdersLinkPresentOnWelcomeDropDown();
			String dueAutoshipDateFromUI = storeFrontOrdersPage.getAutoshipOrderDate();
			String currentPSTDate = storeFrontOrdersPage.getCurrentPSTDate();
			String extendedDueAutoshipDate = storeFrontHomePage.getOneMonthExtendAutoshipDateFromCurrentDate(currentPSTDate);

			s_assert.assertTrue(extendedDueAutoshipDate.contains(dueAutoshipDateFromUI), "Next Autoship date is not after one month");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}
	//Hybris Project-4677:Verify that Country cannot be modified
	@Test
	public void testCountryCannotBeModified_4677() throws InterruptedException  {
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;
			String accountId = null;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontAccountInfoPage=new StoreFrontAccountInfoPage(driver);
			while(true){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
				accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				logger.info("Account Id of the user is "+accountId);
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
			//goto account info page..
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			//validate country can't be modified
			s_assert.assertTrue(storeFrontAccountInfoPage.validateCountryCanOrNotBeModified(),"country can be modified on account info page");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}
	// Hybris Project-4031:from .com  Login as a existing RC and access Solution Tool
	@Test
	public void testLoginAsExstingRCAndAccessSolutionTool_4031(){
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			country = driver.getCountry();
			env = driver.getEnvironment();  
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontRCUserPage=new StoreFrontRCUserPage(driver);
			String PWS = storeFrontHomePage.getBizPWS(country, env);
			PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
			storeFrontHomePage.openPWS(PWS);
			List<Map<String, Object>> randomRCUserList =  null;
			String rcUserEmailID = null;
			String accountIdForRCUser = null;
			while(true){
				randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
				rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
				accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
				logger.info("Account Id of the user is "+accountIdForRCUser);
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
				boolean isError = driver.getCurrentUrl().contains("error");
				if(isError){
					logger.info("login error for the user "+rcUserEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}
			logger.info("login is successful");
			//Access Solution Tool..
			s_assert.assertTrue(storeFrontRCUserPage.validateAccessSolutionTool(),"Solution tool is not giving the expected results");
			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}
	//Hybris Project-1895:To verify the Meet the consultant banner on PWS sites
	@Test
	public void testValidateMeetConsultantBannerPWSSite_1895()  {
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country = driver.getCountry();
			env = driver.getEnvironment();  
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
			storeFrontHomePage.openPWSSite(country, env);

			//click meet your consultant banner link
			storeFrontConsultantPage.clickOnMeetYourConsultantLink();
			//validate we are navigated to "Meet your Consultant" page on .COM
			s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
			//validate 'edit your information' link shouldn't be present as user doesn't logged in..
			s_assert.assertFalse(storeFrontConsultantPage.validateEditYourInformationLink(), "edit your Information link is present in UI");
			s_assert.assertFalse(storeFrontHomePage.verifyConsultantSinceOnMeetYourConsultantPage(), "Consultant since is not present");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}
	//Hybris Project-1904:To Verify the cancel functionality on edit meet the consultant page from com site
	@Test
	public void testCancelFunctionalityOnEditMeetYourConsultantPageComSite_1904()  {
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			country = driver.getCountry();
			env = driver.getEnvironment();  
			storeFrontHomePage = new StoreFrontHomePage(driver);
			String consultantEmailID = null;
			while(true){
				List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
				consultantEmailID = String.valueOf(getValueFromQueryResult(sponserList, "Username"));
				String PWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
				String comPWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
				storeFrontHomePage.openPWS(comPWS);

				//Login with same PWS consultant
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
			//click meet your consultant banner link
			storeFrontConsultantPage.clickOnMeetYourConsultantLink();
			//validate we are navigated to "Meet your Consultant" page
			s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
			//click on 'Personalize My  Profile' link..
			storeFrontHomePage.clickOnPersonalizeMyProfileLink();
			//click on cancel button on 'editConsultantInfo' page
			storeFrontHomePage.clickCancelBtnOnEditConsultantInfoPage();
			//validate we are navigated to "Meet your Consultant" page
			s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-1905:To Verify the Submission Guidelines link on edit meet the consultant page from biz site
	@Test
	public void testSubmissionGuidelinesLinkOnEditMeetTheConsultantPageBizSite_1905()  {
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			country = driver.getCountry();
			env = driver.getEnvironment();  
			storeFrontHomePage = new StoreFrontHomePage(driver);
			String consultantEmailID = null;
			while(true){
				List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
				consultantEmailID = String.valueOf(getValueFromQueryResult(sponserList, "Username"));
				String PWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
				String comPWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
				storeFrontHomePage.openPWS(comPWS);

				//Login with same PWS consultant
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
			//click meet your consultant banner link
			storeFrontConsultantPage.clickOnMeetYourConsultantLink();
			//validate we are navigated to "Meet your Consultant" page
			s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
			//click on 'Personalize My  Profile' link..
			storeFrontHomePage.clickOnPersonalizeMyProfileLink();
			// Click on Submission Guidelines link in the MeetYourConsultant edit information page & validate SG Pdf is open..
			s_assert.assertTrue(storeFrontHomePage.validateSubmissionGuideLinesLink(),"Submission Guoidelines link is not giving the expected results");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	// Hybris Project-1920:To verify the contact us functionality in edit meet the consultant page for com PWS site
	@Test
	public void testContactUsFunctionalityInEditMeetConsultantPagecomPWSSite_1920(){
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			country = driver.getCountry();
			env = driver.getEnvironment(); 
			String comPWS = null;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			String consultantEmailID = null;
			while(true){
				List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
				consultantEmailID = String.valueOf(getValueFromQueryResult(sponserList, "Username"));
				String PWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
				comPWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
				storeFrontHomePage.openPWS(comPWS);

				//Login with same PWS consultant
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
			//click meet your consultant banner link
			storeFrontConsultantPage.clickOnMeetYourConsultantLink();
			//validate we are navigated to "Meet your Consultant" page
			s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
			//click on 'Personalize My  Profile' link..
			storeFrontHomePage.clickOnPersonalizeMyProfileLink();
			//select the checkbox next to the email field and click save..
			storeFrontHomePage.checkEmailFieldCBOnEditConsultantInfoPage();
			storeFrontHomePage.clickOnSaveAfterEditPWS();
			//validate we are navigated to "Meet your Consultant" page
			s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");

			logout();

			storeFrontHomePage.openPWS(comPWS);

			//Login with same PWS consultant
			storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			storeFrontConsultantPage.clickOnMeetYourConsultantLink();
			s_assert.assertFalse(storeFrontHomePage.verifyEmailIdIsPresentInContactBox(), "Email Address is not Present in contact box After Edit");
			s_assert.assertTrue(storeFrontHomePage.verifyEnterYourNameFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your name box is not present on com site");
			s_assert.assertTrue(storeFrontHomePage.verifyEnterYourEmailFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your email box is not present on com site");
			s_assert.assertTrue(storeFrontHomePage.verifyEnterYourMessageFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your Message box is not present on com site");
			s_assert.assertTrue(storeFrontHomePage.verifySubmitButtonIsPresentOnMeetMyConsultantPage(), "Send button is not present on com site");

			logout();

			String bizPWS = storeFrontHomePage.convertComSiteToBizSite(comPWS);
			storeFrontHomePage.openPWS(bizPWS);

			//Login with same PWS consultant
			storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			storeFrontConsultantPage.clickOnMeetYourConsultantLink();
			s_assert.assertFalse(storeFrontHomePage.verifyEmailIdIsPresentInContactBox(), "Email Address is not Present in contact box After Edit");
			s_assert.assertTrue(storeFrontHomePage.verifyEnterYourNameFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your name box is not present on com site");
			s_assert.assertTrue(storeFrontHomePage.verifyEnterYourEmailFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your email box is not present on com site");
			s_assert.assertTrue(storeFrontHomePage.verifyEnterYourMessageFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your Message box is not present on com site");
			s_assert.assertTrue(storeFrontHomePage.verifySubmitButtonIsPresentOnMeetMyConsultantPage(), "Send button is not present on com site");

			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-4317:Soft-Terminated PC Customer enrolls to be a Consultant with his old email
	@Test
	public void testSoftTerminatedPCEnrollsToBeConsultant_4317() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		String firstName=TestConstants.FIRST_NAME+randomNum;
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		/*String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
	    String lastName = "lN";*/
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		System.out.println(pcUserEmailID);
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
		//storeFrontHomePage.clickOnCountryAtWelcomePage();
		driver.get(driver.getURL());

		//Again enroll as consultant with same eamil id
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_CA;    
			regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS; //TestConstants.KIT_PRICE_BIG_BUSINESS_US;
			regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithTerminatedEmail(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, pcUserEmailID, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, pcUserEmailID, password, addressLine1, city, postalCode, phoneNumber);
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
		s_assert.assertAll();
	}
	//Hybris Project-1898:To verify the contact your sponsor section in getting started page
	@Test
	public void testContactYourSponsorSectionInGettingStartedPage_1898() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			storeFrontHomePage = new StoreFrontHomePage(driver);
			RFO_DB = driver.getDBNameRFO();
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName =  TestConstants.REGIMEN_NAME_REVERSE;

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			String bizPWS = null;
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String firstName=TestConstants.FIRST_NAME+randomNum;
			while(true){
				List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
				String consultantEmailID = String.valueOf(getValueFromQueryResult(sponserList, "Username"));
				String PWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
				bizPWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
				storeFrontHomePage.openPWS(bizPWS);
				storeFrontHomePage.clickOnSponsorName();
				s_assert.assertTrue(storeFrontHomePage.verifyJoinMyTeamLinkPresent(), "Join My Team Link is not present on the Com page");
				s_assert.assertTrue(storeFrontHomePage.verifyContactBoxIsPresent(), "Contact Box is not Present");
				s_assert.assertTrue(storeFrontHomePage.verifyEmailIdIsPresentInContactBox(), "Email Address is not Present in contact box");
				//Login with same PWS consultant
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
			storeFrontHomePage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontHomePage.clickOnYourAccountDropdown();
			storeFrontHomePage.clickOnEditMyPWS();
			storeFrontHomePage.enterPhoneNumberOnEditPWS(phoneNumber);
			s_assert.assertTrue(storeFrontHomePage.verifyConsultantSinceOnMeetYourConsultantPage(), "Email Address is not Present in contact box After Edit");
			String emailAddress = firstName+randomNum+"@xyz.com"; 
			storeFrontHomePage.updateEmailOnMeetYourConsultantPage(emailAddress);
			storeFrontHomePage.clickOnSaveAfterEditPWS();
			logout();
			storeFrontHomePage.openPWS(bizPWS);
			storeFrontHomePage.clickOnSponsorName();

			s_assert.assertTrue(storeFrontHomePage.verifyEmailIdIsPresentInContactBoxAfterUpdate(emailAddress), "Email Address is not Present in contact box After Edit");
			s_assert.assertTrue(storeFrontHomePage.verifyPhoneNumberIsPresentInContactBox(phoneNumber), "Phone number is not Present in contact box After Edit");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}
	// Hybris Project-4030:from .com  Login as a existing PC and access Solution Tool
	@Test
	public void testLoginAsExstingPCAndAccessSolutionTool_4030()  {
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			country = driver.getCountry();
			env = driver.getEnvironment();  
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontPCUserPage=new StoreFrontPCUserPage(driver);
			String bizPWS = storeFrontHomePage.getBizPWS(country, env);
			String comPWS = storeFrontHomePage.convertBizSiteToComSite(bizPWS);
			storeFrontHomePage.openPWS(comPWS);

			List<Map<String, Object>> randomPCUserList =  null;
			String pcUserEmailID = null;
			String accountIdForPCUser = null;
			while(true){
				randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
				accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
				logger.info("Account Id of the user is "+accountIdForPCUser);

				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
				boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("error");
				if(isSiteNotFoundPresent){
					logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}
			logger.info("login is successful");
			//Access Solution Tool..
			s_assert.assertTrue(storeFrontPCUserPage.validateAccessSolutionTool(),"Solution tool is not giving the expected results");
			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	// Hybris Project-3959:BIZ:Active PC email id during consultant enrollment under different sponsor
	@Test
	public void testActivePCEmailIdDuringConsultantEnrollmentUnderDifferentSponsor_3959() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName =  TestConstants.REGIMEN_NAME_REVERSE;

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			String firstName=TestConstants.FIRST_NAME+randomNum;
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = "lN";

			storeFrontHomePage = new StoreFrontHomePage(driver);

			// Click on our product link that is located at the top of the page and then click in on quick shop
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");

			//Select a product with the price less than $80 and proceed to buy it
			//storeFrontHomePage.applyPriceFilterLowToHigh();
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
			String pcEmailId= storeFrontHomePage.createNewPC(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);
			logger.info("Enrolled PC user email address is "+pcEmailId);
			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");
			// Get  sponser with PWS for PC User from database
			List<Map<String, Object>> sponserList  = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			String sponserHavingPulseForPC = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));
			String emailAddressOfSponserOfPC= (String) getValueFromQueryResult(sponserList, "Username"); 
			String bizPWSOfSponserOfPC=String.valueOf(getValueFromQueryResult(sponserList, "URL")); 
			bizPWSOfSponserOfPC=storeFrontHomePage.replaceAllHTTPWithHTTPS(bizPWSOfSponserOfPC);
			logger.info("biz pws of sponser of pc from database "+bizPWSOfSponserOfPC);
			// sponser search by Account Number
			List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulseForPC),RFO_DB);
			String sponsorIdOfPC = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
			storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponsorIdOfPC); 
			//storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPC();
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

			logout();
			driver.get(driver.getURL()+"/"+driver.getCountry());

			// Get Canadian sponser with PWS from database
			sponserList  = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			String sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));
			String emailAddressOfSponser= (String) getValueFromQueryResult(sponserList, "Username"); 
			String bizPWSOfSponser=String.valueOf(getValueFromQueryResult(sponserList, "URL")); 
			// sponser search by Account Number
			sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
			String sponsorId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
			while(true){
				if(sponsorIdOfPC.equalsIgnoreCase(sponsorId)==false){
					break;
				}else{
					sponserList  = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
					sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));
					emailAddressOfSponser= (String) getValueFromQueryResult(sponserList, "Username"); 
					bizPWSOfSponser=String.valueOf(getValueFromQueryResult(sponserList, "URL")); 
					// sponser search by Account Number
					sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
					sponsorId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
				}
			}
			logger.info("biz pws of sponser of Consultant from database "+bizPWSOfSponser);
			storeFrontHomePage.openConsultantPWS(bizPWSOfSponser);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			//storeFrontHomePage.searchCID(sponsorId);
			//storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_NAME_BIG_BUSINESS, TestConstants.REGIMEN_NAME_REVERSE);  
			storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
			storeFrontHomePage.enterEmailAddress(pcEmailId);
			s_assert.assertTrue(storeFrontHomePage.verifyUpradingToConsulTantPopup(), "Upgrading to a consultant popup is not present");
			storeFrontHomePage.enterPasswordForUpgradePcToConsultant();
			storeFrontHomePage.clickOnLoginToTerminateToMyPCAccount();
			s_assert.assertTrue(storeFrontHomePage.verifyAccountTerminationMessage(), "Pc user is not terminated successfully");

			storeFrontHomePage.enterEmailAddress(pcEmailId);
			storeFrontHomePage.clickOnEnrollUnderLastUpline();
			logger.info("After click enroll under last upline we are on "+driver.getCurrentUrl());
			s_assert.assertTrue(driver.getCurrentUrl().contains(bizPWSOfSponserOfPC),"We have not navigated to biz pws of pc Sponser");
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_NAME_BIG_BUSINESS, TestConstants.REGIMEN_NAME_REVERSE);  
			storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
			storeFrontHomePage.enterFirstName(firstName);
			storeFrontHomePage.enterLastName(lastName);
			storeFrontHomePage.enterEmailAddress(pcEmailId);
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

			storeFrontHomePage.clickOnWelcomeDropDown();
			storeFrontOrdersPage = storeFrontHomePage.clickOrdersLinkPresentOnWelcomeDropDown();

			// Verify Order is present
			s_assert.assertTrue(storeFrontOrdersPage.verifyAdhocOrderIsPresent(), "Adhoc Order is not present");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}
	// Hybris Phase 2-2341:Add new billing profile | My Account | checkbox UN-CHECKED
	@Test
	public void testAddBillingProfileMyAccountFutureAutoshipCheckboxNotChecked_2341() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
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

			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

			storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
			storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
			storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
			storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontBillingInfoPage.selectNewBillingCardAddress();  
			storeFrontBillingInfoPage.clickOnSaveBillingProfile();

			//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

			s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the page");

			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
			storeFrontOrdersPage.clickAutoshipOrderNumber();

			//------------------ Verify that autoship template doesn't contains the newly created billing profile ------------------------------------------------------------  

			s_assert.assertFalse(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"Autoship Template Payment Method contains the new billing profile even when future autoship checkbox not selected");

			//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
			storeFrontOrdersPage.clickOnFirstAdHocOrder();

			//------------------ Verify that adhoc orders template doesn't contains the newly created billing profile by verifying by name------------------------------------------------------------

			s_assert.assertFalse(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"AdHoc Orders Template Payment Method contains new billing profile when future autoship checkbox not selected");

			//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			s_assert.assertAll(); 
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}

	}

	//Hybris Phase 2-4286 :: Version : 1 :: Verify order details of CRP autoship order
	@Test
	public void testOrderDetailsOfCRPAutoShipOrder_HP2_4286() throws SQLException, InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
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
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}

	}

	// phase 2 4287 -> Verify order details of consultant order
	@Test
	public void testOrdersDetailsOfConsultant_HP2_4287() throws SQLException, InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
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
			String consultantEmailID = null;
			String orderId = null;
			String accountId = null;
			String shippingMethodId =null;
			String lastName = null;

			List<Map<String, Object>> randomConsultantList =  null;
			List<Map<String, Object>> verifyAllDetailsList = null;
			List<Map<String,Object>> shippingCostAndHandlingCostList = null;
			List<Map<String,Object>> getOtherDetailValuesList = null;
			List<Map<String,Object>> getOrderIDList = null;
			DecimalFormat df = new DecimalFormat("#.00");

			storeFrontHomePage = new StoreFrontHomePage(driver);
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

			//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

			// Get Order Number
			String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
			storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

			// Get Order Id
			getOrderIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
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
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	// Hybris Phase 2-4291 :: Version : 1 :: Verify PC autoship order. 
	@Test
	public void testOrderDetailsForAutoshipOrdersForPC_4291() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
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
			String pcUserEmailID = null;
			String lastName = null;
			String orderId = null;
			String accountId = null;
			String shippingMethodId = null;
			List<Map<String, Object>> randomPCUserList =  null;
			List<Map<String,Object>> orderIdAccountIdDetailsList = null;
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

			//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"Consultant Page doesn't contain Welcome User Message");
			logger.info("login is successful");
			storeFrontPCUserPage.clickOnWelcomeDropDown();
			storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

			// Get Order Number
			String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
			storeFrontOrdersPage.clickAutoshipOrderNumber();
			s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksAutoShipHeader(),"Order Text is not present on the Page");
			s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
			s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

			//get Autoship Id Fro RFO
			List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
			String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));

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
			s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"PC autoship template subTotal is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

			// Assert Tax with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"PC autoship template tax amount is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

			// Assert Grand Total with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"PC autoship template grand total is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

			// assert shipping amount with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"PC autoship template shipping amount is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

			// assert Handling Value with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"PC autoship template handling amount is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

			// assert for shipping Method with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"PC autoship template shipping method is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Phase 2-4293 :: Version : 1 :: Verify details of retail order. 
	@Test
	public void testOrderDetailsForAdhocOrdersForRC_4293() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			String firstName = null;
			String addressLine1 = null;
			String postalCode = null;
			//			String locale = null;
			//			String region = null;
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
			String lastName = null;
			String orderId = null;
			String accountId = null;
			String shippingMethodId = null;
			String rcUserEmailID = null;
			List<Map<String, Object>> randomRCList =  null;
			List<Map<String, Object>> verifyAllDetailsList = null;
			List<Map<String,Object>> shippingCostAndHandlingCostList = null;
			List<Map<String,Object>> getOtherDetailValuesList = null;
			List<Map<String,Object>> getShippingMethodBasedOnMethodId = null;
			DecimalFormat df = new DecimalFormat("#.00");
			storeFrontHomePage = new StoreFrontHomePage(driver);

			while(true){
				randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
				rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
				accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
				logger.info("Account Id of the user is "+accountId);

				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
				boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
				if(isSiteNotFoundPresent){
					logger.info("SITE NOT FOUND for the user "+rcUserEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}	

			//s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserEmailID),"RC Page doesn't contain Welcome User Message");
			logger.info("login is successful");
			storeFrontRCUserPage.clickOnWelcomeDropDown();
			storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

			// Get Order Number
			String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
			storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

			// Get Order Id
			List<Map<String, Object>> getOrderIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderHistoryNumber),RFO_DB);
			orderId = String.valueOf(getValueFromQueryResult(getOrderIDList, "OrderID"));

			verifyAllDetailsList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
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
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());


			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_4293_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));

			taxDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));

			grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));

			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4293_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format((Number) getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));

			handlingDB = String.valueOf(df.format((Number) getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

			shippingMethodId = String.valueOf( getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			//GET_SHIPPING_METHOD_QUERY_RFO
			getShippingMethodBasedOnMethodId =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO,shippingMethodId),RFO_DB);
			shippingMethodDB = (String) getValueFromQueryResult(getShippingMethodBasedOnMethodId, "Name");
			String[] name = shippingMethodDB.split("\\(");
			String shippingMethodName = name[0]+" "+"("+name[1];
			//Assert Subtotal with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc Order template subTotal from RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

			// Assert Tax with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"Adhoc Order template tax amount from RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

			// Assert Grand Total with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc Order template grand total from RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

			// assert shipping amount with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc Order template shipping amount from RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

			// assert Handling Value with RFO
			s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc Order template handling amount from RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

			// assert for shipping Method with RFO
			//shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
			s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodName.trim()),"Adhoc Order template shipping method from RFO is "+shippingMethodName+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate());

			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}

	}	

	//  Hybris Project-141:PC Perks Total Savings Calculation & disclaimer
	@Test
	public void testPCPerksTotalSavingsCalculation_141() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
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
			storeFrontPCUserPage.clickEditCrpLinkPresentOnWelcomeDropDown();
			storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
			storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();

			//assert total saving on Autoship cart page
			s_assert.assertTrue(storeFrontUpdateCartPage.verifyTotalSavingsIsAvailableOnAutoshipCart(),"Total Savings is not available on Autoship cart");

			s_assert.assertAll();

		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}

	}

	//Hybris Project-3925:CORP: Enroll CA/US User, Sponsor without PWS & User Enrolls without Pulse
	@Test
	public void testEnrollCAAndUsUserSponsorWithoutPWSAndUserEnrollsWithoutPulse_3925() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName =  TestConstants.REGIMEN_NAME_REVERSE;

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
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
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	// Hybris Project-3926:CORP: Enroll CA/US User, Sponsor with PWS & User Enrolls without Pulse
	@Test
	public void testEnrollCAUserSponsorWithPWSAndEnrollWithoutPulse_3926() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName =  TestConstants.REGIMEN_NAME_REVERSE;

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();

			// Get Canadian sponser without PWS from database
			List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			String sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));

			// sponser search by Account Number
			List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
			String sponsorId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

			storeFrontHomePage.searchCID(sponsorId);
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			s_assert.assertFalse(storeFrontHomePage.verifyCurrentUrlContainCorp(), "Current URL contains Corp");
			s_assert.assertTrue(storeFrontHomePage.verifyCurrentUrlContainBizSite(), "Current URL does not contain Biz");

			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}


	//Hybris Project-3898:Downgrade to RC on the Payment / Order summary page
	@Test
	public void testDowngradePCToRCAtPaymentOrderSummaryPage_3900() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			country = driver.getCountry();
			int randomNum =  CommonUtils.getRandomNum(10000, 1000000);
			env = driver.getEnvironment();
			String firstName = TestConstants.FIRST_NAME+randomNum;
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = TestConstants.LAST_NAME+randomNum;
			String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			String PWS = storeFrontHomePage.getBizPWS(country, env);
			PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
			storeFrontHomePage.openPWS(PWS);
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
			storeFrontHomePage.selectProductAndProceedToBuy();
			storeFrontHomePage.clickOnCheckoutButton();
			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");
			storeFrontHomePage.enterNewPCDetails(firstName, lastName, password, emailAddress);
			//Enter main account info
			storeFrontHomePage.enterMainAccountInfo();
			storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
			storeFrontHomePage.clickOnShippingAddressNextStepBtn();
			//Enter billing info
			storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
			storeFrontHomePage.selectNewBillingCardExpirationDate();
			storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.selectNewBillingCardAddress();
			storeFrontHomePage.clickOnSaveBillingProfile();
			//Uncheck PC Perks Checkbox on Payment/order summary page
			s_assert.assertTrue(storeFrontHomePage.validatePCPerksCheckBoxIsDisplayed(),"PC Perks checkbox is not present");
			s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is not selected");
			storeFrontHomePage.checkPCPerksCheckBox();
			storeFrontHomePage.clickOnShippingAddressNextStepBtn();
			s_assert.assertFalse(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is selected");
			storeFrontHomePage.clickOnBillingNextStepBtn();
			storeFrontHomePage.clickPlaceOrderBtn();
			s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(),"Order is not placed successfully");
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			storeFrontHomePage.clickOnWelcomeDropDown();
			s_assert.assertFalse(storeFrontHomePage.verifyEditPcPerksIsPresentInWelcomDropdownForUpgrade(),"Edit Pc Perks Link is present for RC User");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}


	//Hybris Project-1897:To verify the Meet the consultant banner on solution tool page
	@Test
	public void testMeetConsultantBannerOnSolutionToolPage_1897(){
		RFO_DB = driver.getDBNameRFO();  
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
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
		//click learn more link
		storeFrontHomePage.clickLearnMoreLinkUnderSolutionToolAndSwitchControl();
		//validate consultant info on top right corner..
		s_assert.assertTrue(storeFrontHomePage.validateConsultantNameOnTopRightCorner(),"Consultant Info is not present on right top Corner");
		s_assert.assertAll();
	}

	// Hybris Project-4028:Access Solution tool from .COM Site Category pages Left Menu
	@Test
	public void testAccessSolutionToolcomSiteCategoryPagesLeftMenu_4028()	 {
		if(driver.getCountry().equalsIgnoreCase("ca")){  
			country = driver.getCountry();
			env = driver.getEnvironment();  
			storeFrontHomePage = new StoreFrontHomePage(driver);
			String PWS = storeFrontHomePage.getBizPWS(country, env);
			PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
			storeFrontHomePage.openPWS(PWS);
			//Access Solution Tool..
			s_assert.assertTrue(storeFrontHomePage.validateAccessSolutionTool(),"Solution tool is not giving the expected results");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-1983:Return order history
	@Test
	public void testReturnOrderHistory_1983() throws InterruptedException{

		RFO_DB = driver.getDBNameRFO();
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String consultantEmailID = null;
		String orderId = null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOrderDetailsList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_RETURN_ORDERS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");		
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
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number for assert
		String returnOrderNumber = storeFrontOrdersPage.getReturnOrderNumber();
		storeFrontOrdersPage.clickReturnOrderNumber();

		// Get Order Id
		getOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_RFO,returnOrderNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderID"));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "Total")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "TotalTax")));
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "SubTotal")));
		String orderStatusID = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "ReturnStatusID"));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4287_RFO,orderId),RFO_DB);
		shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));

		handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));


		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc Order template subtotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"Adhoc Order template tax on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc Order template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		//		// assert shipping amount with RFO
		//		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc Order template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		//		// assert Handling Value with RFO
		//		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc Order template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		String createdOrderStatus = storeFrontOrdersPage.convertOrderStatusForReturnOrder(orderStatusID);				
		s_assert.assertTrue(storeFrontOrdersPage.getOrderStatusFromUI().toLowerCase().trim().contains(createdOrderStatus.toLowerCase().trim()),"Adhoc Order template handling amount on RFO is "+createdOrderStatus+" and on UI is "+storeFrontOrdersPage.getOrderStatusFromUI());

		s_assert.assertAll();
	}

	//Hybris Project-2409:Check Return order information on the Order history page. (Returning Consultant Autoship Order)
	@Test
	public void testConsultantAutoshipReturnOrderInformation_2409() throws InterruptedException{

		RFO_DB = driver.getDBNameRFO();
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String consultantEmailID = null;
		String orderId = null;
		String accountID = null;

		List<Map<String, Object>> randomAccountIDList =  null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOrderDetailsList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomAccountIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ID_OF_RETURN_CONSULTANT_AUTOSHIP_ORDER,countryId),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomAccountIDList, "AccountID"));	

			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_CONSULTANT_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");		
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
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number for assert
		String returnOrderNumber = storeFrontOrdersPage.getReturnOrderNumber();
		storeFrontOrdersPage.clickReturnOrderNumber();

		// Get Order Id
		getOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_RFO,returnOrderNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderID"));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "Total")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "TotalTax")));
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "SubTotal")));
		String orderStatusID = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "ReturnStatusID"));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4287_RFO,orderId),RFO_DB);
		shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));

		handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));


		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Adhoc Order template subtotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"Adhoc Order template tax on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"Adhoc Order template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		//		// assert shipping amount with RFO
		//		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"Adhoc Order template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		//		// assert Handling Value with RFO
		//		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"Adhoc Order template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert return order status

		String createdOrderStatus = storeFrontOrdersPage.convertOrderStatusForReturnOrder(orderStatusID);				
		s_assert.assertTrue(storeFrontOrdersPage.getOrderStatusFromUI().toLowerCase().trim().contains(createdOrderStatus.toLowerCase().trim()),"Adhoc Order template handling amount on RFO is "+createdOrderStatus+" and on UI is "+storeFrontOrdersPage.getOrderStatusFromUI());

		s_assert.assertAll();

	}

	//Hybris Project-4459:Check Return order information on the Order history page. (Returning PC Perks Autoship Order)
	@Test(enabled=false)
	public void testPCAutoshipReturnOrderInformation_4459() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String consultantEmailID = null;
		String orderId = null;
		String accountID = null;

		List<Map<String, Object>> randomAccountIDList =  null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOrderDetailsList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomAccountIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ID_OF_RETURN_PC_AUTOSHIP_ORDER,countryId),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomAccountIDList, "AccountID")); 

			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_CONSULTANT_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");  
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
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number for assert
		String returnOrderNumber = storeFrontOrdersPage.getReturnOrderNumber();
		storeFrontOrdersPage.clickReturnOrderNumber();

		// Get Order Id
		getOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_RFO,returnOrderNumber),RFO_DB);
		orderId = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "OrderID"));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "Total")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "TotalTax")));
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOrderDetailsList, "SubTotal")));
		String orderStatusID = String.valueOf(getValueFromQueryResult(getOrderDetailsList, "ReturnStatusID"));

		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_4287_RFO,orderId),RFO_DB);
		shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));

		handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

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

		String createdOrderStatus = storeFrontOrdersPage.convertOrderStatusForReturnOrder(orderStatusID);    
		s_assert.assertTrue(storeFrontOrdersPage.getOrderStatusFromUI().toLowerCase().trim().contains(createdOrderStatus.toLowerCase().trim()),"Adhoc Order template handling amount on RFO is "+createdOrderStatus+" and on UI is "+storeFrontOrdersPage.getOrderStatusFromUI());

		s_assert.assertAll();
	}



	//Hybris Project-2141:Check Shipping and Handling Fee for UPS Ground for Order total 1000-999999
	@Test
	public void testShippingAndHandlingFeeForUPSGroundForOrderTotal_1000_999999_2141() throws InterruptedException{
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.applyPriceFilterHighToLow();
		storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());
		String quantity = "10";

		while(true){
			storeFrontConsultantPage.addQuantityOfProduct(quantity);
			String total = storeFrontUpdateCartPage.getTotalPriceOfProduct();
			String orderTotal = total.split("\\$")[1].trim();
			String[] getTotal = orderTotal.split("\\,");
			String finalTotal = getTotal[0]+getTotal[1];
			System.out.println("Order total for consultant"+finalTotal);
			double totalFromUI = Double.parseDouble(finalTotal);
			if(totalFromUI<1000){
				continue;
			}else{
				break;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPSGroundInOrderSummary();

		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 15.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$25.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
		}
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		logout();

		driver.get(driver.getURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
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
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontPCUserPage.applyPriceFilterHighToLow();
		storeFrontPCUserPage.clickAddToBagButton(driver.getCountry());

		while(true){
			storeFrontPCUserPage.addQuantityOfProduct(quantity);
			String total = storeFrontUpdateCartPage.getTotalPriceOfProductForPC();
			String orderTotal = total.split("\\$")[1].trim();
			String[] getTotal = orderTotal.split("\\,");
			String finalTotal = getTotal[0]+getTotal[1];
			System.out.println("Order total for consultant"+finalTotal);
			double totalFromUI = Double.parseDouble(finalTotal);
			if(totalFromUI<1000){
				continue;
			}else{
				break;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPSGroundInOrderSummary();

		deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 15.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$25.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
		}
		/*storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
				logout();

				driver.get(driver.getURL()+"/"+driver.getCountry());

				List<Map<String, Object>> randomRCList =  null;
				String rcUserEmailID =null;
				while(true){
					randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_HAVING_ACTIVE_ORDER_RFO,countryId),RFO_DB);
					rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
					storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
					boolean isError = driver.getCurrentUrl().contains("error");
					if(isError){
						logger.info("login error for the user "+rcUserEmailID);
						driver.get(driver.getURL());
					}
					else
						break;
				} 

				storeFrontRCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
				storeFrontRCUserPage.applyPriceFilterHighToLow();
				storeFrontRCUserPage.clickAddToBagButton(driver.getCountry());

				while(true){
					storeFrontRCUserPage.addQuantityOfProduct(quantity);
					String total = storeFrontUpdateCartPage.getTotalPriceOfProductForPC();
					String orderTotal = total.split("\\$")[1].trim();
					String[] getTotal = orderTotal.split("\\,");
					String finalTotal = getTotal[0]+getTotal[1];
					System.out.println("Order total for consultant"+finalTotal);
					double totalFromUI = Double.parseDouble(finalTotal);
					if(totalFromUI<1000){
						continue;
					}else{
						break;
					}
				}
				storeFrontUpdateCartPage.clickOnCheckoutButton();
				storeFrontUpdateCartPage.selectShippingMethodUPSGroundInOrderSummary();

				deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
				logger.info("deliveryCharges ="+deliveryCharges);
				handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
				logger.info("handlingCharges ="+handlingCharges);

				if(driver.getCountry().equalsIgnoreCase("CA")){
					//Assert of shipping cost from UI
					s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 15.00"),"Shipping charges on UI is not As per shipping method selected");
					s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
				}else if(driver.getCountry().equalsIgnoreCase("US")){
					s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$25.00"),"Shipping charges on UI is not As per shipping method selected");
					s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
				}*/
		s_assert.assertAll();

	}

	//Hybris Project-2148:Check Shipping and Handling Fee for UPS 1Day for Order total 0-999999
	@Test
	public void testCheckShippingAndHandlingFeeForUPS1DayForOrderTotal_0_999999_2148() throws InterruptedException{
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.applyPriceFilterHighToLow();
		storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());

		while(true){
			String total = storeFrontUpdateCartPage.getTotalPriceOfProduct();
			String orderTotal = total.split("\\$")[1].trim();
			System.out.println("Order total for consultant"+orderTotal);
			double totalFromUI = Double.parseDouble(orderTotal);
			if(totalFromUI<0){
				continue;
			}else{
				break;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();

		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 20.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$23.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
		}
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		logout();

		driver.get(driver.getURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
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
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontPCUserPage.applyPriceFilterHighToLow();
		storeFrontPCUserPage.clickAddToBagButton(driver.getCountry());

		while(true){
			String total = storeFrontUpdateCartPage.getTotalPriceOfProductForPC();
			String orderTotal = total.split("\\$")[1].trim();
			System.out.println("Order total for consultant"+orderTotal);
			double totalFromUI = Double.parseDouble(orderTotal);
			if(totalFromUI<0){
				continue;
			}else{
				break;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();

		deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 20.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$23.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
		}
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		/*logout();

				driver.get(driver.getURL()+"/"+driver.getCountry());

				List<Map<String, Object>> randomRCList =  null;
				String rcUserEmailID =null;
				while(true){
					randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_HAVING_ACTIVE_ORDER_RFO,countryId),RFO_DB);
					rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
					storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
					boolean isError = driver.getCurrentUrl().contains("error");
					if(isError){
						logger.info("login error for the user "+rcUserEmailID);
						driver.get(driver.getURL());
					}
					else
						break;
				} 

				storeFrontRCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
				storeFrontRCUserPage.applyPriceFilterHighToLow();
				storeFrontRCUserPage.clickAddToBagButton(driver.getCountry());

				while(true){
					String total = storeFrontUpdateCartPage.getTotalPriceOfProductForPC();
					String orderTotal = total.split("\\$")[1].trim();
					System.out.println("Order total for consultant"+orderTotal);
					double totalFromUI = Double.parseDouble(orderTotal);
					if(totalFromUI<0){
						continue;
					}else{
						break;
					}
				}
				storeFrontUpdateCartPage.clickOnCheckoutButton();
				storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();

				deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
				logger.info("deliveryCharges ="+deliveryCharges);
				handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
				logger.info("handlingCharges ="+handlingCharges);

				if(driver.getCountry().equalsIgnoreCase("CA")){
					//Assert of shipping cost from UI
					s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 20.00"),"Shipping charges on UI is not As per shipping method selected");
					s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
				}else if(driver.getCountry().equalsIgnoreCase("US")){
					s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$24.00"),"Shipping charges on UI is not As per shipping method selected");
					s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
				}*/
		s_assert.assertAll();
	}

	//Hybris Project-2187:Terms and Conditions - PC enrollment
	@Test
	public void testTermsAndConditionsPCEnrollment_2187() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		// Click on our product link that is located at the top of the page and then click in on quick shop
		/*storeFrontHomePage.clickOnShopLink();
		   storeFrontHomePage.clickOnAllProductsLink();*/
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
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);

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
		//validate PC Terms & Conditions Text, CB..
		s_assert.assertTrue(storeFrontHomePage.validateTermsAndConditions(),"PC  terms and conditions is not visible");
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
		s_assert.assertAll();  
	}

	//Hybris Project-2300:Becomke PC User After adding product to cart
	@Test
	public void testBecomePCUserAfterAddingProductToCart_2300() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		// Click on our product link that is located at the top of the page and then click in on quick shop
		/*storeFrontHomePage.clickOnShopLink();
		   storeFrontHomePage.clickOnAllProductsLink();*/
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
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);

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
		s_assert.assertAll(); 
	}


	//Hybris Project-2295:Without Loggin in, Add Multiple Order line to the cart with Multiple Quantity
	@Test
	public void testWithoutLogin_AddMultipleOrderLineTotheCartWithMultipleQuantity_2295() throws InterruptedException{
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String quantity = "2";
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.clickAddToBagButton();
		String subtotalOfAddedProduct = storeFrontHomePage.getSubTotalOfAddedProduct();
		storeFrontHomePage.addQuantityOfProduct(quantity);
		s_assert.assertTrue(storeFrontHomePage.validateSubTotalAfterQuantityIncreased(subtotalOfAddedProduct,quantity),"subtotal not present on UI as expected");
		s_assert.assertAll();
	}

	//Hybris Project-3874:COM: Join PCPerk in the shipment section - US Sponsor WITHOUT Pulse
	@Test
	public void testJoinPCPerksInShipmentSection_3874() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String lastName = "lN";
		country = driver.getCountry();
		String URL=null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNumber;
		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String crossCountryName=null;

		//Enroll Cross Country Consultant without pulse And PWS.
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		if(driver.getCountry().equalsIgnoreCase("CA")){
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
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		crossCountryName=storeFrontHomePage.selectDifferentCountry();
		//storeFrontHomePage.openConsultantPWS(URL);
		String  consultantEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME, consultantEmailAddress, password, addressLine1, city, postalCode, phoneNumber);
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
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		//storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		URL=driver.getCurrentUrl();
		String comURL=storeFrontHomePage.convertBizSiteToComSite(URL);
		String urlToAssert=storeFrontHomePage.convertCountryInPWS(comURL);
		logout();

		//Get enrolled Consultant Account Number As Sponser Id.
		List<Map<String, Object>> consultantDetails = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,consultantEmailAddress),RFO_DB);
		String sponserId=(String) getValueFromQueryResult(consultantDetails, "AccountNumber"); 

		//Get .com PWS from database to start enrolling rc user and upgrading it to pc user
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
		String emailAddressOfSponser= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String comPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		//Open com pws of Sponser
		storeFrontHomePage.openConsultantPWS(comPWSOfSponser);
		while(true){
			if(driver.getCurrentUrl().contains("sitenotfound")){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
				emailAddressOfSponser= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
				comPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
				accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				storeFrontHomePage.openConsultantPWS(comPWSOfSponser);	
				continue;
			}else
				break;
		}	
		logger.info("biz pws to start enroll is "+comPWSOfSponser);
		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String accountnumber = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		//Hover shop now and click all products link.
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		//storeFrontHomePage.selectProductAndProceedToBuy();
		storeFrontHomePage.clickAddToBagButton();

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
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNumber, emailAddress, password);

		//Assert the default consultant.
		s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(emailAddressOfSponser),"Default consultant is not the one whose pws is used");
		//Assert continue without sponser link is not present
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(), "Continue without Sponser link is present on pws enrollment");
		s_assert.assertTrue(storeFrontHomePage.verifyNotYourSponsorLinkIsPresent(),"Not your Sponser link is not present.");

		//Click not your sponser link and verify continue without sponser link is present.
		storeFrontHomePage.clickOnNotYourSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifySponserSearchFieldIsPresent(),"Sponser search field is not present");

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//Search for sponser and ids.
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponserId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		//verify the cross country sponser is selected.
		s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(consultantEmailAddress),"Cross Country Sponser is not selected");
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		s_assert.assertTrue(storeFrontHomePage.isShippingAddressNextStepBtnIsPresent(),"Shipping Address Next Step Button Is not Present");

		//check pc perks checkbox at checkout page in Shipment section.
		storeFrontHomePage.checkPCPerksCheckBox();
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
		String currentURL=driver.getCurrentUrl();
		s_assert.assertTrue(urlToAssert.contains(currentURL),"After pc Enrollment the site does not navigated to expected url");
		s_assert.assertAll();
	}

	//Hybris Project-3880:BIZ:Join PCPerk in the Order Summary section -CA Sposnor WITHOUT Pulse
	@Test
	public void testJoinPCPerksInOrderSummarySection_3880() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String lastName = "lN";
		String URL=null;
		country = driver.getCountry();
		String firstName=TestConstants.FIRST_NAME+randomNumber;
		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;

		//Enroll  Consultant without pulse And PWS.
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		if(driver.getCountry().equalsIgnoreCase("CA")){
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
		//storeFrontHomePage.openConsultantPWS(URL);
		String  consultantEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME, consultantEmailAddress, password, addressLine1, city, postalCode, phoneNumber);
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
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		//storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		URL=driver.getCurrentUrl();
		String comURL=storeFrontHomePage.convertBizSiteToComSite(URL);
		logout();

		//Get enrolled Consultant Account Number As Sponser Id.
		List<Map<String, Object>> consultantDetails = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,consultantEmailAddress),RFO_DB);
		String sponserId=(String) getValueFromQueryResult(consultantDetails, "AccountNumber"); 

		//Get .Biz PWS from database to start enrolling rc user and upgrading it to pc user
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		//String emailAddressOfSponser= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String bizPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		//Open biz pws of Sponser
		storeFrontHomePage.openConsultantPWS(bizPWSOfSponser);
		while(true){
			if(driver.getCurrentUrl().contains("sitenotfound")){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
				//String emailAddressOfSponser= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
				bizPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
				accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				storeFrontHomePage.openConsultantPWS(bizPWSOfSponser);	
				continue;
			}else
				break;
		}	
		logger.info("biz pws to start enroll is "+bizPWSOfSponser);
		//Hover shop now and click all products link.
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		//storeFrontHomePage.selectProductAndProceedToBuy();
		storeFrontHomePage.clickAddToBagButton();

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
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNumber, emailAddress, password);

		//Assert continue without sponser link is not present
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(), "Continue without Sponser link is present on pws enrollment");
		s_assert.assertTrue(storeFrontHomePage.verifyNotYourSponsorLinkIsPresent(),"Not your Sponser link is not present.");

		//check pc perks checkbox at checkout page in order summary Section.
		storeFrontHomePage.checkPCPerksCheckBox();
		//Click not your sponser link and verify continue without sponser link is present.
		storeFrontHomePage.clickOnNotYourSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifySponserSearchFieldIsPresent(),"Sponser search field is not present");

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//Search for sponser and ids.
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponserId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		//verify the cross country sponser is selected.
		s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(consultantEmailAddress),"Cross Country Sponser is not selected");
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		s_assert.assertTrue(storeFrontHomePage.isShippingAddressNextStepBtnIsPresent(),"Shipping Address Next Step Button Is not Present");
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
		String currentURL=driver.getCurrentUrl();
		s_assert.assertTrue(comURL.contains(currentURL),"After pc Enrollment the site does not navigated to expected url");
		s_assert.assertAll();
	}


	// Hybris Project-3883:BIZ:Join PCPerk in the Order Summary - US Sponsor WITHOUT Pulse
	@Test
	public void testJoinPCPerksInOrderSummarySection_3883() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String lastName = "lN";
		String primaryEmailAddress=null;
		country = driver.getCountry();
		String URL=null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNumber;
		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String crossCountryName=null;

		//Enroll Cross Country Consultant without pulse And PWS.
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		if(driver.getCountry().equalsIgnoreCase("CA")){
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
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		crossCountryName=storeFrontHomePage.selectDifferentCountry();
		//storeFrontHomePage.openConsultantPWS(URL);
		String  newSponserEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME, newSponserEmailAddress, password, addressLine1, city, postalCode, phoneNumber);
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
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		URL=driver.getCurrentUrl();
		String comURL=storeFrontHomePage.convertBizSiteToComSite(URL);
		String urlToAssert=storeFrontHomePage.convertCountryInPWS(comURL);
		logout();

		//Get enrolled Consultant Account Number As Sponser Id.
		List<Map<String, Object>> consultantDetails = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,newSponserEmailAddress),RFO_DB);
		String sponserId=(String) getValueFromQueryResult(consultantDetails, "AccountNumber"); 

		//Get .biz PWS from database to start enrolling rc user and upgrading it to pc user
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		primaryEmailAddress= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String bizPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));

		//Open com pws of Sponser
		storeFrontHomePage.openConsultantPWS(bizPWSOfSponser);
		//Hover shop now and click all products link.
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		//storeFrontHomePage.selectProductAndProceedToBuy();
		storeFrontHomePage.clickAddToBagButton();

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
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNumber, emailAddress, password);

		//Assert continue without sponser link is not present
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(), "Continue without Sponser link is present on pws enrollment");
		s_assert.assertTrue(storeFrontHomePage.verifyNotYourSponsorLinkIsPresent(),"Not your Sponser link is not present.");

		//check pc perks checkbox at checkout page in order summary section.
		storeFrontHomePage.checkPCPerksCheckBox();
		//Click not your sponser link and verify continue without sponser link is present.
		storeFrontHomePage.clickOnNotYourSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifySponserSearchFieldIsPresent(),"Sponser search field is not present");

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//Search for sponser and ids.
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponserId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		//verify the cross country sponser is selected.
		s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(newSponserEmailAddress),"Cross Country Sponser is not selected");
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		s_assert.assertTrue(storeFrontHomePage.isShippingAddressNextStepBtnIsPresent(),"Shipping Address Next Step Button Is not Present");
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
		String currentURL=driver.getCurrentUrl();
		s_assert.assertTrue(urlToAssert.contains(currentURL),"After pc Enrollment the site does not navigated to expected url");
		s_assert.assertAll();
	}

	//Hybris Project-3882:COM:Join PCPerk in the Order Summary section -US Sponsor WITH Pulse
	@Test
	public void testJoinPCPerksInOrderSummarySection_3882() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String lastName = "lN";
		country = driver.getCountry();
		List<Map<String, Object>> randomConsultantList=null;
		List<Map<String, Object>> sponsorIdList =null;
		String requiredCountry=null;
		String requiredCountryId=null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNumber;
		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			requiredCountry="us";
			requiredCountryId="236";
		}else{
			requiredCountry="ca";
			requiredCountryId="40";
		}
		//Get Cross Country Sponser With Pulse And PWS.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",requiredCountry,requiredCountryId),RFO_DB);
		String emailAddressOfSponser= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String bizPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String sponserId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		String comURL=storeFrontHomePage.convertBizSiteToComSite(bizPWSOfSponser);
		String urlToAssert=storeFrontHomePage.convertCountryInPWS(comURL);
		logger.info(" pws to assert is "+urlToAssert);

		//Get .biz PWS from database to start enrolling rc user and upgrading it to pc user
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		String emailAddressOfSponserConsultant= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String bizPWSForEnrollment=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		String accountIdOfConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		//Open biz pws of Sponser
		storeFrontHomePage.openConsultantPWS(bizPWSForEnrollment);
		while(true){
			if(driver.getCurrentUrl().contains("sitenotfound")){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
				bizPWSForEnrollment=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
				accountIdOfConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				storeFrontHomePage.openConsultantPWS(bizPWSForEnrollment);	
				continue;
			}else
				break;
		}	
		logger.info("biz pws to start enroll is "+bizPWSForEnrollment);
		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountIdOfConsultant),RFO_DB);
		String accountnumber = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		//Hover shop now and click all products link.
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		//storeFrontHomePage.selectProductAndProceedToBuy();
		storeFrontHomePage.clickAddToBagButton();

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
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNumber, emailAddress, password);

		//Assert continue without sponser link is not present
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(), "Continue without Sponser link is present on pws enrollment");
		s_assert.assertTrue(storeFrontHomePage.verifyNotYourSponsorLinkIsPresent(),"Not your Sponser link is not present.");

		//check pc perks checkbox at checkout page in order summary section.
		storeFrontHomePage.checkPCPerksCheckBox();
		//Click not your sponser link and verify continue without sponser link is present.
		storeFrontHomePage.clickOnNotYourSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifySponserSearchFieldIsPresent(),"Sponser search field is not present");

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//Search for sponser and ids.
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponserId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		//verify the cross country sponser is selected.
		s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(emailAddressOfSponser),"Cross Country Sponser is not selected");
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		s_assert.assertTrue(storeFrontHomePage.isShippingAddressNextStepBtnIsPresent(),"Shipping Address Next Step Button Is not Present");
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
		String currentURL=driver.getCurrentUrl();
		logger.info(" pws after successful  enroll is "+currentURL);
		s_assert.assertTrue(currentURL.contains(urlToAssert),"After pc Enrollment the site does not navigated to expected url");
		s_assert.assertAll();
	}

	// Hybris Project-3888:BIZ:Verify Search Again functionality on Sponsor Search section
	@Test
	public void testBizSearchAgainFunctionalityOnSponsorSearchSection_3888() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		storeFrontHomePage.openPWS(PWS);  
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		//storeFrontHomePage = new StoreFrontHomePage(driver);
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
		//Enter the User information and DONOT  check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);
		//click not your sponsor link..
		storeFrontHomePage.clickOnNotYourSponsorLink();
		//search for wrong/Incorrect sponsor..
		storeFrontHomePage.searchCID(TestConstants.INVALID_SPONSOR_ID);
		//validate that  Continuewithout a spsonor and Request a spsonr link should not be displayed..
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponsorLinkIsPresent(), "continue without sponsor link is present");
		//click search-again..
		storeFrontHomePage.clickSearchAgain();
		//search for a correct sponsor
		storeFrontHomePage.searchCID(TestConstants.SPONSOR_ID_FOR_PC);
		//validate user is able to search for right sponsor and continue with the flow..
		s_assert.assertTrue(storeFrontHomePage.validateUserAbleToSerachSponsorAndContinueFlow(), "User is not able to search sponsor and thus continue with Flow!!");
		s_assert.assertAll();
	}

	//Hybris Project-3957:During enrollment process, switch from Express Enrollment to standard Enrollment
	@Test
	public void testDuringEnrollmentSwitchFromExpressToStandardEnrollment_3957() throws InterruptedException {
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

	//Hybris Project-3928:Verify the page displayed after clicking on Enroll Now button from .biz home page.
	@Test
	public void testPageDisplayedAfterClickingEnrollNowBizHomePage_3928()	 {
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

	//Hybris Project-2091:Switch From Retail Customer to Prefered Customer
	@Test
	public void testSwitchFronRCToPC_2091() throws InterruptedException{
		int randomNumber =  CommonUtils.getRandomNum(10000, 1000000);
		String newUserName = TestConstants.NEW_RC_USER_NAME+randomNumber;
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;
		String qty = "10";
		String increasedQty = "12";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
			rcEmailID = (String) getValueFromQueryResult(randomRCList, "Username");
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID,password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
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

		//Click on place order
		storeFrontHomePage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontHomePage.validatePCPerksCheckBoxIsDisplayed(), "pc perks checkbox is displayed");
		s_assert.assertFalse(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is selected for rc user");
		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		s_assert.assertTrue(storeFrontHomePage.isPopUpForPCThresholdPresent(),"Threshold poup for PC validation NOT present");
		storeFrontHomePage.addQuantityOfProduct(qty);
		storeFrontHomePage.addQuantityOfProductTillThresholdPopupDisappear(increasedQty);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected msg is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);
		storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.enterMainAccountInfoAndClearPreviousField();
		logger.info("Main account details entered");
		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		//   storeFrontHomePage.enterSponsorIdDuringCreationOfPC(TestConstants.SPONSOR_ID_FOR_PC);
		//   storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//verify PC User Enrolled successfully
		storeFrontHomePage.clickOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontHomePage.verifyEditPcPerksIsPresentInWelcomDropdownForUpgrade(), "User NOT registered successfully as PC User");
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

	//Hybris Project-96:Express Enrollment Billing Info Shipping Info - New
	@Test
	public void testExpressEnrollmentBillingInfoShippingInfo_New_96() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		String newAddressLine1 = null;
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName=TestConstants.FIRST_NAME+randomNum;

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
		storeFrontHomePage.openPWSSite(country, driver.getEnvironment());
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.uncheckCRPCheckBox();
		storeFrontHomePage.uncheckPulseCheckBox();
		storeFrontHomePage.clickOnReviewAndConfirmShippingEditBtn();
		storeFrontHomePage.enterPassword(password);
		storeFrontHomePage.enterConfirmPassword(password);
		storeFrontHomePage.enterNewShippingAddressLine1DuringEnrollment(newAddressLine1);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.verifyNewAddressPresentInMainAccountInfo(newAddressLine1),"new address not present in main account info");
		s_assert.assertTrue(storeFrontHomePage.validateBillingAddressOnMainAccountInfo(addressLine1),"Billing address is not present as expected");
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

	//Hybris Project-95:Express Enrollment Billing Profile Main Account Info - New
	@Test
	public void testExpressEnrollmentBillingProfileMainAccountInfo_New_95() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		String newAddressLine1 = null;
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName=TestConstants.FIRST_NAME+randomNum;

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
		storeFrontHomePage.openPWSSite(country, driver.getEnvironment());
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.uncheckCRPCheckBox();
		storeFrontHomePage.uncheckPulseCheckBox();
		storeFrontHomePage.clickOnReviewAndConfirmShippingEditBtn();
		storeFrontHomePage.enterPassword(password);
		storeFrontHomePage.enterConfirmPassword(password);
		storeFrontHomePage.enterNewShippingAddressLine1DuringEnrollment(newAddressLine1);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.verifyNewAddressPresentInMainAccountInfo(newAddressLine1),"new address not present in main account info");
		s_assert.assertTrue(storeFrontHomePage.verifyPhoneNumberIsPresentInAccountInfo(),"Phone number is not present");
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


	// Hybris Project-3722:Register as RC with Same CA Sponsor WITH Pulse
	@Test
	public void testRegisterAsRCWithSameSponsorWithPulse_3722() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		String bizPWS = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			String PWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
			bizPWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
			storeFrontHomePage.openConsultantPWS(bizPWS);
			if(driver.getCurrentUrl().contains("sitenotfound"))
				continue;
			else
				break;
		}	
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.clickAddToBagButton(country);

		//assert sign up link
		s_assert.assertFalse(storeFrontHomePage.verifySignUpLinkIsPresent(), "Sign up link is present on checkout page");
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);

		/*//CheckoutPage is displayed?
						s_assert.assertTrue(storeFrontHomePage.isCheckoutPageDisplayed(), "Checkout page has NOT displayed");
						logger.info("Checkout page has displayed");*/

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnNotYourSponsorLink();
		//assert continue without sponser link is not present and request your sponser button
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue without sponser link present");
		s_assert.assertFalse(storeFrontHomePage.verifyRequestASponsorBtn(),"Request a sponser button is present");

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
		String currentPWSUrl=driver.getCurrentUrl();
		s_assert.assertTrue(storeFrontHomePage.verifyPWSAfterSuccessfulEnrollment(currentPWSUrl,bizPWS.split("\\:")[1]),"PWS of the RC after enrollment is not same as the one it started enrollment");

		s_assert.assertAll();	

	}

	//Hybris Project-3723:Register as PC customer with US sponsor from BIZ PWS site (ECOM-75)
	@Test
	public void testRegisterAsPCWithUSSponsorFromBizPWSSite_3723() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		String bizPWS = null;
		String comPWS = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			String PWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
			bizPWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
			comPWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
			storeFrontHomePage.openConsultantPWS(bizPWS);
			if(driver.getCurrentUrl().contains("sitenotfound"))
				continue;
			else
				break;
		}	
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.clickAddToBagButton(country);

		//assert sign up link
		s_assert.assertFalse(storeFrontHomePage.verifySignUpLinkIsPresent(), "Sign up link is present on checkout page");
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnNotYourSponsorLink();
		//assert continue without sponser link is not present and request your sponser button
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue without sponser link present");
		s_assert.assertFalse(storeFrontHomePage.verifyRequestASponsorBtn(),"Request a sponser button is present");
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
		//s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		String currentPWSUrl=driver.getCurrentUrl();
		s_assert.assertTrue(storeFrontHomePage.verifyPWSAfterSuccessfulEnrollment(currentPWSUrl,comPWS.split("\\:")[1]),"PWS of the RC after enrollment is not same as the one it started enrollment");

		s_assert.assertAll();

	}

	//Hybris Project-3741:(Shipment) Register as PC customer with CA sponsor from BIZ PWS site (ECOM-75)
	@Test
	public void testRegisterAsPCWithSponsorFromBizPWSSite_3741() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		String bizPWS = null;
		String comPWS = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			String PWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
			bizPWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
			comPWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
			storeFrontHomePage.openConsultantPWS(bizPWS);
			if(driver.getCurrentUrl().contains("sitenotfound"))
				continue;
			else
				break;
		}	
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.clickAddToBagButton(country);

		//assert sign up link
		s_assert.assertFalse(storeFrontHomePage.verifySignUpLinkIsPresent(), "Sign up link is present on checkout page");
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnNotYourSponsorLink();
		//assert continue without sponser link is not present and request your sponser button
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue without sponser link present");
		s_assert.assertFalse(storeFrontHomePage.verifyRequestASponsorBtn(),"Request a sponser button is present");

		// Get Canadian sponser with PWS from database
		List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		String sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));
		String sponsorPWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
		String sponsorComPWS = storeFrontHomePage.convertBizSiteToComSite(sponsorPWS);

		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
		String idForConsultant = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
		storeFrontHomePage.searchCIDForSponserHavingPulseSubscribed(idForConsultant);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.checkPCPerksCheckBox();
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
		String currentPWSUrl=driver.getCurrentUrl();
		s_assert.assertTrue(storeFrontHomePage.verifyPWSAfterSuccessfulEnrollment(currentPWSUrl,bizPWS.split("\\:")[1]),"PWS of the RC after enrollment is not same as the one it started enrollment");
		storeFrontHomePage.clickPlaceOrderBtn();
		currentPWSUrl=driver.getCurrentUrl();
		s_assert.assertTrue(storeFrontHomePage.verifyPWSAfterSuccessfulEnrollment(currentPWSUrl,sponsorComPWS.split("\\:")[1]),"PWS of the RC after enrollment is not same as the one it started enrollment");
		s_assert.assertAll();
	}

	//Hybris Project-2151:Check the shipping method disclaimers for " UPS Ground (HD)/FedEX"
	@Test
	public void testCheckShippingMethodForUPSGroundHD_FedEX_2151() throws InterruptedException{
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
		logger.info("login is successful");

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPSGroundInOrderSummary();

		String selectedShippingMethodName = storeFrontUpdateCartPage.getSelectedShippingMethodName();

		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyShippingAddressContainsShippingMethodName(selectedShippingMethodName),"Checkout page doesn't contain shipping method name FedEx Ground (HD)");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickOnPlaceOrderButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyShippingAddressContainsShippingMethodNameAfterPlaceOrder(selectedShippingMethodName),"Thanku for your order page doesn't contain shipping method name FedEx Ground (HD)");

		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontUpdateCartPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethodOnTemplateAfterAdhocOrderPlaced(selectedShippingMethodName),"Adhoc order page doesn't contain shipping method name FedEx Ground (HD)");
		s_assert.assertAll();
	}

	//Hybris Project-2152:Check the shipping method disclaimers for " UPS 2Day/FedEx 2Day"
	@Test
	public void testCheckShippingMethodForUPS2Day_FedEx2Day_2152() throws InterruptedException{

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
		logger.info("login is successful");

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();
		String selectedShippingMethodName = storeFrontUpdateCartPage.getSelectedShippingMethodName();

		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyShippingAddressContainsShippingMethodName(selectedShippingMethodName),"Checkout page doesn't contain shipping method name FedEx Ground (HD)");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickOnPlaceOrderButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyShippingAddressContainsShippingMethodNameAfterPlaceOrder(selectedShippingMethodName),"Thanku for your order page doesn't contain shipping method name FedEx Ground (HD)");

		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontUpdateCartPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethodOnTemplateAfterAdhocOrderPlaced(selectedShippingMethodName),"Adhoc order page doesn't contain shipping method name FedEx Ground (HD)");
		s_assert.assertAll();

	}

	//Hybris Project-2157:Place An order as logged in PC User and check for PC Perk Promo
	@Test
	public void testPlacedAnAdhocOrderAsPCAndChekcForPCPerkPromo_2157() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertFalse(storeFrontUpdateCartPage.verifyPCPerksPromoDuringPlaceAdhocOrder(), "Pc perk promo is available on Main Account Info Page");
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		s_assert.assertFalse(storeFrontUpdateCartPage.verifyPCPerksPromoDuringPlaceAdhocOrder(), "Pc perk promo is available on Billing Info Page");
		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		s_assert.assertFalse(storeFrontUpdateCartPage.verifyPCPerksPromoDuringPlaceAdhocOrder(), "Pc perk promo is available on order confirmation Page");
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		//String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");

	}

	//Hybris Project-2312:Check Prices for PC
	@Test
	public void testPricesForPC_2312() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
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
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//validate user is able to see various product prices attached to the product. on quick shop page
		s_assert.assertTrue(storeFrontHomePage.validateRetailProductProcesAttachedToProduct(), "retail product prices attached to product is not displayed");
		//Add product to cart & checkout..
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//validate prices related to attched product on summary page..
		s_assert.assertTrue(storeFrontHomePage.validateProductPricingDetailOnSumaaryPage(), "Pricing information related to product is not present on summary page");
		s_assert.assertAll();
	}
	//Hybris Project-2313:Check Prices as Consultant & PC log in
	@Test
	public void testProductPricesAsConsultantAndPC_2313() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		//Login as Consultant..
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
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
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//validate user is able to see various product prices attached to the product. on quick shop page
		s_assert.assertTrue(storeFrontHomePage.validateRetailProductProcesAttachedToProduct(), "retail product prices attached to product is not displayed");
		//Add product to cart & checkout..
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//validate prices related to attched product on summary page..
		s_assert.assertTrue(storeFrontHomePage.validateProductPricingDetailOnSumaaryPage(), "Pricing information related to product is not present on summary page");
		storeFrontHomePage.hitBrowserBackBtn();
		logout();
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
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//validate user is able to see various product prices attached to the product. on quick shop page
		s_assert.assertTrue(storeFrontHomePage.validateRetailProductProcesAttachedToProduct(), "retail product prices attached to product is not displayed");
		s_assert.assertAll();
	}

	//Hybris Project-2314:Check Product prices without logging in
	@Test
	public void testProductPricesWithOutLoggingIn_2314(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//Click All Products link
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//validate user is able to see various product prices attached to the product..
		s_assert.assertTrue(storeFrontHomePage.validateRetailProductProcesAttachedToProduct(), "retail product prices attached to product is not displayed");
		s_assert.assertAll();
	}

	// Hybris Project-88:Standard Enrollment Billing Profile Billing Info - Edit
	@Test
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
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		storeFrontHomePage.clickOnReviewAndConfirmBillingEditBtn();
		storeFrontHomePage.selectNewBillingCardExpirationDateAfterEdit();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
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
	@Test
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
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.clickEnrollmentNextBtn();
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
		String newShippingAddName = firstName+randomNum;
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
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		storeFrontHomePage.clickOnReviewAndConfirmBillingEditBtn();
		storeFrontHomePage.clickAddNewShippingProfileLink();
		storeFrontHomePage.enterNewShippingAddressNameDuringEnrollment(newShippingAddName+" "+lastName);
		storeFrontHomePage.enterNewShippingAddressLine1DuringEnrollment(addressLine1);
		storeFrontHomePage.enterNewShippingAddressCityDuringEnrollment(city);
		storeFrontHomePage.selectProvince();
		storeFrontHomePage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontHomePage.enterNewShippingAddressPhoneNumber(phoneNumber);
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.validateNewShippingAddressPresentOnReviewPage(newShippingAddName),"new shipping address is not present on Review and confirm page");
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

	//Hybris Project-2281:Add/Edit Multiple shippiing address and during checkout
	@Test
	public void testAddMultipeShippingProfileDuringCheckout_2281() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int i=0;
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String profileName=null;
		String newShippingProfileName = TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME+randomNumber;
		String lastName = "lN";
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		if(driver.getCountry().equalsIgnoreCase("CA")){   
			profileName=TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_CA+randomNum;
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			profileName=TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_US+randomNum;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}
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

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		//storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();

		for(i=0;i<2;i++){
			storeFrontUpdateCartPage.clickAddNewShippingProfileLink();
			storeFrontUpdateCartPage.enterNewShippingAddressName(profileName+i+" "+lastName);
			storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
			storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
			storeFrontUpdateCartPage.selectNewShippingAddressState();
			storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
			storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(phoneNumber);
			storeFrontUpdateCartPage.clickOnSaveShippingProfile();
			s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedShippingProfileIsSelectedByDefault(profileName+i),"New Shipping Profile is not selected by default on CRP cart page");
		}
		i=1;
		storeFrontUpdateCartPage.clickOnEditOnNotDefaultAddressOfShipping();
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.selectNewShippingAddressState();
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(phoneNumber);
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedShippingProfileIsSelectedByDefault(newShippingProfileName),"New Edited Shipping Profile is not selected by default on CRP cart page");
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewEditedShippingProfileIsPresentOnOrderConfirmationPage(newShippingProfileName),"New Edited Shipping Profile is not Present by default on Order Summary page");
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(),"Order is not placed successfully");
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		//------------------ Verify that adhoc orders template doesn't contains the newly Edited Shipping profile by verifying by name------------------------------------------------------------
		s_assert.assertTrue(storeFrontOrdersPage.isShippingAddressContainsName(newShippingProfileName),"AdHoc Orders Page do not contain the newly edited shipping address");
		//------------------Verify that Shipping info page contains the newly created Shipping profile
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"Shipping Info page has not been displayed");
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingProfileName),"Newly added Shipping profile is NOT listed on the Shipping info page");
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(profileName+i),"Newly added Shipping profile is NOT listed on the Shipping info page");

		s_assert.assertAll();
	}


	//Hybris Project-3775:Register as RC with Different CA Sponsor WITHOUT Pulse
	@Test
	public void testRegisterAsRCWithDifferentCASponsorWithoutPulse_3775() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO(); 

			//create Sponsor without pulse
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			country = driver.getCountry();
			enrollmentType = TestConstants.STANDARD_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

			String  consultantEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			//storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME, consultantEmailAddress, password, addressLine1, city, postalCode, phoneNumber);
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
			storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
			storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
			storeFrontHomePage.checkTheIAgreeCheckBox();
			storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
			storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
			//storeFrontHomePage.clickOnConfirmAutomaticPayment();
			s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");

			// create RC User
			int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String firstNameRC=TestConstants.FIRST_NAME+randomNumber;
			String lastName = "lN";
			storeFrontHomePage = new StoreFrontHomePage(driver);

			//open pws site
			String pws = storeFrontHomePage.openPWSSite(driver.getCountry(), driver.getEnvironment());

			// Click on our product link that is located at the top of the page and then click in on quick shop
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");

			//Select a product and proceed to buy it
			storeFrontHomePage.selectProductAndProceedToBuy();

			//assert sign up link
			s_assert.assertFalse(storeFrontHomePage.verifySignUpLinkIsPresent(), "Sign up link is present on checkout page");

			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();

			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");

			//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
			String rcEmailAddress = firstNameRC+"@xyz.com";
			storeFrontHomePage.enterNewRCDetails(firstNameRC, TestConstants.LAST_NAME+randomNum, rcEmailAddress, password);

			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");

			//assert not your sponsor link is present
			s_assert.assertTrue(storeFrontHomePage.verifyNotYourSponsorLinkIsPresent(), "Not your sponsor link is not present");

			//assert continue without sponsor link is not present
			s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponsorLinkIsPresent(), "Continue without sponsor link is present");

			// sponser search by Account Number
			List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,consultantEmailAddress),RFO_DB);
			String sponsorId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

			storeFrontHomePage.clickOnNotYourSponsorLink();
			storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponsorId);
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
			s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			s_assert.assertTrue(storeFrontHomePage.verifyBizUrlAfterEnrollment(pws), "User NOT stays on the checkout PWS");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}

	}

	// Hybris Project-2271:check with Browser back button from checkout to cart
	@Test
	public void testBrowserBackButtonFromCheckoutToCart_2271() throws InterruptedException		{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
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
		//click All Products link 
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
		//hit browser back button..
		storeFrontHomePage.hitBrowserBackBtn();
		//validate user is navigated to shopping cart page..
		storeFrontHomePage.validateMiniCart();
		s_assert.assertAll();
	}

	// Hybris Project-2283:Add Multiple shippiing address from myaccounts
	@Test
	public void testAddMultipeShippingAddressFromMyAccounts_2283() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int i=0;
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String profileName=null;
		String lastName = "lN";
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		if(driver.getCountry().equalsIgnoreCase("CA")){   
			profileName=TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_CA+randomNum;
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			profileName=TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_US+randomNum;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}
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

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage=storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();

		//Add multiple Shipping profile from my accounts.
		for(i=0;i<2;i++){
			storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
			storeFrontShippingInfoPage.enterNewShippingAddressName(profileName+i+" "+lastName);
			storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
			storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
			storeFrontShippingInfoPage.selectNewShippingAddressState();
			storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
			storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(phoneNumber);
			storeFrontShippingInfoPage.clickOnSaveShippingProfile();
			s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(profileName+i),"New Created Shipping Profile is not present on shipping info page");
		}
		i=1;
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontUpdateCartPage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(profileName+"0"),"Newly created shipping address is not present on shipment section during checkout");
		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(profileName+i),"Newly created shipping address is not present on shipment section during checkout");
		storeFrontUpdateCartPage.clickOnNewShipToThisAddressRadioButton(profileName+"0");
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewEditedShippingProfileIsPresentOnOrderConfirmationPage(profileName+"0"),"New Edited Shipping Profile is not Present by default on Order Summary page");
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(),"Order is not placed successfully");
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//------------------ Verify that adhoc orders template doesn't contains the newly Edited Shipping profile by verifying by name------------------------------------------------------------
		s_assert.assertTrue(storeFrontOrdersPage.isShippingAddressContainsName(profileName+"0"),"AdHoc Orders Page do not contain the newly edited shipping address");
		s_assert.assertAll();
	}


	//Hybris Project-2286:Place an Order with existing customer - AUTOMATION ONLY
	@Test
	public void testPlaceOrderWithExistingCustomer_2286() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> accountNameDetailsList = null;
		List<Map<String, Object>> randomConsultantList =  null;
		String firstNameDB = null;
		String lastNameDB = null;
		String consultantEmailID = null;
		String accountID = null;
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
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
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

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontUpdateCartPage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		storeFrontUpdateCartPage.clickOnCheckoutButton();


		//assert First Name with RFO
		accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_DETAILS_QUERY,consultantEmailID), RFO_DB);
		firstNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "FirstName");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyFirstNameAndLastNameAndEmailAddressFromUIOnAccountInfoPage(firstNameDB), "First Name on UI is different from DB");

		// assert Last Name with RFO
		lastNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "LastName");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyFirstNameAndLastNameAndEmailAddressFromUIOnAccountInfoPage(lastNameDB), "Last Name on UI is different from DB");
		storeFrontUpdateCartPage.clickEditMainAccountInfoOnCartPage();

		//assert for email address
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyFirstNameAndLastNameAndEmailAddressFromUIOnAccountInfoPage(consultantEmailID), "Email Address on UI is different from DB");

		//verify that user can edit main account info.
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyUserCanEditMainAccountInfoOnCartPage(),"User cannot edit main account info");
		s_assert.assertAll();
	}

	// Hybris Project-2291:Adhoc shopping cart is persistent when user is navigating among PWS sites ad corp site
	@Test
	public void testAdhocShoppingCartIsPersistentForBizAndCom_2291() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment();
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
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		//verify Adhoc Shopping cart for corp site.
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		logout();

		//verify Adhoc shopping cart for biz site.
		String bizPWS=storeFrontHomePage.openPWSSite(country, env);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		logout();

		//verify Adhoc shopping cart for com site.
		String comPWS=storeFrontHomePage.convertBizSiteToComSite(bizPWS);
		storeFrontHomePage.openConsultantPWS(comPWS);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		s_assert.assertAll();
	}

	//Hybris Project-3742:Update the PC Perks Template from different CA sponsor's BIZ PWS
	@Test
	public void testUpdatePCPerksTemplateFromDifferentCASponsorBizPWS_3742() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomPCUserList =  null;
			String pcUserEmailID = null;
			String accountId = null;
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String firstName=TestConstants.FIRST_NAME+randomNum;
			String lastName = "lN";
			storeFrontHomePage = new StoreFrontHomePage(driver);
			while(true){
				randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
				accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
				logger.info("Account Id of the user is "+accountId);
				storeFrontHomePage.openPWSSite(driver.getCountry(), driver.getEnvironment());

				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
				boolean isError = driver.getCurrentUrl().contains("error");
				if(isError){
					logger.info("login error for the user "+pcUserEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}	
			//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
			logger.info("login is successful");
			String PWSAfterLogin = driver.getCurrentUrl();
			logger.info("after login "+PWSAfterLogin);
			storeFrontPCUserPage.clickOnAutoshipCart();
			storeFrontPCUserPage.clickOnContinueShoppingLink();
			storeFrontPCUserPage.clickOnAddToPcPerksButton();
			String updatedMsg = TestConstants.PC_PERKS_TEMPLATE_PRODUCT_ADDED;
			s_assert.assertTrue(storeFrontPCUserPage.verifyUpdateCartMessage(updatedMsg),"Autoship Cart has not been Updated");
			storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
			storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
			storeFrontUpdateCartPage.clickUpdateCartBtn();
			s_assert.assertTrue(storeFrontUpdateCartPage.verifyCartUpdateMessage(),"Autoship Cart has not been Updated after click on update cart");
			storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
			logout();

			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
			storeFrontHomePage.clickAddToBagButton(driver.getCountry());

			//assert sign up link
			s_assert.assertFalse(storeFrontHomePage.verifySignUpLinkIsPresent(), "Sign up link is present on checkout page");
			storeFrontHomePage.clickOnCheckoutButton();

			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");

			//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
			storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");

			storeFrontHomePage.clickOnNotYourSponsorLink();
			//assert continue without sponser link is not present and request your sponser button
			s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue without sponser link present");
			s_assert.assertFalse(storeFrontHomePage.verifyRequestASponsorBtn(),"Request a sponser button is present");

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
			//s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
			String currentPWSUrl=driver.getCurrentUrl();
			logger.info("current url After "+currentPWSUrl);
			s_assert.assertTrue(storeFrontHomePage.verifyPWSAfterSuccessfulEnrollment(currentPWSUrl,PWSAfterLogin.split("\\:")[1]),"PWS of the RC after enrollment is not same as the one it started enrollment");		
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-3762:Update the PC Perks Template from US sponsor's BIZ PWS who has Pulse/ PWS
	@Test
	public void testUpdatePCPerksTemplateFromDifferentUSSponsorBizPWS_3762() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("us")){
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomPCUserList =  null;
			String pcUserEmailID = null;
			String accountId = null;
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String firstName=TestConstants.FIRST_NAME+randomNum;
			String lastName = "lN";
			storeFrontHomePage = new StoreFrontHomePage(driver);
			while(true){
				randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
				accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
				logger.info("Account Id of the user is "+accountId);
				storeFrontHomePage.openPWSSite(driver.getCountry(), driver.getEnvironment());

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
			String PWSAfterLogin = driver.getCurrentUrl();
			logger.info("after login "+PWSAfterLogin);
			storeFrontPCUserPage.clickOnAutoshipCart();
			storeFrontPCUserPage.clickOnContinueShoppingLink();
			storeFrontPCUserPage.clickOnAddToPcPerksButton();
			String updatedMsg = TestConstants.PC_PERKS_TEMPLATE_PRODUCT_ADDED;
			s_assert.assertTrue(storeFrontPCUserPage.verifyUpdateCartMessage(updatedMsg),"Autoship Cart has not been Updated");
			storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
			storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
			storeFrontUpdateCartPage.clickUpdateCartBtn();
			s_assert.assertTrue(storeFrontUpdateCartPage.verifyCartUpdateMessage(),"Autoship Cart has not been Updated after click on update cart");
			storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
			logout();

			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
			storeFrontHomePage.clickAddToBagButton(driver.getCountry());

			//assert sign up link
			s_assert.assertFalse(storeFrontHomePage.verifySignUpLinkIsPresent(), "Sign up link is present on checkout page");
			storeFrontHomePage.clickOnCheckoutButton();

			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");

			//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
			storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
			storeFrontHomePage.enterMainAccountInfo();
			logger.info("Main account details entered");

			storeFrontHomePage.clickOnNotYourSponsorLink();
			//assert continue without sponser link is not present and request your sponser button
			s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue without sponser link present");
			s_assert.assertFalse(storeFrontHomePage.verifyRequestASponsorBtn(),"Request a sponser button is present");

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
			//s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
			String currentPWSUrl=driver.getCurrentUrl();
			logger.info("current url After "+currentPWSUrl);
			s_assert.assertTrue(storeFrontHomePage.verifyPWSAfterSuccessfulEnrollment(currentPWSUrl,PWSAfterLogin.split("\\:")[1]),"PWS of the RC after enrollment is not same as the one it started enrollment");		
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for US env");
		}
	}


	// Hybris Project-1287: Terms and Conditions (Express enrollment)
	@Test
	public void testTermsAndConditions_ExpressEnrollment_1287() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
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

	//Hybris Project-2167:Login as Existing PC and Place an Adhoc Order, Check for Alert message
	@Test
	public void testLoginAsPCAndPlaceAdhocOrder_CheckAlrtMessage_2167() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnPlaceOrderButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmationPOPupPresent(),"pop up not present");
		storeFrontUpdateCartPage.clickOnOkButtonOnCheckoutConfirmationPopUp();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		s_assert.assertAll();
	}

	//Hybris Project-2186:PC2RC: RC enrollment add already exist PC email ID: Verify Popup
	@Test
	public void testRcEnrollmentAddAlreadyExistPcEmailId_VerifyPopUp_2186() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		storeFrontHomePage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");
		storeFrontHomePage.enterNewRCDetails(firstName, pcUserEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyPresenceOfPopUpForExistingPC(),"Pop Up is not present");
		s_assert.assertAll();
	}


	//Hybris Project-2263:Check PC Perk 1st Order confirmation Screen
	@Test
	public void testCheckPcPerkIstOrderConfirmationScreen_2263() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
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

	//Hybris Project-1282:16.North Dakota rule out US
	@Test
	public void testNorthDakotaRuleOut_US_1282() throws InterruptedException{
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.selectCountryUsToCan();
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		s_assert.assertFalse(storeFrontHomePage.verifyPresenceOfNorthDakotaLink(),"I live in North Dakota and want to continue without purchasing a business portfolio Link coming up");
		s_assert.assertAll();
	}

}
