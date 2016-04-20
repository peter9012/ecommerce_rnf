package com.rf.pages.website.nscore;

import java.util.ArrayList;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

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
	private static final By ROWS_COUNT_OF_SEARCH_RESULT  = By.xpath("//table[@id='accounts']/tbody/tr");
	private static String rowNumber = "//table[@id='accounts']/tbody/tr[%s]/td/a";
	private static String proxyLinksLoc = "//div[@class='DistributorProxies']//li/a[text()='%s']";
	private static String myAccountLinkAfterLoginLink = "//div[@class='topContents']//span[text()='%s']";
	private static String waitingForApprovalLink = "//ul[@id='stories']/li/a[contains(text(),'%s')]";

	private static String storyNameOnEditOptionPage = "//p[text()='%s']";
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
		driver.click(ORDER_ID_PENDING_LOC);
		logger.info("pending order id is clicked");
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

}