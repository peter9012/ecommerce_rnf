package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ProductsAndCartDetailsTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-61 Sort/Order
	 * 
	 * Description : This test validates the sorting of product base on price filter applied
	 * and also sorting for different category based on price filter.
	 * 				
	 */ 
	@Test 
	public void testSortProductBaseOnPriceFilter_61(){
		String allProduct = "ALL PRODUCTS";
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProduct);
		sfShopSkinCarePage.productPriceFilterLowToHigh();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterLowToHighAppliedSuccessfully(),"Selected Price filter 'Low To High' is not applied to product successfully");
		sfShopSkinCarePage.productPriceFilterHighToLow();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterHighToLowAppliedSuccessfully(),"Selected Price filter 'High To Low' is not applied to product successfully");
		String categoryName = sfShopSkinCarePage.refineProductByCategoryAndReturnCategoryName();
		sfShopSkinCarePage.productPriceFilterLowToHighSelect();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterLowToHighAppliedSuccessfully(),"Selected Price filter 'Low To High' is not applied to product successfully for category"+categoryName);
		sfShopSkinCarePage.productPriceFilterHighToLowSelect();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterHighToLowAppliedSuccessfully(),"Selected Price filter 'High To Low' is not applied to product successfully for category"+categoryName);
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-62 Sort/Order Removal by default option section
	 * 
	 * Description : This test validates the sorting of product base on price filter applied
	 * and also Unsorting of product when filter removed.
	 * 				
	 */ 
	@Test //Blocked as no default value for price filter present.
	public void testSortAndUnsortProductBaseOnPriceFilter_62(){
		String allProduct = "ALL PRODUCTS";
		
		sfShopSkinCarePage=sfHomePage.navigateToShopSkincareLink(allProduct);
		sfShopSkinCarePage.productPriceFilterLowToHigh();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterLowToHighAppliedSuccessfully(),"Selected Price filter 'Low To High' is not applied to product successfully");
		sfShopSkinCarePage.productPriceFilterHighToLow();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterHighToLowAppliedSuccessfully(),"Selected Price filter 'High To Low' is not applied to product successfully");
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-63 Sort/Order Removal by clicking Clear All link
	 * 
	 * Description : This test validates the sorting of product base on price filter applied
	 * and also Unsorting of product when filter removed by clicking clear all link.
	 * 				
	 */ 
	@Test //Blocked as no clear all link is present.
	public void testSortAndUnsortProductBaseOnPriceFilterApplied_63(){
		String allProduct = "ALL PRODUCTS";
		
		sfShopSkinCarePage=sfHomePage.navigateToShopSkincareLink(allProduct);
		sfShopSkinCarePage.productPriceFilterLowToHigh();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterLowToHighAppliedSuccessfully(),"Selected Price filter 'Low To High' is not applied to product successfully");
		sfShopSkinCarePage.productPriceFilterHighToLow();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterHighToLowAppliedSuccessfully(),"Selected Price filter 'High To Low' is not applied to product successfully");
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-64 Product Detail Page- Product Tabs
	 * 
	 * Description : This test validates the product details page and various tabs on product detail page.
	 * 
	 * 				
	 */
	@Test 
	public void testVerifyProductDetailPageAndTabsOnProductDetailPage_64(){
		String allProduct = "ALL PRODUCTS";
		String productName = null;
		
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProduct);
		productName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfFirstProduct();
		s_assert.assertTrue(sfProductDetailPage.getProductNameFromProductDetailsPage().contains(productName),"User is not redirecting to product details page after clicking on product name");
		s_assert.assertTrue(sfProductDetailPage.isAddToCartButtonIsPresentAtProductDetailsPage(),"Add to Cart button not present on product detail page");
		//Verify all 3 tabs under product detail page.
		s_assert.assertTrue(sfProductDetailPage.isDescriptionTabPresentAtProductDetailsPage(),"Description tab is not present on product detail page");
		s_assert.assertTrue(sfProductDetailPage.isUsageNoteTabPresentAtProductDetailsPage(),"User note tab is not present on product detail page");
		s_assert.assertTrue(sfProductDetailPage.isIngredientsTabPresentAtProductDetailsPage(),"Ingredients tab is not present on product detail page");
		//Click each tab one by one and verify content.
		s_assert.assertAll();
	}
}
