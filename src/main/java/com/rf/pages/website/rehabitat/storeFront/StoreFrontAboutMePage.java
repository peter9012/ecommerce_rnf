package com.rf.pages.website.rehabitat.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

public class StoreFrontAboutMePage extends StoreFrontWebsiteBasePage{

	public StoreFrontAboutMePage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontAboutMePage.class.getName());

	private final By PERSONALIZE_MY_PROFILE_LOC = By.xpath("//a[contains(text(),'PERSONALIZE MY PROFILE')]");
	
	private final By CONTACT_ME_HEADER_LOC = By.xpath("//h3[contains(text(),'Contact Me')]");
	
	private String quesOnAboutMePageLoc = "//form[@id='aboutmeFormData']//h2[contains(text(),'%s')]";
	
	private String answerOnAboutMePageLoc = "//form[@id='aboutmeFormData']//h2[contains(text(),'%s')]/following-sibling::p";
	
	

	

	/***
	 * This method click Personalize my profile button
	 * 
	 * @param
	 * @return Store Front About Me Page
	 * 
	 */
	public StoreFrontAboutMePage clickPersonalizeMyProfileButton(){
		driver.click(PERSONALIZE_MY_PROFILE_LOC);
		logger.info("Personalize my profile btn clicked");
		return this;
	}
	
	/***
	 * This method validates the presence of contact me header on about me page
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isContactMeHeaderPresentOnAboutMePage(){
		return driver.isElementVisible(CONTACT_ME_HEADER_LOC);
	
	}
	
	/***
	 * This method validates the presence of 'What I love most about Rodan and Fields' ques on about me page
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isExpectedQuesPresentOnAboutMePage(String ques){
		return driver.isElementVisible(By.xpath(String.format(quesOnAboutMePageLoc,ques)));
	
	}
	
	/***
	 * This method validates the presence of answer of 'What I love most about Rodan and Fields' ques on about me page
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isAnswerOfExpectedQuesPresentOnAboutMePage(String ques){
		return driver.isElementVisible(By.xpath(String.format(answerOnAboutMePageLoc,ques)));
	
	}


}