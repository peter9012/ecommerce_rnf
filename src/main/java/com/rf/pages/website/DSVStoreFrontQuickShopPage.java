package com.rf.pages.website;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;

public class DSVStoreFrontQuickShopPage extends DSVRFWebsiteBasePage {

	private static final Logger logger = LogManager
			.getLogger(DSVStoreFrontQuickShopPage.class.getName());

	private static final By FIRST_PRODUCT_ADD_TO_CRP_BTN = By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']");
	private static final By FIRST_PRODUCT_NAME = By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]/h3"); 
	private static final By FIRST_PRODUCT_RETAIL_PRICE = By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//span[@class='old-price']");
	private static final By PRODUCT_FILTER_DROP_DOWN = By.xpath("//input[@class='refine-products-button'][contains(@value,'Product(s)')]");
	private static final By PRICE_FILTER_DROP_DOWN = By.xpath("//input[@class='refine-products-button'][contains(@value,'Price')]");
	private static final By ALL_PRODUCTS_FROM_PRODUCT_FILTER_DROP_DOWN = By.xpath("//input[@class='refine-products-button'][contains(@value,'Product(s)')]/following::ul[1]/li");
	private static final By ALL_PRICE_FROM_PRICE_FILTER_DROP_DOWN = By.xpath("//input[@class='refine-products-button'][contains(@value,'Price')]/following::ul[1]/li");
	private static final By ALL_PRODUCTS_DISPLAYED_ON_PAGE = By.xpath("//div[@class='quick-product-wrapper']/div");
	
	private static String RandomProductFromProductFilterDropDown = "//input[@class='refine-products-button'][contains(@value,'Product(s)')]/following::ul[1]/li[%s]//div[contains(@class,'repaired-checkbox')]/input";
	private static String RandomProductCheckboxFromProductFilterDropDown = "//input[@class='refine-products-button'][contains(@value,'Product(s)')]/following::ul[1]/li[1]//div[contains(@class,'repaired-checkbox')]";
	private static String RandomPriceCheckboxFromPriceFilterDropDown = "//input[@class='refine-products-button'][contains(@value,'Price')]/following::ul[1]/li[1]//div[contains(@class,'repaired-checkbox')]";
	private static String RandomPriceFromPriceFilterDropDown = "//input[@class='refine-products-button'][contains(@value,'Price')]/following::ul[1]/li[%s]//div[contains(@class,'repaired-checkbox')]/input";
	private static String SelectedProductCheckbox = "//div[@id='quick-filtered']//div[@class='repaired-checkbox checked']/input[@id='%s'][@class='checked']";
	private static String SelectedProductAsHeadingOnProductPage = "//div[@class='quick-shop-section-header']/h2[text()='%s']";
	private static String RandomProductPrice = "//div[@class='quick-product-wrapper'][1]/div[%s]//span[@class='old-price']";
	
	public DSVStoreFrontQuickShopPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public DSVStoreFrontAutoshipCartPage clickAddToCRPForFirstProduct(){
		driver.quickWaitForElementPresent(FIRST_PRODUCT_ADD_TO_CRP_BTN);
		driver.click(FIRST_PRODUCT_ADD_TO_CRP_BTN);
		driver.waitForLoadingImageToDisappear();
		return new DSVStoreFrontAutoshipCartPage(driver);
	}

	public String getFirstProductName(){
		return driver.findElement(FIRST_PRODUCT_NAME).getText().trim();
	}
	
	public String getFirstProductRetailPrice(){
		return driver.findElement(FIRST_PRODUCT_RETAIL_PRICE).getText().trim();
	}
	
	public String selectAndReturnTheSelectedProductFromFilter(){
		int randomValue;
		driver.quickWaitForElementPresent(PRODUCT_FILTER_DROP_DOWN);
		driver.click(PRODUCT_FILTER_DROP_DOWN);
		List<WebElement> allProductsFromProductFilterList = driver.findElements(ALL_PRODUCTS_FROM_PRODUCT_FILTER_DROP_DOWN);
		randomValue = CommonUtils.getRandomNum(1, allProductsFromProductFilterList.size());
		System.out.println("Random value is "+randomValue);
		driver.quickWaitForElementPresent(By.xpath(String.format(RandomProductCheckboxFromProductFilterDropDown, randomValue)));
		driver.click(By.xpath(String.format(RandomProductCheckboxFromProductFilterDropDown, randomValue)));
		driver.waitForPageLoad();
		return driver.getAttribute(By.xpath(String.format(RandomProductFromProductFilterDropDown, String.valueOf(randomValue))), "id");
	}
	
	public boolean isProductFilterApplied(String selectedProduct){
		driver.quickWaitForElementPresent(By.xpath(String.format(SelectedProductCheckbox, selectedProduct)));
		return driver.isElementPresent(By.xpath(String.format(SelectedProductCheckbox, selectedProduct)))
		&& driver.isElementPresent(By.xpath(String.format(SelectedProductAsHeadingOnProductPage, selectedProduct)));
	}
	
	public String selectAndReturnTheSelectedPriceFromFilter(){
		int randomValue;
		driver.quickWaitForElementPresent(PRICE_FILTER_DROP_DOWN);
		driver.click(PRICE_FILTER_DROP_DOWN);
		List<WebElement> allPriceFromPriceFilterList = driver.findElements(ALL_PRICE_FROM_PRICE_FILTER_DROP_DOWN);
		randomValue = CommonUtils.getRandomNum(1, allPriceFromPriceFilterList.size());
		driver.quickWaitForElementPresent(By.xpath(String.format(RandomPriceFromPriceFilterDropDown, randomValue)));
		driver.click(By.xpath(String.format(RandomPriceCheckboxFromPriceFilterDropDown, randomValue)));
		driver.waitForPageLoad();
		String priceRangeFromUI =  driver.getAttribute(By.xpath(String.format(RandomPriceFromPriceFilterDropDown, String.valueOf(randomValue))), "id");
		return priceRangeFromUI.split("TO")[1].substring(5);		
	}
	
	public double getPriceOfRandomProductAfterPriceFilterApplied(){
		int randomValue;
		List<WebElement> allProductsDisplayedOnPageList = driver.findElements(ALL_PRODUCTS_DISPLAYED_ON_PAGE);
		randomValue = CommonUtils.getRandomNum(1, allProductsDisplayedOnPageList.size());
		System.out.println(("Random price of "+randomValue+" product is "+driver.findElement(By.xpath(String.format(RandomProductPrice, randomValue))).getText().split(" ")[1]));
		return Double.parseDouble(driver.findElement(By.xpath(String.format(RandomProductPrice, randomValue))).getText().split(" ")[1]);
	}
		
}
