package com.rf.pages.website.nscore;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

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
		return formattedDate;
	}

	public String[] getCurrentDateAndMonthAndYearAndMonthShortNameFromPstDate(String pstdate){
		String[] UIMonth= new String[5];
		String[] splittedDate = pstdate.split(" ");
		String date =splittedDate[0];
		if(Integer.parseInt(date)>30){
			date ="30";
		}
		String firstDigitOfDate = String.valueOf(date.charAt(0));
		if(firstDigitOfDate.contains("0")){
			date = String.valueOf(date.charAt(1));
		}
		logger.info("modified date is "+date);
		String month =splittedDate[1].split("\\,")[0];
		logger.info("modified month is "+month);
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
		}else if(month.equalsIgnoreCase("December")){
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

}
