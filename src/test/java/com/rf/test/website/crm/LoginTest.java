package com.rf.test.website.crm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.crm.CRMHomePage;
import com.rf.pages.website.crm.CRMLoginPage;
import com.rf.test.website.RFWebsiteBaseTest;
import com.rf.test.website.storeFront.dataMigration.rfl.accounts.AddBillingTest;

public class LoginTest extends RFWebsiteBaseTest {
	private static final Logger logger = LogManager
			.getLogger(LoginTest.class.getName());
	private CRMLoginPage crmLoginpage;
	private CRMHomePage crmHomePage;

	//Hybris Project-4719:Single sign on to CRM
	@Test
	public void singleSignInToCRM_4719() throws InterruptedException{
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_INVALID_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmLoginpage.getErrorMessageOnLoginPage().contains("check your username and password"),"Login is successful with invalid credential");
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.clickOnBackToServiceCloudConsole();
		s_assert.assertTrue(crmHomePage.verifySearchPage(),"Basic search Page has not been displayed");
		s_assert.assertAll();
		}

}
