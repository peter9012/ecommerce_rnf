package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.RFBasePage;

public class CSCockpitRFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitRFWebsiteBasePage.class.getName());
	protected RFWebsiteDriver driver;
	public CSCockpitRFWebsiteBasePage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	public String getBaseURL(){
		return driver.getURL();
	}
	
	public String getCurrentURL(){
		return driver.getCurrentUrl();
	}
	
	public void openURL(String URL){
		driver.get(URL);
		driver.waitForPageLoad();
	}
	
		
}
