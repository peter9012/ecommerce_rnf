package com.rf.pages.website.nscore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore4HomePage extends NSCore3RFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(NSCore4HomePage.class.getName());

	public NSCore4HomePage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}


	private static String productOnOrderTableOnOrderPage = "//table[@id='products']//td[contains(text(),'%s')]";
	private static String productOnOrderTableOnOrderDetailPage = "//table[@class='DataGrid']//td[contains(text(),'%s')]";

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
		return driver.isElementPresent(By.xpath("//div[contains(@class,'resultItem')][1]//p[contains(text(),'"+firstName+"')]"))
				&& driver.isElementPresent(By.xpath("//div[contains(@class,'resultItem')][1]//p[contains(text(),'"+lastName+"')]"));

	}

	public void clickCustomerlabelOnOrderDetailPage(){
		driver.click(CUSTOMER_LABEL_ORDER_DETAIL_PAGE);
		logger.info("customer label on order detail page clicked");
		driver.waitForPageLoad();
	}

}