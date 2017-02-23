package com.rf.test.website.rehabitat.storeFront.homePage.shopSkincareHeaderNavigation;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class CategoryResultsPageTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-57 Access Results on all category
	 * Description : This test validates 'Results' links for all category on corp,com and biz site.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyResultsLinkForCategoryUnderShopSkincareOnCorpComAndBizSite_57(){
		String prefix = pwsPrefix();
		String currentURL = null;
		String sootheLinkUnderShopSkincare = "SOOTHE";
		String reverseLinkUnderShopSkincare = "REVERSE";
		String redefineLinkUnderShopSkincare = "REDEFINE";
		String unblemishLinkUnderShopSkincare = "UNBLEMISH";
		String sublinkNameUnderShopSkincare = "RESULTS";
		String expectedURLForSootheResults = "soothe-results";
		String expectedURLForReverseResults = "reverse-results";
		String expectedURLForRedefineResults = "redefine-results";
		String expectedURLForUnblemishResults = "unblemish-results";

		//Verify FAQ page for soothe regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(sootheLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForSootheResults), "Expected URL should contain" +expectedURLForSootheResults+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Reverse regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(reverseLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForReverseResults), "Expected URL should contain" +expectedURLForReverseResults+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Redefine regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(redefineLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForRedefineResults), "Expected URL should contain" +expectedURLForRedefineResults+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Unblemish regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(unblemishLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForUnblemishResults), "Expected URL should contain" +expectedURLForUnblemishResults+ ". but actual on UI is"+currentURL);
		
		//Verify the functionality on PWS site.
		sfHomePage.navigateToUrl(sfHomePage.getBaseUrl()+"/" +sfHomePage.getCountry() +"/pws/" + prefix);
		//Verify FAQ page for soothe regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(sootheLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForSootheResults), "Expected URL should contain" +expectedURLForSootheResults+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Reverse regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(reverseLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForReverseResults), "Expected URL should contain" +expectedURLForReverseResults+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Redefine regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(redefineLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForRedefineResults), "Expected URL should contain" +expectedURLForRedefineResults+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Unblemish regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(unblemishLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForUnblemishResults), "Expected URL should contain" +expectedURLForUnblemishResults+ ". but actual on UI is"+currentURL);
		s_assert.assertAll();
	}
}