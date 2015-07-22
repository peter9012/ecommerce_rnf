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
	private final By TOTAL_BILLING_ADDRESSES_LOC =  By.xpath("//ul[@id='multiple-billing-profiles']/li");
	private final By DEFAULT_BILLING_ADDRESSES_LOC = By.xpath("//input[@class='paymentAddress' and @checked='checked']/ancestor::li[1]/p[1]");
	private final By ADD_NEW_BILLING_LINK_LOC = By.linkText("Add a new billing profile»");
	private final By ADD_NEW_BILLING_CARD_NUMBER_LOC = By.id("card-nr");
	private final By ADD_NEW_BILLING_CARD_NAME_LOC = By.id("card-name");
	private final By ADD_NEW_BILLING_CARD_EXP_MONTH_LOC = By.id("expiryMonth");
	private final By ADD_NEW_BILLING_CARD_EXP_YEAR_LOC = By.id("expiryMonth");
	private final By ADD_NEW_BILLING_CARD_SECURITY_CODE_LOC = By.id("security-code");
	private final By ADD_NEW_BILLING_CARD_ADDRESS_DD_LOC = By.xpath("//*[@id='addressBookdropdown']");
	private final By ADD_NEW_BILLING_CARD_ADDRESS_DD_FIRST_VALUE_LOC = By.xpath("//*[@id='addressBookdropdown']/option[1]");
	private final By USE_THIS_BILLING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC = By.xpath("//div[@id='use-for-autoship']/div");
	private final By NEW_BILLING_PROFILE_SAVE_BTN_LOC = By.id("submitButton");
	private final By BILLING_PROFILE_NAME_LOC = By.xpath("//ul[@id='multiple-billing-profiles']//li[1]/p[1]/span[@class='font-bold']");
	
	public StoreFrontBillingInfoPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public boolean verifyBillingInfoPageIsDisplayed(){
		driver.waitForElementPresent(BILLING_INFO_TEMPLATE_HEADER_LOC);
		return driver.getCurrentUrl().contains(TestConstants.BILLING_PAGE_SUFFIX_URL);
	}

	public int getTotalBillingAddressesDisplayed(){
		List<WebElement> totalBillingAddressesDisplayed = driver.findElements(TOTAL_BILLING_ADDRESSES_LOC);		
		return totalBillingAddressesDisplayed.size();
	}

	public boolean isDefaultAddressRadioBtnSelected(String defaultAddressFirstNameDB) throws InterruptedException{
		Thread.sleep(2000);		
		return driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstNameDB+"')]/ancestor::li[1]/form/span/input")).isSelected();
	}
	
	public boolean isDefaultBillingAddressSelected(String address1) throws InterruptedException{
		Thread.sleep(2000);
		System.out.println("UI "+driver.findElement(By.xpath("//ul[@id='multiple-billing-profiles']/li[1]/p[1]")).getText());
		return driver.findElement(By.xpath("//input[@name='addressCode' and @checked='checked']/ancestor::li[1]/p[1]")).getText().contains(address1);
	}

	public String getDefaultBillingAddress(){
		String defaultBillingAddress = driver.findElement(DEFAULT_BILLING_ADDRESSES_LOC).getText();		
		return defaultBillingAddress;
	}

	public void clickAddNewBillingProfileLink() throws InterruptedException{
		driver.click(ADD_NEW_BILLING_LINK_LOC);
		Thread.sleep(2000);
	}

	public void enterNewBillingCardNumber(String cardNumber){
		driver.type(ADD_NEW_BILLING_CARD_NUMBER_LOC, cardNumber);
	}

	public void enterNewBillingNameOnCard(String nameOnCard){
		driver.type(ADD_NEW_BILLING_CARD_NAME_LOC, nameOnCard);
	}

	public void selectNewBillingCardExpirationDate(String month,String year){
		Select cardExpirationMonthDD = new Select(driver.findElement(ADD_NEW_BILLING_CARD_EXP_MONTH_LOC));
		Select cardExpirationYearDD = new Select(driver.findElement(ADD_NEW_BILLING_CARD_EXP_YEAR_LOC));
		cardExpirationMonthDD.selectByVisibleText(month.toUpperCase());
		cardExpirationYearDD.selectByVisibleText(year);		
	}

	public void enterNewBillingSecurityCode(String securityCode){
		driver.type(ADD_NEW_BILLING_CARD_SECURITY_CODE_LOC, securityCode);
	}

	public void selectNewBillingCardAddress() throws InterruptedException{
		driver.findElement(ADD_NEW_BILLING_CARD_ADDRESS_DD_LOC).click();
		Thread.sleep(3000);
		driver.findElement(ADD_NEW_BILLING_CARD_ADDRESS_DD_FIRST_VALUE_LOC).click();
	}

	public void selectUseThisBillingProfileFutureAutoshipChkbox(){
		driver.findElement(USE_THIS_BILLING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC).click();
	}

	public void clickOnSaveBillingProfile() throws InterruptedException{
		driver.click(NEW_BILLING_PROFILE_SAVE_BTN_LOC);
		Thread.sleep(5000);
	}
	
	public void clickOnEditBillingProfile(String billingProfileName){		
		driver.findElement(By.xpath("//span[text()='"+billingProfileName+"']/ancestor::li//a")).click();
	}
	
	public String getBillingProfileName(){
		return driver.findElement(BILLING_PROFILE_NAME_LOC).getText();
	}
}
