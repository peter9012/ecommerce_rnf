package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AutoshipTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-399 Update Autoship Cart- Edit Ship Address - PC
	 * Description : This test validates the updates in shipping address  on checkout page
	 * 
	 *     
	 */
	@Test
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
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingProfile();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1,addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage().contains(lastName),
				"Shipping profile does not get updated at checkout page");
		sfCheckoutPage.clickEditLinkOfShippingProfile();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, updatedLastName, addressLine1,addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickCancelButton();
		s_assert.assertFalse(sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage().contains(updatedLastName),
				"Shipping profile get updated at checkout page even after clicking cancel button at checkout page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-418 Update Autoship- Add a billing profile - Invalid
	 * Description : This test validates the error and mandatory messages for invalid and insufficient Billing profile details
	 * 
	 *     
	 */
	@Test
	public void testUpdateAutoshipAddABillingProfileInvalid_418(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		String cardType = TestConstants.CARD_TYPE;
		String cardNum = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String cvv =  TestConstants.CVV;
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.checkMakeThisMyDefaultAddressChkBox();
		// Click Save Button without filling any details
		sfCheckoutPage.clickNextButtonAfterBillingAddress();
		s_assert.assertTrue(sfCheckoutPage.isAllErrrorMsgsForEnteringMandatoryFieldsForBillingProfileIsPresent(),
				"Mandatory messages are not present as expected");
		// Enter card number less than 16 digit
		sfCheckoutPage.enterUserBillingDetails(cardType, TestConstants.CARD_NUMBER_LESS_THAN_16_DIGIT,cardName,cvv);
		sfCheckoutPage.clickNextButtonAfterBillingAddress();
		s_assert.assertTrue(sfCheckoutPage.getCreditCardErrorMessage().contains(TestConstants.CARD_NUM_ERROR_MSG),
				"Enter Valid card details msg does not appeared as expected");
		// Enter card number in wrong format included special chars along with digits
		sfCheckoutPage.enterUserBillingDetails(cardType, TestConstants.CARD_NUMBER_IN_WRONG_FORMAT,cardName,cvv);
		sfCheckoutPage.clickNextButtonAfterBillingAddress();
		s_assert.assertTrue(sfCheckoutPage.getCreditCardErrorMessage().contains(TestConstants.CARD_NUM_ERROR_MSG),
				"Enter Valid card details msg does not appeared as expected");
		// Enter card cvv with 2 digits
		sfCheckoutPage.enterUserBillingDetails(cardType,cardNum,cardName, TestConstants.CVV_WITH_TWO_DIGIT);
		sfCheckoutPage.clickNextButtonAfterBillingAddress();
		s_assert.assertTrue(sfCheckoutPage.getCVVErrorMessage().contains(TestConstants.CARD_PIN_SHORT_ERROR_MSG),
				"Enter 3 digit pin msg is not appeared as expected");
		// Enter card cvv in wrong format which included chars with digits
		sfCheckoutPage.enterUserBillingDetails(cardType,cardNum,cardName, TestConstants.CVV_WRONG_FORMAT_DETAILS);
		sfCheckoutPage.clickNextButtonAfterBillingAddress();
		s_assert.assertTrue(sfCheckoutPage.getCVVErrorMessage().contains(TestConstants.CARD_PIN_FORMAT_ERROR_MSG),
				"Enter cvv in digit format msg is not appeared as expected");
		// Enter already expired year
		sfCheckoutPage.enterUserBillingDetailsWithSpecificYear(cardType,cardNum,cardName,cvv,TestConstants.ALREADY_EXPIRED_YEAR_INDEX);
		sfCheckoutPage.clickNextButtonAfterBillingAddress();
		s_assert.assertTrue(sfCheckoutPage.getExpDateErrorMessage().contains(TestConstants.EXPIRY_DATE_ERROR_MSG),
				"Enter correct expired date msg not appeared as expected");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-401 Update Autoship Cart- Edit a Billing Profile - PC
	 * Description : This method validates the change in billing profile on checkout page.
	 * 
	 *     
	 */
	@Test
	public void testUpdateAutoshipCartEditABillingProfilePC_401(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String textToAssertInURL = "autoship/cart";
		String firstName = TestConstants.FIRST_NAME + randomWord;
		String updatedFirstName = TestConstants.FIRST_NAME +  CommonUtils.getRandomWord(5);
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.enterBillingAddressDetailsAtCheckout(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSavePaymentButton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfCheckoutPage.isUpdatedDefaultBillingDetailsVisibleOnUI(firstName),
				"Billing Details do not get updated on Checkout Page");
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.enterBillingAddressDetailsAtCheckout(updatedFirstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickCancelButton();
		s_assert.assertFalse(sfCheckoutPage.isUpdatedDefaultBillingDetailsVisibleOnUI(updatedFirstName),
				"Billing Details get updated after clicking cancel on Checkout Page");
		s_assert.assertAll();
	}



	/***
	 * qTest : TC-407 Update Autoship Cart- Shipping Method - All future Shipments
	 * Description : This test validates the change in shipping method for all future shipments
	 * 
	 *     
	 */
	@Test
	public void testUpdateAutoshipCartShippingMethodAllFutureShipments_407(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		String selectedShippingMethodLabel = null;
		String selectedShippingMethodTitle = null;
		String shippingMethodinShippingDetails = null;
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		selectedShippingMethodLabel = sfCheckoutPage.changeTheShippingMethodAtCheckoutPage();
		selectedShippingMethodTitle = sfCheckoutPage.getTitleOfSelectedShippingMethod();
		s_assert.assertTrue(sfCheckoutPage.getLabelOfSelectedShippingMethod().contains(selectedShippingMethodLabel),
				"Selected Shipping method is not found as expected");
		sfCheckoutPage.clickShippingDetailsNextbutton();
		shippingMethodinShippingDetails = sfCheckoutPage.getTitleOfShippingMethodFromShippingDetails();
		s_assert.assertTrue(shippingMethodinShippingDetails.contains(selectedShippingMethodTitle),
				"Shipping method do not get applied. Expected title : "+selectedShippingMethodTitle+" . Actual Title  : "+selectedShippingMethodTitle);
		sfCheckoutPage.clickNextButtonAfterBillingAddress();
		sfCheckoutPage.clickOnConfirmCRPButton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		sfCheckoutPage.clickOnAutoshipCartLink();
		sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		s_assert.assertTrue(sfCheckoutPage.getLabelOfSelectedShippingMethod().contains(selectedShippingMethodLabel),
				"Selected shipping method is not found as expected when checkout pc perks again");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-422 Update Autoship- Add a billing address to new Profile - Invalid
	 * Description : This test validates the error and mandatory messages for invalid and insufficient Billing Address details
	 * 
	 *     
	 */
	@Test
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
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType,cardNum,cardName,cvv);
		// Click Next Button without filling any Billing Address details
		sfCheckoutPage.clickNextButtonAfterBillingAddress();
		s_assert.assertTrue(sfCheckoutPage.isErrrorMsgsForAllMandatoryFieldsArePresent(),
				"Mandatory Fields error messages are not present as expected");
		sfCheckoutPage.enterBillingAddressDetailsAtCheckoutForNewBillingProfile(firstName, lastName, addressLine1, addressLine2, city, state, invalidPostalCode, phoneNumber);
		s_assert.assertTrue(sfCheckoutPage.getErrorMessageForInvalidPostalCode().contains(TestConstants.INVALID_POSTAL_CODE_MSG),
				"Error msg for Invalid postal code is not present as expected");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-450 Edit PC Perks
	 * Description : This test validates the PC perks cart page by navigating to it from Welcome drop down
	 * 
	 *     
	 */
	@Test
	public void testEditPCPerks_450(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
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
	@Test
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
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		// Click Save Payment Button without filling any Billing Address details
		sfCheckoutPage.clickSavePaymentButton();
		s_assert.assertTrue(sfCheckoutPage.isErrrorMsgsForAllMandatoryFieldsForBillingAddressWithExistingProfileArePresent(),
				"Mandatory Address Fields error messages for existing profile are not present as expected.");
		sfCheckoutPage.enterBillingAddressDetailsAtCheckout(firstName, lastName, addressLine1, addressLine2, city, state, invalidPostalCode, phoneNumber);
		s_assert.assertTrue(sfCheckoutPage.getErrorMessageForInvalidPostalCode().contains(TestConstants.INVALID_POSTAL_CODE_MSG),
				"Error msg for Invalid postal code is not present as expected");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-333 Cart Page- Update Billing/Shipping Address link - PC
	 * Description : This test validates the presence of updating billing/shipping Address link after clicking Autoship cart
	 * Note : For Updating billing/shipping Address, No direct link is present after Autoship cart. 
	 *        Need to click on pc perks checkout and then the link are available for Billing and shipping updation
	 * 
	 *     
	 */
	@Test
	public void testEditPCPerks_333(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipCartPage = sfHomePage.navigateToEditPCPerksPage();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfAutoshipCartPage.isPCPerksCartHeaderPresentOnCartPage(),"PC Perks cart header is not present");
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		s_assert.assertTrue(sfCheckoutPage.isShippingLinkPresentAtCheckoutPage(),"Shipping link is not present at checkout page");
		s_assert.assertTrue(sfCheckoutPage.isBillingLinkPresentAtCheckoutPage(),"Billing link is not present at checkout page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-484 Cart Page- PC/Consultant Autoship
	 * Description : This test validates the cart page and checkout for PC/Consultant Autoship
	 * 
	 *     
	 */
	@Test
	public void testVerifyCartAndCheckoutPageForuser_484(){
		String currentURL = null;
		String textToAssertInURL = "autoship/cart";
		String checkoutPageURL = "delivery-account";
		double subtotalAtAutoshipCart;
		double newSubtotalAtAutoshipCart;
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
		//sfAutoshipCartPage = sfHomePage.clickOnAutoshipCartLink();
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipCartPage = sfHomePage.navigateToEditPCPerksPage();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL), "Expected URL should contain "+textToAssertInURL+" on Autoship cart page but actual on UI is "+currentURL);
		subtotalAtAutoshipCart = sfAutoshipCartPage.getSubtotalofItemsAtCart();
		sfShopSkinCarePage = sfAutoshipCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addFirstProductToAutoshipCart("PC");
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		newSubtotalAtAutoshipCart= sfAutoshipCartPage.getSubtotalofItemsAtCart();
		s_assert.assertTrue(newSubtotalAtAutoshipCart>subtotalAtAutoshipCart,"Product is not been added to PC Autoship cart page.");
		sfCheckoutPage = sfAutoshipCartPage.clickOnPCPerksCheckoutButton();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(checkoutPageURL), "Expected URL should contain "+checkoutPageURL+" on checkout page but actual on UI is "+currentURL);
		s_assert.assertAll();
	}	

}