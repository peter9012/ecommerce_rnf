package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;

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
	private final By FIRST_ITEM_CODE_LOC = By.xpath("//ul[contains(@class,'cart__list')]/descendant::li[@class='item-list-item'][1]//div[@class='item-code']");
	private final By FIRST_ITEM_PRODUCT_NAME_LOC = By.xpath("//ul[contains(@class,'cart__list')]/descendant::li[@class='item-list-item'][1]//span[@class='item-name']");
	private final By SUBTOTAL_LOC = By.xpath("//td[text()='Subtotal:']/following::td[1]");
	private final By DELIVERY_LOC = By.xpath("//td[text()='Delivery:']/following::td[1]");
	private final By TOTAL_NO_OF_ITEMS_IN_CART_LOC = By.xpath("//ul[contains(@class,'cart__list')]/descendant::li[@class='item-list-item']");
	private final By CHECKOUT_CONFIRMATION_MSG_LOC=By.xpath("//div[@id='cartCheckoutModal']/p");
	private final By PC_TERMS_AND_CONDITIONS_LINK_LOC = By.xpath("//a[contains(text(),'PC Perks Terms & Conditions')]");
	private final By SHOPPING_CART_TOTAL_PRODUCTS_LOC = By.xpath("//h1[contains(text(),'Your Shopping Cart')]/span");
	private final By CART_PRODUCT_LOC = By.xpath("//ul[contains(@class,'item-list cart')]/li[@class='item-list-item']");
	private final By ADD_MORE_ITEMS_BTN_PC_AUTOSHIP_CART_LOC = By.xpath("//div[@class='cart-container']/descendant::button[contains(text(),'Add More Items')]");

	private String recentlyViewProductOnCartPageLoc = "//div[@id='recentlyViewedTitle']/following::div[@class='owl-item active']//a[contains(text(),'%s')]";
	private String removeLinkForProductOnCartLoc = "removeEntry_";

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
		if(driver.isElementPresent(ADD_MORE_ITEMS_BTN_LOC)){
			driver.click(ADD_MORE_ITEMS_BTN_LOC);
		}
		else{
			driver.click(ADD_MORE_ITEMS_BTN_PC_AUTOSHIP_CART_LOC);
		}
		logger.info("clicked on add more items button");
		driver.waitForPageLoad();
	}

	/***
	 * This method click checkout button 
	 * 
	 * @param
	 * @return store front checkout page object
	 */
	public StoreFrontCheckoutPage clickCheckoutBtn(){
		driver.click(CHECKOUT_BTN_LOC);
		logger.info("Checkout button clicked after registration");
		return new StoreFrontCheckoutPage(driver);
	}

	/***
	 * This method get product item code 
	 * 
	 * @param itemNumber
	 * @return item code
	 * 
	 */
	public String getItemCode(String itemNumber){
		String itemCode = null;
		if(itemNumber.equalsIgnoreCase("1")){
			itemCode = driver.findElement(FIRST_ITEM_CODE_LOC).getText();
		}
		logger.info("Item code of "+itemNumber+" is "+itemCode);
		return itemCode;
	}

	/***
	 * This method get product name of first item 
	 * 
	 * @param itemNumber
	 * @return product name
	 * 
	 */
	public String getProductName(String itemNumber){
		String productName = null;
		if(itemNumber.equalsIgnoreCase("1")){
			productName = driver.findElement(FIRST_ITEM_PRODUCT_NAME_LOC).getText();
		}
		logger.info("product name of "+itemNumber+" is "+productName);
		return productName;
	}

	/***
	 * This method get get subtotal 
	 * 
	 * @param 
	 * @return subtotal
	 * 
	 */
	public String getSubtotalofItems(){
		String subtotal = driver.findElement(SUBTOTAL_LOC).getText();
		logger.info("Subtotal of product is "+subtotal);
		return subtotal;
	}

	/***
	 * This method get delivery charges
	 * 
	 * @param 
	 * @return delivery charges
	 * 
	 */
	public String getDeliveryCharges(){
		String deliveryCharges = driver.findElement(DELIVERY_LOC).getText();
		logger.info("Delivery charges of product is "+deliveryCharges);
		return deliveryCharges;
	}

	/***
	 * This method get total no of items
	 * 
	 * @param
	 * @return total no of items
	 * 
	 */
	public int getTotalNoOfItemsInCart(){
		int totalNoOfItems = driver.findElements(TOTAL_NO_OF_ITEMS_IN_CART_LOC).size(); 
		logger.info("Total no of products are: "+totalNoOfItems);
		return totalNoOfItems;
	}

	/***
	 * This method validate the text present on checkout popup
	 * 
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */
	public boolean isCheckoutConfirmationDisplayed(){
		driver.pauseExecutionFor(5000);
		return driver.isElementVisible(CHECKOUT_CONFIRMATION_MSG_LOC);
	}

	/***
	 * This method verify recently view product are present on cart page
	 * 
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */

	public boolean isRecentlyViewProductPresentOnCartPage(String productName){
		try{
			if(driver.findElement(By.xpath(String.format(recentlyViewProductOnCartPageLoc, productName))).isDisplayed()){
				return true;
			}
		}
		catch(NoSuchElementException e){
			logger.info("Product "+productName+" not present under recently viewed on cart page.");
		}
		return false;
	}

	/***
	 * This method click product name under recently view on cart page 
	 * 
	 * @param
	 * @return store front product detail page object
	 */
	public StoreFrontProductDetailPage clickProductUnderRecentlyView(String productName){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath(String.format(recentlyViewProductOnCartPageLoc, productName))));
		logger.info("Product"+productName+"is clicked under recently view on cart page");
		return new StoreFrontProductDetailPage(driver);
	}

	/***
	 * This method click pc terms and conditions link 
	 * 
	 * @param
	 * @return StoreFrontCartPage object
	 */
	public StoreFrontCartPage clickPCTermsAndConditionsLink(){
		driver.click(PC_TERMS_AND_CONDITIONS_LINK_LOC);
		logger.info("PC Terms and Conditions link Clicked");
		return this;
	}

	/***
	 * This method calculate subtotal as per quantity change 
	 * 
	 * @param itemNumber
	 * @return updated subtotal
	 * 
	 */
	public String getCalculatedSubtotalValueAfterUpdatingQuantity(String previousSubtotal,String previousQty, String updatedQty){
		previousSubtotal = previousSubtotal.split("\\$")[1];
		double prevSubtotal = Double.parseDouble(previousSubtotal);
		int prevQty = Integer.parseInt(previousQty);
		int updatedQuantity = Integer.parseInt(updatedQty);
		double individualPrice = (prevSubtotal/prevQty);
		String calculatedValue = Double.toString((individualPrice*updatedQuantity));
		calculatedValue = "$"+calculatedValue;
		logger.info("calculated subtotal is "+calculatedValue);
		return calculatedValue;
	}
	/***
	 * This method reduce product quantity by one. 
	 * 
	 * @param quantity
	 * @return Reduce quantity by 1
	 * 
	 */
	public String updateAndReduceProductQuantityByOne(String quantity){
		quantity = ""+(Integer.parseInt(quantity)-1);
		logger.info("Updated reduced quantity is "+quantity);
		return quantity;
	}
	/***
	 * This method verifies that there is no product present in cart 
	 * 
	 * @return
	 */
	public boolean isProductPresentInCart(){
		return driver.isElementVisible(CART_PRODUCT_LOC);
	}
	/***
	 * This method get number of products on adhoc cart s
	 * 
	 * @param 
	 * @return product count in adhoc cart.
	 * 
	 */
	public int getProductCountInAdhocCart(){
		int totalProducts=0;
		try{
			String ahhocProductCount = driver.findElement(SHOPPING_CART_TOTAL_PRODUCTS_LOC).getText();
			ahhocProductCount =ahhocProductCount.split("\\(")[1].split("ITEMS")[0].trim();
			totalProducts = Integer.parseInt(ahhocProductCount);
			logger.info("Products counts in adhoc cart is "+totalProducts);
		}
		catch(NoSuchElementException e){
			logger.info("No products are present in adhoc cart.");
		}
		return totalProducts;
	}
	/***
	 * This method removes all product from cart
	 * 
	 * @param itemNumber
	 * @return store front Cart page object 
	 * 
	 */
	public StoreFrontCartPage removeAllProductsFromCart(){
		int count = getProductCountInAdhocCart();
		if(count>0){
			for(int i=0;i<count;i++){
				String removalLink = removeLinkForProductOnCartLoc+"0";
				driver.click(By.id(removalLink));
				driver.waitForPageLoad();
				logger.info("Remove link of "+i+" is clicked");
			}
		}
		else{
			logger.info("There are no products in cart to remove.");
		}
		return this;
	}
}

