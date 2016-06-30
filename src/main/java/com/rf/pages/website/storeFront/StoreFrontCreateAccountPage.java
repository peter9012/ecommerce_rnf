package com.rf.pages.website.storeFront;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;
import com.rf.core.driver.website.RFWebsiteDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontCreateAccountPage extends StoreFrontRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontCreateAccountPage.class.getName());

	public StoreFrontCreateAccountPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void enterFirstName(String firstName){
		driver.waitForElementPresent(By.id("first-name"));
		driver.type(By.id("first-name"),firstName);
		logger.info("first name entered as "+firstName);
	}

	public void enterLastName(String lastName){
		driver.type(By.id("last-name"),lastName);
	}

	public void enterEmailAddress(String emailAddress){		
		driver.type(By.id("email-account"),emailAddress);		
	}

	public void closePopUp(){
		driver.click(By.cssSelector("a[title='Close']"));
	}	

	public Boolean checkExistenceOfEmailAddress() throws InterruptedException{
		driver.type(By.id("email-account"),"\t");		
		try{
			driver.findElement(By.xpath("//div[text()='Please fix this field.']"));
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public void enterPassword(String password){
		driver.type(By.id("new-password-account"),password);
	}

	public void enterConfirmPassword(String password){
		driver.type(By.id("new-password-account2"),password);
	}

	public void enterAddressLine1(String addressLine1){
		driver.type(By.id("address-1"),addressLine1);
	}

	public void enterCity(String city){
		driver.type(By.id("city"),city);
	}

	public void selectProvince(String province){
		Select provinceDD = new Select(driver.findElement(By.id("state")));
		provinceDD.selectByVisibleText(province);
	}

	public void enterPostalCode(String postalCode){
		driver.type(By.id("postcode"),postalCode);
	}

	public void enterPhoneNumber(String phnNum){
		driver.type(By.id("phonenumber"),phnNum);
	}

	public void clickEnrollmentNextBtn(){
		driver.click(By.id("enrollment-next-button"));
	}

	public void acceptTheVerifyYourShippingAddressPop(){
		driver.waitForLoadingImageToDisappear();
		driver.quickWaitForElementPresent(By.id("QAS_RefineBtn"));
		driver.click(By.id("QAS_RefineBtn"));
	}

	public void enterCardNumber(String cardNumber){
		driver.type(By.id("card-nr"),cardNumber);
	}

	public void enterNameOnCard(String nameOnCard){
		driver.type(By.id("card-name"),nameOnCard);
	}

	public void selectExpirationDate(String month,String year){
		Select monthDD = new Select(driver.findElement(By.id("expiryMonth")));
		Select yearDD = new Select(driver.findElement(By.id("expiryYear")));
		monthDD.selectByVisibleText(month.toUpperCase());
		yearDD.selectByVisibleText(year);		
	}

	public void enterSecurityCode(String securityCode){
		driver.type(By.id("security-code"),securityCode);
	}

	public void enterSocialInsuranceNumber(String sin){
		driver.type(By.id("S-S-N"),sin);
	}

	public void enterNameAsItAppearsOnCard(String nameOnCard){
		driver.type(By.id("name-on-card"),nameOnCard);
	}

	public void checKThePoliciesAndProceduresCheckBox(){
		driver.click(By.xpath("//input[@id='policies-check']/.."));
	}

	public void checKTheIAcknowledgeCheckBox(){
		driver.click(By.xpath("//input[@id='acknowledge-check']/.."));
	}

	public void checKTheTermsAndConditionsCheckBox(){
		driver.click(By.xpath("//input[@id='terms-check']/.."));
	}

	public void checKTheIAgreeCheckBox(){
		driver.click(By.xpath("//input[@id='electronically-check']/.."));
	}

	public void clickOnChargeMyCardAndEnrollMeBtn(){
		driver.click(By.id("enroll-button"));
	}

	public StoreFrontConsultantEnrollmentConfirmationPage clickOnConfirmAutomaticPayment() throws InterruptedException{
		//driver.waitForElementToBeClickable(By.cssSelector("input[id='enroll']"), 10);
		driver.waitForElementPresent(By.id("enroll"));
		driver.click(By.id("enroll"));
		return new StoreFrontConsultantEnrollmentConfirmationPage(driver);

	}

}




