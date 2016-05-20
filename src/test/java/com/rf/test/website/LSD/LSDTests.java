package com.rf.test.website.LSD;

import org.jsoup.select.Evaluator.ContainsOwnText;
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
}
