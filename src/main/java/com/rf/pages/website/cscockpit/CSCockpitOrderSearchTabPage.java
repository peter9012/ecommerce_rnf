package com.rf.pages.website.cscockpit;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;

public class CSCockpitOrderSearchTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitOrderSearchTabPage.class.getName());

	private static String orderTypeValueLoc = "//span[contains(text(),'Order Type')]/select/option[contains(text(),'%s')]";
	private static String orderStatusValueLoc = "//span[contains(text(),'Order Status')]/select/option[contains(text(),'%s')]";
	private static String shipToCountryValueLoc = "//span[contains(text(),'Ship To Country')]/select/option[contains(text(),'%s')]";
	private static String orderNumberFromOrdersResultLoc = "//div[@class='csSearchContainer']//div[@class='csListboxContainer']//div[@class='z-listbox-body']//tbody[2]/tr[%s]//a";
	private static String orderTypeOptionLoc = "//span[contains(@class,'orderSearchType')]//option[text()='%s']";
	private static String orderStatusOptionLoc = "//span[contains(text(),'Order Status')]//option[text()='%s']";
	private static String orderStatusLoc = "//a[text()='%s']//following::td[7]//span";	
	private static String orderTypeDDloc = "//span[contains(text(),'Order Type')]//option[text()='%s']";
	private static String customerLastNameInSearchResultsLoc = "//div[@class='csListboxContainer']/descendant::table[2]/tbody[2]/tr[%s]/td[4]//span";
	private static String anotherCustomerEmailIdInSearchResultsLoc = "//div[@class='csListboxContainer']/descendant::table[2]/tbody[2]/tr[%s]/td[6]//span";
	private static String countryDDLoc = "//span[contains(text(),'Country')]/select/option[text()='%s']";
	private static String orderStatusCorrespondingOrderLoc = "//div[@class='csListboxContainer']/descendant::table[2]/tbody[2]/tr[%s]/td[8]//span";
	private static String customerFirstNameInSearchResultsLoc = "//div[@class='z-listbox-body']/descendant::table/tbody[2]/tr[%s]/td[3]//span";
	private static String orderSearchLabel = "//div[text()='%s']";

	private static final By SEARCH_BTN = By.xpath("//td[text()='Search']"); 
	private static final By TOTAL_ORDERS_ON_PLACED_ORDER_DETAILS = By.xpath("//div[@class='orderDetailOrderItemsWidget']//div[@class='z-listbox-body']/table/tbody[2]/tr");
	private static final By ORDERS_DETAIL_ITEMS_LBL = By.xpath("//span[contains(text(),'Order Detail Items')]");
	private static final By TOTAL_ORDERS_FROM_ORDER_SEARCH_RESULT = By.xpath("//div[@class='csSearchContainer']//div[@class='csListboxContainer']//div[@class='z-listbox-body']//tbody[2]/tr");
	private static final By ORDER_NUMBER_TXT_FIELD_ORDER_SEARCH_TAB = By.xpath("//span[text()='Order Number']/following::input[1]");
	private static final By CID_CUSTOMER_NAME_TXT_FIELD = By.xpath("//span[contains(text(),'Customer Name or CID')]/following::input[1]");
	private static final By CUSTOMER_NAME_OR_CID_TXT_FIELD = By.xpath("//span[contains(text(),'Customer Name or CID')]/following::input[1]");
	private static final By ORDER_LINK_SECTION_LOCATOR = By.xpath("//div[contains(@class,'listbox-body')]/table/tbody[2]/tr[1]/td[1]//a");
	private static final By INVALID_CID_SEARCH_RESULT = By.xpath("//span[text()='No Results']");
	private static final By ORDER_SEARCH_LBL = By.xpath("//div[contains(@class,'csContentArea')]//span[text()='Order Search']");
	private static final By ORDER_TYPE_DD = By.xpath("//span[contains(@class,'orderSearchType')]/select");
	private static final By ORDER_STATUS_DD = By.xpath("//span[contains(text(),'Order Status')]/select");
	private static final By SHIP_TO_COUNTRY_DD = By.xpath("//span[contains(text(),'Ship To Country')]/select");
	protected RFWebsiteDriver driver;

	public CSCockpitOrderSearchTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean isOrderSearchPageDisplayed(){
		driver.waitForElementPresent(ORDER_SEARCH_LBL);
		return driver.IsElementVisible(driver.findElement(ORDER_SEARCH_LBL));
	}

	public void clickSearchBtn(){
		driver.quickWaitForElementPresent(SEARCH_BTN);
		driver.click(SEARCH_BTN);
		logger.info("Search button clicked");
		driver.waitForCSCockpitLoadingImageToDisappear();		
	}

	public boolean isOrderTypeDDDisplayedOnOrderSearchTab(){
		return driver.IsElementVisible(driver.findElement(ORDER_TYPE_DD));
	}

	public boolean isShipToCountryDDDisplayedOnOrderSearchTab(){
		return driver.IsElementVisible(driver.findElement(SHIP_TO_COUNTRY_DD));
	}

	public boolean isOrderStatusDDDisplayedOnOrderSearchTab(){
		return driver.IsElementVisible(driver.findElement(ORDER_STATUS_DD));
	}

	public boolean isOrderTypeDDValuePresentOnOrderSearchTab(String orderTypeValue){
		return driver.isElementPresent(By.xpath(String.format(orderTypeValueLoc, orderTypeValue)));	
	}

	public boolean isOrderStatusDDValuePresentOnOrderSearchTab(String orderStatusValue){
		return driver.isElementPresent(By.xpath(String.format(orderStatusValueLoc, orderStatusValue)));	
	}

	public boolean isShipToCountryDDValuePresentOnOrderSearchTab(String shipToCountryValue){
		return driver.isElementPresent(By.xpath(String.format(shipToCountryValueLoc, shipToCountryValue)));	
	}

	public String getFirstOrderNumberFromResultInOrderSearchTab(){
		driver.waitForElementPresent(By.xpath("//div[@class='csListboxContainer']//div[@class='z-listbox-body']//tbody[2]/tr[1]/td[1]//a"));
		return driver.findElement(By.xpath("//div[@class='csListboxContainer']//div[@class='z-listbox-body']//tbody[2]/tr[1]/td[1]//a")).getText();
	}

	public void selectOrderTypeOnOrderSearchTab(String orderType){
		driver.waitForElementPresent(ORDER_TYPE_DD);
		driver.click(ORDER_TYPE_DD);
		driver.click(By.xpath(String.format(orderTypeOptionLoc, orderType)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void selectOrderStatusOnOrderSearchTab(String orderStatus){
		driver.waitForElementPresent(ORDER_STATUS_DD);
		driver.click(ORDER_STATUS_DD);
		driver.click(By.xpath(String.format(orderStatusOptionLoc, orderStatus)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void enterCIDOnOrderSearchTab(String CID){
		driver.waitForElementPresent(CID_CUSTOMER_NAME_TXT_FIELD);
		driver.type(CID_CUSTOMER_NAME_TXT_FIELD, CID);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isOrderNumberTxtFieldDisplayedInOrderSearchTab(){
		return driver.IsElementVisible(driver.findElement(ORDER_NUMBER_TXT_FIELD_ORDER_SEARCH_TAB));
	}

	public void enterOrderNumberInOrderSearchTab(String orderNumber){
		driver.waitForElementPresent(ORDER_NUMBER_TXT_FIELD_ORDER_SEARCH_TAB);
		driver.type(ORDER_NUMBER_TXT_FIELD_ORDER_SEARCH_TAB, orderNumber);
		logger.info("Order number entered for search is "+orderNumber);
	}

	public int getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab(){
		int totalOrdersFromSearchResultFirstPage = driver.findElements(TOTAL_ORDERS_FROM_ORDER_SEARCH_RESULT).size();
		int randomOrderFromSearchResult = CommonUtils.getRandomNum(1, totalOrdersFromSearchResultFirstPage);
		logger.info("Random Order sequence number is "+randomOrderFromSearchResult);
		return randomOrderFromSearchResult;
	}

	public int getTotalOrderSearchResultsInOrderSearchTab(){
		driver.waitForElementPresent(TOTAL_ORDERS_FROM_ORDER_SEARCH_RESULT);
		return driver.findElements(TOTAL_ORDERS_FROM_ORDER_SEARCH_RESULT).size();
	}

	public void clickOrderNumberInOrderSearchResultsInOrderSearchTab(String orderSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderNumberFromOrdersResultLoc,orderSequenceNumber)));
		driver.click(By.xpath(String.format(orderNumberFromOrdersResultLoc,orderSequenceNumber)));
		driver.waitForCSCockpitLoadingImageToDisappear();

	}

	public int clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(String orderNumber){
		driver.waitForElementPresent(By.linkText(orderNumber));
		driver.click(By.linkText(orderNumber));
		logger.info(orderNumber+" link clicked");
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.waitForElementPresent(ORDERS_DETAIL_ITEMS_LBL);
		return driver.findElements(TOTAL_ORDERS_ON_PLACED_ORDER_DETAILS).size();
	}


	public boolean validateOrderStatusOnOrderSearchTab(String orderNumber) {
		driver.waitForElementPresent(By.xpath(String.format(orderStatusLoc, orderNumber)));
		String orderStatus = driver.findElement(By.xpath(String.format(orderStatusLoc, orderNumber))).getText();
		System.out.println("orderStatus"+orderStatus);
		if(orderStatus.contains("Submitted")){
			return true;
		}
		return false;
	}

	public boolean isCustomerNameOrCIDTxtFieldDisplayedOnOrderSearchTab(){
		return driver.IsElementVisible(driver.findElement(CUSTOMER_NAME_OR_CID_TXT_FIELD));
	}

	public boolean isSearchBtnDisplayedOnOrderSearchTab(){
		return driver.IsElementVisible(driver.findElement(SEARCH_BTN));
	}

	public String getValueSelectedByDefaultOnOrderSearchTab(String dropDownName){
		Select dropDown;
		String getValueIsSelectedByDefaultOnOrderSearchTab = null;
		if(dropDownName.equalsIgnoreCase("Order Type")){
			dropDown = new Select(driver.findElement(ORDER_TYPE_DD));
			getValueIsSelectedByDefaultOnOrderSearchTab = dropDown.getFirstSelectedOption().getText();			
		}
		else if(dropDownName.equalsIgnoreCase("Ship To Country")){
			dropDown = new Select(driver.findElement(SHIP_TO_COUNTRY_DD));
			getValueIsSelectedByDefaultOnOrderSearchTab = dropDown.getFirstSelectedOption().getText();
		}
		else if(dropDownName.equalsIgnoreCase("Order Status")){
			dropDown = new Select(driver.findElement(ORDER_STATUS_DD));
			getValueIsSelectedByDefaultOnOrderSearchTab = dropDown.getFirstSelectedOption().getText();
		}
		return getValueIsSelectedByDefaultOnOrderSearchTab;
	}

	public void selectOrderTypeInOrderSearchTab(String orderType){
		driver.waitForElementPresent(By.xpath(String.format(orderTypeDDloc,orderType)));
		driver.click(By.xpath(String.format(orderTypeDDloc,orderType)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(){
		driver.waitForElementPresent(ORDER_LINK_SECTION_LOCATOR);
		return driver.isElementPresent(ORDER_LINK_SECTION_LOCATOR);
	}

	public String getLastNameOfTheCustomerInOrderSearchTab(String customerSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(customerLastNameInSearchResultsLoc, customerSequenceNumber)));
		String firstname = driver.findElement(By.xpath(String.format(customerLastNameInSearchResultsLoc, customerSequenceNumber))).getText();
		logger.info("Selected Cutomer first Name is = "+firstname);
		return firstname;
	}

	public String getEmailIdOfTheCustomerInOrderSearchTab(String customerSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(anotherCustomerEmailIdInSearchResultsLoc, customerSequenceNumber)));
		String customerEmailId = driver.findElement(By.xpath(String.format(anotherCustomerEmailIdInSearchResultsLoc, customerSequenceNumber))).getText();
		logger.info("Selected Cutomer email Id is = "+customerEmailId);
		return customerEmailId;
	}

	public void clearOrderNumberFieldInOrderSearchTab(){
		driver.waitForElementPresent(ORDER_NUMBER_TXT_FIELD_ORDER_SEARCH_TAB);
		driver.clear(ORDER_NUMBER_TXT_FIELD_ORDER_SEARCH_TAB);		  
	}

	public boolean verifyNoResultFoundForInvalidCID(){
		driver.quickWaitForElementPresent(INVALID_CID_SEARCH_RESULT);
		return driver.isElementPresent(INVALID_CID_SEARCH_RESULT);  
	}

	public void selectCountryFromDropDownInCustomerSearchTab(String country){
		driver.waitForElementPresent(By.xpath(String.format(countryDDLoc, country)));
		driver.click(By.xpath(String.format(countryDDLoc, country)));
		driver.waitForCSCockpitLoadingImageToDisappear();
		logger.info("************************************************************************************************************");
		logger.info("COUNTRY SELECTED = "+country);
	}

	public String getOrderStatusOfClickedOrderInOrderSearchTab(String customerSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderStatusCorrespondingOrderLoc, customerSequenceNumber)));
		String orderStatus = driver.findElement(By.xpath(String.format(orderStatusCorrespondingOrderLoc, customerSequenceNumber))).getText();
		logger.info("Selected Customer order status is = "+orderStatus);
		return orderStatus;
	}

	public void clickOrderLinkOnOrderSearchTab(String orderNumber){
		driver.waitForElementPresent(By.linkText(orderNumber));
		driver.click(By.linkText(orderNumber));
		logger.info(orderNumber+" link clicked");
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getfirstNameOfTheCustomerInOrderSearchTab(String customerSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(customerFirstNameInSearchResultsLoc, customerSequenceNumber)));
		String firstname = driver.findElement(By.xpath(String.format(customerFirstNameInSearchResultsLoc, customerSequenceNumber))).getText();
		logger.info("Selected Cutomer first Name is = "+firstname);
		return firstname;
	}

	public boolean isOrderSearchLabelPresent(String label){
		driver.waitForElementPresent(By.xpath(String.format(orderSearchLabel, label)));
		return driver.IsElementVisible(driver.findElement(By.xpath(String.format(orderSearchLabel, label))));
	}

	public String getCIDOrderNumberInOrderSearchResultsInOrderSearchTab(String orderSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderNumberFromOrdersResultLoc,orderSequenceNumber)));
		return driver.findElement(By.xpath(String.format(orderNumberFromOrdersResultLoc,orderSequenceNumber))).getText();
	}

	public String getAndclickOrderNumberInOrderSearchResultsInOrderSearchTab(String orderSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderNumberFromOrdersResultLoc,orderSequenceNumber)));
		String orderNumber=driver.findElement(By.xpath(String.format(orderNumberFromOrdersResultLoc,orderSequenceNumber))).getText();
		driver.click(By.xpath(String.format(orderNumberFromOrdersResultLoc,orderSequenceNumber)));
		driver.waitForCSCockpitLoadingImageToDisappear();
		return orderNumber;
	}

}