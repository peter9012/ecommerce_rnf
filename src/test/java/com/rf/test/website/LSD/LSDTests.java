package com.rf.test.website.LSD;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.RFLSDWebsiteBaseTest;

public class LSDTests extends RFLSDWebsiteBaseTest{

	//Welcome page to login page TC-259
	@Test(priority=1)
	public void testWelcomeLoginPage(){
		String wrongPassword = "101Maiden$";
		lsdHomePage.clickLogout();
		lsdLoginPage.enterUsername(nonwhiteListedUserName);
		lsdLoginPage.enterPassword(password);
		lsdLoginPage.clickLoginBtn();
		s_assert.assertTrue(lsdLoginPage.isLoginFailErrorPresent(), "Login fail error not appeared for non-whiteListed user");
		lsdLoginPage.enterUsername(whiteListedUserName);
		lsdLoginPage.enterPassword(wrongPassword);
		lsdLoginPage.clickLoginBtn();
		s_assert.assertTrue(lsdLoginPage.isLoginFailErrorPresent(), "Login fail error not appeared for whiteListed user with wrong password");
		lsdLoginPage.enterUsername(whiteListedUserName);
		lsdLoginPage.enterPassword(password);
		lsdLoginPage.clickLoginBtn();
		s_assert.assertTrue(lsdHomePage.getCurrentURL().toLowerCase().contains("home"), "user is not on home page after login,the current url is expected to have 'home',but the current URL is "+lsdHomePage.getCurrentURL());
		s_assert.assertAll();
	}

	//TC-1156 Order Summary - Design and data fields layout
	@Test(priority=2)
	public void testOrderSummary_1156(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickViewMyOrdersLink();
		lsdOrderPage.clickFirstProcessedOrder();
		s_assert.assertTrue(lsdOrderPage.getOrderNameAfterClickedOnOrder().toLowerCase().trim().contains(TestConstants.FIRST_ORDER_NAME.toLowerCase().trim()), "Expected Order Username is "+TestConstants.FIRST_ORDER_NAME+" but actual on UI is "+lsdOrderPage.getOrderNameAfterClickedOnOrder().trim());
		s_assert.assertTrue(lsdOrderPage.getOrderTypeAfterClickedOnOrder().trim().contains(TestConstants.ORDER_TYPE_CONSULTANT), "Expected Order type is "+TestConstants.ORDER_TYPE_CONSULTANT+" but actual on UI is "+lsdOrderPage.getOrderTypeAfterClickedOnOrder().trim());
		s_assert.assertTrue(lsdOrderPage.isConsultantMeTxtPresent(), "Consultant me txt is not present at order's page");
		s_assert.assertTrue(lsdOrderPage.isOrderLabelPresent("Order Date"), "Order date label is not present at order's page");
		s_assert.assertTrue(lsdOrderPage.isOrderLabelPresent("Commission Date"), "Commission Date label is not present at order's page");
		s_assert.assertTrue(lsdOrderPage.isOrderLabelPresent("Order Number"), "Order Number label is not present at order's page");
		s_assert.assertTrue(lsdOrderPage.isOrderLabelPresent("SV"), "SV label is not present at order's page");
		s_assert.assertFalse(lsdOrderPage.getOrderDate()==null, "Order date is blank");
		s_assert.assertFalse(lsdOrderPage.getCommisionDate()==null, "Commision date is blank");
		s_assert.assertFalse(lsdOrderPage.getOrderNumber()==null, "Order Number is blank");
		s_assert.assertTrue(lsdOrderPage.getPSQV().contains("0"), "PSQV is not 0");
		s_assert.assertTrue(lsdOrderPage.getOrderStatus().toLowerCase().contains("processed"), "Order status expected is processed but getting "+lsdOrderPage.getOrderStatus());
		//s_assert.assertFalse(lsdOrderPage.getOrderItems()==null, "Order Items are not present");
		//s_assert.assertTrue(lsdOrderPage.getFootNote().contains("Although you receive 0 PSQV"), "PSQV foot note is not present");
		lsdOrderPage.clickCloseIconOfOrder();
		s_assert.assertAll();
	}

	//Main Menu TC-1151
	@Test(priority=3)
	public void testMainMenu_1151(){
		lsdCustomerPage = lsdHomePage.clickCustomersLink();
		s_assert.assertTrue(driver.getCurrentUrl().contains("customers"), "Expected Url should contains customers but actual on UI is: "+driver.getCurrentUrl());
		s_assert.assertTrue(lsdCustomerPage.isCustomerPagePresent(), "Customer page is not present");
		lsdOrderPage = lsdHomePage.clickOrdersLink();
		s_assert.assertTrue(driver.getCurrentUrl().contains("orders"), "Expected Url should contains customers but actual on UI is: "+driver.getCurrentUrl());
		s_assert.assertTrue(lsdOrderPage.isOrdersPagePresent(), "Orders page is not present");
		lsdHomePage.navigateToHomePage();
		s_assert.assertTrue(driver.getCurrentUrl().contains("home"), "Expected Url should contains customers but actual on UI is: "+driver.getCurrentUrl());
		s_assert.assertAll();
	}

	//Feedback Option TC-272
	@Test(priority=4)
	public void testFeedbackoption_272(){
		String parentWindowHandle = driver.getWindowHandle();
		lsdFeedbackPage = lsdHomePage.clickFeedbackLink();
		s_assert.assertTrue(lsdFeedbackPage.isFeedbackPagePresent(parentWindowHandle), "Feedback page is not present");
		s_assert.assertAll();
	}

	//Contact Card - Design and Navigation TC-270
	@Test(priority=5)
	public void testContactCardDesignAndNavigation_270(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickViewMyOrdersLink();
		lsdOrderPage.clickFirstProcessedOrder();
		s_assert.assertTrue(lsdOrderPage.isContactButtonPresentAtFooter(),"Contact button is not present at footer for any processed order");
		s_assert.assertTrue(lsdOrderPage.getContactNameFromContactButton().toLowerCase().trim().contains(TestConstants.FIRST_ORDER_CONTACT_NAME.toLowerCase().trim()), "Expected Contact name is:"+TestConstants.FIRST_ORDER_CONTACT_NAME+" Actual on UI is "+lsdOrderPage.getContactNameFromContactButton());
		lsdOrderPage.clickCloseIconOfOrder();
		lsdOrderPage.clickFirstProcessedPCAutishipOrder();
		s_assert.assertTrue(lsdOrderPage.isContactButtonPresentAtFooter(),"Contact button is not present at footer for PC autoship processed order");
		lsdOrderPage.clickCloseIconOfOrder();
		lsdOrderPage.clickFirstProcessedPCAutishipOrder();
		s_assert.assertTrue(lsdOrderPage.isContactButtonPresentAtFooter(),"Contact button is not present at footer for RC processed order");
		lsdOrderPage.clickContactButtonAtFooter();
		s_assert.assertTrue(lsdOrderPage.isContactDetailsPresent(),"Contact details is not present after clicked on contact button");
		lsdOrderPage.clickCloseIconOfContact();
		lsdOrderPage.clickCloseIconOfOrder();
		lsdHomePage.clickCustomersLink();
		lsdCustomerPage.clickExpandAndMinimizeButtonOfThisMonth();
		lsdCustomerPage.clickFirstProcessedOrderUnderCustomerSection();
		s_assert.assertTrue(lsdCustomerPage.getContactNameFromContactButtonInCustomerPage().toLowerCase().trim().contains(TestConstants.FIRST_ORDER_CONTACT_NAME_UNDER_CUSTOMER_SECTION.toLowerCase().trim()), "Expected Contact name is:"+TestConstants.FIRST_ORDER_CONTACT_NAME_UNDER_CUSTOMER_SECTION+" Actual on UI is "+lsdCustomerPage.getContactNameFromContactButtonInCustomerPage());
		lsdCustomerPage.clickBackArrowIcon();
		s_assert.assertAll();
	}

	//Contact Card button interactions TC-271
	@Test(priority=6)
	public void testContactCardButtonInteractions_271(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickViewMyOrdersLink();
		lsdOrderPage.clickFirstProcessedOrder();
		s_assert.assertTrue(lsdOrderPage.isContactButtonPresentAtFooter(),"Contact button is not present at footer for any processed order");
		lsdOrderPage.clickContactButtonAtFooter();
		s_assert.assertTrue(lsdOrderPage.isPhoneIconPresent(),"Phone icon is not present after clicked on contact button");
		s_assert.assertTrue(lsdOrderPage.isPhoneIconPresent(),"Email icon is not present after clicked on contact button");
		lsdOrderPage.clickCloseIconOfContact();
		lsdOrderPage.clickCloseIconOfOrder();
		s_assert.assertAll();
	}

	//Successful Log/Sign Out TC-273 
	@Test(priority=7)
	public void testSuccessfulLogInSignOut_273(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickLogout();
		s_assert.assertTrue(lsdHomePage.getCurrentURL().toLowerCase().contains("login"), "User is not on logout page, the current url is expected to have 'login',but the currrent URL is: "+lsdHomePage.getCurrentURL());
		lsdLoginPage.enterUsername(whiteListedUserName);
		lsdLoginPage.enterPassword(password);
		lsdLoginPage.clickLoginBtn();
		s_assert.assertTrue(lsdHomePage.getCurrentURL().toLowerCase().contains("home"), "user is not on home page after login,the current url is expected to have 'home',but the current URL is "+lsdHomePage.getCurrentURL());
		s_assert.assertAll();
	}

	//Customers View TC-1149
	@Test(priority=8)
	public void testCustomersView_1149(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickCustomersLink();
		lsdCustomerPage.clickExpandAndMinimizeButtonOfThisMonth();
		s_assert.assertTrue(lsdCustomerPage.isAutoshipOrderSectionPresentAfterExpand(), "Expand button is not working for this month section");
		lsdCustomerPage.clickExpandAndMinimizeButtonOfThisMonth();
		lsdCustomerPage.clickExpandAndMinimizeButtonOfNextMonth();
		s_assert.assertTrue(lsdCustomerPage.isAutoshipOrderSectionPresentAfterExpand(), "Expand button is not working for Next month section");
		lsdCustomerPage.clickExpandAndMinimizeButtonOfNextMonth();
		lsdCustomerPage.clickExpandAndMinimizeButtonOfFurtherOut();
		s_assert.assertTrue(lsdCustomerPage.isAutoshipOrderSectionPresentAfterExpand(), "Expand button is not working for Further Out section");
		lsdCustomerPage.clickExpandAndMinimizeButtonOfFurtherOut();
		lsdCustomerPage.clickExpandAndMinimizeButtonOfThisMonth();
		s_assert.assertTrue(lsdCustomerPage.getCustomerNamePresentInFirstOrder().trim().contains(TestConstants.FIRST_PC_ORDER_NAME), "Expected PC order name is "+TestConstants.FIRST_PC_ORDER_NAME+" but actual on UI is "+lsdCustomerPage.getCustomerNamePresentInFirstOrder().trim());
		s_assert.assertTrue(lsdCustomerPage.getOrderTypeOfFirstOrder().trim().contains(TestConstants.ORDER_TYPE_PC), "Expected order type is "+TestConstants.ORDER_TYPE_PC+" but actual on UI is "+lsdCustomerPage.getOrderTypeOfFirstOrder().trim());
		s_assert.assertTrue(lsdCustomerPage.getOrderStatusOfFirstOrder().trim().toLowerCase().contains(TestConstants.PROCESSED_ORDER_STATUS), "Expected order status is "+TestConstants.PROCESSED_ORDER_STATUS+" but actual on UI is "+lsdCustomerPage.getOrderStatusOfFirstOrder().trim().toLowerCase());
		s_assert.assertFalse(lsdCustomerPage.getOrderDateOfFirstOrder()==null, "Order date is not present in order section");
		s_assert.assertTrue(lsdCustomerPage.isNewLabelPresentForFirstOrder(), "New label is not present for first PC order");
		s_assert.assertTrue(lsdCustomerPage.getClassNameOfFirstProcessedOrder().toLowerCase().contains("green"), "Expected colour of order status 'processed is green' but actual on UI is: "+lsdCustomerPage.getClassNameOfFirstProcessedOrder());
		//s_assert.assertTrue(lsdCustomerPage.getClassNameOfFirstFailedOrder().toLowerCase().contains("red"), "Expected colour of order status 'failed is red' but actual on UI is: "+lsdCustomerPage.getClassNameOfFirstFailedOrder());
		lsdCustomerPage.clickExpandAndMinimizeButtonOfThisMonth();
		lsdCustomerPage.clickExpandAndMinimizeButtonOfNextMonth();
		//s_assert.assertTrue(lsdCustomerPage.getClassNameOfFirstScheduledOrder().toLowerCase().contains("orange"), "Expected colour of order status 'scheduled is orange' but actual on UI is: "+lsdCustomerPage.getClassNameOfFirstScheduledOrder());
		s_assert.assertAll();
	}

	//PC Profile TC-1150
	@Test(priority=9)
	public void testPCProfile_1150(){
		String orderStatus = "Order Status";
		String orderType = "Order Type";
		String orderNumber = "Order Number";
		String QV = "QV";
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickCustomersLink();
		lsdCustomerPage.clickExpandAndMinimizeButtonOfThisMonth();
		lsdCustomerPage.clickFirstProcessedOrderUnderCustomerSection();
		s_assert.assertTrue(lsdCustomerPage.getPCUserNamePresentInOrder().trim().contains(TestConstants.FIRST_PC_ORDER_NAME), "Expected PC order name is "+TestConstants.FIRST_PC_ORDER_NAME+" but actual on UI is "+lsdCustomerPage.getPCUserNamePresentInOrder().trim());
		s_assert.assertTrue(lsdCustomerPage.isNewLabelPresentForFirstOrder(), "New label is not present in first PC order");
		s_assert.assertTrue(lsdCustomerPage.isEnrolledOnTxtPresent(), "Enrolled on is not present at order");
		s_assert.assertTrue(lsdCustomerPage.isNextShipmentTxtPresent(), "Next shipment is not present at order");
		s_assert.assertTrue(lsdCustomerPage.isUpcomingOrderSectionPresent(), "upcoming order section is not present at order");
		s_assert.assertTrue(lsdCustomerPage.isOrderHistorySectionPresent(), "Order history section is not present at order");
		s_assert.assertFalse(lsdCustomerPage.getOrderDateAndTimeStampInUpcomingOrderSection()==null, "Order date and time is not present in order upcoming section");
		s_assert.assertTrue(lsdCustomerPage.isOrderDetailsPresentInOrderUpcomingSection(orderStatus), "Order status is not present in order upcoming section");
		s_assert.assertTrue(lsdCustomerPage.isOrderDetailsPresentInOrderUpcomingSection(orderType), "Order type is not present in order upcoming section");
		s_assert.assertTrue(lsdCustomerPage.isOrderDetailsPresentInOrderUpcomingSection(orderNumber), "Order status is not present in order upcoming section");
		s_assert.assertTrue(lsdCustomerPage.isOrderDetailsPresentInOrderUpcomingSection(QV), "Order status is not present in order upcoming section");
		s_assert.assertTrue(lsdCustomerPage.isOrderItemsPresentInUpcomingOrderSection(), "Order items are not present in upcoming order section");
		//s_assert.assertTrue(lsdCustomerPage.getOrderNote().toLowerCase().contains("note"), "Order not is not present");
		s_assert.assertFalse(lsdCustomerPage.getOrderDateAndTimeStampOfFirstOrderInOrderHistorySection()==null, "Order date and time is not present in order history section of first order");
		s_assert.assertTrue(lsdCustomerPage.isOrderDetailsPresentInOrderHistorySectionOfFirstOrder(orderStatus), "Order status is not present in order history section");
		s_assert.assertTrue(lsdCustomerPage.isOrderDetailsPresentInOrderHistorySectionOfFirstOrder(orderType), "Order type is not present in order history section");
		s_assert.assertTrue(lsdCustomerPage.isOrderDetailsPresentInOrderHistorySectionOfFirstOrder(orderNumber), "Order status is not present in order history section");
		s_assert.assertTrue(lsdCustomerPage.isOrderDetailsPresentInOrderHistorySectionOfFirstOrder(QV), "Order status is not present in order history section");
		s_assert.assertTrue(lsdCustomerPage.isOrderItemsPresentInOrderHistorySectionOfFirstOrder(), "Order items are not present in order history section of first order");
		lsdCustomerPage.clickBackArrowIcon();
		s_assert.assertAll();
	}

	//Filters Functionality TC-264 
	@Test(priority=10)
	public void testFiltersFunctionality_LSD_TC_264(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickOrdersLink();
		lsdOrderPage.clickAddFilters();
		lsdOrderPage.clickConsultantOrderChkBoxInAllOrderTypes();
		lsdOrderPage.clickPreferredCustomerOrderOrderChkBoxInAllOrderTypes();
		lsdOrderPage.clickSetFiltersBtn();
		s_assert.assertTrue(lsdOrderPage.isFilterAppliedSuccessfully("Consultant Orders"), "Consultant order filter is not applied successfully");
		s_assert.assertTrue(lsdOrderPage.isFilterAppliedSuccessfully("Preferred Customer Orders"), "Preferred Customer Orders filter is not applied successfully");
		int totalNoOfOrders = lsdOrderPage.getCountOfTotalOrders();
		for(int i=1; i<=totalNoOfOrders; i++){
			//s_assert.assertTrue(lsdOrderPage.getOrderStatus(i).toLowerCase().contains("processed"), "Expected order Status is 'Processed' but actual on UI for order number:"+i+" is: "+lsdOrderPage.getOrderStatus(i));
			s_assert.assertTrue(lsdOrderPage.getOrderType(i).toLowerCase().contains("crp")|| lsdOrderPage.getOrderType(i).toLowerCase().contains("pc perks") || lsdOrderPage.getOrderType(i).toLowerCase().contains("consultant order") , "Expected order type is 'CRP' or 'Pulse Subscription' or 'consultant order' but actual on UI for order number:"+i+" is: "+lsdOrderPage.getOrderType(i));
		}
		lsdOrderPage.clickAddFilters();
		s_assert.assertTrue(lsdOrderPage.isConsultantOrdersCheckBoxIsChecked(), "Consultant order filter is not checked");
		s_assert.assertTrue(lsdOrderPage.isPreferredCustomerOrdersCheckBoxIsChecked(), "Preferred Customer Orders filter is not checked");
		lsdOrderPage.clickCloseIconOfFilter();
		lsdOrderPage.clickCloseIconOfFilter("Preferred Customer Orders");
		totalNoOfOrders = lsdOrderPage.getCountOfTotalOrders();
		for(int i=1; i<=totalNoOfOrders; i++){
			//s_assert.assertTrue(lsdOrderPage.getOrderStatus(i).toLowerCase().contains("processed"), "Expected order Status is 'Processed' but actual on UI for order number:"+i+" is: "+lsdOrderPage.getOrderStatus(i));
			s_assert.assertFalse(lsdOrderPage.getOrderType(i).toLowerCase().contains("pc perks"), "Expected order type is 'CRP' or 'consultant order' but actual on UI for order number:"+i+" is: "+lsdOrderPage.getOrderType(i));
		}
		lsdOrderPage.clickCloseIconOfFilter("Consultant Orders");
		s_assert.assertFalse(lsdOrderPage.isFilterAppliedSuccessfully("Consultant Orders"), "Consultant order filter is not removed successfully");
		s_assert.assertFalse(lsdOrderPage.isFilterAppliedSuccessfully("Preferred Customer Orders"), "Processed Orders filter is not removed successfully");
		s_assert.assertAll();
	}

	//Order Details - Design and data fields layout TC-1157 
	@Test(priority=11)
	public void testOrderDetailsDesignAndDataFieldsLayout_LSD_TC_1157(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickOrdersLink();
		lsdOrderPage.clickFirstProcessedOrder();
		lsdOrderPage.clickViewDetailsBtn();
		s_assert.assertTrue(lsdOrderPage.getOrderNamePresentInViewOrderDetails().toLowerCase().trim().contains(TestConstants.FIRST_ORDER_NAME.toLowerCase().trim()), "Expected Order Username is "+TestConstants.FIRST_ORDER_NAME+" but actual on UI is "+lsdOrderPage.getOrderNamePresentInViewOrderDetails().trim());
		s_assert.assertTrue(lsdOrderPage.getOrderTypePresentInViewOrderDetails().trim().contains(TestConstants.ORDER_TYPE_CONSULTANT), "Expected Order type is "+TestConstants.ORDER_TYPE_CONSULTANT+" but actual on UI is "+lsdOrderPage.getOrderTypePresentInViewOrderDetails().trim());
		s_assert.assertTrue(lsdOrderPage.isOrderDetailsHeaderPresent("Order details"), "Order details header is not present");
		//s_assert.assertFalse(lsdOrderPage.getOrderNamePresentInViewOrderDetails()==null, "Order name is blank");
		//s_assert.assertFalse(lsdOrderPage.getOrderTypePresentInViewOrderDetails()==null, "Order type is blank");
		s_assert.assertFalse(lsdOrderPage.getEnrolledDatePresentInViewOrderDetails()==null, "Enrolled date is blank");
		s_assert.assertTrue(lsdOrderPage.isOrderHeaderPresent("Overview"), "Overview header is not present");
		s_assert.assertTrue(lsdOrderPage.isOverviewDetailsPresent("Order Date"), "Order Date is not present in overview section");
		s_assert.assertTrue(lsdOrderPage.isOverviewDetailsPresent("Commission Date"), "Commission Date is not present in overview section");
		s_assert.assertTrue(lsdOrderPage.isOverviewDetailsPresent("Order Number"), "Order number is not present in overview section");
		s_assert.assertTrue(lsdOrderPage.getSVValueFromViewOrderDetails().trim().contains(TestConstants.FIRST_ORDER_SV_VALUE), "Expected SV value is "+TestConstants.FIRST_ORDER_SV_VALUE+" but actual on UI is "+lsdOrderPage.getSVValueFromViewOrderDetails().trim());
		s_assert.assertTrue(lsdOrderPage.isOverviewDetailsPresent("Total"), "total is not present in overview section");
		s_assert.assertTrue(lsdOrderPage.isOrderHeaderPresent("Shipment details"), "Shipment details header is not present");
		s_assert.assertTrue(lsdOrderPage.isShipmentDetailsPresent("Order Status"), "Order Status is not present in shipment details section");
		//s_assert.assertTrue(lsdOrderPage.isShipmentDetailsPresent("Tracking"), "Tracking link is not present in shipment details section");
		s_assert.assertTrue(lsdOrderPage.isOrderItemsPresent(), "Order is not contain any item");
		s_assert.assertTrue(lsdOrderPage.isSKUValuePresentUnderOrderItems(), "SKU value is not present under item");
		//s_assert.assertTrue(lsdOrderPage.isTotalPricePresentUnderOrderItems(), "Total price is not present under item");
		s_assert.assertTrue(lsdOrderPage.getTotalPricePresentUnderOrderItems().trim().contains(TestConstants.TOTAL_PRICE), "Expected total price is "+TestConstants.TOTAL_PRICE+" but actual on UI is "+lsdOrderPage.getTotalPricePresentUnderOrderItems().trim());
		//s_assert.assertTrue(lsdOrderPage.isQuantityPresentUnderOrderItems(), "Quantity is not present under item");
		s_assert.assertTrue(lsdOrderPage.getQuantityPresentUnderOrderItems().trim().contains(TestConstants.QUANTITY_OF_PRODUCT), "Expected product quantity is "+TestConstants.QUANTITY_OF_PRODUCT+" but actual on UI is "+lsdOrderPage.getQuantityPresentUnderOrderItems().trim());
		//s_assert.assertTrue(lsdOrderPage.isTrackOrderBtnPresent(), "Track order btn is not present under item");
		s_assert.assertTrue(lsdOrderPage.isOrderHeaderPresent("Shipping details"), "Shipping details header is not present");
		s_assert.assertTrue(lsdOrderPage.isShippingDetailsSubHeadingPresent(), "Shipping details subheading is not present");
		s_assert.assertTrue(lsdOrderPage.getShippingAddressName().toLowerCase().trim().contains(TestConstants.FIRST_ORDER_NAME.toLowerCase().trim()), "Expected Order Username is "+TestConstants.FIRST_ORDER_NAME+" but actual on UI is "+lsdOrderPage.getShippingAddressName().trim());
		s_assert.assertTrue(lsdOrderPage.isShippingMethodPresent(), "Shipping details subheading is not present");
		s_assert.assertTrue(lsdOrderPage.getShippingMethodNameFromViewOrderDetails().toLowerCase().trim().contains(TestConstants.SHIPPING_METHOD.toLowerCase().trim()), "Expected shipping method is "+TestConstants.SHIPPING_METHOD+" but actual on UI is "+lsdOrderPage.getShippingMethodNameFromViewOrderDetails().trim());
		s_assert.assertTrue(lsdOrderPage.isOrderHeaderPresent("Billing details"), "Billing details header is not present");
		s_assert.assertTrue(lsdOrderPage.isBillingDetailsSubHeadingPresent(), "Billing details subheading is not present");
		s_assert.assertTrue(lsdOrderPage.getBillingProfileName().toLowerCase().trim().contains(TestConstants.FIRST_ORDER_NAME.toLowerCase().trim()), "Expected Billing name is "+TestConstants.FIRST_ORDER_NAME+" but actual on UI is "+lsdOrderPage.getBillingProfileName().trim());
		lsdOrderPage.clickBackArrowIconOfViewDetails();
		lsdOrderPage.clickCloseIconOfOrder();
		s_assert.assertAll();
	}

	//Orders View - Design and data fields layout TC-1155
	@Test(priority=12)
	public void testOrdersViewDesignAndDataFieldsLayout_LSD_TC_1155(){
		String countOfPendingOrders = TestConstants.COUNT_OF_PENDING_ORDERS;
		String countOfProcessedOrders = TestConstants.COUNT_OF_PROCESSED_ORDERS;
		String countOfFailedOrders = TestConstants.COUNT_OF_FAILED_ORDERS;
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickOrdersLink();
		String date = lsdHomePage.getCurrentPSTDate();
		String currentDate[] = date.split("\\ ");
		String monthWithYear = currentDate[1].split("\\,")[0]+" "+currentDate[2];
		s_assert.assertTrue(lsdOrderPage.getCommissionTimePeriod().toLowerCase().contains(monthWithYear.toLowerCase()),"Expected commission time period is "+monthWithYear+"But actual on UI is "+lsdOrderPage.getCommissionTimePeriod().toLowerCase());
		s_assert.assertTrue(lsdOrderPage.isReturnedOrderPresent(), "Returned order is not present");
		s_assert.assertTrue(lsdOrderPage.getOrderMetricsCount("Pending").equalsIgnoreCase(countOfPendingOrders), "Expected pending order's count is: "+countOfPendingOrders+" but actual on UI is: "+lsdOrderPage.getOrderMetricsCount("Pending"));
		s_assert.assertTrue(lsdOrderPage.getOrderMetricsCount("Processed").equalsIgnoreCase(countOfProcessedOrders), "Expected Processed order's count is: "+countOfProcessedOrders+" but actual on UI is: "+lsdOrderPage.getOrderMetricsCount("Processed"));
		s_assert.assertTrue(lsdOrderPage.getOrderMetricsCount("Failed").equalsIgnoreCase(countOfFailedOrders), "Expected Failed order's count is: "+countOfFailedOrders+" but actual on UI is: "+lsdOrderPage.getOrderMetricsCount("Failed"));
		s_assert.assertTrue(lsdOrderPage.getFirstOrderUserName().trim().contains(TestConstants.FIRST_ORDER_NAME), "Expected Order Username is "+TestConstants.FIRST_ORDER_NAME+" but actual on UI is "+lsdOrderPage.getFirstOrderUserName().trim());
		s_assert.assertTrue(lsdOrderPage.getFirstOrderStatus().trim().toLowerCase().contains(TestConstants.PROCESSED_ORDER_STATUS), "Expected Order status is "+TestConstants.PROCESSED_ORDER_STATUS+" but actual on UI is "+lsdOrderPage.getFirstOrderStatus().trim());
		s_assert.assertTrue(lsdOrderPage.getFirstOrderType().trim().contains(TestConstants.ORDER_TYPE_CONSULTANT), "Expected Order type is "+TestConstants.ORDER_TYPE_CONSULTANT+" but actual on UI is "+lsdOrderPage.getFirstOrderType().trim());
		s_assert.assertTrue(lsdOrderPage.getFirstOrderPSQVValue().trim().contains(TestConstants.FIRST_ORDER_SV_VALUE), "Expected SV value is "+TestConstants.FIRST_ORDER_SV_VALUE+" but actual on UI is "+lsdOrderPage.getFirstOrderPSQVValue().trim());
		s_assert.assertTrue(lsdOrderPage.isFirstOrderDatePresent(), "First order date is not present");
		s_assert.assertAll();
	}

	//Navigation of tracking link (Order Summary/Details) TC-1117
	@Test(enabled=false)
	public void testNavigationOfTrackingLink_LSD_TC_1117(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickOrdersLink();
		lsdOrderPage.clickFirstProcessedPCAutishipOrder();
		String parentWindowHandle = driver.getWindowHandle();
		String trackingNumber = lsdOrderPage.clickAndGetTrackingNumberLink();
		s_assert.assertTrue(lsdOrderPage.isTrackingWebsitePagePresent(parentWindowHandle, trackingNumber), "Tracking number link is not working");
		s_assert.assertAll();
	}

}
