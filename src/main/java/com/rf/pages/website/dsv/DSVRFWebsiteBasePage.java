package com.rf.pages.website.dsv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.RFBasePage;


public class DSVRFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(DSVRFWebsiteBasePage.class.getName());
	protected RFWebsiteDriver driver;

	public DSVRFWebsiteBasePage(RFWebsiteDriver driver){		
		super(driver);
		this.driver = driver;
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
		driver.pauseExecutionFor(3000);
		logger.info("URL opened is "+URL);
	}
	
	public String convertHTTPS_To_HTTP(String URL){
		if(URL.contains("https")){
			URL=URL.replaceAll("https", "http");
		}	
		return URL;
	}


}