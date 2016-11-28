package com.rf.pages.website.cscockpit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

public class CSCockpitCheckoutTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitCheckoutTabPage.class.getName());

	private static String selectMonthDDLoc = "//td[text()='%s']";
	private static String selectYearDDLoc = "//td[text()='%s']";
	private static String selectCardTypeDDLoc = "//td[text()='%s']";
	private static String firstName = "//span[contains(text(),'%s')]";
	private static String totalsSectionValues = "//span[contains(text(),'%s')]/following::span[1]";
	private static String sooPopupvalues = "//span[contains(text(),'%s')]";
	private static String sooValues = "//div[contains(text(),'%s')]";
	private static String sooDDValues = "//td[contains(text(),'%s')]";

	private static final By CREDIT_CARD = By.xpath("//div[contains(text(),'************')]");
	private static final By DELIVERY_MODE = By.xpath("//span[contains(text(),'Delivery Mode')]/following::option[@selected='selected']");
	private static final By COMMISSION_DATE = By.xpath("//span[contains(text(),'Commission Date')]/following::input[@autocomplete ='off']");
	private static final By PLACE_ORDER_BUTTON_CHECKOUT_TAB = By.xpath("//td[contains(text(),'Place Order')]");
	private static final By SELECT_PAYMENT_DETAILS_POPUP = By.xpath("//span[contains(text(),'Please select payment details')]");
	private static final By PAYMENT_DETAILS_POPUP_OK_BTN = By.xpath("//td[text()='OK']");
	private static final By CVV2_SEARCH_TXT_FIELD = By.xpath("//div[contains(text(),'Action')]/following::td[contains(text(),'Use this card')][1]/preceding::input[1]");
	private static final By USE_THIS_CARD_BTN = By.xpath("//span[contains(text(),'Stored Credit Cards')]/following::div[@class='z-listbox-body']//tbody[2]/tr[1]//td[contains(text(),'Use this card')]");
	private static final By TEST_ORDER_CHKBOX = By.xpath("//label[contains(text(),'Test Order')]/preceding::input[1]");
	private static final By DO_NOT_SHIP_CHKBOX = By.xpath("//label[contains(text(),'Do-not-ship')]/preceding::input[1]");
	private static final By UPDATE_BUTTON_LOC = By.xpath("//td[contains(text(),'UPDATE')]");
	private static final By CV2_TEXT_FIELD_LOC = By.xpath("//input[@class='z-textbox']");
	private static final By USE_THIS_CARD_BUTTON_LOC = By.xpath("//td[contains(text(),'Use this card')]");
	private static final By CREDIT_CARD_VALIDATION_FAILED_OK_LOC = By.xpath("//td[contains(text(),'OK')]");
	private static final By ADDRESS_LINE_TEXT_BOX = By.xpath("//span[text()='Line 1']/following::input[1]");
	private static final By POSTAL_CODE_TEXT_BOX = By.xpath("//span[text()='Postal Code']/following::input[1]");
	private static final By CLOSE_POPUP_OF_PAYMENT_ADDRESS = By.xpath("//div[contains(text(),'ADD NEW PAYMENT PROFILE')]/div[contains(@id,'close')]");
	private static final By POPUP_SAVE_BUTTON = By.xpath("//td[text()='SAVE']");
	private static final By CLOSE_POPUP_OF_DELIVERY_ADDRESS = By.xpath("//div[contains(text(),'Create Delivery Address')]/div[contains(@id,'close')]");
	private static final By DELIVERY_ADDRESS_POPUP_SAVE_BUTTON = By.xpath("//td[text()='Create new address']");
	private static final By DELIVERY_ADDRESS_POPUP_ERROR_TEXT = By.xpath("//div[contains(text(),'ZK')]/following::span[1]");
	private static final By DELIVERY_ADDRESS_POPUP_OKAY_BUTTON = By.xpath("//td[text()='OK']");
	private static final By USE_AS_ENTERED_POPUP = By.xpath("//td[contains(text(),'Use Entered Address >>')]");
	private static final By DELIVERY_ADDRESS_DROPDOWN = By.xpath("//select[contains(@class,'DeliveryAddressList')]");
	private static final By DELIVERY_ADDRESS_DROPDOWN_VALUE = By.xpath("//select[contains(@class,'DeliveryAddressList')]/option[1]");
	private static final By ATTENDENT_NAME_TEXT_BOX = By.xpath("//span[text()='Attention']/following::input[1]");
	private static final By CITY_TOWN_TEXT_BOX = By.xpath("//span[text()='City/Town']/following::input[1]");
	private static final By POSTAL_TEXT_BOX = By.xpath("//span[text()='Postal Code']/following::input[1]");
	private static final By COUNTRY_TEXT_BOX = By.xpath("//span[text()='Country']/following::input[1]");
	private static final By PROVINCE_TEXT_BOX = By.xpath("//span[text()='State/Province']/following::input[1]");
	private static final By PHONE_TEXT_BOX = By.xpath("//span[text()='Phone1']/following::input[1]");
	private static final By POPUP_CARD_NUMBER_TEXT_BOX = By.xpath("//span[text()='Card Number']/following::input[1]");
	private static final By POPUP_NAME_ON_CARD_TEXT_BOX = By.xpath("//span[text()='Name on card']/following::input[1]");
	private static final By POPUP_SECURITY_CODE_TEXT_BOX = By.xpath("//span[text()='Security Code']/following::input[1]");
	private static final By POPUP_BILLING_PROFILE_MONTH_DD = By.xpath("//span[text()='Expiration Date']/../../following-sibling::td[1]/div/span/span[1]/span/img");
	private static final By POPUP_BILLING_PROFILE_YEAR_DD = By.xpath("//span[text()='Expiration Date']/../../following-sibling::td[1]/div/span/span[2]/span/img");
	private static final By POPUP_BILLING_PROFILE_CARD_TYPE_DD = By.xpath("//span[text()='Card Type']/../../following-sibling::td[1]/div/span/span[1]/span/img");
	private static final By BASE_PRICE = By.xpath("//span[contains(text(),'Base Price')]");
	private static final By TOTAL_PRICE = By.xpath("//span[contains(text(),'Total Price')]");
	private static final By ENTRY_CV = By.xpath("//span[contains(text(),'Entry CV')]");
	private static final By ENTRY_QV = By.xpath("//span[contains(text(),'Entry QV')]");
	private static final By SELECTED_DELIVERY_ADDRESS = By.xpath("//select[contains(@class,'csDeliveryAddressList')]//option[@selected='selected']");
	private static final By NEW_ADDRESS_IN_DELIVERY_SECTION = By.xpath("//td[contains(text(),'New Address')]");
	private static final By SUBTOTAL_TXT_TOTALS_SECTION = By.xpath("//span[contains(text(),'Subtotal')]");
	private static final By DISCOUNT_TXT_TOTALS_SECTION = By.xpath("//span[contains(text(),'Discount')]");
	private static final By DELIVERY_COSTS_TXT_TOTALS_SECTION = By.xpath("//span[contains(text(),'Delivery Costs')]");
	private static final By HANDLING_COSTS_TXT_TOTALS_SECTION = By.xpath("//span[contains(text(),'Handling Costs')]");
	private static final By TAXES_TXT_TOTALS_SECTION = By.xpath("//span[contains(text(),'Taxes')]");
	private static final By TOTAL_PRICE_TXT_TOTALS_SECTION = By.xpath("//span[contains(text(),'Total Price')]");
	private static final By TOTAL_CV_TXT_TOTALS_SECTION = By.xpath("//span[contains(text(),'Total CV')]");
	private static final By TOTAL_QV_TXT_TOTALS_SECTION = By.xpath("//span[contains(text(),'Total QV')]");
	private static final By COMMISSION_DATE_TXT = By.xpath("//span[contains(text(),'Commission Date')]");
	private static final By ORDER_NOTES_TXT_LOCATOR = By.xpath("//span[contains(text(),'Order Notes')]");
	private static final By ORDER_NOTE_ADD_BTN = By.xpath("//span[contains(text(),'Order Notes')]/following::td[text()='ADD'][1]");
	private static final By NO_PROMOTION_TXT_LOCATOR = By.xpath("//span[contains(text(),'Applied Promotions')]");
	private static final By CREDIT_CARD_NUMBER_TXT_LOCATOR = By.xpath("//div[contains(text(),'Credit Card number')]");
	private static final By CREDIT_CARD_OWNER_TXT_LOCATOR = By.xpath("//div[contains(text(),'Credit Card Owner')]");
	private static final By CREDIT_CARD_TYPE_TXT_LOCATOR = By.xpath("//div[contains(text(),'Type')]");
	private static final By CREDIT_CARD_MONTH_TXT_LOCATOR = By.xpath("//div[contains(text(),'Month')]");
	private static final By CREDIT_CARD_VALID_TO_YEAR_TXT_LOCATOR = By.xpath("//div[contains(text(),'Valid to year')]");
	private static final By PAYMENT_BILLING_ADDRESS_TXT_LOCATOR = By.xpath("//div[contains(text(),'Billing address')]");
	private static final By PAYMENT_AMOUNT_TXT_LOCATOR = By.xpath("//div[contains(text(),'Amount')]");
	private static final By CREDIT_CARD_CV2_TXT_LOCATOR = By.xpath("//div[contains(text(),'Cv2')]");
	private static final By PERFORM_SOO_BTN_LOCATOR = By.xpath("//div[contains(text(),'Cv2')]");
	private static final By PERFROM_SOO_BUTTON_LOC = By.xpath("//td[text()='PERFORM SOO']");
	private static final By PERFORM_SOO_POPUP_PRICE_LOC = By.xpath("//div[@class='soo-popup-items']//td[4]//input");
	private static final By PERFORM_SOO_POPUP_CV_LOC = By.xpath("//div[@class='soo-popup-items']//td[6]//input");
	private static final By PERFORM_SOO_POPUP_QV_LOC = By.xpath("//div[@class='soo-popup-items']//td[7]//input");
	private static final By PERFORM_SOO_POPUP_DC_LOC = By.xpath("//div[@class='order-totals']//div[3]//input");
	private static final By PERFORM_SOO_POPUP_HC_LOC = By.xpath("//div[@class='order-totals']//div[4]//input");
	private static final By PERFORM_SOO_OVERRIDE_REASON_DEPT_DD_LOC = By.xpath("//span[@class='soo-popup-bottom-left']//div[2]//img");
	private static final By SELECT_OVERRIDE_REASON_DEPT_LOC = By.xpath("//div[@class='z-combobox-pp']//td[contains(text(),'IT')]");
	private static final By PERFORM_SOO_OVERRIDE_REASON_SOO_TYPE_DD_LOC = By.xpath("//span[@class='soo-popup-bottom-left']//div[3]//img");
	private static final By SELECT_OVERRIDE_REASON_TYPE_AS_SALES_LOC = By.xpath("//div[@class='z-combobox-pp']//td[text()='Sale']");
	private static final By SELECT_SOO_REASON_AS_WAIVE_LOC = By.xpath("//div[@class='z-combobox-pp']//td[starts-with(.,'Waive')]");
	private static final By SELECT_OVERRIDE_REASON_TYPE_LOC = By.xpath("//div[@class='z-combobox-pp']//td[contains(text(),'Appeasement')]");
	private static final By PERFORM_SOO_REASON_LOC = By.xpath("//span[@class='soo-popup-bottom-left']//div[4]//img");
	private static final By SELECT_SOO_REASON_LOC = By.xpath("//div[@class='z-combobox-pp']//td[contains(text(),'Damaged')]");
	private static final By SALES_OVERRIDE_POPUP_UPDATE_BUTTON_LOC = By.xpath("//td[contains(text(),'UPDATE')]");
	private static final By CLOSE_POPUP_OF_EDIT_PAYMENT_ADDRESS = By.xpath("//div[contains(text(),'EDIT PAYMENT PROFILE')]/div[contains(@id,'close')]");
	private static final By CARD_NUMBER_TXT_FIELD_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//div[contains(@class,'csAddCardPaymentWidgetFrame')]//span[text()='Card Number']/following::input[1]");
	private static final By NAME_ON_CARD_TXT_FIELD_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//div[contains(@class,'csAddCardPaymentWidgetFrame')]//span[text()='Card Number']/following::input[2]");
	private static final By CARD_TYPE_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//div[contains(@class,'csAddCardPaymentWidgetFrame')]//span[text()='Card Type']/following::img[1]");
	private static final By CARD_TYPE_VALUE_VISA_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//td[text()='VISA']");
	private static final By EXPIRATION_MONTH_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//div[contains(@class,'csAddCardPaymentWidgetFrame')]//span[text()='Expiration Date']/following::img[1]");
	private static final By EXPIRATION_MONTH_VALUE_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//td[text()='12']");
	private static final By EXPIRATION_YEAR_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//div[contains(@class,'csAddCardPaymentWidgetFrame')]//span[text()='Expiration Date']/following::img[2]");
	private static final By EXPIRATION_YEAR_VALUE_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//td[text()='2025']");
	private static final By SECURITY_CODE_TXT_FIELD_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//div[contains(@class,'csAddCardPaymentWidgetFrame')]//span[text()='Security Code']/following::input[1]");
	private static final By BILLING_ADDRESS_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//div[contains(@class,'csAddCardPaymentWidgetFrame')]//span[text()='Select Billing Address']/following::img[1]");
	private static final By BILLING_ADDRESS_VALUE_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//div[contains(@class,'csAddCardPaymentWidgetFrame')]//span[text()='Select Billing Address']/following::td[@class='z-combo-item-text'][1]");
	private static final By SAVE_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP = By.xpath("//div[contains(@class,'csAddCardPaymentWidgetFrame')]//td[@class='z-button-cm'][text()='SAVE']");
	private static final By ADD_NEW_BILLING_PROFILE_BTN = By.xpath("//td[@class='z-button-cm'][text()='Add New']");
	private static final By CLOSE_POPUP_ADD_NEW_PAYMENT_LOC = By.xpath("//div[@class='z-window-highlighted-header']/div");
	private static final By REVIEW_CREDIT_CARD_DETAILS_POPUP = By.xpath("//span[contains(text(),'Please review credit card information entered')]");
	private static final By NO_STORED_CREDIT_CARD_DETAILS = By.xpath("//span[contains(text(),'Please review credit card information entered')]");
	private static final By NO_SELECTED_DELIVERY_ADDRESS_DETAILS = By.xpath("//select[contains(@class,'csDeliveryAddressList')]//option[@selected='selected'][contains(text(),'Non')]");
	private static final By DELIVERY_MODE_DD_VALUES = By.xpath("//div[@class='csDeliveryModeContainer']//select/option");
	private static final By CANCEL_THIS_SOO_LINK = By.xpath("//a[text()='Cancel This SOO']");
	private static final By SOO_DEPARTMENT_DD = By.xpath("//span[@class='soo-popup-bottom-left']//div[2]//span[@class='z-combobox-btn']");
	private static final By SOO_TYPE_DD = By.xpath("//span[@class='soo-popup-bottom-left']//div[3]//span[@class='z-combobox-btn']");
	private static final By SOO_REASON_DD = By.xpath("//span[@class='soo-popup-bottom-left']//div[4]//span[@class='z-combobox-btn']");
	private static final By SOO_POPUP_HEADER = By.xpath("//div[@class='soo-popup-header']");

	protected RFWebsiteDriver driver;

	public CSCockpitCheckoutTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public String getCreditCardNumberInCheckoutTab(){		
		driver.waitForElementPresent(ADD_NEW_BILLING_PROFILE_BTN);
		if(driver.isElementPresent(CREDIT_CARD)==false){
			driver.click(ADD_NEW_BILLING_PROFILE_BTN);
			driver.waitForCSCockpitLoadingImageToDisappear();
			driver.type(CARD_NUMBER_TXT_FIELD_ON_ADD_NEW_BILLING_PROFILE_POPUP, TestConstants.CARD_NUMBER);
			driver.type(NAME_ON_CARD_TXT_FIELD_ON_ADD_NEW_BILLING_PROFILE_POPUP, TestConstants.NEW_BILLING_PROFILE_NAME);
			driver.click(CARD_TYPE_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP);
			driver.click(CARD_TYPE_VALUE_VISA_ON_ADD_NEW_BILLING_PROFILE_POPUP);
			driver.click(EXPIRATION_MONTH_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP);
			driver.click(EXPIRATION_MONTH_VALUE_ON_ADD_NEW_BILLING_PROFILE_POPUP);
			driver.click(EXPIRATION_YEAR_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP);
			driver.click(EXPIRATION_YEAR_VALUE_ON_ADD_NEW_BILLING_PROFILE_POPUP);
			driver.type(SECURITY_CODE_TXT_FIELD_ON_ADD_NEW_BILLING_PROFILE_POPUP, TestConstants.SECURITY_CODE);
			driver.click(BILLING_ADDRESS_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP);
			driver.click(BILLING_ADDRESS_VALUE_ON_ADD_NEW_BILLING_PROFILE_POPUP);
			driver.click(SAVE_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP);
			driver.waitForCSCockpitLoadingImageToDisappear();
		}
		driver.waitForElementPresent(CREDIT_CARD);
		return driver.findElement(CREDIT_CARD).getText();
	}

	public String getDeliverModeTypeInCheckoutTab(){
		driver.waitForElementPresent(DELIVERY_MODE);
		return driver.findElement(DELIVERY_MODE).getText();
	}

	//	public boolean isCommissionDatePopulatedInCheckoutTab(){
	//		boolean isCommissionDatePopulatedInCheckoutTab = !(driver.findElement(COMMISSION_DATE).getAttribute("value").isEmpty());
	//		logger.info("is Commission Date Populated In Checkout Tab = "+isCommissionDatePopulatedInCheckoutTab);
	//		return isCommissionDatePopulatedInCheckoutTab;
	//	}

	public void clickPlaceOrderButtonInCheckoutTab(){
		driver.waitForElementPresent(PLACE_ORDER_BUTTON_CHECKOUT_TAB);
		driver.pauseExecutionFor(3000);
		driver.click(PLACE_ORDER_BUTTON_CHECKOUT_TAB);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.pauseExecutionFor(5000);
	}

	public boolean verifySelectPaymentDetailsPopupInCheckoutTab(){
		driver.waitForElementPresent(SELECT_PAYMENT_DETAILS_POPUP);
		return driver.isElementPresent(SELECT_PAYMENT_DETAILS_POPUP);
	}

	public void clickTestOrderCheckBoxInCheckoutTab(){
		driver.waitForElementPresent(TEST_ORDER_CHKBOX);
		driver.click(TEST_ORDER_CHKBOX);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickDoNotShipCheckBoxInCheckoutTab(){
		driver.waitForElementPresent(DO_NOT_SHIP_CHKBOX);
		driver.click(DO_NOT_SHIP_CHKBOX);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyAddressTextBoxInPopUpInCheckoutTab(){
		driver.quickWaitForElementPresent(ADDRESS_LINE_TEXT_BOX);
		return driver.isElementPresent(ADDRESS_LINE_TEXT_BOX);
	}


	public boolean verifyPostalCodeTextBoxInPopUpInCheckoutTab(){
		driver.quickWaitForElementPresent(POSTAL_CODE_TEXT_BOX);
		return driver.isElementPresent(POSTAL_CODE_TEXT_BOX);
	}


	public void clickCloseOfPaymentAddressPopUpInCheckoutTab(){
		driver.waitForElementPresent(CLOSE_POPUP_OF_PAYMENT_ADDRESS);
		driver.click(CLOSE_POPUP_OF_PAYMENT_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickSaveOfShippingAddressPopUpInCheckoutTab(){
		driver.pauseExecutionFor(5000);
		driver.waitForElementPresent(POPUP_SAVE_BUTTON);
		driver.click(POPUP_SAVE_BUTTON);
		driver.waitForCSCockpitLoadingImageToDisappear();
		logger.info("Save button clicked after entering billing and shipping address. ");
		driver.pauseExecutionFor(4000);
	}

	public void enterPaymentDetailsInPopUpInCheckoutTab(String cardNumber,String nameOnCard,String securityCode,String month,String year,String cardType){
		driver.waitForElementPresent(POPUP_CARD_NUMBER_TEXT_BOX);
		driver.type(POPUP_CARD_NUMBER_TEXT_BOX,cardNumber);
		logger.info("card number entered is "+cardNumber);
		driver.waitForElementPresent(POPUP_NAME_ON_CARD_TEXT_BOX);
		driver.type(POPUP_NAME_ON_CARD_TEXT_BOX, nameOnCard);
		logger.info("Name on card  entered is "+nameOnCard);
		driver.waitForElementPresent(POPUP_SECURITY_CODE_TEXT_BOX);
		driver.type(POPUP_SECURITY_CODE_TEXT_BOX, securityCode);
		logger.info("Security code entered is "+securityCode);
		selectBillingCardExpirationDate(month,year);
		selectCardType(cardType);

	}

	public void selectCardType(String cardType){
		driver.waitForElementPresent(POPUP_BILLING_PROFILE_CARD_TYPE_DD);
		driver.click(POPUP_BILLING_PROFILE_CARD_TYPE_DD);
		driver.pauseExecutionFor(2000);
		driver.click(By.xpath(String.format(selectCardTypeDDLoc, cardType)));
		logger.info("new billing card type selected as "+cardType);
	}

	public void selectBillingCardExpirationDate(String month,String year){
		driver.waitForElementPresent(POPUP_BILLING_PROFILE_MONTH_DD);
		driver.click(POPUP_BILLING_PROFILE_MONTH_DD);
		driver.pauseExecutionFor(2000);
		driver.click(By.xpath(String.format(selectMonthDDLoc, month)));
		logger.info("new billing month selected as "+month.toUpperCase());
		driver.waitForElementPresent(POPUP_BILLING_PROFILE_YEAR_DD);
		driver.click(POPUP_BILLING_PROFILE_YEAR_DD);
		driver.pauseExecutionFor(2000);
		driver.click(By.xpath(String.format(selectYearDDLoc, year)));
		logger.info("new billing year selected as "+year);
	}

	public void clickCloseOfDeliveryAddressPopUpInCheckoutTab(){
		driver.waitForElementPresent(CLOSE_POPUP_OF_DELIVERY_ADDRESS);
		driver.click(CLOSE_POPUP_OF_DELIVERY_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickSaveOfDeliveryAddressPopUpInCheckoutTab(){
		driver.waitForElementPresent(DELIVERY_ADDRESS_POPUP_SAVE_BUTTON);
		driver.click(DELIVERY_ADDRESS_POPUP_SAVE_BUTTON);
		driver.waitForCSCockpitLoadingImageToDisappear();
		logger.info("Save button clicked after entering billing and shipping address. ");
		driver.pauseExecutionFor(4000);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getErrorMessageOfDeliveryAddressPopupWithoutFillingDataInCheckoutTab(){
		driver.waitForElementPresent(DELIVERY_ADDRESS_POPUP_ERROR_TEXT);
		return driver.findElement(DELIVERY_ADDRESS_POPUP_ERROR_TEXT).getText();
	}

	public void clickOKOfDeliveryAddressPopupInCheckoutTab(){
		driver.waitForElementPresent(DELIVERY_ADDRESS_POPUP_OKAY_BUTTON);
		driver.click(DELIVERY_ADDRESS_POPUP_OKAY_BUTTON);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.pauseExecutionFor(4000);
	}

	public void clickUseAsEnteredPopupOkayInCheckoutTab(){
		driver.waitForElementPresent(USE_AS_ENTERED_POPUP);
		driver.click(USE_AS_ENTERED_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getNewlyCreatedDeliveryAddressNameInCheckoutTab(){
		driver.waitForElementPresent(DELIVERY_ADDRESS_DROPDOWN);
		driver.click(DELIVERY_ADDRESS_DROPDOWN);
		driver.waitForElementPresent(DELIVERY_ADDRESS_DROPDOWN_VALUE);
		return driver.findElement(DELIVERY_ADDRESS_DROPDOWN_VALUE).getText();
	}

	public boolean verifyBasePriceIsPresentInCartSectionInCheckoutTab(){
		driver.waitForElementPresent(BASE_PRICE);
		return driver.isElementPresent(BASE_PRICE);
	}

	public boolean verifyTotalPriceIsPresentInCartSectionInCheckoutTab(){
		driver.waitForElementPresent(TOTAL_PRICE);
		return driver.isElementPresent(TOTAL_PRICE);
	}

	public boolean verifyEntryCVIsPresentInCartSectionInCheckoutTab(){
		driver.waitForElementPresent(ENTRY_CV);
		return driver.isElementPresent(ENTRY_CV);
	}

	public boolean verifyEntryQVIsPresentInCartSectionInCheckoutTab(){
		driver.waitForElementPresent(ENTRY_QV);
		return driver.isElementPresent(ENTRY_QV);
	}

	public String getSelectedDeliveryAddressInCheckoutTab(){
		driver.waitForElementPresent(SELECTED_DELIVERY_ADDRESS);
		return driver.findElement(SELECTED_DELIVERY_ADDRESS).getText();
	}

	public boolean verifyNewAddressIsPresentInDeliverySectionInCheckoutTab(){
		driver.waitForElementPresent(NEW_ADDRESS_IN_DELIVERY_SECTION);
		return driver.isElementPresent(NEW_ADDRESS_IN_DELIVERY_SECTION);
	}

	public boolean verifySubtotalTxtIsPresentInTotalsSectionInCheckoutTab(){
		driver.waitForElementPresent(SUBTOTAL_TXT_TOTALS_SECTION);
		return driver.isElementPresent(SUBTOTAL_TXT_TOTALS_SECTION);
	}

	public boolean verifyDiscountTxtIsPresentInTotalsSectionInCheckoutTab(){
		driver.waitForElementPresent(DISCOUNT_TXT_TOTALS_SECTION);
		return driver.isElementPresent(DISCOUNT_TXT_TOTALS_SECTION);
	}

	public boolean verifyDeliverCostsTxtIsPresentInTotalsSectionInCheckoutTab(){
		driver.waitForElementPresent(DELIVERY_COSTS_TXT_TOTALS_SECTION);
		return driver.isElementPresent(DELIVERY_COSTS_TXT_TOTALS_SECTION);
	}

	public boolean verifyHandlingCostsTxtIsPresentInTotalsSectionInCheckoutTab(){
		driver.waitForElementPresent(HANDLING_COSTS_TXT_TOTALS_SECTION);
		return driver.isElementPresent(HANDLING_COSTS_TXT_TOTALS_SECTION);
	}

	public boolean verifyTaxesTxtIsPresentInTotalsSectionInCheckoutTab(){
		driver.waitForElementPresent(TAXES_TXT_TOTALS_SECTION);
		return driver.isElementPresent(TAXES_TXT_TOTALS_SECTION);
	}

	public boolean verifyTotalPriceTxtIsPresentInTotalsSectionInCheckoutTab(){
		driver.waitForElementPresent(TOTAL_PRICE_TXT_TOTALS_SECTION);
		return driver.isElementPresent(TOTAL_PRICE_TXT_TOTALS_SECTION);
	}

	public boolean verifyTotalCVTxtIsPresentInTotalsSectionInCheckoutTab(){
		driver.waitForElementPresent(TOTAL_CV_TXT_TOTALS_SECTION);
		return driver.isElementPresent(TOTAL_CV_TXT_TOTALS_SECTION);
	}

	public boolean verifyTotalQVTxtIsPresentInTotalsSectionInCheckoutTab(){
		driver.waitForElementPresent(TOTAL_QV_TXT_TOTALS_SECTION);
		return driver.isElementPresent(TOTAL_QV_TXT_TOTALS_SECTION);
	}

	public boolean verifyCommissionDateTxtIsPresentInCustomSectionInCheckoutTab(){
		driver.waitForElementPresent(COMMISSION_DATE_TXT);
		return driver.isElementPresent(COMMISSION_DATE_TXT);
	}

	//	public boolean verifyCommissionDateCalenderIconTxtIsPresentInCustomSectionInCheckoutTab(){
	//		driver.waitForElementPresent(COMMISSION_DATE);
	//		return driver.isElementPresent(COMMISSION_DATE);
	//	}

	public boolean verifyOrderNotesTextInOrderInfoSectionInCheckoutTab(){
		driver.waitForElementPresent(ORDER_NOTES_TXT_LOCATOR);
		return driver.isElementPresent(ORDER_NOTES_TXT_LOCATOR);
	}

	public boolean verifyOrderNotesTextAreaInOrderInfoSectionInCheckoutTab(){
		driver.waitForElementPresent(ORDER_NOTES_TXT_LOCATOR);
		return driver.isElementPresent(ORDER_NOTES_TXT_LOCATOR);
	}

	public boolean verifyOrderNoteAddBtnInOrderInfoSectionInCheckoutTab(){
		driver.waitForElementPresent(ORDER_NOTE_ADD_BTN);
		return driver.isElementPresent(ORDER_NOTE_ADD_BTN);
	}

	public boolean verifyNoPromotionsAppliedInAppliedPromotionsSectionInCheckoutTab(){
		driver.waitForElementPresent(NO_PROMOTION_TXT_LOCATOR);
		return driver.isElementPresent(NO_PROMOTION_TXT_LOCATOR);
	}

	public boolean verifyCreditCardNumberSectionInCheckoutTab(){
		driver.waitForElementPresent(CREDIT_CARD_NUMBER_TXT_LOCATOR);
		return driver.isElementPresent(CREDIT_CARD_NUMBER_TXT_LOCATOR);
	}

	public boolean verifyCreditCardOwnerSectionInCheckoutTab(){
		driver.waitForElementPresent(CREDIT_CARD_OWNER_TXT_LOCATOR);
		return driver.isElementPresent(CREDIT_CARD_OWNER_TXT_LOCATOR);
	}

	public boolean verifyCreditCardTypeSectionInCheckoutTab(){
		driver.waitForElementPresent(CREDIT_CARD_TYPE_TXT_LOCATOR);
		return driver.isElementPresent(CREDIT_CARD_TYPE_TXT_LOCATOR);
	}

	public boolean verifyCreditCardMonthSectionInCheckoutTab(){
		driver.waitForElementPresent(CREDIT_CARD_MONTH_TXT_LOCATOR);
		return driver.isElementPresent(CREDIT_CARD_MONTH_TXT_LOCATOR);
	}

	public boolean verifyCreditCardValidToYearSectionInCheckoutTab(){
		driver.waitForElementPresent(CREDIT_CARD_VALID_TO_YEAR_TXT_LOCATOR);
		return driver.isElementPresent(CREDIT_CARD_VALID_TO_YEAR_TXT_LOCATOR);
	}

	public boolean verifyPaymentBillingAddressSectionInCheckoutTab(){
		driver.waitForElementPresent(PAYMENT_BILLING_ADDRESS_TXT_LOCATOR);
		return driver.isElementPresent(PAYMENT_BILLING_ADDRESS_TXT_LOCATOR);
	}

	public boolean verifyPaymentAmountSectionInCheckoutTab(){
		driver.waitForElementPresent(PAYMENT_AMOUNT_TXT_LOCATOR);
		return driver.isElementPresent(PAYMENT_AMOUNT_TXT_LOCATOR);
	}

	public boolean verifyCreditCardCv2SectionInCheckoutTab(){
		driver.waitForElementPresent(CREDIT_CARD_CV2_TXT_LOCATOR);
		return driver.isElementPresent(CREDIT_CARD_CV2_TXT_LOCATOR);
	}

	public boolean verifyUseThisCardBtnInCheckoutTab(){
		driver.waitForElementPresent(USE_THIS_CARD_BTN);
		return driver.isElementPresent(USE_THIS_CARD_BTN);
	}

	public boolean verifyPlaceOrderBtnIsPresentInCheckoutTab(){
		driver.waitForElementPresent(PLACE_ORDER_BUTTON_CHECKOUT_TAB);
		return driver.isElementPresent(PLACE_ORDER_BUTTON_CHECKOUT_TAB);
	}

	public boolean verifyPerformSOOBtnIsPresentInCheckoutTab(){
		driver.waitForElementPresent(PERFORM_SOO_BTN_LOCATOR);
		return driver.isElementPresent(PERFORM_SOO_BTN_LOCATOR);
	}

	public boolean verifyTestOrderChkBoxIsPresentInCheckoutTab(){
		driver.waitForElementPresent(TEST_ORDER_CHKBOX);
		return driver.isElementPresent(TEST_ORDER_CHKBOX);
	}

	public boolean verifyDoNotShipChkBoxIsPresentInCheckoutTab(){
		driver.waitForElementPresent(DO_NOT_SHIP_CHKBOX);
		return driver.isElementPresent(DO_NOT_SHIP_CHKBOX);
	}

	public String getNameFromCartSectionInCheckoutTab(String name){
		driver.waitForElementPresent(By.xpath(String.format(firstName, name)));
		String fullName = driver.findElement(By.xpath(String.format(firstName, name))).getText();
		logger.info("Selected Cutomer first Name is = "+fullName);
		return fullName;
	}

	public void clickCloseOfEditPaymentAddressPopUpInCheckoutTab(){
		driver.waitForElementPresent(CLOSE_POPUP_OF_EDIT_PAYMENT_ADDRESS);
		driver.click(CLOSE_POPUP_OF_EDIT_PAYMENT_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void updateOrderNoteOnCheckOutPage(String updateOrderNote){
		driver.waitForElementPresent(ORDER_NOTES_TXT_FIELD);
		driver.clear(ORDER_NOTES_TXT_FIELD);
		driver.type(ORDER_NOTES_TXT_FIELD, updateOrderNote);
		driver.click(UPDATE_BUTTON_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void enterInvalidCV2OnPaymentInfoSection(String invalidCv2){
		driver.pauseExecutionFor(3000);
		driver.waitForElementPresent(CV2_TEXT_FIELD_LOC);
		driver.findElement(CV2_TEXT_FIELD_LOC).sendKeys(invalidCv2);
	}

	public void entervalidCV2OnPaymentInfoSection(String validCv2){
		driver.pauseExecutionFor(3000);
		driver.waitForElementPresent(CV2_TEXT_FIELD_LOC);
		driver.findElement(CV2_TEXT_FIELD_LOC).sendKeys(validCv2);
	}

	public void clickUseThisCardButtonOnCheckoutPage(){
		driver.waitForElementPresent(USE_THIS_CARD_BUTTON_LOC);
		driver.click(USE_THIS_CARD_BUTTON_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickOkForCreditCardValidationFailedPopUp(){
		driver.waitForElementPresent(CREDIT_CARD_VALIDATION_FAILED_OK_LOC);
		driver.click(CREDIT_CARD_VALIDATION_FAILED_OK_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickOrderNoteEditButton(String orderNote){
		driver.waitForElementPresent(By.xpath(String.format(addedOrderNoteEditBtnLoc, orderNote)));
		driver.click(By.xpath(String.format(addedOrderNoteEditBtnLoc, orderNote)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}


	public void clickPerformSooButton() {
		driver.waitForElementPresent(PERFROM_SOO_BUTTON_LOC);
		driver.click(PERFROM_SOO_BUTTON_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();

	}

	public void enterPriceValueInSalesOrderOverridePopUp(String price) {
		driver.waitForElementPresent(PERFORM_SOO_POPUP_PRICE_LOC);
		driver.type(PERFORM_SOO_POPUP_PRICE_LOC, price);
	}

	public void enterCVValueInSalesOrderOverrridePoPuP(String cv){
		driver.waitForElementPresent(PERFORM_SOO_POPUP_CV_LOC);
		driver.type(PERFORM_SOO_POPUP_CV_LOC, cv);
		logger.info("CV VALUE ENTERED IN SALES OVERRIDE POPUP");
	}

	public void enterQVValueInSalesOrderOvverridePopUp(String qv){
		driver.waitForElementPresent(PERFORM_SOO_POPUP_QV_LOC);
		driver.type(PERFORM_SOO_POPUP_QV_LOC, qv);
		logger.info("QV VALUE ENTERED IN SALES OVERRIDE POPUP");
	}

	public void enterDeliveryCostsInSalesOrderOvverridePopUp(String dc){
		driver.waitForElementPresent(PERFORM_SOO_POPUP_DC_LOC);
		driver.type(PERFORM_SOO_POPUP_DC_LOC, dc);
		logger.info("DC VALUE ENTERED IN SALES OVERRIDE POPUP");
	}

	public void enterHandlingCostsInSalesOrderOvveridePOpUp(String hc){
		driver.waitForElementPresent(PERFORM_SOO_POPUP_HC_LOC);
		driver.type(PERFORM_SOO_POPUP_HC_LOC, hc);
		logger.info("HC VALUE ENTERED IN SALES OVERRIDE POPUP");

	}

	public void selectOverrideReasonSooDept(){
		driver.waitForElementPresent(PERFORM_SOO_OVERRIDE_REASON_DEPT_DD_LOC);
		driver.click(PERFORM_SOO_OVERRIDE_REASON_DEPT_DD_LOC);
		logger.info("SOO DEPT DD CLICKED IN SALES OVERRIDE POPUP");
		driver.waitForElementPresent(SELECT_OVERRIDE_REASON_DEPT_LOC);
		driver.click(SELECT_OVERRIDE_REASON_DEPT_LOC);
		logger.info("SOO DEPT DD VALUE SELECTED IN SALES OVERRIDE POPUP");
	}

	public void selectOverrideReasonSooType(){
		driver.waitForElementPresent(PERFORM_SOO_OVERRIDE_REASON_SOO_TYPE_DD_LOC);
		driver.click(PERFORM_SOO_OVERRIDE_REASON_SOO_TYPE_DD_LOC);
		logger.info("SOO TYPE DD CLICKED IN SALES OVERRIDE POPUP");
		driver.waitForElementPresent(SELECT_OVERRIDE_REASON_TYPE_AS_SALES_LOC);
		driver.click(SELECT_OVERRIDE_REASON_TYPE_AS_SALES_LOC);
		logger.info("SOO TYPE DD VALUE SELECTED IN SALES OVERRIDE POPUP");
	}

	public void selectOverrideReasonSooReason(){
		driver.waitForElementPresent(PERFORM_SOO_REASON_LOC);
		driver.click(PERFORM_SOO_REASON_LOC);
		logger.info("SOO REASON DD CLICKED IN SALES OVERRIDE POPUP");
		driver.waitForElementPresent(SELECT_SOO_REASON_AS_WAIVE_LOC);
		driver.click(SELECT_SOO_REASON_AS_WAIVE_LOC);
		logger.info("SOO REASON DD VALUE SELECTED");
	}

	public void clickUpdateButtonSalesOverridePopUp(){
		driver.waitForElementPresent(SALES_OVERRIDE_POPUP_UPDATE_BUTTON_LOC);
		driver.click(SALES_OVERRIDE_POPUP_UPDATE_BUTTON_LOC);

	}

	public void clickCloseAddNewPaymentProfilePopUp() {
		driver.waitForElementPresent(CLOSE_POPUP_ADD_NEW_PAYMENT_LOC);
		driver.click(CLOSE_POPUP_ADD_NEW_PAYMENT_LOC);
		logger.info("Pop Up closed");
	}

	public void enterSecurityCodeInPaymentPopUpInCheckOutTab(String securityCode){
		driver.waitForElementPresent(POPUP_SECURITY_CODE_TEXT_BOX);
		driver.type(POPUP_SECURITY_CODE_TEXT_BOX, securityCode);
		logger.info("Security code entered is "+securityCode);
	}

	public void addANewBillingProfileIfThereIsNoStoredCreditCard(){
		/*if(driver.isElementPresent(NO_STORED_CREDIT_CARD_DETAILS)==true){*/
		driver.click(ADD_NEW_BILLING_PROFILE_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.type(CARD_NUMBER_TXT_FIELD_ON_ADD_NEW_BILLING_PROFILE_POPUP, TestConstants.CARD_NUMBER);
		driver.type(NAME_ON_CARD_TXT_FIELD_ON_ADD_NEW_BILLING_PROFILE_POPUP, TestConstants.NEW_BILLING_PROFILE_NAME);
		driver.click(CARD_TYPE_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP);
		driver.click(CARD_TYPE_VALUE_VISA_ON_ADD_NEW_BILLING_PROFILE_POPUP);
		driver.click(EXPIRATION_MONTH_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP);
		driver.click(EXPIRATION_MONTH_VALUE_ON_ADD_NEW_BILLING_PROFILE_POPUP);
		driver.click(EXPIRATION_YEAR_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP);
		driver.click(EXPIRATION_YEAR_VALUE_ON_ADD_NEW_BILLING_PROFILE_POPUP);
		driver.type(SECURITY_CODE_TXT_FIELD_ON_ADD_NEW_BILLING_PROFILE_POPUP, TestConstants.SECURITY_CODE);
		driver.click(BILLING_ADDRESS_DD_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP);
		driver.click(BILLING_ADDRESS_VALUE_ON_ADD_NEW_BILLING_PROFILE_POPUP);
		driver.click(SAVE_BTN_ON_ADD_NEW_BILLING_PROFILE_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
		enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		driver.click(USE_THIS_CARD_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
		/*}*/
	}

	public void addDeliveryAddressIfNonSelected(String attendentFirstName,String attendeeLastName,String addressLine,String city,String postalCode,String Country,String province,String phoneNumber){
		if(driver.isElementPresent(NO_SELECTED_DELIVERY_ADDRESS_DETAILS)==true){
			driver.click(ADD_NEW_ADDRESS);
			driver.waitForElementPresent(ATTENDENT_NAME_TEXT_BOX);
			driver.clear(ATTENDENT_NAME_TEXT_BOX);
			driver.type(ATTENDENT_NAME_TEXT_BOX,attendentFirstName+" "+attendeeLastName);
			logger.info("Attendee name entered is "+attendentFirstName+" "+attendeeLastName);
			driver.waitForElementPresent(ADDRESS_LINE_TEXT_BOX);
			driver.type(ADDRESS_LINE_TEXT_BOX,addressLine);
			logger.info("Address line 1 entered is "+addressLine);
			driver.waitForElementPresent(CITY_TOWN_TEXT_BOX);
			driver.type(CITY_TOWN_TEXT_BOX, city);
			logger.info("City entered is "+city);
			driver.waitForElementPresent(POSTAL_TEXT_BOX);
			driver.type(POSTAL_TEXT_BOX, postalCode);
			logger.info("Postal code entered is "+postalCode);
			driver.waitForElementPresent(COUNTRY_TEXT_BOX);
			driver.type(COUNTRY_TEXT_BOX, Country);
			logger.info("Country entered is "+Country);
			driver.waitForElementPresent(PROVINCE_TEXT_BOX);
			driver.type(PROVINCE_TEXT_BOX, province);
			logger.info("Province entered is "+province);
			driver.waitForElementPresent(PHONE_TEXT_BOX);
			driver.type(PHONE_TEXT_BOX, phoneNumber);
			logger.info("Phone number entered is "+phoneNumber);
			driver.click(USE_AS_ENTERED_POPUP);
			driver.click(DELIVERY_ADDRESS_POPUP_SAVE_BUTTON);
			driver.waitForCSCockpitLoadingImageToDisappear();
		}
	}

	public String getSizeOfDeliveryModeDDValues(){
		int deliveryModes = driver.findElements(DELIVERY_MODE_DD_VALUES).size();
		String noOfDeliveryMode = ""+deliveryModes;
		return noOfDeliveryMode;
	}

	public String getValuesFromTotalsSection(String sectionName){
		driver.waitForElementPresent(By.xpath(String.format(totalsSectionValues, sectionName)));
		String value = driver.findElement(By.xpath(String.format(totalsSectionValues, sectionName))).getText();
		logger.info("Value of "+sectionName+" is = "+value);
		return value;
	}

	public boolean verifyPerformSOOPopupValues(String value){
		driver.waitForElementPresent(By.xpath(String.format(sooPopupvalues, value)));
		return driver.isElementPresent(By.xpath(String.format(sooPopupvalues, value)));
	}

	public boolean verifySOOPopupValues(String value){
		driver.waitForElementPresent(By.xpath(String.format(sooValues, value)));
		return driver.isElementPresent(By.xpath(String.format(sooValues, value)));
	}

	public boolean verifyCancelThisSOOLink(){
		driver.waitForElementPresent(CANCEL_THIS_SOO_LINK);
		return driver.isElementPresent(CANCEL_THIS_SOO_LINK);
	}

	public void clickSOODepartmentDD(){
		driver.waitForElementPresent(SOO_DEPARTMENT_DD);
		driver.click(SOO_DEPARTMENT_DD);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickSOOTypeDD(){
		driver.waitForElementPresent(SOO_TYPE_DD);
		driver.click(SOO_TYPE_DD);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickSOOReasonDD(){
		driver.waitForElementPresent(SOO_REASON_DD);
		driver.click(SOO_REASON_DD);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifySOODDValues(String value){
		driver.waitForElementPresent(By.xpath(String.format(sooDDValues, value)));
		return driver.isElementPresent(By.xpath(String.format(sooDDValues, value)));
	}

	public void clickSOOPopupHeader(){
		driver.waitForElementPresent(SOO_POPUP_HEADER);
		driver.click(SOO_POPUP_HEADER);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

}