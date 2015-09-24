package com.rf.pages.website;

import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StoreFrontRCUserPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontRCUserPage.class.getName());

	private final By WELCOME_USER_LOC = By.xpath("//a[contains(text(),'Welcome')]");
	//private final By WELCOME_USER_DD_LOC = By.cssSelector("li[id='account-info-button']");
	private final By WELCOME_DD_SHIPPING_INFO_LINK_LOC = By.linkText("Shipping Info");
	private final By WELCOME_DD_ACCOUNT_INFO_LOC = By.xpath("//a[text()='Account Info']");

	public StoreFrontRCUserPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public boolean verifyRCUserPage(String username) throws InterruptedException{
		driver.waitForElementPresent(WELCOME_USER_LOC);
		return driver.isElementPresent(WELCOME_USER_LOC);

	}

//	public void clickOnWelcomeDropDown() throws InterruptedException{	
//		driver.waitForElementPresent(WELCOME_USER_DD_LOC);
//		driver.click(WELCOME_USER_DD_LOC);
//		logger.info("welcome drop down clicked");
//	}

	public boolean isLinkPresentOnWelcomeDropDown(String link){
		return driver.isElementPresent(By.linkText(link));
	}

	public StoreFrontShippingInfoPage clickShoppingLinkPresentOnWelcomeDropDown(){
		driver.waitForElementPresent(WELCOME_DD_SHIPPING_INFO_LINK_LOC);
		driver.click(WELCOME_DD_SHIPPING_INFO_LINK_LOC);
		logger.info("Shipping link from welcome drop down clicked");
		return new StoreFrontShippingInfoPage(driver);
	}

	
	public StoreFrontAccountInfoPage clickAccountInfoLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_ACCOUNT_INFO_LOC);
		driver.click(WELCOME_DD_ACCOUNT_INFO_LOC);
		logger.info("Account info link from welcome drop down clicked");
		return new StoreFrontAccountInfoPage(driver);
	}
	

}
