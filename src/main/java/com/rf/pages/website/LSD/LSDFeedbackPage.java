package com.rf.pages.website.LSD;

import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rf.core.driver.website.RFWebsiteDriver;

public class LSDFeedbackPage  extends LSDRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(LSDFeedbackPage.class.getName());

	public LSDFeedbackPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public boolean isFeedbackPagePresent(String parentWindowID){
		Set<String> set=driver.getWindowHandles();
		Iterator<String> it=set.iterator();
		boolean status=false;
		while(it.hasNext()){
			String childWindowID=it.next();
			if(!parentWindowID.equalsIgnoreCase(childWindowID)){
				driver.switchTo().window(childWindowID);
				logger.info("navigate to feedback tab");
				if(driver.getCurrentUrl().contains("rfpulsefeedback")){
					status=true;
				}

			}
		}
		driver.close();
		logger.info("Child window closed");
		driver.switchTo().window(parentWindowID);
		logger.info("navigate to parent window");
		return status;
	}

}