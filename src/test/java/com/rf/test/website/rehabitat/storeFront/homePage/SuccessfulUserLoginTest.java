package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class SuccessfulUserLoginTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-27 Verify User Login from the corp site
	 * 
	 * Description : This test validates that the successful user login after clicking the
	 * login icon on home page
	 * 				
	 */
	@Test
	public void testUserloginFromCorp_27(){
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(),"user is NOT successfully logged in");
		s_assert.assertTrue(sfHomePage.getCurrentURL().contains(sfHomePage.getBaseUrl()),"User is not on corp site after login");
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-528 Remember Me Functionality
	 * 
	 * Description : This test validates the Remember Me Functonality at the time of
	 * User login
	 * 				
	 */
	@Test(enabled=false)//TODO
	public void testRememberMe_528(){
		sfHomePage.loginToStoreFrontWithRememberMe(TestConstants.CONSULTANT_EMAIL, password);
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(),"user is NOT successfully logged in");
		closeCurrentWindow();
		openTheBrowserAndApplication();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(),"user needs logged in even after remember me ");
		s_assert.assertFalse(sfHomePage.isLoginIconVisible(),"Login Icon should NOT be visible");
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		s_assert.assertFalse(sfHomePage.isWelcomeUserElementDisplayed(),"user should NOt see Welcome msg after logout ");
		s_assert.assertTrue(sfHomePage.isLoginIconVisible(),"Login Icon should be visible after logout");
		s_assert.assertAll();
	}
}