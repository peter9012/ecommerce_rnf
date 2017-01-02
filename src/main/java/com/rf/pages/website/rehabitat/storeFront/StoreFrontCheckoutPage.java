package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontCheckoutPage extends StoreFrontWebsiteBasePage{
	public StoreFrontCheckoutPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontCheckoutPage.class.getName());

	private final By FIRST_NAME_LOC = By.id("first-name");
	private final By LAST_NAME_LOC = By.id("last-name");
	private final By EMAIL_LOC = By.id("email-account");
	private final By PASSWORD_LOC = By.xpath("//form[@id='registerForm']//input[@id='password']");
	private final By CONFIRM_PASSWORD_LOC = By.xpath("//form[@id='registerForm']//input[@id='the-password-again']");
	private final By PC_PERKS_CHECKBOX_LOC = By.xpath("//input[@id='c2']/following::label[1]");
	private final By CREATE_ACCOUNT_BUTTON_LOC = By.id("next-button");
	private final By ADD_NEW_SHIPPING_ADDRESS_BUTTON_LOC = By.xpath("//button[contains(text(),'Add New')]");
	private final By SHIPPING_ADDRESS_NAME_LOC = By.xpath("//span[@id='defaultShippingAddress']");
	private final By EDIT_LINK_OF_SHIPPING_ADDRESS_LOC=By.xpath("//div[@class='checkout-shipping']//a[1]");
	private final By FIRST_LAST_NAME_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//input[@id='address.firstName']");
	private final By ADDRESS_LINE1_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//input[@id='address.line1']");
	private final By ADDRESS_LINE_2_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//input[@id='address.line2']");
	private final By CITY_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//input[@id='address.townCity']");
	private final By STATE_DD_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//select[@id='address.region']");
	private final By POSTAL_CODE_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//input[@id='address.postcode']");
	private final By PHONE_NUMBER_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//input[@id='address.phone']");

	private final By SHIPPING_NAME_AT_CHECKOUT_PAGE_LOC=By.id("address.firstName");
	private final By SHIPPING_ADDRESS_LINE_1_AT_CHECKOUT_PAGE_LOC=By.id("address.line1");
	private final By SHIPPING_ADDRESS_LINE_2_AT_CHECKOUT_PAGE_LOC=By.id("address.line2");
	private final By SHIPPING_CITY_AT_CHECKOUT_PAGE_LOC=By.id("address.townCity");
	private final By SHIPPING_STATE_AT_CHECKOUT_PAGE_LOC=By.id("address.region");
	private final By SHIPPING_POSTAL_CODE_AT_CHECKOUT_PAGE_LOC=By.id("address.postcode");
	private final By SHIPPING_PHONE_NUMBER_AT_CHECKOUT_PAGE_LOC=By.id("address.phone");

	private final By QUEBEC_PROVINCE_FOR_SHIPPING_LOC=By.xpath("//option[@disabled='disabled' and text()='Quebec']");
	private final By SELECTED_SHIPPING_METHOD_LOC = By.xpath("//li[@class='checked']/label");
	private final By EDIT_LINK_OF_ORDERS_SUMMARY_LOC=By.xpath("//div[@class='price']/a[contains(text(),'Edit')]");
	private final By CANCEL_BUTTON_LOC=By.xpath("//button[contains(text(),'Cancel')]");
	private final By EDIT_LINK_OF_ACCOUNT_INFO_LOC=By.xpath("//a[@class='editIcon']");
	private final By CONTINUE_WITHOUT_SPONSOR_LOC = By.xpath("//a[contains(text(),'Continue Without a Consultant')]");
	private final By USE_MY_DELIVERY_ADDRESS_CHECKBOX_LOC = By.xpath("//input[@id='useDeliveryAddress']/..");
	private final By NEXT_BTN_AFTER_BILLING_LOC = By.id("reviewOrder");
	private final By POPUP_FOR_TERMS_AND_CONDITIONS_LOC = By.id("city_popup");
	private final By EDIT_LINK_OF_SHIPPING_PROFILE_LOC=By.xpath("//span[@id='defaultShippingAddress']/a");
	private final By ADD_NEW_BILLING_PROFILE_BUTTON_LOC = By.xpath("//button[text()='Add New Billing Profile']");
	private final By CONSULTANT_NAME_ID_FIELD_LOC = By.id("sponserparam");
	private final By CONSULTANT_SEARCH_BTN_LOC = By.id("search-sponsor-button");
	private final By REQUEST_A_CONSULTANT_LOC = By.xpath("//a[contains(text(),'Request a Consultant')]");
	private final By NO_RESULT_FOUND_MSG_FOR_SEARCHED_CONSULTANT_LOC = By.xpath("//p[@class='noResult' and contains(text(),'No results found for')]");
	private final By VALIDATION_MSG_FOR_SEARCHED_CONSULTANT_LOC = By.id("sponserparam-error");
	private final By TOTAL_NO_OF_ITEMS_LOC = By.xpath("//div[contains(text(),'Order Summary')]/following::li[contains(@class,'list-items')]//div[@class='thumb']");
	private final By SUBTOTAL_AT_ORDER_REVIEW_PAGE_LOC = By.xpath("//div[contains(text(),'Order Summary')]/following::p[text()='Subtotal:']/following::span[1]");
	private final By DELIVERY_AT_ORDER_REVIEW_PAGE_LOC = By.xpath("//div[contains(text(),'Order Summary')]/following::p[text()='Subtotal:']/following::span[1]");
	private final By FIRST_ITEM_PRODUCT_NAME_REVIEW_PAGE_LOC = By.xpath("//div[contains(text(),'Order Summary')]/following::li[contains(@class,'list-items')][1]//div[@class='name']/a");
	private final By CONFIRMATION_MSG_OF_PLACED_ORDER_LOC = By.xpath("//div[@class='orderHeading']");
	private final By LOGIN_REGISTER_TEXT_LOC=By.xpath("//div[@class='logpage']/h1");
	private final By USER_NAME_LOC=By.xpath("//form[@id='LoginForm']//input[@id='username']");
	private final By PASSWORD_FOR_LOGIN_LOC=By.xpath("//form[@id='LoginForm']//input[@id='password']");
	private final By LOGIN_AND_CHECKOUT_BUTTON_LOC=By.xpath("//form[@id='LoginForm']//input[@value='Login and Checkout']");
	private final By EDIT_LINK_OF_BILLING_PROFILE_LOC=By.xpath("//div[@id='default-payment-method']/a");
	private final By FIRST_LAST_NAME_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.firstName']");
	private final By ADDRESS_LINE1_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.line1']");
	private final By ADDRESS_LINE_2_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.line2']");
	private final By CITY_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.townCity']");
	private final By STATE_DD_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//select[@id='address.region']");
	private final By POSTAL_CODE_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.postcode']");
	private final By PHONE_NUMBER_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.phone']");
	private final By EDIT_LINK_OF_BILLING_ADDRESS_LOC=By.xpath("//div[text()='Billing Info']/following::a[1]");
	private final By CREDIT_CARD_DETAILS_LOC = By.xpath("//div[@id='default-payment-method']//span[@class='cardInfo']");
	private final By INVALID_EXP_DATE_ERROR_MSG_LOC = By.id("valmsg-c-exyr");
	private final By CVV_ERROR_MSG_LOC = By.id("valmsg-c-cvv");
	private final By CREDIT_CARD_ERROR_MSG_LOC = By.id("valmsg-c-cn");
	private final By SAVE_PAYMENT_BUTTON_LOC = By.id("updateBillingAddress");
	private final By FIRST_LAST_NAME_FOR_EDIT_BILLING_ADDRESS_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.firstName']");
	private final By ADDRESS_LINE1_FOR_EDIT_BILLING_ADDRESS_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.line1']");
	private final By ADDRESS_LINE_2_FOR_EDIT_BILLING_ADDRESS_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.line2']");
	private final By CITY_FOR_EDIT_BILLING_ADDRESS_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.townCity']");
	private final By STATE_DD_FOR_EDIT_BILLING_ADDRESS_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//select[@id='address.region']");
	private final By POSTAL_CODE_FOR_EDIT_BILLING_ADDRESS_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.postcode']");
	private final By PHONE_NUMBER_FOR_EDIT_BILLING_ADDRESS_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.phone']");
	private final By BILLING_PROFILE_LOC = By.id("default-payment-method");
	private final By ERROR_MSG_FOR_BILLING_ADDRESS_DETAILS_LOC = By.xpath("//div[@id='checkoutEditBillingAddressForm']//*[@id='errorMessage']");
	private final By ERROR_MSG_FOR_INVALID_BILLING_ADDRESS_LOC = By.xpath("//div[@id='account-billing-container']//p[@id='errorMessage']");

	private String stateForBillingAddressDetails = "//div[@id='checkoutEditBillingAddressForm']//option[text()='%s']";
	private String stateForShippingDetails = "//div[@id='checkoutEditBillingAddressForm']//option[text()='%s']";
	private String billingInfoCardDetailsLoc = "//div[@id='default-payment-method']/ul/strong[contains(text(),'%s')]/following-sibling::span[@class='cardInfo']";
	private String billingInfoAddressNameLoc = "//div[@id='default-payment-method']/ul/strong[contains(text(),'%s')]";
	private String billingAddressLoc = "//div[@id='default-payment-method']/ul/strong[contains(text(),'%s')]/ancestor::ul";
	private String stateForShippingDetailsAtCheckoutPageLoc = "//form[@id='shippingAddressForm']//select[@id='address.region']//option[text()='%s']";
	private String shippingMethodLoc = "//label[contains(text(),'%s')]";
	private String billingAddressFieldsErrorMessageLoc = "//span[contains(text(),'%s')]";

	private String[] cardDetails;

	public StoreFrontCheckoutPage fillNewUserDetails(String userType,String firstName,String lastName,String email,String password){
		driver.type(FIRST_NAME_LOC, firstName);
		logger.info("first name entered as "+firstName);
		driver.type(LAST_NAME_LOC, lastName);
		logger.info("last name entered as "+lastName);
		driver.type(EMAIL_LOC, email+"\t");
		logger.info("email entered as "+email);
		driver.type(PASSWORD_LOC,password);
		logger.info("password entered as "+password);
		driver.type(CONFIRM_PASSWORD_LOC,password);
		logger.info("confirm entered as "+password);
		if(userType.equals(TestConstants.USER_TYPE_PC)){
			driver.click(PC_PERKS_CHECKBOX_LOC);
			logger.info("PC perks checkbox is checked");
		}
		return this;
	}

	public void clickCreateAccountButton(){
		driver.click(CREATE_ACCOUNT_BUTTON_LOC);
		logger.info("clicked on 'Create Account' button");
		driver.waitForLoadingImageToDisappear();
	}

	/***
	 * This method click the save button
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickAddNewShippingAddressButton(){
		driver.click(ADD_NEW_SHIPPING_ADDRESS_BUTTON_LOC);
		logger.info("Add new shipping address button clicked");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method get the default shipping profile name
	 * 
	 * @param
	 * @return profile name
	 * 
	 */
	public String getDefaultShippingAddressNameAtCheckoutPage(){
		driver.pauseExecutionFor(2000);
		String profileName = driver.findElement(SHIPPING_ADDRESS_NAME_LOC).getText();
		logger.info("default profile name is "+profileName);
		return profileName;
	}

	/***
	 * This method click on Edit Shipping Address at checkout page
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickEditLinkOfShippingAddress(){
		driver.click(EDIT_LINK_OF_SHIPPING_ADDRESS_LOC);
		logger.info("Clicked on Edit link of Shipping Address");
		return this;
	}

	/**
	 * This method update the shipping address details at checkout page
	 * 
	 * @param firstName, lastName, addressLine1, city, state, postal code, phone number
	 * @return
	 */
	public StoreFrontCheckoutPage updateShippingAddressDetailsAtCheckoutPage(String firstName, String lastName, String addressLine1, String city, String state, String postal, String phoneNumber) {
		String completeName = firstName+" "+lastName;
		driver.pauseExecutionFor(5000);
		driver.type(FIRST_LAST_NAME_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE1_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.clear(ADDRESS_LINE_2_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("Address line 2 cleared");
		driver.type(CITY_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetailsAtCheckoutPageLoc, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(PHONE_NUMBER_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);
		return this;
	}

	/***
	 * This method validates the error message for postal code field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isQuebecAddressDisabledForConsultant(){
		driver.click(STATE_DD_FOR_REGISTRATION_LOC);
		logger.info("State dropdown clicked");
		return driver.isElementPresent(QUEBEC_PROVINCE_FOR_SHIPPING_LOC);
	}

	/***
	 * This method clear the all shipping address field at checkout page
	 * 
	 * @param
	 * @return
	 * 
	 */
	public void clearAllFieldsForShippingAddressAtCheckoutPage(){
		driver.clear(FIRST_LAST_NAME_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("First & last Name field is cleared");
		driver.clear(ADDRESS_LINE1_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("address line 1 field is cleared");
		driver.clear(CITY_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("City field is cleared");
		driver.clear(POSTAL_CODE_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("Postal code field is cleared");
		driver.clear(PHONE_NUMBER_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("phone number field is cleared");
	}

	/**
	 * This method enters the shipping details at checkout page
	 * 
	 * @param name
	 * @param addressLine1
	 * @param addressLine2
	 * @param city
	 * @param state
	 * @param postalCode
	 * @param phoneNumber
	 * @return
	 */
	public StoreFrontCheckoutPage enterShippingDetails(String name, String addressLine1, String addressLine2, String city, String state, String postalCode, String phoneNumber){
		driver.type(SHIPPING_NAME_AT_CHECKOUT_PAGE_LOC, name);
		logger.info("Entered shipping name as "+name);
		driver.type(SHIPPING_ADDRESS_LINE_1_AT_CHECKOUT_PAGE_LOC, addressLine1);
		logger.info("Entered shipping address Line 1 as "+addressLine1);
		driver.type(SHIPPING_ADDRESS_LINE_2_AT_CHECKOUT_PAGE_LOC, addressLine2);
		logger.info("Entered shipping address Line 2 as "+addressLine2);
		driver.type(SHIPPING_CITY_AT_CHECKOUT_PAGE_LOC, city);
		logger.info("Entered shipping city as "+city);
		driver.findElement(SHIPPING_STATE_AT_CHECKOUT_PAGE_LOC).sendKeys(state);
		logger.info("Entered shipping state as "+state);
		driver.type(SHIPPING_POSTAL_CODE_AT_CHECKOUT_PAGE_LOC, postalCode);
		logger.info("Entered shipping postal code as "+postalCode);
		driver.type(SHIPPING_PHONE_NUMBER_AT_CHECKOUT_PAGE_LOC, phoneNumber);
		logger.info("Entered shipping phone number as "+phoneNumber);
		return this;
	}

	/***
	 * This method click on Edit of main account info at checkout page
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickEditLinkOfAccountInfo(){
		driver.click(EDIT_LINK_OF_ACCOUNT_INFO_LOC);
		logger.info("Clicked on Edit link of Account info");
		return this;
	}

	/***
	 * This method validates save account button is displayed
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isSaveButtonDisplayedForAccountInfo(){
		return driver.findElement(SAVE_BUTTON_LOC).isDisplayed();
	}

	/***
	 * This method validates shipping address fields are displayed at checkout page
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isshippindAddressFieldsAreDisplayedAtCheckoutPage(){
		return driver.findElement(FIRST_LAST_NAME_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC).isDisplayed();
	}

	/***
	 * This method click on cancel button of shipping address at checkout page
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickCancelButton(){
		driver.click(CANCEL_BUTTON_LOC);
		logger.info("Clicked on cancel button");
		return this;
	}

	/***
	 * This method click on Edit of order summary section at checkout page
	 * 
	 * @param
	 * @return
	 * 
	 */
	public void clickEditLinkOfOrderSummarySection(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(EDIT_LINK_OF_ORDERS_SUMMARY_LOC));
		logger.info("Clicked on Edit link of order summary section");
	}

	/***
	 * This method validates cart page is present or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isCartPagePresent(){
		return driver.isElementPresent(CHECKOUT_BUTTON_LOC);
	}

	/***
	 * This method select the shipping method
	 * 
	 * @param shipping method name
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage selectShippingMethod(String methodName){
		driver.click(By.xpath(String.format(shippingMethodLoc, methodName)));
		logger.info("Shipping method select as "+methodName);
		return this;
	}

	/***
	 * This method get the selected shipping method name
	 * 
	 * @param 
	 * @return shipping method name
	 * 
	 */
	public String getSelectedShippingMethodName(){
		driver.pauseExecutionFor(5000);
		String methodName = driver.findElement(SELECTED_SHIPPING_METHOD_LOC).getText();
		logger.info("Selected shipping method name is "+methodName);
		return methodName;
	}

	/***
	 * This method verify Use As Entered displayed in shipping profile or not
	 * 
	 * @param
	 * @return Boolean
	 * 
	 */
	public Boolean isUseAsEnteredPopupDisplayed(){
		return driver.findElement(USE_AS_ENTERED_BUTTON_LOC).isDisplayed();
	}

	/***
	 * This method get the non selected shipping method name
	 * 
	 * @param selected shipping method name, non selected shipping method name, non selected shipping method name
	 * @return shipping method name
	 * 
	 */
	public String getNonSelectedShippingMethodName(String selectedShippingMethodName, String nonSelectedShippingMethodName1,String nonSelectedShippingMethodName2){
		if(selectedShippingMethodName.contains(nonSelectedShippingMethodName1)){
			return nonSelectedShippingMethodName2;
		}else{
			return nonSelectedShippingMethodName1;
		}
	}

	/***
	 * This method clicks on the Continue without Sponsor link
	 * @return same page object
	 */
	public StoreFrontCheckoutPage clickContinueWithoutConsultantLink(){
		driver.click(CONTINUE_WITHOUT_SPONSOR_LOC);
		logger.info("Continue without sponsor link clicked");
		return this;
	}

	/**
	 * This method selects the use my delivery address checkbox
	 * @return
	 */
	public StoreFrontCheckoutPage selectUseMyDeliveyAddressCheckbox(){
		driver.click(USE_MY_DELIVERY_ADDRESS_CHECKBOX_LOC);
		logger.info("Use My delivery address checkbox checked");
		return this;
	}

	/**
	 * This method clicks on the next button after billing address
	 * @return
	 */
	public StoreFrontCheckoutPage clickNextButtonAfterBillingAddress(){
		driver.click(NEXT_BTN_AFTER_BILLING_LOC);
		logger.info("Next button after billing clicked");
		return this;
	}

	/***
	 * This method checks whether the pop up for 
	 * terms and conditions after not selected the checkboxes 
	 * has displayed or not
	 * @return
	 */
	public boolean isPopUpForTermsAndConditionsCheckboxDisplayed(){
		return driver.isElementVisible(POPUP_FOR_TERMS_AND_CONDITIONS_LOC);
	}

	/***
	 * This method click on Edit Shipping profile at checkout page
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickEditLinkOfShippingProfile(){
		driver.pauseExecutionFor(5000);
		//driver.click(EDIT_LINK_OF_SHIPPING_PROFILE_LOC);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(EDIT_LINK_OF_SHIPPING_PROFILE_LOC));
		logger.info("Clicked on Edit link of Shipping profile");
		return this;
	}

	/***
	 * This method get the error message for address line 1 field
	 * 
	 * @param
	 * @return erroe message
	 * 
	 */
	public String getErrorMessageForAddressLine1(){
		String errorMsg =  driver.findElement(ERROR_MESSAGE_FOR_ADDRESS_LINE_1_LOC).getText();
		logger.info("Error message is "+errorMsg);
		return errorMsg;
	}

	/**
	 * This method clicks on the Add new billing profile button
	 * @return
	 */
	public StoreFrontCheckoutPage clickAddNewBillingProfileButton(){
		driver.waitForElementToBeClickable(ADD_NEW_BILLING_PROFILE_BUTTON_LOC, 20);
		driver.click(ADD_NEW_BILLING_PROFILE_BUTTON_LOC);
		logger.info("Add New Billing Profile Button clicked");
		return this;
	}

	/***
	 * This method validates the New Billing Details using firstName of Billing Address.
	 * 
	 * @param String
	 * @return boolean value
	 * 
	 */
	public boolean isNewBillingDetailsVisibleOnUI(String profileFirstName){
		return driver.isElementVisible(By.xpath(String.format(billingInfoAddressNameLoc,profileFirstName)));
	}

	/***
	 * This method get The Credit card last 4 digit from Specfic Billing Profile Details. 
	 * 
	 * @param String
	 * @return String
	 * 
	 */
	public StoreFrontCheckoutPage getCardDetailsFromBillingInfo(String profile){
		cardDetails = driver.getText(By.xpath(String.format(billingInfoCardDetailsLoc, profile))).replaceAll("[^-?0-9]+"," ").trim().split(" ");
		return this;
	}

	/***
	 * This method get The Credit card last 4 digit from Specfic Billing Profile Details. 
	 * 
	 * @param String
	 * @return String
	 * 
	 */
	public String getLastFourDigitsOfCardNumberInBillingDetails(){
		return cardDetails[0];
	}

	/***
	 * This method get the Expiry date of Card from Billing Details. 
	 * 
	 * @param String
	 * @return String
	 * 
	 */
	public String getExpiryDateOfCardNumberInBillingDetails(){
		return cardDetails[1] + "/" + cardDetails[2];
	}

	/***
	 * This method validates the Date Format of Expiry date in Billing Details
	 * 
	 * @param String
	 * @return boolean value
	 * 
	 */
	public boolean isExpiryDateIsPresentAsExpectedInBillingDetails(String expiryDate){
		String[] splittedDate = expiryDate.split("/");
		return (splittedDate[0].length() == 2) && (splittedDate[1].length() == 4); 
	}

	/***
	 * This method get the Billing Details from Billing Profile section 
	 * 
	 * @param String
	 * @return String
	 * 
	 */
	public String getBillingAddressForSpecificBillingProfile(String profile){
		String billingDetails = driver.findElement(By.xpath(String.format(billingAddressLoc, profile))).getText();
		return billingDetails.trim();
	}


	/***
	 * This method looks for the entered consultant by entering the consultant name and 
	 * clicking on the search btn
	 * @return
	 */
	public StoreFrontCheckoutPage searchForConsultant(String consultant){
		driver.type(CONSULTANT_NAME_ID_FIELD_LOC, consultant);
		logger.info("entered consultant name as "+consultant);
		driver.click(CONSULTANT_SEARCH_BTN_LOC);
		logger.info("Consultant search btn clicked");
		return this;
	}

	/***
	 * This method check if 'no result found' msg has been displayed for searched
	 * consultant or not
	 * @return
	 */
	public boolean isNoResultFoundMsgDisplayedForSearchedConsultant(){
		return driver.isElementVisible(NO_RESULT_FOUND_MSG_FOR_SEARCHED_CONSULTANT_LOC);
	}

	/***
	 * This method check the validation msg has been displayed for searched
	 * consultant or not
	 * @return
	 */
	public boolean isValidationMsgDisplayedForSearchedConsultant(){
		return driver.isElementVisible(VALIDATION_MSG_FOR_SEARCHED_CONSULTANT_LOC);
	}

	/***
	 * This method get total no of items
	 * 
	 * @param
	 * @return total no of items
	 * 
	 */
	public int getTotalNoOfItemsAtOrderReviewPage(){
		int totalNoOfItems = driver.findElements(TOTAL_NO_OF_ITEMS_LOC).size(); 
		logger.info("Total no of Items are at order review page: "+totalNoOfItems);
		return totalNoOfItems;
	}

	/***
	 * This method get get subtotal 
	 * 
	 * @param 
	 * @return subtotal
	 * 
	 */
	public String getSubtotalofItemsAtOrderReviewPage(){
		String subtotal = driver.findElement(SUBTOTAL_AT_ORDER_REVIEW_PAGE_LOC).getText();
		logger.info("Subtotal of product is "+subtotal);
		return subtotal;
	}

	/***
	 * This method get delivery charges at order review page
	 * 
	 * @param 
	 * @return delivery charges
	 * 
	 */
	public String getDeliveryChargesAtOrderReviewPage(){
		String deliveryCharges = driver.findElement(DELIVERY_AT_ORDER_REVIEW_PAGE_LOC).getText();
		logger.info("Delivery charges of product is "+deliveryCharges);
		return deliveryCharges;
	}

	/***
	 * This method get product name of first item 
	 * 
	 * @param itemNumber
	 * @return product name
	 * 
	 */
	public String getProductNameAtOrderReviewPage(String itemNumber){
		String productName = null;
		if(itemNumber.equalsIgnoreCase("1")){
			productName = driver.findElement(FIRST_ITEM_PRODUCT_NAME_REVIEW_PAGE_LOC).getText();
		}
		logger.info("product name of "+itemNumber+" is "+productName);
		return productName;
	}


	/***
	 * This method get the confirmation message of consultant enrollment
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isOrderPlacedSuccessfully(){
		return driver.getText(CONFIRMATION_MSG_OF_PLACED_ORDER_LOC).contains("Thank you for your Order!");
	}

	/***
	 * This method get the Order Number after Successful Checkout 
	 * 
	 * @param 
	 * @return String
	 * 
	 */
	public String getOrderNumberAfterCheckout(){
		String orderNumber = driver.getText(CONFIRMATION_MSG_OF_PLACED_ORDER_LOC).replaceAll("[^-?0-9]+","");
		return orderNumber;
	}

	/***
	 * This method validates login or register text displayed or not
	 * 
	 * @param 
	 * @return boolean value
	 * 
	 */
	public boolean isLoginOrRegisterTextDisplayed(){
		return driver.isElementVisible(LOGIN_REGISTER_TEXT_LOC);
	}
	/***
	 * This method login to storefront on checkout page
	 * 
	 * @param 
	 * @return checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage loginAtCheckoutPage(String userName, String password){
		driver.type(USER_NAME_LOC, userName);
		logger.info("user name is entered"+userName);
		driver.type(PASSWORD_FOR_LOGIN_LOC, password);
		logger.info("password is entered"+password);
		driver.click(LOGIN_AND_CHECKOUT_BUTTON_LOC);
		logger.info("clicked on login and checkout button"+password);
		driver.pauseExecutionFor(10000);
		return this;
	}

	/***
	 * This method click on Edit Billing Address at checkout page
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickEditLinkOfBillingProfile(){
		//driver.click(EDIT_LINK_OF_BILLING_PROFILE_LOC);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(EDIT_LINK_OF_BILLING_PROFILE_LOC));
		logger.info("Clicked on Edit link of Billing profile");
		return this;
	}
	/***
	 * This method validates billing address dropdown is enabled
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isBillingAddressDropdownEnabled(){
		return driver.findElement(BILLING_ADDRESS_DD_LOC).isEnabled();
	}

	/***
	 * This method enter the billing address details after click on add a new billing profile
	 * 
	 * @param First name,Last name, address line1, city, state, postal code, phone number
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage enterBillingAddressDetailsAtCheckout(String firstName, String lastName, String addressLine1, String addressLine2, String city, String state, String postal, String phoneNumber){
		String completeName = firstName+" "+lastName;
		driver.type(FIRST_LAST_NAME_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE1_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(ADDRESS_LINE_2_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC, addressLine2);
		logger.info("Entered address line 2 as "+addressLine2);
		driver.type(CITY_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetails, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(PHONE_NUMBER_FOR_BILLING_ADDRESS_AT_CHECKOUT_PAGE_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);
		return this;
	}

	/***
	 * This method click on Edit Billing Address at checkout page
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickEditLinkOfBillingAddress(){
		driver.click(EDIT_LINK_OF_BILLING_ADDRESS_LOC);
		logger.info("Clicked on Edit link of Billing Address");
		return this;
	}

	/***
	 * This method get the credit card details
	 * 
	 * @param
	 * @return credit card details
	 * 
	 */
	public String getCreditCardDetailsFromBillingProfile(){
		driver.pauseExecutionFor(5000);
		String creditCardDetails = driver.findElement(CREDIT_CARD_DETAILS_LOC).getText();
		logger.info("credit card details are "+creditCardDetails);
		return creditCardDetails;
	}

	/***
	 * This method get the error msg of card
	 * 
	 * @param
	 * @return credit card error message
	 * 
	 */
	public String getCreditCardErrorMessage(){
		driver.switchTo().frame(driver.findElement(IFRAME_LOC));
		logger.info("Switched into iframe");
		String creditCardErrorMsg = driver.findElement(CREDIT_CARD_ERROR_MSG_LOC).getText();
		logger.info("credit card error message is "+creditCardErrorMsg);
		driver.switchTo().defaultContent();
		logger.info("Switched to default content");
		return creditCardErrorMsg;
	}

	/***
	 * This method get the error msg of CVV
	 * 
	 * @param
	 * @return CVV error message
	 * 
	 */
	public String getCVVErrorMessage(){
		driver.switchTo().frame(driver.findElement(IFRAME_LOC));
		logger.info("Switched into iframe");
		String ErrorMsg = driver.findElement(CVV_ERROR_MSG_LOC).getText();
		logger.info("CVV error message is "+ErrorMsg);
		driver.switchTo().defaultContent();
		logger.info("Switched to default content");
		return ErrorMsg;
	}

	/***
	 * This method get the error msg of exp date
	 * 
	 * @param
	 * @return Exp date error message
	 * 
	 */
	public String getExpDateErrorMessage(){
		driver.switchTo().frame(driver.findElement(IFRAME_LOC));
		logger.info("Switched into iframe");
		String ErrorMsg = driver.findElement(INVALID_EXP_DATE_ERROR_MSG_LOC).getText();
		logger.info("Exp date error message is "+ErrorMsg);
		driver.switchTo().defaultContent();
		logger.info("Switched to default content");
		return ErrorMsg;
	}

	/***
	 * This method validates Billing Profile Card Details Fields are in editable mode.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isCardDetailsFieldsEnabled(){
		driver.switchTo().frame(driver.findElement(IFRAME_LOC));
		logger.info("Switched into iframe");
		boolean flag = driver.findElement(NAME_ON_CARD_LOC).isEnabled() &&
				driver.findElement(CARD_NUMBER_LOC).isEnabled() &&
				driver.findElement(CARD_TYPE_DD_LOC).isEnabled() &&
				driver.findElement(EXP_MONTH_DD_LOC).isEnabled() &&
				driver.findElement(EXP_YEAR_DD_LOC).isEnabled() &&
				driver.findElement(CVV_LOC).isEnabled();
		driver.switchTo().defaultContent();
		logger.info("Switched to default content");
		return flag;
	}

	/***
	 * This method validates the error message for billing address fields
	 * 
	 * @param error message
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForBillingAddressFields(String errorMessage){
		return driver.isElementVisible(By.xpath(String.format(billingAddressFieldsErrorMessageLoc, errorMessage)));
	}

	/***
	 * This method click the SAVE Payment button
	 * 
	 * @param
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontCheckoutPage clickSavePaymentButton(){
		driver.click(SAVE_PAYMENT_BUTTON_LOC);
		logger.info("Save payment button clicked of billing details");
		return this;
	}

	/***
	 * This method enter the billing address details after click on add a new billing profile
	 * 
	 * @param First name,Last name, address line1, city, state, postal code, phone number
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage enterEditBillingAddressDetailsAtCheckout(String firstName, String lastName, String addressLine1, String addressLine2, String city, String state, String postal, String phoneNumber){
		String completeName = firstName+" "+lastName;
		driver.type(FIRST_LAST_NAME_FOR_EDIT_BILLING_ADDRESS_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE1_FOR_EDIT_BILLING_ADDRESS_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(ADDRESS_LINE_2_FOR_EDIT_BILLING_ADDRESS_LOC, addressLine2);
		logger.info("Entered address line 2 as "+addressLine2);
		driver.type(CITY_FOR_EDIT_BILLING_ADDRESS_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_FOR_EDIT_BILLING_ADDRESS_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForBillingAddressDetails, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_FOR_EDIT_BILLING_ADDRESS_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(PHONE_NUMBER_FOR_EDIT_BILLING_ADDRESS_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);
		return this;
	}

	/***
	 * This method get BILLING PROFILE details
	 * 
	 * @param
	 * @return billing profile details
	 * 
	 */
	public String getBillingProfileDetailsFromBillingProfile(){
		String details = driver.findElement(BILLING_PROFILE_LOC).getText();
		logger.info("Billing profile details are "+details);
		return details;
	}

	/***
	 * This method get the error msg of billing address details
	 * 
	 * @param
	 * @return  error message
	 * 
	 */
	public String getErrorMessageForBillingAddressDetails(){
		String ErrorMsg = driver.findElement(ERROR_MSG_FOR_BILLING_ADDRESS_DETAILS_LOC).getText();
		logger.info("Error message for billing address details is "+ErrorMsg);
		return ErrorMsg;
	}

	/***
	 * This method get the error msg invalid billing address
	 * 
	 * @param
	 * @return error message
	 * 
	 */
	public String getErrorMessageForBillingAddressDetailsWhileAddANewAddress(){
		String ErrorMsg = driver.findElement(ERROR_MSG_FOR_INVALID_BILLING_ADDRESS_LOC).getText();
		logger.info("Error message for billing address details is while add a new address "+ErrorMsg);
		return ErrorMsg;
	}
}
