package com.rf.pages.website.nscore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore4MobilePage extends NSCore4RFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(NSCore4MobilePage.class.getName());

	public NSCore4MobilePage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static final By HEADLINE_NEWS_LOC = By.xpath("//ul[@id='SubNav']//span[text()='HeadLine News']");
	private static final By RF_IN_NEWS_LOC = By.xpath("//ul[@id='SubNav']//span[text()='R+F In The News']");
	private static final By BROWSEHEADLINE_NEWS_LOC = By.xpath("//td[@class='CoreContent']//h2[contains(text(),'Browse HeadLineNews')]");
	private static final By BROWSERF_IN_NEWS_LOC = By.xpath("//td[@class='CoreContent']//h2[contains(text(),'Browse RF in the News')]");

	public void clickHeadLineNewsLink(){
		driver.quickWaitForElementPresent(HEADLINE_NEWS_LOC);
		driver.click(HEADLINE_NEWS_LOC);
		logger.info("HeadLine News Link is clicked");
		driver.waitForPageLoad();
	}

	public void clickRFInNewsLink(){
		driver.quickWaitForElementPresent(RF_IN_NEWS_LOC);
		driver.click(RF_IN_NEWS_LOC);
		logger.info("R+F In News Link is clicked");
		driver.waitForPageLoad();
	}

	public boolean validateBrowseHeadLineNewsPage(){
		driver.quickWaitForElementPresent(BROWSEHEADLINE_NEWS_LOC);
		return driver.isElementPresent(BROWSEHEADLINE_NEWS_LOC);
	}

	public boolean validateBrowseRFInNewsPage(){
		driver.quickWaitForElementPresent(BROWSERF_IN_NEWS_LOC);
		return driver.isElementPresent(BROWSERF_IN_NEWS_LOC);
	}

}