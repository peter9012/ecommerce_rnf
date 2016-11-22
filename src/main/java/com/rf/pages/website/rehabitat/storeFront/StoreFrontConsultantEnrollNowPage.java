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

	private final By SEARCH_SPONSOR_LOC = By.id("search-sponsor-button");
	private final By SELECT_AND_CONTINUE_LOC= By.xpath("//div[@id='findConsultantResultArea']/descendant::button[text()='Select and Continue'][1]");
	private final By SPONSOR_SEARCH_RESULTS_LOC = By.xpath("//div[@class='row']/div[contains(@class,'consultant-box')]");
	
	/***
	 * This method enter the sponsor name and click on search button
	 * 
	 * @param sponsor
	 * @return
	 * 
	 */
	public StoreFrontConsultantEnrollNowPage searchSponsor(String sponsor){
		driver.type(SPONSOR_SEARCH_FIELD_LOC, sponsor);
		logger.info("Entered sponsor as "+sponsor);
		driver.click(SEARCH_SPONSOR_LOC);
		logger.info("Clicked on 'Search' button");
		return this;
	}
	
	/***
	 * This method selects the first sponsor name in the search result.
	 * 
	 * @param sponsor
	 * @return
	 * 
	 */
	public void selectFirstSponsorFronList(){
		driver.click(SELECT_AND_CONTINUE_LOC);
		logger.info("Clicked on 'Select And Continue' button for first result");
	}
	
	/***
	 * This method verifies whether any result has been present after searching
	 * the sponsor or not.
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isSponsorPresentInResult(){
		return driver.isElementPresent(SPONSOR_SEARCH_RESULTS_LOC);
	}
	
		
}

