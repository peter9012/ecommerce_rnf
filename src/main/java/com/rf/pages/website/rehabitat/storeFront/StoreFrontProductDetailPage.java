package com.rf.pages.website.rehabitat.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

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
	private final By CONTENT_UNDER_DESCRIPTION_TAB_LOC = By.xpath("//div[@id='pdp_page']//following::li/a[contains(text(),'Description')]//following::p[3]");
	private final By CONTENT_UNDER_USAGE_NOTE_TAB_LOC = By.xpath("//div[@class='productUsageNotesText']");
	private final By CONTENT_UNDER_INGREDIENTS_TAB_LOC = By.xpath("//div[@class='productIngredientsText']");
	private final By RECENTLY_VIEW_TAB_LOC = By.xpath("//div[@id='pdp_page']//following::li/a[contains(text(),'Recently Viewed')]");
	private final By FORWARD_ARROW_OF_CAROUSEL_ON_PRODUCT_DETAIL_LOC = By.xpath("//div[@id='pdp_page']//following::div[@class='owl-next']/span");

	private String productUnderRecentlyViewTabLoc = "//div[@id='pdp_page']//div[@class='content']//following::div[@class='owl-item active']//a[contains(text(),'%s')]";
	private String tabOnProductDetailPageLoc = "//div[@id='pdp_page']//following::li/a[contains(text(),'%s')]";

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

	/***
	 * This method verify content under Description tab on product detail page
	 * 
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */


	public boolean isContentUnderDescriptionTabPresentAtProductDetailsPage(){
		return driver.findElement(CONTENT_UNDER_DESCRIPTION_TAB_LOC).isDisplayed();
	}
	/***
	 * This method verify content under Usage note tab on product detail page
	 * 
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */

	public boolean isContentUnderUsageNoteTabPresentAtProductDetailsPage(){
		return driver.isElementPresent(CONTENT_UNDER_USAGE_NOTE_TAB_LOC);
	}
	/***
	 * This method verify content under Ingredients tab on product detail page
	 * 
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */

	public boolean isContentUnderIngredientsTabPresentAtProductDetailsPage(){
		return driver.isElementPresent(CONTENT_UNDER_INGREDIENTS_TAB_LOC);
	}
	/***
	 * This method verify Recently view tab on product detail page 
	 * 
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */

	public boolean isRecentlyViewTabPresentAtProductDetailsPage(){
		try{
			if(driver.findElement(RECENTLY_VIEW_TAB_LOC).isDisplayed()){
				return true;
			}
		}
		catch(NoSuchElementException e){
			logger.info("Recently view tab is not present.");
		}
		return false;
	}
	/***
	 * This method verify product present under Recently view tab on product detail page 
	 * 
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */

	public boolean isProductUnderRecentlyViewTabPresentAtProductDetailsPage(String productName){
		try{
			if(driver.findElement(By.xpath(String.format(productUnderRecentlyViewTabLoc, productName))).isDisplayed()){
				return true;
			}
		}
		catch(NoSuchElementException e){
			logger.info("Product "+productName+" not present under recently view tab.");
		}
		return false;
	}
	/***
	 * This method Add  the product to cart from product detail page after clicking
	 * add to cart button.
	 * 
	 * @param
	 * @return 
	 * 
	 */
	public StoreFrontProductDetailPage addProductToCartFromProductDetailPage(){
		String productName = driver.findElement(PRODUCT_NAME_AT_PRODUCT_DETAIL_PAGE_LOC).getText().trim();
		driver.quickWaitForElementPresent(ADD_TO_CART_BUTTON_AT_PRODUCT_DETAIL_PAGE_LOC);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(ADD_TO_CART_BUTTON_AT_PRODUCT_DETAIL_PAGE_LOC));
		logger.info("product "+productName+" is added to cart");
		return this;
	}
	/***
	 * This method click forward carousel arrow under recently viewed tab on product detail page 
	 * to view more product.
	 * 
	 * @param
	 * @return 
	 * 
	 */
	public StoreFrontProductDetailPage scrollCarouselToViewProductOnProductDetailPage(){
		driver.click(FORWARD_ARROW_OF_CAROUSEL_ON_PRODUCT_DETAIL_LOC);
		logger.info("Forward arrow on carousel clicked on product detail page to view more product.");
		return this;
	}

	/***
	 * This method click on tab Name mention in argument on product detail page
	 * 
	 * @param Tab name to  be clicked
	 * @return 
	 * 
	 */
	public StoreFrontProductDetailPage selectTabOnProductDetailPage(String tabName){
		driver.click(By.xpath(String.format(tabOnProductDetailPageLoc, tabName)));
		logger.info("Tab "+tabName+ " Clicked on product detail page");
		driver.pauseExecutionFor(2000);
		return this;
	}
}
