package com.rf.pages.website.pulse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

public class PulseHomePage extends PulseWebsiteBasePage{

	@FindBy(xpath="//span[contains(text(),'Dismiss')]")
	private WebElement dismissLink;


	public PulseHomePage(RFWebsiteDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	private static final Logger logger = LogManager
			.getLogger(PulseHomePage.class.getName());

	public boolean isHomePagePresent(){
		if(driver.isElementVisible(dismissLink)){
			dismissLink.click();
//			driver.pauseExecutionFor(3000);
		}
		return nameDropDown.isDisplayed();
	}
	
}

