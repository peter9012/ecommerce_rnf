package com.rf.pages.website.nscore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore3HomePage extends NSCore3RFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(NSCore3HomePage.class.getName());

	public NSCore3HomePage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static String selectDDValuesInSearchByDD= "//select[contains(@id,'OrderSearchField')]//option[contains(text(),'%s')]"; 
	private static String showColumnSelectionValue= "//label[text()='%s']/preceding-sibling::input[@checked='checked'][1]";
	private static String showColumnSelectionValueChkBox= "//label[text()='%s']/preceding::input[1]";
	private static String cellValueLoc = "//td[@id='ContentCol']/table[contains(@id,'dgOrders')]//tr[%s]/td[%s]";
	private static String customerColumnHeaderLoc = "//td[@id='ContentCol']/table[contains(@id,'dgOrders')]//tr[1]/td[%s]/a";
	private static String detailsBtn = "//td[@id='ContentCol']/table[contains(@id,'dgOrders')]//tr[%s]//input[contains(@id,'btnDetails')]";
	private static String loginPageTabsLoc  = "//ul[@class='GlobalNav']//span[text()='%s']";
	private static String selectDDValue= "//select[contains(@id,'uxSearchFieldList')]//option[contains(text(),'%s')]";
	private static String customerColumnHeaderOnDistributorPageLoc = "//td[@id='ContentCol']//tr[@class='grid-header']/td[%s]/a";
	private static String firstRowValueAsPerColumnNameOnDistributorPage = "//td[@id='ContentCol']//tr[contains(@class,'GridView')]/td[%s]/a";
	private static String cellValueOnDistributorPage  = "//td[@id='ContentCol']//tr[%s]/td[%s]/a";
	private static String userTypeInConsultantDropdownOnDistributorPage = "//select[contains(@id,'_uxAccountTypes')]/option[text()='%s']";

	private static final By ORDERS_LINK = By.xpath("//a[text()='Orders']") ;
	private static final By SEARCH_BY_DD = By.xpath("//select[contains(@id,'OrderSearchField')]") ;
	private static final By SEARCH_LINK = By.xpath("//a[text()='Search']");
	private static final By SEARCH_INPUT_TEXT = By.xpath("//input[contains(@id,'tbSearchVal')]");
	private static final By SHOW_COLUMN_SELECTION = By.xpath("//a[contains(@id,'lnkShowColumns')]");
	private static final By TOTAL_NO_OF_ROWS = By.xpath("//table[contains(@id,'dgOrders')]//tr[contains(@class,'GridView')]");
	private static final By SAVE_CHANGES_LINK = By.xpath("//input[contains(@id,'btnColumns_Save')]");
	private static final By TOTAL_NO_OF_COLUMNS = By.xpath("//tr[@class='grid-header']//a");
	private static final By ORDER_NUMBER = By.xpath("//span[contains(@id,'uxOrderNumber')]");
	private static final By CUSTOMER_TYPE = By.xpath("//select[contains(@id,'uxOrderCustomerDropDown')]//option");
	private static final By SUBTOTAL_LOC = By.xpath("//span[contains(@id,'uxCustomerSubtotal')]");
	private static final By GRAND_TOTAL_LOC = By.xpath("//span[contains(@id,'uxCustomerGrandTotal')]");
	private static final By ACCOUNT_NUMBER_LOC = By.xpath("//span[contains(@id,'uxCustomerAccount')]");
	private static final By SEARCH_BY_NAME_DD = By.xpath("//select[contains(@id,'uxSearchFieldList')]") ;
	private static final By SEARCH_FOR_TXT_BOX = By.xpath("//input[contains(@id,'_uxSearchValue')][@type='text']") ;
	private static final By SEARCH_BTN = By.xpath("//input[contains(@id,'_uxSearch')][@type='button']");
	private static final By TOTAL_NO_OF_ROWS_DISTRIBUTOR_PAGE = By.xpath("//td[@id='ContentCol']//tr[contains(@class,'GridView')]");
	private static final By CONSULTANT_DROPDOWN = By.xpath("//select[contains(@id,'_uxAccountTypes')]");
	private static final By NEXT_PAGE_LINK_ON_DISTRIBUTOR_PAGE = By.xpath("//table[contains(@id,'uxDistributorGrid')]//tr[1]//a[contains(text(),'Next')]");

	public void clickOrdersLink(){
		driver.waitForElementPresent(ORDERS_LINK);
		driver.click(ORDERS_LINK);
		logger.info("Orders link clicked");
		driver.waitForPageLoad();
	}

	public void clickSearchByDD(){
		driver.waitForElementPresent(SEARCH_BY_DD);
		driver.click(SEARCH_BY_DD);
		logger.info("Search By dropdown clicked");
		driver.waitForPageLoad();
	}

	public void selectValueFromSearchByDD(String value){
		driver.waitForElementPresent(By.xpath(String.format(selectDDValuesInSearchByDD, value)));
		driver.click(By.xpath(String.format(selectDDValuesInSearchByDD, value)));
		logger.info("Dropdown value selected as: "+value);
		driver.waitForPageLoad();
	}

	public void clickSearch(){
		driver.waitForElementPresent(SEARCH_LINK);
		driver.click(SEARCH_LINK);
		logger.info("Search button clicked");
		driver.waitForPageLoad();
	}

	public void enterSearchValue(String searchValue){
		driver.waitForElementPresent(SEARCH_INPUT_TEXT);
		driver.type(SEARCH_INPUT_TEXT, searchValue);
		logger.info("search text entered as: "+searchValue);
	}

	public void clickShowColumnSlection(){
		driver.waitForElementPresent(SHOW_COLUMN_SELECTION);
		driver.click(SHOW_COLUMN_SELECTION);
		logger.info("Show column selection clicked");
		driver.waitForPageLoad();
	}

	public void isSelectionColumnValueChecked(String value){
		driver.waitForElementPresent(By.xpath(String.format(showColumnSelectionValue, value)));
		if(driver.isElementPresent(By.xpath(String.format(showColumnSelectionValue, value)))==true){
			logger.info("Column value i.e.: "+value+" already checked");
		}else{
			driver.click(By.xpath(String.format(showColumnSelectionValueChkBox, value)));
			logger.info("Column value i.e.: "+value+" checked");
		}
		driver.waitForPageLoad();
	}

	public void clickSaveChangesLink(){
		driver.waitForElementPresent(SAVE_CHANGES_LINK);
		driver.click(SAVE_CHANGES_LINK);
		logger.info("Save changes link clicked");
		driver.waitForPageLoad();
	}

	public int getRowNumberWithExpectedName(String name, int columnNumber){
		int i=0;
		int rows = driver.findElements(TOTAL_NO_OF_ROWS).size();
		for(i=2;i<=rows+1;i++){
			String nameFromUI= driver.findElement(By.xpath(String.format(cellValueLoc,i,columnNumber))).getText();
			if(nameFromUI.equals(name)){
				logger.info("return row number is: "+(i));
				return i;
			}
		} 
		logger.info("return row number with no match is: "+i);
		return i;
	}

	public boolean isExpectedValuePresentInColumn(String name, int columnNumber){
		int i=0;
		int rows = driver.findElements(TOTAL_NO_OF_ROWS).size();
		for(i=2;i<=rows+1;i++){
			String nameFromUI= driver.findElement(By.xpath(String.format(cellValueLoc,i,columnNumber))).getText();
			if(nameFromUI.equals(name)){
				logger.info(name+" FOUND in search result");
				return true;
			}
		} 
		logger.info(name+" NOT found in search result");
		return false;
	}

	public int getColumnNumberHavingExpectedColumnName(String columnName){
		int noOfColumns = driver.findElements(TOTAL_NO_OF_COLUMNS).size();
		int i =1;
		for(i=1; i<=noOfColumns; i++){
			String nameFromUI= driver.findElement(By.xpath(String.format(customerColumnHeaderLoc,i))).getText();
			if(nameFromUI.equals(columnName)){
				logger.info("return column number is: "+(i));
				return i;
			}
		} 
		logger.info("return column number with no match is: "+i);
		return i;
	}	

	public String getCellValue(int rowNumber, int columnNumber){
		String cellValue= driver.findElement(By.xpath(String.format(cellValueLoc,rowNumber,columnNumber))).getText();
		logger.info("Cell value is: "+cellValue);
		return cellValue;
	}

	public void clickDetails(int rowNumber){
		driver.click(By.xpath(String.format(detailsBtn,rowNumber)));
		logger.info("Details btn clicked at row number: "+rowNumber);
		driver.waitForPageLoad();
	}

	public String getOrderNumberFromOrderDetails(){
		driver.waitForElementPresent(ORDER_NUMBER);
		String orderNumber = driver.findElement(ORDER_NUMBER).getText();
		logger.info("Order number at order details: "+orderNumber);
		return orderNumber;
	}

	public boolean isColumnNamePresent(String columnName){
		int noOfColumns = driver.findElements(TOTAL_NO_OF_COLUMNS).size();
		int i =1;
		for(i=1; i<=noOfColumns; i++){
			String nameFromUI= driver.findElement(By.xpath(String.format(customerColumnHeaderLoc,i))).getText();
			if(nameFromUI.equals(columnName)){
				return true;
			}
		} 
		return false;
	}

	public String getCustomerTypeFromOrderDetails(){
		driver.waitForElementPresent(CUSTOMER_TYPE);
		String customerType = driver.findElement(CUSTOMER_TYPE).getText();
		logger.info("Customer type at order details: "+customerType);
		return customerType;
	}

	public String getSubTotalFromOrderDetails(){
		driver.waitForElementPresent(SUBTOTAL_LOC);
		String subTotal = driver.findElement(SUBTOTAL_LOC).getText();
		logger.info("Subtotal at order details: "+subTotal);
		return subTotal;
	}

	public String getgrandTotalFromOrderDetails(){
		driver.waitForElementPresent(GRAND_TOTAL_LOC);
		String grandTotal = driver.findElement(GRAND_TOTAL_LOC).getText();
		logger.info("Grand total at order details: "+grandTotal);
		return grandTotal;
	}

	public void clickShowColumnSelection(){
		driver.waitForElementPresent(SHOW_COLUMN_SELECTION);
		driver.click(SHOW_COLUMN_SELECTION);
		logger.info("Show column selection clicked");
		driver.waitForPageLoad();
	}

	public String getAccountNumberFromOrderDetails() {
		driver.waitForElementPresent(ACCOUNT_NUMBER_LOC);
		String accountNumber = driver.findElement(ACCOUNT_NUMBER_LOC).getText();
		logger.info("Account number at order details: "+accountNumber);
		return accountNumber;
	}

	public void clickTab(String tabName){
		driver.waitForElementPresent(By.xpath(String.format(loginPageTabsLoc, tabName)));
		driver.click(By.xpath(String.format(loginPageTabsLoc, tabName)));
		logger.info("Tab: "+tabName+" is clicked");
		driver.waitForPageLoad();
	}

	public void selectValueFromNameDDOnDistributorTab(String value){
		driver.waitForElementPresent(SEARCH_BY_NAME_DD);
		driver.click(SEARCH_BY_NAME_DD);
		logger.info("Name dropdown is clicked");
		driver.waitForElementPresent(By.xpath(String.format(selectDDValue, value)));
		driver.click(By.xpath(String.format(selectDDValue, value)));
		logger.info("Dropdown value selected as: "+value);
		driver.waitForPageLoad();
	}

	public void enterSearchForFieldOnDistributorTab(String inputTxt){
		driver.waitForElementPresent(SEARCH_FOR_TXT_BOX);
		driver.type(SEARCH_FOR_TXT_BOX, inputTxt);
		logger.info("Search for text entered as: "+inputTxt);
		driver.waitForPageLoad();
	}

	public void clickSearchOnDistributorTab(){
		driver.waitForElementPresent(SEARCH_BTN);
		driver.click(SEARCH_BTN);
		logger.info("Search button clicked");
		driver.waitForPageLoad();
	}

	public int getColumnNumberHavingExpectedColumnNameOnDistributorPage(String columnName){
		int noOfColumns = driver.findElements(TOTAL_NO_OF_COLUMNS).size();
		int i =1;
		for(i=1; i<=noOfColumns; i++){
			String nameFromUI= driver.findElement(By.xpath(String.format(customerColumnHeaderOnDistributorPageLoc,i))).getText();
			if(nameFromUI.equals(columnName)){
				logger.info("return column number is: "+(i));
				return i;
			}
		} 
		logger.info("return column number with no match is: "+i);
		return i;
	}

	public String getFirstRowCellValueAsPerColumnName(int columnNumber){
		String cellValue= driver.findElement(By.xpath(String.format(firstRowValueAsPerColumnNameOnDistributorPage,columnNumber))).getText();
		logger.info("Cell value is: "+cellValue);
		return cellValue;
	}

	public boolean isAllRowsContainsTheCompleteName(String completeName){
		int i=0;
		boolean flag =false;
		int rows = driver.findElements(TOTAL_NO_OF_ROWS_DISTRIBUTOR_PAGE).size();
		int firstNameColNum = getColumnNumberHavingExpectedColumnNameOnDistributorPage("First Name");
		int lastNameColNum = getColumnNumberHavingExpectedColumnNameOnDistributorPage("Last Name");
		for(i=3;i<rows+3;i++){
			String firstName = driver.findElement(By.xpath(String.format(cellValueOnDistributorPage,i,firstNameColNum))).getText();
			String lastName = driver.findElement(By.xpath(String.format(cellValueOnDistributorPage,i,lastNameColNum))).getText();
			String completeNameString = firstName+" "+lastName;
			if(completeName.trim().equalsIgnoreCase(completeNameString.trim())){
				flag =true;
			}else{
				logger.info("complete name on UI is different from database expected:"+completeName+" Actual: "+completeNameString);
				flag=false;
				break;
			}
		}
		return flag;
	}

	public boolean isRowsContainsTheCompleteName(String completeName){
		int i=3;
		boolean flag =false;
		int firstNameColNum = getColumnNumberHavingExpectedColumnNameOnDistributorPage("First Name");
		int lastNameColNum = getColumnNumberHavingExpectedColumnNameOnDistributorPage("Last Name");
		String firstName = driver.findElement(By.xpath(String.format(cellValueOnDistributorPage,i,firstNameColNum))).getText();
		String lastName = driver.findElement(By.xpath(String.format(cellValueOnDistributorPage,i,lastNameColNum))).getText();
		String completeNameString = firstName+" "+lastName;
		if(completeName.trim().equalsIgnoreCase(completeNameString.trim())){
			flag =true;
		}else{
			logger.info("complete name on UI is different from database expected:"+completeName+" Actual: "+completeNameString);
			flag=false;
		}
		return flag;
	}

	public void selectUserTypeFromDDOnDistributorTab(String UserType){
		driver.waitForElementPresent(CONSULTANT_DROPDOWN);
		driver.click(CONSULTANT_DROPDOWN);
		logger.info("Consultant dropdown is clicked");
		driver.waitForElementPresent(By.xpath(String.format(userTypeInConsultantDropdownOnDistributorPage, UserType)));
		driver.click(By.xpath(String.format(userTypeInConsultantDropdownOnDistributorPage, UserType)));
		logger.info("Dropdown value selected User as: "+UserType);
		driver.waitForPageLoad();
	}

	public boolean isAllRowsContainsTheAccountType(String accountType){
		int i=0;
		boolean flag =false;
		int rows = driver.findElements(TOTAL_NO_OF_ROWS_DISTRIBUTOR_PAGE).size();
		int accountTypeColNum = getColumnNumberHavingExpectedColumnNameOnDistributorPage("Account Type");
		for(i=3;i<rows+3;i++){
			String accountTypeValue = driver.findElement(By.xpath(String.format(cellValueOnDistributorPage,i,accountTypeColNum))).getText();
			if(accountType.trim().equalsIgnoreCase(accountTypeValue.trim())){
				flag =true;
			}else{
				logger.info("Account type on UI is different from database expected:"+accountType+" Actual: "+accountTypeValue);
				flag=false;
				break;
			}
		}
		return flag;
	}

	public String getCellValueOnDistributorPage(int rowNumber, int columnNumber){
		String cellValue= driver.findElement(By.xpath(String.format(cellValueOnDistributorPage,rowNumber,columnNumber))).getText();
		logger.info("Cell value is: "+cellValue);
		return cellValue;
	}

	public String getRowNumberHavingTheCompleteName(String completeName){
		int i=0;
		boolean flag =false;
		while(true){
			int rows = driver.findElements(TOTAL_NO_OF_ROWS_DISTRIBUTOR_PAGE).size();
			int firstNameColNum = getColumnNumberHavingExpectedColumnNameOnDistributorPage("First Name");
			int lastNameColNum = getColumnNumberHavingExpectedColumnNameOnDistributorPage("Last Name");
			for(i=3;i<rows+3;i++){
				flag= false;
				String firstName = driver.findElement(By.xpath(String.format(cellValueOnDistributorPage,i,firstNameColNum))).getText();
				String lastName = driver.findElement(By.xpath(String.format(cellValueOnDistributorPage,i,lastNameColNum))).getText();
				String completeNameString = firstName+" "+lastName;
				if(completeName.trim().equalsIgnoreCase(completeNameString.trim())){
					logger.info("complete name from UI and db match for row "+(i-2));
					break;
				}else{
					flag=true;
				}
			}if(flag == false){
				break;
			}else{
				if(driver.isElementPresent(NEXT_PAGE_LINK_ON_DISTRIBUTOR_PAGE)==true){
					driver.click(NEXT_PAGE_LINK_ON_DISTRIBUTOR_PAGE);
					driver.waitForNSCore4LoadingImageToDisappear();
					logger.info("Next button clicked");
				}else{
					logger.info("None of rows on all pages contains the complete name string.");
					break;
				}
			}
		}
		logger.info("Row number is: "+(i-2));
		logger.info("return value of i is: "+i);
		return ""+i;
	}

}