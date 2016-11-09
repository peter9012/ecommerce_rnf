package com.rf.pages.website.crm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.storeFront.StoreFrontRFWebsiteBasePage;

public class CRMLoginPage extends CRMRFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(CRMLoginPage.class.getName());
	private final By LOGIN_BOX_LOCATION = By.id("login_form");
	private final By USERNAME_TXTFLD_LOC = By.id("username");
	private final By PASSWORD_TXTFLD_LOC = By.id("password");
	private final By LOGIN_BTN_LOC = By.id("Login");
	private final By ERROR_MESSAGE_LOC = By.id("error");


	public CRMLoginPage(RFWebsiteDriver driver) {
		super(driver);
	}
	public CRMHomePage loginUser(String username, String password){
		driver.waitForElementPresent(LOGIN_BOX_LOCATION);
		driver.clear(USERNAME_TXTFLD_LOC);
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.clear(PASSWORD_TXTFLD_LOC);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is: "+username);
		logger.info("login password is: "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		//driver.waitForLoadingImageBoxToDisappear();
		return new CRMHomePage(driver);
	}
	
	public void crmLogout(){
		driver.pauseExecutionFor(5000);
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.id("userNavLabel"));
		driver.click(By.id("userNavLabel"));
		driver.waitForElementPresent(By.id("app_logout"));
		driver.click(By.id("app_logout"));
		logger.info("Logout");
		driver.pauseExecutionFor(3000);
	}
	
	public void loginLogisticsUser(String username, String password){
		driver.waitForElementPresent(LOGIN_BOX_LOCATION);
		driver.clear(USERNAME_TXTFLD_LOC);
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.clear(PASSWORD_TXTFLD_LOC);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is: "+username);
		logger.info("login password is: "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");		
	}
	
	public String getErrorMessageOnLoginPage() throws InterruptedException{		
		driver.waitForElementPresent(ERROR_MESSAGE_LOC);
		return driver.findElement(ERROR_MESSAGE_LOC).getText();
	}
}
