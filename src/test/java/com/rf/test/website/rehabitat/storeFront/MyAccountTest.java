package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class MyAccountTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-178 Account Information Page
	 * 
	 * Description : This test validates that he account info page for consultant,PC and RC user.
	 * 
	 *     
	 */
	@Test//Incomplete for PC and RC user
	public void testVerifyAccountInfoPageForUsers_178(){
		String currentURL = null;
		String urlToAssert = "my-account";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		//Verify details of account info page for consultant.
		s_assert.assertTrue(sfAccountInfoPage.isFirstNameFieldPresent(),"First Name field not present on account Info page");
		s_assert.assertTrue(sfAccountInfoPage.isLastNameFieldPresent(),"Last Name field not present on account Info page");
		s_assert.assertTrue(sfAccountInfoPage.isAddressFieldPresent(),"Address line field not present on account Info page");
		s_assert.assertTrue(sfAccountInfoPage.isCityFieldPresent()," City field not present on account Info page");
		s_assert.assertTrue(sfAccountInfoPage.isPostalFieldPresent(),"Postal field not present on account Info page");
		s_assert.assertTrue(sfAccountInfoPage.isMainPhoneNumberFieldPresent(),"Main phone number field not present on account Info page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-179 Account Information- Update username
	 * 
	 * Description : This test updates the username on account info page for consultant,PC and RC user.
	 * 
	 *     
	 */
	@Test//Incomplete for PC and RC user and also unable to update for consultant.
	public void testUpdateUsernameOnAccountInfoPageForUsers_179(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String currentURL = null;
		String urlToAssert = "my-account";
		String updatedUserName = "updatedUserName"+randomNum;
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		//Update and verify username on account info page for consultant.
		sfAccountInfoPage.enterUserName(updatedUserName);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-249 Email ID format error validation
	 * 
	 * Description : This test do the validations of the username field on account info page for consultant.
	 * 
	 *     
	 */
	@Test
	public void testEmailIDFormatErrorValidation_249(){
		String emailAddressWithoutAtAndDot = "autocon12xyzcom";
		String emailAddressWithoutAt = "autocon12xyzcom.com";
		String emailAddressWithoutDot = "autocon12@xyzcom";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		sfAccountInfoPage.enterEmail("");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg));
		sfAccountInfoPage.enterEmail(emailAddressWithoutAtAndDot);
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg));
		sfAccountInfoPage.enterEmail(emailAddressWithoutAt);
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg));
		sfAccountInfoPage.enterEmail(emailAddressWithoutDot);
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg));
		sfAccountInfoPage.enterEmail(TestConstants.CONSULTANT_EMAIL);
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		s_assert.assertFalse(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg));
		expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		s_assert.assertFalse(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg));
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-250 Password format error validation
	 * 
	 * Description : This test do the validations of the password field on account info page for consultant.
	 * 
	 *     
	 */
	@Test
	public void testPasswordFormatErrorValidation_250(){
		String passwordLessThan5Chars = "111M";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.PASSWORD_VALIDATION_ERROR_LESS_THAN_SIX_CHARS;
		sfAccountInfoPage.enterOldPassword(password);
		sfAccountInfoPage.enterNewPassword(passwordLessThan5Chars);
		sfAccountInfoPage.enterConfirmPassword(passwordLessThan5Chars);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("new password", expectedValidationErrorMsg)," <msg needs to be added>");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("confirm password", expectedValidationErrorMsg),"<msg needs to be added>");
		sfAccountInfoPage.enterNewPassword("");
		sfAccountInfoPage.enterConfirmPassword(password);
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("new password", expectedValidationErrorMsg),"<msg needs to be added>");		
		sfAccountInfoPage.enterNewPassword(password);
		sfAccountInfoPage.enterConfirmPassword("");
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("confirm password", expectedValidationErrorMsg),"<msg needs to be added>");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-230 Checkout- Viewing Main Account Info
	 * 
	 * Description : This test Updates First and Last name on  checkout page for consultant user.
	 * 
	 *     
	 */
	@Test//Incomplete flow is not as per test case in qtest
	public void testUpdateFirstAndLastNameOnCheckoutPageForUser_230(){
		String category_AllProduct = "ALL PRODUCTS";
		String updatedFirstName = "abcd";
		String updatedLastName = "wxyz";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.navigateToShopSkincareLink(category_AllProduct);
		//sfCartPage = sfHomePage.addProductToCart(cartType);
		sfHomePage.selectFirstProduct();
		sfHomePage.checkoutThePopup();
		sfCheckoutPage = sfHomePage.checkoutTheCart();
		sfCheckoutPage.updateFirstName(updatedFirstName);
		sfCheckoutPage.updateLastName(updatedLastName);
		sfCheckoutPage.clickSaveButton();
		//Verify first And Last name are updated successfully.
		//sfCheckoutPage
		//Verify sponser change functionality for user.
		sfCheckoutPage.editMainAccountInfo();
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-248 All Mandatory fields should display message
	 * 
	 * Description : This test validates error message when on account info page all fields
	 * are empty and save buton clicked.
	 * 
	 *     
	 */
	@Test
	public void testClearAllFieldsOnAccountInfoPageAndVerifyErrorMsg_248(){

		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		sfAccountInfoPage.clearAllFields();
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("First Name", expectedValidationErrorMsg),"This field is required.");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("Last Name", expectedValidationErrorMsg),"This field is required.");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("address1", expectedValidationErrorMsg),"This field is required.");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("city", expectedValidationErrorMsg),"This field is required.");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("postal", expectedValidationErrorMsg),"This field is required.");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("phone", expectedValidationErrorMsg),"This field is required.");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),"This field is required.");
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-276 Consultant Autoship Status-Learn more about Pulse
	 * 
	 * Description : This test validates learn more about pulse link functionality.
	 *
	 * 
	 *     
	 */
	@Test
	public void testViewPulseDetails_276(){

		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickLearnMoreAboutPulse();
		s_assert.assertTrue(sfAutoshipStatusPage.isPulsePopupPresent(), "'Pulse popup' overlay is not displayed");
		sfAutoshipStatusPage.closePulsePopup();
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-284 Account Information- Spouse contact checkbox
	 * 
	 * Description : This test Update and validate spouse information on account info page.
	 *
	 * 
	 * 				
	 */
	@Test//Incomplete as on spouse details confirmation popup cancel button not present.
	public void testUpdateSpouseInformation_284(){
		String spouseFirstName = TestConstants.SPOUSE_FIRST_NAME;
		String spouseLastName = TestConstants.SPOUSE_LAST_NAME;
		String profileUpdationMessage = null;

		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		sfAccountInfoPage.checkSpouseCheckbox();
		sfAccountInfoPage.enterSpouseFirstName(spouseFirstName);
		sfAccountInfoPage.enterSpouseLastName(spouseLastName);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isSpouseDetailsConfirmationPopUpPresent(), "'Spouse details' popup is not displayed");
		sfAccountInfoPage.useEnteredDetailsOnSpouseDetailsPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'Spouse details' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-370 Account Information- Spouse contact checkbox - Invalid
	 * 
	 * Description : This test validate Invalid spouse information on account info page.
	 *
	 * 
	 * 				
	 */
	@Test
	public void testUpdateInvalidSpouseInformation_370(){
		String spouseFirstName = " ";
		String spouseLastName = " ";
		String profileUpdationMessage = null;

		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		sfAccountInfoPage.checkSpouseCheckbox();
		sfAccountInfoPage.enterSpouseFirstName(spouseFirstName);
		sfAccountInfoPage.enterSpouseLastName(spouseLastName);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("spouseFirstName", expectedValidationErrorMsg),"This field is required.");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("spouseLastName", expectedValidationErrorMsg),"This field is required.");
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-288 Account Information Page- Account Info Fields - Valid
	 * 
	 * Description : This test validate profile updation on account info page when all fields are filled
	 * and save account info clicked.
	 *
	 * 
	 * 				
	 */
	@Test
	public void testUpdateAccountInfoWithValidDetails_288(){
		String spouseFirstName = TestConstants.SPOUSE_FIRST_NAME;
		String spouseLastName = TestConstants.SPOUSE_LAST_NAME;
		String profileUpdationMessage = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		String city = TestConstants.CITY_CA;
		String state = TestConstants.STATE_CA;
		String postalCode = TestConstants.POSTAL_CODE_CA;
		String phoneNumber = TestConstants.PHONE_NUMBER;

		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		sfAccountInfoPage.enterMainAccountInfo(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfAccountInfoPage.checkSpouseCheckbox();
		sfAccountInfoPage.enterSpouseFirstName(spouseFirstName);
		sfAccountInfoPage.enterSpouseLastName(spouseLastName);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isSpouseDetailsConfirmationPopUpPresent(), "'Spouse details' popup is not displayed");
		sfAccountInfoPage.useEnteredDetailsOnSpouseDetailsPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'Spouse details' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-289 Account Information Page- Account Info Fields - Invalid
	 * 
	 * Description : This test validate error message on account info page when mandatory fields are empty
	 * and save account info clicked.
	 *
	 * 
	 * 				
	 */
	@Test
	public void testUpdateAccountInfoWithInvalidDetails_289(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		String city = TestConstants.CITY_CA;
		String state = TestConstants.STATE_CA;
		String postalCode = TestConstants.POSTAL_CODE_CA;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String emailAddress = "abc@wyz";

		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		String expectedEmailValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		sfAccountInfoPage.clearFields("firstName");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("First Name", expectedValidationErrorMsg),"'This field is required.' for first name");
		sfAccountInfoPage.enterFields("firstName", firstName);
		sfAccountInfoPage.clearFields("lastName");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("Last Name", expectedValidationErrorMsg),"'This field is required.' for last name");
		sfAccountInfoPage.enterFields("lastName", lastName);
		sfAccountInfoPage.clearFields("addressLine");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("address1", expectedValidationErrorMsg),"'This field is required.' for address line");
		sfAccountInfoPage.enterFields("addressLine", addressLine1);
		sfAccountInfoPage.clearFields("city");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("city", expectedValidationErrorMsg),"'This field is required.' for city");
		sfAccountInfoPage.enterFields("city", city);
		sfAccountInfoPage.clearFields("postalCode");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("postal", expectedValidationErrorMsg),"'This field is required.' for postal code");
		sfAccountInfoPage.enterFields("postalCode", postalCode);
		sfAccountInfoPage.clearFields("phone");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("phone", expectedValidationErrorMsg),"'This field is required.' for phone number");
		sfAccountInfoPage.enterFields("phone", phoneNumber);
		sfAccountInfoPage.clearFields("email");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),"'This field is required.' for email address");
		sfAccountInfoPage.enterFields("email", emailAddress);
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedEmailValidationErrorMsg),"Please enter a valid email address.");
		s_assert.assertAll();
	}
}