package com.rf.pages.website.cscockpit;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.rf.core.driver.website.RFWebsiteDriver;

public class CSCockpitOrderTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitOrderTabPage.class.getName());

	private static String refundDropDownOptionLoc =  "//td[@class='z-combo-item-text' and contains(text(),'%s')]";
	private static String orderStatusDDLoc = "//span[contains(text(),'Order Status')]//option[text()='%s']";
	private static String cartSectionLoc ="//div[@class='csCartDetailRow']//span[contains(text(),'%s')]";
	private static String orderSectionLoc ="//div[@class='csOrderDetailRow']//span[contains(text(),'%s')]";
	private static String returnQuantityLoc = "//td[text()='%s']";
	private static String orderEntryTaxLoc = "//span[contains(text(),'Tax commited to avatax, doc code :%s')]";
	private static String shippingAndHandlingPopupLoc ="//span[text()='%s']/following::span[1]";
	private static String ProductInfoCheckboxLoc="//div[contains(text(),'Product Info')]/ancestor::div[@class='z-listbox-header']/following::div[1]//tbody[2]/tr[%s]/td[1]//input";
	private static String ReturnQuantityOnPopupLoc="//div[contains(text(),'Product Info')]/ancestor::div[@class='z-listbox-header']/following::div[1]//tbody[2]/tr[%s]/td[6]//div[@class='expectedQtyDropDown']//img";
	private static String RestockingFeeCheckboxInPopupLoc="//div[contains(text(),'Product Info')]/ancestor::div[@class='z-listbox-header']/following::div[1]//tbody[2]/tr[%s]/td[8]//input[@type='checkbox']";
	private static String ReturnTaxOnlyCheckboxInPopupLoc="//div[contains(text(),'Product Info')]/ancestor::div[@class='z-listbox-header']/following::div[1]//tbody[2]/tr[%s]/td[9]//input[@type='checkbox']";
	private static String disabledProductInfoCheckbox="//div[contains(text(),'Product Info')]/ancestor::div[@class='z-listbox-header']/following::div[1]//tbody[2]/tr[%s]/td[1]//input[@disabled='']";
	private static String returnQuantityAnotherLoc = "//div[contains(text(),'Product Info')]/ancestor::div[@class='z-listbox-header']/ancestor::div[contains(@class,'csReturnRequestCreateWidget')]/following-sibling::div//td[text()='%s']";
	private static String rmaNumberOfReturnOrderInReturnRequestSsection  = "//span[contains(text(),'Return Requests')]/following::div[1]//a[contains(text(),'%s')]";
	private static String ProductCountOnAutoshipTemplateLoc ="//div[contains(@class,'csWidgetListbox')]/div[2]//tbody/tr[%s]/td[6]/div";
	private static String disableReturnOnlyTaxCheckboxInPopup="//span[text()='Refund']/following::div[@class='z-listbox-body'][1]//tbody[2]/tr[%s]/td[9]//input[@type='checkbox' and @disabled]";

	private static final By SEARCH_BTN_ANOTHER_LOCATOR = By.xpath("//td[text()='SEARCH']"); 
	private static final By PLACE_ORDER_BUTTON = By.xpath("//td[contains(text(),'PLACE AN ORDER')]");
	private static final By ORDER_NUMBER = By.xpath("//span[contains(text(),'Order #')]/following::span[1]");
	private static final By TOTAL_ORDERS_ON_PLACED_ORDER_DETAILS = By.xpath("//div[@class='orderDetailOrderItemsWidget']//div[@class='z-listbox-body']/table/tbody[2]/tr");
	private static final By ORDERS_DETAIL_ITEMS_LBL = By.xpath("//span[contains(text(),'Order Detail Items')]");
	private static final By TRANSACTION_STATUS_LINK = By.xpath("//div[@class='orderPaymentTransactionsWidget']//div[@class='csListboxContainer']//div[@class='z-listbox-body']//tbody[2]/tr[1]/td[6]//a[contains(@class,'z-toolbar-button')]");
	private static final By TEST_ORDER_CHKBOX = By.xpath("//label[contains(text(),'Test Order')]/preceding::input[1]");
	private static final By ORDER_STATUS = By.xpath("//span[contains(text(),'Order Status')]/following::span[1]");
	private static final By DO_NOT_SHIP_CHKBOX = By.xpath("//label[contains(text(),'Do-not-ship')]/preceding::input[1]");
	private static final By REFUND_ORDER_BTN_ORDER_TAB = By.xpath("//td[text()='REFUND ORDER'][@class='z-button-cm']");
	private static final By RETURN_COMPLETE_ORDER_CHKBOX_REFUND_POPUP = By.xpath("//label[text()='Return Complete Order']/preceding::input[1]");
	private static final By DISABLED_PRODUCT_CHKBOX_REFUND_POPUP = By.xpath("//span[text()='Refund']/following::div[@class='z-listbox-body'][1]//tbody[2]/tr[1]/td[1]//input[@type='checkbox' and @disabled]");
	private static final By DISABLED_RESTOCKING_FEE_CHKBOX_REFUND_POPUP = By.xpath("//span[text()='Refund']/following::div[@class='z-listbox-body'][1]//tbody[2]/tr[1]/td[8]//input[@type='checkbox' and @disabled]");
	private static final By DISABLED_RETURN_TAX_ONLY_CHKBOX_REFUND_POPUP = By.xpath("//span[text()='Refund']/following::div[@class='z-listbox-body'][1]//tbody[2]/tr[1]/td[9]//input[@type='checkbox' and @disabled]");
	private static final By DISABLED_RETURN_ONLY_TAX_CHKBOX_REFUND_POPUP = By.xpath("//span[text()='Refund']/following::div[@class='returnHeaderCheckbox'][1]//label[contains(text(),'Return Only tax')]/preceding::input[1][@disabled]");
	private static final By DISABLED_RETURN_SHIPPING_CHKBOX_REFUND_POPUP = By.xpath("//span[text()='Refund']/following::div[@class='returnHeaderCheckbox'][1]//label[contains(text(),'Return Shipping')]/preceding::input[1][@disabled]");
	private static final By RETURN_SHIPPING_CHKBOX_REFUND_POPUP = By.xpath("//span[text()='Refund']/following::div[@class='returnHeaderCheckbox'][1]//label[contains(text(),'Return Shipping')]/preceding::input[1]");
	private static final By DISABLED_RETURN_HANDLING_CHKBOX_REFUND_POPUP = By.xpath("//span[text()='Refund']/following::div[@class='returnHeaderCheckbox'][1]//label[contains(text(),'Return Handling')]/preceding::input[1][@disabled]");
	private static final By RETURN_HANDLING_CHKBOX_REFUND_POPUP = By.xpath("//span[text()='Refund']/following::div[@class='returnHeaderCheckbox'][1]//label[contains(text(),'Return Handling')]/preceding::input[1]");
	private static final By REFUND_REASON_DROP_DOWN_REFUND_POPUP = By.xpath("//span[text()='Refund']/following::div[@class='returnHeaderCombo'][2]/span[1]//img");
	private static final By REFUND_TYPE_DROP_DOWN_REFUND_POPUP = By.xpath("//span[text()='Refund']/following::div[@class='returnHeaderCombo'][2]/span[3]//img");
	private static final By RETURN_ACTION_DROP_DOWN_REFUND_POPUP = By.xpath("//span[text()='Refund']/following::div[@class='returnHeaderCombo'][2]/span[2]//img");
	private static final By CREATE_BTN_REFUND_POPUP = By.xpath("//td[@class='z-button-cm'][text()='Create']");
	private static final By CONFIRM_BTN_CONFIRMATION_POPUP = By.xpath("//td[@class='z-button-cm'][text()='Confirm']");
	private static final By OK_BTN_RMA_POPUP = By.xpath("//td[@class='z-button-cm'][text()='OK']");
	private static final By NO_REFUNDABLE_ITEMS_LBL = By.xpath("//span[text()='No refundable items']");
	private static final By FIRST_REFUND_TYPE_FROM_DD_REFUND_POPUP = By.xpath("//div[@class='z-combobox-pp'][@zk_ckval='refund type']//tr[1]/td[2]");
	private static final By FIRST_RETURN_ACTION_FROM_DD_REFUND_POPUP = By.xpath("//div[@class='z-combobox-pp'][@zk_ckval='return action' or @zk_ckval='CREDITCARD' ]//tr[1]/td[2]");
	private static final By RMA_TREE_BTN = By.xpath("//span[contains(text(),'Return Requests')]/following::div[1]//div[@class='z-listbox-body']//tbody[2]//tr[1]/td[1]//a");
	private static final By ORIGINATION_VALUE_LBL = By.xpath("//span[contains(text(),'Origination')]/following::span[1]");
	private static final By REFUND_TOTAL_LBL = By.xpath("//span[text()='REFUND TOTAL']/following::span[1]");
	private static final By RETURN_REQUEST_SECTION = By.xpath("//div[@class='orderReturnRequestsWidget']//span[text()='Return Requests']");
	private static final By ORDER_TITLE_CSCOCKPIT_UI_LOC = By.xpath("//span[contains(text(),'Order #')]//following::span[1]");
	private static final By EXISTING_SPONSER_NAME = By.xpath("//span[@class='csCartDetailsValue']");
	private static final By CHANGE_SPONSER_LINK = By.xpath("//a[text()='Change']");
	private static final By SPONSER_SEARCH_TEXT_BOX = By.xpath("//span[text()='Consultant Name or CID']/following::input");
	private static final By SELECT_BUTTON_TO_SELECT_SPONSER = By.xpath("//td[text()='SELECT']");
	private static final By CV_QV_UPDATE_BTN = By.xpath("//span[text()='Order Detail Items']/following::div[1]//td[@class='z-button-cm'][text()='Update']"); 
	private static final By CV_QV_DISABLED_UPDATE_BTN = By.xpath("//span[text()='Order Detail Items']/following::div[1]//td[@class='z-button-cm'][text()='Update']/ancestor::table[1][@class='z-button z-button-disd']");
	private static final By ORDER_HISTORY = By.xpath("//div[@class='csOrderHistory']/div");
	private static final By MODIFICATION_HISTORY = By.xpath("//span[text()='Modification History']");
	private static final By CV_TXT_BOX_LOCATOR = By.xpath("//div[text()='Total CV']/following::input[1]");
	private static final By QV_TXT_BOX_LOCATOR = By.xpath("//div[text()='Total QV']/following::input[2]");
	private static final By CANCEL_BUTTON_LOCATOR_FOR_UPDATE_QV_CV = By.xpath("//td[text()='Cancel']");
	private static final By OK_BUTTON_LOCATOR_FOR_UPDATE_QV_CV = By.xpath("//td[text()='OK']");
	private static final By ORDER_TEMPLATE = By.xpath("//span[contains(text(),'Order #')]");
	private static final By ORDER_TYPE_LOC = By.xpath("//span[contains(text(),'Order Type')]/following::span[1]");
	private static final By PLACE_ORDER_BUTTON_LOC = By.xpath("//td[text()='PLACE AN ORDER']");
	private static final By PAYMENT_PROFILE_NAME_LOC = By.xpath("//span[text()='Payment Info ']//following::div[1]/span");
	private static final By CLOSE_CONFIRMATION_POPUP_LOC = By.xpath("//div[contains(text(),'Refund confirmation')]/div");
	private static final By RETURN_QUANTITY_ON_POPUP_LOC = By.xpath("//div[@class='expectedQtyDropDown']//img");
	private static final By PRODUCT_INFO_CHECBOX_LOC = By.xpath("//div[@class='editorWidgetEditor']//input[@type='checkbox']");
	private static final By CLOSE_POPUP_ADD_NEW_PAYMENT_LOC = By.xpath("//div[@class='z-window-highlighted-header']/div");
	private static final By REFUND_REQUEST_HEADER_LOC = By.xpath("//span[text()='Refund']");
	private static final By ORDER_LEVEL_SECTION_CHECKBOXES_LOC = By.xpath("//label[contains(text(),'Return')]//preceding::input[1]");
	private static final By REFUND_REASON_DD_ON_POPUP_LOC = By.xpath("//input[@value='Refund Reason']//ancestor::span[1]");
	private static final By CREDIT_CARD_DD_ON_POPUP_LOC = By.xpath("//input[@value='CREDITCARD']//ancestor::span[1]");
	private static final By REFUND_TYPE_DD_ON_POPUP_LOC = By.xpath("//input[@value='Refund Type']//ancestor::span[1]");
	private static final By SHIPPING_ADDRESS_AT_TOP_SECTION_LOC = By.xpath("//div[@class='csOrderDetailsAddress']//span[contains(text(),'Shipping Address')]");
	private static final By PAYMENT_INFO_AT_TOP_SECTION_LOC = By.xpath("//div[@class='csObjectRFCreditCardPaymentInfoContainer']//span[contains(text(),'Payment Info')]");
	private static final By RETURN_SHIPPING_CHECKBOX = By.xpath("//label[contains(text(),'Return Shipping')]//preceding::input[1]");
	private static final By RETURN_HANDLING_CHECKBOX = By.xpath("//label[contains(text(),'Return Handling')]//preceding::input[1]");
	private static final By RMA_TEXT_ID = By.xpath("//a[contains(text(),'RMA')]");
	private static final By ORDER_NUMBER_IN_POPUP = By.xpath("//a[contains(text(),'Order #')]");
	private static final By RESTOCKING_FEE_CHECKBOX_IN_POPUP = By.xpath("//div[@class='editorWidgetEditor']//input[@type='checkbox']/ancestor::td/following-sibling::td[7]//input[@type='checkbox']");
	private static final By RMA_NUMBER_ON_POPUP  = By.xpath("//span[contains(text(),'RMA number')]");
	private static final By CONSULTANT_RECEIVING_COMMISSION_NAME  = By.xpath("//span[contains(text(),'Consultant Receiving Commissions')]/following::span[1]");
	private static final By DISABLED_UPDATE_BTN_IN_COMMISSION_INFO  = By.xpath("//span[contains(text(),'Commission Info')]/following::div[@class='csWidgetContent'][1]//button[@disabled='disabled']");
	private static final By COMMISSION_DATE = By.xpath("//span[contains(text(),'Commission Date')]/following::input[@autocomplete ='off'][1]");
	private static final By UPDATE_BTN_IN_COMMISSION_INFO  = By.xpath("//span[contains(text(),'Commission Info')]/following::div[@class='csWidgetContent'][1]//td[contains(text(),'Update')]");
	private static final By ONLY_TAX_RETURN_VALUE_IN_RETURN_REQUEST = By.xpath("//div[text()='Only Tax Returned']/following::table[1]//tbody[2]//tr[2]/td[4]//span");
	private static final By RETURN_ONLY_TAX_CHKBOX_REFUND_POPUP = By.xpath("//label[contains(text(),'Return Only tax')]/preceding::input[1]");
	private static final By GET_SKU_VALUE_ORDER_DETAIL=By.xpath("//div[@class='csWidgetMasterDetailListboxCellLine1']/span");
	private static final By CHANGE_CONSULTANT_RECEIVING_COMMISSIONS_POPUP = By.xpath("//div[text()='CHANGE CONSULTANT RECEIVING COMMISSIONS']");
	private static final By TEXTFIELD_OF_CONSULTANT_NAME_OR_CID_IN_CONSULTANT_RECEIVING_COMMISSIONS_POPUP = By.xpath("//span[text()='Consultant Name or CID']/following-sibling::input");
	private static final By FIRST_NAME_CHANGE_CONSULTANT_RECEIVING_COMMISSIONS_POPUP = By.xpath("//td[text()='SELECT']/ancestor::tr/td[2]//span");
	private static final By LAST_NAME_CHANGE_CONSULTANT_RECEIVING_COMMISSIONS_POPUP = By.xpath("//td[text()='SELECT']/ancestor::tr/td[3]//span");
	private static final By SELECT_BTN_IN_CHANGE_CONSULTANT_RECEIVING_COMMISSIONS_POPUP = By.xpath("//td[text()='SELECT']");
	private static final By CREATE_NEW_ADDRESS_BTN_LOC = By.xpath("//td[text()='Create new address']");
	private static final By REFUND_TAX_LBL = By.xpath("//span[text()='Refunded Tax']/following::span[1]");
	private static final By ORDER_NUMBER_TXT_BOX_LOC = By.xpath("//span[text()='Order Number']/following-sibling::input[1]");
	private static final By ORIGINATION_NAME_LOC = By.xpath("//span[text()='Origination:']/following-sibling::span");
	private static final By SEARCH_BTN_LOC = By.xpath("//td[text()='Search']");

	protected RFWebsiteDriver driver;

	public CSCockpitOrderTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public String getOrderNumberInOrderTab(){
		driver.waitForElementPresent(ORDER_NUMBER);
		return driver.findElement(ORDER_NUMBER).getText().split("-")[0].trim();
	}


	public boolean isPaymentTransactionStatusClickableOnOrderTab(){
		driver.waitForElementPresent(TRANSACTION_STATUS_LINK);
		try{
			driver.findElement(TRANSACTION_STATUS_LINK);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public boolean verifyTestOrderCheckBoxIsSelectedInOrderTab(){
		driver.quickWaitForElementPresent(TEST_ORDER_CHKBOX);
		return driver.findElement(TEST_ORDER_CHKBOX).isSelected();
	}

	public String getOrderStatusAfterPlaceOrderInOrderTab(){
		driver.waitForElementPresent(ORDER_STATUS);
		return driver.findElement(ORDER_STATUS).getText();
	}



	public boolean verifyDoNotShipCheckBoxIsSelectedInOrderTab(){
		driver.quickWaitForElementPresent(DO_NOT_SHIP_CHKBOX);
		return driver.findElement(DO_NOT_SHIP_CHKBOX).isSelected();
	}

	public String clickRefundOrderBtnOnOrderTab(String order){
		driver.waitForElementPresent(REFUND_ORDER_BTN_ORDER_TAB);
		driver.click(REFUND_ORDER_BTN_ORDER_TAB);
		if(driver.isElementPresent(NO_REFUNDABLE_ITEMS_LBL)){
			while(true){
				clickCloseRefundRequestPopUP();
				clickOrderSearchTab();
				String randomCustomer = String.valueOf(getRandomCustomerFromSearchResult());
				order=clickAndReturnCIDNumberInCustomerSearchTab(randomCustomer);
				clickRefundOrderBtnOnOrderTab();
				if(driver.isElementPresent(NO_REFUNDABLE_ITEMS_LBL)){
					continue;
				}else{
					break;
				}
			}
		}
		driver.waitForCSCockpitLoadingImageToDisappear();
		return order;
	}

	public void clickRefundOrderBtnOnOrderTab(){
		driver.waitForElementPresent(REFUND_ORDER_BTN_ORDER_TAB);
		driver.click(REFUND_ORDER_BTN_ORDER_TAB);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickRMATreeBtnUnderReturnRequestOnOrderTab(){
		driver.waitForElementPresent(RMA_TREE_BTN);
		driver.click(RMA_TREE_BTN);
		driver.waitForLoadingImageToDisappear();
	}

	public String getOrderNumberFromCsCockpitUIOnOrderTab() {
		String orderNumberOnOrderTab = driver.findElement(ORDER_TITLE_CSCOCKPIT_UI_LOC).getText();
		String []orderNumber = orderNumberOnOrderTab.split("\\ ");
		System.out.println(orderNumber[0]);
		return orderNumber[0];
	}

	public String getOrderTitleFromCsCockpitUIOnOrderTab() {
		String orderTitle = driver.findElement(ORDER_TITLE_CSCOCKPIT_UI_LOC).getText();
		System.out.println(orderTitle);
		return orderTitle;
	}

	public String getExistingSponserNameInOrderTab(){
		driver.waitForElementPresent(EXISTING_SPONSER_NAME);
		return driver.findElement(EXISTING_SPONSER_NAME).getText();
	}

	public void clickChangeSponserLinkInOrderTab(){
		driver.waitForElementPresent(CHANGE_SPONSER_LINK);
		driver.click(CHANGE_SPONSER_LINK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void enterConsultantCIDAndClickSearchInOrderTab(String cid){
		driver.waitForElementPresent(SPONSER_SEARCH_TEXT_BOX);
		driver.type(SPONSER_SEARCH_TEXT_BOX, cid);
		driver.click(SEARCH_BTN_ANOTHER_LOCATOR);
	}

	public void clickSelectToSelectSponserInOrderTab(){
		driver.waitForElementPresent(SELECT_BUTTON_TO_SELECT_SPONSER);
		driver.click(SELECT_BUTTON_TO_SELECT_SPONSER);
	}

	public String getNewSponserNameFromUIInOrderTab(){
		driver.waitForElementPresent(EXISTING_SPONSER_NAME);
		return driver.findElement(EXISTING_SPONSER_NAME).getText().trim();
	}

	public boolean isOrderTemplateDisplayedInOrderTab(){
		driver.quickWaitForElementPresent(ORDER_TEMPLATE);
		return driver.isElementPresent(ORDER_TEMPLATE);  
	}

	public int getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder(){
		driver.waitForElementPresent(ORDERS_DETAIL_ITEMS_LBL);
		return driver.findElements(TOTAL_ORDERS_ON_PLACED_ORDER_DETAILS).size();
	}

	public boolean checkReturnCompleteOrderChkBoxOnRefundPopUpAndReturnTrueElseFalse(){
		driver.waitForElementPresent(RETURN_COMPLETE_ORDER_CHKBOX_REFUND_POPUP);
		if(driver.isElementPresent(By.xpath("//span[contains(text(),'This Order was placed over 120 days ago')]"))==false){
			driver.click(RETURN_COMPLETE_ORDER_CHKBOX_REFUND_POPUP);
			logger.info("Return Complete Order checkbox clicked");
			driver.waitForCSCockpitLoadingImageToDisappear();
			return true;
		}
		else{
			driver.click(RETURN_SHIPPING_CHKBOX_REFUND_POPUP);
			logger.info("Return Shipping checkbox clicked");
			driver.waitForCSCockpitLoadingImageToDisappear();
			driver.click(RETURN_HANDLING_CHKBOX_REFUND_POPUP);
			logger.info("Return Handling checkbox clicked");
			driver.waitForCSCockpitLoadingImageToDisappear();
		}
		return false;

	}

	public boolean areAllCheckBoxesGettingDisabledAfterCheckingReturnCompleteOrderChkBox(){
		driver.waitForElementPresent(DISABLED_PRODUCT_CHKBOX_REFUND_POPUP);
		return driver.isElementPresent(DISABLED_PRODUCT_CHKBOX_REFUND_POPUP)&&
				driver.isElementPresent(DISABLED_RESTOCKING_FEE_CHKBOX_REFUND_POPUP)&&
				driver.isElementPresent(DISABLED_RETURN_TAX_ONLY_CHKBOX_REFUND_POPUP)&&
				driver.isElementPresent(DISABLED_RETURN_ONLY_TAX_CHKBOX_REFUND_POPUP)&&
				driver.isElementPresent(DISABLED_RETURN_SHIPPING_CHKBOX_REFUND_POPUP)&&
				driver.isElementPresent(DISABLED_RETURN_HANDLING_CHKBOX_REFUND_POPUP);	
	}

	public void selectRefundReasonOnRefundPopUp(String reason){
		driver.click(REFUND_REASON_DROP_DOWN_REFUND_POPUP);
		driver.waitForElementPresent(By.xpath(String.format(refundDropDownOptionLoc, reason)));
		driver.click(By.xpath(String.format(refundDropDownOptionLoc, reason)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void selectFirstReturnActionOnRefundPopUp(){
		driver.click(RETURN_ACTION_DROP_DOWN_REFUND_POPUP);
		driver.waitForElementPresent(FIRST_RETURN_ACTION_FROM_DD_REFUND_POPUP);
		driver.click(FIRST_RETURN_ACTION_FROM_DD_REFUND_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void selectFirstRefundTypeOnRefundPopUp(){
		driver.click(REFUND_TYPE_DROP_DOWN_REFUND_POPUP);
		driver.waitForElementPresent(FIRST_REFUND_TYPE_FROM_DD_REFUND_POPUP);
		driver.click(FIRST_REFUND_TYPE_FROM_DD_REFUND_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCreateBtnOnRefundPopUp(){
		driver.waitForElementPresent(CREATE_BTN_REFUND_POPUP);
		driver.click(CREATE_BTN_REFUND_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getRefundTotalFromRefundConfirmationPopUp(){
		driver.waitForElementPresent(REFUND_TOTAL_LBL);
		return driver.findElement(REFUND_TOTAL_LBL).getText();
	}

	public void clickConfirmBtnOnConfirmPopUp(){
		driver.waitForElementPresent(CONFIRM_BTN_CONFIRMATION_POPUP);
		driver.click(CONFIRM_BTN_CONFIRMATION_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickOKBtnOnRMAPopUp(){
		driver.waitForElementPresent(OK_BTN_RMA_POPUP);
		driver.click(OK_BTN_RMA_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}


	public boolean isNoRefundableItemsTxtPresent(){
		driver.waitForElementPresent(NO_REFUNDABLE_ITEMS_LBL);
		return driver.isElementPresent(NO_REFUNDABLE_ITEMS_LBL);
	}

	public void closeNoRefundableItemPopUp(){
		driver.findElement(By.xpath("//div[contains(@class,'csReturnRequestCreateWidget')]//div[contains(@class,'close')]"));
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.pauseExecutionFor(3000);
	}

	public String getOriginationValuePresentAsPerSearchedOrder(){
		return driver.findElement(ORIGINATION_VALUE_LBL).getText();
	}

	public boolean isReturnRequestSectionDisplayed(){
		driver.waitForElementPresent(RETURN_REQUEST_SECTION);
		return driver.findElement(RETURN_REQUEST_SECTION).isDisplayed();
	}


	public void selectOrderStatusFromDropDownInOrderTab(String orderStatus){
		driver.waitForElementPresent(By.xpath(String.format(orderStatusDDLoc, orderStatus)));
		driver.click(By.xpath(String.format(orderStatusDDLoc, orderStatus)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyOrderDetailsIsPresentInOrderTab(String Name){
		driver.waitForElementPresent(By.xpath(String.format(orderSectionLoc, Name)));
		return driver.isElementPresent(By.xpath(String.format(orderSectionLoc, Name)));
	}

	public boolean verifyShippingAddressDetailsArePresentInOrderTab(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_AT_TOP_SECTION_LOC);
		return driver.isElementPresent(SHIPPING_ADDRESS_AT_TOP_SECTION_LOC);
	}

	public boolean verifyPaymentInfoDetailsArePresentInOrderTab(){
		driver.waitForElementPresent(PAYMENT_INFO_AT_TOP_SECTION_LOC);
		return driver.isElementPresent(PAYMENT_INFO_AT_TOP_SECTION_LOC);
	}

	public boolean verifyCartDetailsIsPresentInOrderTab(String Name){
		driver.waitForElementPresent(By.xpath(String.format(cartSectionLoc, Name)));
		return driver.isElementPresent(By.xpath(String.format(cartSectionLoc, Name)));
	}

	public boolean verifyReturnOrderBtnIsPresentInOrderTab(){
		driver.waitForElementPresent(REFUND_ORDER_BTN_ORDER_TAB);
		return driver.isElementPresent(REFUND_ORDER_BTN_ORDER_TAB);
	}

	public boolean verifyPlaceAnOrderOrderBtnIsPresentInOrderTab(){
		driver.waitForElementPresent(PLACE_ORDER_BUTTON);
		return driver.isElementPresent(PLACE_ORDER_BUTTON);
	}

	public boolean verifyOrderHistoryInOrderTab(){
		driver.waitForElementPresent(ORDER_HISTORY);
		return driver.isElementPresent(ORDER_HISTORY);
	} 

	public boolean verifyModificationHistoryInOrderTab(){
		driver.waitForElementPresent(MODIFICATION_HISTORY);
		return driver.isElementPresent(MODIFICATION_HISTORY);
	} 

	public String getCVValueInOrderTab(){
		driver.waitForElementPresent(CV_TXT_BOX_LOCATOR);
		String CVValue = driver.findElement(CV_TXT_BOX_LOCATOR).getAttribute("value");
		return CVValue;
	}

	public String getQVValueInOrderTab(){
		driver.waitForElementPresent(QV_TXT_BOX_LOCATOR);
		String QVValue = driver.findElement(QV_TXT_BOX_LOCATOR).getAttribute("value");
		return QVValue;
	}

	public void enterCVValueInOrderTab(String value){
		driver.waitForElementPresent(CV_TXT_BOX_LOCATOR);
		driver.type(CV_TXT_BOX_LOCATOR, value);
	}

	public void enterQVValueInOrderTab(String value){
		driver.waitForElementPresent(QV_TXT_BOX_LOCATOR);
		driver.type(QV_TXT_BOX_LOCATOR, value);
	} 

	public void clickupdateButtonForCVAndQVInOrderTab(){
		driver.waitForElementPresent(CV_QV_UPDATE_BTN);
		driver.click(CV_QV_UPDATE_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCancelBtnAfterUpdateCVAndQVInOrderTab(){
		driver.waitForElementPresent(CANCEL_BUTTON_LOCATOR_FOR_UPDATE_QV_CV);
		driver.click(CANCEL_BUTTON_LOCATOR_FOR_UPDATE_QV_CV);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickOKBtnAfterUpdateCVAndQVInOrderTab(){
		driver.waitForElementPresent(OK_BUTTON_LOCATOR_FOR_UPDATE_QV_CV);
		driver.click(OK_BUTTON_LOCATOR_FOR_UPDATE_QV_CV);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isQVandCVUpdateBtnDisabledOnOrderTab(){
		driver.waitForElementPresent(CV_QV_UPDATE_BTN);
		return driver.isElementPresent(CV_QV_DISABLED_UPDATE_BTN);
	}

	public boolean validateOrderTypeOnOrderTab(String orderTypeConsultant) {
		driver.waitForElementPresent(ORDER_TYPE_LOC);
		String orderTypeOnUI = driver.findElement(ORDER_TYPE_LOC).getText();
		if(orderTypeOnUI.equalsIgnoreCase(orderTypeConsultant)){
			return true;
		}
		return false;
	}

	public void clickPlaceAnOrderButtonInOrderTab(){
		driver.waitForElementPresent(PLACE_ORDER_BUTTON_LOC);
		driver.click(PLACE_ORDER_BUTTON_LOC);
	}

	public boolean validateNewAddressOnOrderTabPage(String newBillingProfileName) {
		driver.waitForElementPresent(PAYMENT_PROFILE_NAME_LOC);
		if(driver.findElement(PAYMENT_PROFILE_NAME_LOC).getText().contains(newBillingProfileName)){
			return true;
		}
		return false;
	}

	public boolean verifyRefundOrderButtonPresentOnOrderTab() {
		driver.waitForElementPresent(REFUND_ORDER_BTN_ORDER_TAB);
		return driver.isElementPresent(REFUND_ORDER_BTN_ORDER_TAB);
	}

	public boolean verifyRefundRequestPopUpPresent() {
		driver.waitForElementPresent(REFUND_REQUEST_HEADER_LOC);
		return driver.isElementPresent(REFUND_REQUEST_HEADER_LOC);
	}

	public boolean verifyOrderLevelCheckBoxSection() {
		List<WebElement> checkBoxes = driver.findElements(ORDER_LEVEL_SECTION_CHECKBOXES_LOC);
		int numberOfCheckBox = checkBoxes.size();
		if(numberOfCheckBox == 4){
			return true;
		}
		return false;
	}

	public boolean verifyRefundReasonDDPresent() {
		return driver.isElementPresent(REFUND_REASON_DD_ON_POPUP_LOC);
	}

	public boolean verifyCreditCardDDPresent(){
		return driver.isElementPresent(CREDIT_CARD_DD_ON_POPUP_LOC);
	}

	public boolean verifyRefundTypeDDPresent() {
		driver.quickWaitForElementPresent(REFUND_TYPE_DD_ON_POPUP_LOC);
		return driver.isElementPresent(REFUND_TYPE_DD_ON_POPUP_LOC);
	}

	public void clickCloseRefundRequestPopUP(){
		driver.waitForElementPresent(CLOSE_POPUP_ADD_NEW_PAYMENT_LOC);
		driver.click(CLOSE_POPUP_ADD_NEW_PAYMENT_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void checkProductInfoCheckboxInPopUp() {
		driver.click(PRODUCT_INFO_CHECBOX_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void selectReturnQuantityOnPopUp(String quantity) {
		driver.click(RETURN_QUANTITY_ON_POPUP_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.click(By.xpath(String.format(returnQuantityLoc, quantity)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCloseRefundConfirmationPopUP() {
		driver.click(CLOSE_CONFIRMATION_POPUP_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyTaxCommittedEntryInOrderTab(String orderNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderEntryTaxLoc, orderNumber)));
		return driver.isElementPresent(By.xpath(String.format(orderEntryTaxLoc, orderNumber)));  
	}

	public void checkReturnShippingCheckboxInPopUp() {
		driver.click(RETURN_SHIPPING_CHECKBOX);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void checkReturnHandlingCheckboxInPopUp() {
		driver.click(RETURN_HANDLING_CHECKBOX);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isRMAIdTxtPresent(){
		driver.waitForElementPresent(RMA_TEXT_ID);
		return driver.isElementPresent(RMA_TEXT_ID);
	}

	public String getOrderNumbertxtFromRefundRequestPopup(){
		driver.waitForElementPresent(ORDER_NUMBER_IN_POPUP);
		return driver.findElement(ORDER_NUMBER_IN_POPUP).getText();
	}

	public void checkRestockingFeeCheckboxInPopUp() {
		driver.click(RESTOCKING_FEE_CHECKBOX_IN_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getShippingAndHandlingVariousSectionTaxInPopup(String sectionName){
		driver.waitForElementPresent(By.xpath(String.format(shippingAndHandlingPopupLoc, sectionName)));
		String taxValue= driver.findElement(By.xpath(String.format(shippingAndHandlingPopupLoc, sectionName))).getText();
		String[] newValue=taxValue.split("\\$");
		String value=newValue[1];
		logger.info("Fetched value of taxes in popup are "+value);
		return value;
	}

	public String getRMANumberFromPopup(){
		driver.waitForElementPresent(RMA_NUMBER_ON_POPUP);
		String RMANumber = driver.findElement(RMA_NUMBER_ON_POPUP).getText();
		logger.info("RMA number is "+RMANumber);
		return RMANumber;
	}

	public String getConsultantReceivingCommissionsName(){
		driver.waitForElementPresent(CONSULTANT_RECEIVING_COMMISSION_NAME);
		logger.info("Consultant receiving commission name is "+driver.findElement(CONSULTANT_RECEIVING_COMMISSION_NAME).getText());
		return driver.findElement(CONSULTANT_RECEIVING_COMMISSION_NAME).getText();
	}

	public void checkProductInfoCheckboxInPopUp(String rowNum) {
		driver.click(By.xpath(String.format(ProductInfoCheckboxLoc,rowNum)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void selectReturnQuantityOnPopUp(String quantity,String rowNum) {
		driver.waitForElementPresent(By.xpath(String.format(ReturnQuantityOnPopupLoc, rowNum)));
		driver.click(By.xpath(String.format(ReturnQuantityOnPopupLoc, rowNum)));
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.waitForElementPresent(By.xpath(String.format(returnQuantityAnotherLoc, quantity)));
		driver.click(By.xpath(String.format(returnQuantityAnotherLoc, quantity)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void checkRestockingFeeCheckboxInPopUp(String rowNum) {
		driver.click(By.xpath(String.format(RestockingFeeCheckboxInPopupLoc, rowNum)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void checkReturnTaxOnlyCheckboxInPopUp(String rowNum) {
		driver.click(By.xpath(String.format(ReturnTaxOnlyCheckboxInPopupLoc, rowNum)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isProductCheckBoxesGettingDisabledAfterCheckingReturnTaxOnlyChkBox(String rowNum){
		driver.waitForElementPresent(By.xpath(String.format(disabledProductInfoCheckbox,rowNum)));
		return driver.isElementPresent(By.xpath(String.format(disabledProductInfoCheckbox, rowNum)));
	}

	public boolean verifyReturnOrderRMANumberInOrderTab(String rmaNumber){
		driver.waitForElementPresent(By.xpath(String.format(rmaNumberOfReturnOrderInReturnRequestSsection,rmaNumber)));
		return driver.isElementPresent(By.xpath(String.format(rmaNumberOfReturnOrderInReturnRequestSsection,rmaNumber)));
	}

	public boolean isUpdateBtnDisabledInCommissionInfoSection(){
		driver.waitForElementPresent(DISABLED_UPDATE_BTN_IN_COMMISSION_INFO);
		return driver.isElementPresent(DISABLED_UPDATE_BTN_IN_COMMISSION_INFO);
	}

	public String getCommissionDateFromCommissionInfoSection(){
		driver.waitForElementPresent(COMMISSION_DATE);
		return driver.findElement(COMMISSION_DATE).getAttribute("value");
	}

	public void enterCommissionDate(String date){
		driver.waitForElementPresent(COMMISSION_DATE);
		WebElement inputField = driver.findElement(COMMISSION_DATE);
		inputField.clear();
		inputField.sendKeys(date);
	}

	public void clickUpdateBtnInCommissionInfoSection(){
		driver.waitForElementPresent(UPDATE_BTN_IN_COMMISSION_INFO);
		driver.click(UPDATE_BTN_IN_COMMISSION_INFO);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String addOneMoreDayInCommissionInfoDate(String date){
		String month = date.split("\\ ")[0];
		String day = date.split("\\ ")[1].split("\\,")[0];
		String year = date.split("\\ ")[2];
		int updatedDay = Integer.parseInt(day)+1;
		day = ""+updatedDay;
		logger.info("date after add one day "+month+" "+day+","+" "+year);
		return month+" "+day+","+" "+year;
	}

	public String getOnlyTaxReturnValueFromReturnRequestSection(){
		driver.waitForElementPresent(ONLY_TAX_RETURN_VALUE_IN_RETURN_REQUEST);
		return driver.findElement(ONLY_TAX_RETURN_VALUE_IN_RETURN_REQUEST).getText();
	}

	public void checkReturnOnlyTaxChkBoxOnRefundPopUp(){
		driver.quickWaitForElementPresent(RETURN_ONLY_TAX_CHKBOX_REFUND_POPUP);
		driver.click(RETURN_ONLY_TAX_CHKBOX_REFUND_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public int getQuantityOfAllProductInOrderTabPage(){
		driver.waitForElementPresent(TOTAL_ORDERS_ON_PLACED_ORDER_DETAILS);
		int returnCountValue=0;
		if(driver.isElementPresent(TOTAL_ORDERS_ON_PLACED_ORDER_DETAILS)){
			int count=driver.findElements(TOTAL_ORDERS_ON_PLACED_ORDER_DETAILS).size();
			for(int i=1;i<=count;i++){
				driver.waitForElementPresent(By.xpath(String.format(ProductCountOnAutoshipTemplateLoc,i)));
				String counts=driver.findElement(By.xpath(String.format(ProductCountOnAutoshipTemplateLoc,i))).getText();
				returnCountValue=returnCountValue+Integer.parseInt(counts);
				logger.info("Qty for product"+i+"is "+counts); 
			}
			logger.info("Quantity sum for all products is "+returnCountValue);
			return returnCountValue;
		}else{
			logger.info("No product found in order detail ");
			logger.info("Quantity sum for all products is "+returnCountValue);
			return 0;
		}
	}

	public boolean checkReturnCompleteOrderAndChkShippingChkBoxOnRefundPopUpAndReturnTrueElseFalse(){
		driver.waitForElementPresent(RETURN_COMPLETE_ORDER_CHKBOX_REFUND_POPUP);
		if(driver.isElementPresent(By.xpath("//span[contains(text(),'This Order was placed over 120 days ago')]")) || driver.isElementPresent(By.xpath("//div[@class='rmanumber']/a[contains(text(),'RMA')]"))==false){
			driver.click(RETURN_COMPLETE_ORDER_CHKBOX_REFUND_POPUP);
			logger.info("Return Complete Order checkbox clicked");
			driver.waitForCSCockpitLoadingImageToDisappear();
			return true;
		}
		else{
			driver.click(RETURN_SHIPPING_CHKBOX_REFUND_POPUP);
			logger.info("Return Shipping checkbox clicked");
			driver.waitForCSCockpitLoadingImageToDisappear();
			driver.click(RETURN_HANDLING_CHKBOX_REFUND_POPUP);
			logger.info("Return Handling checkbox clicked");
			driver.waitForCSCockpitLoadingImageToDisappear();
		}
		return false;
	}

	public String getSKUValueOfExistingProduct(){
		driver.waitForElementPresent(GET_SKU_VALUE_ORDER_DETAIL);
		String skuValue=driver.findElement(GET_SKU_VALUE_ORDER_DETAIL).getText().split("\\-")[0];
		logger.info("Sku Value Of Product is "+skuValue);
		return skuValue;
	}


	public boolean isChangeSponserLinkPresent(){
		driver.waitForElementPresent(CHANGE_SPONSER_LINK);
		return driver.isElementPresent(CHANGE_SPONSER_LINK);
	}

	public boolean verifyClickingChangeLinkChangeConsultantReceivingCommissionsPopUPPresentInOrderTab(){
		driver.waitForElementPresent(CHANGE_CONSULTANT_RECEIVING_COMMISSIONS_POPUP);
		return driver.isElementPresent(CHANGE_CONSULTANT_RECEIVING_COMMISSIONS_POPUP);
	}

	public void enterAccountNumberInConsultantNameOrCIDOfChangeConsultantReceivingCommissionsPopUPInOrderTab(String enterConsultantNameOrCID){
		driver.waitForElementPresent(TEXTFIELD_OF_CONSULTANT_NAME_OR_CID_IN_CONSULTANT_RECEIVING_COMMISSIONS_POPUP);
		driver.type(TEXTFIELD_OF_CONSULTANT_NAME_OR_CID_IN_CONSULTANT_RECEIVING_COMMISSIONS_POPUP, enterConsultantNameOrCID);
	}

	public void clickSearchButtonInChangeConsultantReceivingCommissionsPopUPInOrderTab(){
		driver.waitForElementPresent(SEARCH_BTN_ANOTHER_LOCATOR);
		driver.click(SEARCH_BTN_ANOTHER_LOCATOR);
	}

	public String getCIDFirstAndLastNameFromChangeConsultantReceivingCommissionsPopUPInOrderTab(){
		driver.waitForElementPresent(FIRST_NAME_CHANGE_CONSULTANT_RECEIVING_COMMISSIONS_POPUP);
		String firstName = driver.findElement(FIRST_NAME_CHANGE_CONSULTANT_RECEIVING_COMMISSIONS_POPUP).getText();
		String lastName = driver.findElement(LAST_NAME_CHANGE_CONSULTANT_RECEIVING_COMMISSIONS_POPUP).getText();
		return firstName+" "+lastName;
	}

	public void clickSelectButtonInChangeConsultantReceivingCommissionsPopUPInOrderTab(){
		driver.waitForElementPresent(SELECT_BTN_IN_CHANGE_CONSULTANT_RECEIVING_COMMISSIONS_POPUP);
		driver.click(SELECT_BTN_IN_CHANGE_CONSULTANT_RECEIVING_COMMISSIONS_POPUP);
	}

	public String getRefundTaxFromRefundConfirmationPopUp(){
		return driver.findElement(REFUND_TAX_LBL).getText();
	}

	public boolean verifyReturnTaxOnlyCheckboxIsDisabled(String rowNum){
		driver.waitForElementPresent(By.xpath(String.format(disableReturnOnlyTaxCheckboxInPopup, rowNum)));
		return driver.isElementPresent(By.xpath(String.format(disableReturnOnlyTaxCheckboxInPopup, rowNum)));
	}

	public String getOrderTypeOnOrderTab() {
		driver.waitForElementPresent(ORDER_TYPE_LOC);
		String orderTypeOnUI = driver.findElement(ORDER_TYPE_LOC).getText();
		logger.info("Order Type on UI is: "+orderTypeOnUI);
		return orderTypeOnUI;
	}

	public void enterOrderNumber(String orderNumber) {
		driver.waitForElementPresent(ORDER_NUMBER_TXT_BOX_LOC);
		driver.type(ORDER_NUMBER_TXT_BOX_LOC, orderNumber);
		logger.info("order number is entered as :"+orderNumber);
		driver.click(SEARCH_BTN_LOC);
		logger.info("search btn is clicked");

	}

	public String getOriginationNameFromOrderInfo() {
		driver.waitForElementPresent(ORIGINATION_NAME_LOC);
		return driver.findElement(ORIGINATION_NAME_LOC).getText();
	}

}