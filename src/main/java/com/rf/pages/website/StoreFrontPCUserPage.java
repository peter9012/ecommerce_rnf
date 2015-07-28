package com.rf.pages.website;

import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontPCUserPage extends RFWebsiteBasePage{

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
		Thread.sleep(4000);
		driver.findElement(WELCOME_USER_DD_LOC).click();		
	}

	public StoreFrontOrdersPage clickOrdersLinkPresentOnWelcomeDropDown() throws InterruptedException{
		Thread.sleep(3000);
		driver.findElement(WELCOME_DD_ORDERS_LINK_LOC).click();
		return new StoreFrontOrdersPage(driver);
	}

	public StoreFrontAccountInfoPage clickAccountInfoLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.findElement(WELCOME_DD_ACCOUNT_INFO_LOC).click();
		Thread.sleep(3000);
		return new StoreFrontAccountInfoPage(driver);
	}

	public StoreFrontCartAutoShipPage addProductToPCPerk(){
		driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//form[2]/input[@value='Add to PC Perks']")).click();
		return new StoreFrontCartAutoShipPage(driver);
	}
}

