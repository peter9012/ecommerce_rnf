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
		driver.click(BUSINESS_LINK_LOC);
		logger.info("Our business Link clicked");
	}

	public StoreFrontEnrollNowPage clickOnOurEnrollNowLink(){
		driver.waitForElementPresent(ENROLL_NOW_LINK_LOC);
		driver.click(ENROLL_NOW_LINK_LOC);
		logger.info("Enroll Now Link clicked");
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
		driver.waitForPageLoad();
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
		driver.waitForPageLoad();
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
		driver.waitForPageLoad();
		return new StoreFrontPCUserPage(driver);
	}

	public void openConsultantPWS(String pwsURL){
		logger.info("User PWS is "+pwsURL);
		driver.get(pwsURL);		
	}

	public boolean isCurrentURLShowsError() throws InterruptedException{
		driver.waitForPageLoad();
		logger.info("Curremt URL is "+driver.getCurrentUrl());
		return driver.getCurrentUrl().contains("login?error=true");
	}

	public void searchCID() throws InterruptedException{
		driver.waitForElementPresent(By.cssSelector("input[id='sponserparam']"));
		driver.findElement(By.cssSelector("input[id='sponserparam']")).sendKeys("test");
		driver.waitForElementPresent(By.cssSelector("input[id='search-sponsor-button']"));
		driver.click(By.cssSelector("input[id='search-sponsor-button']"));
		logger.info("Sponsor entered as 'test' and search button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void searchCID(String cid) throws InterruptedException{
		driver.waitForElementPresent(By.cssSelector("input[id='sponserparam']"));
		driver.findElement(By.cssSelector("input[id='sponserparam']")).sendKeys(cid);
		driver.waitForElementPresent(By.cssSelector("input[id='search-sponsor-button']"));
		driver.click(By.cssSelector("input[id='search-sponsor-button']"));		
	}

	public void mouseHoverSponsorDataAndClickContinue() throws InterruptedException{
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@class='the-search-results']/form[1]/div[@class='sponsorDataDiv']"))).click(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).build().perform();
		logger.info("First result of sponsor has been clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void selectEnrollmentKitPage(String kitPrice,String regimenName){
		driver.waitForLoadingImageToDisappear();
		kitPrice =  kitPrice.toUpperCase();
		driver.waitForElementPresent(By.xpath("//div[@class='kit-price' and contains(text(),'"+kitPrice+"')]"));
		logger.info("Enrollment Kit is selected as "+kitPrice);
		driver.click(By.xpath("//div[@class='kit-price' and contains(text(),'"+kitPrice+"')]"));
		regimenName = regimenName.toUpperCase();
		driver.pauseExecutionFor(500);
		driver.click(By.xpath("//div[@class='regimen-name' and text()='"+regimenName+"']"));
		driver.pauseExecutionFor(500);
		logger.info("Regimen is selected as "+regimenName);
		driver.click(By.cssSelector("input[value='Next']"));
		logger.info("Next button clicked after selected Kit and regimen");
		driver.waitForLoadingImageToDisappear();
	}

	public void chooseEnrollmentOption(String option){
		option = option.toUpperCase();
		if(option.equalsIgnoreCase("EXPRESS ENROLLMENT")){
			driver.waitForElementPresent(By.id("express-enrollment"));
			driver.click(By.id("express-enrollment"));
		}
		else{
			driver.waitForElementPresent(By.id("standard-enrollment"));
			driver.click(By.id("standard-enrollment"));
		}
		logger.info("Enrollment Type is selected as "+option);
		driver.click(By.cssSelector("input[value='Next']"));
		logger.info("Next button is clicked after selecting enrollment type");
		driver.waitForLoadingImageToDisappear();
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
		driver.waitForSpinImageToDisappear();
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

	public void clickEnrollmentNextBtn() throws InterruptedException{
		driver.waitForElementPresent(By.id("enrollment-next-button"));
		driver.pauseExecutionFor(2000);
		driver.click(By.id("enrollment-next-button"));
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public boolean verifySubsribeToPulseCheckBoxIsSelected() throws InterruptedException{		
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input[@checked='checked']"));
		return driver.findElement(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input")).isSelected();
	}

	public boolean verifySubsribeToPulseCheckBoxIsNotSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input"));
		return !driver.findElement(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input")).isSelected();
	}

	public boolean verifyEnrollToCRPCheckBoxIsSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input[@checked='checked']"));
		return driver.findElement(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input")).isSelected();
	}

	public boolean verifyEnrollToCRPCheckBoxIsNotSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input"));
		return !driver.findElement(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input")).isSelected();
	}

	public void uncheckPulseAndCRPEnrollment() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input"));
		driver.click(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input/.."));
		driver.click(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input/.."));
		driver.pauseExecutionFor(2000);
	}


	public void acceptTheVerifyYourShippingAddressPop() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='QAS_AcceptOriginal']"));
		driver.waitForElementPresent(By.xpath("//input[@id='QAS_AcceptOriginal']"));
		driver.click(By.xpath("//input[@id='QAS_AcceptOriginal']"));		
	}

	public void enterCardNumber(String cardNumber){
		driver.waitForElementPresent(By.cssSelector("input[id='card-nr']"));
		driver.findElement(By.cssSelector("input[id='card-nr']")).sendKeys(cardNumber);
	}

	public void enterNameOnCard(String nameOnCard){
		driver.findElement(By.cssSelector("input[id='card-name']")).sendKeys(nameOnCard);
	}

	public boolean validateInvalidCreditCardMessage(){
		if(driver.findElement(By.xpath("//div[contains(text(),'Please enter a valid')]")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean validateEmptyCreditCardMessage(){
		if(driver.findElement(By.xpath("//div[contains(text(),'This field')]")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}

	public void clearCreditCardNumber(){
		driver.findElement(By.xpath("//input[@id='card-nr']")).clear();
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
	}

	public void enterNameAsItAppearsOnCard(String nameOnCard){
		driver.findElement(By.cssSelector("input[id='name-on-card']")).sendKeys(nameOnCard);
	}

	public void checkThePoliciesAndProceduresCheckBox() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='policies-check']/.."));
		driver.click(By.xpath("//input[@id='policies-check']/.."));
	}

	public void checkTheIAcknowledgeCheckBox(){
		driver.click(By.xpath("//input[@id='acknowledge-check']/.."));
	}

	public boolean isTheTermsAndConditionsCheckBoxDisplayed(){
		driver.waitForElementPresent(By.xpath("//input[@id='terms-check']/.."));
		return driver.IsElementVisible(driver.findElement(By.xpath("//input[@id='terms-check']/..")));
	}

	public void checkTheTermsAndConditionsCheckBox(){
		driver.waitForElementPresent(By.xpath("//input[@id='terms-check']/.."));
		driver.click(By.xpath("//input[@id='terms-check']/.."));		
	}

	public void clickOnEnrollmentNextButton(){
		driver.waitForElementPresent(By.xpath("//*[@id='enrollment-next-button']"));
		driver.click(By.xpath("//*[@id='enrollment-next-button']")); 
	}
	public boolean verifyPopUpForPoliciesAndProcedures() throws InterruptedException{
		boolean isPopForTermsAndConditionsVisible = false;		
		driver.waitForElementPresent(By.xpath("//div[@class='popup-standard']/h2[text()='Please review and accept all policies and procedures.']"));
		isPopForTermsAndConditionsVisible = driver.IsElementVisible(driver.findElement(By.xpath("//div[@class='popup-standard']/h2[text()='Please review and accept all policies and procedures.']")));
		if(isPopForTermsAndConditionsVisible==true){
			driver.click(By.xpath("//div[@class='popup-standard']/h2[text()='Please review and accept all policies and procedures.']/following::a[@title='Close']"));
			driver.pauseExecutionFor(1000);
			return true;
		}
		return false;
	}

	public void checkTheIAgreeCheckBox(){
		driver.click(By.xpath("//input[@id='electronically-check']/.."));
	}

	public void clickOnChargeMyCardAndEnrollMeBtn() throws InterruptedException{
		driver.waitForElementPresent(By.cssSelector("input[id='enroll-button']"));
		driver.click(By.cssSelector("input[id='enroll-button']"));
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnEnrollMeBtn() throws InterruptedException{
		clickOnChargeMyCardAndEnrollMeBtn();
	}

	public void clickOnConfirmAutomaticPayment() throws InterruptedException{
		driver.waitForElementPresent(By.cssSelector("input[id='enroll']"));
		driver.click(By.cssSelector("input[id='enroll']"));
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyCongratsMessage(){
		driver.waitForElementPresent(By.xpath("//div[@id='Congrats']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='Congrats']")));
	}

	public void clickOnAddToCRPButtonCreatingCRPUnderBizSite() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='quick-refine']/following::div[2]/div[1]/div[2]//input[@value='Add to crp']"));
		driver.click(By.xpath("//div[@id='quick-refine']/following::div[2]/div[1]/div[2]//input[@value='Add to crp']"));
		logger.info("Add to CRP button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnCRPCheckout(){
		driver.waitForElementPresent(By.xpath("//input[@id='crpCheckoutButton']"));
		driver.click(By.xpath("//input[@id='crpCheckoutButton']"));
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnUpdateCartShippingNextStepBtnDuringEnrollment() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.xpath("//input[@class='use_address']"));
		//action.moveToElement(driver.findElement(By.xpath("//input[@id='use_address']"))).double.build().perform();		
		action.moveToElement(driver.findElement(By.xpath("//input[@class='use_address']"))).click(driver.findElement(By.xpath("//input[@class='use_address']"))).build().perform();
		logger.info("Next button on shipping update cart clicked");	
		driver.waitForLoadingImageToDisappear();
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
		driver.pauseExecutionFor(1000);
	}

	public boolean validateErrorMessageWithoutSelectingAllCheckboxes(){
		driver.waitForElementPresent(By.xpath("//h2[contains(text(),'Please accept our terms & conditions')]"));
		return driver.findElement(By.xpath("//h2[contains(text(),'Please accept our terms & conditions')]")).isDisplayed();
	}

	public void acceptTheProvideAccessToSpousePopup(){
		if(driver.findElement(By.xpath("//input[@id='acceptSpouse']")).isDisplayed()){
			driver.click(By.xpath("//input[@id='acceptSpouse']"));
		}
	}

}
