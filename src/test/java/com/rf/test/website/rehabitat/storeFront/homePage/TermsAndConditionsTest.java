package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class TermsAndConditionsTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-20 Terms and conditions link in footer
	 * Description : This test verify terms and conditions footer links on Homepage.
	 * 
	 *     
	 */
	@Test(enabled=true) 
	public void testVerifyTermsAndConditionsLinkInFooter_20(){
		String termsAndConditions = "Terms";
		String currentURL = null;
		String termsAndConditionURL = "terms-conditions";
		//Verify and validate terms and conditions link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(termsAndConditions),"terms and conditions link is not present in footer section");
		sfHomePage.clickFooterLink(termsAndConditions);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(termsAndConditionURL), "Expected URL should contain "+termsAndConditionURL+" but actual on UI is"+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-21 Verify Privacy Policy link
	 * 
	 * Description : This test verify Privacy policy link from terms and conditions Page.	 
	 */
	@Test(enabled=true) 
	public void testVerifyPrivacyPolicyLinkFromTermsAndConditionsPage_21(){
		String termsAndConditions = "Terms";
		String PrivacyPolicy = "Privacy Policy";
		String currentURL = null;
		String termsAndConditionURL = "terms-conditions";
		String privacyPolicyURL = "privacy-policy";
		//Verify and validate terms and conditions link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(termsAndConditions),"terms and conditions link is not present in footer section");
		sfHomePage.clickFooterLink(termsAndConditions);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(termsAndConditionURL), "Expected URL should contain "+termsAndConditionURL+" but actual on UI is"+currentURL);
		//Verify Privacy policy link from terms and conditions page.
		sfHomePage.clickFooterLink(PrivacyPolicy);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(privacyPolicyURL), "Expected URL should contain "+privacyPolicyURL+" but actual on UI is"+currentURL);
		s_assert.assertAll();

	}
}