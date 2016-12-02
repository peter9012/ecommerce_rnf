package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class MyAccountTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-178 Account Information Page
	 * 
	 * Description : This test validates the account info page for consultant,PC and RC user.
	 * 
	 *     
	 */
	@Test//Incomplete for PC and RC user
	public void testVerifyAccountInfoPageForUsers_178(){
		String currentURL = null;
		String urlToAssert = "my-account";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		//Verify details of account info page for consultant.
		s_assert.assertTrue(sfAccountInfoPage.isFirstNameFieldPresent(),"First Name field not present on account Info page");
		s_assert.assertTrue(sfAccountInfoPage.isLastNameFieldPresent(),"Last Name field not present on account Info page");
		s_assert.assertTrue(sfAccountInfoPage.isAddressFieldPresent(),"Address line field not present on account Info page");
		s_assert.assertTrue(sfAccountInfoPage.isCityFieldPresent()," City field not present on account Info page");
		s_assert.assertTrue(sfAccountInfoPage.isPostalFieldPresent(),"Postal field not present on account Info page");
		s_assert.assertTrue(sfAccountInfoPage.isMainPhoneNumberFieldPresent(),"Main phone number field not present on account Info page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-179 Account Information- Update username
	 * 
	 * Description : This test Updates username on account info page for consultant,PC and RC user.
	 * 
	 *     
	 */
	@Test//Incomplete for PC and RC user and also unable to update for consultant.
	public void testUpdateUsernameOnAccountInfoPageForUsers_179(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String currentURL = null;
		String urlToAssert = "my-account";
		String updatedUserName = "updatedUserName"+randomNum;
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME,password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage = sfHomePage.navigateToAccountInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		//Update and verify username on account info page for consultant.
		sfAccountInfoPage.enterUserName(updatedUserName);
		sfAccountInfoPage.saveAccountInfo();
		s_assert.assertAll();
	}
}