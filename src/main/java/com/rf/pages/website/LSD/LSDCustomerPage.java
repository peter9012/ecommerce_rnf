package com.rf.pages.website.LSD;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class LSDCustomerPage extends LSDRFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(LSDCustomerPage.class.getName());

	public LSDCustomerPage(RFWebsiteDriver driver) {
		super(driver);
	}

	private static final By CUSTOMERS_PAGE = By.xpath("//div[@class='customers']");
	private static String orderValuesInUpcomingOrderSection = "//section[@class='pc-order-upcoming']//span[text()='%s']";
	private static String orderValuesOfFirstOrderInOrderHistorySection = "//section[@class='pc-order-history']//pc-order-history-card[contains(@class,'pc-order-history-card')][1]//th[text()='%s']";

	private static final By EXPAND_AND_MINIMIZE_BTN_OF_THIS_MONTH = By.xpath("//h2[text()='This Month']/preceding::div[1]/button");
	private static final By EXPAND_AND_MINIMIZE_BTN_OF_NEXT_MONTH = By.xpath("//h2[text()='Next Month']/preceding::div[1]/button");
	private static final By EXPAND_AND_MINIMIZE_BTN_OF_FURTHER_OUT = By.xpath("//h2[text()='Further Out']/preceding::div[1]/button");
	private static final By CUSTOMER_AUTOSHIP_ORDER_SECTION = By.xpath("//div[@id='sub-stage']//section//div[@class='pc-order-cards']");
	private static final By ORDER_CUSTOMER_NAME = By.xpath("//div[@class='pc-order-cards']//pc-order-card[@class='au-target'][1]//h3");
	private static final By ORDER_TYPE = By.xpath("//div[@class='pc-order-cards']//pc-order-card[@class='au-target'][1]//h4");
	private static final By ORDER_STATUS = By.xpath("//div[@class='pc-order-cards']//pc-order-card[@class='au-target'][1]//div[@class='data']/div[2]/div[1]");
	private static final By ORDER_DATE = By.xpath("//div[@class='pc-order-cards']//pc-order-card[@class='au-target'][1]//div[@class='data']/div[1]/div[2]");
	private static final By FIRST_PROCESSED_ORDER = By.xpath("//div[@class='pc-order-cards']/descendant::div[text()='Processed'][1]");
	private static final By FIRST_FAILED_ORDER = By.xpath("//div[@class='pc-order-cards']/descendant::div[text()='Failed'][1]");
	private static final By FIRST_SCHEDULED_ORDER = By.xpath("//div[@class='pc-order-cards']/descendant::div[text()='Scheduled'][1]");
	private static final By PC_USERNAME_FROM_ORDER = By.xpath("//div[@class='pc-profile-content']/h2");
	private static final By ENROLLED_ON_TXT = By.xpath("//th[text()='Enrolled on:']");
	private static final By NEXT_SHIPMENT_TXT = By.xpath("//th[text()='Enrolled on:']");
	private static final By UPCOMING_ORDER_SECTION = By.xpath("//section[@class='pc-order-upcoming']");
	private static final By ORDER_HISTORY_SECTION = By.xpath("//section[@class='pc-order-history']");
	private static final By DATE_AND_TIME_STAMP_IN_UPCOMING_ORDER_SECTION = By.xpath("//section[@class='pc-order-upcoming']//p[@class='commission-date']/span[1]");
	private static final By ORDER_ITEMS_IN_UPCOMING_ORDER_SECTION_ = By.xpath("//section[@class='pc-order-upcoming']//div[@class='items-container']");
	private static final By ORDER_NOTE = By.xpath("//section[@class='dark-grey tiny-text footnote']/p");
	private static final By DATE_AND_TIME_STAMP_IN_ORDER_HISTORY_SECTION_FOR_FIRST_ORDER = By.xpath("//section[@class='pc-order-history']//pc-order-history-card[contains(@class,'pc-order-history-card')][1]//p[@class='commission-date']");
	private static final By ORDER_ITEMS_OF_FIRST_ORDER_IN_ORDER_HISTORY_SECTION = By.xpath("//section[@class='pc-order-history']//pc-order-history-card[contains(@class,'pc-order-history-card')][1]//div[@class='items-container']");
	private static final By BACK_ARROW_ICON = By.xpath("//section[@id='pc-profile-modal']/div[@class='icon-arrow-back pointer']");
	private static final By NEW_LABEL_LOC = By.xpath("//div[@class='pc-order-cards']//pc-order-card[@class='au-target'][1]//div[text()='New']");
	private static final By NEW_LABEL_IN_ORDER_LOC = By.xpath("//div[@class='pc-profile-content']//div[text()='New']");
	private static final By CONTACT_NAME_AT_FOOTER_FROM_CUSTOMER_PAGE = By.xpath("//section[@id='pc-profile-modal']//div[@class='shadow-card-button-container']//span[@class='label']");
	 
	
	public boolean isCustomerPagePresent(){
		driver.waitForElementPresent(CUSTOMERS_PAGE);
		return driver.isElementPresent(CUSTOMERS_PAGE);
	}

	public void clickExpandAndMinimizeButtonOfThisMonth(){
		driver.waitForElementPresent(EXPAND_AND_MINIMIZE_BTN_OF_THIS_MONTH);
		driver.click(EXPAND_AND_MINIMIZE_BTN_OF_THIS_MONTH);
		logger.info("Expand and minimize Button clicked of this month");
	}

	public void clickExpandAndMinimizeButtonOfNextMonth(){
		driver.waitForElementPresent(EXPAND_AND_MINIMIZE_BTN_OF_NEXT_MONTH);
		driver.click(EXPAND_AND_MINIMIZE_BTN_OF_NEXT_MONTH);
		logger.info("Expand and minimize Button clicked of next month");
	}

	public void clickExpandAndMinimizeButtonOfFurtherOut(){
		driver.waitForElementPresent(EXPAND_AND_MINIMIZE_BTN_OF_FURTHER_OUT);
		driver.click(EXPAND_AND_MINIMIZE_BTN_OF_FURTHER_OUT);
		logger.info("Expand and minimize Button clicked of further out");
	}

	public boolean isAutoshipOrderSectionPresentAfterExpand(){
		driver.waitForElementPresent(CUSTOMER_AUTOSHIP_ORDER_SECTION);
		return driver.isElementPresent(CUSTOMER_AUTOSHIP_ORDER_SECTION);
	}

	public String getCustomerNamePresentInFirstOrder(){
		driver.waitForElementPresent(ORDER_CUSTOMER_NAME);
		String customerName =  driver.findElement(ORDER_CUSTOMER_NAME).getText();
		logger.info("First order's customer name is: "+customerName);
		return customerName;
	}

	public String getOrderTypeOfFirstOrder(){
		driver.waitForElementPresent(ORDER_TYPE);
		String orderType =  driver.findElement(ORDER_TYPE).getText();
		logger.info("First order's order type is: "+orderType);
		return orderType;
	}

	public String getOrderStatusOfFirstOrder(){
		driver.waitForElementPresent(ORDER_STATUS);
		String orderStatus =  driver.findElement(ORDER_STATUS).getText();
		logger.info("First order's order status is: "+orderStatus);
		return orderStatus;
	}

	public String getOrderDateOfFirstOrder(){
		driver.waitForElementPresent(ORDER_DATE);
		String orderDate = driver.findElement(ORDER_DATE).getText();
		logger.info("First order's order date is: "+orderDate);
		return orderDate;
	}

	public String getClassNameOfFirstProcessedOrder(){
		driver.waitForElementPresent(FIRST_PROCESSED_ORDER);
		String className = driver.findElement(FIRST_PROCESSED_ORDER).getAttribute("class");
		logger.info("class name of prcessed order is: "+className);
		return className;
	}

	public String getClassNameOfFirstFailedOrder(){
		driver.waitForElementPresent(FIRST_FAILED_ORDER);
		String className = driver.findElement(FIRST_FAILED_ORDER).getAttribute("class");
		logger.info("class name of failed order is: "+className);
		return className;
	}

	public String getClassNameOfFirstScheduledOrder(){
		driver.waitForElementPresent(FIRST_SCHEDULED_ORDER);
		String className = driver.findElement(FIRST_SCHEDULED_ORDER).getAttribute("class");
		logger.info("class name of Scheduled order is: "+className);
		return className;
	}

	public void clickFirstProcessedOrderUnderCustomerSection(){
		driver.waitForElementPresent(FIRST_PROCESSED_ORDER);
		driver.click(FIRST_PROCESSED_ORDER);
		logger.info("First processed order clicked in customer section");
	}

	public String getPCUserNamePresentInOrder(){
		driver.waitForElementPresent(PC_USERNAME_FROM_ORDER);
		String customerName =  driver.findElement(PC_USERNAME_FROM_ORDER).getText();
		logger.info("Order's customer name is: "+customerName);
		return customerName;
	}

	public boolean isEnrolledOnTxtPresent(){
		driver.waitForElementPresent(ENROLLED_ON_TXT);
		return driver.isElementPresent(ENROLLED_ON_TXT);
	}

	public boolean isNextShipmentTxtPresent(){
		driver.waitForElementPresent(NEXT_SHIPMENT_TXT);
		return driver.isElementPresent(NEXT_SHIPMENT_TXT);
	}

	public boolean isUpcomingOrderSectionPresent(){
		driver.pauseExecutionFor(5000);
		driver.waitForElementToBeVisible(UPCOMING_ORDER_SECTION, 3000);
		return driver.isElementPresent(UPCOMING_ORDER_SECTION);
	}

	public boolean isOrderHistorySectionPresent(){
		driver.waitForElementPresent(ORDER_HISTORY_SECTION);
		return driver.isElementPresent(ORDER_HISTORY_SECTION);
	}

	public String getOrderDateAndTimeStampInUpcomingOrderSection(){
		driver.waitForElementPresent(DATE_AND_TIME_STAMP_IN_UPCOMING_ORDER_SECTION);
		String orderDateAndTime =  driver.findElement(DATE_AND_TIME_STAMP_IN_UPCOMING_ORDER_SECTION).getText();
		logger.info("Order's date and time is: "+orderDateAndTime);
		return orderDateAndTime;
	}

	public boolean isOrderDetailsPresentInOrderUpcomingSection(String orderDetail){
		driver.waitForElementPresent(By.xpath(String.format(orderValuesInUpcomingOrderSection, orderDetail)));
		return driver.isElementPresent(By.xpath(String.format(orderValuesInUpcomingOrderSection, orderDetail)));
	}

	public boolean isOrderItemsPresentInUpcomingOrderSection(){
		driver.waitForElementPresent(ORDER_ITEMS_IN_UPCOMING_ORDER_SECTION_);
		return driver.isElementPresent(ORDER_ITEMS_IN_UPCOMING_ORDER_SECTION_);
	}

	public String getOrderNote(){
		driver.waitForElementPresent(ORDER_NOTE);
		String note = driver.findElement(ORDER_NOTE).getText();
		logger.info("Order note is: "+note);
		return note;
	}

	public String getOrderDateAndTimeStampOfFirstOrderInOrderHistorySection(){
		driver.waitForElementPresent(DATE_AND_TIME_STAMP_IN_ORDER_HISTORY_SECTION_FOR_FIRST_ORDER);
		String orderDateAndTime =  driver.findElement(DATE_AND_TIME_STAMP_IN_ORDER_HISTORY_SECTION_FOR_FIRST_ORDER).getText();
		logger.info("Order's date and time of first order in order history section is: "+orderDateAndTime);
		return orderDateAndTime;
	}

	public boolean isOrderDetailsPresentInOrderHistorySectionOfFirstOrder(String orderDetail){
		driver.waitForElementPresent(By.xpath(String.format(orderValuesOfFirstOrderInOrderHistorySection, orderDetail)));
		return driver.isElementPresent(By.xpath(String.format(orderValuesOfFirstOrderInOrderHistorySection, orderDetail)));
	}

	public boolean isOrderItemsPresentInOrderHistorySectionOfFirstOrder(){
		driver.waitForElementPresent(ORDER_ITEMS_OF_FIRST_ORDER_IN_ORDER_HISTORY_SECTION);
		return driver.isElementPresent(ORDER_ITEMS_OF_FIRST_ORDER_IN_ORDER_HISTORY_SECTION);
	}

	public void clickBackArrowIcon(){
		driver.click(BACK_ARROW_ICON);
		logger.info("Back arrow icon clicked");
	}

	public boolean isNewLabelPresentForFirstOrder(){
		driver.waitForElementPresent(NEW_LABEL_LOC);
		return driver.isElementPresent(NEW_LABEL_LOC);
	}

	public boolean isNewLabelPresentInFirstOrder(){
		driver.waitForElementPresent(NEW_LABEL_IN_ORDER_LOC);
		return driver.isElementPresent(NEW_LABEL_IN_ORDER_LOC);
	}

	public String getContactNameFromContactButtonInCustomerPage(){
		driver.waitForElementPresent(CONTACT_NAME_AT_FOOTER_FROM_CUSTOMER_PAGE);
		String name =  driver.findElement(CONTACT_NAME_AT_FOOTER_FROM_CUSTOMER_PAGE).getText();
		logger.info("Contact name is: "+name);
		return name;
	}
}