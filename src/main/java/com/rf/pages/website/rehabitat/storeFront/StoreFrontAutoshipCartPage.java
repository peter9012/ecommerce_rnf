package com.rf.pages.website.rehabitat.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

public class StoreFrontAutoshipCartPage extends StoreFrontWebsiteBasePage{

	public StoreFrontAutoshipCartPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontAutoshipCartPage.class.getName());

	private final By CLOSE_ICON_PULSE_OVERLAY_LOC = By.id("cboxClose");
	private final By LEARN_MORE_ABOUT_PULSE_LOC = By.xpath("//a[contains(text(),'Learn more about Pulse')]");
	private final By PULSE_POPUP_LOC = By.xpath("//h1[contains(text(),'Pulse Business Management')]");
	private final By PC_PERKS_CHECKOUT_LOC = By.xpath("//a[contains(text(),'PC Perks Checkout')]");
	private final By CRP_CHECKOUT_LOC = By.xpath("//a[contains(text(),'CRP Checkout')]");
	private final By NEXT_BILL_SHIP_DATE_ON_AUTOSHIP_CART_PAGE_LOC = By.xpath("//td[text()='Ship & Bill Date']/following::td[1]");
	private final By CRP_THRESHOLD_MSG_LOC = By.xpath("//div[@class='global-alerts']/div");
	private final By SUBSCRIBE_TO_PULSE_BTN_LOC = By.id("confirmsubmitsubs");

	private String removeLinkForProductInAutoshipCartLoc = "//span[contains(normalize-space(text()),'%s')]/following::input[@id='autoship_remove'][1]";
	private String qtyTBForProductInAutoshipCartLoc = "//span[contains(normalize-space(text()),'%s')]/following::input[@name='qty' and not(@disabled)][1]";
	private String updatedQtyLinkForProductInAutoshipCartLoc = "//span[contains(normalize-space(text()),'%s')]/following::input[@value='Update Qty'][1]";
	private String quantityOfSpecificItemInCartLoc = "//ul[contains(@class,'item-list')]/descendant::input[contains(@class,'quantity-input')][%s]";
	private String updateQuantityLinkForSpecificItemLoc  = "//ul[contains(@class,'item-list')]/descendant::input[contains(@value,'Update Qty')][%s]";
	private String productQuantityLoc = "//li[@class='item-list-item'][%s]//input[@name='qty']";
	private String productUpdateQuantityLinkLoc = "//li[@class='item-list-item'][%s]//input[@value='Update Qty']";
	private String removeLinkOfAnItemAtAutoshipCartLoc = "//li[@class='item-list-item'][%s]//input[@id='autoship_remove'] ";
	private String productQuantityInAutoshipCartLoc = "//span[@class='item-name' and contains(text(),'%s')]/following::input[@name='qty'][1]";
	private String socialMediaIconLoc = "//div[@class='container']//a[contains(@href,'%s')]";


	/***
	 * This method click on learn more about pulse link.
	 * 
	 * @param 
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipCartPage clickLearnMoreAboutPulse(){
		driver.click(LEARN_MORE_ABOUT_PULSE_LOC);
		logger.info("clicked on 'learn more about pulse' link");
		return this;
	}

	/***
	 * This method validates overlay and its content displayed when clicked on learn more about pulse link
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isPulsePopupPresentWithContent(){
		return driver.isElementPresent(PULSE_POPUP_LOC);

	}

	/***
	 * This method close pulse overlay
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipCartPage closePulsePopup(){
		driver.click(CLOSE_ICON_PULSE_OVERLAY_LOC);
		logger.info("clicked X of 'pulse overlay'");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method click on the pc perks checkout button
	 * 
	 * @param
	 * @return StoreFrontCheckoutPage object
	 */
	public StoreFrontCheckoutPage clickOnPCPerksCheckoutButton(){
		driver.click(PC_PERKS_CHECKOUT_LOC);
		logger.info("Clicked on pc perks checkout button");
		return new StoreFrontCheckoutPage(driver);
	}

	/***
	 * This method validates Items on autoship cart page.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isAutoshipItemsPresentOnCartPage(){
		return driver.isElementVisible(CART_PRODUCT_LOC);
	}

	/***
	 * This method get  bill and ship date from autship cart page.
	 * 
	 * @param
	 * @return String nextBillAndShipDate.
	 * 
	 */
	public String getBillAndShipDateFromAutoshipCartPage(){
		String nextBillShipDate = null;
		if(driver.isElementVisible(NEXT_BILL_SHIP_DATE_ON_AUTOSHIP_CART_PAGE_LOC)){
			nextBillShipDate=driver.findElement(NEXT_BILL_SHIP_DATE_ON_AUTOSHIP_CART_PAGE_LOC).getText();
			logger.info("Next bill and ship date on autoship cart page "+nextBillShipDate);
			return nextBillShipDate;
		}else{
			logger.info("No bill and ship date present for user on autoship cart page.");
			return nextBillShipDate;
		}
	}

	/***
	 * This method get total no of item from autoship cart
	 * 
	 * @param
	 * @return no of item
	 * 
	 */
	public int getTotalNumberOfItemsFromCart(){
		int noOfItem = driver.findElements(CART_PRODUCT_LOC).size(); 
		logger.info("total no of items are: "+noOfItem);
		return noOfItem;
	}

	/***
	 * This method get total no of product of an item from 
	 * autoship cart
	 * 
	 * @param item number
	 * @return no of product
	 * 
	 */
	public String getProductQuantityFromAutoshipCart(String itemNumber){
		String qty = driver.getAttribute(By.xpath(String.format(productQuantityLoc, itemNumber)), "value"); 
		logger.info("total no of quantity of"+itemNumber+"item number is "+qty);
		return qty;
	}

	/***
	 * This method enter the quantity at autoship cart
	 * 
	 * @param item number, quantity
	 * @return Store front autoship cart page obj
	 * 
	 */
	public StoreFrontAutoshipCartPage enterProductQuantityAtAutoshipCart(String itemNumber,String qty){
		driver.type(By.xpath(String.format(productQuantityLoc, itemNumber)), qty); 
		logger.info("Entered quantity as "+qty+"of "+itemNumber+" item number");
		return this;
	}

	/***
	 * This method click the update quantity link of an item at autoship cart
	 * 
	 * @param item number
	 * @return Store front autoship cart page obj
	 * 
	 */
	public StoreFrontAutoshipCartPage updateQuantityAtAutoshipCart(String itemNumber){
		driver.click(By.xpath(String.format(productUpdateQuantityLinkLoc, itemNumber))); 
		logger.info("Update quantity link clicked of "+itemNumber+" item number");
		return this;
	}

	/***
	 * This method click the remove link link of an item at autoship cart
	 * 
	 * @param item number
	 * @return Store front autoship cart page obj
	 * 
	 */
	public StoreFrontAutoshipCartPage removeAnItemFromAutoshipCart(String itemNumber){
		driver.click(By.xpath(String.format(removeLinkOfAnItemAtAutoshipCartLoc, itemNumber))); 
		logger.info(itemNumber+" item number removed");
		return this;
	}

	/***
	 * This method get product quantity for specific product
	 * 
	 * @param productName
	 * @return product quantity
	 * 
	 */
	public String getQuantityOfSpecificProductFromAutoshipCart(String productName){
		String productQty = driver.getAttribute(By.xpath(String.format(productQuantityInAutoshipCartLoc, productName)),"value");
		logger.info("Quantity of "+productName+" is "+productQty);
		return productQty;
	}

	/***
	 * This method removes all product from autoship cart from and get threshold message
	 * 
	 * @param 
	 * @return threshold message 
	 * 
	 */
	public String getThresholdMessageWhileRemovingProductFromAutoshipCart(){
		int count = getTotalNumberOfItemsFromCart();
		if(count>0){
			for(int i=1;i<=count;i++){
				removeAnItemFromAutoshipCart("1");
				driver.waitForPageLoad();
				logger.info("Remove link of "+i+" is clicked");
				if(driver.isElementVisible(CRP_THRESHOLD_MSG_LOC)){
					logger.info("Threshole message is "+driver.getText(CRP_THRESHOLD_MSG_LOC));
					return driver.getText(CRP_THRESHOLD_MSG_LOC);
				}
				else
					continue;
			}
		}
		else{
			logger.info("There are no products in cart to remove.");
		}
		return null;
	}

	/***
	 * This method get product quantity for specific product on Autoship cart page
	 * 
	 * @param productName
	 * @return product quantity
	 * 
	 */
	public String getQuantityOfProductFromAutoshipCart(String itemNumber){
		String productQuantity = null;
		productQuantity = driver.getAttribute(By.xpath(String.format(quantityOfSpecificItemInCartLoc, itemNumber)),"value");
		logger.info("Quantity of " + itemNumber + " is " + productQuantity);
		return productQuantity;
	}

	/***
	 * This method enter product quantity
	 * 
	 * @param itemNumber, quantity
	 * @return store front Autoship cart page
	 * 
	 */
	public StoreFrontAutoshipCartPage updateQuantityOfItemInAutoshipCart(String itemNumber, String quantity) {
		driver.type(By.xpath(String.format(quantityOfSpecificItemInCartLoc, itemNumber)), quantity);
		logger.info("Qunatity updated as " + quantity + " for Item Number : " + itemNumber);
		return this;
	}

	/***
	 * This method click update link for the Quantity
	 * 
	 * @param itemNumber
	 * @return store front Autoship cart page
	 * 
	 */
	public StoreFrontAutoshipCartPage clickUpdateQuantityLink(String itemNumber) {
		driver.click(By.xpath(String.format(updateQuantityLinkForSpecificItemLoc, itemNumber)));
		logger.info("Clicked Update Quantity Link for Item Number : " + itemNumber);
		return this;
	}

	/***
	 * This method click on the CRP Checkout button
	 * 
	 * @param
	 * @return StoreFrontCheckoutPage object
	 */
	public StoreFrontCheckoutPage clickOnCRPCheckoutButton(){
		driver.clickByJS(RFWebsiteDriver.driver,CRP_CHECKOUT_LOC);
		logger.info("Clicked on CRP checkout button");
		return new StoreFrontCheckoutPage(driver);
	}

	/***
	 * This method get the expected threshold message for PC Autoship Cart
	 * 
	 * @param country
	 * @return String threshold msg
	 * 
	 */
	public String getExpectedThresholdMsgForPCAutoshipCart(String country){
		String thresholdMsg = null;
		if(country.equalsIgnoreCase("us")){
			thresholdMsg = "please add minimum worth of $80 products excluding enrollment fee";
		}
		else if(country.equalsIgnoreCase("ca")){
			thresholdMsg = "please add minimum worth of cad$90 products excluding enrollment fee";
		}
		else if(country.equalsIgnoreCase("au")){
			thresholdMsg = "please add minimum worth of aud$90 products excluding enrollment fee";
		}
		return thresholdMsg;
	}

	/***
	 * This method click on the remove link of Specific Product from AutoShip Cart 
	 * 
	 * @param String productName
	 * @return StorefrontAutoshipCartPage
	 * 
	 */
	public StoreFrontAutoshipCartPage removeProductFromAutoshipCart(String productName){
		driver.clickByJS(RFWebsiteDriver.driver,By.xpath(String.format(removeLinkForProductInAutoshipCartLoc,productName)));
		logger.info("Clicked on Remove link for Product : " + productName + " from AutoShip Cart");
		return this;

	}


	/***
	 * This method verify the presence of Specific Product in AutoShip Cart.
	 * 
	 * @param String productName
	 * @return boolean
	 * 
	 */
	public boolean isProductPresentInAutoShipCart(String productName){
		return driver.isElementVisible(By.xpath(String.format(updatedQtyLinkForProductInAutoshipCartLoc,productName)));

	}

	/***
	 * This method verify the presence of Specific Product in AutoShip Cart.
	 * 
	 * @param String productName
	 * @return StoreFrontAutoshipCartPage object
	 * 
	 */
	public StoreFrontAutoshipCartPage updateQtyForProductInAutoShipCart(String productName,String quantity){
		driver.type(By.xpath(String.format(qtyTBForProductInAutoshipCartLoc,productName)),quantity);
		logger.info("Entered Quantity for Product : " + productName + " is " + quantity);
		driver.clickByJS(RFWebsiteDriver.driver,By.xpath(String.format(updatedQtyLinkForProductInAutoshipCartLoc,productName)));
		logger.info("Clicked on Update Qty link for Product : " + productName);
		return this;
	}

	/***
	 * This method get the quantity of Specific Product in AutoShip Cart.
	 * 
	 * @param String productName
	 * @return String quantity
	 * 
	 */
	public String getQtyOfProductFromAutoShipCart(String productName){
		return driver.getAttribute(By.xpath(String.format(qtyTBForProductInAutoshipCartLoc,productName)),"value").trim();

	}

}