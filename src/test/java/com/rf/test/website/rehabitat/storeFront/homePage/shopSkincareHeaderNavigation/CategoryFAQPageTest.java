package com.rf.test.website.rehabitat.storeFront.homePage.shopSkincareHeaderNavigation;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class CategoryFAQPageTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-56 Access FAQs on all category
	 * Description : This test validates 'FAQs' links for all category on corp,com and biz site.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyFAQLinkForCategoryUnderShopSkincareOnCorpComAndBizSite_56(){
		String prefix = pwsPrefix();
		String currentURL = null;
		String sootheLinkUnderShopSkincare = "SOOTHE";
		String reverseLinkUnderShopSkincare = "REVERSE";
		String redefineLinkUnderShopSkincare = "REDEFINE";
		String unblemishLinkUnderShopSkincare = "UNBLEMISH";
		String sublinkNameUnderShopSkincare = "FAQS";
		String expectedURLForSootheFAQ = "soothe-faq";
		String expectedURLForReverseFAQ = "reverse-faq";
		String expectedURLForRedefineFAQ = "redefine-faq";
		String expectedURLForUnblemishFAQ = "unblemish-faq";
		String expectedURLForSootheQuestions = "soothe-questions";
		String expectedURLForReverseQuestions = "reverse-questions";
		String expectedURLForRedefineQuestion = "redefine-questions";
		String expectedURLForUnblemishQuestions = "unblemish-questions";

		//Verify FAQ page for soothe regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(sootheLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForSootheFAQ) || currentURL.contains(expectedURLForSootheQuestions) , "Expected URL should contain" +expectedURLForSootheFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Reverse regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(reverseLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForReverseFAQ) || currentURL.contains(expectedURLForReverseQuestions), "Expected URL should contain" +expectedURLForReverseFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Redefine regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(redefineLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForRedefineFAQ) || currentURL.contains(expectedURLForRedefineQuestion), "Expected URL should contain" +expectedURLForRedefineFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Unblemish regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(unblemishLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForUnblemishFAQ) || currentURL.contains(expectedURLForUnblemishQuestions), "Expected URL should contain" +expectedURLForUnblemishFAQ+ ". but actual on UI is"+currentURL);

		//Verify the functionality on PWS site.
		sfHomePage.navigateToUrl(sfHomePage.getCurrentURL() + "/pws/" + prefix);
		//Verify FAQ page for soothe regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(sootheLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForSootheFAQ) || currentURL.contains(expectedURLForSootheQuestions), "Expected URL should contain" +expectedURLForSootheFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Reverse regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(reverseLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForReverseFAQ) || currentURL.contains(expectedURLForReverseQuestions), "Expected URL should contain" +expectedURLForReverseFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Redefine regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(redefineLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForRedefineFAQ) || currentURL.contains(expectedURLForRedefineQuestion), "Expected URL should contain" +expectedURLForRedefineFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Unblemish regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(unblemishLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForUnblemishFAQ) || currentURL.contains(expectedURLForUnblemishQuestions), "Expected URL should contain" +expectedURLForUnblemishFAQ+ ". but actual on UI is"+currentURL);
		s_assert.assertAll();
	}
}