package com.rf.pages.website.storeFrontBrandRefresh;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontBrandRefreshConsultantPage extends StoreFrontBrandRefreshWebsiteBasePage{

	public StoreFrontBrandRefreshConsultantPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontBrandRefreshConsultantPage.class.getName());


	public void hoverOnBeAConsultantAndClickLinkOnEnrollMe(){
		driver.pauseExecutionFor(2000);
		Actions actions = new Actions(RFWebsiteDriver.driver);
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//span[text()= 'BECOME A CONSULTANT']"))).build().perform();
		logger.info("hover on Products link now as shop skincare");
		driver.click(By.xpath("//span[text()= 'Enroll Now']"));
		// logger.info("Clicked "+link+" link is clicked after hovering shop skincare.");
		driver.waitForPageLoad();
	}


}