package com.rf.pages.website;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontConsultantEnrollmentConfirmationPage extends RFWebsiteBasePage {

	private final By RODAN_AND_FIELDS_IMG_LOC = By.cssSelector("img[title='Rodan+Fields']");
	
	public StoreFrontConsultantEnrollmentConfirmationPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public StoreFrontConsultantPage clickOnRodanFieldsImage(){
		driver.click(RODAN_AND_FIELDS_IMG_LOC);
		return new StoreFrontConsultantPage(driver);
	}
}
