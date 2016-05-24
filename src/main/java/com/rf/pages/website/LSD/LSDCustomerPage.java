package com.rf.pages.website.LSD;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class LSDCustomerPage extends LSDRFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(LSDCustomerPage.class.getName());

	public LSDCustomerPage(RFWebsiteDriver driver) {
		super(driver);
	}

	private static final By CUSTOMERS_PAGE = By.xpath("//div[@class='customers']");

	public boolean isCustomerPagePresent(){
		driver.waitForElementPresent(CUSTOMERS_PAGE);
		return driver.isElementPresent(CUSTOMERS_PAGE);
	}
}