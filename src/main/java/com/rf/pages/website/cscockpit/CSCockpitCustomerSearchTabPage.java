package com.rf.pages.website.cscockpit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;

public class CSCockpitCustomerSearchTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitCustomerSearchTabPage.class.getName());

	private static String customerTypeDDLoc = "//span[contains(text(),'Customer Type')]/select/option[text()='%s']";
	private static String countryDDLoc = "//span[contains(text(),'Country')]/select/option[text()='%s']";
	private static String accountStatusDDLoc = "//span[contains(text(),'Account Status')]/select/option[text()='%s']";
	private static String customerEmailIdInSearchResultsLoc = "//div[@class='csListboxContainer']/descendant::table[2]/tbody[2]/tr[%s]/td[4]//span";
	private static String customerCIDInSearchResultsLoc = "//div[@class='csListboxContainer']/descendant::table[2]/tbody[2]/tr[%s]/td[1]//a";

	private static String customerFirstNameInSearchResultsLoc = "//div[@class='csListboxContainer']/descendant::table[2]/tbody[2]/tr[%s]/td[3]//span";
	private static final By SEARCH_BTN = By.xpath("//td[text()='SEARCH']");
	private static final By TOTAL_CUSTOMERS_FROM_RESULT_FIRST_PAGE = By.xpath("//div[@class='csListboxContainer']/descendant::table[2]/tbody[2]/tr");
	private static final By ENTER_EMAIL_ID = By.xpath("//span[contains(text(),'Email Address')]/following::input[1]");
	private static final By FIND_CUSTOMER_LBL = By.xpath("//span[text()='Find Customer']");
	private static final By FIND_ORDER = By.xpath("//a[contains(text(),'Find Order')]");
	private static final By CUSTOMER_TYPE_LOC = By.xpath("//span[@class='customerSearchType onethird']");
	private static final By CUSTOMER_COUNTRY_LOC = By.xpath("//span[@class='countryValues onethird']");
	private static final By CUSTOMER_ACCOUNT_STATUS = By.xpath("//span[@class='rfaccountstatus onethird']");
	private static final By CUSOMER_NAME_CID_FIELD_LOC = By.xpath("//span[text()='Customer Name or CID']");
	private static final By POST_CODE_FIELD_LOC = By.xpath("//span[text()='Postcode']");
	private static final By EMAIL_ADD_FIELD_LOC = By.xpath("//span[text()='Email Address']//following::input[1]");
	private static final By COMMIT_TAX_TAB_LOC = By.xpath("//span[text()='Commit Tax']");
	private static final By AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP = By.xpath("//span[text()='Autoship Templates']/following::div[1]//div/span[text()='crpAutoship']/../../preceding-sibling::td//a");

	protected RFWebsiteDriver driver;

	public CSCockpitCustomerSearchTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean isCustomerSearchPageDisplayed(){
		driver.waitForElementPresent(FIND_CUSTOMER_LBL);
		return driver.IsElementVisible(driver.findElement(FIND_CUSTOMER_LBL));
	}


	public void selectCustomerTypeFromDropDownInCustomerSearchTab(String customerType){
		driver.waitForElementPresent(By.xpath(String.format(customerTypeDDLoc, customerType)));
		driver.click(By.xpath(String.format(customerTypeDDLoc, customerType.toUpperCase())));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void selectCountryFromDropDownInCustomerSearchTab(String country){
		driver.waitForElementPresent(By.xpath(String.format(countryDDLoc, country)));
		driver.click(By.xpath(String.format(countryDDLoc, country)));
		driver.waitForCSCockpitLoadingImageToDisappear();
		logger.info("************************************************************************************************************");
		logger.info("COUNTRY SELECTED = "+country);
		logger.info("************************************************************************************************************");
	}

	public void selectAccountStatusFromDropDownInCustomerSearchTab(String accountStatus){
		driver.waitForElementPresent(By.xpath(String.format(accountStatusDDLoc, accountStatus)));
		driver.click(By.xpath(String.format(accountStatusDDLoc, accountStatus)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickSearchBtn(){
		driver.quickWaitForElementPresent(SEARCH_BTN);
		driver.click(SEARCH_BTN);
		logger.info("Search button clicked");
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getEmailIdOfTheCustomerInCustomerSearchTab(String customerSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(customerEmailIdInSearchResultsLoc, customerSequenceNumber)));
		String customerEmailId = driver.findElement(By.xpath(String.format(customerEmailIdInSearchResultsLoc, customerSequenceNumber))).getText();
		logger.info("Selected Cutomer email Id is = "+customerEmailId);
		return customerEmailId;
	}

	public int getRandomCustomerFromSearchResult(){
		driver.waitForElementPresent(TOTAL_CUSTOMERS_FROM_RESULT_FIRST_PAGE);
		int totalCustomersFromResultsSearchFirstPage =  driver.findElements(TOTAL_CUSTOMERS_FROM_RESULT_FIRST_PAGE).size();
		logger.info("total customers in the customer search result is "+totalCustomersFromResultsSearchFirstPage);
		int randomCustomerFromSearchResult = CommonUtils.getRandomNum(1, totalCustomersFromResultsSearchFirstPage);
		logger.info("Random Customer sequence number is "+randomCustomerFromSearchResult);
		return randomCustomerFromSearchResult;		
	}

	public int getTotalResultsInCustomerSearchOnCustomerSearchTab(){
		driver.waitForElementPresent(TOTAL_CUSTOMERS_FROM_RESULT_FIRST_PAGE);
		return driver.findElements(TOTAL_CUSTOMERS_FROM_RESULT_FIRST_PAGE).size();
	}

	public void clickCIDNumberInCustomerSearchTab(String customerSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(customerCIDInSearchResultsLoc, customerSequenceNumber)));
		driver.click(By.xpath(String.format(customerCIDInSearchResultsLoc, customerSequenceNumber)));
		logger.info("CID sequence number is "+customerSequenceNumber);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String clickAndReturnCIDNumberInCustomerSearchTab(String customerSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(customerCIDInSearchResultsLoc, customerSequenceNumber)));
		String orderNumber = driver.findElement((By.xpath(String.format(customerCIDInSearchResultsLoc, customerSequenceNumber)))).getText();
		driver.click(By.xpath(String.format(customerCIDInSearchResultsLoc, customerSequenceNumber)));
		driver.waitForCSCockpitLoadingImageToDisappear();
		return orderNumber;
	}


	public String getCIDNumberInCustomerSearchTab(String customerSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(customerCIDInSearchResultsLoc, customerSequenceNumber)));
		return driver.findElement(By.xpath(String.format(customerCIDInSearchResultsLoc, customerSequenceNumber))).getText();

	}

	public void enterEmailIdInSearchFieldInCustomerSearchTab(String emailId){
		driver.waitForElementPresent(ENTER_EMAIL_ID);
		driver.type(ENTER_EMAIL_ID, emailId);
	}

	public String getfirstNameOfTheCustomerInCustomerSearchTab(String customerSequenceNumber){
		driver.waitForElementPresent(By.xpath(String.format(customerFirstNameInSearchResultsLoc, customerSequenceNumber)));
		String firstname = driver.findElement(By.xpath(String.format(customerFirstNameInSearchResultsLoc, customerSequenceNumber))).getText();
		logger.info("Selected Cutomer first Name is = "+firstname);
		return firstname;
	}

	public void clickOnFindOrderInCustomerSearchTab(){
		driver.waitForElementPresent(FIND_ORDER);
		driver.click(FIND_ORDER);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyCustomerTypePresenceOnPage() {
		return driver.isElementPresent(CUSTOMER_TYPE_LOC);
	}

	public boolean verifyCustomerCountryPresenceOnPage(){ 
		return driver.isElementPresent(CUSTOMER_COUNTRY_LOC);
	}
	public boolean verifyAccountStatusPresenceOnPage(){
		return driver.isElementPresent(CUSTOMER_ACCOUNT_STATUS);
	}

	public boolean verifyCustomerNameFieldPresenceOnPage() {
		return driver.isElementPresent(CUSOMER_NAME_CID_FIELD_LOC);
	}
	public boolean verifyPostcodeFieldPresenceOnPage(){
		return driver.isElementPresent(POST_CODE_FIELD_LOC);
	}

	public boolean verifyEmailAddressFieldPresenceOnPage() {
		return driver.isElementPresent(EMAIL_ADD_FIELD_LOC);
	}	

	public void clickCommitTaxTab() {
		driver.waitForElementPresent(COMMIT_TAX_TAB_LOC);
		driver.click(COMMIT_TAX_TAB_LOC);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getAndClickAutoshipIDHavingTypeAsCRPAutoshipInCustomerTab(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP);
		String autoshipID = driver.findElement(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}
}