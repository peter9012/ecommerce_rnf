package com.rf.test.website.LSD;

import org.testng.annotations.Test;

import com.rf.pages.website.LSD.LSDHomePage;
import com.rf.pages.website.LSD.LSDLoginPage;
import com.rf.test.website.RFLSDWebsiteBaseTest;

public class LSDTests extends RFLSDWebsiteBaseTest{
	private LSDLoginPage lsdLoginPage;
	private LSDHomePage lsdHomePage;
	String whiteListedUserName = "Kohollaren3@gmail.com";
	String nonwhiteListedUserName = "kohollaten3@gmail.com";

	public LSDTests() {
		lsdLoginPage = new LSDLoginPage(driver);
		lsdHomePage = new LSDHomePage(driver);
	}

	//Welcome page to login page TC-259
	@Test
	public void testWelcomeLoginPage(){
		String wrongPassword = "101Maiden$";
		lsdLoginPage.enterUsername(nonwhiteListedUserName);
		lsdLoginPage.enterPassword(password);
		lsdLoginPage.clickLoginBtn();
		s_assert.assertTrue(lsdLoginPage.isLoginFailErrorPresent(), "Login fail error not appeared for non-whiteListed user");
		lsdLoginPage.enterUsername(whiteListedUserName);
		lsdLoginPage.enterPassword(wrongPassword);
		lsdLoginPage.clickLoginBtn();
		s_assert.assertTrue(lsdLoginPage.isLoginFailErrorPresent(), "Login fail error not appeared for whiteListed user with wrong password");
		lsdLoginPage.enterUsername(whiteListedUserName);
		lsdLoginPage.enterPassword(password);
		lsdLoginPage.clickLoginBtn();
		s_assert.assertTrue(lsdHomePage.getCurrentURL().toLowerCase().contains("home"), "user is not on home page after login,the current url is expected to have 'home',but the current URL is "+lsdHomePage.getCurrentURL());
		s_assert.assertAll();
	}
}
