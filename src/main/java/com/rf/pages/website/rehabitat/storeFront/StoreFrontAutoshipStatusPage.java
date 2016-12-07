package com.rf.pages.website.rehabitat.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

public class StoreFrontAutoshipStatusPage extends StoreFrontWebsiteBasePage{

	public StoreFrontAutoshipStatusPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontAutoshipStatusPage.class.getName());

	private final By CLOSE_ICON_PULSE_OVERLAY_LOC = By.id("cboxClose");
	private final By LEARN_MORE_ABOUT_PULSE_LOC = By.xpath("//a[contains(text(),'Learn more about Pulse')]");
	private final By PULSE_POPUP_LOC = By.xpath("//h1[contains(text(),'Pulse Business Management')]");

	private String socialMediaIconLoc = "//div[@class='container']//a[contains(@href,'%s')]";

	/***
	 * This method click on learn more about pulse link.
	 * 
	 * @param 
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage clickLearnMoreAboutPulse(){
		driver.click(LEARN_MORE_ABOUT_PULSE_LOC);
		logger.info("clicked on 'learn more about pulse' link");
		return this;
	}
	/***
	 * This method validates overlay present when clicked on learn more about pulse link
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isPulsePopupPresent(){
		return driver.findElement(PULSE_POPUP_LOC).isDisplayed();
	}
	/***
	 * This method close pulse overlay
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage closePulsePopup(){
		driver.click(CLOSE_ICON_PULSE_OVERLAY_LOC);
		logger.info("clicked X of 'pulse overlay'");
		driver.pauseExecutionFor(2000);
		return this;
	}

}