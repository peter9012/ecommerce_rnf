package com.rf.pages.website.cscockpit;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;

public class CSCockpitAutoshipSearchTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitAutoshipSearchTabPage.class.getName());
	private static final By AUTOSHIP_TEMPLATE = By.xpath("//span[contains(text(),'Autoship Template #')]");

	protected RFWebsiteDriver driver;

	public CSCockpitAutoshipSearchTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean isAutoshipTemplateDisplayedInAutoshipTemplateTab(){
		driver.isElementPresent(AUTOSHIP_TEMPLATE);
		return driver.isElementPresent(AUTOSHIP_TEMPLATE);  
	}

}