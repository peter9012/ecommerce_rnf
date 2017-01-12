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
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
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
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
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
		sfHomePage.loginToStoreFront(TestConstants.RC_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		orderNumber = sfOrdersPage.getFirstOrderNumberFromOrderHistory();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(detailsLink);
		currentURL = sfOrdersPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(orderNumber) && sfOrdersPage.isTextPresent(orderDetailsText),"Current url should contain for RC "+orderNumber+"but actual on UI is "+currentURL+" and order details page is not present");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-227 Order History- Report a Problem- Field Validations
	 * Description : This test validates report problems page & field validations
	 *     
	 */
	@Test(enabled=false)//TODO incomplete
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
	@Test(enabled=true)//TODO incomplete
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
	@Test(enabled=true)
	public void testCheckoutPageEditsEditShippingInformation_298(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = (TestConstants.LAST_NAME+randomWord).toLowerCase();
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String shippngAddressName = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingAddress();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		shippngAddressName = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage().toLowerCase();
		s_assert.assertTrue(shippngAddressName.contains(lastName), "Expected shipping profile name should contain last name is "+lastName+" but actual on ui is "+shippngAddressName);
		s_assert.assertAll(); 
	}

	/***
	 * qTest : TC-316 Consultants Cannot Ship to Quebec
	 * Description : This test validates Quebec province can not be select as shipping address
	 * for consultant
	 *     
	 */
	@Test(enabled=false) //CA specific test
	public void testConsultantCanNotShipToQuebec_316(){
		if(country.equalsIgnoreCase("ca")){
			sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
			sfShopSkinCarePage = sfHomePage.clickAllProducts();
			sfShopSkinCarePage.selectFirstProduct();
			sfShopSkinCarePage.checkoutTheCartFromPopUp();
			sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
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
	@Test(enabled=true)
	public void testEditAShipAddressCheckoutInvalidDetails_322(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String invalidPostalCode = "123";
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
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
	@Test(enabled=false)//TODO
	public void testAddAShipAddressCheckoutFirstTime_323(){

	}

	/***
	 * qTest : TC-229 Checkout- Viewing Main Account Info
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testCheckoutViewingMainAccountInfo_323(){

	}

	/***
	 * qTest : TC-233 Shipping method cost should reduce for consultant adhoc order with an SV 100+
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testShippingMethodCostShouldReduceForConsultantAdhocOrderWithMoreThanHundredSVValue_233(){

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
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
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
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
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
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
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
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME.toLowerCase()+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String shippngAddressName = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingAddress();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		shippngAddressName = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage().toLowerCase();
		s_assert.assertTrue(shippngAddressName.contains(lastName), "Expected shipping profile name should contain last name is "+lastName+" but actual on ui is "+shippngAddressName);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		s_assert.assertAll();
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
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfBillingAddress();
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		s_assert.assertTrue(sfCheckoutPage.isBillingAddressDropdownEnabled(), "Billing profile is not present in editable mode");
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterBillingAddressDetailsAtCheckout(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfCheckoutPage.isNewBillingDetailsVisibleOnUI(lastName),"New Billing Details do not get updated as Default Billing details on Checkout Page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-304 Billing profile- Add a Profile with same Shipping address
	 * Description : This test case edit the billing profile and validate it
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testBillingProfileAddAProfileWithSameShippingAddress_304(){
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		String cardNumberFromUI = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfBillingAddress();
		cardNumberFromUI = sfCheckoutPage.getCreditCardDetailsFromBillingProfile();
		if(cardNumberFromUI.contains("4747")){
			cardNumber = TestConstants.CARD_NUMBER_2;
		}else{
			cardNumber = TestConstants.CARD_NUMBER;
		}
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		cardNumberFromUI = sfCheckoutPage.getCreditCardDetailsFromBillingProfile();
		cardNumber= cardNumber.substring(11,15);
		s_assert.assertTrue(cardNumberFromUI.contains(cardNumber), "Expected newly added billing profile should contains "+cardName+ "but actual on UI is "+cardNumberFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-305 Adding a payment method- Errors
	 * Description : This test cases verify the validation of Card details fields
	 * TODO :: Have to enter a expire card date 
	 *     
	 */
	@Test(enabled=true)
	public void testAddingAPaymentMethodErrors_305(){
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		String invalidExpMonth = TestConstants.INVALID_CARD_EXP_MONTH;
		String invalidExpYear = TestConstants.INVALID_CARD_EXP_YEAR;
		String cardNumberMoreThan16Digit = TestConstants.CARD_NUMBER_MORE_THAN_16_DIGIT;
		String cardNumberLessThan16Digit = TestConstants.CARD_NUMBER_LESS_THAN_16_DIGIT;
		String cardNumberWithChar = TestConstants.CARD_NUMBER_LESS_THAN_16_DIGIT+"q";
		String CVVWithTwoDigit = TestConstants.CVV_WITH_TWO_DIGIT;
		String errorMessage = null;
		String errorMessageFromUI = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfBillingAddress();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserCreditCardNumber(cardNumberLessThan16Digit);
		//sfCheckoutPage.clickBillingDetailsNextbutton();
		errorMessage = "Invalid Credit card number";
		errorMessageFromUI = sfCheckoutPage.getErrorMessageOfAlert();
		sfCheckoutPage.acceptAlertPopup();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for less than 16 digit card number is "+errorMessage+" but actual on UI is "+errorMessageFromUI);
		sfCheckoutPage.enterUserCreditCardNumber(cardNumberMoreThan16Digit);
		//sfCheckoutPage.clickBillingDetailsNextbutton();
		//errorMessage = "Maximum length is 16";
		errorMessageFromUI = sfCheckoutPage.getErrorMessageOfAlert();
		sfCheckoutPage.acceptAlertPopup();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for more than 16 digit card number is "+errorMessage+" but actual on UI is "+errorMessageFromUI);
		//card number with char
		sfCheckoutPage.enterUserCreditCardNumber(cardNumberWithChar);
		//sfCheckoutPage.clickBillingDetailsNextbutton();
		//errorMessage = "Please enter a valid card number";
		errorMessageFromUI = sfCheckoutPage.getErrorMessageOfAlert();
		sfCheckoutPage.acceptAlertPopup();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for card number with char is "+errorMessage+" but actual on UI is "+errorMessageFromUI);
		//wrong CVV
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVVWithTwoDigit);
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
	 * qTest : TC-306 Tax Rules- Consultant- USA- Ad-hoc order
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testTaxRulesConsultantUSAAdhocOrder_306(){

	}

	/***
	 * qTest : TC-307 Tax Rules- Consultant- USA- Autoship order
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testTaxRulesConsultantUSAAutoshipOrder_307(){

	}

	/***
	 * qTest : TC-308 Display Tax- Order history details
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testDisplayTaxOrderHistoryDetails_308(){

	}

	/***
	 * qTest : TC-309 Display Tax- Checkout
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testDisplayTaxCheckout_309(){

	}

	/***
	 * qTest : TC-310 Add a payment method-Checkout
	 * Description : This test case validates billing profile details are in editable mode
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testAddAPaymentMethodCheckout_310(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String emailID = TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_SUFFIX;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC,firstName, lastName, emailID, password);
		sfCheckoutPage.clickCreateAccountButton();
		sfCheckoutPage.checkoutTheCart();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfCheckoutPage.isCardDetailsFieldsEnabled(), "Credit card details fields are not in editable mode");
		s_assert.assertAll();
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
		String lastName = TestConstants.LAST_NAME;
		String emailID = TestConstants.FIRST_NAME+timeStamp+TestConstants.EMAIL_SUFFIX;
		String shippingMethodWithCost = null;
		String shippingMethodName = TestConstants.SHIPPING_METHOD_UPS_GROUND;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC,firstName, lastName, emailID, password);
		sfCheckoutPage.clickCreateAccountButton();
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		shippingMethodWithCost = sfCheckoutPage.getSelectedShippingMethodName();
		s_assert.assertTrue(shippingMethodWithCost.contains("$0") && shippingMethodWithCost.contains(shippingMethodName), "Expected shipping method name is "+shippingMethodName+" and shipping cost should contain $0 but actual on UI is "+shippingMethodWithCost);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-312 Ship Method-PC - select PC checkbox on checkout
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testShippingMethodPCSelectPCCheckboxOnCheckout_312(){

	}

	/***
	 * qTest : TC-313 Ship Method-PC - Existing PC changes Shipping Method
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testShipMethodPCExistingPCChangesShippingMethod_313(){

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
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickSaveButton();
		shippingMethodWithCost = sfCheckoutPage.getSelectedShippingMethodName();
		s_assert.assertTrue(shippingMethodWithCost.contains("$0") && shippingMethodWithCost.contains(shippingMethodName), "Expected shipping method name is "+shippingMethodName+" and shipping cost should contain $0 but actual on UI is "+shippingMethodWithCost);
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-315 Ship method- Ad Hoc carts
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testShipMethodAdhocCarts(){

	}

	/***
	 * qTest : TC-316 Consultants Cannot Ship to Quebec
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testConsultantsCannotShipToQuebec_316(){

	}

	/***
	 * qTest : TC-318 Address Verification - Billing Profile
	 * Description : This test case edit and add billing profile with address
	 * and verify it 
	 *     
	 */
	@Test(enabled=false)
	public void testAddressVerificationBillingProfile_318(){
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfBillingAddress();
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.checkUseMyDeliveryAddressChkBoxForExistingBillingProfile();
		sfCheckoutPage.enterBillingAddressDetailsAtCheckout(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSavePaymentButton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfCheckoutPage.isNewBillingDetailsVisibleOnUI(lastName),"Edit billing Details do not get updated as Default billing details on checkout page");
		randomWord = CommonUtils.getRandomWord(5);
		lastName = TestConstants.LAST_NAME+randomWord;
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.enterUserBillingDetailsAfterEditTheProfile(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.enterBillingAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfCheckoutPage.isNewBillingDetailsVisibleOnUI(lastName),"New Billing Details do not get updated as Default Billing details on Checkout Page");
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
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
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
	 * qTest : TC-321 Edit a Ship address- Checkout - Valid details
	 * Description : This test edit the ship address and validates it at checkout page 
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testEditAShipAddressCheckoutValidDetails_321(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String shippingProfileNameFromUI = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingProfile();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		s_assert.assertTrue(sfCheckoutPage.isUseAsEnteredPopupDisplayed(), "Use As Entered Confirmation Popup is Not Displayed after added new shipping profile");
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		shippingProfileNameFromUI = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		s_assert.assertTrue(shippingProfileNameFromUI.contains(lastName), "Shipping profile name should contain "+lastName+ "Actual on UI is "+shippingProfileNameFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-332 Cart Page- Product Messaging for Non-replenishable products
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testCartPageProductMessagingForNonReplenishableProducts_332(){

	}

	/***
	 * qTest : TC-335 Cart Page- PC Perks Terms and Conditions
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testCartPagePCPerksTermsAndConditions_335(){

	}

	/***
	 * qTest : TC-354 PC user is in the process of placing an adhoc order less than 90$
	 * Description : This test case validates threshold message for less than 90$
	 *  at cart page
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testPCUserIsInTheProcessOfPlacingAnAdhocOrderLessThan90_354(){
		String errorMessage = "more to your cart to qualify for FREE shipping";
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.productPriceFilterLowToHigh();
		int totalNoOfProduct = sfShopSkinCarePage.getTotalNoOfProduct();
		int productNumber = sfShopSkinCarePage.getProductNumberAccordingToPriceRange(totalNoOfProduct, 0.00);
		System.out.println("product"+productNumber);
		sfShopSkinCarePage.addProductToBag(productNumber);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickMiniCartBagLink();
		s_assert.assertTrue(sfCartPage.isErrorMessagePresentForThreshold(errorMessage), "Threshold message is present for a product more than 90$");
		//sfCheckoutPage=sfCartPage.checkoutTheCart();
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-355 PC user is in the process of placing an adhoc order more than 90$
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testPCUserIsInTheProcessOfPlacingAnAdhocOrderMoreThan90$_355(){

	}

	/***
	 * qTest : TC-366 Billing profile- Add an Address with invalid details to existing Profile
	 * Description : This test case validates error message of address fields of billing profile
	 * while editing
	 *     
	 */
	@Test(enabled=true)
	public void testBillingProfileAddAnAddressWithInvalidDetailsToExistingProfile_366(){
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 ="address line 1";
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = "12345";
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String errorMessageFromUI = null;
		String errorMessage= null;
		String billingProfileDetails = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfBillingAddress();
		sfCheckoutPage.clickEditLinkOfBillingProfile();
		sfCheckoutPage.clickSavePaymentButton();
		errorMessage = "Name must contain first name and last name with no special characters";
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForBillingAddressFields(errorMessage), "Error message is not displayed for first and last name");
		errorMessage = "Please enter valid address";
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForBillingAddressFields(errorMessage), "Error message is not displayed for address line1");
		errorMessage = "Please enter valid city";
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForBillingAddressFields(errorMessage), "Error message is not displayed for City");
		errorMessage = "Please select a Province";
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForBillingAddressFields(errorMessage), "Error message is not displayed for province");
		errorMessage = "Please enter valid postal code";
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForBillingAddressFields(errorMessage), "Error message is not displayed for postal code");
		errorMessage = "Please specify a valid phone number";
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForBillingAddressFields(errorMessage), "Error message is not displayed for phone number");
		// enter invalid address
		sfCheckoutPage.enterEditBillingAddressDetailsAtCheckout(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSavePaymentButton();
		errorMessage = "Unknown street";
		errorMessageFromUI = sfCheckoutPage.getErrorMessageForBillingAddressDetails();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for invalid address details is "+errorMessage+" But actual on UI is "+errorMessage);
		postalCode = TestConstants.POSTAL_CODE_US;
		addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		sfCheckoutPage.enterEditBillingAddressDetailsAtCheckout(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSavePaymentButton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		billingProfileDetails = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		s_assert.assertTrue(billingProfileDetails.contains(lastName), "Billing profile name should contain "+lastName+" but actual On UI is"+billingProfileDetails);
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
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 ="address line 1";
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = "12345";
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String errorMessageFromUI = null;
		String errorMessage= null;
		String billingProfileDetails = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickEditLinkOfBillingAddress();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForFirstAndLastName(), "Error message is not present for first and last name field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForAddressLine1(), "Error message is not present for address line 1 field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForCity(), "Error message is not present for city field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForPostalCode(), "Error message is not present for postal code field");
		s_assert.assertTrue(sfCheckoutPage.isErrorMessagePresentForPhoneNumber(), "Error message is not present for phone number field");
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		errorMessage = "Unknown street";
		errorMessageFromUI = sfCheckoutPage.getErrorMessageForBillingAddressDetailsWhileAddANewAddress();
		s_assert.assertTrue(errorMessageFromUI.contains(errorMessage), "Expected error message for invalid address details is "+errorMessage+" But actual on UI is "+errorMessage);
		postalCode = TestConstants.POSTAL_CODE_US;
		addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		billingProfileDetails = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		s_assert.assertTrue(billingProfileDetails.contains(lastName), "Billing profile name should contain "+lastName+" but actual On UI is"+billingProfileDetails);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-368 Shipping to PO/RR with PO number as address detail
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testShippingToPORRWithPONumberAsAddressDetail_368(){

	}

	/***
	 * qTest : TC-409 Order Confirmation for AdHoc Orders
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testOrderConfirmationForAdhocOrders_409(){
		String productName=null;
		String productQuantity=null;
		String productPrice=null;
		String subTotal=null;
		String orderTotal=null;
		String shippingProfile=null;
		String shippingCharges=null;
		String shippingMethodAfterOrderPlaced=null;
		String ccfourDigits  = null;
		String ccExpiryDate = null;
		String billingProfileName  = null;

		String shippingProfileFromOrderConfirmationPage = null;
		String shippingMethodFromOrderConfirmationPage = null;
		String billingProfileNameFromOrderConfirmationPage = null;
		String lastFourDigitOfCCFromOrderConfirmationPage = null;
		String expDateOfCCFromOrderConfirmationPage = null;
		String totalChhargesFromOrderConfirmationPage = null;

		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage=sfShopSkinCarePage.checkoutTheCartFromPopUp();

		productName = sfCartPage.getProductName("1");
		productQuantity = sfCartPage.getQuantityOfProductFromCart("1");
		productPrice = sfCartPage.getProductPrice("1");
		subTotal = sfCartPage.getSubtotalofItems();
		orderTotal = sfCartPage.getOrderTotal();

		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();

		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(TestConstants.CARD_TYPE, TestConstants.CARD_NUMBER, TestConstants.CARD_NAME, TestConstants.CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		shippingProfile = sfCheckoutPage.getDefaultShippingAddressNameAtCheckoutPage();
		shippingCharges = sfCheckoutPage.getDeliveryChargesAtOrderReviewPage();
		shippingMethodAfterOrderPlaced =  sfCheckoutPage.getSelectedShippingMethodName();
		ccfourDigits = sfCheckoutPage.getLastFourDigitsOfCardNumberInBillingDetails();
		ccExpiryDate = sfCheckoutPage.getExpiryDateOfCardNumberInBillingDetails();
		billingProfileName = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		shippingProfileFromOrderConfirmationPage = sfCheckoutPage.getShippingProfileFromConfirmationPage();
		shippingMethodFromOrderConfirmationPage = sfCheckoutPage.getShippingMethodAfterPlacedOrder();
		billingProfileNameFromOrderConfirmationPage = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		lastFourDigitOfCCFromOrderConfirmationPage = sfCheckoutPage.getLastFourNumbersOfBillingDetailsOnConFirmationPage();
		expDateOfCCFromOrderConfirmationPage = sfCheckoutPage.getExpiryDateOfCardNumberInBillingDetails();
		totalChhargesFromOrderConfirmationPage = sfCheckoutPage.getTotalChargeOnConFirmationPage();

		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertTrue(sfCheckoutPage.isTextPresent("Order #"),"Order Number is not present on the confirmation page");
		s_assert.assertTrue(shippingProfileFromOrderConfirmationPage.equals(shippingProfile), "Shipping Profile is not matching on confirmation page. Expected is :"+shippingProfile+" But found is :"+shippingProfileFromOrderConfirmationPage);
		s_assert.assertTrue(shippingMethodFromOrderConfirmationPage.equals(shippingMethodAfterOrderPlaced), "Shipping Method is not matching on confirmation page. Expected is :"+shippingMethodAfterOrderPlaced+" But found is :"+shippingMethodFromOrderConfirmationPage);
		s_assert.assertTrue(billingProfileNameFromOrderConfirmationPage.equals(billingProfileName),"Billing Profile is not matching on confirmation page. Expected is :"+billingProfileName+" But found is :"+billingProfileNameFromOrderConfirmationPage);
		s_assert.assertTrue(lastFourDigitOfCCFromOrderConfirmationPage.equals(ccfourDigits), "Credit Card Last 4 digits are not matching. Expected is :"+ccfourDigits+" But found is :"+lastFourDigitOfCCFromOrderConfirmationPage);
		s_assert.assertTrue(expDateOfCCFromOrderConfirmationPage.equals(ccExpiryDate), "Credit Card Expiry Date is not matching. Expected is :"+ccExpiryDate+" But found is :"+expDateOfCCFromOrderConfirmationPage);
		s_assert.assertTrue(totalChhargesFromOrderConfirmationPage.equals(orderTotal),"Order total is not matching. Expected is:"+orderTotal+"But found is "+totalChhargesFromOrderConfirmationPage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-414 User shouldn't able to change the sponsor after consultant account created
	 * Description : This test case validates consultant can not change the sponsor after creation of account
	 * through continue without a consultant and not your sponsor link
	 *     
	 */
	@Test(enabled=false)
	public void testUserShouldntAbleToChangeTheSponsorAfterConsultantAccountCreated_414(){
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
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
	@Test(enabled=false)
	public void testUserShouldntAbleToChangeTheSponsorAfterPCAccountCreated_415(){
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		s_assert.assertFalse(sfCheckoutPage.isContinueWithoutConsultantLinkPresent(), "Continue without consultant link is present for consultant's sponsor");
		s_assert.assertFalse(sfCheckoutPage.isNotYourSponsorLinkIsPresent(), "Not your sponsor link is present for consultant's sponsor");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-365 Order Details and Return Order Details- Link to Pulse
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)
	public void testOrderDetailsAndReturnOrderDetailsLinkToPulse_365(){

	}

	/***
	 * qTest : TC-457 Choose a consultant- R+F Corporate sponsor - PC First checkout from Corp
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)
	public void testChooseAConsultantRFCorporateSponsorPCFirstCheckoutFromCorp_457(){

	}

	/***
	 * qTest : TC-458 Choose a consultant- R+F Corporate sponsor - PC First checkout from PWS
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)
	public void testChooseAConsultantRFCorporateSponsorPCFirstCheckoutFromPWS_458(){

	}

	/***
	 * qTest : TC-459 Choose a consultant- R+F Corporate sponsor - RC checkout from Corporate
	 * Description : //TODO
	 * 
	 *     
	 */
	@Test(enabled=false)
	public void testChooseAConsultantRFCorporateSponsorRCCheckoutFromCorporate_459(){

	}

	/***
	 * qTest : TC-470 Review and Place Order
	 * Description : This test case vaidates the order details at review and confirm page
	 * 
	 *     
	 */
	@Test(enabled=false)//TODO
	public void testReviewAndPlaceOrder_470(){
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		String itemCodeFromCart = null;
		String quantityOfFirstProductFromCart = null;
		String productNameFromCart = null;
		String subTotalFromCart = null;
		String deliveryChargesFromCart = null;
		int totalNoOfItemsFromCart = 0;
		String itemCodeFromCheckout = null;
		String quantityOfFirstProductFromCheckout = null;
		String productNameFromCheckout = null;
		String subTotalFromCheckout = null;
		String deliveryChargesFromCheckout = null;
		int totalNoOfItemsFromCheckout = 0;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		totalNoOfItemsFromCart = sfCartPage.getTotalNoOfItemsInCart();
		itemCodeFromCart = sfCartPage.getItemCode("1");
		quantityOfFirstProductFromCart = sfCartPage.getQuantityOfProductFromCart("1");
		productNameFromCart = sfCartPage.getProductName("1");
		subTotalFromCart = sfCartPage.getSubtotalofItems();
		deliveryChargesFromCart = sfCartPage.getDeliveryCharges();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		totalNoOfItemsFromCheckout = sfCheckoutPage.getTotalNoOfItemsAtOrderReviewPage();
		productNameFromCheckout = sfCheckoutPage.getProductNameAtOrderReviewPage("1");
		subTotalFromCheckout = sfCheckoutPage.getSubtotalofItemsAtOrderReviewPage();
		deliveryChargesFromCheckout = sfCheckoutPage.getDeliveryChargesAtOrderReviewPage();
		s_assert.assertTrue(totalNoOfItemsFromCheckout==totalNoOfItemsFromCart, "Expected total no of item at order review page is "+totalNoOfItemsFromCart+"Actual on UI "+totalNoOfItemsFromCheckout);
		s_assert.assertTrue(productNameFromCheckout.contains(productNameFromCart), "Expected product name of item at order review page is "+productNameFromCart+"Actual on UI "+productNameFromCheckout);
		s_assert.assertTrue(subTotalFromCheckout.contains(subTotalFromCart), "Expected subtotal at order review page is "+subTotalFromCart+"Actual on UI "+subTotalFromCheckout);
		s_assert.assertTrue(deliveryChargesFromCheckout.contains(deliveryChargesFromCart), "Expected delivery charges at order review page is "+deliveryChargesFromCart+"Actual on UI "+deliveryChargesFromCheckout);
		sfHomePage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(), "Adhoc order is not placed successfully by PC");
		s_assert.assertAll();
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
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
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
	 * qTest : TC-324 Add a ship address- Checkout - existing user
	 * Description : This test add a new shipping address for existing user
	 *     
	 */
	@Test(enabled=true)
	public void testAddAShipAddressCheckoutExistingUser_324(){
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
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickAddNewShippingAddressButton();
		sfCheckoutPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		s_assert.assertTrue(sfCheckoutPage.isUseAsEnteredPopupDisplayed(), "Use As Entered Confirmation Popup is Not Displayed after added new shipping profile");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-317 Address Verification - Shipping Profile
	 * Description : This test add a new shipping address for existing user and validates it
	 *     
	 */
	@Test(enabled=true)
	public void testAddressVerificationShippingProfile_317(){
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
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingAddress();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		s_assert.assertTrue(sfCheckoutPage.isUseAsEnteredPopupDisplayed(), "Use As Entered Confirmation Popup is Not Displayed after Editing shipping profile");
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		sfCheckoutPage.clickAddNewShippingAddressButton();
		sfCheckoutPage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2,city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickNextbuttonOfShippingDetails();
		s_assert.assertTrue(sfCheckoutPage.isUseAsEnteredPopupDisplayed(), "Use As Entered Confirmation Popup is Not Displayed after added new shipping profile");
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertAll();
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
		String emailID = "E-mail Address";
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

		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
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
		sfOrdersPage.enterTheDetailsForReportProblem(message);
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
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
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
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		String orderNum = null;
		String orderStatus= null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(), "Adhoc order is not placed successfully by PC");
		orderNum = sfCheckoutPage.getOrderNumberAfterCheckout();
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		orderStatus = sfOrdersPage.getStatusOfOrderFromOrderHistory(orderNum).trim();
		s_assert.assertTrue(sfOrdersPage.isOrderPresentInOrderHistory(orderNum),"Order number : " + orderNum + " is not present in Order history");
		s_assert.assertEquals(orderStatus,"Submitted","Status of Order is not fund as expected. Expected : Submitted. Actual : "+orderStatus);
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-483 Order History- Report a Problem-Product Level-With Empty fields
	 * Description : 
	 *     
	 */
	@Test(enabled=true)
	public void testOrderHistoryReportAProblemProductLevelWithEmptyFields_483(){
		String currentUrlOfReportProblemPagePage = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
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
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
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
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.clickAddToCartOfFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfCheckoutPage.selectFirstSponsorFromList();
		sfCheckoutPage.clickSaveButton();
		if(sfCheckoutPage.isFirstLastNameFieldVisible()){
			sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		}
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(), "Adhoc order is not placed successfully by RC");
	}

	/***
	 * qTest : TC-478 Retail User Checkout- Choose a Consultant - Change selected sponsor
	 * Description : This test case  placed adhoc order through RC change the selected sponsor
	 * 
	 */
	@Test(enabled=true)
	public void testRetailUserCheckoutChooseAConsultantChangeSelectedSponsor_478(){
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
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
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.clickAddToCartOfFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfCheckoutPage.selectFirstSponsorFromList();
		sfCheckoutPage.clickRemoveLink();
		sfCheckoutPage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfCheckoutPage.selectFirstSponsorFromList();
		sfCheckoutPage.clickSaveButton();
		if(sfCheckoutPage.isFirstLastNameFieldVisible()){
			sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		}
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(), "Adhoc order is not placed successfully by RC");
	}

	/***
	 * qTest : TC-479 Retail User Checkout- Choose a Consultant - Checkout with Corporate
	 * Description : This test case  placed adhoc order through RC continue without a consultant
	 * 
	 */
	@Test(enabled=true)
	public void testRetailUserCheckoutChooseAConsultantCheckoutWithCorporate_479(){
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
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
		String sponsorName = "RF Corporate";
		String sponsorNameFromUI = null;
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.clickAddToCartOfFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sponsorNameFromUI = sfCheckoutPage.getSponsorInfo();
		s_assert.assertTrue(sponsorNameFromUI.contains(sponsorName), "Expected sponsor name is "+sponsorName+" but actual on UI is "+sponsorNameFromUI);
		sfCheckoutPage.clickSaveButton();
		if(sfCheckoutPage.isFirstLastNameFieldVisible()){
			sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		}
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(), "Adhoc order is not placed successfully by RC");
	}

}