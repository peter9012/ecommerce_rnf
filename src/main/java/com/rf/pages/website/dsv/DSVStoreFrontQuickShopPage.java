package com.rf.pages.website.dsv;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.rf.core.driver.RFDriver;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;

public class DSVStoreFrontQuickShopPage extends DSVRFWebsiteBasePage {

	private static final Logger logger = LogManager
			.getLogger(DSVStoreFrontQuickShopPage.class.getName());

	private static final By FIRST_PRODUCT_ADD_TO_CRP_BTN = By.xpath("//div[@id='quick-refine']/following-sibling::div[3]/div[1]//button[contains(text(),'ADD TO CRP')]");
	private static final By SECOND_PRODUCT_ADD_TO_CRP_BTN = By.xpath("//div[@id='quick-refine']/following-sibling::div[3]/div[2]//button[contains(text(),'ADD TO CRP')]");
	private static final By FIRST_PRODUCT_NAME = By.xpath("//div[@id='quick-refine']/following-sibling::div[3]/div[1]//h3/a"); 
	private static final By FIRST_PRODUCT_RETAIL_PRICE = By.xpath("//div[@id='quick-refine']/following-sibling::div[3]/div[1]//span[@class='old-price'][1]");
	private static final By SECOND_PRODUCT_RETAIL_PRICE = By.xpath("//div[@id='quick-refine']/following-sibling::div[3]/div[2]//span[@class='old-price'][1]");
	private static final By PRODUCT_FILTER_DROP_DOWN = By.xpath("//input[@class='refine-products-button'][contains(@value,'Product(s)')]");
	private static final By PRICE_FILTER_DROP_DOWN = By.xpath("//input[@class='refine-products-button'][contains(@value,'Price')]");
	private static final By ALL_PRODUCTS_FROM_PRODUCT_FILTER_DROP_DOWN = By.xpath("//input[@class='refine-products-button'][contains(@value,'Product(s)')]/following::ul[1]/li");
	private static final By ALL_PRICE_FROM_PRICE_FILTER_DROP_DOWN = By.xpath("//input[@class='refine-products-button'][contains(@value,'Price')]/following::ul[1]/li");
	private static final By ALL_PRODUCTS_DISPLAYED_ON_PAGE = By.xpath("//div[@id='main-content']/div[5]/div");
	private static final By ALL_SORT_FROM_PRICE_FILTER_DROP_DOWN = By.xpath("//select[@id='sortOptions']/option[@value]");

	private static final By CLEAR_ALL_LINK = By.xpath("//a[contains(text(),'Clear All')]");
	private static final By SORT_DROP_DOWN = By.id("sortOptions");
	private static final By FIRST_PRODUCT_ADD_TO_PC_PERKS_BTN = By.xpath("//div[@id='main-content']/div[5]/div[1]//input[@value='ADD to PC Perks']");
	private static final By SECOND_PRODUCT_ADD_TO_PC_PERKS_BTN = By.xpath("//div[@id='main-content']/div[5]/div[2]//input[@value='ADD to PC Perks']");

	private static String RandomProductFromProductFilterDropDown = "//input[@class='refine-products-button'][contains(@value,'Product(s)')]/following::ul[1]/li[%s]//div[contains(@class,'repaired-checkbox')]/input";
	private static String RandomProductCheckboxFromProductFilterDropDown = "//input[@class='refine-products-button'][contains(@value,'Product(s)')]/following::ul[1]/li[%s]//div[contains(@class,'repaired-checkbox')]";
	private static String RandomPriceCheckboxFromPriceFilterDropDown = "//input[@class='refine-products-button'][contains(@value,'Price')]/following::ul[1]/li[%s]//div[contains(@class,'repaired-checkbox')]";
	private static String RandomPriceFromPriceFilterDropDown = "//input[@class='refine-products-button'][contains(@value,'Price')]/following::ul[1]/li[%s]//div[contains(@class,'repaired-checkbox')]/input";
	private static String RandomOrderFromSortFilterDropDown = "//select[@id='sortOptions']/option[@value][%s]";
	private static String SelectedProductCheckbox = "//div[@id='quick-filtered']//div[@class='repaired-checkbox checked']/input[@id='%s'][@class='checked']";
	private static String SelectedProductAsHeadingOnProductPage = "//div[@class='quick-shop-section-header']/h2[text()='%s']";
	private static String RandomProductPrice = "//div[@id='main-content']/div[5]//div[%s]//p[@class='prices']//span[contains(@class,'your-price')]";

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

	public DSVStoreFrontAutoshipCartPage clickAddToCRPForSecondProduct(){
		driver.quickWaitForElementPresent(SECOND_PRODUCT_ADD_TO_CRP_BTN);
		driver.click(SECOND_PRODUCT_ADD_TO_CRP_BTN);
		driver.waitForLoadingImageToDisappear();
		return new DSVStoreFrontAutoshipCartPage(driver);
	}


	public String getFirstProductName(){
		return driver.findElement(FIRST_PRODUCT_NAME).getText().trim();
	}

	public String getFirstProductRetailPrice(){
		return driver.findElement(FIRST_PRODUCT_RETAIL_PRICE).getText().trim();
	}

	public String getSecondProductRetailPrice(){
		return driver.findElement(SECOND_PRODUCT_RETAIL_PRICE).getText().trim();
	}

	public List<String> getAllProductsFromProductFilterList(){
		List<String> allProductsList = new ArrayList<String>();
		List<WebElement> allProductsFromProductFilterList = driver.findElements(ALL_PRODUCTS_FROM_PRODUCT_FILTER_DROP_DOWN);
		System.out.println("allProductsFromProductFilterList size ="+allProductsFromProductFilterList.size());
		for(int i=1;i<=allProductsFromProductFilterList.size();i++){
			allProductsList.add(driver.findElement(By.xpath("//input[@class='refine-products-button'][contains(@value,'Product(s)')]/following::ul[1]/li["+i+"]//div[contains(@class,'repaired-checkbox')]/input")).getAttribute("id"));
		}
		return allProductsList;
	}

	public void clickProductFilterDropDown(){
		driver.quickWaitForElementPresent(PRODUCT_FILTER_DROP_DOWN);
		driver.click(PRODUCT_FILTER_DROP_DOWN);
		driver.pauseExecutionFor(2000);
	}

	public String selectAndReturnTheSelectedProductFromFilter(){
		int randomValue;
		List<WebElement> allProductsFromProductFilterList = driver.findElements(ALL_PRODUCTS_FROM_PRODUCT_FILTER_DROP_DOWN);
		System.out.println("allProductsFromProductFilterList size ="+allProductsFromProductFilterList.size());
		randomValue = CommonUtils.getRandomNum(1, allProductsFromProductFilterList.size());
		System.out.println("Random value is "+randomValue);
		driver.quickWaitForElementPresent(By.xpath(String.format(RandomProductCheckboxFromProductFilterDropDown, randomValue)));
		driver.click(By.xpath(String.format(RandomProductCheckboxFromProductFilterDropDown, randomValue)));
		driver.pauseExecutionFor(1000);
		driver.waitForPageLoad();
		return driver.getAttribute(By.xpath(String.format(RandomProductFromProductFilterDropDown, String.valueOf(randomValue))), "id");
	}

	public boolean isProductFilterApplied(String selectedProduct){
		//driver.quickWaitForElementPresent(By.xpath(String.format(SelectedProductCheckbox, selectedProduct)));
		driver.turnOffImplicitWaits();
		boolean isProductFilterApplied = driver.isElementPresent(By.xpath(String.format(SelectedProductCheckbox, selectedProduct)))
				&& driver.isElementPresent(By.xpath(String.format(SelectedProductAsHeadingOnProductPage, selectedProduct)));
		driver.turnOnImplicitWaits();
		return isProductFilterApplied;
	}

	public void clickClearAllLink(){
		driver.pauseExecutionFor(2000);
		((JavascriptExecutor)RFWebsiteDriver.driver).executeScript("arguments[0].click();", driver.findElement(CLEAR_ALL_LINK));
		driver.pauseExecutionFor(2000);
		System.out.println("clear link clicked");
		driver.waitForPageLoad();
	}

	public String selectAndReturnTheSelectedPriceFromFilter(){
		int randomValue;
		driver.quickWaitForElementPresent(PRICE_FILTER_DROP_DOWN);
		driver.click(PRICE_FILTER_DROP_DOWN);
		driver.pauseExecutionFor(1000);
		List<WebElement> allPriceFromPriceFilterList = driver.findElements(ALL_PRICE_FROM_PRICE_FILTER_DROP_DOWN);
		randomValue = CommonUtils.getRandomNum(1, allPriceFromPriceFilterList.size());
		System.out.println("Random value for price filter is "+randomValue);
		driver.quickWaitForElementPresent(By.xpath(String.format(RandomPriceFromPriceFilterDropDown, randomValue)));
		driver.click(By.xpath(String.format(RandomPriceCheckboxFromPriceFilterDropDown, randomValue)));
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
		String priceRangeFromUI =  driver.getAttribute(By.xpath(String.format(RandomPriceFromPriceFilterDropDown, String.valueOf(randomValue))), "id");
		priceRangeFromUI = priceRangeFromUI.split("TO")[1].substring(5);
		System.out.println("maximum value from selected Price range is "+priceRangeFromUI);
		return priceRangeFromUI;
	}

	public String selectAndReturnTheSelectedSortOrderFromFilter(){
		int randomValue;
		driver.quickWaitForElementPresent(SORT_DROP_DOWN);
		driver.click(SORT_DROP_DOWN);
		List<WebElement> allSortOrderFromSortFilterList = driver.findElements(ALL_SORT_FROM_PRICE_FILTER_DROP_DOWN);
		randomValue = CommonUtils.getRandomNum(1, allSortOrderFromSortFilterList.size());
		driver.quickWaitForElementPresent(By.xpath(String.format(RandomOrderFromSortFilterDropDown, randomValue)));
		driver.click(By.xpath(String.format(RandomOrderFromSortFilterDropDown, randomValue)));
		driver.waitForPageLoad();
		String sortOrder =  driver.findElement(By.xpath(String.format(RandomOrderFromSortFilterDropDown, String.valueOf(randomValue)))).getText();
		return sortOrder;		
	}

	public boolean isSortOrderApplied(String sortOrder){
		driver.waitForElementPresent(By.xpath(String.format(RandomProductPrice, "1")));
		double firstProductPrice =  Double.parseDouble(driver.findElement(By.xpath(String.format(RandomProductPrice, "1"))).getText().replaceAll("[^0-9.]", ""));
		System.out.println("first product price as double  value = "+firstProductPrice);
		double secondProductPrice =  Double.parseDouble(driver.findElement(By.xpath(String.format(RandomProductPrice, "2"))).getText().replaceAll("[^0-9.]", ""));
		System.out.println("second product price as double  value = "+secondProductPrice);
		double thirdProductPrice =  Double.parseDouble(driver.findElement(By.xpath(String.format(RandomProductPrice, "3"))).getText().replaceAll("[^0-9.]", ""));
		System.out.println("third product price as double  value = "+thirdProductPrice);
		boolean isSortOrderApplied = false;
		if(sortOrder.toLowerCase().contains("high to low")){
			isSortOrderApplied = (thirdProductPrice<secondProductPrice?(secondProductPrice<firstProductPrice?true:false):false);
		}
		else if(sortOrder.toLowerCase().contains("low to high")){
			isSortOrderApplied = (thirdProductPrice>secondProductPrice?(secondProductPrice>firstProductPrice?true:false):false);
		}
		return isSortOrderApplied;
	}

	public double getPriceOfRandomProductAfterPriceFilterApplied(){
		int randomValue;
		driver.waitForElementPresent(ALL_PRODUCTS_DISPLAYED_ON_PAGE);
		List<WebElement> allProductsDisplayedOnPageList = driver.findElements(ALL_PRODUCTS_DISPLAYED_ON_PAGE);
		randomValue = CommonUtils.getRandomNum(1, allProductsDisplayedOnPageList.size());
		//System.out.println(("Random price of "+randomValue+" product is "+driver.findElement(By.xpath(String.format(RandomProductPrice, randomValue))).getText().replaceAll("[^0-9.]", "")));
		Double randomProductPrice = Double.parseDouble(driver.findElement(By.xpath(String.format(RandomProductPrice, randomValue))).getText().replaceAll("[^0-9.]", ""));
		System.out.println("Random product price in double format is  "+randomProductPrice);
		return randomProductPrice;
	}

	public DSVStoreFrontAutoshipCartPage clickAddToPCPerksForFirstProduct(){
		driver.quickWaitForElementPresent(FIRST_PRODUCT_ADD_TO_PC_PERKS_BTN);
		driver.click(FIRST_PRODUCT_ADD_TO_PC_PERKS_BTN);
		driver.waitForLoadingImageToDisappear();
		return new DSVStoreFrontAutoshipCartPage(driver);
	}

	public DSVStoreFrontAutoshipCartPage clickAddToPCPerksForSecondProduct(){
		driver.quickWaitForElementPresent(SECOND_PRODUCT_ADD_TO_PC_PERKS_BTN);
		driver.click(SECOND_PRODUCT_ADD_TO_PC_PERKS_BTN);
		driver.waitForLoadingImageToDisappear();
		return new DSVStoreFrontAutoshipCartPage(driver);
	}

}
