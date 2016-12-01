package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class UnsuccessfulUserLoginTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-29 Login with unregistered username or password
	 * 
	 * Description : This test validates that the user with unregistered user name and 
	 * valid password should not be able to login and System should be redirect to the 
	 * login/create an account page with an error- " Your username or password was incorrect."
	 * 				
	 */
	@Test
	public void testLoginUnregisteredUsernamepassword_29(){
		String unRegisteredUsername = "invalidUser1008@rf.com";
		sfHomePage.loginToStoreFront(unRegisteredUsername, password);
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertTrue(sfHomePage.isErrorMessageForIncorrectUsernamePasswordDisplayed(), "Error ");
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-30 Login with incorrect username and valid password
	 * 
	 * Description : This test validates that the user with incorrect user name and 
	 * valid password should not be able to login and System should be redirect to the 
	 * login/create an account page with an error- " Your username or password was incorrect."
	 * 				
	 */
	@Test
	public void testUnsuccessfulUserLogin_30(){
		String incorrectUsername = "invalidUser1008@rf.com";
		sfHomePage.loginToStoreFront(incorrectUsername, password);
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertTrue(sfHomePage.isErrorMessageForIncorrectUsernamePasswordDisplayed(), "Error ");
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-31 Login with valid username or invalid password
	 * 
	 * Description : This test validates that the user with valid user name and 
	 * invalid password should not be able to login and System should be redirect to the 
	 * login/create an account page with an error- " Your username or password was incorrect."
	 * 				
	 */
	@Test
	public void testUnsuccessfulUserLogin_31(){
		String validUsername = "validUser1008@rf.com";
		String incorrectpassword = "1234567890";
		sfHomePage.loginToStoreFront(validUsername, incorrectpassword);
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertTrue(sfHomePage.isErrorMessageForIncorrectUsernamePasswordDisplayed(), "Error ");
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-32 Enter invalid username and invalid password
	 * 
	 * Description : This test validates that the user with invalid user name and 
	 * invalid password should not be able to login and System should be redirect to the 
	 * login/create an account page with an error- " Your username or password was incorrect."
	 * 				
	 */
	@Test
	public void testUnsuccessfulUserLogin_32(){
		String invalidUsername = "invalidUser1008@rf.com";
		String incorrectpassword = "1234567890";
		sfHomePage.loginToStoreFront(invalidUsername, incorrectpassword);
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertTrue(sfHomePage.isErrorMessageForIncorrectUsernamePasswordDisplayed(), "Error ");
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-33 Login with incorrect username or password on login/Create an accout page
	 * 
	 * Description : This test validates that the user with incorrect user name or 
	 * invalid password should not be able to login and System should be redirect to the 
	 * login/create an account page with an error- " Your username or password was incorrect."
	 * 				
	 */
	@Test
	public void testUnsuccessfulUserLogin_33(){
		String invalidUsername = "invalidUser1008@rf.com";
		sfHomePage.loginToStoreFront(invalidUsername, password);
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertTrue(sfHomePage.isErrorMessageForIncorrectUsernamePasswordDisplayed(), "Error ");
		sfHomePage.loginToStoreFront(invalidUsername, password);
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertTrue(sfHomePage.isErrorMessageForIncorrectUsernamePasswordDisplayed(), "Error ");
		s_assert.assertAll();
	}	
	
	/***
	 * qTest : TC-34 Login with unregistered username or password on login/Create an accout page
	 * 
	 * Description : This test validates that the user with incorrect user name or 
	 * invalid password should not be able to login and System should be redirect to the 
	 * login/create an account page with an error- " Your username or password was incorrect."
	 * 				
	 */
	@Test
	public void testUnsuccessfulUserLogin_34(){
		String invalidUsername = "invalidUser1008@rf.com";
		sfHomePage.loginToStoreFront(invalidUsername, password);
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertTrue(sfHomePage.isErrorMessageForIncorrectUsernamePasswordDisplayed(), "Error ");
		sfHomePage.loginToStoreFront(invalidUsername, password);
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertTrue(sfHomePage.isErrorMessageForIncorrectUsernamePasswordDisplayed(), "Error ");
		s_assert.assertAll();
	}	
	
	
	
}