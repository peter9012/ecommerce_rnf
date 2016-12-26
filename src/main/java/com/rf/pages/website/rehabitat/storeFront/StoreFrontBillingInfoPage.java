package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontBillingInfoPage extends StoreFrontWebsiteBasePage{

	public StoreFrontBillingInfoPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontBillingInfoPage.class.getName());

	private final By ADD_NEW_BILLING_PROFILE_LOC = By.xpath("//a[@class='add-new-billing-profile']");
	private final By SAVE_BUTTON_FOR_BILLING_DETAILS_LOC = By.xpath("//button[@id='save-account-payment-Details']");
	private final By ADD_NEW_BILLING_INFO_HEADER_LOC = By.xpath("//form[@id='accountPaymentDetailsForm']//h3[contains(text(),'ADD NEW BILLING INFO')]");
	private final By ADD_NEW_BILLING_ADDRESS_LINK_LOC = By.xpath("//a[@class='add-new-billing-address']");
	private final By ADD_NEW_BILLING_ADDRESS_BLOCK_LOC = By.xpath("//div[@id='account-billing-address-form']");
	private final By ADD_NEW_BILLING_ADDRESS_BLOCK_HEADER_LOC = By.xpath("//h3[@id='addressHeading']");
	private final By STREET_ERROR_MSG_LOC = By.xpath("//h3[@id='addressHeading']/following-sibling::p[@id='errorMessage']");
	private final By ADDRESS_SUGGESTION_MODAL_LOC = By.xpath("//div[@id='cboxLoadedContent']/div[@class='modal-body']");
	private final By ADDRESS_SUGGESTION_MODAL_CLOSE_BTN_LOC = By.xpath("//button[@id='cboxClose']");
	private final By CARD_DETAILS_SUCCESSFULLY_ADDED_MSG_LOC = By.xpath("//div[@class='global-alerts']/div[@class='alert alert-info alert-dismissable']");
	private final By DEFAULT_BILLING_PROFILE_EDIT_BTN_LOC = By.xpath("//div[@class='account-paymentdetails account-list']//strong[contains(text(),'Default')]//ancestor::ul[1]/following-sibling::div/a[contains(text(),'Edit')]");
	private final By NAME_ON_CARD_TF_LOC  = By.xpath("//input[@id='card_nameOnCard']");
	private final By ACCOUNT_NUM_ON_CARD_TF_LOC = By.xpath("//input[@id='card_accountNumber']");
	private final By CARD_TYPE_DD_LOC = By.xpath("//input[@id='card_cardType']");
	private final By CARD_EXPIRY_MONTH_DD_LOC = By.xpath("//select[@id='card_ExpiryMonth']");
	private final By CARD_EXPIRY_YEAR_DD_LOC = By.xpath("//select[@id='card_ExpiryYear']");
	private final By CARD_CVV_NUM_TF_LOC = By.xpath("//input[@id='card_cvNumber']");
	private String billingProfileFirstNameLoc = "//div[@class='account-paymentdetails account-list']//li[text()='%s']";



	/***
	 * This method clicked on add a New Billing Profile link 
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontBillingInfoPage clickAddNewBillingProfileLink(){
		driver.click(ADD_NEW_BILLING_PROFILE_LOC);
		logger.info("Add New Billing Profile link clicked");
		return this;
	}


	/***
	 * This method click the save button for Billing details
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontBillingInfoPage clickSaveButtonForBillingDetails(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SAVE_BUTTON_FOR_BILLING_DETAILS_LOC));
		logger.info("Save button clicked");
		return this;
	}



	/***
	 * This method validates the SubHeader After Clicking 'Add new Billing Profile' Link
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isAddNewBillingInfoSubHeaderPresent(){
		return driver.isElementVisible(ADD_NEW_BILLING_INFO_HEADER_LOC);
	}

	/***
	 * This method clicked on add a New Billing Address link 
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontBillingInfoPage clickAddNewBillingAddressLink(){
		driver.click(ADD_NEW_BILLING_ADDRESS_LINK_LOC);
		logger.info("Add New Billing Address link clicked");
		return this;
	}


	/***
	 * This method validates the Add New Billing Address Form.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isAddNewBillingAddressFormDisplayed(){
		return driver.getAttribute(ADD_NEW_BILLING_ADDRESS_BLOCK_LOC, "style").contains("block");
	}


	/***
	 * This method validates the Add New Billing Address Block Header.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isAddNewBillingAddressBlockHeaderPresent(){
		return driver.getText(ADD_NEW_BILLING_ADDRESS_BLOCK_HEADER_LOC).equalsIgnoreCase("Add new Billing address");
	}



	/***
	 * This method validates the Error Message for Wrong Address.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isUnknownAddressErrorMessageIsPresentAsExpected(){
		return driver.getText(STREET_ERROR_MSG_LOC).equalsIgnoreCase("Unknown street");
	}


	/***
	 * This method fetch the suggested address in modal. 
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String getSuggestedBillingAddressFromBlock(){
		logger.info(driver.getText(ADDRESS_SUGGESTION_MODAL_LOC));
		return driver.getText(ADDRESS_SUGGESTION_MODAL_LOC);
	}


	/***
	 * This method clicked on close Button of Address Suggestion Modal  
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontBillingInfoPage clickOnAddressSuggestionModalCloseBtn(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(ADDRESS_SUGGESTION_MODAL_CLOSE_BTN_LOC));
		logger.info("Address Suggestion Modal Close Button clicked");
		driver.pauseExecutionFor(3000);
		return this;
	}


	/***
	 * This method validates the Creation of New Billing Profile using firstName of Billing Address.
	 * 
	 * @param String
	 * @return boolean value
	 * 
	 */
	public boolean isNewBillingProfilePresentInRowList(String profileFirstName){
		return driver.isElementVisible(By.xpath(String.format(billingProfileFirstNameLoc,profileFirstName)));
	}


	/***
	 * This method validates the Card Details Added Successfull Msg.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isCardDetailsAddedSuccessfulMsgAppearedAsExpected(){
		return driver.getText(CARD_DETAILS_SUCCESSFULLY_ADDED_MSG_LOC).contains("Card details are added successfully.");
	}


	/***
	 * This method clicked on Edit Button of Default Billing Profile  
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontBillingInfoPage clickOnDefaultBillingProfileEditButton(){
		driver.click(DEFAULT_BILLING_PROFILE_EDIT_BTN_LOC);
		logger.info("Default Billing Profile Edit Button Clicked");
		return this;
	}

}

