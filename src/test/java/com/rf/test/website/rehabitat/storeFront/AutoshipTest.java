package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AutoshipTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-399 Update Autoship Cart- Edit Ship Address - PC
	 * Description : This test validates the updates in shipping address  on checkout page
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipCartEditShipAddressPC_399(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String updatedLastName = TestConstants.FIRST_NAME +  CommonUtils.getRandomWord(5);
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String textToAssertInURL = "autoship/cart";
		String shippingProfile = null;

		//Login to application.
		//sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		// Add shipping profile
		sfCheckoutPage.clickAddNewShippingAddressButton();
		sfCheckoutPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickNextbuttonOfShippingDetails();
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfCheckoutPage.clickEditLinkOfShippingAddress();
		shippingProfile = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		s_assert.assertTrue(shippingProfile.contains(lastName), "Shipping Profile is not added successfully at adhoc cart. Expected is :"+lastName+" But found is :"+shippingProfile);
		// Edit shipping profile
		randomWord = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME+randomWord;
		sfCheckoutPage.clickEditLinkOfShippingProfile();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1,addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage().contains(lastName),
				"Shipping profile does not get updated at checkout page");

		// Edit default Shipping Profile and Cancel changes
		sfCheckoutPage.clickEditLinkOfShippingProfile();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, updatedLastName, addressLine1,addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickCancelButton();
		s_assert.assertFalse(sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage().contains(updatedLastName),
				"Shipping profile get updated at checkout page even after clicking cancel button at checkout page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-422 Update Autoship- Add a billing address to new Profile - Invalid
	 * Description : This test validates the error and mandatory messages for invalid and insufficient Billing Address details
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipAddABillingProfileInvalid_422(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String textToAssertInURL = "autoship/cart";
		String cardType = TestConstants.CARD_TYPE;
		String cardNum = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String cvv =  TestConstants.CVV;
		String firstName = TestConstants.FIRST_NAME + randomWord;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String invalidPostalCode = TestConstants.INVALID_POSTAL_CODE;
		String phoneNumber = TestConstants.PHONE_NUMBER;

		//Login to application.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();

		// Adding New Billing Profile
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType,cardNum,cardName,cvv);

		// Click Next Button without filling any Billing Address details
		sfCheckoutPage.selectUseMyDeliveyAddressCheckbox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		s_assert.assertTrue(sfCheckoutPage.isErrrorMsgsForAllMandatoryFieldsArePresent(),
				"Mandatory Fields error messages are not present as expected");

		// Enter Billing details with Invalid postal code
		sfCheckoutPage.enterBillingAddressDetailsAtCheckoutForNewBillingProfile(firstName, lastName, addressLine1, addressLine2, city, state, invalidPostalCode, phoneNumber);
		s_assert.assertTrue(sfCheckoutPage.getErrorMessageForInvalidPostalCode().contains(TestConstants.INVALID_POSTAL_CODE_MSG),
				"Error msg for Invalid postal code is not present as expected");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-418 Update Autoship- Negative - User can't Add a billing profile
	 * Description : This test validates that User can't update the Billing profile from 
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipNegativeUserCantAddABillingProfile_418(){
		String textToAssertInURL = "autoship/cart";
		String currentURL = null;
		//Login to application.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password, true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		s_assert.assertFalse(sfAutoshipCartPage.isBillingSectionPresent(),"Billing Updation option is not present at Autoship cart page");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-407 Update Autoship Cart- User can't Edit Shipment section on this page
	 * Description : This test validates that No shipping section is present on Autoship cart page
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipCartUserCantEditShipmentSectionOnThisPage_407(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		//Login to application.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfAutoshipCartPage.isCRPAutoshipHeaderPresentOnCartPage(),"CRP Autoship header is not present on Autoship cart page");
		s_assert.assertFalse(sfAutoshipCartPage.isShippingSectionPresent(),"Shipping section is present at autoship cart page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-450 Edit PC Perks
	 * Description : This test validates the PC perks cart page by navigating to it from Welcome drop down
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testEditPCPerks_450(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		//Login to application.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.navigateToEditPCPerksPage();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfHomePage.isPCPerksCartHeaderPresentOnCartPage(),"PC Perks cart header is not present");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-425 Update Autoship- Add a billing address to Existing Profile - Invalid
	 * Description : This test validates the error and mandatory messages for invalid and insufficient Billing Address details for existing billing profile
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipAddABillingAddressToExistingProfileInvalid_425(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String textToAssertInURL = "autoship/cart";
		String firstName = TestConstants.FIRST_NAME + randomWord;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String invalidPostalCode = TestConstants.INVALID_POSTAL_CODE;
		String phoneNumber = TestConstants.PHONE_NUMBER;

		//Login to application.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfDefaultBillingProfile();
		//  sfCheckoutPage.checkUseMyDeliveryAddressChkBoxForExistingBillingProfile();

		// Click Save Payment Button without filling any Billing Address details
		sfCheckoutPage.clearAllFieldsOfBillingAddressDetailsForExistingBillingProfile();
		sfCheckoutPage.clickSavePaymentButton();
		s_assert.assertTrue(sfCheckoutPage.isErrrorMsgsForAllMandatoryFieldsForBillingAddressArePresent(),
				"Mandatory Address Fields error messages for existing profile are not present as expected.");

		// Enter Billing Details with invalid postal code
		sfCheckoutPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, addressLine1, addressLine2, city, state, invalidPostalCode, phoneNumber);
		s_assert.assertTrue(sfCheckoutPage.getErrorMessageForInvalidPostalCode().contains(TestConstants.INVALID_POSTAL_CODE_MSG),"Error msg for Invalid postal code is not present as expected");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-333 Cart Page- ADD MORE ITEMS button - PC
	 * Description : This test validates the all products page after clicking 'ADD MORE ITEMS' button on Autoship cart page
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testCartPageAddMoreItemsButtonPC_333(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		String urlToAssertForProductsPage = "/All-Skincare";
		String productPageTitle = null;
		String expectedPageTile = TestConstants.TITLE_OF_ALL_SKINCARE_PAGE;
		//Login to application.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfShopSkinCarePage = sfAutoshipCartPage.clickAddMoreItemsBtn();
		currentURL = sfShopSkinCarePage.getCurrentURL();
		productPageTitle = sfShopSkinCarePage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(urlToAssertForProductsPage), "Expected URL should contain "+urlToAssertForProductsPage+" but actual on UI is "+currentURL);
		s_assert.assertTrue(productPageTitle.contains(expectedPageTile), "Product page title is not found as expected. Expected : " + expectedPageTile + ". Actual : " + productPageTitle);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-484 Cart Page- PC/Consultant Autoship
	 * Description : This test validates the cart page and checkout for PC/Consultant Autoship
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyCartAndCheckoutPageForuser_484(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		String checkoutPageURL = "delivery-account";
		double subtotalAtAutoshipCart;
		double newSubtotalAtAutoshipCart;
		//Login to application.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipCartPage = sfHomePage.navigateToEditPCPerksPage();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" on Autoship cart page but actual on UI is "+currentURL);
		subtotalAtAutoshipCart = sfAutoshipCartPage.getSubtotalofItemsAtCart();
		sfShopSkinCarePage = sfAutoshipCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_PC_PERKS);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		newSubtotalAtAutoshipCart= sfAutoshipCartPage.getSubtotalofItemsAtCart();
		s_assert.assertTrue(newSubtotalAtAutoshipCart>subtotalAtAutoshipCart,"Product is not been added to PC Autoship cart page.");
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(checkoutPageURL), "Expected URL should contain "+checkoutPageURL+" on checkout page but actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-398 Update Autoship Cart- Edit Ship Address - Consultant
	 * Description : This test validates the updates in shipping address  on checkout page
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipCartEditShipAddressConsultant_398(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String updatedLastName = TestConstants.FIRST_NAME +  CommonUtils.getRandomWord(5);
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String textToAssertInURL = "autoship/cart";
		String defaultShippingAddress = null;
		String shippingProfile = null;
		//Login to application.
		//sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		// Add shipping profile
		sfCheckoutPage.clickAddNewShippingAddressButton();
		sfCheckoutPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickNextbuttonOfShippingDetails();
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfCheckoutPage.clickEditLinkOfShippingAddress();
		shippingProfile = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		s_assert.assertTrue(shippingProfile.contains(lastName), "Shipping Profile is not added successfully at adhoc cart. Expected is :"+lastName+" But found is :"+shippingProfile);
		// Edit shipping profile
		randomWord = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME+randomWord;
		sfCheckoutPage.clickEditLinkOfShippingProfile();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1,addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		defaultShippingAddress = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		s_assert.assertTrue(defaultShippingAddress.contains(lastName),
				"Shipping profile does not get updated at checkout page. Expected : " + lastName + " . Actual on UI : " + defaultShippingAddress);
		sfCheckoutPage.clickEditLinkOfShippingProfile();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, updatedLastName, addressLine1,addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickCancelButton();
		s_assert.assertFalse(sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage().contains(updatedLastName),
				"Shipping profile get updated at checkout page even after clicking cancel button at checkout page");
		s_assert.assertAll();
	}

	/* qTest : TC-400 Update Autoship Cart- Edit a Billing Profile - Consultant
	 * Description : This method validates the change in billing profile on checkout page for Consultant
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipCartEditABillingProfileConsultant_400(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String cardName = TestConstants.CARD_NAME + randomWord;
		String cardNum = TestConstants.CARD_NUMBER_2;
		String textToAssertInURL = "autoship/cart";
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String updatedAddressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_US;
		String updatedAddressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_US;
		String updatedCity = TestConstants.SECOND_CITY_US;
		String updatedPostalCode = TestConstants.SECOND_POSTAL_CODE_US;
		String stateAbbreviation = TestConstants.STATE_US_ABBREVIATION;
		String billingDetailsOnUI = null;
		String cardLastName = null;
		String defaultBillingProfileName = null;

		//Login to application.
		//sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();

		// Add new Billing Profile initially with Specific Address Type 1 
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(TestConstants.CARD_TYPE, cardNum, cardName, TestConstants.CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.enterBillingAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfCheckoutPage.getLastName(cardName);
		s_assert.assertTrue(sfCheckoutPage.isNewBillingDetailsVisibleOnUI(cardLastName),"New Billing Details do not get updated as Default Billing details on Checkout Page");
		//Editing Billing Profile and entering Specific Address Type 2 and Save payment
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.clickEditLinkOfDefaultBillingProfile();
		//  sfCheckoutPage.checkUseMyDeliveryAddressChkBoxForExistingBillingProfile();
		sfCheckoutPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, updatedAddressLine1, updatedAddressLine2, updatedCity, state, updatedPostalCode, phoneNumber);
		sfCheckoutPage.clickSavePaymentButton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		billingDetailsOnUI = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		// Asserting Billing Profile Name and Updated Address Fields
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"Billing Profile Name is not found as expected after Updating Billing Address Details. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedAddressLine1),"Address Line 1 : " + updatedAddressLine1 + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedCity),"City : " + updatedCity + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedPostalCode),"PostalCode : " + updatedPostalCode + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in Actual Billing Address");
		//Editing Billing Profile and entering Specific Address Type 1 and cancel payment
		sfCheckoutPage.clickEditLinkOfDefaultBillingProfile();
		//  sfCheckoutPage.checkUseMyDeliveryAddressChkBoxForExistingBillingProfile();
		sfCheckoutPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickCancelButton();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		billingDetailsOnUI = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		//Asserting initial Address after Clicking Cancel Button
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"Billing Profile name is not found as expected after clicking cancel Button for Billing Address Updation. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedAddressLine1),"Address Line 1 : " + updatedAddressLine1 + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedCity),"City : " + updatedCity + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedPostalCode),"PostalCode : " + updatedPostalCode + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in Actual Billing Address");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-334 Cart Page- Update Billing/Shipping Address link - Consultant
	 * Description : This test validates the presence of updating billing/shipping Address link after clicking Autoship cart
	 * Note : For Updating billing/shipping Address, No direct link is present after Autoship cart. 
	 *        Need to click on pc perks checkout and then the link are available for Billing and shipping updation
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testCartPageUpdateBillingShippingAddressLinkConsultant_334(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		//Login to application.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipCartPage = sfHomePage.navigateToEditCRPPage();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfAutoshipCartPage.isCRPAutoshipHeaderPresentOnCartPage(),"CRP Autoship header is not present");
		sfCheckoutPage = sfAutoshipCartPage.clickOnCRPCheckoutButton();
		s_assert.assertTrue(sfCheckoutPage.isShippingLinkPresentAtCheckoutPage(),"Shipping link is not present at checkout page");
		s_assert.assertTrue(sfCheckoutPage.isBillingLinkPresentAtCheckoutPage(),"Billing link is not present at checkout page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-416 Update Autoship- Add a billing profile - Consultant
	 * Description : This test adds and validates new Autoship billing profile for consultant user.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipAddAbillingProfileConsultant_416(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		String randomWord = CommonUtils.getRandomWord(5);
		String cardType = TestConstants.CARD_TYPE;
		String cardNum = TestConstants.CARD_NUMBER_2;
		String cardName = TestConstants.CARD_NAME + randomWord;
		String cvv =  TestConstants.CVV;
		String profileLastName = null;
		String defaultBillingProfileName = null;
		//Login to application.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNum, cardName, cvv);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		profileLastName = sfCheckoutPage.getLastName(cardName);
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		s_assert.assertTrue(defaultBillingProfileName.contains(profileLastName),"New Billing Profile Details do not get updated. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForConsulatntCRP();
		//		sfCheckoutPage.selectCheckboxForPoliciesAndProcedures();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		sfCheckoutPage.clickOnAutoshipCartLink();
		sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		s_assert.assertTrue(defaultBillingProfileName.contains(profileLastName),"Billing details is not found as expected. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-417 Update Autoship- Use can't Add a billing profile - PC
	 * Description : This test validates that Updating link for billing and shipping profile is not present on Autoship cart page.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipUserCantAddABillingProfilePC_417(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";

		//Login to application.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),  password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		s_assert.assertFalse(sfAutoshipCartPage.isShippingSectionPresent(),"Shipping Updation option is not present at Autoship cart page");
		s_assert.assertFalse(sfAutoshipCartPage.isBillingSectionPresent(),"Billing Updation option is not present at Autoship cart page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-449 Consultant Autoship Status- Cancel CRP
	 * Description : This test validates the Cancel CRP functionality for Consulatnt user.
	 * 
	 *     
	 */
	@Test(enabled=false)//needs user
	public void testConsultantAutoshipStatusCancelCRP_449(){
		//Login to application.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		s_assert.assertTrue(sfAutoshipStatusPage.getCurrentCRPStatus().contains("Enrolled"),"Consultant is not enrolled into CRP yet");
		sfAutoshipStatusPage.clickCancelCRPLink();
		sfAutoshipStatusPage.clickCancelCRPButton();
		s_assert.assertTrue(sfAutoshipStatusPage.getActionSucccessMsgOnAutoshipStatusPage().contains(TestConstants.CANCELLED_CRP_ORDER_SUCCESS_MESSAGE),"Cancelled CRP Order Success Message is not present as expected");
		s_assert.assertTrue(sfAutoshipStatusPage.isEnrollIntoCRPButtonPresent(),"Enroll in CRP Button is not present After cancelling CRP for consulatnt");

		// Enrolling Consultant in CRP
		sfAutoshipStatusPage.clickEnrollInCRPButton();
		sfHomePage.addFirstProductForCRPCheckout();
		sfCheckoutPage = sfHomePage.checkoutCRPBag();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForConsulatntCRP();
		//		sfCheckoutPage.selectCheckboxForPoliciesAndProcedures();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isCRPOrderConfirmedSuccessMsgAppeared(),"CRP Order confirmed success messge is not appeared");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		sfHomePage.clickWelcomeDropdown();
		sfCheckoutPage.navigateToAutoshipStatusPage();
		s_assert.assertTrue(sfAutoshipStatusPage.getCurrentCRPStatus().contains("Enrolled"),"Consultant does not get enrolled in CRP");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-405 Consultant Autoship Cart (CRP) Enrollment - CRP Reminder Banner
	 * Description : This test validates the CRP enrollment from CRP Reminder banner
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantAutoshipCartCRPEnrollmentCRPReminderBanner_405(){
		String status = null;
		timeStamp = CommonUtils.getCurrentTimeStamp();
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		email = TestConstants.FIRST_NAME+timeStamp+TestConstants.EMAIL_SUFFIX;
		lastName = TestConstants.LAST_NAME;
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting kit");
		sfHomePage.chooseProductFromKitPage();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
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
		// Click Set up CRP from Banner
		sfHomePage.clickSetUpCRP();
		sfHomePage.addFirstProductForCRPCheckout();
		sfCheckoutPage = sfHomePage.checkoutCRPBag();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForConsulatntCRP();
		//  sfCheckoutPage.selectCheckboxForPoliciesAndProcedures();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isCRPOrderConfirmedSuccessMsgAppeared(),"CRP Order confirmed success messge is not appeared");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		sfCheckoutPage.clickWelcomeDropdown();
		sfOrdersPage = sfCheckoutPage.navigateToOrdersPage();
		s_assert.assertTrue(sfOrdersPage.isAutoshipOrderHistoryTableAppeared(),"Autoship Order history Table is not present on orders page");
		status = sfOrdersPage.getStatusOfFirstOrderPresentInAutoshipOrderHistory();
		s_assert.assertTrue(status.toLowerCase().contains("active"),"Status of CRP is not found as expected. Expected : Active. Actual : "+status);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-448 Consultant Autoship Status
	 * 
	 * Description : This tests validate the tag at autoship status page
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantAutoshipStatus_448(){
		String currentCRPStatus = "Current CRP Status";
		String nextBillAndShipDate = "Next Bill & Ship Date";
		String currentPulseStatus = "Current Subscription Status";
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		s_assert.assertTrue(sfAutoshipStatusPage.isTextVisible(currentCRPStatus), currentCRPStatus+" tag is not present for CRP");
		s_assert.assertTrue(sfAutoshipStatusPage.isTextVisible(nextBillAndShipDate), nextBillAndShipDate+" tag is not present for CRP");
		s_assert.assertTrue(sfAutoshipStatusPage.isTextVisible(currentPulseStatus), currentPulseStatus+" tag is not present for pulse");
		s_assert.assertTrue(sfAutoshipStatusPage.isCancelMyCrpLinkVisible(),"Cancel My CRP link is not visible");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-455 Pending Autoship Details- Schedule Date - 30 Days
	 * 
	 * Description : This tests delay autoship for PC user by 30 days.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testDelayPCAutoshipBy30Days_455(){
		//same as TC-440
	}

	/***
	 * qTest : TC-456 Pending Autoship Details- Schedule Date - 60 Days
	 * 
	 * Description : This tests delay autoship for PC user by 60 days.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testDelayPCAutoshipBy60Days_456(){
		// same as TC-441
	}

	/***
	 * qTest : TC-420 Update Autoship- Add a billing address to new Profile - Consultant
	 * Description : This test adds and validates new Autoship billing profile with new billing address for Consultant user.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipAddAbillingAddressToNewProfile_420(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		String randomWord = CommonUtils.getRandomWord(5);
		String cardType = TestConstants.CARD_TYPE;
		String cardNum = TestConstants.CARD_NUMBER_2;
		String cardName = TestConstants.CARD_NAME + randomWord;
		String cvv =  TestConstants.CVV;
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String cardLastName = null;
		String defaultBillingProfileName = null;

		//Login to application and navigated to Checkout from Autoship cart page 
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();

		// Adding new Billing profile with new Billing Address
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNum, cardName, cvv);
		sfCheckoutPage.selectUseMyDeliveyAddressCheckbox();
		sfCheckoutPage.enterBillingAddressDetailsAtCheckoutForNewBillingProfile(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);

		// Saving Billing Profile for Future Autoship and Verifying updates
		sfCheckoutPage.selectCheckboxForSavingProfileForFutureAutoship();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfCheckoutPage.getLastName(cardName);
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"New Billing Profile Details do not get updated. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);

		// Confirming order
		//		sfCheckoutPage.selectCheckboxForPoliciesAndProcedures();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForConsulatntCRP();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();

		// Navigate again to Autoship checkout to verify billing profile for future autoship.
		sfCheckoutPage.clickOnAutoshipCartLink();
		sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"Billing details is not found as expected for Autoship. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-326 Update Autoship Cart- Order Summary
	 * Description : This test add a new product in to cart, update quantity and remove it
	 * and validate all
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipCartOrderSummary_326(){
		int totalNoOfItemBeforeAdd = 0;
		int totalNoOfItemAfterAdd = 0;
		int totalNoOfItemAfterRemove = 0;
		int totalNoOfProducts = 0;
		int productnumber = 0;
		String productQuantity = null;
		String productQuantityToUpdate = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		totalNoOfItemBeforeAdd = sfAutoshipCartPage.getTotalNumberOfItemsFromCart();
		sfShopSkinCarePage = sfAutoshipCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		totalNoOfProducts = sfShopSkinCarePage.getTotalNoOfProduct();
		productnumber = CommonUtils.getRandomNum(1, totalNoOfProducts);
		sfShopSkinCarePage.addProductToCart(""+productnumber, TestConstants.ORDER_TYPE_CRP);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		totalNoOfItemAfterAdd =  sfAutoshipCartPage.getTotalNumberOfItemsFromCart();
		while(true){
			if(totalNoOfItemBeforeAdd==totalNoOfItemAfterAdd){
				sfAutoshipCartPage.clickAddMoreItemsBtn();
				sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
				productnumber = CommonUtils.getRandomNum(1, totalNoOfProducts);
				sfShopSkinCarePage.addProductToCart(""+productnumber, TestConstants.ORDER_TYPE_CRP);
				sfShopSkinCarePage.checkoutTheCartFromPopUp();
				totalNoOfItemAfterAdd =  sfAutoshipCartPage.getTotalNumberOfItemsFromCart();
				continue;
			}else{
				break;
			}
		}
		totalNoOfItemAfterAdd =  sfAutoshipCartPage.getTotalNumberOfItemsFromCart();
		s_assert.assertTrue(totalNoOfItemAfterAdd>totalNoOfItemBeforeAdd, "Added item from PDP is not present into cart");
		productQuantity = sfAutoshipCartPage.getProductQuantityFromAutoshipCart("2");
		productQuantityToUpdate = sfAutoshipCartPage.updateQuantityByOne(productQuantity).trim();
		sfAutoshipCartPage.enterProductQuantityAtAutoshipCart("2", productQuantityToUpdate);
		sfAutoshipCartPage.updateQuantityAtAutoshipCart("2");
		productQuantity = sfAutoshipCartPage.getProductQuantityFromAutoshipCart("2").trim();
		s_assert.assertTrue(productQuantity.equals(productQuantityToUpdate), "Product quantity is not updated successfully");
		sfAutoshipCartPage.removeAnItemFromAutoshipCart("2");
		totalNoOfItemAfterRemove =  sfAutoshipCartPage.getTotalNumberOfItemsFromCart();
		s_assert.assertTrue(totalNoOfItemAfterRemove==totalNoOfItemBeforeAdd, "Added item from PDP is not present into cart");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-327 Update Autoship Cart page
	 * Description : This test add a new product in to cart, update quantity and remove it
	 * and validate all
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipCart_327(){
		//same as TC-326
	}


	/***
	 * qTest : TC-510 User does not select the Update CTA after making edits to autoship cart
	 * Description : This test validates the Autoship cart page changes for PC/Consultant Autoship
	 * page update and without click on save.
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyUpdateChangesOnAutoShipCartWithoutClickSave_510(){
		String shippingProfileNameBeforeEdit = null;
		String billingProfileNameBeforeEdit = null;
		String shippingProfileNameFromUI = null;
		String billingProfileNameFromFromUI= null;
		//Login to application.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipCartPage=sfHomePage.navigateToEditCRPPage();
		sfCheckoutPage = sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		shippingProfileNameBeforeEdit = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		sfCheckoutPage.clickAddressBookBtn();
		sfCheckoutPage.clickUseThisAddressBtnAndReturnProfileName("1");
		sfCheckoutPage.clickShippingDetailsNextbutton();
		billingProfileNameBeforeEdit = sfCheckoutPage.getDefaultBillingProfileName();
		sfCheckoutPage.clickUseSavedCardBtnOnly();
		sfCheckoutPage.clickUseThesePaymentDetailsAndReturnBillingProfileName("1");
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		sfCheckoutPage.clickAutoshipLink();
		sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		shippingProfileNameFromUI = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		shippingProfileNameBeforeEdit = sfCheckoutPage.getLastName(shippingProfileNameBeforeEdit);
		s_assert.assertTrue(shippingProfileNameFromUI.contains(shippingProfileNameBeforeEdit), "Expected shipping profile name is "+shippingProfileNameBeforeEdit+" but actual on UI is "+shippingProfileNameFromUI);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		billingProfileNameFromFromUI = sfCheckoutPage.getDefaultBillingProfileName();
		billingProfileNameBeforeEdit = sfCheckoutPage.getLastName(billingProfileNameBeforeEdit);
		s_assert.assertTrue(billingProfileNameFromFromUI.contains(billingProfileNameBeforeEdit), "Expected billing profile name is "+billingProfileNameBeforeEdit+" but actual on UI is "+billingProfileNameFromFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-353 Consultant Autoship Cart- Pulse subscription status
	 * Description : This test validates Next bill date and Current Subscription Status label
	 * for pulse
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantAutoshipCartPulseSubscriptionStatus_353(){
		String nextBillAndShipDate = "Next Bill";
		String currentPulseStatus = "Current Subscription Status";
		//sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		s_assert.assertTrue(sfAutoshipStatusPage.isTextVisible(currentPulseStatus), currentPulseStatus+" tag is not present for pulse");
		currentPulseStatus = sfAutoshipStatusPage.getCurrentPulseStatus();
		s_assert.assertTrue(sfAutoshipStatusPage.getCurrentCRPStatus().contains("Enrolled"),"Consultant is not enrolled into CRP yet");
		s_assert.assertTrue(currentPulseStatus.contains("ACTIVE"),"Consultant is not enrolled into Pulse yet expected 'ACTIVE' and Actual"+currentPulseStatus);
		s_assert.assertTrue(sfAutoshipStatusPage.isTextVisible(nextBillAndShipDate), nextBillAndShipDate+" tag is not present for pulse");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-336 Cart Page- Total SV and Additional SV
	 * Description : This test validates the threshold message for autoship 
	 *     
	 */
	@Test(enabled=true)
	public void testCartPageTotalSVAndAdditionalSV_336(){
		String thresholdMessage = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfAutoshipCartPage = sfHomePage.clickAutoshipLink();
		thresholdMessage = sfAutoshipCartPage.getThresholdMessageWhileRemovingProductFromAutoshipCart();
		s_assert.assertTrue(thresholdMessage.contains("minimum commitment"), "Expected threshold message should contain 'minimum commitment' for < 100$ SV but actual on UI is "+thresholdMessage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-396 Add to Autoship Cart - Consultant
	 * Description : This test add a new product in to cart through PLP, Quick View Popup, PDP
	 * and validate all
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testAddToAutoshipCartConsultant_396(){
		String productName = null;
		String productQuantity = null;
		String updatedQuantity = null;
		String updatedQuantityFromUI = null;
		boolean isProductPresent = false;
		//sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		sfAutoshipCartPage = sfShopSkinCarePage.clickAutoshipLink();
		if(sfAutoshipCartPage.isProductAddedToCartPresentOnCartPage(productName)){
			productQuantity = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim(); 
			isProductPresent = true;
		}
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfAutoshipCartPage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_CRP);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutPopupDisplayed(),"Expected checkout popup is not displayed while adding product from PLP");
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		if(isProductPresent){
			updatedQuantity = sfAutoshipCartPage.updateQuantityByOne(productQuantity).trim();
			updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
			s_assert.assertTrue(updatedQuantityFromUI.equals(updatedQuantity), "Added item from PLP is not present into cart");
		}else{
			updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
			s_assert.assertTrue(sfAutoshipCartPage.isProductAddedToCartPresentOnCartPage(productName), "Added item from PLP is not present into cart");
		}
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfAutoshipCartPage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct(TestConstants.PRODUCT_NUMBER);
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(TestConstants.ORDER_TYPE_CRP);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutPopupDisplayed(),"Expected checkout popup is not displayed while adding product from Quick View popup");
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		updatedQuantity = sfAutoshipCartPage.updateQuantityByOne(updatedQuantityFromUI).trim();
		updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
		s_assert.assertTrue(updatedQuantityFromUI.equals(updatedQuantity), "Added item from quick view popup is not present into cart. Expected Quantity : " + updatedQuantity + ". Actual Quantity : " + updatedQuantityFromUI);
		//From product page
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfAutoshipCartPage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage(TestConstants.PRODUCT_NUMBER);
		sfProductDetailPage.addProductToCartFromProductDetailPageAfterLogin(TestConstants.ORDER_TYPE_CRP);
		s_assert.assertTrue(sfProductDetailPage.isCheckoutPopupDisplayed(),"Expected checkout popup is not displayed while adding product from Quick View popup");
		sfProductDetailPage.checkoutTheCartFromPopUp();
		updatedQuantity = sfAutoshipCartPage.updateQuantityByOne(updatedQuantityFromUI).trim();
		updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
		s_assert.assertTrue(updatedQuantityFromUI.equals(updatedQuantity), "Added item from PDP is not present into cart.  Expected Quantity : " + updatedQuantity + ". Actual Quantity : " + updatedQuantityFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-397 Add to Autoship Cart - PC
	 * Description : This test add a new product in to cart through PLP, Quick View Popup, PDP
	 * and validate all
	 *     
	 */
	@Test(enabled=true)
	public void testAddToAutoshipCartPC_397(){
		String productName = null;
		String productQuantity = null;
		String updatedQuantity = null;
		String updatedQuantityFromUI = null;
		boolean isProductPresent = false;
		//sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER_PC_AUTOSHIP);
		sfAutoshipCartPage = sfShopSkinCarePage.clickAutoshipLink();
		if(sfAutoshipCartPage.isProductAddedToCartPresentOnCartPage(productName)){
			productQuantity = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim(); 
			isProductPresent = true;
		}
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfAutoshipCartPage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER_PC_AUTOSHIP, TestConstants.ORDER_TYPE_PC_PERKS);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutPopupDisplayed(),"Expected checkout popup is not displayed while adding product from PLP");
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		if(isProductPresent){
			updatedQuantity = sfAutoshipCartPage.updateQuantityByOne(productQuantity).trim();
			updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
			s_assert.assertTrue(updatedQuantityFromUI.equals(updatedQuantity), "Added item from PLP is not present into cart");
		}else{
			updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
			s_assert.assertTrue(sfAutoshipCartPage.isProductAddedToCartPresentOnCartPage(productName), "Added item from PLP is not present into cart");
		}
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfAutoshipCartPage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct(TestConstants.PRODUCT_NUMBER_PC_AUTOSHIP);
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(TestConstants.ORDER_TYPE_PC_PERKS);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutPopupDisplayed(),"Expected checkout popup is not displayed while adding product from Quick View popup");
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		updatedQuantity = sfAutoshipCartPage.updateQuantityByOne(updatedQuantityFromUI).trim();
		updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
		s_assert.assertTrue(updatedQuantityFromUI.equals(updatedQuantity), "Added item from quick view popup is not present into cart. Expected Quantity : " + updatedQuantity + ". Actual Quantity : " + updatedQuantityFromUI);
		//From product page
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfAutoshipCartPage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage(TestConstants.PRODUCT_NUMBER_PC_AUTOSHIP);
		sfProductDetailPage.addProductToCartFromProductDetailPageAfterLogin(TestConstants.ORDER_TYPE_PC_PERKS);
		s_assert.assertTrue(sfProductDetailPage.isCheckoutPopupDisplayed(),"Expected checkout popup is not displayed while adding product from Quick View popup");
		sfProductDetailPage.checkoutTheCartFromPopUp();
		updatedQuantity = sfAutoshipCartPage.updateQuantityByOne(updatedQuantityFromUI).trim();
		updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
		s_assert.assertTrue(updatedQuantityFromUI.equals(updatedQuantity), "Added item from PDP is not present into cart.  Expected Quantity : " + updatedQuantity + ". Actual Quantity : " + updatedQuantityFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-406 PC Autoship Cart- Maintenance and Threshold
	 * Description : This test validates the threshold message of PC for autoship 
	 *     
	 */
	@Test(enabled=true)
	public void testPCAutoshipCartMaintenanceAndThreshold_406(){
		String thresholdMessage = null;
		String expectedThresholdMessage = "please add minimum worth of $80 products excluding enrollment fee".toLowerCase();
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfAutoshipCartPage = sfHomePage.clickAutoshipLink();
		thresholdMessage = sfAutoshipCartPage.getThresholdMessageWhileRemovingProductFromAutoshipCart().toLowerCase();
		s_assert.assertTrue(thresholdMessage.contains(expectedThresholdMessage), "Expected threshold message is"+expectedThresholdMessage+" for Min SV but actual on UI is "+thresholdMessage);
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-421 Update Autoship- Add a billing address to new Profile - PC
	 * Description : This test adds and validates new Autoship billing profile with new billing address for PC user.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipAddAbillingAddressToNewProfilePC_421(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		String randomWord = CommonUtils.getRandomWord(5);
		String cardType = TestConstants.CARD_TYPE;
		String cardNum = TestConstants.CARD_NUMBER_2;
		String cardName = TestConstants.CARD_NAME + randomWord;
		String cvv =  TestConstants.CVV;
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String cardLastName = null;
		String defaultBillingProfileName = null;

		//Login to application and navigated to Checkout from Autoship cart page 
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();

		// Adding new Billing profile with new Billing Address
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNum, cardName, cvv);
		sfCheckoutPage.selectUseMyDeliveyAddressCheckbox();
		sfCheckoutPage.enterBillingAddressDetailsAtCheckoutForNewBillingProfile(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);

		// Saving Billing Profile and Verifying updates
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfCheckoutPage.getLastName(cardName);
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"New Billing Profile Details do not get updated. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);

		// Confirming order
		sfCheckoutPage.clickConfirmAutoshipOrderButton();

		// Navigate again to Autoship checkout to verify billing profile for future autoship.
		sfCheckoutPage.clickOnAutoshipCartLink();
		sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"Billing details is not found as expected for Autoship. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-403 Consultant Autoship Cart (CRP) Enrollment - Products section - Cancel
	 * Description : This test validates the enroll in crp popup for consultant who is not enrolled in crp.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantAutoshipCartCRPEnrollmentProductsSectionCancel_403(){
		String productNumber = TestConstants.PRODUCT_NUMBER;
		String orderType = TestConstants.ORDER_TYPE_CRP;
		sfHomePage.loginToStoreFront(consultantWithoutPulseAndWithoutCRP(), password, true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.addProductToCart(productNumber, orderType);
		// Add to CRP From Category Page
		s_assert.assertTrue(sfShopSkinCarePage.isEnrollNowPopupIsDisplayed(),"Enroll now popup does not get displayed after Clicking Add to CRP Button from Category Page");
		sfShopSkinCarePage.clickOptionFromEnrollNowPopup("No");
		s_assert.assertFalse(sfShopSkinCarePage.isEnrollNowPopupDisplayedAfterSelectingNoFromEnrollNowPopup(),"Enroll now popup is still getting displayed after Clicking No Option on Enroll now popup");
		// Add to CRP From Quick View Popup
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct(productNumber);
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(orderType);
		s_assert.assertTrue(sfShopSkinCarePage.isEnrollNowPopupIsDisplayed(),"Enroll now popup does not get displayed after Clicking Add to CRP Button from Quick View Popup");
		sfShopSkinCarePage.clickOptionFromEnrollNowPopup("No");
		s_assert.assertFalse(sfShopSkinCarePage.isEnrollNowPopupDisplayedAfterSelectingNoFromEnrollNowPopup(),"Enroll now popup is still getting displayed after Clicking No Option on Enroll now popup");
		// Add to CRP from PDP
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage(productNumber);
		sfProductDetailPage.addProductToCartFromProductDetailPageAfterLogin(orderType);
		s_assert.assertTrue(sfShopSkinCarePage.isEnrollNowPopupIsDisplayed(),"Enroll now popup does not get displayed after Clicking Add to CRP Button from PDP Page");
		sfShopSkinCarePage.clickOptionFromEnrollNowPopup("No");
		s_assert.assertFalse(sfShopSkinCarePage.isEnrollNowPopupDisplayedAfterSelectingNoFromEnrollNowPopup(),"Enroll now popup is still getting displayed after Clicking No Option on Enroll now popup");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-404 Consultant Autoship Cart (CRP) Enrollment - Products section - Confirm
	 * Description : This test validates the enrollment of Consultant in CRP from Category page, Quick view popup and PDP Page
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantAutoshipCartCRPEnrollmentProductsSectionConfirm_404(){
		String status = null;
		String productNumber = TestConstants.PRODUCT_NUMBER;
		String orderType = TestConstants.ORDER_TYPE_CRP;
		// Login to Application
		sfHomePage.loginToStoreFront(consultantWithoutPulseAndWithoutCRP(), password, true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.addProductToCart(productNumber, orderType);
		// Add to CRP From Category Page
		s_assert.assertTrue(sfShopSkinCarePage.isEnrollNowPopupIsDisplayed(),"Enroll now popup does not get displayed after Clicking Add to CRP Button from Category Page");
		sfShopSkinCarePage.clickOptionFromEnrollNowPopup("Yes");
		sfHomePage.addProductForCRPCheckout(TestConstants.PRODUCT_NUMBER);
		sfCheckoutPage = sfHomePage.checkoutCRPBag();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isCRPOrderConfirmedSuccessMsgAppeared(),"CRP Order confirmed success messge is not appeared");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		sfCheckoutPage.clickWelcomeDropdown();
		sfOrdersPage = sfCheckoutPage.navigateToOrdersPage();
		s_assert.assertTrue(sfOrdersPage.isAutoshipOrderHistoryTableAppeared(),"Autoship Order history Table is not present on orders page");
		status = sfOrdersPage.getStatusOfFirstOrderPresentInAutoshipOrderHistory();
		s_assert.assertTrue(status.contains("Active"),"Status of CRP is not found as expected. Expected : Active. Actual : "+status);

		// Removing Consultant from CRP Enrollment
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickCancelCRPLink();
		sfAutoshipStatusPage.clickCancelCRPButton();
		s_assert.assertTrue(sfAutoshipStatusPage.getActionSucccessMsgOnAutoshipStatusPage().contains(TestConstants.CANCELLED_CRP_ORDER_SUCCESS_MESSAGE),"Cancelled CRP Order Success Message is not present as expected");
		s_assert.assertTrue(sfAutoshipStatusPage.isEnrollIntoCRPButtonPresent(),"Enroll in CRP Button is not present After cancelling CRP for consulatnt");

		// Add to CRP from quick View Popup
		sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct(productNumber);
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(orderType);
		s_assert.assertTrue(sfShopSkinCarePage.isEnrollNowPopupIsDisplayed(),"Enroll now popup does not get displayed after Clicking Add to CRP Button from Quick View Popup");
		sfShopSkinCarePage.clickOptionFromEnrollNowPopup("Yes");
		sfHomePage.addProductForCRPCheckout(TestConstants.PRODUCT_NUMBER);
		sfHomePage.checkoutCRPBag();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isCRPOrderConfirmedSuccessMsgAppeared(),"CRP Order confirmed success messge is not appeared");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		sfCheckoutPage.clickWelcomeDropdown();
		sfCheckoutPage.navigateToOrdersPage();
		s_assert.assertTrue(sfOrdersPage.isAutoshipOrderHistoryTableAppeared(),"Autoship Order history Table is not present on orders page");
		status = sfOrdersPage.getStatusOfFirstOrderPresentInAutoshipOrderHistory();
		s_assert.assertTrue(status.contains("Active"),"Status of CRP is not found as expected. Expected : Active. Actual : "+status);

		// Removing Consultant from CRP Enrollment
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickCancelCRPLink();
		sfAutoshipStatusPage.clickCancelCRPButton();
		s_assert.assertTrue(sfAutoshipStatusPage.getActionSucccessMsgOnAutoshipStatusPage().contains(TestConstants.CANCELLED_CRP_ORDER_SUCCESS_MESSAGE),"Cancelled CRP Order Success Message is not present as expected");
		s_assert.assertTrue(sfAutoshipStatusPage.isEnrollIntoCRPButtonPresent(),"Enroll in CRP Button is not present After cancelling CRP for consulatnt");

		// Add to CRP from PDP
		sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage(productNumber);
		sfProductDetailPage.addProductToCartFromProductDetailPageAfterLogin(orderType);
		s_assert.assertTrue(sfShopSkinCarePage.isEnrollNowPopupIsDisplayed(),"Enroll now popup does not get displayed after Clicking Add to CRP Button from PDP Page");
		sfShopSkinCarePage.clickOptionFromEnrollNowPopup("Yes");
		sfHomePage.addProductForCRPCheckout(TestConstants.PRODUCT_NUMBER);
		sfHomePage.checkoutCRPBag();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isCRPOrderConfirmedSuccessMsgAppeared(),"CRP Order confirmed success messge is not appeared");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		sfCheckoutPage.clickWelcomeDropdown();
		sfOrdersPage = sfCheckoutPage.navigateToOrdersPage();
		s_assert.assertTrue(sfOrdersPage.isAutoshipOrderHistoryTableAppeared(),"Autoship Order history Table is not present on orders page");
		status = sfOrdersPage.getStatusOfFirstOrderPresentInAutoshipOrderHistory();
		s_assert.assertTrue(status.contains("Active"),"Status of CRP is not found as expected. Expected : Active. Actual : "+status);

		// Removing Consultant from CRP Enrollment
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.navigateToAutoshipStatusPage();
		sfAutoshipStatusPage.clickCancelCRPLink();
		sfAutoshipStatusPage.clickCancelCRPButton();
		s_assert.assertTrue(sfAutoshipStatusPage.getActionSucccessMsgOnAutoshipStatusPage().contains(TestConstants.CANCELLED_CRP_ORDER_SUCCESS_MESSAGE),"Cancelled CRP Order Success Message is not present as expected");
		s_assert.assertTrue(sfAutoshipStatusPage.isEnrollIntoCRPButtonPresent(),"Enroll in CRP Button is not present After cancelling CRP for consulatnt");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-541 PC Replenishment Order confirmation
	 * Description : This test validates the 
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPCReplenishmentOrderConfirmation_541(){
		String firstName = TestConstants.FIRST_NAME;
		String timeStamp = CommonUtils.getCurrentTimeStamp();
		String randomWords = CommonUtils.getRandomWord(5);
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String lastName = TestConstants.LAST_NAME+randomWords;
		String email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();

		// Enrolling PC User
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.enterQuantityOfProductAtCart("1", "2");
		sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(TestConstants.CARD_TYPE, TestConstants.CARD_NUMBER_2, TestConstants.CARD_NAME,TestConstants.CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectIAcknowledgePCChkBox();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isEmailConfirmationMsgPresentAsExpected(),"Confiaramtion Eamil Msg is not present after confirming order");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");  
	}


	/***
	 * qTest : TC-539 CRP Replenishment Order confirmation
	 * 
	 * Description : This test validates CRP order confirmation email
	 * when a consultant enroll in CRP from crp banner.
	 *     
	 */
	@Test(enabled=true)
	public void testEnrollInCRPAfterConsultantEnrollment_539(){
		String timeStamp = CommonUtils.getCurrentTimeStamp();
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String emailID = TestConstants.FIRST_NAME+timeStamp+TestConstants.EMAIL_SUFFIX;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		String status = null;

		//Enroll a consultant.
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, emailID, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting kit");
		sfHomePage.chooseProductFromKitPage();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'ENROLLMENT SUCCESSFUL' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		//Enroll consultant in CRP
		sfHomePage.clickSetUpCRP();
		sfHomePage.addFirstProductForCRPCheckout();
		sfCheckoutPage = sfHomePage.checkoutCRPBag();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForConsulatntCRP();
		/*sfCheckoutPage.selectIAcknowledgePCChkBox();*/
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isCRPOrderConfirmedSuccessMsgAppeared(),"CRP order confirmation text not present.");
		sfHomePage = sfCheckoutPage.clickRodanAndFieldsLogo();
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		//Verify autoship order details.
		s_assert.assertTrue(sfOrdersPage.isAutoshipOrderHistoryTableAppeared(),"Autoship Order history Table is not present on orders page");
		status = sfOrdersPage.getStatusOfFirstOrderPresentInAutoshipOrderHistory();
		s_assert.assertTrue(status.toLowerCase().contains("active"),"Status of CRP is not found as expected. Expected : Active. Actual : "+status);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-509 User selects Update CTA after making changes in the autoship cart
	 * Description : This test validates the Autoship cart page changes for PC/Consultant Autoship
	 * page Update and click on save.
	 */
	@Test(enabled=true)
	public void testVerifyUpdateChangesOnAutoShipCartAfterClickSave_509(){
		String shippingProfileNameFromAddressBook = null;
		String billingProfileNameFromSavedCard = null;
		String shippingProfileNameFromUI = null;
		String billingProfileNameFromFromUI= null;
		//Login to application.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipCartPage=sfHomePage.navigateToEditCRPPage();
		sfCheckoutPage = sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickAddressBookBtn();
		shippingProfileNameFromAddressBook = sfCheckoutPage.clickUseThisAddressBtnAndReturnProfileName("1");
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickUseSavedCardBtnOnly();
		billingProfileNameFromSavedCard = sfCheckoutPage.clickUseThesePaymentDetailsAndReturnBillingProfileName("1");
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForConsulatntCRP();
		//		sfCheckoutPage.selectIAcknowledgeChkBox();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		sfCheckoutPage.clickAutoshipLink();
		sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		shippingProfileNameFromUI = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		shippingProfileNameFromAddressBook = sfCheckoutPage.getLastName(shippingProfileNameFromAddressBook);
		s_assert.assertTrue(shippingProfileNameFromUI.contains(shippingProfileNameFromAddressBook), "Expected shipping profile name is "+shippingProfileNameFromAddressBook+" but actual on UI is "+shippingProfileNameFromUI);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		billingProfileNameFromFromUI = sfCheckoutPage.getDefaultBillingProfileName();
		billingProfileNameFromSavedCard = sfCheckoutPage.getLastName(billingProfileNameFromSavedCard);
		s_assert.assertTrue(billingProfileNameFromFromUI.contains(billingProfileNameFromSavedCard), "Expected billing profile name is "+billingProfileNameFromSavedCard+" but actual on UI is "+billingProfileNameFromFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-401 Update Autoship Cart- Edit a Billing Profile - PC
	 * Description : This method validates the change in billing profile on checkout page.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipCartEditABillingProfilePC_401(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String cardName = TestConstants.CARD_NAME + randomWord;
		String cardNum = TestConstants.CARD_NUMBER_2;
		String textToAssertInURL = "autoship/cart";
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String updatedAddressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_US;
		String updatedAddressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_US;
		String updatedCity = TestConstants.SECOND_CITY_US;
		String updatedPostalCode = TestConstants.SECOND_POSTAL_CODE_US;
		String stateAbbreviation = TestConstants.STATE_US_ABBREVIATION;
		String billingDetailsOnUI = null;
		String cardLastName = null;
		String defaultBillingProfileName = null;

		//Login to application.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),  password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();

		// Add new Billing Profile initially with Specific Address Type 1 
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(TestConstants.CARD_TYPE, cardNum, cardName, TestConstants.CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.enterBillingAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfCheckoutPage.getLastName(cardName);
		s_assert.assertTrue(sfCheckoutPage.isNewBillingDetailsVisibleOnUI(cardLastName),"New Billing Details do not get updated as Default Billing details on Checkout Page");
		//Editing Billing Profile and entering Specific Address Type 2 and Save payment
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.clickEditLinkOfDefaultBillingProfile();
		//  sfCheckoutPage.checkUseMyDeliveryAddressChkBoxForExistingBillingProfile();
		sfCheckoutPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, updatedAddressLine1, updatedAddressLine2, updatedCity, state, updatedPostalCode, phoneNumber);
		sfCheckoutPage.clickSavePaymentButton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		billingDetailsOnUI = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		// Asserting Billing Profile Name and Updated Address Fields
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"Billing Profile Name is not found as expected after Updating Billing Address Details. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedAddressLine1),"Address Line 1 : " + updatedAddressLine1 + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedCity),"City : " + updatedCity + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedPostalCode),"PostalCode : " + updatedPostalCode + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in Actual Billing Address");
		//Editing Billing Profile and entering Specific Address Type 1 and cancel payment
		sfCheckoutPage.clickEditLinkOfDefaultBillingProfile();
		//  sfCheckoutPage.checkUseMyDeliveryAddressChkBoxForExistingBillingProfile();
		sfCheckoutPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickCancelButton();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		billingDetailsOnUI = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		//Asserting initial Address after Clicking Cancel Button
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"Billing Profile name is not found as expected after clicking cancel Button for Billing Address Updation. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedAddressLine1),"Address Line 1 : " + updatedAddressLine1 + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedCity),"City : " + updatedCity + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedPostalCode),"PostalCode : " + updatedPostalCode + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in Actual Billing Address");
		s_assert.assertAll();
	}
	/* qTest : TC-423 Update Autoship- Add a billing address to Existing Profile - Consultant
	 * Description : This method edit the autoship billing address and validates it
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipAddAbillingAddressToExistingProfileConsultant_423(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String cardName = TestConstants.CARD_NAME + randomWord;
		String cardNum = TestConstants.CARD_NUMBER_2;
		String textToAssertInURL = "autoship/cart";
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String updatedAddressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_US;
		String updatedAddressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_US;
		String updatedCity = TestConstants.SECOND_CITY_US;
		String updatedPostalCode = TestConstants.SECOND_POSTAL_CODE_US;
		String stateAbbreviation = TestConstants.STATE_US_ABBREVIATION;
		String billingDetailsOnUI = null;
		String cardLastName = null;
		String defaultBillingProfileName = null;

		//Login to application.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();

		// Add new Billing Profile initially with Specific Address Type 1 
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(TestConstants.CARD_TYPE, cardNum, cardName, TestConstants.CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.enterBillingAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfCheckoutPage.getLastName(cardName);
		s_assert.assertTrue(sfCheckoutPage.isNewBillingDetailsVisibleOnUI(cardLastName),"New Billing Details do not get updated as Default Billing details on Checkout Page");
		//Editing Billing Profile and entering Specific Address Type 2 and Save payment
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.clickEditLinkOfDefaultBillingProfile();
		//  sfCheckoutPage.checkUseMyDeliveryAddressChkBoxForExistingBillingProfile();
		sfCheckoutPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, updatedAddressLine1, updatedAddressLine2, updatedCity, state, updatedPostalCode, phoneNumber);
		//  sfCheckoutPage.selectCheckboxForSavingExistingProfileForFutureAutoship();
		sfCheckoutPage.clickSavePaymentButton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		billingDetailsOnUI = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		// Asserting Billing Profile Name and Updated Address Fields
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"Billing Profile Name is not found as expected. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedAddressLine1),"Address Line 1 : " + updatedAddressLine1 + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedCity),"City : " + updatedCity + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedPostalCode),"PostalCode : " + updatedPostalCode + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in Actual Billing Address");
		// Confirming Order
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForConsulatntCRP();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		// Repeating AutoShip Checkout flow for Billing Profile Verification
		sfCheckoutPage.clickOnAutoshipCartLink();
		sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		billingDetailsOnUI = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		// Asserting Billing Profile Name and Updated Address Fields
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"Billing Profile Name is not found as expected. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedAddressLine1),"Address Line 1 : " + updatedAddressLine1 + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedCity),"City : " + updatedCity + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedPostalCode),"PostalCode : " + updatedPostalCode + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in Actual Billing Address");
		s_assert.assertAll();

	}

	/* qTest : TC-424 Update Autoship- Add a billing address to Existing Profile - PC
	 * Description : This method edit the autoship billing address and validates it for the PC user
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipAddABillingAddressToExistingProfilePC_424(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String cardName = TestConstants.CARD_NAME + randomWord;
		String cardNum = TestConstants.CARD_NUMBER_2;
		String textToAssertInURL = "autoship/cart";
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String updatedAddressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_US;
		String updatedAddressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_US;
		String updatedCity = TestConstants.SECOND_CITY_US;
		String updatedPostalCode = TestConstants.SECOND_POSTAL_CODE_US;
		String stateAbbreviation = TestConstants.STATE_US_ABBREVIATION;
		String billingDetailsOnUI = null;
		String cardLastName = null;
		String defaultBillingProfileName = null;

		//Login to application.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),  password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();

		// Add new Billing Profile initially with Specific Address Type 1 
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(TestConstants.CARD_TYPE, cardNum, cardName, TestConstants.CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.enterBillingAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfCheckoutPage.getLastName(cardName);
		s_assert.assertTrue(sfCheckoutPage.isNewBillingDetailsVisibleOnUI(cardLastName),"New Billing Details do not get updated as Default Billing details on Checkout Page");
		//Editing Billing Profile and entering Specific Address Type 2 and Save payment
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.clickEditLinkOfDefaultBillingProfile();
		//  sfCheckoutPage.checkUseMyDeliveryAddressChkBoxForExistingBillingProfile();
		sfCheckoutPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, updatedAddressLine1, updatedAddressLine2, updatedCity, state, updatedPostalCode, phoneNumber);
		//  sfCheckoutPage.selectCheckboxForSavingExistingProfileForFutureAutoship();
		sfCheckoutPage.clickSavePaymentButton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		billingDetailsOnUI = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		// Asserting Billing Profile Name and Updated Address Fields
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"Billing Profile Name is not found as expected. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedAddressLine1),"Address Line 1 : " + updatedAddressLine1 + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedCity),"City : " + updatedCity + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedPostalCode),"PostalCode : " + updatedPostalCode + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in Actual Billing Address");
		// Confirming Order
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		// Repeating AutoShip Checkout flow for Billing Profile Verification
		sfCheckoutPage.clickOnAutoshipCartLink();
		sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		billingDetailsOnUI = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		// Asserting Billing Profile Name and Updated Address Fields
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"Billing Profile Name is not found as expected. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedAddressLine1),"Address Line 1 : " + updatedAddressLine1 + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedCity),"City : " + updatedCity + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedPostalCode),"PostalCode : " + updatedPostalCode + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in Actual Billing Address");
		s_assert.assertAll();
	}

	/* Test Add and Edit autoship billing profile for PC 
	 * Description : This method validates the change in billing profile on checkout page for PC
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateAutoshipCartAddEditABillingProfilePC(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String cardName = TestConstants.CARD_NAME + randomWord;
		String cardNum = TestConstants.CARD_NUMBER_2;
		String textToAssertInURL = "autoship/cart";
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String updatedAddressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_US;
		String updatedAddressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_US;
		String updatedCity = TestConstants.SECOND_CITY_US;
		String updatedPostalCode = TestConstants.SECOND_POSTAL_CODE_US;
		String stateAbbreviation = TestConstants.STATE_US_ABBREVIATION;
		String billingDetailsOnUI = null;
		String cardLastName = null;
		String defaultBillingProfileName = null;

		//Login to application.
		//sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),  password,true);
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),  password,true);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		// Add new Billing Profile initially with Specific Address Type 1 
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.enterUserBillingDetails(TestConstants.CARD_TYPE, cardNum, cardName, TestConstants.CVV);
		sfCheckoutPage.enterBillingAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfCheckoutPage.getLastName(cardName);
		s_assert.assertTrue(sfCheckoutPage.isNewBillingDetailsVisibleOnUI(cardLastName),"New Billing Details do not get updated as Default Billing details on Checkout Page");
		//Editing Billing Profile and entering Specific Address Type 2 and Save payment
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.clickEditLinkOfDefaultBillingProfile();
		sfCheckoutPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, updatedAddressLine1, updatedAddressLine2, updatedCity, state, updatedPostalCode, phoneNumber);
		sfCheckoutPage.clickSavePaymentButton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		defaultBillingProfileName = sfCheckoutPage.getDefaultBillingProfileName();
		billingDetailsOnUI = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		// Asserting Billing Profile Name and Updated Address Fields
		s_assert.assertTrue(defaultBillingProfileName.contains(cardLastName),"Billing Profile Name is not found as expected after Updating Billing Address Details. Expected Profile Name : "+ cardName + ". Actual : " + defaultBillingProfileName);
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedAddressLine1),"Address Line 1 : " + updatedAddressLine1 + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedCity),"City : " + updatedCity + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,updatedPostalCode),"PostalCode : " + updatedPostalCode + " is not present in Actual Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in Actual Billing Address");
		s_assert.assertAll();
	}

	/* Test Place autoship order for Consultant 
	 * Description : This method placed an autoship order from consultant
	 *     
	 */
	@Test
	public void testPlacedAnAutoshipOrderForConsultant(){
		String currentURL = null;
		//sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_CRP);
		sfAutoshipCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUpForAutoship();
		sfCheckoutPage = sfAutoshipCartPage.clickOnCRPCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForConsulatntCRP();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("/orders"), "Expected URL should contain '/orders' but actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/* Test Place autoship order for PC 
	 * Description : This method placed an autoship order from PC
	 *     
	 */
	@Test
	public void testPlacedAnAutoshipOrderForPC(){
		String currentURL = null;
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_PC_PERKS);
		sfAutoshipCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUpForAutoship();
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectIAcknowledgePCChkBox();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("/orders"), "Expected URL should contain '/orders' but actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * Add to Autoship Cart - Consultant
	 * Description : This test add a new product in to cart through PLP
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testAddToAutoshipCartConsultant(){
		String productName = null;
		String productQuantity = null;
		String updatedQuantity = null;
		String updatedQuantityFromUI = null;
		boolean isProductPresent = false;
		//sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		sfAutoshipCartPage = sfShopSkinCarePage.clickAutoshipLink();
		if(sfAutoshipCartPage.isProductAddedToCartPresentOnCartPage(productName)){
			productQuantity = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim(); 
			isProductPresent = true;
		}
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfAutoshipCartPage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_CRP);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutPopupDisplayed(),"Expected checkout popup is not displayed while adding product from PLP");
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		if(isProductPresent){
			updatedQuantity = sfAutoshipCartPage.updateQuantityByOne(productQuantity).trim();
			updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
			s_assert.assertTrue(updatedQuantityFromUI.equals(updatedQuantity), "Added item from PLP is not present into cart");
		}else{
			updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
			s_assert.assertTrue(sfAutoshipCartPage.isProductAddedToCartPresentOnCartPage(productName), "Added item from PLP is not present into cart");
		}
		s_assert.assertAll();
	}

	/***
	 * Add to Autoship Cart - PC
	 * Description : This test add a new product in to cart through PLP
	 *     
	 */
	@Test(enabled=true)
	public void testAddToAutoshipCartPC(){
		String productName = null;
		String productQuantity = null;
		String updatedQuantity = null;
		String updatedQuantityFromUI = null;
		boolean isProductPresent = false;
		//sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER_PC_AUTOSHIP);
		sfAutoshipCartPage = sfShopSkinCarePage.clickAutoshipLink();
		if(sfAutoshipCartPage.isProductAddedToCartPresentOnCartPage(productName)){
			productQuantity = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim(); 
			isProductPresent = true;
		}
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfAutoshipCartPage.clickAllProductsCRP();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER_PC_AUTOSHIP, TestConstants.ORDER_TYPE_PC_PERKS);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutPopupDisplayed(),"Expected checkout popup is not displayed while adding product from PLP");
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		if(isProductPresent){
			updatedQuantity = sfAutoshipCartPage.updateQuantityByOne(productQuantity).trim();
			updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
			s_assert.assertTrue(updatedQuantityFromUI.equals(updatedQuantity), "Added item from PLP is not present into cart");
		}else{
			updatedQuantityFromUI = sfAutoshipCartPage.getQuantityOfSpecificProductFromAutoshipCart(productName).trim();
			s_assert.assertTrue(sfAutoshipCartPage.isProductAddedToCartPresentOnCartPage(productName), "Added item from PLP is not present into cart");
		}
		s_assert.assertAll();
	}

	/***
	 * This test checks the status of the consultant CRP and place CRP order
	 */
	@Test
	public void testPlaceConsultantAutoshipOrderAndVerifyDateAndStatus(){
		String orderNumber = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		s_assert.assertTrue(sfOrdersPage.getAutoshipStatus("2").equalsIgnoreCase("active"),"Autoship status is not ACTIVE");
		sfOrdersPage.clickRunAutoshipOrder("2");
		s_assert.assertTrue(sfOrdersPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertTrue(sfOrdersPage.isTextPresent("Order #"),"Order Number is not present on the confirmation page");
		orderNumber = sfOrdersPage.getOrderNumberAfterCheckout();
		sfOrdersPage.clickWelcomeDropdown();
		sfOrdersPage = sfOrdersPage.navigateToOrdersPage();
		sfOrdersPage.clickOrderNumber(orderNumber);
		s_assert.assertTrue(sfOrdersPage.getOrderTypeFromOrderDetailsTemplate().contains("consultant autoship"),"expected status of autoship order is incorrect");
		s_assert.assertAll();
	}

	/***
	 * This test checks the status of the pc autoship and place autoship order
	 */
	@Test
	public void testPlacePCAutoshipOrderAndVerifyDateAndStatus(){
		String orderNumber = null;
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		s_assert.assertTrue(sfOrdersPage.getAutoshipStatus("1").equalsIgnoreCase("active"),"Autoship status is not ACTIVE");
		sfOrdersPage.clickRunAutoshipOrder("1");
		s_assert.assertTrue(sfOrdersPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertTrue(sfOrdersPage.isTextPresent("Order #"),"Order Number is not present on the confirmation page");
		orderNumber = sfOrdersPage.getOrderNumberAfterCheckout();
		sfOrdersPage.clickWelcomeDropdown();
		sfOrdersPage = sfOrdersPage.navigateToOrdersPage();
		sfOrdersPage.clickOrderNumber(orderNumber);
		s_assert.assertTrue(sfOrdersPage.getOrderTypeFromOrderDetailsTemplate().contains("pc autoship"),"expected status of autoship order is incorrect");
		s_assert.assertAll();
	}

	/***
	 * This test checks the status of the consultant pulse and place pulse order
	 */
	@Test
	public void testPlacePulseAutoshipOrderAndVerifyDateAndStatus(){
		String orderNumber = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		s_assert.assertTrue(sfOrdersPage.getAutoshipStatus("1").equalsIgnoreCase("active"),"Autoship status is not ACTIVE");
		sfOrdersPage.clickRunAutoshipOrder("1");
		s_assert.assertTrue(sfOrdersPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertTrue(sfOrdersPage.isTextPresent("Order #"),"Order Number is not present on the confirmation page");
		orderNumber = sfOrdersPage.getOrderNumberAfterCheckout();
		sfOrdersPage.clickWelcomeDropdown();
		sfOrdersPage = sfOrdersPage.navigateToOrdersPage();
		sfOrdersPage.clickOrderNumber(orderNumber);
		s_assert.assertTrue(sfOrdersPage.getOrderTypeFromOrderDetailsTemplate().contains("pulse"),"expected status of autoship order is incorrect");
		s_assert.assertAll();
	}
}