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
	@Test(enabled=false)
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
}
