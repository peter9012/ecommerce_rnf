package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontUpdateCartPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontUpdateCartPage.class.getName());

	private final By UPDATE_CART_BTN_LOC = By.xpath("//input[@value='UPDATE CART']");
	private final By PAYMENT_BILLING_EDIT_BTN_LOC = By.xpath("//a[@class='editPayment']");
	private final By PAYMENT_PROFILE_NAME_LOC = By.xpath("//p[@id='selectedPaymentInfo']/span[1]");
	private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//img[@title='Rodan+Fields']");
	private final By ADD_NEW_SHIPPING_LINK_LOC = By.xpath("//a[@class='add-new-shipping-address']");


	public StoreFrontUpdateCartPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void clickUpdateCartBtn() throws InterruptedException{
		driver.click(UPDATE_CART_BTN_LOC);		
		Thread.sleep(5000);
	}

	public void clickOnEditPaymentBillingProfile(){
		driver.findElement(PAYMENT_BILLING_EDIT_BTN_LOC).click();
	}

	public boolean isNewBillingProfileIsSelectedByDefault(String profileName){
		return driver.findElement(By.xpath("//ul[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button billtothis'][1]/input")).isSelected();
	}

	public String getNameOnPaymentProfile(){
		return driver.findElement(PAYMENT_PROFILE_NAME_LOC).getText();
	}

	public StoreFrontConsultantPage clickRodanAndFieldsLogo(){
		driver.findElement(RODAN_AND_FIELDS_IMG_LOC).click();
		return new StoreFrontConsultantPage(driver);
	}

	public void clickAddNewBillingProfileLink() throws InterruptedException{
		driver.click(By.linkText("Add a new billing profile»"));
		Thread.sleep(2000);
	}

	public void enterNewBillingCardNumber(String cardNumber){
		driver.type(By.id("card-nr"), cardNumber);
	}

	public void enterNewBillingNameOnCard(String nameOnCard){
		driver.type(By.id("card-name"), nameOnCard);
	}

	public void selectNewBillingCardExpirationDate(String month,String year){
		Select cardExpirationMonthDD = new Select(driver.findElement(By.id("expiryMonth")));
		Select cardExpirationYearDD = new Select(driver.findElement(By.id("expiryYear")));
		cardExpirationMonthDD.selectByVisibleText(month.toUpperCase());
		cardExpirationYearDD.selectByVisibleText(year);		
	}

	public void enterNewBillingSecurityCode(String securityCode){
		driver.type(By.id("security-code"), securityCode);
	}

	public void selectNewBillingCardAddress() throws InterruptedException{
		driver.findElement(By.xpath("//*[@id='addressBookdropdown']")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id='addressBookdropdown']/option[1]")).click();
	}

	public void selectUseThisBillingProfileFutureAutoshipChkbox(){
		driver.findElement(By.xpath("//div[@id='use-for-autoship']/div")).click();
	}

	public void clickOnSaveBillingProfile() throws InterruptedException{
		driver.click(By.id("submitButton"));
		Thread.sleep(10000);
	}

	public String getBillingProfileName(){
		return driver.findElement(By.xpath("//ul[@id='multiple-billing-profiles']//li[1]/p[1]/span[@class='font-bold']")).getText();
	}

	public void clickOnNextStepBtn() throws InterruptedException{
		driver.findElement(By.id("clickNext")).click();
		Thread.sleep(2000);
	}

	public void clickOnUpdateCartShippingNextStepBtn() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		Thread.sleep(5000);
		//action.moveToElement(driver.findElement(By.xpath("//input[@id='use_address']"))).doubleClick().build().perform();		
		action.moveToElement(driver.findElement(By.xpath("//input[@id='use_address']"))).click(driver.findElement(By.xpath("//input[@id='use_address']"))).build().perform();
		Thread.sleep(2000);
	}

	public void clickOnEditBillingProfile(String billingProfileName){	
		driver.findElement(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a")).click();
	}

	public String selectAndGetShippingMethodName() throws InterruptedException{
		if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[1]//input")).isSelected()==false){
			driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[1]//span")).click();
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[1]//label")).getText().split("-")[0];	
		}
		else if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[2]//input")).isSelected()==false){
			driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[2]//span")).click();
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[2]//label")).getText().split("-")[0];	
		}
		else if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[3]//input")).isSelected()==false){
			driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[3]//span")).click();
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[3]//label")).getText().split("-")[0];	
		}		
		return null;
	}

	public void clickOnEditShipping() throws InterruptedException{
		driver.findElement(By.xpath("//div[@id='checkout_summary_deliverymode_div']//a")).click();
		Thread.sleep(3000);
	}

	public boolean isShippingAddressPresent(String name){
		try{
			driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/ul//span[@class='font-bold'][contains(text(),'"+name+"')]"));
			return true;
		}
		catch(NoSuchElementException e){
			return false;
		}
	}

	public void enterNewShippingAddressName(String name){
		logger.info("New Shipping Address name is "+name);
		driver.findElement(By.id("new-attention")).sendKeys(name);
	}

	public void enterNewShippingAddressLine1(String addressLine1){
		driver.findElement(By.id("new-address-1")).sendKeys(addressLine1);
	}

	public void enterNewShippingAddressCity(String city){
		driver.findElement(By.id("townCity")).sendKeys(city);
	}

	public void selectNewShippingAddressState(String state){
		Select stateDD = new Select(driver.findElement(By.id("state")));
		stateDD.selectByValue(state);
	}

	public void enterNewShippingAddressPostalCode(String postalCode){
		driver.findElement(By.id("postcode")).sendKeys(postalCode);
	}

	public void enterNewShippingAddressPhoneNumber(String phoneNumber){
		driver.findElement(By.id("phonenumber")).sendKeys(phoneNumber);
	}

	public void clickOnSaveShippingProfile() throws InterruptedException{
		driver.click(By.xpath("//input[@id='autoShipShippingAddr']"));
		Thread.sleep(5000);
	}

	public void acceptNewShippingAddress() throws InterruptedException{
		try{
			Thread.sleep(2000);
			driver.findElement(By.xpath("//input[@id='QAS_RefineBtn']")).click();
			Thread.sleep(5000);
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
		driver.click(ADD_NEW_SHIPPING_LINK_LOC);
		Thread.sleep(2000);
	}

	public void clickOnBuyNowButton(){
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Buy now']"));
		driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Buy now']")).click();
	}

	public void clickOnCheckoutButton(){
		driver.findElement(By.xpath("//input[@value='checkout']")).click();
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
		driver.findElement(By.xpath("//input[@value='OK']")).click();
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
		Thread.sleep(5000);
		action.moveToElement(driver.findElement(By.xpath("//input[@id='saveShippingInfo']"))).click(driver.findElement(By.xpath("//input[@id='saveShippingInfo']"))).build().perform();
		Thread.sleep(2000);
	}
	
	public String getSelectedBillingAddress(){
		return driver.findElement(By.xpath("//input[@name='bill-card'][@checked='checked']/ancestor::li[1]/p[1]")).getText();
	}
	
	public void clickOnBillingNextStepBtn() throws InterruptedException{
		driver.findElement(By.xpath("//div[@id='payment-next-button']/input")).click();
		Thread.sleep(3000);
	}
	
	public void clickPlaceOrderBtn()throws InterruptedException{
		driver.findElement(By.xpath("//input[@id='placeOrderButton']")).click();
		Thread.sleep(3000);
	}

}
