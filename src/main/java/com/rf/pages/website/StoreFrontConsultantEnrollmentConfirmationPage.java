package com.rf.pages.website;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontConsultantEnrollmentConfirmationPage extends RFWebsiteBasePage {

	
	public StoreFrontConsultantEnrollmentConfirmationPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public StoreFrontConsultantPage clickOnRodanFieldsImage(){
		driver.findElement(By.cssSelector("img[title='Rodan+Fields']")).click();
		return new StoreFrontConsultantPage(driver);
	}
}
