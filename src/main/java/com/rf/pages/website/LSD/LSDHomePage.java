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
	private static final By LOGOUT_BTN = By.id("nav-logout") ;
	private static final By VIEW_MY_ORDERS_LINK = By.xpath("//span[text()='View my orders']") ;
	
	
	public void clickLogout(){
		driver.click(LOGOUT_BTN);		
	}
	
	public void clickViewMyOrdersLink(){
		driver.quickWaitForElementPresent(VIEW_MY_ORDERS_LINK);
		driver.click(VIEW_MY_ORDERS_LINK);
	}
	
}