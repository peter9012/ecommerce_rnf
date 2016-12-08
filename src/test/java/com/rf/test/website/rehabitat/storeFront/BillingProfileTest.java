package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class BillingProfileTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-385 Add New Billing Address on Existing Billing Profile
	 * 
	 * Description : This test will add a new billing address to existing billing profile 
	 * on billing info page.
	 * 
	 *     
	 */
	@Test//Incomplete billing profile is not present on billing info page
	public void testAddNewBillingAddressToExistingBillingProfile_385(){
		String currentURL = null;
		String urlToAssert = "payment-details";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		//Click Edit Of randomely choosen Billing Profile and click add new address.
		//sfBillingInfoPage.clicke
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-386 Add Billing Profile With New Billing Address
	 * 
	 * Description : This test will add a new billing address to newly created billing profile 
	 * on billing info page.
	 * 
	 *     
	 */
	@Test//Incomplete Newly added billing profile is not visible on billing info page.
	public void testAddNewBillingAddressToNewlyCreatedBillingProfile_386(){
		String currentURL = null;
		String urlToAssert = "payment-details";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		//Click Edit Of randomely choosen Billing Profile and click add new address.
		//sfBillingInfoPage.clicke
		s_assert.assertAll();
	}
}