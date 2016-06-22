package com.rf.pages.website.LSD;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

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
			driver.pauseExecutionFor(2000);
		}	

	}

	public String getCurrentPSTDate(){
		DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
		df.setTimeZone(TimeZone.getTimeZone("PST"));
		final String dateString = df.format(new Date());
		String[] datePST = dateString.split(" ");
		System.out.println(dateString);
		String splittedDateForMonth = datePST[1]+" "+datePST[2]+" "+datePST[3];
		return splittedDateForMonth;
	}

}
