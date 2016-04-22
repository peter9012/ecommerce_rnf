package com.rf.pages.website.nscore;

import java.util.ArrayList;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore4HomePage extends NSCore4RFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(NSCore4HomePage.class.getName());

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
		driver.waitForElementPresent(ACCOUNT_SEARCH_RESULTS);
		logger.info("account info entered in search field is "+accountinfo);
	}

	public void clickGoBtnOfSearch(String accountNumber){
		driver.click(GO_SEARCH_BTN);
		logger.info("Search Go button clicked");
		try {
			driver.waitForElementPresent(By.linkText(accountNumber));
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
	}

	public String updatePulseProductQuantityAndReturnValue(){
		int quantity = Integer.valueOf(driver.findElement(PULSE_PRODUCT_QUANTITY_TXT_FIELD).getAttribute("value"));;
		quantity = quantity +1;
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
		driver.waitForPageLoad();
	}

	public boolean isAddedProductPresentInOrderDetailPage(String SKU){
		driver.quickWaitForElementPresent(By.xpath(String.format(productOnOrderTableOnOrderDetailPage, SKU)));
		return driver.isElementPresent(By.xpath(String.format(productOnOrderTableOnOrderDetailPage, SKU)));
	}

	public String getQuantityOfPulseProductFromOrderDetailPage(){
		driver.waitForElementPresent(QUANTITY_OF_PULSE_PRODUCT_ON_ORDER_DETAIL_PAGE);
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
		driver.quickWaitForElementPresent(By.xpath(String.format(myAccountLinkAfterLoginLink, linkName)));
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
		driver.quickWaitForElementPresent(I_WANT_TO_WRITE_OWN_STORY);
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

}