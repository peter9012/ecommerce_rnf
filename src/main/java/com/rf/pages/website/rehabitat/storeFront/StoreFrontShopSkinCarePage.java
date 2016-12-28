package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontShopSkinCarePage extends StoreFrontWebsiteBasePage{
	public StoreFrontShopSkinCarePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontShopSkinCarePage.class.getName());

	private final By CHECKOUT_BUTTON_POPUP_LOC = By.xpath("//a[contains(text(),'Checkout')]");
	private final By ADD_TO_CART_BUTTON_AT_PRODUCT_DETAIL_PAGE = By.id("addToCartButton");
	private final By ADD_TO_CART_FIRST_PRODUCT_LOC = By.xpath("//div[@id='product_listing']/descendant::button[text()='Add to cart'][1]");
	private final By ADD_TO_BAG_OF_FIRST_PRODUCT = By.xpath("//div[@id='product_listing']/descendant::span[contains(text(),'One Time Order')][1]");
	
	/**
	 * This method click on the checkOut Button on the popup on the cart.
	 * @return
	 */
	public StoreFrontCartPage checkoutTheCartFromPopUp(){
		driver.click(CHECKOUT_BUTTON_POPUP_LOC);
		logger.info("Clicked on checkout button on the popup");
		return new StoreFrontCartPage(driver);
	}

	public void clickNameOfFirstProduct(){
		String productName = driver.findElement(PRODUCTS_NAME_LINK_LOC).getText();
		driver.click(PRODUCTS_NAME_LINK_LOC);
		logger.info("product name "+productName+ "Clicked");
	}

	public boolean isAddToCartButtonIsPresentAtProductDetailsPage(){
		return driver.isElementPresent(ADD_TO_CART_BUTTON_AT_PRODUCT_DETAIL_PAGE);
	}
	
	/***
	 * This method click add to bag button for first product
	 * 
	 * @param
	 * @return store front shop skincare page object
	 * 
	 */
	public StoreFrontShopSkinCarePage selectFirstProduct(){
		driver.pauseExecutionFor(5000);
		driver.moveToElementByJS(ADD_TO_CART_FIRST_PRODUCT_LOC);
		driver.click(ADD_TO_BAG_OF_FIRST_PRODUCT);
		return this;
	}
}