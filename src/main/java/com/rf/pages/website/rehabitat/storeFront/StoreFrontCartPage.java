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

	private final By QUANTITY_OF_FIRST_PRODUCT_LOC = By.xpath("//div[@class='qty']//input[@id='quantity_0']");
	private final By UPDATE_LINK_OF_FIRST_PRODUCT_LOC = By.xpath("//div[@class='qty']/descendant::input[@value='update'][1]");
	private final By PC_ONE_TIME_FEE_MSG_LOC = By.xpath("//span[contains(text(),'PC PERKS ONE-TIME ENROLLMENT FEE')]");
	private final By ADD_MORE_ITEMS_BTN_LOC = By.xpath("//div[@class='cart-container']/descendant::button[contains(text(),'Add More Items')][2]"); 
	private final By CHECKOUT_BTN_LOC = By.xpath("//div[@class='cart-container']/descendant::button[contains(text(),'Checkout')][2]");

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
			productQuantity = driver.findElement(QUANTITY_OF_FIRST_PRODUCT_LOC).getAttribute("value");
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
			driver.type(QUANTITY_OF_FIRST_PRODUCT_LOC, quantity);
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
			driver.click(UPDATE_LINK_OF_FIRST_PRODUCT_LOC);
		}
		logger.info("Update link of "+itemNumber+" is clicked");
		return this;
	}

	/***
	 * This method verifies if the PC one time joining fee msg 
	 * is displayed or not
	 * @return
	 */
	public boolean isPcOneTimeFeeMsgDisplayed(){
		return driver.isElementVisible(PC_ONE_TIME_FEE_MSG_LOC);
	}

	/***
	 * This method clicks on the Add More Items button
	 * @return
	 */
	public void clickAddMoreItemsBtn(){
		driver.click(ADD_MORE_ITEMS_BTN_LOC);
		logger.info("clicked on add more items button");
	}

	/***
	 * This method clicks on the checkout Btn
	 * @return
	 */
	public void clickCheckoutBtn(){
		driver.click(CHECKOUT_BTN_LOC);
	}

}

