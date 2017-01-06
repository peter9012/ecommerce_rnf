package com.rf.pages.website.rehabitat.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

public class StoreFrontAutoshipCartPage extends StoreFrontWebsiteBasePage{

	public StoreFrontAutoshipCartPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontAutoshipCartPage.class.getName());

	private final By CLOSE_ICON_PULSE_OVERLAY_LOC = By.id("cboxClose");
	private final By LEARN_MORE_ABOUT_PULSE_LOC = By.xpath("//a[contains(text(),'Learn more about Pulse')]");
	private final By PULSE_POPUP_LOC = By.xpath("//h1[contains(text(),'Pulse Business Management')]");
	private final By PC_PERKS_CHECKOUT_LOC = By.xpath("//a[contains(text(),'PC Perks Checkout')]");
	private String socialMediaIconLoc = "//div[@class='container']//a[contains(@href,'%s')]";

	/***
	 * This method click on learn more about pulse link.
	 * 
	 * @param 
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipCartPage clickLearnMoreAboutPulse(){
		driver.click(LEARN_MORE_ABOUT_PULSE_LOC);
		logger.info("clicked on 'learn more about pulse' link");
		return this;
	}

	/***
	 * This method validates overlay and its content displayed when clicked on learn more about pulse link
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isPulsePopupPresentWithContent(){
		return driver.isElementPresent(PULSE_POPUP_LOC);

	}

	/***
	 * This method close pulse overlay
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipCartPage closePulsePopup(){
		driver.click(CLOSE_ICON_PULSE_OVERLAY_LOC);
		logger.info("clicked X of 'pulse overlay'");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method click on the pc perks checkout button
	 * 
	 * @param
	 * @return StoreFrontCheckoutPage object
	 */
	public StoreFrontCheckoutPage clickOnPCPerksCheckoutButton(){
		driver.click(PC_PERKS_CHECKOUT_LOC);
		logger.info("Clicked on pc perks checkout button");
		return new StoreFrontCheckoutPage(driver);
	}

}