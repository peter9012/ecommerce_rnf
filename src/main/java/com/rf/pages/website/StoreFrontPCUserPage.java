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
	private final By YOUR_ACCOUNT_DROPDOWN_LOC = By.xpath("//div[@id='left-menu']//div/button[contains(text(),'Your Account')]");

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
		try{
			driver.quickWaitForElementPresent(By.xpath("//div[contains(@class,'blue')]/div[2]/div[1]//input[contains(@value,'PC Perks')]"));
			driver.click(By.xpath("//div[contains(@class,'blue')]/div[2]/div[1]//input[contains(@value,'PC Perks')]"));
		}catch(Exception e){
			try{
				driver.click(By.xpath("//div[contains(@class,'blue')]/div[2]/div[2]//input[contains(@value,'PC Perks')]"));
			}catch(Exception e1){
				driver.click(By.xpath("//div[contains(@class,'blue')]/div[2]/div[3]//input[contains(@value,'PC Perks')]"));
			}
		}

		logger.info("Add Product to PC Perk button clicked");

		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}

		return new StoreFrontCartAutoShipPage(driver);
	}

	public void clickOnPCPerksStatus(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'PC Perks Status')]"));
		driver.click(By.xpath("//a[contains(text(),'PC Perks Status')]"));
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
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='Close window']"));
			driver.click(By.xpath("//input[@value='Close window']"));
		}catch(Exception e){			
			driver.click(By.xpath("//div[@id='popup-content']/div/div/following::input[@value='Close window']"));
		}
		driver.waitForPageLoad();
		return new StoreFrontHomePage(driver);
	}

	public boolean validateNextPCPerksMiniCart() {  
		return driver.findElement(By.xpath("//li[@id='mini-shopping-special-button']")).isDisplayed();
	}

	public void clickChangeMyAutoshipDateButton(){
		driver.waitForElementPresent(By.id("change-autoship-button"));
		driver.findElement(By.id("change-autoship-button")).click();
	}

	public void selectSecondAutoshipDateAndClickSave(){
		driver.waitForElementPresent(By.xpath("//ul[@id='autoship-date']//li[2]/span[1]"));
		driver.click(By.xpath("//ul[@id='autoship-date']//li[2]/span[1]"));
		logger.info("pc perks delayed date selected");
		driver.waitForElementPresent(By.xpath("//ul[@id='autoship-date']//input[@value='save']"));
		driver.click(By.xpath("//ul[@id='autoship-date']//input[@value='save']"));
		logger.info("save button clicked after different date selected");
		driver.waitForLoadingImageToDisappear();

	}

	public String getAccountterminationReasonTooMuchProduct(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[2]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[2]")).getText();
	}

	public String getAccountterminationReasonTooExpensive(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[3]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[3]")).getText();
	}
	public String getAccountterminationReasonEnrolledInAutoShipProgram(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[4]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[4]")).getText();
	}
	public String getAccountterminationReasonProductNotRight(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[5]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[5]")).getText();
	}
	public String getAccountterminationReasonUpgradingToConsultant(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[6]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[6]")).getText();
	}
	public String getAccountterminationReasonReceiveProductTooOften(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[7]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[7]")).getText();
	}
	public String getAccountterminationReasonDoNotWantToObligated(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[8]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[8]")).getText();
	}
	public String getAccountterminationReasonOther(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[9]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[9]")).getText();
	}

	public boolean verifyToSectionInSendcancellationMessageSection(){
		driver.quickWaitForElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[1]/div[2]"));
		return driver.isElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[1]/div[2]"));
	}
	public boolean verifySubjectSectionInSendcancellationMessageSection(){
		driver.quickWaitForElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[2]/div[2]"));
		return driver.isElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[2]/div[2]"));
	}

	public boolean verifyMessageSectionInSendcancellationMessageSection(){
		driver.quickWaitForElementPresent(By.xpath("//textarea[@id='terminationComments']"));
		return driver.isElementPresent(By.xpath("//textarea[@id='terminationComments']"));
	}

	public boolean verifyIHaveChangedMyMindButton(){
		driver.quickWaitForElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[4]/div[2]/input[1]"));
		return driver.isElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[4]/div[2]/input[1]"));
	}

	public boolean verifySendAnEmailToCancelAccountButton(){
		driver.quickWaitForElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[4]/div[2]/input[2]"));
		return driver.isElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[4]/div[2]/input[2]"));
	}

	public boolean validatePCPerkCartDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='bag-special']/span"));
		return driver.isElementPresent(By.xpath("//div[@id='bag-special']/span"));
	}
}

