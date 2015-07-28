package com.rf.pages.website;

import org.openqa.selenium.By;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontOrdersAutoshipStatusPage extends RFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(StoreFrontOrdersAutoshipStatusPage.class.getName());

	private final By AUTOSHIP_PAGE_HEADER_LOC = By.xpath("//div[@id='main-content']//div[@class='gray-container-info-top']");
	private final By AUTOSHIP_CRP_STATUS_LOC = By.xpath("//p[@id='crp-status']");
	private final By AUTOSHIP_PULSE_STATUS_LOC = By.xpath("//p[@id='pulse-status']");


	public StoreFrontOrdersAutoshipStatusPage(RFWebsiteDriver driver) {
		super(driver);
	}


	public boolean verifyAutoShipStatusHeader(){
		driver.waitForElementPresent(AUTOSHIP_PAGE_HEADER_LOC);
		String autoShipStatusHeaderText = driver.findElement(AUTOSHIP_PAGE_HEADER_LOC).getText();
		if(autoShipStatusHeaderText.contains("AUTOSHIP STATUS")){
			return true;
		}
		return false;
	}

	public boolean verifyAutoShipCRPStatus(){
		driver.waitForElementPresent(AUTOSHIP_CRP_STATUS_LOC);
		String autoShipCRPStatusText = driver.findElement(AUTOSHIP_CRP_STATUS_LOC).getText();
		if(autoShipCRPStatusText.contains("Current CRP Status: Enrolled")){
			return true;
		}
		return false;
	}

	public boolean verifyAutoShipPulseSubscriptionStatus(){
		driver.waitForElementPresent(AUTOSHIP_PULSE_STATUS_LOC);
		String autoShipPulseStatusText = driver.findElement(AUTOSHIP_PULSE_STATUS_LOC).getText();
		if(autoShipPulseStatusText.contains("Current Subscription status: Enrolled")){
			return true;
		}
		return false;
	}

}
