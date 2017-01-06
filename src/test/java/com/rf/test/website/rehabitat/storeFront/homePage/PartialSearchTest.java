package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class PartialSearchTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-204 User enters part of a product name or description (i.e 4 letters of a 6 letter word) and hits enter
	 * Description : This test validates the functionality of searching a product through search field and verify expected search results.
	 * 
	 *     
	 */

	@Test
	public void testUserEnterspartOfAProductNameOrDescriptionAndHitsEnter_204(){
		String productName = null;
		String entityToSearch = null;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage("1");
		entityToSearch = sfShopSkinCarePage.getSplittedProductNameForSearchPurpose(productName);

		//Searching with partial product name
		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		sfHomePage.clickSearchIcon();
		sfHomePage.searchEntityAndHitEnter(entityToSearch);
		s_assert.assertTrue(sfHomePage.isSearchResultsTextAppearedAsExpected(entityToSearch),"Search Result text is not appeared as expected");
		s_assert.assertTrue(sfHomePage.isExpecetedProductPresentInSearchResults(productName),"Expected Search result is not found in the Search results for : " + entityToSearch);

		/*//Searching with Whole Product name
		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		sfHomePage.clickSearchIcon();
		sfHomePage.searchEntityAndHitEnter(productName);
		s_assert.assertTrue(sfHomePage.isSearchResultsTextAppearedAsExpected(entityToSearch),"Search Result text is not appeared as expected");
		s_assert.assertTrue(sfHomePage.isExpecetedProductPresentInSearchResults(productName),"Expected Search result is not found in the Search results");*/

		s_assert.assertAll();
	}
	

}
