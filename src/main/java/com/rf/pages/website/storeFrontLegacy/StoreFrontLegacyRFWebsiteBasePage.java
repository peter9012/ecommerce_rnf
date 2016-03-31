package com.rf.pages.website.storeFrontLegacy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rf.core.driver.website.RFWebsiteDriver;

import com.rf.pages.RFBasePage;



public class StoreFrontLegacyRFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontLegacyRFWebsiteBasePage.class.getName());

	protected RFWebsiteDriver driver;
	private String RFL_DB = null;
	public StoreFrontLegacyRFWebsiteBasePage(RFWebsiteDriver driver){		
		super(driver);
		this.driver = driver;
	}

	

}