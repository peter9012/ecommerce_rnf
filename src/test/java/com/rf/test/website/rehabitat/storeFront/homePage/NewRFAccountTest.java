package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class NewRFAccountTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-50 User would like to create an account on the site
	 * 
	 * Description : This test validates create an account page from sign up now link. 
	 */
	@Test(enabled=true)
	public void testValidateCreateAnAccountPage_50(){
		String prefix = pwsPrefix();
		//Verify create an account page on corp site
		sfHomePage.clickLoginIcon();
		sfHomePage.clickSignUpNowLink();
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed(),"Login or register page not present on corp site.");
		//Verify the functionality on PWS site.
		sfHomePage.navigateToUrl(sfHomePage.getBaseUrl()+"/" +sfHomePage.getCountry() +"/pws/" + prefix);
		sfHomePage.clickLoginIcon();
		sfHomePage.clickSignUpNowLink();
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed(),"Login or register page not present on PWS site.");
		s_assert.assertAll();	
	}
}
