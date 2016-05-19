package com.rf.pages.website.LSD;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class LSDHomePage extends LSDRFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(LSDHomePage.class.getName());

	public LSDHomePage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static String selectDDValuesInSearchByDD= "//select[contains(@id,'OrderSearchField')]//option[contains(text(),'%s')]"; 
	private static final By ORDERS_LINK = By.xpath("//a[text()='Orders']") ;
	
	
}