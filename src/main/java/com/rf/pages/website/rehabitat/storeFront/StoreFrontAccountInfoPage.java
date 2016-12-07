package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

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
	private final By EMAIL_LOC = By.id("profile.email");
	private final By OLD_PASSWORD_LOC = By.id("profile.currentPassword");
	private final By NEW_PASSWORD_LOC = By.id("profile.newPassword");
	private final By CONFIRM_PASSWORD_LOC = By.id("profile.checkNewPassword");
	private final By USERNAME_FIELD_LOC = By.xpath("//input[@class='text-input valid']");
	private final By SAVE_BUTTON_ACCOUNT_INFO_PAGE_LOC = By.id("accountFormBtn");
	private String emailValidationErrorLoc ="//*[@id='profile.email']/following::label[contains(text(),'%s')]";
	private String newPasswordValidationErrorLoc ="//*[@id='profile.newPassword']/following::label[contains(text(),'%s')]";
	private String confirmPasswordValidationErrorLoc ="//*[@id='profile.checkNewPassword']/following::label[contains(text(),'%s')]";
	private String firstNameValidationErrorLoc ="//*[@id='profile.firstName']/following::label[contains(text(),'%s')][1]";
	private String lastNameValidationErrorLoc = "//*[@id='profile.lastName']/following::label[contains(text(),'%s')][1]";
	private String addressLineValidationErrorLoc = "//*[@id='profile.line1']/following::label[contains(text(),'%s')][1]";
	private String cityValidationErrorLoc = "//*[@id='profile.townCity']/following::label[contains(text(),'%s')][1]";
	private String postalValidationErrorLoc = "//*[@id='profile.postcode']/following::label[contains(text(),'%s')][1]";
	private String phoneNumberValidationErrorLoc = "//*[@id='profile.phone1']/following::label[contains(text(),'%s')][1]";

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

	/***
	 * This method enter email address in the email field on account info page 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterEmail(String email){
		driver.type(EMAIL_LOC,email);
		logger.info("entered email as "+email);
		return this;
	}

	/***
	 * This method enter password in the old password field on account info page 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterOldPassword(String password){
		driver.type(OLD_PASSWORD_LOC,password);
		logger.info("entered old password as "+password);
		return this;
	}

	/***
	 * This method enter password in the new password field on account info page 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterNewPassword(String password){
		driver.type(NEW_PASSWORD_LOC,password);
		logger.info("entered new password as "+password);
		return this;
	}

	/***
	 * This method enter password in the confirm password field on account info page 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterConfirmPassword(String password){
		driver.type(CONFIRM_PASSWORD_LOC,password);
		logger.info("entered confirm password as "+password);
		return this;
	}

	/***
	 * This method verifies the presence of error message for a particular field on account info page 
	 * 
	 * @param
	 * @return Boolean
	 * 
	 */
	public Boolean isValidationMsgPresentForParticularField(String field,String errorMsg){
		Boolean isValidationMsgPresentForParticularField = null;
		if(field.equalsIgnoreCase("email"))
			isValidationMsgPresentForParticularField = driver.isElementPresent(By.xpath(String.format(emailValidationErrorLoc,errorMsg))); 
		else if(field.equalsIgnoreCase("new password"))
			isValidationMsgPresentForParticularField = driver.isElementPresent(By.xpath(String.format(newPasswordValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("confirm password"))
			isValidationMsgPresentForParticularField = driver.isElementPresent(By.xpath(String.format(confirmPasswordValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("First Name"))
			isValidationMsgPresentForParticularField = driver.isElementPresent(By.xpath(String.format(firstNameValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("Last Name"))
			isValidationMsgPresentForParticularField = driver.isElementPresent(By.xpath(String.format(lastNameValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("address1"))
			isValidationMsgPresentForParticularField = driver.isElementPresent(By.xpath(String.format(addressLineValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("city"))
			isValidationMsgPresentForParticularField = driver.isElementPresent(By.xpath(String.format(cityValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("postal"))
			isValidationMsgPresentForParticularField = driver.isElementPresent(By.xpath(String.format(postalValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("phone"))
			isValidationMsgPresentForParticularField = driver.isElementPresent(By.xpath(String.format(phoneNumberValidationErrorLoc,errorMsg)));
		return isValidationMsgPresentForParticularField;
	}

	/***
	 * This method clear all fields on account info page 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage clearAllFields(){
		driver.findElement(FIRST_NAME_LOC).clear();
		logger.info("First name field clear");
		driver.findElement(LAST_NAME_LOC).clear();
		logger.info("Last name field clear");
		driver.findElement(ADDRESS_LINE_LOC).clear();
		logger.info("Address line field clear");
		driver.findElement(CITY_LOC).clear();
		logger.info("City field clear");
		driver.findElement(POSTAL_CODE_LOC).clear();
		logger.info("Postal code field clear");
		driver.findElement(MAIN_PHONE_NUMBER_LOC).clear();
		logger.info("Main phone number field clear");
		driver.findElement(EMAIL_LOC).clear();
		logger.info("Email field clear");
		driver.pauseExecutionFor(2000);
		return this;
	}

}