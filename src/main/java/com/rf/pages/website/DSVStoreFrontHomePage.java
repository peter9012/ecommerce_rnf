package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class DSVStoreFrontHomePage extends DSVRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(DSVStoreFrontHomePage.class.getName());

	private static final By LOGIN_LINK = By.xpath("//li[@id='log-in-button']/a");
	private static final By USERNAME_TXTFIELD = By.id("username");
	private static final By PASSWORD_TXTFIELD = By.id("password");
	private static final By LOGIN_BTN = By.xpath("//input[@value='Log in']");
	private static final By WELCOME_TXT = By.xpath("//li[@id='account-info-button']/a");
	private static final By CRP_CART_IMG = By.id("bag-special");
	private static final By NXT_CRP_TXT = By.xpath("//div[@id='bag-special']/following-sibling::div[1]");


	public DSVStoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public void clickLoginLink(){
		driver.quickWaitForElementPresent(LOGIN_LINK);
		driver.click(LOGIN_LINK);
	}

	public void enterUsername(String username){
		driver.quickWaitForElementPresent(USERNAME_TXTFIELD);
		driver.type(USERNAME_TXTFIELD, username);
	}

	public void enterPassword(String password){
		driver.type(PASSWORD_TXTFIELD, password);
	}

	public void clickLoginBtn(){
		driver.click(LOGIN_BTN);
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}
	
	public String getWelcomeText(){
		return driver.findElement(WELCOME_TXT).getText();
	}
	
	public boolean isCRPCartImagePresent(){
		return driver.isElementPresent(CRP_CART_IMG);
	}
	
	public String getNextCRPText(){
		return driver.findElement(NXT_CRP_TXT).getText();
	}
	
	
}
