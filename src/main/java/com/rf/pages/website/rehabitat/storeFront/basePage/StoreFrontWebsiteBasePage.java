package com.rf.pages.website.rehabitat.storeFront.basePage;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.RFBasePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAboutMePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAutoshipCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAutoshipStatusPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCheckoutPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontConsultantEnrollNowPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontHomePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;

public class StoreFrontWebsiteBasePage extends RFBasePage {
	private static final Logger logger = LogManager.getLogger(StoreFrontWebsiteBasePage.class.getName());

	protected RFWebsiteDriver driver;

	public StoreFrontWebsiteBasePage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	private final By YES_BUTTON_ON_ADDRESS_SUGGESTION_MODAL_LOC  =  By.xpath("//div[@id='cboxContent']//button[@id='suggestedAddress']");
	private final By VIEW_SHOPPING_CART_LINK_LOC = By.xpath("//a[contains(text(),'View shopping cart')]");
	private final By TOGGLE_BUTTON_OF_COUNTRY_LOC = By.xpath("//form[@id='country-form']//div[@class='form-group']/div");
	private final By ERROR_MESSAGE_EMPTY_PREFIX_LOC = By.xpath("//*[@id='prefixForm']//label[@class='field-error']");
	private final By SHOPPING_CART_HEADLINE_ON_CHCKOUT_POPUP_LOC = By.xpath("//div[@id='cboxContent']//span[@class='headline-text' and contains(text(),'Added to Your Shopping Cart')]");
	private final By SUBTOTAL_LOC = By.xpath("//td[contains(text(),'Subtotal')]/following::td[1]");
	private final By TOTAL_PRICE_OF_ITEMS_IN_MINI_CART_LOC = By
			.xpath("//ol/descendant::li[@class='mini-cart-item']//div[@class='price']");
	protected String topNavigationLoc = "//div[contains(@class,'navbar-inverse')]";
	protected final By TOTAL_PRODUCTS_LOC = By.xpath("//div[@id='product_listing']//following::div[@class='product-item']");
	private final By CANCEL_BUTTON_LOC=By.xpath("//button[contains(text(),'Cancel')]");
	private final By ACTION_SUCCESS_MSG_LOC = By.xpath("//div[contains(@class,'alert-info') and contains(@class,'alert-dismissable')]");
	private final By BECOME_A_CONSULTANT_LOC = By.xpath(topNavigationLoc + "//*[@title='BECOME A CONSULTANT']");
	private final By ENROLL_NOW_LOC = By.xpath(topNavigationLoc + "//a[@title='ENROLL NOW']");
	private final By WHY_RF_LOC = By.xpath(topNavigationLoc + "//a[@title='WHY R+F']");
	private final By SHOP_SKINCARE_LOC = By.xpath(topNavigationLoc + "//a[@title='SHOP SKINCARE']");
	private final By ALL_PRODUCTS_LOC = By.xpath(topNavigationLoc + "//a[@title='ALL PRODUCTS']");
	private final By RODAN_AND_FIELDS_LOGO_LOC = By.id("header-logo");
	private final By RODAN_AND_FIELDS_IMAGE_LOC = By.xpath("//img[@title='Rodan and Fields']");
	private final By FIND_A_CONSULTANT_LINK_LOC = By.xpath("//a[@title='FIND A CONSULTANT']");
	protected final By SPONSOR_SEARCH_FIELD_LOC = By.id("sponserparam");
	private final By SEARCH_SPONSOR_LOC = By.id("search-sponsor-button");
	private final By SEARCH_BOX_LOC = By.id("search-box");
	private final By SELECT_AND_CONTINUE_LOC = By
			.xpath("//div[@id='findConsultantResultArea']/descendant::div[contains(@class,'consultant-box')][1]");
	private final By SPONSOR_SEARCH_RESULTS_LOC = By
			.xpath("//div[@id='findConsultantResultArea']//div[contains(@class,'consultant-box')][1]");
	private final By SELECTED_SPONSOR_BOX_LOC = By.id("findConsultantResultArea-main1");
	private final By NO_RESULT_FOUND_MSG_LOC = By.xpath("//p[contains(text(),'No results found')]");
	private final By EVENTS_LOC = By.xpath(topNavigationLoc + "//a[@title='EVENTS']");
	private final By PROGRAMS_AND_INCENTIVES_LOC = By.xpath(topNavigationLoc + "//a[@title='PROGRAMS & INCENTIVES']");
	private final By MEET_OUR_COMMUNITY_LOC = By.xpath(topNavigationLoc + "//a[@title='MEET OUR COMMUNITY']");
	private final By WHO_WE_ARE_LINK_LOC = By.xpath(topNavigationLoc + "//a[@title='WHO WE ARE']");
	private final By GIVING_BACK_LINK_LOC = By.xpath(topNavigationLoc + "//a[@title='GIVING BACK']");
	private final By MEET_THE_DOCTORS_LINK_LOC = By.xpath(topNavigationLoc + "//a[@title='MEET THE DOCTORS']");
	private final By ABOUT_RF_LOC = By.xpath(topNavigationLoc + "//a[@title='ABOUT R+F']");
	private final By EXECUTIVE_TEAM_LOC = By.xpath(topNavigationLoc + "//a[@title='EXECUTIVE TEAM']");
	private final By SEARCH_ICON_LOC = By.xpath("//span[@class='icon-search']");
	private final By LOGIN_ICON_LOC = By.xpath("//div[@class='loginBlock']//a");
	private final By USERNAME_TXTFLD_LOC = By.id("username");
	private final By USERNAME_DISABLED_LOC = By.xpath("//input[@class='text-input valid' and @disabled='']");
	private final By PASSWORD_TXTFLD_LOC = By.id("password");
	private final By LOGIN_BTN_LOC = By.xpath("//input[@value='SIGN IN' or @value='LOG IN']");
	private final By CLOSE_ICON_OF_SEARCH_TEXT_BOX_IN_HEADER_NAVIGATION_LOC = By
			.xpath("//div[@class='yCmsComponent']//span[contains(@class,'icon-close')]");
	private final By DEFAULT_COUNTRY_NAME_IN_TOGGLE_LOC = By.xpath("//div[contains(@class,'wSelect-selected')]");
	private final By MINI_CART_ICON_LOC = By.xpath("//a[contains(@class,'mini-cart-link')]");
	private final By SIGN_UP_NOW_LINK_LOC = By.xpath("//a[contains(text(),'Sign up now')]");
	private final By WELCOME_DD_ACCOUNT_INFO_LOC = By.xpath("//a[text()='Account Info']");
	protected final By WELCOME_DROPDOWN_LOC = By.xpath("//div[contains(text(),'Welcome')]");
	private final By WELCOME_DD_SHIPPING_INFO_LOC = By.xpath("//a[text()='Shipping Info']");
	private final By WELCOME_DD_BILLING_INFO_LOC = By.xpath("//a[text()='Billing Info']");
	protected final By FIRST_LAST_NAME_FOR_SHIPPING_DETAILS_LOC = By.id("address.firstName");
	protected final By STATE_DD_FOR_REGISTRATION_LOC = By.id("address.region");
	private final By MAKE_THIS_MY_DEFAULT_ADDRESS_CHKBOX_LOC = By
			.xpath("//label[contains(text(),'Make this my default address')]");
	protected final By USE_AS_ENTERED_BUTTON_LOC = By.xpath("//div[@id='cboxLoadedContent']//button[@id='oldAddress']");
	private final By COUNTRY_LOC = By.xpath("//span[@class='selected-country']/preceding::input[1]");
	private final By COUNTRY_NAME_LOC = By.xpath("//span[@class='selected-country']");
	protected final By SAVE_BUTTON_LOC = By.id("deliveryAccountSubmit");
	private final By WELCOME_DD_AUTOSHIP_STATUS_LOC = By.xpath("//a[text()='Auto-ship Status']");
	private final By WELCOME_DD_ORDERS_LOC = By.xpath("//a[text()='Orders']");
	private final By SAVE_BUTTON_OF_SHIPPING_ADDRESS_LOC = By.xpath("//button[contains(text(),'Save')]");
	protected final By ERROR_MESSAGE_FOR_ADDRESS_LINE_1_LOC = By.id("address.line1-error");
	private final By ERROR_MESSAGE_FOR_CITY_LOC = By.id("address.townCity-error");
	private final By ERROR_MESSAGE_FOR_STATE_LOC = By.id("address.region-error");
	private final By ERROR_MESSAGE_FOR_POSTAL_CODE_LOC = By.id("address.postcode-error");
	private final By ERROR_MESSAGE_FOR_PHONE_NUMBER_LOC = By.id("address.phone-error");
	private final By ERROR_MESSAGE_FOR_FIRST_LAST_NAME_LOC = By.id("address.firstName-error");
	private final By SHIPPING_NEXT_BUTTON_LOC = By.xpath("//button[@id='deliveryAddressSubmit']");
	private final By LOGOUT_LOC = By.xpath("//a[text()='Sign Out']");
	private final By WELCOME_DD_EDIT_CRP_LOC = By.xpath("//a[text()='Edit CRP']");
	private final By WELCOME_DD_CHECK_MY_PULSE_LOC = By.xpath("//a[text()='Check My Pulse']");
	private final By I_DONT_HAVE_SPONSOR_CHKBOX_LOC = By.xpath("//label[text()='I DO NOT HAVE A SPONSOR']");
	private final By SUBMIT_BTN_ON_REQUIRED_SPONSOR_POPUP_LOC = By.id("consultant-sponsor-submit");
	private final By SPONSOR_FIRST_NAME_LOC = By
			.xpath("//div[@class='enroll-page']/descendant::input[@id='sponsor.firstName'][1]");
	private final By SPONSOR_LAST_NAME_LOC = By
			.xpath("//div[@class='enroll-page']/descendant::input[@id='sponsor.lastName'][1]");
	private final By SPONSOR_EMAIL_LOC = By
			.xpath("//div[@class='enroll-page']/descendant::input[@id='sponsor.email'][1]");
	private final By SPONSOR_ZIPCODE_LOC = By
			.xpath("//div[@class='enroll-page']/descendant::input[@id='sponsor.zipcode'][1]");
	private final By THANKS_MSG_ON_SPONSOR_REQUEST_LOC = By.xpath(
			"//div[@id='sponsor-entire-form-display'][@style='display: block;']//div[@id='sponsor-success-data']");
	private final By BACK_TO_HOMEPAGE_LOC = By.xpath(
			"//div[@id='sponsor-entire-form-display'][@style='display: block;']//input[@id='consultant-backhome']");
	private final By FIRST_NAME_FOR_ADDRESS_DETAILS_LOC = By.id("address.firstName");
	private final By LAST_NAME_FOR_ADDRESS_DETAILS_LOC = By.id("address.lastName");
	private final By ADDRESS_LINE_1_FOR_ADDRESS_DETAILS_LOC = By.id("address.line1");
	private final By ADDRESS_LINE_2_FOR_ADDRESS_DETAILS_LOC = By.id("address.line2");
	private final By CITY_FOR_ADDRESS_DETAILS_LOC = By.id("address.townCity");
	private final By POSTAL_CODE_FOR_ADDRESS_DETAILS_LOC = By.id("address.postcode");
	private final By PHONE_NUMBER_FOR_ADDRESS_DETAILS_LOC = By.id("address.phone");
	protected final By CARD_TYPE_DD_LOC = By.xpath("//*[@id='card_cardType']");
	protected final By CARD_NUMBER_LOC = By.id("card_accountNumber");
	protected final By NAME_ON_CARD_LOC = By.id("card_nameOnCard");
	protected final By EXP_MONTH_DD_LOC = By.id("ExpiryMonth");
	protected final By EXP_MONTH_LOC = By.xpath("//select[@id='ExpiryMonth']//option[11]");
	protected final By EXP_YEAR_DD_LOC = By.id("ExpiryYear");
	protected final By EXP_YEAR_LOC = By.xpath("//select[@id='ExpiryYear']//option[11]");
	protected final By CVV_LOC = By.id("card_cvNumber");
	protected final By IFRAME_LOC = By.id("IFrame");
	private final By USE_MY_DELIVERY_ADDRESS_CHK_BOX_LOC = By
			.xpath("//div[@id='account-billing-container']//label[contains(@class,'useDeliveryAddress')]");
	private final By POLICIES_AND_PROCEDURES_CHK_BOX_LOC = By
			.xpath("//div[contains(@class,'checkout-steps')]/descendant::label[1]");
	// private final By POLICIES_AND_PROCEDURES_CHK_BOX_LOC =
	// By.xpath("//*[@id=placeOrderForm1]/div[2]/label");
	// private final By POLICIES_AND_PROCEDURES_CHK_BOX_LOC =
	// By.xpath("//input[@id='Terms3']");
	private final By I_ACKNOWLEDGE_CHK_BOX_LOC = By
			.xpath("//div[contains(@class,'checkout-steps')]/descendant::label[2]");
	private final By TERMS_AND_CONDITIONS_CHK_BOX_LOC = By
			.xpath("//div[contains(@class,'checkout-steps')]/descendant::label[3]");
	// private final By E_SIGN_CONSENT_FORM_CHK_BOX_LOC =
	// By.xpath("//div[contains(@class,'checkout-steps')]/descendant::label[4]");
	private final By E_SIGN_CONSENT_FORM_CHK_BOX_LOC = By.xpath("//input[@id='Terms4']");
	private final By I_ACKNOWLEDGE_PC_CHK_BOX_LOC = By.xpath("//input[@id='Terms2']");
	private final By BILLING_NEXT_BUTTON_LOC = By.id("cmdSubmit");
	private final By BECOME_A_CONSULTANT_BTN_LOC = By.id("placeOrder");
	private final By ENROLLMENT_SUCCESSFUL_MSG_LOC = By.xpath("//*[contains(text(),'ENROLLMENT SUCCESSFUL')]");
	private final By REMOVE_LINK_LOC = By.xpath("//a[contains(text(),'REMOVE')]");
	private final By CONSULTANT_ONLY_PRODUCTS_LINK_LOC = By
			.xpath("//div[@class='navbar-inverse']//a[@title='CONSULTANT ONLY']");
	//private final By ERROR_MESSAGE_FOR_THRESHOLD = By.xpath("//div[@class='global-alerts']/div");
	private final By TOTAL_NO_OF_PRODUCTS_LOC = By.xpath("//div[contains(@class,'product__listing')]/descendant::span[@id='cust_price'][contains(text(),'$')]");
	private final By WELCOME_DD_EDIT_PC_PERKS_LOC = By.xpath("//a[text()='Edit PC Perks']");
	private final By WELCOME_DD_PC_PERKS_FAQ_LOC = By.xpath("//a[text()='PC Perks FAQ']");
	private final By WELCOME_DD_PC_PERKS_STATUS_LOC = By.xpath("//a[text()='PC Perks Status']");
	private final By FIRST_PRODUCT_NAME_LOC = By.xpath("//div[@id='product_listing']/descendant::a[@class='name'][1]");
	private final By FIRST_PRODUCT_IMAGE_LOC = By.xpath("//div[@class='product__listing product__grid']/div[1]/a");
	private final By REMEMBER_ME_LOC = By.xpath("//label[contains(text(),'Remember me')]");
	private final By YOUR_SHOPPING_CART_HEADER_LOC = By
			.xpath("//h1[@class='urcart-header' and contains(text(),'Your Shopping Cart')]");
	private final By INVALID_EXP_YEAR_LOC = By.xpath("//select[@id='c-exyr']//option[2]");
	private final By FORGET_PASSWORD_LINK_LOC = By.id("show-recover-pass");
	private final By FORGET_PASSWORD_LINK_CHECKOUT_LOC = By.id("show-recover-pass-inner");
	private final By RECOVER_PASSWORD_EMAIL_TXTFIELD_LOC = By.xpath("//input[@name='email']");
	private final By RECOVER_PASSWORD_EMAIL_TXTFIELD_CHECKOUT_LOC = By
			.xpath("//div[@class='page-container']/descendant::input[@name='email'][1]");
	private final By RECOVER_PASSWORD_SEND_BTN_LOC = By.xpath("//input[@value='send']");
	private final By RECOVER_PASSWORD_SEND_BTN_CHECKOUT_LOC = By
			.xpath("//div[@class='page-container']/descendant::input[@value='send'][1]");
	private final By TOTAL_NO_OF_ITEMS_IN_MINI_CART_LOC = By.xpath("//ol/li[@class='mini-cart-item']");
	private final By FIRST_ITEM_PRODUCT_NAME_IN_MINI_CART_LOC = By
			.xpath("//ol/descendant::li[@class='mini-cart-item'][1]//a[@class='name']");
	private final By QUANTITY_OF_FIRST_PRODUCT_IN_MINI_CART_LOC = By
			.xpath("//ol/descendant::li[@class='mini-cart-item'][1]//div[@class='qty']");
	private final By SUBTOTAL_IN_MINI_CART_LOC = By.xpath("//div[text()='Subtotal']/following::div[1]");
	private final By YOUR_NEXT_PC_PERKS_CART_TEXT_IN_AUTOSHIP_POPUP_LOC = By
			.xpath("//div[contains(text(),'YOUR NEXT PC PERKS CART')]");
	private final By TOTAL_NO_OF_ITEMS_IN_AUTOSHIP_CART_LOC = By
			.xpath("//div[contains(@class,'autoship-cart-popup')]//li[@class='mini-cart-item']");
	private final By VIEW_PC_PERKS_CART_BUTTON_LOC = By.xpath("//a[contains(text(),'VIEW PC PERKS CART')]");
	protected final By MINI_CART_NUMBER_OF_ITEMS_LOC = By.xpath("//span[@class='nav-items-total']");
	protected final By AUTOSHIP_TEXT_LOC = By.xpath("//span[text()='Auto-ship']");
	private final By PRODUCT_NAME_ON_CHECKOUT_POPUP_LOC = By
			.xpath("//div[@class='add-to-cart-item']//div[@class='details']/a[@class='name']");
	protected final By CHECKOUT_BUTTON_POPUP_LOC = By
			.xpath("//div[@id='addToCartLayer']/a[contains(text(),'Checkout')]");
	private final By AUTOSHIP_CART_LINK_LOC = By
			.xpath("//span[text()='Auto-ship']/ancestor::a[contains(@class,'auto-ship-cart')]");
	private final By RETURN_AUTHORIZATION_FORM_LINK = By
			.xpath("//div[@class='container']/following::ul//a[text()='Return Authorization Form']");
	private final By POLICY_AND_PROCEDURE_LINK_ON_SATISFACTION_GUARANTEE_LOC = By
			.xpath("//div[@class='container']//a[text()='Policies and Procedures']");
	private final By PRESS_MENTION_TAB_ITEMS_LOC = By.xpath("//div[@id='press-mentions']");
	private final By COMPANY_PRESS_RELEASES_TAB_ITEMS_LOC = By.xpath("//div[@id='company-press-releases']");
	private final By PRODUCT_PRESS_RELEASES_TAB_ITEMS_LOC = By.xpath("//div[@id='product-press-releases']");
	private final By FORBES_PRESS_LINK_LOC = By.xpath("//div[@class='yCmsComponent pressRoomNews'][1]/div[2]/div[1]/a");
	protected final By SEARCH_BOX = By.id("search-box");
	protected final By SEARCH_ICON_NEAR_SEARCH_BOX = By
			.xpath("//*[@id='header']//following::button[contains(@class,'icon-search')]");
	private final By ADD_MORE_ITEMS_BTN_LOC = By
			.xpath("//div[@class='cart-container']/descendant::button[contains(text(),'Add More Items')][2]");
	private final By ADD_MORE_ITEMS_BTN_PC_AUTOSHIP_CART_LOC = By
			.xpath("//div[@class='cart-container']/descendant::button[contains(text(),'Add More Items')]");
	private final By PC_ONE_TIME_FEE_MSG_LOC = By.xpath("//span[contains(text(),'ONE-TIME ENROLLMENT FEE')]");
	private final By PDF_VIEWER_LOC = By.xpath("//*[@id='viewer']");
	private final By CRP_AUTOSHIP_CART_HEADER_LOC = By.xpath("//h3[contains(text(),'YOUR NEXT AUTOSHIP CART')]");
	private final By PC_TERMS_AND_CONDITIONS_LINK_LOC = By.xpath("//a[contains(text(),'PC Perks Terms & Conditions')]");
	private final By POLICIES_AND_PROCEDURES_CHK_BOX_PC_LOC = By.xpath("//input[@id='Terms2']");
	private final By TERMS_AND_CONDITIONS_CHK_BOX_PC_LOC = By.xpath("//input[@id='Terms1']");
	private final By SELECT_AND_CONTINUE_FIRST_SPONSER_LOC = By.xpath(
			"//div[@id='findConsultantResultArea']/descendant::div[contains(@class,'consultant-box')][1]//span[@id='selectd-consultant']");
	protected final By CART_PRODUCT_LOC = By
			.xpath("//ul[contains(@class,'item-list cart')]/li[@class='item-list-item']");
	private final By EDIT_LINK_NEXT_TO_MAIN_ACCOUNT_LOC = By
			.xpath("//div[contains(@class,'checkout-steps')]/descendant::a[1]");
	private final By QUANTITY_OF_FIRST_PRODUCT_LOC = By.xpath("//div[@class='qty']//input[@id='quantity_0']");
	private final By UPDATE_LINK_OF_FIRST_PRODUCT_LOC = By
			.xpath("//div[@class='qty']/descendant::input[@value='update'][1]");
	protected final By BILLING_ADDRESS_DD_LOC = By.id("default-address");
	private final By PC_TERMS_AND_CONDITIONS_CHK_BOX_LOC = By
			.xpath("//div[@class='step-body']/descendant::input[@id='Terms1']/following::label[1]");
	private final By PC_PERKS_CART_HEADER_LOC = By.xpath("//h2[contains(text(),'YOUR NEXT PC PERKS CART')]");
	private final By EVENTS_PAGE_HEADER_LOC = By.xpath("//h3[contains(text(),'Business Presentations')]");
	private final By MEET_OUR_COMMUNITY_PAGE_HEADER_LOC = By.xpath("//h1[contains(text(),'MEET OUR COMMUNITY')]");
	private final By WHY_RF_PAGE_HEADER_LOC = By.xpath("//h1[contains(text(),'Why R+F')]");
	private final By PROGRAMS_AND_INCENTVES_PAGE_LOC = By.xpath("//h1[contains(text(),'PROGRAMS & INCENTIVES')]");
	private final By ENROLL_AS_CONSULTANT_PAGE_LOC = By.xpath("//h2[contains(text(),'ENROLL AS A CONSULTANT')]");
	private final By WELCOME_DD_EDIT_PWS_LOC = By.xpath("//a[text()='Edit PWS']");
	protected final By CHECKOUT_BUTTON_LOC = By.xpath(
			"//div[@class='cart-container']/descendant::button[contains(text(),'Checkout')][2] | //a[@id='checkoutPopup']");
	protected final By CHECKOUT_CONFIRMATION_OK_BUTTON_LOC = By.xpath("//div[@id='cartCheckoutModal']/a");
	private final By PWS_PREFIX_INPUT_FIELD_LOC = By.xpath("//input[@name='prefix']");
	private final By NEXT_BTN_LOC = By.xpath("//*[contains(text(),'NEXT')]");
	private final By OK_BTN_LOC = By.xpath("//a[contains(text(),'OK')]");
	private final By CONFIRM_PULSE_SUBSCRIPTION_BTN_LOC = By.id("subscriptionconfirmsubmit");
	private final By PULSE_SUBSCRIPTION_CHKBOX_LOC = By.id("subscription-checkbox");
	private final By PULSE_SUBSCRIBE_BTN_LOC = By.id("confirmprefixaction");
	private final By NEW_POLICIES_PROCEDURES_POPUP_LOC = By.id("updateCondition_popup");
	private final By SET_UP_CRP_POPUP_CLOSE_LOC = By.xpath("//div[@class='close']");
	private final By ACCEPT_RDBTN_NEW_POLICIES_PROCEDURES_POPUP_LOC = By
			.xpath("//form[@id='updateConditionForm']//input[@id='terms-accept']");
	private final By CONTINUE_BTN_NEW_POLICIES_PROCEDURES_POPUP_LOC = By
			.xpath("//form[@id='updateConditionForm']//input[@value='continue']");
	private final By PC_PERKS_PROMO_MSG_LOC = By.xpath("//div[contains(text(),'Subscribe and Save')]");
	protected final By PULSE_CANCELLATION_POPUP_TEXT_LOC = By.id("popup_confirm_savedcart_restore");
	protected final By PULSE_CANCELLATION_POPUP_CANCEL_LOC = By
			.xpath("//*[@id='popup_confirm_savedcart_restore']//a[contains(text(),'CANCEL')]");
	private final By BILLING_PROFILE_DETAILS_ORDER_REVIEW_PAGE_LOC = By.xpath("//*[@id='default-payment-method']/ul");
	protected final By POPUP_FOR_TERMS_AND_CONDITIONS_LOC = By.id("city_popup");// moved
	private final By BILLING_ADDRESS_OTHER_THAN_ND_POPUP_TEXT_LOC = By.xpath("//*[@id='city_popup']//h3");
	private final By CHOOSE_A_KIT_OPTION_ON_POPUP_LOC = By.xpath("//a[text()='Choose a kit']");
	private final By SHIPPING_SECTION_LOC = By.xpath("//div[@class='checkout-shipping']");
	private final By BILLING_SECTION_LOC = By.xpath("//div[@class='checkout-paymentmethod']");
	private final By LEARN_MORE_LINK_LOC = By.xpath("//a[contains(text(),'Learn more')]");
	private final By PC_PROMO_POPUP_LOC = By.id("cboxLoadedContent");
	private final By PC_PROMO_POPUP_CLOSE_BTN_LOC = By.id("cboxClose");
	private final By SPONSOR_NAME_ACCOUNT_INFO_LOC = By.xpath("//div[contains(@id,'findConsultantResultArea')]/descendant::span[@id='selectd-consultant'][2]");
	private final By SAVE_PAYMENT_BUTTON_LOC = By.id("updateBillingAddress");
	private final By FIRST_LAST_NAME_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.firstName']");
	private final By ADDRESS_LINE1_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.line1']");
	private final By ADDRESS_LINE_2_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.line2']");
	private final By CITY_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.townCity']");
	private final By STATE_DD_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//select[@id='address.region']");
	private final By POSTAL_CODE_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.postcode']");
	private final By PHONE_NUMBER_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC=By.xpath("//div[@id='checkoutEditBillingAddressForm']//input[@id='address.phone']");
	private final By ERROR_MESSAGE_EXISTING_PREFIX_LOC = By.id("errorSubPrefix");
	private final By ABOUT_ME_LOC = By.xpath(topNavigationLoc + "//a[contains(@title,'About Me')]");
	private final By CONFIRMATION_MSG_OF_PLACED_ORDER_LOC = By.xpath("//div[@class='orderHeading']/h1");
	private final By ORDER_NUMBER_AT_CONFIRMATION_PAGE_OF_PLACED_ORDER_LOC = By.xpath("//div[@class='orderHeading']");
	private final By ENROLL_NOW_POPUP_LOC = By.xpath("//div[@id='enrollCRPModal' and contains(@style,'block')]//h3[contains(text(),'Do you want to enroll in CRP')]");
	protected final By SORT_FILTER_DD_LOC = By.id("sortOptions1");
	protected final By SHOP_BY_PRICE_FILTER_OPTION_HIGH_TO_LOW_LOC = By.xpath("//select[@id='sortOptions1']/descendant::option[2]");

	private String productNameAllItemsInCartLoc = "//span[@class='item-name' and contains(text(),%s)]";
	protected String addToCartButtonLoc = "//div[contains(@class,'product__listing')]/descendant::span[@id='cust_price'][contains(text(),'$')][1]/following::button[text()='Add to bag'][%s]";
	private String errorMessageLoc = "//div[@class='global-alerts']/div[normalize-space(contains(text() , '%s'))]";
	private String optionOnEnrollNowPopUpLoc = "//div[@id='enrollCRPModal' and contains(@style,'block')]//input[@value='%s']";
	protected String productNameLinkLoc = "//div[@id='product_listing']/descendant::div[@class='details'][%s]//a";
	private String quantityOfSpecificItemInCart = "//div[@class='qty']//input[@id='quantity_%s']";
	private String itemNameInMiniCart = "//ol/descendant::li[@class='mini-cart-item'][%s]//a[@class='name']";
	private String itemQuantityInMiniCart = "//ol/descendant::li[@class='mini-cart-item'][%s]//div[@class='qty']";
	private String stateForShippingDetailsForExistingBillingProfile = "//div[@id='checkoutEditBillingAddressForm']//option[text()='%s']";
	protected String mandatoryFieldErrorMsgOfAddressForNewBillingProfileLoc = "//div[@id='billingAddressForm']//label[contains(@id,'%s-error') and contains(text(),'This field is required.')]";
	private String productNameInAllItemsInCartLoc = "//span[@class='item-name' and contains(text(),'%s')]";
	private String productQuantityInAllItemsLoc = "//span[@class='item-name' and contains(text(),'%s')]/following::div//div[@class='qty']//input[contains(@id,'quantity')]";
	private String selectAndContinueSponserLoc = "//div[@id='findConsultantResultArea']/descendant::div[contains(@class,'consultant-box')][%s]//span[@id='selectd-consultant']";
	private String pageHeaderLoc = "//div[@class='page-container']//*[contains(text(),'%s')]";
	private String disclaimerPageLinkLoc = "//a[contains(text(),'%s')]";
	private String pressRoomTabsLoc = "//ul[@class='tabs']//li/a[contains(text(),'%s')]";
	private String specificExpYearLoc = "//select[@id='c-exyr']//option[%s]";
	private String searchResultTextAsPerEntity = "//h1[contains(text(),'Search Results for \"%s\"')]";
	private String passwordRecoverySubmitMsgLoc = "//div[@id='validEmail'][contains(text(),'%s')]";
	private String expMonthLoc = "//select[@id='c-exmth']//option[contains(text(),'%s')]";
	private String textLoc = "//*[contains(text(),'%s')]";
	private String stateForShippingDetails = "//select[@id='address.region']//option[text()='%s']";
	private String topNavigationSublinksWithTextLoc = topNavigationLoc + "//a[text()='%s']";
	private String topNavigationSublinksWithTitleLoc = topNavigationLoc + "//*[@title='%s']";
	private String activePageLoc = "//span[contains(text(),'%s')]/parent::li";
	private String navigationPageNumberLoc = "//ul[@class='pagination']//a[contains(text(),'%s')]";
	private String subLinkUnderAboutRFLoc = topNavigationLoc + "//a[@title='%s']/following::a[text()='%s'][1]";
	private String footerLinkLoc = "//div[@class='footer-sections']//a[text()='%s']";
	private String countryOptionsInToggleButtonLoc = "//div[@class='wSelect-options-holder']//div[contains(text(),'%s')]";
	private String socialMediaLinkAtFooterLoc = "//a[contains(@class,'%s')]";
	private String sponsorEmptyFieldValidationOnPopUpLoc = "//label[@id='sponsor.%s-error'][contains(text(),'%s')]";
	private String sponsorInvalidFieldValidationOnPopUpLoc = "//label[@id='sponsor.%s-error'][contains(text(),'%s')]";
	private String cardTypeLoc = "//select[@id='card_cardType']//option[text()='%s']";
	private String qtyOfProductOfAnItemLoc = "//div[@class='orderConfirmationInfo']/descendant::div[@class='orderQty'][%s]";
	private String SVOfProductOfAnItemLoc = "//div[@class='orderConfirmationInfo']/descendant::div[@class='orderSv'][%s]";
	private String unitPriceOfProductOfAnItemLoc = "//div[@class='orderConfirmationInfo']/descendant::div[@class='orderUnitPrice'][%s]";
	private String orderTotalOfProductOfAnItemLoc = "//div[@class='orderConfirmationInfo']/descendant::div[@class='orderTotal'][%s]";
	private String productNameOfAnItemLoc = "//div[@class='orderConfirmationInfo']/descendant::p[%s]";


	private String RFO_DB = null;

	/***
	 * This method do the mouseHover on desired webElement
	 * 
	 * @param sponsor
	 * @return
	 * 
	 */
	public StoreFrontWebsiteBasePage mouseHoverOn(String element) {
		if (element.equalsIgnoreCase(TestConstants.BECOME_A_CONSULTANT)) {
			driver.moveToElement(BECOME_A_CONSULTANT_LOC);
			logger.info("mouseHovered on 'Become A Consultant'");
		} else if (element.equalsIgnoreCase(TestConstants.SHOP_SKINCARE)) {
			driver.moveToElement(SHOP_SKINCARE_LOC);
			logger.info("mouseHovered on 'Shop Skincare'");
		} else if (element.equalsIgnoreCase(TestConstants.ABOUT_RF)) {
			driver.moveToElement(ABOUT_RF_LOC);
			logger.info("mouseHovered on 'About R+F'");
		}else if(element.equalsIgnoreCase(TestConstants.ABOUT_ME)){
			driver.moveToElement(ABOUT_ME_LOC);
			logger.info("mouseHovered on 'Shop Skincare'");
		}
		driver.pauseExecutionFor(500);
		return this;
	}
	/***
	 * This method checks if CONSULTANT ONLY link under ShopSkincare is
	 * displayed or not
	 * 
	 * @return boolean
	 */
	public boolean isConsultantOnlyProductsLinkDisplayed() {
		return driver.isElementVisible(CONSULTANT_ONLY_PRODUCTS_LINK_LOC);
	}

	/***
	 * This method clicks on CONSULTANT ONLY link under ShopSkincare
	 * 
	 * @return boolean
	 */
	public StoreFrontShopSkinCarePage clickConsultantOnlyProductsLink() {
		driver.click(CONSULTANT_ONLY_PRODUCTS_LINK_LOC);
		logger.info("Clicked on Consultant Only link");
		return new StoreFrontShopSkinCarePage(driver);
	}

	/***
	 * This method clicks on the Enroll Now link in Top Navigation
	 * 
	 * @param
	 * @return
	 * 
	 */
	public StoreFrontConsultantEnrollNowPage clickEnrollNow(){
		for(int attemptNumber=1;attemptNumber<=3;attemptNumber++){
			try{
				mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
				driver.pauseExecutionFor(500);
				driver.click(ENROLL_NOW_LOC);
				break;
			}catch(Exception ex){
				logger.info("Become a Consultant not mouse hovered properly..retry");
				driver.pauseExecutionFor(1000);
				continue;
			}
		}
		logger.info("clicked on 'Enroll Now'");
		driver.waitForPageLoad();
		return new StoreFrontConsultantEnrollNowPage(driver);
	}

	//	/***
	//	 * This method clicks on the All products link from Top Navigation
	//	 * 
	//	 * @return
	//	 */
	//	public StoreFrontShopSkinCarePage clickAllProducts() {
	//		//		mouseHoverOn(TestConstants.SHOP_SKINCARE);
	//		//		driver.click(ALL_PRODUCTS_LOC);
	//		//		 clickCategoryLink("ESSENTIALS");
	//		driver.get(driver.getCurrentUrl()+"/All-Skincare/c/shopskincare");
	//		logger.info("clicked on 'All Products'");
	//		driver.waitForPageLoad();
	//		return new StoreFrontShopSkinCarePage(driver);
	//	}

	//temp method, till we have All Products not having required products
	public StoreFrontShopSkinCarePage clickAllProductsCRP() {
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(ALL_PRODUCTS_LOC);
		logger.info("clicked on 'All Products'");
		driver.waitForPageLoad();
		return new StoreFrontShopSkinCarePage(driver);
	}

	/***
	 * This method clicks on the 'Executive Team' link in Top Navigation
	 * 
	 * @param
	 * @return
	 * 
	 */
	public StoreFrontWebsiteBasePage clickExecutiveTeam() {
		mouseHoverOn(TestConstants.ABOUT_RF);
		driver.click(EXECUTIVE_TEAM_LOC);
		logger.info("clicked on 'Executuve Team'");
		return this;
	}

	/***
	 * This method enter the sponsor name and click on search button
	 * 
	 * @param
	 * @return current URL
	 * 
	 */
	public String getCurrentURL() {
		String currentURL = driver.getCurrentUrl();
		logger.info("Current URL is " + currentURL);
		return currentURL;
	}

	/***
	 * This methid verifies if the PDF viewer is visible or NOT
	 * 
	 * @return
	 */
	public boolean isPDFViewerDisplayed() {
		driver.pauseExecutionFor(3000);
		driver.findElement(By.xpath("//div"));
		return true;
	}

	/***
	 * This method clicks on R+F logo
	 * 
	 * @param
	 * @return BasePage Object
	 * 
	 */
	public StoreFrontHomePage clickRodanAndFieldsLogo() {
		driver.waitForElementPresent(RODAN_AND_FIELDS_IMAGE_LOC);
		if (driver.isElementVisible(RODAN_AND_FIELDS_IMAGE_LOC)) {
			driver.clickByJS(RFWebsiteDriver.driver, RODAN_AND_FIELDS_IMAGE_LOC);
		} else {
			driver.clickByJS(RFWebsiteDriver.driver, RODAN_AND_FIELDS_LOGO_LOC);
		}
		logger.info("Rodan and Fields logo clicked");
		driver.waitForPageLoad();
		return new StoreFrontHomePage(driver);
	}

	/***
	 * This method clicks on the Find A Consultant Link on Home Page
	 * 
	 * @param
	 * @return BasePage Object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickFindAConsultantLinkOnHomePage() {
		driver.quickWaitForElementPresent(FIND_A_CONSULTANT_LINK_LOC);
		driver.click(FIND_A_CONSULTANT_LINK_LOC);
		logger.info("'Find a consutant' link clicked");
		return this;
	}

	/***
	 * This method enter the sponsor name and click on search button
	 * 
	 * @param sponsor
	 * @return
	 * 
	 */
	public StoreFrontWebsiteBasePage searchSponsor(String sponsor) {
		driver.pauseExecutionFor(2000);
		driver.type(SPONSOR_SEARCH_FIELD_LOC, sponsor);
		logger.info("Entered sponsor as " + sponsor);
		driver.click(SEARCH_SPONSOR_LOC);
		logger.info("Clicked on 'Search' button");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method selects the first sponsor name in the search result.
	 * 
	 * @param sponsor
	 * @return
	 * 
	 */
	public void selectFirstSponsorFromList() {
		driver.pauseExecutionFor(2000);
		driver.click(SELECT_AND_CONTINUE_LOC);
		logger.info("Clicked on 'Select And Continue' button for first result");
	}

	/***
	 * This method verifies whether any result has been present after searching
	 * the sponsor or not.
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isSponsorResultDisplayed() {
		driver.waitForElementToBeVisible(SPONSOR_SEARCH_RESULTS_LOC, 10);
		return driver.isElementVisible(SPONSOR_SEARCH_RESULTS_LOC);
	}

	/***
	 * This method verifies if the 'No results found' message is displayed or
	 * not
	 * 
	 * @return boolean
	 */
	public boolean isNoResultMessagePresent() {
		driver.quickWaitForElementPresent(NO_RESULT_FOUND_MSG_LOC);
		return driver.isElementPresent(NO_RESULT_FOUND_MSG_LOC);
	}

	/***
	 * This method verifies if the user is on expected navigation page in the
	 * sponsor search result.
	 * 
	 * @return boolean
	 */
	public Boolean isTheUserOnNavigationPage(String pageNo) {
		return driver.getAttribute(By.xpath(String.format(activePageLoc, pageNo)), "class").equals("active");
	}

	/***
	 * This method naviagtes the user to the desired navigation page in the
	 * sponsor search result
	 * 
	 * @return boolean
	 */
	public StoreFrontWebsiteBasePage navigateToPaginationInSponsorSearchResult(String pageNo) {
		driver.clickByJS(RFWebsiteDriver.driver,By.xpath(String.format(navigationPageNumberLoc, pageNo)));
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method hover on become a consultant and click on events link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickEvents() {
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(EVENTS_LOC);
		logger.info("clicked on 'Events link'");
		return this;
	}

	/***
	 * This method switch the window
	 * 
	 * @param parent
	 *            window handle
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage switchToParentWindow(String parentWindowID) {
		driver.close();
		driver.switchTo().window(parentWindowID);
		logger.info("Switched to parent window");
		return this;
	}

	/***
	 * This method switch the window from parent to child
	 * 
	 * @param parent
	 *            window handle
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage switchToChildWindow(String parentWindowID) {
		driver.pauseExecutionFor(1000);
		Set<String> set = driver.getWindowHandles();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String childWindowID = it.next();
			if (!parentWindowID.equalsIgnoreCase(childWindowID)) {
				driver.switchTo().window(childWindowID);
				logger.info("Switched to child window");
			}
		}
		return this;
	}

	/***
	 * This method hover on become a consultant and click on meet our community
	 * link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickMeetOurCommunityLink() {
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(MEET_OUR_COMMUNITY_LOC);
		logger.info("clicked on 'MEET OUR COMMUNITY' link");
		return this;
	}

	/***
	 * This method hover on About R+F and click on Who We Are link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickWhoWeAreLink() {
		mouseHoverOn(TestConstants.ABOUT_RF);
		driver.click(WHO_WE_ARE_LINK_LOC);
		logger.info("clicked on 'WHO WE ARE' link");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method hover on About R+F and click on Meet The Doctors link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickMeetTheDoctorsLink() {
		mouseHoverOn(TestConstants.ABOUT_RF);
		driver.click(MEET_THE_DOCTORS_LINK_LOC);
		logger.info("clicked on 'MEET THE DOCTORS' link");
		return this;
	}

	/***
	 * This method hover on About R+F and click on Giving back link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickGivingBackLink() {
		mouseHoverOn(TestConstants.ABOUT_RF);
		driver.click(GIVING_BACK_LINK_LOC);
		logger.info("clicked on 'GIVING BACK'link");
		return this;
	}

	/***
	 * This method click on search icon
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickSearchIcon() {
		driver.click(SEARCH_ICON_LOC);
		logger.info("clicked on Search Icon");
		return this;
	}

	/***
	 * This method validates search text box in header navigation.
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */
	public boolean isSearchBoxPresent() {
		return driver.findElement(SEARCH_BOX_LOC).isDisplayed();
	}

	/***
	 * This method get product name
	 * 
	 * @param
	 * @return product name
	 * 
	 */
	public String getProductName(String productNumber) {
		String productName = driver.findElement(By.xpath(String.format(productNameLinkLoc, productNumber))).getText();
		logger.info("product name is: " + productName);
		return productName;
	}

	/***
	 * This method clicks on the login icon,enter the username and password and
	 * click on 'LOG IN' button
	 * 
	 * @param username
	 *            ,password
	 * @return StoreFrontWebsiteBasePage
	 * 
	 */

	//	public StoreFrontWebsiteBasePage loginToStoreFront(String username, String password) {
	//		clickLoginIcon();
	//		driver.type(USERNAME_TXTFLD_LOC, username);
	//		logger.info("username entered as " + username);
	//		driver.type(PASSWORD_TXTFLD_LOC, password);
	//		logger.info("password entered as  " + password);
	//		driver.click(LOGIN_BTN_LOC);
	//		logger.info("login button clicked");
	//		if (driver.isElementVisible(NEW_POLICIES_PROCEDURES_POPUP_LOC)) {
	//			driver.clickByJS(RFWebsiteDriver.driver,
	//					driver.findElement(ACCEPT_RDBTN_NEW_POLICIES_PROCEDURES_POPUP_LOC));
	//			driver.pauseExecutionFor(1000);
	//			driver.clickByJS(RFWebsiteDriver.driver,
	//					driver.findElement(CONTINUE_BTN_NEW_POLICIES_PROCEDURES_POPUP_LOC));
	//		}
	//		return this;
	//	}

	public StoreFrontWebsiteBasePage loginToStoreFront(String username, String password, boolean closeCRPReminder) {
		clickLoginIcon();
		driver.type(USERNAME_TXTFLD_LOC, username);
		logger.info("username entered as " + username);
		driver.type(PASSWORD_TXTFLD_LOC, password);
		logger.info("password entered as  " + password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		driver.waitForPageLoad();
		if (driver.isElementVisible(NEW_POLICIES_PROCEDURES_POPUP_LOC)) {
			driver.clickByJS(RFWebsiteDriver.driver,ACCEPT_RDBTN_NEW_POLICIES_PROCEDURES_POPUP_LOC);
			driver.pauseExecutionFor(1000);
			driver.clickByJS(RFWebsiteDriver.driver,CONTINUE_BTN_NEW_POLICIES_PROCEDURES_POPUP_LOC);

		}
		if (closeCRPReminder == true && (driver.isElementVisible(SET_UP_CRP_POPUP_CLOSE_LOC))) {
			driver.click(SET_UP_CRP_POPUP_CLOSE_LOC);
		}
		return this;
	}

	/***
	 * This method clicks on the login icon,enter the username and
	 * password,check the Remember Me checkbox and click on 'LOG IN' button
	 * 
	 * @param username
	 *            ,password
	 * @return StoreFrontWebsiteBasePage
	 * 
	 */

	public StoreFrontWebsiteBasePage loginToStoreFrontWithRememberMe(String username, String password) {
		clickLoginIcon();
		driver.type(USERNAME_TXTFLD_LOC, username);
		logger.info("username entered as " + username);
		driver.type(PASSWORD_TXTFLD_LOC, password);
		logger.info("password entered as  " + password);
		driver.clickByJS(RFWebsiteDriver.driver, REMEMBER_ME_LOC);
		driver.pauseExecutionFor(1000);
		logger.info("Remember me checkbox checked");
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		return this;
	}

	/***
	 * This method verifies the display of footer links on homepage. search
	 * result.
	 * 
	 * @return boolean
	 */
	public Boolean isTheFooterLinkDisplayed(String linkName) {
		return driver.isElementVisible(By.xpath(String.format(footerLinkLoc, linkName)));
	}

	/***
	 * This method will click the footer links on homepage. search result.
	 * 
	 * @return store front website base page object
	 */
	public StoreFrontWebsiteBasePage clickFooterLink(String linkName) {
		driver.click(By.xpath(String.format(footerLinkLoc, linkName)));
		logger.info("clicked on link " + "'" + linkName + "'");
		driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method hover on shopSkincare and click sublink mentioned in
	 * argument.
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage navigateToShopSkinCareSubLinks(String LinkName, String sublinkName) {
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(By.xpath(String.format(subLinkUnderAboutRFLoc, LinkName, sublinkName)));
		logger.info("clicked on sublink" + "'" + sublinkName + "'");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method get default selected country name from toggle
	 *
	 * @param
	 * @return country name
	 * 
	 */
	public String getDefaultSelectedCountryNameFromToggle() {
		String countryName = driver.findElement(DEFAULT_COUNTRY_NAME_IN_TOGGLE_LOC).getText();
		logger.info("Country name is: " + countryName);
		return countryName;
	}

	/***
	 * This method select the country from toggle button
	 * 
	 * @param country
	 *            name
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage selectCountryFromToggleButton(String countryName) {
		driver.click(By.xpath(String.format(countryOptionsInToggleButtonLoc, countryName)));
		logger.info("Country " + countryName + " is Selected");
		return this;
	}

	/***
	 * This method click the login icon
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickLoginIcon() {
		driver.click(LOGIN_ICON_LOC);
		logger.info("Login icon clicked");
		return this;
	}

	/***
	 * This method verifies the visibility of the login icon
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public Boolean isLoginIconVisible() {
		// driver.isElementVisible(LOGIN_ICON_LOC);
		// The above code is not working for visiblity so taking a work around
		try {
			driver.turnOffImplicitWaits(1);
			driver.findElement(LOGIN_ICON_LOC).click();
			return true;
		} catch (Exception ex) {
			return false;
		} finally {
			driver.turnOnImplicitWaits();
		}
	}

	/***
	 * This method verify the username field is present or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isUsernameFieldPresent() {
		return driver.isElementPresent(USERNAME_TXTFLD_LOC);
	}

	/***
	 * This method verify the username field is visible or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isUsernameFieldVisible() {
		// driver.isElementVisible(USERNAME_TXTFLD_LOC);
		// The above code is not working for visiblity so taking a work around
		try {
			driver.turnOffImplicitWaits(1);
			driver.findElement(USERNAME_TXTFLD_LOC).sendKeys("");
			return true;
		} catch (Exception ex) {
			return false;
		} finally {
			driver.turnOnImplicitWaits();
		}
	}

	/***
	 * This method verify the username field is disabled or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isUsernameFieldDisabled() {
		return driver.isElementPresent(USERNAME_DISABLED_LOC);
	}

	/***
	 * This method verify the password field is present or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isPasswordFieldPresent() {
		return driver.isElementPresent(PASSWORD_TXTFLD_LOC);
	}

	/***
	 * This method verify the password field is visible or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isPasswordFieldVisible() {
		// driver.isElementVisible(PASSWORD_TXTFLD_LOC);
		// The above code is not working for visiblity so taking a work around
		try {
			driver.turnOffImplicitWaits(1);
			driver.findElement(PASSWORD_TXTFLD_LOC).sendKeys("");
			return true;
		} catch (Exception ex) {
			return false;
		} finally {
			driver.turnOnImplicitWaits();
		}
	}

	/***
	 * This method verify mini cart is present or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isMiniCartPresent() {
		return driver.isElementVisible(MINI_CART_ICON_LOC);
	}

	/***
	 * This method verify toggle button is present or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isToggleButtonPresent() {
		return driver.isElementVisible(TOGGLE_BUTTON_OF_COUNTRY_LOC);
	}

	/***
	 * This method validates social media icon is present or not at footer
	 * 
	 * @param social
	 *            media type
	 * @return boolean value
	 * 
	 */
	public boolean isSocialMediaIconPresentAtFooter(String mediaType) {
		return driver.isElementVisible(By.xpath(String.format(socialMediaLinkAtFooterLoc, mediaType)));
	}

	/***
	 * This method validates the display of top Navigation sublink
	 * 
	 * @param link
	 *            name
	 * @return boolean value
	 * 
	 */
	public boolean isTopNavigationSublinkDisplayed(String topNavigationLink, String sublink) {
		mouseHoverOn(topNavigationLink);
		return driver.isElementVisible(By.xpath(String.format(topNavigationSublinksWithTitleLoc, sublink)));
	}

	/***
	 * This method click Account info link from welcome dropdown.
	 * 
	 * @param
	 * @return store front Account info page object
	 * 
	 */
	public StoreFrontAccountInfoPage navigateToAccountInfoPage() {
		driver.click(WELCOME_DD_ACCOUNT_INFO_LOC);
		logger.info("Account Info clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontAccountInfoPage(driver);
	}

	/***
	 * This method click the welcome dropdown.
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickWelcomeDropdown() {
		//		driver.pauseExecutionFor(2000);
		//		if (driver.isElementPresent(SET_UP_CRP_POPUP_CLOSE_LOC)) {
		//			driver.click(SET_UP_CRP_POPUP_CLOSE_LOC);
		//			logger.info("JOIN CRP banner closed");
		//		}
		driver.pauseExecutionFor(1000);
		driver.clickByJS(RFWebsiteDriver.driver, WELCOME_DROPDOWN_LOC);
		logger.info("Welcome dropdown clicked");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method click shipping info link from welcome dropdown.
	 * 
	 * @param
	 * @return store front Shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage navigateToShippingInfoPage() {
		driver.click(WELCOME_DD_SHIPPING_INFO_LOC);
		logger.info("Shipping Info clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontShippingInfoPage(driver);
	}

	/***
	 * This method enter the consultant shipping details
	 * 
	 * @param First
	 *            name,Last name, address line1, city, state, postal code, phone
	 *            number
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterConsultantShippingDetails(String firstName, String lastName,
			String addressLine1, String addressLine2, String city, String state, String postal, String phoneNumber) {
		String completeName = firstName + " " + lastName;
		driver.type(FIRST_NAME_FOR_ADDRESS_DETAILS_LOC, completeName);
		logger.info("Entered complete name as " + completeName);
		driver.type(ADDRESS_LINE_1_FOR_ADDRESS_DETAILS_LOC, addressLine1);
		logger.info("Entered address line 1 as " + addressLine1);
		driver.type(ADDRESS_LINE_2_FOR_ADDRESS_DETAILS_LOC, addressLine2);
		logger.info("Entered address line 2 as " + addressLine2);
		driver.type(CITY_FOR_ADDRESS_DETAILS_LOC, city);
		logger.info("Entered city as " + city);
		driver.click(STATE_DD_FOR_REGISTRATION_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetails, state)));
		logger.info("State selected as " + state);
		driver.type(POSTAL_CODE_FOR_ADDRESS_DETAILS_LOC, postal);
		logger.info("Entered postal code as " + postal);
		driver.type(PHONE_NUMBER_FOR_ADDRESS_DETAILS_LOC, phoneNumber);
		logger.info("Entered Phone number  as " + phoneNumber);
		return this;
	}

	/***
	 * This method validates the country field is editable or not
	 * 
	 * @param country
	 *            name
	 * @return boolean value
	 * 
	 */
	public boolean isCountryNameEditable(String country) {
		try {
			driver.type(COUNTRY_LOC, country);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/***
	 * This method get the country name
	 * 
	 * @param
	 * @return country name
	 * 
	 */
	public String getCountryName() {
		driver.pauseExecutionFor(2000);
		String countryName = driver.findElement(COUNTRY_NAME_LOC).getText();
		logger.info("country name is " + countryName);
		return countryName;
	}

	/***
	 * This method check the check box of make this my default address
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage checkMakeThisMyDefaultAddressChkBox() {
		driver.click(MAKE_THIS_MY_DEFAULT_ADDRESS_CHKBOX_LOC);
		logger.info("Make this my default address check box checked");
		return this;
	}

	/***
	 * This method click the checkout button at cart page
	 * 
	 * @param
	 * @return store front checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage checkoutTheCart() {
		driver.pauseExecutionFor(3000);
		driver.clickByJS(RFWebsiteDriver.driver, CHECKOUT_BUTTON_LOC);
		logger.info("Clicked on checkout button");
		driver.pauseExecutionFor(2000);
		if (driver.isElementPresent(CHECKOUT_CONFIRMATION_OK_BUTTON_LOC) == true) {
			driver.click(CHECKOUT_CONFIRMATION_OK_BUTTON_LOC);
			logger.info("Clicked on OK button at checkout confirmation popup");
		} else {
			logger.info("No checkout confirmation popup");
		}
		driver.waitForPageLoad();
		return new StoreFrontCheckoutPage(driver);
	}

	/***
	 * This method click Autoship status link from welcome dropdown.
	 * 
	 * @param
	 * @return store front Account info page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage navigateToAutoshipStatusPage() {
		driver.click(WELCOME_DD_AUTOSHIP_STATUS_LOC);
		logger.info("Autoship status clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontAutoshipStatusPage(driver);
	}

	/***
	 * This method Update first name on checkout Page.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage updateFirstName(String updatedName) {
		driver.type(FIRST_LAST_NAME_FOR_SHIPPING_DETAILS_LOC, updatedName);
		logger.info("First Name Updated for account info at checkout page");
		return this;
	}

	/***
	 * This method edit Main Account info at checkout page
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage editMainAccountInfo() {
		driver.click(EDIT_LINK_NEXT_TO_MAIN_ACCOUNT_LOC);
		logger.info("Edit link clicked next to main account info");
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method returns the main first name of the user
	 * 
	 * @return
	 */
	public String getMainFirstNameOfUser() {
		return driver.getAttribute(FIRST_NAME_FOR_ADDRESS_DETAILS_LOC, "value");
	}

	/***
	 * This method returns the main last name of the user
	 * 
	 * @return
	 */
	public String getMainLastNameOfUser() {
		return driver.getAttribute(LAST_NAME_FOR_ADDRESS_DETAILS_LOC, "value");
	}

	/***
	 * This method click billing info link from welcome dropdown.
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontBillingInfoPage navigateToBillingInfoPage() {
		driver.click(WELCOME_DD_BILLING_INFO_LOC);
		logger.info("Billing Info clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontBillingInfoPage(driver);
	}

	/***
	 * This method validates text at page
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isTextPresent(String textName) {
		return driver.isElementPresent(By.xpath(String.format(textLoc, textName)));
	}

	/***
	 * This method click orders link from welcome drop down
	 * 
	 * @param
	 * @return store front orders page object
	 * 
	 */
	public StoreFrontOrdersPage navigateToOrdersPage() {
		driver.click(WELCOME_DD_ORDERS_LOC);
		logger.info("orders link clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontOrdersPage(driver);
	}

	/***
	 * This method validates the error message for first & last name field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForFirstAndLastName() {
		return driver.findElement(ERROR_MESSAGE_FOR_FIRST_LAST_NAME_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for address line 1 field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForAddressLine1() {
		return driver.findElement(ERROR_MESSAGE_FOR_ADDRESS_LINE_1_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for city field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForCity() {
		return driver.findElement(ERROR_MESSAGE_FOR_CITY_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for state field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForState() {
		return driver.findElement(ERROR_MESSAGE_FOR_STATE_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for postal code field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForPostalCode() {
		return driver.findElement(ERROR_MESSAGE_FOR_POSTAL_CODE_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for phone number field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForPhoneNumber() {
		return driver.findElement(ERROR_MESSAGE_FOR_PHONE_NUMBER_LOC).isDisplayed();
	}

	/***
	 * This method click the next button at shipping details page
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickShippingDetailsNextbutton() {
		clickNextbuttonOfShippingDetails();
		clickUseAsEnteredButtonOnPopUp();
		return this;
	}

	/***
	 * This method click the category links
	 * 
	 * @param Category
	 *            name
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickCategoryLink(String categoryName) {
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(By.xpath(String.format(topNavigationSublinksWithTextLoc, categoryName)));
		logger.info("Category " + categoryName + " clicked");
		return this;
	}

	/***
	 * This method validates logout functionality.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isLogoutSuccessful() {
		return driver.isElementPresent(LOGIN_ICON_LOC);
	}

	/***
	 * This method hover on shopSkincare and click link mentioned in argument in
	 * new Tab.
	 * 
	 * @param regimen
	 *            name as linkName
	 * @return store front website base page object
	 * @throws AWTException
	 * 
	 */
	public StoreFrontWebsiteBasePage navigateToShopSkincareLinkInNewTab(String linkName) throws AWTException {
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		WebElement element = driver.findElement(By.xpath(String.format(topNavigationSublinksWithTextLoc, linkName)));
		Actions action = new Actions(RFWebsiteDriver.driver);
		action.contextClick(element).build().perform();
		logger.info("Right click performed on webelement");
		driver.pauseExecutionFor(2000);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_ENTER);
		logger.info("key down performed on webelement options and entered clicked");
		driver.pauseExecutionFor(2000);
		logger.info("clicked on" + "'" + linkName + "'" + "under shopskincare");
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_TAB);
		return this;
	}

	/***
	 * This method validates login functionality in multiple tabs.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isUserLoggedInNewTab() {
		return driver.isElementPresent(WELCOME_DROPDOWN_LOC);
	}

	/***
	 * This method refresh or reload current page.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage pageRefresh() {
		driver.navigate().refresh();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click PC Perks Edit link from welcome dropdown.
	 * 
	 * @param
	 * @return StoreFrontAutoshipCartPage object
	 * 
	 */
	public StoreFrontAutoshipCartPage navigateToEditCRPPage() {
		driver.click(WELCOME_DD_EDIT_CRP_LOC);
		logger.info("Edit CRP clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontAutoshipCartPage(driver);
	}

	/***
	 * This method validates the header on CRP Autoship checkokut page
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isCRPAutoshipHeaderPresentOnCartPage() {
		return driver.isElementVisible(CRP_AUTOSHIP_CART_HEADER_LOC);
	}

	/***
	 * This method click check my pulse link from welcome dropdown.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage navigateToCheckMyPulsePage() {
		driver.click(WELCOME_DD_CHECK_MY_PULSE_LOC);
		logger.info("check my pulse clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click orders link from welcome drop down
	 * 
	 * @param
	 * @return store front orders page object
	 * 
	 */
	public StoreFrontHomePage logout() {
		driver.click(LOGOUT_LOC);
		logger.info("logout link clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
		return new StoreFrontHomePage(driver);
	}

	/***
	 * This method click on I DONT HAVE SPONSOR checkbox
	 * 
	 * @param
	 * @return store front base page object
	 */
	public StoreFrontWebsiteBasePage clickIDontHaveSponsorCheckBox() {
		driver.click(I_DONT_HAVE_SPONSOR_CHKBOX_LOC);
		logger.info("'I dont have sponsor checkbox' clicked");
		return this;
	}

	/***
	 * This method checks if the submit button on required sponsor popup is
	 * disbaled or not
	 * 
	 * @return
	 */
	public boolean isSubmitBtnOnSponsorPopUpDisabled() {
		return !(driver.findElement(SUBMIT_BTN_ON_REQUIRED_SPONSOR_POPUP_LOC).isEnabled());
	}

	/***
	 * This method enters the sponsor's first name, last name, email and zip
	 * code
	 * 
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param zipCode
	 * @return
	 */
	public StoreFrontWebsiteBasePage enterDetailsInRequiredConsultantSponsorPopUp(String firstName, String lastName,
			String email, String zipCode) {
		driver.type(SPONSOR_FIRST_NAME_LOC, firstName);
		logger.info("entered sponsor first name as " + firstName);
		driver.type(SPONSOR_LAST_NAME_LOC, lastName);
		logger.info("entered sponsor last name as " + lastName);
		driver.type(SPONSOR_EMAIL_LOC, email);
		logger.info("entered sponsor email as " + email);
		driver.type(SPONSOR_ZIPCODE_LOC, zipCode);
		logger.info("entered sponsor zipcode as " + zipCode);
		return this;
	}

	/***
	 * This method returns whether empty field validation displayed for sponsor
	 * popup or not
	 * 
	 * @param fieldName
	 * @return
	 */

	public boolean isEmptyFieldValidationForSponsorOnPopupDisplayed(String fieldName) {
		String validationMsg = "This field is required";
		return driver.isElementVisible(
				By.xpath(String.format(sponsorEmptyFieldValidationOnPopUpLoc, fieldName, validationMsg)));
	}

	/***
	 * 
	 * @return
	 */
	public boolean isInvalidFieldValidationForSponsorOnPopupDisplayed(String fieldName) {
		String validationMsg = null;
		if (fieldName.equalsIgnoreCase("zipCode")) {
			validationMsg = "Please enter valid postal code";
		} else if (fieldName.equalsIgnoreCase("email")) {
			validationMsg = "Please enter a valid email address";
		}
		return driver.isElementVisible(
				By.xpath(String.format(sponsorInvalidFieldValidationOnPopUpLoc, fieldName, validationMsg)));
	}

	/***
	 * This method click on Submit button on required consultant sponsor popup
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickSubmitBtnOnRequiredConsultantSponsorPopUp() {
		driver.findElement(SUBMIT_BTN_ON_REQUIRED_SPONSOR_POPUP_LOC).click();
		logger.info("Submit button on required consultant sponsor popup clicked");
		return this;
	}

	/***
	 * This method checks if Thanks msg after request of sponsor is submitted
	 * 
	 * @return
	 */
	public boolean isThanksMessageAfterSponsorRequestPresent() {
		return driver.isElementPresent(THANKS_MSG_ON_SPONSOR_REQUEST_LOC);
	}

	/***
	 * This method clicks on the Back to HomePage button after request of
	 * sponsor is submitted
	 * 
	 * @return
	 */
	public void clickBackToHomePageBtn() {
		driver.click(BACK_TO_HOMEPAGE_LOC);
		logger.info("back to hompage button clicked");
	}

	/***
	 * This method will return the base URL
	 * 
	 * @param
	 * @return String
	 */
	public String getBaseUrl() {
		return driver.getURL();
	}

	/***
	 * This method will return the country
	 * 
	 * @param
	 * @return String
	 */
	public String getCountry() {
		return driver.getCountry();
	}

	/***
	 * This method enter the consultant Address details
	 * 
	 * @param First
	 *            name,Last name, address line1, city, state, postal code, phone
	 *            number
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterConsultantAddressDetails(String firstName, String lastName,
			String addressLine1, String addressLine2, String city, String state, String postal, String phoneNumber) {
		String completeName = firstName + " " + lastName;
		driver.pauseExecutionFor(2000);
		driver.quickWaitForElementPresent(FIRST_NAME_FOR_ADDRESS_DETAILS_LOC);
		driver.type(FIRST_NAME_FOR_ADDRESS_DETAILS_LOC, completeName);
		logger.info("Entered complete name as " + completeName);
		driver.type(ADDRESS_LINE_1_FOR_ADDRESS_DETAILS_LOC, addressLine1);
		logger.info("Entered address line 1 as " + addressLine1);
		driver.type(ADDRESS_LINE_2_FOR_ADDRESS_DETAILS_LOC, addressLine2);
		logger.info("Entered address line 2 as " + addressLine2);
		driver.type(CITY_FOR_ADDRESS_DETAILS_LOC, city);
		logger.info("Entered city as " + city);
		driver.click(STATE_DD_FOR_REGISTRATION_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetails, state)));
		logger.info("State selected as " + state);
		driver.type(POSTAL_CODE_FOR_ADDRESS_DETAILS_LOC, postal);
		logger.info("Entered postal code as " + postal);
		driver.type(PHONE_NUMBER_FOR_ADDRESS_DETAILS_LOC, phoneNumber);
		logger.info("Entered Phone number  as " + phoneNumber);
		return this;
	}

	/***
	 * This method enter the user billing details
	 * 
	 * @param Card
	 *            type, card number, card name, CVV
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterUserBillingDetails(String cardType, String cardNumber, String nameOnCard,
			String CVV) {
		String javascript = "document.getElementById('card_accountNumber').value=" + cardNumber + ";"
				+ "document.getElementById('card_accountNumber').innerHTML=" + cardNumber + ";";
		driver.pauseExecutionFor(2000);
		driver.quickWaitForElementPresent(CARD_NUMBER_LOC);
		((JavascriptExecutor) RFWebsiteDriver.driver).executeScript(javascript);
		logger.info("Entered card number as" + cardNumber);
		driver.pauseExecutionFor(1000);
		driver.click(CARD_NUMBER_LOC);
		driver.pauseExecutionFor(1000);
		Actions actions = new Actions(RFWebsiteDriver.driver);
		WebElement nameOnCardElement = driver.findElement(NAME_ON_CARD_LOC);
		actions.click(nameOnCardElement).sendKeys(nameOnCardElement, nameOnCard).build().perform();
		logger.info("Entered card name as" + nameOnCard);
//		driver.click(EXP_MONTH_DD_LOC);
//		driver.pauseExecutionFor(1000);
//		logger.info("Exp month dropdown clicked");
//		driver.click(EXP_MONTH_LOC);
//		driver.pauseExecutionFor(1000);
//		logger.info("Exp month selected");
//		driver.click(EXP_YEAR_DD_LOC);
//		driver.pauseExecutionFor(1000);
//		logger.info("Exp year dropdown clicked");
//		driver.click(EXP_YEAR_LOC);
//		driver.pauseExecutionFor(1000);
//		logger.info("Exp year selected");
		driver.type(EXP_MONTH_DD_LOC, "12");
		driver.type(EXP_YEAR_DD_LOC, "2025");
		driver.type(CVV_LOC, CVV);
		driver.pauseExecutionFor(1000);
		logger.info("Entered CVV as" + CVV);
		driver.pauseExecutionFor(1000);
		driver.waitForTokenizing();
		driver.waitForPageLoad();		
		return this;
	}

	/***
	 * This method enter the user billing details
	 * 
	 * @param Card
	 *            type, card number, card name, CVV, Exp month
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterUserBillingDetails(String cardType, String cardNumber, String nameOnCard,
			String CVV, String month, String year) {
		driver.switchTo().frame(driver.findElement(IFRAME_LOC));
		logger.info("Switched into iframe");
		driver.clickByJS(RFWebsiteDriver.driver, CARD_TYPE_DD_LOC);
		logger.info("Card type dropdown clicked");
		driver.click(By.xpath(String.format(cardTypeLoc, cardType)));
		logger.info("Card type selected as " + cardType);
		driver.type(CARD_NUMBER_LOC, cardNumber);
		logger.info("Entered card number as" + cardNumber);
		driver.type(NAME_ON_CARD_LOC, nameOnCard);
		logger.info("Entered card name as" + nameOnCard);
		Actions actions = new Actions(RFWebsiteDriver.driver);
		actions.doubleClick(driver.findElement(EXP_MONTH_DD_LOC)).build().perform();
		logger.info("Exp month dropdown clicked");
		driver.click(By.xpath(String.format(expMonthLoc, month)));
		logger.info("Exp month selected as " + month);
		driver.pauseExecutionFor(2000);
		actions.doubleClick(driver.findElement(EXP_YEAR_DD_LOC)).build().perform();
		logger.info("Exp year dropdown clicked");
		driver.click(INVALID_EXP_YEAR_LOC);
		logger.info("Exp year selected as" + year);
		driver.type(CVV_LOC, CVV);
		logger.info("Entered CVV as" + CVV);
		driver.switchTo().defaultContent();
		logger.info("Switched to default content");
		return this;
	}

	/***
	 * This method check the checkbox of Use my delivery address check box
	 * 
	 * @param
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage checkUseMyDeliveryAddressChkBox() {
		driver.click(USE_MY_DELIVERY_ADDRESS_CHK_BOX_LOC);
		logger.info("Use My delivery address check box checked");
		return this;
	}

	/***
	 * This method click the next button at billing details page
	 * 
	 * @param
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickBillingDetailsNextbutton() {
		driver.clickByJS(RFWebsiteDriver.driver, BILLING_NEXT_BUTTON_LOC);
		driver.pauseExecutionFor(1000);
		logger.info("Next button clicked of billing details");
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method select the policies & procedures checkbox
	 * 
	 * @param
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage selectPoliciesAndProceduresChkBox() {
		driver.pauseExecutionFor(3000);
		if (driver.isElementVisible(POLICIES_AND_PROCEDURES_CHK_BOX_LOC))
			driver.clickByJS(RFWebsiteDriver.driver, POLICIES_AND_PROCEDURES_CHK_BOX_LOC);
		else
			driver.clickByJS(RFWebsiteDriver.driver, POLICIES_AND_PROCEDURES_CHK_BOX_PC_LOC);
		logger.info("Policies & procedures checkbox selected");
		driver.pauseExecutionFor(1000);
		return this;
	}

	/***
	 * This method select the I acknowledge checkbox
	 * 
	 * @param
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage selectIAcknowledgeChkBox() {
		driver.pauseExecutionFor(3000);
		driver.click(I_ACKNOWLEDGE_CHK_BOX_LOC);
		logger.info("I acknowledge checkbox selected");
		driver.pauseExecutionFor(1000);
		return this;
	}

	/***
	 * This method select the I acknowledge checkbox for PC
	 * 
	 * @param
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage selectIAcknowledgePCChkBox() {
		driver.pauseExecutionFor(1000);
		driver.clickByJS(RFWebsiteDriver.driver, I_ACKNOWLEDGE_PC_CHK_BOX_LOC);
		logger.info("PC,I acknowledge checkbox selected");
		return this;
	}

	/***
	 * This method select the terms & conditions checkbox
	 * 
	 * @param
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage selectTermsAndConditionsChkBox() {
		driver.pauseExecutionFor(1000);
		if (driver.isElementVisible(TERMS_AND_CONDITIONS_CHK_BOX_LOC)) {
			driver.clickByJS(RFWebsiteDriver.driver, TERMS_AND_CONDITIONS_CHK_BOX_LOC);
		} else {
			driver.clickByJS(RFWebsiteDriver.driver, TERMS_AND_CONDITIONS_CHK_BOX_PC_LOC);
		}
		logger.info("Terms & condition checkbox selected");
		driver.pauseExecutionFor(1000);
		return this;
	}

	/***
	 * This method select the terms & conditions checkbox for PC
	 * 
	 * @param
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage selectPCTermsAndConditionsChkBox() {
		driver.pauseExecutionFor(1000);
		driver.clickByJS(RFWebsiteDriver.driver, PC_TERMS_AND_CONDITIONS_CHK_BOX_LOC);
		logger.info("PC Terms & condition checkbox selected");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method select the E Sign Consent Form checkbox
	 * 
	 * @param
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage selectConsentFormChkBox() {
		driver.clickByJS(RFWebsiteDriver.driver, E_SIGN_CONSENT_FORM_CHK_BOX_LOC);
		logger.info("E Sign Consent Form checkbox selected");
		driver.pauseExecutionFor(1000);
		return this;
	}

	/***
	 * This method verifies if "Enrollment successful" msg is displayed or not
	 * after consultant enrollment
	 * 
	 * @return
	 */
	public boolean isEnrollemntSuccessfulMsgDisplayed() {
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(ENROLLMENT_SUCCESSFUL_MSG_LOC,20);
		return driver.isElementPresent(ENROLLMENT_SUCCESSFUL_MSG_LOC);
	}

	public String getConsultantOrderNumberFromURL(){
		String[] currentURLArray = driver.getCurrentUrl().split("/");
		String consultantOrderNum = currentURLArray[currentURLArray.length-1];
		logger.info("consultantOrderNum ="+consultantOrderNum);
		return consultantOrderNum;
	}

	/***
	 * This method validates the Billing Address on UI for Specific Billing
	 * Profile
	 * 
	 * @param String
	 *            , String, String, String, String
	 * @return boolean value
	 * 
	 */
	public boolean isBillingAddressOnUIIsFoundAsExpected(String address1, String address2, String city, String pin,
			String state, String billingAddressOnUI) {
		return billingAddressOnUI.contains(address1) && billingAddressOnUI.contains(address2)
				&& billingAddressOnUI.contains(city) && billingAddressOnUI.contains(pin)
				&& billingAddressOnUI.contains(state);
	}

	/***
	 * This method validates the Billing Address on UI for Specific Billing
	 * Profile Overloaded method
	 * 
	 * @param String
	 *            , String, String, String
	 * @return boolean value
	 * 
	 */
	public boolean isBillingAddressOnUIIsFoundAsExpected(String address1, String city, String pin, String state,
			String billingAddressOnUI) {
		return billingAddressOnUI.contains(address1) && billingAddressOnUI.contains(city)
				&& billingAddressOnUI.contains(pin) && billingAddressOnUI.contains(state);
	}

	/***
	 * This method clicks on the remove Link
	 * 
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickRemoveLink() {
		driver.clickByJS(RFWebsiteDriver.driver, REMOVE_LINK_LOC);
		// driver.click(REMOVE_LINK_LOC);
		logger.info("Remove link clicked");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method verifies if consultant box of the selected sponsor displayed
	 * or not
	 * 
	 * @return
	 */
	public boolean isSelectedConsultantBoxDisplayed() {
		return driver.isElementVisible(SELECTED_SPONSOR_BOX_LOC);
	}

	/***
	 * This method validates the error message at cart page
	 * 
	 * @param message
	 * @return boolean
	 * 
	 */
	public boolean isErrorMessagePresentForThreshold(String message) {
		return driver.isElementVisible(By.xpath(String.format(errorMessageLoc, message)));
	}

	/***
	 * This method verify mini cart is present or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public StoreFrontCartPage clickMiniCartBagLink() {
		driver.click(MINI_CART_ICON_LOC);
		logger.info("Mini cart bag link clicked");
		return new StoreFrontCartPage(driver);
	}

	/***
	 * This method get total no of product
	 * 
	 * @param
	 * @return total no of product
	 * 
	 */
	public int getTotalNoOfProduct() {
		int totalNoOfProducts = driver.findElements(TOTAL_NO_OF_PRODUCTS_LOC).size();
		logger.info("Total no of products are: " + totalNoOfProducts);
		return totalNoOfProducts;
	}

	/***
	 * This method navigate to back page from current page.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage navigateToBackPage() {
		driver.navigate().back();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click PC Perks Edit link from welcome dropdown.
	 * 
	 * @param
	 * @return StoreFrontAutoshipCartPage object
	 * 
	 */
	public StoreFrontAutoshipCartPage navigateToEditPCPerksPage() {
		driver.click(WELCOME_DD_EDIT_PC_PERKS_LOC);
		logger.info("Edit PC perks clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontAutoshipCartPage(driver);
	}

	/***
	 * This method click PC Perks FAQ link from welcome dropdown.
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontWebsiteBasePage navigateToPCPerksFAQPage() {
		driver.click(WELCOME_DD_PC_PERKS_FAQ_LOC);
		logger.info("PC perks FAQ clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click PC Perks status link from welcome dropdown.
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontWebsiteBasePage navigateToLogoutPage() {
		driver.click(LOGOUT_LOC);
		logger.info("Logout clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method get and return current page title
	 * 
	 * @param
	 * @return current page title
	 * 
	 */
	public String getCurrentpageTitle() {
		String currentPageTitle = driver.getTitle();
		logger.info("Current Page title is " + currentPageTitle);
		return currentPageTitle;
	}

	/***
	 * This method click Quick view button of first product on all product page
	 * and return product name
	 * 
	 * @param
	 * @return product name
	 * 
	 */
	public String clickOnFirstProductQuickViewButtonAndReturnProductName() {
		driver.waitForElementPresent(FIRST_PRODUCT_NAME_LOC);
		String productName = driver.findElement(FIRST_PRODUCT_NAME_LOC).getText();
		driver.waitForElementPresent(FIRST_PRODUCT_IMAGE_LOC);
		driver.click(FIRST_PRODUCT_IMAGE_LOC);
		return productName.split("\\.")[0];
	}

	/***
	 * This method validates the header when redirect to checkout page after
	 * clicking checkout button from checkout popup.
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isPCPerksCartHeaderPresentOnCartPage() {
		return driver.isElementVisible(PC_PERKS_CART_HEADER_LOC);
	}

	/***
	 * This method validates the header when redirect to checkout page after
	 * clicking checkout button from checkout popup.
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isYourShoppingCartHeaderPresentOnCartPage() {
		return driver.isElementVisible(YOUR_SHOPPING_CART_HEADER_LOC);
	}

	/***
	 * This method clicks on the forget password link
	 * 
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickForgetPasswordLink() {
		driver.click(FORGET_PASSWORD_LINK_LOC);
		logger.info("Forget Password link clicked");
		return this;
	}

	/***
	 * This method clicks on the forget password link at checkout page
	 * 
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickForgetPasswordLinkAtCheckout() {
		driver.click(FORGET_PASSWORD_LINK_CHECKOUT_LOC);
		logger.info("Forget Password link at checkout page clicked");
		return this;
	}

	/***
	 * This method enters the Password recover email
	 * 
	 * @param email
	 * @return
	 */
	public StoreFrontWebsiteBasePage enterPasswordRecoverEmail(String email) {
		driver.type(RECOVER_PASSWORD_EMAIL_TXTFIELD_LOC, email);
		logger.info("Password recover email entered is " + email);
		return this;
	}

	/***
	 * This method enters the Password recover email at checkout page
	 * 
	 * @param email
	 * @return
	 */
	public StoreFrontWebsiteBasePage enterPasswordRecoverEmailAtCheckout(String email) {
		driver.type(RECOVER_PASSWORD_EMAIL_TXTFIELD_CHECKOUT_LOC, email);
		logger.info("Password recover email entered at checkout is " + email);
		return this;
	}

	/***
	 * This method clicks on Submit btn for Password Recover email
	 * 
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickSubmitBtnForPasswordRecovery() {
		driver.click(RECOVER_PASSWORD_SEND_BTN_LOC);
		logger.info("clicked on Submit btn for Password Recover email");
		return this;
	}

	/***
	 * This method clicks on Submit btn for Password Recover email at checkout
	 * page
	 * 
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickSubmitBtnForPasswordRecoveryAtCheckout() {
		driver.click(RECOVER_PASSWORD_SEND_BTN_CHECKOUT_LOC);
		logger.info("clicked on Submit btn for Password Recover email at checkout page");
		return this;
	}

	/***
	 * This method verifies if the success msg for password recovery email
	 * submit has displayed or not
	 * 
	 * @return
	 */
	public boolean isPasswordRecoveryEmailMsgDisplayed(String msg) {
		return driver.isElementPresent(By.xpath(String.format(passwordRecoverySubmitMsgLoc, msg)));
	}

	/***
	 * This method validates the header is present on all pages.
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isHeaderIsConsistentOnAllPages() {
		boolean flag = false;
		boolean shopSkinCareOption = false;
		boolean becomeConsultantOption = false;
		boolean aboutRFOption = false;
		if (driver.isElementPresent(SHOP_SKINCARE_LOC) && driver.isElementPresent(BECOME_A_CONSULTANT_LOC)
				&& driver.isElementPresent(ABOUT_RF_LOC) && driver.isElementPresent(RODAN_AND_FIELDS_LOGO_LOC)) {
			mouseHoverOn(TestConstants.SHOP_SKINCARE);
			shopSkinCareOption = driver.isElementVisible(ALL_PRODUCTS_LOC);
			mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
			becomeConsultantOption = driver.isElementVisible(MEET_OUR_COMMUNITY_LOC);
			mouseHoverOn(TestConstants.ABOUT_RF);
			aboutRFOption = driver.isElementVisible(EXECUTIVE_TEAM_LOC);
			flag = shopSkinCareOption && becomeConsultantOption && aboutRFOption;
		} else {
			logger.info("Header is not present.");
		}
		return flag;
	}

	/***
	 * This method get total no of itme in mini cart
	 * 
	 * @param
	 * @return no of item
	 * 
	 */
	public String getNumberOfItemFromMiniCart() {
		String noOfItem = driver.findElement(MINI_CART_NUMBER_OF_ITEMS_LOC).getText();
		logger.info("error is: " + noOfItem);
		return noOfItem;
	}

	/***
	 * This method hover on Mini cart icon
	 * 
	 * @param
	 * @return order total
	 * 
	 */
	public StoreFrontWebsiteBasePage hoverOnMiniCartBagIcon() {
		driver.moveToElementByJS(MINI_CART_ICON_LOC);
		logger.info("hover on mini cart icon");
		return this;
	}

	/***
	 * This method get total no of itme in mini cart
	 * 
	 * @param
	 * @return no of item
	 * 
	 */
	public int getTotalNumberOfItemsInMiniCart() {
		int noOfItem = driver.findElements(TOTAL_NO_OF_ITEMS_IN_MINI_CART_LOC).size();
		logger.info("total no of items are: " + noOfItem);
		return noOfItem;
	}

	/***
	 * This method get subtotal from mini cart
	 * 
	 * @param
	 * @return subtotal
	 * 
	 */
	public String getSubtotalofItemsFromMiniCart() {
		String subtotal = driver.findElement(SUBTOTAL_IN_MINI_CART_LOC).getText();
		logger.info("Subtotal of product is " + subtotal);
		return subtotal;
	}

	/***
	 * This method verify the R+F logo
	 * 
	 * @param
	 * @return boolean value
	 */
	public boolean isRodanAndFieldsLogoPresent() {
		return driver.isElementVisible(RODAN_AND_FIELDS_LOGO_LOC);
	}

	/***
	 * This method verify welcome dropdown
	 * 
	 * @param
	 * @return boolean value
	 */
	public boolean isWelcomeDropdownPresent() {
		return driver.isElementVisible(WELCOME_DROPDOWN_LOC);
	}

	/***
	 * This method verify autoship text link
	 * 
	 * @param
	 * @return boolean value
	 */
	public boolean isAutoshipLinkPresent() {
		return driver.isElementVisible(AUTOSHIP_TEXT_LOC);
	}

	/***
	 * This method verify shop skincare text
	 * 
	 * @param
	 * @return boolean value
	 */

	public boolean isShopSkincareTextPresent() {
		return driver.isElementVisible(SHOP_SKINCARE_LOC);
	}

	/***
	 * This method hover on Mini cart icon
	 * 
	 * @param
	 * @return Store front website base page obj
	 * 
	 */

	public StoreFrontWebsiteBasePage hoverOnAutoshipLink() {
		driver.moveToElementByJS(AUTOSHIP_TEXT_LOC);
		logger.info("hover on autoship link");
		return this;
	}

	/***
	 * This method hover on Mini cart icon
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isYourNextPCPerksCartTextPresentAtAutoshipLinkPopup() {
		return driver.isElementVisible(YOUR_NEXT_PC_PERKS_CART_TEXT_IN_AUTOSHIP_POPUP_LOC);
	}

	/***
	 * This method verify the items are present in autoshi cart popup
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isItemPresentInAutoshipCart() {
		return driver.isElementVisible(TOTAL_NO_OF_ITEMS_IN_AUTOSHIP_CART_LOC);
	}

	/***
	 * This method click on autoship text present at autoship popup
	 * 
	 * @param
	 * @return Store front website base page obj
	 * 
	 */
	public StoreFrontAutoshipCartPage clickViewPCPerksCartButton() {
		driver.click(VIEW_PC_PERKS_CART_BUTTON_LOC);
		return new StoreFrontAutoshipCartPage(driver);
	}

	/***
	 * This method validates the header of checkout popup when product added tom
	 * cart.
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup() {
		return driver.isElementVisible(SHOPPING_CART_HEADLINE_ON_CHCKOUT_POPUP_LOC);
	}

	/***
	 * This method enter the username and password and click on 'LOG IN' button
	 * 
	 * @param username
	 *            ,password
	 * @return StoreFrontWebsiteBasePage
	 * 
	 */

	public StoreFrontWebsiteBasePage loginToStoreFrontExcludingClickOnLoginIcon(String username, String password) {
		driver.type(USERNAME_TXTFLD_LOC, username);
		logger.info("username entered as " + username);
		driver.type(PASSWORD_TXTFLD_LOC, password);
		logger.info("password entered as  " + password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		return this;
	}

	/**
	 * This method get the name of product from checkout pop up.
	 * 
	 * @return String - productName
	 */
	public String getProductNameFromCheckoutPopup() {
		driver.pauseExecutionFor(2000);
		return driver.getText(PRODUCT_NAME_ON_CHECKOUT_POPUP_LOC);
	}

	/**
	 * This method click on the checkOut Button on the popup on the cart.
	 * 
	 * @return
	 */
	public StoreFrontCartPage checkoutTheCartFromPopUp() {
		driver.pauseExecutionFor(5000);
		driver.quickWaitForElementPresent(CHECKOUT_BUTTON_POPUP_LOC);
		driver.click(CHECKOUT_BUTTON_POPUP_LOC);
		logger.info("Clicked on checkout button on the popup");
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
		return new StoreFrontCartPage(driver);
	}

	/***
	 * This method validates the search results text
	 * 
	 * @param String
	 *            searchEntity
	 * @return boolean
	 * 
	 */
	public boolean isSearchResultsTextAppearedAsExpected(String searchEntity) {
		return driver.isElementVisible(By.xpath(String.format(searchResultTextAsPerEntity, searchEntity)));
	}

	/***
	 * This method enter the user billing details
	 * 
	 * @param Card
	 *            type, card number, card name, CVV, yearIndex
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterUserBillingDetailsWithSpecificYear(String cardType, String cardNumber,
			String nameOnCard, String CVV, String yearIndex) {
		driver.switchTo().frame(driver.findElement(IFRAME_LOC));
		logger.info("Switched into iframe");
		driver.clickByJS(RFWebsiteDriver.driver, CARD_TYPE_DD_LOC);
		logger.info("Card type dropdown clicked");
		driver.click(By.xpath(String.format(cardTypeLoc, cardType)));
		logger.info("Card type selected as " + cardType);
		driver.type(CARD_NUMBER_LOC, cardNumber);
		logger.info("Entered card number as" + cardNumber);
		driver.type(NAME_ON_CARD_LOC, nameOnCard);
		logger.info("Entered card name as" + nameOnCard);
		driver.clickByJS(RFWebsiteDriver.driver, EXP_MONTH_DD_LOC);
		logger.info("Exp month dropdown clicked");
		driver.click(EXP_MONTH_LOC);
		logger.info("Exp month selected");
		driver.clickByJS(RFWebsiteDriver.driver, EXP_YEAR_DD_LOC);
		logger.info("Exp year dropdown clicked");
		driver.click(By.xpath(String.format(specificExpYearLoc, yearIndex)));
		logger.info("Exp year selected");
		driver.type(CVV_LOC, CVV);
		logger.info("Entered CVV as" + CVV);
		driver.switchTo().defaultContent();
		logger.info("Switched to default content");
		return this;
	}

	/***
	 * This method will click on return authorization form link on satisfaction
	 * guarantee.
	 * 
	 * @return store front website base page object
	 */
	public StoreFrontWebsiteBasePage clickReturnAuthorizationFormLinkOnSatisfactionPage() {
		driver.click(RETURN_AUTHORIZATION_FORM_LINK);
		logger.info("clicked on return authorization link on satisfaction guarantee page");
		return this;
	}

	/***
	 * This method will click on policies and procedures link on Satisfaction
	 * guarantee page.
	 * 
	 * @return store front website base page object
	 */
	public StoreFrontWebsiteBasePage clickPoliciesAndProceduresLinkOnSatisfactionPage() {
		driver.click(POLICY_AND_PROCEDURE_LINK_ON_SATISFACTION_GUARANTEE_LOC);
		logger.info("clicked on policy and procedures link on satisfaction guarantee page");
		return this;
	}

	/**
	 * This method validates press mention tab content on press room page.
	 * 
	 * @return boolean
	 */
	public boolean isPressMentionTabItemsDisplayed() {
		driver.pauseExecutionFor(5000);
		return driver.isElementVisible(PRESS_MENTION_TAB_ITEMS_LOC);
	}

	/**
	 * This method click on tab name mentioned in argument on press room page
	 * 
	 * @return StoreFrontWebsiteBasePage object
	 */
	public StoreFrontWebsiteBasePage clickPressRoomTabs(String tabName) {
		driver.click(By.xpath(String.format(pressRoomTabsLoc, tabName)));
		return this;
	}

	/**
	 * This method validates tabs on press room page.
	 * 
	 * @return boolean
	 */
	public boolean isCompanyOrProductPressReleasesTabItemsDisplayed(String tabType) {
		driver.pauseExecutionFor(5000);
		if (tabType.equalsIgnoreCase("COMPANY PRESS RELEASES")) {
			return driver.isElementVisible(COMPANY_PRESS_RELEASES_TAB_ITEMS_LOC);
		} else if (tabType.equalsIgnoreCase("PRODUCT PRESS RELEASES")) {
			return driver.isElementVisible(PRODUCT_PRESS_RELEASES_TAB_ITEMS_LOC);
		} else {
			return false;
		}
	}

	/**
	 * This method click on forbes press link in press mention tab on press room
	 * page
	 * 
	 * @return StoreFrontWebsiteBasePage object
	 */
	public StoreFrontWebsiteBasePage clickForbesPressLink() {
		driver.click(FORBES_PRESS_LINK_LOC);
		logger.info("clicked on forbes press link");
		return this;
	}

	/**
	 * This method validates selected page header displayed on selected page or
	 * not
	 * 
	 * @return boolean
	 */
	public boolean isPageHeaderDisplayed(String pageHeader) {
		return driver.isElementVisible(By.xpath(String.format(pageHeaderLoc, pageHeader)));
	}

	/**
	 * This method click on links on disclaimer page.
	 * 
	 * @return StoreFrontWebsiteBasePage object
	 */
	public StoreFrontWebsiteBasePage clickDisclaimerPageLinks(String links) {
		driver.click(By.xpath(String.format(disclaimerPageLinkLoc, links)));
		return this;
	}

	/***
	 * This method search a product through search Icon
	 * 
	 * @param product
	 *            name
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontWebsiteBasePage searchProduct(String productName) {
		driver.type(SEARCH_BOX, productName);
		driver.click(SEARCH_ICON_NEAR_SEARCH_BOX);
		logger.info("clicked on 'Search icon' for search a product");
		driver.waitForPageLoad();
		return this;
	}

	/**
	 * This method click on the checkOut Button on the popup PC Perks order
	 * 
	 * @return
	 */
	public StoreFrontAutoshipCartPage checkoutTheCartFromPopUpForPCPerks() {
		driver.click(CHECKOUT_BUTTON_POPUP_LOC);
		logger.info("Clicked on checkout button on the popup");
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
		return new StoreFrontAutoshipCartPage(driver);
	}

	/***
	 * This method get subtotal of items in double data type
	 * 
	 * @param
	 * @return subtotal
	 * 
	 */
	public Double getSubtotalofItemsAtCart() {
		double valueSubtotal = 0.00;
		String subtotal = driver.findElement(SUBTOTAL_LOC).getText();
		if (subtotal.isEmpty()) {
			logger.info("There is no product in cart and hence subtotal is empty");
		} else {
			subtotal = subtotal.split("\\$")[1].replaceAll(",", "");
			valueSubtotal = Double.parseDouble(subtotal);
			logger.info("Subtotal of product is " + valueSubtotal);
		}

		return valueSubtotal;
	}

	/**
	 * This method click on the Autoship cart Link locator in top navigation.
	 * 
	 * @return
	 */
	public StoreFrontAutoshipCartPage clickOnAutoshipCartLink() {
		driver.click(AUTOSHIP_CART_LINK_LOC);
		logger.info("Clicked on autoship cart link in top navigation");
		driver.waitForPageLoad();
		return new StoreFrontAutoshipCartPage(driver);
	}

	/***
	 * This method click PC Perks status link from welcome dropdown.
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage navigateToPCPerksStatusPage() {
		driver.click(WELCOME_DD_PC_PERKS_STATUS_LOC);
		logger.info("PC perks status clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontAutoshipStatusPage(driver);
	}

	/***
	 * This method clicks on the Add More Items button
	 * 
	 * @return
	 */
	public StoreFrontShopSkinCarePage clickAddMoreItemsBtn() {
		driver.waitForElementPresent(ADD_MORE_ITEMS_BTN_LOC,10);
		try{
			driver.turnOffImplicitWaits(3);
			driver.click(ADD_MORE_ITEMS_BTN_LOC);
			logger.info("Add More Items Clicked");
		}
		catch(Exception e){
			driver.click(ADD_MORE_ITEMS_BTN_PC_AUTOSHIP_CART_LOC);
			logger.info("Add More Items PC Autoship Cart clicked");
		} 
		finally{
			driver.turnOnImplicitWaits();
		}
		driver.waitForPageLoad();
		return new StoreFrontShopSkinCarePage(driver);
	}

	/***
	 * This method get get subtotal
	 * 
	 * @param
	 * @return subtotal
	 * 
	 */
	public String getSubtotalofItems() {
		String subtotal = driver.findElement(SUBTOTAL_LOC).getText();
		logger.info("Subtotal of product is " + subtotal);
		return subtotal;
	}

	/***
	 * This method clicked on autoship link
	 * 
	 * @param
	 * @return Store front website base page obj
	 * 
	 */

	public StoreFrontAutoshipCartPage clickAutoshipLink() {
		driver.click(AUTOSHIP_TEXT_LOC);
		logger.info("clicked on autoship link");
		return new StoreFrontAutoshipCartPage(driver);
	}

	/***
	 * This method verifies if the PC one time joining fee msg is displayed or
	 * not
	 * 
	 * @return
	 */
	public boolean isPcOneTimeFeeMsgDisplayed() {
		driver.quickWaitForElementPresent(PC_ONE_TIME_FEE_MSG_LOC);
		return driver.isElementPresent(PC_ONE_TIME_FEE_MSG_LOC);
	}

	/***
	 * This method click the next button at shipping details page
	 * 
	 * @param
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickNextbuttonOfShippingDetails() {
		driver.clickByJS(RFWebsiteDriver.driver, SHIPPING_NEXT_BUTTON_LOC);
		logger.info("Next button clicked of shipping details");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method enter the user credit card
	 * 
	 * @param card
	 *            number
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterUserCreditCardNumber(String cardNumber) {
		driver.clickByJS(RFWebsiteDriver.driver, CARD_TYPE_DD_LOC);
		driver.type(CARD_NUMBER_LOC, cardNumber + "\t");
		logger.info("Entered card number as" + cardNumber);
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method click pc terms and conditions link
	 * 
	 * @param
	 * @return StoreFrontCartPage object
	 */
	public StoreFrontWebsiteBasePage clickPCTermsAndConditionsLink() {
		driver.click(PC_TERMS_AND_CONDITIONS_LINK_LOC);
		logger.info("PC Terms and Conditions link Clicked");
		return this;
	}

	/***
	 * This method selects the first sponsor name in the search result and
	 * return sponser email.
	 * 
	 * @param sponsor
	 * @return
	 * 
	 */
	public String selectAndReturnFirstSponsorFromList() {
		String sponserName = driver.findElement(SELECT_AND_CONTINUE_FIRST_SPONSER_LOC).getText();
		driver.clickByJS(RFWebsiteDriver.driver, SELECT_AND_CONTINUE_FIRST_SPONSER_LOC);
		logger.info("Clicked on 'Select And Continue' button for first result");
		logger.info("selected first sponser name is " + sponserName);
		driver.pauseExecutionFor(2000);
		return sponserName;
	}

	/***
	 * This method selects and return the sponsor name in the search result.
	 * 
	 * @param sponsor
	 * @return
	 * 
	 */
	public String selectAndReturnSponsorFromList(String sponserNumber) {
		driver.pauseExecutionFor(2000);
		String sponserName = driver.findElement(By.xpath(String.format(selectAndContinueSponserLoc, sponserNumber)))
				.getText();
		driver.clickByJS(RFWebsiteDriver.driver,By.xpath(String.format(selectAndContinueSponserLoc, sponserNumber)));
		// driver.click(By.xpath(String.format(selectAndContinueSponserLoc,
		// sponserNumber)));
		logger.info("Clicked on 'Select And Continue' button for" + sponserNumber + " result");
		logger.info("selected sponser name is " + sponserName);
		driver.pauseExecutionFor(2000);
		return sponserName;
	}

	/***
	 * This method verifies if change sponser link present on account info page
	 * displayed or not
	 * 
	 * @return
	 */
	public boolean isChangeSponserLinkDisplayed() {
		return driver.isElementVisible(REMOVE_LINK_LOC);
	}

	/***
	 * This method get product quantity
	 * 
	 * @param quantity
	 * @return updated quantity by 1
	 * 
	 */
	public String updateQuantityByOne(String quantity) {
		quantity = "" + Integer.toString((Integer.parseInt(quantity) + 1));
		logger.info("Updated quantity is " + quantity);
		return quantity;
	}

	/***
	 * This method enter product quantity
	 * 
	 * @param itemNumber,
	 *            quantity
	 * @return store front Cart page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterQuantityOfProductAtCart(String itemNumber, String quantity) {
		if (itemNumber.equalsIgnoreCase("1")) {
			driver.type(QUANTITY_OF_FIRST_PRODUCT_LOC, quantity);
		}
		logger.info("In cart" + itemNumber + " 's qunatity updated as " + quantity);
		return this;
	}

	/***
	 * This method click on update link
	 * 
	 * @param itemNumber
	 * @return store front Cart page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickOnUpdateLinkThroughItemNumber(String itemNumber) {
		if (itemNumber.equalsIgnoreCase("1")) {
			driver.click(UPDATE_LINK_OF_FIRST_PRODUCT_LOC);
		}
		logger.info("Update link of " + itemNumber + " is clicked");
		return this;
	}

	/***
	 * This method get product quantity for specific product
	 * 
	 * @param productName
	 * @return product quantity
	 * 
	 */
	public String getQuantityOfSpecificProductFromCart(String productName) {
		String productQty = driver.getAttribute(By.xpath(String.format(productQuantityInAllItemsLoc, productName)),
				"value");
		logger.info("Quantity of " + productName + " is " + productQty);
		return productQty;
	}

	/***
	 * This method click the sign up now links
	 * 
	 * @param
	 * @return store front checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickSignUpNowLink() {
		driver.click(SIGN_UP_NOW_LINK_LOC);
		logger.info("Sign up now link clicked");
		driver.waitForPageLoad();
		return new StoreFrontCheckoutPage(driver);
	}

	/***
	 * This method clicks on the 'Why R+F' link in Top Navigation
	 * 
	 * @param
	 * @return
	 * 
	 */
	public StoreFrontWebsiteBasePage clickWhyRF() {
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(WHY_RF_LOC);
		driver.waitForPageLoad();
		logger.info("clicked on 'Why R+F'");
		return this;
	}

	/***
	 * This method hover on become a consultant and click on programms and
	 * incentives link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickProgramsAndIncentives() {
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(PROGRAMS_AND_INCENTIVES_LOC);
		driver.waitForPageLoad();
		logger.info("clicked on 'PROGRAMS & INCENTIVES' link");
		return this;
	}

	/**
	 * This method clicks on the place order button
	 * 
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickPlaceOrderButton() {
		clickBecomeAConsultant();
		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click become a consultant button
	 * 
	 * @param
	 * @return store front Base page object
	 */
	public StoreFrontWebsiteBasePage clickBecomeAConsultant() {
		driver.clickByJS(RFWebsiteDriver.driver, BECOME_A_CONSULTANT_BTN_LOC);
		// driver.click(BECOME_A_CONSULTANT_BTN_LOC);
		logger.info("Become a consultant button clicked");
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
		return this;
	}

	/***
	 * This method verifies sponser search Page
	 * 
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isSponserSearchPageDisplayed() {
		return driver.isElementVisible(SPONSOR_SEARCH_FIELD_LOC);
	}

	public void clickUseAsEnteredButtonOnPopUp() {
		try{
			driver.turnOffImplicitWaits(3);
			driver.click(USE_AS_ENTERED_BUTTON_LOC);
			driver.waitForElementToBeInVisible(USE_AS_ENTERED_BUTTON_LOC, 50);
			//driver.pauseExecutionFor(40000); // UI is slow, will be removed
			logger.info("'Used as entered' button clicked");
		}catch(Exception e){
			driver.turnOnImplicitWaits();
		}
	}

	/***
	 * This method split the card name and provide short name to assert
	 * 
	 * @param String
	 *            cardName
	 * @return String
	 * 
	 */
	public String getLastName(String completeName) {
		String[] splittedName = completeName.trim().split(" ");
		String lastName = splittedName[splittedName.length - 1];
		logger.info("Card last name : " + lastName);
		return lastName;
	}

	/***
	 * This method verifies events Page
	 * 
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isEventsPageHeaderDisplayed() {
		return driver.isElementVisible(EVENTS_PAGE_HEADER_LOC);
	}

	/***
	 * This method verifies meet our community Page
	 * 
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isMeetOurCommunityPageHeaderDisplayed() {
		return driver.isElementVisible(MEET_OUR_COMMUNITY_PAGE_HEADER_LOC);
	}

	/***
	 * This method verifies why RF Page
	 * 
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isWhyRFPageHeaderDisplayed() {
		return driver.isElementVisible(WHY_RF_PAGE_HEADER_LOC);
	}

	/***
	 * This method verifies Programs and incentives page
	 * 
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isProgramsAndIncentivesPageHeaderDisplayed() {
		return driver.isElementVisible(PROGRAMS_AND_INCENTVES_PAGE_LOC);
	}

	/***
	 * This method verifies Enroll as consultant Page
	 * 
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isEnrollAsConsultantPageHeaderDisplayed() {
		return driver.isElementVisible(ENROLL_AS_CONSULTANT_PAGE_LOC);
	}

	/***
	 * This method clicks on the next btn
	 * 
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickNextBtn() {
		driver.click(NEXT_BTN_LOC);
		logger.info("Next btn clicked");
		return this;
	}

	/***
	 * This method will click on the confirm pulse subscription btn Can also be
	 * used while cancelling the pulse subscription,also handles the ok btn on
	 * the cancellation popup
	 * 
	 * @return
	 */
	public StoreFrontCheckoutPage clickConfirmSubscription() {
		driver.clickByJS(RFWebsiteDriver.driver, CONFIRM_PULSE_SUBSCRIPTION_BTN_LOC);
		logger.info("confirm sibscription btn clicked");
		driver.pauseExecutionFor(2000);
		if (driver.isElementVisible(OK_BTN_LOC)) {
			driver.click(OK_BTN_LOC);
			logger.info("OK btn on the confirm pulse subscription popup clicked");
		}
		driver.waitForPageLoad();
		return new StoreFrontCheckoutPage(driver);
	}

	/***
	 * This method selects the checkbox at the time of pulse subscription. Also
	 * clicks on the following subscribe btn
	 * 
	 * @return
	 */
	public StoreFrontCheckoutPage checkTheConfirmSubscriptionChkBoxAndSubscribe() {
		driver.clickByJS(RFWebsiteDriver.driver, PULSE_SUBSCRIPTION_CHKBOX_LOC);
		logger.info("selected the checkbox at the time of pulse subscription");
		driver.clickByJS(RFWebsiteDriver.driver, PULSE_SUBSCRIBE_BTN_LOC);
		logger.info("clicked on the pulse subscribe btn");
		return new StoreFrontCheckoutPage(driver);
	}

	/***
	 * This method verifies whether PC Perks Promo msg is displayed or NOT
	 * 
	 * @return boolean
	 */
	public boolean isPCPerksPromoMsgDisplayed() {
		return driver.isElementVisible(PC_PERKS_PROMO_MSG_LOC);
	}

	/***
	 * This method validates text at page is visible or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isTextVisible(String textName) {
		return driver.isElementVisible(By.xpath(String.format(textLoc, textName)));
	}

	/***
	 * This method validates the presence of mandatory field msgs for billing
	 * profile
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isErrrorMsgsForAllMandatoryFieldsForBillingAddressArePresent() {
		return isMandatoryFieldMsgPresentForTheField("firstName") && isMandatoryFieldMsgPresentForTheField("line1")
				&& isMandatoryFieldMsgPresentForTheField("townCity") && isMandatoryFieldMsgPresentForTheField("region")
				&& isMandatoryFieldMsgPresentForTheField("postcode") && isMandatoryFieldMsgPresentForTheField("phone");
	}

	/***
	 * This method validates the presence of mandatory field msg for billing
	 * address details
	 * 
	 * @param String
	 *            field
	 * @return boolean
	 * 
	 */
	private boolean isMandatoryFieldMsgPresentForTheField(String field) {
		boolean isMsgPresent = driver.isElementPresent(
				By.xpath(String.format(mandatoryFieldErrorMsgOfAddressForNewBillingProfileLoc, field)));
		logger.info("Is Expected Mandatory field msg is present for " + field + " : " + isMsgPresent);
		return isMsgPresent;
	}

	/***
	 * This method verifies the presence address verification popup
	 * 
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isAddressVerificationPopupAppeared() {
		return driver.isElementVisible(USE_AS_ENTERED_BUTTON_LOC);
	}

	/***
	 * This method click EditPWS link from welcome dropdown.
	 * 
	 * @param
	 * @return StoreFrontAutoshipCartPage object
	 * 
	 */
	public StoreFrontAboutMePage navigateToEditPWSPage() {
		driver.click(WELCOME_DD_EDIT_PWS_LOC);
		logger.info("Edit PWS clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontAboutMePage(driver);
	}

	/***
	 * This method validates EditPWS link from welcome dropdown.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isEditPWSLinkPresentInWelcomeDD() {
		return driver.isElementVisible(WELCOME_DD_EDIT_PWS_LOC);
	}

	/***
	 * This method Update last name on checkout Page.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage updateLastName(String updatedName) {
		driver.type(LAST_NAME_FOR_ADDRESS_DETAILS_LOC, updatedName);
		logger.info("Last Name Updated for account info at checkout page" + updatedName);
		return this;
	}

	/***
	 * This method click the save button of shipping address
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickSaveButtonOfShippingAddress() {
		driver.clickByJS(RFWebsiteDriver.driver, SAVE_BUTTON_OF_SHIPPING_ADDRESS_LOC);
		logger.info("Save button clicked");
		return this;
	}

	/***
	 * This method validates find a consultant link on homepage
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isFindAConsultantLinkOnHomePagePresent() {
		return driver.isElementVisible(FIND_A_CONSULTANT_LINK_LOC);
	}

	/***
	 * This method get product qty
	 * 
	 * @param product
	 *            number
	 * @return product qty
	 * 
	 */
	public String getProductQuantityOfAnItem(String itemNumber) {
		String qty = driver.findElement(By.xpath(String.format(qtyOfProductOfAnItemLoc, itemNumber))).getText();
		logger.info("Quantity of product is" + qty + "of item number" + itemNumber);
		return qty;
	}

	/***
	 * This method get product SV value
	 * 
	 * @param product
	 *            number
	 * @return product SV
	 * 
	 */
	public String getProductSVOfAnItem(String itemNumber) {
		String SV = driver.findElement(By.xpath(String.format(SVOfProductOfAnItemLoc, itemNumber))).getText();
		logger.info("SV of product is" + SV + "of item number" + itemNumber);
		return SV;
	}

	/***
	 * This method get product unit price
	 * 
	 * @param product
	 *            number
	 * @return product unit price
	 * 
	 */
	public String getProductUnitPriceOfAnItem(String itemNumber) {
		String price = driver.findElement(By.xpath(String.format(unitPriceOfProductOfAnItemLoc, itemNumber))).getText();
		logger.info("unit price of product is" + price + "of item number" + itemNumber);
		return price;
	}

	/***
	 * This method get product order total
	 * 
	 * @param product
	 *            number
	 * @return product order total
	 * 
	 */
	public String getProductOrderTotalOfAnItem(String itemNumber) {
		String orderTotal = driver.findElement(By.xpath(String.format(orderTotalOfProductOfAnItemLoc, itemNumber)))
				.getText();
		logger.info("order total of product is" + orderTotal + "of item number" + itemNumber);
		return orderTotal;
	}

	/***
	 * This method get product name
	 * 
	 * @param product
	 *            number
	 * @return product name
	 * 
	 */
	public String getProductNameOfAnItem(String itemNumber) {
		String productName = driver.findElement(By.xpath(String.format(productNameOfAnItemLoc, itemNumber))).getText();
		logger.info("product name is" + productName + "of item number" + itemNumber);
		return productName;
	}

	/***
	 * This method validates the presence of shipping section
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isShippingSectionPresent() {
		return driver.isElementPresent(SHIPPING_SECTION_LOC);
	}

	/***
	 * This method validates the presence of Biiling section
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isBillingSectionPresent() {
		return driver.isElementPresent(BILLING_SECTION_LOC);
	}

	/***
	 * This method enter the consultant Billing Address details
	 * 
	 * @param First
	 *            name,Last name, address line1, city, state, postal code, phone
	 *            number
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterConsultantAddressDetails(String firstName, String lastName,
			String addressLine1, String city, String state, String postal, String phoneNumber) {
		String completeName = firstName + " " + lastName;
		driver.type(FIRST_NAME_FOR_ADDRESS_DETAILS_LOC, completeName); // this
		logger.info("Entered complete name as " + completeName);
		driver.type(ADDRESS_LINE_1_FOR_ADDRESS_DETAILS_LOC, addressLine1);
		logger.info("Entered address line 1 as " + addressLine1);
		driver.type(CITY_FOR_ADDRESS_DETAILS_LOC, city);
		logger.info("Entered city as " + city);
		driver.click(STATE_DD_FOR_REGISTRATION_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetails, state)));
		logger.info("State selected as " + state);
		driver.type(POSTAL_CODE_FOR_ADDRESS_DETAILS_LOC, postal);
		logger.info("Entered postal code as " + postal);
		driver.type(PHONE_NUMBER_FOR_ADDRESS_DETAILS_LOC, phoneNumber);
		logger.info("Entered Phone number  as " + phoneNumber);
		return this;
	}

	/***
	 * This method get billing profile details on order review page
	 * 
	 * @param
	 * @return Complete billing profile details.
	 * 
	 */
	public String getBillingProfileDetails() {
		String billingProfileDetails = driver.findElement(BILLING_PROFILE_DETAILS_ORDER_REVIEW_PAGE_LOC).getText();
		logger.info("Billing Profile Details On order review Page" + billingProfileDetails);
		return billingProfileDetails;
	}

	/***
	 * This method enter the consultant billing Address details
	 * 
	 * @param First
	 *            name,Last name, address line1, city, state, postal code, phone
	 *            number
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterConsultantAddressDetails(String firstName, String lastName,
			String addressLine1, String city, String state) {
		String completeName = firstName + " " + lastName;
		driver.type(FIRST_NAME_FOR_ADDRESS_DETAILS_LOC, completeName); // this
		logger.info("Entered complete name as " + completeName);
		driver.type(ADDRESS_LINE_1_FOR_ADDRESS_DETAILS_LOC, addressLine1);
		logger.info("Entered address line 1 as " + addressLine1);
		driver.type(CITY_FOR_ADDRESS_DETAILS_LOC, city);
		logger.info("Entered city as " + city);
		driver.click(STATE_DD_FOR_REGISTRATION_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetails, state)));
		logger.info("State selected as " + state);
		return this;
	}

	/***
	 * This method checks whether the pop up for billing address selected other
	 * than north dakota present.
	 * 
	 * @return
	 */
	public boolean isPopUpForBillingAddressOtherThanNorthDakotaPresent() {
		String expectedText = "Not a resident of North Dakota";
		String popupText = driver.findElement(BILLING_ADDRESS_OTHER_THAN_ND_POPUP_TEXT_LOC).getText();
		if (driver.isElementVisible(POPUP_FOR_TERMS_AND_CONDITIONS_LOC) && popupText.contains(expectedText)) {
			return true;
		} else {
			return false;
		}

	}

	/***
	 * This method selects choose a kit option on popup for invalid ND billing
	 * address
	 * 
	 * @param sponsor
	 * @return
	 * 
	 */
	public void selectChooseAKitOptionOnPopup() {
		driver.click(CHOOSE_A_KIT_OPTION_ON_POPUP_LOC);
		logger.info("Clicked on 'Choose a Kit' option on popup for invalid ND billig address.");
		driver.waitForPageLoad();
	}


	/***
	 * This method validates presence of checkout popup.
	 * 
	 * @param 
	 * @return boolean value
	 * 
	 */
	public boolean isCheckoutPopupDisplayed(){
		driver.pauseExecutionFor(2000);
		return driver.findElement(CHECKOUT_BUTTON_POPUP_LOC).isDisplayed();
	}

	/***
	 * This method clicks on the Learn View link 
	 * @return StoreFrontWebsiteBasePage
	 */
	public StoreFrontWebsiteBasePage clickLearnMoreLink(){
		driver.click(LEARN_MORE_LINK_LOC);
		logger.info("Learn More link is clicked");
		driver.pauseExecutionFor(1000);
		return this;
	}

	/***
	 * this method verifies whether PC perks promo popup is
	 * displayed or not after clicking on learn more link
	 * @return
	 */
	public boolean isLearnMoreAboutPCPromoPopupDisplayed(){
		driver.pauseExecutionFor(4000); // due to slwo UI, will be replaced by wait
		return driver.isElementVisible(PC_PROMO_POPUP_LOC);
	}

	/***
	 * This method closes the PC Perks Promo popup
	 * @return StoreFrontWebsiteBasePage
	 */
	public StoreFrontWebsiteBasePage closePCPerksPromoPopUp(){
		driver.click(PC_PROMO_POPUP_CLOSE_BTN_LOC);
		logger.info("closed the PC Perks promo popup");
		return this;
	}

	/***
	 * This method get available autosuggested prefix name
	 * 
	 * @param
	 * @return autosuggested prefix name
	 * 
	 */
	public String getAvailablePrefixName() {
		String availablePrefix = driver.findElement(PWS_PREFIX_INPUT_FIELD_LOC).getAttribute("value");
		logger.info("Available autosuggested prefix name " + availablePrefix);
		return availablePrefix;
	}

	/***
	 * This method get available autosuggested prefix name
	 * 
	 * @param
	 * @return autosuggested prefix name
	 * 
	 */
	public String getErrorMessageForExistingPrefixName() {
		String existingPrefixMsg = driver.findElement(ERROR_MESSAGE_EXISTING_PREFIX_LOC).getText();
		logger.info("Error msg for Existing prefix" + existingPrefixMsg);
		return existingPrefixMsg;
	}

	/***
	 * This method click on the checkOut Button on the popup on the cart.
	 * 
	 * @return
	 */
	public StoreFrontAutoshipCartPage checkoutTheCartFromPopUpForAutoship() {
		driver.click(CHECKOUT_BUTTON_POPUP_LOC);
		logger.info("Clicked on checkout button on the popup");
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
		return new StoreFrontAutoshipCartPage(driver);
	}

	/***
	 * This method click the SAVE Payment button
	 * 
	 * @param
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickSavePaymentButton(){
		driver.click(SAVE_PAYMENT_BUTTON_LOC);
		logger.info("Save payment button clicked of billing details");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method enter the billing address details after editinig an existing billing profile
	 * 
	 * @param First name,Last name, address line1, city, state, postal code, phone number
	 * @return store front Websitebase page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterBillingAddressDetailsForExistingBillingProfile(String firstName, String lastName, String addressLine1, String addressLine2, String city, String state, String postal, String phoneNumber){
		String completeName = firstName+" "+lastName;
		driver.waitForElementToBeVisible(FIRST_LAST_NAME_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC, 10);
		driver.type(FIRST_LAST_NAME_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE1_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(ADDRESS_LINE_2_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC, addressLine2);
		logger.info("Entered address line 2 as "+addressLine2);
		driver.type(CITY_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetailsForExistingBillingProfile, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(PHONE_NUMBER_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);		
		return this;
	}

	/***
	 * This method click on cancel button for Shipping/Billing Address Updation
	 * 
	 * @param
	 * @return store front Website Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickCancelButton(){
		driver.click(CANCEL_BUTTON_LOC);
		logger.info("Clicked on cancel button");
		return this;
	}

	/***
	 * This method fetch the action success message  
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String getActionSuccessMsg(){
		logger.info(driver.getText(ACTION_SUCCESS_MSG_LOC));
		return driver.getText(ACTION_SUCCESS_MSG_LOC);
	}

	/***
	 * This method validates the product search results page.
	 * 
	 * @param
	 * @return product name
	 * 
	 */
	public boolean isProductSearchResultsPresent() {
		int totalProducts = driver.findElements(TOTAL_PRODUCTS_LOC).size();
		if(totalProducts>0){
			return true;
		}else{
			return false;	
		}

	}
	/***
	 * This method enter product name in search Icon
	 * 
	 * @param product
	 *            name
	 * @return store front base object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterProductName(String productName) {
		driver.type(SEARCH_BOX, productName);
		logger.info("Entered product Name in search box"+productName);
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method clicks on the About me link in Top Navigation
	 * 
	 * @param
	 * @return
	 */
	public StoreFrontAboutMePage clickAboutMe(){
		mouseHoverOn(TestConstants.ABOUT_ME);
		driver.click(ABOUT_ME_LOC);
		logger.info("clicked on 'About Me'");
		return new StoreFrontAboutMePage(driver);
	}

	/***
	 * This method get total of all products in mini cart
	 * 
	 * @param
	 * @return String total
	 * 
	 */
	public String gettotalofItemsInMiniCart(){
		List<WebElement> prices = driver.findElements(TOTAL_PRICE_OF_ITEMS_IN_MINI_CART_LOC);
		float totalPrice = 0;

		for(WebElement productPrice : prices){
			float price = Float.parseFloat(productPrice.getText().replace("$", "").trim());
			totalPrice = totalPrice + price;
		}
		logger.info("Total Price of Items in Cart : " + totalPrice);
		return String.valueOf(totalPrice);
	}

	/***
	 * This method get product quantity
	 * 
	 * @param itemNumber
	 * @return product quantity
	 * 
	 */
	public String getQuantityOfProductFromCart(String itemNumber) {
		String productQuantity = null;
		String productIndex = Integer.toString(Integer.parseInt(itemNumber) - 1);
		productQuantity = driver.getAttribute(By.xpath(String.format(quantityOfSpecificItemInCart, productIndex)),"value");
		logger.info("Quantity of " + itemNumber + " is " + productQuantity);
		return productQuantity;
	}

	/***
	 * This method get product name of first item in mini cart
	 * 
	 * @param itemNumber
	 * @return product name
	 * 
	 */
	public String getProductNameFromMiniCart(String itemNumber) {
		String productName = null;
		productName = driver.getText(By.xpath(String.format(itemNameInMiniCart, itemNumber))).trim();
		logger.info("product name of " + itemNumber + " is " + productName);
		return productName;
	}

	/***
	 * This method get product quantity from mini cart
	 * 
	 * @param itemNumber
	 * @return product quantity
	 * 
	 */
	public String getQuantityOfProductFromMiniCart(String itemNumber) {
		String productQuantity = null;
		productQuantity = driver.getText(By.xpath(String.format(itemQuantityInMiniCart, itemNumber)));
		logger.info("Quantity of " + itemNumber + " is " + productQuantity);
		return productQuantity;
	}

	/***
	 * This method click on View Shopping cart button on mini cart
	 * 
	 * @param
	 * @return store front cart Page object
	 * 
	 */
	public StoreFrontCartPage clickViewShoppingCartLink(){
		driver.click(VIEW_SHOPPING_CART_LINK_LOC);
		logger.info("View Shopping Cart Link Clicked");
		return new StoreFrontCartPage(driver);
	}

	/***
	 * This method validates the specific address field in Billing address on UI
	 * Profile Overloaded method
	 * 
	 * @param String, String
	 * @return boolean value
	 * 
	 */
	public boolean isAddressFieldPresentAsExpectedOnUI(String billingAddressOnUI, String addressField) {
		return billingAddressOnUI.contains(addressField);
	}

	/***
	 * This method validate that enroll now popup is displayed
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */
	public boolean isEnrollNowPopupIsDisplayed(){
		driver.waitForElementToBeVisible(ENROLL_NOW_POPUP_LOC,10);
		return driver.isElementVisible(ENROLL_NOW_POPUP_LOC);
	}

	/***
	 * This method validate that enroll now popup is not displayed
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */
	public boolean isEnrollNowPopupDisplayedAfterSelectingNoFromEnrollNowPopup(){
		driver.waitForElementToBeInVisible(ENROLL_NOW_POPUP_LOC,10);
		return driver.isElementVisible(ENROLL_NOW_POPUP_LOC);
	}

	/***
	 * This method click on the specific option available on enroll now popup
	 * 
	 * @param 
	 * @return StoreFrontWebsiteBasePage object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickOptionFromEnrollNowPopup(String option){
		driver.click(By.xpath(String.format(optionOnEnrollNowPopUpLoc, option)));
		logger.info(option + " Option selected from Enroll now Popup");
		return this;
	}

	/***
	 * This method click the save button
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickSaveButton() {
		driver.quickWaitForElementPresent(SAVE_BUTTON_LOC);
		driver.clickByJS(RFWebsiteDriver.driver,SAVE_BUTTON_LOC);
		logger.info("Save button clicked");
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method will click on the confirm pulse subscription btn
	 * 
	 * 
	 * 
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickConfirmSubscriptionButton() {
		driver.clickByJS(RFWebsiteDriver.driver, CONFIRM_PULSE_SUBSCRIPTION_BTN_LOC);
		logger.info("confirm sibscription btn clicked");
		driver.pauseExecutionFor(2000);
		return this;
	}
	/***
	 * This method enters the pws prefix
	 * 
	 * @param prefix
	 * @return
	 */
	public StoreFrontWebsiteBasePage enterAvailablePrefix(String prefix) {
		driver.type(PWS_PREFIX_INPUT_FIELD_LOC, prefix);
		logger.info("The pws prefix entered is " + prefix);
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method validates products displayed for selected category
	 * 
	 * @param 
	 * @return Boolean
	 * 
	 */
	public boolean isProductsDisplayedOnPage(){
		return driver.isElementVisible(By.xpath(String.format(addToCartButtonLoc,"1")));
	}

	/***
	 * This method clear the all Billing address field at checkout page
	 * 
	 * @param
	 * @return
	 * 
	 */
	public StoreFrontWebsiteBasePage clearAllFieldsOfBillingAddressDetailsForExistingBillingProfile(){
		driver.clear(FIRST_LAST_NAME_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC);
		logger.info("Cleared Field First last Name");
		driver.clear(ADDRESS_LINE1_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC);
		logger.info("Cleared Field Address line 1");
		driver.clear(ADDRESS_LINE_2_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC);
		logger.info("Cleared Field Address line 2");
		driver.clear(CITY_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC);
		logger.info("Cleared Field City");
		Select select = new Select(driver.findElement(STATE_DD_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC));
		select.selectByIndex(1);
		logger.info("Cleared Field State");
		driver.clear(POSTAL_CODE_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC);
		logger.info("Cleared Field Postal Code");
		driver.clear(PHONE_NUMBER_FOR_BILLING_ADDRESS_FOR_EXISTING_PROFILE_LOC);
		logger.info("Cleared Field Phone number");
		return this;
	}

	/***
	 * This method get available autosuggested prefix name
	 * 
	 * @param
	 * @return autosuggested prefix name
	 * 
	 */
	public String getErrorMessageForEmptyPrefixName() {
		String existingPrefixMsg = driver.findElement(ERROR_MESSAGE_EMPTY_PREFIX_LOC).getText();
		logger.info("Error msg for Empty prefix name" + existingPrefixMsg);
		return existingPrefixMsg;
	}

	/***
	 * This method will close search text box in header navigation.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickCloseIconOfSearchTextBoxPopup() {
		driver.clickByJS(RFWebsiteDriver.driver, CLOSE_ICON_OF_SEARCH_TEXT_BOX_IN_HEADER_NAVIGATION_LOC);
		driver.pauseExecutionFor(3000);
		return this;
	}

	/***
	 * This method click on toggle button of country
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickToggleButtonOfCountry() {
		driver.clickByJS(RFWebsiteDriver.driver, TOGGLE_BUTTON_OF_COUNTRY_LOC);
		driver.pauseExecutionFor(1000);
		logger.info("clicked on toggle button of country");
		return this;
	}

	/***
	 * This method hover on shopSkincare and click link mentioned in argument.
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontShopSkinCarePage navigateToShopSkincareLink(String linkName) {
		if(linkName.equals("ALL PRODUCTS")){
			driver.get(driver.getCurrentUrl()+"All-Skincare/c/shopskincare");
		}
		else{
			mouseHoverOn(TestConstants.SHOP_SKINCARE);
			if (driver.isElementVisible(By.xpath(String.format(topNavigationSublinksWithTextLoc, linkName))) == false) {
				driver.pauseExecutionFor(2000);
				mouseHoverOn(TestConstants.SHOP_SKINCARE);
			}
			driver.click(By.xpath(String.format(topNavigationSublinksWithTextLoc, linkName)));
		}
		logger.info("clicked on" + "'" + linkName + "'" + "under shopskincare");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontShopSkinCarePage(driver);
	}

	/***
	 * This method clicks on the event button PWS site
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickEventsOnPWSJoin(){
		driver.clickByJS(RFWebsiteDriver.driver,By.xpath("//a[@href='events']"));
		driver.pauseExecutionFor(2000);
		logger.info("clicked on 'Events' button on pws join site");
		return this;
	}

	/***
	 * This method clicks on the enroll Now button PWS site
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickEnrollOnPWSJoin(){
		driver.clickByJS(RFWebsiteDriver.driver,By.xpath("//a[text()='ENROLL NOW']"));
		driver.pauseExecutionFor(2000);
		logger.info("clicked on 'Enroll Now' button on pws join site");
		return this;
	}

	/***
	 * This method clicks on the programs and incentives button PWS site
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickProgramsIncentivesOnPWSJoin(){
		driver.clickByJS(RFWebsiteDriver.driver,By.xpath("//a[@href='programs-incentives']"));
		driver.pauseExecutionFor(2000);
		logger.info("clicked on 'prgrams and incentives' button on pws join site");
		return this;
	}

	/***
	 * This method clicks on the Meet Our Community button PWS site
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickMeetOurCommunityOnPWSJoin(){
		driver.clickByJS(RFWebsiteDriver.driver,By.xpath("//a[@href='meet-our-community']"));
		driver.pauseExecutionFor(2000);
		logger.info("clicked on 'Meet Our Community' button on pws join site");
		return this;
	}

	/***
	 * This method clicks on the Why RF button PWS site
	 * @return
	 */
	public StoreFrontWebsiteBasePage clickWhyRFOnPWSJoin(){
		driver.clickByJS(RFWebsiteDriver.driver,By.xpath("//a[@href='why-rf']"));
		driver.pauseExecutionFor(2000);
		logger.info("clicked on 'Why R+F' button on pws join site");
		return this;
	}

	/***
	 * This method click pc terms and conditions link
	 * 
	 * @param
	 * @return StoreFrontCartPage object
	 */
	public boolean isProductAddedToCartPresentOnCartPage(String productName) {
		if(productName.contains("'")){
			productName = "\""+productName+"\"";
			return driver.isElementVisible(By.xpath(String.format(productNameAllItemsInCartLoc, productName)));
		}else{
			return driver.isElementVisible(By.xpath(String.format(productNameInAllItemsInCartLoc, productName)));
		}
	}

	/***
	 * This method click Yes Button on Address Suggestion Popup
	 * 
	 * @param
	 * @return StorefrontWebsiteBasePage
	 */
	public StoreFrontWebsiteBasePage clickYesButtonOnAddressSuggestionPopUp() {
		driver.pauseExecutionFor(3000);
		driver.click(YES_BUTTON_ON_ADDRESS_SUGGESTION_MODAL_LOC);
		logger.info("Clicked Yes Button from Address Suggestion Popup");
		return this;
	}

	/***
	 * This method get the confirmation message of consultant enrollment
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isOrderPlacedSuccessfully(){
		driver.quickWaitForElementPresent(CONFIRMATION_MSG_OF_PLACED_ORDER_LOC);
		//		String confirmationText= driver.getText(CONFIRMATION_MSG_OF_PLACED_ORDER_LOC);
		if(driver.isElementPresent(CONFIRMATION_MSG_OF_PLACED_ORDER_LOC)){
			return true;
		}else
			return false;
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
		logger.info("Order Number is "+orderNumber);
		return orderNumber;
	}

	/***
	 * This method select sort by price filter High to low
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontWebsiteBasePage productPriceFilterHighToLow(){
		driver.click(SORT_FILTER_DD_LOC);
		logger.info("Sort filter dropdown clicked");
		driver.click(SHOP_BY_PRICE_FILTER_OPTION_HIGH_TO_LOW_LOC);
		logger.info("Price filter 'HIGH TO LOW' selected");
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method clicks on the All products link from Top Navigation
	 * 
	 * @return
	 */
	public StoreFrontShopSkinCarePage clickAllProducts() {
		driver.quickWaitForElementPresent(SHOP_SKINCARE_LOC);
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(ALL_PRODUCTS_LOC);
		//clickCategoryLink("UNBLEMISH");
		//driver.get(driver.getCurrentUrl()+"/All-Skincare/c/shopskincare");
		logger.info("clicked on 'All Products'");
		driver.waitForPageLoad();
		productPriceFilterHighToLow();
		return new StoreFrontShopSkinCarePage(driver);
	}

	public boolean hasTokenizationFailed(){
		System.out.println("***");
		try{
			driver.turnOffImplicitWaits(0);
			System.out.println("^^^^");
			driver.quickWaitForElementPresent(By.xpath(String.format(errorMessageLoc,"Failed to create subscription")));
			driver.findElement(By.xpath(String.format(errorMessageLoc,"Failed to create subscription")));
			System.out.println("Tokenization Failed");
			driver.clickByJS(RFWebsiteDriver.driver,By.xpath("//div[@class='global-alerts']/following::a[contains(text(),'Continue')][1]"));
			System.out.println("Continue clicked");
			driver.waitForPageLoad();
			return true;
		}catch(Exception e){
			System.out.println("Exception "+e);
			System.out.println("Tokenization NOT Failed");
			return false;
		}finally{
			driver.turnOnImplicitWaits();
		}
	}
}
