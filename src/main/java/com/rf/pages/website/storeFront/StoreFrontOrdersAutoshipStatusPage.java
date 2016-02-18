package com.rf.pages.website.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontOrdersAutoshipStatusPage extends StoreFrontRFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(StoreFrontOrdersAutoshipStatusPage.class.getName());

	private final By AUTOSHIP_PAGE_HEADER_LOC = By.xpath("//div[@id='main-content']//div[@class='gray-container-info-top pt1 text-uppercase']");
	private final By AUTOSHIP_CRP_STATUS_LOC = By.xpath("//div[@id='crp-status']/div[1]/span");
	private final By AUTOSHIP_PULSE_STATUS_LOC = By.xpath("//div[@id='pulse-status']/div[1]/span");
	private final By ENROLL_IN_CRP_LOC = By.xpath("//input[@id='crp-enroll']");

	public StoreFrontOrdersAutoshipStatusPage(RFWebsiteDriver driver) {
		super(driver);
	}


	public boolean verifyAutoShipStatusHeader(){
		driver.waitForElementPresent(AUTOSHIP_PAGE_HEADER_LOC);
		String autoShipStatusHeaderText = driver.findElement(AUTOSHIP_PAGE_HEADER_LOC).getText();
		if(autoShipStatusHeaderText.contains("AUTOSHIP STATUS")){
			return true;
		}
		return false;
	}

	public boolean verifyAutoShipCRPStatus(){
		driver.waitForElementPresent(AUTOSHIP_CRP_STATUS_LOC);
		String autoShipCRPStatusText = driver.findElement(AUTOSHIP_CRP_STATUS_LOC).getText();
		if(autoShipCRPStatusText.contains("Enrolled")){
			return true;
		}
		return false;
	}

	public boolean verifyAutoShipPulseSubscriptionStatus(){
		driver.waitForElementPresent(AUTOSHIP_PULSE_STATUS_LOC);
		String autoShipPulseStatusText = driver.findElement(AUTOSHIP_PULSE_STATUS_LOC).getText();
		if(autoShipPulseStatusText.contains("Enrolled")){
			return true;
		}
		return false;
	}

	public void clickAddToCRPButton(String country) throws InterruptedException{
		applyPriceFilterHighToLow();
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO CRP')][1]"));
		if(driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO CRP')][1]")).isEnabled()==true)
			driver.click(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO CRP')][1]"));
		else
			driver.click(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO CRP')][2]"));
		logger.info("Add To Bag button clicked");
		driver.waitForLoadingImageToDisappear();

		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){
		}
	}

	public void clickOnEnrollInCRP() throws InterruptedException{
		driver.waitForElementToBeClickable(ENROLL_IN_CRP_LOC, 5000);
		driver.waitForElementPresent(ENROLL_IN_CRP_LOC);
		driver.click(ENROLL_IN_CRP_LOC);
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnCRPCheckout(){
		driver.waitForElementPresent(By.id("crpCheckoutButton"));
		driver.click(By.id("crpCheckoutButton"));
		logger.info("checkout button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnEditShipping() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']//p[2]/a"));  
		driver.click(By.xpath("//div[@id='multiple-addresses-summary']//p[2]/a"));
		logger.info("Edit Shipping link clicked "+"//div[@id='multiple-addresses-summary']//p[2]/a"); 
		driver.waitForLoadingImageToDisappear();
	}

}
