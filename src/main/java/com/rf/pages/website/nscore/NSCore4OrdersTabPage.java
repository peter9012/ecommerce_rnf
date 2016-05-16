package com.rf.pages.website.nscore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore4OrdersTabPage extends NSCore4RFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(NSCore4OrdersTabPage.class.getName());

	public NSCore4OrdersTabPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static String productsInfoTableLoc = "//td[contains(text(),'%s')]/following-sibling::td";
	private static String FirstNameLoc = "//table[@id='orders']//tr[@class='GridColHead']/th[2]//following::tbody/tr[%s]//td[2]";
	private static String LastNameLoc = "//table[@id='orders']//tr[@class='GridColHead']/th[2]//following::tbody/tr[%s]//td[3]";
	private static String orderStatusLoc = "//table[@id='orders']//tr[@class='GridColHead']/th[2]//following::tbody/tr[%s]//td[4]";
	private static String orderTypeLoc = "//table[@id='orders']//tr[@class='GridColHead']/th[2]//following::tbody/tr[%s]//td[5]";
	private static String suggestionStartOrderLoc = "//div[contains(@class,'resultItem')]//p[contains(text(),'%s')]";

	private static final By TOTAL_AMT_TO_BE_REFUNDED_LOC = By.xpath("//b[contains(text(),'Total Amount To Be Refunded')]/following-sibling::b");
	private static final By REFUNDED_SHIPPING_LOC = By.xpath("//input[@id='shippingRefunded']");
	private static final By REFUNDED_HANDLING_LOC = By.xpath("//input[@id='handlingRefunded']");
	private final static By CANCEL_ORDER_LINK_LOC = By.xpath("//a[@id='cancelOrder']");
	private final static By ORDER_STATUS_LOC = By.xpath("//div[@class='Content']//td[contains(text(),'Status')]/following::b[1]");
	private final static By ORDER_ID_INPUT_FIELD_LOC = By.xpath("//input[@id='txtSearch']");
	private static final By GO_SEARCH_BTN = By.xpath("//a[@id='btnGo']/img[@alt='Go']");
	private static final By DROP_DOWN_LOC = By.xpath("//select[@id='cboSearchCol']");
	private static final By FIRST_NAME  = By.xpath("//table[@id='orders']//tr[1]/td[2]/a");
	private static final By LAST_NAME  = By.xpath("//table[@id='orders']//tr[1]/td[3]/a");	 
	private static final By ADVANCED_SEARCH_INPUT_FIELD_LOC = By.xpath("//input[@id='txtAdvancedSearch']");
	private static final By ADVANCED_SEARCH_GO_BTN = By.xpath("//a[@id='btnAdvancedGo']");
	private static final By TOTAL_ROWS_SEARCH_RESULT_LOC = By.xpath("//table[@id='orders']/tbody/tr");
	private static final By START_A_NEW_ORDER_LINK_LOC = By.xpath("//span[text()='Start a New Order']");
	private static final By TXT_FIELD_START_ORDER_LOC = By.id("txtCustomerSuggest");
	private static final By SUGGESTION_START_ORDER_LOC = By.xpath("//div[@class='resultItem odd hover']");
	private static final By START_ORDER_BTN_LOC = By.xpath("//a[@id='btnStartOrder']");
	private final static By BROWSE_ORDERS_LINK_LOC = By.xpath("//a[text()='Browse Orders']");
	private final static By SELECT_STATUS_DD_LOC = By.xpath("//select[@id='statusFilter']");
	private final static By SELECT_TYPE_DD_LOC = By.xpath("//select[@id='typeFilter']");
	private static final By GO_SEARCH_BTN_BROWSE_ORDERS_LOC = By.xpath("//a[@id='btnSearchAccounts']/img");
	private static final By PAYMENT_APPLY_LINK_LOC = By.xpath("//a[text()='Apply']");
	private static final By RETURN_ORDER_LOC  = By.id("returnOrder");
	private static final By UPDATE_LINK_LOC  = By.id("btnUpdate");
	private static final By SUBMIT_RETURN_BTN_LOC  = By.id("btnSubmit");
	private static final By GET_ORDER_TYPE  = By.xpath("//p[contains(text(),'Order Type')]");
	private static final By RETURNED_ORDER_CHKBOX_LOC = By.xpath("//input[@class='returned']");
	private static final By RETURN_QUANTITY_INPUT_TXT_LOC = By.xpath("//input[contains(@class,'returnQuantity')]");
	private static final By RETURN_QUANTITY_LOC = By.xpath("//td[contains(text(),'Products')]/following::td[1]//tr[@class='GridRow']/td[4]");
	private static final By RESTOCKING_FEE_TEXT_LOC = By.xpath("//td[contains(text(),'Products')]/following::td[1]//td[contains(text(),'Restocking Fee')]");
	private static final By RESTOCKING_FEE_PERCENT_TXT_BOX_LOC  = By.id("restockingPercent");


	public void clickCancelOrderBtn(){
		driver.waitForElementPresent(CANCEL_ORDER_LINK_LOC);
		driver.click(CANCEL_ORDER_LINK_LOC);
		logger.info("cancel order link is clicked");
		driver.pauseExecutionFor(3000);
		driver.waitForPageLoad();
	}

	public boolean validateOrderStatus() {
		String status = driver.findElement(ORDER_STATUS_LOC).getText();
		if(status.equalsIgnoreCase("Cancelled")){
			return true;
		}return false;
	}

	public void enterOrderIDInInputField(String orderId) {
		driver.quickWaitForElementPresent(ORDER_ID_INPUT_FIELD_LOC);
		driver.clear(ORDER_ID_INPUT_FIELD_LOC);
		driver.type(ORDER_ID_INPUT_FIELD_LOC, orderId);
		logger.info("orderId field enterd as: "+orderId);
		driver.click(GO_SEARCH_BTN);
		logger.info("Search Go button clicked");
		driver.waitForPageLoad();
	}

	public boolean isOrderDetailPagePresent() {
		String url = driver.getCurrentUrl();
		return url.contains("Orders/Details");

	}

	public boolean isOrderInformationPresent(String InfoSection) {
		return driver.isElementPresent(By.xpath(String.format(productsInfoTableLoc,InfoSection)));
	}

	public void selectDropDownAdvancedSearch(String text) {
		Select dropDown = new Select(driver.findElement(DROP_DOWN_LOC));
		dropDown.selectByVisibleText(text);
		logger.info("drop down selected value is: "+text);
	}

	public void enterValueInAdvancedSearchInputField(String value) {
		driver.quickWaitForElementPresent(ADVANCED_SEARCH_INPUT_FIELD_LOC);
		driver.clear(ADVANCED_SEARCH_INPUT_FIELD_LOC);
		driver.type(ADVANCED_SEARCH_INPUT_FIELD_LOC, value);
		logger.info("advanced search field enterd as: "+value);
		driver.click(ADVANCED_SEARCH_GO_BTN);
		logger.info("Advanced Search Go button clicked");
		driver.waitForPageLoad();
	}

	public boolean isSearchResultFirstName(String firstName){
		boolean status = false;
		driver.waitForElementPresent(TOTAL_ROWS_SEARCH_RESULT_LOC);
		int totalSearchResult = driver.findElements(TOTAL_ROWS_SEARCH_RESULT_LOC).size();
		for(int i=1;i<=totalSearchResult;i++){
			String firstNameUI = driver.findElement(By.xpath(String.format(FirstNameLoc,i))).getText();
			if(firstNameUI.equalsIgnoreCase(firstName)){
				status = true;
				continue;
			}
			else{
				status = false;
				break;
			}

		}
		return status;
	}

	public boolean isSearchResultLastName(String lastName){
		boolean status = false;
		driver.waitForElementPresent(TOTAL_ROWS_SEARCH_RESULT_LOC);
		int totalSearchResult = driver.findElements(TOTAL_ROWS_SEARCH_RESULT_LOC).size();
		for(int i=1;i<=totalSearchResult;i++){
			String lastNameUI = driver.findElement(By.xpath(String.format(LastNameLoc,i))).getText();
			if(lastNameUI.equalsIgnoreCase(lastName)){
				status = true;
				continue;
			}
			else{
				status = false;
				break;
			}
		}
		return status;
	}

	public void clickStartANewOrderLink() {
		driver.quickWaitForElementPresent(START_A_NEW_ORDER_LINK_LOC);
		driver.click(START_A_NEW_ORDER_LINK_LOC);
		logger.info("start a new order link in order tab clicked");

	}

	public void enterAccountNameAndClickStartOrder(String accountName) {
		driver.quickWaitForElementPresent(TXT_FIELD_START_ORDER_LOC);
		driver.type(TXT_FIELD_START_ORDER_LOC, accountName);
		logger.info("start order text field entered by: "+accountName);
		driver.waitForElementPresent(SUGGESTION_START_ORDER_LOC);
		driver.click(SUGGESTION_START_ORDER_LOC);
		driver.click(START_ORDER_BTN_LOC);
		logger.info("start button is clicked after entered account name");
	}

	public void clickBrowseOrdersLlink(){
		driver.waitForElementPresent(BROWSE_ORDERS_LINK_LOC);
		driver.click(BROWSE_ORDERS_LINK_LOC);
		logger.info("Browse Orders link is clicked");
		driver.waitForPageLoad();
	}

	public void selectStatusDD(String Status){
		Select sel =new Select(driver.findElement(SELECT_STATUS_DD_LOC));
		sel.selectByVisibleText(Status);
	}

	public void selectTypeDD(String Type){
		Select sel =new Select(driver.findElement(SELECT_TYPE_DD_LOC));
		sel.selectByVisibleText(Type);
	}

	public void clickGoSearchBtn(){
		driver.waitForElementPresent(GO_SEARCH_BTN_BROWSE_ORDERS_LOC);
		driver.click(GO_SEARCH_BTN_BROWSE_ORDERS_LOC);
		logger.info("'GO' Search Btn is clicked");
		driver.pauseExecutionFor(3000);
		driver.waitForPageLoad();
	}

	public boolean validateOrderStatusRow(){
		boolean status = true;
		driver.waitForElementPresent(TOTAL_ROWS_SEARCH_RESULT_LOC);
		int totalSearchResult = driver.findElements(TOTAL_ROWS_SEARCH_RESULT_LOC).size();
		//driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		for(int i=2;i<=totalSearchResult;i++){
			String actualOrderStatus= driver.findElement(By.xpath(String.format(orderStatusLoc,i))).getText();
			if(actualOrderStatus.equalsIgnoreCase("Pending")){
				continue;
			}else{
				status = false;
				break;
			}
		}
		return status;
	}

	public boolean validateOrderTypeRow(){
		boolean status = true;
		driver.waitForElementPresent(TOTAL_ROWS_SEARCH_RESULT_LOC);
		int totalSearchResult = driver.findElements(TOTAL_ROWS_SEARCH_RESULT_LOC).size();
		//driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		for(int i=2;i<=totalSearchResult;i++){
			String actualOrderType= driver.findElement(By.xpath(String.format(orderTypeLoc,i))).getText();
			if(actualOrderType.equalsIgnoreCase("PC")){
				continue;
			}else{
				status = false;
				break;
			}
		}
		return status;
	}

	public void clickPaymentApplyLink() {
		driver.quickWaitForElementPresent(PAYMENT_APPLY_LINK_LOC);
		driver.click(PAYMENT_APPLY_LINK_LOC);
		logger.info("payment apply link is clicked");
	}

	public boolean validateOrderStatusAfterSubmitOrder(){
		String status = driver.findElement(ORDER_STATUS_LOC).getText();
		if(status.equalsIgnoreCase("Submitted")){
			return true;
		}return false;
	}

	public void clickReturnOrderLink(){
		driver.waitForElementPresent(RETURN_ORDER_LOC);
		driver.click(RETURN_ORDER_LOC);
		logger.info("Return order link clicked");
		driver.waitForPageLoad();
	}

	public void clickUpdateLink(){
		driver.waitForElementPresent(UPDATE_LINK_LOC);
		driver.click(UPDATE_LINK_LOC);
		logger.info("Update link clicked");
		driver.waitForNSCore4LoadingImageToDisappear();
		driver.pauseExecutionFor(1000);
	}

	public void clickSubmitReturnBtn(){
		driver.waitForElementPresent(SUBMIT_RETURN_BTN_LOC);
		driver.click(SUBMIT_RETURN_BTN_LOC);
		logger.info("submit return btn clicked");
		driver.waitForNSCore4ProcessImageToDisappear();
	}

	public String getOrderType(){
		driver.waitForElementPresent(GET_ORDER_TYPE);
		String orderType = driver.findElement(GET_ORDER_TYPE).getText();
		logger.info("Order type is "+orderType);
		return orderType;
	}

	public void enableReturnedChkBox(){
		driver.quickWaitForElementPresent(RETURNED_ORDER_CHKBOX_LOC);
		driver.click(RETURNED_ORDER_CHKBOX_LOC);
		logger.info("Returned Order CheckBox is Enabled");
		driver.pauseExecutionFor(2000);
	}

	public void enterReturnQuantity(String quantity){
		driver.waitForElementPresent(RETURN_QUANTITY_INPUT_TXT_LOC);
		driver.type(RETURN_QUANTITY_INPUT_TXT_LOC, quantity);
		logger.info("Return quantity entered as: "+quantity);
	}

	public String getReturnQuantityOfProduct(){
		driver.waitForElementPresent(RETURN_QUANTITY_LOC);
		String quantity = driver.findElement(RETURN_QUANTITY_LOC).getText();
		logger.info("Return quantity of product is: "+quantity);
		return quantity;
	}

	public boolean isRestockingFeeTxtPresent(){
		driver.isElementPresent(RESTOCKING_FEE_TEXT_LOC);
		return driver.isElementPresent(RESTOCKING_FEE_TEXT_LOC);
	}

	public void enterRestockingFeeInPercent(String fee){
		driver.waitForElementPresent(RESTOCKING_FEE_PERCENT_TXT_BOX_LOC);
		driver.type(RESTOCKING_FEE_PERCENT_TXT_BOX_LOC, fee);
		logger.info("Restocking fee in % entered as: "+fee);
	}

	public void enterAccountNameAndClickStartOrder(String accountName, String accountNumber) {
		driver.quickWaitForElementPresent(TXT_FIELD_START_ORDER_LOC);
		driver.type(TXT_FIELD_START_ORDER_LOC, accountName);
		logger.info("start order text field entered by: "+accountName);
		driver.waitForElementPresent(By.xpath(String.format(suggestionStartOrderLoc, accountNumber)));
		driver.click(By.xpath(String.format(suggestionStartOrderLoc, accountNumber)));
		driver.click(START_ORDER_BTN_LOC);
		logger.info("start button is clicked after entered account name");
	}

	public String getCompleteNameFromFirstRow(){
		driver.waitForElementPresent(FIRST_NAME);
		String firstName = driver.findElement(FIRST_NAME).getText();
		logger.info("First name is: "+firstName);
		String lastName = driver.findElement(LAST_NAME).getText();
		String completeName = firstName+" "+lastName;
		logger.info("Last name is: "+lastName);
		logger.info("Complete name is: "+completeName);
		return completeName;
	}


	public boolean isTotalAmountToBeRefundedPresent(){
		driver.quickWaitForElementPresent(TOTAL_AMT_TO_BE_REFUNDED_LOC);
		return driver.isElementPresent(TOTAL_AMT_TO_BE_REFUNDED_LOC);
	}

	public double getRefundedShippingChargesValue(){
		driver.quickWaitForElementPresent(REFUNDED_SHIPPING_LOC);
		//double value=Double.parseDouble(driver.findElement(REFUNDED_SHIPPING_LOC).getAttribute("value"));
		double value=Double.parseDouble(driver.findElement(REFUNDED_SHIPPING_LOC).getText());
		return value;
	}

	public double getRefundedHandlingChargesValue(){
		driver.quickWaitForElementPresent(REFUNDED_HANDLING_LOC);
		//double value=Double.parseDouble(driver.findElement(REFUNDED_HANDLING_LOC).getAttribute("value"));
		double value=Double.parseDouble(driver.findElement(REFUNDED_HANDLING_LOC).getText());
		return value;
	}

	//	public void updateRefundedShippingChargesLowerValue(String value){
	//		driver.waitForElementPresent(REFUNDED_SHIPPING_LOC);
	//		driver.clear(REFUNDED_SHIPPING_LOC);
	//		driver.findElement(REFUNDED_SHIPPING_LOC).sendKeys(value);
	//		driver.pauseExecutionFor(1000);
	//		driver.findElement(REFUNDED_SHIPPING_LOC).sendKeys("\t");
	//		driver.pauseExecutionFor(1000);
	//		logger.info("Refunded_Shipping value updated as"+value);
	//	}

	public void updateRefundedShippingChargesLowerValue(String value){
		driver.waitForElementPresent(REFUNDED_SHIPPING_LOC);
		JavascriptExecutor myExecutor = ((JavascriptExecutor) RFWebsiteDriver.driver);
		while(true){
			try{
				while(driver.findElement(REFUNDED_SHIPPING_LOC).getAttribute("value").length() > 0) {
					driver.findElement(REFUNDED_SHIPPING_LOC).sendKeys(Keys.BACK_SPACE);
				}
				myExecutor.executeScript("arguments[0].value='00';", driver.findElement(REFUNDED_SHIPPING_LOC));
				driver.pauseExecutionFor(1000);
				//driver.findElement(REFUNDED_HANDLING_LOC).sendKeys("\t");
				driver.findElement(By.xpath("//b[contains(text(),'Total Amount To')]")).click();
				driver.pauseExecutionFor(1000);
				logger.info("Refunded Shipping value updated as"+value);
				break;
			}catch(Exception e){
				continue;
			}
		}
	}

	public void updateRefundedHandlingChargesLowerValue(String value){
		driver.waitForElementPresent(REFUNDED_HANDLING_LOC);
		JavascriptExecutor myExecutor = ((JavascriptExecutor) RFWebsiteDriver.driver);
		while(true){
			try{
				while(driver.findElement(REFUNDED_HANDLING_LOC).getAttribute("value").length() > 0) {
					driver.findElement(REFUNDED_HANDLING_LOC).sendKeys(Keys.BACK_SPACE);
				}
				myExecutor.executeScript("arguments[0].value='00';", driver.findElement(REFUNDED_HANDLING_LOC));
				driver.pauseExecutionFor(1000);
				//driver.findElement(REFUNDED_HANDLING_LOC).sendKeys("\t");
				driver.findElement(By.xpath("//b[contains(text(),'Total Amount To')]")).click();
				driver.pauseExecutionFor(1000);
				logger.info("Refunded handling value updated as"+value);
				break;
			}catch(Exception e){
				continue;
			}
		}
	}

	public void updateRefundedShippingChargesValue(String value){
		driver.waitForElementPresent(REFUNDED_SHIPPING_LOC);
		driver.findElement(REFUNDED_SHIPPING_LOC).sendKeys(value);
		driver.pauseExecutionFor(1000);
		driver.findElement(REFUNDED_SHIPPING_LOC).sendKeys("\t");
		driver.pauseExecutionFor(1000);
		logger.info("Refunded_Shipping value updated as"+value);
	}

	public void updateRefundedHandlingChargesValue(String value){
		driver.waitForElementPresent(REFUNDED_HANDLING_LOC);
		driver.findElement(REFUNDED_HANDLING_LOC).sendKeys(value);
		driver.pauseExecutionFor(1000);
		driver.findElement(REFUNDED_HANDLING_LOC).sendKeys("\t");
		driver.pauseExecutionFor(1000);
		logger.info("Refunded_Handling value updated as"+value);  
	}

}