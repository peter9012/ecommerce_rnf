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

	@Test(enabled=false)
	public void testUserEnterPartOfAProductNameOrDescriptionAndHitsEnter_204(){
		String productName = "60 Day Supply";
		String partialProductName = "Day";

		//Searching with  product name
		sfHomePage.clickSearchIcon();
		sfHomePage.searchEntityAndHitEnter(productName);
		s_assert.assertTrue(sfHomePage.isSearchResultsTextAppearedAsExpected(productName),"Search Result text is not appeared as expected");
		s_assert.assertTrue(sfHomePage.isExpecetedProductPresentInSearchResults(productName),"Expected Search result is not found in the Search results for : " + productName);
		//Searching with Partial product Name.
		sfHomePage.clickRodanAndFieldsLogo();
		sfHomePage.clickSearchIcon();
		sfHomePage.searchEntityAndHitEnter(partialProductName);
		s_assert.assertTrue(sfHomePage.isProductSearchResultsPresent(),"Product search results for category Redefine not present.");
		s_assert.assertTrue(sfHomePage.isExpecetedProductPresentInSearchResults(productName),"Expected Search result is not found in the Search results for partial name : " + partialProductName);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-205 User enters a phrase from a product description in the search criteria and selects enter
	 * Description : This test validates the functionality of searching a product through description via search field 
	 * and verify expected search results.
	 *     
	 */

	@Test(enabled=false)
	public void testUserEnterPartOfAProductDescriptionAndHitsEnter_205(){
		String productDesc = "AMP MD™ System Refill";

		//Searching with  product description.
		sfHomePage.clickSearchIcon();
		sfHomePage.searchEntityAndHitEnter(productDesc);
		s_assert.assertTrue(sfHomePage.isSearchResultsTextAppearedAsExpected(productDesc),"Search Result text is not appeared as expected");
		s_assert.assertTrue(sfHomePage.isExpecetedProductPresentInSearchResults(productDesc),"Expected Search result is not found in the Search results by product description for : " + productDesc);
		s_assert.assertTrue(sfHomePage.isProductSearchResultsPresent(),"Product search results for category Redefine not present.");
		s_assert.assertAll();
	}


}
