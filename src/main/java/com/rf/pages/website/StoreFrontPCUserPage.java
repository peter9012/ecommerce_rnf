package com.rf.pages.website;

import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontPCUserPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontPCUserPage.class.getName());


	public StoreFrontPCUserPage(RFWebsiteDriver driver) {
		super(driver);
	}

	private final By WELCOME_USER_LOC = By.xpath("//a[contains(text(),'Welcome')]");
	private final By WELCOME_USER_DD_LOC = By.cssSelector("li[id='account-info-button']"); 
	private final By WELCOME_DD_ORDERS_LINK_LOC = By.xpath("//div[@id='account-info']//a[text()='Orders']");
	private final By LEFT_PANE_TEXT_LOC = By.xpath("//p[@class='left']"); 
	private final By WELCOME_DD_ACCOUNT_INFO_LOC = By.xpath("//a[text()='Account Info']");

	// to do
	public boolean verifyPCUserPage(){
//		driver.waitForElementPresent(WELCOME_USER_LOC);
//		return driver.findElement(LEFT_PANE_TEXT_LOC).getText().contains("R + F Independent Consultant");
		return true;
	}

	public void clickOnWelcomeDropDown() throws InterruptedException{	
		driver.waitForElementPresent(WELCOME_USER_DD_LOC);
		driver.findElement(WELCOME_USER_DD_LOC).click();	
		logger.info("Welcome Drop down clicked "+WELCOME_USER_DD_LOC);
	}

	public StoreFrontOrdersPage clickOrdersLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_ORDERS_LINK_LOC);
		driver.findElement(WELCOME_DD_ORDERS_LINK_LOC).click();
		logger.info("Orders link from welcome drop down clicked "+WELCOME_DD_ORDERS_LINK_LOC);
		return new StoreFrontOrdersPage(driver);
	}

	public StoreFrontAccountInfoPage clickAccountInfoLinkPresentOnWelcomeDropDown() throws InterruptedException{
		logger.info(WELCOME_DD_ACCOUNT_INFO_LOC);
		driver.findElement(WELCOME_DD_ACCOUNT_INFO_LOC).click();
		logger.info("Account info linked from welcome drop down clicked");
		return new StoreFrontAccountInfoPage(driver);
	}

	public StoreFrontCartAutoShipPage addProductToPCPerk(){
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//form[2]/input[@value='Add to PC Perks']"));
		driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//form[2]/input[@value='Add to PC Perks']")).click();
		logger.info("Add Product to PC Perk button clicked "+"//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//form[2]/input[@value='Add to PC Perks']");
		return new StoreFrontCartAutoShipPage(driver);
	}
}

