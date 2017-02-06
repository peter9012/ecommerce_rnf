package com.rf.test.website.rehabitat.storeFront.homePage.refinement;

import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class RefinementWithMultipleRegimensTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-122 Refinement with multiple Regimens
	 * Description : This test validates two or more than 2 regimens
	 * applied successfully or not
	 *     
	 */
	@Test(enabled=true)
	public void testRefinmentWithMultipleRegimens_122(){
		String categoryUnblemish = "UNBLEMISH";
		//String categoryFeatured = "FEATURED";
		String categoryEssentials = "ESSENTIALS";
		String categoryReverse = "REVERSE";
		String categorySoothe = "SOOTHE";
		String categoryRedefine = "REDEFINE";
		String categoryConsultantOnly = "Consultant Only";
		String categoryName1 = null;
		String categoryName2 = null;
		String appliedFilterName = null;
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_AUTOSHIP,  password,true);
		sfHomePage.clickSearchIcon();
		sfShopSkinCarePage = sfHomePage.searchEntityAndHitEnter("Redefine");
		s_assert.assertTrue(sfShopSkinCarePage.isSearchResultsTextAppearedAsExpected("Redefine"), "search result page is not present");
		sfShopSkinCarePage.clickShopByCategoryDD();
		s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryUnblemish), categoryUnblemish+" is not visible in shop by category DD");
		s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryReverse), categoryReverse+" is not visible in shop by category DD");
		s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categorySoothe), categorySoothe+" is not visible in shop by category DD");
		s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryRedefine), categoryRedefine+" is not visible in shop by category DD");
		s_assert.assertFalse(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryConsultantOnly), categoryConsultantOnly+" is visible in shop by category DD for PC");
		//s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryFeatured), categoryFeatured+" is not visible in shop by category DD");
		s_assert.assertTrue(sfShopSkinCarePage.isCategoryNameVisibleInShopByCategoryDD(categoryEssentials), categoryEssentials+" is not visible in shop by category DD");
		sfShopSkinCarePage.clickShopByCategoryDD();
		categoryName1 = sfShopSkinCarePage.refineShopByCategoryAndReturnCategoryName().toLowerCase().trim();
		categoryName2 = sfShopSkinCarePage.refineShopByCategoryAndReturnUniqueCategoryName(categoryName1).toLowerCase().trim();
		appliedFilterName = sfShopSkinCarePage.getAppliedFilterName(1).toLowerCase();
		s_assert.assertTrue(appliedFilterName.contains(categoryName1),"Expected applied category name is "+categoryName1+" but actual on UI is"+categoryName1);
		appliedFilterName = sfShopSkinCarePage.getAppliedFilterName(2).toLowerCase();
		s_assert.assertTrue(appliedFilterName.contains(categoryName2),"Expected applied category name is "+categoryName2+" but actual on UI is"+categoryName1);
		s_assert.assertAll();
	}
}