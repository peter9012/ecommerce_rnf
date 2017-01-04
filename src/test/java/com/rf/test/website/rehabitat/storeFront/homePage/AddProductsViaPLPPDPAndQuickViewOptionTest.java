package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AddProductsViaPLPPDPAndQuickViewOptionTest extends StoreFrontWebsiteBaseTest{

	
	
	/***
	 * qTest : TC-113 Add product to cart from PLP
	 * Description : This test validates the product added to cart from 
	 * category listing page for SOOTHE Category.
	 *     
	 */
	@Test
	public void testAddProductToCartFromPLP_113(){
		String category_soothe = "SOOTHE";
		String productName = null;
		String productNameOnCheckoutPopup = null;
		String currentURL = null;
		sfShopSkinCarePage = sfHomePage.clickOnCategoryFromShopSkinCare(category_soothe);
		currentURL = sfShopSkinCarePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_soothe), "Expected URL should contain "+category_soothe+" but actual on UI is"+currentURL);
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage("1");
		sfShopSkinCarePage.addFirstProductToBag();
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		s_assert.assertTrue(sfShopSkinCarePage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),"Added to ypur shopping cart header is not present on checkout popup");
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(productName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + productName + ". Actual : " + productNameOnCheckoutPopup);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart header is not present when redirected after clicking the checkout button from checkout pop up");
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" is not present on the cart page");
		s_assert.assertAll();
	}

	
	/***
	 * qTest : TC-114 Add Product to cart from PDP
	 * Description : This test validates the product added to cart from 
	 * PDP Page for REDEFINE Category Product
	 *     
	 */
	@Test
	public void testAddProductToCartFromPDP_114(){
		String category_redefine = "REDEFINE";
		String productName = null;
		String productNameOnCheckoutPopup = null;
		String currentURL = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickOnCategoryFromShopSkinCare(category_redefine);
		currentURL = sfShopSkinCarePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_redefine), "Expected URL should contain "+category_redefine+" but actual on UI is"+currentURL);
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfFirstProduct();
		productName = sfProductDetailPage.getProductNameFromProductDetailsPage();
		sfProductDetailPage.clickOnAddToCartButton();
		productNameOnCheckoutPopup = sfProductDetailPage.getProductNameFromCheckoutPopup();
		s_assert.assertTrue(sfProductDetailPage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),"Added to ypur shopping cart header is not present on checkout popup");
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(productName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + productName + ". Actual : " + productNameOnCheckoutPopup);
		sfCartPage = sfProductDetailPage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart header is not present when redirected after clicking the checkout button from checkout pop up");
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" is not present on the cart page");
		s_assert.assertAll();
	}
	
}