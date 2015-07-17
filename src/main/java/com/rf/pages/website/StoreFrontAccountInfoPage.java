package com.rf.pages.website;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;


public class StoreFrontAccountInfoPage extends RFWebsiteBasePage{
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


	public StoreFrontAccountInfoPage updateAccountInformation(String firstName,String lastName,String addressLine1,String city,String postalCode, String mainPhoneNumber){
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
		return new StoreFrontAccountInfoPage(driver);

	}


	public StoreFrontAccountInfoPage reUpdateAccountInformation(String firstName,String lastName,String addressLine1,String city,String postalCode, String mainPhoneNumber){
		driver.clear(ACCOUNT_INFO_FIRST_NAME_LOC);
		driver.type(ACCOUNT_INFO_FIRST_NAME_LOC, firstName);
		driver.clear(ACCOUNT_INFO_LAST_NAME_LOC);
		driver.type(ACCOUNT_INFO_LAST_NAME_LOC, lastName);
		driver.clear(ACCOUNT_INFO_ADDRESS_LINE_1_LOC);
		driver.type(ACCOUNT_INFO_ADDRESS_LINE_1_LOC, addressLine1);
		driver.clear(ACCOUNT_INFO_CITY_LOC);
		driver.type(ACCOUNT_INFO_CITY_LOC, city);
		driver.click(By.xpath(String.format(ACCOUNT_INFO_PROVINCE_LOC_FOR_REUPDATE, TestConstants.CONSULTANT_PROVINCE_FOR_ACCOUNT_INFORMATION_FOR_REUPDATE)));
		driver.clear(ACCOUNT_INFO_POSTAL_CODE_LOC);
		driver.type(ACCOUNT_INFO_POSTAL_CODE_LOC, postalCode);
		driver.clear(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC);
		driver.type(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC, mainPhoneNumber);
		driver.click(By.xpath(String.format(ACCOUNT_INFO_DAY_OF_BIRTH_LOC, TestConstants.CONSULTANT_DAY_OF_BIRTH_FOR_REUPDATE)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_YEAR_OF_BIRTH_LOC, TestConstants.CONSULTANT_YEAR_OF_BIRTH_FOR_REUPDATE)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_MONTH_OF_BIRTH_LOC,TestConstants.CONSULTANT_MONTH_OF_BIRTH_FOR_REUPDATE)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_GENDER_LOC,TestConstants.CONSULTANT_GENDER_FOR_REUPDATE)));
		driver.click(ACCOUNT_SAVE_BUTTON_LOC);
		driver.click(ACCOUNT_INFO_VERIFY_ADDRESS_LOC);
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
		String provinceFromUI =driver.findElement(By.xpath(String.format(ACCOUNT_INFO_PROVINCE_LOC, TestConstants.CONSULTANT_PROVINCE_FOR_ACCOUNT_INFORMATION))).getText();
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
		dob = convertDBDateFormatToUIFormat(dob);
		System.out.println("DOB Format Created ==="+dob);
		String completeDate[] = dob.split(" ");
		String splittedMonth = completeDate[0].substring(0,3);
		String day =driver.findElement(By.xpath(String.format(ACCOUNT_INFO_DAY_OF_BIRTH_LOC, TestConstants.CONSULTANT_DAY_OF_BIRTH))).getAttribute("value");
		System.out.println(day);
		String month = driver.findElement(By.xpath(String.format(ACCOUNT_INFO_MONTH_OF_BIRTH_LOC,TestConstants.CONSULTANT_MONTH_OF_BIRTH))).getText();
		System.out.println(month);
		String year = driver.findElement(By.xpath(String.format(ACCOUNT_INFO_YEAR_OF_BIRTH_LOC, TestConstants.CONSULTANT_YEAR_OF_BIRTH))).getAttribute("value");
		System.out.println(year);
		String finalDateForAssertionFromUI = month+" "+day+","+" "+year;
		System.out.println("finalDateForAssertionFromUI"+finalDateForAssertionFromUI);
		String finalDateForAssertionFromDB = splittedMonth+" "+completeDate[1]+" "+completeDate[2];
		System.out.println("finalDateForAssertionFromDB"+finalDateForAssertionFromDB);
		if(finalDateForAssertionFromUI.equalsIgnoreCase(finalDateForAssertionFromDB)){
			return true;
		}
		return false;
	}		

	public StoreFrontOrdersAutoshipStatusPage clickOnAutoShipStatus(){
		driver.waitForElementPresent(ACCOUNT_AUTOSHIP_STATUS_LOC);
		driver.click(ACCOUNT_AUTOSHIP_STATUS_LOC);
		return new StoreFrontOrdersAutoshipStatusPage(driver);
	}

}

