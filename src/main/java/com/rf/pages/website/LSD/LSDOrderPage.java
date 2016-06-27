package com.rf.pages.website.LSD;

import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class LSDOrderPage extends LSDRFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(LSDOrderPage.class.getName());

	public LSDOrderPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static final By FIRST_PROCESSED_ORDER_LOC = By.xpath("//div[@id='sub-stage']//section[4]/descendant::span[text()='Processed'][1]") ;
	private static final By ORDER_DATE_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][1]//li[2]/span") ;
	private static final By COMMISION_DATE_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][1]//li[3]/span") ;
	private static final By ORDER_NUMBER_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][1]//li[4]/span") ;
	private static final By PSQV_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][1]//li[5]/span") ;
	private static final By ORDER_STATUS_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][2]//li/span") ;
	private static final By ORDER_ITEMS_LOC = By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][2]//div[@class='items-container']//li[1]/span") ;
	private static final By FOOT_NOTE_LOC = By.xpath("//section[contains(@class,'footnote')]/p") ;
	private static final By ORDERS_PAGE = By.xpath("//section[@id='order-summary-header']");
	private static final By CONTACT_BUTTON_AT_FOOTER = By.xpath("//section[@class='order-nav']//div[@class='shadow-card-button-container']");
	private static final By CLOSE_ICON_OF_ORDER_SECTION = By.xpath("//section[@id='order-summary-modal']//div[@class='icon-close pointer']");
	private static final By FIRST_PROCESSED_PC_AUTOSHIP_ORDER_LOC = By.xpath("//div[@id='sub-stage']//section[4]/descendant::span[text()='Processed'][1]/following::span[text()='PC Perks'][1]");
	private static final By FIRST_PROCESSED_RC_ORDER_LOC = By.xpath("//div[@id='sub-stage']//section[4]/descendant::span[text()='Processed'][1]/following::span[text()='Retail Order'][1]");
	private static final By CONTACT_DETAILS_SECTION = By.xpath("//div[@class='contact-profile-content']//div[@class='shadow-card']");
	private static final By PHONE_ICON = By.xpath("//div[@class='icon']//span[@class='icon-phone']");
	private static final By EMAIL_ICON = By.xpath("//div[@class='icon']//span[@class='icon-email']");
	private static final By CLOSE_ICON_OF_CONTACT_SECTION = By.xpath("//section[@id='contact-profile-modal']//div[@class='icon-close pointer']");
	private static final By COMMISSION_TIME_PERIOD= By.id("order-period");
	private static final By FIRST_ORDER_DATE= By.xpath("//div[@id='sub-stage']//section[4]//order-status-card[@class='au-target'][1]//div[contains(@class,'shipment-date')]//li[1]");
	private static final By PREFERRED_CUSTOMER_ORDERS_CHK_BOX_IN_ALL_ORDER_TYPE = By.id("ORDER_TYPE_FILTER_PREFERRED_CUSTOMER");
	private static final By CHECKED_PREFERRED_CUSTOMER_ORDERS_CHK_BOX_IN_ORDER_STATUS = By.xpath("//span[@id='ORDER_TYPE_FILTER_PREFERRED_CUSTOMER'][@checked='checked']");
	private static final By ORDER_NAME_FROM_ORDER= By.xpath("//div[@class='order-summary-content']//h2");
	private static final By ORDER_TYPE_FROM_ORDER= By.xpath("//div[@class='order-summary-content']//p");
	private static final By CONSULTANT_ME_TXT_LOC= By.xpath("//div[@class='order-summary-content']/div[@class='shadow-card'][1]//li[1]//span[text()='Me']");
	private static final By FIRST_RETURNED_ORDER_LOC = By.xpath("//div[@id='sub-stage']//section[1]/descendant::span[text()='Returned'][1]");

	private static final By ADD_FILTERS = By.xpath("//span[text()='Add filters']");
	private static final By CONSULTANT_ORDER_CHK_BOX_IN_ALL_ORDER_TYPE = By.id("ORDER_TYPE_FILTER_CONSULTANT");
	private static final By PROCESSED_ORDERS_CHK_BOX_IN_ORDER_STATUS = By.id("ORDER_STATUS_FILTER_PROCESSED");
	private static final By SET_FILTERS_BTN = By.id("set-filter");
	private static final By ALL_ORDERS = By.xpath("//div[@id='sub-stage']//section[4]//order-status-card[@class='au-target']");
	private static final By CHECKED_CONSULTANT_ORDER_CHK_BOX_IN_ALL_ORDER_TYPE = By.xpath("//span[@id='ORDER_TYPE_FILTER_CONSULTANT'][@checked='checked']");
	private static final By CHECKED_PROCESSED_ORDER_CHK_BOX_IN_ORDER_STATUS = By.xpath("//span[@id='ORDER_STATUS_FILTER_PROCESSED'][@checked='checked']");
	private static final By CLOSE_ICON_OF_FILTER = By.xpath("//section[@id='filter-modal']/div[@class='icon-close pointer au-target']");
	private static final By TRACKING_NUMBER_LINK = By.xpath("//div[@class='tracking-container']//a");
	private static final By TRACKING_NUMBER = By.xpath("//div[@class='shipmentTitleBar_track_nick_section']/div");
	private static final By VIEW_DETAILS_BTN = By.xpath("//span[text()='View details']");
	private static final By ORDER_NAME = By.xpath("//div[@class='order-detail-header']/p[1]");
	private static final By ORDER_TYPE = By.xpath("//div[@class='order-detail-header']/p[2]");
	private static final By ENROLLED_DATE = By.xpath("//div[@class='order-detail-header']/p[3]");
	private static final By ORDER_ITEMS = By.xpath("//div[@class='order-detail-shipment']//div[@class='items-container']/div");
	private static final By SKU_VALUE_UNDER_ORDER_ITEMS = By.xpath("//div[@class='order-detail-shipment']//div[@class='items-container']/div//div[contains(text(),'SKU')]");
	private static final By PRICE_UNDER_ORDER_ITEMS = By.xpath("//div[@class='order-detail-shipment']//div[@class='items-container']/div//div[@class='price']");
	private static final By QUANTITY_UNDER_ORDER_ITEMS = By.xpath("//div[@class='order-detail-shipment']//div[@class='items-container']/div//div[contains(text(),'Quantity')]");
	private static final By TRACK_ORDER_BTN = By.xpath("//span[text()='Track order']");
	private static final By SHIPPING_DETAILS_SUBHEADING = By.xpath("//div[@class='order-detail-shipping']//h3[text()='Address']");
	private static final By SHIPPING_PROFILE_NAME = By.xpath("//div[@class='order-detail-shipping']/div/div[1]/div/span");
	private static final By SHIPPING_METHOD = By.xpath("//div[@class='order-detail-shipping']//h3[text()='Method']");
	private static final By BILLING_DETAILS_SUBHEADING = By.xpath("//div[@class='order-detail-billing']//h3[text()='Address']");
	private static final By BILLING_PROFILE_NAME = By.xpath("//div[@class='order-detail-shipping']/div/div[1]/div/span");
	private static final By THIS_WEEK_TEXT_LOC= By.xpath("//span[contains(text(),'this week:')]");
	private static final By UPCOMING_TEXT_LOC= By.xpath("//span[text()='upcoming']");
	private static final By EARLIER_THIS_MONTH_TEXT_LOC= By.xpath("//span[text()='earlier this month']");
	private static final By FIRST_ORDER_USERNAME_LOC = By.xpath("//div[@id='sub-stage']//section[4]//order-status-card[@class='au-target'][1]//div[@class='order-summary']/div[1]");
	private static final By FIRST_ORDER_STATUS_LOC = By.xpath("//div[@id='sub-stage']//section[4]//order-status-card[@class='au-target'][1]//div[@class='order-summary']//li[1]/span");
	private static final By FIRST_ORDER_TYPE_LOC = By.xpath("//div[@id='sub-stage']//section[4]//order-status-card[@class='au-target'][1]//div[@class='order-summary']//li[2]/span");
	private static final By FIRST_ORDER_PSQV_LOC = By.xpath("//div[@id='sub-stage']//section[4]//order-status-card[@class='au-target'][1]//div[@class='order-summary']//li[3]/span");
	private static final By SV_VALUE_IN_VIEW_ORDER_DETAILS= By.xpath("//div[@class='order-detail-overview']//li[contains(text(),'SV')]/span");
	private static final By SHIPPING_METHOD_IN_VIEW_ORDER_DETAILS= By.xpath("//div[@class='order-detail-shipping-method']/div[1]");
	private static final By CONTACT_NAME_AT_FOOTER = By.xpath("//section[@class='order-nav']//div[@class='shadow-card-button-container']//span[@class='label']");
	private static final By BACK_ARROW_ICON_OF_VIEW_DETAILS = By.xpath("//section[@id='order-detail-modal']/div[1]");

	private static String presentFilterName= "//div[@class='filter-list-tag']//span[text()='%s']";
	private static String getOrderStatus= "//div[@id='sub-stage']//section[4]//order-status-card[@class='au-target'][%s]//ul[contains(@class,'data-point-list')]/li[1]/span";
	private static String getOrderType = "//div[@id='sub-stage']//section[4]//order-status-card[@class='au-target'][%s]//ul[contains(@class,'data-point-list')]/li[2]/span";
	private static String cancelIconOfFilter= "//div[@class='filter-list-tag']//span[text()='%s']/following::div[1]";
	private static String presentorderType = "//div[@id='sub-stage']//section[4]/descendant::span[text()='%s'][1]";
	private static String orderMetricsCount = "//section[@id='order-summary-header']/ul[1]//span[text()='%s']/preceding::div[1]";
	private static String orderLables = "//div[@class='order-summary-content']/div[@class='shadow-card'][1]//li[contains(text(),'%s')]";
	private static String orderDetailsHeader = "//h1[text()='%s']";
	private static String orderHeader = "//h2[text()='%s']";
	private static String overviewDetails = "//div[@class='order-detail-overview']//li[contains(text(),'%s')]";
	private static String shipmentDetails = "//div[@class='order-detail-shipment']//li[contains(text(),'%s')]";
	private static String orderMetrics = "//section[@id='order-summary-header']/ul[1]//span[text()='%s']";

	public void clickFirstProcessedOrder(){
		driver.waitForElementPresent(FIRST_PROCESSED_ORDER_LOC);
		driver.click(FIRST_PROCESSED_ORDER_LOC);
	}

	public String getOrderDate(){
		driver.waitForElementPresent(ORDER_DATE_LOC);
		String orderDate = driver.findElement(ORDER_DATE_LOC).getText();
		logger.info("order date is "+orderDate);
		return orderDate;
	}

	public String getCommisionDate(){
		driver.waitForElementPresent(COMMISION_DATE_LOC);
		String commisionDate = driver.findElement(COMMISION_DATE_LOC).getText();
		logger.info("commision date is "+commisionDate);
		return commisionDate;
	}

	public String getOrderNumber(){
		driver.waitForElementPresent(ORDER_NUMBER_LOC);
		String orderNumber = driver.findElement(ORDER_NUMBER_LOC).getText();
		logger.info("order Number is "+orderNumber);
		return orderNumber;
	}

	public String getPSQV(){
		driver.waitForElementPresent(PSQV_LOC);
		String PSQV = driver.findElement(PSQV_LOC).getText();
		logger.info("PSQV is "+PSQV);
		return PSQV;
	}

	public String getOrderStatus(){
		driver.waitForElementPresent(ORDER_STATUS_LOC);
		String orderStatus = driver.findElement(ORDER_STATUS_LOC).getText();
		logger.info("orderStatus is "+orderStatus);
		return orderStatus;
	}

	public String getOrderItems(){
		driver.waitForElementPresent(ORDER_ITEMS_LOC);
		String orderItems = driver.findElement(ORDER_ITEMS_LOC).getText();
		logger.info("order Items is "+orderItems);
		return orderItems;
	}

	public String getFootNote(){
		driver.waitForElementPresent(FOOT_NOTE_LOC);
		String footNote = driver.findElement(FOOT_NOTE_LOC).getText();
		logger.info("foot Note is "+footNote);
		return footNote;
	}

	public boolean isOrdersPagePresent(){
		driver.waitForElementPresent(ORDERS_PAGE);
		return driver.isElementPresent(ORDERS_PAGE);
	}

	public boolean isContactButtonPresentAtFooter(){
		driver.waitForElementPresent(CONTACT_BUTTON_AT_FOOTER);
		return driver.isElementPresent(CONTACT_BUTTON_AT_FOOTER);
	}

	public void clickCloseIconOfOrder(){
		driver.waitForElementPresent(CLOSE_ICON_OF_ORDER_SECTION);
		driver.click(CLOSE_ICON_OF_ORDER_SECTION);
		logger.info("close icon clicked of an order");
	}

	public void clickFirstProcessedPCAutishipOrder(){
		driver.waitForElementPresent(FIRST_PROCESSED_PC_AUTOSHIP_ORDER_LOC);
		driver.click(FIRST_PROCESSED_PC_AUTOSHIP_ORDER_LOC);
		logger.info("First processed pc autoship order clicked");
		driver.waitForLSDLoaderAnimationImageToDisappear();
	}

	public void clickFirstProcessedRCOrder(){
		driver.waitForElementPresent(FIRST_PROCESSED_RC_ORDER_LOC);
		driver.click(FIRST_PROCESSED_RC_ORDER_LOC);
		logger.info("First processed rc order clicked");
		driver.waitForLSDLoaderAnimationImageToDisappear();
	}

	public void clickContactButtonAtFooter(){
		driver.waitForElementPresent(CONTACT_BUTTON_AT_FOOTER);
		driver.click(CONTACT_BUTTON_AT_FOOTER);
		logger.info("Contact button clicked at footer");
	}

	public boolean isContactDetailsPresent(){
		driver.waitForElementPresent(CONTACT_DETAILS_SECTION);
		return driver.isElementPresent(CONTACT_DETAILS_SECTION);
	}

	public boolean isPhoneIconPresent(){
		driver.waitForElementPresent(PHONE_ICON);
		return driver.isElementPresent(PHONE_ICON);
	}

	public boolean isEmailIconPresent(){
		driver.waitForElementPresent(EMAIL_ICON);
		return driver.isElementPresent(EMAIL_ICON);
	}

	public void clickCloseIconOfContact(){
		driver.waitForElementPresent(CLOSE_ICON_OF_CONTACT_SECTION);
		driver.click(CLOSE_ICON_OF_CONTACT_SECTION);
		logger.info("close icon clicked of contact section");
	}


	public void clickAddFilters(){
		driver.waitForElementPresent(ADD_FILTERS);
		driver.click(ADD_FILTERS);
		logger.info("Add filter button clicked");
	}

	public void clickConsultantOrderChkBoxInAllOrderTypes(){
		driver.waitForElementPresent(CONSULTANT_ORDER_CHK_BOX_IN_ALL_ORDER_TYPE);
		driver.click(CONSULTANT_ORDER_CHK_BOX_IN_ALL_ORDER_TYPE);
		logger.info("Consultant order checkbox checked as filter");
	}

	public void clickProcessedOrderChkBoxInOrderStatus(){
		driver.waitForElementPresent(PROCESSED_ORDERS_CHK_BOX_IN_ORDER_STATUS);
		driver.click(PROCESSED_ORDERS_CHK_BOX_IN_ORDER_STATUS);
		logger.info("Processed orders checkbox checked as filter");
	}

	public void clickSetFiltersBtn(){
		driver.waitForElementPresent(SET_FILTERS_BTN);
		driver.click(SET_FILTERS_BTN);
		logger.info("Set filter filter button clicked");
	}

	public boolean isFilterAppliedSuccessfully(String filterName){
		driver.waitForElementPresent(By.xpath(String.format(presentFilterName, filterName)));
		return driver.isElementPresent(By.xpath(String.format(presentFilterName, filterName)));
	}

	public int getCountOfTotalOrders(){
		driver.waitForElementPresent(ALL_ORDERS);
		int countOfOrders = driver.findElements(ALL_ORDERS).size();
		logger.info("Count of Orders: "+countOfOrders);
		return countOfOrders;
	}

	public String getOrderStatus(int orderNumber){
		driver.waitForElementPresent(By.xpath(String.format(getOrderStatus, orderNumber)));
		String orderStatus = driver.findElement(By.xpath(String.format(getOrderStatus, orderNumber))).getText();
		logger.info("Order status for order number"+orderNumber+"th is: "+orderStatus);
		return orderStatus;
	}

	public String getOrderType(int orderNumber){
		driver.waitForElementPresent(By.xpath(String.format(getOrderType, orderNumber)));
		String orderType = driver.findElement(By.xpath(String.format(getOrderType, orderNumber))).getText();
		logger.info("Order type for order number"+orderNumber+"th is: "+orderType);
		return orderType;
	}

	public void clickCloseIconOfFilter(String filterName){
		driver.waitForElementPresent(By.xpath(String.format(cancelIconOfFilter, filterName)));
		driver.click(By.xpath(String.format(cancelIconOfFilter, filterName)));
		logger.info("cross icon clicked of"+filterName+" filter");
	}

	public boolean isOrderTypePresentInOrders(String orderType){
		driver.waitForElementPresent(By.xpath(String.format(presentorderType, orderType)));
		return driver.isElementPresent(By.xpath(String.format(presentorderType, orderType)));
	}

	public boolean isConsultantOrdersCheckBoxIsChecked(){
		driver.waitForElementPresent(CHECKED_CONSULTANT_ORDER_CHK_BOX_IN_ALL_ORDER_TYPE);
		return driver.isElementPresent(CHECKED_CONSULTANT_ORDER_CHK_BOX_IN_ALL_ORDER_TYPE);
	}

	public boolean isProcessedOrdersCheckBoxIsChecked(){
		driver.waitForElementPresent(CHECKED_PROCESSED_ORDER_CHK_BOX_IN_ORDER_STATUS);
		return driver.isElementPresent(CHECKED_PROCESSED_ORDER_CHK_BOX_IN_ORDER_STATUS);
	}

	public void clickCloseIconOfFilter(){
		driver.waitForElementPresent(CLOSE_ICON_OF_FILTER);
		driver.click(CLOSE_ICON_OF_FILTER);
		logger.info("Close Icon clicked of Filter");
	}

	public String clickAndGetTrackingNumberLink(){
		driver.waitForElementPresent(TRACKING_NUMBER_LINK);
		String trackingNumber = driver.findElement(TRACKING_NUMBER_LINK).getText();
		driver.click(TRACKING_NUMBER_LINK);
		logger.info("Tracking number "+trackingNumber+" clicked");
		driver.waitForPageLoad();
		return trackingNumber;
	}

	public boolean isTrackingWebsitePagePresent(String parentWindowID, String trackingNumber){
		Set<String> set=driver.getWindowHandles();
		Iterator<String> it=set.iterator();
		boolean status=false;
		while(it.hasNext()){
			String childWindowID=it.next();
			if(!parentWindowID.equalsIgnoreCase(childWindowID)){
				driver.switchTo().window(childWindowID);
				logger.info("navigate to feedback tab");
				if(driver.getCurrentUrl().contains("track") && driver.findElement(TRACKING_NUMBER).getText().contains(trackingNumber)){
					status=true;
				}
			}
		}
		driver.close();
		logger.info("Child window closed");
		driver.switchTo().window(parentWindowID);
		logger.info("navigate to parent window");
		return status;
	}


	public void clickViewDetailsBtn(){
		driver.waitForElementPresent(VIEW_DETAILS_BTN);
		driver.click(VIEW_DETAILS_BTN);
		logger.info("View Details button clicked");
	}

	public boolean isOrderDetailsHeaderPresent(String headerName){
		driver.waitForElementPresent(By.xpath(String.format(orderDetailsHeader, headerName)));
		return driver.isElementPresent(By.xpath(String.format(orderDetailsHeader, headerName)));
	}

	public boolean isOrderHeaderPresent(String headerName){
		driver.waitForElementPresent(By.xpath(String.format(orderHeader, headerName)));
		return driver.isElementPresent(By.xpath(String.format(orderHeader, headerName)));
	}

	public String getOrderNamePresentInViewOrderDetails(){
		driver.waitForElementPresent(ORDER_NAME);
		String orderName = driver.findElement(ORDER_NAME).getText();
		logger.info("Order name is: "+orderName);
		return orderName;
	}

	public String getOrderTypePresentInViewOrderDetails(){
		driver.waitForElementPresent(ORDER_TYPE);
		String orderType = driver.findElement(ORDER_TYPE).getText();
		logger.info("Order type is: "+orderType);
		return orderType;
	}

	public String getEnrolledDatePresentInViewOrderDetails(){
		driver.waitForElementPresent(ENROLLED_DATE);
		String enrolledDate = driver.findElement(ENROLLED_DATE).getText();
		logger.info("Enrolled Date is: "+enrolledDate);
		return enrolledDate;
	}

	public boolean isOverviewDetailsPresent(String label){
		driver.waitForElementPresent(By.xpath(String.format(overviewDetails, label)));
		return driver.isElementPresent(By.xpath(String.format(overviewDetails, label)));
	}

	public boolean isShipmentDetailsPresent(String label){
		driver.waitForElementPresent(By.xpath(String.format(shipmentDetails, label)));
		return driver.isElementPresent(By.xpath(String.format(shipmentDetails, label)));
	}

	public boolean isOrderItemsPresent(){
		driver.waitForElementPresent(ORDER_ITEMS);
		return driver.isElementPresent(ORDER_ITEMS);
	}

	public boolean isSKUValuePresentUnderOrderItems(){
		driver.waitForElementPresent(SKU_VALUE_UNDER_ORDER_ITEMS);
		return driver.isElementPresent(SKU_VALUE_UNDER_ORDER_ITEMS);
	}

	public boolean isTotalPricePresentUnderOrderItems(){
		driver.waitForElementPresent(PRICE_UNDER_ORDER_ITEMS);
		return driver.isElementPresent(PRICE_UNDER_ORDER_ITEMS);
	}

	public boolean isQuantityPresentUnderOrderItems(){
		driver.waitForElementPresent(QUANTITY_UNDER_ORDER_ITEMS);
		return driver.isElementPresent(QUANTITY_UNDER_ORDER_ITEMS);
	}

	public boolean isTrackOrderBtnPresent(){
		driver.waitForElementPresent(TRACK_ORDER_BTN);
		return driver.isElementPresent(TRACK_ORDER_BTN);
	}

	public boolean isShippingDetailsSubHeadingPresent(){
		driver.waitForElementPresent(SHIPPING_DETAILS_SUBHEADING);
		return driver.isElementPresent(SHIPPING_DETAILS_SUBHEADING);
	}

	public String getShippingAddressName(){
		driver.waitForElementPresent(SHIPPING_PROFILE_NAME);
		String name = driver.findElement(SHIPPING_PROFILE_NAME).getText();
		logger.info("Shipping address name is: "+name);
		return name;
	}

	public boolean isShippingMethodPresent(){
		driver.waitForElementPresent(SHIPPING_METHOD);
		return driver.isElementPresent(SHIPPING_METHOD);
	}

	public boolean isBillingDetailsSubHeadingPresent(){
		driver.waitForElementPresent(BILLING_DETAILS_SUBHEADING);
		return driver.isElementPresent(BILLING_DETAILS_SUBHEADING);
	}

	public String getBillingProfileName(){
		driver.waitForElementPresent(BILLING_PROFILE_NAME);
		String name = driver.findElement(BILLING_PROFILE_NAME).getText();
		logger.info("Billing address name is: "+name);
		return name;
	}

	public boolean isReturnedOrderPresent(){
		driver.waitForElementPresent(FIRST_RETURNED_ORDER_LOC);
		return driver.isElementPresent(FIRST_RETURNED_ORDER_LOC);
	}

	public boolean isOrderMetricsPresent(String name){
		driver.waitForElementPresent(By.xpath(String.format(orderMetrics, name)));
		return driver.isElementPresent(By.xpath(String.format(orderMetrics, name)));
	}

	public boolean isAddFilterBtnPresent(){
		driver.waitForElementPresent(ADD_FILTERS);
		return driver.isElementPresent(ADD_FILTERS);
	}

	public boolean isThisWeekTextPresentForOrders(){
		driver.waitForElementPresent(THIS_WEEK_TEXT_LOC);
		return driver.isElementPresent(THIS_WEEK_TEXT_LOC);
	}

	public boolean isUpcomingTextPresentForOrders(){
		driver.waitForElementPresent(UPCOMING_TEXT_LOC);
		return driver.isElementPresent(UPCOMING_TEXT_LOC);
	}

	public boolean isEarlierThisMonthTextPresentForOrders(){
		driver.waitForElementPresent(EARLIER_THIS_MONTH_TEXT_LOC);
		return driver.isElementPresent(EARLIER_THIS_MONTH_TEXT_LOC);
	}

	public String getFirstOrderUserName(){
		driver.waitForElementPresent(FIRST_ORDER_USERNAME_LOC);
		String name = driver.findElement(FIRST_ORDER_USERNAME_LOC).getText();
		logger.info("First order's username is: "+name);
		return name;
	}

	public String getFirstOrderStatus(){
		driver.waitForElementPresent(FIRST_ORDER_STATUS_LOC);
		String status = driver.findElement(FIRST_ORDER_STATUS_LOC).getText();
		logger.info("First order's status is: "+status);
		return status;
	}

	public String getFirstOrderType(){
		driver.waitForElementPresent(FIRST_ORDER_TYPE_LOC);
		String orderType = driver.findElement(FIRST_ORDER_TYPE_LOC).getText();
		logger.info("First order's type is: "+orderType);
		return orderType;
	}

	public String getFirstOrderPSQVValue(){
		driver.waitForElementPresent(FIRST_ORDER_PSQV_LOC);
		String PSQV = driver.findElement(FIRST_ORDER_PSQV_LOC).getText();
		logger.info("First order's status is: "+PSQV);
		return PSQV;
	}

	public String getOrderMetricsCount(String name){
		driver.waitForElementPresent(By.xpath(String.format(orderMetricsCount, name)));
		String orderCount = driver.findElement(By.xpath(String.format(orderMetricsCount, name))).getText();
		logger.info("order count of "+name+" is: "+orderCount);
		return orderCount;
	}

	public String getCommissionTimePeriod(){
		driver.waitForElementPresent(COMMISSION_TIME_PERIOD);
		String commissionTime = driver.findElement(COMMISSION_TIME_PERIOD).getText();
		logger.info("Commission Time period is "+commissionTime);
		return commissionTime;
	}

	public boolean isFirstOrderDatePresent(){
		driver.waitForElementPresent(FIRST_ORDER_DATE);
		return driver.IsElementVisible(driver.findElement(FIRST_ORDER_DATE));
	}

	public void clickPreferredCustomerOrderOrderChkBoxInAllOrderTypes(){
		driver.waitForElementPresent(PREFERRED_CUSTOMER_ORDERS_CHK_BOX_IN_ALL_ORDER_TYPE);
		driver.click(PREFERRED_CUSTOMER_ORDERS_CHK_BOX_IN_ALL_ORDER_TYPE);
		logger.info("Preferred Customer order checkbox checked as filter");
	}

	public boolean isPreferredCustomerOrdersCheckBoxIsChecked(){
		driver.waitForElementPresent(CHECKED_PREFERRED_CUSTOMER_ORDERS_CHK_BOX_IN_ORDER_STATUS);
		return driver.isElementPresent(CHECKED_PREFERRED_CUSTOMER_ORDERS_CHK_BOX_IN_ORDER_STATUS);
	}

	public String getOrderNameAfterClickedOnOrder(){
		driver.waitForElementPresent(ORDER_NAME_FROM_ORDER);
		String orderName = driver.findElement(ORDER_NAME_FROM_ORDER).getText();
		logger.info("Order Name is "+orderName);
		return orderName;
	}

	public String getOrderTypeAfterClickedOnOrder(){
		driver.waitForElementPresent(ORDER_TYPE_FROM_ORDER);
		String orderType = driver.findElement(ORDER_TYPE_FROM_ORDER).getText();
		logger.info("Order Type is "+orderType);
		return orderType;
	}

	public boolean isConsultantMeTxtPresent(){
		driver.waitForElementPresent(CONSULTANT_ME_TXT_LOC);
		return driver.isElementPresent(CONSULTANT_ME_TXT_LOC);
	}

	public boolean isOrderLabelPresent(String name){
		driver.waitForElementPresent(By.xpath(String.format(orderLables, name)));
		return driver.isElementPresent(By.xpath(String.format(orderLables, name)));
	}

	public String getSVValueFromViewOrderDetails(){
		driver.waitForElementPresent(SV_VALUE_IN_VIEW_ORDER_DETAILS);
		String SVValue = driver.findElement(SV_VALUE_IN_VIEW_ORDER_DETAILS).getText();
		logger.info("SV value is "+SVValue);
		return SVValue;
	}

	public String getShippingMethodNameFromViewOrderDetails(){
		driver.waitForElementPresent(SHIPPING_METHOD_IN_VIEW_ORDER_DETAILS);
		String shippingMethod = driver.findElement(SHIPPING_METHOD_IN_VIEW_ORDER_DETAILS).getText();
		logger.info("Shipping method is "+shippingMethod);
		return shippingMethod;
	}

	public String getTotalPricePresentUnderOrderItems(){
		driver.waitForElementPresent(PRICE_UNDER_ORDER_ITEMS);
		String totalPrice =  driver.findElement(PRICE_UNDER_ORDER_ITEMS).getText();
		logger.info("Total price under order items is: "+totalPrice);
		return totalPrice;
	}

	public String getQuantityPresentUnderOrderItems(){
		driver.waitForElementPresent(QUANTITY_UNDER_ORDER_ITEMS);
		String quantity =  driver.findElement(QUANTITY_UNDER_ORDER_ITEMS).getText();
		logger.info("Total quantity under order items is: "+quantity);
		return quantity;
	}

	public String getContactNameFromContactButton(){
		driver.waitForElementPresent(CONTACT_NAME_AT_FOOTER);
		String name =  driver.findElement(CONTACT_NAME_AT_FOOTER).getText();
		logger.info("Contact name is: "+name);
		return name;
	}

	public void clickBackArrowIconOfViewDetails(){
		driver.click(BACK_ARROW_ICON_OF_VIEW_DETAILS);
		logger.info("Back arrow icon clicked");
	}

}