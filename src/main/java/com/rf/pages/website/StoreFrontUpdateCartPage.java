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
	}

	public void clickUpdateCartBtn() throws InterruptedException{
		driver.waitForElementPresent(UPDATE_CART_BTN_LOC);
		driver.click(UPDATE_CART_BTN_LOC);		
		logger.info("Update cart button clicked "+UPDATE_CART_BTN_LOC);
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnEditPaymentBillingProfile(){
		driver.waitForElementPresent(PAYMENT_BILLING_EDIT_BTN_LOC);
		driver.click(PAYMENT_BILLING_EDIT_BTN_LOC);
		driver.pauseExecutionFor(2000);
	}

	public boolean isNewBillingProfileIsSelectedByDefault(String profileName){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button billtothis'][1]/input"));
		return driver.findElement(By.xpath("//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button billtothis'][1]/input")).isSelected();
	}

	public boolean isNewBillingProfileIsSelectedByDefaultAfterClickOnEdit(String profileName){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button custom-checkbox billtothis'][1]/input"));
		return driver.findElement(By.xpath("//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button custom-checkbox billtothis'][1]/input")).isSelected();
	}

	public String getNameOnPaymentProfile(){
		return driver.findElement(PAYMENT_PROFILE_NAME_LOC).getText();
	}

	public StoreFrontConsultantPage clickRodanAndFieldsLogo(){
		driver.waitForElementPresent(RODAN_AND_FIELDS_IMG_LOC);
		driver.click(RODAN_AND_FIELDS_IMG_LOC);
		logger.info("Rodan and Fields logo clicked");
		return new StoreFrontConsultantPage(driver);
	}

	public void clickAddNewBillingProfileLink() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Add a new billing profile')]"));
		driver.pauseExecutionFor(2000);
		driver.click(By.xpath("//a[contains(text(),'Add a new billing profile')]"));
		logger.info("Add New Billing Profile link clicked");
	}

	public void enterNewBillingCardNumber(String cardNumber){
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.id("credit-cards"));		
		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		js.executeScript("$('#card-nr-masked').hide();$('#card-nr').show(); ", driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC));
		driver.pauseExecutionFor(2000);
		driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC).clear();
		driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC).sendKeys(cardNumber);
		logger.info("New Billing card number enterd as "+cardNumber);		
	}

	public void enterNewBillingNameOnCard(String nameOnCard){
		driver.waitForElementPresent(By.id("card-name"));
		driver.findElement(By.id("card-name")).clear();
		driver.findElement(By.id("card-name")).sendKeys(nameOnCard);
	}

	public void selectNewBillingCardExpirationDate(){
		driver.click(By.id("expiryMonth"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.click(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.click(By.id("expiryYear"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[10]"));
		driver.click(By.xpath("//select[@id='expiryYear']/option[10]"));
	}

	public void enterNewBillingSecurityCode(String securityCode){
		driver.type(By.id("security-code"), securityCode);
	}

	public void selectNewBillingCardAddress() throws InterruptedException{
		driver.waitForElementPresent(By.id("addressBookdropdown"));
		driver.click(By.id("addressBookdropdown"));
		driver.waitForElementPresent(By.xpath("//*[@id='addressBookdropdown']/option[1]"));
		driver.click(By.xpath("//*[@id='addressBookdropdown']/option[1]"));
		logger.info("New Billing card address selected");
	}

	public void selectUseThisBillingProfileFutureAutoshipChkbox(){
		driver.waitForElementPresent(By.xpath("//div[@id='use-for-autoship']/div"));
		driver.click(By.xpath("//div[@id='use-for-autoship']/div"));
		logger.info("checkbox for Use this billing profile future autoship selected");
	}

	public void clickOnSaveBillingProfile() throws InterruptedException{
		driver.waitForElementPresent(By.id("submitButton"));
		driver.click(By.id("submitButton"));
		driver.waitForLoadingImageToDisappear();
		logger.info("Save billing profile button clicked");
	}

	public boolean verifyNewAddressGetsAssociatedWithTheDefaultBillingProfile(String firstAddressLine){
		driver.waitForElementPresent(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::div[2]//p[1]"));
		return driver.findElement(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::div[2]//p[1]")).getText().contains(firstAddressLine);
	}

	public String getDefaultBillingProfileName(){
		return driver.findElement(By.xpath("//input[@name='bill-card' and @checked='checked']/ancestor::li[1]/p[1]/span[@class='font-bold']")).getText();
	}

	public void clickOnNextStepBtn() throws InterruptedException{
		driver.waitForElementPresent(By.id("clickNext"));
		driver.click(By.id("clickNext"));
		logger.info("Next Step button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnUpdateCartShippingNextStepBtn() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.id("use_address"));
		driver.pauseExecutionFor(2000);
		//action.moveToElement(driver.findElement(By.xpath("//input[@id='use_address']"))).double.build().perform();		
		action.moveToElement(driver.findElement(By.id("use_address"))).click(driver.findElement(By.id("use_address"))).build().perform();
		logger.info("Next button on shipping update cart clicked");
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public void clickOnUpdateCartShippingNextStepBtnDuringEnrollment() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@class='use_address']"));
		driver.click(By.xpath("//input[@class='use_address']"));
		logger.info("Next button on shipping update cart clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnEditBillingProfile(String billingProfileName) throws InterruptedException{
		try{
			driver.waitForElementPresent(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"));
			driver.waitForElementToBeClickable(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"), 20);
			driver.click(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"));
		}catch(NoSuchElementException e){
			try{
				billingProfileName = WordUtils.uncapitalize(billingProfileName);
				driver.waitForElementPresent(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"));
				driver.waitForElementToBeClickable(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"), 20);
				driver.click(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"));
			}catch(NoSuchElementException e1){
				billingProfileName = billingProfileName.toLowerCase(); 
				driver.waitForElementPresent(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"));
				driver.waitForElementToBeClickable(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"), 20);
				driver.click(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a"));
			}
		}		
		logger.info("Edit link for "+billingProfileName+"clicked");
	}

	public void clickOnDefaultBillingProfileEdit() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@checked='checked' and @name='bill-card']/preceding::p[1]/a"));
		driver.click(By.xpath("//input[@checked='checked' and @name='bill-card']/preceding::p[1]/a"));
		driver.waitForLoadingImageToDisappear();
	}

	public String selectAndGetShippingMethodName() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[1]//span"));
		if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[1]//span//input")).isSelected()==false){
			driver.click(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[1]//span"));
			driver.waitForLoadingImageToDisappear();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[1]//label")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[1]//label")).getText().split("-")[0]; 
		}
		else if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[2]//span//input")).isSelected()==false){
			driver.click(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[2]//span"));
			driver.waitForLoadingImageToDisappear();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[2]//label")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[2]//label")).getText().split("-")[0]; 
		}
		else if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[3]//span//input")).isSelected()==false){
			driver.click(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[3]//span"));
			driver.waitForLoadingImageToDisappear();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[3]//label")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[3]//label")).getText().split("-")[0]; 
		}  
		return null;
	}

	public void clickOnEditShipping() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='checkout_summary_deliverymode_div']//a"));		
		driver.click(By.xpath("//div[@id='checkout_summary_deliverymode_div']//a"));
		logger.info("Edit Shipping link clicked "+"//div[@id='checkout_summary_deliverymode_div']//a");		
	}

	public boolean isShippingAddressPresent(String name){
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div//span[@class='font-bold'][contains(text(),'"+name+"')]"));
			driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div//span[@class='font-bold'][contains(text(),'"+name+"')]"));
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
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
		driver.click(By.xpath("//select[@id='state']/option[2]"));
		logger.info("State/Province selected");
	}

	public void enterNewShippingAddressPostalCode(String postalCode){
		driver.findElement(By.id("postcode")).sendKeys(postalCode);
	}

	public void enterNewShippingAddressPhoneNumber(String phoneNumber){
		driver.findElement(By.id("phonenumber")).sendKeys(phoneNumber);
	}

	public void clickOnSaveShippingProfile() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveCrpShippingAddress"));
		driver.click(By.id("saveCrpShippingAddress"));
		logger.info("Save shipping profile button clicked");
		try{
			driver.waitForElementPresent(By.id("QAS_RefineBtn"));
			driver.click(By.id("QAS_RefineBtn"));
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
		logger.info("Ads new shipping profile link clicked");
	}

	public void clickOnBuyNowButton() throws InterruptedException{

		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			if(driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button")).isEnabled()==true)
				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			else
				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[2]//form[@id='productDetailForm']/button"));
			logger.info("Add To Bag button clicked");
			driver.waitForLoadingImageToDisappear();
		}
		else{
			driver.waitForElementPresent(By.xpath("//div[contains(@class,'quickshop-section blue')]/div[contains(@class,'quick-product-wrapper')]/div[1]//button"));
			if(driver.findElement(By.xpath("//div[contains(@class,'quickshop-section blue')]/div[contains(@class,'quick-product-wrapper')]/div[1]//button")).isEnabled()==true)
				driver.click(By.xpath("//div[contains(@class,'quickshop-section blue')]/div[contains(@class,'quick-product-wrapper')]/div[1]//button"));
			else
				driver.click(By.xpath("//div[contains(@class,'quickshop-section blue')]/div[contains(@class,'quick-product-wrapper')]/div[2]//button"));
			logger.info("Buy Now button clicked");
			driver.waitForLoadingImageToDisappear();
		}
		driver.waitForPageLoad();

	}

	public void clickOnAddToCRPButton() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
		if(driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']")).isEnabled()==true)
			driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
		else
			driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[2]//input[@value='Add to crp']"));
		logger.info("Add to CRP button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnAddToCRPButtonDuringEnrollment() throws InterruptedException{
		//  try{
		//   driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select"));
		//   driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select"));
		//   driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select/option[2]"));
		//   driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select/option[2]"));
		//  }catch(NoSuchElementException e){
		//
		//  }
		driver.waitForElementPresent(By.xpath("//div[@id='quick-refine']/following::div[1]/div[2]/div[2]//input[@value='Add to crp']"));
		driver.click(By.xpath("//div[@id='quick-refine']/following::div[1]/div[2]/div[2]//input[@value='Add to crp']"));
		logger.info("Add to CRP button clicked");
	}


	public void clickOnCRPCheckout(){
		driver.waitForElementPresent(By.xpath("//input[@id='quantity0']"));
		driver.findElement(By.xpath("//input[@id='quantity0']")).clear();
		driver.findElement(By.xpath("//input[@id='quantity0']")).sendKeys("5");
		driver.click(By.xpath("//form[@id='updateCartForm0']/a"));
		driver.waitForElementPresent(By.id("crpCheckoutButton"));
		driver.click(By.id("crpCheckoutButton"));
	}

	public void clickOnCheckoutButton(){
		driver.waitForElementPresent(By.xpath("//input[@value='PLACE ORDER']"));
		driver.click(By.xpath("//input[@value='PLACE ORDER']"));
		logger.info("checkout button clicked");
		try{
			driver.waitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
			logger.info("Confirmation OK button clicked");
			driver.waitForLoadingImageToDisappear();
			driver.waitForPageLoad();
		}
		catch(Exception e){

		}
	}

	//	public boolean verifyCheckoutConfirmation(){
	//		try{
	//			driver.findElement(By.xpath("//div[@id='popup-review']/h2[contains(text(),'Checkout Confirmation')]"));
	//			return true;
	//		}catch(NoSuchElementException e){
	//			return false;
	//		}
	//	}
	//
	//	public void clickOnConfirmationOK(){
	//		driver.waitForElementPresent(By.xpath("//input[@value='OK']"));
	//		driver.click(By.xpath("//input[@value='OK']"));
	//		logger.info("Confirmation OK button clicked");
	//		driver.waitForLoadingImageToDisappear();
	//		driver.waitForPageLoad();
	//	}

	public String getSubtotal(){
		driver.waitForElementPresent(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Subtotal')]/following::div[1]/span"));
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Subtotal')]/following::div[1]/span")).getText().trim();
	}

	public String getDeliveryCharges(){
		driver.waitForElementPresent(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Delivery')]/following::div[1]/span"));
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Delivery')]/following::div[1]/span")).getText().trim();
	}

	public String getHandlingCharges(){
		driver.waitForElementPresent(By.xpath("//div[@id='module-handling']//span"));
		return driver.findElement(By.xpath("//div[@id='module-handling']//span")).getText().trim();
	}

	public String getTax(){
		driver.waitForElementPresent(By.xpath("//div[@id='module-tax']//span[@class='taxRight']"));
		return driver.findElement(By.xpath("//div[@id='module-tax']//span[@class='taxRight']")).getText().trim();
	}

	public String getTotal(){
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[text()='Total']/following::div[1]/span")).getText().trim();
	}

	public String getTotalSV(){
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'SV')]/following::div[1]/span")).getText();
	}

	public String getShippingMethod(){
		return driver.findElement(By.xpath("//select[@id='deliveryMode']/option[@selected='selected']")).getText();
	}

	public void clickOnShippingAddressNextStepBtn() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveShippingInfo"));
		driver.click(By.id("saveShippingInfo"));
		logger.info("Next button on shipping address clicked");	
		driver.waitForLoadingImageToDisappear();		
	}

	public void clickOnNextStepBtnShippingAddress(){
		driver.waitForElementPresent(By.id("use_address"));
		driver.click(By.id("use_address"));
		logger.info("Next button on shipping address clicked"); 
		driver.waitForLoadingImageToDisappear();
	}

	public String getSelectedBillingAddress(){
		driver.waitForElementPresent(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::div[1]/preceding::p[3]"));
		return driver.findElement(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::div[1]/preceding::p[3]")).getText();
	}

	public void clickOnBillingNextStepBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
		driver.click(By.xpath("//div[@id='payment-next-button']/input"));
		logger.info("Next button on billing profile clicked");	
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnSubscribePulseTermsAndConditionsChkbox(){
		driver.waitForElementPresent(By.xpath("//input[@id='termsCheck']/.."));
		driver.click(By.xpath("//input[@id='termsCheck']/.."));
	}

	public void clickOnSubscribeBtn(){
		driver.waitForElementPresent(By.xpath("//input[@value='Subscribe']"));
		driver.click(By.xpath("//input[@value='Subscribe']"));
		driver.waitForLoadingImageToDisappear();
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
		driver.waitForElementPresent(By.id("editBillingInfo"));
		driver.waitForElementToBeClickable(driver.findElement(By.id("editBillingInfo")), 30);
		driver.click(By.id("editBillingInfo"));
		logger.info("Clicked on edit Billing link");
	}

	public void clickPlaceOrderBtn()throws InterruptedException{
		driver.waitForElementPresent(By.id("placeOrderButton"));
		driver.click(By.id("placeOrderButton"));
		logger.info("Place order button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();		
	}

	public String getOrderNumberAfterPlaceOrder(){
		driver.waitForElementPresent(By.id("order-confirm"));
		logger.info("Order Number after placing order is "+driver.findElement(By.id("order-confirm")).getText().split(":")[1].trim());
		return driver.findElement(By.xpath("//div[@id='order-confirm']")).getText().split(":")[1].trim();
	}

	public void editShippingAddress() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/ul/li[1]/p[2]/a[text()='Edit']"));
		driver.waitForElementToBeClickable(driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/ul/li[1]/p[2]/a[text()='Edit']")), 15);
		driver.click(By.xpath("//div[@id='multiple-addresses-summary']/ul/li[1]/p[2]/a[text()='Edit']"));
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnSaveShippingProfileAfterEdit() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveShippingAddreessId"));
		driver.click(By.id("saveShippingAddreessId"));	
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnSaveShippingProfileAfterEditDuringEnrollment() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveCrpShippingAddress"));
		driver.click(By.id("saveCrpShippingAddress"));		
	}

	public boolean isUseThisShippingProfileFutureAutoshipChkboxVisible(){
		driver.waitForElementPresent(USE_THIS_SHIPPING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC);
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
		driver.click(By.id("addressState"));
		driver.waitForElementPresent(By.xpath("//select[@id='addressState']/option[3]"));
		driver.click(By.xpath("//select[@id='addressState']/option[3]"));
		logger.info("State/Province selected");
	}

	public void enterNewBillingAddressPostalCode(String postalCode){
		driver.findElement(By.id("billingAddresspostcode")).sendKeys(postalCode+"\t");
		driver.waitForLoadingImageToDisappear();
	}

	public void enterNewBillingAddressPhoneNumber(String phoneNumber){
		driver.findElement(By.id("billingAddressPhonenumber")).sendKeys(phoneNumber);
	}

	public void clickAddANewAddressLink() throws InterruptedException {
		driver.waitForElementPresent(By.xpath("//a[@class='add-new-billing-address']"));
		driver.click(By.xpath("//a[@class='add-new-billing-address']"));
	}

	public String clickOnNewShipToThisAddressRadioButtonAndReturnProfileName(){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]"));
		List<WebElement> allShippingAddresses = driver.findElements(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]"));
		logger.info("Total shipping addresses listed are "+allShippingAddresses.size());
		driver.pauseExecutionFor(2000);
		for(int i =1;i<=allShippingAddresses.size();i++ ) {
			if(driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]["+i+"]//input[@type='radio']")).isSelected()==false){
				driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]["+i+"]//input[@type='radio']"));
				driver.click(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]["+i+"]//input[@type='radio']/.."));
				driver.waitForLoadingImageToDisappear();
				logger.info("New Address Name is "+ driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div["+i+"]/div[1]/div[1]")).getText());
				return driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div["+i+"]/div[1]/div[1]")).getText();
			}
		}
		return null;
	}

	public String clickOnNewShipToThisAddressRadioButtonAndReturnProfileNameUsingEditCRP(){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]"));
		List<WebElement> allShippingAddresses = driver.findElements(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]"));
		logger.info("Total shipping addresses listed are "+allShippingAddresses.size());
		driver.pauseExecutionFor(2000);
		if(allShippingAddresses.size()<2){
			logger.info("ONLY 1 shipping address was present on the cart");
			return driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div[1]/p/span[@class='font-bold']")).getText();
		}
		else{
			for(int i =1;i<=allShippingAddresses.size();i++ ) {
				if(driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]["+i+"]//input[@type='radio']")).isSelected()==false){
					driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]["+i+"]//input[@type='radio']"));
					driver.click(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]["+i+"]//input[@type='radio']/.."));
					driver.waitForLoadingImageToDisappear();
					logger.info("New Address Name is "+ driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div["+i+"]/p/span[@class='font-bold']")).getText());
					return driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div["+i+"]/p/span[@class='font-bold']")).getText();
				}

			}
		}

		return null;
	}

	public void clickOnPaymentNextStepBtn() throws InterruptedException	{
		driver.waitForElementPresent(By.xpath("//input[contains(@class,'paymentnext')]"));
		driver.click(By.xpath("//input[contains(@class,'paymentnext')]"));
		logger.info("Next button on payment section clicked");	
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnEditDefaultBillingProfile(){
		driver.waitForElementPresent(By.xpath("//input[@name='bill-card'][@checked='checked']/preceding::p[1]//a[text()='Edit']"));
		driver.waitForElementToBeClickable(driver.findElement(By.xpath("//input[@name='bill-card'][@checked='checked']/preceding::p[1]//a[text()='Edit']")), 30);
		driver.click(By.xpath("//input[@name='bill-card'][@checked='checked']/preceding::p[1]//a[text()='Edit']"));
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public void clickOnContinueWithoutSponsorLink() throws InterruptedException{
		try{
			driver.quickWaitForElementPresent(By.id("continue-no-sponsor"));
			driver.click(By.id("continue-no-sponsor"));				
		}catch(NoSuchElementException e){
			logger.info("Sponsor is already selected");
		}
	}

	public void clickOnNextButtonAfterSelectingSponsor() throws InterruptedException{
		driver.waitForLoadingImageToDisappear();
		driver.waitForElementPresent(By.id("saveAccountAddress"));
		driver.click(By.id("saveAccountAddress"));
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnSetupCRPAccountBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@value='Setup CRP Account']"));
		driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[1]/div"));
		driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[3]/div"));
		driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[4]/div"));
		driver.click(By.xpath("////input[@value='Setup CRP Account']"));
		logger.info("Next button on billing profile clicked");		
	}

	public void clickOnEditForDefaultShippingAddress() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[contains(@name,'shipping')][@checked='checked']/ancestor::div[contains(@class,'address-section')]//a[text()='Edit']"));
		driver.click(By.xpath("//input[contains(@name,'shipping')][@checked='checked']/ancestor::div[contains(@class,'address-section')]//a[text()='Edit']"));		
	}

	public boolean verifyEditShippingAddressNameSlectedOnUpdateCart(String name){
		logger.info("Asserting Update Shipping Address from default selected");
		if(driver.findElement(By.xpath("//input[contains(@name,'shipping')][@checked='checked']/ancestor::li[1]/p[1]/span[1]")).getText().contains(name)){
			return true;
		}
		return false;
	}

	public void clickOnNextStepButtonAfterEditingDefaultShipping() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@class='paymentnext']"));
		driver.click(By.xpath("//input[@class='paymentnext']"));
		driver.waitForLoadingImageToDisappear();
	}

	public WebElement waitForSaveShippingInfoBtn()	 {
		return  driver.waitForElementToBeClickable(By.xpath("//div[@id='payment-next-button']/input"), 15);
	}

	public boolean verifyOrderPlacedConfirmationMessage(){
		driver.waitForElementPresent(By.xpath("//div[@id='confirm-left-shopping']//h1"));
		String orderPlacedMessage = driver.findElement(By.xpath("//div[@id='confirm-left-shopping']//h1")).getText();
		System.out.println("Message from UI is  "+orderPlacedMessage);
		if(orderPlacedMessage.equalsIgnoreCase("Thank you for your order")){
			return true;
		}else{
			return false;
		}
	}

	public void selectShippingMethodUPSGroundInOrderSummary(){
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']"));
		driver.click(By.xpath("//select[@id='deliveryMode']"));
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']/option[1]"));
		driver.click(By.xpath("//select[@id='deliveryMode']/option[1]"));
		logger.info("UPS 2Day shipping method is selected");

	}

	public double getSubtotalValue(){
		driver.waitForElementPresent(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Subtotal')]/following::div[1]/span"));
		String value= driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Subtotal')]/following::div[1]/span")).getText().trim();
		String[] totalValue= value.split("\\s");
		double  subtotal = Double.parseDouble(totalValue[1]);
		logger.info("Subtotal Value fetched is "+subtotal);
		return subtotal;
	}

	public void changeCreditCardDate(){
		driver.click(By.id("expiryMonth"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[9]"));
		driver.click(By.xpath("//select[@id='expiryMonth']/option[9]"));
		driver.click(By.id("expiryYear"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[9]"));
		driver.click(By.xpath("//select[@id='expiryYear']/option[9]"));
	}

	public String getQuantityOfProductOnCartPage(){
		String quantityBeforeUpdate = driver.findElement(By.id("quantity0")).getAttribute("value");
		return quantityBeforeUpdate;
	}

	public String upgradeQuantityOfProduct(String quantityBeforeUpdate){
		int quantity = Integer.parseInt(quantityBeforeUpdate)+1;
		return Integer.toString(quantity);
	}

	public void clickOnUpdateMoreInfoButton(){
		driver.waitForElementPresent(By.xpath("//input[@value='Update more info']"));
		driver.click(By.xpath("//input[@value='Update more info']"));
	}

	//	public String getSubtotalFromCart(){
	//		driver.waitForElementPresent(By.xpath("//div[@id='module-subtotal'][2]//span"));
	//		return driver.findElement(By.xpath("//div[@id='module-subtotal'][2]//span")).getText();
	//	}
	//
	//	public String getDeliveyFromCart(){
	//		driver.waitForElementPresent(By.xpath("//div[@id='module-subtotal'][3]//span"));
	//		return driver.findElement(By.xpath("//div[@id='module-subtotal'][3]//span")).getText();
	//	}
	//	public String getHandlingFromCart(){
	//		driver.waitForElementPresent(By.xpath("//div[@id='module-handling']//span"));
	//		return driver.findElement(By.xpath("//div[@id='module-handling']//span")).getText();
	//	}
	//	public String getTaxFromCart(){
	//		driver.waitForElementPresent(By.xpath("//span[@class='taxRight']"));
	//		return driver.findElement(By.xpath("//span[@class='taxRight']")).getText();
	//	}
	//	public String getGrandTotalFromCart(){
	//		driver.waitForElementPresent(By.xpath("//div[@id='module-total']//span"));
	//		return driver.findElement(By.xpath("//div[@id='module-total']//span")).getText();
	//	}

	public String getSubtotalFromCart(){
		driver.waitForElementPresent(By.xpath("//div[contains(text(),'Subtotal')]/following::div[1]/span"));
		return driver.findElement(By.xpath("//div[contains(text(),'Subtotal')]/following::div[1]/span")).getText();
	}

	public String getDeliveyFromCart(){
		driver.waitForElementPresent(By.xpath("//div[contains(text(),'Subtotal')]/following::div[2]//span"));
		return driver.findElement(By.xpath("//div[contains(text(),'Subtotal')]/following::div[2]//span")).getText();
	}
	public String getHandlingFromCart(){
		driver.waitForElementPresent(By.xpath("//div[@id='module-handling']//span"));
		return driver.findElement(By.xpath("//div[@id='module-handling']//span")).getText();
	}
	public String getTaxFromCart(){
		driver.waitForElementPresent(By.xpath("//span[@class='taxRight']"));
		return driver.findElement(By.xpath("//span[@class='taxRight']")).getText();
	}
	public String getGrandTotalFromCart(){
		driver.waitForElementPresent(By.id("orderTotal"));
		return driver.findElement(By.id("orderTotal")).getText();
	}

	public boolean validateHeaderContent(){
		String headerContent="REVIEW AND UPDATE YOUR CART";
		driver.waitForElementPresent(By.xpath("//div[contains(text(),'REVIEW AND UPDATE')]"));
		return driver.findElement(By.xpath("//div[contains(text(),'REVIEW AND UPDATE')]")).getText().contains(headerContent);
	}

	public boolean validateCartUpdated(){
		driver.waitForElementPresent(By.xpath("//div[@id='globalMessages']"));
		return driver.isElementPresent(By.xpath("//div[@id='globalMessages']/img"));
	}

	public void selectShippingMethodUPS2DayInOrderSummary(){
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']"));
		driver.click(By.xpath("//select[@id='deliveryMode']"));
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']/option[2]"));
		driver.click(By.xpath("//select[@id='deliveryMode']/option[2]"));
		driver.waitForLoadingImageToDisappear();
		logger.info("UPS 2Day shipping method is selected");

	}

	public double getOrderTotal(){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			String value = driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[text()='Total']/following::div[1]/span")).getText().trim();
			String[] totalValue= value.split("\\s");
			double  orderTotal = Double.parseDouble(totalValue[1]);
			logger.info("Subtotal Value fetched is "+orderTotal);
			return orderTotal;
		} else if(driver.getCountry().equalsIgnoreCase("US")){
			String value = driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[text()='Total']/following::div[1]/span")).getText().trim();
			String fetchValue = value.substring(1);
			double  orderTotal = Double.parseDouble(fetchValue);
			logger.info("Subtotal Value fetched is "+orderTotal);
			return orderTotal;
		}
		return 0;

	}

	public void selectShippingMethod2Day(){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.waitForElementPresent(By.xpath("//div[@id='delivery_modes_dl']//label[contains(text(),'UPS 2Day')]/preceding-sibling::span"));
			driver.click(By.xpath("//div[@id='delivery_modes_dl']//label[contains(text(),'UPS 2Day')]/preceding-sibling::span"));
			logger.info("UPS 2Day shipping method is selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.waitForElementPresent(By.xpath("//div[@id='delivery_modes_dl']//label[contains(text(),'FedEx 2Day')]/preceding-sibling::span"));
			driver.click(By.xpath("//div[@id='delivery_modes_dl']//label[contains(text(),'FedEx 2Day')]/preceding-sibling::span"));
			logger.info("Fedex 2Day shipping method is selected");
		}
	}

	public void clickOnAutoShipButton(){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[1]/div[2]/div[2]//button"));
			if(driver.findElement(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[1]/div[2]/div[1]//button")).isEnabled()==true)
				driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[1]/div[2]/div[2]//button"));
			else
				driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[2]/div[2]/div[2]//button"));
			logger.info("Buy Now button clicked");
			driver.waitForLoadingImageToDisappear();
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[1]/div[2]/div[2]//button"));
			if(driver.findElement(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[1]/div[2]/div[2]//button")).isEnabled()==true){
				driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[1]/div[2]/div[2]//button"));
			}else{
				driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[2]/div[2]/div[2]//button"));
				logger.info("Buy Now button clicked");
				driver.waitForLoadingImageToDisappear();
			}
			try{
				driver.quickWaitForElementPresent(By.xpath("//div[@id='popup-special']/input[2]"));
				driver.click(By.xpath("//div[@id='popup-special']/input[2]"));
				driver.waitForLoadingImageToDisappear();
			}catch(Exception e){
				logger.info("No message popup displays");
			}

		}
	}

}
