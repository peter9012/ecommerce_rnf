package com.rf.pages.website.LSD;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.LSD.LSDLoginPage;
import com.rf.pages.RFBasePage;
import com.rf.pages.website.LSD.LSDBasePage;
import com.rf.pages.website.storeFront.StoreFrontRFWebsiteBasePage;

public class LSDLoginPage extends RFBasePage {
	
	private static final Logger logger = LogManager.getLogger(LSDLoginPage.class.getName());
	
	private final By USERNAME_FIELD = By.id("username");
	private final By PASSWORD_FIELD = By.id("password");
	private final By LOGIN_BUTTON = By.id("login_submit_button");
	protected RFWebsiteDriver driver;
	public LSDLoginPage(RFWebsiteDriver driver) {
		super(driver);
	}
	
	public LSDBasePage login(String sUser, String sPassword) {
		driver.type(USERNAME_FIELD,sUser);
		driver.type(PASSWORD_FIELD,sPassword);
		driver.click(LOGIN_BUTTON);
		
		return new LSDBasePage(driver);
	}
	
	
}
