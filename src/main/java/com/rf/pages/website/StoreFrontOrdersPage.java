package com.rf.pages.website;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontOrdersPage extends RFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontOrdersPage.class.getName());	

	private final By ORDERS_PAGE_TEMPLATE_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top' and contains(text(),'Orders')]");
	private final By ORDERS_PAGE_CRP_AUTOSHIP_TEMPLATE_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top' and contains(text(),'Order details: CRP')]");
	private final By ORDER_NUMBER_LOC = By.cssSelector("table[class='orders-table']>tbody>tr:nth-child(2) td:nth-child(1)>a");
	private final By ORDER_SCHEDULE_DATE_LOC = By.cssSelector("table[class='orders-table']>tbody>tr:nth-child(2) td:nth-child(2)");
	private final By ORDER_GRAND_TOTAL_LOC = By.cssSelector("table[class='orders-table']>tbody>tr:nth-child(2) td:nth-child(3)");
	private final By ORDER_STATUS_LOC = By.cssSelector("table[class='orders-table']>tbody>tr:nth-child(2) td:nth-child(4)");
	private final By ORDER_AUTOSHIP_ORDER_NUMBER_LOC = By.xpath("//table[contains(@id,'autoship-orders-table')]/tbody/tr[1]/td/a");
	private final By ORDER_AUTOSHIP_ADDRESS_LOC = By.xpath("//ul[@class='order-detail-list']/li[1]/p");
	private final By SCHEDULE_DATE_TEXT_LOC = By.xpath("//div[@class='order-summary-left']/span/h3");
	private final By ORDER_STATUS_TEXT_LOC = By.xpath("//div[@class='order-summary-left']/h3");
	private final By ORDER_PAYMENT_METHOD_LOC = By.xpath("//strong[text()='Payment Method:']/ancestor::p");
	private final By ORDER_TOTAL_PRICE_LOC = By.xpath("//table[@class='order-products']//td[@id='productSV']//following::td[2]");
	private final By ORDER_GRAND_TOTAL_BOTTOM_LOC = By.xpath("//li[@class='grand-total']/span");
	private final By SKU_VALUE_OF_ITEM_IN_ORDER_LOC = By.xpath("//td[text()='Items In Order']//following::img[1]//ancestor::td/span");
	private final By ITEM_ORDER_DESCRIPTION_LOC = By.xpath("//td[text()='Items In Order']//following::img[1]//ancestor::td");
	private final By SHIPPING_ADDRESS_LOC = By.xpath("//strong[text()='Shipping Address:']/following::p[1]");
	private final By ORDERS_PAGE_PCPERKS_AUTOSHIP_TEMPLATE_HEADER_LOC = By.xpath("//div[@class='gray-container-info-top' and contains(text(),'Order details:')]");
	private final By LEFT_MENU_ACCOUNT_INFO_LOC = By.xpath("//div[@id='left-menu']//a[text()='ACCOUNT INFO']");
	private String ORDER_NUMBER_STATUS_LOC = "//table[@id='history-orders-table']//a[text()='%s']/following::td[@class='fourth'][1]";
	private final By ACTIONS_BUTTON_LOC = By.xpath("//table[@id='history-orders-table']/tbody/tr[2]/td[5]/span/img");
	private final By ACTIONS_DROPDOWN_LOC = By.xpath("//table[@id='history-orders-table']//tr[2]/td[5]/ul/li[2]/a");
	private final By ORDER_NUM_OF_ORDER_HISTORY = By.xpath("//table[@id='history-orders-table']/tbody/tr[2]/td[1]/a");

	String autoShipOrderNumber = null;
	static String  orderNumberOfOrderHistory = null;

	public StoreFrontOrdersPage(RFWebsiteDriver driver) {
		super(driver);	
	}

	public boolean verifyOrdersPageIsDisplayed(){
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
		logger.info("Order Grand total from UI is "+driver.findElement(ORDER_GRAND_TOTAL_LOC).getText());
		return grandTotal.contains(driver.findElement(ORDER_GRAND_TOTAL_LOC).getText());
	}

	public boolean verifyOrderStatus(String status){
		driver.waitForElementPresent(ORDER_STATUS_LOC);
		logger.info("Order Status from UI "+driver.findElement(ORDER_STATUS_LOC).getText());
		return driver.findElement(ORDER_STATUS_LOC).getText().toLowerCase().contains(status.toLowerCase());
	}

	public void clickAutoshipOrderNumber(){		
		getAutoshipOrderNumber();
		driver.click(ORDER_AUTOSHIP_ORDER_NUMBER_LOC);
		logger.info("autoship order clicked " +ORDER_AUTOSHIP_ORDER_NUMBER_LOC);
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
		return driver.findElement(ORDERS_PAGE_CRP_AUTOSHIP_TEMPLATE_HEADER_LOC).getText().contains("ORDER DETAILS: CRP #"+autoShipOrderNumber);
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
		return autoshipGrandTotalPrice.substring(1);
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
		return driver.findElement(By.xpath("//strong[text()='Shipping Address:']/following::p[1]")).getText();
	}

	public String getShippingAddressFromAdhocTemplate(){
		return driver.findElement(By.xpath("//strong[text()='Shipping Address:']/following::p[1]")).getText();
	}

	public boolean verifyShippingMethod(String shippingMethodDB){
		String shippingMethodUI = driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]")).getText();
		logger.info("Shipping Method from UI is "+shippingMethodUI);
		return shippingMethodUI.contains(shippingMethodDB);
	}

	public String getShippingMethodFromAdhocOrderTemplate(){
		String shippingMethodUI = driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]")).getText();
		logger.info("Shipping Method from UI is "+shippingMethodUI);
		return shippingMethodUI;
	}

	public String getShippingMethodFromAutoshipTemplate(){
		driver.waitForElementPresent(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]"));
		String shippingMethodUI = driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]")).getText();
		logger.info("Shipping Method from UI is "+shippingMethodUI);
		return shippingMethodUI;
	}

	public boolean verifyPCPerksAutoShipHeader(){
		driver.waitForElementPresent(ORDERS_PAGE_PCPERKS_AUTOSHIP_TEMPLATE_HEADER_LOC);
		return driver.findElement(ORDERS_PAGE_PCPERKS_AUTOSHIP_TEMPLATE_HEADER_LOC).getText().contains("ORDER DETAILS: AUTOSHIPMENT #"+autoShipOrderNumber);
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
		driver.waitForElementPresent(By.xpath("//table[@id='history-orders-table']//tr"));
		int sizeOfOrders = driver.findElements(By.xpath("//table[@id='history-orders-table']//tr")).size();
		for(int i=1; i<=sizeOfOrders; i++){
			driver.waitForElementPresent(By.xpath("//table[@id='history-orders-table']//tr["+i+"]/td[4]"));
			if(driver.findElement(By.xpath("//table[@id='history-orders-table']//tr["+i+"]/td[4]")).getText().contains("SUBMITTED")){
				return true;	     
			}

		}
		return false;
	}


	public StoreFrontReportOrderComplaintPage clickOnActions(){
		driver.waitForElementPresent(ACTIONS_BUTTON_LOC);
		driver.findElement(ACTIONS_BUTTON_LOC).click();
		logger.info("Action drop down clicked for first order");
		driver.waitForElementPresent(ACTIONS_DROPDOWN_LOC);
		driver.findElement(ACTIONS_DROPDOWN_LOC).click();
		logger.info("Report Problems link clicked for first order");
		return new StoreFrontReportOrderComplaintPage(driver);
	}

	public void orderNumberForOrderHistory(){
		orderNumberOfOrderHistory = driver.findElement(ORDER_NUM_OF_ORDER_HISTORY).getText();
	}

	public void clickOnFirstAdHocOrder(){
		driver.waitForElementPresent(ORDER_NUM_OF_ORDER_HISTORY);
		driver.findElement(ORDER_NUM_OF_ORDER_HISTORY).click();		
	}

	public String getFirstOrderNumberFromOrderHistory(){
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
	}

	public boolean verifyAutoShipTemplateSubtotal(String subTotalDB){
		String subTotal = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[1]/span")).getText();
		return subTotal.substring(1).contains(subTotalDB);
	}

	public String getSubTotalFromAutoshipTemplate(){
		String subTotal = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[1]/span")).getText();
		return subTotal.substring(1);		
	}

	public boolean verifyAutoShipTemplateShipping(String shippingDB){
		String shippingAmount = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[2]/span")).getText();
		return shippingAmount.substring(1).contains(shippingDB);
	}

	public String getShippingAmountFromAutoshipTemplate(){
		String shippingAmount = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[2]/span")).getText();
		return shippingAmount.substring(1);
	}

	public boolean verifyAutoShipTemplateHandling(String handlingDB){
		String handlingCharges = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[3]/span")).getText();
		return handlingCharges.substring(1).contains(handlingDB);
	}

	public String getHandlingAmountFromAutoshipTemplate(){
		String handlingCharges = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[3]/span")).getText();
		return handlingCharges.substring(1);
	}

	public boolean verifyOrderHistoryTax(String taxDB){
		String tax = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]//p[2]//span")).getText();
		return tax.trim().substring(1).contains(taxDB);
	}

	public String getTaxAmountFromAdhocOrderTemplate(){
		String tax = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]//p[2]//span")).getText();
		return tax.trim().substring(1);
	}

	public boolean verifyAutoShipTemplateTax(String taxDB){
		String tax = driver.findElement(By.xpath("//span[@id='crpTotalTax']")).getText();
		return tax.trim().substring(1).contains(taxDB);
	}

	public String getTaxAmountFromAutoshipTemplate(){
		String tax = driver.findElement(By.xpath("//span[@id='crpTotalTax']")).getText();
		return tax.trim().substring(1);
	}

	//	public boolean verifyPayeeName(String payeeNameDB){
	//		return driver.findElement(By.xpath("")).getText().contains(payeeNameDB);
	//	}

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
		logger.info("total from UI "+driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[4]/span")).getText());
		return driver.findElement(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[4]/span")).getText();
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
			driver.waitForElementPresent(By.xpath("//table[@id='history-orders-table']//tr"));
			int sizeOfOrders = driver.findElements(By.xpath("//table[@id='history-orders-table']//tr")).size();
			for(int i=1; i<=sizeOfOrders; i++){
				driver.waitForElementPresent(By.xpath("//table[@id='history-orders-table']//tr["+i+"]/td[4]"));
				if(driver.findElement(By.xpath("//table[@id='history-orders-table']//tr["+i+"]/td[4]")).getText().contains("FAILED")){
					driver.waitForElementPresent(By.xpath("//table[@id='history-orders-table']//tr["+i+"]//td[text()='FAILED']/preceding::td[3]"));
					String failedOrderNumber = driver.findElement(By.xpath("//table[@id='history-orders-table']//tr["+i+"]//td[text()='FAILED']/preceding::td[3]")).getText();
					clickOrderNumber(failedOrderNumber);
					driver.waitForElementPresent(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[4]/span"));
					if(driver.findElement(By.xpath("//div[@class='gray-container-info-top']")).getText().contains("ORDER DETAILS: CRP #")==true){
						return failedOrderNumber;
					}else{
						driver.navigate().back();
					}
				}
			}   
			if(driver.isElementPresent(By.xpath("//table[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"))==true){
				isNextLinkPresent = true;
				driver.click(By.xpath("//table[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"));
			}
		}while(isNextLinkPresent==true);
		return null;
	}


	public String getOrderNumberFromOrderHistoryForFailedAutoshipOrdersForPC() throws InterruptedException {
		boolean isNextLinkPresent =  false;
		do{
			driver.waitForElementPresent(By.xpath("//table[@id='history-orders-table']//tr"));
			int sizeOfOrders = driver.findElements(By.xpath("//table[@id='history-orders-table']//tr")).size();
			for(int i=1; i<=sizeOfOrders; i++){
				driver.waitForElementPresent(By.xpath("//table[@id='history-orders-table']//tr["+i+"]/td[4]"));
				if(driver.findElement(By.xpath("//table[@id='history-orders-table']//tr["+i+"]/td[4]")).getText().contains("FAILED")){
					driver.waitForElementPresent(By.xpath("//table[@id='history-orders-table']//tr["+i+"]//td[text()='FAILED']/preceding::td[3]"));
					String failedOrderNumber = driver.findElement(By.xpath("//table[@id='history-orders-table']//tr["+i+"]//td[text()='FAILED']/preceding::td[3]")).getText();
					clickOrderNumber(failedOrderNumber);
					driver.waitForElementPresent(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[4]/span"));
					if(driver.findElement(By.xpath("//div[@class='gray-container-info-top']")).getText().contains("AUTOSHIPMENT")==true){
						return failedOrderNumber;
					}else{
						driver.navigate().back();
					}
				}
			}
			if(driver.isElementPresent(By.xpath("//table[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"))==true){
				isNextLinkPresent = true;
				driver.click(By.xpath("//table[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"));
			}
		}while(isNextLinkPresent==true);
		return null;
	}

	public String getOrderNumberFromOrderHistoryForFailedAdhocOrdersForRC() throws InterruptedException {
		driver.pauseExecutionFor(5000);
		boolean isNextLinkPresent =  false;
		do{
			driver.waitForElementPresent(By.xpath("//table[@id='history-orders-table']//tr"));
			int sizeOfOrders = driver.findElements(By.xpath("//table[@id='history-orders-table']//tr")).size();
			for(int i=1; i<=sizeOfOrders; i++){
				driver.waitForElementPresent(By.xpath("//table[@id='history-orders-table']//tr["+i+"]/td[4]"));
				if(driver.findElement(By.xpath("//table[@id='history-orders-table']//tr["+i+"]/td[4]")).getText().contains("FAILED")){
					driver.waitForElementPresent(By.xpath("//table[@id='history-orders-table']//tr["+i+"]//td[text()='FAILED']/preceding::td[3]"));
					String failedOrderNumber = driver.findElement(By.xpath("//table[@id='history-orders-table']//tr["+i+"]//td[text()='FAILED']/preceding::td[3]")).getText();
					clickOrderNumber(failedOrderNumber);
					driver.waitForElementPresent(By.xpath("//div[@id='main-content']//div[@class='order-summary-left'][2]/ul/li[4]/span"));
					if(driver.findElement(By.xpath("//div[@class='gray-container-info-top']")).getText().contains("ORDER")==true){
						return failedOrderNumber;
					}else{
						driver.navigate().back();
					}
				}
			}
			if(driver.isElementPresent(By.xpath("//table[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"))==true){
				isNextLinkPresent = true;
				driver.click(By.xpath("//table[@id='history-orders-table']/following::div[1]//a[contains(text(),'Next Page')]"));
			}

		}while(isNextLinkPresent==true);
		return null;
	}

	public void clickOnFirstAdhocOrder() throws InterruptedException {
		driver.findElement(By.xpath("//table[@id='history-orders-table']/tbody/tr[2]/td/a")).click();		
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
			shippingMethodName="Point of Sale";
			break;

		case 8:
			shippingMethodName="POS Drop-Ship";
			break;
		}
		return shippingMethodName;
	}

}