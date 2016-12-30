package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

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

	private final By FIRST_ORDER_NUMBER_UNDER_ORDER_HISTORY_LOC = By.xpath("//div[@id='orderHistoryContentArea']//tr[2]//td[2]/a");
	private final By FIRST_ACTIONS_DD_UNDER_ORDER_HISTORY_LOC = By.xpath("//div[@id='orderHistoryContentArea']//tr[2]//div[contains(text(),'Actions')]");
	private final By NAME_FIELD_AT_REPORT_PROBLEM_PAGE_LOC = By.xpath("//form[@id='reportAProblemForm']/descendant::span[@class='message-content'][2]");
	private final By EMAIL_FIELD_AT_REPORT_PROBLEM_PAGE_LOC = By.xpath("//div[contains(text(),'Email')]//input[@name='email']");
	private final By PROBLEM_DD_LOC = By.id("problemReasonCode");
	private final By MESSAGE_BOX_LOC = By.id("message");
	private final By CHKBOX_OF_PRODUCT_LOC = By.id("orderEntries1");
	private final By PROBLEM_DD_FIRST_OPTION_LOC = By.xpath("//select[@id='problemReasonCode']/option[1]");
	private final By SUBMIT_BTN_LOC = By.id("reportProblemSubmit");
	private final By CONFIRMATION_MSG_OF_REPORT_PROBLEM = By.xpath("//div[@class='account-section']");
	private final By READ_OUR_RETURN_POLICY_LINK_LOC= By.xpath("//a[contains(text(),'Read our return policy')]");
	private final By EMAIL_ID_AT_REPORT_PROBLEM_PAGE_LOC = By.xpath("//div[contains(text(),'Name')]/following::div[@class='inputValue'][1]");
	private final By NAME_AT_REPORT_PROBLEM_PAGE_LOC = By.xpath("//div[contains(text(),'Name')]/following::div[@class='inputValue'][1]");
	private final By PRODUCT_NAME_AT_REPORT_PROBLEM_PAGE_LOC = By.xpath("//input[@id='orderEntries1']/following::span[2]/span");
	private final By ORDER_NUMBER_AT_REPORT_PROBLEM_PAGE_LOC = By.xpath("//span[@class='message-content']");
	private final By PRODUCT_IMG_LOC = By.xpath("//div[@class='le-checkbox']/label/span/img");
	private final By EMAIL_ID_ERROR_MSG_LOC = By.id("email-error");
	private final By PROBLEM_DD_ERROR_MSG_LOC = By.id("problemReasonCode-error");
	private final By MESSAGE_TEXTBOX_ERROR_MSG_LOC = By.id("message-error");
	private static final By EMAIL_ON_REPORT_PROBLEM_PAGE_TEXTFIELD_LOC = By.xpath("//input[@name='email']");
	private static final By SELECT_CHECKBOX_OF_PRODUCT_ERROR_MSG_LOC = By.xpath("//label[@id='orderEntries-error']");
	private String orderNumberInOrderHistory = "//div[@id='orderHistoryContentArea']//a[contains(@href,'my-account/order') and contains(text(),'%s')]";
	private String statusOfOrderFromOrderHistory = "//div[@id='orderHistoryContentArea']//a[contains(@href,'my-account/order') and contains(text(),'%s')]/ancestor::td/following-sibling::td[@class='status'][1]";
	private final By REPORT_PROBLEM_PAGE_HEADER_LOC = By.xpath("//div[contains(@class,'account-section-header')]");
	private String detailsLinkUnderOrderHistoryLoc = "//div[@id='orderHistoryContentArea']//tr[2]//a[contains(text(),'%s')]";
	private String problemDropdownOptionsLoc = "//select[@id='problemReasonCode']//option[contains(text(),%s)]";
	public  String informationAtOrderReportConfirmationPage = "//div[text()='%s:']/following::div[1]";

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
		driver.click(CHKBOX_OF_PRODUCT_LOC);
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

	/***
	 * This method clicks on Read our Return Policy Link
	 * @return
	 */
	public StoreFrontOrdersPage clickReadOurReturnPolicyLink(){
		driver.click(READ_OUR_RETURN_POLICY_LINK_LOC);
		logger.info("Read our Return Policy Link clicked");
		return this;		
	}

	/***
	 * This method checks whether Return policy document has opened or not
	 * @return
	 */
	public boolean isReturnPolicyPDFOpened(){
		String returnPolicyURL = "return-policy-for-pcperks-and-retails.pdf";
		return driver.getCurrentUrl().contains(returnPolicyURL);
	}

	/***
	 * This method validates problem dropdown reasons
	 * @param
	 * @return boolean
	 */
	public boolean isProblemDropdownOptionsPresent(String reason){
		return driver.findElement(By.xpath(String.format(problemDropdownOptionsLoc, reason))).isDisplayed();
	}

	/***
	 * This method get order number from order report problem page 
	 * 
	 * @param
	 * @return order number
	 * 
	 */
	public String getOrderNumberFromOrderReportProblemPage(){
		String orderNumber = driver.findElement(ORDER_NUMBER_AT_REPORT_PROBLEM_PAGE_LOC).getText();
		logger.info("Order number is "+orderNumber);
		return orderNumber;
	}

	/***
	 * This method get product name from order report problem page 
	 * 
	 * @param
	 * @return product name
	 * 
	 */
	public String getProductNameFromOrderReportProblemPage(){
		String productName = driver.findElement(PRODUCT_NAME_AT_REPORT_PROBLEM_PAGE_LOC).getText();
		logger.info("Product name is "+productName);
		return productName;
	}

	/***
	 * This method get name from order report problem page 
	 * 
	 * @param
	 * @return product name
	 * 
	 */
	public String getNameFromOrderReportProblemPage(){
		String name = driver.findElement(NAME_AT_REPORT_PROBLEM_PAGE_LOC).getText();
		logger.info("Name is "+name);
		return name;
	}

	/***
	 * This method get email id from order report problem page 
	 * 
	 * @param
	 * @return product name
	 * 
	 */
	public String getEmailFromOrderReportProblemPage(){
		String email = driver.findElement(EMAIL_ID_AT_REPORT_PROBLEM_PAGE_LOC).getText();
		logger.info("Email is "+email);
		return email;
	}

	/***
	 * This method get the information from order report confirmation page 
	 * 
	 * @param
	 * @return info
	 * 
	 */
	public String getInformationAtOrderReportConfirmationPage(String infoTag){
		String info = driver.findElement(By.xpath(String.format(informationAtOrderReportConfirmationPage, infoTag))).getText();
		logger.info("Info is "+info);
		return info;
	}

	/***
	 * This method get the title of orders detail page.
	 * 
	 * @param
	 * @return title of page
	 * 
	 */
	public String getTitleOfOrderDetailsPage(){
		String title = driver.getTitle();
		logger.info("Title of order detail page : "+title);
		return title;
	}

	/***
	 * This method navigate back to the Orders Page
	 * 
	 * @param
	 * @return title of page
	 * 
	 */
	public StoreFrontOrdersPage navigateBackToOrdersPage(){
		driver.navigate().back();
		logger.info("Navigated back to Orders Page");
		return this;
	}
	public String getHeaderOfPage(){
		return driver.getText(REPORT_PROBLEM_PAGE_HEADER_LOC);
	}
	/***
	 * This method fetch the status of order.
	 * 
	 * @param
	 * @return status of order
	 * 
	 */
	public String getStatusOfOrderFromOrderHistory(String orderNumber){
		String status = driver.getText(By.xpath(String.format(statusOfOrderFromOrderHistory, orderNumber)));
		logger.info("Status of order : " + orderNumber + " is " + status);
		return status;
	}
	/***
	 * This method validates the order number presence  in order history.
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isOrderPresentInOrderHistory(String orderNumber){
		return driver.isElementVisible(By.xpath(String.format(orderNumberInOrderHistory, orderNumber)));
	}
	/***
	 * This method enter the details for Report a problem excluding Product 
	 * 
	 * @param message
	 * @return store front orders page object
	 * 
	 */
	public StoreFrontOrdersPage enterTheDetailsForReportProblemExcludingProductSelection(String message){
		Select select = new Select(driver.findElement(PROBLEM_DD_LOC));
		select.selectByIndex(1);
		logger.info("First option select from problem dropdown");
		driver.type(MESSAGE_BOX_LOC, message);
		logger.info("Message typed as: "+message);
		return this;
	}


	/***
	 * This method selects the checkbox of Product to Report a problem  
	 * 
	 * @param message
	 * @return store front orders page object
	 * 
	 */
	public StoreFrontOrdersPage selectTheCheckboxOfProductForReportProblem(){
		driver.click(CHKBOX_OF_PRODUCT_LOC);
		logger.info("Check box checked for product");
		return this;
	}
	/***
	 * This method validates presence of error message for not selecting Product checkbox. 
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isErrorMsgPresentForNotSelectingProductCheckbox(){
		return driver.getText(SELECT_CHECKBOX_OF_PRODUCT_ERROR_MSG_LOC).contains("This field is required");
	}
	/***
	 * This method clears the details for Reporting problem  
	 * 
	 * @param
	 * @return store front orders page object
	 * 
	 */
	public StoreFrontOrdersPage clearTheDetailsEnteredForReportProblem(){
		driver.pauseExecutionFor(3000);
		Select select = new Select(driver.findElement(PROBLEM_DD_LOC));
		select.selectByIndex(0);
		logger.info("Unselect the Problem Dropdown");
		driver.clear(MESSAGE_BOX_LOC);
		logger.info("Cleared the message box");
		driver.clear(EMAIL_ON_REPORT_PROBLEM_PAGE_TEXTFIELD_LOC);
		logger.info("Cleared the email textbox ");
		return this;
	}
	/***
	 * This method validates presence of error message for email address. 
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isErrorMsgForEmailPresent(){
		return driver.getText(EMAIL_ID_ERROR_MSG_LOC).contains("Please enter a valid email address");
	}



	/***
	 * This method validates presence of error message for Problem Dropdown. 
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isErrorMsgForProblemDDPresent(){
		return driver.getText(PROBLEM_DD_ERROR_MSG_LOC).contains("Please select a problem");
	}


	/***
	 * This method validates presence of error message for report problem message . 
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isErrorMsgForReportProblemMessageTextFieldPresent(){
		return driver.getText(MESSAGE_TEXTBOX_ERROR_MSG_LOC).contains("This field is required.");
	}
	/***
	 * This method clicks on the image section of product for selecting checbox 
	 * 
	 * @param
	 * @return store front orders page object
	 * 
	 */
	public StoreFrontOrdersPage clickOnTheImageOfProductForSelectingChckBox(){
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(PRODUCT_IMG_LOC));
		logger.info("Product Image Clicked for Selecting checkbox");
		return this;
	}
}

