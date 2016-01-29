package com.rf.pages.website.storeFront;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontEnrollNowPage extends StoreFrontRFWebsiteBasePage{
		
	private Actions actions;
	
	public StoreFrontEnrollNowPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public void searchCID(String cid){
		driver.waitForElementPresent(By.id("sponserparam"));
		driver.findElement(By.id("sponserparam")).sendKeys(cid);
		driver.click(By.id("search-sponsor-button"));
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
