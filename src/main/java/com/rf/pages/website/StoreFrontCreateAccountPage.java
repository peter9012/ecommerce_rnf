package com.rf.pages.website;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;
import com.rf.core.driver.website.RFWebsiteDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontCreateAccountPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontCreateAccountPage.class.getName());

	public StoreFrontCreateAccountPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void enterFirstName(String firstName){
		driver.waitForElementPresent(By.id("first-name"));
		driver.findElement(By.id("first-name")).sendKeys(firstName);
		logger.info("first name entered as "+firstName);
	}

	public void enterLastName(String lastName){
		driver.findElement(By.id("last-name")).sendKeys(lastName);
	}

	public void enterEmailAddress(String emailAddress){		
		driver.findElement(By.id("email-account")).sendKeys(emailAddress);		
	}

	public void closePopUp(){
		driver.click(By.cssSelector("a[title='Close']"));
	}	

	public Boolean checkExistenceOfEmailAddress() throws InterruptedException{
		driver.findElement(By.id("email-account")).sendKeys("\t");		
		try{
			driver.findElement(By.xpath("//div[text()='Please fix this field.']"));
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public void enterPassword(String password){
		driver.findElement(By.id("new-password-account")).sendKeys(password);
	}

	public void enterConfirmPassword(String password){
		driver.findElement(By.id("new-password-account2")).sendKeys(password);
	}

	public void enterAddressLine1(String addressLine1){
		driver.findElement(By.id("address-1")).sendKeys(addressLine1);
	}

	public void enterCity(String city){
		driver.findElement(By.id("city")).sendKeys(city);
	}

	public void selectProvince(String province){
		Select provinceDD = new Select(driver.findElement(By.id("state")));
		provinceDD.selectByVisibleText(province);
	}

	public void enterPostalCode(String postalCode){
		driver.findElement(By.id("postcode")).sendKeys(postalCode);
	}

	public void enterPhoneNumber(String phnNum){
		driver.findElement(By.id("phonenumber")).sendKeys(phnNum);
	}

	public void clickEnrollmentNextBtn(){
		driver.click(By.id("enrollment-next-button"));
	}

	public void acceptTheVerifyYourShippingAddressPop(){
		driver.click(By.cssSelector("input[id='QAS_RefineBtn']"));
	}

	public void enterCardNumber(String cardNumber){
		driver.findElement(By.cssSelector("input[id='card-nr']")).sendKeys(cardNumber);
	}

	public void enterNameOnCard(String nameOnCard){
		driver.findElement(By.cssSelector("input[id='card-name']")).sendKeys(nameOnCard);
	}

	public void selectExpirationDate(String month,String year){
		Select monthDD = new Select(driver.findElement(By.cssSelector("select[id='expiryMonth']")));
		Select yearDD = new Select(driver.findElement(By.cssSelector("select[id='expiryYear']")));
		monthDD.selectByVisibleText(month.toUpperCase());
		yearDD.selectByVisibleText(year);		
	}

	public void enterSecurityCode(String securityCode){
		driver.findElement(By.cssSelector("input[id='security-code']")).sendKeys(securityCode);
	}

	public void enterSocialInsuranceNumber(String sin){
		driver.findElement(By.cssSelector("input[id='S-S-N']")).sendKeys(sin);
	}

	public void enterNameAsItAppearsOnCard(String nameOnCard){
		driver.findElement(By.cssSelector("input[id='name-on-card']")).sendKeys(nameOnCard);
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
		driver.click(By.cssSelector("input[id='enroll-button']"));
	}

	public StoreFrontConsultantEnrollmentConfirmationPage clickOnConfirmAutomaticPayment() throws InterruptedException{
		//driver.waitForElementToBeClickable(By.cssSelector("input[id='enroll']"), 10);
		driver.waitForElementPresent(By.cssSelector("input[id='enroll']"));
		driver.click(By.cssSelector("input[id='enroll']"));
		return new StoreFrontConsultantEnrollmentConfirmationPage(driver);

	}

}




