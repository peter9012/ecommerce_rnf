package com.rf.test.website.storeFront.hybris;

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
import com.rf.pages.website.cscockpit.CSCockpitCustomerSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerTabPage;
import com.rf.pages.website.cscockpit.CSCockpitLoginPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderTabPage;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontAccountTerminationPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class AccountTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AccountTest.class.getName());
	public String emailID=null;
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontAccountTerminationPage storeFrontAccountTerminationPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitCustomerSearchTabPage cscockpitCustomerSearchTabPage;
	private CSCockpitCustomerTabPage cscockpitCustomerTabPage;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String state = null;
	private String postalCode = null;
	private String phoneNumber = null;
	private String country = null;
	private String RFO_DB = null;
	private String env = null;

	//Test Case Hybris Phase 2-3720 :: Version : 1 :: Perform Consultant Account termination through my account
	@Test(enabled=false)//Duplicate test,covered in Enrollment validation TC-4308
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
	@Test(enabled=false)//Duplicate test,covered in Enrollment validation TC-4318
	public void testAccountTerminationPageForPCUser_3719() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
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

	//Hybris Phase 2-1977 :: verify with Valid credentials and Logout.
	@Test
	public void testVerifyLogoutwithValidCredentials_1977() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomPCList =  null;
		String consultantEmailID = null;
		String pcEmailID = null;
		String accountID = null;
		String bizPWS = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			bizPWS = (String) getValueFromQueryResult(randomConsultantList, "URL"); 
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		s_assert.assertTrue(driver.getCurrentUrl().contains("myrfo"+driver.getEnvironment()+".com/"+driver.getCountry()+"/"), "current url doesn't contains expected .com but actual URL is "+driver.getCurrentUrl());
		logout();
		s_assert.assertTrue(!driver.getCurrentUrl().contains("corprfo"),"Consultant user is not on .com site after logout");
		driver.get(bizPWS);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		s_assert.assertTrue(driver.getCurrentUrl().contains("myrfo"+driver.getEnvironment()+".biz/"+driver.getCountry()+"/"), "current url doesn't contains expected .biz but actual URL is "+driver.getCurrentUrl());
		logout();
		s_assert.assertTrue(!driver.getCurrentUrl().contains("corprfo"),"Consultant user is not on .biz site after logout");
		//For PC user.
		storeFrontHomePage.openComPWSSite(driver.getCountry(), driver.getEnvironment());
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcEmailID);
				storeFrontHomePage.openComPWSSite(driver.getCountry(), driver.getEnvironment());
			}
			else
				break;
		}
		logger.info("login is successful");
		s_assert.assertTrue(driver.getCurrentUrl().contains("myrfo"+driver.getEnvironment()+".com/"+driver.getCountry()+"/"), "current url doesn't contains expected .com but actual URL is "+driver.getCurrentUrl());
		logout();
		s_assert.assertTrue(!driver.getCurrentUrl().contains("corprfo"),"PC user is not on .com site after logout");
		driver.get(bizPWS);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		s_assert.assertTrue(driver.getCurrentUrl().contains("myrfo"+driver.getEnvironment()+".biz/"+driver.getCountry()+"/"), "current url doesn't contains expected .biz but actual URL is "+driver.getCurrentUrl());
		logout();
		s_assert.assertTrue(!driver.getCurrentUrl().contains("corprfo"),"PC user is not on .biz site after logout");
		s_assert.assertAll();
	}

	//Hybris Project-2512 :: Version : 1 :: Username validations.
	@Test 
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
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
		//Enter data more than 8 chars with only special chars and validate error msg
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_SPECIAL_CHARS);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 8 special characters IS NOT PRESENT");
		//Enter data more than 8 chars(alphanumeric with atleast a special char)
		storeFrontAccountInfoPage.enterUserName(TestConstants.CONSULTANT_USERNAME_MORE_THAN_6_ALPHANUMERIC_CHARS_WITH_SPCLCHAR);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Validation for username more than 6 alphanumneric characters with atleast a special char IS NOT PRESENT");
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		anotherConsultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
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

	// Hybris Phase 2-2228 :: Version : 1 :: Perform RC Account termination through my account
	@Test(enabled=false)//Duplicate test,covered in UpgradeDowngrade TC-4694
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}	
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
	@Test(enabled=false)//Duplicate test,covered in TC_4308
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}	
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

	// Hybris Project-1276:Email field validation for Active/Inactive users
	@Test
	public void testEmailValidationsDuringEnroll_1276() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> randomConsultantEmailList =  null;
		List<Map<String, Object>> randomPCEmailList =  null;
		List<Map<String, Object>> randomRCEmailList =  null;
		List<Map<String, Object>> randomPCUserList =  null;
		List<Map<String, Object>> randomRCUserList =  null;
		List<Map<String, Object>> randomConsultantListMoreThan180days =  null;
		List<Map<String, Object>> accountContactIDList =  null;
		List<Map<String, Object>> emailAddressIDList =  null;
		List<Map<String, Object>> consultantEmailList =  null;
		String consultantEmailID = null;
		String inactiveConsultantMoreThan180DaysEmailID =null;
		String inActive180consultantAccountId = null;
		String pcUserEmailID= null;
		String rcUserEmailID = null;
		String consultantAccountID = null;
		String pcAccountId = null;
		String pcUserEmailAddress = null;
		String accountContactID = null;
		String emailAddressID = null;
		String activePCAccountId = null;
		String activeRCAccountId = null;
		String country = driver.getCountry();
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
		//Terminate consultant user.
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantAccountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			randomConsultantEmailList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,consultantAccountID),RFO_DB);
			consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantEmailList, "EmailAddress"));
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickConfirmTerminationBtn();  
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		//Terminate PC User
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcAccountId = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID"));
			randomPCEmailList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,pcAccountId),RFO_DB);
			pcUserEmailAddress = String.valueOf(getValueFromQueryResult(randomPCEmailList, "EmailAddress"));
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailAddress, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailAddress);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		storeFrontPCUserPage.cancelMyPCPerksAct();
		driver.get(driver.getURL()+"/"+driver.getCountry());
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_NAME_PERSONAL, TestConstants.REGIMEN_NAME);  
		storeFrontHomePage.chooseEnrollmentOption(TestConstants.STANDARD_ENROLLMENT);
		// assertion for Active PC
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		activePCAccountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID")); 
		randomPCEmailList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,activePCAccountId),RFO_DB);
		pcUserEmailID = String.valueOf(getValueFromQueryResult(randomPCEmailList, "EmailAddress"));
		storeFrontHomePage.enterEmailAddress(pcUserEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForExistingActivePC() , "Existing Active PC User email id should not be acceptable");
		// assertion for Inactive PC less than 90 days
		storeFrontHomePage.enterEmailAddress(pcUserEmailAddress);
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForExistingInactivePC90Days() , "Inactive pc less than 90 days User email id should not be acceptable");
		// assertion for Inactive Consultant less than 180 days
		storeFrontHomePage.enterEmailAddress(consultantEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForExistingActiveCCLessThan6Month() , "Inactive Consultant less than 180 days User email id should not be acceptable");
		// assertion for Inactive consultant greater than 6 month
		randomConsultantListMoreThan180days = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_CONSULTANTS_INACTIVE_MORE_THAN_180_DAYS,countryId),RFO_DB);
		inActive180consultantAccountId = String.valueOf(getValueFromQueryResult(randomConsultantListMoreThan180days, "AccountID"));
		logger.info("Account Id for Inactive consultant greater than 6 month = "+inActive180consultantAccountId);
		if(inActive180consultantAccountId.equals(null)==false){
			accountContactIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_CONTACT_ID_RFO,inActive180consultantAccountId),RFO_DB);
			accountContactID = String.valueOf(getValueFromQueryResult(accountContactIDList, "AccountConTactId"));
			emailAddressIDList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ADDRESS_ID_RFO,accountContactID),RFO_DB);
			emailAddressID = String.valueOf(getValueFromQueryResult(emailAddressIDList, "EmailAddressId"));
			consultantEmailList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_RFO,emailAddressID),RFO_DB);
			inactiveConsultantMoreThan180DaysEmailID = String.valueOf(getValueFromQueryResult(consultantEmailList, "EmailAddress"));
			storeFrontHomePage.enterEmailAddress(inactiveConsultantMoreThan180DaysEmailID);
			s_assert.assertFalse(storeFrontHomePage.verifyPopUpForExistingInactiveCC180Days() , "Inactive Consultant more than 6 month User email id should be acceptable");
		}
		// assertion for Active RC
		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
		activeRCAccountId = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
		randomRCEmailList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,activeRCAccountId),RFO_DB);
		rcUserEmailID = String.valueOf(getValueFromQueryResult(randomRCEmailList, "EmailAddress"));
		storeFrontHomePage.enterEmailAddress(rcUserEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForExistingActiveRC() , "Existing Active RC User email id should not be acceptable");
		s_assert.assertAll();
	}

	//Hybris Project-1976:Navigation to Consultant Pulse page from My Account
	@Test
	public void testAutoshipModuleCheckMyPulseUI_1976() throws InterruptedException  {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickCheckMyPulseLinkPresentOnWelcomeDropDown();
		//pass driver control to child window
		storeFrontConsultantPage.switchToChildWindow();
		//validate home page for pulse
		s_assert.assertTrue(storeFrontConsultantPage.validatePulseHomePage(),"Home Page for pulse is not displayed");
		//s_assert.assertTrue(storeFrontAccountInfoPage.validateSubscribeToPulse(),"pulse subscription is not Present for the user");
		storeFrontConsultantPage.switchToPreviousTab();
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		String Username = "testUser"+randomNumber;
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		storeFrontAccountInfoPage.enterUserName(Username);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		logout();
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(Username,password);   
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		//Enter new user name logout and validate the new user
		storeFrontAccountInfoPage.enterUserName(newUsername);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		//validate the new user
		s_assert.assertTrue(newUsername.equalsIgnoreCase(storeFrontAccountInfoPage.getUserName()),"Username didn't match");
		driver.get(driver.getURL()+"/"+driver.getCountry());
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(Username,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Old Username doesn't get Login failed");
		s_assert.assertAll();
	}

	//Hybris Project-86:Verify editing Spouse details under Account Info
	@Test
	public void testEditAllowMySpouseInMyAccount_86() throws InterruptedException {
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		storeFrontAccountInfoPage.checkAllowMySpouseCheckBox();
		//click cancel on the 'Allow my spouse' popup and validate
		s_assert.assertTrue(storeFrontAccountInfoPage.validateClickCancelOnProvideAccessToSpousePopup(),"Cancel button not working in the popup");
		//Click X on allow my spouse popup
		storeFrontAccountInfoPage.checkAllowMySpouseCheckBox();
		s_assert.assertTrue(storeFrontAccountInfoPage.validateClickXOnProvideAccessToSpousePopup(),"X button not working in the popup");
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
		String productName = storeFrontHomePage.selectProductAndProceedToBuy();
		logger.info("product name is "+productName.toLowerCase().trim());
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		s_assert.assertTrue(storeFrontHomePage.getNameOfTheOnlyAddedProductOnCart().toLowerCase().trim().contains(productName.toLowerCase().trim()),"Added product name is "+productName.toLowerCase().trim()+" while on cart is "+storeFrontHomePage.getNameOfTheOnlyAddedProductOnCart().toLowerCase().trim());
		s_assert.assertTrue(storeFrontHomePage.validateMiniCart(), "mini cart is not being displayed");
		s_assert.assertTrue(storeFrontHomePage.getNumberOfProductsDisplayedOnMiniCart().contains("1"), "number of products displayed in the mini cart expected is 1 but getting "+storeFrontHomePage.getNumberOfProductsDisplayedOnMiniCart());
		storeFrontHomePage.mouseHoverOnMiniCart();
		s_assert.assertTrue(storeFrontHomePage.getNameOfOnlyProductAddedOnMiniCart().toLowerCase().trim().contains(productName.toLowerCase().trim()),"Added product name is "+productName.toLowerCase().trim()+" while on mini cart is "+storeFrontHomePage.getNameOfOnlyProductAddedOnMiniCart().toLowerCase().trim());
		storeFrontHomePage.clickOnViewShippingCartBtnOnMiniCart();
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(),"cart page is not displayed after clicking view shipping cart button on mini cart");
		//click on mini cart and validate the cart page with pre-added products
		s_assert.assertTrue(storeFrontHomePage.clickMiniCartAndValidatePreaddedProductsOnCartPage(), "preadded products on cart page is not displayed");  
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}

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

	// Hybris Project-2134:EC-789- To Verify subscribe to pulse
	@Test
	public void testVerifySubscribeToPulse_2134() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO();  
		List<Map<String, Object>> randomConsultantList =  null;
		//List<Map<String, Object>> randomConsultantPWSList =  null;
		String consultantWithPWSEmailID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Get Consultant with PWS from database
		randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
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

	//Hybris Project-4660 :: Version : 1 :: Change the username of RC user and Login with updated username
	@Test
	public void testchangeUsernameOfRcUserWithUpdatedUserName_4660() throws InterruptedException{
		int randomNumber =  CommonUtils.getRandomNum(10000, 1000000);
		String newUserName = TestConstants.NEW_RC_USER_NAME+randomNumber;
		String updatedUsernameFromDB = null;
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> updatedUserList =  null;
		String rcEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
			rcEmailID = (String) getValueFromQueryResult(randomRCList, "Username");
			accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID,password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		String oldUserNameOnUI = storeFrontHomePage.fetchingUserName();
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.enterPhoneNumberAndPostalCode();
		storeFrontAccountInfoPage.enterNewUserNameAndClicKOnSaveButton(newUserName);
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Your Profile has not been Updated");
		logout();
		storeFrontHomePage.loginAsRCUser(newUserName, password);
		String newUserNameOnUI = storeFrontHomePage.fetchingUserName();
		s_assert.assertTrue(storeFrontHomePage.verifyUserNameAfterLoginAgain(oldUserNameOnUI,newUserNameOnUI),"Login is not successful with new UserName");
		//Verify updated username in RFO database.
		updatedUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_UPDATED_USERNAME_FROM_RFO,accountID),RFO_DB);
		updatedUsernameFromDB =(String) getValueFromQueryResult(updatedUserList, "Username");
		s_assert.assertTrue(updatedUsernameFromDB.equalsIgnoreCase(newUserName),"Username is not updated in RFO database.");
		s_assert.assertAll();
	}

	//Hybris Project-2234:Verify that user can cancel PC Perks subscription through my account
	@Test
	public void testPCUserCancelPcPerks_2234() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String pcUserEmailId = firstName+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password, pcUserEmailId);
		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
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
		storeFrontPCUserPage=new StoreFrontPCUserPage(driver);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontPCUserPage.clickOnYourAccountDropdown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
		storeFrontPCUserPage.cancelMyPCPerksAct();
		storeFrontHomePage.loginAsConsultant(pcUserEmailId, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();
	}

	//Hybris Phase 2-2235:Verify that user can change the information in 'my account info'.
	@Test
	public void testAccountInformationForUpdate_2235() throws InterruptedException{
		int randomNumPhone = CommonUtils.getRandomNum(10000, 99999);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String country = null;
		String consultantEmailID = null;
		String accountID = null;
		String city = null;
		String stateName = null;
		String postalCode = null;
		String phoneNumber = null;
		String addressLine1 = null;
		country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			city = TestConstants.CONSULTANT_CITY_FOR_ACCOUNT_INFORMATION_CA;
			postalCode = TestConstants.CONSULTANT_POSTAL_CODE_FOR_ACCOUNT_INFORMATION_CA;
			phoneNumber = "99999"+randomNumPhone;
			addressLine1 =  TestConstants.CONSULTANT_ADDRESS_LINE1_FOR_ACCOUNT_INFORMATION_CA;
			stateName = TestConstants.CONSULTANT_STATE_FOR_ACCOUNT_INFORMATION_CA;
		}else{
			city = TestConstants.CONSULTANT_CITY_FOR_ACCOUNT_INFORMATION_US;
			postalCode = TestConstants.CONSULTANT_POSTAL_CODE_FOR_ACCOUNT_INFORMATION_US;
			phoneNumber = "99999"+randomNumPhone;
			addressLine1 =  TestConstants.CONSULTANT_ADDRESS_LINE1_FOR_ACCOUNT_INFORMATION_US;
			stateName = TestConstants.CONSULTANT_STATE_FOR_ACCOUNT_INFORMATION_US;
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(), "Account Info page has not been displayed");
		String firstName = TestConstants.CONSULTANT_FIRST_NAME_FOR_ACCOUNT_INFORMATION+randomNum;
		String lastName = TestConstants.CONSULTANT_LAST_NAME_FOR_ACCOUNT_INFORMATION+randomNum;
		storeFrontAccountInfoPage.updateFirstName(firstName);
		storeFrontAccountInfoPage.updateLastName(lastName);
		storeFrontAccountInfoPage.updateAddressWithCityAndPostalCode(addressLine1, city, postalCode);
		String state = storeFrontAccountInfoPage.updateStateAndReturnName(stateName);
		logger.info("State/province selected is "+state);
		storeFrontAccountInfoPage.updateMainPhnNumber(phoneNumber);
		storeFrontAccountInfoPage.updateDateOfBirthAndGender();
		storeFrontAccountInfoPage.uncheckSpouseCheckBox();
		storeFrontAccountInfoPage.clickSaveAccountBtn();
		s_assert.assertTrue(storeFrontAccountInfoPage.isProfileHasUpdatedMessagePresent(), "'Your profile has been updated' message has not appeared after saving the account info");
		//assert First Name with RFO
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyFirstNameFromUIForAccountInfo(firstName), "First Name on UI is not updated");
		// assert Last Name with RFO
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyLasttNameFromUIForAccountInfo(lastName), "Last Name on UI is not updated");
		// assert City with RFO
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCityFromUIForAccountInfo(city), "City on UI is not updated");
		// assert State with RFO
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProvinceFromUIForAccountInfo(state), "State/Province on UI is not updated");
		//assert Postal Code with RFO
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyPostalCodeFromUIForAccountInfo(postalCode), "Postal Code on UI is not updated");
		// assert Main Phone Number with RFO
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyMainPhoneNumberFromUIForAccountInfo(phoneNumber), "Phone Number on UI is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4663:Consultant Account Termination
	@Test(enabled=false)//Duplicate test,covered in Enrollment validation TC-4308
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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

	//Hybris Project-4803:PC account termination for US new PC
	@Test
	public void testPCAccountTermination_4803() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		//  storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
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
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
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

	//Hybris Project-4648:Cancellation Message for PC account Termination
	@Test(enabled=false)//Duplicate test,covered in Enrollment validation TC-4318
	public void testCancellationMessageforPCaccountTermination_4648() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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

	// Hybris Project-4647:PC Account Termination
	@Test(enabled=false)//Duplicate test,covered in Enrollment validation TC-4318
	public void testPCAccountTermination_4647() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		//		int randomNum = CommonUtils.getRandomNum(1,6);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User User and verify Product Details and category.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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

	//Hybris Project-5003:Add Birthday and gender and allow my sponsor information
	//Hybris Project-5002:Modify Main Phone Number and mobile phone information
	@Test
	public void testAddBirthDayAndGenderAndAllowMySponsorInformation_5003_5002() throws InterruptedException{
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
					driver.get(driver.getURL()+"/"+driver.getCountry());
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
			firstNameFromUI=storeFrontAccountInfoPage.getFirstNameFromAccountInfo();
			lastNameFromUI=storeFrontAccountInfoPage.getLastNameFromAccountInfo();
			legalName=firstNameFromUI+" "+lastNameFromUI; 
			storeFrontAccountInfoPage.enterBirthDateOnAccountInfoPage();
			storeFrontAccountInfoPage.clickOnGenderRadioButton(TestConstants.CONSULTANT_GENDER);
			storeFrontAccountInfoPage.clickOnAllowMySpouseOrDomesticPartnerCheckbox();
			storeFrontAccountInfoPage.enterSpouseFirstName(TestConstants.SPOUSE_FIRST_NAME);
			storeFrontAccountInfoPage.enterSpouseLastName(TestConstants.SPOUSE_LAST_NAME);
			storeFrontAccountInfoPage.clickOnSaveAfterEnterSpouseDetails();
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
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not present on UI");
			s_assert.assertAll();
		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-5002:Modify Main Phone Number and mobile phone information
	@Test(enabled=false)//Covered in TC - 5003
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
					driver.get(driver.getURL()+"/"+driver.getCountry());
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
					driver.get(driver.getURL()+"/"+driver.getCountry());
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
			s_assert.assertAll();
			//***REMOVED FURTHER ACTIONS AS VERIFY NAME IS PURPOSE OF TEST***
		}else{
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
					driver.get(driver.getURL()+"/"+driver.getCountry());
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

	// Hybris Project-4359:New autoship date for Reactivated Consultant
	@Test(enabled=false)//Test No longer valid
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
					driver.get(driver.getURL()+"/"+driver.getCountry());
				}
				else
					break;
			}
			//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontOrdersPage = storeFrontHomePage.clickOrdersLinkPresentOnWelcomeDropDown();
			String dueAutoshipDateFromBeforeTerminationUI = storeFrontOrdersPage.getAutoshipOrderDate();
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
			String dueAutoshipDateFromAfterReactivationUI = storeFrontOrdersPage.getAutoshipOrderDate();
			s_assert.assertTrue(dueAutoshipDateFromAfterReactivationUI.contains(dueAutoshipDateFromBeforeTerminationUI), "Autoship date after reactivation is not same as before account termination");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-4659:Change the username of RC user and Login with updated username
	@Test
	public void testChangeTheUserNameOfRCandLoginWithUpdateUsername_4659() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		country = driver.getCountry();
		env = driver.getEnvironment();
		String newUserName = TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, env);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontRCUserPage.enterNewUserNameAndClickSaveButton(newUserName);
		s_assert.assertTrue(storeFrontRCUserPage.isProfileHasUpdatedMessagePresent(), "'Your profile has been updated' message has not appeared after saving the account info");		
		logout();
		storeFrontHomePage.loginAsRCUser(newUserName, password);
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-3877:COM:Different sponsor other than PWS site owner - RC2PC via OrderSummary Section
	@Test
	public void testJoinPCPerksInOrderSummarySection_3877() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =null;
		List<Map<String, Object>> sponsorIdList=null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNumber;
		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		//Get a Sponser for pc user.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
		String emailAddressOfSponser= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String comPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		logger.info("COM PWS of sponsor is "+comPWSOfSponser);
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String sponserId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
		//Get .biz PWS from database to start enrolling rc user and upgrading it to pc user
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
		String emailAddressOfConsultant= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String comPWSOfConsultant=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		//Open com pws of Sponser
		while(true){
			storeFrontHomePage.openConsultantPWS(comPWSOfConsultant);
			if(driver.getCurrentUrl().toLowerCase().contains("error")||driver.getCurrentUrl().toLowerCase().contains("sitenotfound")||driver.getCurrentUrl().toLowerCase().contains("sitenotactive")){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
				emailAddressOfConsultant= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
				comPWSOfConsultant=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
				continue;
			}
			else{
				break;
			}
		}
		//Hover shop now and click all products link.
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//Select a product and proceed to buy it
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
		s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(emailAddressOfConsultant),"Default consultant is not the one whose pws is used");
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
		//verify the  sponser is selected.
		s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(emailAddressOfSponser),"Sponser is not selected");
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		s_assert.assertTrue(storeFrontHomePage.isShippingAddressNextStepBtnIsPresent(),"Shipping Address Next Step Button Is not Present");
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		//check PC Perks Checkbox on Payment/order summary page
		s_assert.assertTrue(storeFrontHomePage.validatePCPerksCheckBoxIsDisplayed(),"PC Perks checkbox is not present");
		s_assert.assertFalse(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is  selected");
		storeFrontHomePage.checkPCPerksCheckBox();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is not selected");
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		String currentURL=driver.getCurrentUrl();
		logger.info("Current URL is "+currentURL);
		s_assert.assertTrue(currentURL.toLowerCase().contains(comPWSOfSponser.split(":")[1].toLowerCase()),"After pc Enrollment the site does not navigated to expected url");
		s_assert.assertAll();
	}

	//Hybris Project-2278:Add New Billing from accounts and chk on Checkout screen
	@Test
	public void testAddNewBillingProfileFromAccountsAndCheckOn_2278() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int i=0;
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontHomePage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		String defaultSelectedBillingProfileName = storeFrontBillingInfoPage.getDefaultBillingAddress();
		for(i=1;i<=2;i++){
			storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
			storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+i);
			storeFrontBillingInfoPage.selectNewBillingCardExpirationDate();
			storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontBillingInfoPage.selectNewBillingCardAddress();
			storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		}
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		//storeFrontUpdateCartPage.clickOnUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewAddressGetsAssociatedWithTheDefaultBillingProfile(defaultSelectedBillingProfileName), "Default selected billing address is not present on checkout page");
		s_assert.assertTrue(storeFrontUpdateCartPage.isTheBillingAddressPresentOnPage(newBillingProfileName, i),"Newly added Billing profile is NOT listed on the checkout page page");	
		i--;
		storeFrontUpdateCartPage.selectDifferentBillingProfile(newBillingProfileName+i);
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewAddressGetsAssociatedWithTheDefaultBillingProfile(newBillingProfileName), "Bill to this card is not selected");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		//storeFrontUpdateCartPage.clickBillingEditAfterSave();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedBillingProfileIsSelectedByDefault(newBillingProfileName+i),"New Billing Profile is not selected by default on CRP cart page");
		//storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(),"Order is not placed successfully");
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//------------------ Verify that adhoc orders template doesn't contains the newly created billing profile by verifying by name------------------------------------------------------------
		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName+i),"AdHoc Orders Template Payment Method contains new billing profile when future autoship checkbox not selected");
		//------------------Verify that billing info page contains the newly created billing profile
		s_assert.assertAll();
	}

	//Hybris Project-4274:UserName Field Validation
	@Test
	public void testVerifyUserNameFieldValidation_4274() throws InterruptedException {		
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 100000);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage=new StoreFrontHomePage(driver);
		String specialSymbolUsername="@"+randomNum;
		//Get CA consultant from database .
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage= storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		//String existingUserName=storeFrontAccountInfoPage.getExistingUserName();
		storeFrontAccountInfoPage.enterUserName(specialSymbolUsername);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
		logout();
		// login with new username.
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(specialSymbolUsername, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"login is not successful");
		s_assert.assertAll();
	}

	// Hybris Project-4275:verify Uniqueness of Username
	@Test
	public void testVerifyUniqnessOfUsername_4275() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumbers = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>>randomPCUserList=null;
		List<Map<String, Object>>randomRCList=null;
		String consultantEmailID = null;
		String crossCountryConsultantEmailID = null;
		String pcUserEmailID = null;
		String crossCountryPCUserEmailID = null;
		String rcUserEmailID = null;
		String crossCountryRCUserEmailID = null;
		String twoWords="West coast"+randomNum;
		String userName="WestCoast"+randomNum;
		String cid=null;
		String crossCountry=null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			cid="236";
			crossCountry="us";
		}else if(driver.getCountry().equalsIgnoreCase("us")){
			cid="40";
			crossCountry="ca";
		}
		storeFrontHomePage=new StoreFrontHomePage(driver);
		//Get Cross country Consultant from database
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,cid),RFO_DB);
			crossCountryConsultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(crossCountryConsultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+crossCountryConsultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logout();
		//Get consultant from database .
		driver.get(driver.getURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage= storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.enterUserName(consultantEmailID);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
		storeFrontAccountInfoPage.enterUserName(crossCountryConsultantEmailID);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getWrongUsernameErrorMessage().contains("Your Username already exist,Please Enter the Different Username"),"Invalid username is accepted");
		storeFrontAccountInfoPage.enterUserName(twoWords);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Invalid username is accepted");
		storeFrontAccountInfoPage.enterUserName(userName);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
		logout();
		//again login with new username.
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(userName, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"login is not successful");
		logout();
		twoWords="West coast"+randomNumber;
		userName="WestCoast"+randomNumber;
		//For PC User
		//Get Cross Country PC User from database
		driver.get(driver.getURL()+"/"+crossCountry);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,cid),RFO_DB);
			crossCountryPCUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(crossCountryPCUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("Login Error for the user "+crossCountryPCUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}	
		logout();
		//Get User from database .
		driver.get(driver.getURL()+"/"+driver.getCountry());
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("Login Error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}	
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage= storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.enterUserName(pcUserEmailID);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
		storeFrontAccountInfoPage.enterUserName(crossCountryPCUserEmailID);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getWrongUsernameErrorMessage().contains("Your Username already exist,Please Enter the Different Username"),"Invalid username is accepted");
		storeFrontAccountInfoPage.enterUserName(twoWords);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Invalid username is accepted");
		storeFrontAccountInfoPage.enterUserName(userName);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
		logout();
		//again login with new username.
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(userName, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"login is not successful");
		logout();
		twoWords="West coast"+randomNumbers;
		userName="WestCoast"+randomNumbers;
		//For RC User
		//Get Cross country User from database
		driver.get(driver.getURL()+"/"+crossCountry);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,cid),RFO_DB);
			crossCountryRCUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(crossCountryRCUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+crossCountryRCUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		logout();
		//Get User from database .
		driver.get(driver.getURL()+"/"+driver.getCountry());
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage= storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.enterUserName(rcUserEmailID);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
		storeFrontAccountInfoPage.enterUserName(crossCountryRCUserEmailID);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getWrongUsernameErrorMessage().contains("Your Username already exist,Please Enter the Different Username"),"Invalid username is accepted");
		storeFrontAccountInfoPage.enterUserName(twoWords);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.getErrorMessage().contains("Please enter valid username"),"Invalid username is accepted");
		storeFrontAccountInfoPage.enterUserName(userName);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
		logout();
		//again login with new username.
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(userName, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"login is not successful");
		s_assert.assertAll();
	}

	// Hybris Project-4276:VErify 2 fields for emails address and USerName is present
	@Test
	public void testVerifyUserNameAndEmailAddressFieldsValidation_4276() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 100000);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage=new StoreFrontHomePage(driver);
		String userName="WestCoast"+randomNum;
		String EmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.NEW_EMAIL_ADDRESS_SUFFIX;
		//Get consultant from database .		
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage= storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		//String existingUserName=storeFrontAccountInfoPage.getExistingUserName();
		storeFrontAccountInfoPage.enterUserName(userName);
		storeFrontAccountInfoPage.enterEmailAddress(EmailAddress);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
		logout();
		// login with new username.
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(EmailAddress, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"login is not successful");
		logout();
		// login with new username.
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(userName, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"login is not successful");
		s_assert.assertAll();
	}

	// Hybris Project-4277:Email and USer NAme has different values
	@Test
	public void testVerifyUserNameAndEmailAddressFieldsValidationWithDifferentValues_4277() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 100000);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage=new StoreFrontHomePage(driver);
		String userName="WestCoast"+randomNum;
		String EmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.NEW_EMAIL_ADDRESS_SUFFIX;
		//Get consultant from database .		
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage= storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		//String existingUserName=storeFrontAccountInfoPage.getExistingUserName();
		storeFrontAccountInfoPage.enterUserName(userName);
		storeFrontAccountInfoPage.enterEmailAddress(EmailAddress);
		storeFrontAccountInfoPage.clickSaveAccountPageInfo();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
		logout();
		// login with new username.
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(EmailAddress, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"login is not successful");
		logout();
		// login with new username.
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(userName, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"login is not successful");
		s_assert.assertAll();
	}

	// Hybris Project-4279:Change User Name Multiple Times and SAve
	@Test
	public void testVerifyChangeUserNameFieldsMultipleTimeAndValidateLoginWithLastChange_4279() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 100000);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage=new StoreFrontHomePage(driver);
		String userName="WestCoast"+randomNum;
		int i=0;
		//Get consultant from database .
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage= storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		//String existingUserName=storeFrontAccountInfoPage.getExistingUserName();
		for(i=1;i<=3;i++){
			storeFrontAccountInfoPage.enterUserName(userName+i);
			storeFrontAccountInfoPage.clickSaveAccountPageInfo();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
		}
		logout();
		// login with new username.
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(userName+(i-1), password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"login is not successful");
		s_assert.assertAll();
	}

	//Hybris Project-4278:Update USer name and Upgrade USer
	@Test
	public void testUpdateUserNameAndUpgradeUser_4278() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			int randomNumForRCToConsultant = CommonUtils.getRandomNum(10000, 1000000);
			int randomNumForPCToConsultant = CommonUtils.getRandomNum(10000, 1000000);
			String userName = TestConstants.FIRST_NAME+randomNum;
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomRCList =  null;
			String rcUserEmailID =null;
			String accountId = null;
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String firstName=TestConstants.FIRST_NAME+randomNum;
			String lastName = "lN";
			String state = null;
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName =  TestConstants.REGIMEN_NAME_REVERSE;
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			while(true){
				randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
				rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
				accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
				randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountId),RFO_DB);
				rcUserEmailID = String.valueOf(getValueFromQueryResult(randomRCList, "EmailAddress"));
				logger.info("Account Id of the user is "+accountId);
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
				boolean isError = driver.getCurrentUrl().contains("error");
				if(isError){
					logger.info("login error for the user "+rcUserEmailID);
					driver.get(driver.getURL()+"/"+driver.getCountry());
				}
				else
					break;
			} 
			storeFrontRCUserPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.enterUserName(userName);
			storeFrontAccountInfoPage.clickSaveAccountPageInfo();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
			logout();
			storeFrontHomePage.loginAsRCUser(userName, password);
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			//Upgrade RC To PC
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
			storeFrontHomePage.clickAddToBagButton(driver.getCountry());
			storeFrontHomePage.clickOnCheckoutButton();
			storeFrontHomePage.clickOnContinueWithoutSponsorLink();
			storeFrontHomePage.checkPCPerksCheckBox();
			storeFrontHomePage.clickOnShippingAddressNextStepBtn();
			//Enter Billing Profile
			storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
			storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
			storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
			storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
			storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.selectNewBillingCardAddress();
			storeFrontHomePage.clickOnSaveBillingProfile();
			storeFrontHomePage.clickOnBillingNextStepBtn();
			storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
			storeFrontHomePage.clickPlaceOrderBtn();
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			logout();
			// RC TO Consultant
			driver.get(driver.getURL()+"/"+driver.getCountry());
			while(true){
				randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
				rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
				accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
				randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountId),RFO_DB);
				rcUserEmailID = String.valueOf(getValueFromQueryResult(randomRCList, "EmailAddress"));
				logger.info("Account Id of the user is "+accountId);
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
				boolean isError = driver.getCurrentUrl().contains("error");
				if(isError){
					logger.info("login error for the user "+rcUserEmailID);
					driver.get(driver.getURL()+"/"+driver.getCountry());
				}
				else
					break;
			} 
			String userNameForConsultant = TestConstants.FIRST_NAME+randomNumForRCToConsultant;
			storeFrontRCUserPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.enterUserName(userNameForConsultant);
			storeFrontAccountInfoPage.clickSaveAccountPageInfo();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
			logout();
			storeFrontHomePage.loginAsRCUser(userNameForConsultant, password);
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			logout();
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			String socialInsuranceNumber2 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
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
			storeFrontHomePage.enterFirstName(firstName);
			storeFrontHomePage.enterLastName(lastName);
			storeFrontHomePage.enterEmailAddress(rcUserEmailID);
			storeFrontHomePage.enterPassword(password);
			storeFrontHomePage.enterConfirmPassword(password);
			storeFrontHomePage.enterAddressLine1(addressLine1);
			storeFrontHomePage.enterCity(city);
			storeFrontHomePage.selectProvince(state);
			storeFrontHomePage.enterPostalCode(postalCode);
			storeFrontHomePage.enterPhoneNumber(phoneNumber);
			storeFrontHomePage.clickNextButton();
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
			storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
			storeFrontHomePage.clickOnEnrollMeBtn();
			storeFrontHomePage.clickOnConfirmAutomaticPayment();
			s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
			driver.get(driver.getURL()+"/"+driver.getCountry());
			//PC to consultant
			List<Map<String, Object>> randomPCUserList;
			String pcUserEmailID = null;
			while(true){
				randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
				pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
				accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
				List<Map<String, Object>> randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountId),RFO_DB);
				pcUserEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));
				logger.info("Account Id of the user is "+accountId);
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
				boolean isLoginError = driver.getCurrentUrl().contains("error");
				if(isLoginError){
					logger.info("Login error for the user "+pcUserEmailID);
					driver.get(driver.getURL()+"/"+driver.getCountry());
				}
				else
					break;
			}	
			String userNameForPCToConsultant = TestConstants.FIRST_NAME+randomNumForPCToConsultant;
			logger.info("login is successful");
			storeFrontPCUserPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.enterUserName(userNameForPCToConsultant);
			storeFrontAccountInfoPage.clickSaveAccountPageInfo();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"Profile updation message not appear for correct username");
			logout();
			storeFrontHomePage.loginAsRCUser(userNameForPCToConsultant, password);
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			logout();
			//get sponsor ID
			List<Map<String, Object>> sponserList  = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			String sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));
			// sponser search by Account Number
			List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
			String sponsorId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
			storeFrontHomePage.searchCID(sponsorId);
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);  
			storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
			storeFrontHomePage.enterEmailAddress(pcUserEmailID);
			s_assert.assertTrue(storeFrontHomePage.verifyUpradingToConsulTantPopup(), "Upgrading to a consultant popup is not present");
			storeFrontHomePage.enterPasswordForUpgradePcToConsultant();
			storeFrontHomePage.clickOnLoginToTerminateToMyPCAccount();
			s_assert.assertTrue(storeFrontHomePage.verifyAccountTerminationMessage(), "Pc user is not terminated successfully");
			storeFrontHomePage.enterEmailAddress(pcUserEmailID);
			storeFrontHomePage.clickOnEnrollUnderLastUpline();
			logger.info("After click enroll under last upline we are on "+driver.getCurrentUrl());
			storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_NAME_BIG_BUSINESS, TestConstants.REGIMEN_NAME_REVERSE);  
			storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
			storeFrontHomePage.enterFirstName(firstName);
			storeFrontHomePage.enterLastName(lastName);
			storeFrontHomePage.enterEmailAddress(pcUserEmailID);
			storeFrontHomePage.enterPassword(password);
			storeFrontHomePage.enterConfirmPassword(password);
			storeFrontHomePage.enterAddressLine1(addressLine1);
			storeFrontHomePage.enterCity(city);
			storeFrontHomePage.selectProvince(state);
			storeFrontHomePage.enterPostalCode(postalCode);
			storeFrontHomePage.enterPhoneNumber(phoneNumber);
			storeFrontHomePage.clickNextButton();
			storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
			storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber2);
			storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
			storeFrontHomePage.clickNextButton();
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

	//Hybris Project-4008:Look up with Terminated US consultant
	@Test
	public void testLookUPWithTerminatedUSConsultant_4008() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		String country= driver.getCountry();
		if(country.equalsIgnoreCase("us")){
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
					driver.get(driver.getURL()+"/"+country);
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
			s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
			storeFrontAccountTerminationPage.clickConfirmTerminationBtn();  
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();
			//connect with a consultant
			storeFrontHomePage.clickConnectUnderConnectWithAConsultantSection();
			//search with terminated consultant
			storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(consultantEmailID);
			//verify 'No Result found' is displayed
			s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"Terminated US Consultant is Present!!!");
			s_assert.assertAll(); 
		}else{
			logger.info("NOT EXECUTED..US Specific test");
		}
	}

	//Hybris Project-4010:Look up with Terminated Preffered consultant's Full name/ Account ID
	@Test
	public void testLookUpWithTerminatedPCFullNameOrAccountID_4010() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		storeFrontPCUserPage.cancelMyPCPerksAct();
		//Navigate to the base url
		driver.get(driver.getURL()+"/"+driver.getCountry());
		//connect with a consultant
		storeFrontHomePage.clickConnectUnderConnectWithAConsultantSection();
		//search with terminated consultant
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountId);
		//verify 'No Result found' is displayed
		s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"Terminated PC User is Present!!!");
		s_assert.assertAll(); 
	}

	//Hybris Project-4012:Look up with Terminated Retail consultant's Full name/ Account ID
	@Test
	public void testLookUpWithTerminatedRCFullNameOrAccountID_4012() throws InterruptedException{
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is not Present");
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup(); 
		//Navigate to the base url
		driver.get(driver.getURL()+"/"+driver.getCountry());
		//connect with a consultant
		storeFrontHomePage.clickConnectUnderConnectWithAConsultantSection();
		//search with terminated consultant
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountId);
		//verify 'No Result found' is displayed
		s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"Terminated RC User is Present!!!");
		s_assert.assertAll(); 
	}

	//Hybris Project-3920 :: Version : 1 :: Verify creating crp autoship from My Account under .biz site 
	@Test 
	public void testCreatingCRPAutoshipUnderBizSite_3920() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		env = driver.getEnvironment();	
		String state = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, env);
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
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state,postalCode, phoneNumber);
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
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnEnrollInCRP();;
		storeFrontHomePage.clickOnAddToCRPButtonCreatingCRPUnderBizSite();
		storeFrontHomePage.clickOnCRPCheckout();
		storeFrontHomePage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnSetupCRPAccountBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyOrderConfirmation(), "Order Confirmation Message has not been displayed");
		storeFrontHomePage.clickOnGoToMyAccountToCheckStatusOfCRP();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCurrentCRPStatus(), "Current CRP Status has not been Enrolled");
		s_assert.assertAll();
	}

	//Hybris Project-3921 :: Version : 1 :: Verify subscribing to Pulse from My Account under .biz site 
	@Test
	public void testSubscribingPulseMyAccount_3921() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, env);
		String state = null;
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

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();		
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, state,postalCode, phoneNumber);
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
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnSubscribeToPulseBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickOnSubscribePulseTermsAndConditionsChkbox();
		storeFrontUpdateCartPage.clickOnSubscribeBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyPulseOrderCreatedMsg(), "Pulse order created msg is NOT present,Pulse might NOT be subscribed successfully");
		s_assert.assertAll();
	}

	// Hybris Project-4064:Access US Con's Canadian PWS as a Canadian Consultant W/O Pulse
	@Test
	public void testAccessUSConsCanadianPWSAsACanadianConsultantWithoutPulse_4064() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantList2 =  null;
		String usConsultantPWS = null;
		String consultantEmailID = null;
		String countryID ="236";
		String country = "us";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITHOUT_PULSE_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress"); 
			randomConsultantList2 =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",country,countryID), RFO_DB);
			usConsultantPWS = (String) getValueFromQueryResult(randomConsultantList2, "URL"); 
			String caConsultantPWS = usConsultantPWS.replaceAll("us", "ca");
			driver.get(caConsultantPWS);
			boolean isPWSError = driver.getCurrentUrl().contains("sitenotfound");
			if(isPWSError){
				logger.info("PWS error for the user "+consultantEmailID);
				driver.get(caConsultantPWS);
			}
			else
				break;
		}
		while(true){
			storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		s_assert.assertTrue(driver.getCurrentUrl().contains("corprfo"),"current url is not a corp url");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(),"welcome dropDown is not present after login");
		s_assert.assertAll();
	}

	// Hybris Project-4065:Login on US Con's Canadian PWS as a Canadian Consultant W/O Pulse
	@Test
	public void testLoginOnUsConsCanadianPWSAsACanadianConsultantWithoutPulse_4065() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantList2 =  null;
		String usConsultantPWS = null;
		String consultantEmailID = null;
		String countryID ="236";
		String country = "us";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITHOUT_PULSE_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress"); 
			randomConsultantList2 =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",country,countryID), RFO_DB);
			usConsultantPWS = (String) getValueFromQueryResult(randomConsultantList2, "URL"); 
			String caConsultantPWS = usConsultantPWS.replaceAll("us", "ca");
			driver.get(caConsultantPWS);
			boolean isPWSError = driver.getCurrentUrl().contains("sitenotfound");
			if(isPWSError){
				logger.info("PWS error for the user "+consultantEmailID);
				driver.get(caConsultantPWS);
			}
			else
				break;
		}
		while(true){
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		s_assert.assertTrue(driver.getCurrentUrl().contains("corprfo"),"current url is not a corp url");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(),"welcome dropDown is not present after login");
		s_assert.assertTrue(storeFrontConsultantPage.isAutoshipLinkPresentOnThePage(),"Autoship link is not present");
		s_assert.assertAll();
	}
	
	//Hybris Project-4814:Edit Existing Shipping Billing Address on Edit CRP Template
	@Test(enabled=false)//Already covered in shipping billing tests
	public void testEditExistingShippingAndBillingAddressOnEditCRPTemplate_4814() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		String profileName=null;
		String lastName = "lN";
		String state = null;
		String accountID = null;
		String billingProfileName = TestConstants.BILLING_ADDRESS_NAME+randomNumber;
		String newBillingProfileName = TestConstants.BILLING_ADDRESS_NAME+randomNum2;
		String newProfileName=TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME+randomNum1;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		if(driver.getCountry().equalsIgnoreCase("CA")){   
			profileName=TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_CA+randomNum;
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			profileName=TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_US+randomNum;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyCRP();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");
		//Enroll new CRP
		storeFrontAccountInfoPage.clickOnEnrollInCRP();
		storeFrontHomePage.clickOnAddToCRPButtonCreatingCRPUnderBizSite();
		storeFrontHomePage.clickOnCRPCheckout();
		storeFrontHomePage.clickOnEditForDefaultShippingAddress();
		storeFrontHomePage.enterNewShippingAddressName(profileName+" "+lastName);
		storeFrontHomePage.enterNewShippingAddressLine1(addressLine1);
		storeFrontHomePage.enterNewShippingAddressCity(city);
		storeFrontHomePage.selectNewShippingAddressState(state);
		storeFrontHomePage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontHomePage.enterNewShippingAddressPhoneNumber(phoneNumber);
		storeFrontHomePage.clickOnSaveShippingProfile();
		s_assert.assertTrue(storeFrontHomePage.isNewlyCreatedShippingProfileIsSelectedByDefault(profileName),"New Shipping Profile is not selected by default on CRP cart page");
		storeFrontHomePage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
		storeFrontHomePage.clickOnDefaultBillingProfileEdit();
		storeFrontHomePage.enterEditedCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(billingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontHomePage.getDefaultSelectedBillingAddressName().contains(billingProfileName), "Expected shipping profile name is: "+billingProfileName+"Actual on UI is: "+storeFrontHomePage.getDefaultSelectedBillingAddressName());
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnSetupCRPAccountBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyOrderConfirmation(), "Order Confirmation Message has not been displayed");
		// assert shipping and billing profile
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontHomePage.clickOnAutoshipCart();
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.validateNewlySelectedDefaultShippingProfileIsUpdatedInAutoshipShippingSection(profileName), "Autoship template does not contain shipping profile name");
		s_assert.assertTrue(storeFrontUpdateCartPage.validateNewlySelectedDefaultBillingProfileIsNotUpdatedInAutoshipBillingProfileSection(billingProfileName), "Autoship template does not contain billing profile name");
		// Edit shipping and billing
		storeFrontUpdateCartPage.clickOnEditShipping();
		storeFrontUpdateCartPage.clickOnEditForDefaultShippingAddress();
		storeFrontUpdateCartPage.enterNewShippingAddressName(newProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.selectNewShippingAddressState(state);
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(phoneNumber);
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedShippingProfileIsSelectedByDefault(newProfileName),"New Shipping Profile is not selected by default on CRP cart page");
		s_assert.assertTrue(storeFrontUpdateCartPage.getDefaultSelectedShippingAddress().contains(newProfileName), "Expected shipping profile name is: "+newProfileName+"Actual on UI is: "+storeFrontUpdateCartPage.getDefaultSelectedShippingAddress());
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.getDefaultSelectedBillingAddressName().contains(newBillingProfileName), "Expected shipping profile name is: "+newBillingProfileName+"Actual on UI is: "+storeFrontUpdateCartPage.getDefaultSelectedBillingAddressName());
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(),"Your Next cart has been updated message not present on UI");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyUpdatedAddressPresentUpdateCartPg(newProfileName), "Autoship template does not contain shipping profile name in Edit section");
		s_assert.assertTrue(storeFrontUpdateCartPage.validateNewlySelectedDefaultBillingProfileIsNotUpdatedInAutoshipBillingProfileSection(newBillingProfileName), "Autoship template does not contain billing profile name in Edit section");
		s_assert.assertAll();
	}

	//Hybris Project-4813:Add New Shipping Billing Address on Edit CRP Template
	@Test(enabled=false)//ALready covered in billing shipping tests
	public void testAddNewShippingAndBillingAddressOnEditCRPTemplate_4813() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		int i=0;
		int countOfBilling = 0;
		String profileName=null;
		String lastName = "lN";
		String accountID = null;
		String state = null;
		String billingProfileName = TestConstants.BILLING_ADDRESS_NAME+randomNumber;
		String newBillingProfileName = TestConstants.BILLING_ADDRESS_NAME+randomNum2;
		String newProfileName=TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME+randomNum1;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		if(driver.getCountry().equalsIgnoreCase("CA")){   
			profileName=TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_CA+randomNum;
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			state = TestConstants.PROVINCE_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			profileName=TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_US+randomNum;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
			state = TestConstants.PROVINCE_US;
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyCRP();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");
		//Enroll new CRP
		storeFrontAccountInfoPage.clickOnEnrollInCRP();
		storeFrontHomePage.clickOnAddToCRPButtonCreatingCRPUnderBizSite();
		storeFrontHomePage.clickOnCRPCheckout();
		//Add multiple shipping address
		for(i=0;i<2;i++){
			storeFrontHomePage.clickAddNewShippingProfileLink();
			storeFrontHomePage.enterNewShippingAddressName(profileName+i+" "+lastName);
			storeFrontHomePage.enterNewShippingAddressLine1(addressLine1);
			storeFrontHomePage.enterNewShippingAddressCity(city);
			storeFrontHomePage.selectNewShippingAddressState(state);
			storeFrontHomePage.enterNewShippingAddressPostalCode(postalCode);
			storeFrontHomePage.enterNewShippingAddressPhoneNumber(phoneNumber);
			storeFrontHomePage.clickOnSaveShippingProfile();
			s_assert.assertTrue(storeFrontHomePage.isNewlyCreatedShippingProfileIsSelectedByDefault(profileName+i),"New Shipping Profile is not selected by default on CRP cart page");
		}
		i=1;
		storeFrontHomePage.selectShippingAddress(profileName+i);
		s_assert.assertTrue(storeFrontHomePage.getDefaultSelectedShippingAddress().contains(profileName+i), "Expected shipping profile name is: "+profileName+i+"Actual on UI is: "+storeFrontHomePage.getDefaultSelectedShippingAddress());
		storeFrontHomePage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
		for(countOfBilling=0;countOfBilling<2;countOfBilling++){
			storeFrontHomePage.clickAddNewBillingProfileLink();
			storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNewBillingNameOnCard(billingProfileName+countOfBilling+" "+lastName);
			storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
			storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.selectNewBillingCardAddress();
			storeFrontHomePage.clickOnSaveBillingProfile();
		}
		countOfBilling = 1;
		storeFrontHomePage.selectBillingAddress(billingProfileName+countOfBilling);
		s_assert.assertTrue(storeFrontHomePage.getDefaultSelectedBillingAddressName().contains(billingProfileName+countOfBilling), "Expected shipping profile name is: "+billingProfileName+countOfBilling+"Actual on UI is: "+storeFrontHomePage.getDefaultSelectedBillingAddressName());
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnSetupCRPAccountBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyOrderConfirmation(), "Order Confirmation Message has not been displayed");
		// assert shipping and billing profile
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontHomePage.clickOnAutoshipCart();
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.validateNewlySelectedDefaultShippingProfileIsUpdatedInAutoshipShippingSection(profileName+i), "Autoship template does not contain shipping profile name");
		s_assert.assertTrue(storeFrontUpdateCartPage.validateNewlySelectedDefaultBillingProfileIsNotUpdatedInAutoshipBillingProfileSection(billingProfileName+countOfBilling), "Autoship template does not contain billing profile name");
		// add shipping and billing
		storeFrontUpdateCartPage.clickOnEditShipping();
		for(i=1;i<=2;i++){
			storeFrontUpdateCartPage.clickAddNewShippingProfileLink();
			storeFrontUpdateCartPage.enterNewShippingAddressName(newProfileName+i+" "+lastName);
			storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
			storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
			storeFrontUpdateCartPage.selectNewShippingAddressState(state);
			storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
			storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(phoneNumber);
			storeFrontUpdateCartPage.clickOnSaveShippingProfile();
			s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedShippingProfileIsSelectedByDefault(newProfileName+i),"New Shipping Profile is not selected by default on CRP cart page");
		}
		i=1;
		storeFrontUpdateCartPage.selectShippingAddress(newProfileName+i);
		s_assert.assertTrue(storeFrontUpdateCartPage.getDefaultSelectedShippingAddress().contains(newProfileName+i), "Expected shipping profile name is: "+newProfileName+i+"Actual on UI is: "+storeFrontUpdateCartPage.getDefaultSelectedShippingAddress());
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		for(countOfBilling=1;countOfBilling<=2;countOfBilling++){
			storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
			storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+countOfBilling+" "+lastName);
			storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
			storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontUpdateCartPage.selectNewBillingCardAddress();
			storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		}
		countOfBilling = 1;
		storeFrontUpdateCartPage.selectBillingAddress(newBillingProfileName+countOfBilling);
		s_assert.assertTrue(storeFrontUpdateCartPage.getDefaultSelectedBillingAddressName().contains(newBillingProfileName+countOfBilling), "Expected shipping profile name is: "+newBillingProfileName+countOfBilling+"Actual on UI is: "+storeFrontUpdateCartPage.getDefaultSelectedBillingAddressName());
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(),"Your Next cart has been updated message not present on UI");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyUpdatedAddressPresentUpdateCartPg(newProfileName+i), "Autoship template does not contain shipping profile name in Edit section");
		s_assert.assertTrue(storeFrontUpdateCartPage.validateNewlySelectedDefaultBillingProfileIsNotUpdatedInAutoshipBillingProfileSection(newBillingProfileName+countOfBilling), "Autoship template does not contain billing profile name in Edit section");
		s_assert.assertAll();
	}

	// Hybris Project-2283:Add Multiple shippiing address from myaccounts
	@Test
	public void testAddMultipeShippingAddressFromMyAccounts_2283() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
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
			state = TestConstants.PROVINCE_CA;
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
			storeFrontShippingInfoPage.selectNewShippingAddressState(state);
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
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//------------------ Verify that adhoc orders template doesn't contains the newly Edited Shipping profile by verifying by name------------------------------------------------------------
		s_assert.assertTrue(storeFrontOrdersPage.isShippingAddressContainsName(profileName+"0"),"AdHoc Orders Page do not contain the newly edited shipping address");
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
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

	//Hybris Project-135:Enroll in pulse from my account - enrolling from 1st till 17th
	@Test
	public void testEnrollInPulseFromMyAccountEnrolligFrom1stTill17th_135() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String accountId = null;
		List<Map<String, Object>> randomConsultantList =  null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String billingProfileName = TestConstants.BILLING_ADDRESS_NAME+randomNum;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String userName = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITHOUT_PULSE_RFO,countryId),RFO_DB);
			userName = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress"); 
			//accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
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
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontConsultantPage.clickOnYourAccountDropdown();
		storeFrontConsultantPage.clickOnAutoshipStatusLink();
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
		storeFrontAccountInfoPage.clickOnCancelMyPulseSubscription();
		storeFrontAccountInfoPage.clickOnOnlySubscribeToPulseBtn();
		storeFrontAccountInfoPage.clickOnNextDuringPulseSubscribtion();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		//Enter Billing Profile
		storeFrontAccountInfoPage.clickAddNewBillingProfileLink();
		storeFrontAccountInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontAccountInfoPage.enterNewBillingNameOnCard(billingProfileName);
		storeFrontAccountInfoPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontAccountInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontAccountInfoPage.selectNewBillingCardAddress();
		storeFrontAccountInfoPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnAccountInfoNextButton();
		storeFrontUpdateCartPage.clickOnSubscribePulseTermsAndConditionsChkbox();
		storeFrontUpdateCartPage.clickOnSubscribeBtn();
		s_assert.assertTrue(storeFrontAccountInfoPage.isOrderPlacedSuccessfully(), "Order is not placed successfully");
		storeFrontAccountInfoPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontAccountInfoPage.clickOrdersLinkPresentOnWelcomeDropDown();
		//Verify Status of CRP autoship template
		s_assert.assertTrue(storeFrontOrdersPage.getStatusOfSecondAutoshipTemplateID().toLowerCase().contains("pending"), "Expected status of second pulse autoship id is: pending and actual on UI is: "+storeFrontOrdersPage.getStatusOfSecondAutoshipTemplateID().toLowerCase());
		String pulseAutoshipID = storeFrontOrdersPage.getPulseAutoshipOrderNumber();
		String autoshipDate = storeFrontOrdersPage.getPulseAutoshipOrderDate();
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
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(userName);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		s_assert.assertTrue(cscockpitCustomerTabPage.getStatusOfAutoshipID(pulseAutoshipID).toLowerCase().contains("pending"), "Expected status of autoship id in CSCockpit is: pending and actual on UI is: "+cscockpitCustomerTabPage.getStatusOfAutoshipID(pulseAutoshipID).toLowerCase());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
			//randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_CONSULTANT_NO_PWS_WITH_COUNTRY_RFO,countryId),RFO_DB);
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyCRP();
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

	//Hybris Project-4769:customer cannot use existing active prefix while enlrolling for pulse.
	@Test 
	public void testCheckAnotherConsultantSitePrefixWhileEnrollingToPulse_4769() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String sitePrefix=null;
		List<Map<String, Object>> randomConsultantList=null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String consultantEmailID = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			List<Map<String, Object>> sitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_ALREADY_EXISTING_SITE_PREFIX_RFO,countryId,accountID),RFO_DB);
			sitePrefix=String.valueOf(getValueFromQueryResult(sitePrefixList, "SitePrefix"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontHomePage.clickOnYourAccountDropdown();
		storeFrontHomePage.clickOnAutoshipStatusLink();
		s_assert.assertTrue(storeFrontAccountInfoPage.validateSubscribeToPulse(),"This user does not have pulse subscribed");
		if(storeFrontAccountInfoPage.validateSubscribeToPulse()){
			storeFrontHomePage.cancelPulseSubscription();
			s_assert.assertTrue(storeFrontAccountInfoPage.validatePulseCancelled(),"pulse subscription is not cancelled for the user");
		}
		storeFrontAccountInfoPage.clickOnOnlySubscribeToPulseBtn();
		s_assert.assertTrue(storeFrontAccountInfoPage.getWebsitePrefixName().contains(sitePrefix),"While subscribing to pulse the same website prefix is not suggested for user");
		s_assert.assertFalse(storeFrontHomePage.verifyAnotherConsultantPrefixIsNotAllowed(sitePrefix),"Same Consultant site prefix is not present");
		//Get Another consultant site prefix.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		String accountIdUsedToGetAnotherConsultantSitePrefix = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		List<Map<String, Object>> AnotherConsultantSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_ALREADY_EXISTING_SITE_PREFIX_RFO,countryId,accountIdUsedToGetAnotherConsultantSitePrefix),RFO_DB);
		String sitePrefixOfAnotherConsultant=String.valueOf(getValueFromQueryResult(AnotherConsultantSitePrefixList, "SitePrefix"));
		logger.info("Another Consultant website prefix is "+sitePrefixOfAnotherConsultant);
		//Validate already existing site prefix allows consultant enrollment.
		storeFrontHomePage.enterWebsitePrefixName(sitePrefixOfAnotherConsultant);
		s_assert.assertTrue(storeFrontHomePage.verifyAnotherConsultantPrefixIsNotAllowed(sitePrefixOfAnotherConsultant),"Another consultant site prefix is accepted");
		s_assert.assertAll();
	}

}