package com.rf.pages.website.LSD;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class LSDOrderPage extends LSDRFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(LSDOrderPage.class.getName());

	public LSDOrderPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static final By FIRST_PROCESSED_ORDER_LOC = By.xpath("//div[@id='sub-stage']//section[4]/descendant::span[text()='Processed'][1]") ;
	private static final By ORDER_DATE_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][1]//li[2]/span") ;
	private static final By COMMISION_DATE_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][1]//li[3]/span") ;
	private static final By ORDER_NUMBER_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][1]//li[4]/span") ;
	private static final By PSQV_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][1]//li[5]/span") ;
	private static final By ORDER_STATUS_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][2]//li/span") ;
	private static final By ORDER_ITEMS_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][2]//div[@class='items-container']//li[1]/span") ;
	private static final By FOOT_NOTE_LOC = By.xpath("//section[contains(@class,'footnote')]/p") ;
	private static final By ORDERS_PAGE = By.xpath("//section[@id='order-summary-header']");
	private static final By CONTACT_BUTTON_AT_FOOTER = By.xpath("//section[@class='order-nav']//div[@class='shadow-card-button-container']");
	private static final By CLOSE_ICON_OF_ORDER_SECTION = By.xpath("//section[@id='order-summary-modal']//div[@class='icon-close pointer']");
	private static final By FIRST_PROCESSED_PC_AUTOSHIP_ORDER_LOC = By.xpath("//div[@id='sub-stage']//section[4]/descendant::span[text()='Processed'][1]/following::span[text()='PC Perks'][1]");
	private static final By FIRST_PROCESSED_RC_ORDER_LOC = By.xpath("//div[@id='sub-stage']//section[4]/descendant::span[text()='Processed'][1]/following::span[text()='Retail Order'][1]");
	private static final By CONTACT_DETAILS_SECTION = By.xpath("//div[@class='contact-profile-content']//div[@class='shadow-card']");
	private static final By PHONE_ICON = By.xpath("//div[@class='icon']//span[@class='icon-phone']");
	private static final By EMAIL_ICON = By.xpath("//div[@class='icon']//span[@class='icon-email']");


	public void clickFirstProcessedOrder(){
		driver.waitForElementPresent(FIRST_PROCESSED_ORDER_LOC);
		driver.click(FIRST_PROCESSED_ORDER_LOC);
	}

	public String getOrderDate(){
		driver.waitForElementPresent(ORDER_DATE_LOC);
		String orderDate = driver.findElement(ORDER_DATE_LOC).getText();
		logger.info("order date is "+orderDate);
		return orderDate;
	}

	public String getCommisionDate(){
		driver.waitForElementPresent(COMMISION_DATE_LOC);
		String commisionDate = driver.findElement(COMMISION_DATE_LOC).getText();
		logger.info("commision date is "+commisionDate);
		return commisionDate;
	}

	public String getOrderNumber(){
		driver.waitForElementPresent(ORDER_NUMBER_LOC);
		String orderNumber = driver.findElement(ORDER_NUMBER_LOC).getText();
		logger.info("order Number is "+orderNumber);
		return orderNumber;
	}

	public String getPSQV(){
		driver.waitForElementPresent(PSQV_LOC);
		String PSQV = driver.findElement(PSQV_LOC).getText();
		logger.info("PSQV is "+PSQV);
		return PSQV;
	}

	public String getOrderStatus(){
		driver.waitForElementPresent(ORDER_STATUS_LOC);
		String orderStatus = driver.findElement(ORDER_STATUS_LOC).getText();
		logger.info("orderStatus is "+orderStatus);
		return orderStatus;
	}

	public String getOrderItems(){
		driver.waitForElementPresent(ORDER_ITEMS_LOC);
		String orderItems = driver.findElement(ORDER_ITEMS_LOC).getText();
		logger.info("order Items is "+orderItems);
		return orderItems;
	}

	public String getFootNote(){
		driver.waitForElementPresent(FOOT_NOTE_LOC);
		String footNote = driver.findElement(FOOT_NOTE_LOC).getText();
		logger.info("foot Note is "+footNote);
		return footNote;
	}

	public boolean isOrdersPagePresent(){
		driver.waitForElementPresent(ORDERS_PAGE);
		return driver.isElementPresent(ORDERS_PAGE);
	}

	public boolean isContactButtonPresentAtFooter(){
		driver.waitForElementPresent(CONTACT_BUTTON_AT_FOOTER);
		return driver.isElementPresent(CONTACT_BUTTON_AT_FOOTER);
	}

	public void clickCloseIconOfOrder(){
		driver.waitForElementPresent(CLOSE_ICON_OF_ORDER_SECTION);
		driver.click(CLOSE_ICON_OF_ORDER_SECTION);
		logger.info("close icon clicked of an order");
	}

	public void clickFirstProcessedPCAutishipOrder(){
		driver.waitForElementPresent(FIRST_PROCESSED_PC_AUTOSHIP_ORDER_LOC);
		driver.click(FIRST_PROCESSED_PC_AUTOSHIP_ORDER_LOC);
		logger.info("First processed pc autoship order clicked");
		driver.waitForLSDLoaderAnimationImageToDisappear();
	}

	public void clickFirstProcessedRCOrder(){
		driver.waitForElementPresent(FIRST_PROCESSED_RC_ORDER_LOC);
		driver.click(FIRST_PROCESSED_RC_ORDER_LOC);
		logger.info("First processed rc order clicked");
		driver.waitForLSDLoaderAnimationImageToDisappear();
	}

	public void clickContactButtonAtFooter(){
		driver.waitForElementPresent(CONTACT_BUTTON_AT_FOOTER);
		driver.click(CONTACT_BUTTON_AT_FOOTER);
		logger.info("Contact button clicked at footer");
	}

	public boolean isContactDetailsPresent(){
		driver.waitForElementPresent(CONTACT_DETAILS_SECTION);
		return driver.isElementPresent(CONTACT_DETAILS_SECTION);
	}

	public boolean isPhoneIconPresent(){
		driver.waitForElementPresent(PHONE_ICON);
		return driver.isElementPresent(PHONE_ICON);
	}

	public boolean isEmailIconPresent(){
		driver.waitForElementPresent(EMAIL_ICON);
		return driver.isElementPresent(EMAIL_ICON);
	}


}