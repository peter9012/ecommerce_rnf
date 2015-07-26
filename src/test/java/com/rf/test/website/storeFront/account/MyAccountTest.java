package com.rf.test.website.storeFront.account;


import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
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
	private StoreFrontConsultantPage storeFrontConsulatantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontAccountTerminationPage storeFrontAccountTerminationPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontReportOrderComplaintPage storeFrontReportOrderComplaintPage;
	private StoreFrontReportProblemConfirmationPage storeFrontReportProblemConfirmationPage;

	private String RFL_DB = null;
	private String RFO_DB = null;

	// Test Case Hybris Phase 2-3720 :: Version : 1 :: Perform Consultant Account termination through my account
	@Test
	public void testAccountTerminationPageForConsultant_3720() throws InterruptedException {
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);			
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsulatantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
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
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_USER_EMAIL_ID_RFL,RFL_DB);
		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, TestConstants.PC_USER_PASSWORD_RFL);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		s_assert.assertFalse(storeFrontAccountInfoPage.verifyAccountTerminationLink(),"Account Termination Link Is Present");
		s_assert.assertAll();
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
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4,TestConstants.CONSULTANT_PASSWORD_RFL);			
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
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
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyEmailAddAtReportConfirmationPage(TestConstants.CONSULTANT_EMAIL_ID_TST4),"Email Address is not present as expected" );
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyOrderNumberAtReportConfirmationPage(),"Order number not present as expected");
		s_assert.assertTrue(storeFrontReportProblemConfirmationPage.verifyBackToOrderButtonAtReportConfirmationPage(),"Back To Order button is not present");
		logout();
		s_assert.assertAll();
	}

	//Hybris Phase 2-1981:Orders page UI for RC
	@Test
	public void testOrdersPageUIForRCUser_HP2_1981() throws SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> orderNumberList =  null;
		List<Map<String, Object>> orderStatusList =  null;
		List<Map<String, Object>> orderGrandTotalList =  null;
		List<Map<String, Object>> orderDateList =  null;

		String orderNumberDB = null;
		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;
		String rcUserUsername = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		rcUserUsername = TestConstants.RCUSER_USERNAME_TST4;
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(TestConstants.RCUSER_EMAIL_ID_TST4, TestConstants.RCUSER_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserUsername),"RC User Page doesn't contain Welcome User Message");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// assert for order number with RFL
		orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.RCUSER_EMAIL_ID_TST4),RFL_DB); 
		orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber"); 
		if(assertTrueDB("Order Number on UI is different from DB",storeFrontOrdersPage.verifyOrderNumber(orderNumberDB),RFL_DB)==false){
			//assert for order number with RFO
			orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO,  TestConstants.RCUSER_EMAIL_ID_TST4),RFO_DB);
			orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
			assertTrue("Order Number on UI is different from DB",storeFrontOrdersPage.verifyOrderNumber(orderNumberDB));
		}
		// assert for order status with RFL
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.RCUSER_EMAIL_ID_TST4),RFL_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		if(assertTrueDB("Order status is not as expected for this User",storeFrontOrdersPage.verifyOrderStatus(orderStatusDB),RFL_DB)==false){
			//assert for order status with RFO
			orderStatusList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFO, TestConstants.RCUSER_EMAIL_ID_TST4),RFO_DB);
			orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
			assertTrue("Order status is not as expected for this User", storeFrontOrdersPage.verifyOrderStatus(orderStatusDB));
		}
		// assert for grand total with RFL
		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.RCUSER_EMAIL_ID_TST4),RFL_DB);
		DecimalFormat df = new DecimalFormat("#.00");
		orderGrandTotalDB = df.format((Number) getValueFromQueryResult(orderGrandTotalList, "GrandTotal")); 
		if(assertTrueDB("Grand total is not as expected for this User",storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),RFL_DB)==false){
			//assert for grand total with RFO
			orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFO,  TestConstants.RCUSER_EMAIL_ID_TST4),RFO_DB);
			DecimalFormat dff = new DecimalFormat("#.00");
			orderGrandTotalDB = dff.format((Number) getValueFromQueryResult(orderGrandTotalList, "AmountToBeAuthorized")); 
			assertTrue("Grand total is not as expected for this User",storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB));
		}
		// assert for order date with RFL
		orderDateList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFL, TestConstants.RCUSER_EMAIL_ID_TST4),RFL_DB);
		orderDateDB = String.valueOf(getValueFromQueryResult(orderDateList, "StartDate"));
		if(assertTrueDB("Scheduled date is not as expected for this User",storeFrontOrdersPage.verifyScheduleDate(orderDateDB),RFL_DB)==false){
			//assert for order date with RFO
			orderDateList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFO,  TestConstants.RCUSER_EMAIL_ID_TST4),RFO_DB);
			orderDateDB = String.valueOf (getValueFromQueryResult(orderDateList, "CompletionDate"));
			assertTrue("Scheduled date is not as expected for this User",storeFrontOrdersPage.verifyScheduleDate(orderDateDB));
		}
		logout();
		s_assert.assertAll();		
	}

	// Hybris Phase 2-2235:Verify that user can change the information in 'my account info'
	@Test
	public void testAccountInformationForUpdate_2235() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();	

		List<Map<String, Object>> accountNameDetailsList = null;
		List<Map<String, Object>> accountAddressDetailsList = null;
		List<Map<String, Object>> mainPhoneNumberList = null;

		String firstNameDB = null;
		String lastNameDB = null;
		String genderDB = null;
		String addressLine1DB= null;
		String cityDB = null;
		String provinceDB = null;
		String postalCodeDB = null;
		String mainPhoneNumberDB = null;
		String dobDB = null;
		String stateDB = null;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsulatantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		//assertTrue("Account Info page has not been displayed", storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed());
		storeFrontAccountInfoPage.updateAccountInformation(TestConstants.CONSULTANT_FIRST_NAME_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_LAST_NAME_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_ADDRESS_LINE_1_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_CITY_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_POSTAL_CODE_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_MAIN_PHONE_NUMBER_FOR_ACCOUNT_INFORMATION);

		accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_NAME_DETAILS_FOR_ACCOUNT_INFO_QUERY_RFL, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFL_DB);
		firstNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "FirstName");
		lastNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "LastName");
		String genderId = String.valueOf(getValueFromQueryResult(accountNameDetailsList, "GenderID"));
		if(genderId.equals("2")){
			genderDB = "male";
		}
		else{
			genderDB = "female";
		}
		dobDB  = (String) getValueFromQueryResult(accountNameDetailsList, "Birthday");
		accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_ADDRESS_DETAILS_FOR_ACCOUNT_INFO_QUERY_RFL, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFL_DB);
		addressLine1DB = (String) getValueFromQueryResult(accountAddressDetailsList, "Address1");
		cityDB = (String) getValueFromQueryResult(accountAddressDetailsList, "City");
		String state = (String) getValueFromQueryResult(accountAddressDetailsList, "State");
		if(state.equalsIgnoreCase("TX")){
			stateDB = "Texas";	
		}
		postalCodeDB = (String) getValueFromQueryResult(accountAddressDetailsList, "PostalCode");
		mainPhoneNumberDB = (String) getValueFromQueryResult(accountAddressDetailsList, "PhoneNumber");

		// assert First Name with RFL
		if(assertTrueDB("First Name on UI is different from DB", storeFrontAccountInfoPage.verifyFirstNameFromUIForAccountInfo(firstNameDB), RFL_DB) == false){
			//assert First Name with RFO
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_NAME_DETAILS_QUERY, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			firstNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "FirstName");
			assertTrue("First Name on UI is different from DB", storeFrontAccountInfoPage.verifyFirstNameFromUIForAccountInfo(firstNameDB));
		}

		//assert Last Name with RFL
		if(assertTrueDB("Last Name on UI is different from DB", storeFrontAccountInfoPage.verifyLasttNameFromUIForAccountInfo(lastNameDB), RFL_DB) == false){
			// assert Last Name with RFO
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_NAME_DETAILS_QUERY, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			lastNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "LastName");
			assertTrue("Last Name on UI is different from DB", storeFrontAccountInfoPage.verifyLasttNameFromUIForAccountInfo(lastNameDB) );
		}

		//assert Address Line 1 with RFL
		if(assertTrueDB("Address Line 1 on UI is different from DB", storeFrontAccountInfoPage.verifyAddressLine1FromUIForAccountInfo(addressLine1DB), RFL_DB) == false){
			// assert Address Line 1 with RFO
			accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			addressLine1DB = (String) getValueFromQueryResult(accountAddressDetailsList, "AddressLine1");
			assertTrue("Address Line 1 on UI is different from DB", storeFrontAccountInfoPage.verifyAddressLine1FromUIForAccountInfo(addressLine1DB));
		}

		// assert City with RFL
		if(assertTrueDB("City on UI is different from DB", storeFrontAccountInfoPage.verifyCityFromUIForAccountInfo(cityDB), RFL_DB) == false){
			// assert City with RFO
			accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			cityDB = (String) getValueFromQueryResult(accountAddressDetailsList, "Locale");
			assertTrue("City on UI is different from DB", storeFrontAccountInfoPage.verifyCityFromUIForAccountInfo(cityDB));
		}

		// assert State with RFL
		if(assertTrueDB("State on UI is different from DB", storeFrontAccountInfoPage.verifyProvinceFromUIForAccountInfo(stateDB), RFL_DB) == false){
			// assert State with RFO
			accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			String provinceFromDB = (String) getValueFromQueryResult(accountAddressDetailsList, "Region");
			if(provinceFromDB.equalsIgnoreCase("TX")){
				provinceDB = "Texas";	
			}
			assertTrue("Province on UI is different from DB", storeFrontAccountInfoPage.verifyProvinceFromUIForAccountInfo(provinceDB));
		}

		// assert Postal Code with RFL
		if(assertTrueDB("", storeFrontAccountInfoPage.verifyPostalCodeFromUIForAccountInfo(postalCodeDB), RFL_DB) == false){
			//assert Postal Code eith RFO
			accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			postalCodeDB = (String) getValueFromQueryResult(accountAddressDetailsList, "PostalCode");
			assertTrue("Postal Code on UI is different from DB", storeFrontAccountInfoPage.verifyPostalCodeFromUIForAccountInfo(postalCodeDB));
		}

		// assert Main Phone Number with RFL
		if(assertTrueDB("Main Phone Number on UI is different from DB", storeFrontAccountInfoPage.verifyMainPhoneNumberFromUIForAccountInfo(mainPhoneNumberDB), RFL_DB)){
			// assert Main Phone Number with RFO
			mainPhoneNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_PHONE_NUMBER_QUERY_RFO, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			mainPhoneNumberDB = (String) getValueFromQueryResult(mainPhoneNumberList, "PhoneNumberRaw");
			assertTrue("Main Phone Number on UI is different from DB", storeFrontAccountInfoPage.verifyMainPhoneNumberFromUIForAccountInfo(mainPhoneNumberDB));
		}

		// assert Gender with RFL
		if(assertTrueDB("Gender on UI is different from DB", storeFrontAccountInfoPage.verifyGenderFromUIAccountInfo(genderDB), RFL_DB) == false){
			// assert Gender Id with RFO
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_NAME_DETAILS_QUERY, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			genderDB = String.valueOf(getValueFromQueryResult(accountNameDetailsList, "GenderId"));
			assertTrue("Gender on UI is different from DB", storeFrontAccountInfoPage.verifyGenderFromUIAccountInfo(genderDB));
		}

		// assert BirthDay with RFL
		if(assertTrueDB("DOB on UI is different from DB", storeFrontAccountInfoPage.verifyBirthDateFromUIAccountInfo(dobDB), RFL_DB) == false){
			// assert BirthDay with RFO
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_NAME_DETAILS_QUERY, TestConstants.CONSULTANT_EMAIL_ID_TST4), RFO_DB);
			dobDB = String.valueOf(getValueFromQueryResult(accountNameDetailsList, "BirthDay"));
			assertTrue("DOB on UI is different from DB", storeFrontAccountInfoPage.verifyBirthDateFromUIAccountInfo(dobDB));
		}		
		storeFrontAccountInfoPage.reUpdateAccountInformation(TestConstants.CONSULTANT_FIRST_NAME_FOR_REUPDATE_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_LAST_NAME_FOR_REUPDATE_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_ADDRESS_LINE_1_FOR_REUPDATE_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_CITY_FOR_REUPDATE_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_POSTAL_CODE_FOR_REUPDATE_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_MAIN_PHONE_NUMBER_FOR_REUPDATE_ACCOUNT_INFORMATION);
		logout();
		s_assert.assertAll();
	}

	// Test Case Hybris Phase 2-2241 :: version 1 :: Verify the various field validations
	@Test
	public void testPhoneNumberFieldValidationForConsultant_2241() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);			
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsulatantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage.enterMainPhoneNumber(TestConstants.CONSULTANT_INVALID_11_DIGIT_MAIN_PHONE_NUMBER);
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyValidationMessageOfPhoneNumber(TestConstants.CONSULTANT_VALIDATION_MESSAGE_OF_MAIN_PHONE_NUMBER),"Validation Message has not been displayed ");
		storeFrontAccountInfoPage.enterMainPhoneNumber(TestConstants.CONSULTANT_VALID_11_DIGITMAIN_PHONE_NUMBER);
		s_assert.assertFalse(storeFrontAccountInfoPage.verifyValidationMessageOfPhoneNumber(TestConstants.CONSULTANT_VALIDATION_MESSAGE_OF_MAIN_PHONE_NUMBER),"Validation Message has been displayed");
		logout();
		s_assert.assertAll();
	}

}
