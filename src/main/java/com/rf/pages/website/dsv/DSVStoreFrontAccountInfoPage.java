package com.rf.pages.website.dsv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import com.rf.core.driver.website.RFWebsiteDriver;

public class DSVStoreFrontAccountInfoPage extends DSVRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(DSVStoreFrontAccountInfoPage.class.getName());

	private static final By FIRST_NAME = By.id("first-name");
	private static final By LAST_NAME = By.id("last-name");
	private static final By ADDRESS_LINE_1 = By.id("address-1");
	private static final By PHONE_NUMBER = By.id("phonenumber");
	private static final By DOB_DATE = By.id("dayOfBirth");
	private static final By DOB_MONTH = By.id("monthOfBirth");
	private static final By DOB_YEAR = By.id("yearOfBirth");
	private static final By SAVE_BUTTON = By.id("saveAccountInfo");
	private static final By QAS_POP_UP = By.id("QAS_Dialog");
	private static final By QAS_USE_AS_ENTERED=By.id("QAS_AcceptOriginal");
	private static final By SUCCESS_MESSAGE = By.xpath("//div[@id='globalMessages']//p");

	public DSVStoreFrontAccountInfoPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void enterFirstNameOfUser(String firstName){
		driver.quickWaitForElementPresent(FIRST_NAME);
		driver.type(FIRST_NAME, firstName);
	}

	public void enterLastNameOfUser(String lastName){
		driver.quickWaitForElementPresent(LAST_NAME);
		driver.type(LAST_NAME, lastName);
	}

	public void enterAddressLineOne(String addressLineOne){
		driver.quickWaitForElementPresent(ADDRESS_LINE_1);
		driver.type(ADDRESS_LINE_1, addressLineOne);
	}

	public void enterPhoneNumber(String phoneNumber){
		driver.quickWaitForElementPresent(PHONE_NUMBER);
		driver.type(PHONE_NUMBER, phoneNumber);
	}

	public void selectDOBDate(int date){
		driver.quickWaitForElementPresent(DOB_DATE);
		Select select=new Select(driver.findElement(DOB_DATE));
		select.selectByIndex(date);
	}

	public void selectDOBMonth(int month){
		driver.quickWaitForElementPresent(DOB_MONTH);
		Select select=new Select(driver.findElement(DOB_MONTH));
		select.selectByIndex(month);
	}

	public void selectDOBYear(int year){
		driver.quickWaitForElementPresent(DOB_YEAR);
		Select select=new Select(driver.findElement(DOB_YEAR));
		select.selectByIndex(year);
	}

	public void clickOnSaveButton(){
		driver.quickWaitForElementPresent(SAVE_BUTTON);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SAVE_BUTTON));
		try{
			driver.waitForElementPresent(QAS_USE_AS_ENTERED);
			driver.findElement(QAS_USE_AS_ENTERED).click();
		}catch(Exception e){
			logger.info("QAS has not appeared");
		}
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		
	}

	public boolean isSuccessMessagePresentOnPage(){
		driver.quickWaitForElementPresent(SUCCESS_MESSAGE);
		if(driver.findElement(SUCCESS_MESSAGE).isDisplayed()){
			return true;
		}else{
			return false;
		}
	}
}
