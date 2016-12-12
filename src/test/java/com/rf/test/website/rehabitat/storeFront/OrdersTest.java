package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class OrdersTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-180 Order History- Link to Order details
	 * Description : This test validates Details link under Actions drop down
	 *     
	 */
	@Test
	public void testOrderHistoryLinkToOrderDetails_178(){
		String detailsLink = "Details";
		String orderNumber = null;
		String currentURL = null;
		String orderDetailsText = "Order Details";
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		orderNumber = sfOrdersPage.getFirstOrderNumberFromOrderHistory();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(detailsLink);
		currentURL = sfOrdersPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(orderNumber) && sfOrdersPage.isTextPresent(orderDetailsText),"Current url should contain "+orderNumber+"but actual on UI is "+currentURL+" and order details page is not present");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-227 Order History- Report a Problem- Field Validations
	 * Description : This test validates report problems page & field validations
	 *     
	 */
	@Test//incomplete
	public void testOrderHistoryReportAProblemFieldValidations_227(){
		String reportProblemsLink = "Report Problems";
		String name = TestConstants.FIRST_NAME;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(reportProblemsLink);
		s_assert.assertFalse(sfOrdersPage.isNameFieldEditableAtReportProblemPage(name), "Name fiels is editable at report problem page");
		s_assert.assertTrue(sfOrdersPage.isEmailFieldPresentAtReportProblemPage(), "Email field is not present at report problem page");
		s_assert.assertTrue(sfOrdersPage.isProblemDDPresentAtReportProblemPage(), "Problem DD is not present at report problem page");
		s_assert.assertTrue(sfOrdersPage.isMessageBoxPresentAtReportProblemPage(), "Message box is not present at report problem page");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-228 Order History- Report a Problem- Confirmation page
	 * Description : This test validates return order request & details
	 *     
	 */
	@Test//incomplete
	public void testOrderHistoryReportAProblemConfirmationPage_228(){
		String reportProblemsLink = "Report Problems";
		String expectedConfirmationMessage = "Thank you. We have sent your problem to our customer service and they will review the issue and respond within 48 hours";
		String confirmationMessageFromUI = null;
		String message = "For Automation";
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(reportProblemsLink);
		sfOrdersPage.enterTheDetailsForReportProblem(message);
		sfOrdersPage.clickSubmitBtnAtReportProblemPage();
		confirmationMessageFromUI = sfOrdersPage.getConfirmationMsgOfReportProblem();
		s_assert.assertTrue(confirmationMessageFromUI.contains(expectedConfirmationMessage), "Expected confirmation message is"+expectedConfirmationMessage+" but actual on UI is "+confirmationMessageFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-298 Checkout page edits - Edit Shipping Information
	 * Description : This test add a new product to cart and Edit Shipping Information
	 * 
	 *     
	 */
	@Test //incomplete User state can't be accessible in CA Environment
	public void testCheckoutPageEditsEditShippingInformation_298(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		String city = TestConstants.CITY_CA;
		String state = TestConstants.STATE_CA;
		String postalCode = TestConstants.POSTAL_CODE_CA;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickAllProducts();
		sfHomePage.selectFirstProduct();
		sfHomePage.checkoutThePopup();
		sfCheckoutPage=sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingAddress();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		s_assert.assertAll();	
	}

	/***
	 * qTest : TC-316 Consultants Cannot Ship to Quebec
	 * Description : This test validates Quebec province can not be select as shipping address
	 * for consultant
	 *     
	 */
	@Test
	public void testConsultantCanNotShipToQuebec_316(){
		if(country.equalsIgnoreCase("ca")){
			sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
			sfHomePage.clickAllProducts();
			sfHomePage.selectFirstProduct();
			sfHomePage.checkoutThePopup();
			sfCheckoutPage=sfHomePage.checkoutTheCart();
			sfCheckoutPage.clickSaveButton();
			sfCheckoutPage.clickAddNewShippingAddressButton();
			s_assert.assertTrue(sfCheckoutPage.isQuebecAddressDisabledForConsultant(), "Quebec province is enabled for consultant as shipping address");
			s_assert.assertAll();
		}
	}

	/***
	 * qTest : TC-322 Edit a Ship address- Checkout - Invalid details
	 * Description : This test validates error message for blank fields & Invalid postal code
	 * at checkout page for consultant 
	 * 
	 *     
	 */
	@Test
	public void testEditAShipAddressCheckoutInvalidDetails_322(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		String city = TestConstants.CITY_CA;
		String state = TestConstants.STATE_CA;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String invalidPostalCode = "123";
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickAllProducts();
		sfHomePage.selectFirstProduct();
		sfHomePage.checkoutThePopup();
		sfCheckoutPage=sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingAddress();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, city, state, invalidPostalCode, phoneNumber);
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForPostalCode(), "Error message is not present for postal code field");
		sfCheckoutPage.clearAllFieldsForShippingAddressAtCheckoutPage();
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForFirstAndLastName(), "Error message is not present for first and last name field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForAddressLine1(), "Error message is not present for address line 1 field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForCity(), "Error message is not present for city field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForPostalCode(), "Error message is not present for postal code field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForPhoneNumber(), "Error message is not present for phone number field");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-323 Add a ship address- Checkout - First Time
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testAddAShipAddressCheckoutFirstTime_323(){

	}

	/***
	 * qTest : TC-229 Checkout- Viewing Main Account Info
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testCheckoutViewingMainAccountInfo_323(){

	}

	/***
	 * qTest : TC-233 Shipping method cost should reduce for consultant adhoc order with an SV 100+
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testShippingMethodCostShouldReduceForConsultantAdhocOrderWithMoreThanHundredSVValue_233(){

	}


	/***
	 * qTest : TC-295 Checkout page edits - Check Edit options
	 * Description : this test validates all edit links at checkout page
	 * 
	 *     
	 */
	@Test
	public void testCheckoutPageEditsCheckEditOptions_295(){
		String currentURL = null;
		String accountInfo = "delivery-account";
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickAllProducts();
		sfHomePage.selectFirstProduct();
		sfHomePage.checkoutThePopup();
		sfCheckoutPage=sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfAccountInfo();
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(sfCheckoutPage.isSaveButtonDisplayedForAccountInfo() && currentURL.contains(accountInfo), "Current URL should contain"+accountInfo+ "but actual on UI is "+currentURL+ "Edit link is not redirecting to account info page");
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingAddress();
		s_assert.assertTrue(sfCheckoutPage.isshippindAddressFieldsAreDisplayedAtCheckoutPage(), "Shipping fields are not displayed after click on edit link of shipping address");
		sfCheckoutPage.clickCancelButton();
		sfCheckoutPage.clickEditLinkOfOrderSummarySection();
		s_assert.assertTrue(sfCheckoutPage.isCartPagePresent(), "Cart page is not displayed after clicked on edit link of order summary section");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-299 Checkout page edits - Edit Shipping Method
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testCheckoutPageEditsEditShippingMethod_299(){
		String shippingMethod = "UPS 2 Days";
		String shippingMethodNameFromUI = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickAllProducts();
		sfHomePage.selectFirstProduct();
		sfHomePage.checkoutThePopup();
		sfCheckoutPage=sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.selectShippingMethod(shippingMethod);
		shippingMethodNameFromUI = sfCheckoutPage.getSelectedShippingMethodName();

		s_assert.assertTrue(shippingMethodNameFromUI.contains(shippingMethod),"Expected shipping method name is "+shippingMethod+" but actual on UI is:"+shippingMethodNameFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-300 Checkout page edits - Edit cart
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testCheckoutPageEditsEditCart_300(){

	}


	/***
	 * qTest : TC-302 Billing profile- Add an Address to existing Profile
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testBillingProfileAddAnAddressToExistingProfile_302(){

	}

	/***
	 * qTest : TC-303 Billing profile- Add an Address to New Profile
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testBillingProfileAddAnAddressToNewProfile_303(){

	}

	/***
	 * qTest : TC-304 Billing profile- Add a Profile with same Shipping address
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testBillingProfileAddAProfileWithSameShippingAddress_304(){

	}

	/***
	 * qTest : TC-305 Adding a payment method- Errors
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testAddingAPaymentMethodErrors_305(){

	}

	/***
	 * qTest : TC-306 Tax Rules- Consultant- USA- Ad-hoc order
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testTaxRulesConsultantUSAAdhocOrder_306(){

	}

	/***
	 * qTest : TC-307 Tax Rules- Consultant- USA- Autoship order
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testTaxRulesConsultantUSAAutoshipOrder_307(){

	}

	/***
	 * qTest : TC-308 Display Tax- Order history details
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testDisplayTaxOrderHistoryDetails_308(){

	}

	/***
	 * qTest : TC-309 Display Tax- Checkout
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testDisplayTaxCheckout_309(){

	}

	/***
	 * qTest : TC-310 Add a payment method-Checkout
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testAddAPaymentMethodCheckout_310(){

	}

	/***
	 * qTest : TC-311 Ship Method-PC - direct PC enrollment
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testShippingMethodPCDirectPCEnrollment_311(){

	}

	/***
	 * qTest : TC-312 Ship Method-PC - select PC checkbox on checkout
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testShippingMethodPCSelectPCCheckboxOnCheckout_312(){

	}

	/***
	 * qTest : TC-313 Ship Method-PC - Existing PC changes Shipping Method
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testShipMethodPCExistingPCChangesShippingMethod_313(){

	}

	/***
	 * qTest : TC-314 Ship Method-PC - Existing PC places Ad-hoc order
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testShipMethodPCExistingPCPlaceAdhocOrder_314(){

	}


	/***
	 * qTest : TC-315 Ship method- Ad Hoc carts
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testShipMethodAdhocCarts(){

	}
	/***
	 * qTest : TC-316 Consultants Cannot Ship to Quebec
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testConsultantsCannotShipToQuebec_316(){

	}

	/***
	 * qTest : TC-318 Address Verification - Billing Profile
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testAddressVerificationBillingProfile_318(){

	}

	/***
	 * qTest : TC-318 Shipping to PO/RR boxes
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testShippingToPORRboxes_319(){

	}
	/***
	 * qTest : TC-321 Edit a Ship address- Checkout - Valid details
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testEditAShipAddressCheckoutValidDetails_321(){

	}

	/***
	 * qTest : TC-332 Cart Page- Product Messaging for Non-replenishable products
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testCartPageProductMessagingForNonReplenishableProducts_332(){

	}
	/***
	 * qTest : TC-335 Cart Page- PC Perks Terms and Conditions
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testCartPagePCPerksTermsAndConditions_335(){

	}

	/***
	 * qTest : TC-354 PC user is in the process of placing an adhoc order less than 90$
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testPCUserIsInTheProcessOfPlacingAnAdhocOrderLessThan90$_354(){

	}
	/***
	 * qTest : TC-355 PC user is in the process of placing an adhoc order more than 90$
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testPCUserIsInTheProcessOfPlacingAnAdhocOrderMoreThan90$_355(){

	}
	/***
	 * qTest : TC-366 Billing profile- Add an Address with invalid details to existing Profile
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testBillingProfileAddAnAddressWithInvalidDetailsToExistingProfile_366(){

	}
	/***
	 * qTest : TC-367 Billing profile- Add an Address with invalid Details to New Profile
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testBillingProfileAddAnAddressWithInvalidDetailsToNewProfile_367(){

	}
	/***
	 * qTest : TC-368 Shipping to PO/RR with PO number as address detail
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testShippingToPORRWithPONumberAsAddressDetail_368(){

	}
	/***
	 * qTest : TC-409 Order Confirmation for AdHoc Orders
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testOrderConfirmationForAdHocOrders_409(){

	}
	/***
	 * qTest : TC-414 User shouldn't able to change the sponsor after consultant account created
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testUserShouldntAbleToChangeTheSponsorAfterConsultantAccountCreated_414(){

	}
	/***
	 * qTest : TC-415 User shouldn't able to change the sponsor after PC account created
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testUserShouldntAbleToChangeTheSponsorAfterPCAccountCreated_415(){

	}
	/***
	 * qTest : TC-365 Order Details and Return Order Details- Link to Pulse
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testOrderDetailsAndReturnOrderDetailsLinkToPulse_365(){

	}
	/***
	 * qTest : TC-457 Choose a consultant- R+F Corporate sponsor - PC First checkout from Corp
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testChooseAConsultantRFCorporateSponsorPCFirstCheckoutFromCorp_457(){

	}
	/***
	 * qTest : TC-458 Choose a consultant- R+F Corporate sponsor - PC First checkout from PWS
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testChooseAConsultantRFCorporateSponsorPCFirstCheckoutFromPWS_458(){

	}

	/***
	 * qTest : TC-459 Choose a consultant- R+F Corporate sponsor - RC checkout from Corporate
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testChooseAConsultantRFCorporateSponsorRCCheckoutFromCorporate_459(){

	}
	/***
	 * qTest : TC-470 Review and Place Order
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testReviewAndPlaceOrder_470(){

	}
	/***
	 * qTest : TC-471 Review and Place Order - Edit cart
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test
	public void testReviewAndPlaceOrderEditCart_471(){

	}

	/***
	 * qTest : TC-324 Add a ship address- Checkout - existing user
	 * Description : This test add a new shipping address for existing user
	 *     
	 */
	@Test
	public void testAddAShipAddressCheckoutExistingUser_324(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		String city = TestConstants.CITY_CA;
		String state = TestConstants.STATE_CA;
		String postalCode = TestConstants.POSTAL_CODE_CA;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickAllProducts();
		sfHomePage.selectFirstProduct();
		sfHomePage.checkoutThePopup();
		sfCheckoutPage=sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickAddNewShippingAddressButton();
		sfCheckoutPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		s_assert.assertTrue(sfCheckoutPage.isUseAsEnteredPopupDisplayed(), "Use As Entered Confirmation Popup is Not Displayed after added new shipping profile");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-317 Address Verification - Shipping Profile
	 * Description : This test add a new shipping address for existing user and validates it
	 *     
	 */
	@Test// Incomplete
	public void testAddressVerificationShippingProfile_317(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		String city = TestConstants.CITY_CA;
		String state = TestConstants.STATE_CA;
		String postalCode = TestConstants.POSTAL_CODE_CA;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickAllProducts();
		sfHomePage.selectFirstProduct();
		sfHomePage.checkoutThePopup();
		sfCheckoutPage=sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingAddress();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		s_assert.assertTrue(sfCheckoutPage.isUseAsEnteredPopupDisplayed(), "Use As Entered Confirmation Popup is Not Displayed after Editing shipping profile");
		sfCheckoutPage.clickUseAsEnteredButton();
		sfCheckoutPage.clickAddNewShippingAddressButton();
		sfCheckoutPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		s_assert.assertTrue(sfCheckoutPage.isUseAsEnteredPopupDisplayed(), "Use As Entered Confirmation Popup is Not Displayed after added new shipping profile");
		sfCheckoutPage.clickUseAsEnteredButton();
		s_assert.assertAll();
	}
}
