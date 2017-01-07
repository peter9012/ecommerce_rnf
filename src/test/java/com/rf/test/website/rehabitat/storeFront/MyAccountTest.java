package com.rf.test.website.rehabitat.storeFront;

import java.awt.AWTException;

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
	@Test
	public void testVerifyAccountInfoPageForUsers_178(){
		String currentURL = null;
		String urlToAssert = "my-account";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		//Verify details of account info page for consultant.
		s_assert.assertTrue(sfAccountInfoPage.isFirstNameFieldPresent(),"First Name field not present on account Info page for consultant");
		s_assert.assertTrue(sfAccountInfoPage.isLastNameFieldPresent(),"Last Name field not present on account Info page for consultant");
		s_assert.assertTrue(sfAccountInfoPage.isAddressFieldPresent(),"Address line field not present on account Info page for consultant");
		s_assert.assertTrue(sfAccountInfoPage.isCityFieldPresent()," City field not present on account Info page for consultant");
		s_assert.assertTrue(sfAccountInfoPage.isPostalFieldPresent(),"Postal field not present on account Info page for consultant");
		s_assert.assertTrue(sfAccountInfoPage.isMainPhoneNumberFieldPresent(),"Main phone number field not present on account Info page for consultant");
		sfAccountInfoPage.logout();
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		//Verify details of account info page for PC
		s_assert.assertTrue(sfAccountInfoPage.isFirstNameFieldPresent(),"First Name field not present on account Info page for pc");
		s_assert.assertTrue(sfAccountInfoPage.isLastNameFieldPresent(),"Last Name field not present on account Info page for pc");
		s_assert.assertTrue(sfAccountInfoPage.isAddressFieldPresent(),"Address line field not present on account Info page for pc");
		s_assert.assertTrue(sfAccountInfoPage.isCityFieldPresent()," City field not present on account Info page for pc");
		s_assert.assertTrue(sfAccountInfoPage.isPostalFieldPresent(),"Postal field not present on account Info page for pc");
		s_assert.assertTrue(sfAccountInfoPage.isMainPhoneNumberFieldPresent(),"Main phone number field not present on account Info page for pc");
		sfAccountInfoPage.logout();
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL, password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		//Verify details of account info page for RC
		s_assert.assertTrue(sfAccountInfoPage.isFirstNameFieldPresent(),"First Name field not present on account Info page for rc");
		s_assert.assertTrue(sfAccountInfoPage.isLastNameFieldPresent(),"Last Name field not present on account Info page for rc");
		s_assert.assertTrue(sfAccountInfoPage.isAddressFieldPresent(),"Address line field not present on account Info page for rc");
		s_assert.assertTrue(sfAccountInfoPage.isCityFieldPresent()," City field not present on account Info page for rc");
		s_assert.assertTrue(sfAccountInfoPage.isPostalFieldPresent(),"Postal field not present on account Info page for rc");
		s_assert.assertTrue(sfAccountInfoPage.isMainPhoneNumberFieldPresent(),"Main phone number field not present on account Info page for rc");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-179 Account Information- Update username
	 * 
	 * Description : This test tests if the username on account info page is disabled for consultant,PC and RC user.
	 * 
	 *     
	 */
	@Test
	public void testDisabledUsernameOnAccountInfoPageForUsers_179(){
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		s_assert.assertTrue(sfAccountInfoPage.isUsernameFieldDisabled(),"Username is NOT disabled for consultant");
		sfAccountInfoPage.clickWelcomeDropdown();
		sfAccountInfoPage.logout();
		//Login as pc user.
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		s_assert.assertTrue(sfAccountInfoPage.isUsernameFieldDisabled(),"Username is NOT disabled for PC");
		sfAccountInfoPage.clickWelcomeDropdown();
		sfAccountInfoPage.logout();
		//Login as rc user.
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		s_assert.assertTrue(sfAccountInfoPage.isUsernameFieldDisabled(),"Username is NOT disabled for RC");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-249 Email ID format error validation
	 * 
	 * Description : This test do the validations of the email field on account info page for consultant.
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
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.PASSWORD_VALIDATION_ERROR_LESS_THAN_SIX_CHARS;
		sfAccountInfoPage.enterOldPassword(password);
		sfAccountInfoPage.enterNewPassword(passwordLessThan5Chars);
		sfAccountInfoPage.enterConfirmPassword(passwordLessThan5Chars);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("new password", expectedValidationErrorMsg)," validation msg for less than 6 chars has not displayed for new password");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("confirm password", expectedValidationErrorMsg),"validation msg for less than 6 chars has not displayed for confirm password");
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
		String randomWord = CommonUtils.getRandomWord(4);
		String updatedFirstName = "updFname"+randomWord;
		String updatedLastName = "updLname"+randomWord;
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(category_AllProduct);
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		//Verify first And Last name are updated successfully.
		//sfCheckoutPage
		//Verify sponser change functionality for user.
		sfCheckoutPage.editMainAccountInfo();
		sfCheckoutPage.updateFirstName(updatedFirstName);
		sfCheckoutPage.updateLastName(updatedLastName);
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.editMainAccountInfo();
		s_assert.assertTrue(sfCheckoutPage.getMainFirstNameOfUser().contains(updatedFirstName), "FirstName was not edited");
		s_assert.assertTrue(sfCheckoutPage.getMainLastNameOfUser().contains(updatedLastName), "LastName was not edited");
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
	public void testAccountInfoPageMandatoryFieldValidation_248(){
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		sfAccountInfoPage.clearAllFields();
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("First Name", expectedValidationErrorMsg),"First name field empty field validation msg has not displayed");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("Last Name", expectedValidationErrorMsg),"Last field empty field validation msg has not displayed");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("address1", expectedValidationErrorMsg),"address 1 field empty field validation msg has not displayed");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("city", expectedValidationErrorMsg),"city field empty field validation msg has not displayed");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("postal", expectedValidationErrorMsg),"postal code field empty field validation msg has not displayed");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("phone", expectedValidationErrorMsg),"phone number field empty field validation msg has not displayed");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),"email field empty field validation msg has not displayed");
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
		s_assert.assertTrue(sfAutoshipStatusPage.isPulsePopupPresentWithContent(), "'Pulse popup' overlay is not displayed");
		sfAutoshipStatusPage.closePulsePopup();
		s_assert.assertFalse(sfAutoshipStatusPage.isPulsePopupPresentWithContent(), "'Pulse popup' overlay is displayed after clicking on close button");
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
	@Test//TODO Incomplete as on spouse details confirmation popup cancel button not present.
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
		String spouseFirstName = "";
		String spouseLastName = "";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		sfAccountInfoPage.checkSpouseCheckbox();
		sfAccountInfoPage.enterSpouseFirstName(spouseFirstName);
		sfAccountInfoPage.enterSpouseLastName(spouseLastName);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("spouseFirstName", expectedValidationErrorMsg),"Spouse First name empty field validation msg has not displayed");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("spouseLastName", expectedValidationErrorMsg),"Spouse Last name empty field validation msg has not displayed");
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
		String country="USA";
		String profileUpdationMessage = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String phoneNumber2 = TestConstants.PHONE_NUMBER;
		String dayOfBirth = TestConstants.DAY_OF_BIRTH;
		String monthOfBirth = TestConstants.MONTH_OF_BIRTH;
		String yearOfBirth = TestConstants.YEAR_OF_BIRTH;
		String email = TestConstants.CONSULTANT_EMAIL;
		String gender = TestConstants.GENDER;

		//		//Login as consultant user.
		//		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		//		sfHomePage.clickWelcomeDropdown();
		//		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		//		s_assert.assertFalse(sfAccountInfoPage.isCountryNameEditable(country), "Country filled is editable at account info page for consultant");
		//		sfAccountInfoPage.enterMainAccountInfo(firstName, lastName, addressLine1, addressLine2,city, state, postalCode, phoneNumber,phoneNumber2,email,dayOfBirth,monthOfBirth,yearOfBirth,gender);
		//		sfAccountInfoPage.saveAccountInfo();
		//		sfAccountInfoPage.clickUseAsEnteredButtonOnPopUp();
		//		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		//		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'Spouse details' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		//		sfAccountInfoPage.clickWelcomeDropdown();
		//		sfAccountInfoPage.logout();

		//Login as pc
		email = TestConstants.PC_EMAIL;
		sfHomePage.loginToStoreFront(email,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		s_assert.assertFalse(sfAccountInfoPage.isCountryNameEditable(country), "Country filled is editable at account info page for consultant");
		sfAccountInfoPage.enterMainAccountInfo(firstName, lastName, addressLine1, addressLine2,city, state, postalCode, phoneNumber,phoneNumber2,email,dayOfBirth,monthOfBirth,yearOfBirth,gender);
		sfAccountInfoPage.saveAccountInfo();
		sfAccountInfoPage.clickUseAsEnteredButtonOnPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'Spouse details' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		sfAccountInfoPage.clickWelcomeDropdown();
		sfAccountInfoPage.logout();

		//Login as rc
		email = TestConstants.RC_EMAIL;
		sfHomePage.loginToStoreFront(email,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		s_assert.assertFalse(sfAccountInfoPage.isCountryNameEditable(country), "Country filled is editable at account info page for consultant");
		sfAccountInfoPage.enterMainAccountInfo(firstName, lastName, addressLine1, addressLine2,city, state, postalCode, phoneNumber,phoneNumber2,email,dayOfBirth,monthOfBirth,yearOfBirth,gender);
		sfAccountInfoPage.saveAccountInfo();
		sfAccountInfoPage.clickUseAsEnteredButtonOnPopUp();
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
	 */
	@Test
	public void testUpdateAccountInfoWithInvalidDetails_289(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String city = TestConstants.CITY_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
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
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("First Name", expectedValidationErrorMsg),"'This field is required.' msg not displayed for Consultant first name ");
		sfAccountInfoPage.enterFields("firstName", firstName);
		sfAccountInfoPage.clearFields("lastName");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("Last Name", expectedValidationErrorMsg),"'This field is required.' msg not displayed for Consultant last name");
		sfAccountInfoPage.enterFields("lastName", lastName);
		sfAccountInfoPage.clearFields("addressLine");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("address1", expectedValidationErrorMsg),"'This field is required.' msg not displayed for Consultant address line");
		sfAccountInfoPage.enterFields("addressLine", addressLine1);
		sfAccountInfoPage.clearFields("city");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("city", expectedValidationErrorMsg),"'This field is required.' msg not displayed for Consultant city");
		sfAccountInfoPage.enterFields("city", city);
		sfAccountInfoPage.clearFields("postalCode");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("postal", expectedValidationErrorMsg),"'This field is required.' msg not displayed for Consultant postal code");
		sfAccountInfoPage.enterFields("postalCode", postalCode);
		sfAccountInfoPage.clearFields("phone");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("phone", expectedValidationErrorMsg),"'This field is required.' msg not displayed for Consultant phone number");
		sfAccountInfoPage.enterFields("phone", phoneNumber);
		sfAccountInfoPage.clearFields("email");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),"'This field is required.' msg not displayed for Consultant email address");
		sfAccountInfoPage.enterFields("email", emailAddress);
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedEmailValidationErrorMsg),"Please enter a valid email address. msg not displayed for Consultant");
		sfAccountInfoPage.clickWelcomeDropdown();
		sfAccountInfoPage.logout();

		//Login as PC user.
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		expectedEmailValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		sfAccountInfoPage.clearFields("firstName");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("First Name", expectedValidationErrorMsg),"'This field is required.' msg not displayed for PC first name");
		sfAccountInfoPage.enterFields("firstName", firstName);
		sfAccountInfoPage.clearFields("lastName");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("Last Name", expectedValidationErrorMsg),"'This field is required.' msg not displayed for PC last name");
		sfAccountInfoPage.enterFields("lastName", lastName);
		sfAccountInfoPage.clearFields("addressLine");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("address1", expectedValidationErrorMsg),"'This field is required.' msg not displayed for PC address line");
		sfAccountInfoPage.enterFields("addressLine", addressLine1);
		sfAccountInfoPage.clearFields("city");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("city", expectedValidationErrorMsg),"'This field is required.' msg not displayed for PC city");
		sfAccountInfoPage.enterFields("city", city);
		sfAccountInfoPage.clearFields("postalCode");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("postal", expectedValidationErrorMsg),"'This field is required.' msg not displayed for PC postal code");
		sfAccountInfoPage.enterFields("postalCode", postalCode);
		sfAccountInfoPage.clearFields("phone");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("phone", expectedValidationErrorMsg),"'This field is required.' msg not displayed for PC phone number");
		sfAccountInfoPage.enterFields("phone", phoneNumber);
		sfAccountInfoPage.clearFields("email");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),"'This field is required.' msg not displayed for PC email address");
		sfAccountInfoPage.enterFields("email", emailAddress);
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedEmailValidationErrorMsg),"Please enter a valid email address. msg not displayed for PC");
		sfAccountInfoPage.clickWelcomeDropdown();
		sfAccountInfoPage.logout();

		//Login as RC user.
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		expectedEmailValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		sfAccountInfoPage.clearFields("firstName");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("First Name", expectedValidationErrorMsg),"'This field is required.' msg not displayed for RC first name");
		sfAccountInfoPage.enterFields("firstName", firstName);
		sfAccountInfoPage.clearFields("lastName");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("Last Name", expectedValidationErrorMsg),"'This field is required.' msg not displayed for RC last name");
		sfAccountInfoPage.enterFields("lastName", lastName);
		sfAccountInfoPage.clearFields("addressLine");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("address1", expectedValidationErrorMsg),"'This field is required.' msg not displayed for RC address line");
		sfAccountInfoPage.enterFields("addressLine", addressLine1);
		sfAccountInfoPage.clearFields("city");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("city", expectedValidationErrorMsg),"'This field is required.' msg not displayed for RC city");
		sfAccountInfoPage.enterFields("city", city);
		sfAccountInfoPage.clearFields("postalCode");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("postal", expectedValidationErrorMsg),"'This field is required.' msg not displayed for RC postal code");
		sfAccountInfoPage.enterFields("postalCode", postalCode);
		sfAccountInfoPage.clearFields("phone");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("phone", expectedValidationErrorMsg),"'This field is required.' msg not displayed for RC phone number");
		sfAccountInfoPage.enterFields("phone", phoneNumber);
		sfAccountInfoPage.clearFields("email");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),"'This field is required.' msg not displayed for RC email address");
		sfAccountInfoPage.enterFields("email", emailAddress);
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedEmailValidationErrorMsg),"Please enter a valid email address. msg not displayed for RC");

		s_assert.assertAll();
	}

	/***
	 * qTest : TC-301 Address Validations/ Errors
	 * 
	 * Description : This test validate error message on account info page when mandatory fields are filled with
	 * Invalid details and save account info clicked.
	 *
	 */
	@Test//Issue numbers are not accepted in first and last name fields and phone number with special char not accepted.
	public void testUpdateAccountInfoWithInvalidDetails_301(){
		String profileUpdationMessage = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String invalidPostalCode = "T5N";
		String firstNameWithSpecialChar = "auto-'First";
		String lastNameWithSpecialChar = "last'-Name";
		String numericFirstName = "1234";
		String numericLastName = "5678";
		String phoneNumberWithSpecialChar = "(234)-234-2342";
		String emailAddress= TestConstants.CONSULTANT_USERNAME;

		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		String expectedEmailValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		String expectedPostalValidationErrorMsg = TestConstants.POSTAL_VALIDATION_ERROR_VALID_POSTAL_CODE;
		sfAccountInfoPage.clearAllFields();
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("First Name", expectedValidationErrorMsg),"This field is required. for first name");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("Last Name", expectedValidationErrorMsg),"'This field is required.'for last name");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("address1", expectedValidationErrorMsg),"'This field is required.' for address line");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("city", expectedValidationErrorMsg),"'This field is required.' for city");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("postal", expectedValidationErrorMsg),"'This field is required.' for postal code");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("phone", expectedValidationErrorMsg),"'This field is required.' for phone number");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),"'This field is required.' for email address");
		//Enter main account info with invalid postal code
		sfAccountInfoPage.enterMainAccountInfo(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfAccountInfoPage.enterFields("email", emailAddress);
		sfAccountInfoPage.clearFields("postalCode");
		sfAccountInfoPage.enterFields("postalCode", invalidPostalCode);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("postal", expectedPostalValidationErrorMsg),"Please enter a valid postal code.");
		//Empty first and last name fields blank and save.
		sfAccountInfoPage.enterFields("postalCode", postalCode);
		sfAccountInfoPage.clearFields("firstName");
		sfAccountInfoPage.clearFields("lastName");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("First Name", expectedValidationErrorMsg),"This field is required. for first name");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("Last Name", expectedValidationErrorMsg),"'This field is required.'for last name");
		//Enter Special characters in name field and click save.
		sfAccountInfoPage.enterFields("firstName", firstNameWithSpecialChar);
		sfAccountInfoPage.enterFields("lastName", lastNameWithSpecialChar);
		sfAccountInfoPage.saveAccountInfo();
		sfAccountInfoPage.useEnteredDetailsOnSpouseDetailsPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "Profile updation message for first and last name Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		//Enter number in first and last name fields and click save.
		sfAccountInfoPage.clearFields("firstName");
		sfAccountInfoPage.clearFields("lastName");
		sfAccountInfoPage.enterFields("firstName", numericFirstName);
		sfAccountInfoPage.enterFields("lastName", numericLastName);
		sfAccountInfoPage.saveAccountInfo();
		/*sfAccountInfoPage.useEnteredDetailsOnSpouseDetailsPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "Profile updation message for first and last name Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);*/

		//Enter phone number with special characters and click save.
		sfAccountInfoPage.clearFields("firstName");
		sfAccountInfoPage.clearFields("lastName");
		sfAccountInfoPage.clearFields("phone");
		sfAccountInfoPage.enterFields("firstName", firstName);
		sfAccountInfoPage.enterFields("lastName", lastName);
		sfAccountInfoPage.enterFields("phone", phoneNumberWithSpecialChar);
		sfAccountInfoPage.saveAccountInfo();
		/*sfAccountInfoPage.useEnteredDetailsOnSpouseDetailsPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "Profile updation message for phone number Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);*/
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-329 User Selects account drop down to Logout
	 * 
	 * Description : This test validate logout functionality.
	 * 
	 * 				
	 */
	@Test//TODO assert the successful log msg as well
	public void testVerifyLogoutFunctionality_329(){
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		s_assert.assertTrue(sfHomePage.isLogoutSuccessful(),"User unable to logout from application");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-330 User is logged into their account with multiple tabs in the browser
	 * 
	 * Description : This test validate login functionality in multiple tabs.
	 * @throws AWTException 
	 * 
	 * 				
	 */
	@Test
	public void testVerifyLoginFunctionalityInMultipleTabs_330() throws AWTException{
		String currentURL = null;
		String currentWindowID = null; 
		String redefineLinkUnderShopSkincare = "REDEFINE";
		String redefineRegimenURL = "/redefine";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.navigateToShopSkincareLinkInNewTab(redefineLinkUnderShopSkincare);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(redefineRegimenURL), "Expected URL should contain" +redefineRegimenURL+" . but actual on UI is"+currentURL);
		s_assert.assertTrue(sfHomePage.isUserLoggedInNewTab(), "Expected user should be loggedIn in new tab");
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		s_assert.assertTrue(sfHomePage.isLogoutSuccessful(),"User unable to logout from application in new tab");
		sfHomePage.switchToParentWindow(currentWindowID);
		sfHomePage.pageRefresh();
		s_assert.assertTrue(sfHomePage.isLogoutSuccessful(),"User unable to logout from application in parent tab");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-331 My Account drop down should display "Edit CRP" link
	 * 
	 * Description : This test validate CRP Autoship page from welcome DD.
	 * 
	 * 				
	 */
	@Test
	public void testVerifyCRPAutoShipPage_331(){
		String currentURL = null;
		String urlToAssert = "autoship/cart";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.navigateToEditCRPPage();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-357 My Account dropdown should display "Check my pulse"
	 * 
	 * Description : This test validate Check My pulse page from welcome DD.
	 * 
	 * 				
	 */
	@Test//Incomplete as pulse functionality not implemented yet.
	public void testVerifyCheckMyPulsePage_357(){
		String currentURL = null;
		String currentWindowID = null;
		String urlToAssert = "myrfpulse";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.navigateToCheckMyPulsePage();
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-392 Password Rules
	 * 
	 * Description : This test update and validate password of user.
	 * 
	 *     
	 */
	@Test
	public void testUpdateAndVerifyPasswordForUser_392(){
		String emptyNewPassword = "";
		String passwordLessThanSixChar = "Maide";
		String passwordSixCharAndOneNum = "Maiden1";
		String newConfirmPassword = "111Maiden$";
		String profileUpdationMessage = null;
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		String expectedNewPasswordValidationErrorMsg = TestConstants.PASSWORD_VALIDATION_ERROR_LESS_THAN_SIX_CHARS;
		String expectedConfirmPasswordValidationErrorMsg = TestConstants.CONFIRM_PASSWORD_VALIDATION_ERROR_SAME_VALUE;
		sfAccountInfoPage.enterOldPassword(password);
		sfAccountInfoPage.enterConfirmPassword(password);
		//Leave password field blank and save account info.
		sfAccountInfoPage.enterNewPassword(emptyNewPassword);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("new password", expectedValidationErrorMsg),"Empty field validatioin for new password field has not displayed");
		//Enter password with less than six char and no number.
		sfAccountInfoPage.enterNewPassword(passwordLessThanSixChar);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("new password", expectedNewPasswordValidationErrorMsg),"validation msg for password less than 6 chars(no numbers) has not displayed");
		//Enter new Password with at least 6 char and 1 number.
		sfAccountInfoPage.enterNewPassword(passwordSixCharAndOneNum);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertFalse(sfAccountInfoPage.isValidationMsgPresentForParticularField("new password", expectedNewPasswordValidationErrorMsg),"validation msg for password less than 6 chars(no numbers) has displayed");
		//Enter different password in confirm password field.
		sfAccountInfoPage.enterConfirmPassword(newConfirmPassword);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("confirm password", expectedConfirmPasswordValidationErrorMsg),"validation msg for confirm password should be same as password has not displayed");
		//Enter correct confirm password field and click save.
		sfAccountInfoPage.enterConfirmPassword(passwordSixCharAndOneNum);
		sfAccountInfoPage.saveAccountInfo();
		sfAccountInfoPage.useEnteredDetailsOnSpouseDetailsPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'New Password' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		//Reset password to default password.
		sfAccountInfoPage.enterOldPassword(password);
		sfAccountInfoPage.enterNewPassword(password);
		sfAccountInfoPage.enterConfirmPassword(password);
		sfAccountInfoPage.saveAccountInfo();
		sfAccountInfoPage.useEnteredDetailsOnSpouseDetailsPopUp();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'New Password' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-285 Account Information- Email Your Consultant - Valid
	 * 
	 * Description : This test logins with a PC and
	 * 
	 *     
	 */
	@Test
	public void testEmailYourConsultantValid_285(){
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		sfAccountInfoPage.clickEmailYourConsultantLink();
		sfAccountInfoPage.enterEmailYourConsultantDetailsAndSubmit("testName", "testEmail@rf.com", "test email");

	}

	/***
	 * qTest : TC-358 User navigates to Report a Problem page from the order history
	 * 
	 * Description : This test verifies if Return Policy link works fine under 
	 * Orders->Actions->Report Problems
	 * 
	 *     
	 */
	@Test
	public void testReportAProblemOrderHistory_358(){
		String reportProblemsLink = "Report Problems";
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(reportProblemsLink);
		sfOrdersPage.clickReadOurReturnPolicyLink();
		String parentWin = CommonUtils.getCurrentWindowHandle();
		sfOrdersPage.switchToChildWindow(parentWin);
		s_assert.assertTrue(sfOrdersPage.isReturnPolicyPDFOpened(), "Return Policy PDF has not opened");
		sfOrdersPage.switchToParentWindow(parentWin);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-387 Order History
	 * 
	 * Description : This test validates order history section, Return order section, order details page
	 *     
	 */
	@Test
	public void testOrderHistory_387(){
		String orderNumberTitle = "Order Number";
		String orderDateTitle = "Order Date";
		String grandTotalTitle = "Grand Total";
		String statusAndTrackingNumberTitle = "Status / Tracking Number";
		String detailsLink = "Details";
		String reportProblems = "Report Problems";
		String orderDetailsText = "Order Details";
		String orderNumber = null;
		String currentURL = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		s_assert.assertTrue(sfOrdersPage.isOrderHistorySectionPresent(), "Order history section is not present on UI");
		s_assert.assertTrue(sfOrdersPage.isReturnOrderSectionPresent(), "Return order section is not present on UI");
		s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInOrderHistorySection(orderNumberTitle), orderNumberTitle+" is not present in order history section");
		s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInOrderHistorySection(orderDateTitle), orderDateTitle+" is not present in order history section");
		s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInOrderHistorySection(grandTotalTitle), orderNumberTitle+" is not present in order history section");
		s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInOrderHistorySection(statusAndTrackingNumberTitle), statusAndTrackingNumberTitle+" is not present in order history section");
		s_assert.assertTrue(sfOrdersPage.isActionsDDPresentInOrderHistorySection(), "Action DD is not present in order history section on UI");
		sfOrdersPage.clickFirstActionDDUnderOrderHistorySection();
		s_assert.assertTrue(sfOrdersPage.isOptionsPresentUnderActionsDDInOrderHistroySection(detailsLink), detailsLink+" is not present in order history section");
		s_assert.assertTrue(sfOrdersPage.isOptionsPresentUnderActionsDDInOrderHistroySection(reportProblems), reportProblems+" is not present in order history section");
		//In Return Order section
		s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInReturnOrderSection(orderNumberTitle), orderNumberTitle+" is not present in return order section");
		s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInReturnOrderSection(orderDateTitle), orderDateTitle+" is not present in return order section");
		s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInReturnOrderSection(grandTotalTitle), orderNumberTitle+" is not present in return order section");
		s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInReturnOrderSection(statusAndTrackingNumberTitle), statusAndTrackingNumberTitle+" is not present in return order section");
		s_assert.assertTrue(sfOrdersPage.isActionsDDPresentInReturnOrderSection(), "Action DD is not present in return  order section on UI");
		sfOrdersPage.clickFirstActionDDUnderReturnOrderSection();
		s_assert.assertTrue(sfOrdersPage.isOptionsPresentUnderActionsDDInReturnOrderSection(detailsLink), detailsLink+" is not present in return order section");
		s_assert.assertTrue(sfOrdersPage.isOptionsPresentUnderActionsDDInReturnOrderSection(reportProblems), reportProblems+" is not present in return order section");
		orderNumber = sfOrdersPage.clickAndGetFirstOrderNumberFromOrderHistory();
		currentURL = sfOrdersPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(orderNumber) && sfOrdersPage.isTextPresent(orderDetailsText),"Current url should contain for consultant "+orderNumber+"but actual on UI is "+currentURL+" and order details page is not present");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-282 Account Information- Reset password - Invalid Current Password
	 * 
	 * Description : This test verifies the reset password functionality by 
	 * entering incorrect Current password in account info page
	 * 
	 *     
	 */
	@Test
	public void testPasswordResetIncorrectCurrentPwd_282(){
		String incorrectCurrentPassword = "111Maiden";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.PASSWORD_VALIDATION_ERROR_DO_NOT_MATCH;
		sfAccountInfoPage.enterOldPassword(incorrectCurrentPassword);
		sfAccountInfoPage.enterNewPassword(password);
		sfAccountInfoPage.enterConfirmPassword(password);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("current password", expectedValidationErrorMsg)," validation msg for incorrect current password has not displayed");
	}

	/***
	 * qTest : TC-283 Account Information- Reset password - Other Invalid scenarios
	 * 
	 * Description : This test verifies the reset password functionality for invalid scenarios
	 * 
	 *     
	 */
	@Test
	public void testPasswordResetInvalidScenrios_283(){
		String incorrectCurrentPassword = "111Maiden";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.CONFIRM_PASSWORD_VALIDATION_ERROR_SAME_VALUE;
		sfAccountInfoPage.enterOldPassword(password);
		sfAccountInfoPage.enterNewPassword(incorrectCurrentPassword);
		sfAccountInfoPage.enterConfirmPassword(password);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("confirm password", expectedValidationErrorMsg)," validation msg for non-matching new password has not displayed");
		expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		sfAccountInfoPage.enterNewPassword("");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("new password", expectedValidationErrorMsg)," validation msg for incorrect current password has not displayed");
		sfAccountInfoPage.enterNewPassword(password);
		sfAccountInfoPage.enterConfirmPassword("");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("confirm password", expectedValidationErrorMsg)," validation msg for non-matching new password has not displayed");
		s_assert.assertAll();		
	}

	/***
	 * qTest : TC-281 Account Information- Reset password - All valid details
	 * 
	 * Description : this tests validates the password reset functionality for PC user
	 * 
	 *     
	 */
	@Test
	public void testResetPasswordValidDetails_281(){
		String newValidPassword = "111Maiden";
		String profileUpdationMessage = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.PC_FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		//Login as pc user
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		sfAccountInfoPage.enterMainAccountInfo(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfAccountInfoPage.enterOldPassword(password);
		sfAccountInfoPage.enterNewPassword(newValidPassword);
		sfAccountInfoPage.enterConfirmPassword(newValidPassword);
		sfAccountInfoPage.saveAccountInfo();
		sfAccountInfoPage.clickUseAsEnteredButtonOnPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'New Password' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		sfAccountInfoPage.clickWelcomeDropdown();
		sfAccountInfoPage.logout();
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL,password);
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertTrue(sfHomePage.isErrorMessageForIncorrectUsernamePasswordDisplayed(), "Error ");
		navigateToStoreFrontBaseURL();
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL,newValidPassword);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		sfAccountInfoPage.enterOldPassword(newValidPassword);
		sfAccountInfoPage.enterNewPassword(password);
		sfAccountInfoPage.enterConfirmPassword(password);
		sfAccountInfoPage.saveAccountInfo();
		sfAccountInfoPage.clickUseAsEnteredButtonOnPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'New Password' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		s_assert.assertAll();

	}
}