package com.rf.pages.website.cscockpit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
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

	private static final By PLACE_ORDER_BUTTON = By.xpath("//td[contains(text(),'PLACE AN ORDER')]");	
	private static final By ORDER_NUMBER_IN_CUSTOMER_ORDER = By.xpath("//span[contains(text(),'Customer Orders')]/following::div[contains(text(),'Order Number')][1]/following::a[1]");
	private static final By FIRST_ORDER_LINK_CUSTOMER_ORDER_SECTION = By.xpath("//div[@class='csSearchResults']/descendant::div[@class='z-listbox-body']//tbody[2]/tr[2]/td[1]//a");
	private static final By ORDER_NUMBER_CUSTOMER_TAB_LOC = By.xpath("//div[@class='customerOrderHistoryWidget']//tr[2]//a");
	private static final By ACCOUNT_STATUS_ON_CUSTOMER_TAB_LOC = By.xpath("//span[contains(text(),'Account Status:')]/following::span[1]");
	private static final By ADD_CARD_BTN = By.xpath("//span[contains(text(),'Billing Information')]/following::td[text()='ADD CARD']");
	private static final By CREDIT_CARD_EDIT_BTN = By.xpath("//span[contains(text(),'Billing Information')]/following::div[1]//div[contains(@class,'listbox-body')]//tbody[2]/tr[1]//td[text()='EDIT']");
	private static final By ADD_NEW_PAYMENT_PROFILE = By.xpath("//div[contains(text(),'ADD NEW PAYMENT PROFILE')]");
	private static final By EDIT_PAYMENT_PROFILE = By.xpath("//div[contains(text(),'EDIT PAYMENT PROFILE')]");
	private static final By SHIPPING_ADDRESS_EDIT_BUTTON = By.xpath("//span[text()='Customer Addresses']/following::div[1]//div[contains(@class,'listbox-body')]//tbody[2]/tr[1]//td[text()='Edit']");
	private static final By EDIT_ADDRESS = By.xpath("//div[contains(text(),'Edit Address')]");
	private static final By CLOSE_POPUP_OF_EDIT_ADDRESS = By.xpath("//div[contains(text(),'Edit Address')]/div[contains(@id,'close')]");
	private static final By ADD_NEW_SHIPPING_ADDRESS = By.xpath("//span[contains(text(),'Customer Address')]/following::td[text()='Add']");
	private static final By CREATE_NEW_ADDRESS = By.xpath("//div[contains(text(),'Create New Address')]");
	private static final By AUTOSHIP_ID_FIRST = By.xpath("//span[text()='Autoship Templates']/following::div[1]//div[contains(@class,'listbox-body')]//tr[2]//a");
	private static final By AUTOSHIP_ID_CONSULTANT_CUSTOMER_TAB_LOC = By.xpath("//span[contains(text(),'crpAutoship')]//preceding::td[1]//a");
	private static final By AUTOSHIP_ID_PC_CUSTOMER_TAB_LOC = By.xpath("//span[contains(text(),'pcAutoship')]//preceding::td[1]//a");
	private static final By AUTOSHIP_TEMPLATE = By.xpath("//span[text()='Autoship Templates']");
	private static final By CUSTOMER_ORDER_SECTION = By.xpath("//span[text()='Customer Orders']");
	private static final By CUSTOMER_BILLING_INFO = By.xpath("//span[text()='Billing Information']");
	private static final By CUSTOMER_ADDRESS = By.xpath("//span[text()='Customer Addresses']");
	private static final By AUTOSHIP_ID_HAVING_TYPE_AS_CRP_AUTOSHIP = By.xpath("//span[text()='Autoship Templates']/following::div[1]//div/span[text()='crpAutoship']/../../preceding-sibling::td//a");

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
		driver.isElementPresent(ADD_CARD_BTN);
		return driver.isElementPresent(ADD_CARD_BTN);  
	}

	public void clickAddCardButtonInCustomerTab(){
		driver.isElementPresent(ADD_CARD_BTN);
		driver.click(ADD_CARD_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isEditButtonForCreditCardPresentInCustomerTab(){
		driver.isElementPresent(CREDIT_CARD_EDIT_BTN);
		return driver.isElementPresent(CREDIT_CARD_EDIT_BTN);  
	}

	public boolean isAddNewPaymentProfilePopupPresentInCustomerTab(){
		driver.isElementPresent(ADD_NEW_PAYMENT_PROFILE);
		return driver.isElementPresent(ADD_NEW_PAYMENT_PROFILE);  
	}

	public void clickEditButtonForCreditCardInCustomerTab(){
		driver.isElementPresent(CREDIT_CARD_EDIT_BTN);
		driver.click(CREDIT_CARD_EDIT_BTN);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isEditPaymentProfilePopupPresentInCustomerTab(){
		driver.isElementPresent(EDIT_PAYMENT_PROFILE);
		return driver.isElementPresent(EDIT_PAYMENT_PROFILE);  
	}

	public void clickEditButtonOfShippingAddressInCustomerTab(){
		driver.isElementPresent(SHIPPING_ADDRESS_EDIT_BUTTON);
		driver.click(SHIPPING_ADDRESS_EDIT_BUTTON);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isEditAddressPopupPresentInCustomerTab(){
		driver.isElementPresent(EDIT_ADDRESS);
		return driver.isElementPresent(EDIT_ADDRESS);  
	}

	public void clickCloseOfEditAddressPopUpInCustomerTab(){
		driver.waitForElementPresent(CLOSE_POPUP_OF_EDIT_ADDRESS);
		driver.click(CLOSE_POPUP_OF_EDIT_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickAddButtonOfCustomerAddressInCustomerTab(){
		driver.isElementPresent(ADD_NEW_SHIPPING_ADDRESS);
		driver.click(ADD_NEW_SHIPPING_ADDRESS);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean isCreateNewAddressPopupPresentInCustomerTab(){
		driver.isElementPresent(CREATE_NEW_ADDRESS);
		return driver.isElementPresent(CREATE_NEW_ADDRESS);  
	}

	public void clickAutoshipIdOnCustomerTab() {
		try{
			driver.quickWaitForElementPresent(AUTOSHIP_ID_CONSULTANT_CUSTOMER_TAB_LOC);
			driver.click(AUTOSHIP_ID_CONSULTANT_CUSTOMER_TAB_LOC);
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){
			driver.waitForElementPresent(AUTOSHIP_ID_PC_CUSTOMER_TAB_LOC);
			driver.click(AUTOSHIP_ID_PC_CUSTOMER_TAB_LOC);
			driver.waitForLoadingImageToDisappear();
		}
	}

	public boolean verifyAutoshipTemplateSectionInCustomerTab(){
		driver.isElementPresent(AUTOSHIP_TEMPLATE);
		return driver.isElementPresent(AUTOSHIP_TEMPLATE);  
	}

	public boolean verifyCustomerOrderSectionInCustomerTab(){
		driver.isElementPresent(CUSTOMER_ORDER_SECTION);
		return driver.isElementPresent(CUSTOMER_ORDER_SECTION);  
	}

	public boolean verifyCustomerBillingInfoSectionInCustomerTab(){
		driver.isElementPresent(CUSTOMER_BILLING_INFO);
		return driver.isElementPresent(CUSTOMER_BILLING_INFO);  
	}

	public boolean verifyCustomerAddressSectionInCustomerTab(){
		driver.isElementPresent(CUSTOMER_ADDRESS);
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


}