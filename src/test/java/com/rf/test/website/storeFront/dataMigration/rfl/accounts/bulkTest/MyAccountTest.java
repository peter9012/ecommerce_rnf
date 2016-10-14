package com.rf.test.website.storeFront.dataMigration.rfl.accounts.bulkTest;


import java.sql.SQLException;
import java.text.DecimalFormat;
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
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontAccountTerminationPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontReportOrderComplaintPage;
import com.rf.pages.website.storeFront.StoreFrontReportProblemConfirmationPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
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
	// Will just verify till termination popup and NOT terminate the account
	@Test
	public void testAccountTerminationPageForConsultant_3720() throws InterruptedException {
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
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
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
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
		} 
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"Account Info page has not been displayed");
		s_assert.assertFalse(storeFrontAccountInfoPage.verifyAccountTerminationLink(),"Account Termination Link Is Present");

		s_assert.assertAll();
	}

	// Hybris Phase 2-2228 :: Version : 1 :: Perform RC Account termination through my account
	@Test(enabled=false) // Will terminate the account
	public void testAccountTerminationPageForRCUser_2228() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID = null;
		String accountID = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS,RFL_DB);
		rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");
		accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
		logger.info("Account ID of the user is "+accountID);

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
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
		storeFrontHomePage.loginAsRCUser(rcUserEmailID,password);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");		
		s_assert.assertAll();

	}

	// Hybris Phase 2-1980 :: Version : 1 :: Order >>Actions >>Report problems
	@Test
	public void testOrdersReportProblems_1980() throws SQLException, InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		String consultantEmailID = null;

		List<Map<String, Object>> randomConsultantList =  null;
		String accountID = null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
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
		// // not working
		s_assert.assertAll();
	}

	//Hybris Phase 2-1981:Orders page UI for RC
	@Test
	public void testOrdersPageUIForRCUser_HP2_1981() throws SQLException, InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> orderNumberList =  null;
		List<Map<String, Object>> orderStatusList =  null;
		List<Map<String, Object>> orderGrandTotalList =  null;
		List<Map<String, Object>> orderDateList =  null;
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailAddress = null;
		String orderNumberDB = null;
		String orderStatusDB = null;
		String orderGrandTotalDB = null;
		String orderDateDB = null;

		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_RC_HAVING_SUBMITTED_ORDERS_RFL,RFL_DB);
			rcUserEmailAddress = (String) getValueFromQueryResult(randomRCList, "EmailAddress");
			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailAddress, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+rcUserEmailAddress);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserEmailAddress),"RC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// assert for order number with RFL
		orderNumberList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFL, rcUserEmailAddress),RFL_DB); 
		orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber"); 
		logger.info("Order Number from RFL DB is "+orderNumberDB);
		if(assertTrueDB("Order Number on UI is different from RFL DB",storeFrontOrdersPage.verifyOrderNumber(orderNumberDB),RFL_DB)==false){
			//assert for order number with RFO
			orderNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO,  rcUserEmailAddress),RFO_DB);
			orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
			logger.info("Order Number from RFO DB is "+orderNumberDB);
			s_assert.assertTrue(storeFrontOrdersPage.verifyOrderNumber(orderNumberDB),"Order Number on UI is different from RFO DB");
		}
		// assert for order status with RFL
		orderStatusList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFL, rcUserEmailAddress),RFL_DB);
		orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
		logger.info("Order Status from RFL DB is "+orderStatusDB);
		if(assertTrueDB("Order Status on UI is different from RFL DB",storeFrontOrdersPage.verifyOrderStatus(orderStatusDB),RFL_DB)==false){
			//assert for order status with RFO
			orderStatusList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_STATUS_FOR_CRP_ORDER_HISTORY_QUERY_RFO, rcUserEmailAddress),RFO_DB);
			orderStatusDB = (String) getValueFromQueryResult(orderStatusList, "Name");
			logger.info("Order Status from RFO DB is "+orderStatusDB);
			s_assert.assertTrue(storeFrontOrdersPage.verifyOrderStatus(orderStatusDB),"Order Status on UI is different from RFO DB");
		}
		// assert for grand total with RFL
		orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFL, rcUserEmailAddress),RFL_DB);
		DecimalFormat df = new DecimalFormat("#.00");
		orderGrandTotalDB = df.format((Number) getValueFromQueryResult(orderGrandTotalList, "GrandTotal"));
		logger.info("Order GrandTotal from RFL DB is "+orderGrandTotalDB);
		if(assertTrueDB("Grand total on UI is different from RFL DB",storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),RFL_DB)==false){
			//assert for grand total with RFO
			orderGrandTotalList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_GRAND_TOTAL_FOR_CRP_ORDER_HISTORY_QUERY_RFO, rcUserEmailAddress),RFO_DB);
			DecimalFormat dff = new DecimalFormat("#.00");
			orderGrandTotalDB = dff.format((Number) getValueFromQueryResult(orderGrandTotalList, "AmountToBeAuthorized")); 
			logger.info("Order GrandTotal from RFO DB is "+orderGrandTotalDB);
			s_assert.assertTrue(storeFrontOrdersPage.verifyGrandTotal(orderGrandTotalDB),"Grand total on UI is different from RFO DB");
		}
		// assert for order date with RFL
		orderDateList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFL, rcUserEmailAddress),RFL_DB);
		orderDateDB = String.valueOf(getValueFromQueryResult(orderDateList, "StartDate"));
		logger.info("Order Scheduled Date from RFL DB is "+orderDateDB);
		if(assertTrueDB("Scheduled date on UI is different from RFL DB",storeFrontOrdersPage.verifyScheduleDate(orderDateDB),RFL_DB)==false){
			//assert for order date with RFO
			orderDateList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DATE_FOR_CRP_ORDER_HISTORY_QUERY_RFO, rcUserEmailAddress),RFO_DB);
			orderDateDB = String.valueOf (getValueFromQueryResult(orderDateList, "CompletionDate"));
			logger.info("Order Scheduled Date from RFO DB is "+orderDateDB);
			s_assert.assertTrue(storeFrontOrdersPage.verifyScheduleDate(orderDateDB),"Scheduled date on UI is different from RFO DB");
		}

		s_assert.assertAll();		
	}

	//Hybris Phase 2-2235:Verify that user can change the information in 'my account info'.
	@Test
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
		String accountID = null;

		String consultantEmailID = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
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

		s_assert.assertAll();
	}


	// Hybris Phase 2-2241 :: version 1 :: Verify the various field validations
	@Test
	public void testPhoneNumberFieldValidationForConsultant_2241() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		String accountID = null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
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

	//Hybris Phase 2-2232:Verify that user can cancel CRP subscription through my account.
	@Test
	public void testCancelCRPSubscriptionForConsultant_2232() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		consultantEmailID ="samanthapetersen15@yahoo.com"; // A Hard Code User ,will remove soon 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);  
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		//s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyCRP();
		s_assert.assertFalse(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");
		s_assert.assertAll();
	}

	//Hybris Phase 2-2046:Add billing profile during CRP enrollment through my account
	@Test//(dependsOnMethods="testCancelCRPSubscriptionForConsultant_2232")
	public void testAddBillingProfileDuringCRPEnrollment_2046() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";

		String consultantEmailID = null;
		consultantEmailID ="samanthapetersen15@yahoo.com"; // A Hard Code User ,will remove soon  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);  
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		//s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnEnrollInCRP();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnAddToCRPButtonDuringEnrollment();
		storeFrontUpdateCartPage.clickOnCRPCheckout();
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickOnSetupCRPAccountBtn();
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the page");


		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template contains the newly added billing profile------------------------------------------------------------ --------------------------------------------- 

		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"Autoship Template Payment Method doesn't contains the newly added billing profile");

		s_assert.assertAll();
	}

	//Hybris Phase 2-2040:Edit shipping address during CRP enrollment through my account
	@Test(dependsOnMethods="testCancelCRPSubscriptionForConsultant_2232")
	public void testEditShippingAddressDuringCRPEnrollment_2040() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String newShippingAddressName = TestConstants.ADDRESS_NAME_US+randomNum;
		String lastName = "test";

		String consultantEmailID = null;
		consultantEmailID ="samanthapetersen15@yahoo.com"; // A Hard Code User ,will remove soon  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);  
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		//s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnEnrollInCRP();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnAddToCRPButtonDuringEnrollment();
		storeFrontUpdateCartPage.clickOnCRPCheckout();
		storeFrontUpdateCartPage.clickOnEditForDefaultShippingAddress();
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileAfterEditDuringEnrollment();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyEditShippingAddressNameSlectedOnUpdateCart(newShippingAddressName), "Shipping address is not updated");
		storeFrontUpdateCartPage.clickOnNextStepButtonAfterEditingDefaultShipping(); 
		storeFrontUpdateCartPage.clickOnSetupCRPAccountBtn();
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName), "Autoship Template Shipping Address doesn't contains the updated shipping address profile");

		s_assert.assertAll();
	}

}
