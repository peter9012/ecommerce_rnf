package com.rf.pages.website.nscore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore3LoginPage extends NSCore3RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(NSCore3LoginPage.class.getName());

	public NSCore3LoginPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static final By LOGIN_BTN = By.id("btnLogin");
	private static final By USERNAME_TXT_FIELD = By.id("txtUsername");
	private static final By PASSWORD_TXT_FIELD = By.id("txtPassword");

	public void enterUsername(String userName){
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(USERNAME_TXT_FIELD);
		driver.type(USERNAME_TXT_FIELD, userName);
		logger.info("username is "+userName);
	}

	public void enterPassword(String password){
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(PASSWORD_TXT_FIELD);
		driver.type(PASSWORD_TXT_FIELD, password);
		logger.info("password is "+password);
	}

	public NSCore3HomePage clickLoginBtn(){
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.waitForElementPresent(LOGIN_BTN);
		driver.click(LOGIN_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.pauseExecutionFor(5000);
		return new NSCore3HomePage(driver);
	}

}