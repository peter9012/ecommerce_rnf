package com.rf.pages.website;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontUpdateCartPage extends RFWebsiteBasePage{

	private final By UPDATE_CART_BTN_LOC = By.xpath("//input[@value='UPDATE CART']");
	private final By PAYMENT_BILLING_EDIT_BTN_LOC = By.xpath("//a[@class='editPayment']");
	private final By PAYMENT_PROFILE_NAME_LOC = By.xpath("//p[@id='selectedPaymentInfo']/span[1]");
	private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//img[@title='Rodan+Fields']");

	public StoreFrontUpdateCartPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void clickUpdateCartBtn() throws InterruptedException{
		driver.click(UPDATE_CART_BTN_LOC);
		System.out.println("*********** update cart clicked");
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
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[1]//label")).getText();	
		}
		else if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[2]//input")).isSelected()==false){
			driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[2]//span")).click();
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[2]//label")).getText();	
		}
		else if(driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[3]//input")).isSelected()==false){
			driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[3]//span")).click();
			return driver.findElement(By.xpath("//div[@id='start-shipping-method']//ul/li[3]//label")).getText();	
		}		
		return null;
	}
	
	public void clickOnEditShipping() throws InterruptedException{
		driver.findElement(By.xpath("//div[@id='checkout_summary_deliverymode_div']//a")).click();
		Thread.sleep(3000);
	}

}
