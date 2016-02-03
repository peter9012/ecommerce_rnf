package com.rf.pages.website.cscockpit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class CSCockpitAutoshipTemplateUpdateTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitAutoshipTemplateUpdateTabPage.class.getName());

	private static String sortByDropDownLoc= "//div[@class='csResultsSortList']/select/option[text()='%s']";
	private static final By PRODUCT_NOT_AVAILABLE_POPUP_OK_BTN = By.xpath("//td[@class='z-button-cm'][text()='OK']");
	private static final By UPDATE_AUTOSHIP_TEMPLATE = By.xpath("//td[contains(text(),'Update Autoship Template')]");

	protected RFWebsiteDriver driver;

	public CSCockpitAutoshipTemplateUpdateTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}
	public void clickAddOrderNodeInAutoshipTemplateUpdateTab(){
		driver.waitForElementPresent(PRODUCT_NOT_AVAILABLE_POPUP_OK_BTN);
		driver.click(PRODUCT_NOT_AVAILABLE_POPUP_OK_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}
	public void clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab(){
		driver.waitForElementPresent(UPDATE_AUTOSHIP_TEMPLATE);
		driver.click(UPDATE_AUTOSHIP_TEMPLATE);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

}