package com.rf.pages.website.nscore;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore4HomePage extends NSCore4RFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(NSCore4HomePage.class.getName());
	int allBillingProfilesSize = 0;

	public NSCore4HomePage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static String productOnOrderTableOnOrderPage = "//table[@id='products']//td[contains(text(),'%s')]";
	private static String productOnOrderTableOnOrderDetailPage = "//table[@class='DataGrid']//td[contains(text(),'%s')]";
	private static String rowNumber = "//table[@id='accounts']/tbody/tr[%s]/td/a";
	private static String proxyLinksLoc = "//div[@class='DistributorProxies']//li/a[text()='%s']";
	private static String myAccountLinkAfterLoginLink = "//div[@class='topContents']//span[text()='%s']";
	private static String waitingForApprovalLink = "//ul[@id='stories']/li/a[contains(text(),'%s')]";
	private static String storyNameOnEditOptionPage = "//p[text()='%s']";
	private static String selectCategoryOnAddNotePopup = "//select[@id='newNoteCategory']/option[text()='%s']";
	private static String selectTypeOnAddNotePopup = "//select[@id='newNoteType']/option[text()='%s']";
	private static String newlyCreatedNoteLoc = "//div[@id='notesPanel']/div[text()='%s']";
	private static String postFollowUpLinkOfParent = "//div[@id='notesPanel']/div[text()='%s']/span/a[text()='Post Follow-up']";
	private static String newlyCreatedChildNoteLoc = "//div[@id='notesPanel']/div[text()='%s']/div[@class='ChildNotes']/div[text()='%s']";
	private static String postFollowUpChildLink = "//div[@id='notesPanel']/div[text()='%s']/div[@class='ChildNotes']/div[text()='%s']/span/a[text()='Post Follow-up']";
	private static String collapseLinkNextToParentNote = "//div[@id='notesPanel']/div[text()='%s']/span/a[text()='Collapse']";
	private static String expandLinkNextToParentNote = "//div[@id='notesPanel']/div[text()='%s']/span/a[text()='Expand']";
	private static String childNoteDetailsOnUI = "//div[@id='notesPanel']/div[text()='%s']/div[@class='ChildNotes' and @style='display: block;']";
	private static String completedDateOfOrder = "//table[@id='orders']/tbody/tr[%s]/td[4]";
	private static String shippedDateOfOrder = "//table[@id='orders']/tbody/tr[%s]/td[5]";
	private static String orderNumber = "//table[@id='orders']/tbody/tr[%s]/td[1]/a";
	private static String genderDDValue ="//select[@id='gender']/option[contains(text(),'%s')]";
	private static String newlyCeatedShippingProfile = "//div[@id='ContentWrap']//table//a[contains(text(),'%s')]";
	private static String newlyCeatedShippingProfileSetDefault = "//div[@id='ContentWrap']//table//a[contains(text(),'%s')]/ancestor::div[1]//a[contains(text(),'Set As Default Address')]";
	private static String newlyCeatedShippingProfileIsDefault = "//div[@id='ContentWrap']//table//a[contains(text(),'%s')]/ancestor::div[1]//span[contains(text(),'default')]";
	private static String deleteAddressnewlyCeatedShippingProfile = "//div[@id='ContentWrap']//table//a[contains(text(),'%s')]/preceding::a[contains(text(),'Delete Address')][1]";
	private static String overViewSublinkLoc = "//div[@class='Overview']//a[contains(text(),'%s')]";
	private static String orderIdLinkLoc = "//table[@id='orders']//a[text()='%s']";
	private static String setAsDefaultForNewlyCreatedBillingProfile = "//div[@id='paymentMethods']/descendant::div[contains(@class,'Profile')][%s]//a[contains(text(),'Set As Default Payment Method')]";
	private static String isDefaultPresentForNewlyCreatedBillingProfile = "//div[@id='paymentMethods']/descendant::div[contains(@class,'Profile')][%s]//span[contains(text(),'default')]";
	private static String deleteNewlyCreatedBillingProfile = "//div[@id='paymentMethods']/descendant::div[contains(@class,'Profile')][%s]//a[contains(text(),'Delete Payment Method')]";


	private static final By TOTAL_NO_OF_COLUMNS = By.xpath("//tr[@class='GridColHead']//a");
	private static final By EDIT_MY_STORY_LINK = By.xpath("//a[@class='EditButton' and contains(text(),'Edit My Story')]");
	private static final By I_WANT_TO_WRITE_OWN_STORY = By.xpath("//a[@id='newStory']/span");
	private static final By STORY_TITLE_LOC = By.id("myStoryName");
	private static final By OUTER_FRAME_ID = By.id("myStoryEditor___Frame");
	private static final By INNER_FRAME_ID = By.xpath("//td[@id='xEditingArea']/iframe");
	private static final By STORY_TEXT_LOC = By.xpath("//html/body");
	private static final By SAVE_STORY_BUTTON = By.id("save");
	private static final By LOGOUT_LINK = By.xpath("//a[contains(text(),'Logout')]") ;
	private static final By ACCOUNT_SEARCH_TXTFIELD = By.id("txtSearch");
	private static final By ACCOUNT_SEARCH_RESULTS = By.xpath("//div[contains(@class,'resultItem')][1]//p");
	private static final By GO_SEARCH_BTN = By.xpath("//a[@id='btnGo']/img[@alt='Go']");
	private static final By CONSULTANT_REPLENISHMENT_EDIT = By.xpath("//li[contains(text(),'Consultant Replenishment')]/descendant::a[text()='Edit']");
	private static final By PULSE_MONTHLY_SUBSCRIPTION_EDIT = By.xpath("//li[contains(text(),'Pulse Monthly Subscription')]/descendant::a[text()='Edit']");
	private static final By PULSE_PRODUCT_QUANTITY_TXT_FIELD = By.xpath("//input[@class='quantity']");
	private static final By SKU_SEARCH_FIELD = By.id("txtQuickAddSearch");
	private static final By SKU_SEARCH_FIRST_RESULT = By.xpath("//div[contains(@class,'jsonSuggestResults')][1]//p[1]");
	private static final By CUSTOMER_LABEL_ORDER_DETAIL_PAGE = By.xpath("//div[@class='CustomerLabel']/a");
	private static final By AUTOSHIP_PRODUCT_QUANTITY_TXT_FIELD  = By.id("txtQuickAddQuantity");
	private static final By AUTOSHIP_PRODUCT_QUANTITY_ADD_BTN  = By.id("btnQuickAdd");
	private static final By UPDATE_PULSE_CART_BTN  = By.id("btnUpdateCart");
	private static final By SAVE_TEMPLATE_BTN  = By.id("btnSaveAutoship");
	private static final By QUANTITY_OF_PULSE_PRODUCT_ON_ORDER_DETAIL_PAGE  = By.xpath("//tr[@class='GridRow']/td[4]");
	private static final By MOBILE_TAB_LOC  = By.xpath("//ul[@id='GlobalNav']//span[text()='Mobile']");
	private static final By ORDER_ID_PENDING_LOC = By.xpath("//table[@id='orders']//tr/td[text()='Pending'][1]//preceding::td[3]/a");
	private static final By ORDER_ID_FROM_OVERVIEW_PAGE_LOC = By.xpath("//table[@id='orders']/tbody/tr[1]/td[1]/a");
	private static final By ORDERS_TAB_LOC = By.xpath("//ul[@id='GlobalNav']//span[text()='Orders']");
	private static final By ROWS_COUNT_OF_SEARCH_RESULT  = By.xpath("//table[@id='accounts']/tbody/tr");
	private static final By ADMIN_TAB_LOC  = By.xpath("//ul[@id='GlobalNav']//span[text()='Admin']");
	private static final By RECENT_ORDER_DROP_DOWN_LOC = By.id("orderStatusFilter");
	private static final By NO_ORDER_FOUND_MSG_LOC = By.xpath("//table[@id='orders']//td[contains(text(),'No orders found')]");
	private static final By FIRST_PENDING_ORDER_LOC = By.xpath("//table[@id='orders']/tbody/tr[1]/td/a");
	private static final By POLICIES_CHANGE_HISTORY_LINK_LOC = By.xpath("//a[contains(text(),'Policies Change History')]");
	private static final By VERSION_COLUMN_LOC = By.xpath("//th[text()='Version']");
	private static final By DATERELEASED_COLUMN_LOC = By.xpath("//th[text()='Date Released']");
	private static final By DATEACCEPTED_COLUMN_LOC = By.xpath("//th[text()='Date Accepted']");
	private static final By STATUS_HISTORY_LINK_LOC = By.xpath("//a[text()='Status History']");
	private static final By CHANGED_ON_COLUMN_LOC = By.xpath("//th[text()='Changed On']");
	private static final By STATUS_COLUMN_LOC = By.xpath("//th[text()='Status']");
	private static final By REASON_COLUMN_LOC = By.xpath("//th[text()='Reason']");
	private static final By POST_NEW_NODE_LINK  = By.xpath("//a[text()='Post New Note']");
	private static final By CATEGORY_DROPDOWN_ON_ADD_NOTE_POPUP  = By.id("newNoteCategory");
	private static final By TYPE_DROPDOWN_ON_ADD_NOTE_POPUP  = By.id("newNoteType");
	private static final By ENTER_NOTE_ON_ADD_NOTE_POPUP  = By.id("newNoteText");
	private static final By SAVE_BTN_ON_ADD_A_NOTE_POPUP  = By.id("btnSaveNote");
	private static final By PLACE_NEW_ORDER_LINK_LOC = By.xpath("//a[text()='Place New Order']");
	private static final By STATUS_LINK_LOC = By.xpath("//td[contains(text(),'Status:')]/following-sibling::td/a");
	private static final By CHANGE_STATUS_DD_LOC = By.xpath("//select[@id='sStatus']");
	private static final By SAVE_STATUS_BTN_LOC = By.xpath("//a[@id='btnSaveStatus']");
	private static final By VIEW_ORDER_CONSULTANT_REPLENISHMENT = By.xpath("//li[contains(text(),'Consultant Replenishment')]//a[text()='View Orders']");
	private static final By VIEW_ORDER_PULSE_MONTHLY_SUBSCRIPTION = By.xpath("//li[contains(text(),'Pulse Monthly Subscription')]//a[text()='View Orders']");
	private static final By START_DATE_OF_DATE_RANGE = By.id("txtStartDate");
	private static final By ORDER_SEARCH_RESULTS = By.xpath("//table[@id='orders']/tbody/tr");
	private static final By ORDER_NUMBER_FROM_ORDER_DETAILS = By.xpath("//div[@class='CustomerLabel']/a");
	private static final By FULL_ACCOUNT_RECORD_LINK = By.xpath("//span[contains(text(),'Full Account Record')]");
	private static final By APPLICATION_ON_FILE_CHKBOX = By.id("chkApplicationOnFile");
	private static final By CRP_START_DATE = By.id("crpStartDate");
	private static final By USERNAME_LOC = By.id("txtUsername");
	private static final By FIRST_NAME = By.id("txtFirstName");
	private static final By LAST_FOUR_DIGIT_OF_HOME_PHONE_NUMBER = By.id("txtHomePhoneLastFour");
	private static final By EMAIL_ADDRESS_LOC = By.id("txtEmail");
	private static final By TAX_EXEMPT_VALUE = By.xpath("//select[@id='isTaxExempt']/option[@selected='selected']");
	private static final By TAX_EXEMPT_DD = By.id("isTaxExempt");
	private static final By TAX_EXEMPT_DD_YES_VALUE = By.xpath("//select[@id='isTaxExempt']/option[contains(text(),'Yes')]");
	private static final By TAX_EXEMPT_DD_NO_VALUE = By.xpath("//select[@id='isTaxExempt']/option[contains(text(),'No')]");
	private static final By DOB_LOC = By.id("txtDOB");
	private static final By NAME_ON_SSN_CARD = By.id("txtLegalName");
	private static final By GENDER_DD = By.id("gender");
	private static final By GENDER_DD_SELECTED_VALUE = By.xpath("//select[@id='gender']/option[@selected='selected']");
	private static final By ATTENTION_NAME = By.id("txtAttention");
	private static final By ZIP_CODE = By.id("txtPostalCode");
	private static final By LAST_FOUR_DIGIT_OF_PHONE_NUMBER = By.id("txtLastFour");
	private static final By SAVE_ACCOUNT_BTN = By.id("btnSaveAccount");
	private static final By USE_AS_ENTERED_BTN = By.xpath("//span[contains(text(),'Use as entered')]/..[@aria-disabled='false']");
	private static final By ACCEPT_BTN = By.xpath("//span[contains(text(),'Accept')]");
	private static final By GET_UPDATION_MSG = By.xpath("//div[@id='messageCenter']/div[contains(@id,'message')]");
	private static final By USE_ADDRESS_AS_ENTERED = By.id("QAS_AcceptOriginal");
	private static final By BILLING_AND_SHIPPING_PROFILE_LINK_LOC = By.xpath("//span[text()='Billing & Shipping Profiles']");
	private static final By SHIPPING_PROFILE_ADD_LINK_LOC = By.xpath("//a[@id='btnAddShippingAddress']");
	private static final By ADD_SHIPPING_ADDRESS_PROFILE_NAME_LOC = By.xpath("//input[@id='profileName']");
	private static final By ADD_SHIPPING_ADDRESS_ATTENTION_LOC = By.xpath("//input[@id='attention']");
	private static final By ADD_SHIPPING_ADDRESS_LINE1_LOC = By.xpath("//input[@id='addressLine1']");
	private static final By ADD_SHIPPING_ADDRESS_ZIPCODE_LOC = By.xpath("//input[@id='zip']");
	private static final By STATE_DD_LOC  = By.id("state");
	private static final By STATE_DD_OPTION_LOC  = By.xpath("//select[@id='state']/option[2]");
	private static final By SAVE_ADDRESS_BTN_LOC = By.xpath("//a[@id='btnSaveAddress']");
	private static final By USE_ADDRESS_AS_ENTERED_BTN_LOC = By.xpath("//input[@id='QAS_AcceptOriginal']");
	private static final By BILLING_PROFILE_ADD_LINK_LOC = By.xpath("//a[@id='btnAddBillingAddress']");
	private static final By ADD_NEW_BILLING_ADDRESS_DROP_DOWN_LOC = By.id("existingAddress");
	private static final By ADD_PAYMENT_METHOD_FIRST_NAME_LOC = By.xpath("//input[@id='uxAttentionFirstName']");
	private static final By ADD_PAYMENT_METHOD_LAST_NAME_LOC = By.xpath("//input[@id='uxAttentionLastName']");
	private static final By ADD_PAYMENT_METHOD_NAME_ON_CARD_LOC = By.xpath("//input[@id='nameOnCard']");
	private static final By ADD_PAYMENT_METHOD_CREDIT_CARD_NO_LOC = By.xpath("//input[@id='accountNumber']");
	private static final By ADD_NEW_PAYMENT_EXPIRATION_YEAR_DROP_DOWN_LOC = By.id("expYear");
	private static final By SAVE_PAYMENT_METHOD_BTN_LOC = By.xpath("//a[@id='btnSavePaymentMethod']");
	private static final By USE_AS_ENTERED_BTN_LOC = By.xpath("//*[@id='QAS_AcceptOriginal']");
	private static final By ACCEPT_BTN_LOC = By.xpath("//button/span[text()='Accept']");
	private static final By NEWLY_CREATED_BILLING_PROFILE_LOC = By.xpath("//div[@id='ContentWrap']//table//a[contains(text(),'Main Billing')]");
	private static final By APPLY_PAYMENT_BTN  = By.id("btnApplyPayment");
	private static final By PLACED_ORDER_NUMBER  = By.xpath("//div[@class='Content']//a[contains(text(),'Order #')]");
	private static final By OPEN_BULK_ADD_LOC  = By.id("btnOpenBulkAdd");
	private static final By SKU_FIELD_SEARCH  = By.xpath("//*[@id='txtQuickAddSearch']");
	private static final By PRODUCT_QUANTITY_LOC  = By.xpath("//table[@id='bulkProductCatalog']//tr[1]//input[@class='quantity']");
	private static final By PRODUCT_SKU_VALUE  = By.xpath("//table[@id='bulkProductCatalog']/tbody//tr[1]/td[1]");
	private static final By ADD_TO_ORDER_BTN  = By.id("btnBulkAdd");
	private static final By CLOSE_LINK_LOC  = By.xpath("//div[@id='bulkAddModal']//a[text()='Close']");
	private static final By TOTAL_BILLING_PROFILES = By.xpath("//div[@id='paymentMethods']/div[contains(@class,'Profile')]");
	private static final By SHIPPING_PROFILES_LOC  = By.xpath("//div[@id='addresses']/div");
	private static final By PRODUCT_SKU_VALUE_CATALOG  = By.xpath("//table[@id='productBulkAddGrid']//tr[3]/td[2]");
	private static final By PRODUCT_SKU_CHK_BOX_CATALOG  = By.xpath("//table[@id='productBulkAddGrid']//tr[3]/td[2]");
	private static final By ADD_TO_CATALOG  = By.id("btnBulkAddProducts");
	private static final By CLOSE_LINK_CATALOG_LOC  = By.xpath("//div[@id='productBulkAdd']//a[text()='Close']");
	private static final By ADD_NEW_PAYMENT_METHOD_LINK  = By.xpath("//a[contains(text(),'Add New Payment Method')]");

	public boolean isLogoutLinkPresent(){
		driver.waitForElementPresent(LOGOUT_LINK);
		return driver.isElementPresent(LOGOUT_LINK);
	}

	public NSCore4LoginPage clickLogoutLink(){
		driver.quickWaitForElementPresent(LOGOUT_LINK);
		driver.click(LOGOUT_LINK);
		logger.info("Logout link clicked");
		driver.waitForPageLoad();	
		return new NSCore4LoginPage(driver);
	}

	public void enterAccountNumberInAccountSearchField(String accountinfo){
		driver.quickWaitForElementPresent(By.id("txtSearch"));
		driver.type(ACCOUNT_SEARCH_TXTFIELD, accountinfo);
		driver.pauseExecutionFor(2000);
		logger.info("account info entered in search field is "+accountinfo);
	}

	public void clickGoBtnOfSearch(String accountNumber){
		driver.click(GO_SEARCH_BTN);
		logger.info("Search Go button clicked");
		try {
			driver.quickWaitForElementPresent(By.linkText(accountNumber));
			driver.click(By.linkText(accountNumber));
			logger.info("Account Number clicked on Browse Account Page");
			driver.waitForPageLoad();
		} catch (Exception e) {

		}
	}

	public void clickConsultantReplenishmentEdit(){
		driver.waitForElementPresent(CONSULTANT_REPLENISHMENT_EDIT);
		driver.click(CONSULTANT_REPLENISHMENT_EDIT);
		logger.info("consultant Replenishment edit clicked");
		driver.waitForPageLoad();
	}

	public void clickPulseMonthlySubscriptionEdit(){
		driver.waitForElementPresent(PULSE_MONTHLY_SUBSCRIPTION_EDIT);
		driver.click(PULSE_MONTHLY_SUBSCRIPTION_EDIT);
		logger.info("pulse monthly subscription edit clicked");
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
	}

	public String updatePulseProductQuantityAndReturnValue(){
		int quantity = Integer.valueOf(driver.findElement(PULSE_PRODUCT_QUANTITY_TXT_FIELD).getAttribute("value"));;
		quantity = quantity +1;
		driver.findElement(PULSE_PRODUCT_QUANTITY_TXT_FIELD).clear();
		driver.findElement(PULSE_PRODUCT_QUANTITY_TXT_FIELD).sendKeys(String.valueOf(quantity));
		driver.click(UPDATE_PULSE_CART_BTN);
		logger.info("update cart button on pulse clicked");
		return String.valueOf(quantity);
	}

	public void enterSKUValue(String sku){
		driver.get(driver.getCurrentUrl());
		driver.waitForPageLoad();
		driver.quickWaitForElementPresent(SKU_SEARCH_FIELD);
		driver.type(SKU_SEARCH_FIELD, sku);
		logger.info("sku value entered is "+sku);
	}

	public void clickFirstSKUSearchResultOfAutoSuggestion(){
		driver.waitForElementPresent(SKU_SEARCH_FIRST_RESULT);
		driver.click(SKU_SEARCH_FIRST_RESULT);	
		logger.info("sku first result clicked");
	}

	public void enterProductQuantityAndAddToOrder(String quantity){
		driver.type(AUTOSHIP_PRODUCT_QUANTITY_TXT_FIELD, quantity);
		driver.click(AUTOSHIP_PRODUCT_QUANTITY_ADD_BTN);
		logger.info("autoship product quantity add button clicked");
	}

	public boolean isProductAddedToOrder(String SKU){
		return driver.isElementPresent(By.xpath(String.format(productOnOrderTableOnOrderPage, SKU)));
	}

	public void clickSaveAutoshipTemplate(){
		driver.click(SAVE_TEMPLATE_BTN);
		logger.info("save template button clicked");
		driver.pauseExecutionFor(2000);
		driver.waitForNSCore4ProcessImageToDisappear();
		driver.waitForPageLoad();
	}

	public boolean isAddedProductPresentInOrderDetailPage(String SKU){
		driver.quickWaitForElementPresent(By.xpath(String.format(productOnOrderTableOnOrderDetailPage, SKU)));
		return driver.isElementPresent(By.xpath(String.format(productOnOrderTableOnOrderDetailPage, SKU)));
	}

	public String getQuantityOfPulseProductFromOrderDetailPage(){
		driver.waitForElementPresent(QUANTITY_OF_PULSE_PRODUCT_ON_ORDER_DETAIL_PAGE);
		logger.info("pulse quantity on UI is "+driver.findElement(QUANTITY_OF_PULSE_PRODUCT_ON_ORDER_DETAIL_PAGE).getText().trim());
		return driver.findElement(QUANTITY_OF_PULSE_PRODUCT_ON_ORDER_DETAIL_PAGE).getText().trim();
	}

	public boolean isFirstAndLastNamePresentinSearchResults(String firstName,String lastName){
		driver.waitForElementPresent(ACCOUNT_SEARCH_RESULTS);
		return driver.findElement(By.xpath("//div[contains(@class,'resultItem')][1]//p")).getText().contains(firstName)
				&& driver.findElement(By.xpath("//div[contains(@class,'resultItem')][1]//p")).getText().contains(lastName);
	}

	public void clickCustomerlabelOnOrderDetailPage(){
		driver.click(CUSTOMER_LABEL_ORDER_DETAIL_PAGE);
		logger.info("customer label on order detail page clicked");
		driver.waitForPageLoad();
	}

	public NSCore4MobilePage clickMobileTab(){
		driver.quickWaitForElementPresent(MOBILE_TAB_LOC);
		driver.click(MOBILE_TAB_LOC);
		logger.info("Mobile Tab is clicked");
		driver.waitForPageLoad();
		return new NSCore4MobilePage(driver);
	}

	public NSCore4OrdersTabPage clickPendingOrderID() {
		driver.click(FIRST_PENDING_ORDER_LOC);
		return new NSCore4OrdersTabPage(driver);
	}

	public NSCore4OrdersTabPage clickOrdersTab() {
		driver.click(ORDERS_TAB_LOC);
		logger.info("orders tab is clicked");
		return new NSCore4OrdersTabPage(driver);
	}

	public String getOrderIDFromOverviewPage() {
		return driver.findElement(ORDER_ID_FROM_OVERVIEW_PAGE_LOC).getText();
	}

	public void clickGoBtnOfSearch(){
		driver.waitForElementPresent(GO_SEARCH_BTN);
		driver.click(GO_SEARCH_BTN);
		driver.waitForPageLoad();
		logger.info("Search Go button clicked");
	}

	public int getCountOfSearchResultRows(){
		driver.waitForElementPresent(ROWS_COUNT_OF_SEARCH_RESULT);
		int count= driver.findElements(ROWS_COUNT_OF_SEARCH_RESULT).size();
		logger.info("Count of search result rows are: "+count);
		return count;
	}
	public String clickAndReturnAccountnumber(int rowNum){
		driver.waitForElementPresent(By.xpath(String.format(rowNumber, rowNum)));
		String accountNumber=driver.findElement(By.xpath(String.format(rowNumber, rowNum))).getText();
		driver.click(By.xpath(String.format(rowNumber, rowNum)));
		logger.info("Account Number "+accountNumber+" clicked.");
		driver.waitForPageLoad();
		return accountNumber;
	}
	public void clickProxyLink(String linkName){
		driver.waitForElementPresent(By.xpath(String.format(proxyLinksLoc, linkName)));
		driver.click(By.xpath(String.format(proxyLinksLoc, linkName)));
		logger.info(linkName+" proxy link clicked");
		driver.waitForPageLoad();
	}

	public void switchToSecondWindow(){
		Set<String> allWindows = driver.getWindowHandles();
		String parentWindow = driver.getWindowHandle();
		for(String allWin : allWindows){
			if(!allWin.equalsIgnoreCase(parentWindow)){
				driver.switchTo().window(allWin);
				break;
			}

		}
		logger.info("Switched to second window");
		driver.waitForPageLoad();
	}

	public void switchToPreviousTab(){
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		driver.close();
		driver.switchTo().window(tabs.get(0));
		driver.pauseExecutionFor(1000);
	}

	public void clickHeaderLinkAfterLogin(String linkName) {
		driver.waitForElementPresent(By.xpath(String.format(myAccountLinkAfterLoginLink, linkName)));
		driver.click(By.xpath(String.format(myAccountLinkAfterLoginLink, linkName)));
		logger.info("my account link is clicked");
	}

	public void clickEditMyStoryLink(){
		driver.quickWaitForElementPresent(EDIT_MY_STORY_LINK);
		driver.click(EDIT_MY_STORY_LINK);
		logger.info("Edit my story link is clicked");
		driver.waitForPageLoad();
	}

	public void clickIWantToWriteMyOwnStory(){
		driver.waitForElementPresent(I_WANT_TO_WRITE_OWN_STORY);
		driver.click(I_WANT_TO_WRITE_OWN_STORY);
		logger.info("I want to write my own story is clicked");
		driver.waitForPageLoad();
	}

	public void typeSomeStoryAndclickSaveStory(String title,String Story) {
		driver.quickWaitForElementPresent(STORY_TITLE_LOC);
		driver.type(STORY_TITLE_LOC, title+"\t");
		driver.switchTo().frame(driver.findElement(OUTER_FRAME_ID));
		System.out.println("inside frame one comes");
		driver.switchTo().frame(driver.findElement(INNER_FRAME_ID));
		driver.pauseExecutionFor(2000); 
		driver.waitForElementPresent(STORY_TEXT_LOC);
		driver.type(STORY_TEXT_LOC, Story);
		driver.switchTo().defaultContent();
		driver.click(SAVE_STORY_BUTTON);
		logger.info("After entering story save button clicked");
		driver.waitForPageLoad();
	}		 

	public boolean verifyNewlyCreatedStoryIsUpdated(String storyTitle){
		driver.waitForElementPresent(By.xpath(String.format(storyNameOnEditOptionPage, storyTitle)));
		return driver.isElementPresent(By.xpath(String.format(storyNameOnEditOptionPage, storyTitle)));
	}

	public boolean verifyWaitingForApprovalLinkForNewStory(String storyTitle){
		driver.waitForElementPresent(By.xpath(String.format(waitingForApprovalLink, storyTitle)));
		return driver.isElementPresent(By.xpath(String.format(waitingForApprovalLink, storyTitle)));
	}

	public String getStoryDeniedText(String storyTitle){
		driver.waitForElementPresent(By.xpath(String.format(waitingForApprovalLink, storyTitle)));
		String denyTxt=driver.findElement(By.xpath(String.format(waitingForApprovalLink, storyTitle))).getText();
		logger.info("Story deny text from UI is: "+denyTxt);
		return denyTxt;
	}

	public NSCore4AdminPage clickAdminTab(){
		driver.quickWaitForElementPresent(ADMIN_TAB_LOC);
		driver.click(ADMIN_TAB_LOC);
		logger.info("Admin Tab is clicked");
		driver.waitForPageLoad();
		return new NSCore4AdminPage(driver);
	}

	public void selectOrderStatusByDropDown(String value) {
		Select select = new Select(driver.findElement(RECENT_ORDER_DROP_DOWN_LOC));
		select.selectByVisibleText(value);
		driver.pauseExecutionFor(2000);

	}

	public boolean isNoOrderFoundMessagePresent() {
		return driver.isElementPresent(NO_ORDER_FOUND_MSG_LOC);  
	}

	public void clickPoliciesChangeHistoryLink(){
		driver.quickWaitForElementPresent(POLICIES_CHANGE_HISTORY_LINK_LOC);
		driver.click(POLICIES_CHANGE_HISTORY_LINK_LOC);
		logger.info("Policies Change History link clicked");
		driver.waitForPageLoad(); 
	}

	public boolean validatePoliciesChangeHistoryPageDisplayedWithRespectiveColumns(){
		return driver.getCurrentUrl().contains("PoliciesChangeHistory") &&
				driver.isElementPresent(VERSION_COLUMN_LOC) &&
				driver.isElementPresent(DATERELEASED_COLUMN_LOC) &&
				driver.isElementPresent(DATEACCEPTED_COLUMN_LOC);
	}

	public void clickStatusHistoryLink(){
		driver.quickWaitForElementPresent(STATUS_HISTORY_LINK_LOC);
		driver.click(STATUS_HISTORY_LINK_LOC);
		logger.info("Status History link clicked");
		driver.waitForPageLoad(); 
	}

	public boolean validateStatusHistoryPageDisplayedWithRespectiveColumns(){
		return driver.getCurrentUrl().contains("StatusHistory") &&
				driver.isElementPresent(CHANGED_ON_COLUMN_LOC) &&
				driver.isElementPresent(STATUS_COLUMN_LOC) &&
				driver.isElementPresent(REASON_COLUMN_LOC);
	}

	public void clickPostNewNodeLinkInOverviewTab(){
		driver.waitForElementPresent(POST_NEW_NODE_LINK);
		driver.click(POST_NEW_NODE_LINK);
		logger.info("Post new node link clicked");
	}

	public void selectCategoryOnAddANotePopup(String category){
		driver.waitForElementPresent(CATEGORY_DROPDOWN_ON_ADD_NOTE_POPUP);
		driver.click(CATEGORY_DROPDOWN_ON_ADD_NOTE_POPUP);
		logger.info("Category Dropdown clicked on add note popup");
		driver.waitForElementPresent(By.xpath(String.format(selectCategoryOnAddNotePopup, category)));
		driver.click(By.xpath(String.format(selectCategoryOnAddNotePopup, category)));
		logger.info("Category"+category+" is selected on add new note popup");
	}

	public void selectTypeOnAddANotePopup(String type){
		driver.waitForElementPresent(TYPE_DROPDOWN_ON_ADD_NOTE_POPUP);
		driver.click(TYPE_DROPDOWN_ON_ADD_NOTE_POPUP);
		logger.info("Type Dropdown clicked on add note popup");
		driver.waitForElementPresent(By.xpath(String.format(selectTypeOnAddNotePopup, type)));
		driver.click(By.xpath(String.format(selectTypeOnAddNotePopup, type)));
		logger.info("Type"+type+" is selected on add new note popup");
	}

	public void enterNoteOnAddANotePopup(String note){
		driver.waitForElementPresent(ENTER_NOTE_ON_ADD_NOTE_POPUP);
		driver.type(ENTER_NOTE_ON_ADD_NOTE_POPUP, note);
		logger.info("Note"+note+" is entered on enter note txt field");
	}

	public void selectAndEnterAddANoteDetailsInPopup(String category,String type,String note){
		selectCategoryOnAddANotePopup(category);
		selectTypeOnAddANotePopup(type);
		enterNoteOnAddANotePopup(note);
	}

	public void clickSaveBtnOnAddANotePopup(){
		driver.waitForElementPresent(SAVE_BTN_ON_ADD_A_NOTE_POPUP);
		driver.click(SAVE_BTN_ON_ADD_A_NOTE_POPUP);
		logger.info("Save btn clicked on add a note popup");
	}

	public boolean isNewlyCreatedNotePresent(String note){
		driver.waitForElementPresent(By.xpath(String.format(newlyCreatedNoteLoc, note)));
		return driver.isElementPresent(By.xpath(String.format(newlyCreatedNoteLoc, note)));
	}

	public void clickPostFollowUpLinkForParentNote(String note){
		driver.quickWaitForElementPresent(By.xpath(String.format(postFollowUpLinkOfParent, note)));
		driver.click(By.xpath(String.format(postFollowUpLinkOfParent, note)));
		logger.info("Post follow up link is clicked for note"+note);
	}

	public boolean isNewlyCreatedChildNotePresent(String parentNote,String childNote){
		driver.waitForElementPresent(By.xpath(String.format(newlyCreatedChildNoteLoc, parentNote,childNote)));
		return driver.isElementPresent(By.xpath(String.format(newlyCreatedChildNoteLoc,parentNote, childNote)));
	}

	public boolean isPostFollowUpLinkPresentForChildNote(String parentNote,String childNote){
		driver.waitForElementPresent(By.xpath(String.format(postFollowUpChildLink, parentNote,childNote)));
		return driver.isElementPresent(By.xpath(String.format(postFollowUpChildLink,parentNote, childNote)));
	}

	public void clickCollapseLinkNearToParentNote(String parentNote){
		driver.quickWaitForElementPresent(By.xpath(String.format(collapseLinkNextToParentNote, parentNote)));
		driver.click(By.xpath(String.format(collapseLinkNextToParentNote, parentNote)));
		logger.info("Collapse link next to Parent note"+parentNote+" is clicked");

	}

	public void clickExpandLinkNearToParentNote(String parentNote){
		driver.quickWaitForElementPresent(By.xpath(String.format(expandLinkNextToParentNote, parentNote)));
		driver.click(By.xpath(String.format(expandLinkNextToParentNote, parentNote)));
		logger.info("Expand link next to Parent note"+parentNote+" is clicked");
	}

	public boolean isChildNoteDetailsAppearsOnUI(String parentNote){
		return driver.isElementPresent(By.xpath(String.format(childNoteDetailsOnUI, parentNote)));
	}

	public void clickPlaceNewOrderLink(){
		driver.quickWaitForElementPresent(PLACE_NEW_ORDER_LINK_LOC);
		driver.click(PLACE_NEW_ORDER_LINK_LOC);
		logger.info("'Place-New-Order' link clicked");
		driver.waitForPageLoad(); 
	}

	public String getStatus(){
		driver.quickWaitForElementPresent(STATUS_LINK_LOC);
		return driver.findElement(STATUS_LINK_LOC).getText();
	}

	public void clickStatusLink(){
		driver.quickWaitForElementPresent(STATUS_LINK_LOC);
		driver.click(STATUS_LINK_LOC);
		logger.info("Status link clicked");
		driver.waitForPageLoad();
	}

	public void changeStatusDD(int index){
		Select sel =new Select(driver.findElement(CHANGE_STATUS_DD_LOC));
		sel.selectByIndex(index);
	}

	public void clickSaveStatusBtn(){
		driver.quickWaitForElementPresent(SAVE_STATUS_BTN_LOC);
		driver.click(SAVE_STATUS_BTN_LOC);
		logger.info("Save Status Button clicked");
		driver.waitForPageLoad();
	}

	public void refreshPage(){
		driver.navigate().refresh();
		driver.pauseExecutionFor(2000);
	}

	public void clickViewOrderLinkUnderConsultantReplenishment(){
		driver.waitForElementPresent(VIEW_ORDER_CONSULTANT_REPLENISHMENT);
		driver.click(VIEW_ORDER_CONSULTANT_REPLENISHMENT);
		logger.info("View order link clicked under consultant replenishment");
		driver.waitForPageLoad();
	}

	public void clickViewOrderLinkUnderPulseMonthlySubscription(){
		driver.waitForElementPresent(VIEW_ORDER_PULSE_MONTHLY_SUBSCRIPTION);
		driver.click(VIEW_ORDER_PULSE_MONTHLY_SUBSCRIPTION);
		logger.info("View order link clicked under Pulse Monthly Subscription"); 
		driver.waitForPageLoad();
	}

	public void clickCalenderStartDateForFilter(){
		driver.waitForElementPresent(START_DATE_OF_DATE_RANGE);
		driver.click(START_DATE_OF_DATE_RANGE);
		logger.info("Calender event start date clicked.");
	}

	public int getCountOfSearchResults(){
		driver.waitForElementPresent(ORDER_SEARCH_RESULTS);
		int count = driver.findElements(ORDER_SEARCH_RESULTS).size();
		logger.info("Total search results: "+count);
		return count;
	}

	public String getCompletedDateOfOrder(int randomOrderRowNumber){
		driver.waitForElementPresent(By.xpath(String.format(completedDateOfOrder, randomOrderRowNumber)));
		String completedDate = driver.findElement(By.xpath(String.format(completedDateOfOrder, randomOrderRowNumber))).getText();
		logger.info("Completed date is: "+completedDate);
		return completedDate;
	}

	public String getShippedDateOfOrder(int randomOrderRowNumber){
		driver.waitForElementPresent(By.xpath(String.format(shippedDateOfOrder, randomOrderRowNumber)));
		String shippedDate = driver.findElement(By.xpath(String.format(shippedDateOfOrder, randomOrderRowNumber))).getText();
		logger.info("Shipped date is: "+shippedDate);
		return shippedDate;
	}

	public String clickAndReturnRandomOrderNumber(int randomOrderRowNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderNumber, randomOrderRowNumber)));
		String orderNo = driver.findElement(By.xpath(String.format(orderNumber, randomOrderRowNumber))).getText();
		driver.click(By.xpath(String.format(orderNumber, randomOrderRowNumber)));
		logger.info("Order Number: "+orderNo+" Clicked");
		driver.waitForPageLoad();
		return orderNo;
	}

	public String getOrderNumberFromOrderDetails(){
		driver.waitForElementPresent(ORDER_NUMBER_FROM_ORDER_DETAILS);
		String orderNumber = driver.findElement(ORDER_NUMBER_FROM_ORDER_DETAILS).getText();
		logger.info("Order Number from order details page is: "+orderNumber);
		return orderNumber;
	}

	public String[] getAllCompleteDate(int totalSearchResults){
		String[] completeDate = new String[totalSearchResults+1];
		for(int i=1; i<=totalSearchResults; i++){
			completeDate[i] = driver.findElement(By.xpath(String.format(completedDateOfOrder, i))).getText();
		}
		return completeDate;
	}

	public boolean isAllCompleteDateContainCurrentYear(String[] totalSearchResults){
		boolean isCurrentYearPresent = false;
		for(int i=1; i<totalSearchResults.length; i++){
			if(totalSearchResults[i].contains("2016") || totalSearchResults[i].contains("N/A")){
				isCurrentYearPresent = true;
			}else{
				isCurrentYearPresent = false;
				break;
			}
		}
		return isCurrentYearPresent;
	}

	public void clickFullAccountRecordLink(){
		driver.waitForElementPresent(FULL_ACCOUNT_RECORD_LINK);
		driver.click(FULL_ACCOUNT_RECORD_LINK);
		logger.info("Full account record link clicked");
		driver.waitForPageLoad();
	}

	public void checkApplicationOnFileChkBox(){
		driver.waitForElementPresent(APPLICATION_ON_FILE_CHKBOX);
		driver.click(APPLICATION_ON_FILE_CHKBOX);
		logger.info("Application on file check box clicked");
		driver.waitForPageLoad();
	}

	public String getCRPStartDate(){
		driver.waitForElementPresent(CRP_START_DATE);
		String startDate = driver.findElement(CRP_START_DATE).getAttribute("value");
		logger.info("CRP start date is: "+startDate);
		return startDate;
	}

	public void clickCRPStartDate(){
		driver.waitForElementPresent(CRP_START_DATE);
		driver.click(CRP_START_DATE);
		logger.info("CRP Start date clicked");
		driver.waitForPageLoad();
	}

	public String getUserName(){
		driver.waitForElementPresent(USERNAME_LOC);
		String userName = driver.findElement(USERNAME_LOC).getAttribute("value");
		logger.info("Username is: "+userName);
		return userName;
	}

	public void enterUserName(String name){
		driver.waitForElementPresent(USERNAME_LOC);
		driver.type(USERNAME_LOC, name);
		logger.info("User Name text entered as: "+name);
	}

	public String getFirstName(){
		driver.waitForElementPresent(FIRST_NAME);
		String userName = driver.findElement(FIRST_NAME).getAttribute("value");
		logger.info("Firstname is: "+userName);
		return userName;
	}

	public void enterFirstName(String name){
		driver.waitForElementPresent(FIRST_NAME);
		driver.type(FIRST_NAME, name);
		logger.info("First Name text entered as: "+name);
	}

	public String getLastFourDgitOfHomePhoneNumber(){
		driver.waitForElementPresent(LAST_FOUR_DIGIT_OF_HOME_PHONE_NUMBER);
		String homePhoneNumber = driver.findElement(LAST_FOUR_DIGIT_OF_HOME_PHONE_NUMBER).getAttribute("value");
		logger.info("last four digit of home phone number is: "+homePhoneNumber);
		return homePhoneNumber;
	}

	public void enterLastFourDigitOfHomePhoneNumber(String number){
		driver.waitForElementPresent(LAST_FOUR_DIGIT_OF_HOME_PHONE_NUMBER);
		driver.type(LAST_FOUR_DIGIT_OF_HOME_PHONE_NUMBER, number);
		logger.info("last four digit of home phone number is: "+number);
	}

	public String getEmailAddress(){
		driver.waitForElementPresent(EMAIL_ADDRESS_LOC);
		String email = driver.findElement(EMAIL_ADDRESS_LOC).getAttribute("value");
		logger.info("Email address is: "+email);
		return email;
	}

	public void enterEmailAddress(String email){
		driver.waitForElementPresent(EMAIL_ADDRESS_LOC);
		driver.type(EMAIL_ADDRESS_LOC, email);
		logger.info("Email address entered as: "+email);
	}

	public String getTaxExemptValue(){
		reLoadPage();
		driver.waitForElementPresent(TAX_EXEMPT_VALUE);
		String taxExemptValue = driver.findElement(TAX_EXEMPT_VALUE).getText().trim();
		logger.info("Tax exempt value is: "+taxExemptValue);
		return taxExemptValue;
	}

	public String getTaxExemptValueForUpdate(String taxExemptValue){
		if(taxExemptValue.contains("No")){
			taxExemptValue = "Yes";
		}else{
			taxExemptValue = "No";
		}
		logger.info("Tax exempt value for update is: "+taxExemptValue);
		return taxExemptValue;
	}

	public void selectTaxExemptValue(String taxExemptValue){
		driver.click(TAX_EXEMPT_DD);
		logger.info("Tax exempt DD clicked");
		if(taxExemptValue.equalsIgnoreCase("Yes")){
			driver.click(TAX_EXEMPT_DD_YES_VALUE);
			logger.info("Yes value selected from DD for tax exempt");
		}else{
			driver.click(TAX_EXEMPT_DD_NO_VALUE);
			logger.info("No value selected from DD for tax exempt");
		}
	}

	public String getNameOnSSNCard(){
		driver.waitForElementPresent(NAME_ON_SSN_CARD);
		String email = driver.findElement(NAME_ON_SSN_CARD).getAttribute("value");
		logger.info("Name on SSN card is: "+email);
		return email;
	}

	public void enterNameOnSSNCard(String name){
		if(name.equalsIgnoreCase("null")){
			logger.info("Before updation name on SSN card is null ");
		}else{
			driver.waitForElementPresent(NAME_ON_SSN_CARD);
			driver.type(NAME_ON_SSN_CARD, name);
			logger.info("SSN Card name entered as: "+name);
		}
	}

	public String getDOBValue(){
		driver.waitForElementPresent(DOB_LOC);
		String dob = driver.findElement(DOB_LOC).getAttribute("value");
		logger.info("Date of birth is: "+dob);
		return dob;
	}

	public String getSelectedGender(){
		reLoadPage();
		String gender = driver.findElement(GENDER_DD_SELECTED_VALUE).getText().trim();
		logger.info("gender is: "+gender);
		return gender;
	}

	public void selectGender(String gender){
		driver.waitForElementPresent(GENDER_DD);
		driver.click(GENDER_DD);
		logger.info("Gender DD clicked");
		driver.click(By.xpath(String.format(genderDDValue, gender)));
		logger.info("Gender selected as: "+gender);
	}

	public String getAttentionName(){
		driver.waitForElementPresent(ATTENTION_NAME);
		String attentionName = driver.findElement(ATTENTION_NAME).getAttribute("value");
		logger.info("Attention name is: "+attentionName);
		return attentionName;
	}

	public void enterAttentionName(String attentionName){
		driver.waitForElementPresent(ATTENTION_NAME);
		driver.type(ATTENTION_NAME, attentionName);
		logger.info("Attention name entered as: "+attentionName);
	}

	public String getZIPCode(){
		driver.waitForElementPresent(ZIP_CODE);
		String zipCode = driver.findElement(ZIP_CODE).getAttribute("value");
		logger.info("ZIP code is: "+zipCode);
		return zipCode;
	}

	public void enterZIPCode(String zipCode){
		driver.waitForElementPresent(ZIP_CODE);
		driver.type(ZIP_CODE, zipCode);
		logger.info("ZIP Code entered as: "+zipCode);
	}


	public String getLastFourDgitOfPhoneNumber(){
		driver.waitForElementPresent(LAST_FOUR_DIGIT_OF_PHONE_NUMBER);
		String phoneNumber = driver.findElement(LAST_FOUR_DIGIT_OF_PHONE_NUMBER).getAttribute("value");
		logger.info("Phone Number is: "+phoneNumber);
		return phoneNumber;
	}

	public void enterLastFourDigitOfPhoneNumber(String number){
		driver.waitForElementPresent(LAST_FOUR_DIGIT_OF_PHONE_NUMBER);
		driver.type(LAST_FOUR_DIGIT_OF_PHONE_NUMBER, number);
		logger.info("Phone number entered as: "+number);
	}

	public void clickSaveBtnForAccountRecord(){
		driver.waitForElementPresent(SAVE_ACCOUNT_BTN);
		driver.click(SAVE_ACCOUNT_BTN);
		logger.info("Save button clicked for Account record");
		driver.waitForPageLoad();
	}

	public String getDayFromDate(String date){
		String day = date.split("\\/")[1];
		logger.info("The day is: "+day);
		return day;
	}

	public String getUpdatedDayFromDate(String day){
		if(day.contains("30")|| day.contains("31")){
			day = "1";
		}else{
			//int updatedDay = Integer.parseInt(day) 
			day = String.valueOf(Integer.parseInt(day)+1);
		}
		logger.info("Updated day is: "+day);
		return day;
	}

	public String getMonthFromDate(String date){
		String month = date.split("\\/")[0];
		logger.info("The month is: "+month);
		return month;
	}

	public String getYearFromDate(String date){
		String year = date.split("\\/")[2];
		logger.info("The Year is: "+year);
		return year;
	}

	public void clickUseAsEnteredbtn(){
		try{if(driver.isElementPresent(USE_AS_ENTERED_BTN)==true){
			driver.waitForElementPresent(USE_AS_ENTERED_BTN);
			driver.click(USE_AS_ENTERED_BTN);
			logger.info("Use as entered button clicked for Account record");
		}else{
			driver.click(ACCEPT_BTN);
			logger.info("Accept button clicked for Account record");
		}
		}catch(Exception e){
			driver.click(USE_ADDRESS_AS_ENTERED);
			logger.info("Usee address as entered button clicked for Account record");
		}
		driver.waitForPageLoad();
	}

	public String getUpdationMessage(){
		String msg = driver.findElement(GET_UPDATION_MSG).getText();
		logger.info("Updation mesage is: "+msg);
		return msg.trim();
	}

	public void clickDOBDate(){
		driver.waitForElementPresent(DOB_LOC);
		driver.click(DOB_LOC);
		logger.info("DOB date clicked");
		driver.waitForPageLoad();
	}

	public String getGenderValueForUpdate(String gender){
		if(gender.contains("None")){
			gender = "Male";
		}else{
			if(gender.contains("Male")){
				gender = "Female";
			}else{
				gender = "Male";
			}

		}
		logger.info("Gender for update is: "+gender);
		return gender;
	}

	public void clickBillingAndShippingProfileLink(){
		driver.quickWaitForElementPresent(BILLING_AND_SHIPPING_PROFILE_LINK_LOC);
		driver.click(BILLING_AND_SHIPPING_PROFILE_LINK_LOC);
		logger.info("Billing & Shipping Profile link clicked");
		driver.waitForPageLoad(); 
	}

	public int getTotalBillingProfiles(){
		List<WebElement> allBillingProfiles = driver.findElements(TOTAL_BILLING_PROFILES);
		logger.info("total Billing profiles are "+allBillingProfiles);
		return allBillingProfiles.size();
	}


	public void clickShippingProfileAddLink(){
		driver.quickWaitForElementPresent(SHIPPING_PROFILE_ADD_LINK_LOC);
		driver.click(SHIPPING_PROFILE_ADD_LINK_LOC);
		logger.info("Shipping Profile -Add link clicked");
		driver.waitForPageLoad(); 
	}

	public void addANewShippingProfile(String profileName,String attention,String addressLine1,String zipCode){
		driver.type(ADD_SHIPPING_ADDRESS_PROFILE_NAME_LOC, profileName);
		driver.type(ADD_SHIPPING_ADDRESS_ATTENTION_LOC, attention);
		driver.type(ADD_SHIPPING_ADDRESS_LINE1_LOC, addressLine1);
		driver.type(ADD_SHIPPING_ADDRESS_ZIPCODE_LOC, zipCode+"\t");
		driver.waitForNSCore4LoadingImageToDisappear();
		driver.click(STATE_DD_LOC);
		driver.click(STATE_DD_OPTION_LOC);
		driver.pauseExecutionFor(3500);
	}

	public void clickSaveAddressBtn(){
		driver.quickWaitForElementPresent(SAVE_ADDRESS_BTN_LOC);
		driver.click(SAVE_ADDRESS_BTN_LOC);
		driver.pauseExecutionFor(5000);
		try{
			driver.quickWaitForElementPresent(USE_ADDRESS_AS_ENTERED_BTN_LOC);
			driver.click(USE_ADDRESS_AS_ENTERED_BTN_LOC);
			logger.info("Use Address as Entered Btn clicked");
			driver.pauseExecutionFor(2000);
		}catch(Exception e){
			e.getMessage();
			logger.info("'Use Address Ad Entered' PopUp not displayed");
		}
		driver.waitForPageLoad();
	}

	public boolean isNewlyCreatedShippingProfilePresent(String shippingProfileName){
		driver.quickWaitForElementPresent(By.xpath(String.format(newlyCeatedShippingProfile, shippingProfileName)));
		return driver.isElementPresent(By.xpath(String.format(newlyCeatedShippingProfile, shippingProfileName)));
	}

	public void clickSetAsDefaultAddressForNewlyCreatedProfile(String shippingProfileName){
		driver.quickWaitForElementPresent(By.xpath(String.format(newlyCeatedShippingProfileSetDefault, shippingProfileName)));
		driver.click(By.xpath(String.format(newlyCeatedShippingProfileSetDefault, shippingProfileName)));
		driver.pauseExecutionFor(3000);
	}

	public boolean validateNewlyCreatedShippingProfileIsDefault(String shippingProfileName){
		driver.quickWaitForElementPresent(By.xpath(String.format(newlyCeatedShippingProfileIsDefault, shippingProfileName)));
		return driver.isElementPresent(By.xpath(String.format(newlyCeatedShippingProfileIsDefault, shippingProfileName)));
	}

	public void deleteAddressNewlyCreatedProfile(String shippingProfileName){
		driver.quickWaitForElementPresent(By.xpath(String.format(deleteAddressnewlyCeatedShippingProfile, shippingProfileName)));
		driver.click(By.xpath(String.format(deleteAddressnewlyCeatedShippingProfile, shippingProfileName)));
		driver.pauseExecutionFor(2000);
		//switch to Alert to delete payment method-
		clickOKBtnOfJavaScriptPopUp();
		driver.pauseExecutionFor(2000);
	}

	public void clickBillingProfileAddLink(){
		driver.quickWaitForElementPresent(BILLING_PROFILE_ADD_LINK_LOC);
		driver.click(BILLING_PROFILE_ADD_LINK_LOC);
		logger.info("Billing Profile -Add link clicked");
		driver.waitForPageLoad(); 
	}

	public void addANewBillingProfile(String firstName,String lastName,String nameOnCard,String cardNumber,String addressLine1,String zipCode){
		//select 'Main-Billing' as billing address-
		//		Select select = new Select(driver.findElement(ADD_NEW_BILLING_ADDRESS_DROP_DOWN_LOC));
		//		select.selectByIndex(1);
		driver.pauseExecutionFor(3000);
		//Add 'Payment-Method'
		driver.type(ADD_PAYMENT_METHOD_FIRST_NAME_LOC, firstName);
		driver.type(ADD_PAYMENT_METHOD_LAST_NAME_LOC, lastName);
		driver.type(ADD_PAYMENT_METHOD_NAME_ON_CARD_LOC, nameOnCard);
		driver.type(ADD_PAYMENT_METHOD_CREDIT_CARD_NO_LOC, cardNumber);
		//select 'Expiration' Date(change year)
		Select sel = new Select(driver.findElement(ADD_NEW_PAYMENT_EXPIRATION_YEAR_DROP_DOWN_LOC));
		sel.selectByIndex(7);
		driver.type(By.id("profileName"), firstName+" "+lastName);
		driver.type(By.id("addressLine1"), addressLine1);
		driver.type(By.id("zip"), zipCode+"\t");
		driver.pauseExecutionFor(2000);
	}

	public void clickSavePaymentMethodBtn(){
		driver.quickWaitForElementPresent(SAVE_PAYMENT_METHOD_BTN_LOC);
		driver.click(SAVE_PAYMENT_METHOD_BTN_LOC);
		logger.info("Save Payment Method Btn clicked");
		driver.pauseExecutionFor(3000);
		try{
			driver.quickWaitForElementPresent(USE_AS_ENTERED_BTN_LOC);
			driver.click(USE_AS_ENTERED_BTN_LOC);
			logger.info("Use as Entered Btn clicked");
			driver.pauseExecutionFor(2000);
		}catch(Exception e){
			try{
				driver.quickWaitForElementPresent(ACCEPT_BTN_LOC);
				driver.click(ACCEPT_BTN_LOC);
				logger.info("Accept Btn clicked");
				driver.pauseExecutionFor(2000);
			}catch(Exception e1){
				e.getMessage();
				logger.info("No PopUp found for 'Use As Entered'/'Accept' button");
			}
		}
		driver.waitForPageLoad();
	}

	public boolean isNewlyCreatedBilingProfilePresent(){
		driver.quickWaitForElementPresent(NEWLY_CREATED_BILLING_PROFILE_LOC);
		return driver.isElementPresent(NEWLY_CREATED_BILLING_PROFILE_LOC);
	}


	public void clickSetAsDefaultPaymentMethodForNewlyCreatedProfile(){
		driver.pauseExecutionFor(2000);
		driver.quickWaitForElementPresent(By.xpath(String.format(setAsDefaultForNewlyCreatedBillingProfile, getTotalBillingProfiles())));
		driver.click(By.xpath(String.format(setAsDefaultForNewlyCreatedBillingProfile, getTotalBillingProfiles())));
		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
	}

	public boolean validateNewlyCreatedBillingProfileIsDefault(){
		driver.quickWaitForElementPresent(By.xpath(String.format(isDefaultPresentForNewlyCreatedBillingProfile, getTotalBillingProfiles())));
		return driver.isElementPresent(By.xpath(String.format(isDefaultPresentForNewlyCreatedBillingProfile, getTotalBillingProfiles())));
	}

	public void deletePaymentMethodNewlyCreatedProfile(){
		driver.quickWaitForElementPresent(By.xpath(String.format(deleteNewlyCreatedBillingProfile, getTotalBillingProfiles())));
		driver.click(By.xpath(String.format(deleteNewlyCreatedBillingProfile, getTotalBillingProfiles())));
		logger.info("Delete button clicked");
		driver.pauseExecutionFor(2000);
		//switch to Alert to delete payment method-
		try{
			Alert alt =driver.switchTo().alert();
			alt.accept();
			logger.info("Alert handled");
		}catch(Exception e){

		}
		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
	}

	public boolean isBillingProfileDeleted(){
		List<WebElement> allBillingProfiles = driver.findElements(TOTAL_BILLING_PROFILES);
		if(allBillingProfilesSize!=allBillingProfiles.size()){
			return true;
		}
		return false;
	}

	public void clickSublinkOfOverview(String linkname){
		driver.waitForElementPresent(By.xpath(String.format(overViewSublinkLoc, linkname)));
		driver.click(By.xpath(String.format(overViewSublinkLoc, linkname)));
		logger.info(linkname+"clicked on overview page");
		driver.waitForPageLoad();
	}

	public void clickApplyPaymentButton(){
		driver.waitForElementPresent(APPLY_PAYMENT_BTN);
		driver.click(APPLY_PAYMENT_BTN);
		logger.info("Apply Payment button clicked");
		driver.waitForNSCore4LoadingImageToDisappear();
	}

	public String getOrderID(){
		driver.waitForElementPresent(PLACED_ORDER_NUMBER);
		String orderID = driver.findElement(PLACED_ORDER_NUMBER).getText();
		logger.info("Order number is: "+orderID);
		return orderID;
	}

	public void clickOrderId(String orderID){
		driver.waitForElementPresent(By.xpath(String.format(orderIdLinkLoc, orderID)));
		driver.click(By.xpath(String.format(orderIdLinkLoc, orderID)));
		logger.info(orderID+"clicked on overview page");
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
	}

	public String addAndGetProductSKU(String quantity){
		driver.waitForElementPresent(OPEN_BULK_ADD_LOC);
		driver.click(OPEN_BULK_ADD_LOC);
		driver.waitForNSCore4LoadingImageToDisappear();
		logger.info("Open bulk add link clicked");
		driver.waitForElementPresent(PRODUCT_SKU_VALUE);
		String SKU = driver.findElement(PRODUCT_SKU_VALUE).getText();
		driver.type(PRODUCT_QUANTITY_LOC, quantity);
		logger.info("quantity added is: "+quantity);
		driver.click(ADD_TO_ORDER_BTN);
		logger.info("Add to order button clicked");
		driver.click(CLOSE_LINK_LOC);
		logger.info("Close link clicked on bulk add popup");
		return SKU;
	}
	
	public void addAndGetSpecficProductSKU(String quantity){
		driver.waitForElementPresent(SKU_FIELD_SEARCH);
		driver.click(SKU_FIELD_SEARCH);
		//driver.waitForNSCore4LoadingImageToDisappear();
		//logger.info("Open bulk add link clicked");
		driver.type(SKU_FIELD_SEARCH, "AARG001");
		driver.waitForElementPresent(By.xpath("//*[@id='ContentWrap']/table/tbody/tr/td[2]/table[2]/tbody/tr/td[2]/p[1]/div/div/p"));
		driver.click(By.xpath("//*[@id='ContentWrap']/table/tbody/tr/td[2]/table[2]/tbody/tr/td[2]/p[1]/div/div/p"));
		//String SKU = driver.findElement(PRODUCT_SKU_VALUE).getText();
		
		driver.type(By.xpath("//*[@id='txtQuickAddQuantity']"), quantity);
		logger.info("quantity added is: "+quantity);
		driver.click(By.xpath("//*[@id='btnQuickAdd']"));
		logger.info("Add to order button clicked");
/*		driver.click(CLOSE_LINK_LOC);
		logger.info("Close link clicked on bulk add popup");*/
		//return SKU;
	}
	

	public int getTotalNoOfShippingProfiles(){
		driver.waitForElementPresent(SHIPPING_PROFILES_LOC);
		int noOfShippingProfile = driver.findElements(SHIPPING_PROFILES_LOC).size();
		logger.info("Total no of shipping profiles is: "+noOfShippingProfile);
		return noOfShippingProfile;
	}

	public String addAndGetProductSKUForCatalog(){
		driver.waitForElementPresent(OPEN_BULK_ADD_LOC);
		driver.click(OPEN_BULK_ADD_LOC);
		driver.waitForNSCore4LoadingImageToDisappear();
		logger.info("Open bulk add link clicked");
		driver.waitForElementPresent(PRODUCT_SKU_VALUE_CATALOG);
		String SKU = driver.findElement(PRODUCT_SKU_VALUE_CATALOG).getText();
		driver.click(PRODUCT_SKU_CHK_BOX_CATALOG);
		logger.info("Checkbox checked for add product");
		driver.click(ADD_TO_CATALOG);
		logger.info("Add to catalog button clicked");
		driver.click(CLOSE_LINK_CATALOG_LOC);
		logger.info("Close link clicked on bulk catalog popup");
		return SKU;
	}

	public void addPaymentMethod(String firstName,String lastName,String nameOnCard,String cardNumber){
		driver.waitForElementPresent(ADD_NEW_PAYMENT_METHOD_LINK);
		driver.click(ADD_NEW_PAYMENT_METHOD_LINK);
		logger.info("Add new payment method link clicked");
		driver.pauseExecutionFor(3000);
		//enter billing info
		//select 'Main-Billing' as billing address-
		Select select = new Select(driver.findElement(ADD_NEW_BILLING_ADDRESS_DROP_DOWN_LOC));
		select.selectByIndex(1);
		driver.pauseExecutionFor(4500);
		//Add 'Payment-Method'
		driver.type(ADD_PAYMENT_METHOD_FIRST_NAME_LOC, firstName);
		logger.info("First name entered for billing profile is: "+firstName);
		driver.type(ADD_PAYMENT_METHOD_LAST_NAME_LOC, lastName);
		logger.info("last name entered for billing profile is: "+lastName);
		driver.type(ADD_PAYMENT_METHOD_NAME_ON_CARD_LOC, nameOnCard);
		logger.info("name on card entered for billing profile is: "+nameOnCard);
		driver.type(ADD_PAYMENT_METHOD_CREDIT_CARD_NO_LOC, cardNumber);
		logger.info("card number entered for billing profile is: "+cardNumber);
		//select 'Expiration' Date(change year)
		Select sel = new Select(driver.findElement(ADD_NEW_PAYMENT_EXPIRATION_YEAR_DROP_DOWN_LOC));
		sel.selectByIndex(7);
		logger.info("Credit card expire year selected");
	}

	public boolean isEditPresentForConsultantReplenishmentPresent(){
		driver.waitForElementPresent(CONSULTANT_REPLENISHMENT_EDIT);
		return driver.isElementPresent(CONSULTANT_REPLENISHMENT_EDIT);
	}

	public boolean isEditPresentForPulseMonthlySubscriptionPresent(){
		driver.waitForElementPresent(PULSE_MONTHLY_SUBSCRIPTION_EDIT);
		return driver.isElementPresent(PULSE_MONTHLY_SUBSCRIPTION_EDIT);
	}
	
	
/*	public void ExcelWriter(String firstname,String lastname, String accountNumber) throws IOException{
		//XSSFWorkbook workbook = new XSSFWorkbook();
        //XSSFSheet sheet = workbook.createSheet("Java Books");
		FileInputStream fsIP= new FileInputStream(new File("C:\\Users\\plu\\heirloom\\rf-automation\\JavaBooks.xlsx"));
		XSSFWorkbook wb = new XSSFWorkbook(fsIP); //Access the workbook
        
		XSSFSheet worksheet = wb.getSheetAt(0);
		
        Object[][] bookData = {
                {firstname, lastname,accountNumber},
        };
 
        int rowCount = n;
        int rows=worksheet.getLastRowNum(); 			
        for (Object[] aBook : bookData) {
        	
            Row row = worksheet.createRow(++rows);
             
            int columnCount = 0;
             
            for (Object field : aBook) {
                Cell cell = row.createCell(++columnCount);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
             
        }
         
         
        try (FileOutputStream outputStream = new FileOutputStream("JavaBooks.xlsx")) {
        	wb.write(outputStream);
            outputStream.close();
        }
	}*/
		
}