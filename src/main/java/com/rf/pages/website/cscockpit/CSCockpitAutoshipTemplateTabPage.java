package com.rf.pages.website.cscockpit;

import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class CSCockpitAutoshipTemplateTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitAutoshipTemplateTabPage.class.getName());

	private static String orderDetailsLoc= "//span[contains(text(),'Order Detail Items')]/following::div[contains(text(),'%s')]";
	private static String orderFromAutoShipTemplateSectionLoc= "//span[text()='Number of Consecutive Autoship Orders From Template']/../../following-sibling::div[1]/div[2]/div/div[1]//tbody[2]//th/div[text()='%s']";
	private static String orderNoteUILoc="//span[contains(text(),'%s')]";
	private static String appliedPromotionsLoc= "//span[contains(text(),'Applied Promotions')]/following::div[contains(text(),'%s')]";

	private static final By SHIPPING_ADDRESS_NAME = By.xpath("//div[@class='csWidgetContent']//span[contains(text(),'Shipping Address')]/../following::div[1]/span");
	private static final By SHIPPING_ADDRESS_LINE_1 = By.xpath("//div[@class='csWidgetContent']//span[contains(text(),'Shipping Address')]/../following::div[2]/span");
	private static final By SHIPPING_ADDRESS_LOCALE_POSTCODE_REGION = By.xpath("//div[@class='csWidgetContent']//span[contains(text(),'Shipping Address')]/../following::div[3]/span");
	private static final By SHIPPING_ADDRESS_COUNTRY = By.xpath("//div[@class='csWidgetContent']//span[contains(text(),'Shipping Address')]/../following::div[4]/span");
	private static final By PAYMENT_ADDRESS_NAME = By.xpath("//span[text()='Payment Info ']/../following::div[1]/span");
	private static final By ORDER_FROM_THIS_AUTOSHIP_TEMPLATE_COMPONENT_TEXT = By.xpath("//div[@class='csConsecutiveOrders']/span[1]");
	private static final By UPDATE_BUTTON_AUTOSHIP_TEMPLATE_TAB = By.xpath("//div[@class='csConsecutiveOrders']/span[1]/following-sibling::span/span/table[@class='csUpdateConsOrders z-button ']//tr/td[text()='Update']");
	private static final By RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB_ANOTHER= By.xpath("//td[text()='Run Now']/ancestor::table[@class='z-button']");
	private static final By CANCEL_EDIT_LINK = By.xpath("//td[contains(text(),'Cancel Edit')]");
	private static final By FIRST_REMOVE_LINK_OF_ORDER_DETAIL = By.xpath("//div[contains(@class,'csWidgetListbox')]/div[2]//tbody/tr[1]/td[10]//a[text()='Remove']");
	private static final By THRESHOLD_POPUP = By.xpath("//span[text()='Your Total SV value should be greater than the threshold 80.0']");
	private static final By THRESHOLD_POPUP_OK = By.xpath("//td[text()='OK']");
	private static final By ADD_MORE_LINES_LINK = By.xpath("//td[text()='Add More Lines']");
	private static final By SUBTOTAL_VALUE = By.xpath("//span[text()='Subtotal:']/following-sibling::span");
	private static final By QUANTITY_TEXT_BOX_OF_SECOND_PRODUCT = By.xpath("//div[contains(@class,'csWidgetListbox')]/div[2]//tbody/tr[2]/td[8]/div/input");
	private static final By UPDATE_QUANTITY_LINK_OF_SECOND_PRODUCT = By.xpath("//div[contains(@class,'csWidgetListbox')]/div[2]//tbody/tr[2]/td[9]/div/a");
	private static final By ORDER_NOTE_TEXT_BOX = By.xpath("//div[@class='csWidgetContent']//tr/td//span[text()='Order Notes: ']/following::textarea"); 
	private static final By ORDER_NOTE_ADD_BUTTON = By.xpath("//div[@class='csWidgetContent']//tr/td//span[text()='Order Notes: ']/following::tr/td[text()='ADD']");
	private static final By REMOVE_LINK_OF_ORDER_DETAIL = By.xpath("//a[text()='Remove']");
	private static final By RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB = By.xpath("//td[text()='Run Now']");
	private static final By CONFIRM_MESSAGE_YES_LOC = By.xpath("//td[text()='Yes']");
	private static final By CONFIRM_MESSAGE_OK_LOC = By.xpath("//td[text()='OK']");
	private static final By AUTOSHIP_TEMPLATE_HEADER = By.xpath("//span[text()='Autoship Template #']");
	private static final By APPLIED_PROMOTION_SECTION = By.xpath("//span[text()='Applied Promotions']");
	private static final By SHIPPING_ADDRESS = By.xpath("//div[@class='csWidgetContent']//span[contains(text(),'Shipping Address')]");
	private static final By PAYMENT_INFO = By.xpath("//span[text()='Payment Info ']");
	private static final By ORDER_FROM_THIS_AUTOSHIP_TEMPLATE = By.xpath("//span[text()='Orders From this Autoship Template']");
	private static final By AUTOSHIP_TEMPLATE_CID_AUTOSHIP_ID = By.xpath("//span[text()='Autoship Template #']/following-sibling::span");
	private static final By CANCEL_AUTOSHIP = By.xpath("//a[text()='Cancel Autoship']");
	private static final By CANCEL_AUTOSHIP_DISABLE_LINK = By.xpath("//a[text()='Cancel Autoship' and @style='display:none;']");
	private static final By EDIT_AUTOSHIP_ANOTHER_LINK = By.xpath("//td[text()='Edit Template']/ancestor::table[contains(@style,'moz-user-select')]");
	private static final By EDIT_AUTOSHIP_LINK = By.xpath("//td[text()='Edit Template']");
	private static final By PLUS_LINK_NEXT_TO_FIRST_PRODUCT = By.xpath("//span[text()='Order Detail Items']/following::div/div//a[text()='+'][1]");
	private static final By VIEW_PRODUCT_PAGE_LINK = By.xpath("//a[text()='View Product Page']");
	private static final By MINUS_LINK_NEXT_TO_FIRST_PRODUCT = By.xpath("//span[text()='Order Detail Items']/following::div/div//a[text()='-'][1]");
	private static final By VIEW_PRODUCT_DETAIL = By.xpath("//input[@value='ADD TO BAG']");

	protected RFWebsiteDriver driver;
	public CSCockpitAutoshipTemplateTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public void clickOkConfirmMessagePopUp(){   
		try{
			driver.click(CONFIRM_MESSAGE_OK_LOC);
			driver.waitForCSCockpitLoadingImageToDisappear();
		}catch(NoSuchElementException e){

		}
	}

	public boolean verifyAutoshipTemplateHeaderSectionInAutoshipTemplateTab(){
		driver.isElementPresent(AUTOSHIP_TEMPLATE_HEADER);
		return driver.isElementPresent(AUTOSHIP_TEMPLATE_HEADER);  
	}

	public boolean verifyShippingAddressSectionInAutoshipTemplateTab(){
		driver.isElementPresent(SHIPPING_ADDRESS);
		return driver.isElementPresent(SHIPPING_ADDRESS);  
	}

	public boolean verifyPaymentInfoSectionInAutoshipTemplateTab(){
		driver.isElementPresent(PAYMENT_INFO);
		return driver.isElementPresent(PAYMENT_INFO);  
	}

	public boolean verifyOrderFromThisAutoshipTemplateSectionInAutoshipTemplateTab(){
		driver.isElementPresent(ORDER_FROM_THIS_AUTOSHIP_TEMPLATE);
		return driver.isElementPresent(ORDER_FROM_THIS_AUTOSHIP_TEMPLATE);  
	}

	public String getAutoshipTemplateCIDFromAutoshipTemplateSectionInAutoshipTemplateTab(){
		driver.waitForElementPresent(AUTOSHIP_TEMPLATE_CID_AUTOSHIP_ID);
		String autoshipTemplateCID = driver.findElement(AUTOSHIP_TEMPLATE_CID_AUTOSHIP_ID).getText();
		return autoshipTemplateCID;
	}

	public boolean verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(){
		driver.isElementPresent(CANCEL_AUTOSHIP_DISABLE_LINK);
		return driver.isElementPresent(CANCEL_AUTOSHIP_DISABLE_LINK);  
	}

	public boolean verifyEditAutoshipTemplateLinkInAutoshipTemplateTab(){
		driver.isElementPresent(EDIT_AUTOSHIP_ANOTHER_LINK);
		return driver.isElementPresent(EDIT_AUTOSHIP_ANOTHER_LINK);  
	}

	public boolean verifyAppliedPromotionSectionInAutoshipTemplateTab(){
		driver.isElementPresent(APPLIED_PROMOTION_SECTION);
		return driver.isElementPresent(APPLIED_PROMOTION_SECTION);  
	}

	public boolean verifyOrderDetailsInAutoshipTemplateTab(String details){
		driver.waitForElementPresent(By.xpath(String.format(orderDetailsLoc, details)));
		return driver.isElementPresent(By.xpath(String.format(orderDetailsLoc, details)));
	}

	public void clickPlusButtonNextToProductInAutoshipTemplateTab() {
		driver.waitForElementPresent(PLUS_LINK_NEXT_TO_FIRST_PRODUCT);
		driver.click(PLUS_LINK_NEXT_TO_FIRST_PRODUCT);
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyViewProductPageLinkInAutoshipTemplateTab(){
		driver.waitForElementPresent(VIEW_PRODUCT_PAGE_LINK);
		return driver.isElementPresent(VIEW_PRODUCT_PAGE_LINK);
	}

	public void clickMinusButtonNextToProductInAutoshipTemplateTab() {
		driver.waitForElementPresent(MINUS_LINK_NEXT_TO_FIRST_PRODUCT);
		driver.click(MINUS_LINK_NEXT_TO_FIRST_PRODUCT);
		driver.waitForLoadingImageToDisappear();
	}

	public boolean verifyPlusButtonNextToProductInAutoshipTemplateTab(){
		driver.waitForElementPresent(PLUS_LINK_NEXT_TO_FIRST_PRODUCT);
		return driver.isElementPresent(PLUS_LINK_NEXT_TO_FIRST_PRODUCT);
	}

	public String getShippingAddressNameInAutoshipTemplateTab(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_NAME);
		String shippingAddName = driver.findElement(SHIPPING_ADDRESS_NAME).getText();
		return shippingAddName;
	}

	public String getShippingAddressLine1InAutoshipTemplateTab(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_LINE_1);
		String shippingAddLine1 = driver.findElement(SHIPPING_ADDRESS_LINE_1).getText();
		return shippingAddLine1;
	}

	public String getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_LOCALE_POSTCODE_REGION);
		String shippingAddDetails = driver.findElement(SHIPPING_ADDRESS_LOCALE_POSTCODE_REGION).getText();
		return shippingAddDetails;
	}

	public String getShippingAddressCountryInAutoshipTemplateTab(){
		driver.waitForElementPresent(SHIPPING_ADDRESS_COUNTRY);
		String shippingAddCountry = driver.findElement(SHIPPING_ADDRESS_COUNTRY).getText();
		return shippingAddCountry;
	}

	public String getPaymentAddressNameInAutoshipTemplateTab(){
		driver.waitForElementPresent(PAYMENT_ADDRESS_NAME);
		String paymentAddName = driver.findElement(PAYMENT_ADDRESS_NAME).getText();
		return paymentAddName;
	}

	public String getTextOfOrderFromAutoshipTemplateInAutoshipTemplateTab(){
		driver.waitForElementPresent(ORDER_FROM_THIS_AUTOSHIP_TEMPLATE_COMPONENT_TEXT);
		String textOfOrderFromAutoshipTemplate = driver.findElement(ORDER_FROM_THIS_AUTOSHIP_TEMPLATE_COMPONENT_TEXT).getText();
		return textOfOrderFromAutoshipTemplate;
	}

	public void clickRunNowButtonOnAutoshipTemplateTab() {
		driver.waitForElementPresent(RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB);
		driver.click(RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB);
		driver.waitForCSCockpitLoadingImageToDisappear();	  
	}

	public void clickOkForRegeneratedIdpopUp() {
		try{
			if(driver.isElementPresent(CONFIRM_MESSAGE_YES_LOC) && driver.findElement(CONFIRM_MESSAGE_YES_LOC).isDisplayed()){
				driver.click(CONFIRM_MESSAGE_YES_LOC);
				driver.waitForCSCockpitLoadingImageToDisappear(); 
			}
		}catch(NoSuchElementException nse){

		}

	}
	public boolean verifyConfirmMessagePopUpIsAppearing() {
		return driver.isElementPresent(CONFIRM_MESSAGE_OK_LOC);
	}

	public boolean verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(String sectionName){
		driver.waitForElementPresent(By.xpath(String.format(orderFromAutoShipTemplateSectionLoc, sectionName)));
		return driver.isElementPresent(By.xpath(String.format(orderFromAutoShipTemplateSectionLoc, sectionName)));
	}

	public boolean verifyUpdateLinkInOrderFromAutoshipTemplateInAutoshipTemplateTab(){
		return driver.isElementPresent(UPDATE_BUTTON_AUTOSHIP_TEMPLATE_TAB);		    
	}

	public boolean verifyRunNowLinkInOrderFromAutoshipTemplateInAutoshipTemplateTab(){
		return driver.isElementPresent(RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB_ANOTHER);		   
	}

	public void clickEditTemplateLinkInAutoshipTemplateTab() {
		driver.waitForElementPresent(EDIT_AUTOSHIP_LINK);
		driver.click(EDIT_AUTOSHIP_LINK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyCancelEditLinkInAutoshipTemplateTab(){
		return driver.isElementPresent(CANCEL_EDIT_LINK);		    
	}

	public void clickRemoveLinkOfOrderDetailInAutoShipTemplateTab(){
		driver.waitForElementPresent(FIRST_REMOVE_LINK_OF_ORDER_DETAIL);
		driver.click(FIRST_REMOVE_LINK_OF_ORDER_DETAIL);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}
	public boolean verifyThresholdPopupInAutoshipTemplateTab(){
		return driver.isElementPresent(THRESHOLD_POPUP);
	}

	public void clickOKOfThresholdPopupInAutoshipTemplateTab(){
		driver.waitForElementPresent(THRESHOLD_POPUP_OK);
		driver.click(THRESHOLD_POPUP_OK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void clickAddMoreLinesLinkInAutoShipTemplateTab(){
		driver.waitForElementPresent(ADD_MORE_LINES_LINK);
		driver.click(ADD_MORE_LINES_LINK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public String getSubtotalInAutoshipTemplateTab(){
		driver.waitForElementPresent(SUBTOTAL_VALUE);
		String autoshipTemplateCID = driver.findElement(SUBTOTAL_VALUE).getText();
		String []subtotal=autoshipTemplateCID.split("\\$");
		String value=subtotal[1];
		logger.info("subtotal fetched is "+value);
		return value;
	}

	public void updateQuantityOfSecondProduct(String qty){
		driver.waitForElementPresent(QUANTITY_TEXT_BOX_OF_SECOND_PRODUCT);
		driver.type(QUANTITY_TEXT_BOX_OF_SECOND_PRODUCT, qty);
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(UPDATE_QUANTITY_LINK_OF_SECOND_PRODUCT);
		driver.click(UPDATE_QUANTITY_LINK_OF_SECOND_PRODUCT);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public void enterOrderNote(String orderNote){
		driver.waitForElementPresent(ORDER_NOTE_TEXT_BOX);
		driver.type(ORDER_NOTE_TEXT_BOX, orderNote);
		driver.waitForElementPresent(ORDER_NOTE_ADD_BUTTON);
		driver.click(ORDER_NOTE_ADD_BUTTON);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyOrderNoteAdded(String orderNoteText){
		return driver.isElementPresent(By.xpath(String.format(orderNoteUILoc, orderNoteText)));
	}

	public void removeProductInOrderDetailInAutoshipTemplateTab(String subtotal){
		double subtotalValue=Double.parseDouble(subtotal);
		int size=driver.findElements(REMOVE_LINK_OF_ORDER_DETAIL).size();
		if(subtotalValue>100.00 && size>=2){
			clickRemoveLinkOfOrderDetailInAutoShipTemplateTab();
			if(driver.isElementPresent(THRESHOLD_POPUP)){
				clickOKOfThresholdPopupInAutoshipTemplateTab();
				updateQuantityOfSecondProduct("2");
				clickRemoveLinkOfOrderDetailInAutoShipTemplateTab();
			}else{
				logger.info("Either subtotal is not greater than 80 or count of product is less than 2");
			}
		}
	}

	public boolean verifyProductDetailOfNewTabInAutoshipTemplateTab(){
		driver.waitForElementPresent(VIEW_PRODUCT_DETAIL);
		return driver.isElementPresent(VIEW_PRODUCT_DETAIL);
	}

	public void clickViewProductPageLinkInAutoshipTemplateTemplateTab(){
		driver.waitForElementPresent(VIEW_PRODUCT_PAGE_LINK);
		driver.click(VIEW_PRODUCT_PAGE_LINK);
		driver.waitForCSCockpitLoadingImageToDisappear();
	}

	public boolean verifyAppliedPromotionsInAutoshipTemplateTab(String details){
		try{
			driver.waitForElementPresent(By.xpath(String.format(appliedPromotionsLoc, details)));
			return driver.isElementPresent(By.xpath(String.format(appliedPromotionsLoc, details)));
		}catch(NoSuchElementException e){
			logger.info("No entries in applied promotion section");
		}
		return false;

	}
}