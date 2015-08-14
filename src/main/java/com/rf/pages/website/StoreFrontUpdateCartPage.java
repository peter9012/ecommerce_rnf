package com.rf.pages.website;

import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontUpdateCartPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontUpdateCartPage.class.getName());

	private final By UPDATE_CART_BTN_LOC = By.xpath("//input[@value='UPDATE CART']");
	private final By PAYMENT_BILLING_EDIT_BTN_LOC = By.xpath("//a[@class='editPayment']");
	private final By PAYMENT_PROFILE_NAME_LOC = By.xpath("//p[@id='selectedPaymentInfo']/span[1]");
	private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//img[@title='Rodan+Fields']");
	private final By ADD_NEW_SHIPPING_LINK_LOC = By.xpath("//a[@class='add-new-shipping-address']");
	private final By USE_THIS_SHIPPING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC = By.xpath("//div[@id='use-for-autoship']/div");
	private final By ADD_NEW_BILLING_CARD_NUMBER_LOC = By.id("card-nr");

	public StoreFrontUpdateCartPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void clickUpdateCartBtn() throws InterruptedException{
		driver.waitForElementPresent(UPDATE_CART_BTN_LOC);
		driver.click(UPDATE_CART_BTN_LOC);		
		Thread.sleep(5000);
		logger.info("Update cart button clicked "+UPDATE_CART_BTN_LOC);
	}

	public void clickOnEditPaymentBillingProfile(){
		driver.waitForElementPresent(PAYMENT_BILLING_EDIT_BTN_LOC);
		driver.findElement(PAYMENT_BILLING_EDIT_BTN_LOC).click();
	}

	public boolean isNewBillingProfileIsSelectedByDefault(String profileName){
		driver.waitForElementPresent(By.xpath("//ul[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button billtothis'][1]/input"));
		return driver.findElement(By.xpath("//ul[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button billtothis'][1]/input")).isSelected();
	}

	public String getNameOnPaymentProfile(){
		return driver.findElement(PAYMENT_PROFILE_NAME_LOC).getText();
	}

	public StoreFrontConsultantPage clickRodanAndFieldsLogo(){
		driver.waitForElementPresent(RODAN_AND_FIELDS_IMG_LOC);
		driver.findElement(RODAN_AND_FIELDS_IMG_LOC).click();
		logger.info("Rodan and Fields logo clicked");
		return new StoreFrontConsultantPage(driver);
	}

	public void clickAddNewBillingProfileLink() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Add a new billing profile')]"));
		driver.findElement(By.xpath("//a[contains(text(),'Add a new billing profile')]")).click();
		logger.info("Add New Billing Profile link clicked");
	}

	public void enterNewBillingCardNumber(String cardNumber){
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.xpath("//td[@id='credit-cards']"));		
		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		js.executeScript("$('#card-nr-masked').hide();$('#card-nr').show(); ", driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC));
		driver.pauseExecutionFor(2000);
		driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC).clear();
		driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC).sendKeys(cardNumber);
		logger.info("New Billing card number enterd as "+cardNumber);		
	}

	public void enterNewBillingNameOnCard(String nameOnCard){
		driver.waitForElementPresent(By.xpath("//input[@id='card-name']"));
		driver.findElement(By.xpath("//input[@id='card-name']")).clear();
		driver.findElement(By.xpath("//input[@id='card-name']")).sendKeys(nameOnCard);
	}

	public void selectNewBillingCardExpirationDate(){
		driver.findElement(By.xpath("//select[@id='expiryMonth']")).click();
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.findElement(By.xpath("//select[@id='expiryMonth']/option[10]")).click();
		driver.findElement(By.xpath("//select[@id='expiryYear']")).click();
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[10]"));
		driver.findElement(By.xpath("//select[@id='expiryYear']/option[10]")).click();
	}

	public void enterNewBillingSecurityCode(String securityCode){
		driver.type(By.id("security-code"), securityCode);
	}

	public void selectNewBillingCardAddress() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//*[@id='addressBookdropdown']"));
		driver.findElement(By.xpath("//*[@id='addressBookdropdown']")).click();
		driver.waitForElementPresent(By.xpath("//*[@id='addressBookdropdown']/option[1]"));
		driver.findElement(By.xpath("//*[@id='addressBookdropdown']/option[1]")).click();
		logger.info("New Billing card address selected");
	}

	public void selectUseThisBillingProfileFutureAutoshipChkbox(){
		driver.waitForElementPresent(By.xpath("//div[@id='use-for-autoship']/div"));
		driver.findElement(By.xpath("//div[@id='use-for-autoship']/div")).click();
		logger.info("checkbox for Use this billing profile future autoship selected");
	}

	public void clickOnSaveBillingProfile() throws InterruptedException{
		driver.waitForElementPresent(By.id("submitButton"));
		driver.click(By.id("submitButton"));
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(30000);
		logger.info("Save billing profile button clicked");
	}

	public boolean verifyNewAddressGetsAssociatedWithTheDefaultBillingProfile(String firstAddressLine){
		driver.waitForElementPresent(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::li[1]//p[1]"));
		return driver.findElement(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::li[1]//p[1]")).getText().contains(firstAddressLine);
	}

	public String getDefaultBillingProfileName(){
		return driver.findElement(By.xpath("//input[@name='bill-card' and @checked='checked']/ancestor::ul[@id='multiple-billing-profiles']//li[1]/p[1]/span[@class='font-bold']")).getText();
	}

	public void clickOnNextStepBtn() throws InterruptedException{
		driver.waitForElementPresent(By.id("clickNext"));
		driver.findElement(By.id("clickNext")).click();
		Thread.sleep(2000);
		logger.info("Next Step button clicked");
	}

	public void clickOnUpdateCartShippingNextStepBtn() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		Thread.sleep(5000);
		driver.waitForElementPresent(By.xpath("//input[@id='use_address']"));
		//action.moveToElement(driver.findElement(By.xpath("//input[@id='use_address']"))).doubleClick().build().perform();		
		action.moveToElement(driver.findElement(By.xpath("//input[@id='use_address']"))).click(driver.findElement(By.xpath("//input[@id='use_address']"))).build().perform();
		logger.info("Next button on shipping update cart clicked");
		Thread.sleep(2000);
	}

	public void clickOnUpdateCartShippingNextStepBtnDuringEnrollment() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		Thread.sleep(5000);
		driver.waitForElementPresent(By.xpath("//input[@class='use_address']"));
		//action.moveToElement(driver.findElement(By.xpath("//input[@id='use_address']"))).doubleClick().build().perform();		
		action.moveToElement(driver.findElement(By.xpath("//input[@class='use_address']"))).click(driver.findElement(By.xpath("//input[@class='use_address']"))).build().perform();
		logger.info("Next button on shipping update cart clicked");
		Thread.sleep(2000);
	}

	public void clickOnEditBillingProfile(String billingProfileName) throws InterruptedException{
		try{
			driver.waitForElementPresent(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"));
			driver.waitForElementToBeClickable(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"), 20);
			Thread.sleep(5000);
			driver.findElement(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a")).click();
		}catch(NoSuchElementException e){
			try{
				billingProfileName = WordUtils.uncapitalize(billingProfileName);
				//driver.waitForElementPresent(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"));
				driver.waitForElementToBeClickable(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"), 20);
				Thread.sleep(5000);
				driver.findElement(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a")).click();
			}catch(NoSuchElementException e1){
				billingProfileName = billingProfileName.toLowerCase(); 
				//driver.waitForElementPresent(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"));
				driver.waitForElementToBeClickable(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"), 20);
				Thread.sleep(5000);
				driver.findElement(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a")).click();
			}
		}
		Thread.sleep(4000);
		logger.info("Edit link for "+billingProfileName+"clicked");
	}

	public void clickOnDefaultBillingProfileEdit() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//ul[@id='multiple-billing-profiles']//input[@checked='checked']/preceding::p[1]/a"));
		driver.findElement(By.xpath("//ul[@id='multiple-billing-profiles']//input[@checked='checked']/preceding::p[1]/a")).click();
		Thread.sleep(4000);
	}

	public String selectAndGetShippingMethodName() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='start-shipping-method']//ul/li[1]//input"));
		if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[1]//input")).isSelected()==false){
			driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[1]//span")).click();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[1]//label")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[1]//label")).getText().split("-")[0];	
		}
		else if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[2]//input")).isSelected()==false){
			driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[2]//span")).click();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[2]//label")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[2]//label")).getText().split("-")[0];	
		}
		else if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[3]//input")).isSelected()==false){
			driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[3]//span")).click();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[3]//label")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[3]//label")).getText().split("-")[0];	
		}		
		return null;
	}

	public void clickOnEditShipping() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='checkout_summary_deliverymode_div']//a"));		
		driver.findElement(By.xpath("//div[@id='checkout_summary_deliverymode_div']//a")).click();
		logger.info("Edit Shipping link clicked "+"//div[@id='checkout_summary_deliverymode_div']//a");
		driver.pauseExecutionFor(3000);
	}

	public boolean isShippingAddressPresent(String name){
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/ul//span[@class='font-bold'][contains(text(),'"+name+"')]"));
			driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/ul//span[@class='font-bold'][contains(text(),'"+name+"')]"));
			return true;
		}
		catch(NoSuchElementException e){
			return false;
		}
	}

	public void enterNewShippingAddressName(String name){
		driver.waitForElementPresent(By.id("new-attention"));
		driver.findElement(By.id("new-attention")).clear();
		driver.findElement(By.id("new-attention")).sendKeys(name);
		logger.info("New Shipping Address name is "+name);
	}

	public void enterNewShippingAddressLine1(String addressLine1){
		driver.findElement(By.id("new-address-1")).sendKeys(addressLine1);
	}

	public void enterNewShippingAddressCity(String city){
		driver.findElement(By.id("townCity")).sendKeys(city);
	}

	public void selectNewShippingAddressState(){
		driver.findElement(By.xpath("//select[@id='state']")).click();
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
		driver.findElement(By.xpath("//select[@id='state']/option[2]")).click();
		logger.info("State/Province selected");
	}

	public void enterNewShippingAddressPostalCode(String postalCode){
		driver.findElement(By.id("postcode")).sendKeys(postalCode);
	}

	public void enterNewShippingAddressPhoneNumber(String phoneNumber){
		driver.findElement(By.id("phonenumber")).sendKeys(phoneNumber);
	}

	public void clickOnSaveShippingProfile() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='autoShipShippingAddr']"));
		driver.click(By.xpath("//input[@id='autoShipShippingAddr']"));
		Thread.sleep(5000);
		logger.info("Save shipping profile button clicked");
		try{
			Thread.sleep(2000);
			driver.waitForElementPresent(By.xpath("//input[@id='QAS_RefineBtn']"));
			driver.findElement(By.xpath("//input[@id='QAS_RefineBtn']")).click();
			Thread.sleep(5000);
			logger.info("Accept New shipping address button clicked");
		}catch(NoSuchElementException e){

		}
	}	

	public boolean verifyNewShippingAddressSelectedOnUpdateCart(String name){		
		try{
			driver.findElement(By.xpath("//div[@id='new-shipping-added']//span[@class='font-bold'][contains(text(),'"+name.toLowerCase()+"')]/ancestor::li[1]//input[@type='radio'][@checked='checked']"));
			return true;
		}catch(NoSuchElementException e){
			try{
				driver.findElement(By.xpath("//div[@id='new-shipping-added']//span[@class='font-bold'][contains(text(),'"+name+"')]/ancestor::li[1]//input[@type='radio'][@checked='checked']"));
				return true;
			}catch(NoSuchElementException e1){
				return false;
			}
		}

	}

	public void clickAddNewShippingProfileLink() throws InterruptedException{
		driver.waitForElementPresent(ADD_NEW_SHIPPING_LINK_LOC);
		driver.click(ADD_NEW_SHIPPING_LINK_LOC);
		Thread.sleep(2000);
		logger.info("Ads new shipping profile link clicked");
	}

	public void clickOnBuyNowButton() throws InterruptedException{
		//		try{
		//			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select"));
		//			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select")).click();
		//			Thread.sleep(2000);
		//			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select/option[2]")).click();
		//		}catch(NoSuchElementException e){
		//
		//		}
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Buy now']"));
		driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Buy now']")).click();
		logger.info("Buy Now button clicked");
	}

	public void clickOnAddToCRPButton() throws InterruptedException{
		//		try{
		//			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select"));
		//			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select")).click();
		//			Thread.sleep(2000);
		//			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select/option[2]")).click();
		//		}catch(NoSuchElementException e){
		//
		//		}
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
		driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']")).click();
		logger.info("Add to CRP button clicked");
	}

	public void clickOnAddToCRPButtonDuringEnrollment() throws InterruptedException{
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select"));
			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select/option[2]")).click();
		}catch(NoSuchElementException e){

		}
		driver.waitForElementPresent(By.xpath("//div[@id='quick-refine']/following::div[1]/div[2]/div[1]//input[@value='Add to crp']"));
		driver.findElement(By.xpath("//div[@id='quick-refine']/following::div[1]/div[2]/div[1]//input[@value='Add to crp']")).click();
		logger.info("Add to CRP button clicked");
	}

	public void clickOnCRPCheckout(){
		driver.waitForElementPresent(By.xpath("//input[@id='crpCheckoutButton']"));
		driver.findElement(By.xpath("//input[@id='crpCheckoutButton']")).click();
	}

	public void clickOnCheckoutButton(){
		driver.waitForElementPresent(By.xpath("//input[@value='checkout']"));
		driver.findElement(By.xpath("//input[@value='checkout']")).click();
		driver.waitForLoadingImageToDisappear();
		logger.info("checkout button clicked");
	}

	public boolean verifyCheckoutConfirmation(){
		try{
			driver.findElement(By.xpath("//div[@id='popup-review']/h2[contains(text(),'Checkout Confirmation')]"));
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public void clickOnConfirmationOK(){
		driver.waitForElementPresent(By.xpath("//input[@value='OK']"));
		driver.findElement(By.xpath("//input[@value='OK']")).click();
		logger.info("Confirmation OK button clicked");
	}

	public String getSubtotal(){
		driver.waitForElementPresent(By.xpath("//div[@class='checkout-module-content']//p[@id='module-subtotal'][1]/span[1]"));
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//p[@id='module-subtotal'][1]/span[1]")).getText();
	}

	public String getDeliveryCharges(){
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//p[@id='module-subtotal'][2]/span[1]")).getText();
	}

	public String getHandlingCharges(){
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//p[@id='module-handling'][1]/span[1]")).getText();
	}

	public String getTax(){
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//p[@id='module-hst'][1]/span[1]")).getText();
	}

	public String getTotal(){
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//p[@id='module-total'][1]/span[1]")).getText();
	}

	public String getTotalSV(){
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//p[@id='module-subtotal'][3]/span[1]")).getText();
	}

	public String getShippingMethod(){
		return driver.findElement(By.xpath("//select[@id='deliveryMode']/option[@selected='selected']")).getText();
	}

	public void clickOnShippingAddressNextStepBtn() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		driver.waitForLoadingImageToDisappear();
		driver.waitForElementPresent(By.xpath("//input[@id='saveShippingInfo']"));
		action.moveToElement(driver.findElement(By.xpath("//input[@id='saveShippingInfo']"))).click(driver.findElement(By.xpath("//input[@id='saveShippingInfo']"))).build().perform();
		logger.info("Next button on shipping address clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public String getSelectedBillingAddress(){
		driver.waitForElementPresent(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::li[1]/p[1]"));
		return driver.findElement(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::li[1]/p[1]")).getText();
	}

	public void clickOnBillingNextStepBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
		driver.findElement(By.xpath("//div[@id='payment-next-button']/input")).click();
		logger.info("Next button on billing profile clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnSubscribePulseTermsAndConditionsChkbox(){
		driver.waitForElementPresent(By.xpath("//input[@id='termsCheck']/.."));
		driver.findElement(By.xpath("//input[@id='termsCheck']/..")).click();
	}

	public void clickSubscribePulseBtn(){
		driver.findElement(By.xpath("//input[@value='Subscribe']")).click();
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public boolean verifyPulseOrderCreatedMsg(){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Your pulse order has been created')]"));
		try{
			driver.findElement(By.xpath("//span[contains(text(),'Your pulse order has been created')]"));
			return true;		
		}catch(Exception e){
			return false;
		}
	}

	public void clickBillingEditAfterSave(){
		driver.waitForElementPresent(By.xpath("//a[@id='editBillingInfo']"));
		driver.waitForElementToBeClickable(driver.findElement(By.xpath("//a[@id='editBillingInfo']")), 30);
		driver.findElement(By.xpath("//a[@id='editBillingInfo']")).click();
		logger.info("Clicked on edit Billing link");
	}

	public void clickPlaceOrderBtn()throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='placeOrderButton']"));
		driver.findElement(By.xpath("//input[@id='placeOrderButton']")).click();
		logger.info("Place order button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();		
	}

	public String getOrderNumberAfterPlaceOrder(){
		driver.waitForElementPresent(By.xpath("//div[@id='order-confirm']"));
		logger.info("Order Number after placing order is "+driver.findElement(By.xpath("//div[@id='order-confirm']")).getText().split(":")[1].trim());
		return driver.findElement(By.xpath("//div[@id='order-confirm']")).getText().split(":")[1].trim();
	}

	public void editShippingAddress() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/ul/li[1]/p[2]/a[text()='Edit']"));
		driver.waitForElementToBeClickable(driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/ul/li[1]/p[2]/a[text()='Edit']")), 15);
		driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/ul/li[1]/p[2]/a[text()='Edit']")).click();
		Thread.sleep(3000);
	}

	public void clickOnSaveShippingProfileAfterEdit() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveShippingAddreessId"));
		driver.findElement(By.id("saveShippingAddreessId")).click();
		Thread.sleep(40000); // env taking too long,will remove
	}

	public void clickOnSaveShippingProfileAfterEditDuringEnrollment() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveCrpShippingAddress"));
		driver.findElement(By.id("saveCrpShippingAddress")).click();
		Thread.sleep(60000);
	}

	public boolean isUseThisShippingProfileFutureAutoshipChkboxVisible(){
		return driver.IsElementVisible(driver.findElement(USE_THIS_SHIPPING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC));
	}

	public void enterNewBillingAddressName(String name){
		driver.waitForElementPresent(By.id("billingAddressattention"));
		driver.findElement(By.id("billingAddressattention")).sendKeys(name);
		logger.info("New Billing Address name is "+name);
	}

	public void enterNewBillingAddressLine1(String addressLine1){
		driver.findElement(By.id("billingAddressline1")).sendKeys(addressLine1);
	}

	public void enterNewBillingAddressCity(String city){
		driver.findElement(By.id("billingAddresstownCity")).sendKeys(city);
	}

	public void selectNewBillingAddressState() throws InterruptedException{
		Thread.sleep(3500);
		driver.findElement(By.xpath("//select[@id='addressState']")).click();
		driver.waitForElementPresent(By.xpath("//select[@id='addressState']/option[3]"));
		driver.findElement(By.xpath("//select[@id='addressState']/option[3]")).click();
		logger.info("State/Province selected");
	}

	public void enterNewBillingAddressPostalCode(String postalCode){
		driver.findElement(By.id("billingAddresspostcode")).sendKeys(postalCode);
	}

	public void enterNewBillingAddressPhoneNumber(String phoneNumber){
		driver.findElement(By.id("billingAddressPhonenumber")).sendKeys(phoneNumber);
	}

	public void clickAddANewAddressLink() throws InterruptedException {
		driver.waitForElementPresent(By.xpath("//a[@class='add-new-billing-address']"));
		Thread.sleep(5000);
		driver.findElement(By.xpath("//a[@class='add-new-billing-address']")).click();
	}

	public String clickOnNewShipToThisAddressRadioButtonAndReturnProfileName(){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/ul/li"));
		List<WebElement> allShippingAddresses = driver.findElements(By.xpath("//div[@id='multiple-addresses-summary']/ul/li"));
		for(int i =1;i<=allShippingAddresses.size();i++ ) {
			if(driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/ul/li["+i+"]//input[@type='radio']")).isSelected()==false){
				driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/ul/li["+i+"]//span[@class='radio-button shiptothis']")).click();
				logger.info("New Address Name is "+ driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/ul/li["+i+"]/p[1]/span[1]")).getText());
				return driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/ul/li["+i+"]/p[1]/span[1]")).getText();
			}
		}
		return null;
	}

	public void clickOnPaymentNextStepBtn() throws InterruptedException	{
		driver.waitForElementPresent(By.xpath("//input[@class='paymentnext']"));
		driver.findElement(By.xpath("//input[@class='paymentnext']")).click();
		logger.info("Next button on payment section clicked");
		Thread.sleep(3000);
	}

	public void clickOnEditDefaultBillingProfile(){
		driver.waitForElementPresent(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::li[1]//a[text()='Edit']"));
		driver.waitForElementToBeClickable(driver.findElement(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::li[1]//a[text()='Edit']")), 30);
		driver.findElement(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::li[1]//a[text()='Edit']")).click();
	}

	public void clickOnContinueWithoutSponsorLink() throws InterruptedException{
		Thread.sleep(3000);
		driver.waitForElementPresent(By.xpath("//a[@id='continue-no-sponsor']"));
		driver.findElement(By.xpath("//a[@id='continue-no-sponsor']")).click();
		Thread.sleep(5000); // taking time,will remove		
	}

	public void clickOnNextButtonAfterSelectingSponsor() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='saveAccountAddress']"));
		driver.findElement(By.xpath("//input[@id='saveAccountAddress']")).click();
		Thread.sleep(40000);
	}

	public void clickOnSetupCRPAccountBtn() throws InterruptedException{
		Thread.sleep(5000);
		driver.waitForElementPresent(By.xpath("//input[@class='positive right pad_right ']"));
		driver.findElement(By.xpath("//ul[@style='cursor: pointer;']/li[1]/div")).click();
		driver.findElement(By.xpath("//ul[@style='cursor: pointer;']/li[3]/div")).click();
		driver.findElement(By.xpath("//ul[@style='cursor: pointer;']/li[4]/div")).click();
		driver.findElement(By.xpath("//input[@class='positive right pad_right ']")).click();
		logger.info("Next button on billing profile clicked");
		Thread.sleep(3000);
	}

	public void clickOnEditForDefaultShippingAddress() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[contains(@name,'shipping')][@checked='checked']/ancestor::li[1]//a[text()='Edit']"));
		driver.findElement(By.xpath("//input[contains(@name,'shipping')][@checked='checked']/ancestor::li[1]//a[text()='Edit']")).click();
		Thread.sleep(3000);
	}

	public boolean verifyEditShippingAddressNameSlectedOnUpdateCart(String name){
		logger.info("Asserting Update Shipping Address from default selected");
		System.out.println("Address by Created**"+name+"**" );
		System.out.println("Address From UI**"+driver.findElement(By.xpath("//input[contains(@name,'shipping')][@checked='checked']/ancestor::li[1]/p[1]/span[1]")).getText()+"**" );
		if(driver.findElement(By.xpath("//input[contains(@name,'shipping')][@checked='checked']/ancestor::li[1]/p[1]/span[1]")).getText().contains(name)){
			return true;
		}
		return false;
	}

	public void clickOnNextStepButtonAfterEditingDefaultShipping() throws InterruptedException{
		Thread.sleep(5000);
		driver.waitForElementPresent(By.xpath("//input[@class='paymentnext']"));
		driver.findElement(By.xpath("//input[@class='paymentnext']")).click();
	}

	public WebElement waitForSaveShippingInfoBtn()	 {
		return  driver.waitForElementToBeClickable(By.xpath("//div[@id='payment-next-button']/input"), 15);
	}

	public WebElement waitForpaymentNextBtn()	 {
		return  driver.waitForElementToBeClickable(By.xpath("//div[@id='payment-next-button']/input"), 15);
	}



}
