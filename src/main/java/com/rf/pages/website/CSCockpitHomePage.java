package com.rf.pages.website;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.pages.RFBasePage;

public class CSCockpitHomePage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitHomePage.class.getName());

	private static String customerTypeDDLoc = "//span[contains(text(),'Customer Type')]/select/option[text()='%s']";
	private static String countryDDLoc = "//span[contains(text(),'Country')]/select/option[text()='%s']";
	private static String accountStatusDDLoc = "//span[contains(text(),'Account Status')]/select/option[text()='%s']";
	private static String orderTypeLoc = "//div[contains(text(),'Order Number')]/following::a[text()='%s']/following::span[2]";
	private static String customerEmailIdInSearchResultsLoc = "//div[@class='csListboxContainer']/descendant::table[2]/tbody[2]/tr[%s]/td[4]//span";
	private static String customerCIDInSearchResultsLoc = "//div[@class='csListboxContainer']/descendant::table[2]/tbody[2]/tr[%s]/td[1]//a";
	private static String sortByDropDownLoc= "//div[@class='csResultsSortList']/select/option[text()='%s']";
	private static String skuValueOfProductFromSearchResultLoc = "//div[@class='csListboxContainer']/div[2]/div[@class='z-listbox-body']//tbody[2]/tr[%s]/td[2]/div";
	private static String addedOrderNoteLoc = "//span[contains(text(),'%s')]";
	private static String addedOrderNoteEditBtnLoc = "//span[contains(text(),'%s')]/following::td[(contains(text(),'Edit'))]";
	private static String orderNumberFromOrdersResultLoc = "//div[@class='csSearchContainer']//div[@class='csListboxContainer']//div[@class='z-listbox-body']//tbody[2]/tr[%s]//a";
	private static String refundDropDownOptionLoc =  "//td[@class='z-combo-item-text' and contains(text(),'%s')]";

	private static final By SEARCH_BTN = By.xpath("//td[text()='SEARCH']"); 
	private static final By SEARCH_BTN_ANOTHER_LOCATOR = By.xpath("//td[text()='Search']");
	private static final By PLACE_ORDER_BUTTON = By.xpath("//td[contains(text(),'PLACE AN ORDER')]");
	private static final By CATALOG_DD = By.xpath("//span[contains(text(),'Select Catalog')]/following::select[1]");
	private static final By CATALOG_DD_OPTION = By.xpath("//span[contains(text(),'Select Catalog')]/following::select[1]/option");
	private static final By SEARCH_SKU_VALUE_TXT_FIELD = By.xpath("//span[contains(text(),'Select Catalog')]/following::input[1]");
	private static final By SEARCH_BTN_SKU = By.xpath("//span[contains(text(),'Select Catalog')]/following::td[contains(text(),'Search')]");
	private static final By ADD_TO_CART_BTN = By.xpath("//td[contains(text(),'Add To Cart')]");
	private static final By CHECKOUT_BTN = By.xpath("//td[contains(text(),'Checkout')]");
	private static final By CREDIT_CARD = By.xpath("//div[contains(text(),'************')]");
	private static final By DELIVERY_MODE = By.xpath("//span[contains(text(),'Delivery Mode')]/following::option[@selected='selected']");
	private static final By COMMISSION_DATE = By.xpath("//span[contains(text(),'Commission Date')]/following::input[@autocomplete ='off']");
	private static final By PLACE_ORDER_BUTTON_CHECKOUT_TAB = By.xpath("//td[contains(text(),'Place Order')]");
	private static final By SELECT_PAYMENT_DETAILS_POPUP = By.xpath("//span[contains(text(),'Please select payment details')]");
	private static final By PAYMENT_DETAILS_POPUP_OK_BTN = By.xpath("//td[text()='OK']");
	private static final By CVV2_SEARCH_TXT_FIELD = By.xpath("//div[contains(text(),'Action')]/following::td[contains(text(),'Use this card')][1]/preceding::input[1]");
	private static final By USE_THIS_CARD_BTN = By.xpath("//td[contains(text(),'Use this card')]");
	private static final By ORDER_NUMBER = By.xpath("//span[contains(text(),'Order #')]/following::span[1]");
	private static final By CUSTOMER_TAB = By.xpath("//span[text()='Customer']");
	private static final By ORDER_NOTES_TXT_FIELD = By.xpath("//span[contains(text(),'Order Notes')]/following::textarea[1]");
	private static final By ADD_BTN = By.xpath("//td[text()='ADD']");
	private static final By TOTAL_CUSTOMERS_FROM_RESULT_FIRST_PAGE = By.xpath("//div[@class='csListboxContainer']/descendant::table[2]/tbody[2]/tr");
	private static final By TOTAL_PRODUCTS_WITH_SKU = By.xpath("//div[@class='csListboxContainer']/div[2]/div[@class='z-listbox-body']//tbody[2]/tr"); 
	private static final By PRODUCT_NOT_AVAILABLE_POPUP_OK_BTN = By.xpath("//td[@class='z-button-cm'][text()='OK']");
	private static final By TOTAL_ORDERS_ON_PLACED_ORDER_DETAILS = By.xpath("//div[@class='orderDetailOrderItemsWidget']//div[@class='z-listbox-body']/table/tbody[2]/tr");
	private static final By ORDERS_DETAIL_ITEMS_LBL = By.xpath("//span[contains(text(),'Order Detail Items')]");
	private static final By ORDER_SEARCH_TAB_BTN = By.xpath("//span[text()='Order Search']");
	private static final By TOTAL_ORDERS_FROM_ORDER_SEARCH_RESULT = By.xpath("//div[@class='csSearchContainer']//div[@class='csListboxContainer']//div[@class='z-listbox-body']//tbody[2]/tr");
	private static final By TRANSACTION_STATUS_LINK = By.xpath("//div[@class='orderPaymentTransactionsWidget']//div[@class='csListboxContainer']//div[@class='z-listbox-body']//tbody[2]/tr[1]/td[6]//a[contains(@class,'z-toolbar-button')]");
	private static final By ENTER_EMAIL_ID = By.xpath("//span[contains(text(),'Email Address')]/following::input[1]");
	private static final By TEST_ORDER_CHKBOX = By.xpath("//label[contains(text(),'Test Order')]/preceding::input[1]");
	private static final By ORDER_STATUS = By.xpath("//span[contains(text(),'Order Status')]/following::span[1]");
	private static final By DO_NOT_SHIP_CHKBOX = By.xpath("//label[contains(text(),'Do-not-ship')]/preceding::input[1]");
	private static final By ORDER_NUMBER_TXT_FIELD_ORDER_SEARCH_TAB = By.xpath("//span[text()='Order Number']/following::input[1]");
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

	protected RFWebsiteDriver driver;

	public CSCockpitHomePage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}	

	public void selectCustomerTypeFromDropDownInCustomerSearchTab(String customerType){
		driver.waitForElementPresent(By.xpath(String.format(customerTypeDDLoc, customerType)));
		driver.click(By.xpath(String.format(customerTypeDDLoc, customerType.toUpperCase())));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void selectCountryFromDropDownInCustomerSearchTab(String country){
		driver.waitForElementPresent(By.xpath(String.format(countryDDLoc, country)));
		driver.click(By.xpath(String.format(countryDDLoc, country)));
		driver.waitForCSCockpitLoadingImageToDisappear();
		logger.info("************************************************************************************************************");
		logger.info("COUNTRY SELECTED = "+country);
		logger.info("************************************************************************************************************");
	}

	public void selectAccountStatusFromDropDownInCustomerSearchTab(String accountStatus){
		driver.waitForElementPresent(By.xpath(String.format(accountStatusDDLoc, accountStatus)));
		driver.click(By.xpath(String.format(accountStatusDDLoc, accountStatus)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickSearchBtn(){
		driver.quickWaitForElementPresent(SEARCH_BTN);
		try{
			driver.click(SEARCH_BTN);
		}catch(Exception e){
			driver.click(SEARCH_BTN_ANOTHER_LOCATOR);	
		}
		logger.info("Search button clicked");
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getEmailIdOfTheCustomerInCustomerSearchTab(String customerSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(customerEmailIdInSearchResultsLoc, customerSequenceNumber)));
		String customerEmailId = driver.findElement(By.xpath(String.format(customerEmailIdInSearchResultsLoc, customerSequenceNumber))).getText();
		logger.info("Selected Cutomer email Id is = "+customerEmailId);
		return customerEmailId;
	}

	public int getRandomCustomerFromSearchResult(){
		int totalCustomersFromResultsSearchFirstPage =  driver.findElements(TOTAL_CUSTOMERS_FROM_RESULT_FIRST_PAGE).size();
		int randomCustomerFromSearchResult = CommonUtils.getRandomNum(1, totalCustomersFromResultsSearchFirstPage);
		logger.info("Random Customer sequence number is "+randomCustomerFromSearchResult);
		return randomCustomerFromSearchResult;		
	}

	public int getRandomProductWithSKUFromSearchResult(){
		int totalProductsWithSKUFromSearchResult = driver.findElements(TOTAL_PRODUCTS_WITH_SKU).size();
		int randomProductFromSearchResult = CommonUtils.getRandomNum(1, totalProductsWithSKUFromSearchResult);
		logger.info("Random Customer sequence number is "+randomProductFromSearchResult);
		return randomProductFromSearchResult;
	}

	public void clickCIDNumberInCustomerSearchTab(String customerSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(customerCIDInSearchResultsLoc, customerSequenceNumber)));
		driver.click(By.xpath(String.format(customerCIDInSearchResultsLoc, customerSequenceNumber)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickPlaceOrderButtonInCustomerTab(){
		driver.waitForElementPresent(PLACE_ORDER_BUTTON);
		driver.click(PLACE_ORDER_BUTTON);
	}

	public void selectValueFromSortByDDInCartTab(String valueFromDropDown){
		driver.waitForElementPresent(By.xpath(String.format(sortByDropDownLoc, valueFromDropDown)));
		driver.click(By.xpath(String.format(sortByDropDownLoc, valueFromDropDown)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void selectCatalogFromDropDownInCartTab(){
		driver.waitForElementPresent(CATALOG_DD);
		driver.click(CATALOG_DD);
		driver.waitForElementPresent(CATALOG_DD_OPTION);
		driver.click(CATALOG_DD_OPTION);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getCustomerSKUValueInCartTab(String productSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(skuValueOfProductFromSearchResultLoc, productSequenceNumber)));
		return driver.findElement(By.xpath(String.format(skuValueOfProductFromSearchResultLoc, productSequenceNumber))).getText();
	}

	public void searchSKUValueInCartTab(String SKUValue){
		driver.waitForElementPresent(SEARCH_SKU_VALUE_TXT_FIELD);
		driver.type(SEARCH_SKU_VALUE_TXT_FIELD, SKUValue);
		driver.click(SEARCH_BTN_SKU);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickAddToCartBtnInCartTab(){
		for(int i=1;i<=10;i++){
			driver.waitForElementPresent(ADD_TO_CART_BTN);
			driver.click(ADD_TO_CART_BTN);
			logger.info("Add to cart button clicked");
			driver.waitForCSCockpitLoadingImageToDisappear();
			try{
				driver.waitForElementPresent(PRODUCT_NOT_AVAILABLE_POPUP_OK_BTN);
				driver.click(PRODUCT_NOT_AVAILABLE_POPUP_OK_BTN);
				clearCatalogSearchFieldAndClickSearchBtn();
				searchSKUValueInCartTab(getCustomerSKUValueInCartTab(String.valueOf(getRandomProductWithSKUFromSearchResult())));
			}catch(Exception e){
				logger.info("Product is available");
				break;
			}	
		}

	}

	public void clearCatalogSearchFieldAndClickSearchBtn(){
		driver.clear(SEARCH_SKU_VALUE_TXT_FIELD);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.click(SEARCH_BTN_SKU);;
	}	

	public void clickCheckoutBtnInCartTab(){
		driver.waitForElementPresent(CHECKOUT_BTN);
		driver.click(CHECKOUT_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getCreditCardNumberInCheckoutTab(){
		driver.waitForElementPresent(CREDIT_CARD);
		return driver.findElement(CREDIT_CARD).getText();
	}

	public String getDeliverModeTypeInCheckoutTab(){
		driver.waitForElementPresent(DELIVERY_MODE);
		return driver.findElement(DELIVERY_MODE).getText();
	}

	public boolean isCommissionDatePopulatedInCheckoutTab(){
		boolean isCommissionDatePopulatedInCheckoutTab = !(driver.findElement(COMMISSION_DATE).getAttribute("value").isEmpty());
		logger.info("is Commission Date Populated In Checkout Tab = "+isCommissionDatePopulatedInCheckoutTab);
		return isCommissionDatePopulatedInCheckoutTab;
	}

	public void clickPlaceOrderButtonInCheckoutTab(){
		driver.waitForElementPresent(PLACE_ORDER_BUTTON_CHECKOUT_TAB);
		driver.click(PLACE_ORDER_BUTTON_CHECKOUT_TAB);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public int getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder(){
		driver.waitForElementPresent(ORDERS_DETAIL_ITEMS_LBL);
		return driver.findElements(TOTAL_ORDERS_ON_PLACED_ORDER_DETAILS).size();
	}

	public boolean verifySelectPaymentDetailsPopupInCheckoutTab(){
		driver.waitForElementPresent(SELECT_PAYMENT_DETAILS_POPUP);
		return driver.isElementPresent(SELECT_PAYMENT_DETAILS_POPUP);
	}

	public void clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab(){
		driver.waitForElementPresent(PAYMENT_DETAILS_POPUP_OK_BTN);
		driver.click(PAYMENT_DETAILS_POPUP_OK_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void enterCVVValueInCheckoutTab(String CVV){
		driver.waitForElementPresent(CVV2_SEARCH_TXT_FIELD);
		driver.type(CVV2_SEARCH_TXT_FIELD, CVV);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickUseThisCardBtnInCheckoutTab(){
		driver.waitForElementPresent(USE_THIS_CARD_BTN);
		driver.click(USE_THIS_CARD_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getOrderNumberInOrderTab(){
		driver.waitForElementPresent(ORDER_NUMBER);
		return driver.findElement(ORDER_NUMBER).getText();
	}

	public void clickCustomerTab(){
		driver.waitForElementPresent(CUSTOMER_TAB);
		driver.click(CUSTOMER_TAB);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getOrderTypeInCustomerTab(String orderNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderTypeLoc, orderNumber)));
		return driver.findElement(By.xpath(String.format(orderTypeLoc, orderNumber))).getText();
	}

	public void enterOrderNotesInCheckoutTab(String orderNote){
		driver.waitForElementPresent(ORDER_NOTES_TXT_FIELD);
		driver.type(ORDER_NOTES_TXT_FIELD, orderNote);
		driver.click(ADD_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getAddedNoteValueInCheckoutTab(String orderNote){
		driver.waitForElementPresent(By.xpath(String.format(addedOrderNoteLoc, orderNote)));
		return driver.findElement(By.xpath(String.format(addedOrderNoteLoc, orderNote))).getText();
	}

	public boolean verifyEditButtonIsPresentForOrderNoteInCheckoutTab(String orderNote){
		driver.waitForElementPresent(By.xpath(String.format(addedOrderNoteLoc, orderNote)));
		return driver.isElementPresent(By.xpath(String.format(addedOrderNoteEditBtnLoc, orderNote)));
	}

	public String getPSTDate(){
		Date startTime = new Date();
		TimeZone pstTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
		DateFormat formatter = DateFormat.getDateInstance();
		formatter.setTimeZone(pstTimeZone);
		String formattedDate = formatter.format(startTime);
		return formattedDate;
	}

	public String convertUIDateFormatToPSTFormat(String UIDate){
		String UIMonth=null;
		String[] splittedDate = UIDate.split("\\/");
		String date = splittedDate[1];
		String month = splittedDate[0];
		String year = splittedDate[2];  
		switch (Integer.parseInt(month)) {  
		case 1:
			UIMonth="Jan";
			break;
		case 2:
			UIMonth="Feb";
			break;
		case 3:
			UIMonth="Mar";
			break;
		case 4:
			UIMonth="Apr";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="Jun";
			break;
		case 7:
			UIMonth="Jul";
			break;
		case 8:
			UIMonth="Aug";
			break;
		case 9:
			UIMonth="Sep";
			break;
		case 10:
			UIMonth="Oct";
			break;
		case 11:
			UIMonth="Nov";
			break;
		case 12:
			UIMonth="Dec";
			break;  
		}
		return date+" "+UIMonth+", "+year;
	}

	public void clickOrderSearchTab(){
		driver.waitForElementPresent(ORDER_SEARCH_TAB_BTN);
		driver.click(ORDER_SEARCH_TAB_BTN);
		logger.info("Order Search tab clicked");
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void enterOrderNumberInOrderSearchTab(String orderNumber){
		driver.waitForElementPresent(ORDER_NUMBER_TXT_FIELD_ORDER_SEARCH_TAB);
		driver.type(ORDER_NUMBER_TXT_FIELD_ORDER_SEARCH_TAB, orderNumber);
		logger.info("Order number entered for search is "+orderNumber);
	}

	public int getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab(){
		int totalOrdersFromSearchResultFirstPage = driver.findElements(TOTAL_ORDERS_FROM_ORDER_SEARCH_RESULT).size();
		int randomOrderFromSearchResult = CommonUtils.getRandomNum(1, totalOrdersFromSearchResultFirstPage);
		logger.info("Random Order sequence number is "+randomOrderFromSearchResult);
		return randomOrderFromSearchResult;
	}

	public void clickOrderNumberInOrderSearchResultsInOrderSearchTab(String orderSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderNumberFromOrdersResultLoc,orderSequenceNumber)));
		driver.click(By.xpath(String.format(orderNumberFromOrdersResultLoc,orderSequenceNumber)));
		driver.waitForCSCockpitLoadingImageToDisappear();

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

	public void enterEmailIdInSearchFieldInCustomerSearchTab(String emailId){
		driver.waitForElementPresent(ENTER_EMAIL_ID);
		driver.type(ENTER_EMAIL_ID, emailId);
	}

	public void clickTestOrderCheckBoxInCheckoutTab(){
		driver.waitForElementPresent(TEST_ORDER_CHKBOX);
		driver.click(TEST_ORDER_CHKBOX);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyTestOrderCheckBoxIsSelectedInOrderTab(){
		driver.isElementPresent(TEST_ORDER_CHKBOX);
		return driver.findElement(TEST_ORDER_CHKBOX).isSelected();
	}

	public String getOrderStatusAfterPlaceOrderInOrderTab(){
		driver.waitForElementPresent(ORDER_STATUS);
		return driver.findElement(ORDER_STATUS).getText();
	}

	public void clickDoNotShipCheckBoxInCheckoutTab(){
		driver.waitForElementPresent(DO_NOT_SHIP_CHKBOX);
		driver.click(DO_NOT_SHIP_CHKBOX);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyDoNotShipCheckBoxIsSelectedInOrderTab(){
		driver.isElementPresent(DO_NOT_SHIP_CHKBOX);
		return driver.findElement(DO_NOT_SHIP_CHKBOX).isSelected();
	}

	public int clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(String orderNumber){
		driver.waitForElementPresent(By.linkText(orderNumber));
		driver.click(By.linkText(orderNumber));
		logger.info(orderNumber+" link clicked");
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.waitForElementPresent(ORDERS_DETAIL_ITEMS_LBL);
		return driver.findElements(TOTAL_ORDERS_ON_PLACED_ORDER_DETAILS).size();
	}

	public void clickRefundOrderBtnOnOrderTab(){
		driver.waitForElementPresent(REFUND_ORDER_BTN_ORDER_TAB);
		driver.click(REFUND_ORDER_BTN_ORDER_TAB);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void checkReturnCompleteOrderChkBoxOnRefundPopUp(){
		driver.waitForElementPresent(RETURN_COMPLETE_ORDER_CHKBOX_REFUND_POPUP);
		if(driver.isElementPresent(By.xpath("//span[contains(text(),'This Order was placed over 120 days ago')]"))==false){
			driver.click(RETURN_COMPLETE_ORDER_CHKBOX_REFUND_POPUP);				
		}
		else{
			driver.click(RETURN_SHIPPING_CHKBOX_REFUND_POPUP);
			driver.waitForCSCockpitLoadingImageToDisappear();
			driver.click(RETURN_HANDLING_CHKBOX_REFUND_POPUP);
		}
		driver.waitForCSCockpitLoadingImageToDisappear();
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
	
	public void refreshPage(){
		driver.navigate().refresh();
		driver.waitForPageLoad();
		driver.waitForCSCockpitLoadingImageToDisappear();
	}
}

