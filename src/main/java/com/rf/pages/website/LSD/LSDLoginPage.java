package com.rf.pages.website.LSD;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.rf.core.driver.website.RFWebsiteDriver;

public class LSDLoginPage extends LSDRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(LSDLoginPage.class.getName());

	public LSDLoginPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static final By LOGIN_BTN = By.id("login_submit_button");
	private static final By USERNAME_TXT_FIELD = By.id("username");
	private static final By PASSWORD_TXT_FIELD = By.id("password");
	private static final By LOGIN_ERROR_MSG = By.id("login-error");

	
	public void enterUsername(String userName){
		driver.waitForElementPresent(USERNAME_TXT_FIELD);
		driver.type(USERNAME_TXT_FIELD, userName);
		logger.info("username is "+userName);
	}

	public void enterPassword(String password){
//		driver.waitForElementPresent(PASSWORD_TXT_FIELD);
		driver.type(PASSWORD_TXT_FIELD, password);
		logger.info("password is "+password);
	}

	public LSDHomePage clickLoginBtn(){
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.waitForElementPresent(LOGIN_BTN);
		driver.click(LOGIN_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.pauseExecutionFor(5000);
		return new LSDHomePage(driver);
	}
	
	public boolean isLoginFailErrorPresent(){
		driver.waitForElementPresent(LOGIN_ERROR_MSG);
		return driver.isElementPresent(LOGIN_ERROR_MSG);
	}
}