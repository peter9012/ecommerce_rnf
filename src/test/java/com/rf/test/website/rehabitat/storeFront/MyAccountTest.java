package com.rf.test.website.rehabitat.storeFront;

import java.awt.AWTException;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCheckoutPage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class MyAccountTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-178 Account Information Page
	 * 
	 * Description : This test validates that he account info page for consultant,PC and RC user.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyAccountInfoPageForUsers_178(){
		String currentURL = null;
		String urlToAssert = "my-account";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
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
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage.logout();
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
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
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage.logout();
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),password,true);
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
	@Test(enabled=true)
	public void testDisabledUsernameOnAccountInfoPageForUsers_179(){
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		s_assert.assertTrue(sfAccountInfoPage.isUsernameFieldDisabled(),"Username is NOT disabled for consultant");
		sfAccountInfoPage.clickWelcomeDropdown();
		sfAccountInfoPage.logout();
		//Login as pc user.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		s_assert.assertTrue(sfAccountInfoPage.isUsernameFieldDisabled(),"Username is NOT disabled for PC");
		sfAccountInfoPage.clickWelcomeDropdown();
		sfAccountInfoPage.logout();
		//Login as rc user.
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),password,true);
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
	@Test(enabled=true)
	public void testEmailIDFormatErrorValidation_249(){
		String emailAddressWithoutAtAndDot = "autocon12mailinatorcom";
		String emailAddressWithoutAt = "autocon12mailinator.com";
		String emailAddressWithoutDot = "autocon12@mailinator";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		sfAccountInfoPage.enterEmail("");
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED +" msg NOT displayed for blank email");
		sfAccountInfoPage.enterEmail(emailAddressWithoutAtAndDot);
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS+" msg NOT displayed for email "+emailAddressWithoutAtAndDot);
		sfAccountInfoPage.enterEmail(emailAddressWithoutAt);
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS+" msg NOT displayed for email "+emailAddressWithoutAt);
		sfAccountInfoPage.enterEmail(emailAddressWithoutDot);
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS+" msg NOT displayed for email "+emailAddressWithoutDot);
		sfAccountInfoPage.enterEmail(consultantWithPulseAndWithCRP());
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS;
		s_assert.assertFalse(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),TestConstants.EMAIL_VALIDATION_ERROR_VALID_EMAIL_ADDRESS+" msg displayed");
		expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		s_assert.assertFalse(sfAccountInfoPage.isValidationMsgPresentForParticularField("email", expectedValidationErrorMsg),TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED+" msg displayed");
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
	@Test(enabled=true)
	public void testAccountInfoPageMandatoryFieldValidation_248(){
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
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
	@Test(enabled=true)
	public void testViewPulseDetails_276(){
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
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
	@Test(enabled=true) 
	public void testUpdateSpouseInformation_284(){
		String randomWord = CommonUtils.getRandomWord(5);
		String spouseFirstName = TestConstants.SPOUSE_FIRST_NAME+randomWord;
		String spouseLastName = TestConstants.SPOUSE_LAST_NAME+randomWord;
		String spouseEmail = "testUser"+randomWord+"@mailinator.com";
		String spousePhoneNumber = TestConstants.PHONE_NUMBER;
		String profileUpdationMessage = null;
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		sfAccountInfoPage.checkSpouseCheckbox();
		sfAccountInfoPage.enterSpouseFirstName(spouseFirstName);
		sfAccountInfoPage.enterSpouseLastName(spouseLastName);
		sfAccountInfoPage.enterSpousePhoneNumber(spousePhoneNumber);
		sfAccountInfoPage.enterSpouseEmail(spouseEmail);
		sfAccountInfoPage.saveAccountInfo();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'Spouse details' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		s_assert.assertTrue(sfAccountInfoPage.getSpouseDetailsAfterSaving("firstName").equalsIgnoreCase(spouseFirstName), "spouse first name not saved");
		s_assert.assertTrue(sfAccountInfoPage.getSpouseDetailsAfterSaving("lastName").equalsIgnoreCase(spouseLastName), "spouse last name not saved");
		s_assert.assertTrue(sfAccountInfoPage.getSpouseDetailsAfterSaving("phoneNumber").equalsIgnoreCase(spousePhoneNumber), "spouse phone number not saved");
		s_assert.assertTrue(sfAccountInfoPage.getSpouseDetailsAfterSaving("email").equalsIgnoreCase(spouseEmail), "spouse email not saved");
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
	@Test(enabled=true)
	public void testUpdateInvalidSpouseInformation_370(){
		String spouseFirstName = "";
		String spouseLastName = "";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
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
	@Test(enabled=true)
	public void testUpdateAccountInfoWithValidDetails_288(){
		String country="USA";
		String profileUpdationMessage = null;
		randomWords = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME + randomWords;
		String phoneNumber2 = TestConstants.PHONE_NUMBER;
		String dayOfBirth = TestConstants.DAY_OF_BIRTH;
		String monthOfBirth = TestConstants.MONTH_OF_BIRTH;
		String yearOfBirth = TestConstants.YEAR_OF_BIRTH;
		String email = consultantWithPulseAndWithCRP();
		String gender = TestConstants.GENDER;

		//		//Login as consultant user.
		//		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password);
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
		email = pcUserWithPWSSponsor();
		sfHomePage.loginToStoreFront(email,password,true);
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
		email = rcWithOrderWithoutSponsor();
		sfHomePage.loginToStoreFront(email,password,true);
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
	@Test(enabled=true)
	public void testUpdateAccountInfoWithInvalidDetails_289(){
		randomWords = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME + randomWords;
		String emailAddress = "abc@wyz";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
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
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
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
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),password,true);
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
	@Test(enabled=true) //TODO Issue numbers are not accepted in first and last name fields and phone number with special char not accepted.
	public void testUpdateAccountInfoWithInvalidDetails_301(){
		String profileUpdationMessage = null;
		randomWords = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME + randomWords;
		String invalidPostalCode = "T5N";
		String firstNameWithSpecialChar = "auto-'First";
		String lastNameWithSpecialChar = "last'-Name";
		String numericFirstName = "1234";
		String numericLastName = "5678";
		String phoneNumberWithSpecialChar = "(234)-234-2342";
		String emailAddress= consultantWithPulseAndWithCRP();

		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
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
		//sfAccountInfoPage.useEnteredDetailsOnSpouseDetailsPopUp();
		sfAccountInfoPage.useEnteredDetailsOnSpouseDetailsPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "Profile updation message for first and last name Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);

		//Enter phone number with special characters and click save.
		sfAccountInfoPage.clearFields("firstName");
		sfAccountInfoPage.clearFields("lastName");
		sfAccountInfoPage.clearFields("phone");
		sfAccountInfoPage.enterFields("firstName", firstName);
		sfAccountInfoPage.enterFields("lastName", lastName);
		sfAccountInfoPage.enterFields("phone", phoneNumberWithSpecialChar);
		sfAccountInfoPage.saveAccountInfo();
		sfAccountInfoPage.useEnteredDetailsOnSpouseDetailsPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "Profile updation message for first and last name Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-329 User Selects account drop down to Logout
	 * 
	 * Description : This test validate logout functionality.
	 * 
	 * 				
	 */
	@Test(enabled=true)
	public void testVerifyLogoutFunctionality_329(){
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
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
	@Test(enabled=true)
	public void testVerifyLoginFunctionalityInMultipleTabs_330() throws AWTException{
		String currentURL = null;
		String currentWindowID = null; 
		String redefineLinkUnderShopSkincare = "REDEFINE";
		String redefineRegimenURL = "/redefine";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
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
	@Test(enabled=true)
	public void testVerifyCRPAutoShipPage_331(){
		String currentURL = null;
		String urlToAssert = "autoship/cart";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
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
	@Test(enabled=true) //Not auto loggedIn in pulse.
	public void testVerifyCheckMyPulsePage_357(){
		String currentURL = null;
		String currentWindowID = null;
		String urlToAssert = "myrfpulse";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
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
	@Test(enabled=true)
	public void testUpdateAndVerifyPasswordForUser_392(){
		String emptyNewPassword = "";
		String passwordLessThanSixChar = "Maide";
		String passwordSixCharAndOneNum = "Maiden1";
		String newConfirmPassword = "111Maiden$";
		String profileUpdationMessage = null;
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		String expectedNewPasswordValidationErrorMsg = TestConstants.PASSWORD_VALIDATION_ERROR_LESS_THAN_EIGHT_CHARS;
		String expectedConfirmPasswordValidationErrorMsg = TestConstants.CONFIRM_PASSWORD_VALIDATION_ERROR_SAME_VALUE;
		//Enter password with less than six char and no number.
		sfAccountInfoPage.enterNewPassword(passwordLessThanSixChar);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("new password", expectedNewPasswordValidationErrorMsg),"validation msg for password less than 6 chars(no numbers) has not displayed");
		//Enter new Password with at least 6 char and 1 number.
		sfAccountInfoPage.enterNewPassword(passwordSixCharAndOneNum);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("new password", expectedNewPasswordValidationErrorMsg),"validation msg for password less than 6 chars(no numbers) has not displayed");
		//Enter different password in confirm password field.
		sfAccountInfoPage.enterConfirmPassword(newConfirmPassword);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("confirm password", expectedConfirmPasswordValidationErrorMsg),"validation msg for confirm password should be same as password has not displayed");
		//Enter correct confirm password field and click save.
		sfAccountInfoPage.enterConfirmPassword(passwordSixCharAndOneNum);
		sfAccountInfoPage.saveAccountInfo();
		//Reset password to default password.
		//sfAccountInfoPage.enterOldPassword(password);
		sfAccountInfoPage.enterNewPassword(password);
		sfAccountInfoPage.enterConfirmPassword(password);
		sfAccountInfoPage.saveAccountInfo();
		sfAccountInfoPage.useEnteredDetailsOnSpouseDetailsPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'New Password' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-285 Account Information- Email Your Consultant - Valid
	 * 
	 * Description : This test logins with a PC and validates the 
	 * Email your consultant function -  Valid case
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testEmailYourConsultantValid_285(){
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		sfAccountInfoPage.clickEmailYourConsultantLink();
		sfAccountInfoPage.enterEmailYourConsultantDetailsAndSubmit("testname", "testemail@mailinator.com", "test email");
		String profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.EMAIL_SENT_MESSAGE.trim()), "Email to consultant not sent suucessfully or the msg is different from 'Email Sent'");
		s_assert.assertAll();
	}

	/***
	  * qTest : TC-286 Account Information- Email Your Consultant - Invalid
	  * Description : This test logins with a PC and validates the 
	  * Email your consultant function -  Invalid case
	  *     
	  */
	 @Test(enabled=true)
	 public void testEmailYourConsultantInvalid_286(){
	  String moreThan200Chars  = TestConstants.MORE_THAN_200_CHARS;
	  String emailContent = "emailContent";
	  sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
	  sfHomePage.clickWelcomeDropdown();
	  sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
	  sfAccountInfoPage.clickEmailYourConsultantLink();
	  sfAccountInfoPage.enterEmailYourConsultantDetailsAndSubmit(TestConstants.FIRST_NAME, consultantWithPulseAndWithCRP(), moreThan200Chars);
	  s_assert.assertTrue(sfAccountInfoPage.isEmailYourValidationDisplayed(emailContent, TestConstants.VALIDATION_ERROR_LESS_THAN_200_CHARS),"validation of more than 200 chars not displayed");

	  sfAccountInfoPage.enterEmailYourConsultantDetails(TestConstants.FIRST_NAME, "", "test msg");
	  s_assert.assertTrue(sfAccountInfoPage.isSendButtonForEmailToConsultantDisabled(),"Send button is enabled while email field is blank");

	  sfAccountInfoPage.enterEmailYourConsultantDetails("", consultantWithPulseAndWithCRP(), "test msg");
	  s_assert.assertTrue(sfAccountInfoPage.isSendButtonForEmailToConsultantDisabled(),"Send button is enabled while name field is blank");

	  sfAccountInfoPage.enterEmailYourConsultantDetails(TestConstants.FIRST_NAME, consultantWithPulseAndWithCRP(), "");
	  s_assert.assertTrue(sfAccountInfoPage.isSendButtonForEmailToConsultantDisabled(),"Send button is enabled while message field is blank");

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
	@Test(enabled=false)//This test is not good to run for automation as it can change the password and fail other tests
	public void testPasswordResetIncorrectCurrentPwd_282(){
		String incorrectCurrentPassword = "111Maiden";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.PASSWORD_VALIDATION_ERROR_DO_NOT_MATCH;
		//sfAccountInfoPage.enterOldPassword(incorrectCurrentPassword);
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
	@Test(enabled=false)//Current password functionality no longer exist
	public void testPasswordResetInvalidScenrios_283(){
		String incorrectCurrentPassword = "111Maiden";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.CONFIRM_PASSWORD_VALIDATION_ERROR_SAME_VALUE;
		//sfAccountInfoPage.enterOldPassword(password);
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
	@Test(enabled=true)
	public void testResetPasswordValidDetails_281(){
		String newValidPassword = "111Maiden";
		String profileUpdationMessage = null;
		randomWords = CommonUtils.getRandomWord(5);
		firstName = TestConstants.PC_FIRST_NAME;
		lastName = TestConstants.LAST_NAME + randomWords;
		//Login as pc user
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		sfAccountInfoPage.enterMainAccountInfo(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		//sfAccountInfoPage.enterOldPassword(password);
		sfAccountInfoPage.enterNewPassword(newValidPassword);
		sfAccountInfoPage.enterConfirmPassword(newValidPassword);
		sfAccountInfoPage.saveAccountInfo();
		sfAccountInfoPage.clickUseAsEnteredButtonOnPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'New Password' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		sfAccountInfoPage.clickWelcomeDropdown();
		sfAccountInfoPage.logout();
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertTrue(sfHomePage.isErrorMessageForIncorrectUsernamePasswordDisplayed(), "Error ");
		navigateToStoreFrontBaseURL();
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),newValidPassword,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		//sfAccountInfoPage.enterOldPassword(newValidPassword);
		sfAccountInfoPage.enterNewPassword(password);
		sfAccountInfoPage.enterConfirmPassword(password);
		sfAccountInfoPage.saveAccountInfo();
		sfAccountInfoPage.clickUseAsEnteredButtonOnPopUp();
		profileUpdationMessage = sfAccountInfoPage.getProfileUpdationMessage();
		s_assert.assertTrue(profileUpdationMessage.equalsIgnoreCase(TestConstants.PROFILE_UPDATION_MESSAGE.trim()), "'New Password' profile updation message Expected = "+TestConstants.PROFILE_UPDATION_MESSAGE+" but Actual = "+profileUpdationMessage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-440 PC Perks Status- Delay Autoship - 30 Days
	 * 
	 * Description : This tests delay autoship for PC user by 30 days.
	 * 
	 *     
	 *//*
	@Test(enabled=true)//Needs fix
	public void testDelayPCAutoshipBy30Days_440(){
		String currentNextBillShipDate = null;
		String nextBillShipDateAfterOneMonth = null;
		String nextBillShipDateUIFormat = null;
		String nextBillShipDateFromUI = null;
		String nextBillShipDateUnderAutoshipOrder = null;
		//Login as pc user
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToPCPerksStatusPage();
		currentNextBillShipDate = sfAutoshipStatusPage.getNextBillAndShipDateFromAutoship();
		nextBillShipDateAfterOneMonth = sfAutoshipStatusPage.delayedNextBillShipDate(currentNextBillShipDate,"1");
		nextBillShipDateUIFormat =sfAutoshipStatusPage.delayedNextBillShipDateInUIFormat(nextBillShipDateAfterOneMonth);
		sfAutoshipStatusPage.delayOrCancelPCPerks();
		sfAutoshipStatusPage.clickYesChangeMyAutoshipDateBtn();
		//Update next bill ship date by 30 days.
		sfAutoshipStatusPage.fillNextBillAndShipdate(nextBillShipDateAfterOneMonth);
		sfAutoshipStatusPage.selectSubmitQueryButton();
		nextBillShipDateFromUI = sfAutoshipStatusPage.getNextBillAndShipDateFromAutoship();
		s_assert.assertTrue(nextBillShipDateFromUI.equalsIgnoreCase(nextBillShipDateUIFormat),"Expected next autoship bill ship date on autoship status page "+nextBillShipDateUIFormat+" but actual in UI"+nextBillShipDateFromUI);
		sfAutoshipStatusPage.clickWelcomeDropdown();
		sfOrdersPage = sfAutoshipStatusPage.navigateToOrdersPage();
		nextBillShipDateUnderAutoshipOrder = sfOrdersPage.getNextBillAndShipDateFromOrderDetailPage();
		s_assert.assertTrue(nextBillShipDateUnderAutoshipOrder.contains(nextBillShipDateFromUI),"Expected next autoship bill ship date for autoship order"+nextBillShipDateUIFormat+" but actual in UI"+nextBillShipDateFromUI);
		s_assert.assertAll();
	}

	*//***
	 * qTest : TC-441 PC Perks Status- Delay Autoship - 60 Days
	 * 
	 * Description : This tests delay autoship for PC user by 60 days.
	 * 
	 *     
	 *//*
	@Test(enabled=false)//Needs fix
	public void testDelayPCAutoshipBy60Days_441(){
		String currentNextBillShipDate = null;
		String nextBillShipDateAfterOneMonth = null;
		String nextBillShipDateUIFormat = null;
		String nextBillShipDateFromUI = null;
		String nextBillShipDateUnderAutoshipOrder = null;
		//Login as pc user
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToPCPerksStatusPage();
		currentNextBillShipDate = sfAutoshipStatusPage.getNextBillAndShipDateFromAutoship();
		nextBillShipDateAfterOneMonth = sfAutoshipStatusPage.delayedNextBillShipDate(currentNextBillShipDate,"2");
		nextBillShipDateUIFormat =sfAutoshipStatusPage.delayedNextBillShipDateInUIFormat(nextBillShipDateAfterOneMonth);
		sfAutoshipStatusPage.delayOrCancelPCPerks();
		sfAutoshipStatusPage.selectDelayPCPerksOnPopup();
		//Update next bill ship date by 60 days.
		sfAutoshipStatusPage.fillNextBillAndShipdate(nextBillShipDateAfterOneMonth);
		sfAutoshipStatusPage.selectSubmitQueryButton();
		nextBillShipDateFromUI = sfAutoshipStatusPage.getNextBillAndShipDateFromAutoship();
		s_assert.assertTrue(nextBillShipDateFromUI.equalsIgnoreCase(nextBillShipDateUIFormat),"Expected next autoship bill ship date"+nextBillShipDateUIFormat+" but actual in UI"+nextBillShipDateFromUI);
		sfAutoshipStatusPage.clickWelcomeDropdown();
		sfOrdersPage = sfAutoshipStatusPage.navigateToOrdersPage();
		nextBillShipDateUnderAutoshipOrder = sfOrdersPage.getNextBillAndShipDateFromOrderDetailPage();
		s_assert.assertTrue(nextBillShipDateUnderAutoshipOrder.contains(nextBillShipDateFromUI),"Expected next autoship bill ship date for autoship order"+nextBillShipDateUIFormat+" but actual in UI"+nextBillShipDateFromUI);
		s_assert.assertAll();
	}

	*//***
	 * qTest : TC-442 PC Perks Status- Delay Autoship - Cancel delay
	 * 
	 * Description : This tests validates PC autoship date not get updated 
	 * when date is updated and submit querry not clicked.
	 *     
	 *//*
	@Test(enabled=false)//Needs fix
	public void testDelayPCAutoshipBy30DaysWithoutClickUpdate_442(){
		String currentNextBillShipDate = null;
		String nextBillShipDateAfterOneMonth = null;
		String nextBillShipDateFromUI = null;
		//Login as pc user
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToPCPerksStatusPage();
		currentNextBillShipDate = sfAutoshipStatusPage.getNextBillAndShipDateFromAutoship();
		nextBillShipDateAfterOneMonth = sfAutoshipStatusPage.delayedNextBillShipDate(currentNextBillShipDate,"1");
		sfAutoshipStatusPage.delayOrCancelPCPerks();
		sfAutoshipStatusPage.selectDelayPCPerksOnPopup();
		//Update next bill ship date by 60 days.
		sfAutoshipStatusPage.fillNextBillAndShipdate(nextBillShipDateAfterOneMonth);
		sfAutoshipStatusPage.pageRefresh();
		nextBillShipDateFromUI = sfAutoshipStatusPage.getNextBillAndShipDateFromAutoship();
		s_assert.assertTrue(nextBillShipDateFromUI.equalsIgnoreCase(currentNextBillShipDate),"Expected next autoship bill ship date on autoship status page "+currentNextBillShipDate+" but actual in UI"+nextBillShipDateFromUI);
		s_assert.assertAll();

	}
*/
	/***
	 * qTest : TC-279 PC Perks Status- View autoship details
	 * 
	 * Description : This tests validates Details on PC perks autoship cart page
	 * from autoship detail page.
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyPCPerksAutoshipCartPageFromAutoShipDetailsPage_279(){
		String currentURL = null;
		String autoshipCart = "autoship/cart";
		String currentNextBillShipDate = null;
		String billShipDateFromAutoshipCart = null;
		//Login as pc user
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToPCPerksStatusPage();
		s_assert.assertTrue(sfAutoshipStatusPage.isPCPerksAutoshipStatusPagePresent(),"PC Perks status page is not present.");
		currentNextBillShipDate = sfAutoshipStatusPage.getNextBillAndShipDateFromAutoship();
		sfAutoshipCartPage = sfAutoshipStatusPage.viewDetailsOfAutoship();
		//Verify autoship cart page.
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(autoshipCart), "Expected URL should contain"+autoshipCart+" but actual on UI is"+currentURL);
		//Verify autoship items at autoship cart page.
		s_assert.assertTrue(sfAutoshipCartPage.isAutoshipItemsPresentOnCartPage(),"There are no autoship items present on autoship cart page.");
		billShipDateFromAutoshipCart = sfAutoshipCartPage.getBillAndShipDateFromAutoshipCartPage();
		s_assert.assertTrue(billShipDateFromAutoshipCart.equalsIgnoreCase(currentNextBillShipDate),"Expected next autoship bill ship date on autoship cart page "+currentNextBillShipDate+" but actual in UI"+billShipDateFromAutoshipCart);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-280 PC Perks Status page
	 * 
	 * Description : This tests validates Details on PC perks autoship details page
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyPCPerksAutoshipDetails_280(){
		String pcPerksStatus = null;
		String expectedPCPerksStatus = "Enrolled";
		//Login as pc user
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToPCPerksStatusPage();
		s_assert.assertTrue(sfAutoshipStatusPage.isPCPerksAutoshipStatusPagePresent(),"PC Perks status page is not present.");
		s_assert.assertTrue(sfAutoshipStatusPage.isNextAutoshipBillShipDatePresent(),"Bill ship date for next autoship order is not present on autoship status page.");
		pcPerksStatus = sfAutoshipStatusPage.getCurrentPCPerksStatusFromAutoshipStatusPage();
		s_assert.assertTrue(pcPerksStatus.equalsIgnoreCase(expectedPCPerksStatus),"PC Perks status expected on autoship status page "+expectedPCPerksStatus+" but actual in UI"+pcPerksStatus);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-518 Edit PWS - User is subscribed to Pulse
	 * 
	 * Description : This test validates edits PWS functionality of user 
	 * who is subscribed to pulse.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testEditPWSOfUserSubscribedToPulse_518(){
		//Login as consultant user and verify about me page.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAboutMePage = sfHomePage.navigateToEditPWSPage();
		s_assert.assertTrue(sfAboutMePage.isAboutMePagePresent(),"About me Page content not visible after clicking edit pws from welcome DD");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-359 Enroll into pulse from my account order page
	 * 
	 * Description : This test enroll user in pulse from order page
	 * 
	 * 				
	 */
	@Test(enabled=true) 
	public void testEnrollUserInPulseFromOrderHistoryPage_359(){
		//Duplicate TestCase same as TC-499 Cart Page- Checkout CTA - Anonymous - Login
	}

	/***
	 * qTest : TC-360 Access Link to Pulse from Order History
	 * 
	 * Description : This test validate pulse link functionality on order page.
	 * 
	 * 				
	 */
	@Test(enabled=true)//Not auto loggedIn in pulse.
	public void testVerifyPulseLinkOnOrderPage_360(){
		String currentURL = null;
		String currentWindowID = null;
		String urlToAssert = "myrfpulse";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfOrdersPage.clickPulseLink();
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-287 Account Information- Consultant information
	 * 
	 * Description : This test validates sponser name on account info page.
	 *
	 * 
	 * 				
	 */
	@Test(enabled=true)
	public void testVerifySponserNameOnAccountInfoPage_287(){
		//Login as consultant user.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		s_assert.assertTrue(sfAccountInfoPage.isSponserNameWithTitleRFIndependentConsultantPresent(),"Sponser name along title 'R+F' independent consultant not present.");
		s_assert.assertAll();
	}

	/***
	 * qtest: TC-369 Consultant Autoship Status- Subscribe to Pulse (Using some other Prefix less than 180 days)
	   Description: This method re-enroll consultant in pulse with existing autosuggested prefix and other consultant prefix.
	 *
	 */ 
	@Test(enabled=true)//Accepting inactive prefix less than 180 days.
	public void testReEnnrollmentInPulseWithin180DaysWithOtherUserPrefix_369(){
		String otherUserPrefix = pwsPrefix();
		timeStamp = CommonUtils.getCurrentTimeStamp();
		String newPrefix = firstName+timeStamp;
		String errorMessage = null;
		String expectedErrorMsgForPrefix = TestConstants.ERROR_MSG_EXISTING_PREFIX;
		//Subscribe to pulse with a new prefix.
		sfCheckoutPage = new StoreFrontCheckoutPage(driver);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRPForCancellation(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickSubscribeToPulseBtn();
		sfAutoshipStatusPage.enterAvailablePrefix(otherUserPrefix);
		errorMessage = sfAutoshipStatusPage.getErrorMessageForExistingPrefixName();
		s_assert.assertTrue(errorMessage.contains(expectedErrorMsgForPrefix),"Error message for existing prefix name not available Expected"+expectedErrorMsgForPrefix+"while Actual"+errorMessage);
		sfAutoshipStatusPage.enterAvailablePrefix(newPrefix);
		sfCheckoutPage = sfAutoshipStatusPage.clickConfirmSubscription();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isPopUpForTermsAndConditionsCheckboxDisplayed(), "validation popup for terms and conditions not displayed");
		sfCheckoutPage.closePopUp();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForAutoshipOrder().clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order is Not placed successfully");
		//Cancel the pulse of user.
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickCancelPulseSubscription().clickConfirmSubscription();
		s_assert.assertTrue(sfAutoshipStatusPage.isSubscribeToPulseBtnDisplayed(), "Pulse subscription is NOT cancelled");
		s_assert.assertAll();
	}

	/***
	 * qtest: TC-473 reCaptcha - Submit as Unselected
	   Description: This test validate the send button is disabled or not while not selecting the captcha
	 *
	 */ 
	@Test
	public void testReCaptchaSubmitAsUnselected_473(){
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfAboutMePage = sfHomePage.clickAboutMe();
		s_assert.assertTrue(sfAboutMePage.isSendButtonDisabled(), "Send button is enabled before enter captcha");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-250 Password format error validation
	 * 
	 * Description : This test do the validations of the password field on account info page for consultant.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPasswordFormatErrorValidation_250(){
		String passwordLessThan5Chars = "111M";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		String expectedValidationErrorMsg = TestConstants.PASSWORD_VALIDATION_ERROR_LESS_THAN_EIGHT_CHARS;
		//sfAccountInfoPage.enterOldPassword(password);
		sfAccountInfoPage.enterNewPassword(passwordLessThan5Chars);
		sfAccountInfoPage.enterConfirmPassword(passwordLessThan5Chars);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("new password", expectedValidationErrorMsg)," validation msg for less than 6 chars has not displayed for new password");
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("confirm password", expectedValidationErrorMsg),"validation msg for less than 6 chars has not displayed for confirm password");
		sfAccountInfoPage.enterNewPassword("");
		sfAccountInfoPage.enterConfirmPassword(password);
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.CONFIRM_PASSWORD_VALIDATION_ERROR_SAME_VALUE;
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("confirm password", expectedValidationErrorMsg),"<msg needs to be added>");		
		sfAccountInfoPage.enterNewPassword(password);
		sfAccountInfoPage.enterConfirmPassword("");
		sfAccountInfoPage.saveAccountInfo();
		expectedValidationErrorMsg = TestConstants.CONFIRM_PASSWORD_VALIDATION_ERROR_SAME_VALUE;
		s_assert.assertTrue(sfAccountInfoPage.isValidationMsgPresentForParticularField("confirm password", expectedValidationErrorMsg),"<msg needs to be added>");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-358 User navigates to Report a Problem page from the order history
	 * 
	 * Description : This test verifies if Return Policy link works fine under 
	 * Orders->Actions->Report Problems
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testReportAProblemOrderHistory_358(){
		String reportProblemsLink = "Report Problems";
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(reportProblemsLink);
		String parentWin = CommonUtils.getCurrentWindowHandle();
		sfOrdersPage.clickReadOurReturnPolicyLink();
		sfOrdersPage.switchToChildWindow(parentWin);
		s_assert.assertTrue(sfOrdersPage.isReturnPolicyPDFOpened(), "Return Policy PDF has not opened");
		sfOrdersPage.switchToParentWindow(parentWin);
		s_assert.assertAll();
	}

	/***
	 * qtest: TC-278 Consultant Autoship Status- Subscribe to Pulse (Re-Enrollment within 180 days)
	  Description: This method re-enroll consultant in pulse with new prefix and existing autosuggested prefix .
	 *
	 */	
	@Test(enabled=true)
	public void testReEnnrollmentInPulseWithin180DaysOfExistingAutoSuggestedPrefix_278(){
		String prefix = firstName + CommonUtils.getCurrentTimeStamp();
		String autoSuggestedPrefixName = null;
		//Subscribe to pulse with a new prefix.
		sfCheckoutPage = new StoreFrontCheckoutPage(driver);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRPForCancellation(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickSubscribeToPulseBtn();
		autoSuggestedPrefixName = sfAutoshipStatusPage.getAvailablePrefixName();
		sfAutoshipStatusPage.enterAvailablePrefix(prefix);
		sfCheckoutPage = sfAutoshipStatusPage.clickConfirmSubscription();
		sfCheckoutPage.clickSaveButton();
	//	sfCheckoutPage.clickUseSavedCardBtnOnly();
	//	sfCheckoutPage.clickUseThesePaymentDetailsAndReturnBillingProfileName("1");
		sfCheckoutPage.clickBillingDetailsNextbutton().clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isPopUpForTermsAndConditionsCheckboxDisplayed(), "validation popup for terms and conditions not displayed");
		sfCheckoutPage.closePopUp();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForAutoshipOrder().clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order is Not placed successfully");
		//Cancel the pulse of user.
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickCancelPulseSubscription().clickConfirmSubscription();
		s_assert.assertTrue(sfAutoshipStatusPage.isSubscribeToPulseBtnDisplayed(), "Pulse subscription is NOT cancelled");
		//Subscribe to pulse again with autosuggested prefix prefix.
		sfAutoshipStatusPage.clickSubscribeToPulseBtn();
		sfAutoshipStatusPage.enterAvailablePrefix(autoSuggestedPrefixName);
		sfCheckoutPage = sfAutoshipStatusPage.clickConfirmSubscription();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickBillingDetailsNextbutton().clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isPopUpForTermsAndConditionsCheckboxDisplayed(), "validation popup for terms and conditions not displayed");
		sfCheckoutPage.closePopUp();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForAutoshipOrder().clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order is Not placed successfully");
		//Cancel the pulse of user.
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickCancelPulseSubscription().clickConfirmSubscription();
		s_assert.assertTrue(sfAutoshipStatusPage.isSubscribeToPulseBtnDisplayed(), "Pulse subscription is NOT cancelled");
		s_assert.assertAll();
	}

	/***
	 * qtest: TC-380 Cancel Pulse Subscription From My Account Autoship page
	 * Description: This method cancel pulse from autoship status page.
	 */	
	@Test(enabled=true)
	public void testConsultantFirstTimePulseEnrollment_380(){
		String prefix = firstName + CommonUtils.getCurrentTimeStamp();
		sfCheckoutPage = new StoreFrontCheckoutPage(driver);
		//sfHomePage.loginToStoreFront(consultantWithoutPulseAndWithoutCRP(),password,true);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRPForCancellation(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickSubscribeToPulseBtn();
		sfAutoshipStatusPage.enterAvailablePrefix(prefix);
		sfCheckoutPage = sfAutoshipStatusPage.clickConfirmSubscription();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickBillingDetailsNextbutton().clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isPopUpForTermsAndConditionsCheckboxDisplayed(), "validation popup for terms and conditions not displayed");
		sfCheckoutPage.closePopUp();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForAutoshipOrder().clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order is Not placed successfully");
		//Cancel pulse of user.
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickCancelPulseSubscription();
		sfAutoshipStatusPage.clickConfirmSubscriptionButton();
		s_assert.assertTrue(sfAutoshipStatusPage.isPulseCancellationPopupPresent(),"Pulse cancellation popup is not present.");
		sfAutoshipStatusPage.clickCancelOnPulseCancellationPopup();
		s_assert.assertFalse(sfAutoshipStatusPage.isPulseCancellationPopupPresent(),"Pulse cancellation popup is present after clicking cancel button.");
		sfAutoshipStatusPage.clickConfirmSubscription();
		s_assert.assertTrue(sfAutoshipStatusPage.isSubscribeToPulseBtnDisplayed(), "Pulse subscription is NOT cancelled");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-519 Edit PWS - User is not subscribed to Pulse
	 * 
	 * Description : This test validates edits PWS functionality of user 
	 * who is not subscribed to pulse.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testEditPWSOfUserNotSubscribedToPulse_519(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		lastName = TestConstants.LAST_NAME;
		email = firstName + timeStamp+TestConstants.EMAIL_SUFFIX;

		//Enroll consultant user.
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting kit");
		sfHomePage.chooseProductFromKitPage();
		sfHomePage.UnSelectSubscribeToPulseCheckBox();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'Your order number is' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		sfHomePage.clickWelcomeDropdown();
		s_assert.assertFalse(sfHomePage.isEditPWSLinkPresentInWelcomeDD(),"Edit PWS link is present for user not subscribed to pulse.");
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-361 View Pulse autoship status and next bill date from Autoship status page in my account
	 * 
	 * Description : This test validate pulse autoship status and next bill ship date
	 * from autoship status page.
	 * 				
	 */
	@Test(enabled=true) //Not auto loggedIn in pulse.
	public void testVerifyPulseStatusAndNextBillShipDate_361(){
		String currentURL = null;
		String currentWindowID = null;
		String currentPulseStatus = null;
		String urlToAssert = "myrfpulse";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		s_assert.assertFalse(sfAutoshipStatusPage.isSubscribeToPulseBtnDisplayed(),"User is not subscribed to pulse");
		currentPulseStatus = sfAutoshipStatusPage.getCurrentPulseStatus();
		s_assert.assertTrue(currentPulseStatus.contains("ACTIVE"),"Consultant is not enrolled into Pulse yet expected 'ACTIVE' and Actual"+currentPulseStatus);
		s_assert.assertTrue(sfAutoshipStatusPage.isNextPulseAutoshipBillShipDatePresent(),"Pulse next Bill ship date not present on autoship status page.");
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
	 * qtest: TC-277 Consultant Autoship Status- Subscribe to Pulse (First Time Pulse Enrollment)
	 * Description: This method subscribe the consultant with pulse and also cancels the same
	 */	
	@Test(enabled=true)
	public void testConsultantFirstTimePulseEnrollment_277(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"wocrpwop"+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String prefix = firstName + CommonUtils.getCurrentTimeStamp();
		String emptyPrefix = "";
		String errorMsg = TestConstants.VALIDATION_ERROR_THIS_FIELD_IS_REQUIRED;
		String error = null;
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting kit");
		sfHomePage.chooseProductFromKitPage();
		sfHomePage.UnSelectSubscribeToPulseCheckBox();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfHomePage.selectBillingAddressFromDD();
		//sfHomePage.checkUseMyDeliveryAddressChkBox();
		sfHomePage.clickBillingDetailsNextbutton();
		if(sfHomePage.hasTokenizationFailed()==true){
			sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfHomePage.clickBillingDetailsNextbutton();
		}
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'ENROLLMENT SUCCESSFUL' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		navigateToStoreFrontBaseURL();
		//Login with consultant not enrolled in pulse.
		sfHomePage.loginToStoreFront(email,password,true);
		//Enroll user in pulse.
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickSubscribeToPulseBtn();
		sfAutoshipStatusPage.enterAvailablePrefix(emptyPrefix);
		error = sfAutoshipStatusPage.getErrorMessageForEmptyPrefixName();
		s_assert.assertTrue(error.contains(errorMsg),"Error message for empty prefix name Expected"+errorMsg+"while Actual"+error);
		sfAutoshipStatusPage.enterAvailablePrefix(prefix);
		sfCheckoutPage = sfAutoshipStatusPage.clickConfirmSubscription();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickBillingDetailsNextbutton().clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isPopUpForTermsAndConditionsCheckboxDisplayed(), "validation popup for terms and conditions not displayed");
		sfCheckoutPage.closePopUp();
		sfCheckoutPage.selectTermsAndConditionsChkBox().clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"User enrolled in pulse successfully");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-230 Checkout- Viewing Main Account Info
	 * 
	 * Description : This test Updates First and Last name on  checkout page for consultant user.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateFirstAndLastNameOnCheckoutPageForUser_230(){
		String randomWord = CommonUtils.getRandomWord(4);
		String updatedFirstName = "updFname"+randomWord;
		String updatedLastName = "updLname"+randomWord;
		String firstNameAccountInfo = "";
		String lastNameAccountInfo = "";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		//Verify first And Last name are updated successfully.
		sfCheckoutPage.editMainAccountInfo();
		sfCheckoutPage.updateFirstName(updatedFirstName);
		sfCheckoutPage.updateLastName(updatedLastName);
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.editMainAccountInfo();
		s_assert.assertTrue(sfCheckoutPage.getMainFirstNameOfUser().contains(updatedFirstName), "FirstName was not edited");
		s_assert.assertTrue(sfCheckoutPage.getMainLastNameOfUser().contains(updatedLastName), "LastName was not edited");
		//Verify consultant can not edit sponser details.
		s_assert.assertFalse(sfCheckoutPage.isChangeSponserLinkDisplayed(),"Change sponser link is present on account info page for consultant user");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		sfCheckoutPage.clickWelcomeDropdown();
		sfAccountInfoPage = sfCheckoutPage.navigateToAccountInfoPage();
		firstNameAccountInfo = sfAccountInfoPage.getFirstNameFromAccountInfo();
		lastNameAccountInfo = sfAccountInfoPage.getLastNameFromAccountInfo();
		//verify first and last name on account info page.
		s_assert.assertTrue(firstNameAccountInfo.equalsIgnoreCase(updatedFirstName.trim()),"Updated first name is not present on account info page.");
		s_assert.assertTrue(lastNameAccountInfo.equalsIgnoreCase(updatedLastName.trim()),"Updated Last name is not present on account info page.");
		sfCheckoutPage.clickWelcomeDropdown();
		sfCheckoutPage.logout();
		//Login as PC user
		navigateToStoreFrontBaseURL();
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		//Verify PC can not edit sponser details.
		sfCheckoutPage.editMainAccountInfo();
		s_assert.assertFalse(sfCheckoutPage.isChangeSponserLinkDisplayed(),"Change sponser link is present on account info page for PC user");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-387 Order History
	 * 
	 * Description : This test validates order history section, Return order section, order details page
	 *     
	 */
	@Test(enabled=true)
	public void testOrderHistory_387(){
		String orderNumberTitle = "Order Number";
		String orderDateTitle = "Order Date";
		String grandTotalTitle = "Grand Total";
		String statusAndTrackingNumberTitle = "Status/Tracking Number";
		String detailsLink = "Details";
		String reportProblems = "Report Problems";
		/*String orderDetailsText = "Order Details";
	  String orderNumber = null;
	  String currentURL = null;*/
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
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
		/*s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInReturnOrderSection(orderNumberTitle), orderNumberTitle+" is not present in return order section");
	  s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInReturnOrderSection(orderDateTitle), orderDateTitle+" is not present in return order section");
	  s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInReturnOrderSection(grandTotalTitle), orderNumberTitle+" is not present in return order section");
	  s_assert.assertTrue(sfOrdersPage.isHeaderTitlePresentInReturnOrderSection(statusAndTrackingNumberTitle), statusAndTrackingNumberTitle+" is not present in return order section");
	  s_assert.assertTrue(sfOrdersPage.isActionsDDPresentInReturnOrderSection(), "Action DD is not present in return  order section on UI");
	  sfOrdersPage.clickFirstActionDDUnderReturnOrderSection();
	  s_assert.assertTrue(sfOrdersPage.isOptionsPresentUnderActionsDDInReturnOrderSection(detailsLink), detailsLink+" is not present in return order section");
	  s_assert.assertTrue(sfOrdersPage.isOptionsPresentUnderActionsDDInReturnOrderSection(reportProblems), reportProblems+" is not present in return order section");
	  orderNumber = sfOrdersPage.clickAndGetFirstOrderNumberFromOrderHistory();
	  currentURL = sfOrdersPage.getCurrentURL();
	  s_assert.assertTrue(currentURL.contains(orderNumber) && sfOrdersPage.isTextPresent(orderDetailsText),"Current url should contain for consultant "+orderNumber+"but actual on UI is "+currentURL+" and order details page is not present");*/
		s_assert.assertAll();
	}
}