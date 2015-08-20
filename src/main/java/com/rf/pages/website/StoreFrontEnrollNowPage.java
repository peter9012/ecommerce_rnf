package com.rf.pages.website;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import com.rf.core.driver.website.RFWebsiteDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontEnrollNowPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontEnrollNowPage.class.getName());
	
	private Actions actions;
	
	public StoreFrontEnrollNowPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void searchCID(String cid){
		driver.waitForElementPresent(By.cssSelector("input[id='sponserparam']"));
		driver.findElement(By.cssSelector("input[id='sponserparam']")).sendKeys(cid);
		driver.click(By.cssSelector("input[id='search-sponsor-button']"));
	}
	
	public void mouseHoverSponsorDataAndClickContinue(){
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.cssSelector("div[class='sponsorDataDiv']"))).click(driver.findElement(By.cssSelector("input[value='Select & Continue']"))).build().perform();				
	}
	
	public void selectEnrollmentKitPage(String kitPrice,String regimenName){
		kitPrice =  kitPrice.toUpperCase();
		driver.click(By.xpath("//div[@class='kit-price' and contains(text(),'"+kitPrice+"')]"));
		regimenName = regimenName.toUpperCase();
		driver.click(By.xpath("//div[@class='regimen-name' and text()='"+regimenName+"']"));
		driver.click(By.cssSelector("input[value='Next']"));
	}
	
	public StoreFrontCreateAccountPage chooseEnrollmentOption(String option){
		option = option.toUpperCase();
		if(option.equalsIgnoreCase("EXPRESS ENROLLMENT")){
			driver.click(By.id("express-enrollment"));
		}
		else{
			// to do
		}
		driver.click(By.cssSelector("input[value='Next']"));
		return new StoreFrontCreateAccountPage(driver);
	}
}
