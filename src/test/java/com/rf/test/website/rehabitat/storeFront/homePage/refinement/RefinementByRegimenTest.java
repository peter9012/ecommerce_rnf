package com.rf.test.website.rehabitat.storeFront.homePage.refinement;

import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class RefinementByRegimenTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-118 Refinement by Regimen
	 * Description : This test validates refinmen
	 *     
	 */
	@Test(enabled=true)
	public void testRefinmentByRegimen_118(){
		String categoryUnblemish = "UNBLEMISH";
		String categoryFeatured = "FEATURED";
		String categoryEssentials = "ESSENTIALS";
		String categoryReverse = "REVERSE";
		//String categoryEnhancement = "ENHANCEMENT";
		String categorySoothe = "SOOTHE";
		String categoryRedefine = "REDEFINE";
		String categoryConsultantOnly = "Consultant Only";
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_AUTOSHIP,  password,true);
		sfHomePage.clickSearchIcon();
		sfShopSkinCarePage = sfHomePage.searchEntityAndHitEnter(TestConstants.SHOP_SKINCARE);
		s_assert.assertTrue(sfShopSkinCarePage.isSearchResultsTextAppearedAsExpected(TestConstants.SHOP_SKINCARE), "search result page is not present");
		sfShopSkinCarePage.clickShopByCategoryDD();
		s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryUnblemish), categoryUnblemish+" is not visible in shop by category DD");
		s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryFeatured), categoryFeatured+" is not visible in shop by category DD");
		s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryEssentials), categoryEssentials+" is not visible in shop by category DD");
		s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryReverse), categoryReverse+" is not visible in shop by category DD");
		s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categorySoothe), categorySoothe+" is not visible in shop by category DD");
		s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryRedefine), categoryRedefine+" is not visible in shop by category DD");
		s_assert.assertFalse(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryConsultantOnly), categoryConsultantOnly+" is visible in shop by category DD for PC");
		sfShopSkinCarePage.clickShopByCategoryDD();
		String categoryName = sfShopSkinCarePage.refineProductByCategoryAndReturnCategoryName().toLowerCase();
		String currentURL = sfShopSkinCarePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(categoryName) && sfHomePage.isTextPresent(categoryName),"Current url should contain category name is "+categoryName+"but actual on UI is "+currentURL+" and category details page is not present");
		s_assert.assertAll();
	}
	
}