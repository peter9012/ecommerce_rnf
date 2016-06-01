package com.rf.test.website.LSD;

import org.testng.annotations.Test;
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
	public void testOrderSummary(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickViewMyOrdersLink();
		lsdOrderPage.clickFirstProcessedOrder();
		s_assert.assertFalse(lsdOrderPage.getOrderDate()==null, "Order date is blank");
		s_assert.assertFalse(lsdOrderPage.getCommisionDate()==null, "Commision date is blank");
		s_assert.assertFalse(lsdOrderPage.getOrderNumber()==null, "Order Number is blank");
		s_assert.assertTrue(lsdOrderPage.getPSQV().contains("0"), "PSQV is not 0");
		s_assert.assertTrue(lsdOrderPage.getOrderStatus().toLowerCase().contains("processed"), "Order status expected is processed but getting "+lsdOrderPage.getOrderStatus());
		s_assert.assertFalse(lsdOrderPage.getOrderItems()==null, "Order Items are not present");
		s_assert.assertTrue(lsdOrderPage.getFootNote().contains("Although you receive 0 PSQV"), "PSQV foot note is not present");
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
		s_assert.assertTrue(lsdCustomerPage.isAutoshipOrderSectionPresentAfterExpand(), "Expand button is not working for this month section");
		lsdCustomerPage.clickExpandAndMinimizeButtonOfNextMonth();
		lsdCustomerPage.clickExpandAndMinimizeButtonOfFurtherOut();
		s_assert.assertTrue(lsdCustomerPage.isAutoshipOrderSectionPresentAfterExpand(), "Expand button is not working for this month section");
		lsdCustomerPage.clickExpandAndMinimizeButtonOfFurtherOut();
		lsdCustomerPage.clickExpandAndMinimizeButtonOfThisMonth();
		s_assert.assertFalse(lsdCustomerPage.getCustomerNamePresentInFirstOrder()==null, "Username is not present in order section");
		s_assert.assertFalse(lsdCustomerPage.getOrderTypeOfFirstOrder()==null, "Order type is not present in order section");
		s_assert.assertFalse(lsdCustomerPage.getOrderStatusOfFirstOrder()==null, "Order status is not present in order section");
		s_assert.assertFalse(lsdCustomerPage.getOrderDateOfFirstOrder()==null, "Order date is not present in order section");
		s_assert.assertTrue(lsdCustomerPage.getClassNameOfFirstProcessedOrder().toLowerCase().contains("green"), "Expected colour of order status 'processed is green' but actual on UI is: "+lsdCustomerPage.getClassNameOfFirstProcessedOrder());
		s_assert.assertTrue(lsdCustomerPage.getClassNameOfFirstFailedOrder().toLowerCase().contains("red"), "Expected colour of order status 'failed is red' but actual on UI is: "+lsdCustomerPage.getClassNameOfFirstFailedOrder());
		lsdCustomerPage.clickExpandAndMinimizeButtonOfThisMonth();
		lsdCustomerPage.clickExpandAndMinimizeButtonOfNextMonth();
		s_assert.assertTrue(lsdCustomerPage.getClassNameOfFirstScheduledOrder().toLowerCase().contains("orange"), "Expected colour of order status 'scheduled is orange' but actual on UI is: "+lsdCustomerPage.getClassNameOfFirstScheduledOrder());
		s_assert.assertAll();
	}

	//PC Profile TC-1150
	@Test
	public void testPCProfile_1150(){
		String orderStatus = "Order Status";
		String orderType = "Order Type";
		String orderNumber = "Order Number";
		String QV = "QV";
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickCustomersLink();
		lsdCustomerPage.clickExpandAndMinimizeButtonOfThisMonth();
		lsdCustomerPage.clickFirstProcessedOrderUnderCustomerSection();
		s_assert.assertFalse(lsdCustomerPage.getPCUserNamePresentInOrder()==null, "PC Username is not present in order section");
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
		s_assert.assertTrue(lsdCustomerPage.getOrderNote().toLowerCase().contains("note"), "Order not is not present");
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
	@Test
	public void testFiltersFunctionality_LSD_TC_264(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickOrdersLink();
		lsdOrderPage.clickAddFilters();
		lsdOrderPage.clickConsultantOrderChkBoxInAllOrderTypes();
		lsdOrderPage.clickProcessedOrderChkBoxInOrderStatus();
		lsdOrderPage.clickSetFiltersBtn();
		s_assert.assertTrue(lsdOrderPage.isFilterAppliedSuccessfully("Consultant Orders"), "Consultant order filter is not applied successfully");
		s_assert.assertTrue(lsdOrderPage.isFilterAppliedSuccessfully("Processed Orders"), "Processed Orders filter is not applied successfully");
		int totalNoOfOrders = lsdOrderPage.getCountOfTotalOrders();
		for(int i=1; i<=totalNoOfOrders; i++){
			s_assert.assertTrue(lsdOrderPage.getOrderStatus(i).toLowerCase().contains("processed"), "Expected order Status is 'Processed' but actual on UI for order number:"+i+" is: "+lsdOrderPage.getOrderStatus(i));
			s_assert.assertTrue(lsdOrderPage.getOrderType(i).toLowerCase().contains("crp")|| lsdOrderPage.getOrderType(i).toLowerCase().contains("pulse subscription") || lsdOrderPage.getOrderType(i).toLowerCase().contains("consultant order") , "Expected order type is 'CRP' or 'Pulse Subscription' or 'consultant order' but actual on UI for order number:"+i+" is: "+lsdOrderPage.getOrderType(i));
		}
		lsdOrderPage.clickAddFilters();
		s_assert.assertTrue(lsdOrderPage.isConsultantOrdersCheckBoxIsChecked(), "Consultant order filter is not checked");
		s_assert.assertTrue(lsdOrderPage.isProcessedOrdersCheckBoxIsChecked(), "Processed order filter is not checked");
		lsdOrderPage.clickCloseIconOfFilter();
		lsdOrderPage.clickCloseIconOfFilter("Consultant Orders");
		lsdOrderPage.clickCloseIconOfFilter("Processed Orders");
		s_assert.assertFalse(lsdOrderPage.isFilterAppliedSuccessfully("Consultant Orders"), "Consultant order filter is not removed successfully");
		s_assert.assertFalse(lsdOrderPage.isFilterAppliedSuccessfully("Processed Orders"), "Processed Orders filter is not removed successfully");
		s_assert.assertTrue(lsdOrderPage.isOrderTypePresentInOrders("Failed"), "Filters are removed successfully");
		s_assert.assertAll();
	}

	//Navigation of tracking link (Order Summary/Details) TC-1117
	@Test
	public void testNavigationOfTrackingLink_LSD_TC_1117(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickOrdersLink();
		lsdOrderPage.clickFirstProcessedPCAutishipOrder();
		String parentWindowHandle = driver.getWindowHandle();
		String trackingNumber = lsdOrderPage.clickAndGetTrackingNumberLink();
		s_assert.assertTrue(lsdOrderPage.isTrackingWebsitePagePresent(parentWindowHandle, trackingNumber), "Tracking number link is not working");
		s_assert.assertAll();
	}

	//Order Details - Design and data fields layout TC-1157 
	@Test
	public void testOrderDetailsDesignAndDataFieldsLayout_LSD_TC_1157(){
		lsdHomePage.navigateToHomePage();
		lsdHomePage.clickOrdersLink();
		lsdOrderPage.clickFirstProcessedPCAutishipOrder();
		lsdOrderPage.clickViewDetailsBtn();
		s_assert.assertTrue(lsdOrderPage.isOrderDetailsHeaderPresent("Order details"), "Order details header is not present");
		s_assert.assertFalse(lsdOrderPage.getOrderNamePresentInViewOrderDetails()==null, "Order name is blank");
		s_assert.assertFalse(lsdOrderPage.getOrderTypePresentInViewOrderDetails()==null, "Order type is blank");
		s_assert.assertFalse(lsdOrderPage.getEnrolledDatePresentInViewOrderDetails()==null, "Enrolled date is blank");

		s_assert.assertTrue(lsdOrderPage.isOrderHeaderPresent("Overview"), "Overview header is not present");
		s_assert.assertTrue(lsdOrderPage.isOverviewDetailsPresent("Order Date"), "Order Date is not present in overview section");
		s_assert.assertTrue(lsdOrderPage.isOverviewDetailsPresent("Commission Date"), "Commission Date is not present in overview section");
		s_assert.assertTrue(lsdOrderPage.isOverviewDetailsPresent("Order Number"), "Order number is not present in overview section");
		s_assert.assertTrue(lsdOrderPage.isOverviewDetailsPresent("PSQV"), "PSQV is not present in overview section");
		s_assert.assertTrue(lsdOrderPage.isOverviewDetailsPresent("Total"), "total is not present in overview section");

		s_assert.assertTrue(lsdOrderPage.isOrderHeaderPresent("Shipment details"), "Shipment details header is not present");
		s_assert.assertTrue(lsdOrderPage.isShipmentDetailsPresent("Order Status"), "Order Status is not present in shipment details section");
		s_assert.assertTrue(lsdOrderPage.isShipmentDetailsPresent("Tracking"), "Tracking link is not present in shipment details section");

		s_assert.assertTrue(lsdOrderPage.isOrderItemsPresent(), "Order is not contain any item");
		s_assert.assertTrue(lsdOrderPage.isSKUValuePresentUnderOrderItems(), "SKU value is not present under item");
		s_assert.assertTrue(lsdOrderPage.isTotalPricePresentUnderOrderItems(), "Total price is not present under item");
		s_assert.assertTrue(lsdOrderPage.isQuantityPresentUnderOrderItems(), "Quantity is not present under item");

		s_assert.assertTrue(lsdOrderPage.isTrackOrderBtnPresent(), "Track order btn is not present under item");

		s_assert.assertTrue(lsdOrderPage.isOrderHeaderPresent("Shipping details"), "Shipping details header is not present");
		s_assert.assertTrue(lsdOrderPage.isShippingDetailsSubHeadingPresent(), "Shipping details subheading is not present");
		s_assert.assertFalse(lsdOrderPage.getShippingAddressName()==null, "Shipping address is blank");
		s_assert.assertTrue(lsdOrderPage.isShippingMethodPresent(), "Shipping details subheading is not present");

		s_assert.assertTrue(lsdOrderPage.isOrderHeaderPresent("Billing details"), "Billing details header is not present");
		s_assert.assertTrue(lsdOrderPage.isBillingDetailsSubHeadingPresent(), "Billing details subheading is not present");
		s_assert.assertFalse(lsdOrderPage.getBillingProfileName()==null, "Billing address is blank");
		s_assert.assertAll();
	}
}
