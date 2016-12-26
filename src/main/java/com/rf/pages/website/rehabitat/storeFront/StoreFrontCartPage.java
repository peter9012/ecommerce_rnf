package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontCartPage extends StoreFrontWebsiteBasePage{
	public StoreFrontCartPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontCartPage.class.getName());

	private final By QUANTITY_OF_FIRST_PRODUCT = By.xpath("//div[@class='qty']//input[@id='quantity_0']");
	private final By UPDATE_LINK_OF_FIRST_PRODUCT = By.xpath("//div[@class='qty']/descendant::input[@value='update'][1]");

	/***
	 * This method get product quantity 
	 * 
	 * @param itemNumber
	 * @return product quantity
	 * 
	 */
	public String getQuantityOfProductFromCart(String itemNumber){
		String productQuantity = null;
		if(itemNumber.equalsIgnoreCase("1")){
			productQuantity = driver.findElement(QUANTITY_OF_FIRST_PRODUCT).getAttribute("value");
		}
		logger.info("Quantity of "+itemNumber+" is "+productQuantity);
		return productQuantity;
	}

	/***
	 * This method get product quantity 
	 * 
	 * @param quantity
	 * @return updated quantity by 1
	 * 
	 */
	public String updateQuantityByOne(String quantity){
		quantity = ""+(Integer.parseInt(quantity)+1);
		logger.info("Updated quantity is "+quantity);
		return quantity;
	}

	/***
	 * This method enter product quantity 
	 * 
	 * @param itemNumber, quantity
	 * @return store front Cart page object 
	 * 
	 */
	public StoreFrontCartPage enterQuantityOfProductAtCart(String itemNumber, String quantity){
		if(itemNumber.equalsIgnoreCase("1")){
			driver.type(QUANTITY_OF_FIRST_PRODUCT, quantity);
		}
		logger.info("In cart"+itemNumber+" 's qunatity updated as "+quantity);
		return this;
	}

	/***
	 * This method click on update link 
	 * 
	 * @param itemNumber
	 * @return store front Cart page object 
	 * 
	 */
	public StoreFrontCartPage clickOnUpdateLinkThroughItemNumber(String itemNumber){
		if(itemNumber.equalsIgnoreCase("1")){
			driver.click(UPDATE_LINK_OF_FIRST_PRODUCT);
		}
		logger.info("Update link of "+itemNumber+" is clicked");
		return this;
	}

}

