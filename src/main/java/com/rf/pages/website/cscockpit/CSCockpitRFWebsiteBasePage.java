package com.rf.pages.website.cscockpit;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.RFBasePage;

public class CSCockpitRFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitRFWebsiteBasePage.class.getName());

	private static final By NO_RESULTS_LBL = By.xpath("//span[text()='No Results']");
	private static final By CHANGE_ORDER_LINK = By.xpath("//a[text()='Change Order']");
	private static final By MENU_BTN_LOCATOR = By.xpath("//span[text()='Menu']");
	private static final By LOGOUT_BTN_LOCATOR = By.xpath("//a[contains(text(),'Logout')]");
	private static final By FIND_ORDER_LINK_LEFT_NAVIGATION_LOC = By.xpath("//a[contains(text(),'Find Order')]");
	private static final By CUSTOMER_TAB = By.xpath("//span[text()='Customer']");
	private static final By ORDER_SEARCH_TAB_BTN = By.xpath("//span[text()='Order Search']");
	private static final By CUSTOMER_SEARCH_TAB_LOC = By.xpath("//span[text()='Customer Search']");
	
	protected RFWebsiteDriver driver;
	public CSCockpitRFWebsiteBasePage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public String getBaseURL(){
		return driver.getURL();
	}

	public String getCurrentURL(){
		return driver.getCurrentUrl();
	}

	public void openURL(String URL){
		driver.get(URL);
		driver.waitForPageLoad();
	}	


	public boolean isNoResultDisplayed(){
		driver.quickWaitForElementPresent(NO_RESULTS_LBL);
		return driver.isElementPresent(NO_RESULTS_LBL);
	}

	public String getPSTDate(){
		Date startTime = new Date();
		TimeZone pstTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
		DateFormat formatter = DateFormat.getDateInstance();
		formatter.setTimeZone(pstTimeZone);
		String formattedDate = formatter.format(startTime);
		return formattedDate;
	}

	public String convertUIDateFormatToPSTFormat(String UIDate){
		String UIMonth=null;
		String[] splittedDate = UIDate.split("\\/");
		String date = splittedDate[1];
		String month = splittedDate[0];
		String year = splittedDate[2];  
		switch (Integer.parseInt(month)) {  
		case 1:
			UIMonth="Jan";
			break;
		case 2:
			UIMonth="Feb";
			break;
		case 3:
			UIMonth="Mar";
			break;
		case 4:
			UIMonth="Apr";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="Jun";
			break;
		case 7:
			UIMonth="Jul";
			break;
		case 8:
			UIMonth="Aug";
			break;
		case 9:
			UIMonth="Sep";
			break;
		case 10:
			UIMonth="Oct";
			break;
		case 11:
			UIMonth="Nov";
			break;
		case 12:
			UIMonth="Dec";
			break;  
		}
		return date+" "+UIMonth+", "+year;
	}

	public void refreshPage(){
		driver.navigate().refresh();
		driver.waitForPageLoad();
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickChangeOrderLinkOnLeftNavigation(){
		driver.waitForElementPresent(CHANGE_ORDER_LINK);
		driver.click(CHANGE_ORDER_LINK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickMenuButton(){
		driver.waitForElementPresent(MENU_BTN_LOCATOR);
		driver.click(MENU_BTN_LOCATOR);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickLogoutButton(){
		driver.waitForElementPresent(LOGOUT_BTN_LOCATOR);
		driver.click(LOGOUT_BTN_LOCATOR);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickFindOrderLinkOnLeftNavigation() {
		driver.waitForElementPresent(FIND_ORDER_LINK_LEFT_NAVIGATION_LOC);
		driver.click(FIND_ORDER_LINK_LEFT_NAVIGATION_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCustomerTab(){
		driver.waitForElementPresent(CUSTOMER_TAB);
		driver.click(CUSTOMER_TAB);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickOrderSearchTab(){
		driver.waitForElementPresent(ORDER_SEARCH_TAB_BTN);
		driver.click(ORDER_SEARCH_TAB_BTN);
		logger.info("Order Search tab clicked");
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCustomerSearchTab() {
		driver.waitForElementPresent(CUSTOMER_SEARCH_TAB_LOC);
		driver.click(CUSTOMER_SEARCH_TAB_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}


}
