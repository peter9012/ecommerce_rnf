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
	@Test(enabled=false)
	public void testVerifyFAQLinkForCategoryUnderShopSkincareOnCorpComAndBizSite_56(){
		String pwsSite = TestConstants.CONSULTANT_PWS;
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

		//Verify FAQ page for soothe regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(sootheLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForSootheFAQ), "Expected URL should contain" +expectedURLForSootheFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Reverse regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(reverseLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForReverseFAQ), "Expected URL should contain" +expectedURLForReverseFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Redefine regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(redefineLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForRedefineFAQ), "Expected URL should contain" +expectedURLForRedefineFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Unblemish regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(unblemishLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForUnblemishFAQ), "Expected URL should contain" +expectedURLForUnblemishFAQ+ ". but actual on UI is"+currentURL);

		//Verify the functionality on PWS site.
		sfHomePage.navigateToUrl(pwsSite);
		//Verify FAQ page for soothe regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(sootheLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForSootheFAQ), "Expected URL should contain" +expectedURLForSootheFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Reverse regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(reverseLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForReverseFAQ), "Expected URL should contain" +expectedURLForReverseFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Redefine regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(redefineLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForRedefineFAQ), "Expected URL should contain" +expectedURLForRedefineFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Unblemish regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(unblemishLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForUnblemishFAQ), "Expected URL should contain" +expectedURLForUnblemishFAQ+ ". but actual on UI is"+currentURL);
		s_assert.assertAll();
	}
}