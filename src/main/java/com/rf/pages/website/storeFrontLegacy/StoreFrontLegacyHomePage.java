package com.rf.pages.website.storeFrontLegacy;

import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;

public class StoreFrontLegacyHomePage extends StoreFrontLegacyRFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(StoreFrontLegacyHomePage.class.getName());

	private static String regimenLoc= "//cufontext[text()='%s']/following::a[1]/img";
	private static String genderLocForPCAndRC= "//label[text()='%s']/preceding::input[1]";
	private static String expiryMonthLoc= "//select[contains(@id,'uxMonthDropDown')]//option[@value='%s']";
	private static String expiryYearLoc= "//select[contains(@id,'uxYearDropDown')]//option[@value='%s']";
	private static String regimenHeaderLoc = "//div[@id='HeaderCol']//span[text()='%s']";
	private static String regimenNameLoc= "//cufon[@alt='%s']";
	private static String redefineRegimenSubLinks= "//cufontext[text()='REDEFINE']/following::li//span[text()='%s']";
	private static String detailLinkOnProgramIncentivePageLoc = "//div[@id='RFContent']//td[1]/p/strong[text()='%s']/following-sibling::a[1]";
	private static String businessSystemSubTitleLoc = "//div[@id='HeaderCol']//span[text()='%s']";
	private static String subTitleLoc = "//div[@id='HeaderCol']//span[text()='%s']";
	private static String essentialsRegimenSubLinks= "//cufontext[text()='ESSENTIALS']/following::li//span[text()='%s']";
	private static String enhancementsRegimenSubLinks= "//cufontext[text()='ENHANCEMENTS']/following::li//span[text()='%s']";
	private static String regimenNameOnPwsLoc = "//div[@id='ProductCategories']//p[@class='productInfo']//span[text()='%s']";
	private static String regimenImageOnPwsLoc = "//div[@id='ProductCategories']//p[@class='productInfo']//span[text()='%s']/../preceding-sibling::p/a";
	private static String myAccountLinkAfterLoginLink = "//div[@class='topContents']//span[text()='%s']";
	private static String consultantEnrollmentKit = "//span[@class='kitPrice']//cufontext[contains(text(),'%s')]/preceding::div[@class='imageWrap'][1]";
	private static String consultantRegimenLoc = "//span[@class='catName']//cufontext[contains(text(),'%s')]/following::img[1]";
	private static String retailPriceOfItem = "//div[@class='FloatCol']/div[%s]//tr[2]//div[1]/span[1]";
	private static String addToCartBtnLoc = "//div[@class='FloatCol']/div[%s]//a[text()='Add to Cart']";
	private static String sectionUnderReplenishmentOrderManagementLoc = "//a[text()='%s']";
	private static String linkUnderMyAccount = "//div[@id='RFContent']//span[contains(text(),'%s')]";
	private static String viewDetailsOnOrderHistoryPage = "//div[@id='RFContent']//tr[@class='tdhead']/following-sibling::tr[%s]//a[contains(text(),'View Details')]";
	private static String orderNumberOnOrderHistoryPage = "//div[@id='RFContent']//tr[@class='tdhead']/following-sibling::tr[%s]/td[1]";
	private static String regimenImageHeaderLoc = "//div[@id='HeaderCol']//cufon[@alt='%s']";

	private static final By PRODUCTS_LIST_LOC = By.xpath("//div[@id='FullPageItemList']");
	private static final By RESULTS_TEXT_LOC = By.xpath("//cufontext[text()='RESULTS']/preceding::canvas[1]");
	private static final By TESTIMONIAL_PAGE_CONTENT_LOC = By.xpath("//div[@id='RFContent']/div/div/blockquote[1]/div[1]");
	private static final By NEWS_TEXT_LOC = By.xpath("//div[@id='RFContent']");
	private static final By FAQS_TEXT_LOC = By.xpath("//div[@id='RFContent']//cufontext[text()='Questions']");
	private static final By ADVICE_PAGE_CONTENT_LOC = By.xpath("//div[@id='RFContent']//td[1]");
	private static final By GLOSSARY_PAGE_CONTENT_LOC = By.xpath("//div[@id='GlossaryAF']");
	private static final By INGREDIENTS_AND_USAGE_LINK_LOC = By.xpath("//a[text()='Ingredients and Usage']");
	private static final By INGREDIENTS_CONTENT_LOC = By.xpath("//span[@id='ProductUsage']");
	private static final By FOOTER_CONTACT_US_LINK_LOC = By.xpath("//div[@id='FooterPane']//span[text()='Contact Us']");
	private static final By CONTACT_US_PAGE_HEADER_LOC = By.xpath("//cufontext[text()='About ']");

	private static final By BE_A_CONSULTANT_LOC = By.xpath("//span[text()='Be a Consultant']");
	private static final By ENROLL_NOW_ON_BUSINESS_PAGE_LOC = By.xpath("//*[@id='LeftNav']//span[text()='Enroll Now']");
	private static final By CID_LOC = By.id("NameOrId");
	private static final By CID_SEARCH_LOC = By.id("BtnSearch");
	private static final By SEARCH_RESULTS_LOC = By.xpath("//div[@id='searchResults']//a");
	private static final By BIG_BUSINESS_LAUNCH_KIT_LOC = By.xpath("//cufontext[contains(text(),'Big')]/ancestor::div[@class='KitContent'][1]");
	private static final By BUSINESS_PORTFOLIO_KIT_LOC =By.xpath("//cufontext[contains(text(),'Portfolio')]/ancestor::div[@class='KitContent'][1]");
	private static final By SELECT_ENROLLMENT_KIT_NEXT_BTN_LOC =By.xpath("//a[@id='BtnNext']//canvas");
	private static final By ACCOUNT_INFORMATION_NEXT_BTN = By.xpath("//a[@id='btnNext']//cufon");
	private static final By REDEFINE_REGIMEN_LOC = By.xpath("//cufontext[text()='REDEFINE ']/ancestor::a[1]");
	private static final By REGIMEN_NEXT_BTN_LOC = By.xpath("//a[@id='BtnDone']");
	private static final By EXPRESS_ENROLLMENT_LOC = By.id("btnGoToExpressEnroll");
	private static final By ENROLLMENT_NEXT_BTN_LOC = By.xpath("//a[@id='btnNext']");
	private static final By STANDARD_ENROLLMENT_LOC = By.id("btnGoToStandardEnrollment");

	private static final By ACCOUNT_FIRST_NAME_LOC = By.id("Account_FirstName");
	private static final By ACCOUNT_LAST_NAME_LOC = By.id("Account_LastName");
	private static final By ACCOUNT_EMAIL_ADDRESS_LOC = By.id("Account_EmailAddress");
	private static final By ACCOUNT_PASSWORD_LOC = By.id("Account_Password");
	private static final By ACCOUNT_CONFIRM_PASSWORD_LOC = By.id("txtConfirmPassword");
	private static final By ACCOUNT_MAIN_ADDRESS1_LOC = By.id("MainAddress_Address1");
	private static final By ACCOUNT_POSTAL_CODE_LOC = By.id("MainAddress_PostalCode");
	private static final By ACCOUNT_PHONE_NUMBER1_LOC = By.id("txtPhoneNumber1");
	private static final By ACCOUNT_PHONE_NUMBER2_LOC = By.id("txtPhoneNumber2");
	private static final By ACCOUNT_PHONE_NUMBER3_LOC = By.id("txtPhoneNumber3");

	private static final By SETUP_ACCOUNT_NEXT_BTN_LOC = By.id("btnNext");
	private static final By USE_AS_ENTERED_BTN_LOC = By.xpath(".//*[@id='QAS_AcceptOriginal']");
	private static final By CARD_NUMBER_LOC = By.xpath(".//*[@id='Payment_AccountNumber']");
	private static final By NAME_ON_CARD_LOC = By.xpath(".//*[@id='Payment_NameOnCard']");
	private static final By EXP_MONTH_LOC = By.id("ExpMonth");
	private static final By EXP_YEAR_LOC = By.id("ExpYear");
	private static final By SSN1_LOC = By.xpath(".//*[@id='txtTaxNumber1']");
	private static final By SSN2_LOC = By.xpath(".//*[@id='txtTaxNumber2']");
	private static final By SSN3_LOC = By.xpath(".//*[@id='txtTaxNumber3']");
	private static final By SSN_CARD_NAME_LOC  = By.xpath(".//*[@id='Account_EnrollNameOnCard']");
	private static final By PWS_LOC = By.id("Account_EnrollSubdomain");
	private static final By PWS_AVAILABLE_MSG_LOC = By.xpath("//li[@id='Abailable0' and contains(text(),'is available')]");
	private static final By COMPLETE_ACCOUNT_NEXT_BTN_LOC = By.xpath(".//*[@id='btnNext']");

	private static final By EMAIL_POLICY_AND_PROCEDURE_TOGGLE_BTN_LOC = By.xpath("//input[@id='Account_EnrollPolicyProcedure']/following::div[1]//div[@class='ibutton-handle-middle']") ;
	private static final By ENROLL_SWITCH_POLICY_TOGGLE_BTN_LOC = By.xpath("//input[@id='Account_EnrollSwitchingPolicy']/following::div[1]//div[@class='ibutton-handle-middle']") ;
	private static final By ENROLL_TERMS_CONDITIONS_TOGGLE_BTN_LOC = By.xpath("//input[@id='Account_EnrollTermsAndConditions']/following::div[1]//div[@class='ibutton-handle-middle']") ;
	private static final By ENROLL_E_SIGN_CONSENT_TOGGLE_BTN_LOC = By.xpath("//input[@id='Account_EnrollESignConsent']/following::div[1]//div[@class='ibutton-handle-middle']") ;

	private static final By CHARGE_AND_ENROLL_ME_BTN_LOC = By.id("ChargeAndEnrollMe");
	private static final By CONFIRM_AUTOSHIP_BTN_LOC = By.id("confirmAuthoship");
	private static final By CONGRATS_MSG_LOC = By.id("Congrats");

	private static final By AUTOSHIP_OPTIONS_NEXT_BTN_LOC = By.xpath("//a[@id='btnNext']");
	private static final By ADD_TO_CART_BTN_LOC = By.xpath("//div[@class='productList']/div[1]/div[1]/a[2]"); 
	private static final By YOUR_CRP_ORDER_POPUP_NEXT_BTN_LOC = By.id("PlaceCRPOrder");
	private static final By USERNAME_TEXT_BOX_LOC = By.id("username");
	private static final By PASSWORD_TXTFLD_ONFOCUS_LOC = By.id("passwordWatermarkReplacement");
	private static final By PASSWORD_TEXT_BOX_LOC = By.id("password");
	private static final By ENTER_LOGIN_BTN_LOC = By.id("loginButton");
	private static final By SUBSCRIBE_TO_PULSE_TOGGLE_BTN_LOC = By.xpath("//input[@id='Account_EnrollPulse']/following::div[1]//div[@class='ibutton-handle-middle']");
	private static final By ENROLL_IN_CRP_TOGGLE_BTN_LOC = By.xpath("//input[@id='Account_EnrollCRP']/following::div[1]//div[@class='ibutton-handle-middle']");
	private static final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//div[@id='logo']//img");
	private static final By LOGOUT_LOC = By.xpath("//a[text()='Log Out']");
	private static final By EXISTING_CONSULTANT_LOC = By.xpath("//div[@id='ExistentConsultant']/p[contains(text(),'already have a Consultant account')]");
	private static final By BECOME_A_CONSULTANT_MENU = By.xpath("//a[@href='/Pages/BusinessSystem/WhyRF/GettingStarted']");
	private static final By ENROLL_NOW_ON_BIZ_PWS_PAGE_LOC = By.xpath("//*[@id='nav']/div/ul/li[2]/ul/li[2]/a/span");
	private static final By ENROLL_NOW_ON_WHY_RF_PAGE_LOC = By.xpath("//ul[@class='SubNav']//span[contains(text(),'Enroll Now')]");

	private static final By ADD_TO_CART_BTN = By.id("addToCartButton");//"//a[text()='Add to Cart']");
	private static final By CLICK_HERE_LINK_FOR_PC = By.xpath("//a[contains(@id,'PreferredLink')]");
	private static final By ENROLL_NOW_FOR_PC_AND_RC = By.xpath("//a[contains(text(),'Enroll Now')]");
	private static final By FIRST_NAME_FOR_PC_AND_RC = By.xpath("//input[contains(@id,'uxFirstName')]");
	private static final By LAST_NAME_FOR_PC_AND_RC = By.xpath("//input[contains(@id,'uxLastName')]");
	private static final By EMAIL_ADDRESS_FOR_PC_AND_RC = By.xpath("//input[contains(@id,'uxEmailAddress')]");
	private static final By CREATE_PASSWORD_FOR_PC_AND_RC = By.xpath("//table[@class='FormTable']//input[contains(@id,'uxPassword')]");
	private static final By CONFIRM_PASSWORD_FOR_PC_AND_RC = By.xpath("//input[contains(@id,'uxConfirmPassword')]");
	private static final By PHONE_NUMBER_FOR_PC_AND_RC = By.xpath("//input[contains(@id,'uxPhoneNumber')]");
	private static final By CONTINUE_BTN_PREFERRED_PROFILE_PAGE_LOC = By.xpath("//input[contains(@id,'uxContinue')]");
	private static final By ID_NUMBER_AS_SPONSOR = By.xpath("//input[contains(@id,'uxSponsorID')]");
	private static final By BEGIN_SEARCH_BTN = By.xpath("//a[contains(@id,'uxSearchByName')]");
	private static final By SPONSOR_RADIO_BTN = By.xpath("//div[@class='DashRow']/input");
	private static final By TERMS_AND_CONDITION_CHK_BOX_FOR_PC = By.xpath("//input[contains(@id,'uxAgree1')]");
	private static final By CONTINUE_BTN_PREFERRED_AUTOSHIP_SETUP_PAGE_LOC = By.xpath("//div[@id='MyAutoshipItems']/following::input[contains(@id,'uxContinue')]");

	private static final By ADDRESS_NAME_FOR_SHIPPING_PROFILE = By.xpath("//input[contains(@id,'uxAddressName')]");
	private static final By ATTENTION_FIRST_NAME = By.xpath("//td[@class='tdinput']//input[contains(@id,'uxAttentionFirstName')][1]");
	private static final By ATTENTION_LAST_NAME = By.xpath("//td[@class='tdinput']//input[contains(@id,'uxAttentionLastName')][1]");
	private static final By ADDRESS_LINE_1 = By.xpath("//input[contains(@id,'uxAddressLine1')]");
	private static final By ZIP_CODE = By.xpath("//input[contains(@id,'uxZipCode')]");
	private static final By CITY_DD = By.xpath("//input[contains(@id,'uxCityDropDown_Input')]");
	private static final By FIRST_VALUE_OF_CITY_DD = By.xpath("//div[contains(@id,'uxCityDropDown_DropDown')]//ul[@class='rcbList']/li");
	private static final By COUNTRY_DD = By.xpath("//input[contains(@id,'uxCountyDropDown_Input')]");
	private static final By FIRST_VALUE_OF_COUNTRY_DD = By.xpath("//div[contains(@id,'uxCountyDropDown_DropDown')]//ul[@class='rcbList']/li");
	private static final By PHONE_NUMBER_SHIPPING_PROFILE_PAGE = By.xpath("//input[contains(@id,'uxShippingEditor_AppPhone')]");
	private static final By BILLING_NAME_FOR_BILLING_PROFILE = By.xpath("//input[contains(@id,'uxBillingProfileName')]");
	private static final By NAME_ON_CARD = By.xpath("//input[contains(@id,'uxNameOnCard')]");
	private static final By CREDIT_CARD_NUMBER_INPUT_FIELD = By.xpath("//input[contains(@id,'uxCreditCardNumber')]");
	private static final By EXPIRATION_DATE_MONTH_DD = By.xpath("//select[contains(@id,'uxMonthDropDown')]");
	private static final By EXPIRATION_DATE_YEAR_DD = By.xpath("//select[contains(@id,'uxYearDropDown')]");
	private static final By PHONE_NUMBER_BILLING_PROFILE_PAGE = By.xpath("//input[contains(@id,'uxPhone')]");
	private static final By COMPLETE_ENROLLMENT_BTN = By.xpath("//input[contains(@id,'uxComplete')]");
	private static final By WELCOME_TXT_AFTER_ENROLLMENT = By.xpath("//cufontext[contains(text(),'Welcome')]");
	private static final By USERNAME_TXTFLD_LOC = By.id("username");
	private static final By PASSWORD_TXTFLD_LOC = By.id("password");
	private static final By LOGIN_BTN_LOC = By.xpath("//a[@id='loginButton']");
	private static final By CLICK_HERE_LINK_FOR_RC = By.xpath("//a[contains(@id,'RetailLink')]");
	private static final By LOG_OUT_ON_CORP_BTN = By.xpath("//a[text()='Log-Out']");
	private static final By CARRERS_LINK_LOC = By.xpath("//div[@id='LeftNav']//span[text()='Careers']");
	private static final By PRESS_ROOM_LINK_LOC = By.xpath("//div[@id='LeftNav']//span[text()='Press Room']");
	private static final By CONTACT_US_LINK_LOC = By.xpath("//div[@id='LeftNav']//span[text()='Contact Us']");
	private static final By WHO_WE_ARE_LINK_LOC = By.xpath("//div[@id='LeftNav']//span[text()='Who We Are']");
	private static final By PC_PERKS_LINK_LOC = By.xpath("//div[@id='HeaderCol']//span[text()='PC PERKS']");
	private static final By DIGITAL_PRODUCT_LINK_LOC = By.xpath("//div[@id='HeaderCol']//span[text()='Digital Product Catalog']");
	private static final By PRODUCT_PHILOSOPHY_LOC = By.xpath("//span[text()='Product Philosophy']");
	private static final By DIGITAL_PRODUCT_CATALOG_LOC = By.xpath("//span[text()='Digital Product Catalog']");
	private static final By REAL_RESULTS_LINK_LOC = By.xpath("//div[@id='HeaderCol']//span[text()='Real Results']");
	private static final By EXECUTIVE_TEAM_LINK_LOC = By.xpath("//div[@id='LeftNav']//span[text()='Executive Team']");
	private static final By ABOUT_RF_LOC = By.xpath("//span[text()='About R+F']");
	private static final By SOLUTION_TOOL_LINK_LOC = By.xpath("//div[@id='HeaderCol']//span[text()='Solution Tool']");
	private static final By PRIVACY_POLICY_LINK = By.xpath("//span[text()='Privacy Policy']");
	private static final By PRIVACY_POLICY_TEXT = By.xpath("//u[text()='PRIVACY POLICY']");
	private static final By SATISFACTION_GUARANTEE_LINK = By.xpath("//span[text()='Satisfaction Guarantee']");
	private static final By SATISFACTION_GUARANTEE_TEXT = By.xpath("//cufontext[text()='Guarantee']");
	private static final By TERMS_AND_CONDITIONS_LINK = By.xpath("//span[text()='Terms & Conditions']");
	private static final By SOLUTION_TOOL_LOC = By.xpath("//span[text()='Products']/following::li/a/span[text()='Solution Tool']");
	private static final By SOLUTION_TOOL_PAGE_LOC = By.xpath("//div[@id='RFContent']");
	private static final By FIND_RODAN_FIELD_CONSULTANT_LINK_LOC = By.xpath("//div[@id='RFContent']//a[text()='Find a Rodan + Fields Consultant']");
	private static final By SPONSOR_RADIO_BTN_ON_FIND_CONSULTANT_PAGE = By.xpath("//div[@class='DashRow']//input");
	private static final By PWS_TXT_ON_FIND_CONSULTANT_PAGE = By.xpath("//span[contains(text(),'PWS URL')]/a");
	private static final By CHECKOUT_BTN = By.xpath("//span[text()='Checkout']");
	private static final By CLICK_HERE_LINK = By.xpath("//a[text()='Click here']");
	private static final By DETAILS_LINK = By.xpath("//a[text()='Details']");
	private static final By CLICK_HERE_LOC = By.xpath("//div[@id='RFContent']//a[contains(text(),'Income Disclosure Statement')]");
	private static final By SELECTED_HIGHLIGHT_LINK = By.xpath("//div[@id='ContentWrapper']//a[@class='selected']/span");
	private static final By REAL_RESULTS_PAGE_LOC = By.xpath("//div[@id='RFContent']//cufontext[text()='REAL ']/preceding::canvas[1]");
	private static final By PC_PERKS_PAGE_LOC = By.xpath("//div[@id='HeaderCol']//cufontext[text()='PC ']/preceding::canvas[1]");
	private static final By SOLUTION_TOOL_PAGE = By.xpath("//div[@id='RFContent']//a[text()='Find a Rodan + Fields Consultant']");
	private static final By DIGITAL_PRODUCT_CATALOGUE = By.xpath("//div[@class='body']//a[text()='Digital Product Catalog']");
	private static final By BUSINESS_PRESENTATION_SECTION_LOC = By.xpath("//div[@id='RFContent']//following::div[@style='margin-bottom:1em;']/h2//cufontext[text()='Presentations']/ancestor::cufon");
	private static final By HOME_TAB_LOC = By.xpath("//span[text()='Home']");
	private static final By FIND_A_CONSULTANT_IMAGE_LOC = By.xpath("//img[@alt='Find a Rodan and Fields Consultant']");
	private static final By START_NOW_BTN = By.xpath("//a[text()='START NOW']");
	private static final By MEET_OUR_COMMUNITY_LINK_LOC = By.xpath("//div[@id='LeftNav']//span[text()='Meet Our Community']");
	private static final By EVENTS_LINK_LOC = By.xpath("//div[@id='LeftNav']//span[text()='Events']");
	private static final By INCOME_ILLUSTRATOR_LINK_LOC = By.xpath("//div[@id='LeftNav']//span[text()='Income Illustrator']");
	private static final By PROGRAMS_INCENTIVES_LINK_LOC = By.xpath("//div[@id='LeftNav']//span[text()='Programs and Incentives']");
	private static final By WHY_RF_LINK_LOC = By.xpath("//div[@id='LeftNav']//span[text()='Why R+F']");
	private static final By BUSINESS_KITS_LINK_LOC = By.xpath("//span[text()='Business Kits']");
	private static final By BUSINESS_KITS_SECTION_LOC = By.xpath("//div[@id='RFContent']/div/div[@class='body']");
	private static final By REDEFINE_YOUR_FUTURE_LINK_LOC = By.xpath("//span[text()='Redefine Your Future']");
	private static final By ENROLL_NOW_LINK_LOC = By.xpath("//div[@id='LeftNav']//span[text()='Enroll Now']");
	private static final By ENROLL_NOW_LINK_UNDER_WHYRF_LOC = By.xpath("//div[@id='HeaderCol']//span[text()='Enroll Now']");
	private static final By UPCOMING_EVENTS_LINK_LOC = By.xpath("//span[text()='Upcoming Events']");
	private static final By DISCLAIMER_LINK_LOC = By.xpath("//span[text()='Disclaimer']");
	private static final By GIVING_BACK_LINK_LOC = By.xpath("//div[@id='HeaderCol']//span[text()='Giving Back']");
	private static final By BILLING_FIRST_NAME = By.xpath("//td[@class='tdinput']//input[@type='text' and contains(@id,'uxAttentionFirstName')]");
	private static final By BILLING_LAST_NAME = By.xpath("//td[@class='tdinput']//input[@type='text' and contains(@id,'uxAttentionLastName')]");
	private static final By SOLUTION_TOOL_IMAGE_LOC = By.xpath("//img[@alt='Solution Tool']");
	private static final By RF_IN_THE_NEWS_IMAGE_LOC = By.xpath("//img[@alt='Rodan and Fields AGING REDEFINED']");
	private static final By USERNAME_TXTFLD_CHECKOUT_PAGE_LOC = By.xpath("//input[contains(@id,'uxUserNameText')]");
	private static final By PASSWORD_TXTFLD_CHECKOUT_PAGE_LOC = By.xpath("//input[contains(@id,'uxPasswordText')]");
	private static final By SIGN_IN_BTN_CHECKOUT_PAGE_LOC = By.xpath("//a[contains(@id,'lnkLogin')]");
	private static final By RENEW_LATER_LINK = By.xpath("//a[@id='renewLater']");
	private static final By FORGOT_PASSWORD_PWS_LINK_LOC = By.xpath("//a[contains(text(),'Forgot password')]");
	private static final By CHANGE_PASSWORD_TEXT_LOC = By.xpath("//div[@id='ContentWrapper']/p[contains(text(),'reset your password')]");
	private static final By SEND_EMAIL_BTN_LOC = By.xpath("//input[@value='Send Email']");
	private static final By EMAIL_ADDRESS_FIELD_LOC = By.xpath("//td[contains(text(),'Email Address')]/following-sibling::td/input");
	private static final By EDIT_ORDER_UNDER_MY_ACCOUNT_LOC = By.xpath("//span[text()=' Edit Order']");
	private static final By CHANGE_LINK_FOR_SHIPPING_INFO_ON_PWS = By.xpath("//a[contains(@id,'uxChangeShippingLink')]");
	private static final By SHIPPING_ADDRESS_NAME_LOC = By.xpath("//b[text()='Shipping to:']/../following-sibling::p/span[1]");
	private static final By USE_THIS_ADDRESS_SHIPPING_INFORMATION = By.xpath("//a[contains(@id,'uxUseNewAddress')]");
	private static final By ENROLL_NOW_LINK = By.xpath("//span[text()='Enroll Now']");
	private static final By WEBSITE_PREFIX_BIZ_PWS = By.xpath("//li[@id='Abailable1']");
	private static final By CHANGE_MY_PC_PERKS_STATUS_UNDER_MYACCOUNT_LINK = By.xpath("//a[@class='IconLink']/span[text()=' Change my PC Perks Status']");
	private static final By DELAY_OR_CANCEL_PC_PERKS_LINK = By.xpath("//a[@id='PcCancellLink']/span[text()=' Delay or Cancel PC Perks']");
	private static final By YES_CHANGE_MY_AUTOSHIP_DATE_BTN = By.xpath("//a[@id='BtnChangeAutoship']");
	private static final By CHANGE_MY_AUTOSHIP_DATE_BTN = By.xpath("//a[@id='BtnChange']");
	private static final By CONFIRMATION_MSG_LOC = By.xpath("//div[@id='RFContent']/p");
	private static final By BACK_TO_MY_ACCOUNT_BTN_LOC = By.xpath("//a[@id='BtnBack']");
	private static final By CONFIRM_MSG_AT_ORDERS_LOC = By.xpath("//div[@id='ContentWrapper']/div[2]");
	private static final By RADIO_BUTTON_FOR_SIXTY_DAYS_LOC = By.xpath("//input[@value='60']");
	private static final By TOTAL_ITEM_LOC = By.xpath("//div[@id='MyAutoshipItems']//li");
	private static final By REMOVE_LINK_LOC = By.xpath("//div[@id='MyAutoshipItems']//li[1]/a");
	private static final By ADD_TO_CART_LINK_LOC = By.xpath("//span[contains(text(),'Retail')]");
	private static final By STATUS_MSG_LOC = By.xpath("//span[contains(@id,'uxStatusMessage')]");
	private static final By UPDATE_ORDER_BTN_LOC = By.xpath("//div[@id='MyAutoshipItems']/following-sibling::p[2]/input");
	private static final By MSG_UPDATE_ORDER_CONFIRMATION_LOC = By.xpath("//p[@class='success']");
	private static final By ORDER_TOTAL_LOC = By.xpath("//div[@id='TotalBar']/p");
	private static final By ORDER_TOTAL_AT_OVERVIEW_LOC = By.xpath("//span[contains(@id,'uxSubTotal')]");
	private static final By PREFIX_SUGGESTIONS_LIST = By.xpath("//div[@class='websitePrefix']//li[contains(text(),'unavailable')]");
	private static final By EDIT_ORDER_BTN_LOC = By.xpath("//a[text()='Edit Order']");
	private static final By CHANGE_BILLING_INFO_LINK_ON_PWS = By.xpath("//a[contains(@id,'uxChangeAddressLink')]");
	private static final By EDIT_ORDER_BILLING_DETAILS_UPDATE_MESSAGE = By.xpath("//p[contains(@class,'success Pad10')]");
	private static final By WEBSITE_PREFIX_LOC = By.xpath("//input[@id='Account_EnrollSubdomain']");
	private static final By INVALID_LOGIN = By.xpath("//p[@id='loginError']");
	private static final By EXISTING_PC_LOC = By.xpath("//div[@id='ExistentPC']/p[contains(text(),'already have a Preferred Customer')]");
	private static final By EXISTING_RC_LOC = By.xpath("//div[@id='ExistentRetail']/p[contains(text(),'already have a Retail account')]");
	private static final By WEBSITE__PREFIX_LOC = By.xpath("//input[@id='Account_EnrollSubdomain']");
	private static final By EDIT_MY_PHOTO_LINK = By.xpath("//a[contains(text(),'Edit My Photo')]");
	private static final By UPLAOD_A_NEW_PHOTO_BTN = By.xpath("//div[@class='qq-uploader']");
	private static final By EMAIL_VERIFICATION_TEXT = By.xpath("//div[@class='SubmittedMessage'][@style='']");
	private static final By CANCEL_ENROLLMENT_BTN = By.xpath("//a[@id='BtnCancelEnrollment']");
	private static final By SEND_EMAIL_TO_RESET_MY_PASSWORD_BTN = By.xpath("//a[@id='BtnResetPassword']");
	private static final By TOTAL_ROWS_ON_ORDER_HISTORY_PAGE = By.xpath("//div[@id='RFContent']//tr[@class='tdhead']/following-sibling::tr");
	private static final By ORDER_DETAILS_POPUP = By.xpath("//h2[@class='FL modal']//cufontext[contains(text(),'Order')]/../following-sibling::cufon/cufontext[text()='Details']");
	private static final By CLOSE_OF_ORDER_DETAILS_POPUP = By.xpath("//h2[@class='FL modal']/following::cufontext[text()='X']/..");
	private static final By CONNECT_WITH_A_CONSULTANT = By.cssSelector("a[href*='LocatePWS']");
	private static final By CLICK_HERE_TO_LEARN_MORE_ABOUT_DIRECT_SELLING = By.cssSelector("a[href*='directselling']");
	private static final By PRESS_ROOM = By.xpath("//span[contains(text(),'Press Room')]");

	public StoreFrontLegacyHomePage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void clickBeAConsultantBtn(){
		driver.quickWaitForElementPresent(BE_A_CONSULTANT_LOC);
		driver.click(BE_A_CONSULTANT_LOC);
		logger.info("Be a consultant button clicked");
		driver.waitForPageLoad();
	}

	public void clickEnrollNowBtnOnBusinessPage(){
		driver.quickWaitForElementPresent(ENROLL_NOW_ON_BUSINESS_PAGE_LOC);
		driver.click(ENROLL_NOW_ON_BUSINESS_PAGE_LOC);
		logger.info("Enroll Now button on business page is clicked");
		driver.waitForPageLoad();
	}

	public void enterCID(String cid){
		driver.quickWaitForElementPresent(CID_LOC);
		driver.type(CID_LOC, cid);
		logger.info("CID enterd as: "+cid);
		driver.click(CID_SEARCH_LOC);
		logger.info("search button clicked after entering CID");
	}

	public void clickSearchResults(){
		driver.quickWaitForElementPresent(SEARCH_RESULTS_LOC);
		driver.click(SEARCH_RESULTS_LOC);
		logger.info("Search results clicked");
		driver.waitForPageLoad();
	}

	public void selectEnrollmentKit(String kit){
		if(kit.equalsIgnoreCase("Big Business Launch Kit")){
			driver.quickWaitForElementPresent(BIG_BUSINESS_LAUNCH_KIT_LOC);
			driver.click(BIG_BUSINESS_LAUNCH_KIT_LOC);
			logger.info("Big Business Launch Kit is selected");
		}
	}

	public void selectRegimenAndClickNext(String regimen){
		if(regimen.equalsIgnoreCase("Redefine")){
			driver.quickWaitForElementPresent(REDEFINE_REGIMEN_LOC);
			driver.click(REDEFINE_REGIMEN_LOC);
			logger.info("Redefine regimen is selected");
		}
		driver.click(REGIMEN_NEXT_BTN_LOC);
		logger.info("Regimen Next button clicked");
	}

	public void selectEnrollmentType(String enrollmentType){
		if(enrollmentType.equalsIgnoreCase("Express")){
			driver.waitForElementPresent(EXPRESS_ENROLLMENT_LOC);
			driver.click(EXPRESS_ENROLLMENT_LOC);
			logger.info("express enrollment is selected");
		}
		else if(enrollmentType.equalsIgnoreCase("Standard")){
			driver.waitForElementPresent(STANDARD_ENROLLMENT_LOC);
			driver.click(STANDARD_ENROLLMENT_LOC);
			logger.info("standard enrollment is selected");
		}
		driver.click(ENROLLMENT_NEXT_BTN_LOC);
		logger.info("Next button after selecting enrollment type is clicked");
		driver.waitForPageLoad();
	}

	public void enterSetUpAccountInformation(String firstName,String lastName,String emailAddress,String password,String addressLine1,String postalCode,String phnNumber1,String phnNumber2,String phnNumber3){
		driver.type(ACCOUNT_FIRST_NAME_LOC, firstName);
		logger.info("first name entered as: "+firstName);
		driver.type(ACCOUNT_LAST_NAME_LOC, lastName);
		logger.info("last name entered as: "+lastName);
		driver.type(ACCOUNT_EMAIL_ADDRESS_LOC, emailAddress);
		logger.info("email address entered as: "+emailAddress);
		driver.type(ACCOUNT_PASSWORD_LOC, password);
		logger.info("password entered as: "+password);
		driver.type(ACCOUNT_CONFIRM_PASSWORD_LOC, password);
		logger.info("confirm password entered as: "+password);
		driver.type(ACCOUNT_MAIN_ADDRESS1_LOC, addressLine1);
		logger.info("Main Address Line1 entered as: "+addressLine1);
		driver.type(ACCOUNT_POSTAL_CODE_LOC, postalCode+"\t");
		logger.info("Postal code entered as: "+postalCode);
		driver.pauseExecutionFor(3000);
		driver.type(ACCOUNT_PHONE_NUMBER1_LOC,phnNumber1);
		logger.info("Phone number 1 entered as: "+phnNumber1);
		driver.type(ACCOUNT_PHONE_NUMBER2_LOC,phnNumber2);
		logger.info("Phone number 2 entered as: "+phnNumber2);
		driver.type(ACCOUNT_PHONE_NUMBER3_LOC,phnNumber3);	
		logger.info("Phone number 3 entered as: "+phnNumber3);
	}

	public void clickSetUpAccountNextBtn(){
		driver.click(SETUP_ACCOUNT_NEXT_BTN_LOC);
		logger.info("set up account next button is clicked");
		try{
			driver.quickWaitForElementPresent(USE_AS_ENTERED_BTN_LOC);
			driver.findElement(USE_AS_ENTERED_BTN_LOC).click();
			logger.info("use as entered button clicked");
		}catch(NoSuchElementException e){
			logger.info("Use as entered popup not present.");
		}
		driver.waitForPageLoad();
		driver.pauseExecutionFor(5000);
	}

	public void enterBillingInformation(String cardNumber,String nameOnCard,String expMonth,String expYear){
		driver.findElement(CARD_NUMBER_LOC).sendKeys(cardNumber);
		logger.info("card number entered is: "+cardNumber);
		driver.findElement(NAME_ON_CARD_LOC).sendKeys(nameOnCard);
		logger.info("name on card entered is: "+nameOnCard);
		Select dropdown1 = new Select(driver.findElement(EXP_MONTH_LOC));
		dropdown1.selectByVisibleText(expMonth);
		logger.info("Exp Month selected as: "+expMonth);
		Select dropdown2 = new Select(driver.findElement(EXP_YEAR_LOC));
		dropdown2.selectByVisibleText(expYear);
		logger.info("Exp Year selected as: "+expYear);
	}

	public void enterAccountInformation(int randomNum1,int randomNum2,int randomNum3,String SSNCardName){
		driver.findElement(SSN1_LOC).sendKeys(String.valueOf(randomNum1));
		logger.info("SSN1 entered as: "+randomNum1);
		driver.findElement(SSN2_LOC).sendKeys(String.valueOf(randomNum2));
		logger.info("SSN2 entered as: "+randomNum2);
		driver.findElement(SSN3_LOC).sendKeys(String.valueOf(randomNum3));
		logger.info("SSN3 entered as: "+randomNum3);
		driver.findElement(SSN_CARD_NAME_LOC).sendKeys(SSNCardName);
		logger.info("SSN card name entered as: "+SSNCardName);
	}

	public void enterPWS(String pws){
		driver.quickWaitForElementPresent(PWS_LOC);
		driver.type(PWS_LOC, pws);
		logger.info("PWS enterd as: "+pws);
	}

	public void clickCompleteAccountNextBtn(){
		driver.waitForElementToBeVisible(PWS_AVAILABLE_MSG_LOC, 30);
		driver.findElement(COMPLETE_ACCOUNT_NEXT_BTN_LOC).click();
		logger.info("complete account next button clicked");
		driver.waitForPageLoad();
		driver.pauseExecutionFor(5000);
	}

	public void clickTermsAndConditions(){
		driver.findElement(EMAIL_POLICY_AND_PROCEDURE_TOGGLE_BTN_LOC).click();
		logger.info("Email policy and procedure toggle button clicked");
		driver.findElement(ENROLL_SWITCH_POLICY_TOGGLE_BTN_LOC).click();
		logger.info("Enroll switch policy toggle button clicked");
		driver.findElement(ENROLL_TERMS_CONDITIONS_TOGGLE_BTN_LOC).click();
		logger.info("Enroll terms and conditions toggle button clicked");
		driver.findElement(ENROLL_E_SIGN_CONSENT_TOGGLE_BTN_LOC).click();
		logger.info("Enroll e sign consent toggle button clicked");
	}

	public void chargeMyCardAndEnrollMe(){
		driver.quickWaitForElementPresent(CHARGE_AND_ENROLL_ME_BTN_LOC);
		driver.click(CHARGE_AND_ENROLL_ME_BTN_LOC);
		logger.info("Charge and enroll me button clicked");
		driver.pauseExecutionFor(2000);
		driver.quickWaitForElementPresent(CONFIRM_AUTOSHIP_BTN_LOC);
		driver.click(CONFIRM_AUTOSHIP_BTN_LOC);
		logger.info("Confirm autoship button clicked");
		driver.waitForPageLoad();
	}

	public boolean isCongratulationsMessageAppeared(){
		driver.quickWaitForElementPresent(CONGRATS_MSG_LOC);
		return driver.isElementPresent(CONGRATS_MSG_LOC);
	}

	public void clickYesSubscribeToPulseNow() {
		driver.waitForElementPresent(SUBSCRIBE_TO_PULSE_TOGGLE_BTN_LOC);
		driver.click(SUBSCRIBE_TO_PULSE_TOGGLE_BTN_LOC);
		logger.info("subscribe to pulse now option clicked By yes");

	}

	public void clickYesEnrollInCRPNow() {
		driver.waitForElementPresent(ENROLL_IN_CRP_TOGGLE_BTN_LOC);
		driver.click(ENROLL_IN_CRP_TOGGLE_BTN_LOC);
		logger.info("Enroll in CRP now option clicked By yes");		  
	}

	public void clickAutoShipOptionsNextBtn() {
		driver.waitForElementPresent(AUTOSHIP_OPTIONS_NEXT_BTN_LOC);
		driver.click(AUTOSHIP_OPTIONS_NEXT_BTN_LOC);
		logger.info("Autoships options next step button clicked");		  
	}

	public void selectProductToAddToCart() {
		driver.waitForElementPresent(ADD_TO_CART_BTN_LOC);
		driver.click(ADD_TO_CART_BTN_LOC);
		logger.info("Add to cart button clicked");		  
	}

	public void clickYourCRPOrderPopUpNextBtn() {
		driver.waitForElementPresent(YOUR_CRP_ORDER_POPUP_NEXT_BTN_LOC);
		driver.click(YOUR_CRP_ORDER_POPUP_NEXT_BTN_LOC);
		logger.info("Your crp order popup next btn clicked");		  
	}

	public void clickBillingInfoNextBtn(){
		driver.findElement(COMPLETE_ACCOUNT_NEXT_BTN_LOC).click();
		logger.info("standard enrollment complete account next button clicked");
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
	}

	public void openBizPWS(String pws){
		driver.get(pws);
		driver.waitForPageLoad();
		logger.info("page redirected to biz PWS");
	}

	public void clickEnrollNowBtnOnbizPWSPage(){
		//driver.get(driver.getCurrentUrl()+"/Pages/BusinessSystem/WhyRF/GettingStarted");
		//		driver.quickWaitForElementPresent(ENROLL_NOW_ON_BIZ_PWS_PAGE_LOC);
		WebElement enrollnow = driver.findElement(ENROLL_NOW_ON_BIZ_PWS_PAGE_LOC);
		JavascriptExecutor executor = (JavascriptExecutor)(RFWebsiteDriver.driver);
		executor.executeScript("arguments[0].click();", enrollnow);
		//		driver.get(driver.getCurrentUrl()+"/NewEnrollment/EnrollmentKit");
		logger.info("Enroll Now button on biz PWS page is clicked");
		driver.waitForPageLoad();
	}

	public void clickEnrollNowBtnOnWhyRFPage(){
		driver.quickWaitForElementPresent(ENROLL_NOW_ON_WHY_RF_PAGE_LOC);
		driver.click(ENROLL_NOW_ON_WHY_RF_PAGE_LOC);
		logger.info("Enroll Now button on Why RF page is clicked");
		driver.waitForPageLoad();
	}

	public void clickOnRodanAndFieldsLogo(){
		driver.quickWaitForElementPresent(RODAN_AND_FIELDS_IMG_LOC);
		driver.click(RODAN_AND_FIELDS_IMG_LOC);
		logger.info("Rodan and Fields logo clicked"); 
		driver.waitForPageLoad();
	}

	public void logOut(){
		driver.quickWaitForElementPresent(LOGOUT_LOC);
		driver.click(LOGOUT_LOC);
		logger.info("Log Out Link clicked"); 
		driver.waitForPageLoad();
	}

	public boolean validateExistingConsultantPopUp(String emailAddress){
		driver.quickWaitForElementPresent(EXISTING_CONSULTANT_LOC);
		driver.type(ACCOUNT_EMAIL_ADDRESS_LOC, emailAddress);
		logger.info("email address entered as: "+emailAddress);
		driver.type(ACCOUNT_PASSWORD_LOC, "");
		logger.info("password entered as: "+"");
		return driver.isElementPresent(EXISTING_CONSULTANT_LOC);
	}

	public StoreFrontLegacyConsultantPage loginAsConsultant(String userName, String password) {
		driver.waitForElementPresent(USERNAME_TEXT_BOX_LOC);
		driver.type(USERNAME_TEXT_BOX_LOC, userName);
		logger.info("Entered Username is: "+userName);
		driver.click(PASSWORD_TXTFLD_ONFOCUS_LOC);
		driver.type(PASSWORD_TEXT_BOX_LOC, password);
		logger.info("Entered Password is: "+password);
		driver.click(ENTER_LOGIN_BTN_LOC);
		logger.info("login  enter button clicked");
		driver.waitForPageLoad();
		clickRenewLater();
		return new StoreFrontLegacyConsultantPage(driver);
	}

	public void selectRegimen(String regimen){
		regimen = regimen.toUpperCase();
		driver.get(driver.getURL()+"/Shop/"+regimen);

		//		driver.quickWaitForElementPresent(By.xpath(String.format(regimenLoc, regimen)));
		//		driver.click(By.xpath(String.format(regimenLoc, regimen)));
		logger.info("Regimen selected is: "+regimen);
	}

	public void clickAddToCartBtn(){
		//		driver.quickWaitForElementPresent(ADD_TO_CART_BTN);
		//		driver.click(ADD_TO_CART_BTN);
		//		logger.info("Add to cart button clicked");
		//		driver.waitForPageLoad();
		try{
			driver.findElement(By.xpath("//a[text()='Add to Cart']"));
			driver.click(By.xpath("//a[text()='Add to Cart']"));
			System.out.println("Add to cart button on ProdDetailPage is clicked");

		} catch (NoSuchElementException e1) {
			try{
				driver.findElement(ADD_TO_CART_BTN_LOC);
				driver.quickWaitForElementPresent(ADD_TO_CART_BTN_LOC);
				driver.click(ADD_TO_CART_BTN_LOC);
				logger.info("Add to cart button is clicked");
			}
			catch(NoSuchElementException e2)
			{
				driver.quickWaitForElementPresent(By.xpath("//*[@id='FullPageItemList']/div[1]//a[@id='addToCartButton']"));
				driver.click(By.xpath("//*[@id='FullPageItemList']/div[1]//a[@id='addToCartButton']"));
				logger.info("Add to cart button is clicked");

			}


		}
	}

	public void clickClickHereLinkForPC(){
		driver.quickWaitForElementPresent(CLICK_HERE_LINK_FOR_PC);
		driver.click(CLICK_HERE_LINK_FOR_PC);
		logger.info("Click here link clicked for PC enrollment");
		driver.waitForPageLoad();
	}

	public void clickEnrollNowBtnForPCAndRC(){
		driver.quickWaitForElementPresent(ENROLL_NOW_FOR_PC_AND_RC);
		driver.click(ENROLL_NOW_FOR_PC_AND_RC);
		logger.info("Enroll now button clicked");
		driver.waitForPageLoad();
	}

	public void enterProfileDetailsForPCAndRC(String firstName,String lastName,String emailAddress,String password,String phnNumber,String gender){
		driver.type(FIRST_NAME_FOR_PC_AND_RC, firstName);
		logger.info("first name entered as: "+firstName);
		driver.type(LAST_NAME_FOR_PC_AND_RC, lastName);
		logger.info("last name entered as: "+lastName);
		driver.type(EMAIL_ADDRESS_FOR_PC_AND_RC, emailAddress);
		logger.info("email address entered as: "+emailAddress);
		driver.type(CREATE_PASSWORD_FOR_PC_AND_RC, password);
		logger.info("password entered as: "+password);
		driver.type(CONFIRM_PASSWORD_FOR_PC_AND_RC, password);
		logger.info("confirm password entered as: "+password);
		driver.type(PHONE_NUMBER_FOR_PC_AND_RC,phnNumber);
		logger.info("Phone number entered as: "+phnNumber);
		driver.click(By.xpath(String.format(genderLocForPCAndRC, gender)));
		logger.info("Gender selected is: "+gender);
	}

	public void clickContinueBtnForPCAndRC(){
		driver.quickWaitForElementPresent(CONTINUE_BTN_PREFERRED_PROFILE_PAGE_LOC);
		driver.click(CONTINUE_BTN_PREFERRED_PROFILE_PAGE_LOC);
		logger.info("Continue button clicked");
		driver.waitForPageLoad();
	}

	public void enterIDNumberAsSponsorForPCAndRC(String sponsorID){
		driver.quickWaitForElementPresent(ID_NUMBER_AS_SPONSOR);
		driver.type(ID_NUMBER_AS_SPONSOR, sponsorID);
		logger.info("Sponsor ID entered is: "+sponsorID);
	}

	public void clickBeginSearchBtn(){
		driver.quickWaitForElementPresent(BEGIN_SEARCH_BTN);
		driver.click(BEGIN_SEARCH_BTN);
		logger.info("Begin search button clicked");
	}

	public void selectSponsorRadioBtn(){
		driver.quickWaitForElementPresent(SPONSOR_RADIO_BTN);
		driver.click(SPONSOR_RADIO_BTN);
		logger.info("Radio button of sponsor is selected");
	}

	public void clickSelectAndContinueBtnForPCAndRC(){
		driver.quickWaitForElementPresent(CONTINUE_BTN_PREFERRED_PROFILE_PAGE_LOC);
		driver.click(CONTINUE_BTN_PREFERRED_PROFILE_PAGE_LOC);
		logger.info("Select and Continue button clicked");
		driver.waitForPageLoad();		
	}

	public void checkTermsAndConditionChkBoxForPCAndRC(){
		driver.quickWaitForElementPresent(TERMS_AND_CONDITION_CHK_BOX_FOR_PC);
		driver.click(TERMS_AND_CONDITION_CHK_BOX_FOR_PC);
		logger.info("Terms & condition checkbox checked");
	}

	public void clickContinueBtnOnAutoshipSetupPageForPC(){
		driver.quickWaitForElementPresent(CONTINUE_BTN_PREFERRED_AUTOSHIP_SETUP_PAGE_LOC);
		driver.click(CONTINUE_BTN_PREFERRED_AUTOSHIP_SETUP_PAGE_LOC);
		logger.info("Continue button clicked on Autoship setup page");
		driver.waitForPageLoad();
	}


	public void enterShippingProfileDetails(String addressName, String firstName,String lastName,String addressLine1,String postalCode,String phnNumber){
		driver.type(ADDRESS_NAME_FOR_SHIPPING_PROFILE, addressName);
		logger.info("Address name entered as: "+addressName);
		driver.type(ATTENTION_FIRST_NAME, firstName);
		logger.info("Attention first name entered as: "+firstName);
		driver.type(ATTENTION_LAST_NAME, lastName);
		logger.info("Attention last name entered as: "+lastName);
		driver.type(ADDRESS_LINE_1, addressLine1);
		logger.info("Address line 1 entered as: "+addressLine1);
		driver.type(ZIP_CODE, postalCode+"\t");
		driver.waitForStorfrontLegacyLoadingImageToDisappear();
		driver.pauseExecutionFor(3000);
		logger.info("Postal code entered as: "+postalCode);
		driver.type(CITY_DD, "Fremont");
		logger.info("City Selected");
		//		driver.click(FIRST_VALUE_OF_CITY_DD);
		//		logger.info("City selected");
		//		driver.waitForElementPresent(COUNTRY_DD);
		//		driver.click(COUNTRY_DD);
		//		logger.info("Country dropdown clicked");
		/*		driver.click(FIRST_VALUE_OF_COUNTRY_DD);
		logger.info("Country selected");*/
		driver.type(COUNTRY_DD, "ALAMEDA");
		logger.info("Country selected");
		driver.type(PHONE_NUMBER_SHIPPING_PROFILE_PAGE,phnNumber);
		logger.info("Phone number entered as: "+phnNumber);
	}

	public void enterBillingInfoDetails(String billingName, String firstName,String lastName,String cardName,String cardNumer,String month,String year,String addressLine1,String postalCode,String phnNumber){
		driver.pauseExecutionFor(2000);
		driver.type(BILLING_NAME_FOR_BILLING_PROFILE, billingName);
		logger.info("Billing profile name entered as: "+billingName);
		//driver.type(ATTENTION_FIRST_NAME, firstName);
		driver.type(BILLING_FIRST_NAME, firstName);
		logger.info("Attention first name entered as: "+firstName);
		//driver.type(ATTENTION_LAST_NAME, lastName);
		driver.type(BILLING_LAST_NAME, lastName);
		logger.info("Attention last name entered as: "+lastName);
		driver.type(NAME_ON_CARD, cardName);
		logger.info("Card Name entered as: "+cardName);
		driver.type(CREDIT_CARD_NUMBER_INPUT_FIELD, cardNumer);
		logger.info("Card number entered as: "+cardNumer);
		driver.click(EXPIRATION_DATE_MONTH_DD);
		logger.info("Expiration month dropdown clicked");
		driver.click(By.xpath(String.format(expiryMonthLoc, month)));
		logger.info("Expiry month selected is: "+month);
		driver.click(EXPIRATION_DATE_YEAR_DD);
		logger.info("Expiration year dropdown clicked");
		driver.click(By.xpath(String.format(expiryYearLoc, year)));
		logger.info("Expiry year selected is: "+year);
		driver.type(ADDRESS_LINE_1, addressLine1);
		logger.info("Billing street address entered as: "+addressLine1);
		driver.type(ZIP_CODE, postalCode+"\t");
		logger.info("Postal code entered as: "+postalCode);
		driver.waitForStorfrontLegacyLoadingImageToDisappear();
		/*driver.click(CITY_DD);
		  logger.info("City dropdown clicked");
		  driver.click(FIRST_VALUE_OF_CITY_DD);
		  logger.info("City selected");*/
		driver.type(CITY_DD, "Fremont");
		logger.info("City Selected");
		driver.type(PHONE_NUMBER_BILLING_PROFILE_PAGE,phnNumber);
		logger.info("Phone number entered as: "+phnNumber);
	}

	public void clickCompleteEnrollmentBtn(){
		driver.quickWaitForElementPresent(COMPLETE_ENROLLMENT_BTN);
		driver.click(COMPLETE_ENROLLMENT_BTN);
		logger.info("Complete enrollmet button clicked");
		//		driver.waitForPageLoad();
	}

	public void clickUseAsEnteredBtn(){
		driver.quickWaitForElementPresent(USE_AS_ENTERED_BTN_LOC);
		driver.findElement(USE_AS_ENTERED_BTN_LOC).click();
		logger.info("use as entered button clicked");
		driver.waitForPageLoad();
	}

	public boolean isEnrollmentCompletedSuccessfully(){
		driver.quickWaitForElementPresent(WELCOME_TXT_AFTER_ENROLLMENT);
		return driver.isElementPresent(WELCOME_TXT_AFTER_ENROLLMENT);
	}

	public StoreFrontLegacyPCUserPage loginAsPCUser(String username,String password){
		driver.waitForElementPresent(USERNAME_TXTFLD_LOC);
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.click(PASSWORD_TXTFLD_ONFOCUS_LOC);
		driver.type(PASSWORD_TXTFLD_LOC,password);  
		logger.info("login username is "+username);
		logger.info("login password is "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		driver.waitForPageLoad();
		return new StoreFrontLegacyPCUserPage(driver);		
	}

	public void loginAsRCUser(String username,String password){
		driver.waitForElementPresent(USERNAME_TXTFLD_LOC);
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.click(PASSWORD_TXTFLD_ONFOCUS_LOC);
		driver.type(PASSWORD_TXTFLD_LOC,password);  
		logger.info("login username is "+username);
		logger.info("login password is "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		driver.waitForPageLoad();
	}

	public void clickClickHereLinkForRC(){
		driver.quickWaitForElementPresent(CLICK_HERE_LINK_FOR_RC);
		driver.click(CLICK_HERE_LINK_FOR_RC);
		logger.info("Click here link for RC User is clicked.");
		driver.waitForPageLoad();
	}

	public void clickCreateMyAccountBtnOnCreateRetailAccountPage(){
		driver.quickWaitForElementPresent(CONTINUE_BTN_PREFERRED_PROFILE_PAGE_LOC);
		driver.click(CONTINUE_BTN_PREFERRED_PROFILE_PAGE_LOC);
		logger.info("Create my account button clicked");
		try{
			clickOKBtnOfJavaScriptPopUp();
		}catch(Exception e){
			logger.info("No alert present");
		}
		driver.waitForPageLoad();
	}

	public String getJavaScriptPopUpText(){
		Alert alert = driver.switchTo().alert();
		String alertTxt=alert.getText();
		logger.info("Txt From java Script popup is "+alertTxt);
		return alertTxt;
	}

	public boolean verifyUserSuccessfullyLoggedInOnCorpSite() {
		driver.waitForElementPresent(LOG_OUT_ON_CORP_BTN);
		if(driver.isElementPresent(LOG_OUT_ON_CORP_BTN)){
			logger.info("User Successfully Logged In");
			return true;
		}else return false;
	}

	public void selectBusinessPortfolioEnrollmentKit(){
		driver.quickWaitForElementPresent(BUSINESS_PORTFOLIO_KIT_LOC);
		driver.click(BUSINESS_PORTFOLIO_KIT_LOC);
		logger.info("Business Portfolio Kit is selected");
	}

	public void clickNextBtnOnSelectEnrollmentKitPage(){
		driver.quickWaitForElementPresent(SELECT_ENROLLMENT_KIT_NEXT_BTN_LOC);
		driver.click(SELECT_ENROLLMENT_KIT_NEXT_BTN_LOC);
		logger.info("Business Portfolio Kit is selected");
		driver.waitForPageLoad();
	}

	public void clickAccountInformationNextBtn(){
		driver.quickWaitForElementPresent(ACCOUNT_INFORMATION_NEXT_BTN);
		driver.click(ACCOUNT_INFORMATION_NEXT_BTN);
		logger.info("Account Information Next button clicked");
		driver.waitForPageLoad();
	}

	public void clickChargeMyCardAndEnrollMeWithOutConfirmAutoship(){
		driver.quickWaitForElementPresent(CHARGE_AND_ENROLL_ME_BTN_LOC);
		driver.click(CHARGE_AND_ENROLL_ME_BTN_LOC);
		logger.info("Charge and enroll me button clicked");
		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
	}

	public void clickSubSectionUnderRegimen(String regimenHeader){
		driver.quickWaitForElementPresent(By.xpath(String.format(regimenHeaderLoc, regimenHeader)));
		driver.click(By.xpath(String.format(regimenHeaderLoc, regimenHeader)));
		logger.info("Regimen selected is: "+regimenHeader);
	}

	public boolean verifyUserIsRedirectedToProductsPage(String regimenType) {
		driver.waitForElementPresent(PRODUCTS_LIST_LOC);
		return driver.isElementPresent(PRODUCTS_LIST_LOC)&& driver.isElementPresent(By.xpath(String.format(regimenImageHeaderLoc, regimenType.toUpperCase()))); 
	}

	public boolean verifyUserIsRedirectedToResultsPage(String regimenType) {
		driver.waitForElementPresent(RESULTS_TEXT_LOC);
		return driver.isElementPresent(RESULTS_TEXT_LOC)&& driver.isElementPresent(By.xpath(String.format(regimenImageHeaderLoc, regimenType.toUpperCase()))); 
	}

	public boolean verifyUserIsRedirectedToTestimonialsPage() {
		// TODO Auto-generated method stub
		driver.waitForElementPresent(TESTIMONIAL_PAGE_CONTENT_LOC);
		return driver.isElementPresent(TESTIMONIAL_PAGE_CONTENT_LOC);
	}

	public boolean verifyUserIsRedirectedToNewsPage() {
		driver.waitForElementPresent(NEWS_TEXT_LOC);
		return driver.isElementPresent(NEWS_TEXT_LOC);
	}

	public boolean verifyUserIsRedirectedToFAQsPage(String regimenType) {
		driver.waitForElementPresent(FAQS_TEXT_LOC);
		return driver.isElementPresent(FAQS_TEXT_LOC) && driver.isElementPresent(By.xpath(String.format(regimenImageHeaderLoc, regimenType)));
	}

	public boolean verifyUserIsRedirectedToAdvicePage() {
		driver.waitForElementPresent(ADVICE_PAGE_CONTENT_LOC);
		return driver.isElementPresent(ADVICE_PAGE_CONTENT_LOC);
	}

	public boolean verifyUserIsRedirectedToGlossaryPage() {
		driver.waitForElementPresent(GLOSSARY_PAGE_CONTENT_LOC);
		return driver.isElementPresent(GLOSSARY_PAGE_CONTENT_LOC);
	}

	public void clickIngredientsAndUsageLink() {
		driver.waitForElementPresent(INGREDIENTS_AND_USAGE_LINK_LOC);
		driver.click(INGREDIENTS_AND_USAGE_LINK_LOC);
		logger.info("INGREDIENTS AND USAGE LINK CLICKED");
	}

	public boolean verifyIngredientsAndUsageInfoVisible() {
		driver.waitForElementPresent(INGREDIENTS_CONTENT_LOC);
		return driver.IsElementVisible(driver.findElement(INGREDIENTS_CONTENT_LOC));

	}

	public void clickContactUsAtFooter() {
		driver.waitForElementPresent(FOOTER_CONTACT_US_LINK_LOC);
		driver.click(FOOTER_CONTACT_US_LINK_LOC);
		logger.info("contact us link is clicked");
	}

	public boolean verifylinkIsRedirectedToContactUsPage() {
		driver.waitForElementPresent(CONTACT_US_PAGE_HEADER_LOC);
		return driver.isElementPresent(CONTACT_US_PAGE_HEADER_LOC);
	}

	public void clickProductPhilosophyLink(){
		driver.quickWaitForElementPresent(PRODUCT_PHILOSOPHY_LOC);
		driver.click(PRODUCT_PHILOSOPHY_LOC);
		logger.info("Product Philosophy Link clicked");
		driver.waitForPageLoad();
	}

	public void clickDigitalProductCatalogLink(){
		driver.quickWaitForElementPresent(DIGITAL_PRODUCT_CATALOG_LOC);
		driver.click(DIGITAL_PRODUCT_CATALOG_LOC);
		logger.info("Digital Product Catalog Link clicked");
		driver.waitForPageLoad();
	}

	public boolean validateProductPhilosohyPageDisplayed(){
		return driver.getCurrentUrl().contains("Products/Philosophy");
	}

	public boolean validateRealResultsLink(){
		driver.quickWaitForElementPresent(REAL_RESULTS_LINK_LOC);
		driver.click(REAL_RESULTS_LINK_LOC);
		logger.info("Real Results Link clicked");
		driver.waitForPageLoad();
		boolean status= driver.getCurrentUrl().contains("Results");
		driver.navigate().back();
		return status;
	}

	public boolean validateExecutiveTeamLinkPresent(){
		driver.quickWaitForElementPresent(EXECUTIVE_TEAM_LINK_LOC);
		return driver.isElementPresent(EXECUTIVE_TEAM_LINK_LOC);
	}

	public boolean validateExecuteTeamLink(){
		driver.quickWaitForElementPresent(EXECUTIVE_TEAM_LINK_LOC);
		driver.click(EXECUTIVE_TEAM_LINK_LOC);
		logger.info("Execute Team Link clicked");
		driver.waitForPageLoad();
		return driver.getCurrentUrl().contains("Executives");
	}

	public boolean validateCareersLinkPresent(){
		driver.quickWaitForElementPresent(CARRERS_LINK_LOC);
		return driver.isElementPresent(CARRERS_LINK_LOC);
	}

	public boolean validateContactUsLinkPresent(){
		driver.quickWaitForElementPresent(CONTACT_US_LINK_LOC);
		return driver.isElementPresent(CONTACT_US_LINK_LOC);
	}

	public boolean validateWhoWeAreLinkPresent(){
		driver.quickWaitForElementPresent(WHO_WE_ARE_LINK_LOC);
		return driver.isElementPresent(WHO_WE_ARE_LINK_LOC);
	}

	public boolean validatePressRoomLinkPresent(){
		driver.quickWaitForElementPresent(PRESS_ROOM_LINK_LOC);
		return driver.isElementPresent(PRESS_ROOM_LINK_LOC);
	}

	public boolean validateSolutionToolLink(){
		driver.quickWaitForElementPresent(SOLUTION_TOOL_LINK_LOC);
		driver.click(SOLUTION_TOOL_LINK_LOC);
		logger.info("Solution Tool Link clicked");
		driver.waitForPageLoad();
		boolean status= driver.getCurrentUrl().contains("SolutionTool");
		driver.navigate().back();
		return status;
	}

	public boolean validatePCPerksLink(){
		driver.quickWaitForElementPresent(PC_PERKS_LINK_LOC);
		driver.click(PC_PERKS_LINK_LOC);
		logger.info("PC Perks Link clicked");
		driver.waitForPageLoad();
		boolean status= driver.getCurrentUrl().contains("PCPerks");
		driver.navigate().back();
		return status;
	}

	public boolean validateDigitalProductCatalogLink(){
		driver.quickWaitForElementPresent(DIGITAL_PRODUCT_LINK_LOC);
		driver.click(DIGITAL_PRODUCT_LINK_LOC);
		logger.info("Digital Product Link clicked");
		driver.waitForPageLoad();
		boolean status= driver.getCurrentUrl().contains("Digimag");
		driver.navigate().back();
		return status;
	}

	public void clickAboutRFBtn(){
		driver.quickWaitForElementPresent(ABOUT_RF_LOC);
		driver.click(ABOUT_RF_LOC);
		logger.info("About RF button clicked");
		driver.waitForPageLoad();
	}

	public void clickPrivacyPolicyLink(){
		driver.quickWaitForElementPresent(PRIVACY_POLICY_LINK);
		driver.click(PRIVACY_POLICY_LINK);
		logger.info("Privacy policy link clicked");
		driver.waitForPageLoad();
	}

	public boolean isPrivacyPolicyPagePresent(){
		driver.quickWaitForElementPresent(PRIVACY_POLICY_TEXT);
		return driver.isElementPresent(PRIVACY_POLICY_TEXT);
	}

	public void clickSatisfactionGuaranteeLink(){
		driver.quickWaitForElementPresent(SATISFACTION_GUARANTEE_LINK);
		driver.click(SATISFACTION_GUARANTEE_LINK);
		logger.info("Satisfaction guarantee link clicked");
		driver.waitForPageLoad();
	}

	public boolean isSatisfactionGuaranteePagePresent(){
		driver.quickWaitForElementPresent(SATISFACTION_GUARANTEE_TEXT);
		return driver.isElementPresent(SATISFACTION_GUARANTEE_TEXT);
	}

	public void selectPromotionRegimen(){
		String regimen = "Promotions";
		driver.quickWaitForElementPresent(By.xpath(String.format(regimenLoc, regimen)));
		driver.click(By.xpath(String.format(regimenLoc, regimen)));
		logger.info("Regimen selected is: "+regimen);
	}

	public boolean isRegimenNamePresentAfterClickedOnRegimen(String regimen){
		regimen = regimen.toUpperCase();
		driver.quickWaitForElementPresent(By.xpath(String.format(regimenNameLoc, regimen)));
		return driver.isElementPresent(By.xpath(String.format(regimenNameLoc, regimen)));
	}

	public void clickTermsAndConditionsLink(){
		driver.quickWaitForElementPresent(TERMS_AND_CONDITIONS_LINK);
		driver.click(TERMS_AND_CONDITIONS_LINK);
		logger.info("Terms & Conditions link clicked");
		driver.waitForPageLoad();
	}

	public void clickSolutionToolUnderProduct(){
		driver.quickWaitForElementPresent(SOLUTION_TOOL_LOC);
		driver.click(SOLUTION_TOOL_LOC);
		logger.info("Solution tool clicked under product tab button");
		driver.waitForPageLoad();
	}

	public boolean verifySolutionToolPage(){
		driver.quickWaitForElementPresent(SOLUTION_TOOL_PAGE_LOC);
		return driver.isElementPresent(SOLUTION_TOOL_PAGE_LOC);
	}

	public void clickFindRodanFieldConsultantLink(){
		driver.quickWaitForElementPresent(FIND_RODAN_FIELD_CONSULTANT_LINK_LOC);
		driver.click(FIND_RODAN_FIELD_CONSULTANT_LINK_LOC);
		logger.info("Find rodan and fields consultant link is clicked.");
		driver.waitForPageLoad();
	}

	public void selectSponsorRadioBtnOnFindConsultantPage(){
		driver.quickWaitForElementPresent(SPONSOR_RADIO_BTN_ON_FIND_CONSULTANT_PAGE);
		driver.click(SPONSOR_RADIO_BTN_ON_FIND_CONSULTANT_PAGE);
		logger.info("Radio button of sponsor is selected");
	}

	public String getPWSFromFindConsultantPage(){
		driver.quickWaitForElementPresent(PWS_TXT_ON_FIND_CONSULTANT_PAGE);
		String fetchPWS=driver.findElement(PWS_TXT_ON_FIND_CONSULTANT_PAGE).getText();
		logger.info("Fetched PWS from find a consultant page is"+fetchPWS);
		return fetchPWS;
	}

	public boolean verifyRedefineRegimenSections(String sublinkName){
		driver.quickWaitForElementPresent(By.xpath(String.format(redefineRegimenSubLinks, sublinkName)));
		return driver.IsElementVisible(driver.findElement(By.xpath(String.format(redefineRegimenSubLinks, sublinkName))));
	}

	public boolean verifyAddToCartButton() {
		driver.quickWaitForElementPresent(ADD_TO_CART_BTN);
		return driver.isElementPresent(ADD_TO_CART_BTN);
	}

	public boolean verifyCheckoutBtnOnMyShoppingCart() {
		driver.quickWaitForElementPresent(CHECKOUT_BTN);
		return driver.isElementPresent(CHECKOUT_BTN);
	}

	public void clickClickhereLink(){
		driver.quickWaitForElementPresent(CLICK_HERE_LINK);
		driver.click(CLICK_HERE_LINK);
		logger.info("Clicke here link clicked");
		driver.waitForPageLoad();
	}

	public boolean isClickHereLinkRedirectinToAppropriatePage(String redirectedPageLink){
		driver.waitForPageLoad();
		Set<String> set=driver.getWindowHandles();
		Iterator<String> it=set.iterator();
		String parentWindow=it.next();
		String childWindow=it.next();
		driver.switchTo().window(childWindow);
		boolean status= driver.getCurrentUrl().contains(redirectedPageLink);
		driver.close();
		driver.switchTo().window(parentWindow);
		return status;
	}

	public void clickDetailsLink(){
		driver.quickWaitForElementPresent(DETAILS_LINK);
		driver.click(DETAILS_LINK);
		logger.info("Details link clicked");
		driver.waitForPageLoad();
	}

	public boolean verifySubSectionPresentAtBusinessSystemPage(
			String SubSectionUnderBusinessSystem) {
		return driver.isElementPresent(By.xpath(String.format(businessSystemSubTitleLoc, SubSectionUnderBusinessSystem)));
	}

	public void clickSubSectionUnderBusinessSystem(String secondSubSectionUnderBusinessSystem) {
		driver.quickWaitForElementPresent(By.xpath(String.format(businessSystemSubTitleLoc, secondSubSectionUnderBusinessSystem)));
		driver.click(By.xpath(String.format(businessSystemSubTitleLoc, secondSubSectionUnderBusinessSystem)));
		logger.info("business System SubTitle selected is: "+secondSubSectionUnderBusinessSystem);
	}

	public boolean verifySubSectionPresentAtProgramsAndIncentives(String firstSubSectionUnderProgramsAndIncentives) {
		return driver.isElementPresent(By.xpath(String.format(subTitleLoc, firstSubSectionUnderProgramsAndIncentives)));
	}

	public void clickToReadIncomeDisclosure() {
		driver.quickWaitForElementPresent(CLICK_HERE_LOC);
		driver.click(CLICK_HERE_LOC);

	}

	public String getCurrentUrlOpenedWindow() {
		//  driver.pauseExecutionFor(5000);
		String parentWindowID=driver.getWindowHandle();
		Set<String> set=driver.getWindowHandles();
		Iterator<String> it=set.iterator();
		while(it.hasNext()){
			String childWindowID=it.next();
			if(!parentWindowID.equalsIgnoreCase(childWindowID)){
				driver.switchTo().window(childWindowID);
				String currentUrl = driver.getCurrentUrl();
				driver.close();
				driver.switchTo().window(parentWindowID);
				return currentUrl;
			}
		}
		return null;
	}


	public String getSelectedHighlightLinkName() {
		driver.waitForElementPresent(SELECTED_HIGHLIGHT_LINK);
		String linkName = driver.findElement(SELECTED_HIGHLIGHT_LINK).getText();
		logger.info("Selected And highlight link: "+linkName);
		return linkName;
	}

	public void clickDetailsLinkUnderProgramsIncentivePage(String sectionName) {
		driver.quickWaitForElementPresent(By.xpath(String.format(detailLinkOnProgramIncentivePageLoc,sectionName)));
		driver.click(By.xpath(String.format(detailLinkOnProgramIncentivePageLoc,sectionName)));
		logger.info("detail link clicked for section :"+sectionName);

	}

	public boolean verifyEssentialsRegimenSections(String sublinkName){
		driver.quickWaitForElementPresent(By.xpath(String.format(essentialsRegimenSubLinks, sublinkName)));
		return driver.IsElementVisible(driver.findElement(By.xpath(String.format(essentialsRegimenSubLinks, sublinkName))));
	}

	public boolean verifyUserIsRedirectedToRealResultsPage() {
		driver.waitForElementPresent(REAL_RESULTS_PAGE_LOC);
		return driver.isElementPresent(REAL_RESULTS_PAGE_LOC); 
	}

	public boolean verifyUserIsRedirectedToPCPerksPage() {
		driver.waitForElementPresent(PC_PERKS_PAGE_LOC);
		return driver.isElementPresent(PC_PERKS_PAGE_LOC); 
	}

	public boolean verifyUserIsRedirectedToSolutionToolPage() {
		driver.waitForElementPresent(SOLUTION_TOOL_PAGE);
		return driver.isElementPresent(SOLUTION_TOOL_PAGE); 
	}

	public boolean verifyUserIsRedirectedToDigitalProductCataloguePage() {
		driver.waitForElementPresent(DIGITAL_PRODUCT_CATALOGUE);
		return driver.isElementPresent(DIGITAL_PRODUCT_CATALOGUE); 
	}

	public boolean verifyEnhancementsRegimenSections(String sublinkName){
		driver.quickWaitForElementPresent(By.xpath(String.format(enhancementsRegimenSubLinks, sublinkName)));
		return driver.IsElementVisible(driver.findElement(By.xpath(String.format(enhancementsRegimenSubLinks, sublinkName))));
	}

	public void clickSubSectionUnderBusinessSystemTab(String tabName) {
		driver.quickWaitForElementPresent(By.xpath(String.format(businessSystemSubTitleLoc, tabName)));
		driver.click(By.xpath(String.format(businessSystemSubTitleLoc, tabName)));
		logger.info("business System SubTitle selected is: "+tabName);
	}

	public boolean verifyBusinessPresentationSectionUnderEvents() {
		driver.quickWaitForElementPresent(BUSINESS_PRESENTATION_SECTION_LOC);
		return driver.IsElementVisible(driver.findElement(BUSINESS_PRESENTATION_SECTION_LOC));
	}

	public void clickHomeTabBtn(){
		driver.quickWaitForElementPresent(HOME_TAB_LOC);
		driver.click(HOME_TAB_LOC);
		logger.info("Home button clicked");
		driver.waitForPageLoad();
	}

	public void clickFindAConsultantImageLink(){
		driver.quickWaitForElementPresent(FIND_A_CONSULTANT_IMAGE_LOC);
		driver.click(FIND_A_CONSULTANT_IMAGE_LOC);
		logger.info("Find a consultant Image Link Is clicked");
		driver.waitForPageLoad();
	}

	public boolean isStartNowBtnRedirectinToAppropriatePage(String redirectedPageLink){
		driver.waitForPageLoad();
		Set<String> set=driver.getWindowHandles();
		Iterator<String> it=set.iterator();
		String parentWindow=it.next();
		String childWindow=it.next();
		driver.switchTo().window(childWindow);
		boolean status= driver.getCurrentUrl().contains(redirectedPageLink);
		driver.close();
		driver.switchTo().window(parentWindow);
		return status;
	}

	public void clickStartNowBtn(){
		driver.quickWaitForElementPresent(START_NOW_BTN);
		driver.click(START_NOW_BTN);
		logger.info("Start now btn clicked");
		driver.waitForPageLoad();
	}

	public boolean validateWhoWeAreLink(){
		driver.quickWaitForElementPresent(WHO_WE_ARE_LINK_LOC);
		driver.click(WHO_WE_ARE_LINK_LOC);
		logger.info("Who We Are Link clicked");
		driver.waitForPageLoad();
		return driver.getCurrentUrl().contains("About");
	}

	public boolean validateCompanyCareersLink(){
		driver.quickWaitForElementPresent(CARRERS_LINK_LOC);
		driver.click(CARRERS_LINK_LOC);
		logger.info("Company Careers Link clicked");
		driver.waitForPageLoad();
		return driver.getCurrentUrl().contains("Company/careers");
	}

	public boolean validateCompanyPressRoomLink(){
		driver.quickWaitForElementPresent(PRESS_ROOM_LINK_LOC);
		driver.click(PRESS_ROOM_LINK_LOC);
		logger.info("Company Press Room Link clicked");
		driver.waitForPageLoad();
		return driver.getCurrentUrl().contains("Company/PR");
	}

	public boolean validateCompanyContactUsLink(){
		driver.quickWaitForElementPresent(CONTACT_US_LINK_LOC);
		driver.click(CONTACT_US_LINK_LOC);
		logger.info("Company Contact Us Link clicked");
		driver.waitForPageLoad();
		return driver.getCurrentUrl().contains("Company/Contact");
	}

	public boolean validateEnrollNowLinkPresent(){
		driver.quickWaitForElementPresent(ENROLL_NOW_LINK_LOC);
		return driver.isElementPresent(ENROLL_NOW_LINK_LOC);
	}

	public boolean validateMeetOurCommunityLinkPresent(){
		driver.quickWaitForElementPresent(MEET_OUR_COMMUNITY_LINK_LOC);
		return driver.isElementPresent(MEET_OUR_COMMUNITY_LINK_LOC);
	}

	public boolean validateEventsLinkPresent(){
		driver.quickWaitForElementPresent(EVENTS_LINK_LOC);
		return driver.isElementPresent(EVENTS_LINK_LOC);
	}

	public boolean validateIncomeIllustratorLinkPresent(){
		driver.quickWaitForElementPresent(INCOME_ILLUSTRATOR_LINK_LOC);
		return driver.isElementPresent(INCOME_ILLUSTRATOR_LINK_LOC);
	}

	public boolean validateProgramsAndIncentivesLinkPresent(){
		driver.quickWaitForElementPresent(PROGRAMS_INCENTIVES_LINK_LOC);
		return driver.isElementPresent(PROGRAMS_INCENTIVES_LINK_LOC);
	}

	public boolean validateWhyRFLinkPresent(){
		driver.quickWaitForElementPresent(WHY_RF_LINK_LOC);
		return driver.isElementPresent(WHY_RF_LINK_LOC);
	}

	public void clickWhyRFLinkUnderBusinessSystem(){
		driver.quickWaitForElementPresent(WHY_RF_LINK_LOC);
		driver.click(WHY_RF_LINK_LOC);
		logger.info("Why RF Link clicked");
		driver.waitForPageLoad();
	}

	public void clickBusinessKitsUnderWhyRF(){
		driver.quickWaitForElementPresent(BUSINESS_KITS_LINK_LOC);
		driver.click(BUSINESS_KITS_LINK_LOC);
		logger.info("Business Kits Link clicked");
		driver.waitForPageLoad();
	}

	public boolean validateBusinessKitSectionIsDisplayed(){
		driver.quickWaitForElementPresent(BUSINESS_KITS_SECTION_LOC);
		return driver.isElementPresent(BUSINESS_KITS_SECTION_LOC);
	}

	public void clickRedefineYourFutureLinkUnderWhyRF(){
		driver.quickWaitForElementPresent(REDEFINE_YOUR_FUTURE_LINK_LOC);
		driver.click(REDEFINE_YOUR_FUTURE_LINK_LOC);
		logger.info("Redefine Your Future Link clicked");
		driver.waitForPageLoad();
	}

	public boolean validateRedefineYourFuturePageDisplayed(){
		return driver.getCurrentUrl().contains("Business/Redefine");
	}

	public void clickEnrollNowLinkUnderWhyRF(){
		driver.quickWaitForElementPresent(ENROLL_NOW_LINK_UNDER_WHYRF_LOC);
		driver.click(ENROLL_NOW_LINK_UNDER_WHYRF_LOC);
		logger.info("Enroll Now Link clicked");
		driver.waitForPageLoad();
	}


	public boolean validateSearchSponsorPageDisplayed(){
		return driver.getCurrentUrl().contains("NewEnrollment/SearchSponsor");
	}

	public void clickEventsLinkUnderBusinessSystem(){
		driver.quickWaitForElementPresent(EVENTS_LINK_LOC);
		driver.click(EVENTS_LINK_LOC);
		logger.info("Events Link clicked");
		driver.waitForPageLoad();
	}

	public boolean validateUpcomingEventsLinkIsPresent(){
		driver.quickWaitForElementPresent(UPCOMING_EVENTS_LINK_LOC);
		return driver.isElementPresent(UPCOMING_EVENTS_LINK_LOC);
	}

	public boolean validateDisclaimerLinkInFooter(){
		driver.quickWaitForElementPresent(DISCLAIMER_LINK_LOC);
		driver.click(DISCLAIMER_LINK_LOC);
		logger.info("Disclaimer Link clicked");
		driver.waitForPageLoad();
		return driver.getCurrentUrl().contains("Disclaimer");
	}

	public void clickGivingBackLinkUnderAboutRF(){
		driver.quickWaitForElementPresent(GIVING_BACK_LINK_LOC);
		driver.click(GIVING_BACK_LINK_LOC);
		logger.info("Giving Back Link clicked");
		driver.waitForPageLoad();
	}

	public boolean validateCompanyPFCFoundationPageDisplayed(){
		return driver.getCurrentUrl().contains("Company/PFCFoundation/Mission");
	}

	public void clickSolutionToolImageLink(){
		driver.quickWaitForElementPresent(SOLUTION_TOOL_IMAGE_LOC);
		driver.click(SOLUTION_TOOL_IMAGE_LOC);
		logger.info("Solution tool clicked under Home tab button");
		driver.waitForPageLoad();
	}

	public void clickRFInTheNewsImageLink(){
		driver.quickWaitForElementPresent(RF_IN_THE_NEWS_IMAGE_LOC);
		driver.click(RF_IN_THE_NEWS_IMAGE_LOC);
		logger.info("R+F In the news Image Link Is clicked");
		driver.waitForPageLoad();
	}

	public void loginAsUserOnCheckoutPage(String username,String password){
		driver.waitForElementPresent(USERNAME_TXTFLD_CHECKOUT_PAGE_LOC);
		driver.type(USERNAME_TXTFLD_CHECKOUT_PAGE_LOC, username);
		driver.click(PASSWORD_TXTFLD_CHECKOUT_PAGE_LOC);
		driver.type(PASSWORD_TXTFLD_CHECKOUT_PAGE_LOC,password);  
		logger.info("login username is "+username);
		logger.info("login password is "+password);
		driver.click(SIGN_IN_BTN_CHECKOUT_PAGE_LOC);
		logger.info("Sign In button clicked");
		driver.waitForPageLoad();
	}

	public void enterBillingInfo(String billingName, String firstName,String lastName,String cardName,String cardNumer,String month,String year,String addressLine1,String postalCode,String phnNumber){
		driver.type(BILLING_NAME_FOR_BILLING_PROFILE, billingName);
		logger.info("Billing profile name entered as: "+billingName);
		driver.type(ATTENTION_FIRST_NAME, firstName);
		driver.type(BILLING_FIRST_NAME, firstName);
		logger.info("Attention first name entered as: "+firstName);
		driver.type(ATTENTION_LAST_NAME, lastName);
		driver.type(BILLING_LAST_NAME, lastName);
		logger.info("Attention last name entered as: "+lastName);
		driver.type(NAME_ON_CARD, cardName);
		logger.info("Card Name entered as: "+cardName);
		driver.type(CREDIT_CARD_NUMBER_INPUT_FIELD, cardNumer);
		logger.info("Card number entered as: "+cardNumer);
		driver.click(EXPIRATION_DATE_MONTH_DD);
		logger.info("Expiration month dropdown clicked");
		driver.click(By.xpath(String.format(expiryMonthLoc, month)));
		logger.info("Expiry month selected is: "+month);
		driver.click(EXPIRATION_DATE_YEAR_DD);
		logger.info("Expiration year dropdown clicked");
		driver.click(By.xpath(String.format(expiryYearLoc, year)));
		logger.info("Expiry year selected is: "+year);
		driver.type(ADDRESS_LINE_1, addressLine1);
		logger.info("Billing street address entered as: "+addressLine1);
		driver.type(ZIP_CODE, postalCode+"\t");
		logger.info("Postal code entered as: "+postalCode);
		driver.waitForStorfrontLegacyLoadingImageToDisappear();
		driver.click(CITY_DD);
		/*		logger.info("City dropdown clicked");
		driver.click(FIRST_VALUE_OF_CITY_DD);*/
		logger.info("City selected");
		driver.type(CITY_DD, "Fremont");
		logger.info("City Selected");
		driver.type(PHONE_NUMBER_BILLING_PROFILE_PAGE,phnNumber);
		logger.info("Phone number entered as: "+phnNumber);
	}

	public void clickRenewLater(){
		try {
			driver.click(RENEW_LATER_LINK);
			driver.waitForPageLoad();
		} catch (Exception e) {

		}
	}


	public boolean isRegimenNamePresent(String regimen){
		regimen = regimen.toUpperCase();
		driver.quickWaitForElementPresent(By.xpath(String.format(regimenNameOnPwsLoc, regimen)));
		return driver.isElementPresent(By.xpath(String.format(regimenNameOnPwsLoc, regimen)));
	}

	public void clickRegimenOnPWS(String regimen){
		driver.quickWaitForElementPresent(By.xpath(String.format(regimenImageOnPwsLoc, regimen)));
		driver.click(By.xpath(String.format(regimenImageOnPwsLoc, regimen)));
		logger.info("Regimen selected is: "+regimen);
	}

	public void clickForgotPasswordLinkOnBizHomePage(){
		driver.quickWaitForElementPresent(FORGOT_PASSWORD_PWS_LINK_LOC);
		driver.click(FORGOT_PASSWORD_PWS_LINK_LOC);
		logger.info("Forgot Password Link clicked");
		driver.waitForPageLoad();
	}

	public boolean validateChangePasswordMessagePrompt(){
		driver.quickWaitForElementPresent(CHANGE_PASSWORD_TEXT_LOC);
		return driver.isElementPresent(CHANGE_PASSWORD_TEXT_LOC);
	}

	public void enterEmailAddressToRecoverPassword(String emailID){
		driver.quickWaitForElementPresent(EMAIL_ADDRESS_FIELD_LOC);
		driver.type(EMAIL_ADDRESS_FIELD_LOC, emailID);
		logger.info("email address entered to recover password is "+emailID);
	}


	public void clickSendEmailButton(){
		driver.quickWaitForElementPresent(SEND_EMAIL_BTN_LOC);
		driver.click(SEND_EMAIL_BTN_LOC);
		logger.info("Send Email Button clicked");
		driver.waitForPageLoad();
	}

	public boolean validatePasswordChangeAndEmailSent(){
		Alert alt=driver.switchTo().alert();
		boolean status= alt.getText().contains("resetting your password");
		alt.accept();
		return status;
	}

	public void clickHeaderLinkAfterLogin(String linkName) {
		try{
			driver.quickWaitForElementPresent(By.xpath(String.format(myAccountLinkAfterLoginLink, linkName)));
			driver.click(By.xpath(String.format(myAccountLinkAfterLoginLink, linkName)));
			logger.info("my account link is clicked");
		}
		catch(NoSuchElementException e){
			driver.quickWaitForElementPresent(By.xpath("//*[@id='ConsultantWelcome']/ul/li[1]/a"));
			driver.click(By.xpath("//*[@id='ConsultantWelcome']/ul/li[1]/a"));
			logger.info("my account link is clicked");
		}
	}

	public void clickEditOrderLink(){
		driver.quickWaitForElementPresent(EDIT_ORDER_UNDER_MY_ACCOUNT_LOC);
		driver.click(EDIT_ORDER_UNDER_MY_ACCOUNT_LOC);
		logger.info("edit order link is clicked"); 
	}

	public void clickChangeLinkUnderShippingToOnPWS(){
		driver.quickWaitForElementPresent(CHANGE_LINK_FOR_SHIPPING_INFO_ON_PWS);
		driver.click(CHANGE_LINK_FOR_SHIPPING_INFO_ON_PWS);
		logger.info("Change Link under shipping to clicked");
		driver.waitForPageLoad();
	}

	public void enterShippingProfileDetailsForPWS(String addressName, String firstName,String lastName,String addressLine1,String postalCode,String phnNumber){
		driver.type(ADDRESS_NAME_FOR_SHIPPING_PROFILE, addressName);
		logger.info("Address name entered as: "+addressName);
		driver.type(ATTENTION_FIRST_NAME, firstName);
		logger.info("Attention first name entered as: "+firstName);
		driver.type(ATTENTION_LAST_NAME, lastName);
		logger.info("Attention last name entered as: "+lastName);
		driver.type(ADDRESS_LINE_1, addressLine1);
		logger.info("Address line 1 entered as: "+addressLine1);
		driver.type(ZIP_CODE, postalCode+"\t");
		driver.waitForStorfrontLegacyLoadingImageToDisappear();
		logger.info("Postal code entered as: "+postalCode);
		driver.waitForElementPresent(CITY_DD);
		driver.click(CITY_DD);
		driver.click(FIRST_VALUE_OF_CITY_DD);
		logger.info("First value of City selected");
		driver.waitForElementPresent(COUNTRY_DD);
		driver.click(COUNTRY_DD);
		logger.info("Country dropdown clicked");
		driver.click(FIRST_VALUE_OF_COUNTRY_DD);
		logger.info("First value of Country selected");
		driver.type(PHONE_NUMBER_SHIPPING_PROFILE_PAGE,phnNumber);
		logger.info("Phone number entered as: "+phnNumber);
	}

	public String getShippingAddressName(){
		String name = driver.findElement(SHIPPING_ADDRESS_NAME_LOC).getText();
		logger.info("Fetched shipping address name is: "+name);
		return name;
	}

	public void clickUseThisAddressShippingInformationBtn(){
		driver.quickWaitForElementPresent(USE_THIS_ADDRESS_SHIPPING_INFORMATION);
		driver.click(USE_THIS_ADDRESS_SHIPPING_INFORMATION);
		logger.info("Use this Address shipping information clicked");
		driver.waitForPageLoad();
	}

	public void clickEnrollNowBtnAtWhyRFPage(){
		driver.waitForElementPresent(ENROLL_NOW_LINK);
		driver.click(ENROLL_NOW_LINK);
		logger.info("Enroll now button clicked");
		driver.waitForPageLoad();
	}

	public void selectConsultantEnrollmentKitByPrice(String price) {
		//driver.pauseExecutionFor(2000);
		//*** Price became unstable for selection, opting for Javascript
		JavascriptExecutor js = (JavascriptExecutor) RFWebsiteDriver.driver;
		js.executeScript("selectKit(269)");
		driver.waitForPageLoad();
	}

	public void selectRegimenForConsultant(String regimen){
		regimen = regimen.toUpperCase();
		driver.waitForElementPresent(By.xpath(String.format(consultantRegimenLoc, regimen)));
		driver.click(By.xpath(String.format(consultantRegimenLoc, regimen)));
		logger.info("Regimen selected is: "+regimen);
	}

	public void clickNextBtnAfterSelectRegimen(){
		driver.click(REGIMEN_NEXT_BTN_LOC);
		logger.info("Regimen Next button clicked");
	}

	public String getBizPWSBeforeEnrollment(){
		driver.waitForElementPresent(WEBSITE_PREFIX_BIZ_PWS);
		String bizPWS = driver.findElement(WEBSITE_PREFIX_BIZ_PWS).getText().split("\\ ")[0];
		logger.info("Biz PWS before enrollment is: "+bizPWS);
		return bizPWS;
	}

	public void clickChangeMyPcPerksStatus() {
		driver.waitForElementPresent(CHANGE_MY_PC_PERKS_STATUS_UNDER_MYACCOUNT_LINK);
		driver.click(CHANGE_MY_PC_PERKS_STATUS_UNDER_MYACCOUNT_LINK);
		logger.info("change my pc perks status link is clicked");

	}

	public void clickDelayCancelPcPerksLink() {
		driver.quickWaitForElementPresent(DELAY_OR_CANCEL_PC_PERKS_LINK); 
		driver.click(DELAY_OR_CANCEL_PC_PERKS_LINK);
		logger.info("delay or cancel pc perks link is clicked");
	}

	public void clickPopUpYesChangeMyAutoshipDate() {
		driver.waitForElementPresent(YES_CHANGE_MY_AUTOSHIP_DATE_BTN);
		driver.click(YES_CHANGE_MY_AUTOSHIP_DATE_BTN);
		logger.info("yes change my autoship date is clicked");

	}

	public void clickChangeAutohipDateBtn() {
		driver.waitForElementPresent(CHANGE_MY_AUTOSHIP_DATE_BTN);
		driver.click(CHANGE_MY_AUTOSHIP_DATE_BTN);
		logger.info("change my date btn is clicked");
	}

	public boolean verifyConfirmationMessage() {
		return driver.isElementPresent(CONFIRMATION_MSG_LOC);
	}

	public void clickBackToMyAccountBtn() {
		driver.quickWaitForElementPresent(BACK_TO_MY_ACCOUNT_BTN_LOC);
		driver.click(BACK_TO_MY_ACCOUNT_BTN_LOC);
		logger.info("back to my account button is clicked");

	}

	public boolean verifyConfirmationMessageInOrders() {
		return driver.isElementPresent(CONFIRM_MSG_AT_ORDERS_LOC);
	}

	public void selectSecondRadioButton() {
		driver.click(RADIO_BUTTON_FOR_SIXTY_DAYS_LOC);
		logger.info("radio button for 60 days is selected");
	}

	public void clickEditOrderbtn() {
		driver.click(EDIT_ORDER_BTN_LOC);
		logger.info("edit order button is clicked");

	}

	public void clickRemoveLinkAboveTotal() {
		int numberofRemoveLink = driver.findElements(TOTAL_ITEM_LOC).size();
		for(int i =1;i<=numberofRemoveLink;i++ ){

			driver.click(REMOVE_LINK_LOC);
			driver.waitForStorfrontLegacyLoadingImageToDisappear();
		}
		logger.info("all products Removed"); 
	}

	public void clickAddToCartBtnForLowPriceItems() {
		int numberofAddToCartLink = driver.findElements(ADD_TO_CART_LINK_LOC).size();
		for(int i = 1;i<=numberofAddToCartLink;i++){
			String LowerPrice = driver.findElement(By.xpath(String.format(retailPriceOfItem,i))).getText();
			String []split = LowerPrice.split("\\$")[1].split("\\.");
			int value = Integer.parseInt(split[0]);
			if(value<50){
				driver.click(By.xpath(String.format(addToCartBtnLoc,i)));
				driver.waitForStorfrontLegacyLoadingImageToDisappear();
				break;
			}else
				continue;
		}
	}

	public void clickAddToCartBtnForHighPriceItems() {
		int numberofAddToCartLink = driver.findElements(ADD_TO_CART_LINK_LOC).size();
		for(int i = 1;i<=numberofAddToCartLink;i++){
			String highPrice = driver.findElement(By.xpath(String.format(retailPriceOfItem,i))).getText();
			String []split = highPrice.split("\\$")[1].split("\\.");
			int value = Integer.parseInt(split[0]);
			if(value>80){
				driver.click(By.xpath(String.format(addToCartBtnLoc,i)));
				driver.waitForStorfrontLegacyLoadingImageToDisappear();
				break;
			}else
				continue;
		}
	}

	public boolean isStatusMessageDisplayed() {
		return driver.findElement(STATUS_MSG_LOC).isDisplayed();
	}

	public void clickOnUpdateOrderBtn() {
		driver.click(UPDATE_ORDER_BTN_LOC);
		logger.info("update order button is clicked");
	}

	public void handleAlertAfterUpdateOrder() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			//exception handling
		}
	}

	public String getConfirmationMessage() {
		driver.waitForElementPresent(MSG_UPDATE_ORDER_CONFIRMATION_LOC);
		String confirmationMessage = driver.findElement(MSG_UPDATE_ORDER_CONFIRMATION_LOC).getText();
		System.out.println(confirmationMessage+"confirmatin mssge");
		return confirmationMessage;

	}

	public void clickSectionUnderReplenishmentOrderManagement(String subSectionName) {
		driver.quickWaitForElementPresent(By.xpath(String.format(sectionUnderReplenishmentOrderManagementLoc, subSectionName)));
		driver.click(By.xpath(String.format(sectionUnderReplenishmentOrderManagementLoc, subSectionName)));
		logger.info("subSection"+subSectionName+" link is clicked");

	}

	public String getOrderTotal() {
		return driver.findElement(ORDER_TOTAL_LOC).getText();
	}

	public String getOrderTotalAtOverview() {
		String totalAtOverview =  driver.findElement(ORDER_TOTAL_AT_OVERVIEW_LOC).getText();
		return totalAtOverview;
	}

	public String getSplittedPrefixFromConsultantUrl(String url) {
		String requiredPrefix[] = url.split("\\//")[1].split("\\.");
		return requiredPrefix[0];
	}

	public boolean isExistingPrefixAvailable() {
		driver.waitForElementPresent(PREFIX_SUGGESTIONS_LIST);
		return driver.isElementPresent(PREFIX_SUGGESTIONS_LIST);
	}

	public void clickChangeBillingInformationLinkUnderBillingTabOnPWS(){
		driver.quickWaitForElementPresent(CHANGE_BILLING_INFO_LINK_ON_PWS);
		driver.click(CHANGE_BILLING_INFO_LINK_ON_PWS);
		logger.info("Change Billing info link clicked");
		driver.waitForPageLoad();
	}
	public void enterBillingInfoForPWS(String billingName, String firstName,String lastName,String cardName,String cardNumer,String month,String year,String addressLine1,String postalCode,String phnNumber){
		driver.type(BILLING_NAME_FOR_BILLING_PROFILE, billingName);
		logger.info("Billing profile name entered as: "+billingName);
		driver.type(ATTENTION_FIRST_NAME, firstName);
		driver.type(BILLING_FIRST_NAME, firstName);
		logger.info("Attention first name entered as: "+firstName);
		driver.type(ATTENTION_LAST_NAME, lastName);
		driver.type(BILLING_LAST_NAME, lastName);
		logger.info("Attention last name entered as: "+lastName);
		driver.type(NAME_ON_CARD, cardName);
		logger.info("Card Name entered as: "+cardName);
		driver.type(CREDIT_CARD_NUMBER_INPUT_FIELD, cardNumer);
		logger.info("Card number entered as: "+cardNumer);
		driver.click(EXPIRATION_DATE_MONTH_DD);
		logger.info("Expiration month dropdown clicked");
		driver.click(By.xpath(String.format(expiryMonthLoc, month)));
		logger.info("Expiry month selected is: "+month);
		driver.click(EXPIRATION_DATE_YEAR_DD);
		logger.info("Expiration year dropdown clicked");
		driver.click(By.xpath(String.format(expiryYearLoc, year)));
		logger.info("Expiry year selected is: "+year);
		driver.type(ADDRESS_LINE_1, addressLine1);
		logger.info("Billing street address entered as: "+addressLine1);
		driver.type(ZIP_CODE, postalCode+"\t");
		logger.info("Postal code entered as: "+postalCode);
		driver.waitForStorfrontLegacyLoadingImageToDisappear();
		driver.click(CITY_DD);
		logger.info("City dropdown clicked");
		driver.click(FIRST_VALUE_OF_CITY_DD);
		logger.info("City selected");
		driver.type(PHONE_NUMBER_BILLING_PROFILE_PAGE,phnNumber);
		logger.info("Phone number entered as: "+phnNumber);
	}
	public String getOrderBillingDetailsUpdateMessage(){
		driver.waitForElementPresent(EDIT_ORDER_BILLING_DETAILS_UPDATE_MESSAGE);
		String messgae = driver.findElement(EDIT_ORDER_BILLING_DETAILS_UPDATE_MESSAGE).getText();
		logger.info("Order updation message is: "+messgae);
		return messgae;
	}

	public String getModifiedPWSValue(String url,String availablityText) {
		String requiredPrefix[] = url.split("\\//")[1].split("\\/");
		String siteprefixToAssert = requiredPrefix[0]+availablityText;
		return siteprefixToAssert;
	}

	public String getModifiedEmailValue(String url) {
		String requiredPrefix[] = url.split("\\//")[1].split("\\.");
		String siteprefixToAssert = requiredPrefix[0]+"@"+requiredPrefix[1]+".com";
		return siteprefixToAssert;
	}

	public void enterUserPrefixInPrefixField(String prefixField){
		driver.quickWaitForElementPresent(WEBSITE_PREFIX_LOC);
		driver.clear(WEBSITE_PREFIX_LOC);
		driver.type(WEBSITE_PREFIX_LOC, prefixField);
		logger.info("Prefix field enterd as: "+prefixField);
	}

	public boolean isInvalidLoginPresent(){
		driver.waitForElementPresent(INVALID_LOGIN);
		return driver.isElementPresent(INVALID_LOGIN);
	}

	public void enterEmailAddress(String emailAddress){
		driver.waitForElementPresent(ACCOUNT_EMAIL_ADDRESS_LOC);
		driver.type(ACCOUNT_EMAIL_ADDRESS_LOC, emailAddress+"\t");
		logger.info("email address entered as: "+emailAddress);
	}

	public void clickSetUpAccountNextButton(){
		driver.quickWaitForElementPresent(SETUP_ACCOUNT_NEXT_BTN_LOC);
		driver.click(SETUP_ACCOUNT_NEXT_BTN_LOC);
		logger.info("set up account next button is clicked");
	}

	public boolean validateExistingPCPopUp(String emailAddress){
		driver.quickWaitForElementPresent(EXISTING_PC_LOC);
		driver.type(ACCOUNT_EMAIL_ADDRESS_LOC, emailAddress);
		logger.info("email address entered as: "+emailAddress);
		driver.type(ACCOUNT_PASSWORD_LOC, "");
		logger.info("password entered as: "+"");
		return driver.isElementPresent(EXISTING_PC_LOC);
	}

	public boolean validateExistingRCPopUp(String emailAddress){
		driver.quickWaitForElementPresent(EXISTING_RC_LOC);
		driver.type(ACCOUNT_EMAIL_ADDRESS_LOC, emailAddress);
		logger.info("email address entered as: "+emailAddress);
		driver.type(ACCOUNT_PASSWORD_LOC, "");
		logger.info("password entered as: "+"");
		return driver.isElementPresent(EXISTING_RC_LOC);
	}

	public void enterSpecialCharacterInWebSitePrefixField(String prefixField){
		driver.quickWaitForElementPresent(WEBSITE__PREFIX_LOC);
		driver.clear(WEBSITE__PREFIX_LOC);
		driver.type(WEBSITE__PREFIX_LOC, prefixField);
		logger.info("PWS enterd as: "+prefixField);
	}

	public String getWebSitePrefixFieldText(){
		driver.quickWaitForElementPresent(WEBSITE__PREFIX_LOC);
		return driver.findElement(WEBSITE__PREFIX_LOC).getText();
	}

	public void clickEditMyPhotoLink(){
		driver.waitForElementPresent(EDIT_MY_PHOTO_LINK);
		driver.click(EDIT_MY_PHOTO_LINK);
		logger.info("Edit my photo link clicked");
		driver.waitForPageLoad();
	}

	public void clickCancelEnrollmentBtn(){
		driver.waitForElementPresent(CANCEL_ENROLLMENT_BTN);
		driver.click(CANCEL_ENROLLMENT_BTN);
		logger.info("Cancel enrollment button clicked");
		driver.waitForPageLoad();
	}

	public void clickSendEmailToResetMyPassword(){
		driver.waitForElementPresent(SEND_EMAIL_TO_RESET_MY_PASSWORD_BTN);
		driver.click(SEND_EMAIL_TO_RESET_MY_PASSWORD_BTN);
		logger.info("Send email to reset my password button clicked");
		driver.waitForPageLoad();
	}

	public boolean isUploadANewPhotoButtonPresent(){
		driver.waitForElementPresent(UPLAOD_A_NEW_PHOTO_BTN);
		return driver.isElementPresent(UPLAOD_A_NEW_PHOTO_BTN);
	}

	public boolean isExistingConsultantPopupPresent(){
		driver.waitForElementPresent(EXISTING_CONSULTANT_LOC);
		return driver.isElementPresent(EXISTING_CONSULTANT_LOC);
	}

	public boolean isEmailVerificationTextPresent(){
		driver.waitForElementPresent(EMAIL_VERIFICATION_TEXT);
		return driver.isElementPresent(EMAIL_VERIFICATION_TEXT);
	}

	public boolean verifyLinkPresentUnderMyAccount(String linkName) {
		return driver.isElementPresent(By.xpath(String.format(linkUnderMyAccount, linkName)));
	}

	public int clickViewDetailsForOrderAndReturnOrderNumber() {
		driver.waitForElementPresent(TOTAL_ROWS_ON_ORDER_HISTORY_PAGE);
		int totalRowsSize=driver.findElements(TOTAL_ROWS_ON_ORDER_HISTORY_PAGE).size();
		logger.info("Total rows on order history page: "+totalRowsSize);
		if(totalRowsSize<1){
			return 0;
		}else{
			int randomOrderFromSearchResult = CommonUtils.getRandomNum(1, totalRowsSize);
			logger.info("Random Number created is: "+randomOrderFromSearchResult);
			String orderNumber = driver.findElement(By.xpath(String.format(orderNumberOnOrderHistoryPage, randomOrderFromSearchResult))).getText();
			driver.click(By.xpath(String.format(viewDetailsOnOrderHistoryPage, randomOrderFromSearchResult)));
			logger.info("View Order details link is clicked for order :"+orderNumber);
			return Integer.parseInt(orderNumber);
		}
	}

	public boolean isOrderDetailsPopupPresent(){
		driver.waitForElementPresent(ORDER_DETAILS_POPUP);
		return driver.isElementPresent(ORDER_DETAILS_POPUP);
	}

	public void  clickCloseOfOrderDetailsPopup(){
		driver.waitForElementPresent(CLOSE_OF_ORDER_DETAILS_POPUP);
		driver.click(CLOSE_OF_ORDER_DETAILS_POPUP);
		logger.info("Order details popup closed.");
	}

	public String clickAndReturnPWSFromFindConsultantPage(){
		driver.quickWaitForElementPresent(PWS_TXT_ON_FIND_CONSULTANT_PAGE);
		String fetchPWS=driver.findElement(PWS_TXT_ON_FIND_CONSULTANT_PAGE).getText();
		logger.info("Fetched PWS from find a consultant page is"+fetchPWS);
		driver.click(PWS_TXT_ON_FIND_CONSULTANT_PAGE);
		logger.info("PWS "+fetchPWS+" is clicked.");
		driver.waitForPageLoad();
		return fetchPWS;
	}

	public void clickConnectWithAConsultant(){
		driver.waitForElementPresent(CONNECT_WITH_A_CONSULTANT);
		driver.click(CONNECT_WITH_A_CONSULTANT);
		logger.info("Connect with a consultant");
	}

	public void clickClickhereLinkToLearnDirectSelling(){
		driver.quickWaitForElementPresent(CLICK_HERE_TO_LEARN_MORE_ABOUT_DIRECT_SELLING);
		driver.click(CLICK_HERE_TO_LEARN_MORE_ABOUT_DIRECT_SELLING);
		logger.info("Click here link clicked");
		logger.info("Redirect to direct selling page");
		driver.waitForPageLoad();
		driver.pauseExecutionFor(3000);
	}

	public void clickCompanyPressRoomLink(){
		driver.quickWaitForElementPresent(PRESS_ROOM);
		driver.click(PRESS_ROOM);
		logger.info("Company Press Room Link clicked");
		driver.waitForPageLoad();
	}


}