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
	private static final By CUSTOMERS_LINK = By.id("nav-customers");
	private static final By ORDERS_LINK = By.id("nav-orders");
	private static final By FEEDBACK_LINK = By.id("nav-feedback");

	public void clickLogout(){
		driver.click(LOGOUT_BTN);
		driver.waitForLSDJustAMomentImageToDisappear();
	}

	public void clickViewMyOrdersLink(){
		driver.quickWaitForElementPresent(VIEW_MY_ORDERS_LINK);
		driver.click(VIEW_MY_ORDERS_LINK);
		driver.waitForLSDLoaderAnimationImageToDisappear();
	}

	public LSDCustomerPage clickCustomersLink(){
		driver.waitForElementPresent(CUSTOMERS_LINK);
		driver.click(CUSTOMERS_LINK);
		logger.info("Customers link clicked from main menu");
		driver.waitForLSDLoaderAnimationImageToDisappear();
		return new LSDCustomerPage(driver);
	}

	public LSDOrderPage clickOrdersLink(){
		driver.waitForElementPresent(ORDERS_LINK);
		driver.click(ORDERS_LINK);
		logger.info("Orders link clicked from main menu");
		driver.waitForLSDLoaderAnimationImageToDisappear();
		return new LSDOrderPage(driver);
	}
	public LSDFeedbackPage clickFeedbackLink(){
		driver.click(FEEDBACK_LINK);
		logger.info("Feedback link clicked from main menu");
		driver.pauseExecutionFor(2000);
		return new LSDFeedbackPage(driver);
	}

}