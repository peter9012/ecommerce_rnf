package com.rf.pages.website.storeFront;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

public class StoreFrontShippingInfoPage extends StoreFrontRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontShippingInfoPage.class.getName());

	private final By SHIPPING_PAGE_TEMPLATE_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top']/div[text()='Shipping info']");  
	private final By TOTAL_SHIPPING_ADDRESSES_LOC = By.xpath("//div[@id='multiple-billing-profiles']/div[contains(@class,'col')]");
	private final By USE_THIS_SHIPPING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC = By.xpath("//div[@id='use-for-autoship']/div");
	private final By NEW_SHIPPING_PROFILE_SAVE_BTN_LOC = By.id("saveShippingAddress");
	private final By ADD_NEW_SHIPPING_LINK_LOC = By.xpath("//a[@class='add-new-shipping-address']");


	public StoreFrontShippingInfoPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public boolean verifyShippingInfoPageIsDisplayed(){
		driver.waitForElementPresent(SHIPPING_PAGE_TEMPLATE_HEADER_LOC);
		return driver.getCurrentUrl().contains(TestConstants.SHIPPING_PAGE_SUFFIX_URL);
	}

	public boolean isDefaultAddressRadioBtnSelected(String defaultAddressFirstName) throws InterruptedException{
		try{
			driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+defaultAddressFirstName+"')]/ancestor::div[1]/form/span/input[@checked='checked']"));
			boolean flag=driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstName+"')]/ancestor::div[1]/form/span/input[@checked='checked']")).isSelected();
			return flag;
		}catch(NoSuchElementException e){
			//String word = Character.toUpperCase(defaultAddressFirstName.charAt(0)) + defaultAddressFirstName.substring(1);
			if(driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstName+"')]/ancestor::div[1]/form/span[@checked='address.defaultAddress']")).isSelected()){
				return true;
			}else{
				return false;
			}
		}
	}

	public boolean isAutoshipOrderAddressTextPresent(String firstName){
		String getAutoshipOrderDetailsText = driver.findElement(By.xpath("//div[@class='autoshipOrderDetails']")).getText();
		logger.info("Autoship address details "+getAutoshipOrderDetailsText);
		return getAutoshipOrderDetailsText.contains(firstName);
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
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div[1]//a[text()='Edit']"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div[1]//a[text()='Edit']")));
		// driver.click(By.xpath("//div[@id='multiple-billing-profiles']/div[1]//a[text()='Edit']"));
		logger.info("First Address Edit link clicked");
		driver.waitForLoadingImageToDisappear();

	}

	public void clickAddNewShippingProfileLink() throws InterruptedException{
		driver.waitForElementPresent(ADD_NEW_SHIPPING_LINK_LOC);
		driver.click(ADD_NEW_SHIPPING_LINK_LOC);
		logger.info("Add new shipping profile link clicked");
	}

	public void enterNewShippingAddressName(String name){
		//		driver.waitForElementPresent(By.id("new-attention"));
		//		driver.clear(By.id("new-attention"));
		driver.type(By.id("new-attention"),name);
		logger.info("New Shipping Address name is "+name);
	}

	public void enterNewShippingAddressCity(String city){
		//		driver.waitForElementPresent(By.id("townCity"));
		//		driver.clear(By.id("townCity"));
		driver.type(By.id("townCity"),city);
		logger.info("New Shipping City is "+city);
	}

	public void selectNewShippingAddressState(String state){
		//		driver.waitForElementPresent(By.id("state"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("state")));		
		//		driver.click(By.id("state"));
		driver.pauseExecutionFor(1000);
		//		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[contains(text(),'"+state+"')]"));
		driver.click(By.xpath("//select[@id='state']/option[contains(text(),'"+state+"')]"));
		logger.info("State/Province selected");
	}

	public void enterNewShippingAddressPostalCode(String postalCode){
		//		driver.waitForElementPresent(By.id("postcode"));
		//		driver.clear(By.id("postcode"));
		driver.type(By.id("postcode"),postalCode);
	}

	public void enterNewShippingAddressPhoneNumber(String phoneNumber){
		driver.waitForElementPresent(By.id("phonenumber"));
		driver.type(By.id("phonenumber"),phoneNumber);
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
		driver.clear(By.id("security-code"));
		driver.type(By.id("security-code"),securityCode);
	}

	public void selectUseThisShippingProfileFutureAutoshipChkbox(){
		driver.pauseExecutionFor(2000);
		driver.click(USE_THIS_SHIPPING_PROFILE_FUTURE_AUTOSHIP_CHKBOX_LOC);
	}

	public void clickOnSaveShippingProfile() throws InterruptedException{
		driver.waitForElementPresent(NEW_SHIPPING_PROFILE_SAVE_BTN_LOC);
		driver.click(NEW_SHIPPING_PROFILE_SAVE_BTN_LOC);
		logger.info("New Shipping prifile save button clicked");
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.id("QAS_RefineBtn"));
			driver.click(By.id("QAS_RefineBtn"));
		}catch(Exception e){

		}
		driver.waitForLoadingImageToDisappear();
		//driver.pauseExecutionFor(3000);
		driver.waitForPageLoad();
	}

	public void makeShippingProfileAsDefault(String firstName) throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+firstName+"')]/following::form[@id='setDefaultAddressForm'][1]/span[1]"));
		driver.click(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+firstName+"')]/following::form[@id='setDefaultAddressForm'][1]/span[1]"));
		logger.info("Default shipping profile selected is having name "+firstName);
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='YES, UPDATE MY AUTO-SHIP']"));
			driver.click(By.xpath("//input[@value='YES, UPDATE MY AUTO-SHIP']"));
		}catch(Exception e){

		}
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isShippingAddressPresentOnShippingPage(String firstName){
		boolean isFirstNamePresent = false;
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div[contains(@class,'sel-profile')]"));
		List<WebElement> allBillingProfiles = driver.findElements(By.xpath("//div[@id='multiple-billing-profiles']/div[contains(@class,'sel-profile')]"));  
		for(int i=1;i<=allBillingProfiles.size();i++){   
			isFirstNamePresent = driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div["+i+"]/p/span[1]")).getText().toLowerCase().contains(firstName.toLowerCase());
			if(isFirstNamePresent == true){    
				return true;
			}
		}
		return false;
	}

	public String getAddressUpdateConfirmationMessageFromUI(){
		return driver.findElement(By.xpath(".//div[@id='globalMessages']//p")).getText();

	}

	public String getErrorMessageForUSAddressFromUI(){
		//  return driver.findElement(By.xpath("//div[@class='tipsy-inner']")).getText();
		System.out.println(driver.findElement(By.xpath("//input[@id='postcode']/following::label")).getText());
		return driver.findElement(By.xpath("//input[@id='postcode']/following::label")).getText();  
	}

	public void changeMainAddressToQuebec(){
		driver.waitForElementPresent(By.id("state"));
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[@value='QC']"));
		driver.click(By.xpath("//select[@id='state']/option[@value='QC']"));
		logger.info("state selected is quebec");
	}

	public void changeAddressToUSAddress() throws InterruptedException{
		driver.waitForElementPresent(By.id("new-address-1"));
		driver.clear(By.id("new-address-1"));
		driver.type(By.id("new-address-1"),TestConstants.ADDRESS_LINE_1_US);
		logger.info("Address line 1 entered is "+TestConstants.ADDRESS_LINE_1_US);
		driver.clear(By.id("townCity"));
		driver.type(By.id("townCity"),TestConstants.CITY_US);
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
		driver.click(By.xpath("//select[@id='state']/option[2]"));
		logger.info("state selected");
		driver.clear(By.id("postcode"));
		driver.type(By.id("postcode"),TestConstants.POSTAL_CODE_US);
		logger.info("postal code entered is "+TestConstants.POSTAL_CODE_US);
		driver.clear(By.id("phonenumber"));
		driver.type(By.id("phonenumber"),TestConstants.PHONE_NUMBER_US);
		logger.info("phone number entered is "+TestConstants.PHONE_NUMBER_US);
	}	

	public void clickOnNewAddressRadioButton(){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div[2]//input"));
		driver.click(By.xpath("//div[@id='multiple-billing-profiles']/div[2]//input"));
		logger.info("new address radio button clicked");
	}

	public void clickOnPopUpAfterClickingRadioButton(){
		driver.waitForElementPresent(By.xpath("//div[@id='popup-quickinfo']/div/input[@value='YES, UPDATE MY AUTO-SHIP']"));
		driver.click(By.xpath("//div[@id='popup-quickinfo']/div/input[@value='YES, UPDATE MY AUTO-SHIP']"));
		logger.info("clicked on YES, UPDATE MY AUTO-SHIP Button");
	}

	public boolean verifyChangeInDefaultAddressForShippingAddress(){
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']//span[text()='Failed to change default address.']"));
		logger.info(driver.findElement(By.xpath("//div[@id='main-content']//span[text()='Failed to change default address.']")).getText());
		if(driver.isElementPresent(By.xpath("//div[@id='main-content']//span[text()='Failed to change default address.']"))){
			return true;}
		else{
			return false;
		}
	}

	public boolean verifyOldDefaultSelectAddress(String addressname, String addressnameAfterAdd){
		return addressname.equalsIgnoreCase(addressnameAfterAdd);
	}

	public boolean verifyRadioButtonNotSelectedByDefault(String name){
		return driver.isElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+name+"')]/following::input[@checked='checked']"));
	}

	public boolean verifyRadioButtonIsSelectedByDefault(String name) {
		String shippingname = Character.toUpperCase(name.charAt(0)) + name.substring(1);
		return driver.isElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+shippingname+"')]/following::input[@checked='checked']"));
	}

	public boolean validateUpdateAutoShipPopUpPresent(){
		try{
			driver.quickWaitForElementPresent(By.xpath("//div[@id='popup-quickinfo']"));
			boolean status= driver.isElementPresent(By.xpath("//div[@id='popup-quickinfo']"));
			driver.click(By.xpath("//input[@class='shippingAddresspopup btn btn-primary']"));
			driver.waitForLoadingImageToDisappear();
			return status;
		}catch(Exception e){
			return driver.isElementPresent(By.xpath("//div[@class='successMessage']/span"));
		}
	}

	public boolean isQuebecProvinceDisabled(){
		driver.waitForElementPresent(By.xpath("//option[contains(text(),'Quebec')][@disabled='disabled']"));
		return driver.isElementPresent(By.xpath("//option[contains(text(),'Quebec')][@disabled='disabled']"));
	}

	public void clickSelectStateDD(){
		driver.waitForElementPresent(By.xpath("//div[@id='start-new-shipping-address']//select[@id='state']"));
		driver.click(By.xpath("//div[@id='start-new-shipping-address']//select[@id='state']"));
	}
}