package com.rf.pages.website.rehabitat.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

public class StoreFrontAutoshipStatusPage extends StoreFrontWebsiteBasePage{

	public StoreFrontAutoshipStatusPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontAutoshipStatusPage.class.getName());

	private final By CLOSE_ICON_PULSE_OVERLAY_LOC = By.id("cboxClose");
	private final By LEARN_MORE_ABOUT_PULSE_LOC = By.xpath("//a[contains(text(),'Learn more about Pulse')]");
	private final By PULSE_POPUP_LOC = By.xpath("//h1[contains(text(),'Pulse Business Management')]");
	private final By PC_PERKS_AUTOSHIP_STATUS_LOC = By.xpath("//div[contains(@class,'pcPerks-statusPage')]/div[contains(text(),'PC Perks Status')]");
	private final By DELAY_OR_CANCEL_PC_PERKS_LOC = By.xpath("//a[text()='Delay or Cancel PC Perks']");
	private final By DELAY_OR_CANCEL_PC_PERKS_POPUP_LOC = By.xpath("//h2[text()='Delay or Cancel PC Perks']");
	private final By CANCEL_OPTION_ON_CANCEL_PC_PERKS_POPUP_LOC = By.xpath("//h2[text()='Delay or Cancel PC Perks']");
	private final By DELAY_OPTION_ON_CANCEL_PC_PERKS_POPUP_LOC = By.xpath("//h2[text()='Delay or Cancel PC Perks']");
	private final By REASON_DD_LOC = By.id("//select[@id='code']");
	private final By REASON_DD_VALUE_OTHER_LOC = By.id("//select[@id='code']/option[text()='Other']");
	private final By MESSAGE_BOX_LOC = By.id("reasonMessage");
	private final By SEND_EMAIL_TO_CANCEL_ACCOUNT_BUTTON_LOC = By.xpath("//form[@id='pcperkscancellationform']/descendant::button[text()='SEND E-MAIL TO CANCEL ACCOUNT']");
	private final By ACCOUNT_TERMINATION_CONFIRMATION_POPUP_LOC = By.xpath("//div[@class='modal-content']/descendant::h2[text()='CONFIRM ACCOUNT TERMINATION']");
	private final By POPUP_CONFIRM_TERMINATION_BUTTON = By.id("cancel-pc-perks-confirm");
	private String socialMediaIconLoc = "//div[@class='container']//a[contains(@href,'%s')]";

	/***
	 * This method click on learn more about pulse link.
	 * 
	 * @param 
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage clickLearnMoreAboutPulse(){
		driver.click(LEARN_MORE_ABOUT_PULSE_LOC);
		logger.info("clicked on 'learn more about pulse' link");
		return this;
	}

	/***
	 * This method validates overlay and its content displayed when clicked on learn more about pulse link
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isPulsePopupPresentWithContent(){
		return driver.isElementPresent(PULSE_POPUP_LOC);

	}

	/***
	 * This method close pulse overlay
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage closePulsePopup(){
		driver.click(CLOSE_ICON_PULSE_OVERLAY_LOC);
		logger.info("clicked X of 'pulse overlay'");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method validates PC Autoship status page
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isPCPerksAutoshipStatusPagePresent(){
		return driver.isElementVisible(PC_PERKS_AUTOSHIP_STATUS_LOC);
	}
	/***
	 * This method close pulse overlay
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage delayOrCancelPCPerks(){
		driver.click(DELAY_OR_CANCEL_PC_PERKS_LOC);
		logger.info("clicked 'Delay or cancel PC Perks'");
		driver.pauseExecutionFor(2000);
		return this;
	}
	/***
	 * This method validates Delay or cancel pc perks popup
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isDelayOrCancelPCPerksPopupPresent(){
		return driver.isElementVisible(DELAY_OR_CANCEL_PC_PERKS_POPUP_LOC);
	}
	/***
	 * This method clicks on cancel on delay or cancel popup to cancel pc perks.
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage selectCancelPCPerksOnPopup(){
		driver.click(CANCEL_OPTION_ON_CANCEL_PC_PERKS_POPUP_LOC);
		logger.info("clicked 'Cancel PC perks' on delay or cancel popup.");
		driver.pauseExecutionFor(2000);
		return this;
	}
	/***
	 * This method fill all required details and click send an email to cancel account
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage fillTheEntriesAndClickOnSubmitDuringTermination(){
		driver.click(REASON_DD_LOC);
		driver.click(REASON_DD_VALUE_OTHER_LOC);
		driver.type(MESSAGE_BOX_LOC, "I want to terminate my account");
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SEND_EMAIL_TO_CANCEL_ACCOUNT_BUTTON_LOC));
		driver.waitForLoadingImageToDisappear(); 
		return this;
	}
	/***
	 * This method validates account termination confirmation popup
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean verifyAccountTerminationIsConfirmedPopup(){
		if(driver.isElementVisible(ACCOUNT_TERMINATION_CONFIRMATION_POPUP_LOC)){
			return true;
		}else{
			return false;
		}
	}
	public void clickOnConfirmTerminationPopup(){
		driver.quickWaitForElementPresent(POPUP_CONFIRM_TERMINATION_BUTTON);
		driver.click(POPUP_CONFIRM_TERMINATION_BUTTON);
		logger.info("Confirm popup clicked");
		driver.waitForPageLoad();
	}

}