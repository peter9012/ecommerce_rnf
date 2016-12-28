package com.rf.pages.website.rehabitat.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

public class StoreFrontProductDetailPage extends StoreFrontWebsiteBasePage{

	public StoreFrontProductDetailPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontProductDetailPage.class.getName());
	
	private final By ADD_TO_CART_BUTTON_AT_PRODUCT_DETAIL_PAGE_LOC = By.id("addToCartButton");
	private final By PRODUCT_NAME_AT_PRODUCT_DETAIL_PAGE_LOC = By.xpath("//div[@class='name']/h1");
	private final By DESCRIPTION_TAB_UNDER_PRODUCT_DETAIL_LOC = By.xpath("//div[@id='pdp_page']//following::li/a[contains(text(),'Description')]");
	private final By USAGE_NOTES_TAB_UNDER_PRODUCT_DETAIL_LOC = By.xpath("//div[@id='pdp_page']//following::li/a[contains(text(),'Usage Notes')]");
	private final By INGREDIENTS_TAB_UNDER_PRODUCT_DETAIL_LOC = By.xpath("//div[@id='pdp_page']//following::li/a[contains(text(),'Ingredients')]");
	/***
	 * This method verify Add to cart button at product detail page
	 * 
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */
	
	public boolean isAddToCartButtonIsPresentAtProductDetailsPage(){
		return driver.isElementPresent(ADD_TO_CART_BUTTON_AT_PRODUCT_DETAIL_PAGE_LOC);
	}
	/***
	 * This method return  the product name on product detail page after clicking
	 * product name on all product page
	 * 
	 * @param
	 * @return Product name 
	 * 
	 */
	public String getProductNameFromProductDetailsPage(){
		String productName = driver.findElement(PRODUCT_NAME_AT_PRODUCT_DETAIL_PAGE_LOC).getText().trim();
		logger.info("product name from product detail page is "+productName);
		return productName;
	}
	/***
	 * This method verify Description tab under product detail on product detail page
	 * 
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */
	
	public boolean isDescriptionTabPresentAtProductDetailsPage(){
		return driver.isElementPresent(DESCRIPTION_TAB_UNDER_PRODUCT_DETAIL_LOC);
	}
	/***
	 * This method verify Usage Note tab under product detail on product detail page
	 * 
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */
	
	public boolean isUsageNoteTabPresentAtProductDetailsPage(){
		return driver.isElementPresent(USAGE_NOTES_TAB_UNDER_PRODUCT_DETAIL_LOC);
	}
	/***
	 * This method verify Description tab under product detail on product detail page
	 * 
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */
	
	public boolean isIngredientsTabPresentAtProductDetailsPage(){
		return driver.isElementPresent(INGREDIENTS_TAB_UNDER_PRODUCT_DETAIL_LOC);
	}
}
