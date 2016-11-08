package com.rf.pages.website.storeFront;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;


public class StoreFrontAccountInfoPage extends StoreFrontRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontAccountInfoPage.class.getName());

	Actions actions;

	private final By ACCOUNT_INFO_TEMPLATE_HEADER_LOC = By.xpath("//div[@id='main-content']//div[contains(text(),'Account info')]");
	private final By TERMINATE_MY_ACCOUNT = By.xpath("//a[text()='Terminate My Account']");
	private final By ACCOUNT_INFO_FIRST_NAME_LOC = By.xpath("//input[@id='first-name']");
	private final By ACCOUNT_INFO_LAST_NAME_LOC = By.xpath("//input[@id='last-name']");
	private final By ACCOUNT_INFO_ADDRESS_LINE_1_LOC = By.xpath("//input[@id='address-1']");
	private final By ACCOUNT_INFO_CITY_LOC = By.xpath("//input[@id='city']");
	private final By ACCOUNT_INFO_PROVINCE_LOC=By.xpath("//select[@id='state']/option[1]");
	private final By ACCOUNT_INFO_POSTAL_CODE_LOC = By.xpath("//input[@id='postal-code']");
	private final By ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC = By.xpath("//input[@id='phonenumber']");
	private final By ACCOUNT_SAVE_BUTTON_LOC = By.xpath("//input[@id='saveAccountInfo']");
	private final By ACCOUNT_INFO_VERIFY_ADDRESS_LOC = By.xpath("//input[@id='QAS_AcceptOriginal']");
	private String ACCOUNT_INFO_DAY_OF_BIRTH_LOC = "//select[@id='dayOfBirth']//option[@value='%s']";
	private String ACCOUNT_INFO_GENDER_LOC = "//input[@id='%s']/..";
	private String ACCOUNT_INFO_MONTH_OF_BIRTH_LOC = "//select[@id='monthOfBirth']//option[@value='%s']";
	private String ACCOUNT_INFO_YEAR_OF_BIRTH_LOC = "//select[@id='yearOfBirth']//option[@value='%s']";
	private String ACCOUNT_INFO_RADIO_BUTTON_LOC = "//input[@id='%s']";
	private final By ACCOUNT_AUTOSHIP_STATUS_LOC = By.xpath("//div[@id='left-menu']//a[text()='Autoship Status']");
	private final By VALIDATION_MESSAGE_FOR_MAIN_PHONE_NUMBER_LOC = By.xpath("//label[contains(text(),'Please specify a valid phone number')]");
	private final By ACCOUNT_INFO_PROVINCE_VERIFY_ACCOUNT_INFO_LOC = By.xpath("//select[@id='state']//option[@selected='selected']");
	private final By LEFT_MENU_ACCOUNT_INFO_LOC = By.xpath("//div[@id='left-menu']//a[text()='ACCOUNT INFO']");
	private final By CANCEL_MY_CRP_LOC = By.xpath("//a[contains(text(),'Cancel my CRP')]");
	private final By CANCEL_MY_CRP_NOW_LOC = By.xpath("//a[@id='cancel-crp-button']");
	private final By ENROLL_IN_CRP_LOC = By.xpath("//input[@id='crp-enroll']");
	private final By DAY_OF_BIRTH_FOR_4178_LOC = By.xpath("//select[@id='dayOfBirth']//option[@selected='selected'][2]");
	private final By MONTH_OF_BIRTH_4178_LOC = By.xpath("//select[@id='monthOfBirth']//option[@selected='selected'][2]");
	private final By YEAR_OF_BIRTH_4178_LOC = By.xpath("//select[@id='yearOfBirth']//option[@selected='selected'][2]");
	private final By YOUR_ACCOUNT_DROPDOWN_LOC = By.xpath("//button[@class='btn btn-default dropdown-toggle']");
	private final By ACCOUNT_SHIPPING_INFO_LOC = By.xpath("//div[@id='left-menu']//a[text()='Shipping Info']");
	private final By NEXT_BILL_DATE_OF_PULSE_TEMPLATE = By.xpath("//div[@id='left-menu']//a[text()='Shipping Info']");
	private final By NEXT_DUE_DATE_OF_CRP_TEMPLATE = By.xpath("//span[contains(text(),'Next Bill & Ship Date')]/following::span[1]");
	private final By NEXT_DUE_DATE_PULSE_SUBSCRIPTION_STATUS = By.xpath("//span[contains(text(),'Next Bill Date')]/following::span[1]");

	public StoreFrontAccountInfoPage(RFWebsiteDriver driver) {
		super(driver);

	}
	public boolean verifyAccountInfoPageIsDisplayed(){
		driver.waitForElementPresent(ACCOUNT_INFO_TEMPLATE_HEADER_LOC);
		return driver.getCurrentUrl().contains(TestConstants.ACCOUNT_PAGE_SUFFIX_URL);
	}

	public String getUsernameFromAccountInfoPage(){
		return driver.findElement(By.xpath("//input[@id='username-account']")).getAttribute("value"); 
	}

	public void enterSpouseLastNameOptional(String firstName){
		try{
			driver.quickWaitForElementPresent(By.id("spouse-last"));
			driver.clear(By.id("spouse-last"));
			driver.findElement(By.id("spouse-last")).sendKeys(firstName);
			logger.info("Spouse last name entered as "+firstName);
		}catch(Exception e){

		}
	}

	public StoreFrontAccountTerminationPage clickTerminateMyAccount() throws InterruptedException{
		driver.waitForElementPresent(TERMINATE_MY_ACCOUNT);
		driver.click(TERMINATE_MY_ACCOUNT);
		driver.waitForPageLoad();
		driver.pauseExecutionFor(3000);
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
		//driver.click(By.xpath(String.format(ACCOUNT_INFO_PROVINCE_LOC, TestConstants.CONSULTANT_PROVINCE_FOR_ACCOUNT_INFORMATION)));
		driver.click(By.id("state"));
		driver.click(ACCOUNT_INFO_PROVINCE_LOC);
		driver.clear(ACCOUNT_INFO_POSTAL_CODE_LOC);
		driver.type(ACCOUNT_INFO_POSTAL_CODE_LOC, postalCode);
		driver.clear(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC);
		driver.type(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC, mainPhoneNumber);
		driver.click(By.xpath(String.format(ACCOUNT_INFO_DAY_OF_BIRTH_LOC, TestConstants.CONSULTANT_DAY_OF_BIRTH)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_YEAR_OF_BIRTH_LOC, TestConstants.CONSULTANT_YEAR_OF_BIRTH)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_MONTH_OF_BIRTH_LOC,TestConstants.CONSULTANT_MONTH_OF_BIRTH)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_GENDER_LOC,TestConstants.CONSULTANT_GENDER)));
		driver.click(ACCOUNT_SAVE_BUTTON_LOC);
		driver.waitForElementPresent(ACCOUNT_INFO_VERIFY_ADDRESS_LOC);
		driver.click(ACCOUNT_INFO_VERIFY_ADDRESS_LOC);
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontAccountInfoPage(driver);
	}

	public void updateFirstName(String firstName){
		driver.waitForElementPresent(ACCOUNT_INFO_FIRST_NAME_LOC);
		driver.type(ACCOUNT_INFO_FIRST_NAME_LOC, firstName);
	}

	public void updateLastName(String lastName){
		driver.waitForElementPresent(ACCOUNT_INFO_LAST_NAME_LOC);
		driver.type(ACCOUNT_INFO_LAST_NAME_LOC, lastName);
	}

	public void updateAddressWithCityAndPostalCode(String addressLine1,String city,String postalCode){
		driver.type(ACCOUNT_INFO_ADDRESS_LINE_1_LOC, addressLine1);		
		driver.type(ACCOUNT_INFO_CITY_LOC, city);
		driver.type(ACCOUNT_INFO_POSTAL_CODE_LOC, postalCode);
	}

	public String updateRandomStateAndReturnName(){
		Select stateDD = new Select(driver.findElement(By.xpath("//select[@id='state']")));
		List<WebElement> allStates = driver.findElements(By.xpath("//select[@id='state']/option"));
		String selectedState = null;
		int randomState =0;
		do{
			randomState = RandomUtils.nextInt(1, allStates.size()-1);
			logger.info("random number for state is "+randomState);
			selectedState =  driver.findElement(By.xpath("//select[@id='state']/option["+randomState+"]")).getText();			
		}while(selectedState.contains("Quebec"));

		driver.click(By.xpath("//select[@id='state']/option["+randomState+"]"));
		return driver.findElement(By.xpath("//select[@id='state']/option["+randomState+"]")).getText();
	}

	public void updateMainPhnNumber(String phnNumber){
		driver.waitForElementPresent(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC);
		driver.type(ACCOUNT_INFO_MAIN_PHONE_NUMBER_LOC, phnNumber);
	}

	public void updateDateOfBirthAndGender(){
		driver.click(By.xpath(String.format(ACCOUNT_INFO_DAY_OF_BIRTH_LOC, TestConstants.CONSULTANT_DAY_OF_BIRTH)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_YEAR_OF_BIRTH_LOC, TestConstants.CONSULTANT_YEAR_OF_BIRTH)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_MONTH_OF_BIRTH_LOC,TestConstants.CONSULTANT_MONTH_OF_BIRTH)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_GENDER_LOC,TestConstants.CONSULTANT_GENDER)));
	}

	public void uncheckSpouseCheckBox(){
		if(driver.findElement(By.xpath("//input[@id='enrollAllowSpouse1']")).isSelected()==true){
			driver.findElement(By.xpath("//input[@id='enrollAllowSpouse1']")).click();
		}
	}

	public void clickSaveAccountBtn(){
		driver.pauseExecutionFor(3000);
		driver.waitForElementPresent(ACCOUNT_SAVE_BUTTON_LOC);
		driver.click(ACCOUNT_SAVE_BUTTON_LOC);
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(ACCOUNT_INFO_VERIFY_ADDRESS_LOC);
			driver.click(ACCOUNT_INFO_VERIFY_ADDRESS_LOC);
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}

		driver.waitForPageLoad();
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
		String provinceFromUI =driver.findElement(ACCOUNT_INFO_PROVINCE_VERIFY_ACCOUNT_INFO_LOC).getText();
		logger.info("State assertion... updated state is "+province+" on UI it is "+provinceFromUI);
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
		if(mainPhoneNumberFromUI.trim().equalsIgnoreCase(mainPhoneNumber.trim())){
			System.out.println("phone selected in if block is "+mainPhoneNumberFromUI);
			return true;
		}else{
			try{
				String[] mainPhone = mainPhoneNumberFromUI.split("-");
				mainPhoneNumberFromUI = mainPhone[0]+mainPhone[1]+mainPhone[2];
			}catch(Exception e){

			}
			if(mainPhoneNumberFromUI.equalsIgnoreCase(mainPhoneNumber)){
				System.out.println("phone selected in else block is "+mainPhoneNumberFromUI);
				return true;
			}
		}
		return false;
	}


	public boolean verifyGenderFromUIAccountInfo(String gender){
		boolean genderValue = driver.findElement(By.xpath(String.format(ACCOUNT_INFO_RADIO_BUTTON_LOC, TestConstants.CONSULTANT_GENDER))).isSelected();
		if(genderValue == true){
			String genderFromUI = "male";
			genderFromUI.equalsIgnoreCase(gender);
			return true;
		}else if(driver.findElement(By.xpath(String.format(ACCOUNT_INFO_RADIO_BUTTON_LOC, TestConstants.CONSULTANT_FEMALE_GENDER))).isSelected() == true){
			String genderFromUI = "female";
			genderFromUI.equalsIgnoreCase(gender);
			return true;
		}else if(gender.equals("others")){
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
			String month = driver.findElement(By.xpath("//select[@id='monthOfBirth']//option[@selected='selected'][2]")).getAttribute("value");

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
		// driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(By.id("subscribe_pulse_button_new")));
		driver.pauseExecutionFor(2500);
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
		driver.type(By.id("old-password-account"), oldPassword);		
	}

	public void enterNewPassword(String newPassword){
		driver.type(By.id("new-password-account"), newPassword);		
	}

	public void enterConfirmedPassword(String newPassword){
		driver.type(By.id("new-password-account2"), newPassword);		
	}

	public void clickSaveAccountPageInfo(){
		driver.waitForElementPresent(ACCOUNT_SAVE_BUTTON_LOC);
		driver.click(ACCOUNT_SAVE_BUTTON_LOC);
		logger.info("Save account info button clicked "+ACCOUNT_SAVE_BUTTON_LOC);
		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
	}

	public StoreFrontAccountInfoPage clickOnAccountInfoFromLeftPanel(){
		driver.click(LEFT_MENU_ACCOUNT_INFO_LOC);
		logger.info("Account inof link from left panel clicked "+LEFT_MENU_ACCOUNT_INFO_LOC);
		return new StoreFrontAccountInfoPage(driver);
	}

	public StoreFrontAccountInfoPage clickOnCancelMyCRP() throws InterruptedException{
		if(driver.isElementPresent(CANCEL_MY_CRP_LOC)){
			driver.waitForElementPresent(CANCEL_MY_CRP_LOC);
			driver.click(CANCEL_MY_CRP_LOC);
			driver.waitForElementPresent(CANCEL_MY_CRP_NOW_LOC);
			driver.click(CANCEL_MY_CRP_NOW_LOC);
			driver.waitForLoadingImageToDisappear();
			driver.pauseExecutionFor(2000);
		}else{
			logger.info("No crp present for this user");
		}
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
		driver.waitForElementPresent(By.xpath(".//div[@id='crp-status']/div[1]/span"));
		if(driver.findElement(By.xpath(".//div[@id='crp-status']/div[1]/span")).getText().equalsIgnoreCase("Enrolled")){
			return true;
		}
		return false;
	}

	public void enterUserName(String username) throws InterruptedException	{
		driver.clear(By.id("username-account"));
		driver.type(By.id("username-account"),username+"\t");
		driver.waitForLoadingImageToDisappear();
	}

	public String getErrorMessage() {
		driver.waitForElementPresent(By.xpath(".//*[@id='accountInfo']//label[@class='error']"));
		String errorMessage=driver.findElement(By.xpath(".//*[@id='accountInfo']//label[@class='error']")).getText();
		return errorMessage;
	}

	public boolean checkErrorMessage()	{
		Boolean status=driver.findElement(By.xpath("//div[@class='tipsy-inner']")).isDisplayed();
		return status;
	}

	public String getUserName(){
		return driver.findElement(By.xpath(".//input[@id='username-account']")).getAttribute("value");
	}

	public void checkAllowMySpouseCheckBox(){
		try{
			driver.findElement(By.xpath("//input[@id='enrollAllowSpouse1']/ancestor::div[1][@class='repaired-checkbox checked']"));
			System.out.println("checkbox already checked");
			driver.click(By.xpath("//input[@id='enrollAllowSpouse1']/.."));
			driver.pauseExecutionFor(2000);
		}
		catch(Exception e){

		}
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@id='enrollAllowSpouse1']/..")));
		driver.pauseExecutionFor(1000);

	}

	public boolean validateEnterSpouseDetailsAndAccept(){
		actions = new Actions(RFWebsiteDriver.driver);
		int randomNumber =  CommonUtils.getRandomNum(10000, 1000000);
		String spouseFirstName="Mary"+randomNumber;
		String spouseLastName="Rose";
		driver.waitForElementTobeEnabled(By.xpath("//input[@id='spouse-first']"));
		driver.pauseExecutionFor(5000);
		driver.clear(By.xpath("//input[@id='spouse-first']"));
		driver.type(By.xpath("//input[@id='spouse-first']"),spouseFirstName);
		driver.clear(By.xpath("//input[@id='spouse-last']"));
		driver.type(By.xpath("//input[@id='spouse-last']"),spouseLastName);
		//actions.sendKeys(Keys.TAB).build().perform();
		driver.click(By.xpath("//input[@id='old-password-account']"));
		driver.pauseExecutionFor(2000);
		driver.quickWaitForElementPresent(By.xpath("//input[@id='acceptSpouse']"));
		driver.pauseExecutionFor(2000);
		WebElement accept_btn = driver.findElement(By.xpath("//input[@id='acceptSpouse']"));
		driver.click(By.xpath("//input[@id='acceptSpouse']"));
		//		actions = new Actions(RFWebsiteDriver.driver);
		//		actions.moveToElement(accept_btn).click(accept_btn).build().perform();
		driver.pauseExecutionFor(2000);
		return driver.findElement(By.xpath("//input[@id='spouse-first']")).getAttribute("value").contains(spouseFirstName);
		//return driver.findElement(By.xpath("//input[@id='spouse-first']")).isDisplayed();
	}

	public boolean validateClickCancelOnProvideAccessToSpousePopup(){
		actions = new Actions(RFWebsiteDriver.driver);
		int randomNumber =  CommonUtils.getRandomNum(10000, 1000000);
		String spouseFirstName="Mary"+randomNumber;
		String spouseLastName="Rose";
		driver.waitForElementTobeEnabled(By.xpath("//input[@id='spouse-first']"));
		driver.pauseExecutionFor(5000);
		driver.clear(By.xpath("//input[@id='spouse-first']"));
		driver.type(By.xpath("//input[@id='spouse-first']"),spouseFirstName);
		driver.clear(By.xpath("//input[@id='spouse-last']"));
		driver.type(By.xpath("//input[@id='spouse-last']"),spouseLastName);
		//actions.sendKeys(Keys.TAB).build().perform();
		driver.click(By.xpath("//input[@id='old-password-account']"));
		driver.pauseExecutionFor(2000);
		driver.quickWaitForElementPresent(By.xpath("//input[@id='cancelSpouse']"));
		driver.click(By.xpath("//input[@id='cancelSpouse']"));
		driver.pauseExecutionFor(1500);
		return !driver.findElement(By.xpath("//input[@id='spouse-first']")).getAttribute("value").contains(spouseFirstName);
		//return driver.findElement(By.xpath("//input[@id='cancelSpouse']")).isDisplayed() || driver.findElement(By.xpath("//input[@id='spouse-first']")).isDisplayed();
	}

	public void clickOnSaveButtonAfterAddressChange() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveAccountInfo"));
		driver.click(By.id("saveAccountInfo"));
		logger.info("Save button after selecting address is clicked");
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
			driver.click(By.id("QAS_AcceptOriginal"));
			logger.info("Accept as original button clicked");
			driver.waitForLoadingImageToDisappear();
		}catch(NoSuchElementException e){

		}  
	}

	public void changeMainAddressToQuebec(){
		driver.waitForElementPresent(By.id("state"));
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[@value='QC']"));
		driver.click(By.xpath("//select[@id='state']/option[@value='QC']"));
		logger.info("state selected is quebec");
	}

	public String getAddressUpdateConfirmationMessageFromUI(){
		return driver.findElement(By.xpath(".//div[@id='globalMessages']//p")).getText();
	}

	public StoreFrontShippingInfoPage clickOnShippingInfoOnAccountInfoPage(){
		driver.waitForElementPresent(ACCOUNT_SHIPPING_INFO_LOC);
		driver.click(ACCOUNT_SHIPPING_INFO_LOC);
		logger.info("Shipping Info link clicked "+ACCOUNT_SHIPPING_INFO_LOC);
		driver.pauseExecutionFor(3000);
		driver.waitForLoadingImageToDisappear();
		return new StoreFrontShippingInfoPage(driver);

	}

	public void clickOnPcPerksStatus(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'PC Perks Status')]"));
		driver.click(By.xpath("//a[contains(text(),'PC Perks Status')]"));
	}

	public StoreFrontAccountTerminationPage clickOnCancelPCPerks(){
		driver.waitForElementPresent(By.id("cancel-pc-perks-button"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("cancel-pc-perks-button")));
		//driver.click(By.id("cancel-pc-perks-button"));
		return new StoreFrontAccountTerminationPage(driver);
	}

	public boolean validatePulseCancelled(){
		driver.waitForElementPresent(By.id("subscribe_pulse_button_new"));
		return driver.isElementPresent(By.id("subscribe_pulse_button_new"));
	}

	public boolean validateSubscribeToPulse(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Cancel my Pulse subscription')]"));
		return driver.isElementPresent(By.xpath("//a[contains(text(),'Cancel my Pulse subscription')]"));
	}

	public void enterNewUserNameAndClicKOnSaveButton(String newUserName) {
		driver.waitForElementPresent(By.id("username-account"));
		driver.clear(By.id("username-account"));
		driver.type(By.id("username-account"), newUserName+"\t");
		driver.click(By.id("saveAccountInfo"));
		logger.info("save button clicked");
		try{
			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
			driver.click(By.id("QAS_AcceptOriginal"));
			logger.info("Accept as original button clicked");
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){
			driver.quickWaitForElementPresent(By.id("QAS_RefineBtn"));
			driver.click(By.id("QAS_RefineBtn"));
			logger.info("Accept as original button clicked");
			driver.waitForLoadingImageToDisappear();
		}  
	}

	public boolean verifyProfileUpdationMessage(){
		driver.waitForElementPresent(By.xpath("//div[@id='globalMessages']//p"));
		String updationMessage = driver.findElement(By.xpath("//div[@id='globalMessages']//p")).getText();
		if(updationMessage.equals("Your profile has been updated")){
			return true;
		}
		return false;
	}	

	public boolean errorMessageForExistingUser(){
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='globalMessages']//div[@class='information_message negative']/span[contains(., 'Unable to update account details')]"));
			return driver.findElement(By.xpath("//div[@id='globalMessages']//div[@class='information_message negative']/span[contains(., 'Unable to update account details')]")).isDisplayed();
		}catch(NoSuchElementException e){
			driver.waitForElementPresent(By.xpath("//div[@id='globalMessages']//div[contains(., 'Your Username already exist,Please Enter the Different Username')]//div"));
			return driver.findElement(By.xpath("//div[@id='globalMessages']//div[contains(., 'Your Username already exist,Please Enter the Different Username')]//div")).isDisplayed();
		}
	}

	public String getWrongUsernameErrorMessage() {
		driver.waitForElementPresent(By.xpath("//div[@class='information_message negative']/p[2]"));
		String errorMessage=driver.findElement(By.xpath("//div[@class='information_message negative']/p[2]")).getText();
		return errorMessage;
	}

	public boolean enterUserNameWithSpclChar(String prefix) throws InterruptedException{
		char splChar[] = {'!','$','%','*','^','(',')'};
		int lenth = splChar.length;
		boolean flag;
		for(int i = 0; i<lenth; i++){
			flag = false;
			String username = prefix+splChar[i];
			enterUserName(username);
			clickSaveAccountPageInfo();
			flag = errorMessagePresent();
			if(flag==true){
				continue;
			}
			else{
				return false;

			}


		}
		return true;
	}

	public boolean errorMessagePresent(){
		driver.waitForElementPresent(By.xpath(".//form[@id='accountInfo']//label[@class='error']"));
		if(driver.findElement(By.xpath(".//form[@id='accountInfo']//label[@class='error']")).isDisplayed()){
			return true;

		}else{
			return false;
		}
	}

	public boolean verifyCrpStatusAfterReactivation() {
		driver.quickWaitForElementPresent(By.id("crp-enroll"));
		return driver.isElementPresent(By.id("crp-enroll"));

	}

	public boolean verifyPulseStatusAfterReactivation(String currentPulseStatus) {
		driver.quickWaitForElementPresent(By.xpath("//div[@id='currentPulseSubscriptionStatus']/span"));
		if(driver.findElement(By.xpath("//div[@id='currentPulseSubscriptionStatus']/span")).getText().equals(currentPulseStatus)){
			return true;
		}else{
			return false;
		}
	}

	public String getCRPStatusFromUI() {
		return driver.findElement(By.xpath("//div[@id='currentCRPStatus']/span")).getText();
	}

	public String getPulseStatusFromUI() {
		return driver.findElement(By.xpath("//div[@id='currentPulseSubscriptionStatus']/span")).getText();
	}

	public void clickOndelayOrCancelPCPerks(){
		driver.waitForElementPresent(By.xpath("//a[text()='Delay or Cancel PC Perks']"));
		driver.click(By.xpath("//a[text()='Delay or Cancel PC Perks']"));		
	}

	public boolean isYesChangeMyAutoshipDateButtonPresent() {
		driver.waitForElementPresent(By.id("change-autoship-button"));
		return driver.isElementPresent(By.id("change-autoship-button"));
	}

	public boolean isCancelPCPerksLinkPresent() {
		driver.waitForElementPresent(By.xpath("//a[@id='cancel-pc-perks-button']"));
		return driver.isElementPresent(By.xpath("//a[@id='cancel-pc-perks-button']"));

	}

	public void clickOnCancelMyPulseSubscription(){
		try{
			if(validateSubscribeToPulse()==true){
				cancelPulseSubscription();
			}
		}catch(Exception e){

		}
		driver.waitForPageLoad();
	}

	public void clickOnOnlySubscribeToPulseBtn(){
		driver.waitForElementPresent(By.id("subscribe_pulse_button_new"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("subscribe_pulse_button_new")));
		//driver.click(By.id("subscribe_pulse_button_new"));
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(4000);
	}

	public String getWebsitePrefixName(){
		driver.waitForElementPresent(By.xpath("//p[@id='prefix-validation']/span[2]"));
		String pwsUnderPulse = driver.findElement(By.xpath("//p[@id='prefix-validation']/span[2]")).getText();
		return pwsUnderPulse;
	}

	public void clickOnNextDuringPulseSubscribtion(){
		driver.waitForElementPresent(By.id("pulse-enroll"));
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(By.id("pulse-enroll")));
		driver.waitForPageLoad();
	}

	public void enterWebsitePrefixName(String name){
		driver.waitForElementPresent(By.id("webSitePrefix"));
		driver.type(By.id("webSitePrefix"), name);
		clickOnNextDuringPulseSubscribtion();
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyWebsitePrefixSuggestionIsPresent(){
		List<WebElement> noOfSuggestions = driver.findElements(By.xpath("//p[@id='prefix-validation']/span"));
		if(noOfSuggestions.size()>0){
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyEnteredBirthDateFromDB(String dobDB, String consultantDayOfBirth, String consultantMonthOfBirth, String consultantYearOfBirth) {
		String date[] = dobDB.split("\\ ");		  
		String formattedDate = consultantYearOfBirth+"-"+consultantMonthOfBirth+"-0"+consultantDayOfBirth;
		System.out.println(formattedDate);
		if(date[0].equals(formattedDate.trim())){
			return true;
		}		  
		return false;
	}

	public void clickOnGenderRadioButton(String consultantGender) {
		for(int i=0;i<=5;i++){
			driver.waitForElementPresent(By.xpath("//input[@id='"+consultantGender+"']/.."));
			driver.click(By.xpath("//input[@id='"+consultantGender+"']/.."));
			driver.waitForLoadingImageToDisappear();
			logger.info("gender radio button is clicked at attempt "+i);
			if(verifyGenderRadioBtnSelected(TestConstants.CONSULTANT_GENDER)==false){
				continue;
			}else{
				break;
			}
		}
	}

	public boolean verifyGenderRadioBtnSelected(String gender){
		return driver.isElementPresent(By.xpath("//input[@id='"+gender+"' and @checked='checked']/.."));
	}

	public void clickOnSaveAfterEnterSpouseDetails() {
		driver.waitForElementPresent(By.id("saveAccountInfo"));
		driver.click(By.id("saveAccountInfo"));
		try{
			driver.quickWaitForElementPresent(By.id("acceptSpouse"));
			if(driver.isElementPresent(By.id("acceptSpouse"))){
				driver.click(By.id("acceptSpouse"));
				logger.info("acceptSpouse pop up handled");
				driver.waitForLoadingImageToDisappear();
				driver.click(By.id("saveAccountInfo"));
			}else if(driver.isElementPresent(By.id("QAS_AcceptOriginal"))){
				driver.click(By.id("QAS_AcceptOriginal"));
				logger.info("Accept as original button clicked");
				driver.waitForLoadingImageToDisappear();
			}
		}catch(Exception e){
			logger.info("acceptSpouse popup not present");
		}
		driver.waitForLoadingImageToAppear();
		driver.waitForPageLoad();
	}

	public void enterBirthDateOnAccountInfoPage() {
		driver.click(By.xpath(String.format(ACCOUNT_INFO_DAY_OF_BIRTH_LOC, TestConstants.CONSULTANT_DAY_OF_BIRTH)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_YEAR_OF_BIRTH_LOC, TestConstants.CONSULTANT_YEAR_OF_BIRTH)));
		driver.click(By.xpath(String.format(ACCOUNT_INFO_MONTH_OF_BIRTH_LOC,TestConstants.CONSULTANT_MONTH_OF_BIRTH)));
	}

	public StoreFrontAccountInfoPage enterMobileNumber(String phoneNumberCa) {
		driver.waitForElementPresent(By.id("mobilenumber"));
		driver.clear(By.id("mobilenumber"));
		driver.type(By.id("mobilenumber"), phoneNumberCa);
		driver.waitForElementPresent(ACCOUNT_SAVE_BUTTON_LOC);
		driver.click(ACCOUNT_SAVE_BUTTON_LOC);
		driver.pauseExecutionFor(2000);
		logger.info("Save account info button clicked");
		return new StoreFrontAccountInfoPage(driver);		  
	}

	public void enterNameOfUser(String firstName,String lastName) {
		driver.waitForElementPresent(ACCOUNT_INFO_FIRST_NAME_LOC);
		driver.type(ACCOUNT_INFO_FIRST_NAME_LOC, firstName);
		driver.type(ACCOUNT_INFO_LAST_NAME_LOC, lastName);		  
	}

	public boolean verifyEmailAddressOnAccountInfoPage(String consultantEmailID) {
		return driver.findElement(By.id("username-account")).getAttribute("value").contains(consultantEmailID);
	}

	public boolean validateCountryCanOrNotBeModified(){
		driver.waitForElementPresent(By.xpath("//div[contains(@class,'country-read-only')]/input[@disabled='disabled']"));
		return driver.isElementPresent(By.xpath("//div[contains(@class,'country-read-only')]/input[@disabled='disabled']"));
	}

	public void clickMeetYourConsultantLink(){
		driver.waitForElementPresent(By.xpath("//div[@id='header-middle-top']//a"));
		driver.click(By.xpath("//div[@id='header-middle-top']//a"));
		logger.info("Meet your consultant link is clicked");
	}

	public boolean verifyFirstNameAndLastNameAtMeetYourConsultantSection(String firstName,String lastName){
		driver.waitForElementPresent(By.xpath("//div[@id='content-left-menu']/following-sibling::div[1]"));
		String textOfFirstLastName = driver.findElement(By.xpath("//div[@id='content-left-menu']/following-sibling::div[1]")).getText();
		logger.info("Fetched text of first and last name==="+textOfFirstLastName);
		if(textOfFirstLastName.equalsIgnoreCase(firstName+" "+lastName)){
			return true;
		}
		return false;
	}

	public boolean verifyFirstNameAndLastNameAtMainAccountInfoduringPlacingAdhocOrder(String firstName,String lastName){
		driver.waitForElementPresent(By.xpath("//div[@id='checkout_summary_deliveryaddress_div']/div[2]/div[1]/div[1]"));
		String textOfFirstLastName = driver.findElement(By.xpath("//div[@id='checkout_summary_deliveryaddress_div']/div[2]/div[1]/div[1]")).getText();
		logger.info("Fetched text of first and last name==="+textOfFirstLastName);
		if(textOfFirstLastName.equalsIgnoreCase(firstName+" "+lastName)){
			return true;
		}
		return false;
	}

	public String getFirstNameFromAccountInfo(){
		driver.waitForElementPresent(ACCOUNT_INFO_FIRST_NAME_LOC);
		String firstName=driver.findElement(ACCOUNT_INFO_FIRST_NAME_LOC).getAttribute("value");
		logger.info("FirstName from UI is "+firstName);
		return firstName;
	}

	public String getLastNameFromAccountInfo(){
		driver.waitForElementPresent(ACCOUNT_INFO_LAST_NAME_LOC);
		String firstName=driver.findElement(ACCOUNT_INFO_LAST_NAME_LOC).getAttribute("value");
		logger.info("LastName from UI is "+firstName);
		return firstName;
	}

	public void enterEmailAddress(String email) throws InterruptedException {
		driver.clear(By.id("email-account"));
		driver.type(By.id("email-account"),email+"\t");
		logger.info("entered Email Adrress is "+email);
		driver.waitForLoadingImageToDisappear();
	}

	public String getNextBillDateOfPulseTemplate(){
		driver.waitForElementPresent(NEXT_BILL_DATE_OF_PULSE_TEMPLATE);
		String billDate=driver.findElement(NEXT_BILL_DATE_OF_PULSE_TEMPLATE).getText();
		logger.info("Billdate from UI is "+billDate);
		return billDate;
	}

	public String getNextDueDateOfCRPTemplate(){
		driver.waitForElementPresent(NEXT_DUE_DATE_OF_CRP_TEMPLATE);
		String dueDate=driver.findElement(NEXT_DUE_DATE_OF_CRP_TEMPLATE).getText();
		logger.info("Due date of CRP from UI is "+dueDate);
		return dueDate;
	}

	public String getNextDueDateOfPulseSubscriptionTemplate(){
		driver.waitForElementPresent(NEXT_DUE_DATE_PULSE_SUBSCRIPTION_STATUS);
		String dueDate=driver.findElement(NEXT_DUE_DATE_PULSE_SUBSCRIPTION_STATUS).getText();
		logger.info("Due date of CRP from UI is "+dueDate);
		return dueDate;
	}

	public boolean verifyThresholdErrorMsgPresent() {
		return driver.isElementPresent(By.xpath("//div[@id='shopping-wrapper']/div[contains(@class,'error')]"));
	}

	public void enterPhoneNumberAndPostalCode(){
		if(driver.getCountry().equalsIgnoreCase("ca")){
			driver.waitForElementPresent(By.id("postal-code"));
			driver.clear(By.id("postal-code"));
			driver.type(By.id("postal-code"), TestConstants.POSTAL_CODE_CA);
			driver.clear(By.id("phonenumber"));
			driver.type(By.id("phonenumber"), TestConstants.PHONE_NUMBER_CA);
		}
		else{
			driver.waitForElementPresent(By.id("postal-code"));
			driver.clear(By.id("postal-code"));
			driver.type(By.id("postal-code"), TestConstants.POSTAL_CODE_US);
			driver.clear(By.id("phonenumber"));
			driver.type(By.id("phonenumber"), TestConstants.PHONE_NUMBER_US);
		}
	}

	public String updateStateAndReturnName(String state){
		Select stateDD = new Select(driver.findElement(By.xpath("//select[@id='state']")));
		stateDD.selectByVisibleText(state);
		return state;
	}

	public boolean validateClickXOnProvideAccessToSpousePopup(){
		actions = new Actions(RFWebsiteDriver.driver);
		int randomNumber =  CommonUtils.getRandomNum(10000, 1000000);
		String spouseFirstName="Mary"+randomNumber;
		String spouseLastName="Rose";
		driver.waitForElementTobeEnabled(By.xpath("//input[@id='spouse-first']"));
		driver.pauseExecutionFor(5000);
		driver.clear(By.xpath("//input[@id='spouse-first']"));
		driver.type(By.xpath("//input[@id='spouse-first']"),spouseFirstName);
		driver.clear(By.xpath("//input[@id='spouse-last']"));
		driver.type(By.xpath("//input[@id='spouse-last']"),spouseLastName);
		//actions.sendKeys(Keys.TAB).build().perform();
		driver.click(By.xpath("//input[@id='old-password-account']"));
		driver.pauseExecutionFor(2000);
		driver.quickWaitForElementPresent(By.xpath("//span[@class='icon-close']"));
		driver.click(By.xpath("//span[@class='icon-close']"));
		driver.pauseExecutionFor(1500);
		return !driver.findElement(By.xpath("//input[@id='spouse-first']")).getAttribute("value").contains(spouseFirstName);
		//return driver.findElement(By.xpath("//input[@id='cancelSpouse']")).isDisplayed() || driver.findElement(By.xpath("//input[@id='spouse-first']")).isDisplayed();
	}

	public String getSpouseFirstName(){
		driver.waitForElementPresent(By.xpath("//input[@id='spouse-first']"));
		String spouseFirstName=driver.findElement(By.xpath("//input[@id='spouse-first']")).getAttribute("value");
		logger.info("Spouse first name from UI is "+spouseFirstName);
		return spouseFirstName;
	}

	public String getSpouseLastName(){
		driver.waitForElementPresent(By.xpath("//input[@id='spouse-last']"));
		String spouseLastName=driver.findElement(By.xpath("//input[@id='spouse-last']")).getAttribute("value");
		logger.info("Spouse last name from UI is "+spouseLastName);
		return spouseLastName;
	}

}