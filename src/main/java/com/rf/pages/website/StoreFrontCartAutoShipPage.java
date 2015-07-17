package com.rf.pages.website;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontCartAutoShipPage extends RFWebsiteBasePage{

	public StoreFrontCartAutoShipPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public boolean verifycartAutoshipPage(){
		return driver.getCurrentUrl().contains("");
	}
	
	public StoreFrontUpdateCartPage clickUpdateMoreInfoLink(){
		driver.click(By.xpath("//input[@value='Update more info']"));
		return new StoreFrontUpdateCartPage(driver);
	}
}
