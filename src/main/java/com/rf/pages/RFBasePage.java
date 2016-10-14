package com.rf.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;

import com.rf.core.driver.website.RFWebsiteDriver;

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

	public void highlightElement(WebElement element){
		WrapsDriver wrappedElement = (WrapsDriver) element;
		JavascriptExecutor js = (JavascriptExecutor)wrappedElement.getWrappedDriver();
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		System.out.println("************scrolled");
		for (int i = 0; i < 5; i++) {		
			js.executeScript("arguments[0].setAttribute('style',arguments[1]);",element, "color: yellow; border: 2px solid red;");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			js.executeScript("arguments[0].setAttribute('style',arguments[1]);",element, "");
		}
	}
}
