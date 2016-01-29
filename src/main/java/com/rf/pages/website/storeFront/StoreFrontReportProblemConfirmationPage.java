package com.rf.pages.website.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontReportProblemConfirmationPage extends StoreFrontRFWebsiteBasePage  {

	private final By REPORT_CONFIRMATION_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top pt1 text-uppercase']");
	private final By REPORT_CONFIRMATION_EMAIL_ADD_LOC = By.xpath("//div[text()='E-mail Address:']/following::div[1]");
	private final By REPORT_CONFIRMATION_ORDER_NUM_LOC = By.xpath("//div[contains(text(),'Order Number')]/following::div[1]");
	private final By REPORT_CONFIRMATION_THANK_U_MSG_LOC = By.xpath("//div[@id='main-content']//h3[contains(text(),'Thank you')]");
	private final By REPORT_CONFIRMATION_BACK_BUTTON_LOC = By.xpath("//input[@value='Back to orders']");

	public StoreFrontReportProblemConfirmationPage(RFWebsiteDriver driver) {
		super(driver);

	}

	public boolean verifyHeaderAtReportConfirmationPage(String header){
		driver.waitForElementPresent(REPORT_CONFIRMATION_HEADER_LOC);
		String headerUI= driver.findElement(REPORT_CONFIRMATION_HEADER_LOC).getText();

		if(headerUI.equals(header)){
			return true;
		}
		return false;
	}

	public boolean verifyThankYouTagAtReportConfirmationPage(String message){
		driver.waitForElementPresent(REPORT_CONFIRMATION_THANK_U_MSG_LOC);
		String messageUI = driver.findElement(REPORT_CONFIRMATION_THANK_U_MSG_LOC).getText();
		if(messageUI.equals(message)){
			return true;
		}
		return false;
	}

	public boolean verifyEmailAddAtReportConfirmationPage(String EmailAdd){
		String emailAddress = EmailAdd.toLowerCase();
		driver.waitForElementPresent(REPORT_CONFIRMATION_EMAIL_ADD_LOC);
		String emailAdd = driver.findElement(REPORT_CONFIRMATION_EMAIL_ADD_LOC).getText();
		
		if(emailAdd.trim().equalsIgnoreCase(emailAddress.trim())){
			return true;
		}
		return false;
	}

	public boolean verifyOrderNumberAtReportConfirmationPage(){
		String orderNumber = StoreFrontOrdersPage.orderNumberOfOrderHistory;
		driver.waitForElementPresent(REPORT_CONFIRMATION_ORDER_NUM_LOC);
		String orderNumberAtUI = driver.findElement(REPORT_CONFIRMATION_ORDER_NUM_LOC).getText();
		if(orderNumber.equals(orderNumberAtUI)){
			return true;
		}
		return false;
	}

	public boolean verifyBackToOrderButtonAtReportConfirmationPage(){
		if(driver.findElement(REPORT_CONFIRMATION_BACK_BUTTON_LOC).isDisplayed() == true){
			return true;
		}
		return false;
	}
}

