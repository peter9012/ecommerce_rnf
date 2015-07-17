package com.rf.pages.website;

import org.openqa.selenium.By;

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
}
