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
	//private final By WELCOME_USER_DD_LOC = By.cssSelector("li[id='account-info-button']"); 
	private final By WELCOME_DD_ORDERS_LINK_LOC = By.xpath("//div[@id='account-info']//a[text()='Orders']");
	private final By WELCOME_DD_ACCOUNT_INFO_LOC = By.xpath("//a[text()='Account Info']");

	public boolean verifyPCUserPage(){
		driver.waitForElementPresent(WELCOME_USER_LOC);
		return driver.isElementPresent(WELCOME_USER_LOC);		
	}

//	public void clickOnWelcomeDropDown() throws InterruptedException{	
//		driver.waitForElementPresent(WELCOME_USER_DD_LOC);
//		driver.click(WELCOME_USER_DD_LOC);	
//		logger.info("Welcome Drop down clicked "+WELCOME_USER_DD_LOC);
//	}

//	public StoreFrontOrdersPage clickOrdersLinkPresentOnWelcomeDropDown() throws InterruptedException{
//		driver.waitForElementPresent(WELCOME_DD_ORDERS_LINK_LOC);
//		driver.click(WELCOME_DD_ORDERS_LINK_LOC);
//		logger.info("Orders link from welcome drop down clicked "+WELCOME_DD_ORDERS_LINK_LOC);
//		return new StoreFrontOrdersPage(driver);
//	}

	public StoreFrontAccountInfoPage clickAccountInfoLinkPresentOnWelcomeDropDown() throws InterruptedException{
		logger.info(WELCOME_DD_ACCOUNT_INFO_LOC);
		driver.click(WELCOME_DD_ACCOUNT_INFO_LOC);
		logger.info("Account info linked from welcome drop down clicked");
		return new StoreFrontAccountInfoPage(driver);
	}

	public StoreFrontCartAutoShipPage addProductToPCPerk(){
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//input[contains(@value,'ADD')]"));
		driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//input[contains(@value,'ADD')]"));
		logger.info("Add Product to PC Perk button clicked "+"//div[@id='main-content']/div[5]/div[1]//input[contains(@value,'ADD')]");
		return new StoreFrontCartAutoShipPage(driver);
	}

	public void clickOnPCPerksStatus(){
		driver.waitForElementPresent(By.linkText("PC Perks Status"));
		driver.click(By.linkText("PC Perks Status"));
	}

	public void clickDelayOrCancelPCPerks(){
		driver.waitForElementPresent(By.xpath("//a[text()='Delay or Cancel PC Perks']"));
		driver.click(By.xpath("//a[text()='Delay or Cancel PC Perks']"));
	}

	public void clickPleaseCancelMyPcPerksActBtn(){
		driver.waitForElementPresent(By.id("cancel-pc-perks-button"));
		driver.click(By.id("cancel-pc-perks-button"));
	}

	public StoreFrontHomePage cancelMyPCPerksAct(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[6]"));
		driver.click(By.xpath("//select[@id='problemType']/option[6]"));
		driver.waitForElementPresent(By.xpath("//textarea[@id='terminationComments']"));
		driver.findElement(By.xpath("//textarea[@id='terminationComments']")).sendKeys("test");
		driver.click(By.xpath("//input[@id='pcperkTerminationConfirm']"));
		driver.waitForElementPresent(By.xpath("//input[@id='confirmpcTemrminate']"));
		driver.click(By.xpath("//input[@id='confirmpcTemrminate']"));
		driver.waitForLoadingImageToDisappear();
		driver.waitForElementPresent(By.xpath("//input[@value='Close window']"));
		driver.click(By.xpath("//input[@value='Close window']"));
		driver.waitForPageLoad();
		return new StoreFrontHomePage(driver);
	}

	public boolean validateNextPCPerksMiniCart() {  
		return driver.findElement(By.xpath("//li[@id='mini-shopping-special-button']")).isDisplayed();
	}
}

