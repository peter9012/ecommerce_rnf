package com.rf.pages.website.storeFrontBrandRefresh;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontBrandRefreshConsultantPage extends StoreFrontBrandRefreshWebsiteBasePage{

	private static String consultantOnlyProduct= "//p[contains(text(),'%s')]/preceding::a[1]/img";

	public StoreFrontBrandRefreshConsultantPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontBrandRefreshConsultantPage.class.getName());

	public void clickConsultantOnlyProduct(String productName){
		driver.quickWaitForElementPresent(By.xpath(String.format(consultantOnlyProduct, productName)));
		driver.click(By.xpath(String.format(consultantOnlyProduct, productName)));
		logger.info("consultant only product selected is: "+productName);
	}

}