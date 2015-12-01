package com.rf.pages.website;

import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;


public class StoreFrontHomePage extends RFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(StoreFrontHomePage.class.getName());
	private Actions actions;
	private final By LOGIN_LINK_LOC = By.cssSelector("li[id='log-in-button']>a");
	private final By USERNAME_TXTFLD_LOC = By.id("username");
	private final By PASSWORD_TXTFLD_LOC = By.id("password");
	private final By CONSULTANT_VALIDATION_POPUP_LESS_THAN_6_MONTH = By.xpath("//div[@id='inactiveConsultant180Popup']/div/div");
	private final By LOGIN_BTN_LOC = By.cssSelector("input[value='SIGN IN']");
	//private final By FIELD_SPONSOR_LINK_LOC = By.xpath("//div[@id='sponsorPage']/div/div/div[2]/div/div[1]/a");
	private final By FIELD_SPONSOR_LINK_LOC = By.xpath("//a[contains(text(),'Don’t Have an R+F Sponsor?')]");
	private final By CONFIRMATION_MESSAGE_LOC = By.xpath("//div[@id='sponsorPopup']/div/h2");
	private final By FORGOT_PASSWORD_LOC=By.xpath("//div[@id='header']//a[@id='show-recover-pass']");
	private String addressLine1=null;
	private String city=null;
	private String postalCode=null;

	public StoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public StoreFrontConsultantPage dismissPolicyPopup(){
		try {	
			driver.quickWaitForElementPresent(By.id("agree"));
			WebElement we = driver.findElement(By.xpath("//div[@class='shipping-popup-gray']/span[1]"));
			if (we.isDisplayed()){
				we.click();
				driver.click(By.xpath("//input[@value='Continue']"));
				driver.waitForLoadingImageToDisappear();
			}
			//do nothing
		}
		catch (Exception e) {
			System.out.println("Policy Popup Dialog not seen.");
		}
		return null;
	} 

	public void clickRenewLater()  {
		try{
			driver.quickWaitForElementPresent(By.xpath("//div[contains(@class,'fancybox-overlay')]//input[@id='renewLater']"));
			driver.click(By.xpath("//div[contains(@class,'fancybox-overlay')]//input[@id='renewLater']"));
			logger.info("Renew later button clicked");
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}
	}

	public StoreFrontConsultantPage loginAsConsultant(String username,String password){
		driver.waitForElementPresent(LOGIN_LINK_LOC);
		driver.click(LOGIN_LINK_LOC);
		logger.info("login link clicked");
		logger.info("login username is: "+username);
		logger.info("login password is: "+password);
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);			
		driver.click(LOGIN_BTN_LOC);	
		dismissPolicyPopup();
		clickRenewLater();
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
		dismissPolicyPopup();
		clickRenewLater();
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
		dismissPolicyPopup();
		clickRenewLater();
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
		try{
			driver.quickWaitForElementPresent(By.id("sponsor-name-id"));
			driver.findElement(By.id("sponsor-name-id")).sendKeys("test");
		}catch(NoSuchElementException e){
			driver.findElement(By.id("sponserparam")).sendKeys("test");
		}
		try{
			driver.click(By.xpath("//input[@value='Search']"));			
		}catch(NoSuchElementException e){
			driver.click(By.id("search-sponsor-button"));

		}

		logger.info("Sponsor entered as 'test' and search button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void searchCID(String cid) throws InterruptedException{
		try{
			driver.quickWaitForElementPresent(By.id("sponsor-name-id"));
			driver.findElement(By.id("sponsor-name-id")).sendKeys(cid);
		}catch(NoSuchElementException e){
			driver.findElement(By.id("sponserparam")).sendKeys(cid);
		}
		try{
			driver.click(By.xpath("//input[@value='Search']"));			
		}catch(NoSuchElementException e){
			driver.click(By.id("search-sponsor-button"));

		}
		logger.info("Sponsor entered as "+cid+" and search button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void searchCIDOnSelectASponsorPopUp(String cid){
		driver.waitForElementPresent(By.id("sponsorparam"));
		driver.findElement(By.id("sponsorparam")).sendKeys(cid);
		driver.waitForElementPresent(By.xpath("//input[@value='Search']"));
		driver.click(By.xpath("//input[@value='Search']"));
		logger.info("Sponsor entered as "+cid+" and search button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public String clickOnSelectAndContinueOnSelectASponsorPopUp(){
		driver.waitForElementPresent(By.xpath("//div[contains(@class,'the-sponsor-results')]/div[1]//input[@value='Select & Continue']"));
		String sponsorName = getSponsorNameFromSelectAndContinueSection();
		driver.click(By.xpath("//div[contains(@class,'the-sponsor-results')]/div[1]//input[@value='Select & Continue']"));
		logger.info("select and continue button clicked");
		driver.pauseExecutionFor(1000);
		driver.waitForPageLoad();
		return sponsorName;
	}

	public String getCIDFromUIAfterSponsorSearch(){
		driver.waitForElementPresent(By.xpath("//div[@class='the-search-results']/div[1]//ul/li[2]"));
		return driver.findElement(By.xpath("//div[@class='the-search-results']/div[1]//ul/li[2]")).getText();
	}

	public void mouseHoverSponsorDataAndClickContinue() throws InterruptedException{
		/*actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@class='sponsorDataDiv']"))).click(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).build().perform();*/
		JavascriptExecutor js = (JavascriptExecutor)(RFWebsiteDriver.driver);
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id='search-results']/div[1]/div[1]//input[@value='Select & Continue']")));
		logger.info("First result of sponsor has been clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void hoverOnBecomeAConsultantAndClickEnrollNowLink(){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.id("corp-opp")); 
		WebElement shopSkinCare = driver.findElement(By.id("corp-opp"));
		actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();
		WebElement allProducts = driver.findElement(By.xpath("//ul[@id='dropdown-menu' and @style='display: block;']//a[text()='Enroll Now']"));
		actions.moveToElement(allProducts).pause(1000).build().perform();
		while(true){
			try{
				driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath(" //ul[@id='dropdown-menu' and @style='display: block;']//a[text()='Enroll Now']")));

				break;
			}catch(Exception e){
				System.out.println("element not clicked..trying again");
				actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();

			}
		}
		logger.info("Enroll Now link clicked "); 
		driver.waitForPageLoad();
	}


	public void mouseHoverOtherSponsorDataAndClickContinue() throws InterruptedException{
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@class='the-search-results']/form[3]/div[@class='sponsorDataDiv']"))).click(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).build().perform();
		logger.info("Second result of sponsor has been clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void selectEnrollmentKitPage(String kitName,String regimenName){
		driver.waitForLoadingImageToDisappear();
		//kitPrice =  kitPrice.toUpperCase();
		driver.waitForElementPresent(By.xpath("//img[@title='"+kitName+"']"));
		//logger.info("EnrollmentTest Kit is selected as "+kitPrice);
		driver.pauseExecutionFor(500);
		System.out.println(kitName);
		//driver.click(By.xpath("//div[@class='imageCLass' and contains(text(),'"+kitPrice+"')]"));
		driver.click(By.xpath("//img[@title='"+kitName+"']"));
		regimenName = regimenName.toUpperCase();
		driver.pauseExecutionFor(500);
		//****next line span = div for old UI****
		Actions actions = new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//span[@class='regimen-name' and contains(.,'"+regimenName+"')]"))).click();
		/*driver.click(By.xpath("//span[@class='regimen-name' and contains(.,'"+regimenName+"')]"));*/
		driver.pauseExecutionFor(500);
		logger.info("Regimen is selected as "+regimenName);
		driver.click (By.id("next-button")); // - old UI (By.cssSelector("input[value='Next']"));
		logger.info("Next button clicked after selected Kit and regimen");
		driver.waitForLoadingImageToDisappear();
	}

	public void selectEnrollmentKitPage(String kitName){
		driver.waitForLoadingImageToDisappear();
		//kitPrice =  kitPrice.toUpperCase();
		driver.waitForElementPresent(By.xpath("//img[@title='"+kitName+"']"));
		//logger.info("EnrollmentTest Kit is selected as "+kitPrice);
		driver.pauseExecutionFor(500);
		System.out.println(kitName);
		//driver.click(By.xpath("//div[@class='imageCLass' and contains(text(),'"+kitPrice+"')]"));
		driver.click(By.xpath("//img[@title='"+kitName+"']"));
		driver.waitForLoadingImageToDisappear();
	}

	public void selectPortfolioEnrollmentKitPage(String kitName){
		driver.waitForLoadingImageToDisappear();
		kitName =  kitName.toUpperCase();
		driver.click(By.xpath("//img[@title='"+kitName+"']"));
		//		driver.waitForElementPresent(By.xpath("//div[@class='kit-price' and contains(text(),'"+kitPrice+"')]"));
		//		logger.info("EnrollmentTest Kit is selected as "+kitPrice);
		//		driver.click(By.xpath("//div[@class='kit-price' and contains(text(),'"+kitPrice+"')]"));
		//		logger.info("Next button clicked after selected Kit");
		//		driver.waitForLoadingImageToDisappear();
	}

	public void chooseEnrollmentOption(String option){
		option = option.toUpperCase();
		if(option.equalsIgnoreCase("EXPRESS ENROLLMENT")){
			driver.waitForElementPresent(By.id("express-enrollment"));
			driver.click(By.id("express-enrollment"));
			logger.info("Express EnrollmentTest is clicked");
		}
		else{
			driver.waitForElementPresent(By.id("standard-enrollment"));
			driver.click(By.id("standard-enrollment"));
			logger.info("Standard EnrollmentTest is clicked");
		}

		driver.click(By.cssSelector("input[value='Next']"));
		logger.info("Next button is clicked after selecting enrollment type");
		driver.waitForLoadingImageToDisappear();
	}

	public void enterFirstName(String firstName){
		driver.waitForElementPresent(By.id("first-name"));
		driver.type(By.id("first-name"), firstName);
		logger.info("first name entered as "+firstName);
	}

	public void enterLastName(String lastName){
		driver.type(By.id("last-name"),lastName);
	}

	public void enterEmailAddress(String emailAddress){	
		driver.pauseExecutionFor(2000);
		driver.type(By.id("email-account"), emailAddress+"\t");
		logger.info("email Address of the user is "+emailAddress);
		driver.waitForSpinImageToDisappear();
	}

	public void closePopUp(){
		driver.click(By.cssSelector("a[title='Close']"));
		driver.pauseExecutionFor(2000);
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
		driver.type(By.id("new-password-account"),password);
	}

	public void enterConfirmPassword(String password){
		driver.type(By.id("new-password-account2"),password);
	}

	public void enterAddressLine1(String addressLine1){
		driver.type(By.id("address-1"),addressLine1);
		logger.info("Address Line 1 entered is "+addressLine1);
	}

	public void enterCity(String city){
		driver.type(By.id("city"),city);
		logger.info("City entered is "+city);
	}

	public void selectProvince(){		
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
		driver.click(By.xpath("//select[@id='state']/option[2]"));
		logger.info("state selected");
	}

	public void selectProvince(String province){		
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
		driver.click(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
		logger.info("state selected");
	}

	public boolean verifyQuebecProvinceIsDisabled(){
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[contains(text(),'"+TestConstants.PROVINCE_QUEBEC+"')]"));
		return !(driver.findElement(By.xpath("//select[@id='state']/option[contains(text(),'"+TestConstants.PROVINCE_QUEBEC+"')]")).isEnabled());
	}

	public void enterPostalCode(String postalCode){
		driver.type(By.id("postcode"),postalCode);
		logger.info("postal code entered is "+postalCode);
	}

	public void enterPhoneNumber(String phnNum){
		driver.type(By.id("phonenumber"),phnNum);
		logger.info("phone number entered is "+phnNum);
	}

	public void clickEnrollmentNextBtn() throws InterruptedException{
		driver.waitForElementPresent(By.id("enrollment-next-button"));
		driver.pauseExecutionFor(2000);
		driver.click(By.id("enrollment-next-button"));
		logger.info("EnrollmentTest Next Button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		try{
			driver.turnOffImplicitWaits();
			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
			driver.click(By.id("QAS_AcceptOriginal"));
			logger.info("Accept the original button clicked");
		}
		catch(Exception e){
			logger.info("Accept the original pop up was NOT present");
		}
		finally{
			driver.turnOnImplicitWaits();
		}
	}

	public void clickNextButton(){
		driver.waitForElementPresent(By.id("enrollment-next-button"));
		driver.pauseExecutionFor(2000);
		driver.click(By.id("enrollment-next-button"));
		logger.info("EnrollmentTest Next Button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		try{
			driver.turnOffImplicitWaits();
			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
			driver.click(By.id("QAS_AcceptOriginal"));
			logger.info("Accept the original button clicked");
		}
		catch(Exception e){
			logger.info("Accept the original pop up was NOT present");
		}
		finally{
			driver.turnOnImplicitWaits();
		}
	}

	public boolean verifySubsribeToPulseCheckBoxIsSelected() throws InterruptedException{		
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input[@checked='checked']"));
		return driver.isElementPresent(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input[@checked='checked']"));
	}

	public boolean verifySubsribeToPulseCheckBoxIsNotSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input"));
		return !driver.findElement(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input")).isSelected();
	}

	public boolean verifyEnrollToCRPCheckBoxIsSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input[@checked='checked']"));
		return driver.isElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input[@checked='checked']"));
	}

	public boolean verifyEnrollToCRPCheckBoxIsNotSelected(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input"));
		return !driver.findElement(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input")).isSelected();
	}

	public void uncheckPulseAndCRPEnrollment() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input"));
		driver.click(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input/.."));
		logger.info("Yes,Subscribe me to pulse checkbox is unchecked");
		driver.click(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input/.."));
		logger.info("Yes,enroll me in CRP checkbox is unchecked");
		driver.pauseExecutionFor(2000);
	}


	//	public void acceptTheVerifyYourShippingAddressPop() throws InterruptedException{
	//		driver.waitForElementPresent(By.id("QAS_AcceptOriginal"));
	//		driver.click(By.id("QAS_AcceptOriginal"));
	//		logger.info("accept the original button clicked");
	//	}

	public boolean verifySuggesstionsForEnteredAddressPop(){
		try{
			driver.waitForElementPresent(By.id("QAS_AcceptOriginal"));
			driver.click(By.id("QAS_AcceptOriginal"));
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public void enterCardNumber(String cardNumber){
		driver.waitForElementPresent(By.id("card-nr"));
		driver.findElement(By.id("card-nr")).sendKeys(cardNumber+"\t");
		logger.info("card number entered as "+cardNumber);
	}

	public void enterNameOnCard(String nameOnCard){
		driver.findElement(By.id("card-name")).sendKeys(nameOnCard);
		logger.info("name on card entered is "+nameOnCard);
	}

	public boolean validateInvalidCreditCardMessage(){
		if(driver.findElement(By.xpath("//input[@id='card-nr']/following::label[1][contains(text(),'Please enter a valid')]")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean validateEmptyCreditCardMessage(){
		if(driver.findElement(By.xpath("//input[@id='card-nr']/following::label[1][contains(text(),'This field is required')]")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}

	public void clearCreditCardNumber(){
		driver.findElement(By.id("card-nr")).clear();
		logger.info("credit card number box cleared");
	}

	public void selectExpirationDate(String month,String year){
		Select monthDD = new Select(driver.findElement(By.id("expiryMonth")));
		Select yearDD = new Select(driver.findElement(By.id("expiryYear")));
		monthDD.selectByVisibleText(month.toUpperCase());
		yearDD.selectByVisibleText(year);		
	}

	public void enterSecurityCode(String securityCode){
		driver.findElement(By.id("security-code")).sendKeys(securityCode);
		logger.info("security code entered is "+securityCode);
	}

	public void enterSocialInsuranceNumber(String sin) throws InterruptedException{
		driver.findElement(By.id("S-S-N")).clear();
		driver.findElement(By.id("S-S-N")).sendKeys(sin+"\t");
		logger.info("Social Insurance Number is "+sin);
	}

	public void enterNameAsItAppearsOnCard(String nameOnCard){
		driver.findElement(By.id("name-on-card")).sendKeys(nameOnCard);
		logger.info("name on card entered is "+nameOnCard);
	}

	public void checkThePoliciesAndProceduresCheckBox() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='policies-check']/.."));
		driver.click(By.xpath("//input[@id='policies-check']/.."));
		logger.info("The Policies And Procedures CheckBox is checked");
	}

	public void checkTheIAcknowledgeCheckBox(){
		driver.click(By.xpath("//input[@id='acknowledge-check']/.."));
		logger.info("The I Acknowledge CheckBox is checked");
	}

	public String getErrorMessageForInvalidSSN(){
		driver.waitForElementPresent(By.xpath("//input[@id='S-S-N']/following::label[1]"));
		String errorMessage=driver.findElement(By.xpath("//input[@id='S-S-N']/following::label[1]")).getText();
		return errorMessage;
	}

	public String getInvalidPasswordMessage(){
		driver.waitForElementPresent(By.xpath("//input[@id='new-password-account']/following::label[1]"));
		String errorMessage=driver.findElement(By.xpath("//input[@id='new-password-account']/following::label[1]")).getText();
		return errorMessage;
	}

	public String getInvalidPasswordNotmatchingMessage(){
		driver.waitForElementPresent(By.xpath("//input[@id='new-password-account2']/following::label[1]"));
		String errorMessage=driver.findElement(By.xpath("//input[@id='new-password-account2']/following::label[1]")).getText();
		return errorMessage;
	}

	public boolean isTheTermsAndConditionsCheckBoxDisplayed(){
		driver.waitForElementPresent(By.xpath("//input[@id='terms-check']/.."));
		return driver.IsElementVisible(driver.findElement(By.xpath("//input[@id='terms-check']/..")));
	}

	public void checkTheTermsAndConditionsCheckBox(){
		driver.waitForElementPresent(By.xpath("//input[@id='terms-check']/.."));
		driver.click(By.xpath("//input[@id='terms-check']/.."));		
		logger.info("The Terms And Conditions CheckBox selected");
	}

	public void clickOnEnrollmentNextButton(){
		driver.waitForElementPresent(By.id("enrollment-next-button"));
		driver.click(By.id("enrollment-next-button")); 
		logger.info("enrollment next button clicked");
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

	public boolean verifyPopUpForTermsAndConditions() throws InterruptedException{
		boolean isPopForTermsAndConditionsVisible = false;		
		driver.waitForElementPresent(By.xpath("//div[@class='popup-standard']/h2[text()='Please accept our terms & conditions']"));
		isPopForTermsAndConditionsVisible = driver.IsElementVisible(driver.findElement(By.xpath("//div[@class='popup-standard']/h2[text()='Please accept our terms & conditions']")));
		if(isPopForTermsAndConditionsVisible==true){
			driver.click(By.xpath("//div[@class='popup-standard']/h2[text()='Please accept our terms & conditions']/following::a[@title='Close']"));
			driver.pauseExecutionFor(1000);
			return true;
		}
		return false;
	}

	public void checkTheIAgreeCheckBox(){
		driver.click(By.xpath("//input[@id='electronically-check']/.."));
		logger.info("I Agree checkbox clicked");
	}

	public void clickOnChargeMyCardAndEnrollMeBtn() throws InterruptedException{
		driver.waitForElementPresent(By.id("enroll-button"));
		driver.click(By.id("enroll-button"));
		logger.info("Charge my card button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnEnrollMeBtn() throws InterruptedException{
		clickOnChargeMyCardAndEnrollMeBtn();
	}

	public void clickOnConfirmAutomaticPayment() throws InterruptedException{
		driver.waitForElementPresent(By.id("enroll"));
		driver.click(By.id("enroll"));
		logger.info("Automatic payment confirmation button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyCongratsMessage(){
		driver.waitForElementPresent(By.id("Congrats"));
		return driver.IsElementVisible(driver.findElement(By.id("Congrats")));
	}


	public void clickOnUpdateCartShippingNextStepBtnDuringEnrollment() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.xpath("//input[@class='use_address btn btn-primary']"));
		//action.moveToElement(driver.findElement(By.xpath("//input[@id='use_address']"))).double.build().perform();  
		action.moveToElement(driver.findElement(By.xpath("//input[@class='use_address btn btn-primary']"))).click(driver.findElement(By.xpath("//input[@class='use_address btn btn-primary']"))).build().perform();
		logger.info("Next button on shipping update cart clicked"); 
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnGoToMyAccountToCheckStatusOfCRP(){
		driver.waitForElementPresent(By.xpath("//div[@id='order-confirm']/a"));
		driver.click(By.xpath("//div[@id='order-confirm']/a"));	
		logger.info("Go to my account to check status of CRP button clicked");
	}

	public void clickOnAccountInfoLinkLeftSidePannel(){
		driver.waitForElementPresent(By.xpath("//div[@id='left-menu']//a[text()='ACCOUNT INFO']"));
		driver.click(By.xpath("//div[@id='left-menu']//a[text()='ACCOUNT INFO']"));
		logger.info("account info link on side panel clicked");
		driver.pauseExecutionFor(1000);
	}

	public boolean validateErrorMessageWithoutSelectingAllCheckboxes(){
		driver.waitForElementPresent(By.xpath("//h2[contains(text(),'Please accept our terms & conditions')]"));
		return driver.findElement(By.xpath("//h2[contains(text(),'Please accept our terms & conditions')]")).isDisplayed();
	}

	public void acceptTheProvideAccessToSpousePopup(){
		try{
			driver.quickWaitForElementPresent(By.id("acceptSpouse"));
			driver.click(By.id("acceptSpouse"));
		}
		catch(Exception e){

		}
	}

	public void clickSwitchToExpressEnrollmentLink(){
		driver.waitForElementPresent(By.xpath("//a[@id='lnk-switch']"));
		driver.click(By.xpath("//a[@id='lnk-switch']"));
		driver.pauseExecutionFor(3000);
	}

	public void checkPulseCheckBox(){
		driver.waitForElementPresent(By.xpath("//input[@id='pulse-check']/.."));
		if(driver.findElement(By.xpath("//input[@id='pulse-check']")).getAttribute("checked").contains("true")){
			driver.click(By.xpath("//input[@id='pulse-check']/.."));
		}
		else{
			driver.pauseExecutionFor(500);
		}
	}

	public void checkCRPCheckBox(){
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.xpath("//input[@id='CRP-check']/.."));
		if(driver.findElement(By.xpath("//input[@id='CRP-check']")).getAttribute("checked").contains("true")){
			driver.click(By.xpath("//input[@id='CRP-check']/.."));
		}
		else{
			driver.pauseExecutionFor(500);
		}
	}

	public void selectProductAndProceedToAddToCRP() throws InterruptedException{
		driver.quickWaitForElementPresent(By.xpath("//div[@id='quick-refine']"));
		try{

			driver.click(By.xpath("//div[@id='quick-refine']/following::div[1]/div[2]/div[1]//input[@value='Add to crp']"));
			logger.info("Add to CRP button clicked");
		}catch(Exception e){
			try{

				driver.click(By.xpath("//div[@id='quick-refine']/following::div[3]/div[2]//input[@value='Add to crp']"));
				logger.info("Add to CRP button clicked");
			}catch(Exception e1){

				driver.click(By.xpath("//div[@id='quick-refine']/following::div[2]/div[2]/div[1]/div[2]/div[2]//button"));
				logger.info("Add to CRP button clicked");				
			}
		}

		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
		}catch(Exception e){

		}
		driver.waitForLoadingImageToDisappear();
	}

	public void clickNextOnCRPCartPage(){
		driver.waitForElementPresent(By.id("submitForm"));
		driver.click(By.id("submitForm"));
	}
	public void clickSwitchToExpressEnrollmentOnRecurringMonthlyChargesSection(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Switch to Express')]"));
		driver.findElement(By.xpath("//a[contains(text(),'Switch to Express')]")).click();
	}

	public void enterUserInformationForEnrollment(String kitName,String regimenName,String enrollmentType,String firstName,String lastName,String password,String addressLine1,String city,String postalCode,String phoneNumber){
		selectEnrollmentKitPage(kitName, regimenName);		
		chooseEnrollmentOption(enrollmentType);
		enterFirstName(firstName);
		enterLastName(lastName);
		enterEmailAddress(firstName+TestConstants.EMAIL_ADDRESS_SUFFIX);
		enterPassword(password);
		enterConfirmPassword(password);
		enterAddressLine1(addressLine1);
		enterCity(city);
		selectProvince();
		enterPostalCode(postalCode);
		enterPhoneNumber(phoneNumber);

	}

	public void enterUserInformationForEnrollment(String kitName,String regimenName,String enrollmentType,String firstName,String lastName,String emailaddress,String password,String addressLine1,String city,String province,String postalCode,String phoneNumber){
		selectEnrollmentKitPage(kitName, regimenName);  
		chooseEnrollmentOption(enrollmentType);
		enterFirstName(firstName);
		enterLastName(lastName);
		enterEmailAddress(emailaddress);
		enterPassword(password);
		enterConfirmPassword(password);
		enterAddressLine1(addressLine1);
		enterCity(city);
		selectProvince(province);
		enterPostalCode(postalCode);
		enterPhoneNumber(phoneNumber);
		enterEmailAddress(emailaddress);
	}

	public void enterUserInformationForEnrollmentWithEmail(String kitName,String regimenName,String enrollmentType,String firstName,String lastName,String emailaddress,String password,String addressLine1,String city,String postalCode,String phoneNumber){
		selectEnrollmentKitPage(kitName, regimenName);  
		chooseEnrollmentOption(enrollmentType);
		enterFirstName(firstName);
		enterLastName(lastName);
		enterEmailAddress(emailaddress);
		enterPassword(password);
		enterConfirmPassword(password);
		enterAddressLine1(addressLine1);
		enterCity(city);
		selectProvince();
		enterPostalCode(postalCode);
		enterPhoneNumber(phoneNumber);

	}

	//Method Overloaded without Kit and Regimen
	public void enterUserInformationForEnrollment(String firstName,String lastName,String password,String addressLine1,String city,String postalCode,String phoneNumber){
		enterFirstName(firstName);
		enterLastName(lastName);
		enterPassword(password);
		enterConfirmPassword(password);
		enterAddressLine1(addressLine1);
		enterCity(city);
		selectProvince();
		enterPostalCode(postalCode);
		enterPhoneNumber(phoneNumber);
		enterEmailAddress(firstName+TestConstants.EMAIL_ADDRESS_SUFFIX);
	}

	//method overloaded,no need for enrollment type if kit is portfolio
	public void enterUserInformationForEnrollment(String kitName,String regimenName,String firstName,String lastName,String password,String addressLine1,String city,String postalCode,String phoneNumber){
		selectEnrollmentKitPage(kitName, regimenName);		
		enterFirstName(firstName);
		enterLastName(lastName);
		enterPassword(password);
		enterConfirmPassword(password);
		enterAddressLine1(addressLine1);
		enterCity(city);
		selectProvince();
		enterPostalCode(postalCode);
		enterPhoneNumber(phoneNumber);
		enterEmailAddress(firstName+TestConstants.EMAIL_ADDRESS_SUFFIX);
	}

	public void enterUserInformationForEnrollment(String kitName,String firstName,String lastName,String password,String addressLine1,String city,String postalCode,String phoneNumber){
		selectEnrollmentKitPage(kitName);  
		enterFirstName(firstName);
		enterLastName(lastName);
		enterEmailAddress(firstName+TestConstants.EMAIL_ADDRESS_SUFFIX);
		enterPassword(password);
		enterConfirmPassword(password);
		enterAddressLine1(addressLine1);
		enterCity(city);
		selectProvince();
		enterPostalCode(postalCode);
		enterPhoneNumber(phoneNumber);

	}

	// method overloaded, parameter for province is there
	public void enterUserInformationForEnrollment(String kitName,String regimenName,String enrollmentType,String firstName,String lastName,String password,String addressLine1,String city,String province,String postalCode,String phoneNumber){
		selectEnrollmentKitPage(kitName, regimenName);		
		chooseEnrollmentOption(enrollmentType);
		enterFirstName(firstName);
		enterLastName(lastName);
		enterPassword(password);
		enterConfirmPassword(password);
		enterAddressLine1(addressLine1);
		enterCity(city);
		selectProvince(province);
		enterPostalCode(postalCode);
		enterPhoneNumber(phoneNumber);
		enterEmailAddress(firstName+TestConstants.EMAIL_ADDRESS_SUFFIX);
	}

	public void clickOnSwitchToStandardEnrollmentLink(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Switch to Standard Enrollment')]"));
		driver.click(By.xpath("//a[contains(text(),'Switch to Standard Enrollment')]"));
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
	}

	public void clickOnSwitchToExpressEnrollmentLink(){
		driver.waitForElementPresent(By.xpath("//a[@id='lnk-switch']"));
		driver.click(By.xpath("//a[@id='lnk-switch']"));
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
	}

	//	public boolean validateErrorMessageForActivePC(){
	//		String ActivePC="autopc@xyz.com";
	//		driver.findElement(By.xpath("//input[@id='email-account']")).sendKeys(ActivePC);
	//		driver.findElement(By.xpath("//input[@id='new-password-account']")).sendKeys(password);   
	//		boolean status=driver.findElement(By.xpath("//div[@class='tipsy-inner']")).isDisplayed();
	//		if(driver.findElement(By.xpath("//a[@class='fancybox-item fancybox-close']")).isDisplayed()){
	//			driver.click(By.xpath("//a[@class='fancybox-item fancybox-close']"));
	//		}
	//		driver.findElement(By.xpath("//input[@id='email-account']")).clear();
	//		return status;
	//	}

	//	public boolean validateErrorMessageForActiveConsultant(){
	//		String ActiveConsultant="con0708@yopmail.com";
	//		driver.findElement(By.xpath("//input[@id='email-account']")).sendKeys(ActiveConsultant);
	//		driver.findElement(By.xpath("//input[@id='new-password-account']")).sendKeys(password);   
	//		boolean status=driver.findElement(By.xpath("//div[@class='tipsy-inner']")).isDisplayed();
	//		if(driver.findElement(By.xpath("//a[@class='fancybox-item fancybox-close']")).isDisplayed()){
	//			driver.click(By.xpath("//a[@class='fancybox-item fancybox-close']"));
	//		}
	//		driver.findElement(By.xpath("//input[@id='email-account']")).clear();
	//		return status;
	//	}

	//	public boolean validateErrorMessageForActiveRC(){
	//		String ActiveRC="Retail29@mailinator.com";
	//		driver.findElement(By.xpath("//input[@id='email-account']")).sendKeys(ActiveRC);
	//		driver.findElement(By.xpath("//input[@id='new-password-account']")).sendKeys(password);   
	//		boolean status=driver.findElement(By.xpath("//div[@class='tipsy-inner']")).isDisplayed();
	//		if(driver.findElement(By.xpath("//a[@class='fancybox-item fancybox-close']")).isDisplayed()){
	//			driver.click(By.xpath("//a[@class='fancybox-item fancybox-close']"));
	//		}
	//		driver.findElement(By.xpath("//input[@id='email-account']")).clear();
	//		return status;
	//	}

	public boolean verifyPopUpForExistingActivePC() throws InterruptedException{
		boolean isPopForExistingAccountVisible = false;
		//Thread.sleep(5000);
		isPopForExistingAccountVisible = driver.findElement(By.xpath("//div[@id='activePCPopup']/div/div")).isDisplayed();
		if(isPopForExistingAccountVisible==true){
			driver.findElement(By.xpath("//a[contains(@class, 'fancybox-close')]")).click();
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyPopUpForExistingActiveCCLessThan6Month() throws InterruptedException{
		boolean isPopForExistingAccountVisible = false;
		driver.waitForElementPresent(CONSULTANT_VALIDATION_POPUP_LESS_THAN_6_MONTH);
		isPopForExistingAccountVisible = driver.findElement(CONSULTANT_VALIDATION_POPUP_LESS_THAN_6_MONTH).isDisplayed();
		if(isPopForExistingAccountVisible==true){
			driver.findElement(By.xpath("//a[contains(@class, 'fancybox-close')]")).click();
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyPopUpForExistingActiveRC() throws InterruptedException{
		boolean isPopForExistingAccountVisible = false;
		isPopForExistingAccountVisible = driver.findElement(By.xpath("//div[@id='activeRetailPopup']/div/div")).isDisplayed();
		if(isPopForExistingAccountVisible==true){
			driver.findElement(By.xpath("//a[contains(@class, 'fancybox-close')]")).click();
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyPopUpForExistingInactivePC90Days() throws InterruptedException{
		boolean isPopForExistingAccountVisible = false;
		isPopForExistingAccountVisible = driver.findElement(By.xpath("//div[@id='inactivePc90Popup']/div/div")).isDisplayed();
		if(isPopForExistingAccountVisible==true){
			driver.findElement(By.xpath("//a[contains(@class, 'fancybox-close')]")).click();
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyPopUpForExistingInactiveCC180Days() throws InterruptedException{
		boolean isPopForExistingAccountVisible = false;
		isPopForExistingAccountVisible = driver.findElement(By.xpath("//div[@id='inactiveConsultant180Popup']/div/div")).isDisplayed();
		if(isPopForExistingAccountVisible==true){
			driver.findElement(By.xpath("//a[contains(@class, 'fancybox-close')]")).click();

			return true;
		}else{
			return false;
		}
	}

	public boolean validateIncorrectLogin(){
		return driver.findElement(By.xpath("//p[text()='Your username or password was incorrect.']")).isDisplayed();
	}

	public boolean verifySwitchPCToUnderDifferentConsultant(){
		boolean flag = false;
		if(driver.findElement(By.id("inactivePc90Form")).isDisplayed()){
			flag = true;
			return flag;
		}else{
			return flag;
		}
	}

	public void mouseHoverSponsorDataAndClickContinueForPC() throws InterruptedException{
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@id='the-search-results']/div[1]/div[@class='result-inner shadow']"))).click(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).build().perform();
		logger.info("First result of sponsor has been clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
	}

	public void enterSponsorIdDuringCreationOfPC(String sponsorID){
		driver.waitForElementPresent(By.id("sponsor-name-id"));
		driver.findElement(By.id("sponsor-name-id")).sendKeys(sponsorID);
		driver.findElement(By.xpath("//input[contains(@class,'submitSponser')]")).click();
	}

	public boolean validateMiniCart() {
		actions=new Actions(RFWebsiteDriver.driver);
		return driver.findElement(By.xpath("//a[@id='shopping-cart']")).isDisplayed();
	}

	public boolean clickMiniCartAndValidatePreaddedProductsOnCartPage(){
		driver.waitForElementPresent(By.xpath("//a[@id='shopping-cart']"));
		driver.click(By.xpath("//a[@id='shopping-cart']"));
		driver.waitForPageLoad();
		return driver.findElement(By.xpath("//div[@id='left-shopping']")).isDisplayed();
	}

	public void clickOnReviewAndConfirmShippingEditBtn(){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Shipping info')]/a[text()='Edit']"));
		driver.click(By.xpath("//h3[contains(text(),'Shipping info')]/a[text()='Edit']"));
	}

	public boolean isReviewAndConfirmPageContainsShippingAddress(String shippingAddress){
		driver.waitForElementPresent(By.xpath("//div[@id='summarySection']//div[contains(@class,'third-list-module')][1]//span[contains(@class,'font-bold')]/.."));
		return driver.findElement(By.xpath("//div[@id='summarySection']//div[contains(@class,'third-list-module')][1]//span[contains(@class,'font-bold')]/..")).getText().contains(shippingAddress);
	}

	public boolean isReviewAndConfirmPageContainsFirstAndLastName(String name){
		driver.waitForElementPresent(By.xpath("//div[@id='summarySection']//div[contains(@class,'third-list-module')][1]//span[contains(@class,'font-bold')]"));
		return driver.findElement(By.xpath("//div[@id='summarySection']//div[contains(@class,'third-list-module')][1]//span[contains(@class,'font-bold')]")).getText().toLowerCase().contains(name.toLowerCase());
	}

	public boolean validateUserLandsOnPWSbizSite(){
		return driver.getCurrentUrl().contains("biz");
	}

	public void cancelPulseSubscription(){
		driver.waitForElementPresent(By.xpath("//a[text()='Cancel my Pulse subscription »']"));
		driver.click(By.xpath("//a[text()='Cancel my Pulse subscription »']"));
		driver.pauseExecutionFor(2000);
		driver.click(By.xpath("//a[@id='cancelPulse']"));
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.id("cancel-pulse-button"));
			driver.click(By.id("cancel-pulse-button"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}
		driver.waitForPageLoad();
	}

	public boolean validatePWS(){
		driver.waitForElementPresent(By.xpath("//div[@id='header-logo']"));
		return driver.findElement(By.xpath("//div[@id='header-logo']")).isDisplayed();
	}

	public void clickOnCountryAtWelcomePage(){
		String country = driver.getCountry();
		if(country.equalsIgnoreCase("ca")){
			driver.waitForElementPresent(By.xpath("//a[contains(text(),'Can')]"));
			driver.click(By.xpath("//a[contains(text(),'Can')]"));

		}else{
			driver.waitForElementPresent(By.xpath("//a[contains(text(),'Uni')]"));
			driver.click(By.xpath("//a[contains(text(),'Uni')]"));
		}
	}

	public String navigateToCommercialWebsite(String bizURL){
		return bizURL.replaceFirst("biz", "com");
	}

	//	public void enterSponsorIdDuringCreationOfPC(String sponsorID){
	//		driver.waitForElementPresent(By.id("sponsor-name-id"));
	//		driver.findElement(By.id("sponsor-name-id")).sendKeys(sponsorID);
	//		driver.findElement(By.xpath("//input[@class='submitSponser']")).click();
	//	}
	//
	//	public void mouseHoverSponsorDataAndClickContinueForPC() throws InterruptedException{
	//		actions =  new Actions(RFWebsiteDriver.driver);
	//		actions.moveToElement(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).click().build().perform();
	//		logger.info("First result of sponsor has been clicked");
	//		driver.waitForLoadingImageToDisappear();
	//		driver.waitForPageLoad();
	//	}

	public boolean validateShippingMethodDisclaimersForUPSGroundHD(){
		return driver.findElement(By.xpath("//select[@id='selectDeliveryModeForCRP']/option[3]")).getText().contains("Overnight");
	}

	public boolean validatePCPerksCheckBoxIsDisplayed(){
		if(driver.findElement(By.xpath("//div[@id='pc-customer2-div-order-summary']")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}

	public void clickOnNotYourSponsorLink(){
		driver.waitForElementPresent(By.xpath("//a[@id='not-your-sponsor']"));
		driver.click(By.xpath("//a[@id='not-your-sponsor']"));
	}

	public void clickNotYourSponsorLinkOnKitPage(){
		driver.waitForElementPresent(By.xpath("//a[text()='Not your sponsor?']"));
		driver.click(By.xpath("//a[text()='Not your sponsor?']"));
		driver.waitForLoadingImageToDisappear();

	}

	public boolean validateInvalidSponsor(){
		return driver.isElementPresent(By.xpath("//span[contains(text(),'No result found')]"));
	}

	public void clickSearchAgain(){
		driver.waitForLoadingImageToDisappear();
		driver.waitForElementPresent(By.xpath("//a[@id='sponsor_search_again']"));
		driver.click(By.xpath("//a[@id='sponsor_search_again']"));
		driver.findElement(By.id("sponsor-name-id")).clear();
	}

	public void checkPCPerksCheckBox(){
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.xpath("//input[@id='pc-customer2']/.."));
		driver.findElement(By.xpath("//input[@id='pc-customer2']/..")).click(); 
		driver.pauseExecutionFor(2000);
	}

	public void searchCIDForPCAndRC() throws InterruptedException{
		driver.waitForElementPresent(By.id("sponsor-name-id"));
		driver.findElement(By.id("sponsor-name-id")).sendKeys("test");
		driver.waitForElementPresent(By.xpath("//input[@value='Search']"));
		driver.click(By.xpath("//input[@value='Search']"));
		logger.info("Sponsor entered as 'test' and search button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void mouseHoverSponsorDataAndClickContinueForPCAndRC() throws InterruptedException{
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@class='result-inner shadow']"))).click(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).build().perform();
		logger.info("First result of sponsor has been clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	//	public String getUserNameAForVerifyLogin(String profileName){
	//		driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+profileName+"')]"));
	//		String userName = driver.findElement(By.xpath("//span[contains(text(),'"+profileName+"')]")).getText();
	//		return userName;
	//	}

	public boolean validateExpiredDateMessage(){
		return driver.isElementPresent(By.xpath("//div[@class='tipsy-inner'][contains(text(),'Must be a valid Expiration Date')]"));
	}

	public void clickEnrollmentNextButtonWithoutPopupHandled() throws InterruptedException{
		driver.waitForElementPresent(By.id("enrollment-next-button"));
		driver.pauseExecutionFor(2000);
		driver.click(By.id("enrollment-next-button"));
		logger.info("EnrollmentTest Next Button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public boolean validateSetUpAccountPageIsDisplayed(){
		return driver.findElement(By.xpath("//h1[contains(text(),'SetUp Account')]")).getText().contains("Account");
		//return driver.getTitle().contains("account");
	}

	public boolean validateUpdatedMainAccountInfo(){
		return driver.findElement(By.xpath(".//div[@id='summarySection']/div[4]/div[3]/p/br[1]")).getText().contains(addressLine1)
				&&  driver.findElement(By.xpath(".//div[@id='summarySection']/div[4]/div[3]/p/br[2]")).getText().contains(city)
				&& driver.findElement(By.xpath(".//div[@id='summarySection']/div[4]/div[3]/p/br[2]")).getText().contains(postalCode);
	}

	public void reEnterContactInfoAndPassword(){
		String country = driver.getCountry();
		if(country.equalsIgnoreCase("ca")){
			driver.findElement(By.xpath("//input[@id='address-1']")).clear();
			driver.findElement(By.xpath("//input[@id='address-1']")).sendKeys(TestConstants.RE_ENTER_ADDRESS_LINE_1);
			driver.findElement(By.xpath("//input[@id='city']")).clear();
			driver.findElement(By.xpath("//input[@id='city']")).sendKeys(TestConstants.RE_ENTER_CITY);
			driver.findElement(By.xpath("//input[@id='postcode']")).clear();
			driver.findElement(By.xpath("//input[@id='postcode']")).sendKeys(TestConstants.RE_ENTER_POSTALCODE);
		}else{
			driver.findElement(By.xpath("//input[@id='address-1']")).clear();
			driver.findElement(By.xpath("//input[@id='address-1']")).sendKeys(TestConstants.NEW_ADDRESS_LINE_1_US);
			driver.findElement(By.xpath("//input[@id='city']")).clear();
			driver.findElement(By.xpath("//input[@id='city']")).sendKeys(TestConstants.CITY_US);
			driver.findElement(By.xpath("//input[@id='postcode']")).clear();
			driver.findElement(By.xpath("//input[@id='postcode']")).sendKeys(TestConstants.POSTAL_CODE_US);
		}
		addressLine1=driver.findElement(By.xpath("//input[@id='address-1']")).getText();
		city=driver.findElement(By.xpath("//input[@id='city']")).getText();
		postalCode=driver.findElement(By.xpath("//input[@id='postcode']")).getText();
		driver.findElement(By.xpath("//input[@id='new-password-account']")).sendKeys(driver.getPassword());
		driver.findElement(By.xpath("//input[@id='new-password-account2']")).sendKeys(driver.getPassword());
		clickNextButton();
		driver.waitForLoadingImageToDisappear();
	}

	public boolean validateUpdateCartPageIsDisplayed(){
		driver.pauseExecutionFor(1000);
		return driver.getCurrentUrl().contains("autoship");
	}

	public void selectAProductAndAddItToCRP(){
		driver.waitForElementPresent(By.xpath("//div[contains(@class,'blue')]/div[2]/div[1]//input[@value='Add to crp']"));
		driver.click(By.xpath("//div[contains(@class,'blue')]/div[2]/div[1]//input[@value='Add to crp']"));;
		driver.waitForSpinImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
		}catch(Exception e){

		}
		driver.pauseExecutionFor(1000);
		driver.waitForPageLoad();
	}

	public void selectAnotherProductAndAddItToCRP(){

		if(driver.getCountry().equalsIgnoreCase("ca")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[7]/div[1]//form[@id='productDetailForm']/input[2]"));
			driver.click(By.xpath("//div[@id='main-content']/div[7]/div[1]//form[@id='productDetailForm']/input[2]"));
		}else{
			driver.waitForElementPresent(By.xpath("//div[contains(@class,'blue')]/div[2]/div[2]//input[@value='Add to crp']"));
			driver.click(By.xpath("//div[contains(@class,'blue')]/div[2]/div[2]//input[@value='Add to crp']"));
		}
		driver.waitForSpinImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
		}catch(Exception e){

		}
		driver.pauseExecutionFor(1000);
		driver.waitForPageLoad();
	}

	public String getAutoshipTemplateUpdatedMsg(){
		driver.quickWaitForElementPresent(By.xpath(".//div[@id='globalMessages']//p"));
		return driver.findElement(By.xpath(".//div[@id='globalMessages']//p")).getText();
	}

	public boolean validateRemoveProductsTillErrorMsgIsDisplayed(){
		boolean flag=false;
		while(true){
			try{
				driver.waitForElementPresent(By.xpath("//a[text()='Remove']"));
				driver.click(By.xpath("//a[text()='Remove']"));;
				driver.waitForPageLoad();
				if(driver.findElement(By.xpath(".//div[@id='globalMessages']//p")).getText().equalsIgnoreCase(TestConstants.AUTOSHIP_TEMPLATE_ERROR_MSG_CONSULTANT)){
					flag=true;
					break;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(flag==true){
			return true;
		}else
			return false;

	}

	public boolean validateErrorMsgAfterRemovingProductsFromPcCart(){
		boolean flag=false;
		while(true){
			try{
				driver.waitForElementNotPresent(By.xpath("//a[text()='Remove']"));
				driver.click(By.xpath("//a[text()='Remove']"));				
				if(driver.findElement(By.xpath(".//div[@id='globalMessages']//p")).getText().equalsIgnoreCase(TestConstants.AUTOSHIP_TEMPLATE_ERROR_MSG_PC)){
					flag=true;
					break;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(flag==true){
			return true;
		}else
			return false;

	}

	public boolean validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(){
		driver.waitForElementNotPresent(By.xpath(".//div[@id='globalMessages']//p"));
		return driver.findElement(By.xpath(".//div[@id='globalMessages']//p")).getText().contains(TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG_AFTER_UPDATING_PRODUCT_QTY);
	}

	public void clickOnUserName(){
		driver.waitForElementPresent(By.xpath("//div[@id='header-middle-top']//a"));
		driver.click(By.xpath("//div[@id='header-middle-top']//a"));
	}

	public boolean verifyJoinMyTeamLinkPresent(){
		driver.waitForPageLoad();
		if(driver.isElementPresent(By.xpath("//a[@class='joinMe']"))){
			return true;
		}else
			return false;
	}

	public void clickOnJoinMyTeamBtn(){
		driver.click(By.xpath("//a[@class='joinMe']"));
		driver.pauseExecutionFor(1000);
		driver.waitForPageLoad();
	}

	public void openPWS(String pws){
		driver.get(pws);
		driver.waitForPageLoad();
	}

	public void clickOnPersonalizeMyProfileLink(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Personalize my Profile')]"));
		driver.click(By.xpath("//a[contains(text(),'Personalize my Profile')]"));
	}

	public String convertBizToComPWS(String pws){
		String com  = "com";
		String biz ="biz";
		if(pws.contains(biz)){
			String comPws = pws.replaceAll("biz",com);
			return comPws;
		}else{
			String bizPws = pws.replaceAll("com",biz);
			return bizPws;
		}
	}

	public String convertBizSiteToComSite(String pws){
		String com  = "com";
		String biz ="biz";
		if(pws.contains(biz))
			pws = pws.replaceAll(biz,com);
		return pws;
	}

	public String convertComSiteToBizSite(String pws){
		String com  = "com";
		String biz ="biz";
		if(pws.contains(com))
			pws = pws.replaceAll(com,biz);
		return pws;		
	}


	public boolean verifyContinueWithoutSponserLinkPresent(){
		return driver.isElementPresent(By.id("continue-no-sponsor"));
	}

	public boolean verifyRequestASponsorBtn(){
		return driver.isElementPresent(By.xpath("//input[@value='Request a sponsor']"));
	}

	public void searchCIDForSponserHavingPulseSubscribed(String sponserName){
		driver.waitForElementPresent(By.id("sponsor-name-id"));
		driver.findElement(By.id("sponsor-name-id")).sendKeys(sponserName);
		driver.waitForElementPresent(By.xpath("//input[@value='Search']"));
		driver.click(By.xpath("//input[@value='Search']"));
		logger.info("Sponsor entered as 'test' and search button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyPWSAfterSuccessfulEnrollment(String actualPWS,String newPWS){
		if(actualPWS.equalsIgnoreCase(newPWS)){
			return true;
		}
		else
			return false;
	}

	public boolean verifyPCUserIsOnSponserPWSAfterSuccessfulEnrollment(String sponserPWS,String newPWS){
		if(newPWS.contains(sponserPWS)){
			return true;
		}
		else
			return false;
	}

	public boolean verifyPCUserIsOnCorpSiteAfterSuccessfulEnrollment(String corpUrl,String newPWS){
		if(newPWS.contains(corpUrl)){
			return true;
		}
		else
			return false;
	}

	public String getSponsorNameFromSelectAndContinueSection(){
		driver.waitForElementPresent(By.xpath("//div[contains(@class,'the-sponsor-results')]/div[1]//li[@class='font-bold']"));
		return driver.findElement(By.xpath("//div[contains(@class,'the-sponsor-results')]/div[1]//li[@class='font-bold']")).getText();
	}

	public String getSponsorNameFromHeaderKitPage(){
		driver.waitForElementPresent(By.xpath("//div[@id='header']//div[contains(@class,'consultant-info')]//a"));
		return driver.findElement(By.xpath("//div[@id='header']//div[contains(@class,'consultant-info')]//a")).getText();	
	}

	public boolean isSponsorPresentInSearchList(){
		driver.waitForElementPresent(By.xpath("//div[@class='sponsorDataDiv']"));
		return driver.isElementPresent(By.xpath("//div[@class='sponsorDataDiv']"));
	}

	public void updateQuantityOfProductToTheSecondProduct(String qty) throws InterruptedException{
		driver.waitForElementPresent(By.id("quantity1"));
		driver.findElement(By.id("quantity1")).clear();
		driver.findElement(By.id("quantity1")).sendKeys(qty);
		logger.info("quantity added is "+qty);
		driver.click(By.xpath("//a[@class='updateLink']"));
		driver.pauseExecutionFor(5500);
		logger.info("Update button clicked after adding quantity");
		driver.waitForPageLoad();
	}

	public void clickOnCheckoutButton(){
		driver.waitForElementPresent(By.xpath("//input[@value='PLACE ORDER']"));
		driver.click(By.xpath("//input[@value='PLACE ORDER']"));
		logger.info("checkout button clicked");
		if(driver.isElementPresent(By.xpath("//input[@onclick='checkOutNow();']"))){
			driver.click(By.xpath("//input[@onclick='checkOutNow();']"));
		}
		driver.waitForPageLoad();
	}

	public void clickEditShoppingCartLink(){
		driver.waitForElementPresent(By.xpath("//a[@class='gray-anchor']"));
		driver.click(By.xpath("//a[@class='gray-anchor']"));
		logger.info("edit shopping cart link clicked");
		driver.waitForPageLoad();
	}

	public double getSubTotalOfFirstProduct(){
		driver.waitForElementPresent(By.xpath("//input[@id='quantity0']"));
		String qty=driver.findElement(By.xpath("//input[@id='quantity0']")).getAttribute("value");
		Integer.parseInt(qty);
		String total=driver.findElement(By.xpath("//div[@class='cartItems']/div[1]//span[@class='special-price']")).getText();
		String[] totalvalue= total.split("\\$+");
		Double.parseDouble(totalvalue[1].trim());
		//Integer.parseInt(totalvalue[1].trim());
		return Integer.parseInt(qty)*Double.parseDouble(totalvalue[1].trim());
	}

	public double getSubTotalOfSecondProduct(){
		driver.waitForElementPresent(By.xpath("//input[@id='quantity1']"));
		String qty=driver.findElement(By.xpath("//input[@id='quantity1']")).getAttribute("value");
		Integer.parseInt(qty);
		String total=driver.findElement(By.xpath("//div[@class='cartItems']/div[2]//span[@class='special-price']")).getText();
		String[] totalvalue= total.split("\\$+");
		Double.parseDouble(totalvalue[1].trim());
		//Integer.parseInt(totalvalue[1].trim());
		return Integer.parseInt(qty)*Double.parseDouble(totalvalue[1].trim());
	}

	public boolean validateSubTotal(double subtotal1,double subtotal2){
		driver.waitForElementPresent(By.xpath("//div[@class='col-xs-12']//div[3]/span"));
		String subTotalVal=driver.findElement(By.xpath("//div[@class='col-xs-12']//div[3]/span")).getText();
		String[] totalvalue= subTotalVal.split("\\$+");
		double subTotalValue=Double.parseDouble(totalvalue[1].trim());
		if(subTotalValue==(subtotal1+subtotal2)){
			return true;
		}else{
			return false;
		}
	}

	public void removefirstProductFromTheCart(){
		driver.waitForElementPresent(By.xpath("//a[@href='javascript:submitRemove(1);']"));
		driver.click(By.xpath("//a[@href='javascript:submitRemove(1);']"));
		driver.pauseExecutionFor(1500);
		driver.waitForPageLoad();
	}

	public String getProductRemovedAutoshipTemplateUpdatedMsg(){
		driver.quickWaitForElementPresent(By.xpath(".//div[@id='globalMessages']//p"));
		return driver.findElement(By.xpath(".//div[@id='globalMessages']//p")).getText();
	}

	public String getThresholdMessageIsDisplayed(){
		driver.pauseExecutionFor(3000);
		driver.quickWaitForElementPresent(By.xpath(".//div[@id='globalMessages']//p"));
		return driver.findElement(By.xpath(".//div[@id='globalMessages']//p")).getText();
	}

	public void clickOnBelowFieldsSponsorLink(){
		driver.waitForPageLoad();
		driver.waitForElementPresent(FIELD_SPONSOR_LINK_LOC);
		driver.click(FIELD_SPONSOR_LINK_LOC);
		driver.waitForPageLoad();
		logger.info("field sponsor link has been clicked");
	}

	public void enterDetailsInRequestASponsorForm(String firstName,String lastName,String emailId,String postalCode){
		driver.waitForElementPresent(By.id("firstName"));
		driver.type(By.id("firstName"),firstName );
		driver.type(By.id("lastName"),lastName);
		driver.type(By.id("email"),emailId);
		driver.findElement(By.id("zipcode")).sendKeys(postalCode+"\t");
		driver.waitForLoadingImageToDisappear();
		driver.click(By.xpath("//input[@value='Submit']"));
		logger.info("form submitted");
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyConfirmationMessageAfterSubmission(){

		if(driver.findElement(CONFIRMATION_MESSAGE_LOC).isDisplayed()){

			String confirmationMessage = driver.findElement(CONFIRMATION_MESSAGE_LOC).getText();
			logger.info("message=="+confirmationMessage);
			if(confirmationMessage.equalsIgnoreCase("YOUR REQUEST HAS BEEN SUBMITTED")){
				return true;
			}
			else{
				return false;
			}

		}
		return false;
	}

	public void logout(){
		driver.findElement(By.id("account-info-button")).click();
		driver.waitForElementPresent(By.linkText("Log out"));
		driver.findElement(By.linkText("Log out")).click();
		logger.info("Logout");
	}	

	public void removeFirstProductFromTheCart(){
		driver.quickWaitForElementPresent(By.xpath("//a[@href='javascript:submitRemove(0);']"));
		driver.click(By.xpath("//a[@href='javascript:submitRemove(0);']"));
		driver.pauseExecutionFor(1500);
		driver.waitForPageLoad();
		driver.pauseExecutionFor(3000);
	}

	public void removeSecondProductFromTheCart(){
		driver.quickWaitForElementPresent(By.xpath("//a[@href='javascript:submitRemove(1);']"));
		driver.click(By.xpath("//a[@href='javascript:submitRemove(1);']"));
		driver.pauseExecutionFor(1500);
		driver.waitForPageLoad();
	}

	public boolean validateEmptyShoppingCartPageIsDisplayed(){
		driver.waitForElementNotPresent(By.xpath("//div[@id='left-shopping']//span"));
		return driver.findElement(By.xpath("//div[@id='left-shopping']//span")).getText().contains(TestConstants.AUTOSHIP_TEMPLATE_EMPTY_SHOPPING_CART_MSG);
	}

	public void clickOnContinueShoppingLinkOnEmptyShoppingCartPage(){
		driver.waitForElementNotPresent(By.xpath(".//div[@id='left-shopping']/a[contains(text(),'Continue shopping')]"));
		driver.click(By.xpath(".//div[@id='left-shopping']/a[contains(text(),'Continue shopping')]"));
		driver.waitForPageLoad();
	}

	public void addSecondProduct(){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
		driver.click(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
		logger.info("Continue shopping link clicked");
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[2]//form[@id='productDetailForm']/button"));
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[2]//form[@id='productDetailForm']/button"));
			logger.info("Buy Now button clicked and another product selected");
			driver.waitForPageLoad();
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.click(By.xpath("//div[@class='quickshop-section blue']/div[contains(@class,'quick-product')]/div[contains(@class,'product-third-module')][2]//form[@action='/us/cart/add']/button"));
			logger.info("Buy Now button clicked and another product selected");
			driver.waitForPageLoad();
		}
	}

	public String createBizToCom(String bizUrl){
		if(bizUrl.contains("biz")){
			String pws = bizUrl.replaceAll("biz","com");
			return pws;
		}else
			return bizUrl;
	}

	public boolean verifyInvalidSponsorPopupIsPresent(){
		driver.waitForElementPresent(By.id("inactiveConsultant180Form"));
		if(driver.isElementPresent(By.id("inactiveConsultant180Form"))){
			return true;
		}else
			return false;
	}

	public void clickOnCnacelEnrollment(){
		driver.waitForElementPresent(By.xpath("//form[@id='inactiveConsultant180Form']/input[@value='Cancel Enrollment']"));
		//  driver.click(By.xpath("//form[@id='inactiveConsultant180Form']/input[@class='cancelEnrollment']"));
		driver.click(By.xpath("//form[@id='inactiveConsultant180Form']/input[@value='Cancel Enrollment']"));
	}

	public void enterPasswordAfterTermination(){
		driver.waitForElementPresent(By.xpath("//input[@class='password-field']"));
		driver.type(By.xpath("//input[@class='password-field']"), driver.getPassword());
	}

	public boolean verifyReactiveYourPCAccountPopup(){
		driver.waitForElementPresent(By.xpath("//h2[contains(text(),'REACTIVATE YOUR PC ACCOUNT')]"));
		if(driver.isElementPresent(By.xpath("//h2[contains(text(),'REACTIVATE YOUR PC ACCOUNT')]"))){
			return true;
		}else
			return false;
	}

	public void clickOnLoginToReactiveMyAccount(){
		driver.pauseExecutionFor(2000);
		//  driver.waitForElementPresent(By.xpath("//input[@class='reactivatePC']"));
		//  driver.click(By.xpath("//input[@class='reactivatePC']/.."));
		driver.waitForElementPresent(By.xpath("//input[@value='Log In to Reactivate My Account']"));
		driver.click(By.xpath("//input[@value='Log In to Reactivate My Account']"));
		//  driver.waitForPageLoad();

	}

	public void clickOnCnacelEnrollmentForPC(){
		driver.waitForElementPresent(By.xpath("//div[@id='terminate-log-in']/following::input[1]"));
		driver.click(By.xpath("//div[@id='terminate-log-in']/following::input[1]"));
		driver.pauseExecutionFor(2000);
	}

	public String fetchingUserName(){
		return driver.findElement(By.xpath("//div[@id='account-info-button']//span[2]")).getText();
	}

	public boolean verifyUserNameAfterLoginAgain(String oldUserNameOnUI,String newUserNameOnUI){
		if(oldUserNameOnUI.equals(newUserNameOnUI)){
			return true;
		}      
		return  false;
	}

	public void uncheckCRPCheckBox(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, enroll me in CRP']/preceding::div[1]/input"));
		if(driver.isElementPresent(By.xpath("//input[@checked='checked']"))){
			driver.click(By.xpath("//input[@id='CRP-check']/.."));
		}
	}

	public boolean validateTermsAndConditionsForConsultantApplicationPulse(){
		driver.waitForElementPresent(By.xpath("//strong[contains(text(),'I have read and accepted all Terms and Conditions for the Consultant Application, Pulse')]"));
		return driver.isElementPresent(By.xpath("//strong[contains(text(),'I have read and accepted all Terms and Conditions for the Consultant Application, Pulse')]"));
	}

	public void uncheckPulseCheckBox(){
		driver.waitForElementPresent(By.xpath("//li[text()='Yes, subscribe me to Pulse']/preceding::div[1]/input/.."));
		if(driver.isElementPresent(By.xpath("//input[@checked='checked']"))){
			driver.click(By.xpath("//input[@id='pulse-check']/.."));
		}
	}

	public boolean validateTermsAndConditionsForConsultantApplicationCRP(){
		driver.waitForElementPresent(By.xpath("//strong[contains(text(),'I have read and accepted all Terms and Conditions for the Consultant Application, CRP')]"));
		return driver.isElementPresent(By.xpath("//strong[contains(text(),'I have read and accepted all Terms and Conditions for the Consultant Application, CRP')]"));
	}

	public void clickOnEnrollUnderLastUpline(){
		try{
			driver.waitForElementPresent(By.id("enrollUnderLastUpline"));
			driver.pauseExecutionFor(1000);
			driver.click(By.id("enrollUnderLastUpline"));
		}catch(Exception e){
			try{
				driver.waitForElementPresent(By.xpath("//form[@id='inactiveConsultant180Form']//input[@id='enrollUnderLastUpline']"));
				driver.pauseExecutionFor(1000);
				driver.click(By.xpath("//form[@id='inactiveConsultant180Form']//input[@id='enrollUnderLastUpline']"));
			}catch(Exception e2){
				driver.waitForElementPresent(By.xpath("//form[@id='inactivePc90Form']//input[@id='enrollUnderLastUpline']"));
				driver.pauseExecutionFor(1000);
				driver.click(By.xpath("//form[@id='inactivePc90Form']//input[@id='enrollUnderLastUpline']"));
			}
		}
	}

	public void enterPasswordForReactivationForConsultant(){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Reactivate My Account')]/following::input[2]"));
		driver.type(By.xpath("//h3[contains(text(),'Reactivate My Account')]/following::input[2]"), driver.getPassword());
	}

	public void clickOnLoginToReactiveMyAccountForConsultant(){
		//driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Reactivate My Account')]/following::a[2]/input"));
		driver.click(By.xpath("//h3[contains(text(),'Reactivate My Account')]/following::a[2]/input"));
		driver.waitForPageLoad();
	}

	public boolean verifyTerminatedConsultantIsNotInSponsorList(){
		driver.waitForPageLoad();
		if(driver.isElementPresent(By.xpath("//span[contains(text(),'No result found')]"))){
			return true;
		}else
			return false;
	}

	public boolean verifyTerminatedConsultantPresentInSponsorList(){
		driver.waitForPageLoad();
		if(driver.isElementPresent(By.xpath("//div[@class='sponsorDataDiv']"))){
			return true;
		}else
			return false;
	}

	public boolean isFirstNamePrepopulated(){
		driver.waitForElementPresent(By.id("first-name"));
		return driver.findElement(By.id("first-name")).getAttribute("value").length()>0;

	}

	public boolean isLastNamePrepopulated(){
		return driver.findElement(By.id("last-name")).getAttribute("value").length()>0;
	}

	public boolean isEmailAddressPrepopulated(){  
		return driver.findElement(By.id("email-account")).getAttribute("value").length()>0;

	}

	public boolean isAddressLine1Prepopulated(){
		return driver.findElement(By.id("address-1")).getAttribute("value").length()>0;
	}

	public boolean isCityPrepopulated(){
		return driver.findElement(By.id("city")).getAttribute("value").length()>0;
	}

	public boolean isSelectProvincePrepopulated(){
		return driver.findElement(By.xpath("//select[@id='state']/option[@selected='selected'][2]")).getText().length()>0;
	}

	public boolean isEnterPostalCodePrepopulated(){
		return driver.findElement(By.id("postcode")).getAttribute("value").length()>0;
	}

	public boolean isPhoneNumberPrepopulated(){
		return driver.findElement(By.id("phonenumber")).getAttribute("value").length()>0;
	}

	public void clickOnCrossIconForAddressPopup(){
		driver.waitForElementPresent(By.xpath("span[@class='icon-close']"));
		driver.findElement(By.xpath("//span[@class='icon-close']/..")).click();
	}

	public void clearAddressLine1(){
		driver.waitForElementPresent(By.id("address-1"));
		driver.findElement(By.id("address-1")).clear();
	}

	public boolean verifyEnterValueForMandatoryFieldPopup(){
		driver.waitForElementPresent(By.xpath("//input[@id='address-1']/following::label[1][contains(text(),'This field is required')]"));
		return driver.findElement(By.xpath("//input[@id='address-1']/following::label[1][contains(text(),'This field is required')]")).getText().contains("This field is required.");
	}

	public void clickEnrollmentNextBtnWithoutClickOnUseAsEnteredAddress() throws InterruptedException{
		driver.waitForElementPresent(By.id("enrollment-next-button"));
		driver.pauseExecutionFor(2000);
		driver.click(By.id("enrollment-next-button"));
		logger.info("EnrollmentTest Next Button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public void clickOnReviewAndConfirmBillingEditBtn(){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Billing info')]/a"));
		driver.findElement(By.xpath("//h3[contains(text(),'Billing info')]/a")).click();
	}

	public boolean isEnterNameOnCardPrepopulated(){
		return driver.findElement(By.id("card-name")).getAttribute("value").length()>0;
	}

	public boolean isEnterCardNumberPrepopulated(){
		driver.waitForElementPresent(By.id("card-nr"));
		return driver.findElement(By.id("card-nr")).getAttribute("value").length()>0;
	}

	public void clickEnrollmentNextBtnWithoutHandlingPopUP(){
		driver.waitForElementPresent(By.id("enrollment-next-button"));
		driver.pauseExecutionFor(2000);
		driver.click(By.id("enrollment-next-button"));
		logger.info("EnrollmentTest Next Button clicked");
	}

	public String getDotComPWS(){
		driver.waitForElementPresent(By.xpath("//p[@id='prefix-validation']/span[1]"));
		String pwsUnderPulse = driver.findElement(By.xpath("//p[@id='prefix-validation']/span[1]")).getText();
		String[] pws = pwsUnderPulse.split("/");
		String comPwsString = pws[0]+"/"+pws[1]+"/"+pws[2]+"/"+pws[3];
		logger.info("Com Pws String from UI is "+comPwsString);
		return comPwsString;
	}

	public String getDotBizPWS(){
		driver.waitForElementPresent(By.xpath("//p[@id='prefix-validation']/span[2]"));
		String pwsUnderPulse = driver.findElement(By.xpath("//p[@id='prefix-validation']/span[2]")).getText();
		String[] pws = pwsUnderPulse.split("/");
		String bizPwsString = pws[0]+"/"+pws[1]+"/"+pws[2]+"/"+pws[3];
		logger.info("Biz Pws String from UI is "+bizPwsString);
		return bizPwsString;
	}

	public boolean verifyCurrentUrlContainComSite(){  
		driver.waitForPageLoad();
		return driver.getCurrentUrl().contains("com");
	}

	public boolean verifyCurrentUrlContainBizSite(){  
		driver.waitForPageLoad();
		return driver.getCurrentUrl().contains("biz");
	}

	public boolean verifyCreateAccountpageIsDisplayed(){
		return driver.findElement(By.xpath("//h1[contains(text(),'SetUp Account')]")).getText().contains("SetUp Account");
	}

	public boolean verifyCRPSelectionpageIsDisplayed(){
		return driver.findElement(By.xpath("//div[@id='main-content']/div[text()='6.Consultant Replenishment Program Selection']")).getText().contains("6.Consultant Replenishment Program Selection");
	}

	public void selectInvalidNewBillingCardExpirationDate(){
		driver.click(By.id("expiryMonth"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.click(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.click(By.id("expiryYear"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[2]"));
		driver.click(By.xpath("//select[@id='expiryYear']/option[2]"));
		logger.info("Invalid expiration date is selected");
	}

	public boolean validateInvalidCreditCardExpiryDate(){
		if(driver.findElement(By.xpath("//input[@id='monthYear']/following::label[1][contains(text(),'Must be a valid Expiration Date')]")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}

	public void selectKitAndEnrollmentType(String kitName,String regimenName,String enrollmentType){
		selectEnrollmentKitPage(kitName, regimenName);  
		chooseEnrollmentOption(enrollmentType);
	}

	public void enterInvalidPassword(String password){
		driver.waitForElementPresent(By.id("new-password-account"));
		driver.clear(By.id("new-password-account"));
		driver.type(By.id("new-password-account"),password+"\t");
		driver.clear(By.id("new-password-account"));
	}

	public void enterInvalidConfirmPassword(String password){
		driver.waitForElementPresent(By.id("new-password-account2"));
		driver.clear(By.id("new-password-account2"));
		driver.type(By.id("new-password-account2"),password+"\t");
		driver.clear(By.id("new-password-account2"));
	}

	public String getErrorMessageForInvalidSponser(){
		driver.waitForElementPresent(By.xpath("//div[@id='sponsorPage']//span[contains(text(),'No result found for')]"));
		String errorMessage=driver.findElement(By.xpath("//div[@id='sponsorPage']//span[contains(text(),'No result found for')]")).getText();
		return errorMessage;
	}

	public void clickOnEnrollUnderLastUplineProcessToPopupDisappear(String consultantEmailID) throws InterruptedException{
		while(true){
			if(!driver.findElement(By.id("inactiveConsultant180Popup")).getCssValue("display").contains("none")){
				clickOnEnrollUnderLastUpline();
				hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
				selectProductAndProceedToBuy();
				clickOnCheckoutButton();
				enterEmailAddress(consultantEmailID);
			}else{
				break;
			}
		}
	}

	public int getQuantityValueForTheFirstProduct(){
		driver.waitForElementPresent(By.xpath("//input[@id='quantity0']"));
		String qty=driver.findElement(By.xpath("//input[@id='quantity0']")).getAttribute("value");
		return Integer.parseInt(qty);
	}

	public int getQuantityValueForTheSecondProduct(){
		driver.waitForElementPresent(By.xpath("//input[@id='quantity1']"));
		String qty=driver.findElement(By.xpath("//input[@id='quantity1']")).getAttribute("value");
		return Integer.parseInt(qty);
	}

	public void enterNewShippingAddressNameDuringEnrollment(String name){
		driver.waitForElementPresent(By.id("attention"));
		driver.findElement(By.id("attention")).clear();
		driver.findElement(By.id("attention")).sendKeys(name);
		logger.info("New Shipping Address name is "+name);
	}

	public void enterNewShippingAddressLine1DuringEnrollment(String addressLine1){
		driver.findElement(By.id("address-1")).clear();
		driver.findElement(By.id("address-1")).sendKeys(addressLine1);
		logger.info("New Shipping Address is "+addressLine1);
	}

	public void enterNewShippingAddressCityDuringEnrollment(String city){
		driver.findElement(By.id("city")).clear();
		driver.findElement(By.id("city")).sendKeys(city);
		logger.info("New Shipping City is "+city);
	}

	public void selectNewShippingAddressStateDuringEnrollment(){
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']//option[2]"));
		driver.click(By.xpath("//select[@id='state']//option[2]"));
		logger.info("State/Province selected");
	}

	public void clickEditShipping(){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div/div[2]/a"));
		driver.click(By.xpath("//div[@id='multiple-addresses-summary']/div/div[2]/a"));
		logger.info("Edit shipping link clicked.");
	}

	public void clickOnSaveShippingProfileAfterEdit() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveShippingAddreessId"));
		driver.click(By.id("saveShippingAddreessId")); 
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyUpdatedShippingAddress(String address){
		try{
			driver.findElement(By.xpath("//div[@id='multiple-addresses-summary']/div//span[contains(text(),'"+address+"')]"));
			return true;
		}catch(NoSuchElementException e){
			String word = Character.toUpperCase(address.charAt(0)) + address.substring(1);
			if(driver.isElementPresent(By.xpath("//div[@id='multiple-addresses-summary']/div//span[contains(text(),'"+word+"')]"))){
				return true;
			}else{
				return false;
			}
		}
	}

	public boolean isDefaultShippingAddressRadioBtnSelected(String defaultAddressFirstName) throws InterruptedException{
		try{
			driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstName+"')]/following::span/input"));
			return driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstName+"')]/following::span/input")).isSelected();
		}catch(NoSuchElementException e){
			String word = Character.toUpperCase(defaultAddressFirstName.charAt(0)) + defaultAddressFirstName.substring(1);
			if(driver.isElementPresent(By.xpath("//span[contains(text(),'"+word+"')]/following::span/input"))){
				return driver.findElement(By.xpath("//span[contains(text(),'"+word+"')]/following::span/input")).isSelected();
			}else {
				return false;
			}
		}
	}

	public boolean verifyShippingAddressOnOrderPage(String address){
		try{
			driver.findElement(By.xpath("//div[@id='checkout_summary_deliverymode_div']//span[contains(text(),'"+address+"')]"));
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}

	public boolean verifyShippingAddressIsPresentOnReviewPage(String name){
		driver.waitForElementNotPresent(By.xpath("//div[@id='summarySection']//span[contains(text(),'"+name+"')]"));
		return driver.isElementPresent(By.xpath("//div[@id='summarySection']//span[contains(text(),'"+name+"')]"));
	}

	public StoreFrontAccountInfoPage clickOnEditShippingOnReviewAndConfirmPage(){
		driver.waitForElementPresent(By.xpath("//div[@id='summarySection']/div[4]//a[contains(text(),'Edit')]"));
		driver.click(By.xpath("//div[@id='summarySection']/div[4]//a[contains(text(),'Edit')]"));
		logger.info("Edit shipping link clicked on review and confirm page.");
		return new StoreFrontAccountInfoPage(driver);
	}

	public void editFirstName(String firstName){
		driver.waitForElementPresent(By.id("first-name"));
		driver.clear(By.id("first-name"));
		driver.type(By.id("first-name"), firstName);
		logger.info("first name entered as "+firstName);
	}

	public void enterNewShippingAddressName(String name){
		driver.waitForElementPresent(By.id("new-attention"));
		driver.findElement(By.id("new-attention")).clear();
		driver.findElement(By.id("new-attention")).sendKeys(name);
		logger.info("New Shipping Address name is "+name);
	}

	public void clickOnEditBillingProfile() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//a[text()='Edit']"));
		driver.click(By.xpath("//div[@id='multiple-billing-profiles']//a[text()='Edit']"));
		logger.info("Edit billing profile clicked");
	}

	public void clickOnEditBillingOnReviewAndConfirmPage(){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Billing info')]/a[text()='Edit']"));
		driver.click(By.xpath("//h3[contains(text(),'Billing info')]/a[text()='Edit']"));
	}

	public String getProductName(){
		if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div[1]/h3/a"));
			String productName=driver.findElement(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div[1]/h3/a")).getText();
			logger.info("fetched product name is "+productName);
			return productName;
		}
		else {
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]/h3/a"));
			String productName=driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div[1]/h3/a")).getText();
			logger.info("fetched product name is "+productName);
			return productName;
		}		
	}


	public boolean verifyNumberOfProductsInMiniCart(String numberOfProductsInCart){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.xpath("//a[@id='shopping-cart']")); 
		WebElement allProducts = driver.findElement(By.xpath("//a[@id='shopping-cart']"));
		actions.moveToElement(allProducts).build().perform();
		driver.waitForLoadingImageToDisappear();
		driver.waitForElementPresent(By.xpath("//ul[@id='subtotal']/li/span[1]")); 
		String productCount=driver.findElement(By.xpath("//ul[@id='subtotal']/li/span[1]")).getText();
		return productCount.contains(numberOfProductsInCart);
	}

	public boolean isProductImageExist(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='big-picture']/img"));
		return driver.isElementPresent(By.xpath("//div[@id='big-picture']/img"));
	}

	public boolean verifyProductName(String ProductName){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='product-details']/div[1]/div/h1"));
		String name=driver.findElement(By.xpath("//div[@id='product-details']/div[1]/div/h1")).getText();
		return name.contains(ProductName);
	}

	public void mouseHoverProductAndClickQuickInfo(){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		if(driver.getCountry().equalsIgnoreCase("us")){
			driver.waitForElementPresent(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div[1]//a[1]/img")); 
			WebElement allProducts = driver.findElement(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div[1]//a[1]/img"));
			actions.moveToElement(allProducts).build().perform();
			driver.quickWaitForElementPresent(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div[1]//a[2]/input"));
			driver.click(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div[1]//a[2]/input"));
			driver.pauseExecutionFor(5000);
		}
		else{
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]/div[1]/a[1]/img")); 
			WebElement allProducts = driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div[1]/div[1]/a[1]/img"));
			actions.moveToElement(allProducts).build().perform();
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]/div[1]/a[2]/input"));
			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]/div[1]/a[2]/input"));
			driver.pauseExecutionFor(5000);
		}
	}


	public boolean isModalWindowExists(){
		driver.quickWaitForElementPresent(By.id("popup-quickinfo"));
		return driver.isElementPresent(By.id("popup-quickinfo"));
	}

	public void updateProductQuantityOnModalWindowAndProceedToBuy(String qty){
		driver.waitForElementPresent(By.id("quickinfo-quantity"));
		driver.findElement(By.id("quickinfo-quantity")).clear();
		driver.findElement(By.id("quickinfo-quantity")).sendKeys(qty);
		logger.info("quantity added is "+qty);
		driver.click(By.xpath("//form[@id='productDetailFormQuickInfo']/input[3]"));
		driver.pauseExecutionFor(5500);
		logger.info("Update button clicked after adding quantity");
		driver.waitForPageLoad();
	}

	public void clickMiniCart(){
		driver.waitForElementPresent(By.xpath("//a[@id='shopping-cart']"));
		driver.click(By.xpath("//a[@id='shopping-cart']"));
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public double getSubTotalOnShoppingCartPage(){
		driver.waitForElementPresent(By.xpath("//div[@id='subtotal-shopping']//span"));
		String total = driver.findElement(By.xpath("//div[@id='subtotal-shopping']//span")).getText();
		String total1[] = total.split("\\$");
		return Double.parseDouble(total1[1]);
	}


	public boolean verifySubTotalAccordingToQuantity(String qty,double subTotalOfAddedProduct,double subTotalOfAfterUpdate){
		double quantity = Double.parseDouble(qty);
		double totalAfterUpdate = (quantity*subTotalOfAddedProduct);
		if(totalAfterUpdate == (subTotalOfAfterUpdate)){
			return true;}
		else{return false;}
	}

	public void clickProductLinkForProductDetail(){
		driver.waitForElementPresent(By.xpath("//div[@id='mini-shopping']/div[1]/div/div[2]/div[1]/a"));
		driver.click(By.xpath("//div[@id='mini-shopping']/div[1]/div/div[2]/div[1]/a"));
		driver.waitForPageLoad();
	}

	public boolean validateQuickShopScreen(){
		driver.pauseExecutionFor(1500);
		return driver.getCurrentUrl().contains("quickShop");
	}

	public boolean verifyAddToBagButton() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));

		}
		return false;

	}

	public void clickAddToBagButtonOnQuickInfoPopup(){
		driver.quickWaitForElementPresent(By.xpath("//form[@id='productDetailFormQuickInfo']/input[3]"));
		driver.click(By.xpath("//form[@id='productDetailFormQuickInfo']/input[3]"));
		logger.info("Add To Bag button clicked from Quick Info Screen");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();

	}

	public boolean verifyAddToBagButtonOnQuickInfoPopup() throws InterruptedException{
		driver.quickWaitForElementPresent(By.xpath("//form[@id='productDetailFormQuickInfo']/input[3]"));
		return driver.isElementPresent(By.xpath("//form[@id='productDetailFormQuickInfo']/input[3]"));

	}

	public void clickViewProductDetailLink(){
		driver.quickWaitForElementPresent(By.id("quickinfo-view-details"));
		driver.click(By.xpath("//p[@id='quickinfo-view-details']/a"));
		logger.info("view product detail link clicked");
		driver.waitForPageLoad();
	}

	public boolean verifyAddToBagButtonOnProductDetailPage(){
		driver.quickWaitForElementPresent(By.xpath("//input[@value='ADD TO BAG']"));
		return driver.isElementPresent(By.xpath("//input[@value='ADD TO BAG']"));
	}

	public boolean verifyProductPrice(String Price){
		driver.quickWaitForElementPresent(By.xpath("//p[@id='retail-price']/span"));
		String name=driver.findElement(By.xpath("//p[@id='retail-price']/span")).getText();
		return name.contains(Price);
	}

	//	public String getProductName(){
	//		if(driver.getCountry().equalsIgnoreCase("US")){
	//			driver.quickWaitForElementPresent(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div[1]/h3/a"));
	//			String productName=driver.findElement(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div[1]/h3/a")).getText();
	//			logger.info("fetched product name is "+productName);
	//			return productName;
	//		}else if(driver.getCountry().equalsIgnoreCase("CA")){
	//			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]/h3/a"));
	//			String productName=driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div[1]/h3/a")).getText();
	//			logger.info("fetched product name is "+productName);
	//			return productName;
	//		}
	//		return null;
	//	}

	public String getProductPrice(){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]/p/span[1]"));
			String productName=driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div[1]/p/span[1]")).getText();
			logger.info("fetched product name is "+productName);
			return productName;
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]/p/span[1]"));
			String productName=driver.findElement(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]/p/span[1]")).getText();
			logger.info("fetched product name is "+productName);
			return productName;
		}
		return null;
	}

	public void enterSponsorNameAndClickOnSearchForPCAndRC(){
		driver.waitForElementPresent(By.xpath("//input[@id='sponsor-name-id']"));
		driver.type(By.xpath("//input[@id='sponsor-name-id']"), "test");
		//		if(driver.getCountry().equalsIgnoreCase("ca"))
		//			driver.click(By.xpath("//input[@value='Search']"));
		driver.click(By.xpath("//input[@value='Search']"));
	}

	public boolean verifyPCPerksCheckBoxIsSelected(){
		driver.waitForElementPresent(By.xpath("//input[@id='pc-customer2'][@class='checked']"));
		return driver.isElementPresent(By.xpath("//input[@id='pc-customer2'][@class='checked']"));
	}

	public boolean validateRFConnectionLink(){
		driver.waitForElementPresent(By.xpath("//div[@class='footer-sections']//a[contains(text(),'RF Connection')]"));
		driver.click(By.xpath("//div[@class='footer-sections']//a[contains(text(),'RF Connection')]"));
		driver.pauseExecutionFor(5000);
		Set<String> set=driver.getWindowHandles();
		Iterator<String> it=set.iterator();
		String parentWindow=it.next();
		String childWindow=it.next();
		driver.switchTo().window(childWindow);
		boolean status= driver.getCurrentUrl().contains("rfconnection");
		driver.close();
		driver.switchTo().window(parentWindow);
		return status;
	}

	public boolean validateCareersLink(){
		driver.waitForElementPresent(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Careers')]"));
		driver.click(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Careers')]"));
		driver.pauseExecutionFor(4000);
		return driver.getCurrentUrl().contains("careers");
	}

	public boolean validateContactUsLink(){
		driver.waitForElementPresent(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Contact Us')]"));
		driver.click(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Contact Us')]"));
		driver.pauseExecutionFor(4000);
		return driver.getCurrentUrl().contains("contact");
	}

	public boolean validateDisclaimerLink(){
		driver.waitForElementPresent(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Disclaimer')]"));
		driver.click(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Disclaimer')]"));
		driver.pauseExecutionFor(5000);
		return driver.getCurrentUrl().contains("disclaimer");
	}

	public boolean validatePrivacyPolicyLink(){
		driver.waitForElementPresent(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Privacy Policy')]"));
		driver.click(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Privacy Policy')]"));
		driver.pauseExecutionFor(5500);
		return driver.getCurrentUrl().contains("privacy-policy");
	}

	public boolean validateTermsConditionsLink(){
		driver.waitForElementPresent(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Terms & Cond')]"));
		driver.click(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Terms & Cond')]"));
		driver.pauseExecutionFor(4000);
		return driver.getCurrentUrl().contains("terms-conditions");
	}

	public boolean validateSatisfactionGuaranteeLink(){
		driver.waitForElementPresent(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Satisfaction Guarantee')]"));
		driver.click(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Satisfaction Guarantee')]"));
		driver.pauseExecutionFor(5000);
		return driver.getCurrentUrl().contains("satisfaction-guarantee");
	}

	public boolean validatePressRoomLink(){
		driver.waitForElementPresent(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Press Room')]"));
		driver.click(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Press Room')]"));
		driver.pauseExecutionFor(5000);
		return driver.getCurrentUrl().contains("pressroom");
	}

	public boolean validateDermRFLink(){
		driver.waitForElementPresent(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Derm RF')]"));
		driver.click(By.xpath("//div[@class='footer-sections']//a[contains(text(),'Derm RF')]"));
		driver.pauseExecutionFor(8000);
		Set<String> set=driver.getWindowHandles();
		Iterator<String> it=set.iterator();
		String parentWindow=it.next();
		String childWindow=it.next();
		driver.switchTo().window(childWindow);
		boolean status= driver.getCurrentUrl().contains("dermrf");
		driver.close();
		driver.switchTo().window(parentWindow);
		return status;
	}

	public boolean validateCountryFlagDropDownBtn(){
		return driver.isElementPresent(By.xpath("//div[@class='hidden-xs footer-content']//button")) ;
	}

	public boolean validateLoginFunctionality(){
		driver.waitForElementPresent(LOGIN_LINK_LOC);
		driver.click(LOGIN_LINK_LOC);
		logger.info("login link clicked");
		driver.pauseExecutionFor(1500);
		return driver.isElementPresent(USERNAME_TXTFLD_LOC) && driver.isElementPresent(PASSWORD_TXTFLD_LOC) 
				&&  driver.isElementPresent(LOGIN_BTN_LOC)  && driver.isElementPresent(FORGOT_PASSWORD_LOC);
	}

	public boolean validateTopNavigationMenuElements(){
		driver.pauseExecutionFor(1000);
		return driver.isElementPresent(By.xpath("//div[@id='header']//li[@id='CompanyBar']")) &&
				driver.isElementPresent(By.xpath("//div[@id='header']//li[@id='BusinessSystemBar']"))  &&
				driver.isElementPresent(By.xpath("//div[@id='header']//li[@id='ShopOurProductsBar']"));
	}

	public boolean verifyRedefineCategory(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']//h2"));
		return driver.isElementPresent(By.xpath("//div[@id='main-content']//h2"));
	}

	public boolean verifyProductsInRedefineCategory(){
		if(driver.getCountry().equalsIgnoreCase("US")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div"));
		}else if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[5]/div"));
		}
		return false;
	}

	public boolean verifyReverseCategory(){
		driver.quickWaitForElementPresent(By.xpath("//h2[text()='REVERSE']"));
		return driver.isElementPresent(By.xpath("//h2[text()='REVERSE']"));
	}

	public boolean verifyProductsInReverseCategory(){
		driver.waitForElementPresent(By.xpath("//h2[text()='REVERSE']/../following-sibling::div[contains(@class,'quick-product-wrapper')]/div"));
		return driver.isElementPresent(By.xpath("//h2[text()='REVERSE']/../following-sibling::div[contains(@class,'quick-product-wrapper')]/div"));
	}

	public boolean verifySootheCategory(){
		driver.quickWaitForElementPresent(By.xpath("//h2[text()='SOOTHE']"));
		return driver.isElementPresent(By.xpath("//h2[text()='SOOTHE']"));
	}

	public boolean verifyProductsInSootheCategory(){
		driver.waitForElementPresent(By.xpath("//h2[text()='SOOTHE']/../following-sibling::div[contains(@class,'quick-product-wrapper')]/div"));
		return driver.isElementPresent(By.xpath("//h2[text()='SOOTHE']/../following-sibling::div[contains(@class,'quick-product-wrapper')]/div"));
	}

	public boolean verifyUnblemishCategory(){
		driver.quickWaitForElementPresent(By.xpath("//h2[text()='UNBLEMISH']"));
		return driver.isElementPresent(By.xpath("//h2[text()='UNBLEMISH']"));
	}

	public boolean verifyProductsInUnblemishCategory(){
		driver.waitForElementPresent(By.xpath("//h2[text()='UNBLEMISH']/../following-sibling::div[contains(@class,'quick-product-wrapper')]/div"));
		return driver.isElementPresent(By.xpath("//h2[text()='UNBLEMISH']/../following-sibling::div[contains(@class,'quick-product-wrapper')]/div"));
	}

	public boolean verifyEssentialCategory(){
		driver.quickWaitForElementPresent(By.xpath("//h2[text()='ESSENTIALS']"));
		return driver.isElementPresent(By.xpath("//h2[text()='ESSENTIALS']"));
	}

	public boolean verifyProductsInEssentialCategory(){
		driver.waitForElementPresent(By.xpath("//h2[text()='ESSENTIALS']/../following-sibling::div[contains(@class,'quick-product-wrapper')]/div"));
		return driver.isElementPresent(By.xpath("//h2[text()='ESSENTIALS']/../following-sibling::div[contains(@class,'quick-product-wrapper')]/div"));
	}

	public boolean verifyEnhancementCategory(){
		driver.quickWaitForElementPresent(By.xpath("//h2[text()='ENHANCEMENTS']"));
		return driver.isElementPresent(By.xpath("//h2[text()='ENHANCEMENTS']"));
	}

	public boolean verifyProductsInEnhancementCategory(){
		driver.waitForElementPresent(By.xpath("//h2[text()='ENHANCEMENTS']/../following-sibling::div[contains(@class,'quick-product-wrapper')]/div"));
		return driver.isElementPresent(By.xpath("//h2[text()='ENHANCEMENTS']/../following-sibling::div[contains(@class,'quick-product-wrapper')]/div"));
	}

	public boolean verifyAddToBagButton(int number) throws InterruptedException{
		logger.info("Random Selected product for detail assertion is "+number);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+number+"]/div[2]/div[1]//button"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+number+"]/div[2]/div[1]//button"));
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div["+number+"]/div[2]/div[1]//button"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div["+number+"]/div[2]/div[1]//button"));

		}
		return false;

	}

	public boolean verifyRetailPriceOfProduct(int number) throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+number+"]/p/span[1]"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+number+"]/p/span[1]"));
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div["+number+"]/p/span[1]"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div["+number+"]/p/span[1]"));

		}
		return false;

	}

	public boolean verifyMyPriceOfProduct(int number) throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath(".//div[@id='main-content']/div[5]/div["+number+"]/p/span[2]"));
			return driver.isElementPresent(By.xpath(".//div[@id='main-content']/div[5]/div["+number+"]/p/span[2]"));
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div["+number+"]/p/span[2]"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div["+number+"]/p/span[2]"));

		}
		return false;

	}

	public boolean verifyAddToCRPButton(int number) throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+number+"]/div[2]/div[2]//form[@id='productDetailForm']"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+number+"]/div[2]/div[2]//form[@id='productDetailForm']"));
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div["+number+"]/div[2]/div[2]//button"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div["+number+"]/div[2]/div[2]//button"));

		}
		return false;

	}

	public boolean verifySVValue(int number) throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+number+"]/p"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+number+"]/p"));
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div["+number+"]/p"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div["+number+"]/p"));

		}
		return false;

	}

	public boolean verifyAddToPCPerksButton(int number) throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div["+number+"]/div[2]/div[2]//input"));
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div["+number+"]/div[2]/div[2]//input"));

		}
		return false;

	}

	public boolean verifyUrlAfterplacedAnAdhocOrder(String urlBeforeOrderPlaced, String urlAfterOrderPlaced){
		return urlBeforeOrderPlaced.equalsIgnoreCase(urlAfterOrderPlaced);
	}

	public String getProductName(int num){
		if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div["+num+"]/h3/a"));
			String productName=driver.findElement(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div["+num+"]/h3/a")).getText();
			logger.info("fetched product name is "+productName);
			return productName;
		}else if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+num+"]/h3/a"));
			String productName=driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div["+num+"]/h3/a")).getText();
			logger.info("fetched product name is "+productName);
			return productName;
		}
		return null;
	}

	public String getProductPrice(int num){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+num+"]/p/span[1]"));
			String productName=driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div["+num+"]/p/span[1]")).getText();
			logger.info("fetched product name is "+productName);
			return productName;
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div["+num+"]/p/span[1]"));
			String productName=driver.findElement(By.xpath("//div[@id='main-content']/div[4]/div[2]/div["+num+"]/p/span[1]")).getText();
			logger.info("fetched product name is "+productName);
			return productName;
		}
		return null;
	}

	public void mouseHoverProductAndClickQuickInfo(int num){
		if(driver.getCountry().equalsIgnoreCase("US")){
			Actions actions = new Actions(RFWebsiteDriver.driver);
			driver.waitForElementPresent(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div["+num+"]//a[1]/img")); 
			WebElement allProducts = driver.findElement(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div["+num+"]//a[1]/img"));
			actions.moveToElement(allProducts).build().perform();
			driver.quickWaitForElementPresent(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div["+num+"]//a[2]/input"));
			driver.click(By.xpath(".//div[@id='main-content']/div[4]/div[2]/div["+num+"]//a[2]/input"));
			driver.pauseExecutionFor(5000);
		}else if(driver.getCountry().equalsIgnoreCase("CA")){
			Actions actions = new Actions(RFWebsiteDriver.driver);
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+num+"]/div[1]/a[1]/img")); 
			WebElement allProducts = driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div["+num+"]/div[1]/a[1]/img"));
			actions.moveToElement(allProducts).build().perform();
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div["+num+"]/div[1]//a[2]/input"));
			driver.click(By.xpath("//div[@id='main-content']/div[5]/div["+num+"]/div[1]//a[2]/input"));
			driver.pauseExecutionFor(5000);
		}
	}

	public boolean verifyAddToPCPerksButtonOnQuickInfoPopup(){
		driver.quickWaitForElementPresent(By.xpath("//form[@id='productDetailForm']/input[3]"));
		return driver.isElementPresent(By.xpath("//form[@id='productDetailForm']/input[3]"));
	}

	public boolean verifyAddToPCPerksButtonOnProductDetailPage(){
		driver.quickWaitForElementPresent(By.xpath("//input[@value='Add to PC Perks']"));
		return driver.isElementPresent(By.xpath("//input[@value='Add to PC Perks']"));
	}

	public boolean verifyProductLongDescription(){
		driver.quickWaitForElementPresent(By.id("description-box"));
		return driver.isElementPresent(By.id("description-box"));
	}

	public boolean verifyAddToCRPButtonOnQuickInfoPopup(){
		driver.quickWaitForElementPresent(By.xpath("//form[@id='productDetailForm']/input[3]"));
		return driver.isElementPresent(By.xpath("//form[@id='productDetailForm']/input[3]"));
	}

	public boolean verifyAddToCRPButtonOnProductDetailPage(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='shop']/div[3]/div[1]/div[2]/form[2]//input[3]"));
		return driver.isElementPresent(By.xpath("//div[@id='shop']/div[3]/div[1]/div[2]/form[2]//input[3]"));
	}

	public boolean verifyPriceFromLowToHigh(){
		driver.waitForElementPresent(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[1]//span[@class='your-price']"));{
			String firstProductPrice = driver.findElement(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[1]//span[@class='your-price']")).getText().split("\\$")[1].trim();
			String secondProductPrice = driver.findElement(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[2]//span[@class='your-price']")).getText().split("\\$")[1].trim();
			String thirdProductPrice = driver.findElement(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[3]//span[@class='your-price']")).getText().split("\\$")[1].trim();
			System.out.println("1st "+firstProductPrice);
			System.out.println("2nd "+secondProductPrice);
			System.out.println("3rd "+thirdProductPrice);

			if(Double.parseDouble(thirdProductPrice)>Double.parseDouble(secondProductPrice)){
				if(Double.parseDouble(secondProductPrice)>Double.parseDouble(firstProductPrice)){
					return true;
				}else{
					return false;
				}
			}return false;
		}
	}


	public boolean verifyPriceFromHighTolow(){
		driver.waitForElementPresent(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[1]//span[@class='your-price']"));{
			String firstProductPrice = driver.findElement(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[1]//span[@class='your-price']")).getText().split("\\$")[1].trim();
			String secondProductPrice = driver.findElement(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[2]//span[@class='your-price']")).getText().split("\\$")[1].trim();
			String thirdProductPrice = driver.findElement(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[3]//span[@class='your-price']")).getText().split("\\$")[1].trim();
			System.out.println("1st "+firstProductPrice);
			System.out.println("2nd "+secondProductPrice);
			System.out.println("3rd "+thirdProductPrice);

			if(Double.parseDouble(firstProductPrice)>Double.parseDouble(secondProductPrice)){
				if(Double.parseDouble(secondProductPrice)>Double.parseDouble(thirdProductPrice)){
					return true;
				}else{
					return false;
				}
			}return false;
		}

	}

	public boolean verifyPriceAfterDeselectThefilter(String priceBeforeApplyFilter){
		return driver.findElement(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[1]//span[@class='your-price']")).getText().split("\\$")[1].trim().contains(priceBeforeApplyFilter);
	}

	public String getProductNameBeforeApplyProductFilter(){
		System.out.println("Hi");
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']//div[@class='quick-shop-section-header']/h2"));
		System.out.println(driver.findElement(By.xpath("//div[@id='main-content']//div[@class='quick-shop-section-header']/h2")).getText());
		return driver.findElement(By.xpath("//div[@id='main-content']//div[@class='quick-shop-section-header']/h2")).getText();
	}

	public int getSizeOfProductFilter(){
		driver.waitForElementPresent(By.xpath("//input[@class='refine-products-button']"));
		driver.click(By.xpath("//input[@class='refine-products-button']"));
		int sizeOfProduct = driver.findElements(By.xpath("//ul[contains(@class,'refine-products')]/li")).size();
		driver.click(By.xpath("//input[@class='refine-products-button']"));
		return sizeOfProduct;
	}

	public boolean verifyProductFilterIsApply(int i){
		driver.waitForElementPresent(By.xpath("//input[@class='refine-products-button']"));
		driver.click(By.xpath("//input[@class='refine-products-button']"));
		driver.waitForElementPresent(By.xpath("//ul[contains(@class,'refine-products')]/li[1]"));
		String productNameFromfilter = driver.findElement(By.xpath("//ul[contains(@class,'refine-products')]/li["+i+"]//div[contains(@class,'dropdown-items text')]")).getText().trim();
		driver.click(By.xpath("//ul[contains(@class,'refine-products')]/li["+i+"]//div[@class='pull-right']//input/.."));
		driver.waitForPageLoad();
		String productNameFromUI = driver.findElement(By.xpath("//div[@class='quick-shop-section-header']/h2")).getText().trim();
		driver.waitForElementPresent(By.xpath("//input[@class='refine-products-button']"));
		driver.click(By.xpath("//input[@class='refine-products-button']"));
		driver.pauseExecutionFor(1000);
		driver.click(By.xpath("//ul[contains(@class,'refine-products')]/li["+i+"]//div[@class='pull-right']//input/.."));
		driver.waitForPageLoad();
		return productNameFromUI.contains(productNameFromfilter);
	}

	public void clickOnClearAll(){
		driver.click(By.xpath("//a[contains(text(),'Clear All')]"));
		driver.waitForPageLoad();
	}

	public boolean verifyProductNameAfterRemoveProductFilter(String productNameBeforeApplyFilter){
		driver.waitForElementPresent(By.xpath("//div[@class='quick-shop-section-header']/h2"));
		return driver.findElement(By.xpath("//div[@class='quick-shop-section-header']/h2")).getText().trim().contains(productNameBeforeApplyFilter.trim());
	}

	public void enterSponsorNameAndClickOnSearchForPCAndRC(String sponsor){
		driver.waitForElementPresent(By.xpath("//input[@id='sponsor-name-id']"));
		driver.type(By.xpath("//input[@id='sponsor-name-id']"),sponsor);
		if(driver.getCountry().equalsIgnoreCase("ca")){
			driver.click(By.xpath("//input[@value='Search']"));
		}
		driver.click(By.xpath("//input[@value='Search']"));
	}

	public void clickEditShippingInShipmentOnCheckoutPage(){
		driver.waitForElementPresent(By.xpath("//div[@id='checkout_summary_deliverymode_div']/div[1]/a[text()='Edit']"));
		driver.click(By.xpath("//div[@id='checkout_summary_deliverymode_div']/div[1]/a[text()='Edit']"));
		logger.info("Edit For shipping address clicked.");
	}

	public String getPCPerksMessageFromModalPopup(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='quickinfo-shop']/div[5]/p"));
		return driver.findElement(By.xpath("//div[@id='quickinfo-shop']/div[5]/p")).getText();
	}

	public boolean verifyPCPerksTermsAndConditionsPopupPresent() throws InterruptedException{
		boolean isPCPerksTermsAndConditionsPopup = false;
		driver.waitForElementPresent(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']"));
		isPCPerksTermsAndConditionsPopup = driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']")));
		if(isPCPerksTermsAndConditionsPopup==true){
			return true;
		}
		return false;
	}


	public String getPCPerksTermsAndConditionsPopupText(){
		driver.waitForElementPresent(By.xpath("//div[@id='pcperktermsconditions']//p[1]"));
		String textFromUI=driver.findElement(By.xpath("//div[@id='pcperktermsconditions']//p[1]")).getText();
		logger.info("terms and candition text from UI is "+textFromUI);
		return textFromUI;
	}


	public void clickPCPerksTermsAndConditionPopupOkay(){
		driver.click(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']"));
		logger.info("PC Perks terms and candition popup okay button clicked");
	}

	public boolean validateDiscountForEnrollingAsPCUser(String discountText){
		driver.quickWaitForElementPresent(By.xpath("//span[contains(text(),'"+discountText+"')]/preceding-sibling::div/input[@class='checked']"));
		if(driver.isElementPresent(By.xpath("//span[contains(text(),'"+discountText+"')]/preceding-sibling::div/input[@class='checked']"))){
			return true;
		}else{
			return false;
		}
	}

	public void clickOnPCPerksPromoLink() {
		driver.waitForElementPresent(By.xpath("//div[@id='shopping-wrapper']/div[2]/div[2]/div[4]/a/span"));
		driver.click(By.xpath("//div[@id='shopping-wrapper']/div[2]/div[2]/div[4]/a/span"));
	}

	public boolean verifyModalWindowIsPresent() {
		driver.waitForElementPresent(By.xpath("//div[@id='popup-pcperks']"));
		if(driver.isElementPresent(By.xpath("//div[@id='popup-pcperks']"))){
			return true;}
		else{
			return false;
		}
	}

	public void clickOnModalWindowCloseIcon() {
		driver.click(By.xpath("//span[@class='icon-close']"));
	}

	public void clickOnOrderSummaryPCPerksPromoLink() {
		driver.waitForElementPresent(By.xpath("//div[@id='shopping-wrapper']//div[@class='mg-tp-5']/following::a/span"));
		driver.click(By.xpath("//div[@id='shopping-wrapper']//div[@class='mg-tp-5']/following::a/span"));		  
	}

	public String getPCPerksTermsAndConditionsPopupHeaderText(){
		driver.waitForElementPresent(By.xpath("//div[@id='pcperktermsconditions']//h2"));
		String textFromUI=driver.findElement(By.xpath("//div[@id='pcperktermsconditions']//h2")).getText();
		logger.info("terms and candition Header text from UI is "+textFromUI);
		return textFromUI;
	}

	public boolean verifyNotYourSponsorLinkPresent() {
		driver.waitForElementPresent(By.xpath("//a[@id='not-your-sponsor']"));
		if(driver.isElementPresent(By.xpath("//a[@id='not-your-sponsor']"))){
			logger.info("NOT YOUR SPONSOR LINK PRESENT");
			return true;
		}
		return false;
	}

	public boolean verifyContinueWithoutSponsorLinkIsPresent() {
		driver.waitForElementPresent(By.xpath("//a[@id='continue-no-sponsor']"));
		if(driver.isElementPresent(By.xpath("//a[@id='continue-no-sponsor']"))){
			logger.info("CONTINUE WITHOUR SPONSOR LINK PRESENT");
			return true;
		}
		return false;
	}

	public void clickOnSponsorName(){
		driver.waitForElementPresent(By.xpath("//div[@id='header-middle-top']//a"));
		driver.click(By.xpath("//div[@id='header-middle-top']//a"));
	}

	public boolean verifyContactBoxIsPresent(){
		return driver.isElementPresent(By.xpath("//div[@class='contactBox']"));
	}

	public boolean verifyEmailIdIsPresentInContactBox(){
		return driver.isElementPresent(By.id("txtContactMe"));
	}

	public boolean verifyPhoneNumberIsPresentInContactBox(){
		driver.waitForPageLoad();
		return driver.isElementPresent(By.xpath("//span[@class='icon-phone iconMsg']/following::a[1]"));
	}

	public void clickOnEditMyPWS(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'EDIT MY PWS')]"));
		driver.click(By.xpath("//a[contains(text(),'EDIT MY PWS')]"));
	}

	public void enterPhoneNumberOnEditPWS(String number){
		driver.waitForElementPresent(By.id("phone"));
		driver.type(By.id("phone"), number);
	}

	public void clickOnSaveAfterEditPWS(){
		driver.waitForElementPresent(By.xpath("//div[@class='editphotosmode']//input"));
		driver.click(By.xpath("//div[@class='editphotosmode']//input"));
	}

	public void clickCancelBtnOnEditConsultantInfoPage(){
		driver.quickWaitForElementPresent(By.xpath("//div[contains(@class,'top-save')]//a[@class='btn btn-secondary editmode-cancel']"));
		driver.click(By.xpath("//div[contains(@class,'top-save')]//a[@class='btn btn-secondary editmode-cancel']"));
		driver.pauseExecutionFor(1000);
	}

	public boolean validateSubmissionGuideLinesLink(){
		//click Submission Guidelines Link..
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Submission Guidelines')]"));
		driver.click(By.xpath("//a[contains(text(),'Submission Guidelines')]"));
		driver.pauseExecutionFor(4000);
		String parentWindowID=driver.getWindowHandle();
		Set<String> set=driver.getWindowHandles();
		Iterator<String> it=set.iterator();
		boolean status=false;
		while(it.hasNext()){
			String childWindowID=it.next();
			if(!parentWindowID.equalsIgnoreCase(childWindowID)){
				driver.switchTo().window(childWindowID);
				if(driver.getCurrentUrl().contains("PWS_Profile_Guidelines_US.pdf")){
					status=true;
					driver.close();
					return status;
				}
			}
		}
		return status;
	}

	public void checkEmailFieldCBOnEditConsultantInfoPage(){
		driver.waitForElementPresent(By.xpath("//input[@id='onlyShowContactMeForm']/.."));
		if(!driver.isElementPresent(By.xpath("//input[@checked='checked']"))){
			driver.click(By.xpath("//input[@id='onlyShowContactMeForm']/.."));
		}
	}

	public void clickSaveBtnOnEditConsultantInfoPage(){
		driver.quickWaitForElementPresent(By.xpath("//div[contains(@class,'top-save')]//input[@class='edit-meet-your-consultant btn btn-primary']"));
		driver.click(By.xpath("//div[contains(@class,'top-save')]//input[@class='edit-meet-your-consultant btn btn-primary']"));
		driver.pauseExecutionFor(1000);
	}

	public void enterUserInformationForEnrollmentWithTerminatedEmail(String kitName,String regimenName,String enrollmentType,String firstName,String lastName,String emailaddress,String password,String addressLine1,String city,String postalCode,String phoneNumber){
		// Actions actions = new Actions(RFWebsiteDriver.driver);
		selectEnrollmentKitPage(kitName, regimenName);  
		chooseEnrollmentOption(enrollmentType);
		enterFirstName(firstName);
		enterLastName(lastName);
		enterEmailAddress(emailaddress);
		// actions.sendKeys(Keys.TAB).perform();
		driver.waitForElementPresent(By.id("enrollUnderLastUpline"));
		clickOnEnrollUnderLastUpline();
		driver.pauseExecutionFor(6000);
	}

	public boolean verifyCurrentUrlContainCorp(){  
		driver.waitForPageLoad();
		System.out.println("current url is "+driver.getCurrentUrl());
		return driver.getCurrentUrl().toLowerCase().contains("corp");
	}

	public boolean verifyBizUrlAfterEnrollment(String pws){
		driver.waitForPageLoad();
		return driver.getCurrentUrl().trim().contains(pws.trim());
	}

	public boolean verifyNotYourSponsorLinkIsPresent(){
		driver.quickWaitForElementPresent(By.id("not-your-sponsor"));
		return driver.isElementPresent(By.id("not-your-sponsor"));
	}

	public void clickLearnMoreLinkUnderSolutionToolAndSwitchControl(){
		driver.waitForElementPresent(By.xpath("//div[@id='corp_content']/div/div[1]/div[3]/descendant::a"));
		driver.click(By.xpath("//div[@id='corp_content']/div/div[1]/div[3]/descendant::a"));
		driver.pauseExecutionFor(11000);
		String parentWindowID=driver.getWindowHandle();
		Set<String> set=driver.getWindowHandles();
		Iterator<String> it=set.iterator();
		while(it.hasNext()){
			String childWindowID=it.next();
			if(!parentWindowID.equalsIgnoreCase(childWindowID)){
				driver.switchTo().window(childWindowID);
			}
		}
	}

	public boolean validateConsultantNameOnTopRightCorner(){
		driver.waitForElementPresent(By.xpath("//div[@id='dis_name']"));
		return driver.isElementPresent(By.xpath("//div[@id='dis_name']"));
	}
}


