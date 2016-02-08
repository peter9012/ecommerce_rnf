package com.rf.pages.website.cscockpit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class CSCockpitCommitTaxTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitCommitTaxTabPage.class.getName());
	private static String ddValuesLoc = "//div[@class='z-combobox-pp'][contains(@style,'display: block')]//tr[contains(@class,'z-combo-item')][@z.label='%s']";

	private static final By ORDER_NUMBER_SEARCH_TXT_FIELD = By.xpath("//span[text()='ENTER ORDER NUMBERS']/following::textarea");
	private static final By COMMIT_TAX_BUTTON = By.xpath("//td[text()='Commit Tax']");
	private static final By CONFIRMATION_POPUP_OKAY = By.xpath("//td[text()='Yes']");
	private static final By SUCCESSFUL_MESSAGE_POPUP_OKAY = By.xpath("//td[text()='OK']");

	protected RFWebsiteDriver driver;

	public CSCockpitCommitTaxTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public void enterOrderNumberInCommitTaxTab(String orderNumber){
		driver.waitForElementPresent(ORDER_NUMBER_SEARCH_TXT_FIELD);
		driver.type(ORDER_NUMBER_SEARCH_TXT_FIELD, orderNumber);
		driver.click(COMMIT_TAX_BUTTON);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyConfirmationPopup(){
		driver.waitForElementPresent(CONFIRMATION_POPUP_OKAY);
		return driver.isElementPresent(CONFIRMATION_POPUP_OKAY);
	}

	public void clickConfirmationPopupOkayInCommitTaxTab(){
		driver.waitForElementPresent(CONFIRMATION_POPUP_OKAY);
		driver.click(CONFIRMATION_POPUP_OKAY);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickSuccessfulCommittedTaxPopupOkay(){
		driver.waitForElementPresent(SUCCESSFUL_MESSAGE_POPUP_OKAY);
		driver.click(SUCCESSFUL_MESSAGE_POPUP_OKAY);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getCommitTaxIdOfOrderInCommitTaxTab(){
		driver.waitForElementPresent(SUCCESSFUL_MESSAGE_POPUP_OKAY);
		String commitTaxId=driver.findElement(SUCCESSFUL_MESSAGE_POPUP_OKAY).getText();
		logger.info("commit tax id is "+commitTaxId);
		return commitTaxId;
	}

}