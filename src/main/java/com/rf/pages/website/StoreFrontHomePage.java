package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;


public class StoreFrontHomePage extends RFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(StoreFrontHomePage.class.getName());
	private Actions actions;

	private final By BUSINESS_LINK_LOC = By.cssSelector("a[id='corp-opp']"); 
	private final By ENROLL_NOW_LINK_LOC = By.cssSelector("a[title='Enroll Now']");	
	private final By LOGIN_LINK_LOC = By.cssSelector("li[id='log-in-button']>a");
	private final By LOGIN_BTN_LOC = By.cssSelector("input[value='Log in']");
	private final By USERNAME_TXTFLD_LOC = By.id("username");
	private final By PASSWORD_TXTFLD_LOC = By.id("password");


	public StoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public void clickOnOurBusinessLink(){
		driver.waitForElementPresent(BUSINESS_LINK_LOC);
		driver.findElement(BUSINESS_LINK_LOC).click();
	}

	public StoreFrontEnrollNowPage clickOnOurEnrollNowLink(){
		driver.waitForElementPresent(ENROLL_NOW_LINK_LOC);
		driver.findElement(ENROLL_NOW_LINK_LOC).click();
		return new StoreFrontEnrollNowPage(driver);
	}

	public StoreFrontConsultantPage loginAsConsultant(String username,String password){
		driver.waitForElementPresent(LOGIN_LINK_LOC);
		driver.click(LOGIN_LINK_LOC);
		logger.info("login link clicked");
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is: "+username);
		logger.info("login password is: "+password);
		driver.click(LOGIN_BTN_LOC);	
		logger.info("login button clicked");
		return new StoreFrontConsultantPage(driver);
	}

	public StoreFrontRCUserPage loginAsRCUser(String username,String password){
		driver.waitForElementPresent(LOGIN_LINK_LOC);
		driver.click(LOGIN_LINK_LOC);
		logger.info("login link clicked");
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is "+username);
		logger.info("login password is "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		return new StoreFrontRCUserPage(driver);
	}
	public StoreFrontPCUserPage loginAsPCUser(String username,String password){
		driver.waitForElementPresent(LOGIN_LINK_LOC);
		driver.click(LOGIN_LINK_LOC);		
		logger.info("login link clicked");
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is "+username);
		logger.info("login password is "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		return new StoreFrontPCUserPage(driver);
	}

	public void openConsultantPWS(String pwsURL){
		logger.info("User PWS is "+pwsURL);
		driver.get(pwsURL);		
	}

	public boolean isCurrentURLShowsError() throws InterruptedException{
		Thread.sleep(5000);
		logger.info("Curremt URL is "+driver.getCurrentUrl());
		return driver.getCurrentUrl().contains("login?error=true");
	}

	public void searchCID(String cid) throws InterruptedException{
		driver.waitForElementPresent(By.cssSelector("input[id='sponserparam']"));
		driver.findElement(By.cssSelector("input[id='sponserparam']")).sendKeys("test");
		driver.findElement(By.cssSelector("input[id='search-sponsor-button']")).click();
		Thread.sleep(3000);
	}

	public void mouseHoverSponsorDataAndClickContinue() throws InterruptedException{
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@class='the-search-results']/form[1]/div[@class='sponsorDataDiv']"))).click(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).build().perform();
		Thread.sleep(3000);
	}

	public void selectEnrollmentKitPage(String kitPrice,String regimenName){
		kitPrice =  kitPrice.toUpperCase();
		driver.waitForElementPresent(By.xpath("//div[@class='kit-price' and contains(text(),'"+kitPrice+"')]"));
		driver.findElement(By.xpath("//div[@class='kit-price' and contains(text(),'"+kitPrice+"')]")).click();
		regimenName = regimenName.toUpperCase();
		driver.findElement(By.xpath("//div[@class='regimen-name' and text()='"+regimenName+"']")).click();
		driver.findElement(By.cssSelector("input[value='Next']")).click();
	}

	public void chooseEnrollmentOption(String option){
		option = option.toUpperCase();
		if(option.equalsIgnoreCase("EXPRESS ENROLLMENT")){
			driver.waitForElementPresent(By.id("express-enrollment"));
			driver.findElement(By.id("express-enrollment")).click();
		}
		else{
			driver.waitForElementPresent(By.id("standard-enrollment"));
			driver.findElement(By.id("standard-enrollment")).click();
		}
		driver.findElement(By.cssSelector("input[value='Next']")).click();

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
		driver.findElement(By.cssSelector("a[title='Close']")).click();
	}	

	public Boolean checkExistenceOfEmailAddress() throws InterruptedException{
		driver.findElement(By.id("email-account")).sendKeys("\t");
		Thread.sleep(3000);
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

	public void clickEnrollmentNextBtn() throws InterruptedException{
		driver.findElement(By.id("enrollment-next-button")).click();
		Thread.sleep(5000);
	}

	public boolean verifySubsribeToPulseCheckBoxIsSelected() throws InterruptedException{
		Thread.sleep(3000);
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input"));
		return driver.findElement(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input")).isSelected();
	}

	public boolean verifySubsribeToPulseCheckBoxIsNotSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input"));
		return !driver.findElement(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input")).isSelected();
	}

	public boolean verifyEnrollToCRPCheckBoxIsSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input"));
		return driver.findElement(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input")).isSelected();
	}

	public boolean verifyEnrollToCRPCheckBoxIsNotSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input"));
		return !driver.findElement(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input")).isSelected();
	}

	public void uncheckPulseAndCRPEnrollment() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input"));
		driver.findElement(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input/..")).click();
		driver.findElement(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input/..")).click();
		Thread.sleep(2000);
	}


	public void acceptTheVerifyYourShippingAddressPop() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='QAS_AcceptOriginal']"));
		driver.waitForElementPresent(By.xpath("//input[@id='QAS_AcceptOriginal']"));
		driver.findElement(By.xpath("//input[@id='QAS_AcceptOriginal']")).click();
		Thread.sleep(5000); 
	}

	public void enterCardNumber(String cardNumber){
		driver.waitForElementPresent(By.cssSelector("input[id='card-nr']"));
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

	public void enterSocialInsuranceNumber(String sin) throws InterruptedException{
		logger.info("Social Insurance Number is "+sin);
		driver.findElement(By.cssSelector("input[id='S-S-N']")).sendKeys(sin+"\t");
		Thread.sleep(3000);
	}

	public void enterNameAsItAppearsOnCard(String nameOnCard){
		driver.findElement(By.cssSelector("input[id='name-on-card']")).sendKeys(nameOnCard);
	}

	public void checkThePoliciesAndProceduresCheckBox() throws InterruptedException{
		Thread.sleep(5000);
		driver.waitForElementPresent(By.xpath("//input[@id='policies-check']/.."));
		driver.findElement(By.xpath("//input[@id='policies-check']/..")).click();
	}

	public void checkTheIAcknowledgeCheckBox(){
		driver.findElement(By.xpath("//input[@id='acknowledge-check']/..")).click();
	}

	public boolean isTheTermsAndConditionsCheckBoxDisplayed(){
		driver.waitForElementPresent(By.xpath("//input[@id='terms-check']/.."));
		return driver.IsElementVisible(driver.findElement(By.xpath("//input[@id='terms-check']/..")));
	}

	public void checkTheTermsAndConditionsCheckBox(){
		driver.findElement(By.xpath("//input[@id='terms-check']/..")).click();		
	}

	public boolean verifyPopUpForPoliciesAndProcedures() throws InterruptedException{
		boolean isPopForTermsAndConditionsVisible = false;
		Thread.sleep(3000);
		isPopForTermsAndConditionsVisible = driver.IsElementVisible(driver.findElement(By.xpath("//div[@class='popup-standard']/h2[text()='Please review and accept all policies and procedures.']")));
		if(isPopForTermsAndConditionsVisible==true){
			driver.findElement(By.xpath("//div[@class='popup-standard']/h2[text()='Please review and accept all policies and procedures.']/following::a[@title='Close']")).click();
			Thread.sleep(5000);
			return true;
		}
		return false;
	}

	public void checkTheIAgreeCheckBox(){
		driver.findElement(By.xpath("//input[@id='electronically-check']/..")).click();
	}

	public void clickOnChargeMyCardAndEnrollMeBtn() throws InterruptedException{
		driver.findElement(By.cssSelector("input[id='enroll-button']")).click();
		Thread.sleep(3000);
	}

	public void clickOnEnrollMeBtn() throws InterruptedException{
		driver.findElement(By.cssSelector("input[id='enroll-button']")).click();
		Thread.sleep(3000);
	}

	public void clickOnConfirmAutomaticPayment() throws InterruptedException{
		//driver.waitForElementToBeClickable(By.cssSelector("input[id='enroll']"), 10);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("input[id='enroll']")).click();
		Thread.sleep(10000);

	}

	public boolean verifyCongratsMessage(){
		driver.waitForElementPresent(By.xpath("//div[@id='Congrats']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='Congrats']")));
	}

	public void clickOnAddToCRPButtonCreatingCRPUnderBizSite() throws InterruptedException{
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select"));
			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select/option[2]")).click();
		}catch(NoSuchElementException e){

		}
		driver.waitForElementPresent(By.xpath("//div[@id='quick-refine']/following::div[2]/div[1]/div[2]//input[@value='Add to crp']"));
		driver.findElement(By.xpath("//div[@id='quick-refine']/following::div[2]/div[1]/div[2]//input[@value='Add to crp']")).click();
		logger.info("Add to CRP button clicked");
	}

	public void clickOnCRPCheckout(){
		driver.waitForElementPresent(By.xpath("//input[@id='crpCheckoutButton']"));
		driver.findElement(By.xpath("//input[@id='crpCheckoutButton']")).click();
	}

	public void clickOnUpdateCartShippingNextStepBtnDuringEnrollment() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		Thread.sleep(5000);
		driver.waitForElementPresent(By.xpath("//input[@class='use_address']"));
		//action.moveToElement(driver.findElement(By.xpath("//input[@id='use_address']"))).doubleClick().build().perform();		
		action.moveToElement(driver.findElement(By.xpath("//input[@class='use_address']"))).click(driver.findElement(By.xpath("//input[@class='use_address']"))).build().perform();
		logger.info("Next button on shipping update cart clicked");
		Thread.sleep(2000);
	}

	public boolean verifyOrderConfirmation(){
		logger.info("Asserting Order Confirmation Message");
		driver.waitForElementPresent(By.xpath("//div[@id='order-confirm']/span"));
		if(driver.findElement(By.xpath("//div[@id='order-confirm']/span")).getText().equalsIgnoreCase("Your CRP order has been created")){
			return true;
		}
		return false;
	}

	public void clickOnGoToMyAccountToCheckStatusOfCRP(){
		driver.waitForElementPresent(By.xpath("//div[@id='order-confirm']/a"));
		driver.click(By.xpath("//div[@id='order-confirm']/a"));
	}

	public void clickOnAccountInfoLinkLeftSidePannel(){
		driver.waitForElementPresent(By.xpath("//div[@id='left-menu']//a[text()='ACCOUNT INFO']"));
		driver.click(By.xpath("//div[@id='left-menu']//a[text()='ACCOUNT INFO']"));

	}

}
