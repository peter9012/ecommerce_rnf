package com.rf.pages.website;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontCartAutoShipPage extends RFWebsiteBasePage{

	private final By UPDATE_MORE_INFO_LINK_LOC = By.xpath("//input[@value='Update more info']");
	
	public StoreFrontCartAutoShipPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public boolean verifycartAutoshipPage(){
		return driver.getCurrentUrl().contains("");
	}
	
	public StoreFrontUpdateCartPage clickUpdateMoreInfoLink() throws InterruptedException{
		driver.click(UPDATE_MORE_INFO_LINK_LOC);
		Thread.sleep(5000);
		return new StoreFrontUpdateCartPage(driver);
	}
}
