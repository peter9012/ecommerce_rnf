package com.rf.test.website.crm;

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

public class AccountDetailsTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AccountDetailsTest.class.getName());

	private CRMLoginPage crmLoginpage;
	private CRMHomePage crmHomePage;
	private CRMAccountDetailsPage crmAccountDetailsPage; 


	private String RFO_DB = null;


	//	// Hybris Project-4494:View the Account Policies for Consultant
	//	@Test
	//	public void testViewAccountPoliciesForConsultant_4494() throws InterruptedException{
	//		consultantEmailAddress="auto734005@xyz.com";
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUserById(consultantEmailAddress);
	//
	//	}
	//	// Hybris Project-4539:View Billing profile for a PC
	//	@Test//WIP
	//	public void testViewBillingProfileForPC_4539() throws InterruptedException{
	//		consultantEmailAddress="auto734005@xyz.com";
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUserById("pc");
	//		crmHomePage.clickOnAccountNameForAccountDetailPageInAccountSection();
	//		int countOfBillingProfile=crmHomePage.clickOnBillingProfileAndGetNumberBillingProfile();
	//		s_assert.assertTrue(crmHomePage.verifyBillingInfoActionField(),"Action field is not present");
	//		assertEquals(countOfBillingProfile,crmHomePage.getCountOfBillingProfileUnderBillingProfileSection(),"Billing Addresses count is not same");
	//		s_assert.assertAll();
	//
	//	}
	//
	//	@Test//WIP
	//	public void testViewBillingProfileForRC_4540() throws InterruptedException{
	//		consultantEmailAddress="auto92831796394@xyz.com";
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUserById("auto92831796394@xyz.com");
	//		crmHomePage.clickOnAccountNameForAccountDetailPageInAccountSection();
	//		int countOfBillingProfile=crmHomePage.clickOnBillingProfileAndGetNumberBillingProfile();
	//		s_assert.assertTrue(crmHomePage.verifyBillingInfoActionField(),"Action field is not present");
	//		assertEquals(countOfBillingProfile,crmHomePage.getCountOfBillingProfileUnderBillingProfileSection(),"Billing Addresses count is not same");
	//		s_assert.assertAll();
	//	}
	//	@Test 
	//	public void testviewShippingProfileForConsultant_4542()throws InterruptedException{
	//		consultantEmailAddress="auto459940@xyz.com";
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUserById("auto459940@xyz.com");
	//		crmHomePage.clickOnShippingProfileName();
	//		s_assert.assertAll();
	//
	//	}
	//	@Test 
	//	public void testviewShippingProfileForPC_4543()throws InterruptedException{
	//		consultantEmailAddress="auto459940@xyz.com";
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUserById("auto808408@xyz.com");
	//		crmHomePage.clickOnShippingProfileName();
	//		s_assert.assertAll();	
	//	}
	//
	
	
	//	@Test
	//	public void ConsultantDetailViewPage_4546() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();	
	//		List<Map<String, Object>> randomConsultantList =  null;
	//		String consultantEmailID = null;
	//		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
	//		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");	
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUserandSelect(consultantEmailID);
	//		crmHomePage.AccountDetailsPage();
	//		s_assert.assertAll();	
	//	}
	//	@Test
	//	public void PCDetailViewPage_4548() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();
	//		List<Map<String, Object>> randomPCUserList =  null;
	//		String pcUserEmailID = null;
	//		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
	//		System.out.println(randomPCUserList);
	//		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
	//		System.out.println(pcUserEmailID);
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUserandSelect(pcUserEmailID);
	//		crmHomePage.AccountDetailsPage();
	//		s_assert.assertAll();
	//	}
	//	@Test
	//	public void RCDetailViewPage_4550() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();
	//		List<Map<String, Object>> randomRCUserList =  null;
	//		String rcUserEmailID = null;
	//		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
	//		rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");	
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUserandSelect(rcUserEmailID);
	//		crmHomePage.AccountDetailsPage();
	//		s_assert.assertAll();
	//	}
	//
	//	@Test
	//	public void SearchForAccountByPartialName_4533() throws InterruptedException{
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		//s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUserandSelect("ala*");
	//		s_assert.assertAll();
	//	}
	//	@Test//WIP
	//	public void EditConsultantAccountDetails_4503() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();	
	//		List<Map<String, Object>> randomConsultantList =  null;
	//		String consultantEmailID = null;
	//		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
	//		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");	
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUser(consultantEmailID);
	//		crmHomePage.EditAccountDetails();
	//		crmHomePage.EnterNewLocale();
	//		crmHomePage.ClickSave();
	//		crmHomePage.GetUpdatedAddress();
	//		s_assert.assertAll();
	//	}
	//	@Test
	//	public void EditPCAccountDetails_4507() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();
	//		List<Map<String, Object>> randomPCUserList =  null;
	//		String pcUserEmailID = null;
	//		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
	//		System.out.println(randomPCUserList);
	//		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
	//		System.out.println(pcUserEmailID);
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUser(pcUserEmailID);
	//		crmHomePage.EditAccountDetails();
	//		crmHomePage.EnterNewLocale();
	//		crmHomePage.ClickSave();
	//		crmHomePage.GetUpdatedAddress();
	//		s_assert.assertAll();
	//	}
	//	@Test
	//	public void EditRCAccountDetails_4510() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();
	//		List<Map<String, Object>> randomRCUserList =  null;
	//		String rcUserEmailID = null;
	//		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
	//		rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.searchUser(rcUserEmailID);
	//		crmHomePage.EditAccountDetails();
	//		crmHomePage.EnterNewLocale();
	//		crmHomePage.ClickSave();
	//		crmHomePage.GetUpdatedAddress();
	//		s_assert.assertAll();
	//	}
	//	@Test
	//	public void EditShippingDetailsConsultant_4513() throws InterruptedException{
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.clickaccountNameForAccountDetailsPage("");
	//		crmHomePage.clickEditShipping();
	//		crmHomePage.ClickSave();
	//		crmHomePage.GetUpdatedAddress();
	//		s_assert.assertAll();
	//	}
	//	@Test
	//	public void EditShippingDetailsPC_4514() throws InterruptedException{
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.clickaccountNameForAccountDetailsPage("");
	//		crmHomePage.clickEditShipping();
	//		crmHomePage.ClickSave();
	//		crmHomePage.GetUpdatedAddress();
	//		s_assert.assertAll();
	//	}
	//	@Test
	//	public void EditShippingDetailsRC_4515() throws InterruptedException{
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.clickaccountNameForAccountDetailsPage("");
	//		crmHomePage.clickEditShipping();
	//		crmHomePage.ClickSave();
	//		crmHomePage.GetUpdatedAddress();
	//		s_assert.assertAll();
	//	}
	//	@Test
	//	public void ChangeAccountStatusFromActiveToInactive_4518() throws InterruptedException{
	//		driver.get(driver.getCrmURL());
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.clickaccountNameForAccountDetailsPage("");
	//
	//	}
}












