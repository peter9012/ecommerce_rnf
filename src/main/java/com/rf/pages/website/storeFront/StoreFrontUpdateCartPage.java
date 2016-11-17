package com.rf.pages.website.storeFront;

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
import com.rf.core.website.constants.TestConstants;

public class StoreFrontUpdateCartPage extends StoreFrontRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontUpdateCartPage.class.getName());

	private final By PAYMENT_BILLING_EDIT_BTN_LOC = By.xpath("//a[@class='editPayment']");
	private final By PAYMENT_PROFILE_NAME_LOC = By.xpath("//div[@id='multiple-billing-profiles']/descendant::span[1]");
	private final By USE_THIS_SHIPPING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC = By.xpath("//div[@id='use-for-autoship']/div");
	private final By ADD_NEW_BILLING_CARD_NUMBER_LOC = By.id("card-nr");

	public StoreFrontUpdateCartPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public void clickOnEditPaymentBillingProfile(){
		driver.waitForElementPresent(PAYMENT_BILLING_EDIT_BTN_LOC);
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(PAYMENT_BILLING_EDIT_BTN_LOC));
		driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isNewBillingProfileIsSelectedByDefault(String profileName){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button billtothis'][1]/input"));
		return driver.findElement(By.xpath("//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button billtothis'][1]/input")).isSelected();
	}

	public boolean isNewBillingProfileIsSelectedByDefaultAfterClickOnEdit(String profileName){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button custom-checkbox billtothis'][1]/input"));
		return driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button custom-checkbox billtothis'][1]/input")).isSelected();
	}

	public String getNameOnPaymentProfile(){
		return driver.findElement(PAYMENT_PROFILE_NAME_LOC).getText();
	}

	public void enterNewBillingCardNumber(String cardNumber){
		//		driver.waitForPageLoad();
		driver.quickWaitForElementPresent(By.id("card-nr"));		
		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		js.executeScript("$('#card-nr-masked').hide();$('#card-nr').show(); ", driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC));
		driver.pauseExecutionFor(2000);
		driver.type(ADD_NEW_BILLING_CARD_NUMBER_LOC,cardNumber);
		logger.info("New Billing card number enterd as "+cardNumber);		
	}

	public void enterNewBillingNameOnCard(String nameOnCard){
		driver.waitForElementPresent(By.id("card-name"));		
		//		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		//		js.executeScript("$('#card-name-masked').hide();$('#card-name').show(); ", driver.findElement(By.id("card-name")));
		driver.pauseExecutionFor(2000);		
		driver.type(By.id("card-name"),nameOnCard);
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
		driver.waitForElementPresent(By.xpath("//input[@id='submitButton']"));
		driver.click(By.xpath("//input[@id='submitButton']"));
		driver.pauseExecutionFor(5000);		
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
		//driver.waitForElementPresent(By.id("clickNext"));
		driver.click(By.id("clickNext"));
		logger.info("Next Step button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnUpdateCartShippingNextStepBtn() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.id("use_address"));
		//driver.pauseExecutionFor(2000);
		//action.moveToElement(driver.findElement(By.xpath("//input[@id='use_address']"))).double.build().perform();		
		action.moveToElement(driver.findElement(By.id("use_address"))).click(driver.findElement(By.id("use_address"))).build().perform();
		logger.info("Next button on shipping update cart clicked");
		driver.waitForLoadingImageToDisappear();
		//driver.pauseExecutionFor(2000);
	}

	public void clickOnUpdateCartShippingNextStepBtnDuringEnrollment() throws InterruptedException{
		driver.isElementPresent(By.xpath("//div[@id='start-shipping-method']//input[@value='Next step']"));
		//driver.click(By.xpath("//div[@id='start-shipping-method']//input[@value='Next step']"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='start-shipping-method']//input[@value='Next step']")));
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

	public String selectAndGetShippingMethodName() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[1]//span"));
		if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[1]//span//input")).isSelected()==false){
			//driver.click(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[1]//span"));
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[1]//span")));
			driver.waitForLoadingImageToDisappear();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[1]//label")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[1]//label")).getText().split("-")[0].trim(); 
		}
		else if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[2]//span//input")).isSelected()==false){
			driver.click(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[2]//span"));
			driver.waitForLoadingImageToDisappear();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[2]//label")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[2]//label")).getText().split("-")[0].trim(); 
		}
		else if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[3]//span//input")).isSelected()==false){
			driver.click(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[3]//span"));
			driver.waitForLoadingImageToDisappear();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[3]//label")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//div[@class='row pb2']/div[3]//label")).getText().split("-")[0].trim(); 
		}  
		return null;
	}

	public boolean isShippingAddressPresent(String name){
		try{
			driver.quickWaitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div//span[@class='font-bold'][contains(text(),'"+name+"')]"));
			driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div//span[@class='font-bold'][contains(text(),'"+name+"')]"));
			return true;
		}
		catch(NoSuchElementException e){
			try{
				//driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div//div[contains(text(),'"+name+"')]"));
				driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div//div[contains(text(),'"+name+"')]"));
				return true;
			}catch(NoSuchElementException e1){
				return false;
			}
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

	public void clickOnBuyNowButton() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//*[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG') or @type='submit'][1]"));
			if(driver.findElement(By.xpath("//*[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG') or @type='submit'][1]")).isEnabled()==true)
				driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//*[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG') or @type='submit'][1]")));
			else
				driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//*[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG') or @type='submit'][2]")));
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

	public void clickOnBuyNowButton(String country) throws InterruptedException{

		if(country.equalsIgnoreCase("ca")){
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
				driver.click(By.xpath("//div[contains(@class,'quickshop-section blue')]/div[contains(@class,'quick-product-wrapper')]/div[1]/descendant::button[1]"));
			else
				driver.click(By.xpath("//div[contains(@class,'quickshop-section blue')]/div[contains(@class,'quick-product-wrapper')]/div[2]/descendant::button[1]"));
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
		driver.waitForElementPresent(By.xpath("//div[@id='quick-refine']/following::div[1]/div[2]/div[2]//input[@value='Add to crp']"));
		driver.click(By.xpath("//div[@id='quick-refine']/following::div[1]/div[2]/div[2]//input[@value='Add to crp']"));
		logger.info("Add to CRP button clicked");
	}


	public void clickOnCRPCheckout(){
		driver.waitForElementPresent(By.xpath("//input[@id='quantity0']"));
		driver.clear(By.xpath("//input[@id='quantity0']"));
		driver.type(By.xpath("//input[@id='quantity0']"),"5");
		driver.click(By.xpath("//form[@id='updateCartForm0']/a"));
		driver.waitForElementPresent(By.id("crpCheckoutButton"));
		driver.click(By.id("crpCheckoutButton"));
	}

	public void clickOnCheckoutButton(){
		driver.waitForElementPresent(By.xpath("//input[@value='CHECKOUT']"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@value='CHECKOUT']")));
		logger.info("checkout button clicked");
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
			logger.info("Confirmation OK button clicked");
			driver.waitForLoadingImageToDisappear();
			driver.waitForPageLoad();
		}
		catch(Exception e){

		}
	}

	public String getDeliveryCharges(){
		driver.waitForElementPresent(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Delivery')]/following::div[1]/span"));
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Delivery')]/following::div[1]/span")).getText().trim();
	}

	public String getHandlingCharges(){
		driver.waitForElementPresent(By.xpath("//div[@id='module-handling']//span"));
		return driver.findElement(By.xpath("//div[@id='module-handling']//span")).getText().trim();
	}

	public String getSubtotal(){
		driver.waitForElementPresent(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Subtotal')]/following::div[1]/span"));
		String subTotal= driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Subtotal')]/following::div[1]/span")).getText().trim();
		if(subTotal.contains(",")){
			subTotal = subTotal.replaceAll(",","");
		}
		return subTotal;
	}

	public String getTax(){
		driver.waitForElementPresent(By.xpath("//div[@id='module-tax']//span[@class='taxRight']"));
		String tax=driver.findElement(By.xpath("//div[@id='module-tax']//span[@class='taxRight']")).getText().trim();
		if(tax.contains(",")){
			tax = tax.replaceAll(",","");
		}
		return tax.trim();
	}

	public String getTotal(){
		String total =  driver.findElement(By.xpath("//div[@id='module-total'][1]//b[text()='Total']/following::div[1]/span")).getText().trim();
		if(total.contains(",")){
			total = total.replaceAll(",","");
		}
		return total;
	}

	public String getTotalSV(){
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'SV')]/following::div[1]/span")).getText();
	}

	public String getShippingMethod(){
		return driver.findElement(By.xpath("//select[@id='deliveryMode']/option[@selected='selected']")).getText();
	}

	public void clickOnShippingAddressNextStepBtn() throws InterruptedException{
		driver.pauseExecutionFor(3000);
		driver.waitForElementPresent(By.id("saveShippingInfo"));
		//driver.click(By.id("saveShippingInfo"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("saveShippingInfo")));
		logger.info("Next button on shipping address clicked"); 
		driver.waitForLoadingImageToDisappear();  
	}

	public void clickOnNextStepBtnShippingAddress(){
		driver.pauseExecutionFor(2000);
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
		//driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='payment-next-button']/input")));
		//driver.click(By.xpath("//div[@id='payment-next-button']/input"));
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
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("editBillingInfo")));
		//driver.click(By.id("editBillingInfo"));
		logger.info("Clicked on edit Billing link");
	}

	public void clickPlaceOrderBtn()throws InterruptedException{
		driver.waitForElementPresent(By.id("placeOrderButton"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("placeOrderButton")));
		//driver.click(By.id("placeOrderButton"));
		logger.info("Place order button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();  
	}

	public String getOrderNumberAfterPlaceOrder(){
		driver.waitForElementPresent(By.xpath("//div[@id='confirm-left-shopping']//h4"));
		logger.info("Order Number after placing order is "+driver.findElement(By.xpath("//div[@id='confirm-left-shopping']//h4")).getText().split("#")[1].trim());
		return driver.findElement(By.xpath("//div[@id='confirm-left-shopping']//h4")).getText().split("#")[1].trim();
	}

	public void editShippingAddress() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/ul/li[1]/p[2]/a[text()='Edit']"));
		driver.waitForElementToBeClickable(driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/ul/li[1]/p[2]/a[text()='Edit']")), 15);
		driver.click(By.xpath("//div[@id='multiple-addresses-summary']/ul/li[1]/p[2]/a[text()='Edit']"));
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnSaveShippingProfileAfterEdit() throws InterruptedException{
		//		driver.waitForElementPresent(By.id("saveShippingAddreessId"));
		driver.click(By.id("saveShippingAddreessId"));	
		driver.waitForLoadingImageToDisappear();
		try{			
			driver.quickWaitForElementPresent(By.xpath("//*[@id='QAS_RefineBtn']"));
			driver.click(By.xpath("//*[@id='QAS_RefineBtn']"));
			logger.info("QAS Popup clicked");
		}catch(Exception e){

		}
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
		driver.type(By.id("billingAddressattention"),name);
		logger.info("New Billing Address name is "+name);
	}

	public void enterNewBillingAddressLine1(String addressLine1){
		driver.type(By.id("billingAddressline1"),addressLine1);
	}

	public void enterNewBillingAddressCity(String city){
		driver.type(By.id("billingAddresstownCity"),city);
	}

	public void selectNewBillingAddressState() throws InterruptedException{
		driver.click(By.id("addressState"));
		driver.waitForElementPresent(By.xpath("//select[@id='addressState']/option[3]"));
		driver.click(By.xpath("//select[@id='addressState']/option[3]"));
		logger.info("State/Province selected");
	}

	public void enterNewBillingAddressPostalCode(String postalCode){
		driver.type(By.id("billingAddresspostcode"),postalCode+"\t");
		driver.waitForLoadingImageToDisappear();
	}

	public void enterNewBillingAddressPhoneNumber(String phoneNumber){
		driver.type(By.id("billingAddressPhonenumber"),phoneNumber);
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
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@name='bill-card'][@checked='checked']/preceding::p[1]//a[text()='Edit']")));
		//driver.click(By.xpath("//input[@name='bill-card'][@checked='checked']/preceding::p[1]//a[text()='Edit']"));
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
		//driver.click(By.id("saveAccountAddress"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("saveAccountAddress")));
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
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
		if(orderPlacedMessage.toLowerCase().contains("thank you")){
			return true;
		}else{
			return false;
		}
	}

	public void selectShippingMethodUPSGroundInOrderSummary(){
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']"));
		driver.click(By.xpath("//select[@id='deliveryMode']"));
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']/option[1]"));
		driver.click(By.xpath("//select[@id='deliveryMode']/option[contains(text(),'Ground')]"));
		logger.info("UPS 2Day shipping method is selected"); 
	}


	public double getSubtotalValue(){
		if(driver.getCountry().equalsIgnoreCase("ca")){
			driver.waitForElementPresent(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Subtotal')]/following::div[1]/span"));
			String value= driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Subtotal')]/following::div[1]/span")).getText().trim();
			String[] totalValue= value.split("\\s");
			String totValue = totalValue[1];
			if (totValue.contains(",")){
				String[] totalValueArray = totalValue[1].split(",");
				totValue = totalValueArray[0]+totalValueArray[1];
			}			
			double  subtotal = Double.parseDouble(totValue);
			logger.info("Subtotal Value fetched is "+subtotal);
			return subtotal;
		}else{
			driver.waitForElementPresent(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Subtotal')]/following::div[1]/span"));
			String value= driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Subtotal')]/following::div[1]/span")).getText().trim();
			String subtotal= value.replace('$', ' ');
			double Subtotal=Double.parseDouble(subtotal);
			logger.info("Subtotal Value fetched is "+subtotal);
			return Subtotal;
		}
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
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Update Shipping and Billing info')]"));
		//driver.click(By.xpath("//a[contains(text(),'Update Shipping and Billing info')]"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//a[contains(text(),'Update Shipping and Billing info')]")));
		driver.pauseExecutionFor(1000);
		driver.waitForPageLoad();
	}

	public String removeProductsFromCartForPC(int i){
		try{
			String SVValue = driver.findElement(By.xpath("//div[@class='cart-items']/div["+i+"]//p[@id='cart-price']/span[2]")).getText();
			driver.click(By.xpath("//div[@class='cart-items']/div["+i+"]//a[text()='Remove']"));
			logger.info("//div[@class='cart-items']/div["+i+"]//a[text()='Remove'] clicked");
			driver.waitForPageLoad();
			return SVValue;
		}catch (Exception e) {
			logger.info("No Remove option");
		}
		return null;
	}

	public boolean getValueOfFlagForPC(int i){
		try{
			String SVValue = driver.findElement(By.xpath("//div[@class='cart-items']/div["+i+"]//p[@id='cart-price']/span[2]")).getText().split("\\$")[1];
			System.out.println("Value of your price  "+SVValue);
			if(Double.parseDouble(SVValue)>0){
				return true;
			}
		}catch(Exception e){
		}
		return false;
	}

	public double compareSubtotalValue(String SVValueOfRemovedProduct, double totalSVValue){
		System.out.println("enter in compare method");
		String sv = SVValueOfRemovedProduct.split("\\$")[1];
		double SVValueOfRemoved = Double.parseDouble(sv);
		double finalSVValue = totalSVValue-SVValueOfRemoved;
		System.out.println("Final SV Value "+finalSVValue);
		return finalSVValue;

	}

	public String getDeliveyFromCart(){
		driver.waitForElementPresent(By.xpath("//div[contains(text(),'Subtotal')]/following::div[2]//span"));
		String delivery= driver.findElement(By.xpath("//div[contains(text(),'Subtotal')]/following::div[2]//span")).getText();
		if(delivery.contains(",")){
			delivery=delivery.replaceAll(",", "");
		}
		return delivery;
	}

	public String getSubtotalFromCart(){
		driver.waitForElementPresent(By.xpath("//div[contains(text(),'Subtotal')]/following::div[1]/span"));
		String subtotal = driver.findElement(By.xpath("//div[contains(text(),'Subtotal')]/following::div[1]/span")).getText();
		if(subtotal.contains(",")){
			subtotal=subtotal.replaceAll(",", "");
		}
		return subtotal;
	}

	public String getTaxFromCart(){
		driver.waitForElementPresent(By.xpath("//span[@class='taxRight']"));
		String tax= driver.findElement(By.xpath("//span[@class='taxRight']")).getText();
		if(tax.contains(",")){
			tax=tax.replaceAll(",", "");
		}
		return tax;
	}

	public String getGrandTotalFromCart(){
		driver.waitForElementPresent(By.id("orderTotal"));
		String grandTotal = driver.findElement(By.id("orderTotal")).getText();
		if(grandTotal.contains(",")){
			grandTotal=grandTotal.replaceAll(",", "");
		}
		return grandTotal;
	}

	public boolean validateHeaderContent(){
		String headerContent="REVIEW AND UPDATE YOUR CART";
		driver.waitForElementPresent(By.xpath("//div[contains(text(),'REVIEW AND UPDATE')]"));
		return driver.findElement(By.xpath("//div[contains(text(),'REVIEW AND UPDATE')]")).getText().contains(headerContent);
	}

	public boolean validateCartUpdated(){
		//driver.waitForElementPresent(By.xpath("//div[@id='globalMessages']"));
		return driver.isElementPresent(By.xpath("//div[@id='globalMessages']/img"));
	}

	public void selectShippingMethodUPS2DayInOrderSummary(){
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']"));
		driver.click(By.xpath("//select[@id='deliveryMode']"));
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']/option[contains(text(),'2Day')]"));
		driver.click(By.xpath("//select[@id='deliveryMode']/option[contains(text(),'2Day')]"));
		driver.waitForLoadingImageToDisappear();
		logger.info("UPS 2Day shipping method is selected");

	}

	public double getOrderTotal(){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			String value = driver.findElement(By.xpath("//div[@class='checkout-module-content']//b[text()='Total']/following::div[1]/span")).getText().trim();
			String[] totalValue= value.split("\\s");
			double  orderTotal = Double.parseDouble(totalValue[1]);
			logger.info("Subtotal Value fetched is "+orderTotal);
			return orderTotal;
		} else if(driver.getCountry().equalsIgnoreCase("US")){
			String value = driver.findElement(By.xpath("//div[@class='checkout-module-content']//b[text()='Total']/following::div[1]/span")).getText().trim();
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

	public String getTotalPriceOfProduct(){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			String value = driver.findElement(By.xpath("//div[@id='total-shopping'][2]//span")).getText().trim();
			return value;


		} else if(driver.getCountry().equalsIgnoreCase("US")){
			String value = driver.findElement(By.xpath("//div[@id='total-shopping'][2]//span")).getText().trim();
			return value;

		}
		return null;
	}

	public boolean checkDateFunctionality(){
		return driver.isElementPresent(By.xpath("//a[contains(text(),'Change next ship date')]"));
	}

	public void clickOnChangeNextShipDate(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Change next ship date')]"));
		driver.click(By.xpath("//a[contains(text(),'Change next ship date')]"));
	}

	public void clickOnDate(){
		driver.waitForElementPresent(By.xpath("//input[@id='todayDate']/.."));
		driver.click(By.xpath("//input[@id='todayDate']/.."));
	}

	public int getCountofDateFromCalendar(){
		driver.waitForElementPresent(By.xpath("//td[contains(@class,'undefined')]"));
		return driver.findElements(By.xpath("//td[contains(@class,'undefined')]")).size();

	}

	public boolean verifyEnabledDatesOfTheCalendar(int i){
		driver.waitForElementPresent(By.xpath("//td[contains(@class,'undefined')]//a[text()='"+i+"']"));
		return driver.findElement(By.xpath("//td[contains(@class,'undefined')]//a[text()='"+i+"']/..")).getAttribute("data-handler").contains("selectDay");
	}

	public boolean verifyDisabledDatesOfTheCalendar(int i){
		driver.waitForElementPresent(By.xpath("//td[contains(@class,'undefined')]//span[contains(text(),'"+i+"')]/.."));
		return driver.findElement(By.xpath("//td[contains(@class,'undefined')]//span[contains(text(),'"+i+"')]/..")).getAttribute("class").contains("unselectable ");
	}

	public void clickOnAddANewShippingAddress(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Add new shipping address')]"));
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(By.xpath("//a[contains(text(),'Add new shipping address')]")));
	}

	public void selectNewShippingAddressStateOnCartPage(){
		try{
			driver.quickWaitForElementPresent(By.xpath("//form[@id='deliveryAddressForm']//select[@id='state']"));
			driver.click(By.xpath("//form[@id='deliveryAddressForm']//select[@id='state']"));
			driver.quickWaitForElementPresent(By.xpath("//form[@id='deliveryAddressForm']//option[2]"));
			driver.click(By.xpath("//form[@id='deliveryAddressForm']//option[2]"));
		}catch(Exception e){
			driver.quickWaitForElementPresent(By.xpath("//form[@id='deliveryaddressForm']//select[@id='state']"));
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//form[@id='deliveryaddressForm']//select[@id='state']")));
			driver.quickWaitForElementPresent(By.xpath("//form[@id='deliveryaddressForm']//option[2]"));
			driver.click(By.xpath("//form[@id='deliveryaddressForm']//option[2]"));
		}
		logger.info("State/Province selected");
	}

	public boolean  verifyNewShippingAddressIsSelectedByDefaultOnAdhocCart(String name){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']//input[@checked='checked']/preceding::span[contains(text(),'"+name+"')]"));
		return driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']//input[@checked='checked']/preceding::span[contains(text(),'"+name+"')]")).getText().contains(name);
	}

	public void clickOnConfirmationOK(){
		//driver.waitForElementPresent(By.xpath("//input[@value='OK']"));
		driver.click(By.xpath("//input[@value='OK']"));
		logger.info("Confirmation OK button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public String userNameBeforeEdit(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']//div[@class='user-name col-xs-12']"));
		return driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']//div[@class='user-name col-xs-12']")).getText();
	}

	public String getUpdatedAddressPresentOnOrderConfirmationPage(){
		//driver.quickWaitForElementPresent(By.xpath("//div[@id='confirm-left-shopping']/div[2]//div[contains(@class,'ship-method')]/span[@class='font-bold']"));
		String shippingAddresName = driver.findElement(By.xpath("//div[@id='confirm-left-shopping']/div[2]//div[contains(@class,'ship-method')]/span[@class='font-bold']")).getText();
		return shippingAddresName.trim();
	}

	//	public void clickOnUseAsEnteredButton(){
	//		try{
	//			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
	//			driver.click(By.id("QAS_AcceptOriginal"));
	//		}catch(Exception e){
	//			logger.info("useAsEntered popUp not Present");
	//		}
	//		driver.waitForLoadingImageToDisappear();
	//	}

	public void addAshippingProfile(String city,String state,String addressLine,String profileName,String phoneNumber,String postalCode) throws InterruptedException{
		List<WebElement> totalShipingAddress = driver.findElements(By.xpath("//div[@id='multiple-addresses-summary']/div"));
		if(totalShipingAddress.size() <= 1){
			driver.waitForElementPresent(By.xpath("//a[contains(text(),'Add new shipping address')]"));
			driver.click(By.xpath("//a[contains(text(),'Add new shipping address')]"));
			logger.info("add a new shipping address clicked");
			enterNewShippingAddressName(profileName);
			enterNewShippingAddressLine1(addressLine);
			enterNewShippingAddressCity(city);
			super.selectNewShippingAddressState(state);
			enterNewShippingAddressPhoneNumber(phoneNumber);
			enterNewShippingAddressPostalCode(postalCode);
			clickOnSaveShippingProfile();
			logger.info("save address button clicked");
		}else{
			logger.info("It is having more than one shipping address");
			//List<WebElement> totalRadioButton = driver.findElements(By.xpath("//div[@id='multiple-addresses-summary']//span"));
		}

		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnEditOnNonDefaultAddress(){
		if(driver.isElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div[1]//input[@checked='checked']"))==false){
			driver.click(By.xpath("//div[@id='multiple-addresses-summary']/div[1]//a[contains(text(),'Edit')]"));
		}else{
			driver.click(By.xpath("//div[@id='multiple-addresses-summary']/div[2]//a[contains(text(),'Edit')]"));
		}
		driver.waitForLoadingImageToDisappear();
		//driver.pauseExecutionFor(5000);		
	}

	public void clickOnSaveShippingProfileOnUpdateCrpPage() throws InterruptedException{
		//driver.waitForElementPresent(By.id("saveCrpShippingAddress"));
		driver.click(By.id("saveCrpShippingAddress"));
		logger.info("Save shipping profile button clicked");
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.id("QAS_RefineBtn"));
			driver.click(By.id("QAS_RefineBtn"));
		}catch(Exception e){

		}
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyUpdatedAddressPresentUpdateCartPg(String profileNameSecond){
		driver.findElement(By.xpath("//div[@id='checkout_summary_deliverymode_div']/div[2]/p/span[1]"));
		String name = driver.findElement(By.xpath("//div[@id='checkout_summary_deliverymode_div']/div[2]/p/span[1]")).getText();
		if(name.equalsIgnoreCase(profileNameSecond)){
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyUpdatedShippingAddress(String address){
		try{
			driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div//span[contains(text(),'"+address+"')]"));
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public void clickOnEditBillingProfileDuringEnrollInCRP(String billingProfileName) throws InterruptedException{
		try{
			driver.pauseExecutionFor(3000);
			driver.waitForElementPresent(By.xpath("//span[text()='"+billingProfileName+"']/following::a[contains(text(),'Edit')][1]"));
			driver.waitForElementToBeClickable(By.xpath("//span[text()='"+billingProfileName+"']/following::a[contains(text(),'Edit')][1]"), 20);
			driver.click(By.xpath("//span[text()='"+billingProfileName+"']/following::a[contains(text(),'Edit')][1]"));
		}catch(NoSuchElementException e){
			try{
				billingProfileName = WordUtils.uncapitalize(billingProfileName);
				driver.waitForElementPresent(By.xpath("//span[text()='"+billingProfileName+"']/following::a[contains(text(),'Edit')][1]"));
				//driver.waitForElementToBeClickable(By.xpath("//span[text()='"+billingProfileName+"']/following::a[contains(text(),'Edit')][1]"), 20);
				driver.click(By.xpath("//span[text()='"+billingProfileName+"']/following::a[contains(text(),'Edit')][1]"));
			}catch(NoSuchElementException e1){
				billingProfileName = billingProfileName.toLowerCase(); 
				driver.waitForElementPresent(By.xpath("//span[text()='"+billingProfileName+"']/following::a[contains(text(),'Edit')][1]"));
				//driver.waitForElementToBeClickable(By.xpath("//span[text()='"+billingProfileName+"']/following::a[contains(text(),'Edit')][1]"), 20);
				driver.click(By.xpath("//span[text()='"+billingProfileName+"']/following::a[contains(text(),'Edit')][1]"));
			}
		}  
		logger.info("Edit link for "+billingProfileName+"clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyNewlyCreatedShippingAddressIsSelectedByDefault(String name){
		driver.waitForElementPresent(By.xpath("//div[@id='new-shipping-added']//span[contains(text(),'"+name+"')]/../following-sibling::p//input[@checked='checked']"));
		return driver.isElementPresent(By.xpath("//div[@id='new-shipping-added']//span[contains(text(),'"+name+"')]/../following-sibling::p//input[@checked='checked']"));
	}

	public void clickOnSaveCRPShippingInfo(){
		driver.waitForElementPresent(By.id("saveCrpShippingAddress"));
		driver.click(By.id("saveCrpShippingAddress"));
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.id("QAS_RefineBtn"));
			driver.click(By.id("QAS_RefineBtn"));
		}catch(Exception e){
			logger.info("No Accept popup");
		}
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(3000);
		driver.waitForPageLoad();
	}

	public boolean verifyCheckoutConfirmation(){
		try{
			driver.findElement(By.xpath("//div[@id='popup-review']/h2[contains(text(),'Checkout Confirmation')]"));
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public boolean verifyConsultantIsAbleToContinueCheckoutProcess(){
		try{
			driver.quickWaitForElementPresent(By.id("saveShippingInfo"));
			if(driver.isElementPresent(By.id("saveShippingInfo"))){
				return true;
			}
		}catch(Exception e){
			return false;
		}
		return false;
	}

	public boolean verifySVValueOnCartPage(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='cart-summary-module']/div[5]/div[2]/span"));
		return driver.isElementPresent(By.xpath("//div[@id='cart-summary-module']/div[5]/div[2]/span"));
	}

	public boolean verifySVValueOnOrderSummaryPage(){
		driver.waitForElementPresent(By.xpath("//div[contains(@class,'checkout-module-content')]//div[contains(text(),'Total SV')]/following::div[1]/span"));
		return driver.isElementPresent(By.xpath("//div[contains(@class,'checkout-module-content')]//div[contains(text(),'Total SV')]/following::div[1]/span"));
	}

	public boolean verifySVValueOnOrderConfirmationPage(){
		driver.quickWaitForElementPresent(By.id("productSV"));
		return driver.isElementPresent(By.id("productSV"));
	}

	public int getNoOfProductInCart(){
		return driver.findElements(By.xpath("//div[@class='cart-items']/div")).size();
	}

	public boolean getValueOfFlag(int i){
		try{
			String SVValue = driver.findElement(By.xpath("//div[@class='cart-items']/div["+i+"]//span[@id='cartQVSV']")).getText().split("\\ ")[1];
			if(Double.parseDouble(SVValue)>0){
				return true;
			}
		}catch(Exception e){
		}
		return false;
	}

	public String removeProductSFromCart(int i){
		try{
			String SVValue = driver.findElement(By.xpath("//div[@class='cart-items']/div["+i+"]//span[@id='cartQVSV']")).getText();
			driver.click(By.xpath("//div[@class='cart-items']/div["+i+"]//a[text()='Remove']"));
			logger.info("//div[@class='cart-items']/div["+i+"]//a[text()='Remove'] clicked");
			driver.waitForPageLoad();
			return SVValue;
		}catch (Exception e) {
			logger.info("No Remove option");
		}
		return null;
	}
	public String getSVValueFromCart(){
		if(driver.getCountry().equalsIgnoreCase("ca")){
			logger.info(driver.findElement(By.xpath("//div[contains(text(),'Total SV')]/following::span[1]")).getText());
			return driver.findElement(By.xpath("//div[contains(text(),'Total SV')]/following::span[1]")).getText();
		}else{
			logger.info(driver.findElement(By.xpath("//div[@class='SV-info']//div[contains(text(),'Total SV')]/following::span[1]")).getText());
			return driver.findElement(By.xpath("//div[@class='SV-info']//div[contains(text(),'Total SV')]/following::span[1]")).getText();
		}
	}

	public double compareSVValue(String SVValueOfRemovedProduct, double totalSVValue){
		System.out.println("enter in compare method");
		String sv = SVValueOfRemovedProduct.split("\\ ")[1];
		double SVValueOfRemoved = Double.parseDouble(sv);
		double finalSVValue = totalSVValue-SVValueOfRemoved;
		System.out.println("Final SV Value "+finalSVValue);
		return finalSVValue;

	}

	public void clickOnEditOnNotDefaultAddressOfBilling(){
		if(driver.isElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div[1]//input[@checked='checked']"))==false){
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div[1]//a[contains(text(),'Edit')]")));
			//driver.click(By.xpath("//div[@id='multiple-billing-profiles']/div[1]//a[contains(text(),'Edit')]"));
		}else{
			try{
				driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div[2]//a[contains(text(),'Edit')]")));
				//driver.click(By.xpath("//div[@id='multiple-billing-profiles']/div[2]//a[contains(text(),'Edit')]"));
			}catch(NoSuchElementException e){
				driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div/div[2]//a[contains(text(),'Edit')]")));
				//driver.click(By.xpath("//div[@id='multiple-billing-profiles']/div/div[2]//a[contains(text(),'Edit')]"));
			}
		}
		driver.pauseExecutionFor(5000);
	}

	public boolean isNewEditedBillingProfileIsPresentOnOrderConfirmationPage(String profileName){
		driver.waitForElementPresent(By.xpath("//div[@id='confirm-left-shopping']//h3[contains(text(),'Billing to')]/following::span[contains(text(),'"+profileName+"')]"));
		return driver.isElementPresent(By.xpath("//div[@id='confirm-left-shopping']//h3[contains(text(),'Billing to')]/following::span[contains(text(),'"+profileName+"')]"));
	}

	public boolean isBillingProfileIsSelectedByDefault(String profileName){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/../following-sibling::p//input[@checked='checked']"));
		return driver.isElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/../following-sibling::p//input[@checked='checked']"));
	}

	public boolean isTheBillingAddressPresentOnPage(String firstName){
		boolean isFirstNamePresent = false;
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div"));
		List<WebElement> allBillingProfiles = driver.findElements(By.xpath("//div[@id='multiple-billing-profiles']/div"));  
		for(int i=1;i<=allBillingProfiles.size();i++){   
			isFirstNamePresent = driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div["+i+"]/p[1]/span[1]")).getText().toLowerCase().contains(firstName.toLowerCase());
			if(isFirstNamePresent == true){ 
				return true;
			}
		}
		return false;
	}

	public boolean isDefaultBillingProfileIsPresentOrderConfirmationPage(String profileName){
		if(driver.getCountry().equalsIgnoreCase("us")){
			driver.waitForElementPresent(By.xpath("//div[@id='confirm-left-shopping']//span[contains(text(),'"+profileName+"')]"));
			return driver.isElementPresent(By.xpath("//div[@id='confirm-left-shopping']//span[contains(text(),'"+profileName+"')]"));
		}else{
			driver.waitForElementPresent(By.xpath("//div[@id='confirm-left-shopping']//h2[contains(text(),'Billing')]/following::span[contains(text(),'"+profileName+"')]"));
			return driver.isElementPresent(By.xpath("//div[@id='confirm-left-shopping']//h2[contains(text(),'Billing')]/following::span[contains(text(),'"+profileName+"')]"));
		}
	}

	public String getDefaultShippingProfileName(){
		return driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']//input[@checked='checked']/ancestor::div[2]//div[contains(@class,'user-name')]")).getText();
	}

	public void addAshippingProfileAndClickOnItsEdit(String city,String state,String addressLine,String profileName,String phoneNumber,String postalCode) throws InterruptedException{
		//driver.waitForElementPresent(By.xpath("//a[text()='Add new shipping address »']"));
		driver.click(By.xpath("//a[contains(text(),'Add new shipping address')]"));
		logger.info("add new shipping address clicked");
		enterNewShippingAddressName(profileName+" "+TestConstants.LAST_NAME);
		enterNewShippingAddressLine1(addressLine);
		enterNewShippingAddressCity(city);
		selectNewShippingAddressStateOnCartPage();
		enterNewShippingAddressPhoneNumber(phoneNumber);
		enterNewShippingAddressPostalCode(postalCode);
		clickOnSaveShippingProfile();
		logger.info("save address button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.click(By.xpath("//div[@id='multiple-addresses-summary']//span[contains(text(),'"+profileName+"')]/following::a[@class='editShippingAddress'][1]"));
		logger.info("Edit clicked of newly added shipping profile");
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isOnlyOneShippingProfilePresentOnAdhocCart(){
		List<WebElement> totalShippingAddressPresentOnAdhocCart = driver.findElements(By.xpath("//div[@id='multiple-addresses-summary']/div"));
		if(totalShippingAddressPresentOnAdhocCart.size()==1){
			return true;
		}
		return false;
	}

	public boolean verifyNewlyEditedShippingAddressIsSelected(String name){
		driver.waitForElementPresent(By.xpath("//div[@id='new-shipping-added']//span[contains(text(),'"+name+"')]/following::span[@class='radio-button shiptothis']/input[@checked='checked']"));
		return driver.isElementPresent(By.xpath("//div[@id='new-shipping-added']//span[contains(text(),'"+name+"')]/following::span[@class='radio-button shiptothis']/input[@checked='checked']"));
	}

	public String getDefaultSelectedShippingAddressNameFromShippingInfoPage(){
		if(driver.getCountry().equalsIgnoreCase("us")){
			driver.waitForElementPresent(By.xpath("//input[@checked='checked' and @name='addressCode']/../../preceding-sibling::p[3]/span[1]"));
			String name= driver.findElement(By.xpath("//input[@checked='checked' and @name='addressCode']/../../preceding-sibling::p[3]/span[1]")).getText();
			logger.info("Shipping method name to assert is "+name);
			return name;
		} else if(driver.getCountry().equalsIgnoreCase("ca")){
			driver.waitForElementPresent(By.xpath("//input[@checked='checked' and @name='addressCode']/../../preceding-sibling::p[3]/span[1]"));
			String name= driver.findElement(By.xpath("//input[@checked='checked' and @name='addressCode']/../../preceding-sibling::p[3]/span[1]")).getText();
			return name;
		}
		return null;
	}

	public boolean verifySelectedbillingProfileIsDefault(String name) {
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),"+name+")]/following::input[@checked='checked']"));
		if(driver.isElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),"+name+")]/following::input[@checked='checked']"))){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean verifySelectedShippingAddressIsDefault(String newShipingAddressName) {
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']//span[contains(text(),"+newShipingAddressName+")]/following::p[2]//input[@checked='checked']"));
		if(driver.isElementPresent(By.xpath("//div[@id='multiple-addresses-summary']//span[contains(text(),"+newShipingAddressName+")]/following::p[2]//input[@checked='checked']"))){
			return true;
		}
		return false;
	}

	public boolean verifySelectedShippingMethodNameOnUI(String selectedMethodName) {
		String methodName = driver.findElement(By.xpath("//div[@id='checkout_summary_deliverymode_div']/div[3]/p")).getText();
		return methodName.contains(selectedMethodName.trim());
	}

	public boolean verifyErrorMessageForCreditCardSecurityCode(){
		driver.quickWaitForElementPresent(By.xpath("//label[text()='This field is required.']"));
		return driver.isElementPresent(By.xpath("//label[text()='This field is required.']"));
	}
	public boolean isNewlyCreatedBillingProfileIsSelectedByDefault(String profileName){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/../following-sibling::div//input[@checked='checked']"));
		return driver.isElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/../following-sibling::div//input[@checked='checked']"));
	}

	public String getTotalPriceOfProductForPC(){
		String value = driver.findElement(By.xpath("//div[@id='total-shopping']//span")).getText().trim();
		return value;
	}

	public void clickOnEditOnNotDefaultAddressOfShipping(){
		if(driver.isElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div[1]/div[1]//input[@checked='checked']"))==false){
			//driver.click(By.xpath("//div[@id='multiple-addresses-summary']/div[1]/div[1]//a[text()='Edit']"));
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div[1]/div[1]//a[text()='Edit']")));
		}else{
			try{
				driver.click(By.xpath("//div[@id='multiple-addresses-summary']/div[1]/div[2]//a[text()='Edit']"));
			}catch(NoSuchElementException e){
				driver.click(By.xpath("//div[@id='multiple-billing-profiles']/div/div[2]//a[contains(text(),'Edit')]"));
			}
		}
		driver.pauseExecutionFor(5000);
	}

	public boolean isNewEditedShippingProfileIsPresentOnOrderConfirmationPage(String profileName){
		driver.waitForElementPresent(By.xpath("//div[@id='confirm-left-shopping']//span[contains(text(),'"+profileName+"')]"));
		return driver.isElementPresent(By.xpath("//div[@id='confirm-left-shopping']//span[contains(text(),'"+profileName+"')]"));
	}

	public boolean verifyPCPerksPromoDuringPlaceAdhocOrder(){
		//		return driver.isElementPresent(By.xpath("//div[contains(text(),'PC')]/.."));
		return driver.isElementPresent(By.xpath("//span[contains(text(),'Subscribe and save')]"));
	}

	public boolean verifyShippingAddressContainsShippingMethodNameAfterPlaceOrder(String selectedShippingMethod){
		driver.quickWaitForElementPresent(By.xpath("//div[contains(text(),'Shipping Method')]"));
		String methodName = driver.findElement(By.xpath("//div[contains(text(),'Shipping Method')]")).getText().split("\\:")[1].trim();
		return selectedShippingMethod.contains(methodName);
	}

	public boolean verifyShippingAddressContainsShippingMethodName(String selectedShippingMethod){
		String methodNameFromUI = null;
		driver.quickWaitForElementPresent(By.xpath("//span[contains(text(),'Shipping Method')]/ancestor::p[1]"));
		String[] methodName = driver.findElement(By.xpath("//span[contains(text(),'Shipping Method')]/ancestor::p[1]")).getText().split("\\:");
		if(methodName[1].contains("Ground")){
			methodNameFromUI = methodName[1].split("\\)")[0]+")";
		}
		if(methodName[1].contains("2Day")){
			String[] shippingMethod = methodName[1].split("\\ ");
			methodNameFromUI = shippingMethod[0]+" "+shippingMethod[1];
		}
		return selectedShippingMethod.contains(methodNameFromUI.trim());
	}

	public String getSelectedShippingMethodName(){
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']"));
		driver.click(By.xpath("//select[@id='deliveryMode']"));
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']/option[1]"));
		return driver.findElement(By.xpath("//select[@id='deliveryMode']/option[@selected='selected']")).getText();
	}

	public void clickOnNewShipToThisAddressRadioButton(String name){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]"));
		List<WebElement> allShippingAddresses = driver.findElements(By.xpath("//div[@id='multiple-addresses-summary']/div[contains(@class,'address-section')]"));
		logger.info("Total shipping addresses listed are "+allShippingAddresses.size());
		driver.pauseExecutionFor(2000);
		if(driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']//div[contains(text(),'"+name+"')]/../following-sibling::div[2]//span/input[@type='radio']")).isSelected()==false){
			driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']//div[contains(text(),'"+name+"')]/../following-sibling::div[2]//span/input[@type='radio']"));
			driver.click(By.xpath("//div[@id='multiple-addresses-summary']//div[contains(text(),'"+name+"')]/../following-sibling::div[2]//span/input[@type='radio']/.."));
			driver.waitForLoadingImageToDisappear();
		}
	}

	public boolean verifyFirstNameAndLastNameAndEmailAddressFromUIOnAccountInfoPage(String name){
		driver.waitForElementPresent(By.xpath("//div[@id='checkout_summary_deliveryaddress_div']//div[contains(text(),'"+name+"')]"));
		return driver.isElementPresent(By.xpath("//div[@id='checkout_summary_deliveryaddress_div']//div[contains(text(),'"+name+"')]"));
	}

	public boolean verifyUserCanEditMainAccountInfoOnCartPage(){
		return driver.findElement(By.id("addressForm")).isDisplayed();
	}

	public void clickEditMainAccountInfoOnCartPage(){
		driver.waitForElementPresent(By.xpath("//div[@id='checkout_summary_deliveryaddress_div']//a"));
		driver.click(By.xpath("//div[@id='checkout_summary_deliveryaddress_div']//a"));
		driver.waitForLoadingImageToDisappear();
		logger.info("Edit main account info link clicked on cart page ");
	}

	public boolean verifyCartUpdateMessage(){
		return driver.isElementPresent(By.xpath("//div[@id='content-full-page']//div[contains(text(),'Your Next cart has been updated')]"));
	}

	public String getProductCountOnAutoShipCartPage(){
		String countOfProductInAutoShipCart=null;
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='shopping-wrapper']/div[3]/div[1]/h1/span"));
			String count=driver.findElement(By.xpath("//div[@id='shopping-wrapper']/div[3]/div[1]/h1/span")).getText().trim();
			String[] arr=count.split("\\ ");
			String []countOfProduct=arr[0].split("\\(");
			countOfProductInAutoShipCart=countOfProduct[1];
			logger.info("count of product in autoship cart is "+countOfProductInAutoShipCart);
		}catch(NoSuchElementException e){
			try{
				driver.waitForElementPresent(By.xpath("//div[@id='shopping-wrapper']/div/div[1]/h1/span  "));
				String count=driver.findElement(By.xpath(".//div[@id='shopping-wrapper']/div/div[1]/h1/span  ")).getText().trim();
				String[] arr=count.split("\\ ");
				String []countOfProduct=arr[0].split("\\(");
				countOfProductInAutoShipCart=countOfProduct[1];
				logger.info("count of product in autoship cart is "+countOfProductInAutoShipCart);
			}catch(NoSuchElementException e1){
				driver.waitForElementPresent(By.xpath("//div[@id='shopping-wrapper']/div[2]/div[1]/h1/span"));
				String count=driver.findElement(By.xpath("//div[@id='shopping-wrapper']/div[2]/div[1]/h1/span")).getText().trim();
				String[] arr=count.split("\\ ");
				String []countOfProduct=arr[0].split("\\(");
				countOfProductInAutoShipCart=countOfProduct[1];
				logger.info("count of product in autoship cart is "+countOfProductInAutoShipCart);
			}
		}
		return countOfProductInAutoShipCart.trim();
	}

	public void selectDifferentProductAndAddItToPCPerks(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][2]"));
		driver.click(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][2]"));
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
		}catch(Exception e){
		}
		logger.info("Add To PC perks button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public boolean selectFirstAddressAndValidateSecondIsUnSelected(){
		driver.navigate().refresh();
		driver.waitForPageLoad();
		//((JavascriptExecutor) RFWebsiteDriver.driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div[1]/div[3]/span")));
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div[1]/div[3]/span"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div[1]/div[3]/span")));
		return driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div[2]/div[3]/span")).isSelected();
	}

	public boolean selectSecondAddressAndValidateFirstIsUnSelected(){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div[2]/div[3]/span"));
		driver.click(By.xpath("//div[@id='multiple-addresses-summary']/div[2]/div[3]/span"));
		driver.pauseExecutionFor(2000);
		return driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div[1]/div[3]/span")).isSelected();
	}

	public String selectAndGetBillingMethodName(String country) throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div/div[1]//div[@class='billing-address-radio']//input"));
		if(driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div/div[1]//div[@class='billing-address-radio']//input")).isSelected()==false){
			driver.click(By.xpath("//div[@id='multiple-billing-profiles']/div/div[1]//div[@class='billing-address-radio']//input/.."));
			driver.waitForLoadingImageToDisappear();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div/div[1]//div[@class='billing-address-radio']//input")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div/div[1]//div[@class='billing-address-radio']//input/preceding::p[3]//span")).getText(); 
		}
		else if(driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div/div[2]//div[@class='billing-address-radio']//input")).isSelected()==false){
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div/div[2]//div[@class='billing-address-radio']//input/..")));
			//driver.click(By.xpath("//div[@id='multiple-billing-profiles']/div/div[2]//div[@class='billing-address-radio']//input/.."));
			driver.waitForLoadingImageToDisappear();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div/div[2]//div[@class='billing-address-radio']//input")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div/div[2]//div[@class='billing-address-radio']//input/preceding::p[3]//span")).getText();  
		}
		else {
			driver.click(By.xpath("//div[@id='multiple-billing-profiles']/div/div[3]//div[@class='billing-address-radio']//input/.."));
			driver.waitForLoadingImageToDisappear();
			logger.info("Shipping method selected is "+driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div/div[3]//div[@class='billing-address-radio']//input")).getText().split("-")[0]);
			return driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div/div[3]//div[@class='billing-address-radio']//input/preceding::p[3]//span")).getText();  
		}

	}

	public boolean verifyIsOnlyOneRadioButtonSelected(String country){
		int noOfRadioButtonSelect =0;
		List<WebElement> totalBillingAddress = driver.findElements(By.xpath("//div[@id='multiple-billing-profiles']/div/div"));
		for(int i=1; i<=totalBillingAddress.size(); i++){
			driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div/div["+i+"]//div[@class='billing-address-radio']//input"));
			if(driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div/div["+i+"]//div[@class='billing-address-radio']//input")).isSelected()==true){
				driver.waitForLoadingImageToDisappear();
				noOfRadioButtonSelect++;
			}else{
				logger.info("//div[@id='multiple-billing-profiles']/div/div["+i+"]//div[@class='billing-address-radio']//input"+" "+"is not selected");
			}
		}
		return noOfRadioButtonSelect==1;
	}

	public boolean isTheBillingAddressPresentOnPage(String firstName, int noOfBillingProfile){
		boolean isFirstNamePresent = false;
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div"));
		for(int i=1;i<=noOfBillingProfile;i++){   
			isFirstNamePresent = driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']")).getText().toLowerCase().contains(firstName.toLowerCase());
			if(isFirstNamePresent == true){ 
				return true;
			}
		}
		return false;
	}

	public void selectDifferentBillingProfile(String profileName){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+profileName+"')]/following::div[1]//input/.."));
		driver.click(By.xpath("//span[contains(text(),'"+profileName+"')]/following::div[1]//input/.."));
	}

	public boolean isShippingAddressNextStepBtnIsPresent(){
		return driver.isElementPresent(By.id("saveShippingInfo"));
	}

	public String getDefaultSelectedShippingMethodName(){
		String defaultShippingAddress=driver.findElement(By.xpath("//span[contains(@style,'radio_btn_checked.png')]/input[@id='Radio4']/../following-sibling::label")).getText();
		String[] arr=defaultShippingAddress.split("-");
		String defaultShipmethod=arr[0];
		logger.info("default selected shipping method is "+defaultShipmethod.trim());
		return defaultShipmethod.trim();
	}

	public int getDifferentProductCountOnAutoShipCartPage(){
		int count=0;
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/div[2]/div"));
			count=driver.findElements(By.xpath("//div[@id='left-shopping']/div[2]/div")).size();
			logger.info("Different type product in autoship cart is "+count);
		}catch(NoSuchElementException e){
			driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/div[2]/div"));
			count=driver.findElements(By.xpath("//div[@id='left-shopping']/div[2]/div")).size();
			logger.info("Different type product in autoship cart is "+count);
		}
		return count;
	}

	public String getShippingChargesAtOrderConfirmationPage(){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping')]/following::span[1]"));
		return driver.findElement(By.xpath("//span[contains(text(),'Shipping')]/following::span[1]")).getText();
	}

	public String getHandlingChargesAtOrderConfirmationPage(){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Handling')]/following::span[1]"));
		return driver.findElement(By.xpath("//span[contains(text(),'Handling')]/following::span[1]")).getText();
	}

	public void selectNewlyAddedBillingAddressName(String name){
		driver.waitForElementPresent(By.id("addressBookdropdown"));
		driver.click(By.id("addressBookdropdown"));
		driver.waitForElementPresent(By.xpath("//*[@id='addressBookdropdown']/option[@firstname='"+name+"']"));
		driver.click(By.xpath("//*[@id='addressBookdropdown']/option[@firstname='"+name+"']"));
		logger.info("New Billing card address selected");
		driver.pauseExecutionFor(2000);
	}

	public String getProductNameAtOrderConfirmationPage(String productNumber){
		driver.waitForElementPresent(By.xpath("//div[contains(@class,'order-products checkout-confirm')]/div["+productNumber+"]//p[2]"));
		return driver.findElement(By.xpath("//div[contains(@class,'order-products checkout-confirm')]/div["+productNumber+"]//p[2]")).getText().trim();
	}

	public void selectShippingMethodUPStandardOvernightInOrderSummary(){
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']"));
		driver.click(By.xpath("//select[@id='deliveryMode']"));
		driver.waitForElementPresent(By.xpath("//select[@id='deliveryMode']/option[1]"));
		driver.click(By.xpath("//select[@id='deliveryMode']/option[contains(text(),'Overnight')]"));
		logger.info("UPS Standard Overnight/1day shipping method is selected");

	}

	public void clickOnSaveShippingProfileWithoutAcceptingQASValidationPopUp(){
		driver.click(By.id("saveShippingAddreessId"));
		driver.waitForLoadingImageToDisappear();
		logger.info("Save shipping profile button clicked");
	}

	public boolean validateQASValidationPopUpIsDisplayed(){
		return driver.isElementPresent(By.xpath("//div[@id='QAS_Dialog']"));
	}

	public boolean validateNewlySelectedDefaultBillingProfileIsNotUpdatedInAutoshipBillingProfileSection(String profileName){
		driver.waitForElementPresent(By.xpath("//p[@id='selectedPaymentInfo']/span[1]"));
		return driver.findElement(By.xpath("//p[@id='selectedPaymentInfo']/span[1]")).getText().trim().equalsIgnoreCase(profileName);
	}

	public boolean validateNewlySelectedDefaultShippingProfileIsUpdatedInAutoshipShippingSection(String profileName){
		driver.waitForElementPresent(By.xpath("//p[@id='selectedAddress']/span[1]"));
		return driver.findElement(By.xpath("//p[@id='selectedAddress']/span[1]")).getText().trim().equalsIgnoreCase(profileName.trim());
	}

	public void selectShippingMethodUPStandardOvernightUnderShippingMethodOnAutoShipOrderPage(){
		driver.waitForElementPresent(By.xpath("//div[@id='delivery_modes_dl']/div//div[3]//label[contains(text(),'Standard Overnight')]/preceding-sibling::span"));
		driver.click(By.xpath("//div[@id='delivery_modes_dl']/div//div[3]//label[contains(text(),'Standard Overnight')]/preceding-sibling::span"));
		driver.waitForLoadingImageToAppear();
		logger.info("UPS Standard Overnight/1day shipping method is selected"); 
	}

	public String getQtyOfAddedProduct(String SKUValue){
		driver.waitForElementPresent(By.xpath("//p[contains(text(),'"+SKUValue+"')]/preceding::input[1]"));
		logger.info("Qty of product in storefornt "+driver.findElement(By.xpath("//p[contains(text(),'"+SKUValue+"')]/preceding::input[1]")).getAttribute("value"));
		return driver.findElement(By.xpath("//p[contains(text(),'"+SKUValue+"')]/preceding::input[1]")).getAttribute("value");
	}

	public void selectShippingMethod2DayForAdhocOrder(){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.waitForElementPresent(By.xpath("//span[contains(text(),'UPS 2Day')]/..//span[contains(@class,'radio')]"));
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//span[contains(text(),'UPS 2Day')]/..//span[contains(@class,'radio')]")));
			logger.info("UPS 2Day shipping method is selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.waitForElementPresent(By.xpath("//div[@id='delivery_modes_dl']//label[contains(text(),'FedEx 2Day')]/preceding-sibling::span"));
			driver.click(By.xpath("//div[@id='delivery_modes_dl']//label[contains(text(),'FedEx 2Day')]/preceding-sibling::span"));
			logger.info("Fedex 2Day shipping method is selected");
		}

		driver.waitForLoadingImageToDisappear();
	}

	public void selectDifferentProductAndAddItToCRP(int j) throws InterruptedException{  
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::*[contains(text(),'ADD TO CRP') or @value='Add to crp']["+j+"]"));
		driver.click(By.xpath("//div[@id='main-content']/descendant::*[contains(text(),'ADD TO CRP') or @value='Add to crp']["+j+"]"));
		logger.info("Add to CRP button clicked");
		driver.waitForLoadingImageToDisappear();

		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}
	}

	public void selectProductAndProceedToBuyForPC(int i) throws InterruptedException{
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][1]"));
		driver.click(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')]["+i+"]"));
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
		}catch(Exception e){
		}
		logger.info("Add To PC perks button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void selectShippingMethodUPSGroundForAdhocOrder(){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.waitForElementPresent(By.xpath("//span[contains(text(),'UPS Ground')]/..//span[contains(@class,'radio')]"));
			driver.click(By.xpath("//span[contains(text(),'UPS Ground')]/..//span[contains(@class,'radio')]"));
			logger.info("UPS Ground HD shipping method is selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.waitForElementPresent(By.xpath("//div[@id='delivery_modes_dl']//label[contains(text(),'FedEx 2Day')]/preceding-sibling::span"));
			driver.click(By.xpath("//div[@id='delivery_modes_dl']//label[contains(text(),'FedEx 2Day')]/preceding-sibling::span"));
			logger.info("Fedex 2Day shipping method is selected");
		}

		driver.waitForLoadingImageToDisappear();
	}

	public void selectShippingMethodUPSStandardOverNightForAdhocOrder(){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.waitForElementPresent(By.xpath("//span[contains(text(),'UPS Standard Overnight')]/..//span[contains(@class,'radio')]"));
			driver.click(By.xpath("//span[contains(text(),'UPS Standard Overnight')]/..//span[contains(@class,'radio')]"));
			logger.info("UPS Standard overnight shipping method is selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.waitForElementPresent(By.xpath("//div[@id='delivery_modes_dl']//label[contains(text(),'FedEx 2Day')]/preceding-sibling::span"));
			driver.click(By.xpath("//div[@id='delivery_modes_dl']//label[contains(text(),'FedEx 2Day')]/preceding-sibling::span"));
			logger.info("Fedex 2Day shipping method is selected");
		}

		driver.waitForLoadingImageToDisappear();
	}

	public boolean isDeliveryChargesPresent(){
		driver.waitForElementPresent(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Delivery')]/following::div[1]/span"));
		String deliveryCharges =  driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'Delivery')]/following::div[1]/span")).getText().trim().split("\\$")[1].trim();
		return !(deliveryCharges==null);
	}

	public boolean isShippingChargesPresentAtOrderConfirmationPage(){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping')]/following::span[1]"));
		String deliveryCharges = driver.findElement(By.xpath("//span[contains(text(),'Shipping')]/following::span[1]")).getText();
		return !(deliveryCharges==null);
	}

}