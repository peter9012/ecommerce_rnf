package com.rf.pages.website.rehabitat.storeFront;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

public class StoreFrontHomePage extends StoreFrontWebsiteBasePage{

	public StoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontHomePage.class.getName());

	private final By ENROLL_NOW_BUTTON_LOC = By.xpath("//a[text()='Enroll Now']");

	public boolean isFindAConsultantPagePresent(){
		String findAConsultantURL = "/find-consultant";
		return driver.isElementPresent(SPONSOR_SEARCH_FIELD_LOC)&& driver.getCurrentUrl().contains(findAConsultantURL);
	}

	/***
	 * This method click on enroll now button
	 * 
	 * @param 
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickEnrollNowButton(){
		driver.click(ENROLL_NOW_BUTTON_LOC);
		logger.info("clicked on 'Enroll now button'");
		return this;
	}


}

