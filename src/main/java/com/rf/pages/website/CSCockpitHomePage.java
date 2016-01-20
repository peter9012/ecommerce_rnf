package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.RFBasePage;

public class CSCockpitHomePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitHomePage.class.getName());
	
	private static String customerTypeDDLoc = "//span[contains(text(),'Customer Type')]/select/option[text()='%s']";
	private static String countryDDLoc = "//span[contains(text(),'Country')]/select/option[text()='%s']";
	private static String accountStatusDDLoc = "//span[contains(text(),'Account Status')]/select/option[text()='%s']";
	private static final By SEARCH_BTN = By.xpath("//td[text()='SEARCH']"); 
	
	protected RFWebsiteDriver driver;
	
	public CSCockpitHomePage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}	
	
	public void selectCustomerTypeFromDropDownInCustomerSearchTab(String customerType){
		driver.waitForElementPresent(By.xpath(String.format(customerTypeDDLoc, customerType)));
		driver.click(By.xpath(String.format(customerTypeDDLoc, customerType.toUpperCase())));
	}
	
	public void selectCountryFromDropDownInCustomerSearchTab(String country){
		driver.waitForElementPresent(By.xpath(String.format(countryDDLoc, country)));
		driver.click(By.xpath(String.format(countryDDLoc, country)));
	}
	
	public void selectAccountStatusFromDropDownInCustomerSearchTab(String accountStatus){
		driver.waitForElementPresent(By.xpath(String.format(accountStatusDDLoc, accountStatus)));
		driver.click(By.xpath(String.format(accountStatusDDLoc, accountStatus)));
	}
	
	public void clickSearchBtn(){
		driver.click(SEARCH_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

}
