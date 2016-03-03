package com.rf.test.website.crm.regression;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.crm.CRMAccountDetailsPage;
import com.rf.pages.website.crm.CRMContactDetailsPage;
import com.rf.pages.website.crm.CRMHomePage;
import com.rf.pages.website.crm.CRMLoginPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class CRMRegressionTest extends RFWebsiteBaseTest{

	private static final Logger logger = LogManager
			.getLogger(CRMRegressionTest.class.getName());

	private CRMLoginPage crmLoginpage;
	private CRMHomePage crmHomePage;
	private CRMAccountDetailsPage crmAccountDetailsPage; 
	private CRMContactDetailsPage crmContactDetailsPage;
	private StoreFrontHomePage storeFrontHomePage;
	private String RFO_DB = null;

	//Hybris Project-4527:Search for account by email address
	@Test 
	public void testSearchForAccountByEmail_4527() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		logger.info("The email address is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isAccountActivitiesLinkPresentInLeftNaviagation(), "Accounts Activities link is not present on left navigation panel");

		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
		s_assert.assertAll();
	}

	//Hybris Project-4530:Search for account by name
	@Test
	public void testSearchForAccountByName_4530() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomFirstName =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String firstName = null;
		randomFirstName = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_FirstName_RFO,countryId),RFO_DB);	
		firstName = (String) getValueFromQueryResult(randomFirstName, "FirstName");		
		logger.info("The first name is "+firstName);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(firstName);
		String nameOnFirstRow = crmHomePage.getNameOnFirstRowInSearchResults();
		s_assert.assertTrue(nameOnFirstRow.toLowerCase().trim().contains(firstName.toLowerCase().trim()), "the name on first row which is = "+nameOnFirstRow.toLowerCase().trim()+" is expected to contain firstname = "+firstName.toLowerCase().trim());
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isAccountActivitiesLinkPresentInLeftNaviagation(), "Accounts Activities link is not present on left navigation panel");

		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
		s_assert.assertAll();
	}

	//Hybris Project-4545:View Consultant Account details
	@Test
	public void testViewConsultantAccountDetailsTest_4545() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		logger.info("The username is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isAccountActivitiesLinkPresentInLeftNaviagation(), "Accounts Activities link is not present on left navigation panel");

		crmHomePage.clickNameOnFirstRowInSearchResults();

		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsSectionPresent(),"Account Details Section is not present");
		s_assert.assertTrue(crmAccountDetailsPage.isMainAddressSectionPresent(),"Main Address Section is not present");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("Edit Account"),"Edit Account button is not Enabled in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("Edit PWS Domain"),"Edit PWS Domain button is not Enabled in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("Change Account Status"),"Change Account Status button is not Enabled in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("Pulse"),"Pulse button is not Enabled in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("New Pulse"),"New Pulse button is not Enabled in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("My Account"),"My Account button is not Enabled in account detail section");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Name"),"Account Name label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Name"),"Recognition Name label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Title"),"Recognition Title label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Active"),"Active label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Status"),"Account Status label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Soft Termination Date"),"Soft Termination Date label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Hard Termination Date"),"Hard Termination Date label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("SV"),"SV label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("PSQV"),"PSQV label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Number"),"Account Number label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Type"),"Account Type label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Sponsor"),"Enrollment Sponsor label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Placement Sponsor"),"Placement Sponsor label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Date"),"Enrollment Date label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Next Renewal Date"),"Next Renewal Date label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Last Renewal Date"),"Last Renewal Date label is not present in account detail section");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 1"),"Address Line 1 label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 2"),"Address Line 2 label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 3"),"Address Line 3 label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Locale"),"Locale label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Sub Region"),"Sub Region label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Region"),"Region label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Postal code"),"Postal code label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Country"),"Country label is not present in main address section");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderTaxInformationSectionPresent("Business Entity"),"Business Entity label is not present under tax information section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderTaxInformationSectionPresent("Tax Exempt"),"Tax Exempt label is not present under tax information section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderTaxInformationSectionPresent("Tax ID Number"),"Tax ID Number label is not present under tax information section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderTaxInformationSectionPresent("Legal Tax Name"),"Legal Tax Name label is not present under tax information section");

		s_assert.assertAll();
	}


	//Hybris Project-4548:Preferred Customer detail view page
	@Test
	public void testPreferredCustomerdDetailViewTest_4548() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomPCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcUserName = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
		logger.info("The username is "+pcUserName);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(pcUserName.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+pcUserName.toLowerCase().trim());
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isAccountActivitiesLinkPresentInLeftNaviagation(), "Accounts Activities link is not present on left navigation panel");

		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusFieldDisplayedAndNonEmpty(),"Account Status is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isCountryFieldDisplayedAndNonEmpty(),"Country field is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isPlacementSponsorFieldDisplayed(),"Placement sponsor is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEnrollmentDateFieldDisplayedAndNonEmpty(),"Enrollment Date is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMainPhoneFieldDisplayedAndNonEmpty(),"Main Phone is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEmailAddressFieldDisplayedAndNonEmpty(),"Email Address is not displayed on account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Contacts"),"Contact link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Autoships"),"Autoships link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Account Activities"),"Account Activities link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Shipping Profiles"),"Shipping Profiles link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Billing  Profiles"),"Billing  Profiles link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Account Statuses History"),"Account Statuses History link is not displayed on account in section account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Contacts"),"Contacts mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Autoships"),"Autoships mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Account Activities"),"Account Activities mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Shipping Profiles"),"Shipping Profiles mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Billing"),"Billing Profiles mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Account Statuses History"),"Contacts mouse hover section is not displayed on account section in account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Contacts"),"Contacts blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Autoships"),"Autoships blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Account Activities"),"Account Activities blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Shipping Profiles"),"Shipping Profiles blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Billing"),"Billing Profiles blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Account Statuses History"),"Account Statuses History blue line section is not displayed on account section in account details page");

		s_assert.assertAll();
	}

	//Hybris Project-4550:Retail Customer detail view page
	@Test
	public void testRetailCustomerdDetailViewTest_4550() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomRCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcUserName = null;
		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcUserName = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
		logger.info("The username is "+rcUserName);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(rcUserName.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+rcUserName.toLowerCase().trim());
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isAccountActivitiesLinkPresentInLeftNaviagation(), "Accounts Activities link is not present on left navigation panel");

		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusFieldDisplayedAndNonEmpty(),"Account Status is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isCountryFieldDisplayedAndNonEmpty(),"Country field is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isPlacementSponsorFieldDisplayed(),"Placement sponsor is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEnrollmentDateFieldDisplayedAndNonEmpty(),"Enrollment Date is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMainPhoneFieldDisplayedAndNonEmpty(),"Main Phone is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEmailAddressFieldDisplayedAndNonEmpty(),"Email Address is not displayed on account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Contacts"),"Contact link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Account Activities"),"Account Activities link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Shipping Profiles"),"Shipping Profiles link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Billing  Profiles"),"Billing  Profiles link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Account Statuses History"),"Account Statuses History link is not displayed on account in section account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Contacts"),"Contacts mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Account Activities"),"Account Activities mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Shipping Profiles"),"Shipping Profiles mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Billing"),"Billing Profiles mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Account Statuses History"),"Contacts mouse hover section is not displayed on account section in account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Contacts"),"Contacts blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Account Activities"),"Account Activities blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Shipping Profiles"),"Shipping Profiles blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Billing"),"Billing Profiles blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Account Statuses History"),"Account Statuses History blue line section is not displayed on account section in account details page");

		s_assert.assertAll();
	}

	//Hybris Project-4491:Add Shipping Profile for Consultant
	@Test
	public void testAddShippingProfileForConsultant_4491() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		logger.info("The username is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected default selected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	//Hybris Project-4492:Add Shipping Profile for PC
	@Test
	public void testAddShippingProfileForPC_4492() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomPCList =  null;
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");		
		logger.info("The username is "+pcEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected default selected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	//Hybris Project-4493:Add Shipping Profile for RC
	@Test
	public void testAddShippingProfileForRC_4493() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomRCList =  null;
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
		logger.info("The username is "+rcEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected default selected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	// Hybris Project-4480:Add the Account Notes for PC
	@Test
	public void testAddAccountNoteForPCUser_4480() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomPCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcUserEmailID = null;
		String orderNote="This is automation note"+randomNum;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");   
		logger.info("The username is "+pcUserEmailID); 

		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(pcUserEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+pcUserEmailID.toLowerCase().trim());

		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(),"Log Account Activity Section is not present on Account Details page");
		//Verify account dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPagePresent(),"Account dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownSearchOnAccountDetailPagePresent(),"Account dropdown search button is not present on Account Details page");
		//verify channel dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPagePresent(),"Channel dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Email"),"Channel dropdown Email option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Call"),"Channel dropdown call option is not present on Account Details page");
		//Verify reason dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOnAccountDetailPagePresent(),"Reason dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Consultants"),"Reason dropdown Consultants option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("CRP"),"Reason dropdown CRP option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("PCs"),"Reason dropdown PCs option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Order"),"Reason dropdown Order option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Returns"),"Reason dropdown Returns option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("RF Mall"),"Reason dropdown RF Mall option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Pulse"),"Reason dropdown Pulse option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Sales Support RF Connection Transfer"),"Reason dropdown Sales Support RF option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Technology"),"Reason dropdown Technology option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Marketing Promotion"),"Reason dropdown Marketing Promotion option is not present on Account Details page");
		//verify details dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isDetailsDropdownOnAccountDetailPagePresent(),"Detail dropdown is not present on Account Details page");
		//verify note section
		s_assert.assertTrue(crmAccountDetailsPage.isNoteSectionOnAccountDetailPagePresent(),"Note section is not present on Account Details page");
		//add note and click save.
		crmAccountDetailsPage.selectChannelDropdown("Email");
		crmAccountDetailsPage.selectReasonDropdown("Consultants");
		crmAccountDetailsPage.selectDetailDropdown("Consultant event approval");
		crmAccountDetailsPage.enterNote(orderNote);
		crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
		//Verify channel account and reason dropdown are disabled.
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPageIsEnabled(),"Account dropdown is not disable on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPageIsEnabled(),"Channel dropdown is not disable on Account Details page");
		s_assert.assertFalse(crmAccountDetailsPage.isDetailDropdownOnAccountDetailPageIsEnabled(),"Detail dropdown is not disable on Account Details page");
		//verify the note in account activity section present
		s_assert.assertTrue(crmAccountDetailsPage.getNoteFromUIOnAccountDetailPage().contains(orderNote),"Note text from UI is "+crmAccountDetailsPage.getNoteFromUIOnAccountDetailPage()+" While expected text is "+orderNote);
		s_assert.assertAll();
	}

	//Hybris Project-4481:Add the Account Notes for RC
	@Test
	public void testAddAccountNoteForRC_4481() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomRCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcUserEmailId = null;
		String accountId=null;
		String orderNote="This is automation note"+randomNum;

		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcUserEmailId = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
		logger.info("The username is "+rcUserEmailId); 

		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserEmailId);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(rcUserEmailId.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+rcUserEmailId.toLowerCase().trim());

		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(),"Log Account Activity Section is not present on Account Details page");
		//Verify account dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPagePresent(),"Account dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownSearchOnAccountDetailPagePresent(),"Account dropdown search button is not present on Account Details page");
		//verify channel dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPagePresent(),"Channel dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Email"),"Channel dropdown Email option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Call"),"Channel dropdown call option is not present on Account Details page");
		//Verify reason dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOnAccountDetailPagePresent(),"Reason dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Consultants"),"Reason dropdown Consultants option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("CRP"),"Reason dropdown CRP option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("PCs"),"Reason dropdown PCs option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Order"),"Reason dropdown Order option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Returns"),"Reason dropdown Returns option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("RF Mall"),"Reason dropdown RF Mall option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Pulse"),"Reason dropdown Pulse option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Sales Support RF Connection Transfer"),"Reason dropdown Sales Support RF option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Technology"),"Reason dropdown Technology option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Marketing Promotion"),"Reason dropdown Marketing Promotion option is not present on Account Details page");
		//verify details dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isDetailsDropdownOnAccountDetailPagePresent(),"Detail dropdown is not present on Account Details page");
		//verify note section
		s_assert.assertTrue(crmAccountDetailsPage.isNoteSectionOnAccountDetailPagePresent(),"Note section is not present on Account Details page");
		//add note and click save.
		crmAccountDetailsPage.selectChannelDropdown("Email");
		crmAccountDetailsPage.selectReasonDropdown("Consultants");
		crmAccountDetailsPage.selectDetailDropdown("Consultant event approval");
		crmAccountDetailsPage.enterNote(orderNote);
		crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
		//Verify channel account and reason dropdown are disabled.
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPageIsEnabled(),"Account dropdown is not disable on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPageIsEnabled(),"Channel dropdown is not disable on Account Details page");
		s_assert.assertFalse(crmAccountDetailsPage.isDetailDropdownOnAccountDetailPageIsEnabled(),"Detail dropdown is not disable on Account Details page");
		//verify the note in account activity section present
		s_assert.assertTrue(crmAccountDetailsPage.getNoteFromUIOnAccountDetailPage().contains(orderNote),"Note text from UI is "+crmAccountDetailsPage.getNoteFromUIOnAccountDetailPage()+" While expected text is "+orderNote);
		s_assert.assertAll();
	}

	//Hybris Project-4499:Verify the Proxy to Pulse for a Consultant
	@Test 
	public void testVerifyProxyToPulseForConsultant_4499() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		logger.info("The email address is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());		
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
				emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
			}else{
				break;
			}
		}
		crmHomePage.clickNameWithActiveStatusInSearchResults();
		crmAccountDetailsPage.clickAccountDetailsButton("Pulse");
		crmAccountDetailsPage.switchToChildWindow();
		s_assert.assertTrue(crmAccountDetailsPage.isSVSectionPresentOnPulsePage(), "SV Section is not present on Pulse Page");		
		s_assert.assertAll();
	}

	//Hybris Project-4542:View Shipping Profile for Consultant
	@Test
	public void testViewShippingProfileForConsultant_4542() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		logger.info("The email address is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());		
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
				emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
			}else{
				break;
			}
		}
		crmHomePage.clickNameWithActiveStatusInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Action"),"Action label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Name"),"Name label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("ProfileName"),"ProfileName label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Default"),"Default label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 1"),"Address Line 1 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 2"),"Address Line 2 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 3"),"Address Line 3 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Locale"),"Locale label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Region"),"Region label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Postal code"),"Postal code label is not present in Shipping address section");

		String shippingProfilesCount = crmAccountDetailsPage.getShippingProfilesCount();
		String countDisplayedWithShippingLink = crmAccountDetailsPage.getCountDisplayedWithLink("Shipping Profiles");
		s_assert.assertTrue(shippingProfilesCount.equals(countDisplayedWithShippingLink), "shipping profiles count = "+shippingProfilesCount+"while count Displayed With Shipping Link = "+countDisplayedWithShippingLink);
		s_assert.assertTrue(crmAccountDetailsPage.isOnlyOneShippingProfileIsDefault(),"default shipping profiles is not one");

		s_assert.assertAll();
	}

	// Hybris Project-4479:Add the Account Notes for Consultant
	@Test
	public void testAddAccountNoteForConsultant_4479() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		String orderNote="This is automation note"+randomNum;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The username is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());

		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(),"Log Account Activity Section is not present on Account Details page");
		//Verify account dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPagePresent(),"Account dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownSearchOnAccountDetailPagePresent(),"Account dropdown search button is not present on Account Details page");
		//verify channel dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPagePresent(),"Channel dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Email"),"Channel dropdown Email option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Call"),"Channel dropdown call option is not present on Account Details page");
		//Verify reason dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOnAccountDetailPagePresent(),"Reason dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Consultants"),"Reason dropdown Consultants option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("CRP"),"Reason dropdown CRP option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("PCs"),"Reason dropdown PCs option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Order"),"Reason dropdown Order option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Returns"),"Reason dropdown Returns option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("RF Mall"),"Reason dropdown RF Mall option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Pulse"),"Reason dropdown Pulse option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Sales Support RF Connection Transfer"),"Reason dropdown Sales Support RF option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Technology"),"Reason dropdown Technology option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Marketing Promotion"),"Reason dropdown Marketing Promotion option is not present on Account Details page");
		//verify details dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isDetailsDropdownOnAccountDetailPagePresent(),"Detail dropdown is not present on Account Details page");
		//verify note section
		s_assert.assertTrue(crmAccountDetailsPage.isNoteSectionOnAccountDetailPagePresent(),"Note section is not present on Account Details page");
		//add note and click save.
		crmAccountDetailsPage.selectChannelDropdown("Email");
		crmAccountDetailsPage.selectReasonDropdown("Consultants");
		crmAccountDetailsPage.selectDetailDropdown("Consultant event approval");
		crmAccountDetailsPage.enterNote(orderNote);
		crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
		//Verify channel account and reason dropdown are disabled.
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPageIsEnabled(),"Account dropdown is not disable on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPageIsEnabled(),"Channel dropdown is not disable on Account Details page");
		s_assert.assertFalse(crmAccountDetailsPage.isDetailDropdownOnAccountDetailPageIsEnabled(),"Detail dropdown is not disable on Account Details page");
		//verify the note in account activity section present
		s_assert.assertTrue(crmAccountDetailsPage.getNoteFromUIOnAccountDetailPage().contains(orderNote),"Note text from UI is "+crmAccountDetailsPage.getNoteFromUIOnAccountDetailPage()+" While expected text is "+orderNote);
		s_assert.assertAll();
	}

	// Hybris Project-4507:Edit Preferred Customer Account details
	@Test
	public void testEditPreferredCustomerActDetails_4507() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomPCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcUserName = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		logger.info("The username is "+pcUserName); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating Recognition Name field
		crmAccountDetailsPage.updateRecognitionNameField(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify updated Recognition Name in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getRecognitionName().equalsIgnoreCase(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME),"Recognition name is not updated");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating Main address
		crmAccountDetailsPage.updateAddressLine3Field(TestConstants.CRM_MAIN_ADDRESS_LINE_3);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify updated Main Address in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getMainAddressLine3().trim().equalsIgnoreCase(TestConstants.CRM_MAIN_ADDRESS_LINE_3.trim()),"Main account address is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4510:Edit Retail Customer Account details
	@Test
	public void testEditRetailCustomerActDetails_4510() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomRCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcUserName = null;
		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcUserName = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
		logger.info("The username is "+rcUserName); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating Recognition Name field
		crmAccountDetailsPage.updateRecognitionNameField(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify updated Recognition Name in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getRecognitionName().equalsIgnoreCase(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME),"Recognition name is not updated");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating Main address
		crmAccountDetailsPage.updateAddressLine3Field(TestConstants.CRM_MAIN_ADDRESS_LINE_3);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify updated Main Address in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getMainAddressLine3().trim().equalsIgnoreCase(TestConstants.CRM_MAIN_ADDRESS_LINE_3.trim()),"Main account address is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4513:Edit Shipping Profile for Consultant
	@Test
	public void testEditShippingProfileForConsultant_4513() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstNameWithSpecialChar = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The username is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstNameWithSpecialChar+" "+lastName);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getFirstShippingProfileName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstNameWithSpecialChar), "Expected shipping profile name is "+shippingProfileFirstNameWithSpecialChar+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	//Hybris Project-4544:View Shipping Profile for RC
	@Test
	public void testViewShippingProfileForRC_4544() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomRCList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcEmailID = null;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
		logger.info("The email address is "+rcEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(rcEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+rcEmailID.toLowerCase().trim());		
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser()==false){
				logger.info("No active user in the search results..searching new user");
				randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
				logger.info("The email address is "+rcEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
				emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
			}else{
				break;
			}
		}
		crmHomePage.clickNameWithActiveStatusInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Action"),"Action label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Name"),"Name label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("ProfileName"),"ProfileName label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Default"),"Default label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 1"),"Address Line 1 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 2"),"Address Line 2 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 3"),"Address Line 3 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Locale"),"Locale label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Region"),"Region label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Postal code"),"Postal code label is not present in Shipping address section");

		String shippingProfilesCount = crmAccountDetailsPage.getShippingProfilesCount();
		String countDisplayedWithShippingLink = crmAccountDetailsPage.getCountDisplayedWithLink("Shipping Profiles");
		s_assert.assertTrue(shippingProfilesCount.equals(countDisplayedWithShippingLink), "shipping profiles count = "+shippingProfilesCount+"while count Displayed With Shipping Link = "+countDisplayedWithShippingLink);
		s_assert.assertTrue(crmAccountDetailsPage.isOnlyOneShippingProfileIsDefault(),"default shipping profiles is not one");

		s_assert.assertAll();
	}

	//Hybris Project-4537:View Billing profile for a consultant
	@Test
	public void testViewBillingProfileForConsultant_4537() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		logger.info("The email address is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());		
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
				emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
			}else{
				break;
			}
		}
		crmHomePage.clickNameWithActiveStatusInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Action"),"Action label is not present in Billing profiles section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Name"),"Name label is not present in Billing profiles section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Profile Name"),"Profile Name label is not present in Billing profiles section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Default"),"Default label is not present in Billing profiles section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Name On Card"),"Name On Card label is not present in Billing profiles section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Valid Thru"),"Valid Thru label is not present in Billing profiles section");

		String billingProfilesCount = crmAccountDetailsPage.getBillingProfilesCount();
		String countDisplayedWithBillingLink = crmAccountDetailsPage.getCountDisplayedWithLink("Billing");
		s_assert.assertTrue(billingProfilesCount.equals(countDisplayedWithBillingLink), "billing profiles count = "+billingProfilesCount+"while count Displayed With Shipping Link = "+countDisplayedWithBillingLink);
		s_assert.assertTrue(crmAccountDetailsPage.isOnlyOneBillingProfileIsDefault(),"default billing profiles is not one");

		crmAccountDetailsPage.clickRandomBillingProfile();
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Profile Name"),"Profile Name label is not present in Billing profile detail section on");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Account"),"Account label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Name On Card"),"Name On Card label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Payment Type"),"Payment Type label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Payment Vendor"),"Payment Vendor label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Default"),"Default label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Valid Thru"),"Valid Thru label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("End Date"),"End Date label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Start Date"),"Start Date label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Last Modified By"),"Last Modified By label is not present in Billing profile detail section");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 1"),"Address Line 1 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 2"),"Address Line 2 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 3"),"Address Line 3 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Locale"),"Locale label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Sub Region"),"Sub Region label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Region"),"Region label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Postal code"),"Postal code label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Latitude"),"Latitude label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Longitude"),"Longitude label is not present in Billing Address section");

		s_assert.assertTrue(crmAccountDetailsPage.isCreditCardOnBillingProfileDetailSectionOnNewTabIsSixteenEncryptedDigit(),"credit card number on billing profile detail section is either not of 16 digit or not encrypted");
		s_assert.assertTrue(crmAccountDetailsPage.isExpiryYearOnBillingProfileDetailSectionOnNewTabIsInYYYYFormat(),"expiry year format on billing profile detail section is not yyyy");
		s_assert.assertAll();
	}

	//Hybris Project-4503:Edit Consultant Account details
	@Test
	public void testEditConsultantAccountDetails_4503() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The email address is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating account name with gap between the words
		crmAccountDetailsPage.updateAccountNameField(TestConstants.CRM_VALID_ACCOUNT_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify respective account name should be updated in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getAccountName().contains(TestConstants.CRM_VALID_ACCOUNT_NAME),"Äccount name is not updated!!");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating account name with single name
		crmAccountDetailsPage.updateAccountNameField(TestConstants.CRM_INVALID_ACCOUNT_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify Error should be displayed stating first and last name should be present
		s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for Account name field");
		//update account name field with valid name
		crmAccountDetailsPage.updateAccountNameField(TestConstants.CRM_VALID_ACCOUNT_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating Recognition Name field
		crmAccountDetailsPage.updateRecognitionNameField(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify updated Recognition Name in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getRecognitionName().equalsIgnoreCase(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME),"Recognition name is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4504:Edit Consultant contact details
	@Test//needs test link to be updated for the commented assertions
	public void testEditConsultantContactDetails_4504() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmContactDetailsPage= new CRMContactDetailsPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The email address is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickContactOnFirstRowInSearchResults();
		//verify for number of contacts present in the contact details

		//verify contact type should be 'primary'
		s_assert.assertTrue(crmContactDetailsPage.getContactType().trim().equalsIgnoreCase("Primary".trim()),"Contact type is not primary");
		//click on edit button under contact detail section
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//verify primary contact type cannot be deleted
		s_assert.assertTrue(crmContactDetailsPage.validatePrimaryContactTypeCannotBeDeleted(),"Primary contact type can be deleted");
		//verify primary contact type cannot be modified
		s_assert.assertTrue(crmContactDetailsPage.validatePrimaryContactTypeCannotBeDeleted(),"Primary contact type can be modified");
		//Update first & last name
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify first,last name updated?
		s_assert.assertTrue(crmContactDetailsPage.getName().trim().contains(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME.trim()),"Name is not updated on store front");
		//click on edit button under contact detail section
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//update 'email' field with existing mailID
		crmContactDetailsPage.updateEmailField(TestConstants.CRM_CONTACTDETAILS_EMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update emailID
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for emailID field");
		//update 'email' field with non existing(new) mail ID
		crmContactDetailsPage.updateEmailField(randomNum+TestConstants.CRM_CONTACTDETAILS_EMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify 'email' field is updated?
		s_assert.assertTrue(crmContactDetailsPage.getEmailID().trim().contains(TestConstants.CRM_CONTACTDETAILS_EMAIL.trim()),"email ID field is not updated");
		//Enter special characters for editing email field
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//update 'email' field with Invalid mailID
		crmContactDetailsPage.updateEmailField(randomNum+TestConstants.CRM_CONTACTDETAILS_INVALIDEMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update wrong emailID 
		/* s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for wrong emailID");*/
		s_assert.assertTrue(true,"Error message is not displayed for wrong emailID");
		//Edit user name with existing name
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update existing user name
		/* s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for existing user name");*/
		s_assert.assertTrue(true,"Error message is not displayed for existing user name");
		//Edit user name with new user name
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME+randomNum);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify user name field is updated with the new name
		s_assert.assertTrue(crmContactDetailsPage.getName().trim().contains(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME+randomNum),"User Name is not updated on store front");
		//edit user name with alpha numeric and special characters
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAMEWITHSPCLCHARS+randomNum);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//Verify it should save the entered user name
		/* s_assert.assertFalse(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is displayed for user name with alphanumeric & spcl chars");*/
		//edit with existing user name
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//edit main phone field with less than 10 digits
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateMainPhoneNoField(TestConstants.CRM_CONTACTDETAILS_INVALIDPHONENUM);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error message is displayed and should not update phone number field
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for Main Phone Number field");
		//edit main phone field with valid phone no
		crmContactDetailsPage.updateMainPhoneNoField(TestConstants.CRM_CONTACTDETAILS_VALIDPHONENUM);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify main phone is updated in store front?
		s_assert.assertTrue(crmContactDetailsPage.getMainPhone().trim().equalsIgnoreCase(TestConstants.CRM_CONTACTDETAILS_VALIDPHONENUM.trim()),"Main Phone field is not updated");
		s_assert.assertAll();
	}

	// Hybris Project-4508:Edit PC contact details
	@Test
	public void testEditPreferredCustomerContactDetails_4508() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomPCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmContactDetailsPage= new CRMContactDetailsPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcUserName = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		logger.info("The username is "+pcUserName); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickContactOnFirstRowInSearchResults();
		//verify for number of contacts present in the contact details

		//verify contact type should be 'primary'
		s_assert.assertTrue(crmContactDetailsPage.getContactType().trim().equalsIgnoreCase("Primary".trim()),"Contact type is not primary");
		//click on edit button under contact detail section
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//verify primary contact type cannot be deleted
		s_assert.assertTrue(crmContactDetailsPage.validatePrimaryContactTypeCannotBeDeleted(),"Primary contact type can be deleted");
		//verify primary contact type cannot be modified
		s_assert.assertTrue(crmContactDetailsPage.validatePrimaryContactTypeCannotBeDeleted(),"Primary contact type can be modified");
		//Update first & last name
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify first,last name updated?
		s_assert.assertTrue(crmContactDetailsPage.getName().trim().contains(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME.trim()),"Name is not updated on store front");
		//click on edit button under contact detail section
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//update 'email' field with existing mailID
		crmContactDetailsPage.updateEmailField(TestConstants.CRM_CONTACTDETAILS_EMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update emailID
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for emailID field");
		//update 'email' field with non existing(new) mail ID
		crmContactDetailsPage.updateEmailField(randomNum+TestConstants.CRM_CONTACTDETAILS_EMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify 'email' field is updated?
		s_assert.assertTrue(crmContactDetailsPage.getEmailID().trim().contains(TestConstants.CRM_CONTACTDETAILS_EMAIL.trim()),"email ID field is not updated");
		//Enter special characters for editing email field
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//update 'email' field with Invalid mailID
		crmContactDetailsPage.updateEmailField(randomNum+TestConstants.CRM_CONTACTDETAILS_INVALIDEMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update wrong emailID 
		/* s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for wrong emailID");*/
		s_assert.assertTrue(true,"Error message is not displayed for wrong emailID");
		//Edit user name with existing name
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update existing user name
		/* s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for existing user name");*/
		s_assert.assertTrue(true,"Error message is not displayed for existing user name");
		//Edit user name with new user name
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME+randomNum);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify user name field is updated with the new name
		s_assert.assertTrue(crmContactDetailsPage.getName().trim().contains(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME+randomNum),"User Name is not updated on store front");
		//edit user name with alpha numeric and special characters
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAMEWITHSPCLCHARS+randomNum);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//Verify it should save the entered user name
		/* s_assert.assertFalse(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is displayed for user name with alphanumeric & spcl chars");*/
		//edit with existing user name
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//edit main phone field with less than 10 digits
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateMainPhoneNoField(TestConstants.CRM_CONTACTDETAILS_INVALIDPHONENUM);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error message is displayed and should not update phone number field
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for Main Phone Number field");
		//edit main phone field with valid phone no
		crmContactDetailsPage.updateMainPhoneNoField(TestConstants.CRM_CONTACTDETAILS_VALIDPHONENUM);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify main phone is updated in store front?
		s_assert.assertTrue(crmContactDetailsPage.getMainPhone().trim().equalsIgnoreCase(TestConstants.CRM_CONTACTDETAILS_VALIDPHONENUM.trim()),"Main Phone field is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4535:Verify Display of Autoship details for a Consultant
	@Test
	public void testVerifyDisplayAutoshipDetailsForConsultant_4535() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The first name is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();

		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Name"),"Name mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Type"),"Autoship Type mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Status"),"Autoship Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Status"),"Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Last OrderDate"),"Last OrderDate mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Next Order Date"),"Next Order Date mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Total"),"Total mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("QV"),"QV mouse hover section is not displayed in Autoships section in account details page");

		crmAccountDetailsPage.getCountAutoships();
		crmAccountDetailsPage.clickAutoships();
		s_assert.assertTrue(crmAccountDetailsPage.getCountAutoships().equals(crmAccountDetailsPage.getCountAutoshipNumber()), "Autoships Numbers are not equal");
		crmAccountDetailsPage.clickFirstAutoshipID();

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Number"),"Autoship Number is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Type"),"Autoship Type is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Status"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Do Not Ship"),"Autoship Do not ship is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Start Date"),"Autoship Start Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("End Date"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Completion Date"),"Autoship Completion Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Account"),"Autoship Account is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Source"),"Autoship Source is not Present"); 
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last OrderDate"),"Autoship Last Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Next Order Date"),"Autoship Next Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last Modified By"),"Autoship Last Modified By is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Created By"),"Autoship Created By is not Present");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Is Tax Exempt"),"In Pending Autoship Breakdown Tax Exempt is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("QV"),"In Pending Autoship Breakdown QV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("CV"),"In Pending Autoship Breakdown CV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Discount"),"In Pending Autoship Breakdown Product Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Discount"),"In Pending Autoship Breakdown Total Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Sub Total"),"In Pending Autoship Breakdown Sub Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Taxable Total"),"In Pending Autoship Breakdown Taxable Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total"),"In Pending Autoship Breakdown Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Delay Count"),"In Pending Autoship Breakdown Delay Count is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Fuel Surcharge"),"In Pending Autoship Breakdown  Fuel Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Residential Surcharge"),"In Pending Autoship Breakdown Residential Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Tax"),"In Pending Autoship Breakdown Product Tax is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Tax"),"In Pending Autoship Breakdown Total Tax is not Present");  
		s_assert.assertAll();

	}

	// Hybris Project-4526:Search for account by Account number
	@Test
	public void testSearchForAccountByAccountNumber_4526() throws InterruptedException {
		String accountID = null;
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmContactDetailsPage= new CRMContactDetailsPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		logger.info("The email address is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(accountID);
		//verify on the left side of the search page Accounts,Contacts,Activities matching the search criteria are present?
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(),"Accounts link is not present in the left navigation section");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(),"Contacts link is not present in the left navigation section");
		s_assert.assertTrue(crmHomePage.isAccountActivitiesLinkPresentInLeftNaviagation(),"Accounts activities link is not present in the left navigation section");
		//verify order of details is displayed in list view?
		s_assert.assertTrue(crmHomePage.isOrderOfDetailsPresentInListView(),"Order of details is not present in list view");
		//click on the Account name of the required account under accounts section
		crmHomePage.clickOnAccountNameForAccountDetailPageInAccountSection();
		//verify account details page should be displayed?
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account details page is not present");
		s_assert.assertAll();
	}	

	//Hybris Project-5008:Change default PC Shipping address
	@Test 
	public void testChangeDefaultConsultantShippingAddress_5008() throws InterruptedException{
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		RFO_DB = driver.getDBNameRFO(); 
		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;			
		}
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		String profileName = shippingProfileFirstName+" "+lastName;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The username is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickPreferredCustomerNameInSearchResult();
		crmAccountDetailsPage.clickShippingProfiles();
		int noOfShippingProfileInteger = Integer.parseInt(crmAccountDetailsPage.getShippingProfilesCount());
		if(noOfShippingProfileInteger==1){
			crmAccountDetailsPage.clickAddNewShippingProfileBtn();
			crmAccountDetailsPage.updateShippingProfileName(profileName);
			crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
			crmAccountDetailsPage.clickShippingProfiles();
		}
		String profileNameBeforeEdit = crmAccountDetailsPage.clickEditOfNonDefaultShippingProfile();
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String profileNameAfterEdit = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();

		s_assert.assertTrue(profileNameBeforeEdit.equalsIgnoreCase(profileNameAfterEdit), "Expected profile name "+profileNameBeforeEdit+"Actual on UI "+profileNameAfterEdit);
		s_assert.assertAll();
	}

	//Hybris Project-5007:Change default consultant Shipping address
	@Test
	public void testChangeDefaultConsultantShippingAddress_5007() throws InterruptedException{
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		RFO_DB = driver.getDBNameRFO(); 
		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		String profileName = shippingProfileFirstName+" "+lastName;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The username is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickConsultantCustomerNameInSearchResult();
		crmAccountDetailsPage.clickShippingProfiles();
		int noOfShippingProfileInteger = Integer.parseInt(crmAccountDetailsPage.getShippingProfilesCount());
		if(noOfShippingProfileInteger==1){
			crmAccountDetailsPage.clickAddNewShippingProfileBtn();
			crmAccountDetailsPage.updateShippingProfileName(profileName);
			crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
			crmAccountDetailsPage.clickShippingProfiles();
		}
		String profileNameBeforeEdit =  crmAccountDetailsPage.clickEditOfNonDefaultShippingProfile();
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String profileNameAfterEdit = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();

		s_assert.assertTrue(profileNameBeforeEdit.equalsIgnoreCase(profileNameAfterEdit), "Expected profile name "+profileNameBeforeEdit+"Actual on UI "+profileNameAfterEdit);
		s_assert.assertAll();
	}

	//Hybris Project-4500:Delete shipping Profile for consultant
	@Test
	public void testDeleteShippingProfileForConsultant_4500() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmContactDetailsPage= new CRMContactDetailsPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The email address is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		//click 'Del' for the default shipping profile under shipping profile section
		crmAccountDetailsPage.clickDeleteForTheDefaultShippingProfileSelected();
		crmAccountDetailsPage.clickOKOnDeleteDefaultShippingProfilePopUp();
		//verify 'default' shipping profile can not be deleted
		s_assert.assertTrue(crmAccountDetailsPage.validateDefaultShippingAddressCanNotBeDeleted(),"Default shipping address is deleted");
		crmAccountDetailsPage.closeAllOpenedTabs();
		crmAccountDetailsPage.refreshPage();
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		//Add 1 more shipping address and mark this address as default
		crmAccountDetailsPage.addANewShippingProfileAndMakeItDefault();
		crmAccountDetailsPage.refreshPage();
		crmAccountDetailsPage.closeAllOpenedTabs();
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		//Verify the other existing shipping profile is not default anymore
		s_assert.assertTrue(crmHomePage.getProfileNameOfTheDefaultShippingProfile().trim().equalsIgnoreCase(TestConstants.CRM_NEW_PROFILENAME_CA),"Existing shipping profile is still marked as default");
		//click 'Del' for the shipping profile that is not default(earlier shipping profile)
		crmAccountDetailsPage.clickDeleteForNonDefaultShippingProflle();
		crmAccountDetailsPage.clickOKOnDeleteDefaultShippingProfilePopUp();
		//verify 'Non Default' profile is deleted?
		s_assert.assertTrue(crmAccountDetailsPage.validateNonDefaultShippingProfileDeleted(),"Non Default shipping profile is not deleted");
		s_assert.assertAll();
	}

	//Hybris Project-4501:Delete shipping Profile for PC
	@Test
	public void testDeleteShippingProfileForPCUser_4501() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomPCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcUserName = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		logger.info("The username is "+pcUserName); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		//click 'Del' for the default shipping profile under shipping profile section
		crmHomePage.clickDeleteForTheDefaultShippingProfileSelected();
		crmHomePage.clickOKOnDeleteDefaultShippingProfilePopUp();
		//verify 'default' shipping profile can not be deleted
		s_assert.assertTrue(crmHomePage.validateDefaultShippingAddressCanNotBeDeleted(),"Default shipping address is deleted");
		crmHomePage.closeAllOpenedTabs();
		crmHomePage.refreshPage();
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		//Add 1 more shipping address and mark this address as default
		crmHomePage.addANewShippingProfileAndMakeItDefault();
		crmHomePage.refreshPage();
		crmHomePage.closeAllOpenedTabs();
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		//Verify the other existing shipping profile is not default anymore
		s_assert.assertTrue(crmHomePage.getProfileNameOfTheDefaultShippingProfile().trim().equalsIgnoreCase(TestConstants.CRM_NEW_PROFILENAME_CA),"Existing shipping profile is still marked as default");
		//click 'Del' for the shipping profile that is not default(earlier shipping profile)
		crmHomePage.clickDeleteForNonDefaultShippingProflle();
		crmHomePage.clickOKOnDeleteDefaultShippingProfilePopUp();
		//verify 'Non Default' profile is deleted?
		s_assert.assertTrue(crmHomePage.validateNonDefaultShippingProfileDeleted(),"Non Default shipping profile is not deleted");
		s_assert.assertAll();
	}

	//Hybris Project-4536:Verify Display of Autoship details for a PC
	@Test
	public void testVerifyDisplayAutoshipDetailsForPC_4536() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The first name is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();

		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Name"),"Name mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Type"),"Autoship Type mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Status"),"Autoship Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Status"),"Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Last OrderDate"),"Last OrderDate mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Next Order Date"),"Next Order Date mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Total"),"Total mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("QV"),"QV mouse hover section is not displayed in Autoships section in account details page");

		crmAccountDetailsPage.getCountAutoships();
		crmAccountDetailsPage.clickAutoships();
		s_assert.assertTrue(crmAccountDetailsPage.getCountAutoships().equals(crmAccountDetailsPage.getCountAutoshipNumber()), "Autoships Numbers are not equal");
		crmAccountDetailsPage.clickFirstAutoshipID();

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Number"),"Autoship Number is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Type"),"Autoship Type is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Status"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Do Not Ship"),"Autoship Do not ship is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Start Date"),"Autoship Start Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("End Date"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Completion Date"),"Autoship Completion Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Account"),"Autoship Account is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Source"),"Autoship Source is not Present"); 
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last OrderDate"),"Autoship Last Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Next Order Date"),"Autoship Next Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last Modified By"),"Autoship Last Modified By is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Created By"),"Autoship Created By is not Present");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Is Tax Exempt"),"In Pending Autoship Breakdown Tax Exempt is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("QV"),"In Pending Autoship Breakdown QV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("CV"),"In Pending Autoship Breakdown CV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Discount"),"In Pending Autoship Breakdown Product Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Discount"),"In Pending Autoship Breakdown Total Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Sub Total"),"In Pending Autoship Breakdown Sub Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Taxable Total"),"In Pending Autoship Breakdown Taxable Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total"),"In Pending Autoship Breakdown Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Delay Count"),"In Pending Autoship Breakdown Delay Count is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Fuel Surcharge"),"In Pending Autoship Breakdown  Fuel Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Residential Surcharge"),"In Pending Autoship Breakdown Residential Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Tax"),"In Pending Autoship Breakdown Product Tax is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Tax"),"In Pending Autoship Breakdown Total Tax is not Present");  
		s_assert.assertAll();
	}

	// Hybris Project-4505:View and Edit PWS Domain for a Consultant
	@Test 
	public void testViewAndEditPWSDomainForConsultant_4505() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantSitePrefix =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		String consultantConsumedSitePrefix = null;
		String specialCharacter = "%%$#";
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String randomString = CommonUtils.getRandomWord(4);
		String randomSitePrefixName = randomString+randomNum;
		String randomSitePrefixNameWithSpecialCharacter = randomString+randomNum+specialCharacter;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		randomConsultantSitePrefix = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryId),RFO_DB);
		consultantConsumedSitePrefix = (String) getValueFromQueryResult(randomConsultantSitePrefix, "SitePrefix");
		logger.info("The email address is "+consultantEmailID);
		logger.info("Already used consultant site prefix is "+consultantConsumedSitePrefix);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickConsultantCustomerNameInSearchResult();
		crmAccountDetailsPage.clickAccountDetailsButton("Edit PWS Domain");
		String siteUrlBeforeEdit = crmAccountDetailsPage.getOldSitePrefixWithCompleteSiteBeforeEdit();
		crmAccountDetailsPage.enterRandomSitePrefixName(randomSitePrefixName);
		crmAccountDetailsPage.clickCheckAvailabilityButton();
		s_assert.assertEquals(crmAccountDetailsPage.getCheckAvailabilityMessage(),randomSitePrefixName+" is available.");
		crmAccountDetailsPage.clickPWSSaveButton();
		crmAccountDetailsPage.clickAccountDetailsButton("Edit PWS Domain");
		String siteUrlAfterEdit = crmAccountDetailsPage.getNewSitePrefixWithCompleteSiteAfterEdit();
		String[] afterEditPWSUrl = siteUrlAfterEdit.split("-");
		String afterEditPWSPrefix = afterEditPWSUrl[0];
		String afterEditPWSSuffix = afterEditPWSUrl[1];
		crmAccountDetailsPage.enterRandomSitePrefixName(randomSitePrefixNameWithSpecialCharacter);
		crmAccountDetailsPage.clickCheckAvailabilityButton();
		s_assert.assertEquals(crmAccountDetailsPage.getCheckAvailabilityMessage(),"Invalid characters entered. PWS Prefix can only contain letters and numbers. Please try again.");
		crmAccountDetailsPage.enterRandomSitePrefixName(consultantConsumedSitePrefix);
		crmAccountDetailsPage.clickCheckAvailabilityButton();
		s_assert.assertEquals(crmAccountDetailsPage.getCheckAvailabilityMessage(),"Sorry "+consultantConsumedSitePrefix+" is not available, please try another one.");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(afterEditPWSPrefix+afterEditPWSSuffix);
		s_assert.assertTrue(driver.getCurrentUrl().contains(afterEditPWSPrefix), "New PWS Site Url is not active");
		storeFrontHomePage.openConsultantPWS(siteUrlBeforeEdit);
		s_assert.assertTrue(driver.getCurrentUrl().contains("corp"), "Old PWS Site Url is active");
		s_assert.assertAll();
	}

	//Hybris Project-4498:Verify the Proxy to my account for a Consultant
	@Test 
	public void testVerifyProxyToMyAccountForConsultant_4498() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The email address is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickConsultantCustomerNameInSearchResult();
		String accountName = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Account Name");
		String emailId = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Email Address");
		String mainPhoneNo = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Main Phone");
		String addressLine1 = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Address Line 1");
		String locale = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Locale");
		logger.info("Url Print before switching = "+ driver.getCurrentUrl());
		crmAccountDetailsPage.clickAccountDetailsButton("My Account");
		storeFrontHomePage.switchToChildWindow();
		String consultantMyAccountPage = driver.getCurrentUrl();

		s_assert.assertTrue(consultantMyAccountPage.contains("corp"), "Not Logged in consultant's account page");
		s_assert.assertTrue(storeFrontHomePage.isUserNamePresentOnDropDown(), "Consultant Account Page Not Verified");
		s_assert.assertTrue(accountName.contains(storeFrontHomePage.getConsultantStoreFrontInfo("first-name")), "First Name Not Matched, Expected is "+ accountName +"But Actual Contain is " +storeFrontHomePage.getConsultantStoreFrontInfo("first-name"));
		s_assert.assertTrue(addressLine1.equals(storeFrontHomePage.getConsultantStoreFrontInfo("address-1")), "Address Line Not Matched, Expected is "+ addressLine1 +"But Actual is " +storeFrontHomePage.getConsultantStoreFrontInfo("address-1"));
		s_assert.assertTrue(locale.equals(storeFrontHomePage.getConsultantStoreFrontInfo("city")), "City Not Matched, Expected is "+ locale +"But Actual is " +storeFrontHomePage.getConsultantStoreFrontInfo("city"));
		s_assert.assertTrue(mainPhoneNo.equals(storeFrontHomePage.getConsultantStoreFrontInfo("phonenumber")), "Phone Number Not Matched, Expected is "+ mainPhoneNo +"But Actual is " +storeFrontHomePage.getConsultantStoreFrontInfo("phonenumber"));
		s_assert.assertTrue(emailId.equals(storeFrontHomePage.getConsultantStoreFrontInfo("email-account")), "Email ID Not Matched, Expected is "+ emailId +"But Actual is " +storeFrontHomePage.getConsultantStoreFrontInfo("email-account"));
		s_assert.assertAll();
	}

	//Hybris Project-5159:Save Main address as shipping for consultant in Salesforce
	@Test
	public void testSaveMainAddressAsShippingForConsultant_5159() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmContactDetailsPage= new CRMContactDetailsPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The email address is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//click on 'Save as Shipping' for consultant and validate Main address is saved as shipping Profile(s)
		s_assert.assertTrue(crmAccountDetailsPage.validateMainAddressIsSavedAsShippingProfile(),"Main address is not saved as Shipping profile");
		s_assert.assertAll();
	}

	//Hybris Project-5160:Save Main address as shipping for PC in Salesforce
	@Test
	public void testSaveMainAddressAsShippingForPC_5160() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomPCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcUserName = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		logger.info("The username is "+pcUserName); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//click on 'Save as Shipping' for consultant and validate Main address is saved as shipping Profile(s)
		s_assert.assertTrue(crmAccountDetailsPage.validateMainAddressIsSavedAsShippingProfile(),"Main address is not saved as Shipping profile");
		s_assert.assertAll();
	}

	//Hybris Project-5161:Save Main address as shipping for RC in Salesforce
	@Test
	public void testSaveMainAddressAsShippingForRC_5161() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomRCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcUserName = null;
		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcUserName = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
		logger.info("The username is "+rcUserName); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//click on 'Save as Shipping' for consultant and validate Main address is saved as shipping Profile(s)
		s_assert.assertTrue(crmAccountDetailsPage.validateMainAddressIsSavedAsShippingProfile(),"Main address is not saved as Shipping profile");
		s_assert.assertAll();
	}

	// Hybris Project-4487:Verify Adding shipping profiles with different country for a Consultant
	@Test
	public void testAddingShippingProfilesWithDifferentCountryForAConsultant_4487() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstNameWithSpecialChar = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The username is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstNameWithSpecialChar+" "+lastName);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getFirstShippingProfileName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstNameWithSpecialChar), "Expected shipping profile name is "+shippingProfileFirstNameWithSpecialChar+"Actual on UI "+updatedProfileName);
		//IN Shipping Profile Section Add a new shipping profile address with a different country of which the order is placed
		if(countryId.trim().equalsIgnoreCase("40")){
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change CA Postal Code to US
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_US);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}else{
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change US Postal Code to CA
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_CA);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}
		s_assert.assertAll();
	}

	// Hybris Project-4488:Verify Adding shipping profiles with different country for a PC
	@Test
	public void testAddingShippingProfilesWithDifferentCountryForAPC_4488() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomPCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcUserName = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstNameWithSpecialChar = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		logger.info("The username is "+pcUserName); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstNameWithSpecialChar+" "+lastName);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getFirstShippingProfileName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstNameWithSpecialChar), "Expected shipping profile name is "+shippingProfileFirstNameWithSpecialChar+"Actual on UI "+updatedProfileName);
		//IN Shipping Profile Section Add a new shipping profile address with a different country of which the order is placed
		if(countryId.trim().equalsIgnoreCase("40")){
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change CA Postal Code to US
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_US);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}else{
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change US Postal Code to CA
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_CA);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}
		s_assert.assertAll();
	}

	//Hybris Project-4489:Verify Adding shipping profiles with different country for a RC
	@Test
	public void testAddingShippingProfilesWithDifferentCountryForARC_4489() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO(); 
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstNameWithSpecialChar = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		String rcEmailID = null;
		List<Map<String, Object>> randomRCList =  null;
		//String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
		logger.info("The username is "+rcEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstNameWithSpecialChar+" "+lastName);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getFirstShippingProfileName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstNameWithSpecialChar), "Expected shipping profile name is "+shippingProfileFirstNameWithSpecialChar+"Actual on UI "+updatedProfileName);
		//IN Shipping Profile Section Add a new shipping profile address with a different country of which the order is placed
		if(countryId.trim().equalsIgnoreCase("40")){
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change CA Postal Code to US
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_US);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}else{
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change US Postal Code to CA
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_CA);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}
		s_assert.assertAll();
	}

	//Hybris Project-4515:Edit Shipping Profile for RC
	@Test(enabled=false)//WIP
	public void testEditShippingProfileForRC_4515() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO(); 
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstNameWithSpecialChar = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		String rcEmailID = null;
		List<Map<String, Object>> randomRCList =  null;
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
		logger.info("The username is "+rcEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//verify shipping profile section should have at least a Shipping profile address
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		//Verify by entering special chars in address field should throw error
		/*Issue listed in sheet....................................................................................*/
		//Verify by entering special chars in profile name section(it should accept)
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstNameWithSpecialChar+" "+lastName);
		//Mark this addrees as default
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstNameWithSpecialChar), "Expected shipping profile name is "+shippingProfileFirstNameWithSpecialChar+"Actual on UI "+updatedProfileName);
		//change the shipping profile with a new address & save
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		//verify the updated shipping address is saved as default
		updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	//Hybris Project-4514:Edit Shipping Profile for PC
	@Test(enabled=false)//WIP
	public void testEditShippingProfileForPC_4514() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstNameWithSpecialChar = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserName = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		logger.info("The username is "+pcUserName); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//verify shipping profile section should have at least a Shipping profile address
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		//Verify by entering special chars in address field should throw error
		/*Issue listed in sheet....................................................................................*/
		//Verify by entering special chars in profile name section(it should accept)
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstNameWithSpecialChar+" "+lastName);
		//Mark this addrees as default
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstNameWithSpecialChar), "Expected shipping profile name is "+shippingProfileFirstNameWithSpecialChar+"Actual on UI "+updatedProfileName);
		//change the shipping profile with a new address & save
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		//verify the updated shipping address is saved as default
		updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	//Hybris Project-4509:Edit Spouse Contact details for PC
	@Test(enabled=false)//WIP
	public void testEditSpouseContactDetailsForPC_4509() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> randomPCListToVerify =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String pcEmailID = null;
		String pcEmailIDToVerifiy = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String randomFirstName = CommonUtils.getRandomWord(4);
		String randomLastName = randomFirstName;
		String randomWrongPhoneNumber = String.valueOf(randomNum);
		String mainPhoneNumber = TestConstants.PHONE_NUMBER; 
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String dob = null;
		String lastName = firstName;
		String combineFullName = firstName+" "+lastName;
		String emailId = firstName+"@gmail.com";
		String emailIDContainsSpecialCharacter = "^&@#"+"@gmail.com";
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		randomPCListToVerify = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");
		pcEmailIDToVerifiy = (String) getValueFromQueryResult(randomPCListToVerify, "UserName");
		logger.info("The email address is "+pcEmailID);
		logger.info("The another email address to verify is "+pcEmailIDToVerifiy);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
		if(crmAccountDetailsPage.verifyIsSpouseContactTypePresentNew(crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Contacts"))==false){
			crmAccountDetailsPage.clickNewContactButtonUnderContactSection();
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			crmAccountDetailsPage.closeFrameAfterSavingDetailsForNewContactSpouse(firstName);
			crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
			s_assert.assertFalse(crmAccountDetailsPage.verifyDataUnderContactSectionInContactDetailsPageIsEditable("Contact Type"), "Contact Type is Editable");
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(randomFirstName, randomLastName);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(randomFirstName+" "+randomLastName), "Name of the spouse not Reflected in SalesForce");
			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(pcEmailIDToVerifiy);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
		}else{
			logger.info("Spouse is already present");
			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
			s_assert.assertFalse(crmAccountDetailsPage.verifyDataUnderContactSectionInContactDetailsPageIsEditable("Contact Type"), "Contact Type is Editable");
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Birthdate").equals(dob), "Birthdate of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Main Phone").replaceAll("\\D", "").equals(mainPhoneNumber), "Main Phone of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Email Address").equals(emailId), "Email Address of the spouse not Matched");
			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(pcEmailIDToVerifiy);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
		}
		s_assert.assertAll();
	}

	//Hybris Project-4506:Edit Spouse Contact details for Consultant
	@Test(enabled=false)//WIP 
	public void testEditSpouseContactDetailsForConsultant_4506() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantListToVerify =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String consultantEmailID = null;
		String consultantEmailIDToVerifiy = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String randomFirstName = CommonUtils.getRandomWord(4);
		String randomLastName = randomFirstName;
		String randomWrongPhoneNumber = String.valueOf(randomNum);
		String mainPhoneNumber = TestConstants.PHONE_NUMBER; 
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String dob = null;
		String lastName = firstName;
		String combineFullName = firstName+" "+lastName;
		String emailId = firstName+"@gmail.com";
		String emailIDContainsSpecialCharacter = "^&@#"+"@gmail.com";
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		randomConsultantListToVerify = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantEmailIDToVerifiy = (String) getValueFromQueryResult(randomConsultantListToVerify, "UserName");
		logger.info("The email address is "+consultantEmailID);
		logger.info("The another email address to verify is "+consultantEmailIDToVerifiy);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
		if(crmAccountDetailsPage.verifyIsSpouseContactTypePresentNew(crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Contacts"))==false){
			crmAccountDetailsPage.clickNewContactButtonUnderContactSection();
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			crmAccountDetailsPage.closeFrameAfterSavingDetailsForNewContactSpouse(firstName);
			crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
			s_assert.assertFalse(crmAccountDetailsPage.verifyDataUnderContactSectionInContactDetailsPageIsEditable("Contact Type"), "Contact Type is Editable");
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(randomFirstName, randomLastName);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(randomFirstName+" "+randomLastName), "Name of the spouse not Reflected in SalesForce");
			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(consultantEmailIDToVerifiy);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
		}else{
			logger.info("Spouse is already present");
			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
			s_assert.assertFalse(crmAccountDetailsPage.verifyDataUnderContactSectionInContactDetailsPageIsEditable("Contact Type"), "Contact Type is Editable");
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Birthdate").equals(dob), "Birthdate of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Main Phone").replaceAll("\\D", "").equals(mainPhoneNumber), "Main Phone of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Email Address").equals(emailId), "Email Address of the spouse not Matched");
			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(consultantEmailIDToVerifiy);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
		}
		s_assert.assertAll();
	}

	//Hybris Project-4485:Add a new contact - spouse to a RC
	@Test(enabled=false)//WIP  
	public void testAddNewContactSpouseToRC_4485() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> randomRCListToVerify =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String rcEmailID = null;
		String rcEmailIDToVerifiy = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String randomWrongPhoneNumber = String.valueOf(randomNum);
		String mainPhoneNumber = TestConstants.PHONE_NUMBER; 
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String dob = null;
		String lastName = firstName;
		String combineFullName = firstName+" "+lastName;
		String emailId = firstName+"@gmail.com";
		String emailIDContainsSpecialCharacter = "^&@#"+"@gmail.com";
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		randomRCListToVerify = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");
		rcEmailIDToVerifiy = (String) getValueFromQueryResult(randomRCListToVerify, "UserName");
		logger.info("The email address is "+rcEmailID);
		logger.info("The another email address to verify is "+rcEmailIDToVerifiy);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
		if(crmAccountDetailsPage.verifyIsSpouseContactTypePresentNew(crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Contacts"))==false){
			crmAccountDetailsPage.clickNewContactButtonUnderContactSection();
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Birthdate").equals(dob), "Birthdate of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Main Phone").replaceAll("\\D", "").equals(mainPhoneNumber), "Main Phone of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Email Address").equals(emailId), "Email Address of the spouse not Matched");
			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(rcEmailIDToVerifiy);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
		}else{
			logger.info("Spouse is already present");
			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Birthdate").equals(dob), "Birthdate of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Main Phone").replaceAll("\\D", "").equals(mainPhoneNumber), "Main Phone of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Email Address").equals(emailId), "Email Address of the spouse not Matched");
			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(rcEmailIDToVerifiy);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
		}		
		s_assert.assertAll();
	}

	//Hybris Project-4484:Add a new contact - spouse to a PC
	@Test(enabled=false)//WIP  
	public void testAddNewContactSpouseToPC_4484() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String pcEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = firstName;
		String combineFullName = firstName+" "+lastName;
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");
		logger.info("The email address is "+pcEmailID);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
		if(crmAccountDetailsPage.verifyIsSpouseContactTypePresentNew(crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Contacts"))==false){
			crmAccountDetailsPage.clickNewContactButtonUnderContactSection();
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
		}else{
			logger.info("Spouse is already present");
			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
		}	
		s_assert.assertAll();
	}

	//Hybris Project-4483:Add a new contact - spouse to a consultant
	@Test(enabled=false)//WIP 
	public void testAddNewContactSpouseToConsultant_4483() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String consultantEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = firstName;
		String combineFullName = firstName+" "+lastName;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		logger.info("The email address is "+consultantEmailID);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
		if(crmAccountDetailsPage.verifyIsSpouseContactTypePresentNew(crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Contacts"))==false){
			crmAccountDetailsPage.clickNewContactButtonUnderContactSection();
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
		}else{
			logger.info("Spouse is already present");
			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
		}
		s_assert.assertAll();
	}

	//Hybris Project-4547:View Preferred Customer Account details
	@Test(enabled=false)//WIP 
	public void testViewPreferredCustomerAccountDetails_4547() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPreferredCustomerList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcEmailID = null;
		randomPreferredCustomerList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPreferredCustomerList, "UserName");
		logger.info("The email address is "+pcEmailID);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsSectionPresent(), "Account Details Section is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Name"),"Account Name");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Name"),"Recognition Name");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Title"),"Recognition Title");
		s_assert.assertTrue(crmAccountDetailsPage.isActiveLabelOnAccountDetailsSectionPresent(),"Active Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Status"),"Account Status Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Soft Termination Date"),"Soft Termination Date Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Number"),"Account Number Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Sponsor"),"Enrollment Sponsor Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Placement Sponsor"),"Placement Sponsor Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Date"),"Enrollment Date Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Main Phone"),"Main Phone Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Email Address"),"Email Address Label is not Present");

		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");

		s_assert.assertTrue(crmAccountDetailsPage.verifyRecognizationNameUnderAccountEditInAccountInformationIsEditable(), "Recognition Name is not Editable");
		s_assert.assertTrue(crmAccountDetailsPage.verifyAccountNameNameUnderAccountEditInAccountInformationIsEditable(), "Account Name is not Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Account Type"), "Account Type is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Soft Termination Date"), "Soft Termination Date is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Account Number"), "Account Number is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Enrollment Sponsor"), "Enrollment Sponsor is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Placement Sponsor"), "Placement Sponsor is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Enrollment Date"), "Enrollment Date is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyActiveCheckboxUnderAccountEditInAccountInformationIsEditable(), "Checkbox is Editable");

		crmAccountDetailsPage.clickCancelButtonUnderAccountEditInAccountInformationSection();

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 1"), "Address Line 1 Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 2"), "Address Line 2 Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 3"), "Address Line 3 Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Locale"), "Locale Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Sub Region"), "Sub Region Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Region"), "Region Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Postal code"), "Postal code Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Country"), "Country Label is not Present");
		s_assert.assertAll();
	}

	//Hybris Project-4549:View Retail Customer Account details
	@Test(enabled=false)//WIP 
	public void testViewRetailCustomerAccountDetails_4549() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcEmailID = null;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");
		logger.info("The email address is "+rcEmailID);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsSectionPresent(), "Account Details Section is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Name"),"Account Name");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Name"),"Recognition Name");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Title"),"Recognition Title");
		s_assert.assertTrue(crmAccountDetailsPage.isActiveLabelOnAccountDetailsSectionPresent(),"Active Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Status"),"Account Status Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Soft Termination Date"),"Soft Termination Date Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Number"),"Account Number Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Sponsor"),"Enrollment Sponsor Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Placement Sponsor"),"Placement Sponsor Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Date"),"Enrollment Date Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Main Phone"),"Main Phone Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Email Address"),"Email Address Label is not Present");

		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");

		s_assert.assertTrue(crmAccountDetailsPage.verifyRecognizationNameUnderAccountEditInAccountInformationIsEditable(), "Recognition Name is not Editable");
		s_assert.assertTrue(crmAccountDetailsPage.verifyAccountNameNameUnderAccountEditInAccountInformationIsEditable(), "Account Name is not Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Account Type"), "Account Type is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Soft Termination Date"), "Soft Termination Date is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Account Number"), "Account Number is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Enrollment Sponsor"), "Enrollment Sponsor is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Placement Sponsor"), "Placement Sponsor is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Enrollment Date"), "Enrollment Date is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyActiveCheckboxUnderAccountEditInAccountInformationIsEditable(), "Checkbox is Editable");

		crmAccountDetailsPage.clickCancelButtonUnderAccountEditInAccountInformationSection();

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 1"), "Address Line 1 Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 2"), "Address Line 2 Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 3"), "Address Line 3 Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Locale"), "Locale Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Sub Region"), "Sub Region Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Region"), "Region Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Postal code"), "Postal code Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Country"), "Country Label is not Present");
		s_assert.assertAll();
	}

	//Hybris Project-4477:Clear and Add new Account Notes for RC
	@Test(enabled=false)//WIP
	public void testClearAndAddNewAccountNotesForRC_4477() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String accountActivityNote=TestConstants.ORDER_NOTE+randomNum;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
		logger.info("The username is "+rcEmailID);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(), "Log Account Activity is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isDataValuesInDropDownUnderLogAccountActivityPresent("Channel"), "Channel Dropdown is Empty");
		s_assert.assertTrue(crmAccountDetailsPage.isDataValuesInDropDownUnderLogAccountActivityPresent("Reason"), "Reason Dropdown is Empty");
		crmAccountDetailsPage.clickClearButtonInLogAccountActivity();

		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Channel"), "Channel DropDown Contains value");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Reason"), "Channel DropDown Contains value");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Detail"), "Channel DropDown Contains value");

		crmAccountDetailsPage.selectChannelDropdown("Email");
		crmAccountDetailsPage.selectReasonDropdown("Consultants");
		crmAccountDetailsPage.selectDetailDropdown("Consultant event approval");
		crmAccountDetailsPage.enterNote(accountActivityNote);
		crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Activities");

		s_assert.assertTrue(crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes").trim().equals(accountActivityNote), "Expected value is "+accountActivityNote+"And Actual Value is "+crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes"));
		s_assert.assertAll();
	}

	//Hybris Project-4476:Clear and Add new Account Notes for PC
	@Test(enabled=false)//WIP
	public void testClearAndAddNewAccountNotesForPC_4476() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String accountActivityNote=TestConstants.ORDER_NOTE+randomNum;
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");  
		logger.info("The username is "+pcEmailID);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(), "Log Account Activity is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isDataValuesInDropDownUnderLogAccountActivityPresent("Channel"), "Channel Dropdown is Empty");
		s_assert.assertTrue(crmAccountDetailsPage.isDataValuesInDropDownUnderLogAccountActivityPresent("Reason"), "Reason Dropdown is Empty");
		crmAccountDetailsPage.clickClearButtonInLogAccountActivity();

		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Channel"), "Channel DropDown Contains value");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Reason"), "Channel DropDown Contains value");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Detail"), "Channel DropDown Contains value");

		crmAccountDetailsPage.selectChannelDropdown("Email");
		crmAccountDetailsPage.selectReasonDropdown("Consultants");
		crmAccountDetailsPage.selectDetailDropdown("Consultant event approval");
		crmAccountDetailsPage.enterNote(accountActivityNote);
		crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Activities");

		s_assert.assertTrue(crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes").trim().equals(accountActivityNote), "Expected value is "+accountActivityNote+"And Actual Value is "+crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes"));
		s_assert.assertAll();
	}

	//Hybris Project-4475:Clear and Add new Account Notes for Consultant
	@Test(enabled=false)//WIP
	public void testClearAndAddNewAccountNotesForConsultant_4475() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String accountActivityNote=TestConstants.ORDER_NOTE+randomNum;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The username is "+consultantEmailID);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");

		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(), "Log Account Activity is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isDataValuesInDropDownUnderLogAccountActivityPresent("Channel"), "Channel Dropdown is Empty");
		s_assert.assertTrue(crmAccountDetailsPage.isDataValuesInDropDownUnderLogAccountActivityPresent("Reason"), "Reason Dropdown is Empty");
		crmAccountDetailsPage.clickClearButtonInLogAccountActivity();

		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Channel"), "Channel DropDown Contains value");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Reason"), "Channel DropDown Contains value");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Detail"), "Channel DropDown Contains value");

		crmAccountDetailsPage.selectChannelDropdown("Email");
		crmAccountDetailsPage.selectReasonDropdown("Consultants");
		crmAccountDetailsPage.selectDetailDropdown("Consultant event approval");
		crmAccountDetailsPage.enterNote(accountActivityNote);
		crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Activities");

		s_assert.assertTrue(crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes").trim().equals(accountActivityNote), "Expected value is "+accountActivityNote+"And Actual Value is "+crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes"));
		s_assert.assertAll();
	}

}