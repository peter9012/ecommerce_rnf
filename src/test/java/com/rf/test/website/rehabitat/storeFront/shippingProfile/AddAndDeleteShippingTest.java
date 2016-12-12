package com.rf.test.website.rehabitat.storeFront.shippingProfile;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AddAndDeleteShippingTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-77 Adding shipping address from my account
	 * Description : This test add a new shipping address and validates it
	 * 
	 *     
	 */
	@Test//Incomplete
	public void testAddShippingAddressFromMyAccount_77(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		String city = TestConstants.CITY_CA;
		String state = TestConstants.STATE_CA;
		String postalCode = TestConstants.POSTAL_CODE_CA;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickAddANewShippingAddressLink();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfShippingInfoPage.clickSaveButtonOfShippingAddress();
		sfShippingInfoPage.clickUseAsEnteredButton();
		s_assert.assertTrue(sfShippingInfoPage.isShippingProfilePresent(lastName), "Newly added shipping profile is not present at shipping info page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-78 Cancelling adding of the shipping profile
	 * Description : This test click add a new shipping address and click on cancel
	 * and validates the profile
	 * 
	 *     
	 */
	@Test
	public void testCancellingAddingOftheShippingProfile_78(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		String city = TestConstants.CITY_CA;
		String state = TestConstants.STATE_CA;
		String postalCode = TestConstants.POSTAL_CODE_CA;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickAddANewShippingAddressLink();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfShippingInfoPage.clickCancelButtonOfShippingAddress();
		s_assert.assertFalse(sfShippingInfoPage.isShippingAddressDetailFieldsPresent(lastName), "Shipping detail fields are present after clicked on cancel button");
		s_assert.assertFalse(sfShippingInfoPage.isShippingProfilePresent(lastName), "Newly added shipping profile is successfully saved at shipping info page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-79 Negative Test - checking error messages for the address fields
	 * Description : This test click add a new shipping address and click on save
	 * and validates the error fields
	 * 
	 *     
	 */
	@Test
	public void testCheckingErrorMessageForTheAddressField_79(){
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickAddANewShippingAddressLink();
		sfShippingInfoPage.clickSaveButtonOfShippingAddress();
		s_assert.assertTrue(sfShippingInfoPage.isErrorMessagePresentForFirstAndLastName(), "Error message is not present for first and last name field");
		s_assert.assertTrue(sfShippingInfoPage.isErrorMessagePresentForAddressLine1(), "Error message is not present for address line 1 field");
		s_assert.assertTrue(sfShippingInfoPage.isErrorMessagePresentForCity(), "Error message is not present for city field");
		s_assert.assertTrue(sfShippingInfoPage.isErrorMessagePresentForState(), "Error message is not present for state field");
		s_assert.assertTrue(sfShippingInfoPage.isErrorMessagePresentForPostalCode(), "Error message is not present for postal code field");
		s_assert.assertTrue(sfShippingInfoPage.isErrorMessagePresentForPhoneNumber(), "Error message is not present for phone number field");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-176 Adding a Ship Address- Default Country AU site
	 * Description : This test validates the country name & country field 
	 * should not be editable for AU
	 * 
	 *     
	 */
	@Test
	public void testAddingAShipAddressDefaultCountryAUSite_176(){
		if(country.equalsIgnoreCase("au")){
			String expectedCountryName = "australia";
			String countryNameFromUI = null;
			sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
			sfHomePage.clickWelcomeDropdown();
			sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
			sfShippingInfoPage.clickAddANewShippingAddressLink();
			s_assert.assertFalse(sfShippingInfoPage.isCountryNameEditable(country), "Country field is editable at shipping profile page");
			countryNameFromUI = sfShippingInfoPage.getCountryName().toLowerCase();
			s_assert.assertTrue(countryNameFromUI.contains(expectedCountryName), "Expected country name is"+expectedCountryName+" but actual on UI is"+countryNameFromUI);
			sfShippingInfoPage.clickRodanAndFieldsLogo();
			sfShippingInfoPage.clickAllProducts();
			sfShippingInfoPage.selectFirstProduct();
			sfShippingInfoPage.checkoutThePopup();
			sfCheckoutPage = sfShippingInfoPage.checkoutTheCart();
			sfCheckoutPage.clickSaveButton();
			sfCheckoutPage.clickAddNewShippingAddressButton();
			s_assert.assertFalse(sfCheckoutPage.isCountryNameEditable(country), "Country filed is editable at checkout page");
			countryNameFromUI = sfCheckoutPage.getCountryName().toLowerCase();
			s_assert.assertTrue(countryNameFromUI.contains(expectedCountryName), "Expected country name at checkout page is"+expectedCountryName+" but actual on UI is"+countryNameFromUI);
			s_assert.assertAll();
		}
	}

	/***
	 * qTest : TC-175 Adding a Ship Address- Default Country CA site
	 * Description : This test validates the country name & country field 
	 * should not be editable for CA
	 * 
	 *     
	 */
	@Test
	public void testAddingAShipAddressDefaultCountryCASite_175(){
		if(country.equalsIgnoreCase("ca")){
			String expectedCountryName = "canada";
			String countryNameFromUI = null;
			sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
			sfHomePage.clickWelcomeDropdown();
			sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
			sfShippingInfoPage.clickAddANewShippingAddressLink();
			s_assert.assertFalse(sfShippingInfoPage.isCountryNameEditable(country), "Country field is editable at shipping profile page");
			countryNameFromUI = sfShippingInfoPage.getCountryName().toLowerCase();
			s_assert.assertTrue(countryNameFromUI.contains(expectedCountryName), "Expected country name is"+expectedCountryName+" but actual on UI is"+countryNameFromUI);
			sfShippingInfoPage.clickRodanAndFieldsLogo();
			sfShippingInfoPage.clickAllProducts();
			sfShippingInfoPage.selectFirstProduct();
			sfShippingInfoPage.checkoutThePopup();
			sfCheckoutPage = sfShippingInfoPage.checkoutTheCart();
			sfCheckoutPage.clickSaveButton();
			sfCheckoutPage.clickAddNewShippingAddressButton();
			s_assert.assertFalse(sfCheckoutPage.isCountryNameEditable(country), "Country filed is editable at checkout page");
			countryNameFromUI = sfCheckoutPage.getCountryName().toLowerCase();
			s_assert.assertTrue(countryNameFromUI.contains(expectedCountryName), "Expected country name at checkout page is"+expectedCountryName+" but actual on UI is"+countryNameFromUI);
			s_assert.assertAll();
		}
	}

	/***
	 * qTest : TC-174 Adding a Ship Address- Default Country US site
	 * Description : This test validates the country name & country field 
	 * should not be editable for US
	 * 
	 *     
	 */
	@Test// Incomplete
	public void testAddingAShipAddressDefaultCountryUSSite_174(){
		if(country.equalsIgnoreCase("us")){
			String expectedCountryName = "us";
			String countryNameFromUI = null;
			sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
			sfHomePage.clickWelcomeDropdown();
			sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
			sfShippingInfoPage.clickAddANewShippingAddressLink();
			s_assert.assertFalse(sfShippingInfoPage.isCountryNameEditable(country), "Country field is editable at shipping profile page");
			countryNameFromUI = sfShippingInfoPage.getCountryName().toLowerCase();
			s_assert.assertTrue(countryNameFromUI.contains(expectedCountryName), "Expected country name is"+expectedCountryName+" but actual on UI is"+countryNameFromUI);
			sfShippingInfoPage.clickRodanAndFieldsLogo();
			sfShippingInfoPage.clickAllProducts();
			sfShippingInfoPage.selectFirstProduct();
			sfShippingInfoPage.checkoutThePopup();
			sfCheckoutPage = sfShippingInfoPage.checkoutTheCart();
			sfCheckoutPage.clickSaveButton();
			sfCheckoutPage.clickAddNewShippingAddressButton();
			s_assert.assertFalse(sfCheckoutPage.isCountryNameEditable(country), "Country filed is editable at checkout page");
			countryNameFromUI = sfCheckoutPage.getCountryName().toLowerCase();
			s_assert.assertTrue(countryNameFromUI.contains(expectedCountryName), "Expected country name at checkout page is"+expectedCountryName+" but actual on UI is"+countryNameFromUI);
			s_assert.assertAll();
		}
	}

	/***
	 * qTest : TC-173 Default Ship Address displayed in checkout - Multiple Address
	 * Description : This test validates the default shipping profile at
	 * checkout page
	 *     
	 */
	@Test
	public void testDefaultShipAddressDisplayedInCheckoutMultipleAddress_173(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		String city = TestConstants.CITY_CA;
		String state = TestConstants.STATE_CA;
		String postalCode = TestConstants.POSTAL_CODE_CA;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String defaultShippingAddressName = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickAddANewShippingAddressLink();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfShippingInfoPage.checkMakeThisMyDefaultAddressChkBox();
		sfShippingInfoPage.clickSaveButtonOfShippingAddress();
		sfShippingInfoPage.clickUseAsEnteredButton();
		defaultShippingAddressName = sfShippingInfoPage.getDefaultShippingAddressName();
		s_assert.assertTrue(defaultShippingAddressName.contains(lastName), "Expected default shipping address name is "+lastName+" but actual on UI is "+defaultShippingAddressName);
		sfShippingInfoPage.clickRodanAndFieldsLogo();
		sfShippingInfoPage.clickAllProducts();
		sfShippingInfoPage.selectFirstProduct();
		sfShippingInfoPage.checkoutThePopup();
		sfCheckoutPage = sfShippingInfoPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		defaultShippingAddressName = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		s_assert.assertTrue(defaultShippingAddressName.contains(lastName), "Expected default shipping address name at checkout page is "+lastName+" but actual on UI is "+defaultShippingAddressName);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-371 Shipping Profile-Edit a ship address and cancel
	 * Description : This test edit the shipping profile ,click on cancel
	 * and validates it
	 *    
	 */
	@Test
	public void testEditAShippingAddressAndCancel_371(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String defaultShippingAddressName = null;
		String defaultShippingAddressNameAfterUpdate = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		defaultShippingAddressName = sfShippingInfoPage.getDefaultShippingAddressName();
		sfShippingInfoPage.clickEditLinkOfDefaultShippingAddress();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfShippingInfoPage.clickCancelButtonOfShippingAddress();
		defaultShippingAddressNameAfterUpdate = sfShippingInfoPage.getDefaultShippingAddressName();
		s_assert.assertTrue(defaultShippingAddressName.contains(defaultShippingAddressNameAfterUpdate), "Expected default shipping address name is "+defaultShippingAddressName+"Actual on UI is"+defaultShippingAddressNameAfterUpdate);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-172 Default Ship Address displayed in checkout - Single Address
	 * Description : This test validates the default shipping profile at
	 * checkout page
	 *     
	 */
	@Test
	public void testDefaultShipAddressDisplayedInCheckoutSingleAddress_172(){

	}

	/***
	 * qTest : TC-377 Delete shipping profile
	 * Description : This test validates delete shipping profile
	 *     
	 */
	@Test
	public void testDeleteShippingProfile_377(){

	}

	/***
	 * qTest : TC-378 Delete autoship shipping profile
	 * Description : This test validates delete shipping profile
	 *     
	 */
	@Test
	public void testDeleteAutoshipShippingProfile_378(){

	}

	/***
	 * qTest : TC-384 Autoship Information should appear on the Shipping profile
	 * Description : This test validates delete shipping profile
	 *     
	 */
	@Test
	public void testAutoshipInformationShouldAppearOnTheShippingProfilePage_384(){

	}

	/**
	 * qTest : TC-320 Shipping Profile-Edit a ship address and save
	 * Description: This test update the shipping profile details & validate it
	 * at shipping info page 
	 * 
	 */
	@Test
	public void testShippingProfileEditAShipAddressAndSave_320(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage=sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickEditLinkOfDefaultShippingAddress();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfShippingInfoPage.clickSaveButtonOfShippingAddress();
		s_assert.assertTrue(sfShippingInfoPage.isShippingProfilePresent(lastName), "Expected profile name is not present in Address list");
		s_assert.assertAll();
	}

}