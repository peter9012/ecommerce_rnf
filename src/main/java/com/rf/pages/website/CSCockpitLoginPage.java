package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.RFBasePage;

public class CSCockpitLoginPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitLoginPage.class.getName());
	
	protected RFWebsiteDriver driver;
	
	private static final By LOGIN_BTN = By.xpath("//td[text()='Login']");
	private static final By USERNAME_TXT_FIELD = By.name("j_username");
		
	public CSCockpitLoginPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}	
	
	public void enterUsername(String userName){
		driver.type(USERNAME_TXT_FIELD, userName);
	}

	public CSCockpitHomePage clickLoginBtn(){
		driver.waitForElementPresent(LOGIN_BTN);
		driver.click(LOGIN_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return new CSCockpitHomePage(driver);
	}
}
