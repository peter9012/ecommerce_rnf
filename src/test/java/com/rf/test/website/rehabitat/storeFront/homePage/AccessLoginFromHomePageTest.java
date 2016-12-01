package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AccessLoginFromHomePageTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-9 User click on the login symbol on the homepage
	 * Description : This test validates username & password field is appear after
	 * click on login link
	 *     
	 */
	@Test
	public void testUserClickOnTheLoginSymbolOnTheHomepage_9(){
		sfHomePage.clickLoginIcon();
		s_assert.assertTrue(sfHomePage.isUsernameFieldPresent(), "Username field is not present after clicked on login icon");
		s_assert.assertTrue(sfHomePage.isPasswordFieldPresent(), "Password field is not present after clicked on login icon");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-10 User selects login symbol to close the login provision
	 * Description : This test validates provision will get close after
	 * reclick on login link
	 *     
	 */
	@Test
	public void testUserSelectsLoginSymbolToCloseTheLoginProvision_10(){
		sfHomePage.clickLoginIcon();
		s_assert.assertTrue(sfHomePage.isUsernameFieldPresent(), "Username field is not present after clicked on login icon");
		s_assert.assertTrue(sfHomePage.isPasswordFieldPresent(), "Password field is not present after clicked on login icon");
		sfHomePage.clickLoginIcon();
		s_assert.assertFalse(sfHomePage.isUsernameFieldPresent(), "Username field is present after clicked on login icon for close provision");
		s_assert.assertFalse(sfHomePage.isPasswordFieldPresent(), "Password field is present after clicked on login icon for close provision");
		s_assert.assertAll();
	}
}