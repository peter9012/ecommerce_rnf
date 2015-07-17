package com.rf.pages.website;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

public class StoreFrontBillingInfoPage extends RFWebsiteBasePage{

	private final By DEFAULT_ADDRESS_RADIO_BTN_LOC = By.cssSelector("ul[id='multiple-billing-profiles']>li:nth-child(1) input[name='bill-card']");
	private final By BILLING_INFO_TEMPLATE_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top' and text()='Billing info']"); 

	public StoreFrontBillingInfoPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public boolean verifyBillingInfoPageIsDisplayed(){
		driver.waitForElementPresent(BILLING_INFO_TEMPLATE_HEADER_LOC);
		return driver.getCurrentUrl().contains(TestConstants.BILLING_PAGE_SUFFIX_URL);
	}

	public int getTotalBillingAddressesDisplayed(){
		List<WebElement> totalBillingAddressesDisplayed = driver.findElements(By.xpath("//ul[@id='multiple-billing-profiles']/li"));		
		return totalBillingAddressesDisplayed.size();
	}

	public boolean isDefaultAddressRadioBtnSelected(String defaultAddressFirstNameDB) throws InterruptedException{
		Thread.sleep(2000);		
		return driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstNameDB+"')]/ancestor::li[1]/form/span/input")).isSelected();
	}

	public String getDefaultBillingAddress(){
		String defaultBillingAddress = driver.findElement(By.xpath("//input[@class='paymentAddress' and @checked='checked']/ancestor::li[1]/p[1]")).getText();		
		System.out.println("defaultBillingAddress "+defaultBillingAddress);
		return defaultBillingAddress;
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
	
	public void clickOnEditBillingProfile(String billingProfileName){
		System.out.println("&&&&&&&&&&& "+"//span[text()='"+billingProfileName+"']/ancestor::li//a");
		driver.findElement(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a")).click();
	}
	
	public String getBillingProfileName(){
		return driver.findElement(By.xpath("//ul[@id='multiple-billing-profiles']//li[1]/p[1]/span[@class='font-bold']")).getText();
	}
}
