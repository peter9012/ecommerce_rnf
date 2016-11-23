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
	private final By FIRST_EVENT_CALENDAR_LOC = By.xpath("//h3[contains(text(),'Presentations')]/following::a[text()='EVENT CALENDER'][1]");
	private final By VISIT_THE_BLOG_LOC = By.xpath("//a[text()='VISIT THE BLOG']");
	private String socialMediaIconLoc = "//div[@class='container']//a[contains(@href,'%s')]";
	
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

	/***
	 * This method click on first event calendar button
	 * 
	 * @param 
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontHomePage clickFirstEventCalendar(){
		driver.click(FIRST_EVENT_CALENDAR_LOC);
		logger.info("clicked on 'Event calendar button'");
		return this;
	}
	
	/***
	  * This method click on visit the block button
	  * 
	  * @param
	  * @return store front home page object
	  * 
	  */
	 public StoreFrontHomePage clickVisitTheBlock(){
	  driver.click(VISIT_THE_BLOG_LOC);
	  logger.info("clicked on 'VISIT THE BLOG' button");
	  return this;
	 }

	 /***
	  * This method click on social media icon
	  * 
	  * @param social media type
	  * @return store front Home page object
	  * 
	  */
	 public StoreFrontHomePage clickSocialMediaIcon(String mediaType){
	  driver.click(By.xpath(String.format(socialMediaIconLoc, mediaType)));
	  logger.info("clicked on"+mediaType+" icon");
	  return this;
	 }
}