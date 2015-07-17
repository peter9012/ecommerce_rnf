package com.rf.pages.website;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontReportProblemConfirmationPage extends RFWebsiteBasePage  {
	
    private final By REPORT_CONFIRMATION_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top']");
    private final By REPORT_CONFIRMATION_EMAIL_ADD_LOC = By.xpath("//li[text()='E-mail Address:']/following::li[1]");
    private final By REPORT_CONFIRMATION_ORDER_NUM_LOC = By.xpath("//li[contains(text(),'Order Number')]/following::li[1]");
    private final By REPORT_CONFIRMATION_THANK_U_MSG_LOC = By.xpath("//div[@id='main-content']//h3[contains(text(),'Thank you')]");
    private final By REPORT_CONFIRMATION_BACK_BUTTON_LOC = By.xpath("//input[@value='Back to orders']");
    
	public StoreFrontReportProblemConfirmationPage(RFWebsiteDriver driver) {
		super(driver);
		
	}
    
	public boolean verifyHeaderAtReportConfirmationPage(String header){
		
		String headerUI= driver.findElement(REPORT_CONFIRMATION_HEADER_LOC).getText();
		
		if(headerUI.equals(header)){
		return true;
	}
	return false;
	}
	
	public boolean verifyThankYouTagAtReportConfirmationPage(String message){
		String messageUI = driver.findElement(REPORT_CONFIRMATION_THANK_U_MSG_LOC).getText();
		if(messageUI.equals(message)){
		return true;
	}
		return false;
	}
	
	public boolean verifyEmailAddAtReportConfirmationPage(String EmailAdd){
		String emailAddress = EmailAdd.toLowerCase();

		String emailAdd = driver.findElement(REPORT_CONFIRMATION_EMAIL_ADD_LOC).getText();

		if(emailAdd.equals(emailAddress)){
			return true;
		}
		return false;
	}
	
	public boolean verifyOrderNumberAtReportConfirmationPage(){
		String orderNumber = StoreFrontOrdersPage.orderNumberOfOrderHistory;
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

