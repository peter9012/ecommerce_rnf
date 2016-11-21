package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontConsultantEnrollNowPage extends StoreFrontWebsiteBasePage{
	public StoreFrontConsultantEnrollNowPage(RFWebsiteDriver driver) {
		super(driver);		
	}
	
	private static final Logger logger = LogManager
			.getLogger(StoreFrontConsultantEnrollNowPage.class.getName());

	private final By SPONSOR_SEARCH_FIELD_LOC = By.id("sponserparam");
	private final By SEARCH_SPONSOR_LOC = By.id("sposearch-sponsor-buttonnserparam");
	private final By SELECT_AND_CONTINUE_LOC= By.xpath("//div[@id='findConsultantResultArea']/descendant::button[text()='Select and Continue'][1]");
	
	
	public StoreFrontConsultantEnrollNowPage searchSponsorAndSelectFromResult(String sponsor){
		driver.type(SPONSOR_SEARCH_FIELD_LOC, sponsor);
		logger.info("Entered sponsor as "+sponsor);
		driver.click(SEARCH_SPONSOR_LOC);
		logger.info("Clicked on 'Search' button");
		selectFirstSponsorFronList();
		return this;
	}
	
	public void selectFirstSponsorFronList(){
		driver.click(SELECT_AND_CONTINUE_LOC);
		logger.info("Clicked on 'Select And Continue' button for first result");
	}
	
		
}

