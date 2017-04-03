package com.rf.test.website.pulse;

import org.testng.annotations.Test;

public class UserLoginTest extends PulseWebsiteBaseTest{

	@Test(dataProvider="rfTestData")
	public void testUserLogin_US(String username,String fname, String lname, String account, String country){
		pulseHomePage=pulseLoginPage.enterCredentailsAndLogin(username, password);
		s_assert.assertTrue(pulseHomePage.isHomePagePresent(), "Login Failed for username "+username);
		s_assert.assertAll();		
	}
	
	@Test(dataProvider="rfTestData")
	public void testUserLogin_CA(String username,String fname, String lname, String account, String country){
		pulseHomePage=pulseLoginPage.enterCredentailsAndLogin(username, password);
		s_assert.assertTrue(pulseHomePage.isHomePagePresent(), "Login Failed for username "+username);
		s_assert.assertAll();		
	}
}