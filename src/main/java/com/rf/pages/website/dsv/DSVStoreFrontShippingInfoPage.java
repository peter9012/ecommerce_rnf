package com.rf.pages.website.dsv;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;

public class DSVStoreFrontShippingInfoPage extends DSVRFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(DSVStoreFrontShippingInfoPage.class.getName());
	
	private static final By ADD_A_NEW_SHIPPING_ADDRESS_LINK = By.xpath("//a[contains(text(),'Add new shipping address')]");
	private static final By NAME_TXT_FIELD = By.id("new-attention");
	private static final By ADDRESS_LINE_1_TXT_FIELD = By.id("new-address-1");
	private static final By CITY_TXT_FIELD = By.id("townCity");
	private static final By PROVINCE_DROP_DOWN = By.id("state");
	private static final By POSTAL_CODE_TXT_FIELD = By.id("postcode");
	private static final By PHONE_NUMBER_TXT_FIELD = By.id("phonenumber");
	private static final By CARD_NUMBER_DROP_DOWN = By.id("cardDropDowndropdown");
	private static final By SECURITY_CODE_TXT_FIELD = By.id("security-code");
	private static final By SAVE_ADDRESS_BTN = By.id("saveShippingAddress");
	private static final By VERIFY_YOUR_SHIPPING_ADDRESS_POPUP = By.id("QAS_AcceptOriginal");
	private static String ShippingProfiles = "//div[@id='multiple-billing-profiles']//span[contains(@class,'font-bold')][contains(text(),'%s')]";
	
	
	public DSVStoreFrontShippingInfoPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	public void clickAddANewShippingAddressLink(){
		driver.waitForLoadingImageToDisappear();
		driver.quickWaitForElementPresent(ADD_A_NEW_SHIPPING_ADDRESS_LINK);
		driver.pauseExecutionFor(2000);
		driver.click(ADD_A_NEW_SHIPPING_ADDRESS_LINK);
	}
	
	public void enterNewShippingAddressDetails(String name,String addressLine1,String city,String postalCode,String phoneNumber,String securityCode) throws Exception{
		logger.info("Shipping Name entered is "+name);
		driver.waitForElementPresent(NAME_TXT_FIELD);
		driver.type(NAME_TXT_FIELD, name);
		driver.type(ADDRESS_LINE_1_TXT_FIELD, addressLine1);
		driver.type(CITY_TXT_FIELD, city);
		Select provinceDD= new Select(driver.findElement(PROVINCE_DROP_DOWN));
		provinceDD.selectByIndex(1);
		driver.type(POSTAL_CODE_TXT_FIELD, postalCode);
		driver.type(PHONE_NUMBER_TXT_FIELD, phoneNumber);
//		Select cardNumberDD = new Select(driver.findElement(CARD_NUMBER_DROP_DOWN));
//		cardNumberDD.selectByIndex(1);
//		driver.type(SECURITY_CODE_TXT_FIELD, securityCode);
	}
	
	public void clickSaveAddressBtn(){
		driver.click(SAVE_ADDRESS_BTN);
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(VERIFY_YOUR_SHIPPING_ADDRESS_POPUP);
			driver.click(VERIFY_YOUR_SHIPPING_ADDRESS_POPUP);
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){
			logger.info("'Verify Your Shipping Address' popUp has not displayed");			
		}
		driver.waitForPageLoad();
	}	
	
	public boolean isShippingProfilePresentonPage(String shippingName){
		driver.quickWaitForElementPresent(By.xpath(String.format(ShippingProfiles, shippingName)));
		return driver.isElementPresent(By.xpath(String.format(ShippingProfiles, shippingName)));		
	}
	
	public void clickEditShippingProfileLink(String shippingName){
		driver.click(By.xpath(String.format(ShippingProfiles, shippingName)+"/following::a[text()='Edit'][1]"));
	}
	
	public void enterNameWithCardNumberAndSecurityCode(String shippingName,String securityCode){
		logger.info("Shipping name entered for edit is "+shippingName);
		driver.waitForElementPresent(NAME_TXT_FIELD);
		driver.type(NAME_TXT_FIELD, shippingName);
//		Select cardNumberDD = new Select(driver.findElement(CARD_NUMBER_DROP_DOWN));
//		cardNumberDD.selectByIndex(1);
//		driver.type(SECURITY_CODE_TXT_FIELD, securityCode);
	}

}
