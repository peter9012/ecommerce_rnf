package com.rf.pages.website;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;


public class StoreFrontConsultantPage extends RFWebsiteBasePage{

	private final By WELCOME_USER_LOC = By.xpath("//a[contains(text(),'Welcome')]");
	private final By WELCOME_USER_DD_LOC = By.cssSelector("li[id='account-info-button']"); 
	private final By WELCOME_DD_SHIPPING_INFO_LINK_LOC = By.linkText("Shipping Info");
	private final By WELCOME_DD_ORDERS_LINK_LOC = By.xpath("//div[@id='account-info']//a[text()='Orders']");
	private final By WELCOME_DD_BILLING_INFO_LINK_LOC = By.linkText("Billing Info");
	private final By LEFT_PANE_TEXT_LOC = By.xpath("//p[@class='left']"); 
	private final By WELCOME_DD_ACCOUNT_INFO_LOC = By.xpath("//a[text()='Account Info']");
	private final By NEXT_CRP_IMG_LOC = By.xpath("//div[contains(text(),'Next CRP')]");

	public StoreFrontConsultantPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	// to do
	public boolean verifyConsultantPage(){
		//		driver.waitForElementPresent(WELCOME_USER_LOC);
		//		return driver.findElement(LEFT_PANE_TEXT_LOC).getText().contains("R + F Independent Consultant");
		return true;
	}

	public void clickOnWelcomeDropDown() throws InterruptedException{		
		driver.findElement(WELCOME_USER_DD_LOC).click();
		Thread.sleep(3000);
	}

	public boolean isLinkPresentOnWelcomeDropDown(String link){
		return driver.isElementPresent(By.linkText(link));
	}

	public StoreFrontShippingInfoPage clickShippingLinkPresentOnWelcomeDropDown(){
		driver.findElement(WELCOME_DD_SHIPPING_INFO_LINK_LOC).click();
		return new StoreFrontShippingInfoPage(driver);
	}

	public StoreFrontOrdersPage clickOrdersLinkPresentOnWelcomeDropDown() throws InterruptedException{
		Thread.sleep(3000);
		driver.findElement(WELCOME_DD_ORDERS_LINK_LOC).click();
		return new StoreFrontOrdersPage(driver);
	}

	public StoreFrontBillingInfoPage clickBillingInfoLinkPresentOnWelcomeDropDown(){
		driver.findElement(WELCOME_DD_BILLING_INFO_LINK_LOC).click();
		return new StoreFrontBillingInfoPage(driver);
	}

	public String getCurrentURL(){
		return driver.getCurrentUrl();
	}

	public StoreFrontAccountInfoPage clickAccountInfoLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.findElement(WELCOME_DD_ACCOUNT_INFO_LOC).click();
		Thread.sleep(3000);
		return new StoreFrontAccountInfoPage(driver);
	}

	public StoreFrontCartAutoShipPage clickNextCRP(){
		driver.click(NEXT_CRP_IMG_LOC);
		return new StoreFrontCartAutoShipPage(driver);
	}

	public StoreFrontCartAutoShipPage addProductToCRP(){
		driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//form[2]/input[@value='Add to crp']")).click();
		return new StoreFrontCartAutoShipPage(driver);
	} 

}

