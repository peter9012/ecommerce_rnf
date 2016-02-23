package com.rf.pages.website.cscockpit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;

public class CSCockpitCartTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitCartTabPage.class.getName());

	protected RFWebsiteDriver driver;
	private static final By PRODUCT_NOT_AVAILABLE_FOR_CRP_AUTOSHIP = By.xpath("//td[@class='z-button-cm'][text()='OK']");

	public CSCockpitCartTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}	

	public boolean verifyProductNotAvailablePopUp(){
		driver.quickWaitForElementPresent(PRODUCT_NOT_AVAILABLE_FOR_CRP_AUTOSHIP);
		return driver.isElementPresent(PRODUCT_NOT_AVAILABLE_FOR_CRP_AUTOSHIP);
	}

}