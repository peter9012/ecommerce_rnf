package com.rf.pages.website;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;

public class StoreFrontOrdersPage extends RFWebsiteBasePage{
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
		return driver.findElement(ORDER_NUMBER_LOC).getText().equalsIgnoreCase(orderNum);
	}

	public boolean verifyScheduleDate(String schDate){
		schDate = convertDBDateFormatToUIFormat(schDate);
		System.out.println("schDate "+schDate);
		System.out.println("UI date "+driver.findElement(ORDER_SCHEDULE_DATE_LOC).getText());
		return driver.findElement(ORDER_SCHEDULE_DATE_LOC).getText().equalsIgnoreCase(schDate);
	}

	public boolean verifyGrandTotal(String grandTotal){
		if(driver.getCurrentUrl().contains("/ca")){
			grandTotal = "CAD$ " +grandTotal;
		}
		else{
			grandTotal = "$"+grandTotal;
		}		
		return grandTotal.contains(driver.findElement(ORDER_GRAND_TOTAL_LOC).getText());
	}

	public boolean verifyOrderStatus(String status){
		return driver.findElement(ORDER_STATUS_LOC).getText().equalsIgnoreCase(status);
	}

	public void clickAutoshipOrderNumber(){		
		getOrderNumber();
		driver.click(ORDER_AUTOSHIP_ORDER_NUMBER_LOC);		
	}

	public String getOrderNumber(){
		autoShipOrderNumber = driver.findElement(ORDER_AUTOSHIP_ORDER_NUMBER_LOC).getText(); 
		return  autoShipOrderNumber;
	}

	public boolean verifyCRPAutoShipHeader(){
		System.out.println(driver.findElement(ORDERS_PAGE_CRP_AUTOSHIP_TEMPLATE_HEADER_LOC).getText());
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
		String svValue = driver.findElement(By.xpath("//table[@class='order-products']//tr[2]//td[@id='productSV']")).getText();
		System.out.println("----sv UI---- "+svValue);
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

	public boolean verifyPresenceOfScheduleDateText(){
		boolean isScheduleDateTextPresent = false;
		String scheduleDateText = driver.findElement(SCHEDULE_DATE_TEXT_LOC).getText();
		if(scheduleDateText.contains("SCHEDULE DATE")){
			isScheduleDateTextPresent = true;
		}
		return isScheduleDateTextPresent;
	}

	public boolean verifyPresenceOfOrderStatusText(){
		boolean isOrderStatusTextPresent = false;
		String orderStatusText = driver.findElement(ORDER_STATUS_TEXT_LOC).getText();
		if(orderStatusText.contains("ORDER STATUS")){
			isOrderStatusTextPresent = true;
		}
		return isOrderStatusTextPresent;
	}

	public boolean verifyExpirationDate(String expirationDate){
		String paymentCardDetails = driver.findElement(ORDER_PAYMENT_METHOD_LOC).getText();
		if(paymentCardDetails.contains(expirationDate)){
			return true;
		}
		return false;
	}

	public boolean verifyAutoshipTotalPrice(String price){
		String autoshipTotalPrice = driver.findElement(ORDER_TOTAL_PRICE_LOC).getText();
		String splittedPriceForAssertion = price.split("\\.")[0];
		if(autoshipTotalPrice.contains(splittedPriceForAssertion)){
			return true;
		}
		return false;
	}

	public boolean verifyAutoshipGrandTotalPrice(String grandTotalDB){
		String autoshipGrandTotalPrice = driver.findElement(ORDER_GRAND_TOTAL_BOTTOM_LOC).getText();
		System.out.println("GT--------- "+autoshipGrandTotalPrice.substring(1));
		if(autoshipGrandTotalPrice.substring(1).contains(grandTotalDB)){
			return true;
		}
		return false;
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

	public boolean verifyShippingMethod(String shippingMethodDB){
		System.out.println("UI --"+driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]")).getText());
		System.out.println("DB --"+shippingMethodDB);
		return driver.findElement(By.xpath("//ul[@class='order-detail-list']/li[2]/p[1]")).getText().contains(shippingMethodDB);
	}

	public boolean verifyPCPerksAutoShipHeader(){
		System.out.println(driver.findElement(ORDERS_PAGE_PCPERKS_AUTOSHIP_TEMPLATE_HEADER_LOC).getText());
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
		driver.click(LEFT_MENU_ACCOUNT_INFO_LOC);
		System.out.println("Clicked");
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

	//	public boolean verifyCRPAutoShipAddressFromDB(){
	//		String address = driver.findElement(ORDER_AUTOSHIP_ADDRESS_LOC).getText();
	//		return true;
	//	}

	public StoreFrontReportOrderComplaintPage clickOnActions(){
		driver.findElement(ACTIONS_BUTTON_LOC).click();
		driver.findElement(ACTIONS_DROPDOWN_LOC).click();	 
		return new StoreFrontReportOrderComplaintPage(driver);
	}

	public void orderNumberForOrderHistory(){
		orderNumberOfOrderHistory = driver.findElement(ORDER_NUM_OF_ORDER_HISTORY).getText();
	}

	public boolean isPaymentMethodContainsName(String name){
		return driver.findElement(ORDER_PAYMENT_METHOD_LOC).getText().toLowerCase().contains(name.toLowerCase());
	}

	public void clickOrderNumber(String orderNumber){
		driver.click(By.linkText(orderNumber));
	}

	public boolean verifyAutoShipTemplateSubtotal(String subTotalDB){
		String subTotal = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[1]/span")).getText();
		return subTotal.substring(1).contains(subTotalDB);
	}

	public boolean verifyAutoShipTemplateShipping(String shippingDB){
		String subTotal = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[2]/span")).getText();
		return subTotal.substring(1).contains(shippingDB);
	}

	public boolean verifyAutoShipTemplateHandling(String handlingDB){
		String subTotal = driver.findElement(By.xpath("//div[@class='order-summary-left']/ul[1]/li[3]/span")).getText();
		return subTotal.substring(1).contains(handlingDB);
	}

	public boolean verifyAutoShipTemplateTax(String taxDB){
		String subTotal = driver.findElement(By.xpath("//li[@id='module-hst']/span")).getText();
		return subTotal.trim().substring(1).contains(taxDB);
	}

//	public boolean verifyPayeeName(String payeeNameDB){
//		return driver.findElement(By.xpath("")).getText().contains(payeeNameDB);
//	}

	public boolean verifyCardType(String cardTypeDB){		
		if(cardTypeDB.contains("master")){		
			try{
				driver.findElement(By.xpath("//span[@class='cardType mastercard']"));				
				return true;
			}
			catch(NoSuchElementException e){
				return false;
			}
		}
		else if(cardTypeDB.contains("Visa")){
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

}