package com.rf.pages.website;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.RFDriver;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

public class StoreFrontAccountTerminationPage extends RFWebsiteBasePage {
	private final By ACCOUNT_TERMINATION_TEMPLATE_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top' and contains(text(),' Account termination')]");
	private final By SUBMIT_BOX_LOC = By.xpath("//input[@value='submit']");
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
		driver.waitForElementPresent(SUBMIT_BOX_LOC);
		driver.findElement(SUBMIT_BOX_LOC).click();
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
		driver.findElement(POPUP_CANCEL_TERMINATION_BUTTON).click();
		Thread.sleep(3000);
	}

	public void selectTerminationReason() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//select[@id='reason']"));
		Select selectDD = new Select(driver.findElement(By.xpath("//select[@id='reason']")));
		selectDD.selectByVisibleText("Other");    	
	}

	public void enterTerminationComments(){
		driver.findElement(By.xpath("//textarea[@id='terminationComments']")).sendKeys("Automation Test comments");
	}

	public void selectCheckBoxForVoluntarilyTerminate() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='volunteerTermination']"));
		Thread.sleep(3000);
		Actions actions = new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@class='repaired-checkbox']")));
		actions.click(driver.findElement(By.xpath("//div[@class='repaired-checkbox']"))).build().perform();
	}
}
