package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ProductQuickViewTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-151 PC: Product Quick View
	 * Description : This test validates the flow of options available on Quick view popup for PC User.
	 * Note : The test is Incomplete as One time order functionality from quick view popup is not working for pc user
	 *     
	 */

	@Test(enabled=false)//TODO Incomplete 
	public void testPCProductQuickView_151(){
		String selectedProductName = null;
		String productNameOnPDPPage = null;
		String textToAssertInURL = "autoship/cart";
		String currentURL = null;
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		selectedProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isProductImagePresentAtQuickViewPopup(),
				"Product image is not present at Quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isProductNamePresentAtQuickViewPopupAsExpected(selectedProductName),
				"Product name is not present as expected on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityTextFieldPresentAtQuickViewPopup(),
				"Quantity Text field is not presnt on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isPricePresentOnQuickViewPopup(),
				"Special price is not present on Quick view pop up");
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentOnQuickViewPopUp(),
				"Retail price is not present on Quick view pop up");
		s_assert.assertTrue(sfShopSkinCarePage.isAddToPCPerksButtonPresentAtQuickViewPopup(),
				"Add to pc perks button is not present on quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isOneTimeOrderButtonPresentAtQuickViewPopup(),
				"One time order button is not present on quick view popup");
		s_assert.assertFalse(sfShopSkinCarePage.isPCPerksPromoMessagePresentOnQuickViewPopup(),
				"PC Perks Promo message is present on Quick view popup for PC User");
		sfShopSkinCarePage.clickPCPerksButtonFromQuickViewPopup();
		s_assert.assertTrue(sfShopSkinCarePage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),
				"Added to your shipping cart Headline is not present on Checkout popup");
		sfAutoshipCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUpForPCPerks();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL),
				"Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfAutoshipCartPage.isPCPerksCartHeaderPresentOnCartPage(),
				"PC Perks Cart Header is not present on Cart Page as expected");
		s_assert.assertTrue((sfAutoshipCartPage.isProductAddedToCartPresentOnCartPage(selectedProductName)),
				"Product added to PC Perks is not present on Cart page");
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfHomePage.clickAllProducts();
		selectedProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		sfProductDetailPage = sfShopSkinCarePage.clickOnViewProductDetailsLinkOnQuickViewPopup();
		productNameOnPDPPage = sfProductDetailPage.getProductNameFromProductDetailsPage();
		s_assert.assertTrue(productNameOnPDPPage.contains(selectedProductName),
				"Product Added to cart is different from the Product on PDP page");
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-194 Product qty field shouldn't allow any characters except a digits on the quick view
	 * Description : This test validates that special characters is not allowed in Quantity text field on quick view pop up
	 * Note : Warning message is not coming yet. Need to add a assertion for the same when functionality start working fine.
	 *     
	 */

	@Test(enabled=false) //TODO Incomplete 
	public void testProductQtyFieldShouldnotAllowAnyCharactersExceptADigitsOnTheQuickView_194(){
		String selectedProductName = null;
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		selectedProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isProductNamePresentAtQuickViewPopupAsExpected(selectedProductName),
				"Product name is not present as expected on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityTextFieldPresentAtQuickViewPopup(),
				"Quantity Text field is not present on quick view poup");
		sfShopSkinCarePage.enterQuantityOfProductOnQuickViewPopup();
		sfShopSkinCarePage.clickAddToCartButtonOnQuickViewPopup();
		// Need to add assertion for Warning message
		s_assert.assertAll();
	}
	

}
