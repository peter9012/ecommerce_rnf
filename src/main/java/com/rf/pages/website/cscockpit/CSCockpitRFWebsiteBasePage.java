package com.rf.pages.website.cscockpit;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.RFBasePage;

public class CSCockpitRFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitRFWebsiteBasePage.class.getName());

	private static String sortByDropDownLoc= "//div[@class='csResultsSortList']/select/option[text()='%s']";
	private static String skuValueOfProductFromSearchResultLoc = "//div[@class='csListboxContainer']/div[2]/div[@class='z-listbox-body']//tbody[2]/tr[%s]/td[2]/div";

	private static final By NO_RESULTS_LBL = By.xpath("//span[text()='No Results']");
	private static final By CHANGE_ORDER_LINK = By.xpath("//a[text()='Change Order']");
	private static final By MENU_BTN_LOCATOR = By.xpath("//span[text()='Menu']");
	private static final By LOGOUT_BTN_LOCATOR = By.xpath("//a[contains(text(),'Logout')]");
	private static final By FIND_ORDER_LINK_LEFT_NAVIGATION_LOC = By.xpath("//a[contains(text(),'Find Order')]");
	private static final By CUSTOMER_TAB = By.xpath("//span[text()='Customer']");
	private static final By ORDER_SEARCH_TAB_BTN = By.xpath("//span[text()='Order Search']");
	private static final By CUSTOMER_SEARCH_TAB_LOC = By.xpath("//span[text()='Customer Search']");
	private static final By FIND_AUTOSHIP_LINK_LEFT_NAVIGATION_LOC = By.xpath("//a[contains(text(),'Find Autoship')]");
	private static final By CATALOG_DD = By.xpath("//span[contains(text(),'Select Catalog')]/following::select[1]");
	private static final By CATALOG_DD_OPTION = By.xpath("//span[contains(text(),'Select Catalog')]/following::select[1]/option");
	private static final By SEARCH_SKU_VALUE_TXT_FIELD = By.xpath("//span[contains(text(),'Select Catalog')]/following::input[1]");
	private static final By SEARCH_BTN_SKU = By.xpath("//span[contains(text(),'Select Catalog')]/following::td[contains(text(),'Search')]");
	private static final By ADD_TO_CART_BTN = By.xpath("//td[contains(text(),'Add To Cart')]");
	private static final By CHECKOUT_BTN = By.xpath("//td[contains(text(),'Checkout')]");
	private static final By TOTAL_PRODUCTS_WITH_SKU = By.xpath("//div[@class='csListboxContainer']/div[2]/div[@class='z-listbox-body']//tbody[2]/tr"); 
	private static final By PRODUCT_NOT_AVAILABLE_POPUP_OK_BTN = By.xpath("//td[@class='z-button-cm'][text()='OK']");
	private static final By PAYMENT_DETAILS_POPUP_OK_BTN = By.xpath("//td[text()='OK']");
	private static final By CVV2_SEARCH_TXT_FIELD = By.xpath("//div[contains(text(),'Action')]/following::td[contains(text(),'Use this card')][1]/preceding::input[1]");
	private static final By USE_THIS_CARD_BTN = By.xpath("//span[contains(text(),'Stored Credit Cards')]/following::div[@class='z-listbox-body']//tbody[2]/tr[1]//td[contains(text(),'Use this card')]");
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
	private static final By REVIEW_CREDIT_CARD_DETAILS_POPUP = By.xpath("//span[contains(text(),'Please review credit card information entered')]");
	private static final By AUTOSHIP_SEARCH_TAB = By.xpath("//span[text()='Autoship Search']");
	
	protected RFWebsiteDriver driver;
	public CSCockpitRFWebsiteBasePage(RFWebsiteDriver driver) {
		super(driver);
		this.driver=driver;
	}

	public String getBaseURL(){
		return driver.getURL();
	}

	public String getCurrentURL(){
		return driver.getCurrentUrl();
	}

	public void openURL(String URL){
		driver.get(URL);
		driver.waitForPageLoad();
	}	


	public boolean isNoResultDisplayed(){
		driver.quickWaitForElementPresent(NO_RESULTS_LBL);
		return driver.isElementPresent(NO_RESULTS_LBL);
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

	public void refreshPage(){
		driver.navigate().refresh();
		driver.waitForPageLoad();
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickChangeOrderLinkOnLeftNavigation(){
		driver.waitForElementPresent(CHANGE_ORDER_LINK);
		driver.click(CHANGE_ORDER_LINK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickMenuButton(){
		driver.waitForElementPresent(MENU_BTN_LOCATOR);
		driver.click(MENU_BTN_LOCATOR);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickLogoutButton(){
		driver.waitForElementPresent(LOGOUT_BTN_LOCATOR);
		driver.click(LOGOUT_BTN_LOCATOR);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickFindOrderLinkOnLeftNavigation() {
		driver.waitForElementPresent(FIND_ORDER_LINK_LEFT_NAVIGATION_LOC);
		driver.click(FIND_ORDER_LINK_LEFT_NAVIGATION_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCustomerTab(){
		driver.waitForElementPresent(CUSTOMER_TAB);
		driver.click(CUSTOMER_TAB);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickOrderSearchTab(){
		driver.waitForElementPresent(ORDER_SEARCH_TAB_BTN);
		driver.click(ORDER_SEARCH_TAB_BTN);
		logger.info("Order Search tab clicked");
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCustomerSearchTab() {
		driver.waitForElementPresent(CUSTOMER_SEARCH_TAB_LOC);
		driver.click(CUSTOMER_SEARCH_TAB_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickFindAutoshipInLeftNavigation(){
		driver.waitForElementPresent(FIND_AUTOSHIP_LINK_LEFT_NAVIGATION_LOC);
		driver.click(FIND_AUTOSHIP_LINK_LEFT_NAVIGATION_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();		
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
	}

	public void switchToPreviousTab(){
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		driver.close();
		driver.switchTo().window(tabs.get(0));
		driver.pauseExecutionFor(1000);
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

	public int getRandomProductWithSKUFromSearchResult(){
		int totalProductsWithSKUFromSearchResult = driver.findElements(TOTAL_PRODUCTS_WITH_SKU).size();
		int randomProductFromSearchResult = CommonUtils.getRandomNum(1, totalProductsWithSKUFromSearchResult);
		logger.info("Random Customer sequence number is "+randomProductFromSearchResult);
		return randomProductFromSearchResult;
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
				driver.quickWaitForElementPresent(PRODUCT_NOT_AVAILABLE_POPUP_OK_BTN);
				driver.click(PRODUCT_NOT_AVAILABLE_POPUP_OK_BTN);
				clearCatalogSearchFieldAndClickSearchBtn();
				searchSKUValueInCartTab(getCustomerSKUValueInCartTab(String.valueOf(getRandomProductWithSKUFromSearchResult())));
			}catch(Exception e){
				logger.info("Product is available");
				break;
			}	
		}

	}

	public void clickCheckoutBtnInCartTab(){
		driver.waitForElementPresent(CHECKOUT_BTN);
		driver.click(CHECKOUT_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clearCatalogSearchFieldAndClickSearchBtn(){
		driver.clear(SEARCH_SKU_VALUE_TXT_FIELD);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.click(SEARCH_BTN_SKU);;
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
		if(driver.isElementPresent(REVIEW_CREDIT_CARD_DETAILS_POPUP)==true){
			clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
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
			driver.waitForPageLoad();
			enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
			driver.click(USE_THIS_CARD_BTN);
			driver.waitForCSCockpitLoadingImageToDisappear();
		}

	}

	public void clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab(){
		driver.waitForElementPresent(PAYMENT_DETAILS_POPUP_OK_BTN);
		driver.click(PAYMENT_DETAILS_POPUP_OK_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickAutoshipSearchTab(){
		driver.waitForElementPresent(AUTOSHIP_SEARCH_TAB);
		driver.click(AUTOSHIP_SEARCH_TAB);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.pauseExecutionFor(30000);
	}
}
