package com.rf.pages.website.nscore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.RFBasePage;

public class NSCore4RFWebsiteBasePage extends RFBasePage{
	protected RFWebsiteDriver driver;
	private static final Logger logger = LogManager
			.getLogger(NSCore4RFWebsiteBasePage.class.getName());
	public NSCore4RFWebsiteBasePage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}	

	private static String tabLoc = "//ul[@id='GlobalNav']//span[text()='%s']";

	public void clickTab(String tabName){
		driver.quickWaitForElementPresent(By.xpath(String.format(tabLoc, tabName)));
		driver.click(By.xpath(String.format(tabLoc, tabName)));
		logger.info("Tab clicked is: "+tabName);
		driver.waitForPageLoad();
	}

	public void clickOKBtnOfJavaScriptPopUp(){
		Alert alert = driver.switchTo().alert();
		alert.accept();
		logger.info("Ok button of java Script popup is clicked.");
		driver.waitForPageLoad();
	}
}
