package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

import java.util.List;

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
	private final By POSTAL_CODE_ERROR_MSG_LOC = By.xpath("//label[@id='address.postcode-error']");
	private final By FIRST_LAST_NAME_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC=By.xpath("//div[contains(@id,'billingAddressForm')]//input[@id='address.firstName']");
	private final By ADDRESS_LINE1_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC=By.xpath("//div[contains(@id,'billingAddressForm')]//input[@id='address.line1']");
	private final By ADDRESS_LINE_2_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC=By.xpath("//div[contains(@id,'billingAddressForm')]//input[@id='address.line2']");
	private final By CITY_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC=By.xpath("//div[contains(@id,'billingAddressForm')]//input[@id='address.townCity']");
	private final By STATE_DD_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC=By.xpath("//div[contains(@id,'billingAddressForm')]//select[@id='address.region']");
	private final By POSTAL_CODE_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC=By.xpath("//div[contains(@id,'billingAddressForm')]//input[@id='address.postcode']");
	private final By PHONE_NUMBER_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC=By.xpath("//div[contains(@id,'billingAddressForm')]//input[@id='address.phone']");
	private final By FIRST_LAST_NAME_FOR_BILLING_ADDRESS_LOC=By.xpath("//div[@id='account-billing-container']//input[@id='address.firstName']");
	private final By ADDRESS_LINE1_FOR_BILLING_ADDRESS_LOC=By.xpath("//div[@id='account-billing-container']//input[@id='address.line1']");
	private final By ADDRESS_LINE_2_FOR_BILLING_ADDRESS_LOC=By.xpath("//div[@id='account-billing-container']//input[@id='address.line2']");
	private final By CITY_FOR_BILLING_ADDRESS_LOC=By.xpath("//div[@id='account-billing-container']//input[@id='address.townCity']");
	private final By STATE_DD_FOR_BILLING_ADDRESS_LOC=By.xpath("//div[@id='account-billing-container']//select[@id='address.region']");
	private final By POSTAL_CODE_FOR_BILLING_ADDRESS_LOC=By.xpath("//div[@id='account-billing-container']//input[@id='address.postcode']");
	private final By PHONE_NUMBER_FOR_BILLING_ADDRESS_LOC=By.xpath("//div[@id='account-billing-container']//input[@id='address.phone']");
	private final By CHECKOUT_BTN_CONSULTANT_LOC = By.id("checkoutPopup");
	private final By CONFIRMATION_MSG_OF_PLACED_ORDER_LOC = By.xpath("//div[@class='orderHeading']/h1");
	private final By ADDRESS_NAME_ERROR_MGS_LOC =  By.xpath("//span[@id='billTo_firstName.errors' and contains(text(),'Name must contain first name and last name with no special characters.')]");
	private final By SHIPPING_LINK_AT_CHECKOUT_PAGE_LOC = By.xpath("//div[contains(text(),'Shipping')]/ancestor::a[1]");
	private final By BILLING_LINK_AT_CHECKOUT_PAGE_LOC = By.xpath("//div[contains(text(),'Billing')]/ancestor::a[1]");
	private final By SHIPPING_NAME_AT_CHECKOUT_PAGE_LOC=By.id("address.firstName");
	private final By SHIPPING_ADDRESS_LINE_1_AT_CHECKOUT_PAGE_LOC=By.id("address.line1");
	private final By SHIPPING_ADDRESS_LINE_2_AT_CHECKOUT_PAGE_LOC=By.id("address.line2");
	private final By SHIPPING_CITY_AT_CHECKOUT_PAGE_LOC=By.id("address.townCity");
	private final By SHIPPING_STATE_AT_CHECKOUT_PAGE_LOC=By.id("address.region");
	private final By SHIPPING_POSTAL_CODE_AT_CHECKOUT_PAGE_LOC=By.id("address.postcode");
	private final By SHIPPING_PHONE_NUMBER_AT_CHECKOUT_PAGE_LOC=By.id("address.phone");
	private final By PROFILE_FOR_FUTURE_AUTOSHIP_CHECKBOX_LOC = By.xpath("//label[@for='futureAutoship']");
	private final By TERMS_AND_CONDITIONS_CHCKBOX_FOR_CONSULTANT_CRP_LOC = By.xpath("//a[contains(text(),'terms and conditions')]/ancestor::label[1]/preceding-sibling::input[1]");
	private final By POLICIES_AND_PROCEDURES_CHECBOX_LOC = By.xpath("//a[contains(text(),'policies and procedures')]/ancestor::label[1]/preceding-sibling::input[1]");
	private final By QUEBEC_PROVINCE_FOR_SHIPPING_LOC=By.xpath("//option[@disabled='disabled' and text()='Quebec']");
	private final By SELECTED_SHIPPING_METHOD_LOC = By.xpath("//li[@class='checked']/label");
	private final By EDIT_LINK_OF_ORDERS_SUMMARY_LOC=By.xpath("//div[@class='price']/a[contains(text(),'Edit')]");
	private final By CANCEL_BUTTON_LOC=By.xpath("//button[contains(text(),'Cancel')]");
	private final By EDIT_LINK_OF_ACCOUNT_INFO_LOC=By.xpath("//a[@class='editIcon']");
	private final By CONTINUE_WITHOUT_SPONSOR_LOC = By.xpath("//a[contains(text(),'Continue Without a Consultant')]");
	private final By NEXT_BTN_AFTER_BILLING_LOC = By.id("reviewOrder");
	private final By EDIT_LINK_OF_SHIPPING_PROFILE_LOC=By.xpath("//span[@id='defaultShippingAddress']/a");
	private final By CONSULTANT_NAME_ID_FIELD_LOC = By.id("sponserparam");
	private final By CONSULTANT_SEARCH_BTN_LOC = By.id("search-sponsor-button");
	private final By REQUEST_A_CONSULTANT_LOC = By.xpath("//a[contains(text(),'Request a Consultant')]");
	private final By NO_RESULT_FOUND_MSG_FOR_SEARCHED_CONSULTANT_LOC = By.xpath("//p[@class='noResult' and contains(text(),'No results found for')]");
	private final By VALIDATION_MSG_FOR_SEARCHED_CONSULTANT_LOC = By.id("sponserparam-error");
	private final By TOTAL_NO_OF_ITEMS_LOC = By.xpath("//div[contains(text(),'Order Summary')]/following::li[contains(@class,'list-items')]//div[@class='thumb']");
	private final By SUBTOTAL_AT_ORDER_REVIEW_PAGE_LOC = By.xpath("//div[contains(text(),'Order Summary')]/following::p[text()='Subtotal:']/following::span[1]");
	private final By DELIVERY_AT_ORDER_REVIEW_PAGE_LOC = By.xpath("//div[contains(text(),'Order Summary')]/following::p[text()='Delivery']/following::span[1]");
	private final By FIRST_ITEM_PRODUCT_NAME_REVIEW_PAGE_LOC = By.xpath("//div[contains(text(),'Order Summary')]/following::li[contains(@class,'list-items')][1]//div[@class='name']/a");
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
	private final By EDIT_LINK_OF_BILLING_ADDRESS_LOC=By.xpath("//div[contains(text(),'Billing Info')]/following::a[1]");
	private final By CREDIT_CARD_DETAILS_LOC = By.xpath("//div[@id='default-payment-method']//span[@class='cardInfo']");
	private final By INVALID_EXP_DATE_ERROR_MSG_LOC = By.id("valmsg-c-exyr");
	private final By CVV_ERROR_MSG_LOC = By.id("card_cvNumber-error");
	private final By CREDIT_CARD_ERROR_MSG_LOC = By.id("valmsg-c-cn");
	private final By SAVE_PAYMENT_BUTTON_LOC = By.id("updateBillingAddress");
	private final By ADD_NEW_BILLING_PROFILE_BUTTON_LOC = By.xpath("//button[contains(text(),'Add New Billing Profile')]");
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
	private final By NOT_YOUR_SPONSOR_LINK_LOC = By.xpath("//a[contains(text(),'Continue Without a Consultant')]");
	private final By UNCHECKED_SHIPPING_METHOD_LOC = By.xpath("//div[@id='delivery_method']//li[not(@class='checked')]/label");
	private final By SHIPPING_METHOD_TITLE_FORM_ALL_SHIPPING_DETAILS_LOC = By.xpath("//div[contains(@class,'checkout-steps')]/descendant::div[@class='checkout-shipping-items'][2]/dl[2]/dd[1]");
	private final By CONFIRM_CRP_ORDER_BTN_LOC = By.xpath("//button[@id='confirmCRPOrder']");
	private final By FIRST_LAST_NAME_FOR_NEW_SHIPPING_ADDRESS_LOC = By.xpath("//form[@id='addressForm']//input[@id='address.firstName']");
	private final By ADDRESS_LINE1_FOR_NEW_SHIPPING_ADDRESS_LOC = By.xpath("//form[@id='addressForm']//input[@id='address.line1']");
	private final By ADDRESS_LINE_2_FOR_NEW_SHIPPING_ADDRESS_LOC = By.xpath("//form[@id='addressForm']//input[@id='address.line2']");
	private final By CITY_FOR_NEW_SHIPPING_ADDRESS_LOC = By.xpath("//form[@id='addressForm']//input[@id='address.townCity']");
	private final By STATE_DD_FOR_NEW_SHIPPING_ADDRESS_LOC = By.xpath("//form[@id='addressForm']//select[@id='address.region']");
	private final By POSTAL_CODE_FOR_NEW_SHIPPING_ADDRESS_LOC = By.xpath("//form[@id='addressForm']//input[@id='address.postcode']");
	private final By PHONE_NUMBER_FOR_NEW_SHIPPING_ADDRESS_LOC = By.xpath("//form[@id='addressForm']//input[@id='address.phone']");
	private final By SAVE_SHIPPING_ADDRESS_CHECKBOX_LOC = By.xpath("//label[contains(@for,'saveAddressInMyAddressBook')]");
	private final By SPONSOR_INFO_LOC = By.xpath("//div[@id='sponsorInfo']/span");
	private final By NOT_YOUR_CONSULTANT_LINK_LOC = By.id("not-your-sponsor");
	private final By FIRST_NAME_ERROR_FIELD_LOC = By.id("first-name-error");
	private final By LAST_NAME_ERROR_FIELD_LOC = By.id("last-name-error");
	private final By EMAIL_ERROR_FIELD_LOC = By.id("email-account-error");
	private final By PASSWORD_ERROR_FIELD_LOC = By.id("password-error");
	private final By CONFIRM_PASSWORD_ERROR_FIELD_LOC = By.id("the-password-again-error");
	private final By SHIPPING_PROFILE_IN_SHIPPING_SECTION_LOC = By.xpath("//div[contains(text(),'Shipping')]/ancestor::h2/following-sibling::div/descendant::dd[1]");
	private final By USE_MY_DELIVERY_ADDRESS_CHK_BOX_FOR_EXISTING_PROFILE_LOC= By.xpath("//label[contains(@class,'useDeliveryAddress') and contains(@class,'editUseAddressLabel')]");
	private final By CARD_TYPE_DD_AFTER_EDIT_PROFILE_LOC = By.xpath("//div[@id='account-billing-container']//select[@id='card_cardType']");
	private final By CARD_NUMBER_AFTER_EDIT_PROFILE_LOC= By.xpath("//div[@id='account-billing-container']//input[@id='card_accountNumber']");
	private final By NAME_ON_CARD_AFTER_EDIT_PROFILE_LOC= By.xpath("//div[@id='account-billing-container']//input[@id='card_nameOnCard']");
	private final By EXP_MONTH_DD_AFTER_EDIT_PROFILE_LOC= By.xpath("//div[@id='account-billing-container']//select[@id='ExpiryMonth']");
	private final By EXP_MONTH_AFTER_EDIT_PROFILE_LOC= By.xpath("//div[@id='account-billing-container']//select[@id='ExpiryMonth']//option[11]");
	private final By EXP_YEAR_DD_AFTER_EDIT_PROFILE_LOC= By.xpath("//div[@id='account-billing-container']//select[@id='ExpiryYear']");
	private final By EXP_YEAR_AFTER_EDIT_PROFILE_LOC= By.xpath("//div[@id='account-billing-container']//select[@id='ExpiryYear']//option[11]");
	private final By CVV_AFTER_EDIT_PROFILE_LOC= By.xpath("//div[@id='account-billing-container']//input[@id='card_cvNumber']");
	private final By SPONSOR_NAME_ACCOUNT_INFO_LOC = By.xpath("//div[contains(@id,'findConsultantResultArea')]//span[@id='selectd-consultant']");
	private final By NOT_YOUR_CONSULTAN_LINK_LOC = By.id("not-your-autoSponsor");
	private final By CLOSE_BTN_OF_POPUP_LOC = By.id("close_popup");
	private final By USE_SAVED_CARD_BTN_LOC = By.xpath("//button[contains(text(),'Use a saved card')]");
	private final By USE_THESE_PAYMENT_DETAILS_BTN_LOC = By.xpath("//button[contains(text(),'Use these payment details')]");
	private final By SHIPPING_METHOD_AFTER_ORDER_PLACED = By.xpath("//div[contains(text(),'Shipping Method')]");
	private final By DEFAULT_BILLING_PROFILE_NAME_LOC = By.xpath("//div[@id='default-payment-method']//strong");
	private final By CONFIRM_CRP_ORDER_MSG_LOC = By.xpath("//h2[contains(text(),'CRP ORDER CONFIRMED')]");
	private final By BILLING_PROFILE_AFTER_ORDER_PLACED = By.xpath("//div[@class='orderBillingAddress']");
	private final By BILLING_PROFILE_NAME_LOC = By.xpath("//div[@id='default-payment-method']//strong");
	private final By CREDIT_CARD_NUMBER_AT_ORDER_CONFIRMATION_PAGE = By.xpath("//div[@class='orderBillingDetails']/div[2]");
	private final By EDIT_LINK_OF_SHIPPING_DETAILS = By.xpath("//div[contains(text(),'Shipping')]/following-sibling::a[contains(text(),'Edit')]");
	private final By ORDER_NUMBER_AT_CONFIRMATION_PAGE_OF_PLACED_ORDER_LOC = By.xpath("//div[@class='orderHeading']");
	private final By USE_MY_DELIVERY_ADDRESS_CHECKBOX_LOC = By.xpath("//input[@id='useDeliveryAddress']");

	private final By ADDRESS_BOOK_BTN_LOC = By.xpath("//button[contains(text(),'Address Book')]");
	private String useThisAddressBtnInAddressBookLoc = "//div[@id='addressbook']/descendant::form[@id='useShipAddressFromBook'][%s]//button";
	private String profileNameFromAddressBookLoc = "//div[@id='addressbook']/descendant::strong[%s]";
	private String useThisPaymentDetailsBtnInSavedCardLoc = "//div[@id='savedpaymentsbody']/descendant::button[contains(text(),'Use these payment details')][%s]";
	private String profileNameFromSavedCardLoc = "//div[@id='savedpaymentsbody']/descendant::strong[%s]";
	private String orderItemsTagLoc = "//li[@class='orderItemsHeading']//div[contains(text(),'%s')]";
	private String chargesFromOrderConfirmationPage = "//div[@class='orderGrandTotal']//div[contains(text(),'%s')]/following::div[@class='orderValue'][1]";
	private String chargesFromOrderReviewPage = "//*[contains(text(),'%s')]/following::span[1]";
	private String billingInfoAddressNameLoc = "//div[@id='default-payment-method']//strong[contains(text(),'%s')]";
	private String billingAddressLoc = "//div[@id='default-payment-method']//strong[contains(text(),'%s')]/ancestor::div[1]";
	private String billingInfoCardDetailsLoc = "//div[@id='default-payment-method']//strong[contains(text(),'%s')]/following-sibling::span[@class='cardInfo']";
	private String mandatoryFieldErrorMsgOfAddressForNewShippingProfileLoc = "//form[@id='addressForm']//label[contains(@id,'%s-error') and contains(text(),'This field is required.')]";
	private String stateForNewShippingAddressDetailsLoc = "//form[@id='addressForm']//select[@id='address.region']//option[text()='%s']";
	private String mandatoryFieldErrorMsgForNewAddressLoc  = "//label[contains(@id,'%s-error') and contains(text(),'This field is required.')]";
	private String stateForShippingDetailsForNewProfileLoc = "//div[contains(@id,'illingAddressForm')]//select[@id='address.region']//option[text()='%s']";
	private String mandatoryFieldErrorMsgOfAddressForNewBillingProfileLoc = "//div[@id='billingAddressForm']//label[contains(@id,'%s-error') and contains(text(),'This field is required.')]";
	private String mandatoryFieldErrorMsgOfAddressForExistingBillingProfileLoc = "//span[contains(@id,'%s.errors')]";
	private String errorMessageForBillingProfileDetails = "//div[contains(text(),'%s')]";
	private String shippingMethodCheckboxUsingLabel = "//div[@id='delivery_method']//label[@for='%s']/preceding-sibling::input";
	private String mandatoryFieldErrorMsg = "//div[@id='billingAddressForm']//label[contains(@id,'%s-error')]";
	private String stateForBillingAddressDetails = "//div[@id='checkoutEditBillingAddressForm']//option[text()='%s']";
	private String stateForShippingDetails = "//div[@id='checkoutEditBillingAddressForm']//option[text()='%s']";
	private String stateForShippingDetailsAtCheckoutPageLoc = "//form[@id='shippingAddressForm']//select[@id='address.region']//option[text()='%s']";
	private String shippingMethodLoc = "//label[contains(text(),'%s')]";
	private String billingAddressFieldsErrorMessageLoc = "//span[contains(text(),'%s')]";
	private String stateForBillingAddress = "//div[@id='account-billing-container']//select[@id='address.region']//option[text()='%s']";
	private String cardTypeAfterEditProfileLoc= "//div[@id='account-billing-container']//select[@id='card_cardType']//option[text()='%s']";

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
		return driver.isElementVisible(USE_AS_ENTERED_BUTTON_LOC);
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
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(ADD_NEW_BILLING_PROFILE_BUTTON_LOC));
		//driver.click(ADD_NEW_BILLING_PROFILE_BUTTON_LOC);
		logger.info("Add New Billing Profile Button clicked");
		driver.pauseExecutionFor(2000);
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
		String ErrorMsg = driver.findElement(CVV_ERROR_MSG_LOC).getText();
		logger.info("CVV error message is "+ErrorMsg);
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
		String ErrorMsg = driver.findElement(INVALID_EXP_DATE_ERROR_MSG_LOC).getText();
		logger.info("Exp date error message is "+ErrorMsg);
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
		boolean flag = driver.findElement(NAME_ON_CARD_LOC).isEnabled() &&
				driver.findElement(CARD_NUMBER_LOC).isEnabled() &&
				driver.findElement(CARD_TYPE_DD_LOC).isEnabled() &&
				driver.findElement(EXP_MONTH_DD_LOC).isEnabled() &&
				driver.findElement(EXP_YEAR_DD_LOC).isEnabled() &&
				driver.findElement(CVV_LOC).isEnabled();
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

	/***
	 * This method verify the Continue without Sponsor link
	 * @param
	 * @return boolean
	 */
	public boolean isContinueWithoutConsultantLinkPresent(){
		return driver.isElementVisible(CONTINUE_WITHOUT_SPONSOR_LOC);
	}

	/***
	 * This method verify the not your Sponsor link
	 * @param
	 * @return boolean
	 */
	public boolean isNotYourSponsorLinkIsPresent(){
		return driver.isElementVisible(NOT_YOUR_SPONSOR_LINK_LOC);
	}

	/***
	 * This method get the shipping Profile on Confirmation Page 
	 * 
	 * @param 
	 * @return Shipping Profile Name
	 * 
	 */
	public String getShippingProfileFromConfirmationPage(){
		String profileNameAtConfirmation=driver.findElement(By.xpath("//div[@class='orderShippingAddress']//li[1]")).getText();
		logger.info("Shipping Profile name at Confimation Page : "+profileNameAtConfirmation);
		return profileNameAtConfirmation;
	}

	/***
	 * This method get the Last four Digits of Credit Card from Confirmation Page 
	 * 
	 * @param 
	 * @return 4 Digits
	 * 
	 */
	public String getLastFourNumbersOfBillingDetailsOnConFirmationPage(){
		String ccNumber=driver.findElement(CREDIT_CARD_NUMBER_AT_ORDER_CONFIRMATION_PAGE).getText();
		String lastFourNumbers=ccNumber.substring(ccNumber.lastIndexOf(' ') + 1);
		logger.info("Last Four Numbers of Billing Profile at Confimation Page : "+lastFourNumbers);
		return lastFourNumbers;
	}

	/***
	 * This method validates the presence of mandatory field msgs for billing profile
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isAllErrrorMsgsForEnteringMandatoryFieldsForBillingProfileIsPresent(){
		boolean flag = false;
		driver.switchTo().frame(driver.findElement(IFRAME_LOC));
		flag = driver.isElementVisible(By.xpath(String.format(errorMessageForBillingProfileDetails, TestConstants.NAME_ON_CARD_MANDATORY_MSG))) &&
				driver.isElementVisible(By.xpath(String.format(errorMessageForBillingProfileDetails, TestConstants.CARD_NUM_MANDATORY_MSG))) &&
				driver.isElementVisible(By.xpath(String.format(errorMessageForBillingProfileDetails, TestConstants.CARD_TYPE_MANDATORY_MSG))) &&
				driver.isElementVisible(By.xpath(String.format(errorMessageForBillingProfileDetails, TestConstants.EXPIRY_MONTH_MANDATORY_MSG))) &&
				driver.isElementVisible(By.xpath(String.format(errorMessageForBillingProfileDetails, TestConstants.EXPIRY_YEAR_MANDATORY_MSG))) &&
				driver.isElementVisible(By.xpath(String.format(errorMessageForBillingProfileDetails, TestConstants.CARD_PIN_MANDATORY_MSG)));
		driver.switchTo().defaultContent();
		return flag;
	}

	/***
	 * This method validates the New Billing Details using firstName of Billing Address.
	 * 
	 * @param String
	 * @return boolean value
	 * 
	 */
	public boolean isUpdatedDefaultBillingDetailsVisibleOnUI(String profileFirstName){
		return driver.isElementVisible(By.xpath(String.format(billingInfoAddressNameLoc,profileFirstName)));
	}

	/**
	 * This method update the shipping address details at checkout page
	 * 
	 * @param firstName, lastName, addressLine1, city, state, postal code, phone number
	 * @return
	 */
	public StoreFrontCheckoutPage updateShippingAddressDetailsAtCheckoutPage(String firstName, String lastName, String addressLine1, String addressLine2, String city, String state, String postal, String phoneNumber) {
		String completeName = firstName+" "+lastName;
		driver.pauseExecutionFor(5000);
		driver.type(FIRST_LAST_NAME_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE1_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(ADDRESS_LINE_2_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, addressLine2);
		logger.info("Entered address line 2 as "+addressLine2);
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
	 * This method select the shipping method other than the initial one and return the label of shipping method
	 * 
	 * @param 
	 * @return String 
	 * 
	 */
	public String changeTheShippingMethodAtCheckoutPage(){
		List<WebElement> shippingMethods = driver.findElements(UNCHECKED_SHIPPING_METHOD_LOC);
		int randomNum = CommonUtils.getRandomNum(0,shippingMethods.size()-1);
		WebElement methodToSelect = shippingMethods.get(randomNum);
		String methodLabel = methodToSelect.getAttribute("for");
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath(String.format(shippingMethodCheckboxUsingLabel, methodLabel))));
		logger.info("Clicked Shipping method : " + methodLabel);
		return methodLabel;
	}

	/***
	 * This method get the Title name of Selected shipping method
	 * 
	 * @param 
	 * @return String 
	 * 
	 */
	public String getTitleOfSelectedShippingMethod(){
		String shippingMethodName = driver.getText(SELECTED_SHIPPING_METHOD_LOC);
		String title = shippingMethodName.split("-")[0].trim();
		logger.info("Title of Shipping method selected on checkout page : " + title);
		return title;
	}

	/***
	 * This method get the label of Selected shipping method
	 * 
	 * @param 
	 * @return String 
	 * 
	 */
	public String getLabelOfSelectedShippingMethod(){
		return driver.getAttribute(SELECTED_SHIPPING_METHOD_LOC, "for").trim();
	}

	/***
	 * This method get the Shipping title from all shipping details at checkout page
	 * 
	 * @param 
	 * @return String 
	 * 
	 */
	public String getTitleOfShippingMethodFromShippingDetails(){
		return driver.getText(SHIPPING_METHOD_TITLE_FORM_ALL_SHIPPING_DETAILS_LOC).trim();
	}

	/***
	 * This method validates the presence of mandatory field msgs for billing profile
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isErrrorMsgsForAllMandatoryFieldsForBillingAddressArePresent(){
		return isMandatoryFieldMsgPresentForTheField("firstName") &&
				isMandatoryFieldMsgPresentForTheField("line1") &&
				isMandatoryFieldMsgPresentForTheField("townCity") &&
				isMandatoryFieldMsgPresentForTheField("region") &&
				isMandatoryFieldMsgPresentForTheField("postcode") &&
				isMandatoryFieldMsgPresentForTheField("phone");
	}

	/***
	 * This method get the Error message for invalid postal code
	 * 
	 * @param 
	 * @return String 
	 * 
	 */
	public String getErrorMessageForInvalidPostalCode(){
		return driver.getText(POSTAL_CODE_ERROR_MSG_LOC).trim();
	}

	/***
	 * This method validates the presence of mandatory field msgs for billing profile
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isErrrorMsgsForAllMandatoryFieldsForBillingAddressWithExistingProfileArePresent(){
		return isErrorMsgPresentAsExpectedForTheMandatoryAddressFieldForExistingBillingProfile("firstName",TestConstants.ADDRESS_NAME_MANDATORY_MSG) &&
				isErrorMsgPresentAsExpectedForTheMandatoryAddressFieldForExistingBillingProfile("street1",TestConstants.ADDRESS_LINE_MANDATORY_MSG) &&
				isErrorMsgPresentAsExpectedForTheMandatoryAddressFieldForExistingBillingProfile("city",TestConstants.CITY_MANDATORY_MSG) &&
				isErrorMsgPresentAsExpectedForTheMandatoryAddressFieldForExistingBillingProfile("state",TestConstants.PROVINCE_MANDATORY_MSG) &&
				isErrorMsgPresentAsExpectedForTheMandatoryAddressFieldForExistingBillingProfile("postalCode",TestConstants.POSTAL_CODE_MANDATORY_MSG) &&
				isErrorMsgPresentAsExpectedForTheMandatoryAddressFieldForExistingBillingProfile("phoneNumber",TestConstants.PHONE_NUMBER_MANDATORY_MSG);
	}

	/***
	 * This method enter the billing address details after editing default billing profile
	 * 
	 * @param First name,Last name, address line1, city, state, postal code, phone number
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage enterBillingAddressDetailsAtCheckoutForNewBillingProfile(String firstName, String lastName, String addressLine1, String addressLine2, String city, String state, String postal, String phoneNumber){
		String completeName = firstName+" "+lastName;
		driver.type(FIRST_LAST_NAME_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE1_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(ADDRESS_LINE_2_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC, addressLine2);
		logger.info("Entered address line 2 as "+addressLine2);
		driver.type(CITY_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetailsForNewProfileLoc, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(PHONE_NUMBER_FOR_BILLING_ADDRESS_FOR_NEW_PROFILE_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);
		return this;
	}

	/***
	 * This method validates the presence of Shipping link at checkout page
	 * 
	 * @param
	 * @return boolean 
	 * 
	 */
	public boolean isShippingLinkPresentAtCheckoutPage(){
		return driver.isElementVisible(SHIPPING_LINK_AT_CHECKOUT_PAGE_LOC);
	}
	/***
	 * This method validates the presence of Billing link at checkout page
	 * 
	 * @param
	 * @return boolean 
	 * 
	 */
	public boolean isBillingLinkPresentAtCheckoutPage(){
		return driver.isElementVisible(BILLING_LINK_AT_CHECKOUT_PAGE_LOC);
	}


	/***
	 * This method validates the presence of mandatory field msg for billing address details
	 * 
	 * @param String field
	 * @return boolean 
	 * 
	 */
	private boolean isMandatoryFieldMsgPresentForTheField(String field){
		boolean isMsgPresent = driver.isElementPresent(By.xpath(String.format(mandatoryFieldErrorMsgOfAddressForNewBillingProfileLoc, field)));
		logger.info("Is Expected Mandatory field msg is present for " + field + " : " + isMsgPresent);
		return isMsgPresent;
	}

	/***
	 * This method validates the presence of mandatory field msgs for billing profile
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isErrrorMsgsForAllMandatoryFieldsArePresent(){
		return isMandatoryFieldMsgPresentForTheField("firstName") &&
				isMandatoryFieldMsgPresentForTheField("line1") &&
				isMandatoryFieldMsgPresentForTheField("townCity") &&
				isMandatoryFieldMsgPresentForTheField("region") &&
				isMandatoryFieldMsgPresentForTheField("postcode") &&
				isMandatoryFieldMsgPresentForTheField("phone");
	}

	/**
	 * This method fill new shipping address details at checkout page
	 * 
	 * @param firstName, lastName, addressLine1, addressLine2 city, state, postal code, phone number
	 * @return
	 */
	public StoreFrontCheckoutPage enterNewShippingAddressDetailsAtCheckoutPage(String firstName, String lastName, String addressLine1, String addressLine2, String city, String state, String postal, String phoneNumber) {
		String completeName = firstName+" "+lastName;
		driver.pauseExecutionFor(5000);
		driver.type(FIRST_LAST_NAME_FOR_NEW_SHIPPING_ADDRESS_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE1_FOR_NEW_SHIPPING_ADDRESS_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(ADDRESS_LINE_2_FOR_NEW_SHIPPING_ADDRESS_LOC,addressLine2);
		logger.info("Address line 2 cleared");
		driver.type(CITY_FOR_NEW_SHIPPING_ADDRESS_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_FOR_NEW_SHIPPING_ADDRESS_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForNewShippingAddressDetailsLoc, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_FOR_NEW_SHIPPING_ADDRESS_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(PHONE_NUMBER_FOR_NEW_SHIPPING_ADDRESS_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);
		return this;
	}

	/***
	 * This method select checkbox for saving shipping address
	 * 
	 * @param 
	 * @return Store front checkout page object 
	 * 
	 */
	public StoreFrontCheckoutPage selectCheckboxToSaveShippingAddress(){
		driver.click(SAVE_SHIPPING_ADDRESS_CHECKBOX_LOC);
		logger.info("Selected checkbox for saving shipping address in address book");
		return this;
	}

	/***
	 * This method get the Shipping address name present at my account section
	 * 
	 * @param 
	 * @return String 
	 * 
	 */
	public String getShippingAddressNameFromShippingSection(){
		return driver.getText(SHIPPING_PROFILE_IN_SHIPPING_SECTION_LOC).trim();
	}

	/***
	 * This method validates the shipping address first name is visible
	 * 
	 * @param 
	 * @return boolean value
	 * 
	 */
	public boolean isFirstLastNameFieldVisible(){
		return driver.isElementVisible(SHIPPING_NAME_AT_CHECKOUT_PAGE_LOC);
	}

	/***
	 * This method get the sponsor info
	 * 
	 * @param 
	 * @return sponsor name
	 * 
	 */
	public String getSponsorInfo(){
		String sponsorName = driver.getText(SPONSOR_INFO_LOC);
		logger.info("Sponsor selected as "+sponsorName);
		return sponsorName;
	}

	/***
	 * This method clicks on the not your sponsor link
	 * @param
	 * @return same page object
	 */
	public StoreFrontCheckoutPage clickNotYourConsultantLink(){
		driver.click(NOT_YOUR_CONSULTANT_LINK_LOC);
		logger.info("Not your consultant link clicked");
		return this;
	}

	/***
	 * This method clicks on the not your sponsor link
	 * @param
	 * @return boolean
	 */
	public boolean isSponsorSearchBoxVisible(){
		return driver.isElementVisible(SPONSOR_SEARCH_FIELD_LOC);
	}

	/***
	 * This method validates all error fields are present or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isAllErrorFieldsPresent(){
		boolean flag = driver.isElementVisible(FIRST_NAME_ERROR_FIELD_LOC)&&
				driver.isElementVisible(LAST_NAME_ERROR_FIELD_LOC)&&
				driver.isElementVisible(EMAIL_ERROR_FIELD_LOC)&&
				driver.isElementVisible(PASSWORD_ERROR_FIELD_LOC)&&
				driver.isElementVisible(CONFIRM_PASSWORD_ERROR_FIELD_LOC); 
		return flag;
	}

	/***
	 * This method get the error message of email field 
	 * 
	 * @param 
	 * @return error message
	 * 
	 */
	public String getErrorMessageOfEmailField(){
		String errorMessage = driver.getText(EMAIL_ERROR_FIELD_LOC);
		logger.info("error message of email field : "+errorMessage);
		return errorMessage;
	}

	/***
	 * This method validates the presence of mandatory field msg for Shipping address details
	 * 
	 * @param String field
	 * @return boolean 
	 * 
	 */
	private boolean isMandatoryMsgPresentForField(String field){
		boolean isMsgPresent = driver.isElementPresent(By.xpath(String.format(mandatoryFieldErrorMsgOfAddressForNewShippingProfileLoc, field)));
		logger.info("Is Expected Mandatory field msg is present for " + field + " : " + isMsgPresent);
		return isMsgPresent;
	}

	/***
	 * This method validates the presence of mandatory field msgs for billing profile
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isErrrorMsgsForAllMandatoryFieldsOfShippingAddressArePresent(){
		return isMandatoryMsgPresentForField("firstName") &&
				isMandatoryMsgPresentForField("line1") &&
				isMandatoryMsgPresentForField("townCity") &&
				isMandatoryMsgPresentForField("region") &&
				isMandatoryMsgPresentForField("postcode") &&
				isMandatoryMsgPresentForField("phone");
	}

	/***
	 * This method check the checkbox of Use my delivery address check box for existing billing profile
	 * 
	 * @param
	 * @return store front checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage checkUseMyDeliveryAddressChkBoxForExistingBillingProfile(){
		driver.click(USE_MY_DELIVERY_ADDRESS_CHK_BOX_FOR_EXISTING_PROFILE_LOC);
		logger.info("Use My delivery address check box checked");
		driver.pauseExecutionFor(3000);
		return this;
	}

	/***
	 * This method enter the billing address details after click on add a new billing profile
	 * 
	 * @param First name,Last name, address line1, city, state, postal code, phone number
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage enterBillingAddressDetails(String firstName, String lastName, String addressLine1, String addressLine2, String city, String state, String postal, String phoneNumber){
		String completeName = firstName+" "+lastName;
		driver.type(FIRST_LAST_NAME_FOR_BILLING_ADDRESS_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE1_FOR_BILLING_ADDRESS_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(ADDRESS_LINE_2_FOR_BILLING_ADDRESS_LOC, addressLine2);
		logger.info("Entered address line 2 as "+addressLine2);
		driver.type(CITY_FOR_BILLING_ADDRESS_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_FOR_BILLING_ADDRESS_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForBillingAddress, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_FOR_BILLING_ADDRESS_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(PHONE_NUMBER_FOR_BILLING_ADDRESS_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);
		return this;
	}

	/***
	 * This method enter the user billing details
	 * 
	 * @param Card type, card number, card name, CVV
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterUserBillingDetailsAfterEditTheProfile(String cardType, String cardNumber, String nameOnCard,String CVV){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(CARD_TYPE_DD_AFTER_EDIT_PROFILE_LOC));
		logger.info("Card type dropdown clicked");
		driver.click(By.xpath(String.format(cardTypeAfterEditProfileLoc, cardType)));
		logger.info("Card type selected as "+cardType);
		driver.type(CARD_NUMBER_AFTER_EDIT_PROFILE_LOC, cardNumber);
		logger.info("Entered card number as"+cardNumber);
		driver.type(NAME_ON_CARD_AFTER_EDIT_PROFILE_LOC, nameOnCard);
		logger.info("Entered card name as"+nameOnCard);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(EXP_MONTH_DD_AFTER_EDIT_PROFILE_LOC));
		//driver.click(EXP_MONTH_DD_AFTER_EDIT_PROFILE_LOC);
		logger.info("Exp month dropdown clicked");
		driver.click(EXP_MONTH_AFTER_EDIT_PROFILE_LOC);
		logger.info("Exp month selected");
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(EXP_YEAR_DD_AFTER_EDIT_PROFILE_LOC));
		//driver.click(EXP_YEAR_DD_AFTER_EDIT_PROFILE_LOC);
		logger.info("Exp year dropdown clicked");
		driver.click(EXP_YEAR_AFTER_EDIT_PROFILE_LOC);
		logger.info("Exp year selected");
		driver.type(CVV_AFTER_EDIT_PROFILE_LOC, CVV);
		logger.info("Entered CVV as"+CVV);
		return this;
	}

	/***
	 * This method get the error message of alert 
	 * 
	 * @param 
	 * @return error message
	 * 
	 */
	public String getErrorMessageOfAlert(){
		Alert alert = driver.switchTo().alert();
		String errorMessage = alert.getText();
		//String errorMessage = driver.getText(EMAIL_ERROR_FIELD_LOC);
		logger.info("error message of alert : "+errorMessage);
		return errorMessage;
	}

	/***
	 * This method click on 'OK' button on the alert popup 
	 * 
	 * @param 
	 * @return error message
	 * 
	 */
	public StoreFrontCheckoutPage acceptAlertPopup(){
		Alert alert = driver.switchTo().alert();
		alert.accept();
		logger.info("Alert popup accepted");
		return this;
	}

	/***
	 * This method get the sponsor name from account info page
	 * 
	 * @param 
	 * @return sponsor name
	 * 
	 */
	public String getSponsorNameFromAccountInfo(){
		String sponsorName = driver.getText(SPONSOR_NAME_ACCOUNT_INFO_LOC);
		logger.info("Sponsor selected as "+sponsorName);
		return sponsorName;
	}

	/***
	 * This method verify the not your Sponsor link
	 * @param
	 * @return boolean
	 */
	public boolean isNotYourConsultantLinkPresent(){
		return driver.isElementVisible(NOT_YOUR_CONSULTAN_LINK_LOC);
	}

	/***
	 * This method get the confirmation message of consultant enrollment
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isOrderPlacedSuccessfully(){
		String confirmationText= driver.getText(CONFIRMATION_MSG_OF_PLACED_ORDER_LOC);
		if(confirmationText.toLowerCase().contains(("Thank you for your Order!").toLowerCase())){
			return true;
		}else
			return false;
	}

	/***
	 * This method clicks on the 'Use a saved card' btn on the cart
	 * Also clicks on the 'Use these payment details' btn
	 * @return
	 */
	public StoreFrontCheckoutPage clickUseSavedCardBtn(){
		driver.click(USE_SAVED_CARD_BTN_LOC);
		logger.info("clicked on the 'Use a saved card' btn on the cart");
		driver.waitForElementToBeVisible(USE_THESE_PAYMENT_DETAILS_BTN_LOC, 15);
		driver.click(USE_THESE_PAYMENT_DETAILS_BTN_LOC);
		logger.info("clicked on the 'Use these payment details' btn on the cart");
		return this;
	}

	/**
	 * This method clicks on the close pop cross btn
	 * @return
	 */
	public StoreFrontCheckoutPage closePopUp(){
		driver.click(CLOSE_BTN_OF_POPUP_LOC);
		logger.info("Clicked on the cross btn of the popup");
		return this;
	}

	/***
	 * This method get default BILLING PROFILE name
	 *    
	 * @param
	 * @return billing profile details
	 * 
	 */
	public String getDefaultBillingProfileName(){
		String profileName = driver.findElement(DEFAULT_BILLING_PROFILE_NAME_LOC).getText().trim();
		logger.info("Defaut Billing profile name : "+profileName);
		return profileName;
	}

	/***
	 * This method click on the confirm Autoship order button
	 * 
	 * @param 
	 * @return StoreFrontCheckoutPage object 
	 * 
	 */
	public StoreFrontCheckoutPage clickConfirmAutoshipOrderButton(){
		driver.click(CONFIRM_CRP_ORDER_BTN_LOC);
		driver.waitForPageLoad();
		logger.info("confirm CRP order btn clicked");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method validates the presence of Confirm CRP Order Success Message
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */
	public boolean isCRPOrderConfirmedSuccessMsgAppeared(){
		return driver.isElementVisible(CONFIRM_CRP_ORDER_MSG_LOC);
	}

	/***
	 * This method get the billing profile after order placed 
	 * 
	 * @param 
	 * @return Shipping method
	 * 
	 */
	public String getBillingProfileAfterPlacedOrder(){
		String billingProfile = driver.getText(BILLING_PROFILE_AFTER_ORDER_PLACED);
		logger.info("Billing profile at order confirmation page : "+billingProfile);
		return billingProfile;
	}

	/***
	 * This method get the only billing profile name 
	 * 
	 * @param 
	 * @return String
	 * 
	 */
	public String getBillingProfileName(){
		String billingProfile = driver.getText(BILLING_PROFILE_NAME_LOC);
		logger.info("Billing profile name: "+billingProfile);
		return billingProfile;
	}

	/***
	 * This method get the tag name in order item heading 
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */
	public boolean isTagPresentInOrderItemsHeading(String tagName){
		return driver.isElementVisible(By.xpath(String.format(orderItemsTagLoc, tagName)));
	}

	/***
	   This method get the charges acc to their label name at order confirmation page 
	 * 
	 * @param label name 
	 * @return charges
	 * 
	 */
	public String getChargesAccordingToLabelAtOrderConfirmationPage(String labelName){
		String charge=driver.findElement(By.xpath(String.format(chargesFromOrderConfirmationPage, labelName))).getText();
		logger.info(labelName+" 's value at order confirmation page is"+charge);
		return charge;
	}

	/***
	  This method get the charges acc to their label name at order review page 
	 * 
	 * @param label name 
	 * @return charges
	 * 
	 */
	public String getChargesAccordingToLabelAtOrderReviewPage(String labelName){
		String charge=driver.findElement(By.xpath(String.format(chargesFromOrderReviewPage, labelName))).getText();
		logger.info(labelName+" 's value at order review page is"+charge);
		return charge;
	}

	/***
	 * This method click on the shipping details Edit button
	 * 
	 * @param 
	 * @return StoreFrontCheckoutPage object 
	 * 
	 */
	public StoreFrontCheckoutPage clickShippingDetailsEditButton(){
		driver.click(EDIT_LINK_OF_SHIPPING_DETAILS);
		logger.info("Clicked shipping details edit button");
		return this;
	}


	/***
	 * This method get the Order Number after Successful Checkout 
	 * 
	 * @param 
	 * @return String
	 * 
	 */
	public String getOrderNumberAfterCheckout(){
		String orderNumber = driver.getText(ORDER_NUMBER_AT_CONFIRMATION_PAGE_OF_PLACED_ORDER_LOC).replaceAll("[^-?0-9]+","");
		return orderNumber;
	}

	/***
	 * This method get the shipping method after Successful Checkout 
	 * 
	 * @param 
	 * @return Shipping method
	 * 
	 */
	public String getShippingMethodAfterPlacedOrder(){
		String shippingMethod = driver.getText(SHIPPING_METHOD_AFTER_ORDER_PLACED);
		logger.info("Shipping method at order confirmation page : "+shippingMethod);
		return shippingMethod;
	}

	/***
	 * This method select the checkbox for saving billing profile for future autoship
	 * 
	 * @param 
	 * @return StoreFrontCheckoutPage object 
	 * 
	 */
	public StoreFrontCheckoutPage selectCheckboxForSavingProfileForFutureAutoship(){
		driver.click(PROFILE_FOR_FUTURE_AUTOSHIP_CHECKBOX_LOC);
		logger.info("Selected checkbox for saving Billing profile for future autoship");
		return this;
	}

	/***
	 * This method select the checkbox for policies and procedures on checkout page
	 * 
	 * @param 
	 * @return StoreFrontCheckoutPage object 
	 * 
	 */
	public StoreFrontCheckoutPage selectCheckboxForPoliciesAndProcedures(){
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(POLICIES_AND_PROCEDURES_CHECBOX_LOC));
		logger.info("Selected checkbox for policies and procedures on checkout page");
		return this;
	}

	/***
	 * This method select terms and conditions checkbox for consultant
	 * 
	 * @param
	 * @return Store front checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage selectTermsAndConditionsCheckBoxForConsulatntCRP(){
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(TERMS_AND_CONDITIONS_CHCKBOX_FOR_CONSULTANT_CRP_LOC));
		logger.info("Clicked Terms and conditions checkbox for consultant CRP checkout");
		return this;
	}

	/***
	 * This method validates the presence of appropriate mandatory field msg for billing address details
	 * 
	 * @param String field
	 * @return boolean 
	 * 
	 */
	private boolean isErrorMsgPresentAsExpectedForTheMandatoryAddressFieldForExistingBillingProfile(String field, String expectedMsg){
		boolean isElementVisible = false;
		isElementVisible = driver.isElementVisible(By.xpath(String.format(mandatoryFieldErrorMsgOfAddressForExistingBillingProfileLoc, field)));
		if(isElementVisible){
			String errorMsg = driver.getText(By.xpath(String.format(mandatoryFieldErrorMsgOfAddressForExistingBillingProfileLoc, field)));
			if(expectedMsg.contains(errorMsg)){
				logger.info("Error msg for billing address field " + field + " is present as expected");
				return true;
			}
			else{
				logger.info("Error msg for billing address field " + field + " is NOT present as expected. Expected : " + expectedMsg + ".Actual : " + errorMsg);
				return false;
			}
		}
		logger.info("Error msg for billing address field " + field + " is not appeared");
		return false;
	}

	/**
	 * This method selects the use my delivery address checkbox
	 * @return
	 */
	public StoreFrontCheckoutPage selectUseMyDeliveyAddressCheckbox(){
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(USE_MY_DELIVERY_ADDRESS_CHECKBOX_LOC));
		logger.info("Use My delivery address checkbox checked");
		return this;
	}

	/**
	 * This method click the address book btn
	 * 
	 * @param
	 * @return Store Front Checkout page obj.
	 */
	public StoreFrontCheckoutPage clickAddressBookBtn(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(ADDRESS_BOOK_BTN_LOC));
		logger.info("Address book nutton clicked");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/**
	 * This method click use this address btn from address book
	 * 
	 * @param profile number
	 * @return profile name
	 */
	public String clickUseThisAddressBtnAndReturnProfileName(String profileNumber){
		String name = driver.getText(By.xpath(String.format(profileNameFromAddressBookLoc, profileNumber)));
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(By.xpath(String.format(useThisAddressBtnInAddressBookLoc, profileNumber))));
		logger.info(profileNumber+"st is selected as shipping profile and profile name is "+name);
		return name;
	}

	/***
	 * This method clicks on the 'Use a saved card' btn on the cart
	 * @param
	 * @return Store Front Checkout page obj.
	 */
	public StoreFrontCheckoutPage clickUseSavedCardBtnOnly(){
		driver.click(USE_SAVED_CARD_BTN_LOC);
		logger.info("clicked on the 'Use a saved card' btn on the cart");
		return this;
	}

	/**
	 * This method click use this payment details from saved card
	 * 
	 * @param profile number
	 * @return profile name
	 */
	public String clickUseThesePaymentDetailsAndReturnBillingProfileName(String profileNumber){
		driver.pauseExecutionFor(2000);
		String name = driver.getText(By.xpath(String.format(profileNameFromSavedCardLoc, profileNumber)));
		driver.click(By.xpath(String.format(useThisPaymentDetailsBtnInSavedCardLoc, profileNumber)));
		logger.info(profileNumber+"st is selected as billing profile and profile name is "+name);
		return name;
	}

}
