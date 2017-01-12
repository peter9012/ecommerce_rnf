package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

import java.util.List;

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
	private final By BILLING_PROFILE_SECTION_HEADER = By.xpath("//h3[@class='sub-header' and contains(text(),'BILLING PROFILE')]");
	private final By BILLING_PROFILES_DETAILS_LIST_LOC = By.xpath("//div[@class='account-paymentdetails account-list']");
	private final By BILLING_INFO_SECTION_HEADER_LOC = By.xpath("//h2[contains(@class,'account-section-header') and contains(text(),'BILLING INFO')]");
	private final By BILLING_ADDRESS_DD_OPTIONS_LOC = By.xpath("//select[@id='billingAddress.addressId']/option");
	private final By PROFILES_IN_BILLING_LIST_LOC = By.xpath("//div[@class='account-cards card-select']//div[@class='row']/div[contains(@class,'card')]"); 
	private final By SET_AS_DEFAULT_BILLING_PROFILE_BUTTON_LOC = By.xpath("//div[@class='myModal' and contains(@style,'display')]//div[@class='modal-content']//a[contains(text(),'Set as Default')]");
	private final By DEFAULT_BILLING_PROFILE_ADDRESS_NAME_LOC = By.xpath("//div[@class='account-paymentdetails account-list']//strong[contains(text(),'Default')]/ancestor::li[1]");
	private final By DEFAULT_PROFILE_DELETE_BTN_LOC = By.xpath("//div[@class='account-paymentdetails account-list']//strong[contains(text(),'Default')]//ancestor::ul[1]/following-sibling::div[contains(@class,'account-cards-actions')]/a[contains(text(),'Delete')]");
	private final By SUCCESSFUL_ACTION_MSG_LOC = By.xpath("//div[@class='global-alerts']/div[@class='alert alert-info alert-dismissable']");

	private String creditCardNumberForSpecificBillingProfileLoc = "//div[@class='account-paymentdetails account-list']//li[text()='%s']/following-sibling::li[contains(text(),'Credit')]";
	private String creditCardExpDateForSpecificBillingProfileLoc = "//div[@class='account-paymentdetails account-list']//li[text()='%s']/following-sibling::li[contains(text(),'Expiration')]";
	private String billingAddressForSpecificBillingProfileLoc = "//div[@class='account-paymentdetails account-list']//li[text()='%s']/following-sibling::li[not(contains(text(),'Credit card') or contains(text(),'Expiration Date'))]";
	private String billingProfileFirstNameLoc = "//div[@class='account-paymentdetails account-list']//li[contains(text(),'%s')]";
	private String setDefaultSpecificBillingProfileRadioBtnLoc = "//div[@class='account-paymentdetails account-list']//li[text()='%s']/ancestor::ul/following-sibling::div[contains(@class,'set-default')]/a[contains(text(),'Default')]";
	private String defaultTagForSpecficBillingProfileLoc = "//div[@class='account-paymentdetails account-list']//li[text()='%s']/strong[contains(text(),'(Default)')]";
	private String deleteActionForSpecificBillingProfileLoc = "//div[@class='account-paymentdetails account-list']//li[text()='%s']/ancestor::ul/following-sibling::div[contains(@class,'account-cards-actions')]/a[contains(text(),'Delete')]";


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
	 * This method validates the Card Details Added Successful Msg.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isCardDetailsAddedSuccessfulMsgAppearedAsExpected(String expectedMsg){
		return driver.getText(CARD_DETAILS_SUCCESSFULLY_ADDED_MSG_LOC).contains(expectedMsg);
	}

	/***
	 * This method validates the Disability of Billing Profile Card Details Fields.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isCardDetailsFieldsDisabled(){
		boolean flag = driver.findElement(NAME_ON_CARD_TF_LOC).isEnabled() &&
				driver.findElement(ACCOUNT_NUM_ON_CARD_TF_LOC).isEnabled() &&
				driver.findElement(CARD_TYPE_DD_LOC).isEnabled() &&
				driver.findElement(CARD_EXPIRY_MONTH_DD_LOC).isEnabled() &&
				driver.findElement(CARD_EXPIRY_YEAR_DD_LOC).isEnabled() &&
				driver.findElement(CARD_CVV_NUM_TF_LOC).isEnabled();
		return !flag;
	}

	/***
	 * This method validates the Billing Info Header.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isBillingInfoHeaderIsPresent(){
		return driver.isElementVisible(BILLING_INFO_SECTION_HEADER_LOC);
	}

	/***
	 * This method validates the Billing Profile Header.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isBillingProfilesSectionHeaderIsPresent(){
		return driver.isElementVisible(BILLING_PROFILE_SECTION_HEADER);
	}

	/***
	 * This method validates the Billing Profiles List.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isBillingProfileDetailsListIsPresent(){
		return driver.isElementVisible(BILLING_PROFILES_DETAILS_LIST_LOC);
	}

	/***
	 * This method get The Credit card last 4 digit from Specfic Billing Profile Details. 
	 * 
	 * @param String
	 * @return String
	 * 
	 */
	public String getLastFourDigitOfCreditCardNumberForSpecificBillingProfile(String profile){
		String creditCardLastFourDigit = driver.getText(By.xpath(String.format(creditCardNumberForSpecificBillingProfileLoc, profile))).replaceAll("[^-?0-9]+","");
		return creditCardLastFourDigit;
	}

	/***
	 * This method validates the last 4 digit of credit card number.
	 * 
	 * @param String , String
	 * @return boolean value
	 * 
	 */
	public boolean isLastFourDigitMatchesWithSixteenDigitCardNumber(String cardNum, String lastFourDigit){
		return cardNum.endsWith(lastFourDigit);
	}

	/***
	 * This method get the Billing Address from Specfic Billing Profile Details. 
	 * 
	 * @param String
	 * @return String
	 * 
	 */
	public String getBillingAddressForSpecificBillingProfile(String profile){
		List<WebElement> addressElements = driver.findElements(By.xpath(String.format(billingAddressForSpecificBillingProfileLoc, profile)));
		String addressString = "";
		for(WebElement element : addressElements){
			addressString = addressString + element.getText().trim() + " ";
		}
		return addressString.trim();
	}

	/***
	 * This method validates the Expiry Date of Card from Specfic Billing Profile Details. 
	 * 
	 * @param String
	 * @return boolean
	 * 
	 */
	public boolean isExpiryDateOfCardIsPresentForSpecificProfile(String profile){
		String creditCardExpDate = driver.getText(By.xpath(String.format(creditCardExpDateForSpecificBillingProfileLoc, profile))).replaceAll("[^-?0-9]+"," ").trim();
		String[] splittedDate = creditCardExpDate.split(" ");
		return (splittedDate[0].length() == 2) && (splittedDate[1].length() == 4); 
	}

	/***
	 * This method validates the Card Details Updated Successful Msg.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isCardDetailsUpdatedSuccessfulMsgAppearedAsExpected(String expectedMsg){
		return driver.getText(CARD_DETAILS_SUCCESSFULLY_ADDED_MSG_LOC).contains(expectedMsg);
	}

	/***
	 * This method validates the presence of billing addresses dropdown.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isBillingAddressDropdownIsPresent(){
		return driver.isElementVisible(BILLING_ADDRESS_DD_LOC);
	}

	/***
	 * This method get the count of addresses present in Billing address dropdown.
	 * 
	 * @param 
	 * @return int
	 * 
	 */
	public int getCountOfBillingAddressesPresentInDropdown(){
		return driver.findElements(BILLING_ADDRESS_DD_OPTIONS_LOC).size();
	}

	/***
	 * This method get the count of billing profiles present in Billing details List section.
	 * 
	 * @param 
	 * @return int
	 * 
	 */
	public int getCountOfBillingProfilesPresentInProfilesListSection(){
		return driver.findElements(PROFILES_IN_BILLING_LIST_LOC).size();
	}

	/***
	 * This method get the count of billing profiles present in Billing details List section.
	 * 
	 * @param 
	 * @return StoreFrontBillingInfoPage
	 * 
	 */
	public StoreFrontBillingInfoPage selectRandomAddressFromBillingAddressDropdown(int index){
		Select select = new Select(driver.findElement(BILLING_ADDRESS_DD_LOC));
		select.selectByIndex(index);
		return this;
	}

	/***
	 * This method get the default Billing profile name 
	 * 
	 * @param 
	 * @return String
	 * 
	 */
	public String getDefaultBillingProfileName(){
		logger.info("Default Profile Name : "+driver.getText(DEFAULT_BILLING_PROFILE_ADDRESS_NAME_LOC));
		return driver.getText(DEFAULT_BILLING_PROFILE_ADDRESS_NAME_LOC).replace("(DEFAULT)","").trim();
	}


	/***
	 * This method set the specific profile to be default profile.
	 * 
	 * @param 
	 * @return StoreFrontBillingInfoPage
	 * 
	 */
	public StoreFrontBillingInfoPage setProfileAsDefault(String profile){
		driver.click(By.xpath(String.format(setDefaultSpecificBillingProfileRadioBtnLoc,profile)));
		logger.info("Default button is clicked for Billing Profile : "+profile);
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(SET_AS_DEFAULT_BILLING_PROFILE_BUTTON_LOC));
		logger.info("Set as default option is selected from Popup");
		return this;
	}

	/***
	 * This method validates the presence of Delete option for Default profile 
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */
	public boolean isDeleteOptionAvailableForDefaultProfile(){
		return driver.isElementVisible(DEFAULT_PROFILE_DELETE_BTN_LOC);
	}

	/***
	 * This method validates that Profile get updated as default profile.
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */
	public boolean isProfileGetUpdatedAsDefaultProfile(String profile, String defaultProfile){
		logger.info("Profile Name : "+profile);
		logger.info("Default : "+defaultProfile);
		return defaultProfile.contains(profile);
	}


	/***
	 * This method validates that Profile is default or not.
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */
	public boolean isProfileIsDefaultProfile(String profile){
		return driver.isElementVisible(By.xpath(String.format(defaultTagForSpecficBillingProfileLoc, profile)));
	}


	/***
	 * This method validates the presence of Delete option for Specific Profile 
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */
	public boolean isDeleteOptionAvailableForProfile(String profile){
		return driver.isElementVisible(By.xpath(String.format(deleteActionForSpecificBillingProfileLoc,profile)));
	}

	/***
	 * This method clicked on add a New Billing Profile link 
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontBillingInfoPage clickAddNewBillingProfileLink(){
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(ADD_NEW_BILLING_PROFILE_LOC));
		logger.info("Add New Billing Profile link clicked");
		return this;
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
		driver.navigate().refresh();
		return this;
	}

	/***
	 * This method clicked on add a New Billing Address link 
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontBillingInfoPage clickAddNewBillingAddressLink(){
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(ADD_NEW_BILLING_ADDRESS_LINK_LOC));
		logger.info("Add New Billing Address link clicked");
		return this;
	}

	/***
	 * This method clicked on Edit Button of Default Billing Profile  
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontBillingInfoPage clickOnDefaultBillingProfileEditButton(){
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(DEFAULT_BILLING_PROFILE_EDIT_BTN_LOC));
		logger.info("Default Billing Profile Edit Button Clicked");
		return this;
	}

	/***
	 * This method validates the Card Details Added Successfull Msg.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isActionSuccessfulMsgAppearedAsExpected(String expectedMsg){
		return driver.getText(SUCCESSFUL_ACTION_MSG_LOC).contains(expectedMsg);
	}

	/***
	 * This method get the count of Default Billing Profiles.
	 * 
	 * @param 
	 * @return int
	 * 
	 */
	public int getCountOfDefaultBillingProfiles(){
		return driver.findElements(DEFAULT_BILLING_PROFILE_ADDRESS_NAME_LOC).size();
	}

}
