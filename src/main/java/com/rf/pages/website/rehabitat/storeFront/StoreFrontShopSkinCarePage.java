package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontShopSkinCarePage extends StoreFrontWebsiteBasePage{
	public StoreFrontShopSkinCarePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontShopSkinCarePage.class.getName());

	private final By ADD_TO_BAG_BUTTON_LOC = By.xpath("//div[@class='product-item'][1]//button[text()='Add to Bag']");
	private final By CHECKOUT_BUTTON_POPUP_LOC = By.xpath("//a[contains(text(),'Checkout')]");
	private final By ADD_TO_CART_BUTTON_AT_PRODUCT_DETAIL_PAGE = By.id("addToCartButton");
	
	public StoreFrontShopSkinCarePage addFirstProductToBag(){
		driver.click(ADD_TO_BAG_BUTTON_LOC);
		logger.info("Added first product to the bag");
		return this;
	}

	public StoreFrontCartPage checkoutTheCartFromPopUp(){
		driver.click(CHECKOUT_BUTTON_POPUP_LOC);
		logger.info("Clicked on checkout button on the popup");
		return new StoreFrontCartPage(driver);
	}

	public void clickOnNameOfFirstProduct(){
		String productName = driver.findElement(PRODUCTS_NAME_LINK_LOC).getText();
		driver.click(PRODUCTS_NAME_LINK_LOC);
		logger.info("product name "+productName+ "Clicked");
	}

	public boolean isAddToCartButtonIsPresentAtProductDetailsPage(){
		return driver.isElementPresent(ADD_TO_CART_BUTTON_AT_PRODUCT_DETAIL_PAGE);
	}

}

