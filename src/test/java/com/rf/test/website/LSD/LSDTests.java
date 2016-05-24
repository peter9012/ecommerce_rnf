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
		lsdHomePage.navigaeToHomePage();
		lsdHomePage.clickViewMyOrdersLink();
		lsdOrderPage.clickFirstProcessedOrder();
		s_assert.assertFalse(lsdOrderPage.getOrderDate()==null, "Order date is blank");
		s_assert.assertFalse(lsdOrderPage.getCommisionDate()==null, "Commision date is blank");
		s_assert.assertFalse(lsdOrderPage.getOrderNumber()==null, "Order Number is blank");
		s_assert.assertTrue(lsdOrderPage.getPSQV().contains("0"), "PSQV is not 0");
		s_assert.assertTrue(lsdOrderPage.getOrderStatus().toLowerCase().contains("processed"), "Order status expected is processed but getting "+lsdOrderPage.getOrderStatus());
		s_assert.assertFalse(lsdOrderPage.getOrderItems()==null, "Order Items are not present");
		s_assert.assertTrue(lsdOrderPage.getFootNote().contains("Although you receive 0 PSQV"), "PSQV foot note is not present");
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
		lsdHomePage.navigaeToHomePage();
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
}
