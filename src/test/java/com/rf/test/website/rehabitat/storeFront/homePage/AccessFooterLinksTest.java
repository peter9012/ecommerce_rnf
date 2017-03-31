package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AccessFooterLinksTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-13 Verify the footer links of the site
	 * Description : This test verify the presence footer links on Homepage.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyFooterLinksOfSite_13(){
		String homePageURL = null;
		String disclaimer = "Disclaimer";
		String satisfactionGuarantee = "Satisfaction Guarantee";
		String contactUS = "Contact Us";
		String careers = "Careers";
		String pressRoom = "Press Room";
		//String directSellingAssociation= "Direct Selling Association";
		String termsAndConditions = "Terms";
		String PrivacyPolicy = "Privacy Policy";
		String prefix = pwsPrefix();
		homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		//Verify disclaimer link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(disclaimer),"Disclaimer link is not present in footer section");
		//Verify satisfaction guarantee link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(satisfactionGuarantee),"Satisfaction guarantee link is not present in footer section");
		//Verify contact us link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(contactUS),"Contact us link is not present in footer section");
		//Verify careers link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(careers),"careers link is not present in footer section");
		//Verify pressroom link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(pressRoom),"pressroom link is not present in footer section");
		//Verify direct selling association link in footer section.
		//s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(directSellingAssociation),"Direct selling association link is not present in footer section");
		//Verify terms and conditions link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(termsAndConditions),"terms and conditions link is not present in footer section");
		//Verify Privacy policy link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(PrivacyPolicy),"Privacy policy link is not present in footer section");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-14 User selects a link from the footer that links to an internal content page
	 * Description : This test verify and validates the footer links on Homepage that open in same window.
	 * 
	 *     
	 */
	@Test(enabled=true) 
	public void testVerifyFooterLinksOfSiteOpensInSameWindow_14(){
		String homePageURL = null;
		String currentURL = null;
		String disclaimer = "Disclaimer";
		String satisfactionGuarantee = "Satisfaction Guarantee";
		String contactUS = "Contact Us";
		String careers = "Careers";
		String pressRoom = "Press";
		//String directSellingAssociation= "Direct Selling Association";
		String termsAndConditions = "Terms";
		String PrivacyPolicy = "Privacy Policy";
		String disclaimerURL = "disclaimer";
		String satisfactionGuaranteeURL = "satisfaction-guarantee";
		String contactUsURL = "contact";
		String careersURL = "careers";
		String pressroomURL = "press";
		String directSellingAssociationURL = "who-we-are";
		String termsAndConditionURL = "terms-conditions";
		String privacyPolicyURL = "privacy-policy";
		String prefix = pwsPrefix();
		homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		//Verify and validate disclaimer link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(disclaimer),"Disclaimer link is not present in footer section");
		sfHomePage.clickFooterLink(disclaimer);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(disclaimerURL), "Expected URL should contain "+disclaimerURL+" but actual on UI is"+currentURL);
		//Verify and validate satisfaction guarantee link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(satisfactionGuarantee),"Satisfaction guarantee link is not present in footer section");
		sfHomePage.clickFooterLink(satisfactionGuarantee);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(satisfactionGuaranteeURL), "Expected URL should contain "+satisfactionGuaranteeURL+" but actual on UI is"+currentURL);
		//Verify and validate contact us link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(contactUS),"Contact us link is not present in footer section");
		sfHomePage.clickFooterLink(contactUS);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(contactUsURL), "Expected URL should contain "+contactUsURL+" but actual on UI is"+currentURL);
		//Verify and validate careers link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(careers),"careers link is not present in footer section");
		sfHomePage.clickFooterLink(careers);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(careersURL), "Expected URL should contain "+careersURL+" but actual on UI is"+currentURL);
		//Verify and validate pressroom link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(pressRoom),"pressroom link is not present in footer section");
		sfHomePage.clickFooterLink(pressRoom);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(pressroomURL), "Expected URL should contain "+pressroomURL+" but actual on UI is"+currentURL);
		//Verify and validate direct selling association link in footer section.
		//s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(directSellingAssociation),"Direct selling association link is not present in footer section");
		//sfHomePage.clickFooterLink(directSellingAssociation);
		//currentURL = sfHomePage.getCurrentURL();
		//s_assert.assertTrue(currentURL.contains(directSellingAssociationURL), "Expected URL should contain "+directSellingAssociationURL+" but actual on UI is"+currentURL);
		//Verify and validate terms and conditions link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(termsAndConditions),"terms and conditions link is not present in footer section");
		sfHomePage.clickFooterLink(termsAndConditions);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(termsAndConditionURL), "Expected URL should contain "+termsAndConditionURL+" but actual on UI is"+currentURL);
		//Verify and validate Privacy policy link in footer section.
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(PrivacyPolicy),"Privacy policy link is not present in footer section");
		sfHomePage.clickFooterLink(PrivacyPolicy);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(privacyPolicyURL), "Expected URL should contain "+privacyPolicyURL+" but actual on UI is"+currentURL);
		s_assert.assertAll();
	}
}