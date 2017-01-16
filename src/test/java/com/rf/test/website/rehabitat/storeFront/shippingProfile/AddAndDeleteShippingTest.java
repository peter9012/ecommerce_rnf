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
	@Test(enabled=true)//Incomplete
	public void testAddShippingAddressFromMyAccount_77(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickAddANewShippingAddressLink();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2,city, state, postalCode, phoneNumber);
		sfShippingInfoPage.clickSaveButtonOfShippingAddress();
		sfShippingInfoPage.clickUseAsEnteredButtonOnPopUp();
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
	@Test(enabled=true)
	public void testCancellingAddingOftheShippingProfile_78(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickAddANewShippingAddressLink();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1,addressLine2, city, state, postalCode, phoneNumber);
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
	@Test(enabled=true)
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
	@Test(enabled=false) //AU test
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
			sfShopSkinCarePage = sfShippingInfoPage.clickAllProducts();
			sfShopSkinCarePage.selectFirstProduct();
			sfShopSkinCarePage.checkoutTheCartFromPopUp();
			sfCheckoutPage = sfShopSkinCarePage.checkoutTheCart();
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
	@Test(enabled=false) //CA test
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
			sfShopSkinCarePage = sfShippingInfoPage.clickAllProducts();
			sfShopSkinCarePage.selectFirstProduct();
			sfShopSkinCarePage.checkoutTheCartFromPopUp();
			sfCheckoutPage = sfShopSkinCarePage.checkoutTheCart();
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
	@Test(enabled=true)//TODO Incomplete
	public void testAddingAShipAddressDefaultCountryUSSite_174(){
		if(country.equalsIgnoreCase("us")){
			String expectedCountryName = "usa";
			String countryNameFromUI = null;
			sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
			sfHomePage.clickWelcomeDropdown();
			sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
			sfShippingInfoPage.clickAddANewShippingAddressLink();
			s_assert.assertFalse(sfShippingInfoPage.isCountryNameEditable(country), "Country field is editable at shipping profile page");
			countryNameFromUI = sfShippingInfoPage.getCountryName().toLowerCase();
			s_assert.assertTrue(countryNameFromUI.contains(expectedCountryName), "Expected country name is"+expectedCountryName+" but actual on UI is"+countryNameFromUI);
			sfShippingInfoPage.clickRodanAndFieldsLogo();
			sfShopSkinCarePage = sfShippingInfoPage.clickAllProducts();
			sfShopSkinCarePage.selectFirstProduct();
			sfShopSkinCarePage.checkoutTheCartFromPopUp();
			sfCheckoutPage = sfShopSkinCarePage.checkoutTheCart();
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
	@Test(enabled=true)
	public void testDefaultShipAddressDisplayedInCheckoutMultipleAddress_173(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String defaultShippingAddressName = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickAddANewShippingAddressLink();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1,addressLine2, city, state, postalCode, phoneNumber);
		sfShippingInfoPage.checkMakeThisMyDefaultAddressChkBox();
		sfShippingInfoPage.clickSaveButtonOfShippingAddress();
		sfShippingInfoPage.clickUseAsEnteredButtonOnPopUp();
		defaultShippingAddressName = sfShippingInfoPage.getDefaultShippingAddressName().toLowerCase();
		s_assert.assertTrue(defaultShippingAddressName.contains(lastName.toLowerCase()), "Expected default shipping address name is "+lastName.toLowerCase()+" but actual on UI is "+defaultShippingAddressName);
		sfShippingInfoPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfShippingInfoPage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		defaultShippingAddressName = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		s_assert.assertTrue(defaultShippingAddressName.contains(lastName.toLowerCase()), "Expected default shipping address name at checkout page is "+lastName.toLowerCase()+" but actual on UI is "+defaultShippingAddressName);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-371 Shipping Profile-Edit a ship address and cancel
	 * Description : This test edit the shipping profile ,click on cancel
	 * and validates it
	 *    
	 */
	@Test(enabled=true)
	public void testEditAShippingAddressAndCancel_371(){
		String randomWord = CommonUtils.getRandomWord(5);
		String randomWord1 = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String lastNameForFurtherUpdation = TestConstants.LAST_NAME+randomWord1;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String defaultShippingAddressName = null;
		String defaultShippingAddressNameAfterUpdate = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickEditLinkOfDefaultShippingAddress();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1,addressLine2, city, state, postalCode, phoneNumber);
		sfShippingInfoPage.clickSaveButtonOfShippingAddress();
		sfShippingInfoPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfShippingInfoPage.getAddressUpdateSuccessMsg().contains(TestConstants.SHIPPING_ADDRESS_UPDATE_MESSAGE),"Shipping Address update msg is not found as expected");
		s_assert.assertTrue(sfShippingInfoPage.isShippingProfilePresent(lastName), "Expected profile name is not present in Address list");
		defaultShippingAddressName = sfShippingInfoPage.getDefaultShippingAddressName();
		sfShippingInfoPage.clickEditLinkOfDefaultShippingAddress();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastNameForFurtherUpdation, addressLine1,addressLine2, city, state, postalCode, phoneNumber);
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
	@Test(enabled=false)//TODO
	public void testDefaultShipAddressDisplayedInCheckoutSingleAddress_172(){

	}

	/***
	 * qTest : TC-377 Delete shipping profile
	 * Description : This test validates delete shipping profile which is not a part of autoship
	 *     
	 */
	@Test(enabled=true)
	public void testDeleteShippingProfile_377(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickAddANewShippingAddressLink();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2,city, state, postalCode, phoneNumber);
		sfShippingInfoPage.clickSaveButtonOfShippingAddress();
		sfShippingInfoPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfShippingInfoPage.getActionSuccessMsgOnShippingInfoPage().contains(TestConstants.SHIPPING_ADDRESS_ADDED_MESSAGE),"Shipping Address created msg is not found as expected");
		s_assert.assertTrue(sfShippingInfoPage.isShippingProfilePresent(lastName),
				"Expected profile name is not present in Address list");
		sfShippingInfoPage.clickDeleteLinkForShippingProfile(lastName);
		sfShippingInfoPage.clickCancelButtonOnDeleteShippingPopup();
		s_assert.assertTrue(sfShippingInfoPage.isShippingProfilePresent(lastName),
				"Shipping profile is not present in the list after selecting cancel option from delete shipping popup");
		sfShippingInfoPage.clickDeleteLinkForShippingProfile(lastName);
		sfShippingInfoPage.clickDeleteButtonOnDeleteShippingPopup();
		s_assert.assertTrue(sfShippingInfoPage.getActionSuccessMsgOnShippingInfoPage().contains(TestConstants.SHIPPING_ADDRESS_REMOVED_MESSAGE),"Shipping Address removed msg is not found as expected");
		s_assert.assertFalse(sfShippingInfoPage.isShippingProfilePresent(lastName),
				"Shipping profile is present in the list after selecting delete option from delete shipping popup");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-378 Delete autoship shipping profile
	 * Description : This test verify the working of cancel & update my autoship button
	 * while delete a shipping profile selected for CRP
	 *     
	 */
	@Test(enabled=true)
	public void testDeleteAutoshipBillingProfile_378(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String autoShipShippingProfileLastName = null;
		String checkoutPageText = "Account Info";
		String currentURL = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_WITH_CRP_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickAddANewShippingAddressLink();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2,city, state, postalCode, phoneNumber);
		sfShippingInfoPage.checkMakeThisMyDefaultAddressChkBox();
		sfShippingInfoPage.clickSaveButtonOfShippingAddress();
		sfShippingInfoPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfShippingInfoPage.isShippingProfilePresent(lastName), "Newly added shipping profile is not present at shipping info page");
		autoShipShippingProfileLastName = sfShippingInfoPage.getAutoshipShippingProfileName().split(TestConstants.LAST_NAME.toLowerCase())[1].trim();
		sfShippingInfoPage.clickDeleteLinkForShippingProfile(autoShipShippingProfileLastName);
		sfShippingInfoPage.cancelDeleteAddressAndUpdateShippingAddressForAutoshipPopup();
		s_assert.assertTrue(sfShippingInfoPage.isShippingProfilePresent(autoShipShippingProfileLastName), "Existing shipping profile is not present at shipping info page & cancel button is not working");
		sfShippingInfoPage.clickDeleteLinkForShippingProfile(autoShipShippingProfileLastName);
		sfShippingInfoPage.clickUpdateMyAutoshipOnDeleteAddressAndUpdateShippingAddressForAutoshipPopup();
		currentURL = sfShippingInfoPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains("checkout") && sfShippingInfoPage.isTextPresent(checkoutPageText),"User is not redirecting to checkout page after clicked on update my autoship button");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-384 Autoship Information should appear on the Shipping profile
	 * Description :  This test verify the working of cancel & update my autoship button
	 * while delete a shipping profile selected for CRP 
	 *     
	 */
	@Test(enabled=true)
	public void testAutoshipInformationShouldAppearOnTheShippingProfilePage_384(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String autoShipShippingProfileLastName = null;
		String checkoutPageText = "Account Info";
		String currentURL = null;
		String CRPText = "Next CRP";
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_WITH_CRP_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickAddANewShippingAddressLink();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2,city, state, postalCode, phoneNumber);
		sfShippingInfoPage.checkMakeThisMyDefaultAddressChkBox();
		sfShippingInfoPage.clickSaveButtonOfShippingAddress();
		sfShippingInfoPage.clickUseAsEnteredButtonOnPopUp();
		autoShipShippingProfileLastName = sfShippingInfoPage.getAutoshipShippingProfileName().split(TestConstants.LAST_NAME.toLowerCase())[1].trim();
		s_assert.assertTrue(sfShippingInfoPage.isTextVisible(CRPText),CRPText+" text is not present");
		s_assert.assertTrue(sfShippingInfoPage.isAutoshipShippingProfileNamePresent(), "Autoship shipping profile name is not present");
		sfShippingInfoPage.clickDeleteLinkForShippingProfile(autoShipShippingProfileLastName);
		sfShippingInfoPage.cancelDeleteAddressAndUpdateShippingAddressForAutoshipPopup();
		s_assert.assertTrue(sfShippingInfoPage.isShippingProfilePresent(autoShipShippingProfileLastName), "Existing shipping profile is not present at shipping info page & cancel button is not working");
		sfShippingInfoPage.clickDeleteLinkForShippingProfile(autoShipShippingProfileLastName);
		sfShippingInfoPage.clickUpdateMyAutoshipOnDeleteAddressAndUpdateShippingAddressForAutoshipPopup();
		currentURL = sfShippingInfoPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains("checkout") && sfShippingInfoPage.isTextPresent(checkoutPageText),"User is not redirecting to checkout page after clicked on update my autoship button");
		s_assert.assertAll();
	}

	/**
	 * qTest : TC-320 Shipping Profile-Edit a ship address and save
	 * Description: This test update the shipping profile details & validate it
	 * at shipping info page 
	 * 
	 */
	@Test(enabled=true)
	public void testShippingProfileEditAShipAddressAndSave_320(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage=sfHomePage.navigateToShippingInfoPage();
		sfShippingInfoPage.clickEditLinkOfDefaultShippingAddress();
		sfShippingInfoPage.enterConsultantShippingDetails(firstName, lastName, addressLine1,addressLine2, city, state, postalCode, phoneNumber);
		sfShippingInfoPage.clickSaveButtonOfShippingAddress();
		sfShippingInfoPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfShippingInfoPage.getAddressUpdateSuccessMsg().contains(TestConstants.SHIPPING_ADDRESS_UPDATE_MESSAGE),"Shipping Address update msg is not found as expected");
		s_assert.assertTrue(sfShippingInfoPage.isShippingProfilePresent(lastName), "Expected profile name is not present in Address list");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-506 Add a Shipping Address on Update Autoship Cart
	 * Description : This test validates the flow of adding new Shipping address while checkout for autoship cart
	 * 
	 *     
	 */
	@Test(enabled=false)
	public void testAddAShippingAddressOnUpdateAutoshipCart_506(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String textToAssertInURL = "autoship/cart";
		String shippingAddressNameInAccountInfo = null;
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_WITH_CRP_USERNAME, password);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickAddNewShippingAddressButton();
		// Next button without filling Shipping address details
		sfCheckoutPage.clickShippingDetailsNextbutton();
		s_assert.assertTrue(sfCheckoutPage.isErrrorMsgsForAllMandatoryFieldsOfShippingAddressArePresent(),
				"Mandatory Fields error messages are not present as expected");
		sfCheckoutPage.enterNewShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		shippingAddressNameInAccountInfo = sfCheckoutPage.getShippingAddressNameFromShippingSection();
		s_assert.assertTrue(shippingAddressNameInAccountInfo.contains(lastName),
				"New Shipping Address added do not get updated on shipping section. Expected lastName : " + lastName + ".Actual Name :  " + shippingAddressNameInAccountInfo);
		s_assert.assertAll();
	}

}