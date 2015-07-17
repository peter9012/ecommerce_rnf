package com.rf.pages.website;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontUpdateCartPage extends RFWebsiteBasePage{

	public StoreFrontUpdateCartPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void clickUpdateCartBtn(){
		driver.click(By.xpath("//input[@value='UPDATE CART']"));
	}

	public void clickOnEditPaymentBillingProfile(){
		driver.findElement(By.xpath("//a[@class='editPayment']")).click();
	}

	public boolean isNewBillingProfileIsSelectedByDefault(String profileName){
		return driver.findElement(By.xpath("//ul[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/following::span[@class='radio-button billtothis'][1]/input")).isSelected();
	}

	public String getNameOnPaymentProfile(){
		return driver.findElement(By.xpath("//p[@id='selectedPaymentInfo']/span[1]")).getText();
	}
	
	public StoreFrontConsultantPage clickRodanAndFieldsLogo(){
		driver.findElement(By.xpath("//img[@title='Rodan+Fields']")).click();
		return new StoreFrontConsultantPage(driver);
	}
}
