package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class DSVStoreFrontQuickShopPage extends DSVRFWebsiteBasePage {

	private static final Logger logger = LogManager
			.getLogger(DSVStoreFrontQuickShopPage.class.getName());

	private static final By FIRST_PRODUCT_ADD_TO_CRP_BTN = By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']");
	private static final By FIRST_PRODUCT_NAME = By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]/h3"); 
	private static final By FIRST_PRODUCT_RETAIL_PRICE = By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//span[@class='old-price']");
	
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

}
