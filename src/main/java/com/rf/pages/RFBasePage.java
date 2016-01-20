package com.rf.pages;

import org.openqa.selenium.WebDriver;

/**
 * @author ShubhamMathur RFBasePage is the super class for all the page
 *         classes
 */
public class RFBasePage {
	public WebDriver webdriver;
	
	public RFBasePage(WebDriver driver) {
		webdriver = driver;
	}
	
	public WebDriver getWebdriver(){
		return webdriver;
	}
}
