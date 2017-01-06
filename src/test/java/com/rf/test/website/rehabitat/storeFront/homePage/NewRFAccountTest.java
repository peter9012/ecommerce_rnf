package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class NewRFAccountTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-50 User would like to create an account on the site
	 * 
	 * Description : This test validates create an account page from sign up now link. 
	 */
	@Test//InComplete for biz and com site.
	public void testValidateCreateAnAccountPage_50(){

		//Verify create an account page
		sfHomePage.clickLoginIcon();
		sfHomePage.clickSignUpNowLink();
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed(),"Login or register page not present on corp site.");
		s_assert.assertAll();	
	}
}
