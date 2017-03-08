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
	private final By ORDER_TOTAL_LOC = By.xpath("//td[contains(text(),'Order Total')]/following::td[1]");
	private final By CHECKOUT_BTN_LOC = By.xpath("//div[@class='cart-container']/descendant::button[contains(text(),'Checkout')][2]");
	private final By FIRST_ITEM_CODE_LOC = By.xpath("//ul[contains(@class,'cart__list')]/descendant::li[@class='item-list-item'][1]//div[@class='item-code']");
	private final By FIRST_ITEM_PRODUCT_NAME_LOC = By.xpath("//ul[contains(@class,'cart__list')]/descendant::li[@class='item-list-item'][1]//span[@class='item-name']");
	private final By DELIVERY_LOC = By.xpath("//td[text()='Delivery:']/following::td[1]");
	private final By TOTAL_NO_OF_ITEMS_IN_CART_LOC = By.xpath("//ul[contains(@class,'cart__list')]/descendant::li[@class='item-list-item']");
	private final By CHECKOUT_CONFIRMATION_MSG_LOC=By.xpath("//div[@id='cartCheckoutModal']/p");	
	private final By SHOPPING_CART_TOTAL_PRODUCTS_LOC = By.xpath("//h1[contains(text(),'Your Shopping Cart')]/span");
	private final By CART_LOGIN_LOC = By.xpath("//div[@class='cartLogin']//a[text()='Log in']");
	private final By SHIPPING_METHOD_AFTER_ORDER_PLACED = By.xpath("//div[contains(text(),'Shipping Method')]");
	private final By FIRST_ITEM_PRODUCT_PRICE_LOC = By.xpath("//ul[contains(@class,'cart__list')]/descendant::li[@class='item-list-item'][1]//div[@class='item-price'][1]");
	private final By CHECKOUT_BTN_CONSULTANT_LOC = By.id("checkoutPopup");
	private final By CLOSE_BTN_ON_CONFIRMATION_POPUP_LOC = By.xpath("//button[@id='cboxClose']");
	private final By LEARN_MORE_LINK_LOC = By.xpath("//a[contains(text(),'Learn more')]");
	private final By PC_PERKS_PROMOTION_OVERLAY_CLOSE_BTN_LOC = By.xpath("//button[@id='cboxClose']");
	private final By PROMOTION_OVERLAY_CONTENT_LOC = By.xpath("//div[@id='cboxContent']//div[@class='content']");
	private final By PC_PERKS_PROMOTION_POPUP_LOC = By.xpath("//div[@id='cboxContent']");

	private String productPriceInAllItemsInCartLoc = "//li[@class='item-list-item']//div[@class='item-info']//span[@class='item-name' and contains(text(),'%s')]/ancestor::div[1]/following-sibling::div[@class='item-price-info']";
	private String recentlyViewProductOnCartPageLoc = "//div[@id='recentlyViewedTitle']/following::div[@class='owl-item active']//a[contains(text(),'%s')]";
	private String removeLinkForProductOnCartLoc = "//button[@id='removeEntry_%s']";
	private String productNameInCartLoc = "//ul[contains(@class,'cart__list')]/descendant::li[@class='item-list-item'][%s]//span[@class='item-name']";

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
		driver.clickByJS(RFWebsiteDriver.driver, By.xpath(String.format(recentlyViewProductOnCartPageLoc, productName)));
		logger.info("Product"+productName+"is clicked under recently view on cart page");
		return new StoreFrontProductDetailPage(driver);
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
		while(true){
			if(driver.isElementPresent(By.xpath(String.format(removeLinkForProductOnCartLoc, "0")))){
				driver.click(By.xpath(String.format(removeLinkForProductOnCartLoc, "0")));
				logger.info("Remove link clicked");
			}else{
				break;
			}
		}
		return this;
	}

	/***
	 * This method click on the cart login button
	 * 
	 * @param
	 * @return StoreFrontCartPage object
	 */
	public StoreFrontCartPage clickOnCartLoginLink(){
		driver.click(CART_LOGIN_LOC);
		logger.info("Clicked on login link from top navigation");
		return this;
	}

	/***
	 * This method get the price of product for a product present in cart
	 * 
	 * @param String prodName
	 * @return String price
	 */
	public String getPriceOfProductFromAllItemsInCart(String productName){
		String price = driver.getText(By.xpath(String.format(productPriceInAllItemsInCartLoc,productName))).replace("$","").trim();
		return price;
	}

	/***
	 * This method validates the presence of spefic price in cart
	 * 
	 * @param String expected price , String actual price
	 * @return boolean
	 */
	public boolean isProductPriceUpdatedInCartAsPerUser(String specificPrice, String priceOnCart){
		return priceOnCart.contains(specificPrice);
	}

	/***
	 * This method get the shipping Profile on Confirmation Page 
	 * 
	 * @param 
	 * @return Shipping Profile Name
	 * 
	 */
	public String getShippingProfileFromConfirmationPage(){
		String profileNameAtConfirmation=driver.findElement(By.xpath("//div[@class='orderShippingAddress']//li[1]")).getText();
		logger.info("Shipping Profile name at Confimation Page : "+profileNameAtConfirmation);
		return profileNameAtConfirmation;
	}

	/***
	 * This method get the shipping method after Successful Checkout 
	 * 
	 * @param 
	 * @return Shipping method
	 * 
	 */
	public String getShippingMethodAfterPlacedOrder(){
		String shippingMethod = driver.getText(SHIPPING_METHOD_AFTER_ORDER_PLACED).replaceAll("[^-?0-9]+","");
		logger.info("Shipping method at order confirmation page : "+shippingMethod);
		return shippingMethod;
	}

	/***
	 * This method get the Last four Digits of Credit Card from Confirmation Page 
	 * 
	 * @param 
	 * @return 4 Digits
	 * 
	 */

	public String getLastFourNumbersOfBillingDetailsOnConFirmationPage(){
		String ccNumber=driver.findElement(By.xpath("//div[@class='orderBillingDetails']/div[2]")).getText();
		String lastFourNumbers=ccNumber.substring(ccNumber.lastIndexOf(' ') + 1);
		logger.info("Last Four Numbers of Billing Profile at Confimation Page : "+lastFourNumbers);
		return lastFourNumbers;
	}

	/***
	 * This method get the Last four Digits of Credit Card from Confirmation Page 
	 * 
	 * @param 
	 * @return 4 Digits
	 * 
	 */
	public String getTotalChargeOnConFirmationPage(){
		String charge=driver.findElement(By.xpath("//div[@class='orderBillingDetails']/div[2]")).getText();
		String totalCharge=charge.substring(charge.lastIndexOf('$') + 1);
		logger.info("Total Charge with Billing Information at Confimation Page : "+totalCharge);
		return totalCharge;
	}

	/***
	 * This method get Product Price
	 * 
	 * @param 
	 * @return orderTotal
	 * 
	 */
	public String getProductPrice(String itemNumber){
		String productPrice = null;
		if(itemNumber.equalsIgnoreCase("1")){
			productPrice = driver.findElement(FIRST_ITEM_PRODUCT_PRICE_LOC).getText();
		}
		logger.info("product price of "+itemNumber+" is "+productPrice);
		return productPrice;
	}

	/***
	 * This method click checkout button 
	 * 
	 * @param
	 * @return store front checkout page object
	 */
	public StoreFrontCheckoutPage clickCheckoutBtn(){		
		try{
			driver.turnOffImplicitWaits(3);
			driver.waitForElementPresent(CHECKOUT_BTN_LOC,10);
			driver.clickByJS(RFWebsiteDriver.driver,CHECKOUT_BTN_LOC);
			logger.info("Checkout btn clicked");
		}
		catch(Exception e){
			driver.click(CHECKOUT_BTN_CONSULTANT_LOC);
			logger.info("Checkout btn for consultant clicked");
		}finally{
			driver.turnOnImplicitWaits();
		}
		driver.waitForPageLoad();
		return new StoreFrontCheckoutPage(driver);
	}

	/***
	 * This method click the checkout button at cart page
	 * 
	 * @param
	 * @return store front cart page object
	 * 
	 */
	public StoreFrontCartPage clickCheckoutTheCartFromCartPage(){
		driver.pauseExecutionFor(2000);
		driver.waitForElementToBeClickable(CHECKOUT_BUTTON_LOC, 20);
		driver.clickByJS(RFWebsiteDriver.driver, CHECKOUT_BUTTON_LOC);
		logger.info("Clicked on checkout button");
		return this;
	}

	/***
	 * This method click on ok button on confirmation popup
	 * 
	 * @param
	 * @return store front checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickOkOnCheckoutConfirmationPopup(){
		driver.waitForElementToBeClickable(CHECKOUT_CONFIRMATION_OK_BUTTON_LOC, 20);
		driver.click(CHECKOUT_CONFIRMATION_OK_BUTTON_LOC);
		logger.info("Clicked on ok button on checkout confirmation pop up");
		return new StoreFrontCheckoutPage(driver);
	}

	/***
	 * This method click the close button of confirmation popup
	 * 
	 * @param
	 * @return store front cart page object
	 * 
	 */
	public StoreFrontCartPage clickCloseBtnOfCheckoutConfirmationPopup(){
		driver.click(CLOSE_BTN_ON_CONFIRMATION_POPUP_LOC);
		logger.info("Clicked on close button of confirmation poup");
		return this;
	}

	/***
	 * This method click Learn more Link at cart page
	 * 
	 * @param
	 * @return store front cart page object
	 * 
	 */
	public StoreFrontCartPage clickLearnMoreLink(){
		driver.click(LEARN_MORE_LINK_LOC);
		logger.info("Clicked on Learn more Link");
		return this;
	}

	/***
	 * This method click close btn of PC Perks Promotion overlay
	 * 
	 * @param
	 * @return store front cart page object
	 * 
	 */
	public StoreFrontCartPage clickCloseBtnOfPromotionOverlay(){
		driver.click(PC_PERKS_PROMOTION_OVERLAY_CLOSE_BTN_LOC);
		logger.info("Clicked on close btn of PC Perks Promotion overlay");
		return this;
	}

	/***
	 * This method get the Promotion overlay content
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String getPromotionOverlayContent(){
		driver.pauseExecutionFor(3000);
		return driver.getText(PROMOTION_OVERLAY_CONTENT_LOC);
	}

	/***
	 * This method verify the presence of PC Perks Promotion popup
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public boolean isPCPerksPromotionPopupPresent(){
		driver.pauseExecutionFor(2000);
		return driver.isElementVisible(PC_PERKS_PROMOTION_POPUP_LOC);
	}


	/***
	 * This method press Escape key through action class to dismiss the promotion overlay
	 * 
	 * @param 
	 * @return
	 * 
	 */
	public StoreFrontCartPage pressEscapeKeyForDismissingPromotionOverlay(){
		driver.pressEscapeKey();
		return this;
	}

	/***
	 * This method get Order Total
	 * 
	 * @param 
	 * @return orderTotal
	 * 
	 */
	public String getOrderTotal(){
		String orderTotal=driver.findElement(ORDER_TOTAL_LOC).getText();
		logger.info("Order total at Cart Page is "+orderTotal);
		return orderTotal;
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
		productName = driver.getText(By.xpath(String.format(productNameInCartLoc, itemNumber))).trim();
		logger.info("product name of "+itemNumber+" is "+productName);
		return productName;
	}
}

