package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class CategoryLandingPageTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-94 Redefine Category Landing page
	 * Description : This test validates redefine category landing page
	 * and add to bag button is present or not 
	 *     
	 */
	@Test(enabled=true)
	public void testRedefineCategoryLandingPage_94(){
		String category_Redefine = "REDEFINE";
		String currentURL = null;
		sfHomePage.clickCategoryLink(category_Redefine);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Redefine), "Expected URL should contain "+category_Redefine+" but actual on UI is"+currentURL);
		s_assert.assertTrue(sfHomePage.isProductsDisplayedOnPage(), "Add to cart is not present for first product on Category Landing page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-91 Reverse Category Landing page
	 * Description : This test validates reverse category landing page
	 * and add to bag button is present or not 
	 *     
	 */
	@Test(enabled=true)
	public void testReverseCategoryLandingPage_91(){
		String category_Reverse = "REVERSE";
		String currentURL = null;
		sfHomePage.clickCategoryLink(category_Reverse);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Reverse), "Expected URL should contain "+category_Reverse+" but actual on UI is"+currentURL);
		s_assert.assertTrue(sfHomePage.isProductsDisplayedOnPage(), "Add to cart is not present for first product on Category Landing page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-93 Unblemish Category Landing page
	 * Description : This test validates unblemish category landing page
	 * and add to bag button is present or not 
	 *     
	 */
	@Test(enabled=true)
	public void testUnblemishCategoryLandingPage_93(){
		String category_Unblemish = "UNBLEMISH";
		String currentURL = null;
		sfHomePage.clickCategoryLink(category_Unblemish);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Unblemish), "Expected URL should contain "+category_Unblemish+" but actual on UI is"+currentURL);
		s_assert.assertTrue(sfHomePage.isProductsDisplayedOnPage(), "Add to cart is not present for first product on Category Landing page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-92 Soothe Category Landing page
	 * Description : This test validates soothe category landing page
	 * and add to bag button is present or not 
	 *     
	 */
	@Test(enabled=true)
	public void testSootheCategoryLandingPage_92(){
		String category_Soothe = "SOOTHE";
		String currentURL = null;
		sfHomePage.clickCategoryLink(category_Soothe);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Soothe), "Expected URL should contain "+category_Soothe+" but actual on UI is"+currentURL);
		s_assert.assertTrue(sfHomePage.isProductsDisplayedOnPage(), "Add to cart is not present for first product on Category Landing page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-89 Essentials Category Landing page
	 * Description : This test validates essentials category landing page
	 * and add to bag button is present or not 
	 *     
	 */
	@Test(enabled=true)
	public void testEssentialsCategoryLandingPage_89(){
		String category_Essentials = "ESSENTIALS";
		String currentURL = null;
		sfHomePage.clickCategoryLink(category_Essentials);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Essentials), "Expected URL should contain "+category_Essentials+" but actual on UI is"+currentURL);
		s_assert.assertTrue(sfHomePage.isProductsDisplayedOnPage(), "Add to cart is not present for first product on Category Landing page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-96 Enhancements Category Landing page
	 * Description : This test validates Enhancements category landing page
	 * and add to bag button is present or not 
	 *     
	 */
	@Test(enabled=true)
	public void testEnhancementsCategoryLandingPage_96(){
		String category_Enhancements = "ENHANCEMENT";
		String currentURL = null;
		sfHomePage.clickCategoryLink(category_Enhancements);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Enhancements), "Expected URL should contain "+category_Enhancements+" but actual on UI is"+currentURL);
		s_assert.assertTrue(sfHomePage.isProductsDisplayedOnPage(), "Add to cart is not present for first product on Category Landing page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-95 All Products Landing page
	 * Description : This test validates all products link's landing page
	 * and add to bag button is present or not 
	 *     
	 */
	@Test(enabled=true)
	public void testAllProductsLandingPage_95(){
		String currentURL = null;
		String allProducts = "shopskincare";

		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(allProducts), "Expected URL should contain "+allProducts+" but actual on UI is"+currentURL);
		s_assert.assertTrue(sfHomePage.isProductsDisplayedOnPage(), "Add to cart is not present for first product on Category Landing page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-97 Featured Category Landing page
	 * Description : This test validates Featured category landing page
	 * and add to bag button is present or not 
	 *     
	 */
	@Test(enabled=false)
	public void testFeaturedCategoryLandingPage_97(){
		String category_Featured = "FEATURED";
		String currentURL = null;
		sfHomePage.clickCategoryLink(category_Featured);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Featured), "Expected URL should contain "+category_Featured+" but actual on UI is"+currentURL);
		s_assert.assertTrue(sfHomePage.isProductsDisplayedOnPage(), "Add to cart is not present for first product on Category Landing page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-90 "Consultant only" category option appears for the consultant user
	 * Description : //TODO
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantOnlyCategoryAppearsForTheConsultantUser_90(){
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),  password,true);
		sfHomePage.mouseHoverOn(TestConstants.SHOP_SKINCARE);
		s_assert.assertFalse(sfHomePage.isConsultantOnlyProductsLinkDisplayed(), "Consultant Only Link should NOT be present for PC user");
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),  password,true);
		sfHomePage.mouseHoverOn(TestConstants.SHOP_SKINCARE);
		s_assert.assertFalse(sfHomePage.isConsultantOnlyProductsLinkDisplayed(), "Consultant Only Link should NOT be present for RC user");
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfHomePage.mouseHoverOn(TestConstants.SHOP_SKINCARE);
		s_assert.assertTrue(sfHomePage.isConsultantOnlyProductsLinkDisplayed(), "Consultant Only Link should NOT be present for PC user");
		sfShopSkinCarePage = sfHomePage.clickConsultantOnlyProductsLink();
		s_assert.assertTrue(sfShopSkinCarePage.getCurrentURL().contains("consultant-only"), "Consultant only page has not displayed");
		sfShopSkinCarePage.isAddToCartDDOptionsDisplayed(TestConstants.USER_TYPE_CONSULTANT);
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isPricePresentOnQuickViewPopup(),"Price is not present for Product on Quick view popup for consultant Only products");
		s_assert.assertAll();
	}

}