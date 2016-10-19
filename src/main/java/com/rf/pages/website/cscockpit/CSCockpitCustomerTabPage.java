package com.rf.pages.website.cscockpit;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.rf.core.driver.website.RFWebsiteDriver;


public class CSCockpitCustomerTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitCustomerTabPage.class.getName());

	private static String orderTypeLoc = "//div[contains(text(),'Order Number')]/following::a[text()='%s']/following::span[2]";
	private static String orderSectionLoc ="//div[text()='%s']";
	private static String orderTypeCustomerTabLoc = "//div[@class='z-listbox-body']//a[contains(text(),'%s')]//following::td[2]//span";
	private static String orderDetailsLoc = "//span[contains(text(),'%s')]";
	private static String customerTypeLoc = "//span[contains(text(),'Customer Type')]/following::span[text()='%s']";
	private static String autoshipTemplateDetailsLoc = "//span[contains(text(),'Autoship Templates')]/following::div[contains(text(),'%s')]";
	private static String orderNumberLoc = "//div[@class='csSearchResults']/descendant::div[@class='z-listbox-body']//tbody[2]/tr[2]/td[1]//a[contains(text(),'%s')]";
	private static String autoshipIdStatusLoc = "//span[text()='Autoship Templates']/following::div[1]//div/a[text()='%s']/following::span[contains(text(),'pcAutoship')]/following::span[1]";
	private static String autoshipNumberWhoseAutoshipIsCancelledLoc = "//span[text()='Autoship Templates']/following::span[contains(text(),'Cancelled')]/preceding::a[text()='%s'][1]";
	private static String autoshipTemplateID = "//span[contains(text(),'Autoship Templates')]/following::a[text()='%s']";
	private static String autoshipIdStatusForCustomerLoc = "//span[text()='Autoship Templates']/following::a[contains(text(),'%s')]/following::span[contains(text(),'crpAutoship')]/following::span[1]";
	private static String creditCardOwnerName ="//div[contains(text(),'%s')]";
	private static String crpAutoshipIdWhoseAutoshipIsCancelledLoc = "//span[text()='Autoship Templates']/following::span[text()='crpAutoship']/../../..//span[contains(text(),'Cancelled')]/../../preceding-sibling::td//a[contains(text(),'%s')]";
	private static String statusOfAutoshipTemplateID ="//a[text()='%s']/ancestor::tr[1]/td[3]//span";

	private static final By FIRST_SHIPPED_ORDER_LINK_CUSTOMER_ORDER_SECTION = By.xpath("//span[contains(text(),'Customer Orders')]/following::div[contains(text(),'Order Status')][1]/following::span[text()='Shipped'][1]/preceding::a[1]");
	private static final By FIRST_SUBMITTED_ORDER_LINK_CUSTOMER_ORDER_SECTION = By.xpath("//span[contains(text(),'Customer Orders')]/following::div[contains(text(),'Order Status')][1]/following::span[text()='Shipped'][1]/preceding::a[1]");
	private static final By PLACE_ORDER_BUTTON = By.xpath("//td[contains(text(),'PLACE AN ORDER')]");	
	private static final By ORDER_NUMBER_IN_CUSTOMER_ORDER = By.xpath("//span[contains(text(),'Customer Orders')]/following::div[contains(text(),'Order Number')][1]/following::a[1]");
	private static final By FIRST_ORDER_LINK_CUSTOMER_ORDER_SECTION = By.xpath("//div[@class='csSearchResults']/descendant::div[@class='z-listbox-body']//tbody[2]/tr[2]/td[1]//a");
	private static final By ORDER_NUMBER_CUSTOMER_TAB_LOC = By.xpath("//div[@class='customerOrderHistoryWidget']//tr[2]//a");
	private static final By ACCOUNT_STATUS_ON_CUSTOMER_TAB_LOC = By.xpath("//span[contains(text(),'Account Status:')]/following::span[1]");
	private static final By ADD_CARD_BTN = By.xpath("//span[contains(text(),'Billing Information')]/following::td[text()='ADD CARD']");
	private static final By CREDIT_CARD_EDIT_BTN = By.xpath("//span[contains(text(),'Billing Information')]/following::div[1]//div[contains(@class,'listbox-body')]//tbody[2]/tr[1]//td[text()='EDIT']");
	private static final By EDIT_PAYMENT_PROFILE = By.xpath("//div[contains(text(),'EDIT PAYMENT PROFILE')]");
	private static final By SHIPPING_ADDRESS_EDIT_BUTTON = By.xpath("//span[text()='Customer Addresses']/following::div[1]//div[contains(@class,'listbox-body')]//tbody[2]/descendant::div[contains(text(),'Shipping Address')][1]/following::td[text()='Edit'][1]");
	private static final By EDIT_ADDRESS = By.xpath("//div[contains(text(),'Edit Address')]");
	private static final By CLOSE_POPUP_OF_EDIT_ADDRESS = By.xpath("//div[contains(text(),'Edit Address')]/div[contains(@id,'close')]");
	private static final By ADD_NEW_SHIPPING_ADDRESS = By.xpath("//span[contains(text(),'Customer Address')]/following::td[text()='Add']");
	private static final By CREATE_NEW_ADDRESS = By.xpath("//div[contains(text(),'Create New Address')]");
	private static final By AUTOSHIP_ID_FIRST = By.xpath("//span[text()='Autoship Templates']/following::div[contains(@class,'listbox-body')][1]//tr[2]//a");
	private static final By AUTOSHIP_ID_CONSULTANT_CUSTOMER_TAB_LOC = By.xpath("//span[contains(text(),'crpAutoship')]//preceding::td[1]//a");
	private static final By AUTOSHIP_ID_PC_CUSTOMER_TAB_LOC = By.xpath("//span[contains(text(),'pcAutoship')]//preceding::td[1]//a");
	private static final By AUTOSHIP_TEMPLATE = By.xpath("//span[text()='Autoship Templates']");
	private static final By CUSTOMER_ORDER_SECTION = By.xpath("//span[text()='Customer Orders']");
	private static final By CUSTOMER_BILLING_INFO = By.xpath("//span[text()='Billing Information']");
	private static final By CUSTOMER_ADDRESS = By.xpath("//span[text()='Customer Addresses']");
	private static final By AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP = By.xpath("//span[text()='Autoship Templates']/following::div[1]//div/span[text()='crpAutoship']/../../preceding-sibling::td//a");
	private static final By CREATE_PULSE_TEMPLATE_BTN = By.xpath("//td[contains(text(),'Create Pulse Template')]");
	private static final By CREATE_PULSE_TEMPLATE_BTN_ON_POPUP = By.xpath("//div[contains(text(),'Add PWS Prefix')]/following::td[contains(text(),'Create Pulse Template')]");
	private static final By NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE_LOC = By.xpath("//span[contains(text(),'Autoship Templates')]/following::div[@class='csWidgetContent'][1]//div[@class='z-listbox-body']//tbody[2]//tr[2]/td[7]//span");
	private static final By PULSE_AUTOSHIP_ID_HAVING_TYPE_AS_PULSE_AUTOSHIP = By.xpath("//span[text()='Autoship Templates']/following::span[text()='pulseAutoshipTemplate'][1]/../../preceding-sibling::td//a");
	private static final By SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT = By.xpath("//span[contains(text(),'Set as a Autoship Shipping Address')]/ancestor::td[contains(@style,'display:none;')]");
	private static final By SHIPPING_PROFILE_ERROR_POPUP_OK_BTN = By.xpath("//div[@class='z-window-modal']//td[text()='OK']");
	private static final By USE_THIS_ADDRESS = By.xpath("//td[contains(text(),'Use this Address')]");
	private static final By USE_ENTERED_ADDRESS = By.xpath("//td[contains(text(),'Use Entered Address')]");
	private static final By SHIPPING_ADDRESS_PROFILE_FIRST_NAME = By.xpath("//span[text()='Customer Addresses']/following::div[1]//div[contains(@class,'listbox-body')]//tbody[2]/descendant::div[contains(text(),'Shipping Address')][1]/ancestor::tr[1]/td[1]/div");
	private static final By SHIPPING_ADDRESS_PROFILE_LAST_NAME = By.xpath("//span[text()='Customer Addresses']/following::div[1]//div[contains(@class,'listbox-body')]//tbody[2]/descendant::div[contains(text(),'Shipping Address')][1]/ancestor::tr[1]/td[2]/div");
	private static final By SET_AS_AUTOSHIP_SHIPPING_ADDRESS_CHKBOX= By.xpath("//span[contains(text(),'Set as a Autoship Shipping Address')]/preceding::span[@class='z-checkbox'][1]/input");
	private static final By YES_BTN_OF_UPDATE_AUTOSHIP_ADDRESS_POPUP = By.xpath("//td[text()='Yes']");
	private static final By CREATE_NEW_ADDRESS_IN_SHIPPING_ADDRESS_POPUP = By.xpath("//td[contains(text(),'Create new address')]");
	private static final By UPDATE_ADDRESS_IN_SHIPPING_ADDRESS_POPUP = By.xpath("//td[contains(text(),'Update address')]");
	private static final By AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_PENDING = By.xpath("//span[text()='Autoship Templates']/following::span[text()='crpAutoship']/../../..//span[contains(text(),'PENDING')]/../../preceding-sibling::td//a");
	private static final By SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT_FOR_PENDING_AUTOSHIP = By.xpath("//span[contains(text(),'Set as a Autoship Shipping Address')]");
	private static final By AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_PENDING = By.xpath("//span[text()='Autoship Templates']/following::span[text()='pcAutoship']/../../..//span[contains(text(),'PENDING')]/../../preceding-sibling::td//a");
	private static final By CLOSE_POPUP_OF_CREATE_NEW_ADDRESS = By.xpath("//div[contains(text(),'Create New Address')]/div[contains(@id,'close')]");
	private static final By ADDRESS_CAN_NOT_BE_ADDED_POPUP = By.xpath("//span[contains(text(),'Address Cannot be added for Inactive user')]");
	private static final By OK_BTN_OF_ADDRESS_CAN_NOT_BE_ADDED_POPUP = By.xpath("//td[text()='OK']");
	private static final By NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE = By.xpath("//span[text()='Autoship Templates']/following::div[1]//div/span[text()='crpAutoship']/following::span[contains(text(),'PENDING')]/../../following::td[4]//span");
	private static final By PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_PENDING = By.xpath("//span[text()='Autoship Templates']/following::span[text()='pulseAutoshipTemplate']/../../..//span[contains(text(),'PENDING')]/../../preceding-sibling::td//a");
	private static final By PULSE_TEMPLATE_NEXT_DUE_DATE_STATUS_AS_PENDING = By.xpath("//span[text()='Autoship Templates']/following::span[text()='pulseAutoshipTemplate']/../../..//span[contains(text(),'PENDING')]/../../following::td[4]//span");
	private static final By RELOAD_PAGE_BTN_IN_LEFT_PANEL = By.xpath("//td[text()='Reload Page']");
	private static final By DEFAULT_SELECTED_SHIPPING_ADDRESS = By.xpath("//span[contains(text(),'Default shipping address')]/following::option[@selected='selected'][1]");
	private static final By AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_CANCELLED = By.xpath("//span[text()='Autoship Templates']/following::span[text()='pcAutoship']/../../..//span[contains(text(),'Cancelled')]/../../preceding-sibling::td//a");
	private static final By AUTOSHIP_TEMPLATE_WITH_NO_ENTRIES_TXT = By.xpath("//span[text()='Autoship Templates']/following::span[1][text()='No Entries']");
	private static final By SET_AS_AUTOSHIP_BILLING_PROFILE_CHK_BOX = By.xpath("//span[contains(text(),'Set as a Autoship Billing Profile')]/preceding::td[1]//input");
	private static final By PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_CANCELLED  = By.xpath("//span[text()='Autoship Templates']/following::span[text()='pulseAutoshipTemplate']/../../..//span[text()='Cancelled']/ancestor::tr/td[1]//a");
	private static final By AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_CANCELLED = By.xpath("//span[text()='Autoship Templates']/following::span[text()='crpAutoship']/../../..//span[contains(text(),'Cancelled')]/../../preceding-sibling::td//a");
	private static final By CREDIT_CARD_DROP_DOWN_IMG_ON_NEW_SHIPPING_ADDRESS_POPUP= By.xpath("//span[contains(text(),'Credit Card')]/following::img[1]");
	private static final By CREDIT_CARD_DROP_DOWN_FIRST_VALUE_ON_NEW_SHIPPING_ADDRESS_POPUP= By.xpath("//td[contains(text(),'Credit')]/following::td[2]");
	private static final By CVV_TXTFIELD_ON_NEW_SHIPPING_ADDRESS_POPUP= By.xpath("//span[text()='CVV']/following::input[1]");
	private static final By UPDATE_ADDRESS_IN_EDIT_PAYMENT_PROFILE_POPUP = By.xpath("//div[@class='csCreateAddressActions']//td[text()='Update address']");
	private static final By USE_ENTERED_ADDRESS_BTN_IN_QAS_POPUP = By.xpath("//td[contains(text(),'Use Entered Address')]");
	private static final By POPUP_ERROR_MESSAGE = By.xpath("//div[@class='z-messagebox']//span");
	private static final By CLOSE_POPUP_OF_EDIT_PAYMENT_PROFILE = By.xpath("//div[contains(text(),'EDIT PAYMENT PROFILE')]/div[contains(@id,'close')]");
	private static final By CLOSE_POPUP_OF_ADD_NEW_PAYMENT_PROFILE = By.xpath("//div[contains(text(),'ADD NEW PAYMENT PROFILE')]/div[contains(@id,'close')]");


	protected RFWebsiteDriver driver;

	public CSCockpitCustomerTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public void clickFirstOrderInCustomerTab(){
		driver.waitForElementPresent(FIRST_ORDER_LINK_CUSTOMER_ORDER_SECTION);
		driver.click(FIRST_ORDER_LINK_CUSTOMER_ORDER_SECTION);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickPlaceOrderButtonInCustomerTab(){
		driver.waitForElementPresent(PLACE_ORDER_BUTTON);
		driver.click(PLACE_ORDER_BUTTON);
	}

	public String getOrderTypeInCustomerTab(String orderNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderTypeLoc, orderNumber)));
		return driver.findElement(By.xpath(String.format(orderTypeLoc, orderNumber))).getText();
	}

	public String clickAndGetOrderNumberInCustomerTab(){
		driver.waitForElementPresent(ORDER_NUMBER_IN_CUSTOMER_ORDER);
		String orderNumber=driver.findElement(ORDER_NUMBER_IN_CUSTOMER_ORDER).getText();
		logger.info("Order number fetched is "+orderNumber);
		driver.click(ORDER_NUMBER_IN_CUSTOMER_ORDER);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return orderNumber;
	}

	public String getOrderNumberInCustomerTab() {
		driver.waitForElementPresent(ORDER_NUMBER_CUSTOMER_TAB_LOC);
		return driver.findElement(ORDER_NUMBER_CUSTOMER_TAB_LOC).getText();

	}

	public String validateAccountStatusOnCustomerTab() {
		driver.waitForElementPresent(ACCOUNT_STATUS_ON_CUSTOMER_TAB_LOC);
		return driver.findElement(ACCOUNT_STATUS_ON_CUSTOMER_TAB_LOC).getText();
	}
	public String getOrderTypeOnCustomerTab(String orderNumber){
		driver.waitForElementPresent(By.xpath(String.format(orderTypeCustomerTabLoc,orderNumber)));
		return driver.findElement(By.xpath(String.format(orderTypeCustomerTabLoc, orderNumber))).getText();
	}

	public String getOrderDetailsInCustomerTab(String CID){
		driver.waitForElementPresent(By.xpath(String.format(orderDetailsLoc, CID)));
		String orderNumber = driver.findElement(By.xpath(String.format(orderDetailsLoc, CID))).getText();
		logger.info("Selected Cutomer order number is = "+orderNumber);
		return orderNumber;
	}

	public boolean getAccountDetailsInCustomerTab(String details){
		driver.waitForElementPresent(By.xpath(String.format(orderDetailsLoc, details)));
		return driver.isElementPresent(By.xpath(String.format(orderDetailsLoc, details)));
	}

	public boolean verifyCustomerTypeIsPresentInCustomerTab(String customerType){
		driver.waitForElementPresent(By.xpath(String.format(customerTypeLoc, customerType)));
		return driver.isElementPresent(By.xpath(String.format(customerTypeLoc, customerType)));
	}

	public boolean verifyAutoshipTemplateDetailsInCustomerTab(String details){
		driver.waitForElementPresent(By.xpath(String.format(autoshipTemplateDetailsLoc, details)));
		return driver.isElementPresent(By.xpath(String.format(autoshipTemplateDetailsLoc, details)));
	}

	public String getAndClickFirstAutoshipIDInCustomerTab(){
		driver.waitForElementPresent(AUTOSHIP_ID_FIRST);
		String autoshipID = driver.findElement(AUTOSHIP_ID_FIRST).getText();
		driver.click(AUTOSHIP_ID_FIRST);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public boolean verifySectionsIsPresentInCustomerTab(String sectionName){
		driver.waitForElementPresent(By.xpath(String.format(orderSectionLoc, sectionName)));
		return driver.isElementPresent(By.xpath(String.format(orderSectionLoc, sectionName)));
	}

	public boolean isAddCardButtonPresentInCustomerTab(){
		driver.quickWaitForElementPresent(ADD_CARD_BTN);
		return driver.isElementPresent(ADD_CARD_BTN);  
	}

	public boolean isBillingListContainsBillingName(String billingProfileName){
		List<WebElement> allBillingProfiles=driver.findElements(By.xpath("//span[text()='Billing Information']/following::tr[contains(@class,'csListItem')]/td[2]/div"));
		for(WebElement billingProfile : allBillingProfiles){
			if(billingProfile.getText().contains(billingProfileName)){
				return true;
			}
		}
		return false;
	}

	public void clickEditBtnForTheBillingProfile(String billingProfile){
		driver.waitForElementPresent(By.xpath("//div[contains(text(),'"+billingProfile+"')]/following::td[text()='EDIT'][1]"));
		driver.click(By.xpath("//div[contains(text(),'"+billingProfile+"')]/following::td[text()='EDIT'][1]"));
	}

	public void clickAddCardButtonInCustomerTab(){
		driver.waitForElementPresent(ADD_CARD_BTN);
		driver.click(ADD_CARD_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isEditButtonForCreditCardPresentInCustomerTab(){
		driver.waitForElementPresent(CREDIT_CARD_EDIT_BTN);
		return driver.isElementPresent(CREDIT_CARD_EDIT_BTN);  
	}

	public void clickEditButtonForCreditCardInCustomerTab(){
		driver.waitForElementPresent(CREDIT_CARD_EDIT_BTN);
		driver.click(CREDIT_CARD_EDIT_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isEditPaymentProfilePopupPresentInCustomerTab(){
		driver.waitForElementPresent(EDIT_PAYMENT_PROFILE);
		return driver.isElementPresent(EDIT_PAYMENT_PROFILE);  
	}

	public void clickEditButtonOfShippingAddressInCustomerTab(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_EDIT_BUTTON);
		driver.click(SHIPPING_ADDRESS_EDIT_BUTTON);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isEditAddressPopupPresentInCustomerTab(){
		driver.waitForElementPresent(EDIT_ADDRESS);
		return driver.isElementPresent(EDIT_ADDRESS);  
	}

	public void clickCloseOfEditAddressPopUpInCustomerTab(){
		driver.waitForElementPresent(CLOSE_POPUP_OF_EDIT_ADDRESS);
		driver.click(CLOSE_POPUP_OF_EDIT_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickAddButtonOfCustomerAddressInCustomerTab(){
		driver.waitForElementPresent(ADD_NEW_SHIPPING_ADDRESS);
		driver.click(ADD_NEW_SHIPPING_ADDRESS);
		logger.info("Add shipping address button clicked");
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isCreateNewAddressPopupPresentInCustomerTab(){
		driver.waitForElementPresent(CREATE_NEW_ADDRESS);
		return driver.isElementPresent(CREATE_NEW_ADDRESS);  
	}

	public void clickAutoshipIdOnCustomerTab() {
		try{
			driver.quickWaitForElementPresent(AUTOSHIP_ID_CONSULTANT_CUSTOMER_TAB_LOC);
			driver.click(AUTOSHIP_ID_CONSULTANT_CUSTOMER_TAB_LOC);
			driver.waitForCSCockpitLoadingImageToDisappear();
		}catch(Exception e){
			driver.waitForElementPresent(AUTOSHIP_ID_PC_CUSTOMER_TAB_LOC);
			driver.click(AUTOSHIP_ID_PC_CUSTOMER_TAB_LOC);
			driver.waitForCSCockpitLoadingImageToDisappear();
		}
	}

	public void clickOnAutoShipId(String status,String isActive){
		List<WebElement> autoshipIdsList = driver.findElements(By.xpath("//span[text()='PENDING']/following::span[text()='Yes']/ancestor::tr[1]//a"));
		autoshipIdsList.get(0).click();
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyAutoshipTemplateSectionInCustomerTab(){
		driver.waitForElementPresent(AUTOSHIP_TEMPLATE);
		return driver.isElementPresent(AUTOSHIP_TEMPLATE);  
	}

	public boolean verifyCustomerOrderSectionInCustomerTab(){
		driver.quickWaitForElementPresent(CUSTOMER_ORDER_SECTION);
		return driver.isElementPresent(CUSTOMER_ORDER_SECTION);  
	}

	public boolean verifyCustomerBillingInfoSectionInCustomerTab(){
		driver.quickWaitForElementPresent(CUSTOMER_BILLING_INFO);
		return driver.isElementPresent(CUSTOMER_BILLING_INFO);  
	}

	public boolean verifyCustomerAddressSectionInCustomerTab(){
		driver.quickWaitForElementPresent(CUSTOMER_ADDRESS);
		return driver.isElementPresent(CUSTOMER_ADDRESS);  
	}

	public String getAndClickAutoshipIDHavingTypeAsCRPAutoshipInCustomerTab(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP);
		String autoshipID = driver.findElement(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public void clickCreatePulseTemplateBtn(){
		driver.waitForElementPresent(CREATE_PULSE_TEMPLATE_BTN);
		driver.click(CREATE_PULSE_TEMPLATE_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCreatePulseTemplateBtnOnPopup(){
		driver.waitForElementPresent(CREATE_PULSE_TEMPLATE_BTN_ON_POPUP);
		driver.click(CREATE_PULSE_TEMPLATE_BTN_ON_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getNextDueDateOfAutoshipTemplate(){
		driver.waitForElementPresent(NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE_LOC);
		return driver.findElement(NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE_LOC).getText();
	}

	public String convertPulseTemplateDate(String UIDate){
		String UIMonth=null;
		String[] splittedDate = UIDate.split("\\/");
		String date = splittedDate[1];
		String month = splittedDate[0];
		String year = splittedDate[2];  
		switch (Integer.parseInt(month)) {  
		case 1:
			UIMonth="January";
			break;
		case 2:
			UIMonth="February";
			break;
		case 3:
			UIMonth="March";
			break;
		case 4:
			UIMonth="April";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="June";
			break;
		case 7:
			UIMonth="July";
			break;
		case 8:
			UIMonth="August";
			break;
		case 9:
			UIMonth="September";
			break;
		case 10:
			UIMonth="October";
			break;
		case 11:
			UIMonth="November";
			break;
		case 12:
			UIMonth="December";
			break;  
		}
		System.out.println("Date is "+UIMonth+" "+date+", "+"20"+year);
		return date+" "+UIMonth+", "+year;
	}

	public String getAndClickPulseAutoshipIDHavingTypeAsPulseAutoshipTemplate(){
		driver.waitForElementPresent(PULSE_AUTOSHIP_ID_HAVING_TYPE_AS_PULSE_AUTOSHIP);
		String autoshipID = driver.findElement(PULSE_AUTOSHIP_ID_HAVING_TYPE_AS_PULSE_AUTOSHIP).getText();
		logger.info("Pulse Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(PULSE_AUTOSHIP_ID_HAVING_TYPE_AS_PULSE_AUTOSHIP);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public boolean isSetAsAutoshipShippingProfileTxtPresentInAddNewShippingProfilePopup(){
		driver.quickWaitForElementPresent(SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT);
		return driver.isElementPresent(SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT);  
	}

	public void clickCreateNewAddressBtn(){
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(CREATE_NEW_ADDRESS_IN_SHIPPING_ADDRESS_POPUP);
		driver.click(CREATE_NEW_ADDRESS_IN_SHIPPING_ADDRESS_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public void clickUpdateAddressBtn(){
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(UPDATE_ADDRESS_IN_SHIPPING_ADDRESS_POPUP);
		driver.click(UPDATE_ADDRESS_IN_SHIPPING_ADDRESS_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public boolean verifyAndClickShiipngAddressErrorPopupAndClickOkBtn(){
		driver.waitForElementPresent(SHIPPING_PROFILE_ERROR_POPUP_OK_BTN);
		boolean isPopupPresent = driver.isElementPresent(SHIPPING_PROFILE_ERROR_POPUP_OK_BTN);
		driver.click(SHIPPING_PROFILE_ERROR_POPUP_OK_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return isPopupPresent;
	}

	public void clickUseEnteredAddressBtn(){
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(USE_ENTERED_ADDRESS);
		driver.click(USE_ENTERED_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickUseThisAddressBtn(){
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(USE_THIS_ADDRESS);
		driver.click(USE_THIS_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getFirstShippingAddressProfileName(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_PROFILE_LAST_NAME);
		String firstname = driver.findElement(SHIPPING_ADDRESS_PROFILE_FIRST_NAME).getText();
		String lastname = driver.findElement(SHIPPING_ADDRESS_PROFILE_LAST_NAME).getText();
		String fullname = firstname+" "+lastname;
		return fullname;
	}

	public void clickSetAsAutoshipChkBoxInCreateNewAddressPopup(){
		driver.waitForElementPresent(SET_AS_AUTOSHIP_SHIPPING_ADDRESS_CHKBOX);
		driver.click(SET_AS_AUTOSHIP_SHIPPING_ADDRESS_CHKBOX);
	}

	public void clickOnYesOnUpdateAutoshipAddressPopup(){
		driver.waitForElementPresent(YES_BTN_OF_UPDATE_AUTOSHIP_ADDRESS_POPUP);
		driver.click(YES_BTN_OF_UPDATE_AUTOSHIP_ADDRESS_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_PENDING);
		String autoshipID = driver.findElement(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_PENDING).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_PENDING);
		driver.waitForCSCockpitLoadingImageToDisappear(60);
		return autoshipID;
	}

	public boolean isSetAsAutoshipShippingProfileTxtPresentInAddNewShippingProfilePopupForPendingAutoship(){
		driver.quickWaitForElementPresent(SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT_FOR_PENDING_AUTOSHIP);
		return driver.isElementPresent(SET_AS_AUTOSHIP_SHIPPING_PROFILE_TEXT_FOR_PENDING_AUTOSHIP);  
	}

	public String getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_PENDING);
		String autoshipID = driver.findElement(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_PENDING).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_PENDING);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public void clickCloseOfCreateNewAddressPopUpInCustomerTab(){
		driver.waitForElementPresent(CLOSE_POPUP_OF_CREATE_NEW_ADDRESS);
		driver.click(CLOSE_POPUP_OF_CREATE_NEW_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean addressCanNotBeAddedForInactiveUserInCustomerTab(){
		driver.isElementPresent(ADDRESS_CAN_NOT_BE_ADDED_POPUP);
		return driver.isElementPresent(ADDRESS_CAN_NOT_BE_ADDED_POPUP);  
	}

	public void clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab(){
		driver.waitForElementPresent(OK_BTN_OF_ADDRESS_CAN_NOT_BE_ADDED_POPUP);
		driver.click(OK_BTN_OF_ADDRESS_CAN_NOT_BE_ADDED_POPUP);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}


	public String getNextDueDateOfCRPAutoshipAndStatusIsPending(){
		driver.waitForElementPresent(NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE);
		return driver.findElement(NEXT_DUE_DATE_OF_AUTOSHIP_TEMPLATE).getText();
	}

	public String getAndClickPulseTemplateAutoshipIDHavingStatusIsPending(){
		if(isCreatePulseTemplateBtnPresent() == true){
			clickCreatePulseTemplateBtn();
			clickCreatePulseTemplateBtnOnPopup();
			driver.waitForCSCockpitLoadingImageToDisappear();
		}
		driver.waitForElementPresent(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_PENDING);
		String autoshipID = driver.findElement(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_PENDING).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_PENDING);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public boolean isCreatePulseTemplateBtnPresent(){
		driver.waitForElementPresent(CREATE_PULSE_TEMPLATE_BTN);
		return driver.isElementPresent(CREATE_PULSE_TEMPLATE_BTN);
	}

	public String getNextDueDateOfPulseAutoshipSubscriptionAndStatusIsPending(){
		driver.waitForElementPresent(PULSE_TEMPLATE_NEXT_DUE_DATE_STATUS_AS_PENDING);
		return driver.findElement(PULSE_TEMPLATE_NEXT_DUE_DATE_STATUS_AS_PENDING).getText();
	}

	public void clickOrderNumberInCustomerOrders(String orderNumber){
		driver.pauseExecutionFor(20000);
		if(driver.isElementPresent(By.xpath(String.format(orderNumberLoc,orderNumber)))==true){
			logger.info("Order found");
		}else{
			for(int i=0; i<=10; i++){
				driver.click(RELOAD_PAGE_BTN_IN_LEFT_PANEL);
				driver.pauseExecutionFor(5000);
				if(driver.isElementPresent(By.xpath(String.format(orderNumberLoc,orderNumber)))==true){
					break;
				}else{
					continue;
				}

			}
		}
		driver.waitForElementPresent(By.xpath(String.format(orderNumberLoc,orderNumber)));
		driver.findElement(By.xpath(String.format(orderNumberLoc, orderNumber))).click();
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getDefaultSelectedShippingAddressFromDropDown(){
		driver.waitForElementPresent(DEFAULT_SELECTED_SHIPPING_ADDRESS);
		logger.info("Default selected shipping address in customer tab is "+driver.findElement(DEFAULT_SELECTED_SHIPPING_ADDRESS).getText());
		return driver.findElement(DEFAULT_SELECTED_SHIPPING_ADDRESS).getText().trim();
	}

	public String getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsCancelled(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_CANCELLED);
		String autoshipID = driver.findElement(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_CANCELLED).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_CANCELLED);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public boolean isAutoshipTemplateHavingNoEntries(){
		driver.waitForElementPresent(AUTOSHIP_TEMPLATE_WITH_NO_ENTRIES_TXT);
		return driver.isElementPresent(AUTOSHIP_TEMPLATE_WITH_NO_ENTRIES_TXT);
	}

	public String getStatusOfAutoShipIdFromAutoshipTemplate(String autoshipId){
		driver.waitForElementPresent(By.xpath(String.format(autoshipIdStatusLoc, autoshipId)));
		String autoshipIDStatus = driver.findElement(By.xpath(String.format(autoshipIdStatusLoc, autoshipId))).getText();
		logger.info("Autoship id status from CS cockpit UI Is"+autoshipIDStatus);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipIDStatus;
	}

	public void clickAutoshipIDHavingTypeAsPCAutoshipAndStatusAsCancelled(String autoshipId){
		driver.waitForElementPresent(By.xpath(String.format(autoshipNumberWhoseAutoshipIsCancelledLoc, autoshipId)));
		driver.click(By.xpath(String.format(autoshipNumberWhoseAutoshipIsCancelledLoc, autoshipId)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickAutoshipTemplateIdViaUsingId(String id){
		driver.waitForElementPresent(By.xpath(String.format(autoshipTemplateID, id)));
		driver.click(By.xpath(String.format(autoshipTemplateID, id)));
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isPulseTemplateAutoshipIDHavingStatusIsPendingPresent(){
		driver.waitForElementPresent(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_PENDING);
		return driver.isElementPresent(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_PENDING);
	}

	public String getStatusOfAutoShipIdForCRPAutoshipTypeFromAutoshipTemplate(String autoshipId){
		driver.waitForElementPresent(By.xpath(String.format(autoshipIdStatusForCustomerLoc, autoshipId)));
		String autoshipIDStatus = driver.findElement(By.xpath(String.format(autoshipIdStatusForCustomerLoc, autoshipId))).getText();
		logger.info("Autoship id status from CS cockpit UI Is"+autoshipIDStatus);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipIDStatus;
	}

	public String getEmailAddressFromTopSectionInCustomerTabPage(){
		driver.waitForElementPresent(By.xpath("//div[@class='csObjectCustomerContainer']//span[contains(text(),'Email')]/following-sibling::span[1]"));
		return driver.findElement(By.xpath("//div[@class='csObjectCustomerContainer']//span[contains(text(),'Email')]/following-sibling::span[1]")).getText();
	}

	public String getUserNameAndCIDStringFromTopSectionInCustomerTabPage(){
		return driver.findElement(By.xpath("//div[@class='csObjectCustomerContainer']/descendant::span[contains(@class,'csCustomerNameLabelValue')][1]")).getText();
	}

	public boolean isNewlyAddedCreditCardPresent(String profileName){
		driver.waitForElementPresent(By.xpath(String.format(creditCardOwnerName, profileName)));
		return driver.isElementPresent(By.xpath(String.format(creditCardOwnerName, profileName)));
	}

	public void clickOnSetAsAutoshipBillingProfileChkBox(){
		driver.waitForElementPresent(SET_AS_AUTOSHIP_BILLING_PROFILE_CHK_BOX);
		driver.click(SET_AS_AUTOSHIP_BILLING_PROFILE_CHK_BOX);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isCRPAutoshipIDHavingStatusIsPendingPresent(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_PENDING);
		return driver.isElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_PENDING);
	}

	public String getAndClickPulseTemplateAutoshipIDHavingStatusIsCancelled(){
		driver.waitForElementPresent(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_CANCELLED);
		String cancelledPulseTemplateAutoshipID = driver.findElement(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_CANCELLED).getText();
		logger.info("Cancelled Pulse Template Autoship ID Is"+cancelledPulseTemplateAutoshipID);
		driver.click(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_CANCELLED);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return cancelledPulseTemplateAutoshipID;
	}

	public String getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsCancelled(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_CANCELLED);
		String autoshipID = driver.findElement(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_CANCELLED).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_CANCELLED);
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public void selectCreditCardDropDownImgOnNewShippingAddressPopUp(){
		driver.click(CREDIT_CARD_DROP_DOWN_IMG_ON_NEW_SHIPPING_ADDRESS_POPUP);
		driver.quickWaitForElementPresent(CREDIT_CARD_DROP_DOWN_FIRST_VALUE_ON_NEW_SHIPPING_ADDRESS_POPUP);
		driver.click(CREDIT_CARD_DROP_DOWN_FIRST_VALUE_ON_NEW_SHIPPING_ADDRESS_POPUP);
	}

	public void enterCVVOnNewShippingAddressPopUp(String cvv){
		driver.waitForElementPresent(CVV_TXTFIELD_ON_NEW_SHIPPING_ADDRESS_POPUP);
		driver.type(CVV_TXTFIELD_ON_NEW_SHIPPING_ADDRESS_POPUP,cvv);
	}


	public void clickUpdateAddressbtnInEditAddressPopup(){
		driver.waitForElementPresent(UPDATE_ADDRESS_IN_EDIT_PAYMENT_PROFILE_POPUP);
		driver.click(UPDATE_ADDRESS_IN_EDIT_PAYMENT_PROFILE_POPUP);
		//driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getPopupMessage(){
		driver.waitForElementPresent(POPUP_ERROR_MESSAGE);
		return driver.findElement(POPUP_ERROR_MESSAGE).getText();
	}

	public boolean isPCAutoshipIDHavingStatusIsPendingPresent(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_PENDING);
		return driver.isElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_PC_AUTOSHIP_STATUS_AS_PENDING);
	}

	public void clickCloseOfAddNewPaymentProfilePopUpInCustomerTab(){
		driver.waitForElementPresent(CLOSE_POPUP_OF_ADD_NEW_PAYMENT_PROFILE);
		driver.click(CLOSE_POPUP_OF_ADD_NEW_PAYMENT_PROFILE);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickCloseOfEditPaymentProfilePopUpInCustomerTab(){
		driver.waitForElementPresent(CLOSE_POPUP_OF_EDIT_PAYMENT_PROFILE);
		driver.click(CLOSE_POPUP_OF_EDIT_PAYMENT_PROFILE);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isPulseTemplateAutoshipIDHavingStatusIsCancelledPresent(){
		driver.waitForElementPresent(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_CANCELLED);
		return driver.isElementPresent(PULSE_TEMPLATE_AUTOSHIP_ID_STATUS_AS_CANCELLED);
	}

	public boolean isCRPAutoshipIDHavingStatusIsCancelledPresent(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_CANCELLED);
		return driver.isElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_CANCELLED);
	}

	public String getAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsCancelled(){
		driver.waitForElementPresent(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_CANCELLED);
		String autoshipID = driver.findElement(AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP_STATUS_AS_CANCELLED).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		return autoshipID;
	}

	public String getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsCancelled(String autoshipId){
		driver.waitForElementPresent(By.xpath(String.format(crpAutoshipIdWhoseAutoshipIsCancelledLoc, autoshipId)));
		String autoshipID = driver.findElement(By.xpath(String.format(crpAutoshipIdWhoseAutoshipIsCancelledLoc, autoshipId))).getText();
		logger.info("Autoship id from CS cockpit UI Is"+autoshipID);
		driver.click(By.xpath(String.format(crpAutoshipIdWhoseAutoshipIsCancelledLoc, autoshipId)));
		driver.waitForCSCockpitLoadingImageToDisappear();
		return autoshipID;
	}

	public boolean isOrderPresentInCustomerOrderSection(){
		driver.waitForElementPresent(FIRST_ORDER_LINK_CUSTOMER_ORDER_SECTION);
		return driver.isElementPresent(FIRST_ORDER_LINK_CUSTOMER_ORDER_SECTION);  
	}

	public boolean isShippedOrSubmittedOrderPresentInCustomerOrderSection(){
		driver.waitForElementPresent(FIRST_SHIPPED_ORDER_LINK_CUSTOMER_ORDER_SECTION);
		return driver.isElementPresent(FIRST_SHIPPED_ORDER_LINK_CUSTOMER_ORDER_SECTION )||driver.isElementPresent(FIRST_SUBMITTED_ORDER_LINK_CUSTOMER_ORDER_SECTION);
	}

	public String getStatusOfAutoshipID(String autoshipId){
		driver.waitForElementPresent(By.xpath(String.format(statusOfAutoshipTemplateID, autoshipId)));
		String status = driver.findElement(By.xpath(String.format(statusOfAutoshipTemplateID, autoshipId))).getText();
		logger.info("Status of Autoship id from CS cockpit UI Is"+status);
		return status;
	}


}

