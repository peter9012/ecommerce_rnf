package com.rf.pages.website;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontBillingInfoPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontBillingInfoPage.class.getName());

	private final By DEFAULT_ADDRESS_RADIO_BTN_LOC = By.cssSelector("ul[id='multiple-billing-profiles']>li:nth-child(1) input[name='bill-card']");
	private final By BILLING_INFO_TEMPLATE_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top' and text()='Billing info']");
	private final By TOTAL_BILLING_ADDRESSES_LOC =  By.xpath("//ul[@id='multiple-billing-profiles']/li");
	private final By DEFAULT_BILLING_ADDRESSES_LOC = By.xpath("//input[@class='paymentAddress' and @checked='checked']/ancestor::li[1]/p[1]");
	private final By ADD_NEW_BILLING_LINK_LOC = By.linkText("Add a new billing profile»");
	private final By ADD_NEW_BILLING_CARD_NUMBER_LOC = By.id("card-nr");
	private final By ADD_NEW_BILLING_CARD_NAME_LOC = By.id("card-name");
	private final By ADD_NEW_BILLING_CARD_EXP_MONTH_LOC = By.id("expiryMonth");
	private final By ADD_NEW_BILLING_CARD_EXP_YEAR_LOC = By.id("expiryYear");
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
		driver.waitForElementPresent(TOTAL_BILLING_ADDRESSES_LOC);
		List<WebElement> totalBillingAddressesDisplayed = driver.findElements(TOTAL_BILLING_ADDRESSES_LOC);
		logger.info("Total Billing Profiles On UI are "+totalBillingAddressesDisplayed.size());		
		return totalBillingAddressesDisplayed.size();
	}

	public boolean isDefaultAddressRadioBtnSelected(String defaultAddressFirstNameDB) throws InterruptedException{
		Thread.sleep(2000);
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+defaultAddressFirstNameDB+"')]/ancestor::li[1]/form/span/input"));
		return driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstNameDB+"')]/ancestor::li[1]/form/span/input")).isSelected();
	}

	public boolean isDefaultBillingAddressSelected(String firstName) throws InterruptedException{
		try{
			driver.waitForElementPresent(By.xpath("//span[text()='"+firstName+"']/ancestor::li[1]/form//input"));
			Thread.sleep(2000);
			return driver.findElement(By.xpath("//span[text()='"+firstName+"']/ancestor::li[1]/form//input")).isSelected();

		}catch(NoSuchElementException e){
			try{
				return driver.findElement(By.xpath("//span[text()='"+firstName.toLowerCase()+"']/ancestor::li[1]/form//input")).isSelected();				
			}
			catch(NoSuchElementException e1){
				return false;
			}			
		}	 

	}

	public String getDefaultBillingAddress(){
		driver.waitForElementPresent(DEFAULT_BILLING_ADDRESSES_LOC);
		String defaultBillingAddress = driver.findElement(DEFAULT_BILLING_ADDRESSES_LOC).getText();
		logger.info("Default Billing address is "+DEFAULT_BILLING_ADDRESSES_LOC);
		return defaultBillingAddress;
	}

	public void clickAddNewBillingProfileLink() throws InterruptedException{
		driver.waitForElementPresent(ADD_NEW_BILLING_LINK_LOC);
		driver.click(ADD_NEW_BILLING_LINK_LOC);
		Thread.sleep(2000);
		logger.info("Add new billing profile link clicked");
	}

	public void enterNewBillingCardNumber(String cardNumber){
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.xpath("//td[@id='credit-cards']"));		
		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		js.executeScript("$('#card-nr-masked').hide();$('#card-nr').show(); ", driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC).clear();
		driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC).sendKeys(cardNumber);
		logger.info("New Billing card number enterd as "+cardNumber);		
	}

	public void enterNewBillingNameOnCard(String nameOnCard){
		driver.waitForElementPresent(ADD_NEW_BILLING_CARD_NAME_LOC);
		driver.type(ADD_NEW_BILLING_CARD_NAME_LOC, nameOnCard);
		logger.info("New Billing card name enterd as "+nameOnCard);
	}

	public void selectNewBillingCardExpirationDate(String month,String year){
		driver.waitForElementPresent(ADD_NEW_BILLING_CARD_EXP_MONTH_LOC);
		Select cardExpirationMonthDD = new Select(driver.findElement(ADD_NEW_BILLING_CARD_EXP_MONTH_LOC));
		Select cardExpirationYearDD = new Select(driver.findElement(ADD_NEW_BILLING_CARD_EXP_YEAR_LOC));
		cardExpirationMonthDD.selectByValue(month.toUpperCase());
		logger.info("new billing month selected as "+month.toUpperCase());
		cardExpirationYearDD.selectByVisibleText(year);
		logger.info("new billing year selected as "+year);

	}

	public void enterNewBillingSecurityCode(String securityCode){
		driver.waitForElementPresent(ADD_NEW_BILLING_CARD_SECURITY_CODE_LOC);
		driver.type(ADD_NEW_BILLING_CARD_SECURITY_CODE_LOC, securityCode);
		logger.info("New billing security code entered as "+securityCode);
	}

	public void selectNewBillingCardAddress() throws InterruptedException{
		driver.waitForElementPresent(ADD_NEW_BILLING_CARD_ADDRESS_DD_LOC);
		driver.findElement(ADD_NEW_BILLING_CARD_ADDRESS_DD_LOC).click();
		logger.info("New billing card address drop down clicked");
		Thread.sleep(3000);
		driver.findElement(ADD_NEW_BILLING_CARD_ADDRESS_DD_FIRST_VALUE_LOC).click();
		logger.info("New billing card address selected");
	}

	public void selectUseThisBillingProfileFutureAutoshipChkbox(){
		driver.waitForElementPresent(USE_THIS_BILLING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC);
		driver.findElement(USE_THIS_BILLING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC).click();
		logger.info("Checkbox for Use this billing profile for future autoship selected");
	}

	public void clickOnSaveBillingProfile() throws InterruptedException{
		driver.waitForElementPresent(NEW_BILLING_PROFILE_SAVE_BTN_LOC);
		driver.click(NEW_BILLING_PROFILE_SAVE_BTN_LOC);
		Thread.sleep(5000);
		logger.info("save billing profile button clicked");
	}
	
	public void makeBillingProfileDefault(String firstName) throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//ul[@id='multiple-billing-profiles']//span[contains(text(),'"+firstName+"')]/following::span[@class='radio-button billtothis'][1]/input"));
		driver.findElement(By.xpath("//ul[@id='multiple-billing-profiles']//span[contains(text(),'"+firstName+"')]/following::span[@class='radio-button billtothis'][1]/input")).click();
		logger.info("default billing profile selected has the name "+firstName);
		Thread.sleep(60000);  // action taking too long,will remove later
	}

	public void clickOnEditBillingProfile() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//ul[@id='multiple-billing-profiles']//input[@checked='checked']/preceding::p[1]/a"));
		driver.findElement(By.xpath("//ul[@id='multiple-billing-profiles']//input[@checked='checked']/preceding::p[1]/a")).click();
		driver.waitForPageLoad();
		logger.info("Edit billing profile link clicked");
	}

	public String getBillingProfileName(){
		driver.waitForElementPresent(BILLING_PROFILE_NAME_LOC);
		logger.info("new billing profile name is "+driver.findElement(BILLING_PROFILE_NAME_LOC).getText());
		return driver.findElement(BILLING_PROFILE_NAME_LOC).getText();
	}

	public boolean isTheBillingAddressPresentOnPage(String firstName){
		boolean isFirstNamePresent = false;
		driver.waitForElementPresent(By.xpath("//ul[@id='multiple-billing-profiles']/li"));
		List<WebElement> allBillingProfiles = driver.findElements(By.xpath("//ul[@id='multiple-billing-profiles']/li"));		
		for(int i=1;i<=allBillingProfiles.size();i++){			
			isFirstNamePresent = driver.findElement(By.xpath("//ul[@id='multiple-billing-profiles']/li["+i+"]/p[1]/span[1]")).getText().toLowerCase().contains(firstName.toLowerCase());
			if(isFirstNamePresent == true){	
				return true;
			}
		}
		return false;
	}


}
