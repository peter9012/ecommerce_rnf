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

	public CSCockpitCartTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}	

}