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
	@Test(enabled=true)
	public void testOrderHistoryLinkToOrderDetails_180(){
		String detailsLink = "Details";
		String orderNumber = null;
		String currentURL = null;
		String orderDetailsText = "Order Details";
		//For consultant
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		orderNumber = sfOrdersPage.getFirstOrderNumberFromOrderHistory();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(detailsLink);
		currentURL = sfOrdersPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(orderNumber) && sfOrdersPage.isTextPresent(orderDetailsText),"Current url should contain for consultant "+orderNumber+"but actual on UI is "+currentURL+" and order details page is not present");
		sfOrdersPage.clickRodanAndFieldsLogo();
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage.logout();
		//For PC
		navigateToStoreFrontBaseURL();
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		orderNumber = sfOrdersPage.getFirstOrderNumberFromOrderHistory();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(detailsLink);
		currentURL = sfOrdersPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(orderNumber) && sfOrdersPage.isTextPresent(orderDetailsText),"Current url should contain for PC "+orderNumber+"but actual on UI is "+currentURL+" and order details page is not present");
		sfOrdersPage.clickRodanAndFieldsLogo();
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage.logout();
		//For RC
		navigateToStoreFrontBaseURL();
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(), password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		orderNumber = sfOrdersPage.getFirstOrderNumberFromOrderHistory();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(detailsLink);
		currentURL = sfOrdersPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(orderNumber) && sfOrdersPage.isTextPresent(orderDetailsText),"Current url should contain for RC "+orderNumber+"but actual on UI is "+currentURL+" and order details page is not present");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-298 Checkout page edits - Edit Shipping Information
	 * Description : This test add a new product to cart and Edit Shipping Information
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testCheckoutPageEditsEditShippingInformation_298(){
		//already covered in Add And Delete Shipping 

	}

	/***
	 * qTest : TC-316 Consultants Cannot Ship to Quebec
	 * Description : This test validates Quebec province can not be select as shipping address
	 * for consultant
	 *     
	 */
	@Test(enabled=true) //CA specific test
	public void testConsultantCanNotShipToQuebec_316(){
		if(country.equalsIgnoreCase("ca")){
			sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
			sfShopSkinCarePage = sfHomePage.clickAllProducts();
			sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
			sfShopSkinCarePage.checkoutTheCartFromPopUp();
			sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
			sfCheckoutPage.clickSaveButton();
			sfCheckoutPage.clickAddNewShippingAddressButton();
			s_assert.assertTrue(sfCheckoutPage.isQuebecAddressDisabledForConsultant(), "Quebec province is enabled for consultant as shipping address");
			s_assert.assertAll();
		}
	}

	/***
	 * qTest : TC-295 Checkout page edits - Check Edit options
	 * Description : this test validates all edit links at checkout page
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testCheckoutPageEditsCheckEditOptions_295(){
		String currentURL = null;
		String accountInfo = "delivery-account";
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
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
	 * Description : This test cases upadte the shipping method name and validates it
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testCheckoutPageEditsEditShippingMethod_299(){
		String shippingMethod = null;
		String shippingMethodNameFromUI = null;
		String nonSelectedshippingMethodName1 = TestConstants.SHIPPING_METHOD_UPS_2DAY;
		String nonSelectedshippingMethodName2 = TestConstants.SHIPPING_METHOD_UPS_OVERNIGHT;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		shippingMethodNameFromUI = sfCheckoutPage.getSelectedShippingMethodName();
		shippingMethod = sfCheckoutPage.getNonSelectedShippingMethodName(shippingMethodNameFromUI, nonSelectedshippingMethodName1, nonSelectedshippingMethodName2);
		sfCheckoutPage.selectShippingMethod(shippingMethod);
		shippingMethodNameFromUI = sfCheckoutPage.getSelectedShippingMethodName();
		s_assert.assertTrue(shippingMethodNameFromUI.contains(shippingMethod),"Expected shipping method name is "+shippingMethod+" but actual on UI is:"+shippingMethodNameFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-300 Checkout page edits - Edit cart
	 * Description : This test cases validates the redirection of cart and checkout page
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testCheckoutPageEditsEditCart_300(){
		String productQuantityFromUI = null;
		String updatedQuantity = null;
		String checkoutPageText = "Account Info";
		String currentURL = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickEditLinkOfOrderSummarySection();
		s_assert.assertTrue(sfCheckoutPage.isCartPagePresent(), "Cart page is not displayed after clicked on edit link of order summary section");
		productQuantityFromUI = sfCartPage.getQuantityOfProductFromCart("1");
		updatedQuantity = sfCartPage.updateQuantityByOne(productQuantityFromUI);
		sfCartPage.enterQuantityOfProductAtCart("1", updatedQuantity);
		sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		currentURL = sfCartPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains("checkout") && sfCheckoutPage.isTextPresent(checkoutPageText),"Current url should contain checkout but actual on UI is "+currentURL+" and checkout page is not present");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-302 Billing profile- Add an Address to existing Profile
	 * Description : This test case edit the shipping profile at checkout page
	 * and verify it
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testBillingProfileAddAnAddressToExistingProfile_302(){
		// Covered in Billing Profile
	}

	/***
	 * qTest : TC-303 Billing profile- Add an Address to New Profile
	 * Description : This test case validates new billing profile is added or not
	 * after edit from order review page
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testBillingProfileAddAnAddressToNewProfile_303(){
		// Covered in Billing Profile	
	}

	/***
	 * qTest : TC-305 Adding a payment method- Errors
	 * Description : This test cases verify the validation of Card details fields
	 * TODO :: Have to enter a expire card date 
	 *     
	 */
	@Test(enabled=true)
	public void testAddingAPaymentMethodErrors_305(){
		String invalidExpMonth = TestConstants.INVALID_CARD_EXP_MONTH;
		String invalidExpYear = TestConstants.INVALID_CARD_EXP_YEAR;
		String cardNumberMoreThan16Digit = TestConstants.CARD_NUMBER_MORE_THAN_16_DIGIT;
		String cardNumberLessThan16Digit = TestConstants.CARD_NUMBER_LESS_THAN_16_DIGIT;
		String cardNumberWithChar = TestConstants.CARD_NUMBER_LESS_THAN_16_DIGIT+"q";
		String CVVWithTwoDigit = TestConstants.CVV_WITH_TWO_DIGIT;
		String errorMessage = null;
		String errorMessageFromUI = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserCreditCardNumber(cardNumberLessThan16Digit);
		errorMessage = "Invalid Credit card number";
		errorMessageFromUI = sfCheckoutPage.getCreditCardErrorMessage();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for less than 16 digit card number is "+errorMessage+" but actual on UI is "+errorMessageFromUI);
		sfCheckoutPage.enterUserCreditCardNumber(cardNumberMoreThan16Digit);
		errorMessageFromUI = sfCheckoutPage.getCreditCardErrorMessage();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for more than 16 digit card number is "+errorMessage+" but actual on UI is "+errorMessageFromUI);
		//card number with char
		sfCheckoutPage.enterUserCreditCardNumber(cardNumberWithChar);
		errorMessageFromUI = sfCheckoutPage.getCreditCardErrorMessage();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for card number with char is "+errorMessage+" but actual on UI is "+errorMessageFromUI);
		//wrong CVV
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVVWithTwoDigit+"\t");
		sfCheckoutPage.clickBillingDetailsNextbutton();
		errorMessage = "Please enter at least 3 characters";
		errorMessageFromUI = sfCheckoutPage.getCVVErrorMessage();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for CVV with char is "+errorMessage+" but actual on UI is "+errorMessageFromUI);
		/*// Invalid Exp date
	   sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV, invalidExpMonth, invalidExpYear);
	   sfCheckoutPage.clickBillingDetailsNextbutton();
	   errorMessage = "Please enter a valid expiration date";
	   errorMessageFromUI = sfCheckoutPage.getExpDateErrorMessage();
	   s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for card exp date is "+errorMessage+" but actual on UI is "+errorMessageFromUI);*/
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-310 Add a payment method-Checkout
	 * Description : This test case validates billing profile details are in editable mode
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testAddAPaymentMethodCheckout_310(){
		//covered in enrollments
	}

	/***
	 * qTest : TC-311 Ship Method-PC - direct PC enrollment
	 * Description : This testcase validates shipping method name and cost for PC
	 * During creation of PC
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testShippingMethodPCDirectPCEnrollment_311(){
		String timeStamp = CommonUtils.getCurrentTimeStamp();
		String firstName = TestConstants.FIRST_NAME;
		String randomWord = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME+randomWord;
		String emailID = TestConstants.FIRST_NAME+timeStamp+TestConstants.EMAIL_SUFFIX;
		String shippingMethodWithCost = null;
		String shippingMethodName = TestConstants.SHIPPING_METHOD_UPS_GROUND;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC,firstName, lastName, emailID, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		shippingMethodWithCost = sfCheckoutPage.getSelectedShippingMethodName();
		s_assert.assertTrue(shippingMethodWithCost.contains("$0") && shippingMethodWithCost.contains(shippingMethodName), "Expected shipping method name is "+shippingMethodName+" and shipping cost should contain $0 but actual on UI is "+shippingMethodWithCost);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-314 Ship Method-PC - Existing PC places Ad-hoc order
	 * Description : This testcase validates shipping method name and cost for PC
	 * During Adhoc order
	 *     
	 */
	@Test(enabled=true)
	public void testShipMethodPCExistingPCPlaceAdhocOrder_314(){
		String shippingMethodWithCost = null;
		String shippingMethodName = TestConstants.SHIPPING_METHOD_UPS_GROUND;
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		shippingMethodWithCost = sfCheckoutPage.getSelectedShippingMethodName();
		s_assert.assertTrue(shippingMethodWithCost.contains("$0") && shippingMethodWithCost.contains(shippingMethodName), "Expected shipping method name is "+shippingMethodName+" and shipping cost should contain $0 but actual on UI is "+shippingMethodWithCost);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-319 Shipping to PO/RR boxes
	 * Description : This test cases edit the shipping address with different different type of address
	 * like PO box, RR, APO,DPO,FPO and validates it
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testShippingToPORRboxes_319(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.PO_ADDRESS_LINE_1_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String shippingProfileNameFromUI = null;
		String errorMessageForPOAddress = "We are unable to ship to P.O. Boxes";
		String errorMessageForPOAddressFromUI = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingProfile();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		errorMessageForPOAddressFromUI = sfCheckoutPage.getErrorMessageForAddressLine1();
		s_assert.assertTrue(errorMessageForPOAddressFromUI.contains(errorMessageForPOAddress), "Expected error message should contain "+errorMessageForPOAddress+ "Actual on UI is "+errorMessageForPOAddressFromUI);

		//RR details
		addressLine1 = TestConstants.RR_ADDRESS_LINE_1_US;
		city = TestConstants.RR_CITY_US;
		state = TestConstants.RR_STATE_US;
		postalCode = TestConstants.RR_POSTAL_CODE_US;
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		shippingProfileNameFromUI = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		s_assert.assertTrue(shippingProfileNameFromUI.contains(lastName) &&shippingProfileNameFromUI.contains(city)&& shippingProfileNameFromUI.contains(state) && shippingProfileNameFromUI.contains(postalCode) , "For RR address Shipping profile name should contain "+lastName+ "City "+city+" State "+state+"Postal code "+postalCode+" Actual on UI is "+shippingProfileNameFromUI);

		//APO address
		randomWord = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME+randomWord;
		addressLine1 = TestConstants.APO_ADDRESS_LINE_1_US;
		city = TestConstants.APO_CITY_US;
		state = TestConstants.APO_STATE_US;
		postalCode = TestConstants.APO_POSTAL_CODE_US;
		sfCheckoutPage.clickEditLinkOfShippingProfile();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		shippingProfileNameFromUI = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		s_assert.assertTrue(shippingProfileNameFromUI.contains(lastName) &&shippingProfileNameFromUI.contains(city)&& shippingProfileNameFromUI.contains(state) && shippingProfileNameFromUI.contains(postalCode) , "For APO addressShipping profile name should contain "+lastName+ "City "+city+" State "+state+"Postal code "+postalCode+" Actual on UI is "+shippingProfileNameFromUI);

		//DPO address
		randomWord = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME+randomWord;
		addressLine1 = TestConstants.DPO_ADDRESS_LINE_1_US;
		city = TestConstants.DPO_CITY_US;
		state = TestConstants.DPO_STATE_US;
		postalCode = TestConstants.DPO_POSTAL_CODE_US;
		sfCheckoutPage.clickEditLinkOfShippingProfile();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		shippingProfileNameFromUI = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		s_assert.assertTrue(shippingProfileNameFromUI.contains(lastName) &&shippingProfileNameFromUI.contains(city)&& shippingProfileNameFromUI.contains(state) && shippingProfileNameFromUI.contains(postalCode) , "For DPO addressShipping profile name should contain "+lastName+ "City "+city+" State "+state+"Postal code "+postalCode+" Actual on UI is "+shippingProfileNameFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-366 Billing profile- Add an Address with invalid details to existing Profile
	 * Description : This test case validates error message of address fields of billing profile
	 * while editing
	 *     
	 */
	@Test(enabled=true)
	public void testBillingProfileAddAnAddressWithInvalidDetailsToExistingProfile_366(){
		String randomWord = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME+randomWord;
		String invalidAddressLine1 ="address line 1";
		String invalidPostalCode = "12345";
		String errorMessageFromUI = null;
		String errorMessage= null;
		String billingProfileDetails = null;
		randomWord = CommonUtils.getRandomWord(5);
		cardName = cardName+randomWord;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.clickEditLinkOfDefaultBillingProfile();
		sfCheckoutPage.clearAllFieldsOfBillingAddressDetailsForExistingBillingProfile();
		sfCheckoutPage.clickSavePaymentButton();
		s_assert.assertTrue(sfCheckoutPage.isErrrorMsgsForAllMandatoryFieldsForBillingAddressArePresent(),
				"Mandatory Address Fields error messages for existing profile are not present as expected.");
		// enter invalid address
		sfCheckoutPage.enterEditBillingAddressDetailsAtCheckout(firstName, lastName, invalidAddressLine1, addressLine2, city, state, invalidPostalCode, phoneNumber);
		sfCheckoutPage.clickSavePaymentButton();
		errorMessage = "Address entered may not be a deliverable address";
		errorMessageFromUI = sfCheckoutPage.getAddressNonDeliverableWarningMsg();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for invalid address details is "+errorMessage+" But actual on UI is "+errorMessage);
		sfCheckoutPage.clickEditAddressBtnOnAddressSuggestionPopup();
		sfCheckoutPage.enterEditBillingAddressDetailsAtCheckout(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSavePaymentButton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		billingProfileDetails = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		s_assert.assertTrue(billingProfileDetails.contains(cardName), "Billing profile name should contain "+cardName+" but actual On UI is"+billingProfileDetails);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-367 Billing profile- Add an Address with invalid Details to New Profile
	 * Description : This test case validates error message of address fields of billing profile
	 * while adding a new profile
	 *     
	 */
	@Test(enabled=true)
	public void testBillingProfileAddAnAddressWithInvalidDetailsToNewProfile_367(){
		String randomWord = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME;
		String invalidAddressLine1 ="address line 1";
		String invalidPostalCode = "12345";
		String errorMessageFromUI = null;
		String errorMessage= null;
		String billingProfileDetails = null;
		String billingLastName = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		billingLastName = lastName+randomWord;
		cardName = firstName+" "+billingLastName;
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForFirstAndLastName(), "Error message is not present for first and last name field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForAddressLine1(), "Error message is not present for address line 1 field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForCity(), "Error message is not present for city field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForPostalCode(), "Error message is not present for postal code field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForPhoneNumber(), "Error message is not present for phone number field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForState(), "Error message is not present for state field");
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, invalidAddressLine1, addressLine2, city, state, invalidPostalCode, phoneNumber);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		errorMessage = "Address entered may not be a deliverable address";
		errorMessageFromUI = sfCheckoutPage.getAddressNonDeliverableWarningMsg();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for invalid address details is "+errorMessage+" But actual on UI is "+errorMessage);
		sfCheckoutPage.clickEditAddressBtnOnAddressSuggestionPopup();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		billingProfileDetails = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		s_assert.assertTrue(billingProfileDetails.contains(billingLastName), "Billing profile name should contain "+lastName+" but actual On UI is"+billingProfileDetails);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-409 Order Confirmation for AdHoc Orders
	 * Description : This test case validates SubTotal, Grand Total, Shipping charges, Item name, product qty, SV, 
	 * shipping method, Shipping profile name, Billing profile name, CC details
	 * at order confirmation page
	 *  
	 */

	@Test(enabled=true)//TODO assertion for SV & GST
	public void testOrderConfirmationForAdhocOrders_409(){
		String productName=null;
		String productQuantity="Qty";
		String subTotal=null;
		String orderTotal=null;
		String shippingProfile=null;
		String shippingCharges=null;
		String shippingMethodBeforeOrderPlaced=null;
		String ccfourDigits  = null;
		String ccExpiryDate = null;
		String billingProfileName  = null;
		String billingProfileLastName = null;
		String text_Subtotal = "Subtotal";
		String text_Shipping = "Shipping";
		String text_GrandTotal = "GRAND TOTAL";
		String text_Delivery = "Delivery";
		String text_OrderTotal = "Total";
		String productSVValue = null;
		String yourPrice = null;
		String shippingProfileFromOrderConfirmationPage = null;
		String shippingMethodFromOrderConfirmationPage = null;
		String billingProfileNameFromOrderConfirmationPage = null;
		String lastFourDigitOfCCFromOrderConfirmationPage = null;
		String expDateOfCCFromOrderConfirmationPage = null;
		String grandTotalAtOrderConfirmationPage = null;
		String shippingChargeAtOrderConfirmationPage = null;
		String subTotalAtOrderConfirmationPage = null;
		String productQtyAtOrderDetailsPage = null;
		String productSVAtOrderDetailsPage = null;
		String productUnitPriceAtOrderDetailsPage = null;
		String productNameAtOrderDetailsPage = null;

		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		//productSVValue = sfShopSkinCarePage.getProductRetailAndSVPrice(TestConstants.PRODUCT_NUMBER);
		//yourPrice = sfShopSkinCarePage.getYourPriceOfAProduct(TestConstants.PRODUCT_NUMBER);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC, validProductId);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		productName = sfCartPage.getProductName("1");
		productQuantity = sfCartPage.getQuantityOfProductFromCart("1");
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		//shippingMethodBeforeOrderPlaced =  sfCheckoutPage.getSelectedShippingMethodName();
		shippingProfile = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		billingProfileName = sfCheckoutPage.getBillingProfileName();
		billingProfileLastName = sfCheckoutPage.getLastName(billingProfileName);
		sfCheckoutPage.getCardDetailsFromBillingInfo(billingProfileLastName);
		ccfourDigits = sfCheckoutPage.getLastFourDigitsOfCardNumberInBillingDetails();
		ccExpiryDate = sfCheckoutPage.getExpiryDateOfCardNumberInBillingDetails();
		shippingCharges = sfCheckoutPage.getShippingChragesAtCheckoutPage();
		orderTotal = sfCheckoutPage.getChargesAccordingToLabelAtOrderReviewPage(text_OrderTotal);
		subTotal = sfCheckoutPage.getChargesAccordingToLabelAtOrderReviewPage(text_Subtotal);
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertTrue(sfCheckoutPage.isTextPresent("Order #"),"Order Number is not present on the confirmation page");
		productQtyAtOrderDetailsPage = sfCheckoutPage.getProductQuantityOfAnItem("2");
		//productSVAtOrderDetailsPage = sfCheckoutPage.getProductSVOfAnItem("2").trim();
		//productUnitPriceAtOrderDetailsPage = sfCheckoutPage.getProductUnitPriceOfAnItem("2");
		productNameAtOrderDetailsPage = sfCheckoutPage.getProductNameOfAnItem("2");
		shippingProfileFromOrderConfirmationPage = sfCheckoutPage.getShippingProfileFromConfirmationPage();
		shippingMethodFromOrderConfirmationPage = sfCheckoutPage.getShippingMethodAfterPlacedOrder().split("\\:")[1].trim();
		billingProfileNameFromOrderConfirmationPage = sfCheckoutPage.getBillingProfileAfterPlacedOrder();
		lastFourDigitOfCCFromOrderConfirmationPage = sfCheckoutPage.getLastFourNumbersOfBillingDetailsOnConFirmationPage();
		expDateOfCCFromOrderConfirmationPage = sfCheckoutPage.getExpiryDateOfCardNumberInBillingDetails();
		grandTotalAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_GrandTotal);
		shippingChargeAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_Shipping);
		subTotalAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_Subtotal);
		s_assert.assertTrue(shippingProfile.contains(shippingProfileFromOrderConfirmationPage), "Shipping Profile is not matching on confirmation page. Expected is :"+shippingProfile+" But found is :"+shippingProfileFromOrderConfirmationPage);
		//s_assert.assertTrue(shippingMethodBeforeOrderPlaced.contains(shippingMethodFromOrderConfirmationPage), "Shipping Method is not matching on confirmation page. Expected is :"+shippingMethodBeforeOrderPlaced+" But found is :"+shippingMethodFromOrderConfirmationPage);
		s_assert.assertTrue(billingProfileNameFromOrderConfirmationPage.contains(billingProfileLastName),"Billing Profile is not matching on confirmation page. Expected is :"+billingProfileLastName+" But found is :"+billingProfileNameFromOrderConfirmationPage);
		s_assert.assertTrue(lastFourDigitOfCCFromOrderConfirmationPage.equals(ccfourDigits), "Credit Card Last 4 digits are not matching. Expected is :"+ccfourDigits+" But found is :"+lastFourDigitOfCCFromOrderConfirmationPage);
		s_assert.assertTrue(expDateOfCCFromOrderConfirmationPage.equals(ccExpiryDate), "Credit Card Expiry Date is not matching. Expected is :"+ccExpiryDate+" But found is :"+expDateOfCCFromOrderConfirmationPage);
		s_assert.assertTrue(grandTotalAtOrderConfirmationPage.equals(orderTotal),"Order total is not matching. Expected is:"+orderTotal+"But found is "+grandTotalAtOrderConfirmationPage);
		s_assert.assertTrue(shippingChargeAtOrderConfirmationPage.equals(shippingCharges),"Shipping charge is not matching. Expected is:"+shippingCharges+"But found is "+shippingChargeAtOrderConfirmationPage);
		s_assert.assertTrue(subTotalAtOrderConfirmationPage.equals(subTotal),"Subtotal is not matching. Expected is:"+subTotal+"But found is "+subTotalAtOrderConfirmationPage);
		s_assert.assertTrue(productQtyAtOrderDetailsPage.contains(productQuantity),"Product qty is not matching. Expected is:"+productQuantity+"But found is "+productQtyAtOrderDetailsPage);
		//s_assert.assertTrue(productSVValue.contains(productSVAtOrderDetailsPage),"Product SV value is not matching. Expected is:"+productSVValue+"But found is "+productSVAtOrderDetailsPage);
		//	s_assert.assertTrue(productUnitPriceAtOrderDetailsPage.contains(yourPrice),"Product unit price is not matching. Expected is:"+yourPrice+"But found is "+productUnitPriceAtOrderDetailsPage);
		s_assert.assertTrue(productNameAtOrderDetailsPage.contains(productName),"Product name is not matching. Expected is:"+productName+"But found is "+productNameAtOrderDetailsPage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-414 User shouldn't able to change the sponsor after consultant account created
	 * Description : This test case validates consultant can not change the sponsor after creation of account
	 * through continue without a consultant and not your sponsor link
	 *     
	 */
	@Test(enabled=true)
	public void testUserShouldntAbleToChangeTheSponsorAfterConsultantAccountCreated_414(){
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		s_assert.assertFalse(sfCheckoutPage.isContinueWithoutConsultantLinkPresent(), "Continue without consultant link is present for consultant's sponsor");
		s_assert.assertFalse(sfCheckoutPage.isNotYourSponsorLinkIsPresent(), "Not your sponsor link is present for consultant's sponsor");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-415 User shouldn't able to change the sponsor after PC account created
	 *Description : This test case validates PC can not change the sponsor after creation of account
	 * through continue without a consultant and not your sponsor link
	 *     
	 */
	@Test(enabled=true)
	public void testUserShouldntAbleToChangeTheSponsorAfterPCAccountCreated_415(){
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		s_assert.assertFalse(sfCheckoutPage.isContinueWithoutConsultantLinkPresent(), "Continue without consultant link is present for consultant's sponsor");
		s_assert.assertFalse(sfCheckoutPage.isNotYourSponsorLinkIsPresent(), "Not your sponsor link is present for consultant's sponsor");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-458 Choose a consultant- R+F Corporate sponsor - PC First checkout from PWS
	 * Description :  This test case validates the sponsor functionality and before enrolled PC
	 * & after enrollment PC can not change the sponsor start from PWS
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testChooseAConsultantRFCorporateSponsorPCFirstCheckoutFromPWS_458(){
		String timeStamp = CommonUtils.getCurrentTimeStamp();
		String randomWord = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME+randomWord;
		String emailID = TestConstants.FIRST_NAME+timeStamp+TestConstants.EMAIL_SUFFIX;
		String sponsorName = "RF Corporate";
		String sponsorNameFromUI = null;
		String homePageURL = sfHomePage.getCurrentURL();
		String prefix = pwsPrefix();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT, validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC,firstName, lastName, emailID, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickRemoveLink();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sponsorNameFromUI = sfCheckoutPage.getSponsorInfo();
		s_assert.assertTrue(sponsorNameFromUI.contains(sponsorName), "Expected sponsor name is "+sponsorName+" but actual on UI is "+sponsorNameFromUI);
		sfCheckoutPage.clickNotYourConsultantLink();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sponsorNameFromUI = sfCheckoutPage.getSponsorInfo();
		s_assert.assertTrue(sponsorNameFromUI.contains(sponsorName), "Expected sponsor name is "+sponsorName+" but actual on UI is "+sponsorNameFromUI);
		sfCheckoutPage.clickNotYourConsultantLink();
		s_assert.assertTrue(sfCheckoutPage.isSponsorSearchBoxVisible(), "Sponsor search box is not visible after clicked on not your consultant link");
		sfCheckoutPage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		s_assert.assertTrue(sponsorNameFromUI.contains(sponsorName), "Expected sponsor name is "+sponsorName+" but actual on UI is at last "+sponsorNameFromUI);
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectIAcknowledgePCChkBox();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfHomePage= sfCheckoutPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.checkoutTheCart();
		s_assert.assertFalse(sfCheckoutPage.isNotYourConsultantLinkPresent(), "Not your sponsor link is present for consultant's sponsor");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-470 Review and Place Order
	 * Description : This test case vaidates the order details at review and confirm page
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testReviewAndPlaceOrder_470(){
		// Duplicate, same as 409
	}

	/***
	 * qTest : TC-324 Add a ship address- Checkout - existing user
	 * Description : This test add a new shipping address for existing user
	 *     
	 */
	@Test(enabled=true)
	public void testAddAShipAddressCheckoutExistingUser_324(){
		//Already covered in AddAndDeeleteShippingTest
	}

	/***
	 * qTest : TC-317 Address Verification - Shipping Profile
	 * Description : This test add a new shipping address for existing user and validates it
	 *     
	 */
	@Test(enabled=true)
	public void testAddressVerificationShippingProfile_317(){
		//Already covered in AddAndDeeleteShippingTest
	}

	/***
	 * qTest : TC-482 Order History- Report a Problem-Order Level-All fields filled
	 * Description : This test validates report problem and report confirmation page 
	 *     
	 */
	@Test(enabled=true)
	public void testOrderHistoryReportAProlemOrderLevelAllFieldsFilled_482(){
		String reportProblemsLink = "Report Problems";
		String reasonWhereIsTheShipment = "\"Where's the shipment\"";
		String reasonOrderIsIncorrect = "\"Order is incorrect\"";
		String reasonNeedToReturnAnItem = "\"Need to return an item or an order\"";
		String reasonDontKnowWasEnrolledInAutoship = "\"DIdn't know was enrolled in an autoship\"";
		String reasonShipmentWasDamaged = "\"My Shipment was damaged or missing items\"";
		String message = "For Automation";
		String orderNumber = "Order Number";
		String productName = "Item # and Name";
		String name = "Name";
		String emailID = "Email Address";
		String problemReason = "Problem Reason";
		String messageTag = "Message";

		String orderNumberAtReportProblemPage = null;
		String productNameAtReportProblemPage = null;
		String nameAtReportProblemPage = null;
		String emailIDAtReportProblemPage = null;
		String problemReasonAtReportProblemPage = "Where's the shipment";

		String orderNumberAtReportConfirmationPage = null;
		String productNameAtReportConfirmationPage = null;
		String nameAtReportConfirmationPage = null;
		String emailIDAtReportConfirmationPage = null;
		String ProblemReasonAtReportConfirmationPage = null;
		String MessageAtReportConfirmationPage = null;

		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(reportProblemsLink);
		s_assert.assertTrue(sfOrdersPage.isProblemDDPresentAtReportProblemPage(), "Problem DD is not present at report problem page");
		s_assert.assertTrue(sfOrdersPage.isMessageBoxPresentAtReportProblemPage(), "Message box is not present at report problem page");
		s_assert.assertTrue(sfOrdersPage.isProblemDropdownOptionsPresent(reasonWhereIsTheShipment), "Problem reason "+reasonWhereIsTheShipment+" is not present");
		s_assert.assertTrue(sfOrdersPage.isProblemDropdownOptionsPresent(reasonOrderIsIncorrect), "Problem reason "+reasonOrderIsIncorrect+" is not present");
		s_assert.assertTrue(sfOrdersPage.isProblemDropdownOptionsPresent(reasonNeedToReturnAnItem), "Problem reason "+reasonNeedToReturnAnItem+" is not present");
		s_assert.assertTrue(sfOrdersPage.isProblemDropdownOptionsPresent(reasonDontKnowWasEnrolledInAutoship), "Problem reason "+reasonDontKnowWasEnrolledInAutoship+" is not present");
		s_assert.assertTrue(sfOrdersPage.isProblemDropdownOptionsPresent(reasonShipmentWasDamaged), "Problem reason "+reasonShipmentWasDamaged+" is not present");
		orderNumberAtReportProblemPage = sfOrdersPage.getOrderNumberFromOrderReportProblemPage();
		productNameAtReportProblemPage = sfOrdersPage.getProductNameFromOrderReportProblemPage();
		nameAtReportProblemPage = sfOrdersPage.getNameFromOrderReportProblemPage();
		emailIDAtReportProblemPage = sfOrdersPage.getEmailFromOrderReportProblemPage();
		sfOrdersPage.enterTheDetailsForReportProblem(message, reasonWhereIsTheShipment);
		sfOrdersPage.clickSubmitBtnAtReportProblemPage();
		orderNumberAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(orderNumber);
		productNameAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(productName);
		nameAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(name);
		emailIDAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(emailID);
		ProblemReasonAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(problemReason);
		MessageAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(messageTag);
		s_assert.assertTrue(nameAtReportConfirmationPage.contains(nameAtReportProblemPage), "Expected name at report confirmation page is "+nameAtReportProblemPage+" Actual on UI is "+nameAtReportConfirmationPage);
		s_assert.assertTrue(emailIDAtReportConfirmationPage.contains(emailIDAtReportProblemPage), "Expected email address at report confirmation page is "+emailIDAtReportProblemPage+" Actual on UI is "+emailIDAtReportConfirmationPage);
		s_assert.assertTrue(productNameAtReportConfirmationPage.contains(productNameAtReportProblemPage), "Expected product name at report confirmation page is "+productNameAtReportProblemPage+" Actual on UI is "+productNameAtReportConfirmationPage);
		s_assert.assertTrue(orderNumberAtReportConfirmationPage.contains(orderNumberAtReportProblemPage), "Expected order number at report confirmation page is "+productNameAtReportProblemPage+" Actual on UI is "+orderNumberAtReportConfirmationPage);
		s_assert.assertTrue(problemReasonAtReportProblemPage.contains(ProblemReasonAtReportConfirmationPage), "Expected Problem reason at report confirmation page is "+problemReasonAtReportProblemPage+" Actual on UI is "+ProblemReasonAtReportConfirmationPage);
		s_assert.assertTrue(MessageAtReportConfirmationPage.contains(message), "Expected order number at report confirmation page is "+message+" Actual on UI is "+MessageAtReportConfirmationPage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-521 Order Details - Action>Details link
	 * Description : This test validates the action - order details and Report a problem for order.
	 *     
	 */
	@Test(enabled=true)
	public void testOrderDetailsActionDetailslink_521(){
		String currentUrlOfOrdersPage = null;
		String currentUrlOfReportProblemPagePage = null;
		String urlOfOrderDetailPageToassert = null;
		String urlOfReportAProblemPageToAssert = null;
		String orderNumber = null;
		String titleOfOrderDetailsPage  = null;
		String headerToAssert = "report a problem";
		String actualHeaderOnUI = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		orderNumber = sfOrdersPage.getFirstOrderNumberFromOrderHistory();
		urlOfOrderDetailPageToassert = "my-account/order/" + orderNumber;
		urlOfReportAProblemPageToAssert = "my-account/order/" + orderNumber + "/reportproblem" ;
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder("Details");
		currentUrlOfOrdersPage = sfOrdersPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentUrlOfOrdersPage.contains(urlOfOrderDetailPageToassert),"Expected URL should contain "+urlOfOrderDetailPageToassert+" but actual on UI is "+currentUrlOfOrdersPage);
		titleOfOrderDetailsPage = sfOrdersPage.getTitleOfOrderDetailsPage();
		s_assert.assertTrue(titleOfOrderDetailsPage.contains(TestConstants.TITLE_OF_ORDER_DETAILS_PAGE),"Expected Title of Order Details page : " + TestConstants.TITLE_OF_ORDER_DETAILS_PAGE + " but actual on UI : " + titleOfOrderDetailsPage);
		sfOrdersPage.navigateBackToOrdersPage();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder("Report Problems");
		currentUrlOfReportProblemPagePage = sfOrdersPage.getCurrentURL().toLowerCase(); 
		s_assert.assertTrue(currentUrlOfReportProblemPagePage.contains(urlOfReportAProblemPageToAssert),"Expected URL should contain "+urlOfReportAProblemPageToAssert+" but actual on UI is "+currentUrlOfReportProblemPagePage);
		actualHeaderOnUI = sfOrdersPage.getHeaderOfPage().toLowerCase();
		s_assert.assertTrue(actualHeaderOnUI.contains(headerToAssert),"Expected Header should contain : " + headerToAssert.toUpperCase() + " but actual on UI : " + actualHeaderOnUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-543 Order Details-Track Order status for new order
	 * Description : This test validates the status of newly placed order on orders page
	 *     
	 */
	@Test(enabled=true)
	public void testOrderDetailsTrackOrderStatusForNewOrder_543(){
		//covered in Orders TC-520
	}

	/***
	 * qTest : TC-483 Order History- Report a Problem-Product Level-With Empty fields
	 * Description : 
	 *     
	 */
	@Test(enabled=true)
	public void testOrderHistoryReportAProblemProductLevelWithEmptyFields_483(){
		String currentUrlOfReportProblemPagePage = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		String orderNumber = sfOrdersPage.getFirstOrderNumberFromOrderHistory();
		String urlOfReportAProblemPageToAssert = "my-account/order/" + orderNumber + "/reportproblem" ;
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder("Report Problems");
		currentUrlOfReportProblemPagePage = sfOrdersPage.getCurrentURL(); 
		s_assert.assertTrue(currentUrlOfReportProblemPagePage.contains(urlOfReportAProblemPageToAssert),"Expected URL should contain "+urlOfReportAProblemPageToAssert+" but actual on UI is "+currentUrlOfReportProblemPagePage);
		sfOrdersPage.enterTheDetailsForReportProblemExcludingProductSelection("auto message");
		sfOrdersPage.clickSubmitBtnAtReportProblemPage();
		s_assert.assertTrue(sfOrdersPage.isErrorMsgPresentForNotSelectingProductCheckbox(),"Error message is not present for not selecting Product checkbox");
		sfOrdersPage.clickOnTheImageOfProductForSelectingChckBox();
		sfOrdersPage.clearTheDetailsEnteredForReportProblem();
		sfOrdersPage.clickSubmitBtnAtReportProblemPage();
		s_assert.assertTrue(sfOrdersPage.isErrorMsgForEmailPresent(),"Error message is not present for email details");
		s_assert.assertTrue(sfOrdersPage.isErrorMsgForProblemDDPresent(),"Error message is not present for problem dropdown details");
		s_assert.assertTrue(sfOrdersPage.isErrorMsgForReportProblemMessageTextFieldPresent(),"Error message is not present for report problem message details");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-476 Retail User Checkout- Choose a Consultant - single search
	 * Description : This test case  placed adhoc order through RC selecting a sponsor in a single search
	 * 
	 */
	@Test(enabled=true)
	public void testRetailUserCheckoutChooseAConsultantSingleSearch_476(){
		//already covered in TC-477 Orders
	}


	/***
	 * qTest : TC-479 Retail User Checkout- Choose a Consultant - Checkout with Corporate
	 * Description : This test case  placed adhoc order through RC continue without a consultant
	 * 
	 */
	@Test(enabled=true)
	public void testRetailUserCheckoutChooseAConsultantCheckoutWithCorporate_479(){
		String sponsorName = "RF Corporate";
		String sponsorNameFromUI = null;
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sponsorNameFromUI = sfCheckoutPage.getSponsorInfo();
		s_assert.assertTrue(sponsorNameFromUI.contains(sponsorName), "Expected sponsor name is "+sponsorName+" but actual on UI is "+sponsorNameFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-322 Edit a Ship address- Checkout - Invalid details
	 * Description : This test validates error message for blank fields & Invalid postal code
	 * at checkout page for consultant 
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testEditAShipAddressCheckoutInvalidDetails_322(){
		String randomWord = CommonUtils.getRandomWord(5);
		lastName = lastName+randomWord;
		String invalidPostalCode = "123";
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingAddress();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, addressLine2, city, state, invalidPostalCode, phoneNumber);
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForPostalCode(), "Error message is not present for postal code field");
		sfCheckoutPage.clearAllFieldsForShippingAddressAtCheckoutPage();
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForFirstAndLastName(), "Error message is not present for first and last name field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForAddressLine1(), "Error message is not present for address line 1 field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForCity(), "Error message is not present for city field");
		//s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForState(), "Error message is not present for state field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForPostalCode(), "Error message is not present for postal code field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForPhoneNumber(), "Error message is not present for phone number field");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-321 Edit a Ship address- Checkout - Valid details
	 * Description : This test edit the ship address and validates it at checkout page 
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testEditAShipAddressCheckoutValidDetails_321(){
		//Already covered in AddAndDeletShippingTest
	}

	/***
	 * qTest : TC-471 Review and Place Order - Edit cart
	 * Description : This test case validates Edit link of cart at order review page
	 *     
	 */
	@Test(enabled=true)
	public void testReviewAndPlaceOrderEditCart_471(){
		String productQuantityFromUI = null;
		String updatedQuantity = null;
		String checkoutPageText = "Account Info";
		String currentURL = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfOrderSummarySection();
		s_assert.assertTrue(sfCheckoutPage.isCartPagePresent(), "Cart page is not displayed after clicked on edit link of order summary section");
		productQuantityFromUI = sfCartPage.getQuantityOfProductFromCart("1");
		updatedQuantity = sfCartPage.updateQuantityByOne(productQuantityFromUI);
		sfCartPage.enterQuantityOfProductAtCart("1", updatedQuantity);
		sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		currentURL = sfCartPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains("checkout") && sfCheckoutPage.isTextPresent(checkoutPageText),"Current url should contain checkout but actual on UI is "+currentURL+" and checkout page is not present");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-323 Add a ship address- Checkout - First Time
	 * Description : This test cases login with a user who doesn't have any shipping profile
	 * and add a shipping address and validates the next billing page
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testAddAShipAddressCheckoutFirstTime_323(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String emailID = TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_SUFFIX;
		String randomWord = CommonUtils.getRandomWord(5);
		lastName = lastName+randomWord;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_RC,firstName, lastName, emailID, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_RC);
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfCheckoutPage.isWelcomeDropdownPresent(), "Welcome dropdown is not present at cart page & user not registered successfully");
		sfCheckoutPage.clickWelcomeDropdown();
		sfCheckoutPage.logout();
		sfCheckoutPage.loginToStoreFront(emailID, password,true);
		sfCheckoutPage.clickMiniCartBagLink();
		sfCheckoutPage.checkoutTheCart();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfCheckoutPage.isBillingLinkPresentAtCheckoutPage(),"User successfully redirected to payment page after added a shipping address");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-457 Choose a consultant- R+F Corporate sponsor - PC First checkout from Corp
	 * Description : This test case validates the sponsor functionality and before enrolled PC
	 * & after enrollment PC can not change the sponsor 
	 *     
	 */
	@Test(enabled=true)
	public void testChooseAConsultantRFCorporateSponsorPCFirstCheckoutFromCorp_457(){
		String timeStamp = CommonUtils.getCurrentTimeStamp();
		String randomWord = CommonUtils.getRandomWord(5);
		lastName = lastName+randomWord;
		String emailID = TestConstants.FIRST_NAME+timeStamp+TestConstants.EMAIL_SUFFIX;
		String sponsorName = "RF Corporate";
		String sponsorNameFromUI = null;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT, validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC,firstName, lastName, emailID, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		sfCheckoutPage.checkoutTheCart();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sponsorNameFromUI = sfCheckoutPage.getSponsorInfo();
		s_assert.assertTrue(sponsorNameFromUI.contains(sponsorName), "Expected sponsor name is "+sponsorName+" but actual on UI is "+sponsorNameFromUI);
		sfCheckoutPage.clickNotYourConsultantLink();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sponsorNameFromUI = sfCheckoutPage.getSponsorInfo();
		s_assert.assertTrue(sponsorNameFromUI.contains(sponsorName), "Expected sponsor name is "+sponsorName+" but actual on UI is "+sponsorNameFromUI);
		sfCheckoutPage.clickNotYourConsultantLink();
		s_assert.assertTrue(sfCheckoutPage.isSponsorSearchBoxVisible(), "Sponsor search box is not visible after clicked on not your consultant link");
		sfCheckoutPage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		s_assert.assertTrue(sponsorNameFromUI.contains(sponsorName), "Expected sponsor name is "+sponsorName+" but actual on UI is at last "+sponsorNameFromUI);
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectIAcknowledgePCChkBox();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfHomePage= sfCheckoutPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.checkoutTheCart();
		s_assert.assertFalse(sfCheckoutPage.isNotYourConsultantLinkPresent(), "Not your sponsor link is present for consultant's sponsor");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-227 Order History- Report a Problem- Field Validations
	 * Description : This test validates report problems page & field validations
	 *     
	 */
	@Test(enabled=true)
	public void testOrderHistoryReportAProblemFieldValidations_227(){
		// covered in TC-228
	}

	/***
	 * qTest : TC-228 Order History- Report a Problem- Confirmation page
	 * Description : This test validates return order request & details
	 *     
	 */
	@Test(enabled=true)
	public void testOrderHistoryReportAProblemConfirmationPage_228(){
		String reportProblemsLink = "Report Problems";
		String expectedConfirmationMessage = TestConstants.CONFIRMATION_MSG_FOR_REPORT_PROBLEMS;
		String confirmationMessageFromUI = null;
		String message = "For Automation";
		String orderNumberAtReportProblemPage = null;
		String productNameAtReportProblemPage = null;
		String nameAtReportProblemPage = null;
		String emailIDAtReportProblemPage = null;
		String problemReasonAtReportProblemPage = null;
		String orderNumberAtReportConfirmationPage = null;
		String productNameAtReportConfirmationPage = null;
		String nameAtReportConfirmationPage = null;
		String emailIDAtReportConfirmationPage = null;
		String ProblemReasonAtReportConfirmationPage = null;
		String MessageAtReportConfirmationPage = null;
		String orderNumber = "Order Number";
		String productName = "Item # and Name";
		String name = "Name";
		String emailID = "Email Address";
		String problemReason = "Problem Reason";
		String messageTag = "Message";
		String prefix = pwsPrefix();
		String reasonWhereIsTheShipment = "\"Where's the shipment\"";
		String reasonOrderIsIncorrect = "\"Order is incorrect\"";
		String reasonNeedToReturnAnItem = "\"Need to return an item or an order\"";
		String reasonDontKnowWasEnrolledInAutoship = "\"DIdn't know was enrolled in an autoship\"";
		String reasonShipmentWasDamaged = "\"My Shipment was damaged or missing items\"";
		String homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(reportProblemsLink);
		// get the order details
		orderNumberAtReportProblemPage = sfOrdersPage.getOrderNumberFromOrderReportProblemPage();
		productNameAtReportProblemPage = sfOrdersPage.getProductNameFromOrderReportProblemPage();
		nameAtReportProblemPage = sfOrdersPage.getNameFromOrderReportProblemPage();
		emailIDAtReportProblemPage = sfOrdersPage.getEmailFromOrderReportProblemPage();
		// assert the validation fields
		s_assert.assertFalse(sfOrdersPage.isNameFieldEditableAtReportProblemPage(name), "Name fiels is editable at report problem page");
		s_assert.assertTrue(sfOrdersPage.isEmailFieldPresentAtReportProblemPage(), "Email field is not present at report problem page");
		s_assert.assertTrue(sfOrdersPage.isProblemDDPresentAtReportProblemPage(), "Problem DD is not present at report problem page");
		s_assert.assertTrue(sfOrdersPage.isMessageBoxPresentAtReportProblemPage(), "Message box is not present at report problem page");
		s_assert.assertTrue(sfOrdersPage.isMessageInstructionsPresentAtReportProblemPage(), "Message instruction as max 800 char including spaces is not present at report problem page");
		sfOrdersPage.clearTheDetailsEnteredForReportProblem();
		sfOrdersPage.clickSubmitBtnAtReportProblemPage();
		s_assert.assertTrue(sfOrdersPage.isErrorMsgPresentForNotSelectingProductCheckbox(),"Error message is not present for not selecting Product checkbox");
		s_assert.assertTrue(sfOrdersPage.isErrorMsgForEmailPresent(),"Error message is not present for email details");
		s_assert.assertTrue(sfOrdersPage.isErrorMsgForProblemDDPresent(),"Error message is not present for problem dropdown details");
		s_assert.assertTrue(sfOrdersPage.isErrorMsgForReportProblemMessageTextFieldPresent(),"Error message is not present for report problem message details");
		// verify Reason codes in problem DD
		s_assert.assertTrue(sfOrdersPage.isProblemDropdownOptionsPresent(reasonWhereIsTheShipment), "Problem reason "+reasonWhereIsTheShipment+" is not present");
		s_assert.assertTrue(sfOrdersPage.isProblemDropdownOptionsPresent(reasonOrderIsIncorrect), "Problem reason "+reasonOrderIsIncorrect+" is not present");
		s_assert.assertTrue(sfOrdersPage.isProblemDropdownOptionsPresent(reasonNeedToReturnAnItem), "Problem reason "+reasonNeedToReturnAnItem+" is not present");
		s_assert.assertTrue(sfOrdersPage.isProblemDropdownOptionsPresent(reasonDontKnowWasEnrolledInAutoship), "Problem reason "+reasonDontKnowWasEnrolledInAutoship+" is not present");
		s_assert.assertTrue(sfOrdersPage.isProblemDropdownOptionsPresent(reasonShipmentWasDamaged), "Problem reason "+reasonShipmentWasDamaged+" is not present");

		// enter the details
		sfOrdersPage.enterEmailIdAtReportProblemPage(emailIDAtReportProblemPage);
		sfOrdersPage.enterTheDetailsForReportProblem(message);
		problemReasonAtReportProblemPage = sfOrdersPage.getSelectedProblemReasonFromOrderReportPage().toLowerCase();
		sfOrdersPage.clickSubmitBtnAtReportProblemPage();
		confirmationMessageFromUI = sfOrdersPage.getConfirmationMsgOfReportProblem().toLowerCase();
		s_assert.assertTrue(confirmationMessageFromUI.contains(expectedConfirmationMessage.toLowerCase()), "Expected confirmation message is"+expectedConfirmationMessage+" but actual on UI is "+confirmationMessageFromUI);
		orderNumberAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(orderNumber);
		productNameAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(productName);
		nameAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(name);
		emailIDAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(emailID);
		ProblemReasonAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(problemReason).toLowerCase();
		MessageAtReportConfirmationPage = sfOrdersPage.getInformationAtOrderReportConfirmationPage(messageTag);
		s_assert.assertTrue(nameAtReportConfirmationPage.contains(nameAtReportProblemPage), "Expected name at report confirmation page is "+nameAtReportProblemPage+" Actual on UI is "+nameAtReportConfirmationPage);
		s_assert.assertTrue(emailIDAtReportConfirmationPage.contains(emailIDAtReportProblemPage), "Expected email address at report confirmation page is "+emailIDAtReportProblemPage+" Actual on UI is "+emailIDAtReportConfirmationPage);
		s_assert.assertTrue(productNameAtReportConfirmationPage.contains(productNameAtReportProblemPage), "Expected product name at report confirmation page is "+productNameAtReportProblemPage+" Actual on UI is "+productNameAtReportConfirmationPage);
		s_assert.assertTrue(orderNumberAtReportConfirmationPage.contains(orderNumberAtReportProblemPage), "Expected order number at report confirmation page is "+productNameAtReportProblemPage+" Actual on UI is "+orderNumberAtReportConfirmationPage);
		s_assert.assertTrue(problemReasonAtReportProblemPage.contains(ProblemReasonAtReportConfirmationPage), "Expected Problem reason at report confirmation page is "+problemReasonAtReportProblemPage+" Actual on UI is "+ProblemReasonAtReportConfirmationPage);
		s_assert.assertTrue(MessageAtReportConfirmationPage.contains(message), "Expected confirmation message is "+message+" Actual on UI is "+MessageAtReportConfirmationPage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-315 Ship method- Ad Hoc carts
	 * Description : This test case validates the change in shipping method for the adhoc order 
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testShipMethodAdHocCarts_315(){
		String selectedShippingMethodLabel = null;
		String changedShippingMethodLabel = null;
		String titleOfSelectedShipppingMethod = null;
		String titleOfShippingMethodAfterNextButton = null;
		String shippingMethodAtOrderConfirmationPage = null;
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickShippingDetailsEditButton();
		changedShippingMethodLabel = sfCheckoutPage.changeTheShippingMethodAtCheckoutPage();
		selectedShippingMethodLabel = sfCheckoutPage.getLabelOfSelectedShippingMethod();
		titleOfSelectedShipppingMethod = sfCheckoutPage.getTitleOfSelectedShippingMethod();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		titleOfShippingMethodAfterNextButton = sfCheckoutPage.getTitleOfShippingMethodFromShippingDetails();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		shippingMethodAtOrderConfirmationPage = sfCheckoutPage.getShippingMethodAfterPlacedOrder();
		s_assert.assertTrue(selectedShippingMethodLabel.contains(changedShippingMethodLabel),"Shipping method does not get updated at Checkout page. Expected : " + changedShippingMethodLabel + ". Actual : " + selectedShippingMethodLabel);
		s_assert.assertTrue(titleOfSelectedShipppingMethod.contains(titleOfShippingMethodAfterNextButton),"Shipping method title is not found as expected after Shipping details next button. Expected : " + titleOfSelectedShipppingMethod + ". Actual : " + titleOfShippingMethodAfterNextButton);
		s_assert.assertTrue(shippingMethodAtOrderConfirmationPage.contains(titleOfSelectedShipppingMethod),"Shipping method is not found as expected on order confirmation page after placing order. Expected : " + titleOfSelectedShipppingMethod + ". Actual : " + shippingMethodAtOrderConfirmationPage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-318 Address Verification - Billing Profile
	 * Description : This test case edit and add billing profile with address
	 * and verify it 
	 *     
	 */
	@Test(enabled=true)
	public void testAddressVerificationBillingProfile_318(){
		//Already covered in BillingProfile	
	}

	/***
	 * qTest : TC-517 Order history- Return Orders/Credits - Returns Don't exist
	 * Description : This test validates empty message for return orders
	 *     
	 */
	@Test(enabled=true)
	public void testOrderHistoryReturnOrderReturnDoNotExist_517(){
		String noReturnOrderMessage = "You have no Return orders";
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		s_assert.assertTrue(sfOrdersPage.isTextVisible(noReturnOrderMessage), "No message for empty return order section");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-520 Order Details - Order number link
	 * Description : This test case validates SubTotal, Grand Total, Shipping charges, Item name, product qty, SV, 
	 * shipping method, Shipping profile name, Billing profile name, CC details
	 * at order Details page
	 *     
	 */
	@Test(enabled=true)
	public void testOrderDetailsOrderNumberLink_520(){
		String orderNumber = null;
		String productName=null;
		String productQuantity="Qty";
		String subTotal=null;
		String orderTotal=null;
		String shippingProfile=null;
		String shippingCharges=null;
		String shippingMethodBeforeOrderPlaced=null;
		String ccfourDigits  = null;
		String ccExpiryDate = null;
		String billingProfileName  = null;
		String billingProfileLastName = null;
		String text_Subtotal = "Subtotal";
		String text_Shipping = "Shipping";
		String text_GrandTotal = "GRAND TOTAL";
		String text_Delivery = "Delivery";
		String text_OrderTotal = "Total";
		String orderStatus= null;
		String shippingProfileFromOrderConfirmationPage = null;
		String shippingMethodFromOrderConfirmationPage = null;
		String billingProfileNameFromOrderConfirmationPage = null;
		String lastFourDigitOfCCFromOrderConfirmationPage = null;
		String expDateOfCCFromOrderConfirmationPage = null;
		String grandTotalAtOrderConfirmationPage = null;
		String shippingChargeAtOrderConfirmationPage = null;
		String subTotalAtOrderConfirmationPage = null;
		String productQtyAtOrderDetailsPage = null;

		//sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC, validProductId);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		productName = sfCartPage.getProductName("1");
		productQuantity = sfCartPage.getQuantityOfProductFromCart("1");
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		//shippingMethodBeforeOrderPlaced =  sfCheckoutPage.getSelectedShippingMethodName();
		shippingProfile = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		billingProfileName = sfCheckoutPage.getBillingProfileName();
		billingProfileLastName = sfCheckoutPage.getLastName(billingProfileName);
		sfCheckoutPage.getCardDetailsFromBillingInfo(billingProfileLastName);
		ccfourDigits = sfCheckoutPage.getLastFourDigitsOfCardNumberInBillingDetails();
		ccExpiryDate = sfCheckoutPage.getExpiryDateOfCardNumberInBillingDetails();
		shippingCharges = sfCheckoutPage.getShippingChragesAtCheckoutPage();
		orderTotal = sfCheckoutPage.getChargesAccordingToLabelAtOrderReviewPage(text_OrderTotal);
		subTotal = sfCheckoutPage.getChargesAccordingToLabelAtOrderReviewPage(text_Subtotal);
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertTrue(sfCheckoutPage.isTextPresent("Order #"),"Order Number is not present on the confirmation page");
		orderNumber = sfCheckoutPage.getOrderNumberAfterCheckout();
		sfCheckoutPage.clickWelcomeDropdown();
		sfOrdersPage = sfCheckoutPage.navigateToOrdersPage();
		orderStatus = sfOrdersPage.getStatusOfOrderFromOrderHistory(orderNumber).trim();
		s_assert.assertTrue(orderStatus.contains("Submitted")|| orderStatus.contains("In Process"),"Status of Order is not fund as expected. Expected : Submitted/In Process. Actual : "+orderStatus);
		sfOrdersPage.clickOrderNumber(orderNumber);
		productQtyAtOrderDetailsPage = sfOrdersPage.getQuantityOfSpecificProductFromOrdersPage(productName);
		//productSVAtOrderDetailsPage = sfOrdersPage.getProductSVOfAnItem("2").trim();
		//productUnitPriceAtOrderDetailsPage = sfOrdersPage.getProductUnitPriceOfAnItem("2");
		shippingProfileFromOrderConfirmationPage = sfCheckoutPage.getShippingProfileFromConfirmationPage();
		shippingMethodFromOrderConfirmationPage = sfCheckoutPage.getShippingMethodAfterPlacedOrder().split("\\:")[1].trim();
		billingProfileNameFromOrderConfirmationPage = sfCheckoutPage.getBillingProfileAfterPlacedOrder();
		lastFourDigitOfCCFromOrderConfirmationPage = sfCheckoutPage.getLastFourNumbersOfBillingDetailsOnConFirmationPage();
		expDateOfCCFromOrderConfirmationPage = sfCheckoutPage.getExpiryDateOfCardNumberInBillingDetails();
		grandTotalAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_GrandTotal);
		shippingChargeAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_Shipping);
		subTotalAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_Subtotal);
		s_assert.assertTrue(shippingProfile.contains(shippingProfileFromOrderConfirmationPage), "Shipping Profile is not matching on confirmation page. Expected is :"+shippingProfile+" But found is :"+shippingProfileFromOrderConfirmationPage);
		//s_assert.assertTrue(shippingMethodBeforeOrderPlaced.contains(shippingMethodFromOrderConfirmationPage), "Shipping Method is not matching on confirmation page. Expected is :"+shippingMethodBeforeOrderPlaced+" But found is :"+shippingMethodFromOrderConfirmationPage);
		s_assert.assertTrue(billingProfileNameFromOrderConfirmationPage.contains(billingProfileLastName),"Billing Profile is not matching on confirmation page. Expected is :"+billingProfileLastName+" But found is :"+billingProfileNameFromOrderConfirmationPage);
		s_assert.assertTrue(lastFourDigitOfCCFromOrderConfirmationPage.equals(ccfourDigits), "Credit Card Last 4 digits are not matching. Expected is :"+ccfourDigits+" But found is :"+lastFourDigitOfCCFromOrderConfirmationPage);
		s_assert.assertTrue(expDateOfCCFromOrderConfirmationPage.equals(ccExpiryDate), "Credit Card Expiry Date is not matching. Expected is :"+ccExpiryDate+" But found is :"+expDateOfCCFromOrderConfirmationPage);
		s_assert.assertTrue(grandTotalAtOrderConfirmationPage.equals(orderTotal),"Order total is not matching. Expected is:"+orderTotal+"But found is "+grandTotalAtOrderConfirmationPage);
		s_assert.assertTrue(shippingChargeAtOrderConfirmationPage.equals(shippingCharges),"Shipping charge is not matching. Expected is:"+shippingCharges+"But found is "+shippingChargeAtOrderConfirmationPage);
		s_assert.assertTrue(subTotalAtOrderConfirmationPage.equals(subTotal),"Subtotal is not matching. Expected is:"+subTotal+"But found is "+subTotalAtOrderConfirmationPage);
		s_assert.assertTrue(productQtyAtOrderDetailsPage.contains(productQuantity),"Product qty is not matching. Expected is:"+productQuantity+"But found is "+productQtyAtOrderDetailsPage);
		s_assert.assertTrue(sfOrdersPage.isProductNamePresentOnOrderDetailPage(productName),"Product name is not matching. Expected is:"+productName+"But not found");
		//s_assert.assertTrue(productSVValue.contains(productSVAtOrderDetailsPage),"Product SV value is not matching. Expected is:"+productSVValue+"But found is "+productSVAtOrderDetailsPage);
		//s_assert.assertTrue(productUnitPriceAtOrderDetailsPage.contains(yourPrice),"Product unit price is not matching. Expected is:"+yourPrice+"But found is "+productUnitPriceAtOrderDetailsPage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-478 Retail User Checkout- Choose a Consultant - Change selected sponsor
	 * Description : This test case  placed adhoc order through RC change the selected sponsor
	 * 
	 */
	@Test(enabled=true)
	public void testRetailUserCheckoutChooseAConsultantChangeSelectedSponsor_478(){
		// covered in TC-477

	}

	/***
	  * qTest : TC-477 Retail User Checkout- Choose a Consultant - multiple search
	  * Description : This test case  search sponsor multiple times and validate the last selected sponsor
	  * 
	  */
	 @Test(enabled=true)
	 public void testRetailUserCheckoutChooseAConsultantMultipleSearch_477(){
	  String sponsorID = consultantWithPulseAndWithCRP().toLowerCase();
	  sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(), password,true);
	  sfShopSkinCarePage = sfHomePage.clickAllProducts();
	  sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
	  sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
	  sfCheckoutPage = sfCartPage.checkoutTheCart();
	  sfCheckoutPage.searchSponsor(TestConstants.SPONSOR);
	  s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
	  sfCheckoutPage.selectFirstSponsorFromList();
	  sfCheckoutPage.clickRemoveLink();
	  sfCheckoutPage.searchSponsor(sponsorID);
	  s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+sponsorID);
	  sfCheckoutPage.selectFirstSponsorFromList();
	  s_assert.assertTrue(sfCheckoutPage.isSponsorDetailsPresentInSelectedSponsor(sponsorID), "Sponsor ID is not selected");
	  s_assert.assertAll();
	 }
	 
	/***
	 * qTest : TC-313 Ship Method-PC - Existing PC changes Shipping Method
	 * Description : This test case change the all shipping method and verify their charges
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testShipMethodPCExistingPCChangesShippingMethod_313(){
		String shippingMethodWithCost = null;
		String deliveryCharge = null;
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfCartPage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.selectShippingMethod(TestConstants.SHIPPING_METHOD_UPS_GROUND);
		shippingMethodWithCost = sfCheckoutPage.getSelectedShippingMethodName();
		sfCheckoutPage.clickNextbuttonOfShippingDetails();
		deliveryCharge = sfCheckoutPage.getDeliveryChargesAtOrderReviewPage().toLowerCase();
		s_assert.assertTrue(shippingMethodWithCost.contains("$0") && deliveryCharge.contains("free"), "Expected shipping method cost for UPS ground is "+shippingMethodWithCost+" and delivery charge should contain free but actual on UI is "+deliveryCharge);
		sfCheckoutPage.navigateToBackPage();
		sfCheckoutPage.selectShippingMethod(TestConstants.SHIPPING_METHOD_UPS_2DAY);
		shippingMethodWithCost = sfCheckoutPage.getSelectedShippingMethodName();
		sfCheckoutPage.clickNextbuttonOfShippingDetails();
		deliveryCharge = sfCheckoutPage.getDeliveryChargesAtOrderReviewPage().trim();
		s_assert.assertTrue(shippingMethodWithCost.contains(deliveryCharge), "Expected shipping method cost for UPS 2Day is "+shippingMethodWithCost+" but actual on UI is "+deliveryCharge);
		// For SHIPPING_METHOD_UPS_OVERNIGHT
		sfCheckoutPage.navigateToBackPage();
		sfCheckoutPage.selectShippingMethod(TestConstants.SHIPPING_METHOD_UPS_OVERNIGHT);
		shippingMethodWithCost = sfCheckoutPage.getSelectedShippingMethodName();
		sfCheckoutPage.clickNextbuttonOfShippingDetails();
		deliveryCharge = sfCheckoutPage.getDeliveryChargesAtOrderReviewPage().trim();
		s_assert.assertTrue(shippingMethodWithCost.contains(deliveryCharge), "Expected shipping method cost for UPS Overnight is "+shippingMethodWithCost+" but actual on UI is "+deliveryCharge);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-459 Choose a consultant- R+F Corporate sponsor - RC checkout from Corporate
	 * Description : This test case validates the sponsor functionality and before enrolled RC
	 * & after enrollment RC can change the sponsor
	 *     
	 */
	@Test(enabled=true)
	public void testChooseAConsultantRFCorporateSponsorRCCheckoutFromCorporate_459(){
		String timeStamp = CommonUtils.getCurrentTimeStamp();
		String randomWord = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME+randomWord;
		String emailID = TestConstants.FIRST_NAME+timeStamp+TestConstants.EMAIL_SUFFIX;
		String sponsorName = "RF Corporate";
		String sponsorNameFromUI = null;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT, validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_RC,firstName, lastName, emailID, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_RC);
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sponsorNameFromUI = sfCheckoutPage.getSponsorInfo();
		s_assert.assertTrue(sponsorNameFromUI.contains(sponsorName), "Expected sponsor name is "+sponsorName+" but actual on UI is "+sponsorNameFromUI);
		sfCheckoutPage.clickNotYourConsultantLink();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sponsorNameFromUI = sfCheckoutPage.getSponsorInfo();
		s_assert.assertTrue(sponsorNameFromUI.contains(sponsorName), "Expected sponsor name is "+sponsorName+" but actual on UI is "+sponsorNameFromUI);
		sfCheckoutPage.clickNotYourConsultantLink();
		s_assert.assertTrue(sfCheckoutPage.isSponsorSearchBoxVisible(), "Sponsor search box is not visible after clicked on not your consultant link");
		sfCheckoutPage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		s_assert.assertTrue(sponsorNameFromUI.contains(sponsorName), "Expected sponsor name is "+sponsorName+" but actual on UI is at last "+sponsorNameFromUI);
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfHomePage= sfCheckoutPage.clickRodanAndFieldsLogo();
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		sfHomePage.loginToStoreFront(emailID,password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		s_assert.assertTrue(sfCheckoutPage.isNotYourConsultantLinkPresentForSponsor(), "Not your sponsor link is not present for consultant's sponsor");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-487 Order History- Report a Problem-Product Level-With Empty fields
	 * Description : This test validates the error messages for Report problem details Fields. 
	 *     
	 */
	@Test(enabled=true)
	public void testOrderHistoryReportAProblemProductLevelWithEmptyFields_487(){
		//duplicate test Same as TC-483
	}

	/***
	 * qTest : TC-233 Shipping method cost should reduce for consultant adhoc order with an SV 100+
	 * Description : This test case validates shipping method cost should reduce while adding a product > 100$
	 *     
	 */
	@Test(enabled=false)
	public void testShippingMethodCostShouldReduceForConsultantAdhocOrderWithMoreThanHundredSVValue_233(){
		double shippingMethodWithCostBefore = 0.00;
		double shippingMethodWithCostAfter = 0.00;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.productPriceFilterLowToHigh();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		shippingMethodWithCostBefore = Double.parseDouble(sfCheckoutPage.getSelectedShippingMethodName().split("\\$")[1]);
		sfCheckoutPage.clickRodanAndFieldsLogo();
		sfCheckoutPage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		shippingMethodWithCostAfter = Double.parseDouble(sfCheckoutPage.getSelectedShippingMethodName().split("\\$")[1]);
		s_assert.assertTrue(shippingMethodWithCostAfter<shippingMethodWithCostBefore, "Expected shipping method cost should be less than from"+shippingMethodWithCostBefore+" but actual on UI is "+shippingMethodWithCostAfter);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-354 PC user is in the process of placing an adhoc order less than 90$
	 * Description : This test case validates threshold message for less than 90$
	 *  at cart page
	 *     
	 */
	@Test(enabled=true)
	public void testPCUserIsInTheProcessOfPlacingAnAdhocOrderLessThan90_354(){
		double deliverCharges = 0.00; 
		String errorMessage = "more to your cart to qualify for FREE shipping";
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.productPriceFilterLowToHigh();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickMiniCartBagLink();
		s_assert.assertTrue(sfCartPage.isErrorMessagePresentForThreshold(errorMessage), "Threshold message is present for a product more than 90$");
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		deliverCharges = Double.parseDouble(sfCheckoutPage.getDeliveryChargesAtOrderReviewPage().split("\\$")[1]);
		s_assert.assertTrue(deliverCharges>0.00, "Deliver charges not applied for the order total < 100$");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-355 PC user is in the process of placing an adhoc order more than 90$
	 * Description : This test case validates threshold message & delivery charges for more than 90$
	 *  at cart page
	 *     
	 */
	@Test(enabled=true)
	public void testPCUserIsInTheProcessOfPlacingAnAdhocOrderMoreThan90_355(){
		String deliveryCharges = null; 
		String errorMessage = "more to your cart to qualify for FREE shipping";
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickMiniCartBagLink();
		s_assert.assertFalse(sfCartPage.isErrorMessagePresentForThreshold(errorMessage), "Threshold message is present for a product more than 90$");
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.selectShippingMethod(TestConstants.SHIPPING_METHOD_UPS_GROUND);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		deliveryCharges = sfCheckoutPage.getDeliveryChargesAtOrderReviewPage().toLowerCase();
		s_assert.assertTrue(deliveryCharges.contains("free"), "Delivery charges applied for the order total > 100$ actual on UI is "+deliveryCharges);
		s_assert.assertAll();
	}

	@Test
	public void testPlacedAnAdhocOrderFromPC(){
		String orderNumber = null;
		String productName=null;
		String productQuantity="Qty";
		String subTotal=null;
		String orderTotal=null;
		String shippingProfile=null;
		String shippingCharges=null;
		String shippingMethodBeforeOrderPlaced=null;
		String ccfourDigits  = null;
		String ccExpiryDate = null;
		String billingProfileName  = null;
		String billingProfileLastName = null;
		String text_Subtotal = "Subtotal";
		String text_Shipping = "Shipping";
		String text_GrandTotal = "GRAND TOTAL";
		String text_OrderTotal = "Total";
		String orderStatus= null;
		String shippingProfileFromOrderConfirmationPage = null;
		String shippingMethodFromOrderConfirmationPage = null;
		String billingProfileNameFromOrderConfirmationPage = null;
		String lastFourDigitOfCCFromOrderConfirmationPage = null;
		String expDateOfCCFromOrderConfirmationPage = null;
		String grandTotalAtOrderConfirmationPage = null;
		String shippingChargeAtOrderConfirmationPage = null;
		String subTotalAtOrderConfirmationPage = null;
		String productQtyAtOrderDetailsPage = null;
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC, validProductId);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		productName = sfCartPage.getProductName("1");
		productQuantity = sfCartPage.getQuantityOfProductFromCart("1");
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		//shippingMethodBeforeOrderPlaced =  sfCheckoutPage.getSelectedShippingMethodName();
		shippingProfile = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		billingProfileName = sfCheckoutPage.getBillingProfileName();
		billingProfileLastName = sfCheckoutPage.getLastName(billingProfileName);
		sfCheckoutPage.getCardDetailsFromBillingInfo(billingProfileLastName);
		ccfourDigits = sfCheckoutPage.getLastFourDigitsOfCardNumberInBillingDetails();
		ccExpiryDate = sfCheckoutPage.getExpiryDateOfCardNumberInBillingDetails();
		shippingCharges = sfCheckoutPage.getShippingChragesAtCheckoutPage();
		orderTotal = sfCheckoutPage.getChargesAccordingToLabelAtOrderReviewPage(text_OrderTotal);
		subTotal = sfCheckoutPage.getChargesAccordingToLabelAtOrderReviewPage(text_Subtotal);
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertTrue(sfCheckoutPage.isTextPresent("Order #"),"Order Number is not present on the confirmation page");
		orderNumber = sfCheckoutPage.getOrderNumberAfterCheckout();
		sfCheckoutPage.clickWelcomeDropdown();
		sfOrdersPage = sfCheckoutPage.navigateToOrdersPage();
		orderStatus = sfOrdersPage.getStatusOfOrderFromOrderHistory(orderNumber).trim();
		s_assert.assertTrue(orderStatus.contains("Submitted")|| orderStatus.contains("In Process"),"Status of Order is not fund as expected. Expected : Submitted/In Process. Actual : "+orderStatus);
		sfOrdersPage.clickOrderNumber(orderNumber);
		productQtyAtOrderDetailsPage = sfOrdersPage.getQuantityOfSpecificProductFromOrdersPage(productName);
		//productSVAtOrderDetailsPage = sfOrdersPage.getProductSVOfAnItem("2").trim();
		//productUnitPriceAtOrderDetailsPage = sfOrdersPage.getProductUnitPriceOfAnItem("2");
		shippingProfileFromOrderConfirmationPage = sfCheckoutPage.getShippingProfileFromConfirmationPage();
		shippingMethodFromOrderConfirmationPage = sfCheckoutPage.getShippingMethodAfterPlacedOrder().split("\\:")[1].trim();
		billingProfileNameFromOrderConfirmationPage = sfCheckoutPage.getBillingProfileAfterPlacedOrder();
		lastFourDigitOfCCFromOrderConfirmationPage = sfCheckoutPage.getLastFourNumbersOfBillingDetailsOnConFirmationPage();
		expDateOfCCFromOrderConfirmationPage = sfCheckoutPage.getExpiryDateOfCardNumberInBillingDetails();
		grandTotalAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_GrandTotal);
		shippingChargeAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_Shipping);
		subTotalAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_Subtotal);
		s_assert.assertTrue(shippingProfile.contains(shippingProfileFromOrderConfirmationPage), "Shipping Profile is not matching on confirmation page. Expected is :"+shippingProfile+" But found is :"+shippingProfileFromOrderConfirmationPage);
		//s_assert.assertTrue(shippingMethodBeforeOrderPlaced.contains(shippingMethodFromOrderConfirmationPage), "Shipping Method is not matching on confirmation page. Expected is :"+shippingMethodBeforeOrderPlaced+" But found is :"+shippingMethodFromOrderConfirmationPage);
		s_assert.assertTrue(billingProfileNameFromOrderConfirmationPage.contains(billingProfileLastName),"Billing Profile is not matching on confirmation page. Expected is :"+billingProfileLastName+" But found is :"+billingProfileNameFromOrderConfirmationPage);
		s_assert.assertTrue(lastFourDigitOfCCFromOrderConfirmationPage.equals(ccfourDigits), "Credit Card Last 4 digits are not matching. Expected is :"+ccfourDigits+" But found is :"+lastFourDigitOfCCFromOrderConfirmationPage);
		s_assert.assertTrue(expDateOfCCFromOrderConfirmationPage.equals(ccExpiryDate), "Credit Card Expiry Date is not matching. Expected is :"+ccExpiryDate+" But found is :"+expDateOfCCFromOrderConfirmationPage);
		s_assert.assertTrue(grandTotalAtOrderConfirmationPage.equals(orderTotal),"Order total is not matching. Expected is:"+orderTotal+"But found is "+grandTotalAtOrderConfirmationPage);
		s_assert.assertTrue(shippingChargeAtOrderConfirmationPage.equals(shippingCharges),"Shipping charge is not matching. Expected is:"+shippingCharges+"But found is "+shippingChargeAtOrderConfirmationPage);
		s_assert.assertTrue(subTotalAtOrderConfirmationPage.equals(subTotal),"Subtotal is not matching. Expected is:"+subTotal+"But found is "+subTotalAtOrderConfirmationPage);
		s_assert.assertTrue(productQtyAtOrderDetailsPage.contains(productQuantity),"Product qty is not matching. Expected is:"+productQuantity+"But found is "+productQtyAtOrderDetailsPage);
		s_assert.assertTrue(sfOrdersPage.isProductNamePresentOnOrderDetailPage(productName),"Product name is not matching. Expected is:"+productName+"But not found");
		//s_assert.assertTrue(productSVValue.contains(productSVAtOrderDetailsPage),"Product SV value is not matching. Expected is:"+productSVValue+"But found is "+productSVAtOrderDetailsPage);
		//s_assert.assertTrue(productUnitPriceAtOrderDetailsPage.contains(yourPrice),"Product unit price is not matching. Expected is:"+yourPrice+"But found is "+productUnitPriceAtOrderDetailsPage);
		s_assert.assertAll();
	}

	//RC Adhoc Order
	@Test
	public void testPlacedAnAdhocOrderFromRC(){
		String orderNumber = null;
		String productName=null;
		String productQuantity="Qty";
		String subTotal=null;
		String orderTotal=null;
		String shippingProfile=null;
		String shippingCharges=null;
		String shippingMethodBeforeOrderPlaced=null;
		String ccfourDigits  = null;
		String ccExpiryDate = null;
		String billingProfileName  = null;
		String billingProfileLastName = null;
		String text_Subtotal = "Subtotal";
		String text_Shipping = "Shipping";
		String text_GrandTotal = "GRAND TOTAL";
		String text_OrderTotal = "Total";
		String orderStatus= null;
		String shippingProfileFromOrderConfirmationPage = null;
		String shippingMethodFromOrderConfirmationPage = null;
		String billingProfileNameFromOrderConfirmationPage = null;
		String lastFourDigitOfCCFromOrderConfirmationPage = null;
		String expDateOfCCFromOrderConfirmationPage = null;
		String grandTotalAtOrderConfirmationPage = null;
		String shippingChargeAtOrderConfirmationPage = null;
		String subTotalAtOrderConfirmationPage = null;
		String productQtyAtOrderDetailsPage = null;

		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC, validProductId);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		productName = sfCartPage.getProductName("1");
		productQuantity = sfCartPage.getQuantityOfProductFromCart("1");
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		//shippingMethodBeforeOrderPlaced =  sfCheckoutPage.getSelectedShippingMethodName();
		shippingProfile = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		billingProfileName = sfCheckoutPage.getBillingProfileName();
		billingProfileLastName = sfCheckoutPage.getLastName(billingProfileName);
		sfCheckoutPage.getCardDetailsFromBillingInfo(billingProfileLastName);
		ccfourDigits = sfCheckoutPage.getLastFourDigitsOfCardNumberInBillingDetails();
		ccExpiryDate = sfCheckoutPage.getExpiryDateOfCardNumberInBillingDetails();
		shippingCharges = sfCheckoutPage.getShippingChragesAtCheckoutPage();
		orderTotal = sfCheckoutPage.getChargesAccordingToLabelAtOrderReviewPage(text_OrderTotal);
		subTotal = sfCheckoutPage.getChargesAccordingToLabelAtOrderReviewPage(text_Subtotal);
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertTrue(sfCheckoutPage.isTextPresent("Order #"),"Order Number is not present on the confirmation page");
		orderNumber = sfCheckoutPage.getOrderNumberAfterCheckout();
		sfCheckoutPage.clickWelcomeDropdown();
		sfOrdersPage = sfCheckoutPage.navigateToOrdersPage();
		orderStatus = sfOrdersPage.getStatusOfOrderFromOrderHistory(orderNumber).trim();
		s_assert.assertTrue(orderStatus.contains("Submitted")|| orderStatus.contains("In Process"),"Status of Order is not fund as expected. Expected : Submitted/In Process. Actual : "+orderStatus);
		sfOrdersPage.clickOrderNumber(orderNumber);
		productQtyAtOrderDetailsPage = sfOrdersPage.getQuantityOfSpecificProductFromOrdersPage(productName);
		//productSVAtOrderDetailsPage = sfOrdersPage.getProductSVOfAnItem("2").trim();
		//productUnitPriceAtOrderDetailsPage = sfOrdersPage.getProductUnitPriceOfAnItem("2");
		shippingProfileFromOrderConfirmationPage = sfCheckoutPage.getShippingProfileFromConfirmationPage();
		shippingMethodFromOrderConfirmationPage = sfCheckoutPage.getShippingMethodAfterPlacedOrder().split("\\:")[1].trim();
		billingProfileNameFromOrderConfirmationPage = sfCheckoutPage.getBillingProfileAfterPlacedOrder();
		lastFourDigitOfCCFromOrderConfirmationPage = sfCheckoutPage.getLastFourNumbersOfBillingDetailsOnConFirmationPage();
		expDateOfCCFromOrderConfirmationPage = sfCheckoutPage.getExpiryDateOfCardNumberInBillingDetails();
		grandTotalAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_GrandTotal);
		shippingChargeAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_Shipping);
		subTotalAtOrderConfirmationPage = sfCheckoutPage.getChargesAccordingToLabelAtOrderConfirmationPage(text_Subtotal);
		s_assert.assertTrue(shippingProfile.contains(shippingProfileFromOrderConfirmationPage), "Shipping Profile is not matching on confirmation page. Expected is :"+shippingProfile+" But found is :"+shippingProfileFromOrderConfirmationPage);
		//s_assert.assertTrue(shippingMethodBeforeOrderPlaced.contains(shippingMethodFromOrderConfirmationPage), "Shipping Method is not matching on confirmation page. Expected is :"+shippingMethodBeforeOrderPlaced+" But found is :"+shippingMethodFromOrderConfirmationPage);
		s_assert.assertTrue(billingProfileNameFromOrderConfirmationPage.contains(billingProfileLastName),"Billing Profile is not matching on confirmation page. Expected is :"+billingProfileLastName+" But found is :"+billingProfileNameFromOrderConfirmationPage);
		s_assert.assertTrue(lastFourDigitOfCCFromOrderConfirmationPage.equals(ccfourDigits), "Credit Card Last 4 digits are not matching. Expected is :"+ccfourDigits+" But found is :"+lastFourDigitOfCCFromOrderConfirmationPage);
		s_assert.assertTrue(expDateOfCCFromOrderConfirmationPage.equals(ccExpiryDate), "Credit Card Expiry Date is not matching. Expected is :"+ccExpiryDate+" But found is :"+expDateOfCCFromOrderConfirmationPage);
		s_assert.assertTrue(grandTotalAtOrderConfirmationPage.equals(orderTotal),"Order total is not matching. Expected is:"+orderTotal+"But found is "+grandTotalAtOrderConfirmationPage);
		s_assert.assertTrue(shippingChargeAtOrderConfirmationPage.equals(shippingCharges),"Shipping charge is not matching. Expected is:"+shippingCharges+"But found is "+shippingChargeAtOrderConfirmationPage);
		s_assert.assertTrue(subTotalAtOrderConfirmationPage.equals(subTotal),"Subtotal is not matching. Expected is:"+subTotal+"But found is "+subTotalAtOrderConfirmationPage);
		s_assert.assertTrue(productQtyAtOrderDetailsPage.contains(productQuantity),"Product qty is not matching. Expected is:"+productQuantity+"But found is "+productQtyAtOrderDetailsPage);
		s_assert.assertTrue(sfOrdersPage.isProductNamePresentOnOrderDetailPage(productName),"Product name is not matching. Expected is:"+productName+"But not found");
		//s_assert.assertTrue(productSVValue.contains(productSVAtOrderDetailsPage),"Product SV value is not matching. Expected is:"+productSVValue+"But found is "+productSVAtOrderDetailsPage);
		//s_assert.assertTrue(productUnitPriceAtOrderDetailsPage.contains(yourPrice),"Product unit price is not matching. Expected is:"+yourPrice+"But found is "+productUnitPriceAtOrderDetailsPage);
		s_assert.assertAll();
	}
	// Placed an adhoc order from consultant
	public void testPlacedAnAdhocOrderFromConsultant(){
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertAll();
	}

}