package com.rf.pages.website.storeFront;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontBillingInfoPage extends StoreFrontRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontBillingInfoPage.class.getName());

	private final By BILLING_INFO_TEMPLATE_HEADER_LOC = By.xpath("//div[@id='main-content']//span[contains(text(),'Billing info')]");
	private final By TOTAL_BILLING_ADDRESSES_LOC =  By.xpath("//div[@id='multiple-billing-profiles']/div[contains(@class,'col-sm-4')]");
	private final By DEFAULT_BILLING_ADDRESSES_LOC = By.xpath("//input[@class='paymentAddress' and @checked='checked']/ancestor::li[1]/p[1]");
	private final By ADD_NEW_BILLING_LINK_LOC = By.xpath("//a[@class='add-new-billing-profile']");
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
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+defaultAddressFirstNameDB+"')]/following::span[@class='radio-button billtothis'][1]/input"));
		return driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstNameDB+"')]/following::span[@class='radio-button billtothis'][1]/input")).isSelected();
	}

	public boolean isDefaultBillingAddressSelected(String firstName) throws InterruptedException{
		try{
			driver.quickWaitForElementPresent(By.xpath("//span[contains(text(),'"+firstName+"')]/following::input[@name='bill-card'][1]"));
			return driver.findElement(By.xpath("//span[contains(text(),'"+firstName+"')]/following::input[@name='bill-card'][1]")).isSelected();

		}catch(NoSuchElementException e){
			try{
				return driver.findElement(By.xpath("//span[contains(text(),'"+firstName.toLowerCase()+"')]/following::input[@name='bill-card'][1]")).isSelected();				
			}
			catch(NoSuchElementException e1){
				return false;
			}			
		}	 

	}

	public String getDefaultBillingAddress(){
		try{
			driver.waitForElementPresent(By.xpath("//input[@class='paymentAddress' and @checked='checked']/preceding::p[3]//span"));
			String defaultBillingAddress = driver.findElement(By.xpath("//input[@class='paymentAddress' and @checked='checked']/preceding::p[3]//span")).getText();
			logger.info("Default Billing address is "+DEFAULT_BILLING_ADDRESSES_LOC);
			return defaultBillingAddress;
		}catch(Exception e){
			driver.waitForElementPresent(DEFAULT_BILLING_ADDRESSES_LOC);
			String defaultBillingAddress = driver.findElement(DEFAULT_BILLING_ADDRESSES_LOC).getText();
			logger.info("Default Billing address is "+DEFAULT_BILLING_ADDRESSES_LOC);
			return defaultBillingAddress;
		}
	}

	public void clickAddNewBillingProfileLink() throws InterruptedException{
		driver.waitForElementPresent(ADD_NEW_BILLING_LINK_LOC);
		driver.click(ADD_NEW_BILLING_LINK_LOC);
		logger.info("Add new billing profile link clicked");
	}

	public void enterNewBillingCardNumber(String cardNumber){
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.id("card-nr"));		
		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		js.executeScript("$('#card-nr-masked').hide();$('#card-nr').show(); ", driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC));
		driver.pauseExecutionFor(2000);
		driver.type((ADD_NEW_BILLING_CARD_NUMBER_LOC),cardNumber);
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
		driver.click(ADD_NEW_BILLING_CARD_ADDRESS_DD_LOC);
		logger.info("New billing card address drop down clicked");
		driver.waitForElementPresent(ADD_NEW_BILLING_CARD_ADDRESS_DD_FIRST_VALUE_LOC);
		driver.click(ADD_NEW_BILLING_CARD_ADDRESS_DD_FIRST_VALUE_LOC);
		logger.info("New billing card address selected");
	}

	public void selectUseThisBillingProfileFutureAutoshipChkbox(){
		driver.waitForElementPresent(USE_THIS_BILLING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC);
		driver.click(USE_THIS_BILLING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC);
		logger.info("Checkbox for Use this billing profile for future autoship selected");
	}

	public void clickOnSaveBillingProfile() throws InterruptedException{
		driver.waitForElementPresent(NEW_BILLING_PROFILE_SAVE_BTN_LOC);
		driver.click(NEW_BILLING_PROFILE_SAVE_BTN_LOC);
		driver.waitForLoadingImageToDisappear();
		logger.info("save billing profile button clicked");
	}

	public void makeBillingProfileDefault(String firstName) throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+firstName+"')]/following::span[@class='radio-button billtothis'][1]/input/.."));
		driver.click(By.xpath("//span[contains(text(),'"+firstName+"')]/following::span[@class='radio-button billtothis'][1]/input/.."));
		logger.info("default billing profile selected has the name "+firstName);
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnEditBillingProfile() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//input[@checked='checked']/preceding::p[1]/a"));
		driver.click(By.xpath("//div[@id='multiple-billing-profiles']//input[@checked='checked']/preceding::p[1]/a"));
		driver.waitForPageLoad();
		logger.info("Edit billing profile link clicked");
	}

	public void clickOnEditOfFirstBillingProfile(){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div[1]//a[contains(text(),'Edit')]"));
		driver.click(By.xpath("//div[@id='multiple-billing-profiles']/div[1]//a[contains(text(),'Edit')]"));
		driver.waitForPageLoad();
		logger.info("First billing profile's edit link clicked");
	}

	public boolean isFirstBillingProfileIsDefault(){
		return driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div[1]//input[@name='bill-card']")).isSelected();
	}

	public String getBillingProfileName(){
		driver.waitForElementPresent(BILLING_PROFILE_NAME_LOC);
		logger.info("new billing profile name is "+driver.findElement(BILLING_PROFILE_NAME_LOC).getText());
		return driver.findElement(BILLING_PROFILE_NAME_LOC).getText();
	}

	public boolean isAutoshipOrderAddressTextPresent(String firstName){
		try{
			driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+firstName+"')]/ancestor::div[1]//b[@class='AutoshipOrderAddress' and text()='Autoship Order Address']"));
			driver.findElement(By.xpath("//span[contains(text(),'"+firstName+"')]/ancestor::div[1]//b[@class='AutoshipOrderAddress' and text()='Autoship Order Address']"));   
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public boolean validateBillingProfileUpdated(){
		driver.waitForElementPresent(By.xpath("//div[@class='successMessage']/span"));
		return driver.isElementPresent(By.xpath("//div[@class='successMessage']/span"));
	}
	
}
