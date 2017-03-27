package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontAccountInfoPage extends StoreFrontWebsiteBasePage{

	public StoreFrontAccountInfoPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontAccountInfoPage.class.getName());

	private final By DISABLED_SEND_BUTTON_EMAIL_TO_CONSULTANT_LOC= By.xpath("//button[@id='emailToConsultantSubmitButton'][@disabled='']");
	private final By FIRST_NAME_LOC = By.id("profile.firstName");
	private final By LAST_NAME_LOC = By.id("profile.lastName");
	private final By ADDRESS_LINE_LOC = By.id("profile.line1");
	private final By ADDRESS_LINE_2_LOC = By.id("profile.line2");
	private final By CITY_LOC = By.id("profile.townCity");
	private final By POSTAL_CODE_LOC = By.id("profile.postcode");
	private final By MAIN_PHONE_NUMBER_LOC = By.id("profile.phone1");
	private final By PHONE_NUMBER_2_LOC = By.id("profile.phone2");
	private final By DAY_OF_BIRTH_LOC = By.xpath("//select[contains(@class,'day')]");
	private final By MONTH_OF_BIRTH_LOC = By.xpath("//select[contains(@class,'month')]");
	private final By YEAR_OF_BIRTH_LOC = By.xpath("//select[contains(@class,'year')]");
	private final By EMAIL_LOC = By.id("profile.email");
	private final By OLD_PASSWORD_LOC = By.id("profile.currentPassword");
	private final By NEW_PASSWORD_LOC = By.id("profile.newPassword");
	private final By CONFIRM_PASSWORD_LOC = By.id("profile.checkNewPassword");
	private final By USERNAME_FIELD_LOC = By.xpath("//input[@class='text-input valid']");
	private final By SAVE_BUTTON_ACCOUNT_INFO_PAGE_LOC = By.id("accountFormBtn");
	private final By SPOUSE_CHECKBOX_ALREADY_CHECKED_LOC = By.xpath("//input[@id='agree-spouse' and @checked='checked']/following-sibling::label");
	private final By SPOUSE_CHECKBOX_LOC = By.xpath("//input[@id='agree-spouse']/following-sibling::label");
	private final By ACCEPT_BTN_SPOUSE_POPUP_LOC = By.id("accept-spouse");
	private final By SPOUSE_FIRST_NAME_LOC = By.id("profile.spouseFirstname");
	private final By SPOUSE_LAST_NAME_LOC = By.id("profile.spouseLastname");
	private final By SPOUSE_EMAIL_LOC = By.id("profile.spouseEmail");
	private final By SPOUSE_PHONE_NUMBER_LOC = By.id("profile.spousePhoneNumber");
	private final By SPOUSE_DETAIL_POPUP_YES_BTN_LOC = By.xpath("//div[@id='cboxLoadedContent']/descendant::button[@id='suggestedAddress']");
	private final By SPOUSE_DETAIL_POPUP_USE_AS_ENTERED_BTN_LOC = By.xpath("//div[@id='cboxLoadedContent']/descendant::button[@id='oldAddress']");
	private final By PROFILE_UPDATION_MESSAGE_LOC = By.xpath("//div[@class='global-alerts']/div");
	private final By STATE_DD_LOC = By.id("profile.region");
	private final By EMAIL_YOUR_CONSULTANT_LOC = By.xpath("//a[text()='Email your Consultant']");
	private final By EMAIL_TO_CONSULTANT_NAME_LOC= By.id("emailToConsultantForm.name");
	private final By EMAIL_TO_CONSULTANT_EMAIL_LOC= By.id("emailToConsultantForm.emailId");
	private final By EMAIL_TO_CONSULTANT_EMAIL_CONTENT_LOC= By.id("emailToConsultantForm.emailContent");
	private final By EMAIL_TO_CONSULTANT_EMAIL_SUBMIT_BTN_LOC= By.id("emailToConsultantSubmitButton");
	private final By SPONSER_NAME_ON_ACCOUNT_INFO_LOC = By.xpath("//p[text()='R+F Independent Consultant']/preceding-sibling::p[1]");

	private String spouseFirstNameValidationErrorLoc = " //*[@id='profile.spouseFirstname']/../label[contains(text(),'%s')][1]";
	private String spouseLastNameValidationErrorLoc = " //*[@id='profile.spouseLastname']/../label[contains(text(),'%s')][1]";
	private String stateForAccountDetails = "//select[@id='profile.region']//option[text()='%s']";
	private String emailValidationErrorLoc ="//*[@id='profile.email']/../label[contains(text(),'%s')]";
	private String currentPasswordValidationErrorLoc ="//*[@id='profile.currentPassword']/../label[contains(text(),'%s')]";
	private String newPasswordValidationErrorLoc ="//*[@id='profile.newPassword']/../label[contains(text(),'%s')]";
	private String confirmPasswordValidationErrorLoc ="//*[@id='profile.checkNewPassword']/../label[contains(text(),'%s')]";
	private String firstNameValidationErrorLoc ="//*[@id='profile.firstName']/../label[contains(text(),'%s')][1]";
	private String lastNameValidationErrorLoc = "//*[@id='profile.lastName']/../label[contains(text(),'%s')][1]";
	private String addressLineValidationErrorLoc = "//*[@id='profile.line1']/../label[contains(text(),'%s')][1]";
	private String cityValidationErrorLoc = "//*[@id='profile.townCity']/../label[contains(text(),'%s')][1]";
	private String postalValidationErrorLoc = "//*[@id='profile.postcode']/../label[contains(text(),'%s')][1]";
	private String phoneNumberValidationErrorLoc = "//*[@id='profile.phone1']/../label[contains(text(),'%s')][1]";
	private String genderRadioBtnLoc = "//input[@name='gender'][@value='%s']/..";
	private String emailYourConsultantValidationMsgLoc = "//label[@id='emailToConsultantForm.%s-error'][contains(text(),'%s')]";

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
		driver.pauseExecutionFor(3000);
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

	//	/***
	//	 * This method enter password in the old password field on account info page 
	//	 * 
	//	 * @param
	//	 * @return account Info Page object
	//	 * 
	//	 */
	//	public StoreFrontAccountInfoPage enterOldPassword(String password){
	//		driver.type(OLD_PASSWORD_LOC,password);
	//		logger.info("entered old password as "+password);
	//		return this;
	//	}

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
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(emailValidationErrorLoc,errorMsg))); 
		else if(field.equalsIgnoreCase("current password"))
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(currentPasswordValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("new password"))
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(newPasswordValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("confirm password"))
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(confirmPasswordValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("First Name"))
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(firstNameValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("Last Name"))
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(lastNameValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("address1"))
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(addressLineValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("city"))
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(cityValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("postal"))
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(postalValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("phone"))
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(phoneNumberValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("spouseFirstName"))
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(spouseFirstNameValidationErrorLoc,errorMsg)));
		else if(field.equalsIgnoreCase("spouseLastName"))
			isValidationMsgPresentForParticularField = driver.isElementVisible(By.xpath(String.format(spouseLastNameValidationErrorLoc,errorMsg)));
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
		//  driver.findElement(EMAIL_LOC).clear();
		//  logger.info("Email field clear");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method click checkbox for spouse details on account info page. 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage checkSpouseCheckbox(){
		if(driver.isElementPresent(SPOUSE_CHECKBOX_ALREADY_CHECKED_LOC)==false){
			driver.click(SPOUSE_CHECKBOX_LOC);
			logger.info("'Spouse' checkbox clicked on account info page.");
			driver.pauseExecutionFor(2000);
			driver.click(ACCEPT_BTN_SPOUSE_POPUP_LOC);
			logger.info("'ACCEPT' button clicked on spouse account access detail popup");
		}else{
			logger.info("'Spouse' checkbox already checked on account info page");
		}
		return this;
	}
	/***
	 * This method enter Spouse first name field on account info page 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterSpouseFirstName(String firstName){
		driver.type(SPOUSE_FIRST_NAME_LOC,firstName);
		logger.info("entered Spouse first Name as "+"'"+firstName+"'");
		return this;
	}
	/***
	 * This method enter Spouse last name field on account info page 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterSpouseLastName(String lastName){
		driver.type(SPOUSE_LAST_NAME_LOC,lastName);
		logger.info("entered Spouse last Name as "+"'"+lastName+"'");
		return this;
	}

	/***
	 * This method enter Spouse email field on account info page 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterSpouseEmail(String email){
		driver.type(SPOUSE_EMAIL_LOC,email);
		logger.info("entered Spouse email as "+"'"+email+"'");
		return this;
	}

	/***
	 * This method enter Spouse phone field on account info page 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterSpousePhoneNumber(String phoneNum){
		driver.type(SPOUSE_PHONE_NUMBER_LOC,phoneNum);
		logger.info("entered Spouse phoneNum as "+"'"+phoneNum+"'");
		return this;
	}

	/***
	 * This method validates Spouse details popup when entered spouse details and click save button on account info page.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isSpouseDetailsConfirmationPopUpPresent(){
		if(driver.findElement(SPOUSE_DETAIL_POPUP_YES_BTN_LOC).isDisplayed() && driver.findElement(SPOUSE_DETAIL_POPUP_USE_AS_ENTERED_BTN_LOC).isDisplayed()){
			return true;
		}else{
			return false;
		}
	}
	/***
	 * This method click use as entered on spouse details popup on account info page. 
	 * 
	 * @param
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage useEnteredDetailsOnSpouseDetailsPopUp(){
		try{
			driver.turnOffImplicitWaits(1);
			driver.click(SPOUSE_DETAIL_POPUP_USE_AS_ENTERED_BTN_LOC);
			logger.info("'USE AS ENTERED' Btn clicked on Spouse Detail popup at account info page.");
			driver.waitForPageLoad();
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){
			driver.turnOnImplicitWaits();
		}
		return this;
	}

	/***
	 * This method get profile Updation message on account info page.
	 * 
	 * @param
	 * @return profile updation message
	 * 
	 */
	public String getProfileUpdationMessage(){
		String []profileUpdationMessage = driver.findElement(PROFILE_UPDATION_MESSAGE_LOC).getText().split("×");
		logger.info("profile updation message is: "+profileUpdationMessage[0]);
		return profileUpdationMessage[1].trim();
	}

	public String getSpouseDetailsAfterSaving(String field){
		if(field.equalsIgnoreCase("firstName")){
			return driver.getAttribute(SPOUSE_FIRST_NAME_LOC, "value").trim().toLowerCase();
		}
		else if(field.equalsIgnoreCase("lastName")){
			return driver.getAttribute(SPOUSE_LAST_NAME_LOC, "value").trim().toLowerCase();
		}
		else if(field.equalsIgnoreCase("phoneNumber")){
			return driver.getAttribute(SPOUSE_PHONE_NUMBER_LOC, "value").trim().toLowerCase();
		}
		else if(field.equalsIgnoreCase("email")){
			return driver.getAttribute(SPOUSE_EMAIL_LOC, "value").trim().toLowerCase();
		}
		return null;
	}

	/***
	 * This method enter main Account info on Account Info Page
	 * 
	 * @param First name,Last name, address line1, city, state, postal code, phone number
	 * @return store front Account Info page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterMainAccountInfo(String firstName, String lastName, String addressLine1, String city, String state, String postal, String phoneNumber){
		driver.type(FIRST_NAME_LOC, firstName);
		logger.info("Entered first name as "+firstName);
		driver.type(LAST_NAME_LOC, lastName);
		logger.info("Entered last name as "+lastName);
		driver.type(ADDRESS_LINE_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(CITY_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForAccountDetails, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(MAIN_PHONE_NUMBER_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);
		return this;
	}

	/***
	 * This method enter main Account info on Account Info Page
	 * 
	 * @param First name,Last name, address line1, city, state, postal code, phone number
	 * @return store front Account Info page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterMainAccountInfo(String firstName, String lastName, String addressLine1, String addressLine2,String city, String state, String postal, String phoneNumber,String phoneNumber2
			,String email,String dayOfBirth,String monthOfBirth,String yearOfBirth,String gender){
		driver.type(FIRST_NAME_LOC, firstName);
		logger.info("Entered first name as "+firstName);
		driver.type(LAST_NAME_LOC, lastName);
		logger.info("Entered last name as "+lastName);
		driver.type(ADDRESS_LINE_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(ADDRESS_LINE_2_LOC, addressLine2);
		logger.info("Entered address line 2 as "+addressLine2);
		driver.type(CITY_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForAccountDetails, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(MAIN_PHONE_NUMBER_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);
		driver.type(PHONE_NUMBER_2_LOC, phoneNumber2);
		logger.info("Entered Phone number 2 as "+phoneNumber2);
		//  driver.type(EMAIL_LOC, email);
		//  logger.info("Entered email as "+email);
		Select dayOfBirthDD = new Select(driver.findElement(DAY_OF_BIRTH_LOC));
		Select monthOfBirthDD = new Select(driver.findElement(MONTH_OF_BIRTH_LOC));
		Select yearOfBirthDD = new Select(driver.findElement(YEAR_OF_BIRTH_LOC));
		dayOfBirthDD.selectByVisibleText(dayOfBirth);
		logger.info("selected day of birth as "+dayOfBirth);
		monthOfBirthDD.selectByVisibleText(monthOfBirth);
		logger.info("selected month of birth as "+monthOfBirth);
		yearOfBirthDD.selectByVisibleText(yearOfBirth);
		logger.info("selected year of birth as "+yearOfBirth);
		driver.click(By.xpath(String.format(genderRadioBtnLoc, gender.toUpperCase())));
		return this;
	}

	/***
	 * This method clear field mention in argument on account info page 
	 * 
	 * @param fieldName
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage clearFields(String fieldName){
		if(fieldName.equalsIgnoreCase("firstName")){
			driver.findElement(FIRST_NAME_LOC).clear();
			logger.info("First name field clear");
		}
		else if(fieldName.equalsIgnoreCase("lastName")){
			driver.findElement(LAST_NAME_LOC).clear();
			logger.info("Last name field clear");
		}
		else if(fieldName.equalsIgnoreCase("addressLine")){
			driver.findElement(ADDRESS_LINE_LOC).clear();
			logger.info("Address line field clear");
		}
		else if(fieldName.equalsIgnoreCase("city")){
			driver.findElement(CITY_LOC).clear();
			logger.info("City field clear");
		}
		else if(fieldName.equalsIgnoreCase("postalCode")){
			driver.findElement(POSTAL_CODE_LOC).clear();
			logger.info("Postal code field clear");
		}
		else if(fieldName.equalsIgnoreCase("phone")){
			driver.findElement(MAIN_PHONE_NUMBER_LOC).clear();
			logger.info("Main phone number field clear");
		}
		else if(fieldName.equalsIgnoreCase("email")){
			driver.findElement(EMAIL_LOC).clear();
			logger.info("Email field clear");
		}
		driver.pauseExecutionFor(2000);
		return this;
	}
	/***
	 * This method enter value in field mention in argument on account info page 
	 * 
	 * @param fieldName
	 * @return account Info Page object
	 * 
	 */
	public StoreFrontAccountInfoPage enterFields(String fieldName,String value){
		if(fieldName.equalsIgnoreCase("firstName")){
			driver.type(FIRST_NAME_LOC, value);
			logger.info("First name entered "+value);
		}
		else if(fieldName.equalsIgnoreCase("lastName")){
			driver.type(LAST_NAME_LOC, value);
			logger.info("Last name entered "+value);
		}
		else if(fieldName.equalsIgnoreCase("addressLine")){
			driver.type(ADDRESS_LINE_LOC, value);
			logger.info("Address line entered "+value);
		}
		else if(fieldName.equalsIgnoreCase("city")){
			driver.type(CITY_LOC, value);
			logger.info("City entered "+value);
		}
		else if(fieldName.equalsIgnoreCase("postalCode")){
			driver.type(POSTAL_CODE_LOC, value);
			logger.info("Postal code entered "+value);
		}
		else if(fieldName.equalsIgnoreCase("phone")){
			driver.type(MAIN_PHONE_NUMBER_LOC, value);
			logger.info("Main phone number entered "+value);
		}
		else if(fieldName.equalsIgnoreCase("email")){
			driver.type(EMAIL_LOC, value);
			logger.info("Email entered as "+value);
		}
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method clicks on the Email Your Consultant link
	 * @return
	 */
	public StoreFrontAccountInfoPage clickEmailYourConsultantLink(){
		driver.click(EMAIL_YOUR_CONSULTANT_LOC);
		logger.info("clicks on the Email Your Consultant link");
		return this;
	}

	/***
	 * This method enters the details to the "Email Your Consultant" fields.
	 * @param name
	 * @param email
	 * @param emailContent
	 * @return
	 */
	public StoreFrontAccountInfoPage enterEmailYourConsultantDetailsAndSubmit(String name,String email,String emailContent){
		driver.type(EMAIL_TO_CONSULTANT_NAME_LOC, name);
		logger.info("Email to consultant, name entered as "+name);
		driver.type(EMAIL_TO_CONSULTANT_EMAIL_LOC, email);
		logger.info("Email to consultant, email entered as "+email);
		enterEmailContentAtEmailYourConsultantFields(emailContent);
		driver.click(EMAIL_TO_CONSULTANT_EMAIL_SUBMIT_BTN_LOC);
		logger.info("Email to consultant, submit button clicked");
		driver.pauseExecutionFor(1000);
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method enters the email content to the "Email Your Consultant" fields
	 * @param emailContent
	 * @return
	 */
	public StoreFrontAccountInfoPage enterEmailContentAtEmailYourConsultantFields(String emailContent){
		driver.type(EMAIL_TO_CONSULTANT_EMAIL_CONTENT_LOC, emailContent);
		logger.info("Email to consultant, email content entered as "+emailContent);
		return this;
	}

	/***
	 * This method verifies if the email your consultant validation is 
	 * displayed or not.
	 * 
	 * @param field
	 * @param msg
	 * @return
	 */
	public boolean isEmailYourValidationDisplayed(String field,String msg){
		return driver.isElementVisible(By.xpath(String.format(emailYourConsultantValidationMsgLoc,field,msg)));

	}

	/***
	 * This method get first name on account info page.
	 * 
	 * @param
	 * @return first name message
	 * 
	 */
	public String getFirstNameFromAccountInfo(){
		String firstNameAccountInfo = driver.getAttribute(FIRST_NAME_LOC,"value");
		logger.info("First Name on account info is: "+firstNameAccountInfo);
		return firstNameAccountInfo.trim();
	}

	/***
	 * This method get Last name on account info page.
	 * 
	 * @param
	 * @return first name message
	 * 
	 */
	public String getLastNameFromAccountInfo(){
		String lastNameAccountInfo = driver.getAttribute(LAST_NAME_LOC,"value");
		logger.info("Last Name on account info is: "+lastNameAccountInfo);
		return lastNameAccountInfo.trim();
	}

	/***
	 * This method verifies if the email your consultant validation is 
	 * displayed or not.
	 * 
	 * @param 
	 * @param 
	 * @return boolean
	 */
	public boolean isSponserNameWithTitleRFIndependentConsultantPresent(){
		return driver.isElementVisible(SPONSER_NAME_ON_ACCOUNT_INFO_LOC);
	}

	/***
	 * This method validates the send button is disabled or not
	 * @param 
	 * @return boolean value
	 */
	public boolean isSendButtonForEmailToConsultantDisabled(){
		return driver.isElementPresent(DISABLED_SEND_BUTTON_EMAIL_TO_CONSULTANT_LOC);
	}

	/***
	 * This method enters the details to the "Email Your Consultant" fields.
	 * @param name
	 * @param email
	 * @param emailContent
	 * @return
	 */
	public StoreFrontAccountInfoPage enterEmailYourConsultantDetails(String name,String email,String emailContent){
		driver.type(EMAIL_TO_CONSULTANT_NAME_LOC, name);
		logger.info("Email to consultant, name entered as "+name);
		driver.type(EMAIL_TO_CONSULTANT_EMAIL_LOC, email);
		logger.info("Email to consultant, email entered as "+email);
		enterEmailContentAtEmailYourConsultantFields(emailContent);
		driver.waitForPageLoad();
		return this;
	}

}