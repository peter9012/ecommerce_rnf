package com.rf.pages.website.LSD;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.RFBasePage;

public class LSDRFWebsiteBasePage extends RFBasePage{
	protected RFWebsiteDriver driver;
	public LSDRFWebsiteBasePage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}	
	
	public String getCurrentURL(){
		return driver.getCurrentUrl();
	}
	
	public void navigateToHomePage(){
		if(driver.getCurrentUrl().contains("home")==false){
			driver.get(driver.getURL()+"/#/home");
			driver.waitForLSDLoaderAnimationImageToDisappear();
		}
		
	}

}
