package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AccessLoginFromHomePageTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-9 User click on the login symbol on the homepage
	 * Description : This test validates username & password field is appear after
	 * click on login link
	 *     
	 */
	@Test(enabled=true)
	public void testUserClickOnTheLoginSymbolOnTheHomepage_9(){
		s_assert.assertFalse(sfHomePage.isUsernameFieldVisible(), "Username field should NOT be visible without clicking on login icon");
		s_assert.assertFalse(sfHomePage.isPasswordFieldVisible(), "Password field should NOT be visible without clicking on login icon");
		sfHomePage.clickLoginIcon();
		s_assert.assertTrue(sfHomePage.isUsernameFieldVisible(), "Username field should be visible after clicking on login icon");
		s_assert.assertTrue(sfHomePage.isPasswordFieldVisible(), "Password field should be visible after clicking on login icon");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-10 User selects login symbol to close the login provision
	 * Description : This test validates provision will get close after
	 * reclick on login link
	 *     
	 */
	@Test(enabled=true)
	public void testUserSelectsLoginSymbolToCloseTheLoginProvision_10(){
		sfHomePage.clickLoginIcon();
		s_assert.assertTrue(sfHomePage.isUsernameFieldVisible(), "Username field should be visible after clicking on login icon");
		s_assert.assertTrue(sfHomePage.isPasswordFieldVisible(), "Password field should be visible after clicking on login icon");
		sfHomePage.clickLoginIcon();
		s_assert.assertFalse(sfHomePage.isUsernameFieldVisible(), "Username field should NOT be visible without clicking on login icon");
		s_assert.assertFalse(sfHomePage.isPasswordFieldVisible(), "Password field should NOT be visible without clicking on login icon");
		s_assert.assertAll();
	}
}