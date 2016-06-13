package com.rf.pages.mobile.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.mobile.RFMobileDriver;
import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontConsultantEnrollmentConfirmationPage extends StoreFrontRFMobileBasePage {

	private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//img[@title='Rodan+Fields']");
	
	public StoreFrontConsultantEnrollmentConfirmationPage(RFMobileDriver driver) {
		super(driver);		
	}

	public StoreFrontConsultantPage clickOnRodanFieldsImage(){
		driver.click(RODAN_AND_FIELDS_IMG_LOC);
		return new StoreFrontConsultantPage(driver);
	}
}
