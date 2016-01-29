package com.rf.pages.website.cscockpit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;

public class CSCockpitCartTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitCartTabPage.class.getName());
	
	private static String sortByDropDownLoc= "//div[@class='csResultsSortList']/select/option[text()='%s']";
	private static String skuValueOfProductFromSearchResultLoc = "//div[@class='csListboxContainer']/div[2]/div[@class='z-listbox-body']//tbody[2]/tr[%s]/td[2]/div";
	
	private static final By CATALOG_DD = By.xpath("//span[contains(text(),'Select Catalog')]/following::select[1]");
	private static final By CATALOG_DD_OPTION = By.xpath("//span[contains(text(),'Select Catalog')]/following::select[1]/option");
	private static final By SEARCH_SKU_VALUE_TXT_FIELD = By.xpath("//span[contains(text(),'Select Catalog')]/following::input[1]");
	private static final By SEARCH_BTN_SKU = By.xpath("//span[contains(text(),'Select Catalog')]/following::td[contains(text(),'Search')]");
	private static final By ADD_TO_CART_BTN = By.xpath("//td[contains(text(),'Add To Cart')]");
	private static final By CHECKOUT_BTN = By.xpath("//td[contains(text(),'Checkout')]");
	private static final By TOTAL_PRODUCTS_WITH_SKU = By.xpath("//div[@class='csListboxContainer']/div[2]/div[@class='z-listbox-body']//tbody[2]/tr"); 
	private static final By PRODUCT_NOT_AVAILABLE_POPUP_OK_BTN = By.xpath("//td[@class='z-button-cm'][text()='OK']");
	
		
	protected RFWebsiteDriver driver;

	public CSCockpitCartTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
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

	public int getRandomProductWithSKUFromSearchResult(){
		int totalProductsWithSKUFromSearchResult = driver.findElements(TOTAL_PRODUCTS_WITH_SKU).size();
		int randomProductFromSearchResult = CommonUtils.getRandomNum(1, totalProductsWithSKUFromSearchResult);
		logger.info("Random Customer sequence number is "+randomProductFromSearchResult);
		return randomProductFromSearchResult;
	}



}