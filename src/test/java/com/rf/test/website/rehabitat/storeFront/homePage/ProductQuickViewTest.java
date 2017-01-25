package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ProductQuickViewTest extends StoreFrontWebsiteBaseTest{


	/***
	 * qTest: TC-150 Anonymous User: Product Quick View
	 * 
	 * Description: This method verifies she quick view web elements
	 * with  anonymous user 
	 */
	@Test(enabled=true)
	public void testProductQuickViewAnonymous_150(){
		String selectedProductName = null;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		selectedProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isProductImagePresentAtQuickViewPopup(),
				"Product image is not present at Quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isProductNamePresentAtQuickViewPopupAsExpected(selectedProductName),
				"Product name is not present as expected on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityTextFieldPresentAtQuickViewPopup(),
				"Quantity Text field is not presnt on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isAddToCartButtonDisplayedOnQuickViewPopup(),
				"Add to cart Btn is not presnt on quick view poup");		
		s_assert.assertTrue(sfShopSkinCarePage.isPCPerksPromoMessageAndSaveAmountPresentOnQuickViewPopup(),
				"PC Perks Promo message is NOT present on Quick view popup for anonymous user");
		s_assert.assertTrue(sfShopSkinCarePage.isViewProductDetailsLinkDisplayedOnQuickViewPopup(),
				"View product Details link is not presnt on quick view poup");		
		s_assert.assertAll();
	}

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
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_AUTOSHIP, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		selectedProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isProductImagePresentAtQuickViewPopup(),"Product image is not present at Quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isProductNamePresentAtQuickViewPopupAsExpected(selectedProductName),"Product name is not present as expected on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityTextFieldPresentAtQuickViewPopup(),"Quantity Text field is not presnt on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isPricePresentOnQuickViewPopup(),"Special price is not present on Quick view pop up");
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentOnQuickViewPopUp(),"Retail price is not present on Quick view pop up");
		s_assert.assertTrue(sfShopSkinCarePage.isAddToPCPerksButtonPresentAtQuickViewPopup(),"Add to pc perks button is not present on quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isOneTimeOrderButtonPresentAtQuickViewPopup(),"One time order button is not present on quick view popup");
		s_assert.assertFalse(sfShopSkinCarePage.isPCPerksPromoMessageAndSaveAmountPresentOnQuickViewPopup(),"PC Perks Promo message is present on Quick view popup for PC User");
		//sfShopSkinCarePage.clickPCPerksButtonFromQuickViewPopup();
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(TestConstants.ORDER_TYPE_ADHOC);
		s_assert.assertTrue(sfShopSkinCarePage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),"Added to your shipping cart Headline is not present on Checkout popup");
		sfAutoshipCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUpForPCPerks();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL),"Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfAutoshipCartPage.isPCPerksCartHeaderPresentOnCartPage(),"PC Perks Cart Header is not present on Cart Page as expected");
		s_assert.assertTrue((sfAutoshipCartPage.isProductAddedToCartPresentOnCartPage(selectedProductName)),"Product added to PC Perks is not present on Cart page");
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		selectedProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		sfProductDetailPage = sfShopSkinCarePage.clickOnViewProductDetailsLinkOnQuickViewPopup();
		productNameOnPDPPage = sfProductDetailPage.getProductNameFromProductDetailsPage();
		s_assert.assertTrue(productNameOnPDPPage.contains(selectedProductName),"Product Added to cart is different from the Product on PDP page");
		s_assert.assertAll();
	}

	/***
	 * qTest: TC-195 Quick-View- Display PC perks promo message for RC user
	 * Description: This method verifies she quick view web elements
	 * with RC user 
	 */
	@Test(enabled=true)
	public void testProductQuickViewRCUser_195(){
		String selectedProductName = null;
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL_HAVING_ORDER, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		selectedProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isProductImagePresentAtQuickViewPopup(),
				"Product image is not present at Quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isProductNamePresentAtQuickViewPopupAsExpected(selectedProductName),
				"Product name is not present as expected on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityTextFieldPresentAtQuickViewPopup(),
				"Quantity Text field is not presnt on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isAddToCartButtonDisplayedOnQuickViewPopup(),
				"Add to cart Btn is not presnt on quick view poup");		
		s_assert.assertTrue(sfShopSkinCarePage.isPCPerksPromoMessageAndSaveAmountPresentOnQuickViewPopup(),
				"PC Perks Promo message is NOT present on Quick view popup for anonymous user");
		s_assert.assertTrue(sfShopSkinCarePage.isViewProductDetailsLinkDisplayedOnQuickViewPopup(),
				"View product Details link is not presnt on quick view poup");		
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-152 Consultant: Product Quick View
	 * Description : This test validates the flow of options available on Quick view popup for Consultant User.
	 * Note : The test is Incomplete as One time order functionality from quick view popup is not working for consultant user
	 *     
	 */
	@Test(enabled=false)//TODO Incomplete 
	public void testConsultantProductQuickView_152(){
		String selectedProductName = null;
		String productNameOnPDPPage = null;
		String textToAssertInURL = "autoship/cart";
		String currentURL = null;
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		selectedProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isProductImagePresentAtQuickViewPopup(),"Product image is not present at Quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isProductNamePresentAtQuickViewPopupAsExpected(selectedProductName),"Product name is not present as expected on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityTextFieldPresentAtQuickViewPopup(),"Quantity Text field is not presnt on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isPricePresentOnQuickViewPopup(),"Special price is not present on Quick view pop up");
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentOnQuickViewPopUp(),"Retail price is not present on Quick view pop up");
		s_assert.assertTrue(sfShopSkinCarePage.isAddToPCPerksButtonPresentAtQuickViewPopup(),"Add to pc perks button is not present on quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isOneTimeOrderButtonPresentAtQuickViewPopup(),"One time order button is not present on quick view popup");
		s_assert.assertFalse(sfShopSkinCarePage.isPCPerksPromoMessageAndSaveAmountPresentOnQuickViewPopup(),"PC Perks Promo message is present on Quick view popup for PC User");
		//sfShopSkinCarePage.clickPCPerksButtonFromQuickViewPopup();
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(TestConstants.ORDER_TYPE_ADHOC);
		s_assert.assertTrue(sfShopSkinCarePage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),"Added to your shipping cart Headline is not present on Checkout popup");
		sfAutoshipCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUpForPCPerks();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL),"Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfAutoshipCartPage.isPCPerksCartHeaderPresentOnCartPage(),"PC Perks Cart Header is not present on Cart Page as expected");
		s_assert.assertTrue((sfAutoshipCartPage.isProductAddedToCartPresentOnCartPage(selectedProductName)),"Product added to PC Perks is not present on Cart page");
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		selectedProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		sfProductDetailPage = sfShopSkinCarePage.clickOnViewProductDetailsLinkOnQuickViewPopup();
		productNameOnPDPPage = sfProductDetailPage.getProductNameFromProductDetailsPage();
		s_assert.assertTrue(productNameOnPDPPage.contains(selectedProductName),"Product Added to cart is different from the Product on PDP page");
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-153 Adjust the product qty on the quick view
	 * Description : This test validates update qty on quick view popup for consultant user
	 * Note : The test is Incomplete as One time order functionality from quick view popup is not working for consultant user
	 *     
	 */
	@Test(enabled=false)//TODO Incomplete 
	public void testConsultantProductQuickViewQtyUpdate_153(){
		String selectedProductName = null;
		String productNameOnPDPPage = null;
		String textToAssertInURL = "autoship/cart";
		String currentURL = null;
		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		selectedProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isProductImagePresentAtQuickViewPopup(),"Product image is not present at Quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isProductNamePresentAtQuickViewPopupAsExpected(selectedProductName),"Product name is not present as expected on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityTextFieldPresentAtQuickViewPopup(),"Quantity Text field is not presnt on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isPricePresentOnQuickViewPopup(),"Special price is not present on Quick view pop up");
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentOnQuickViewPopUp(),"Retail price is not present on Quick view pop up");
		s_assert.assertTrue(sfShopSkinCarePage.isAddToPCPerksButtonPresentAtQuickViewPopup(),"Add to pc perks button is not present on quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isOneTimeOrderButtonPresentAtQuickViewPopup(),"One time order button is not present on quick view popup");
		s_assert.assertFalse(sfShopSkinCarePage.isPCPerksPromoMessageAndSaveAmountPresentOnQuickViewPopup(),"PC Perks Promo message is present on Quick view popup for PC User");
		//sfShopSkinCarePage.clickPCPerksButtonFromQuickViewPopup();
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(TestConstants.ORDER_TYPE_ADHOC);
		s_assert.assertTrue(sfShopSkinCarePage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),"Added to your shipping cart Headline is not present on Checkout popup");
		sfAutoshipCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUpForPCPerks();
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-194 Product qty field shouldn't allow any characters except a digits on the quick view
	 * Description : This test validates that special characters is not allowed in Quantity text field on quick view pop up
	 * Note : Warning message is not coming yet. Need to add a assertion for the same when functionality start working fine.
	 *     
	 */

	@Test(enabled=false)//Unable to add product to adhoc cart from quick view.
	public void testProductQtyFieldShouldnotAllowAnyCharactersExceptADigitsOnTheQuickView_194(){
		String currentURL = null;
		String selectedProductName = null;
		String specialChar = "@";
		String previousProductCount=null;
		String updateProductCount =null;
		String newProductCount = null;
		String expectedURL="/cart";

		//Login to application.
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_AUTOSHIP, password);
		//Get product count on adhoc cart page.
		sfHomePage.clickWelcomeDropdown();
		previousProductCount = sfHomePage.getNumberOfItemFromMiniCart();
		updateProductCount = sfHomePage.updateQuantityByOne(previousProductCount);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		selectedProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isProductNamePresentAtQuickViewPopupAsExpected(selectedProductName),"Product name is not present as expected on quick view poup");
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityTextFieldPresentAtQuickViewPopup(),"Quantity Text field is not present on quick view poup");
		sfShopSkinCarePage.enterQuantityOfProductOnQuickViewPopup(specialChar);
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		currentURL = sfCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.endsWith(expectedURL),"Expected URL should contain "+expectedURL+" but actual on UI is "+currentURL);
		newProductCount = sfHomePage.getNumberOfItemFromMiniCart();
		s_assert.assertTrue(newProductCount.equals(updateProductCount),"Expected product count on adhoc cart contain "+updateProductCount+" but actual on UI is "+newProductCount);
		s_assert.assertAll();
	}

}
