package com.rf.pages.website.storeFront;

import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontCartAutoShipPage extends StoreFrontRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontCartAutoShipPage.class.getName());


//	private final By UPDATE_MORE_INFO_LINK_LOC = By.xpath("//input[@value='Update more info']");
	private final By UPDATE_MORE_INFO_LINK_LOC = By.xpath("//a[contains(text(),'Update Shipping and Billing info')]");	
	
	public StoreFrontCartAutoShipPage(RFWebsiteDriver driver) {
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

