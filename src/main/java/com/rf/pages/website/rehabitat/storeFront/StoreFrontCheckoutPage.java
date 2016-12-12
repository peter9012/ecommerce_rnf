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
	private final By SELECTED_SHIPPING_METHOD_LOC = By.xpath("//li[@class='checked']/label");
	private String shippingMethodLoc = "//label[contains(text(),'%s')]";
	private final By EDIT_LINK_OF_ORDERS_SUMMARY_LOC=By.xpath("//div[@class='price']/a[contains(text(),'Edit')]");
	private final By CANCEL_BUTTON_LOC=By.xpath("//button[contains(text(),'Cancel')]");
	private final By EDIT_LINK_OF_ACCOUNT_INFO_LOC=By.xpath("//a[@class='editIcon']");
	private String stateForShippingDetailsAtCheckoutPageLoc = "//form[@id='shippingAddressForm']//select[@id='address.region']//option[text()='%s']";

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

	/***
	 * This method clear the all shipping address field at checkout page
	 * 
	 * @param
	 * @return
	 * 
	 */
	public void clearAllFieldsForShippingAddressAtCheckoutPage(){
		driver.clear(FIRST_LAST_NAME_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("First & last Name field is cleared");
		driver.clear(ADDRESS_LINE1_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("address line 1 field is cleared");
		driver.clear(CITY_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("City field is cleared");
		driver.clear(POSTAL_CODE_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("Postal code field is cleared");
		driver.clear(PHONE_NUMBER_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC);
		logger.info("phone number field is cleared");
	}

	/***
	 * This method click on Edit of main account info at checkout page
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickEditLinkOfAccountInfo(){
		driver.click(EDIT_LINK_OF_ACCOUNT_INFO_LOC);
		logger.info("Clicked on Edit link of Account info");
		return this;
	}

	/***
	 * This method validates save account button is displayed
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isSaveButtonDisplayedForAccountInfo(){
		return driver.findElement(SAVE_BUTTON_LOC).isDisplayed();
	}

	/***
	 * This method validates shipping address fields are displayed at checkout page
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isshippindAddressFieldsAreDisplayedAtCheckoutPage(){
		return driver.findElement(FIRST_LAST_NAME_FOR_SHIPPING_AT_CHECKOUT_PAGE_LOC).isDisplayed();
	}

	/***
	 * This method click on cancel button of shipping address at checkout page
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickCancelButton(){
		driver.click(CANCEL_BUTTON_LOC);
		logger.info("Clicked on cancel button");
		return this;
	}

	/***
	 * This method click on Edit of order summary section at checkout page
	 * 
	 * @param
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickEditLinkOfOrderSummarySection(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(EDIT_LINK_OF_ORDERS_SUMMARY_LOC));
		logger.info("Clicked on Edit link of order summary section");
		return this;
	}

	/***
	 * This method validates cart page is present or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isCartPagePresent(){
		return driver.isElementPresent(CHECKOUT_BUTTON_LOC);
	}

	/***
	 * This method select the shipping method
	 * 
	 * @param shipping method name
	 * @return store front Checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage selectShippingMethod(String methodName){
		driver.click(By.xpath(String.format(shippingMethodLoc, methodName)));
		logger.info("Shipping method select as "+methodName);
		return this;
	}

	/***
	 * This method get the selected shipping method name
	 * 
	 * @param 
	 * @return shipping method name
	 * 
	 */
	public String getSelectedShippingMethodName(){
		driver.pauseExecutionFor(5000);
		String methodName = driver.findElement(SELECTED_SHIPPING_METHOD_LOC).getText();
		logger.info("Selected shipping method name is "+methodName);
		return methodName;
	}

	/***
	 * This method verify Use As Entered displayed in shipping profile or not
	 * 
	 * @param
	 * @return Boolean
	 * 
	 */
	public Boolean isUseAsEnteredPopupDisplayed(){
		return driver.findElement(USE_AS_ENTERED_BUTTON_LOC).isDisplayed();
	}

}

