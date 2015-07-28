package com.rf.pages.website;

import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;


public class StoreFrontRCUserPage extends RFWebsiteBasePage{
	private final By WELCOME_USER_LOC = By.xpath("//a[contains(text(),'Welcome')]");
	private final By WELCOME_USER_DD_LOC = By.cssSelector("li[id='account-info-button']");
	private final By WELCOME_DD_SHIPPING_INFO_LINK_LOC = By.linkText("Shipping Info");
	private final By WELCOME_DD_ORDERS_LINK_LOC = By.linkText("Orders");
	private final By WELCOME_DD_ACCOUNT_INFO_LOC = By.xpath("//a[text()='Account Info']");
	
	public StoreFrontRCUserPage(RFWebsiteDriver driver) {
		super(driver);		
	}
	
	public boolean verifyRCUserPage(String username) throws InterruptedException{
		Thread.sleep(2000);
		driver.waitForElementPresent(WELCOME_USER_LOC);
		return driver.findElement(By.xpath("//a[contains(text(),'Welcome "+username+"')]")).isDisplayed();
	}

	public void clickOnWelcomeDropDown() throws InterruptedException{	
		Thread.sleep(5000);
		driver.waitForElementPresent(WELCOME_USER_DD_LOC);
		driver.findElement(WELCOME_USER_DD_LOC).click();
	}
	
	public boolean isLinkPresentOnWelcomeDropDown(String link){
		return driver.isElementPresent(By.linkText(link));
	}
	
	public StoreFrontShippingInfoPage clickShoppingLinkPresentOnWelcomeDropDown(){
		driver.findElement(WELCOME_DD_SHIPPING_INFO_LINK_LOC).click();
		return new StoreFrontShippingInfoPage(driver);
	}
	
	public StoreFrontOrdersPage clickOrdersLinkPresentOnWelcomeDropDown(){
		driver.findElement(WELCOME_DD_ORDERS_LINK_LOC).click();
		return new StoreFrontOrdersPage(driver);
	}
	
	public StoreFrontAccountInfoPage clickAccountInfoLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.findElement(WELCOME_DD_ACCOUNT_INFO_LOC).click();
		Thread.sleep(3000);
		return new StoreFrontAccountInfoPage(driver);
	}

}
