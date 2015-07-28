package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.storeFront.RFPWSLoginTest;


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
	private final By ACCOUNT_USERNAME_LOC = By.xpath("//input[@id='username-account']");
	private final By ACCOUNT_SAVE_BUTTON_LOC = By.xpath("//input[@id='saveAccountInfo']");
	private final By ACCOUNT_INFO_VERIFY_ADDRESS_LOC = By.xpath("//input[@id='QAS_AcceptOriginal']");
	private String ACCOUNT_INFO_PROVINCE_LOC_FOR_REUPDATE ="//select[@id='state']//option[text()='%s']";
	private final By ACCOUNT_INFO_GENDER_LOC_FEMALE= By.xpath("//input[@id='female']");
	private String ACCOUNT_INFO_DAY_OF_BIRTH_LOC = "//select[@id='dayOfBirth']//option[@value='%s']";
	private String ACCOUNT_INFO_GENDER_LOC = "//label[@for='%s']";
	private String ACCOUNT_INFO_MONTH_OF_BIRTH_LOC = "//select[@id='monthOfBirth']//option[@value='%s']";
	private String ACCOUNT_INFO_YEAR_OF_BIRTH_LOC = "//select[@id='yearOfBirth']//option[@value='%s']";
	private String ACCOUNT_INFO_RADIO_BUTTON_LOC = "//input[@id='%s']";
	private final By ACCOUNT_AUTOSHIP_STATUS_LOC = By.xpath("//div[@id='left-menu']//a[text()='Autoship Status']");
	private final By VALIDATION_MESSAGE_FOR_MAIN_PHONE_NUMBER_LOC = By.xpath("//div[@class='tipsy-inner']");
	private String ACCOUNT_INFO_PROVINCE_VERIFY_ACCOUNT_INFO_LOC_ = "//select[@id='state']//option[@selected='selected']";

	public StoreFrontAccountInfoPage(RFWebsiteDriver driver) {
		super(driver);

	}
	public boolean verifyAccountInfoPageIsDisplayed(){
		driver.waitForElementPresent(ACCOUNT_INFO_TEMPLATE_HEADER_LOC);
		return driver.getCurrentUrl().contains(TestConstants.ACCOUNT_PAGE_SUFFIX_URL);

	}

	public StoreFrontAccountTerminationPage clickTerminateMyAccount() throws InterruptedException{
		driver.waitForElementPresent(TERMINATE_MY_ACCOUNT);
		driver.findElement(TERMINATE_MY_ACCOUNT).click();
		Thread.sleep(3000);
		return new StoreFrontAccountTerminationPage(driver);

	}
	public boolean verifyAccountTerminationLink(){
		return driver.isElementPresent(TERMINATE_MY_ACCOUNT);		
	}


	public StoreFrontAccountInfoPage updateAccountInformation(String firstName,String lastName,String addressLine1,String city,String postalCode, String mainPhoneNumber) throws InterruptedException{
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
		driver.click(ACCOUNT_INFO_VERIFY_ADDRESS_LOC);
		Thread.sleep(5000);
		return new StoreFrontAccountInfoPage(driver);
	}


	public boolean verifyFirstNameFromUIForAccountInfo(String firstName){
		String firstNameFromUI = driver.findElement(ACCOUNT_INFO_FIRST_NAME_LOC).getAttribute("value");
		if(firstNameFromUI.equalsIgnoreCase(firstName)){
			return true;
		}
		return false;
	}

	public boolean verifyLasttNameFromUIForAccountInfo(String lastName){
		String lastNameFromUI = driver.findElement(ACCOUNT_INFO_LAST_NAME_LOC).getAttribute("value");
		if(lastNameFromUI.equalsIgnoreCase(lastName)){
			return true;
		}
		return false;
	}

	public boolean verifyAddressLine1FromUIForAccountInfo(String addressLine1){
		String addressLine1FromUI = driver.findElement(ACCOUNT_INFO_ADDRESS_LINE_1_LOC).getAttribute("value");
		if(addressLine1FromUI.equalsIgnoreCase(addressLine1)){
			return true;
		}
		return false;
	}

	public boolean verifyCityFromUIForAccountInfo(String city){
		String cityFromUI = driver.findElement(ACCOUNT_INFO_CITY_LOC).getAttribute("value");
		if(cityFromUI.equalsIgnoreCase(city)){
			return true;
		}
		return false;
	}

	public boolean verifyProvinceFromUIForAccountInfo(String province){
		String provinceFromUI =driver.findElement(By.xpath(ACCOUNT_INFO_PROVINCE_VERIFY_ACCOUNT_INFO_LOC_)).getAttribute("value");
		if(provinceFromUI.equalsIgnoreCase(province)){
			return true;
		}
		return false;
	}

	public boolean verifyPostalCodeFromUIForAccountInfo(String postalCode){
		String postalCodeFromUI = driver.findElement(ACCOUNT_INFO_POSTAL_CODE_LOC).getAttribute("value");
		if(postalCodeFromUI.equalsIgnoreCase(postalCode)){
			return true;
		}
		return false;
	}

	public boolean verifyMainPhoneNumberFromUIForAccountInfo(String mainPhoneNumber){
		String mainPhoneNumberFromUI = driver.findElement(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC).getAttribute("value");
		if(mainPhoneNumberFromUI.equalsIgnoreCase(mainPhoneNumber)){
			return true;
		}
		return false;
	}

	public boolean verifyGenderFromUIAccountInfo(String gender){
		boolean genderValue = driver.findElement(By.xpath(String.format(ACCOUNT_INFO_RADIO_BUTTON_LOC, TestConstants.CONSULTANT_GENDER))).isSelected();
		System.out.println(genderValue);
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
			System.out.println(day);
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

	public StoreFrontOrdersAutoshipStatusPage clickOnAutoShipStatus(){
		driver.waitForElementPresent(ACCOUNT_AUTOSHIP_STATUS_LOC);
		driver.click(ACCOUNT_AUTOSHIP_STATUS_LOC);
		return new StoreFrontOrdersAutoshipStatusPage(driver);
	}

	public StoreFrontAccountInfoPage enterMainPhoneNumber(String mainPhoneNumber){
		driver.clear(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC);
		driver.type(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC, mainPhoneNumber);
		driver.click(ACCOUNT_SAVE_BUTTON_LOC);
		return new StoreFrontAccountInfoPage(driver);
	}

	public boolean verifyValidationMessageOfPhoneNumber(String validationMessage){
		if(driver.isElementPresent(VALIDATION_MESSAGE_FOR_MAIN_PHONE_NUMBER_LOC)){
			String validationMessageFromUI = driver.findElement(VALIDATION_MESSAGE_FOR_MAIN_PHONE_NUMBER_LOC).getText();
			System.out.println("Validation Message from UI -->  "+validationMessageFromUI);
			if(validationMessageFromUI.equalsIgnoreCase(validationMessage)){
				return true;
			}else {
				return false;
			}
		}
		return false;
	}

	public void enterOldPassword(String oldPassword){
		driver.findElement(By.xpath("//input[@id='old-password-account']")).sendKeys(oldPassword);
	}

	public void enterNewPassword(String newPassword){
		driver.findElement(By.xpath("//input[@id='new-password-account']")).sendKeys(newPassword);
	}

	public void enterConfimedPassword(String newPassword){
		driver.findElement(By.xpath("//input[@id='new-password-account2']")).sendKeys(newPassword);
	}

	public void clickSaveAccountPageInfo(){
		driver.click(ACCOUNT_SAVE_BUTTON_LOC);
	}

}

