package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.test.website.storeFront.account.AddShippingTest;


public class StoreFrontConsultantPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontConsultantPage.class.getName());


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

	
	public boolean verifyConsultantPage() throws InterruptedException{
		Thread.sleep(5000);
		driver.waitForElementPresent(By.xpath("//li[@id='account-info-button']/a"));
		return driver.findElement(By.xpath("//li[@id='account-info-button']/a")).getText().contains("Welcome");		
	}

	public void clickOnWelcomeDropDown() throws InterruptedException{
		Thread.sleep(3000);
		driver.findElement(WELCOME_USER_DD_LOC).click();
		Thread.sleep(2000);
		logger.info("User has clicked on welcome drop down");
	}

	public boolean isLinkPresentOnWelcomeDropDown(String link){
		return driver.isElementPresent(By.linkText(link));
	}

	public StoreFrontShippingInfoPage clickShippingLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.findElement(WELCOME_DD_SHIPPING_INFO_LINK_LOC).click();
		Thread.sleep(3000);
		logger.info("User has clicked on shipping link from welcome drop down");
		return new StoreFrontShippingInfoPage(driver);
	}

	public StoreFrontOrdersPage clickOrdersLinkPresentOnWelcomeDropDown() throws InterruptedException{
		Thread.sleep(3000);
		driver.findElement(WELCOME_DD_ORDERS_LINK_LOC).click();
		logger.info("User has clicked on orders link from welcome drop down");
		return new StoreFrontOrdersPage(driver);
	}

	public StoreFrontBillingInfoPage clickBillingInfoLinkPresentOnWelcomeDropDown(){
		driver.findElement(WELCOME_DD_BILLING_INFO_LINK_LOC).click();
		logger.info("User has clicked on billing link from welcome drop down");
		return new StoreFrontBillingInfoPage(driver);
	}

	public String getCurrentURL(){
		return driver.getCurrentUrl();
	}

	public StoreFrontAccountInfoPage clickAccountInfoLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.findElement(WELCOME_DD_ACCOUNT_INFO_LOC).click();
		Thread.sleep(3000);
		logger.info("User has clicked on account link from welcome drop down");
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

