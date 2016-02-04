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
		logger.info("The first name is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.contains(consultantEmailID), "the email on first row which is = "+emailOnfirstRow+" is expected to contain email = "+consultantEmailID);
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
		s_assert.assertTrue(nameOnFirstRow.contains(firstName), "the name on first row which is = "+nameOnFirstRow+" is expected to contain firstname = "+firstName);
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isAccountActivitiesLinkPresentInLeftNaviagation(), "Accounts Activities link is not present on left navigation panel");

		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
		s_assert.assertAll();
	}

}
