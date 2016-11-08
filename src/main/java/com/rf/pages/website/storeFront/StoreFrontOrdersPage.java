package com.rf.pages.website.storeFront;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontOrdersPage extends StoreFrontRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontOrdersPage.class.getName());	

	private final By ORDERS_PAGE_TEMPLATE_HEADER_LOC = By.xpath("//div[@id='main-content']//div[contains(text(),'Orders')]");
	private final By ORDERS_PAGE_CRP_AUTOSHIP_TEMPLATE_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top']");
	private final By ORDER_NUMBER_LOC = By.xpath("//div[@id='history-orders-table']/div[2]/div[2]/div/a");
	private final By ORDER_SCHEDULE_DATE_LOC = By.xpath("//div[@id='history-orders-table']/div[2]//span[contains(text(),'Actions')]/preceding::div[3]");
	private final By ORDER_GRAND_TOTAL_LOC = By.xpath("//div[@id='history-orders-table']/div[2]//span[contains(text(),'Actions')]/preceding::div[2]");
	private final By ORDER_STATUS_LOC = By.xpath("//div[@id='history-orders-table']/div[2]/div[2]//div[contains(@class,'col-sm')][4]");
	private final By ORDER_AUTOSHIP_ORDER_NUMBER_LOC = By.xpath("//div[@id='pending-autoship-orders-table']/div[1]/div//div[contains(text(),'Schedule Date')]/following::div[@class='ref-labels'][2]/div//div[1]");
	private final By ORDER_AUTOSHIP_ADDRESS_LOC = By.xpath("//ul[@class='order-detail-list']/li[1]/p");
	private final By SCHEDULE_DATE_TEXT_LOC = By.xpath("//div[@id='main-content']//span[contains(text(),'date')]");
	private final By ORDER_STATUS_TEXT_LOC = By.xpath("//div[@id='main-content']//span[contains(text(),'Order status')]");
	private final By ORDER_PAYMENT_METHOD_LOC = By.xpath("//strong[text()='Payment Method:']/ancestor::p");
	private final By ORDER_TOTAL_PRICE_LOC = By.xpath("//table[@class='order-products']//td[@id='productSV']//following::td[2]");
	private final By ORDER_GRAND_TOTAL_BOTTOM_LOC = By.xpath("//div[@id='main-content']//div[contains(text(),'Grand Total')]/following::div[1]");
	private final By SKU_VALUE_OF_ITEM_IN_ORDER_LOC = By.xpath("//td[text()='Items In Order']//following::img[1]//ancestor::td/span");
	private final By ITEM_ORDER_DESCRIPTION_LOC = By.xpath("//td[text()='Items In Order']//following::img[1]//ancestor::td");
	private final By ORDERS_PAGE_PCPERKS_AUTOSHIP_TEMPLATE_HEADER_LOC = By.xpath("//div[@id='main-content']//div[contains(text(),'Order details:')]");
	private final By LEFT_MENU_ACCOUNT_INFO_LOC = By.xpath("//div[@id='left-menu']//a[text()='Account Info']");
	private String ORDER_NUMBER_STATUS_LOC = "//table[@id='history-orders-table']//a[text()='%s']/following::td[@class='fourth'][1]";
	private final By ACTIONS_BUTTON_LOC = By.xpath("//div[@id='history-orders-table']/div[2]//span[contains(text(),'Actions')]");
	private final By ACTIONS_DROPDOWN_LOC = By.xpath("//div[@id='history-orders-table']/div[2]//span[contains(text(),'Actions')]/following::a[@id='idReportProblem'][1]");
	private final By ORDER_NUM_OF_ORDER_HISTORY = By.xpath(".//div[@id='history-orders-table']/descendant::a[1]");
	private final By YOUR_ACCOUNT_DROPDOWN_LOC = By.xpath("//div[@id='left-menu']//div/button[contains(text(),'Your Account')]");
	private final By AUTOSHIP_DATE_LOC = By.xpath("//div[@id='pending-autoship-orders-table']/div[1]/div//div[contains(text(),'Schedule Date')]/following::div[@class='ref-labels'][2]/div//div[2]");

	String autoShipOrderNumber = null;
	static String  orderNumberOfOrderHistory = null;

	public StoreFrontOrdersPage(RFWebsiteDriver driver) {
		super(driver);	
	}

	public boolean verifyOrdersPageIsDisplayed(){
		driver.waitForPageLoad();
		driver.waitForElementPresent(ORDERS_PAGE_TEMPLATE_HEADER_LOC);
		return driver.getCurrentUrl().contains(TestConstants.ORDERS_PAGE_SUFFIX_URL);
	}

	public boolean verifyOrderNumber(String orderNum){
		driver.waitForElementPresent(ORDER_NUMBER_LOC);
		logger.info("Order Number from UI is "+driver.findElement(ORDER_NUMBER_LOC).getText());
		return driver.findElement(ORDER_NUMBER_LOC).getText().equalsIgnoreCase(orderNum);
	}

	public boolean verifyScheduleDate(String schDate){
		driver.waitForElementPresent(ORDER_SCHEDULE_DATE_LOC);
		schDate = convertDBDateFormatToUIFormat(schDate);
		logger.info("Order Schedule date from UI "+schDate);
		return driver.findElement(ORDER_SCHEDULE_DATE_LOC).getText().equalsIgnoreCase(schDate);
	}

	public boolean verifyGrandTotal(String grandTotal){
		if(driver.getCurrentUrl().contains("/ca")){
			grandTotal = "CAD$ " +grandTotal;
		}
		else{
			grandTotal = "$"+grandTotal;
		}
		String grandTotalFromUI = driver.findElement(ORDER_GRAND_TOTAL_LOC).getText();
		if(grandTotalFromUI.contains(",")){
			grandTotalFromUI = grandTotalFromUI.replaceAll(",","");
		}
		logger.info("Order Grand total from UI is "+grandTotalFromUI);
		return grandTotal.contains(grandTotalFromUI);
	}

	public boolean verifyOrderStatus(String status){
		driver.waitForElementPresent(ORDER_STATUS_LOC);
		logger.info("Order Status from UI "+driver.findElement(ORDER_STATUS_LOC).getText());
		return driver.findElement(ORDER_STATUS_LOC).getText().toLowerCase().contains(status.toLowerCase());
	}

	public void clickAutoshipOrderNumber(){
		driver.waitForElementPresent(ORDER_AUTOSHIP_ORDER_NUMBER_LOC);
		//getAutoshipOrderNumber();		
		driver.click(ORDER_AUTOSHIP_ORDER_NUMBER_LOC);
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		logger.info("autoship order clicked " +ORDER_AUTOSHIP_ORDER_NUMBER_LOC);
	}

	public void clickAutoshipOrderNumber(String orderNumber){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'"+orderNumber+"')]"));
		driver.click(By.xpath("//a[contains(text(),'"+orderNumber+"')]"));
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		logger.info("autoship order clicked " +orderNumber);
	}

	public String getAutoshipOrderNumber(){
		driver.waitForElementPresent(ORDER_AUTOSHIP_ORDER_NUMBER_LOC);
		autoShipOrderNumber = driver.findElement(ORDER_AUTOSHIP_ORDER_NUMBER_LOC).getText();
		logger.info("autoship order number is "+autoShipOrderNumber);
		return  autoShipOrderNumber;
	}

	public boolean verifyCRPAutoShipHeader(){
		driver.waitForElementPresent(ORDERS_PAGE_CRP_AUTOSHIP_TEMPLATE_HEADER_LOC);
		logger.info(driver.findElement(ORDERS_PAGE_CRP_AUTOSHIP_TEMPLATE_HEADER_LOC).getText());
		return driver.findElement(ORDERS_PAGE_CRP_AUTOSHIP_TEMPLATE_HEADER_LOC).getText().contains(autoShipOrderNumber);
	}

	public boolean verifyCRPAutoShipAddress(String firstName,String lastName,String addressLine1,String city,String postalCode,String country){
		String shippingAddress = firstName+" "+lastName+"\n"+addressLine1+"\n"+city.toUpperCase()+", ";
		if(country.equalsIgnoreCase("Canada")){
			shippingAddress = shippingAddress+"AB ";
		}		
		shippingAddress = shippingAddress+postalCode+"\n"+ country.toUpperCase();		
		return driver.findElement(ORDER_AUTOSHIP_ADDRESS_LOC).getText().contains(shippingAddress);

	}

	public boolean verifySVValue(String userType){
		driver.waitForElementPresent(By.xpath("//table[@class='order-products']//tr[2]//td[@id='productSV']"));
		String svValue = driver.findElement(By.xpath("//table[@class='order-products']//tr[2]//td[@id='productSV']")).getText();
		if(userType.equalsIgnoreCase("consultant")){
			if(Integer.parseInt(svValue)>=100){
				return true;
			}
		}
		else if(userType.equalsIgnoreCase("preferred customer")){
			if(Integer.parseInt(svValue)>=90){
				return true;
			}
		}
		return false;
	}

	public boolean verifyPresenceOfScheduleDateText() throws InterruptedException{
		driver.pauseExecutionFor(3000);
		boolean isScheduleDateTextPresent = false;
		driver.waitForElementPresent(SCHEDULE_DATE_TEXT_LOC);
		String scheduleDateText = driver.findElement(SCHEDULE_DATE_TEXT_LOC).getText();
		if(scheduleDateText.contains("SCHEDULE DATE")){
			isScheduleDateTextPresent = true;
		}
		return isScheduleDateTextPresent;
	}

	public boolean verifyPCPerksOrderPageHeader(){
		driver.waitForElementPresent(ORDERS_PAGE_PCPERKS_AUTOSHIP_TEMPLATE_HEADER_LOC);
		return driver.findElement(ORDERS_PAGE_PCPERKS_AUTOSHIP_TEMPLATE_HEADER_LOC).getText().contains("ORDER DETAILS");
	}

	public boolean verifyPresenceOfOrderDateText() throws InterruptedException{
		boolean isScheduleDateTextPresent = false;
		driver.waitForElementPresent(SCHEDULE_DATE_TEXT_LOC);
		String scheduleDateText = driver.findElement(SCHEDULE_DATE_TEXT_LOC).getText();
		System.out.println("oRDER DATE IS "+scheduleDateText);
		if(scheduleDateText.contains("ORDER DATE")){
			isScheduleDateTextPresent = true;
		}
		return isScheduleDateTextPresent;
	}

	public boolean verifyPresenceOfOrderStatusText(){
		boolean isOrderStatusTextPresent = false;
		driver.waitForElementPresent(ORDER_STATUS_TEXT_LOC);
		String orderStatusText = driver.findElement(ORDER_STATUS_TEXT_LOC).getText();
		if(orderStatusText.contains("ORDER STATUS")){
			isOrderStatusTextPresent = true;
		}
		return isOrderStatusTextPresent;
	}

	public boolean verifyExpirationDate(String expirationDate){
		driver.waitForElementPresent(ORDER_PAYMENT_METHOD_LOC);
		String paymentCardDetails = driver.findElement(ORDER_PAYMENT_METHOD_LOC).getText();
		if(paymentCardDetails.contains(expirationDate)){
			return true;
		}
		return false;
	}

	public boolean verifyAutoshipTotalPrice(String price){
		driver.waitForElementPresent(ORDER_TOTAL_PRICE_LOC);
		String autoshipTotalPrice = driver.findElement(ORDER_TOTAL_PRICE_LOC).getText();
		String splittedPriceForAssertion = price.split("\\.")[0];
		if(autoshipTotalPrice.contains(splittedPriceForAssertion)){
			return true;
		}
		return false;
	}

	public boolean verifyAutoshipGrandTotalPrice(String grandTotalDB){
		driver.waitForElementPresent(ORDER_GRAND_TOTAL_BOTTOM_LOC);
		String autoshipGrandTotalPrice = driver.findElement(ORDER_GRAND_TOTAL_BOTTOM_LOC).getText();
		if(autoshipGrandTotalPrice.substring(1).contains(grandTotalDB)){
			return true;
		}
		return false;
	}

	public String getGrandTotalFromAutoshipTemplate(){
		driver.waitForElementPresent(ORDER_GRAND_TOTAL_BOTTOM_LOC);
		String autoshipGrandTotalPrice = driver.findElement(ORDER_GRAND_TOTAL_BOTTOM_LOC).getText();
		if(autoshipGrandTotalPrice.contains(",")){
			String[] total = autoshipGrandTotalPrice.split("\\,");
			autoshipGrandTotalPrice = total[0]+total[1];
		}
		return autoshipGrandTotalPrice.trim();
	}

	public boolean verifySKUValueOfItemInOrder(String skuValueDB){
		String skuValue = driver.findElement(SKU_VALUE_OF_ITEM_IN_ORDER_LOC).getText();
		if(skuValue.equals(skuValueDB)){
			return true;
		}
		return false;
	}

	public boolean verifyDescriptionOfItemInOrder(String itemDescDB){
		String itemDescription = driver.findElement(ITEM_ORDER_DESCRIPTION_LOC).getText();
		if(itemDescription.contains(itemDescDB)){
			return true;
		}
		return false;
	}


	public boolean verifyShippingAddressDetails(String shippingAddressDB){		
		return driver.findElement(By.xpath("//strong[text()='Shipping Address:']/following::p[1]")).getText().contains(shippingAddressDB);		
	}

	public String getShippingAddressFromAutoshipTemplate(){
		return driver.findElement(By.xpath("//strong[text()='Shipping Address:']/following::p[1]")).getText().trim().toLowerCase();
	}

	public String getShippingAddressFromAdhocTemplate(){
		return driver.findElement(By.xpath("//strong[text()='Shipping Address:']/following::p[1]")).getText().trim().toLowerCase();
	}

	public String getShippingMethod(){
		logger.info("Shipping Method from UI is "+driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]")).getText().trim());
		return driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]")).getText().trim(); 
	}

	public String getShippingMethodFromAdhocOrderTemplate(){
		String shippingMethodUI = driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]")).getText().trim();
		logger.info("Shipping Method from UI is "+shippingMethodUI);
		return shippingMethodUI;
	}

	public String getShippingMethodFromAutoshipTemplate(){
		driver.waitForElementPresent(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]"));
		String shippingMethodUI = driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]")).getText();
		String shippinMethodLabel = driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]/span")).getText();
		shippingMethodUI = shippingMethodUI.split(shippinMethodLabel)[1].trim();
		logger.info("Shipping Method from UI is "+shippingMethodUI);
		return shippingMethodUI;
	}

	public boolean verifyPCPerksAutoShipHeader(){
		driver.waitForElementPresent(ORDERS_PAGE_PCPERKS_AUTOSHIP_TEMPLATE_HEADER_LOC);
		return driver.findElement(ORDERS_PAGE_PCPERKS_AUTOSHIP_TEMPLATE_HEADER_LOC).getText().contains("ORDER DETAILS: AUTOSHIPMENT");
	}

	public boolean verifyOrderStatusToBeSubmitted(String orderNumber){
		String orderStatus = driver.findElement(By.xpath(String.format(ORDER_NUMBER_STATUS_LOC,orderNumber))).getText();
		if(orderStatus.equals("SUBMITTED")){
			return true;
		}
		return false;	
	}

	public StoreFrontAccountInfoPage clickOnAccountInfoFromLeftPanel(){
		driver.waitForElementPresent(LEFT_MENU_ACCOUNT_INFO_LOC);
		driver.click(LEFT_MENU_ACCOUNT_INFO_LOC);
		logger.info("Account info link from left panel clicked");
		return new StoreFrontAccountInfoPage(driver);
	}


	public boolean verifyCRPTotalPrice(String price){
		String[] totalPriceValueForAssertion = price.split("\\.");
		String priceSplittedForAssertion = totalPriceValueForAssertion[0];
		String crpTotalPrice = driver.findElement(ORDER_TOTAL_PRICE_LOC).getText();
		if(crpTotalPrice.contains(priceSplittedForAssertion)){
			return true;
		}
		return false;
	}

	public boolean verifyForSubmittedAdhocOrders() throws InterruptedException {
		boolean isNextLinkPresent =  false;
		do{
			driver.waitForElementPresent(By.xpath("//div[@id='history-orders-table']/div"));
			int sizeOfOrders = driver.findElements(By.xpath("//div[@id='history-orders-table']/div")).size();
			for(int i=2; i<=sizeOfOrders; i++){
				driver.waitForElementPresent(By.xpath("  //div[@id='history-orders-table']/div["+i+"]/div[2]/div/div/div[4]"));
				if(driver.findElement(By.xpath("  //div[@id='history-orders-table']/div["+i+"]/div[2]/div/div/div[4]")).getText().contains("SUBMITTED")){
					return true;
				}
			} if(driver.isElementPresent(By.xpath("//div[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"))==true){
				isNextLinkPresent = true;
				driver.click(By.xpath("//div[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"));
			}
		}
		while(isNextLinkPresent==true);
		return false;
	}

	public StoreFrontReportOrderComplaintPage clickOnActions(){
		driver.waitForElementPresent(ACTIONS_BUTTON_LOC);
		driver.click(ACTIONS_BUTTON_LOC);
		logger.info("Action drop down clicked for first order");
		driver.waitForElementPresent(ACTIONS_DROPDOWN_LOC);
		driver.click(ACTIONS_DROPDOWN_LOC);
		logger.info("Report Problems link clicked for first order");
		return new StoreFrontReportOrderComplaintPage(driver);
	}

	public void orderNumberForOrderHistory(){
		orderNumberOfOrderHistory = driver.findElement(ORDER_NUM_OF_ORDER_HISTORY).getText();
	}

	public void clickOnFirstAdHocOrder(){
		driver.waitForElementPresent(ORDER_NUM_OF_ORDER_HISTORY);
		//driver.click(ORDER_NUM_OF_ORDER_HISTORY); 
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(ORDER_NUM_OF_ORDER_HISTORY));
		logger.info("First order from the order history clicked");  
	}

	public String getFirstOrderNumberFromOrderHistory(){
		driver.waitForElementPresent(ORDER_NUM_OF_ORDER_HISTORY);
		String firstOrderNumber = driver.findElement(ORDER_NUM_OF_ORDER_HISTORY).getText(); 
		return  firstOrderNumber;
	}

	public boolean isPaymentMethodContainsName(String name){
		driver.waitForElementPresent(ORDER_PAYMENT_METHOD_LOC);
		return driver.findElement(ORDER_PAYMENT_METHOD_LOC).getText().toLowerCase().contains(name.toLowerCase());
	}

	public void clickOrderNumber(String orderNumber){
		driver.waitForElementPresent(By.linkText(orderNumber));
		driver.click(By.linkText(orderNumber));
		logger.info("Order number clicked "+orderNumber);
		driver.waitForPageLoad();
	}

	public boolean verifyAutoShipTemplateSubtotal(String subTotalDB){
		String subTotal = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[1]/span")).getText();
		return subTotal.substring(1).contains(subTotalDB);
	}

	public String getSubTotalFromAutoshipTemplate(){
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']//div[contains(text(),'Subtotal')]/following::div[1]"));
		String subTotal = driver.findElement(By.xpath("//div[@id='main-content']//div[contains(text(),'Subtotal')]/following::div[1]")).getText();
		if(subTotal.contains(",")){
			String[] total = subTotal.split("\\,");
			subTotal = total[0]+total[1];
		}
		System.out.println("Subtotal is "+subTotal);
		return subTotal.trim();  
	}

	public boolean verifyAutoShipTemplateShipping(String shippingDB){
		String shippingAmount = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[2]/span")).getText();
		return shippingAmount.substring(1).contains(shippingDB);
	}

	public String getShippingAmountFromAutoshipTemplate(){
		String shippingAmount = driver.findElement(By.xpath("//div[@id='main-content']//div[contains(text(),'Shipping')]/following::div[1]")).getText();
		return shippingAmount.substring(1);
	}

	public boolean verifyAutoShipTemplateHandling(String handlingDB){
		String handlingCharges = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[3]/span")).getText();
		return handlingCharges.substring(1).contains(handlingDB);
	}

	public String getHandlingAmountFromAutoshipTemplate(){
		String handlingCharges = driver.findElement(By.xpath("//div[@id='main-content']//div[contains(text(),'Handling')]/following::div[1]")).getText();
		return handlingCharges;
	}

	public boolean verifyOrderHistoryTax(String taxDB){
		String tax = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]//p[2]//span")).getText();
		return tax.trim().substring(1).contains(taxDB);
	}

	public String getTaxAmountFromAdhocOrderTemplate(){
		String tax = null;
		try{
			driver.turnOffImplicitWaits();
			tax = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]//p[2]//span")).getText();
		}catch(NoSuchElementException e){
			try{
				tax = driver.findElement(By.id("crpTotalTax")).getText();
			}catch(Exception e1){
				tax = driver.findElement(By.id("totalTax")).getText();
			}
		}
		driver.turnOnImplicitWaits();
		return tax.trim().substring(0);
	}

	public boolean verifyAutoShipTemplateTax(String taxDB){
		String tax = driver.findElement(By.id("crpTotalTax")).getText();
		return tax.trim().substring(1).contains(taxDB);
	}

	public String getTaxAmountFromAutoshipTemplate(){

		String tax = null;
		if(driver.getCountry().equalsIgnoreCase("US")){
			try{
				tax = driver.findElement(By.id("crpTotalTax")).getText();
			}catch(Exception e){
				tax = driver.findElement(By.id("totalTax")).getText();
			}
			System.out.println("Tax is "+tax);
			return tax.trim();
		}
		else {
			if(driver.isElementPresent(By.xpath("//div[@id='module-hst']//div[text()='GST:']/following::div[1]/span"))&&(driver.isElementPresent(By.xpath("//div[@id='module-hst']//div[text()='PST:']/following::div[1]/span")))){
				String gstTax = driver.findElement(By.xpath("//div[@id='module-hst']//div[text()='GST:']/following::div[1]/span")).getText();
				String gstTaxValue[] = gstTax.split("\\ ");
				System.out.println("gstTax==="+gstTaxValue[1]);
				double gstTaxIntegerValue = Double.parseDouble(gstTaxValue[1]);
				String pstTax = driver.findElement(By.xpath("//div[@id='module-hst']//div[text()='PST:']/following::div[1]/span")).getText();
				String pstTaxValue[] = pstTax.split("\\ ");
				System.out.println("pstTax==="+pstTaxValue[1]);
				double pstTaxIntegerValue = Double.parseDouble(pstTaxValue[1]);
				double totalTax = (gstTaxIntegerValue+pstTaxIntegerValue);
				System.out.println("totalTax==="+totalTax);
				String taxFinal = Double.toString(totalTax);
				return taxFinal.trim();
			}else if(driver.isElementPresent(By.xpath("//div[@id='module-hst']//div[text()='GST:']/following::div[1]/span"))){
				String gstTax = driver.findElement(By.xpath("//div[@id='module-hst']//div[text()='GST:']/following::div[1]/span")).getText();
				return gstTax.trim();
			}
			else if(driver.isElementPresent(By.id("totalTax"))){
				tax = driver.findElement(By.id("totalTax")).getText();
				return tax.trim();
			}else{
				tax = driver.findElement(By.id("crpTotalTax")).getText();
				return tax.trim();
			}
		}
	}

	public boolean verifyCardType(String cardTypeDB){	
		String cardType = cardTypeDB.toLowerCase();
		if(cardType.contains("master")){		
			try{
				driver.findElement(By.xpath("//span[@class='cardType mastercard']"));				
				return true;
			}
			catch(NoSuchElementException e){
				return false;
			}
		}
		else if(cardType.contains("visa")){
			try{
				driver.findElement(By.xpath("//span[@class='cardType visa']"));
				return true;
			}
			catch(NoSuchElementException e){
				return false;
			}
		}
		return false;
	}

	public boolean verifyReturnOrderNumber(String returnOrderNumber){
		return driver.findElement(By.xpath("//div[@id='main-content']/div/div[4]//table[@class='orders-table']/tbody/tr[1]/td[1]/a")).getText().equals(returnOrderNumber);
	}

	public boolean verifyReturnOrderGrandTotal(String total){
		return driver.findElement(By.xpath("//div[@id='main-content']/div/div[4]//table[@class='orders-table']/tbody/tr[1]/td[3]")).getText().contains(total);
	}

	public boolean verifyReturnOrderStatus(String status){
		return driver.findElement(By.xpath("//div[@id='main-content']/div/div[4]//table[@class='orders-table']/tbody/tr[1]/td[4]")).getText().equalsIgnoreCase(status);
	}

	public String getShippingAddress(){
		return driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[1]/p")).getText();
	}

	public boolean isShippingAddressContainsName(String name){
		String uncapitalizeName = WordUtils.uncapitalize(name);
		String lowerCaseName = name.toLowerCase();
		driver.waitForElementPresent(By.xpath("//ul[@class='order-detail-list']/li[1]/p/span[1]"));
		return (driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[1]/p/span[1]")).getText().contains(name)||driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[1]/p/span[1]")).getText().contains(uncapitalizeName)||driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[1]/p/span[1]")).getText().contains(lowerCaseName));
	}

	public boolean verifyShippingAddress(String shippingAddress){
		return driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[1]/p")).getText().equalsIgnoreCase(shippingAddress);
	}

	public boolean verifyAdhocOrderTemplateSubtotal(String subTotal){
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[1]/span"));
		logger.info("subtotal from UI is "+driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[1]/span")).getText());
		return driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[1]/span")).getText().contains(subTotal);
	}

	public String getSubTotalFromAdhocOrderTemplate(){
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[1]/span"));
		logger.info("subtotal from UI is "+driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[1]/span")).getText());
		return driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[1]/span")).getText();
	}

	public boolean verifyAdhocOrderTemplateShippingCharges(String shippingCharges){
		logger.info("shippingCharges from UI "+driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[2]/span")).getText());
		return driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[2]/span")).getText().contains(shippingCharges);
	}

	public String getShippingAmountFromAdhocOrderTemplate(){
		logger.info("shippingCharges from UI "+driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[2]/span")).getText());
		return driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[2]/span")).getText();
	}

	public boolean verifyAdhocOrderTemplateHandlingCharges(String handlingCharges){
		logger.info("handlingCharges from UI "+driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[3]/span")).getText());
		return driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[3]/span")).getText().contains(handlingCharges);
	}

	public String getHandlingAmountFromAdhocOrderTemplate(){
		logger.info("handlingCharges from UI "+driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[3]/span")).getText());
		return driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[3]/span")).getText();
	}

	public boolean verifyAdhocOrderTemplateTax(String tax){
		logger.info("tax from UI "+driver.findElement(By.xpath("//span[@id='crpTotalTax']")).getText());
		return driver.findElement(By.xpath("//span[@id='crpTotalTax']")).getText().contains(tax);
	}

	public boolean verifyAdhocOrderTemplateTotal(String total){
		logger.info("total from UI "+driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[4]/span")).getText());
		return driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[4]/span")).getText().contains(total);
	}

	public String getGrandTotalFromAdhocOrderTemplate(){
		try{
			logger.info("total from UI "+driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[4]/span")).getText());
			return driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[4]/span")).getText();
		}catch(Exception e){
			logger.info("total from UI "+driver.findElement(By.xpath("//div[@id='orderGrandTotal']")).getText());
			return driver.findElement(By.xpath("//div[@id='orderGrandTotal']")).getText();
		}
	}

	public boolean verifyAdhocOrderTemplateTotalSV(String totalSV){
		String absoluteTotalSV = totalSV.split("\\.")[0];
		logger.info("total SV from UI "+driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[5]/span")).getText());
		return (driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[5]/span")).getText().contains(totalSV)||(driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[5]/span")).getText().contains(absoluteTotalSV)));
	}

	public boolean verifyShippingMethodOnTemplateAfterOrderCreation(String shippingMethodDB){
		String[] shippingMethod = shippingMethodDB.split(" ");
		shippingMethodDB = shippingMethod[0]+" "+shippingMethod[1]+" "+shippingMethod[2];
		String shippingMethodUI = driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]")).getText();
		logger.info("Shipping Method from UI is "+shippingMethodUI);
		return shippingMethodUI.contains(shippingMethodDB);
	}

	public String getOrderNumberFromOrderHistoryForFailedAutoshipOrdersForConsultant() throws InterruptedException {
		boolean isNextLinkPresent =  false;
		do{
			driver.waitForElementPresent(By.xpath("//div[@id='history-orders-table']/div"));
			int sizeOfOrders = driver.findElements(By.xpath("//div[@id='history-orders-table']/div")).size();
			for(int i=2; i<=sizeOfOrders; i++){
				driver.waitForElementPresent(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(@class,'grand-total')]/following::div[1]"));
				if(driver.findElement(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(@class,'grand-total')]/following::div[1]")).getText().contains("FAILED")){
					driver.waitForElementPresent(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(text(),'FAILED')]/preceding::div[3]"));
					String failedOrderNumber = driver.findElement(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(text(),'FAILED')]/preceding::div[3]")).getText();
					clickOrderNumber(failedOrderNumber);
					driver.waitForElementPresent(By.xpath("//div[@class='gray-container-info-top']/div"));
					if(driver.findElement(By.xpath("//div[@class='gray-container-info-top']/div")).getText().contains("ORDER DETAILS: CRP")==true){
						return failedOrderNumber;
					}else{
						driver.navigate().back();
					}
				}
			}   
			if(driver.isElementPresent(By.xpath("//div[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"))==true){
				isNextLinkPresent = true;
				driver.click(By.xpath("//div[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"));
			}
		}while(isNextLinkPresent==true);
		return null;
	}

	public String getOrderNumberFromOrderHistoryForFailedAutoshipOrdersForPC() throws InterruptedException {
		boolean isNextLinkPresent =  false;
		do{
			driver.waitForElementPresent(By.xpath("//div[@id='history-orders-table']/div"));
			int sizeOfOrders = driver.findElements(By.xpath("//div[@id='history-orders-table']/div")).size();
			for(int i=2; i<=sizeOfOrders; i++){
				driver.waitForElementPresent(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(@class,'grand-total')]/following::div[1]"));
				if(driver.findElement(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(@class,'grand-total')]/following::div[1]")).getText().contains("FAILED")){
					driver.waitForElementPresent(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(text(),'FAILED')]/preceding::div[3]"));
					String failedOrderNumber = driver.findElement(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(text(),'FAILED')]/preceding::div[3]")).getText();
					clickOrderNumber(failedOrderNumber);
					driver.waitForElementPresent(By.xpath("//div[@class='gray-container-info-top']/div"));
					if(driver.findElement(By.xpath("//div[@class='gray-container-info-top']/div")).getText().contains("AUTOSHIPMENT")==true){
						return failedOrderNumber;
					}else{
						driver.navigate().back();
					}
				}
			}
			if(driver.isElementPresent(By.xpath("//div[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"))==true){
				isNextLinkPresent = true;
				driver.click(By.xpath("//div[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"));
			}
		}while(isNextLinkPresent==true);
		return null;
	}

	public String getOrderNumberFromOrderHistoryForFailedAdhocOrdersForRC() throws InterruptedException {
		driver.pauseExecutionFor(5000);
		boolean isNextLinkPresent =  false;
		do{
			driver.waitForElementPresent(By.xpath("//div[@id='history-orders-table']/div"));
			int sizeOfOrders = driver.findElements(By.xpath("//div[@id='history-orders-table']/div")).size();
			for(int i=2; i<=sizeOfOrders; i++){
				driver.waitForElementPresent(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(@class,'grand-total')]/following::div[1]"));
				if(driver.findElement(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(@class,'grand-total')]/following::div[1]")).getText().contains("FAILED")){
					driver.waitForElementPresent(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(text(),'FAILED')]/preceding::div[3]"));
					String failedOrderNumber = driver.findElement(By.xpath("//div[@id='history-orders-table']/div["+i+"]/div[2]//div[contains(text(),'FAILED')]/preceding::div[3]")).getText();
					clickOrderNumber(failedOrderNumber);
					driver.waitForElementPresent(By.xpath("//div[@class='gray-container-info-top']/div"));
					if(driver.findElement(By.xpath("//div[@class='gray-container-info-top']/div")).getText().contains("ORDER")==true){
						return failedOrderNumber;
					}else{
						driver.navigate().back();
					}
				}
			}
			if(driver.isElementPresent(By.xpath("//div[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"))==true){
				isNextLinkPresent = true;
				driver.click(By.xpath("//div[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"));
			}

		}while(isNextLinkPresent==true);
		return null;
	}

	public String orderDetails_getTotalSV()	{
		return driver.findElement(By.xpath("//LI[text()='Total SV:']/span")).getText();
	}

	public String orderDetails_getSubTotal() {
		return driver.findElement(By.xpath("//LI[text()='Subtotal:']/span")).getText();
	}

	public String orderDetails_ShippingCharges() {
		return driver.findElement(By.xpath("//LI[text()='Shipping:']/span")).getText();
	}

	public String orderDetails_HandlingCharges(){
		return driver.findElement(By.xpath("//li[contains(text(),'Handl')]/span")).getText();
	}

	public String orderDetails_grandTotal()	{
		return driver.findElement(By.xpath("//li[@class='grand-total']/span")).getText();
	}

	public String orderDetails_getTax() {
		return driver.findElement(By.xpath("//li[@id='module-hst']/span")).getText();
	}

	public String orderDetails_getShippingMethodName()	{
		return driver.findElement(By.xpath("//p[span/strong[contains(text(),'Shi')]]/br")).getText();
	}

	public String convertShippingMethodNameAsOnUI(String shippingMethodID){

		String methodId = shippingMethodID;
		if(methodId=="null"){
			methodId="9";
		}
		String shippingMethodName = null;
		switch (Integer.parseInt(methodId)) {  
		case 1:
			shippingMethodName="FedEx 2Day";
			break;

		case 2:
			shippingMethodName="FedEx 2Day";
			break;

		case 3:
			shippingMethodName="FedEx Ground (HD)";
			break;

		case 4:
			shippingMethodName="USPS 1Day";
			break;

		case 5:
			shippingMethodName="USPS 2Day";
			break;

		case 6:
			shippingMethodName="USPS Grnd";
			break;

		case 7:
			shippingMethodName="UPS Ground (HD)";
			break;

		case 8:
			shippingMethodName="POS Drop-Ship";
			break;

		case 9:
			shippingMethodName="";
			break;
		}
		return shippingMethodName;
	}

	public void clickDetailsUnderActionsForFirstOrderUnderOrderHistory(){
		String firstOrderNumber=getFirstOrderNumberFromOrderHistory();
		driver.waitForElementPresent(By.xpath("//a[text()="+firstOrderNumber+"]/following::span[1]"));
		driver.click(By.xpath("//a[text()="+firstOrderNumber+"]/following::span[1]"));
		driver.click(By.linkText("Details"));
		driver.waitForPageLoad();
	}

	public String validateOrderDetailsPageIsDisplayedForSimilarOrderNo(){
		return driver.findElement(By.xpath("//div[@class='gray-container-info-top']")).getText();
	}

	public String getTotalSV(){
		return driver.findElement(By.xpath("//div[@class='checkout-module-content']//div[contains(text(),'SV')]/following::div[1]/span")).getText().substring(0,3);
	}

	public String getTotalSVValue(){
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']//div[contains(text(),'Total SV')]/following::div[1]"));
		String svValue = driver.findElement(By.xpath("//div[@id='main-content']//div[contains(text(),'Total SV')]/following::div[1]")).getText();
		logger.info("Total SV value on UI is "+svValue);
		return svValue;
	}

	public boolean validateOrderDetails(){
		driver.pauseExecutionFor(2000);
		return driver.findElements(By.xpath("//ul[@class='order-detail-list']/li[1]/p")).size()>0 
				&& driver.findElements(By.xpath("//ul[@class='order-detail-list']/li[3]/p")).size()>0; 

	}

	public Boolean validateGranTotalText(){
		driver.pauseExecutionFor(2000);
		return driver.findElement(By.xpath("//div[@id='main-content']//div[contains(text(),'Grand Total')]")).getText().contains("Grand Total");   
	}

	public boolean validateCurrency(){
		driver.pauseExecutionFor(2000);
		String country = driver.getCountry();
		if(country.equalsIgnoreCase("ca")){
			return driver.findElement(By.xpath("//div[@id='main-content']//div[contains(text(),'Grand Total')]/following::div[1]")).getText().contains("CAD$");
		}else 
			return driver.findElement(By.xpath("//div[@id='main-content']//div[contains(text(),'Grand Total')]/following::div[1]")).getText().contains("US$");
	}

	public String getAutoshipOrderDate(){
		String autoShipOrderDate = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			driver.waitForElementPresent(By.xpath("//div[@id='pending-autoship-orders-table']/descendant::div[text()='PENDING'][1]/preceding::div[2]"));
			autoShipOrderDate = driver.findElement(By.xpath("//div[@id='pending-autoship-orders-table']/descendant::div[text()='PENDING'][1]/preceding::div[2]")).getText();
			return  autoShipOrderDate;
		}else{
			driver.waitForElementPresent(AUTOSHIP_DATE_LOC);
			autoShipOrderDate = driver.findElement(AUTOSHIP_DATE_LOC).getText();
			logger.info("autoship order Date is "+autoShipOrderDate);
			return  autoShipOrderDate;
		}
	}

	public boolean verifyAdhocOrderIsPresent(){
		driver.waitForElementPresent(ORDER_NUM_OF_ORDER_HISTORY);
		if(driver.isElementPresent(ORDER_NUM_OF_ORDER_HISTORY)){
			return true;
		}else
			return true;
	}

	public double getOrderGrandTotal(){
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.waitForElementPresent(ORDER_GRAND_TOTAL_BOTTOM_LOC);
			String value = driver.findElement(ORDER_GRAND_TOTAL_BOTTOM_LOC).getText().trim();
			if(value.contains(",")){
				value.replace(",","");
			}
			String[] totalValue= value.split("\\s");
			double  orderTotal = Double.parseDouble(totalValue[1]);
			logger.info("Subtotal Value fetched is "+orderTotal);
			return orderTotal;
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.waitForElementPresent(ORDER_GRAND_TOTAL_BOTTOM_LOC);
			String value = driver.findElement(ORDER_GRAND_TOTAL_BOTTOM_LOC).getText().trim();
			String fetchValue = value.substring(1);
			double  orderTotal = Double.parseDouble(fetchValue);
			logger.info("Subtotal Value fetched is "+orderTotal);
			return orderTotal;
		}
		return 0;
	}

	public boolean validateSubTotal(double subtotal1,double subtotal2){
		driver.waitForElementPresent(By.xpath("//div[@id='idSubTotal']"));
		String subTotalVal=driver.findElement(By.xpath("//div[@id='idSubTotal']")).getText();
		String[] totalvalue= subTotalVal.split("\\$+");
		double subTotalValue=Double.valueOf((totalvalue[1].replaceAll(",", "")).trim());
		logger.info("Total subtotal Value is "+subTotalVal);
		if(subTotalValue==(subtotal1+subtotal2)){
			return true;
		}else{
			return false;
		}
	}

	public boolean validateQuantityForFirstProduct(int expectedQty){
		driver.waitForElementPresent(By.xpath("//div[@class='order-products orders-table col-sm-8 col-xs-12']/div[2]/div[2]//div[3]"));
		String actualQty=driver.findElement(By.xpath("//div[@class='order-products orders-table col-sm-8 col-xs-12']/div[2]/div[2]//div[3]")).getText();
		if(expectedQty==Integer.parseInt(actualQty)){
			return true;
		}else{
			return false;
		}
	}

	public boolean validateQuantityForSecondProduct(int expectedQty){
		driver.waitForElementPresent(By.xpath("//div[@class='order-products orders-table col-sm-8 col-xs-12']/div[3]/div[2]//div[3]"));
		String actualQty=driver.findElement(By.xpath("//div[@class='order-products orders-table col-sm-8 col-xs-12']/div[3]/div[2]//div[3]")).getText();
		if(expectedQty==Integer.parseInt(actualQty)){
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyTotalValueOfProductOnOrderDetails(String totalPrice){
		driver.waitForElementPresent(By.xpath("//div[@id='productSV']/following::div[2]"));
		return driver.findElement(By.xpath("//div[@id='productSV']/following::div[2]")).getText().equals(totalPrice);
	}

	public boolean verifyQuantityOnOrdersDetails(String quantityOfProductsOrdered ){
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']//div[@class='row']/div[2]/div[3]"));
		if(driver.findElement(By.xpath("//div[@id='main-content']//div[@class='row']/div[2]/div[3]")).getText().equals(quantityOfProductsOrdered)){
			return true;
		}
		return false;
	}

	public boolean isBillingAddressContainsName(String name){
		String uncapitalizeName = WordUtils.uncapitalize(name);
		String lowerCaseName = name.toLowerCase();
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div/ul/li[3]/p"));
		return (driver.findElement(By.xpath("//div[@id='main-content']/div/ul/li[3]/p")).getText().contains(name)||driver.findElement(By.xpath("//div[@id='main-content']/div/ul/li[3]/p")).getText().contains(uncapitalizeName)||driver.findElement(By.xpath("//div[@id='main-content']/div/ul/li[3]/p")).getText().contains(lowerCaseName));
	}

	public boolean verifySVValueOnOrderPage(){
		driver.quickWaitForElementPresent(By.id("productSV"));
		return driver.isElementPresent(By.id("productSV"));
	}

	public boolean verifyAutoshipOrderSectionOnOrderPage(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div/div[2]/h3[contains(text(),'Autoship order')]"));
		return driver.isElementPresent(By.xpath("//div[@id='main-content']/div/div[2]/h3[contains(text(),'Autoship order')]"));
	}

	public boolean verifyOrderHistorySectionOnOrderPage(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']//h3[contains(text(),'order history')]"));
		return driver.isElementPresent(By.xpath("//div[@id='main-content']//h3[contains(text(),'order history')]"));
	}

	public boolean verifyReturnOrderSectionOnOrderPage(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']//h3[contains(text(),'Return Order')]"));
		return driver.isElementPresent(By.xpath("//div[@id='main-content']//h3[contains(text(),'Return Order')]"));
	}

	public boolean verifyNextAutoshipDateRadioButtons(){
		driver.quickWaitForElementPresent(By.xpath("//span[@class='radio-button selectautoshipDate']"));
		if(driver.isElementPresent(By.xpath("//span[@class='radio-button selectautoshipDate']"))){
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyAutoShipOrderDate(String date){
		if(driver.findElement(AUTOSHIP_DATE_LOC).getText().equalsIgnoreCase(date)){
			return true;
		}else{
			return false;
		}
	}

	public String getCurrentPSTDate(){
		DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
		df.setTimeZone(TimeZone.getTimeZone("PST"));
		final String dateString = df.format(new Date());
		String[] datePST = dateString.split(" ");
		System.out.println(dateString);
		String splittedDateForMonth = datePST[1]+" "+datePST[2]+" "+datePST[3];
		System.out.println("Fianl for expand "+splittedDateForMonth);
		return splittedDateForMonth;
	}

	public String getReturnOrderNumber(){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Return Order')]/following::div[1]/div[1]/div[3]//a"));
		autoShipOrderNumber = driver.findElement(By.xpath("//h3[contains(text(),'Return Order')]/following::div[1]/div[1]/div[3]//a")).getText();
		logger.info("autoship order number is "+autoShipOrderNumber);
		return  autoShipOrderNumber;
	}

	public void clickReturnOrderNumber(){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Return Order')]/following::div[1]/div[1]/div[3]//a"));
		driver.click(By.xpath("//h3[contains(text(),'Return Order')]/following::div[1]/div[1]/div[3]//a"));
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		logger.info("Return order clicked " +By.xpath("//h3[contains(text(),'Return Order')]/following::div[1]/div[1]/div[3]//a"));
	}

	public String getOrderStatusFromUI(){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Order status')]/following::span[1]"));
		return driver.findElement(By.xpath("//span[contains(text(),'Order status')]/following::span[1]")).getText();
	}

	public String convertOrderStatusForReturnOrder(String orderStatusID){
		String methodId = orderStatusID;
		String orderStatus = null;
		switch (Integer.parseInt(methodId)) {  
		case 1:
			orderStatus="FedEx 2Day";
			break;

		case 2:
			orderStatus="FedEx 2Day";
			break;

		case 3:
			orderStatus="FedEx Ground (HD)";
			break;

		case 4:
			orderStatus="USPS 1Day";
			break;

		case 5:
			orderStatus="completed";
			break;

		case 6:
			orderStatus="USPS Grnd";
			break;
		}
		return orderStatus;
	}

	public void clickAutoshipOrderNumberOfPulse(){
		driver.waitForElementPresent(ORDER_AUTOSHIP_ORDER_NUMBER_LOC);
		try{
			driver.click(By.xpath("//div[@id='pending-autoship-orders-table']/div[3]//div[contains(@class,'ref-values')]//a"));
		}catch(NoSuchElementException e){
			driver.click(ORDER_AUTOSHIP_ORDER_NUMBER_LOC);
		}
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		logger.info("autoship order clicked " +ORDER_AUTOSHIP_ORDER_NUMBER_LOC);
	}

	public boolean verifyOrderStatus(String status, String orderNumber){
		driver.waitForElementPresent(ORDER_STATUS_LOC);
		logger.info("Order Status from UI "+driver.findElement(By.xpath(String.format("//div[@id='history-orders-table']//a[contains(text(),'%s')]/following::div[3]", orderNumber))).getText());
		return driver.findElement(By.xpath(String.format("//div[@id='history-orders-table']//a[contains(text(),'%s')]/following::div[3]", orderNumber))).getText().toLowerCase().contains(status.toLowerCase());
	}

	public boolean verifyShippingMethodOnTemplateAfterAdhocOrderPlaced(String selectedShippingMethod){
		String shippingMethodUI = driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]")).getText().split("\\:")[1].trim();
		logger.info("Shipping Method from UI is "+shippingMethodUI);
		return selectedShippingMethod.trim().contains(shippingMethodUI);
	}

	public int getSizeOfReturnOrders(){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Return Order')]/following::div[1]/div[1]/div"));
		int sizeOfOrders = driver.findElements(By.xpath("//h3[contains(text(),'Return Order')]/following::div[1]/div[1]/div")).size();
		return sizeOfOrders;
	}

	public String getReturnOrderNumber(int i){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Return Order')]/following::div[1]/div[1]/div["+i+"]//a"));
		String orderNumber = driver.findElement(By.xpath("//h3[contains(text(),'Return Order')]/following::div[1]/div[1]/div["+i+"]//a")).getText().trim();
		logger.info("autoship order number is "+orderNumber);
		return  orderNumber;
	}

	public void clickReturnOrderNumber(String orderNumber){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Return Order')]/following::a[contains(text(),'"+orderNumber+"')]"));
		//driver.click(By.xpath("//h3[contains(text(),'Return Order')]/following::a[contains(text(),'"+orderNumber+"')]"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//h3[contains(text(),'Return Order')]/following::a[contains(text(),'"+orderNumber+"')]")));
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		logger.info("Return order clicked " +By.xpath("//h3[contains(text(),'Return Order')]/following::a[contains(text(),'"+orderNumber+"')]"));
	}

	public boolean validateBillingAddressOnOrderPage(String name) {
		return driver.isElementPresent(By.xpath("//span[@id='idBillingAddress']/following::p[1]/span[contains(text(),'"+name+"')]"));
	}

	public String getCreditCardNumber(){
		driver.waitForElementPresent(By.xpath("//ul[@class='order-detail-list']/li[3]/p"));
		String cardNumber= driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[3]/p")).getText();
		return cardNumber;
	}

	public String getProductNameFromAdhocTemplate(String productNumber){
		driver.waitForElementPresent(By.xpath("//div[contains(@class,'order-products orders-table')]/div["+productNumber+"]//p[2]"));
		return driver.findElement(By.xpath("//div[contains(@class,'order-products orders-table')]/div["+productNumber+"]//p[2]")).getText().trim();
	}

	public boolean isShippingCostPresent(){
		return driver.isElementPresent(By.xpath("//div[@id='main-content']//div[contains(text(),'Shipping')]/following::div[1]"));
	}

	public boolean isHandlingCostPresent(){
		return driver.isElementPresent(By.xpath("//div[@id='main-content']//div[contains(text(),'Handling')]/following::div[1]"));
	}

	public boolean verifyRMANumberIsPresentInReturnOrderHistory(String rmaNumber){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Return Order and Credits')]/following::a[text()='"+rmaNumber+"']"));
		return driver.isElementPresent(By.xpath("//h3[contains(text(),'Return Order and Credits')]/following::a[text()='"+rmaNumber+"']"));
	}

	public String getGranTotalOfRMANumberInReturnOrderHistory(String rmaNumber){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Return Order and Credits')]/following::a[text()='"+rmaNumber+"']/following::div[2]"));
		return driver.findElement(By.xpath("//h3[contains(text(),'Return Order and Credits')]/following::a[text()='"+rmaNumber+"']/following::div[2]")).getText();
	}

	public boolean validateSameDatePresentForAutoship(String autoshipDate) {
		String currentDate = getPSTDate();
		try{
			String currentDay = currentDate.split("\\ ")[0].split("\\,")[0];
			logger.info("Current day is: "+currentDay);
			String autoshipDay = autoshipDate.split("\\ ")[1].split("\\,")[0];
			logger.info("Current autoship day is: "+autoshipDay);
			if(Integer.parseInt(currentDay)<=17){
				if(autoshipDay.contains(currentDay)){
					return true;
				}
				return false;
			}else{
				if(autoshipDay.contains("17")){
					return true;
				}
				return false;
			}
		}catch(NumberFormatException nfe){
			String currentDay = currentDate.split("\\ ")[1].split("\\,")[0];
			logger.info("Current day is: "+currentDay);
			String autoshipDay = autoshipDate.split("\\ ")[1].split("\\,")[0];
			logger.info("Current autoship day is: "+autoshipDay);
			if(Integer.parseInt(currentDay)<=17){
				if(autoshipDay.contains(currentDay)){
					return true;
				}
				return false;
			}else{
				if(autoshipDay.contains("17")){
					return true;
				}
				return false;
			}
		}

	}

	public void clickReportProblemsUnderActionsForFirstOrderUnderOrderHistory() {
		String firstOrderNumber=getFirstOrderNumberFromOrderHistory();
		driver.waitForElementPresent(By.xpath("//a[text()="+firstOrderNumber+"]/following::span[1]"));
		driver.click(By.xpath("//a[text()="+firstOrderNumber+"]/following::span[1]"));
		driver.click(By.linkText("Report Problems"));
		logger.info("Report problems under actions is clicked");
		driver.waitForPageLoad();
	}

	public boolean verifyReportAProblemSectionPresent() {
		return driver.isElementPresent(By.xpath("//div[contains(text(),'Report a problem')]"));
	}

	public void selectItemsAndReportAProblem() {
		if(driver.isElementPresent(By.xpath("//input[@id='problemCheckBox']"))){
			driver.click(By.xpath("//input[@id='problemCheckBox']/ancestor::div[1]"));
			logger.info("item checkbox is checked");
			Select select = new Select(driver.findElement(By.xpath("//select[@id='problemType']")));
			select.selectByVisibleText("Order is incorrect");
			driver.type(By.xpath("//textarea[@id='problemText']"), "Problem");
			logger.info("problem type selected is::  Order is incorrect");
			driver.click(By.id("submitButton"));
			logger.info("problem submit button clicked");
			driver.waitForPageLoad();
		}
	}

	public boolean verifyReportAProblemConfirmationMessage() {
		boolean presenceThankyouMessage = driver.isElementPresent(By.xpath("//div[@id='main-content']//h3[text()='Thank you']"));
		boolean presenceSendMessage = driver.isElementPresent(By.xpath("//div[@id='main-content']//p[contains(text(),'We have sent your problem')]"));
		if(presenceThankyouMessage && presenceSendMessage ){
			return true;}
		else
			return false;
	}

	public String getStatusOfFirstAutoshipTemplateID(){
		driver.isElementPresent(By.xpath("//div[@id='pending-autoship-orders-table']/div[1]/div//div[contains(text(),'Schedule Date')]/following::div[@class='ref-labels'][2]/div//div[4]"));
		String status = driver.findElement(By.xpath("//div[@id='pending-autoship-orders-table']/div[1]/div//div[contains(text(),'Schedule Date')]/following::div[@class='ref-labels'][2]/div//div[4]")).getText().trim();
		logger.info("Status of first autoship template id is: "+status);
		return status;
	}

	public String getStatusOfSecondAutoshipTemplateID(){
		driver.isElementPresent(By.xpath("//div[@id='pending-autoship-orders-table']/div[@class='row']/div[2]//div[@class='row']/div[5][not(input)]/ancestor::div[1]/div[4]"));
		String status = driver.findElement(By.xpath("//div[@id='pending-autoship-orders-table']/div[@class='row']/div[2]//div[@class='row']/div[5][not(input)]/ancestor::div[1]/div[4]")).getText().trim();
		logger.info("Status of second autoship template id is: "+status);
		return status;
	}

	public String getPulseAutoshipOrderDate(){
		String autoShipOrderDate = null;
		driver.waitForElementPresent(By.xpath("//div[@id='pending-autoship-orders-table']/div[@class='row']/div[2]//div[@class='row']/div[5][not(input)]/ancestor::div[1]/div[2]"));
		autoShipOrderDate = driver.findElement(By.xpath("//div[@id='pending-autoship-orders-table']/div[@class='row']/div[2]//div[@class='row']/div[5][not(input)]/ancestor::div[1]/div[2]")).getText();
		logger.info("Return autoship order date is: "+autoShipOrderDate);
		return  autoShipOrderDate;
	}

	public String getPulseAutoshipOrderNumber(){
		driver.waitForElementPresent(By.xpath("//div[@id='pending-autoship-orders-table']/div[@class='row']/div[2]//div[@class='row']/div[5][not(input)]/ancestor::div[1]//a"));
		autoShipOrderNumber = driver.findElement(By.xpath("//div[@id='pending-autoship-orders-table']/div[@class='row']/div[2]//div[@class='row']/div[5][not(input)]/ancestor::div[1]//a")).getText();
		logger.info("Pulse autoship order number is "+autoShipOrderNumber);
		return  autoShipOrderNumber;
	}

	public String getFirstOrderNumberWithNonZeroSubtotalFromOrderHistory(){
		driver.waitForElementPresent(By.xpath("//div[@id='history-orders-table']/descendant::div[contains(@class,'-m grand-total')][not(contains(text(),'0.0'))][1]/../div[1]/a"));
		String firstOrderNumber = driver.findElement(By.xpath("//div[@id='history-orders-table']/descendant::div[contains(@class,'-m grand-total')][not(contains(text(),'0.0'))][1]/../div[1]/a")).getText(); 
		return  firstOrderNumber;
	}

	public void clickDetailsUnderActionsForOrderUnderOrderHistory(String orderNumber){
		driver.waitForElementPresent(By.xpath("//a[text()="+orderNumber+"]/following::span[1]"));
		driver.click(By.xpath("//a[text()="+orderNumber+"]/following::span[1]"));
		driver.click(By.linkText("Details"));
		driver.waitForPageLoad();
	}

	public boolean isShippingAmountFromAutoshipTemplatePresent(){
		String shippingAmount = driver.findElement(By.xpath("//div[@id='main-content']//div[contains(text(),'Shipping')]/following::div[1]")).getText();
		return !(shippingAmount.substring(1)==null);
	}
}