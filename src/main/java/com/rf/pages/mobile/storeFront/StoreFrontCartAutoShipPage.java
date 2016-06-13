package com.rf.pages.mobile.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.mobile.RFMobileDriver;
import com.rf.core.driver.website.RFWebsiteDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontCartAutoShipPage extends StoreFrontRFMobileBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontCartAutoShipPage.class.getName());


	private final By UPDATE_MORE_INFO_LINK_LOC = By.xpath("//input[@value='Update more info']");
	
	public StoreFrontCartAutoShipPage(RFMobileDriver driver) {
		super(driver);		
	}


	public StoreFrontUpdateCartPage clickUpdateMoreInfoLink() throws InterruptedException{
		driver.waitForElementPresent(UPDATE_MORE_INFO_LINK_LOC);
		driver.click(UPDATE_MORE_INFO_LINK_LOC);
		logger.info("Update More Info Link Clicked "+UPDATE_MORE_INFO_LINK_LOC);
		driver.waitForLoadingImageToDisappear();
		return new StoreFrontUpdateCartPage(driver);
	}	
}
