package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AccessCategoryLandingPageFromHeaderTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-11 Access Category landing pages under shop skincare section
	 * Description : This test validates category, Category landing pages, 
	 * under shop skincare section 
	 *     
	 */
	@Test
	public void testAccessCategoryLandingPagesUnderShopSkincareSection_11(){
		String category_Unblemish = "UNBLEMISH";
		String category_Featured = "FEATURED";
		String category_Essentials = "ESSENTIALS";
		String category_Reverse = "REVERSE";
		String category_Enhancement = "ENHANCEMENT";
		String category_Soothe = "SOOTHE";
		String category_Redefine = "REDEFINE";
		String topNavigationLink = TestConstants.SHOP_SKINCARE; 
		String currentURL = null; 
		//Verify Category links
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Unblemish), category_Unblemish +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Featured), category_Featured +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Essentials), category_Essentials +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Reverse), category_Reverse +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Enhancement), category_Enhancement +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Soothe), category_Soothe +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Redefine), category_Redefine +"category is not present under shop skincare");
		//Verify Category landing pages
		//Unblemish
		sfHomePage.clickCategoryLink(category_Unblemish);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Unblemish), "Expected URL should contain "+category_Unblemish+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//FEATURED
		sfHomePage.clickCategoryLink(category_Featured);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Featured), "Expected URL should contain "+category_Featured+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//ESSENTIALS
		sfHomePage.clickCategoryLink(category_Essentials);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Essentials), "Expected URL should contain "+category_Essentials+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//REVERSE
		sfHomePage.clickCategoryLink(category_Reverse);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Reverse), "Expected URL should contain "+category_Reverse+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//ENHANCEMENT
		sfHomePage.clickCategoryLink(category_Enhancement);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Enhancement), "Expected URL should contain "+category_Enhancement+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//SOOTHE
		sfHomePage.clickCategoryLink(category_Soothe);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Soothe), "Expected URL should contain "+category_Soothe+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//REDEFINE
		sfHomePage.clickCategoryLink(category_Redefine);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Redefine), "Expected URL should contain "+category_Redefine+" but actual on UI is"+currentURL);
		s_assert.assertAll();
	}
}