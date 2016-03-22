package com.rf.test.website.crm.smoke;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.crm.CRMAccountDetailsPage;
import com.rf.pages.website.crm.CRMHomePage;
import com.rf.pages.website.crm.CRMLoginPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class CRMSmokeTest extends RFWebsiteBaseTest{

	private static final Logger logger = LogManager
			.getLogger(CRMSmokeTest.class.getName());

	private CRMLoginPage crmLoginpage;
	private CRMHomePage crmHomePage;
	private CRMAccountDetailsPage crmAccountDetailsPage; 

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

}
