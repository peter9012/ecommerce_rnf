package com.rf.pages.website.LSD;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.RFBasePage;

public class LSDRFWebsiteBasePage extends RFBasePage{
	protected RFWebsiteDriver driver;
	public LSDRFWebsiteBasePage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}	

}
