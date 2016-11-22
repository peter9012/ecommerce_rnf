package com.rf.pages.website.rehabitat.storeFront;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

public class StoreFrontHomePage extends StoreFrontWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontHomePage.class.getName());


	public StoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public boolean isFindAConsultantPagePresent(){
		String findAConsultantURL = "/find-consultant";
		return driver.isElementPresent(SPONSOR_SEARCH_FIELD_LOC)&& driver.getCurrentUrl().contains(findAConsultantURL);
	}

}

