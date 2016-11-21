package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontHomePage extends StoreFrontWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontHomePage.class.getName());

	
	public StoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	
		
}

