package com.rf.pages.website;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;


public class StoreFrontAccountInfoPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontAccountInfoPage.class.getName());

	private final By ACCOUNT_INFO_TEMPLATE_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top' and contains(text(),'Account info')]");
	private final By TERMINATE_MY_ACCOUNT = By.xpath("//a[text()='Terminate My Account']");
	private final By ACCOUNT_INFO_FIRST_NAME_LOC = By.xpath("//input[@id='first-name']");
	private final By ACCOUNT_INFO_LAST_NAME_LOC = By.xpath("//input[@id='last-name']");
	private final By ACCOUNT_INFO_ADDRESS_LINE_1_LOC = By.xpath("//input[@id='address-1']");
	private final By ACCOUNT_INFO_CITY_LOC = By.xpath("//input[@id='city']");
	private String ACCOUNT_INFO_PROVINCE_LOC = "//select[@id='state']//option[text()='%s']";
	private final By ACCOUNT_INFO_POSTAL_CODE_LOC = By.xpath("//input[@id='postal-code']");
	private final By ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC = By.xpath("//input[@id='phonenumber']");
	private final By ACCOUNT_SAVE_BUTTON_LOC = By.xpath("//input[@id='saveAccountInfo']");
	private final By ACCOUNT_INFO_VERIFY_ADDRESS_LOC = By.xpath("//input[@id='QAS_AcceptOriginal']");
	private String ACCOUNT_INFO_DAY_OF_BIRTH_LOC = "//select[@id='dayOfBirth']//option[@value='%s']";
	private String ACCOUNT_INFO_GENDER_LOC = "//label[@for='%s']";
	private String ACCOUNT_INFO_MONTH_OF_BIRTH_LOC = "//select[@id='monthOfBirth']//option[@value='%s']";
	private String ACCOUNT_INFO_YEAR_OF_BIRTH_LOC = "//select[@id='yearOfBirth']//option[@value='%s']";
	private String ACCOUNT_INFO_RADIO_BUTTON_LOC = "//input[@id='%s']";
	private final By ACCOUNT_AUTOSHIP_STATUS_LOC = By.xpath("//div[@id='left-menu']//a[text()='Autoship Status']");
	private final By VALIDATION_MESSAGE_FOR_MAIN_PHONE_NUMBER_LOC = By.xpath("//div[@class='tipsy-inner']");
	private final By ACCOUNT_INFO_PROVINCE_VERIFY_ACCOUNT_INFO_LOC = By.xpath("//select[@id='state']//option[@selected='selected']");
	private final By LEFT_MENU_ACCOUNT_INFO_LOC = By.xpath("//div[@id='left-menu']//a[text()='ACCOUNT INFO']");
	private final By CANCEL_MY_CRP_LOC = By.xpath("//p[@id='crp-status']/a[contains(text(),'Cancel my CRP')]");
	private final By CANCEL_MY_CRP_NOW_LOC = By.xpath("//input[@id='cancel-crp-button']");
	private final By ENROLL_IN_CRP_LOC = By.xpath("//input[@id='crp-enroll']");
	private final By DAY_OF_BIRTH_FOR_4178_LOC = By.xpath("//select[@id='dayOfBirth']//option[@selected='selected'][2]");
	private final By MONTH_OF_BIRTH_4178_LOC = By.xpath("//select[@id='monthOfBirth']//option[@selected='selected'][2]");
	private final By YEAR_OF_BIRTH_4178_LOC = By.xpath("//select[@id='yearOfBirth']//option[@selected='selected'][2]");

	public StoreFrontAccountInfoPage(RFWebsiteDriver driver) {
		super(driver);

	}
	public boolean verifyAccountInfoPageIsDisplayed(){
		driver.waitForElementPresent(ACCOUNT_INFO_TEMPLATE_HEADER_LOC);
		return driver.getCurrentUrl().contains(TestConstants.ACCOUNT_PAGE_SUFFIX_URL);

	}

	public StoreFrontAccountTerminationPage clickTerminateMyAccount() throws InterruptedException{
		driver.waitForElementPresent(TERMINATE_MY_ACCOUNT);
		driver.click(TERMINATE_MY_ACCOUNT);
		return new StoreFrontAccountTerminationPage(driver);

	}
	public boolean verifyAccountTerminationLink(){
		return driver.isElementPresent(TERMINATE_MY_ACCOUNT);		
	}


	public StoreFrontAccountInfoPage updateAccountInformation(String firstName,String lastName,String addressLine1,String city,String postalCode, String mainPhoneNumber) throws InterruptedException{
		driver.waitForElementPresent(ACCOUNT_INFO_FIRST_NAME_LOC);
		driver.clear(ACCOUNT_INFO_FIRST_NAME_LOC);
		driver.type(ACCOUNT_INFO_FIRST_NAME_LOC, firstName);
		driver.clear(ACCOUNT_INFO_LAST_NAME_LOC);
		driver.type(ACCOUNT_INFO_LAST_NAME_LOC, lastName);
		driver.clear(ACCOUNT_INFO_ADDRESS_LINE_1_LOC);
		driver.type(ACCOUNT_INFO_ADDRESS_LINE_1_LOC, addressLine1);
		driver.clear(ACCOUNT_INFO_CITY_LOC);
		driver.type(ACCOUNT_INFO_CITY_LOC, city);
		driver.click(By.xpath(String.format(ACCOUNT_INFO_PROVINCE_LOC, TestConstants.CONSULTANT_PROVINCE_FOR_ACCOUNT_INFORMATION)));
		driver.clear(ACCOUNT_INFO_POSTAL_CODE_LOC);
		driver.type(ACCOUNT_INFO_POSTAL_CODE_LOC, postalCode);
		driver.clear(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC);
		driver.type(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC, mainPhoneNumber);
		driver.click(By.xpath(String.format(ACCOUNT_INFO_DAY_OF_BIRTH_LOC, TestConstants.CONSULTANT_DAY_OF_BIRTH)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_YEAR_OF_BIRTH_LOC, TestConstants.CONSULTANT_YEAR_OF_BIRTH)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_MONTH_OF_BIRTH_LOC,TestConstants.CONSULTANT_MONTH_OF_BIRTH)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_GENDER_LOC,TestConstants.CONSULTANT_GENDER)));
		driver.click(ACCOUNT_SAVE_BUTTON_LOC);
		try{
			driver.waitForElementPresent(ACCOUNT_INFO_VERIFY_ADDRESS_LOC);
			driver.click(ACCOUNT_INFO_VERIFY_ADDRESS_LOC);
		}
		catch(NoSuchElementException e){

		}
		return new StoreFrontAccountInfoPage(driver);
	}


	public boolean verifyFirstNameFromUIForAccountInfo(String firstName){
		driver.waitForElementPresent(ACCOUNT_INFO_FIRST_NAME_LOC);
		String firstNameFromUI = driver.findElement(ACCOUNT_INFO_FIRST_NAME_LOC).getAttribute("value");
		if(firstNameFromUI.equalsIgnoreCase(firstName)){
			return true;
		}
		return false;
	}

	public boolean verifyLasttNameFromUIForAccountInfo(String lastName){
		driver.waitForElementPresent(ACCOUNT_INFO_LAST_NAME_LOC);
		String lastNameFromUI = driver.findElement(ACCOUNT_INFO_LAST_NAME_LOC).getAttribute("value");
		if(lastNameFromUI.equalsIgnoreCase(lastName)){
			return true;
		}
		return false;
	}

	public boolean verifyAddressLine1FromUIForAccountInfo(String addressLine1){
		driver.waitForElementPresent(ACCOUNT_INFO_ADDRESS_LINE_1_LOC);
		String addressLine1FromUI = driver.findElement(ACCOUNT_INFO_ADDRESS_LINE_1_LOC).getAttribute("value");
		if(addressLine1FromUI.equalsIgnoreCase(addressLine1)){
			return true;
		}
		return false;
	}

	public boolean verifyCityFromUIForAccountInfo(String city){
		driver.waitForElementPresent(ACCOUNT_INFO_CITY_LOC);
		String cityFromUI = driver.findElement(ACCOUNT_INFO_CITY_LOC).getAttribute("value");
		if(cityFromUI.equalsIgnoreCase(city)){
			return true;
		}
		return false;
	}

	public boolean verifyProvinceFromUIForAccountInfo(String province){
		driver.waitForElementPresent(ACCOUNT_INFO_PROVINCE_VERIFY_ACCOUNT_INFO_LOC);
		String provinceFromUI =driver.findElement(ACCOUNT_INFO_PROVINCE_VERIFY_ACCOUNT_INFO_LOC).getAttribute("value");
		if(provinceFromUI.equalsIgnoreCase(province)){
			return true;
		}
		return false;
	}

	public boolean verifyPostalCodeFromUIForAccountInfo(String postalCode){
		driver.waitForElementPresent(ACCOUNT_INFO_POSTAL_CODE_LOC);
		String postalCodeFromUI = driver.findElement(ACCOUNT_INFO_POSTAL_CODE_LOC).getAttribute("value");
		if(postalCodeFromUI.equalsIgnoreCase(postalCode)){
			return true;
		}
		return false;
	}

	public boolean verifyMainPhoneNumberFromUIForAccountInfo(String mainPhoneNumber){
		driver.waitForElementPresent(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC);
		String mainPhoneNumberFromUI = driver.findElement(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC).getAttribute("value");
		if(mainPhoneNumberFromUI.equalsIgnoreCase(mainPhoneNumber)){
			return true;
		}
		return false;
	}

	public boolean verifyGenderFromUIAccountInfo(String gender){
		boolean genderValue = driver.findElement(By.xpath(String.format(ACCOUNT_INFO_RADIO_BUTTON_LOC, TestConstants.CONSULTANT_GENDER))).isSelected();
		if(genderValue == true){
			String genderFromUI = "male";
			genderFromUI.equalsIgnoreCase(gender);
			return true;
		}
		return false;
	}

	public boolean verifyBirthDateFromUIAccountInfo(String dob){
		if(dob == null){
			return false;
		}else{
			logger.info("Asserting Date of Birth");
			dob = convertDBDateFormatToUIFormat(dob);
			String completeDate[] = dob.split(" ");
			String splittedMonth = completeDate[0].substring(0,3);
			String day =driver.findElement(By.xpath(String.format(ACCOUNT_INFO_DAY_OF_BIRTH_LOC, TestConstants.CONSULTANT_DAY_OF_BIRTH))).getAttribute("value");
			String month = driver.findElement(By.xpath(String.format(ACCOUNT_INFO_MONTH_OF_BIRTH_LOC,TestConstants.CONSULTANT_MONTH_OF_BIRTH))).getText();

			switch (Integer.parseInt(month)) {  
			case 1:
				month="January";
				break;
			case 2:
				month="February";
				break;
			case 3:
				month="March";
				break;
			case 4:
				month="April";
				break;
			case 5:
				month="May";
				break;
			case 6:
				month="June";
				break;
			case 7:
				month="July";
				break;
			case 8:
				month="August";
				break;
			case 9:
				month="September";
				break;
			case 10:
				month="October";
				break;
			case 11:
				month="November";
				break;
			case 12:
				month="December";
				break;  
			}

			String year = driver.findElement(By.xpath(String.format(ACCOUNT_INFO_YEAR_OF_BIRTH_LOC, TestConstants.CONSULTANT_YEAR_OF_BIRTH))).getAttribute("value");
			String finalDateForAssertionFromUI = month+" "+day+","+" "+year;
			String finalDateForAssertionFromDB = splittedMonth+" "+completeDate[1]+" "+completeDate[2];

			if(finalDateForAssertionFromUI.equalsIgnoreCase(finalDateForAssertionFromDB)){
				return true;
			}
		}
		return false;
	}

	public boolean verifyBirthDateFromUIAccountInfoForCheckAccountInfo(String dob){
		if(dob == null){
			return false;
		}else{
			logger.info("Asserting Date of Birth");
			dob = convertDBDateFormatToUIFormat(dob);
			String completeDate[] = dob.split(" ");
			String splittedMonth = completeDate[0].substring(0,3);
			String day =driver.findElement(DAY_OF_BIRTH_FOR_4178_LOC).getAttribute("value");
			String month = driver.findElement(MONTH_OF_BIRTH_4178_LOC).getText();

			switch (Integer.parseInt(month)) {  
			case 1:
				month="Jan";
				break;
			case 2:
				month="Feb";
				break;
			case 3:
				month="Mar";
				break;
			case 4:
				month="Apr";
				break;
			case 5:
				month="May";
				break;
			case 6:
				month="Jun";
				break;
			case 7:
				month="Jul";
				break;
			case 8:
				month="Aug";
				break;
			case 9:
				month="Sep";
				break;
			case 10:
				month="Oct";
				break;
			case 11:
				month="Nov";
				break;
			case 12:
				month="Dec";
				break;  
			}

			String year = driver.findElement(YEAR_OF_BIRTH_4178_LOC).getAttribute("value");
			String finalDateForAssertionFromUI = month+" "+day+","+" "+year;
			String finalDateForAssertionFromDB = splittedMonth+" "+completeDate[1]+" "+completeDate[2];
			if(finalDateForAssertionFromUI.equalsIgnoreCase(finalDateForAssertionFromDB)){
				return true;
			}
		}
		return false;
	}

	public StoreFrontOrdersAutoshipStatusPage clickOnAutoShipStatus(){
		driver.waitForElementPresent(ACCOUNT_AUTOSHIP_STATUS_LOC);
		driver.click(ACCOUNT_AUTOSHIP_STATUS_LOC);
		logger.info("Autoship status clicked "+ACCOUNT_AUTOSHIP_STATUS_LOC);
		driver.pauseExecutionFor(3000);
		driver.waitForLoadingImageToDisappear();
		return new StoreFrontOrdersAutoshipStatusPage(driver);
		
	}

	public StoreFrontAccountInfoPage enterMainPhoneNumber(String mainPhoneNumber){
		driver.waitForElementPresent(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC);
		driver.clear(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC);
		driver.type(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC, mainPhoneNumber);
		driver.waitForElementPresent(ACCOUNT_SAVE_BUTTON_LOC);
		driver.click(ACCOUNT_SAVE_BUTTON_LOC);
		driver.pauseExecutionFor(2000);
		logger.info("Save account info button clicked");
		return new StoreFrontAccountInfoPage(driver);
	}
	
	public void clickOnSubscribeToPulseBtn(){
		driver.waitForElementPresent(By.id("subscribe_pulse_button_new"));
		driver.click(By.id("subscribe_pulse_button_new"));
		driver.pauseExecutionFor(1000);
		driver.waitForElementPresent(By.id("pulse-enroll"));
		driver.click(By.id("pulse-enroll"));
		driver.waitForPageLoad();
	}

	public boolean verifyValidationMessageOfPhoneNumber(String validationMessage){
		if(driver.isElementPresent(VALIDATION_MESSAGE_FOR_MAIN_PHONE_NUMBER_LOC)){
			String validationMessageFromUI = driver.findElement(VALIDATION_MESSAGE_FOR_MAIN_PHONE_NUMBER_LOC).getText();
			if(validationMessageFromUI.equalsIgnoreCase(validationMessage)){
				return true;
			}else {
				return false;
			}
		}
		return false;
	}

	public void enterOldPassword(String oldPassword){
		driver.findElement(By.id("old-password-account")).sendKeys(oldPassword);
	}

	public void enterNewPassword(String newPassword){
		driver.findElement(By.id("new-password-account")).sendKeys(newPassword);
	}

	public void enterConfirmedPassword(String newPassword){
		driver.findElement(By.id("new-password-account2")).sendKeys(newPassword);
	}

	public void clickSaveAccountPageInfo(){
		driver.waitForElementPresent(ACCOUNT_SAVE_BUTTON_LOC);
		driver.click(ACCOUNT_SAVE_BUTTON_LOC);
		logger.info("Save account info button clicked "+ACCOUNT_SAVE_BUTTON_LOC);
	}

	public StoreFrontAccountInfoPage clickOnAccountInfoFromLeftPanel(){
		driver.click(LEFT_MENU_ACCOUNT_INFO_LOC);
		logger.info("Account inof link from left panel clicked "+LEFT_MENU_ACCOUNT_INFO_LOC);
		return new StoreFrontAccountInfoPage(driver);
	}

	public StoreFrontAccountInfoPage clickOnCancelMyCRP() throws InterruptedException{
		driver.waitForElementPresent(CANCEL_MY_CRP_LOC);
		driver.click(CANCEL_MY_CRP_LOC);
		driver.waitForElementPresent(CANCEL_MY_CRP_NOW_LOC);
		driver.click(CANCEL_MY_CRP_NOW_LOC);
		return new StoreFrontAccountInfoPage(driver);
	}

	public boolean verifyCRPCancelled(){
		logger.info("Asserting Cancel CRP");
		try{
			driver.findElement(ENROLL_IN_CRP_LOC);
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public void clickOnEnrollInCRP() throws InterruptedException{
		driver.waitForElementToBeClickable(ENROLL_IN_CRP_LOC, 5000);
		driver.waitForElementPresent(ENROLL_IN_CRP_LOC);
		driver.click(ENROLL_IN_CRP_LOC);
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isOrderOfRequiredTypePresentInHistory(String orderType){
		List<WebElement> allOrders = driver.findElements(By.xpath("//table[@id='history-orders-table']/tbody/tr"));
		for(int i=2;i<=allOrders.size();i++){
			if(driver.findElement(By.xpath("//table[@id='history-orders-table']/tbody/tr["+i+"]/td[4]")).getText().contains(orderType.toUpperCase())||driver.findElement(By.xpath("//table[@id='history-orders-table']/tbody/tr["+i+"]/td[4]")).getText().contains(orderType.toLowerCase())){
				return true;
			}
		}
		return false;
	}

	public boolean verifyCurrentCRPStatus() throws InterruptedException{
		logger.info("Asserting Current CRP Status");
		driver.waitForElementPresent(By.xpath("//p[@id='crp-status']/span[1]"));
		if(driver.findElement(By.xpath("//p[@id='crp-status']/span[1]")).getText().equalsIgnoreCase("Enrolled")){
			return true;
		}
		return false;
	}

	public void enterUserName(String username) throws InterruptedException	{
		driver.findElement(By.id("username-account")).clear();
		driver.findElement(By.id("username-account")).sendKeys(username);		
	}

	
	public String getErrorMessage()	{
		String errorMessage=driver.findElement(By.xpath("//div[@class='tipsy-inner']")).getText();
		return errorMessage;
	}

	public boolean checkErrorMessage()	{
		Boolean status=driver.findElement(By.xpath("//div[@class='tipsy-inner']")).isDisplayed();
		return status;
	}
}

