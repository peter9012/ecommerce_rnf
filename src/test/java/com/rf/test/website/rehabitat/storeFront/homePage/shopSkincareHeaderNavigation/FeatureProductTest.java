package com.rf.test.website.rehabitat.storeFront.homePage.shopSkincareHeaderNavigation;

import org.testng.annotations.Test;

import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class FeatureProductTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-81 Access Featured Products From Shop Skincare Section In Header Navigation
	 * Description : This test validates 'Featured products'under shopskincare category on corp,com and biz site.
	 * 
	 *     
	 */
	@Test(enabled=false) //TODO Incomplete for com and biz site.
	public void testVerifyFeaturedProductCategoryUnderShopSkincareOnCorpComAndBizSite_81(){
		String currentURL = null;
		String featuredRegimenURL = "/c/featured";
		String category_Featured = "FEATURED";
		//Click featured link under shopskincare.
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(category_Featured);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(sfShopSkinCarePage.isProductsDisplayedOnPage() && currentURL.contains(featuredRegimenURL),"Expected featured products not displayed for selected category or Expected page URL"+featuredRegimenURL+" But actual on UI is: "+currentURL);
		s_assert.assertAll();
	}
}