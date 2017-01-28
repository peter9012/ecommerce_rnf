package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;
import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class UserRegistrationTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-390 User registration validations
	 * 
	 * Description : This test validates that he account info page for consultant,PC and RC user.
	 * 
	 *     
	 */
	@Test(enabled=false)
	public void testUserRegistrationValidations_390(){
		String timeStamp = CommonUtils.getCurrentTimeStamp();
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String emailID = TestConstants.FIRST_NAME+timeStamp+TestConstants.LAST_NAME;
		String errorMessage = "Please enter a valid email address";
		String errorMessageFromUI = null;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		s_assert.assertTrue(sfCheckoutPage.isAllErrorFieldsPresent(), "Errors are not present for all blank fields during registration");
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC,firstName, lastName, emailID, password);
		errorMessageFromUI = sfCheckoutPage.getErrorMessageOfEmailField();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for invalid email is "+errorMessage+" but actual on UI is "+errorMessage);
		emailID = TestConstants.FIRST_NAME+timeStamp+TestConstants.EMAIL_SUFFIX;
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC,firstName, lastName, emailID, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		s_assert.assertTrue(sfCheckoutPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		s_assert.assertAll();		
	}
}