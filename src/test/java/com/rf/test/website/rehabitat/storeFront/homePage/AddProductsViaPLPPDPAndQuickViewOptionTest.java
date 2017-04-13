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
	@Test(enabled=true)
	public void testAddProductToCartFromPLP_113(){
		String category_soothe = "SOOTHE";
		String productName = null;
		String productNameOnCheckoutPopup = null;
		String currentURL = null;
		sfShopSkinCarePage = sfHomePage.clickOnCategoryFromShopSkinCare(category_soothe);
		currentURL = sfShopSkinCarePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_soothe), "Expected URL should contain "+category_soothe+" but actual on UI is"+currentURL);
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
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
	@Test(enabled=true)
	public void testAddProductToCartFromPDP_114(){
		String category_redefine = "REDEFINE";
		String productName = null;
		String productNameOnCheckoutPopup = null;
		String currentURL = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickOnCategoryFromShopSkinCare(category_redefine);
		currentURL = sfShopSkinCarePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_redefine), "Expected URL should contain "+category_redefine+" but actual on UI is"+currentURL);
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProduct(TestConstants.PRODUCT_NUMBER);
		productName = sfProductDetailPage.getProductNameFromProductDetailsPage();
		sfProductDetailPage.addProductToCartFromProductDetailPageAfterLogin(TestConstants.ORDER_TYPE_ADHOC);
		productNameOnCheckoutPopup = sfProductDetailPage.getProductNameFromCheckoutPopup();
		s_assert.assertTrue(sfProductDetailPage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),"Added to ypur shopping cart header is not present on checkout popup");
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(productName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + productName + ". Actual : " + productNameOnCheckoutPopup);
		sfCartPage = sfProductDetailPage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart header is not present when redirected after clicking the checkout button from checkout pop up");
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" is not present on the cart page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-117 Add product to PC Perks Autoship Cart from Quick View
	 * Description : This test validates the flow of adding product to PC Perks autoship for PC User
	 *     
	 */
	@Test(enabled=true)
	public void testAddProductToPCPerksAutoshipCartFromQuickView_117(){
		String selectedProductName = null;
		String textToAssertInURL = "autoship/cart";
		String currentURL = null;
		//Login to application.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),  password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfHomePage.clickSearchIcon();
		sfHomePage.searchEntityAndHitEnter(validProductId);
		selectedProductName = validProductName;
		sfShopSkinCarePage.clickOnQuickViewLinkThroughProductId(validProductId);
		s_assert.assertTrue(sfShopSkinCarePage.isProductNamePresentAtQuickViewPopupAsExpected(selectedProductName),
				"Product name is not present as expected on quick view poup");
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(TestConstants.ORDER_TYPE_PC_PERKS);
		s_assert.assertTrue(sfShopSkinCarePage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),
				"Added to your shipping cart Headline is not present on Checkout popup");
		sfAutoshipCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUpForPCPerks();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(textToAssertInURL),
				"Expected URL should contain "+textToAssertInURL+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfAutoshipCartPage.isPCPerksCartHeaderPresentOnCartPage(),
				"PC Perks Cart Header is not present on Cart Page as expected");
		s_assert.assertTrue((sfAutoshipCartPage.isProductPresentInAutoShipCart(selectedProductName)),
				"Product added to PC Perks is not present on Autoship Cart page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-116 Add product to CRP Autoship Cart from PLP
	 * Description : This test validates the product added to cart from 
	 * PLP page and verify at autoship cart
	 */
	@Test(enabled=true)
	public void testAddProductToCRPAutoshipCartFromPLP_116(){
		String productName = null;
		String productQuantity = null;
		String updatedQuantity = null;
		String updatedQuantityFromUI = null;
		boolean isProductPresent = false;
		String prefix = pwsPrefix();
		sfHomePage.navigateToUrl(sfHomePage.getCurrentURL() + "/pws/" + prefix);
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		productName = validProductName;
		sfAutoshipCartPage = sfHomePage.clickAutoshipLink();
		if(sfAutoshipCartPage.isProductPresentInAutoShipCart(productName)){
			productQuantity = sfAutoshipCartPage.getQtyOfProductFromAutoShipCart(productName); 
			isProductPresent = true;
		}
		sfAutoshipCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfAutoshipCartPage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_CRP,validProductId);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutPopupDisplayed(),"Expected checkout popup is not displayed while adding product from PLP");
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		if(isProductPresent){
			updatedQuantity = sfAutoshipCartPage.updateQuantityByOne(productQuantity).trim();
			updatedQuantityFromUI = sfAutoshipCartPage.getQtyOfProductFromAutoShipCart(productName);
			s_assert.assertTrue(updatedQuantityFromUI.equals(updatedQuantity), "Added item from PLP is not present into cart");
		}else{
			s_assert.assertTrue(sfAutoshipCartPage.isProductPresentInAutoShipCart(productName), "Added item from PLP is not present into cart");
		}
		s_assert.assertAll();
	}

}