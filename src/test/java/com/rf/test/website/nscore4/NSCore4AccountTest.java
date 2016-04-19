package com.rf.test.website.nscore4;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.pages.website.nscore.NSCore4HomePage;
import com.rf.pages.website.nscore.NSCore4LoginPage;
import com.rf.pages.website.nscore.NSCore4MobilePage;
import com.rf.test.website.RFNSCoreWebsiteBaseTest;

public class NSCore4AccountTest extends RFNSCoreWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(NSCore4AccountTest.class.getName());

	private NSCore4HomePage nscore4HomePage;
	private NSCore4LoginPage nscore4LoginPage;
	private NSCore4MobilePage nscore4MobilePage;
	String RFL_DB = null;

	public NSCore4AccountTest() {
		nscore4HomePage = new NSCore4HomePage(driver);		
	}

	//NSC4_AdministratorLogin_LogingLogout
	@Test
	public void testAdministrationLoginLogout(){
		s_assert.assertTrue(nscore4HomePage.isLogoutLinkPresent(),"Home Page has not appeared after login");
		nscore4LoginPage = nscore4HomePage.clickLogoutLink();		
		s_assert.assertTrue(nscore4LoginPage.isLoginButtonPresent(),"Login page has not appeared after logout");
		s_assert.assertAll();
	}		

}