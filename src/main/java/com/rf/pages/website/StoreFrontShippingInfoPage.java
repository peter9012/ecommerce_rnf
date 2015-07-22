package com.rf.pages.website;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

public class StoreFrontShippingInfoPage extends RFWebsiteBasePage{

	private final By SHIPPING_PAGE_TEMPLATE_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top' and text()='Shipping info']");  
	private final By TOTAL_SHIPPING_ADDRESSES_LOC = By.xpath("//ul[@id='multiple-billing-profiles']/li");

	public StoreFrontShippingInfoPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public boolean verifyShippingInfoPageIsDisplayed(){
		driver.waitForElementPresent(SHIPPING_PAGE_TEMPLATE_HEADER_LOC);
		return driver.getCurrentUrl().contains(TestConstants.SHIPPING_PAGE_SUFFIX_URL);
	}

	public boolean isDefaultAddressRadioBtnSelected(String defaultAddressFirstNameDB) throws InterruptedException{
		Thread.sleep(2000);
		return driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstNameDB+"')]/ancestor::li[1]/form/span/input")).isSelected();
	}
	
	public boolean isDefaultShippingAddressSelected(String address1) throws InterruptedException{
		Thread.sleep(2000);
		System.out.println("UI "+driver.findElement(By.xpath("//ul[@id='multiple-billing-profiles']/li[1]/p[1]")).getText());
		return driver.findElement(By.xpath("//input[@name='addressCode' and @checked='checked']/ancestor::li[1]/p[1]")).getText().contains(address1);
	}	
	

	public boolean verifyAutoShipAddressTextNextToDefaultRadioBtn(String defaultAddressFirstNameDB){
		return driver.findElement(By.xpath("//span[contains(text(),'"+defaultAddressFirstNameDB+"')]/following::b[@class='AutoshipOrderAddress']")).getText().equals(TestConstants.AUTOSHIP_ADRESS_TEXT);
	}

	public int getTotalShippingAddressesDisplayed(){
		List<WebElement> totalShippingAddressesDisplayed = driver.findElements(TOTAL_SHIPPING_ADDRESSES_LOC);
		return totalShippingAddressesDisplayed.size();
	}
}
