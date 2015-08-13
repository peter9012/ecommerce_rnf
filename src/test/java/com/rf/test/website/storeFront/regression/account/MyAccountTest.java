package com.rf.test.website.storeFront.regression.account;


import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.*;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontAccountTerminationPage;
import com.rf.pages.website.StoreFrontBillingInfoPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontRCUserPage;
import com.rf.pages.website.StoreFrontReportOrderComplaintPage;
import com.rf.pages.website.StoreFrontReportProblemConfirmationPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class MyAccountTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(MyAccountTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontAccountTerminationPage storeFrontAccountTerminationPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontReportOrderComplaintPage storeFrontReportOrderComplaintPage;
	private StoreFrontReportProblemConfirmationPage storeFrontReportProblemConfirmationPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;

	private String RFL_DB = null;
	private String RFO_DB = null;

	// Test Case Hybris Phase 2-3720 :: Version : 1 :: Perform Consultant Account termination through my account
	@Test
	public void testAccountTerminationPageForConsultant_3720() throws InterruptedException {
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		//randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		//consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_STAG2; // A Hard Coded User
		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_ACC_TERMINATION_PASSWORD_TST4);			
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
	
		//Test Case Hybris Phase 2-3719 :: Version : 1 :: Perform PC Account termination through my account
		@Test
		public void testAccountTerminationPageForPCUser_3719() throws InterruptedException{
			RFL_DB = driver.getDBNameRFL();
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomPCUserList =  null;
			String pcUserEmailID = null;
			//randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_PC_USER_EMAIL_ID_RFL,RFL_DB);
			//pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "EmailAddress");
			pcUserEmailID=TestConstants.PC_EMAIL_ID_STAG2; // A Hard Coded User;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, TestConstants.CONSULTANT_ACC_TERMINATION_PASSWORD_TST4);
			logger.info("login is successful");
			storeFrontPCUserPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
			s_assert.assertFalse(storeFrontAccountInfoPage.verifyAccountTerminationLink(),"Account Termination Link Is Present");
			logout();
			s_assert.assertAll();
		}
		
		// Hybris Phase 2-1980 :: Version : 1 :: Order >>Actions >>Report problems
		@Test
		public void testOrdersReportProblems_1980() throws SQLException, InterruptedException{
			RFL_DB = driver.getDBNameRFL();
			RFO_DB = driver.getDBNameRFO();

			List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;
			//		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_CONSULTANT_HAVING_SUBMITTED_ORDERS_RFL,RFL_DB);
			//		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
			consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_STAG2; // A Hard Coded User
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_ACC_TERMINATION_PASSWORD_TST4);			
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

		

		// Hybris Phase 2-2228 :: Version : 1 :: Perform RC Account termination through my account
		@Test(enabled=false) // Enable false because of Termination account of RC user
		public void testAccountTerminationPageForRCUser_2228() throws InterruptedException{
			RFL_DB = driver.getDBNameRFL();
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomRCUserList =  null;
			String rcUserEmailID = null;
			randomRCUserList = DBUtil.performDatabaseQuery(com.rf.core.website.constants.dbQueries.DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "EmailAddress");
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, TestConstants.RC_PASSWORD_TST4);
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
			storeFrontHomePage.loginAsRCUser(rcUserEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
			s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");		
			s_assert.assertAll();

		}

		//Hybris Phase 2-2235:Verify that user can change the information in 'my account info'.
		@Test(enabled=false)
		public void testAccountInformationForUpdate_2235() throws InterruptedException{
			RFO_DB = driver.getDBNameRFO(); 

			List<Map<String, Object>> accountNameDetailsList = null;
			List<Map<String, Object>> accountAddressDetailsList = null;
			List<Map<String, Object>> mainPhoneNumberList = null;
			List<Map<String, Object>> randomConsultantList =  null;

			String firstNameDB = null;
			String lastNameDB = null;
			String genderDB = null;
			String addressLine1DB= null;
			String cityDB = null;
			String provinceDB = null;
			String postalCodeDB = null;
			String mainPhoneNumberDB = null;
			String dobDB = null;
			//String stateDB = null;

			String consultantEmailID = null;
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_CONSULTANT_EMAIL_ID_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
			s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(), "Account Info page has not been displayed");
			storeFrontAccountInfoPage.updateAccountInformation(TestConstants.CONSULTANT_FIRST_NAME_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_LAST_NAME_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_ADDRESS_LINE_1_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_CITY_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_POSTAL_CODE_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_MAIN_PHONE_NUMBER_FOR_ACCOUNT_INFORMATION);

			//assert First Name with RFO
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, consultantEmailID), RFO_DB);
			firstNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "FirstName");
			assertTrue("First Name on UI is different from DB", storeFrontAccountInfoPage.verifyFirstNameFromUIForAccountInfo(firstNameDB));

			// assert Last Name with RFO
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, consultantEmailID), RFO_DB);
			lastNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "LastName");
			assertTrue("Last Name on UI is different from DB", storeFrontAccountInfoPage.verifyLasttNameFromUIForAccountInfo(lastNameDB) );

			// assert Address Line 1 with RFO
			accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, consultantEmailID), RFO_DB);
			addressLine1DB = (String) getValueFromQueryResult(accountAddressDetailsList, "AddressLine1");
			assertTrue("Address Line 1 on UI is different from DB", storeFrontAccountInfoPage.verifyAddressLine1FromUIForAccountInfo(addressLine1DB));


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
			mainPhoneNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_PHONE_NUMBER_QUERY_RFO, consultantEmailID), RFO_DB);
			mainPhoneNumberDB = (String) getValueFromQueryResult(mainPhoneNumberList, "PhoneNumberRaw");
			assertTrue("Main Phone Number on UI is different from DB", storeFrontAccountInfoPage.verifyMainPhoneNumberFromUIForAccountInfo(mainPhoneNumberDB));

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
			logout();
			
			s_assert.assertAll();
		}

		// Hybris Phase 2-2241 :: version 1 :: Verify the various field validations
		@Test
		public void testPhoneNumberFieldValidationForConsultant_2241() throws InterruptedException{
			RFL_DB = driver.getDBNameRFL();
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;

			//------------------------------- Random Users part is commented for now-----------------------------------------------	
			/*		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
					consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");*/
			//---------------------------------------------------------------------------------------------------------------------

			consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_STAG2; // A Hard Coded User
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
			RFL_DB = driver.getDBNameRFL();
			RFO_DB = driver.getDBNameRFO();
			String consultantEmailID = null;
			consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_STAG2; // A Hard Coded User
			//randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_PC_USER_EMAIL_ID_RFL,RFL_DB);
			//pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "EmailAddress");
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_ACC_TERMINATION_PASSWORD_TST4);			
			s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
			logger.info("login is successful");
			logout();
			s_assert.assertAll();
			}
		
		@Test
		 public void testUsernameValidations_2512() throws InterruptedException
		 {
		  RFL_DB = driver.getDBNameRFL();
		  RFO_DB = driver.getDBNameRFO();
		  List<Map<String, Object>> randomConsultantList =  null;
		  String consultantEmailID = null;

		  //------------------------------- Random Users part is commented for now----------------------------------------------- 
		  /*  randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		    consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");*/
		  //---------------------------------------------------------------------------------------------------------------------

		  consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A Hard Coded User

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
	}
