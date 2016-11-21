package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontCartPage extends StoreFrontWebsiteBasePage{
	public StoreFrontCartPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontCartPage.class.getName());

	private final By CHECKOUT_BUTTON_LOC = By.xpath("//div[@class='cart-container']/descendant::button[contains(text(),'Checkout')][2]");

	public StoreFrontCheckoutPage checkoutTheCart(){
		driver.click(CHECKOUT_BUTTON_LOC);
		logger.info("Clicked on checkout button");
		return new StoreFrontCheckoutPage(driver);
	}

}

