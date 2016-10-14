package com.rf.pages.website.cscockpit;

import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

public class CSCockpitAutoshipTemplateTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitAutoshipTemplateTabPage.class.getName());

	private static String orderDetailsLoc= "//span[contains(text(),'Order Detail Items')]/following::div[contains(text(),'%s')]";
	private static String orderFromAutoShipTemplateSectionLoc= "//span[text()='Number of Consecutive Autoship Orders From Template']/../../following-sibling::div[1]/div[2]/div/div[1]//tbody[2]//th/div[text()='%s']";
	private static String orderNoteUILoc="//span[contains(text(),'%s')]";
	private static String appliedPromotionsLoc= "//span[contains(text(),'Applied Promotions')]/following::div[contains(text(),'%s')]";
	private static String changeDelayAutoshipDate = "//label[contains(text(),'%s Days from next ship date')]/preceding::input[1]";
	private static String nextDelayautoshipdateFromPopup = "//label[contains(text(),'%s Days from next ship date')]";
	private static String ProductCountOnAutoshipTemplateLoc ="//div[contains(@class,'csWidgetListbox')]/div[2]//tbody/tr[%s]/td[8]/div/input";
	private static String allAboveTabOfCSCockpit = "//span[text()='%s']/ancestor::li";
	private static String addressValuesInPaymentProfilePopup = "//span[text()='%s']";

	private static final By NEXT_CRP_CART_LINK=By.xpath("//div[@class='csObjectCRPOrderContainer']//span[text()='Next CRP Cart']");
	private static final By SHIPPING_ADDRESS_NAME = By.xpath("//div[@class='csWidgetContent']//span[contains(text(),'Shipping Address')]/../following::div[1]/span");
	private static final By SHIPPING_ADDRESS_LINE_1 = By.xpath("//div[@class='csWidgetContent']//span[contains(text(),'Shipping Address')]/../following::div[2]/span");
	private static final By SHIPPING_ADDRESS_LOCALE_POSTCODE_REGION = By.xpath("//div[@class='csWidgetContent']//span[contains(text(),'Shipping Address')]/../following::div[3]/span");
	private static final By SHIPPING_ADDRESS_COUNTRY = By.xpath("//div[@class='csWidgetContent']//span[contains(text(),'Shipping Address')]/../following::div[4]/span");
	private static final By PAYMENT_ADDRESS_NAME = By.xpath("//span[text()='Payment Info ']/../following::div[1]/span");
	private static final By ORDER_FROM_THIS_AUTOSHIP_TEMPLATE_COMPONENT_TEXT = By.xpath("//div[@class='csConsecutiveOrders']/span[1]");
	private static final By UPDATE_BUTTON_AUTOSHIP_TEMPLATE_TAB = By.xpath("//div[@class='csConsecutiveOrders']/span[1]/following-sibling::span/span/table[@class='csUpdateConsOrders z-button ']//tr/td[text()='Update']");
	private static final By RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB_ANOTHER= By.xpath("//td[text()='Run Now']/ancestor::table[@class='z-button']");
	private static final By CANCEL_EDIT_LINK = By.xpath("//td[contains(text(),'Cancel Edit')]");
	private static final By FIRST_REMOVE_LINK_OF_ORDER_DETAIL = By.xpath("//div[contains(@class,'csWidgetListbox')]/div[2]//tbody/tr[1]/td[10]//a[text()='Remove']");
	private static final By THRESHOLD_POPUP = By.xpath("//span[contains(text(),'Your Total SV value should be greater than the threshold')]");
	private static final By THRESHOLD_POPUP_OK = By.xpath("//td[text()='OK']");
	private static final By ADD_MORE_LINES_LINK = By.xpath("//td[text()='Add More Lines']");
	private static final By SUBTOTAL_VALUE = By.xpath("//span[text()='Subtotal:']/following-sibling::span");
	private static final By QUANTITY_TEXT_BOX_OF_SECOND_PRODUCT = By.xpath("//div[contains(@class,'csWidgetListbox')]/div[2]//tbody/tr[2]/td[8]/div/input");
	private static final By UPDATE_QUANTITY_LINK_OF_SECOND_PRODUCT = By.xpath("//div[contains(@class,'csWidgetListbox')]/div[2]//tbody/tr[2]/td[9]/div/a");
	private static final By ORDER_NOTE_TEXT_BOX = By.xpath("//div[@class='csWidgetContent']//tr/td//span[text()='Order Notes: ']/following::textarea"); 
	private static final By ORDER_NOTE_ADD_BUTTON = By.xpath("//div[@class='csWidgetContent']//tr/td//span[text()='Order Notes: ']/following::tr/td[text()='ADD']");
	private static final By REMOVE_LINK_OF_ORDER_DETAIL = By.xpath("//a[text()='Remove']");
	private static final By RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB = By.xpath("//td[text()='Run Now']");
	private static final By CONFIRM_MESSAGE_YES_LOC = By.xpath("//td[text()='Yes']");
	private static final By CONFIRM_MESSAGE_OK_LOC = By.xpath("//td[text()='OK']");
	private static final By AUTOSHIP_TEMPLATE_HEADER = By.xpath("//span[text()='Autoship Template #']");
	private static final By APPLIED_PROMOTION_SECTION = By.xpath("//span[text()='Applied Promotions']");
	private static final By SHIPPING_ADDRESS = By.xpath("//div[@class='csWidgetContent']//span[contains(text(),'Shipping Address')]");
	private static final By PAYMENT_INFO = By.xpath("//span[text()='Payment Info ']");
	private static final By ORDER_FROM_THIS_AUTOSHIP_TEMPLATE = By.xpath("//span[text()='Orders From this Autoship Template']");
	private static final By AUTOSHIP_TEMPLATE_CID_AUTOSHIP_ID = By.xpath("//span[text()='Autoship Template #']/following-sibling::span");
	private static final By CANCEL_AUTOSHIP = By.xpath("//a[text()='Cancel Autoship']");
	private static final By CANCEL_AUTOSHIP_DISABLE_LINK = By.xpath("//a[text()='Cancel Autoship' and @style='display:none;']");
	private static final By EDIT_AUTOSHIP_LINK = By.xpath("//td[text()='Edit Template']");
	private static final By PLUS_LINK_NEXT_TO_FIRST_PRODUCT = By.xpath("//span[text()='Order Detail Items']/following::div/div//a[text()='+'][1]");
	private static final By VIEW_PRODUCT_PAGE_LINK = By.xpath("//a[text()='View Product Page']");
	private static final By MINUS_LINK_NEXT_TO_FIRST_PRODUCT = By.xpath("//span[text()='Order Detail Items']/following::div/div//a[text()='-'][1]");
	private static final By VIEW_PRODUCT_DETAIL = By.xpath("//input[@value='ADD TO BAG']");
	private static final By AUTOSHIP_TEMPLATE_STATUS = By.xpath("//span[text()='Template Status:']/following-sibling::span");
	private static final By AUTOSHIP_TEMPLATE_UPDATE_BTN = By.xpath("//span[@class='csUpdateConsOrders']//span[@z.disd='true']");
	private static final By AUTOSHIP_TEMPLATE_RUN_NOW_BTN = By.xpath("//td[contains(text(),'Run Now')]/../preceding::tr[1]//button[@disabled='disabled']");
	private static final By CANCEL_AUTOSHIP_TEMPLATE_LOC = By.xpath("//a[text()='Cancel Autoship']");
	private static final By CANCEL_AUTOSHIP_TEMPLATE_POPUP_CONFIRM_BTN_LOC = By.xpath("//td[text()='Confirm']");
	private static final By SHIPPING_ADDRESS_PROFILE_NAME = By.xpath("//div[@class='csOrderDetailsAddress']//div[2]//span");
	private static final By CURRENT_CRP_AUTOSHIP_DATE = By.xpath("//span[@class='z-datebox']//input");
	private static final By DISABLED_UPDATE_AUTOSHIP_BTN = By.xpath("//span[text()='Order Detail Items']/following::div[@class='csWidgetContent'][1]//a[text()='Update'][@style='display:none;']");
	private static final By DISABLED_REMOVE_AUTOSHIP_BTN = By.xpath("//span[text()='Order Detail Items']/following::div[@class='csWidgetContent'][1]//a[text()='Remove'][@style='display:none;']");
	private static final By DISABLED_QTY_INPUT_TXT = By.xpath("//span[text()='Order Detail Items']/following::div[@class='csWidgetContent'][1]//tr[contains(@class,'csListItem ')]/td[8]//input[@disabled='disabled']");
	private static final By CONFIRM_MSG_OF_RUN_NOW = By.xpath("//div[text()='Confirm']/following::div[@class='z-messagebox']//span");
	private static final By RUN_NOW_AGAIN = By.xpath("//div[contains(text(),'Run Now?')]");
	private static final By RUN_NOW_AGAIN_YES_BTN = By.xpath("//div[contains(text(),'Run Now?')]/following::td[text()='Yes']");
	private static final By THRESHOLD_POPUP_FOR_US = By.xpath("//span[text()='Your total SV value should be equal to or greater than 80']");
	private static final By THRESHOLD_POPUP_FOR_CA = By.xpath("//span[text()='Your total SV value should be equal to or greater than 100']");
	private static final By NEXT_DUE_DATE_OF_CRP_AUTOSHIP = By.xpath("//span[contains(text(),'Next Due Date')]/following::span[1]");
	private static final By DISABLED_RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB= By.xpath("//td[text()='Run Now']/../../ancestor::table[1][contains(@class,disd)]");
	private static final By CANCELLATION_REASON_DD = By.xpath("//span[contains(text(),'Cancellation Reason:')]/following-sibling::span//img");
	private static final By CANCELLATION_DD_OPTION = By.xpath("//span[contains(text(),'Cancellation Reason:')]/following-sibling::span//img/following::td[contains(text(),'much')]");
	private static final By REQUEST_SOURCE_DD = By.xpath("//span[contains(text(),'Request Source')]/following-sibling::span//img");
	private static final By REQUEST_SOURCE_DD_OPTION = By.xpath("//span[contains(text(),'Request Source')]/following-sibling::span//img/following::td[contains(text(),'EMAIL')]");
	private static final By PC_PERKS_MESSAGE_TEXT_BOX = By.xpath("//span[text()='Message']/following::textarea[1]");
	private static final By DELAY_AUTOSHIP_LINK = By.xpath("//a[text()='Delay Autoship']");
	private static final By POPUP_SAVE_BUTTON_LOC = By.xpath("//td[text()='Save']");
	private static final By NEXT_CART_DATE = By.xpath("//div[@class='csObjectPCPerksOrderContainer']/div[8]//span//input");
	private static final By DISABLED_EDIT_AUTOSHIP_TEMPLATE_BTN_ = By.xpath("//div[@class='csObjectCRPOrderContainer']/div[9]//span[@style='display:none']");
	private static final By QTY_INPUT_TXT_BOX_OF_FIRST_PRODUCT = By.xpath("//div[contains(@class,'csWidgetListbox')]/div[2]//tbody/tr[1]/td[8]//input");
	private static final By FIRST_UPDATE_LINK_OF_ORDER_DETAIL = By.xpath("//div[contains(@class,'csWidgetListbox')]/div[2]//tbody/tr[1]/td[9]//a[text()='Update']");
	private static final By THRESHOLD_POPUP_FOR_ORDER_TOTAL = By.xpath("//div[@class='z-messagebox']");
	private static final By ADD_CARD_BTN = By.xpath("//div[@class='csPaymentInfoLine']/following::td[text()='Add Card'][1]");
	private static final By CLOSE_BTN_OF_ADD_A_NEW_PAYMENT_PROFILE_POPUP = By.xpath("//div[contains(text(),'Add a New Payment Profile')]/div");
	private static final By SELECT_BILLING_ADDRESS_ERROR_MSG = By.xpath("//span[contains(text(),'Please select Billing Address')]");
	private static final By ADD_A_NEW_ADDRESS_IN_PAYMENT_PROFILE_POPUP = By.xpath("//a[contains(text(),'Add a new Address')]");
	private static final By PAYMENT_INFO_ADDRESS_DD = By.xpath("//div[@class='csObjectRFCreditCardPaymentInfoContainer']//div[@class='csDeliveryModeContainer']/span/span");
	private static final By CANCEL_AUTOSHIP_TEMPLATE_POPUP_CANCEL_BTN_LOC = By.xpath("//td[text()='Cancel']");
	private static final By DISABLED_EDIT_TEMPLATE = By.xpath("//td[text()='Edit Template']/ancestor::span[@style='display:none']");
	private static final By CANCEL_AUTOSHIP_POPUP_ALERT = By.xpath("//div[text()='Cancel Autoship Popup']");
	private static final By NUMBER_OF_CONSECUTIVE_AUTOSHIP_ORDERS_FROM_TEMPLATE = By.xpath("//div[@class='csConsecutiveOrders']/input");
	private static final By NEXT_PC_PERKS_CART=By.xpath("//div[@class='csObjectPCPerksOrderContainer']//span[text()='Next PCPerks Cart']");
	private static final By EDIT_PAYMENT_INFO = By.xpath("//div[@class='csObjectRFCreditCardPaymentInfoContainer']//td[text()='Edit']");
	private static final By CLOSE_BTN_OF_EDIT_PAYMENT_PROFILE_POPUP = By.xpath("//div[contains(text(),'EDIT PAYMENT PROFILE')]/div");
	private static final By FIRST_CREDIT_CARD_PAYMENT_INFO = By.xpath("//body/div[3]//tbody//tr[2]/td[2]");
	private static final By AUTOSHIP_TEMPLATE_TYPE = By.xpath("//span[text()='Template Type:']/following-sibling::span");
	private static final By ADD_NEW_ADDRESS_BTN_UNDER_SHIPPING_ADDRESS = By.xpath("//td[text()='Add New Address']");
	private static final By SHIPPING_ADDRESS_LINE1 = By.xpath("//div[@class='csWidgetContent']//span[contains(text(),'Shipping Address')]/../following-sibling::div[2]/span");
	private static final By SELECT_VALID_FUTURE_DATE_POPUP = By.xpath("//span[contains(text(),'Please select a valid future date before 18th of month')]");
	private static final By SHIPPING_METHOD_NAME_FROM_UI = By.xpath("//span[text()='Shipping Method']/following::span[1]");
	private static final By PAYMENT_INFO_PROFILE_NAME_FROM_DD = By.xpath("//div[@class='csObjectRFCreditCardPaymentInfoContainer']//div[@class='csDeliveryModeContainer']/span/input");
	private static final By ALREADY_CANCEL_AUTOSHIP_DISABLE_LINK = By.xpath("//div[@class='csCancelAutoship']/a[@style='display:none;']");
	private static final By TOTAL_SV_VALUE = By.xpath("//span[contains(text(),'Total QV')]/following::span[1]");

	protected RFWebsiteDriver driver;
	public CSCockpitAutoshipTemplateTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public void clickOkConfirmMessagePopUp(){   
		try{
			driver.click(CONFIRM_MESSAGE_OK_LOC);
			driver.waitForCSCockpitLoadingImageToDisappear();
		}catch(NoSuchElementException e){

		}
	}

	public boolean verifyAutoshipTemplateHeaderSectionInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(AUTOSHIP_TEMPLATE_HEADER);
		return driver.isElementPresent(AUTOSHIP_TEMPLATE_HEADER);  
	}

	public boolean verifyShippingAddressSectionInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(SHIPPING_ADDRESS);
		return driver.isElementPresent(SHIPPING_ADDRESS);  
	}

	public boolean verifyPaymentInfoSectionInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(PAYMENT_INFO);
		return driver.isElementPresent(PAYMENT_INFO);  
	}

	public boolean verifyOrderFromThisAutoshipTemplateSectionInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(ORDER_FROM_THIS_AUTOSHIP_TEMPLATE);
		return driver.isElementPresent(ORDER_FROM_THIS_AUTOSHIP_TEMPLATE);  
	}

	public String getAutoshipTemplateCIDFromAutoshipTemplateSectionInAutoshipTemplateTab(){
		driver.waitForElementPresent(AUTOSHIP_TEMPLATE_CID_AUTOSHIP_ID);
		String autoshipTemplateCID = driver.findElement(AUTOSHIP_TEMPLATE_CID_AUTOSHIP_ID).getText();
		return autoshipTemplateCID;
	}

	public boolean verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(CANCEL_AUTOSHIP_DISABLE_LINK);
		return driver.isElementPresent(CANCEL_AUTOSHIP_DISABLE_LINK);  
	}

	public boolean verifyEditAutoshipTemplateLinkInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(EDIT_AUTOSHIP_LINK);
		return driver.isElementPresent(EDIT_AUTOSHIP_LINK);  
	}

	public boolean verifyAppliedPromotionSectionInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(APPLIED_PROMOTION_SECTION);
		return driver.isElementPresent(APPLIED_PROMOTION_SECTION);  
	}

	public boolean verifyOrderDetailsInAutoshipTemplateTab(String details){
		driver.waitForElementPresent(By.xpath(String.format(orderDetailsLoc, details)));
		return driver.isElementPresent(By.xpath(String.format(orderDetailsLoc, details)));
	}

	public void clickPlusButtonNextToProductInAutoshipTemplateTab() {
		driver.waitForElementPresent(PLUS_LINK_NEXT_TO_FIRST_PRODUCT);
		driver.click(PLUS_LINK_NEXT_TO_FIRST_PRODUCT);
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyViewProductPageLinkInAutoshipTemplateTab(){
		driver.waitForElementPresent(VIEW_PRODUCT_PAGE_LINK);
		return driver.isElementPresent(VIEW_PRODUCT_PAGE_LINK);
	}

	public void clickMinusButtonNextToProductInAutoshipTemplateTab() {
		driver.waitForElementPresent(MINUS_LINK_NEXT_TO_FIRST_PRODUCT);
		driver.click(MINUS_LINK_NEXT_TO_FIRST_PRODUCT);
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyPlusButtonNextToProductInAutoshipTemplateTab(){
		driver.waitForElementPresent(PLUS_LINK_NEXT_TO_FIRST_PRODUCT);
		return driver.isElementPresent(PLUS_LINK_NEXT_TO_FIRST_PRODUCT);
	}

	public String getShippingAddressNameInAutoshipTemplateTab(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_NAME);
		String shippingAddName = driver.findElement(SHIPPING_ADDRESS_NAME).getText();
		return shippingAddName;
	}

	public String getShippingAddressLine1InAutoshipTemplateTab(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_LINE_1);
		String shippingAddLine1 = driver.findElement(SHIPPING_ADDRESS_LINE_1).getText();
		return shippingAddLine1;
	}

	public String getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_LOCALE_POSTCODE_REGION);
		String shippingAddDetails = driver.findElement(SHIPPING_ADDRESS_LOCALE_POSTCODE_REGION).getText();
		return shippingAddDetails;
	}

	public String getShippingAddressCountryInAutoshipTemplateTab(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_COUNTRY);
		String shippingAddCountry = driver.findElement(SHIPPING_ADDRESS_COUNTRY).getText();
		return shippingAddCountry;
	}

	public String getPaymentAddressNameInAutoshipTemplateTab(){
		driver.waitForElementPresent(PAYMENT_ADDRESS_NAME);
		String paymentAddName = driver.findElement(PAYMENT_ADDRESS_NAME).getText();
		return paymentAddName;
	}

	public String getTextOfOrderFromAutoshipTemplateInAutoshipTemplateTab(){
		driver.waitForElementPresent(ORDER_FROM_THIS_AUTOSHIP_TEMPLATE_COMPONENT_TEXT);
		String textOfOrderFromAutoshipTemplate = driver.findElement(ORDER_FROM_THIS_AUTOSHIP_TEMPLATE_COMPONENT_TEXT).getText();
		return textOfOrderFromAutoshipTemplate;
	}

	public void clickRunNowButtonOnAutoshipTemplateTab() {
		driver.waitForElementPresent(RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB);
		driver.click(RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB);
		driver.waitForCSCockpitLoadingImageToDisappear(); 
		if(driver.isElementPresent(RUN_NOW_AGAIN)==true){
			driver.click(RUN_NOW_AGAIN_YES_BTN);
			driver.waitForCSCockpitLoadingImageToDisappear();
		}
	}

	public void clickOkForRegeneratedIdpopUp() {
		try{
			if(driver.isElementPresent(CONFIRM_MESSAGE_YES_LOC) && driver.findElement(CONFIRM_MESSAGE_YES_LOC).isDisplayed()){
				driver.click(CONFIRM_MESSAGE_YES_LOC);
				driver.waitForCSCockpitLoadingImageToDisappear(); 
			}
		}catch(NoSuchElementException nse){

		}

	}
	public boolean verifyConfirmMessagePopUpIsAppearing() {
		driver.quickWaitForElementPresent(CONFIRM_MESSAGE_OK_LOC);
		return driver.isElementPresent(CONFIRM_MESSAGE_OK_LOC);
	}

	public boolean verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(String sectionName){
		driver.waitForElementPresent(By.xpath(String.format(orderFromAutoShipTemplateSectionLoc, sectionName)));
		return driver.isElementPresent(By.xpath(String.format(orderFromAutoShipTemplateSectionLoc, sectionName)));
	}

	public boolean verifyUpdateLinkInOrderFromAutoshipTemplateInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(UPDATE_BUTTON_AUTOSHIP_TEMPLATE_TAB);
		return driver.isElementPresent(UPDATE_BUTTON_AUTOSHIP_TEMPLATE_TAB);		    
	}

	public boolean verifyRunNowLinkInOrderFromAutoshipTemplateInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB_ANOTHER);
		return driver.isElementPresent(RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB_ANOTHER);		   
	}

	public void clickEditTemplateLinkInAutoshipTemplateTab() {
		driver.waitForElementPresent(EDIT_AUTOSHIP_LINK);
		driver.click(EDIT_AUTOSHIP_LINK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyCancelEditLinkInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(CANCEL_EDIT_LINK);
		return driver.isElementPresent(CANCEL_EDIT_LINK);		    
	}

	public boolean verifyThresholdPopupInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(THRESHOLD_POPUP);
		return driver.isElementPresent(THRESHOLD_POPUP);
	}

	public void clickOKOfThresholdPopupInAutoshipTemplateTab(){
		driver.waitForElementPresent(THRESHOLD_POPUP_OK);
		driver.click(THRESHOLD_POPUP_OK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickAddMoreLinesLinkInAutoShipTemplateTab(){
		driver.waitForElementPresent(ADD_MORE_LINES_LINK);
		driver.click(ADD_MORE_LINES_LINK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getSubtotalInAutoshipTemplateTab(){
		driver.waitForElementPresent(SUBTOTAL_VALUE);
		String autoshipTemplateCID = driver.findElement(SUBTOTAL_VALUE).getText();
		String []subtotal=autoshipTemplateCID.split("\\$");
		String value=subtotal[1];
		logger.info("subtotal fetched is "+value);
		return value;
	}

	public void updateQuantityOfSecondProduct(String qty){
		driver.waitForElementPresent(QUANTITY_TEXT_BOX_OF_SECOND_PRODUCT);
		driver.type(QUANTITY_TEXT_BOX_OF_SECOND_PRODUCT, qty);
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(UPDATE_QUANTITY_LINK_OF_SECOND_PRODUCT);
		driver.click(UPDATE_QUANTITY_LINK_OF_SECOND_PRODUCT);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void enterOrderNote(String orderNote){
		driver.waitForElementPresent(ORDER_NOTE_TEXT_BOX);
		driver.type(ORDER_NOTE_TEXT_BOX, orderNote);
		driver.waitForElementPresent(ORDER_NOTE_ADD_BUTTON);
		driver.click(ORDER_NOTE_ADD_BUTTON);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyOrderNoteAdded(String orderNoteText){
		return driver.isElementPresent(By.xpath(String.format(orderNoteUILoc, orderNoteText)));
	}

	public void removeProductInOrderDetailInAutoshipTemplateTab(String subtotal){
		double subtotalValue=Double.parseDouble(subtotal);
		int size=driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size();
		if(subtotalValue>100.00 && size>=2){
			clickRemoveLinkOfOrderDetailInAutoShipTemplateTab();
			if(driver.isElementPresent(THRESHOLD_POPUP)){
				clickOKOfThresholdPopupInAutoshipTemplateTab();
				updateQuantityOfSecondProduct("2");
				clickRemoveLinkOfOrderDetailInAutoShipTemplateTab();
			}else{
				logger.info("Either subtotal is not greater than 80 or count of product is less than 2");
			}
		}
	}

	public boolean verifyProductDetailOfNewTabInAutoshipTemplateTab(){
		driver.waitForElementPresent(VIEW_PRODUCT_DETAIL);
		return driver.isElementPresent(VIEW_PRODUCT_DETAIL);
	}

	public void clickViewProductPageLinkInAutoshipTemplateTemplateTab(){
		driver.waitForElementPresent(VIEW_PRODUCT_PAGE_LINK);
		driver.click(VIEW_PRODUCT_PAGE_LINK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyAppliedPromotionsInAutoshipTemplateTab(String details){
		try{
			driver.waitForElementPresent(By.xpath(String.format(appliedPromotionsLoc, details)));
			return driver.isElementPresent(By.xpath(String.format(appliedPromotionsLoc, details)));
		}catch(NoSuchElementException e){
			logger.info("No entries in applied promotion section");
		}
		return false;

	}

	public String getAutoShipTemplateStatus(){
		driver.waitForElementPresent(AUTOSHIP_TEMPLATE_STATUS);
		return driver.findElement(AUTOSHIP_TEMPLATE_STATUS).getText().trim();
	}

	public boolean verifyUpdateButtonIsDisabledAfterCancelPulseSubscription(){
		driver.waitForElementPresent(AUTOSHIP_TEMPLATE_UPDATE_BTN);
		return driver.isElementPresent(AUTOSHIP_TEMPLATE_UPDATE_BTN);
	}

	public boolean verifyRunNowButtonIsDisabledAfterCancelPulseSubscription(){
		driver.waitForElementPresent(AUTOSHIP_TEMPLATE_RUN_NOW_BTN);
		return driver.isElementPresent(AUTOSHIP_TEMPLATE_RUN_NOW_BTN);
	}

	public void clickCancelAutoship(){
		driver.waitForElementPresent(CANCEL_AUTOSHIP_TEMPLATE_LOC);
		driver.click(CANCEL_AUTOSHIP_TEMPLATE_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickConfirmCancelAutoshipTemplatePopup(){
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(CANCEL_AUTOSHIP_TEMPLATE_POPUP_CONFIRM_BTN_LOC);
		driver.click(CANCEL_AUTOSHIP_TEMPLATE_POPUP_CONFIRM_BTN_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getAutoshipTemplateShippingAddressName(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_PROFILE_NAME);
		String shippingProfileName = driver.findElement(SHIPPING_ADDRESS_PROFILE_NAME).getText();
		return shippingProfileName;
	}

	public void clickEditAutoshiptemplate(){
		driver.waitForElementPresent(EDIT_AUTOSHIP_LINK);
		driver.click(EDIT_AUTOSHIP_LINK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getCRPAutoshipDateFromCalendar(){
		driver.waitForElementPresent(CURRENT_CRP_AUTOSHIP_DATE);
		return driver.findElement(CURRENT_CRP_AUTOSHIP_DATE).getAttribute("value");
	}

	public String addOneMoreDayInCRPAutoshipDate(String date){
		String month = date.split("\\ ")[0];
		String day = date.split("\\ ")[1].split("\\,")[0];
		String year = date.split("\\ ")[2];
		if(Integer.parseInt(day)<17){
			int day1 = Integer.parseInt(day)+1;
			day = ""+day1;
		}
		logger.info("date after add one day "+month+" "+day+","+" "+year);
		return month+" "+day+","+" "+year;
	}

	public void enterCRPAutoshipDate(String date){
		driver.waitForElementPresent(CURRENT_CRP_AUTOSHIP_DATE);
		WebElement inputField = driver.findElement(CURRENT_CRP_AUTOSHIP_DATE);
		inputField.clear();
		inputField.sendKeys(date+"\t");
	}

	public boolean IsUpdateBtnDisabledForPulseSubscription(){
		driver.waitForElementPresent(DISABLED_UPDATE_AUTOSHIP_BTN);
		return driver.isElementPresent(DISABLED_UPDATE_AUTOSHIP_BTN);
	}


	public boolean IsRemoveBtnDisabledForPulseSubscription(){
		driver.waitForElementPresent(DISABLED_REMOVE_AUTOSHIP_BTN);
		return driver.isElementPresent(DISABLED_REMOVE_AUTOSHIP_BTN);
	}

	public boolean IsInputTxtDisabledForPulseSuscription(){
		driver.waitForElementPresent(DISABLED_QTY_INPUT_TXT);
		return driver.isElementPresent(DISABLED_QTY_INPUT_TXT);
	}

	public void clickRemoveLinkOfOrderDetailInAutoShipTemplateTab(){
		while(true){
			driver.waitForElementPresent(FIRST_REMOVE_LINK_OF_ORDER_DETAIL);
			driver.click(FIRST_REMOVE_LINK_OF_ORDER_DETAIL);
			driver.waitForCSCockpitLoadingImageToDisappear();
			if(driver.isElementPresent(THRESHOLD_POPUP)==true){
				break;
			}else{
				continue;
			}
		}
	}

	public void addProductInAutoShipCartTillHaveTwoProduct(){
		boolean autoshipProductList=true;
		do{
			if(driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size()>=2){
				break;
			}else{
				clickAddMoreLinesLinkInAutoShipTemplateTab();
				clearCatalogSearchFieldAndClickSearchBtn();
				selectValueFromSortByDDInCartTab("Price: High to Low");
				selectCatalogFromDropDownInCartTab(); 
				String newRandomProductSequenceNumber = String.valueOf(getRandomProductWithSKUFromSearchResult()); 
				String SKUValues = getCustomerSKUValueInCartTab(newRandomProductSequenceNumber);
				searchSKUValueInCartTab(SKUValues);
				clickAddToCartBtnTillProductAddedInCartTab();
				clickCheckoutBtnInCartTab();
				clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
				if(driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size()>=2){
					autoshipProductList=false; 
					break;
				}
			}
		}while(autoshipProductList);
	}

	public void removeProductInOrderDetailInAutoshipTemplateTab(){
		clickAddMoreLinesLinkInAutoShipTemplateTab();
		selectValueFromSortByDDInCartTab("Consultant Price: Low to High");
		selectCatalogFromDropDownInCartTab(); 
		String newRandomProductSequenceNumber = String.valueOf(getRandomProductWithSKUFromSearchResult()); 
		String SKUValues = getCustomerSKUValueInCartTab(newRandomProductSequenceNumber);
		searchSKUValueInCartTab(SKUValues);
		clickAddToCartBtnTillProductAddedInCartTab();
		clickCheckoutBtnInCartTab();
		clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		clickRemoveLinkOfOrderDetailInAutoShipTemplateTab();
		clickOKOfThresholdPopupInAutoshipTemplateTab();
		int size=driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size();
		while(true){
			if(size>=2 ){
				updateQuantityOfSecondProduct("20");
				clickRemoveLinkOfOrderDetailInAutoShipTemplateTab();
				clickOKOfThresholdPopupInAutoshipTemplateTab();
				break;
			}else{
				clickAddMoreLinesLinkInAutoShipTemplateTab();
				clearCatalogSearchFieldAndClickSearchBtn();
				selectValueFromSortByDDInCartTab("Consultant Price: Low to High");
				selectCatalogFromDropDownInCartTab(); 
				newRandomProductSequenceNumber = String.valueOf(getRandomProductWithSKUFromSearchResult()); 
				SKUValues = getCustomerSKUValueInCartTab(newRandomProductSequenceNumber);
				searchSKUValueInCartTab(SKUValues);
				clickAddToCartBtnTillProductAddedInCartTab();
				clickCheckoutBtnInCartTab();
				clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
				clickRemoveLinkOfOrderDetailInAutoShipTemplateTab();
				clickOKOfThresholdPopupInAutoshipTemplateTab();
				size=driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size();
				continue;
			}
		}
	}

	public String getConfirmMessageAfterClickOnRunNowBtn(){
		driver.waitForElementPresent(CONFIRM_MSG_OF_RUN_NOW);
		return driver.findElement(CONFIRM_MSG_OF_RUN_NOW).getText();
	}

	public String getOrderNumberFromConfirmationMsg(String message){
		String msg = message.split("number")[1];
		logger.info("Created order number is "+msg.split("\\.")[0]);
		return msg.split("\\.")[0].trim();
	}

	public void clickAddNewPaymentAddressInCheckoutTab(){
		driver.waitForElementPresent(ADD_NEW);
		driver.click(ADD_NEW);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}
	private static final By ADD_NEW = By.xpath("//span[text()='Payment']/following::td[contains(text(),'Add New')]");

	public boolean verifyThresholdPopupForUS(){
		try{
			driver.quickWaitForElementPresent(THRESHOLD_POPUP_FOR_US);
			return driver.isElementPresent(THRESHOLD_POPUP_FOR_US);
		}catch(Exception e){
			logger.info("There is no product having SV value less than 100/80");
			return true;
		}
	}

	public boolean verifyThresholdPopupForCA(){
		try{
			driver.quickWaitForElementPresent(THRESHOLD_POPUP_FOR_CA);
			return driver.isElementPresent(THRESHOLD_POPUP_FOR_CA);
		}catch(Exception e){
			logger.info("There is no product having SV value less than 100/80");
			return true;
		}
	}

	public String getNextDueDateOfCRPAutoship(){
		driver.waitForElementPresent(NEXT_DUE_DATE_OF_CRP_AUTOSHIP);
		return driver.findElement(NEXT_DUE_DATE_OF_CRP_AUTOSHIP).getText().trim();
	}

	public String convertCRPDateToFormat(String UIDate){
		String UIMonth=null;
		String[] splittedDate = UIDate.split("\\/");
		String date = splittedDate[1];
		String month = splittedDate[0];
		if(Character.toString(date.charAt(0)).equals("0")){
			date = date.substring(1);
		}
		switch (Integer.parseInt(month)) {  
		case 1:
			UIMonth="January";
			break;
		case 2:
			UIMonth="February";
			break;
		case 3:
			UIMonth="March";
			break;
		case 4:
			UIMonth="April";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="June";
			break;
		case 7:
			UIMonth="July";
			break;
		case 8:
			UIMonth="August";
			break;
		case 9:
			UIMonth="September";
			break;
		case 10:
			UIMonth="October";
			break;
		case 11:
			UIMonth="November";
			break;
		case 12:
			UIMonth="December";
			break;  
		}
		logger.info("Next due date in format "+UIMonth+" "+date+", "+"2016");
		return UIMonth+" "+date+", "+"2016";
	}

	public String getOneMonthOutDate(String date){
		String completeDate[] = date.split(" ");
		String year =completeDate[2];
		String month=completeDate[1].split("\\,")[0];
		String day = completeDate[0];
		int monthCount = 0;
		int yearCount = 0;
		String UIMonth = null;
		if(month.equalsIgnoreCase("January")){
			monthCount=1;
		}else if(month.equalsIgnoreCase("February")){
			monthCount=2;
		}else if(month.equalsIgnoreCase("March")){
			monthCount=3;
		}
		else if(month.equalsIgnoreCase("April")){
			monthCount=4;
		}
		else if(month.equalsIgnoreCase("May")){
			monthCount=5;
		}
		else if(month.equalsIgnoreCase("June")){
			monthCount=6;
		}
		else if(month.equalsIgnoreCase("July")){
			monthCount=7;
		}
		else if(month.equalsIgnoreCase("August")){
			monthCount=8;
		}
		else if(month.equalsIgnoreCase("September")){
			monthCount=9;
		}
		else if(month.equalsIgnoreCase("October")){
			monthCount=10;
		}
		else if(month.equalsIgnoreCase("November")){
			monthCount=11;
		}else if(month.equalsIgnoreCase("December")){
			monthCount=12;
		}else{
			monthCount=0;
		}
		monthCount=monthCount+1;
		if(monthCount==13){
			monthCount=1;
			yearCount=1;
		}
		switch (monthCount) {  
		case 1:
			UIMonth="January";
			break;
		case 2:
			UIMonth="February";
			break;
		case 3:
			UIMonth="March";
			break;
		case 4:
			UIMonth="April";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="June";
			break;
		case 7:
			UIMonth="July";
			break;
		case 8:
			UIMonth="August";
			break;
		case 9:
			UIMonth="September";
			break;
		case 10:
			UIMonth="October";
			break;
		case 11:
			UIMonth="November";
			break;
		case 12:
			UIMonth="December";
			break;  
		}
		if(yearCount==1){
			int yearly=Integer.parseInt(year)+1;
			year=Integer.toString(yearly);
		}
		String dateAfterOneMonth=UIMonth+" "+day+","+" "+year;
		logger.info("Date will be "+dateAfterOneMonth);
		return dateAfterOneMonth;
	}

	public String getOneMonthOutDateAfter17(String date){
		String completeDate[] = date.split(" ");
		String year =completeDate[2];
		String month=completeDate[1].split("\\,")[0];
		int monthCount = 0;
		int yearCount = 0;
		String UIMonth = null;
		if(month.equalsIgnoreCase("January")){
			monthCount=1;
		}else if(month.equalsIgnoreCase("February")){
			monthCount=2;
		}else if(month.equalsIgnoreCase("March")){
			monthCount=3;
		}
		else if(month.equalsIgnoreCase("April")){
			monthCount=4;
		}
		else if(month.equalsIgnoreCase("May")){
			monthCount=5;
		}
		else if(month.equalsIgnoreCase("June")){
			monthCount=6;
		}
		else if(month.equalsIgnoreCase("July")){
			monthCount=7;
		}
		else if(month.equalsIgnoreCase("August")){
			monthCount=8;
		}
		else if(month.equalsIgnoreCase("September")){
			monthCount=9;
		}
		else if(month.equalsIgnoreCase("October")){
			monthCount=10;
		}
		else if(month.equalsIgnoreCase("November")){
			monthCount=11;
		}else if(month.equalsIgnoreCase("December")){
			monthCount=12;
		}else{
			monthCount=0;
		}
		monthCount=monthCount+1;
		if(monthCount==13){
			monthCount=1;
			yearCount=1;
		}
		switch (monthCount) {  
		case 1:
			UIMonth="January";
			break;
		case 2:
			UIMonth="February";
			break;
		case 3:
			UIMonth="March";
			break;
		case 4:
			UIMonth="April";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="June";
			break;
		case 7:
			UIMonth="July";
			break;
		case 8:
			UIMonth="August";
			break;
		case 9:
			UIMonth="September";
			break;
		case 10:
			UIMonth="October";
			break;
		case 11:
			UIMonth="November";
			break;
		case 12:
			UIMonth="December";
			break;  
		}
		if(yearCount==1){
			int yearly=Integer.parseInt(year)+1;
			year=Integer.toString(yearly);
		}
		String dateAfterOneMonth=UIMonth+" "+"17"+","+" "+year;
		logger.info("Date will be After 17 "+dateAfterOneMonth);
		return dateAfterOneMonth;
	}

	public boolean verifyOkBtnOnConfirmMessagePopUp(){   
		driver.waitForElementPresent(CONFIRM_MESSAGE_OK_LOC);
		return driver.isElementPresent(CONFIRM_MESSAGE_OK_LOC);
	}

	public boolean verifyConfirmMessageOrReRunPopupAfterClickOnRunNowBtn(){
		driver.waitForElementPresent(CONFIRM_MSG_OF_RUN_NOW);
		if(driver.isElementPresent(CONFIRM_MSG_OF_RUN_NOW)==true){
			return driver.isElementPresent(CONFIRM_MSG_OF_RUN_NOW);
		}else{
			return driver.isElementPresent(RUN_NOW_AGAIN);
		}
	}

	public boolean verifyDisabledRunNowLinkInOrderFromAutoshipTemplateInAutoshipTemplateTab(){
		return driver.isElementPresent(DISABLED_RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB);     
	}

	public void selectPCPerksCancellationReasonFromDropDownInAutoShipTemplateTab(){
		driver.waitForElementPresent(CANCELLATION_REASON_DD);
		driver.click(CANCELLATION_REASON_DD);
		driver.waitForElementPresent(CANCELLATION_DD_OPTION);
		driver.click(CANCELLATION_DD_OPTION);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void selectPCPerksRequestSourceFromDropDownInAutoShipTemplateTab(){
		driver.waitForElementPresent(REQUEST_SOURCE_DD);
		driver.click(REQUEST_SOURCE_DD);
		driver.waitForElementPresent(REQUEST_SOURCE_DD_OPTION);
		driver.click(REQUEST_SOURCE_DD_OPTION);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void enterPCPerksCancellationMessage(String message){
		driver.waitForElementPresent(PC_PERKS_MESSAGE_TEXT_BOX);
		driver.type(PC_PERKS_MESSAGE_TEXT_BOX, message);
	}

	public void clickDelayAutoshipLink(){
		driver.waitForElementPresent(DELAY_AUTOSHIP_LINK);
		driver.click(DELAY_AUTOSHIP_LINK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void selectNextDelayAutoshipDateInPopup(String noOfDays){
		driver.waitForElementPresent(By.xpath(String.format(changeDelayAutoshipDate, noOfDays)));
		driver.click(By.xpath(String.format(changeDelayAutoshipDate, noOfDays)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getNextDelayAutoshipDateFromPopup(String noOfDays){
		driver.waitForElementPresent(By.xpath(String.format(nextDelayautoshipdateFromPopup, noOfDays)));
		return driver.findElement(By.xpath(String.format(nextDelayautoshipdateFromPopup, noOfDays))).getText();
	}

	public void clickSaveBtnInPopUP() {
		driver.waitForElementPresent(POPUP_SAVE_BUTTON_LOC);
		driver.click(POPUP_SAVE_BUTTON_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getNextCartDate(){
		driver.waitForElementPresent(NEXT_CART_DATE);
		return driver.findElement(NEXT_CART_DATE).getAttribute("value").trim();
	}

	public String convertCartDateToFormat(String UIDate){
		String UIMonth=null;
		String[] splittedDate = UIDate.split("\\/");
		String date = splittedDate[1];
		if(Character.toString(date.charAt(0)).equals("0")){
			date = date.substring(1);
		}
		String month = splittedDate[0];
		String year  = splittedDate[2];
		switch (Integer.parseInt(month)) {  
		case 1:
			UIMonth="January";
			break;
		case 2:
			UIMonth="February";
			break;
		case 3:
			UIMonth="March";
			break;
		case 4:
			UIMonth="April";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="June";
			break;
		case 7:
			UIMonth="July";
			break;
		case 8:
			UIMonth="August";
			break;
		case 9:
			UIMonth="September";
			break;
		case 10:
			UIMonth="October";
			break;
		case 11:
			UIMonth="November";
			break;
		case 12:
			UIMonth="December";
			break;  
		}
		logger.info("Next due date in format "+UIMonth+" "+date+", "+year);
		return UIMonth+" "+date+", "+year;
	}

	public boolean isEditTemplateBtnDisabled(){
		driver.waitForElementPresent(DISABLED_EDIT_AUTOSHIP_TEMPLATE_BTN_);
		return driver.isElementPresent(DISABLED_EDIT_AUTOSHIP_TEMPLATE_BTN_);
	}

	public void enterQtyOfFirstProduct(String qty){
		driver.waitForElementPresent(QTY_INPUT_TXT_BOX_OF_FIRST_PRODUCT);
		driver.type(QTY_INPUT_TXT_BOX_OF_FIRST_PRODUCT, qty);
	}

	public void clickUpdateLinkOfOrderDetail(){
		driver.waitForElementPresent(FIRST_UPDATE_LINK_OF_ORDER_DETAIL);
		driver.click(FIRST_UPDATE_LINK_OF_ORDER_DETAIL);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}


	public void clickRemoveLinkOfOrderDetailForOrderTotal(){
		while(true){
			driver.waitForElementPresent(FIRST_REMOVE_LINK_OF_ORDER_DETAIL);
			driver.pauseExecutionFor(2000);
			driver.click(FIRST_REMOVE_LINK_OF_ORDER_DETAIL);
			driver.waitForCSCockpitLoadingImageToDisappear();
			if(driver.isElementPresent(THRESHOLD_POPUP_FOR_ORDER_TOTAL)==true){
				break;
			}else{
				continue;
			}
		}
	}

	public boolean verifyThresholdPopupForOrderTotal(){
		driver.quickWaitForElementPresent(THRESHOLD_POPUP_FOR_ORDER_TOTAL);
		return driver.isElementPresent(THRESHOLD_POPUP_FOR_ORDER_TOTAL);
	}

	public void clickAddCardBtnInPaymentInfoSection(){
		driver.waitForElementPresent(ADD_CARD_BTN);
		driver.click(ADD_CARD_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCloseOfAddANewPaymentProfilePopup(){
		driver.waitForElementPresent(CLOSE_BTN_OF_ADD_A_NEW_PAYMENT_PROFILE_POPUP);
		driver.click(CLOSE_BTN_OF_ADD_A_NEW_PAYMENT_PROFILE_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isSelectBillingAddressMsgPresent(){
		driver.waitForElementPresent(SELECT_BILLING_ADDRESS_ERROR_MSG);
		return driver.isElementPresent(SELECT_BILLING_ADDRESS_ERROR_MSG);
	}

	public void clickAddANewAddressOfAddANewPaymentProfilePopup(){
		driver.waitForElementPresent(ADD_A_NEW_ADDRESS_IN_PAYMENT_PROFILE_POPUP);
		driver.click(ADD_A_NEW_ADDRESS_IN_PAYMENT_PROFILE_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickPaymentInfoAddressDropDown(){
		driver.waitForElementPresent(PAYMENT_INFO_ADDRESS_DD);
		driver.click(PAYMENT_INFO_ADDRESS_DD);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getDefaultSelectedPaymentInfoAddress(){
		driver.waitForElementPresent(PAYMENT_INFO_PROFILE_NAME_FROM_DD);
		String profileName=driver.findElement(PAYMENT_INFO_PROFILE_NAME_FROM_DD).getAttribute("value");
		logger.info("profile name from dropdown is "+profileName);
		return profileName;
	}

	public boolean verifyNextCRPCartInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(NEXT_CRP_CART_LINK);
		return driver.isElementPresent(NEXT_CRP_CART_LINK);      
	}

	public String getQuantityOfProductInAutoshipTemplateTabPage(String productNumber){
		driver.waitForElementPresent(By.xpath(String.format(ProductCountOnAutoshipTemplateLoc, productNumber)));
		String count=driver.findElement(By.xpath(String.format(ProductCountOnAutoshipTemplateLoc, productNumber))).getAttribute("value");
		logger.info("Qty for product"+productNumber+"is "+count);
		return count;
	}

	public int getCountOfProductInAutoshipTemplateTabPage(){
		int count=driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size();
		logger.info("count for total products in autoship template is "+count);
		return count;
	}

	public void addProductInAutoShipCartTillHaveRequiredProduct(int reqProduct,String profileName){
		boolean addBillingProfile=true;
		boolean autoshipProductList=true;
		do{
			if(driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size()>=reqProduct){
				break;
			}else{
				clickAddMoreLinesLinkInAutoShipTemplateTab();
				clearCatalogSearchFieldAndClickSearchBtn();
				selectValueFromSortByDDInCartTab("Price: High to Low");
				selectCatalogFromDropDownInCartTab(); 
				String newRandomProductSequenceNumber = String.valueOf(getRandomProductWithSKUFromSearchResult()); 
				String SKUValues = getCustomerSKUValueInCartTab(newRandomProductSequenceNumber);
				searchSKUValueInCartTab(SKUValues);
				clickAddToCartBtnInCartTab();
				//clickAddToCartBtnTillProductAddedInCartTab();
				clickCheckoutBtnInCartTab();
				if(addBillingProfile){
					clickAddNewPaymentAddressInCheckoutTab();
					enterBillingInfo(TestConstants.CARD_NUMBER,profileName,TestConstants.SECURITY_CODE);
					clickSaveAddNewPaymentProfilePopUP();
					addBillingProfile=false;
				}
				enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
				clickUseThisCardBtnInCheckoutTab();
				clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
				if(driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size()>=reqProduct){
					autoshipProductList=false; 
					break;
				}
			}
		}while(autoshipProductList);
	}

	public void clickRemoveLinkToRemoveProductFromAutoshipCart(){
		driver.waitForElementPresent(FIRST_REMOVE_LINK_OF_ORDER_DETAIL);
		driver.pauseExecutionFor(2000);
		driver.click(FIRST_REMOVE_LINK_OF_ORDER_DETAIL);
		driver.waitForCSCockpitLoadingImageToDisappear();		   
	}

	public int getQuantityOfAllProductInAutoshipTemplateTabPage(){
		driver.waitForElementPresent(REMOVE_LINK_OF_ORDER_DETAIL);
		int count=driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size();
		int returnCountValue=0;
		for(int i=1;i<=count;i++){
			driver.waitForElementPresent(By.xpath(String.format(ProductCountOnAutoshipTemplateLoc,i)));
			String counts=driver.findElement(By.xpath(String.format(ProductCountOnAutoshipTemplateLoc,i))).getAttribute("value");
			returnCountValue=returnCountValue+Integer.parseInt(counts);
			logger.info("Qty for product"+i+"is "+counts);
		}
		logger.info("Quantity sum for all products is "+returnCountValue);
		return returnCountValue;
	}

	public void clickCancelButtonOfCancelAutoshipTemplatePopup(){
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(CANCEL_AUTOSHIP_TEMPLATE_POPUP_CANCEL_BTN_LOC);
		driver.click(CANCEL_AUTOSHIP_TEMPLATE_POPUP_CANCEL_BTN_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isEditTemplateInAutoshipTemplateTabPresent(){
		driver.waitForElementPresent(DISABLED_EDIT_TEMPLATE);
		return driver.isElementPresent(DISABLED_EDIT_TEMPLATE);  
	}

	public boolean isCancelAutoshipPopupAlertPresent(){
		driver.waitForElementPresent(CANCEL_AUTOSHIP_POPUP_ALERT);
		return driver.isElementPresent(CANCEL_AUTOSHIP_POPUP_ALERT);  
	}

	public boolean IsPageTabSelected(String tabName){
		boolean flag = false;
		String isSelected = driver.findElement(By.xpath(String.format(allAboveTabOfCSCockpit, tabName))).getAttribute("z.sel");
		if(isSelected.equals("true")){
			flag = true;
		}
		return flag;
	}

	public int getNumberOfConsecutiveAutoshipOrdersFromTemplate(){
		driver.waitForElementPresent(NUMBER_OF_CONSECUTIVE_AUTOSHIP_ORDERS_FROM_TEMPLATE);
		return Integer.parseInt(driver.findElement(NUMBER_OF_CONSECUTIVE_AUTOSHIP_ORDERS_FROM_TEMPLATE).getAttribute("value"));
	}

	public boolean verifyNextPCPerksCartInAutoshipTemplateTab(){
		driver.quickWaitForElementPresent(NEXT_PC_PERKS_CART);
		return driver.isElementPresent(NEXT_PC_PERKS_CART);      
	}

	public String getPaymentInfoAddressNameFromAddressDropDown(){
		driver.waitForElementPresent(PAYMENT_INFO_PROFILE_NAME_FROM_DD);
		String addressName = driver.findElement(PAYMENT_INFO_PROFILE_NAME_FROM_DD).getAttribute("value");
		return addressName;
	}

	public void clickEditUnderPaymentInfoSection(){
		driver.waitForElementPresent(EDIT_PAYMENT_INFO);
		driver.click(EDIT_PAYMENT_INFO);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCloseOfEditPaymentProfilePopup(){
		driver.waitForElementPresent(CLOSE_BTN_OF_EDIT_PAYMENT_PROFILE_POPUP);
		driver.click(CLOSE_BTN_OF_EDIT_PAYMENT_PROFILE_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isAddressValuesPresentInPaymentProfilePopup(String addressValue){
		driver.waitForElementPresent(By.xpath(String.format(addressValuesInPaymentProfilePopup, addressValue)));
		return driver.isElementPresent(By.xpath(String.format(addressValuesInPaymentProfilePopup, addressValue)));
	}

	public void clickFirstCreditCardInPaymentAddressDropDown(){
		driver.waitForElementPresent(FIRST_CREDIT_CARD_PAYMENT_INFO);
		driver.click(FIRST_CREDIT_CARD_PAYMENT_INFO);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}


	public String getAutoShipTemplateType(){
		driver.waitForElementPresent(AUTOSHIP_TEMPLATE_TYPE);
		return driver.findElement(AUTOSHIP_TEMPLATE_TYPE).getText().trim();
	}

	public void clickAddNewAddressButtonUnderShippingAddress() {
		driver.waitForElementPresent(ADD_NEW_ADDRESS_BTN_UNDER_SHIPPING_ADDRESS);
		driver.click(ADD_NEW_ADDRESS_BTN_UNDER_SHIPPING_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getShippingAddressLine1UnderShippingAddress(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_LINE1);
		return driver.findElement(SHIPPING_ADDRESS_LINE1).getText().trim();
	}

	public boolean isSelectValidFutureDatePopupPresent(){
		driver.waitForElementPresent(SELECT_VALID_FUTURE_DATE_POPUP);
		return driver.isElementPresent(SELECT_VALID_FUTURE_DATE_POPUP);  
	}

	public String getShippingMethodNameFromUIUnderShippingAddressInAutoshipTemplateTab(){
		driver.waitForElementPresent(SHIPPING_METHOD_NAME_FROM_UI);
		return driver.findElement(SHIPPING_METHOD_NAME_FROM_UI).getText().trim();
	}

	public boolean verifyCancelAutoshipTemplateLinkInAutoshipTemplateTabForRandomUser(){
		driver.quickWaitForElementPresent(ALREADY_CANCEL_AUTOSHIP_DISABLE_LINK);
		return driver.isElementPresent(ALREADY_CANCEL_AUTOSHIP_DISABLE_LINK);  
	}

	public int getTotalSVValue(){
		driver.waitForElementPresent(TOTAL_SV_VALUE);
		String value = driver.findElement(TOTAL_SV_VALUE).getText();
		int SVValue = Integer.parseInt(value);
		logger.info("Total SV Value is: "+SVValue);
		return SVValue;
	}

	public void addProductInAutoShipCartTillHaveRequiredProductToBeAdded(int reqProduct,String profileName){
		boolean addBillingProfile=true;
		boolean autoshipProductList=true;
		String currentPageNo = "0";
		do{
			if(driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size()>=reqProduct){
				logger.info("Product count is equal to requirement");
				break;
			}else{

				int pageNo =1+Integer.parseInt(currentPageNo);
				logger.info("Entered Page no "+pageNo);
				clickAddMoreLinesLinkInAutoShipTemplateTab();
				logger.info("Add more lines button clicked");
				clearCatalogSearchFieldAndClickSearchBtn();
				logger.info("Catalog search field cleared");
				selectValueFromSortByDDInCartTab("Price: High to Low");
				logger.info("Price filter selected is: Higt to low");
				selectCatalogFromDropDownInCartTab();
				enterRandomPageNumber(""+pageNo);
				logger.info("Enter random page number for first product of page");
				String newRandomProductSequenceNumber = String.valueOf(getRandomProductWithSKUFromSearchResult()); 
				String SKUValues = getCustomerSKUValueInCartTab(newRandomProductSequenceNumber);
				logger.info("SKU value is: "+SKUValues);
				searchSKUValueInCartTab(SKUValues);
				currentPageNo = clickAddToCartBtnInCartTabAndReturnCurrentPageNo(""+pageNo);
				logger.info("Current Page no "+currentPageNo);
				//clickAddToCartBtnTillProductAddedInCartTab();
				clickCheckoutBtnInCartTab();
				if(addBillingProfile){
					clickAddNewPaymentAddressInCheckoutTab();
					enterBillingInfo(TestConstants.CARD_NUMBER,profileName,TestConstants.SECURITY_CODE);
					clickSaveAddNewPaymentProfilePopUP();
					addBillingProfile=false;
				}
				enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
				clickUseThisCardBtnInCheckoutTab();
				clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
				if(driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size()>=reqProduct){
					autoshipProductList=false; 
					break;
				}
			}
		}while(autoshipProductList);
	}

	

}