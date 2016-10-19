package com.rf.pages.website.storeFront;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontAccountTerminationPage extends StoreFrontRFWebsiteBasePage {

	private static final Logger logger = LogManager
			.getLogger(StoreFrontAccountTerminationPage.class.getName());

	private final By ACCOUNT_TERMINATION_TEMPLATE_HEADER_LOC = By.xpath("//div[@id='main-content']//div[contains(text(),'Account termination')]");
	private final By SUBMIT_BOX_LOC = By.xpath("//input[@value='SUBMIT']");
	private final By ACCOUNT_TERMINATION_PAGE_POPUP_HEADER = By.xpath("//div[@id='popup-content']//h2");
	private final By POPUP_CANCEL_TERMINATION_BUTTON = By.xpath("//input[@value='Cancel Termination']");
	private final By POPUP_CONFIRM_TERMINATION_BUTTON = By.xpath("//input[@value='Confirm Termination']");
	public StoreFrontAccountTerminationPage(RFWebsiteDriver driver) {
		super(driver);
	}

	public boolean verifyAccountTerminationPageIsDisplayed(){
		driver.waitForElementPresent(ACCOUNT_TERMINATION_TEMPLATE_HEADER_LOC);
		return driver.getCurrentUrl().contains(TestConstants.ACCOUNT_TERMINATION_PAGE_SUFFIX_URL);

	}

	public void clickSubmitToTerminateAccount(){
		try{
			driver.quickWaitForElementPresent(SUBMIT_BOX_LOC);
			driver.click(SUBMIT_BOX_LOC);
		}catch(Exception e){
			driver.click(By.xpath("//input[@value='submit']"));
		}		
		logger.info("Submit to terminate account button clicked ");
	}

	public boolean verifyPopupHeader(){
		driver.waitForElementPresent(ACCOUNT_TERMINATION_PAGE_POPUP_HEADER);
		return driver.findElement(ACCOUNT_TERMINATION_PAGE_POPUP_HEADER).getText().contains("CONFIRM ACCOUNT TERMINATION");

	}
	public boolean verifyPopupCancelTerminationButton(){
		driver.waitForElementPresent(POPUP_CANCEL_TERMINATION_BUTTON);
		if(driver.findElement(POPUP_CANCEL_TERMINATION_BUTTON).isDisplayed()){
			return true;
		}else{

			return false;
		}
	}
	public boolean verifyPopupConfirmTerminationButton(){
		driver.waitForElementPresent(POPUP_CONFIRM_TERMINATION_BUTTON);
		if(driver.findElement(POPUP_CONFIRM_TERMINATION_BUTTON).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}
	public void clickCancelTerminationButton() throws InterruptedException{
		driver.waitForElementPresent(POPUP_CANCEL_TERMINATION_BUTTON);
		driver.click(POPUP_CANCEL_TERMINATION_BUTTON);		
	}

	public void selectTerminationReason() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//select[@id='reason']"));
		Select selectDD = new Select(driver.findElement(By.xpath("//select[@id='reason']")));
		selectDD.selectByVisibleText("Other");    	
		logger.info("Termination reason is selected as 'Other'");
	}

	public void enterTerminationComments(){
		driver.waitForElementPresent(By.xpath("//textarea[@id='terminationComments']"));
		driver.findElement(By.xpath("//textarea[@id='terminationComments']")).sendKeys("Automation Test comments");
		logger.info("termination comments entered");
	}

	public void selectCheckBoxForVoluntarilyTerminate() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='volunteerTermination']"));
		Actions actions = new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@class='repaired-checkbox']")));
		actions.click(driver.findElement(By.xpath("//div[@class='repaired-checkbox']"))).build().perform();
		logger.info("Checkbox for voluntarily terminate selected");
	}

	public void fillTheEntriesAndClickOnSubmitDuringTermination(){
		driver.waitForElementPresent(By.id("reason"));
		driver.click(By.id("reason"));
		driver.click(By.xpath("//select[@id='reason']/option[contains(text(),'Other')]"));
		driver.type(By.id("terminationComments"), "I want to terminate my account");
		driver.waitForElementPresent(By.xpath("//div[@class='repaired-checkbox']"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@class='repaired-checkbox']")));
		//driver.click(By.xpath("//div[@class='repaired-checkbox']"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//form[@id='accountTerminationInfo']//div/input[contains(@class,'btn btn-primary')]")));
		//  driver.click(By.xpath("//form[@id='accountTerminationInfo']//div/input[contains(@class,'btn btn-primary')]"));
		driver.waitForLoadingImageToDisappear();  
	}

	public boolean verifyAccountTerminationIsConfirmedPopup(){
		if(driver.findElement(By.xpath("//div[@id='showConsultantTerminatePopUp']")).isDisplayed()){
			return true;
		}else{
			return false;
		}
	}

	public void clickOnCloseWindowAfterTermination(){
		driver.waitForElementPresent(By.xpath("//input[@value='Close window']"));
		driver.click(By.xpath("//input[@value='Close window']"));		
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public void clickOnConfirmTerminationPopup(){
		try{
			driver.quickWaitForElementPresent(POPUP_CONFIRM_TERMINATION_BUTTON);
			driver.click(POPUP_CONFIRM_TERMINATION_BUTTON);
			logger.info("Confirm popup clicked");

		}catch(Exception e){
			System.out.println("Confirm popup not seen.");
		}

		driver.waitForPageLoad();
	}

	public void fillTheEntriesAndClickOnSubmitDuringTerminationForPC(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.click(By.xpath("//select[@id='problemType']/option[contains(text(),'Other')]"));
		driver.type(By.id("terminationComments"), "I want to terminate my account");
		driver.click(By.id("pcperkTerminationConfirm"));
	}

	public void clickOnConfirmAccountTermination(){
		driver.waitForElementPresent(By.xpath("//input[@id='confirmpcTemrminate']"));
		driver.click(By.xpath("//input[@id='confirmpcTemrminate']"));
		driver.waitForLoadingImageToDisappear();   
	}

	public boolean verifyMessageWithoutComments(){
		driver.waitForElementPresent(By.xpath("//textarea[@id='terminationComments']/following::label[@class='error']"));
		String message = driver.findElement(By.xpath("//textarea[@id='terminationComments']/following::label[@class='error']")).getText();
		System.out.println(message);
		if(message.equalsIgnoreCase("This field is required.")){
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyFieldValidatonForReason(){
		return driver.isElementPresent(By.xpath("//ul[@class='reason']//label[@class='error']"));
	}

	public boolean verifyCheckBoxValidationIsPresent(){
		return driver.isElementPresent(By.xpath("//div[@class='terminate-labelerrorLabel']/label"));
	}

	public boolean verifyMessageWithoutReason(){
		driver.waitForElementPresent(By.xpath("//div[@id='globalMessages']//p[1]"));
		String message = driver.findElement(By.xpath("//div[@id='globalMessages']//p[1]")).getText();
		System.out.println(message);
		if(message.equalsIgnoreCase("Reason is required.")){
			return true;
		}else{
			return false;}
	}

	public void clickOnAgreementCheckBox(){
		driver.quickWaitForElementPresent(By.xpath("//div[@class='repaired-checkbox']"));
		driver.click(By.xpath("//div[@class='repaired-checkbox']"));
	}

	public boolean validateConfirmAccountTerminationPopUp(){
		driver.waitForElementPresent(By.xpath("//div[@id='showConsultantTerminatePopUp' and @style='display: block;']"));
		return driver.isElementPresent(By.xpath("//div[@id='showConsultantTerminatePopUp' and @style='display: block;']"));
	}

	public void clickConfirmTerminationBtn(){
		driver.click(By.xpath("//input[@onclick='confirmTermination()']"));
		driver.pauseExecutionFor(2000);
	}

}
