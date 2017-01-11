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
	@Test(enabled=true)
	public void testAccessCategoryLandingPagesUnderShopSkincareSection_11(){
		String categoryUnblemish = "UNBLEMISH";
		String categoryFeatured = "FEATURED";
		String categoryEssentials = "ESSENTIALS";
		String categoryReverse = "REVERSE";
		String categoryEnhancement = "ENHANCEMENT";
		String categorySoothe = "SOOTHE";
		String categoryRedefine = "REDEFINE";
		String topNavigationLink = TestConstants.SHOP_SKINCARE; 
		String currentURL = null; 
		//Verify Category links
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,categoryUnblemish), categoryUnblemish +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,categoryFeatured), categoryFeatured +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,categoryEssentials), categoryEssentials +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,categoryReverse), categoryReverse +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,categoryEnhancement), categoryEnhancement +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,categorySoothe), categorySoothe +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,categoryRedefine), categoryRedefine +"category is not present under shop skincare");
		//Verify Category landing pages
		//Unblemish
		sfHomePage.navigateToShopSkincareLink(categoryUnblemish);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(categoryUnblemish), "Expected URL should contain "+categoryUnblemish+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//FEATURED
		sfHomePage.navigateToShopSkincareLink(categoryFeatured);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(categoryFeatured), "Expected URL should contain "+categoryFeatured+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//ESSENTIALS
		sfHomePage.navigateToShopSkincareLink(categoryEssentials);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(categoryEssentials), "Expected URL should contain "+categoryEssentials+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//REVERSE
		sfHomePage.navigateToShopSkincareLink(categoryReverse);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(categoryReverse), "Expected URL should contain "+categoryReverse+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//ENHANCEMENT
		sfHomePage.navigateToShopSkincareLink(categoryEnhancement);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(categoryEnhancement), "Expected URL should contain "+categoryEnhancement+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//SOOTHE
		sfHomePage.navigateToShopSkincareLink(categorySoothe);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(categorySoothe), "Expected URL should contain "+categorySoothe+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//REDEFINE
		sfHomePage.navigateToShopSkincareLink(categoryRedefine);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(categoryRedefine), "Expected URL should contain "+categoryRedefine+" but actual on UI is"+currentURL);
		s_assert.assertAll();
	}
}