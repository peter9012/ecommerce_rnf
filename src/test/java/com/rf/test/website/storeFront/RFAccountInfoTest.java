package com.rf.test.website.storeFront;


import org.testng.annotations.Test;
import com.rf.core.utils.DBUtil;
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
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontAccountTerminationPage storeFrontAccountTerminationPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;

	// Test Case Hybris Phase 2-3720 :: Version : 1 :: Perform Consultant Account termination through my account
	@Test
	public void testAccountTerminationPageForConsultant() throws InterruptedException {
		storeFrontHomePage = new StoreFrontHomePage(driver);
		if(driver.getEnvName().equalsIgnoreCase("stg2")){
			storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_FOR_ACCOUNTINFO, TestConstants.PASSWORD);
		}
		else if(driver.getEnvName().equalsIgnoreCase("tst4")){
			storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_ACC_TERMINATION_EMAIL_ID_TST4,TestConstants.CONSULTANT_ACC_TERMINATION_PASSWORD_TST4);
		}
		
		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsulatantPage.verifyConsultantPage());
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsulatantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		assertTrue("Account Info page has not been displayed", storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed());
		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
		assertTrue("Account Termination Page has not been displayed",storeFrontAccountTerminationPage.verifyAccountTerminationPageIsDisplayed());
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		assertTrue("Account termination Page Pop Up Header is not Present", storeFrontAccountTerminationPage.verifyPopupHeader());
		assertTrue("Account termination page Pop up cancel termination button is not present", storeFrontAccountTerminationPage.verifyPopupCancelTerminationButton());
		assertTrue("Account termination Page Pop Up Confirm termination button is not present", storeFrontAccountTerminationPage.verifyPopupConfirmTerminationButton());
		storeFrontAccountTerminationPage.clickCancelTerminationButton();
	}


//	// Hybris Phase 2-2235:Verify that user can change the information in 'my account info'
//	@Test
//	public void testAccountInformationForUpdate() throws SQLException, InterruptedException{
//		ResultSet accountNameDetails_resultSet = null;
//		ResultSet accountAddressDetails_resultSet = null;
//		ResultSet mainPhoneNumber_resultSet = null;
//		
//		String firstNameDB = null;
//		String lastNameDB = null;
//		String genderDB = null;
//		String addressLine1DB= null;
//		String cityDB = null;
//		String provinceDB = null;
//		String postalCodeDB = null;
//		String mainPhoneNumberDB = null;
//		String dobDB = null;
//		
//		storeFrontHomePage = new StoreFrontHomePage(driver);
//		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_PASSWORD);
//		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsulatantPage.verifyConsultantPage());
//		storeFrontConsulatantPage.clickOnWelcomeDropDown();
//		storeFrontAccountInfoPage = storeFrontConsulatantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
//		assertTrue("Account Info page has not been displayed", storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed());
//		storeFrontAccountInfoPage.updateAccountInformation(TestConstants.CONSULTANT_FIRST_NAME_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_LAST_NAME_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_ADDRESS_LINE_1_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_CITY_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_POSTAL_CODE_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_MAIN_PHONE_NUMBER_FOR_ACCOUNT_INFORMATION);
//		
//		accountNameDetails_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_NAME_DETAILS_QUERY, TestConstants.CONSULTANT_EMAIL_ID_FOR_ACCOUNT_INFORMATION));
//		accountAddressDetails_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_ADDRESS_DETAILS_QUERY, TestConstants.CONSULTANT_EMAIL_ID_FOR_ACCOUNT_INFORMATION));
//		mainPhoneNumber_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ACCOUNT_PHONE_NUMBER_QUERY, TestConstants.CONSULTANT_EMAIL_ID_FOR_ACCOUNT_INFORMATION));
//		
//		while (accountNameDetails_resultSet.next()) {
//			firstNameDB = accountNameDetails_resultSet.getString("FirstName");
//			lastNameDB = accountNameDetails_resultSet.getString("LastName");
//			String genderId = accountNameDetails_resultSet.getString("GenderId");
//			System.out.println("Gender Id "+genderId);
//			if(genderId.equals("2")){
//				genderDB = "male";
//			}
//			else{
//				genderDB = "female";
//			}
//			dobDB  = accountNameDetails_resultSet.getString("Birthday");
//			System.out.println(firstNameDB);
//			System.out.println(lastNameDB);
//			System.out.println(genderDB);
//			System.out.println(dobDB);
//		}
//		
//		while (accountAddressDetails_resultSet.next()) {
//			addressLine1DB = accountAddressDetails_resultSet.getString("AddressLine1");
//			cityDB = accountAddressDetails_resultSet.getString("Locale");
//			String provinceFromDB = accountAddressDetails_resultSet.getString("Region");
//			if(provinceFromDB.equalsIgnoreCase("ON")){
//				provinceDB = "Ontario";	
//			}
//			postalCodeDB = accountAddressDetails_resultSet.getString("PostalCode");
//			System.out.println(addressLine1DB);
//			System.out.println(cityDB);
//			System.out.println(provinceDB);
//			System.out.println(postalCodeDB);
//		}
//		
//		while (mainPhoneNumber_resultSet.next()) {
//			mainPhoneNumberDB = mainPhoneNumber_resultSet.getString("PhoneNumberRaw");
//			System.out.println(mainPhoneNumberDB);
//		}
//				
//		assertTrue("First Name on UI is different from DB", storeFrontAccountInfoPage.verifyFirstNameFromUIForAccountInfo(firstNameDB));
//		assertTrue("Last Name on UI is different from DB", storeFrontAccountInfoPage.verifyLasttNameFromUIForAccountInfo(lastNameDB) );
//		assertTrue("Address Line 1 on UI is different from DB", storeFrontAccountInfoPage.verifyAddressLine1FromUIForAccountInfo(addressLine1DB));
//		assertTrue("City on UI is different from DB", storeFrontAccountInfoPage.verifyCityFromUIForAccountInfo(cityDB));
//		assertTrue("Province on UI is different from DB", storeFrontAccountInfoPage.verifyPostalCodeFromUIForAccountInfo(postalCodeDB));
//		assertTrue("Postal Code on UI is different from DB", storeFrontAccountInfoPage.verifyPostalCodeFromUIForAccountInfo(postalCodeDB));
//		assertTrue("Main Phone Number on UI is different from DB", storeFrontAccountInfoPage.verifyMainPhoneNumberFromUIForAccountInfo(mainPhoneNumberDB));
//		assertTrue("DOB on UI is different from DB", storeFrontAccountInfoPage.verifyBirthDateFromUIAccountInfo(dobDB));
//		assertTrue("Gender on UI is different from DB", storeFrontAccountInfoPage.verifyGenderFromUIAccountInfo(genderDB));
//		storeFrontAccountInfoPage.reUpdateAccountInformation(TestConstants.CONSULTANT_FIRST_NAME_FOR_REUPDATE_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_LAST_NAME_FOR_REUPDATE_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_ADDRESS_LINE_1_FOR_REUPDATE_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_CITY_FOR_REUPDATE_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_POSTAL_CODE_FOR_REUPDATE_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_MAIN_PHONE_NUMBER_FOR_REUPDATE_ACCOUNT_INFORMATION);
//	}

	//Test Case Hybris Phase 2-3719 :: Version : 1 :: Perform PC Account termination through my account
	@Test
	public void testAccountTerminationPageForPCUser() throws InterruptedException{
		storeFrontHomePage = new StoreFrontHomePage(driver);
		if(driver.getEnvName().equalsIgnoreCase("stg2")){
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(TestConstants.PCUSER_EMAIL_ID_FOR_ACCOUNTINFO, TestConstants.PCUSER_PASSWORD);
		}
		else if (driver.getEnvName().equalsIgnoreCase("tst4")){
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(TestConstants.PC_EMAIL_ID_TST4, TestConstants.PC_PASSWORD_TST4);
		}		
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		assertTrue("Account Info page has not been displayed", storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed());
		assertFalse("Account Termination Link Is Present", storeFrontAccountInfoPage.verifyAccountTerminationLink());
	}

		
	// Test Case Hybris Phase 2-2241 :: version 1 :: Verify the various field validations
	@Test
	public void testFieldValidationForConsultant() throws InterruptedException{
		storeFrontHomePage = new StoreFrontHomePage(driver);
		if(driver.getEnvName().equalsIgnoreCase("stg2")){
			storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_FOR_ACCOUNT_INFORMATION, TestConstants.CONSULTANT_PASSWORD_STG2);
		}
		else if(driver.getEnvName().equalsIgnoreCase("tst4")){
			storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_ACC_TERMINATION_EMAIL_ID_TST4, TestConstants.CONSULTANT_ACC_TERMINATION_PASSWORD_TST4);
		}
		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsulatantPage.verifyConsultantPage());
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsulatantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		assertTrue("Account Info page has not been displayed", storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed());
		storeFrontAccountInfoPage.enterMainPhoneNumber(TestConstants.CONSULTANT_INVALID_11_DIGIT_MAIN_PHONE_NUMBER);
		assertTrue("Validation Message has not been displayed ", storeFrontAccountInfoPage.verifyValidationMessageOfPhoneNumber(TestConstants.CONSULTANT_VALIDATION_MESSAGE_OF_MAIN_PHONE_NUMBER));
		storeFrontAccountInfoPage.enterMainPhoneNumber(TestConstants.CONSULTANT_VALID_11_DIGITMAIN_PHONE_NUMBER);
		assertFalse("Validation Message has been displayed", storeFrontAccountInfoPage.verifyValidationMessageOfPhoneNumber(TestConstants.CONSULTANT_VALIDATION_MESSAGE_OF_MAIN_PHONE_NUMBER));
	}
	
}
