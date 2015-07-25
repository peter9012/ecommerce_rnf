package com.rf.test.website.storeFront;


import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Description;
import org.testng.annotations.Test;

import com.rf.core.driver.mobile.RFMobileDriver;
import com.rf.core.utils.DBUtil;
import com.rf.core.utils.SoftAssert;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontAccountTerminationPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontRCUserPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class RFAccountInfoTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(RFAccountInfoTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontAccountTerminationPage storeFrontAccountTerminationPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
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
		s_assert.assertAll();
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4179:Enrolled Consultant, No CRP/Pulse, No Orders, No Downlines, Inctive
	@Test
	public void testEnrolledConsultantNoCRPNoPulseNoOrdersINACTIVE_4179() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_INACTIVE_RFL_4179,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();		
	}

	// Hybris Phase 2-4181:Enrolled Consultant, No CRP/Pulse, Failed Orders, No Downlines, InActive
	@Test
	public void testEnrolledConsultantNoCRPNoFailedOrdersINACTIVE_4181() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_INACTIVE_RFL_4181,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();
	}


	// Hybris Phase 2-4184:Enrolled Consultant, Has CRP/ No Pulse, No Orders, No Downlines, InActive
	@Test
	public void testEnrolledConsultantHasCRPNoOrdersACTIVE_4184() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_INACTIVE_RFL_4184,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();
	}




	// Hybris Phase 2-4186:Enrolled Consultant, No CRP/ Has Pulse, No Orders, No Downlines, InActive
	@Test
	public void testEnrolledConsultantNoCRPHasPulseNoOrdersINACTIVE_4186() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULATNT_NOCRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4186,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();
	}

	// Hybris Phase 2-4188:Enrolled Consultant, Has CRP/ Has Pulse, No Orders, No Downlines, InActive
	@Test
	public void testEnrolledConsultantHasCRPHasPulseNoOrdersINACTIVE_4188() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4188,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");			
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4190:Enrolled Consultant, Has CRP/ Has Pulse, Failed Orders, No Downlines, Inactive
	@Test
	public void testEnrolledConsultantHasCRPHasPulseFailedOrdersINACTIVE_4190() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4188,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");			
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4192:Enrolled Consultant, Has CRP/ Has Pulse, Has Submitted Orders, No Downlines, Inactive
	@Test
	public void testEnrolledConsultantHasCRPHasPulseSubmittedOrdersINACTIVE_4192() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_SUBMITTED_ORDERS_INACTIVE_RFL_4192,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");			
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4194:Enrolled Consultant, Has CRP/ Has Pulse, Has Failed Order, Has Downlines, Inactive
	@Test
	public void testEnrolledConsultantHasCRPHasPulseHasFailedOrdersINACTIVE_4194() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_INACTIVE_RFL_4194,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");		

	}

	// Hybris Phase 2-4196:Enrolled Consultant, Has CRP/ Has Pulse, Has Submitted Orders, Has Downlines, Inactive
	@Test
	public void testEnrolledConsultantHasCRPHasPulseHasSubmittedOrdersINACTIVE_4196() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_HAS_SUBMITTED_ORDERS_INACTIVE_RFL_4196,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");			
		logout();
		s_assert.assertAll();
	}	

}
