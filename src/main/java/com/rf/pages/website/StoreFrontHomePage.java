package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;


public class StoreFrontHomePage extends RFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(StoreFrontHomePage.class.getName());

	private final By BUSINESS_LINK_LOC = By.cssSelector("a[id='corp-opp']"); 
	private final By ENROLL_NOW_LINK_LOC = By.cssSelector("a[title='Enroll Now']");	
	private final By LOGIN_LINK_LOC = By.cssSelector("li[id='log-in-button']>a");
	private final By LOGIN_BTN_LOC = By.cssSelector("input[value='Log in']");
	private final By USERNAME_TXTFLD_LOC = By.id("username");
	private final By PASSWORD_TXTFLD_LOC = By.id("password");


	public StoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public void clickOnOurBusinessLink(){
		driver.waitForElementPresent(BUSINESS_LINK_LOC);
		driver.findElement(BUSINESS_LINK_LOC).click();
	}

	public StoreFrontEnrollNowPage clickOnOurEnrollNowLink(){
		driver.findElement(ENROLL_NOW_LINK_LOC).click();
		return new StoreFrontEnrollNowPage(driver);
	}

	public StoreFrontConsultantPage loginAsConsultant(String username,String password){
		try{
			driver.waitForElementPresent(LOGIN_LINK_LOC);
			driver.click(LOGIN_LINK_LOC);
			logger.info("login link clicked");
		}catch(Exception e){
			logger.info("login linked not present");
		}
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is: "+username);
		logger.info("login password is: "+password);
		driver.click(LOGIN_BTN_LOC);	
		logger.info("login button clicked");
		return new StoreFrontConsultantPage(driver);
	}

	public StoreFrontRCUserPage loginAsRCUser(String username,String password){
		try{
			driver.waitForElementPresent(LOGIN_LINK_LOC);
			driver.click(LOGIN_LINK_LOC);
			logger.info("login link clicked");
		}catch(Exception e){
			logger.info("login linked not present");
		}
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is "+username);
		logger.info("login password is "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		return new StoreFrontRCUserPage(driver);
	}
	public StoreFrontPCUserPage loginAsPCUser(String username,String password){
		try{
			driver.waitForElementPresent(LOGIN_LINK_LOC);
			driver.click(LOGIN_LINK_LOC);		
			logger.info("login link clicked");
		}catch(Exception e){
			logger.info("login linked not present");
		}
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);		
		logger.info("login username is "+username);
		logger.info("login password is "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		return new StoreFrontPCUserPage(driver);
	}

	public void openConsultantPWS(String pwsURL){
		logger.info("User PWS is "+pwsURL);
		driver.get(pwsURL);		
	}

	public boolean isCurrentURLShowsError() throws InterruptedException{
		Thread.sleep(5000);
		logger.info("Curremt URL is "+driver.getCurrentUrl());
		return driver.getCurrentUrl().contains("login?error=true");
	}

}
