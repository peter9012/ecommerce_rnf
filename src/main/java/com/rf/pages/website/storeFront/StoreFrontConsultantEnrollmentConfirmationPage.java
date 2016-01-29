package com.rf.pages.website.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontConsultantEnrollmentConfirmationPage extends StoreFrontRFWebsiteBasePage {

	private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//img[@title='Rodan+Fields']");
	
	public StoreFrontConsultantEnrollmentConfirmationPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public StoreFrontConsultantPage clickOnRodanFieldsImage(){
		driver.click(RODAN_AND_FIELDS_IMG_LOC);
		return new StoreFrontConsultantPage(driver);
	}
}
