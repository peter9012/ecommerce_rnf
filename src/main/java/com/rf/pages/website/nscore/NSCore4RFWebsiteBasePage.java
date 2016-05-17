package com.rf.pages.website.nscore;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.RFBasePage;

public class NSCore4RFWebsiteBasePage extends RFBasePage{
	protected RFWebsiteDriver driver;
	private static final Logger logger = LogManager
			.getLogger(NSCore4RFWebsiteBasePage.class.getName());
	public NSCore4RFWebsiteBasePage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}	

	private static String tabLoc = "//ul[@id='GlobalNav']//span[text()='%s']";
	private static String selectMonthOnCalender = "//select[@class='ui-datepicker-month']/option[text()='%s']";
	private static String selectYearOnCalender = "//select[@class='ui-datepicker-year']/option[text()='%s']";
	private static String selectDateOnCalender = "//table[@class='ui-datepicker-calendar']/tbody/tr/td/a[text()='%s']";

	private static final By MONTH_DROPDOWN_OF_CALENDER  = By.xpath("//select[@class='ui-datepicker-month']");
	private static final By YEAR_DROPDOWN_OF_CALENDER  = By.xpath("//select[@class='ui-datepicker-year']");
	private static final By SUBMIT_ORDER_BTN_LOC = By.xpath("//a[@id='btnSubmitOrder']/span[contains(text(),'Submit')]");

	public void clickTab(String tabName){
		driver.quickWaitForElementPresent(By.xpath(String.format(tabLoc, tabName)));
		driver.click(By.xpath(String.format(tabLoc, tabName)));
		logger.info("Tab clicked is: "+tabName);
		driver.waitForPageLoad();
	}

	public void clickOKBtnOfJavaScriptPopUp(){
		Alert alert = driver.switchTo().alert();
		alert.accept();
		logger.info("Ok button of java Script popup is clicked.");
		driver.waitForPageLoad();
	}

	public void selectMonthOnCalenderForNewEvent(String month){
		driver.waitForElementPresent(MONTH_DROPDOWN_OF_CALENDER);
		driver.click(MONTH_DROPDOWN_OF_CALENDER); 
		logger.info("Month dropdown clicked on calendar");
		driver.quickWaitForElementPresent(By.xpath(String.format(selectMonthOnCalender, month)));
		driver.click(By.xpath(String.format(selectMonthOnCalender, month)));
		logger.info("Month"+month+" is selected from dropdown.");
	}

	public void selectYearOnCalenderForNewEvent(String year){
		driver.waitForElementPresent(YEAR_DROPDOWN_OF_CALENDER);
		driver.click(YEAR_DROPDOWN_OF_CALENDER);
		logger.info("Year dropdown clicked on calendar");
		driver.quickWaitForElementPresent(By.xpath(String.format(selectYearOnCalender, year)));
		driver.click(By.xpath(String.format(selectYearOnCalender, year)));
		logger.info("Year"+year+" is selected from dropdown.");
	}

	public void clickSpecficDateOfCalendar(String date){
		driver.waitForElementPresent(By.xpath(String.format(selectDateOnCalender, date)));
		driver.click(By.xpath(String.format(selectDateOnCalender, date)));
	}

	public static String getPSTDate(){
		Date startTime = new Date();
		TimeZone pstTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
		DateFormat formatter = DateFormat.getDateInstance();
		formatter.setTimeZone(pstTimeZone);
		String formattedDate = formatter.format(startTime);
		logger.info("PST Date is "+formattedDate);
		return formattedDate;
	}

	public String[] getCurrentDateAndMonthAndYearAndMonthShortNameFromPstDate(String pstdate){
		String[] UIMonth= new String[5];
		String[] splittedDate = pstdate.split(" ");
		String date =splittedDate[0];
		String month =null;		
		try{
			if(Integer.parseInt(date)>30){
				date ="30";
			}
			logger.info("modified date is "+date);
			month=splittedDate[1].split("\\,")[0];
			logger.info("modified month is "+month);
		}catch(NumberFormatException nfe){
			date =splittedDate[1].split(",")[0];			
			if(Integer.parseInt(date)>30){
				date ="30";
			}
			logger.info("modified date is "+date);
			month =splittedDate[0];
			logger.info("modified month is "+month);
		}
		String firstDigitOfDate = String.valueOf(date.charAt(0));
		if(firstDigitOfDate.contains("0")){
			date = String.valueOf(date.charAt(1));
		}

		String year = splittedDate[2];
		logger.info("modified year is "+year); 
		if(month.equalsIgnoreCase("Jan")){
			UIMonth[0]= date;
			UIMonth[1]="January";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}else if(month.equalsIgnoreCase("Feb")){
			UIMonth[0]=date;  
			UIMonth[1]="February";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}else if(month.equalsIgnoreCase("Mar")){
			UIMonth[0]=date;  
			UIMonth[1]="March";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}
		else if(month.equalsIgnoreCase("Apr")){
			UIMonth[0]=date;
			UIMonth[1]="April";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}
		else if(month.equalsIgnoreCase("May")){
			UIMonth[0]=date;
			UIMonth[1]="May";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}
		else if(month.equalsIgnoreCase("Jun")){
			UIMonth[0]=date;
			UIMonth[1]="June";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}
		else if(month.equalsIgnoreCase("Jul")){
			UIMonth[0]=date;
			UIMonth[1]="July";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}
		else if(month.equalsIgnoreCase("Aug")){
			UIMonth[0]=date;
			UIMonth[1]="August";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}
		else if(month.equalsIgnoreCase("Sep")){
			UIMonth[0]=date;
			UIMonth[1]="September";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}
		else if(month.equalsIgnoreCase("Oct")){
			UIMonth[0]=date;
			UIMonth[1]="October";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}
		else if(month.equalsIgnoreCase("Nov")){
			UIMonth[0]=date;
			UIMonth[1]="November";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}else if(month.equalsIgnoreCase("Dec")){
			UIMonth[0]=date;
			UIMonth[1]="December";
			UIMonth[2]=year;
			UIMonth[3]=month;
		}
		return UIMonth;
	}

	public void navigateToBackPage(){
		driver.waitForPageLoad();
		driver.navigate().back();
	}

	public String getMonthInWords(String month){
		String UIMonth = null;
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
		logger.info("month is: "+UIMonth);
		return UIMonth;
	}

	public void reLoadPage(){
		driver.navigate().refresh();
		driver.waitForPageLoad();
	}

	public void clickSubmitOrderBtn() {
		driver.pauseExecutionFor(3000);
		driver.quickWaitForElementPresent(SUBMIT_ORDER_BTN_LOC);
		driver.click(SUBMIT_ORDER_BTN_LOC);
		logger.info("Submit order button is clicked");
		driver.waitForNSCore4ProcessImageToDisappear();
		driver.waitForPageLoad();
		driver.pauseExecutionFor(3000);
	}

	public void handleAlertPop() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.accept();
			logger.info("popup ok button clicked");
			driver.pauseExecutionFor(5000);
			driver.waitForPageLoad();
		} catch (Exception e) {
			//exception handling
		}
	}

}
