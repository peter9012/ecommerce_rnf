package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontAccountInfoPage extends StoreFrontWebsiteBasePage{

	public StoreFrontAccountInfoPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontAccountInfoPage.class.getName());

	private final By FIRST_NAME_LOC = By.id("profile.firstName");
	private final By LAST_NAME_LOC = By.id("profile.lastName");
	private final By ADDRESS_LINE_LOC = By.id("profile.line1");
	private final By CITY_LOC = By.id("profile.townCity");
	private final By POSTAL_CODE_LOC = By.id("profile.postcode");
	private final By MAIN_PHONE_NUMBER_LOC = By.id("profile.phone1");
	private final By USERNAME_FIELD_LOC = By.xpath("//input[@class='text-input valid']");
	private final By SAVE_BUTTON_ACCOUNT_INFO_PAGE_LOC = By.id("accountFormBtn");

	private String socialMediaIconLoc = "//div[@class='container']";

	/***
	 * This method verify first Name field on account info page. 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isFirstNameFieldPresent(){
		return driver.findElement(FIRST_NAME_LOC).isDisplayed();
	}

	/***
	 * This method verify Last Name field on account info page. 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isLastNameFieldPresent(){
		return driver.findElement(LAST_NAME_LOC).isDisplayed();
	}

	/***
	 * This method verify Address field on account info page. 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isAddressFieldPresent(){
		return driver.findElement(ADDRESS_LINE_LOC).isDisplayed();
	}

	/***
	 * This method verify city field on account info page. 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isCityFieldPresent(){
		return driver.findElement(CITY_LOC).isDisplayed();
	}

	/***
	 * This method verify Postal field on account info page. 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isPostalFieldPresent(){
		return driver.findElement(POSTAL_CODE_LOC).isDisplayed();
	}

	/***
	 * This method verify main phone number field on account info page. 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isMainPhoneNumberFieldPresent(){
		return driver.findElement(MAIN_PHONE_NUMBER_LOC).isDisplayed();
	}

	/***
	 * This method enter Updated Username on account info page. 
	 * 
	 * @param
	 * @return account info page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterUserName(String userName){
		driver.type(USERNAME_FIELD_LOC, userName);
		logger.info("Entered username is "+userName);
		return this;
	}

	/***
	 * This method click save button on account info page. 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage saveAccountInfo(){
		driver.click(SAVE_BUTTON_ACCOUNT_INFO_PAGE_LOC);
		logger.info("Save button clicked on account info page.");
		return this;
	}

}

