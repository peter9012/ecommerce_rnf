package com.rf.test.website.storeFront.dataMigration.rfl.accounts;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersAutoshipStatusPage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class ViewAccountDetailsTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(ViewAccountDetailsTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontOrdersAutoshipStatusPage storeFrontOrdersAutoshipStatusPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;

	private String RFL_DB = null;
	private String RFO_DB = null;


	// Hybris Phase 2-4178:View Account Information with active templates
	@Test
	public void testAccountDetailsForAccountInfo() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
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


		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_HAVING_SUBMITTED_ORDERS_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,password);   
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");


		accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ACCOUNT_NAME_DETAILS_FOR_ACCOUNT_INFO_QUERY_RFL, consultantEmailID), RFL_DB);
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

		accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ACCOUNT_ADDRESS_DETAILS_FOR_ACCOUNT_INFO_QUERY_RFL, consultantEmailID), RFL_DB);
		addressLine1DB = (String) getValueFromQueryResult(accountAddressDetailsList, "Address1");
		cityDB = (String) getValueFromQueryResult(accountAddressDetailsList, "City");
		provinceDB = (String) getValueFromQueryResult(accountAddressDetailsList, "State");
		postalCodeDB = (String) getValueFromQueryResult(accountAddressDetailsList, "PostalCode");
		mainPhoneNumberDB = (String) getValueFromQueryResult(accountAddressDetailsList, "PhoneNumber");

		// assert First Name with RFL
		if(assertTrueDB("First Name on UI is different from DB", storeFrontAccountInfoPage.verifyFirstNameFromUIForAccountInfo(firstNameDB), RFL_DB) == false){
			//assert First Name with RFO
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, consultantEmailID), RFO_DB);
			firstNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "FirstName");
			assertTrue("First Name on UI is different from DB", storeFrontAccountInfoPage.verifyFirstNameFromUIForAccountInfo(firstNameDB));
		}

		//assert Last Name with RFL
		if(assertTrueDB("Last Name on UI is different from DB", storeFrontAccountInfoPage.verifyLasttNameFromUIForAccountInfo(lastNameDB), RFL_DB) == false){
			// assert Last Name with RFO
			accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NAME_DETAILS_QUERY, consultantEmailID), RFO_DB);
			lastNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "LastName");
			assertTrue("Last Name on UI is different from DB", storeFrontAccountInfoPage.verifyLasttNameFromUIForAccountInfo(lastNameDB) );
		}

		//assert Address Line 1 with RFL
		if(assertTrueDB("Address Line 1 on UI is different from DB", storeFrontAccountInfoPage.verifyAddressLine1FromUIForAccountInfo(addressLine1DB), RFL_DB) == false){
			// assert Address Line 1 with RFO
			accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, consultantEmailID), RFO_DB);
			addressLine1DB = (String) getValueFromQueryResult(accountAddressDetailsList, "AddressLine1");
			assertTrue("Address Line 1 on UI is different from DB", storeFrontAccountInfoPage.verifyAddressLine1FromUIForAccountInfo(addressLine1DB));
		}

		// assert City with RFL
		if(assertTrueDB("City on UI is different from DB", storeFrontAccountInfoPage.verifyCityFromUIForAccountInfo(cityDB), RFL_DB) == false){
			// assert City with RFO
			accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, consultantEmailID), RFO_DB);
			cityDB = (String) getValueFromQueryResult(accountAddressDetailsList, "Locale");
			assertTrue("City on UI is different from DB", storeFrontAccountInfoPage.verifyCityFromUIForAccountInfo(cityDB));
		}

		// assert State with RFL
		if(assertTrueDB("State on UI is different from DB", storeFrontAccountInfoPage.verifyProvinceFromUIForAccountInfo(provinceDB), RFL_DB) == false){
			// assert State with RFO
			accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, consultantEmailID), RFO_DB);
			provinceDB = (String) getValueFromQueryResult(accountAddressDetailsList, "Region");
			/*if(provinceFromDB.equalsIgnoreCase("TX")){
	    provinceDB = "Texas"; 
	   }*/
			assertTrue("Province on UI is different from DB", storeFrontAccountInfoPage.verifyProvinceFromUIForAccountInfo(provinceDB));
		}

		// assert Postal Code with RFL
		if(assertTrueDB("", storeFrontAccountInfoPage.verifyPostalCodeFromUIForAccountInfo(postalCodeDB), RFL_DB) == false){
			//assert Postal Code eith RFO
			accountAddressDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ADDRESS_DETAILS_QUERY_RFO, consultantEmailID), RFO_DB);
			postalCodeDB = (String) getValueFromQueryResult(accountAddressDetailsList, "PostalCode");
			assertTrue("Postal Code on UI is different from DB", storeFrontAccountInfoPage.verifyPostalCodeFromUIForAccountInfo(postalCodeDB));
		}

		// assert Main Phone Number with RFL
		if(assertTrueDB("Main Phone Number on UI is different from DB", storeFrontAccountInfoPage.verifyMainPhoneNumberFromUIForAccountInfo(mainPhoneNumberDB), RFL_DB) == false){
			// assert Main Phone Number with RFO
			mainPhoneNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_PHONE_NUMBER_QUERY_RFO, consultantEmailID), RFO_DB);
			mainPhoneNumberDB = (String) getValueFromQueryResult(mainPhoneNumberList, "PhoneNumberRaw");
			assertTrue("Main Phone Number on UI is different from DB", storeFrontAccountInfoPage.verifyMainPhoneNumberFromUIForAccountInfo(mainPhoneNumberDB));
		}

		/*// assert Gender with RFL
	  if(assertTrueDB("Gender on UI is different from DB", storeFrontAccountInfoPage.verifyGenderFromUIAccountInfo(genderDB), RFL_DB) == false){
	   // assert Gender Id with RFO
	   accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ACCOUNT_NAME_DETAILS_QUERY, consultantEmailID), RFO_DB);
	   genderDB = String.valueOf(getValueFromQueryResult(accountNameDetailsList, "GenderId"));
	   assertTrue("Gender on UI is different from DB", storeFrontAccountInfoPage.verifyGenderFromUIAccountInfo(genderDB));
	  }

	  // assert BirthDay with RFL
	  if(assertTrueDB("DOB on UI is different from DB", storeFrontAccountInfoPage.verifyBirthDateFromUIAccountInfoForCheckAccountInfo(dobDB), RFL_DB) == false){
	   // assert BirthDay with RFO
	   accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ACCOUNT_NAME_DETAILS_QUERY, consultantEmailID), RFO_DB);
	   dobDB = String.valueOf(getValueFromQueryResult(accountNameDetailsList, "BirthDay"));
	   assertTrue("DOB on UI is different from DB", storeFrontAccountInfoPage.verifyBirthDateFromUIAccountInfoForCheckAccountInfo(dobDB));
	  }  
		 */  
		 s_assert.assertAll();
	}

	// Hybris Phase 2-4179:Enrolled Consultant, No CRP/Pulse, No Orders, No Downlines, Inctive
	@Test
	public void testEnrolledConsultantNoCRPNoPulseNoOrdersINACTIVE_4179() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_INACTIVE_RFL_4179,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
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
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_INACTIVE_RFL_4181,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();
	}


	// Hybris Phase 2-4182:Inactive Account should have no autoship template	 
	@Test
	public void testNoAutoshipTemplateForInactiveAccount_4182() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO(); 
		String userEmailID = null;
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_INACTIVE_ACCOUNT_NO_AUTOSHIP_TEMPLATE_4182, RFL_DB);
		userEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(userEmailID, password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");   
		s_assert.assertAll();
	}

	// Hybris Phase 2-4184:Enrolled Consultant, Has CRP/ No Pulse, No Orders, No Downlines, InActive
	@Test(enabled=false) // NO DATA FROM DATABASE
	public void testEnrolledConsultantHasCRPNoOrdersACTIVE_4184() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_INACTIVE_RFL_4184,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();
	}


	// Hybris Phase 2-4186:Enrolled Consultant, No CRP/ Has Pulse, No Orders, No Downlines, InActive
	@Test(enabled=false) // NO DATA FROM DATABASE
	public void testEnrolledConsultantNoCRPHasPulseNoOrdersINACTIVE_4186() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULATNT_NOCRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4186,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();
	}

	// Hybris Phase 2-4188:Enrolled Consultant, Has CRP/ Has Pulse, No Orders, No Downlines, InActive
	@Test(enabled=false) // NO DATA FROM DATABASE
	public void testEnrolledConsultantHasCRPHasPulseNoOrdersINACTIVE_4188() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4188,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");			
		s_assert.assertAll();
	}


	// Hybris Phase 2-4189 :: Version : 1 :: Enrolled Consultant, Has CRP/ Has Pulse, Failed Orders, No Downlines, Active
	@Test
	public void testEnrolledConsultantHasCRPHasPulseFailedOrdersACTIVE_4189() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_INACTIVE_RFL_4189,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage = storeFrontAccountInfoPage.clickOnAccountInfoFromLeftPanel();
		storeFrontOrdersAutoshipStatusPage = storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipStatusHeader(),"Autoship status header is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipCRPStatus(),"AutoShip CRP Status is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipPulseSubscriptionStatus(),"AutoShip Pulse Subscription Status is not as expected");
		
		s_assert.assertAll();

	}

	// Hybris Phase 2-4191 :: Version : 1 :: Enrolled Consultant, Has CRP/ Has Pulse, Has Submitted Orders, No Downlines, Active 
	@Test
	public void testEnrolledConsultantHasCRPHasPulseSubmittedOrdersACTIVE_4191() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_INACTIVE_RFL_4189,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage = storeFrontAccountInfoPage.clickOnAccountInfoFromLeftPanel();
		storeFrontOrdersAutoshipStatusPage = storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipStatusHeader(),"Autoship status header is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipCRPStatus(),"AutoShip CRP Status is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipPulseSubscriptionStatus(),"AutoShip Pulse Subscription Status is not as expected");
		
		s_assert.assertAll();

	}


	// Hybris Phase 2-4190:Enrolled Consultant, Has CRP/ Has Pulse, Failed Orders, No Downlines, Inactive
	@Test(enabled=false) // NO DATA FROM DATABASE
	public void testEnrolledConsultantHasCRPHasPulseFailedOrdersINACTIVE_4190() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4190,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");			
		s_assert.assertAll();
	}

	// Hybris Phase 2-4192:Enrolled Consultant, Has CRP/ Has Pulse, Has Submitted Orders, No Downlines, Inactive
	@Test(enabled=false) // NO DATA FROM DATABASE
	public void testEnrolledConsultantHasCRPHasPulseSubmittedOrdersINACTIVE_4192() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_SUBMITTED_ORDERS_INACTIVE_RFL_4192,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");		
		s_assert.assertAll();
	}


	// Hybris Phase 2-4193 :: Version : 1 :: Enrolled Consultant, Has CRP/ Has Pulse, Has Failed Order, Has Downlines, Active 
	@Test
	public void testEnrolledConsultantHasCRPHasPulseFailedOrdersOrdersACTIVE_4193() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_ACTIVE_RFL_4193,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		storeFrontAccountInfoPage = storeFrontAccountInfoPage.clickOnAccountInfoFromLeftPanel();
		storeFrontOrdersAutoshipStatusPage = storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipStatusHeader(),"Autoship status header is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipCRPStatus(),"AutoShip CRP Status is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipPulseSubscriptionStatus(),"AutoShip Pulse Subscription Status is not as expected");
		
		s_assert.assertAll();

	}


	// Hybris Phase 2-4194:Enrolled Consultant, Has CRP/ Has Pulse, Has Failed Order, Has Downlines, Inactive
	@Test(enabled=false)// NO DATA FROM DATABASE
	public void testEnrolledConsultantHasCRPHasPulseHasFailedOrdersINACTIVE_4194() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_INACTIVE_RFL_4194,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");		

	}

	// Hybris Phase 2-4196:Enrolled Consultant, Has CRP/ Has Pulse, Has Submitted Orders, Has Downlines, Inactive
	@Test(enabled=false) // NO DATA FROM DATABASE
	public void testEnrolledConsultantHasCRPHasPulseHasSubmittedOrdersINACTIVE_4196() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_HAS_SUBMITTED_ORDERS_INACTIVE_RFL_4196,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");			
		s_assert.assertAll();
	}	

	//Hybris Phase 2-4227 :: Version : 1 :: Account with NULL email Address
	@Test
	public void testAccountWithNullEmailAddress_4227() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> userWithNoEmailAddressIDList =  null;		
		userWithNoEmailAddressIDList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_ACCOUNTS_WITH_NULL_EMAIL_ADDRESS,RFL_DB);
		s_assert.assertTrue(userWithNoEmailAddressIDList.size()==0,"Account with NULL email Address exist");			
		s_assert.assertAll();
	}	

	//Hybris Phase 2-4223 :: Version : 1 :: Account with multiple payment profiles
	@Test
	public void testBillingInfoPageDetails_4223() throws SQLException, InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomEmailList =  null;
		List<Map<String, Object>> billingAddressCountList =  null;
		int totalBillingAddressesFromDB = 0;
		String userEmailId = null;
		randomEmailList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_USER_MULTIPLE_PAYMENTS_RFL,RFL_DB);
		userEmailId =  (String) getValueFromQueryResult(randomEmailList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(userEmailId, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

		// assert with RFL
		billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_BILLING_ADDRESS_COUNT_QUERY_TST4,userEmailId),RFL_DB);
		totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
		if(assertEqualsDB("Billing Addresses count on UI is different from DB", totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed(),RFL_DB)==false){
			//assert with RFO
			billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_BILLING_ADDRESS_COUNT_QUERY,userEmailId),RFO_DB);
			totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");			
			s_assert.assertEquals(totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed(),"Billing Addresses count on UI is different from DB");			
		}
		
		s_assert.assertAll();
	}

	// Hybris Phase 2-4195 : Enrolled Consultant, Has CRP/ Has Pulse, Has Submitted Orders
	@Test(enabled=false) // WRONG DATA FROM QUERY,Need to check
	public void testEnrolledConsultantHasCRPPULSESubmittedOrders_HP2_4195() throws InterruptedException, SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantEmailIdList =  null;
		List<Map<String, Object>> orderNumberList =  null;		
		String consultantEmail = null;
		randomConsultantEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_ORDERS_RFL,RFL_DB);
		consultantEmail = (String) getValueFromQueryResult(randomConsultantEmailIdList, "UserName");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmail, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		//		orderNumberList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFL, consultantEmail),RFL_DB);
		//		//assert on RFL
		//		orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
		//		if(assertTrueDB("Status of Order Number is not found as Submitted",storeFrontOrdersPage.verifyOrderStatusToBeSubmitted(orderNumberDB),RFL_DB)==false){
		//			//assert On RFO
		//			orderNumberList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO, consultantEmail),RFO_DB);
		//			orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
		//			assertTrue("Status of Order Number is not found as Submitted",storeFrontOrdersPage.verifyOrderStatusToBeSubmitted(orderNumberDB));	
		//		}		
		s_assert.assertTrue(storeFrontOrdersPage.verifyForSubmittedAdhocOrders(),"Submitted Orders NOT present");
		storeFrontAccountInfoPage = storeFrontOrdersPage.clickOnAccountInfoFromLeftPanel();
		storeFrontOrdersAutoshipStatusPage = storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipStatusHeader(),"Autoship status header is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipCRPStatus(),"AutoShip CRP Status is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipPulseSubscriptionStatus(),"AutoShip Pulse Subscription Status is not as expected");
		
		s_assert.assertAll();
	}


	//Hybris Phase 2-4205 : Enrolled PC, Has CRP/ Has Pulse, Has Submitted Orders
	@Test(enabled=false) // NO DATA FROM DATABASE
	public void testEnrolledPCHasCRPPULSESubmittedOrders_HP2_4205() throws InterruptedException, SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserEmailIdList =  null;
		String pcUserEmail = null;
		randomPCUserEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PC_HAS_CRP_PULSE_SUBMITTED_ORDERS_RFL_4205,RFL_DB);
		pcUserEmail = (String) getValueFromQueryResult(randomPCUserEmailIdList, "UserName");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmail, password);
		s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontOrdersAutoshipStatusPage = storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipStatusHeader(),"Autoship status header is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipCRPStatus(),"AutoShip CRP Status is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipPulseSubscriptionStatus(),"AutoShip Pulse Subscription Status is not as expected");
		
		s_assert.assertAll();
	}

	//Hybris Phase 2-4200:Enrolled RC , Failed Order
	@Test(enabled=false) // NO DATA FROM DATABASE
	public void testEnrolledRCHasFailedOrders_HP2_4200() throws InterruptedException, SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCUserEmailIdList =  null;
		String rcUserEmail = null;
		randomRCUserEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ENROLLED_RC_USER_HAS_FAILED_ORDER_RFL_4200,RFL_DB);
		rcUserEmail = (String) getValueFromQueryResult(randomRCUserEmailIdList, "UserName");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmail, password);
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.isOrderOfRequiredTypePresentInHistory("FAILED"), "Failed order is not present in order history");

	}

}
