package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontOrdersPage extends StoreFrontWebsiteBasePage{

	public StoreFrontOrdersPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontOrdersPage.class.getName());

	private final By FIRST_ORDER_NUMBER_UNDER_ORDER_HISTORY_LOC = By.xpath("//div[@class='account-section']/descendant::div[@class='account-orderhistory'][2]/descendant::a[1]");
	private final By FIRST_ACTIONS_DD_UNDER_ORDER_HISTORY_LOC = By.xpath("//div[@class='account-section']/descendant::div[@class='account-orderhistory'][2]/descendant::div[contains(text(),'Actions')][1]");
	private final By NAME_FIELD_AT_REPORT_PROBLEM_PAGE_LOC = By.xpath("//form[@id='reportAProblemForm']/descendant::span[@class='message-content'][2]");
	private final By EMAIL_FIELD_AT_REPORT_PROBLEM_PAGE_LOC = By.xpath("//div[contains(text(),'Email')]//input[@name='email']");
	private final By PROBLEM_DD_LOC = By.id("problemReasonCode");
	private final By MESSAGE_BOX_LOC = By.id("message");
	private final By CHKBOX_OF_PRODUCT = By.id("orderEntries1");
	private final By PROBLEM_DD_FIRST_OPTION_LOC = By.xpath("//select[@id='problemReasonCode']/option[1]");
	private final By SUBMIT_BTN_LOC = By.xpath("//button[text()='SUBMIT']");
	private final By CONFIRMATION_MSG_OF_REPORT_PROBLEM = By.xpath("//div[@class='account-section']");

	private String detailsLinkUnderOrderHistoryLoc = "//div[@class='account-section']/descendant::div[@class='account-orderhistory'][2]/descendant::a[contains(text(),'%s')][1]";

	/***
	 * This method get first order number from order history 
	 * 
	 * @param
	 * @return order number
	 * 
	 */
	public String getFirstOrderNumberFromOrderHistory(){
		String orderNumber = driver.findElement(FIRST_ORDER_NUMBER_UNDER_ORDER_HISTORY_LOC).getText();
		logger.info("Order number is "+orderNumber);
		return orderNumber;
	}

	/***
	 * This method click on action DD and choose options 
	 * 
	 * @param link name
	 * @return store front orders page object
	 * 
	 */
	public StoreFrontOrdersPage chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(String linkname){
		driver.click(FIRST_ACTIONS_DD_UNDER_ORDER_HISTORY_LOC);
		logger.info("Actions drpdown clicked under order history");
		driver.click(By.xpath(String.format(detailsLinkUnderOrderHistoryLoc, linkname)));
		logger.info(linkname+" link clicked of first order under order history");
		return this;
	}

	/***
	 * This method validates the name field is editable or not 
	 * 
	 * @param name
	 * @return boolean value
	 * 
	 */
	public boolean isNameFieldEditableAtReportProblemPage(String name){
		try{
			driver.type(NAME_FIELD_AT_REPORT_PROBLEM_PAGE_LOC, name);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/***
	 * This method validates the email field is present or not 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isEmailFieldPresentAtReportProblemPage(){
		return driver.isElementPresent(EMAIL_FIELD_AT_REPORT_PROBLEM_PAGE_LOC);
	}

	/***
	 * This method validates the problem drop down is present or not 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isProblemDDPresentAtReportProblemPage(){
		return driver.isElementPresent(PROBLEM_DD_LOC);
	}

	/***
	 * This method validates the problem drop down is present or not 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isMessageBoxPresentAtReportProblemPage(){
		return driver.isElementPresent(MESSAGE_BOX_LOC);
	}

	/***
	 * This method enter the details for return a product  
	 * 
	 * @param message
	 * @return store front orders page object
	 * 
	 */
	public StoreFrontOrdersPage enterTheDetailsForReportProblem(String message){
		driver.click(CHKBOX_OF_PRODUCT);
		logger.info("Check box checked for product");
		driver.click(PROBLEM_DD_LOC);
		logger.info("Problem dropdown clicked");
		driver.click(PROBLEM_DD_FIRST_OPTION_LOC);
		logger.info("First option select from problem dropdown");
		driver.type(MESSAGE_BOX_LOC, message);
		logger.info("Message typed as: "+message);
		return this;
	}

	/***
	 * This method click the submit button at return order page  
	 * 
	 * @param message
	 * @return store front orders page object
	 * 
	 */
	public StoreFrontOrdersPage clickSubmitBtnAtReportProblemPage(){
		driver.click(SUBMIT_BTN_LOC);
		logger.info("Submit button clicked for return order");
		return this;
	}

	/***
	 * This method get the confirmation message of report problem
	 * 
	 * @param
	 * @return Confirmation message
	 * 
	 */
	public String getConfirmationMsgOfReportProblem(){
		String msg = driver.findElement(CONFIRMATION_MSG_OF_REPORT_PROBLEM).getText();
		logger.info("Confirmation message is "+msg);
		return msg;
	}
}

