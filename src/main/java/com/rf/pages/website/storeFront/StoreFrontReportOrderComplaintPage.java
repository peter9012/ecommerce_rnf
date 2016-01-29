package com.rf.pages.website.storeFront;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.rf.core.driver.website.RFWebsiteDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StoreFrontReportOrderComplaintPage extends StoreFrontRFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(StoreFrontReportOrderComplaintPage.class.getName());	
	
	//private final By ORDER_NUM_OF_ORDER_HISTORY = By.xpath("//table[@id='history-orders-table']/tbody/tr[2]/td[1]/a");
	private final By REPORT_PAGE_HEADER_LOC = By.xpath("//div[contains(text(),'Report a problem')]");
	private final By REPORT_PAGE_CHECKBOX_LOC = By.xpath("//div[@class='repaired-checkbox']");
	private final By REPORT_PAGE_DROPDOWN_OPTION_LOC = By.xpath("//select[@id='problemType']/option");
	private final By REPORT_PAGE_TEXT_AREA_LOC = By.xpath("//textarea[@id='problemText']");
	private final By REPORT_PAGE_SUBMIT_LOC = By.xpath("//input[@id='submitButton']");
	private final By REPORT_PAGE_DROPDOWN_SELECT_LOC = By.xpath("//select[@id='problemType']");
	private final By REPORT_PAGE_DROPDOWN_SELECT_OPTION_LOC = By.xpath("//select[@id='problemType']/option[contains(text(),'Order is incorrect')]");

	public StoreFrontReportOrderComplaintPage(RFWebsiteDriver driver) {
		super(driver);

	}
	public boolean VerifyOrderNumberOnReportPage(){
		driver.waitForElementPresent(REPORT_PAGE_HEADER_LOC);
		String header = driver.findElement(REPORT_PAGE_HEADER_LOC).getText();
		if(header.contains(StoreFrontOrdersPage.orderNumberOfOrderHistory)){
			return true;
		}
		return false;
	}

	public void clickOnCheckBox(){
		driver.waitForElementPresent(REPORT_PAGE_CHECKBOX_LOC);
		driver.click(REPORT_PAGE_CHECKBOX_LOC);
		logger.info("Report page checkbox clicked");
	}

	public boolean verifyCountOfDropDownOptionsOnReportPage(){
		driver.waitForElementPresent(REPORT_PAGE_DROPDOWN_OPTION_LOC);
		List<WebElement> list = driver.findElements(REPORT_PAGE_DROPDOWN_OPTION_LOC);
		int size = list.size();	
		if(size == 6){
			return true;
		}
		return false;

	}
	public void selectOptionFromDropDown() throws InterruptedException{
		driver.waitForElementPresent(REPORT_PAGE_DROPDOWN_SELECT_LOC);
		driver.click(REPORT_PAGE_DROPDOWN_SELECT_LOC);
		logger.info("Report Page dropdown clicked");
		driver.waitForElementPresent(REPORT_PAGE_DROPDOWN_SELECT_OPTION_LOC);
		driver.click(REPORT_PAGE_DROPDOWN_SELECT_OPTION_LOC);
		logger.info("Report Page dropdown option selected");
	}

	public StoreFrontReportProblemConfirmationPage enterYourProblemAndSubmit(String problemTextArea){
		driver.waitForElementPresent(REPORT_PAGE_TEXT_AREA_LOC);
		driver.type(REPORT_PAGE_TEXT_AREA_LOC,problemTextArea);
		driver.click(REPORT_PAGE_SUBMIT_LOC);
		logger.info("Report page submit button clicked");
		return new StoreFrontReportProblemConfirmationPage(driver);
	}
}
