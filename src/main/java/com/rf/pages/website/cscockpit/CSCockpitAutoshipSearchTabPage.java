package com.rf.pages.website.cscockpit;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.thoughtworks.selenium.webdriven.commands.IsElementPresent;

public class CSCockpitAutoshipSearchTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitAutoshipSearchTabPage.class.getName());

	private static String ddValuesLoc = "//div[@class='z-combobox-pp'][contains(@style,'display: block')]//tr[contains(@class,'z-combo-item')][@z.label='%s']";
	
	private static final By SEARCH_BY_FIELD_LOC = By.xpath("//span[text()='Search By:']//ancestor::div[1]");
	private static final By TEMPLATE_TYPE_FIELD_LOC = By.xpath("//span[text()='Template Type:']//ancestor::div[1]");
	private static final By SEARCH_BY_DD_FIRST_OPTION_LOC = By.xpath("//body[@class='gecko gecko34']/div[@class='z-combobox-pp']//tr/td[contains(text(),'Next')]");
	private static final By SEARCH_BY_DD_SECOND_OPTION_LOC = By.xpath("//body[@class='gecko gecko34']/div[@class='z-combobox-pp']/table//tr[2]");
	private static final By SEARCH_BY_DD_THIRD_OPTION_LOC = By.xpath("//body[@class='gecko gecko34']/div[@class='z-combobox-pp']/table//tr[3]");
	private static final By SEARCH_BY_DD_IMG_LOC = By.xpath("//span[contains(text(),'Search By')]/following::img[1]");
	private static final By TEMPLATE_TYPE_DD_IMG_LOC = By.xpath("//span[contains(text(),'Template Type')]/following::img[1]");
	private static final By LAST_ORDER_STATUS_DD_IMG_LOC = By.xpath("//span[contains(text(),'Last Order Status')]/following::img[1]");
	private static final By TEMPLATE_STATUS_DD_IMG_LOC = By.xpath("//span[contains(text(),'Template Status')]/following::img[1]");
	private static final By TEMPLATE_TYPE_DD_FOURTH_OPTION_LOC = By.xpath("//body[@class='gecko gecko34']/div[@class='z-combobox-pp']/table//tr[4]");
	private static final By ORDER_STATUS_FIELD_LOC = By.xpath("//span[text()='Last Order Status']//ancestor::div[1]");
	private static final By CUSTOMER_NAME_FIELD_LOC = By.xpath("//span[text()='Customer Name or CID']");
	private static final By SPONSOR_NAME_FIELD_LOC = By.xpath("//span[text()='Sponsor Name or CID']");
	private static final By TEMPLATE_NAME_FIELD_LOC = By.xpath("//span[text()='Template Number']");
	private static final By RUN_SELECTED_BUTTON_LOC = By.xpath("//td[text()='Run Selected']");
	private static final By SEARCH_AUTOSHIP_BUTTON_LOC = By.xpath("//td[text()='Search Autoship']");
	private static final By AUTOSHIP_TEMPLATE = By.xpath("//span[contains(text(),'Autoship Template #')]");

	protected RFWebsiteDriver driver;

	public CSCockpitAutoshipSearchTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean isAutoshipTemplateDisplayedInAutoshipTemplateTab(){
		driver.isElementPresent(AUTOSHIP_TEMPLATE);
		return driver.isElementPresent(AUTOSHIP_TEMPLATE);  
	}

	public boolean verifySearchByFieldPresentOnAutoshipSearch() {
		return driver.isElementPresent(SEARCH_BY_FIELD_LOC);
	}

	public boolean verifyTemplateTypeFieldPresentOnAutoshipSearch() {
		return driver.isElementPresent(TEMPLATE_TYPE_FIELD_LOC);
	}

	public boolean verifyLastOrderStatusFieldPresentOnAutoshipSearch() {
		return driver.isElementPresent(ORDER_STATUS_FIELD_LOC);
	}

	public boolean verifyCustomerNameCidFieldPresentOnAutoshipPage() {
		return driver.isElementPresent(CUSTOMER_NAME_FIELD_LOC);
	}
	
	public boolean verifySponsorNameCidFieldPresentOnAutoshipPage() {
		return driver.isElementPresent(SPONSOR_NAME_FIELD_LOC);
	}
	
	public boolean verifyTemplateNumberCidFieldPresentOnAutoshipPage() {
		return driver.isElementPresent(TEMPLATE_NAME_FIELD_LOC);
	}

	public boolean verifyRunSelectedButtonPresent() {
		return driver.isElementPresent(RUN_SELECTED_BUTTON_LOC);
	}
	
	public boolean verifySearchAutoshipButtonPresent() {
		return driver.isElementPresent(SEARCH_AUTOSHIP_BUTTON_LOC);
	}
	
	public void clickSearchByDropDown(){
		driver.click(SEARCH_BY_DD_IMG_LOC);
	}
	
	public void clickTemplateTypeDropDown(){
		driver.click(TEMPLATE_TYPE_DD_IMG_LOC);
	}
	
	public void clickLastOrderStatusDropDown(){
		driver.click(LAST_ORDER_STATUS_DD_IMG_LOC);
	}
	
	public void clickTemplateStatusDropDown(){
		driver.click(TEMPLATE_STATUS_DD_IMG_LOC);
	}
	
	public boolean isSearchByDropDownValuePresent(String ddValue){	
		logger.info("Drop down value to be verified is "+ddValue);
		return driver.isElementPresent(By.xpath(String.format(ddValuesLoc, ddValue)));
	}
	
	public boolean isTemplateTypeDropDownValuePresent(String ddValue){	
		logger.info("Drop down value to be verified is "+ddValue);
		return driver.isElementPresent(By.xpath(String.format(ddValuesLoc, ddValue)));
	}
	
	public boolean islastOrderStatusDropDownValuePresent(String ddValue){
		logger.info("Drop down value to be verified is "+ddValue);
		return driver.isElementPresent(By.xpath(String.format(ddValuesLoc, ddValue)));
	}
	
	public boolean isTemplateStatusDropDownValuePresent(String ddValue){
		logger.info("Drop down value to be verified is "+ddValue);
		return driver.isElementPresent(By.xpath(String.format(ddValuesLoc, ddValue)));
	}

}