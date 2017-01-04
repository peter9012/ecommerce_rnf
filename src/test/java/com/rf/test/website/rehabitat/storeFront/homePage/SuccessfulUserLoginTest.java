package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
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

	/***
	 * qTest : TC-536 Reset password from Login Provision
	 * 
	 * Description : This test validates the reset password functionality
	 * from top login provision on home page
	 * 
	 * THIS TEST DOESN'T VERIFY THE PASSWORD RESET FROM EMAIL FUNCTIONALITY			
	 */
	@Test
	public void testResetPasswordFromLogin_536(){
		sfHomePage.clickLoginIcon().clickForgetPasswordLink();
		sfHomePage.enterPasswordRecoverEmail(TestConstants.CONSULTANT_EMAIL).clickSubmitBtnForPasswordRecovery();
		s_assert.assertTrue(sfHomePage.isPasswordRecoveryEmailMsgDisplayed(TestConstants.PASSWORD_RECOVERY_SUBMIT_SUCCESS_MESSAGE), "Success msg has NOT displayed after clicking submit btn for password recovery");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-537 Reset password from "Create Account Page"
	 * 
	 * Description : This test validates the reset password functionality
	 * from from "Create Account Page"
	 * 
	 * THIS TEST DOESN'T VERIFY THE PASSWORD RESET FROM EMAIL FUNCTIONALITY			
	 */
	@Test
	public void testResetPasswordFromCreateAccountPage_537(){
		String allProduct = "ALL PRODUCTS";
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProduct);
		sfShopSkinCarePage.addFirstProductToBag();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickForgetPasswordLinkAtCheckout();
		sfCheckoutPage.enterPasswordRecoverEmailAtCheckout(TestConstants.CONSULTANT_EMAIL).clickSubmitBtnForPasswordRecoveryAtCheckout();
		s_assert.assertTrue(sfCheckoutPage.isPasswordRecoveryEmailMsgDisplayed(TestConstants.PASSWORD_RECOVERY_SUBMIT_SUCCESS_MESSAGE), "Success msg has NOT displayed after clicking submit btn for password recovery");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-538 Reset password warning message when email address entered with is not registered in RF
	 * 
	 * Description : This test validates the reset password functionality
	 * from top login provision on home page when enetered email is non-registered
	 * 
	 * THIS TEST DOESN'T VERIFY THE PASSWORD RESET FROM EMAIL FUNCTIONALITY			
	 */
	@Test//TODO for terminated users
	public void testResetPasswordFromLoginUnRegisteredEmail_538(){
		String unRegisteredEmail = "abc@xyz.com";
		sfHomePage.clickLoginIcon().clickForgetPasswordLink();
		sfHomePage.enterPasswordRecoverEmail(unRegisteredEmail).clickSubmitBtnForPasswordRecovery();
		s_assert.assertTrue(sfHomePage.isPasswordRecoveryEmailMsgDisplayed(TestConstants.PASSWORD_RECOVERY_SUBMIT_EMAIL_NOT_REGISTERED_MESSAGE), "Warning msg for un-registered msg has NOT displayed after clicking submit btn for password recovery");
		s_assert.assertAll();
	}
}