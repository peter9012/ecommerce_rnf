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
	private static final By CLOSE_BTN_OF_ADD_A_NEW_PAYMENT_PROFILE_POPUP = By.xpath("//div[contains(text(),'ADD NEW PAYMENT PROFILE')]/div");
	private static final By TOTAL_NO_PRODUCT = By.xpath("//div[@class='csListboxContainer']//td[contains(@class,'csCartLine')]");

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

	public void clickCloseOfAddANewPaymentProfilePopup(){
		driver.waitForElementPresent(CLOSE_BTN_OF_ADD_A_NEW_PAYMENT_PROFILE_POPUP);
		driver.click(CLOSE_BTN_OF_ADD_A_NEW_PAYMENT_PROFILE_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public int getCountOfProduct(){
		driver.waitForElementPresent(TOTAL_NO_PRODUCT);
		return driver.findElements(TOTAL_NO_PRODUCT).size();
	}

}