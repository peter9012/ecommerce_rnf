package com.rf.pages.website.storeFrontLegacy;

import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select;
import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontLegacyPCUserPage extends StoreFrontLegacyRFWebsiteBasePage{

	private static String consultantOnlyProduct= "//p[contains(text(),'%s')]/preceding::a[1]/img";

	public StoreFrontLegacyPCUserPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontLegacyPCUserPage.class.getName());
	private static final By DELAY_OR_CANCEL_PC_PERKS = By.xpath("//a[@id='PcCancellLink']");
	private static final By PC_CANCELLATION_POPUP = By.xpath("//div[@id='PcCancellationDialog']");
	private static final By CANCEL_MY_PC_PERKS_ACCOUNT_BTN = By.xpath("//a[@id='BtnCancelPCPerks']");
	private static final By CHANGED_MY_MIND_BTN = By.xpath("//a[@id='BtnChangedMyMind']");
	private static final By SELECT_REASON_DD_FOR_PC_TERMINATION = By.xpath("//select[@id='CancellationReasonId']");
	private static final By REASON_FOR_PC_TERMINATION = By.xpath("//select[@id='CancellationReasonId']/option[2]");
	private static final By MESSAGE_BOX_FOR_PC_TERMINATION = By.xpath("//textarea[@id='EmailMessage']");
	private static final By SEND_EMAIL_TO_CANCEL_BTN = By.xpath("//a[@id='BtnSendEmail']");
	private static final By EMAIL_CONFIRMATION_MSG = By.xpath("//div[@id='RFContent']/p");
	private static final By INVALID_LOGIN = By.xpath("//p[@id='loginError']");
	private static final By UPDATE_ORDER_BTN = By.xpath("//div[@id='TotalBar']/following::input[@value='Update Order']");
	private static final By MY_ACCOUNT_LINK = By.xpath("//a[text()='My Account']");

	public boolean isDelayOrCancelPCPerksLinkPresent(){
		driver.waitForElementPresent(DELAY_OR_CANCEL_PC_PERKS);
		return driver.isElementPresent(DELAY_OR_CANCEL_PC_PERKS);
	}

	public boolean ispCPerksFAQsLinkRedirectingToAppropriatePage(String redirectedPageLink){
		driver.waitForPageLoad();
		Set<String> set=driver.getWindowHandles();
		Iterator<String> it=set.iterator();
		String parentWindow=it.next();
		String childWindow=it.next();
		driver.switchTo().window(childWindow);
		boolean status= driver.getCurrentUrl().contains(redirectedPageLink);
		driver.close();
		driver.switchTo().window(parentWindow);
		return status;
	}

	public void clickDelayOrCancelPCPerksLink(){
		driver.waitForElementPresent(DELAY_OR_CANCEL_PC_PERKS);
		driver.click(DELAY_OR_CANCEL_PC_PERKS);
		logger.info("Delay or cancel PC perks link clicked");
	}

	public boolean isDelayOrCancelPCPerksPopupPresent(){
		driver.waitForElementPresent(PC_CANCELLATION_POPUP);
		return driver.isElementPresent(PC_CANCELLATION_POPUP);
	}

	public void clickNoThanksPleaseCancelMyPCPerksAccountBtn(){
		driver.waitForElementPresent(CANCEL_MY_PC_PERKS_ACCOUNT_BTN);
		driver.click(CANCEL_MY_PC_PERKS_ACCOUNT_BTN);
		logger.info("No Thanks, Please cancel my pc perks account button clicked");
		driver.waitForPageLoad();
	}

	public void clickChangedMyMindBtn(){
		driver.waitForElementPresent(CHANGED_MY_MIND_BTN);
		driver.click(CHANGED_MY_MIND_BTN);
		logger.info("I changed my mind button clicked");
		driver.waitForPageLoad();
	}

	public void selectReasonForPCTermination(){
		driver.waitForElementPresent(SELECT_REASON_DD_FOR_PC_TERMINATION);
		driver.click(SELECT_REASON_DD_FOR_PC_TERMINATION);
		logger.info("select reason dropdown clicked for pc termination");
		driver.click(REASON_FOR_PC_TERMINATION);
		logger.info("Reason selected for pc termination");
		driver.waitForPageLoad();
	}

	public void enterMessageForPCTermination(){
		driver.waitForElementPresent(MESSAGE_BOX_FOR_PC_TERMINATION);
		driver.type(MESSAGE_BOX_FOR_PC_TERMINATION, "For Automation");
		logger.info("Message typed for pc trmination");
	}

	public void clickSendEmailToCancelBtn(){
		driver.waitForElementPresent(SEND_EMAIL_TO_CANCEL_BTN);
		driver.click(SEND_EMAIL_TO_CANCEL_BTN);
		logger.info("Send email to cancel button clicked");
		driver.waitForPageLoad();
	}

	public String getEmailConfirmationMsgAfterPCTermination(){
		driver.waitForElementPresent(EMAIL_CONFIRMATION_MSG);
		String emailConfirmationMsg = driver.findElement(EMAIL_CONFIRMATION_MSG).getText();
		logger.info("Email confirmation message is: "+emailConfirmationMsg);
		return emailConfirmationMsg;
	}

	public boolean isInvalidLoginPresent(){
		driver.waitForElementPresent(INVALID_LOGIN);
		return driver.isElementPresent(INVALID_LOGIN);
	}

	public void clickUpdateOrderBtn() {
		driver.waitForStorfrontLegacyLoadingImageToDisappear();
		driver.waitForElementPresent(UPDATE_ORDER_BTN);
		JavascriptExecutor js = (JavascriptExecutor)(RFWebsiteDriver.driver);
		js.executeScript("arguments[0].click();", driver.findElement(UPDATE_ORDER_BTN));
		logger.info("Update order button clicked");
	}

	public void clickMyAccountLink(){
		driver.waitForElementPresent(MY_ACCOUNT_LINK);
		driver.click(MY_ACCOUNT_LINK);
		logger.info("My Account link clicked");
		driver.waitForPageLoad();
	}

}