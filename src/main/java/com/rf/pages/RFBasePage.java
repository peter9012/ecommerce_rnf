package com.rf.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * @author ShubhamMathur RFBasePage is the super class for all the page
 *         classes
 */
public class RFBasePage {
	public WebDriver webdriver;
	private static final Logger logger = LogManager
			.getLogger(RFBasePage.class.getName());

	public RFBasePage(WebDriver driver) {
		webdriver = driver;
	}
	
	public WebDriver getWebdriver(){
		return webdriver;
	}

}
