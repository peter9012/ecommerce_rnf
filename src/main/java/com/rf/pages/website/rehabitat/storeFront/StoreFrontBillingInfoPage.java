package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontBillingInfoPage extends StoreFrontWebsiteBasePage{

	public StoreFrontBillingInfoPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontBillingInfoPage.class.getName());

	private final By ADD_NEW_SHIPPING_ADDRESS_LOC = By.xpath("//a[contains(text(),'Add new shipping address')]");
	private final By SAVE_BUTTON_OF_SHIPPING_ADDRESS_LOC = By.xpath("//button[contains(text(),'Save')]");
	private final By CANCEL_BUTTON_OF_SHIPPING_ADDRESS_LOC = By.xpath("//div[@class='accountActions']//a");
	private final By ERROR_MESSAGE_FOR_FIRST_LAST_NAME_LOC = By.id("address.firstName-error");
	private final By DEFAULT_SHIPPING_ADDRESS_NAME_LOC = By.xpath("//strong[contains(text(),'Default')]");
	private final By ERROR_MESSAGE_FOR_ADDRESS_LINE_1_LOC = By.id("address.line1-error");
	private final By ERROR_MESSAGE_FOR_CITY_LOC = By.id("address.townCity-error");
	private final By ERROR_MESSAGE_FOR_STATE_LOC = By.id("address.region-error");
	private final By ERROR_MESSAGE_FOR_POSTAL_CODE_LOC = By.id("address.postcode-error");
	private final By ERROR_MESSAGE_FOR_PHONE_NUMBER_LOC = By.id("address.phone-error");
	private String shippingProfileNameLoc  = "//div[contains(@class,'account-addressbook')]/descendant::strong[contains(text(),'%s')][1]";
	
	/***
	 * This method clicked on add a new shipping address link 
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontBillingInfoPage clickAddANewShippingAddressLink(){
		driver.click(ADD_NEW_SHIPPING_ADDRESS_LOC);
		logger.info("Add new shipping address link clicked");
		return this;
	}


	/***
	 * This method click the save button of shipping address
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontBillingInfoPage clickSaveButtonOfShippingAddress(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SAVE_BUTTON_OF_SHIPPING_ADDRESS_LOC));
		logger.info("Save button clicked");
		return this;
	}

	/***
	 * This method validates that shipping profile is present or not
	 * at shipping info page 
	 * 
	 * @param profile name
	 * @return boolean value
	 * 
	 */
	public boolean isShippingProfilePresent(String profileName){
		return driver.isElementPresent(By.xpath(String.format(shippingProfileNameLoc, profileName)));
	}

	/***
	 * This method validates that shipping address details field is present or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isShippingAddressDetailFieldsPresent(String name){
		try{
			driver.pauseExecutionFor(1000);
			driver.type(FIRST_LAST_NAME_FOR_SHIPPING_DETAILS_LOC, name);
			return true;
		}catch(Exception e){
			return false;
		}
	}


	/***
	 * This method click the cancel button of shipping address
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontBillingInfoPage clickCancelButtonOfShippingAddress(){
		driver.click(CANCEL_BUTTON_OF_SHIPPING_ADDRESS_LOC);
		logger.info("Cancel button clicked");
		return this;
	}

	/***
	 * This method validates the error message for first & last name field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForFirstAndLastName(){
		return driver.findElement(ERROR_MESSAGE_FOR_FIRST_LAST_NAME_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for address line 1 field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForAddressLine1(){
		return driver.findElement(ERROR_MESSAGE_FOR_ADDRESS_LINE_1_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for city field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForCity(){
		return driver.findElement(ERROR_MESSAGE_FOR_CITY_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for state field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForState(){
		return driver.findElement(ERROR_MESSAGE_FOR_STATE_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for postal code field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForPostalCode(){
		return driver.findElement(ERROR_MESSAGE_FOR_POSTAL_CODE_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for phone number field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForPhoneNumber(){
		return driver.findElement(ERROR_MESSAGE_FOR_PHONE_NUMBER_LOC).isDisplayed();
	}

	/***
	 * This method get the default shipping profile name
	 * 
	 * @param
	 * @return profile name
	 * 
	 */
	public String getDefaultShippingAddressName(){
		driver.pauseExecutionFor(2000);
		String profileName = driver.findElement(DEFAULT_SHIPPING_ADDRESS_NAME_LOC).getText();
		logger.info("default profile name is "+profileName);
		return profileName;
	}
	
}

