package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontCheckoutPage extends StoreFrontWebsiteBasePage{
	public StoreFrontCheckoutPage(RFWebsiteDriver driver) {
		super(driver);		
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontCheckoutPage.class.getName());

	private final By FIRST_NAME_LOC = By.id("first-name");
	private final By LAST_NAME_LOC = By.id("last-name");
	private final By EMAIL_LOC = By.id("email-account");
	private final By PASSWORD_LOC = By.xpath("//form[@id='registerForm']//input[@id='password']");
	private final By CONFIRM_PASSWORD_LOC = By.xpath("//form[@id='registerForm']//input[@id='the-password-again']");
	private final By PC_PERKS_CHECKBOX_LOC = By.xpath("//input[@id='c2']/following::label[1]");
	private final By CREATE_ACCOUNT_BUTTON_LOC = By.id("next-button");
	private final By ADD_NEW_SHIPPING_ADDRESS_BUTTON_LOC = By.xpath("//button[contains(text(),'Add New')]");
	private final By SHIPPING_ADDRESS_NAME_LOC = By.xpath("//span[@id='defaultShippingAddress']/b");
	private final By EDIT_LINK_OF_SHIPPING_ADDRESS_LOC=By.xpath("//div[@class='checkout-shipping']//a[1]");
	private final By FIRST_LAST_NAME_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//input[@id='address.firstName']");
	private final By ADDRESS_LINE1_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//input[@id='address.line1']");
	private final By CITY_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//input[@id='address.townCity']");
	private final By STATE_DD_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//select[@id='address.region']");
	private final By POSTAL_CODE_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//input[@id='address.postcode']");
	private final By PHONE_NUMBER_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC=By.xpath("//form[@id='shippingAddressForm']//input[@id='address.phone']");
	private final By QUEBEC_PROVINCE_FOR_SHIPPING_LOC=By.xpath("//option[@disabled='disabled' and text()='Quebec']");
	
	public StoreFrontCheckoutPage fillNewUserDetails(String userType,String firstName,String lastName,String email,String password){
		driver.type(FIRST_NAME_LOC, firstName);
		logger.info("first name entered as "+firstName);
		driver.type(LAST_NAME_LOC, lastName);
		logger.info("last name entered as "+lastName);
		driver.type(EMAIL_LOC, email+"\t");
		logger.info("email entered as "+email);
		driver.type(PASSWORD_LOC,password);
		logger.info("password entered as "+password);
		driver.type(CONFIRM_PASSWORD_LOC,password);
		logger.info("confirm entered as "+password);
		if(userType.equals(TestConstants.USER_TYPE_PC)){
			driver.click(PC_PERKS_CHECKBOX_LOC);
			logger.info("PC perks checkbox is checked");
		}
		return this;
	}

	public void clickCreateAccountButton(){
		driver.click(CREATE_ACCOUNT_BUTTON_LOC);
		logger.info("'Create Account' button clicked");
	}


	/***
	 * This method click the save button
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickAddNewShippingAddressButton(){
		driver.click(ADD_NEW_SHIPPING_ADDRESS_BUTTON_LOC);
		logger.info("Add new shipping address button clicked");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method get the default shipping profile name
	 * 
	 * @param
	 * @return profile name
	 * 
	 */
	public String getDefaultShippingAddressNameAtCheckoutPage(){
		driver.pauseExecutionFor(2000);
		String profileName = driver.findElement(SHIPPING_ADDRESS_NAME_LOC).getText();
		logger.info("default profile name is "+profileName);
		return profileName;
	}

	/***
	 * This method click on Edit Shipping Address at checkout page
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickEditLinkOfShippingAddress(){
		driver.click(EDIT_LINK_OF_SHIPPING_ADDRESS_LOC);
		logger.info("Clicked on Edit link of Shipping Address");
		return this;
	}

	private String stateForShippingDetailsAtCheckoutPageLoc = "//form[@id='shippingAddressForm']//select[@id='address.region']//option[text()='%s']";
	/**
	 * This method update the shipping address details at checkout page
	 * 
	 * @param firstName, lastName, addressLine1, city, state, postal code, phone number
	 * @return
	 */
	public StoreFrontCheckoutPage updateShippingAddressDetailsAtCheckoutPage(String firstName, String lastName, String addressLine1, String city, String state, String postal, String phoneNumber) {
		String completeName = firstName+" "+lastName;
		driver.pauseExecutionFor(5000);
		driver.type(FIRST_LAST_NAME_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE1_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(CITY_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetailsAtCheckoutPageLoc, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(PHONE_NUMBER_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);
		return this;
	}

	/***
	 * This method validates the error message for postal code field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isQuebecAddressDisabledForConsultant(){
		driver.click(STATE_DD_FOR_REGISTRATION_LOC);
		logger.info("State dropdown clicked");
		return driver.isElementPresent(QUEBEC_PROVINCE_FOR_SHIPPING_LOC);
	}

}

