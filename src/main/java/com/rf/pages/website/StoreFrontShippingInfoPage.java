package com.rf.pages.website;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

public class StoreFrontShippingInfoPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontShippingInfoPage.class.getName());

	private final By SHIPPING_PAGE_TEMPLATE_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top' and text()='Shipping info']");  
	private final By TOTAL_SHIPPING_ADDRESSES_LOC = By.xpath("//ul[@id='multiple-billing-profiles']/li");
	private final By USE_THIS_SHIPPING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC = By.xpath("//div[@id='use-for-autoship']/div");
	private final By NEW_SHIPPING_PROFILE_SAVE_BTN_LOC = By.id("saveShippingAddress");
	private final By ADD_NEW_SHIPPING_LINK_LOC = By.linkText("Add a new shipping address»");


	public StoreFrontShippingInfoPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public boolean verifyShippingInfoPageIsDisplayed(){
		driver.waitForElementPresent(SHIPPING_PAGE_TEMPLATE_HEADER_LOC);
		return driver.getCurrentUrl().contains(TestConstants.SHIPPING_PAGE_SUFFIX_URL);
	}

	public boolean isDefaultAddressRadioBtnSelected(String defaultAddressFirstNameDB) throws InterruptedException{
		return driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstNameDB+"')]/ancestor::li[1]/form/span/input")).isSelected();
	}

	public boolean isDefaultShippingAddressSelected(String name) throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@name='addressCode' and @checked='checked']/ancestor::li[1]/p[1]"));
		return driver.findElement(By.xpath("//input[@name='addressCode' and @checked='checked']/ancestor::li[1]/p[1]")).getText().contains(name);
	}	


	public boolean verifyAutoShipAddressTextNextToDefaultRadioBtn(String defaultAddressFirstNameDB){
		return driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstNameDB+"')]/following::b[@class='AutoshipOrderAddress']")).getText().equals(TestConstants.AUTOSHIP_ADRESS_TEXT);
	}

	public int getTotalShippingAddressesDisplayed(){
		driver.waitForElementPresent(TOTAL_SHIPPING_ADDRESSES_LOC);
		List<WebElement> totalShippingAddressesDisplayed = driver.findElements(TOTAL_SHIPPING_ADDRESSES_LOC);
		return totalShippingAddressesDisplayed.size();
	}

	public void clickOnEditForFirstAddress(){
		driver.waitForElementPresent(By.xpath("//ul[@id='multiple-billing-profiles']//li[1]//a[text()='Edit']"));
		driver.click(By.xpath("//ul[@id='multiple-billing-profiles']//li[1]//a[text()='Edit']"));
		logger.info("First Address Edit link clicked");

	}

	public void clickAddNewShippingProfileLink() throws InterruptedException{
		driver.waitForElementPresent(ADD_NEW_SHIPPING_LINK_LOC);
		driver.click(ADD_NEW_SHIPPING_LINK_LOC);
		logger.info("Add new shipping profile link clicked");
	}

	public void enterNewShippingAddressName(String name){
		driver.waitForElementPresent(By.id("new-attention"));
		driver.findElement(By.id("new-attention")).clear();
		driver.findElement(By.id("new-attention")).sendKeys(name);
		logger.info("New Shipping Address name is "+name);
	}

	public void enterNewShippingAddressLine1(String addressLine1){
		driver.findElement(By.id("new-address-1")).clear();
		driver.findElement(By.id("new-address-1")).sendKeys(addressLine1);
		logger.info("New Shipping Address is "+addressLine1);
	}

	public void enterNewShippingAddressCity(String city){
		driver.findElement(By.id("townCity")).clear();
		driver.findElement(By.id("townCity")).sendKeys(city);
		logger.info("New Shipping City is "+city);
	}

	public void selectNewShippingAddressState(){
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
		driver.click(By.xpath("//select[@id='state']/option[2]"));
		logger.info("State/Province selected");
	}

	public void enterNewShippingAddressPostalCode(String postalCode){
		driver.findElement(By.id("postcode")).clear();
		driver.findElement(By.id("postcode")).sendKeys(postalCode);
	}

	public void enterNewShippingAddressPhoneNumber(String phoneNumber){
		driver.findElement(By.id("phonenumber")).clear();
		driver.findElement(By.id("phonenumber")).sendKeys(phoneNumber);
	}

	public void selectFirstCardNumber() throws InterruptedException{
		try{
			driver.waitForElementPresent(By.id("cardDropDowndropdown"));
			driver.click(By.id("cardDropDowndropdown"));

		}catch(WebDriverException e){
			Actions action = new Actions(RFWebsiteDriver.driver);
			action.moveToElement(driver.findElement(By.id("cardDropDowndropdown"))).click().build().perform();

		}
		driver.waitForElementPresent(By.xpath("//select[@id='cardDropDowndropdown']/option[2]"));
		driver.click(By.xpath("//select[@id='cardDropDowndropdown']/option[2]"));
		logger.info("First Card number selected from drop down");
	}

	public void enterNewShippingAddressSecurityCode(String securityCode){
		driver.findElement(By.id("security-code")).clear();
		driver.findElement(By.id("security-code")).sendKeys(securityCode);
	}

	public void selectUseThisShippingProfileFutureAutoshipChkbox(){
		driver.click(USE_THIS_SHIPPING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC);
	}

	public void clickOnSaveShippingProfile() throws InterruptedException{
		driver.waitForElementPresent(NEW_SHIPPING_PROFILE_SAVE_BTN_LOC);
		driver.click(NEW_SHIPPING_PROFILE_SAVE_BTN_LOC);
		logger.info("New Shipping prifile save button clicked");
		try{
			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
			driver.click(By.id("QAS_AcceptOriginal"));
		}catch(NoSuchElementException e){

		}
		driver.waitForLoadingImageToDisappear();
	}

	public void makeShippingProfileAsDefault(String firstName) throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//ul[@id='multiple-billing-profiles']//span[contains(text(),'"+firstName+"')]/following::form[@id='setDefaultAddressForm'][1]/span[1]"));
		driver.click(By.xpath("//ul[@id='multiple-billing-profiles']//span[contains(text(),'"+firstName+"')]/following::form[@id='setDefaultAddressForm'][1]/span[1]"));
		logger.info("Default shipping profile selected is having name "+firstName);
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@class='shippingAddresspopup']"));
			driver.click(By.xpath("//input[@class='shippingAddresspopup']"));
		}catch(Exception e){

		}
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isShippingAddressPresentOnShippingPage(String firstName){
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

	public boolean isAutoshipOrderAddressTextPresent(String firstName){
		try{
			driver.findElement(By.xpath("//span[contains(text(),'"+StringUtils.uncapitalize(firstName)+"')]/ancestor::li[1]//b[@class='AutoshipOrderAddress' and text()='Autoship Order Address']"));			
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

}


